import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { NotificationBatch } from './notification-batch.model';
import { NotificationBatchService } from './notification-batch.service';
import { AccountingDataService } from '../accounting-data/accounting-data.service';
import { ReconcileService } from '../reconcile/reconcile.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, DialogModule, AccordionModule, CheckboxModule, TreeModule, TreeNode } from 'primeng/primeng';
import { SidebarModules } from '../../shared/primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CommonService } from '../../entities/common.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
import { PageEvent } from '@angular/material';
import { AwqAllParams, GroupingFilterParams } from '../accounting-data/accounting-data.model';
import { DatePipe, DecimalPipe, CurrencyPipe } from '@angular/common';

declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-notification-batch',
    templateUrl: './notification-batch.component.html'
})
export class NotificationBatchComponent implements OnInit, OnDestroy {
    sales: any = [{ "brand": "Apple", "lastYearSale": "51%", "thisYearSale": "40%", "lastYearProfit": "$54,406.00", "thisYearProfit": "$43,342" }, { "brand": "Samsung", "lastYearSale": "83%", "thisYearSale": "96%", "lastYearProfit": "$423,132", "thisYearProfit": "$312,122" }, { "brand": "Microsoft", "lastYearSale": "38%", "thisYearSale": "5%", "lastYearProfit": "$12,321", "thisYearProfit": "$8,500" }, { "brand": "Philips", "lastYearSale": "49%", "thisYearSale": "22%", "lastYearProfit": "$745,232", "thisYearProfit": "$650,323," }, { "brand": "Song", "lastYearSale": "17%", "thisYearSale": "79%", "lastYearProfit": "$643,242", "thisYearProfit": "500,332" }, { "brand": "LG", "lastYearSale": "52%", "thisYearSale": " 65%", "lastYearProfit": "$421,132", "thisYearProfit": "$150,005" }, { "brand": "Sharp", "lastYearSale": "82%", "thisYearSale": "12%", "lastYearProfit": "$131,211", "thisYearProfit": "$100,214" }, { "brand": "Panasonic", "lastYearSale": "44%", "thisYearSale": "45%", "lastYearProfit": "$66,442", "thisYearProfit": "$53,322" }, { "brand": "HTC", "lastYearSale": "90%", "thisYearSale": "56%", "lastYearProfit": "$765,442", "thisYearProfit": "$296,232" }, { "brand": "Toshiba", "lastYearSale": "75%", "thisYearSale": "54%", "lastYearProfit": "$21,212", "thisYearProfit": "$12,533" }];

