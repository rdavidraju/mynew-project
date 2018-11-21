import 'jquery';
import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { FileTemplates, RowIdentifiersList } from './file-templates.model';
import { LineInfo } from './LineInfo.model';
import { FileTemplatesService } from './file-templates.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Subscription';
import { Router, NavigationEnd } from '@angular/router';
//import { FileTemplateLinesService } from '../file-template-lines/file-template-lines.service';
import { FileTemplateLines } from '../file-template-lines/file-template-lines.model';
import { SourceProfileFileAssignments } from '../source-profile-file-assignments/source-profile-file-assignments.model';
import { FileTemplatesPostingData } from './fileTemplatesPosting.model';
import { SourceProfilesService } from '../source-profiles/source-profiles.service';
import { SourceProfiles } from '../source-profiles/source-profiles.model';
import { JhiDateUtils } from 'ng-jhipster';
import { SourceProfileFileAssignmentsService } from '../source-profile-file-assignments/source-profile-file-assignments.service';
import { FileSelectDirective, FileDropDirective, FileUploader, FileItem ,FileUploaderOptions} from 'ng2-file-upload/ng2-file-upload';
import { MdProgressSpinnerModule } from '@angular/material';
//import { NotificationsService } from 'angular2-notifications-lite';
import { DialogModule, CheckboxModule, SelectItem } from 'primeng/primeng';
import { } from 'primeng/primeng';
import { currentRoute } from '../../breadcrumb.component';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage'; 
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { IntermediateTableService } from '../intermediate-table/intermediate-table.service';
import { FileTemplateLinesService } from '../file-template-lines/file-template-lines.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { DataManagementWqService } from '../data-management-wq/data-management-wq.service';
import { ExcelConditions, Condition } from '.';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
declare var $: any;
declare var jQuery: any;
const URL = '';

