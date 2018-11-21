import { Component, OnInit, ViewChild, ChangeDetectionStrategy, TemplateRef, OnDestroy } from '@angular/core';
import { CommonService } from '../common.service';
import { DashBoard4 } from '../../home/home.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ng2-webstorage';
import { Subscription } from 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { DecimalPipe } from '@angular/common';
import Chart from 'chart.js';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { ReconcileService } from '../reconcile/reconcile.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { TenantDetailsService } from '../tenant-details/tenant-details.service';
import { DashboardService } from './dashboardver.service';
import { OrderBy } from "../orderByPipe";
import { ActivatedRoute, Router } from '@angular/router';
//import { IMyDrpOptions } from 'mydaterangepicker';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
/* import 'pivottable/dist/pivot.min.js';
import 'pivottable/dist/c3_renderers.js'; */
import { fadeInContent } from '@angular/material/typings/select/select-animations';
import 'moment';
declare var $: any;
declare var jQuery: any;
declare var moment: any;
@Component({
    selector: 'jhi-dashboardv8',
    templateUrl: './dashboard-v8.component.html',
    animations: [
        trigger(
            'enterAnimation', [
                transition(':enter', [
                    style({ opacity: 0 }),
                    animate('500ms 100ms ease-in', style({ opacity: 1 }))
                ])/* ,
                transition(':leave', [
                    style({  opacity: 1 }),
                    animate('100ms 100ms ease-in', style({  opacity: 0 }))
                ]) */
            ]
        ),
        trigger(
            'slideIn', [
                state('*', style({ 'overflow-y': 'hidden' })),
                state('void', style({ 'overflow-y': 'hidden' })),
                transition('* => void', [
                    style({ height: '*', opacity: 1 }),
                    animate('0.5s 100ms ease-in', style({ height: 0, opacity: 0 }))
                ]),
                transition('void => *', [
                    style({ height: '0', opacity: 0 }),
                    animate('0.5s 100ms ease-in', style({ height: '*', opacity: 1 }))
                ])
            ]
        )
    ]
})

export class DashboardV8Component implements OnInit, OnDestroy {
    private alive: boolean = true;      //Unsubscribe variable
    autoTicks = false;
    disabled = false;
    invert = false;
    max = 30;
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
    reconStatusTab: any = 0;
    accStatusTab: any = 0;
    reconCurrencyTab: any = 0;
    accCurrencyTab: any = 0;
    tempLoading: any = "loading";
    userDetails: any;
    processesList: any;
    testReconWidth: any = '300';
    testReconHeight: any = '300';
    processFilter = new DashBoard4();
    selectedReconAnalysis: any = 0;
    selectedTab: any = 0;
    selectedReconTab: any = 0;
    selectedAccTab: any = 0;
    selectedAccTabActivity: any = 0;
    selectedAccTabKPI: any = 0;
    currentModule: any = "Reconciliation Status";
    currentAccModule: any = 'Accounting Status';
    selectedViewRSAging: any = "Aging Bucket 4";
    selectedUnReconItemsValue: any = 'Top 10 by amount';
    selectedViolationType: any = "Days";
    selectedViolationOperatorType: any = "Greater Than Or Equal To";
    selectedViolationTypeValue: any = 415;
    topTenUnReconValue: any;
    topTenUnReconValueTrg: any;
    topTenUnReconValueSub: any;
    topTenUnReconValueSubTrg: any;
    topTenUnReconValueTitle: any;
    topTenUnReconViolation: any;
    topTenUnReconViolationTitle: any;
    awaitingApproval: any;
    awaitingApprovalCount: any;
    selectedUnReconItemsViolationValue: any = ">= 415";
    selectedProcess: any;
    showRecon: boolean = false;
    testshow: boolean = false;
    reconDateRange: any;
    accDateRange: any;
    showAccounting: boolean = false;
    showAccountingDrillDownData: boolean = false;
    selectedPeriodTab: any = 0;
    reconSrcBlock: boolean = false;
    reconTrgBlock: boolean = false;
    toggleReconView: boolean = true;
    /* Reconciliation Status*/
    reconColor: any = '#43A047';/* #8ac9f3 */
    unReconColor: any = '#f86f00';/* #fda4b8 */
    reconColorTemp: any = '#43A047';
    unReconColorTemp: any = '#f86f00';
    tempSelectedPostion: any = 'bottom';
    selectedPostion: any = 'bottom';
    reconBackgroundTemp: any = '#ffffff';
    reconBackground: any = '#ffffff';

    /* Version 8 Variables */
    showetl = false;
    showrecon = true;
    showacc = false;
    reconAllData: any[];
    recondetaildata: any;
    recondetaildataTrg: any;
    accountingAllData: any[];
    randomChartColors: any;
    monthslist: any = [];
    monthyear: any;
    selectedGrpByTab: any = 'Un-Reconciled';
    accSelectedGrpByTab: any = 'Un-Accounted';
    recon_app: boolean = false;
    acc_app: boolean = false;

    /* Custom Datepicker  @ Nish*/
    selectedMoreRange: any = true;
    startDate: any;
    endDate: any;
    isViewOnly: boolean = false;
    showReconDetilsTab: boolean = false;