    tempReconAppCol = { "trgnotificationBatchTableColumns": [{ "field": "Provider_47", "width": "150px", "header": "Provider", "align": "left" }, { "field": "Currency_47", "width": "150px", "header": "Currency", "align": "left" }, { "field": "Amt_47", "width": "150px", "header": "Amt", "align": "right" }, { "field": "Date_47", "width": "150px", "header": "Date", "align": "left" }], "trgcolumnOptions": [{ "label": "Provider Name", "value": { "field": "ProviderName_46", "width": "150px", "header": "Provider Name", "align": "left" } }, { "label": "Currency Code", "value": { "field": "CurrencyCode_46", "width": "150px", "header": "Currency Code", "align": "left" } }, { "label": "Amount", "value": { "field": "Amount_46", "width": "150px", "header": "Amount", "align": "right" } }, { "label": "Date", "value": { "field": "Date_46", "width": "150px", "header": "Date", "align": "left" } }], "srccolumnOptions": [{ "label": "Provider Name", "value": { "field": "ProviderName_46", "width": "150px", "header": "Provider Name", "align": "left" } }, { "label": "Currency Code", "value": { "field": "CurrencyCode_46", "width": "150px", "header": "Currency Code", "align": "left" } }, { "label": "Amount", "value": { "field": "Amount_46", "width": "150px", "header": "Amount", "align": "right" } }, { "label": "Date", "value": { "field": "Date_46", "width": "150px", "header": "Date", "align": "left" } }], "srcnotificationBatchTableColumns": [{ "field": "ProviderName_46", "width": "150px", "header": "Provider Name", "align": "left" }, { "field": "CurrencyCode_46", "width": "150px", "header": "Currency Code", "align": "left" }, { "field": "Amount_46", "width": "150px", "header": "Amount", "align": "right" }, { "field": "Date_46", "width": "150px", "header": "Date", "align": "left" }] };
    showFilters = false;
    currentAccount: any;
    notificationBatches: any = [];
    copyNotificationBatches = [];
    groupingSummaryParams = new AwqAllParams();
    filterValues: GroupingFilterParams[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    isViewOnly = false;
    queryCount: any;
    itemsPerPage: any;
    pageSizeOptions = [10, 25, 50, 100];
    batchListRecordsLength: number;
    viewDetailRecordsLength: number;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    currentSearch: string;
    TemplatesHeight: any;
    sideBarHeight: any;
    columnOptions: SelectItem[];
    srccolumnOptions: SelectItem[];
    trgcolumnOptions: SelectItem[];
    selectedRows = [];
    actionButtons = false;
    selectedBatchRows: any = [];
    selectedBatchDetails: any = {};
    display = false;
    visibleSidebar5;
    selectedBatchDetailsReports: any = [];
    selectedValues: any = [];
    listOfSelected: any = [];
    approvalDetails: any = [];
    approvalsHistory: any = [];
    processDetails: any = {};
    fullScreenHeight: any;
    selectedBatchId: any;
    batchRecordsSrcTrgDetails: any = [];
    fetchBatchRecordsSrcTrg: any = [];
    lastindex: any;
    usersList: any = [];
    selectedUser: any;
    displayCheckBox = true;
    sideBarHierarchyList: TreeNode[];
    selectedProcessType: any = 'IN_PROCESS';
    reAssignTo = false;
    reAssignTo1 = false;
    showTabB = false;
    currentStatus: any;
    statusObject: any;
    accountedLedgerList: any = [];
    qualifierColumns: any[] = [];
    selectedModule: any;
    selectedUserId: any;
    selectedFile: any;
    selViewId: any;
    selGrpId: any;
    sourceHeaderColumns1: any;
    filterValues1: GroupingFilterParams[] = [];
    selectedDataLinesToAccount: any[] = [];
    selectedKeys: any[] = [];
    selectedSummary: any[] = [];
    sortOrder = 'desc';
    dataHeaderColumns: any[] = [];
    searchWord: any = "";
    isAccountedSummary = false;
    dataViewLines: any[] = [];
    changeSort = false;
    viewLength: any;
    dataViewInfo: any;
    pushedGroupingValues: any[] = [];
    groupingSummaryFilters: any[] = [];
    groupingSummary: any[] = [];
    dataColumnOptions: SelectItem[];
    accountedHeaders: any[] = [];
    accountedColumnOptions: any[] = [];
    accountedSummaryData: any[] = [];
    selectedLederRef: any;
    tempParam: any;
    summaryLength = 0;
    summaryPageEvent: PageEvent = new PageEvent();
    summaryPageSizeOptions = [10, 25, 50, 100];
    moduleType: any = [
        {
            "name": "All",
            "value": "ALL"
        },
        {
            "name": "Accounting",
            "value": "ACCOUNTING_APPROVALS"
        },
        {
            "name": "Reconciliation",
            "value": "RECON_APPROVALS"
        }
    ];
    selectedModuleType: any = 'ALL';
    notificationBatchTableColumns = [

        /* { field: 'notificationDate', header: 'Notification Date', width: '150px', align: 'left' }, */

        /* { field: 'module', header: 'Module Type', width: '150px', align: 'left' }, */
        { field: 'ruleGroupName', header: 'Rule Group Name', width: '12%', align: 'left' },
        { field: 'ruleName', header: 'Rule Name', width: '10%', align: 'left' },
        { field: 'dataViewName', header: 'Data View Name', width: '10%', align: 'left' },
        /* { field: 'approver', header: 'Approver', width: '130px', align: 'left' } */
        /* ,
        { field: 'status', header: 'status', width: '150px', align: 'left' } */

    ];

    constructor(
        private notificationBatchService: NotificationBatchService,
        private accountingDataService: AccountingDataService,
        private reconcileService: ReconcileService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService,
        private config: NgbDatepickerConfig,
        private notificationService: NotificationsService,
        private datePipe: DatePipe,
    ) {
        this.columnOptions = [];
        for (let i = 0; i < this.notificationBatchTableColumns.length; i++) {
            this.columnOptions.push({ label: this.notificationBatchTableColumns[i].header, value: this.notificationBatchTableColumns[i] });
        }
        this.summaryPageEvent.pageIndex = 0;
        this.summaryPageEvent.pageSize = ITEMS_PER_PAGE;

        this.itemsPerPage = ITEMS_PER_PAGE;
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {

    }

    ngOnInit() {

        this.fullScreenHeight = (this.commonService.screensize().height - 190) + 'px';
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        this.sideBarHeight = (this.commonService.screensize().height - 200) + 'px';
        $(".testHei").css({
            'height': this.fullScreenHeight
        });
        this.fetchNotificationBatchList('IN_PROCESS');
        $(".sideBarNotificationQue").css('height', this.sideBarHeight);
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.notificationBatchService.usersListTask().subscribe(
            (res) => {
                this.usersList = res;
                //console.log('this.usersList ' + JSON.stringify(this.usersList));
            });

        /* function to fetch sidebar hierarchy */
        this.notificationBatchService.fetchHierarchySideBar().subscribe(
            (res) => {
                this.sideBarHierarchyList = res;
                if (this.sideBarHierarchyList[0].children.length) {
                    $('.hideSidePanel').removeClass('ifJEDetailsPage');
                } else {
                    $('.hideSidePanel').addClass('ifJEDetailsPage');
                }
                //console.log('this.sideBarHierarchyList ' + JSON.stringify(this.sideBarHierarchyList));
            });

        this.registerChangeInNotificationBatches();
        $(".search-icon-body").click(function() {
            if ($(".ftlSearch md-input-container").hasClass("hidethis")) {
                $(".ftlSearch md-input-container").removeClass("hidethis");
                $(".ftlSearch md-input-container").addClass("show-this");
            } else if ($(".ftlSearch md-input-container").hasClass("show-this")) {
                const value = $('.ftlSearch md-input-container .mySearchBox').filter(function() {
                    return this.value != '';
                });
                if (value.length <= 0) { // zero-length string
                    $(".ftlSearch md-input-container").removeClass("show-this");
                    $(".ftlSearch md-input-container").addClass("hidethis");
                }
            } else {
                $(".ftlSearch md-input-container").addClass("show-this");
            }
        });
        $(".ftlSearch md-input-container .mySearchBox").blur(function() {
            const value = $('.ftlSearch md-input-container .mySearchBox').filter(function() {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".ftlSearch md-input-container").removeClass("show-this");
                $(".ftlSearch md-input-container").addClass("hidethis");
            }
        });

    }

    nodeSelect(event) {
        this.selectedUserId = event.node.userId;
        //   console.log('event ' + this.selectedUserId);
        this.fetchNotificationBatchList('IN_PROCESS');
        /* console.log('selectedFile ' + this.selectedFile); */
    }

    nodeUnselect(event) {
        /* console.log('event ' + JSON.stringify(event)); */
        /* console.log('selectedFile ' + this.selectedFile); */
    }

    fetchNotificationBatchList(status?: any) {
        // console.log('status ' + status);
        this.page = 0;
        if (this.selectedUserId || status) {
            this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage, this.selectedModuleType, status, this.selectedUserId).subscribe(
                (res) => {
                    this.notificationBatches = res.json();
                    this.notificationBatches.forEach((element, i) => {
                        setTimeout(() => {
                            if (element['image']) {
                                $('.userPicture').css({
                                    'background-image': 'url(data:image/png;base64,' + element['image'] + ')',
                                    'background-position': '-15px',
                                    'background-size': '125px'
                                });
                            } else {
                                $('.userPicture').attr('data-content', element.approver.charAt(0));
                            }
                        }, 0);
                        /*  if (!element.ruleGroupsList.length) {
                             this.notificationBatches.splice(i, 1);
                         } */
                    });
                    this.batchListRecordsLength = +res.headers.get('x-count');
                    //  console.log('List of notificationBatches ::' + JSON.stringify(this.notificationBatches));
                    if (status == 'IN_PROCESS') {
                        $('.inProcessList').css('background-color', '#e1e1e1');
                        $('.approvedList, .rejectedList, .classAllList').css('background-color', 'initial');
                    } else if (status == 'APPROVED') {
                        $('.approvedList').css('background-color', '#e1e1e1');
                        $('.inProcessList, .rejectedList, .classAllList').css('background-color', 'initial');
                    } else if (status == 'REJECTED') {
                        $('.rejectedList').css('background-color', '#e1e1e1');
                        $('.approvedList, .inProcessList, .classAllList').css('background-color', 'initial');
                    } else {
                        $('.classAllList').css('background-color', '#e1e1e1');
                        $('.approvedList, .rejectedList, .inProcessList').css('background-color', 'initial');
                    }
                });
        } else {
            this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage, this.selectedModuleType, status).subscribe(
                (res) => {
                    this.notificationBatches = res.json();
                    this.batchListRecordsLength = +res.headers.get('x-count');
                    this.notificationBatches.forEach((element, i) => {
                        setTimeout(() => {
                            if (element['image']) {
                                $('.userPicture').css({
                                    'background-image': 'url(data:image/png;base64,' + element['image'] + ')',
                                    'background-position': '-15px',
                                    'background-size': '125px'
                                });
                            } else {
                                $('.userPicture').attr('data-content', element.approver.charAt(0));
                            }
                        }, 0);
                        /*if (!element.ruleGroupsList.length) {
                            this.notificationBatches.splice(i, 1);
                        }*/
                    });
                    //   console.log('List of notificationBatches ::' + JSON.stringify(this.notificationBatches));
                    if (status == 'IN_PROCESS') {
                        $('.inProcessList').css('background-color', '#e1e1e1');
                        $('.approvedList, .rejectedList, .classAllList').css('background-color', 'initial');
                    } else if (status == 'APPROVED') {
                        $('.approvedList').css('background-color', '#e1e1e1');
                        $('.inProcessList, .rejectedList, .classAllList').css('background-color', 'initial');
                    } else if (status == 'REJECTED') {
                        $('.rejectedList').css('background-color', '#e1e1e1');
                        $('.approvedList, .inProcessList, .classAllList').css('background-color', 'initial');
                    } else {
                        $('.classAllList').css('background-color', '#e1e1e1');
                        $('.approvedList, .rejectedList, .inProcessList').css('background-color', 'initial');
                    }
                });
        }

    }

