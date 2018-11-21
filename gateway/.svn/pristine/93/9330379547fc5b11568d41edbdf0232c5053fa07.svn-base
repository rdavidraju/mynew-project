import { Component, OnInit, ViewChild, ChangeDetectionStrategy, TemplateRef } from '@angular/core';
import { CommonService } from '../common.service';
import { DashBoard4 } from '../../home/home.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ng2-webstorage';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { DecimalPipe } from '@angular/common';
import Chart from 'chart.js';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-dashboardv6',
    templateUrl: './dashboard-v6.component.html',
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

export class DashboardV6Component implements OnInit {
    userDetails: any;
    processesList: any;
    testReconWidth: any = '300';
    testReconHeight: any = '300';
    processFilter = new DashBoard4();
    selectedTab: any = 0;
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
    selectedUnReconItemsViolationValue:any = ">= 415";
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
                formatter: function(value, context) {
                    return value + '%';
                }
            }
        }
    };
    public pieChartReconciliationColors: {}[] = [{ backgroundColor: [this.reconColor, this.unReconColor] }];

    /* UN-RECONCILIED BY AGING BUCKET */
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
    topTenUnReconItemData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,73,56,578.97", "Percentage": 14.9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,48,78,278.59", "Percentage": 14.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "4,18,21,781.67", "Percentage": 13.2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "3,34,40,452.21", "Percentage": 10.5 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,93,171.42", "Percentage": 9.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,84,16,066.87", "Percentage": 9 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,73,33,868.12", "Percentage": 8.6 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,34,94,099.10", "Percentage": 7.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,16,29,831.66", "Percentage": 6.8 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Sum Of Amount": "2,03,62,004.36", "Percentage": 6.4 }];
    unReconItemByViolationData: any = [{ "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 415, "Sum Of Amount": "4,18,21,781.67" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 416, "Sum Of Amount": "2,84,93,171.42" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 417, "Sum Of Amount": "4,56,443.36" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 418, "Sum Of Amount": "59,69,020.66" }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Un-Reconcilied By Age(Days)": 419, "Sum Of Amount": "1,62,67,594.22" }];
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
        "0-30": {"totalAmount": "62,86,578.10", "totalPercentage": "100%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "28,31,893.05", "Percentage": 45.05 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "24,48,793.20", "Percentage": 38.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "4,48,574.50", "Percentage": 7.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "3,21,165.60", "Percentage": 5.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,25,487.15", "Percentage": 2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "52,474.65", "Percentage": 0.83 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "45,487.95", "Percentage": 0.72 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "12,154.50", "Percentage": 0.19 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": 547.5, "Percentage": 0.01 } ]},
        "31-60": {"totalAmount": "3,25,25,180.95", "totalPercentage": "33.33%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "1,18,31,893.05", "Percentage": 12.13 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "83,56,593.65", "Percentage": 8.56 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "21,65,987.65", "Percentage": 2.22 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "19,88,956.40", "Percentage": 2.04 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "18,98,657.60", "Percentage": 1.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "17,98,654.00", "Percentage": 1.84 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "16,98,796.59", "Percentage": 1.74 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "13,65,698.56", "Percentage": 1.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "12,36,587.98", "Percentage": 1.27 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "1,83,355.47", "Percentage": 0.19 } ]},
        "61-90": {"totalAmount": "1,19,14,392.60", "totalPercentage": "81.55%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "46,31,893.05", "Percentage": 31.7 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "39,56,593.65", "Percentage": 27.08 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "9,95,987.65", "Percentage": 6.82 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "7,88,956.40", "Percentage": 5.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "6,98,657.60", "Percentage": 4.78 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "5,65,865.65", "Percentage": 3.87 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "99,796.59", "Percentage": 0.68 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "76,698.56", "Percentage": 0.52 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "56,587.98", "Percentage": 0.39 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "43,355.47", "Percentage": 0.3 } ]},
        ">90": {"totalAmount": "44,12,14,392.60", "totalPercentage": "40.07%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "19,78,31,893.05", "Percentage": 17.97 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "18,39,56,593.65", "Percentage": 16.71 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "1,49,95,987.65", "Percentage": 1.36 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "1,37,88,956.40", "Percentage": 1.25 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,36,98,657.60", "Percentage": 1.24 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "1,25,65,865.65", "Percentage": 1.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "11,99,796.59", "Percentage": 0.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "10,76,698.56", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "10,56,587.98", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "10,43,355.47", "Percentage": 0.09 } ]}
    };

    unAccountedAgingWiseData: any = {
        "0-30": {"totalAmount": "62,86,578.10", "totalPercentage": "100%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "28,31,893.05", "Percentage": 45.05 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "24,48,793.20", "Percentage": 38.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "4,48,574.50", "Percentage": 7.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "3,21,165.60", "Percentage": 5.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,25,487.15", "Percentage": 2 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "52,474.65", "Percentage": 0.83 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "45,487.95", "Percentage": 0.72 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "12,154.50", "Percentage": 0.19 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": 547.5, "Percentage": 0.01 } ]},
        "31-60": {"totalAmount": "3,25,25,180.95", "totalPercentage": "33.33%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "1,18,31,893.05", "Percentage": 12.13 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "83,56,593.65", "Percentage": 8.56 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "21,65,987.65", "Percentage": 2.22 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "19,88,956.40", "Percentage": 2.04 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "18,98,657.60", "Percentage": 1.95 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "17,98,654.00", "Percentage": 1.84 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "16,98,796.59", "Percentage": 1.74 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "13,65,698.56", "Percentage": 1.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "12,36,587.98", "Percentage": 1.27 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "1,83,355.47", "Percentage": 0.19 } ]},
        "61-90": {"totalAmount": "1,19,14,392.60", "totalPercentage": "81.55%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "46,31,893.05", "Percentage": 31.7 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "39,56,593.65", "Percentage": 27.08 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "9,95,987.65", "Percentage": 6.82 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "7,88,956.40", "Percentage": 5.4 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "6,98,657.60", "Percentage": 4.78 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "5,65,865.65", "Percentage": 3.87 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "99,796.59", "Percentage": 0.68 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "76,698.56", "Percentage": 0.52 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "56,587.98", "Percentage": 0.39 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "43,355.47", "Percentage": 0.3 } ]},
        ">90": {"totalAmount": "44,12,14,392.60", "totalPercentage": "40.07%","data": [ { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "AUD", "Amount": "19,78,31,893.05", "Percentage": 17.97 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "BRL", "Amount": "18,39,56,593.65", "Percentage": 16.71 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CAD", "Amount": "1,49,95,987.65", "Percentage": 1.36 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CHF", "Amount": "1,37,88,956.40", "Percentage": 1.25 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "CZK", "Amount": "1,36,98,657.60", "Percentage": 1.24 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "DKK", "Amount": "1,25,65,865.65", "Percentage": 1.14 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "EUR", "Amount": "11,99,796.59", "Percentage": 0.11 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "GBP", "Amount": "10,76,698.56", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "HKD", "Amount": "10,56,587.98", "Percentage": 0.1 }, { "Provider Name": "Cayan", "Ledger Name": "NSPL Primary Ledger", "Currency": "INR", "Amount": "10,43,355.47", "Percentage": 0.09 } ]}
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
     public pieChartAccountingColors: {}[] = [{ backgroundColor: ['#34A853', '#2196F3','#ea4335'] }]; 

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
    public pieChartActivityColors: {}[] = [{ backgroundColor: ['#FFBB00','#34A853'] }];
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




    constructor(
        private commonService: CommonService,
        private config: NgbDatepickerConfig,
        private $sessionStorage: SessionStorageService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        $(window).scroll(function () {
            /* console.log('selectedModuleType ' + this.selectedModuleType); */
            let offset = $("#reconciliationDiv").offset();
            let posRecon = offset.top - $(window).scrollTop();
            let offsetAcc = $("#accountingDiv").offset();
            let posAcc = offset.top - $(window).scrollTop();
            /* console.log('posRecon '+ posRecon);
            console.log('posAcc position '+ posAcc); */
            if (posAcc > -583) {
                /* console.log('selectedModuleType ' + this.selectedModuleType); */
                this.selectedModuleType = 0;
                $('#md-tab-label-0-1').removeClass('mat-tab-label-active');
                $('#md-tab-label-0-1').attr("tabindex", "-1");
                $('#md-tab-label-0-1').attr("aria-selected", "false");
                $('#md-tab-label-0-0').addClass('mat-tab-label-active');
                $('#md-tab-label-0-0').attr("tabindex", "0");
                $('#md-tab-label-0-0').attr("aria-selected", "true");
                $('.mainNav .mat-ink-bar').css('left', '0px');
                /* console.log('selectedModuleType ' + this.selectedModuleType); */
                /* console.log('this.selectedModuleType ' + this.selectedModuleType); */
            } else {
                /* console.log('selectedModuleType ' + this.selectedModuleType); */
                this.selectedModuleType = 1;
                $('#md-tab-label-0-0').removeClass('mat-tab-label-active');
                $('#md-tab-label-0-0').attr("tabindex", "-1");
                $('#md-tab-label-0-0').attr("aria-selected", "false");
                $('#md-tab-label-0-1').addClass('mat-tab-label-active');
                $('#md-tab-label-0-1').attr("tabindex", "0");
                $('#md-tab-label-0-1').attr("aria-selected", "true");
                $('.mainNav .mat-ink-bar').css('left', '160px');
                /* console.log('selectedModuleType ' + this.selectedModuleType); */
                /* console.log('this.selectedModuleType ' + this.selectedModuleType);  */
            }
            /* console.log('selectedModuleType ' + this.selectedModuleType); */
        });
    }

    changeMinDate() {
        this.config.minDate = this.processFilter.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    selectedProcess: any;
    showRecon: boolean = false;
    testshow:boolean = false;
    ngOnInit() {
        $(".dashboardV5").css('min-height', (this.commonService.screensize().height - 161) + 'px');
        if (this.$sessionStorage.retrieve('userData')) {
            this.userDetails = this.$sessionStorage.retrieve('userData');
        }
        /* if(this.testLabels.length>0){
            this.testshow = true;
        } */
        this.processesList = [{"id":1,"processName":"Test Process"},{"id":2,"processName":"Test Process 2"},{"id":3,"processName":"Test Process 3"},{"id":4,"processName":"Test Process 4"},{"id":5,"processName":"Test Process 5"},{"id":6,"processName":"Test Process 6"},{"id":7,"processName":"Test Process 7"},{"id":8,"processName":"Test Process 8"}];
        if(this.processesList.length>0){
            this.selectedProcess = this.processesList[0].id;
            this.activeItem = this.processesList[0];
            this.selectedProcessName = this.processesList[0].processName;
            console.log('processesList ' + JSON.stringify(this.processesList));
        }
        /* this.navbarService.getprocessessList(this.userDetails).subscribe((res: any) => {
            this.processesList = res;
            this.selectedProcess = this.processesList[0].id;
            this.activeItem = this.processesList[0];
            this.selectedProcessName = this.processesList[0].processName;
            console.log('processesList ' + JSON.stringify(this.processesList));
        }); */
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
    }
    activeItem: any;
    selectedProcessName: any;
    selectedProcessFun(col) {
        console.log('selected process ::' + JSON.stringify(col));
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
        } else if (this.openModuleSetting == 'Un-Reconcilied Transaction By Aging Bucket') {
            this.tempagingColor = this.agingColor;
            this.tempbarChartType = this.barChartType;
            this.agingBucketBackgroundTemp = this.agingBucketBackground;
            $(".agingBackgroundColor").css("background-color", this.agingBucketBackgroundTemp);
            this.unReconAgingBucketColors = [{ backgroundColor: [this.tempagingColor, this.tempagingColor, this.tempagingColor, this.tempagingColor] }];
        } else if (this.openModuleSetting == 'Un-Reconcilied Items By Value') {
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
        } else if (this.openModuleSetting == 'Un-Reconcilied Transaction By Aging Bucket') {
            this.agingColor = this.tempagingColor;
            this.barChartType = this.tempbarChartType;
            this.agingBucketBackground = this.agingBucketBackgroundTemp;
            $(".agingBackgroundColor").css("background-color", this.agingBucketBackground);
            this.unReconAgingBucketColors = [{ backgroundColor: [this.agingColor, this.agingColor, this.agingColor, this.agingColor] }];
        } else if (this.openModuleSetting == 'Un-Reconcilied Items By Value') {
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

    fetchReconciliationStatus(e){
        //var chartElement = e.active[0]._chart.getElementAtEvent(event);
        
        /* console.log('selected region ::' + e.active[0]._chart.config.data.labels[e.active[0]._index]); */
        if(e.active.length>0){
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if(labelName == 'Reconciled'){
                this.selectedTab = 1;
            }else if(labelName == 'Un-Reconciled'){
                this.selectedTab = 0;
            }
        } 
    }
    unReconAgingBucketDataSelected:any;
    showAgingFilterData:boolean = false;
    selectedRegion:any;
    fetchUnReconciliationAging(e){
        /* var chartElement = e.active[0]._chart.getElementAtEvent(event); */
        if(e.active.length>0){
            this.showAgingFilterData = true;
            this.selectedRegion = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if(this.selectedRegion != undefined){
                this.unReconAgingBucketDataSelected = this.unAccountedAgingWiseData[this.selectedRegion];
            }
            
        }else{
            this.showAgingFilterData = false;
        }
    }

    fetchAccountingStatus(e){
        if(e.active.length>0){
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if(labelName == 'Accounted'){
                this.selectedAccTab = 2;
            }else if(labelName == 'In Process'){
                this.selectedAccTab = 1;
            }else if(labelName == 'Un-Accounted'){
                this.selectedAccTab = 0;
            }
        } 
    }

    fetchAccountingActivity(e){
        if(e.active.length>0){
            let labelName = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if(labelName == 'Accounted For Un-Identified'){
                this.selectedAccTabActivity = 0;
            }else if(labelName == 'Accounted For Identified'){
                this.selectedAccTabActivity = 1;
            }
        } 
    }

    unAccountedAgingBucketDataSelected:any;
    showAgingFilterDataUnAcc:boolean = false;
    selectedUnAccRegion:any;
    fetchAccountingAging(e){
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
        if(e.active.length>0){
            this.selectedUnAccRegion = e.active[0]._chart.config.data.labels[e.active[0]._index];
            if(this.selectedUnAccRegion != undefined){
                this.unAccountedAgingBucketDataSelected = this.unAccountedAgingBucketData[this.selectedUnAccRegion];
                this.showAgingFilterDataUnAcc = true;
            }
            
        }else{
            this.showAgingFilterDataUnAcc = false;
        }
    }


}