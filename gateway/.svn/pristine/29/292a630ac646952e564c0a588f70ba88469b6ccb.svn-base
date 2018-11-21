import { Component, OnInit, ViewChild, ChangeDetectionStrategy, TemplateRef, Inject, OnDestroy, ViewEncapsulation } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { CommonService } from '../../entities/common.service';
import { DashboardService } from './dashboardver.service';
import { NavBarService } from '../../layouts/navbar/navbar.service';
import { DashBoard4, ModuleAnalysis, DashBoardValues } from '../.././home';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ng2-webstorage';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { DecimalPipe, DatePipe } from '@angular/common';
import { JhiDateUtils, JhiAlertService } from 'ng-jhipster';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { NotificationsService } from 'angular2-notifications-lite';
import { ProcessesService } from '../../entities/processes/processes.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DaterangepickerConfig } from 'ng2-daterangepicker';
import { UserPreference } from './dashboardv2.model';
import { Subscription } from 'rxjs/Rx';
import Chart from 'chart.js';
import 'moment';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'chartjs-plugin-datalabels';
declare var $: any;
declare var jQuery: any;
declare var moment: any;

@Component({
    selector: 'jhi-dashboardv7',
    templateUrl: './dashboard-v7.component.html',
    animations: [
        trigger('flyInOut', [
            state('in', style({ transform: 'translateX(0)' })),
            transition('void => *', [
                style({ transform: 'translateX(-100%)' }),
                animate(500)
            ])/* ,
            transition('* => void', [
                animate(900, style({ transform: 'translateX(100%)' }))
            ]) */
        ])
    ],
    encapsulation: ViewEncapsulation.None
})



export class DashboardV7Component implements OnInit, OnDestroy {
    private alive: boolean = true;      //Unsubscribe variable
    autoTicks = false;
    disabled = false;
    invert = false;
    max = 15;
    min = 0;
    showTicks = false;
    step = 1;
    thumbLabel = true;
    value = 0;
    vertical = false;

    get tickInterval(): number | 'auto' {
        return this.showTicks ? (this.autoTicks ? 'auto' : this._tickInterval) : 0;
    }
    set tickInterval(v) {
        this._tickInterval = Number(v);
    }
    private _tickInterval = 1;
    maxDate: Date;
    minDate: Date;
    request: any;
    showbalgrid: boolean = false;
    drillThruSeconds: any;
    dashBoardLayOutObj = new DashBoardValues();
    dashBoardLayOutObjTemp = new DashBoardValues();
    processFilter = new DashBoard4();
    tempProcessFilter = new DashBoard4();
    moduleAnalysisList = new ModuleAnalysis();
    selectedReconAnalysis: any = 0;
    blockedDocument: boolean = false;
    customizationMode: boolean = false;
    processesList: any; //variable to store processes list
    selectedProcessId: any; //variable to store selected processes id
    selectedProcessName: any; //variable to store selected processes name
    processesDetailsList: any = [];
    calenderDetails: any;
    activeItem: any;
    oneWeekObj: any;
    accountingDateObj: any;
    reconDateObj: any;
    tempDateObj: any;
    visibleSidebar2;
    openModuleSetting: any;
    showetl: boolean = false;
    showrecon: boolean = true;
    showacc: boolean = false;
    setThemeColor: any;
    showReconViolationEdit: boolean = false;
    showAccountingViolationEdit: boolean = false;
    dateRangeLov: any = [{ "name": "Yesterday" }, { "name": "This Month" }, { "name": "Custom Date Range" }];
    isStartDateChanged: boolean = false;
    isEndDateChanged: boolean = false;
    dateRangeactiveItem: any;
    private timer;
    public selectedDate(value: any) {
        this.newrequest = true;
        this.processFilter.endDate = value.end._d;
        this.processFilter.startDate = value.start._d;
    }
    selectedDateRangeOption: any = [
        {
            "value": "Today",
            "name": "1D",
            "checked": false,
            "title": "Today",
            "type": "default"
        },
        {
            "value": "Last 7 Days",
            "name": "1W",
            "checked": false,
            "title": "1 Week",
            "type": "default"
        },
        {
            "value": "Last 14 Days",
            "name": "2W",
            "checked": false,
            "title": "2 Weeks",
            "type": "default"
        },
        {
            "value": "Last 30 Days",
            "name": "1M",
            "checked": false,
            "title": "1 Month",
            "type": "default"
        },
        {
            "value": "Last 2Months",
            "name": "2M",
            "checked": false,
            "title": "Last 2 Months",
            "type": "default"
        },
        {
            "value": "Last 3Months",
            "name": "3M",
            "checked": false,
            "title": "Last 3 Months",
            "type": "default"
        },
        {
            "value": "Last 6Months",
            "name": "6M",
            "checked": false,
            "title": "Last 6 Months",
            "type": "default"
        },
        {
            "value": "Yesterday",
            "name": "Yesterday",
            "checked": false,
            "title": "Yesterday",
            "type": "default"
        },
        {
            "value": "This Month",
            "name": "This Month",
            "checked": true,
            "title": "This Month",
            "type": "default"
        }
    ];
    /* Recon variables used */
    selectedReconFilterValue = "Amount";
    showReconViewSpecificChart: boolean = false;
    tempReconChartData: Array<any> = [];
    tempReconChartLabels: Array<any> = [];
    showReconPreviousButton: any;
    splitReconCard: boolean = true;
    showReconChart: boolean = false;
    reconViewChartDisplay: boolean = true;
    currentReconAnalysisData: any;
    modulelist: any = ['extraction', 'reconciliation', 'accounting'];
    rangeTo: any;
    rangeFrom: any;
    newrequest: boolean = false;
    newrequestviol: boolean = false;
    violationPeriod: any;
    reconPeriodAnalysisType: any;
    reconPeriodPreviousDateRange: any = [];
    spName: any;
    spNames: any = [];
    reconRg: any;
    reconRgId: any;
    accountingRg: any;

    /* Dummy Data for reconciliation chart */
    reconciliationAnalysisData: Array<any> = [];
    reconViewSpecificAnalysis: any;
    reconDrillDownAnalysisData: Array<any> = [];
    reconWeekDrillDownAnalysisData: Array<any> = [];
    reconViewSpecificDrillDownAnalysis: Array<any> = [];
    selectedPeriodForReconMonth: any;
    selectedPeriodForReconWeek: any;
    showReconViewChart: boolean = false;

    /* Reconciliation Chart Variables */
    public reconChartData: Array<any> = [];
    public reconChartLabels: Array<any> = [];
    public reconChartType: string = 'bar';
    public reconChartLegend: boolean = true;
    public lineChartLegend: boolean = true;
    public reconChartOptions: any = {};
    public reconChartColors: Array<any> = [];

    public dummyChartType: string = 'pie';

    /* Recon View Specific */
    public reconViewSpecificChartData: Array<any> = [];
    public reconViewSpecificChartLabels: Array<any> = [];
    public reconViewSpecificChartType: string = 'pie';
    public comboViewSpecificChartType: string = 'bar';
    public reconViewSpecificChartLegend: boolean = true;
    public viewSpecificChartLegend: boolean = false;
    public reconViewSpecificChartColors: {}[];
    public reconViewSpecificChartOptions: any = {};

    /* Accounting variables used */
    splitAccountingCard: boolean = true;
    showAccountingChart: boolean = false;
    accountingViewChartDisplay: boolean = true;
    showAccountingPreviousButton: any;
    currentAccountingAnalysisData: any;
    selectedAccountingFilterValue = "Amount";
    accountingPeriodAnalysisType: any;
    accountingPeriodPreviousDateRange: any = [];
    notAccountingStatus: boolean = false;
    pendingJEStatus: boolean = false;
    jeCreationStatus: boolean = false;
    accountingGroupByLov: any;
    accountingSelectedViewName: any;
    accountingSelectedViewId: any;
    accountingGrpByView: any;
    acctempSelectedLabel: any = 'Not Accounted';
    showAccountingViewChart: boolean = false;
    selectedPeriodForAccountingMonth: any;
    selectedPeriodForAccountingWeek: any;
    accountingSelectedMonthIndex: any;
    accountingSelectedWeekIndex: any;
    dateRangeForViewAcc: any;

    /* Dummy Data for accounting chart */
    accountingAnalysisData: Array<any> = [];
    accountingViewSpecificAnalysis: any;
    accountingDrillDownAnalysisData: Array<any> = [];
    accountingWeekDrillDownAnalysisData: Array<any> = [];
    accountingViewSpecificDrillDownAnalysis: Array<any> = [];

    /* accounting Chart Variables */
    public accountingChartData: Array<any> = [];
    public accountingChartLabels: Array<any> = [];
    public accountingChartType: string = 'bar';
    public accountingChartLegend: boolean = true;
    public accountingChartOptions: any = {};
    public accountingChartColors: Array<any> = [];

    /* accounting View Specific */
    public accountingViewSpecificChartDataDetailed: Array<any> = [];
    public accountingViewSpecificChartLabelsDetailed: Array<any> = [];
    public accountingViewSpecificChartTypeDetailed: string = 'pie';
    public accountingViewSpecificChartLegendDetailed: boolean = true;
    public accountingViewSpecificChartData: Array<any> = [];
    public accountingViewSpecificChartLabels: Array<any> = [];
    public accountingViewSpecificChartType: string = 'bar';
    public accountingViewSpecificChartLegend: boolean = false;
    public accountingViewSpecificChartColors: {}[] = [{ backgroundColor: ['#4285F4', '#34A853', '#FBBC05'] }];
    public accountingViewSpecificChartOptions: any = {};

    /* ETL variables used */
    etlPeriodAnalysisType: any;
    etlPeriodPreviousDateRange: any = [];
    showETLChart: boolean = false;
    selectedETLFilterValue = "Amount";
    extractionFailedStatus: boolean = false;
    extractedStatus: boolean = false;
    transformationFailedStatus: boolean = false;
    transformedStatus: boolean = false;
    currentETLAnalysisData: any;
    showETLPreviousButton: any;
    dateRangeForViewETL: any;
    splitETLCard: boolean = false;
    etltempSelectedLabel: any;
    showETLViewChart: boolean = false;
    etlSelectedMonthIndex: any;
    etlSelectedWeekIndex: any;
    etlViewChartDisplay: boolean = false;
    etlTemplatesLov: any;

    /* ETL Chart Variables */
    public etlChartData: Array<any> = [];
    public etlChartLabels: Array<any> = [];
    public etlChartType: string = 'bar';
    public etlChartLegend: boolean = true;
    public etlChartOptions: any = {};
    public etlChartColors: Array<any> = [];

    etlAnalysisData: Array<any> = [];
    etlViewSpecificAnalysis: any;
    etlDrillDownAnalysisData: Array<any> = [];
    etlWeekDrillDownAnalysisData: Array<any> = [];
    etlViewSpecificDrillDownAnalysis: Array<any> = [];
    selectedPeriodForETLMonth: any;
    selectedPeriodForETLWeek: any;

    /* ETL View Specific */
    public etlViewSpecificChartData: Array<any> = [];
    public etlViewSpecificChartLabels: Array<any> = [];
    public etlViewSpecificChartType: string = 'pie';
    public etlViewSpecificChartLegend: boolean = true;
    public etlViewSpecificChartColors: {}[];
    public etlViewSpecificChartOptions: any = {};