@Component({
    selector: 'ftl-confirm-action-modal',
    templateUrl: 'file-templates-confirm-action-modal.html'
})
export class ConfirmActionModalDialog {
    functionsList: any = [];
    constructor(
        
        public dialogRef: MdDialogRef<ConfirmActionModalDialog>,
        public dialog: MdDialog,
        private spaservice: SourceProfileFileAssignmentsService,
        private lookUpCodeService: LookUpCodeService,
        private fileTemplatesService: FileTemplatesService,
        @Inject(MD_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.fileTemplatesService.formula = null;
        this.fileTemplatesService.example = null;
        this.fileTemplatesService.functionName = null;

        this.lookUpCodeService.fetchLookUpsByLookUpType('TEMPLATE_FUNCTIONS').subscribe((res: any) => {
            this.functionsList = res;
        });

    }
    
    onNoClick(): void {
        this.dialogRef.close();
    }
}

@Component({
    selector: 'jhi-file-templates-edit',
    templateUrl: './file-templates-new-edit.component.html'
})
export class FileTemplatesEditComponent implements OnInit, OnDestroy {
fileTypeSearch:any;
sourceSearch:any;
    excelFileColumnList:any=[];
    operatorList: any =[];
    tooltipValue = 'Fill mandatory fields';
    duplicateFileTemplateColumn=false;
    templateErroMsg: string;
    showPositionCols = false;
    excelColumnPlaceHolder='Enter Column';
    pushFlag: boolean ;
    emptySpace =' ';
    sourceList = [];
    //sourceList = ['Amex','Chase','BAI2','Bank of America','Braintree','Citi','Jpmorgan','Paypal','Worldpay','Adyan','American Express','BAI2_ACH',
    //'BAI2_Lockbox','MT940','Zaakpay','Worldpay_Chbk','Paytm','Others'];
    columnListForm: FormGroup ;
    selectedConditionTypeIndex:number;
    logicalOperators: any;
    excelColumn:any;
    finalInfoColsOriginalLength: number;
    submitted:boolean;
    today=new Date();     
    @Input('createFromRemoteArea') createFromRemoteArea: String = '';
    hideLocalOptions: boolean ;
    detailId: any;
    indexToView: any;
    viewSampleData: any;
    mouseOverRowIdentifier: any;
    selectedAll: any;
    RIs = [];
    //rowIdentifier : any='';
    length: number;
    duplicateFileTempName: boolean;
    fileTemplate = new FileTemplates();
    fileTemplateLines: FileTemplateLines[] = [];
    fileTemplatesList: FileTemplates[];
    delimiterList: any = [];
    routeSub: any;
    isCreateNew: boolean;
    isEdit: boolean ;
    isViewOnly: boolean ;
    isEditLine: boolean ;
    copyTemplate: boolean ;
    dropfile: boolean ;
    fileTemplatesPostingData: FileTemplatesPostingData = {};
    fileTempId: any;
    templateUnfrozenWidth: any;  
    expandProfiles: boolean ;
    fileTypes = ['Delimiter', 'DFR', 'Position', 'BAI2','BAI2_ACH','BAI2_LockBox', 'Excel', 'MT940_Standard'];
    screenres: any;
    delimiterCols = [
        { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center',hide:false },
        { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
        //{ field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true, display:'none'},
        { field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:false},
       
        // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
        // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
         { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center' ,hide:false},
      //  { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center' ,hide:false},
        
        { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center',hide:false},
        /*{ field: 'columnDelimeter', header: 'Column Delimiter', width: '100px', align: 'left' },*///commented as not required to display
        /*  { field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
    ];
    bai2Cols = [
      //  { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true, display:'none'},
       // { field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:true},
       { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center' ,hide:false},
        { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center',hide:false },
        // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
        // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
      //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center' ,hide:true},
       
        { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
        { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center',hide:false},
       
        /*{ field: 'columnDelimeter', header: 'Column Delimiter', width: '100px', align: 'left' },*///commented as not required to display
        /*  { field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
    ];
    bai2ACHCols = [
      //  { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true, display:'none'},
      //  { field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:true},
      { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center' ,hide:false},
        { field: 'dateFormat', header: 'Date Format', width: '150px', align: 'center',hide:false },
        { field: 'positionBegin', header: 'Position Begin', width: '150px', align: 'center',hide:false },
        { field: 'positionEnd', header: 'Position End', width: '100px', align: 'center',hide:false },
        { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
        { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center',hide:false},
        // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
        // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
      //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center' ,hide:true},
       
       
       
        /*{ field: 'columnDelimeter', header: 'Column Delimiter', width: '100px', align: 'left' },*///commented as not required to display
        /*  { field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
    ];
    bai2LockBoxCols = [
        //  { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true, display:'none'},
        //  { field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:true},
        { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center' ,hide:false},
          { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center',hide:false },
          { field: 'positionBegin', header: 'Position Begin', width: '150px', align: 'center',hide:false },
          { field: 'positionEnd', header: 'Position End', width: '100px', align: 'center',hide:false },
          { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
          { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center',hide:false},
         
          // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
          // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
        //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center' ,hide:true},
        
          /*{ field: 'columnDelimeter', header: 'Column Delimiter', width: '100px', align: 'left' },*///commented as not required to display
          /*  { field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
      ];
    positionCols = [
      //  { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true },
        //{ field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:true },
        { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center' ,hide:false},
        // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
        // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
      //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center',hide:true},
        //{ field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center',hide:true},
        { field: 'positionBegin', header: 'Position Begin', width: '150px', align: 'center',hide:false },
        { field: 'positionEnd', header: 'Position End', width: '100px', align: 'center',hide:false },
        { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
        { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center' ,hide:false},
        /*{ field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/

    ];
    mt940Cols = [
        //  { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true },
          //{ field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:true },
        
          // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
          // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
        //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center',hide:true},
          { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center',hide:false},
          { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center' ,hide:false},
          { field: 'positionBegin', header: 'Position Begin', width: '150px', align: 'center',hide:false },
          { field: 'positionEnd', header: 'Position End', width: '100px', align: 'center',hide:false },
          { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
          { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center' ,hide:false},
          /*{ field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
  
      ];
     excelCols = [
        //  { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:true },
        { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center' ,hide:false},  
        
          
          // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
          // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
        //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center',hide:true},
       //   { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center',hide:true},
         // { field: 'positionBegin', header: 'Position Begin', width: '150px', align: 'center',hide:false },
          //{ field: 'positionEnd', header: 'Position End', width: '100px', align: 'center',hide:false },
          { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
          { field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:false },
          { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center' ,hide:false},
          /*{ field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
  
      ];
      dfrCols = [
        { field: 'recordStartRow', header: 'Record Start Row', width: '350px', align: 'center',hide:false},
        //   { field: 'recordTYpe', header: 'Type', width: '150px', align: 'center',hide:false },
          //{ field: 'enclosedChar', header: 'Enclosed Char', width: '100px', align: 'center',hide:true },
          { field: 'dateFormat', header: 'Date Format', width: '130px', align: 'center' ,hide:false},
          // { field: 'timeFormat', header: 'Time Format', width: '100px', align: 'center' ,hide:false},
          // { field: 'amountFormat', header: 'Amount Format', width: '100px', align: 'center' ,hide:true},
        //  { field: 'skipColumn', header: 'Skip Column', width: '100px', align: 'center',hide:true},
         
         // { field: 'positionBegin', header: 'Position Begin', width: '150px', align: 'center',hide:false },
         // { field: 'positionEnd', header: 'Position End', width: '100px', align: 'center',hide:false },
          { field: 'constantValue', header: 'Constant', width: '150px', align: 'center',hide:false},
          { field: 'zeroFill', header: 'Zero Fill', width: '100px', align: 'center' ,hide:false},
          /*{ field: 'formula', header: 'Translational Formula', width: '100px', align: 'left' }*/
  
      ];
    columnOptions: SelectItem[];
    mySelectedRows = [];

    finalLineInfoCols = [];
    lineInfoCols = [];
    fileName: any = '';
    mouseOverRowNo: number = -1;
    mouseOnRow: number = -1;
    /**
     * File template lines start
     */

    fileTemplateLinesInfo: FileTemplateLines[] = [];
    iter: any = 0;
    templateLines: FileTemplateLines[] = [];
    enableDeleteButton: boolean ;
    editLine: boolean ;
    tempObj: FileTemplateLines = {};
    currentEditingLine: FileTemplateLines = {};
    fullScreenView: boolean ;
    /**
     * File template lines end
     */

    /**
     * Source profile assignment start
     */
    profilesList: any = [];
    frequencyList: any[];
    sourceProfilesList: any = [];
    sourceProfilesAdded: SourceProfileFileAssignments = {};
    selectedProfileName: any = "";
    selectedProfileId: any;
    displaySelectedProfile: boolean ;
    sprofileId: any;
    selectedFrequencyType: any = "";
    sourceProfileAssignments: any;
    profileAssignment: boolean ;
    /**
     * source profile assignment end
     */
    /**
    * drop zone start
    */
    refreshFile: boolean ;
    filesAdded: FileList;
    private uploadvar: boolean ;
    uploadedIcon: boolean ;
    uploadStatus: boolean ;
    public uploader: FileUploader = new FileUploader({ url: URL });
    public uploaderOptions: FileUploaderOptions;
    public hasBaseDropZoneOver: boolean ;
    public hasAnotherDropZoneOver: boolean ;
    uploadingProcess: any = false;
    files: FileList;
    private file: File;
    filesFromButton: Array<File>;
    viewAction: boolean ;
    active: any;
    desc: any;
    extractedFileData: any;
    colHeaders: any;
    data: any = [];
    extractedTemplateLine: any = [];
    sourceData: boolean ;
    ftable: any;
    presentPath: any;
    screenheight: any;
    screenwidth: any;

    /**
      * drop zone end
      */
    lastLineNumber: any = 0;
    enableLineInfo: boolean ;
    saveSuccess: boolean ;

    displaySampleData: boolean ;

    functionsList: any = [];
    mouseOverLine: boolean;
    showFunction = [];
    saveFunction = [];
    isVisibleA: boolean ;
    hideSkipRowData: boolean ;
    rowIdentifierHeaders = ['Criteria', 'Identifier', 'Position Start', 'Position End'];
    criteriaList = [];
    /*[
                {
                    "id":1,
                    "lookUpCode":"CONTAINS",
                    "meaning" : "Contains",
                },
                {
                    "id":2,
                    "lookUpCode":"POSITION",
                    "meaning" : "Position",
                },
                {
                    "id":3,
                    "lookUpCode":"RECORD_START_ROW",
                    "meaning" : "Record Start Row",
                }
                ];*/
    rowIdentifiersList: RowIdentifiersList[];
    rowIdentifierClass: any;
    selectedTab: any;
    sampleDataAfterDropForMI = [];
    checkAll: any;
    checkRows: any = [];
    RIsToFetch = [];
    sampleDataForMIInVIewMode = [];
    sampleDataIndexToView: any;
    columnHeaders = [];
    selectedRI: RowIdentifiersList[];
    showFuncMI = false;
    showFuncIndex: number = -1;
    saveFunctionArrayMI = [];
    selectedTabIndex = 0;
    duplicateIdentifier: any;

    startDateChange = false;
    endDateChange = false;
    dateFormatList:any[]=[];
    excelInput;
    constructor(
        private builder: FormBuilder,
        private config: NgbDatepickerConfig,
        private fileTemplatesService: FileTemplatesService,
        private router: Router,
        private fileTemplatesLinesService: FileTemplateLinesService,
        private sourceProfilesService: SourceProfilesService,
        private route: ActivatedRoute,
        private dateUtils: JhiDateUtils,
        private SPAService: SourceProfileFileAssignmentsService,
        //private notificationsService: NotificationsService,
        private commonService: CommonService,
        private sessionStorage: SessionStorageService,
        private lookUpCodeService: LookUpCodeService,
        private intermediateTableService: IntermediateTableService,
        private dataManagementWqService: DataManagementWqService,
        public dialog: MdDialog
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.columnOptions = [];
    }

    changeMinDate() {
        this.config.minDate = this.fileTemplate.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    //start - below lines are in place of 
    // this.screenheight = (window.screen.height) +"px"
    //  this.screenwidth = (window.screen.width) +"px"
    //  //console.log("screen height : "+ this.screenheight + " screen width : " + this.screenwidth );;
    //end 
   
    ngOnInit() {
        this.SPAService.ftToProf='';

        this.lookUpCodeService.fetchLookUpsByLookUpType('SOURCE_TYPES').subscribe(
            (res: any) => {
                this.sourceList = res;
            });
        this.lookUpCodeService.getAllLookups('DATE_FORMAT').subscribe(
            (res: any) => {
                this.dateFormatList = res;
            });
            $("templateLines input").on("focus",function(){
            $(this).parent().addClass("ui-state-active");
            });
        if (this.createFromRemoteArea) {
            this.hideLocalOptions = true;
            this.isVisibleA = true;
            if (this.createFromRemoteArea == 'create') {
                this.isCreateNew = true;
            } else if (this.createFromRemoteArea.toString().startsWith('detail')) {
                let detailData: any = [];
                detailData = this.createFromRemoteArea.toString().split('-');
                if (detailData[1]){
                    this.detailId = detailData[1];
                }
            }
        }

        this.commonService.callFunction();
        // this.commonService.callFunction();
        this.commonService.screensize();
        $(".left-component").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) + 'px'
        });
        $(".ft-sampleData").css({
            'max-width': (this.commonService.screensize().width - 112) + 'px'
        });
        this.templateUnfrozenWidth = (this.commonService.screensize().width / 2) + 'px';
        //console.log(this.templateUnfrozenWidth);

        //fetch delimiters
        this.fileTemplatesService.fetchDelimiters().subscribe(
            (res: any) => {
            });
        $('html, body').animate({ scrollTop: '0px' }, 0);
        //this.fixedTable();
        ////console.log(this.commonService.screensize().height);


        // this.getSourceProfilesList();
        this.routeSub = this.route.params.subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;

            if (this.presentPath == "filetemplate-new") {
                $('.component-title').removeClass('margin-left-22');

            } else {
                $('.component-title').addClass('margin-left-22');
            }

            //fetch delimiter lookups
            this.lookUpCodeService.fetchLookUpsByLookUpType('DELIMITER').subscribe((res: any) => {
                this.delimiterList = res;
            });
            this.lookUpCodeService.fetchLookUpsByLookUpType('TEMPLATE_FUNCTIONS').subscribe((res: any) => {
                this.functionsList = res;
            });
            // params['id']= this.detailId;
            if (params['id']){
                this.detailId = params['id'];
            }
            this.fileTemplate.id = params['id'];
            if (this.detailId) {
                this.enableLineInfo =true;
                this.fileTemplatesService.fetchById(this.detailId).subscribe(
                    (res: FileTemplates) => {
                        this.fileTemplate = res;
                        this.today =this.fileTemplate.startDate;
                       // //console.log('fetched obj is' + JSON.stringify(this.fileTemplate));
                        if(this.fileTemplate && this.fileTemplate.fileType && this.fileTemplate.fileType.toLowerCase() == 'excel') {
                            if(this.fileTemplate.startRowColumns)   {
                                if(this.fileTemplate.excelConditions) {
                                    this.fileTemplate.excelConditions.columnsList=[];
                                    const colsList: any =  jQuery.extend( true, [], this.fileTemplate.startRowColumns);
                                  
                                    for(let c=0;c< colsList.length;c++)  {
                                        const colObj = {"col":colsList[c]};
                                        this.fileTemplate.excelConditions.columnsList[c] = (colObj);
                                    }
                               //     //console.log('this.fileTemplate.excelConditions.columnsList'+JSON.stringify(this.fileTemplate.excelConditions));
                                }
                               
                            }
                        }
                        const exCondition = new ExcelConditions();
                        if( this.fileTemplate.excelConditions &&  this.fileTemplate.excelConditions.columnHeadings){
                        this.fileTemplate.excelConditions.columnHeadings = exCondition.columnHeadings;
                        }
                        if (this.fileTemplate.sampleData) {
                            this.sourceData = false;
                            this.data = this.fileTemplate.sampleData;
                            if (this.data[0] && this.data[0]['colHeaders']){
                                this.colHeaders = this.data[0]['colHeaders'];
                            }
                        }
                        // if (this.fileTemplate.startDate) {
                        //     this.fileTemplate.startDate =  this.fileTemplate.startDate.getDate() + 1;
                        // }
                        // if (this.fileTemplate.endDate) {
                        //     this.fileTemplate.endDate = this.fileTemplate.endDate.getDate() + 1;
                        // }
                        if (this.fileTemplate.status == 'Active') {
                            this.fileTemplate.enabledFlag = true;
                        } else {
                            this.fileTemplate.enabledFlag = false;
                        }
                        const interIDs = [];
                        this.selectedFileType();
                        if (this.fileTemplate.multipleRowIdentifiers) {
                            //fetch intermediate records
                            this.intermediateTableService.getIntermediateRecords(this.detailId).subscribe((resp: any) => {
                                this.rowIdentifiersList = resp;
                                this.rowIdentifiersList.forEach((rowIdentifer )=> {
                                    if (rowIdentifer['rowIdentifierCriteria'] == 'Position') {
                                        this.showPositionCols = true;
                                    }
                                });
                                ////console.log(' this.rowIdentifiersList ' + JSON.stringify(this.rowIdentifiersList));
                                this.sampleDataForMIInVIewMode = [];

                                this.rowIdentifiersList.forEach((RI) => {
                                    const sampleDataList = [];
                                    if(RI['sampleData'] != null && RI['sampleData'] != ' ' && typeof RI['sampleData'] != 'string'){
                                        RI['sampleData'].forEach((sampleData) => {
                                            const sampleDatawithRI = {};
                                            Object.keys(sampleData).forEach((key) => {
                                                const value = sampleData[key];
                                                sampleDatawithRI['rowIdentifier'] = key;
                                                sampleDatawithRI['sampleData'] = value;
                                            });
                                            sampleDataList.push(sampleDatawithRI);
                                        });
                                        this.sampleDataForMIInVIewMode.push(sampleDataList);
                                    }
                                   
                                    // //console.log('==--------='+JSON.stringify(RI.data));
                                    interIDs.push(RI.id);
                                });
                                //call get lines by template id and inter ids
                                this.fileTemplatesLinesService.fetchFTLByTempIdAndInterIDs(this.detailId, interIDs).subscribe((response: any) => {
                                    let resp1: any = [];
                                    resp1 = response;
                                    let temp: number = -1;
                                    this.sampleDataAfterDropForMI = [];

                                    resp1.forEach((RILines) => {

                                        Object.keys(RILines).forEach((key) => {

                                            const value = RILines[key];
                                            const FTLWithRI = {};
                                            FTLWithRI['rowIdentifier'] = key;
                                            FTLWithRI['templateLines'] = value;
                                            if (temp == -1) {
                                                if (!this.columnHeaders){
                                                    this.columnHeaders = [];
                                                }
                                                FTLWithRI['templateLines'].forEach((line) => {
                                                    this.columnHeaders.push(line.columnHeader);
                                                });

                                            }

                                            this.sampleDataAfterDropForMI.push(FTLWithRI);
                                            temp = 0;
                                        });
                                    });
                                });
                            });
                        }
                        this.fetchSourceProfileAssignments(this.fileTemplate.id);

                        if (!this.fileTemplate.multipleRowIdentifiers) {
                            ////console.log('this.fileTemplate.id' + this.fileTemplate.id);
                            this.fileTemplatesService.fetchTemplateLinesByTemplateId(this.fileTemplate.id).subscribe((resp: any) => {
                                this.fileTemplateLines = resp;
                                ////console.log('length while fetching is:' + this.fileTemplateLines.length);
                                if (this.fileTemplateLines.length > 0) {
                                    this.dropfile = false;
                                } else {
                                    this.dropfile = true;
                                }

                            });
                        }
                        this.isCreateNew = false;

                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            if (url.search('copy') != -1) {
                                //this.fileTemplate.id =null;
                                this.copyTemplate = true;
                                this.isEditLine = true;
                                this.lookUpCodeService.fetchLookUpsByLookUpType('ROW_IDENTIFIER').subscribe((resp: any) => {
                                    this.criteriaList = resp;
                                    this.dropfile = true;
                                   // //console.log('this.criteriaList size is' + this.criteriaList.length);
                                });
                                this.fileTemplate.templateName = '';
                                this.checkFileTemplateName(this.fileTemplate.templateName);
                                ////console.log('this.fileTemplate.templateName is ' + this.fileTemplate.templateName);
                            }
                            this.dropfile = true;
                            if (this.sessionStorage.retrieve('sourceProfileName')) {
                                this.expandProfiles = true;
                                this.fileTemplate = this.sessionStorage.retrieve('fileTemplatesInfo');
                                this.fileTemplateLines = this.sessionStorage.retrieve('fileTemplateLinesInfo');
                                this.selectedProfileName = this.sessionStorage.retrieve('sourceProfileName');
                                // //console.log(' this.selectedProfileName' + this.selectedProfileName);
                                this.sessionStorage.clear('sourceProfileName');
                                this.sessionStorage.clear('fileTemplatesInfo');
                                this.sessionStorage.clear('fileTemplateLinesInfo');
                                this.selectedProfile();
                            }
                            this.fileTemplateLines.forEach((line) => {
                                if (line.lastLine){
                                    this.lastLineNumber = line.lineNumber;
                                }
                            });
                            // this.addLinee();
                            this.isEditLine = true;
                            //this.lastLineNumber
                        } else {
                            this.isViewOnly = true;
                           
                            $('.sdtitle').css('display', 'block');
                            if ((this.commonService.screensize().width <= 1024)){
                                this.isVisibleA = true;
                            }


                        }


                    }
                );

            } else {
                const sourceProfileObj = new SourceProfiles();
                sourceProfileObj.startDate = this.fileTemplate.startDate;
                sourceProfileObj.endDate = this.fileTemplate.endDate;
                this.sourceProfilesList = this.sourceProfilesService.getAllSourceProfiles(sourceProfileObj).subscribe((res: any) => {
                    this.sourceProfilesList = res;
                    this.sourceProfilesList.forEach((profile) => {
                        const prof = {
                            "id": profile.id,
                            "name": profile.sourceProfileName
                        };
                        this.profilesList.push(prof);
                    });
                    this.selectedProfile();

                    this.isCreateNew = true;
                  
                    
                    this.isVisibleA = true;
                    this.isEditLine = true;
                    this.enableLineInfo=true;
                    //add class to datatable
                    $('#template_lines .ui-datatable tr td').addClass('.ui-cell-editing');
                    // this.addLinee();
                    /**Default File Type :- Delimiter */
                    this.fileTemplate.fileType = 'Delimiter';
                    this.selectedFileType();
                    if (this.sessionStorage.retrieve('sourceProfileName')) {
                        const profId = this.sessionStorage.retrieve('profileId');
                        this.expandProfiles = true;
                        this.fileTemplate = this.sessionStorage.retrieve('fileTemplatesInfo');
                        this.fileTemplateLines = this.sessionStorage.retrieve('fileTemplateLinesInfo');
                        this.selectedProfileName = this.sessionStorage.retrieve('sourceProfileName');
                        this.sessionStorage.clear('sourceProfileName');
                        this.sessionStorage.clear('fileTemplatesInfo');
                        this.sessionStorage.clear('fileTemplateLinesInfo');
                    }
                });
            }
        });

