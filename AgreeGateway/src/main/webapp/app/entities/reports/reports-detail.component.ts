//#region Import section
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { Reports, ReportTypes, DataViews, DefinitionColsInfo, DataViewInfo, AgingBucket, AgingBucketDetails } from './reports.model';
import { ReportsService } from './reports.service';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { NotificationsService } from 'angular2-notifications-lite';
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
    private alive: boolean = true;      //Unsubscribe variable
    report = new Reports();                     // It holds Complete report definition
    selView = new DataViewInfo();               // It holds Selected Dataview
    srcViews: DataViews[] = [];                 // It holds list of all dataviews
    selReportType = new ReportTypes();          // It holds selected report type and definition
    srcReportTypes: ReportTypes[] = [];         // It holds list of all report types
    parameterTypes: LookUpCode[] = [];          // It holds list of all parameters types
    sysColumns: DefinitionColsInfo[] = [];      // It holds list of all report(System columns) type columns
    viewColumns: DefinitionColsInfo[] = [];     // It holds list of all columns of selected dataview
    isCreateNew: boolean = false;               // It holds form state is in create new
    isEdit: boolean = false;                    // It holds form state is in edit
    isViewOnly: boolean = false;                // It holds form state is in view
    viewBcktInfo: boolean = false;              // It holds bucket dialog state is in edit/view
    expandLayOut: boolean = false;
    startDateChange: boolean = false;
    endDateChange: boolean = false;
    bktStartDateChange: boolean = false;
    bktEndDateChange: boolean = false;
    routeSub: any;
    reportOutput: any[] = [];                   // It holds all report output types
    parametersList: Array<any> = [];            // It holds all selected parameter columns list
    tableReportColList: Array<any> = [];        // It holds all selected tabular output columns list
    groupByColumnsList: Array<any> = [];        // It holds all selected groupby columns list
    conditionsList: Array<any> = [];            // It holds all selected conditions columns list
    fullScreenHeight: any;                      // It holds full screen height
    finalColsList:DefinitionColsInfo[]=[];      // It holds all final columns
    displayBucketDialog: boolean = false;       // It decides to display bucket dialog or not
    srcAgingBuckets: AgingBucket[]=[];          // It holds all source aging buckets
    currentBucket = new AgingBucket();          // It holds current aging buckets
    selectedBucket = new AgingBucket();          // It holds current aging buckets
    bucketTypes: LookUpCode[] =[];              // It holds all aging bucket types
    bucketsList: AgingBucketDetails[]=[];       // It holds all bucket details list
    selctdBucketsList: DefinitionColsInfo[]=[]; // It holds all selected aging buckets
    selctdRoleBackList: DefinitionColsInfo[]=[];// It holds all selected Rollback list
    showDates=false;                            // It decides either display aging bucket dates or not
    today=new Date();                           // It holds current date
    srcDecimalCols:DefinitionColsInfo[]=[];     // It holds all list of decimal columns
    srcDateCols:DefinitionColsInfo[]=[];        // It holds all list of date columns
    notificationsObj={
        timeOut: 3000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: true,
        maxLength: 100
    };
    accStatusList: LookUpCode[] = [];      
    roleCntMinMax:Number=12;
    //#endregion

    constructor(
        private eventManager: JhiEventManager,
        private reportsService: ReportsService,
        private notificationsService: NotificationsService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
    ) {
        this.reportsService.getLookupValues('BUCKET_TYPE').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
            this.bucketTypes = res;
        });
    }
    
    ngOnInit() {
        $(".reportDefination").css({
            'min-height': 'calc(100vh - 170px)'
        });
        this.report.reportViewType = 'TABLE';
        this.reportsService.getLookupValues('ACCOUNTING_STATUS','REPORTING').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
            this.accStatusList = res;
        },
        (res: Response) => {
          this.onError('Failed to load Account Status list!')
        });
        this.routeSub = this.route.params.takeWhile(() => this.alive).subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            let presentPath = this.route.snapshot.url;
            //Check is 'ID' is there in route params or not
            if (params['id']) {
                //Load report definition for ID in route params
                this.reportsService.getReportDefinition(params['id']).takeWhile(() => this.alive).subscribe((res: Reports) => {
                    this.report = res;
                    //Bind all columns in respective arrays
                    this.report.columnsDefinition.forEach(col=>{
                        //parameter columns
                        if(col.parameterName){
                            if(col.parameterName.length>0)
                                this.parametersList.push(col);
                        }
                        //layout columns
                        if(col.layoutValue&&col.layoutValue.match(/^-?\d+$/)){
                            if(col.columnType=='AGING'){
                                this.selctdBucketsList.push(col);
                            }else if(col.columnType=='"ROLL_BACK"'){
                                this.selctdRoleBackList.push(col);
                            }
                            else{
                                this.tableReportColList.push(col);
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
                    this.reportsService.getLookupValues('FORM_CONTROLS').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
                        this.parameterTypes = res;
                        if (url.endsWith('edit'))
                            this.enableEditForm();
                    },
                    (res: Response) => {
                      this.onError('Failed to load Parameters Selection Types list!')
                    });
                    //Load all aging buckets
                    this.reportsService.getAllBuckets().takeWhile(() => this.alive).subscribe((allBuckets: AgingBucket[]) => {
                        this.srcAgingBuckets = allBuckets;
                    },
                    (res: Response) => {
                      this.onError('Failed to load Buckets list!')
                    });
                    //Load all report types
                    this.reportsService.getReportTypes().takeWhile(() => this.alive).subscribe((rptTypes: ReportTypes[]) => {
                        this.srcReportTypes = rptTypes;
                        this.srcReportTypes.forEach(type=>{
                            type.typeDisplayName1=type.typeDisplayName.replace(' Report','');
                            if(type.id==this.report.reportTypeId){
                                this.selReportType=type;
                                this.reportTypeChanged();
                            }
                        })
                    },
                    (res: Response) => {
                      this.onError('Failed to load Report Types list!')
                    });
                    //Load all data views list
                    this.reportsService.getDataViewsList().takeWhile(() => this.alive).subscribe((dtViews: DataViews[]) => {
                        this.srcViews = dtViews;
                        this.srcViews.forEach(item => {
                            if (item.id == this.report.sourceViewId) {
                                this.selView = item;
                                this.viewChanged();
                            }
                        })
                    },
                    (res: Response) => {
                      this.onError('Failed to load Data Views list!')
                    });
                    if (url.endsWith('edit')) {
                        this.isEdit=true;
                    }else{
                        this.isViewOnly = true;
                        this.expandLayOut=true;
                    } 
                },
                (res: Response) => {
                  this.onError('Failed to load Report definition!')
                });
            }
            else {
                this.isCreateNew = true;
                this.report.startDate=this.today;   //set start date as today
                //Load all selection types
                this.reportsService.getLookupValues('FORM_CONTROLS').takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
                    this.parameterTypes = res;
                },
                (res: Response) => {
                  this.onError('Failed to load Parameters Selection Types list!')
                });
                //Load all aging buckets
                this.reportsService.getAllBuckets().takeWhile(() => this.alive).subscribe((res: AgingBucket[]) => {
                    this.srcAgingBuckets = res;
                },
                (res: Response) => {
                  this.onError('Failed to load Buckets list!')
                });
                //Load all report types
                this.reportsService.getReportTypes().takeWhile(() => this.alive).subscribe((res: ReportTypes[]) => {
                    this.srcReportTypes = res;
                    this.srcReportTypes.forEach(element => {
                        element.typeDisplayName1=element.typeDisplayName.replace(' Report','');
                    });
                    if(this.srcReportTypes.length>0){
                        this.selReportType=this.srcReportTypes[0];
                        this.reportTypeChanged();
                    }
                },
                (res: Response) => {
                  this.onError('Failed to load Report Types list!')
                });
                //Load all dataviews list
                this.reportsService.getDataViewsList().takeWhile(() => this.alive).subscribe((res: DataViews[]) => {
                    this.srcViews = res;
                },
                (res: Response) => {
                  this.onError('Failed to load Data Views list!')
                });
            }
        });
    }

    public ngOnDestroy() {
        this.alive = false;
    }

    //It will Check is report name is available or not
    checkReportName() {
        if (!this.report.reportName || this.report.reportName.length < 1)
            return;
        this.reportsService.checkReportNameAvailable(this.report.reportName).takeWhile(() => this.alive).subscribe((res: number) => {
            if (res > 0) {
                if (this.report.id == res) {
                    return;
                } else {
                    this.report.reportName = '';
                    this.notificationsService.error('Oops...!', 'Sorry, report name is not available.');
                }
            }
        },
        (res: Response) => {
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
        }
        else if(this.tableReportColList.length<1){
            this.onError('Report should have atleast one column in output view!');
            return;
        }
        let flag:boolean=true;
        for(var k=0, len=this.parametersList.length;k<len;k++){
            if(this.parametersList[k].isMandatory){
                flag=false;
                break;
            }
        }
        if(flag){
            this.onError('Report should have atleast one Mandatory parameter!');
            return;
        }
        if (this.report.reportType === 'TRAIL_BALANCE_REPORT') {
            this.reportsService.getSegmentByViewIdAndQaulifier(this.report.sourceViewId, 'NATURAL_ACCOUNT').takeWhile(() => this.alive).toPromise().then((res: any) => {
                if (res) {
                    let newParam={
                        "ColumnId": 1,
                        "viewColDisplayName": res.segmentName + ' Description',
                        "layoutDisplayName": res.segmentName + ' Description',
                        "columnType": "NATURAL_ACCOUNT",
                        "dataType": "VARCHAR"
                    };
                    let newParam1={
                        "ColumnId": 2,
                        "viewColDisplayName": res.segmentName,
                        "layoutDisplayName": res.segmentName,
                        "columnType": "NATURAL_ACCOUNT",
                        "dataType": "VARCHAR"
                    };
                    this.tableReportColList.unshift(newParam);
                    this.tableReportColList.unshift(newParam1);
                }
                 this.saveReport();
            }, (res: any) => {
                this.saveReport();
            });
        } else {
            this.saveReport();
        }
    }

    //To Save Report Definition
    saveReport() {  
        //#region  Add all types columns in to final columns list 
        this.finalColsList=[];                       
        let paramListDup=[...this.parametersList];                        // Creating duplicate array 
        if(this.report.reportType=='AGING_REPORT'){                       //Set column order for aging bucket columns, these columns will come at end in layout
            let tmp=this.tableReportColList.length;
            let count=1;
            this.selctdBucketsList.forEach(element => {
                element.layoutValue=(tmp+count).toString();
                count++;
            });
            this.finalColsList=paramListDup.concat(this.selctdBucketsList);
        }else if(this.report.reportType=='ROLL_BACK_REPORT'){                       //Set column order for roleback columns, these columns will come at end in layout
            let tmp=this.tableReportColList.length;
            let count=1;
            this.selctdRoleBackList.forEach(element => {
                element.layoutValue=(tmp+count).toString();
                count++;
            });
            this.finalColsList=paramListDup.concat(this.selctdRoleBackList);
        }else{
            this.report.agregator=undefined;
            this.report.dateQualifier=undefined;
            this.finalColsList=paramListDup;
        }
        this.conditionsList.forEach(element => {
            let flag=false;
            this.finalColsList.forEach(item=>{
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
        this.groupByColumnsList.forEach(grpElement => {
            let flag=false;
            this.finalColsList.forEach(item=>{
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
        this.tableReportColList.forEach(element => {
            let flag=false;
            this.finalColsList.forEach(item=>{
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

        //#endregion
        this.report.columnsDefinition=this.finalColsList;
        if(this.startDateChange || this.isEdit){
            this.report.startDate=new Date(this.report.startDate.getTime() + 86400000);
        }
        if(this.endDateChange || this.isEdit){
            if(this.report.endDate)
                this.report.endDate=new Date(this.report.endDate.getTime() + 86400000);
        }
        //Post report definition
        this.reportsService.postReportDef(this.report).takeWhile(() => this.alive).subscribe((res) => {
            if (res.status === 200) {
                this.notificationsService.success('Saved!',this.report.reportName + ' saved successfully!')
                this.router.navigate(['/reports', {outlets: {'content': ['report-list']}}]);
            } else {
                this.notificationsService.error('Oops...!',this.report.reportName + ' not saved! Try again.')
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
        if(!$event.dragData)
            return;
        if (this.checkColumnExistence($event.dragData,destArray)){
            destArray.splice(ind,0,$event.dragData)
            // destArray.push($event.dragData);
        }
        
    }

    columnsDragDropSuccessGroupBy($event: any, destArray: any) {
        if(!$event.dragData)
            return;
        if($event.dragData.dataType=='DECIMAL'){
            this.onError('\'Decimal\' data type columns are not eligible for Groupby');
            return;
        }
        if (this.checkColumnExistence($event.dragData,destArray))
            destArray.push($event.dragData);
    }

    //This will work when drag and drop success in parameters section, if success it will add that column in parameters array
    columnsDragDropSuccessParameters($event: any, destArray: any) {
        if(!$event.dragData)
            return;
        switch($event.dragData.dataType){
            case 'INTEGER':
                $event.dragData.parameterType='SINGLE_SELECT_LOV';
            break;
            case 'DECIMAL':
                $event.dragData.parameterType='AMOUNT_RANGE';
            break;
            case 'DATE':
            case 'DATETIME':
                $event.dragData.parameterType='DATE_RANGE';
            break;
            case 'VARCHAR':
                $event.dragData.parameterType='SINGLE_SELECT_LOV';
            break;
            case 'BOOLEAN':
                $event.dragData.parameterType='BOOLEAN';
            break;
            default:
                this.onError('This column doese not have data type');
            break;
        }
        if(!$event.dragData.parameterName)
            $event.dragData.parameterName=$event.dragData.layoutDisplayName;
        if (this.checkColumnExistence($event.dragData,destArray))
            destArray.push($event.dragData);
    }

    //This will work when drag and drop success in conditions section, if success it will add that column in respective array
    columnsDragDropSuccessConditions($event: any, destArray: any) {
        if(!$event.dragData)
            return;
        if ($event.dragData.columnType !== 'DATA_VIEW') {
            this.notificationsService.info('Oops...!', 'Only \'Data view Columns\' are eligible for \'Conditions\'', {
                timeOut: 3000,
                showProgressBar: false,
                pauseOnHover: true,
                clickToClose: true,
                maxLength: 100
            });

            return;
        }
        if (this.checkColumnExistence($event.dragData,destArray))
        {
            this.reportsService.getLookupValues($event.dragData.dataType).takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
                let newItem = new DefinitionColsInfo();
                newItem=$event.dragData;
                newItem.LOV=res;
                switch($event.dragData.dataType){
                    case 'INTEGER':
                    case 'DATE':
                    case 'DATETIME':
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
                destArray.push($event.dragData);
            },
            (res: Response) => {
              //this.onError('res')
            });
        }
    }
    //#endregion

    //This will work when drag and drop success, if success it will check is column is already there in destination arry
    checkColumnExistence(dragData:any,srcArray:any[]):boolean{
        let i=srcArray.length-1;
        for(;i>=0;i--){
            if (srcArray[i].ColumnId == dragData.ColumnId && srcArray[i].columnType == dragData.columnType) {                   // Check is column exists or not if exists
                this.notificationsService.alert('Warning!','Column "' + srcArray[i].viewColDisplayName + '" already exists')
                return false;
            }
        }
        if(i<0)
            return true;
        return false;
    }

    //This will check column name is available or not
    checkNameExistence(item:any,srcArray:any[],lable:string,ind:number){
        for(var i=0;i<srcArray.length;i++){
            if( i!=ind && srcArray[i][lable]==item[lable]){
                if(item[lable]==undefined)
                    return;
                this.notificationsService.alert('Warning!',item[lable] + '" already exists')
                item[lable]=undefined;
                return;
            }
        }
    }
    test(){
    }

    checkNameExistenceOnDrop(item:any){
        if(!item)
            return;
        for(var i=0;i<this.tableReportColList.length;i++){
            if( !(this.tableReportColList[i].ColumnId == item.ColumnId && this.tableReportColList[i].columnType == item.columnType)){
                if (item.layoutDisplayName == this.tableReportColList[i].layoutDisplayName) {
                    item.layoutDisplayName=item.layoutDisplayName+'_1';
                }
            }
        }
        for(var i=0;i<this.selctdBucketsList.length;i++){
            if( !(this.selctdBucketsList[i].ColumnId == item.ColumnId && this.selctdBucketsList[i].columnType == item.columnType)){
                if (item.layoutDisplayName == this.selctdBucketsList[i].layoutDisplayName) {
                    item.layoutDisplayName=item.layoutDisplayName+'_1';
                }
            }
        }
        for(var i=0;i<this.selctdRoleBackList.length;i++){
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
        if (this.selReportType.mode && this.isCreateNew)
            this.report.reportMode = "Both";
        if (this.selReportType.show && this.isCreateNew)
            this.report.show = "Both";
        if (this.selReportType.reconcile && this.isCreateNew)
            this.report.reconcile = "Both";
        if(this.report.reportType == 'ROLL_BACK_REPORT'){
            this.report.rollBackType='Months';
            this.report.rollBackCount=0;
            this.report.allowSequence=true;
            this.updateOffset();
        }
        if(this.isCreateNew){
            this.sysColumns = [];
            this.selctdBucketsList=[];
            this.selctdRoleBackList=[];
            //Remove existing system columns
            for (var i = 0; i < this.parametersList.length; i++) {
                if (this.parametersList[i].columnType == 'FIN_FUNCTION') {
                    this.parametersList.splice(i, 1);
                    i--;
                }
            }
            for (var i = 0; i < this.conditionsList.length; i++) {
                if (this.conditionsList[i].columnType == 'FIN_FUNCTION') {
                    this.conditionsList.splice(i, 1);
                    i--;
                }
            }
            for (var i = 0; i < this.groupByColumnsList.length; i++) {
                if (this.groupByColumnsList[i].columnType == 'FIN_FUNCTION') {
                    this.groupByColumnsList.splice(i, 1);
                    i--;
                }
            }
            for (var i = 0; i < this.tableReportColList.length; i++) {
                if (this.tableReportColList[i].columnType == 'FIN_FUNCTION') {
                    this.tableReportColList.splice(i, 1);
                    i--;
                }
            }
            this.tableReportColList = [];
            for (var i = 0; i < this.srcDecimalCols.length; i++) {
                if (this.srcDecimalCols[i].columnType == 'FIN_FUNCTION') {
                    this.srcDecimalCols.splice(i, 1);
                    i--;
                }
            }
            for (var i = 0; i < this.srcDateCols.length; i++) {
                if (this.srcDateCols[i].columnType == 'FIN_FUNCTION') {
                    this.srcDateCols.splice(i, 1);
                    i--;
                }
            }
        }
        this.reportsService.getLookupValues(this.report.reportType).takeWhile(() => this.alive).subscribe((res: LookUpCode[]) => {
            res.forEach(item => {
                let newItem = new DefinitionColsInfo();
                newItem.ColumnId = item.id;
                newItem.viewColName = item.lookUpCode;
                newItem.viewColDisplayName = item.meaning;
                newItem.layoutDisplayName = item.meaning;
                newItem.parameterName = item.meaning;
                newItem.columnType = 'FIN_FUNCTION';
                newItem.dataType=item.description;
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
            });
        },
        (res: Response) => {
          this.onError('Failed to load Report Type definition!')
        });
    }

    /* Delete Functions */
    columnDeleteFunction(col:any, ind:any, mySrcArray:any[]){
        mySrcArray.splice(ind,1);
    }

    /* Remove parameters col instance in report layout columns (If exists) */
    cleanUpParametersInLayoutCols(columnId:any,columnType:any){
        this.tableReportColList.forEach(item=>{
            if(columnId==item.ColumnId && columnType==item.columnType){
                item.parameterType=undefined;
                item.parameterName=undefined;
                item.isMandatory=undefined;
            }
        })
    }

    /* Remove parameters col instance in report layout columns (If exists) */
    cleanUpConditionsInLayoutCols(columnId:any,columnType:any){
        this.tableReportColList.forEach(item=>{
            if(columnId==item.ColumnId && columnType==item.columnType){
                item.conditionalOperator=undefined;
                item.conditionalValue=undefined;
            }
        })
    }
    
    // this will work at data view changed
    viewChanged() {
        if(this.isCreateNew){
            //Remove existing system columns
            for(var i=0;i<this.parametersList.length;i++){
                if (this.parametersList[i].columnType == 'DATA_VIEW') {
                    this.parametersList.splice(i,1);
                    i--;
                }
            }
            for(var i=0;i<this.conditionsList.length;i++){
                if (this.conditionsList[i].columnType == 'DATA_VIEW') {
                    this.conditionsList.splice(i,1);
                    i--;
                }
            }
            for(var i=0;i<this.groupByColumnsList.length;i++){
                if (this.groupByColumnsList[i].columnType == 'DATA_VIEW') {
                    this.groupByColumnsList.splice(i,1);
                    i--;
                }
            }
            for(var i=0;i<this.tableReportColList.length;i++){
                if (this.tableReportColList[i].columnType == 'DATA_VIEW') {
                    this.tableReportColList.splice(i,1);
                    i--;
                }
            }
            this.viewColumns=[];
            this.report.dateQualifier=undefined;
            this.report.agregator=undefined;
        }
        
        this.reportsService.getColsListByDVid(this.report.sourceViewId).takeWhile(() => this.alive).subscribe((res: DataViewInfo) => {
            this.selView = res;
            for(var i=0; i<this.srcDecimalCols.length;i++){
                if(this.srcDecimalCols[i].columnType=='DATA_VIEW'){
                    this.srcDecimalCols.splice(i,1);
                    i--;
                }
            }
            for(var i=0; i<this.srcDateCols.length;i++){
                if(this.srcDateCols[i].columnType=='DATA_VIEW'){
                    this.srcDateCols.splice(i,1);
                    i--;
                }
            }
            this.selView.dataViewsColumnsList.forEach(item => {
                let newItem = new DefinitionColsInfo();
                newItem.ColumnId = item.id;
                newItem.viewColName = item.columnHeader;
                newItem.viewColDisplayName = item.columnName;
                newItem.layoutDisplayName = item.columnName;
                newItem.parameterName = item.columnName;
                newItem.columnType = 'DATA_VIEW';
                newItem.dataType=item.colDataType;
                
                if(item.colDataType=='DECIMAL'){
                    let decNewItem=Object.assign(new DefinitionColsInfo(),newItem);
                    if(item.qualifier){
                        if(item.qualifier=='AMOUNT')
                            this.report.agregator=decNewItem;
                    }
                    this.srcDecimalCols.push(decNewItem);
                }
                if(item.colDataType=='DATE' || item.colDataType=='DATETIME'){
                    let dateNewItem=Object.assign(new DefinitionColsInfo(),newItem);
                    if(item.qualifier){
                        if(item.qualifier=='TRANSDATE')
                            this.report.dateQualifier=dateNewItem;
                    }
                    this.srcDateCols.push(dateNewItem);
                }
                if(this.selReportType.type!='ACCOUNT_BALANCE_REPORT' || (this.selReportType.type=='ACCOUNT_BALANCE_REPORT' && item.groupBy)){
                    this.viewColumns.push(newItem);
                }
            });
        },
        (res: Response) => {
          this.onError('Failed to load Data View definition!');
        });
    }
    
    //#region Buckets related code
    //Check is bucket name is available or not
    checkBucketName() {
        if (!this.currentBucket.bucketName || this.currentBucket.bucketName.length < 1)
            return;
        this.reportsService.checkBucketNameAvailable(this.currentBucket.bucketName).takeWhile(() => this.alive).subscribe((res: number) => {
            if (res > 0) {
                this.currentBucket.bucketName = '';
                this.notificationsService.error('Oops...!', 'Sorry, Bucket name is not available.');
            }
        },
        (res: Response) => {
          this.onError('Failed to check bucket name availability!')
        });
    }

    //it will work if user update count
    updateBucketCount(count:number){
        if(count==0)
        {
            this.bucketsList=[];
            this.bucketsList.push(new AgingBucketDetails());
            this.currentBucket.count=1;
            count=1;
        }
        if(this.bucketsList.length>count){                                          //if user reduced the count
            this.bucketsList.splice(count,this.bucketsList.length-count);
        }
        for(var i=this.bucketsList.length; i<count; i++){        //if user increased the count
            this.bucketsList.push(new AgingBucketDetails());
        }
        this.currentBucket.count=count;
    }

    // this method check pressed key is numeric or not
    keyHandlerCount(code){
        if (code > 31 && (code < 48 || code > 57))
            return false;
        return true;
    }

    //It will work if user press any key at bucket from/to value
    keyHandlerBuckets(code, ind: number, isFrom: boolean) {
        if(ind==0 && this.bucketsList[0].fromValue==null){
            return true;
        }
        if ((ind > 0 && isFrom && !this.bucketsList[ind - 1].toValue) || (!isFrom&&!this.bucketsList[ind].fromValue)) { 
            return false;
        }
        return true;
    }

    //It will work if bucket from/to value changed
    checkBucketValue(ind:number,Val:number,isFrom:Boolean){
        if(ind==0){                                                                         //if first bucket
            if(this.bucketsList[ind].fromValue != undefined){
                if(this.bucketsList[ind].toValue<=this.bucketsList[ind].fromValue){
                    this.bucketsList[ind].toValue=undefined;
                    this.notificationsService.error('Oops!','Please give a meaningfull \'From\' and \'To\' values at index: '+(ind+1),this.notificationsObj);
                    return;   
                }
            }else
                return;
        }else if(isFrom){
            if(!this.bucketsList[ind-1].toValue || this.bucketsList[ind-1].toValue>=this.bucketsList[ind].fromValue){
                this.bucketsList[ind].fromValue=undefined;
                this.notificationsService.error('Oops!','Please give a meaningfull \'From\' and \'To\' values at index: '+(ind+1),this.notificationsObj);
                return;
            }
        }
        else{
            if(this.bucketsList[ind].toValue<=this.bucketsList[ind].fromValue){
                this.bucketsList[ind].toValue=undefined;
                this.notificationsService.error('Oops!','Please give a meaningfull \'From\' and \'To\' values at index: '+(ind+1),this.notificationsObj);
            }
        }
    }

    //Delete bucket
    deleteBucket(index: number){
        this.bucketsList.splice(index,1);
        this.currentBucket.count--;
        if(this.bucketsList.length==0)
        {
            this.currentBucket.count++;
            this.bucketsList.push(new AgingBucketDetails());
        }
    }

    //Save bucket info
    saveBucketsInfo(){
        this.currentBucket.bucketDetDataList=this.bucketsList;
        if(this.bktStartDateChange){
            this.currentBucket.startDate=new Date(this.currentBucket.startDate.getTime() + 86400000);
        }
        if(this.bktEndDateChange){
            if(this.report.endDate)
                this.currentBucket.endDate=new Date(this.currentBucket.endDate.getTime() + 86400000);
        }
        this.reportsService.postBucketDef(this.currentBucket).takeWhile(() => this.alive).subscribe((res) => {
            if (res.status === 200) {
                this.notificationsService.success('Saved!',this.currentBucket.bucketName + ' saved successfully!')
                this.reportsService.getAllBuckets().takeWhile(() => this.alive).subscribe((res1: AgingBucket[]) => {    //To load all buckets again
                    this.srcAgingBuckets = res1;
                    this.report.agingBucketId=res.json();                                   //Assign new bucket as selected bucket
                    this.displayBucketDialog=false;                                        //Close dialog
                    this.bucketChanged();                                                  //calling bucket changed fuction
                },
                (res: Response) => {
                  this.onError('Failed to load buckets information!')
                });
            } else {
                this.notificationsService.error('Oops...!',this.currentBucket.bucketName + ' not saved! Try again.')
            }
        },
        (res: Response) => {
          this.onError('Failed to save bucket definition!')
        });
    }

    //If user changes bucket
    bucketChanged(){
        this.selctdBucketsList=[];
        if(this.report.agingBucketId<1){
            this.report.agingBucketId=undefined;
            return;
        }
        this.reportsService.getBucketDef(this.report.agingBucketId).takeWhile(() => this.alive).subscribe((res: AgingBucket) => {
            this.selectedBucket=res;
            res.bucketDetDataList.forEach(element => {
                let newItem = new DefinitionColsInfo();
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

    //Get selected bucket infor by ID, and display in dialog
    showBucketDef(id:number){
        this.viewBcktInfo=true;
        this.reportsService.getBucketDef(id).takeWhile(() => this.alive).subscribe((res: AgingBucket) => {
            this.bucketsList = res.bucketDetDataList;
            this.currentBucket=res;
            this.displayBucketDialog=true;
        },
        (res: Response) => {
          this.onError('Failed to load bucket definition!');
        });
    }

    //Show bucket window
    showBucketDialog(){
        this.currentBucket=new AgingBucket();
        this.currentBucket.type='AGE';
        this.currentBucket.count= 1;
        this.bucketsList=[];
        this.bucketsList.push(new AgingBucketDetails());
        this.currentBucket.startDate=new Date();
        this.currentBucket.enabledFlag=true;
        this.displayBucketDialog=true;
    }
    //#endregion

    //It will work if user update rollback count
    updateOffset(){
        //If user update value to opposit sign of current value
        if (this.selctdRoleBackList.length > 1) {   
            if( (+(this.selctdRoleBackList[1].viewColDisplayName) > 0 && this.report.rollBackCount < 0)||(+(this.selctdRoleBackList[1].viewColDisplayName) < 0 && this.report.rollBackCount > 0))
                this.selctdRoleBackList = [];
        }
        let newCount=Math.abs(this.report.rollBackCount)+1;
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
                let newItem = new DefinitionColsInfo();
                
                if(cnt==0){
                    newItem.viewColDisplayName = '0';
                    newItem.layoutDisplayName = dsplyName;
                }
                else{
                    if(this.report.rollBackCount<0){
                        newItem.viewColDisplayName = '-'+(cnt).toString();
                        newItem.layoutDisplayName = dsplyName+'(-'+(cnt).toString()+')';
                    }else{
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
        this.notificationsService.error(
            'Error!',
            errorMessage,
            {
                timeOut: 3000,
                showProgressBar: false,
                pauseOnHover: true,
                clickToClose: true,
                maxLength: 100
            }
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
        for(var i=0, paramLen=this.parametersList.length;i<paramLen;i++){
            for(var j=0, typesLen=this.parameterTypes.length; j<typesLen;j++){
                if(this.parametersList[i].parameterType==this.parameterTypes[j].meaning){
                    this.parametersList[i].parameterType=this.parameterTypes[j].lookUpCode;
                }
            }
        }
    }

    updatedRoleBackType() {
        switch (this.report.rollBackType) {
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

    //load classes for columns definition section
    dynamicClassAdder(){
        if(this.isViewOnly){
            return 'col-md-12 col-sm-12 col-xs-12';
        }else{
            return 'col-md-9 col-sm-6 col-xs-6';
        }
    }

    testMethod(){
        
    }
    
}