    testCheck() {
        this.listOfSelected = [];
        //  console.log('called test' + JSON.stringify(this.selectedValues));
        for (let i = 0; i < this.selectedValues.length; i++) {
            const obj = {
                "id": this.selectedValues[i]
            };
            this.listOfSelected.push(obj);
        }
        //  console.log('listOfSelected ' + JSON.stringify(this.listOfSelected));
    }

    /* function to approve selected batches */

    approveSelectedBatch(obj?: any) {
        const idArr = [];
        if (obj) {
            //   console.log('selected obj ' + JSON.stringify(obj));
            idArr.push(obj.id.toString());
            //  console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.approveBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    if (this.display == true) {
                        this.display = false;
                    }
                    this.commonService.success(
                        'Success!',
                        'Successfully Approved'
                    )
                    if (this.visibleSidebar5) {
                        this.displaySelectedBatchDetails(res['errorReport'][0]['details'], 'APPROVED', this.selectedModule);
                        this.selectedBatchRows = [];
                        this.selectedBatchDetails.status = 'Approved';
                    } else {
                        this.fetchNotificationBatchList('IN_PROCESS');
                        this.selectedBatchRows = [];
                    }
                });
        } else {
            this.selectedBatchRows.forEach((element) => {
                idArr.push(element.id.toString());
            });

            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.approveBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    this.commonService.success(
                        'Success!',
                        'Successfully Approved'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        }
    }
    reassignSelectedBatch(obj?: any) {
        //   console.log('id ' + this.selectedUser);
        const idArr = [];
        //  console.log('approve selected batch :' + JSON.stringify(this.selectedBatchRows));
        if (obj) {
            idArr.push(obj.id.toString());
            //    console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.reassignBatchTask(this.selectedUser, idArr, 'BATCH').subscribe(
                (res) => {
                    if (this.display == true) {
                        this.display = false;
                    }
                    this.commonService.success(
                        'Success!',
                        'Successfully Reassigned'
                    )
                    this.visibleSidebar5 = false;
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        } else {
            this.selectedBatchRows.forEach((element) => {
                idArr.push(element.id.toString());
            });
            //  console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.reassignBatchTask(this.selectedUser, idArr, 'BATCH').subscribe(
                (res) => {
                    this.commonService.success(
                        'Success!',
                        'Successfully Reassigned'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        }

    }

    approveSelectedReports() {
        //  console.log('this.selectedBatchId ' + this.selectedBatchId);
        //  console.log('selected rec to approve ::' + JSON.stringify(this.selectedValues));
        this.notificationBatchService.approveBatchTask(this.selectedValues, 'RECORD', this.selectedBatchId).subscribe(
            (res) => {
                this.visibleSidebar5 = false;
                this.commonService.success(
                    'Success!',
                    'Approved'
                )

            });
    }

    /*     approveSelectedBatch(obj?: any) {
            if (obj) {
                console.log('selected obj ' + JSON.stringify(obj));
                this.notificationBatchService.approveBatchTask(obj.taskId, obj.id).subscribe(
                    (res) => {
                        if (this.display == true) {
                            this.display = false;
                        }
                        this.commonService.success(
                            'Success!',
                            'Successfully Approved'
                        )
                        this.fetchNotificationBatchList();
                        this.selectedBatchRows = [];
                    });
            } else {
                console.log('approve selected batch :' + JSON.stringify(this.selectedBatchRows));
                console.log('notificationBatches ' + JSON.stringify(this.notificationBatches));
                let count = 0;
                for (var i = 0; i < this.notificationBatches.length; i++) {
                    for (var j = 0; j < this.selectedBatchRows.length; j++) {
                        if ((this.notificationBatches[i].id == this.selectedBatchRows[j].id) && (this.notificationBatches[i].statusCode == 'APPROVED')) {
                            count++;
                        }
                    }
                }
                if (count > 0) {
                    this.commonService.error(
                        'Warning!',
                        'Selected Batch is already Approved'
                    )
                } else {
                    this.notificationBatchService.approveBatchTask(this.selectedBatchRows[0].taskId, this.selectedBatchRows[0].id).subscribe(
                        (res) => {
                            this.commonService.success(
                                'Success!',
                                'Successfully Approved'
                            )
                            this.fetchNotificationBatchList();
                            this.selectedBatchRows = [];
                        });
                }
    
            }
        } */

    /* function to reject selected batches */

    rejectSelectedBatch(obj?: any) {
        const idArr = [];
        if (obj) {
            //  console.log('selected obj ' + JSON.stringify(obj));
            idArr.push(obj.id.toString());
            //  console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.rejectBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    if (this.display == true) {
                        this.display = false;
                    }
                    this.commonService.success(
                        'Success!',
                        'Successfully Rejected'
                    )
                    if (this.visibleSidebar5) {
                        this.displaySelectedBatchDetails(res['errorReport'][0]['details'], 'REJECTED', this.selectedModule);
                        this.selectedBatchRows = [];
                        this.selectedBatchDetails.status = 'Rejected';
                    } else {
                        this.fetchNotificationBatchList('IN_PROCESS');
                        this.selectedBatchRows = [];
                    }
                    /* this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = []; */
                });
        } else {
            //   console.log('approve selected batch :' + JSON.stringify(this.selectedBatchRows));
            this.selectedBatchRows.forEach((element) => {
                idArr.push(element.id.toString());
            });
            //   console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.rejectBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    this.commonService.success(
                        'Success!',
                        'Successfully Rejected'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        }
    }


    rejectSelectedReports() {
        //    console.log('this.selectedBatchId ' + this.selectedBatchId);
        //   console.log('selected rec to approve ::' + JSON.stringify(this.selectedValues));
        this.notificationBatchService.rejectBatchTask(this.selectedValues, 'RECORD', this.selectedBatchId).subscribe(
            (res) => {
                this.visibleSidebar5 = false;
                this.commonService.success(
                    'Success!',
                    'Successfully Rejected'
                )

            });
    }

    /*     rejectSelectedBatch(obj?: any) {
            if (obj) {
                console.log('selected obj ' + JSON.stringify(obj));
                this.notificationBatchService.rejectBatchTask(obj.taskId, obj.id).subscribe(
                    (res) => {
                        if (this.display == true) {
                            this.display = false;
                        }
                        this.commonService.success(
                            'Success!',
                            'Successfully Rejected'
                        )
                        this.fetchNotificationBatchList();
                        this.selectedBatchRows = [];
                    });
            } else {
                console.log('reject selected batch :' + JSON.stringify(this.selectedBatchRows));
                console.log('notificationBatches ' + JSON.stringify(this.notificationBatches));
                let count = 0;
                for (var i = 0; i < this.notificationBatches.length; i++) {
                    for (var j = 0; j < this.selectedBatchRows.length; j++) {
                        if ((this.notificationBatches[i].id == this.selectedBatchRows[j].id) && (this.notificationBatches[i].statusCode == 'REJECTED')) {
                            count++;
                        }
                    }
                }
                if (count > 0) {
                    this.commonService.error(
                        'Warning!',
                        'Selected Batch is already Rejected'
                    )
                } else {
                    this.notificationBatchService.rejectBatchTask(this.selectedBatchRows[0].taskId, this.selectedBatchRows[0].id).subscribe(
                        (res) => {
                            this.commonService.success(
                                'Success!',
                                'Successfully Rejected'
                            )
                            this.fetchNotificationBatchList();
                            this.selectedBatchRows = [];
                        });
                }
            }
        } */

    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        /* this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage).subscribe(
            (res) => {
                this.notificationBatches = res.json();
                this.batchListRecordsLength = +res.headers.get('x-count');
                this.notificationBatches.splice(this.notificationBatches.length, 1);
            }); */

        this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage, this.selectedModuleType, this.selectedProcessType, this.selectedUserId).subscribe(
            (res) => {
                this.notificationBatches = res.json();
                this.batchListRecordsLength = +res.headers.get('x-count');
                this.notificationBatches.splice(this.notificationBatches.length, 1);
                this.notificationBatches.forEach((element, i) => {
                    setTimeout(() => {
                        if (element['image']) {
                            $('.userPicture').css({
                                'background-image': 'url(data:image/png;base64,' + element['image'] + ')',
                                'background-position': '-15px',
                                'background-size': '125px'
                            });
                        } else {
                            $('.userPicture').attr('data-content', element.approver.charAt(0));
                        }
                    }, 0);
                    if (!element.ruleGroupsList.length) {
                        this.notificationBatches.splice(i, 1);
                    }
                });
            })
    }

    rejectFun(event) {
        //  console.log('rejectFun' + JSON.stringify(event));
    }


    /* function called when row is selected */
    /* function called when row is deselected */
    onRowSelect(event) {
        //  console.log('selectedRows ' + JSON.stringify(this.selectedRows));
        //  console.log('event ' + JSON.stringify(event.data));
        this.selectedBatchRows.push(event.data);
        this.selectedBatchRows = this.selectedBatchRows.filter((data, index, self) =>
            self.findIndex((t) => t.id === data.id && t.id === data.id) === index);
        if (this.selectedBatchRows.length > 0) {
            this.actionButtons = true;
        } else {
            this.actionButtons = false;
        }
        // console.log('event ' + JSON.stringify(this.selectedBatchRows));
    }


    onRowUnselect(event) {
        //  console.log('eve ' + JSON.stringify(event));
        for (let i = 0; i < this.selectedBatchRows.length; i++) {
            if (this.selectedBatchRows[i].id == event.data.id) {
                this.selectedBatchRows.splice(i, 1);
            }
        }

        //   console.log('deselect event ' + JSON.stringify(this.selectedBatchRows));
        if (this.selectedBatchRows.length > 0) {
            this.actionButtons = true;
        } else {
            this.actionButtons = false;
        }
    }

    getAccountedColumnHeaders() {
        this.accountingDataService.accountedColumnHeaders('accounted').subscribe(
            (res: any) => {
                this.accountedHeaders = res['columns'];
                this.accountedColumnOptions = [];
                for (let i = 0; i < this.accountedHeaders.length; i++) {
                    // this.accountedHeaders[i]['searchWord'] = "";
                    this.accountedColumnOptions.push({ label: this.accountedHeaders[i].header, value: this.accountedHeaders[i] });
                }
            },
            (res: Response) => {
                this.onError(res.json())
                this.commonService.error('Internal Server Error!', 'Please contact system admin');
            })
    }

    getAccountedSummary() {
        const status = this.currentStatus;
        const enDate = new Date();
        const stDate = new Date(enDate);
        stDate.setDate(stDate.getDate() - 30);
        let curStatus: any;
        if (this.selectedBatchDetails.status == 'InProcess') {
            curStatus = 'IN_PROCESS';
        } else if (this.selectedBatchDetails.status == 'Approved') {
            curStatus = 'Approved';
        } else if (this.selectedBatchDetails.status == 'Rejected') {
            curStatus = 'Rejected';
        }
        this.groupingSummaryParams.filters = [
            /* {
                key: 'ledger_name',
                dataType: 'STRING',
                values: [this.selectedLederRef[0].ledgerName]
            }, */
            {
                key: 'final_status',
                dataType: 'STRING',
                values: [curStatus]
            }
        ]
        this.isAccountedSummary = false;
        this.groupingSummaryParams.groupId = this.selGrpId;
        /* this.groupingSummaryParams.periodFactor = this.selectedBatchDetails.qualfierDate[0].columnName; */
        this.groupingSummaryParams.viewId = this.selViewId;
        this.groupingSummaryParams.status = this.selectedLederRef[0].status;
        this.groupingSummaryParams.pageNumber = this.summaryPageEvent.pageIndex;
        this.groupingSummaryParams.pageSize = this.summaryPageEvent.pageSize;
        /* this.groupingSummaryParams.rangeFrom = stDate;
        this.groupingSummaryParams.rangeTo = enDate; */
        this.accountingDataService.accountedSummaryInfo(this.groupingSummaryParams).subscribe(
            (res: any) => {
                this.dataViewLines = [];
                this.accountedSummaryData = res.summary;
                //    console.log('this.accountedSummaryData ::' + JSON.stringify(this.accountedSummaryData));
                this.visibleSidebar5 = true;

                if (status == 'InProcess') {
                    $('.inprocessRec').css('background-color', '#e1e1e1');
                    $('.approvedRec, .rejectedRec, .classAll').css('background-color', 'initial');
                } else if (status == 'Approved') {
                    $('.approvedRec').css('background-color', '#e1e1e1');
                    $('.inprocessRec, .rejectedRec, .classAll').css('background-color', 'initial');
                } else if (status == 'Rejected') {
                    $('.rejectedRec').css('background-color', '#e1e1e1');
                    $('.approvedRec, .inprocessRec, .classAll').css('background-color', 'initial');
                } else {
                    $('.classAll').css('background-color', '#e1e1e1');
                    $('.approvedRec, .rejectedRec, .inprocessRec').css('background-color', 'initial');
                }
            },
            (res: Response) => {
                this.onError(res)
                this.commonService.error('Error!', 'Error Occured while fetching Accounted Summary!');
            }
        );
    }

    /* Function to get selected batch details */
    displaySelectedBatchDetails(id: any, status: any, module: any) {
        this.selectedModule = module;
        //   console.log('Select Batch id ::' + id);
        this.currentStatus = status;
        this.notificationBatchService.getApprovalsHistory(id).subscribe(
            (res) => {
                this.approvalDetails = res;
                this.processDetails = this.approvalDetails.details;
                //    console.log('this.approvalDetails ' + JSON.stringify(this.approvalDetails));
                //   console.log('this.processDetails ' + JSON.stringify(this.processDetails));
                this.approvalsHistory = this.approvalDetails.approversList;
                if (this.approvalDetails.approversList) {
                    for (let j = 0; j < this.approvalsHistory.length; j++) {
                        if (j + 1 != this.approvalsHistory.length) {
                            this.approvalsHistory[j]['next'] = true;
                        }
                    }
                }
                this.selectedBatchDetails = {};
                this.notificationBatches.forEach((element) => {
                    if (element.id == id) {
                        this.selectedBatchDetails = element;
                        this.selectedValues = '';
                        this.viewDetailedReport(this.selectedBatchDetails.id);
                        //  this.display = true;
                    }
                });
            });
    }

    viewDetailedReport(id: any) {
        console.log('selectedBatchDetails ' + JSON.stringify(this.selectedBatchDetails));
        console.log('this.selectedModule ' + this.selectedModule);
        if (this.selectedModule == 'ACCOUNTING_APPROVALS') {
            this.accountedHeaders = [];
            let tempDateRange: any;
            const enDate = new Date();
            const stDate = new Date(enDate);
            stDate.setDate(stDate.getDate() - 1825);
            this.getAccountedColumnHeaders();
            this.notificationBatches.forEach((element) => {
                if (id == element.id) {
                    this.selViewId = element.dataViewId;
                    this.selGrpId = element.ruleGroupsList[0].groupId;
                    tempDateRange = {
                        "groupId": element.ruleGroupsList[0].groupId,
                        "viewId": element.dataViewId,
                        "rangeFrom": stDate,
                        "rangeTo": enDate,
                        "periodFactor": "fileDate"
                    }
                }
            });
            this.accountedLedgerList = [];
            this.reconcileService.getQualifierColumns(tempDateRange.viewId).subscribe(
                (res: any) => {
                    this.qualifierColumns = res;
                    //    console.log('this.qualifierColumns ::' + JSON.stringify(this.qualifierColumns));
                });
            this.accountingDataService.distinctStatuses(tempDateRange).subscribe(
                (res: any) => {
                    this.statusObject = res;
                    //   console.log('this.statusObject ::' + JSON.stringify(this.statusObject));
                    if (this.statusObject.summary.length > 0) {
                        this.statusObject.summary.forEach((element) => {
                            //un accounted, not reconciled
                            //un accounted, reconciled
                            //accounted, reconciled
                            //accounted, not reconciled
                            if ((element.status == 'accounted' || element.status == 'accounted, reconciled' || element.status == 'accounted, not reconciled') && element.amountValue != 0) {
                                this.accountedLedgerList.push(element);
                            }
                        });
                        console.log('this.accountedLedgerList ::' + JSON.stringify(this.accountedLedgerList));
                        this.selectedLederRef = this.accountedLedgerList;
                        this.getColumnHeaders(tempDateRange.groupId, tempDateRange.viewId);
                        this.getAccountedSummary();
                    }
                });
        } else {
            const status = this.currentStatus;
            this.page = 0;
            //    console.log('id ' + id);
            this.selectedBatchId = id;
            //    console.log('selectedBatchId ' + this.selectedBatchId);
            this.selectedBatchDetailsReports = [];
            this.notificationBatchService.getSelectedBatchDetailss(id, this.page, this.itemsPerPage, status).subscribe(
                (res) => {
                    this.selectedBatchDetailsReports = res.json();
                    setTimeout(() => {
                        if (this.selectedBatchDetailsReports) {
                            this.showTabB = true;
                            const pos = this.selectedBatchDetailsReports[0].source.length + 1;
                            /* console.log(pos);
                            console.log(document.querySelector('.borderSepration table tbody tr td:nth-child(' + pos + ')')); */
                            $('.borderSepration table tbody tr td:nth-child(1)').addClass('tbodyLastChildBorder');
                            $('.borderSepration table tbody tr td:nth-child(' + pos + ')').addClass('tbodyLastChildBorder');
                            /*  $('.borderSepration table tr:last-child th:nth-child('+5+')').css('background','#ef5350');
                             this.selectedBatchDetailsReports[0].source.forEach(element => {
                                 
                             });
                             $('.borderSepration table tr:last-child th:nth-child('+5+')').css('background','#EC407A');
                             $('.borderSepration table tr:last-child th:nth-child('+5+')').css('background','#42A5F5'); */
                        }
                    }, 0);

                    //    console.log('this.selectedBatchDetailsReports ' + JSON.stringify(this.selectedBatchDetailsReports));
                    this.viewDetailRecordsLength = +res.headers.get('xcount');
                    //    console.log('this.viewDetailRecordsLength ' + JSON.stringify(this.viewDetailRecordsLength));
                    this.selectedBatchDetailsReports.forEach((element) => {
                        const arr = [];
                        this.batchRecordsSrcTrgDetails.push(arr);
                    });
                    this.visibleSidebar5 = true;

                    if (status == 'InProcess') {
                        $('.inprocessRec').css('background-color', '#e1e1e1');
                        $('.approvedRec, .rejectedRec, .classAll').css('background-color', 'initial');
                    } else if (status == 'Approved') {
                        $('.approvedRec').css('background-color', '#e1e1e1');
                        $('.inprocessRec, .rejectedRec, .classAll').css('background-color', 'initial');
                    } else if (status == 'Rejected') {
                        $('.rejectedRec').css('background-color', '#e1e1e1');
                        $('.approvedRec, .inprocessRec, .classAll').css('background-color', 'initial');
                    } else {
                        $('.classAll').css('background-color', '#e1e1e1');
                        $('.approvedRec, .rejectedRec, .inprocessRec').css('background-color', 'initial');
                    }
                });
        }

    }

    getColumnHeaders(groupId, viewId) {
        this.accountingDataService.fetchingColHeaders(groupId, viewId, 'accounted').subscribe(
            (res: any) => {
                this.sourceHeaderColumns1 = res;
                this.dataHeaderColumns = this.sourceHeaderColumns1.columns;
                this.dataColumnOptions = [];
                for (let i = 0; i < this.dataHeaderColumns.length; i++) {
                    this.dataHeaderColumns[i]['searchWord'] = "";
                    this.dataColumnOptions.push({ label: this.dataHeaderColumns[i].header, value: this.dataHeaderColumns[i] });
                }
            },
            (res: Response) => {
                this.onError(res.json())
                this.commonService.error('Internal Server Error!', 'Please contact system admin');
            })
    }

    viewDetailedReports(status?: any) {

        this.page = 0;
        //    console.log('selectedBatchId ' + this.selectedBatchId);
        this.selectedBatchDetailsReports = [];
        this.notificationBatchService.getSelectedBatchDetailss(this.selectedBatchId, this.page, this.itemsPerPage, status).subscribe(
            (res) => {
                this.selectedBatchDetailsReports = res.json();
                //    console.log('this.selectedBatchDetailsReports ' + JSON.stringify(this.selectedBatchDetailsReports));
                this.viewDetailRecordsLength = +res.headers.get('xcount');
                //    console.log('this.viewDetailRecordsLength ' + JSON.stringify(this.viewDetailRecordsLength));
                this.selectedBatchDetailsReports.forEach((element) => {
                    const arr = [];
                    this.batchRecordsSrcTrgDetails.push(arr);
                });
                if (status == 'INPROCESS') {
                    $('.inprocessRec').css('background-color', '#e1e1e1');
                    $('.approvedRec, .rejectedRec, .classAll').css('background-color', 'initial');
                } else if (status == 'APPROVED') {
                    $('.approvedRec').css('background-color', '#e1e1e1');
                    $('.inprocessRec, .rejectedRec, .classAll').css('background-color', 'initial');
                } else if (status == 'REJECTED') {
                    $('.rejectedRec').css('background-color', '#e1e1e1');
                    $('.approvedRec, .inprocessRec, .classAll').css('background-color', 'initial');
                } else {
                    $('.classAll').css('background-color', '#e1e1e1');
                    $('.approvedRec, .rejectedRec, .inprocessRec').css('background-color', 'initial');
                }
            });
    }

    onPaginateChangeViewDetail(event) {
        this.selectedBatchDetailsReports = [];
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.notificationBatchService.getSelectedBatchDetailss(this.selectedBatchId, this.page, this.itemsPerPage).subscribe(
            (res) => {
                this.selectedBatchDetailsReports = res.json();
                this.viewDetailRecordsLength = +res.headers.get('xcount');
                this.selectedBatchDetailsReports.splice(this.selectedBatchDetailsReports.length, 1);
                this.selectedBatchDetailsReports.forEach((element) => {
                    const arr = [];
                    this.batchRecordsSrcTrgDetails.push(arr);
                });
            });
    }

    getDetailRecord(event) {
        const obj = this.selectedBatchDetailsReports[event.index].reconReference;
        if (this.lastindex != undefined) {
            this.batchRecordsSrcTrgDetails[this.lastindex] = [];
        }
        this.notificationBatchService.getSelectedBatchSrcTrgDetails(obj, this.selectedBatchId).subscribe(
            (res) => {
                this.fetchBatchRecordsSrcTrg = res;
                this.batchRecordsSrcTrgDetails[event.index] = this.fetchBatchRecordsSrcTrg[0];
                this.lastindex = event.index;
            });
    }

    getAccountedDetails(e?) {
        const len = 0;
        this.filterValues1 = [];
        const filterKeys: any[] = [];
        this.selectedDataLinesToAccount = [];
        this.dataViewLines = [];
        if (this.selectedSummary.length > 0) {
            /* let tempLedgerVal = [];
            tempLedgerVal.push(e['ledger_name']); */
            this.filterValues1 = [
                {
                    key: 'source_meaning',
                    dataType: 'STRING'
                },
                {
                    key: 'category_meaning',
                    dataType: 'STRING'
                },
                {
                    key: 'entered_currency',
                    dataType: 'STRING'
                },
                {
                    key: 'ledger_name',
                    dataType: 'STRING'
                },
                {
                    key: 'final_status',
                    dataType: 'STRING',
                    values: [this.selectedBatchDetails.status]
                }
            ];
            console.log('this.selectedSummary ' + JSON.stringify(this.selectedSummary));
            let arr: any[] = [];
            if (this.filterValues1.length > 0) {
                for (let l = 0; l < this.filterValues1.length; l++) {
                    arr = [];
                    for (let k = 0; k < this.selectedSummary.length; k++) {
                        if (this.filterValues1[l].key === 'source_meaning') {
                            arr.push(this.selectedSummary[k]['source_meaning']);
                            this.filterValues1[l].values = arr;
                        } else if (this.filterValues1[l].key === 'category_meaning') {
                            arr.push(this.selectedSummary[k]['category_meaning']);
                            this.filterValues1[l].values = arr;
                        } else if (this.filterValues1[l].key === 'entered_currency') {
                            arr.push(this.selectedSummary[k]['entered_currency']);
                            this.filterValues1[l].values = arr;
                        } else if (this.filterValues1[l].key === 'ledger_name') {
                            arr.push(this.selectedSummary[k]['ledger_name']);
                            this.filterValues1[l].values = arr;
                        }
                    }
                }
            }
            // console.log('fltrers : '+JSON.stringify(this.filterValues1));
            if (this.selectedKeys.length > 0) {
                for (let i = 0; i < this.filterValues.length; i++) {
                    this.filterValues1.push(this.filterValues[i]);
                }
            }
            console.log('this.qualifierColumns ' + JSON.stringify(this.qualifierColumns));
            this.groupingSummaryParams.filters = this.filterValues1;
            this.groupingSummaryParams.pageNumber = this.page;
            this.groupingSummaryParams.pageSize = this.itemsPerPage;
            this.groupingSummaryParams.sortOrderBy = this.sortOrder;
            if (!this.groupingSummaryParams.sortByColumnName) {
                this.groupingSummaryParams.sortByColumnName = this.qualifierColumns[0].columnName;
            }
            this.groupingSummaryParams.columnSearch = [];
            // this.groupingSummaryParams.periodFactor = this.selectedBatchDetails.qualfierDate[0].columnName;
            for (let i = 0; i < this.dataHeaderColumns.length; i++) {
                if (this.dataHeaderColumns[i].searchWord != "") {
                    this.groupingSummaryParams.columnSearch.push({
                        columnName: this.dataHeaderColumns[i].columnName,
                        searchWord: this.dataHeaderColumns[i].searchWord
                    })
                }
            }
            if (this.searchWord != "") {
                this.groupingSummaryParams.searchWord = this.searchWord;
            }
            this.accountingDataService.detailedInfoRecords(this.groupingSummaryParams).subscribe(
                (res: any) => {
                    this.isAccountedSummary = true;
                    this.dataViewLines = res;
                    this.groupingSummaryParams.columnSearch = [];
                    if (this.dataViewLines.length > 1) {
                        this.changeSort = false;
                        this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
                        this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
                    } else {
                        this.commonService.info('Info!', 'No Records Found!');
                    }
                },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Internal Server Error!', 'Please contact system admin');
                }
            )
        } else {
            this.dataViewLines = [];
            this.filterValues1 = [
                {
                    key: 'ledger_name',
                    dataType: 'STRING',
                    values: [this.selectedLederRef[0].ledgerName]
                }
            ];
            if (this.selectedKeys.length > 0) {
                for (let i = 0; i < this.filterValues.length; i++) {
                    this.filterValues1.push(this.filterValues[i]);
                }
            }
            this.groupingSummaryParams.filters = this.filterValues1;
            this.groupingSummaryParams.pageNumber = this.page;
            this.groupingSummaryParams.pageSize = this.itemsPerPage;
            this.groupingSummaryParams.sortOrderBy = this.sortOrder;
            this.groupingSummaryParams.sortByColumnName = this.qualifierColumns[0].columnName;
            this.groupingSummaryParams.columnSearch = [];
            for (let i = 0; i < this.dataHeaderColumns.length; i++) {
                if (this.dataHeaderColumns[i].searchWord != "") {
                    this.groupingSummaryParams.columnSearch.push({
                        columnName: this.dataHeaderColumns[i].columnName,
                        searchWord: this.dataHeaderColumns[i].searchWord
                    })
                }
            }
            if (this.searchWord != "") {
                this.groupingSummaryParams.searchWord = this.searchWord;
            }
            this.accountingDataService.detailedInfoRecords(this.groupingSummaryParams).subscribe(
                (res: any) => {
                    this.isAccountedSummary = true;
                    this.dataViewLines = res;
                    this.groupingSummaryParams.columnSearch = [];
                    if (this.dataViewLines.length > 1) {
                        this.changeSort = false;
                        this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
                        this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
                    } else {
                        this.commonService.info('Info!', 'No Records Found!');
                    }
                },
                (res: Response) => {
                    this.onError(res.json()
                    )
                    this.commonService.error('Internal Server Error!', 'Please contact system admin');
                }
            )
        }
    }

    getGroupByAcctRecords() {
        this.selectedDataLinesToAccount = [];
        this.pushedGroupingValues = [];
        const values: any[] = [];
        const keys: any[] = [];
        const filters: any[] = [];
        let arr: any[] = [];
        let len = 0;
        this.filterValues = [];
        if (this.selectedKeys.length > 0) {
            for (let i = 0; i < this.selectedKeys.length; i++) {
                len = 0;
                if (this.filterValues.length <= 0) {
                    this.filterValues.push({
                        key: this.selectedKeys[i].filterColumn,
                        dataType: this.selectedKeys[i].dataType
                    })
                } else {
                    for (let j = 0; j < this.filterValues.length; j++) {
                        if (this.filterValues[j].key == this.selectedKeys[i].filterColumn) {
                            len = len + 1;
                        }
                    }
                    if (len > 0) {

                    } else {
                        this.filterValues.push({
                            key: this.selectedKeys[i].filterColumn,
                            dataType: this.selectedKeys[i].dataType
                        })
                    }
                }

            }
            if (this.filterValues.length > 0) {
                for (let l = 0; l < this.filterValues.length; l++) {
                    arr = [];
                    for (let k = 0; k < this.selectedKeys.length; k++) {
                        if (this.filterValues[l].key === this.selectedKeys[k].filterColumn && this.filterValues[l].dataType != 'DATE') {
                            arr.push(this.selectedKeys[k].name);
                            this.filterValues[l].values = arr;
                        } else if (this.filterValues[l].key === this.selectedKeys[k].filterColumn && this.filterValues[l].dataType == 'DATE') {
                            arr.push(this.datePipe.transform(this.selectedKeys[k].name, 'yyyy-MM-dd'));
                            this.filterValues[l].values = arr;
                        }
                    }
                }
            }
            this.groupingSummaryParams.filters = this.filterValues;
            const pushLen = 0;
            this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).subscribe(
                (res: any) => {
                    this.groupingSummaryFilters = res;
                    for (let g = 0; g < this.filterValues.length; g++) {
                        for (let h = 0; h < this.groupingSummary.length; h++) {
                            if (this.filterValues[g].key === this.groupingSummary[h].filterName) {
                                this.pushedGroupingValues.push(this.groupingSummary[h]);
                            }
                        }
                    }
                    for (let i = 0; i < this.groupingSummaryFilters.length; i++) {
                        this.pushedGroupingValues.push(this.groupingSummaryFilters[i]);
                    }
                    this.groupingSummary = this.pushedGroupingValues;
                },
                (res: Response) => {
                    this.onError(res)
                    this.commonService.error('Error!', 'Error Occured while fetching Reconciliation Rule Groups!');
                }
            );
        } else {
            this.groupingSummaryParams.filters = [];
            this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).subscribe(
                (res: any) => {
                    this.groupingSummary = res;
                },
                (res: Response) => {
                    this.onError(res)
                    this.commonService.error('Error!', 'Error Occured while fetching Reconciliation Rule Groups!');
                }
            );
        }
        this.groupingSummaryParams.pageNumber = this.page;
        this.groupingSummaryParams.pageSize = this.itemsPerPage;
        this.groupingSummaryParams.sortOrderBy = this.sortOrder;
        this.groupingSummaryParams.sortByColumnName = this.qualifierColumns[0].columnName;;
        this.groupingSummaryParams.columnSearch = [];
        for (let i = 0; i < this.dataHeaderColumns.length; i++) {
            if (this.dataHeaderColumns[i].searchWord != "") {
                this.groupingSummaryParams.columnSearch.push({
                    columnName: this.dataHeaderColumns[i].columnName,
                    searchWord: this.dataHeaderColumns[i].searchWord
                })
            }
        }
        if (this.searchWord != "") {
            this.groupingSummaryParams.searchWord = this.searchWord;
        }
        this.getAccountedSummary();
    }


    changeAppType(val: any) {
        if (val.value == 'IN_PROCESS') {
            this.selectedProcessType = 'IN_PROCESS';
            this.displayCheckBox = true;
            this.fetchNotificationBatchList('IN_PROCESS');
        } else if (val.value == 'APPROVED') {
            this.selectedProcessType = 'APPROVED';
            this.displayCheckBox = false;
            this.fetchNotificationBatchList('APPROVED');
        } else if (val.value == 'REJECTED') {
            this.selectedProcessType = 'REJECTED';
            this.displayCheckBox = false;
            this.fetchNotificationBatchList('REJECTED');
        } else {
            this.selectedProcessType = 'All';
            this.displayCheckBox = false;
            this.fetchNotificationBatchList();
        }
    }
    changeModuleType(val: any) {
        this.selectedModuleType = val.value;
        this.selectedProcessType = 'IN_PROCESS';
        this.fetchNotificationBatchList('IN_PROCESS');
    }
    ngOnDestroy() {
        this.notificationService.remove();
        // this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: NotificationBatch) {
        return item.id;
    }
    registerChangeInNotificationBatches() {
        this.eventSubscriber = this.eventManager.subscribe('notificationBatchListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

}
