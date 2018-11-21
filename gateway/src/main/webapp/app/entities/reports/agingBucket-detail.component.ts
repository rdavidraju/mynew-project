import { Component, OnInit, OnDestroy } from '@angular/core';
import { DatePipe, Location } from '@angular/common';
import { Subscription,Subject } from 'rxjs/Rx';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportsService } from './reports.service';
import { AgingBucket, AgingBucketDetails } from './reports.model';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { CommonService } from '../common.service';

@Component({
    selector: 'jhi-bucket-detail',
    templateUrl: './agingBucket-detail.component.html'
})

export class BucketDetailComponent implements OnInit, OnDestroy{
    private unsubscribe: Subject<void> = new Subject();
    isViewOnly:boolean=false;
    isCreateNew:boolean=false;
    isEdit:boolean=false;
    routeSub: any;
    currentBucket = new AgingBucket();
    bucketsList: AgingBucketDetails[]=[];
    bucketTypes: LookUpCode[] =[];
    cmpHeight: any;
    today=new Date();
    
    constructor(
        private reportsService: ReportsService,
        private datePipe: DatePipe,
        private route: ActivatedRoute,
        private cs: CommonService,
        private router: Router,
        private location: Location
    ){
        this.reportsService.getLookupValues('BUCKET_TYPE').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
            this.bucketTypes = res;
        });
    }

    ngOnInit(){
        this.cmpHeight ='calc(100vh - 300px)';
        this.routeSub = this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            if(params['id']){
                this.reportsService.getBucketDef(params['id']).takeUntil(this.unsubscribe).subscribe((res: AgingBucket) => {
                    this.bucketsList = res.bucketDetDataList;
                    this.currentBucket=res;
                },
                (res: Response) => {
                  this.onError('Failed to load bucket definition!');
                });
                if(params['mode'] && params['mode']=='view'){
                    this.isViewOnly=true;
                }else if(params['mode'] && params['mode']=='edit'){
                    this.isEdit=true;
                }
            }else if(params['mode'] && params['mode']=='new'){
                this.isCreateNew=true;
                this.currentBucket.startDate=new Date();
                this.currentBucket.enabledFlag=true;
            }else{
                this.router.navigate(['/reports',{outlets:{'content':['aging-bucket-list']}}]);
            }
        });
    }

    // this method check pressed key is numeric or not
    keyHandlerCount(code){
        if (code > 31 && (code < 48 || code > 57)){
            return false;
        }
        return true;
    }

    closeWindow(){
        if(this.isEdit){
            this.isViewOnly=true;
            this.isEdit=false;
            this.router.navigate(['/reports', {outlets: {'content': ['aging-bucket-detail',this.currentBucket.id,'view']}}]);
        }else {
            this.router.navigate(['/reports', {outlets: {'content': ['aging-bucket-list']}}]);
        }
    }

    enableEdit(){
        this.isViewOnly=false;
        this.isEdit=true;
        let url = this.router.createUrlTree(['/reports', {outlets: {'content': ['aging-bucket-detail',this.currentBucket.id,'edit']}}]).toString();
        this.location.go(url);
    }

    copyBucket(){
        let url = this.router.createUrlTree(['/reports', {outlets: {'content': ['aging-bucket-detail',this.currentBucket.id,'createNew']}}]).toString();
        this.currentBucket.id=undefined;
        this.bucketsList.forEach((item)=>{
            item.id=undefined;
        });
        this.currentBucket.bucketName=this.currentBucket.bucketName+' - (Copy)';
        this.currentBucket.enabledFlag=true;
        this.isCreateNew=true;
        this.isViewOnly=false;
        this.isEdit=false;
        this.location.go(url);
    }

    //Delete bucket
    deleteBucket(index: number){
        this.bucketsList.splice(index,1);
        this.currentBucket.count--;
        if(this.bucketsList.length==0){
            this.currentBucket.count++;
            this.bucketsList.push(new AgingBucketDetails());
        }
    }

    //It will work if bucket from/to value changed
    checkBucketValue(ind:number,Val:number,isFrom:Boolean){
        if(ind==0){                                                                         //if first bucket
            if(this.bucketsList[ind].fromValue != undefined){
                if(this.bucketsList[ind].toValue<=this.bucketsList[ind].fromValue){
                    this.bucketsList[ind].toValue=undefined;
                    this.cs.error('Oops!','Please give a meaningfull \'From\' and \'To\' values at Seq.No: '+(ind+1));
                    return;   
                }
            }else{
                return;
            }
        }else if(isFrom){
            if(!this.bucketsList[ind-1].toValue || this.bucketsList[ind-1].toValue>=this.bucketsList[ind].fromValue){
                this.bucketsList[ind].fromValue=undefined;
                this.cs.error('Oops!','Please give a meaningfull \'From\' and \'To\' values at Seq.No: '+(ind+1));
                return;
            }
        }else{
            if(this.bucketsList[ind].toValue<=this.bucketsList[ind].fromValue){
                this.bucketsList[ind].toValue=undefined;
                this.cs.error('Oops!','Please give a meaningfull \'From\' and \'To\' values at Seq.No: '+(ind+1));
            }
        }
    }

    updateBucketCount(count:number){
        if(count==0){
            this.bucketsList=[];
            this.bucketsList.push(new AgingBucketDetails());
            this.currentBucket.count=1;
            count=1;
        }
        if(this.bucketsList.length>count){                                          //if user reduced the count
            this.bucketsList.splice(count,this.bucketsList.length-count);
        }
        for(let i=this.bucketsList.length; i<count; i++){        //if user increased the count
            this.bucketsList.push(new AgingBucketDetails());
        }
        this.currentBucket.count=count;
    }

    checkBucketName() {
        if (!this.currentBucket.bucketName || this.currentBucket.bucketName.length < 1){
            this.cs.info('Oops...!', 'Please give bucket name!');
            return;
        }
        this.reportsService.checkBucketNameAvailable(this.currentBucket.bucketName).takeUntil(this.unsubscribe).subscribe((res: number) => {
            if (res > 0) {
                this.currentBucket.bucketName = '';
                this.cs.error('Oops...!', 'Sorry, Bucket name is not available.');
            }
        },
        (res: Response) => {
          this.onError('Failed to check bucket name availability!')
        });
    }

    //Save bucket info
    saveBucketsInfo(){
        if (!this.currentBucket.bucketName || this.currentBucket.bucketName.length < 1){
            this.cs.info('Oops...!', 'Please give bucket name!');
            return;
        }
        if(this.isCreateNew){
            this.reportsService.checkBucketNameAvailable(this.currentBucket.bucketName).takeUntil(this.unsubscribe).subscribe((res: number) => {
                if (res > 0) {
                    this.currentBucket.bucketName = '';
                    this.cs.error('Oops...!', 'Sorry, Bucket name is not available.');
                }else{
                    this.currentBucket.bucketDetDataList=this.bucketsList;
                    this.reportsService.postBucketDef(this.currentBucket).takeUntil(this.unsubscribe).subscribe((res) => {
                        if (res.status === 200) {
                            this.cs.success('Saved!',this.currentBucket.bucketName + ' saved successfully!')
                            this.router.navigate(['/reports',{outlets:{'content':['aging-bucket-list']}}]);
                        }else {
                            this.cs.error('Oops...!',this.currentBucket.bucketName + ' not saved! Try again.')
                        }
                    },
                    (res: Response) => {
                      this.onError('Failed to save bucket definition!')
                    });
                }
            },
            (res: Response) => {
              this.onError('Failed to check bucket name availability!')
            });
        }else{
            this.currentBucket.bucketDetDataList = this.bucketsList;
            this.reportsService.postBucketDef(this.currentBucket).takeUntil(this.unsubscribe).subscribe((res) => {
                if (res.status === 200) {
                    this.cs.success('Saved!', this.currentBucket.bucketName + ' saved successfully!')
                    this.router.navigate(['/reports', { outlets: { 'content': ['aging-bucket-list'] } }]);
                } else {
                    this.cs.error('Oops...!', this.currentBucket.bucketName + ' not saved! Try again.')
                }
            },
            (res: Response) => {
                this.onError('Failed to save bucket definition!')
            });
        }
        
    }

    ngOnDestroy(){
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.cs.destroyNotification(); 
    }

    //Show error messages
    private onError(errorMessage) {
        this.cs.error(
            'Error!',
            errorMessage
        )
    }
}