    dataviewdata: any = []
    dataviewlabels: any = [/* 'Aplha Source DV', 'Aplha Target DV' */];
    dataviewdataset: any = [
        /* {
            label: 'One-One',
            data: [47.8, 12],
        },
        {
            label: 'One-Many',
            data: [20.7, 48],
        },
        {
            label: 'Many-Many',
            data: [11.4, 22],
        } */
    ];
    stackedchartcolors: any = [];
    comboViewSpecificChartOptions = {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom',
            display: false,
            labels: {
                fontColor: this.setThemeColor
            }
        },
        layout: {
            padding: {
                left: 10,
                right: 10
            }
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + ' %';
                }
            }
        },
        plugins: {
            datalabels: {
                display: true,
                color: 'black',
                font: {
                    weight: 'bold'
                },
                formatter: function (value, context) {
                    return value + '%';
                }
            }
        },
        scales: {
            xAxes: [{
                stacked: true,
                gridLines: {
                    display: false
                }
            }],
            yAxes: [{
                stacked: true,
                ticks: {
                    min: 0,
                    max: 100,
                    callback: function (value) { return value + "%" },
                    fontColor: this.setThemeColor
                },
                gridLines: {
                    display: false
                }
            }]
        }
    };
    viewSpecificChartOptions = {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom',
            display: false,
            labels: {
                fontColor: this.setThemeColor
            }
        },
        layout: {
            padding: {
                left: 10,
                right: 10
            }
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.labels[tooltipItems.index] + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + ' %';
                }
            }
        },
        plugins: {
            datalabels: {
                display: true,
                color: 'black',
                font: {
                    weight: 'bold'
                },
                formatter: function (value, context) {
                    return value + '%';
                }
            }
        },
        scales: {
            xAxes: [{
                stacked: false,
                gridLines: {
                    display: false
                }
            }],
            yAxes: [{
                stacked: false,
                ticks: {
                    min: 0,
                    max: 100,
                    callback: function (value) { return value + "%" },
                    fontColor: this.setThemeColor
                },
                gridLines: {
                    display: false
                }
            }]
        }
    };






    constructor(
        public dialog: MdDialog,
        private commonService: CommonService,
        private dashboardService: DashboardService,
        private navbarService: NavBarService,
        private config: NgbDatepickerConfig,
        private $sessionStorage: SessionStorageService,
        private datePipe: DatePipe,
        private dateUtils: JhiDateUtils,
        private notificationsService: NotificationsService,
        private alertService: JhiAlertService,
        private processesService: ProcessesService,
        private router: Router,
        private daterangepickerOptions: DaterangepickerConfig
    ) {
        //this.etlChartColors = this.commonService.etlRandomColors
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.maxDate = new Date();
        this.reconViewSpecificChartColors = this.commonService.randomColors;
        this.etlViewSpecificChartColors = this.commonService.etlRandomColors;
        this.stackedchartcolors = this.commonService.stackedRandomColors;
        this.request = this.navbarService.themeCol$.takeWhile(() => this.alive).subscribe(
            data => {
                if (data == 'white') {
                    this.setThemeColor = 'white';
                } else {
                    this.setThemeColor = '#666666';
                }
                this.reconAnalysisOptionFun();
                this.reconViewAnalysisOptionFunction();
                this.etlAnalysisOptionFun();
            });
        this.daterangepickerOptions.settings = {
            locale: { format: 'DD-MMM-YYYY' },
            alwaysShowCalendars: false,
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment()],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                'Last 3Months': [moment().subtract(2, 'month').startOf('month'), moment()],
                'Last 6Months': [moment().subtract(6, 'month').startOf('month'), moment()]
            },
            //opens: 'left',
            autoApply: true,
        };
    }
    getmindate() {
        console.log('minDate this.processFilter.startDate ' + this.processFilter.startDate);
        console.log('minDate this.processFilter.endDate ' + this.processFilter.endDate);
        this.minDate = this.processFilter.startDate;
    }
    changeMinDate() {
        this.config.minDate = this.processFilter.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    incre(value, type) {
        if (type == 'reconciliation') {
            this.reconViolationPeriod = value + 1;
        } else {
            this.tempaccountingViolationPeriod = value + 1;
        }
    }
    decre(value, type) {
        if (type == 'reconciliation') {
            this.reconViolationPeriod = value - 1;
        } else {
            this.tempaccountingViolationPeriod = value - 1;
        }
    }
    editviolation() {
        this.newrequestviol = true;
    }

    ngOnInit() {

        $(".dashboardV7").css('min-height', (this.commonService.screensize().height - 161) + 'px');
        this.request = this.navbarService.getFormConfigDetails('dashboardV1', 'customHeadings', 'user').takeWhile(() => this.alive).subscribe((res: any) => {
            if (res) {
                let temp = JSON.parse(res.value);
                this.dashBoardLayOutObj = temp;
                this.dashBoardLayOutObjTemp = temp;
                if (this.dashBoardLayOutObj.drillThruSeconds) {
                    this.drillThruSeconds = this.dashBoardLayOutObj.drillThruSeconds;
                } else {
                    this.drillThruSeconds = 3000;
                }
            }
        });
        if (this.dashBoardLayOutObj != undefined) {
            this.fetchProcessessList();
        }
    }
    ngOnDestroy() {
        this.notificationsService.remove();
        $('.spinner, .clockwise').removeClass('hidden');
        this.alive = false;
        this.request.unsubscribe();
    }
    toggleRange(e) {
        //console.log('range ' + e.value);
        this.isStartDateChanged = false;
        this.isEndDateChanged = false;
        this.selectedMoreRange = false;
        let selectedRange: any;
        if (e.value == 'Today') {
            selectedRange = [moment(), moment()];
        } else if (e.value == 'Last 7 Days') {
            selectedRange = [moment().subtract(6, 'days'), moment()];
        } else if (e.value == 'Last 14 Days') {
            selectedRange = [moment().subtract(13, 'days'), moment()];
        } else if (e.value == 'Last 30 Days') {
            selectedRange = [moment().subtract(29, 'days'), moment()];
        } else if (e.value == 'Last 2Months') {
            selectedRange = [moment().subtract(1, 'month').startOf('month'), moment()];
        } else if (e.value == 'Last 3Months') {
            selectedRange = [moment().subtract(2, 'month').startOf('month'), moment()];
        } else if (e.value == 'Last 6Months') {
            selectedRange = [moment().subtract(5, 'month').startOf('month'), moment()];
        } else if (e.value == 'Custom Date Range') {
            this.selectedMoreRange = true;
        } else if (e.value == 'Yesterday') {
            selectedRange = [moment().subtract(1, 'days'), moment().subtract(1, 'days')];
        } else if (e.value == 'This Month') {
            selectedRange = [moment().startOf('month'), moment()];
        } else {
            selectedRange = [moment().subtract(this.dateDifference, 'days'), moment()];
          }
        if (e.value != 'Custom Date Range') {
            this.processFilter.startDate = new Date(selectedRange[0]);
            this.processFilter.endDate = new Date(selectedRange[1]);
            this.loadall();
        }
        this.newrequest = true;
    }
    selectedMoreRange: boolean = false;
    dateDifference: any;
    changeDateRange(val) {
        console.log('this.processFilter.startDate ' + this.processFilter.startDate);
        console.log('this.processFilter.endDate ' + this.processFilter.endDate);
        for (let i = 0; i < this.selectedDateRangeOption.length; i++) {
            this.selectedDateRangeOption[i]['checked'] = false;
            if (this.selectedDateRangeOption[i].type == "custom") {
                this.selectedDateRangeOption.splice(i, 1);
            }
        }
        let oneDay = 24 * 60 * 60 * 1000;
        let selectedRange: any;
        this.dateDifference = Math.round(Math.abs((this.processFilter.startDate.getTime() - this.processFilter.endDate.getTime()) / (oneDay)));
        console.log('Date Diff: ' + this.dateDifference);
        if (this.dateDifference == 1) {
            selectedRange = [moment(), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Today')].checked = true;
        } else if (this.dateDifference == 7) {
            selectedRange = [moment().subtract(7, 'days'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Last 7 Days')].checked = true;
        } else if (this.dateDifference == 14) {
            selectedRange = [moment().subtract(14, 'days'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Last 14 Days')].checked = true;
        } else if (this.dateDifference == 30) {
            selectedRange = [moment().subtract(30, 'days'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Last 30 Days')].checked = true;
        } else if (this.dateDifference == 60) {
            selectedRange = [moment().subtract(1, 'month').startOf('month'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Last 2Months')].checked = true;
        } else if (this.dateDifference == 90) {
            selectedRange = [moment().subtract(2, 'month').startOf('month'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Last 3Months')].checked = true;
        } else if (this.dateDifference == 180) {
            selectedRange = [moment().subtract(5, 'month').startOf('month'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex(x => x.value == 'Last 6Months')].checked = true;
        } else {
            selectedRange = [moment().subtract(this.dateDifference-1, 'days'), moment()];
            this.selectedDateRangeOption.push({
                checked: true,
                value: this.dateDifference + 'Days',
                title: 'Last ' + this.dateDifference + ' Days',
                name: this.dateDifference + ' Days',
                type: "custom"
            });
        }
        console.log('selectedRange ' + selectedRange);
        this.processFilter.startDate = new Date(selectedRange[0]);
        this.processFilter.endDate = new Date(selectedRange[1]);
        this.loadall();
    }

    ondrillthru() {

        //console.log("inside drill func");
        for (let i = 0; i < this.modulelist.length; i++) {
            this.timer = Observable.timer(this.drillThruSeconds * i);
            //console.log("inside drill func + ");
            this.timer.takeWhile(() => this.alive).subscribe((t) => this.displaySelectedProcess(this.modulelist[i]));
        }
        this.timer = Observable.timer(this.drillThruSeconds * this.modulelist.length);
        this.timer.takeWhile(() => this.alive).subscribe((t) => this.displaySelectedProcess(this.modulelist[0]));
    }
    loadall() {
        this.blockedDocument = true;
        /* console.log('this.showReconPreviousButton ' + this.showReconPreviousButton);
        console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
        console.log('this.currentReconAnalysisData ' + this.currentReconAnalysisData); 
        */
        //       [datasets]="accountingViewSpecificChartData" [labels]="accountingViewSpecificChartLabels"
        this.reconRuleGrpId = null;
        this.accountingRg = null;
        this.accountingRuleGrpId = null;
        this.currentReconAnalysisData = null;
        this.reconPeriodAnalysisType = 'None';
        this.accountingPeriodAnalysisType = 'None';
        this.etlPeriodAnalysisType = 'None';
        this.processFilter.startDate = this.commonService.convertDate(this.processFilter.startDate);
        this.processFilter.endDate = this.commonService.convertDate(this.processFilter.endDate);
        this.showetl = false;
        this.showrecon = false;
        this.showacc = false;
        this.showGroupChart = false;
        /* $('.extractionStatusCls, .reconciliationStatusCls, .accountingStatusCls').removeClass('sideProgessActive');
        $('.reconciliationStatusCls').addClass('sideProgessActive'); */
        this.fetchSelectedProcessDetails(this.selectedProcessId);
        this.newrequest = false;
        this.newrequestviol = false;
        this.reconPeriodPreviousDateRange = [];
        this.reconciliationAnalysisData = [];
        this.reconDrillDownAnalysisData = [];
        this.reconWeekDrillDownAnalysisData = [];
        this.reconStatus = false;
        this.unReconStatus = false;
        this.selectedPeriodForReconMonth = undefined;
        this.selectedPeriodForReconWeek = undefined;
        this.splitReconCard = true;
        this.showReconPreviousButton = 'None';
        this.reconViewChartDisplay = true;
        this.reconViewSpecificChartLabels = [];
        this.showReconViewChart = false;

        this.showAccountingViewChart = false;
        this.accountingPeriodPreviousDateRange = [];
        this.accountingAnalysisData = [];
        this.accountingDrillDownAnalysisData = [];
        this.accountingWeekDrillDownAnalysisData = [];
        this.selectedPeriodForAccountingMonth = undefined;
        this.selectedPeriodForAccountingWeek = undefined;
        this.splitAccountingCard = true;
        this.showAccountingPreviousButton = 'none';
        this.notAccountingStatus = false;
        this.pendingJEStatus = false;
        this.jeCreationStatus = false;
        this.accountingChartLabels = [];
        this.accountingChartData = [];
        this.accountingViewChartDisplay = true;
        this.accountingViewSpecificChartLabels = [];
        this.accountingViewSpecificChartData = [];

        this.showETLViewChart = false;
        this.splitETLCard = false;
        this.etlPeriodPreviousDateRange = [];
        this.selectedPeriodForETLMonth = undefined;
        this.selectedPeriodForETLWeek = undefined;
        //this.showETLPreviousButton = false;
        this.etlViewChartDisplay = false;
        this.extractedStatus = false;
        this.extractionFailedStatus = false;
        this.transformationFailedStatus = false;
        this.transformedStatus = false;
        this.etlChartData = [];
        this.etlChartLabels = [];
        this.showETLChart = false;
        this.etlChartLabels = [];
        this.showETLPreviousButton = 'none';

        this.violationPeriod = this.processFilter.violationPeriod;
        //let stDate = new Date(this.processFilter.startDate);
        // stDate.setDate(stDate.getDate() + 1);
        this.stEndRange = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        };

        this.accStEndRange = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        };

        this.etlStEndRange = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        };

        //console.log("indside load all" + JSON.stringify(this.stEndRange));
        //console.log("etlStEndRange" + JSON.stringify(this.etlStEndRange));
        this.fetchAllModuleDetails();
    }
    /* Function 0: Function to open sidebar*/

    openSideBar(type) {
        this.visibleSidebar2 = true;
        this.openModuleSetting = type;
    }

    stEndRange: any;
    accStEndRange: any;
    etlStEndRange: any;
    noProcessList: boolean = false;
    /* Function 1: Function to get processess list*/
    fetchProcessessList() {
        this.request = this.navbarService.getprocessessList().takeWhile(() => this.alive).subscribe((res: any) => {
            this.processesList = res;
            if (this.processesList.length > 0) {
                // this.dateRangeactiveItem = { "name": "This Month" };
                $('.rangeMenuLov .mat-menu-item .addActCls_1').addClass('nav-active');
                this.noProcessList = false;
                this.processesList.forEach(element => {
                    if (element.id == this.dashBoardLayOutObj.processesId && this.dashBoardLayOutObj.processesId) {
                        this.selectedProcessId = element.id;
                        this.selectedProcessName = element.processName;
                        this.processFilter.processId = element.id;
                        this.tempProcessFilter.processId = element.id;
                    } else if (!this.dashBoardLayOutObj.processesId) {
                        this.selectedProcessId = this.processesList[0].id;
                        this.selectedProcessName = this.processesList[0].processName;
                        this.processFilter.processId = this.processesList[0].id;
                        this.tempProcessFilter.processId = this.processesList[0].id;
                    }
                });
                this.fetchSelectedProcessDetails(this.selectedProcessId);
                this.request = this.navbarService.getCalenderDetails(this.selectedProcessId).takeWhile(() => this.alive).subscribe((res: any) => {
                    this.calenderDetails = res;
                    let stDate;
                    if(this.calenderDetails.startDate && this.calenderDetails.currentdate){
                        this.processFilter.startDate = this.calenderDetails.startDate;
                    this.processFilter.startDate = new Date(this.processFilter.startDate);
                    this.processFilter.endDate = this.calenderDetails.currentdate;
                    this.processFilter.endDate = new Date(this.processFilter.endDate);
                    this.tempProcessFilter.startDate = this.calenderDetails.startDate;
                    this.tempProcessFilter.startDate = new Date(this.tempProcessFilter.startDate);
                    this.tempProcessFilter.endDate = this.calenderDetails.currentdate;
                    this.tempProcessFilter.endDate = new Date(this.tempProcessFilter.endDate);
                    stDate = new Date(this.processFilter.endDate);
                    stDate.setDate(stDate.getDate() - 6);
                    }else{
                        let selectedRange = [moment().startOf('month'), moment()];
                        this.processFilter.startDate = new Date(selectedRange[0]);
                        this.processFilter.endDate = new Date(selectedRange[1]);
                    }
                    /*  let obj = {
                         "startDate": this.processFilter.startDate,
                         "endDate": this.processFilter.endDate
                     }; */
                    this.stEndRange = {
                        "startDate": this.processFilter.startDate,
                        "endDate": this.processFilter.endDate
                    };
                    this.accStEndRange = {
                        "startDate": this.processFilter.startDate,
                        "endDate": this.processFilter.endDate
                    };
                    this.etlStEndRange = {
                        "startDate": this.processFilter.startDate,
                        "endDate": this.processFilter.endDate
                    };
                    if (new Date(stDate) < new Date(this.calenderDetails.startDate)) {

                        this.oneWeekObj = {
                            "startDate": new Date(this.calenderDetails.startDate),
                            "endDate": this.processFilter.endDate
                        };

                        this.accountingDateObj = {
                            "startDate": new Date(this.calenderDetails.startDate),
                            "endDate": this.processFilter.endDate
                        };

                        this.reconDateObj = {
                            "startDate": new Date(this.calenderDetails.startDate),
                            "endDate": this.processFilter.endDate
                        };
                    } else {

                        this.oneWeekObj = {
                            "startDate": stDate,
                            "endDate": this.processFilter.endDate
                        };

                        this.accountingDateObj = {
                            "startDate": stDate,
                            "endDate": this.processFilter.endDate
                        };

                        this.reconDateObj = {
                            "startDate": stDate,
                            "endDate": this.processFilter.endDate
                        };
                    }
                    this.activeItem = this.processesList[0];
                    this.selectedProcessName = this.processesList[0].processName;
                    this.reconPeriodAnalysisType = 'None';
                    this.reconPeriodPreviousDateRange[0] = this.stEndRange;

                    this.accountingPeriodAnalysisType = 'None';
                    this.accountingPeriodPreviousDateRange[0] = this.accountingDateObj;

                    this.etlPeriodAnalysisType = 'None';
                    this.etlPeriodPreviousDateRange[0] = this.etlStEndRange;

                    this.fetchAllModuleDetails();
                });
            } else {
                this.commonService.info('Info!', 'There is no process');
                this.noProcessList = true;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something Went Wrong');
                this.blockedDocument = false;

            });
    }

    reconViolationPeriod: any;
    tempreconViolationPeriod: any;
    accountingViolationPeriod: any;
    tempaccountingViolationPeriod: any;
    reconRuleGrpId: any;
    accountingRuleGrpId: any;
    /* Function 2: to fetchSelectedProcessDetails*/
    fetchSelectedProcessDetails(id) {
        this.spNames = [];
        this.processFilter.processId = id;
        this.request = this.processesService.getProcessDetail(id).takeWhile(() => this.alive).subscribe((res) => {
            this.processesDetailsList = res;
            if (this.processesDetailsList[0].processDetailList.recGrpList && this.processesDetailsList[0].processDetailList.recGrpList.length) {
                this.reconRuleGrpId = this.processesDetailsList[0].processDetailList.recGrpList[0].typeId;
                this.reconRg = this.processesDetailsList[0].processDetailList.recGrpList[0].reconcRuleGroupName;
                this.reconRgId = this.processesDetailsList[0].processDetailList.recGrpList[0].typeId;
            }
            if (this.processesDetailsList[0].processDetailList.actGrpList && this.processesDetailsList[0].processDetailList.actGrpList.length) {
                this.accountingRuleGrpId = this.processesDetailsList[0].processDetailList.actGrpList[0].typeId;
                this.accountingRg = this.processesDetailsList[0].processDetailList.actGrpList[0].actRuleGroupName;
            }
            if (this.dashBoardLayOutObj.reconViolationPeriod && this.dashBoardLayOutObj.accountingViolationPeriod) {
                this.reconViolationPeriod = this.dashBoardLayOutObj.reconViolationPeriod;
                this.tempreconViolationPeriod = this.dashBoardLayOutObj.reconViolationPeriod;
                this.accountingViolationPeriod = this.dashBoardLayOutObj.accountingViolationPeriod;
                this.tempaccountingViolationPeriod = this.dashBoardLayOutObj.accountingViolationPeriod;
            } else {
                // this.tempProcessFilter.violationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                //this.violationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                if (this.processesDetailsList[0].processDetailList.violationList && this.processesDetailsList[0].processDetailList.violationList.length) {
                    this.reconViolationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                    this.tempreconViolationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                    this.accountingViolationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                    this.tempaccountingViolationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                } else {
                    this.reconViolationPeriod = 2;
                    this.tempreconViolationPeriod = 2;
                    this.accountingViolationPeriod = 2;
                    this.tempaccountingViolationPeriod = 2;
                }

                //  this.processFilter.violationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
            }
            for (let i = 0; i < this.processesDetailsList[0].processDetailList.spList.length; i++) {
                this.spNames.push(this.processesDetailsList[0].processDetailList.spList[i].sourceProfileName);
            };
            //this.spName = this.processesDetailsList[0].processDetailList.spList[0].sourceProfileName;

            // this.accountingRg = this.processesDetailsList[0].processDetailList.actGrpList[0].actRuleGroupName;
        });
    }

    /* Function 3: */
    selectedProcessFun(col) {
        this.activeItem = col;
        this.processFilter.processId = col.id;
        this.tempProcessFilter.processId = col.id;
        this.selectedProcessName = col.processName;

    }

    /* Function 4: function to call all api's to get details */
    fetchAllModuleDetails() {
        this.blockedDocument = true;
        this.fetchMouleWiseAnalysis(this.selectedProcessId, this.stEndRange);
        this.fetchReconDetailsList();
        this.fetchAccountingDetailsList();
        this.fetchETLDetailsList();
    }

    /* Function 5 : function to get each module analysis*/
    fetchMouleWiseAnalysis(proId, obj) {
        this.request = this.navbarService.fetchModuleAnalysis(proId, obj).takeWhile(() => this.alive).subscribe((res: any) => {
            res[0]['etl'] = (res[0].extracted + res[0].transformation) / 2;
            res[0]['allValues'] = ((res[0].etl + res[0].reconciliation + res[0].accounting) / 3).toFixed(2);
            this.moduleAnalysisList = res[0];
            this.blockedDocument = false;
            console.log('this.reconRuleGrpId ' + this.reconRuleGrpId);
            console.log('this.accountingRuleGrpId ' + this.accountingRuleGrpId);
            $('.extractionStatusCls').addClass('sideProgessActive');
            $('.accountingStatusCls, .reconciliationStatusCls').removeClass('sideProgessActive');
            this.showetl = true;
            this.showrecon = false;
            this.showacc = false;
            /* if(this.reconRuleGrpId){
                $('.reconciliationStatusCls').addClass('sideProgessActive');
                $('.extractionStatusCls, .accountingStatusCls').removeClass('sideProgessActive');
                this.showetl = false;
                this.showrecon = true;
                this.showacc = false;
            }else if(this.accountingRuleGrpId){
                $('.accountingStatusCls').addClass('sideProgessActive');
                $('.extractionStatusCls, .reconciliationStatusCls').removeClass('sideProgessActive');
                this.showetl = false;
                this.showrecon = false;
                this.showacc = true;
            }else{
                $('.extractionStatusCls').addClass('sideProgessActive');
                $('.accountingStatusCls, .reconciliationStatusCls').removeClass('sideProgessActive');
                this.showetl = true;
                this.showrecon = false;
                this.showacc = false;
            } */
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching module wise analysis');
                this.blockedDocument = false;
            });
    }

    /* Function 6: */
    displaySelectedProcess(type) {
        //console.log('selectedType :' + type);
        if (type == 'extraction') {
            /* $("#extractionDiv").css("transform", "translateX(0%)");
            $("#reconciliationAct").css("transform", "translateX(105%)");
            $("#accountingAct").css("transform", "translateX(205%)"); */
            this.showetl = true;
            this.showrecon = false;
            this.showacc = false;
            $('.allStausCls,.reconciliationStatusCls,.accountingStatusCls').removeClass('sideProgessActive');
            $('.extractionStatusCls').addClass('sideProgessActive');
        } else if (type == 'reconciliation') {
            /* $("#extractionDiv").css("transform", "translateX(-105%)");
            $("#reconciliationAct").css("transform", "translateX(0%)");
            $("#accountingAct").css("transform", "translateX(105%)"); */
            this.showetl = false;
            this.showrecon = true;
            this.showacc = false;
            $('.extractionStatusCls, .allStausCls, .accountingStatusCls').removeClass('sideProgessActive');
            $('.reconciliationStatusCls').addClass('sideProgessActive');
        } else if (type == 'accounting') {
            /* $("#extractionDiv").css("transform", "translateX(-205%)");
            $("#reconciliationAct").css("transform", "translateX(-105%)");
            $("#accountingAct").css("transform", "translateX(0%)"); */
            this.showetl = false;
            this.showrecon = false;
            this.showacc = true;
            $('.extractionStatusCls, .allStausCls, .reconciliationStatusCls').removeClass('sideProgessActive');
            $('.accountingStatusCls').addClass('sideProgessActive');
        } else if (type == 'all') {
            this.showetl = true;
            this.showrecon = true;
            this.showacc = true;
            $('.extractionStatusCls, .accountingStatusCls, .reconciliationStatusCls').removeClass('sideProgessActive');
            $('.allStausCls').addClass('sideProgessActive');
        }
    }

    /* Function 7: */
    /* Function to get Recon Details For Given Period */
    fetchReconDetailsList() {
        this.showReconChart = false;
        this.blockedDocument = true;
        let tempReconData = [];
        let tempUnReconData = [];
        let tempReconLabels = [];
        let reconAnalysisData = [];
        let tempReconValue = [];
        let tempUnReconValue = [];
        this.request = this.dashboardService.fetchReconciliationForGivenPeriod(this.processFilter.processId, this.reconPeriodAnalysisType, this.stEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            reconAnalysisData = res.json();
            this.reconPeriodAnalysisType = res.headers.get('reconPeriodAnalysisType');
            reconAnalysisData.forEach(element => {
                tempReconLabels.push(element.labelValue);
                if (this.selectedReconFilterValue == 'Amount') {
                    tempReconData.push(element.recon.amountPer);
                    tempUnReconData.push(element.unRecon.amountPer);
                    tempReconValue.push(element.recon.amount);
                    tempUnReconValue.push(element.unRecon.amount);
                } else {
                    tempReconData.push(element.recon.countPer);
                    tempUnReconData.push(element.unRecon.countPer);
                    tempReconValue.push(element.recon.count);
                    tempUnReconValue.push(element.unRecon.count);
                }
            });

            this.reconChartData = [
                { data: tempUnReconData, label: 'Un-Reconciled', hidden: this.unReconStatus, values: tempUnReconValue },
                { data: tempReconData, label: 'Reconciled', hidden: this.reconStatus, values: tempReconValue }
            ];
            setTimeout(() => { this.reconChartLabels = tempReconLabels }, 0);
            setTimeout(() => {
                this.reconAnalysisOptionFun();
                this.changeReconChartColor();
                if (this.reconChartData[0].data.length > 0 && this.reconChartLabels && this.reconChartLabels.length > 0) {
                    this.showReconChart = true;
                    this.blockedDocument = false;
                    if (this.reconPeriodAnalysisType == 'Months') {
                        this.reconciliationAnalysisData = res.json();
                        this.reconPeriodPreviousDateRange[0] = this.stEndRange;
                        this.currentReconAnalysisData = 'Months';
                        this.showReconPreviousButton = 'None';
                        this.fetchReconViewAnalysis();
                    } else if (this.reconPeriodAnalysisType == 'Weeks') {
                        this.reconDrillDownAnalysisData = res.json();
                        this.reconPeriodPreviousDateRange[1] = this.stEndRange;
                        this.currentReconAnalysisData = 'Weeks';
                        if (this.reconciliationAnalysisData.length > 0) {
                            this.showReconPreviousButton = 'Months';
                        } else {
                            this.showReconPreviousButton = 'None';
                        }
                        this.fetchReconViewAnalysis();
                    } else {
                        this.reconWeekDrillDownAnalysisData = res.json();
                        this.currentReconAnalysisData = 'Days';
                        if (this.reconDrillDownAnalysisData.length > 0) {
                            this.showReconPreviousButton = 'Weeks';
                        } else {
                            this.showReconPreviousButton = 'None';
                        }
                        this.fetchReconViewAnalysis();
                    }
                }
            }, 0);
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching reconciliation analysis');
                this.blockedDocument = false;
            });
    }

    /* Function 8: function to fetch drill down analysis */
    reconStatus: boolean = false;
    unReconStatus: boolean = false;
    tempSelectedLabel: any = 'Un-Reconciled';
    getReconDrillDownAnalysis(e) {
        this.selectedReconAnalysis = 0;
        /* console.log('this.showReconPreviousButton ' + this.showReconPreviousButton);
        console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
        console.log('this.currentReconAnalysisData ' + this.currentReconAnalysisData); */
        if (e.active[0] != undefined) {
            this.displaySrcTrgChartUnRecon = false;
            this.displaySrcTrgChart = false;
            // this.reconViewChartDisplay = false;
            this.tempSelectedLabel = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
            let tempReconLab: boolean = false;
            let tempUnReconLab: boolean = false;
            if (this.tempSelectedLabel == 'Un-Reconciled') {
                tempReconLab = true;
                tempUnReconLab = false;
                this.reconStatus = true;
                this.unReconStatus = false;
            } else if (this.tempSelectedLabel == 'Reconciled') {
                tempReconLab = false;
                tempUnReconLab = true;
                this.reconStatus = false;
                this.unReconStatus = true;
            } else {
                tempReconLab = false;
                tempUnReconLab = false;
            }
            this.blockedDocument = true;
            this.showReconViewChart = true;
            console.log('this.reconciliationAnalysisData ' + JSON.stringify(this.reconciliationAnalysisData));
            console.log('e.active["0"]._index ' + e.active["0"]._index);
            if (this.currentReconAnalysisData == 'Months') {
                //console.log('this.reconciliationAnalysisData ' + JSON.stringify(this.reconciliationAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                let obj = {
                    "startDate": new Date(this.reconciliationAnalysisData[e.active["0"]._index].dateRange.startDate),
                    "endDate": new Date(this.reconciliationAnalysisData[e.active["0"]._index].dateRange.endDate)
                }
                this.stEndRange = obj;
                this.reconPeriodAnalysisType = 'Months';
                this.reconSelectedMonthIndex = e.active["0"]._index;
                this.selectedPeriodForReconMonth = this.reconciliationAnalysisData[this.reconSelectedMonthIndex].labelValue;
                this.selectedPeriodForReconWeek = undefined;
                let tempReconLabels = [];
                let tempReconData = [];
                let tempUnReconData = [];
                this.reconChartLabels = [];
                this.reconChartData = [];
                this.fetchReconDetailsList();
            } else if (this.currentReconAnalysisData == 'Weeks') {
                //console.log('this.reconciliationAnalysisData ' + JSON.stringify(this.reconciliationAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                let obj = {
                    "startDate": new Date(this.reconDrillDownAnalysisData[e.active["0"]._index].dateRange.startDate),
                    "endDate": new Date(this.reconDrillDownAnalysisData[e.active["0"]._index].dateRange.endDate)
                }
                this.stEndRange = obj;
                this.reconPeriodAnalysisType = 'Week';
                this.reconSelectedWeekIndex = e.active["0"]._index;
                this.selectedPeriodForReconWeek = this.reconDrillDownAnalysisData[this.reconSelectedWeekIndex].labelValue;
                let tempReconLabels = [];
                let tempReconData = [];
                let tempUnReconData = [];
                this.reconChartLabels = [];
                this.reconChartData = [];
                this.fetchReconDetailsList();
            } else {
                //console.log('this.reconciliationAnalysisData ' + JSON.stringify(this.reconciliationAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                this.fetchReconDetailsList();
            }
        }
    }

    dateRangeForView: any;
    reconViewSpecificAnalysis1: any;
    showReconViewBackButton: boolean = false;
    showGroupChart: boolean = false;
    reconViewCombinationSpecificAnalysis: any = [];
    // reconViewCombinationSpecificAnalysis: any = [{ "combination": [{ "viewId": 396, "viewName": "DV 1", "amountPer": 25, "countPer": 34 }, { "viewId": 397, "viewName": "DV 2", "amountPer": 39, "countPer": 45 }] }, { "combination": [{ "viewId": 246, "viewName": "DV 2", "amountPer": 87, "countPer": 78 }, { "viewId": 247, "viewName": "DV 3", "amountPer": 75, "countPer": 70 }] }];
    fetchReconViewAnalysis() {
        this.reconViewCombinationSpecificAnalysis = [];
        this.reconGroupByLov = undefined;
        this.reconSelectedViewName = undefined;
        this.showReconViewBackButton = false;
        this.reconViewSpecificAnalysis = [];
        this.dateRangeForView = this.stEndRange;
        this.request = this.dashboardService.fetchReconciliationViewSpecific(this.processFilter.processId, this.reconViolationPeriod, this.stEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            this.reconViewSpecificAnalysis = res;
            this.reconViewSpecificAnalysis1 = res;
            if (this.showReconViewChart == true) {
                this.reconViewChartDisplay = true;
                this.splitReconCard = true;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Error While fetching KPI for Reconciliation');
                this.blockedDocument = false;
            });
        /* Below api to get combination of souce and target */
        if(this.reconRgId){
            this.request = this.dashboardService.fetchReconciliationCombinationViewSpecific(this.reconRgId, this.tempSelectedLabel, this.stEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
                this.reconViewCombinationSpecificAnalysis = res;
                this.dataviewDatabyRule(this.reconViewCombinationSpecificAnalysis[0], 0);
                if (this.reconViewCombinationSpecificAnalysis && this.reconViewCombinationSpecificAnalysis.length) {
                    if (this.selectedReconAnalysis < 1) {
                        this.selectedReconAnalysis = 0;
                    }
                    console.log('this.selectedReconAnalysis ' + this.selectedReconAnalysis);
                    console.log('this.reconViewCombinationSpecificAnalysis ' + JSON.stringify(this.reconViewCombinationSpecificAnalysis));
                    this.getReconCombinationDrillDownAnalysis(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].viewId,
                        this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].viewName,
                        this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].ruleIdList,
                        this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].viewType);
                }
                if (this.showReconViewChart == true) {
                    this.reconViewChartDisplay = true;
                    this.splitReconCard = true;
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                    this.blockedDocument = false;
                });
        }
        
    }

    /* Function 9 */

    displayPreviousReconAnalysis() {
        this.blockedDocument = true;
        let tempReconLabels = [];
        let tempReconData = [];
        let tempUnReconData = [];
        this.reconChartLabels = [];
        this.reconChartData = [];
        this.reconStatus = false;
        this.unReconStatus = false;
        /* console.log('this.showReconPreviousButton ' + this.showReconPreviousButton);
        console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
        console.log('this.currentReconAnalysisData ' + this.currentReconAnalysisData);
        console.log('reconPeriodPreviousDateRange ' + JSON.stringify(this.reconPeriodPreviousDateRange)); */
        /* if (this.showReconPreviousButton == 'Months') {
            this.selectedPeriodForReconMonth = undefined;
            this.selectedPeriodForReconWeek = undefined;
            this.stEndRange = this.reconPeriodPreviousDateRange[0];
            this.reconPeriodAnalysisType = 'None';
            this.fetchReconDetailsList();
            this.currentReconAnalysisData = 'Months';
            this.showReconPreviousButton = 'Months';
        }  */if (this.currentReconAnalysisData == 'Months') {
            /* console.log('under months'); */
            this.selectedPeriodForReconMonth = undefined;
            this.selectedPeriodForReconWeek = undefined;
            this.stEndRange = this.reconPeriodPreviousDateRange[0];
            this.reconPeriodAnalysisType = 'None';
            this.fetchReconDetailsList();
        } else if (this.currentReconAnalysisData == 'Weeks') {
            /* console.log('under weeks'); */
            this.selectedPeriodForReconMonth = undefined;
            this.selectedPeriodForReconWeek = undefined;
            this.stEndRange = this.reconPeriodPreviousDateRange[0];
            //this.reconPeriodAnalysisType = 'Months';
            this.currentReconAnalysisData = 'Weeks';
            if (this.reconciliationAnalysisData.length > 0) {
                this.reconPeriodAnalysisType = 'None';
            } else {
                this.reconPeriodAnalysisType = 'none';
            }
            /* console.log('under weeks' + this.reconPeriodAnalysisType); */
            this.fetchReconDetailsList();
        } else if (this.currentReconAnalysisData == 'Days') {
            /* console.log('under Days'); */
            this.selectedPeriodForReconMonth = undefined;
            this.selectedPeriodForReconWeek = undefined;
            this.stEndRange = this.reconPeriodPreviousDateRange[1];
            //this.reconPeriodAnalysisType = 'Months';
            this.currentReconAnalysisData = 'Days';
            if (this.reconciliationAnalysisData.length > 0) {
                this.reconPeriodAnalysisType = 'Months';
            } else {
                this.reconPeriodAnalysisType = 'none';
            }
            /* console.log('under weeks' + this.reconPeriodAnalysisType); */
            this.fetchReconDetailsList();
        }
    }
    srcId: any;
    trgId: any;

    /* 
        dataviewlabels: any = ['Aplha Source DV', 'Aplha Target DV']
        dataviewdataset: any = [
            {
                label: 'One-One',
                data: [47.8, 12],
            },
            {
                label: 'One-Many',
                data: [20.7, 48],
            },
            {
                label: 'Many-Many',
                data: [11.4, 22],
            }
        ] 
    */

    displaySrcTrgChart: boolean = false;
    displaySrcTrgChartUnRecon: boolean = false;
    selectedCombViewIndex: any = 0;
    changeReconViewOrder: boolean = false;
    toggleReconView: boolean = true;
    changeReconViewTab(ind) {
        console.log(ind);
        this.selectedReconAnalysis = ind;
        let tempInd = this.selectedReconAnalysis;
        let tempIndValue = parseInt(tempInd) + 1;
        console.log('selectedReconAnalysis ' + this.selectedReconAnalysis);
        console.log('this.reconViewCombinationSpecificAnalysis ' + JSON.stringify(this.reconViewCombinationSpecificAnalysis));
        console.log('selectedReconAnalysis ' + tempIndValue);
        /* console.log('this.changeReconViewOrder ' + this.changeReconViewOrder);
         console.log('changed ::' + JSON.stringify(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis])); */
        if (!this.changeReconViewOrder) {
            this.toggleReconView = true;
            $('.srcReconOrder_' + this.selectedReconAnalysis).css('order', 0);
            $('.trgReconOrder_' + this.selectedReconAnalysis).css('order', 3);
            this.getReconCombinationDrillDownAnalysis(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].viewId,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].viewName,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].ruleIdList,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][0].viewType);
            this.dataviewDatabyRule(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis], this.selectedReconAnalysis);
        } else {
            this.toggleReconView = false;
            $('.srcReconOrder_' + this.selectedReconAnalysis).css('order', 3);
            $('.trgReconOrder_' + this.selectedReconAnalysis).css('order', 0);
            this.getReconCombinationDrillDownAnalysis(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][1].viewId,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][1].viewName,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][1].ruleIdList,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]["combination"][1].viewType);
            this.dataviewDatabyRule(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis], this.selectedReconAnalysis);
        }
    }
    /* Function 9 : to get dataview @author:Nishanth */
    showReconGrpChart: boolean = false;
    dataviewDatabyRule(obj, ind) {
        console.log('this.showReconGrpChart ' + this.showReconGrpChart);
        console.log('obj ' + JSON.stringify(obj));
        console.log('ind ' + ind);
        //  this.showReconGrpChart = false;
        console.log('this.tempSelectedLabel ' + this.tempSelectedLabel);
        this.selectedCombViewIndex = ind;
        this.displaySrcTrgChart = false;
        this.displaySrcTrgChartUnRecon = false;
        console.log('this.showReconGrpChart ' + this.showReconGrpChart);
        this.dataviewlabels = [];
        this.dataviewdata = [];
        this.dataviewdataset = [];
        if (this.tempSelectedLabel == 'Reconciled') {
            //console.log(JSON.stringify(obj));
            obj.combination.forEach(element => {
                if (element.viewType == "Source") {
                    this.srcId = element.viewId;
                } else {
                    this.trgId = element.viewId;
                }
            });
            this.request = this.dashboardService.dVcombinationbyrule(this.reconRgId, this.tempSelectedLabel, this.srcId, this.trgId, this.stEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
                this.dataviewdata = res;
                let obj;
                let tempLab = [];
                //console.log('this.dataviewdata ' + JSON.stringify(this.dataviewdata));
                tempLab.push(this.dataviewdata[0].srcDvName);
                tempLab.push(this.dataviewdata[0].tgtDvName);
                this.dataviewdata.forEach(element => {
                    if (this.selectedReconFilterValue == 'Amount') {
                        obj = {
                            label: element.ruleName,
                            data: [element.srcAmountPer, element.tgtAmountPer],
                            viewIds: [element.srcDvId, element.tgtDvId]
                        }
                    } else {
                        obj = {
                            label: element.ruleName,
                            data: [element.srcCountPer, element.tgtCountPer],
                            viewIds: [element.srcDvId, element.tgtDvId]
                        }
                    }
                    this.dataviewdataset.push(obj);
                });
                //console.log('this.dataviewdataset ' + JSON.stringify(this.dataviewdataset));
                //console.log('this.dataviewlabels ' + JSON.stringify(this.dataviewlabels));
                setTimeout(() => {
                    this.dataviewlabels = tempLab;
                }, 0);
                if (this.dataviewdataset && this.dataviewdataset[0].data.length > 0) {
                    this.displaySrcTrgChart = true;
                    this.displaySrcTrgChartUnRecon = false;
                }
            },
                (res: Response) => {
                    this.onError(res.json())
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });
        } else {

            //console.log('ind ' + ind);
            let tempData = [];
            let tempViewIds = [];
            let tempLab = [];
            console.log('ind ' + ind);
            console.log('this.reconViewCombinationSpecificAnalysis ' + JSON.stringify(this.reconViewCombinationSpecificAnalysis));
            this.reconViewCombinationSpecificAnalysis[ind].combination.forEach(element => {
                tempLab.push(element.viewName);
                tempViewIds.push(element.viewId);
                if (this.selectedReconFilterValue == 'Amount') {
                    tempData.push(element.amountPer);
                } else {
                    tempData.push(element.countPer);
                }
            });
            setTimeout(() => {
                this.dataviewlabels = tempLab;
            }, 0);
            this.dataviewdataset = [{ data: tempData, viewIds: tempViewIds }];
            setTimeout(() => {
                if (this.dataviewdataset && this.dataviewdataset[0].data.length > 0) {
                    //console.log('this.dataviewdataset unrecon ' + JSON.stringify(this.dataviewdataset));
                    //console.log('this.dataviewlabels unrecon ' + JSON.stringify(this.dataviewlabels));
                    /* this.displaySrcTrgChart = false;
                    this.displaySrcTrgChartUnRecon = true; */
                    this.displaySrcTrgChart = true;
                    this.showReconGrpChart = true;
                    console.log('this.showReconGrpChart ' + this.showReconGrpChart);
                }
            }, 0)
        }
    }

    reconSelectedMonthIndex: any;
    reconSelectedWeekIndex: any;
    reconGroupByLov: any;
    reconSelectedViewName: any;
    reconSelectedViewId: any;
    reconGrpByView: any;
    /* Function 10 : function to fetch recon view drill down analysis */
    // getReconViewDrillDownAnalysis(e) {
    //     if (e.active[0] != undefined) {
    //         this.reconSelectedViewName = this.reconViewSpecificAnalysis.reconciliationData[e.active[0]._index].labelValue;
    //         this.reconSelectedViewId = this.reconViewSpecificAnalysis.reconciliationData[e.active[0]._index].viewId;
    //         this.navbarService.getReconGroupByLov(this.reconViewSpecificAnalysis.reconciliationData[e.active[0]._index].viewId).takeWhile(() => this.alive).subscribe((res: any) => {
    //             this.reconGroupByLov = res;
    //             this.fetchViewDrillDownAnalysis(this.reconGroupByLov.columnsList[0].columnAliasName, this.reconGroupByLov.columnsList[0].columnName, rulelist, type);
    //             this.activeItem1 = this.reconGroupByLov.columnsList[0];
    //         });
    //     }
    // }
    getReconCombinationDrillDownAnalysis(id, name, rulelist, type) {
        this.reconSelectedViewName = name;
        this.reconSelectedViewId = id;
        this.request = this.navbarService.getReconGroupByLov(this.reconSelectedViewId).takeWhile(() => this.alive).subscribe((res: any) => {
            this.reconGroupByLov = res;
            this.tempRuleList = rulelist;
            this.tempType = type;
            this.fetchViewDrillDownAnalysis(this.reconGroupByLov.columnsList[0].columnAliasName, this.reconGroupByLov.columnsList[0].columnName, rulelist, type);
            this.activeItem1 = this.reconGroupByLov.columnsList[0];
        });
    }
    tempRuleList: any;
    tempType: any;
    getReconCombinationDrillDownAnalysis1(e) {
        if (e.active[0] != undefined) {
            //console.log(e);
            let rulelist;
            let type;
            this.reconSelectedViewName = e.active[0]._model.label;
            this.reconViewCombinationSpecificAnalysis[this.selectedCombViewIndex].combination.forEach(element => {
                if (element.viewName == this.reconSelectedViewName) {
                    this.reconSelectedViewId = element.viewId;
                    this.tempRuleList = element.ruleIdList;
                    this.tempType = element.viewType;
                    /* this.tempRuleList = element.ruleIdList;
                    this.tempType = element.viewType; */
                    //console.log('tempRuleList ' + this.tempRuleList);
                    //console.log('tempType ' + this.tempType);
                }
            })
            this.request = this.navbarService.getReconGroupByLov(this.reconSelectedViewId).takeWhile(() => this.alive).subscribe((res: any) => {
                this.reconGroupByLov = res;
                this.fetchViewDrillDownAnalysis(this.reconGroupByLov.columnsList[0].columnAliasName, this.reconGroupByLov.columnsList[0].columnName, this.tempRuleList, this.tempType);
                this.activeItem1 = this.reconGroupByLov.columnsList[0];
            });
        }
    }
    activeItem1: any;
    recongroupByCol = [];
    fetchViewDrillDownAnalysis(colAliasName, colName, rulelists?, viewType?) {
        //console.log('tempRuleList ' + this.tempRuleList);
        //console.log('tempType ' + this.tempType);
        /*  if(rulelists == undefined){
             rulelists = this.tempRuleList;
         }else if(viewType == undefined){
             viewType = this.tempType;
         } */
        let tempAmtQualifier;
        if (this.reconGroupByLov.amtQualifier.amtQualifierAliasName) {
            tempAmtQualifier = this.reconGroupByLov.amtQualifier.amtQualifierAliasName;
        } else {
            tempAmtQualifier = this.reconGroupByLov.amtQualifier.header;
        }
        let groupByCol = [];
        this.reconGrpByView = colName;
        groupByCol.push(colAliasName);
        this.request = this.dashboardService.fetchReconciliationViewSpecificgroupBy(this.processFilter.processId, this.reconSelectedViewId,
            this.tempSelectedLabel, tempAmtQualifier, groupByCol, this.dateRangeForView, this.tempRuleList, this.tempType).takeWhile(() => this.alive).subscribe((res: any) => {
                this.reconViewSpecificAnalysis = res[colAliasName];
                let tempViewLabel = [];
                let tempViewData = [];
                let tempValueData = [];
                if (this.reconViewSpecificAnalysis.length > 0) {
                    this.reconViewSpecificAnalysis.forEach(element1 => {
                        tempViewLabel.push(element1.name);
                        //console.log('this.selectedReconFilterValue ' + this.selectedReconFilterValue);
                        if (this.selectedReconFilterValue == 'Amount') {
                            tempViewData.push(element1.amountPer);
                            tempValueData.push(element1.amount);
                        } else {
                            tempViewData.push(element1.countPer);
                            tempValueData.push(element1.count);
                        }
                    });
                    this.reconViewSpecificChartData = [{ data: tempViewData, values: tempValueData }];
                    setTimeout(() => {
                        this.reconViewSpecificChartLabels = tempViewLabel;
                    }, 0);
                    setTimeout(() => {
                        if (this.reconViewSpecificChartData.length > 0 && this.reconViewSpecificChartLabels && this.reconViewSpecificChartLabels.length > 0) {/* && this.showReconViewChart == true */
                            // console.log('this.reconViewSpecificChartData ' + JSON.stringify(this.reconViewSpecificChartData));
                            this.displaySrcTrgChart = false;
                            this.displaySrcTrgChartUnRecon = false;
                            this.reconViewChartDisplay = true;
                            this.showReconViewBackButton = true;
                            this.showGroupChart = true;
                            this.splitReconCard = true;
                            this.blockedDocument = false;
                        }
                    }, 0);
                }
            });
    }

    changeReconGrpBy(val: any) {
        this.selectedReconFilterValue = val.value;
        this.reconPeriodAnalysisType = 'None';
        this.reconStatus = false;
        this.unReconStatus = false;
        this.fetchReconDetailsList();
    }

    /* Function to change recon chart options */
    reconAnalysisOptionFun() {

        this.reconChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            tooltips: {
                callbacks: {
                    
                    footer: function (tooltipItems, data) {
                        var input = data.datasets[tooltipItems[0].datasetIndex].values[tooltipItems[0].index];
                        var finalinput;
                        if (input >= 1000000000000) {
                            finalinput = parseFloat((input / 1000000000000).toFixed(2).replace(/\.0$/, '')) + 'T';
                        }
                        else if (input >= 1000000000) {
                            finalinput = parseFloat((input / 1000000000).toFixed(2).replace(/\.0$/, '')) + 'B';
                        }
                        else if (input >= 1000000) {
                            finalinput = parseFloat((input / 1000000).toFixed(2).replace(/\.0$/, '')) + 'M';
                        }
                        else if (input >= 100000) {
                            finalinput = parseFloat((input / 1000).toFixed(2).replace(/\.0$/, '')) + 'K';
                        } else {
                            finalinput = parseFloat(input.toFixed(2).replace(/\.0$/, '')).toLocaleString();
                        }
                        return [data.datasets[tooltipItems[0].datasetIndex].label + ' :' + finalinput, 'Percentage :' + data.datasets[tooltipItems[0].datasetIndex].data[tooltipItems[0].index] + ' % '];
                    }
                }
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        fontColor: this.setThemeColor
                    }
                }],
                yAxes: [
                    {
                        ticks: {
                            min: 0,
                            max: 100,
                            callback: function (value) { return value + "%" },
                            fontColor: this.setThemeColor
                        },
                        gridLines: {
                            display: false
                        }
                    }
                ]
            },
            legend: {
                position: 'bottom',
                labels: {
                    fontColor: this.setThemeColor
                }
            },
            /* title: {
                display: true,
                text: this.currentReconAnalysisData + ' Wise Analysis',
                position: 'top',
                fontSize: 20,
                //fontFamily: 'Lato',
                //fontWeight: 400,
                fontColor: this.setThemeColor
            }, */
            plugins: {
                datalabels: {
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    display: function (context) {
                        return (context.dataset.data[context.dataIndex] > 0 && !(context.dataset.hidden));/* && context.active */
                    },
                    align: function (context) {
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        var invert = Math.abs(value) <= 20;
                        return value < 20 ? 'top' : 'end'
                    },
                    borderWidth: 1,
                    borderRadius: 4,
                    formatter: function (value, context) {
                        return value + '%';
                    },
                    /* color: function (context) {
                        //console.log('context ' + context);
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        return value < 0 ? 'green' :  // draw negative values in red
                            context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green

                                'rgba(52, 168, 83, 0.9)';

                    } */
                }
            }
        };
    }

    reconViewAnalysisOptionFunction() {
        this.reconViewSpecificChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                position: 'bottom',
                maxHeight: 10,
                display: true,
                labels: {
                    fontColor: this.setThemeColor
                }
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10
                }
            },
            /* title: {
                display: true,
                text: 'Data Source Analysis',
                position: 'top',
                fontSize: 20,
                fontFamily: 'Lato',
                fontWeight: 400,
                fontColor: this.setThemeColor
            }, */
            plugins: {
                datalabels: {
                    display: true,
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    formatter: function (value, context) {
                        return value + '%';
                    }
                }
            },
            tooltips: {
                callbacks: {
                    label: function (tooltipItems, data) {
                        var input = data.datasets[tooltipItems.datasetIndex].values[tooltipItems.index];
                        var finalinput;
                        if (input >= 1000000000000) {
                            finalinput = parseFloat((input / 1000000000000).toFixed(2).replace(/\.0$/, '')) + 'T';
                        }
                        else if (input >= 1000000000) {
                            finalinput = parseFloat((input / 1000000000).toFixed(2).replace(/\.0$/, '')) + 'B';
                        }
                        else if (input >= 1000000) {
                            finalinput = parseFloat((input / 1000000).toFixed(2).replace(/\.0$/, '')) + 'M';
                        }
                        else if (input >= 100000) {
                            finalinput = parseFloat((input / 1000).toFixed(2).replace(/\.0$/, '')) + 'K';
                        } else {
                            finalinput = parseFloat((input).toFixed(2).replace(/\.0$/, '')).toLocaleString();

                        }
                        return data.labels[tooltipItems.index] + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + ' % ( ' + finalinput + ' )';
                    }
                }
            }
        };
    }

    /* Function to change recon chart color based on chart type */

    changeReconChartColor() {
        if (this.reconChartType == 'bar') {
            this.reconChartColors = [
                { backgroundColor: '#DC3912', },
                { backgroundColor: '#3366CC', }
            ];
        } else if (this.reconChartType == 'line') {
            this.reconChartColors = [
                { backgroundColor: 'transparent', borderColor: '#DC3912' },
                { backgroundColor: 'transparent', borderColor: '#3366CC' }
            ];
        }
    }


    changeAccountingGrpBy(val: any) {
        this.accountingPeriodAnalysisType = 'None';
        this.selectedAccountingFilterValue = val.value;
        this.notAccountingStatus = false;
        this.pendingJEStatus = false;
        this.jeCreationStatus = false;
        this.fetchAccountingDetailsList();
    }

    /* Accounting Mode Coding */

    /* Function 7: */
    /* Function to get Accounting Details For Given Period */
    fetchAccountingDetailsList() {
        //console.log('this.accountingPeriodPreviousDateRange ' + JSON.stringify(this.accountingPeriodPreviousDateRange));
        this.showAccountingChart = false;
        this.blockedDocument = true;
        let tempNotAccountedData = [];
        let tempPendingJEData = [];
        let tempJECreationData = [];
        let tempNotAccountedValues = [];
        let tempPendingJEValues = [];
        let tempJECreationValues = [];
        let tempAccountingLabels = [];
        let accAnalysisData = [];
        // this.showAccountingPreviousButton = 'none';
        //this.reconciliationAnalysisData = [];
        this.request = this.dashboardService.fetchAccountingForGivenPeriod(this.processFilter.processId, this.accountingPeriodAnalysisType, this.accStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            //this.reconciliationAnalysisData = res;
            //console.log(res.headers.get('accountingPeriodAnalysisType'));
            accAnalysisData = res.json();
            this.accountingPeriodAnalysisType = res.headers.get('accountingPeriodAnalysisType');
            //console.log('this.accountingPeriodAnalysisType ' + this.accountingPeriodAnalysisType);
            /* if (this.selectedAccountingFilterValue == 'Amount') { */
            accAnalysisData.forEach(element => {
                tempAccountingLabels.push(element.labelValue);
                if (this.selectedAccountingFilterValue == 'Amount') {
                    if (element.notAccounted != undefined) {
                        tempNotAccountedData.push(element.notAccounted.amountPer);
                        tempNotAccountedValues.push(element.notAccounted.amount);
                    }
                    if (element.accounted != undefined) {
                        tempPendingJEData.push(element.accounted.amountPer);
                        tempPendingJEValues.push(element.accounted.amount);
                    }
                    if (element.finalAccounted != undefined) {
                        tempJECreationData.push(element.finalAccounted.amountPer);
                        tempJECreationValues.push(element.finalAccounted.amount);
                    }
                } else {
                    if (element.notAccounted != undefined) {
                        tempNotAccountedData.push(element.notAccounted.countPer);
                        tempNotAccountedValues.push(element.notAccounted.count);
                    }
                    if (element.accounted != undefined) {
                        tempPendingJEData.push(element.accounted.countPer);
                        tempPendingJEValues.push(element.accounted.count);
                    }
                    if (element.finalAccounted != undefined) {
                        tempJECreationData.push(element.finalAccounted.countPer);
                        tempJECreationValues.push(element.finalAccounted.count);
                    }
                }
            });

            this.accountingChartData = [
                { data: tempNotAccountedData, label: 'Not Accounted', values: tempNotAccountedValues, hidden: this.notAccountingStatus, valueType: this.selectedAccountingFilterValue },
                { data: tempPendingJEData, label: 'Pending Journals', values: tempPendingJEValues, hidden: this.pendingJEStatus, valueType: this.selectedAccountingFilterValue },
                { data: tempJECreationData, label: 'JE Creation', values: tempJECreationValues, hidden: this.jeCreationStatus, valueType: this.selectedAccountingFilterValue }
            ];
            setTimeout(() => { this.accountingChartLabels = tempAccountingLabels }, 0);
            setTimeout(() => {
                this.accountingAnalysisOptionFun();
                this.changeAccountingChartColor();
                if (this.accountingChartData[0].data.length > 0 && this.accountingChartLabels && this.accountingChartLabels.length > 0) {
                    this.showAccountingChart = true;
                    this.blockedDocument = false;
                    if (this.accountingPeriodAnalysisType == 'Months') {
                        this.accountingAnalysisData = res.json();
                        //console.log('this.accountingAnalysisData ' + JSON.stringify(this.accountingAnalysisData));
                        this.accountingPeriodPreviousDateRange[0] = this.accStEndRange;
                        this.currentAccountingAnalysisData = 'Months';
                        this.showAccountingPreviousButton = 'None';
                        this.fetchAccountingViewAnalysis();
                    } else if (this.accountingPeriodAnalysisType == 'Weeks') {
                        this.accountingDrillDownAnalysisData = res.json();
                        //console.log('this.accountingDrillDownAnalysisData ' + JSON.stringify(this.accountingDrillDownAnalysisData));
                        /* this.selectedPeriodForReconMonth = this.accountingAnalysisData[this.reconSelectedMonthIndex].labelValue;
                        this.selectedPeriodForReconWeek = undefined; */
                        this.accountingPeriodPreviousDateRange[1] = this.accStEndRange;
                        this.currentAccountingAnalysisData = 'Weeks';
                        if (this.accountingAnalysisData.length > 0) {
                            this.showAccountingPreviousButton = 'Months';
                        } else {
                            this.showAccountingPreviousButton = 'None';
                        }
                        this.fetchAccountingViewAnalysis();
                    } else {
                        this.accountingWeekDrillDownAnalysisData = res.json();
                        //console.log('this.accountingWeekDrillDownAnalysisData ' + JSON.stringify(this.accountingWeekDrillDownAnalysisData));
                        //this.selectedPeriodForReconMonth = this.accountingAnalysisData[this.reconSelectedMonthIndex].labelValue;
                        /* this.selectedPeriodForReconWeek = this.accountingAnalysisData[this.reconSelectedWeekIndex].labelValue;;
                        this.accountingPeriodPreviousDateRange[2] = this.stEndRange; */
                        this.currentAccountingAnalysisData = 'Days';
                        if (this.accountingDrillDownAnalysisData.length > 0) {
                            this.showAccountingPreviousButton = 'Weeks';
                        } else {
                            this.showAccountingPreviousButton = 'None';
                        }
                        this.fetchAccountingViewAnalysis();
                    }
                    //console.log('this.showAccountingPreviousButton ' + JSON.stringify(this.showAccountingPreviousButton));
                }
            }, 0);
            /* } else {

            } */
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching accounting analysis');
                this.blockedDocument = false;
            });
    }
    jeStatusName: any;
    /* Function 8: function to fetch drill down analysis */
    getAccountingDrillDownAnalysis(e) {
        /* console.log('this.showAccountingPreviousButton ' + this.showAccountingPreviousButton);
        console.log('this.accountingPeriodAnalysisType ' + this.accountingPeriodAnalysisType);
        console.log('this.currentAccountingAnalysisData ' + this.currentAccountingAnalysisData); */
        if (e.active[0] != undefined) {
            this.acctempSelectedLabel = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
            /* let tempReconLab: boolean = false;
            let tempUnReconLab: boolean = false; */
            if (this.acctempSelectedLabel == 'Not Accounted') {
                /* tempReconLab = true;
                tempUnReconLab = false; */
                /* this.reconStatus = true;
                this.unReconStatus = false; */
                this.notAccountingStatus = false;
                this.pendingJEStatus = true;
                this.jeCreationStatus = true;
            } else if (this.acctempSelectedLabel == 'Pending Journals') {
                this.jeStatusName = 'accounted';
                /* tempReconLab = false;
                tempUnReconLab = true; */
                this.notAccountingStatus = true;
                this.pendingJEStatus = false;
                this.jeCreationStatus = true;
            } else {
                this.jeStatusName = 'JE creation';
                this.notAccountingStatus = true;
                this.pendingJEStatus = true;
                this.jeCreationStatus = false;
                /* tempReconLab = false;
                tempUnReconLab = false; */
            }
            this.blockedDocument = true;
            this.showAccountingViewChart = true;
            //console.log('this.currentAccountingAnalysisData ' + this.currentAccountingAnalysisData);
            if (this.currentAccountingAnalysisData == 'Months') {
                //console.log('this.accountingAnalysisData ' + JSON.stringify(this.accountingAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                let obj = {
                    "startDate": new Date(this.accountingAnalysisData[e.active["0"]._index].dateRange.startDate),
                    "endDate": new Date(this.accountingAnalysisData[e.active["0"]._index].dateRange.endDate)
                }
                this.accStEndRange = obj;
                this.accountingPeriodAnalysisType = 'Months';
                this.accountingSelectedMonthIndex = e.active["0"]._index;
                this.selectedPeriodForAccountingMonth = this.accountingAnalysisData[this.accountingSelectedMonthIndex].labelValue;
                this.selectedPeriodForAccountingWeek = undefined;
                let tempNotAccountedData = [];
                let tempPendingJEData = [];
                let tempJECreationData = [];
                let tempAccountingLabels = [];
                this.accountingChartLabels = [];
                this.accountingChartData = [];
                this.fetchAccountingDetailsList();
            } else if (this.currentAccountingAnalysisData == 'Weeks') {
                //console.log('this.accountingDrillDownAnalysisData ' + JSON.stringify(this.accountingDrillDownAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                let obj = {
                    "startDate": new Date(this.accountingDrillDownAnalysisData[e.active["0"]._index].dateRange.startDate),
                    "endDate": new Date(this.accountingDrillDownAnalysisData[e.active["0"]._index].dateRange.endDate)
                }
                //console.log('obj ' + JSON.stringify(obj));
                this.accStEndRange = obj;
                this.accountingPeriodAnalysisType = 'Week';
                this.accountingSelectedWeekIndex = e.active["0"]._index;
                //console.log('this.accountingSelectedWeekIndex ' + JSON.stringify(this.accountingSelectedWeekIndex));
                this.selectedPeriodForAccountingWeek = this.accountingDrillDownAnalysisData[this.accountingSelectedWeekIndex].labelValue;
                let tempNotAccountedData = [];
                let tempPendingJEData = [];
                let tempJECreationData = [];
                let tempAccountingLabels = [];
                this.accountingChartLabels = [];
                this.accountingChartData = [];
                this.fetchAccountingDetailsList();
            } else {
                //console.log('this.accountingAnalysisData ' + JSON.stringify(this.accountingAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                this.fetchAccountingDetailsList();
            }
        }
    }

    /* Function 9 */

    displayPreviousAccountingAnalysis() {
        this.blockedDocument = true;
        let tempAccountingLabels = [];
        let tempNotAccountedData = [];
        let tempPendingJEData = [];
        let tempJECreationData = [];
        this.accountingChartLabels = [];
        this.accountingChartData = [];
        this.notAccountingStatus = false;
        this.pendingJEStatus = false;
        this.jeCreationStatus = false;
        //console.log('this.showAccountingPreviousButton ' + this.showAccountingPreviousButton);
        if (this.currentAccountingAnalysisData == 'Months') {
            this.selectedPeriodForAccountingMonth = undefined;
            this.selectedPeriodForAccountingWeek = undefined;
            this.accStEndRange = this.accountingPeriodPreviousDateRange[0];
            this.accountingPeriodAnalysisType = 'None';
            this.fetchAccountingDetailsList();
            /* this.currentAccountingAnalysisData = 'Months';
            this.showAccountingPreviousButton = 'none'; */
        } else if (this.currentAccountingAnalysisData == 'Weeks') {
            this.selectedPeriodForAccountingMonth = undefined;
            this.selectedPeriodForAccountingWeek = undefined;
            this.accStEndRange = this.accountingPeriodPreviousDateRange[0];
            //this.accountingPeriodAnalysisType = 'Weeks';
            this.currentAccountingAnalysisData = 'Weeks';
            if (this.accountingAnalysisData.length > 0) {
                this.accountingPeriodAnalysisType = 'None';
            } else {
                this.accountingPeriodAnalysisType = 'None';
            }
            this.fetchAccountingDetailsList();
        } else if (this.currentAccountingAnalysisData == 'Days') {
            /* console.log('under Days'); */
            this.selectedPeriodForAccountingMonth = undefined;
            this.selectedPeriodForAccountingWeek = undefined;
            this.accStEndRange = this.accountingPeriodPreviousDateRange[1];
            //this.reconPeriodAnalysisType = 'Months';
            this.currentAccountingAnalysisData = 'Days';
            if (this.accountingAnalysisData.length > 0) {
                this.accountingPeriodAnalysisType = 'Months';
            } else {
                this.accountingPeriodAnalysisType = 'None';
            }
            /* console.log('under weeks' + this.accountingPeriodAnalysisType); */
            this.fetchAccountingDetailsList();
        }
    }

    /* Function 10 : function to fetch Accounting view drill down analysis */
    /* getAccountingViewDrillDownAnalysis(e) {
        if (e.active[0] != undefined) {
            //console.log('selectedLabel ' + e.active["0"]._index);
            let tempViewLabel = [];
            let tempViewData = [];
            this.accountingViewSpecificDrillDownAnalysis.forEach(element1 => {
                tempViewLabel.push(element1.labelValue);
                tempViewData.push(element1.unAccounting.amount);
            });

            setTimeout(() => { this.accountingViewSpecificChartLabels = tempViewLabel }, 0);
            setTimeout(() => { this.accountingViewSpecificChartData = tempViewData }, 0);
            setTimeout(() => {
                if (this.accountingViewSpecificChartData.length > 0 && this.accountingViewSpecificChartLabels && this.accountingViewSpecificChartLabels.length > 0) {
                    this.blockedDocument = false;
                }
            }, 0)
        }
    } */

    getAccountingViewDrillDownAnalysis(e) {
        console.log('selectedAccountingAnalysis ' + this.selectedAccountingAnalysis, e);
        if (e.active != undefined && e.active[0] != undefined && this.accountingViewSpecificAnalysis != undefined) {
            this.selectedAccountingAnalysis = e.active[0]._index;
            console.log('e.active[0]._index ' + e.active[0]._index);
            this.accountingSelectedViewName = this.accountingViewSpecificAnalysis[e.active[0]._index].viewName;
            this.accountingSelectedViewId = this.accountingViewSpecificAnalysis[e.active[0]._index].viewId;
            this.request = this.navbarService.getReconGroupByLov(this.accountingViewSpecificAnalysis[e.active[0]._index].viewId).takeWhile(() => this.alive).subscribe((res: any) => {
                this.accountingGroupByLov = res;
                this.fetchAccountingViewDrillDownAnalysis(this.accountingGroupByLov.columnsList[0].columnAliasName, this.accountingGroupByLov.columnsList[0].columnName);
                this.activeItem2 = this.accountingGroupByLov.columnsList[0];
            });
        } else {
            this.selectedAccountingAnalysis = e;
            this.accountingSelectedViewName = this.accountingViewSpecificAnalysis[this.selectedAccountingAnalysis].viewName;
            this.accountingSelectedViewId = this.accountingViewSpecificAnalysis[this.selectedAccountingAnalysis].viewId;
            this.request = this.navbarService.getReconGroupByLov(this.accountingViewSpecificAnalysis[this.selectedAccountingAnalysis].viewId).takeWhile(() => this.alive).subscribe((res: any) => {
                this.accountingGroupByLov = res;
                this.fetchAccountingViewDrillDownAnalysis(this.accountingGroupByLov.columnsList[0].columnAliasName, this.accountingGroupByLov.columnsList[0].columnName);
                this.activeItem2 = this.accountingGroupByLov.columnsList[0];
            });
        }
    }

    getAccountingViewDrillDownAnalysisDetails() {
        console.log('this.accountingViewSpecificAnalysis ' + JSON.stringify(this.accountingViewSpecificAnalysis));
        this.accountingSelectedViewName = this.accountingViewSpecificAnalysis[0].viewName;
        this.accountingSelectedViewId = this.accountingViewSpecificAnalysis[0].viewId;
        this.request = this.navbarService.getReconGroupByLov(this.accountingViewSpecificAnalysis[0].viewId).takeWhile(() => this.alive).subscribe((res: any) => {
            this.accountingGroupByLov = res;
            this.fetchAccountingViewDrillDownAnalysis(this.accountingGroupByLov.columnsList[0].columnAliasName, this.accountingGroupByLov.columnsList[0].columnName);
            this.activeItem2 = this.accountingGroupByLov.columnsList[0];
        });
    }

    accountingdateRangeForView: any;
    showAccountingViewBackButton: boolean = false;
    etlKPIDetailList: any;
    accountingViewSpecificAnalysis1: any;
    jeDetailsList: any;
    showJEDetails: boolean = false;
    selectedAccountingAnalysis: any = 0;
    fetchAccountingViewAnalysis() {
        /* console.log('under view '); */
        console.log('this.acctempSelectedLabel ' + this.acctempSelectedLabel);
        this.accountingGroupByLov = undefined;
        this.accountingSelectedViewName = undefined;
        this.showAccountingViewBackButton = false;
        this.accountingViewSpecificAnalysis = [];
        this.accountingdateRangeForView = this.accStEndRange;
        this.request = this.dashboardService.fetchAccountingViewSpecific(this.processFilter.processId, this.accountingViolationPeriod, this.accStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            // this.accountingViewSpecificAnalysis = res;
            this.accountingViewSpecificAnalysis1 = res;
            /*  this.accountingViewSpecificChartData = [];
             this.accountingViewSpecificChartLabels = [];
             let tempViewLabel = [];
             let tempViewData = [];
             let tempValueData = [];
             if (this.accountingViewSpecificAnalysis.accountingData != undefined && this.accountingViewSpecificAnalysis.accountingData.length > 0) {
                 this.accountingViewSpecificAnalysis.accountingData.forEach(element1 => {
                     tempViewLabel.push(element1.labelValue);
                     if (this.acctempSelectedLabel == 'Not Accounted' && element1.notAccounted != undefined) {
                         if (this.selectedAccountingFilterValue == 'Amount') {
                             tempViewData.push(element1.notAccounted.amountPer);
                             tempValueData.push(element1.notAccounted.amount);
                         } else {
                             tempViewData.push(element1.notAccounted.countPer);
                             tempValueData.push(element1.notAccounted.count);
                         }
                     } else if (this.acctempSelectedLabel == 'Pending Journals' && element1.accounted != undefined) {
                         if (this.selectedAccountingFilterValue == 'Amount') {
                             tempViewData.push(element1.accounted.amountPer);
                             tempValueData.push(element1.accounted.amount);
                         } else {
                             tempViewData.push(element1.accounted.countPer);
                             tempValueData.push(element1.accounted.count);
                         }
                     } else if (this.acctempSelectedLabel == 'JE Creation' && element1.finalaccounted != undefined) {
                         if (this.selectedAccountingFilterValue == 'Amount') {
                             tempViewData.push(element1.finalaccounted.amountPer);
                             tempValueData.push(element1.finalaccounted.amount);
                         } else {
                             tempViewData.push(element1.finalaccounted.countPer);
                             tempValueData.push(element1.finalaccounted.count);
                         }
                     }
                 });
                 this.accountingViewSpecificChartData = [{ data: tempViewData, values: tempValueData, valueType: this.selectedAccountingFilterValue }];
                 setTimeout(() => {
                     this.accountingViewSpecificChartLabels = tempViewLabel;
                 }, 0);
                 setTimeout(() => {
                     this.accountingViewAnalysisOptionFunction();
                     if (this.accountingViewSpecificChartData.length > 0 && this.accountingViewSpecificChartLabels && this.accountingViewSpecificChartLabels.length > 0 && this.showAccountingViewChart == true) {
                         if (this.acctempSelectedLabel == 'Not Accounted') {
                             this.showJEDetails = false;
                         }
                         this.accountingViewChartDisplay = true;
                         this.splitAccountingCard = true;
                         this.blockedDocument = false;
                     }
                 }, 0);
             } */
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching accounting analysis');
                this.blockedDocument = false;
            });
        this.request = this.dashboardService.fetchAccountingViewSpecificDetails(this.processFilter.processId, this.acctempSelectedLabel, this.accStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            /* this.accountingViewSpecificAnalysis = res;
            this.accountingViewSpecificAnalysis1 = res; */
            this.accountingViewSpecificAnalysis = res;
            if (this.acctempSelectedLabel != 'Not Accounted') {
                this.request = this.dashboardService.getdetailInfoForJE(this.processFilter.processId, this.accStEndRange, this.jeStatusName, this.accountingViewSpecificAnalysis[0].viewId).takeWhile(() => this.alive).subscribe((res: any) => {
                    this.jeDetailsList = res;
                    this.jeDetailsList.forEach(element => {
                        element.balance = Math.abs(element.balance);
                    });
                    if (this.jeDetailsList.length > 0) {
                        this.showJEDetails = true;
                    }
                },
                    (res: Response) => {
                        this.onError(res.json()
                        )
                        this.commonService.error('Error!', 'Something went wrong while fetching JE analysis');
                        this.blockedDocument = false;
                    });
            }
            console.log('this.accountingViewSpecificAnalysis ' + JSON.stringify(this.accountingViewSpecificAnalysis));
            this.accountingViewSpecificChartData = [];
            this.accountingViewSpecificChartLabels = [];
            let tempViewLabel = [];
            let tempViewData = [];
            let tempValueData = [];
            if (res != undefined && res.length > 0) {
                //console.log('res ' + JSON.stringify(res));
                /* console.log('this.acctempSelectedLabel ' + JSON.stringify(this.acctempSelectedLabel)); */
                res.forEach(element1 => {
                    tempViewLabel.push(element1.viewName);
                    if (this.selectedAccountingFilterValue == 'Amount') {
                        tempViewData.push(element1.amountPer);
                        tempValueData.push(element1.dvAmt);
                    } else {
                        tempViewData.push(element1.countPer);
                        tempValueData.push(element1.dvCount);
                    }
                    /* if (this.acctempSelectedLabel == 'Not Accounted' && element1.notAccounted != undefined) {
                        if (this.selectedAccountingFilterValue == 'Amount') {
                            tempViewData.push(element1.notAccounted.amountPer);
                            tempValueData.push(element1.notAccounted.amount);
                        } else {
                            tempViewData.push(element1.notAccounted.countPer);
                            tempValueData.push(element1.notAccounted.count);
                        }
                    } else if (this.acctempSelectedLabel == 'Pending Journals' && element1.accounted != undefined) {
                        if (this.selectedAccountingFilterValue == 'Amount') {
                            tempViewData.push(element1.accounted.amountPer);
                            tempValueData.push(element1.accounted.amount);
                        } else {
                            tempViewData.push(element1.accounted.countPer);
                            tempValueData.push(element1.accounted.count);
                        }
                    } else if (this.acctempSelectedLabel == 'JE Creation' && element1.finalaccounted != undefined) {
                        if (this.selectedAccountingFilterValue == 'Amount') {
                            tempViewData.push(element1.finalaccounted.amountPer);
                            tempValueData.push(element1.finalaccounted.amount);
                        } else {
                            tempViewData.push(element1.finalaccounted.countPer);
                            tempValueData.push(element1.finalaccounted.count);
                        }
                    } */
                });
                this.accountingViewSpecificChartData = [{ data: tempViewData, values: tempValueData, valueType: this.selectedAccountingFilterValue }];
                console.log('this.accountingViewSpecificChartData ' + JSON.stringify(this.accountingViewSpecificChartData));
                setTimeout(() => {
                    this.accountingViewSpecificChartLabels = tempViewLabel;
                }, 0);
                setTimeout(() => {
                    this.accountingViewAnalysisOptionFunction();
                    if (this.accountingViewSpecificChartData.length > 0 && this.accountingViewSpecificChartLabels && this.accountingViewSpecificChartLabels.length > 0) {/*  && this.showAccountingViewChart == true */
                        this.getAccountingViewDrillDownAnalysisDetails();
                        console.log('this.accountingViewSpecificChartData ' + JSON.stringify(this.accountingViewSpecificChartData));
                        if (this.acctempSelectedLabel == 'Not Accounted') {
                            this.showJEDetails = false;
                        }
                        this.accountingViewChartDisplay = true;
                        this.splitAccountingCard = true;
                        this.blockedDocument = false;
                    }
                }, 0);
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching accounting analysis');
                this.blockedDocument = false;
            });
    }

    showFullAmount: boolean = false;
    activeItem2: any;
    accountinggroupByCol = [];
    fetchAccountingViewDrillDownAnalysis(colAliasName, colName) {
        console.log('this.accountingGroupByLov ' + JSON.stringify(this.accountingGroupByLov));
        if (this.acctempSelectedLabel != 'Not Accounted') {
            this.jeDetailsList = [];
            this.request = this.dashboardService.getdetailInfoForJE(this.processFilter.processId, this.accStEndRange, this.jeStatusName, this.accountingSelectedViewId).takeWhile(() => this.alive).subscribe((res: any) => {
                this.jeDetailsList = res;
                this.jeDetailsList.forEach(element => {
                    element.balance = Math.abs(element.balance);
                });
                if (this.jeDetailsList.length > 0) {
                    this.showJEDetails = true;
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Something went wrong while fetching JE analysis');
                    this.blockedDocument = false;
                });
        } else {
            let tempAmtQualifier;
            if (this.accountingGroupByLov.amtQualifier.amtQualifierAliasName) {
                tempAmtQualifier = this.accountingGroupByLov.amtQualifier.amtQualifierAliasName;
            } else {
                tempAmtQualifier = this.accountingGroupByLov.amtQualifier.header;
            }
            this.accountingViewSpecificChartDataDetailed = [];
            this.accountingViewSpecificChartLabelsDetailed = [];
            let groupByCol = [];
            let g;
            let h;
            this.accountingGrpByView = colName;
            groupByCol.push(colAliasName);
            this.request = this.dashboardService.fetchReconciliationViewSpecificgroupBy(this.processFilter.processId, this.accountingSelectedViewId,
                this.acctempSelectedLabel, tempAmtQualifier, groupByCol, this.accountingdateRangeForView, g, h).takeWhile(() => this.alive).subscribe((res: any) => {
                    //this.accountingViewSpecificAnalysis = res[colAliasName];
                    let tempViewLabel = [];
                    let tempViewData = [];
                    let tempValueData = [];
                    if (res[colAliasName].length > 0) {
                        res[colAliasName].forEach(element1 => {
                            tempViewLabel.push(element1.name);
                            if (this.selectedAccountingFilterValue == 'Amount') {
                                tempViewData.push(element1.amountPer);
                                tempValueData.push(element1.amount);
                            } else {
                                tempViewData.push(element1.countPer);
                                tempValueData.push(element1.count);
                            }
                        });
                        this.accountingViewSpecificChartDataDetailed = [{ data: tempViewData, values: tempValueData, valueType: this.selectedAccountingFilterValue }];
                        console.log('this.accountingViewSpecificChartDataDetailed ' + JSON.stringify(this.accountingViewSpecificChartDataDetailed));
                        setTimeout(() => {
                            this.accountingViewSpecificChartLabelsDetailed = tempViewLabel;
                            console.log('this.accountingViewSpecificChartLabelsDetailed ' + JSON.stringify(this.accountingViewSpecificChartLabelsDetailed));
                        }, 0);
                        setTimeout(() => {
                            if (this.accountingViewSpecificChartDataDetailed.length > 0 && this.accountingViewSpecificChartLabelsDetailed && this.accountingViewSpecificChartLabelsDetailed.length > 0 && this.showAccountingViewChart == true) {
                                this.accountingViewChartDisplay = true;
                                this.showAccountingViewBackButton = true;
                                this.splitAccountingCard = true;
                                this.blockedDocument = false;
                            }
                        }, 0);
                    }
                });
        }
    }

    /* Function to change Accounting chart options */
    accountingAnalysisOptionFun() {
        this.accountingChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            tooltips: {
                callbacks: {
                    label: function (tooltipItems, data) {
                        return data.datasets[tooltipItems.datasetIndex].label;/*  + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + ' % ' */
                        /* ( ' + finalinput + ' ) */
                    },
                    footer: function (tooltipItems, data) {
                        var input = data.datasets[tooltipItems[0].datasetIndex].values[tooltipItems[0].index];
                        var finalinput;
                        if (input >= 1000000000000) {
                            finalinput = parseFloat((input / 1000000000000).toFixed(2).replace(/\.0$/, '')) + 'T';
                        }
                        else if (input >= 1000000000) {
                            finalinput = parseFloat((input / 1000000000).toFixed(2).replace(/\.0$/, '')) + 'B';
                        }
                        else if (input >= 1000000) {
                            finalinput = parseFloat((input / 1000000).toFixed(2).replace(/\.0$/, '')) + 'M';
                        }
                        else if (input >= 100000) {
                            finalinput = parseFloat((input / 1000).toFixed(2).replace(/\.0$/, '')) + 'K';
                        } else {
                            finalinput = parseFloat(input.toFixed(2).replace(/\.0$/, '')).toLocaleString();
                        }
                        return [data.datasets[tooltipItems[0].datasetIndex].valueType + ' :' + finalinput, 'Percentage :' + data.datasets[tooltipItems[0].datasetIndex].data[tooltipItems[0].index] + ' % '];
                    }
                }
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        fontColor: this.setThemeColor
                    }
                }],
                yAxes: [
                    {
                        ticks: {
                            min: 0,
                            max: 100,
                            callback: function (value) { return value + "%" },
                            fontColor: this.setThemeColor
                        },
                        gridLines: {
                            display: false
                        }
                    }
                ]
            },
            legend: {
                position: 'bottom',
                labels: {
                    fontColor: this.setThemeColor
                }
            },
            /* title: {
                display: true,
                text: this.currentAccountingAnalysisData + ' Wise Analysis',
                position: 'top',
                fontSize: 20,
                fontColor: this.setThemeColor
            }, */
            plugins: {
                datalabels: {
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    display: function (context) {
                        return (context.dataset.data[context.dataIndex] > 0 && !(context.dataset.hidden));/* && context.active */
                    },
                    align: function (context) {
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        var invert = Math.abs(value) <= 20;
                        return value < 20 ? 'top' : 'end'
                    },
                    borderWidth: 1,
                    borderRadius: 4,
                    formatter: function (value, context) {
                        return value + '%';
                    },
                    /* color: function (context) {
                        //console.log('context ' + context);
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        return value < 0 ? 'green' :  // draw negative values in red
                            context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green

                                'rgba(52, 168, 83, 0.9)';

                    } */
                }
            }
        };
    }

    accountingViewAnalysisOptionFunction() {
        this.accountingViewSpecificChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                position: 'bottom',
                display: true,
                labels: {
                    fontColor: this.setThemeColor
                }
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10
                }
            },
            /* title: {
                display: true,
                text: 'Data Source Analysis',
                position: 'top',
                fontSize: 20,
                fontColor: this.setThemeColor
            }, */
            plugins: {
                datalabels: {
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    display: true,
                    formatter: function (value, context) {
                        return value + '%';
                    }
                }
            },
            tooltips: {
                callbacks: {
                    label: function (tooltipItems, data) {
                        return data.labels[tooltipItems.index];
                    },
                    footer: function (tooltipItems, data) {
                        var input = data.datasets[tooltipItems[0].datasetIndex].values[tooltipItems[0].index];
                        var finalinput;
                        if (input >= 1000000000000) {
                            finalinput = parseFloat((input / 1000000000000).toFixed(2).replace(/\.0$/, '')) + 'T';
                        }
                        else if (input >= 1000000000) {
                            finalinput = parseFloat((input / 1000000000).toFixed(2).replace(/\.0$/, '')) + 'B';
                        }
                        else if (input >= 1000000) {
                            finalinput = parseFloat((input / 1000000).toFixed(2).replace(/\.0$/, '')) + 'M';
                        }
                        else if (input >= 100000) {
                            finalinput = parseFloat((input / 1000).toFixed(2).replace(/\.0$/, '')) + 'K';
                        } else {
                            finalinput = parseFloat(input.toFixed(2).replace(/\.0$/, '')).toLocaleString();
                        }
                        return [data.datasets[tooltipItems[0].datasetIndex].valueType + ' :' + finalinput, 'Percentage :' + data.datasets[tooltipItems[0].datasetIndex].data[tooltipItems[0].index] + ' % '];
                    }
                }
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        fontColor: this.setThemeColor
                    }
                }],
                yAxes: [
                    {
                        ticks: {
                            min: 0,
                            max: 100,
                            callback: function (value) { return value + "%" },
                            fontColor: this.setThemeColor
                        },
                        gridLines: {
                            display: false
                        }
                    }
                ]
            }
        };
    }

    /* Function to change Accounting chart color based on chart type */

    changeAccountingChartColor() {
        if (this.accountingChartType == 'bar') {
            this.accountingChartColors = [
                {
                    backgroundColor: 'rgba(234, 67, 53, 0.9)',
                    borderColor: 'rgba(234, 67, 53, 0.9)',
                    pointBackgroundColor: 'rgba(234, 67, 53, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(234, 67, 53, 0.5)'
                },
                {
                    backgroundColor: 'rgba(251, 188, 5, 0.9)',
                    borderColor: 'rgba(251, 188, 5, 0.9)',
                    pointBackgroundColor: 'rgba(251, 188, 5, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(251, 188, 5, 0.5)'
                },
                {
                    backgroundColor: 'rgba(52, 168, 83, 0.9)',
                    borderColor: 'rgba(52, 168, 83, 0.9)',
                    pointBackgroundColor: 'rgba(52, 168, 83, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(52, 168, 83, 0.5)'
                }
            ];
        } else if (this.accountingChartType == 'line') {
            this.accountingChartColors = [
                {
                    backgroundColor: 'transparent',
                    borderColor: 'rgba(234, 67, 53, 0.9)',
                    pointBackgroundColor: 'rgba(234, 67, 53, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(234, 67, 53, 0.5)'
                },
                {
                    backgroundColor: 'transparent',
                    borderColor: 'rgba(251, 188, 5, 0.9)',
                    pointBackgroundColor: 'rgba(251, 188, 5, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(251, 188, 5, 0.5)'
                },
                {
                    backgroundColor: 'transparent',
                    borderColor: 'rgba(52, 168, 83, 0.9)',
                    pointBackgroundColor: 'rgba(52, 168, 83, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(52, 168, 83, 0.5)'
                }
            ];
        }
    }

    /* ETL */

    changeETLGrpBy(val: any) {
        this.selectedETLFilterValue = val.value;
        this.extractionFailedStatus = false;
        this.extractedStatus = false;
        this.transformationFailedStatus = false;
        this.transformedStatus = false;
        this.fetchETLDetailsList();
    }

    /* Function to get ETL Details For Given Period */


    fetchETLDetailsList() {
        //console.log('showETLPreviousButton ' + this.showETLPreviousButton);
        //console.log('this.etlPeriodPreviousDateRange ' + JSON.stringify(this.etlPeriodPreviousDateRange));
        //console.log('etlStEndRange ' + JSON.stringify(this.etlStEndRange));
        this.showETLChart = false;
        this.blockedDocument = true;
        let tempExtractionFailed = [];
        let tempExtracted = [];
        let tempTransformationFailed = [];
        let tempTransformed = [];
        let tempETLLabels = [];
        let extractionAnalysisData = [];
        let transformationAnalysisData = [];
        this.showETLPreviousButton = 'none';
        //this.reconciliationAnalysisData = [];
        this.request = this.dashboardService.fetchExtractionForGivenPeriod(this.processFilter.processId, this.etlPeriodAnalysisType, this.etlStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            extractionAnalysisData = res.json();
            this.request = this.dashboardService.fetchTransformationForGivenPeriod(this.processFilter.processId, this.etlPeriodAnalysisType, this.etlStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
                transformationAnalysisData = res.json();
                //console.log(res.headers.get('tarnsformationperiodanalysistype'));
                this.etlPeriodAnalysisType = res.headers.get('tarnsformationperiodanalysistype');
                extractionAnalysisData.forEach(elementExt => {

                    /* tempExtractionFailed.push(elementExt.ntExtracted.count); */
                    tempExtracted.push(elementExt.extracted.count);
                });
                transformationAnalysisData.forEach(elementTrans => {
                    tempETLLabels.push(elementTrans.labelValue);
                    tempTransformationFailed.push(elementTrans.ntTransformed.count);
                    tempTransformed.push(elementTrans.transformed.count);
                });

                //console.log('this.etlPeriodAnalysisType ' + this.etlPeriodAnalysisType);
                this.etlChartData = [
                    /* { data: tempUnReconData, label: 'Un-Reconciled', hidden: this.unReconStatus },
                    { data: tempReconData, label: 'Reconciled', hidden: this.reconStatus } */

                    /* { data: tempExtractionFailed, label: 'Extraction Failed', hidden: this.extractionFailedStatus }, */
                    { data: tempExtracted, label: 'Extracted', hidden: this.extractedStatus },
                    { data: tempTransformationFailed, label: 'Transformation Failed', hidden: this.transformationFailedStatus },
                    { data: tempTransformed, label: 'Transformed', hidden: this.transformedStatus }
                ];
                setTimeout(() => { this.etlChartLabels = tempETLLabels }, 0);
                setTimeout(() => {
                    this.etlAnalysisOptionFun();
                    this.changeETLChartColor();
                    if (this.etlChartData[0].data.length > 0 && this.etlChartLabels && this.etlChartLabels.length > 0) {
                        this.showETLChart = true;
                        this.blockedDocument = false;
                        if (this.etlPeriodAnalysisType == 'Months') {
                            this.etlAnalysisData = res.json();
                            //console.log('this.etlAnalysisData ' + JSON.stringify(this.etlAnalysisData));
                            //console.log('this.etlStEndRange ' + JSON.stringify(this.etlStEndRange));
                            /* this.selectedPeriodForReconMonth = undefined;
                            this.selectedPeriodForReconWeek = undefined; */
                            this.etlPeriodPreviousDateRange[0] = this.etlStEndRange;
                            this.currentETLAnalysisData = 'Months';
                            this.showETLPreviousButton = 'None';
                            this.fetchETLViewAnalysis();
                        } else if (this.etlPeriodAnalysisType == 'Weeks') {
                            this.etlDrillDownAnalysisData = res.json();
                            //console.log('this.etlDrillDownAnalysisData ' + JSON.stringify(this.etlDrillDownAnalysisData));
                            /* this.selectedPeriodForReconMonth = this.etlAnalysisData[this.reconSelectedMonthIndex].labelValue;
                            this.selectedPeriodForReconWeek = undefined; */
                            this.etlPeriodPreviousDateRange[1] = this.etlStEndRange;
                            this.currentETLAnalysisData = 'Weeks';
                            if (this.etlAnalysisData.length > 0) {
                                this.showETLPreviousButton = 'Months';
                            } else {
                                this.showETLPreviousButton = 'None';
                            }
                            this.fetchETLViewAnalysis();
                        } else {
                            this.etlWeekDrillDownAnalysisData = res.json();
                            //console.log('this.etlWeekDrillDownAnalysisData ' + JSON.stringify(this.etlWeekDrillDownAnalysisData));
                            //this.selectedPeriodForReconMonth = this.etlAnalysisData[this.reconSelectedMonthIndex].labelValue;
                            /* this.selectedPeriodForReconWeek = this.etlAnalysisData[this.reconSelectedWeekIndex].labelValue;;
                            this.etlPeriodPreviousDateRange[2] = this.stEndRange; */
                            this.currentETLAnalysisData = 'Days';
                            if (this.etlDrillDownAnalysisData.length > 0) {
                                this.showETLPreviousButton = 'Weeks';
                            } else {
                                this.showETLPreviousButton = 'None';
                            }
                            this.fetchETLViewAnalysis();
                        }
                        //console.log('this.etlPeriodPreviousDateRange ' + JSON.stringify(this.etlPeriodPreviousDateRange));
                    }
                }, 0);
            })

        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching reconciliation analysis');
                this.blockedDocument = false;
            });

        this.request = this.navbarService.getETLDetailedAnalysisData(this.processFilter.processId, this.accStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            this.etlKPIDetailList = res;
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something Went Wrong');
                this.blockedDocument = false;
            });
    }

    showETLViewBackButton: boolean = false;

    etlSelectedProfileName: any;
    etlSelectedProfileId: any;
    etlGrpByView: any;
    fetchETLViewAnalysis() {
        this.etlSelectedProfileName = undefined;
        this.showETLViewBackButton = false;
        this.etlViewSpecificAnalysis = [];
        //this.dateRangeForViewETL = this.etlStEndRange;
        this.request = this.dashboardService.fetchETLDetailsAnalysisForGivenPeriod(this.processFilter.processId, this.etlStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
            this.etlViewSpecificAnalysis = res.json();
            //console.log('this.etlViewSpecificAnalysis ' + JSON.stringify(this.etlViewSpecificAnalysis));
            let tempViewLabel = [];
            let tempViewData = [];
            //console.log('this.etltempSelectedLabel ' + this.etltempSelectedLabel);
            if (this.etlViewSpecificAnalysis.length > 0) {
                this.etlViewSpecificAnalysis.forEach(element => {
                    tempViewLabel.push(element.profileName);
                    if (this.etltempSelectedLabel == 'Extraction Failed') {
                        tempViewData.push(element.ntExtracted.count);
                    } else if (this.etltempSelectedLabel == 'Extracted') {
                        tempViewData.push(element.extracted.count);
                    }
                    else if (this.etltempSelectedLabel == 'Transformation Failed') {
                        tempViewData.push(element.trasnformFailed.transfomedFailed);
                    }
                    else if (this.etltempSelectedLabel == 'Transformed') {
                        tempViewData.push(element.trasnformed.transfomed);
                    }
                });

                this.etlViewSpecificChartData = tempViewData;
                //console.log('this.etlViewSpecificChartData ' + JSON.stringify(this.etlViewSpecificChartData));
                setTimeout(() => {
                    this.etlViewSpecificChartLabels = tempViewLabel;
                }, 0);
                setTimeout(() => {
                    this.etlViewAnalysisOptionFunction();
                    if (this.etlViewSpecificChartData.length > 0 && this.etlViewSpecificChartLabels && this.etlViewSpecificChartLabels.length > 0 && this.showETLViewChart == true) {
                        //console.log('this.etlViewSpecificChartData ' + JSON.stringify(this.etlViewSpecificChartData));
                        this.etlViewChartDisplay = true;
                        this.splitETLCard = true;
                        this.blockedDocument = false;
                    }
                }, 0);
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.commonService.error('Error!', 'Something went wrong while fetching reconciliation analysis');
                this.blockedDocument = false;
            });
    }

    getETLViewDrillDownAnalysis(e) {
        //console.log('e.active[0]._index ' + e.active[0]._index);
        if (e.active[0] != undefined && this.showETLViewBackButton == false) {
            this.etlSelectedProfileName = this.etlViewSpecificAnalysis[e.active[0]._index].profileName;
            let tempViewLabel = [];
            let tempViewData = [];
            //console.log('this.etltempSelectedLabel ' + this.etltempSelectedLabel);
            if (this.etlViewSpecificAnalysis.length > 0) {
                this.etlViewSpecificAnalysis[e.active[0]._index]['templateDetails'].forEach(element => {
                    tempViewLabel.push(element.tempName);
                    if (this.etltempSelectedLabel == 'Extraction Failed') {
                        tempViewData.push(element.ntExtracted.count);
                    } else if (this.etltempSelectedLabel == 'Extracted') {
                        tempViewData.push(element.extracted.count);
                    }
                    else if (this.etltempSelectedLabel == 'Transformation Failed') {
                        tempViewData.push(element.trasnformFailed.transfomedFailed);
                    }
                    else if (this.etltempSelectedLabel == 'Transformed') {
                        tempViewData.push(element.trasnformed.transfomed);
                    }
                });

                this.etlViewSpecificChartData = tempViewData;
                //console.log('this.etlViewSpecificChartData ' + JSON.stringify(this.etlViewSpecificChartData));
                setTimeout(() => {
                    this.etlViewSpecificChartLabels = tempViewLabel;
                }, 0);
                setTimeout(() => {
                    this.etlViewAnalysisOptionFunction();
                    if (this.etlViewSpecificChartData.length > 0 && this.etlViewSpecificChartLabels && this.etlViewSpecificChartLabels.length > 0 && this.showETLViewChart == true) {
                        //console.log('this.etlViewSpecificChartData ' + JSON.stringify(this.etlViewSpecificChartData));
                        this.etlViewChartDisplay = true;
                        this.splitETLCard = true;
                        this.blockedDocument = false;
                        this.showETLViewBackButton = true;
                    }
                }, 0);
            }
            /*  this.reconSelectedViewName = this.reconViewSpecificAnalysis.reconciliationData[e.active[0]._index].labelValue;
             this.reconSelectedViewId = this.reconViewSpecificAnalysis.reconciliationData[e.active[0]._index].viewId;
             this.navbarService.getReconGroupByLov(this.reconViewSpecificAnalysis.reconciliationData[e.active[0]._index].viewId).takeWhile(() => this.alive).subscribe((res: any) => {
                 this.reconGroupByLov = res;
                 this.fetchViewDrillDownAnalysis(this.reconGroupByLov.columnsList[0].columnAliasName, this.reconGroupByLov.columnsList[0].columnName);
                 this.activeItem1 = this.reconGroupByLov.columnsList[0];
             }); */
        }
    }

    displayPreviousETLAnalysis() {
        this.blockedDocument = true;
        let tempExtractionFailed = [];
        let tempExtracted = [];
        let tempTransformationFailed = [];
        let tempTransformed = [];
        let tempETLLabels = [];
        this.etlChartLabels = [];
        this.etlChartData = [];
        this.extractionFailedStatus = false;
        this.extractedStatus = false;
        this.transformationFailedStatus = false;
        this.transformedStatus = false;
        //console.log('this.showETLPreviousButton ' + this.showETLPreviousButton);
        if (this.currentETLAnalysisData == 'Months') {
            this.selectedPeriodForETLMonth = undefined;
            this.selectedPeriodForETLWeek = undefined;
            this.etlStEndRange = this.etlPeriodPreviousDateRange[0];
            this.etlPeriodAnalysisType = 'None';
            this.fetchETLDetailsList();
            /* this.currentETLAnalysisData = 'Month';
            this.showETLPreviousButton = 'none'; */
        } else if (this.currentETLAnalysisData == 'Weeks') {
            this.selectedPeriodForETLMonth = undefined;
            this.selectedPeriodForETLWeek = undefined;
            this.etlStEndRange = this.etlPeriodPreviousDateRange[0];
            /* this.etlPeriodAnalysisType = 'Weeks'; */
            this.currentETLAnalysisData = 'Weeks';
            if (this.etlAnalysisData.length > 0) {
                this.etlPeriodAnalysisType = 'None';
            } else {
                this.etlPeriodAnalysisType = 'None';
            }
            this.fetchETLDetailsList();
        } else if (this.currentETLAnalysisData == 'Days') {
            this.selectedPeriodForETLMonth = undefined;
            this.selectedPeriodForETLWeek = undefined;
            this.etlStEndRange = this.etlPeriodPreviousDateRange[1];
            this.etlPeriodAnalysisType = 'Days';
            /* this.currentETLAnalysisData = 'Week'; */
            if (this.etlAnalysisData.length > 0) {
                this.etlPeriodAnalysisType = 'Months';
            } else {
                this.etlPeriodAnalysisType = 'None';
            }
            this.fetchETLDetailsList();
        }
    }

    /* Function to change recon chart options */
    etlAnalysisOptionFun() {

        this.etlChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            /* tooltips: {
                callbacks: {
                    label: function (tooltipItems, data) {
                        return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + '%';
                    }
                }
            }, */
            layout: {
                padding: {
                    left: 10,
                }
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        fontColor: this.setThemeColor
                    }
                }],
                yAxes: [
                    {
                        ticks: {
                            min: 0,
                            /* max: 100, */
                            /* callback: function (value) { return value + "%" }, */
                            fontColor: this.setThemeColor,
                        },
                        gridLines: {
                            display: false
                        }
                    }
                ]
            },
            legend: {
                position: 'bottom',
                labels: {
                    fontColor: this.setThemeColor
                }
            },
            plugins: {
                datalabels: {
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    display: function (context) {
                        return (context.dataset.data[context.dataIndex] > 0 && !(context.dataset.hidden));/* && context.active */
                    },
                    align: function (context) {
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        var invert = Math.abs(value) <= 0;
                        return value < 0 ? 'top' : 'end'
                    },
                    borderWidth: 1,
                    borderRadius: 4,
                    /* align: function(context) {
                        var chart = context.chart;
                        var area = chart.chartArea;
                        var meta = chart.getDatasetMeta(context.datasetIndex);
               
                        // WARNING: meta.data._model is PRIVATE and thus can 
                        // change without notice, breaking code relying on it!
                        var model = meta.data[context.dataIndex]._model;
                        var bottom = Math.min(model.base, area.bottom);
                        var height = bottom - model.y;
                        return height < 50 ? 'end' : 'top'
                      }, */
                    /* color: this.setThemeColor */
                    /* formatter: function (value, context) {
                        return value + '%';
                    }, */
                    /* color: function (context) {
                        //console.log('context ' + context);
                        var index = context.dataIndex;
                        var value = context.dataset.data[index];
                        return value < 0 ? 'green' :  // draw negative values in red
                            context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green

                                'rgba(52, 168, 83, 0.9)';

                    } */
                }
            }
        };
    }

    /* Function to change ETL chart color based on chart type */

    changeETLChartColor() {
        if (this.etlChartType == 'bar') {
            this.etlChartColors = [
                { backgroundColor: '#3366CC' },
                { backgroundColor: '#DC3912' },
                { backgroundColor: '#FF9900' },
                { backgroundColor: '#3B3EAC' }
            ];
        } else if (this.etlChartType == 'line') {
            this.etlChartColors = [
                { backgroundColor: 'transparent', borderColor: '#3366CC' },
                { backgroundColor: 'transparent', borderColor: '#DC3912' },
                { backgroundColor: 'transparent', borderColor: '#FF9900' },
                { backgroundColor: 'transparent', borderColor: '#3B3EAC' }
            ];
        }
    }

    getETLDrillDownAnalysis(e) {
        if (e.active[0] != undefined) {
            //console.log('showETLPreviousButton ' + this.showETLPreviousButton);
            //console.log('this.etlPeriodPreviousDateRange ' + JSON.stringify(this.etlPeriodPreviousDateRange));
            //console.log('etlStEndRange ' + JSON.stringify(this.etlStEndRange));
            this.etltempSelectedLabel = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
            //console.log('this.etltempSelectedLabel ' + this.etltempSelectedLabel);
            if (this.etltempSelectedLabel == 'Extraction Failed') {
                this.extractionFailedStatus = false;
                this.extractedStatus = true;
                this.transformationFailedStatus = true;
                this.transformedStatus = true;
            } else if (this.etltempSelectedLabel == 'Extracted') {
                this.extractionFailedStatus = true;
                this.extractedStatus = false;
                this.transformationFailedStatus = true;
                this.transformedStatus = true;
            } else if (this.etltempSelectedLabel == 'Transformation Failed') {
                this.extractionFailedStatus = true;
                this.extractedStatus = true;
                this.transformationFailedStatus = false;
                this.transformedStatus = true;
            } else {
                this.extractionFailedStatus = true;
                this.extractedStatus = true;
                this.transformationFailedStatus = true;
                this.transformedStatus = false;
            }
            this.blockedDocument = true;
            this.showETLViewChart = true;
            //console.log('this.currentETLAnalysisData ' + this.currentETLAnalysisData);
            //console.log('this.etlDrillDownAnalysisData ' + JSON.stringify(this.etlDrillDownAnalysisData));
            //console.log('this.etlAnalysisData ' + JSON.stringify(this.etlAnalysisData));
            if (this.currentETLAnalysisData == 'Months') {
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                let obj = {
                    "startDate": new Date(this.etlAnalysisData[e.active["0"]._index].dateRange.startDate),
                    "endDate": new Date(this.etlAnalysisData[e.active["0"]._index].dateRange.endDate)
                }
                this.etlStEndRange = obj;
                this.etlPeriodAnalysisType = 'Months';
                this.etlSelectedMonthIndex = e.active["0"]._index;
                this.selectedPeriodForETLMonth = this.etlAnalysisData[this.etlSelectedMonthIndex].labelValue;
                this.selectedPeriodForETLWeek = undefined;
                this.etlChartLabels = [];
                this.etlChartData = [];
                this.fetchETLDetailsList();
            } else if (this.currentETLAnalysisData == 'Weeks') {
                //console.log('this.etlDrillDownAnalysisData ' + JSON.stringify(this.etlDrillDownAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                let obj = {
                    "startDate": new Date(this.etlDrillDownAnalysisData[e.active["0"]._index].dateRange.startDate),
                    "endDate": new Date(this.etlDrillDownAnalysisData[e.active["0"]._index].dateRange.endDate)
                }
                //console.log('obj ' + JSON.stringify(obj));
                this.etlStEndRange = obj;
                this.etlPeriodAnalysisType = 'Week';
                this.etlSelectedWeekIndex = e.active["0"]._index;
                //console.log('this.etlSelectedWeekIndex ' + JSON.stringify(this.etlSelectedWeekIndex));
                this.selectedPeriodForETLWeek = this.etlDrillDownAnalysisData[this.etlSelectedWeekIndex].labelValue;
                this.etlChartLabels = [];
                this.etlChartData = [];
                this.fetchETLDetailsList();
            } else {
                //console.log('this.etlAnalysisData ' + JSON.stringify(this.etlAnalysisData));
                //console.log('e.active["0"]._index ' + JSON.stringify(e.active["0"]._index));
                this.fetchETLDetailsList();
            }
        }
    }

    etlViewAnalysisOptionFunction() {
        this.etlViewSpecificChartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                position: 'bottom',
                display: true,
                labels: {
                    fontColor: this.setThemeColor
                }
            },
            layout: {
                padding: {
                    left: 10,
                    right: 10
                }
            },
            /* title: {
                display: true,
                text: 'Data Source Analysis',
                position: 'top',
                fontSize: 20,
                fontFamily: 'Lato',
                fontWeight: 400,
                fontColor: this.setThemeColor
            }, */
            plugins: {
                datalabels: {
                    display: true,
                    color: 'black',
                    font: {
                        weight: 'bold'
                    },
                    /* formatter: function (value, context) {
                        return value + '%';
                    } */
                }
            },
            /*  tooltips: {
                 callbacks: {
                     label: function (tooltipItems, data) {
                         return data.labels[tooltipItems.index] + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                     }
                 }
             } */
        };
    }

    updateViolationAnalysis(type) {
        if (type == 'reconciliation') {
            this.reconViolationPeriod = this.tempreconViolationPeriod;
            this.request = this.dashboardService.fetchViolationAnalysis(this.processFilter.processId, this.reconViolationPeriod, type, this.stEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
                this.reconViewSpecificAnalysis1.unReconItemsViolation = res.unReconItemsViolation;
                this.reconViewSpecificAnalysis1.unReconItemsViolationPer = res.unReconItemsViolationPer;
                this.showReconViolationEdit = false;
            });
        } else {
            this.accountingViolationPeriod = this.tempaccountingViolationPeriod;
            this.request = this.dashboardService.fetchViolationAnalysis(this.processFilter.processId, this.accountingViolationPeriod, type, this.accStEndRange).takeWhile(() => this.alive).subscribe((res: any) => {
                this.accountingViewSpecificAnalysis1.unAccountedItemsViolation = res.unAccountedItemsViolation;
                this.accountingViewSpecificAnalysis1.unAccountedItemsViolationPer = res.unAccountedItemsViolationPer;
                this.showAccountingViolationEdit = false;
            });
        }
    }
    cancelViolationAnalysis(type) {
        if (type == 'reconciliation') {
            this.tempreconViolationPeriod = this.reconViolationPeriod;
            this.showReconViolationEdit = false;
        } else {
            this.tempaccountingViolationPeriod = this.accountingViolationPeriod;
            this.showAccountingViolationEdit = false;
        }
    }
    test() {
        $('.example-margin .mat-slider .mat-accent .mat-slider-horizontal').addClass('.cdk-focused, .cdk-mouse-focused');
    }

    /* Function To redirect to reconciliation work queue */
    goToRWQ(e) {
        if (e.active[0] != undefined) {
            //console.log('this.commonService.reconDashBoardObject ' + JSON.stringify(this.commonService.reconDashBoardObject));
            //console.log('e ' + e);
            //console.log('this.selectedCombViewIndex ' + this.selectedCombViewIndex);
            //console.log('this.reconViewCombinationSpecificAnalysis ' + JSON.stringify(this.reconViewCombinationSpecificAnalysis));
            //console.log('this.dataviewdata ' + JSON.stringify(this.dataviewdata));
            this.commonService.reconDashBoardObject.ruleGroupId = this.processesDetailsList[0].processDetailList.recGrpList[0].typeId;
            this.commonService.reconDashBoardObject.ruleGroupName = this.processesDetailsList[0].processDetailList.recGrpList[0].reconcRuleGroupName;
            this.commonService.reconDashBoardObject.periodFactor = "fileDate";
            this.reconViewCombinationSpecificAnalysis[this.selectedCombViewIndex].combination.forEach(element => {
                if (element.viewType == "Source") {
                    this.commonService.reconDashBoardObject.sourceViewId = element.viewIdDisplay;
                    this.commonService.reconDashBoardObject.sourceViewName = element.viewName;
                } else if (element.viewType == "Target") {
                    this.commonService.reconDashBoardObject.targetViewId = element.viewIdDisplay;
                    this.commonService.reconDashBoardObject.targetViewName = element.viewName;
                }
            });

            if (this.tempSelectedLabel == 'Un-Reconciled') {
                this.commonService.reconDashBoardObject.status = 'unreconciled';
            } else {
                this.commonService.reconDashBoardObject.status = 'reconciled';
            }
            this.commonService.reconDashBoardObject.startDateRange = this.stEndRange.startDate;
            this.commonService.reconDashBoardObject.endDateRange = this.stEndRange.endDate;

            if (this.commonService.reconDashBoardObject.ruleGroupId && this.commonService.reconDashBoardObject.sourceViewId && this.commonService.reconDashBoardObject.sourceViewName
                && this.commonService.reconDashBoardObject.status && this.commonService.reconDashBoardObject.startDateRange && this.commonService.reconDashBoardObject.endDateRange) {
                //console.log('this.commonService.reconDashBoardObject ' + JSON.stringify(this.commonService.reconDashBoardObject));
                let link = ['/reconcilewq', { outlets: { 'content': 'reconcile-details' } }];
                this.router.navigate(link);
            }
        }
    }

    /* Function to redirect to accounting work queue */
    goToAWQ(e) {
        if (e.active[0] != undefined) {
            //console.log('e ' + e);
            /* //console.log('this.accDetailedList ' + JSON.stringify(this.accDetailedList));
            //console.log('this.selectedTabProcessWiseAnalysis ' + this.selectedTabProcessWiseAnalysis);
            //console.log('this.selectedAccViewId ' + this.selectedAccViewId + ' this.selectedAccViewName ' + this.selectedAccViewName);
            //console.log('this.selectedAccModuleName ' + this.selectedAccModuleName);
            //console.log('this.accountingDateObj ' + JSON.stringify(this.accountingDateObj));
            //console.log('this.processesDetailsList ' + JSON.stringify(this.processesDetailsList));
            this.commonService.acctDashBoardObject.ruleGroupId = this.processesDetailsList[0].processDetailList.actGrpList[0].typeId;
        this.commonService.acctDashBoardObject.dataViewId = this.selectedAccViewId;
        this.commonService.acctDashBoardObject.dataViewName = this.selectedAccViewName;

                if(this.selectedAccModuleName == 'Not Accounted'){
                    this.commonService.acctDashBoardObject.status = 'un accounted';
                }else{
                    this.commonService.acctDashBoardObject.status = 'accounted';
                }
                this.commonService.acctDashBoardObject.startDateRange = this.accountingDateObj.startDate;
                this.commonService.acctDashBoardObject.endDateRange = this.accountingDateObj.endDate;
                //console.log('ruleGrpId ' + this.commonService.acctDashBoardObject.ruleGroupId);
                //console.log('dataViewId ' + this.commonService.acctDashBoardObject.dataViewId);
                //console.log('dataViewName ' + this.commonService.acctDashBoardObject.dataViewName);
                //console.log('status ' + this.commonService.acctDashBoardObject.status);
                //console.log('startDateRange ' + this.commonService.acctDashBoardObject.startDateRange);
                //console.log('endDateRange ' + this.commonService.acctDashBoardObject.endDateRange);
                let link = ['/accounting-data', {outlets: {'content':'accounting-data-home'}}]
                this.router.navigate(link); */
        }
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    // selectedProcessId:any;
    reconViolation: any;
    accViolation: any;
    dateRange: any;
    openDialog1(val): void {
        let dialogRef = this.dialog.open(KPIModal, {
            width: '500px',
            data: { val: val }
        });

        dialogRef.afterClosed().takeWhile(() => this.alive).subscribe(result => {
            //console.log(result);
            if (result != undefined && result.savePreference == true) {
                let obj = {
                    "value": JSON.stringify(result)
                }
                this.navbarService.postFormConfigDetails('dashboardV1', 'customHeadings', 'user', obj).takeWhile(() => this.alive).subscribe((res: any) => {
                    this.commonService.success('Success', 'Successfully Updated');
                    if (result.loadsavedPreference == true) {
                        $('.dashboardV7 .customToggleTabs .mat-button-toggle-checked').removeClass('mat-button-toggle-checked');
                        this.ngOnInit();
                    }
                },
                    (res: Response) => {
                        this.onError(res.json()
                        )
                        this.commonService.error('Error!', 'Something Went Wrong While Posting');
                        this.blockedDocument = false;
                    });
            }
        });
    }
}

