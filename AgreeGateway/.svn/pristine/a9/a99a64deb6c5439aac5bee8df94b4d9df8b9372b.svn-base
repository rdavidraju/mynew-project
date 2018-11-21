import { Component, OnInit, ViewChild, ChangeDetectionStrategy, TemplateRef } from '@angular/core';
import { CommonService } from '../entities/common.service';
import { NavBarService } from '../layouts/navbar/navbar.service';
import Chart from 'chart.js';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

/* import { startOfDay, endOfDay, subDays, addDays, endOfMonth, isSameDay, isSameMonth, addHours } from 'date-fns';
import { Subject } from 'rxjs/Subject';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent } from 'angular-calendar'; */

declare var $: any;
declare var jQuery: any;

/* const colors: any = {
    red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
    },
    blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
    },
    yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
    }
}; */

@Component({
    selector: 'jhi-dashboardv4',
    templateUrl: './dashboard-v4.component.html',
    styleUrls: ['home.scss'],
    // changeDetection: ChangeDetectionStrategy.OnPush
})

export class DashboardV4Component implements OnInit {
    /* @ViewChild('modalContent') modalContent: TemplateRef<any>;

    view: string = 'month';
    viewDate: Date = new Date();
    modalData: {
      action: string;
      event: CalendarEvent;
    };
    actions: CalendarEventAction[] = [
      {
        label: '<i class="fa fa-fw fa-pencil"></i>',
        onClick: ({ event }: { event: CalendarEvent }): void => {
          this.handleEvent('Edited', event);
        }
      },
      {
        label: '<i class="fa fa-fw fa-times"></i>',
        onClick: ({ event }: { event: CalendarEvent }): void => {
          this.events = this.events.filter(iEvent => iEvent !== event);
          this.handleEvent('Deleted', event);
        }
      }
    ];
  
    refresh: Subject<any> = new Subject();
  
    events: CalendarEvent[] = [
      {
        start: subDays(startOfDay(new Date()), 1),
        end: addDays(new Date(), 1),
        title: 'A 3 day event',
        color: colors.red,
        actions: this.actions
      },
      {
        start: startOfDay(new Date()),
        title: 'An event with no end date',
        color: colors.yellow,
        actions: this.actions
      },
      {
        start: subDays(endOfMonth(new Date()), 3),
        end: addDays(endOfMonth(new Date()), 3),
        title: 'A long event that spans 2 months',
        color: colors.blue
      },
      {
        start: addHours(startOfDay(new Date()), 2),
        end: new Date(),
        title: 'A draggable and resizable event',
        color: colors.yellow,
        actions: this.actions,
        resizable: {
          beforeStart: true,
          afterEnd: true
        },
        draggable: true
      }
    ];
  
    activeDayIsOpen: boolean = true; */
    curModuleName:string = 'recon';

    constructor(
        private commonService: CommonService,
        private navbarService: NavBarService,
        // private modal: NgbModal
    ){}
    /* Reconciliation */
    reconColor:any = '#fda4b8';
    unReconColor:any = '#8ac9f3';
    reconColorTemp:any = '#fda4b8';
    unReconColorTemp:any = '#8ac9f3';
    tempSelectedPostion:any = 'bottom';
    selectedPostion:any = 'bottom';

