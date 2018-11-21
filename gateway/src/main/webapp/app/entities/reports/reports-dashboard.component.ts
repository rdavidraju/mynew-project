import { Component, OnInit, OnDestroy, HostListener, ElementRef, Renderer2, ViewChild,ViewChildren, QueryList } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Subscription, Subject } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { PageEvent } from '@angular/material';
import { Reports, ReportTypes, Dashboard, UseCase, ReportQueCard, UsecaseData } from './reports.model';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { ReportsService } from './reports.service';
import { Principal, ResponseWrapper } from '../../shared';
import { CommonService } from '../common.service';
import { ConfirmDialogComponent } from '../scheduling/confirm.dialog.component';
import { GridsterComponent } from 'angular2gridster';
import { IGridsterOptions } from 'angular2gridster';
import { IGridsterDraggableOptions } from 'angular2gridster';
import { WebDataRocksPivot } from "../../shared/webdatarocks/webdatarocks.angular4";
import { RunReportWizardComponent } from './run-wizard.component';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'd3';
import 'nvd3';
declare var d3: any;
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-reports',
    templateUrl: './reports-dashboard.component.html'
})
export class ReportsDashboardComponent implements OnDestroy, OnInit {
    @ViewChild(GridsterComponent) gridster: GridsterComponent;
    @ViewChildren(WebDataRocksPivot) webDataRocksPivots: QueryList<WebDataRocksPivot>;
    @ViewChild(RunReportWizardComponent) runReportWizardComponent: RunReportWizardComponent;
    private unsubscribe: Subject<void> = new Subject();
    gridsterOptions = {
        lanes: 4,
        direction: 'vertical',
        floating: true,
        dragAndDrop: true,
        resizable: true,
        useCSSTransforms: true,
        shrink: true,
        maxWidth: 1,
        maxHeight: 4,
        cellHeight:420
    };

    limitSelectionSettings = {
        text: " ",
        enableCheckAll: true,
        enableSearchFilter: true,
        badgeShowLimit: 1
    };
    
    gridsterDraggableOptions: IGridsterDraggableOptions = {
        handlerClass: 'panel-heading',
        scroll:true
    };

    displayDashboardDef: boolean=false;
    dashboardViewOnly: boolean=false;
    currentDashboard:Dashboard=new Dashboard();
    selectedDashBoard:number=0;
    currentUsecasesList:UseCase[]=[];
    usecaseList:any[]=[];
    rprtTempList:any[]=[];
    selRptCols:any[]=[];
    dashboardList:Dashboard[]=[];
    selDashBoardForView:Dashboard=new Dashboard();
    isEditDB:boolean=false;
    screenHeight:any;
    selectedUsecaseTitle: string ='';
    selectedReportId:any;
    displayRerunWizard:boolean=false;
    selUsecaseIndex:number; 
    selUsecase:any={};
    selectedReportName;
    
    options: {
        "viewType": "grid",
        "grid": {
            "type": "compact",
            "title": "",
            "showFilter": true,
            "showHeaders": false,
            "fitGridlines": false,
            "showTotals": true,
            "showGrandTotals": "on",
            "showExtraTotalLabels": false,
            "showHierarchies": true,
            "showHierarchyCaptions": true,
            "showReportFiltersArea": true,
            "pagesFilterLayout": "horizontal"
        },
        "configuratorActive": false,
        "configuratorButton": true,
        "configuratorMatchHeight": false,
        "showAggregations": true,
        "showCalculatedValuesButton": true,
        "editing": false,
        "drillThrough": true,
        "showDrillThroughConfigurator": true,
        "sorting": "on",
        "datePattern": "dd/MM/yyyy",
        "dateTimePattern": "dd/MM/yyyy HH:mm:ss",
        "saveAllFormats": false,
        "showDefaultSlice": true,
        "showEmptyData": false,
        "defaultHierarchySortName": "asc",
        "selectEmptyCells": true,
        "showOutdatedDataAlert": false
    }

    formats: [
        {
            "name": "",
            "thousandsSeparator": ",",
            "decimalSeparator": ".",
            "decimalPlaces": 2,
            "maxSymbols": 20,
            "currencySymbol": "",
            "currencySymbolAlign": "left",
            "nullValue": " ",
            "infinityValue": "Infinity",
            "divideByZeroValue": "Infinity"
        }
    ]

