import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { PageEvent } from '@angular/material';
import { Reports, ParametersSubmited, ReportTypes, ReportColsInfo } from './reports.model';
import { ReportsService } from './reports.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { CommonService } from '../common.service';
import { NotificationsService } from 'angular2-notifications-lite';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-reports',
    templateUrl: './reports.component.html'
})
export class ReportsComponent implements OnDestroy, OnInit {

    private alive: boolean = true;      //Unsubscribe variable
    currentAccount: any;
    reports: Reports[];
    selectedReports: Reports[];
    eventSubscriber: Subscription;
    columnOptions = [];
    TemplatesHeight: any;
    pageSize: number = 0;
    pageSizeOptions = [10, 25, 50, 100];
    length: number;
    pageEvent: PageEvent = new PageEvent();
    sortOrder:string;
    sortCol:string;
    listType:string="All Reports List";
    listsLov:string[]=['Favourite Reports List','Recent Reports List'];
    displayCustLov:boolean=false;
    fltrdRpt:string;
    srcReportTypes: ReportTypes[] = [];
    reportInfo = new ReportColsInfo();
    displayParameters:boolean=false;
    selectedTab:number=0;
    selectedTab1:number=0;
    parametersSubmited:any[]=[];
    notificationsObj = {
        timeOut: 1000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: true,
        maxLength: 60
    };
    recentRpts: boolean = false;
    favRpts: boolean = false;
    showProcessing:boolean = false;
    request:any;

    constructor(
        private reportsService: ReportsService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private cs: CommonService,
        private notificationsService: NotificationsService,
        public datepipe: DatePipe
    ) {
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageSize = this.pageSize;
        this.pageEvent.pageIndex = 0;
        this.loadReportsList();
        this.reportsService.getReportTypes().takeWhile(() => this.alive).subscribe((rptTypes: ReportTypes[]) => {
            this.srcReportTypes = rptTypes;
            this.srcReportTypes.forEach(type=>{
                type.typeDisplayName1=type.typeDisplayName.replace(' Report','');
            })
        },
        (res: Response) => {
          
        });
    }
    tabChange(taIndex){
        if(taIndex>0){
            this.fltrdRpt=this.srcReportTypes[taIndex-1].type;
        }else{
            this.fltrdRpt=undefined;
        }
        this.pageEvent.pageIndex=0;
        // this.pageEvent.pageSize=this.pageSize;
        this.loadReportsList();
    }
    listTypeChange(type:string){
        this.recentRpts=false;
        this.favRpts=false;
        this.listsLov.push(this.listType);
        this.listType=type;
        this.listsLov.splice(this.listsLov.indexOf(type),1);
        if(this.listType === 'Favourite Reports List'){
            this.favRpts=true;
        }else if(this.listType === 'Recent Reports List'){
            this.recentRpts=true;
        }
        this.pageEvent.pageIndex=0;
        // this.pageEvent.pageSize=this.pageSize;
        this.loadReportsList();
    }

    loadReportsList() {
        this.reportsService.getAllReportsList({
            pageInd: this.pageEvent.pageIndex,
            pageSize: this.pageEvent.pageSize
        },this.fltrdRpt,this.sortOrder,this.sortCol,this.favRpts,this.recentRpts).takeWhile(() => this.alive).subscribe((res: any) => {
            this.length = +res.headers.get('x-count');
            this.reports = res.json();
        },
        (res: Response) => {
            this.reports =[];
        });
    }
    
    buildParameters(reqId, rptType) {
        this.parametersSubmited = [];
        this.reportsService.getParamsObj(reqId).takeWhile(() => this.alive).subscribe((res: any) => {
            let fnlObj = res.filterMap;
            this.displayParameters=true;
            fnlObj.fields.forEach(element => {
                let newParam = new ParametersSubmited();
                newParam.Parameter = element.fieldName;
                if (Array.isArray(element.selectedValue)) {
                    newParam.Value = element.selectedValue.join(', ');
                } else {
                    newParam.Value = element.selectedValue;
                }
                this.parametersSubmited.push(newParam);
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
            if(fnlObj.dateTimeVal){
                let repDtParam=new ParametersSubmited();
                repDtParam.Parameter="Report Date";
                let newDate = new Date(fnlObj.dateTimeVal);
                repDtParam.Value=this.datepipe.transform(newDate, 'medium').toString();
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
            if(fnlObj.outputType){
                let opParam=new ParametersSubmited();
                opParam.Parameter="Output Type";
                opParam.Value=fnlObj.outputType;
                this.parametersSubmited.push(opParam);
            }
            if(rptType=='ROLL_BACK_REPORT'){
                let colsCnt=0;
                for(var i=0;i<fnlObj.outputCols.length;i++){
                    if(fnlObj.outputCols[i].columnType=='ROLL_BACK'){
                        colsCnt++;
                    }
                };
                this.parametersSubmited.push({"Roll Back Count":colsCnt});
            }
            if(rptType=='AGING_REPORT'){
                let cols=[];
                for(var i=0;i<fnlObj.outputCols.length;i++){
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

    loadSortedReportList(event){
        this.sortOrder = 'Ascending';
        if (event.order < 1)
            this.sortOrder = 'Descending';
        this.sortCol = event.field;
        this.loadReportsList();
    }

    exportToCSV(reqId) {
        this.showProcessing=true;
        this.request=this.reportsService.downloadReportCSVFile(reqId).takeWhile(() => this.alive).subscribe((res: any) => {
            this.showProcessing=true;
        },(res: Response)=>{
            this.showProcessing=false;
        });
    }

    cancelRequest(){
        this.request.unsubscribe();
        this.showProcessing=false;
    }

    ngOnInit() {
        this.TemplatesHeight ='calc(100vh - 350px)';
        $(".reports-list-table").css({
            'height': 'calc(100vh - 150px)'
        });
    }

    public ngOnDestroy() {
        this.alive = false;
    }

    getColor(type: any) {
        switch (type) {
            case 'AGING_REPORT':
                return '#44a79f';
            case 'ACCOUNT_ANALYSIS_REPORT':
                return '#f0ad4e';
            case 'ROLL_BACK_REPORT':
                return 'rgb(127, 142, 6)';
            case 'ACCOUNT_BALANCE_REPORT':
                return '#0275d8';
        }
    }

    setFavReport(row:any){
        this.reportsService.setFavReport(row.id).takeWhile(() => this.alive).subscribe((res:any) => {
            row.isFavourite=true;
        })
    }

    removeFavRpt(row:any){
        this.reportsService.removeFavReport(row.id).takeWhile(()=> this.alive).subscribe((res:any)=>{
            row.isFavourite=false;
        })
    }

    myTest(){
        this.selectedTab=2;
    }

    step = 0;

    setStep(index: number) {
        this.step = index;
    }

    nextStep() {
        this.step++;
    }

    prevStep() {
        this.step--;
    }
}