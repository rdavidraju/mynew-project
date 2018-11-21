import { Component, OnInit, ViewChild } from '@angular/core';
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
import { UserService } from '../shared/user/user.service'
import { NavBarService } from '../layouts/navbar/navbar.service';
import { SessionStorageService } from 'ng2-webstorage';
import { NotificationsService } from 'angular2-notifications-lite';
//import 'chartjs-plugin-zoom';
declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: [
        'home.scss'
    ],
    animations: [
        trigger('flyInOut', [
            state('in', style({ transform: 'translateX(0)' })),
            transition('void => *', [
                style({ transform: 'translateX(100%)' }),
                animate(1000)
            ])
        ])
    ]

})
export class DashboardComponent implements OnInit {
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
    tableView1: boolean = false;
    visibleSidebar2;
    dashBoardData: any;
    notificationList: any = [];
    moduleType: any;
    dayType: any;
    processId:any;
    ccReconProcess: any = [
        {
            "Date": "26-Dec",
            "Extraction": 80,
            "Transformation": 75,
            "Reconciliation": 40,
            "Accounting": 50,
            "Journals": 50,
            "Total": 59
        },
        {
            "Date": "25-Dec",
            "Extraction": 100,
            "Transformation": 90,
            "Reconciliation": 80,
            "Accounting": 100,
            "Journals": 83,
            "Total": 91
        },
        {
            "Date": "24-Dec",
            "Extraction": 100,
            "Transformation": 100,
            "Reconciliation": 80,
            "Accounting": 55,
            "Journals": 90,
            "Total": 85
        },
        {
            "Date": "23-Dec",
            "Extraction": 100,
            "Transformation": 100,
            "Reconciliation": 100,
            "Accounting": 100,
            "Journals": 63,
            "Total": 93
        },
        {
            "Date": "24-Dec",
            "Extraction": 100,
            "Transformation": 100,
            "Reconciliation": 100,
            "Accounting": 100,
            "Journals": 100,
            "Total": 100
        }
    ];
    /* Sample Data For Reconciliation */
    reconciliedData: any = [
        /*  {
            "Recon Process": "Rule Group 1",
            "View": "Dv1",
            "Recon": 45.67,
            "UnRecon": 23.41
        },
        {
            "Recon Process": "Rule Group 1",
            "View": "Dv2",
            "Recon": 30.19,
            "UnRecon": 7.29
        },
        {
            "Recon Process": "Rule Group 2",
            "View": "Dv3",
            "Recon": 44.83,
            "UnRecon": 5.78
        },
        {
            "Recon Process": "Rule Group 2",
            "View": "Dv4",
            "Recon": 49.78,
            "UnRecon": 15.63
        }  */
    ];

    /* Sample Data For Accounting */
    /* accountingData: any = [
        {
            "Accounting Process": "Rule Group 1",
            "View": "Dv1",
            "Accounted": 37.54,
            "UnAccounted": 15.74
        },
        {
            "Accounting Process": "Rule Group 1",
            "View": "Dv2",
            "Accounted": 65.42,
            "UnAccounted": 17.29
        },
        {
            "Accounting Process": "Rule Group 2",
            "View": "Dv3",
            "Accounted": 31.83,
            "UnAccounted": 7.31
        },
        {
            "Accounting Process": "Rule Group 2",
            "View": "Dv4",
            "Accounted": 48.61,
            "UnAccounted": 7.42
        }
    ]; */

