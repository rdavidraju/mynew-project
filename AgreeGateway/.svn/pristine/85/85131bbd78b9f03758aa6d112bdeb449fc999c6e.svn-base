import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { BaseChartDirective } from 'ng2-charts/ng2-charts';
import { Account, LoginModalService, Principal } from '../shared';
import { SidebarModules, OverlayPanelModule } from '../shared/primeng/primeng';
import { DashBoard } from './home.model';
import { CommonService } from '../entities/common.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe,NgSwitch } from '@angular/common';
import { JhiDateUtils } from 'ng-jhipster';
import { trigger, state, style, animate, transition } from '@angular/animations';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import Chart from 'chart.js';
import { UserService } from '../shared/user/user.service';
import { NavBarService } from '../layouts/navbar/navbar.service';
import { SessionStorageService } from 'ng2-webstorage';
import { NotificationsService } from 'angular2-notifications-lite';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
//import 'chartjs-plugin-zoom';
declare var $: any;
declare var jQuery: any;

/**Material Dialog */
@Component({
    selector: 'dashboard-material-dialog',
    templateUrl: 'dashboard-material-dialog.html',
})
export class DashboardMaterialDialog implements OnInit{
    dashBoardDetails: any;
    isCustomRange: boolean = false;
    isViewOnly:any;
    //userDetails: any;
    processesList:any = [];
    constructor(
        public dialogRef: MdDialogRef<DashboardMaterialDialog>,
        public dialog: MdDialog,
        private navbarService: NavBarService,
        private $sessionStorage: SessionStorageService,
        @Inject(MD_DIALOG_DATA) public data: any) { }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit(){
        //this.userDetails = this.$sessionStorage.retrieve('userData');
        this.dashBoardDetails = this.data;
        this.navbarService.getprocessessList().subscribe((res: any) => {
            this.processesList = res;
            console.log('processesList ' + JSON.stringify(this.processesList));
        });
        console.log(this.dashBoardDetails);
    }

    filterData(){
        this.data = this.dashBoardDetails;
        this.dialogRef.close(this.data);
    }
    closeDia(){
        this.dialogRef.close();
    }

}

/**Main Component */
@Component({
    selector: 'jhi-dashboardv2',
    templateUrl: './dashboard-v2.component.html',
    styleUrls: [
        'home.scss'
    ],
    animations: [
        trigger('flyInOut1', [
            state('in', style({transform: 'translateY(0)'})),
            transition('void => *', [
            style({transform: 'translateY(-100%)'}),
            animate(1000)
            ]),
            transition('* => void', [
            animate(1000, style({transform: 'translateX(100%)'}))
            ])
        ]),
        trigger('flyInOut', [
                    state('in', style({ transform: 'translateX(0)' })),
                    transition('void => *', [
                        style({ transform: 'translateX(100%)' }),
                        animate(1000)
                    ])
                ])
        ]
})
export class DashboardV2Component implements OnInit {
    @ViewChild(BaseChartDirective) chart: BaseChartDirective;
    
    account: Account;
    modalRef: NgbModalRef;
    selectedDateRange: any = [];
    isCustomRange: boolean = false;
    tableView: boolean = false;
    dashBoardDetails: any = new DashBoard();
    //color = 'primary';
    extractionColor = '#ffa1b5';
    transformationColor = '#86c7f3';
    reconColor = '#ffe29a';
    accountingColor = '#93d9d9';
    journalsColor = '#ff9f40';
    mode = 'determinate';
    value = 50;
    extractionValue: any;
    transformationValue: any;
    reconciliationValue: any;
    accountingValue: any;
    journalsValue: any;
    selectedTab: any;
    isViewOnly:any;
    selectedTab1: any;
    selectedTab2:any;
    tableView1: boolean = false;
    visibleSidebar2;
    dashBoardData: any;
    notificationList: any = [];
    moduleType: any;
    dayType: any;
    processId:any;
    dateRange: any = "7 Days";
    endDate: any;
    startDate: any;
    process: any;
    ccReconProcess: any = [];
    processesList:any = [];
    userDetails: any;
    dataExtractionArr:any;
    dataTransformationArr:any;
    dataReconciliationArr:any;
    dataAccountingArr:any;