    reconBackgroundTemp:any = '#ffffff';
    reconBackground:any = '#ffffff';
 /* Reconciliation */
 barChartOptionsReconciliation: any = {
    responsive: true,
    scales: {
        xAxes: [
            {
                stacked: true ,
                display:true ,
                ticks: {
                    min: 0,
                    max: 100,
                    callback: function (value) { return value + "%" }
                }
            }
        ],
        yAxes: [
            {
                stacked: true
            }
        ]
    },
      tooltips: {
        callbacks: {
            label: function (tooltipItems, data) {
                return data.datasets[tooltipItems.datasetIndex].label + ': '  + tooltipItems.xLabel  + '% - '+ data.datasets[0].proName[tooltipItems.index];
            }
        }
    },  
    legend: {
        position: 'bottom'
    },
    title: {
        display: false,
        text: 'Reconciliation'
    }
};
barChartLabelsReconciliation: string[];
barChartTypeReconciliation: string = 'horizontalBar';
barChartLegendReconciliation: boolean = true;
barChartDataReconciliation: any[];
dataReconciliationArr: any;
showCanvas: boolean = false;
public barChartReconciliationColors: Array<any> = [
        { 
            backgroundColor: this.reconColor
        },
        { 
            backgroundColor: this.unReconColor
        }
    ];

/* START Aging Wise Analysis */
pieChartLabels: string[];
pieChartData: number[];
pieChartType: string = 'pie';
pieChartOptions: any = {
    responsive: true,
    legend: {
        position: 'bottom'
    },
    title: {
        display: false,
        text: 'Aging wise analysis'
    },
    plugins: {
            datalabels: {
                display: false,
                color: 'white',
                font: {
                    weight: 'bold'
                }
            }
        }
};
    ngOnInit() {
        $(".dashboard4").css('min-height', (this.commonService.screensize().height - 161) + 'px');
        $(".reconBackgroundColor").css("background-color", this.reconBackground);
        let dateObj = {
            "startDate": '2018-02-01T15:58:18+05:30',
            "endDate": '2018-02-19T15:58:18+05:30'
        }
        this.navbarService.fetchCountsForDashBoardRecon(dateObj,10).subscribe((res: any) => {
            // this.tableView = true;
            console.log('barChartDataReconciliation ' + JSON.stringify(res));
            let objRec = [];
            let objUnRec = [];
            let objLabel = [];
            let objStack = [];
            this.dataReconciliationArr = res.reconciliationData;
            res.reconciliationData.forEach(element => {
                objRec.push(element.reconciledPer);
                objUnRec.push(element.unReconciledPer); 
                objLabel.push(element.label);
                objStack.push(element.stack);
            });
            this.barChartDataReconciliation = [
                { data: objRec, label: 'Reconciled', proName: objStack },
                { data: objUnRec, label: 'Un Reconciled', proName: objStack }
            ]; 
            this.barChartLabelsReconciliation = objLabel;
            if(this.barChartDataReconciliation.length > 0) {
                /**Aging Analysis */
                let obj = {
                    "status": 'Reconciled',
                    "dataviewName": res.reconciliationData[0].label,
                    "processId": 10,
                    "viewId": res.reconciliationData[0].viewId,
                    "ruleId": res.reconciliationData[0].ruleId,
                    "dvType": res.reconciliationData[0].dvType,
                    "tenantId": 5,
                    "startDate": "2018-02-01T15:58:18+05:30",
                    "endDate": "2018-02-19T15:58:18+05:30"
                }
                this.selectedStatus = 'Reconciled';
                this.selectedSource = res.reconciliationData[0].label;
                this.agingOnload(obj);
                this.showCanvas = true
            }
            console.log('this.barChartDataReconciliation ' + JSON.stringify(this.barChartDataReconciliation));
            console.log('this.barChartLabelsReconciliation ' + JSON.stringify(this.barChartLabelsReconciliation));
            console.log('recon ' + JSON.stringify(res.reconciliationData));
            console.log('recon label ' + JSON.stringify(res.rulesList));
            console.log('this.barChartDataReconciliation ' + JSON.stringify(this.barChartDataReconciliation));
            console.log('this.barChartLabelsReconciliation ' + JSON.stringify(this.barChartLabelsReconciliation));
        });
    }

    showAnalysis(moduleName) {
        this.curModuleName = moduleName;
    }

