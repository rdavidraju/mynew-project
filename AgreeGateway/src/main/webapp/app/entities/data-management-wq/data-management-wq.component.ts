 import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { PageEvent } from '@angular/material';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { CommonService } from '../../entities/common.service';
import { DataManagementWqService } from './data-management-wq.service';
import { SourceProfilesService } from '../source-profiles/source-profiles.service';
import { FileTemplatesService } from '../file-templates/file-templates.service';
import { SourceProfileFileAssignmentsService } from '../source-profile-file-assignments/source-profile-file-assignments.service';
import { ConfirmDialogModule, ConfirmationService } from 'primeng/primeng';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { JobDetailsService } from '../jobs/job-details.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { SessionStorageService } from 'ng2-webstorage';
import { FileSelectDirective, FileDropDirective, FileUploader, FileItem } from 'ng2-file-upload/ng2-file-upload';
import {SchedulingModel,Parameters} from '../scheduling/scheduling.component';
import {FileTemplatesEditComponent} from '../file-templates/file-templates-edit.component';
import { DatePipe } from '@angular/common';
declare var $: any;
declare var jQuery: any;
declare var gapi: any;
declare var google: any;
declare var Dropbox: any;
declare var dropboxfile: any;
//declare var reason:any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'confirm-action-modal',
    templateUrl: 'confirm-action-modal.html'
})
export class ConfirmActionModalDialog {
    constructor(
        public dialogRef: MdDialogRef<ConfirmActionModalDialog>,
        public dialog: MdDialog,
        private spaservice: SourceProfileFileAssignmentsService,
        private dataManagementWqService: DataManagementWqService,
        @Inject(MD_DIALOG_DATA) public data: any) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}

const URL = '';
@Component({
    selector: 'jhi-file-template-lines',
    templateUrl: './data-management-wq.component.html',
    providers: [ConfirmationService],
    animations: [
        trigger('flyInOut', [
            state('in', style({ transform: 'translateX(0)' })),
            transition('void => *', [
                style({ transform: 'translateX(100%)' }),
                animate(1000)
            ])
        ])
    ]
})
export class DataManagementWqComponent implements OnInit {
    toggleTempAcc:boolean=false;
    //clientId = "207729342764-6ge4qb0ees3s28nc2gacoet8kbc1u4v6.apps.googleusercontent.com";
    clientId="42091266813-c1u2qp9gej3ffqhihm11m3q31628mhof.apps.googleusercontent.com";
    scope = ['https://www.googleapis.com/auth/drive.readonly'];
    UserData = this.$sessionStorage.retrieve('userData');
    currentAccount: any;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    TemplatesHeight: any;
    selectedProfileStatus: number=0;
    showActions: boolean = false;
    mouseOverTemplate: number = -1;
    mouseOverBatchDetail: number = -1;
    viewbyOptions = ['All', 'Extracted', 'Hold', 'Transformed', 'Skipped', 'Errored', 'Scheduled','Manual'];
    viewBy: any = 'All';
    batchDetailsList = [];
    openBatchDetail: boolean = false;
    confirmAction: boolean = false;
    selectedBatch: any;
    showBatchActions: boolean = false;
    jobProgramId: number = 81;
    Dropbox: any;
    showSearch:boolean=false;
    showAdditionalFilters:boolean=false;
    batchHistoryColumns = [
       /* { field: 'batchName', header: 'Batch Name', width: '100px', align: 'center' },*/
       
        // { field: 'extractionStatus', header: 'Extracted Status', width: '200px' },
        { field: 'extractionStatusMeaning', header: 'Extracted Status', width: '200px' },
        { field: 'extractedDateTime', header: 'Extracted Time', width: '150px'},
        // { field: 'transformationStatus', header: 'Transformed Status', width: '250px'},
         { field: 'transformationStatusMeaning', header: 'Transformed Status', width: '250px'},
        { field: 'transformedDateTime', header: 'Transformed Time', width: '150px' },
        { field: 'batchType', header: 'Batch Type', width: '80px' },
        { field: 'reference', header: 'Reference', width: '80px'},
        // { field: 'nextSchedule', header: 'Next Schedule', width: '100px' }
        /*{ field: 'Actions', header: 'Actions', width: '100px' }*/
    ];
    scheduleListColumns = [
        { field: 'jobName', header: 'Job Name', width: '100px', align: 'center' },
        { field: 'programName', header: 'Program Type', width: '100px', align: 'center' },
        { field: 'type', header: 'Frequency Type', width: '100px', align: 'center' },
        { field: 'frequencyDetails', header: 'Frequency Details', width: '100px', align: 'center' }
    ];
    templateDetailsColumnsList = [
//        { field: 'templateName', header: 'Template Name', width: '70px', align: 'center' },
        { field: 'sourceDirectoryPath', header: 'Source Directory Path', align: 'center' },
        { field: 'localDirectoryPath', header: 'Local Directory Path', align: 'center' }
    ];
    batchHistoryList = [];
    batchData = [];
    sourceProfilesForTenant = [];
    //fileTemplatesList  =[];
    statusList = ['Active', 'InActive', 'All'];
    selectedTab: any;
    highlightClass = [];
    selectedAll: any;
    selectAllBatches:any;
    BHselectedAll: any;
    searchWord: any;
    enableConfirmDialog: boolean = false;
   // loadScheduleContent: boolean = false;
    msgs = [];

    //drop zone
    public uploader: FileUploader = new FileUploader({ url: URL });
    public hasBaseDropZoneOver: boolean = false;
    public hasAnotherDropZoneOver: boolean = false;
    enableLineInfo: boolean = false;
    filesAdded: FileList;
    fileName: any = '';
    file : any;
    uploadedIcon: boolean = false;
    uploadStatus: boolean = false;
    spin: boolean = false;
    schedulesList = [];
    loadSchedules: boolean = false;
    loadProfileSchedules: boolean = false;
    profileSchedulesList = [];
    loadLinesForSelectedFile = [];
    linesListBasedOnFile = [];
    fileTemplateLines = [];
    fileTemplateLineColumns = [];
    lineStatusColumns = [{ field: 'recordStatus', header: 'Status', width: '100px', align: 'center' }];
    masterOrStagingLines = [];
    fieldWrtColMap = {};
    isEditLine: boolean = true;
    //pagination

    /*********************/
    page: any = 0;
    batchDetailPage:any=0;
    linePage:any=0;
    page1: any = 0;
    batchpageEvent: PageEvent = new PageEvent();
    batchDetailPageEvent: PageEvent = new PageEvent();
    linesPageEvent : PageEvent = new PageEvent();
    linesPageSize: number;
    batchPageSize: number;
    batchDetailPageSize:number;
    batchHeaderDetailsPageSize : number;
    pageSizeOptions = [5, 10, 25, 100];
 
    linesLength: any;
    batchLength: any;
    batchDetailLength : any;
    /*********************/

    //dropzone
    /*********************/
    dropZoneWidthClass: any ;
    templatesTableWidthClass: any = 'col-md-12 ';
    hideDropZoneVar: any = '';
    hideDZone: boolean = true;
    /*********************/
    //temp stores
    /*********************/
    tempStore :any;
    stagingData:boolean;
    /*********************/
    showProfileOptions:boolean=false;
    viewAllBatches:any='selectedProfile';
    
    /***************/
    //matchedTemplates:any;
   // showMatchedTemplates :boolean=false;
    mouseOnMatchedTemp:any=-1;
    
    /***************/
    
    
    /********************/
    schedulingObj = new SchedulingModel();
    /********************/
    programmeList = ["DataExtraction","DataTransformation"];
    parametersList:Parameters[] =[new Parameters()];
    remoteValue :string='';
    showTemplateDetail:boolean=false;
    
    showBatchHeaderActions:boolean=false;
    templateNameForSchedulesDisplay : any;
   // transformImmediately :any;
    linesToUpdate:any = [];
    linesStatusFilter : any='Fail';
    
    selectedFileName : any;
    selectedSrcInbHistId : any;
    bhDetIndex : any;
    selectedFilesTempId : any;
    hideDropZoneToggler: boolean = false;
    
    showViewBy:boolean=false;
    