        // this.frequencyList = [
        //     {
        //         "id": 1,
        //         "name": "Daily"
        //     },
        //     {
        //         "id": 2,
        //         "name": "Weekly"
        //     },
        //     {
        //         "id": 3,
        //         "name": "Fortnight"
        //     },
        //     {
        //         "id": 4,
        //         "name": "Monthly"
        //     }
        // ];
        ////console.log('window.innerHeight '+window.innerHeight );
        //$(".file-templates-form:last-child .setups-accordian .mat-expansion-panel .mat-expansion-panel-content").css('min-height', (window.innerHeight - 850) + 'px');
    };
    getSourceProfilesList() {
        if (this.isCreateNew) {
            const sourceProfileObj = new SourceProfiles();
            sourceProfileObj.startDate = this.fileTemplate.startDate;
            sourceProfileObj.endDate = this.fileTemplate.endDate;
            this.sourceProfilesList = this.sourceProfilesService.getAllSourceProfiles(sourceProfileObj).subscribe((res: any) => {
                this.sourceProfilesList = res;
                this.sourceProfilesList.forEach((profile) => {
                    const prof = {
                        "id": profile.id,
                        "name": profile.sourceProfileName
                    };
                    this.profilesList.push(prof);
                });
                this.selectedProfile();
            });
        }
    }
    //If Start Date Entered Apply Class
    startEndDateClass() {
        if (this.fileTemplate.startDate != null) {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }

    ngOnDestroy() {
      //  this.notificationsService.remove();
        this.routeSub.unsubscribe();
    }
    
    selectedFileType() {
        this.columnOptions=[];
        this.finalLineInfoCols = [];
        this.sampleDataAfterDropForMI = [];
        this.fileTemplateLines = [];
        switch (this.fileTemplate.fileType) {
            case 'DFR':
            this.fileTemplate.delimiter ='124';
            this.finalLineInfoCols = this.dfrCols;
            this.addLinee();
            break;
            case 'Excel':
            this.finalLineInfoCols = this.delimiterCols;
            this.fileTemplate.delimiter ='44';
            this.rowIdentifiersList=[];
            this.finalLineInfoCols = this.excelCols;
            this.fileTemplate.multipleRowIdentifiers=false;
            if(this.isCreateNew || this.copyTemplate) {
                this.fileTemplate.excelConditions = new ExcelConditions();
                this.dropfile =false;
                let ruleListForm: FormGroup;
            ruleListForm = this.builder.group({
                data: "",
            });
            this.columnListForm = ruleListForm;
            this.fileTemplateLines=[];
                        
           // this.uploaderOptions.queueLimit = 0;
            
           this.lookUpCodeService.fetchLookUpsByLookUpType('LOGICAL_OPERATOR').subscribe((res: any) => {
            const logicalOperatorLOV = res;
            this.logicalOperators = logicalOperatorLOV;
            //console.log('this.logicalOperators length is'+this.logicalOperators.length);
        });
            }
            break;
            case 'Delimiter':
                this.finalLineInfoCols = this.delimiterCols;
               this.dropfile=true;
                this.addLinee();
                break;
            case "Position":
                this.finalLineInfoCols = this.positionCols;
              
                this.addLinee();
                break;
            case 'BAI2':
                this.fileTemplate.delimiter = '44';
                const fileType = 'BAI2';
                this.finalLineInfoCols = this.bai2Cols;
                this.fileTemplate.multipleRowIdentifiers = false;

                this.fileTemplatesService.fileTemplLinesBAi2(this.fileTemplate.delimiter, fileType).subscribe((res) => {
                    if (res && res[0]){
                        this.fileTemplateLines = res[0].templateLines;
                    }else{
                        this.commonService.info('something went wrong','');
                    }
                });
                this.enableLineInfo = true;
                break;
            case 'BAI2_ACH':
                const fileType1 = 'BAI2_ACH';
                this.finalLineInfoCols = this.bai2ACHCols;
                this.fileTemplate.multipleRowIdentifiers = false;
                this.fileTemplatesService.fileTemplLinesBAi2(this.fileTemplate.delimiter, fileType1).subscribe((res) => {
                        if(res && res[0]){
                        this.fileTemplateLines = res[0].templateLines;
                        } else{
                        this.commonService.info('something went wrong','');
                        }
                    });
                 break;
            case 'BAI2_LockBox':
                const fileType2 = 'BAI2_LOCKBOX';
                this.finalLineInfoCols = this.bai2LockBoxCols;
                this.fileTemplate.multipleRowIdentifiers = false;
                this.fileTemplatesService.fileTemplLinesBAi2(this.fileTemplate.delimiter, fileType2).subscribe((res) => {
                        if(res && res[0]){
                        this.fileTemplateLines = res[0].templateLines;
                        }  else{
                        this.commonService.info('something went wrong','');
                        }
                    });
                 break;
             case 'MT940_Standard':
                const fileType3 = 'MT940_Standard';
                this.finalLineInfoCols = this.mt940Cols;
                this.fileTemplate.multipleRowIdentifiers = false;
                this.fileTemplatesService.fileTemplLinesBAi2(this.fileTemplate.delimiter, fileType3).subscribe((resp) => {
                        if(resp && resp[0]){
                        this.fileTemplateLines = resp[0].templateLines;
                     } else{
                        this.commonService.info('something went wrong','');
                     }
                    });
                 break;
        }
        this.finalInfoColsOriginalLength =this.finalLineInfoCols.length;
        for (let i = 0; i < this.finalLineInfoCols.length; i++) {
            this.columnOptions.push({ label: this.finalLineInfoCols[i].header, value: this.finalLineInfoCols[i] });
        }
        for (let v = 0; v < this.finalLineInfoCols.length;) {
        if(this.finalLineInfoCols[v].hide)  {
                 this.finalLineInfoCols.splice(v,1);
               //  //console.log(' this.finalLineInfoCols isss'+JSON.stringify( this.finalLineInfoCols));
             }    else{
             v= v+1;
             }
            }
// //console.log('this.columnOptions before'+JSON.stringify(this.columnOptions));
    }
    selectedDelimiter() {
        //console.log('selected delimiter ' + this.fileTemplate.delimiter);
    }
    assignMasterTableColumnRefForMI() {
        let miInd: number ;
        this.sampleDataAfterDropForMI.forEach((obj) => {
            if (obj.templateLines) {
                let lastMasterCol: any = [];
                let MasterColName: any = "";
                let MasterColNum: number ;
                let ColNum: number ;
                const ref: any = "";
                // //console.log('before'+JSON.stringify(this.fileTemplateLines));
                let FT = new FileTemplateLines();
                let rowIdent: any;
                obj.templateLines.forEach((tempLine) => {
                    if (tempLine.recordIdentifier){
                        rowIdent = tempLine.recordIdentifier;
                    }
                    if (tempLine.lastMasterTableRefCol) {
                        ////console.log('tempLinesrk'+JSON.stringify(tempLine));
                        lastMasterCol = tempLine.masterTableReferenceColumn.split("_");
                        MasterColName = lastMasterCol[0];
                        MasterColNum = parseInt(lastMasterCol[1].toString(),10);
                        if (tempLine.lastColNumber) {
                            ColNum = tempLine.columnNumber;
                            FT = tempLine;
                        }
                    }  else {
                        ////console.log('one is also not true');
                        MasterColName = 'FIELD';
                        MasterColNum = 0;
                    }


                });
                if (MasterColName) {
                    if (FT.masterTableReferenceColumn) {
                        lastMasterCol = FT.masterTableReferenceColumn.split("_");
                        MasterColName = lastMasterCol[0];
                        MasterColNum = parseInt(lastMasterCol[1].toString(),10);
                        if (ColNum <= 0) {
                            ColNum = FT.columnNumber;
                        }  else {

                        }
                    }

                }
                let lineNo = 1;
                obj.templateLines.forEach((FTline) => {
                     if (FTline.recordIdentifier){
                         FTline.columnHeader = FTline.columnHeader+ ' - ' + FTline.recordIdentifier;
                     }
                    if (!FTline.recordIdentifier){
                        FTline.recordIdentifier = rowIdent;
                    }
                    if (!FTline.masterTableReferenceColumn) {
                        MasterColNum += 1;
                        if (MasterColNum < 10){
                            FTline.masterTableReferenceColumn = MasterColName + '_0' + MasterColNum;
                        }    else{
                            FTline.masterTableReferenceColumn = MasterColName + '_' + MasterColNum;
                        }
                        ColNum += 1;
                        FTline.columnNumber = ColNum;

                    }
                    FTline.lineNumber = lineNo;
                    lineNo = lineNo + 1;

                });
                ////console.log('after'+JSON.stringify(this.fileTemplateLines));
            }
            miInd = miInd + 1;
        });
    }
    assignMasterTableColumnRef() {
        let lastMasterCol: any = [];
        let MasterColName: any = "";
        let MasterColNum = 0;

        let ColNum= 0;
        const ref: any = "";
        // //console.log('before'+JSON.stringify(this.fileTemplateLines));
        let FT = new FileTemplateLines();
        this.fileTemplateLines.forEach((tempLine) => {
            if (tempLine.lastMasterTableRefCol) {
                ////console.log('tempLinesrk'+JSON.stringify(tempLine));
                lastMasterCol = tempLine.masterTableReferenceColumn.split("_");
                MasterColName = lastMasterCol[0];
                MasterColNum = parseInt(lastMasterCol[1].toString(),10);
                if (tempLine.lastColNumber) {
                    ColNum = tempLine.columnNumber;
                    FT = tempLine;
                }
            }   else {
                ////console.log('one is also not true');
                MasterColName = 'FIELD';
                MasterColNum = 0;
            }


        });
        if (MasterColName) {
            if (FT.masterTableReferenceColumn) {
                lastMasterCol = FT.masterTableReferenceColumn.split("_");
                MasterColName = lastMasterCol[0];
                MasterColNum = parseInt(lastMasterCol[1].toString(),10);
                if (ColNum <= 0) {
                    ColNum = FT.columnNumber;
                    ////console.log(' FT.columnNumber'+ ColNum);
                }else {

                }
            }

        }
        let lineNo= 1;
        this.fileTemplateLines.forEach((FTline) => {
             if (FTline.recordIdentifier){
                 FTline.columnHeader = FTline.columnHeader  + ' - ' + FTline.recordIdentifier;
             }

            ////console.log('MasterColNameMasterColNum'+(MasterColName+'-'+MasterColNum));
            if (!FTline.masterTableReferenceColumn) {
                MasterColNum += 1;
                if (MasterColNum < 10){
                    FTline.masterTableReferenceColumn = MasterColName + '_0' + MasterColNum;
                } else{
                    FTline.masterTableReferenceColumn = MasterColName + '_' + MasterColNum;
                }
                ColNum += 1;
                FTline.columnNumber = ColNum;

            }
            FTline.lineNumber = lineNo;
            lineNo = lineNo + 1;

        });
        ////console.log('after'+JSON.stringify(this.fileTemplateLines));
    }
    saveTemplate() {
        //console.log('save template with ' + JSON.stringify(this.fileTemplateLines));
        this.submitted = true;
        let readyToSave= false;
        //   //console.log('testing-this.fileTemplateLinesInfo '+JSON.stringify(this.fileTemplatesPostingData))
        /**
         * File template
         */

        //   if( this.fileTemplate.startDate )
        //      this.fileTemplate.startDate = this.dateUtils.convertLocalDateToServer(this.fileTemplate.startDate);
        //   if( this.fileTemplate.endDate )if( this.fileTemplate.startDate )
        //       this.fileTemplate.endDate = this.dateUtils.convertLocalDateToServer(this.fileTemplate.endDate);
        if (!this.duplicateFileTempName) {
            if (this.isCreateNew) {
                // if (this.startDateChange) {
                //     this.fileTemplate.startDate = new Date(this.fileTemplate.startDate.getTime() + 86400000);
                // }
                // if (this.endDateChange) {
                //     this.fileTemplate.endDate = new Date(this.fileTemplate.endDate.getTime() + 86400000);
                // }
            }   else if (this.isEdit) {
                if (this.copyTemplate) {
                    this.fileTemplate.id = null;
                    if(this.sourceProfileAssignments && this.sourceProfileAssignments.id){
                        this.sourceProfileAssignments.id = null;
                    }
                    
                   

                }

                // if (this.fileTemplate.startDate.getTime()){
                //     this.fileTemplate.startDate = new Date(this.fileTemplate.startDate.getTime() + 86400000);
                // }
                    
                // if (this.fileTemplate.endDate && this.fileTemplate.endDate.getTime()){
                //     this.fileTemplate.endDate = new Date(this.fileTemplate.endDate.getTime() + 86400000);
                // }
                    
            }


            if (this.isCreateNew) {
                this.fileTemplate.status = 'Active';
                this.fileTemplate.enabledFlag = true;
            }  else {
                if (this.fileTemplate.enabledFlag) {
                    this.fileTemplate.status = 'Active';
                }  else {
                    this.fileTemplate.status = 'Inactive';
                }
            }
            if (this.data && this.data[0]){
                this.data[0]['colHeaders'] = this.colHeaders;
            }
               
            this.fileTemplate.data = JSON.stringify(this.data);
            // //console.log('prints sample data hee with nam' + JSON.stringify(this.fileTemplate.data));
            if (this.fileName) {
                this.fileTemplate.sdFilename = this.fileName;

            }
            this.fileTemplatesPostingData.fileTempDTO = this.fileTemplate;
            if(this.fileTemplate.fileType.toLowerCase() == 'excel' && this.fileTemplate.excelConditions) {
                if(this.fileTemplate.excelConditions && this.fileTemplate.excelConditions.columnsList ) {
                    const cols: any=[];
                    for(let c=0;c<this.fileTemplate.excelConditions.columnsList.length;c++) {
                        cols.push(this.fileTemplate.excelConditions.columnsList[c].col);
                    }
                    this.fileTemplatesPostingData.fileTempDTO.startRowColumns = cols;
                }
                if(this.fileTemplatesPostingData.fileTempDTO.excelConditions){
                    this.fileTemplatesPostingData.fileTempDTO.excelConditions.columnsList =null;
                }
               
                this.fileTemplatesPostingData.endRowConditionsList = this.fileTemplate.excelConditions.endConditionsList;
                this.fileTemplatesPostingData.skipRowConditionsList = this.fileTemplate.excelConditions.skipConditionsList;
            }
            //this.fileTemplatesPostingData.fileTempDTO ['rowIdentifier']=this.rowIdentifier;
            /**   
             * File template lines
             */
            this.assignMasterTableColumnRef();
            // this.fileTemplateLinesInfo = this.fileTemplateLinesInfo.concat(this.fileTemplateLines);
            if (!this.fileTemplatesPostingData.fileTemplateLinesListDTO) {
                this.fileTemplatesPostingData.fileTemplateLinesListDTO = [];
            }
            if (this.fileTemplate.multipleRowIdentifiers) {
                this.assignMasterTableColumnRefForMI();
                this.fileTemplatesPostingData.multipleRIList = this.rowIdentifiersList;
                if(this.copyTemplate && this.fileTemplatesPostingData.multipleRIList )  {
                    
                        for(let c=0;c<this.fileTemplatesPostingData.multipleRIList.length;c++){
                            this.fileTemplatesPostingData.multipleRIList[c]['templateId']=null;
                            this.fileTemplatesPostingData.multipleRIList[c]['id']=null;
                        }
                    
                }
                //console.log(' this.fileTemplate hasnowwww'+JSON.stringify( this.fileTemplate));
                if (this.sampleDataAfterDropForMI && this.sampleDataAfterDropForMI.length > 0 && this.sampleDataAfterDropForMI.length == this.rowIdentifiersList.length) {
                    let MIIndex = 0;
                    this.sampleDataAfterDropForMI.forEach((MIObj) => {

                        let emptyLines=0;
                        if (MIObj.templateLines && MIObj.templateLines.length > 0) {
                            for(let c=0;c<MIObj.templateLines.length;c++)  {
                                if(this.copyTemplate) {
                                    MIObj.templateLines[c].id=null;
                                    MIObj.templateLines[c].templateId=null;
                                }
                               if(MIObj.templateLines[c].columnHeader) {
           
                               } else{
                                   emptyLines = emptyLines+1;
                                   this.commonService.error(
                                       '',
                                       'Column Header is empty in line - '+(c+1) +' for '+MIObj.rowIdentifier
                                   )
                                   return;
                               }
                            }
                               if(emptyLines == MIObj.templateLines.length)  {
                                   this.commonService.error(
                                       '',
                                       'Empty lines found!'
                                   )
                                   return;
                               } else {
                                this.fileTemplatesPostingData.fileTemplateLinesListDTO.push(MIObj.templateLines);
                                if (MIObj.data) {
                                    this.fileTemplatesPostingData.multipleRIList[MIIndex].data = JSON.stringify(MIObj.extractedData);
                                }
                                MIObj.templateLines.forEach((line) => {
                                    line['interRI'] = MIObj.rowIdentifier;
                                });
                                MIIndex = MIIndex + 1;
    
                               }                       
                        } else {
                            this.commonService.error(
                                'Error','No lines found for ' + MIObj.rowIdentifier
                            );
                            return;
                        }
                       



                    });
                    if (MIIndex == this.sampleDataAfterDropForMI.length){
                        readyToSave = true;
                    }
                       
                }  else {
                    if (this.rowIdentifiersList && this.rowIdentifiersList.length > 0) {
                        this.commonService.error(
                            '','Refresh to get template lines based on identifiers'
                        )
                    }
                }
            }  else {
                this.fileTemplate.multipleRowIdentifiers = false;
                if (this.fileTemplateLines && this.fileTemplateLines.length > 0) {
                    let emptyLines=0;
                 for(let c=0;c<this.fileTemplateLines.length;c++)  {
                    if(this.fileTemplateLines[c].columnHeader){

                    }  else{
                        emptyLines = emptyLines+1;
                        this.commonService.error(
                            '',
                            'Column Header is empty in line - '+(c+1)
                        )
                        return;
                    }
                 }
                    if(emptyLines == this.fileTemplateLines.length) {
                        this.commonService.error(
                           '', 'Empty lines found!'
                        )
                        return;
                    } else  {
                        if (this.copyTemplate) {
                            for (let c = 0; c < this.fileTemplateLines.length; c++) {
                                this.fileTemplateLines[c].id = null;
                                this.fileTemplateLines[c].templateId = null;
                            }
                        }
                        this.fileTemplatesPostingData.fileTemplateLinesListDTO[0] = this.fileTemplateLines;
                        readyToSave = true;
                    }
                   
                } else {
                    this.commonService.error(
                       '', 'No lines found!'
                    )
                }

            }


            /**
             * Source profile assignment
             */
            if (this.sourceProfilesAdded){
                this.fileTemplatesPostingData.sourceProfileFileAssignmentDTO = this.sourceProfilesAdded;
            }
             
            if (this.copyTemplate){
                this.fileTemplatesPostingData.sourceProfileFileAssignmentDTO = this.sourceProfileAssignments;
            }
                

            /* this.fileTemplateLinesInfo.forEach(line=>
             {
                 line.edit=false;   
                 line.lastLine =false; 
                 this.enableDeleteButton = false;
             });*/
            //fetch saved template

            let tempIdAfterSave: any = [];
            //   this.fileTemplate.startDate.addDays(1);
            //   this.fileTemplate.endDate.addDays(1);
            // if (this.fileTemplate.startDate) {
            //     this.fileTemplate.startDate = this.fileTemplate.startDate.getDate() + 1;
            // }
            // if (this.fileTemplate.endDate) {
            //     this.fileTemplate.endDate = this.fileTemplate.endDate.getDate() + 1;
            // }
            
            console.log('testing-this.fileTemplateLinesInfo ' + JSON.stringify(this.fileTemplatesPostingData));
            if (readyToSave) {
                this.fileTemplatesService.postFileTemplates(this.fileTemplatesPostingData).subscribe((res: Response) => {
                    tempIdAfterSave = res;
                    if (tempIdAfterSave && tempIdAfterSave.taskStatus && tempIdAfterSave.taskStatus.toString().toLowerCase().search('success') != -1  && tempIdAfterSave.details) {
                        this.fileTempId = tempIdAfterSave.details;
                        this.fileTemplatesService.fetchById(this.fileTempId).subscribe(
                            (resp: FileTemplates) => {
                                this.fileTemplate = resp;
                                //    if (this.fileTemplate.startDate) {
                                //    this.fileTemplate.startDate = {year: this.fileTemplate.startDate.getFullYear(),
                                //             month: this.fileTemplate.startDate.getMonth() + 1,
                                //             day: this.fileTemplate.startDate.getDate()};
                                //        }
                                //   if (this.fileTemplate.endDate) {
                                //     this.fileTemplate.endDate = {year: this.fileTemplate.endDate.getFullYear(),
                                //             month: this.fileTemplate.endDate.getMonth() + 1,
                                //             day: this.fileTemplate.endDate.getDate()};
                                //      }
                                // if (this.fileTemplate.startDate) {
                                //     this.fileTemplate.startDate =  this.fileTemplate.startDate.getDate() + 1;
                                // }
                                // if (this.fileTemplate.endDate) {
                                //     this.fileTemplate.endDate =   this.fileTemplate.endDate.getDate() + 1;
                                // }
                                this.fileTemplatesService.fetchTemplateLinesByTemplateId(this.detailId).subscribe((response: any) => {
                                    this.fileTemplateLines = response;
                                    //this.rowIdentifier=this.fileTemplateLines[0].recordIdentifier;
                                    //   //console.log('printed row id'+ this.rowIdentifier);
                                    if (this.fileTemplateLines === []){
                                        this.dropfile = true;
                                    }else{
                                        this.dropfile = false;
                                    }

                                });
                                this.fetchSourceProfileAssignments(this.fileTemplate.id);
                                if (!this.createFromRemoteArea) {
                                    if (this.fileTemplate.id) {
                                        let link: any = '';
                                         link = ['/file-templates', { outlets: { 'content': this.fileTemplate.id + '/details' } }];
                                      // link = ['/file-templates', {outlets: {'content': ['file-templates-home']}}];
                                       this.router.navigate(link);
                                        this.commonService.success(
                                            'Saved!',
                                            this.fileTemplatesPostingData.fileTempDTO.templateName + ' saved successfully!'
                                        )
                                    }
                                }  else {
                                    this.fileTemplatesService.returnToWQ = true;
                                    this.dataManagementWqService.loadCreateTemplateForm = false;
                                    this.createFromRemoteArea = null;
                                    this.dataManagementWqService.showMatchedTemplates = false;
                                    if (this.fileTemplate) {
                                        if (!this.dataManagementWqService.matchedTemplates){
                                            this.dataManagementWqService.matchedTemplates = [];
                                        }
                                        const temp = [];
                                        temp.push(this.fileTemplate.id);
                                        temp.push(this.fileTemplate.templateName);
                                        if (this.sourceProfileAssignments && this.sourceProfileAssignments.id){
                                            temp.push(this.sourceProfileAssignments.id);
                                        }
                                        this.dataManagementWqService.matchedTemplates.push(temp);
                                        this.dataManagementWqService.showMatchedTemplates = true;
                                    }

                                }


                            });
                        this.fileTemplateLinesInfo = [];
                    }else if(tempIdAfterSave && tempIdAfterSave.taskStatus && tempIdAfterSave.taskStatus.toString().toLowerCase().search('success' ) == -1){
                       if(tempIdAfterSave && tempIdAfterSave.details){
                        this.commonService.error(
                            tempIdAfterSave.details,
                            ''
                        );
                    }
                    }


                });

                if (this.isEdit) {
                    this.isEdit = false;
                }
                if (this.isCreateNew) {
                    this.isCreateNew = false;
                }
                this.isViewOnly = true;

            }
        }

    }
    /**
    * File template lines start
    */
    addLinee() {
        if (this.fileTemplateLines){
            this.fileTemplateLines = [...this.fileTemplateLines, {
                "edit": true,
                "columnHeader": '',
                "enableConstant" : true,
                "recordTYpe":'Custom Column'
            }];
        }
          
    }
    hidecolumn(event)  {
        //console.log('event in hide'+event);
         //console.log('col options in hide'+JSON.stringify(this.columnOptions));
        //console.log('finalLineInfoCols in hide'+JSON.stringify(this.finalLineInfoCols));
        if( this.finalInfoColsOriginalLength == this.finalLineInfoCols.length)   {
            this.finalLineInfoCols.forEach((line)=>{
                line.hide= false;
            });
        }
        if(this.finalLineInfoCols.length>0){
            this.finalLineInfoCols[this.finalLineInfoCols.length-1].hide=false;
        }
       
        //this.finalLineInfoCols.
    }
    setAsLastLine() {
        const lengthOfLines = this.fileTemplateLinesInfo.length;
        this.fileTemplateLinesInfo.forEach((line) => {
            line.lastLine = false;
        });
        this.fileTemplateLinesInfo[lengthOfLines - 1].lastLine = true;
    }
    saveSingleLine(line: any) {
        this.fileTemplatesService.update(line).subscribe((res: any) => {
        });
    }
    onRowSelect(event) {
        ////console.log('line number for removed is'+event.data);
    }
    removeLineFromExtractedLines(selectedIndex) {
        if (this.fileTemplate.multipleRowIdentifiers == true) {
            let riToDelete: any;
            let colHeaderToDelete: any;
            if (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[selectedIndex]) {
                if (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[selectedIndex].recordIdentifier){
                    riToDelete = this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[selectedIndex].recordIdentifier;
                }
                    

                if (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[selectedIndex].columnHeader){
                    colHeaderToDelete = this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[selectedIndex].columnHeader;
                }
                    

            }
            this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines = this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines.filter((val, i) => i != selectedIndex);
            // this.sampleDataForMIInVIewMode[this.selectedTabIndex].colHeaderValues.forEach(ele=>{
            //     ele.splice(selectedIndex, 1);
            // });
            // this.sampleDataForMIInVIewMode[this.selectedTabIndex].sampleData.forEach(ele=>{
            //     ele.splice(selectedIndex, 1);
            // });


            if (this.sampleDataForMIInVIewMode.length > 1) {
                this.sampleDataForMIInVIewMode[this.selectedTabIndex].forEach((ele) => {
                    if (ele.rowIdentifier == riToDelete) {
                        ele.colHeaderValues.splice(ele.colHeaderValues.indexOf(colHeaderToDelete), 1);

                        if (ele.sampleData) {
                            ele.sampleData.forEach((line) => {
                                if (line) {
                                    let objInd= 0;
                                    line.forEach((objInLine) => {

                                        if (objInLine[0] && objInLine[0] == colHeaderToDelete) {
                                            line.splice(objInd, 1);
                                        }
                                        objInd = objInd + 1;
                                    });
                                }
                            });
                        }
                    }

                });
            }
            if (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines.length == 0) {
                this.addLineForMI(this.selectedTabIndex);
            }
        } else {
            let riToDelete: any;
            let colHeaderToDelete: any;
            if (this.fileTemplateLines[selectedIndex]) {
                if (this.fileTemplateLines[selectedIndex].recordIdentifier){
                    riToDelete = this.fileTemplateLines[selectedIndex].recordIdentifier;
                }
                    

                if (this.fileTemplateLines[selectedIndex].columnHeader){
                    colHeaderToDelete = this.fileTemplateLines[selectedIndex].columnHeader;
                }
           

            }

            this.fileTemplateLines = this.fileTemplateLines.filter((val, i) => i != selectedIndex);
            this.fileTemplateLines = this.fileTemplateLines;
            //console.log('fileTemplateLines=>>' + JSON.stringify(this.fileTemplateLines));
            if (this.data) {
                if (this.data.length > 1) {
                    this.data.forEach((ele) => {
                        if (ele.rowIdentifier == riToDelete) {
                            ele.colHeaderValues.splice(ele.colHeaderValues.indexOf(colHeaderToDelete), 1);
                            //console.log('ele.sampleData' + JSON.stringify(ele.sampleData));
                            if (ele.sampleData) {
                                ele.sampleData.forEach((line) => {
                                    if (line) {
                                        let objInd = 0;
                                        line.forEach((objInLine) => {

                                            if (objInLine[0] && objInLine[0] == colHeaderToDelete) {
                                                line.splice(objInd, 1);
                                            }
                                            objInd = objInd + 1;
                                        });
                                    }
                                });
                            }
                        }

                    });
                } else {

                    this.data.forEach((ele) => {
                        if (ele) {
                            if (ele.sampleData) {
                                ele.sampleData.forEach((ele1) => {
                                    ele1.splice(selectedIndex, 1)
                                });
                            }

                            if (ele.colHeaderValues) {
                                ele.colHeaderValues.splice(selectedIndex, 1);
                            }


                        }

                    });

                }
            }

            //console.log('data=>>' + JSON.stringify(this.data));
            if (this.fileTemplateLines.length == 0) {
                this.addLinee();
            }
        }
    }
    clearFields(i: any) {
        this.removeLineFromExtractedLines(i);
    }
    removeLine(line: any) {
        // //console.log('line number for removed'+line.lineNumber);
        this.fileTemplateLinesInfo.splice(this.fileTemplateLinesInfo.indexOf(line), 1);
        //sort and shift line numbers

        if (this.fileTemplateLinesInfo.length == 0) {
            this.addLinee();
        }
        //  this.sortAndShiftLineNumbers();
    }
    sortAndShiftLineNumbers() {
        ////console.log('sort and shift line numbers');
        let numberAtLast= 0;
        if (this.fileTemplateLines) {
            this.fileTemplateLines.forEach((line) => {
                numberAtLast = numberAtLast + 1;
                line.lineNumber = numberAtLast;
            });

            this.fileTemplateLines = this.fileTemplateLines.sort();
            numberAtLast = this.fileTemplateLines[this.fileTemplateLines.length - 1].lineNumber;
        }
        //console.log('numberAtLast' + numberAtLast);
        if (this.fileTemplateLinesInfo) {
            this.fileTemplateLinesInfo.forEach((line) => {
                numberAtLast = numberAtLast + 1;
                line.lineNumber = numberAtLast;
            });
            this.fileTemplateLinesInfo = this.fileTemplateLinesInfo.sort();
        }  else {
            this.lastLineNumber = numberAtLast;
        }
    }
    clearLineData(line: any) {
        // //console.log('clear line data');
        ////console.log('line index to be edited is'+this.fileTemplateLines.indexOf(line));
        const index = this.fileTemplateLines.indexOf(line);
        // //console.log('line after editing'+JSON.stringify(line));

        this.fileTemplatesService.findLine(line.id).subscribe((res: any) => {
            this.fileTemplateLines[index] = res;
        });


    }
    /**
    * File template lines end
    */
    /**
     * Source profile assignment
     */


    selectedProfile() {
        if (this.selectedProfileName == '-1') {
            let recentRoutes = this.sessionStorage.retrieve('recentRoutes');    // Get existing routes object from session storage
            if (recentRoutes == undefined) {  // If object is undefined create new object
                recentRoutes = {};
            }
            Object.assign(recentRoutes, {
                fileTemplatesRoute: {    // Assign new item to existing routes json
                    parentRoute: currentRoute.parent.snapshot.url.map((segment) => segment.path).join("/"),
                    childRoute: currentRoute.snapshot.url.map((segment) => segment.path).join("/")
                }
            });
            this.sessionStorage.store('recentRoutes', recentRoutes);  // Store object into session storage
            const keysByIndex = Object.keys(recentRoutes);    // TO get all keys into one array
            this.sessionStorage.store('fileTemplatesInfo', this.fileTemplate);  // Store current filled Info
            this.sessionStorage.store('fileTemplateLinesInfo', this.fileTemplateLines);  // Store current filled Lines Info
            this.router.navigate(['/source-profiles', { outlets: { 'content': ['source-profiles-new'] } }]);
        } else if (this.selectedProfileName) {

            this.displaySelectedProfile = true;
            //console.log(this.profilesList);
            this.profilesList.forEach((prof) => {
                if (prof.name === this.selectedProfileName) {
                    ////console.log('create new1');
                    this.sprofileId = prof.id;
                }
            });
            this.sourceProfilesAdded.sourceProfileId = this.sprofileId;
        }
    }
    selectedFreqType() {
        this.sourceProfilesAdded.frequencyType = this.selectedFrequencyType;
    }
    fetchSourceProfileAssignments(templateId: any) {
        this.SPAService.fetchSPAByTempId(templateId).subscribe((res: Response) => {
            const sourceProfileAssignments = res;
            this.sourceProfileAssignments = sourceProfileAssignments[0];
            //console.log('this.sourceProfileAssignments' + JSON.stringify(this.sourceProfileAssignments));
            if (this.sourceProfileAssignments) {
                this.selectedProfileId = this.sourceProfileAssignments.sourceProfileId;
            }
        });

    }
    mouseOverRow(i: number) {
        this.mouseOnRow = i;
    }
    mouseOverOnRow(i: number) {
        this.mouseOverRowNo = i;
    }
    //drop zone
    getFiles(event) {
        this.files = event.target.files;
        this.file = this.files[0];
        ////console.log(this.file);
        let msg: any;
        //this.filesFromButton.push(this.file);
        ////console.log('file size in files from button arr:'+this.filesFromButton.length);
        this.fileTemplatesService.uploadFile(this.file).subscribe(
            (res: Response) => {
                msg = res.status;
                if (msg == 'success') {
                    this.uploadStatus = true;
                }
            }
        );
    }
   
    public onFileDrop(filelist: FileList,refresh:boolean): void {
        // if(this.fileTemplate.fileType == 'Excel')
        // {
        //     let columnsList : any = ['Amount','Account description','Flow','Budget','Stat.','Trans date','Value date'];
        //     this.excelFileColumnList = columnsList;
             this.lookUpCodeService.fetchLookUpsByLookUpType('VARCHAR').subscribe((res: any) => {
                this.operatorList = res;
        //         //console.log('operators size is '+this.operatorList.length);
             });
        // }
        // else{
            //console.log('file type is'+this.fileTemplate.fileType);
            let proceedWithNewFile= false;
            if (this.copyTemplate) {
                const data = {
                    Proceed: 'Proceed',
                    cancel: 'No Thanks',
                    message: 'Template lines along with validations gets refreshed. Are you sure to reset and proceed with new file ?'
                };
    
                const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    width: '400px',
                    data: data,
                    disableClose: true
                });
    
                dialogRef.afterClosed().subscribe((result )=> {
    
                    if (result && result == 'Proceed') {
                        proceedWithNewFile = true;
                        //console.log('proceedWithNewFile' + proceedWithNewFile + '=>result is' + result);
                        this.proceedWithFile(filelist,refresh);
                    } else if (result && result == 'cancel') {
    
                    }
                });
            }  else{
                this.proceedWithFile(filelist,refresh);
            }
    