    selectedStatus:any;
    selectedSource:any;
     /* NEW Function To Display Aging Wise Analysis On Click Of Reconciliation */
     agingCC:boolean = false;
     agingApp:boolean = false;
     fetchAgingAnalysis(e: any): void {
         this.agingCC = false;
         this.agingApp = false;
         console.log(e);
         //console.log('this.userDetails ' + JSON.stringify(this.userDetails));
         $(document).ready(function () {
             $('.agingCls li a').click(function () {
                 $('.agingCls li a').removeClass("active");
                 $(this).addClass("active");
             });
             $('.agingAnalysisCls li a').click(function () {
                 $('.agingAnalysisCls li a').removeClass("active");
                 $(this).addClass("active");
             });
         });
         
         if (e.active.length > 0) {
             var chartElement = e.active[0]._chart.getElementAtEvent(event);
             console.log('chartElement[0] ' + chartElement[0]._index);
             console.log('chartElement  ' + chartElement[0]);
             this.selectedStatus = chartElement[0]._model.datasetLabel;
             this.selectedSource = chartElement[0]._model.label;
             console.log('this.dataReconciliationArr ' + JSON.stringify(this.dataReconciliationArr));
             console.log(chartElement[0]._model.datasetLabel);
             console.log(chartElement[0]._model.label);
             let obj = {
                 "status": chartElement[0]._model.datasetLabel,
                 "dataviewName": chartElement[0]._model.label,
                 "processId": 10,
                 "viewId": this.dataReconciliationArr[chartElement[0]._index].viewId,
                 "ruleId": this.dataReconciliationArr[chartElement[0]._index].ruleId,
                 "dvType": this.dataReconciliationArr[chartElement[0]._index].dvType,
                 "tenantId": 5,
                 "startDate": "2018-02-01T15:58:18+05:30",
                 "endDate": "2018-02-19T15:58:18+05:30"
             }
             console.log('obj ' + JSON.stringify(obj));
             this.pieChartData = [];
             this.pieChartLabels = [];
             /* this.barChartLabelsAging = [];
             this.barChartDataAging = []; */
             let obj1 = [];
             let obj2 = [];
             let objRec = [];
                     //  let objAcc = [];
                     let objApp = [];
                     let objLab = [];
             if(chartElement[0]._model.datasetLabel == "Reconciled"){
                 this.navbarService.getAgingWiseAnalysis(obj).subscribe((res: any) => {
                    
                     /* this.testHeight = $('#heightTest').height(); */
                     console.log('Reconciled Analysis ::' + JSON.stringify(res));
                     if(res.agingWithAnalysis.data.length>0){
                         res.agingWithAnalysis.data.forEach(element => {
                         element['approvalCount'] = 0;
                         obj1.push(element.age);
                         obj2.push(element.count);
                         
                         objRec.push(element.count);
                         objApp.push(element.approvalCount);
                         objLab.push(element.age);
                         }); 
                         this.pieChartLabels = obj1;
                         this.pieChartData = obj2;
                         if(this.pieChartData.length>0){
                            this.agingCC = true;
                        }else{
                            this.agingCC = false;
                        }
                        /*  this.barChartDataAging = [
                                 { data: objRec, label: 'Reconciled' },
                                 // { data: objAcc, label: 'Accounted' },
                                 { data: objApp, label: 'Approved' }
                         ];
                         this.barChartLabelsAging = objLab; 
                         if(this.pieChartData.length>0){
                             this.agingCC = true;
                         }else{
                             this.agingCC = false;
                         }
                         if(this.barChartDataAging.length>0){
                             this.agingApp = true;
                         }else{
                             this.agingApp = false;
                         } */
                         console.log('pieChartLabels ' + JSON.stringify(this.pieChartLabels));
                         console.log('pieChartData ' + JSON.stringify(this.pieChartData));
                         /* console.log('barChartDataAging ' + JSON.stringify(this.barChartDataAging));
                         console.log('barChartLabelsAging ' + JSON.stringify(this.barChartLabelsAging)); */
                         
                     }/* else{
                         this.notificationService.info('Info!', 'No Records found for selected view');
                     } */
                 });
 
             }else if(chartElement[0]._model.datasetLabel == "Un Reconciled"){
                 this.navbarService.fetchUnReconAnalysis(obj).subscribe((res: any) => {
                     console.log('Unreconciled Analysis ::' + JSON.stringify(res));
                     if(res.length>0){
                         res.forEach(element => {
                         element['approvalCount'] = 0;
                         obj1.push(element.age);
                         obj2.push(element.count);
                         
                         objRec.push(element.count);
                         objApp.push(element.approvalCount);
                         objLab.push(element.age);
                         }); 
                         this.pieChartLabels = obj1;
                         this.pieChartData = obj2;
                         if(this.pieChartData.length>0){
                            this.agingCC = true;
                        }else{
                            this.agingCC = false;
                        }
 /*     
                         this.barChartDataAging = [
                                 { data: objRec, label: 'Un-Reconciled' },
                                 // { data: objAcc, label: 'Accounted' },
                                 { data: objApp, label: 'Approved' }
                         ];
                         this.barChartLabelsAging = objLab; 
                         if(this.pieChartData.length>0){
                             this.agingCC = true;
                         }else{
                             this.agingCC = false;
                         }
                         if(this.barChartDataAging.length>0){
                             this.agingApp = true;
                         }else{
                             this.agingApp = false;
                         }
                         console.log('pieChartLabels ' + JSON.stringify(this.pieChartLabels));
                         console.log('pieChartData ' + JSON.stringify(this.pieChartData));
                         console.log('barChartDataAging ' + JSON.stringify(this.barChartDataAging));
                         console.log('barChartLabelsAging ' + JSON.stringify(this.barChartLabelsAging)); */
                     }/* else{
                         this.notificationService.info('Info!', 'No Records found for selected view');
                     } */
                 });
                 
             }/* else if(chartElement[0]._model.datasetLabel == "Not accounted"){
                 this.navbarService.fetchNotAccountedDataAnalysis(obj).subscribe((res: any) => {
                     console.log('Not accounted Analysis ::' + JSON.stringify(res));
                     if(res.length>0){
                         res.forEach(element => {
                         element['approvalCount'] = 0;
                         obj1.push(element.age);
                         obj2.push(element.count);
                         
                         objRec.push(element.count);
                         objApp.push(element.approvalCount);
                         objLab.push(element.age);
                         }); 
                         this.pieChartLabels = obj1;
                         this.pieChartData = obj2;
 
                         this.barChartDataAging = [
                                 { data: objRec, label: 'Not accounted' },
                                 // { data: objAcc, label: 'Accounted' },
                              { data: objApp, label: 'Approved' }
                         ];
                         this.barChartLabelsAging = objLab;
                         if(this.pieChartData.length>0){
                             this.agingCC = true;
                         }else{
                             this.agingCC = false;
                         }
                         if(this.barChartDataAging.length>0){
                             this.agingApp = true;
                         }else{
                             this.agingApp = false;
                         } 
                         console.log('pieChartLabels ' + JSON.stringify(this.pieChartLabels));
                         console.log('pieChartData ' + JSON.stringify(this.pieChartData));
                         console.log('barChartDataAging ' + JSON.stringify(this.barChartDataAging));
                         console.log('barChartLabelsAging ' + JSON.stringify(this.barChartLabelsAging));
                     }else{
                         this.notificationService.info('Info!', 'No Records found for selected view');
                     }
                 });
             } */
         }
     }

