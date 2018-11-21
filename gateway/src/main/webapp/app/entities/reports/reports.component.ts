import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Response } from '@angular/http';
import { Subscription, Subject } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { PageEvent,MdDialog, MdDialogRef, MD_DIALOG_DATA,MdPaginator } from '@angular/material';
import { Reports, ParametersSubmited, ReportTypes, ReportColsInfo, ReportRequestList } from './reports.model';
import { ReportsService } from './reports.service';
import { ITEMS_PER_PAGE, ResponseWrapper, pageSizeOptionsList } from '../../shared';
import { CommonService } from '../common.service';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
declare var d3: any;

@Component({
    selector: 'jhi-reports',
    templateUrl: './reports.componentV1.html'
})
export class ReportsComponent implements OnDestroy, OnInit {
    reqListpageEvent: PageEvent = new PageEvent();
    @ViewChild('reqListPaginator') reqListPaginator: MdPaginator;
    @ViewChild('paginatorDiv') paginatorDiv:ElementRef;
    reportReqList: ReportRequestList[] = [];
    private unsubscribe: Subject<void> = new Subject();
    currentAccount: any;
    reports: Reports[];
    selectedReports: Reports[];
    eventSubscriber: Subscription;
    columnOptions = [];
    pageSize: number = 0;
    page: number = 0;
    pageSizeOptions = pageSizeOptionsList;
    length: number;
    pageEvent: PageEvent = new PageEvent();
    sortOrder: string;
    sortCol: string;
    listType: string = "All Reports List";
    listsLov: string[] = ['My Favourite Reports', 'My Recent Reports'];
    displayCustLov: boolean = false;
    fltrdRpt: string;
    rptSearchStr: string = '';
    srcReportTypes: ReportTypes[] = [];
    reportInfo = new ReportColsInfo();
    displayParameters: boolean = false;
    showRequestsList: boolean = false;
    selectedTab: number = 0;
    parametersSubmited: any[] = [];
    notificationsObj = {
        timeOut: 1000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: true,
        maxLength: 60
    };
    recentRpts: boolean = false;
    favRpts: boolean = false;
    showProcessing: boolean = false;
    request: any;
    isContentLoaded: Boolean = false;
    TemplatesHeight: any;
    tempWidth: any;
    requestsLength:number = 0;
    selRptId:any;
    requestType:string;
    showPaginator:boolean=true;
    reqSortOrder:string=undefined;
    reqSortCol:string=undefined;

    constructor(
        private reportsService: ReportsService,
        private router: Router,
        private cs: CommonService,
        public datepipe: DatePipe
    ) {
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.pageSize;
        this.reqListpageEvent.pageIndex=0;
        this.reqListpageEvent.pageSize = this.pageSize;
        this.loadReportsList();
        this.reportsService.getReportTypes().takeUntil(this.unsubscribe).subscribe((rptTypes: ReportTypes[]) => {
            this.srcReportTypes = rptTypes;
            this.srcReportTypes.forEach((type)=>{
                type.typeDisplayName1=type.typeDisplayName.replace(' Report','');
            })
        },
        (res: Response) => {
          
        });
    }

    addReport(){
        if(this.fltrdRpt){
            this.router.navigate(['/reports', {outlets: {'content': ['new-report',this.fltrdRpt]}}]);
        }else{
            this.router.navigate(['/reports', {outlets: {'content': ['new-report']}}]);
        }
    }

    tabChange(taIndex){
        if(taIndex>0){
            this.fltrdRpt=this.srcReportTypes[taIndex-1].type;
        }else{
            this.fltrdRpt=undefined;
        }
        this.pageEvent.pageIndex=this.page;
        this.loadReportsList();
    }
    listTypeChange(type:string){
        this.recentRpts=false;
        this.favRpts=false;
        this.listsLov.push(this.listType);
        this.listType=type;
        this.listsLov.splice(this.listsLov.indexOf(type),1);
        if(this.listType === 'My Favourite Reports'){
            this.favRpts=true;
        }else if(this.listType === 'My Recent Reports'){
            this.recentRpts=true;
        }
        this.pageEvent.pageIndex=this.page;
        this.loadReportsList();
    }
    
