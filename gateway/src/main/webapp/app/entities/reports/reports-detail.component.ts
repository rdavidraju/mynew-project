//#region Import section
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, Subject } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { Reports, ReportTypes, DataViews, DefinitionColsInfo, DataViewInfo, AgingBucket, AgingBucketDetails } from './reports.model';
import { ReportsService } from './reports.service';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { CommonService } from '../../entities/common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import "rxjs/add/operator/takeWhile";
import { concat } from 'rxjs/operator/concat';
import { fadeInContent } from '@angular/material/typings/select/select-animations';
import { repeat } from 'rxjs/operator/repeat';
import { BoundCallbackObservable } from 'rxjs/observable/BoundCallbackObservable';
import { iterateListLike } from '@angular/core/src/change_detection/change_detection_util';
import { retry } from 'rxjs/operator/retry';
import { checkNoChangesNode } from '@angular/core/src/view/view';

declare var $: any;
declare var jQuery: any;
//#endregion

@Component({
    selector: 'jhi-reports-detail',
    templateUrl: './reports-detail.component.html'
})
export class ReportsDetailComponent implements OnDestroy, OnInit {
    //#region Parameters section
    private unsubscribe: Subject<void> = new Subject();
    report = new Reports();                     // It holds Complete report definition
    selView = new DataViewInfo();               // It holds Selected Dataview
    srcViews: any[] = [];                 // It holds list of all dataviews
    selReportType = new ReportTypes();          // It holds selected report type and definition
    srcReportTypes: ReportTypes[] = [];         // It holds list of all report types
    parameterTypes: LookUpCode[] = [];          // It holds list of all parameters types
    public sysColumns: DefinitionColsInfo[] = [];      // It holds list of all report(System columns) type columns
    public sysColumnsTemp: DefinitionColsInfo[] = [];      // It holds list of all report(System columns) type columns
    public viewColumns: DefinitionColsInfo[] = [];     // It holds list of all columns of selected dataview
    public viewColumnsTemp: DefinitionColsInfo[] = [];     // It holds list of all columns of selected dataview
    isCreateNew: boolean = false;               // It holds form state is in create new
    isCopy: boolean = false;
    isEdit: boolean = false;                    // It holds form state is in edit
    isViewOnly: boolean = false;                // It holds form state is in view
    expandLayOut: boolean = true;
    isSubmitted: boolean = false;
    routeSub: any;
    // selViewName:String = '';
    // selTrgtViewName:String ='';
    reportOutput: any[] = [];                   // It holds all report output types
    public parametersList: Array<DefinitionColsInfo> = [];            // It holds all selected parameter columns list
    tableReportColList: Array<DefinitionColsInfo> = [];        // It holds all selected tabular output columns list
    groupByColumnsList: Array<DefinitionColsInfo> = [];        // It holds all selected groupby columns list
    conditionsList: Array<DefinitionColsInfo> = [];            // It holds all selected conditions columns list
    reportAgrigateColList: Array<DefinitionColsInfo> = [];        // It holds all selected tabular output columns list
    reportSummaryColList: Array<DefinitionColsInfo> = [];        // It holds all selected tabular output columns list
    fullScreenHeight: any;                      // It holds full screen height
    finalColsList:DefinitionColsInfo[]=[];      // It holds all final columns
    srcAgingBuckets: AgingBucket[]=[];          // It holds all source aging buckets
    selectedBucket = new AgingBucket();          // It holds current aging buckets
    selctdBucketsList: DefinitionColsInfo[]=[]; // It holds all selected aging buckets
    tbAccColsList: DefinitionColsInfo[]=[];     // It holds all selected aging buckets
    selctdRoleBackList: DefinitionColsInfo[]=[];// It holds all selected Rollback list
    today=new Date();                           // It holds current date
    srcDecimalCols:DefinitionColsInfo[]=[];     // It holds all list of decimal columns
    srcDateCols:DefinitionColsInfo[]=[];        // It holds all list of date columns
    
    accStatusList: LookUpCode[] = [];    
    recStatusList: LookUpCode[] = [];  
    roleCntMinMax:number=12;
    openFormulaDialog: boolean = false;
    expressionColsSrc:DefinitionColsInfo[]=[];
    expressionColsDest:DefinitionColsInfo[]=[];
    selectedviewTemp: any;
    selectedtrgtviewTemp: any;
    dataLoaded=false;

    singleSlectSettings = { 
        singleSelection: true, 
        text:" ",
        enableSearchFilter: true,
        classes:"cuppaSingleSelection"
      };
    //#endregion
    constructor(
        private eventManager: JhiEventManager,
        private reportsService: ReportsService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService
    ) {
        
    }
    