    /* START Aging With Accounting And Approved Analysis */
    /* Option For Aging With Accounting And Approved Analysis */
    public barChartOptionsAging: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        legend: {
            position: 'bottom'
        },
        title: {
            display: false,
            text: 'Aging with accounting and approved analysis'
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItem, data) {
                    var dslabels = data.labels[tooltipItem.index];
                    var dataset = data.datasets[tooltipItem.datasetIndex];
                    var dslabelamt = dataset.data[tooltipItem.index];
                    return data.datasets[tooltipItem.datasetIndex].label + ': ' + dslabelamt;
                }
            }
        }
    };
    public barChartLabelsAging: string[];
    public barChartTypeAging: string = 'line';
    public barChartLegendAging: boolean = true;
    public barChartDataAging: any[];

    /* END Aging With Accounting And Approved Analysis */
    
    /* Extraction */
    public barChartOptionsExtraction: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            xAxes: [
                {
                    ticks: {
                        fontSize: 10
                    },
                }
            ]
        },
        legend: {
            display: false,
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Extraction'
        }
    };
    public barChartLabelsExtraction: string[];
    public barChartTypeExtraction: string = 'horizontalBar';
    public barChartLegendExtraction: boolean = true;
    public barChartDataExtraction: any[];

    /* Transformation */
    public barChartOptionsTransformation: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            xAxes: [
                {
                    ticks: {
                        fontSize: 10
                    }
                }
            ]
        },
        legend: {
            display: false,
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Transformation'
        },
        zoom: {
            enabled: true,
            mode: 'xy'
        }
    };
    public barChartLabelsTransformation: string[]/*  = ['Profiles'] */;
    public barChartTypeTransformation: string = 'horizontalBar';
    public barChartLegendTransformation: boolean = true;
    public barTransformationChartColors: Array<any> = [
        {
            backgroundColor: 'rgb(134, 199, 243)'
        }
    ]
    public barChartDataTransformation: any[];

    
    /* Reconciliation */
    public barChartOptionsReconciliation: any = {
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
    public barChartLabelsReconciliation: string[];
    public barChartTypeReconciliation: string = 'horizontalBar';
    public barChartLegendReconciliation: boolean = true;

    public barChartDataReconciliation: any[];
    public barReconciliationChartColors: Array<any> = [
        {
            backgroundColor: 'rgba(92, 184, 92, 0.85)',
            borderColor: 'rgba(92, 184, 92, 0.85)',
            pointBackgroundColor: 'rgba(92, 184, 92, 0.85)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(92, 184, 92, 0.85)'
        },
        {
            backgroundColor: 'rgba(92, 184, 92, 0.34)',
            borderColor: 'rgba(92, 184, 92, 0.34)',
            pointBackgroundColor: 'rgba(92, 184, 92, 0.34)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(92, 184, 92, 0.34)'
        }
    ];
    /* Accounting */
    public barChartOptionsAccounting: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            xAxes: [
                {
                    stacked: true,
                    display:true,
                    ticks: {
                        fontSize: 10

                    }
                }
            ],
            yAxes: [
                {
                    stacked: true,
                     ticks: {
                        min: 0,
                        max: 100,
                        callback: function (value) { return value + "%" }
                    }, 
                    scaleLabel: {
                        display: true
                    }
                }
            ]
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data, typeCount,event) {
                      return data.datasets[tooltipItems.datasetIndex].label +': ' + data.datasets[tooltipItems.datasetIndex].typeCount[tooltipItems.index]+'('+tooltipItems.yLabel +' %)';
                }
            }
        },
        legend: {
            position: 'bottom'
        },
        title: {
            display: false,
            text: 'Accounting'
        }
    };
    public barChartLabelsAccounting: string[];
    public barChartTypeAccounting: string = 'bar';
    public barChartLegendAccounting: boolean = true;

    public barChartDataAccounting: any[];
    public barAccountingChartColors: Array<any> = [
        {
            backgroundColor: 'rgb(147, 217, 217)',
            borderColor: 'rgb(196, 234, 234)',
            pointBackgroundColor: 'rgb(196, 234, 234)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(196, 234, 234)'
        },
        {
            backgroundColor: 'rgb(196, 234, 234)',
            borderColor: 'rgb(196, 234, 234)',
            pointBackgroundColor: 'rgb(196, 234, 234)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgb(196, 234, 234)'
        }
    ];

    /* START Aging Wise Analysis */
    public pieChartLabels: string[];
    public pieChartData: number[];
    public pieChartType: string = 'pie';
    public pieChartOptions: any = {
        responsive: true,
        legend: {
            position: 'bottom'
        },
        title: {
            display: false,
            text: 'Aging wise analysis'
        }
    };

    step = 0;
    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private commonService: CommonService,
        private config: NgbDatepickerConfig,
        private datePipe: DatePipe,
        private dateUtils: JhiDateUtils,
        private userService: UserService,
        private navbarService: NavBarService,
        private $sessionStorage: SessionStorageService,
        private notificationService: NotificationsService,
        public dialog: MdDialog,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    changeMinDate() {
        this.config.minDate = this.dashBoardDetails.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    ngOnInit() {
        
      //  if (this.$sessionStorage.retrieve('userData')) {
            //this.userDetails = this.$sessionStorage.retrieve('userData');
      //  }
        //if(this.userDetails){
            this.navbarService.getprocessessList().subscribe((res: any) => {
                this.processesList = res;
                console.log('processesList ' + JSON.stringify(this.processesList));
                this.processId = this.processesList[0].id;
                this.process = this.processId;
                this.getEachDayAnalysisFun(this.processId);
                this.displayETRA(this.processId);
            });
       // }
        
        let strDate = new Date(Date.now());
            strDate.setDate(strDate.getDate() - 6);
        let enDate = new Date(Date.now());
        this.dashBoardDetails.startDate = strDate;
        this.dashBoardDetails.endDate = enDate;
        
        this.moduleType = "All";
        this.dayType = "7Days"
        
        this.getNotificationDetails();
        $('body').removeClass('login');
        $(document).ready(function () {
            $('.agingCls li a').click(function () {
                $('.agingCls li a').removeClass("active");
                $(this).addClass("active");
            });
            $('.agingAnalysisCls li a').click(function () {
                $('.agingAnalysisCls li a').removeClass("active");
                $(this).addClass("active");
            });
            $('.notiCls li a').click(function () {
                $('.notiCls li a').removeClass("active");
                $(this).addClass("active");
            });
        });
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
    
    
    /* Function For EachDayAnalysis Function */
    getEachDayAnalysisFun(proId){
        let stDate = new Date(Date.now());
            stDate.setDate(stDate.getDate() - 6);
        let endDate = new Date(Date.now());
        let eachDayAnalysisObj = {
            "startDate": stDate,
            "endDate": endDate
        }
        console.log('eachDayAnalysisObj ' + JSON.stringify(eachDayAnalysisObj));

        this.navbarService.fetchEachDayAnalysis(proId,eachDayAnalysisObj).subscribe((res: any) => {
            this.ccReconProcess = res;
            this.ccReconProcess.forEach(element => {
                if(element.transformation == null){
                    element.transformation = 0;
                }
                element['total'] = +((element.extraction + element.transformation + element.reconciliation + element.accounting + element.journals)/5).toFixed(2);
            });
            console.log('ccReconProcess ' + JSON.stringify(this.ccReconProcess));
            this.selectedTab = 0;
            this.extractionValue = this.ccReconProcess[this.selectedTab].extraction;
            this.transformationValue = this.ccReconProcess[this.selectedTab].transformation;
            this.reconciliationValue = this.ccReconProcess[this.selectedTab].reconciliation;
            this.accountingValue = this.ccReconProcess[this.selectedTab].accounting;
            this.journalsValue = this.ccReconProcess[this.selectedTab].journals;
        });
    }
    accountingActive:boolean;
    displayETRA(procId) {
        if (this.$sessionStorage.retrieve('userData')) {
            this.userDetails = this.$sessionStorage.retrieve('userData');
        }
        if (this.userDetails) {
            let dateObj = {
                "startDate": this.dashBoardDetails.startDate,
                "endDate": this.dashBoardDetails.endDate
            }
            console.log('dateObj ' + JSON.stringify(dateObj));
            this.navbarService.fetchCountsForDashBoardRecon(dateObj,procId).subscribe((res: any) => {
                this.tableView = true;
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
                console.log('this.barChartDataReconciliation ' + JSON.stringify(this.barChartDataReconciliation));
                console.log('this.barChartLabelsReconciliation ' + JSON.stringify(this.barChartLabelsReconciliation));
                console.log('recon ' + JSON.stringify(res.reconciliationData));
                console.log('recon label ' + JSON.stringify(res.rulesList));
                console.log('this.barChartDataReconciliation ' + JSON.stringify(this.barChartDataReconciliation));
                console.log('this.barChartLabelsReconciliation ' + JSON.stringify(this.barChartLabelsReconciliation));
            });
            this.navbarService.fetchCountsForDashBoardAccounted(dateObj,this.processId).subscribe((res: any) => {
                console.log('accres ' + JSON.stringify(res));
                this.barChartDataAccounting = res.accountingData;
                this.barChartLabelsAccounting = res.dvList;
                if(this.barChartDataAccounting.length>0){
                    this.accountingActive = true;
                }
                console.log('barChartDataAccounting ' + JSON.stringify(this.barChartDataAccounting));
                console.log('barchartlabacc' + JSON.stringify(this.barChartLabelsAccounting));
            });
            
            this.navbarService.fetchCountsForDashBoard(procId, dateObj).subscribe((res: any) => {
                this.dashBoardData = res;
                this.dataTransformationArr = res.dataTransformation;
                this.dataAccountingArr = res.accounting;
                console.log('this.dashBoardData ' + JSON.stringify(this.dashBoardData));
                /* Extraction */
                let objExt = [];
                let objExtLabel = [];
                this.dataExtractionArr = res.dataExtraction;
                res.dataExtraction.forEach(element => {
                    objExt.push(element.extractionCount);
                    objExtLabel.push(element.sourceProfileName);
                });
                this.barChartDataExtraction = [
                    { data: objExt}
                ];
                this.barChartLabelsExtraction = objExtLabel;
                console.log('this.barChartDataExtraction ' + JSON.stringify(this.barChartDataExtraction));
                console.log('this.barChartLabelsExtraction ' + JSON.stringify(this.barChartLabelsExtraction));
                /* Transformation */
                let objTrans = [];
                let objTransLabel = [];
                res.dataTransformation.forEach(element => {
                    objTrans.push(element.transformationCount);
                    objTransLabel.push(element.sourceProfileName);
                });
                this.barChartDataTransformation = [
                    { data: objTrans}
                ];
                this.barChartLabelsTransformation = objTransLabel;
                console.log('this.barChartDataTransformation ' + JSON.stringify(this.barChartDataTransformation));
                console.log('this.barChartLabelsTransformation ' + JSON.stringify(this.barChartLabelsTransformation));
               
            });
        }
    }

    getNotificationDetails() {
       // console.log('this.userDetails ' + JSON.stringify(this.userDetails));
        console.log('moduleType ' + this.moduleType + ' dayType ' + this.dayType);
        if (this.userDetails) {
            this.navbarService.fetchNotificationDetailsForDashBoard( this.moduleType, this.dayType).subscribe((res: any) => {
                if(this.moduleType == 'All'){
                    $('.allNoti').addClass("allNotiactive");
                    $('.reconNoti').removeClass("reconNotiactive");
                    $('.accNoti').removeClass("accNotiactive");
                    $('.jourNoti').removeClass("jourNotiactive");
                }else if(this.moduleType == 'RECON'){
                    $('.reconNoti').addClass("reconNotiactive");
                    $('.allNoti').removeClass("allNotiactive");
                    $('.accNoti').removeClass("accNotiactive");
                    $('.jourNoti').removeClass("jourNotiactive");
                }else if(this.moduleType == 'ACCOUNTING'){
                    $('.accNoti').addClass("accNotiactive");
                    $('.allNoti').removeClass("allNotiactive");
                    $('.reconNoti').removeClass("reconNotiactive");
                    $('.jourNoti').removeClass("jourNotiactive");
                }else if(this.moduleType == 'JOURNAL_ENTRY'){
                    $('.jourNoti').addClass("jourNotiactive");
                    $('.allNoti').removeClass("allNotiactive");
                    $('.reconNoti').removeClass("reconNotiactive");
                    $('.accNoti').removeClass("accNotiactive");
                }
                this.notificationList = res;
            });
        }
    }

    // events
    public chartClickedAccounting(e: any): void {
        console.log('chart clicked ' + e);
        if (e.active.length > 0) {
            var chartElement = e.active[0]._chart.getElementAtEvent(event);
            console.log(chartElement[0]._model.datasetLabel);
            console.log(chartElement[0]._model.label);

        }
    }

    public chartClicked(e: any, val, overlaypanel: any): void {
        console.log('chart clicked ' + e + ' val ' + val);
        if (e.active.length > 0) {
            overlaypanel.toggle(event);
            var chartElement = e.active[0]._chart.getElementAtEvent(event);
            console.log(chartElement[0]._model.datasetLabel);
            console.log(chartElement[0]._model.label);
        }
    }
    public chartHovered(e: any): void {
        console.log(e);

    }

    /* NEW Function To Display Aging Wise Analysis On Click Of Reconciliation */
    agingCC:boolean = false;
    agingApp:boolean = false;
    fetchAgingAnalysis(e: any): void {
        this.agingCC = false;
        this.agingApp = false;
        console.log('chart clicked ' + e);
        console.log('this.userDetails ' + JSON.stringify(this.userDetails));
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
            console.log('this.dataReconciliationArr ' + JSON.stringify(this.dataReconciliationArr));
            console.log(chartElement[0]._model.datasetLabel);
            console.log(chartElement[0]._model.label);
            let obj = {
                "status": chartElement[0]._model.datasetLabel,
                "dataviewName": chartElement[0]._model.label,
                "processId": this.processId,
                "viewId": this.dataReconciliationArr[chartElement[0]._index].viewId,
                "ruleId": this.dataReconciliationArr[chartElement[0]._index].ruleId,
                "dvType": this.dataReconciliationArr[chartElement[0]._index].dvType,
                "tenantId": this.userDetails.tenantId,
                "startDate": this.dashBoardDetails.startDate,
                "endDate": this.dashBoardDetails.endDate
            }
            console.log('obj ' + JSON.stringify(obj));
            this.pieChartData = [];
            this.pieChartLabels = [];
            this.barChartLabelsAging = [];
            this.barChartDataAging = [];
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

                        this.barChartDataAging = [
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
                        }
                        console.log('pieChartLabels ' + JSON.stringify(this.pieChartLabels));
                        console.log('pieChartData ' + JSON.stringify(this.pieChartData));
                        console.log('barChartDataAging ' + JSON.stringify(this.barChartDataAging));
                        console.log('barChartLabelsAging ' + JSON.stringify(this.barChartLabelsAging));
                        
                    }else{
                        this.notificationService.info('Info!', 'No Records found for selected view');
                    }
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
                        console.log('barChartLabelsAging ' + JSON.stringify(this.barChartLabelsAging));
                    }else{
                        this.notificationService.info('Info!', 'No Records found for selected view');
                    }
                });
                
            }else if(chartElement[0]._model.datasetLabel == "Not accounted"){
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
            }
        }
    }
    /* OLD Function To Display Aging Wise Analysis On Click Of Reconciliation */
    chartClickedReconciliation(e: any, obj: any): void {
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


        let dvId;
        let ruleId;
        console.log('this.userDetails ' + JSON.stringify(this.userDetails));
        if (e.active.length > 0) {
            var chartElement = e.active[0]._chart.getElementAtEvent(event);
            //console.log('chartElement  ' + e.active[0].getElementAtEvent(event));
            console.log(chartElement[0]._model.datasetLabel);
            console.log(chartElement[0]._model.label);
            this.dataReconciliationArr.forEach(element => {
                if (element.dataViewName == chartElement[0]._model.label) {
                    dvId = element.dvId;
                    ruleId = element.ruleId;
                }
            });
            let obj = {
                "status": chartElement[0]._model.datasetLabel,
                "dataviewName": chartElement[0]._model.label,
                "processId": this.processId,
                "viewId": dvId,
                "ruleId": ruleId,
                "tenantId": this.userDetails.tenantId,
                "startDate": this.dashBoardDetails.startDate,
                "endDate": this.dashBoardDetails.endDate
            }
            this.navbarService.getAgingWiseAnalysis(obj).subscribe((res: any) => {
                console.log('res ' + JSON.stringify(res));
                if (res.agingWithAnalysis.data.length > 0) {

                    this.pieChartData = [];
                    this.pieChartLabels = [];
                    let obj1 = [];
                    let obj2 = [];
                    this.barChartLabelsAging = [];
                    this.barChartDataAging = [];
                    let objRec = [];
                    //  let objAcc = [];
                    let objApp = [];
                    let objLab = [];
                    res.agingWithAnalysis.data.forEach(element => {
                        console.log('element1 ' + JSON.stringify(element));
                        obj1.push(element.age);
                        obj2.push(element.Count);
                        objRec.push(element.Count);
                        //objAcc.push(element.Accounted);
                        objApp.push(element.approvalCount);
                        objLab.push(element.age);
                    });
                    this.pieChartLabels = obj1;
                    this.pieChartData = obj2;
                    this.barChartDataAging = [
                        { data: objRec, label: 'Reconciled' },
                        // { data: objAcc, label: 'Accounted' },
                        { data: objApp, label: 'Approved' }
                    ];
                    this.barChartLabelsAging = objLab;
                    /*   this.chart.chart.config.data.labels = this.pieChartLabels;
                      this.chart.chart.update(); */

                } else {
                    this.notificationService.info('Info!', 'No Records found for selected view');
                }

                console.log('this.pieChartData ' + JSON.stringify(this.pieChartData));
                console.log('this.pieChartLabels ' + JSON.stringify(this.pieChartLabels));
            });
        }
    }

    loadContent(e) {
        console.log(e);
        this.selectedTab = e.index;
        this.extractionValue = this.ccReconProcess[this.selectedTab].extraction;
        this.transformationValue = this.ccReconProcess[this.selectedTab].transformation;
        this.reconciliationValue = this.ccReconProcess[this.selectedTab].reconciliation;
        this.accountingValue = this.ccReconProcess[this.selectedTab].accounting;
        this.journalsValue = this.ccReconProcess[this.selectedTab].journals;
    }

    changeReportView() {
        this.tableView1 = this.tableView1 ? false : true;
    }

    showFilterData() {
        let dialogRef = this.dialog.open(DashboardMaterialDialog, {
            width: '520px',
            data: {
                "process": this.process,
                "startDate": this.startDate,
                "endDate": this.endDate,
                "selectedDateRange": this.dateRange
            }
        });
        dialogRef.afterClosed().subscribe(result => {
            console.log(result);
            if(result){
                this.process = result.process;
                this.startDate = result.startDate;
                this.endDate = result.endDate;
                this.dateRange = result.selectedDateRange;
                if (this.dateRange == '7 Days') {
                    this.dashBoardDetails.startDate = new Date(Date.now());
                    this.dashBoardDetails.startDate.setDate(this.dashBoardDetails.startDate.getDate() - 6);
                    this.dashBoardDetails.endDate = new Date(Date.now());
                } else if (this.dateRange == '15 Days') {
                    this.dashBoardDetails.startDate = new Date(Date.now());
                    this.dashBoardDetails.startDate.setDate(this.dashBoardDetails.startDate.getDate() - 14);
                    this.dashBoardDetails.endDate = new Date(Date.now());
                } else if (this.dateRange == '30 Days') {
                    this.dashBoardDetails.startDate = new Date(Date.now());
                    this.dashBoardDetails.startDate.setDate(this.dashBoardDetails.startDate.getDate() - 29);
                    this.dashBoardDetails.endDate = new Date(Date.now());
                } else {
                    this.dashBoardDetails.startDate = this.startDate;
                    this.dashBoardDetails.endDate = this.endDate;
                }
                console.log('dashBoardDetails '  + JSON.stringify(this.dashBoardDetails));
                this.displayETRA(this.process);
            }
        });
    }
}