@Component({
    selector: 'kpiModal',
    templateUrl: 'dashboardModal-v7.html',
})
export class KPIModal {
    userPreference = new UserPreference();
    dateRanges: any;
    processesList: any;
    processesDetailsList: any;
    private alive: boolean = true;      //Unsubscribe variable
    constructor(
        public dialogRef: MdDialogRef<KPIModal>,
        private navbarService: NavBarService,
        private processesService: ProcessesService,
        @Inject(MD_DIALOG_DATA) public data: any) { }

    ngOnInit() {
        this.dateRanges = [{ "name": "Today" }, { "name": "Last 7 Days" }, { "name": "Last 14 Days" }, { "name": "This Month" }, { "name": "Last 2Months" }, { "name": "Last 3Months" }, { "name": "Last 6Months" }];
        this.navbarService.getprocessessList().takeWhile(() => this.alive).subscribe((res: any) => {
            this.processesList = res;
        });
        this.navbarService.getFormConfigDetails('dashboardV1', 'customHeadings', 'user').takeWhile(() => this.alive).subscribe((res: any) => {
            if (res) {
                let temp = JSON.parse(res.value);
                //this.userPreference = temp;
                temp.savePreference = false;
                temp.loadsavedPreference = false;
                this.userPreference.processesId = temp.processesId;
                //  this.userPreference.dateRange = temp.dateRange;
                this.userPreference.reconViolationPeriod = temp.reconViolationPeriod;
                this.userPreference.accountingViolationPeriod = temp.accountingViolationPeriod;
                this.userPreference.drillThruSeconds = temp.drillThruSeconds;
            } else {
                if (this.processesList) {
                    this.processesService.getProcessDetail(this.processesList[0].id).takeWhile(() => this.alive).subscribe((res) => {
                        this.processesDetailsList = res;
                        this.userPreference.processesId = this.processesList[0].id;
                        // this.userPreference.dateRange = this.dateRanges[3].name;
                        this.userPreference.reconViolationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                        this.userPreference.accountingViolationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
                        this.userPreference.drillThruSeconds = 3000;
                    });
                }
            }
        });
    }
    onNoClick(): void {
        this.dialogRef.close(this.data);
    }
    ngOnDestroy() {
        $('.spinner, .clockwise').removeClass('hidden');
        this.alive = false;
    }
}