    ngOnInit() {
        this.report.reportViewType = 'TABLE';
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
        this.routeSub = this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            const presentPath = this.route.snapshot.url;
            //Check is 'ID' is there in route params or not
            if (params['id']) {
                //Load report definition for ID in route params
                this.reportsService.getReportDefinition(params['id']).takeUntil(this.unsubscribe).subscribe((resReports: Reports) => {
                    this.report = resReports;
                    //Bind all columns in respective arrays
                    this.report.columnsDefinition.forEach((col)=>{
                        //parameter columns
                        if(col.parameterName){
                            if(col.parameterName.length>0){
                                this.parametersList.push(col);
                            }
                        }
                        //layout columns
                        if(col.layoutValue&&col.layoutValue.match(/^-?\d+$/)){
                            if(col.columnType=='AGING'){
                                this.selctdBucketsList.push(col);
                            }else if(col.columnType=='ROLL_BACK'){
                                this.selctdRoleBackList.push(col);
                            }else if(col.columnType=='NATURAL_ACCOUNT'){
                                this.tbAccColsList.push(col);
                            }else{
                                if(this.report.reportType == 'ACCOUNT_ANALYSIS_REPORT' && this.report.reportMode=='SUMMARY'){
                                    if(col.dataType=='DECIMAL'){
                                        this.reportAgrigateColList.push(col);
                                    }else{
                                        this.reportSummaryColList.push(col);
                                    }
                                }else{
                                    this.tableReportColList.push(col);
                                }
                            }
                        }
                        //conditions
                        if(col.conditionalValue&&col.conditionalValue.length>0){
                            this.conditionsList.push(col);
                        }
                        //Groupby
                        if(col.groupBy){
                            this.groupByColumnsList.push(col);
                        }
                    })
                    //Load all selection types
                    this.reportsService.getLookupValues('FORM_CONTROLS').takeUntil(this.unsubscribe).subscribe((resParamTypes: LookUpCode[]) => {
                        this.parameterTypes = resParamTypes;
                    },
                    (resParamTypesErr: Response) => {
                      this.onError('Failed to load Parameters Selection Types list!')
                    });
                    //Load all aging buckets
                    this.reportsService.getAllBuckets().takeUntil(this.unsubscribe).subscribe((allBuckets: AgingBucket[]) => {
                        this.srcAgingBuckets = allBuckets;
                    },
                    (resAllBucketsErr: Response) => {
                      this.onError('Failed to load Buckets list!')
                    });
                    //Load all report types
                    this.reportsService.getReportTypes().takeUntil(this.unsubscribe).subscribe((rptTypes: ReportTypes[]) => {
                        this.srcReportTypes = rptTypes;
                        this.dataLoaded=true;
                        if(!url.endsWith('edit')){
                            this.isViewOnly = true;
                        }
                        this.srcReportTypes.forEach((type)=>{
                            type.typeDisplayName1=type.typeDisplayName.replace(' Report','');
                            if(type.id==this.report.reportTypeId){
                                this.selReportType=type;
                                this.reportTypeChanged();
                            }
                        });
                        //Load all data views list
                        // this.selectedviewTemp=[];
                        // this.selectedtrgtviewTemp=[];
                        this.reportsService.getDataViewsList().takeUntil(this.unsubscribe).subscribe((dtViews: DataViews[]) => {
                            this.srcViews=dtViews;
                            for(let i=0, len=dtViews.length;i<len;i++){
                                if(this.report.sourceViewId==dtViews[i].id){
                                    this.viewChanged();
                                    break;
                                }
                            }
                            
                            // for(let i=0, len=dtViews.length;i<len;i++){
                            //     const newView={
                            //         "id": i,
                            //         "itemName": dtViews[i].dataViewDispName,
                            //         "dataName": dtViews[i].id
                            //     };
                            //     this.srcViews.push(newView);
                            //     if(this.report.sourceViewId==dtViews[i].id){
                            //         this.selectedviewTemp=newView;
                            //         this.selViewName=dtViews[i].dataViewDispName;
                            //         this.viewChanged();
                            //     }
                            //     if(this.report.trgtViewId){
                            //         if(this.report.trgtViewId==dtViews[i].id){
                            //             this.selectedtrgtviewTemp=newView;
                            //             this.selTrgtViewName=dtViews[i].dataViewDispName;
                            //             this.trgtViewSelected();
                            //         }
                            //     }
                            // }
                            if (url.endsWith('edit')) {
                                this.enableEditForm();
                            }else{
                                this.isViewOnly = true;
                                this.expandLayOut=true;
                            } 
                        },
                        (res: Response) => {
                        this.onError('Failed to load Data Views list!')
                        });
                    },
                    (resDtViewsErr: Response) => {
                      this.onError('Failed to load Report Types list!')
                    });
                    
                    
                },
                (resReportsErr: Response) => {
                  this.onError('Failed to load Report definition!')
                });
            }else {
                this.isCreateNew = true;
                this.report.startDate=this.today;   //set start date as today
                //Load all selection types
                this.reportsService.getLookupValues('FORM_CONTROLS').takeUntil(this.unsubscribe).subscribe((resParamType: LookUpCode[]) => {
                    this.parameterTypes = resParamType;
                },
                (resParamTypeErr: Response) => {
                  this.onError('Failed to load Parameters Selection Types list!')
                });
                //Load all aging buckets
                this.reportsService.getAllBuckets().takeUntil(this.unsubscribe).subscribe((resBuckets: AgingBucket[]) => {
                    this.srcAgingBuckets = resBuckets;
                },
                (resBucketsErr: Response) => {
                  this.onError('Failed to load Buckets list!')
                });
                //Load all report types
                this.reportsService.getReportTypes().takeUntil(this.unsubscribe).subscribe((resReportType: ReportTypes[]) => {
                    this.srcReportTypes = resReportType;
                    this.dataLoaded=true;
                    for(let i=0;i<this.srcReportTypes.length;i++){
                        this.srcReportTypes[i].typeDisplayName1=this.srcReportTypes[i].typeDisplayName.replace(' Report','');
                    }
                    if(params['type']){
                        for(let i=0;i<this.srcReportTypes.length;i++){
                            if(this.srcReportTypes[i].type==params['type']){
                                this.selReportType=this.srcReportTypes[i];
                                this.reportTypeChanged();
                            }
                        }
                    }else if(this.srcReportTypes.length>0){
                        this.selReportType=this.srcReportTypes[0];
                        this.reportTypeChanged();
                    }
                },
                (resReportTypeErr: Response) => {
                  this.onError('Failed to load Report Types list!')
                });
                //Load all dataviews list
                this.reportsService.getDataViewsList().takeUntil(this.unsubscribe).subscribe((resDtViews: DataViews[]) => {
                    this.srcViews=resDtViews;
                    // for(let i=0, len=resDtViews.length;i<len;i++){
                    //     this.srcViews.push({
                    //         "id": i,
                    //         "itemName": resDtViews[i].dataViewDispName,
                    //         "dataName": resDtViews[i].id
                    //     });
                    // }
                },
                (resDtViewsErr: Response) => {
                  this.onError('Failed to load Data Views list!')
                });
            }
        });
    }

    public ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.commonService.destroyNotification(); 
    }

    //If user changes bucket
    bucketChanged(){
        this.selctdBucketsList=[];
        if(this.report.agingBucketId<1){
            this.report.agingBucketId=undefined;
            return;
        }
        this.reportsService.getBucketDef(this.report.agingBucketId).takeUntil(this.unsubscribe).subscribe((res: AgingBucket) => {
            this.selectedBucket=res;
            res.bucketDetDataList.forEach((element) => {
                const newItem = new DefinitionColsInfo();
                newItem.ColumnId = element.id;
                if(res.type=='VALUE'){
                    newItem.viewColDisplayName = 'Value('+element.fromValue+' - '+element.toValue+')';
                    newItem.layoutDisplayName='Value('+element.fromValue+' - '+element.toValue+')';
                }else if(res.type=='AGE'){
                    newItem.viewColDisplayName = 'Days('+element.fromValue+' - '+element.toValue+')';
                    newItem.layoutDisplayName='Days('+element.fromValue+' - '+element.toValue+')';
                }
                newItem.columnType = 'AGING';
                this.selctdBucketsList.push(newItem);
            });
        },
        (res: Response) => {
          this.onError('Failed to load bucket definition!')
        });
    }


    //It will Check is report name is available or not
    checkReportName() {
        if (!this.report.reportName || this.report.reportName.length < 1){
            return;
        }
        this.reportsService.checkReportNameAvailable(this.report.reportName).takeUntil(this.unsubscribe).subscribe((resVal: string) => {
            if (resVal.length> 0) {
                if (this.report.id == resVal) {
                    return;
                } else {
                    this.report.reportName = '';
                    this.commonService.error('Oops...!', 'Sorry, report name already exists.');
                }
            }
        },
        (resErr: Response) => {
          this.onError('Failed to check report name availability!');
        });
    }

    checkBeforeSave(){
        if(this.parametersList.length<1){
            this.onError('Report should contain atleast one parameter!');
            return;
        }else if(this.report.reportType=='AGING_REPORT' && this.selctdBucketsList.length<1){
            this.onError('Selected bucket group should have atleast one bucket!');
            return;
        }else if(this.tableReportColList.length<1){
            if(this.report.reportType == 'ACCOUNT_ANALYSIS_REPORT' && this.report.reportMode=='SUMMARY'){
                if(this.reportSummaryColList.length<1&&this.reportAgrigateColList.length<1){
                    this.onError('Report should have atleast one column in output view!');
                    return;    
                }
            }else{
                this.onError('Report should have atleast one column in output view!');
                return;
            }
        }
        let flag:boolean=true;
        for(let k=0, len=this.parametersList.length;k<len;k++){
            if(this.parametersList[k].isMandatory){
                flag=false;
                break;
            }
        }
        if(flag){
            this.onError('Report should have atleast one Mandatory parameter!');
            return;
        }
        this.reportsService.checkReportNameAvailable(this.report.reportName).takeUntil(this.unsubscribe).subscribe((resVal: string) => {
            if (resVal.length> 0) {
                if (this.report.id == resVal) {
                    this.saveReport();
                } else {
                    this.report.reportName = '';
                    this.commonService.error('Oops...!', 'Sorry, report name already exists.');
                    return;
                }
            }else{
                this.saveReport();
            }
        },
        (resErr: Response) => {
          this.onError('Failed to check report name availability!');
        });
    }

    //To Save Report Definition
    saveReport() {  
        //#region  Add all types columns in to final columns list 
        this.finalColsList=[];                       
        const paramListDup=[...this.parametersList];                        // Creating duplicate array 
        if(this.report.reportType=='TRAIL_BALANCE_REPORT'){
            for(let k=this.tbAccColsList.length-1;k>=0;k--){
                this.tableReportColList.unshift(this.tbAccColsList[k]);
            }
        }
        if(this.report.reportType=='AGING_REPORT'){                       //Set column order for aging bucket columns, these columns will come at end in layout
            const tmp=this.tableReportColList.length;
            let count=1;
            this.selctdBucketsList.forEach((element) => {
                element.layoutValue=(tmp+count).toString();
                count++;
            });
            this.finalColsList=paramListDup.concat(this.selctdBucketsList);
        }else if(this.report.reportType=='ROLL_BACK_REPORT'){                       //Set column order for roleback columns, these columns will come at end in layout
            const tmp=this.tableReportColList.length;
            let count=1;
            this.selctdRoleBackList.forEach((element) => {
                element.layoutValue=(tmp+count).toString();
                count++;
            });
            this.finalColsList=paramListDup.concat(this.selctdRoleBackList);
        }else{
            this.report.agregator=undefined;
            this.report.dateQualifier=undefined;
            this.finalColsList=paramListDup;
        }
        this.conditionsList.forEach((element) => {
            let flag=false;
            this.finalColsList.forEach((item)=>{
                if(item.ColumnId==element.ColumnId && item.columnType==element.columnType){
                    item.LOV=undefined;
                    item.conditionalOperator=element.conditionalOperator;
                    item.conditionalValue=element.conditionalValue;
                    flag=true;
                }
            })
            if (!flag){
                element.LOV=undefined;
                this.finalColsList.push(element);
            }
        });
        this.groupByColumnsList.forEach((grpElement) => {
            let flag=false;
            this.finalColsList.forEach((item)=>{
                if(item.ColumnId==grpElement.ColumnId && item.columnType==grpElement.columnType){
                    item.groupBy=true;
                    flag=true;
                }
            })
            if (!flag){
                grpElement.groupBy=true;
                this.finalColsList.push(grpElement);
            }
        });
        if(this.report.reportType == 'ACCOUNT_ANALYSIS_REPORT' && this.report.reportMode=='SUMMARY'){
            this.reportSummaryColList.forEach((element) => {
                let flag=false;
                this.finalColsList.forEach((item)=>{
                    if(item.ColumnId==element.ColumnId && item.columnType==element.columnType){
                        item.layoutDisplayName=element.layoutDisplayName;
                        item.layoutValue=(this.reportSummaryColList.indexOf(item)+1).toString();
                        flag=true;
                    }
                })
                if (!flag){
                    element.layoutValue=(this.reportSummaryColList.indexOf(element)+1).toString();
                    this.finalColsList.push(element);
                }
            });
            this.reportAgrigateColList.forEach((element) => {
                let flag=false;
                this.finalColsList.forEach((item)=>{
                    if(item.ColumnId==element.ColumnId && item.columnType==element.columnType){
                        item.layoutDisplayName=element.layoutDisplayName;
                        item.layoutValue=(this.reportSummaryColList.length + this.reportAgrigateColList.indexOf(item)+1).toString();
                        flag=true;
                    }
                })
                if (!flag){
                    element.layoutValue=(this.reportSummaryColList.length + this.reportAgrigateColList.indexOf(element)+1).toString();
                    this.finalColsList.push(element);
                }
            });
        }else{
            this.tableReportColList.forEach((element) => {
                let flag=false;
                this.finalColsList.forEach((item)=>{
                    if(item.ColumnId==element.ColumnId && item.columnType==element.columnType){
                        item.layoutDisplayName=element.layoutDisplayName;
                        item.layoutValue=(this.tableReportColList.indexOf(item)+1).toString();
                        flag=true;
                    }
                })
                if (!flag){
                    element.layoutValue=(this.tableReportColList.indexOf(element)+1).toString();
                    this.finalColsList.push(element);
                }
            });
        }
        

        //#endregion
        this.report.columnsDefinition=this.finalColsList;
        //Post report definition
        this.report.reportName.trim();
        this.reportsService.postReportDef(this.report).takeUntil(this.unsubscribe).subscribe((res) => {
            if (res.status === 200) {
                this.commonService.success('Saved!',this.report.reportName + ' saved successfully!')
                this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
            }else {
                this.commonService.error('Oops...!',this.report.reportName + ' not saved! Try again.')
            }
        },
        (res: Response) => {
          this.onError('Failed to save Report definition!')
        });
    }

    // this method will check is given key and value of JSON will exist in array or not
    checkIfExists(arr, key, Val) {
        return arr.some(function(e) {
            return e[key] ? e[key] === Val : false;
        });
    }

    //#region Drag and Drop related code
    //This function will help us to catch column drop event and store that column in respective array
    columnsDragDropSuccess($event: any, destArray: any, ind) {
        if(!$event.dragData || this.isViewOnly){
            return;
        }
        if (this.checkColumnExistence($event.dragData,destArray)){
            $event.dragData.isRecent=true;
            destArray.splice(ind,0,$event.dragData);
            // destArray.push($event.dragData);
        }else{
            this.commonService.info('Warning!','Column already exists')
        }
        
    }

    columnsDragDropSuccessSummary($event: any, destArray: any, ind) {
        console.log($event.dragData);
        if(!$event.dragData || this.isViewOnly){
            return;
        }
        if($event.dragData.dataType==='DECIMAL'){
            this.commonService.info('Warning!','Only other than decimal columns accept here');
            return;
        }
        if (this.checkColumnExistence($event.dragData,destArray)){
            $event.dragData.isRecent=true;
            destArray.splice(ind,0,$event.dragData);
            // destArray.push($event.dragData);
        }else{
            this.commonService.info('Warning!','Column already exists')
        }
        
    }

    columnsDragDropSuccessAggrigate($event: any, destArray: any, ind) {
        console.log($event.dragData);
        if(!$event.dragData || this.isViewOnly){
            return;
        }
        if($event.dragData.dataType!=='DECIMAL'){
            this.commonService.info('Warning!','Only decimal columns accept here');
            return;
        }
        if (this.checkColumnExistence($event.dragData,destArray)){
            $event.dragData.isRecent=true;
            destArray.splice(ind,0,$event.dragData);
            // destArray.push($event.dragData);
        }else{
            this.commonService.info('Warning!','Column already exists')
        }
        
    }

    columnsDragDropSuccessGroupBy($event: any, destArray: any) {
        if(!$event.dragData || this.isViewOnly){
            return;
        }
        if($event.dragData.dataType=='DECIMAL'){
            this.onError('\'Decimal\' data type columns are not eligible for Groupby');
            return;
        }else if($event.dragData.viewColName === 'CODE_COMBINATION' || $event.dragData.viewColName === 'ACCOUNTING_STATUS'|| $event.dragData.viewColName === 'RECON_STATUS'){
            this.commonService.info('Oops..!', 'This column eligible for Report Layout!');
            return;
        }else{
            if (this.checkColumnExistence($event.dragData,destArray)){
                $event.dragData.isRecent=true;
                destArray.push($event.dragData);
            }else{
                this.commonService.info('Warning!','Column already exists')
            }
        }
    }

    //This will work when drag and drop success in parameters section, if success it will add that column in parameters array
    columnsDragDropSuccessParameters($event: any, destArray: any) {
        if (!$event.dragData || this.isViewOnly){
            return;
        }else if ($event.dragData.dataType === 'DECIMAL') {
            this.commonService.info('Oops..!', 'Decimal columns are eligible for Report Layout and Conditions!');
            return;
        }else if ($event.dragData.dataType === 'DATE' || $event.dragData.dataType === 'DATETIME') {
            this.commonService.info('Oops..!', 'Date columns are eligible for Report Layout!');
            return;
        }else if ($event.dragData.viewColName === 'CODE_COMBINATION' || $event.dragData.viewColName === 'ACCOUNTING_STATUS'|| $event.dragData.viewColName === 'RECON_STATUS') {
            this.commonService.info('Oops..!', 'This column eligible for Report Layout!');
            return;
        }else {
            switch ($event.dragData.dataType) {
                case 'INTEGER':
                    $event.dragData.parameterType = 'MULTI_SELECT_LOV';
                    break;
                case 'DECIMAL':
                    // $event.dragData.parameterType='AMOUNT_RANGE';
                    break;
                case 'DATE':
                case 'DATETIME':
                    // $event.dragData.parameterType='DATE_RANGE';
                    break;
                case 'VARCHAR':
                    $event.dragData.parameterType = 'MULTI_SELECT_LOV';
                    break;
                case 'BOOLEAN':
                    $event.dragData.parameterType = 'BOOLEAN';
                    break;
                default:
                    this.onError('This column doese not have data type');
                    break;
            }
            if (!$event.dragData.parameterName){
                $event.dragData.parameterName = $event.dragData.layoutDisplayName;
            }
            if(this.checkColumnExistence($event.dragData,this.conditionsList)){
                if (this.checkColumnExistence($event.dragData, destArray)){
                    $event.dragData.isRecent=true;
                    destArray.push($event.dragData);
                }else{
                    this.commonService.info('Warning!','Column already exists')
                }
            }else{
                this.commonService.info('Warning','Column already exists in \'Conditions\' list');
            }
        }
    }

    //This will work when drag and drop success in conditions section, if success it will add that column in respective array
    columnsDragDropSuccessConditions($event: any, destArray: any) {
        if(!$event.dragData){
            return;
        }
        if ($event.dragData.columnType !== 'DATA_VIEW') {
            this.commonService.info('Oops...!', 'Only \'Data view Columns\' are eligible for \'Conditions\'', );

            return;
        }
        if ($event.dragData.viewColName === 'CODE_COMBINATION' || $event.dragData.viewColName === 'ACCOUNTING_STATUS'|| $event.dragData.viewColName === 'RECON_STATUS') {
            this.commonService.info('Oops..!', 'This column eligible for Report Layout!');
            return;
        }else if(this.checkColumnExistence($event.dragData,this.parametersList)){
            if (this.checkColumnExistence($event.dragData,destArray)){
                if ($event.dragData.dataType === 'DATE' || $event.dragData.dataType === 'DATETIME') {
                    this.commonService.info('Oops..!', 'Date columns are eligible for Report Layout!');
                    return;
                }
                this.reportsService.getLookupValues($event.dragData.dataType).takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
                    let newItem = new DefinitionColsInfo();
                    newItem=$event.dragData;
                    newItem.LOV=res;
                    switch($event.dragData.dataType){
                        case 'INTEGER':
                        // case 'DATE':
                        // case 'DATETIME':
                        case 'DECIMAL':
                                    newItem.conditionalOperator='=';
                        break;
                        case 'VARCHAR':
                                    newItem.conditionalOperator='EQUALS';
                        break;
                        case 'BOOLEAN':
                                    newItem.conditionalOperator='BOOLEAN';
                        break;
                        default:
                                    this.onError('This column doese not have data type');
                        break;
                    }
                    $event.dragData.isRecent=true;
                    destArray.push($event.dragData);
                },
                (res: Response) => {
                //this.onError('res')
                });
            }else{
                this.commonService.info('Warning!','Column already exists')
            }
        }else{
            this.commonService.info('Warning','Column already exists in \'Parameters\' list');
        }
    }
    //#endregion

    //This will work when drag and drop success, if success it will check is column is already there in destination arry
    checkColumnExistence(dragData:any,srcArray:any[]):boolean{
        let i=srcArray.length-1;
        for(;i>=0;i--){
            if (srcArray[i].ColumnId == dragData.ColumnId && srcArray[i].columnType == dragData.columnType) {                   // Check is column exists or not if exists
                srcArray[i].isRecent=true;
                return false;
            }else{
                srcArray[i].isRecent=false;
            }
        }
        if(i<0){
            return true;
        }
        return false;
    }

    //This will check column name is available or not
    checkNameExistence(item:any,srcArray:any[],lable:string,ind:number){
        if(!item[lable] || ((item[lable]).trim()).length<1){
            item[lable]=item.viewColDisplayName;
        }
        for(let i=0;i<srcArray.length;i++){
            if( i!=ind && srcArray[i][lable]==item[lable]){
                if(item[lable]==undefined){
                    return;
                }
                this.commonService.info('Warning!',item[lable] + '" already exists');
                item[lable]=undefined;
                return;
            }
        }
    }
    test(){
    }

    checkNameExistenceOnDrop(item:any){
        if(!item){
            return;
        }
        for(let i=0;i<this.tableReportColList.length;i++){
            if( !(this.tableReportColList[i].ColumnId == item.ColumnId && this.tableReportColList[i].columnType == item.columnType)){
                if (item.layoutDisplayName == this.tableReportColList[i].layoutDisplayName) {
                    item.layoutDisplayName=item.layoutDisplayName+'_1';
                }
            }
        }
        for(let i=0;i<this.selctdBucketsList.length;i++){
            if( !(this.selctdBucketsList[i].ColumnId == item.ColumnId && this.selctdBucketsList[i].columnType == item.columnType)){
                if (item.layoutDisplayName == this.selctdBucketsList[i].layoutDisplayName) {
                    item.layoutDisplayName=item.layoutDisplayName+'_1';
                }
            }
        }
        for(let i=0;i<this.selctdRoleBackList.length;i++){
            if( !(this.selctdRoleBackList[i].ColumnId == item.ColumnId && this.selctdRoleBackList[i].columnType == item.columnType)){
                if (item.layoutDisplayName == this.selctdRoleBackList[i].layoutDisplayName) {
                    item.layoutDisplayName=item.layoutDisplayName+'_1';
                }
            }
        }
    }

    // this will work at report type changed
    reportTypeChanged() {
        this.report.reportTypeId = this.selReportType.id;
        this.report.reportType = this.selReportType.type;
        if(this.isCreateNew){
            this.report.reportMode = undefined;
            this.report.show = undefined;
            this.report.reconcile = undefined;
        }
        if (this.selReportType.mode && this.isCreateNew){
            this.report.reportMode = "DETAIL";
        }
        if (this.selReportType.show && this.isCreateNew){
            this.report.show = "Both";
        }
        if (this.selReportType.reconcile && this.isCreateNew){
            this.report.reconcile = "Both";
        }
        if(this.report.reportType == 'ROLL_BACK_REPORT'){
            if(!this.isViewOnly){
                this.report.rollBackType='Months';
                this.report.rollBackCount=0;
                this.report.allowSequence=true;
                this.updateOffset();
            }
        }
        
        if(this.isCreateNew){
            this.sysColumns = [];
            this.selctdBucketsList=[];
            this.selctdRoleBackList=[];
            this.reportAgrigateColList=[];
            this.reportSummaryColList=[];
            this.tableReportColList = [];
            this.tbAccColsList = [];
            //Remove existing system columns
            for (let i = 0; i < this.parametersList.length; i++) {
                if (this.parametersList[i].columnType == 'FIN_FUNCTION') {
                    this.parametersList.splice(i, 1);
                    i--;
                }
            }
            for (let i = 0; i < this.conditionsList.length; i++) {
                if (this.conditionsList[i].columnType == 'FIN_FUNCTION') {
                    this.conditionsList.splice(i, 1);
                    i--;
                }
            }
            for (let i = 0; i < this.groupByColumnsList.length; i++) {
                if (this.groupByColumnsList[i].columnType == 'FIN_FUNCTION') {
                    this.groupByColumnsList.splice(i, 1);
                    i--;
                }
            }
            for (let i = 0; i < this.tableReportColList.length; i++) {
                if (this.tableReportColList[i].columnType == 'FIN_FUNCTION') {
                    this.tableReportColList.splice(i, 1);
                    i--;
                }
            }
            
            for (let i = 0; i < this.srcDecimalCols.length; i++) {
                if (this.srcDecimalCols[i].columnType == 'FIN_FUNCTION') {
                    this.srcDecimalCols.splice(i, 1);
                    i--;
                }
            }
            for (let i = 0; i < this.srcDateCols.length; i++) {
                if (this.srcDateCols[i].columnType == 'FIN_FUNCTION') {
                    this.srcDateCols.splice(i, 1);
                    i--;
                }
            }
        }
        
        this.reportsService.getLookupValues(this.report.reportType).takeUntil(this.unsubscribe).subscribe((LkpCodes: LookUpCode[]) => {
            LkpCodes.forEach((item) => {
                const newItem = new DefinitionColsInfo();
                newItem.ColumnId = item.id;
                newItem.viewColName = item.lookUpCode;
                newItem.viewColDisplayName = item.meaning;
                newItem.layoutDisplayName = item.meaning;
                newItem.parameterName = item.meaning;
                newItem.columnType = 'FIN_FUNCTION';
                newItem.dataType=item.description;
                newItem.isRecent=false;
                if (this.isCreateNew) {
                    this.tableReportColList.push(newItem);
                }
                if(item.description=='DECIMAL'){
                    this.srcDecimalCols.push(Object.assign(new DefinitionColsInfo(),newItem));
                }
                if(item.description=='DATE' || item.description=='DATETIME'){
                    this.srcDateCols.push(Object.assign(new DefinitionColsInfo(),newItem));
                }
                this.sysColumns.push(newItem);
                this.sysColumnsTemp.push(newItem);
                if(this.report.reportType === 'TRAIL_BALANCE_REPORT' && (!this.isViewOnly) && newItem.viewColName=='Ledger_Name'){
                    newItem.isMandatory=true;
                    newItem.isRequired=true;
                    if(this.isCreateNew){
                        newItem.parameterType = 'SINGLE_SELECT_LOV';
                        this.parametersList.push(newItem);
                    }
                }
            });
        },
        (LkpCodesErr: Response) => {
          this.onError('Failed to load Report Type definition!')
        });
    }

    /* Delete Functions */
    columnDeleteFunction(col:any, ind:any, mySrcArray:any[]){
        mySrcArray.splice(ind,1);
    }

    /* Remove parameters col instance in report layout columns (If exists) */
    cleanUpParametersInLayoutCols(columnId:any,columnType:any){
        this.tableReportColList.forEach((item)=>{
            if(columnId==item.ColumnId && columnType==item.columnType){
                item.parameterType=undefined;
                item.parameterName=undefined;
                item.isMandatory=undefined;
            }
        })
    }

    /* Remove parameters col instance in report layout columns (If exists) */
    cleanUpConditionsInLayoutCols(columnId:any,columnType:any){
        this.tableReportColList.forEach((item)=>{
            if(columnId==item.ColumnId && columnType==item.columnType){
                item.conditionalOperator=undefined;
                item.conditionalValue=undefined;
            }
        })
    }

    
    // viewSelected(){
    //     this.report.sourceViewId=this.selectedviewTemp.dataName;
    //     $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
    //     this.viewChanged();
    // }
    
    // trgtViewSelected(){
    //     this.report.trgtViewId=this.selectedtrgtviewTemp.dataName;
    //     $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
    // }
    
    // this will work at data view changed
    viewChanged() {
        if(this.isCreateNew || this.isEdit){
            //Remove existing system columns
            for(let i=0;i<this.parametersList.length;i++){
                if (this.parametersList[i].columnType == 'DATA_VIEW') {
                    this.parametersList.splice(i,1);
                    i--;
                }
            }
            for(let i=0;i<this.conditionsList.length;i++){
                if (this.conditionsList[i].columnType == 'DATA_VIEW') {
                    this.conditionsList.splice(i,1);
                    i--;
                }
            }
            for(let i=0;i<this.groupByColumnsList.length;i++){
                if (this.groupByColumnsList[i].columnType == 'DATA_VIEW') {
                    this.groupByColumnsList.splice(i,1);
                    i--;
                }
            }
            for(let i=0;i<this.tableReportColList.length;i++){
                if (this.tableReportColList[i].columnType == 'DATA_VIEW') {
                    this.tableReportColList.splice(i,1);
                    i--;
                }
            }
            for(let i=0;i<this.reportSummaryColList.length;i++){
                if (this.reportSummaryColList[i].columnType == 'DATA_VIEW') {
                    this.reportSummaryColList.splice(i,1);
                    i--;
                }
            }
            for(let i=0;i<this.reportAgrigateColList.length;i++){
                if (this.reportAgrigateColList[i].columnType == 'DATA_VIEW') {
                    this.reportAgrigateColList.splice(i,1);
                    i--;
                }
            }

            this.viewColumns=[];
            this.report.dateQualifier=undefined;
            this.report.agregator=undefined;
            this.tbAccColsList=[];
            if(this.report.reportType=='TRAIL_BALANCE_REPORT'){
                this.reportsService.getSegmentByViewIdAndQaulifier(this.report.sourceViewId,'NATURAL_ACCOUNT').subscribe((res)=>{
                    if(res){
                        const newItem = new DefinitionColsInfo();
                        newItem.ColumnId = res.id;
                        newItem.viewColName = res.segmentName;
                        newItem.viewColDisplayName = res.segmentName;
                        newItem.layoutDisplayName = res.segmentName;
                        newItem.parameterName = res.segmentName;
                        newItem.columnType = 'NATURAL_ACCOUNT';
                        newItem.dataType='VARCHAR';
                        newItem.isRecent=false;
                        this.tbAccColsList.push(newItem);
                        const newItem1 = new DefinitionColsInfo();
                        newItem1.ColumnId = res.id+1;
                        newItem1.viewColName = res.segmentName+' Description';
                        newItem1.viewColDisplayName = res.segmentName+' Description';
                        newItem1.layoutDisplayName = res.segmentName+' Description';
                        newItem1.parameterName = res.segmentName+' Description';
                        newItem1.columnType = 'NATURAL_ACCOUNT';
                        newItem1.dataType='VARCHAR';
                        newItem1.isRecent=false;
                        this.tbAccColsList.push(newItem1);
                    }
                })
            }
        }
        this.reportsService.getColsListByDVid(this.report.sourceViewId).takeUntil(this.unsubscribe).subscribe((res: DataViewInfo) => {
            this.selView = res;
            for(let i=0; i<this.srcDecimalCols.length;i++){
                if(this.srcDecimalCols[i].columnType=='DATA_VIEW'){
                    this.srcDecimalCols.splice(i,1);
                    i--;
                }
            }
            for(let i=0; i<this.srcDateCols.length;i++){
                if(this.srcDateCols[i].columnType=='DATA_VIEW'){
                    this.srcDateCols.splice(i,1);
                    i--;
                }
            }
            let columnList=[];
            if (this.selView.viewRelation == 'UNION') {
                columnList = this.selView.dataViewsUnionColumnsList;
            }else {
                columnList = this.selView.dataViewsColumnsList;
            }
            columnList.forEach((item) => {
                const newItem = new DefinitionColsInfo();
                newItem.ColumnId = item.id;
                newItem.viewColName = item.columnHeader;
                newItem.viewColDisplayName = item.columnName;
                newItem.layoutDisplayName = item.columnName;
                newItem.parameterName = item.columnName;
                newItem.columnType = 'DATA_VIEW';
                newItem.dataType=item.colDataType;
                
                if(item.colDataType=='DECIMAL'){
                    const decNewItem=Object.assign(new DefinitionColsInfo(),newItem);
                    if(item.qualifier){
                        if(item.qualifier=='AMOUNT'){
                            this.report.agregator=decNewItem;
                        }
                    }
                    this.srcDecimalCols.push(decNewItem);
                }
                if(item.colDataType=='DATE' || item.colDataType=='DATETIME'){
                    const dateNewItem=Object.assign(new DefinitionColsInfo(),newItem);
                    if(item.qualifier){
                        if(item.qualifier=='TRANSDATE'){
                            this.report.dateQualifier=dateNewItem;
                        }
                    }
                    this.srcDateCols.push(dateNewItem);
                }
                this.viewColumns.push(newItem);
                this.viewColumnsTemp.push(newItem);
            });
        },
        (res: Response) => {
          this.onError('Failed to load Data View definition!');
        });
    }
    
    //It will work if user update rollback count
    updateOffset(){
        //If user update value to opposit sign of current value
        if (this.selctdRoleBackList.length > 1) {   
            if( (+(this.selctdRoleBackList[1].viewColDisplayName) > 0 && this.report.rollBackCount < 0)||(+(this.selctdRoleBackList[1].viewColDisplayName) < 0 && this.report.rollBackCount > 0)){
                this.selctdRoleBackList = [];
            }
        }
        const newCount=Math.abs(this.report.rollBackCount)+1;
        if (this.selctdRoleBackList.length > newCount) {
            // this.selctdRoleBackList.splice(this.selctdRoleBackList.length - 1, 1);
            this.selctdRoleBackList.length=newCount;
            return;
        } 
        let dsplyName='Current ';
        switch(this.report.rollBackType){
            case 'Days': dsplyName="Selected Day"; break;
            case 'Months': dsplyName="Selected Month"; break;
            case 'Years': dsplyName="Selected Year"; break;
        }
        if (this.selctdRoleBackList.length < newCount) {
            let cnt=this.selctdRoleBackList.length;
            for(;cnt<newCount;cnt++){
                const newItem = new DefinitionColsInfo();                
                if(cnt==0){
                    newItem.viewColDisplayName = '0';
                    newItem.layoutDisplayName = dsplyName;
                }else {
                    if(this.report.rollBackCount<0){
                        newItem.viewColDisplayName = '-'+(cnt).toString();
                        newItem.layoutDisplayName = dsplyName+'(-'+(cnt).toString()+')';
                    }else {
                        newItem.viewColDisplayName = (cnt).toString();
                        newItem.layoutDisplayName = dsplyName+'(+'+(cnt).toString()+')';
                    }
                }
                newItem.columnType = 'ROLL_BACK';
                this.selctdRoleBackList.push(newItem);
            }
        }
    }

    //Show error messages
    private onError(errorMessage) {
        this.commonService.error(
            'Error!',
            errorMessage
        )
    }

    startDateChanged(dt:Date){
        if(this.report.endDate){
            if(this.report.endDate<this.report.startDate){
                this.report.endDate=this.report.startDate;
            }
        }
    }

    enableEditForm(){
        this.isViewOnly=false;
        this.isEdit=true;
        this.today=this.report.startDate;
        let dvcolumnList = [];
        if (this.selView.viewRelation == 'UNION') {
            dvcolumnList = this.selView.dataViewsUnionColumnsList;
        }else {
            dvcolumnList = this.selView.dataViewsColumnsList;
        }
        if(this.report.reportType === 'TRAIL_BALANCE_REPORT'){
            for(let j=0, paramLen=this.parametersList.length;j<paramLen;j++){
                if(this.parametersList[j].viewColName=='Ledger_Name'){
                    this.parametersList[j].isMandatory=true;
                    this.parametersList[j].isRequired=true;
                }
            }
        }
        for(let i=0, conLen=this.conditionsList.length;i<conLen;i++){
            if(this.conditionsList[i].columnType=='DATA_VIEW'){
                dvcolumnList.forEach((item) => {
                    if(this.conditionsList[i].viewColDisplayName===item.columnName){
                        this.bindConditionalLOVs(item.colDataType,i);
                    }
                })
            }else if(this.conditionsList[i].columnType=='FIN_FUNCTION'){

            }
        }
    }

    bindConditionalLOVs(colDataType:string,ind:number){
        this.reportsService.getLookupValues(colDataType).takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
            const newItem = new DefinitionColsInfo();
            this.conditionsList[ind]['LOV']=res;
        },
        (res: Response) => {
          //this.onError('res')
        });
    }

    copyReport(){
        this.report.id=undefined;
        this.report.reportName=this.report.reportName+'-(copy)';
        this.enableEditForm();
    }

    onOpen(event){
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"false");
    }

    updatedRoleBackType() {
        switch (this.report.rollBackType){
            case 'Days':
                this.roleCntMinMax = 365;
                if (this.report.rollBackCount) {
                    if (this.report.rollBackCount > 365) {
                        this.report.rollBackCount = 365;
                    } else if (this.report.rollBackCount && this.report.rollBackCount < -365) {
                        this.report.rollBackCount = -365;
                    }
                }
                break;
            case 'Months':
                this.roleCntMinMax = 12;
                if (this.report.rollBackCount) {
                    if (this.report.rollBackCount > 12) {
                        this.report.rollBackCount = 12;
                    } else if (this.report.rollBackCount && this.report.rollBackCount < -12) {
                        this.report.rollBackCount = -12;
                    }
                }
                break;
            case 'Years':
                this.roleCntMinMax = 5;
                if (this.report.rollBackCount) {
                    if (this.report.rollBackCount > 5) {
                        this.report.rollBackCount = 5;
                    } else if (this.report.rollBackCount && this.report.rollBackCount < -5) {
                        this.report.rollBackCount = -5;
                    }
                }
                break;
        }
        this.updateOffset();
    }

    reportModeChanged(type){
        if(this.report.reportType !== 'ACCOUNT_ANALYSIS_REPORT'){
            return;
        }
        if(type=='DETAIL'){
            if(this.tableReportColList.length==(this.reportAgrigateColList.length+this.reportSummaryColList.length) || (this.reportAgrigateColList.length==0&&this.reportSummaryColList.length==0)){
                this.reportAgrigateColList=[];
                this.reportSummaryColList=[];
                return;
            }else{
                this.reportSummaryColList.forEach((element) => {
                    this.tableReportColList.push(element);
                });
                this.reportAgrigateColList.forEach((element) => {
                    this.tableReportColList.push(element);
                });
            }
        }else if(type=='SUMMARY'){
            if(this.tableReportColList.length==(this.reportAgrigateColList.length+this.reportSummaryColList.length) || this.tableReportColList.length==0){
                this.tableReportColList=[];
                return;
            }else{
                this.tableReportColList.forEach((element) => {
                    if(element.dataType=='DECIMAL'){
                        this.reportAgrigateColList.push(element);
                    }else{
                        this.reportSummaryColList.push(element);
                    }
                    
                });
                
            }
        }
    }

    //load classes for columns definition section
    dynamicClassAdder(){
        if(this.isViewOnly){
            return 'col-md-12 col-sm-12 col-xs-12';
        }else{
            return 'col-md-9 col-sm-6 col-xs-6';
        }
    }
}