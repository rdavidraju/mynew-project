import { Component, OnInit, ViewChild, ChangeDetectionStrategy, TemplateRef, Inject } from '@angular/core';
import { CommonService } from '../entities/common.service';
import { NavBarService } from '../layouts/navbar/navbar.service';
import { DashBoard4, ModuleAnalysis, DashBoardValues } from './home.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ng2-webstorage';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { DecimalPipe, DatePipe } from '@angular/common';
import { JhiDateUtils, JhiAlertService } from 'ng-jhipster';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { NotificationsService } from 'angular2-notifications-lite';
import { ProcessesService } from '../entities/processes/processes.service';
import Chart from 'chart.js';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'chartjs-plugin-datalabels';
declare var $: any;
declare var jQuery: any;

/* import '../../node_modules/chartjs-plugin-datalabels/dist/chartjs-plugin-datalabels.js'; */
@Component({
    selector: 'jhi-dashboardv5',
    templateUrl: './dashboard-v5.component.html',
    styleUrls: ['home.scss'],
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
    ]
})

export class DashboardV5Component implements OnInit {
    dashBoardLayOutObj = new DashBoardValues();
    dashBoardLayOutObjTemp = new DashBoardValues();
    blockedDocument: boolean = false;
    public lineChartLabelsRecon: Array<any> = [];
    public lineChartLabelsReconDrill: Array<any> = [];
    groupedExtractionTransformation: boolean = false;
    reconciliationAnalysisData: any;
    accountingAnalysisData: any;
    unReconItemsValueKpi: any;
    unReconItemsViolationKpi: any;
    awaitingAppCountKpi: any;
    selectedProcessAnalysis: any = '1 Week';
    selectedReconDayWiseAnalysis: any = '1 Week';
    selectedAccountingDayWiseAnalysis: any = '1 Week';
    topTenUnReconItemData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,73,56,578.97", "Percentage": 14.9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,48,78,278.59", "Percentage": 14.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,18,21,781.67", "Percentage": 13.2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "3,34,40,452.21", "Percentage": 10.5 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,93,171.42", "Percentage": 9.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,16,066.87", "Percentage": 9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,73,33,868.12", "Percentage": 8.6 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,34,94,099.10", "Percentage": 7.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,16,29,831.66", "Percentage": 6.8 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,03,62,004.36", "Percentage": 6.4 }];
    unReconItemByViolationData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 415, "Sum Of Amount": "4,18,21,781.67" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 416, "Sum Of Amount": "2,84,93,171.42" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 417, "Sum Of Amount": "4,56,443.36" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 418, "Sum Of Amount": "59,69,020.66" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconciled By Age(Days)": 419, "Sum Of Amount": "1,62,67,594.22" }];
    awaitingForApprovalData: any = [{ "Ledger Name": "NSPL Primary Ledger", "Provider Name": "BitPay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "6,29,433.22" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "BitPay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "27,92,308" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Paytm", "Approval Status": "Awaiting for approval", "Sum Of Amount": "5,26,50,118.71" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "75,37,168.57" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "1,77,670.04" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": 148.18 }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "1,17,70,606.19" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "2,07,873.91" }];
    tableView1: boolean = false;
    //userDetails: any;
    processesList: any;
    testReconWidth: any = '300';
    testReconHeight: any = '300';
    processFilter = new DashBoard4();
    tempProcessFilter = new DashBoard4();
    moduleAnalysisList = new ModuleAnalysis();
    selectedTab: any = 0;
    selectedTabReconGroup: any = 0;
    selectedAccTab: any = 0;
    selectedAccTabActivity: any = 0;
    currentModule: any = "Reconciliation Status";
    currentAccModule: any = 'Accounting Status';
    selectedViewRSAging: any = "Aging Bucket 4";
    selectedUnReconItemsValue: any = 'Top 10 by amount';
    selectedViolationType: any = "Days";
    selectedViolationOperatorType: any = "Greater Than Or Equal To";
    selectedViolationTypeValue: any = 415;
    topTenUnReconValue: any = 26;
    topTenUnReconValueSub: any = "26%"
    topTenUnReconValueTitle: any = "317M"
    topTenUnReconViolation: any = 100;
    topTenUnReconViolationTitle: any = "5";
    awaitingApproval: any = 100;
    awaitingApprovalCount: any = "8";
    selectedUnReconItemsViolationValue: any = ">= 415";
    selectedReconStatus: any = "Un-Reconciled";
    selectedAccountingStatus: any = "Not Accounted";
    ruleGrpId: any;
    reconDetailedList: any;
    recongroupBy: any;
    selectedReconGroupBy: any;
    reconGroupByData: any;
    selectedTabProcessWiseAnalysisAcc = 0;
    selectedTabProcessWiseAnalysisAccJE = 0;
    customizationMode: boolean = false;
    reconFilterBy: any = ["Amount", "Count"];
    selectedReconFilterValue = "Amount";
    selectedAccFilterValue = "Amount";
    showDrillDown: boolean = false;
    selectedAccountType: any = "JE Creation";
    reconApprovalData: any = [
        {
            "user": "Nishanth",
            "count": 4562
        },
        {
            "user": "Raj",
            "count": 1654
        },
        {
            "user": "Sameer",
            "count": 1974
        }
    ];
    showAccProcess: boolean = false;

    public extractionAnalysisColors: Array<any> = [
        {
            backgroundColor: 'transparent',
            borderColor: 'rgba(76, 101, 197, 0.9)',
            pointBackgroundColor: 'rgba(76, 101, 197, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(76, 101, 197, 0.5)'
        },
        {
            backgroundColor: 'transparent',
            borderColor: 'rgba(91, 201, 241, 0.9)',
            pointBackgroundColor: 'rgba(91, 201, 241, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(91, 201, 241, 0.5)'
        }
    ];

    public transformationAnalysisColors: Array<any> = [
        {
            backgroundColor: 'transparent',
            borderColor: 'rgba(92, 203, 173, 0.9)',
            pointBackgroundColor: 'rgba(92, 203, 173, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(92, 203, 173, 0.5)'
        },
        {
            backgroundColor: 'transparent',
            borderColor: 'rgba(166, 231, 80, 0.9)',
            pointBackgroundColor: 'rgba(166, 231, 80, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(166, 231, 80, 0.5)'
        }
    ];
    // ccReconProcess: any = [ { "Date": "11-Mar", "Extraction": 80, "Transformation": 75, "Reconciliation": 40, "Accounting": 50, "Journals": 50, "Total": 59 }, { "Date": "10-Mar", "Extraction": 100, "Transformation": 90, "Reconciliation": 80, "Accounting": 100, "Journals": 83, "Total": 91 }, { "Date": "09-Mar", "Extraction": 100, "Transformation": 100, "Reconciliation": 80, "Accounting": 55, "Journals": 90, "Total": 85 }, { "Date": "08-Mar", "Extraction": 100, "Transformation": 100, "Reconciliation": 100, "Accounting": 100, "Journals": 63, "Total": 93 }, { "Date": "07-Mar", "Extraction": 100, "Transformation": 100, "Reconciliation": 100, "Accounting": 100, "Journals": 100, "Total": 100 } ];
    ccReconProcess: any = [];
    public extractionAndTransformationData: Array<any> = [/* 
        { data: [65, 59, 80, 81, 56, 55, 40], label: 'Extracted', hidden: true },
        { data: [28, 48, 40, 19, 86, 27, 90], label: 'Extraction Failed' },
        { data: [18, 48, 77, 9, 100, 27, 40], label: 'Transformed' },
        { data: [45, 75, 70, 49, 35, 65, 75], label: 'Transformation Failed' } */
    ];
    public extractionAndTransformationLabels: Array<any> = [/* "09 Mar", "08 Mar", "07 Mar", "06 Mar", "05 Mar", "04 Mar", "03 Mar" */];
    public extractionAndTransformationOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom'
        },
        scales: {
            yAxes: [
                {
                    ticks: {
                        min: 0
                    }
                }
            ]
        },
        plugins: {
            datalabels: {
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && context.active);
                },
                anchor: 'end',
                align: 'top',
                borderWidth: 1,
                borderRadius: 4,
                /* formatter: function (value, context) {
                    return value + '%';
                }, */
                color: function (context) {
                    console.log('context ' + context);
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :
                        context.datasetIndex == 0 ? 'rgba(76, 101, 197, 0.9)' :
                            context.datasetIndex == 1 ? 'rgba(91, 201, 241, 0.9)' :
                                context.datasetIndex == 2 ? 'rgba(92, 203, 173, 0.9)' :
                                    'rgba(166, 231, 80, 0.9)';
                }
            }
        }
    };

    /* public extractionAndTransformationColors: Array<any> = [{ backgroundColor: ['rgba(76, 101, 197, 0.9)', 'rgba(91, 201, 241, 0.9)', 'rgba(92, 203, 173, 0.9)', 'rgba(166, 231, 80, 0.9)'] }]; */
    public extractionAndTransformationColors: Array<any> = [
        {
            backgroundColor: 'rgba(76, 101, 197, 0.9)',
            borderColor: 'rgba(76, 101, 197, 0.9)',
            pointBackgroundColor: 'rgba(76, 101, 197, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(76, 101, 197, 0.5)'
        },
        {
            backgroundColor: 'rgba(91, 201, 241, 0.9)',
            borderColor: 'rgba(91, 201, 241, 0.9)',
            pointBackgroundColor: 'rgba(91, 201, 241, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(91, 201, 241, 0.5)'
        },
        {
            backgroundColor: 'rgba(92, 203, 173, 0.9)',
            borderColor: 'rgba(92, 203, 173, 0.9)',
            pointBackgroundColor: 'rgba(92, 203, 173, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(92, 203, 173, 0.5)'
        },
        {
            backgroundColor: 'rgba(166, 231, 80, 0.9)',
            borderColor: 'rgba(166, 231, 80, 0.9)',
            pointBackgroundColor: 'rgba(166, 231, 80, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(166, 231, 80, 0.5)'
        }
    ];
    public extractionAndTransformationLegend: boolean = true;
    public extractionAndTransformationChartType: string = 'bar';

    /* Reconciliation Status*/
    reconColor: any = '#4285f4';/* #8ac9f3 */
    unReconColor: any = '#F44336';/* #fda4b8 */
    reconColorTemp: any = '#4285f4';
    unReconColorTemp: any = '#F44336';
    tempSelectedPostion: any = 'bottom';
    selectedPostion: any = 'bottom';
    reconBackgroundTemp: any = '#ffffff';
    reconBackground: any = '#ffffff';

    public pieChartReconciliation: any[];
    public pieChartReconciliationType: string = 'pie';
    public temppieChartReconciliationType: string = 'pie';
    tempSelectedReconChart: any = 'pie';
    public pieChartReconciliationLegend: boolean = true;
    public pieChartReconciliationLabels: string[];
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
                anchor: 'center',
                formatter: function (value, context) {
                    return value + '%';
                }
            }
        }
    };
    public pieChartReconciliationColors: {}[] = [{ backgroundColor: [this.reconColor, this.unReconColor] }];
    public unReconGrpByColors = [{ backgroundColor: '#f0756a' }];
    accGrpByColors1 = [{ backgroundColor: '#f0756a' }];

    public reconGrpByColors = [{ backgroundColor: '#f0756a' }];
    public reconGroupByChartData: Array<any> = [];
    public reconGroupByChartLabels = [];
    public reconGroupByChartOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return (data.datasets[0].data[tooltipItems.index]).toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");/* data.datasets[tooltipItems.datasetIndex].label + ': ' +  */
                }
            }
        },
        legend: {
            position: 'bottom',
            display: false
        },
        scales: {
            xAxes: [
                {
                    ticks: {
                        min: 0,
                        callback: function (value) {
                            // return value + "%" 
                            if (value >= 1000000000000) {
                                return (value / 1000000000000).toFixed(1).replace(/\.0$/, '') + 'T';
                            }
                            if (value >= 1000000000) {
                                return (value / 1000000000).toFixed(1).replace(/\.0$/, '') + 'B';
                            }
                            if (value >= 1000000) {
                                return (value / 1000000).toFixed(1).replace(/\.0$/, '') + 'M';
                            }
                            if (value >= 1000) {
                                return (value / 1000).toFixed(1).replace(/\.0$/, '') + 'K';
                            }
                            return value;
                        }
                    }
                }
            ]
        },
        plugins: {
            datalabels: {
                display: false
            }
        }
    }
    public reconGroupByChartLegends: boolean = true;
    public reconGroupByChartType: any = 'horizontalBar';
    public approverByChartType: any = 'bar';
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
            yAxes: [
                {
                    ticks: {
                        callback: function (value) {
                            // return value + "%" 
                            if (value >= 1000000000000) {
                                return (value / 1000000000000).toFixed(1).replace(/\.0$/, '') + 'T';
                            }
                            if (value >= 1000000000) {
                                return (value / 1000000000).toFixed(1).replace(/\.0$/, '') + 'B';
                            }
                            if (value >= 1000000) {
                                return (value / 1000000).toFixed(1).replace(/\.0$/, '') + 'M';
                            }
                            if (value >= 1000) {
                                return (value / 1000).toFixed(1).replace(/\.0$/, '') + 'K';
                            }
                            return value;
                        }
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
    public barChartLabels: string[] = ['0-30', '31-60', '61-90', '>90'];
    public barChartType: string = 'bar';
    public tempbarChartType: string = 'bar';
    public barChartLegend: boolean = true;
    public unReconAgingBucketColors: {}[] = [{ backgroundColor: [this.agingColor, this.agingColor, this.agingColor, this.agingColor] }];
    public barChartData: any[] = [
        { data: [544554, 698755, 265987, 874598], label: 'Un-Recon' }
    ];

    /* Un-Recon Items By Value */
    unReconItemsByValueRadius: any = 85;
    unReconItemsByValueOuterStrokeColor: any = '#F44336';
    unReconItemsByValueInnerStrokeColor: any = '#ededed';
    unReconItemsByValueTitleFontSize: any = 40;
    unReconItemsByValueSubtitleFontSize: any = 18;
    unReconItemsByValueTitleColor: any = '#444444';
    tempunReconItemsByValueTitleColor: any = '#444444';
    unReconItemsByValueSubTitleColor: any = '#A9A9A9';
    tempunReconItemsByValueSubTitleColor: any = '#A9A9A9';
    unReconItemsByValueUnitsColor: any = '#A9A9A9';
    tempunReconItemsByValueRadius: any = 85;
    tempunReconItemsByValueOuterStrokeColor: any = '#F44336';
    tempunReconItemsByValueInnerStrokeColor: any = '#ededed';
    tempunReconItemsByValueTitleFontSize: any = 40;
    tempunReconItemsByValueSubtitleFontSize: any = 18;

    /* Un-Recon Items By Violation */
    unReconItemsByViolationRadius: any = 85;
    unReconItemsByViolationOuterStrokeColor: any = '#9C27B0';
    unReconItemsByViolationInnerStrokeColor: any = '#ededed';
    unReconItemsByViolationTitleFontSize: any = 40;
    unReconItemsByViolationSubtitleFontSize: any = 18;
    unReconItemsByViolationTitleColor: any = '#444444';
    tempunReconItemsByViolationTitleColor: any = '#444444';
    unReconItemsByViolationSubTitleColor: any = '#A9A9A9';
    tempunReconItemsByViolationSubTitleColor: any = '#A9A9A9';
    unReconItemsByViolationUnitsColor: any = '#444444';
    tempunReconItemsByViolationRadius: any = 85;
    tempunReconItemsByViolationOuterStrokeColor: any = '#9C27B0';
    tempunReconItemsByViolationInnerStrokeColor: any = '#ededed';
    tempunReconItemsByViolationTitleFontSize: any = 40;
    tempunReconItemsByViolationSubtitleFontSize: any = 18;

    /* Awaiting For Approvals */
    reconAwaitingForApprovalRadius: any = 85;
    reconAwaitingForApprovalOuterStrokeColor: any = '#fd9700';
    reconAwaitingForApprovalInnerStrokeColor: any = '#ededed';
    reconAwaitingForApprovalTitleFontSize: any = 40;
    reconAwaitingForApprovalTitleColor: any = '#444444';
    tempreconAwaitingForApprovalRadius: any = 85;
    tempreconAwaitingForApprovalOuterStrokeColor: any = '#fd9700';
    tempreconAwaitingForApprovalInnerStrokeColor: any = '#ededed';
    tempreconAwaitingForApprovalTitleFontSize: any = 40;
    tempreconAwaitingForApprovalTitleColor: any = '#444444';



    /* Sample JSON For Tables */
    selectedViewRS: any = "rules";
    selectedViewRSUnRecon: any = "currency";

    selectedViewAcc: any = "rules";
    reconciliationStatusData: any = {
        "reconcilied": [{ "Rule Name": "Rule 1", "Ledger Name": "NSPL Primary Ledger", "Count": 687, "Amount": "3,10,82,694.65", "Percentage": 1.2 }, { "Rule Name": "Rule 2", "Ledger Name": "NSPL Global", "Count": 16, "Amount": "3,50,50,955.12", "Percentage": 1.36 }, { "Rule Name": "Rule 3", "Ledger Name": "NSPL Primary Ledger", "Count": "3,421", "Amount": "97,60,04,034.25", "Percentage": 37.78 }, { "Rule Name": "Rule 4", "Ledger Name": "NSPL Primary Ledger", "Count": 7, "Amount": "9,68,43,335.47", "Percentage": 3.75 }, { "Rule Name": "Rule 5", "Ledger Name": "NSPL Primary Ledger", "Count": 15, "Amount": "76,14,03,293.76", "Percentage": 29.47 }, { "Rule Name": "Rule 6", "Ledger Name": "NSPL Primary Ledger", "Count": 11, "Amount": "52,52,27,565.25", "Percentage": 20.33 }, { "Rule Name": "Rule 7", "Ledger Name": "NSPL Primary Ledger", "Count": 6, "Amount": "1,96,96,496.58", "Percentage": 0.76 }, { "Rule Name": "Rule 8", "Ledger Name": "NSPL Global", "Count": 13, "Amount": "8,18,13,840.04", "Percentage": 3.17 }, { "Rule Name": "Rule 9", "Ledger Name": "NSPL Primary Ledger", "Count": 6, "Amount": "5,62,37,015.66", "Percentage": 2.18 }],
        "unReconcilied": [{ "Currency Code": "AUD", "Ledger Name": "NSPL Primary Ledger", "Count": 48, "Amount": "3,51,75,673.73", "Percentage": 7.8 }, { "Currency Code": "BRL", "Ledger Name": "NSPL Primary Ledger", "Count": 79, "Amount": "34,17,83,450.98", "Percentage": 28.03 }, { "Currency Code": "CAD", "Ledger Name": "NSPL Primary Ledger", "Count": 2, "Amount": "39,11,852.17", "Percentage": 0.32 }, { "Currency Code": "CHF", "Ledger Name": "NSPL Primary Ledger", "Count": 6, "Amount": "2,84,104.86", "Percentage": 0.02 }, { "Currency Code": "CZK", "Ledger Name": "NSPL Primary Ledger", "Count": 2, "Amount": "23,99,779.13", "Percentage": 0.2 }, { "Currency Code": "DKK", "Ledger Name": "NSPL Primary Ledger", "Count": 7, "Amount": "2,219.61", "Percentage": 0 }, { "Currency Code": "EUR", "Ledger Name": "NSPL Primary Ledger", "Count": 58, "Amount": "22,26,406.54", "Percentage": 0.18 }, { "Currency Code": "GBP", "Ledger Name": "NSPL Primary Ledger", "Count": 10, "Amount": "36,89,790.77", "Percentage": 0.3 }, { "Currency Code": "HKD", "Ledger Name": "NSPL Primary Ledger", "Count": 9, "Amount": "62,86,578.10", "Percentage": 0.52 }, { "Currency Code": "INR", "Ledger Name": "NSPL Primary Ledger", "Count": 59, "Amount": "69,37,81,715.82", "Percentage": 56.89 }]
    };
    unReconAgingBucketData: any = [{ "Age": "0 to 30 days", "Count": 9, "Amount": "62,86,578.10", "Percentage": 0.52 }, { "Age": "31 to 60 days", "Count": 33, "Amount": "9,75,75,542.86", "Percentage": 8 }, { "Age": "61 to 90 days", "Count": 119, "Amount": "1,46,09,563.91", "Percentage": 1.2 }, { "Age": "above 90 days", "Count": 540, "Amount": "1,10,10,76,820", "Percentage": 90.28 }];
    /* topTenUnReconItemData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,73,56,578.97", "Percentage": 14.9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,48,78,278.59", "Percentage": 14.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,18,21,781.67", "Percentage": 13.2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "3,34,40,452.21", "Percentage": 10.5 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,93,171.42", "Percentage": 9.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,16,066.87", "Percentage": 9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,73,33,868.12", "Percentage": 8.6 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,34,94,099.10", "Percentage": 7.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,16,29,831.66", "Percentage": 6.8 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,03,62,004.36", "Percentage": 6.4 }];
    unReconItemByViolationData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 415, "Sum Of Amount": "4,18,21,781.67" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 416, "Sum Of Amount": "2,84,93,171.42" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 417, "Sum Of Amount": "4,56,443.36" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 418, "Sum Of Amount": "59,69,020.66" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 419, "Sum Of Amount": "1,62,67,594.22" }];
    awaitingForApprovalData: any = [{ "Ledger Name": "NSPL Primary Ledger", "Provider Name": "BitPay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "6,29,433.22" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "BitPay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "27,92,308" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Paytm", "Approval Status": "Awaiting for approval", "Sum Of Amount": "5,26,50,118.71" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "75,37,168.57" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "1,77,670.04" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": 148.18 }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "1,17,70,606.19" }, { "Ledger Name": "NSPL Primary Ledger", "Provider Name": "Interpay", "Approval Status": "Awaiting for approval", "Sum Of Amount": "2,07,873.91" }]; */


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

    public pieChartAccounting: any[] = [61, 18, 21];
    public pieChartAccountingType: string = 'pie';
    public temppieChartAccountingType: string = 'pie';
    public pieChartAccountingLegend: boolean = true;
    public pieChartAccountingLabels: string[] = ['Accounted', 'In Process', 'Un-Accounted'];
    public pieChartAccountingOptions: any = {
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
    public pieChartAccountingColors: {}[] = [{ backgroundColor: ['#34A853', '#2196F3', '#ea4335'] }];

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

    public pieChartActivityAccounting: any[] = [72, 28];
    public pieChartActivityAccountingType: string = 'pie';
    public temppieChartActivityAccountingType: string = 'pie';
    public pieChartActivityAccountingLegend: boolean = true;
    public pieChartActivityAccountingLabels: string[] = ['Accounted For Un-Identified', 'Accounted For Identified'];
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
    public unAccountedAgingBucketColors: {}[] = [{ backgroundColor: [this.agingColor, this.agingColor, this.agingColor, this.agingColor] }];
    public pieChartActivityColors: {}[] = [{ backgroundColor: ['#FFBB00', '#34A853'] }];
    /* public pieChartActivityAccountingColors: {}[] = [{ backgroundColor: [this.reconColor, this.unReconColor] }]; */

    /* Test Multi Axis */

    /* public testDataSets: any[] = [{
            data: [20, 50, 100, 75, 25, 0],
            label: 'Left dataset',

            // This binds the dataset to the left y axis
            yAxisID: 'left-y-axis'
        }, {
            data: [0.1, 0.5, 1.0, 2.0, 1.5, 0],
            label: 'Right dataset',

            // This binds the dataset to the right y axis
            yAxisID: 'right-y-axis',
        }];
    public testLabels: string[] = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
    public testOptions: any = {
        scales: {
            yAxes: [{
                id: 'left-y-axis',
                type: 'linear',
                position: 'left'
            }, {
                id: 'right-y-axis',
                type: 'linear',
                position: 'right'
            }]
        }
    }; */

    dayWiseSampleData: any = [{
        "1W": { "labelValue": ["09 Mar", "08 Mar", "07 Mar", "06 Mar", "05 Mar", "04 Mar", "03 Mar"], "dataValues": [65, 59, 80, 81, 56, 55, 40], "unReconItemsValue": 317, "unReconItemsViolation": 5, "awaitingAppCount": 8 },
        "2W": { "labelValue": ["09 Mar", "08 Mar", "07 Mar", "06 Mar", "05 Mar", "04 Mar", "03 Mar", "02 Mar", "01 Mar", "28 Feb", "27 Feb", "26 Feb", "25 Feb", "24 Feb"], "dataValues": [87, 78, 85, 59, 68, 75, 85, 65, 59, 80, 81, 56, 55, 40], "unReconItemsValue": 425, "unReconItemsViolation": 17, "awaitingAppCount": 14 },
        "1M": { "labelValue": ["09 Mar", "07 Mar", "05 Mar", "03 Mar", "01 Mar", "27 Feb", "25 Feb", "23 Feb", "21 Feb", "19 Feb", "17 Feb", "15 Feb", "13 Feb", "11 Feb"], "dataValues": [56, 98, 65, 45, 75, 48, 58, 75, 85, 65, 75, 85, 47, 78], "unReconItemsValue": 198, "unReconItemsViolation": 16, "awaitingAppCount": 10 },
        "3M": { "labelValue": ["09 Mar", "03 Mar", "25 Feb", "19 Feb", "13 Feb", "07 Feb"], "dataValues": [65, 59, 80, 81, 56, 55, 40], "unReconItemsValue": 750, "unReconItemsViolation": 34, "awaitingAppCount": 41 }
    },
    {
        "1W": { "labelValue": ["09 Mar", "08 Mar", "07 Mar", "06 Mar", "05 Mar", "04 Mar", "03 Mar"], "dataValues": [35, 41, 20, 19, 44, 45, 60], "unReconItemsValue": 317, "unReconItemsViolation": 5, "awaitingAppCount": 8 },
        "2W": { "labelValue": ["09 Mar", "08 Mar", "07 Mar", "06 Mar", "05 Mar", "04 Mar", "03 Mar", "02 Mar", "01 Mar", "28 Feb", "27 Feb", "26 Feb", "25 Feb", "24 Feb"], "dataValues": [13, 22, 15, 41, 32, 25, 15, 35, 41, 20, 19, 44, 45, 60], "unReconItemsValue": 425, "unReconItemsViolation": 17, "awaitingAppCount": 14 },
        "1M": { "labelValue": ["09 Mar", "07 Mar", "05 Mar", "03 Mar", "01 Mar", "27 Feb", "25 Feb", "23 Feb", "21 Feb", "19 Feb", "17 Feb", "15 Feb", "13 Feb", "11 Feb"], "dataValues": [44, 2, 35, 55, 25, 52, 42, 25, 15, 35, 25, 15, 53, 22], "unReconItemsValue": 198, "unReconItemsViolation": 16, "awaitingAppCount": 10 },
        "3M": { "labelValue": ["09 Mar", "03 Mar", "25 Feb", "19 Feb", "13 Feb", "07 Feb"], "dataValues": [35, 41, 20, 19, 44, 45, 60], "unReconItemsValue": 750, "unReconItemsViolation": 34, "awaitingAppCount": 41 }
    }];/* [{
	"1W":{"labelValue":["09 Mar","08 Mar","07 Mar","06 Mar","05 Mar","04 Mar","03 Mar"],"dataValues":[65, 59, 80, 81, 56, 55, 40]},
	"2W":{"labelValue":["09 Mar","08 Mar","07 Mar","06 Mar","05 Mar","04 Mar","03 Mar","02 Mar","01 Mar","28 Feb","27 Feb","26 Feb","25 Feb","24 Feb"],"dataValues":[87,78,85,59,68,75,85,65,59,80,81,56,55,40]},
	"1M":{"labelValue":["09 Mar","07 Mar","05 Mar","03 Mar","01 Mar","27 Feb","25 Feb","23 Feb","21 Feb","19 Feb","17 Feb","15 Feb","13 Feb","11 Feb"],"dataValues":[56,98,65,45,75,48,58,75,85,65,75,85,47,78]},
	"3M":{"labelValue":["09 Mar","03 Mar","25 Feb","19 Feb","13 Feb","07 Feb"],"dataValues":[65, 59, 80, 81, 56, 55, 40]}
},
{
	"1W":{"labelValue":["09 Mar","08 Mar","07 Mar","06 Mar","05 Mar","04 Mar","03 Mar"],"dataValues":[35, 41, 20, 19, 44, 45, 60]},
	"2W":{"labelValue":["09 Mar","08 Mar","07 Mar","06 Mar","05 Mar","04 Mar","03 Mar","02 Mar","01 Mar","28 Feb","27 Feb","26 Feb","25 Feb","24 Feb"],"dataValues":[13,22,15,41,32,25,15,35,41,20,19,44,45,60]},
	"1M":{"labelValue":["09 Mar","07 Mar","05 Mar","03 Mar","01 Mar","27 Feb","25 Feb","23 Feb","21 Feb","19 Feb","17 Feb","15 Feb","13 Feb","11 Feb"],"dataValues":[44,2,35,55,25,52,42,25,15,35,25,15,53,22]},
	"3M":{"labelValue":["09 Mar","03 Mar","25 Feb","19 Feb","13 Feb","07 Feb"],"dataValues":[35, 41, 20, 19, 44, 45, 60]}
}]; */

    public lineChartDataDrill: Array<any> = [];
    public lineChartData: Array<any> = [
        /* {data: [65, 59, 80, 81, 56, 55, 40], label: 'Series A'},
        {data: [28, 48, 40, 19, 86, 27, 90], label: 'Series B'},
        {data: [18, 48, 77, 9, 100, 27, 40], label: 'Series C'} */
    ];
    public lineChartDataAccDrill: Array<any> = [];
    public lineChartDataAcc: Array<any> = [];
    public lineChartDataExtraction: Array<any> = [];
    public lineChartDataExtractionDrillDown: Array<any> = [];
    public lineChartDataTransformation: Array<any> = [];
    public lineChartDataTransformationDrillDown: Array<any> = [];
    public lineChartDataJournals: Array<any> = [];
    public lineChartLabelsTransformation: Array<any> = [];
    public lineChartLabelsTransformationDrillDown: Array<any> = [];
    public lineChartLabelsExtraction: Array<any> = [];
    public lineChartLabelsExtractionDrillDown: Array<any> = [];
    public lineChartLabels: Array<any> = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
    public lineChartLabelsAccDrill: Array<any> = [];
    public lineChartLabelsAcc: Array<any> = [/* 'January', 'February', 'March', 'April', 'May', 'June', 'July' */];
    public lineChartLabelsJE: Array<any> = [/* 'January', 'February', 'March', 'April', 'May', 'June', 'July' */];
    public reconChartType: string = 'line';
    public lineChartOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        },
        /* tooltips: {
            mode: 'y',
            axis: 'x'
        }, */
        scales: {
            yAxes: [
                {
                    ticks: {
                        min: 0,
                        max: 100,
                        callback: function (value) { return value + "%" }
                    }
                }
            ]
        },
        legend: {
            position: 'bottom'
        },
        plugins: {
            datalabels: {

                /* aspectRatio: 4/3,
                layout: {
                    padding: {
                        top: 42,
                        right: 16,
                        bottom: 32,
                        left: 8
                    }
                }, */
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && context.active);
                },
                //color: 'black',
                /* font: {
                    weight: 'bold'
                }, */
                anchor: 'end',
                align: 'top',
                borderWidth: 1,
                borderRadius: 4,
                /* offset: '5px' *//* ,*/
                formatter: function (value, context) {
                    return value + '%';
                },
                color: function (context) {
                    console.log('context ' + context);
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :  // draw negative values in red
                        context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green

                            'rgba(52, 168, 83, 0.9)';

                }
            }
        },
    };
    public lineChartOptionsAcc: any = {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        },
        scales: {
            yAxes: [
                {
                    ticks: {
                        min: 0,
                        max: 100,
                        callback: function (value) { return value + "%" }
                    }
                }
            ]
        },
        legend: {
            position: 'bottom'
        },
        plugins: {
            datalabels: {
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && context.active);
                },
                anchor: 'end',
                align: 'top',
                borderWidth: 1,
                borderRadius: 4,
                formatter: function (value, context) {
                    return value + '%';
                },
                color: function (context) {
                    console.log('context ' + context);
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :  // draw negative values in red
                        context.datasetIndex == 0 ? 'rgba(234, 67, 53, 0.9)' :    // else, alternate values in blue and green
                            context.datasetIndex == 1 ? 'rgba(251,188,5, 0.9)' :
                                'rgba(52, 168, 83, 0.9)';

                }
            }
        },
    };
    public approversChartOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            xAxes: [
                {
                    stacked: false /* ,
                display:true , */
                }
            ],
            yAxes: [
                {
                    stacked: false,
                    scaleLabel: {
                        display: true,
                        labelString: 'Count'
                    },
                    ticks: {
                        min: 0/* ,
                    max: 100,
                    callback: function (value) { return value + "%" } */
                    }
                }
            ]
        },
        legend: {
            position: 'bottom',
            display: false
        },
        title: {
            display: false,
            text: 'Extraction',
            position: 'top'
        },
        /*  plugins: {
             datalabels: {
                 display: true,
                 align: 'top',
                 borderWidth: 1,
                 borderRadius: 4,
                 color: function (context) {
                     console.log('context ' + context);
                     var index = context.dataIndex;
                     var value = context.dataset.data[index];
                     return value < 0 ? 'red' :  // draw negative values in red
                         context.datasetIndex == 1 ? 'red' :    // else, alternate values in blue and green
                             'green';
 
                 }
             }
         }, */
        plugins: {
            datalabels: {
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && context.active);
                },
                anchor: 'end',
                align: 'top',
                borderWidth: 1,
                borderRadius: 4,
                /* formatter: function (value, context) {
                    return value + '%';
                }, */
                color: function (context) {
                    console.log('context ' + context);
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :
                        context.datasetIndex == 0 ? 'rgba(76, 101, 197, 0.9)' :

                            'rgba(91, 201, 241, 0.9)';

                }
            }
        },
    };
    public lineChartExtractionOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            xAxes: [
                {
                    stacked: false /* ,
                display:true , */
                }
            ],
            yAxes: [
                {
                    ticks: {
                        min: 0
                    },
                    stacked: false,
                    scaleLabel: {
                        display: true,
                        labelString: 'Runs'
                    }
                }
            ]
        },
        legend: {
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Extraction',
            position: 'top'
        },
        /*  plugins: {
             datalabels: {
                 display: true,
                 align: 'top',
                 borderWidth: 1,
                 borderRadius: 4,
                 color: function (context) {
                     console.log('context ' + context);
                     var index = context.dataIndex;
                     var value = context.dataset.data[index];
                     return value < 0 ? 'red' :  // draw negative values in red
                         context.datasetIndex == 1 ? 'red' :    // else, alternate values in blue and green
                             'green';
 
                 }
             }
         }, */
        plugins: {
            datalabels: {
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && context.active);
                },
                anchor: 'end',
                align: 'top',
                borderWidth: 1,
                borderRadius: 4,
                /* formatter: function (value, context) {
                    return value + '%';
                }, */
                color: function (context) {
                    console.log('context ' + context);
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :
                        context.datasetIndex == 0 ? 'rgba(76, 101, 197, 0.9)' :

                            'rgba(91, 201, 241, 0.9)';

                }
            }
        },
    };
    public lineChartTransformationOptions: any = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            xAxes: [
                {
                    stacked: false /* ,
                display:true , */
                    ,
                    ticks: {
                        min: 0/* ,
                    max: 100,
                    callback: function (value) { return value + "%" } */
                    }
                }
            ],
            yAxes: [
                {
                    stacked: false,
                    scaleLabel: {
                        display: true,
                        labelString: 'Files'
                    },
                    ticks: {
                        min: 0/* ,
                    max: 100,
                    callback: function (value) { return value + "%" } */
                    }
                }
            ]
        },
        legend: {
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Transformation',
            position: 'top'
        },
        plugins: {
            datalabels: {
                display: function (context) {
                    return (context.dataset.data[context.dataIndex] > 0 && context.active);
                },
                anchor: 'end',
                align: 'top',
                borderWidth: 1,
                borderRadius: 4,
                /* formatter: function (value, context) {
                    return value + '%';
                }, */
                color: function (context) {
                    console.log('context ' + context);
                    var index = context.dataIndex;
                    var value = context.dataset.data[index];
                    return value < 0 ? 'green' :
                        context.datasetIndex == 0 ? 'rgba(92, 203, 173, 0.9)' :

                            'rgba(166, 231, 80, 0.9)';

                }
            }
        },
    };
    public lineChartColorsAccounting: Array<any> = [
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
            borderColor: 'rgba(251,188,5, 0.9)',
            pointBackgroundColor: 'rgba(251,188,5, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(251,188,5, 0.5)'
        },
        {
            backgroundColor: 'transparent',
            borderColor: 'rgba(52, 168, 83, 0.9)',
            pointBackgroundColor: 'rgba(52, 168, 83, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(52, 168, 83, 0.5)'
        }

        /* { 
          backgroundColor: 'rgba(208, 241, 239, 0.5)',
          borderColor: 'rgba(208, 241, 239, 0.9)',
          pointBackgroundColor: 'rgba(208, 241, 239, 0.5)',
          pointBorderColor: '#fff',
          pointHoverBackgroundColor: '#fff',
          pointHoverBorderColor: 'rgba(208, 241, 239, 0.5)'
        },
        { 
          backgroundColor: 'rgba(164, 223, 219, 0.5)',
          borderColor: 'rgba(164, 223, 219, 0.9)',
          pointBackgroundColor: 'rgba(164, 223, 219, 0.5)',
          pointBorderColor: '#fff',
          pointHoverBackgroundColor: '#fff',
          pointHoverBorderColor: 'rgba(164, 223, 219, 0.5)'
        }  */
    ];
    public reconChartColors: Array<any> = [
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
            borderColor: 'rgba(52, 168, 83, 0.9)',
            pointBackgroundColor: 'rgba(52, 168, 83, 0.5)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(52, 168, 83, 0.5)'
        }
    ];
    public lineChartLegend: boolean = true;
    public lineChartLegendAcc: boolean = true;
    public lineChartLegendJE: boolean = true;
    public lineChartType: string = 'line';
    public lineChartTypeJournals: string = 'line';

    constructor(
        public dialog: MdDialog,
        private commonService: CommonService,
        private navbarService: NavBarService,
        private config: NgbDatepickerConfig,
        private $sessionStorage: SessionStorageService,
        private datePipe: DatePipe,
        private dateUtils: JhiDateUtils,
        private notificationsService: NotificationsService,
        private alertService: JhiAlertService,
        private processesService: ProcessesService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };

    }

    changeMinDate() {
        this.config.minDate = this.processFilter.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    selectedProcess: any;
    showRecon: boolean = false;
    testshow: boolean = false;
    extractionValue: any;
    transformationValue: any;
    reconciliationValue: any;
    accountingValue: any;
    journalsValue: any;
    selectedTabCCProcess: any;
    goToAWQ() {
        this.commonService.acctDashBoardObject.ruleGroupId = 30;
        this.commonService.acctDashBoardObject.dataViewId = 35;
        this.commonService.acctDashBoardObject.status = 'un accounted';
        this.commonService.acctDashBoardObject.startDateRange = "2018-01-01";
        this.commonService.acctDashBoardObject.endDateRange = "2018-05-01";
        this.commonService.acctDashBoardObject.dataViewName = 'BetaChargesDataViewNew';
    }
    goToRWQ(e) {
        if (e.active[0] != undefined) {
            console.log('e ' + e);
            console.log('this.reconDetailedList.reconciliationData ' + JSON.stringify(this.reconDetailedList));
            console.log('this.selectedTabProcessWiseAnalysis ' + this.selectedTabProcessWiseAnalysis);
            console.log('this.reconDetailedList ' + JSON.stringify(this.reconDetailedList));
            console.log('this.selectedReconViewId ' + this.selectedReconViewId + ' this.selectedReconViewName ' + this.selectedReconViewName);
            console.log('this.selectedReconModuleName ' + this.selectedReconModuleName);
            /*     this.commonService.reconDashBoardObject.ruleGroupId = 5;
                this.commonService.reconDashBoardObject.sourceViewId = 5;
                this.commonService.reconDashBoardObject.status = 'unreconciled';
                this.commonService.reconDashBoardObject.startDateRange = "2018-01-01";
                this.commonService.reconDashBoardObject.endDateRange = "2018-05-01";
                this.commonService.reconDashBoardObject.sourceViewName = 'Nspl_Source'; */
        }
    }
    ngOnInit() {
        $('#md-button-toggle-1').addClass('mat-button-toggle-checked');
        $('.c-inline-editor').removeClass('form-inline');
        this.dashBoardLayOutObj = { "selectedProcess": 1, "etl": { "headerNameTitle": "ETL", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 }, "displaylegend": true, "legendPosition": "bottom" }, "kpi": { "headerNameTitle": "KPI's For", "extraction": { "extractionFailedTitle": "Extraction Failed Items" }, "transformation": { "transFailedTitle": "Transformation Failed Items" } } }, "reconciliation": { "headerNameTitle": "Reconciliation", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 } }, "kpi": { "headerNameTitle": "KPI's For", "unReconItemsTitle": "Un-Reconciled Items By Value", "unReconViolationTitle": "Un-Reconciled Items By Violation", "approvalsTitle": "Awaiting For Approvals" }, "secondLevelProcess": { "viewSpecificTitle": "Process Wise Analysis For" }, "thirdLevelProcess": { "groupSpecificTitle": "Items Details" } }, "accounting": { "headerNameTitle": "Accounting", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 } }, "kpi": { "headerNameTitle": "KPI's For", "unAccountedItemsTitle": "Un-Accounted Items By Value", "unAccountedViolationTitle": "Un-Accounted Items By Violation", "approvalsTitle": "Awaiting For Approvals", "jePendingTitle": "JE Pending" }, "secondLevelProcess": { "viewSpecificTitle": "Process Wise Analysis For" }, "thirdLevelProcess": { "groupSpecificTitle": "Items Details" } }, "journals": { "headerNameTitle": "Journals", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 } }, "kpi": { "headerNameTitle": "KPI's For", "unPostedItemsTitle": "JE Un-Posted Items By Value", "unPostedViolationTitle": "JE Un-Posted Items By Violation" } }, "notifications": { "headerTitle": "Notification(s)", "display": true, "defaultRange": "7Days" }, "dayWiseAnalysisBlock": { "headerTitle": "CC Recon Process", "display": true } };
        this.dashBoardLayOutObjTemp = { "selectedProcess": 1, "etl": { "headerNameTitle": "ETL", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 }, "displaylegend": true, "legendPosition": "bottom" }, "kpi": { "headerNameTitle": "KPI's For", "extraction": { "extractionFailedTitle": "Extraction Failed Items" }, "transformation": { "transFailedTitle": "Transformation Failed Items" } } }, "reconciliation": { "headerNameTitle": "Reconciliation", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 } }, "kpi": { "headerNameTitle": "KPI's For", "unReconItemsTitle": "Un-Reconciled Items By Value", "unReconViolationTitle": "Un-Reconciled Items By Violation", "approvalsTitle": "Awaiting For Approvals" }, "secondLevelProcess": { "viewSpecificTitle": "Process Wise Analysis For" }, "thirdLevelProcess": { "groupSpecificTitle": "Items Details" } }, "accounting": { "headerNameTitle": "Accounting", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 } }, "kpi": { "headerNameTitle": "KPI's For", "unAccountedItemsTitle": "Un-Accounted Items By Value", "unAccountedViolationTitle": "Un-Accounted Items By Violation", "approvalsTitle": "Awaiting For Approvals", "jePendingTitle": "JE Pending" }, "secondLevelProcess": { "viewSpecificTitle": "Process Wise Analysis For" }, "thirdLevelProcess": { "groupSpecificTitle": "Items Details" } }, "journals": { "headerNameTitle": "Journals", "dayWiseAnalysis": { "headerNameTitle": "Day Wise Analysis For", "chartPreference": { "line": 0, "bar": 1 } }, "kpi": { "headerNameTitle": "KPI's For", "unPostedItemsTitle": "JE Un-Posted Items By Value", "unPostedViolationTitle": "JE Un-Posted Items By Violation" } }, "notifications": { "headerTitle": "Notification(s)", "display": true, "defaultRange": "7Days" }, "dayWiseAnalysisBlock": { "headerTitle": "CC Recon Process", "display": true } };
        $(".dashboardV5").css('min-height', (this.commonService.screensize().height - 161) + 'px');
        /* if (this.$sessionStorage.retrieve('userData')) {
            this.userDetails = this.$sessionStorage.retrieve('userData');
        } */
        //this.getCustomizedData();
         this.navbarService.getFormConfigDetails('dashboard', 'customHeadings', 'user').subscribe((res: any) => {
             let temp = JSON.parse(res.value);
             this.dashBoardLayOutObj = temp;
             this.dashBoardLayOutObjTemp = temp;
             console.log('dashBoardLayOutObj ' + this.dashBoardLayOutObj);
         });

        if (this.dashBoardLayOutObj != undefined) {

            this.selectedTabCCProcess = 0;


            this.fetchProcessessList();
            /* this.extractionValue = this.ccReconProcess[this.selectedTab].Extraction;
            this.transformationValue = this.ccReconProcess[this.selectedTab].Transformation;
            this.reconciliationValue = this.ccReconProcess[this.selectedTab].Reconciliation;
            this.accountingValue = this.ccReconProcess[this.selectedTab].Accounting;
            this.journalsValue = this.ccReconProcess[this.selectedTab].Journals; */
            /* if(this.testLabels.length>0){
                this.testshow = true;
            } */
            //this.processesList = [{"id":1,"processName":"Test Process"},{"id":2,"processName":"Test Process 2"},{"id":3,"processName":"Test Process 3"},{"id":4,"processName":"Test Process 4"},{"id":5,"processName":"Test Process 5"},{"id":6,"processName":"Test Process 6"},{"id":7,"processName":"Test Process 7"},{"id":8,"processName":"Test Process 8"}];

            console.log('reconStatusTest ::' + $("#reconStatusTest").width());
            console.log('reconAgingTest ::' + $("#reconAgingTest").width());
            /* this.testReconWidth = $("#reconStatusTest").width();
            this.testReconHeight = $("#reconStatusTest").height(); */
            this.pieChartReconciliationLabels = ['Reconciled', 'Un-Reconciled'];
            this.pieChartReconciliation = [86, 14];
            if (this.pieChartReconciliation.length > 0) {
                this.showRecon = true;
            }
            if (this.pieChartAccounting.length > 0) {
                this.showAccountingStatus = true;
            }
            if (this.pieChartActivityAccounting.length > 0) {
                this.showActivityAccountingStatus = true;
            }
            // this.lineChartData = [{ data : this.dayWiseSampleData[0]['1W'].dataValues, label: 'Reconcilied'},{ data : this.dayWiseSampleData[1]['1W'].dataValues, label: 'Un-Reconcilied'}];

            //  this.lineChartDataAcc = [{ data: this.dayWiseSampleData[0]['1W'].dataValues, label: 'Accounted' }, { data: this.dayWiseSampleData[1]['1W'].dataValues, label: 'Un-Accounted' }];
            /* this.lineChartDataJournals = [{ data: this.dayWiseSampleData[0]['1W'].dataValues, label: 'JE Posted' }, { data: this.dayWiseSampleData[1]['1W'].dataValues, label: 'JE Un-Posted' }]; */

            this.lineChartLabels = this.dayWiseSampleData[0]['1W'].labelValue;
            this.lineChartLabelsAcc = this.dayWiseSampleData[0]['1W'].labelValue;
            /* this.lineChartLabelsJE = this.dayWiseSampleData[0]['1W'].labelValue; */
            /* this.unReconItemsValueKpi = this.dayWiseSampleData[0]['1W'].unReconItemsValue;
            this.unReconItemsViolationKpi = this.dayWiseSampleData[0]['1W'].unReconItemsViolation;
            this.awaitingAppCountKpi = this.dayWiseSampleData[0]['1W'].awaitingAppCount; */
            $('.transformationStatusCls, .reconciliationStatusCls, .accountingStatusCls, .journalsStatusCls').removeClass('sideProgessActive');
            $('.extractionStatusCls').addClass('sideProgessActive');
            $('.class1W').css('background-color', 'rgba(197, 202, 233, 0.3)');
            $('.class3W, .class1M, .class3M').css('background-color', 'white');
            this.selectedPeriod = "1 Week";
            //this.reconTabSelected('Un-Reconciled Items By Value');
            if (this.unReconItemsValueKpi != null && this.unReconItemsViolationKpi != null && this.awaitingAppCountKpi != null) {
                $('.count').each(function () {
                    $(this).prop('Counter', 0).animate({
                        Counter: $(this).text()
                    }, {
                            duration: 2000,
                            easing: 'swing',
                            step: function (now) {
                                $(this).text(Math.ceil(now));
                            }
                        });
                });
            }
            this.getNotificationDetails();
            this.moduleType = "All";
            this.dayType = "7Days"
        }
        /*  },
         (res: Response) => {
             this.onError(res.json()
             )
             this.notificationsService.error('Error!', 'Something Went Wrong While Fetching');
             this.blockedDocument = false;
         }); */
        /*    this.reconciliationAnalysisData = {"1W":{"reconAmount":"858K","reconPercentage":78,"unReconAmount":"242K","unReconPercentage":22,"detailedData":{"labelValue":["09 Mar","08 Mar","07 Mar","06 Mar","05 Mar","04 Mar","03 Mar"],"recon":[65,59,80,81,56,55,40],"unRecon":[35,41,20,19,44,45,60]},"unReconItemsValue":317,"unReconItemsViolation":5,"awaitingAppCount":8},"2W":{"reconAmount":"850K","reconPercentage":68,"unReconAmount":"400K","unReconPercentage":32,"detailedData":{"labelValue":["09 Mar","08 Mar","07 Mar","06 Mar","05 Mar","04 Mar","03 Mar","02 Mar","01 Mar","28 Feb","27 Feb","26 Feb","25 Feb","24 Feb"],"recon":[87,78,85,59,68,75,85,65,59,80,81,56,55,40],"unRecon":[13,22,15,41,32,25,15,35,41,20,19,44,45,60]},"unReconItemsValue":425,"unReconItemsViolation":17,"awaitingAppCount":14},"1M":{"reconAmount":"1,320K","reconPercentage":88,"unReconAmount":"180K","unReconPercentage":12,"detailedData":{"labelValue":["09 Mar","07 Mar","05 Mar","03 Mar","01 Mar","27 Feb","25 Feb","23 Feb","21 Feb","19 Feb","17 Feb","15 Feb","13 Feb","11 Feb"],"recon":[56,98,65,45,75,48,58,75,85,65,75,85,47,78],"unRecon":[44,2,35,55,25,52,42,25,15,35,25,15,53,22]},"unReconItemsValue":198,"unReconItemsViolation":16,"awaitingAppCount":10},"3M":{"reconAmount":"4,060K","reconPercentage":58,"unReconAmount":"2,940K","unReconPercentage":42,"detailedData":{"labelValue":["09 Mar","03 Mar","25 Feb","19 Feb","13 Feb","07 Feb"],"recon":[65,59,80,81,56,55,40],"unRecon":[35,41,20,19,44,45,60]},"labelValue":["09 Mar","03 Mar","25 Feb","19 Feb","13 Feb","07 Feb"],"unReconItemsValue":750,"unReconItemsViolation":34,"awaitingAppCount":41}};
        if(this.reconciliationAnalysisData){
            this.selectedTabRec('1W');
            this.lineChartData = [
                                    { data : this.reconciliationAnalysisData['1W'].detailedData.recon, label: 'Reconcilied'},
                                    { data : this.reconciliationAnalysisData['1W'].detailedData.unRecon, label: 'Un-Reconcilied'}
                                ];
            this.lineChartLabels = this.reconciliationAnalysisData['1W'].detailedData.labelValue;
        }    */
        $(window).scroll(function () {
            let offsetExt = $("#extractionDiv").offset();
            if (offsetExt != undefined) {
                let posExt = offsetExt.top - $(window).scrollTop();
                /* let offsetTra = $("#transformationDiv").offset();
                let posTra = offsetTra.top - $(window).scrollTop(); */
                let offsetRec = $("#reconciliationAct").offset();
                let posRecon = offsetRec.top - $(window).scrollTop();
                let offsetAcc = $("#accountingAct").offset();
                let posAcc = offsetAcc.top - $(window).scrollTop();
                /* let offsetJe = $("#journalAct").offset();
                let posAJe = offsetJe.top - $(window).scrollTop(); */
                /* console.log('posExt ' + posExt);
                console.log('posTra ' + posTra);
                console.log('posRecon ' + posRecon);
                console.log('posAcc ' + posAcc);
                console.log('posAJe ' + posAJe);  */
                if (posExt < 280 && posRecon > 290 && posAcc > 65/*  && posAJe > 65 */) {/* posTra>142 && */
                    $('.transformationStatusCls,.reconciliationStatusCls,.accountingStatusCls,.journalsStatusCls').removeClass('sideProgessActive');
                    $('.extractionStatusCls').addClass('sideProgessActive');
                }/* else if(posExt<36 && posTra<125 && posRecon>128 && posAcc>128 && posAJe>128){
                    $('.extractionStatusCls, .reconciliationStatusCls,.accountingStatusCls,.journalsStatusCls').removeClass('sideProgessActive');
                    $('.transformationStatusCls').addClass('sideProgessActive');
                } */else if (posExt < 20 && posRecon < 300 && posAcc > 294/*  && posAJe > 125 */) {/* posTra<52 && */
                    $('.extractionStatusCls, .transformationStatusCls, .accountingStatusCls, .journalsStatusCls').removeClass('sideProgessActive');
                    $('.reconciliationStatusCls').addClass('sideProgessActive');
                } else if (posExt < 20 && posRecon < 20 && posAcc < 294/*  && posAJe > 125 */) {/* posTra<20 && */
                    $('.extractionStatusCls, .transformationStatusCls, .reconciliationStatusCls, .journalsStatusCls').removeClass('sideProgessActive');
                    $('.accountingStatusCls').addClass('sideProgessActive');
                }/*  else if (posExt < 20 && posRecon < 20 && posAcc < 20 && posAJe < 160) {posTra<20 &&
                    $('.extractionStatusCls, .transformationStatusCls, .reconciliationStatusCls, .accountingStatusCls').removeClass('sideProgessActive');
                    $('.journalsStatusCls').addClass('sideProgessActive');
                } */
            }
        });
    }
    moduleType: any = 'All';
    dayType: any = '7Days';
    notificationList: any = [];
    getNotificationDetails() {
        /* console.log('this.userDetails ' + JSON.stringify(this.userDetails)); */
        console.log('moduleType ' + this.moduleType + ' dayType ' + this.dayType);
        /* if (this.userDetails) { */
        this.navbarService.fetchNotificationDetailsForDashBoard(this.moduleType, this.dayType).subscribe((res: any) => {
            if (this.moduleType == 'All') {
                $('.allNoti').addClass("allNotiactive");
                $('.reconNoti').removeClass("reconNotiactive");
                $('.accNoti').removeClass("accNotiactive");
                $('.jourNoti').removeClass("jourNotiactive");
            } else if (this.moduleType == 'RECON') {
                $('.reconNoti').addClass("reconNotiactive");
                $('.allNoti').removeClass("allNotiactive");
                $('.accNoti').removeClass("accNotiactive");
                $('.jourNoti').removeClass("jourNotiactive");
            } else if (this.moduleType == 'ACCOUNTING') {
                $('.accNoti').addClass("accNotiactive");
                $('.allNoti').removeClass("allNotiactive");
                $('.reconNoti').removeClass("reconNotiactive");
                $('.jourNoti').removeClass("jourNotiactive");
            } else if (this.moduleType == 'JOURNAL_ENTRY') {
                $('.jourNoti').addClass("jourNotiactive");
                $('.allNoti').removeClass("allNotiactive");
                $('.reconNoti').removeClass("reconNotiactive");
                $('.accNoti').removeClass("accNotiactive");
            }
            this.notificationList = res;
            console.log('this.notificationList ' + JSON.stringify(this.notificationList));
        });
        /* } */
    }

    displaySelectedProcess(type) {
        console.log('selectedType :' + type);
        if (type == 'extraction') {
            /* $('.transformationStatusCls,.reconciliationStatusCls,.accountingStatusCls,.journalsStatusCls').removeClass('sideProgessActive');
            $('.extractionStatusCls').addClass('sideProgessActive'); */
            $('html, body').animate({
                scrollTop: $("#extractionDiv").offset().top - 105
            }, 2000);
        }/* else if(type == 'transformation'){
            $('.extractionStatusCls, .reconciliationStatusCls,.accountingStatusCls,.journalsStatusCls').removeClass('sideProgessActive');
            $('.transformationStatusCls').addClass('sideProgessActive');
            $('html, body').animate({
                scrollTop: $("#transformationDiv").offset().top - 105
            }, 2000);
        } */else if (type == 'reconciliation') {
            /* $('.extractionStatusCls, .transformationStatusCls, .accountingStatusCls, .journalsStatusCls').removeClass('sideProgessActive');
            $('.reconciliationStatusCls').addClass('sideProgessActive'); */
            $('html, body').animate({
                scrollTop: $("#reconciliationAct").offset().top - 105
            }, 2000);
        } else if (type == 'accounting') {
            /* $('.extractionStatusCls, .transformationStatusCls, .reconciliationStatusCls, .journalsStatusCls').removeClass('sideProgessActive');
            $('.accountingStatusCls').addClass('sideProgessActive'); */
            $('html, body').animate({
                scrollTop: $("#accountingAct").offset().top - 105
            }, 2000);
        } else if (type == 'journals') {
            /* $('.extractionStatusCls, .transformationStatusCls, .reconciliationStatusCls, .accountingStatusCls').removeClass('sideProgessActive');
            $('.journalsStatusCls').addClass('sideProgessActive'); */
            $('html, body').animate({
                scrollTop: $("#journalAct").offset().top - 105
            }, 2000);
        }
    }
    selectedPeriod: any;

    selectedTabRec(type: any) {
        this.unReconItemsValueKpi = this.reconciliationAnalysisData[type].unReconItemsValue;
        this.unReconItemsViolationKpi = this.reconciliationAnalysisData[type].unReconItemsViolation;
        this.awaitingAppCountKpi = this.reconciliationAnalysisData[type].awaitingAppCount;

        if (type == '1W') {
            this.selectedPeriod = "1 Week";
            this.selectedProcessAnalysis = "1 Week";
            this.selectedReconDayWiseAnalysis = "1 Week";
            $('.class1W').css('background-color', 'rgba(197, 202, 233, 0.3)');
            $('.class2W, .class1M, .class3M').css('background-color', 'white');
            /* this.unReconItemsValueKpi = this.reconciliationAnalysisData[type].unReconItemsValue;
            this.unReconItemsViolationKpi = this.reconciliationAnalysisData[type].unReconItemsViolation;
            this.awaitingAppCountKpi = this.reconciliationAnalysisData[type].awaitingAppCount; */
        } else if (type == '2W') {
            this.selectedPeriod = "2 Weeks";
            this.selectedProcessAnalysis = "2 Weeks";
            this.selectedReconDayWiseAnalysis = "2 Weeks";
            $('.class2W').css('background-color', 'rgba(197, 202, 233, 0.3)');
            $('.class1W, .class1M, .class3M').css('background-color', 'white');
            /* this.unReconItemsValueKpi = this.reconciliationAnalysisData[type].unReconItemsValue;
            this.unReconItemsViolationKpi = this.reconciliationAnalysisData[type].unReconItemsViolation;
            this.awaitingAppCountKpi = this.reconciliationAnalysisData[type].awaitingAppCount; */
        } else if (type == '1M') {
            this.selectedPeriod = "1 Month";
            this.selectedProcessAnalysis = "1 Month";
            this.selectedReconDayWiseAnalysis = "1 Month";
            $('.class1M').css('background-color', 'rgba(197, 202, 233, 0.3)');
            $('.class1W, .class2W, .class3M').css('background-color', 'white');
            /* this.unReconItemsValueKpi = this.dayWiseSampleData[0][type].unReconItemsValue;
            this.unReconItemsViolationKpi = this.dayWiseSampleData[0][type].unReconItemsViolation;
            this.awaitingAppCountKpi = this.dayWiseSampleData[0][type].awaitingAppCount; */
        } else if (type == '3M') {
            this.selectedPeriod = "3 Months";
            this.selectedProcessAnalysis = "3 Months";
            this.selectedReconDayWiseAnalysis = "3 Months";
            $('.class3M').css('background-color', 'rgba(197, 202, 233, 0.3)');
            $('.class1W, .class2W, .class1M').css('background-color', 'white');
            /* this.unReconItemsValueKpi = this.dayWiseSampleData[0][type].unReconItemsValue;
            this.unReconItemsViolationKpi = this.dayWiseSampleData[0][type].unReconItemsViolation;
            this.awaitingAppCountKpi = this.dayWiseSampleData[0][type].awaitingAppCount; */
        }
        this.selectedDateRange = type;
        console.log('selected Range ' + type);
        $('.count').each(function () {
            $(this).prop('Counter', 0).animate({
                Counter: $(this).text()
            }, {
                    duration: 2000,
                    easing: 'swing',
                    step: function (now) {
                        $(this).text(Math.ceil(now));
                    }
                });
        });
        /* this.lineChartData = [{ data : this.reconciliationAnalysisData[this.selectedDateRange].dataValues, label: 'Reconcilied'},{ data : this.reconciliationAnalysisData[this.selectedDateRange].dataValues, label: 'Un-Reconcilied'}];
        this.lineChartLabelsRecon = this.reconciliationAnalysisData[this.selectedDateRange].labelValue; */
        this.lineChartData = [
            { data: this.reconciliationAnalysisData[this.selectedDateRange].detailedData.unRecon, label: 'Un-Reconciled' },
            { data: this.reconciliationAnalysisData[this.selectedDateRange].detailedData.recon, label: 'Reconcilied' }
        ];
        this.lineChartLabelsRecon = this.reconciliationAnalysisData[this.selectedDateRange].detailedData.labelValue;

    }
    selectedDateRange: any;
    selectedDateRangeAcc: any;
    selectedDateRangeJe: any;
    toggleRange(e) {
        console.log('range ' + e);
        this.selectedDateRange = e.value;
        console.log('selected Range ' + e.value);
        this.lineChartData = [
            { data: this.reconciliationAnalysisData[this.selectedDateRange].dataValues, label: 'Un-Reconciled' },
            { data: this.reconciliationAnalysisData[this.selectedDateRange].dataValues, label: 'Reconcilied' }
        ];
        this.lineChartLabelsRecon = this.reconciliationAnalysisData[this.selectedDateRange].labelValue;
    }

    toggleRangeAcc(e) {
        console.log('range ' + e);
        this.selectedDateRangeAcc = e.value;
        console.log('selected Range ' + e.value);
        this.lineChartDataExtraction = [{ data: this.dayWiseSampleData[0][this.selectedDateRangeAcc].dataValues, label: 'Extracted' }, { data: this.dayWiseSampleData[1][this.selectedDateRangeAcc].dataValues, label: 'Extraction Failed' }];
        this.lineChartLabelsExtraction = this.dayWiseSampleData[0][this.selectedDateRangeAcc].labelValue;
        this.lineChartDataTransformation = [{ data: this.dayWiseSampleData[0][this.selectedDateRangeAcc].dataValues, label: 'Transformed' }, { data: this.dayWiseSampleData[1][this.selectedDateRangeAcc].dataValues, label: 'Transformation Failed' }];
        this.lineChartLabelsTransformation = this.dayWiseSampleData[0][this.selectedDateRangeAcc].labelValue;
        this.lineChartDataAcc = [{ data: this.dayWiseSampleData[0][this.selectedDateRangeAcc].dataValues, label: 'Accounted' }, { data: this.dayWiseSampleData[1][this.selectedDateRangeAcc].dataValues, label: 'Un-Accounted' }];
        this.lineChartLabelsAcc = this.dayWiseSampleData[0][this.selectedDateRangeAcc].labelValue;
    }

    toggleRangeJE(e) {
        console.log('range ' + e);
        this.selectedDateRangeJe = e.value;
        console.log('selected Range ' + e.value);
        //this.lineChartData = [{ data : this.dayWiseSampleData[0][this.selectedDateRange].dataValues, label: 'Reconcilied'},{ data : this.dayWiseSampleData[1][this.selectedDateRange].dataValues, label: 'Un-Reconcilied'}];
        this.lineChartDataJournals = [{ data: this.dayWiseSampleData[0][this.selectedDateRangeJe].dataValues, label: 'JE Posted' }, { data: this.dayWiseSampleData[1][this.selectedDateRangeJe].dataValues, label: 'JE Un-Posted' }];
        this.lineChartLabelsJE = this.dayWiseSampleData[0][this.selectedDateRangeJe].labelValue;
    }

    public barChartOptions1: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom',
            display: true
        },
        plugins: {
            datalabels: {
                display: true,
                color: 'white',
                formatter: function (value, context) {
                    return value + '%';
                }
            }
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.labels[tooltipItems.index] + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        }
        /* scales: {
            xAxes: [
                {
                    stacked: false ,
                    display:true ,
                }
            ],
            yAxes: [
                {
                    stacked: false,
                    ticks: {
                        min: 0,
                        max: 100,
                        callback: function (value) { return value + "%" }
                    }
                }
            ]
        } */
    };
    public barChartLabels1: string[] = [/* 'Dv1', 'Dv2', 'Dv3' */];
    public barChartType1: string = 'pie';
    public barChartLegend1: boolean = true;
    public barChartData1: any[] = [
        /* { data: [45, 35, 20], label: 'Un-Reconcilied' } */
    ];
    /* chartClickedRecon(e) {
        console.log('e ' + e);
        this.selectedProcessAnalysis = e.active["0"]._chart.config.data.labels[e.active["0"]._index];
        let data = [
            Math.round(Math.random() * 100),
            45,
            35,
            20
        ];

        let clone = JSON.parse(JSON.stringify(this.barChartData1));
        clone[0].data = data;
        this.barChartData1 = clone;

    } */

    /* Accounting Day Wise Analysis */
    public accountingDayWiseAnalysisData: any[] = [];
    public analysisChartReconColors: {}[] = [{ backgroundColor: ['rgba(234, 67, 53, 0.9)', 'rgba(52, 168, 83, 0.9)'] }];
    /* public analysisChartReconColors: {}[] = [{ backgroundColor: ['#34A853', '#4285F4'] }]; */
    public accountingDayWiseAnalysisLabels: string[] = [];
    public accountingDayWiseAnalysisChartType: string = 'pie';
    public accountingDayWiseAnalysisLegend: boolean = true;
    public accountingDayWiseAnalysisOptions: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom',
            display: true
        },
        plugins: {
            datalabels: {
                display: false
            }
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data) {
                    return data.labels[tooltipItems.index] + ': ' + data.datasets[0].data[tooltipItems.index] + '%';
                }
            }
        }
    };

    loadContent(e) {
        console.log(e);
        this.selectedTabCCProcess = e.index;
        this.extractionValue = this.ccReconProcess[this.selectedTabCCProcess].Extraction;
        this.transformationValue = this.ccReconProcess[this.selectedTabCCProcess].Transformation;
        this.reconciliationValue = this.ccReconProcess[this.selectedTabCCProcess].Reconciliation;
        this.accountingValue = this.ccReconProcess[this.selectedTabCCProcess].Accounting;
        this.journalsValue = this.ccReconProcess[this.selectedTabCCProcess].Journals;
        /* for
        this.ccReconProcess.forEach(element => {
            
        }); */
    }
    activeItem: any;
    selectedProcessName: any;
    selectedProcessFun(col) {
        console.log('selected process ::' + JSON.stringify(col));
        this.activeItem = col;
        this.processFilter.processId = col.id;
        this.tempProcessFilter.processId = col.id;
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
        /* if(type == 'Reconcilied'){
            this.selectedTab = 1;
        }else if(type == 'Un-Reconcilied'){
            this.selectedTab = 0;
        } */
        this.selectedTab = event.index;
        console.log('this.selectedTab ' + event.index);
    }
    visibleSidebar2;
    openModuleSetting: any;

    //tempProcessFilter:any;
    openSideBar(type) {
        //this.tempProcessFilter = this.processFilter;
        this.visibleSidebar2 = true;
        this.openModuleSetting = type;
    }

    cancelReconSetting() {
        console.log('this.tempProcessFilter ' + JSON.stringify(this.tempProcessFilter));
        console.log('this.processFilter ' + JSON.stringify(this.processFilter));
        this.tempProcessFilter.asOfValue = this.processFilter.asOfValue;
        this.tempProcessFilter.startDateSel = this.processFilter.startDateSel;
        this.tempProcessFilter.endDateSel = this.processFilter.endDateSel;
        this.tempProcessFilter.closedPeriods = this.processFilter.closedPeriods;
        this.tempProcessFilter.selectedCalender = this.processFilter.selectedCalender;
        console.log('this.tempProcessFilter ' + JSON.stringify(this.tempProcessFilter));
        console.log('this.processFilter ' + JSON.stringify(this.processFilter));
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
        console.log('processFilter ' + JSON.stringify(this.processFilter));
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
        console.log('this.selectedAccTab ' + event.index);
    }

    changeAccActivityTab(event) {
        this.selectedAccTabActivity = event.index;
        console.log('this.selectedAccTabActivity ' + event.index);
    }
    selectedModuleType: any = 0;
    changeModuleType(event) {
        /* console.log('selectedModuleType ' + this.selectedModuleType);
        console.log('event.index ' + event.index); */
        if (event.index == 0) {
            $('html, body').animate({
                scrollTop: $("#reconciliationDiv").offset().top - 105
            }, 2000);
        } else if (event.index == 1) {
            $('html, body').animate({
                scrollTop: $("#accountingDiv").offset().top - 105
            }, 2000);
        }
        /* console.log('selectedModuleType ' + this.selectedModuleType);
        console.log('event.index ' + event.index); */
    }

    fetchReconciliationStatus(e) {
        //var chartElement = e.active[0]._chart.getElementAtEvent(event);

        /* console.log('selected region ::' + e.active[0]._chart.config.data.labels[e.active[0]._index]); */
        if (e.active.length > 0) {
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (labelName == 'Reconciled') {
                this.selectedTab = 1;
            } else if (labelName == 'Un-Reconciled') {
                this.selectedTab = 0;
            }
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

    fetchAccountingStatus(e) {
        if (e.active.length > 0) {
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (labelName == 'Accounted') {
                this.selectedAccTab = 2;
            } else if (labelName == 'In Process') {
                this.selectedAccTab = 1;
            } else if (labelName == 'Un-Accounted') {
                this.selectedAccTab = 0;
            }
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

    changeReportView() {
        this.tableView1 = this.tableView1 ? false : true;
    }

    /* Function on hover of recon detailsTab *//* 
    reconTabHover(type){
        if(type == 'Un-Reconciled Items By Value'){
            $('.reconItemsValueClass').css('background','aliceblue');
        }else if(type == 'Un-Reconciled Items By Violation'){
            $('.reconItemsViolationClass').css('background','blanchedalmond');
        }else if(type == 'Awaiting For Approvals'){
            $('.reconAwaitingAppClass').css('background','lavender');
        }
    } */

    selectedDetailTabRec: any;
    reconTabSelected(type) {
        this.blockedDocument = true;
        this.selectedDetailTabRec = type;
        if (type == 'Un-Reconciled Items By Value') {
            this.showApproverChart = false;
            this.unReconGrpByColors = [{ backgroundColor: '#f0756a' }];
            $('.reconItemsValueClass').addClass('active');
            $('.reconItemsViolationClass, .reconAwaitingAppClass').removeClass('active');
            this.unReconGrpByChartsData = [];
            this.unReconGrpByChartsLabels = [];
            this.unReconItemValuesDetails = [];
            this.showUnReconItemsByValueDetails();
        } else if (type == 'Un-Reconciled Items By Violation') {
            this.showApproverChart = false;
            this.unReconGrpByColors = [{ backgroundColor: '#f0756a' }];
            $('.reconItemsViolationClass').addClass('active');
            $('.reconItemsValueClass, .reconAwaitingAppClass').removeClass('active');
            this.unReconGrpByChartsData = [];
            this.unReconGrpByChartsLabels = [];
            this.unReconItemValuesDetails = [];
            this.showUnReconViolationDetails();
        } else if (type == 'Awaiting For Approvals') {
            this.unReconGrpByColors = [{ backgroundColor: 'rgba(52, 168, 83, 0.9)' }];
            $('.reconAwaitingAppClass').addClass('active');
            $('.reconItemsValueClass, .reconItemsViolationClass').removeClass('active');
            this.unReconGrpByChartsData = [];
            this.unReconGrpByChartsLabels = [];
            this.unReconItemValuesDetails = [];
            this.showUnReconApprovedDetails();
        }
    }

    unReconItemValuesDetails: any = [];


    calenderDetails: any;
    oneWeekObj: any;
    processesLength: boolean;
    /* Function to get processess list */
    fetchProcessessList() {
        /* this.tempProcessFilter.violationPeriod = 2; */
        this.navbarService.getprocessessList().subscribe((res: any) => {
            this.processesList = res;
            if (this.processesList.length > 0) {
                this.processesLength = true;
                this.selectedProcess = this.processesList[0].id;
                this.fetchSelectedProcessDetails(this.selectedProcess);
                this.navbarService.getCalenderDetails(this.selectedProcess).subscribe((res: any) => {
                    this.calenderDetails = res;
                    this.processFilter.startDate = this.calenderDetails.startDate;
                    this.processFilter.startDate = new Date(this.processFilter.startDate);
                    this.processFilter.endDate = this.calenderDetails.currentdate;
                    this.processFilter.endDate = new Date(this.processFilter.endDate);
                    this.tempProcessFilter.startDate = this.calenderDetails.startDate;
                    this.tempProcessFilter.startDate = new Date(this.tempProcessFilter.startDate);
                    this.tempProcessFilter.endDate = this.calenderDetails.currentdate;
                    this.tempProcessFilter.endDate = new Date(this.tempProcessFilter.endDate);
                    let obj = {
                        "startDate": this.processFilter.startDate,
                        "endDate": this.processFilter.endDate
                    };
                    let stDate = new Date(this.processFilter.endDate);
                    stDate.setDate(stDate.getDate() - 6);
                    this.oneWeekObj = {
                        "startDate": stDate,
                        "endDate": this.processFilter.endDate
                    };
                    this.processFilter.processId = this.processesList[0].id;
                    this.tempProcessFilter.processId = this.processesList[0].id;
                    this.activeItem = this.processesList[0];
                    this.selectedProcessName = this.processesList[0].processName;
                    this.fetchAllModuleDetails(obj);
                });
            } else {
                this.processesLength = false;
                this.notificationsService.info('Info!', 'There is no process');
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something Went Wrong');
                this.blockedDocument = false;
            });
    }

    callAllBySelectedProcessId(id) {
        this.selectedProcess = id;
        let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        };
        this.fetchSelectedProcessDetails(this.selectedProcess);
        this.fetchAllModuleDetails(obj);
    }

    processesDetailsList: any = [];
    fetchSelectedProcessDetails(id) {
        this.processesService.getProcessDetail(id).subscribe((res) => {
            this.processesDetailsList = res;
            this.tempProcessFilter.violationPeriod = this.processesDetailsList[0].processDetailList.violationList[0].typeId;
        });
    }
    fetchAllModuleDetails(obj) {


        this.blockedDocument = true;
        console.log('obj ' + JSON.stringify(obj));
        this.fetchETLDetailsList(obj);
        this.fetchETLDetailedList(this.oneWeekObj);
        this.fetchReconDetailsList(obj);
        this.fetchAccountingDetailsList(obj);
        setTimeout(() => this.fetchReconciliationDetailedList(this.oneWeekObj), 0);
        setTimeout(() => this.fetchAccountingDetailedList(this.oneWeekObj), 0);
        this.fetchMouleWiseAnalysis(this.selectedProcess, obj);
        this.fetchCCReconProcess(this.selectedProcess);
        /* this.fetchJournalsDetailedList(obj); */
    }
    extractionAnalysisData: any;
    transformationAnalysisData: any;
    showExtractionAnalysis: boolean = false;
    showTransformationAnalysis: boolean = false;
    showETLAnalysis: boolean = false;
    /* public extractionAndTransformationData: Array<any> = [
        { data: [65, 59, 80, 81, 56, 55, 40], label: 'Extracted', hidden: true },
        { data: [28, 48, 40, 19, 86, 27, 90], label: 'Extraction Failed' },
        { data: [18, 48, 77, 9, 100, 27, 40], label: 'Transformed' },
        { data: [45, 75, 70, 49, 35, 65, 75], label: 'Transformation Failed' }
    ]; */
    etlAnalysisData: any = {};
    showETLModule: boolean = false;
    selectedETLPeriod: any;
    selectedETLDayWiseAnalysis: any;
    fetchETLDetailsList(obj) {
        this.navbarService.getExtractionAnalysisData(this.selectedProcess, obj).subscribe((res: any) => {
            this.extractionAnalysisData = res;
            if (this.extractionAnalysisData) {
                this.etlAnalysisData.extraction = this.extractionAnalysisData;
                this.lineChartDataExtraction = [
                    { data: this.extractionAnalysisData['1W'].detailedData.extractionFailed, label: 'Extraction Failed' },
                    { data: this.extractionAnalysisData['1W'].detailedData.extracted, label: 'Extracted' }
                ];
                this.lineChartLabelsExtraction = this.extractionAnalysisData['1W'].detailedData.labelValue;

                if (this.lineChartDataExtraction[0].data.length > 0 && this.lineChartLabelsExtraction && this.lineChartLabelsExtraction.length > 0) {
                    this.showExtractionAnalysis = true;
                }
            }
            this.navbarService.getTransformationAnalysisData(this.selectedProcess, obj).subscribe((res: any) => {
                this.transformationAnalysisData = res;
                if (this.transformationAnalysisData) {
                    this.etlAnalysisData.transformation = this.transformationAnalysisData;
                    this.lineChartDataTransformation = [
                        { data: this.transformationAnalysisData['1W'].detailedData.transformationFailed, label: 'Transformation Failed' },
                        { data: this.transformationAnalysisData['1W'].detailedData.transformed, label: 'Transformed' }
                    ];
                    this.lineChartLabelsTransformation = this.transformationAnalysisData['1W'].detailedData.labelValue;
                    if (this.lineChartDataTransformation[0].data.length > 0 && this.lineChartLabelsTransformation && this.lineChartLabelsTransformation.length > 0) {
                        this.showTransformationAnalysis = true;
                    }
                }
                if (this.extractionAnalysisData != undefined && this.transformationAnalysisData != undefined) {
                    this.extractionAndTransformationData = [
                        { data: this.extractionAnalysisData['1W'].detailedData.extractionFailed, label: 'Extraction Failed' },
                        { data: this.extractionAnalysisData['1W'].detailedData.extracted, label: 'Extracted' },
                        { data: this.transformationAnalysisData['1W'].detailedData.transformationFailed, label: 'Transformation Failed' },
                        { data: this.transformationAnalysisData['1W'].detailedData.transformed, label: 'Transformed' }
                    ];
                    this.extractionAndTransformationLabels = this.transformationAnalysisData['1W'].detailedData.labelValue;
                    console.log('this.extractionAndTransformationData :: ' + JSON.stringify(this.extractionAndTransformationData));
                    console.log('this.extractionAndTransformationLabels :: ' + JSON.stringify(this.extractionAndTransformationLabels));
                    if (this.extractionAndTransformationData[0].data.length > 0 && this.extractionAndTransformationLabels && this.extractionAndTransformationLabels.length > 0) {
                        this.showETLAnalysis = true;
                        this.showETLModule = true;
                        this.selectedETLPeriod = '1 Week';
                        this.selectedETLDayWiseAnalysis = '1 Week';
                        $('.twoWeeKClsETL, .oneMonthClsETL, .threeMonthClsETL').removeClass('periodicActiveBlock');
                        setTimeout(() => $('.oneWeeKClsETL').addClass('periodicActiveBlock'), 0);
                    }
                }
                console.log('etlAnalysisData ' + JSON.stringify(this.etlAnalysisData));
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.notificationsService.error('Error!', 'Something Went Wrong');
                    this.blockedDocument = false;
                });
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something Went Wrong');
                this.blockedDocument = false;
            });


    }

    // lineChartLabelsRecon:any = [];

    showReconModule: boolean = false;
    selectedReconPeriod: any;
    selectedReconProcessAnalysis: any;

    /* Function to get Recon Details For Given Period */
    fetchReconDetailsList(obj) {
        this.blockedDocument = true;
        console.log('obj ' + JSON.stringify(obj));
        this.navbarService.fetchReconciliationForGivenPeriod(this.selectedProcess, obj).subscribe((res: any) => {
            this.reconciliationAnalysisData = res;
            $('#md-button-toggle-1').addClass('mat-button-toggle-checked');
            console.log('this.reconciliationAnalysisData ' + JSON.stringify(this.reconciliationAnalysisData));
            this.reconSelectedRange = '1W';
            if (this.reconciliationAnalysisData) {
                if (this.selectedReconFilterValue == 'Amount') {
                    this.lineChartData = [
                        { data: this.reconciliationAnalysisData['1W'].detailedData.unReconAmtPer, label: 'Un-Reconciled' },
                        { data: this.reconciliationAnalysisData['1W'].detailedData.reconAmtPer, label: 'Reconciled' }
                    ];
                } else {
                    this.lineChartData = [
                        { data: this.reconciliationAnalysisData['1W'].detailedData.unRecon, label: 'Un-Reconciled' },
                        { data: this.reconciliationAnalysisData['1W'].detailedData.recon, label: 'Reconciled' }
                    ];
                }
                this.lineChartLabelsRecon = this.reconciliationAnalysisData['1W'].detailedData.labelValue;
                if (this.lineChartData[0].data.length > 0 && this.lineChartLabelsRecon && this.lineChartLabelsRecon.length > 0) {
                    /* $('.twoWeeKClsRecon, .oneMonthClsRecon, .threeMonthClsRecon').removeClass('periodicActiveBlock'); */
                    this.showReconModule = true;
                    this.selectedReconPeriod = "1 Week";
                    this.selectedReconProcessAnalysis = "1 Week";
                    $('.oneWeeKClsRecon, .twoWeeKClsRecon, .oneMonthClsRecon, .threeMonthClsRecon').removeClass('periodicActiveBlock');
                    setTimeout(() => $('.oneWeeKClsRecon').addClass('periodicActiveBlock'), 0);
                    this.blockedDocument = false;
                }
                console.log('this.lineChartLabelsRecon ' + JSON.stringify(this.lineChartLabelsRecon));
                console.log('this.lineChartData ' + JSON.stringify(this.lineChartData));
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something Went Wrong while fetching reconciliation data for given period');
                this.blockedDocument = false;
            });
    }





    /* Function to get each day analysis - CC Recon Process */
    fetchCCReconProcess(proId) {
        let stDate = new Date(Date.now());
        stDate.setDate(stDate.getDate() - 6);
        let endDate = new Date(Date.now());
        let eachDayAnalysisObj = {
            "startDate": stDate,
            "endDate": endDate
        }
        console.log('eachDayAnalysisObj ' + JSON.stringify(eachDayAnalysisObj));
        this.navbarService.fetchEachDayAnalysis(proId, eachDayAnalysisObj).subscribe((res: any) => {
            this.ccReconProcess = res;
            console.log('ccReconProcess ' + JSON.stringify(this.ccReconProcess));
            this.ccReconProcess.forEach(element => {
                if (element.transformation == null) {
                    element.transformation = 0;
                }
                element['total'] = +((element.extraction + element.transformation + element.reconciliation + element.accounting + element.journals) / 5).toFixed(2);
            });
            console.log('ccReconProcess ' + JSON.stringify(this.ccReconProcess));
            this.selectedTabCCProcess = 0;
            this.extractionValue = this.ccReconProcess[this.selectedTabCCProcess].extraction;
            this.transformationValue = this.ccReconProcess[this.selectedTabCCProcess].transformation;
            this.reconciliationValue = this.ccReconProcess[this.selectedTabCCProcess].reconciliation;
            this.accountingValue = this.ccReconProcess[this.selectedTabCCProcess].accounting;
            this.journalsValue = this.ccReconProcess[this.selectedTabCCProcess].journals;
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching each day analysis data');
                this.blockedDocument = false;
            });
    }

    moduleWiseAnalysisData: any;
    /* function to get each module analysis */
    fetchMouleWiseAnalysis(proId, obj) {
        this.navbarService.fetchModuleAnalysis(proId, obj).subscribe((res: any) => {
            res[0]['etl'] = (res[0].extracted + res[0].transformation) / 2;
            this.moduleAnalysisList = res[0];
            console.log('this.moduleAnalysisList ' + JSON.stringify(this.moduleAnalysisList));
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching module wise analysis');
                this.blockedDocument = false;
            });
    }


    changeETLChartView() {
        this.groupedExtractionTransformation = !this.groupedExtractionTransformation;
    }

    dateDiff: any = 0;
    reconSelectedRange: any;
    recononciliationPeriodDetails(type) {
        this.backToOriginalView();
        this.reconSelectedRange = type;
        this.showApproverChart = false;
        /* $('html, body').animate({
            scrollTop: $("#reconciliationAct").offset().top - 105
        }, 2000); */
        this.dateDiff = 0;
        this.lineChartLabelsRecon = [];
        this.lineChartData = [
            { data: [], label: 'Un-Reconciled' },
            { data: [], label: 'Reconcilied' }
        ];
        this.reconGroupByChartData = [];
        this.reconGroupByChartLabels = [];
        this.selectedReconStatus = "Un-Reconciled";
        $('.reconItemsValueClass, .reconItemsViolationClass, .reconAwaitingAppClass').removeClass('active');
        if (type == '1W') {
            this.dateDiff = 6;
            this.selectedReconPeriod = "1 Week";
            this.selectedReconProcessAnalysis = "1 Week";
            this.selectedReconDayWiseAnalysis = "1 Week";
            $('.oneWeeKClsRecon').addClass('periodicActiveBlock');
            $('.twoWeeKClsRecon, .oneMonthClsRecon, .threeMonthClsRecon').removeClass('periodicActiveBlock');
        } else if (type == '2W') {
            this.dateDiff = 13;
            this.selectedReconPeriod = "2 Weeks";
            this.selectedReconProcessAnalysis = "2 Weeks";
            this.selectedReconDayWiseAnalysis = "2 Weeks";
            $('.twoWeeKClsRecon').addClass('periodicActiveBlock');
            $('.oneWeeKClsRecon, .oneMonthClsRecon, .threeMonthClsRecon').removeClass('periodicActiveBlock');
        } else if (type == '1M') {
            this.dateDiff = 29;
            this.selectedReconPeriod = "1 Month";
            this.selectedReconProcessAnalysis = "1 Month";
            this.selectedReconDayWiseAnalysis = "1 Month";
            $('.oneMonthClsRecon').addClass('periodicActiveBlock');
            $('.oneWeeKClsRecon,.twoWeeKClsRecon, .threeMonthClsRecon').removeClass('periodicActiveBlock');
        } else if (type == '3M') {
            this.dateDiff = 90;
            this.selectedReconPeriod = "3 Months";
            this.selectedReconProcessAnalysis = "3 Months";
            this.selectedReconDayWiseAnalysis = "3 Months";
            $('.threeMonthClsRecon').addClass('periodicActiveBlock');
            $('.oneWeeKClsRecon, .twoWeeKClsRecon, .oneMonthClsRecon').removeClass('periodicActiveBlock');
        }
        console.log('type ' + type);
        if (this.selectedReconFilterValue == "Amount") {
            this.lineChartData = [
                { data: this.reconciliationAnalysisData[type].detailedData.unReconAmtPer, label: 'Un-Reconciled' },
                { data: this.reconciliationAnalysisData[type].detailedData.reconAmtPer, label: 'Reconciled' }
            ];
        } else {
            this.lineChartData = [
                { data: this.reconciliationAnalysisData[type].detailedData.unRecon, label: 'Un-Reconciled' },
                { data: this.reconciliationAnalysisData[type].detailedData.recon, label: 'Reconciled' }
            ];
        }
        /* console.log('obj ' + this.processFilter.startDate);
        console.log('obj ' + this.processFilter.endDate); */
        console.log('dateDiff ' + this.dateDiff);
        setTimeout(() => this.lineChartLabelsRecon = this.reconciliationAnalysisData[type].detailedData.labelValue, 0);
        /* this.processFilter.endDate = new Date(this.processFilter.endDate);
        //this.processFilter.startDate = new Date(this.processFilter.startDate);
        this.processFilter.startDate.setDate(this.processFilter.endDate.getDate() - dateDiff);
       // this.processFilter.startDate = new Date(this.processFilter.startDate); */
        let stDate = new Date(this.processFilter.endDate);
        stDate.setDate(stDate.getDate() - this.dateDiff);
        this.reconDateObj = {
            "startDate": stDate,
            "endDate": this.processFilter.endDate
        }
        console.log('obj ' + JSON.stringify(this.reconDateObj));
        this.showUnReconGrpChart = false;
        this.fetchReconciliationDetailedList(this.reconDateObj);
    }

    selectedETLStatus: any;
    selectedETLProcessAnalysis: any;
    dateDiffETL: any;
    etlDateObj: any;
    etlPeriodDetails(type) {
        this.blockedDocument = false;
        /* let dateDiff = 0; */
        this.lineChartLabelsExtraction = [];
        this.lineChartDataExtraction = [
            { data: [], label: 'Extraction Failed' },
            { data: [], label: 'Extracted' }
        ];
        this.selectedETLStatus = "Extraction Failed";
        if (type == '1W') {
            this.dateDiffETL = 6;
            this.selectedETLPeriod = "1 Week";
            this.selectedETLProcessAnalysis = "1 Week";
            this.selectedETLDayWiseAnalysis = "1 Week";
            $('.oneWeeKClsETL').addClass('periodicActiveBlock');
            $('.twoWeeKClsETL, .oneMonthClsETL, .threeMonthClsETL').removeClass('periodicActiveBlock');
        } else if (type == '2W') {
            this.dateDiffETL = 13;
            this.selectedETLPeriod = "2 Weeks";
            this.selectedETLProcessAnalysis = "2 Weeks";
            this.selectedETLDayWiseAnalysis = "2 Weeks";
            $('.twoWeeKClsETL').addClass('periodicActiveBlock');
            $('.oneWeeKClsETL, .oneMonthClsETL, .threeMonthClsETL').removeClass('periodicActiveBlock');
        } else if (type == '1M') {
            this.dateDiffETL = 29;
            this.selectedETLPeriod = "1 Month";
            this.selectedETLProcessAnalysis = "1 Month";
            this.selectedETLDayWiseAnalysis = "1 Month";
            $('.oneMonthClsETL').addClass('periodicActiveBlock');
            $('.oneWeeKClsETL,.twoWeeKClsETL, .threeMonthClsETL').removeClass('periodicActiveBlock');
        } else if (type == '3M') {
            this.dateDiffETL = 90;
            this.selectedETLPeriod = "3 Months";
            this.selectedETLProcessAnalysis = "3 Months";
            this.selectedETLDayWiseAnalysis = "3 Months";
            $('.threeMonthClsETL').addClass('periodicActiveBlock');
            $('.oneWeeKClsETL, .twoWeeKClsETL, .oneMonthClsETL').removeClass('periodicActiveBlock');
        }
        console.log('type ' + type);

        this.lineChartDataExtraction = [
            { data: this.extractionAnalysisData[type].detailedData.extractionFailed, label: 'Extraction Failed' },
            { data: this.extractionAnalysisData[type].detailedData.extracted, label: 'Extracted' }
        ];
        setTimeout(() => this.lineChartLabelsExtraction = this.extractionAnalysisData[type].detailedData.labelValue, 0);

        this.lineChartDataTransformation = [
            { data: this.transformationAnalysisData[type].detailedData.transformationFailed, label: 'Transformation Failed' },
            { data: this.transformationAnalysisData[type].detailedData.transformed, label: 'Transformed' }
        ];
        setTimeout(() => this.lineChartLabelsTransformation = this.transformationAnalysisData[type].detailedData.labelValue, 0);

        this.extractionAndTransformationData = [
            { data: this.extractionAnalysisData[type].detailedData.extractionFailed, label: 'Extraction Failed' },
            { data: this.extractionAnalysisData[type].detailedData.extracted, label: 'Extracted' },
            { data: this.transformationAnalysisData[type].detailedData.transformationFailed, label: 'Transformation Failed' },
            { data: this.transformationAnalysisData[type].detailedData.transformed, label: 'Transformed' }
        ];
        setTimeout(() => this.extractionAndTransformationLabels = this.transformationAnalysisData[type].detailedData.labelValue, 0);

        /* this.processFilter.endDate = new Date(this.processFilter.endDate);
        this.processFilter.startDate.setDate(this.processFilter.endDate.getDate() - dateDiff);
        this.processFilter.startDate = new Date(this.processFilter.startDate);
        let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        } */
        let stDate = new Date(this.processFilter.endDate);
        stDate.setDate(stDate.getDate() - this.dateDiffETL);
        this.etlDateObj = {
            "startDate": stDate,
            "endDate": this.processFilter.endDate
        }
        if (this.lineChartDataExtraction[0].data.length > 0 && this.lineChartDataTransformation[0].data.length > 0 && this.extractionAndTransformationData[0].data.length > 0) {
            this.blockedDocument = false;
        }
        console.log('obj ' + JSON.stringify(this.etlDateObj));
        this.fetchETLDetailedList(this.etlDateObj);
    }
    showReconSubProcess: boolean = false;

    tempChartArr: any = [];
    tempChartViewName: any = [];
    tempLabelObj: any;
    unReconItemsValueKpiPer: any;
    unReconItemsViolationKpiPer: any;
    awaitingAppCountKpiPer: any;
    fetchReconciliationDetailedList(obj) {
        this.blockedDocument = true;
        this.reconGroupByData = [];
        console.log('fetching Recon Details obj :' + JSON.stringify(obj));
        console.log('fetching Recon Details this.selectedProcess ' + this.selectedProcess);
        this.navbarService.fetchReconciliationDetailedData(this.selectedProcess, this.tempProcessFilter.violationPeriod, obj).subscribe((res: any) => {
            this.reconDetailedList = res;
            this.selectedTabProcessWiseAnalysis = 0;
            console.log('Recon Details this.reconDetailedList ' + JSON.stringify(this.reconDetailedList));
            this.tempChartArr = [];
            this.barChartLabels1 = [];
            this.barChartData1 = [];
            this.tempChartViewName = [];
            this.reconDetailedList.reconciliationData.forEach(element => {
                let tempObj = [];
                if (this.selectedReconFilterValue == 'Amount') {
                    tempObj = [element.unReconciledAmtPer, element.reconciledAmtPer];
                } else {
                    tempObj = [element.unReconciledPer, element.reconciledPer];
                }
                this.barChartData1.push(tempObj);
                this.tempChartViewName.push(element.viewName);
            });
            this.barChartLabels1 = ['Un-Reconciled', 'Reconciled'];
            console.log('this.barChartData1 ' + JSON.stringify(this.barChartData1));
            this.unReconItemsValueKpi = res.unReconItemsValue;
            this.unReconItemsViolationKpi = res.unReconItemsViolation;
            this.awaitingAppCountKpi = res.awaitingAppCount;
            this.unReconItemsValueKpiPer = res.unReconItemsValuePer;
            this.unReconItemsViolationKpiPer = res.unReconItemsViolationPer;
            this.awaitingAppCountKpiPer = res.awaitingAppCountPer;
            if (this.barChartData1.length > 0 && this.barChartLabels1 && this.barChartLabels1.length > 0) {
                this.showReconSubProcess = true;
                this.selectedReconViewId = this.reconDetailedList.reconciliationData[0].viewId;
                this.selectedReconViewName = this.reconDetailedList.reconciliationData[0].viewName;
                this.navbarService.getReconGroupByLov(this.selectedReconViewId).subscribe((res: any) => {
                    this.reconGroupByList = res;
                    this.reconGroupByData = res.columnsList;
                    console.log('this.reconGroupByData ' + JSON.stringify(this.reconGroupByData));
                    this.recongroupBy = [this.reconGroupByData[0].columnAliasName];
                    this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName;
                    console.log('this.reconGroupByList ' + JSON.stringify(this.reconGroupByList));
                    console.log('this.recongroupBy ' + this.recongroupBy);
                    console.log('this.recongroupBy ' + JSON.stringify(this.recongroupBy));
                    if (this.reconGroupByData.length > 0) {
                        this.displayReconGrpList = true;
                    }
                    /*  let obj = {
                         "startDate": this.processFilter.startDate,
                         "endDate": this.processFilter.endDate
                     } */
                    this.selectedReconModuleName = 'Un-Reconciled';
                    this.reconGroupByData.forEach(element => {
                        this.fetchUnProcessedOrProcessedData(this.selectedProcess, this.selectedReconViewId, this.selectedReconModuleName, this.reconGroupByList.amtQualifier.amtQualifierAliasName, this.recongroupBy, obj);
                    });
                });

            } else {
                this.blockedDocument = false;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching reconciliation detailed data');
                this.blockedDocument = false;
            });
    }

    etlDetailedList: any;
    etlExtractionFailedItemsKpi: any;
    etlTransformationFailedItemsKpi: any;
    etlExtractionFailedItemsKpiPer: any;
    etlTransformationFailedItemsKpiPer: any;
    fetchETLDetailedList(obj) {
        this.navbarService.getETLDetailedAnalysisData(this.selectedProcess, obj).subscribe((res: any) => {
            this.etlDetailedList = res;
            this.etlExtractionFailedItemsKpi = res.totalExtractionFailedCt;
            this.etlTransformationFailedItemsKpi = res.totalTransformationFailedCt;
            this.etlExtractionFailedItemsKpiPer = res.totalExtractionFailedCtPer;
            this.etlTransformationFailedItemsKpiPer = res.totalTransformationFailedCtPer;
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something Went Wrong');
                this.blockedDocument = false;
            });
    }

    extractionDateObj: any;
    chartClickedExtraction(e) {
        if (e.active[0] != undefined) {
            this.selectedETLDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (this.selectedETLPeriod == '1 Week') {
                this.showDrillDownExt = false;
                this.extractionDateObj = {
                    "startDate": new Date(this.extractionAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.extractionAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index])
                }
                this.fetchETLDetailedList(this.extractionDateObj);
            } else if (this.selectedETLPeriod == '2 Weeks') {
                this.showDrillDownExt = false;
                this.extractionDateObj = {
                    "startDate": new Date(this.extractionAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.extractionAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index])
                }
                this.fetchETLDetailedList(this.extractionDateObj);
            } else if (this.selectedETLPeriod == '1 Month') {
                if (this.showDrillDownExt == false) {
                    this.extractionDateObj = {
                        "startDate": new Date(this.extractionAnalysisData['1M'].detailedData.labelDateValue[e.active["0"]._index].startDate),
                        "endDate": new Date(this.extractionAnalysisData['1M'].detailedData.labelDateValue[e.active["0"]._index].endDate)
                    }
                    this.fetchETLDetailedList(this.extractionDateObj);
                    this.fetchWeekAnalysisForModule('extraction', this.extractionDateObj);
                    let elem: any = document.querySelector('.drillDownPanelExt')
                    elem.style.height = "250px";
                } else {
                    this.extractionDateObj = {
                        "startDate": new Date(this.extractionDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                        "endDate": new Date(this.extractionDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                    }
                    this.fetchETLDetailedList(this.extractionDateObj);
                }
            } else if (this.selectedETLPeriod == '3 Months') {
                if (this.showDrillDownExt == false) {
                    this.extractionDateObj = {
                        "startDate": new Date(this.extractionAnalysisData['3M'].detailedData.labelDateValue[e.active["0"]._index].startDate),
                        "endDate": new Date(this.extractionAnalysisData['3M'].detailedData.labelDateValue[e.active["0"]._index].endDate)
                    }
                    this.fetchETLDetailedList(this.extractionDateObj);
                    this.fetchWeekAnalysisForModule('extraction', this.extractionDateObj);
                    let elem: any = document.querySelector('.drillDownPanelExt')
                    elem.style.height = "250px";
                } else {
                    this.extractionDateObj = {
                        "startDate": new Date(this.extractionDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                        "endDate": new Date(this.extractionDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                    }
                    this.fetchETLDetailedList(this.extractionDateObj);
                }
            }
        }
    }

    transformationDateObj: any;
    showDrillDownTrans: boolean = false;

    chartClickedTransformation(e) {
        if (e.active[0] != undefined) {
            this.selectedETLDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (this.selectedETLPeriod == '1 Week') {
                this.showDrillDownTrans = false;
                this.transformationDateObj = {
                    "startDate": new Date(this.transformationAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.transformationAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index])
                }
                this.fetchETLDetailedList(this.transformationDateObj);
            } else if (this.selectedETLPeriod == '2 Weeks') {
                this.showDrillDownTrans = false;
                this.transformationDateObj = {
                    "startDate": new Date(this.transformationAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.transformationAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index])
                }
                this.fetchETLDetailedList(this.transformationDateObj);
            } else if (this.selectedETLPeriod == '1 Month') {
                if (this.showDrillDownTrans == false) {
                    this.transformationDateObj = {
                        "startDate": new Date(this.transformationAnalysisData['1M'].detailedData.labelDates[e.active["0"]._index].startDate),
                        "endDate": new Date(this.transformationAnalysisData['1M'].detailedData.labelDates[e.active["0"]._index].endDate)
                    }
                    this.fetchETLDetailedList(this.transformationDateObj);
                    this.fetchWeekAnalysisForModule('transformation', this.transformationDateObj);
                    let elem: any = document.querySelector('.drillDownPanelTrans')
                    elem.style.height = "250px";
                } else {
                    this.transformationDateObj = {
                        "startDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                        "endDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                    }
                    this.fetchETLDetailedList(this.transformationDateObj);
                }
            } else if (this.selectedETLPeriod == '3 Months') {
                if (this.showDrillDownTrans == false) {
                    this.transformationDateObj = {
                        "startDate": new Date(this.extractionAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index].startDate),
                        "endDate": new Date(this.extractionAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index].endDate)
                    }
                    this.fetchETLDetailedList(this.transformationDateObj);
                    this.fetchWeekAnalysisForModule('transformation', this.transformationDateObj);
                    let elem: any = document.querySelector('.drillDownPanelTrans')
                    elem.style.height = "250px";
                } else {
                    this.transformationDateObj = {
                        "startDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                        "endDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                    }
                    this.fetchETLDetailedList(this.transformationDateObj);
                }
            }
        }
    }

    showDrillDownETL: boolean = false;
    etlChartDateObj: any;
    chartClickedETL(e) {
        if (e.active[0] != undefined) {
            this.selectedETLDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if (this.selectedETLPeriod == '1 Week') {
                this.showDrillDownETL = false;
                this.etlChartDateObj = {
                    "startDate": new Date(this.transformationAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.transformationAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index])
                }
                this.fetchETLDetailedList(this.etlChartDateObj);
            } else if (this.selectedETLPeriod == '2 Weeks') {
                this.showDrillDownETL = false;
                this.etlChartDateObj = {
                    "startDate": new Date(this.transformationAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.transformationAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index])
                }
                this.fetchETLDetailedList(this.etlChartDateObj);
            } else if (this.selectedETLPeriod == '1 Month') {
                if (this.showDrillDownETL == false) {
                    this.etlChartDateObj = {
                        "startDate": new Date(this.transformationAnalysisData['1M'].detailedData.labelDates[e.active["0"]._index].startDate),
                        "endDate": new Date(this.transformationAnalysisData['1M'].detailedData.labelDates[e.active["0"]._index].endDate)
                    }
                    this.fetchETLDetailedList(this.etlChartDateObj);
                    this.fetchWeekAnalysisForModule('etl', this.etlChartDateObj);
                    let elem: any = document.querySelector('.drillDownPanelETL')
                    elem.style.height = "250px";
                } else {
                    this.etlChartDateObj = {
                        "startDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                        "endDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                    }
                    this.fetchETLDetailedList(this.etlChartDateObj);
                }
            } else if (this.selectedETLPeriod == '3 Months') {
                if (this.showDrillDownETL == false) {
                    this.etlChartDateObj = {
                        "startDate": new Date(this.extractionAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index].startDate),
                        "endDate": new Date(this.extractionAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index].endDate)
                    }
                    this.fetchETLDetailedList(this.etlChartDateObj);
                    this.fetchWeekAnalysisForModule('etl', this.etlChartDateObj);
                    let elem: any = document.querySelector('.drillDownPanelETL')
                    elem.style.height = "250px";
                } else {
                    this.etlChartDateObj = {
                        "startDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                        "endDate": new Date(this.transformationDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                    }
                    this.fetchETLDetailedList(this.etlChartDateObj);
                }
            }
        }
    }

    reconDateObj: any;
    chartClickedRecon(e) {
        if (e.active[0] != undefined) {
            this.blockedDocument = true;
            this.showApproverChart = false;
            $('.reconItemsValueClass, .reconItemsViolationClass, .reconAwaitingAppClass').removeClass('active');
            this.reconGroupByChartData = [];
            this.reconGroupByChartLabels = [];
            /* console.log('.active[1]._chart.tooltip._active["0"]._datasetIndex ' + e.active[1]._chart.tooltip._active[0]._datasetIndex);
            console.log('.active["0"]._chart.config.data.datasets ' + e.active["0"]._chart.config.data.datasets[e.active[1]._chart.tooltip._active[0]._datasetIndex].label);
            console.log('e.active["0"]._index ' + e.active[0]._index);
            console.log('selected ' + e.active[0]._chart.config.data.labels[e.active[0]._index]);
            console.log('selected Date ' + e.active["0"]._chart.config.data.labels[e.active["0"]._index]);
            console.log('this.reconciliationAnalysisData ' + JSON.stringify(this.reconciliationAnalysisData)); */
            this.selectedReconStatus = e.active["0"]._chart.config.data.datasets[e.active[1]._chart.tooltip._active[0]._datasetIndex].label;
            this.selectedReconDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];
            let obj;
            this.reconGrpByColors = [{ backgroundColor: '#f0756a' }];
            /* if(this.selectedReconStatus == 'Un-Reconcilied'){
                this.analysisChartReconColors = [{ backgroundColor: ['#f07b71','#f6b3ae'] }];
            }else{
                this.analysisChartReconColors = [{ backgroundColor: ['#34A853', '#4285F4'] }];
            } */
            /* public analysisChartReconColors: {}[] = [{ backgroundColor: ['#f07b71','#f6b3ae'] }]; */
            /* public analysisChartReconColors: {}[] = [{ backgroundColor: ['#34A853', '#4285F4'] }]; */
            if (this.selectedReconPeriod == '1 Week') {
                this.showDrillDown = false;
                this.reconDateObj = {
                    "startDate": new Date(this.reconciliationAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.reconciliationAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index])
                }
            } else if (this.selectedReconPeriod == '2 Weeks') {
                this.showDrillDown = false;
                this.reconDateObj = {
                    "startDate": new Date(this.reconciliationAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.reconciliationAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index])
                }
            } else if (this.selectedReconPeriod == '1 Month') {
                this.reconDateObj = {
                    "startDate": new Date(this.reconciliationAnalysisData['1M'].detailedData.labelDateValue[e.active["0"]._index].startDate),
                    "endDate": new Date(this.reconciliationAnalysisData['1M'].detailedData.labelDateValue[e.active["0"]._index].endDate)
                }
                this.fetchWeekAnalysisForModule('reconciliation', this.reconDateObj);
                let elem: any = document.querySelector('.drillDownPanel')
                elem.style.height = "250px";
                /* console.log(elem); */
                // $('.drillDownPanel').css('height', '250px !important');

            } else if (this.selectedReconPeriod == '3 Months') {
                this.reconDateObj = {
                    "startDate": new Date(this.reconciliationAnalysisData['3M'].detailedData.labelDateValue[e.active["0"]._index].startDate),
                    "endDate": new Date(this.reconciliationAnalysisData['3M'].detailedData.labelDateValue[e.active["0"]._index].endDate)
                }
                this.fetchWeekAnalysisForModule('reconciliation', this.reconDateObj);
                let elem: any = document.querySelector('.drillDownPanel')
                elem.style.height = "250px";
                /* obj = {
                    "startDate": new Date(this.reconciliationAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.reconciliationAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index])
                } */
            }
            console.log('obj ' + JSON.stringify(obj));
            this.showUnReconGrpChart = false;
            this.fetchReconciliationDetailedList(this.reconDateObj);
        }
    }

    chartClickedReconDrill(e) {
        if (e.active[0] != undefined) {
            this.blockedDocument = true;
            this.showApproverChart = false;
            $('.reconItemsValueClass, .reconItemsViolationClass, .reconAwaitingAppClass').removeClass('active');
            this.reconGroupByChartData = [];
            this.reconGroupByChartLabels = [];
            this.selectedReconStatus = e.active["0"]._chart.config.data.datasets[e.active[1]._chart.tooltip._active[0]._datasetIndex].label;
            this.selectedReconDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];
            let obj;
            this.reconGrpByColors = [{ backgroundColor: '#f0756a' }];
            if (this.selectedReconPeriod == '1 Month') {
                this.reconDateObj = {
                    "startDate": new Date(this.reconDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.reconDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                }

            } else if (this.selectedReconPeriod == '3 Months') {
                this.reconDateObj = {
                    "startDate": new Date(this.reconDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.reconDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                }
            }
            this.showUnReconGrpChart = false;
            this.fetchReconciliationDetailedList(this.reconDateObj);
        }
    }
    selectedReconModuleName: any;
    reconGroupByList: any;
    displayReconGrpList: boolean = false;
    selectedReconViewId: any;
    selectedReconViewName: any;
    selectedTabProcessWiseAnalysis: any = 0;
    fetchReconGroupByDetails(e, i) {
        console.log('selectedTabProcessWiseAnalysis ' + this.selectedTabProcessWiseAnalysis);
        this.blockedDocument = true;
        if (e.active[0] != undefined) {
            console.log('selectedTabProcessWiseAnalysis ' + this.selectedTabProcessWiseAnalysis);
            console.log('this.reconDetailedList ' + JSON.stringify(this.reconDetailedList));
            this.reconGroupByData = [];
            this.reconGroupByChartData = [];
            this.reconGroupByChartLabels = [];
            this.displayReconGrpList = false;
            this.reconGroupByList.columnsList = null;
            this.reconGroupByList = null;
            console.log('e ' + i);
            console.log('selected ' + e.active[0]._chart.config.data.labels[e.active[0]._index]);
            this.selectedReconModuleName = e.active[0]._chart.config.data.labels[e.active[0]._index];

            console.log('view id :' + this.reconDetailedList.reconciliationData[i].viewId);
            this.selectedReconViewId = this.reconDetailedList.reconciliationData[i].viewId;
            this.selectedReconViewName = this.reconDetailedList.reconciliationData[i].viewName;
            if (this.selectedReconModuleName == "Un-Reconciled") {
                this.reconGrpByColors = [{ backgroundColor: '#f0756a' }];
            } else {
                this.reconGrpByColors = [{ backgroundColor: '#8adba0' }];
            }
            this.navbarService.getReconGroupByLov(this.selectedReconViewId).subscribe((res: any) => {
                this.reconGroupByList = res;
                this.reconGroupByData = res.columnsList;
                this.recongroupBy = [this.reconGroupByData[0].columnAliasName];

                console.log('this.reconGroupByList ' + JSON.stringify(this.reconGroupByList))
                if (this.reconGroupByData.length > 0) {
                    this.displayReconGrpList = true;
                }
                console.log('groupBy ' + JSON.stringify(this.recongroupBy));
                /* let obj = {
                    "startDate": this.processFilter.startDate,
                    "endDate": this.processFilter.endDate
                } */
                /* let stDate = new Date(this.processFilter.endDate);
                stDate.setDate(stDate.getDate() - this.dateDiff);
                let obj = {
                    "startDate": stDate,
                    "endDate": this.processFilter.endDate
                } */
                this.reconGroupByData.forEach((element, j) => {
                    this.selectedReconGroupBy = this.reconGroupByData[j].columnAliasName;
                    this.fetchUnProcessedOrProcessedData(this.selectedProcess, this.selectedReconViewId, this.selectedReconModuleName, this.reconGroupByList.amtQualifier.amtQualifierAliasName, this.selectedReconGroupBy, this.reconDateObj);
                })
            });
        }
    }

    selectedAccGroupBy: any;
    showViewSource: boolean = false;
    fetchAccGroupByDetails(e, i) {
        if (e.active[0] != undefined) {
            /* let obj = {
                "startDate": this.processFilter.startDate,
                "endDate": this.processFilter.endDate
            } */
            console.log('view id :' + this.accDetailedList.accountingData[i].viewId);
            this.selectedAccViewId = this.accDetailedList.accountingData[i].viewId;
            this.selectedAccViewName = this.accDetailedList.accountingData[i].viewName;

            if (e.active[0]._chart.config.data.labels[e.active[0]._index] == 'JE Creation') {
                this.tempJEAccGrpName = [];
                this.selectedAccModuleName = e.active[0]._chart.config.data.labels[e.active[0]._index];
                this.fetchDetailedInfoForJEByView(this.selectedAccViewId, 'JE Creation', this.accountingDateObj);
            }else if (e.active[0]._chart.config.data.labels[e.active[0]._index] == 'Pending Journals') {
                this.tempJEAccGrpName = [];
                this.selectedAccModuleName = e.active[0]._chart.config.data.labels[e.active[0]._index];
                this.fetchDetailedInfoForJEByView(this.selectedAccViewId, 'Accounted', this.accountingDateObj);
            } else {
                this.blockedDocument = true;
                this.accGroupByData = [];
                console.log('selectedTabProcessWiseAnalysisAcc ' + this.selectedTabProcessWiseAnalysisAcc);
                console.log('this.accDetailedList ' + JSON.stringify(this.accDetailedList));
                this.accGroupByChartData = [];
                this.accGroupByChartLabels = [];

                this.displayAccGrpList = false;
                this.accGroupByList = {};
                console.log('e ' + i);
                console.log('selected ' + e.active[0]._chart.config.data.labels[e.active[0]._index]);
                if (e.active[0]._chart.config.data.labels[e.active[0]._index] == 'Pending Journals') {
                    this.selectedAccModuleName = 'Accounted';
                } else {
                    this.selectedAccModuleName = e.active[0]._chart.config.data.labels[e.active[0]._index];
                }
                this.showViewSource = false;
                this.navbarService.getReconGroupByLov(this.selectedAccViewId).subscribe((res: any) => {
                    this.accGroupByList = res;
                    this.accGroupByData = res.columnsList;
                    this.accgroupBy = [this.accGroupByData[0].columnAliasName];
                    this.selectedAccGroupBy = this.accGroupByData[0].columnAliasName;
                    console.log('this.accGroupByList ' + JSON.stringify(this.accGroupByList))
                    if (this.accGroupByData.length > 0) {
                        this.displayAccGrpList = true;
                    }
                    this.tempNotAccGrpName = [];
                    console.log('groupBy ' + JSON.stringify(this.accgroupBy));
                    this.accGroupByData.forEach(element => {
                        this.fetchUnProcessedOrProcessedDataAcc(this.selectedProcess, this.selectedAccViewId, this.selectedAccModuleName, this.accGroupByList.amtQualifier.amtQualifierAliasName, element.columnAliasName, element.columnName, this.accountingDateObj);
                    })
                });
            }
        }
    }
    showGrpChart: boolean = false;
    /* this.reconGroupByChartData = [];
        this.reconGroupByChartLabels = []; */
    fetchUnProcessedOrProcessedData(selectedProcess, viewId, selectedReconModuleName, amtQuailifier, groupBy, obj?) {
        this.reconGroupByChartData = [];
        this.reconGroupByChartLabels = [];
        /* this.showGrpChart = false; */
        /* let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        } */
        console.log('selectedProcess ' + selectedProcess + ' viewId ' + viewId + ' selectedReconModuleName ' + selectedReconModuleName + ' amtQuailifier ' + amtQuailifier + ' groupBy ' + groupBy);
        let objData = [];
        let objValue = [];

        this.navbarService.getUnProcessedOrProcessedData(selectedProcess, viewId, selectedReconModuleName, amtQuailifier, groupBy, obj).subscribe((res: any) => {
            console.log('getUnProcessedOrProcessedData ::' + JSON.stringify(res));

            if (res == null || res == [] || res == '') {
                this.reconGroupByChartData = [];
                this.reconGroupByChartLabels = [];
                this.showGrpChart = true;
                this.blockedDocument = false;
            }
            res.forEach(element => {
                objData.push(element.value);
                objValue.push(element.name);
            });
            /* this.reconGroupByChartData = objData; */
            if (objData.length > 0 && objValue) {
                this.reconGroupByChartData.push(objData);
                this.reconGroupByChartLabels.push(objValue);
                console.log('this.reconGroupByChartData ' + JSON.stringify(this.reconGroupByChartData));
                console.log('this.reconGroupByChartLabels ' + JSON.stringify(this.reconGroupByChartLabels));
                this.showGrpChart = true;
                console.log('showUnReconGrpChart ' + this.showUnReconGrpChart);
                this.blockedDocument = false;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching un processed or processed data for reconciliation');
                this.blockedDocument = false;
            });
    }


    public accGroupByChartData: Array<any> = [];
    public accGroupByChartLabels = [];
    showGrpChartAcc: boolean = false;
    showAccGrpChart: boolean = false;
    tempNotAccGrpName: any = [];
    fetchUnProcessedOrProcessedDataAcc(selectedProcess, viewId, selectedAccModuleName, amtQuailifier, groupBy, colName, obj?) {
        this.blockedDocument = true;
        this.accGroupByChartData = [];
        this.accGroupByChartLabels = [];
        /* this.showGrpChart = false; */
        /* let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        } */
        console.log('selectedProcess ' + selectedProcess + ' viewId ' + viewId + ' selectedReconModuleName ' + selectedAccModuleName + ' amtQuailifier ' + amtQuailifier + ' groupBy ' + groupBy);
        let objDataAcc = [];
        let objValueAcc = [];

        this.navbarService.getUnProcessedOrProcessedData(selectedProcess, viewId, selectedAccModuleName, amtQuailifier, groupBy, obj, colName).subscribe((res: any) => {
            this.tempNotAccGrpName.push(colName);
            console.log('getUnProcessedOrProcessedDataAcc ::' + JSON.stringify(res));

            if (res == null || res == [] || res == '') {
                this.accGroupByChartData = [];
                this.accGroupByChartLabels = [];
                this.showGrpChart = true;
                this.blockedDocument = false;
            }
            res.forEach(element => {
                objDataAcc.push(element.value);
                objValueAcc.push(element.name);
            });
            /* this.accGroupByChartData = objData; */
            if (objDataAcc.length > 0 && objValueAcc) {
                this.accGroupByChartData.push(objDataAcc);
                this.accGroupByChartLabels.push(objValueAcc);
                console.log('this.accGroupByChartData ' + JSON.stringify(this.accGroupByChartData));
                console.log('this.accGroupByChartLabels ' + JSON.stringify(this.accGroupByChartLabels));
                this.showGrpChartAcc = true;
                this.blockedDocument = false;
                console.log('showGrpChartAcc ' + this.showGrpChartAcc);
            } else {
                this.showGrpChartAcc = true;
                this.blockedDocument = false;
                /* this.notificationsService.info('Info!', 'No data for selected process'); */
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something Went Wrong while fetching un processed or processed data for accounting');
                this.blockedDocument = false;
            });
    }

    /* 
        filterReconGrpDetails(type) {
            let gropBy = [type];
            this.fetchUnProcessedOrProcessedData(this.selectedProcess, this.selectedReconViewId, this.selectedReconModuleName, this.reconGroupByList.amtQualifier.amtQualifierAliasName, gropBy);
        } */

    /* Accounting Module Start */
    showAccountingModule: boolean = false;
    selectedAccountingPeriod: any;
    selectedAccountingProcessAnalysis: any;

    showJournalsModule: boolean = false;
    selectedJournalsPeriod: any;
    selectedJournalsProcessAnalysis: any;
    selectedJournalsDayWiseAnalysis: any;
    accSelectedRange: any;
    /* Function to get Recon Details For Given Period */
    fetchAccountingDetailsList(obj) {
        this.blockedDocument = true;
        console.log('obj ' + JSON.stringify(obj));
        this.navbarService.fetchAccountingForGivenPeriod(this.selectedProcess, obj).subscribe((res: any) => {
            this.accountingAnalysisData = res;
            this.accSelectedRange = '1W';
            console.log('this.accountingAnalysisData ' + JSON.stringify(this.accountingAnalysisData));
            if (this.accountingAnalysisData) {

                if (this.selectedAccFilterValue == 'Amount') {
                    this.lineChartDataAcc = [
                        { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProcAmtPer, label: 'Not Accounted' },
                        { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accountedAmtPer, label: 'Pending Journals' },
                        { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccountedAmtPer, label: 'JE Creation' }
                    ];
                } else {
                    this.lineChartDataAcc = [
                        { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProc, label: 'Not Accounted' },
                        { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accounted, label: 'Pending Journals' },
                        { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccounted, label: 'JE Creation' }
                    ];
                }
                this.lineChartLabelsAcc = this.accountingAnalysisData[this.accSelectedRange].detailedData.labelValue;
                if (this.lineChartDataAcc[0].data.length > 0 && this.lineChartLabelsAcc && this.lineChartLabelsAcc.length > 0) {
                    this.showAccountingModule = true;
                    this.selectedAccountingPeriod = "1 Week";
                    this.selectedAccountingProcessAnalysis = "1 Week";
                    $('.oneWeeKClsAcc, .twoWeeKClsAcc, .oneMonthClsAcc, .threeMonthClsAcc').removeClass('periodicActiveBlock');
                    setTimeout(() => $('.oneWeeKClsAcc').addClass('periodicActiveBlock'), 0);
                }
                this.lineChartDataJournals = [
                    { data: this.accountingAnalysisData['1W'].detailedData.finalNotAccounted, label: 'JE Un-Posted' },
                    { data: this.accountingAnalysisData['1W'].detailedData.finalAccounted, label: 'JE Posted' }
                ];
                this.lineChartLabelsJE = this.accountingAnalysisData['1W'].detailedData.labelValue;
                if (this.lineChartDataJournals[0].data.length > 0 && this.lineChartLabelsJE && this.lineChartLabelsJE.length > 0) {
                    this.showJournalsModule = true;
                    this.selectedJournalsPeriod = "1 Week";
                    this.selectedJournalsDayWiseAnalysis = '1 Week';
                    this.selectedJournalsProcessAnalysis = "1 Week";
                    this.blockedDocument = false;
                    setTimeout(() => $('.oneWeeKClsJournals').addClass('periodicActiveBlock'), 0);
                }
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching accounting data for given period');
                this.blockedDocument = false;
            });
    }

    accountedSelectedPeriod: any;
    accountingDateObj: any;
    dateDiffAcc: any;
    accountingPeriodDetails(type) {
        this.showDrillDownAcc = false;
        console.log('this.selectedAccViewId ' + this.selectedAccViewId);
        this.tempNotAccGrpName = [];
        this.showViewSource = false;
        this.blockedDocument = true;
        this.accSelectedRange = type;
        let dateDiff = 0;
        this.lineChartLabelsAcc = [];
        this.lineChartDataAcc = [
            { data: [], label: 'Not Accounted' },
            { data: [], label: 'Pending Journals' },
            { data: [], label: 'JE Creation' }
        ];
        this.accGroupByChartData = [];
        this.accGroupByChartLabels = [];
        this.selectedAccountingStatus = "Not Accounted";
        $('.accItemsValueClass, .accItemsViolationClass, .accAwaitingAppClass').removeClass('active');
        if (type == '1W') {
            this.dateDiffAcc = 6;
            this.selectedAccountingPeriod = "1 Week";
            this.selectedAccountingProcessAnalysis = "1 Week";
            this.selectedAccountingDayWiseAnalysis = "1 Week";
            $('.oneWeeKClsAcc').addClass('periodicActiveBlock');
            $('.twoWeeKClsAcc, .oneMonthClsAcc, .threeMonthClsAcc').removeClass('periodicActiveBlock');
        } else if (type == '2W') {
            this.dateDiffAcc = 13;
            this.selectedAccountingPeriod = "2 Weeks";
            this.selectedAccountingProcessAnalysis = "2 Weeks";
            this.selectedAccountingDayWiseAnalysis = "2 Weeks";
            $('.twoWeeKClsAcc').addClass('periodicActiveBlock');
            $('.oneWeeKClsAcc, .oneMonthClsAcc, .threeMonthClsAcc').removeClass('periodicActiveBlock');
        } else if (type == '1M') {
            this.dateDiffAcc = 29;
            this.selectedAccountingPeriod = "1 Month";
            this.selectedAccountingProcessAnalysis = "1 Month";
            this.selectedAccountingDayWiseAnalysis = "1 Month";
            $('.oneMonthClsAcc').addClass('periodicActiveBlock');
            $('.oneWeeKClsAcc,.twoWeeKClsAcc, .threeMonthClsAcc').removeClass('periodicActiveBlock');
        } else if (type == '3M') {
            this.dateDiffAcc = 90;
            this.selectedAccountingPeriod = "3 Months";
            this.selectedAccountingProcessAnalysis = "3 Months";
            this.selectedAccountingDayWiseAnalysis = "3 Months";
            $('.threeMonthClsAcc').addClass('periodicActiveBlock');
            $('.oneWeeKClsAcc, .twoWeeKClsAcc, .oneMonthClsAcc').removeClass('periodicActiveBlock');
        }
        /*  this.lineChartDataAcc = [
             { data: this.accountingAnalysisData[type].detailedData.notAccounted, label: 'Not Accounted' },
             { data: this.accountingAnalysisData[type].detailedData.accountingInProcess, label: 'Accounting In Process' },
             { data: this.accountingAnalysisData[type].detailedData.accounted, label: 'Accounted' }
         ]; */
        /* if(this.selectedAccFilterValue == 'Amount'){
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notAccounted, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accountingInProcess, label: 'Accounting In Process' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accounted, label: 'Accounted' }
            ];
        }else{
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notAccounted, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accountingInProcess, label: 'Accounting In Process' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accounted, label: 'Accounted' }
            ];
        } */
        if (this.selectedAccFilterValue == 'Amount') {
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProcAmtPer, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accountedAmtPer, label: 'Pending Journals' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccountedAmtPer, label: 'JE Creation' }
            ];
        } else {
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProc, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accounted, label: 'Pending Journals' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccounted, label: 'JE Creation' }
            ];
        }
        setTimeout(() => this.lineChartLabelsAcc = this.accountingAnalysisData[type].detailedData.labelValue, 0);
        /* this.processFilter.endDate = new Date(this.processFilter.startDate);
        this.processFilter.endDate.setDate(this.processFilter.endDate.getDate() - dateDiff);
        this.processFilter.startDate = new Date(this.processFilter.startDate);
        let obj = {
            "startDate": this.processFilter.endDate,
            "endDate": this.processFilter.startDate
        } */
        /* this.processFilter.endDate = new Date(this.processFilter.endDate);
        this.processFilter.startDate.setDate(this.processFilter.endDate.getDate() - dateDiff);
        this.processFilter.startDate = new Date(this.processFilter.startDate); */
        /*  let obj = {
             "startDate": this.processFilter.startDate,
             "endDate": this.processFilter.endDate
         } */
        let stDate = new Date(this.processFilter.endDate);
        stDate.setDate(stDate.getDate() - this.dateDiffAcc);
        this.accountingDateObj = {
            "startDate": stDate,
            "endDate": this.processFilter.endDate
        }
        console.log('obj ' + JSON.stringify(this.accountingDateObj));
        this.showAccGrpChart = false;
        this.showGrpChartAcc = false;
        this.fetchAccountingDetailedList(this.accountingDateObj);
        this.fetchDetailedInfoForJE(this.accountingDateObj);
    }

    showAccountingSubProcess: boolean = false;
    unAccItemsValueKpi: any;
    unAccItemsViolationKpi: any;
    accAwaitingAppCountKpi: any;
    /* fetchAccountingDetailedList(obj) {
        console.log('obj ::' + JSON.stringify(obj));
        this.navbarService.fetchAccountingDetailedData(this.selectedProcess, obj).subscribe((res: any) => {
            this.accountingDayWiseAnalysisLabels = [];
            this.accountingDayWiseAnalysisData = [];
            console.log('res ' + JSON.stringify(res));
            console.log('this.selectedAccountingStatus ' + this.selectedAccountingStatus);
            this.unAccItemsValueKpi = res.unAccountedItemsValue;
            this.unAccItemsViolationKpi = res.unAccountedItemsViolation;
            this.accAwaitingAppCountKpi = res.awaitingAppCount;
            if(this.selectedAccountingStatus == 'Not Accounted'){
                res.accountingData.forEach(element => {
                    this.accountingDayWiseAnalysisLabels.push(element.viewName);
                    this.accountingDayWiseAnalysisData.push(element.notAccountedper);
                });
            }else if(this.selectedAccountingStatus == 'Accounting In Process'){
                    res.accountingData.forEach(element => {
                        this.accountingDayWiseAnalysisLabels.push(element.viewName);
                        this.accountingDayWiseAnalysisData.push(element.accountingInprocessper);
                    });
            }else if(this.selectedAccountingStatus == 'Accounted'){
                res.accountingData.forEach(element => {
                    this.accountingDayWiseAnalysisLabels.push(element.viewName);
                    this.accountingDayWiseAnalysisData.push(element.accountedper);
                });
            }
            if(this.accountingDayWiseAnalysisData && this.accountingDayWiseAnalysisLabels && this.accountingDayWiseAnalysisLabels.length>0){
                this.showAccountingSubProcess = true;
            }
        });
    } */
    accDetailedList: any;
    tempChartArrAcc: any;
    selectedAccViewId: any;
    selectedAccViewName: any;
    accGroupByList: any;
    accGroupByData: any;
    accgroupBy: any;
    displayAccGrpList: boolean = false;
    selectedAccModuleName: any;
    tempChartViewNameAcc: any = [];
    accItemsValueKpiPer: any;
    accItemsViolationKpiPer: any;
    accAwaitingAppCountKpiPer: any;
    jePendingKpi: any;
    jePendingKpiPer: any;

    fetchAccountingDetailedList(obj) {
        this.tempChartViewNameAcc = [];
        this.blockedDocument = true;
        console.log('fetching Acc Details obj :' + JSON.stringify(obj));
        console.log('fetching Acc Details this.selectedProcess ' + this.selectedProcess);
        this.accGroupByData = [];
        this.navbarService.fetchAccountingDetailedData(this.selectedProcess, this.tempProcessFilter.violationPeriod, obj).subscribe((res: any) => {
            this.accDetailedList = res;
            console.log('Acc Details this.accDetailedList ' + JSON.stringify(this.accDetailedList));
            this.tempChartArrAcc = [];
            this.accountingDayWiseAnalysisLabels = [];
            this.accountingDayWiseAnalysisData = [];
            /* this.accDetailedList.accountingData.forEach(element => {
                let tempObj = [];
                tempObj = [element.notAccountedper, element.accountingInprocessper, element.accountedper];
                this.accountingDayWiseAnalysisData.push(tempObj);
                this.tempChartViewNameAcc.push(element.viewName);
            }); */
            this.accDetailedList.accountingData.forEach(element => {
                let tempObj = [];
                if (this.selectedAccFilterValue == 'Amount') {
                    tempObj = [element.notAcctAndAcctInProcAmtPer, element.accountedamtPer, element.finalAccountedamtPer];
                } else {
                    tempObj = [element.notAcctAndAcctInProcCtPer, element.accountedper, element.finalAccountedper];
                }
                this.accountingDayWiseAnalysisData.push(tempObj);
                /* this.tempChartViewNameAcc.push(element.viewName); */
            });
            this.accountingDayWiseAnalysisLabels = ['Not Accounted', 'Pending Journals', 'JE Creation'];
            console.log('this.accountingDayWiseAnalysisData ' + JSON.stringify(this.accountingDayWiseAnalysisData));
            this.unAccItemsValueKpi = res.unAccountedItemsValue;
            this.unAccItemsViolationKpi = res.unAccountedItemsViolation;
            this.accAwaitingAppCountKpi = res.awaitingAppCount;
            this.accItemsValueKpiPer = res.unAccountedItemsValuePer;
            this.accItemsViolationKpiPer = res.unAccountedItemsViolationPer;
            this.accAwaitingAppCountKpiPer = res.awaitingAppCountPer;
            this.jePendingKpi = res.accountedAmt;
            this.jePendingKpiPer = res.accountedAmtPer;
            if (this.accountingDayWiseAnalysisData.length > 0 && this.accountingDayWiseAnalysisLabels && this.accountingDayWiseAnalysisLabels.length > 0) {
                this.showAccountingSubProcess = true;
                this.selectedAccViewId = this.accDetailedList.accountingData[0].viewId;
                this.selectedAccViewName = this.accDetailedList.accountingData[0].viewName;
                this.navbarService.getReconGroupByLov(this.selectedAccViewId).subscribe((res: any) => {
                    this.accGroupByList = res;
                    this.accGroupByData = res.columnsList;
                    this.accgroupBy = [this.accGroupByData[0].columnAliasName];
                    this.selectedReconGroupBy = this.accGroupByData[0].columnAliasName;
                    console.log('this.accGroupByList ' + JSON.stringify(this.accGroupByList));
                    console.log('this.accGroupByData ' + JSON.stringify(this.accGroupByData));
                    console.log('this.accgroupBy ' + JSON.stringify(this.accgroupBy));
                    if (this.accGroupByData.length > 0) {
                        this.displayAccGrpList = true;
                    }
                    /*  let obj = {
                         "startDate": this.processFilter.startDate,
                         "endDate": this.processFilter.endDate
                     } */
                    console.log('this.tempChartViewNameAcc ' + JSON.stringify(this.tempChartViewNameAcc));
                    this.selectedAccModuleName = 'Not Accounted';
                    this.accGroupByData.forEach(element => {
                        this.fetchUnProcessedOrProcessedDataAcc(this.selectedProcess, this.selectedAccViewId, this.selectedAccModuleName, this.accGroupByList.amtQualifier.amtQualifierAliasName, element.columnAliasName, element.columnName, this.accountingDateObj);
                        this.tempChartViewNameAcc.push(element.columnName);
                        console.log('this.tempChartViewNameAcc ' + JSON.stringify(this.tempChartViewNameAcc));
                        /* this.fetchDetailedInfoForJE(this.selectedAccViewId, element.columnAliasName, obj); */
                    });
                    // this.fetchDetailedInfoForJE(obj);
                });
            } else {
                /* this.notificationsService.info('Info!', 'No Data For Selected Date Range'); */
                this.blockedDocument = false;
                this.showGrpChartAcc = true;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching accounting detailed data');
                this.blockedDocument = false;
            });
    }

    chartClickedAcc(e) {
        if (e.active[0] != undefined) {
            this.blockedDocument = true;
            $('.accItemsValueClass, .accItemsViolationClass, .accAwaitingAppClass').removeClass('active');
            this.accGroupByChartData = [];
            this.accGroupByChartLabels = [];
            console.log('.active[1]._chart.tooltip._active["0"]._datasetIndex ' + e.active[1]._chart.tooltip._active[0]._datasetIndex);
            console.log('.active["0"]._chart.config.data.datasets ' + e.active["0"]._chart.config.data.datasets[e.active[1]._chart.tooltip._active[0]._datasetIndex].label);
            console.log('e.active["0"]._index ' + e.active[0]._index);
            console.log('selected ' + e.active[0]._chart.config.data.labels[e.active[0]._index]);
            console.log('selected Date ' + e.active["0"]._chart.config.data.labels[e.active["0"]._index]);
            console.log('this.accountingAnalysisData ' + JSON.stringify(this.accountingAnalysisData));
            this.selectedAccountingStatus = e.active["0"]._chart.config.data.datasets[e.active[1]._chart.tooltip._active[0]._datasetIndex].label;
            this.selectedAccountingDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];
            /* let obj; */
            if (this.selectedAccountingPeriod == '1 Week') {
                this.showDrillDownAcc = false;
                this.accountingDateObj = {
                    "startDate": new Date(this.accountingAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.accountingAnalysisData['1W'].detailedData.labelDates[e.active["0"]._index])
                }
            } else if (this.selectedAccountingPeriod == '2 Weeks') {
                this.showDrillDownAcc = false;
                this.accountingDateObj = {
                    "startDate": new Date(this.accountingAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.accountingAnalysisData['2W'].detailedData.labelDates[e.active["0"]._index])
                }
            } else if (this.selectedAccountingPeriod == '1 Month') {
                /* obj = {
                    "startDate": new Date(this.accountingAnalysisData['1M'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.accountingAnalysisData['1M'].detailedData.labelDates[e.active["0"]._index])
                } */
                this.accountingDateObj = {
                    "startDate": new Date(this.accountingAnalysisData['1M'].detailedData.labelDatesValue[e.active["0"]._index].startDate),
                    "endDate": new Date(this.accountingAnalysisData['1M'].detailedData.labelDatesValue[e.active["0"]._index].endDate)
                }
                this.fetchWeekAnalysisForModule('accounting', this.accountingDateObj);
                let elem: any = document.querySelector('.drillDownPanelAcc')
                elem.style.height = "250px";
            } else if (this.selectedAccountingPeriod == '3 Months') {
                /* obj = {
                    "startDate": new Date(this.accountingAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.accountingAnalysisData['3M'].detailedData.labelDates[e.active["0"]._index])
                } */
                this.accountingDateObj = {
                    "startDate": new Date(this.accountingAnalysisData['3M'].detailedData.labelDatesValue[e.active["0"]._index].startDate),
                    "endDate": new Date(this.accountingAnalysisData['3M'].detailedData.labelDatesValue[e.active["0"]._index].endDate)
                }
                this.fetchWeekAnalysisForModule('accounting', this.accountingDateObj);
                let elem: any = document.querySelector('.drillDownPanelAcc')
                elem.style.height = "250px";
            }
            console.log('obj ' + JSON.stringify(this.accountingDateObj));
            this.showAccGrpChart = false;
            this.fetchAccountingDetailedList(this.accountingDateObj);
        }
    }

    chartClickedAccountingDrill(e) {
        if (e.active[0] != undefined) {
            this.blockedDocument = true;
            $('.accItemsValueClass, .accItemsViolationClass, .accAwaitingAppClass').removeClass('active');
            this.accGroupByChartData = [];
            this.accGroupByChartLabels = [];
            this.selectedAccountingStatus = e.active["0"]._chart.config.data.datasets[e.active[1]._chart.tooltip._active[0]._datasetIndex].label;
            this.selectedAccountingDayWiseAnalysis = e.active[0]._chart.config.data.labels[e.active[0]._index];

            if (this.selectedAccountingPeriod == '1 Month') {
                this.accountingDateObj = {
                    "startDate": new Date(this.accDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.accDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                }

            } else if (this.selectedAccountingPeriod == '3 Months') {
                this.accountingDateObj = {
                    "startDate": new Date(this.accDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index]),
                    "endDate": new Date(this.accDrillDownData.oneWeek.detailedData.labelDates[e.active["0"]._index])
                }
            }
            this.showAccGrpChart = false;
            this.fetchAccountingDetailedList(this.accountingDateObj);
        }
    }

    reconDisplayChart: boolean = false;
    /* changeReconChartView() {
        this.reconDisplayChart = !this.reconDisplayChart;
    } */
    changeReconChartCol(type) {
        this.reconDisplayChart = !this.reconDisplayChart;
        if (type == 'line') {
            this.reconChartColors = [
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
                    borderColor: 'rgba(52, 168, 83, 0.9)',
                    pointBackgroundColor: 'rgba(52, 168, 83, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(52, 168, 83, 0.5)'
                }
            ];
        } else if (type == 'bar') {
            this.reconChartColors = [
                {
                    backgroundColor: 'rgba(234, 67, 53, 0.9)',
                    borderColor: 'rgba(234, 67, 53, 0.9)',
                    pointBackgroundColor: 'rgba(234, 67, 53, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(234, 67, 53, 0.5)'
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
        }
    }

    showunReconItemValuesDetails: boolean = false;
    showUnReconItemsByValueDetails() {
        this.unReconItemValuesDetails = [];
        console.log('this.reconDetailedList ' + JSON.stringify(this.reconDetailedList));
        if (this.reconDetailedList.reconciliationData.length < 1) {
            this.blockedDocument = false;
        } else {
            this.reconDetailedList.reconciliationData.forEach(element => {
                if (element.unReconciledCount == 0) {
                    this.blockedDocument = false;
                } else {
                    let obj = {
                        "viewName": element.viewName,
                        "viewId": element.viewId,
                        "count": element.unReconciledCount,
                        "amount": element.unReconciledAmt,
                        "reconCount": element.ReconciledCount,
                        "reconAmount": element.reconciledAmt
                    }
                    this.unReconItemValuesDetails.push(obj);
                }
            });
            if (this.unReconItemValuesDetails.length > 0) {

                this.fetchUnReconItemsGrpBy(this.unReconItemValuesDetails[0].viewId);
            }
        }
    }
    showUnReconViolationDetails() {
        this.unReconItemValuesDetails = [];
        console.log('this.reconDetailedList ' + JSON.stringify(this.reconDetailedList));
        if (this.reconDetailedList.reconciliationData.length < 1) {
            this.blockedDocument = false;
        } else {
            this.reconDetailedList.reconciliationData.forEach(element => {
                if (element.violationCount == 0) {
                    this.blockedDocument = false;
                } else {
                    let obj = {
                        "viewName": element.viewName,
                        "viewId": element.viewId,
                        "count": element.violationCount,
                        "amount": element.violationAmount
                    }
                    this.unReconItemValuesDetails.push(obj);
                }
            });
            if (this.unReconItemValuesDetails.length > 0) {
                console.log('this.unReconItemValuesDetails ' + JSON.stringify(this.unReconItemValuesDetails));

                this.fetchUnReconViolationItemsGrpBy(this.unReconItemValuesDetails[0].viewId);
            }
        }
    }

    showApproverChart: boolean = false;
    reconApproverDetailsData: any = [];
    reconApproverDetailsLabels: any = [];
    showUnReconApprovedDetails() {
        /* this.reconApprovalData.forEach(element => {
            this.reconApproverDetailsData.push(element.count);
            this.reconApproverDetailsLabels.push(element.user);
        });
        if(this.reconApproverDetailsData.length>0){
            this.showApproverChart = true;
        } */

        this.unReconItemValuesDetails = [];
        console.log('this.reconDetailedList ' + JSON.stringify(this.reconDetailedList));
        if (this.reconDetailedList.reconciliationData.length < 1) {
            this.blockedDocument = false;
        } else {
            this.reconDetailedList.reconciliationData.forEach(element => {
                if (element.unApprovedCount == 0) {
                    this.blockedDocument = false;
                } else {
                    let obj = {
                        "viewName": element.viewName,
                        "viewId": element.viewId,
                        "count": element.unApprovedCount,
                        "amount": element.unApprovedAmount
                    }
                    this.unReconItemValuesDetails.push(obj);
                }
            });
            if (this.unReconItemValuesDetails.length > 0) {
                console.log('this.unReconItemValuesDetails ' + JSON.stringify(this.unReconItemValuesDetails));

                this.fetchUnReconApprovedItemsGrpBy(this.unReconItemValuesDetails[0].viewId);
            }
        }
    }

    unReconGrpByList: any;
    recongroupByAlias: any;
    unReconGrpByChartsData: any = [];
    unReconGrpByChartsLabels: any = [];
    showUnReconGrpChart: boolean = false;
    fetchUnReconItemsGrpBy(viewId) {
        this.navbarService.getReconGroupByLov(viewId).subscribe((res: any) => {
            this.unReconGrpByChartsData = [];
            this.unReconGrpByChartsLabels = [];
            let tempRes = res;
            this.unReconGrpByList = res.columnsList;
            /* this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName; */
            console.log('this.unReconGrpByList ' + JSON.stringify(this.unReconGrpByList))

            /* let obj = {
                "startDate": this.processFilter.startDate,
                "endDate": this.processFilter.endDate
            } */
            /* let stDate = new Date(this.processFilter.endDate);
            stDate.setDate(stDate.getDate() - this.dateDiff);
            let obj = {
                "startDate": stDate,
                "endDate": this.processFilter.endDate
            } */
            if (this.selectedDetailTabRec == 'Awaiting For Approvals') {
                this.unReconGrpByList.forEach(element => {
                    this.recongroupByAlias = [element.columnAliasName];
                    console.log('recongroupByAlias ' + JSON.stringify(this.recongroupByAlias));
                    let moduleType = "Reconciliation";
                    this.navbarService.getApproversAnalysisData(this.selectedProcess, viewId, moduleType, this.reconDateObj).subscribe((res: any) => {
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.unReconGrpByChartsData.push(objData);
                            this.unReconGrpByChartsLabels.push(objValue);
                            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
                            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                        }
                    });
                });
            } else {
                this.unReconGrpByList.forEach(element => {
                    this.recongroupByAlias = [element.columnAliasName];
                    console.log('recongroupByAlias ' + JSON.stringify(this.recongroupByAlias));
                    this.navbarService.getUnProcessedOrProcessedData(this.selectedProcess, viewId, this.selectedReconModuleName, tempRes.amtQualifier.amtQualifierAliasName, this.recongroupByAlias, this.reconDateObj).subscribe((res: any) => {
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        if (res == null || res == [] || res == '') {
                            this.unReconGrpByChartsData = [];
                            this.unReconGrpByChartsLabels = [];
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                            this.notificationsService.info('Info!', '"No data for Un-Reconciled Items By Value"');
                        }
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.showunReconItemValuesDetails = true;
                            this.unReconGrpByChartsData.push(objData);
                            this.unReconGrpByChartsLabels.push(objValue);
                            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
                            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                        }
                    });
                });
            }
            /* getUnProcessedOrProcessedDataViolation */
            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
        });
    }

    fetchUnReconViolationItemsGrpBy(viewId) {
        this.navbarService.getReconGroupByLov(viewId).subscribe((res: any) => {
            this.unReconGrpByChartsData = [];
            this.unReconGrpByChartsLabels = [];
            let tempRes = res;
            this.unReconGrpByList = res.columnsList;
            /* this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName; */
            console.log('this.unReconGrpByList ' + JSON.stringify(this.unReconGrpByList))

            /* let stDate = new Date(this.processFilter.endDate);
            stDate.setDate(stDate.getDate() - this.dateDiff);
            let obj = {
                "startDate": stDate,
                "endDate": this.processFilter.endDate
            } */

            this.unReconGrpByList.forEach(element => {
                this.recongroupByAlias = [element.columnAliasName];
                console.log('recongroupByAlias ' + JSON.stringify(this.recongroupByAlias));
                let moduleType = "Un-Reconciled";
                this.navbarService.getUnProcessedOrProcessedDataViolation(this.selectedProcess, viewId, this.selectedReconModuleName, tempRes.amtQualifier.amtQualifierAliasName, this.recongroupByAlias, moduleType, this.tempProcessFilter.violationPeriod, this.reconDateObj).subscribe((res: any) => {
                    console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                    let objData = [];
                    let objValue = [];
                    if (res == null || res == [] || res == '') {
                        this.unReconGrpByChartsData = [];
                        this.unReconGrpByChartsLabels = [];
                        this.showUnReconGrpChart = true;
                        this.blockedDocument = false;
                    }
                    res.forEach(element => {
                        objData.push(element.value);
                        objValue.push(element.name);
                    });
                    if (objData.length > 0 && objValue.length > 0) {
                        this.showunReconItemValuesDetails = true;
                        this.unReconGrpByChartsData.push(objData);
                        this.unReconGrpByChartsLabels.push(objValue);
                        console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
                        console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
                        this.showUnReconGrpChart = true;
                        this.blockedDocument = false;
                    }
                });
            });
            /* getUnProcessedOrProcessedDataViolation */
            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
        });
    }

    fetchUnReconApprovedItemsGrpBy(viewId) {
        this.navbarService.getReconGroupByLov(viewId).subscribe((res: any) => {
            this.unReconGrpByChartsData = [];
            this.unReconGrpByChartsLabels = [];
            let tempRes = res;
            this.unReconGrpByList = res.columnsList;
            /* this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName; */
            console.log('this.unReconGrpByList ' + JSON.stringify(this.unReconGrpByList))

            /* let stDate = new Date(this.processFilter.endDate);
            stDate.setDate(stDate.getDate() - this.dateDiff);
            let obj = {
                "startDate": stDate,
                "endDate": this.processFilter.endDate
            } */

            if (this.selectedDetailTabRec == 'Awaiting For Approvals') {
                this.unReconGrpByList.forEach(element => {
                    this.recongroupByAlias = [element.columnAliasName];
                    console.log('recongroupByAlias ' + JSON.stringify(this.recongroupByAlias));
                    let moduleType = "Reconciliation";
                    this.navbarService.getApproversAnalysisData(this.selectedProcess, viewId, moduleType, this.reconDateObj).subscribe((res: any) => {
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.showunReconItemValuesDetails = true;
                            this.unReconGrpByChartsData.push(objData);
                            this.unReconGrpByChartsLabels.push(objValue);
                            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
                            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                        }
                    });
                });
            } else {
                this.unReconGrpByList.forEach(element => {
                    this.recongroupByAlias = [element.columnAliasName];
                    console.log('recongroupByAlias ' + JSON.stringify(this.recongroupByAlias));
                    let moduleType = "Reconciled";
                    this.navbarService.getUnProcessedOrProcessedDataApproved(this.selectedProcess, viewId, this.selectedReconModuleName, tempRes.amtQualifier.amtQualifierAliasName, this.recongroupByAlias, moduleType, this.reconDateObj).subscribe((res: any) => {
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.unReconGrpByChartsData.push(objData);
                            this.unReconGrpByChartsLabels.push(objValue);
                            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
                            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                        }
                    });
                });
            }

            /* getUnProcessedOrProcessedDataViolation */
            console.log('this.unReconGrpByChartsData ' + JSON.stringify(this.unReconGrpByChartsData));
            console.log('this.unReconGrpByChartsLabels ' + JSON.stringify(this.unReconGrpByChartsLabels));
        });
    }

    reconGrpTabFun(e) {
        this.blockedDocument = true;
        console.log('e' + e);
        console.log('selectedTabReconGroup ' + this.selectedTabReconGroup);
        this.fetchUnReconItemsGrpBy(this.unReconItemValuesDetails[e.index].viewId);
    }
    /* changeReconChartView() {
        this.groupedExtractionTransformation = !this.groupedExtractionTransformation;
    } */

    selectedDetailTabAcc: any;
    accGrpByChartsData = [];
    accGrpByChartsLabels = [];
    accItemValuesDetails = [];
    accTabSelected(type) {
        this.blockedDocument = true;
        this.selectedDetailTabAcc = type;
        this.unAccGrpByChartsData = [];
        if (type == 'Un-Accounted Items By Value') {
            this.accGrpByColors1 = [{ backgroundColor: '#f0756a' }];
            $('.accItemsValueClass').addClass('active');
            $('.accItemsViolationClass, .accAwaitingAppClass').removeClass('active');
            this.accGrpByChartsData = [];
            this.accGrpByChartsLabels = [];
            this.accItemValuesDetails = [];
            this.showAccItemsByValueDetails();
        } else if (type == 'Un-Accounted Items By Violation') {
            this.accGrpByColors1 = [{ backgroundColor: '#f0756a' }];
            $('.accItemsViolationClass').addClass('active');
            $('.accItemsValueClass, .accAwaitingAppClass').removeClass('active');
            this.accGrpByChartsData = [];
            this.accGrpByChartsLabels = [];
            this.accItemValuesDetails = [];
            this.showAccViolationDetails();
        } else if (type == 'Awaiting For Approvals') {
            this.accGrpByColors1 = [{ backgroundColor: 'rgba(52, 168, 83, 0.9)' }];
            $('.accAwaitingAppClass').addClass('active');
            $('.accItemsValueClass, .accItemsViolationClass').removeClass('active');
            this.accGrpByChartsData = [];
            this.accGrpByChartsLabels = [];
            this.accItemValuesDetails = [];
            this.showAccApprovalsDetails();
        }
    }


    showAccItemValuesDetails: boolean = false;
    showAccItemsByValueDetails() {
        this.blockedDocument = true;
        this.accItemValuesDetails = [];
        console.log('this.accDetailedList ' + JSON.stringify(this.accDetailedList));
        this.accDetailedList.accountingData.forEach(element => {
            let obj = {
                "viewName": element.viewName,
                "viewId": element.viewId,
                "count": element.notAccountedCount,
                "amount": element.notAccountedamt
            }
            this.accItemValuesDetails.push(obj);
        });
        if (this.accItemValuesDetails.length > 0) {

            this.fetchUnAccItemsGrpBy(this.accItemValuesDetails[0].viewId);
        } else {
            this.blockedDocument = false;
            this.notificationsService.info('Info!', '"No data for Un-Accounted Items By Value"');
        }
    }
    showAccViolationDetails() {
        this.blockedDocument = true;
        this.accItemValuesDetails = [];
        console.log('this.accDetailedList ' + JSON.stringify(this.accDetailedList));
        this.accDetailedList.accountingData.forEach(element => {
            let obj = {
                "viewName": element.viewName,
                "viewId": element.viewId,
                "count": element.violationCount,
                "amount": element.violationAmount
            }
            this.accItemValuesDetails.push(obj);
        });
        if (this.accItemValuesDetails.length > 0) {
            console.log('this.accItemValuesDetails ' + JSON.stringify(this.accItemValuesDetails));

            this.fetchUnAccViolationItemsGrpBy(this.accItemValuesDetails[0].viewId);
        } else {
            this.blockedDocument = false;
            this.notificationsService.info('Info!', '"No data for Un-Accounted Items By Violation"');
        }
    }

    showAccApprovalsDetails() {
        this.blockedDocument = true;
        this.accItemValuesDetails = [];
        console.log('this.accDetailedList ' + JSON.stringify(this.accDetailedList));
        this.accDetailedList.accountingData.forEach(element => {
            let obj = {
                "viewName": element.viewName,
                "viewId": element.viewId,
                "count": element.unApprovedCount,
                "amount": element.unApprovedAmount
            }
            this.accItemValuesDetails.push(obj);
        });
        if (this.accItemValuesDetails.length > 0) {
            console.log('this.accItemValuesDetails ' + JSON.stringify(this.accItemValuesDetails));

            this.fetchUnAccApprovedItemsGrpBy(this.accItemValuesDetails[0].viewId);
        } else {
            this.blockedDocument = false;
            this.notificationsService.info('Info!', 'No data for "Awaiting For Approvals"');
        }
    }

    unAccGrpByList: any;
    accgroupByAlias: any;
    unAccGrpByChartsData: any = [];
    unAccGrpByChartsLabels: any = [];
    showUnAccGrpChart: boolean = false;
    tempKPIValues: any = [];
    fetchUnAccItemsGrpBy(viewId) {
        this.blockedDocument = true;
        this.unAccGrpByList = [];
        this.navbarService.getReconGroupByLov(viewId).subscribe((res: any) => {
            this.unAccGrpByChartsData = [];
            this.unAccGrpByChartsLabels = [];
            let tempRes = res;
            this.tempKPIValues = [];
            this.unAccGrpByList = res.columnsList;
            /* this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName; */
            console.log('this.unAccGrpByList ' + JSON.stringify(this.unAccGrpByList))

            /* let obj = {
                "startDate": this.processFilter.startDate,
                "endDate": this.processFilter.endDate
            } */

            if (this.selectedDetailTabAcc == 'Awaiting For Approvals') {
                this.unAccGrpByList.forEach(element => {
                    this.accgroupByAlias = [element.columnAliasName];
                    console.log('accgroupByAlias ' + JSON.stringify(this.accgroupByAlias));
                    this.navbarService.getApproversAnalysisData(this.selectedProcess, viewId, 'accounting', this.accountingDateObj).subscribe((res: any) => {
                        this.tempKPIValues.push(element.columnName);
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        if (res == null || res == [] || res == '') {
                            this.unAccGrpByChartsData = [];
                            this.unAccGrpByChartsLabels = [];
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                        }
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.showAccItemValuesDetails = true;
                            this.unAccGrpByChartsData.push(objData);
                            this.unAccGrpByChartsLabels.push(objValue);
                            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
                            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
                            this.showAccGrpChart = true;
                            this.blockedDocument = false;
                        }
                    },
                        (res: Response) => {
                            this.onError(res.json()
                            )
                            this.notificationsService.error('Error!', 'Something went wrong while fetching approvers analysis data');
                            this.blockedDocument = false;
                        });
                });
            } else {
                this.unAccGrpByList.forEach(element => {
                    this.accgroupByAlias = [element.columnAliasName];
                    console.log('accgroupByAlias ' + JSON.stringify(this.accgroupByAlias));
                    this.navbarService.getUnProcessedOrProcessedData(this.selectedProcess, viewId, this.selectedAccModuleName, tempRes.amtQualifier.amtQualifierAliasName, this.accgroupByAlias, this.accountingDateObj).subscribe((res: any) => {
                        this.tempKPIValues.push(element.columnName);
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        if (res == null || res == [] || res == '') {
                            this.unAccGrpByChartsData = [];
                            this.unAccGrpByChartsLabels = [];
                            this.showUnReconGrpChart = true;
                            this.blockedDocument = false;
                        }
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.showAccItemValuesDetails = true;
                            this.unAccGrpByChartsData.push(objData);
                            this.unAccGrpByChartsLabels.push(objValue);
                            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
                            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
                            this.showAccGrpChart = true;
                            this.blockedDocument = false;
                        }
                    },
                        (res: Response) => {
                            this.onError(res.json()
                            )
                            this.notificationsService.error('Error!', 'Something went wrong while fetching unprocessed or processed data for accounting');
                            this.blockedDocument = false;
                        });
                });
            }
            /* getUnProcessedOrProcessedDataViolation */
            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
        });
    }

    fetchUnAccViolationItemsGrpBy(viewId) {
        this.blockedDocument = true;
        this.navbarService.getReconGroupByLov(viewId).subscribe((res: any) => {
            this.unAccGrpByChartsData = [];
            this.unAccGrpByChartsLabels = [];
            let tempRes = res;
            this.tempKPIValues = [];
            this.unAccGrpByList = res.columnsList;
            /* this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName; */
            console.log('this.unAccGrpByList ' + JSON.stringify(this.unAccGrpByList))

            /* let obj = {
                "startDate": this.processFilter.startDate,
                "endDate": this.processFilter.endDate
            } */

            this.unAccGrpByList.forEach(element => {
                this.accgroupByAlias = [element.columnAliasName];
                console.log('accgroupByAlias ' + JSON.stringify(this.accgroupByAlias));
                let moduleType = "Not Accounted";
                this.navbarService.getUnProcessedOrProcessedDataViolation(this.selectedProcess, viewId, this.selectedAccModuleName, tempRes.amtQualifier.amtQualifierAliasName, this.accgroupByAlias, moduleType, this.tempProcessFilter.violationPeriod, this.accountingDateObj).subscribe((res: any) => {
                    this.tempKPIValues.push(element.columnName);
                    console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                    let objData = [];
                    let objValue = [];
                    if (res == null || res == [] || res == '') {
                        this.unAccGrpByChartsData = [];
                        this.unAccGrpByChartsLabels = [];
                        this.showAccGrpChart = true;
                        this.blockedDocument = false;
                    }
                    res.forEach(element => {
                        objData.push(element.value);
                        objValue.push(element.name);
                    });
                    if (objData.length > 0 && objValue.length > 0) {
                        this.showAccItemValuesDetails = true;
                        this.unAccGrpByChartsData.push(objData);
                        this.unAccGrpByChartsLabels.push(objValue);
                        console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
                        console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
                        this.showAccGrpChart = true;
                        this.blockedDocument = false;
                    }
                });
            });
            /* getUnProcessedOrProcessedDataViolation */
            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
        });
    }

    fetchUnAccApprovedItemsGrpBy(viewId) {
        this.blockedDocument = true;
        this.navbarService.getReconGroupByLov(viewId).subscribe((res: any) => {
            this.unAccGrpByChartsData = [];
            this.unAccGrpByChartsLabels = [];
            let tempRes = res;
            this.unAccGrpByList = res.columnsList;
            this.tempKPIValues = [];
            /* this.selectedReconGroupBy = this.reconGroupByData[0].columnAliasName; */
            console.log('this.unAccGrpByList ' + JSON.stringify(this.unAccGrpByList))

            /* let obj = {
                "startDate": this.processFilter.startDate,
                "endDate": this.processFilter.endDate
            } */

            if (this.selectedDetailTabAcc == 'Awaiting For Approvals') {
                this.unAccGrpByList.forEach(element => {
                    this.accgroupByAlias = [element.columnAliasName];
                    console.log('accgroupByAlias ' + JSON.stringify(this.accgroupByAlias));
                    let moduleType = "accounting";
                    this.navbarService.getApproversAnalysisData(this.selectedProcess, viewId, moduleType, this.accountingDateObj).subscribe((res: any) => {
                        this.tempKPIValues.push(element.columnName);
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        if (res == null || res == [] || res == '') {
                            this.unAccGrpByChartsData = [];
                            this.unAccGrpByChartsLabels = [];
                            this.showAccGrpChart = true;
                        }
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.showAccItemValuesDetails = true;
                            this.unAccGrpByChartsData.push(objData);
                            this.unAccGrpByChartsLabels.push(objValue);
                            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
                            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
                            this.showAccGrpChart = true;
                            this.blockedDocument = false;
                        } else {
                            this.blockedDocument = false;
                            this.notificationsService.info('Info!', 'No data for "Awaiting For Approvals"');
                        }
                    },
                        (res: Response) => {
                            this.onError(res.json()
                            )
                            this.notificationsService.error('Error!', 'Something went wrong while fetching "Awaiting For Approvals" data');
                            this.blockedDocument = false;
                        });
                });
            } else {
                this.unAccGrpByList.forEach(element => {
                    this.accgroupByAlias = [element.columnAliasName];
                    console.log('accgroupByAlias ' + JSON.stringify(this.accgroupByAlias));
                    let moduleType = "accounted";
                    this.navbarService.getUnProcessedOrProcessedDataApproved(this.selectedProcess, viewId, this.selectedAccModuleName, tempRes.amtQualifier.amtQualifierAliasName, this.accgroupByAlias, moduleType, this.accountingDateObj).subscribe((res: any) => {
                        this.tempKPIValues.push(element.columnName);
                        console.log('getUnProcessedOrProcessedData ' + JSON.stringify(res));
                        let objData = [];
                        let objValue = [];
                        if (res == null || res == [] || res == '') {
                            this.unAccGrpByChartsData = [];
                            this.unAccGrpByChartsLabels = [];
                            this.showAccGrpChart = true;
                        }
                        res.forEach(element => {
                            objData.push(element.value);
                            objValue.push(element.name);
                        });
                        if (objData.length > 0 && objValue.length > 0) {
                            this.showAccItemValuesDetails = true;
                            this.unAccGrpByChartsData.push(objData);
                            this.unAccGrpByChartsLabels.push(objValue);
                            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
                            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
                            this.showAccGrpChart = true;
                            this.blockedDocument = false;
                        }
                    });
                });
            }
            /* getUnProcessedOrProcessedDataViolation */
            console.log('this.unAccGrpByChartsData ' + JSON.stringify(this.unAccGrpByChartsData));
            console.log('this.unAccGrpByChartsLabels ' + JSON.stringify(this.unAccGrpByChartsLabels));
        });
    }



    selectedTabAccGroup: any = 0;
    accGrpTabFun(e) {
        this.blockedDocument = true;
        console.log('e' + e);
        this.fetchUnAccItemsGrpBy(this.accItemValuesDetails[e.index].viewId);
    }
    accDisplayChart: boolean = false;
    public dayWiseColorsAccounting: {}[] = [{ backgroundColor: ['rgba(234, 67, 53, 0.9)', 'rgba(251,188,5, 0.9)', 'rgba(52, 168, 83, 0.9)'] }];
    changeAccChartCol(type) {
        this.accDisplayChart = !this.accDisplayChart;
        if (type == 'line') {
            this.lineChartColorsAccounting = [
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
                    borderColor: 'rgba(251,188,5, 0.9)',
                    pointBackgroundColor: 'rgba(251,188,5, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(251,188,5, 0.5)'
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
        } else if (type == 'bar') {
            this.lineChartColorsAccounting = [
                {
                    backgroundColor: 'rgba(234, 67, 53, 0.9)',
                    borderColor: 'rgba(234, 67, 53, 0.9)',
                    pointBackgroundColor: 'rgba(234, 67, 53, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(234, 67, 53, 0.5)'
                },
                {
                    backgroundColor: 'rgba(251,188,5, 0.9)',
                    borderColor: 'rgba(251,188,5, 0.9)',
                    pointBackgroundColor: 'rgba(251,188,5, 0.5)',
                    pointBorderColor: '#fff',
                    pointHoverBackgroundColor: '#fff',
                    pointHoverBorderColor: 'rgba(251,188,5, 0.5)'
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
        }
    }



    journalsDetailedList: any;
    journalsUnPostedItemsKpi: any;
    journalsUnPostedItemsViolationKpi: any;
    fetchJournalsDetailedList(obj) {
        this.navbarService.getJournalsAnalysisData(this.selectedProcess, obj).subscribe((res: any) => {
            this.journalsDetailedList = res;

            this.journalsUnPostedItemsKpi = res.unPostedItemsValue;
            this.journalsUnPostedItemsViolationKpi = res.unPostedItemsViolation;
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something went wrong while fetching journals detailed data');
                this.blockedDocument = false;
            });
    }

    journalsPeriodDetails(type) {
        let dateDiff = 0;
        this.lineChartLabelsJE = [];
        this.lineChartDataJournals = [
            { data: [], label: 'JE Un-Posted' },
            { data: [], label: 'JE Posted' }
        ];

        //$('.accItemsValueClass, .accItemsViolationClass, .accAwaitingAppClass').removeClass('active');
        if (type == '1W') {
            dateDiff = 6;
            this.selectedJournalsPeriod = "1 Week";
            this.selectedJournalsProcessAnalysis = "1 Week";
            this.selectedJournalsDayWiseAnalysis = '1 Week';
            $('.oneWeeKClsJournals').addClass('periodicActiveBlock');
            $('.twoWeeKClsJournals, .oneMonthClsJournals, .threeMonthClsJournals').removeClass('periodicActiveBlock');
        } else if (type == '2W') {
            dateDiff = 13;
            this.selectedJournalsPeriod = "2 Weeks";
            this.selectedJournalsProcessAnalysis = "2 Weeks";
            this.selectedJournalsDayWiseAnalysis = '2 Weeks';
            $('.twoWeeKClsJournals').addClass('periodicActiveBlock');
            $('.oneWeeKClsJournals, .oneMonthClsJournals, .threeMonthClsJournals').removeClass('periodicActiveBlock');
        } else if (type == '1M') {
            dateDiff = 29;
            this.selectedJournalsPeriod = "1 Month";
            this.selectedJournalsProcessAnalysis = "1 Month";
            this.selectedJournalsDayWiseAnalysis = '1 Month';
            $('.oneMonthClsJournals').addClass('periodicActiveBlock');
            $('.oneWeeKClsJournals,.twoWeeKClsJournals, .threeMonthClsJournals').removeClass('periodicActiveBlock');
        } else if (type == '3M') {
            dateDiff = 90;
            this.selectedJournalsPeriod = "3 Months";
            this.selectedJournalsProcessAnalysis = "3 Months";
            this.selectedJournalsDayWiseAnalysis = '3 Months';
            $('.threeMonthClsJournals').addClass('periodicActiveBlock');
            $('.oneWeeKClsJournals, .twoWeeKClsJournals, .oneMonthClsJournals').removeClass('periodicActiveBlock');
        }
        console.log('type ' + type);
        this.lineChartDataJournals = [
            { data: this.accountingAnalysisData[type].detailedData.finalNotAccounted, label: 'JE Un-Posted' },
            { data: this.accountingAnalysisData[type].detailedData.finalAccounted, label: 'JE Posted' }
        ];
        setTimeout(() => this.lineChartLabelsJE = this.accountingAnalysisData[type].detailedData.labelValue, 0);
        /* this.processFilter.endDate = new Date(this.processFilter.startDate);
        this.processFilter.endDate.setDate(this.processFilter.endDate.getDate() - dateDiff);
        this.processFilter.startDate = new Date(this.processFilter.startDate);
        let obj = {
            "startDate": this.processFilter.endDate,
            "endDate": this.processFilter.startDate
        } */
        this.processFilter.endDate = new Date(this.processFilter.endDate);
        this.processFilter.startDate.setDate(this.processFilter.endDate.getDate() - dateDiff);
        this.processFilter.startDate = new Date(this.processFilter.startDate);
        let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        }
        console.log('obj ' + JSON.stringify(obj));
        /* this.fetchJournalsDetailedList(obj); */
    }

    fetchAllModuleData() {
        /* {
            "processId": 1,
            "asOfValue": true,
            "startDate": "2018-03-01T00:00:00.000Z",
            "endDate": "2018-03-28T00:00:00.000Z",
            "closedPeriods": true,
            "selectedCalender": "2018-02-01"
        } */
        console.log('tempProcessFilter ' + JSON.stringify(this.tempProcessFilter));
        this.tempProcessFilter.startDate = '';
        this.tempProcessFilter.endDate = '';
        if (this.tempProcessFilter.asOfValue == false) {
            this.tempProcessFilter.startDate = this.tempProcessFilter.startDateSel;
            /* this.processFilter.startDate = new Date(this.processFilter.startDate); */
            this.tempProcessFilter.endDate = this.tempProcessFilter.endDateSel;
            /* this.processFilter.endDate = new Date(this.processFilter.endDate); */
            console.log('this.tempProcessFilter.startDate :' + this.tempProcessFilter.startDate);
            console.log('this.tempProcessFilter.endDate :' + this.tempProcessFilter.endDate);
        } else if (this.tempProcessFilter.asOfValue == true && (this.tempProcessFilter.closedPeriods == false || this.tempProcessFilter.closedPeriods == undefined)) {
            this.tempProcessFilter.startDate = this.calenderDetails.startDate;
            this.tempProcessFilter.startDate = new Date(this.tempProcessFilter.startDate);
            this.tempProcessFilter.endDate = this.calenderDetails.currentdate;
            this.tempProcessFilter.endDate = new Date(this.tempProcessFilter.endDate);
            console.log('this.tempProcessFilter.startDate :' + this.tempProcessFilter.startDate);
            console.log('this.tempProcessFilter.endDate :' + this.tempProcessFilter.endDate);
        } else if (this.tempProcessFilter.asOfValue == true && this.tempProcessFilter.closedPeriods == true) {
            this.tempProcessFilter.startDate = this.tempProcessFilter.selectedCalender;
            this.tempProcessFilter.startDate = new Date(this.tempProcessFilter.startDate);
            this.tempProcessFilter.endDate = this.calenderDetails.currentdate;
            this.tempProcessFilter.endDate = new Date(this.tempProcessFilter.endDate);
            console.log('this.tempProcessFilter.startDate :' + this.tempProcessFilter.startDate);
            console.log('this.tempProcessFilter.endDate :' + this.tempProcessFilter.endDate);
        }

        let obj = {
            "startDate": this.tempProcessFilter.startDate,
            "endDate": this.tempProcessFilter.endDate
        };
        this.fetchSelectedProcessDetails(this.selectedProcess);
        this.fetchAllModuleDetails(obj);

    }
    blockDocument() {
        this.blockedDocument = true;
        setTimeout(() => {
            this.blockedDocument = false;
        }, 2000);
    }

    openDialog(key, val): void {
        let dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
            width: '250px',
            data: { key: key, val: val }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result != undefined) {
                this.dashBoardLayOutObj.etl.headerNameTitle = result;
            }

            //this.dashBoardLayOutObj[key] = result;
            // console.log('this.dashBoardLayOutObj ' + JSON.stringify(this.dashBoardLayOutObj));
        });
    }

    SaveCustomizedData() {
        this.customizationMode = false;
        let obj = {
            "value": JSON.stringify(this.dashBoardLayOutObj)
        }
        this.navbarService.postFormConfigDetails('dashboard', 'customHeadings', 'user', obj).subscribe((res: any) => {
            /* console.log('res ' + JSON.stringify(res)); */
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationsService.error('Error!', 'Something Went Wrong While Posting');
                this.blockedDocument = false;
            });
    }

    /* getCustomizedData(){
        this.navbarService.getFormConfigDetails('dashboard', 'customHeadings', 'user').subscribe((res: any) => {
            this.dashBoardLayOutObj = JSON.stringify(JSON.parse(res.value));
            this.dashBoardLayOutObjTemp = JSON.stringify(JSON.parse(res.value));
            console.log('res ' + JSON.stringify(JSON.parse(res.value)));
        },
        (res: Response) => {
            this.onError(res.json()
            )
            this.notificationsService.error('Error!', 'Something Went Wrong While Fetching');
            this.blockedDocument = false;
        });
    } */

    changeReconGrp: boolean = false;
    changeReconGrpBy(val: any) {
        let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        };
        this.selectedReconFilterValue = val.value;

        if (this.selectedReconFilterValue == 'Amount') {
            $('#md-button-toggle-2').removeClass('mat-button-toggle-checked');
            $('#md-button-toggle-1').addClass('mat-button-toggle-checked');
            this.lineChartData = [
                { data: this.reconciliationAnalysisData[this.reconSelectedRange].detailedData.unReconAmtPer, label: 'Un-Reconciled' },
                { data: this.reconciliationAnalysisData[this.reconSelectedRange].detailedData.reconAmtPer, label: 'Reconciled' }
            ];
            this.lineChartDataDrill = [
                { data: this.reconDrillDownData.oneWeek.detailedData.unReconAmtPer, label: 'Un-Reconciled' },
                { data: this.reconDrillDownData.oneWeek.detailedData.reconAmtPer, label: 'Reconcilied' }
            ];
        } else {
            $('#md-button-toggle-1').removeClass('mat-button-toggle-checked');
            $('#md-button-toggle-2').addClass('mat-button-toggle-checked');
            this.lineChartData = [
                { data: this.reconciliationAnalysisData[this.reconSelectedRange].detailedData.unRecon, label: 'Un-Reconciled' },
                { data: this.reconciliationAnalysisData[this.reconSelectedRange].detailedData.recon, label: 'Reconciled' }
            ];
            this.lineChartDataDrill = [
                { data: this.reconDrillDownData.oneWeek.detailedData.unRecon, label: 'Un-Reconciled' },
                { data: this.reconDrillDownData.oneWeek.detailedData.recon, label: 'Reconcilied' }
            ];
        }
        this.barChartData1 = [];
        this.tempChartViewName = [];
        this.reconDetailedList.reconciliationData.forEach(element => {
            let tempObj = [];
            if (this.selectedReconFilterValue == 'Amount') {
                tempObj = [element.unReconciledAmtPer, element.reconciledAmtPer];
            } else {
                tempObj = [element.unReconciledPer, element.reconciledPer];
            }
            this.barChartData1.push(tempObj);
            this.tempChartViewName.push(element.viewName);
        });
    }

    changeAccGrpBy(val: any) {
        /* let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        }; */
        this.selectedAccFilterValue = val.value;

        /* if (this.selectedAccFilterValue == 'Amount') {
            $('#md-button-toggle-2').removeClass('mat-button-toggle-checked');
            $('#md-button-toggle-1').addClass('mat-button-toggle-checked');
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notAccountedAmtPer, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProcAmtPer, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccountedAmtPer, label: 'JE Posted' }
            ];
        } else {
            $('#md-button-toggle-1').removeClass('mat-button-toggle-checked');
            $('#md-button-toggle-2').addClass('mat-button-toggle-checked');
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notAccounted, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProc, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccounted, label: 'JE Posted' }
            ];
        } */
        if (this.selectedAccFilterValue == 'Amount') {
            $('#md-button-toggle-2').removeClass('mat-button-toggle-checked');
            $('#md-button-toggle-1').addClass('mat-button-toggle-checked');
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProcAmtPer, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accountedAmtPer, label: 'Pending Journals' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccountedAmtPer, label: 'JE Creation' }
            ];
        } else {
            $('#md-button-toggle-1').removeClass('mat-button-toggle-checked');
            $('#md-button-toggle-2').addClass('mat-button-toggle-checked');
            this.lineChartDataAcc = [
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.notActAndAcctInProc, label: 'Not Accounted' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.accounted, label: 'Pending Journals' },
                { data: this.accountingAnalysisData[this.accSelectedRange].detailedData.finalAccounted, label: 'JE Creation' }
            ];
        }
        this.accountingDayWiseAnalysisData = [];
        this.tempChartViewNameAcc = [];

        this.accDetailedList.accountingData.forEach(element => {
            let tempObj = [];
            if (this.selectedAccFilterValue == 'Amount') {
                tempObj = [element.notAcctAndAcctInProcAmtPer, element.accountedamtPer, element.finalAccountedamtPer];
            } else {
                tempObj = [element.notAcctAndAcctInProcCtPer, element.accountedper, element.finalAccountedper];
            }
            this.accountingDayWiseAnalysisData.push(tempObj);
            this.tempChartViewNameAcc.push(element.viewName);
        });
    }

    changeAccProcessView(e) {
        console.log('e ' + e.value);
        if (e.value == 'Account') {
            this.showAccProcess = true;
        } else {
            this.showAccProcess = false;
        }
    }

    journalsAccDetails: any = [];
    fetchDetailedInfoForJE(obj) {
        console.log('selectedAccountType ' + this.selectedAccountType);
        this.journalsAccDetails = [];
        if (this.selectedAccountType != "None") {
            this.navbarService.getdetailInfoForJE(this.selectedProcess, obj, this.selectedAccountType).subscribe((res: any) => {
                this.journalsAccDetails = res;
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.notificationsService.error('Error!', 'Something Went Wrong while fetching detail info for JE');
                    this.blockedDocument = false;
                });
        } else {
            this.navbarService.getdetailInfoForJE(this.selectedProcess, obj).subscribe((res: any) => {
                this.journalsAccDetails = res;
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.notificationsService.error('Error!', 'Something Went Wrong while fetching detail info for JE');
                    this.blockedDocument = false;
                });
        }
        /* this.navbarService.getdetailInfoForJE(this.selectedProcess, viewId, grpBy, obj).subscribe((res: any) => {
            this.journalsAccDetails = res;
        }); */
    }

    public barChartOptionsJE: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        maintainAspectRatio: false,
        legend: {
            position: 'bottom',
            display: false
        },
        plugins: {
            datalabels: {
                display: false,
            }
        },
        scales: {
            yAxes: [
                {
                    ticks: {
                        /* min: 0, */
                        callback: function (value) {
                            if (value >= 1000000000000) {
                                return (value / 1000000000000).toFixed(1).replace(/\.0$/, '') + 'T';
                            }
                            if (value >= 1000000000) {
                                return (value / 1000000000).toFixed(1).replace(/\.0$/, '') + 'B';
                            }
                            if (value >= 1000000) {
                                return (value / 1000000).toFixed(1).replace(/\.0$/, '') + 'M';
                            }
                            if (value >= 1000) {
                                return (value / 1000).toFixed(1).replace(/\.0$/, '') + 'K';
                            }
                            if (value <= -1000000000000) {
                                return (value / 1000000000000).toFixed(1).replace(/\.0$/, '') + 'T';
                            }
                            if (value <= -1000000000) {
                                return (value / 1000000000).toFixed(1).replace(/\.0$/, '') + 'B';
                            }
                            if (value <= -1000000) {
                                return (value / 1000000).toFixed(1).replace(/\.0$/, '') + 'M';
                            }
                            if (value <= -1000) {
                                return (value / 1000).toFixed(1).replace(/\.0$/, '') + 'K';
                            }
                            return value;
                        }
                    }
                }
            ]
        }
    };
    public barChartLabelsJE: any[] = [/* '2006', '2007', '2008', '2009', '2010', '2011', '2012' */];
    public barChartTypeJE: string = 'bar';
    public barChartLegendJE: boolean = true;

    public barChartDataJE: any[] = [/* 
        {data: [65, 59, 80, 81, 56, 55, 40], label: 'Series A'},
        {data: [28, 48, 40, 19, 86, 27, 90], label: 'Series B'} */
    ];
    journalsAccDetailsByView: any = [];
    tempJEAccGrpName: any = [];
    fetchDetailedInfoForJEByView(viewId, status, obj) {
        this.journalsAccDetailsByView = [];
        this.barChartDataJE = [];
        this.barChartLabelsJE = [];
        this.accGroupByData.forEach((element, i) => {
            let tempLab = [];
            let tempData = [];
            this.navbarService.getdetailInfoForJEByView(this.selectedProcess, viewId, element.columnAliasName, status, obj).subscribe((res: any) => {
                if (res.length > 0) {
                    this.tempJEAccGrpName.push(element.columnName);
                    //this.journalsAccDetailsByView.push(temp);
                    res.forEach(element1 => {
                        tempLab.push(element1[element.columnAliasName]);
                        let obj = {
                            data: [element1.balance], label: element1.description
                        }
                        tempData.push(obj);
                    });
                    this.barChartLabelsJE.push(tempLab);
                    this.barChartDataJE.push(tempData);
                    console.log('this.barChartDataJE ' + JSON.stringify(this.barChartDataJE));
                    if ((this.accGroupByData.length == this.barChartDataJE.length) && this.barChartDataJE.length > 0 && this.barChartLabelsJE) {
                        this.showViewSource = true;
                        console.log('this.showViewSource ' + this.showViewSource);
                    }
                }
            },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.notificationsService.error('Error!', 'Something Went Wrong while fetching detail info for JE');
                    this.blockedDocument = false;
                });
        });

        /* if(this.accGroupByData.length == this.journalsAccDetailsByView.length){
            this.journalsAccDetailsByView[0].forEach(element => {
                this.barChartLabelsJE.push
            });
        } */
    }

    backToOriginalView() {
        this.showDrillDown = false;
        let elem: any = document.querySelector('.drillDownPanel')
        elem.style.height = "0px";
    }

    backToOriginalViewAcc() {
        this.showDrillDownAcc = false;
        let elem: any = document.querySelector('.drillDownPanelAcc')
        elem.style.height = "0px";
    }

    backToOriginalViewExtraction() {
        if (this.showDrillDownExt) {
            this.showDrillDownExt = false;
            let elem: any = document.querySelector('.drillDownPanelExt');
            elem.style.height = "0px";
        }
        if (this.showDrillDownTrans) {
            this.showDrillDownTrans = false;
            let elem1: any = document.querySelector('.drillDownPanelTrans');
            elem1.style.height = "0px";
        }
        if (this.showDrillDownETL) {
            this.showDrillDownETL = false;
            let elem2: any = document.querySelector('.drillDownPanelETL');
            elem2.style.height = "0px";
        }
    }

    /* ChangeSelectedAccType(){
        let obj = {
            "startDate": this.processFilter.startDate,
            "endDate": this.processFilter.endDate
        }
        this.fetchDetailedInfoForJE(obj);
    } */

    reconDrillDownData: any = [];
    extractionDrillDownData: any = [];
    transformationDrillDownData: any = [];
    extractionAndTransformationDataDrillDown: any = [];
    extractionAndTransformationLabelsDrillDown: any = [];
    accDrillDownData: any = [];
    showDrillDownAcc: boolean = false;
    showDrillDownExt: boolean = false;

    fetchWeekAnalysisForModule(moduleName, obj) {
        if (moduleName == "extraction") {
            /* this.showDrillDownExt = true; */
            this.navbarService.getWeekAnalysisForModule(this.selectedProcess, moduleName, obj).subscribe((res: any) => {
                this.extractionDrillDownData = res;
                this.lineChartDataExtractionDrillDown = [
                    { data: this.extractionDrillDownData.oneWeek.detailedData.extractionFailed, label: 'Extraction Failed' },
                    { data: this.extractionDrillDownData.oneWeek.detailedData.extracted, label: 'Extracted' }
                ];
                this.lineChartLabelsExtractionDrillDown = this.extractionDrillDownData.oneWeek.detailedData.labelValue;

                if (this.lineChartDataExtractionDrillDown[0].data.length > 0 && this.lineChartLabelsExtractionDrillDown && this.lineChartLabelsExtractionDrillDown.length > 0) {
                    this.showDrillDownExt = true;
                }
            });
        } else if (moduleName == "transformation") {
            /* this.showDrillDownTrans = true; */
            this.navbarService.getWeekAnalysisForModule(this.selectedProcess, moduleName, obj).subscribe((res: any) => {
                this.transformationDrillDownData = res;
                this.lineChartDataTransformationDrillDown = [
                    { data: this.transformationDrillDownData.oneWeek.detailedData.transformationFailed, label: 'Transformation Failed' },
                    { data: this.transformationDrillDownData.oneWeek.detailedData.transformed, label: 'Transformed' }
                ];
                this.lineChartLabelsTransformationDrillDown = this.transformationDrillDownData.oneWeek.detailedData.labelValue;

                if (this.lineChartDataTransformationDrillDown[0].data.length > 0 && this.lineChartLabelsTransformationDrillDown && this.lineChartLabelsTransformationDrillDown.length > 0) {
                    this.showDrillDownTrans = true;
                }
            });
        } else if (moduleName == "etl") {
            this.navbarService.getWeekAnalysisForModule(this.selectedProcess, 'extraction', obj).subscribe((res: any) => {
                this.extractionDrillDownData = res;
                this.navbarService.getWeekAnalysisForModule(this.selectedProcess, 'transformation', obj).subscribe((res: any) => {
                    this.transformationDrillDownData = res;
                    this.extractionAndTransformationDataDrillDown = [
                        { data: this.extractionDrillDownData.oneWeek.detailedData.extractionFailed, label: 'Extraction Failed' },
                        { data: this.extractionDrillDownData.oneWeek.detailedData.extracted, label: 'Extracted' },
                        { data: this.transformationDrillDownData.oneWeek.detailedData.transformationFailed, label: 'Transformation Failed' },
                        { data: this.transformationDrillDownData.oneWeek.detailedData.transformed, label: 'Transformed' }
                    ];
                    this.extractionAndTransformationLabelsDrillDown = this.transformationDrillDownData.oneWeek.detailedData.labelValue;
                    if (this.extractionAndTransformationDataDrillDown[0].data.length > 0 && this.extractionAndTransformationLabelsDrillDown && this.extractionAndTransformationLabelsDrillDown.length > 0) {
                        this.showDrillDownETL = true;
                    }
                });
            });
        } else if (moduleName == "reconciliation") {
            this.navbarService.getWeekAnalysisForModule(this.selectedProcess, moduleName, obj).subscribe((res: any) => {
                this.reconDrillDownData = res;
                if (this.selectedReconFilterValue == 'Amount') {
                    this.lineChartDataDrill = [
                        { data: this.reconDrillDownData.oneWeek.detailedData.unReconAmtPer, label: 'Un-Reconciled' },
                        { data: this.reconDrillDownData.oneWeek.detailedData.reconAmtPer, label: 'Reconcilied' }
                    ];
                } else {
                    this.lineChartDataDrill = [
                        { data: this.reconDrillDownData.oneWeek.detailedData.unRecon, label: 'Un-Reconciled' },
                        { data: this.reconDrillDownData.oneWeek.detailedData.recon, label: 'Reconcilied' }
                    ];
                }
                this.lineChartLabelsReconDrill = this.reconDrillDownData.oneWeek.detailedData.labelValue;

                if (this.lineChartDataDrill[0].data.length > 0 && this.lineChartLabelsReconDrill && this.lineChartLabelsReconDrill.length > 0) {
                    this.showDrillDown = true;
                }
            });
        } else if (moduleName == "accounting") {
            this.navbarService.getWeekAnalysisForModule(this.selectedProcess, moduleName, obj).subscribe((res: any) => {
                this.accDrillDownData = res;
                if (this.selectedAccFilterValue == 'Amount') {
                    this.lineChartDataAccDrill = [
                        { data: this.accDrillDownData.oneWeek.detailedData.notActAndAcctInProcAmtPer, label: 'Not Accounted' },
                        { data: this.accDrillDownData.oneWeek.detailedData.accountedAmtPer, label: 'Pending Journals' },
                        { data: this.accDrillDownData.oneWeek.detailedData.finalAccountedAmtPer, label: 'JE Creation' }
                    ];
                } else {
                    this.lineChartDataAccDrill = [
                        { data: this.accDrillDownData.oneWeek.detailedData.notActAndAcctInProc, label: 'Not Accounted' },
                        { data: this.accDrillDownData.oneWeek.detailedData.accounted, label: 'Pending Journals' },
                        { data: this.accDrillDownData.oneWeek.detailedData.finalAccounted, label: 'JE Creation' }
                    ];
                }
                this.lineChartLabelsAccDrill = this.accDrillDownData.oneWeek.detailedData.labelValue;

                if (this.lineChartDataAccDrill[0].data.length > 0 && this.lineChartLabelsAccDrill && this.lineChartLabelsAccDrill.length > 0) {
                    this.showDrillDownAcc = true;
                }
            });
        }
    }


    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'dialog-overview-example-dialog',
    templateUrl: 'dialogForEdit.html',
})
export class DialogOverviewExampleDialog {

    constructor(
        public dialogRef: MdDialogRef<DialogOverviewExampleDialog>,
        @Inject(MD_DIALOG_DATA) public data: any) { }

    onNoClick(): void {
        this.dialogRef.close(this.data);
    }

}