    constructor(
        private reportsService: ReportsService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private cs: CommonService,
        private dialog: MdDialog,
        public datepipe: DatePipe,
        private el: ElementRef,
        private renderer: Renderer2,
    ) {
        this.reportsService.getReportTemplates().takeUntil(this.unsubscribe).subscribe((res)=>{
            this.rprtTempList=res;
        },(res)=>{});
        this.loadAllDashboards();
    }

    loadAllDashboards(){
        this.usecaseList=[];
        this.reportsService.getDashboardsList().takeUntil(this.unsubscribe).subscribe((res:Dashboard[])=>{
            this.dashboardList=res;
            if(this.dashboardList.length>0){
                this.selDashBoardForView=this.dashboardList[0];
            }else{
                this.addNewDashboard();
            }
            this.dashboardList.forEach((item,i)=>{
                if(item.defaultFlag){
                    this.selDashBoardForView=item;
                }
            });
            this.loadDashBoardData(this.selDashBoardForView.id);
        },(res)=>{});
    }

    heightchange(i:number){
        
    }

    showFields(i:number){
        this.usecaseList[i].pivotOptions.options.configuratorActive=true;
        this.usecaseList[i].pivotOptions=this.usecaseList[i].pivotOptions;
    }

    exportReport(i:number,type:string){
        const options={filename:this.usecaseList[i].name,showFilters:true, header:this.usecaseList[i].name,pageOrientation:'landscape'};
        const pivots: WebDataRocksPivot[] = this.webDataRocksPivots.toArray();
        pivots[i].exportMyReport(type,options);
    }

    refreshPivot(ind:number){
        const pivots: WebDataRocksPivot[] = this.webDataRocksPivots.toArray();
        pivots[ind].refreshPivot();
    }

    customizeToolbar(toolbar) {
        toolbar.getTabs = function() {
            const tabs = [];
            tabs.push({
                id:"fm-tab-newtab",
                title:"My Tab",
                handler:newtabHandler,
                icon:`<i class="material-icons">alarm_on</i>`
            });
            return tabs;
          }
          const newtabHandler=function(){
            alert('Helloooo');
          }
    }

    refreshReport = function() {
        alert("New functionality!");
      }  

    ngOnInit() {
        this.screenHeight=+this.cs.screensize().height;
    }