    agingOnload(obj) {
        let obj1 = [], obj2 = [], objRec = [], objApp = [], objLab = [];
        this.navbarService.getAgingWiseAnalysis(obj).subscribe((res: any) => {
            console.log('Reconciled Analysis ::' + JSON.stringify(res));
            if (res.agingWithAnalysis.data.length > 0) {
                res.agingWithAnalysis.data.forEach(element => {
                    element['approvalCount'] = 0;
                    obj1.push(element.age);
                    obj2.push(element.count);

                    objRec.push(element.count);
                    objApp.push(element.approvalCount);
                    objLab.push(element.age);
                });
                this.pieChartLabels = obj1;
                this.pieChartData = obj2;
                if (this.pieChartData.length > 0) {
                    this.agingCC = true;
                } else {
                    this.agingCC = false;
                }
            }
        });
    }

    cancelReconSetting(){
        this.reconColorTemp = this.reconColor;
        this.unReconColorTemp = this.unReconColor;
        this.tempSelectedPostion = this.selectedPostion;
        /* this.tempSelectedReconChart = this.pieChartReconciliationType; */
        this.reconBackgroundTemp = this.reconBackground;
        $(".reconBackgroundColor").css("background-color",this.reconBackgroundTemp);
    }
    saveReconSetting(){
        this.reconColor = this.reconColorTemp;
        this.unReconColor = this.unReconColorTemp;
        this.selectedPostion = this.tempSelectedPostion;
        /* this.pieChartReconciliationType = this.tempSelectedReconChart; */
        this.reconBackground = this.reconBackgroundTemp;
        console.log('this.reconBackground ' + this.reconBackground);
        $(".reconBackgroundColor").css("background-color",this.reconBackground);
        /* this.pieChartReconciliationColors = [ { backgroundColor: [this.reconColor, this.unReconColor] } ]; */
        this.barChartReconciliationColors = [
            { 
                backgroundColor: this.reconColor
            },
            { 
                backgroundColor: this.unReconColor
            }
        ];
        this.barChartOptionsReconciliation.legend = {
            position: this.selectedPostion
        }
        
        }
    }
    /* dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
        if (isSameMonth(date, this.viewDate)) {
            if (
                (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
                events.length === 0
            ) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
                this.viewDate = date;
            }
        }
    }
    
    eventTimesChanged({
        event,
        newStart,
        newEnd
    }: CalendarEventTimesChangedEvent): void {
        event.start = newStart;
        event.end = newEnd;
        this.handleEvent('Dropped or resized', event);
        this.refresh.next();
    }
    
    handleEvent(action: string, event: CalendarEvent): void {
        this.modalData = { event, action };
        this.modal.open(this.modalContent, { size: 'lg' });
    }
    
    addEvent(): void {
        this.events.push({
            title: 'New event',
            start: startOfDay(new Date()),
            end: endOfDay(new Date()),
            color: colors.red,
            draggable: true,
            resizable: {
                beforeStart: true,
                afterEnd: true
            }
        });
        this.refresh.next();
    } */


