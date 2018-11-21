//#region Import Statments
import { Component, OnDestroy, OnInit, ViewChild, ElementRef, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Response } from '@angular/http';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Subscription, Subject } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService } from 'ng-jhipster';
import { FormGroup, FormControl } from '@angular/forms';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA,MdPaginator } from '@angular/material';
import { Reports, ReportColsInfo, ParametersSubmited, ReportRequestList,OozieStatus, ReportHeaders, DataViews } from './reports.model';
import { ReportsService } from './reports.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ChartData, pageSizeOptionsList } from '../../shared';
import { SchedulingModel, Parameters, Scheduling } from '../scheduling/scheduling.component';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Observable } from 'rxjs/Observable';
import { PageEvent } from '@angular/material';
import { CommonService } from '../common.service';
import { ShareViaEmailDialog } from './';
import { ConfirmDialogComponent } from '../scheduling/confirm.dialog.component';
import "rxjs/add/operator/takeWhile";
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import '../../shared/pivottable/dist/pivot.min.js';
import '../../shared/pivottable/dist/c3_renderers.js';
import Chart from 'chart.js';
import { fadeInContent } from '@angular/material/typings/select/select-animations';
declare var $: any;
declare var jQuery: any;
declare var window: any;
//#endregion
@Component({
    selector: 'jhi-run-reports',
    templateUrl: './run-reports.component.html'
})
export class RunReportsComponent implements OnDestroy, OnInit {

    //#region Variables Declarations
    // @ViewChild('paginator') paginator: MdPaginator;
    @ViewChild(Scheduling) scheduling: Scheduling;
    @ViewChild('reqListPaginator') reqListPaginator: MdPaginator;
    @ViewChild('requestName') reqNameEditBtn:ElementRef;
    private unsubscribe: Subject<void> = new Subject();      //Unsubscribe variable
    parentDiv123: any;
    selectedReport = new Reports();
    reportsList: Reports[] = [];          //To hold all reports list
    selectedReportId: any;           //To hold current selected report ID
    showReportOutput: boolean = false;  //To decide show/hide report output
    showTabular: boolean = false;       //To decide show/hide Tabular report output
    showPivot: boolean = false;         //To decide show/hide Pivot report output
    showChart: boolean = false;         //To decide show/hide chart report output    
    expandAdnlParams = false;           //To decide show/hide Additional parameters
    today = new Date();                   // It holds current date
    TemplatesHeight: any;
    cmpHeight:any;
    showSegFilters: boolean = false;
    asofDateChange: boolean = false;
    scheduleInitiated: boolean = false;
    TemplatesWidth: any;
    sortCol: string = '';
    sortOrder: string = '';
    searchStr: string = '';
    sysReqName: string = '';
    reqName:String='';
    gotopage:number=1;
    changeOPwithReq = false;
    columnFilters:any[]=[];
    limitSelectionSettings = {
        text: " ",
        enableCheckAll: false,
        enableSearchFilter: true,
        badgeShowLimit: 2
    };

    limitFieldsSelectionSettings = {
        text: " ",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: true,
        badgeShowLimit: 1
    };

    singleSlectSettings = { 
        singleSelection: true, 
        text:" ",
        enableSearchFilter: true,
        classes:"cuppaSingleSelection"
      };

    singleValueSelectionSettings = {
        text: " ",
        enableSearchFilter: false,
        badgeShowLimit: 1,
        enableCheckAll: false
    };

    multiSelectionSettingsCharts = {
        text: " ",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: true,
        limitSelection: 1,
        badgeShowLimit: 2
    };
    periodsList=[];
    reportsTableColumns = [];                   //Report table columns
    drilldownTableColumns = [];                 //Drilldown table columns
    columnOptions: any[] = [];                    //Column options for toggling
    reportInfo = new ReportColsInfo();          //To hold complete report definition to run
    reportResult: any[] = [];                     //Tabular report result
    pvtReportResult: any[] = [];                  //Pivot report result
    chartResult: any[] = [];   //Chart report result
    pageSize: number = 0;
    pageSizeOptions = [50, 100, 250, 500, 1000];
    reqListPageSizeOptions = pageSizeOptionsList;
    length: number;
    requestsLength: number = 0;
    pageEvent: PageEvent = new PageEvent();
    reqListpageEvent: PageEvent = new PageEvent();
    reportOutput: LookUpCode[] = [];             //To hold report output types
    chartTypes: LookUpCode[] = [];               //To hold chart types
    reportAllCols: Reports = new Reports();     //To hold report all columns
    reportReqList: ReportRequestList[] = [];
    multiSelCols: any[] = [];                     //To hold multi select columns for report
    multiSelRows: any[] = [];                     //To hold multi select rows for report
    singleSelCol: any;                          //To hold single column for report
    rptValCol: any;                             //To hold value column for report
    fnlObj;                                     //Final object send to server
    displayScheduling: boolean = false;          //TO display/hide scheduling dialog
    rptName: string;                            //To hold report name
    showChangeView: boolean = false;               //Show or hide change view dialog
    showParameters: boolean = false;              //Show or hide parameters
    showRequestsList: boolean = false;
    showPvtOptns: boolean = false;
    rptSubmitted: boolean = false;
    changeFromParams: boolean = false;
    urlWithRequestId: boolean = false;
    showFilters: boolean = true;
    blockUI: boolean = false;
    selectedTab:number=0;
    displayParameters: boolean;
    expandParamsHeader:boolean = true;
    rprtDate:string='';
    routeSub: any;
    selectedReqId: any;
    parametersSubmited: ParametersSubmited[] = [];
    parametersSubmitedForTable: ParametersSubmited[] = [];
    adnlParametersSubmited: ParametersSubmited[] = [];
    schedObj = new SchedulingModel();
    schedParameters: Parameters[] = [];
    schedParametersForDisplay: Parameters[] = [];
    pvtRowsEligibleCols: any[] = [];
    pvtColsEligibleCols: any[] = [];
    request:any;
    showProcessing:boolean=false;
    
    allVarcharCols = [];
    allDecimalCols = [];
    accStatusList: LookUpCode[] = []; 
    recStatusList: LookUpCode[] = [];              
    chartColors = ["#E7E9ED",
        "#FF6384",
        "#4BC0C0",
        "#FFCE56",
        "#36A2EB"
    ];
    outputPath: string;
    srcViews: any[] = []; 
    selectedviewTemp: any[]=[];

    reportsReqListTableCols = [                  //  Report list table columns
        { field: 'reqName', header: 'Request Name' },
        { field: 'createdDate', header: 'Created On' },
        { field: 'submittedTime', header: 'Submited On' },
        { field: 'generatedTime', header: 'Generated On' },
        { field: 'status', header: 'Status' }
    ];
    requestType:string = undefined;
    timer:any;
    disableRecStatus: boolean = true;
    disableAccStatus: boolean = true;
    disableRecStatusAll: boolean = false;
    disableAccStatusAll: boolean = false;
    lastScrollStop:number = 0;
    public chartOptions:any = {
        scaleShowVerticalLines: false,
        responsive: true,
        plugins: {
            datalabels: {
                display: false,
            }
        }
      };
      public chartLabels:string[] = [];
      public chartType:string = 'bar';
      public chartLegend:boolean = true;
     
