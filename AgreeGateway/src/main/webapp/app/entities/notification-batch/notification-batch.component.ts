import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { NotificationBatch } from './notification-batch.model';
import { NotificationBatchService } from './notification-batch.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, DialogModule, AccordionModule, CheckboxModule, TreeModule,TreeNode} from 'primeng/primeng';
import { SidebarModules } from '../../shared/primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CommonService } from '../../entities/common.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-notification-batch',
    templateUrl: './notification-batch.component.html'
})
export class NotificationBatchComponent implements OnInit, OnDestroy {

    currentAccount: any;
    notificationBatches: any = [];
    copyNotificationBatches = [];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    isViewOnly: boolean = false;
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
    actionButtons: boolean = false;
    selectedBatchRows: any = [];
    selectedBatchDetails: any = {};
    display: boolean = false;
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
    usersList:any=[];
    selectedUser:any;
    displayCheckBox: boolean = true;
    sideBarHierarchyList: TreeNode[];
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
        { field: 'ruleGroupName', header: 'Rule Group Name', width: '150px', align: 'left' },
        { field: 'ruleName', header: 'Rule Name', width: '130px', align: 'left' },
        { field: 'dataViewName', header: 'Data View Name', width: '130px', align: 'left' },
        { field: 'approver', header: 'Approver', width: '130px', align: 'left' }
        /* ,
        { field: 'status', header: 'status', width: '150px', align: 'left' } */

    ];

    constructor(
        private notificationBatchService: NotificationBatchService,
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
    ) {
        this.columnOptions = [];
        for (let i = 0; i < this.notificationBatchTableColumns.length; i++) {
            this.columnOptions.push({ label: this.notificationBatchTableColumns[i].header, value: this.notificationBatchTableColumns[i] });
        }

        this.itemsPerPage = ITEMS_PER_PAGE;
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {

    }

    ngOnInit() {
        this.fullScreenHeight = (this.commonService.screensize().height - 290) + 'px';
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        this.sideBarHeight = (this.commonService.screensize().height - 200) + 'px';
        $(".testHei").css({
            'max-height': this.fullScreenHeight
        });
        this.fetchNotificationBatchList('IN_PROCESS');
        $(".sideBarNotificationQue").css('height', this.sideBarHeight);
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.notificationBatchService.usersListTask().subscribe(
            (res) => {
                this.usersList = res;
                console.log('this.usersList ' + JSON.stringify(this.usersList));
        });

        /* function to fetch sidebar hierarchy */
        this.notificationBatchService.fetchHierarchySideBar().subscribe(
            (res) => {
                this.sideBarHierarchyList = res;
                console.log('this.sideBarHierarchyList ' + JSON.stringify(this.sideBarHierarchyList));
        });

        this.registerChangeInNotificationBatches();
        $(".search-icon-body").click(function () {
            if ($(".ftlSearch md-input-container").hasClass("hidethis")) {
                $(".ftlSearch md-input-container").removeClass("hidethis");
                $(".ftlSearch md-input-container").addClass("show-this");
            } else if ($(".ftlSearch md-input-container").hasClass("show-this")) {
                var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
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
        $(".ftlSearch md-input-container .mySearchBox").blur(function () {
            var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".ftlSearch md-input-container").removeClass("show-this");
                $(".ftlSearch md-input-container").addClass("hidethis");
            }
        });
        
    }
    selectedUserId:any;
    selectedFile:any;
    nodeSelect(event) {
        this.selectedUserId = event.node.userId;  
        console.log('event ' + this.selectedUserId); 
          this.fetchNotificationBatchList('IN_PROCESS');
         /* console.log('selectedFile ' + this.selectedFile); */
     }

    nodeUnselect(event) {
        /* console.log('event ' + JSON.stringify(event)); */
        /* console.log('selectedFile ' + this.selectedFile); */
    }

    fetchNotificationBatchList(status?: any) {
        console.log('status ' + status);
        this.page = 0;
        if(this.selectedUserId || status){
            this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage, this.selectedModuleType, status, this.selectedUserId).subscribe(
                (res) => {
                    this.notificationBatches = res.json();
                    this.batchListRecordsLength = +res.headers.get('x-count');
                    console.log('List of notificationBatches ::' + JSON.stringify(this.notificationBatches));
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
        }else{
            this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage, this.selectedModuleType, status).subscribe(
                (res) => {
                    this.notificationBatches = res.json();
                    this.batchListRecordsLength = +res.headers.get('x-count');
                    console.log('List of notificationBatches ::' + JSON.stringify(this.notificationBatches));
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
        console.log('called test' + JSON.stringify(this.selectedValues));
        for (var i = 0; i < this.selectedValues.length; i++) {
            let obj = {
                "id": this.selectedValues[i]
            };
            this.listOfSelected.push(obj);
        }
        console.log('listOfSelected ' + JSON.stringify(this.listOfSelected));
    }

    /* function to approve selected batches */

    approveSelectedBatch(obj?: any) {
        let idArr = [];
        if (obj) {
            console.log('selected obj ' + JSON.stringify(obj));
            idArr.push(obj.id.toString());
            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.approveBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    if (this.display == true) {
                        this.display = false;
                    }
                    this.notificationService.success(
                        'Success!',
                        'Successfully Approved'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        } else {
            console.log('approve selected batch :' + JSON.stringify(this.selectedBatchRows));
            this.selectedBatchRows.forEach(element => {
                idArr.push(element.id.toString());
            });
            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.approveBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    this.notificationService.success(
                        'Success!',
                        'Successfully Approved'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        }
    }

    reassignSelectedBatch(obj?:any) {
        console.log('id ' + this.selectedUser);
        let idArr = [];
        console.log('approve selected batch :' + JSON.stringify(this.selectedBatchRows));
        if (obj) {
            idArr.push(obj.id.toString());
            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.reassignBatchTask(this.selectedUser, idArr, 'BATCH').subscribe(
                (res) => {
                    if (this.display == true) {
                        this.display = false;
                    }
                    this.notificationService.success(
                        'Success!',
                        'Successfully Reassigned'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        }else{
            this.selectedBatchRows.forEach(element => {
                idArr.push(element.id.toString());
            });
            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.reassignBatchTask(this.selectedUser, idArr, 'BATCH').subscribe(
            (res) => {
                this.notificationService.success(
                    'Success!',
                    'Successfully Reassigned'  
                )
                this.fetchNotificationBatchList('IN_PROCESS');
                this.selectedBatchRows = [];
            }); 
        }
         
    }

    approveSelectedReports() {
        console.log('this.selectedBatchId ' + this.selectedBatchId);
        console.log('selected rec to approve ::' + JSON.stringify(this.selectedValues));
        this.notificationBatchService.approveBatchTask(this.selectedValues, 'RECORD', this.selectedBatchId).subscribe(
            (res) => {
                this.visibleSidebar5 = false;
                this.notificationService.success(
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
                        this.notificationService.success(
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
                    this.notificationService.error(
                        'Warning!',
                        'Selected Batch is already Approved'
                    )
                } else {
                    this.notificationBatchService.approveBatchTask(this.selectedBatchRows[0].taskId, this.selectedBatchRows[0].id).subscribe(
                        (res) => {
                            this.notificationService.success(
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
        let idArr = [];
        if (obj) {
            console.log('selected obj ' + JSON.stringify(obj));
            idArr.push(obj.id.toString());
            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.rejectBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    if (this.display == true) {
                        this.display = false;
                    }
                    this.notificationService.success(
                        'Success!',
                        'Successfully Rejected'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        } else {
            console.log('approve selected batch :' + JSON.stringify(this.selectedBatchRows));
            this.selectedBatchRows.forEach(element => {
                idArr.push(element.id.toString());
            });
            console.log('idArr ' + JSON.stringify(idArr));
            this.notificationBatchService.rejectBatchTask(idArr, 'BATCH').subscribe(
                (res) => {
                    this.notificationService.success(
                        'Success!',
                        'Successfully Rejected'
                    )
                    this.fetchNotificationBatchList('IN_PROCESS');
                    this.selectedBatchRows = [];
                });
        }
    }


    rejectSelectedReports() {
        console.log('this.selectedBatchId ' + this.selectedBatchId);
        console.log('selected rec to approve ::' + JSON.stringify(this.selectedValues));
        this.notificationBatchService.rejectBatchTask(this.selectedValues, 'RECORD', this.selectedBatchId).subscribe(
            (res) => {
                this.visibleSidebar5 = false;
                this.notificationService.success(
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
                        this.notificationService.success(
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
                    this.notificationService.error(
                        'Warning!',
                        'Selected Batch is already Rejected'
                    )
                } else {
                    this.notificationBatchService.rejectBatchTask(this.selectedBatchRows[0].taskId, this.selectedBatchRows[0].id).subscribe(
                        (res) => {
                            this.notificationService.success(
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
        this.notificationBatchService.fetchListOfNotificationBatch(this.page + 1, this.itemsPerPage).subscribe(
            (res) => {
                this.notificationBatches = res.json();
                this.batchListRecordsLength = +res.headers.get('x-count');
                this.notificationBatches.splice(this.notificationBatches.length, 1);
            });
    }

    rejectFun(event) {
        console.log('rejectFun' + JSON.stringify(event));
    }


    /* function called when row is selected */
    /* function called when row is deselected */
    onRowSelect(event) {
        console.log('selectedRows ' + JSON.stringify(this.selectedRows));
        console.log('event ' + JSON.stringify(event.data));
        this.selectedBatchRows.push(event.data);
        this.selectedBatchRows = this.selectedBatchRows.filter((data, index, self) =>
            self.findIndex(t => t.id === data.id && t.id === data.id) === index);
        if (this.selectedBatchRows.length > 0) {
            this.actionButtons = true;
        } else {
            this.actionButtons = false;
        }
        console.log('event ' + JSON.stringify(this.selectedBatchRows));
    }


    onRowUnselect(event) {
        console.log('eve ' + JSON.stringify(event));
        for (var i = 0; i < this.selectedBatchRows.length; i++) {
            if (this.selectedBatchRows[i].id == event.data.id) {
                this.selectedBatchRows.splice(i, 1);
            }
        }

        console.log('deselect event ' + JSON.stringify(this.selectedBatchRows));
        if (this.selectedBatchRows.length > 0) {
            this.actionButtons = true;
        } else {
            this.actionButtons = false;
        }
    }
    /* Function to get selected batch details */
    displaySelectedBatchDetails(id: any) {
        console.log('Select Batch id ::' + id);
        this.notificationBatchService.getApprovalsHistory(id).subscribe(
            (res) => {
                this.approvalDetails = res;
                this.processDetails = this.approvalDetails.details;
                     console.log('this.approvalDetails ' + JSON.stringify(this.approvalDetails));
                     console.log('this.processDetails ' + JSON.stringify(this.processDetails));
                this.approvalsHistory = this.approvalDetails.approversList;
                for (let j = 0; j < this.approvalsHistory.length; j++) {
                    if (j + 1 != this.approvalsHistory.length) {
                        this.approvalsHistory[j]['next'] = true;
                    }
                }
                this.selectedBatchDetails = {};
                this.notificationBatches.forEach(element => {
                    if (element.id == id) {
                        this.selectedBatchDetails = element;
                        this.display = true;
                    }
                });
            });


    }

    viewDetailedReport(id: any) {
        let status = 'INPROCESS';
        this.page = 0;
        console.log('id ' + id);
        this.selectedBatchId = id;
        console.log('selectedBatchId ' + this.selectedBatchId);
        this.selectedBatchDetailsReports = [];
        this.notificationBatchService.getSelectedBatchDetails(id, this.page, this.itemsPerPage, status).subscribe(
            (res) => {
                this.selectedBatchDetailsReports = res.json();
                console.log('this.selectedBatchDetailsReports ' + JSON.stringify(this.selectedBatchDetailsReports));
                this.viewDetailRecordsLength = +res.headers.get('xcount');
                console.log('this.viewDetailRecordsLength ' + JSON.stringify(this.viewDetailRecordsLength));
                this.selectedBatchDetailsReports.forEach(element => {
                    let arr = [];
                    this.batchRecordsSrcTrgDetails.push(arr);
                });
                this.visibleSidebar5 = true;

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

    viewDetailedReports(status?: any) {

        this.page = 0;
        console.log('selectedBatchId ' + this.selectedBatchId);
        this.selectedBatchDetailsReports = [];
        this.notificationBatchService.getSelectedBatchDetails(this.selectedBatchId, this.page, this.itemsPerPage, status).subscribe(
            (res) => {
                this.selectedBatchDetailsReports = res.json();
                console.log('this.selectedBatchDetailsReports ' + JSON.stringify(this.selectedBatchDetailsReports));
                this.viewDetailRecordsLength = +res.headers.get('xcount');
                console.log('this.viewDetailRecordsLength ' + JSON.stringify(this.viewDetailRecordsLength));
                this.selectedBatchDetailsReports.forEach(element => {
                    let arr = [];
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
        this.notificationBatchService.getSelectedBatchDetails(this.selectedBatchId, this.page, this.itemsPerPage).subscribe(
            (res) => {
                this.selectedBatchDetailsReports = res.json();
                this.viewDetailRecordsLength = +res.headers.get('xcount');
                this.selectedBatchDetailsReports.splice(this.selectedBatchDetailsReports.length, 1);
                this.selectedBatchDetailsReports.forEach(element => {
                    let arr = [];
                    this.batchRecordsSrcTrgDetails.push(arr);
                });
            });
    }

    getDetailRecord(event) {
        let obj = this.selectedBatchDetailsReports[event.index].reconReference;
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

    ngOnDestroy() {
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
