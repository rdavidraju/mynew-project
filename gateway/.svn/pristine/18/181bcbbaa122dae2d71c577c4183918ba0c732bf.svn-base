//#region Import Statments
import { Component, OnDestroy, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Response } from '@angular/http';
import { Subscription, Subject } from 'rxjs/Rx';
import { DatePipe } from '@angular/common';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService } from 'ng-jhipster';
import { FormGroup, FormControl } from '@angular/forms';
import { Reports, ReportColsInfo, ParametersSubmited, OozieStatus, DataViews } from './reports.model';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { ReportsService } from './reports.service';
import { Principal, ResponseWrapper } from '../../shared';
import { SchedulingModel, Parameters, Scheduling } from '../scheduling/scheduling.component';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { Observable } from 'rxjs/Observable';
import { CommonService } from '../common.service';
import { ConfirmDialogComponent } from '../scheduling/confirm.dialog.component';
import "rxjs/add/operator/takeWhile";
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { fadeInContent } from '@angular/material/typings/select/select-animations';
declare var $: any;
declare var jQuery: any;
//#endregion
@Component({
    selector: 'jhi-report-run-wizard',
    templateUrl: './run-wizard.component.html'
})

export class RunReportWizardComponent implements OnDestroy, OnInit{
    @Input('selectedReportId') selectedReportId: string;
    @Input('selectedReportName') selectedReportName: string;
    @Output() toggleDialog: EventEmitter<boolean> = new EventEmitter<boolean>();
    private unsubscribe: Subject<void> = new Subject();
    reportInfo = new ReportColsInfo();
    periodsList=[];
    asofDateChange: boolean = false;
    showSegFilters: boolean = false;
    timer:any;
    disableRecStatus: boolean = true;
    disableAccStatus: boolean = true;
    disableRecStatusAll: boolean = false;
    disableAccStatusAll: boolean = false;
    showProcessing:boolean=false;
    blockUI: boolean = false;
    rptSubmitted: boolean = false;
    srcViews: any[] = []; 
    selectedviewTemp: any[]=[];
    fnlObj;
    request:any;
    adnlParametersSubmited: ParametersSubmited[] = [];
    oldParams: ParametersSubmited[] = [];
    accStatusList: LookUpCode[] = []; 
    recStatusList: LookUpCode[] = [];    
    today = new Date();
    sysReqName:string='';
    
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