       // }
       
    }
    proceedWithFile(filelist:FileList,refresh:boolean) {
      
            this.closeSDView();
            //console.log('on file drop called' + this.fileTemplate.delimiter);
            let msg: any;
            const fileItem: FileItem = this.uploader.queue[this.uploader.queue.length - 1];
            this.uploader.queue = [];
            this.uploader.queue[0] = fileItem;
            if (filelist && filelist.length > 0) {
                // //console.log('file list length >0');
                this.filesAdded = filelist;
                const file: File = filelist[0];
                this.fileName = file.name;
                if (this.fileTemplate.multipleRowIdentifiers) {
                    if (this.rowIdentifiersList && this.rowIdentifiersList.length > 0) {
                        for(let c=0;c<this.rowIdentifiersList.length;c++) {
                            this.rowIdentifiersList[c].creationDate=null;
                            this.rowIdentifiersList[c].lastUpdatedDate=null;
                            this.rowIdentifiersList[c].createdBy=null;
                            this.rowIdentifiersList[c].lastUpdatedBy=null;
                        }
                        let refreshDataWith ;
                        if(refresh){
                            refreshDataWith=  this.sampleDataAfterDropForMI ;
                        }
                        this.fileTemplatesService.fetchSampleDataByFile(file, this.rowIdentifiersList, this.fileTemplate.delimiter, true,this.fileTemplate.fileType,refreshDataWith).subscribe((res: any) => {
                            this.uploadedIcon = true;
                            this.duplicateFileTemplateColumn=false;
                            //console.log('res.status1 has' + res.status);
                            this.sampleDataAfterDropForMI = [];
                            this.sampleDataAfterDropForMI = res;
                            if (this.sampleDataAfterDropForMI) {
                                console.log('sampleDataAfterDropForMI has' + JSON.stringify( this.sampleDataAfterDropForMI ));
                                let delim = '';
                                if (this.sampleDataAfterDropForMI && this.sampleDataAfterDropForMI[0] && this.sampleDataAfterDropForMI[0].delimiter) {
                                    delim = this.sampleDataAfterDropForMI[0].delimiter;
                                    this.fileTemplate.delimiter = delim;
                                }
                                if (this.sampleDataAfterDropForMI && this.sampleDataAfterDropForMI[0] && this.sampleDataAfterDropForMI[0].fileType){
                                    this.fileTemplate.fileType = this.sampleDataAfterDropForMI[0].fileType;
                                }
                                //   this.selectedFileType();


                                this.sampleDataForMIInVIewMode = [];
                                this.sampleDataAfterDropForMI.forEach((obj )=> {
                                    const sampleDataList = [];
                                    obj['extractedData'].forEach((sampleDataObj) => {
                                        const sampleDatawithRI = {};
                                        Object.keys(sampleDataObj).forEach((key) => {

                                            const value = sampleDataObj[key];

                                            sampleDatawithRI['rowIdentifier'] = key;
                                            if (value) {
                                                sampleDatawithRI['sampleData'] = value;
                                                const headers: any = [];
                                                if (value[0]) {
                                                    value[0].forEach((objct) => {
                                                        if (objct) {
                                                            headers.push(objct[0]);
                                                        }
                                                    });
                                                    sampleDatawithRI['colHeaderValues'] = headers;
                                                }
                                            }
                                        });
                                        sampleDataList.push(sampleDatawithRI);
                                    });
                                    this.sampleDataForMIInVIewMode.push(sampleDataList);
                                });
                                this.uploadedIcon = true;
                                //this.closeSDView();
                                // this.viewSDdata();
                            } else {
                                this.uploadedIcon = true;
                                this.commonService.error(
                                    
                                    'something went wrong. Try again',''
                                )
                            }
                        }
                            ,
                            (res: Response) => {
                                this.onError(res.json()
                                )
                                this.commonService.error('Internal Server Error!', '');
                            }
                        );
                    }  else {
                        this.commonService.info( 'Select row identifiers','');
                    }
                }  else {
                    if (!this.fileTemplate.skipRowsStart) {
                        //console.log('undefined start');
                        this.fileTemplate.skipRowsStart = 0;
                    }
                    if (!this.fileTemplate.skipRowsEnd) {
                        //console.log('undefined end');
                        this.fileTemplate.skipRowsEnd = 0;
                    }
                    const rowIdent = new RowIdentifiersList();
                    rowIdent.rowIdentifier = this.fileTemplate.rowIdentifier;
                    //   if(!  this.rowIdentifiersList)
                    //       this.rowIdentifiersList=[];
                    //   this.rowIdentifiersList.push(rowIdent);
                    if (!this.fileTemplate.rowIdentifier || this.fileTemplate.rowIdentifier == undefined){
                        this.fileTemplate.rowIdentifier = '';
                    }
                        
                        let excelConditionsList: any;
                        const columnsList: any=[];
                        if(this.fileTemplate.excelConditions && this.fileTemplate.excelConditions.columnsList)  {
                            for(let c=0;c<this.fileTemplate.excelConditions.columnsList.length;c++) {
                                columnsList.push(this.fileTemplate.excelConditions.columnsList[c].col);
                            }
                    excelConditionsList = {
                        'filePath':null,
                        'colNames': columnsList,
                        'skipConditions': this.fileTemplate.excelConditions.skipConditionsList,
                        'endConditionsList':this.fileTemplate.excelConditions.endConditionsList,
                        'unMerge':true
                    };
                        }
                        let refreshDataWith ;
                        if(refresh){
                            refreshDataWith=  this.extractedFileData ;
                        }
                    this.fileTemplatesService.fileUpload(file, this.fileTemplate.skipRowsStart, this.fileTemplate.skipRowsEnd, this.fileTemplate.rowIdentifier, this.fileTemplate.delimiter,this.fileTemplate.fileType,excelConditionsList,refreshDataWith)
                        .subscribe(
                            (res: any) => {
                                this.uploadedIcon = true;
                                this.data = [];
                                this.duplicateFileTemplateColumn=false;
                                //console.log('res.status has' + res.status);
                                this.extractedFileData = [];
                                this.colHeaders = [];
                                const resp: any = res;
                                ////console.log('response after drop'+JSON.stringify(resp));
                                msg = resp[0].status;
                                if (msg == 'Success' || msg == 200) {
                                    this.extractedFileData = resp[0];
                                    //console.log('this.extractedFileData has object'+JSON.stringify(this.extractedFileData));
                                    this.colHeaders = this.extractedFileData.headers;
                                    this.excelFileColumnList = this.extractedFileData.headers;
                                    this.sampleDataForMIInVIewMode = [];

                                    const sampleDataList = [];


                                    if(this.extractedFileData &&  this.extractedFileData['extractedData']){
                                        this.extractedFileData['extractedData'].forEach((sampleDataObj) => {
                                            const sampleDatawithRI = {};
                                            Object.keys(sampleDataObj).forEach((key) => {
    
                                                const value = sampleDataObj[key];
    
                                                sampleDatawithRI['rowIdentifier'] = key;
                                                if (value) {
                                                    sampleDatawithRI['sampleData'] = value;
                                                    const headers: any = [];
                                                    if (value[0]) {
                                                        value[0].forEach((objct) => {
                                                            if (objct) {
                                                                headers.push(objct[0]);
                                                            }
    
                                                        });
                                                        sampleDatawithRI['colHeaderValues'] = headers;
                                                    }
    
                                                }
                                                //  sampleDatawithRI['sampleData'] = value;
    
    
                                            });
                                            sampleDataList.push(sampleDatawithRI);
                                        });
                                    }
                                    
                                    this.data = (sampleDataList);


                                    // if(this.extractedFileData && this.extractedFileData.extractedData[0] && this.extractedFileData.extractedData[0][this.fileTemplate.rowIdentifier])
                                    // this.data = this.extractedFileData.extractedData[0][this.fileTemplate.rowIdentifier];
                                    // //console.log('this.extractedFileData.data'+ JSON.stringify(  this.data));

                                    // //console.log(' this.fileTemplate.delimiter'+ this.fileTemplate.delimiter);
                                    this.fileTemplate.fileType = this.extractedFileData.fileType;
                                    let delim= '';
                                    if (this.extractedFileData && this.extractedFileData.delimiter) {
                                        delim = this.extractedFileData.delimiter
                                        this.fileTemplate.delimiter = delim;
                                    }
                                    this.fileTemplateLines = this.extractedFileData.templateLines;
                                    this.uploadedIcon = true;
                                    // this.selectedFileType();
                                    this.lastLineNumber = +this.extractedFileData.lastLineNumber;
                                    //console.log('this.lastLineNumber:' + this.lastLineNumber);
                                    this.fileTemplateLinesInfo = [];
                                    //console.log(' this.fileTemplateLinesInfo lengthhhh' + this.fileTemplateLinesInfo.length);
                                    //  this.addLinee();
                                    if(this.fileTemplate.fileType.toLowerCase() == 'excel') {
                                        this.selectedConditionTypeIndex=1;
                                    } else{
                                        this.enableLineInfo = true;
                                        this.viewSDdata();
                                        this.refreshFile = false;
                                    }
                                }
                            }
                            ,
                            (res: Response) => {
                                this.onError(res.json()
                                )
                                this.commonService.error('Internal Server Error!', '');
                            }
                        );

                }

            } else {
                this.commonService.info(
                    'please drop a file',
                    ''
                )

            
        }
    }

    logForm(event) {
        //console.log(this.files);
    }

    public fileOverBase(file: File): void {
        if (file){
            this.hasBaseDropZoneOver = true;
        }
            
        ////console.log('hasBaseDropZoneOver'+this.hasBaseDropZoneOver);
        ////console.log('uploader?.queue?.length'+this.uploader.queue.length);
    }

    public fileOverAnother(e: any): void {
        this.hasAnotherDropZoneOver = e;
    }
    close() {
        this.uploadvar = false;
    }

    uploadFile(event) {

        this.uploadvar = true;
        //this.uploader.queue[].upload();
        //let files: any[];
        for (let i = 0; i < this.uploader.queue.length; i++) {
            // //console.log(i);
            //files.push(this.uploader.queue[i]);
            // //console.log('fileslength'+files.length);
            this.fileTemplatesService.uploadFile(this.uploader.queue[i]).subscribe(

            );
        }
    }
    //select file
    fileChange(file: any) {
        this.onFileDrop(file.files,false);
    }
    closeSDView() {
        this.sourceData = false;
        // $('#sampleSourceData').hide('1100');
        this.dropfile = true;
        $('.nonsdviewlong').addClass('col-md-4');
        $('.nonsdviewlong').removeClass('col-md-6');
        $('.sdviewswith').addClass('col-md-9');
        $('.sdviewswith').removeClass('col-md-4');
        $('.nonsdview').addClass('col-md-2');
        $('.nonsdview').removeClass('col-md-3');
        $('#sampleSourceData').addClass('closeSampleSourceData');
        $('.sdtitle').css('display', 'block');
        //$('.on-click-sdFileName').css('display','block');


    }
    viewSDdata() {
        this.sourceData = true;
        //$('#sampleSourceData').show('1100');
        this.dropfile = false;
        $('.nonsdviewlong').removeClass('col-md-4');
        $('.nonsdviewlong').addClass('col-md-6');
        $('.sdviewswith').removeClass('col-md-9');
        $('.sdviewswith').addClass('col-md-4');
        $('.nonsdview').removeClass('col-md-2');
        $('.nonsdview').addClass('col-md-3');
        $('.sdtitle').css('display', 'none');
        // $('.on-click-sdFileName').css('display','none');

    }
    scrolltobottom() {
        $("html, body").animate({ scrollTop: $(document).height() }, 1000);
    }
    //Author : Shobha
    refreshAndExtractCSV() {
        this.onFileDrop(this.filesAdded,true);
        this.refreshFile = true;
        this.fileTemplateLines = [];
        this.colHeaders = [];
        this.data = [];
        this.fileTemplateLinesInfo = [];
    }

    // Full screen Toggle Method 
    toggleFullScreen() {
        if (this.fullScreenView == true) {
            this.fullScreenView = false;

        } else {
            this.fullScreenView = true;
        }

    }
    valuechange(newValue) {
        //console.log(newValue)
    }

   
    //Author : Shobha
    checkFileTemplateName(name) {
        // //console.log('on change called: ', name);
        if (name.includes('-')) {
            //console.log('contains hyphen');
            this.duplicateFileTempName = true;
            this.templateErroMsg = 'Template name should not contains hyphen(-)';
        } else {
            let count = 0;
            this.fileTemplatesService.fetchFileTemplates().subscribe((res: any) => {

                this.fileTemplatesList = res.json;
                this.fileTemplatesList.forEach((fileTemp) => {

                    let enteredName: string;
                    enteredName = name.toLowerCase();
                    let existingName: string;
                    existingName = fileTemp.templateName.toLowerCase();

                    if (existingName == enteredName) {
                        count++;
                    }
                });
                if (count > 0) {
                    this.duplicateFileTempName = true;
                    this.templateErroMsg = 'Template name already exists';
                } else {
                    //console.log('duplicateFileTempName' + this.duplicateFileTempName);
                    this.duplicateFileTempName = false;
                }
            });
        }

    }
    validationMsg() {
        this.submitted = true;
        if (this.duplicateFileTempName == true) {
            this.commonService.error(
                'Warning!',
                'Template already exists with this name'
            )
        } else {
            // this.commonService.error(
            //     'Warning!',
            //     'Fill Mandatory Fields'
            // )
        }
      

    }
    addfunction(i) {
        //    if(!this.showFunction)
        //        this.showFunction = [];
        //    if(this.saveFunction && !this.saveFunction[i])
        //    {
        //         this.fileTemplateLines[i].formula='';
        //        this.fileTemplateLines[i].functionName='';
        //         this.fileTemplateLines[i].example='';
        //    }


        //    this.showFunction[i] =true;
        if(!this.isViewOnly) {

       
        const data = {
            addFormula: 'Add Formula', ok: 'ok', cancel: 'cancel', formula: this.fileTemplateLines[i].formula
            , functionName: this.fileTemplateLines[i].functionName
            , example: this.fileTemplateLines[i].example
        };

        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: data,
            disableClose: true
        });

        dialogRef.afterClosed().subscribe((result) => {
            //console.log('resultresult' + JSON.stringify(result));
            //console.log('data' + JSON.stringify(data));
            if (result && result == 'ok') {

                this.fileTemplateLines[i].formula = data.formula;
                this.fileTemplateLines[i].functionName = data.functionName;
                this.fileTemplateLines[i].example = data.example;
            }else if (result && result == 'cancel') {
                //  this.notificationService.info('Confirm saving!', 'Extraction Failed!');
            }
        });
    }
    }
    showExcelFunction(i, examp) {
        this.fileTemplateLines[i].example = examp;
    }
    saveFunc(i) {
        if (!this.saveFunction){
            this.saveFunction = [];
        }
         
        this.saveFunction[i] = true;
        this.showFunction[i] = false;
    }
    cancelFormula(i) {
        this.fileTemplateLines[i].formula = '';
        this.fileTemplateLines[i].example = '';
        this.fileTemplateLines[i].functionName = '';;
        // this.showFunction[i] =false;
    }
    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
            $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }
    checkIdentifier(e) {
        //console.log('event hasss'+e.checked);
        ////console.log('this.fileTemplate.multipleRowIdentifiers'+this.fileTemplate.multipleRowIdentifiers);
        // let data = {
        //     Proceed: 'Proceed',
        //     cancel: 'No Thanks',
        //     message: 'Changing this resets the Template lines and dependent fields.Do you want to proceed?'
        // };

        // var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
        //     width: '400px',
        //     data: data,
        //     disableClose: true
        // });

        // dialogRef.afterClosed().subscribe(result => {

        //     if (result && result == 'Proceed') {
                if (this.fileTemplate.multipleRowIdentifiers) {
                    this.hideSkipRowData = true;
                    this.viewSampleData = false;
                    this.sampleDataAfterDropForMI = [];
                    const row = new RowIdentifiersList();
                    if (!this.rowIdentifiersList){
                        this.rowIdentifiersList = [];
                    }
                   
                    this.rowIdentifiersList.push(row);
                    //clear singleRowIdentifier values
                    this.fileTemplate.rowIdentifier = '';
                    this.data = [];
        
        
                    // //console.log(' this.rowIdentifiersList'+JSON.stringify( this.rowIdentifiersList));
                    //fetch lookups
                    this.lookUpCodeService.fetchLookUpsByLookUpType('ROW_IDENTIFIER').subscribe((res: any) => {
                        this.criteriaList = res;
                    });
        
                }  else {
                    this.hideSkipRowData = false;
                    this.rowIdentifiersList = [];
                    this.viewSampleData = false;
                    this.sampleDataAfterDropForMI = [];
                    // this.addRowIdentifier();
                }
            //}
       // });
        
    }
    addRowIdentifier() {
        /*let obj : any={};
    obj['rowIdentifier'] =   this.rowIdentifiersList[  this.rowIdentifiersList.length-1].rowIdentifier;
    this. sampleDataAfterDrop.push(obj);*/
        const row = new RowIdentifiersList();
        this.rowIdentifiersList.push(row);
        if (!this.checkRows){
            this.checkRows = [];
        }
         
        this.selectedAll = false;
    }
    loadFileTemplateLine(e) {
        this.selectedTabIndex = e.index;
    }
    extractDataForRI() {
        this.filterRowIdentifiers();
    }
    pushrowIdentifiers(rowIdentifierRow, ri, event) {
        // //console.log('event isss'+JSON.stringify(event));
        if (event == false) {
            this.RIsToFetch.splice(ri, 1);
        } else {
            this.RIsToFetch.push(rowIdentifierRow);
        }
    }
    pushAll() {
        this.RIsToFetch = this.rowIdentifiersList;
        this.RIs = this.rowIdentifiersList;
    }
    selectAll() {
        for (let i = 0; i < this.rowIdentifiersList.length; i++) {
            this.rowIdentifiersList[i].selected = this.selectedAll;
        }
        this.selectedRI = this.rowIdentifiersList;
    }
    checkIfAllSelected(i) {
        this.selectedAll = this.rowIdentifiersList.every(function(item: any) {
            return item.selected == true;
        })
    }
    deleteRowIdentifier(ri) {
        this.rowIdentifiersList.splice(ri, 1);
        this.checkIfRowIdentifierExists(ri);
        if (this.rowIdentifiersList.length == 0){
            this.addRowIdentifier();
        }
           
    }
    filterRowIdentifiers() {
        this.onFileDrop(this.filesAdded,true);
        /* let selectedRowIdentifiers = [];
         this.rowIdentifiersList.forEach(rowIdentifier=>{
             if(rowIdentifier.selected)
                 {
                 selectedRowIdentifiers.push(rowIdentifier);
                 }
         });
         //console.log('selected are'+JSON.stringify(selectedRowIdentifiers));
         //api call with selectedRowIdentifiers
   
         let ind : number = 0;
         this.rowIdentifiersList.forEach(recIdentifier=>{
             if(this. sampleDataAfterDrop && this. sampleDataAfterDrop[ind])
                 {
                 this. sampleDataAfterDrop[ind].rowIdentifier = recIdentifier.rowIdentifier;
                 }
             else
                 {
                 this. sampleDataAfterDrop[ind] = [];
                 this. sampleDataAfterDrop[ind]['rowIdentifier'] =recIdentifier.rowIdentifier;
                 }
             ind = ind+1;
         });
        */
    }
    sampleDataPreview(ri) {

    }
    private onError(error) {
    }
    addLineForMI(i) {
        if (this.sampleDataAfterDropForMI[i] && this.sampleDataAfterDropForMI[i].templateLines){
            this.sampleDataAfterDropForMI[i].templateLines = [...this.sampleDataAfterDropForMI[i].templateLines, {
                "edit": true,
                "columnHeader": '',
                "enableConstant":true,
                "recordTYpe":'Custom Column'
            }];
        }
          
    }
    addFunctionForMI(i, colIndex) {
        //console.log('i=>' + i + 'colIndex' + colIndex);
        /*   if(!this.saveFunctionArrayMI )
               this.saveFunctionArrayMI=[];
           if(!this.saveFunctionArrayMI[i])
               this.saveFunctionArrayMI[i]=[];
           */
        /*  if( this.saveFunctionArrayMI && ! this.saveFunctionArrayMI[i][colIndex])
          {
              this.sampleDataAfterDropForMI[i].templateLines[colIndex].formula='';
              this.sampleDataAfterDropForMI[i].templateLines[colIndex].functionName='';
              this.sampleDataAfterDropForMI[i].templateLines[colIndex].example='';
          }*/
          if(!this.isViewOnly) {

          
        const data = {
            addFormula: 'Add Formula', ok: 'ok', cancel: 'cancel',
             formula: this.sampleDataAfterDropForMI[i].templateLines[colIndex].formula
            , functionName: this.sampleDataAfterDropForMI[i].templateLines[colIndex].functionName
            , example: this.sampleDataAfterDropForMI[i].templateLines[colIndex].example
        };

        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            width: '400px',
            data: data,
            disableClose: true
        });

        dialogRef.afterClosed().subscribe((result) => {
            //console.log('resultresult' + JSON.stringify(result));
            //console.log('data' + JSON.stringify(data));
            if (result && result == 'ok') {

                this.sampleDataAfterDropForMI[i].templateLines[colIndex].formula = data.formula;
                this.sampleDataAfterDropForMI[i].templateLines[colIndex].functionName = data.functionName;
                this.sampleDataAfterDropForMI[i].templateLines[colIndex].example = data.example;
            }  else if (result && result == 'cancel') {
                //  this.notificationService.info('Confirm saving!', 'Extraction Failed!');
            }
        });

        if (this.sampleDataAfterDropForMI[i] && this.sampleDataAfterDropForMI[i].templateLines) {
            this.showFuncMI = true;
            this.showFuncIndex = i;
        }
    }
    }
    closeFormulaModel(i) {
        if (this.fileTemplateLines[i].functionName) {
            this.showFunction[i] = false;
            this.commonService.error(
                'Confirm to save',
                ''
            )

        } else {
            this.showFunction[i] = false;
        }
    }
    checkIfExists(ind) {
        let i = null;
        for (i = 0; this.rowIdentifiersList.length > i; i += 1) {
            if (ind != i) {
                const keys = Object.keys(this.rowIdentifiersList[i]);
                let matchKeyLength = 0;
                for (let j = 0; j < keys.length; j++) {
                    if (this.rowIdentifiersList[i][keys[j]] == this.rowIdentifiersList[ind][keys[j]]) {
                        matchKeyLength = matchKeyLength + 1;
                    }
                }
                if (keys.length == matchKeyLength){
                    return true;
                }else return false;
            }
        }

        return false;
    };
    
    rowIdentifiersClass() {
        if (this.showPositionCols) {
            //return 'col-md-9 col-sm-12 col-xs-12 table-responsive scrollHeight';
            return 'col-md-9 col-sm-12 col-xs-12 ';
        } else {
            //return 'col-md-7 col-sm-8 col-xs-12 table-responsive scrollHeight';
            return 'col-md-7 col-sm-8 col-xs-12';
        }
    }
    checkIfRowIdentifierExists(ind) {

        let alreadyExists = false;
        let positionInd = 0;
        this.rowIdentifiersList.forEach((rowIdentiifer) => {
            if (rowIdentiifer.criteria == 'POSITION') {
                this.showPositionCols = true;

            } else{
                positionInd = positionInd + 1;
            }
                

        });
        if (positionInd == this.rowIdentifiersList.length){
            this.showPositionCols = false;
        }
            

        alreadyExists = this.checkIfExists(ind);
        if (alreadyExists) {
            this.duplicateIdentifier = true;
            if (this.uploader.queue.length == 0) {
                this.dropfile = false;
            }
            this.commonService.info('',this.rowIdentifiersList[ind].rowIdentifier + ' already exists');
        }  else {
            this.duplicateIdentifier = false;
            this.dropfile = true;
        }
    }
    checkForColumnDuplicates(event, i) {
        //console.log('event' + event);
        if (this.fileTemplate.multipleRowIdentifiers) {
         
            //this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i]['columnHeader'];
            if(this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].columnHeader){
                for(let c=0;c<this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines.length;c++) {
                    if (
                        c != i && this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[c].columnHeader 
                         && 
                        this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].columnHeader
                         &&
                         this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[c].recordStartRow 
                         &&
                         this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].recordStartRow
                         &&
                         (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[c].recordStartRow ===
                          this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].recordStartRow)
                         &&
                         (
                             (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[c].columnHeader 
                            +'-'+this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[c].recordStartRow)
                         === 
                              (this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].columnHeader 
                             +'-'+this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].recordStartRow))) {
                        //this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].columnHeader = null;
                        this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i]['duplicateFileTemplateColumn']=true;
                        this.duplicateFileTemplateColumn=true;
                        this.tooltipValue = 'Duplicate Template Lines';
                        // this.commonService.error('Oops!!', this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[c].columnHeader + ' already exists with combination of '+this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i].recordStartRow);
                        break;
                    }else{
                        this.sampleDataAfterDropForMI[this.selectedTabIndex].templateLines[i]['duplicateFileTemplateColumn']=false;
                        this.duplicateFileTemplateColumn=false;
                    }
                }
            }
           
        } else {
           if(this.fileTemplateLines[i].columnHeader){
            for(let c=0;c<this.fileTemplateLines.length;c++) {
                //console.log(' this.fileTemplateLines[c].columnHeader'+ this.fileTemplateLines[c].columnHeader);
                //console.log(' this.fileTemplateLines[i].columnHeader'+ this.fileTemplateLines[i].columnHeader);
                if (c != i &&  this.fileTemplateLines[c].columnHeader
                    && !this.fileTemplateLines[c]['recordStartRow'] 
                    && this.fileTemplateLines[i].columnHeader
                    && !this.fileTemplateLines[i]['recordStartRow']
                    && 
                    (this.fileTemplateLines[c].columnHeader === this.fileTemplateLines[i].columnHeader)
                    ) {
                    // this.commonService.error('Oops!!', this.fileTemplateLines[c].columnHeader + ' already exists');
                       // this.fileTemplateLines[i].columnHeader =null;
                       this.fileTemplateLines[i].duplicateFileTemplateColumn=true;
                       this.duplicateFileTemplateColumn=true;
                       this.tooltipValue = 'Duplicate Template Lines'; 
                       //console.log('  this.fileTemplateLines[i].columnHeader after'+  this.fileTemplateLines[i].columnHeader);
                    break;
                }else{
                    this.fileTemplateLines[i].duplicateFileTemplateColumn=false;
                    this.duplicateFileTemplateColumn=false;
                }

                if (c != i &&  this.fileTemplateLines[c].columnHeader
                    && this.fileTemplateLines[i]['recordStartRow'] 
                    && this.fileTemplateLines[i].columnHeader
                    && this.fileTemplateLines[i]['recordStartRow']
                    && (this.fileTemplateLines[i]['recordStartRow']  === this.fileTemplateLines[c]['recordStartRow'] )
                    && (this.fileTemplateLines[c].columnHeader === this.fileTemplateLines[i].columnHeader)
                    ) {
                    // this.commonService.error('Oops!!', this.fileTemplateLines[c].columnHeader + ' already exists with combination of '+ this.fileTemplateLines[i]['recordStartRow']);
                        //this.fileTemplateLines[i].columnHeader =null;
                        this.fileTemplateLines[i].duplicateFileTemplateColumn=true;
                        this.duplicateFileTemplateColumn=true;
                    break;
                }else{
                    this.fileTemplateLines[i].duplicateFileTemplateColumn=false;
                    this.duplicateFileTemplateColumn=false;
                }
            }
           }
           
        }

    }
    startDateChanged(dt: Date) {
        if (this.fileTemplate.endDate) {
            if (this.fileTemplate.endDate < this.fileTemplate.startDate) {
                this.fileTemplate.endDate = this.fileTemplate.startDate;
            }
        }
    }
   
    pushExcelColumn() {
      //  if(this.pushFlag) {
            if(!this.fileTemplate.excelConditions.columnsList){
                this.fileTemplate.excelConditions.columnsList=[];
            }           
            if(this.excelColumn){
                let duplicateColumn=false;
                if(  this.fileTemplate.excelConditions.columnsList.length>0){
                    for(let i=0;i< this.fileTemplate.excelConditions.columnsList.length;i++){
                      //  //console.log('this.fileTemplate.excelConditions.columnsList[i].col'+this.fileTemplate.excelConditions.columnsList[i].col);
                       // //console.log('this.excelColumn'+this.excelColumn);
                        if(this.fileTemplate.excelConditions.columnsList[i].col === this.excelColumn){
                            duplicateColumn =true;
                            this.commonService.error('',this.excelColumn+' already exists');
                            break;
                        }else{
                            duplicateColumn=false;
                        }
                    }
                }
                    if(!duplicateColumn){
                        const obj = {"col":this.excelColumn};
                        if(this.fileTemplate.excelConditions){
                            this.fileTemplate.excelConditions.columnsList.push(obj);
                        }
                        this.excelColumn='';
                        if(this.fileTemplate.excelConditions && this.fileTemplate.excelConditions.columnsList && this.fileTemplate.excelConditions.columnsList.length>0){
                           // this.uploaderOptions.queueLimit = null;
                            this.dropfile = true;
                        }
                      //  this.pushFlag= false;
                    }else{
                        
                    }
                 
                
              
               
            }
          
        //}
       
          
    }
    deleteExcelColumn(i) {
        this.fileTemplate.excelConditions.columnsList.splice(i,1);
        if(this.fileTemplate.excelConditions.columnsList.length>0){
             this.dropfile = true;
         } else{
            this.dropfile=false;
         }
         
    }
    blockSpecialChar(e){
        const k = (e.which) ? e.which : e.keyCode;
        ////console.log('key code is'+k);
        if(k == 13) {
           // this.pushFlag=true;
           // this.pushExcelColumn();
           // this.pushFlag=false;
            return true;
        }  else {
            return true;
        }
       // return false;
    }
    addEndCondition(ind?:number) {
        const condition = new Condition();
        condition.type = 'end';
        //condition.columnList = this.excelFileColumnList;
        //condition.OperatorsList = this.operatorList;
        if(!this.fileTemplate.excelConditions.endConditionsList[ind+1]){
            this.fileTemplate.excelConditions.endConditionsList.push(condition);
        }
        
    }
    addSkipCondition(ind?:number) {
        const condition = new Condition();
        condition.type = 'skip';
       // condition.columnList = this.excelFileColumnList;
        //condition.OperatorsList = this.operatorList;
        if(!this.fileTemplate.excelConditions.skipConditionsList[ind+1]){
            this.fileTemplate.excelConditions.skipConditionsList.push(condition);
        }
        
    }
    deleteEndRowCondition(i){
        this.fileTemplate.excelConditions.endConditionsList.splice(i,1);
    }
    deleteSkipRowCondition(i){
        this.fileTemplate.excelConditions.skipConditionsList.splice(i,1);
    }
    checkEndConditionLogicalOperator(event){
        //console.log('fileTemplate.excelConditions.endConditionsList [endCondInd].logicalOperator'+JSON.stringify(this.fileTemplate.excelConditions.endConditionsList));
    }
    refreshExcelFile() {
        //console.log('end conditions' + JSON.stringify(this.fileTemplate.excelConditions.endConditionsList));
       
                const file: File = this.filesAdded[0];
                let excelConditionsList: any;
                const columnsList: any=[];
                if(this.fileTemplate.excelConditions && this.fileTemplate.excelConditions.columnsList){
                    for(let c=0;c<this.fileTemplate.excelConditions.columnsList.length;c++) {
                        columnsList.push(this.fileTemplate.excelConditions.columnsList[c].col);
                    }
                }
                
                excelConditionsList = {
                    'filePath':null,
                    'colNames': columnsList,
                    'skipConditions': this.fileTemplate.excelConditions.skipConditionsList,
                    'endConditionsList':this.fileTemplate.excelConditions.endConditionsList,
                    'unMerge':true
                };
        this.fileTemplatesService.fileUpload(file, this.fileTemplate.skipRowsStart, this.fileTemplate.skipRowsEnd, this.fileTemplate.rowIdentifier,
             this.fileTemplate.delimiter, this.fileTemplate.fileType,excelConditionsList,null)
            .subscribe(
                (res: any) => {
                let msg:any;
                    this.uploadedIcon = true;
                    this.data = [];
                    //console.log('res.status has' + res.status);
                    this.extractedFileData = [];
                    this.colHeaders = [];
                    const resp: any = res;
                    ////console.log('response after drop'+JSON.stringify(resp));
                    if(resp && resp[0] && resp[0].status){
                        msg = resp[0].status;
                    }
                    
                    if (msg == 'Success' || msg == 200) {
                        this.extractedFileData = resp[0];
                        //console.log('this.extractedFileData has object'+JSON.stringify(this.extractedFileData));
                        this.colHeaders = this.extractedFileData.headers;
                        this.excelFileColumnList = this.extractedFileData.headers;
                        this.sampleDataForMIInVIewMode = [];

                        const sampleDataList = [];


                        if(this.extractedFileData &&  this.extractedFileData['extractedData']){
                            this.extractedFileData['extractedData'].forEach((sampleDataObj) => {
                                const sampleDatawithRI = {};
                                Object.keys(sampleDataObj).forEach((key) => {
    
                                    const value = sampleDataObj[key];
    
                                    sampleDatawithRI['rowIdentifier'] = key;
                                    if (value) {
                                        sampleDatawithRI['sampleData'] = value;
                                        const headers: any = [];
                                        if (value[0]) {
                                            value[0].forEach((objct) => {
                                                if (objct) {
                                                    headers.push(objct[0]);
                                                }
    
                                            });
                                            sampleDatawithRI['colHeaderValues'] = headers;
                                        }
    
                                    }
                                    //  sampleDatawithRI['sampleData'] = value;
    
    
                                });
                                sampleDataList.push(sampleDatawithRI);
                            });
                        }
                        
                        this.data = (sampleDataList);


                        // if(this.extractedFileData && this.extractedFileData.extractedData[0] && this.extractedFileData.extractedData[0][this.fileTemplate.rowIdentifier])
                        // this.data = this.extractedFileData.extractedData[0][this.fileTemplate.rowIdentifier];
                        // //console.log('this.extractedFileData.data'+ JSON.stringify(  this.data));

                        // //console.log(' this.fileTemplate.delimiter'+ this.fileTemplate.delimiter);
                        this.fileTemplate.fileType = this.extractedFileData.fileType;
                        let delim = '';
                        if (this.extractedFileData && this.extractedFileData.delimiter) {
                            delim = this.extractedFileData.delimiter
                            this.fileTemplate.delimiter = delim;
                        }
                        this.fileTemplateLines = this.extractedFileData.templateLines;
                        this.uploadedIcon = true;
                        // this.selectedFileType();
                        this.lastLineNumber = +this.extractedFileData.lastLineNumber;
                        //console.log('this.lastLineNumber:' + this.lastLineNumber);
                        this.fileTemplateLinesInfo = [];
                        //console.log(' this.fileTemplateLinesInfo lengthhhh' + this.fileTemplateLinesInfo.length);
                        //  this.addLinee();
                        this.enableLineInfo = true;
                        this.viewSDdata();
                        this.refreshFile = false;

                    }
                });
    }

}