    public pieChartReconciliation: any[];
    public pieChartReconciliationType: string = 'bar';
    public temppieChartReconciliationType: string = 'bar';
    tempSelectedReconChart: any = 'pie';
    public pieChartReconciliationLegend: boolean = true;
    public pieChartReconciliationLabels: string[] = [];
    public pieChartReconciliationOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.labels[tooltipItems.index] + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        },
        legend: {
            position: 'bottom',
            display: false
        },
        plugins: {
            datalabels: {
                display: true,
                color: 'black',
                font: {
                    weight: 'bold'
                },
                align: function (context) {
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    var invert = Math.abs(value) <= 20;
                    return value < 20 ? 'top' : 'center'
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
                },
                gridLines: {
                    display: false
                }
            }]
        }
    };
    public pieChartReconciliationColors: {}[] = [{ backgroundColor: [this.reconColor, this.unReconColor] }];

    /* UN-RECONCILED BY AGING BUCKET */
    agingColor: any = '#F44336';/* #fda4b8 */
    tempagingColor: any = '#F44336';
    agingBucketBackgroundTemp: any = '#ffffff';
    agingBucketBackground: any = '#ffffff';
    public barChartOptions: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    let labelValue = new DecimalPipe('en').transform(data.datasets[0].data[tooltipItems.index])
                    return data.labels[tooltipItems.index] + ': ' + labelValue;
                }
            }
        },
        scales: {
            xAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    autoSkip: false
                    /* fontColor: this.setThemeColor */
                }
            }],
            yAxes: [
                {
                    ticks: {
                        beginAtZero: true,
                        callback: function (value) {
                            if (value >= 1000000000000) {
                                return parseFloat((value / 1000000000000).toFixed(1).replace(/\.0$/, '')) + 'T';
                            }
                            else if (value >= 1000000000) {
                                return parseFloat((value / 1000000000).toFixed(1).replace(/\.0$/, '')) + 'B';
                            }
                            else if (value >= 1000000) {
                                return parseFloat((value / 1000000).toFixed(1).replace(/\.0$/, '')) + 'M';
                            }
                            else if (value >= 1000) {
                                return parseFloat((value / 1000).toFixed(1).replace(/\.0$/, '')) + 'K';
                            } else {
                                //return value.toFixed(1).replace(/\.0$/, '');
                                if (value) {
                                    // return parseFloat(value.toFixed(1).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
                                    // return value.toFixed(1).replace(/\.0$/, '');
                                    return parseFloat(value.toFixed(1).replace(/\.0$/, '')).toLocaleString();
                                } else {
                                    return 0;
                                }

                            }
                        }
                    },
                    gridLines: {
                        display: false
                    }
                }
            ]
        },
        legend: {
            position: 'bottom',
            display: false
        },
        plugins: {
            datalabels: {
                display: false
            }
        }
    };
    public barChartLabels: string[] = [];
    public barChartLabelsTrg: string[] = [];
    public barChartType: string = 'bar';
    public barChartTypeTrg: string = 'bar';
    public tempbarChartType: string = 'bar';
    public barChartLegend: boolean = true;
    public unReconAgingBucketColors: {}[] = this.commonService.randomColors;
    public barChartData: any[] = [];
    public barChartDataTrg: any[] = [];

    /* Accounting Aging */
    public accAgingChartLabels: string[] = [];
    public accAgingChartType: string = 'bar';
    public accAgingChartLegend: boolean = true;
    public accAgingBucketColors: {}[] = this.commonService.randomColors;
    public accAgingChartData: any[] = [];

    /* Un-Recon Items By Value */
    unReconItemsByValueRadius: any = 80;
    unReconItemsByValueOuterStrokeColor: any = '#03A9F4';
    unReconItemsByValueInnerStrokeColor: any = '#ededed';
    unReconItemsByValueTitleFontSize: any = 35;
    unReconItemsByValueSubtitleFontSize: any = 14;
    unReconItemsByValueTitleColor: any = '#444444';
    tempunReconItemsByValueTitleColor: any = '#444444';
    unReconItemsByValueSubTitleColor: any = '#A9A9A9';
    tempunReconItemsByValueSubTitleColor: any = '#A9A9A9';
    unReconItemsByValueUnitsColor: any = '#A9A9A9';
    tempunReconItemsByValueRadius: any = 80;
    tempunReconItemsByValueOuterStrokeColor: any = '#03A9F4';
    tempunReconItemsByValueInnerStrokeColor: any = '#ededed';
    tempunReconItemsByValueTitleFontSize: any = 35;
    tempunReconItemsByValueSubtitleFontSize: any = 14;

    /* Un-Recon Items By Violation */
    unReconItemsByViolationRadius: any = 80;
    unReconItemsByViolationOuterStrokeColor: any = '#9C27B0';
    unReconItemsByViolationInnerStrokeColor: any = '#ededed';
    unReconItemsByViolationTitleFontSize: any = 35;
    unReconItemsByViolationSubtitleFontSize: any = 14;
    unReconItemsByViolationTitleColor: any = '#444444';
    tempunReconItemsByViolationTitleColor: any = '#444444';
    unReconItemsByViolationSubTitleColor: any = '#A9A9A9';
    tempunReconItemsByViolationSubTitleColor: any = '#A9A9A9';
    unReconItemsByViolationUnitsColor: any = '#444444';
    tempunReconItemsByViolationRadius: any = 80;
    tempunReconItemsByViolationOuterStrokeColor: any = '#9C27B0';
    tempunReconItemsByViolationInnerStrokeColor: any = '#ededed';
    tempunReconItemsByViolationTitleFontSize: any = 35;
    tempunReconItemsByViolationSubtitleFontSize: any = 14;

    /* Awaiting For Approvals */
    reconAwaitingForApprovalRadius: any = 80;
    reconAwaitingForApprovalOuterStrokeColor: any = '#fd9700';
    reconAwaitingForApprovalInnerStrokeColor: any = '#ededed';
    reconAwaitingForApprovalTitleFontSize: any = 35;
    reconAwaitingForApprovalTitleColor: any = '#444444';
    tempreconAwaitingForApprovalRadius: any = 80;
    tempreconAwaitingForApprovalOuterStrokeColor: any = '#fd9700';
    tempreconAwaitingForApprovalInnerStrokeColor: any = '#ededed';
    tempreconAwaitingForApprovalTitleFontSize: any = 35;
    tempreconAwaitingForApprovalTitleColor: any = '#444444';
    isTimePickerEnabled = false;
    maxDate: Date;
    minDate: Date;
    /* myDateRangePickerOptions: IMyDrpOptions = {
        // other options...
        dateFormat: 'dd.mm.yyyy',
    };
    private model: any = {beginDate: {year: 2018, month: 10, day: 9},
                             endDate: {year: 2018, month: 10, day: 19}}; */
    selectedrange: any;
    /* daterangepickerOptions = {
        startDate: null,
        endDate: null,
        format: "DD MMM YYYY",
        maxDate: moment(),
        inactiveBeforeStart: true,
        autoApply: true,
        showRanges: true,
        preDefinedRanges: [{
            name: 'Today',
            value: {
                start: moment(),
                end: moment(),
            }
        }, {
            name: 'Yesterday',
            value: {
                start: moment().subtract(1, 'days'),
                end: moment().subtract(1, 'days'),
            }
        }, {
            name: 'Last(7)days',
            value: {
                start: moment().subtract(6, 'days'),
                end: moment()
            }
        },],
        singleCalendar: false,
        displayFormat: 'DD MMM YYYY',
        position: 'right',
        noDefaultRangeSelected: true,
        disableBeforeStart: true
    } */
    getprvsmonthslist() {
        this.monthslist = [];
        let curentDate = moment();
        let currentMonth = moment().format("MMM'YY");
        let endOfMonth;
        //console.log('currentMonth ' + currentMonth);
        for (let i = 0; i <= 11; i++) {
            curentDate = moment().subtract(i, 'month');
            const month = curentDate.format('MMM');
            const year = "'" + curentDate.format('YY');
            this.monthyear = month.concat(year);
            let startOfMonth = new Date(moment().subtract(i, 'month').startOf('month'));
            let d = new Date();
            d.setDate(startOfMonth.getDate());
            d.setMonth(startOfMonth.getMonth());
            d.setFullYear(startOfMonth.getFullYear());
            if (currentMonth == this.monthyear) {
                endOfMonth = new Date(moment());
            } else {
                endOfMonth = new Date(moment().subtract(i, 'month').endOf('month'));
            }
            let obj = {
                "periodName": this.monthyear,
                "startDate": d,
                "endDate": endOfMonth
            }
            this.monthslist.push(obj);
            this.selectedMonth = this.monthslist[0]['periodName'];
        }
        //console.log('this.monthslist ' + JSON.stringify(this.monthslist));
    }
    selectedMonth: any;
    changePeriod(e) {
        this.dateFormatRange = "Period Wise";
        /* console.log(e);
        console.log('stED ' + JSON.stringify(this.monthslist)); */
        this.selectedMonth = this.monthslist[e.index]['periodName'];
        this.reconDateRange.startDate = this.monthslist[e.index].startDate;
        this.reconDateRange.endDate = this.monthslist[e.index].endDate;
        /* console.log('this.reconDateRange.startDate ' + this.reconDateRange.startDate);
        console.log('this.reconDateRange.endDate ' + this.reconDateRange.endDate); */
        this.refreshData();
    }

    rangeSelected($event) {
        let userselecteddtrange;
        userselecteddtrange = $event;
        //console.log("rangeSelected" + JSON.stringify(userselecteddtrange));
        this.reconDateRange = userselecteddtrange;
    }
    custompicker: any = false;
    openPicker() {
        this.custompicker = true;
        this.reconDateRange = {
            "startDate": this.monthslist[this.selectedPeriodTab].startDate,
            "endDate": this.monthslist[this.selectedPeriodTab].endDate
        };
    }
    closePicker() {
        $('.refreshBut').removeClass('blink');
        this.custompicker = false;
    }
    refreshData() {
        this.reconDateRanges = [];
        this.accDateRanges = [];
        this.selectedReconTab = 0;
        this.selectedTab = 0;
        this.selectedAccTab = 0;
        this.selectedAccTabKPI = 0;
        $('.refreshBut').removeClass('blink');
        /* this.reconDateRange.startDate = this.commonService.convertDate(new Date(this.reconDateRange.startDate));
        this.reconDateRange.endDate = this.commonService.convertDate(new Date(this.reconDateRange.endDate)); */
        /* console.log('this.reconDateRange.startDate ' + this.reconDateRange.startDate);
        console.log('this.reconDateRange.endDate ' + this.reconDateRange.endDate); */
        this.ngOnInit();
    }
    dateFormatRange: any = "Period Wise";
    toggleRange(e) {
        $('.refreshBut').addClass('blink');
        // this.selectedMoreRange = false;
        let selectedRange: any;
        if (e.value == 'Today') {
            selectedRange = [moment(), moment()];
            this.selectedMonth = "Today";
        } else if (e.value == 'Last 7 Days') {
            selectedRange = [moment().subtract(6, 'days'), moment()];
            this.selectedMonth = "1 Week";
        } else if (e.value == 'Last 14 Days') {
            selectedRange = [moment().subtract(13, 'days'), moment()];
            this.selectedMonth = "2 Weeks";
        } else if (e.value == 'Last 30 Days') {
            selectedRange = [moment().subtract(29, 'days'), moment()];
            this.selectedMonth = "1 Month";
        } else if (e.value == 'Last 2Months') {
            selectedRange = [moment().subtract(1, 'month').startOf('month'), moment()];
            this.selectedMonth = "2 Months";
        } else if (e.value == 'Last 3Months') {
            selectedRange = [moment().subtract(2, 'month').startOf('month'), moment()];
            this.selectedMonth = "3 Months";
        } else if (e.value == 'Last 6Months') {
            selectedRange = [moment().subtract(5, 'month').startOf('month'), moment()];
            this.selectedMonth = "6 Months";
        } /* else if (e.value == 'Custom') {
            this.selectedMoreRange = true;
        } */ else if (e.value == 'thismonth') {
            selectedRange = [moment().startOf('month'), moment()];
            this.selectedMonth = "This Month";
        }
        if (e.value != 'Custom') {
            this.reconDateRange.startDate = new Date(selectedRange[0]);
            this.reconDateRange.endDate = new Date(selectedRange[1]);
            this.reconDateRange.startDate = new Date(selectedRange[0]);
            this.reconDateRange.endDate = new Date(selectedRange[1]);
            this.dateFormatRange = "Range Wise";
        }
        this.refreshData();
    }
    changeDateRange(val) {
        let selectedRange: any;
        if (val == 'Custom') {
            this.selectedMoreRange = true;
        } else if (val == 'Yesterday') {
            selectedRange = [moment().subtract(1, 'days'), moment().subtract(1, 'days')];
            this.startDate = new Date(selectedRange[0]);
            this.endDate = new Date(selectedRange[1]);
        } else if (val == 'This Month') {
            selectedRange = [moment().startOf('month').hours(16).minutes(35).seconds(58).milliseconds(587), moment()];
            this.startDate = new Date(selectedRange[0]);
            this.endDate = new Date(selectedRange[1]);
            this.reconDateRange = selectedRange;
            this.reconDateRange = selectedRange;
        }
    }
    /*@author: Nishanth*/
    request: any;
    getconfiguredmodules() {
        this.request = this.tenantService.fetchTenantConfigModule().subscribe(
            (res: any) => {
                let configmodule = res
                configmodule.forEach(element => {
                    if (element.modules == 'RECON_APPROVALS') {
                        this.recon_app = true;
                    }
                    else if (element.modules == 'ACCOUNTING_APPROVALS') {
                        this.acc_app = true;
                    }
                });
            });
    }

    /* Sample JSON For Tables */
    selectedViewRS: any = [];
    selectedViewRSUnRecon: any = "currency";

    selectedViewAcc: any = "rules";
    reconciliationStatusData: any = {
        "reconciled": [{ "Rule Name": "Rule 1", "Ledger Name": "NSPL Primary Ledger", "Count": 687, "Amount": "3,10,82,694.65", "Percentage": 1.2 }, { "Rule Name": "Rule 2", "Ledger Name": "NSPL Global", "Count": 16, "Amount": "3,50,50,955.12", "Percentage": 1.36 }, { "Rule Name": "Rule 3", "Ledger Name": "NSPL Primary Ledger", "Count": "3,421", "Amount": "97,60,04,034.25", "Percentage": 37.78 }, { "Rule Name": "Rule 4", "Ledger Name": "NSPL Primary Ledger", "Count": 7, "Amount": "9,68,43,335.47", "Percentage": 3.75 }, { "Rule Name": "Rule 5", "Ledger Name": "NSPL Primary Ledger", "Count": 15, "Amount": "76,14,03,293.76", "Percentage": 29.47 }, { "Rule Name": "Rule 6", "Ledger Name": "NSPL Primary Ledger", "Count": 11, "Amount": "52,52,27,565.25", "Percentage": 20.33 }, { "Rule Name": "Rule 7", "Ledger Name": "NSPL Primary Ledger", "Count": 6, "Amount": "1,96,96,496.58", "Percentage": 0.76 }, { "Rule Name": "Rule 8", "Ledger Name": "NSPL Global", "Count": 13, "Amount": "8,18,13,840.04", "Percentage": 3.17 }, { "Rule Name": "Rule 9", "Ledger Name": "NSPL Primary Ledger", "Count": 6, "Amount": "5,62,37,015.66", "Percentage": 2.18 }],
        "unReconciled": [{ "Currency Code": "AUD", "Ledger Name": "NSPL Primary Ledger", "Count": 48, "Amount": "3,51,75,673.73", "Percentage": 7.8 }, { "Currency Code": "BRL", "Ledger Name": "NSPL Primary Ledger", "Count": 79, "Amount": "34,17,83,450.98", "Percentage": 28.03 }, { "Currency Code": "CAD", "Ledger Name": "NSPL Primary Ledger", "Count": 2, "Amount": "39,11,852.17", "Percentage": 0.32 }, { "Currency Code": "CHF", "Ledger Name": "NSPL Primary Ledger", "Count": 6, "Amount": "2,84,104.86", "Percentage": 0.02 }, { "Currency Code": "CZK", "Ledger Name": "NSPL Primary Ledger", "Count": 2, "Amount": "23,99,779.13", "Percentage": 0.2 }, { "Currency Code": "DKK", "Ledger Name": "NSPL Primary Ledger", "Count": 7, "Amount": "2,219.61", "Percentage": 0 }, { "Currency Code": "EUR", "Ledger Name": "NSPL Primary Ledger", "Count": 58, "Amount": "22,26,406.54", "Percentage": 0.18 }, { "Currency Code": "GBP", "Ledger Name": "NSPL Primary Ledger", "Count": 10, "Amount": "36,89,790.77", "Percentage": 0.3 }, { "Currency Code": "HKD", "Ledger Name": "NSPL Primary Ledger", "Count": 9, "Amount": "62,86,578.10", "Percentage": 0.52 }, { "Currency Code": "INR", "Ledger Name": "NSPL Primary Ledger", "Count": 59, "Amount": "69,37,81,715.82", "Percentage": 56.89 }]
    };
    unReconAgingBucketData: any = [{ "Age": "0 to 30 days", "Count": 9, "Amount": "62,86,578.10", "Percentage": 8 }, { "Age": "31 to 60 days", "Count": 33, "Amount": "9,75,75,542.86", "Percentage": 15 }, { "Age": "61 to 90 days", "Count": 119, "Amount": "1,46,09,563.91", "Percentage": 18 }, { "Age": "above 90 days", "Count": 540, "Amount": "1,10,10,76,820", "Percentage": 59 }];
    topTenUnReconItemData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,73,56,578.97", "Percentage": 14.9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,48,78,278.59", "Percentage": 14.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,18,21,781.67", "Percentage": 13.2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "3,34,40,452.21", "Percentage": 10.5 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,93,171.42", "Percentage": 9.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,16,066.87", "Percentage": 9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,73,33,868.12", "Percentage": 8.6 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,34,94,099.10", "Percentage": 7.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,16,29,831.66", "Percentage": 6.8 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,03,62,004.36", "Percentage": 6.4 }];
    unReconItemByViolationData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 415, "Sum Of Amount": "4,18,21,781.67" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 416, "Sum Of Amount": "2,84,93,171.42" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 417, "Sum Of Amount": "4,56,443.36" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 418, "Sum Of Amount": "59,69,020.66" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 419, "Sum Of Amount": "1,62,67,594.22" }];
    awaitingForApprovalData: any = [{ "Ledger Name": "NSPL Primary Ledger", "Provider Name": "BitPay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "6,29,433.22" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "BitPay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "27,92,308" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Paytm", "Approval Status": "Awaiting for approval", "Sum Of Amount": "5,26,50,118.71" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "75,37,168.57" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "1,77,670.04" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": 148.18 }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "1,17,70,606.19" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "2,07,873.91" }];


    accountingStatusData: any = {
        "accounted": [{ "Accounting Rules": "Accounting for revenue booking", "Count Of Scr Ids": 44, "Amount": "45,62,871.63", "Percentage": 0.2 }, { "Accounting Rules": "Accounting for revenue – BRL", "Count Of Scr Ids": 109, "Amount": "38,29,46,929.92", "Percentage": 16.41 }, { "Accounting Rules": "Accounting for revenue – EUR", "Count Of Scr Ids": 300, "Amount": "7,53,06,730.73", "Percentage": 3.23 }, { "Accounting Rules": "Accounting for revenue – AUD", "Count Of Scr Ids": 96, "Amount": "15,65,19,996.38", "Percentage": 6.71 }, { "Accounting Rules": "Accounting for revenue – Others", "Count Of Scr Ids": "2,434", "Amount": "1,71,43,59,088.56", "Percentage": 73.45 }],
        "inProcess": [{ "Accounting Rules": "Accounting for revenue booking", "Count Of Scr Ids": 29, "Amount": "44,03,184.09", "Percentage": 0.19 }, { "Accounting Rules": "Accounting for revenue – BRL", "Count Of Scr Ids": 94, "Amount": "38,27,87,242.38", "Percentage": 16.41 }, { "Accounting Rules": "Accounting for revenue – EUR", "Count Of Scr Ids": 285, "Amount": "7,51,47,043.19", "Percentage": 3.22 }, { "Accounting Rules": "Accounting for revenue – AUD", "Count Of Scr Ids": 81, "Amount": "15,63,60,308.84", "Percentage": 6.7 }, { "Accounting Rules": "Accounting for revenue – Others", "Count Of Scr Ids": 2419, "Amount": "1,71,41,99,401.02", "Percentage": 73.48 }],
        "unAccounted": [{ "Accounting Rules": "Accounting for revenue booking", "Count Of Scr Ids": 34, "Amount": "45,40,168.65", "Percentage": 0.19 }, { "Accounting Rules": "Accounting for revenue – BRL", "Count Of Scr Ids": 99, "Amount": "38,29,24,226.94", "Percentage": 16.41 }, { "Accounting Rules": "Accounting for revenue – EUR", "Count Of Scr Ids": 290, "Amount": "7,52,84,027.75", "Percentage": 3.23 }, { "Accounting Rules": "Accounting for revenue – AUD", "Count Of Scr Ids": 86, "Amount": "15,64,97,293.40", "Percentage": 6.71 }, { "Accounting Rules": "Accounting for revenue – Others", "Count Of Scr Ids": 2424, "Amount": "1,71,43,36,385.58", "Percentage": 73.46 }]
    };

    activityBasedAcc: any = {
        "unIdentified": [{ "Accounting Rules": "Accounting for Un-Identified Rule 3", "Count Of Scr Ids": 6, "Amount": 284104.86, "Percentage": 100 }],
        "identified": [{ "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Cayan", "Date": "Jan 27,2017", "Currency Code": "CHF", "Description": "M Pay", "Acc No": 50003, "Amount": "10,274.77" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Cayan", "Date": "Dec 26,2017", "Currency Code": "CHF", "Description": "M Pay – 2909060", "Acc No": 50003, "Amount": "58,656.79" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Cayan", "Date": "Jan 28,2017", "Currency Code": "CHF", "Description": "M Pay – 2909060", "Acc No": 50003, "Amount": "41,508.92" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Cayan", "Date": "Jan 29,2017", "Currency Code": "CHF", "Description": "M Pay – 2909060", "Acc No": 50003, "Amount": "32,375.77" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Cayan", "Date": "Jan 26,2017", "Currency Code": "CHF", "Description": "M Pay – 2909060", "Acc No": 50003, "Amount": "27,818.34" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Cayan", "Date": "Jan 27,2017", "Currency Code": "CHF", "Description": "M Pay(TX142008)", "Acc No": 50003, "Amount": "1,13,470.27" }]
    }

    unAccountedAgingBucket: any = [{ "Age": "0 to 30 days", "Count": 9, "Amount": "62,86,578.10", "Percentage": 0.52 }, { "Age": "31 to 60 days", "Count": 33, "Amount": "9,75,75,542.86", "Percentage": 8 }, { "Age": "61 to 90 days", "Count": 119, "Amount": "1,46,09,563.91", "Percentage": 1.2 }, { "Age": "above 90 days", "Count": 540, "Amount": "1,10,10,76,820", "Percentage": 90.28 }];

    unAccountedAgingBucketData: any = {
        "0-30": { "totalAmount": "62,86,578.10", "totalPercentage": "100%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "28,31,893.05", "Percentage": 45.05 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "24,48,793.20", "Percentage": 38.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "4,48,574.50", "Percentage": 7.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "3,21,165.60", "Percentage": 5.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,25,487.15", "Percentage": 2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "52,474.65", "Percentage": 0.83 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "45,487.95", "Percentage": 0.72 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "12,154.50", "Percentage": 0.19 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": 547.5, "Percentage": 0.01 }] },
        "31-60": { "totalAmount": "3,25,25,180.95", "totalPercentage": "33.33%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "1,18,31,893.05", "Percentage": 12.13 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "83,56,593.65", "Percentage": 8.56 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "21,65,987.65", "Percentage": 2.22 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "19,88,956.40", "Percentage": 2.04 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "18,98,657.60", "Percentage": 1.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "17,98,654.00", "Percentage": 1.84 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "16,98,796.59", "Percentage": 1.74 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "13,65,698.56", "Percentage": 1.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "12,36,587.98", "Percentage": 1.27 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "1,83,355.47", "Percentage": 0.19 }] },
        "61-90": { "totalAmount": "1,19,14,392.60", "totalPercentage": "81.55%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "46,31,893.05", "Percentage": 31.7 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "39,56,593.65", "Percentage": 27.08 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "9,95,987.65", "Percentage": 6.82 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "7,88,956.40", "Percentage": 5.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "6,98,657.60", "Percentage": 4.78 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "5,65,865.65", "Percentage": 3.87 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "99,796.59", "Percentage": 0.68 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "76,698.56", "Percentage": 0.52 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "56,587.98", "Percentage": 0.39 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "43,355.47", "Percentage": 0.3 }] },
        ">90": { "totalAmount": "44,12,14,392.60", "totalPercentage": "40.07%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "19,78,31,893.05", "Percentage": 17.97 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "18,39,56,593.65", "Percentage": 16.71 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "1,49,95,987.65", "Percentage": 1.36 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "1,37,88,956.40", "Percentage": 1.25 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,36,98,657.60", "Percentage": 1.24 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "1,25,65,865.65", "Percentage": 1.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "11,99,796.59", "Percentage": 0.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "10,76,698.56", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "10,56,587.98", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "10,43,355.47", "Percentage": 0.09 }] }
    };

    unAccountedAgingWiseData: any = {
        "0-30": { "totalAmount": "62,86,578.10", "totalPercentage": "100%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "28,31,893.05", "Percentage": 45.05 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "24,48,793.20", "Percentage": 38.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "4,48,574.50", "Percentage": 7.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "3,21,165.60", "Percentage": 5.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,25,487.15", "Percentage": 2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "52,474.65", "Percentage": 0.83 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "45,487.95", "Percentage": 0.72 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "12,154.50", "Percentage": 0.19 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": 547.5, "Percentage": 0.01 }] },
        "31-60": { "totalAmount": "3,25,25,180.95", "totalPercentage": "33.33%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "1,18,31,893.05", "Percentage": 12.13 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "83,56,593.65", "Percentage": 8.56 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "21,65,987.65", "Percentage": 2.22 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "19,88,956.40", "Percentage": 2.04 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "18,98,657.60", "Percentage": 1.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "17,98,654.00", "Percentage": 1.84 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "16,98,796.59", "Percentage": 1.74 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "13,65,698.56", "Percentage": 1.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "12,36,587.98", "Percentage": 1.27 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "1,83,355.47", "Percentage": 0.19 }] },
        "61-90": { "totalAmount": "1,19,14,392.60", "totalPercentage": "81.55%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "46,31,893.05", "Percentage": 31.7 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "39,56,593.65", "Percentage": 27.08 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "9,95,987.65", "Percentage": 6.82 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "7,88,956.40", "Percentage": 5.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "6,98,657.60", "Percentage": 4.78 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "5,65,865.65", "Percentage": 3.87 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "99,796.59", "Percentage": 0.68 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "76,698.56", "Percentage": 0.52 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "56,587.98", "Percentage": 0.39 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "43,355.47", "Percentage": 0.3 }] },
        ">90": { "totalAmount": "44,12,14,392.60", "totalPercentage": "40.07%", "data": [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "19,78,31,893.05", "Percentage": 17.97 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "18,39,56,593.65", "Percentage": 16.71 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "1,49,95,987.65", "Percentage": 1.36 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "1,37,88,956.40", "Percentage": 1.25 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,36,98,657.60", "Percentage": 1.24 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "1,25,65,865.65", "Percentage": 1.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "11,99,796.59", "Percentage": 0.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "10,76,698.56", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "10,56,587.98", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "10,43,355.47", "Percentage": 0.09 }] }
    };

    selectedViewtra: any = "Transactions";
    /* ACCOUNTING */
    showAccountingStatus: boolean = false;
    /* reconColor: any = '#8ac9f3';
    unReconColor: any = '#fda4b8';
    reconColorTemp: any = '#fda4b8';
    unReconColorTemp: any = '#8ac9f3';
    tempSelectedPostion: any = 'bottom';
    selectedPostion: any = 'bottom';
    reconBackgroundTemp: any = '#ffffff';
    reconBackground: any = '#ffffff'; */

    public pieChartAccounting: any[] = [];
    public pieChartAccountingType: string = 'bar';
    public temppieChartAccountingType: string = 'bar';
    public pieChartAccountingLegend: boolean = true;
    public pieChartAccountingLabels: any = [];/* 'Not Accounted', 'Pending Journals', 'JE Creation' */
    public pieChartAccountingOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    let labelName;
                    if (data.labels[tooltipItems.index] == 'JE,Created') {
                        labelName = 'JE Created';
                    } else if (data.labels[tooltipItems.index] == 'JE,Pending') {
                        labelName = 'JE Pending';
                    } else {
                        labelName = 'Un-Accounted';
                    }
                    return labelName + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        },
        legend: {
            position: 'bottom',
            display: false
        },
        scales: {
            xAxes: [{
                stacked: false,
                gridLines: {
                    display: false
                },
                //display: false
            }],
            yAxes: [{
                stacked: false,
                ticks: {
                    min: 0,
                    max: 100,
                    callback: function (value) { return value + "%" },
                },
                gridLines: {
                    display: false
                }
            }]
        },
        plugins: {
            datalabels: {
                display: true,
                color: 'black',
                font: {
                    weight: 'bold'
                },
                align: function (context) {
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    var invert = Math.abs(value) <= 20;
                    return value < 20 ? 'top' : 'center'
                },
                formatter: function (value, context) {
                    return value + '%';
                }
            }
        }
    };
    public pieChartAccountingColors: {}[] = [{ backgroundColor: ['rgba(234, 67, 53, 0.9)', 'rgba(251, 188, 5, 0.9)', 'rgba(52, 168, 83, 0.9)'] }];

    /* ACTIVITY ACCOUNTING STATUS */
    showActivityAccountingStatus: boolean = false;
    showActivityAccounting: boolean = false;
    /* reconColor: any = '#8ac9f3';
    unReconColor: any = '#fda4b8';
    reconColorTemp: any = '#fda4b8';
    unReconColorTemp: any = '#8ac9f3';
    tempSelectedPostion: any = 'bottom';
    selectedPostion: any = 'bottom';
    reconBackgroundTemp: any = '#ffffff';
    reconBackground: any = '#ffffff'; */

    public pieChartActivityAccounting: any[] = [];
    public pieChartActivityAccountingTrg: any[] = [];
    public pieChartActivityAccountingType: string = 'pie';
    public pieChartActivityAccountingTypeTrg: string = 'pie';
    public temppieChartActivityAccountingType: string = 'doughnut';
    public pieChartActivityAccountingLegend: boolean = true;
    public pieChartActivityAccountingLabels: string[] = [];
    public pieChartActivityAccountingLabelsTrg: string[] = [];
    public pieChartActivityAccountingOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.labels[tooltipItems.index] + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        },
        legend: {
            position: 'bottom',
            display: false
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
    public pieChartGrpCurrencyAccounting: any[] = [];
    public pieChartGrpCurrencyAccountingType: string = 'pie';
    public temppieChartGrpCurrencyAccountingType: string = 'doughnut';
    public pieChartGrpCurrencyAccountingLegend: boolean = true;
    public pieChartGrpCurrencyAccountingLabels: string[] = [];
    public pieChartGrpCurrencyAccountingOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.labels[tooltipItems.index] + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        },
        legend: {
            position: 'bottom',
            display: false
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

    public unAccountedAgingBucketColors: {}[] = [{ backgroundColor: [this.agingColor, this.agingColor, this.agingColor, this.agingColor, this.agingColor] }];
    public pieChartActivityColors: {}[] = this.commonService.randomColors;
    reconDrillDownData: any = [];
    accDrillDownData: any = [];
    reconPeriodAnalysisType: any = 'None';
    accPeriodAnalysisType: any = 'None';
    reconDateRanges: any = [];
    accDateRanges: any = [];
    showReconDrillDownData: boolean = false;
    showReconDrillDownDataTrg: boolean = false;
    /* Reconciliation Chart Variables */
    public reconChartData: Array<any> = [];
    public reconChartLabels: Array<any> = [];
    public reconChartDataTrg: Array<any> = [];
    public reconChartLabelsTrg: Array<any> = [];
    public reconChartType: string = 'bar';
    public reconChartTypeTrg: string = 'bar';
    public reconChartLegend: boolean = true;
    public lineChartLegend: boolean = true;
    public reconChartOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + ' %';/*  + finalinput + ' )' */
                }
            }
        },
        scales: {
            xAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    /* fontColor: this.setThemeColor */
                }
            }],
            yAxes: [
                {
                    ticks: {
                        min: 0,
                        max: 100,
                        callback: function (value) { return value + "%" },
                        /* fontColor: this.setThemeColor */
                    },
                    gridLines: {
                        display: false
                    }
                }
            ]
        },
        legend: {
            position: 'bottom',
            display: false
            /*  labels: {
                 fontColor: this.setThemeColor
             } */
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
                align: function (context) {
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    var invert = Math.abs(value) <= 20;
                    return value < 20 ? 'top' : 'center'
                },
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && !(context.dataset.hidden));/* && context.active */
                },
                /* anchor: 'end',
                align: 'top', */
                borderWidth: 1,
                borderRadius: 4,
                formatter: function (value, context) {
                    return value + '%';
                },
                color: 'black',
                /* color: function (context) {
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :  // draw negative values in red
                        context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green

                            'rgba(52, 168, 83, 0.9)';

                } */
            }
        }
    };
    //  reconColor: any = '#43A047';/* #8ac9f3 */
    //unReconColor: any = '#f86f00';/* #fda4b8 */
    public reconChartColors: Array<any> = [
        { backgroundColor: '#f86f00', },
        { backgroundColor: '#43A047', }
    ];

    /* accounting Chart Variables */
    public accountingChartData: Array<any> = [];
    public accountingChartLabels: Array<any> = [];
    public accountingChartType: string = 'bar';
    public accountingChartLegend: boolean = true;
    public accountingChartOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index] + ' %';/*  + finalinput + ' )' */
                }
            }
        },
        scales: {
            xAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    /* fontColor: this.setThemeColor */
                },
            }],
            yAxes: [
                {
                    ticks: {
                        min: 0,
                        max: 100,
                        callback: function (value) { return value + "%" },
                        /* fontColor: this.setThemeColor */
                    },
                    gridLines: {
                        display: false
                    }
                }
            ]
        },
        legend: {
            position: 'bottom',
            display: false
            /* labels: {
                fontColor: this.setThemeColor
            } */
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
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && !(context.dataset.hidden));//&& context.active
                },
                align: function (context) {
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    var invert = Math.abs(value) <= 20;
                    return value < 20 ? 'top' : 'center'
                },
                /* borderWidth: 1,
                borderRadius: 4, */
                formatter: function (value, context) {
                    return value + '%';
                },
                color: 'black',
                font: {
                    weight: 'bold'
                },
                /* color: function (context) {
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :  // draw negative values in red
                        context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green

                            'rgba(52, 168, 83, 0.9)';

                } */
            }
        }
    };
    public accountingChartColors: Array<any> = [
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

    showReconDefaultData: boolean = false;
    showReconDefaultDataTrg: boolean = false;
    constructor(
        private commonService: CommonService,
        private config: NgbDatepickerConfig,
        private $sessionStorage: SessionStorageService,
        private reconcileService: ReconcileService,
        private ns: NotificationsService,
        private dashboardService: DashboardService,
        private tenantService: TenantDetailsService,
        private router: Router,
    ) {
        $('.spinner, .clockwise').addClass('hidden');
        this.randomChartColors = this.commonService.randomColors;
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.getprvsmonthslist();
        let stDt = new Date(moment().startOf('month'));
        let edDt = new Date(moment());
        stDt = this.commonService.convertDate(stDt);
        edDt = this.commonService.convertDate(edDt);
        this.reconDateRange = {
            "startDate": stDt,
            "endDate": edDt
        };
        this.reconDateRange = {
            "startDate": stDt,
            "endDate": edDt
        };
    }
    private onError(errorMessage) {
        this.commonService.error(
            'Error!',
            errorMessage/* ,
            {
                timeOut: 3000,
                showProgressBar: false,
                pauseOnHover: true,
                clickToClose: true,
                maxLength: 100
            } */
        )
    }

    changeMinDate() {
        this.config.minDate = this.processFilter.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    getmindate() {
        this.minDate = this.commonService.convertDate(this.reconDateRange.startDate);
    }
    reconRuleGrpId: any;
    reconRuleGrpIdEncrypt: any;
    accRuleGrpIdEncrypt: any;
    ngOnInit() {
        this.maxDate = new Date();
        this.minDate = this.commonService.convertDate(this.reconDateRange.startDate);

        this.getconfiguredmodules();
        this.bucketList = [];
        this.request = this.dashboardService.fetchBucketList('AGE').subscribe(
            (res: any) => {
                this.bucketList = res;
                this.activeItem2 = this.bucketList[0];
                this.activeItem3 = this.bucketList[0];
                this.activeAccAging = this.bucketList[0];
                this.getAllData();
            })
        //this.getBucketList();

        $(".dashboardV5").css('min-height', (this.commonService.screensize().height - 161) + 'px');
        if (this.$sessionStorage.retrieve('userData')) {
            this.userDetails = this.$sessionStorage.retrieve('userData');
        }
        /* if(this.testLabels.length>0){
            this.testshow = true;
        } */
        this.processesList = [{ "id": 1, "processName": "Test Process" }, { "id": 2, "processName": "Test Process 2" }, { "id": 3, "processName": "Test Process 3" }, { "id": 4, "processName": "Test Process 4" }, { "id": 5, "processName": "Test Process 5" }, { "id": 6, "processName": "Test Process 6" }, { "id": 7, "processName": "Test Process 7" }, { "id": 8, "processName": "Test Process 8" }];
        if (this.processesList.length > 0) {
            this.selectedProcess = this.processesList[0].id;
            this.activeItem = this.processesList[0];
            this.selectedProcessName = this.processesList[0].processName;
            //console.log('processesList ' + JSON.stringify(this.processesList));
        }
        /* this.navbarService.getprocessessList(this.userDetails).subscribe((res: any) => {
            this.processesList = res;
            this.selectedProcess = this.processesList[0].id;
            this.activeItem = this.processesList[0];
            this.selectedProcessName = this.processesList[0].processName;
            //console.log('processesList ' + JSON.stringify(this.processesList));
        }); */
        //console.log('reconStatusTest ::' + $("#reconStatusTest").width());
        //console.log('reconAgingTest ::' + $("#reconAgingTest").width());
        /* this.testReconWidth = $("#reconStatusTest").width();
        this.testReconHeight = $("#reconStatusTest").height(); */

        /*  if (this.pieChartAccounting.length > 0) {
             this.showAccountingStatus = true;
         }
         if (this.pieChartActivityAccounting.length > 0) {
             this.showActivityAccountingStatus = true;
         } */
    }
    activeItem2: any;
    activeItem3: any;
    activeAccAging: any;
    bucketList: any = [];
    /* getBucketList() {
        this.bucketList = [];
        this.dashboardService.fetchBucketList().subscribe(
            (res: any) => {
                this.bucketList = res;
                this.activeItem2 = this.bucketList[0];
                this.activeAccAging = this.bucketList[0];
                this.getReconAgingBucket(this.bucketList[0].id);
                this.getAccAgingBucket(this.bucketList[0].id);
            })
    } */
    /* fetchAgingBucket(col) {
        
        //console.log('fetchAgingBucket ' + JSON.stringify(col));
    } */
    accRuleGrpId: any;
    reconAgingBucketList: any = [];
    agingBy: any = 'Count';
    reconAgingChart: boolean = false;
    reconBucketId: any;
    reconBucketIdTrg: any;
    agingLov: any = [];
    accAgingLov: any = [];
    /* Function to fetch recon aging bucket analysis */
    getReconAgingBucket(bucketId) {
        this.agingLov = [];
        this.reconBucketId = bucketId;
        $('.spinnerBlock').addClass('spinnerBlock-icon');
        this.requestAging = this.dashboardService.fetchAgingAnalysis(this.reconRuleGrpId, bucketId, 'unreconciled', this.reconDateRange, this.reconSelectedViewId, this.reconSelectedType).subscribe(
            (res: any) => {
                this.reconAgingBucketList = res;
                let agingData = [];
                let tempLab = [];
                let tempLov = [];
                if (this.reconAgingBucketList.length) {
                    this.reconAgingBucketList.forEach(element => {
                        let tempName = {
                            "name": element.age
                        }
                        tempLab.push(element.age);
                        tempLov.push(tempName);
                        if (this.agingBy == 'Amount') {
                            agingData.push(element.amount);
                        } else {
                            agingData.push(element.count);
                        }
                    });
                    setTimeout(() => {
                        this.barChartLabels = tempLab;
                        let tempName = {
                            "name": "All"
                        }
                        tempLov.push(tempName);
                        tempLov[0]["checked"] = true;
                        this.agingLov = tempLov;
                        /* console.log('this.agingLov ' + JSON.stringify(this.agingLov)); */
                    }, 0);
                    this.barChartData = [
                        { data: agingData, label: 'Un-Recon' }
                    ];
                    setTimeout(() => {
                        if (this.barChartData && this.barChartData[0].data.length) {
                            this.reconAgingChart = true;
                            $('.spinnerBlock').removeClass('spinnerBlock-icon');
                        } else {
                            $('.spinnerBlock').removeClass('spinnerBlock-icon');
                        }
                    }, 0);
                }
            },
            (res: Response) => {
                this.onError(res.json())
                $('.spinnerBlock').removeClass('spinnerBlock-icon');
                this.commonService.error('Error!', 'Something went wrong while fetching reconciliation aging analysis');
            });
    }

    accAgingBucketList: any = [];
    accAgingChart: boolean = false;
    accAgingBy: any = 'Count';
    accBucketId: any;
    requestAging: any;
    /* Function to fetch recon aging bucket analysis */
    getAccAgingBucket(bucketId) {
        this.accBucketId = bucketId;
        this.accAgingLov = [];
        $('.spinnerBlock').addClass('spinnerBlock-icon');
        this.requestAging = this.dashboardService.fetchAgingAnalysis(this.accRuleGrpId, bucketId, 'unaccounted', this.reconDateRange, this.accSelectedViewId).subscribe(
            (res: any) => {
                this.accAgingBucketList = res;
                let agingData = [];
                let tempLab = [];
                let tempLov = [];
                if (this.accAgingBucketList.length) {
                    this.accAgingBucketList.forEach(element => {
                        let tempName = {
                            "name": element.age
                        }
                        tempLab.push(element.age);
                        tempLov.push(tempName);
                        if (this.accAgingBy == 'Amount') {
                            agingData.push(element.amount);
                        } else {
                            agingData.push(element.count);
                        }
                    });
                    setTimeout(() => {
                        let tempName = {
                            "name": "All"
                        }
                        tempLov.push(tempName);
                        tempLov[0]["checked"] = true;
                        this.accAgingLov = tempLov;
                        this.accAgingChartLabels = tempLab;
                    }, 0);
                    this.accAgingChartData = [
                        { data: agingData, label: 'Un-Acc' }
                    ];
                    setTimeout(() => {
                        if (this.accAgingChartData && this.accAgingChartData[0].data.length) {
                            this.accAgingChart = true;
                            $('.spinnerBlock').removeClass('spinnerBlock-icon');
                        } else {
                            $('.spinnerBlock').removeClass('spinnerBlock-icon');
                        }
                    }, 0);
                }
            },
            (res: Response) => {
                this.onError(res.json())
                $('.spinnerBlock').removeClass('spinnerBlock-icon');
                this.commonService.error('Error!', 'Something went wrong while fetching accounting aging analysis');
            });
    }
    /*  Function : 1
        Function to fetch Recon & Acc rule group list data
    */
    request1: any;
    getAllData() {
        /* Recon */
        this.request = this.dashboardService.reconAllDataList(this.reconDateRange).subscribe(
            (res: any) => {
                this.reconAllData = res;
                if (this.reconAllData.length > 0) {
                    // this.getBucketList();
                    this.reconRuleGrpId = this.reconAllData[0].ruleGroupId;
                    this.reconRuleGrpIdEncrypt = this.reconAllData[0].ruleGroupIdForDisplay;
                    this.displayReconStatus(0);
                    //this.fetchReconKPI();
                } else {
                    //this.commonService.info('Info!', 'No Active Rule Groups Found For Reconciliation!')
                }
            },
            (res: Response) => {
                this.onError('Error Occured while fetching Reconciliation Data !')
            }
        );

        /* Accounting */
        this.request = this.dashboardService.accountingAllDataList(this.reconDateRange).subscribe(
            (res: any) => {
                this.accountingAllData = res;
                if (this.accountingAllData.length > 0) {
                    this.accRuleGrpId = this.accountingAllData[0].ruleGroupId;
                    this.accRuleGrpIdEncrypt = this.accountingAllData[0].ruleGroupIdForDisplay;
                    this.displayAccountingStatus(0);
                    /* if (this.bucketList.length) {
                        this.getAccAgingBucket(this.bucketList[0].id);
                    } */
                } else {
                    //this.commonService.info('Info!', 'No Active Rule Groups Found For Accounting!')
                }
            },
            (res: Response) => {
                this.onError('Error Occured while fetching Accounting Data !')
            }
        );
    }

    /*  Function : 2
        Function to display recon status chart based on selected rule group
    */
    reconSelViewIdDisplay: any;
    reconSelViewType: any;
    reconSelViewName: any;
    reconSelType: any;
    onloadTemp: boolean = false;
    displayReconStatus(ind) {
        this.selectedReconAnalysis = 0;
        this.toggleReconView = true;
        this.showReconDrillDownData = false;
        this.reconRuleGrpId = this.reconAllData[ind].ruleGroupId;
        this.reconRuleGrpIdEncrypt = this.reconAllData[ind].ruleGroupIdForDisplay;
        this.loadingTab = true;
        this.request = this.dashboardService.fetchReconciliationCombinationViewSpecific(this.reconRuleGrpIdEncrypt, 'Reconciled', this.reconDateRange).subscribe((res: any) => {
            this.reconViewCombinationSpecificAnalysis = res;
            if (this.reconViewCombinationSpecificAnalysis.length) {
                this.fetchReconKPIAndDetails(this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewId, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewIdDisplay,
                    this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewType, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewName, 'Un-Reconciled');
                /* this.fetchReconKPIAndDetailsTrg(this.reconViewCombinationSpecificAnalysis[0]['combination'][1].viewId, this.reconViewCombinationSpecificAnalysis[0]['combination'][1].viewIdDisplay,
                    this.reconViewCombinationSpecificAnalysis[0]['combination'][1].viewType, this.reconViewCombinationSpecificAnalysis[0]['combination'][1].viewName, 'Reconciled'); */
                this.loadingTab = false;
            } else {
                this.loadingTab = false;
            }
        });
    }
    loadingKPIGrp: boolean = false;
    changeReconViewTab(e?) {
        if(e){
            this.selectedReconAnalysis = e.index
        }
        /* console.log('ind ' + this.selectedReconAnalysis); */
       // let tempInd = this.selectedReconAnalysis;
       // let tempIndValue = parseInt(tempInd) + 1;
        /* console.log('selectedReconAnalysis ' + tempIndValue);
        console.log('this.changeReconViewOrder ' + this.changeReconViewOrder);
        console.log('changed ::' + JSON.stringify(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis])); */
        if (!this.changeReconViewOrder) {
            this.toggleReconView = true;
            $('.srcReconOrder_' + this.selectedReconAnalysis).css('order', 0);
            $('.trgReconOrder_' + this.selectedReconAnalysis).css('order', 3);
            this.fetchReconKPIAndDetails(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[0].viewId, this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[0].DisplayviewType,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[0].viewType, this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[0].viewName, 'Un-Reconciled')
        } else {
            this.toggleReconView = false;
            $('.srcReconOrder_' + this.selectedReconAnalysis).css('order', 3);
            $('.trgReconOrder_' + this.selectedReconAnalysis).css('order', 0);
            this.fetchReconKPIAndDetails(this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[1].viewId, this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[1].DisplayviewType,
                this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[1].viewType, this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis].combination[1].viewName, 'Un-Reconciled')
        }
    }
    changeReconViewOrder: boolean = false;
    fetchReconKPIAndDetails(viewId, viewIdDisplay, viewType, viewName, selType) {
        this.loadingKPIGrp = true;
        this.reconDateRanges = [];
        this.selectedTab = 0;
        this.subProcessAnalysisHeading = "Reconciliation Status";
        this.reconPeriodAnalysisType = 'None';
        this.reconSelectedViewId = viewId;
        this.reconSelViewIdDisplay = viewIdDisplay;
        this.reconSelViewType = viewType;
        this.reconSelectedType = viewType;
        this.reconSelViewName = viewName;
        this.reconSelType = selType;
        this.reconDrillDownData = [];
        this.showReconApprovalPer = false;
        this.fetchReconDrillDown();
        if (this.bucketList.length) {
            this.getReconAgingBucket(this.bucketList[0].id);
        }
        this.fetchReconKPI();
        this.getGrpByDetails();
    }



    /*  Function : 3
        Function to display accounting status chart based on selected rule group
    */
    accSelViewIdDisplay: any;
    accSelViewType: any;
    accSelViewName: any;
    accSelType: any;
    requestAccKPI: any;
    displayAccountingStatus(ind) {
        this.selectedAccAnalysis = 0;
        this.showAccountingDrillDownData = false;
        this.accPeriodAnalysisType = 'None';
        this.accRuleGrpId = this.accountingAllData[ind].ruleGroupId;
        this.accRuleGrpIdEncrypt = this.accountingAllData[ind].ruleGroupIdForDisplay;
        this.loadingTab = true;
        this.request = this.dashboardService.fetchAccViewObj(this.accRuleGrpIdEncrypt, 'JE Created', this.reconDateRange).subscribe((res: any) => {
            this.accViewCombinationSpecificAnalysis = res;
            //console.log('this.accViewCombinationSpecificAnalysis ' + JSON.stringify(this.accViewCombinationSpecificAnalysis));
            if (this.accViewCombinationSpecificAnalysis.length) {
                this.fetchAccKPIAndDetails(this.accViewCombinationSpecificAnalysis[0].viewId, this.accViewCombinationSpecificAnalysis[0].viewIdDisplay,
                    this.accViewCombinationSpecificAnalysis[0].viewType, this.accViewCombinationSpecificAnalysis[0].viewName, 'Un-Accounted');
                this.loadingTab = false;
            } else {
                this.loadingTab = false;
            }
        });
    }
    selectedAccAnalysis: any = 0;
    changeAccSubProcess(e) {
        this.selectedAccAnalysis = e.index;
        //console.log('index ' + e.index);
        this.fetchAccKPIAndDetails(this.accViewCombinationSpecificAnalysis[e.index].viewId, this.accViewCombinationSpecificAnalysis[e.index].DisplayviewType,
            this.accViewCombinationSpecificAnalysis[e.index].viewType, this.accViewCombinationSpecificAnalysis[e.index].viewName, 'Un-Accounted');
    }
    fetchAccKPIAndDetails(viewId, viewIdDisplay, viewType, viewName, selType) {
        this.accDateRanges = [];
        this.selectedAccTabKPI = 0;
        this.subProcessAnalysisHeadingAcc = "Accounting Status";
        this.accPeriodAnalysisType = 'None';
        this.accSelectedViewId = viewId;
        this.accSelViewIdDisplay = viewIdDisplay;
        this.accSelViewType = viewType;
        this.accSelectedType = viewType;
        this.accSelViewName = viewName;
        this.accSelType = selType;
        this.accDrillDownData = [];
        this.showAccApprovalPer = false;
        this.fetchAccountingDrillDown();
        if (this.bucketList.length && this.accSelectedViewId) {
            this.getAccAgingBucket(this.bucketList[0].id);
        }
        this.fetchAccKPI();
        this.accGetGrpByDetails();
    }
    tempReconLabName: any;
    fetchReconciliationStatus(e) {
        //console.log(e);
        //console.log('this.selectedGrpByTab ' + this.selectedGrpByTab);
        if (e.active[0] != undefined) {
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            this.tempReconLabName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (labelName == 'Reconciled') {
                this.reconStatusTab = 1;
                this.selectedTab = 1;
                //  this.fetchGrpByData('Reconciled');
                this.selectedGrpByTab = 'Reconciled';
                //console.log('this.selectedGrpByTab ' + this.selectedGrpByTab);
            } else if (labelName == 'Un-Reconciled') {
                this.reconStatusTab = 0;
                this.selectedTab = 0;
                //  this.fetchGrpByData('Un-Reconciled');
                this.selectedGrpByTab = 'Un-Reconciled';
                //console.log('this.selectedGrpByTab ' + this.selectedGrpByTab);
            } else {

            }
            this.fetchReconDrillDown();
        }
    }



    fetchReconDrillDown(back?) {
        let dateRange: any = null;
        //console.log('this.reconDateRanges ' + JSON.stringify(this.reconDateRanges));
        //console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
        //console.log('this.reconDrillDownData ' + JSON.stringify(this.reconDrillDownData));
        //console.log('this.selectedTab ' + this.selectedTab);
        //console.log('this.tempReconLabName ' + this.tempReconLabName);
        if (!back) {
            if (this.reconDrillDownData.length && this.reconDrillDownData[this.selectedTab] && this.reconDrillDownData[this.selectedTab]['dateRange']) {
                this.showReconDefaultData = false;
                //console.log('this.reconDrillDownData ' + JSON.stringify(this.reconDrillDownData));
                this.reconDrillDownData.forEach(element => {
                    if (this.tempReconLabName == element.labelValue) {
                        dateRange = {
                            "startDate": new Date(element.dateRange.startDate),
                            "endDate": new Date(element.dateRange.endDate),
                        };
                    }
                });

                if (this.reconPeriodAnalysisType == 'Months') {
                    //this.reconPeriodAnalysisType = 'None';
                    this.reconDateRanges.push(dateRange);
                } else if (this.reconPeriodAnalysisType == 'Weeks') {
                    //this.reconPeriodAnalysisType = 'Months';
                    this.reconDateRanges.push(dateRange);
                } else {
                    //this.reconPeriodAnalysisType = 'Weeks';
                    dateRange = dateRange;
                }
                this.fetchReconDetails(dateRange);
            } else {
                //dateRange = this.reconDateRange;
                this.reconDateRanges.push(this.reconDateRange);
                this.fetchReconDetails(this.reconDateRange);
            }
        } else {
            //console.log(this.reconDateRanges);
            if (this.reconPeriodAnalysisType == 'Months') {
                this.reconPeriodAnalysisType = 'None';
            } else if (this.reconPeriodAnalysisType == 'Weeks') {
                this.reconPeriodAnalysisType = 'None';
            } else {
                this.reconPeriodAnalysisType = 'Months';
            }
            if (this.reconDateRanges.length > 1) {
                //console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
                this.reconDateRanges.splice(this.reconDateRanges.length - 1, 1);
                dateRange = this.reconDateRanges[this.reconDateRanges.length - 1];
                this.fetchReconDetails(dateRange);
            } else {
                //console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
                this.fetchReconDetails();
                this.showReconDrillDownData = false;
            }

        }
        //console.log('this.reconDateRanges ' + JSON.stringify(this.reconDateRanges));

    }

    fetchReconDetails(dateRange?) {
        if (!dateRange) {
            dateRange = this.reconDateRange;
        }
        if (this.reconPeriodAnalysisType != 'Days') {
            //console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
            let tempReconData = [];
            let tempUnReconData = [];
            let tempReconLabels = [];
            this.request = this.dashboardService.fetchReconciliationForGivenPeriodBasedOnRuleGrpId(this.reconPeriodAnalysisType, this.reconRuleGrpId, this.reconSelectedViewId, this.reconSelectedType, dateRange).subscribe((res: any) => {
                this.reconDrillDownData = res.json();
                this.reconPeriodAnalysisType = res.headers.get('reconPeriodAnalysisType');
                //console.log('this.reconPeriodAnalysisType ' + this.reconPeriodAnalysisType);
                this.reconDrillDownData.forEach(element => {
                    tempReconLabels.push(element.labelValue);
                    tempReconData.push(element.recon.countPer);
                    tempUnReconData.push(element.unRecon.countPer);
                });
                this.reconChartData = [
                    { data: tempUnReconData, label: 'Un-Reconciled' },
                    { data: tempReconData, label: 'Reconciled' }
                ];
                setTimeout(() => { this.reconChartLabels = tempReconLabels }, 0);
                setTimeout(() => {
                    if (this.reconChartData[0].data.length > 0 && this.reconChartLabels && this.reconChartLabels.length > 0) {
                        this.showReconDrillDownData = true;
                    }
                }, 0);
            });
        }
    }

    tempAccLabName: any;
    fetchAccountingStatus(e) {
        if (e.active[0] != undefined) {
            //console.log('labelName ' + e.active[0]._chart.config.data.labels[e.active[0]._index]);
            this.tempAccLabName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            //console.log('this.tempAccLabName ' + this.tempAccLabName);
            if (this.tempAccLabName == 'Accounted') {
                this.accStatusTab = 2;
                this.selectedAccTab = 2;
            } else if (this.tempAccLabName == 'In Process') {
                this.accStatusTab = 1;
                this.selectedAccTab = 1;
            } else if (this.tempAccLabName == 'Un-Accounted') {
                this.accStatusTab = 0;
                this.selectedAccTab = 0;
            }
            this.fetchAccountingDrillDown();
        }
    }

    fetchAccountingDrillDown(back?) {
        let dateRange: any = null;
        //console.log('this.accDateRanges ' + JSON.stringify(this.accDateRanges));
        //console.log('this.accPeriodAnalysisType ' + this.accPeriodAnalysisType);
        //console.log('this.accDrillDownData ' + JSON.stringify(this.accDrillDownData));
        //console.log('this.selectedTab ' + this.selectedTab);
        //console.log('this.tempAccLabName ' + this.tempAccLabName);
        if (!back) {
            //console.log('this.accDrillDownData ' + JSON.stringify(this.accDrillDownData));
            //console.log('this.selectedAccTab ' + this.selectedAccTab);
            if (this.accDrillDownData.length && this.accDrillDownData[this.selectedAccTab] && this.accDrillDownData[this.selectedAccTab]['dateRange']) {
                this.accDrillDownData.forEach(element => {
                    if (this.tempAccLabName == element.labelValue) {
                        dateRange = {
                            "startDate": new Date(element.dateRange.startDate),
                            "endDate": new Date(element.dateRange.endDate),
                        };
                    }
                });
                /*  dateRange = {
                     "startDate": new Date(this.accDrillDownData[this.selectedAccTab]['dateRange']['startDate']),
                     "endDate": new Date(this.accDrillDownData[this.selectedAccTab]['dateRange']['endDate']),
                 }; */
                if (this.accPeriodAnalysisType == 'Months') {
                    this.accDateRanges.push(dateRange);
                } else if (this.accPeriodAnalysisType == 'Weeks') {
                    this.accDateRanges.push(dateRange);
                } else {
                    dateRange = dateRange;
                }
                this.fetchAccDetails(dateRange);
            } else {
                //dateRange = this.reconDateRange;
                this.accDateRanges.push(this.reconDateRange);
                this.fetchAccDetails(this.reconDateRange);
            }
        } else {
            //console.log(this.accDateRanges);
            if (this.accPeriodAnalysisType == 'Months') {
                this.accPeriodAnalysisType = 'None';
            } else if (this.accPeriodAnalysisType == 'Weeks') {
                this.accPeriodAnalysisType = 'None';
            } else {
                this.accPeriodAnalysisType = 'Months';
            }
            if (this.accDateRanges.length > 1) {
                this.accDateRanges.splice(this.accDateRanges.length - 1, 1);
                dateRange = this.accDateRanges[this.accDateRanges.length - 1];
                this.fetchAccDetails(dateRange);
            } else {
                this.fetchAccDetails();
                this.showAccountingDrillDownData = false;
            }

        }
        //console.log('this.accDateRanges ' + JSON.stringify(this.accDateRanges));
    }

    fetchAccDetails(dateRange?) {
        if (!dateRange) {
            dateRange = this.reconDateRange;
        }
        if (this.accPeriodAnalysisType != 'Days') {
            let tempNotAccData = [];
            let tempJEPenData = [];
            let tempJECreData = [];
            let tempAccLabels = [];
            this.request = this.dashboardService.fetchAccountingForGivenPeriodBasedOnRuleGrpId(this.accPeriodAnalysisType, this.accRuleGrpId, this.accSelectedViewId, dateRange).subscribe((res: any) => {
                this.accDrillDownData = res.json();
                this.accPeriodAnalysisType = res.headers.get('accountingperiodanalysistype');
                //console.log('this.accPeriodAnalysisType ' + this.accPeriodAnalysisType);

                this.accDrillDownData.forEach(element => {
                    tempAccLabels.push(element.labelValue);
                    tempNotAccData.push(element.notAccounted.countPer);
                    tempJEPenData.push(element.accounted.countPer);
                    tempJECreData.push(element.finalAccounted.countPer);
                });
                this.accountingChartData = [
                    { data: tempNotAccData, label: 'Un-Accounted' },
                    { data: tempJEPenData, label: 'JE Pending' },
                    { data: tempJECreData, label: 'JE Created' }
                ];
                setTimeout(() => { this.accountingChartLabels = tempAccLabels }, 0);
                setTimeout(() => {
                    if (this.accountingChartData[0].data.length > 0 && this.accountingChartLabels && this.accountingChartLabels.length > 0) {
                        this.showAccountingDrillDownData = true;
                    }
                }, 0);
            });
        }
    }


    /* Function to fetch recon kpi details */
    /* Recon detail data*/
    unReconCountTitle: any;
    topTenUnReconCount: any;
    requestReconKPI: any;
    reconViolationPeriod: any = 2;
    fetchReconKPI() {
        this.requestReconKPI = this.dashboardService.fetchReconSpecificbyRgIdViewId(this.reconRuleGrpId, this.reconViolationPeriod, this.reconSelectedViewId, this.reconSelectedType, this.reconDateRange).subscribe(
            (res: any) => {
                let decimal = 2;
                let currency = '';
                this.recondetaildata = res;
                this.topTenUnReconValue = parseInt(this.recondetaildata.unReconItemsValuePer);
                this.topTenUnReconCount = parseInt(this.recondetaildata.unReconItemsCountPer);
                //  this.topTenUnReconValueTitle = this.recondetaildata.unReconItemsValue;
                if (this.recondetaildata.unReconItemsValuePer > 0) {
                    this.topTenUnReconValueSub = this.recondetaildata.unReconItemsValuePer;
                } else {
                    this.topTenUnReconValueSub = '';
                }
                this.topTenUnReconViolation = parseInt(this.recondetaildata.unReconItemsViolationPer);
                this.topTenUnReconViolationTitle = this.convertAmtFormat(this.recondetaildata.unReconItemsViolation);

                this.awaitingApproval = parseInt(this.recondetaildata.awaitingAppCountPer);
                this.awaitingApprovalCount = this.convertAmtFormat(this.recondetaildata.awaitingAppCount);
                if (this.recondetaildata.unReconItemsValue != 0 || this.recondetaildata.unReconItemsValue != null || this.recondetaildata.unReconItemsValue != undefined) {
                    /* having decimal */

                    this.topTenUnReconValueTitle = this.convertAmtFormat(this.recondetaildata.unReconItemsValue);
                    this.unReconCountTitle = this.convertAmtFormat(this.recondetaildata.unReconItemsCount);
                }
            },
            (res: Response) => {
                this.onError('Error Occured while fetching Reconciliation detail Data !')
            }
        );
    }

    convertAmtFormat(value) {
        let decimal = 1;
        let currency = '';
        let finalValue;
        if (value >= 0) {
            if (value >= 1000000000000) {
                finalValue = currency + " " + parseFloat((value / 1000000000000).toFixed(decimal).replace(/\.0$/, '')) + 'T';
            }
            else if (value >= 1000000000) {
                finalValue = currency + " " + parseFloat((value / 1000000000).toFixed(decimal).replace(/\.0$/, '')) + 'B';
            }
            else if (value >= 1000000) {
                finalValue = currency + " " + parseFloat((value / 1000000).toFixed(decimal).replace(/\.0$/, '')) + 'M';
            }
            else if (value >= 100000) {
                finalValue = currency + " " + parseFloat((value / 1000).toFixed(decimal).replace(/\.0$/, '')) + 'K';
            } else {
                if (value) {
                    finalValue = currency + " " + parseFloat(value.toFixed(decimal).replace(/\.0$/, '')).toLocaleString();
                } else {
                    finalValue = currency + " " + 0;
                }

            }
        } else {
            if (value <= -1000000000000) {
                finalValue = currency + " " + parseFloat((value / 1000000000000).toFixed(decimal).replace(/\.0$/, '')) + 'T';
            }
            else if (value <= -1000000000) {
                finalValue = currency + " " + parseFloat((value / 1000000000).toFixed(decimal).replace(/\.0$/, '')) + 'B';
            }
            else if (value <= -1000000) {
                finalValue = currency + " " + parseFloat((value / 1000000).toFixed(decimal).replace(/\.0$/, '')) + 'M';
            }
            else if (value <= -100000) {
                finalValue = currency + " " + parseFloat((value / 1000).toFixed(decimal).replace(/\.0$/, '')) + 'K';
            } else {
                if (value) {
                    finalValue = currency + " " + parseFloat(value.toFixed(decimal).replace(/\.0$/, '')).toLocaleString();
                } else {
                    finalValue = currency + " " + 0;
                }

            }
        }

        return finalValue;
    }

    /* Function to fetch accounting kpi details */
    /* accounting detail data*/
    accdetaildata: any;
    /*  public accountingItemChart: any[] = [];
     public accountingItemChartLabels: string[] = [];
     public accountingItemChartType: string = 'pie'; */
    fetchAccKPI() {
        this.requestAccKPI = this.dashboardService.fetchAccSpecificbyRgIdViewId(this.accRuleGrpId, this.accountingViolationPeriod, this.accSelectedViewId, this.reconDateRange).subscribe(
            (res: any) => {
                this.accdetaildata = res;
                this.accdetaildata.accountingData[0].un_accountedamt = this.convertAmtFormat(this.accdetaildata.accountingData[0].un_accountedamt);
                this.accdetaildata.unAccountedItemsViolation = this.convertAmtFormat(this.accdetaildata.unAccountedItemsViolation);
            },
            (res: Response) => {
                this.onError('Error Occured while fetching Accounting detail Data !')
            }
        );
    }

    reconViewCombinationSpecificAnalysis: any = [];
    subProcessAnalysisHeading: any;
    showReconApprovalPer: boolean = false;
    loadingTab: boolean = false;
    reconSelectedViewId: any;
    /* Function to fetch view combination based on ruleGrpId */
    fetchReconCombViews(type?, e?) {
        this.loadingTab = true;
        if (type == 'aging') {
            if (e.active[0] != undefined) {
                this.showReconApprovalPer = false;
                let aging = e.active["0"]._model.label;
                let lab = "Aging ";
                this.subProcessAnalysisHeading = lab.concat(aging);
                this.reconViewCombinationSpecificAnalysis = [];
                this.request = this.dashboardService.fetchSrcTrgCombViewAging(this.reconRuleGrpIdEncrypt, aging, this.reconBucketId, this.reconDateRange).subscribe((res: any) => {
                    this.reconViewCombinationSpecificAnalysis = res;
                    if (this.reconViewCombinationSpecificAnalysis.length > 0) {
                        this.loadingTab = false;
                        /* this.getGrpByDetails(this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewId, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewIdDisplay,
                            this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewType, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewName, 'Un-Reconciled'); */
                    }
                },
                    (res: Response) => {
                        this.onError(res.json()
                        )
                        this.commonService.error('Error!', 'Error While fetching data view combination data');
                    });
            }
        } else if (type == 'violation') {
            this.showReconApprovalPer = false;
            let violationPer = e;
            let lab = "Violation Period ";
            this.subProcessAnalysisHeading = lab.concat(e);
            this.reconViewCombinationSpecificAnalysis = [];
            this.request = this.dashboardService.fetchSrcTrgCombViewViolation(this.reconRuleGrpIdEncrypt, violationPer, this.reconDateRange).subscribe((res: any) => {
                this.reconViewCombinationSpecificAnalysis = res;
                if (this.reconViewCombinationSpecificAnalysis.length > 0) {
                    this.loadingTab = false;
                    /* this.getGrpByDetails(this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewId, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewIdDisplay,
                        this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewType, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewName, 'Un-Reconciled'); */
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });
        } else if (type == 'approvals') {
            this.subProcessAnalysisHeading = "Awaiting Approvals";
            this.reconViewCombinationSpecificAnalysis = [];
            this.request = this.dashboardService.fetchReconciliationCombinationViewSpecific(this.reconRuleGrpIdEncrypt, 'approvals', this.reconDateRange).subscribe((res: any) => {
                this.reconViewCombinationSpecificAnalysis = res;
                this.showReconApprovalPer = true;
                if (this.reconViewCombinationSpecificAnalysis.length > 0) {
                    this.loadingTab = false;
                    /* this.getGrpByDetails(this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewId, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewIdDisplay,
                        this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewType, this.reconViewCombinationSpecificAnalysis[0]['combination'][0].viewName, 'approvals'); */
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });

        } else {
            let typeM;
            if (e != undefined && e.active[0] != undefined) {
                if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'Label 0') {
                    typeM = e.active["0"]._model.label;
                    this.selectedGrpByTab = typeM;
                } else {
                    typeM = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
                    this.selectedGrpByTab = typeM;
                }
            } else {
                typeM = 'Un-Reconciled';
                this.selectedGrpByTab = 'Un-Reconciled';
            }
            this.showReconApprovalPer = false;
            this.subProcessAnalysisHeading = "Reconciliation Status";
            this.reconViewCombinationSpecificAnalysis = [];
            this.request = this.dashboardService.fetchReconciliationCombinationViewSpecific(this.reconRuleGrpIdEncrypt, typeM, this.reconDateRange).subscribe((res: any) => {
                this.reconViewCombinationSpecificAnalysis = res;
                if (this.reconViewCombinationSpecificAnalysis.length > 0) {
                    //this.fetchReconKPIAndDetails(typeM);
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });
        }
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
    activeItem: any;
    selectedProcessName: any;
    selectedProcessFun(col) {
        //console.log('selected process ::' + JSON.stringify(col));
        this.activeItem = col;
        this.selectedProcessName = col.processName;

    }
    showData(moduleName) {
        this.currentModule = moduleName;

        /* if(moduleName == 'reconciliation') {
            this.moduleData = [{"name":"Recon 01","Processor":"paytm","currency":"USD","transactionDate":"19/02/18","status":"Un-Reconciled","amount":'25,315.00'},{"name":"Recon 02","Processor":"Payzapp","currency":"AUD","transactionDate":"16/03/18","status":"Un-Reconciled","amount":'25,831.00'},{"name":"Recon 03","Processor":"Cayan","currency":"INR","transactionDate":"10/04/18","status":"Un-Reconciled","amount":'26,347.00'},{"name":"Recon 04","Processor":"Paypal","currency":"EUR","transactionDate":"05/05/18","status":"Un-Reconciled","amount":'26,863.00'}];
            
        }else if(moduleName == 'accounting') {
            this.moduleData = [{"name":"Account 01","Processor":"Payzapp","currency":"INR","transactionDate":"24/06/18","status":"Not-Accounted","amount":'27,895.00'},{"name":"Account 02","Processor":"Adyen","currency":"USD","transactionDate":"19/07/18","status":"Not-Accounted","amount":'28,411.00'},{"name":"Account 03","Processor":"Paypal","currency":"AUD","transactionDate":"13/08/18","status":"Not-Accounted","amount":'28,927.00'},{"name":"Account 04","Processor":"Zakpay","currency":"EUR","transactionDate":"07/09/18","status":"Not-Accounted","amount":'29,443.00'}];
        }else {
            this.moduleData = [{"name":"Journal 01","Processor":"Payzapp","currency":"USD","transactionDate":"16/12/18","status":"Un-Posted","amount":'31,507.00'},{"name":"Journal 02","Processor":"Paytm","currency":"AUD","transactionDate":"10/01/19","status":"Un-Posted","amount":'32,023.00'},{"name":"Journal 03","Processor":"Cayan","currency":"INR","transactionDate":"04/02/19","status":"Un-Posted","amount":'32,539.00'},{"name":"Journal 04","Processor":"Zakpay","currency":"EUR","transactionDate":"01/03/19","status":"Un-Posted","amount":'33,055.00'}];
        } */
    }

    changeTab(event) {
        /* if(type == 'Reconciled'){
            this.selectedTab = 1;
        }else if(type == 'Un-Reconciled'){
            this.selectedTab = 0;
        } */
        this.selectedTab = event.index;
        //console.log('this.selectedTab ' + event.index);
    }
    visibleSidebar2;
    openModuleSetting: any;

    openSideBar(type) {
        this.visibleSidebar2 = true;
        this.openModuleSetting = type;
    }

    cancelReconSetting() {
        if (this.openModuleSetting == 'Reconciliation Status') {
            this.reconColorTemp = this.reconColor;
            this.unReconColorTemp = this.unReconColor;
            this.tempSelectedPostion = this.selectedPostion;
            this.temppieChartReconciliationType = this.pieChartReconciliationType;
            this.reconBackgroundTemp = this.reconBackground;
            $(".reconBackgroundColor").css("background-color", this.reconBackgroundTemp);
        } else if (this.openModuleSetting == 'Un-Reconciled Transaction By Aging Bucket') {
            this.tempagingColor = this.agingColor;
            this.tempbarChartType = this.barChartType;
            this.agingBucketBackgroundTemp = this.agingBucketBackground;
            $(".agingBackgroundColor").css("background-color", this.agingBucketBackgroundTemp);
            this.unReconAgingBucketColors = [{ backgroundColor: [this.tempagingColor, this.tempagingColor, this.tempagingColor, this.tempagingColor] }];
        } else if (this.openModuleSetting == 'Un-Reconciled Items By Value') {
            this.tempunReconItemsByValueRadius = this.unReconItemsByValueRadius;
            this.tempunReconItemsByValueOuterStrokeColor = this.unReconItemsByValueOuterStrokeColor;
            this.tempunReconItemsByValueInnerStrokeColor = this.unReconItemsByValueInnerStrokeColor;
            this.tempunReconItemsByValueTitleFontSize = this.unReconItemsByValueTitleFontSize;
            this.tempunReconItemsByValueSubtitleFontSize = this.unReconItemsByValueSubtitleFontSize;
            this.tempunReconItemsByValueTitleColor = this.unReconItemsByValueTitleColor;
            this.tempunReconItemsByValueSubTitleColor = this.unReconItemsByValueSubTitleColor;
            this.tempunReconItemsByValueSubTitleColor = this.unReconItemsByValueUnitsColor;
        } else if (this.openModuleSetting == 'Un-Reconciliation Items By Violation') {
            this.tempunReconItemsByViolationRadius = this.unReconItemsByViolationRadius;
            this.tempunReconItemsByViolationOuterStrokeColor = this.unReconItemsByViolationOuterStrokeColor;
            this.tempunReconItemsByViolationInnerStrokeColor = this.unReconItemsByViolationInnerStrokeColor;
            this.tempunReconItemsByViolationTitleFontSize = this.unReconItemsByViolationTitleFontSize;
            this.tempunReconItemsByViolationSubtitleFontSize = this.unReconItemsByViolationSubtitleFontSize;
            this.tempunReconItemsByViolationTitleColor = this.unReconItemsByViolationTitleColor;
            this.tempunReconItemsByViolationSubTitleColor = this.unReconItemsByViolationSubTitleColor;
            this.tempunReconItemsByViolationSubTitleColor = this.unReconItemsByViolationUnitsColor;
        } else if (this.openModuleSetting == 'Awaiting Approvals') {
            this.tempreconAwaitingForApprovalRadius = this.reconAwaitingForApprovalRadius;
            this.tempreconAwaitingForApprovalOuterStrokeColor = this.reconAwaitingForApprovalOuterStrokeColor;
            this.tempreconAwaitingForApprovalInnerStrokeColor = this.reconAwaitingForApprovalInnerStrokeColor;
            this.tempreconAwaitingForApprovalTitleFontSize = this.reconAwaitingForApprovalTitleFontSize;
            this.tempreconAwaitingForApprovalTitleColor = this.reconAwaitingForApprovalTitleColor;
        }
    }
    saveReconSetting() {
        if (this.openModuleSetting == 'Reconciliation Status') {
            this.reconColor = this.reconColorTemp;
            this.unReconColor = this.unReconColorTemp;
            /* this.selectedPostion = this.tempSelectedPostion; */
            this.pieChartReconciliationType = this.temppieChartReconciliationType;
            this.reconBackground = this.reconBackgroundTemp;
            $(".reconBackgroundColor").css("background-color", this.reconBackground);
            this.pieChartReconciliationColors = [{ backgroundColor: [this.reconColor, this.unReconColor] }];
        } else if (this.openModuleSetting == 'Un-Reconciled Transaction By Aging Bucket') {
            this.agingColor = this.tempagingColor;
            this.barChartType = this.tempbarChartType;
            this.agingBucketBackground = this.agingBucketBackgroundTemp;
            $(".agingBackgroundColor").css("background-color", this.agingBucketBackground);
            this.unReconAgingBucketColors = [{ backgroundColor: [this.agingColor, this.agingColor, this.agingColor, this.agingColor] }];
        } else if (this.openModuleSetting == 'Un-Reconciled Items By Value') {
            this.unReconItemsByValueRadius = this.tempunReconItemsByValueRadius;
            this.unReconItemsByValueOuterStrokeColor = this.tempunReconItemsByValueOuterStrokeColor;
            this.unReconItemsByValueInnerStrokeColor = this.tempunReconItemsByValueInnerStrokeColor;
            this.unReconItemsByValueTitleFontSize = this.tempunReconItemsByValueTitleFontSize;
            this.unReconItemsByValueSubtitleFontSize = this.tempunReconItemsByValueSubtitleFontSize;
            this.unReconItemsByValueTitleColor = this.tempunReconItemsByValueTitleColor;
            this.unReconItemsByValueSubTitleColor = this.tempunReconItemsByValueSubTitleColor;
            this.unReconItemsByValueUnitsColor = this.tempunReconItemsByValueSubTitleColor;
        } else if (this.openModuleSetting == 'Un-Reconciliation Items By Violation') {
            this.unReconItemsByViolationRadius = this.tempunReconItemsByViolationRadius;
            this.unReconItemsByViolationOuterStrokeColor = this.tempunReconItemsByViolationOuterStrokeColor;
            this.unReconItemsByViolationInnerStrokeColor = this.tempunReconItemsByViolationInnerStrokeColor;
            this.unReconItemsByViolationTitleFontSize = this.tempunReconItemsByViolationTitleFontSize;
            this.unReconItemsByViolationSubtitleFontSize = this.tempunReconItemsByViolationSubtitleFontSize;
            this.unReconItemsByViolationTitleColor = this.tempunReconItemsByViolationTitleColor;
            this.unReconItemsByViolationSubTitleColor = this.tempunReconItemsByViolationSubTitleColor;
            this.unReconItemsByViolationUnitsColor = this.tempunReconItemsByViolationSubTitleColor;
        } else if (this.openModuleSetting == 'Awaiting Approvals') {
            this.reconAwaitingForApprovalRadius = this.tempreconAwaitingForApprovalRadius;
            this.reconAwaitingForApprovalOuterStrokeColor = this.tempreconAwaitingForApprovalOuterStrokeColor;
            this.reconAwaitingForApprovalInnerStrokeColor = this.tempreconAwaitingForApprovalInnerStrokeColor;
            this.reconAwaitingForApprovalTitleFontSize = this.tempreconAwaitingForApprovalTitleFontSize;
            this.reconAwaitingForApprovalTitleColor = this.tempreconAwaitingForApprovalTitleColor;
        }
    }

    /* Accounting */
    //currentAccModule: any;
    showAccountingData(moduleName) {
        this.currentAccModule = moduleName;
    }

    changeAccTab(event) {
        this.selectedAccTab = event.index;
        //console.log('this.selectedAccTab ' + event.index);
    }

    changeAccActivityTab(event) {
        this.selectedAccTabActivity = event.index;
        //console.log('this.selectedAccTabActivity ' + event.index);
    }
    selectedModuleType: any = 0;
    changeModuleType(e) {
        if (e.value == 'etl') {
            this.showetl = true;
            this.showrecon = false;
            this.showacc = false;
            $('.allStausCls,.reconciliationStatusCls,.accountingStatusCls').removeClass('sideProgessActive');
            $('.extractionStatusCls').addClass('sideProgessActive');
        } else if (e.value == 'Reconciliation') {
            this.showetl = false;
            this.showrecon = true;
            this.showacc = false;
            $('.extractionStatusCls, .allStausCls, .accountingStatusCls').removeClass('sideProgessActive');
            $('.reconciliationStatusCls').addClass('sideProgessActive');
        } else if (e.value == 'Accounting') {
            this.showetl = false;
            this.showrecon = false;
            this.showacc = true;
            $('.extractionStatusCls, .allStausCls, .reconciliationStatusCls').removeClass('sideProgessActive');
            $('.accountingStatusCls').addClass('sideProgessActive');
        }
    }


    unReconAgingBucketDataSelected: any;
    showAgingFilterData: boolean = false;
    selectedRegion: any;
    fetchUnReconciliationAging(e) {

        /* var chartElement = e.active[0]._chart.getElementAtEvent(event); */
        if (e.active.length > 0) {
            this.showAgingFilterData = true;
            this.selectedRegion = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (this.selectedRegion != undefined) {
                this.unReconAgingBucketDataSelected = this.unAccountedAgingWiseData[this.selectedRegion];
            }

        } else {
            this.showAgingFilterData = false;
        }
    }

    fetchAccountingActivity(e) {
        if (e.active.length > 0) {
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (labelName == 'Accounted For Un-Identified') {
                this.selectedAccTabActivity = 0;
            } else if (labelName == 'Accounted For Identified') {
                this.selectedAccTabActivity = 1;
            }
        }
    }

    unAccountedAgingBucketDataSelected: any;
    showAgingFilterDataUnAcc: boolean = false;
    selectedUnAccRegion: any;
    fetchAccountingAging(e) {
        /* if(e.active.length>0){
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
             if(labelName == 'Reconciled'){
                this.selectedTab = 1;
            }else if(labelName == 'Un-Reconciled'){
                this.selectedTab = 0;
            }else if(labelName == 'Un-Reconciled'){
                this.selectedTab = 0;
            }else if(labelName == 'Un-Reconciled'){
                this.selectedTab = 0;
            } 
        } */
        if (e.active.length > 0) {
            this.selectedUnAccRegion = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (this.selectedUnAccRegion != undefined) {
                this.unAccountedAgingBucketDataSelected = this.unAccountedAgingBucketData[this.selectedUnAccRegion];
                this.showAgingFilterDataUnAcc = true;
            }

        } else {
            this.showAgingFilterDataUnAcc = false;
        }
    }

    convertAmountFormat(input: number, currency?: any, decimal?: number) {
        if (input != 0 || input != null || input != undefined) {
            /* having decimal */
            if (decimal == 0) {
                decimal = 0;
            } else if (!decimal) {
                decimal = 2;
            }

            if (input >= 1000000000000) {
                return currency + " " + parseFloat((input / 1000000000000).toFixed(decimal).replace(/\.0$/, '')) + 'T';
            }
            else if (input >= 1000000000) {
                return currency + " " + parseFloat((input / 1000000000).toFixed(decimal).replace(/\.0$/, '')) + 'B';
            }
            else if (input >= 1000000) {
                return currency + " " + parseFloat((input / 1000000).toFixed(decimal).replace(/\.0$/, '')) + 'M';
            }
            else if (input >= 100000) {
                return currency + " " + parseFloat((input / 1000).toFixed(decimal).replace(/\.0$/, '')) + 'K';
            } else {
                //return currency + " " + input.toFixed(decimal).replace(/\.0$/, '');
                if (input) {
                    // return currency + " " + parseFloat(input.toFixed(decimal).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
                    // return currency + " " + input.toFixed(decimal).replace(/\.0$/, '');
                    return currency + " " + parseFloat(input.toFixed(decimal).replace(/\.0$/, '')).toLocaleString();
                } else {
                    return currency + " " + 0;
                }

            }
        } else {

        }
    }

    viewSpecificColumnsGrpBy: any = [];
    grpByDetails: any = [];
    reconStatusTabCol: any = [];
    columnOptions: any = [];
    showUnReconGrpBy: boolean = false;

    /* Variables for grpBy parameters */
    //reconSelectedViewId: any;
    reconSelectedDisplayViewId: any;
    reconSelectedType: any;
    selectedCombViewName: any;

    /* acc */
    accSelectedViewRS: any = [];
    accSelectedViewId: any = [];
    accSelectedDisplayViewId: any;
    accSelectedType: any
    accSelectedCombViewName: any
    showAccGrpBy: boolean = false;
    accViewSpecificColumnsGrpBy: any = [];
    requestGrp: any;
    /* Function to fetch grp by view details for recon & accounting */
    getGrpByDetails() {
        if (this.showReconApprovalPer) {
            this.reconStatusTabCol = [];
            this.requestGrp = this.dashboardService.fetchApprovalsDetails(this.reconRuleGrpIdEncrypt, this.reconSelectedViewId, 'approvals', this.reconDateRange).subscribe((res: any) => {
                this.grpByDetails = res;
                this.reconStatusTabCol = [
                    /* { field: 'batchId', header: 'Batch Id', align: 'right' }, */
                    { field: 'batchName', header: 'Batch Name', align: 'left' },
                    { field: 'currentApprover', header: 'Current Approver', align: 'left' },
                    { field: 'count', header: 'Count', align: 'center' },
                    { field: 'nextApprover', header: 'Next Approver', align: 'left' }
                ]
            });
        } else {
            this.selectedGrpByTab = this.reconSelType;
            this.viewSpecificColumnsGrpBy = [];
            let colList = [];
            this.selectedViewRS = [];
            this.showUnReconGrpBy = false;
            this.requestGrp = this.dashboardService.fetchColLovGrpBy(this.reconSelectedViewId).subscribe((res: any) => {
                this.viewSpecificColumnsGrpBy = res;
                if (this.viewSpecificColumnsGrpBy.currencyCodeQualifier) {
                    this.fetchGrpByCurrency();
                } else {
                    if (this.viewSpecificColumnsGrpBy.columnsList.length > 0) {
                        this.viewSpecificColumnsGrpBy.columnsList.forEach((element, i) => {
                            let temp = ((element.columnName).length) * 10;
                            let tempWidth = temp.toString().concat('px !important');
                          //  $('reconWidth').css('width', tempWidth);
                            if (element.length > 4) {
                                if (i < 4) {
                                    this.selectedViewRS.push(element.columnAliasName);
                                }
                            } else {
                                this.selectedViewRS.push(element.columnAliasName);
                            }
                        });
                        if (this.selectedViewRS.length > 0) {
                            this.fetchGrpByData(this.reconSelType);
                        }
                    }
                }

            });
        }
    }

    accGetGrpByDetails() {
        if (this.accSelType == 'Un,Accounted') {
            this.accSelType = 'Un-Accounted';
        } else if (this.accSelType == 'JE,Pending') {
            this.accSelType = 'JE Pending';
        } else if (this.accSelType == 'JE,Created') {
            this.accSelType = 'JE Created';
        }
        //console.log('this.accSelType ' + this.accSelType);
        if (this.showAccApprovalPer) {
            this.accStatusTabCol = [];
            this.request = this.dashboardService.fetchApprovalsDetails(this.accRuleGrpIdEncrypt, this.accSelectedViewId, 'approvals', this.reconDateRange).subscribe((res: any) => {
                this.grpByDetails = res;
                this.accStatusTabCol = [
                    { field: 'batchName', header: 'Batch Name', align: 'left' },
                    { field: 'currentApprover', header: 'Current Approver', align: 'left' },
                    { field: 'count', header: 'Count', align: 'center' },
                    { field: 'nextApprover', header: 'Next Approver', align: 'left' }
                ]
            });
        } else {
            this.accSelectedGrpByTab = this.accSelType;
            this.accViewSpecificColumnsGrpBy = [];
            let colList = [];
            this.accSelectedViewRS = [];
            this.showAccGrpBy = false;
            this.request = this.dashboardService.fetchColLovGrpBy(this.accSelectedViewId).subscribe((res: any) => {
                this.accViewSpecificColumnsGrpBy = res;
                if (this.accViewSpecificColumnsGrpBy.currencyCodeQualifier) {
                    this.fetchAccGrpByCurrency();
                } else {
                    if (this.accViewSpecificColumnsGrpBy.columnsList.length > 0) {
                        this.accViewSpecificColumnsGrpBy.columnsList.forEach((element, i) => {
                            if (element.length > 4) {
                                if (i < 4) {
                                    this.accSelectedViewRS.push(element.columnAliasName);
                                }
                            } else {
                                this.accSelectedViewRS.push(element.columnAliasName);
                            }
                        });
                        if (this.accSelectedViewRS.length > 0) {
                            this.accFetchGrpByData(this.accSelType);
                        }
                    }
                }
            });
        }
    }

    accStatusTabCol: any = [];
    accColumnOptions: any = [];
    accGrpByDetails: any = [];
    reconStatusVar: boolean = false;
    selectedAging: any;
    showAging: boolean = false;
    reconType: any;
    fetchGrpByData(type?, e?, val?) {
        this.grpByDetails = [];
        this.showAging = false;
        $('.profTrg .mat-tab-label').removeClass('mat-tab-label-active');
        $('.spinnerBlockTable').addClass('spinnerBlockTable-icon');
        let violationPer = null;
        let age = null;
        let filterKey = null;
        let filterValues:any = null;
        if (type == 'approvals') {
            this.showReconApprovalPer = true;
            this.subProcessAnalysisHeading = "Awaiting Approvals";
            this.reconStatusTabCol = [];
            this.requestGrp = this.dashboardService.fetchApprovalsDetails(this.reconRuleGrpIdEncrypt, this.reconSelectedViewId, 'reconciliation', this.reconDateRange).subscribe((res: any) => {
                this.grpByDetails = res;
                this.loadingKPIGrp = false;
                $('.spinnerBlockTable').removeClass('spinnerBlockTable-icon');
                this.reconStatusTabCol = [
                    /* { field: 'batchId', header: 'Batch Id', align: 'right' }, */
                    { field: 'batchName', header: 'Batch Name', align: 'left' },
                    { field: 'currentApprover', header: 'Current Approver', align: 'left' },
                    { field: 'count', header: 'Count', align: 'center' },
                    { field: 'nextApprover', header: 'Next Approver', align: 'left' }
                ]
            });
        } else {
            if (type == 'violation') {
                this.showReconApprovalPer = false;
                violationPer = e;
                let lab = "Violation Period ";
                //this.subProcessAnalysisHeading = lab.concat(e);
                this.subProcessAnalysisHeading = "Violation Period";
                type = 'Un-Reconciled';
            } else if (type == 'aging') {
                this.showAging = true;
                if (e && e.active && e.active[0] != undefined) {
                    this.agingLov.forEach(element => {
                        if(element.name == e.active["0"]._model.label){
                            element['checked'] = true;
                        }else{
                            element['checked'] = false;
                        }
                    });
                    this.showReconApprovalPer = false;
                    age = e.active["0"]._model.label;
                    this.selectedAging = e.active["0"]._model.label;
                    let lab = "Aging ";
                    this.subProcessAnalysisHeading = lab.concat(age);
                    type = 'Un-Reconciled';
                }else if(e && e.active == undefined){
                    this.agingLov.forEach(element => {
                        if(element.name == e.name){
                            element['checked'] = true;
                        }else{
                            element['checked'] = false;
                        }
                    });
                    //console.log('barChartLabels ' + JSON.stringify(this.barChartLabels));
                    this.showReconApprovalPer = false;
                    age = e.name;
                    this.selectedAging = e.name;
                    let lab = "Aging ";
                    this.subProcessAnalysisHeading = lab.concat(age);
                    type = 'Un-Reconciled';
                }else{
                    this.agingLov.forEach(element => {
                        if(element.name == "All"){
                            element['checked'] = true;
                        }else{
                            element['checked'] = false;
                        }
                    });
                    //console.log('barChartLabels ' + JSON.stringify(this.barChartLabels));
                    this.showReconApprovalPer = false;
                    age = "All";
                    this.selectedAging = "All";
                    let lab = "Aging ";
                    this.subProcessAnalysisHeading = lab.concat(age);
                    type = 'Un-Reconciled';
                }
            } else if (type == 'currency') {
                this.showReconApprovalPer = false;
                /* if(e == "All"){
                    this.selectedViewRS.push(this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName);
                    filterKey = this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
                   // filterValues = this.uniqueCurrencyCode;
                    this.subProcessAnalysisHeading = "Currency";
                    console.log('selectedViewRS ' + JSON.stringify(this.selectedViewRS));
                }else{ */
                    if (e != undefined && e.active && e.active[0] != undefined) {
                        ////console.log('e ' + JSON.stringify(e));
                        if (val) {
                            type = val;
                            this.uniqueCurrencyCode = this.pieChartActivityAccountingLabels;
                            //this.uniqueCurrencyCode.push("All");
                        } else {
                            type = this.selectedGrpByTab;
                        }
                        filterKey = this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
                        filterValues = e.active["0"]._chart.config.data.labels[e.active["0"]._index];
                        this.reconCurrencyCode = e.active["0"]._chart.config.data.labels[e.active["0"]._index];
                        this.selectedGrpByTab = type;
                        let lab = "Currency";
                        //this.subProcessAnalysisHeading = lab.concat(e.active["0"]._chart.config.data.labels[e.active["0"]._index]);
                        this.subProcessAnalysisHeading = "Currency";
                        this.uniqueCurrencyCode.forEach((element,i) => {
                            if(element == filterValues){
                                this.reconCurrencyTab = i+1;
                            }
                        });
                    } else {
                        if (e != undefined && e.active && !e.active[0]) {
                            this.selectedViewRS.push(this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName);
                            if (val) {
                                type = val;
                                this.uniqueCurrencyCode = this.pieChartActivityAccountingLabels;
                                this.reconCurrencyTab = 0;
                            } else {
                                type = this.selectedGrpByTab;
                            }
                        }else if (e) {
                            if (val) {
                                type = val;
                                this.uniqueCurrencyCode = this.pieChartActivityAccountingLabels;
                            } else {
                                type = this.selectedGrpByTab;
                            }
                            filterKey = this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
                            if (e.length) {
                                filterValues = e;
                            } else {
                                filterValues = this.uniqueCurrencyCode[e-1];
                            }
                        }
                        this.subProcessAnalysisHeading = "Currency";
                    }
                /* } */
                
            } else if (type == 'reconciliation' || 'Un-Reconciled' || 'Reconciled') {
                this.showReconApprovalPer = false;
                if (e != undefined && e.active[0] != undefined) {
                    if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'Label 0') {
                        type = e.active["0"]._model.label;
                        this.selectedGrpByTab = type;
                        this.subProcessAnalysisHeading = type;
                    } else {
                        type = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
                        this.selectedGrpByTab = type;
                        this.subProcessAnalysisHeading = type;
                    }
                } else {
                    this.selectedGrpByTab = type;
                    this.subProcessAnalysisHeading = type;
                }
            }

            this.reconStatusTabCol = [];
            this.columnOptions = [];

            this.showUnReconGrpBy = false;
            if(type == 'Reconciled'){
                this.reconStatusTab = 1;
            }else{
                this.reconStatusTab = 0;
            }
            this.selectedViewRS = this.selectedViewRS.filter(function (item, i, ar) { return ar.indexOf(item) === i; });
            this.reconType = type
            this.request = this.dashboardService.fetchUnReconciledOrUnAccountedGroupByInfo(this.reconRuleGrpIdEncrypt, this.reconSelectedViewId, this.reconSelViewType, type, this.selectedViewRS, this.reconDateRange, violationPer, age, filterKey, filterValues, this.reconBucketId).subscribe((res: any) => {
                this.grpByDetails = res;
                this.onloadTemp = true;
                //  console.log('tempUniqueCurrency ' + JSON.stringify(this.tempUniqueCurrency));
                $('.spinnerBlockTable').removeClass('spinnerBlockTable-icon');
                this.selectedViewRS = [];
                //console.log('this.selectedTab ' + this.selectedTab);
                //console.log('this.subProcessAnalysisHeading ' + this.subProcessAnalysisHeading);
                if (this.subProcessAnalysisHeading != "Currency") {
                    this.fetchReconGrpByCurrency();
                    //console.log('this.uniqueCurrencyCode ' + JSON.stringify(this.uniqueCurrencyCode));
                  /*   var unique = {};
                    this.uniqueCurrencyCode = [];
                    this.grpByDetails.detailList.forEach(element => {
                        if (!unique[element[this.tempColName]]) {
                            this.uniqueCurrencyCode.push(element[this.tempColName]);
                            unique[element[this.tempColName]] = element;
                        }
                        //console.log('this.uniqueCurrencyCode ' + JSON.stringify(this.uniqueCurrencyCode));
                    });
                    this.fetchReconGrpByCurrency(this.uniqueCurrencyCode[0]); */
                } else {
                    if (filterValues != null) {
                        this.fetchReconGrpByCurrency(filterValues);
                    } else {
                        this.fetchReconGrpByCurrency();
                    }

                }
                this.grpByDetails.columnNames.columnsList.forEach((element, i) => {
                    if (element.columnAliasName != this.tempColName && this.grpByDetails.columnNames.columnsList.length > 1) {
                        if (element.length > 4) {
                            if (i < 4) {
                                this.selectedViewRS.push(element.columnAliasName);
                            }
                        } else {
                            this.selectedViewRS.push(element.columnAliasName);
                        }
                        let obj = { field: element.columnAliasName, header: element.columnName, align: element.align }
                        this.reconStatusTabCol.push(obj);
                    } else {
                        if (element.length > 4) {
                            if (i < 4) {
                                this.selectedViewRS.push(element.columnAliasName);
                            }
                        } else {
                            this.selectedViewRS.push(element.columnAliasName);
                        }
                        let obj = { field: element.columnAliasName, header: element.columnName, align: element.align }
                        this.reconStatusTabCol.push(obj);
                    }
                });
                /* if(e == "All"){
                    let obj = { field: this.grpByDetails.columnNames.amtQualifier.header, header: this.grpByDetails.columnNames.amtQualifier.field, align: 'left' }
                    this.reconStatusTabCol.push(obj);
                } */
                for (let i = 0; i < this.reconStatusTabCol.length; i++) {
                    this.columnOptions.push({ label: this.reconStatusTabCol[i].header, value: this.reconStatusTabCol[i] });
                }
                /* console.log('this.reconStatusTabCol ' + JSON.stringify(this.reconStatusTabCol)); */
                this.loadingKPIGrp = false;
                this.showUnReconGrpBy = true;
                //this.scrollTab();
            });
        }
    }
    reconFilteredData: any = [];
    reconCurrencyOption: boolean = false;
    fetchReconGrpByCurrency(val?) {
        // console.log('val ' + val);
        if (val) {
            this.changedCol = false;
            this.changeColor(val);
            this.reconCurrencyCode = val;
            this.reconFilteredData = [];
            let tempDetailList = [];
            this.grpByDetails.detailList.forEach(element => {
                if (val == element[this.tempColName]) {
                    tempDetailList.push(element);
                    //console.log('tempDetailList ' + JSON.stringify(tempDetailList));
                }
            });
            if (tempDetailList && tempDetailList.length) {
                this.reconCurrencyOption = true;
                this.reconFilteredData['detailList'] = tempDetailList;
                this.reconFilteredData['columnNames'] = this.grpByDetails.columnNames;
                /* this.scrollTab(); */
                //console.log('this.reconFilteredData ' + JSON.stringify(this.reconFilteredData));
            } else {
                this.reconCurrencyOption = false;
                this.reconFilteredData['detailList'] = this.grpByDetails.detailList;
                this.reconFilteredData['columnNames'] = this.grpByDetails.columnNames;
                /* this.scrollTab(); */
            }
        } else {
            this.reconCurrencyOption = false;
            this.reconFilteredData['detailList'] = this.grpByDetails.detailList;
            this.reconFilteredData['columnNames'] = this.grpByDetails.columnNames;
            /* this.scrollTab(); */
        }

    }
    scrollTab() {
        $('html, body').animate({
            scrollTop: $("#reconDetailAnalysis").offset().top - 95
        }, 1000);
    }
    scrollTabAcc() {
        $('html, body').animate({
            scrollTop: $("#accDetailAnalysis").offset().top - 95
        }, 2000);
    }
    reconGrpByCurrency: any = [];
    showReconCurrencyChart: boolean = false;
    requestCurrency: any;
    tempUniqueCurrency: any;
    tempColName: any;
    uniqueCurrencyCode: any = [];
    accUniqueCurrencyCode: any = [];
    reconCurrencyCode: any;
    naturalCompare(a, b) {
        var ax = [], bx = [];
     
        a.replace(/(\d+)|(\D+)/g, function (_, $1, $2) { ax.push([$1 || Infinity, $2 || ""]) });
        b.replace(/(\d+)|(\D+)/g, function (_, $1, $2) { bx.push([$1 || Infinity, $2 || ""]) });
     
        while (ax.length && bx.length) {
          var an = ax.shift();
          var bn = bx.shift();
          var nn = (an[0] - bn[0]) || an[1].localeCompare(bn[1]);
          if (nn) return nn;
        }
     
        return ax.length - bx.length;
     }
    fetchGrpByCurrency() {
        $('.spinnerCurrencyBlock').addClass('spinnerBlock-icon');
        this.tempUniqueCurrency = [];
        this.uniqueCurrencyCode = [];
        this.showReconCurrencyChart = false;
        let violationPer = null;
        let age = null;
        this.pieChartActivityAccounting = [];
        this.pieChartActivityAccountingLabels = [];
        this.requestCurrency = this.dashboardService.fetchUnReconciledOrUnAccountedGroupByInfoCurrency(this.reconRuleGrpIdEncrypt, this.reconSelectedViewId, this.reconSelViewType, 'un-reconciled', this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName, this.reconDateRange, violationPer, age).subscribe((res: any) => {
            this.reconGrpByCurrency = res;
            /* console.log('this.reconGrpByCurrency ' + JSON.stringify(this.reconGrpByCurrency)); */
            //console.log('this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName ' + this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName);
            this.tempColName = this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
            //console.log('this.tempColName ' + this.tempColName);
            if (this.reconGrpByCurrency.detailList && this.reconGrpByCurrency.detailList.length) {
                //this.reconGrpByCurrency.detailList[this.tempColName].sort();
                this.reconGrpByCurrency.detailList = this.reconGrpByCurrency.detailList.sort((n1, n2) => {
                    return this.naturalCompare(n1[this.tempColName], n2[this.tempColName])
                  })
              //  console.log('this.reconGrpByCurrency.detailList ' + JSON.stringify(this.reconGrpByCurrency.detailList));
                var unique = {};
                this.reconGrpByCurrency.detailList.forEach(element => {
                    if (!unique[element[this.tempColName]]) {
                        this.uniqueCurrencyCode.push(element[this.tempColName]);
                        unique[element[this.tempColName]] = element;
                    }
                    // console.log('this.uniqueCurrencyCode ' + JSON.stringify(this.uniqueCurrencyCode));
                    this.pieChartActivityAccounting.push(element['countPer']);
                    this.pieChartActivityAccountingLabels.push(element[this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName]);
                });
                this.reconCurrencyCode = this.uniqueCurrencyCode[0];
                if (this.viewSpecificColumnsGrpBy.columnsList.length > 0) {
                    this.viewSpecificColumnsGrpBy.columnsList.forEach((element, i) => {
                        if (element.length > 4) {
                            if (i < 4) {
                                this.selectedViewRS.push(element.columnAliasName);
                            }
                        } else {
                            this.selectedViewRS.push(element.columnAliasName);
                        }
                    });
                    if (this.selectedViewRS.length > 0) {
                        this.fetchGrpByData(this.reconSelType);
                    }
                }
            } else {
                if (this.viewSpecificColumnsGrpBy.columnsList.length > 0) {
                    this.viewSpecificColumnsGrpBy.columnsList.forEach((element, i) => {
                        if (element.length > 4) {
                            if (i < 4) {
                                this.selectedViewRS.push(element.columnAliasName);
                            }
                        } else {
                            this.selectedViewRS.push(element.columnAliasName);
                        }
                    });
                    if (this.selectedViewRS.length > 0) {
                        this.fetchGrpByData(this.reconSelType);
                    }
                }
            }
            if (this.pieChartActivityAccountingLabels && this.pieChartActivityAccountingLabels.length) {
                $('.spinnerCurrencyBlock').removeClass('spinnerBlock-icon');
                this.showReconCurrencyChart = true;
            }else{
                $('.spinnerCurrencyBlock').removeClass('spinnerBlock-icon');
            }
        });
    }
    accGrpByCurrency: any = [];
    showAccCurrencyChart: boolean = false;
    acctempUniqueCurrency: any;
    acctempColName: any;
    accuniqueCurrencyCode: any = [];
    accCurrencyCode: any;
    fetchAccGrpByCurrency() {
        this.acctempUniqueCurrency = [];
        this.accuniqueCurrencyCode = [];
        this.showAccCurrencyChart = false;
        let violationPer = null;
        let age = null;
        this.pieChartGrpCurrencyAccounting = [];
        this.pieChartGrpCurrencyAccountingLabels = [];
        this.request1 = this.dashboardService.fetchUnReconciledOrUnAccountedGroupByInfoCurrency(this.accRuleGrpIdEncrypt, this.accSelectedViewId, this.accSelViewType, 'un-accounted', this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName, this.reconDateRange, violationPer, age).subscribe((res: any) => {
            this.accGrpByCurrency = res;
            this.acctempColName = this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
            var unique = {};
            if (this.accGrpByCurrency.detailList && this.accGrpByCurrency.detailList.length) {
                this.accGrpByCurrency.detailList = this.accGrpByCurrency.detailList.sort((n1, n2) => {
                    return this.naturalCompare(n1[this.acctempColName], n2[this.acctempColName])
                  })
                this.accGrpByCurrency.detailList.forEach(element => {
                    if (!unique[element[this.acctempColName]]) {
                        this.accuniqueCurrencyCode.push(element[this.acctempColName]);
                        unique[element[this.acctempColName]] = element;
                    }
                    this.pieChartGrpCurrencyAccounting.push(element['countPer']);
                    this.pieChartGrpCurrencyAccountingLabels.push(element[this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName]);
                });
                this.accCurrencyCode = this.accuniqueCurrencyCode[0];

                if (this.accViewSpecificColumnsGrpBy.columnsList.length > 0) {
                    this.accViewSpecificColumnsGrpBy.columnsList.forEach((element, i) => {
                        if (element.length > 4) {
                            if (i < 4) {
                                this.accSelectedViewRS.push(element.columnAliasName);
                            }
                        } else {
                            this.accSelectedViewRS.push(element.columnAliasName);
                        }
                    });
                    if (this.accSelectedViewRS.length > 0) {
                        this.accFetchGrpByData(this.accSelType);
                    }
                }
            } else {
                if (this.accViewSpecificColumnsGrpBy.columnsList.length > 0) {
                    this.accViewSpecificColumnsGrpBy.columnsList.forEach((element, i) => {
                        if (element.length > 4) {
                            if (i < 4) {
                                this.accSelectedViewRS.push(element.columnAliasName);
                            }
                        } else {
                            this.accSelectedViewRS.push(element.columnAliasName);
                        }
                    });
                    if (this.accSelectedViewRS.length > 0) {
                        this.accFetchGrpByData(this.accSelType);
                    }
                }
            }
            if (this.pieChartGrpCurrencyAccountingLabels && this.pieChartGrpCurrencyAccountingLabels.length) {
                this.showAccCurrencyChart = true;
            }
        });
    }
    showAccAging:boolean = false;
    accFetchGrpByData(type?, e?, val?) {
        this.accGrpByDetails = [];
        this.showAccAging = false;
        $('.spinnerBlockTable').addClass('spinnerBlockTable-icon');
        if (type == 'Un,Accounted') {
            type = 'Un-Accounted';
        } else if (type == 'JE,Pending') {
            type = 'JE Pending';
        } else if (type == 'JE,Created') {
            type = 'JE Created';
        }
        let violationPer = null;
        let age = null;
        let filterKey = null;
        let filterValues = null;
        if (type == 'approvals') {
            this.showAccApprovalPer = true;
            this.subProcessAnalysisHeadingAcc = "Awaiting Approvals";
            this.accStatusTabCol = [];
            this.request = this.dashboardService.fetchApprovalsDetails(this.accRuleGrpIdEncrypt, this.accSelectedViewId, 'accounting', this.reconDateRange).subscribe((res: any) => {
                this.accGrpByDetails = res;
                $('.spinnerBlockTable').removeClass('spinnerBlockTable-icon');
                this.accStatusTabCol = [
                    /* { field: 'batchId', header: 'Batch Id', align: 'right' }, */
                    { field: 'batchName', header: 'Batch Name', align: 'left' },
                    { field: 'currentApprover', header: 'Current Approver', align: 'left' },
                    { field: 'count', header: 'Count', align: 'right' },
                    { field: 'nextApprover', header: 'Next Approver', align: 'left' }
                ]
            });
        } else {
            if (type == 'violation') {
                this.showAccApprovalPer = false;
                violationPer = e;
                let lab = "Violation Period ";
                //this.subProcessAnalysisHeadingAcc = lab.concat(e);
                this.subProcessAnalysisHeadingAcc = "Violation Period";
                type = 'Un-Accounted';
            } else if (type == 'aging') {
                this.showAccAging = true;
                if (e && e.active && e.active[0] != undefined) {
                    this.accAgingLov.forEach(element => {
                        if(element.name == e.active["0"]._model.label){
                            element['checked'] = true;
                        }else{
                            element['checked'] = false;
                        }
                    });
                    this.showAccApprovalPer = false;
                    age = e.active["0"]._model.label;
                    let lab = "Aging ";
                    this.subProcessAnalysisHeadingAcc = lab.concat(age);
                    type = 'Un-Accounted';
                }else if(e && e.active == undefined){
                    this.accAgingLov.forEach(element => {
                        if(element.name == e.name){
                            element['checked'] = true;
                        }else{
                            element['checked'] = false;
                        }
                    });
                   // console.log('accAgingChartLabels ' + JSON.stringify(this.accAgingChartLabels));
                    this.showAccApprovalPer = false;
                    age = e.name;
                    let lab = "Aging ";
                    this.subProcessAnalysisHeadingAcc = lab.concat(age);
                    type = 'Un-Accounted';
                } else{
                    this.accAgingLov.forEach(element => {
                        if(element.name == "All"){
                            element['checked'] = true;
                        }else{
                            element['checked'] = false;
                        }
                    });
                  //  console.log('accAgingChartLabels ' + JSON.stringify(this.accAgingChartLabels));
                    this.showAccApprovalPer = false;
                    age = "All";
                    let lab = "Aging ";
                    this.subProcessAnalysisHeadingAcc = lab.concat(age);
                    type = 'Un-Accounted';
                }
            }else if (type == 'currency') {
                this.showAccApprovalPer = false;
                if (e != undefined && e.active && e.active[0] != undefined) {
                    if (val) {
                        type = val;
                        this.accuniqueCurrencyCode = this.pieChartGrpCurrencyAccountingLabels;
                    } else {
                        type = this.accSelectedGrpByTab;
                    }
                    ////console.log('e ' + JSON.stringify(e));
                    //type = 'Un-Accounted';
                    filterKey = this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
                    filterValues = e.active["0"]._chart.config.data.labels[e.active["0"]._index];
                    this.accSelectedGrpByTab = type;
                    let lab = "Currency";
                    //this.subProcessAnalysisHeadingAcc = lab.concat(e.active["0"]._chart.config.data.labels[e.active["0"]._index]);
                    this.subProcessAnalysisHeadingAcc = "Currency";
                    this.accuniqueCurrencyCode.forEach((element,i) => {
                        if(element == filterValues){
                            this.accCurrencyTab = i+1;
                        }
                    });
                } else {
                    //console.log('e.length ' + e.length);
                    if (e != undefined && e.active && !e.active[0]) {
                        this.accSelectedViewRS.push(this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName);
                        if (val) {
                            type = val;
                            this.accuniqueCurrencyCode = this.pieChartActivityAccountingLabels;
                            this.accCurrencyTab = 0;
                        } else {
                            type = this.accSelectedGrpByTab;
                        }
                    }else if (e) {
                        if (val) {
                            type = val;
                            this.accuniqueCurrencyCode = this.pieChartGrpCurrencyAccountingLabels;
                        } else {
                            type = this.accSelectedGrpByTab;
                        }
                        filterKey = this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
                        if (e.length) {
                            filterValues = e;
                        } else {
                            filterValues = this.accuniqueCurrencyCode[e-1];
                        }
                    }
                    //type = 'Un-Accounted';
                    this.subProcessAnalysisHeadingAcc = "Currency";
                }
            } else if (type == 'Accounted' || 'Un-Accounted') {
                this.showAccApprovalPer = false;
                if (e != undefined && e.active[0] != undefined) {
                    if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'Label 0') {
                        type = e.active["0"]._model.label;
                        this.accSelectedGrpByTab = type;
                        this.subProcessAnalysisHeadingAcc = type;
                    } else {
                        type = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
                        this.accSelectedGrpByTab = type;
                        this.subProcessAnalysisHeadingAcc = type;
                    }
                } else {
                    this.accSelectedGrpByTab = type;
                    this.subProcessAnalysisHeadingAcc = type;
                }
            }

            //console.log('type ' + type);
            //console.log('this.accRuleGrpIdEncrypt ' + this.accRuleGrpIdEncrypt);
            //console.log('this.accSelectedViewId ' + this.accSelectedViewId);
            //console.log('this.accSelectedType ' + this.accSelectedType);
            //console.log('this.accSelectedViewRS ' + this.accSelectedViewRS);
            this.accStatusTabCol = [];
            this.accColumnOptions = [];
            this.showAccGrpBy = false;
            if(type == 'Un-Accounted'){
                this.accStatusTab = 0;
            }else if(type == 'JE Pending'){
                this.accStatusTab = 1;
            }else{
                this.accStatusTab = 2;
            }
            this.accSelectedViewRS = this.accSelectedViewRS.filter(function (item, i, ar) { return ar.indexOf(item) === i; });
            this.accSelectedViewRS.forEach((element,i) => {
                if(element == 'reconciliation_status'){
                    this.accSelectedViewRS.splice(i,1);
                }
            });
            this.accType = type;
            this.request = this.dashboardService.fetchUnReconciledOrUnAccountedGroupByInfo(this.accRuleGrpIdEncrypt, this.accSelectedViewId, this.accSelectedType, type, this.accSelectedViewRS, this.reconDateRange, violationPer, age, filterKey, filterValues, this.accBucketId).subscribe((res: any) => {
                this.accGrpByDetails = res;
                this.accSelectedViewRS = [];
                if (this.subProcessAnalysisHeadingAcc != "Currency") {
                    this.fetchAccGrpByCurrencyCode();
                    //console.log('this.accuniqueCurrencyCode ' + JSON.stringify(this.accuniqueCurrencyCode));
                    /* var unique = {};
                    this.accuniqueCurrencyCode = [];
                    this.accGrpByDetails.detailList.forEach(element => {
                        if (!unique[element[this.acctempColName]]) {
                            this.accuniqueCurrencyCode.push(element[this.acctempColName]);
                            unique[element[this.acctempColName]] = element;
                        }
                        //  console.log('this.accuniqueCurrencyCode ' + JSON.stringify(this.accuniqueCurrencyCode));
                    });
                    this.fetchAccGrpByCurrencyCode(this.accuniqueCurrencyCode[0]); */
                } else {
                    if (filterValues != null) {
                        this.fetchAccGrpByCurrencyCode(filterValues);
                    } else {
                        this.fetchAccGrpByCurrencyCode();
                    }

                }
                this.accGrpByDetails.columnNames.columnsList.forEach((element, i) => {
                    if (element.length > 4) {
                        if (i < 4) {
                            this.accSelectedViewRS.push(element.columnAliasName);
                        }
                    } else {
                        this.accSelectedViewRS.push(element.columnAliasName);
                    }
                    let obj = { field: element.columnAliasName, header: element.columnName, align: element.align }
                    this.accStatusTabCol.push(obj);
                });
                for (let i = 0; i < this.accStatusTabCol.length; i++) {
                    this.accColumnOptions.push({ label: this.accStatusTabCol[i].header, value: this.accStatusTabCol[i] });
                }
                this.showAccGrpBy = true;
                $('.spinnerBlockTable').removeClass('spinnerBlockTable-icon');
                //console.log('this.accGrpByDetails ' + JSON.stringify(this.accGrpByDetails.detailList));
                //console.log('accStatusTabCol ' + JSON.stringify(this.accStatusTabCol));
                //console.log('accColumnOptions ' + JSON.stringify(this.accColumnOptions));
            });
        }
    }
    accFilteredData: any = [];
    accCurrencyOption: boolean = false;
    fetchAccGrpByCurrencyCode(val?) {
        /* console.log('val ' + val);
        console.log('this.accGrpByDetails.detailList ' + JSON.stringify(this.accGrpByDetails)); */
        if (val) {
            this.accChangedCol = false;
            this.accChangeColor(val);
            this.accCurrencyCode = val;
            this.accFilteredData = [];
            let acctempDetailList = [];
            this.accGrpByDetails.detailList.forEach(element => {
                if (val == element[this.acctempColName]) {
                    acctempDetailList.push(element);
                }
            });
            if (acctempDetailList && acctempDetailList.length) {
                this.accCurrencyOption = true;
                this.accFilteredData['detailList'] = acctempDetailList;
                this.accFilteredData['columnNames'] = this.accGrpByDetails.columnNames;
                //   console.log('this.accFilteredData ' + JSON.stringify(this.accFilteredData));
            } else {
                this.accCurrencyOption = false;
                this.accFilteredData['detailList'] = this.accGrpByDetails.detailList;
                this.accFilteredData['columnNames'] = this.accGrpByDetails.columnNames;
            }
        } else {
            this.accCurrencyOption = false;
            this.accFilteredData['detailList'] = this.accGrpByDetails.detailList;
            this.accFilteredData['columnNames'] = this.accGrpByDetails.columnNames;
        }

    }
    changeGrpByView(type) {
        //console.log('selectedViewRS ' + JSON.stringify(this.selectedViewRS));
        this.reconStatusTabCol.forEach(element => {
            this.reconStatusTabCol.forEach((element1, i) => {
                if (element == element1.field) {

                } else {
                    this.reconStatusTabCol.splice(i, 1);
                }
            });
            this.columnOptions.forEach((element2, j) => {
                if (element == element2.value.field) {

                } else {
                    this.columnOptions.splice(j, 1);
                }
            });
        });
        this.fetchGrpByData(type);
    }
    changeReconGrpTab(e) {
        /* console.log('e ' + e.index); */
        if(e.index == 0){
            this.selectedGrpByTab = 'Un-Reconciled';
        }else{
            this.selectedGrpByTab = 'Reconciled';
        }
        //this.selectedGrpByTab = e.value;
        if (this.selectedGrpByTab == 'Un-Reconciled') {
            this.fetchGrpByData('Un-Reconciled');
        } else {
            this.fetchGrpByData('Reconciled');
        }
    }


    changeAccGrpTab(e) {
        /* console.log('e ' + e.index); */
        if(e.index == 0){
            this.accSelectedGrpByTab = 'Un-Accounted';
        }else if(e.index == 1){
            this.accSelectedGrpByTab = 'JE Pending';
        }else{
            this.accSelectedGrpByTab = 'JE Created';
        }
       // this.accSelectedGrpByTab = e.value;
        this.accFetchGrpByData(this.accSelectedGrpByTab);
    }
    /* Function to fetch view combination based on ruleGrpId */
    accViewCombinationSpecificAnalysis: any = [];
    subProcessAnalysisHeadingAcc: any;
    showAccApprovalPer: boolean = false;
    accLoadingTab: boolean = false;
    fetchAccountingCombViews(type?, e?) {
        this.accLoadingTab = true;
        if (type == 'aging') {
            if (e.active[0] != undefined) {
                this.showAccApprovalPer = false;
                let aging = e.active["0"]._model.label;
                let lab = "Aging ";
                this.subProcessAnalysisHeadingAcc = lab.concat(aging);
                this.accViewCombinationSpecificAnalysis = [];
                // this.accGetGrpByDetails(1, '88378ED2B0BF7D89CD03A5952BEC9596E5BC6264', 'Source', 'Alpha_Source', 'Un-Accounted');
                this.request = this.dashboardService.fetchAccViewObjAging(this.reconRuleGrpIdEncrypt, type, aging, this.reconBucketId, this.reconDateRange).subscribe((res: any) => {
                    this.accViewCombinationSpecificAnalysis = res;
                    if (this.accViewCombinationSpecificAnalysis.length > 0) {
                        this.accLoadingTab = false;
                        //this.accGetGrpByDetails(this.accViewCombinationSpecificAnalysis[0].viewId, this.accViewCombinationSpecificAnalysis[0].viewIdDisplay, 'Source', this.accViewCombinationSpecificAnalysis[0].viewName, 'Un-Accounted');
                    } else {
                        this.accLoadingTab = false;
                    }
                },
                    (res: Response) => {
                        this.onError(res.json()
                        )
                        this.commonService.error('Error!', 'Error While fetching data view combination data');
                    });
            }
        } else if (type == 'violation') {
            this.showAccApprovalPer = false;
            let violationPer = e;
            let lab = "Violation Period ";
            this.subProcessAnalysisHeadingAcc = lab.concat(e);
            this.accViewCombinationSpecificAnalysis = [];
            // this.accGetGrpByDetails(1, '88378ED2B0BF7D89CD03A5952BEC9596E5BC6264', 'Source', 'Alpha_Source', 'Un-Accounted');
            this.request = this.dashboardService.fetchAccViewObjViolation(this.accRuleGrpIdEncrypt, type, violationPer, this.reconDateRange).subscribe((res: any) => {
                this.accViewCombinationSpecificAnalysis = res;
                if (this.accViewCombinationSpecificAnalysis.length > 0) {
                    this.accLoadingTab = false;
                    // this.accGetGrpByDetails(this.accViewCombinationSpecificAnalysis[0].viewId, this.accViewCombinationSpecificAnalysis[0].viewIdDisplay, 'Source', this.accViewCombinationSpecificAnalysis[0].viewName, 'Un-Accounted');
                } else {
                    this.accLoadingTab = false;
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });
        } else if (type == 'approvals') {
            this.subProcessAnalysisHeadingAcc = "Awaiting Approvals";
            this.accViewCombinationSpecificAnalysis = [];
            this.request = this.dashboardService.fetchReconciliationCombinationViewSpecific(this.accRuleGrpIdEncrypt, 'approvals', this.reconDateRange).subscribe((res: any) => {
                this.accViewCombinationSpecificAnalysis = res;
                this.showAccApprovalPer = true;
                if (this.accViewCombinationSpecificAnalysis.length > 0) {
                    this.accLoadingTab = false;
                    /*  this.accGetGrpByDetails(this.accViewCombinationSpecificAnalysis[0]['combination'][0].viewId, this.accViewCombinationSpecificAnalysis[0]['combination'][0].viewIdDisplay,
                         this.accViewCombinationSpecificAnalysis[0]['combination'][0].viewType, this.accViewCombinationSpecificAnalysis[0]['combination'][0].viewName, 'approvals'); */
                } else {
                    this.accLoadingTab = false;
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });

        } else {
            let typeM;
            if (e != undefined && e.active[0] != undefined) {
                if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'Label 0') {
                    if (e.active["0"]._model.label == 'Un,Accounted') {
                        typeM = 'Un-Accounted';
                    } else if (e.active["0"]._model.label == 'JE,Pending') {
                        typeM = 'JE Pending';
                    } else if (e.active["0"]._model.label == 'JE,Created') {
                        typeM = 'JE Created';
                    }
                    this.accSelectedGrpByTab = typeM;
                } else {
                    if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'Un,Accounted') {
                        typeM = 'Un-Accounted';
                    } else if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'JE,Pending') {
                        typeM = 'JE Pending';
                    } else if (e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label == 'JE,Created') {
                        typeM = 'JE Created';
                    }
                    //typeM = e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label;
                    //console.log('tooltip ' + e.active[0]._chart.config.data.datasets[e.active[0]._chart.tooltip._active[0]._datasetIndex].label);
                    this.accSelectedGrpByTab = typeM;
                }
            } else {
                typeM = 'Un-Accounted';
                this.accSelectedGrpByTab = 'Un-Accounted';
            }
            this.showAccApprovalPer = false;
            this.subProcessAnalysisHeadingAcc = "Accounting Status";
            this.accViewCombinationSpecificAnalysis = [];
            this.request = this.dashboardService.fetchAccViewObj(this.accRuleGrpIdEncrypt, typeM, this.reconDateRange).subscribe((res: any) => {
                this.accViewCombinationSpecificAnalysis = res;
                if (this.accViewCombinationSpecificAnalysis.length > 0) {
                    this.accLoadingTab = false;
                    //this.accGetGrpByDetails(this.accViewCombinationSpecificAnalysis[0].viewId, this.accViewCombinationSpecificAnalysis[0].viewIdDisplay, 'Source', this.accViewCombinationSpecificAnalysis[0].viewName, typeM);
                } else {
                    this.accLoadingTab = false;
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Error!', 'Error While fetching data view combination data');
                });
        }
    }

    setMyStyles(i, type, status) {
        let temp;
        if (status == 'Reconciliation') {
            if (type == 'Amount') {
                temp = this.grpByDetails.detailList[i].avgAmtPer;
            } else {
                temp = this.grpByDetails.detailList[i].avgCtPer;
            }
        } else {
            if (type == 'Amount') {
                temp = this.accGrpByDetails.detailList[i].avgAmtPer;
            } else {
                temp = this.accGrpByDetails.detailList[i].avgCtPer;
            }
        }
        let val = temp + "%";
        let styles = {
            'width': val
        };
        return styles;
    }
    changedCol: boolean = false;
    accChangedCol: boolean = false;
    changeColor(e: any) {
        this.pieChartActivityAccountingLabels.forEach((element, i) => {
            if (element == e) {
                this.changedCol = true;
                let tempCol = this.commonService.randomColors[0]['backgroundColor'][i];
                setTimeout(() => {
                    $('.reconMdBut .mat-button-toggle').css('background-color', 'unset');
                    $('.reconMdBut .mat-button-toggle-checked').css('background-color', this.commonService.randomColors[0]['backgroundColor'][i] + '!important');
                }, 0);
            }
        });
        if (this.changedCol == false) {
            /* setTimeout(() => { */
                $('.reconMdBut .mat-button-toggle').css('background-color', 'unset');
                $('.reconMdBut .mat-button-toggle-checked').css('background-color', '#5574A6');
            /* }, 0); */
        }
    }

    accChangeColor(e: any) {
        /* console.log('e ' + e);
        console.log('this.pieChartGrpCurrencyAccountingLabels ' + JSON.stringify(this.pieChartGrpCurrencyAccountingLabels)); */
        this.pieChartGrpCurrencyAccountingLabels.forEach((element, i) => {
            if (element == e) {
                this.accChangedCol = true;
                let tempCol = this.commonService.randomColors[0]['backgroundColor'][i];
                setTimeout(() => {
                    $('.accMdBut .mat-button-toggle').css('background-color', 'unset');
                    $('.accMdBut .mat-button-toggle-checked').css('background-color', this.commonService.randomColors[0]['backgroundColor'][i] + '!important');
                }, 0);
            }
        });
        if(this.accChangedCol == true){
            /* setTimeout(() => { */
                $('.accMdBut .mat-button-toggle').css('background-color', 'unset');
                $('.accMdBut .mat-button-toggle-checked').css('background-color', '#5574A6');
            /* }, 0); */
        }
    }

    exportGrpByFile(fileType, moduleType) {
        if (moduleType == 'Reconciliation') {
            let tempName = this.reconAllData[this.selectedReconTab].ruleGroupName + "_" + this.selectedCombViewName;
            this.request = this.dashboardService.downloadReconGrpBy(this.reconRuleGrpIdEncrypt, this.reconSelectedViewId, this.reconSelectedType, this.selectedGrpByTab, this.selectedViewRS, this.reconDateRange, fileType).subscribe(
                res => { this.commonService.exportData(res, fileType, tempName); },
                err => this.commonService.error('Warning!', 'Error Occured')
            );
        } else {
            let tempName = this.accountingAllData[this.selectedAccTab].ruleGroupName + "_" + this.accSelectedCombViewName
            this.request = this.dashboardService.downloadReconGrpBy(this.accRuleGrpIdEncrypt, this.accSelectedViewId, this.accSelectedType, this.accSelectedGrpByTab, this.accSelectedViewRS, this.reconDateRange, fileType).subscribe(
                res => { this.commonService.exportData(res, fileType, tempName); },
                err => this.commonService.error('Warning!', 'Error Occured')
            );
        }
    }
    sub: any;

    showActiveCls(type) {
        if (type == 'src') {
            let val = Number(this.selectedTab) + Number(1);
            let temp = ".mat-tab-label:nth-child(" + val + ")";
            //$(".profSrc .mat-tab-labels .mat-tab-label:nth-child(1)").addClass('mat-tab-label-active');​
            $(".profSrc " + temp).addClass('mat-tab-label-active');
        }
    }
    cancelRequest() {
        if (this.requestReconKPI)
            this.requestReconKPI.unsubscribe();
        if (this.requestGrp)
            this.requestGrp.unsubscribe();
        if (this.requestAging)
            this.requestAging.unsubscribe();
        if (this.requestAccKPI)
            this.requestAccKPI.unsubscribe();
        if (this.request1)
            this.request1.unsubscribe();
        if (this.request)
            this.request.unsubscribe();
        if (this.requestCurrency)
            this.requestCurrency.unsubscribe();
    }
    ngOnDestroy() {
        this.ns.remove();
        $('.spinner, .clockwise').removeClass('hidden');
        this.alive = false;
        if (this.requestReconKPI)
            this.requestReconKPI.unsubscribe();
        if (this.requestGrp)
            this.requestGrp.unsubscribe();
        if (this.requestAging)
            this.requestAging.unsubscribe();
        if (this.requestAccKPI)
            this.requestAccKPI.unsubscribe();
        if (this.request1)
            this.request1.unsubscribe();
        if (this.request)
            this.request.unsubscribe();
        if (this.requestCurrency)
            this.requestCurrency.unsubscribe();
    }

    accountingViolationPeriod: any = 2;
    tempAccountingViolationPeriod: any = 2;
    tempreconViolationPeriod: any = 2;
    unReconItemsViolation: any;
    unReconItemsViolationPer: any;
    updateViolationAnalysis(type) {
        let temp = null;
        if (type == 'reconciliation') {
            this.reconViolationPeriod = this.tempreconViolationPeriod;
            this.fetchReconKPI();
        } else {
            this.accountingViolationPeriod = this.tempAccountingViolationPeriod;
            this.fetchAccKPI();
        }
    }
    cancelViolationAnalysis(type) {
        if (type == 'reconciliation') {
            this.tempreconViolationPeriod = this.reconViolationPeriod;
        } else {
            this.tempAccountingViolationPeriod = this.accountingViolationPeriod;
        }
    }
    accType: any;
    navigateToRWQ(currCode){
        if(this.reconType == 'Un-Reconciled'){
            this.commonService.reconDashBoardObject.status = 'unreconciled';
        }else{
            this.commonService.reconDashBoardObject.status = 'reconciled';
        }
        this.commonService.reconDashBoardObject.ruleGroupId = this.reconRuleGrpIdEncrypt;
        this.commonService.reconDashBoardObject.ruleGroupName = this.reconAllData[this.selectedReconTab]['ruleGroupName'];
        this.commonService.reconDashBoardObject.sourceViewId = this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]['combination'][0]['viewIdDisplay'];
        this.commonService.reconDashBoardObject.sourceViewName = this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]['combination'][0]['viewName'];
        this.commonService.reconDashBoardObject.targetViewId = this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]['combination'][1]['viewIdDisplay'];
        this.commonService.reconDashBoardObject.targetViewName = this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]['combination'][1]['viewName'];
        this.commonService.reconDashBoardObject.periodFactor = this.reconViewCombinationSpecificAnalysis[this.selectedReconAnalysis]['combination'][0]['dateColumn'];
        this.commonService.reconDashBoardObject.startDateRange = this.reconDateRange.startDate;
        this.commonService.reconDashBoardObject.endDateRange = this.reconDateRange.endDate;
        this.commonService.reconDashBoardObject.groupBy = "columnName";
        this.commonService.reconDashBoardObject.columnName = this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
        this.commonService.reconDashBoardObject.columnDisplayName = this.viewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifier;
        this.commonService.reconDashBoardObject.columnValue = currCode;
        /* console.log('this.commonService.reconDashBoardObject ' + JSON.stringify(this.commonService.reconDashBoardObject)); */
        let link = ['/reconcilewq', { outlets: { 'content': 'reconcile-details' } }];
        this.router.navigate(link);
    }
    navigateToAWQ(currCode, ind){
        if(this.accountingAllData[this.selectedAccTab]['activityBased'] == true){
            let temp: any;
            if(this.accType == "Un-Accounted" && this.accFilteredData.detailList[ind]['reconciliation_status'] == "Un-Reconciled"){
                temp = "un accounted, not reconciled";
            }else if(this.accType == "Un-Accounted" && this.accFilteredData.detailList[ind]['reconciliation_status'] == "Reconciled"){
                temp = "un accounted, reconciled";
            }else if(this.accType != "Un-Accounted" && this.accFilteredData.detailList[ind]['reconciliation_status'] == "Un-Reconciled"){
                temp = "accounted, not reconciled";
            }else if(this.accType != "Un-Accounted" && this.accFilteredData.detailList[ind]['reconciliation_status'] == "Reconciled"){
                temp = "accounted, reconciled";
            }
            this.commonService.acctDashBoardObject.status = temp;
        }else{
            if(this.accType == "Un-Accounted"){
                this.commonService.acctDashBoardObject.status = "un accounted";
            }else{
                this.commonService.acctDashBoardObject.status = "accounted";
            }
        }
        this.commonService.acctDashBoardObject.ruleGroupId = this.accRuleGrpIdEncrypt;
        this.commonService.acctDashBoardObject.ruleGroupName = this.accountingAllData[this.selectedAccTab]['ruleGroupName'];
        this.commonService.acctDashBoardObject.dataViewId = this.accViewCombinationSpecificAnalysis[this.selectedAccAnalysis]['viewIdDisplay'];
        this.commonService.acctDashBoardObject.dataViewName = this.accViewCombinationSpecificAnalysis[this.selectedAccAnalysis]['viewName'];
        //this.commonService.acctDashBoardObject.status = this.accType;
        this.commonService.acctDashBoardObject.periodFactor = this.accViewCombinationSpecificAnalysis[this.selectedAccAnalysis]['dateColumn'];
        this.commonService.acctDashBoardObject.startDateRange = this.reconDateRange.startDate;
        this.commonService.acctDashBoardObject.endDateRange = this.reconDateRange.endDate;
        this.commonService.acctDashBoardObject.groupBy = "columnName";
        this.commonService.acctDashBoardObject.columnName = this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifierAliasName;
        this.commonService.acctDashBoardObject.columnValue = currCode;
        this.commonService.acctDashBoardObject.columnDisplayName = this.accViewSpecificColumnsGrpBy.currencyCodeQualifier.currencyCodeQualifier;
        /* console.log('this.commonService.acctDashBoardObject ' + JSON.stringify(this.commonService.acctDashBoardObject)); */
        let link = ['/accounting-data', { outlets: { 'content': 'accounting-data-home' } }];
        this.router.navigate(link);
    }
    // changeDateRange(e) {
    ////     console.log('called');
    //     this.daterangepickerOptions = {
    //         startDate: '09/01/2018',
    //         endDate: '09/02/2018',
    //         format: 'DD/MM/YYYY'
    //     }
    // }
}