    loadSortedReportList(event){
        this.sortOrder = 'Ascending';
        if (event.order < 1){
            this.sortOrder = 'Descending';
        }
        this.sortCol = event.field;
        this.loadReportsList();
    }

    globalSearch(){
        this.pageEvent.pageIndex=0;
        this.loadReportsList();
    }

    loadReportsList() {
        this.reqListpageEvent.pageIndex=0;
        this.reqListpageEvent.pageSize = this.pageSize;
        this.requestType=undefined;
        if(this.request){
            this.request.unsubscribe();
        }
        this.request = this.reportsService.getAllReportsList({
            pageInd: this.pageEvent.pageIndex,
            pageSize: this.pageEvent.pageSize
        },this.fltrdRpt,this.sortOrder,this.sortCol,this.favRpts,this.recentRpts,this.rptSearchStr).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.length = +res.headers.get('x-count');
            this.reports = res.json();
            this.reports.forEach((item)=>{
                this.reportsService.convertItemFromServer(item);
            })
            this.isContentLoaded=true;
        },
        (res: Response) => {
            this.reports =[];
            this.isContentLoaded=true;
        });
    }
    
    buildParameters(reqId, rptType) {
        this.parametersSubmited = [];
        this.reportsService.getParamsObj(reqId).takeUntil(this.unsubscribe).subscribe((res: any) => {
            const fnlObj = res.filterMap;
            this.displayParameters=true;
            fnlObj.fields.forEach((element) => {
                const newParam = new ParametersSubmited();
                newParam.Parameter = element.fieldName;
                if (Array.isArray(element.selectedValue)) {
                    newParam.Value = element.selectedValue.join(', ');
                } else {
                    newParam.Value = element.selectedValue;
                }
                this.parametersSubmited.push(newParam);
            });
            
            if(fnlObj.reconcile){
                const recParam = new ParametersSubmited();
                recParam.Parameter = 'Reconcile Status';
                if (fnlObj.reconcile === 'Both') {
                    recParam.Value = 'Reconciled, Un-Reconciled';
                } else {
                    recParam.Value = fnlObj.reconcile;
                }
                this.parametersSubmited.push(recParam);
            }
            
            if(fnlObj.accVal){
                const accParam = new ParametersSubmited();
                accParam.Parameter="Accounted Status";
                if (fnlObj.accVal === 'Both') {
                    accParam.Value = 'All';
                } else {
                    accParam.Value = fnlObj.accVal;
                }
                this.parametersSubmited.push(accParam);
            }
            if(fnlObj.dateTimeVal){
                const repDtParam=new ParametersSubmited();
                repDtParam.Parameter="Report Date";
                const newDate = new Date(fnlObj.dateTimeVal);
                repDtParam.Value=this.datepipe.transform(newDate, 'medium').toString();
                this.parametersSubmited.push(repDtParam);
            }
            if(fnlObj.aggregator){
                if(fnlObj.aggregator.ColumnId){
                    const argrParam=new ParametersSubmited();
                    argrParam.Parameter="Agregator";
                    argrParam.Value=fnlObj.aggregator.layoutDisplayName;
                    this.parametersSubmited.push(argrParam);
                }
            }
            if(fnlObj.outputType){
                const opParam=new ParametersSubmited();
                opParam.Parameter="Output Type";
                opParam.Value=fnlObj.outputType;
                this.parametersSubmited.push(opParam);
            }
            if(rptType=='ROLL_BACK_REPORT'){
                let colsCnt=0;
                for(let j=0;j<fnlObj.outputCols.length;j++){
                    if(fnlObj.outputCols[j].columnType=='ROLL_BACK'){
                        colsCnt++;
                    }
                };
                this.parametersSubmited.push({"Roll Back Count":colsCnt});
            }
            if(rptType=='AGING_REPORT'){
                const cols=[];
                for(let i=0;i<fnlObj.outputCols.length;i++){
                    if(fnlObj.outputCols[i].columnType=='AGING'){
                        cols.push(fnlObj.outputCols[i].layoutDisplayName);
                    }
                };
                this.parametersSubmited.push({"Bucket Columns":cols.join(", ")});
            }
        },
        (res: Response) => {
          
        });
    }

    cancelRequest(){
        this.request.unsubscribe();
        this.showProcessing=false;
    }

    showReportOutput(event){
        if(event.data.id&&event.data.requestId && event.data.lastRun){
            this.router.navigate(['/reports', {outlets: {'content': ['run-reports',event.data.requestId,event.data.id]}}]);
        }
    }

    ngOnInit() {
        const screenSize=+this.cs.screensize().height;
        const screenWidth=+this.cs.screensize().width;
        this.tempWidth=screenWidth-100;
        if (screenSize > 600) {
            this.TemplatesHeight = (screenSize - 200) + 'px';
        } else {
            this.TemplatesHeight = (screenSize - 50) + 'px';
        }
    }

    public ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.cs.destroyNotification(); 
    }

    downloadReport(reqId:any,outputType:string,reportName:string){
        this.showProcessing=true;
        this.reportsService.downloadReportOutput(reqId,outputType,reportName).takeUntil(this.unsubscribe).subscribe((res:any) => {
            this.showProcessing=false;
        },(res: Response)=>{
            this.showProcessing=false;
        })
    }

    setFavReport(row:any){
        this.reportsService.setFavReport(row.id).takeUntil(this.unsubscribe).subscribe((res:any) => {
            row.isFavourite=true;
        })
    }

    removeFavRpt(row:any){
        this.reportsService.removeFavReport(row.id).takeUntil(this.unsubscribe).subscribe((res:any)=>{
            row.isFavourite=false;
        })
    }
    
    reqTabChange(type){
        this.reqListpageEvent.pageIndex=0;
        this.reqListpageEvent.pageSize=this.pageSize;
        if(type=='all'){
            this.requestType=undefined;
        }else{
            this.requestType=type;
        }
        this.getRequestsList();
    }

    sortReqTable(event){
        this.reqSortOrder = 'ascending';
        if (event.order < 1){
            this.reqSortOrder = 'descending';
        }
        this.reqSortCol = event.field;
        this.getRequestsList();
    }

    getRequestsList() {
        if (!this.selRptId) {
            this.onError('Please select a valid report');
            return;
        }
        this.reportReqList=[];
        this.reportsService.getReportReqList(this.selRptId, {
            page: this.reqListpageEvent.pageIndex,
            size: this.reqListpageEvent.pageSize
        },this.requestType,this.reqSortCol,this.reqSortOrder).takeUntil(this.unsubscribe).subscribe((res: Response) => {
            this.reportReqList = res.json();
            this.showRequestsList=true;
            if (this.reportReqList.length > 0) {
                this.requestsLength = +res.headers.get('x-count');
            }
        },
            (res: Response) => {
                this.onError('Failed to load Report Requests list!');
            });
    }

    refreshAsOfNow(rptId:any,reqId:any){
        this.reportsService.refreshAsOfNowAsync(rptId,reqId).takeUntil(this.unsubscribe).subscribe((res)=>{
        })
    }

    private onError(errorMessage) {
        this.cs.error(
            'Error!',
            errorMessage
        )
    }

    myTestMethod(){
        let expenses = [{"name":"jim","amount":34,"date":"11/12/2015"},
        {"name":"carl","amount":120.11,"date":"11/12/2015"},
        {"name":"jim","amount":45,"date":"12/01/2015"},
        {"name":"stacy","amount":12.00,"date":"01/04/2016"},
        {"name":"stacy","amount":34.10,"date":"01/04/2016"},
        {"name":"stacy","amount":44.80,"date":"01/05/2016"}
      ];

      let expensesByName = d3.nest()
        .key(function(d) { return d.name; })
        .key(function(d) { return d.date; })
        .entries(expenses);
    
        console.log(expensesByName);
        let list = [
            {name: 1, lastname: "foo1", age: 16, gender:'M'},
            {name: 2, lastname: "foo", age: 13, gender:'M'},
            {name: 3, lastname: "foo1", age: 11, gender:'M'},
            {name: 4, lastname: "foo", age: 11, gender:'M'},
            {name: 5, lastname: "foo1", age: 16, gender:'M'},
            {name: 6, lastname: "foo", age: 16, gender:'M'},
            {name: 7, lastname: "foo1", age: 13, gender:'M'},
            {name: 8, lastname: "foo1", age: 16, gender:'M'},
            {name: 9, lastname: "foo", age: 13, gender:'M'},
            {name: 0, lastname: "foo", age: 16, gender:'M'}
        ];
        let key='lastname';
        let dist = list.map((item)=>  item[key]);
        console.log(dist);

        // let result = list.reduce((a, c) => (a[c.lastname] = a[c.lastname] ? (a[c.lastname].age += c.age, a[c.lastname]) : c, a), {});
        // console.log(result);   
        


        // let result = this.groupBy(list, function(item)
        // {
        //     return [item.lastname, item.age];
        // });
        // console.log(result);
    }

    multiFilter(array, filters) {
        const filterKeys = Object.keys(filters);
        // filters all elements passing the criteria
        return array.filter((item) => {
            // dynamically validate all filter criteria
            return filterKeys.every(key => !!~filters[key].indexOf(item[key]));
        });
    }

    groupBy( array , myFun )
    {
        let groups = {};
        array.forEach(function (o) {
            let group = JSON.stringify(myFun(o));
            groups[group] = groups[group] || [];
            groups[group].push(o);
        });
        console.log(groups);
        return Object.keys(groups).map(function (group) {
            return groups[group];
        })
    }

    data=[ {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1576366.72,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1226865.37,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 851660.51,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "SALES_CATEGORY",
        "Provider Name" : "Cayan",
        "Amount" : 1178913.64,
        "Ledger Currency" : "USD",
        "Transaction Currency" : "AUD",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "RECON",
        "Code Combination" : "100.0000.40000.00000",
        "Accounted Debit Amount" : 895974.37,
        "Entered Debit Amount" : 1178913.64,
        "Currency Code" : "AUD",
        "Accounted Date" : "2018-06-20",
        "Line Type" : "RECIEVABLES",
        "Variance Amount" : 0,
        "Ledger Name" : "Alpha Primary Ledger",
        "Posted Date" : "2018-06-23",
        "Accounting Status" : "Journals Entered",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "SALES_CATEGORY",
        "Provider Name" : "Cayan",
        "Amount" : 1178913.64,
        "Ledger Currency" : "USD",
        "Transaction Currency" : "AUD",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 1178913.64,
        "Source" : "RECON",
        "Code Combination" : "100.0000.30500.00000",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "2018-06-20",
        "Line Type" : "REVENUE",
        "Variance Amount" : 0,
        "Ledger Name" : "Alpha Primary Ledger",
        "Posted Date" : "2018-06-23",
        "Accounting Status" : "Journals Entered",
        "Accounted Credit Amount" : 895974.37
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 912813.04,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1820433.21,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 985418.54,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1185229.67,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 996658.28,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 951519.57,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1033475.6,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1820433.21,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1168001.59,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1576366.72,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1576366.72,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1168001.59,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1178913.64,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 996658.28,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1033475.6,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1185229.67,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 912813.04,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1178913.64,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 4912982.88,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1178913.64,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 996658.28,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1033475.6,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 996658.28,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 912813.04,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 920801.27,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 4912982.88,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 951519.57,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 951519.57,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1185229.67,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1226865.37,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 4785393.75,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1219826.08,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 996658.28,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1820433.21,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1168001.59,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1576366.72,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 912813.04,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 985418.54,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 851660.51,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 4283245.0,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1168001.59,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1219826.08,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1219826.08,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Not Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 4912982.88,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      }, {
        "Category" : "",
        "Provider Name" : "Cayan",
        "Amount" : 1168001.59,
        "Ledger Currency" : "",
        "Transaction Currency" : "",
        "Recon Status" : "Reconciled",
        "Entered Credit Amount" : 0.0,
        "Source" : "",
        "Code Combination" : "",
        "Accounted Debit Amount" : 0.0,
        "Entered Debit Amount" : 0.0,
        "Currency Code" : "AUD",
        "Accounted Date" : "",
        "Line Type" : "",
        "Variance Amount" : 0,
        "Ledger Name" : "",
        "Posted Date" : "",
        "Accounting Status" : "Un Accounted",
        "Accounted Credit Amount" : 0.0
      } ]
    
}