    constructor(
        private reportsService: ReportsService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private cs: CommonService,
        public datepipe: DatePipe,
        private dialog: MdDialog,
    ){
        this.loadDataviews();
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

    loadDataviews(){
        this.reportsService.getDataViewsList().takeUntil(this.unsubscribe).subscribe((dtViews: DataViews[]) => {
            for (let i = 0, len = dtViews.length; i < len; i++) {
                const newView = {
                    "id": i,
                    "itemName": dtViews[i].dataViewDispName,
                    "dataName": dtViews[i].id
                };
                this.srcViews.push(newView);
            }
        },
            (res: Response) => {
                this.onError('Failed to load Data Views list!')
            });
    }

    updateSelDataview(){
        this.srcViews.forEach((element) => {
            if(element.dataName==this.reportInfo.trgtViewId){
                this.reportInfo.trgtViewId=element.dataName;
                this.selectedviewTemp=[];
                this.selectedviewTemp.push(element);
            }
        });
    }

    trgtViewChanged(){
        this.reportInfo.trgtViewId=this.selectedviewTemp[0].dataName;
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
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
        this.rptSubmitted = true;
        if(this.reportInfo.reportTypeCode=='ROLL_BACK_REPORT'){
            this.fnlObj.dateTimeVal=new Date(this.fnlObj.dateTimeVal);
        }else if (this.fnlObj.dateTimeVal != this.today) {
            this.fnlObj.dateTimeVal.setDate(this.fnlObj.dateTimeVal.getDate() + 1);
        }
    }


    displayCancelButton(){
        this.timer=setTimeout(()=>{ 
            if(this.blockUI){
                this.showProcessing = true;
            }
        }, 4000);
    }

    getReqInfo(reqId) {
        this.request = this.reportsService.getRequestInfoByReqId(reqId).takeUntil(this.unsubscribe).subscribe((res)=>{
            this.buildParameters(res.filterMap);
            this.reportSelected(this.selectedReportId);
        })
    }

    reportSelected(reportId: any) {
        this.reportInfo = new ReportColsInfo();
        this.disableRecStatus = true;
        this.disableAccStatus = true;
        this.blockUI=true;
        this.reportsService.getReportFieldsInfo(reportId).takeUntil(this.unsubscribe).subscribe((res: ReportColsInfo) => {      //To load selected report definition
            this.reportInfo = res;
            this.updateSelDataview();
            if(this.reportInfo.reportTypeCode&&(this.reportInfo.reportTypeCode=='AGING_REPORT'||this.reportInfo.reportTypeCode=='ROLL_BACK_REPORT')){
                this.reportInfo.categorizeBy='RECONCILIATION';
            }
            this.reportInfo.dateTimeVal = this.today;                     //Default report date is current date
            this.reportInfo.outputType = 'TABLE';                         //Default report output type
            const tempVar = res;
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
            this.updateSelDataview();
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
                            // Close dialog
                            return;
                        }
                    });
                }
            }else{
                this.bindFieldControlsInfo(tempVar,reportId);
            }
            this.toggleDialog.emit(true);
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
                    this.oldParams.forEach((element) => {
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
                this.oldParams.forEach((element) => {
                    if(element.ParamId==this.reportInfo.fields[i].rParamId){
                        tempVar.fields[i].selectedValue = new Date(element.Value);
                    }
                });
            }else if (tempVar.fields[i].fieldType === 'MONTH_RANGE') {
                tempVar.fields[i].selectedValue = { 'fromDate': undefined, 'toDate': new Date() };
            }
            this.oldParams.forEach((ele)=>{        //if user is going to update parameter values
                if(ele.Parameter=='Report Date'){
                    this.reportInfo.dateTimeVal=new Date(ele.Value);
                }
            })
        }
    }

    buildParameters(fnlObj) {
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
        this.oldParams=destArray;
    }

    runReport() {
        this.reportsService.testOozieServer().takeUntil(this.unsubscribe).subscribe((oozieStatus: OozieStatus) => {
            if (oozieStatus && oozieStatus.dbStatus && oozieStatus.ooziestatus) {
                this.removeLovSrcList();
                let changedToChart: boolean = false;
                if (this.fnlObj.outputType == 'CHART') {           //Need to remove this condition check after original chart functionality added
                    this.fnlObj.outputType = 'PIVOT';
                    changedToChart = true;
                }
                this.displayCancelButton();
                this.blockUI = true;
                this.sysReqName=this.selectedReportName+(new Date()).getTime();
                this.fnlObj.outputCols = [];
                this.request = this.reportsService.getReportOutputFirstTime(this.sysReqName, this.selectedReportId, this.selectedReportName, this.fnlObj,
                    undefined, {
                        page: 0,
                        size: 25
                    }).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                        this.rptSubmitted = false;
                        this.blockUI = false;
                        this.showProcessing = false;
                        clearTimeout(this.timer);
                        this.toggleDialog.emit(false);
                        if (!res.json().requestInfo || !res.json().requestInfo.idForDisplay) {
                            this.showProcessing = false;
                            clearTimeout(this.timer);
                            this.blockUI = false;
                            this.cs.error('Sorry!', 'Request failed to run');
                            return;
                        }
                    }, (res: Response) => {
                        this.blockUI = false;
                        this.rptSubmitted = false;
                        this.showProcessing = false;
                        clearTimeout(this.timer);
                        this.cs.error('Sorry!', 'Request failed to run');
                    });

            } else {
                this.cs.error('Sorry!', 'Request failed please try again!');
            }
        },(res: any)=>{
            this.cs.error('Sorry!', 'Request failed please try again!');
        });
    }


    ngOnInit(){

    }

    ngOnDestroy(){
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.cs.destroyNotification(); 
    }

    private onError(errorMessage) {
        this.cs.error(
            'Error!',
            errorMessage
        )
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
        this.blockUI=false;
    }
}