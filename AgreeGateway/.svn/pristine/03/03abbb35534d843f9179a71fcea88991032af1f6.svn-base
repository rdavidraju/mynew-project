import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute ,Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager ,JhiDateUtils } from 'ng-jhipster';

import { SourceConnectionDetails, SourceProfile } from './source-connection-details.model';
import { SourceConnectionDetailsService } from './source-connection-details.service';
import { SelectItem } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {previousRoute} from '../../breadcrumb.component';
import { NotificationsService } from 'angular2-notifications-lite';
import { Response } from '@angular/http';
import { SourceProfilesService } from '../source-profiles/source-profiles.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { SourceProfiles, SourceProfileswithConnections } from '../source-profiles/source-profiles.model';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage';
import { currentRoute } from '../../breadcrumb.component';
import {OrderBy} from "../orderByPipe";
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-source-connection-details-detail',
    templateUrl: './source-connection-details-detail.component.html'
})

export class SourceConnectionDetailsDetailComponent implements OnInit, OnDestroy {

    // sourceConnectionDetails: SourceConnectionDetails;
    today=new Date();  
    startDateChange: boolean = false;
    endDateChange: boolean = false;
    sourceConnectionDetails = new SourceConnectionDetails();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isViewOnly = false;
    isCreateNew = false;
    isEdit = false;
    hideProfile = false;
    paramid: any;
    connectionTypesList = [];
    finalCols = [];
    displayColumns = [];
    parentPath = [] ;
    previousPath: any;
    isView = false;
    sourceConnectionDetailsList: any = [];
    duplicateConName = false;
    checkData = false;
    sourceProfiles: SourceProfileswithConnections[];
    sourceProfileList: SourceProfile[];
    presentPath:any;
    eligibleProfile:boolean = false;
    sourceProfileName:any = '';
    hideSaveButton:boolean = false;
    isVisibleA: boolean = false;   
    testCon:string=null;
    constructor(
        private config: NgbDatepickerConfig,
        private eventManager: JhiEventManager,
        private sourceConnectionDetailsService: SourceConnectionDetailsService,
        private route: ActivatedRoute,
        private dateUtils: JhiDateUtils,
        private router: Router,
        private notificationsService: NotificationsService,
        private sourceProfilesService: SourceProfilesService,
        private commonService: CommonService,
        private sessionStorage: SessionStorageService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }
     changeMinDate() {
        this.config.minDate = this.sourceConnectionDetails.startDate;
    }

