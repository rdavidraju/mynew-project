//#region Import Statments
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Response } from '@angular/http';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService } from 'ng-jhipster';
import { FormGroup, FormControl } from '@angular/forms';
import { NotificationsService } from 'angular2-notifications-lite';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { Reports, ReportColsInfo, ParametersSubmited, ReportRequestList,OozieStatus } from './reports.model';
import { ReportsService } from './reports.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ChartData } from '../../shared';
import { SchedulingModel, Parameters } from '../scheduling/scheduling.component';
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
import 'pivottable/dist/pivot.min.js';
import 'pivottable/dist/c3_renderers.js';
import Chart from 'chart.js';
import { fadeInContent } from '@angular/material/typings/select/select-animations';
declare var $: any;
declare var jQuery: any;
//#endregion
@Component({
    selector: 'jhi-run-reports',
    templateUrl: './run-reports.component.html'
})
export class RunReportsComponent implements OnDestroy, OnInit {
    //#region Variables Declarations
    private alive: boolean = true;      //Unsubscribe variable
    parentDiv123: any;
    selectedReport = new Reports();
    reportsList: Reports[] = [];          //To hold all reports list
    selectedReportId: number;           //To hold current selected report ID
    showReportOutput: boolean = false;  //To decide show/hide report output
    showTabular: boolean = false;       //To decide show/hide Tabular report output
    showPivot: boolean = false;         //To decide show/hide Pivot report output
    showChart: boolean = false;         //To decide show/hide chart report output    
    expandAdnlParams = true;           //To decide show/hide Additional parameters
    today = new Date();                   // It holds current date
    TemplatesHeight: any;
    cmpHeight:any;
    TemplatesWidth: any;
    sortCol: string = '';
    sortOrder: string = '';
    searchStr: string = '';
    limitSelectionSettings = {
        text: "You can select multiple",
        enableCheckAll: false,
        enableSearchFilter: true,
        badgeShowLimit: 2
    };

    limitFieldsSelectionSettings = {
        text: "You can select multiple",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: true,
        badgeShowLimit: 2
    };

    limitFieldsSelectionSettings1 = {
        text: "You can select multiple",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: true,
        badgeShowLimit: 1
    };

    multiSelectionSettingsCharts = {
        text: "You can select multiple",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: true,
        limitSelection: 1,
        badgeShowLimit: 2
    };

    reportsTableColumns = [];                   //Report table columns
    drilldownTableColumns = [];                 //Drilldown table columns
    columnOptions: any[] = [];                    //Column options for toggling
    reportInfo = new ReportColsInfo();          //To hold complete report definition to run
    reportResult: any[] = [];                     //Tabular report result
    pvtReportResult: any[] = [];                  //Pivot report result
    chartResult: ChartData = new ChartData();   //Chart report result
    pageSize: number = 0;
    pageSizeOptions = [10, 25, 50, 100];
    length: number;
    requestsLength: number = 0;
    pageEvent: PageEvent = new PageEvent();
    reportOutput: LookUpCode[] = [];             //To hold report output types
    chartTypes: LookUpCode[] = [];               //To hold chart types
    reportAllCols: Reports = new Reports();     //To hold report all columns
    reportReqList: ReportRequestList[] = [];
    multiSelCols: any[] = [];                     //To hold multi select columns for report
    multiSelRows: any[] = [];                     //To hold multi select rows for report
    singleSelCol: any;                          //To hold single column for report
    rptValCol: any;                             //To hold value column for report
    chartType: string;                          //To hold chart type
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
    prevReqId:number=0;
    blockUI: boolean = false;
    selectedTab:number=0;
    displayParameters: boolean;
    expandParamsHeader:boolean = true;
    rprtDate:string='';
    routeSub: any;
    selectedReqId: number;
    parametersSubmited: ParametersSubmited[] = [];
    schedObj = new SchedulingModel();
    schedParameters: Parameters[] = [];
    pvtRowsEligibleCols: any[] = [];
    pvtColsEligibleCols: any[] = [];
    request:any;
    showProcessing:boolean=false;
    notificationsObj = {
        timeOut: 3000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: true,
        maxLength: 60
    };

    chartOptions = {
        responsive: false,
        maintainAspectRatio: false
    }
    allVarcharCols = [];
    allDecimalCols = [];
    accStatusList: LookUpCode[] = [];          
    chartColors = ["#E7E9ED",
        "#FF6384",
        "#4BC0C0",
        "#FFCE56",
        "#36A2EB"
    ];
    outputPath: string;