    batchDetailColumnList = [
                             { field: 'templateName', header: 'Template Name', width: '250px', align: 'center' },
                             { field: 'fileName', header: 'File Name', width: '300px', align: 'center' },
                            //  { field: 'fileSize', header: 'File Size', width: '70px', align: 'center' },
                             { field: 'fileTransformedDate', header: 'Transformed Date', width: '180px', align: 'center' },
                            //  { field: 'status', header: 'Status', width: '70px', align: 'center' },
                             { field: 'status', header: 'Status', width: '180px', align: 'center' },
                            //  { field: 'failedReason', header: 'Failed status', width: '70px', align: 'center' },
                             { field: 'Actions', header: 'Actions', width: '100px', align: 'center' }
                             ];
    indexToFetchBatchDetail : any;
    displayTemplateCheckbox:boolean=true;
    displayBatchCheckbox:boolean=true;
    displayBatchDetailsCheckbox:boolean=true; 
    statusFilter:boolean=false;
    selectedStatInd:any;
    selectedSubStatInd:any;
    status:any=['All','Selected Profile'];
    subStatus:any=['All','Errored','Extracted','Transformed','Holded'];
    profilePanelOpenState: boolean = true;
    batchPanelOpenState: boolean = false;
    multipleIdentifiers : any = [];
    selectedIdentifierIndex:number=0 ;
    constructor(
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService,
        public dataManagementWqService: DataManagementWqService,
        public sourceProfilesService: SourceProfilesService,
        private fileTemplatesService: FileTemplatesService,
        private spaservice: SourceProfileFileAssignmentsService,
        private confirmationService: ConfirmationService,
        public dialog: MdDialog,
        private jobDetailsService: JobDetailsService,
        private notificationService: NotificationsService,
        private $sessionStorage: SessionStorageService

    ) {
        //this.pageSize = ITEMS_PER_PAGE;
        
        this.batchpageEvent.pageIndex = 0;
        this.batchDetailPageEvent.pageIndex = 0;
        this.linesPageEvent.pageIndex = 0;
        
        //this.batchpageEvent.pageSize = ITEMS_PER_PAGE;
        //this.batchDetailPageEvent.pageSize=ITEMS_PER_PAGE;
        //this.linesPageEvent.pageSize = ITEMS_PER_PAGE;
        this.batchPageSize=ITEMS_PER_PAGE;
        this.batchDetailPageSize=ITEMS_PER_PAGE;
        this.linesPageSize=ITEMS_PER_PAGE;
        //this.pageEvent.pageSize = this.batchHeaderDetailsPageSize;
        /*this.itemsPerPage = ITEMS_PER_PAGE;
        this.linespageSize = ITEMS_PER_PAGE;
        this.batchpageSize=ITEMS_PER_PAGE;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.linespageSize;*/
        if (this.$sessionStorage.retrieve('userData'))
            this.UserData = this.$sessionStorage.retrieve('userData');
        this.dataManagementWqService.searchData=null;
    }




    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        $(".setHeight").css({
            'max-height': this.TemplatesHeight
        });
        $(".templateLines").css({
            'max-width': (this.commonService.screensize().width) + 'px'
        });
       
        this.sourceProfilesService.fetchSourceProfilesAndconDetails(true).subscribe(
            (res: any) => {
                this.sourceProfilesForTenant = res;
                this.selectedTab = 0;
                if (this.sourceProfilesForTenant && this.sourceProfilesForTenant.length > 0 && this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
                {
                    this.getTemplateDetails(this.sourceProfilesForTenant[this.selectedTab].id);
                    this.viewAllBatches='all';
                   // this.filterBysProfile();
                    this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                    
                }
               
            });

        //add class to highlight first md card
        $(".profiles.md-tab-label").addClass("mat-tab-label-active");
        this.loadTemplates();
        let i: number = 0;
        $(".search-icon-body").click(function() {
            if ( $( ".ftlSearch md-input-container" ).hasClass( "hidethis" ) ) {
                $( ".ftlSearch md-input-container" ).removeClass( "hidethis" );
                $( ".ftlSearch md-input-container" ).addClass( "show-this" );
            } else if ( $( ".ftlSearch md-input-container" ).hasClass( "show-this" ) ) {
                var value = $( '.ftlSearch md-input-container .mySearchBox' ).filter( function() {
                    return this.value != '';
                } );
                if ( value.length <= 0 ) { // zero-length string
                    $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                    $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
                }
            } else {
                $( ".ftlSearch md-input-container" ).addClass( "show-this" );
            }
        } );
        $(".ftlSearch md-input-container .mySearchBox").blur(function() {
            var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if(value.length<=0) { // zero-length string
                $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
            }
       });

        /**Esc to close dialog */
        $(document).keyup((e) => {
            if (e.keyCode == 27) { // escape key
                if ($('.etl-drop-zone').css('right') == '20px') {
                    $('.etl-drop-zone').css('right', '-360px');
                    this.hideDropZoneToggler = false;
                }
            }
        });

    }
    confirm() {
        this.confirmationService.confirm({
            message: 'Are you sure to ',
            header: 'Confirmation',
            icon: 'fa fa-question-circle',

            accept: () => {
               // console.log('call api to accept src inb');
            },
            reject: () => {
               // console.log('call api to reject src inb');
            }
        });
    }
    clearOtherPaginations(){
          //  this.batchpageEvent = new PageEvent();
            this.batchDetailPageEvent = new PageEvent();
            this.linesPageEvent= new PageEvent();
            
            
            
          //  this.batchpageEvent.pageIndex = 0;
            this.batchDetailPageEvent.pageIndex = 0;
            this.batchDetailLength = 0; 
            this.linesLength=0;
            this.linesPageEvent.pageIndex = 0;
            
            
           // this.batchPageSize=ITEMS_PER_PAGE;
            this.batchDetailPageSize=ITEMS_PER_PAGE;
            this.linesPageSize=ITEMS_PER_PAGE;
       
    }
    
    fetchBatchHeadersList(id: any, idType:any) {
        console.log('id '+id+' idType '+idType);
        this.batchHistoryList = [];
        this.batchData = [];
        let sourceProfIds: any = [];
        let srcProfAssId : any;
        if(idType && idType == 'template')
        {
            srcProfAssId = id;
        }
        else if(idType && idType == 'profile')
            {
            if(this.viewAllBatches == 'all')
            {
            this.sourceProfilesForTenant.forEach(profile => {
                sourceProfIds.push(profile.id);
            });
            }
        else
            {
        if (!id || id == null || id == undefined) {
            this.sourceProfilesForTenant.forEach(profile => {
                sourceProfIds.push(profile.id);
            });
        }
        else
            sourceProfIds.push(id);
            }
        }
        

        
            if(this.viewBy == 'All')
                {
               // console.log('input for batches'+sourceProfIds+'=>'+this.page+'=>'+ this.batchPageSize);
                
                this.dataManagementWqService.fetchBatchHeaders(sourceProfIds,srcProfAssId,this.page, this.batchPageSize,'').subscribe(
                        (res: any) => {
                            this.batchData=[];
                            this.batchLength =0;
                            this.batchHistoryList = res;
                            this.batchData = this.batchHistoryList;
                            console.log('batch history fetched is'+JSON.stringify(this.batchData));
                            if(this.batchData && this.batchData.length > 0)
                                {
                                if(  this.batchData[0].totalCount)
                                    this.batchLength =  this.batchData[0].totalCount;
                                   this.showViewBy =true;
                                   this.showSearch=true;
                                   let cnt : number =0;
                                   this.batchData.forEach(batch=>{
                                       if(batch.hold == true)
                                           cnt=cnt+1;
                                   });
                                   if(this.batchData.length == cnt)
                                       this.displayBatchCheckbox = false;
                                }
                            else
                       {
                                if(this.viewAllBatches == 'all')
                                    {
                                    this.notificationService.info('No batches found', '');
                                    }
                                else
                                    {
                                    this.notificationService.info('No batches found for '+ this.sourceProfilesForTenant[this.selectedTab].sourceProfileName, '');
                                    }
                               
                                this.showViewBy =false;
                                this.showSearch=false;
                       }
                        });
                
                }
            
            else
                {
                 //   console.log('input for batches'+sourceProfIds+'=>'+this.page+'=>'+ this.batchPageSize+'=>'+this.viewBy);
                this.dataManagementWqService.fetchBatchHeaders(sourceProfIds,srcProfAssId,this.page, this.batchPageSize,this.viewBy).subscribe(
                        (res: any) => {
                            this.batchData =[];
                            this.batchLength =0;
                            this.batchHistoryList = res;
                            this.batchData = this.batchHistoryList;
                            console.log('batch history fetched is'+JSON.stringify(this.batchData));
                            if(this.batchData && this.batchData.length > 0)
                                { 
                                if(  this.batchData[0].totalCount)
                                    this.batchLength =  this.batchData[0].totalCount;
                                    this.showViewBy =true;
                                    this.showSearch=true;
                                    let cnt : number =0;
                                    this.batchData.forEach(batch=>{
                                        if(batch.hold == true)
                                            cnt=cnt+1;
                                    });
                                    if(this.batchData.length == cnt)
                                        this.displayBatchCheckbox = false;
                                }
                            else
                                {
                                if(this.viewAllBatches == 'all')
                                {
                                this.notificationService.info('No batches found', '');
                                }
                            else
                                {
                                this.notificationService.info('No batches found for '+ this.sourceProfilesForTenant[this.selectedTab].sourceProfileName, '');
                                }
                                this.showViewBy =false;
                                this.showSearch=false;
                                }
                        });
                
                }
           
    }
    //sourceProfilesForTenant[selectedTab].templatesList[0]
    selectAllBatchDetailsList() {
        for (var i = 0; i < this.batchDetailsList.length; i++) {
            if (!this.batchDetailsList[i].hold)
                this.batchDetailsList[i].selected = this.BHselectedAll;
        }
        this.enableBatchDetailActions();
    }
    selectAll() {
        for (var i = 0; i < this.dataManagementWqService.fileTemplatesList.length; i++) {
            if (!this.dataManagementWqService.fileTemplatesList[i].hold)
                this.dataManagementWqService.fileTemplatesList[i].selected = this.selectedAll;
        }
        this.enableActions();
    }
    selectAllBatchesList()
    {
        for(var i=0;i<this.batchData.length;i++)
            {
               this.batchData[i].selected=this.selectAllBatches;
            }
       this.enableBatchHeaderActions();
    }
        enableBatchHeaderActions() {
        let count: number = 0;
        this.batchData.forEach(batch => {
            if (batch.selected)
                count = count + 1;
        });
        if (count > 0)
            {
            this.showBatchHeaderActions = true;
            this.showSearch=false;
            }
        else
            {
           
            this.showBatchHeaderActions = false;
        this.showSearch=true;
            }
        
      //  this.showBatchActions = false;
       // this.showActions = false;
    }
    enableBatchDetailActions() {
        let count: number = 0;
        this.batchDetailsList.forEach(batchDetail => {
            if (batchDetail.selected)
                count = count + 1;
        });
        if (count > 0)
            this.showBatchActions = true;
        else
            this.showBatchActions = false;
    }