      public chartData:any[] = [];
    //#endregion
    constructor(
        private reportsService: ReportsService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private dialog: MdDialog,
        private cs: CommonService,
        public datepipe: DatePipe,
        private decimalPipe: DecimalPipe,
        private router: Router,
        @Inject(DOCUMENT) private document: Document
    ) {
        this.pageSize = 50;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.pageSize;
        this.reqListpageEvent.pageIndex=0;
        this.reqListpageEvent.pageSize = this.pageSize;
        this.reportsService.getLookupValues('ACCOUNTING_STATUS','REPORTING').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
            this.accStatusList = res;
        },
        (res: Response) => {
          this.onError('Failed to load Account Status list!')
        });
        this.reportsService.getLookupValues('RECONCILIATION_STATUS').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
            this.recStatusList = res;
        },
        (res: Response) => {
          this.onError('Failed to load Reconcile Status list!')
        });
        this.reportsService.getLookupValues('REPORT_OUTPUT_TYPE').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {      //To load report output types
            this.reportOutput = res;
        });
        this.reportsService.getLookupValues('CHART_TYPE').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {              //To load chart types
            this.chartTypes=res;
        });
    }
    ngOnInit() {
        this.lastScrollStop=document.body.scrollTop;
        window.onscroll = () =>{
            let start = document.body.scrollTop;
            if((start+100) > this.lastScrollStop){
                this.expandParamsHeader=false;
            }
            this.lastScrollStop = start;
        }
        this.cmpHeight ='calc(100vh - 330px)';
        if (this.cs.screensize().height > 500) {
            this.TemplatesHeight = (this.cs.screensize().height - 450) + 'px';
        } else {
            this.TemplatesHeight = 100 + 'px';
        }

        this.sortOrder = '';
        this.sortCol = '';
        this.blockUI=true;
        this.routeSub = this.activatedRoute.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.activatedRoute.snapshot.url.map((segment) => segment.path).join('/');
            const presentPath = this.activatedRoute.snapshot.url;
            this.loadDataviews();
             //Check is 'Request ID' is there in route params or not
            if(params['reqId'] && params['id']){
                this.selectedReqId = params['reqId'];
                this.selectedReportId = params['id'];
                this.reportsService.find(this.selectedReportId).takeUntil(this.unsubscribe).subscribe((res: Reports) => { 
                    this.selectedReport=res;
                    this.rptName=this.selectedReport.reportName;
                    this.urlWithRequestId=true;
                    this.getRequestsList(undefined);
                },
                (res)=>{
                    this.blockUI=false;
                    this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
                });
            }else if (params['id']) {
                this.selectedReportId = params['id'];
                this.reportsService.find(this.selectedReportId).takeUntil(this.unsubscribe).subscribe((res: Reports) => { 
                    this.selectedReport=res;
                    this.rptName=this.selectedReport.reportName;
                    this.reqName=this.selectedReport.reportName+'-(Copy)';
                    this.reportSelected(this.selectedReportId);
                },
                (res)=>{
                    this.blockUI=false;
                    this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
                });
            }else{
                this.blockUI=false;
                this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
            }
        });
    }

    tabChange(event){
        this.reqListpageEvent.pageIndex=0;
        this.reqListPaginator.pageIndex=0;
        this.reqListpageEvent.pageSize=this.pageSize;
        this.reqListPaginator.pageSize=this.pageSize;
        switch (event.index) {
            case 1:
                this.getRequestsList('run');
                break;
            case 2:
                this.getRequestsList('request');
                break;
            default:
                this.getRequestsList(undefined);
                break;
        }
    }

    sortReqTable(event){
        let sortOrder = 'ascending';
        if (event.order < 1){
            sortOrder = 'descending';
        }
        const sortCol = event.field;
        this.getRequestsList(this.requestType,sortCol,sortOrder);
    }

    loadDataviews(){
        this.reportsService.getDataViewsList().takeUntil(this.unsubscribe).subscribe((dtViews: DataViews[]) => {
            this.srcViews=dtViews;
            // for (let i = 0, len = dtViews.length; i < len; i++) {
            //     const newView = {
            //         "id": i,
            //         "itemName": dtViews[i].dataViewDispName,
            //         "dataName": dtViews[i].id
            //     };
            //     this.srcViews.push(newView);
            // }
            // this.updateSelDataview();
        },
            (res: Response) => {
                this.onError('Failed to load Data Views list!')
            });
    }

    // updateSelDataview(){
    //     this.srcViews.forEach((element) => {
    //         if(element.dataName==this.reportInfo.trgtViewId){
    //             this.reportInfo.trgtViewId=element.dataName;
    //             // this.selectedviewTemp=[];
    //             // this.selectedviewTemp.push(element);
    //         }
    //     });
    // }
    // trgtViewChanged(){
    //     this.reportInfo.trgtViewId=this.selectedviewTemp[0].dataName;
    //     $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
    // }

    // To get report request list
    getRequestsList(requestType?:string, sortCol?:string, sortOrder?:string) {
        if (!this.selectedReportId || this.selectedReportId == undefined) {
            this.onError('Please select a valid report');
            return;
        }
        this.requestType=requestType;
        this.blockUI=true;
        this.reportsService.getReportReqList(this.selectedReportId, {
            page: this.reqListpageEvent.pageIndex,
            size: this.reqListpageEvent.pageSize
        },this.requestType,sortCol,sortOrder).takeUntil(this.unsubscribe).subscribe((res: Response) => {
            this.reportReqList = res.json();
            if (this.reportReqList.length > 0) {
                if (this.selectedReqId) {
                    for (let i = 0; i < this.reportReqList.length; i++) {
                        if (this.reportReqList[i].reqId == this.selectedReqId) {
                            this.reqName=res.json()[i].reqName;
                            this.rptName=res.json()[i].reqName;
                            this.rprtDate=this.datepipe.transform(res.json()[i].createdDate, 'medium').toString();
                            this.reportInfo = jQuery.extend(true, {}, res.json()[i].filterMap);
                            this.reportInfo.dateTimeVal = new Date(this.reportInfo.dateTimeVal.split('T')[0]);
                            if(this.urlWithRequestId){
                                if(this.reportInfo.outputType!=='CHART'){
                                    this.multiSelRows=this.reportInfo.groupingCols;
                                    this.multiSelCols=this.reportInfo.columnCols;
                                    if(this.reportInfo.columnCols && this.reportInfo.columnCols.length>0){
                                        this.singleSelCol=this.reportInfo.columnCols[0];
                                    }
                                    if(this.reportInfo.valueCols && this.reportInfo.valueCols.length>0){
                                        this.rptValCol=this.reportInfo.valueCols[0];
                                    }
                                }else{
                                    this.singleSelCol=this.reportInfo.groupingCols[0];
                                    if(this.reportInfo.chartType=='PIE'){
                                        this.rptValCol=this.reportInfo.valueCols[0];
                                    }else{
                                        this.multiSelRows=this.reportInfo.valueCols;
                                    }
                                }
                                this.viewReportReqOutput(this.selectedReqId, this.reportInfo.outputType);
                                this.urlWithRequestId=false;
                            }
                        }
                    }
                } else {
                    this.reportInfo = jQuery.extend(true, {}, res.json()[0].filterMap);
                    this.reportInfo.dateTimeVal = new Date(this.reportInfo.dateTimeVal.split('T')[0]);;
                }
                this.bindLOVs();
                this.requestsLength = +res.headers.get('x-count');
            }else {
                this.reportReqList=[];
            }
            this.blockUI=false;
        },
            (res: Response) => {
                this.onError('Failed to load Report Requests list!');
                this.reportSelected(this.selectedReportId);
                this.blockUI=false;
            })
    }

    viewRequestOutput(row) {
        this.columnFilters=[];
        this.reportInfo = jQuery.extend(true, {}, row.filterMap);
        this.reportInfo.dateTimeVal = new Date(this.reportInfo.dateTimeVal.split('T')[0]);
        this.rprtDate=this.datepipe.transform(row.createdDate, 'medium').toString();
        this.selectedReqId = row.reqId;
        this.gotopage=1;
        this.pageEvent.pageIndex = 0;
        this.viewReportReqOutput(row.reqId, row.filterMap.outputType);
    }

    public ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.cs.destroyNotification(); 
        // window.removeEventListener('scroll', this.scroll, true);
    }

    onPvtFieldsSelected(source, destination: any[]) {
        if (source instanceof Array) {
            this.allVarcharCols.forEach((element) => {
                if (source.indexOf(element) >= 0 && destination.indexOf(element) >= 0) {
                    const ind = destination.indexOf(element);
                    destination.splice(ind, 1);
                } else if (destination.indexOf(element) < 0) {
                    destination.push(element);
                }
            });
        } else {
            this.allVarcharCols.forEach((element) => {
                if (source == element && destination.indexOf(element) >= 0) {
                    const ind = destination.indexOf(element);
                    destination.splice(ind, 1);
                } else if (destination.indexOf(element) < 0) {
                    destination.push(element);
                }
            });
        }
    }

    checkColumnExistence(source, destination) {
        if (!destination){
            return;
        }
        if (source instanceof Array) {
            if (destination instanceof Array) {
                for (let i = 0, srcLen = source.length; i < srcLen; i++) {
                    for (let j = 0, destLen = destination.length; j < destLen; j++) {
                        if (source[i].id == destination[j].id) {
                            source.splice(i, 1);
                            srcLen = source.length;
                            break;
                        }
                    }
                }
            } else {
                for (let i = 0, srcLen = source.length; i < srcLen; i++) {
                    if (source[i].id == destination.id) {
                        source = undefined;
                        break;
                    }
                }
            }
        } else {
            if (destination instanceof Array) {
                for (let j = 0, destLen = destination.length; j < destLen; j++) {
                    if (source.id == destination[j].id) {
                        source = undefined;
                        break;
                    }
                }
            } else {
                if (source.id == destination.id) {
                    source = undefined;
                }
            }
        }
    }

    sortReportTable(reqId, event) {
        this.sortOrder = 'ascending';
        if (event.order < 1){
            this.sortOrder = 'descending';
        }
        this.sortCol = event.field;
        this.selectedReqId-reqId;
        this.updatedReportOutput();
    }

    updatedPagination(){
        if(this.gotopage>0&&((this.gotopage*this.pageEvent.pageSize)<=this.length)){
            this.pageEvent.pageIndex=this.gotopage-1;
            this.updatedReportOutput();
        }else{
            this.cs.info('Oops...!','You are requesting invalid page!');
        }
    }

    updatedReportOutput() {
        this.blockUI=true;
        this.gotopage=this.pageEvent.pageIndex+1;
        this.reportsService.getReportOutputByReqId(this.selectedReqId,'table', this.pageEvent.pageIndex, this.pageEvent.pageSize,this.columnFilters, this.sortOrder, this.sortCol, this.searchStr).takeUntil(this.unsubscribe).subscribe((res: Response) => {
            this.reportResult = res.json().data;
            this.showReportOutput = true;
            this.length = +res.headers.get('x-count');
            if(this.length==0){
                this.cs.info('Oops!', 'No data available, Update parameters and re-run');
            }
            this.showChart = false;
            this.showPivot = false;
            this.showTabular = true;
            this.showChangeView = false;
            this.showParameters = false;
            this.blockUI=false;
            this.showProcessing = false;
            clearTimeout(this.timer);
        },
        (res: Response) => {
            this.blockUI=false;
            this.onError('Report request failed');
        });
    }

    tabularReportPresentation(res: Response) {
        this.reportResult = res.json().data;
        this.reportsTableColumns = res.json().columns;
            for (let i = 0, cnt = res.json().columns.length; i < cnt; i++) {
                this.columnOptions.push({ label: res.json().columns[i]['header'], value: res.json().columns[i] });
            }
        this.showReportOutput = true;
        this.length = +res.headers.get('x-count');
        if(this.length==0){
            this.cs.info('Oops!', 'No data available, Update parameters and re-run');
        }
        this.showChart = false;
        this.showPivot = false;
        this.showTabular = true;
        this.showChangeView = false;
        this.showParameters = false;
        this.blockUI=false;
        this.showProcessing = false;
        clearTimeout(this.timer);
    }
    
    displayCancelButton(){
        this.timer=setTimeout(()=>{ 
            if(this.blockUI){
                this.showProcessing = true;
            }
        }, 4000);
    }

    viewReportReqOutput(reqId, outputType: string) {
        this.fnlObj = jQuery.extend(true, {}, this.reportInfo);
        if(this.reportInfo.additionalParams){
            this.adnlParametersSubmited=this.reportInfo.additionalParams;
        }
        if(this.reportInfo.reportConditionsList){
            this.reportInfo.reportConditionsList.forEach((element) => {
                this.adnlParametersSubmited.push({Parameter:element.displayName+' ('+(element.conditionalOperator.toString())+')',Value:element.conditionalVal});
            });
        }
        
        this.displayCancelButton();
        this.blockUI=true;
        this.gotopage=this.pageEvent.pageIndex+1;
        if (this.reportInfo.outputType == 'TABLE') {
            this.request = this.reportsService.getReportOutputByReqId(reqId,'table', this.pageEvent.pageIndex, this.pageEvent.pageSize ,this.columnFilters).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                this.showRequestsList=false;
                this.tabularReportPresentation(res);
                this.buildParameters(this.fnlObj,Number(reqId),false);
            },(res)=>{
                this.blockUI=false;
                this.showProcessing = false;
                this.expandParamsHeader=true;
                clearTimeout(this.timer);
                this.cs.error('Oops...!','Data not loaded try again!');
            });
        } else if (this.reportInfo.outputType == 'PIVOT') {
            //***************** Building Pivot *****************
            this.showPivot = true;
            this.showReportOutput=true;
            this.fnlObj.columnCols = [];                                    //Reset all arrays
            this.fnlObj.valueCols = [];
            this.fnlObj.groupingCols = [];
            const pvtRows = [];
            let pvtCols = [];
            const pvtVals = [];
            this.fnlObj.groupingCols = this.multiSelRows;
            this.fnlObj.columnCols=this.multiSelCols;
            this.fnlObj.valueCols.push(this.rptValCol);
            this.fnlObj.groupingCols.forEach((selCol) => {                           //rows section selected columns
                pvtRows.push(selCol.itemName);
            });
            let degits=2;

            if (this.reportInfo.fields[0].repType == 'AGING_REPORT') {
                pvtCols.push("Buckets");
                if(this.fnlObj.aggregator){
                    const newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                    if(newA.layoutDisplayName){
                        pvtVals.push(newA.layoutDisplayName);
                    }
                }
                this.reportInfo.outputCols.forEach((eachitem)=>{
                    if(eachitem.columnType && eachitem.dataType){
                        if(eachitem.columnType=='AGING' && eachitem.dataType=='INTEGER'){
                            degits=0;
                        }
                    }
                });

            } else if (this.reportInfo.fields[0].repType == 'ROLL_BACK_REPORT') {
                pvtCols.push("Months");
                if(this.fnlObj.aggregator){
                    const newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                    if(newA.layoutDisplayName){
                        pvtVals.push(newA.layoutDisplayName);
                    }
                }
            } else {
                if(this.fnlObj.columnCols && this.fnlObj.columnCols.length>0){
                    this.fnlObj.columnCols.forEach((element) => {
                        pvtCols.push(element.itemName);
                    });
                }
                if(this.fnlObj.valueCols && this.fnlObj.valueCols.length>0){
                    pvtVals.push(this.fnlObj.valueCols[0].itemName);                //values section selected columns for local use
                }
            }
            //**********************************************
            this.request = this.reportsService.getReportOutputByReqId(reqId,'pivot', 0, 0, this.columnFilters,).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                this.pvtReportResult = res.json().data;
                if(this.pvtReportResult.length==0){
                    this.cs.info('Oops!', 'No data available, Update parameters and re-run');
                }else{
                    let render="Col Heatmap";
                    this.expandParamsHeader=true;
                    const derivers = $.pivotUtilities.derivers;
                    const utils = $.pivotUtilities;
                    const sum = $.pivotUtilities.aggregatorTemplates.sum;
                    const numberFormat = $.pivotUtilities.numberFormat;
                    const intFormat = numberFormat({ digitsAfterDecimal: degits });
                    this.buildParameters(this.fnlObj,Number(reqId),false);
                    
                    $("#pvtoutput").pivotUI(
                        this.pvtReportResult,
                        {
                            rows: pvtRows,
                            cols: pvtCols,
                            vals: pvtVals,
                            aggregator: sum(intFormat)(pvtVals),
                            aggregatorName: "Sum",
                            rendererOptions: {
                                table: {
                                    clickCallback:
                                        function(e, value, filters, pivotData) {
                                        }
                                }
                            },
                            renderers: $.extend(
                                $.pivotUtilities.renderers,
                                $.pivotUtilities.c3_renderers
                            ),
                            rendererName: render
                        }
                    );
                    $(".pvtUnused").prepend("<p class=\"no-mb pivot-available-columns-section-head\">Available Columns</p>");
                    $(".pvtRows").prepend("<p class=\"no-mb pivot-rows-section-head\">Rows Section</p>");
                    $(".pvtCols").prepend("<p class=\"colHead pivot-columns-section-head\">Columns Section</p>");
                    $(".pvtVals").prepend("<p class=\"no-mb pivot-aggregator-section-head\">Aggregator</p>");
                    $("#pvtoutput > table > tr:nth-child(1) > td:nth-child(1)").prepend("<p class=\"no-mb pivot-viewAs-section-head\">View As</p>");
                    this.showPvtOptns = false;
                    this.showParameters = false;
                    this.showReportOutput = true;
                    this.showTabular = false;
                    this.showChart = false;
                    this.showPivot = true;
                }
                this.showChangeView = false;
                this.blockUI=false;
                this.showProcessing = false;
                clearTimeout(this.timer);
                
            },(res)=>{
                this.blockUI=false;
                this.showProcessing = false;
                clearTimeout(this.timer);
                this.cs.error('Oops...!','Data not loaded try again!');
            });
        }else if(this.reportInfo.outputType=='CHART'){
            //***************** Building Chart *****************
            this.showChart = false;
            this.showReportOutput=true;
            let chartLabel=this.singleSelCol.itemName;
            let chartValue=[];
            if (this.reportInfo.fields[0].repType == 'AGING_REPORT' || this.reportInfo.fields[0].repType == 'ROLL_BACK_REPORT') {
                if(this.fnlObj.aggregator){
                    const newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                    if(newA.layoutDisplayName){
                        chartValue.push(newA.layoutDisplayName);
                    }
                }
            }else{
                if(this.reportInfo.chartType=='PIE'){
                    chartValue.push(this.rptValCol.itemName);
                }else{
                    this.multiSelRows.forEach((item)=>{
                        chartValue.push(item.itemName);
                    })
                }
            }
            //**********************************************

            this.request = this.reportsService.getReportOutputByReqId(reqId,'table', 0, 0, this.columnFilters,).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                let result = res.json().data;
                
                this.chartType=this.reportInfo.chartType.toLowerCase();
                if(result.length==0){
                    this.cs.info('Oops!', 'No data available, Update parameters and re-run');
                }else{
                    this.expandParamsHeader=true;
                    const obj = <any>Object;
                    this.chartData=[];
                    this.chartLabels=[];
                    if(this.reportInfo.chartType=='PIE'){
                        this.chartResult = obj.values(
                            result.reduce((a, c) => (
                              a[c[chartLabel]] = a[c[chartLabel]] ?
                              (a[c[chartLabel]][chartValue[0]] += c[chartValue[0]], a[c[chartLabel]]) :
                              c, a), {}
                            )
                          );
                        for(let j=0;j<this.chartResult.length;j++){
                            this.chartData.push(Math.round(this.chartResult[j][chartValue[0]]));
                            this.chartLabels.push(this.chartResult[j][chartLabel]);
                        }
                    }else{
                        for(let k=0;k<chartValue.length;k++){
                            this.chartResult = [];
                            this.chartResult = obj.values(
                                result.reduce((a, c) => (
                                    a[c[chartLabel]] = a[c[chartLabel]] ?
                                        (a[c[chartLabel]][chartValue[k]] += c[chartValue[k]], a[c[chartLabel]]) :
                                        c, a), {}
                                )
                            );
                            if(this.chartResult.length>0){
                                this.chartData.push({ data: [], label: chartValue[k] });
                                for (let j = 0; j < this.chartResult.length; j++) {
                                    this.chartData[k].data.push(Math.round(this.chartResult[j][chartValue[k]]));
                                    if(this.chartLabels.indexOf(this.chartResult[j][chartLabel])<0){
                                        this.chartLabels.push(this.chartResult[j][chartLabel]);
                                    }
                                }
                            }
                        }
                    }
                    this.buildParameters(this.fnlObj,Number(reqId),false);
                    this.showReportOutput = true;
                    this.showTabular = false;
                    this.showChart = true;
                    this.showPivot = false;
                }
                this.showChangeView = false;
                this.showParameters = false;
                this.blockUI=false;
                this.showProcessing = false;
                clearTimeout(this.timer);
            },(res)=>{
                this.blockUI=false;
                this.showProcessing = false;
                clearTimeout(this.timer);
                this.cs.error('Oops...!','Data not loaded try again!');
            });

        }else {
            this.cs.info('Oops...!','Invalid Output Type Selection!');
            return;
        }
    }

    reRunAsOfNow(filterMap, reqId) {
        this.reportInfo = filterMap;
        this.reportInfo.dateTimeVal = new Date();
        this.rprtDate=this.datepipe.transform(new Date(), 'medium').toString();
        this.selectedReqId = reqId;
        this.runReport(true,true);
    }

    updateParameters(reqId,rptId){

    }

    checkIfExists(arr, key, Val) {
        return arr.some(function(e) {
            return e[key] ? e[key] === Val : false;
        });
    }

    reconcileAccStatusChange(isFromAcc:Boolean){
        if(this.reportInfo.reportTypeCode!='ACCOUNT_ANALYSIS_REPORT' ){
            if(this.reportInfo.reconcile=='Both' && this.reportInfo.accVal=='Both'){
                if(isFromAcc){
                    this.reportInfo.reconcile='RECONCILED';
                }else{
                    this.reportInfo.accVal='ACCOUNTED';
                }
            }
            if(this.reportInfo.reconcile=='Both'){
                this.disableAccStatusAll=true;
            }else{
                this.disableAccStatusAll=false;
            }
            if(this.reportInfo.accVal=='Both'){
                this.disableRecStatusAll=true;
            }else{
                this.disableRecStatusAll=false;
            }
        }
    }
    
    //It will work when report selection is updated
    reportSelected(reportId: any) {
        this.sortOrder = '';
        this.sortCol = '';
        this.searchStr = '';
        this.reportInfo = new ReportColsInfo();
        this.disableRecStatus = true;
        this.disableAccStatus = true;
        this.blockUI=true;
        this.reportsService.getReportFieldsInfo(reportId).takeUntil(this.unsubscribe).subscribe((res: ReportColsInfo) => {      //To load selected report definition
            this.reportInfo = res;
            if(this.reportInfo.reportTypeCode&&(this.reportInfo.reportTypeCode=='AGING_REPORT'||this.reportInfo.reportTypeCode=='ROLL_BACK_REPORT')){
                this.reportInfo.categorizeBy='RECONCILIATION';
            }
            this.reportInfo.dateTimeVal = this.today;                     //Default report date is current date
            this.reportInfo.outputType = 'TABLE';                         //Default report output type
            const tempVar = res;
            this.showParameters = true;
            if (this.reportInfo.accVal && this.reportInfo.accVal == 'Both'){
                this.disableAccStatus = false;
            }
            if (this.reportInfo.reconcile && this.reportInfo.reconcile == 'Both'){
                this.disableRecStatus = false;
            }
            if(this.reportInfo.reportTypeCode!='ACCOUNT_ANALYSIS_REPORT'){
                if((this.reportInfo.accVal && this.reportInfo.accVal == 'Both')&&
                (this.reportInfo.reconcile && this.reportInfo.reconcile == 'Both')){
                    if (this.reportInfo.accVal && this.reportInfo.accVal == 'Both'){
                        this.disableRecStatusAll=true;
                        this.reportInfo.reconcile='RECONCILED';
                    }
                    if (this.reportInfo.reconcile && this.reportInfo.reconcile == 'Both'){
                        this.disableAccStatusAll=true;
                        this.reportInfo.accVal='ACCOUNTED';
                    }
                }
            }
            // this.updateSelDataview();
            this.blockUI=false;
            if(tempVar.fields[0].repType == 'ACCOUNT_ANALYSIS_REPORT'||tempVar.fields[0].repType == 'ACCOUNT_BALANCE_REPORT'){
                if(tempVar.segmentsCount && tempVar.segmentsCount>0){
                    this.bindFieldControlsInfo(tempVar,reportId);
                }else{
                    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
                        data: { message: "Accounting not done for selected Data Source. \nDo you want to continue?", ok: 'ok', cancel: 'no', okLabel: 'Yes', cancelLabel: 'No' }
                    });
                    dialogRef.afterClosed().subscribe((result) => {
                        if (result && result == 'ok') {
                            this.bindFieldControlsInfo(tempVar,reportId);
                        }else {
                            this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
                        }
                    });
                }
            }else{
                this.bindFieldControlsInfo(tempVar,reportId);
            }
        },
            (res: Response) => {
                this.reportInfo.fields = [];
                this.blockUI=false;
                this.onError('Report definition failed to load!');
            });
    }

    bindFieldControlsInfo(tempVar: ReportColsInfo, reportId: any){
        if(tempVar.segmentInfo){
            for(let i=0;i<tempVar.segmentInfo.length;i++){
                this.reportInfo.segmentInfo[i].flexValues1=[];
                for(let j=0, len=tempVar.segmentInfo[i].flexValues.length;j<len;j++){
                    this.reportInfo.segmentInfo[i].flexValues1.push({
                        "id": j,
                        "itemName": tempVar.segmentInfo[i].flexValues[j],
                        "dataName": tempVar.segmentInfo[i].flexValues[j].split('-')[0]
                    });
                }
                this.reportInfo.segmentInfo[i].flexValues=[];
                this.reportInfo.segmentInfo[i].selFlexValues1=[];
            }
        }
        for (let i = 0; i < tempVar.fields.length; i++) {
            if (tempVar.fields[i].fieldType === 'MULTI_SELECT_LOV' || tempVar.fields[i].fieldType === 'SINGLE_SELECT_LOV' || tempVar.fields[i].fieldType === 'SINGLE_SELECTION') {
                this.reportsService.getReportParamLovsByParamId(reportId, this.reportInfo.fields[i].rParamId).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    const ind = res.index;
                    this.reportInfo.fields[ind].fieldValuesList =[];
                    this.reportInfo.fields[ind].fieldValuesList = res.fieldValuesList;
                    let selVals = [];
                    this.parametersSubmited.forEach((element) => {
                        if (element.ParamId == this.reportInfo.fields[ind].rParamId) {
                            selVals = element.Value;
                        }
                    });
                    if (this.reportInfo.fields[ind].fieldType === 'MULTI_SELECT_LOV') {
                        this.reportInfo.fields[ind].selectedValue = [];
                        const newValueList=[];
                        for (let j = 0; j < this.reportInfo.fields[ind].fieldValuesList.length; j++) {
                            const newVal={
                                "id": j,
                                "itemName": this.reportInfo.fields[ind].fieldValuesList[j],
                                "dataName": this.reportInfo.fields[ind].fieldValuesList[j]
                            }
                            newValueList.push(newVal);
                            if(selVals.indexOf(this.reportInfo.fields[ind].fieldValuesList[j])>-1){
                                this.reportInfo.fields[ind].selectedValue.push(newVal);
                            }
                        }
                        this.reportInfo.fields[ind].fieldValuesList = newValueList;
                        if(this.reportInfo.fields[ind].isMandatory&&newValueList.length==1){
                            this.reportInfo.fields[ind].selectedValue=[];
                            this.reportInfo.fields[ind].selectedValue=[...newValueList];
                        }
                    }else{
                        this.reportInfo.fields[ind].selectedValue = selVals;
                        if(this.reportInfo.fields[ind].isMandatory&&this.reportInfo.fields[ind].fieldValuesList.length==1){
                            this.reportInfo.fields[ind].selectedValue=this.reportInfo.fields[ind].fieldValuesList[0];
                        }else{
                            this.reportInfo.fields[ind].fieldValuesList.unshift(undefined);
                        }
                    }
                },
                    (res: Response) => {
                        this.onError('Failed to load LOV values for ' + this.reportInfo.fields[i].fieldName);
                    });
            } else if (tempVar.fields[i].fieldType === 'DATE_RANGE') {
                tempVar.fields[i].selectedValue = { 'fromDate': undefined, 'toDate': new Date() };
                this.parametersSubmited.forEach((element) => {
                    if(element.ParamId==this.reportInfo.fields[i].rParamId){
                        tempVar.fields[i].selectedValue = new Date(element.Value);
                    }
                });
            }else if (tempVar.fields[i].fieldType === 'MONTH_RANGE') {
                tempVar.fields[i].selectedValue = { 'fromDate': undefined, 'toDate': new Date() };
            }
            this.parametersSubmited.forEach((ele)=>{        //if user is going to update parameter values
                if(ele.Parameter=='Report Date'){
                    this.reportInfo.dateTimeVal=new Date(ele.Value);
                }
            })
        }
        this.bindLOVs();
    }

    bindLOVs() {
        if(!this.reportInfo.outputCols){
            return;
        }
        this.allDecimalCols=[];
        this.allVarcharCols=[];
        for (let i = 0; i < this.reportInfo.outputCols.length; i++) {
            if (this.reportInfo.outputCols[i] && this.reportInfo.outputCols[i] !== null) {
                const newObj={
                    "id": this.reportInfo.outputCols[i].ColumnId,
                    "itemName": this.reportInfo.outputCols[i].layoutDisplayName,
                    "dataName": this.reportInfo.outputCols[i].layoutDisplayName,
                    "dataType": this.reportInfo.outputCols[i].dataType
                };
                if (this.reportInfo.outputCols[i].dataType == 'DECIMAL' || this.reportInfo.outputCols[i].dataType == 'AGGREGATOR' || this.reportInfo.outputCols[i].dataType == 'INTEGER') {
                    this.allDecimalCols.push(newObj);
                    if(this.rptValCol){
                        if(this.rptValCol.dataName==newObj.dataName&&this.rptValCol.id==newObj.id){
                            this.rptValCol=newObj;
                        }
                    }
                } else {
                    this.allVarcharCols.push(newObj);
                    if(this.singleSelCol){
                        if(this.singleSelCol.dataName==newObj.dataName&&this.singleSelCol.id==newObj.id){
                            this.singleSelCol=newObj;
                        }
                    }
                }
            }
        }
        this.pvtRowsEligibleCols = [...this.allVarcharCols];
        this.pvtColsEligibleCols = [...this.allVarcharCols];
    }

    //It will work if output type is updated (need to update with latest changes)
    outputTypeChanged() {
        this.multiSelRows = [];
        this.multiSelCols=[];
        this.singleSelCol = undefined;
        this.rptValCol = undefined;
        if (this.reportInfo.outputType == 'TABLE') {
            return;
        }else if(this.reportInfo.outputType == 'CHART'){
            this.reportInfo.chartType='BAR';
        }
        this.showChangeView = true;
    }

    selectData(event: any) {     //For chart 
    }
    testMe() {
        console.log(window.location.href);
    }

    removeLovSrcList(){
        this.fnlObj = jQuery.extend(true, {}, this.reportInfo);                         //Creating new object for report info
        this.fnlObj.fields.forEach((item) => {                                            //Gathering all selected values for multi select LOV
            if (item.fieldType == 'MULTI_SELECT_LOV') {
                if(item.selectedValue&&item.selectedValue[0]&&typeof item.selectedValue[0] === 'object' && item.selectedValue[0].constructor === Object){
                    const newValues = [];
                    item.selectedValue.forEach((val) => {
                        newValues.push(val.dataName);
                    });
                    item.selectedValue = newValues;
                }
            }
            item.fieldValuesList = [];
        });
        if(this.fnlObj.segmentInfo){
            for(let i=0;i<this.fnlObj.segmentInfo.length;i++){
                this.fnlObj.segmentInfo[i].selFlexValues=[];
                for(let j=0, len=this.fnlObj.segmentInfo[i].selFlexValues1.length;j<len;j++){
                    this.fnlObj.segmentInfo[i].selFlexValues.push(this.fnlObj.segmentInfo[i].selFlexValues1[j].dataName.split('-')[0]);
                }
                this.fnlObj.segmentInfo[i].flexValues1=[];
                this.fnlObj.segmentInfo[i].selFlexValues1=[];
            }
        }
        this.columnOptions = [];
        this.rptSubmitted = true;
        if(this.reportInfo.reportTypeCode=='ROLL_BACK_REPORT'){
            this.fnlObj.dateTimeVal=new Date(this.fnlObj.dateTimeVal);
        }else if (this.fnlObj.dateTimeVal != this.today) {
            this.fnlObj.dateTimeVal.setDate(this.fnlObj.dateTimeVal.getDate() + 1);
        }
    }
    //It will work if user click on submit
    runReport(resetPageNum: boolean, isRefresh?: boolean) {
        this.reportsService.testOozieServer().takeUntil(this.unsubscribe).subscribe((oozieStatus: OozieStatus) => {
            if (oozieStatus && oozieStatus.dbStatus && oozieStatus.ooziestatus) {
                const dialogRef = this.dialog.open(ConfirmDialogComponent, {
                    data: { message: "Do you want to submit report as job request?", ok: 'ok', cancel: 'no', okLabel: 'Yes', cancelLabel: 'No, I will wait' }
                });
                dialogRef.afterClosed().subscribe((result) => {
                    if (!result) {
                        return;
                    }
                    if (isRefresh){
                        this.gotopage=1;
                        this.pageEvent.pageIndex = 0;
                    }
                    this.removeLovSrcList();
                    if (result != 'ok') {
                        this.displayCancelButton();
                    }
                    this.blockUI = true;
                    this.sysReqName=this.selectedReport.reportName+(new Date()).getTime();
                    switch (this.fnlObj.outputType) {
                        case 'TABLE':
                            this.fnlObj.outputCols = [];
                            this.showPivot = false;
                            this.request = this.reportsService.getReportOutputFirstTime(this.sysReqName,this.selectedReportId,this.reqName, this.fnlObj,
                                result, {
                                    page: this.pageEvent.pageIndex,
                                    size: this.pageEvent.pageSize
                                }).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                                    this.rptSubmitted = false;
                                    this.blockUI = false;
                                    if (result == 'ok') {
                                        this.showChangeView = false;
                                        this.showParameters = false;
                                        this.cs.success('Submitted', 'You will get a notification once job done!');
                                    } else {
                                        this.showProcessing = false;
                                        clearTimeout(this.timer);
                                        if(!res.json().requestInfo || !res.json().requestInfo.idForDisplay){
                                            this.showProcessing=false;
                                            this.rptSubmitted=false;
                                            clearTimeout(this.timer);
                                            this.blockUI = false;
                                            this.cs.error('Sorry!', 'Request failed to run');
                                            return;
                                        }
                                        this.expandParamsHeader=true;
                                        this.reqName=res.json().requestInfo.reqName;
                                        this.rptName=res.json().requestInfo.reqName;
                                        this.selectedReqId = res.json().requestInfo.idForDisplay;
                                        this.tabularReportPresentation(res);
                                        if (!isRefresh){
                                            if(res.json().additionalParams){
                                                this.adnlParametersSubmited=res.json().additionalParams;
                                            }
                                            if(res.json().reportConditionsList){
                                                res.json().reportConditionsList.forEach((element) => {
                                                    this.adnlParametersSubmited.push({Parameter:element.displayName+' ('+(element.conditionalOperator.toString())+')',Value:element.conditionalVal});
                                                });
                                            }
                                        }
                                        this.buildParameters(this.fnlObj,Number(res.json().requestInfo.id),false);
                                    }
                                }, (res: Response) => {
                                    this.blockUI = false;
                                    this.rptSubmitted = false;
                                    this.showChangeView = false;
                                    this.showProcessing = false;
                                    clearTimeout(this.timer);
                                    this.cs.error('Sorry!', 'Request failed to run');
                                });
        
                            break;
                        case 'PIVOT':
                            if (this.multiSelRows.length < 1) {
                                this.cs.info(
                                    'Sorry!',
                                    'Please select atleast one column as pivot rows'
                                );
                                this.showProcessing = false;
                                this.rptSubmitted=false;
                                clearTimeout(this.timer);
                                this.blockUI = false;
                                return;
                            }
                            if ((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && this.multiSelCols.length<1) {
                                this.cs.info(
                                    'Sorry!',
                                    'Please select atleast one column as pivot columns'
                                );
                                this.showProcessing = false;
                                this.rptSubmitted=false;
                                clearTimeout(this.timer);
                                this.blockUI = false;
                                return;
                            }
                            if ((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && !this.rptValCol) {
                                this.cs.info(
                                    'Sorry!',
                                    'Please select atleast one column as values'
                                );
                                this.showProcessing = false;
                                this.rptSubmitted=false;
                                clearTimeout(this.timer);
                                this.blockUI = false;
                                return;
                            }
                            this.fnlObj.columnCols = [];                                    //Reset all arrays
                            this.fnlObj.valueCols = [];
                            this.fnlObj.groupingCols = [];
                            const pvtRows = [];
                            let pvtCols = [];
                            const pvtVals = [];
                            this.fnlObj.groupingCols = this.multiSelRows;
                            this.fnlObj.columnCols=this.multiSelCols;
                            this.fnlObj.valueCols.push(this.rptValCol);
        
                            this.multiSelRows.forEach((selCol) => {                           //rows section selected columns
                                pvtRows.push(selCol.itemName);
                            });
                            let degits=2;
                            if (this.reportInfo.fields[0].repType == 'AGING_REPORT') {
                                pvtCols.push("Buckets");
                                const newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                                pvtVals.push(newA.layoutDisplayName);
                                this.reportInfo.outputCols.forEach((eachitem)=>{
                                    if(eachitem.columnType && eachitem.dataType){
                                        if(eachitem.columnType=='AGING' && eachitem.dataType=='INTEGER'){
                                            degits=0;
                                        }
                                    }
                                });
        
                            } else if (this.reportInfo.fields[0].repType == 'ROLL_BACK_REPORT') {
                                pvtCols.push("Months");
                                const newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                                pvtVals.push(newA.layoutDisplayName);
                            } else {
                                this.multiSelCols.forEach((selCol) => {                           //rows section selected columns
                                    pvtCols.push(selCol.itemName);
                                });
                                pvtVals.push(this.rptValCol.itemName);                //values section selected columns for local use
                            }
        
                            this.fnlObj.outputCols = [];
                            this.request = this.reportsService.getReportOutputFirstTime(this.sysReqName,this.selectedReportId,this.reqName, this.fnlObj, result).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                                this.rptSubmitted = false;
                                if (result == 'ok') {
                                    this.showChangeView = false;
                                    this.showParameters = false;
                                    this.cs.success('Submitted', 'You will get a notification once job done!');
                                    this.showProcessing = false;
                                    this.rptSubmitted=false;
                                    clearTimeout(this.timer);
                                    this.blockUI = false;
                                    return;
                                }
                                if(!res.json().requestInfo || !res.json().requestInfo.id){
                                    this.showProcessing=false;
                                    clearTimeout(this.timer);
                                    this.rptSubmitted=false;
                                    this.blockUI = false;
                                    this.cs.error('Sorry!', 'Request failed to run');
                                    return;
                                }
                                
                                if (res.json().length < 1) {
                                    this.cs.info(
                                        'Error!',
                                        'Data is not available for selected filters'
                                    );
                                    this.pvtReportResult = [];
                                    this.showProcessing = false;
                                    clearTimeout(this.timer);
                                    this.rptSubmitted=false;
                                    this.blockUI = false;
                                    return;
                                }
                                // if (this.showParameters) {
                                //     this.getRequestsList();
                                // }
                                this.expandParamsHeader=true;
                                this.showProcessing = false;
                                clearTimeout(this.timer);
                                this.showChangeView = false;
                                this.showParameters = false;
                                this.blockUI = false;
                                this.selectedReqId = res.json().requestInfo.idForDisplay;
                                if (!isRefresh){
                                    if(res.json().additionalParams){
                                        this.adnlParametersSubmited=res.json().additionalParams;
                                    }
                                    if(res.json().reportConditionsList){
                                        res.json().reportConditionsList.forEach((element) => {
                                            this.adnlParametersSubmited.push({Parameter:element.displayName+' ('+(element.conditionalOperator.toString())+')',Value:element.conditionalVal});
                                        });
                                    }
                                    this.buildParameters(this.fnlObj,Number(res.json().requestInfo.id),false);
                                }
                                this.pvtReportResult = res.json().data;
                                if(this.pvtReportResult.length==0){
                                    this.cs.info('Oops!', 'No data available');
                                }else{
                                    this.showReportOutput = true;                           //Reset helping boolean variables
                                    this.showTabular = false;
                                    this.showChart = false;
                                    this.showPivot = true;
                                    //***************** Building Pivot *****************
                                    const derivers = $.pivotUtilities.derivers;
                                    const utils = $.pivotUtilities;
                                    const sum = $.pivotUtilities.aggregatorTemplates.sum;
                                    const numberFormat = $.pivotUtilities.numberFormat;
                                    const intFormat = numberFormat({ digitsAfterDecimal: degits });
                                    let render = "Col Heatmap";
                                    $("#pvtoutput").pivotUI(
                                        this.pvtReportResult,
                                        {
                                            rows: pvtRows,
                                            cols: pvtCols,
                                            vals: pvtVals,
                                            aggregator: sum(intFormat)(pvtVals),
                                            aggregatorName: "Sum",
                                            rendererOptions: {
                                                table: {
                                                    clickCallback:
                                                        function(e, value, filters, pivotData) {
                                                        }
                                                }
                                            },
                                            renderers: $.extend(
                                                $.pivotUtilities.renderers,
                                                $.pivotUtilities.c3_renderers
                                            ),
                                            rendererName: render
                                        }
                                    );
                                    $(".pvtUnused").prepend("<p class=\"no-mb pivot-available-columns-section-head\">Available Columns</p>");
                                    $(".pvtRows").prepend("<p class=\"no-mb pivot-rows-section-head\">Rows Section</p>");
                                    $(".pvtCols").prepend("<p class=\"colHead pivot-columns-section-head\">Columns Section</p>");
                                    $(".pvtVals").prepend("<p class=\"no-mb pivot-aggregator-section-head\">Aggregator</p>");
                                    $("#pvtoutput > table > tr:nth-child(1) > td:nth-child(1)").prepend("<p class=\"no-mb pivot-viewAs-section-head\">View As</p>");
                                    //**********************************************
                                }
                            }, (res: Response) => {
                                this.rptSubmitted = false;
                                this.showChangeView = false;
                                this.blockUI = false;
                                this.cs.error('Sorry!', 'Request failed to run');
                            });
                            break;
                        case 'CHART':
                            if((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && !this.singleSelCol){
                                this.cs.info(
                                    'Sorry!',
                                    'Please select atleast one column as Chart Label'
                                );
                                this.showProcessing = false;
                                this.rptSubmitted=false;
                                clearTimeout(this.timer);
                                this.blockUI = false;
                                return;
                            }
                            if((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && ((this.reportInfo.chartType=='PIE'&&!this.rptValCol)||(this.reportInfo.chartType !== 'PIE' && this.multiSelRows.length<1))){
                                this.cs.info(
                                    'Sorry!',
                                    'Please select atleast one column as Chart Value'
                                );
                                this.showProcessing = false;
                                this.rptSubmitted=false;
                                clearTimeout(this.timer);
                                this.blockUI = false;
                                return;
                            }


                        //***************** Building Chart *****************
                            this.showChart = false;
                            this.showReportOutput=true;
                            let chartLabel=this.singleSelCol.itemName;
                            this.fnlObj.groupingCols=[this.singleSelCol];
                            let chartValue=[];
                            if (this.reportInfo.fields[0].repType == 'AGING_REPORT' || this.reportInfo.fields[0].repType == 'ROLL_BACK_REPORT') {
                                if(this.fnlObj.aggregator){
                                    const newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                                    if(newA.layoutDisplayName){
                                        chartValue=newA.layoutDisplayName;
                                    }
                                }
                            }else{
                                if(this.reportInfo.chartType=='PIE'){
                                    chartValue.push(this.rptValCol.itemName);
                                    this.fnlObj.valueCols=[this.rptValCol];
                                }else{
                                    this.fnlObj.valueCols=this.multiSelRows;
                                    this.multiSelRows.forEach((item)=>{
                                        chartValue.push(item.itemName);
                                    })
                                }
                            }
                            //**********************************************

                            this.request = this.reportsService.getReportOutputFirstTime(this.sysReqName,this.selectedReportId,this.reqName, this.fnlObj, result).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                                this.rptSubmitted = false;
                                if (result == 'ok') {
                                    this.showChangeView = false;
                                    this.showParameters = false;
                                    this.cs.success('Submitted', 'You will get a notification once job done!');
                                    this.showProcessing = false;
                                    this.rptSubmitted=false;
                                    clearTimeout(this.timer);
                                    this.blockUI = false;
                                    return;
                                }
                                if(!res.json().requestInfo || !res.json().requestInfo.id){
                                    this.showProcessing=false;
                                    clearTimeout(this.timer);
                                    this.rptSubmitted=false;
                                    this.blockUI = false;
                                    this.cs.error('Sorry!', 'Request failed to run');
                                    return;
                                }
                                
                                if (res.json().length < 1) {
                                    this.cs.info(
                                        'Error!',
                                        'Data is not available for selected filters'
                                    );
                                    this.chartData = [];
                                    this.showProcessing = false;
                                    clearTimeout(this.timer);
                                    this.rptSubmitted=false;
                                    this.blockUI = false;
                                    return;
                                }
                                // if (this.showParameters) {
                                //     this.getRequestsList();
                                // }
                                this.expandParamsHeader=true;
                                this.showProcessing = false;
                                clearTimeout(this.timer);
                                this.showChangeView = false;
                                this.showReportOutput = true;                           //Reset helping boolean variables
                                this.showTabular = false;
                                this.showChart = true;
                                this.showPivot = false;
                                this.showParameters = false;
                                this.blockUI = false;
                                this.selectedReqId = res.json().requestInfo.idForDisplay;
                                let resultData = res.json().data;
                                this.chartType=this.reportInfo.chartType.toLowerCase();
                                if(resultData.length==0){
                                    this.cs.info('Oops!', 'No data available, Update parameters and re-run');
                                }else{
                                    if (!isRefresh){
                                        if(res.json().additionalParams){
                                            this.adnlParametersSubmited=res.json().additionalParams;
                                        }
                                        if(res.json().reportConditionsList){
                                            res.json().reportConditionsList.forEach((element) => {
                                                this.adnlParametersSubmited.push({Parameter:element.displayName+' ('+(element.conditionalOperator.toString())+')',Value:element.conditionalVal});
                                            });
                                        }
                                        this.buildParameters(this.fnlObj,Number(res.json().requestInfo.id),false);
                                    }
                                    
    
                                    const obj = <any>Object;
                                    this.chartData=[];
                                    this.chartLabels=[];
                                    if(this.reportInfo.chartType=='PIE'){
                                        this.chartResult = obj.values(
                                            resultData.reduce((a, c) => (
                                            a[c[chartLabel]] = a[c[chartLabel]] ?
                                            (a[c[chartLabel]][chartValue[0]] += c[chartValue[0]], a[c[chartLabel]]) :
                                            c, a), {}
                                            )
                                        );
                                        for(let j=0;j<this.chartResult.length;j++){
                                            this.chartData.push(Math.round(this.chartResult[j][chartValue[0]]));
                                            this.chartLabels.push(this.chartResult[j][chartLabel]);
                                        }
                                    }else{
                                        for(let k=0;k<chartValue.length;k++){
                                            this.chartResult = [];
                                            this.chartResult = obj.values(
                                                resultData.reduce((a, c) => (
                                                    a[c[chartLabel]] = a[c[chartLabel]] ?
                                                        (a[c[chartLabel]][chartValue[k]] += c[chartValue[k]], a[c[chartLabel]]) :
                                                        c, a), {}
                                                )
                                            );
                                            if(this.chartResult.length>0){
                                                this.chartData.push({ data: [], label: chartValue[k] });
                                                for (let j = 0; j < this.chartResult.length; j++) {
                                                    this.chartData[k].data.push(Math.round(this.chartResult[j][chartValue[k]]));
                                                    if(this.chartLabels.indexOf(this.chartResult[j][chartLabel])<0){
                                                        this.chartLabels.push(this.chartResult[j][chartLabel]);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    this.showReportOutput = true;
                                    this.showTabular = false;
                                    this.showChart = true;
                                    this.showPivot = false;
                                }
                                this.showChangeView = false;
                                this.showParameters = false;
                                this.blockUI=false;
                                this.showProcessing = false;
                            },(res)=>{
                                this.blockUI=false;
                                this.showProcessing = false;
                                clearTimeout(this.timer);
                                this.cs.error('Oops...!','Data not loaded try again!');
                            });

                        break;
                    }
                    
                });
                
            } else {
                this.cs.error('Sorry!', 'Request failed please try again!');
            }
        },(res: any)=>{
            this.cs.error('Sorry!', 'Request failed please try again!');
        });
    }
    
    ledgerChanged(ldgrName){
        if(ldgrName=='(Blank)'){
            this.cs.info('Sorry...!','Invalid selection');
            ldgrName=undefined;
            return;
        }
        this.reportsService.getPeriodsByLedgerName(ldgrName).takeUntil(this.unsubscribe).subscribe((res)=>{
            this.periodsList=res;
        });
    }

    onParametersHide(){
        if(!(this.showTabular || this.showPivot || this.displayScheduling || this.showChart)){
            this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
        }
    }

    buildParameters(fnlObj,reqId?:any, isnewReq?:boolean) {
        const destArray:ParametersSubmited[] = [];
        //#region binding report parameters to table object
        fnlObj.fields.forEach((element) => {
            if(element && element !== null && element.selectedValue.length>0){
                const newParam = new ParametersSubmited();
                newParam.Parameter = element.fieldName;
                newParam.ParamId=element.rParamId;
                newParam.Value = element.selectedValue;
                destArray.push(newParam);
            }
        });
        
        if(fnlObj.reconcile){
            const recParam = new ParametersSubmited();
            recParam.Parameter = 'Reconcile Status';
            if (fnlObj.reconcile === 'Both') {
                recParam.Value = 'All';
            } else {
                recParam.Value = fnlObj.reconcile;
            }
            destArray.push(recParam);
        }
        
        if(fnlObj.accVal){
            const accParam = new ParametersSubmited();
            accParam.Parameter="Accounted Status";
            if (fnlObj.accVal === 'Both') {
                accParam.Value = 'All';
            } else{
                accParam.Value = fnlObj.accVal;
            }
            destArray.push(accParam);
        }
        if(fnlObj.segmentInfo&&fnlObj.segmentInfo.length>0){
            for(let i=0;i<fnlObj.segmentInfo.length;i++){
                if(fnlObj.segmentInfo[i].selFlexValues.length>0){
                    const segParam = new ParametersSubmited();
                    segParam.ParamId=fnlObj.segmentInfo[i].segId;
                    segParam.Parameter=fnlObj.segmentInfo[i].segName;
                    segParam.Value=fnlObj.segmentInfo[i].selFlexValues;
                    destArray.push(segParam);
                }
            }
        }
        if(fnlObj.reportTypeCode=='TRAIL_BALANCE_REPORT'){
            const periodsParam=new ParametersSubmited();
            periodsParam.Parameter='Period';
            periodsParam.Value=fnlObj.selPeriod.periodName;
            destArray.push(periodsParam);
        }
        
        this.adnlParametersSubmited.forEach((item)=>{
            destArray.push(item);
        })
        if(fnlObj.dateTimeVal){
            const repDtParam=new ParametersSubmited();
            const newDate = new Date(fnlObj.dateTimeVal);
            if(fnlObj.reportTypeCode=='ROLL_BACK_REPORT'){
                repDtParam.Parameter="Period";
                let months=['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
                repDtParam.Value=months[newDate.getMonth()]+'-'+(newDate.getFullYear().toString()).substring(2, 4);
                destArray.push(repDtParam);
            }else if(fnlObj.reportTypeCode!=='TRAIL_BALANCE_REPORT'){
                repDtParam.Parameter="As Of Date";
                repDtParam.Value=this.datepipe.transform(newDate, 'mediumDate').toString();
                destArray.push(repDtParam);
            }
            
        }
        
        if(isnewReq){
            this.parametersSubmitedForTable=destArray;
        }else{
            this.parametersSubmited=destArray;
        }
    }

    hideDialog(event) {
        this.displayScheduling = event;
        this.showParameters=!event;
        this.rptSubmitted=false;
        // this.onParametersHide();
    }

    testMethod() {
        const dialogRef = this.dialog.open(ShareViaEmailDialog, {
            data: { reqId: this.selectedReqId, ok: 'ok', cancel: 'cancel' }
        });
    }

    downloadReport(type){
        this.displayCancelButton();
        this.blockUI=true;
        this.reportsService.downloadReportOutput(this.selectedReqId,type,this.rptName).takeUntil(this.unsubscribe).subscribe((res:any) => {
            this.showProcessing=false;
            clearTimeout(this.timer);
            this.blockUI=false;
        },(res: Response)=>{
            this.showProcessing=false;
            clearTimeout(this.timer);
            this.blockUI=false;
        })
    }

    shareReport() {
        const dialogRef = this.dialog.open(ShareViaEmailDialog, {
            data: { reqId: this.selectedReqId,url:window.location.href, ok: 'ok', cancel: 'cancel' }
        });
    }

    initiateSchedulingProcess(){
        this.scheduleInitiated=true;
        this.reportSelected(this.selectedReportId);
    }

    displayScheduleWindow() {
        this.displayScheduling = true;
        this.removeLovSrcList();
        this.buildParameters(this.fnlObj);
        // this.showParameters=false;
        this.scheduleInitiated=false;
        this.schedParameters = [];
        this.schedParametersForDisplay=[];
        this.schedObj.jobName = this.rptName;
        this.schedObj.programName = 'Reporting';
        this.schedParameters.push(new Parameters());
        this.schedParameters[0].paramName = "Report Id";
        this.schedParameters[0].selectedValue = this.selectedReportId;
        this.schedParameters.push(new Parameters());
        this.schedParameters[1].paramName = "Parameters";
        this.schedParameters[1].selectedValue = JSON.stringify(this.fnlObj);
        this.schedParameters.push(new Parameters());
        this.schedParameters[2].paramName = "Page Number";
        this.schedParameters[2].selectedValue = 0;
        this.schedParameters.push(new Parameters());
        this.schedParameters[3].paramName = "Page Size";
        this.schedParameters[3].selectedValue = 1;
        this.schedParameters.push(new Parameters());
        this.schedParameters[4].paramName = "View Type";
        this.schedParameters[4].selectedValue = this.fnlObj.outputType;
        this.schedParameters.push(new Parameters());
        this.schedParameters[5].paramName = "Request Name";
        this.schedParameters[5].selectedValue = this.reqName;
        this.schedParametersForDisplay.push(new Parameters());
        this.schedParametersForDisplay[0].paramName = "Report Name";
        this.schedParametersForDisplay[0].selectedValue = this.rptName;
        this.parametersSubmited.forEach((item)=>{
            let newParameter=new Parameters();
            newParameter.paramName=item.Parameter;
            newParameter.selectedValue=item.Value;
            this.schedParametersForDisplay.push(newParameter);
        })
        this.schedObj.parameters = this.schedParameters;
        this.schedObj.displayParameters = this.schedParametersForDisplay;
        this.scheduling.ngOnInit();
    }

    //Show error messages
    private onError(errorMessage) {
        this.cs.error(
            'Error!',
            errorMessage
        )
    }
    
    reportColumnFilter(){
        this.columnFilters=[];
        this.pageEvent.pageIndex=0;
        // if(this.paginator){
        //     this.paginator.pageIndex=0;
        //     this.paginator.pageIndex=this.gotopage;
        // }
        this.reportsTableColumns.forEach((element) => {
            if(element.searchString&&element.searchString.length>0){
                this.columnFilters.push({searchColumn:element.field,searchString:element.searchString,dataType:element.dataType});
            }
        });
        this.updatedReportOutput();
    }

    checkFilter(val:String){
        if(val && val.length>0){
            this.reportColumnFilter();
        }
    }

    amountRestrict(code) {
        if (code > 31 && (code < 48 || code > 57)&&code!=46) {
            return false;
        }
        return true;
    }

    cancelRequest(){
        if(this.rptSubmitted){
            this.reportsService.cancelJob(this.sysReqName).takeUntil(this.unsubscribe).subscribe((res)=>{

            });
        }
        if(this.request){
            this.request.unsubscribe();
        }
        this.showProcessing=false;
        clearTimeout(this.timer);
        this.rptSubmitted = false;
        this.showChangeView = false;
        this.blockUI=false;
    }

    enableReqNameEdit(){
        this.reqNameEditBtn.nativeElement.focus();
    }

    onColumnRightClick(event: any){
        console.log('****************');
        console.log(event);
        return false;
    }

    showOrClearFilter(){
        this.showFilters=!this.showFilters;
        if(!this.showFilters){
            this.columnFilters=[];
            this.reportsTableColumns.forEach((element) => {
                element.searchString=undefined;
            });
            this.pageEvent.pageIndex=0;
            this.updatedReportOutput();
        }
    }
}
