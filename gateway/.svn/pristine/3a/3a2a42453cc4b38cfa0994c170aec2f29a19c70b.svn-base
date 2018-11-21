import {Component, OnInit, OnDestroy } from '@angular/core';
import { ReportsService } from './reports.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Response } from '@angular/http';
import { PageEvent } from '@angular/material';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper,pageSizeOptionsList } from '../../shared';
import { AgingBucketDetails, AgingBucket } from './reports.model';
import { Subscription,Subject } from 'rxjs/Rx';
import { CommonService } from '../common.service';

@Component({
    selector: 'jhi-bucket-list',
    templateUrl: './agingBuckets.component.html'
})

export class BucketListComponent implements OnInit, OnDestroy{
    private unsubscribe: Subject<void> = new Subject();
    length: number = 0;
    pageSize: number = 0;
    page: number = 0;
    pageSizeOptions = pageSizeOptionsList;
    bucketListSearch: string='';
    pageEvent: PageEvent = new PageEvent();
    bucketsList: AgingBucket[]=[];
    
    sortOrder:string='Ascending';
    sortCol:string;
    
    constructor(
        private reportsService: ReportsService,
        private commonService: CommonService,
        private datePipe: DatePipe,
        private router: Router
    ){
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageIndex=this.page;
        this.pageEvent.pageSize=this.pageSize;
        this.loadBucketList();
    }

    loadSortedBucketList(event){
        this.sortOrder = 'Ascending';
        if (event.order < 1){
            this.sortOrder = 'Descending';
        }
        this.sortCol = event.field;
        this.loadBucketList();
    }

    searchForBuckets(){
        this.pageEvent.pageIndex=0;
        this.loadBucketList();
    }

    loadBucketList(){
        this.reportsService.getAllBucketsByPagination(this.pageEvent.pageIndex,this.pageEvent.pageSize,this.bucketListSearch).takeUntil(this.unsubscribe).subscribe((res: Response) => {    //To load all buckets again
            this.bucketsList = res.json();
            this.length = +res.headers.get('x-count');
            this.bucketsList.forEach((item)=>{
                this.reportsService.convertItemFromServer(item);
            })
        },
        (resErr: Response) => {
          this.onError('Failed to load buckets information!')
        });
    }

    goToDetail(event){
        if(event.data.id){
            this.router.navigate(['/reports', {outlets: {'content': ['aging-bucket-detail',event.data.id,'view']}}]);
        }
    }

    ngOnInit(){

    }

    ngOnDestroy(){
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.commonService.destroyNotification(); 
    }

    private onError(errorMessage) {
        this.commonService.error(
            'Error!',
            errorMessage
        )
    }
}