    public ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.cs.destroyNotification(); 
    }

    saveDashboardPreferences(){
        const preferencesList:any[]=[];
        this.usecaseList.forEach((element) => {
            preferencesList.push({id:element.usecaseId.toString(),x: element.x, y: element.y, w: element.w, h: element.h});
        });
        this.reportsService.updatePreferences(preferencesList).takeUntil(this.unsubscribe).subscribe((res)=>{
            this.loadDashBoardData(this.selDashBoardForView.id);
        })
    }

    customizeCellFunction(cellBuilder, cellData) {
        if (
            cellData &&
            cellData.type == "header" &&
            cellData.member &&
            cellData.label == ""
        ) {
            cellBuilder.text = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cellData.member.caption;
        }
        if (cellData && cellData.type == "value" && !cellData.isTotalRow) {
            if (cellData.rowIndex % 2 == 0) {
                cellBuilder.addClass("odd-row");
            } else {
                cellBuilder.addClass("even-row");
            }
        }
    }

    toggleFullScreen(usecase,index){
        usecase.fullScreen=!usecase.fullScreen;
        // if(usecase.fullScreen){
        //     usecase.height=this.screenHeight; 
        // }else{
        //     usecase.height="300";
        // }
        // this.refreshPivot(index);
    }

    fullscreenUsecase:any;
    showFullScreenFlag:boolean=false;
    fullScreenIndex:number;
    showFullScreen(ind){
        this.showFullScreenFlag=true;
        this.fullScreenIndex=ind;
        this.fullscreenUsecase=this.usecaseList[ind];
    }

    exitFullScreen(){
        this.showFullScreenFlag=false;
        this.fullscreenUsecase=undefined;
    }

    loadDashBoardData(id:any){
        if(!id){
            return;
        }
        this.reportsService.getDashboardOutput(id).takeUntil(this.unsubscribe).subscribe((res)=>{
            this.usecaseList=res;
            this.usecaseList.forEach((element) => {
                element.fullScreen=false;
                if(element.type == 'TABLE'){
                    element.pivotOptions.options={
                        "viewType": "grid",
                        "grid": {
                            "type": "classic",
                            "showFilter": true,
                            "showHeaders": false,
                            "fitGridlines": true,
                            "showTotals": false,
                            "showGrandTotals": "on",
                            "showExtraTotalLabels": false,
                            "showHierarchies": false,
                            "showHierarchyCaptions": true,
                            "showReportFiltersArea": true,
                            "pagesFilterLayout": "horizontal"
                        },
                        "configuratorActive": false,
                        "configuratorButton": true,
                        "configuratorMatchHeight": false,
                        // "showAggregations": true,
                        "showCalculatedValuesButton": true,
                        "showAggregationLabels": false,
                        "editing": false,
                        "drillThrough": true,
                        "showDrillThroughConfigurator": true,
                        "sorting": "on",
                        "datePattern": "dd/MM/yyyy",
                        "dateTimePattern": "dd/MM/yyyy HH:mm:ss",
                        "saveAllFormats": true,
                        "showDefaultSlice": true,
                        "showEmptyData": false,
                        "defaultHierarchySortName": "asc",
                        "selectEmptyCells": false,
                        "showOutdatedDataAlert": false
                    }
                }else if (element.type == 'PIVOT') {
                    element.pivotOptions.options={
                        "viewType": "grid",
                        "grid": {
                            "type": "compact",
                            "showFilter": true,
                            "showHeaders": false,
                            "fitGridlines": true,
                            "showTotals": true,
                            "showGrandTotals": "on",
                            "showExtraTotalLabels": false,
                            "showHierarchies": true,
                            "showHierarchyCaptions": true,
                            "showReportFiltersArea": true,
                            "pagesFilterLayout": "horizontal"
                        },
                        "configuratorActive": false,
                        "configuratorButton": true,
                        "configuratorMatchHeight": false,
                        "showAggregationLabels": false,
                        // "showAggregations": true,
                        "showCalculatedValuesButton": true,
                        "editing": false,
                        "drillThrough": true,
                        "showDrillThroughConfigurator": true,
                        "sorting": "on",
                        "datePattern": "dd/MM/yyyy",
                        "dateTimePattern": "dd/MM/yyyy HH:mm:ss",
                        "saveAllFormats": true,
                        "showDefaultSlice": true,
                        "showEmptyData": false,
                        "defaultHierarchySortName": "asc",
                        "selectEmptyCells": false,
                        "showOutdatedDataAlert": false
                    };
                }
                element.pivotOptions.formats=[
                    {
                        "name": "",
                        "thousandsSeparator": ",",
                        "decimalSeparator": ".",
                        "decimalPlaces": 2,
                        "maxSymbols": 20,
                        "currencySymbol": "",
                        "currencySymbolAlign": "left",
                        "nullValue": " ",
                        "infinityValue": "Infinity",
                        "divideByZeroValue": "Infinity"
                    }
                ];
            });
        },(res)=>{});
    }
    
    reRunUsecase(index: number, usecase: any){
        this.selUsecaseIndex=index;
        this.selUsecase=usecase;
        this.selectedReportId=usecase.reportId;
        this.selectedReportName=usecase.name;
        this.runReportWizardComponent.getReqInfo(usecase.requestId)
    }

    toggleReportRunDialog(evt){
        if(!evt){
            this.refreshUsecase(this.selUsecaseIndex, this.selUsecase.usecaseId);
        }
        this.displayRerunWizard=evt;
    }

    refreshUsecase(index: number, id: any) {
        this.reportsService.getUsecaseOutput(id).takeUntil(this.unsubscribe).subscribe((res) => {
            this.usecaseList[index] = res;
            if (this.usecaseList[index].type == 'TABLE') {
                this.usecaseList[index].pivotOptions.options = {
                    "viewType": "grid",
                    "grid": {
                        "type": "classic",
                        "showFilter": true,
                        "showHeaders": false,
                        "fitGridlines": true,
                        "showTotals": false,
                        "showGrandTotals": "on",
                        "showExtraTotalLabels": false,
                        "showHierarchies": false,
                        "showHierarchyCaptions": true,
                        "showReportFiltersArea": true,
                        "pagesFilterLayout": "horizontal"
                    },
                    "configuratorActive": false,
                    "configuratorButton": true,
                    "configuratorMatchHeight": false,
                    // "showAggregations": true,
                    "showCalculatedValuesButton": true,
                    "showAggregationLabels": false,
                    "editing": false,
                    "drillThrough": true,
                    "showDrillThroughConfigurator": true,
                    "sorting": "on",
                    "datePattern": "dd/MM/yyyy",
                    "dateTimePattern": "dd/MM/yyyy HH:mm:ss",
                    "saveAllFormats": true,
                    "showDefaultSlice": true,
                    "showEmptyData": false,
                    "defaultHierarchySortName": "asc",
                    "selectEmptyCells": false,
                    "showOutdatedDataAlert": false
                }
            }else if (this.usecaseList[index].type == 'PIVOT') {
                this.usecaseList[index].pivotOptions.options = {
                    "viewType": "grid",
                    "grid": {
                        "type": "compact",
                        "showFilter": true,
                        "showHeaders": false,
                        "fitGridlines": true,
                        "showTotals": true,
                        "showGrandTotals": "on",
                        "showExtraTotalLabels": false,
                        "showHierarchies": true,
                        "showHierarchyCaptions": true,
                        "showReportFiltersArea": true,
                        "pagesFilterLayout": "horizontal"
                    },
                    "configuratorActive": false,
                    "configuratorButton": true,
                    "configuratorMatchHeight": false,
                    // "showAggregations": true,
                    "showAggregationLabels": false,
                    "showCalculatedValuesButton": true,
                    "editing": false,
                    "drillThrough": true,
                    "showDrillThroughConfigurator": true,
                    "sorting": "on",
                    "datePattern": "dd/MM/yyyy",
                    "dateTimePattern": "dd/MM/yyyy HH:mm:ss",
                    "saveAllFormats": true,
                    "showDefaultSlice": true,
                    "showEmptyData": false,
                    "defaultHierarchySortName": "asc",
                    "selectEmptyCells": false,
                    "showOutdatedDataAlert": false
                };
            }
            this.usecaseList[index].pivotOptions.formats = [
                {
                    "name": "",
                    "thousandsSeparator": ",",
                    "decimalSeparator": ".",
                    "decimalPlaces": 2,
                    "maxSymbols": 20,
                    "currencySymbol": "",
                    "currencySymbolAlign": "left",
                    "nullValue": " ",
                    "infinityValue": "Infinity",
                    "divideByZeroValue": "Infinity"
                }
            ];
            this.fullscreenUsecase=undefined;
            if(this.showFullScreenFlag){
                this.fullscreenUsecase=this.usecaseList[index];
            }
        });
    }

    itemResize(event:any){
        console.log(event);
        const a=this.usecaseList;
        this.usecaseList=[];
        this.usecaseList=a;
    }
 
    getReportCols(oneCase:UseCase, isView?:boolean){
        this.reportsService.getReportTemplateCols(oneCase.reportTempId).takeUntil(this.unsubscribe).subscribe((res)=>{
            oneCase.srcGroupbyCol=res.varCols;
            oneCase.srcColbyCol=res.varCols;
            oneCase.srcValbyCol=res.decimalCols;
            const rows=[],cols=[],vals=[];
            let colIds=[],valIds=[];
            
            if(oneCase.colbyCol){
                colIds = oneCase.colbyCol.split(',');
            }
            if(oneCase.valbyCol){
                valIds = oneCase.valbyCol.split(',');
            }
            res.decimalCols.forEach((element) => {
                if (valIds.indexOf((element.id).toString()) >= 0) {
                    vals.push(element);
                }
            });
            res.varCols.forEach((element) => {
                if(colIds.indexOf((element.id).toString())>=0){
                    cols.push(element);
                }
            });
            if(oneCase.outputType=='PIVOT'){
                const rowIds=oneCase.groupbyCol.split(',');
                res.varCols.forEach((element) => {
                    if(rowIds.indexOf((element.id).toString())>=0){
                        rows.push(element);
                    }
                });
            }
            oneCase.selGroupbyCol=rows;
            oneCase.selColbyCol=cols;
            oneCase.selValbyCol=vals;
        },(res)=>{});
    }

    addNewDashboard(){
        this.currentDashboard=new Dashboard();
        this.currentUsecasesList=[new UseCase()];
        this.displayDashboardDef=!this.displayDashboardDef;
    }

    removeUsecase(ind:number){
        this.currentUsecasesList.splice(ind,1);
        if(this.currentUsecasesList.length==0){
            this.currentUsecasesList.push(new UseCase());
        }
    }

    addNewUsecase(){
        this.currentUsecasesList.push(new UseCase());
    }

    showDashboardDef(id:any,isView?:boolean){
        this.reportsService.getDashboardDef(id).takeUntil(this.unsubscribe).subscribe((res:any)=>{
            this.currentDashboard=res.reportingDashboard;
            this.currentUsecasesList=res.reportingDashboardUsecasesList;
            this.currentUsecasesList.forEach((item) => {
                this.getReportCols(item,true);
            });
            if(isView){
                this.dashboardViewOnly=true;
            }
            if(this.isEditDB){
                this.currentUsecasesList.push(new UseCase());
            }
            this.displayDashboardDef=true;
        },(res:any)=>{
            this.cs.error('Oops...!','Failed to load dashboard definition');
        });
    }

    deleteDashboard(name:string,id:any){
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            data: { message: name+" will delete permanently, do you want continue?", ok: 'ok', cancel: 'no', okLabel: 'Yes', cancelLabel: 'No, Cancel' }
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
                this.reportsService.deleteDashboard(id).takeUntil(this.unsubscribe).subscribe((res)=>{
                    if(res==200){
                        this.cs.info('Done!',name+' Deleted!')
                        this.loadAllDashboards();
                    }
                })
            }
        });
    }

    checkDashBoardName(){
        this.reportsService.checkDashboardName(this.currentDashboard.name).takeUntil(this.unsubscribe).subscribe((cnt:number)=>{
            if(this.currentDashboard.id && this.currentDashboard.id==cnt){
                return;
            }else if(cnt>0){
                this.cs.info('Oops..!','Name already exists!');
                this.currentDashboard.name=undefined;
            }
        })
    }

    saveDashboard(){
        for (let i = 0; i < this.currentUsecasesList.length; i++) {
            this.currentUsecasesList[i].seqNum = i + 1;
            if (this.currentUsecasesList[i].outputType == 'PIVOT') {
                let tempRow = '';
                for (let j = 0; j < this.currentUsecasesList[i].selGroupbyCol.length; j++) {
                    if (j > 0) {
                        tempRow = tempRow + ',';
                    }
                    tempRow = tempRow + this.currentUsecasesList[i].selGroupbyCol[j].id;
                }
                this.currentUsecasesList[i].groupbyCol = tempRow;
            }
            let tempCol = '';
            for (let j = 0; j < this.currentUsecasesList[i].selColbyCol.length; j++) {
                if (j > 0) {
                    tempCol = tempCol + ',';
                }
                tempCol = tempCol + this.currentUsecasesList[i].selColbyCol[j].id;
            }
            this.currentUsecasesList[i].colbyCol = tempCol;
            let tempVal = '';
            for (let j = 0; j < this.currentUsecasesList[i].selValbyCol.length; j++) {
                if (j > 0) {
                    tempVal = tempVal + ',';
                }
                tempVal = tempVal + this.currentUsecasesList[i].selValbyCol[j].id;
            }
            this.currentUsecasesList[i].valbyCol = tempVal;
        }
        const finalUsecase:UseCase[] = this.currentUsecasesList.map((a) => {
            const newObject = {};
            Object.keys(a).forEach((propertyKey) => {
                 newObject[propertyKey] = a[propertyKey];
            });
            return newObject ;
        });
        if(this.isEditDB){
            const maxY=Math.max.apply(Math, finalUsecase.map(function(o) { return o.yAxis; }));
            let tmp=1;
            for(let i=0;i<finalUsecase.length;i++){
                if(finalUsecase[i].yAxis==-1){
                    finalUsecase[i].yAxis=maxY+tmp;
                    tmp=tmp+1;
                }
            }
        }else{
            for(let i=0;i<finalUsecase.length;i++){
                finalUsecase[i].yAxis=i;
            } 
        }
        for(let i=0;i<finalUsecase.length;i++){
            delete finalUsecase[i].srcGroupbyCol;
            delete finalUsecase[i].srcValbyCol;
            delete finalUsecase[i].srcColbyCol;
            delete finalUsecase[i].selGroupbyCol;
            delete finalUsecase[i].selValbyCol;
            delete finalUsecase[i].selColbyCol;
        }
        
        this.reportsService.postDashboard({"reportingDashboard":this.currentDashboard,"reportingDashboardUsecasesList":finalUsecase}).takeUntil(this.unsubscribe).subscribe((res)=>{
            this.cs.success('Done!','Saved, Successfully!');
            this.displayDashboardDef=false;
            this.loadAllDashboards();
        },
        (res:Response)=>{
            this.cs.error('Oops..!','Failed to save!');
        });
    }
}