    reportsReqListTableCols = [                  //  Report list table columns
        { field: 'reqName', header: 'Request Name' },
        { field: 'createdDate', header: 'Created On' },
        { field: 'submittedTime', header: 'Submited On' },
        { field: 'generatedTime', header: 'Generated On' },
        { field: 'status', header: 'Status' }
    ];
    //#endregion
    constructor(
        private reportsService: ReportsService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private notificationsService: NotificationsService,
        private dialog: MdDialog,
        private cs: CommonService,
        public datepipe: DatePipe,
        private router: Router
    ) {
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.pageSize;
        this.reportsService.getLookupValues('ACCOUNTING_STATUS','REPORTING').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
            this.accStatusList = res;
        },
        (res: Response) => {
          this.onError('Failed to load Account Status list!')
        });
        this.reportsService.getLookupValues('REPORT_OUTPUT_TYPE').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {      //To load report output types
            this.reportOutput = res;
        });
        this.reportsService.getLookupValues('CHART_TYPE').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {              //To load chart types
            this.chartTypes = res;
        });
    }
    ngOnInit() {
        // this.cmpHeight=(this.cs.screensize().height - 300) + 'px';
        this.cmpHeight ='calc(100vh - 300px)';
        if (this.cs.screensize().height > 500) {
            this.TemplatesHeight = (this.cs.screensize().height - 450) + 'px';
        } else {
            this.TemplatesHeight = 100 + 'px';
        }

        $(".split-example").css({
            'min-height': 'calc(100vh - 150px)'
        });

        this.sortOrder = '';
        this.sortCol = '';
        this.blockUI=true;
        this.reportsService.getAllReportsList().takeWhile(() => this.alive).subscribe((res: Response) => {                                 //To load reports list
            this.blockUI=false;
            this.reportsList = res.json();
            this.routeSub = this.activatedRoute.params.takeWhile(() => this.alive).subscribe(params => {
                let url = this.activatedRoute.snapshot.url.map(segment => segment.path).join('/');
                let presentPath = this.activatedRoute.snapshot.url;
                //Check is 'Request ID' is there in route params or not
                if (params['reqId'] && params['id']) {
                    this.selectedReqId = +params['reqId'];
                    this.selectedReportId = +params['id'];
                    this.reportsList.forEach(item => {
                        if (item.id == this.selectedReportId)
                            this.rptName = item.reportName;
                    });
                    this.urlWithRequestId=true;
                    this.getRequestsList(undefined);
                }
                //Check is 'ID' is there in route params or not
                else if (params['id']) {
                    this.selectedReportId = +params['id'];
                    this.reportsList.forEach(item => {
                        if (item.id == this.selectedReportId) {
                            this.rptName = item.reportName;
                            // this.getRequestsList();
                            this.reportSelected(item.id);
                        }
                    });
                }
            });
        },(res)=>{
            this.blockUI=false;
        });
    }
    tabChange(event){
        this.pageEvent.pageIndex=0;
        this.pageEvent.pageSize=this.pageSize;
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
        if (event.order < 1)
            sortOrder = 'descending';
        let sortCol = event.field;
        this.getRequestsList(undefined,sortCol,sortOrder);
    }

    // To get report request list
    requestType:string = undefined;
    getRequestsList(requestType?:string, sortCol?:string, sortOrder?:string) {
        if (!this.selectedReportId || this.selectedReportId == undefined || this.selectedReportId < 1) {
            this.onError('Please select a valid report');
            return;
        }
        this.requestType=requestType;
        this.blockUI=true;
        this.reportsService.getReportReqList(this.selectedReportId, {
            page: this.pageEvent.pageIndex,
            size: this.pageEvent.pageSize
        },this.requestType).takeWhile(() => this.alive).subscribe((res: Response) => {
            this.reportReqList = res.json();
            if (this.reportReqList.length > 0) {
                if (this.selectedReqId) {
                    for (var i = 0; i < this.reportReqList.length; i++) {
                        if (this.reportReqList[i].reqId == this.selectedReqId) {
                            this.reportInfo = jQuery.extend(true, {}, res.json()[i].filterMap);
                            this.reportInfo.dateTimeVal = new Date(this.reportInfo.dateTimeVal);
                            if(this.urlWithRequestId){
                                this.multiSelRows=this.reportInfo.groupingCols;
                                if(this.reportInfo.columnCols && this.reportInfo.columnCols.length>0){
                                    this.singleSelCol=this.reportInfo.columnCols[0];
                                }
                                if(this.reportInfo.valueCols && this.reportInfo.valueCols.length>0){
                                    this.rptValCol=this.reportInfo.valueCols[0];
                                }
                                this.viewReportReqOutput(this.selectedReqId, this.reportInfo.outputType);
                                this.urlWithRequestId=false;
                            }
                        }
                    }
                } else {
                    this.reportInfo = jQuery.extend(true, {}, res.json()[0].filterMap);
                    this.reportInfo.dateTimeVal = new Date(this.reportInfo.dateTimeVal);;
                }
                this.bindLOVs();
                this.requestsLength = +res.headers.get('x-count');
            }
            else {
                let msg='Submit your first request!';
                if(requestType&&requestType=='request'){
                    msg='Submit your first job request!';
                }
                this.notificationsService.info(
                    'First Request',
                    msg,
                    {
                        timeOut: 2000,
                        showProgressBar: false,
                        pauseOnHover: true,
                        clickToClose: true,
                        maxLength: 50
                    }
                )
                this.showRequestsList=false;
                this.reportSelected(this.selectedReportId);
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
        this.reportInfo = jQuery.extend(true, {}, row.filterMap);
        this.reportInfo.dateTimeVal = new Date(this.reportInfo.dateTimeVal);;
        this.selectedReqId = row.reqId;
        this.viewReportReqOutput(row.reqId, row.filterMap.outputType);
    }

    public ngOnDestroy() {
        this.alive = false;
    }

    onPvtFieldsSelected(source, destination: any[]) {
        if (source instanceof Array) {
            this.allVarcharCols.forEach(element => {
                if (source.indexOf(element) >= 0 && destination.indexOf(element) >= 0) {
                    let ind = destination.indexOf(element);
                    destination.splice(ind, 1);
                } else if (destination.indexOf(element) < 0) {
                    destination.push(element);
                }
            });
        } else {
            this.allVarcharCols.forEach(element => {
                if (source == element && destination.indexOf(element) >= 0) {
                    let ind = destination.indexOf(element);
                    destination.splice(ind, 1);
                } else if (destination.indexOf(element) < 0) {
                    destination.push(element);
                }
            });
        }
    }

    checkColumnExistence(source, destination) {
        if (!destination)
            return;
        if (source instanceof Array) {
            if (destination instanceof Array) {
                for (var i = 0, srcLen = source.length; i < srcLen; i++) {
                    for (var j = 0, destLen = destination.length; j < destLen; j++) {
                        if (source[i].id == destination[j].id) {
                            source.splice(i, 1);
                            srcLen = source.length;
                            break;
                        }
                    }
                }
            } else {
                for (var i = 0, srcLen = source.length; i < srcLen; i++) {
                    if (source[i].id == destination.id) {
                        source = undefined;
                        break;
                    }
                }
            }
        } else {
            if (destination instanceof Array) {
                for (var j = 0, destLen = destination.length; j < destLen; j++) {
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
        if (event.order < 1)
            this.sortOrder = 'descending';
        this.sortCol = event.field;
        this.selectedReqId-reqId;
        this.updatedReportOutput();
    }

    updatedReportOutput() {
        this.blockUI=true;
        this.reportsService.getReportOutputByReqId(this.selectedReqId,'table', this.pageEvent.pageIndex, this.pageEvent.pageSize, this.sortOrder, this.sortCol, this.searchStr).takeWhile(() => this.alive).subscribe((res: Response) => {
            this.tabularReportPresentation(res);
        },
        (res: Response) => {
            this.blockUI=false;
            this.onError('Report request failed');
        });
    }

    tabularReportPresentation(res: Response) {
        this.reportResult = res.json().data;
        this.reportsTableColumns = res.json().columns;
        for (var i = 0, cnt = res.json().columns.length; i < cnt; i++) {
            this.columnOptions.push({ label: res.json().columns[i]['header'], value: res.json().columns[i] });
        }
        this.showReportOutput = true;
        this.length = +res.headers.get('x-count');
        if(this.length==0){
            this.notificationsService.info('Oops!', 'No data available, Update parameters and re-run', this.notificationsObj);
        }
        this.showChart = false;
        this.showPivot = false;
        this.showTabular = true;
        this.showChangeView = false;
        this.showParameters = false;
        this.blockUI=false;
        this.showProcessing = false;
    }

    viewReportReqOutput(reqId, outputType: string) {
        this.fnlObj = jQuery.extend(true, {}, this.reportInfo);
        let changedToChart:boolean=false;
        if(this.reportInfo.outputType=='CHART'){           //Need to remove this condition check after original chart functionality added
            this.reportInfo.outputType='PIVOT';
            changedToChart=true;
        }
        this.showProcessing = true;
        this.blockUI=true;
        if (this.reportInfo.outputType == 'TABLE') {
            
            this.request = this.reportsService.getReportOutputByReqId(reqId,'table', this.pageEvent.pageIndex, this.pageEvent.pageSize).takeWhile(() => this.alive).subscribe((res: Response) => {
                this.showRequestsList=false;
                this.tabularReportPresentation(res);
            },(res)=>{
                this.blockUI=false;
                this.showProcessing = false;
            });
        } else if (this.reportInfo.outputType == 'PIVOT') {
            //***************** Building Pivot *****************
            this.showPivot = false;
            var derivers = $.pivotUtilities.derivers;
            var utils = $.pivotUtilities;
            var sum = $.pivotUtilities.aggregatorTemplates.sum;
            var numberFormat = $.pivotUtilities.numberFormat;
            var intFormat = numberFormat({ digitsAfterDecimal: 2 });
            this.fnlObj.columnCols = [];                                    //Reset all arrays
            this.fnlObj.valueCols = [];
            this.fnlObj.groupingCols = [];
            let pvtRows = [];
            let pvtCols = [];
            let pvtVals = [];
            this.fnlObj.groupingCols = this.multiSelRows;
            this.fnlObj.columnCols.push(this.singleSelCol);
            this.fnlObj.valueCols.push(this.rptValCol);
            this.fnlObj.groupingCols.forEach(selCol => {                           //rows section selected columns
                pvtRows.push(selCol.itemName);
            });
            
            if (this.reportInfo.fields[0].repType == 'AGING_REPORT') {
                pvtCols.push("Buckets");
                let newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                pvtVals.push(newA.layoutDisplayName);

            } else if (this.reportInfo.fields[0].repType == 'ROLL_BACK_REPORT') {
                pvtCols.push("Months");
                let newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                pvtVals.push(newA.layoutDisplayName);
            } else {
                pvtCols.push(this.fnlObj.columnCols[0].itemName);             //columns section selected columns for local use
                pvtVals.push(this.fnlObj.valueCols[0].itemName);                //values section selected columns for local use
            }
            //**********************************************
            this.request = this.reportsService.getReportOutputByReqId(reqId,'pivot', 0, 0).takeWhile(() => this.alive).subscribe((res: Response) => {
                this.pvtReportResult = res.json().data;
                if(this.pvtReportResult.length==0){
                    this.notificationsService.info('Oops!', 'No data available, Update parameters and re-run', this.notificationsObj);
                }
                var render="Col Heatmap";
                if(changedToChart){           //Need to remove this condition check after original chart functionality added
                    pvtCols=[];
                    pvtCols.push(this.fnlObj.columnCols[0].itemName);
                    if(this.reportInfo.chartType=='BAR')
                        render="Bar Chart";
                    else if(this.reportInfo.chartType=='LINE')
                        render="Line Chart";
                }
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
                                    function (e, value, filters, pivotData) {
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

                this.showChangeView = false;
                this.showPvtOptns = false;
                this.showParameters = false;
                this.showReportOutput = true;
                this.showTabular = false;
                this.showChart = false;
                this.showPivot = true;
                this.blockUI=false;
                this.showProcessing = false;
                if(changedToChart){           //Need to remove this condition check after original chart functionality added
                    this.reportInfo.outputType='CHART';
                }
                $(".pvtUnused").prepend("<p class=\"no-mb\">Available Columns</p>");
                $(".pvtRows").prepend("<p class=\"no-mb\">Rows Section</p>");
                $(".pvtCols").prepend("<p class=\"colHead\">Columns Section</p>");
                $(".pvtVals").prepend("<p class=\"no-mb\">Aggregator</p>");
                $("#pvtoutput > table > tr:nth-child(1) > td:nth-child(1)").prepend("<p class=\"no-mb\">View As</p>");
            },(res)=>{
                this.blockUI=false;
                this.showProcessing = false;
            });
        } else {
            return;
        }
        this.buildParameters(this.fnlObj,Number(reqId));
    }

    reRunAsOfNow(filterMap, reqId) {
        this.reportInfo = filterMap;
        this.reportInfo.dateTimeVal = this.today;
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
    
    //It will work when report selection is updated
    disableRecStatus: boolean = true;
    disableAccStatus: boolean = true;
    reportSelected(reportId: number) {
        this.sortOrder = '';
        this.sortCol = '';
        this.searchStr = '';
        this.reportInfo = new ReportColsInfo();
        this.disableRecStatus = true;
        this.disableAccStatus = true;
        this.blockUI=true;
        this.reportsService.getReportFieldsInfo(reportId).takeWhile(() => this.alive).subscribe((res: ReportColsInfo) => {      //To load selected report definition
            this.reportInfo = res;
            this.reportInfo.dateTimeVal = this.today;                     //Default report date is current date
            this.reportInfo.outputType = 'TABLE';                                                   //Default report output type
            let tempVar = res;
            this.showParameters = true;
            if (this.reportInfo.accVal && this.reportInfo.accVal == 'Both')
                this.disableAccStatus = false;
            if (this.reportInfo.reconcile && this.reportInfo.reconcile == 'Both')
                this.disableRecStatus = false;
            this.blockUI=false;
            if(tempVar.segmentInfo){
                for(var i=0;i<tempVar.segmentInfo.length;i++){
                    this.reportInfo.segmentInfo[i].flexValues1=[];
                    for(var j=0, len=tempVar.segmentInfo[i].flexValues.length;j<len;j++){
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
            for (var i = 0; i < tempVar.fields.length; i++) {
                if (tempVar.fields[i].fieldType === 'MULTI_SELECT_LOV' || tempVar.fields[i].fieldType === 'SINGLE_SELECT_LOV' || tempVar.fields[i].fieldType === 'SINGLE_SELECTION') {
                    this.reportsService.getReportParamLovsByParamId(reportId, this.reportInfo.fields[i].rParamId).takeWhile(() => this.alive).subscribe((res: any) => {
                        let ind = res.index;
                        this.reportInfo.fields[ind].fieldValuesList = res.fieldValuesList;
                        let selVals=[];
                            this.parametersSubmited.forEach(element => {
                                if(element.ParamId==this.reportInfo.fields[ind].rParamId){
                                    selVals = element.Value;
                                }
                            });
                        if (this.reportInfo.fields[ind].fieldType === 'MULTI_SELECT_LOV') {
                            this.reportInfo.fields[ind].selectedValue = [];
                            let newValueList=[];
                            for (var i = 0; i < this.reportInfo.fields[ind].fieldValuesList.length; i++) {
                                let newVal={
                                    "id": i,
                                    "itemName": this.reportInfo.fields[ind].fieldValuesList[i],
                                    "dataName": this.reportInfo.fields[ind].fieldValuesList[i]
                                }
                                newValueList.push(newVal);
                                if(selVals.indexOf(this.reportInfo.fields[ind].fieldValuesList[i])>-1){
                                    this.reportInfo.fields[ind].selectedValue.push(newVal);
                                }
                            }
                            this.reportInfo.fields[ind].fieldValuesList = newValueList;
                        }else{
                            this.reportInfo.fields[ind].selectedValue = selVals;
                        }
                    },
                        (res: Response) => {
                            this.onError('Failed to load LOV values for ' + this.reportInfo.fields[i].fieldName);
                        });
                } else if (tempVar.fields[i].fieldType === 'DATE_RANGE') {
                    tempVar.fields[i].selectedValue = { 'fromDate': undefined, 'toDate': new Date() };
                    this.parametersSubmited.forEach(element => {
                        if(element.ParamId==this.reportInfo.fields[i].rParamId){
                            tempVar.fields[i].selectedValue = new Date(element.Value);
                        }
                    });
                }
                else if (tempVar.fields[i].fieldType === 'MONTH_RANGE') {
                    tempVar.fields[i].selectedValue = { 'fromDate': undefined, 'toDate': new Date() };
                }
                this.parametersSubmited.forEach(ele=>{        //if user is going to update parameter values
                    if(ele.Parameter=='Report Date'){
                        this.reportInfo.dateTimeVal=new Date(ele.Value);
                    }
                })
            }
            this.bindLOVs();
        },
            (res: Response) => {
                this.reportInfo.fields = [];
                this.blockUI=false;
                this.onError('Report definition failed to load!');
            });
    }

    bindLOVs() {
        for (var i = 0; i < this.reportInfo.outputCols.length; i++) {
            if (this.reportInfo.outputCols[i] && this.reportInfo.outputCols[i] !== null) {
                if (this.reportInfo.outputCols[i].dataType == 'DECIMAL' || this.reportInfo.outputCols[i].dataType == 'AGGREGATOR') {
                    this.allDecimalCols.push({
                        "id": this.reportInfo.outputCols[i].ColumnId,
                        "itemName": this.reportInfo.outputCols[i].layoutDisplayName,
                        "dataName": this.reportInfo.outputCols[i].layoutDisplayName,
                        "dataType": this.reportInfo.outputCols[i].dataType
                    });
                } else {
                    this.allVarcharCols.push({
                        "id": this.reportInfo.outputCols[i].ColumnId,
                        "itemName": this.reportInfo.outputCols[i].layoutDisplayName,
                        "dataName": this.reportInfo.outputCols[i].layoutDisplayName,
                        "dataType": this.reportInfo.outputCols[i].dataType
                    });
                }
            }
        }
        this.pvtRowsEligibleCols = [...this.allVarcharCols];
        this.pvtColsEligibleCols = [...this.allVarcharCols];
    }

    //It will work if output type is updated (need to update with latest changes)
    changeOPwithReq = false;
    outputTypeChanged() {
        this.multiSelRows = [];
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
    }
    //It will work if user click on submit
    runReport(resetPageNum: boolean, isRefresh?: boolean) {
        
        this.reportsService.testOozieServer().takeWhile(() => this.alive).subscribe((oozieStatus: OozieStatus) => {
            if (oozieStatus && oozieStatus.dbStatus && oozieStatus.ooziestatus) {
                var dialogRef = this.dialog.open(ConfirmDialogComponent, {
                    data: { message: "Do you want to submit report as job request?", ok: 'ok', cancel: 'no', okLabel: 'Yes', cancelLabel: 'No, I will wait' }
                });
                dialogRef.afterClosed().subscribe(result => {
                    if (!result) {
                        return;
                    }
                    if (resetPageNum)
                        this.pageEvent.pageIndex = 0;
                    this.fnlObj = jQuery.extend(true, {}, this.reportInfo);                         //Creating new object for report info
                    this.fnlObj.fields.forEach(item => {                                            //Gathering all selected values for multi select LOV
                        if (item.fieldType == 'MULTI_SELECT_LOV') {
                            if(item.selectedValue&&item.selectedValue[0]&&typeof item.selectedValue[0] === 'object' && item.selectedValue[0].constructor === Object){
                                let newValues = [];
                                item.selectedValue.forEach(val => {
                                    newValues.push(val.dataName);
                                });
                                item.selectedValue = newValues;
                            }
                        }
                        item.fieldValuesList = [];
                    });
                    if(this.fnlObj.segmentInfo){
                        for(var i=0;i<this.fnlObj.segmentInfo.length;i++){
                            this.fnlObj.segmentInfo[i].selFlexValues=[];
                            for(var j=0, len=this.fnlObj.segmentInfo[i].selFlexValues1.length;j<len;j++){
                                this.fnlObj.segmentInfo[i].selFlexValues.push(this.fnlObj.segmentInfo[i].selFlexValues1[j].dataName.split('-')[0]);
                            }
                            this.fnlObj.segmentInfo[i].flexValues1=[];
                            this.fnlObj.segmentInfo[i].selFlexValues1=[];
                        }
                    }
                    this.columnOptions = [];
                    this.rptSubmitted = true;
                    if (this.fnlObj.dateTimeVal != this.today) {
                        this.fnlObj.dateTimeVal.setDate(this.fnlObj.dateTimeVal.getDate() + 1);
                    }
                    let changedToChart: boolean = false;
                    if (this.fnlObj.outputType == 'CHART') {           //Need to remove this condition check after original chart functionality added
                        this.fnlObj.outputType = 'PIVOT';
                        changedToChart = true;
                    }
                    if (result != 'ok') {
                        this.showProcessing = true;
                    }
                    this.blockUI = true;
                    switch (this.fnlObj.outputType) {
                        case 'TABLE':
                            this.fnlObj.outputCols = [];
                            this.showPivot = false;
                            this.request = this.reportsService.getReportOutputFirstTime(this.selectedReportId, this.fnlObj,
                                result, {
                                    page: this.pageEvent.pageIndex,
                                    size: this.pageEvent.pageSize
                                }).takeWhile(() => this.alive).subscribe((res: Response) => {
                                    this.rptSubmitted = false;
                                    this.blockUI = false;
                                    if (result == 'ok') {
                                        this.showChangeView = false;
                                        this.showParameters = false;
                                        this.notificationsService.success('Submitted', 'You will get a notification once job done!', this.notificationsObj);
                                    } else {
                                        this.showProcessing = false;
                                        if(!res.json().requestInfo || !res.json().requestInfo.id){
                                            this.showProcessing=false;
                                            this.blockUI = false;
                                            return;
                                        }
                                        
                                        this.selectedReqId = res.json().requestInfo.id;
                                        this.tabularReportPresentation(res);
                                        if (!isRefresh)
                                            this.buildParameters(this.fnlObj,Number(res.json().requestInfo.id));
                                    }
                                }, (res: Response) => {
                                    this.blockUI = false;
                                    this.rptSubmitted = false;
                                    this.showChangeView = false;
                                    this.showProcessing = false;
                                    this.notificationsService.error('Sorry!', 'Request failed to run', this.notificationsObj);
                                });
        
                            break;
                        case 'PIVOT':
                            if (this.multiSelRows.length < 1) {
                                this.notificationsService.info(
                                    'Sorry!',
                                    'Pivot row labels section should not be empty, please select atleast one column',
                                    this.notificationsObj
                                );
                                this.showProcessing = false;
                                this.blockUI = false;
                                return;
                            }
                            if ((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && !this.singleSelCol) {
                                this.notificationsService.info(
                                    'Sorry!',
                                    'Pivot column labels section should not be empty, please select atleast one column',
                                    this.notificationsObj
                                );
                                this.showProcessing = false;
                                this.blockUI = false;
                                return;
                            }
                            if ((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && !this.rptValCol) {
                                this.notificationsService.info(
                                    'Sorry!',
                                    'Pivot values section should not be empty, please select atleast one column',
                                    this.notificationsObj
                                );
                                this.showProcessing = false;
                                this.blockUI = false;
                                return;
                            }
                            this.fnlObj.columnCols = [];                                    //Reset all arrays
                            this.fnlObj.valueCols = [];
                            this.fnlObj.groupingCols = [];
                            let pvtRows = [];
                            let pvtCols = [];
                            let pvtVals = [];
                            this.fnlObj.groupingCols = this.multiSelRows;
                            this.fnlObj.columnCols.push(this.singleSelCol);
                            this.fnlObj.valueCols.push(this.rptValCol);
        
                            this.multiSelRows.forEach(selCol => {                           //rows section selected columns
                                pvtRows.push(selCol.itemName);
                            });
                            if (this.reportInfo.fields[0].repType == 'AGING_REPORT') {
                                pvtCols.push("Buckets");
                                let newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                                pvtVals.push(newA.layoutDisplayName);
        
                            } else if (this.reportInfo.fields[0].repType == 'ROLL_BACK_REPORT') {
                                pvtCols.push("Months");
                                let newA = jQuery.extend(true, {}, this.fnlObj.aggregator);
                                pvtVals.push(newA.layoutDisplayName);
                            } else {
                                pvtCols.push(this.singleSelCol.itemName);             //columns section selected columns for local use
                                pvtVals.push(this.rptValCol.itemName);                //values section selected columns for local use
                            }
        
                            this.fnlObj.outputCols = [];
                            this.request = this.reportsService.getReportOutputFirstTime(this.selectedReportId, this.fnlObj, result).takeWhile(() => this.alive).subscribe((res: Response) => {
                                this.rptSubmitted = false;
                                if (result == 'ok') {
                                    this.showChangeView = false;
                                    this.showParameters = false;
                                    this.notificationsService.success('Submitted', 'You will get a notification once job done!', this.notificationsObj);
                                    this.showProcessing = false;
                                    this.blockUI = false;
                                    return;
                                }
                                
                                if (res.json().length < 1) {
                                    this.notificationsService.info(
                                        'Error!',
                                        'Data is not available for selected filters',
                                        this.notificationsObj
                                    );
                                    this.pvtReportResult = [];
                                    this.showProcessing = false;
                                    this.blockUI = false;
                                    return;
                                }
                                // if (this.showParameters) {
                                //     this.getRequestsList();
                                // }
                                this.showChangeView = false;
                                this.showReportOutput = true;                           //Reset helping boolean variables
                                this.showTabular = false;
                                this.showChart = false;
                                this.showPivot = true;
                                this.showParameters = false;
                                this.blockUI = false;
                                this.selectedReqId = res.json().requestInfo.id;
                                if (!isRefresh)
                                    this.buildParameters(this.fnlObj,Number(res.json().requestInfo.id));
                                this.pvtReportResult = res.json().data;
                                if(this.pvtReportResult.length==0){
                                    this.notificationsService.info('Oops!', 'No data available, Update parameters and re-run', this.notificationsObj);
                                }
                                //***************** Building Pivot *****************
                                var derivers = $.pivotUtilities.derivers;
                                // var renderers = $.extend($.pivotUtilities.renderers, $.pivotUtilities.c3_renderers);
                                var utils = $.pivotUtilities;
                                var sum = $.pivotUtilities.aggregatorTemplates.sum;
                                var numberFormat = $.pivotUtilities.numberFormat;
                                var intFormat = numberFormat({ digitsAfterDecimal: 2 });
                                var render = "Col Heatmap";
                                if (changedToChart) {           //Need to remove this condition check after original chart functionality added
                                    pvtCols = [];
                                    pvtCols.push(this.singleSelCol.itemName);
                                    if (this.reportInfo.chartType == 'BAR')
                                        render = "Bar Chart";
                                    else if (this.reportInfo.chartType == 'LINE')
                                        render = "Line Chart";
                                }
        
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
                                                    function (e, value, filters, pivotData) {
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
                                if (changedToChart) {           //Need to remove this condition check after original chart functionality added
                                    this.reportInfo.outputType = 'CHART';
                                }
                                $(".pvtUnused").prepend("<p class=\"no-mb\">Available Columns</p>");
                                $(".pvtRows").prepend("<p class=\"no-mb\">Rows Section</p>");
                                $(".pvtCols").prepend("<p class=\"colHead\">Columns Section</p>");
                                $(".pvtVals").prepend("<p class=\"no-mb\">Aggregator</p>");
                                $("#pvtoutput > table > tr:nth-child(1) > td:nth-child(1)").prepend("<p class=\"no-mb\">View As</p>");
                                //**********************************************
                            }, (res: Response) => {
                                if (changedToChart) {           //Need to remove this condition check after original chart functionality added
                                    this.reportInfo.outputType = 'CHART';
                                }
                                this.rptSubmitted = false;
                                this.showChangeView = false;
                                this.blockUI = false;
                                this.notificationsService.error('Sorry!', 'Request failed to run', this.notificationsObj);
                            });
                            break;
                        case 'CHART':
                            //#region  Original Chart
                            // if (!this.singleSelCol) {
                            //     this.notificationsService.info(
                            //         'Sorry!',
                            //         'Chart labels section should not be empty, please select atleast one column',
                            //         this.notificationsObj
                            //     );
                            //     return;
                            // }
                            // switch (this.fnlObj.chartType) {
                            //     case 'BAR':
                            //     case 'LINE':
                            //         if ((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && this.multiSelRows.length < 1) {
                            //             this.notificationsService.info(
                            //                 'Sorry!',
                            //                 'Chart values section should not be empty, please select atleast one column',
                            //                 this.notificationsObj
                            //             );
                            //             return;
                            //         }
                            //         this.fnlObj.groupingCols = [];
                            //         this.fnlObj.valueCols = [];
                            //         this.fnlObj.columnCols = [];
                            //         this.fnlObj.groupingCols.push(this.singleSelCol);
                            //         this.fnlObj.valueCols = this.multiSelRows;
                            //         break;
                            //     case 'PIE':
                            //         if ((this.reportInfo.fields[0].repType !== 'AGING_REPORT' && this.reportInfo.fields[0].repType !== 'ROLL_BACK_REPORT') && !this.rptValCol) {
                            //             this.notificationsService.info(
                            //                 'Sorry!',
                            //                 'Chart values section should not be empty, please select atleast one column',
                            //                 this.notificationsObj
                            //             );
                            //             return;
                            //         }
                            //         this.fnlObj.groupingCols = [];
                            //         this.fnlObj.valueCols = [];
                            //         this.fnlObj.groupingCols.push(this.singleSelCol);
                            //         this.fnlObj.valueCols.push(this.rptValCol);
                            //         break;
                            // }
                            // this.reportsService.getChartReportOutput(this.selectedReportId, this.fnlObj).takeWhile(() => this.alive).subscribe((res: any) => {
                            //     this.rptSubmitted = false;
                            //     if (res.length < 1) {
                            //         this.notificationsService.info(
                            //             'Error!',
                            //             'Data is not available for selected filters',
                            //             this.notificationsObj
                            //         );
                            //         this.chartResult = new ChartData();;
                            //         return;
                            //     }
                            //     this.showChangeView = false;
                            //     this.showReportOutput = true;
        
                            //     this.pvtReportResult = [];
                            //     this.chartResult = res;
                            //     var j = 0;
                            //     for (var i = 0; i < this.chartResult.datasets.length; i++) {
                            //         this.chartResult.datasets[i].backgroundColor = this.chartColors[j];
                            //         j++;
                            //         if (j == this.chartColors.length) {
                            //             j = 0;
                            //         }
                            //     }
                            //     this.showTabular = false;
                            //     this.showChart = true;
                            //     this.showPivot = false;
                            //     this.showParameters = false;
        
                            // });
                            //#endregion
                            break;
                    }
                    
                });
                //#endregion
            } else {
                this.notificationsService.error('Sorry!', 'Server is unstable!', {
                    timeOut: 4000,
                    showProgressBar: false,
                    pauseOnHover: true,
                    clickToClose: true,
                    maxLength: 100
                });
            }
        },(res: any)=>{
            this.notificationsService.error('Sorry!', 'Server is unstable!', {
                timeOut: 4000,
                showProgressBar: false,
                pauseOnHover: true,
                clickToClose: true,
                maxLength: 100
            });
        });
        
        
    }

    onParametersHide(){
        if(!(this.showTabular || this.showPivot)){
            this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
        }
    }

    buildParameters(fnlObj,reqId) {
        if (this.prevReqId==reqId){
            return;
        }
        this.prevReqId=reqId;
        this.parametersSubmited = [];
        //#region binding report parameters to table object
        fnlObj.fields.forEach(element => {
            if(element && element !== null && element.selectedValue.length>0){
                let newParam = new ParametersSubmited();
                newParam.Parameter = element.fieldName;
                newParam.ParamId=element.rParamId;
                newParam.Value = element.selectedValue;
                this.parametersSubmited.push(newParam);
            }
        });
        let recParam = new ParametersSubmited();
        recParam.Parameter = 'Reconcile Status';
        if (fnlObj.reconcile === 'Both') {
            recParam.Value = 'Reconciled, Un-Reconciled';
        } else {
            recParam.Value = fnlObj.reconcile;
        }
        this.parametersSubmited.push(recParam);
        let accParam = new ParametersSubmited();
        accParam.Parameter="Accounted Status";
        if (fnlObj.accVal === 'Both') {
            accParam.Value = 'Accounted, Un-Accounted';
        } else {
            accParam.Value = fnlObj.accVal;
        }
        this.parametersSubmited.push(accParam);
        if(fnlObj.segmentInfo&&fnlObj.segmentInfo.length>0){
            for(var i=0;i<fnlObj.segmentInfo.length;i++){
                if(fnlObj.segmentInfo[i].selFlexValues.length>0){
                    let segParam = new ParametersSubmited();
                    segParam.ParamId=fnlObj.segmentInfo[i].segId;
                    segParam.Parameter=fnlObj.segmentInfo[i].segName;
                    segParam.Value=fnlObj.segmentInfo[i].selFlexValues;
                    this.parametersSubmited.push(segParam);
                }
            }
        }
        if(fnlObj.dateTimeVal){
            let repDtParam=new ParametersSubmited();
            repDtParam.Parameter="Report Date";
            let newDate = new Date(fnlObj.dateTimeVal);
            repDtParam.Value=this.datepipe.transform(newDate, 'medium').toString();
            this.rprtDate=repDtParam.Value;
            this.parametersSubmited.push(repDtParam);
        }
        if(fnlObj.aggregator){
            if(fnlObj.aggregator.ColumnId){
                let argrParam=new ParametersSubmited();
                argrParam.Parameter="Agregator";
                argrParam.Value=fnlObj.aggregator.layoutDisplayName;
                this.parametersSubmited.push(argrParam);
            }
        }
        // if(fnlObj.outputType){
        //     let opParam=new ParametersSubmited();
        //     opParam.Parameter="Output Type";
        //     opParam.Value=fnlObj.outputType;
        //     this.parametersSubmited.push(opParam);
        // }
        if(fnlObj.fields[0].rptType=='ROLL_BACK_REPORT'){
            let colsCnt=0;
            for(var i=0;i<fnlObj.outputCols.length;i++){
                if(fnlObj.outputCols[i].columnType=='ROLL_BACK'){
                    colsCnt++;
                }
            };
            let cntParam=new ParametersSubmited();
            cntParam.Parameter="Roll Back Count";
            cntParam.Value=colsCnt.toString();
            this.parametersSubmited.push(cntParam);
        }
        if(fnlObj.fields[0].rptType=='AGING_REPORT'){
            let cols=[];
            for(var i=0;i<fnlObj.outputCols.length;i++){
                if(fnlObj.outputCols[i].columnType=='AGING'){
                    cols.push(fnlObj.outputCols[i].layoutDisplayName);
                }
            };
            let bktParam=new ParametersSubmited();
            bktParam.Parameter="Bucket Columns";
            bktParam.Value=fnlObj.cols.join(', ');
            this.parametersSubmited.push(bktParam);
        }
    }

    hideDialog(event) {
        this.displayScheduling = event;
    }

    public chartLabels: string[] = ['Extraction', 'Transformation', 'Reconciliation', 'Accounting', 'Journals'];
    public chartData: number[] = [300, 500, 100, 250, 200];
    public pieChartOptions: any = {
        responsive: true,
        legend: {
            position: 'bottom',
            labels: {
                fontSize: 12
            }
        }
    };
    public barChartOptions: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            xAxes: [
                {
                    stacked: true
                }
            ],
            yAxes: [
                {
                    stacked: true
                }
            ]
        },
        legend: {
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Accounting'
        }/* ,
        scales: { xAxes: [{ stacked: true }], yAxes: [{ stacked: true }] } */
    };
    testMethod() {
        var dialogRef = this.dialog.open(ShareViaEmailDialog, {
            data: { reqId: this.selectedReqId, ok: 'ok', cancel: 'cancel' }
        });
    }

    exportToCSV() {
        this.showProcessing=true;
        this.blockUI=true;
        this.request = this.reportsService.downloadReportCSVFile(this.selectedReqId).takeWhile(() => this.alive).subscribe((res: any) => {
            this.showProcessing=false;
            this.blockUI=false;
        },(res: Response)=>{
            this.blockUI=false;
            this.showProcessing=false;
        });
    }

    shareReport() {
        var dialogRef = this.dialog.open(ShareViaEmailDialog, {
            data: { reqId: this.selectedReqId, ok: 'ok', cancel: 'cancel' }
        });
    }

    displayScheduleWindow() {
        this.schedParameters = [];
        this.schedObj.jobName = this.rptName;
        this.schedObj.programName = 'Reporting';
        this.schedParameters.push(new Parameters());
        this.schedParameters[0].paramName = "Report Id";
        this.schedParameters[0].selectedValue = this.selectedReportId;
        this.schedParameters.push(new Parameters());
        this.schedParameters[1].paramName = "Parameters";
        this.schedParameters[1].selectedValue = JSON.stringify(this.reportInfo);
        this.schedParameters.push(new Parameters());
        this.schedParameters[2].paramName = "Page Number";
        this.schedParameters[2].selectedValue = 0;
        this.schedParameters.push(new Parameters());
        this.schedParameters[3].paramName = "Page Size";
        this.schedParameters[3].selectedValue = 1;
        this.schedParameters.push(new Parameters());
        this.schedParameters[4].paramName = "View Type";
        this.schedParameters[4].selectedValue = this.reportInfo.outputType;
        this.schedObj.parameters = this.schedParameters;
        this.displayScheduling = !this.displayScheduling;
    }

    noFuctionality() {
        this.notificationsService.info('Sorry!', 'Yet to implement, please wait for some time', this.notificationsObj);
    }
    //Show error messages
    private onError(errorMessage) {
        this.notificationsService.error(
            'Error!',
            errorMessage,
            {
                timeOut: 3000,
                showProgressBar: false,
                pauseOnHover: true,
                clickToClose: true,
                maxLength: 50
            }
        )
    }

    cancelRequest(){
        if(this.request)
            this.request.unsubscribe();
        this.showProcessing=false;
        this.rptSubmitted = false;
        this.showChangeView = false;
        this.blockUI=false;
    }

    toggleParamsHeader(){
        if (this.expandParamsHeader) {
            this.TemplatesHeight = (this.cs.screensize().height - 450) + 'px';
        } else {
            this.TemplatesHeight = (this.cs.screensize().height - 150) + 'px';
        }
    }
}