    /* agingDataReconcile: any =
    [
        {
            "type": "Reconciled",
            "viewType": "Dv1",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }, {
            "type": "Reconciled",
            "viewType": "Dv2",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 352
                },
                {
                    "Age": "1-2",
                    "Count": 689
                },
                {
                    "Age": "2-3",
                    "Count": 753
                },
                {
                    "Age": "3-4",
                    "Count": 1128
                },
                {
                    "Age": "4-5",
                    "Count": 97
                }
            ]
        }, {
            "type": "Reconciled",
            "viewType": "Dv3",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }, {
            "type": "Reconciled",
            "viewType": "Dv4",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }, {
            "type": "Un Reconciled",
            "viewType": "Dv1",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }, {
            "type": "Un Reconciled",
            "viewType": "Dv2",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }, {
            "type": "Un Reconciled",
            "viewType": "Dv3",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }, {
            "type": "Un Reconciled",
            "viewType": "Dv4",
            "data": [
                {
                    "Age": "0-1",
                    "Count": 1352
                },
                {
                    "Age": "1-2",
                    "Count": 789
                },
                {
                    "Age": "2-3",
                    "Count": 853
                },
                {
                    "Age": "3-4",
                    "Count": 1328
                },
                {
                    "Age": "4-5",
                    "Count": 245
                }
            ]
        }
    ]; */
    public barChartOptionsAging: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        legend: {
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Aging with accounting and approved analysis'
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItem, data) {
                    var dslabels = data.labels[tooltipItem.index];
                    var dataset = data.datasets[tooltipItem.datasetIndex];
                    var dslabelamt = dataset.data[tooltipItem.index];
                    return data.datasets[tooltipItem.datasetIndex].label + ': ' + dslabelamt;/* dslabels + '-' +  */
                }
            }
        }
        /* ,
        scales: { xAxes: [{ stacked: true }], yAxes: [{ stacked: true }] } */
    };
    public barChartLabelsAging: string[]/*  = [] */;
    public barChartTypeAging: string = 'line';
    public barChartLegendAging: boolean = true;

    public barChartDataAging: any[]/*  = [
        { data: [65], label: 'Reconciled' },
        { data: [65], label: 'Accounted' },
        { data: [28], label: 'Approved' }
    ] */;
    /* agingDataAccounting: any = [
        {
            "viewType": "Dv1",
            "data": [
                {
                    "Age": "0-1",
                    "Recon": 1352,
                    "Accounted": 341,
                    "Approved": 890
                },
                {
                    "Age": "1-2",
                    "Recon": 789,
                    "Accounted": 345,
                    "Approved": 271
                },
                {
                    "Age": "2-3",
                    "Recon": 853,
                    "Accounted": 321,
                    "Approved": 456
                },
                {
                    "Age": "3-4",
                    "Recon": 1328,
                    "Accounted": 890,
                    "Approved": 980
                },
                {
                    "Age": "4-5",
                    "Recon": 245,
                    "Accounted": 134,
                    "Approved": 45
                }
            ]
        },
        {
            "viewType": "Dv2",
            "data": [
                {
                    "Age": "0-1",
                    "Recon": 952,
                    "Accounted": 541,
                    "Approved": 420
                },
                {
                    "Age": "1-2",
                    "Recon": 1009,
                    "Accounted": 471,
                    "Approved": 645
                },
                {
                    "Age": "2-3",
                    "Recon": 653,
                    "Accounted": 221,
                    "Approved": 406
                },
                {
                    "Age": "3-4",
                    "Recon": 1128,
                    "Accounted": 790,
                    "Approved": 380
                },
                {
                    "Age": "4-5",
                    "Recon": 345,
                    "Accounted": 114,
                    "Approved": 145
                }
            ]
        }
    ]; */
    /* Extraction */
    public barChartOptionsExtraction: any = {
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
            text: 'Extraction'
        }
    };
    public barChartLabelsExtraction: string[]/*  = ['Profiles'] */;
    public barChartTypeExtraction: string = 'bar';
    public barChartLegendExtraction: boolean = true;

    public barChartDataExtraction: any[]/*  = [
        { data: [65], label: 'BoA Profile'},
        { data: [28], label: 'Chase Profile' }
    ] */;

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
    public barChartTypeTransformation: string = 'bar';
    public barChartLegendTransformation: boolean = true;
    public barTransformationChartColors: Array<any> = [
        {
            backgroundColor: 'rgb(134, 199, 243)'
        }
    ]
    public barChartDataTransformation: any[]/*   = [
        { data: [65], label: 'BoA Profile' },
        { data: [28], label: 'Chase Profile' } 
    ] */;

    testObj: any;
    /* Reconciliation */
    public barChartOptionsReconciliation: any = {
        scaleShowVerticalLines: false,
        responsive: true,
        scales: {
            xAxes: [
                {
                    stacked: true,
                     display:false, 
                    ticks: {
                        fontSize: 10,
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
                        display: true/*  ,
                        labelString: "Percentage"  */
                    }
                }
            ]
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data, totalCount) {
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[tooltipItems.datasetIndex].totalCount[tooltipItems.datasetIndex] + '(' + tooltipItems.yLabel + '%)';
                }
            }
        },
        legend: {
            position: 'bottom'/* ,
            labels: {
                fontColor: 'rgb(255, 99, 132)'
            } */
        },
        title: {
            display: true,
            text: 'Reconciliation'
        }
    };
    public barChartLabelsReconciliation: string[];/*  = ['2006', '2007', '2008', '2009']; */
    public barChartTypeReconciliation: string = 'bar';
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
                    display:false,
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
                        display: true /* ,
                        labelString: "Percentage"  */
                    }
                }
            ]
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItems, data, totalCount) {
                    /* return data.datasets[tooltipItems.datasetIndex].label +': ' + tooltipItems.yLabel +' %'; */
                    return data.datasets[tooltipItems.datasetIndex].label + ': ' + data.datasets[tooltipItems.datasetIndex].totalCount[tooltipItems.datasetIndex] + '(' + tooltipItems.yLabel + '%)';
                }
            }
        },
        legend: {
            position: 'bottom'
        },
        title: {
            display: true,
            text: 'Accounting'
        }/* ,
        scales: { xAxes: [{ stacked: true }], yAxes: [{ stacked: true }] } */
    };
    public barChartLabelsAccounting: string[]/*  = ['2006'] */;
    public barChartTypeAccounting: string = 'bar';
    public barChartLegendAccounting: boolean = true;

    public barChartDataAccounting: any[]/*  = [
        { data: [65], label: 'BoA Profile' },
        { data: [28], label: 'Chase Profile' }
    ] */;
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

    public pieChartLabels: string[]/*  = ['Extraction', 'Transformation', 'Reconciliation', 'Accounting', 'Journals'] */;
    public pieChartData: number[]/*  = [300, 500, 100, 250, 200] */;
    public pieChartType: string = 'pie';
    public pieChartOptions: any = {
        responsive: true,
        legend: {
            position: 'bottom'/*  ,
            labels: {
                fontSize: 12
            }  */
        },
        title: {
            display: true,
            text: 'Aging wise analysis'
        }
    };
    public pieChartLabels1: string[] = ['Profile', 'CC-Profile'];
    public pieChartData1: number[] = [300, 500];
    public pieChartType1: string = 'pie';

    /* Extraction */
    public pieChartLabelsExtraction: string[] = ['CM Reco', 'CM Reco'];
    public pieChartDataExtraction: number[] = [55, 45];
    public pieChartTypeExtraction: string = 'pie';
    /* Transformation */
    public pieChartLabelsTransformation: string[] = ['CM Reco1', 'CM Reco2', 'CM Reco3', 'CM Reco4', 'CM Reco5'];
    public pieChartDataTransformation: number[] = [65, 8, 24, 5, 3];
    public pieChartTypeTransformation: string = 'pie';
    /* Reconciliation */
    public pieChartLabelsReconciliation: string[] = ['CM Reco', 'CM Reco'];
    public pieChartDataReconciliation: number[] = [75, 25];
    public pieChartTypeReconciliation: string = 'pie';
    /* Accounting */
    public pieChartLabelsAccounting: string[] = ['Accounted', 'Un Accounted'];
    public pieChartDataAccounting: number[] = [83, 17];
    public pieChartTypeAccounting: string = 'pie';
    /* Journals */
    public pieChartLabelsJournals: string[] = ['Journal1', 'Journal2', 'Journal3', 'Journal4', 'Journal5'];
    public pieChartDataJournals: number[] = [65, 8, 24, 5, 3];
    public pieChartTypeJournals: string = 'pie';

    /* notificationList: any = [
        {
            "actionType": "SCHEDULER",
            "actionValue": "0",
            "module": "RECON",
            "id": 1,
            "time": "23 days ago",
            "message": "Reconciled 3435 records for SR Rule1 with views SR_SRC_DV_28Nov_10, SR_TGT_DV_28Nov_10",
            "creationDate": "2017-11-28T16:47:13+05:30",
            "isViewed": true
        },
        {
            "actionType": "SCHEDULER",
            "actionValue": "0",
            "module": "RECON",
            "id": 4,
            "time": "23 days ago",
            "message": "Reconciled 3435 records for SR Rule1 with views SR_SRC_DV_28Nov_10, SR_TGT_DV_28Nov_10",
            "creationDate": "2017-11-28T19:03:59+05:30",
            "isViewed": true
        },
        {
            "actionType": "SCHEDULER",
            "actionValue": "23",
            "module": "RECON",
            "id": 6,
            "time": "23 days ago",
            "message": "Reconciled 3435 records for SR Rule1 with views SR_SRC_DV_28Nov_10, SR_TGT_DV_28Nov_10",
            "creationDate": "2017-11-29T08:57:59+05:30",
            "isViewed": true
        },
        {
            "actionType": "SCHEDULER",
            "actionValue": "39",
            "module": "ACCOUNTING",
            "id": 10,
            "time": "23 days ago",
            "message": "For view sr_src_dv_28nov_10, line type DEBIT Accounted :  1126 Partially Accounted :  0 Un Accounted :  0",
            "creationDate": "2017-11-29T12:13:54+05:30",
            "isViewed": true
        },
        {
            "actionType": "SCHEDULER",
            "actionValue": "41",
            "module": "JOURNALS",
            "id": 11,
            "time": "23 days ago",
            "message": "0 Journal Headers Created",
            "creationDate": "2017-11-29T13:10:03+05:30",
            "isViewed": true
        },
        {
            "actionType": "SCHEDULER",
            "actionValue": "45",
            "module": "ACCOUNTING",
            "id": 12,
            "time": "22 days ago",
            "message": "For view sr_src_dv_28nov_10, line type DEBIT Accounted :  125 Partially Accounted :  0 Un Accounted :  0",
            "creationDate": "2017-11-29T16:59:35+05:30",
            "isViewed": true
        },
        {
            "actionType": "SCHEDULER",
            "actionValue": "45",
            "module": "ACCOUNTING",
            "id": 13,
            "time": "22 days ago",
            "message": "For view sr_src_dv_28nov_10, line type CREDIT Accounted :  125 Partially Accounted :  0 Un Accounted :  0",
            "creationDate": "2017-11-29T18:08:57+05:30",
            "isViewed": true
        }
    ]; */
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
        this.processId = 7;
        this.displayETRA();
        this.moduleType = "All";
        this.dayType = "7Days"
        this.getNotificationDetails();
        $('body').removeClass('login');
        $(document).ready(function () {
            /* $('ul li a').click(function(){
                $('li a').removeClass("active");
                $(this).addClass("active");
            }); */
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
        //console.log('height: '+this.commonService.screensize().height);
        // $(".page-content .home-main .row").css('height',(this.commonService.screensize().height - 161)+'px');
        this.principal.identity().then((account) => {
            this.account = account;
            //UserData =this.account;
        });
        this.registerAuthenticationSuccess();
        this.tableView = true;
        /*   let objRec = [];
        let objUnRec = [];
        let objLabel = [];
        this.reconciliedData.forEach(element => {
            objRec.push(element.reconciledPercent);
            objUnRec.push(element.unReconciledPercent);
            objLabel.push(element.dataViewName);
        });
        this.barChartDataReconciliation = [
            { data: objRec, label: 'Reconciled' },
            { data: objUnRec, label: 'Un Reconciled' }
        ];
        this.barChartLabelsReconciliation = objLabel;   */

        /* let objAcc = [];
        let objUnAcc = [];
        let objAccLabel = [];
        this.accountingData.forEach(element => {
            objAcc.push(element.Accounted);
            objUnAcc.push(element.UnAccounted);
            objAccLabel.push(element.View);
        });
        this.barChartDataAccounting = [
            { data: objAcc, label: 'Accounting' },
            { data: objUnAcc, label: 'Un Accounting' }
        ];
        this.barChartLabelsAccounting = objAccLabel; */

        this.selectedTab = 0;
        this.extractionValue = this.ccReconProcess[this.selectedTab].Extraction;
        this.transformationValue = this.ccReconProcess[this.selectedTab].Transformation;
        this.reconciliationValue = this.ccReconProcess[this.selectedTab].Reconciliation;
        this.accountingValue = this.ccReconProcess[this.selectedTab].Accounting;
        this.journalsValue = this.ccReconProcess[this.selectedTab].Journals;

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
    userDetails: any;
    displayETRA() {
        if (this.$sessionStorage.retrieve('userData')) {
            this.userDetails = this.$sessionStorage.retrieve('userData');
        }
        if (this.userDetails) {
            let dateObj = {
                "startDate": "2018-01-01T15:58:18+05:30",
                "endDate": "2018-01-19T15:58:18+05:30"
            }
            this.navbarService.fetchCountsForDashBoard(this.processId, dateObj).subscribe((res: any) => {
                this.dashBoardData = res;
                //  this.reconciliedData = this.dashBoardData.reconciliation;
                console.log('this.dashBoardData ' + JSON.stringify(this.dashBoardData));
                /* Extraction */
                let objExt = [];
                let objExtLabel = [];
                res.dataExtraction.forEach(element => {
                    objExt.push(element.extractionCount);
                    objExtLabel.push(element.sourceProfileName);
                });
                this.barChartDataExtraction = [
                    { data: objExt, label: objExtLabel }
                ];
                this.barChartLabelsExtraction = objExtLabel;
                /* Transformation */
                let objTrans = [];
                let objTransLabel = [];
                res.dataTransformation.forEach(element => {
                    objTrans.push(element.transformationCount);
                    objTransLabel.push(element.sourceProfileName);
                });
                this.barChartDataTransformation = [
                    { data: objTrans, label: objTransLabel }
                ];
                this.barChartLabelsTransformation = objTransLabel;
                /* Reconciliation */
                let objRec = [];
                let objTotRec = [];
                let objUnRec = [];
                let objTotUnRec = [];
                let objLabel = [];
                let objDvId = [];
                res.reconciliation.forEach(element => {
                    objRec.push(element.reconciledPercent);
                    objTotRec.push(element.reconciledCount);
                    objUnRec.push(element.unReconciledPercent);
                    objTotRec.push(element.unReconciledCount);
                    objLabel.push(element.dataViewName);
                    objDvId.push(element.dvId);
                });
                this.barChartDataReconciliation = [
                    { data: objRec, label: 'Reconciled', totalCount: objTotRec, dvId: objDvId },
                    { data: objUnRec, label: 'Not Reconciled', totalCount: objTotRec, dvId: objDvId }
                ];
                this.barChartLabelsReconciliation = objLabel;
                console.log('this.barChartDataReconciliation ' + JSON.stringify(this.barChartDataReconciliation));
                console.log('this.barChartLabelsReconciliation ' + JSON.stringify(this.barChartLabelsReconciliation));
                /* Accounting */
                let objAcc = [];
                let objUnAcc = [];
                let objAccLabel = [];
                let objTotAcc = [];
                res.accounting.forEach(element => {
                    objAcc.push(element.acountedPercent);
                    objTotAcc.push(element.AccountedCount);
                    objUnAcc.push(element.unAccountedPercent);
                    objTotAcc.push(element.unAccountedCount);
                    objAccLabel.push(element.dataViewName);
                });
                this.barChartDataAccounting = [
                    { data: objAcc, label: 'Accounted', totalCount: objTotAcc },
                    { data: objUnAcc, label: 'Not Accounted', totalCount: objTotAcc }
                ];
                this.barChartLabelsAccounting = objAccLabel;
            });
        }
    }

    getNotificationDetails() {
        console.log('this.userDetails ' + JSON.stringify(this.userDetails));
        console.log('moduleType ' + this.moduleType + ' dayType ' + this.dayType);
        if (this.userDetails) {
            this.navbarService.fetchNotificationDetailsForDashBoard( this.moduleType, this.dayType).subscribe((res: any) => {
                if(this.moduleType == 'All'){
                    $('.allNoti').addClass("allNotiactive");
                    $('.allNoti').click(function () {
                        $('.reconNoti').removeClass("reconNotiactive");
                        $('.accNoti').removeClass("accNotiactive");
                        $('.jourNoti').removeClass("jourNotiactive");
                        $(this).addClass("allNotiactive");
                    });
                }else if(this.moduleType == 'RECON'){
                    $('.reconNoti').click(function () {
                        $('.allNoti').removeClass("allNotiactive");
                        $('.accNoti').removeClass("accNotiactive");
                        $('.jourNoti').removeClass("jourNotiactive");
                        $(this).addClass("reconNotiactive");
                    });
                }else if(this.moduleType == 'ACCOUNTING'){
                    $('.accNoti').click(function () {
                        $('.allNoti').removeClass("allNotiactive");
                        $('.reconNoti').removeClass("reconNotiactive");
                        $('.jourNoti').removeClass("jourNotiactive");
                        $('.accNoti').removeClass("accNoti:active");
                        $(this).addClass("accNotiactive");
                    });
                }else if(this.moduleType == 'JOURNAL_ENTRY'){
                    $('.jourNoti').click(function () {
                        $('.allNoti').removeClass("allNotiactive");
                        $('.reconNoti').removeClass("reconNotiactive");
                        $('.accNoti').removeClass("accNotiactive");
                        $('.jourNoti').removeClass("jourNoti:active");
                        $(this).addClass("jourNotiactive");
                    });
                }
                this.notificationList = res;
               // console.log('notificationList ' + JSON.stringify(this.notificationList));

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
        console.log('this.userDetails ' + JSON.stringify(this.userDetails));
        if (e.active.length > 0) {
            var chartElement = e.active[0]._chart.getElementAtEvent(event);
            //console.log('chartElement  ' + e.active[0].getElementAtEvent(event));
            console.log(chartElement[0]._model.datasetLabel);
            console.log(chartElement[0]._model.label);
            this.dashBoardData.reconciliation.forEach(element => {
                if (element.dataViewName == chartElement[0]._model.label) {
                    dvId = element.dvId;
                }
            });
            let obj = {
                "status": chartElement[0]._model.datasetLabel,
                "dataviewName": chartElement[0]._model.label,
                "dvId": dvId,
                "tenantId": this.userDetails.tenantId,
                "startDate": "2018-01-01T15:58:18+05:30",
                "endDate": "2018-01-19T15:58:18+05:30"
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
            /* this.agingDataReconcile.forEach(element => {
                                if (element.type == chartElement[0]._model.datasetLabel && element.viewType == chartElement[0]._model.label) {
                                    element.data.forEach(element1 => {
                                        console.log('element1 ' + JSON.stringify(element1));
                                        obj1.push(element1.Age);
                                        obj2.push(element1.Count);
                                    });
                                }
                            });
                            this.pieChartLabels = obj1;
                            this.pieChartData = obj2;
                            console.log('this.pieChartData ' + JSON.stringify(this.pieChartData));
                            console.log('this.pieChartLabels ' + JSON.stringify(this.pieChartLabels)); */
            /* this.barChartLabelsAging = [];
            this.barChartDataAging = [];
            let objRec = [];
            let objAcc = [];
            let objApp = [];
            let objLab = [];
             console.log('this.agingDataAccounting ' + JSON.stringify(this.agingDataAccounting));
            this.agingDataAccounting.forEach(element => {
                if(element.viewType == chartElement[0]._model.label){
                    element.data.forEach(element1 => {
                        objRec.push(element1.Recon);
                        objAcc.push(element1.Accounted);
                        objApp.push(element1.Approved);
                        objLab.push(element1.Age);
                    });
                }
                
            });
            this.barChartDataAging = [
                { data: objRec, label: 'Reconciled' },
                { data: objAcc, label: 'Accounted' },
                { data: objApp, label: 'Approved' }
            ];
            this.barChartLabelsAging = objLab;  */
        }
    }

    loadContent(e) {
        console.log(e);
        this.selectedTab = e.index;
        this.extractionValue = this.ccReconProcess[this.selectedTab].Extraction;
        this.transformationValue = this.ccReconProcess[this.selectedTab].Transformation;
        this.reconciliationValue = this.ccReconProcess[this.selectedTab].Reconciliation;
        this.accountingValue = this.ccReconProcess[this.selectedTab].Accounting;
        this.journalsValue = this.ccReconProcess[this.selectedTab].Journals;
        /* for
        this.ccReconProcess.forEach(element => {
            
        }); */
    }

    changeReportView() {
        this.tableView1 = this.tableView1 ? false : true;
    }

    showFilterData() {
        this.visibleSidebar2 = this.visibleSidebar2 ? false : true;
    }
}
//export var UserData: any = [];