    resetMinDate() {
       this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    private onError(error) {
       // this.alertService.error(error.message, null, null);
        this.notificationsService.error(
                                'Warning!',
                                error.message 
        )
    }
    fromProfile:boolean = false;
    // this method check pressed key is numeric or not
    keyHandlerCount(code){
        if (code > 31 && (code < 48 || code > 57))
            return false;
        return true;
    }
    ngOnInit() {
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) + 'px'
        });
        this.sourceConnectionDetailsService.fetchConnections().subscribe((res: any) => {
            this.connectionTypesList = res;
            this.parentPath = previousRoute;
            this.subscription = this.route.params.subscribe((params) => {
                this.previousPath = this.route.snapshot.url;
                let url = this.route.snapshot.url.map(segment => segment.path).join('/');
                this.presentPath = this.route.snapshot.url;
                if (params['id']) {
                    this.paramid = params['id'];
                    if (this.sessionStorage.retrieve('sourceConnectionDetails')) {
                        this.sourceConnectionDetails = this.sessionStorage.retrieve('sourceConnectionDetails');
                        this.finalCols = this.sessionStorage.retrieve('connectionCols');
                        //this.sourceConnectionDetails['sourceProfileList'] = this.finalCols;
                        this.getDisplayColumns();
                       // this.getDisplayColumns1();
                        this.sessionStorage.clear('sourceConnectionDetails');
                        this.sessionStorage.clear('connectionCols');
                        let obj={
                            "connectionId": this.sourceConnectionDetails.id
                        }
                      //  console.log('obj ' + JSON.stringify(obj));
                        this.sourceConnectionDetailsService.getAllSourceProfiles(obj).subscribe(
                                    ( res: ResponseWrapper ) => {
                                        this.sourceProfiles = res.json();
                                        
                                        
                                    },
                                    ( res: ResponseWrapper ) => this.onError( res.json() )
                                );
                       
                        
                    }
                    this.getConnectionsDetails(this.paramid);
                    if (url.endsWith('edit')) {
                        this.isEdit = true;
                    } else {
                        this.isViewOnly = true;
                    }
                    
                } else {
                    this.isCreateNew = true;
                    this.isVisibleA = true;
                    if (this.sessionStorage.retrieve('recentRoutes')['profilesRoute']) {
                        this.hideProfile = true;
                    }
                    if (this.sessionStorage.retrieve('sourceConnectionDetails')) {
                        this.sourceConnectionDetails = this.sessionStorage.retrieve('sourceConnectionDetails');
                        this.finalCols = this.sessionStorage.retrieve('connectionCols');
                        //this.sourceConnectionDetails['sourceProfileList'] = this.finalCols;
                        this.sessionStorage.clear('sourceConnectionDetails');
                        this.sessionStorage.clear('connectionCols');
                        this.fromProfile = true;
                      //  this.getDisplayColumns();
                    }
                    this.sourceProfileList = [{
                        sourceProfileName: this.sessionStorage.retrieve('sourceProfileName')
                    }];
                    this.sessionStorage.clear('sourceProfileName');
                    this.sourceConnectionDetails['sourceProfileList'] = this.sourceProfileList;
                    let obj = {
                        "connectionId": 0
                    }
                    this.sourceConnectionDetailsService.getAllSourceProfiles(obj).subscribe(
                        (res: ResponseWrapper) => {
                            this.sourceProfiles = res.json();
                            console.log("sourceProfiles id " + JSON.stringify(this.sourceProfiles));
                            console.log('this.hideProfile ' + this.hideProfile);
                            if(this.fromProfile){
                                this.sourceProfileList[0].sourceProfileName = this.sourceProfiles[0].sourceProfileName;
                                this.sourceProfileList[0].description = this.sourceProfiles[0].description;
                                this.sourceProfileList[0].enabledFlag = this.sourceProfiles[0].enabledFlag;
                                //this.selectedProfile(this.sourceProfiles[0].sourceProfileName,0); 
                            }
                                        
                            if (this.sourceProfiles.length > 0) {
                                this.eligibleProfile = false;
                            } else {
                                this.eligibleProfile = true;
                            }
                        },
                        (res: ResponseWrapper) => this.onError(res.json())
                    );
                }
                
            });

        });
        
        
        
        this.registerChangeInSourceConnectionDetails();
    }

    //If Start Date Entered Apply Class
    startEndDateClass(){
        if(this.sourceConnectionDetails.startDate != null){
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }else{
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }
    
    /* sourceProList(obj){
         fetching source profile 
        
        
         this.sourceConnectionDetailsService.getAllSourceProfilesList(obj.startDate,obj.endDate).subscribe(
            ( res: ResponseWrapper ) => {
                this.sourceProfiles = res.json();
            },
            ( res: ResponseWrapper ) => this.onError( res.json() )
        ); 
    } */
    
    selectedProfile(val, ind){
        for(var i=0;i<this.sourceProfileList.length;i++){
            if(i == ind){
                this.sourceProfileList[ind] = val;
            }
        }
    }
    deleteColumn(ind){
     //   console.log('ind to del :' + JSON.stringify(ind));
        if(this.sourceProfileList.length>1){
            this.sourceProfileList.splice(ind,1);
            /* this.notificationsService.error(
                                'Success!',
                                'Selected associate profile successfully deleted' 
                        ) */
        }else{
            
            this.notificationsService.error(
                                'Warning!',
                                'Atleast one associate profile should exist' 
                        )
        }
    }
    addProfileRow(){
        let count=0;
        for(var i=0;i<this.sourceProfileList.length;i++){
            if(this.sourceProfileList[i].sourceProfileName == '' || this.sourceProfileList[i].sourceProfileName == null || this.sourceProfileList[i].sourceProfileName == undefined){
                count++;
            }
        }
        if(count>0){
            this.notificationsService.error(
                                'Warning!',
                                'Fill mandatory fields to add another profile' 
                        )
        }else{
            const myobj = {
                            sourceProfileName : ''
                        };
            this.sourceProfileList.push(myobj);
        }
    }
    getAllProf(stDate:any, enDate:any){
       // console.log('stDate ' + stDate + ' enDate ' + enDate);
        if(this.presentPath == "source-connection-details-new"){
         let obj={
                    "connectionId": 0,
                    "startDate": stDate,
                    "endDate": enDate
                }
       // console.log('obj ' + JSON.stringify(obj));
         this.sourceConnectionDetailsService.getAllSourceProfiles(obj).subscribe(
                        ( res: ResponseWrapper ) => {
                            this.sourceProfiles = res.json();
                          //  console.log("sourceProfiles all " + JSON.stringify(this.sourceProfiles));
                        },
                        ( res: ResponseWrapper ) => this.onError( res.json() )
                    );    
        }
    }
    
    /* Function to check connection name duplicates */
    checkConnectionName(val){
        var count = 0;
        //this.sourceConnectionDetailsList = [];
        this.sourceConnectionDetailsService.connectionListsFunc().subscribe((res:any)=>{
            this.sourceConnectionDetailsList = res;
            this.sourceConnectionDetailsList.forEach(element => {
                if(element.name.toLowerCase( ) == val.toLowerCase( )){
                        count++;
                    }
                });
                if(count > 0){
                    this.duplicateConName = true;
                }else{
                    this.duplicateConName = false;
                }
             });
    }

    getConnectionsDetails(paramid : any){
        //this.sourceProfileList = [];
        this.sourceConnectionDetailsService.find(paramid).subscribe((res:Response) => {
            this.sourceConnectionDetails = res;
            this.today =this.sourceConnectionDetails.startDate;
         //   console.log('this.sourceConnectionDetails ' + JSON.stringify(this.sourceConnectionDetails));
            let obj={
                    "connectionId": this.sourceConnectionDetails.id
                }
          //  console.log('obj ' + JSON.stringify(obj));
            this.sourceConnectionDetailsService.getAllSourceProfiles(obj).subscribe(
                        ( res: ResponseWrapper ) => {
                            this.sourceProfiles = res.json();
                          //  console.log("sourceProfiles id " + JSON.stringify(this.sourceProfiles));
                        },
                        ( res: ResponseWrapper ) => this.onError( res.json() )
                    );
          //  console.log('connection details ' + JSON.stringify(this.sourceConnectionDetails));
            this.getDisplayColumns();
            this.sourceProfileList = this.sourceConnectionDetails['sourceProfileList'];
        });
        
    }

    load(id) {
        this.sourceConnectionDetailsService.find(id).subscribe((sourceConnectionDetails) => {
            this.sourceConnectionDetails = sourceConnectionDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSourceConnectionDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sourceConnectionDetailsListModification',
            (response) => this.load(this.sourceConnectionDetails.id)
        );
    }
    saveSourceConnection() {
        if(this.isCreateNew)
        {
            if(this.startDateChange){
                this.sourceConnectionDetails.startDate=new Date(this.sourceConnectionDetails.startDate.getTime() + 86400000);
            }
            if(this.endDateChange){
                this.sourceConnectionDetails.endDate=new Date(this.sourceConnectionDetails.endDate.getTime() + 86400000);
            }
        }
        else if(this.isEdit)
        {
            if(this.sourceConnectionDetails.startDate.getTime())
            this.sourceConnectionDetails.startDate=new Date(this.sourceConnectionDetails.startDate.getTime() + 86400000);
            if(this.sourceConnectionDetails.endDate.getTime())
            this.sourceConnectionDetails.endDate=new Date(this.sourceConnectionDetails.endDate.getTime() + 86400000);
        }
        
        for (var i = 0; i < this.finalCols.length; i++) {
            if (this.sourceConnectionDetails != null) {
                if (this.finalCols[i].code == ('protocol')) {
                    this.sourceConnectionDetails.protocol = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('userName')) {
                    this.sourceConnectionDetails.userName = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('password')) {
                    this.sourceConnectionDetails.password = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('callBackUrl')) {
                    this.sourceConnectionDetails.callBackUrl = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('accessToken')) {
                    this.sourceConnectionDetails.accessToken = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('host')) {
                    this.sourceConnectionDetails.host = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('port')) {
                    this.sourceConnectionDetails.port = this.finalCols[i].value;
                }
                else if (this.finalCols[i].code == ('clientKey')) {
                    this.sourceConnectionDetails.clientKey = this.finalCols[i].value;
                }

            }
        }
      //  console.log('this.sourceConnectionDetails ::' + JSON.stringify(this.sourceConnectionDetails));
        if(this.sourceConnectionDetails.sourceProfileList){
            var valueArr = this.sourceConnectionDetails.sourceProfileList.map(function(item){ return item.id });
            var isDuplicate = valueArr.some(function(item, idx){ 
                return valueArr.indexOf(item) != idx 
            });
          //  console.log('this.sourceConnectionDetails ::' + JSON.stringify(this.sourceConnectionDetails));
        }else{
            isDuplicate = false;
        }
        
      //  console.log('isDuplicate ' + isDuplicate);
        if(isDuplicate != true){
            
            this.sourceConnectionDetails['sourceProfileList'] = this.sourceProfileList;
            for(var i=0;i<this.sourceConnectionDetails.sourceProfileList.length;i++){
                    if(this.sourceConnectionDetails.sourceProfileList[i].sourceProfileName == ''){
                        this.sourceConnectionDetails.sourceProfileList.splice(this.sourceConnectionDetails.sourceProfileList[i],1);
                    }
            }

            let link: any = '';
            if (this.sourceConnectionDetails.id !== undefined) {
             //   console.log('sourceConnectionDetails to update ::' + JSON.stringify(this.sourceConnectionDetails));
                this.sourceConnectionDetailsService.update(this.sourceConnectionDetails)
                    .subscribe((sourceConnectionDetails) => {
                        this.sourceConnectionDetails = sourceConnectionDetails;
                        if (this.sourceConnectionDetails.id) {
                            link = ['/source-connection-details', { outlets: { 'content': this.sourceConnectionDetails.id + '/details' } }];
                            this.router.navigate(link);
                            this.notificationsService.success(
                                'Success',
                                'Connection details updated successfully'
                            )
                            this.hideSaveButton = false;
                        }

                    });
            } else {
                
                this.sourceConnectionDetails['enabledFlag'] = true;
             //   console.log('save source connection details :' + JSON.stringify(this.sourceConnectionDetails));
                this.sourceConnectionDetailsService.create(this.sourceConnectionDetails)
                    .subscribe((res: Response) => {
                        this.sourceConnectionDetails = res;
                        this.notificationsService.success(
                            'Success',
                            'New connection successfully created'
                        )
                        this.hideSaveButton = false;
                        let recentRoutes = this.sessionStorage.retrieve('recentRoutes');
                        let keysByIndex = Object.keys(recentRoutes);    // TO get all keys into one array
                        if (this.sourceConnectionDetails.id) {
                            link = ['/source-connection-details', { outlets: { 'content': this.sourceConnectionDetails.id + '/details' } }];
                            
                            if (recentRoutes == undefined || keysByIndex.length < 1) {

                                if (this.isEdit) {
                                    this.isEdit = false;
                                }
                                if (this.isCreateNew) {
                                    this.isCreateNew = false;
                                }
                            this.isViewOnly = true;

                            this.router.navigate(link);
                        } else {
                         //   console.log('Normal Save3');
                            this.sessionStorage.store('connectionId',this.sourceConnectionDetails.id);
                            this.sessionStorage.store('connectionType',this.sourceConnectionDetails.connectionType);
                            this.sessionStorage.store('connectionCols',this.finalCols);
                            let routerParent = recentRoutes[keysByIndex[keysByIndex.length-1]].parentRoute;
                            let routerChild = recentRoutes[keysByIndex[keysByIndex.length-1]].childRoute;
                            delete recentRoutes[keysByIndex[keysByIndex.length-1]];
                            this.sessionStorage.store('recentRoutes',recentRoutes);
                            if(routerChild.search('/')==-1){
                                this.router.navigate([`/${routerParent}`, { outlets: { 'content': [routerChild] } }]);
                            }else{
                                let routeLink = ['/'+routerParent, { outlets: { 'content': routerChild.split('/')[0] + '/'+routerChild.split('/')[1] } }];
                                this.router.navigate(routeLink);
                            }
                        }
                        }
                    });
            }
        }else{
            this.notificationsService.error(
                            'Warning',
                            'Duplicate Associate Profile Selected'
            )
        }
        
    }
    getDisplayColumns() {
      //  console.log('this.sourceConnectionDetails ' + JSON.stringify(this.sourceConnectionDetails));
        if ( this.sourceConnectionDetails.connectionType ) {
              this.connectionTypesList.forEach(con => {
                    if(con.connectionTypeCode === this.sourceConnectionDetails.connectionType)
                    {
                        this.finalCols = con.displayColumns;
                    }
                    else
                        {
                        }
                });
            this.finalCols.forEach( item => {
                        item['value'] = this.sourceConnectionDetails[item['code']];
                    });
        }
             //   console.log('this.finalCols ' + JSON.stringify(this.finalCols));
    }

    getDisplayColumns1() {
     //   console.log('this.sourceConnectionDetails ' + JSON.stringify(this.sourceConnectionDetails));
        if ( this.sourceConnectionDetails.connectionType ) {
              this.connectionTypesList.forEach(con => {
                    if(con.connectionTypeCode === this.sourceConnectionDetails.connectionType)
                    {
                        this.finalCols = con.displayColumns;
                    }
                    else
                        {
                        }
                });
              this.testCon=null;
            this.finalCols.forEach( item => {
                        item['value'] = '';
                    });
        }
            //    console.log('this.finalCols ' + JSON.stringify(this.finalCols));
    }

    validationMsg(){
        if(this.duplicateConName == true){
            this.notificationsService.error(
                     'Warning!',
                     'Connection Name already exists' 
            )
        }else{
            this.notificationsService.error(
                     'Warning!',
                     'Fill Mandatory Fields' 
            )
        }
        var i;
        
    }
    showPassword(input: any): any {
        if(this.isView === false){
            this.isView = true;
        }else{
            this.isView = false;
        }
        input.type = input.type === 'text' ?  'password' : 'text';
    }

    selectedAssociateProfile(){
      //  console.log('this.sourceProfileName ' + this.sourceProfileName);
        let count = 0;
        this.sourceProfileList.forEach(element => {
            if(element.sourceProfileName == '-1'){
                count++;
            }
        });
        if(count>0){
         //   console.log('inside -1');
            let recentRoutes = this.sessionStorage.retrieve('recentRoutes');    // Get existing routes object from session storage
            if(recentRoutes == undefined){  // If object is undefined create new object
                recentRoutes = {};
            }
            Object.assign(recentRoutes,{connectionProfileRoute: {    // Assign new item to existing routes json
                parentRoute: currentRoute.parent.snapshot.url.map(segment => segment.path).join("/"),
                childRoute: currentRoute.snapshot.url.map(segment => segment.path).join("/")}});
            this.sessionStorage.store('recentRoutes',recentRoutes);  // Store object into session storage
            this.sessionStorage.store('connectionCols',this.finalCols);
            let keysByIndex = Object.keys(recentRoutes);    // TO get all keys into one array
            this.sessionStorage.store('sourceConnectionDetails',this.sourceConnectionDetails);  // Store current filled Info
            this.router.navigate(['/source-profiles', {outlets: { 'content': ['source-profiles-new']}}]);
        }
    }
    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }
    testConnection()
    {
        for (var i = 0; i < this.finalCols.length; i++) {
            if (this.sourceConnectionDetails != null) {
                if (this.finalCols[i].code == ('protocol')) {
                    this.sourceConnectionDetails.protocol = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('userName')) {
                    this.sourceConnectionDetails.userName = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('password')) {
                    this.sourceConnectionDetails.password = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('callBackUrl')) {
                    this.sourceConnectionDetails.callBackUrl = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('accessToken')) {
                    this.sourceConnectionDetails.accessToken = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('host')) {
                    this.sourceConnectionDetails.host = this.finalCols[i].value;
                } else if (this.finalCols[i].code == ('port')) {
                    this.sourceConnectionDetails.port = this.finalCols[i].value;
                }
                else if (this.finalCols[i].code == ('clientKey')) {
                    this.sourceConnectionDetails.clientKey = this.finalCols[i].value;
                }

            }
        }
        this.sourceConnectionDetailsService.testConenction(this.sourceConnectionDetails).subscribe((res: any) => {
            let resp : any =res;
        console.log('tested connection with '+JSON.stringify(res));
        if(res && res.status && res.status.toString().search('Success') != -1)
            {
            this.testCon='success';
            this.notificationsService.success(
                    'Test connection is successful',
                    ''
                );
            }
        else
            {
            this.testCon='failed';
            this.notificationsService.error(
                    'Invalid connection',
                    res.message+''
                );
            }
        
        });
    }
    blockSpecialChar(e) {
        const allowedRegex = /[0-9\/]/g;

        if (!e.key.match(allowedRegex)) {
            e.preventDefault();
        }
        
        var k = (e.which) ? e.which : e.keyCode;
        return ((k > 64 && k < 91) || (k > 96 && k < 123) || k == 8 || (k>47 && k<57));
        }
    keyDown(event) {
        const allowedRegex = /[0-9\/]/g;

         if (!event.key.match(allowedRegex)) {
             event.preventDefault();
         }
     }
     close()
     {
         let recentRoutes = this.sessionStorage.retrieve('recentRoutes');   
         let keysByIndex = Object.keys(recentRoutes); 
         if (recentRoutes == undefined || keysByIndex.length < 1){
             if(this.isCreateNew)
             {
                this.router.navigate(['/source-connection-details', {outlets: {'content': ['source-connection-detailst']}}]);
             }
             else if(this.isEdit)
             {
                this.router.navigate(['/source-connection-details', {outlets: {'content': [this.sourceConnectionDetails.id]+'/details'}}]);
             }
            
         }else{
             let routerParent = recentRoutes[keysByIndex[keysByIndex.length-1]].parentRoute;
             let routerChild = recentRoutes[keysByIndex[keysByIndex.length-1]].childRoute;
             console.log('recentRoutes before'+JSON.stringify(recentRoutes));
             delete recentRoutes[keysByIndex[keysByIndex.length-1]];
             console.log('recentRoutes after'+JSON.stringify(recentRoutes));
             this.sessionStorage.store('recentRoutes',recentRoutes);
             if(routerChild.search('/')==-1){
                 this.router.navigate([`/${routerParent}`, { outlets: { 'content': [routerChild] } }]);
             }else{
                 let routeLink = ['/'+routerParent, { outlets: { 'content': routerChild.split('/')[0] + '/'+routerChild.split('/')[1] } }];
                 this.router.navigate(routeLink);
             }
         }
     }
     startDateChanged(dt:Date){
        if(this.sourceConnectionDetails.endDate){
            if(this.sourceConnectionDetails.endDate<this.sourceConnectionDetails.startDate){
                this.sourceConnectionDetails.endDate=this.sourceConnectionDetails.startDate;
            }
        }
    }
    }
