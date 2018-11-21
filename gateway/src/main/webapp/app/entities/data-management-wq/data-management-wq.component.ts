 import { Component, OnInit, OnDestroy, Inject, ViewChild ,HostListener} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

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
import {SourceProfileswithConnections,SourceProfileAndTemplate} from '../../entities/source-profiles';
import {SourceProfileFileAssignments} from '../../entities/source-profile-file-assignments';
import {FileTemplatesEditComponent} from '../file-templates/file-templates-edit.component';
import {pageSizeOptionsList} from '../../shared/constants/pagination.constants';
import { DatePipe } from '@angular/common';
//import {steps} from '../../shared/primeng/primeng';
import { Scheduling } from '../scheduling/scheduling.component';
declare var $: any;
declare var jQuery: any;
declare var gapi: any;
declare var google: any;
declare var Dropbox: any;
declare var dropboxfile: any;
//declare var reason:any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { SourceProfiles } from '../source-profiles';
import { SourceConnectionDetailsService, SourceProfile } from '../source-connection-details';

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
            transition(':enter', [
                style({ transform: 'translateX(100%)' }),
                animate(500)
            ])
        ]),
        trigger('slide', [
            transition(':enter', [
              style({
                transform: 'translateY(-110%)'
              }),
              animate('0.3s')
            ]),
            transition(':leave', [
              animate('0.3s', style({
                transform: 'translateY(-110%)'
              }))
            ])
          ],),
          trigger('slideleft', [
              transition(':enter', [
                style({
                  transform: 'translateX(110%)'
                }),
                animate('0.3s')
              ]),
              transition(':leave', [
                animate('0.3s', style({
                  transform: 'translateX(110%)'
                }))
              ])
            ],)
    ]
})
export class DataManagementWqComponent implements OnInit {
    showTempoo:boolean;
    selectedTemplateIndex:number;
    openTemplateDialog:boolean;
    options = {
        success: function (files) {
            if(files && files.length>0)  {
                this.filesAdded = files;
                files.forEach((file)=>{
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
    innerWidth:any;
    matchForTemplates = false;
    connectionDetailsList=[];
    sourceType='';
    activeIndex = 0;
    items:any;
    isLinear = false;
    firstFormGroup: FormGroup;
    secondFormGroup: FormGroup;
  
    templateName:string;
    selectedTabIndex=0;
    sourceProfile = new SourceProfileswithConnections();
    sourceProfileAndTemplate = new SourceProfileAndTemplate();
    profileFileAssignments = new SourceProfileFileAssignments();
    showSources =false;
    sampleTemplatesList=[];
    //sampleTemplatesList =['Template -1 ','Template -2 ','Template -3 ','Template -4 ','Template -5 '];
    @ViewChild(Scheduling) scheduling: Scheduling;
    toggleTempAcc =false;
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
    @ViewChild('selectedProfileStatus') selectedProfileStatus: any=0;
    showActions  = false;
    mouseOverTemplate: number = -1;
    mouseOverBatchDetail: number = -1;
    viewbyOptions = ['All', 'Extracted', 'Hold', 'Transformed', 'Skipped', 'Errored', 'Scheduled','Manual'];
    viewBy: any = 'All';
    batchDetailsList = [];
    openBatchDetail  = false;
    confirmAction  = false;
    selectedBatch: any;
    showBatchActions  = false;
    jobProgramId = 81;
    Dropbox: any;
    showSearch =false;
    showAdditionalFilters =false;
    batchHistoryColumns = [
       /* { field: 'batchName', header: 'Batch Name', width: '100px', align: 'center' },*/
       
        // { field: 'extractionStatus', header: 'Extracted Status', width: '200px' },
        // { field: 'extractionStatusMeaning', header: 'Extracted Status', width: '15%' },
        // { field: 'extractedDateTime', header: 'Extracted Time', width: '15%'},
        // { field: 'transformationStatus', header: 'Transformed Status', width: '250px'},
        //  { field: 'transformationStatusMeaning', header: 'Transformed Status', width: '15%'},
        // { field: 'transformedDateTime', header: 'Transformed Time', width: '15%' },
        // { field: 'batchType', header: 'Batch Type', width: '10%' },
        { field: 'reference', header: 'Reference', width: '10%'},
        // { field: 'nextSchedule', header: 'Next Schedule', width: '100px' }
        /*{ field: 'Actions', header: 'Actions', width: '100px' }*/
    ];
    scheduleListColumns = [
        { field: 'jobName', header: 'Job Name', width: '25%', align: 'center' },
        { field: 'programName', header: 'Program Type', width: '25%', align: 'center' },
        { field: 'type', header: 'Frequency Type', width: '25%', align: 'center' },
        // { field: 'frequencyDetails', header: 'Frequency Details', width: '100px', align: 'center' }
    ];
    templateDetailsColumnsList = [
//        { field: 'templateName', header: 'Template Name', width: '70px', align: 'center' },
        { field: 'sourceDirectoryPath', header: 'Source Directory Path', align: 'center', width: '34%' }
        // { field: 'localDirectoryPath', header: 'Local Directory Path', align: 'center', width: '34%' }
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
    enableConfirmDialog  = false;
   // loadScheduleContent  = false;
    msgs = [];

    //drop zone
    public uploader: FileUploader = new FileUploader({ url: URL });
    public hasBaseDropZoneOver  = false;
    public hasAnotherDropZoneOver  = false;
    enableLineInfo  = false;
    filesAdded: FileList;
    fileName: any = '';
    file : any;
    uploadedIcon  = false;
    uploadStatus  = false;
    spin  = false;
    schedulesList = [];
    loadSchedules  = false;
    loadProfileSchedules  = false;
    profileSchedulesList = [];
    loadLinesForSelectedFile = [];
    linesListBasedOnFile = [];
    fileTemplateLines = [];
    fileTemplateLineColumns = [];
    lineStatusColumns = [{ field: 'recordStatus', header: 'Status', width: '100px', align: 'center' }];
    masterOrStagingLines = [];
    fieldWrtColMap = {};
    isEditLine  = true;
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
    pageSizeOptions =pageSizeOptionsList;
 
    linesLength: any;
    batchLength: any;
    batchDetailLength: any;
    /*********************/

    //dropzone
    /*********************/
    dropZoneWidthClass: any ;
    templatesTableWidthClass: any = 'col-md-12 ';
    hideDropZoneVar: any = '';
    hideDZone  = true;
    /*********************/
    //temp stores
    /*********************/
    tempStore :any;
    stagingData ;
    /*********************/
    showProfileOptions =false;
    viewAllBatches:any='selectedProfile';
    
    /***************/
    //matchedTemplates:any;
   // showMatchedTemplates  =false;
    mouseOnMatchedTemp:any=-1;
    
    /***************/
    
    
    /********************/
    schedulingObj = new SchedulingModel();
    /********************/
    programmeList = ["DataExtraction","DataTransformation"];
    parametersList:Parameters[] =[new Parameters()];
    displayParameters:Parameters[] =[new Parameters()];
    remoteValue ='';
    showTemplateDetail =false;
    
    showBatchHeaderActions =false;
    templateNameForSchedulesDisplay : any;
   // transformImmediately :any;
    linesToUpdate:any = [];
    linesStatusFilter : any='Fail';
    
    selectedFileName : any;
    selectedSrcInbHistId : any;
    bhDetIndex : any;
    selectedFilesTempId : any;
    hideDropZoneToggler  = false;
    
    showViewBy =false;
    
    batchDetailColumnList = [
                              { field: 'templateName', header: 'Template Name', width: '30%', align: 'center' },
                            //  { field: 'fileName', header: 'File Name', width: '40%', align: 'center' },
                            //  { field: 'fileSize', header: 'File Size', width: '70px', align: 'center' },
                             { field: 'fileTransformedDate', header: 'Transformed Date', width: '15%', align: 'center' },
                             { field: 'lineCount', header: 'Lines Transformed', width: '15%', align: 'right' },
                              { field: 'statusMeaning', header: 'Status', width: '150px', align: 'center' },
                            //  { field: 'status', header: 'Status', width: '8%', align: 'center' },
                            //  { field: 'failedReason', header: 'Failed status', width: '70px', align: 'center' },
                            { field: 'holdReason', header: 'Hold Reason', width: '20%', align: 'center' },
                            // { field: 'Actions', header: 'Actions', width: '15%', align: 'center' }
                             ];
    indexToFetchBatchDetail : any;
    displayTemplateCheckbox =true;
    displayBatchCheckbox =true;
    displayBatchDetailsCheckbox =true; 
    statusFilter =false;
    selectedStatInd:any;
    selectedSubStatInd:any;
    status:any=['All','Selected Profile'];
    subStatus:any=['All','Errored','Extracted','Transformed','Holded'];
    profilePanelOpenState  = true;
    batchPanelOpenState  = false;
    multipleIdentifiers : any = [];
    selectedIdentifierIndex=0 ;
    displayForm  = false;
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
        private $sessionStorage: SessionStorageService,
        private _formBuilder: FormBuilder,
        private sourceConnectionDetailsService: SourceConnectionDetailsService,

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
        if (this.$sessionStorage.retrieve('userData')){
            this.UserData = this.$sessionStorage.retrieve('userData');
        }
            
        this.dataManagementWqService.searchData=null;
    }

   

    @HostListener('window:resize', ['$event'])
    onResize(event) {
      this.innerWidth = window.innerWidth;
      //console.log('shooo=width'+ this.innerWidth);
     // console.log('shooo=height'+ window.innerHeight);
      if(window.innerHeight>500){
        if($(window).height() > 630){
            $(".source-list-styles").css("height", '625px');
            $(".source-list-styles").css("max-height", '625px');
            $(".source-style").css("height",'505px');
            $(".source-style").css("max-height",'505px');
        }else{
            const heightCal =  $(window).height() -140;
            $(".source-list-styles").css("height", heightCal+'px');
            $(".source-list-styles").css("max-height", heightCal+'px');
            $(".source-style").css("height",(heightCal-120)+'px');
            $(".source-style").css("max-height",(heightCal-120)+'px');
        }
      }else{
       
        $(".source-list-styles").css("height", '500px');
        $(".source-list-styles").css("max-height", '500px');
        $(".source-style").css("height",'380px');
        $(".source-style").css("max-height",'380px');
      }
     
        
     
    }
    ngOnInit() {
       // console.log('read component');
        if($(window).height()>500){
            if($(window).height() > 630){
                $(".source-list-styles").css("height", '625px');
                $(".source-list-styles").css("max-height", '625px');
                $(".source-style").css("height",+'505px');
                $(".source-style").css("max-height",'505px');
            }else{
            const heightCal =  $(window).height() -140;
            $(".source-list-styles").css("height", heightCal+'px');
            $(".source-list-styles").css("max-height", heightCal+'px');
            $(".source-style").css("height",(heightCal-120)+'px');
            $(".source-style").css("max-height",(heightCal-120)+'px');
        }
          }else{
          
            $(".source-list-styles").css("height", '500px');
            $(".source-list-styles").css("max-height", '500px');
            $(".source-style").css("height",320+'px');
            $(".source-style").css("max-height",320+'px');

           
          }
       
        $(document).ready(function() {
            //Initialize tooltips
            $('.nav-tabs > li a[title]').tooltip();
            
            //Wizard
            $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
        
                var $target = $(e.target);
            
                if ($target.parent().hasClass('disabled')) {
                    return false;
                }
            });
        
            $(".next-step").click(function (e) {
        
                var $active = $('.wizard .nav-tabs li.active');
                $active.next().removeClass('disabled');
                nextTab($active);
        
            });
            $(".prev-step").click(function (e) {
        
                var $active = $('.wizard .nav-tabs li.active');
                prevTab($active);
        
            });
        });
        
        function nextTab(elem) {
            $(elem).next().find('a[data-toggle="tab"]').click();
        }
        function prevTab(elem) {
            $(elem).prev().find('a[data-toggle="tab"]').click();
        }
        
        
        //according menu
        
        $(document).ready(function()
        {
            //Add Inactive Class To All Accordion Headers
            $('.accordion-header').toggleClass('inactive-header');
            
            //Set The Accordion Content Width
            var contentwidth = $('.accordion-header').width();
            $('.accordion-content').css({});
            
            //Open The First Accordion Section When Page Loads
            $('.accordion-header').first().toggleClass('active-header').toggleClass('inactive-header');
            $('.accordion-content').first().slideDown().toggleClass('open-content');
            
            // The Accordion Effect
            $('.accordion-header').click(function () {
                if($(this).is('.inactive-header')) {
                    $('.active-header').toggleClass('active-header').toggleClass('inactive-header').next().slideToggle().toggleClass('open-content');
                    $(this).toggleClass('active-header').toggleClass('inactive-header');
                    $(this).next().slideToggle().toggleClass('open-content');
                }
                
                else {
                    $(this).toggleClass('active-header').toggleClass('inactive-header');
                    $(this).next().slideToggle().toggleClass('open-content');
                }
            });
            
            return false;
        });
        this.firstFormGroup = this._formBuilder.group({
            firstCtrl: ['', Validators.required]
          });
          this.secondFormGroup = this._formBuilder.group({
            secondCtrl: ['', Validators.required]
          });
        
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        /* $(".setHeight").css({
            'max-height': this.TemplatesHeight
        });
        $(".etlwqTemplate").css({
            'max-width': (this.commonService.screensize().width) + 'px'
        }); */
       
        this.sourceProfilesService.fetchSourceProfilesAndconDetails(true).subscribe(
            (res: any) => {
                this.sourceProfilesForTenant = res;
                this.selectedTab = 0;
                if (this.sourceProfilesForTenant && this.sourceProfilesForTenant.length > 0 && this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id){
                    
                    this.getTemplateDetails(this.sourceProfilesForTenant[this.selectedTab].id);
                    this.viewAllBatches='all';
                   // this.filterBysProfile();
                    this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                    
                }
               
            });

        //add class to highlight first md card
        $(".profiles.md-tab-label").addClass("mat-tab-label-active");
        this.loadTemplates();
        let i = 0;
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
    ngOnDestroy() {
        this.notificationService.remove();
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
        this.selectAllBatches=false;
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
                           // console.log('batch history fetched is'+JSON.stringify(this.batchData));
                            if(this.batchData && this.batchData.length > 0)
                                {
                                if(  this.batchData[0].totalCount){
                                    this.batchLength =  this.batchData[0].totalCount;
                                }
                                   this.showViewBy =true;
                                   this.showSearch=true;
                                   let cnt : number =0;
                                   this.batchData.forEach(batch=>{
                                       if(batch.hold == true || batch.hold === '1' || 
                                       batch.transformationStatus || (batch.extractionStatusMeaning === '0 file(s) - Extracted'
                                    || batch.extractionStatus === 'No File(s)'
                                    ) ){
                                           cnt=cnt+1;
                                       }
                                   });
                                   if(this.batchData.length == cnt){
                                       this.displayBatchCheckbox = false;
                                   }  else{
                                        this.displayBatchCheckbox=true;
                                   }
                                }
                            else
                       {
                                if(this.viewAllBatches == 'all')
                                    {
                                    this.commonService.info('Info','No batches found');
                                    }
                                else
                                    {
                                    this.commonService.info('Info','No batches found for '+ this.sourceProfilesForTenant[this.selectedTab].sourceProfileName);
                                    }
                               
                                this.showViewBy =false;
                                this.showSearch=false;
                       }
                       this.displayForm = true;
                        },
                        err => {
                            this.displayForm = true;
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
                           // console.log('batch history fetched is'+JSON.stringify(this.batchData));
                            if(this.batchData && this.batchData.length > 0)  { 
                                if(  this.batchData[0].totalCount){
                                    this.batchLength =  this.batchData[0].totalCount;
                                }
                                    this.showViewBy =true;
                                    this.showSearch=true;
                                    let cnt=0;
                                    this.batchData.forEach((batch)=>{
                                        if(batch.hold == true || batch.hold === '1' || batch.transformationStatus ||(batch.extractionStatusMeaning === '0 file(s) - Extracted'
                                        || batch.extractionStatus === 'No File(s)'
                                    )){
                                            cnt=cnt+1;
                                        }
                                    });
                                    if(this.batchData.length == cnt){
                                        this.displayBatchCheckbox = false;
                                    }  else{
                                        this.displayBatchCheckbox=true;
                                    }
                                } else {
                                if(this.viewAllBatches == 'all') {
                                this.commonService.info('Info','No batches found');
                                }  else {
                                this.commonService.info('Info','No batches found for '+ this.sourceProfilesForTenant[this.selectedTab].sourceProfileName);
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
            if (!this.batchDetailsList[i].hold || this.batchDetailsList[i].hold === '0')
                this.batchDetailsList[i].selected = this.BHselectedAll;
        }
        this.enableBatchDetailActions();
    }
    selectAll() {
        for (var i = 0; i < this.dataManagementWqService.fileTemplatesList.length; i++) {
            if (!this.dataManagementWqService.fileTemplatesList[i].hold ||this.dataManagementWqService.fileTemplatesList[i].hold === '0')
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
        let count = 0;
        this.batchDetailsList.forEach((batchDetail) => {
            if (batchDetail.selected){
                count = count + 1;
            }
               
        });
        if (count > 0){
            this.showBatchActions = true;
        
        }else{
            this.showBatchActions = false;
          
        }
           
    }


    enableActions() {
        let count = 0;
        this.dataManagementWqService.fileTemplatesList.forEach((fileTemplate) => {
            if (fileTemplate.selected){
                count = count + 1;
            }
               
        });
        if (count > 0){
            this.showActions = true;
        } else{
            this.showActions = false;
        } 
        
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
                    if(template.hold==true || template.hold === '1' || template.hold === 1)
                        {
                            count=count+1;
                        }
                });
                if(this.dataManagementWqService.fileTemplatesList.length == count)
                    this.displayTemplateCheckbox = false;
                    else
                    this.displayTemplateCheckbox=true;
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
        let status = this.statusList[e.value];
        this.selectedProfileStatus=e.value;
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
        this.scheduling.ngOnInit();
        this.schedulingObj.transformImmediately =null;
        this.schedulingObj.programName=null;
            let  paramSet = {};
            let displayParamSet = {};
            this.parametersList=[];
            let count:number=0;
            let fileTemplates: any = '';
            let fileTemplateNames: any = '';
            this.schedulingObj.jobName = this.sourceProfilesForTenant[this.selectedTab].sourceProfileName+'-'+this.schedulingObj.programName;
            if(level == 'profile')
            {
             paramSet = { 
                 "paramName":"SourceProfileName",
                    "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id
                    
                };
            displayParamSet = { 
                    "paramName":"SourceProfileName",
                       "selectedValue": this.sourceProfilesForTenant[this.selectedTab].sourceProfileName
                       
                   };      

            this.displayParameters.push(displayParamSet);          
             this.parametersList.push(paramSet);

             paramSet = {
                     "paramName":"TemplateName",
                          "selectedValue": null
                      };
            displayParamSet = {
                        "paramName":"TemplateName",
                             "selectedValue": null
                         };
            this.displayParameters.push(displayParamSet); 
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
                      displayParamSet = {
                                "paramName":"BatchHeader",
                                     "selectedValue": null
                                 };
                    this.displayParameters.push(displayParamSet); 
                    this.parametersList.push(paramSet);

                            paramSet = {
                                   "paramName":"BatchHeaderDetail",
                                      "selectedValue": null
                                  };
                                  displayParamSet = {
                                    "paramName":"BatchHeaderDetail",
                                       "selectedValue": null
                                   };
                            this.displayParameters.push(displayParamSet); 
                           this.parametersList.push(paramSet);
                          
                           
                      }
                  else
                      {
                      let paramSet = {
                              "paramName":"TransformImmediately",
                              "selectedValue": false
                              };
                     let displayParamSet = {
                                "paramName":"TransformImmediately",
                                "selectedValue": false
                                };
                            this.displayParameters.push(displayParamSet); 
                              this.parametersList.push(paramSet);
                      }
                 
                  
             this.schedulingObj.parameters = this.parametersList;
             this.schedulingObj.displayParameters = this.displayParameters;
            }
            else if(level == 'template')
                {
                let i = 0;
                paramSet = {
                   "paramName":"SourceProfileName",
                        "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id,
                    };
                    displayParamSet = {
                        "paramName":"SourceProfileName",
                             "selectedValue": this.sourceProfilesForTenant[this.selectedTab].sourceProfileName,
                         };
                this.displayParameters.push(displayParamSet); 
                this.parametersList.push(paramSet);
                if(this.dataManagementWqService.fileTemplatesList.length == 1){
                    fileTemplates = this.dataManagementWqService.fileTemplatesList[0].templateId;
                    count =1;
                } else{
                this.dataManagementWqService.fileTemplatesList.forEach((ft) => {
                    if (ft.selected == true) {
                        if (i == this.dataManagementWqService.fileTemplatesList.length - 1) {
                            fileTemplates = fileTemplates + ft.templateId;
                            fileTemplateNames = fileTemplateNames+ft.templateName;
                        }  else {
                            fileTemplates = fileTemplates + ft.templateId + ',';
                            fileTemplateNames = fileTemplateNames+ft.templateName;+',';
                        }
                        count = count+1;
                    }
                    i = i + 1;
                });
            }
                paramSet = {
                     "paramName":"TemplateName",
                        "selectedValue": fileTemplates
                    };
                    displayParamSet = {
                        "paramName":"TemplateName",
                           "selectedValue": fileTemplateNames
                       };
                this.displayParameters.push(displayParamSet); 
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
                              displayParamSet = {
                                "paramName":"BatchHeader",
                                     "selectedValue": null
                                 };
                          this.parametersList.push(paramSet);
                          this.displayParameters.push(displayParamSet); 
                          paramSet = {
                                  "paramName":"BatchHeaderDetail",
                                     "selectedValue": null
                                 };
                                 displayParamSet = {
                                    "paramName":"BatchHeaderDetail",
                                       "selectedValue": null
                                   };
                          this.parametersList.push(paramSet);
                          this.displayParameters.push(displayParamSet); 
                 }   
                 else
                 {
                 let paramSet = {
                         "paramName":"TransformImmediately",
                         "selectedValue": false
                         };
                         let displayParamSet = {
                            "paramName":"TransformImmediately",
                            "selectedValue": false
                            };
                         this.parametersList.push(paramSet);
                         this.displayParameters.push(displayParamSet); 
                 }
            
                 this.schedulingObj.parameters = this.parametersList;
                 this.schedulingObj.displayParameters = this.displayParameters;
                }
            else if(level == 'batchHeader')
                {
                let i = 0;
                let batchHeaderIds : any='' ;
                let batchHeaderNames : any='' ;
                paramSet = {
                   "paramName":"SourceProfileName",
                        "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id,
                    };
                    displayParamSet = {
                        "paramName":"SourceProfileName",
                             "selectedValue": this.sourceProfilesForTenant[this.selectedTab].sourceProfileName,
                         };
                this.parametersList.push(paramSet);
                this.displayParameters.push(displayParamSet); 
                
                paramSet = {
                        "paramName":"TemplateName",
                             "selectedValue": null
                         };
                displayParamSet = {
                            "paramName":"TemplateName",
                                 "selectedValue": null
                             };
                this.displayParameters.push(displayParamSet); 
                     this.parametersList.push(paramSet);
                     
                     /*paramSet = {
                             "paramName":"ConnectionName",
                                  "selectedValue": null
                              };
                          this.parametersList.push(paramSet);*/
                
                     if(this.schedulingObj.programName == 'DataTransformation'){
                         if( this.batchData.length==1){
                            batchHeaderIds = this.batchData[0].id;
                            batchHeaderNames = this.batchData[0].batchName;
                         }else{
                            this.batchData.forEach((batch)=>{
                                if(batch.selected == true) {
                                    if (i == this.batchData.length - 1) {
                                        batchHeaderIds = batchHeaderIds + batch.id;
                                        batchHeaderNames = batchHeaderNames+batch.batchName;
                                    } else
                                        {
                                        batchHeaderIds = batchHeaderIds + batch.id+',';
                                        batchHeaderNames = batchHeaderNames+batch.batchName+',';
                                        }
                                    }
                                i=i+1;
                            });
                         }
                
                paramSet = {
                        "paramName":"BatchHeader",
                           "selectedValue": batchHeaderIds
                       };
                       displayParamSet = {
                        "paramName":"BatchHeader",
                           "selectedValue": batchHeaderNames
                       };
                this.parametersList.push(paramSet);
                this.displayParameters.push(displayParamSet); 
                     }
                this.schedulingObj.parameters = this.parametersList;
                this.schedulingObj.displayParameters = this.displayParameters;
                    this.schedulingObj.programName='DataTransformation';
                }
            else 
            {
                
                let i = 0;
                let batchDetailIds : any ='';
                let batchDetailNames : any ='';
                paramSet = {
                   "paramName":"SourceProfileName",
                        "selectedValue": this.sourceProfilesForTenant[this.selectedTab].id,
                    };
                    displayParamSet = {
                        "paramName":"SourceProfileName",
                             "selectedValue": this.sourceProfilesForTenant[this.selectedTab].sourceProfileName,
                         };
                this.displayParameters.push(displayParamSet); 
                this.parametersList.push(paramSet);
                paramSet = {
                        "paramName":"TemplateName",
                             "selectedValue": ''
                         };
                displayParamSet = {
                            "paramName":"TemplateName",
                                 "selectedValue": ''
                             };
                this.displayParameters.push(displayParamSet); 
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
                        displayParamSet = {
                                    "paramName":"BatchHeader",
                                         "selectedValue": ''
                                     };
                        this.displayParameters.push(displayParamSet); 
                        this.parametersList.push(paramSet);
                          if( this.batchDetailsList.length==1){
                            batchDetailIds = batchDetailIds + this.batchDetailsList[0].id;
                            batchDetailNames = batchDetailNames + this.batchDetailsList[0].fileName;
                          }else{
                            this.batchDetailsList.forEach((batchDetail)=>{
                                if(batchDetail.selected == true) {
                                    if (i == this.batchDetailsList.length - 1) {
                                        batchDetailIds = batchDetailIds + batchDetail.id;
                                        batchDetailNames = batchDetailNames + batchDetail.fileName;
                                    }  else
                                        {
                                        batchDetailIds = batchDetailIds + batchDetail.id+',';
                                        batchDetailNames = batchDetailNames + batchDetail.fileName;
                                        }
                                    }
                                i=i+1;
                            });
                          }
               
                paramSet = {
                        "paramName":"BatchHeaderDetail",
                           "selectedValue": batchDetailIds
                       };
                displayParamSet = {
                        "paramName":"BatchHeaderDetail",
                           "selectedValue": batchDetailNames
                       };
                this.parametersList.push(paramSet);
                this.displayParameters.push(displayParamSet); 
                     }
                this.schedulingObj.parameters = this.parametersList;
                this.schedulingObj.displayParameters = this.displayParameters;
                    this.schedulingObj.programName='DataTransformation';
            }
                
        

    }
    //('profile',null,null,null,null)
    uncheckAll(){
        this.selectedAll=false;
        if(this.dataManagementWqService && this.dataManagementWqService.fileTemplatesList && this.dataManagementWqService.fileTemplatesList.length){
            this.dataManagementWqService.fileTemplatesList.forEach((temp)=>{
                if(temp && temp.selected){
                    temp.selected = false;
                }
            });

        }
        this.selectAllBatches=false;
        if(this.batchData && this.batchData.length>0){
            this.batchData.forEach((batch)=>{
                batch.selected=false;
            });
        }
        this.BHselectedAll=false;
        this.batchDetailsList.forEach((batchDetail)=> {
               
            batchDetail.selected = false;
         
        });
    }
    extractNow(level, profileId, templateId, batchHeaderId, batchDetailId) {
        this.jobDetailsService.oozieDBTest().subscribe(
            (res: Response) => {
                const resp: any=res;
              if (resp.ooziestatus== true) {

             
        //console.log('extract now with' + JSON.stringify(this.dataManagementWqService.fileTemplatesList));
        let fileTemplates: any = '';
    let i = 0;
    //comma separated templates, no connection should be sent, as it handles in code itself
    let  paramSet = {};
    let action ='';
    let count=0;
    if(level == 'profile') {
         paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id
            };
         action=this.sourceProfilesForTenant[this.selectedTab].sourceProfileName;
        }  else if(level == 'template')  {
            if(this.dataManagementWqService.fileTemplatesList.length == 1){
                fileTemplates = this.dataManagementWqService.fileTemplatesList[0].templateId;
                count =1;
            }  else{
                this.dataManagementWqService.fileTemplatesList.forEach((ft) => {
                    if (ft.selected == true) {
                        if (i == this.dataManagementWqService.fileTemplatesList.length - 1) {
                            fileTemplates = fileTemplates + ft.templateId;
                        } else {
                            fileTemplates = fileTemplates + ft.templateId + ',';
                        }
                        count = count+1;
                    }
                    i = i + 1;
                });
            }
       
        action = this.sourceProfilesForTenant[this.selectedTab].sourceProfileName +' templates';
         paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id,
                "param2": fileTemplates
            };
        }  else if(level == 'batch') {
        let batchDetailsIds: any = '';
    let j = 0;
    this.batchDetailsList.forEach((batchDetail) => {
        if (batchDetail.selected == true) {
            if (j == this.batchDetailsList.length - 1) {
                batchDetailsIds = batchDetailsIds + batchDetail.templateId;
            } else {
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
        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            data: { processBy:level, ok: 'ok', cancel: 'cancel', action: action ,process:'extraction',count:count,level:level}
        });
        dialogRef.afterClosed().subscribe((result) => {
        
       
            if (result && result == 'ok'){
        this.jobDetailsService.initiateOnDemandTypeJob('DataExtraction', this.UserData, paramSet).subscribe( 
            (res:any) => {
                const status: any =res;
                if(status) {
                   console.log('status after initiating job '+JSON.stringify(status));
                    if(status && status.status)  { 
                        this.commonService.success('SUCCESS', 'Extraction Process Initiated. Job Id: '+status.status);      
                        this.uncheckAll();  
                        //refresh batches
                        this.refreshBatchData();
                    } else {
                        this.commonService.error('Warning!', status.status);
                        this.uncheckAll();
                        this.refreshBatchData();
                    }
                

                }
              
               
            },
            (res: Response) => {
                this.onError(res)
                this.commonService.error('Warning!', 'Extraction Process Initiation Failed!');
                this.uncheckAll();
                this.refreshBatchData();
            }
        );}
    });
     }  else{
        this.commonService.error('Sorry!', 'Server is unstable!'/* , {
            timeOut: 4000,
            showProgressBar: false,
            pauseOnHover: true,
            clickToClose: true,
            maxLength: 100
        } */);
        this.uncheckAll();
        this.refreshBatchData();
     }
            });
    }
    transformNow(level, profileId, templateId, batchHeaderId, batchDetailId) {
         this.jobDetailsService.oozieDBTest().subscribe(
            (res: Response) => {
                let resp : any=res;
              if (resp.ooziestatus== true) {

        //console.log('transform now with' + JSON.stringify(this.dataManagementWqService.fileTemplatesList));
        let fileTemplates: any = '';
    let i = 0;
    let lev : any;
    let count=0;
    let paramSet = {};
    if(level == 'profile')  {
        lev = 'profile';
        paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id
            };
    } else if(level == 'template') {
        lev = 'template';
        if(this.dataManagementWqService.fileTemplatesList.length == 1){
            fileTemplates = this.dataManagementWqService.fileTemplatesList[0].templateId;
            count =1;
        } else{
            this.dataManagementWqService.fileTemplatesList.forEach((ft) => {
                if (ft.selected == true) {
                    if (i == this.dataManagementWqService.fileTemplatesList.length - 1) {
                        fileTemplates = fileTemplates + ft.templateId;
                    } else {
                        fileTemplates = fileTemplates + ft.templateId + ',';
                    }
                    count = count+1;
                }
                i = i + 1;
            });
        }
       
        paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id,
                "param2": fileTemplates
            };
        } else if(level == 'batchHeader'){
        lev = 'batch header';
        let batchHeaderIds: any ='';
    let j=0;
    if(this.batchData.length ==1){
        batchHeaderIds = this.batchData[0].id;
        count=1;
    }else{
        this.batchData.forEach((batch)=>{
            if(batch.selected == true){
                if(j == this.batchData.length -1){
                batchHeaderIds = batchHeaderIds+batch.id;
            }else  {
                batchHeaderIds = batchHeaderIds+batch.id+',';
                }
                count = count+1;
        }
            j=j+1;
        });
    }
  
    paramSet = {
            "param1": this.sourceProfilesForTenant[this.selectedTab].id,
            //"param2": fileTemplates,
            "param3": batchHeaderIds
        };
    }  else  {
        lev = 'batch detail';
        //comma separated templates, no connection should be sent, as it handles in code itself
        let batchDetailsIds: any = '';
        let j = 0;
        if( this.batchDetailsList.length==1){
            count =1;
            batchDetailsIds = this.batchDetailsList[0].srcInbHistId;
        }else{
            this.batchDetailsList.forEach((batchDetail) => {
                if (batchDetail.selected == true) {
                    if (j == this.batchDetailsList.length - 1) {
                        batchDetailsIds = batchDetailsIds + batchDetail.srcInbHistId;
                    } else {
                        batchDetailsIds = batchDetailsIds + batchDetail.srcInbHistId + ',';
                    }
                    count = count+1;
                }
                j = j + 1;
            });
        }
       
        paramSet = {
                "param1": this.sourceProfilesForTenant[this.selectedTab].id,
              //  "param2": fileTemplates,
                "param3": this.batchData[this.indexToFetchBatchDetail].id,
                "param4":batchDetailsIds
            };
    }
    
   
  // console.log('paramSet before job creation'+JSON.stringify(paramSet));
        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            data: { processBy:level, ok: 'ok', cancel: 'cancel', 
                action:  this.sourceProfilesForTenant[this.selectedTab].sourceProfileName,
                process:'transformation',count : count,level:lev}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok'){
        this.jobDetailsService.initiateOnDemandTypeJob('DataTransformation', this.UserData, paramSet).subscribe(
            (res: any) => {
                this.commonService.success('SUCCESS', 'Transformation Process has been initiated Successfully');
                this.uncheckAll();
            },
            (res: Response) => {
                this.onError(res)
                this.commonService.error('Warning!', 'Transformation failed!');
                this.uncheckAll();
            }
        );
        }
        });
    } else{
        this.commonService.error('Sorry!', 'Server is unstable!'/* , {
            timeOut: 4000,
            showProgressBar: false,
            pauseOnHover: true,
            clickToClose: true,
            maxLength: 100
        } */);
        this.uncheckAll();
     }
    });
    }
    openDialog(indexToHold): void {
        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { ind: indexToHold, ok: 'ok', cancel: 'cancel' }
        });
        dialogRef.afterClosed().subscribe((result) => {
        });
    }
    holdTemplate(spaid) {
        this.dataManagementWqService.reason=null;
        let reason: any;
        if(this.sourceProfilesForTenant[this.selectedTab].holdReason == undefined || this.sourceProfilesForTenant[this.selectedTab].holdReason == null){
            
        }   else{
            reason =  this.sourceProfilesForTenant[this.selectedTab].holdReason+'';
        }
        
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: spaid, ok: 'ok',reason:'', cancel: 'cancel', action: 'hold',previousReason:reason }
        });
        dialogRef.afterClosed().subscribe((result) => {
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
    unHoldTemplate(spaid,reason) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: spaid, ok: 'ok', cancel: 'cancel', action: 'Release Hold' ,previousReason:reason}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok'){
                this.dataManagementWqService.unHoldTemplate(spaid).subscribe(
                    (res: any) => {
                        this.releasedHold();
                        this.getTemplateDetails(this.sourceProfilesForTenant[this.selectedTab].id);
                    });
            }
               
        });

    }
    holdBatchDetail(srcFileInbHistId,reason) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: srcFileInbHistId, ok: 'ok', cancel: 'cancel', action: 'hold',previousReason:reason }
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok'){
                this.dataManagementWqService.holdBatchDetail(srcFileInbHistId).subscribe(
                    (res: any) => {
                        //refresh batchdetails
                        this.holded();
                        this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
                    });
            }
                
        });

    }
    releaseBatchDetailHold(srcFileInbHistId,reason) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: srcFileInbHistId, ok: 'ok', cancel: 'cancel', action: 'Release Hold',previousReason:reason }
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok'){
                this.dataManagementWqService.releaseBatchDetailHold(srcFileInbHistId).subscribe(
                    (res: any) => {
                        this.releasedHold();
                        this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
                    });
            }
              
        });

    }
    holdBatch(batchId,reason) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: batchId, ok: 'ok', cancel: 'cancel', action: 'hold',previousReason:reason }
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok'){
                this.dataManagementWqService.holdBatch(batchId).subscribe(
                    (res: any) => {
                        this.holded();
                        if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id){
                            this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                        }
                           
                    });
            }
               
        });

    }
    releaseBatchHold(batchId,reason) {
        this.dataManagementWqService.reason=null;
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { id: batchId, ok: 'ok', cancel: 'cancel', action: 'Release Hold' ,previousReason:reason}
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok'){

                this.dataManagementWqService.releaseBatchHold(batchId).subscribe(
                    (res: any) => {
                        this.releasedHold();
                        if (this.sourceProfilesForTenant[this.selectedTab] && this.sourceProfilesForTenant[this.selectedTab].id){
                            this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
                        }
                     
                    });
            }
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
        if(this.batchData && this.batchData[ind] && this.batchData[ind].id){
            batchId = this.batchData[ind].id;
        }
      
      //  console.log('this.batchDetailPageEvent.pageIndex + 1, this.batchDetailPageSize,batchId'+this.batchDetailPageEvent.pageIndex +'=>'+ this.batchDetailPageSize+'=>'+batchId)
        this.dataManagementWqService.fetchBatchDetailsById(this.batchDetailPage, this.batchDetailPageSize,batchId).subscribe(
            (res: any) => {
                this.batchDetailsList = res;
               // console.log('this.batchDetailsList'+JSON.stringify(this.batchDetailsList));
                if (this.batchDetailsList && this.batchDetailsList.length>0){
                    if(this.batchDetailsList[0] && this.batchDetailsList[0].totalCount){
                        this.batchDetailLength = this.batchDetailsList[0].totalCount;
                    }
                   
                    let cnt=0;
                    this.batchDetailsList.forEach((batchDetail) => {
                        batchDetail['selected'] = false;
                        if(batchDetail.fileName){
                            let index =0;
                            index = batchDetail.fileName.lastIndexOf('_');
                            let extInd = batchDetail.fileName.lastIndexOf('.');
                            let extensn =  batchDetail.fileName.toString().substring(extInd,batchDetail.fileName.toString().length);
                            batchDetail.fileName = batchDetail.fileName.toString().substring(0,index)+extensn;
                        }
                        if((batchDetail.status == 'LOADED') || (batchDetail.status == 'LOADING_FAILED')  || (batchDetail.status != 'LOADED' && (batchDetail.hold == true || batchDetail.hold == '1'))){
                            cnt=cnt+1;
                        }
                    });
                    this.openBatchDetail = true;
                    if(this.batchDetailsList.length == cnt){
                        this.displayBatchDetailsCheckbox=false;
                    }  else{
                        this.displayBatchDetailsCheckbox=true;
                    }
                        
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
        this.commonService.success('SUCCESS', 'Hold successful!');
    }
    holdFailed() {
        this.commonService.error('Warning!', 'Failed to Hold');
    }
    releasedHold() {
        this.commonService.success('SUCCESS', 'Releasing hold successful!');
    }
    releaseHoldFailed() {
        this.commonService.error('Warning!', 'Failed to release Hold!');
    }
    public fileOverBase(file: File): void {
        if (file){
            this.hasBaseDropZoneOver = true;
        }
       // console.log('hasBaseDropZoneOver' + this.hasBaseDropZoneOver);
       // console.log('uploader?.queue?.length' + this.uploader.queue.length);
    }
    public onFileDrop(filelist: FileList): void {
        this.spin = true;
        const fileItem: FileItem = this.uploader.queue[this.uploader.queue.length - 1];
        this.uploader.queue = [];
        this.uploader.queue[0] = fileItem;
        if (filelist.length > 0) {

            this.filesAdded = filelist;
            const file: File = filelist[0];
            this.fileName = file.name;
          // this.matchTemps();
          this.findMatchedTemplateByFile(file);
        } else{
            this.spin=false;
        }
    }
    
    findMatchedTemplateByFile(file: File ){
        this.dataManagementWqService.findMatchedTemplatesByFile(file).subscribe((res: Response) => {
            this.dataManagementWqService.matchedTemplates =[];
            this.dataManagementWqService.matchedTemplates=res;
            if(this.dataManagementWqService.matchedTemplates && this.dataManagementWqService.matchedTemplates.length>0  ){
                this.matchForTemplates = true;
                this.openTemplateDialog=true;
                this.spin=false;
                this.selectedTemplateIndex=null;
                this.activeIndex=0;
                this.items=[{
                    label: 'Select Templates',
                    command: (event: any) => {
                        this.activeIndex = 0;
                        //this.messageService.add({severity:'info', summary:'First Step', detail: event.item.label});
                    }
                },
                {
                    label: 'Create Pipeline',
                    command: (event: any) => {
                        this.activeIndex = 1;
                      //  this.messageService.add({severity:'info', summary:'Seat Selection', detail: event.item.label});
                    }
                },
                {
                    label: 'Review Details',
                    command: (event: any) => {
                        this.activeIndex = 2;
                       // this.messageService.add({severity:'info', summary:'Pay with CC', detail: event.item.label});
                    }
                }
            ];
                console.log('this.dataManagementWqService.matchedTemplates'+JSON.stringify(this.dataManagementWqService.matchedTemplates));
            } else{
                this.commonService.info('','Sorry! Matching templates not found');
            }
            
        });
    }
    matchTemps()
    {
        this.dataManagementWqService.matchedTemplates=
        [
            [
              "B176064B3F93B68E8C634DCEFF85E04D6408F208",
              "Alpha1MTarget23rdJuly",
              "326"
            ]
          ];
          this.dataManagementWqService.showMatchedTemplates=true;
          this.spin=false;
          this.uploadedIcon=true;
        //this.dataManagementWqService.matchedTemplates.push(['B176064B3F93B68E8C634DCEFF85E04D6408F208','Alpha1MTarget23rdJuly','326']);
        // if(this.dataManagementWqService.fileTemplatesList && this.dataManagementWqService.fileTemplatesList .length>0)
        // {
        //     for(var i=0;i<this.dataManagementWqService.fileTemplatesList.length;i++)
        //     {
        //         this.dataManagementWqService.fileTemplatesList[i]['SPAid'] = this.dataManagementWqService.fileTemplatesList[i]['spaid'];
        //     }
        //     this.dataManagementWqService.matchTemplates(this.fileName, this.dataManagementWqService.fileTemplatesList).subscribe((res: Response) => {
        //         this.dataManagementWqService.matchedTemplates =res; 
        //         this.spin=false;
        //         this.uploadedIcon=true;
        //         this.dataManagementWqService.showMatchedTemplates=true;
        //         //console.log('this.matchedTemplates'+JSON.stringify(this.matchedTemplates));
        //         if(!this.dataManagementWqService.matchedTemplates || ( this.dataManagementWqService.matchedTemplates && this.dataManagementWqService.matchedTemplates.length<=0) )
        //             {
                    
        //             var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
        //                 width: '400px',
        //                 data: { createNew  : 'Template', ok: 'ok', cancel: 'cancel', message:'Matching templates not found. Proceed creating new Template' }
        //             });
        //             dialogRef.afterClosed().subscribe(result => {
        //                 if (result && result == 'ok')
        //                     {
        //                     this.dataManagementWqService.loadCreateTemplateForm=true;
        //                     this.remoteValue='create';
        //                     }
        //                 else
        //                     {
        //                  // this.emptyQueue();
        //                     }
        //             });
                    
                    
        //             }
        //      });
        // }
       
       
    }
    emptyQueue()
    {
      //  this.uploader.queue = [];
        this.uploader = new FileUploader({ url: URL });
        this.uploadedIcon=false;
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
    
    //button = Dropbox.createChooseButton(this.options);
    button :any;
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
                 this.commonService.info('oops!', 'No schedules found for '+this.dataManagementWqService.fileTemplatesList[i].templateName);
                }
            });
    }
    viewProfileSchedules() {
      
        this.dataManagementWqService.fetchSchedulesByProfileOrTemplate(this.sourceProfilesForTenant[this.selectedTab].id,null).subscribe(
            (res: any) => {
                let schedules = res;
                this.profileSchedulesList = [];
                this.profileSchedulesList = schedules;
                if(!this.profileSchedulesList  || this.profileSchedulesList .length<=0)
                    {
                    this.commonService.info('oops!', 'No schedules found for '+this.sourceProfilesForTenant[this.selectedTab].sourceProfileName);
                    }
            });
    }

    fetchTemplateDetailsByTemplateId(tempId,interId) {
        this.fileTemplatesService.fetchTemplateLinesByTemplateIdAndIdentifieId(tempId,interId).subscribe(
            (res: any) => {
                this.fileTemplateLines = [];
                this.fileTemplateLines = res;

                this.fileTemplateLineColumns = [];
               // console.log('file temp lines for'+interId+' are:'+JSON.stringify( this.fileTemplateLines));
                if (this.fileTemplateLines && this.fileTemplateLines.length > 0) {
                    this.fileTemplateLines.forEach(tempLine => {
                        if (!this.fileTemplateLineColumns) {
                            this.fileTemplateLineColumns = [];
                        }
                        let obj = { field: tempLine.columnHeader, header: tempLine.columnHeader, width: '100px', align: 'center' };
                        this.fileTemplateLineColumns.push(obj);
                       
                        if (tempLine.masterTableReferenceColumn) {
                            tempLine.masterTableReferenceColumn = tempLine.masterTableReferenceColumn.replace("_", "");
                            if(tempLine.masterTableReferenceColumn)
                            tempLine.masterTableReferenceColumn = tempLine.masterTableReferenceColumn.toString().toLowerCase();
                            this.fieldWrtColMap[tempLine.masterTableReferenceColumn] = tempLine.columnHeader;
                        }

                    });
                   
                    let  obj : any;
                    if(this.stagingData==true)
                        {
                            let statusObj = { field: 'errorMessage', header: 'Reason', width: '100px', align: 'center' };
                            this.fileTemplateLineColumns.push(statusObj);
                        obj = { field: 'lineContent', header: 'Line Content', width: '100px', align: 'center' };
                        this.fileTemplateLineColumns.push(obj);
                        obj = { field: 'recordStatus', header: 'Status', width: '100px', align: 'center' };
                        this.fileTemplateLineColumns.push(obj);
                         obj = { field: 'Actions', header: 'Actions', width: '100px', align: 'center' };
                         this.fileTemplateLineColumns.push(obj);
                        }
                     
                    
                    this.buildDataByTemplateLinesAndData();
                }
            });
    }
    buildDataByTemplateLinesAndData() {
        //console.log('template lines by tempid are' + JSON.stringify(this.fileTemplateLines));
        //console.log('fieldWrtColMap are' + JSON.stringify(this.fieldWrtColMap));
        let ind = 0;
        //masterOrStagingLines
        this.linesListBasedOnFile.forEach((masterLine) => {
            if (masterLine) {
                const keys = Object.keys(masterLine);
                for (let i = 0; i < keys.length; i++) {
                    const key = keys[i];
                    if (this.fieldWrtColMap.hasOwnProperty(key)) {
                        masterLine[this.fieldWrtColMap[key]] = masterLine[key];
                    }
                }

            }
        });
        //console.log('fetched lines after modifyng' + JSON.stringify(this.linesListBasedOnFile));

    }
    fetchLinesBySrcFileInb(type,spId, tempId, fileName, srcInbHistId, ind) {
        //temp store
        this.linesToUpdate=[];
     //   this.linesListBasedOnFile=[];
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
        console.log('this.batchDetailsList[ind].status'+this.batchDetailsList[ind].status);
       if(this.batchDetailsList[ind].status && this.batchDetailsList[ind].status.toString().toLowerCase().search('loaded')!=-1)
           {
               this.dataManagementWqService.fetchIdentifiersListIfMultiple(tempId,srcInbHistId,'master').subscribe(
                (res: Response) => {
                    let resp : any = res;
                    this.multipleIdentifiers = [];
                    if(resp && resp.length > 0)
                    {
                      //  console.log('intermediate identifiers of master '+JSON.stringify(resp));
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
                    //console.log('intermediate identifiers of staging '+JSON.stringify(resp));
                    this.multipleIdentifiers = resp;
                    this.fetchLinesFromDataStaging(spId,tempId,srcInbHistId , this.multipleIdentifiers[this.selectedIdentifierIndex].id,fileName,type);
                }
                else{
                    this.fetchLinesFromDataStaging(spId,tempId,srcInbHistId , null,fileName,type);
                }

            });
       
           }
    }
    fetchLinesByRowId(e)
    {
        this.selectedIdentifierIndex = e.index;
        if(this.batchDetailsList[this.bhDetIndex].status.toString().toLowerCase() == 'loaded' || this.batchDetailsList[this.bhDetIndex].status.toString().toLowerCase()  == 'transformed' ) {
            if(this.multipleIdentifiers.length >0 ){
                this.fetchLinesFromDataMaster(this.tempStore[0],this.tempStore[1],this.tempStore[3] , this.multipleIdentifiers[this.selectedIdentifierIndex].id);
            } else{
                this.fetchLinesFromDataMaster(this.tempStore[0],this.tempStore[1],this.tempStore[3] , null);
            }
           
        } else{
            if(this.multipleIdentifiers.length >0 ){
                this.fetchLinesFromDataStaging(this.tempStore[0],this.tempStore[1],this.tempStore[3] , this.multipleIdentifiers[this.selectedIdentifierIndex].id,this.tempStore[2]);
            }    else{
                this.fetchLinesFromDataStaging(this.tempStore[0],this.tempStore[1],this.tempStore[3] , null,this.tempStore[2]);
            }
           
        }
       
    }
   
    fetchLinesFromDataStaging(spId,tempId,srcInbHistId,interId,fileName,type?:any)
    {
        this.dataManagementWqService.fetchDataStagingLinesBySrcFileInb(this.linePage + 1, this.linesPageSize, spId, tempId, srcInbHistId,this.linesStatusFilter,interId).subscribe(
            (res: Response) => {
                const stagingLinesListBasedOnFile: any = res.json();
                console.log('stagingLinesListBasedOnFile'+JSON.stringify(stagingLinesListBasedOnFile));
               
                this.stagingData=true;
                this.linesListBasedOnFile = [];
                if (stagingLinesListBasedOnFile) {
                    this.linesListBasedOnFile = stagingLinesListBasedOnFile;
                    if (res.headers && res.headers.get('x-count'))  {
                        this.linesLength = res.headers.get('x-count');
                        console.log(' this.lineslength  '+     this.linesLength );
                        if((this.linesLength ==0)&& this.linesStatusFilter && this.linesStatusFilter == 'Fail' && type && type=='fileName') {
                            this.linesStatusFilter='Success';
                           this.fetchLinesFromDataStaging(spId,tempId,srcInbHistId,interId,fileName,type);
                        } else{
                            if (this.linesListBasedOnFile.length <= 0) {
                                const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                                    width: '400px',
                                    data: { records: 'records', ok: 'ok', cancel: 'cancel', action: fileName }
                                });
                                dialogRef.afterClosed().subscribe((result) => {
        
                                });
                            } else {
                                this.linesListBasedOnFile.forEach((line)=>{
                                    if(line.recordStatus != 'SUCCESS'){
                                        line['editable'] = true;
                                    } else{
                                        line['editable'] = false;
                                    }
                                       
                                });
                                this.fetchTemplateDetailsByTemplateId(tempId,interId);
                                }
                            
                        }
                        }  else  {
                        console.log(' this.lineslength  fetchng failed in staging');
                        }
                  // console.log('staging - linesListBasedOnFile' + JSON.stringify(this.linesListBasedOnFile));
                  

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
                    const masterLinesListBasedOnFile: any = res.json();
                    //this.linesListBasedOnFile = [];
                    //console.log('masterLinesListBasedOnFile' + JSON.stringify(masterLinesListBasedOnFile));
                    if (masterLinesListBasedOnFile && masterLinesListBasedOnFile.length > 0) {
                        this.linesListBasedOnFile = masterLinesListBasedOnFile;
                        if (res.headers && res.headers.get('x-count')){
                            this.linesLength = res.headers.get('x-count');
                            }  else {
                            console.log(' this.lineslength  fetchng failed');
                            }

                        this.linesListBasedOnFile.forEach((line) => {
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
        if (rowData && rowData.recordStatus && rowData.recordStatus.toString().toLowerCase() == 'success') {
            return 'success-row';
        }  else {
            return 'failed-row';
            }
    }
    saveStagingLines() {
        if (this.linesListBasedOnFile && this.linesToUpdate && this.linesToUpdate.length>0) {
            
           
            const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                width: '400px',
                data: { updateLines  : 'yes', ok: 'ok', cancel: 'cancel', message:'Are you sure to update '+this.linesToUpdate.length +'lines?' }
            });
            dialogRef.afterClosed().subscribe((result) => {
                if (result && result == 'ok')
                    {
                   // let linesToUpdate = [];
                   /* this.linesListBasedOnFile.forEach(line => {
                        if (line.recordStatus && line.recordStatus != 'SUCCESS') {
                            linesToUpdate.push(line);
                        }
                    });*/
                    this.linesToUpdate.forEach((line) => {
                        line.recordStatus ='SUCCESS';
                        const keys = Object.keys(line);
                        for (let i = 0; i < keys.length; i++) {
                            //key in line is value for a json object. get the key of that value and replace the value of key in line with the key of line
                            for (const key in this.fieldWrtColMap) {

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
                                this.commonService.success('Success', 'Lines updated successfully');
                                const resp: any = res;
                                //console.log('resp'+JSON.stringify(resp));
                                if(resp && resp.taskName && resp.taskName.search('Transformation is') != -1) {
                                       // this.loadLinesForSelectedFile=[];
                                        this.linesListBasedOnFile=[];
                                        this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
                                        // this.dataManagementWqService.deleteStagingRecords(this.linesToUpdate[0].srcFileInbId).subscribe(
                                        //     (res: any) => {
                                        //         this.linesListBasedOnFile=[];
                                        //         this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
                                        //     });
                                   
                                    }   else{
                                    if(this.tempStore) {
                                        this.fetchLinesBySrcFileInb(null,this.tempStore[0],this.tempStore[1],this.tempStore[2],this.tempStore[3],this.tempStore[4]);
                                    }
                                    }
                            });
                    }  else {
                    //this.fetchLinesBySrcFileInb(this.tempStore[0],this.tempStore[1],this.tempStore[2],this.tempStore[3],this.tempStore[4]);
                    }
            });
            
          
        } else  {
            this.commonService.info('Oops!!', 'No lines selected to update');
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
       this.selectAllBatches=false;
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
    
    deleteLines(id) {
        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { deleteLines:'delete lines', ok: 'ok', cancel: 'cancel', message:'Are you sure to delete line?' }
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
                this.dataManagementWqService.deleteLines(id).subscribe(
                        (res: Response) => {
                           const resp = res.json();
                            //console.log('response after delete is:'+JSON.stringify(resp));
                            if(resp.toLowerCase().search('success')){
                                //this.loadLinesForSelectedFile=[];
                                this.linesListBasedOnFile=[];
                                this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
                            } else{

                            }
                          
                        });
                }else {
                
                }
        });
      
    }
    deleteAllLines(srcFileInbId){
        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: { deleteLines:'delete lines', ok: 'ok', cancel: 'cancel', message:'Are you sure to clear data?' }
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
                this.dataManagementWqService.deleteAllLines(srcFileInbId).subscribe(
                        (res: Response) => {
                        });
                } else  {
                
                }
        });
       
    }
    searchData() {
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
                            if(this.batchData && this.batchData.length > 0) {
                            if(  this.batchData[0].totalCount){
                                this.batchLength =  this.batchData[0].totalCount;
                            }
                              
                               this.showViewBy =true;
                            }
                          
                        },
                        (res: Response) => {
                            this.onError(res)
                            this.commonService.error('Error', 'Error occured while searching');
                        }
                    );
        } else {
        this.fetchBatchHeadersList(null,'profile');
        }
    }
    private onSuccess(data, headers) {
        if(this.sourceProfilesService.searchData){
            this.links = this.parseLinks.parse(headers.get('link'));
        }
        
        this.batchLength = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.batchData = data.json;
        //console.log('  this.fileTemplates printed below'+JSON.stringify(  this.fileTemplates));
    }
    confirmAsSuccess(row,i)
    {
        console.log('index to success'+i)
        this.linesListBasedOnFile[i].recordStatus='SUCCESS';
        if(!this.linesToUpdate){
            this.linesToUpdate=[];
        }
        
        let count = 0;
        this.linesToUpdate.forEach((line)=>{
            if(line.id == row.id)  {
                count = count +1;
                }
                
        });
        if(count == 0)  {
           // console.log('count is == 0'+count);
            
            this.linesToUpdate.push(row);
            }
    }
    filterLinesByStatus(spId, tempId, fileName, srcInbHistId, bhDetIndex) {
        console.log('linesStatusFilter'+this.linesStatusFilter);
        this.fetchLinesBySrcFileInb(null,spId,tempId,fileName,srcInbHistId,bhDetIndex); 
    }
    onSPaginateChange(event) {
        this.page = event.pageIndex;
        this.batchPageSize = event.pageSize;
        console.log('pagination params'+ this.page +'=>'+this.batchPageSize);
       this.fetchBatchHeadersList(this.sourceProfilesForTenant[this.selectedTab].id,'profile');
      }
    onBatchDetailPaginateChange(event){
        this.batchDetailPage=event.pageIndex;
        this.batchDetailPageSize=event.pageSize;
        this.BHselectedAll=false;
        this.fetchBatchDetailsByBatchId(this.indexToFetchBatchDetail);
    }
    onLinePaginateChange(event) {
        this.linePage = event.pageIndex;
        this.linesPageSize = event.pageSize;
        console.log('pagination params'+ this.linePage +'=>'+this.linesPageSize);
        this.filterLinesByStatus(this.sourceProfilesForTenant[this.selectedTab].id,this.selectedFilesTempId,this.selectedFileName,this.selectedSrcInbHistId,this.bhDetIndex); 
      
      }
    searchProfileData() {
        // fetchSPs(this.statusList[this.selectedProfileStatus])
        console.log('this.sourceProfilesService.searchData'+this.sourceProfilesService.searchData);
        if(this.sourceProfilesService.searchData && this.sourceProfilesService.searchData != null && this.sourceProfilesService.searchData != '') {
            const filteredPrfos: any =[];
            const addedProfileIds: number[]=[];
            this.sourceProfilesForTenant.forEach((prof)=>{
                if(prof.sourceProfileName){
                    if(prof.sourceProfileName.toString().toLowerCase().search(this.sourceProfilesService.searchData.toString().toLowerCase()) != -1) {
                        filteredPrfos.push(prof);
                        addedProfileIds.push(prof.id);
                    }
                }
               
               
            });
            //{{profile.connectionType}} - {{profile.connectionName}}
            this.sourceProfilesForTenant.forEach((prof)=>{
                if(prof.connectionType){
                    if(prof.connectionType.toString().toLowerCase().search(this.sourceProfilesService.searchData.toString().toLowerCase()) != -1  && addedProfileIds.indexOf(prof.id) <= -1){
                        filteredPrfos.push(prof);
                        addedProfileIds.push(prof.id);
                    }
                }
               
            });

            this.sourceProfilesForTenant.forEach((prof)=>{
                if(prof.connectionName){
                    if(prof.connectionName.toString().toLowerCase().search(this.sourceProfilesService.searchData.toString().toLowerCase()) != -1  && addedProfileIds.indexOf(prof.id) <= -1){
                        filteredPrfos.push(prof);
                        addedProfileIds.push(prof.id);
                    }
                }
               
            });
            this.sourceProfilesForTenant=filteredPrfos;
        }  else   {
                  this.fetchSPs(this.statusList[this.selectedProfileStatus['value']]);
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
            //                     let status ;
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
            //                     this.commonService.info('No data found on search','');
            //                     }
            //                 });
           
    }
    refreshBatchData(){
         this.fetchSPs(this.statusList[this.selectedProfileStatus['value']]);
    }
    showOrHideAdditionalFilters()  {
        if(this.showAdditionalFilters == true){
            this.showAdditionalFilters=false;
        }  else if(this.showAdditionalFilters == false){
            this.showAdditionalFilters=true;
        }
        
    }
    statusFilterOptions(){
        if(this.statusFilter == true){
            this.statusFilter=false;
        } else  if(this.statusFilter == false){
            this.statusFilter=true;
        }
        
    }
    getbatch()    {

    }

    toggleHeader() {
        this.toggleTempAcc=true;
    }

    batchStatusColor(name) {
        if (name.search('Transformation Failed') != -1) {
            return 'badge badge-danger';
        } else {
            return 'badge badge-success';
        }
    }

    /**Converting to proper time */
    properTime(time) {
        const t = time.split(':');
        const d = new Date();
        d.setHours(t[0]);
        d.setMinutes(t[1]);
        d.setSeconds(t[2]);
        return d;
    }
    checkTransformImmediately(event){
        if(event.checked == true)   {
           this. schedulingObj.parameters[this.schedulingObj.parameters.length -1].selectedValue=true;
           this. schedulingObj.parameters[this.schedulingObj.parameters.length -1].selectedValue=true;
           //console.log(' this. schedulingObj.parameters[this.schedulingObj.parameters.length -1].selectedValue'+ this. schedulingObj.parameters[this.schedulingObj.parameters.length -1].selectedValue);
        } else   {
            this. schedulingObj.parameters[this.schedulingObj.parameters.length -1].selectedValue=false;
        }
    }

    forceTransform(srcFileInbId)   {
        this.dataManagementWqService.forceTransform(srcFileInbId).subscribe(
            (res: any) => {
                const resp = res;
                if(resp._body.toString().toLowerCase().search('success') != -1) {
                    this.commonService.success('SUCCESS', 'Force transformation successful!');     
                    this.fetchBatchDetailsByBatchId( this.indexToFetchBatchDetail);
                } else{
                    this.commonService.error('Oops', 'Force transformation failed');     
                }
            });
    }
    openSources(){
     
        this.openTemplateDialog=false;
        this.showTempoo=false;
        this.showSources=!this.showSources;
        if(this.showSources){
            this.activeIndex=0;
           
        }
        //fetch connections
        this.sourceConnectionDetailsService.connectionsForTenant().subscribe(
            (res: any) => {
            this.connectionDetailsList =res;
            console.log('$(window).height()'+$(window).height());
            if($(window).height()>500){
                if($(window).height() > 630){
                    $(".source-list-styles").css("height", '625px');
                    $(".source-list-styles").css("max-height", '625px');
                    $(".source-style").css("height",'505px');
                    $(".source-style").css("max-height",'505px');
                }else{
                    const heightCal =  $(window).height() -140;
                    $(".source-list-styles").css("height", heightCal+'px');
                    $(".source-list-styles").css("max-height", heightCal+'px');
                    $(".source-style").css("height",(heightCal-120)+'px');
                    $(".source-style").css("max-height",(heightCal-120)+'px');
                }
                
              }else{
              
                $(".source-list-styles").css("height", '500px');
                $(".source-list-styles").css("max-height", '500px');
                $(".source-style").css("height",'380px');
                $(".source-style").css("max-height",'380px');
              }
            });
         
       
    }
 
    openTempDialog(){
       // this.templateName = temp;
        $(".source-templates").children().removeClass("hilight-source-template");
        //    $(this).parents("div:first").addClass('etl-source-img');
            $(".source-templates div span" ).click(function() {
                $(this).parent("div").addClass('hilight-source-template');
                
              });
             // let x = document.getElementsByClassName("ui-menuitem-link");
             // $("a").remove(".ui-menuitem-link");
             // $( ".ui-steps ul li:first" ).append( "<i class=\"rec-font-profile\" style=\"font-size: 26px;border-radius: 50%;border: 6px solid rgb(205, 209, 215); padding: 7px; background-color: #1d7486;  color: white;\"></i>" );
  //  $( ".ui-steps ul li:nth-child(2)" ).append( "<p>hey</p>" );
             // $("a").find(".ui-menuitem-link").replace( "<i class=\"rec-font-profile\" style=\"font-size: 26px;border-radius: 50%;border: 6px solid rgb(205, 209, 215); padding: 7px; background-color: #1d7486;  color: white;\"></i>" );
             // if(x.length > 0){
              // Removing a class with VanillaJS
          
             // x[0].classList.remove("ui-menuitem-link");
             // x[1].classList.remove("ui-menuitem-link");
             // x[2].classList.remove("ui-menuitem-link");
             // }
    }
    assignTemplate(temp){
        if(temp && temp.templateName){
        this.templateName =  temp.templateName;
        if( !this.matchForTemplates&& temp.idForDisplay){
        this.sourceProfileAndTemplate.templateId =temp.idForDisplay;
        }
        if( this.matchForTemplates&& temp.id){
            this.sourceProfileAndTemplate.templateId =temp.id;
            }
        }
        $(".etl-templates").children().removeClass("select-template");
        //    $(this).parents("div:first").addClass('etl-source-img');
            $(".etl-templates div span" ).click(function() {
                $(this).parent("div").addClass('select-template');
                
              });
              this.activeIndex=1;
            
    }
    myTypeScriptFunction(source) {
        //get templates by source
       this.resetDialogValues();
        this.fileTemplatesService.templatesBySource(source).subscribe(
            (res: any) => {
               this.sampleTemplatesList=[];
               this.sampleTemplatesList=res;
            });
        this.activeIndex=0;
       // this.openTemplateDialog = true;
       this.showTempoo=false;
       this.sourceType=source;
        setTimeout(() => { this.showTempoo = true; }, 0.5);
        $(".source-list-styles div").children().removeClass("etl-source-img");
    //    $(this).parents("div:first").addClass('etl-source-img');
        $(".source-list-styles div a").click(function() {
            $(this).parent("div").addClass('etl-source-img');
            
          });
    this.items=[{
        label: 'Templates for '+this.sourceType,
        command: (event: any) => {
            this.activeIndex = 0;
            //this.messageService.add({severity:'info', summary:'First Step', detail: event.item.label});
        }
    },
    {
        label: 'Create Pipeline',
        command: (event: any) => {
            this.activeIndex = 1;
          //  this.messageService.add({severity:'info', summary:'Seat Selection', detail: event.item.label});
        }
    },
    {
        label: 'Review Details',
        command: (event: any) => {
            this.activeIndex = 2;
           // this.messageService.add({severity:'info', summary:'Pay with CC', detail: event.item.label});
        }
    }
];
    this.openTemplateDialog=true;
    this.openTempDialog();
    //var stepsValue = document.getElementById("steps");
    //$(stepsValue).find('a').remove("ui-menuitem-link");
   
   
    // $('.ui-steps ul').each(function(i, items_list){
     

    //     $(items_list).find('li').each(function(j, li){
    //         //alert(li.text());
    //       

    //     })

      

    // });
    //$( ".ui-steps ul li:first" ).append( "<i class=\"rec-font-profile\" style=\"font-size: 26px;border-radius: 50%;border: 6px solid rgb(205, 209, 215); padding: 7px; background-color: #1d7486;  color: white;\"></i>" );
  //  $( ".ui-steps ul li:nth-child(2)" ).append( "<p>hey</p>" );
    
    }
    checkSourceProfileName(event){

    }
    saveProfile(){
        console.log('profile has'+JSON.stringify( this.sourceProfileAndTemplate));
        this.sourceProfilesService.postProfileAndTagTemplate(this.sourceProfileAndTemplate).subscribe(
            (res: any) => {
                let profResp: any;
                profResp = res;
                if(profResp && profResp.taskStatus && profResp.taskStatus === 'SUCCESS'){
                    if(profResp.taskName){
                    this.commonService.success('',profResp.taskName);
                     // this.router.navigate(['/ETL-WQ', {outlets: {'content': ['ETL-WQ-home']}}]);
                     //this.router.navigate(['/ETL-WQ', {outlets: {'content': ['ETL-WQ-home']}}]).then(()=>  {});
                     this.resetDialogValues();
                     this.showSources=false;
                     this.ngOnInit();
                    }
                   
                }else{
                    if(profResp.details){
                    this.commonService.error('',profResp.details);
                    }else{
                        this.commonService.error('','Profile save failed');
                    }
                }
              
            });
    }
    resetDialogValues(){
        this.openTemplateDialog =false;
        this.activeIndex = 0;
        this.sourceProfileAndTemplate =new SourceProfileAndTemplate();
        this.selectedTemplateIndex = null;
        $(".etl-templates").children().removeClass("select-template");
    }
    fileChange(file: any) {
        this.onFileDrop(file.files);
    }
   
}