    enableActions() {
        let count: number = 0;
        this.dataManagementWqService.fileTemplatesList.forEach(fileTemplate => {
            if (fileTemplate.selected)
                count = count + 1;
        });
        if (count > 0)
            this.showActions = true;
        else
            this.showActions = false;
        
       // this.showBatchActions=false;
        //this.showBatchHeaderActions=false;
    }
    checkIfAllbatchDetailsSelected() {
        if (this.batchDetailsList) {
            this.BHselectedAll = this.batchDetailsList.every(function (item: any) {
                return item.selected == true;
            })
        }
        this.enableBatchDetailActions();
    }
    
    checkIfAllSelected() {
        if (this.dataManagementWqService.fileTemplatesList) {
            this.selectedAll = this.dataManagementWqService.fileTemplatesList.every(function (item: any) {
                return item.selected == true;
            })
        }
        this.enableActions();
    }
    checkIfAllBatchesSelected()
    {
       if(this.batchData)
           {
               this.selectAllBatches = this.batchData.every(function(item:any){
                   return item.selected == true;
               });
           }
       this.enableBatchHeaderActions();
    }
    loadTemplates() {

    }
    onRowSelect(ind) {
        //console.log('line number for removed is' + JSON.stringify(event.data));
        this.batchDetailsList = [];
        this.BHselectedAll =null;
       // let batchRec: any = event.data;
        this.selectedBatch =  this.batchData[ind].id;
       
        this.fetchBatchDetailsByBatchId( ind);
    }
    getTemplateDetails(profileId) {
        this.fileTemplatesService.fetchFileTemplatesBySourceProfileId(profileId).subscribe((res: any) => {
            this.dataManagementWqService.fileTemplatesList = res;
            if( this.dataManagementWqService.fileTemplatesList  &&  this.dataManagementWqService.fileTemplatesList .length>0)
                {
                let count:number=0;
                this.dataManagementWqService.fileTemplatesList.forEach(template=>{
                    if(template.hold==true)
                        {
                            count=count+1;
                        }
                });
                if(this.dataManagementWqService.fileTemplatesList.length == count)
                    this.displayTemplateCheckbox = false;
                }
            //console.log('this.dataManagementWqService.fileTemplatesList ' + JSON.stringify(this.dataManagementWqService.fileTemplatesList));
        });
    }
    loadContent(e) {
        $(".ETLWQ .profiles .mat-tab-header .mat-tab-label-container .mat-tab-labels > div").removeClass('show-more-options');
        this.emptyQueue();  
        //console.log('loadcontent called' + e.index);
        this.selectedTab = e.index;
        this.sourceProfilesService.searchData=null;
        this.dataManagementWqService.fileTemplatesList = [];
        this.selectedAll = null;
        this.showProfileOptions=false;
        this.showActions = false;
        this.getTemplateDetails(this.sourceProfilesForTenant[this.selectedTab].id);
        if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
            {
            this.viewAllBatches='selectedProfile';
            this.filterBysProfile();
            }
          //  this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id);

    }
    fetchSourceProfiles(e) {
        let status = this.statusList[e.index];
        this.selectedProfileStatus=e.index;
        this.sourceProfilesForTenant = [];
        this.fetchSPs(status);
        this.showActions=false;
    }
    fetchSPs(status)
    {
        if (status == 'Active') {
            this.sourceProfilesService.fetchSourceProfilesAndconDetails(true).subscribe(
                (res: any) => {
                    this.sourceProfilesForTenant = res;
                    this.dataManagementWqService.fileTemplatesList = [];
                    this.batchHistoryList = [];
                    this.batchData = [];
                    if (this.sourceProfilesForTenant && this.sourceProfilesForTenant.length > 0 && this.sourceProfilesForTenant[0] && this.sourceProfilesForTenant[0].id)
                        this.getTemplateDetails(this.sourceProfilesForTenant[0].id);
                    if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
                        this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                });
        }
        else if (status == 'InActive') {
            this.sourceProfilesService.fetchSourceProfilesAndconDetails(false).subscribe(
                (res: any) => {
                    this.sourceProfilesForTenant = res;
                    this.dataManagementWqService.fileTemplatesList = [];
                    this.batchHistoryList = [];
                    this.batchData = [];
                    if (this.sourceProfilesForTenant && this.sourceProfilesForTenant.length > 0 && this.sourceProfilesForTenant[0] && this.sourceProfilesForTenant[0].id)
                        this.getTemplateDetails(this.sourceProfilesForTenant[0].id);
                    if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
                        this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                });
            this.dropZoneWidthClass = null;
            //this.hideDropZoneVar = 'UnHide dropzone';
            this.templatesTableWidthClass = 'col-md-12  ';
            this.hideDZone = true;
        }
        else if (status == 'All') {
            this.sourceProfilesService.fetchSourceProfilesAndconDetails(null).subscribe(
                (res: any) => {
                    this.sourceProfilesForTenant = res;
                    this.dataManagementWqService.fileTemplatesList = [];
                    this.batchHistoryList = [];
                    this.batchData = [];
                    if (this.sourceProfilesForTenant && this.sourceProfilesForTenant.length > 0 && this.sourceProfilesForTenant[0] && this.sourceProfilesForTenant[0].id)
                        this.getTemplateDetails(this.sourceProfilesForTenant[0].id);
                    if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
                        this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                });
        }

    }
    schedule(level) {
        this.schedulingObj.transformImmediately =null;
        this.schedulingObj.programName=null;
            let  paramSet = {};
            this.parametersList=[];
            let count:number=0;
            let fileTemplates: any = '';
            this.schedulingObj.jobName = this.sourceProfilesForTenant[this.selectedTab].sourceProfileName+'-'+this.schedulingObj.programName;
            if(level == 'profile')
            {
             paramSet = {
                 "paramName":"SourceProfileName",
                    "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id
                    
                };
             this.parametersList.push(paramSet);
             paramSet = {
                     "paramName":"TemplateName",
                          "selectedValue": null
                      };
                  this.parametersList.push(paramSet);
                  
                 /* paramSet = {
                          "paramName":"ConnectionName",
                               "selectedValue": null
                           };
                       this.parametersList.push(paramSet);*/
                  if(this.schedulingObj.programName == 'DataTransformation')
                      {
                      paramSet = {
                              "paramName":"BatchHeader",
                                   "selectedValue": null
                               };
                           this.parametersList.push(paramSet);
                           paramSet = {
                                   "paramName":"BatchHeaderDetail",
                                      "selectedValue": null
                                  };
                           this.parametersList.push(paramSet);
                          
                           
                      }
                  else
                      {
                      let paramSet = {
                              "paramName":"TransformImmediately",
                              "selectedValue": false
                              };
                              this.parametersList.push(paramSet);
                      }
                 
                  
             this.schedulingObj.parameters = this.parametersList;
            }
            else if(level == 'template')
                {
                let i = 0;
                paramSet = {
                   "paramName":"SourceProfileName",
                        "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id,
                    };
                this.parametersList.push(paramSet);
                this.dataManagementWqService.fileTemplatesList.forEach(ft => {
                    if (ft.selected == true) {
                        if (i == this.dataManagementWqService.fileTemplatesList.length - 1) {
                            fileTemplates = fileTemplates + ft.templateId;
                        }
                        else {
                            fileTemplates = fileTemplates + ft.templateId + ',';
                        }
                        count = count+1;
                    }
                    i = i + 1;
                });
                paramSet = {
                     "paramName":"TemplateName",
                        "selectedValue": fileTemplates
                    };
                 this.parametersList.push(paramSet);
                /* paramSet = {
                         "paramName":"ConnectionName",
                              "selectedValue": null
                          };
                      this.parametersList.push(paramSet);*/
                 if(this.schedulingObj.programName == 'DataTransformation'){
                     paramSet = {
                             "paramName":"BatchHeader",
                                  "selectedValue": null
                              };
                          this.parametersList.push(paramSet);
                          paramSet = {
                                  "paramName":"BatchHeaderDetail",
                                     "selectedValue": null
                                 };
                          this.parametersList.push(paramSet);
                 }   
                 else
                 {
                 let paramSet = {
                         "paramName":"TransformImmediately",
                         "selectedValue": false
                         };
                         this.parametersList.push(paramSet);
                 }
            
                 this.schedulingObj.parameters = this.parametersList;
                }
            else if(level == 'batchHeader')
                {
                let i = 0;
                let batchHeaderIds : any='' ;
                paramSet = {
                   "paramName":"SourceProfileName",
                        "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id,
                    };
                this.parametersList.push(paramSet);
                
                paramSet = {
                        "paramName":"TemplateName",
                             "selectedValue": null
                         };
                     this.parametersList.push(paramSet);
                     
                     /*paramSet = {
                             "paramName":"ConnectionName",
                                  "selectedValue": null
                              };
                          this.parametersList.push(paramSet);*/
                
                     if(this.schedulingObj.programName == 'DataTransformation'){
                this.batchData.forEach(batch=>{
                    if(batch.selected == true)
                        {
                        if (i == this.batchData.length - 1) {
                            batchHeaderIds = batchHeaderIds + batch.id;
                        }
                        else
                            {
                            batchHeaderIds = batchHeaderIds + batch.id+',';
                            }
                        }
                    i=i+1;
                });
                paramSet = {
                        "paramName":"BatchHeader",
                           "selectedValue": batchHeaderIds
                       };
                this.parametersList.push(paramSet);
                     }
                this.schedulingObj.parameters = this.parametersList;
                    this.schedulingObj.programName='DataTransformation';
                }
            else 
            {
                
                let i = 0;
                let batchDetailIds : any ='';
                paramSet = {
                   "paramName":"SourceProfileName",
                        "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id,
                    };
                this.parametersList.push(paramSet);
                paramSet = {
                        "paramName":"TemplateName",
                             "selectedValue": ''
                         };
                     this.parametersList.push(paramSet);
                     
                   /*  paramSet = {
                             "paramName":"ConnectionName",
                                  "selectedValue": ''
                              };
                          this.parametersList.push(paramSet);*/
                     if(this.schedulingObj.programName == 'DataTransformation'){
                          paramSet = {
                                  "paramName":"BatchHeader",
                                       "selectedValue": ''
                                   };
                               this.parametersList.push(paramSet);
                     
                               
                          
                this.batchDetailsList.forEach(batchDetail=>{
                    if(batchDetail.selected == true)
                        {
                        if (i == this.batchDetailsList.length - 1) {
                            batchDetailIds = batchDetailIds + batchDetail.id;
                        }
                        else
                            {
                            batchDetailIds = batchDetailIds + batchDetail.id+',';
                            }
                        }
                    i=i+1;
                });
                paramSet = {
                        "paramName":"BatchHeaderDetail",
                           "selectedValue": batchDetailIds
                       };
                this.parametersList.push(paramSet);
                     }
                this.schedulingObj.parameters = this.parametersList;
                    this.schedulingObj.programName='DataTransformation';
            }
                
        

    }
    //('profile',null,null,null,null)
    extractNow(level, profileId, templateId, batchHeaderId, batchDetailId) {
        //console.log('extract now with' + JSON.stringify(this.dataManagementWqService.fileTemplatesList));
        let fileTemplates: any = '';
    let i = 0;
    //comma separated templates, no connection should be sent, as it handles in code itself
    let  paramSet = {};
    let action ='';
    let count:number=0;
    if(level == 'profile')
        {
         paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id
            };
         action=this.sourceProfilesForTenant[this.selectedTab].sourceProfileName;
        }
    else if(level == 'template')
        {
        this.dataManagementWqService.fileTemplatesList.forEach(ft => {
            if (ft.selected == true) {
                if (i == this.dataManagementWqService.fileTemplatesList.length - 1) {
                    fileTemplates = fileTemplates + ft.templateId;
                }
                else {
                    fileTemplates = fileTemplates + ft.templateId + ',';
                }
                count = count+1;
            }
            i = i + 1;
        });
        action = this.sourceProfilesForTenant[this.selectedTab].sourceProfileName +' templates';
         paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id,
                "param2": fileTemplates
            };
        }
    else if(level == 'batch')
        {
        let batchDetailsIds: any = '';
    let j = 0;
    this.batchDetailsList.forEach(batchDetail => {
        if (batchDetail.selected == true) {
            if (j == this.batchDetailsList.length - 1) {
                batchDetailsIds = batchDetailsIds + batchDetail.templateId;
            }
            else {
                batchDetailsIds = batchDetailsIds + batchDetail.templateId + ',';
            }

        }
        j = j + 1;
    });
    paramSet = {
            "param1": this.sourceProfilesForTenant[this.selectedTab].id,
            "param2": '',
            "param3": batchHeaderId
        };
        }
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            data: { processBy:level, ok: 'ok', cancel: 'cancel', action: action ,process:'extraction',count:count,level:level}
        });
        dialogRef.afterClosed().subscribe(result => {
        
       
            if (result && result == 'ok'){
        this.jobDetailsService.initiateOnDemandTypeJob('DataExtraction', this.UserData, paramSet).subscribe( 
            (res: any) => {
                let status : any =res;
                if(status)
                {
                  //  console.log('status after initiating job '+JSON.stringify(status));
                    if(status.status && status.status.toLowerCase().search('failed'))
                    { 
                        this.notificationService.error('Warning!', status.status);
                    }
                    else
                    this.notificationService.success('SUCCESS', 'Extraction successful!');

                }
              
               
            },
            (res: Response) => {
                this.onError(res)
                this.notificationService.error('Warning!', 'Extraction failed!');
            }
        );}
    });
    }
    transformNow(level, profileId, templateId, batchHeaderId, batchDetailId) {
        //console.log('transform now with' + JSON.stringify(this.dataManagementWqService.fileTemplatesList));
        let fileTemplates: any = '';
    let i = 0;
    let lev : any;
    let count:number=0;
    let paramSet = {};
    if(level == 'profile')
    {
        lev = 'profile';
        paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id
            };
    }
    else if(level == 'template')
        {
        lev = 'template';
        this.dataManagementWqService.fileTemplatesList.forEach(ft => {
            if (ft.selected == true) {
                if (i == this.dataManagementWqService.fileTemplatesList.length - 1) {
                    fileTemplates = fileTemplates + ft.templateId;
                }
                else {
                    fileTemplates = fileTemplates + ft.templateId + ',';
                }
                count = count+1;
            }
            i = i + 1;
        });
        paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id,
                "param2": fileTemplates
            };
        }
    else if(level == 'batchHeader'){
        lev = 'batch header';
        let batchHeaderIds : any ='';
    let j=0;
    this.batchData.forEach(batch=>{
        if(batch.selected == true){
            if(j == this.batchData.length -1){
            batchHeaderIds = batchHeaderIds+batch.id;
        }
        else
            {
            batchHeaderIds = batchHeaderIds+batch.id+',';
            }
            count = count+1;
    }
        j=j+1;
    });
    paramSet = {
            "param1": this.sourceProfilesForTenant[this.selectedTab].id,
            //"param2": fileTemplates,
            "param3": batchHeaderIds
        };
    }
    else 
    {
        lev = 'batch detail';
        //comma separated templates, no connection should be sent, as it handles in code itself
        let batchDetailsIds: any = '';
        let j = 0;
        this.batchDetailsList.forEach(batchDetail => {
            if (batchDetail.selected == true) {
                if (j == this.batchDetailsList.length - 1) {
                    batchDetailsIds = batchDetailsIds + batchDetail.srcInbHistId;
                }
                else {
                    batchDetailsIds = batchDetailsIds + batchDetail.srcInbHistId + ',';
                }
                count = count+1;
            }
            j = j + 1;
        });
        paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id,
              //  "param2": fileTemplates,
                //"param3": '',
                "param4":batchDetailsIds
            };
    }
    
   
  // console.log('paramSet before job creation'+JSON.stringify(paramSet));
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            data: { processBy:level, ok: 'ok', cancel: 'cancel', 
                action:  this.sourceProfilesForTenant[this.selectedTab].sourceProfileName,
                process:'transformation',count:count,level:lev}
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok'){
        this.jobDetailsService.initiateOnDemandTypeJob('DataTransformation', this.UserData, paramSet).subscribe(
            (res: any) => {
                this.notificationService.success('SUCCESS', 'Transformation initiated!');
            },
            (res: Response) => {
                this.onError(res)
                this.notificationService.error('Warning!', 'Transformation failed!');
            }
        );
        }
        });
    }
    openDialog(indexToHold): void {
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { ind: indexToHold, ok: 'ok', cancel: 'cancel' }
        });
        dialogRef.afterClosed().subscribe(result => {
        });
    }
    holdTemplate(spaid) {
        this.dataManagementWqService.reason=null;
        let reason : any;
        if(this.sourceProfilesForTenant[this.selectedTab].holdReason == undefined || this.sourceProfilesForTenant[this.selectedTab].holdReason == null)
        {
            
        }
        else
        reason =  this.sourceProfilesForTenant[this.selectedTab].holdReason+'';
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: spaid, ok: 'ok',reason:'', cancel: 'cancel', action: 'hold',previousReason:reason }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                {
               //console.log('reason'+this.dataManagementWqService.reason);
                this.dataManagementWqService.holdTemplate(spaid).subscribe(
                    (res: any) => {
                        this.holded();
                        this.getTemplateDetails(this.sourceProfilesForTenant[this.selectedTab].id);
                    });
                }
        });

    }
    unHoldTemplate(spaid) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: spaid, ok: 'ok', cancel: 'cancel', action: 'Release Hold' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                this.dataManagementWqService.unHoldTemplate(spaid).subscribe(
                    (res: any) => {
                        this.releasedHold();
                        this.getTemplateDetails(this.sourceProfilesForTenant[this.selectedTab].id);
                    });
        });

    }
    holdBatchDetail(srcFileInbHistId) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: srcFileInbHistId, ok: 'ok', cancel: 'cancel', action: 'hold' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                this.dataManagementWqService.holdBatchDetail(srcFileInbHistId).subscribe(
                    (res: any) => {
                        //refresh batchdetails
                        this.holded();
                        this.fetchBatchDetailsByBatchId(this.selectedBatch);
                    });
        });

    }
    releaseBatchDetailHold(srcFileInbHistId) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: srcFileInbHistId, ok: 'ok', cancel: 'cancel', action: 'Release Hold' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                this.dataManagementWqService.releaseBatchDetailHold(srcFileInbHistId).subscribe(
                    (res: any) => {
                        this.releasedHold();
                        this.fetchBatchDetailsByBatchId(this.selectedBatch);
                    });
        });

    }
    holdBatch(batchId) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: batchId, ok: 'ok', cancel: 'cancel', action: 'hold' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                this.dataManagementWqService.holdBatch(batchId).subscribe(
                    (res: any) => {
                        this.holded();
                        if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
                            this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                    });
        });

    }
    releaseBatchHold(batchId) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: batchId, ok: 'ok', cancel: 'cancel', action: 'Release Hold' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                this.dataManagementWqService.releaseBatchHold(batchId).subscribe(
                    (res: any) => {
                        this.releasedHold();
                        if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id)
                            this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                    });
        });

    }
    fetchByViewOption() {
       // console.log('view by' + this.viewBy);
        if (this.batchData) {
            this.batchData = [];
            this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
            /* if (this.viewBy != 'All') {
                this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id);
               this.batchHistoryList.forEach(batch => {
                    if ((batch.extractionStatus != null && batch.extractionStatus.toLowerCase() == this.viewBy.toLowerCase()) || (batch.transformationStatus != null && batch.transformationStatus.toLowerCase() == this.viewBy.toLowerCase())) {
                        this.batchData.push(batch);
                    }
                });
            }
            else {
                this.batchData = this.batchHistoryList;
            }*/
        }
    }
    fetchBatchDetailsByBatchId(ind) {
        this.batchDetailsList = [];
        this.showBatchActions = false;
      let batchId : any ;
        console.log('ind'+ind);
        this.indexToFetchBatchDetail = ind;
        if(this.batchData && this.batchData[ind] && this.batchData[ind].id)
        batchId = this.batchData[ind].id;
      //  console.log('this.batchDetailPageEvent.pageIndex + 1, this.batchDetailPageSize,batchId'+this.batchDetailPageEvent.pageIndex +'=>'+ this.batchDetailPageSize+'=>'+batchId)
        this.dataManagementWqService.fetchBatchDetailsById(this.batchDetailPage, this.batchDetailPageSize,batchId).subscribe(
            (res: any) => {
                this.batchDetailsList = res;
                console.log('this.batchDetailsList'+JSON.stringify(this.batchDetailsList));
                if (this.batchDetailsList && this.batchDetailsList.length>0)
                    {
                    if(this.batchDetailsList[0] && this.batchDetailsList[0].totalCount)
                    this.batchDetailLength = this.batchDetailsList[0].totalCount;
                    let cnt:number=0;
                    this.batchDetailsList.forEach(batchDetail => {
                        batchDetail['selected'] = false;
                        if((batchDetail.status == 'Loaded') || (batchDetail.status == 'Loading Failed')  || (batchDetail.status != 'Loaded' && batchDetail.hold == true))
                        cnt=cnt+1;
                    });
                    this.openBatchDetail = true;
                    if(this.batchDetailsList.length == cnt)
                        this.displayBatchDetailsCheckbox=false;
                    }
                   
               // console.log('this.batchDetailsList ' + JSON.stringify(this.batchDetailsList));
            });
    }
    holdBatchHistDetail(bhDetIndex) {

    }
    releaseHoldBatchHistDetail(bhDetIndex) {

    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
        this.spin = false;
    }

    holded() {
        this.notificationService.success('SUCCESS', 'Hold successful!');
    }
    holdFailed() {
        this.notificationService.error('Warning!', 'Failed to Hold');
    }
    releasedHold() {
        this.notificationService.success('SUCCESS', 'Releasing hold successful!');
    }
    releaseHoldFailed() {
        this.notificationService.error('Warning!', 'Failed to release Hold!');
    }
    public fileOverBase(file: File): void {
        if (file)
            this.hasBaseDropZoneOver = true;
       // console.log('hasBaseDropZoneOver' + this.hasBaseDropZoneOver);
       // console.log('uploader?.queue?.length' + this.uploader.queue.length);
    }
    public onFileDrop(filelist: FileList): void {
        this.spin = true;
        let fileItem: FileItem = this.uploader.queue[this.uploader.queue.length - 1];
        this.uploader.queue = [];
        let msg: any;
        this.uploader.queue[0] = fileItem;
        if (filelist.length > 0) {

           // console.log('file list length >0');
            this.filesAdded = filelist;
            let file: File = filelist[0];
            this.fileName = file.name;
           this.matchTemps();
        }
        else
            this.spin=false;
    }
    matchTemps()
    {
        this.dataManagementWqService.matchedTemplates=[];
        this.dataManagementWqService.matchTemplates(this.fileName, this.dataManagementWqService.fileTemplatesList).subscribe((res: Response) => {
            this.dataManagementWqService.matchedTemplates =res; 
            this.spin=false;
            this.uploadedIcon=true;
            this.dataManagementWqService.showMatchedTemplates=true;
            //console.log('this.matchedTemplates'+JSON.stringify(this.matchedTemplates));
            if(!this.dataManagementWqService.matchedTemplates || ( this.dataManagementWqService.matchedTemplates && this.dataManagementWqService.matchedTemplates.length<=0) )
                {
                
                var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    width: '400px',
                    data: { createNew  : 'Template', ok: 'ok', cancel: 'cancel', message:'Matching templates not found. Proceed creating new Template' }
                });
                dialogRef.afterClosed().subscribe(result => {
                    if (result && result == 'ok')
                        {
                        this.dataManagementWqService.loadCreateTemplateForm=true;
                        this.remoteValue='create';
                        }
                    else
                        {
                     // this.emptyQueue();
                        }
                });
                
                
                }
         });
       
    }
    emptyQueue()
    {
      //  this.uploader.queue = [];
        this.uploader = new FileUploader({ url: URL });
        this.uploadedIcon=false;
    }
    fileChange(file: any) {
        this.onFileDrop(file.files);
    }
    //#region   Google Picker
    onApiLoad() {
        this.emptyQueue();
        var a=this;
        gapi.load('auth', { 'callback': this.onAuthApiLoad.bind(a) });
        gapi.load('picker');
    }

    onAuthApiLoad() {
        gapi.auth.authorize(
            {
                'client_id': this.clientId,
                'scope': this.scope,
                'immediate': false
            },
            this.handleAuthResult.bind(this));
    }
    //setDeveloperKey('AIzaSyDicmpQEyJ95jTHGG81BqmWI7eqTbVlHKs').
    handleAuthResult(authResult) {
        if (authResult && !authResult.error) {
            if (authResult.access_token) {
                var pickerBuilder = new google.picker.PickerBuilder();
                var picker = pickerBuilder. enableFeature(google.picker.Feature.NAV_HIDDEN).setOAuthToken(authResult.access_token). addView(google.picker.ViewId.DOCS).                  
                   // setDeveloperKey('AIzaSyDicmpQEyJ95jTHGG81BqmWI7eqTbVlHKs').
                    setCallback(this.pickerFile.bind(this)). build();
                picker.setVisible(true);
            }
        }
    }

    pickerFile(e){
        var url = 'nothing';
        var r = this;
       // console.log('Picker File');
        if (e[google.picker.Response.ACTION] == google.picker.Action.PICKED) {
            var doc = e[google.picker.Response.DOCUMENTS][0];
            url = doc[google.picker.Document.URL];
        }
        var message = 'You picked: ' + url;
        alert(message);
        if (doc) {
            // this.dataManagementWqService.accessDrive(doc.id).subscribe((res: Response) => {
            //     let resp = res;
            //     console.log('after file'+JSON.stringify(resp));
            // });
            
          //  console.log('File ID: ' + doc.id);
           // console.log('File name: ' + doc.name);
            this.fileName=doc.name;
            this.file= doc;
            this.matchTemps();
           // this.sampleFileUpload(doc);
        }
    };
    sampleFileUpload(doc)
    {
       // console.log("inside fileuploa func " + doc.name);
 
    }

    //#region DropBox Chooser
    options = {
        success: function (files) {
            if(files && files.length>0)
                {
                this.filesAdded = files;
                files.forEach(file=>{
                   // console.log('dropbox file  = '+ file.name);
                    this.fileName=file.name;
                    //this.file= file;
                    this.matchTemps();
                    //let msg: any;
                  //  this.sampleFileUpload({name:'Hi'});
                   
                });
            
                }
        }.bind(this),
        cancel: function () {
            //optional
        },
        linkType: "preview", // "preview" or "direct"
        multiselect: true, // true or false
        // extensions: ['.png', '.jpg'],
    };
    button = Dropbox.createChooseButton(this.options);

    selectFromDropBox() {
        this.emptyQueue();
        this.button.click();
    }
    //#endregion
    viewSchedules(spId, tempId,i) {
        this.dataManagementWqService.fetchSchedulesByProfileOrTemplate(spId, tempId).subscribe(
            (res: any) => {
                let schedules = res;
                this.schedulesList = [];
                this.schedulesList[i] = [];
                this.profileSchedulesList = schedules;
               // this.schedulesList[i] = schedules;
                this.loadProfileSchedules=true;
                if(!this.profileSchedulesList  || this.profileSchedulesList .length<=0)
                {
                 this.notificationService.info('oops!', 'No schedules found for '+this.dataManagementWqService.fileTemplatesList[i].templateName);
                }
            });
    }
    viewProfileSchedules() {
        this.profileSchedulesList=[];
        this.dataManagementWqService.fetchSchedulesByProfileOrTemplate(this.sourceProfilesForTenant[this.selectedTab].id, '').subscribe(
            (res: any) => {
                let schedules = res;
                this.profileSchedulesList = [];
                this.profileSchedulesList = schedules;
                if(!this.profileSchedulesList  || this.profileSchedulesList .length<=0)
                    {
                    this.notificationService.info('oops!', 'No schedules found for '+this.sourceProfilesForTenant[this.selectedTab].sourceProfileName);
                    }
            });
    }

    fetchTemplateDetailsByTemplateId(tempId,interId) {
        this.fileTemplatesService.fetchTemplateLinesByTemplateIdAndIdentifieId(tempId,interId).subscribe(
            (res: any) => {
                this.fileTemplateLines = [];
                this.fileTemplateLines = res;

                this.fileTemplateLineColumns = [];
                console.log('file temp lines for'+interId+' are:'+JSON.stringify( this.fileTemplateLines));
                if (this.fileTemplateLines && this.fileTemplateLines.length > 0) {
                    this.fileTemplateLines.forEach(tempLine => {
                        if (!this.fileTemplateLineColumns) {
                            this.fileTemplateLineColumns = [];
                        }
                        let obj = { field: tempLine.columnHeader, header: tempLine.columnHeader, width: '100px', align: 'center' };
                        this.fileTemplateLineColumns.push(obj);
                        if (tempLine.masterTableReferenceColumn) {
                            tempLine.masterTableReferenceColumn = tempLine.masterTableReferenceColumn.replace("_", "");
                            tempLine.masterTableReferenceColumn = tempLine.masterTableReferenceColumn.toLowerCase();
                            this.fieldWrtColMap[tempLine.masterTableReferenceColumn] = tempLine.columnHeader;
                        }

                    });
                    let  obj : any;
                    if(this.stagingData==true)
                        {
                        obj = { field: 'lineContent', header: 'Line Content', width: '100px', align: 'center' };
                        this.fileTemplateLineColumns.push(obj);
                        }
                      obj = { field: 'recordStatus', header: 'Status', width: '100px', align: 'center' };
                    this.fileTemplateLineColumns.push(obj);
                     obj = { field: 'Actions', header: 'Actions', width: '100px', align: 'center' };
                     this.fileTemplateLineColumns.push(obj);
                    
                    this.buildDataByTemplateLinesAndData();
                }
            });
    }
    buildDataByTemplateLinesAndData() {
        //console.log('template lines by tempid are' + JSON.stringify(this.fileTemplateLines));
        //console.log('fieldWrtColMap are' + JSON.stringify(this.fieldWrtColMap));
        let ind: number = 0;
        //masterOrStagingLines
        this.linesListBasedOnFile.forEach(masterLine => {
            if (masterLine) {
                var keys = Object.keys(masterLine);
                for (var i = 0; i < keys.length; i++) {
                    var key = keys[i];
                    if (this.fieldWrtColMap.hasOwnProperty(key)) {
                        masterLine[this.fieldWrtColMap[key]] = masterLine[key];
                    }
                }

            }
        });
        //console.log('fetched lines after modifyng' + JSON.stringify(this.linesListBasedOnFile));

    }
    fetchLinesBySrcFileInb(spId, tempId, fileName, srcInbHistId, ind) {
        //temp store
        this.linesToUpdate=[];
        this.linesListBasedOnFile=[];
        this.tempStore =[];
        this.tempStore.push(spId);
        this.tempStore.push( tempId);
        this.tempStore.push(fileName);
        this.tempStore.push(srcInbHistId);
        this.tempStore.push(ind);
        this.bhDetIndex =ind;
        this.selectedSrcInbHistId=srcInbHistId;
        this.selectedFilesTempId =tempId;
       /* if(! this.loadLinesForSelectedFile)
        this.loadLinesForSelectedFile = [];
        if( this.loadLinesForSelectedFile &&  this.loadLinesForSelectedFile.length>0)
            {
            let i:number=0;
        for( i=0;i< this.loadLinesForSelectedFile.length;i++)
            {
        if(i==ind)
        this.loadLinesForSelectedFile[i]=true;
        else
        this.loadLinesForSelectedFile[i]=false;
        
            }
            
            
            }
        else
        {
        this.loadLinesForSelectedFile[ind]=true;
        
        }*/
       if(this.batchDetailsList[ind].status.toLowerCase().search('Loaded'))
           {
               this.dataManagementWqService.fetchIdentifiersListIfMultiple(tempId,srcInbHistId,'master').subscribe(
                (res: Response) => {
                    let resp : any = res;
                    this.multipleIdentifiers = [];
                    if(resp && resp.length > 0)
                    {
                        console.log('intermediate identifiers of master '+JSON.stringify(resp));
                        this.multipleIdentifiers = resp;
                        this.fetchLinesFromDataMaster(spId,tempId,srcInbHistId , this.multipleIdentifiers[this.selectedIdentifierIndex].id);
                    }
                    else{
                        this.fetchLinesFromDataMaster(spId,tempId,srcInbHistId , null);
                    }

                });
        
           }
       else
           {
           console.log('parameter for pagination are:'+this.linePage+'-' +  this.linesPageSize +'-'+ spId+'-' + tempId+'-' + srcInbHistId+'-'+this.linesStatusFilter);
           this.dataManagementWqService.fetchIdentifiersListIfMultiple(tempId,srcInbHistId,'staging').subscribe(
            (res: Response) => {
                let resp : any = res;
                //this.multipleIdentifiers = [];
                if(resp && resp.length > 0)
                {
                    console.log('intermediate identifiers of staging '+JSON.stringify(resp));
                    this.multipleIdentifiers = resp;
                    this.fetchLinesFromDataStaging(spId,tempId,srcInbHistId , this.multipleIdentifiers[this.selectedIdentifierIndex].id,fileName);
                }
                else{
                    this.fetchLinesFromDataStaging(spId,tempId,srcInbHistId , null,fileName);
                }

            });
       
           }
    }
    fetchLinesByRowId(e)
    {
        this.selectedIdentifierIndex = e.index;
        if(this.batchDetailsList[this.bhDetIndex].status == 'Loaded')
        {
            if(this.multipleIdentifiers.length >0 )
            this.fetchLinesFromDataMaster(this.tempStore[0],this.tempStore[1],this.tempStore[3] , this.multipleIdentifiers[this.selectedIdentifierIndex].id);
            else
            this.fetchLinesFromDataMaster(this.tempStore[0],this.tempStore[1],this.tempStore[3] , null);
        }
        else{
            if(this.multipleIdentifiers.length >0 )
            this.fetchLinesFromDataStaging(this.tempStore[0],this.tempStore[1],this.tempStore[3] , this.multipleIdentifiers[this.selectedIdentifierIndex].id,this.tempStore[2]);
            else
            this.fetchLinesFromDataStaging(this.tempStore[0],this.tempStore[1],this.tempStore[3] , null,this.tempStore[2]);
        }
       
    }
   
    fetchLinesFromDataStaging(spId,tempId,srcInbHistId,interId,fileName)
    {
        this.dataManagementWqService.fetchDataStagingLinesBySrcFileInb(this.linePage + 1, this.linesPageSize, spId, tempId, srcInbHistId,this.linesStatusFilter).subscribe(
            (res: Response) => {
                let stagingLinesListBasedOnFile: any = res.json();
                this.stagingData=true;
                this.linesListBasedOnFile = [];
                if (stagingLinesListBasedOnFile) {
                    this.linesListBasedOnFile = stagingLinesListBasedOnFile;
                    if (res.headers && res.headers.get('x-count'))
                        {
                        this.linesLength = res.headers.get('x-count');
                        console.log(' this.lineslength  '+     this.linesLength );
                        }
                    else
                        {
                        console.log(' this.lineslength  fetchng failed in staging');
                        }
                  // console.log('staging - linesListBasedOnFile' + JSON.stringify(this.linesListBasedOnFile));
                    if (this.linesListBasedOnFile.length <= 0) {
                        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                            width: '400px',
                            data: { records: 'records', ok: 'ok', cancel: 'cancel', action: fileName }
                        });
                        dialogRef.afterClosed().subscribe(result => {

                        });
                    }
                    else
                        {
                        this.linesListBasedOnFile.forEach(line=>{
                            if(line.recordStatus != 'SUCCESS')
                                line['editable'] = true;
                            else
                                line['editable'] = false;
                        });
                        this.fetchTemplateDetailsByTemplateId(tempId,interId);
                        }
                    

                }
            });
    }
    fetchLinesFromDataMaster(spId,tempId,srcInbHistId,interId)
    {
        console.log('parameter for pagination are:'+this.linePage +'-' +  this.linesPageSize +'-'+ spId+'-' + tempId+'-' + srcInbHistId);
        this.dataManagementWqService.fetchDataMasterLinesBySrcFileInb(this.linePage + 1, this.linesPageSize, spId, tempId, srcInbHistId,interId).subscribe(
                (res: Response) => {
                    //console.log('spIdtempIdsrcInbHistId' + spId + '=>' + tempId + '=>' + srcInbHistId);
                    this.linesListBasedOnFile = [];
                    let masterLinesListBasedOnFile: any = res.json();
                    this.linesListBasedOnFile = [];
                    //console.log('masterLinesListBasedOnFile' + JSON.stringify(masterLinesListBasedOnFile));
                    if (masterLinesListBasedOnFile && masterLinesListBasedOnFile.length > 0) {
                        this.linesListBasedOnFile = masterLinesListBasedOnFile;
                        if (res.headers && res.headers.get('x-count'))
                            {
                            this.linesLength = res.headers.get('x-count');
                            }
                        else
                            {
                            console.log(' this.lineslength  fetchng failed');
                            }

                        this.linesListBasedOnFile.forEach(line => {
                            line['recordStatus'] = 'SUCCESS';
                            line['editable'] = false;
                        });
                       // console.log('master - linesListBasedOnFile' + this.linesListBasedOnFile.length);
                        this.stagingData=false;
                        this.fetchTemplateDetailsByTemplateId(tempId,interId);

                    }
                });
    }

    lookupRowStyleClass(rowData: any) {
        if (rowData.recordStatus.toLowerCase() == 'success') {
            return 'success-row';
        }
        else
            {
            return 'failed-row';
            }
    }
    saveStagingLines() {
        if (this.linesListBasedOnFile && this.linesToUpdate && this.linesToUpdate.length>0) {
            
           
            var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                width: '400px',
                data: { updateLines  : 'yes', ok: 'ok', cancel: 'cancel', message:'Are you sure to update '+this.linesToUpdate.length +'lines?' }
            });
            dialogRef.afterClosed().subscribe(result => {
                if (result && result == 'ok')
                    {
                   // let linesToUpdate = [];
                   /* this.linesListBasedOnFile.forEach(line => {
                        if (line.recordStatus && line.recordStatus != 'SUCCESS') {
                            linesToUpdate.push(line);
                        }
                    });*/
                    this.linesToUpdate.forEach(line => {
                        line.recordStatus ='SUCCESS';
                        var keys = Object.keys(line);
                        for (var i = 0; i < keys.length; i++) {
                            //key in line is value for a json object. get the key of that value and replace the value of key in line with the key of line
                            for (var key in this.fieldWrtColMap) {

                                if (this.fieldWrtColMap[key] == keys[i]) {
                                    // console.log(key);
                                    // console.log('this.fieldWrtColMap[key]'+this.fieldWrtColMap[key]); 
                                    // console.log('keys[i]'+keys[i]);
                                    line[key] = line[this.fieldWrtColMap[key]];
                                }
                            }
                        }
                    });
                    this.dataManagementWqService.updateStagingLines(this.linesToUpdate).subscribe(
                            (res: any) => {
                                this.notificationService.success('', 'Lines updated successfully');
                                let resp : any = res;
                                //console.log('resp'+JSON.stringify(resp));
                                if(resp && resp.taskName && resp.taskName.search('Transformation is') != -1)
                                    {
                                        this.dataManagementWqService.deleteStagingRecords(this.linesToUpdate[0].srcFileInbId).subscribe(
                                            (res: any) => {
                                                this.linesListBasedOnFile=[];
                                                this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
                                            });
                                   
                                    }
                                else
                                    {
                                    if(this.tempStore)
                                    {
                                        this.fetchLinesBySrcFileInb(this.tempStore[0],this.tempStore[1],this.tempStore[2],this.tempStore[3],this.tempStore[4]);
                                    }
                                    }
                            });
                    }
                else
                    {
                    //this.fetchLinesBySrcFileInb(this.tempStore[0],this.tempStore[1],this.tempStore[2],this.tempStore[3],this.tempStore[4]);
                    }
            });
            
          
        }
        else
            {
            this.notificationService.info('', 'No lines selected to update');
            }
    }
    showHideDropZone(param) {
        if(param == 'OPEN'){
            $('.etl-drop-zone').css('right', '20px');
            this.hideDropZoneToggler = true;
        }else{
            $('.etl-drop-zone').css('right', '-360px');
            this.hideDropZoneToggler = false;
        }
        /* if (this.dropZoneWidthClass == 'col-md-2') {
            this.dropZoneWidthClass = null;
            this.hideDropZoneVar = 'UnHide dropzone';
            this.templatesTableWidthClass = 'col-md-12  ';
            this.hideDZone = true;
        }
        else {
            this.hideDropZoneVar = 'Hide dropzone';
            this.templatesTableWidthClass = 'col-md-10  ';
            this.hideDZone = false;
            setTimeout(() => {
                this.dropZoneWidthClass = 'col-md-2';  
            }, 1000);
        } */
    }
    
    filterBysProfile()
    {
       console.log('filterBysProfile'+this.viewAllBatches);
        if(this.viewAllBatches == 'selectedProfile')
            {
                this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
            }
        else  if(this.viewAllBatches == 'all')
            {
            this.fetchBatchHeadersList(null,'profile');
            }
    }
    profileOptions(index)
    {
        if(index == this.selectedTab){
        let ind:number = index + 1;
        console.log('index : '+ind);
        if($(".ETLWQ .profiles .mat-tab-header .mat-tab-label-container .mat-tab-labels > div:nth-child("+ind+")").hasClass('show-more-options'))
            {
                $(".ETLWQ .profiles .mat-tab-header .mat-tab-label-container .mat-tab-labels > div:nth-child("+ind+")").removeClass('show-more-options');
            }
        else
            {
                $(".ETLWQ .profiles .mat-tab-header .mat-tab-label-container .mat-tab-labels > div:nth-child("+ind+")").addClass('show-more-options')
            }
        }
    }
    actionsForTemplate(event)
    {
       // console.log('eventttttttt'+JSON.stringify(event));
    }
    loadCreateTemplate()
    {
        this.dataManagementWqService.loadCreateTemplateForm=true;
    }
    emptySchedules()
    {
        this.profileSchedulesList=[];
        this.loadProfileSchedules=false;
    }
    fetchTemplateDetails(id)
    {
        //console.log('fetchTemplateDetails(temp[0])'+id);
        this.remoteValue= 'detail'+'-'+id;
        this.showTemplateDetail=true;
        
    }
    hideDialog(event){
        this.dataManagementWqService.loadScheduleContent=event;
        }
    proceedForExtraction(spaId)
    {
        this.dataManagementWqService.proceedForExtraction(spaId,this.filesAdded[0]).subscribe(
                (res: Response) => {
                   // console.log('response is here');
                });
    }
    proceedForExtractionWithUrl(spaId,accessToken, fileId)
    {
        this.dataManagementWqService.proceedForExtractionWithFileId(spaId,accessToken, fileId).subscribe(
                (res: Response) => {
                   // console.log('response is here');
                });
    }
    
    deleteLines(id)
    {
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { deleteLines:'delete lines', ok: 'ok', cancel: 'cancel', message:'Are you sure to delete line?' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                {
                this.dataManagementWqService.deleteLines(id).subscribe(
                        (res: Response) => {
                        });
                }
            else
                {
                
                }
        });
      
    }
    deleteAllLines(srcFileInbId)
    {
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { deleteLines:'delete lines', ok: 'ok', cancel: 'cancel', message:'Are you sure to clear data?' }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok')
                {
                this.dataManagementWqService.deleteAllLines(srcFileInbId).subscribe(
                        (res: Response) => {
                        });
                }
            else
                {
                
                }
        });
       
    }
    searchData()
    {
        console.log('searchWord'+this.dataManagementWqService.searchData);
        if(this.dataManagementWqService.searchData && this.dataManagementWqService.searchData != null && this.dataManagementWqService.searchData != '')
        {
        this.dataManagementWqService.search(
                {
                    page:this.batchpageEvent.pageIndex + 1,
                    size:this.batchPageSize,
                }).subscribe(
                        (res: any) => {
                            this.batchData=[];
                            this.batchData=res;
                            if(this.batchData && this.batchData.length > 0)
                            {
                            if(  this.batchData[0].totalCount)
                                this.batchLength =  this.batchData[0].totalCount;
                               this.showViewBy =true;
                            }
                          
                        },
                        (res: Response) => {
                            this.onError(res)
                            this.notificationService.error('Error while searching', '');
                        }
                    );
        }
    else
        {
        this.fetchBatchHeadersList(null,'profile');
        }
    }
    private onSuccess(data, headers) {
        if(this.sourceProfilesService.searchData)
        this.links = this.parseLinks.parse(headers.get('link'));
        this.batchLength = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.batchData = data.json;
        //console.log('  this.fileTemplates printed below'+JSON.stringify(  this.fileTemplates));
    }
    confirmAsSuccess(row,i)
    {
        console.log('index to success'+i)
        this.linesListBasedOnFile[i].recordStatus='SUCCESS';
        if(!this.linesToUpdate)
        this.linesToUpdate=[];
        let count : number = 0;
        this.linesToUpdate.forEach(line=>{
            if(line.id == row.id)
                {
                count = count +1;
                }
                
        });
        if(count == 0)
            {
           // console.log('count is == 0'+count);
            
            this.linesToUpdate.push(row);
            }
    }
    filterLinesByStatus(spId, tempId, fileName, srcInbHistId, bhDetIndex)
    {
        console.log('linesStatusFilter'+this.linesStatusFilter);
        this.fetchLinesBySrcFileInb(spId,tempId,fileName,srcInbHistId,bhDetIndex); 
    }
    onSPaginateChange(event) {
        this.page = event.pageIndex;
        this.batchPageSize = event.pageSize;
        console.log('pagination params'+ this.page +'=>'+this.batchPageSize);
       this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
      }
    onBatchDetailPaginateChange(event)
    {
        this.batchDetailPage=event.pageIndex;
        this.batchDetailPageSize=event.pageSize;
        this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
    }
    onLinePaginateChange(event) {
        this.linePage = event.pageIndex;
        this.linesPageSize = event.pageSize;
        console.log('pagination params'+ this.linePage +'=>'+this.linesPageSize);
        this.filterLinesByStatus(this.sourceProfilesForTenant[this.selectedTab].id,this.selectedFilesTempId,this.selectedFileName,this.selectedSrcInbHistId,this.bhDetIndex); 
      
      }
    searchProfileData()
    {
        // fetchSPs(this.statusList[this.selectedProfileStatus])
        console.log('this.sourceProfilesService.searchData'+this.sourceProfilesService.searchData);
        if(this.sourceProfilesService.searchData && this.sourceProfilesService.searchData != null && this.sourceProfilesService.searchData != '')
        {
            let filteredPrfos : any =[];
            let addedProfileIds : number[]=[];
            this.sourceProfilesForTenant.forEach(prof=>{
                if(prof.sourceProfileName)
                if(prof.sourceProfileName.toLowerCase().search(this.sourceProfilesService.searchData.toLowerCase()) != -1)
                {
                    filteredPrfos.push(prof);
                    addedProfileIds.push(prof.id);
                }
               
            });
            //{{profile.connectionType}} - {{profile.connectionName}}
            this.sourceProfilesForTenant.forEach(prof=>{
                if(prof.connectionType)
                if(prof.connectionType.toLowerCase().search(this.sourceProfilesService.searchData.toLowerCase()) != -1  && addedProfileIds.indexOf(prof.id) <= -1){
                    filteredPrfos.push(prof);
                    addedProfileIds.push(prof.id);
                }
            });

            this.sourceProfilesForTenant.forEach(prof=>{
                if(prof.connectionName)
                if(prof.connectionName.toLowerCase().search(this.sourceProfilesService.searchData.toLowerCase()) != -1  && addedProfileIds.indexOf(prof.id) <= -1){
                    filteredPrfos.push(prof);
                    addedProfileIds.push(prof.id);
                }
            });
            this.sourceProfilesForTenant=filteredPrfos;
        }
        else
            {
                  this.fetchSPs(this.statusList[this.selectedProfileStatus]);
            }
            // this.sourceProfilesService.search(
            //         {
            //             page:null,
            //             size:null,
            //         }).subscribe(
            //                 (res: any) => {
            //                     let resp :any=[];
            //                 resp=res;
            //                 if(resp && resp.length>0)
            //                     {
            //                     let status:boolean;
            //                 let filteredProfs = [];
            //                 if(this.statusList[this.selectedProfileStatus]=='Active')
            //                     {
                              
            //                     resp.forEach(sp=>{
            //                         if(sp && (sp.enabledFlag == true ))
            //                             {
            //                                 filteredProfs.push(sp);
            //                             }
            //                     });
            //                     }
            //                 else  if(this.statusList[this.selectedProfileStatus]=='InActive')
            //                     {
            //                     resp.forEach(sp=>{
            //                         if(sp && (sp.enabledFlag ==false ))
            //                             {
            //                                 filteredProfs.push(sp);
            //                             }
            //                     });
            //                     }
            //                 else
            //                     {
            //                         this.sourceProfilesForTenant = filteredProfs; 
            //                     }
            //                     }
            //                 else
            //                     {
            //                     this.notificationService.info('No data found on search','');
            //                     }
            //                 });
           
    }
    refreshBatchData()
    {
         this.fetchSPs(this.statusList[this.selectedProfileStatus]);
    }
    showOrHideAdditionalFilters()
    {
        if(this.showAdditionalFilters == true)
        this.showAdditionalFilters=false;
       else if(this.showAdditionalFilters == false)
        this.showAdditionalFilters=true;
    }
    statusFilterOptions()
    {
        if(this.statusFilter == true)
        this.statusFilter=false;
       else  if(this.statusFilter == false)
        this.statusFilter=true;
    }
    getbatch()
    {

    }

    toggleHeader()
    {
        this.toggleTempAcc=true;
    }
}
