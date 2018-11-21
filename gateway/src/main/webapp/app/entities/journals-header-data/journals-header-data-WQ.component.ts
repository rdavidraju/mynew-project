import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { JournalsHeaderData, JournalsHeaderDataView } from './journals-header-data.model';
import { JournalsHeaderDataService } from './journals-header-data.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
import { CommonService } from '../common.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JhiDateUtils } from 'ng-jhipster';
import { NotificationsService } from 'angular2-notifications-lite';
import { SidebarModules } from '../../shared/primeng/primeng';
import { TemplateDetailsService } from '../template-details/template-details.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { DecimalPipe } from '@angular/common';
import { saveAs as importedSaveAs } from "file-saver";
import { trigger, state, style, animate, transition } from '@angular/animations';
import 'moment';
/* import { Angular2Csv } from 'angular2-csv/Angular2-csv'; */
//import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver/FileSaver';
declare var $: any;
declare var jQuery: any;
declare var moment: any;

@Component({
    selector: 'jhi-journals-header-data-wq',
    templateUrl: './journals-header-data-WQ.component.html',
    animations: [
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
export class JournalsHeaderDataWQComponent implements OnInit, OnDestroy {

    currentAccount: any;
    maxDate: Date;
    minDate: Date;
    journalsHeaderData: JournalsHeaderData[];
    journalsHeaderDataView = new JournalsHeaderDataView();
    error: any;
    isViewOnly = false;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    itemsPerPage1: any;
    pageSizeOptions = [10, 25, 50, 100];
    page: any;
    page1: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    columnOptions: SelectItem[];
    columnOptions1: SelectItem[];
    columnOptions2: SelectItem[];
    TemplatesHeight: any;
    mySelectedRows = [];
    mySelectedRows1 = [];
    mySelectedRows2 = [];
    mySelectedRows3 = [];
    periodsList: any = [];
    ledgersList: any = [];
    /* selectedDateRange: any = []; */
    isVisibleA = true;
    isCustomRange = false;
    rangeTo: any;
    journalsHeaderBatchDetails: any = [];
    journalslevelDetails: any = [];
    journalsViewColumns: any = [];
    journalsDrillDownViewColumns: any = [];
    journalLevel: any;
    selectedBatch: any;
    reverseType: any;
    visibleFullScreenBatchDetails = false;
    visibleFullScreenJEDetails = false;
    batchTableHeight: any;
    testT = false;
    noBatcheTemplate = false;
    selectedBatchName: any;
    showJEDetails = false;
    tempBatchId: any;
    tempLevel: any;
    journalslevelDetailsLength: any;
    showReverseButton = false;
    selectedTempName: any;
    reverseFunctionDetails: any;
    journalsHeaderBatchLength: any;
    selectedMoreRange = false;
    dateDifference: any;
    isStartDateChanged = false;
    isEndDateChanged = false;
    setIntId: any;
    callSetInt = false;
    callFromPostJE = false;
    jeTempIdSearch: any;
    jeLedgerSearch: any;
    jePeriodSearch: any;
    //  fileServerPath: any = 'http://192.168.0.44';
    jeTempExcelDownload = '/RECON/WQ/journalTemplate.xlsx';
    jeBatchTempExcelDownload = '/RECON/WQ/journalHeader.xlsx';
    /**Je Details View Dialog */
    jeDetailsViewDialog = false;
    jeDetailsViewList: any;
    tempBatchObj: any;
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
    journalsTableColumns = [
        //field="templateName" header="Template Name"
        { field: 'templateName', header: 'Template Name', width: '150px', align: 'left' },
        { field: 'batchName', header: 'Batch Name', width: '250px', align: 'left' },
        { field: 'batchDate', header: 'Batch Date', width: '110px', align: 'left' },
        { field: 'glDate', header: 'GL Date', width: '110px', align: 'left' },
        //field="glDate" header="GL Date"
        { field: 'ledger', header: 'Ledger', width: '190px', align: 'left' },
        { field: 'period', header: 'Period', width: '90px', align: 'left' },
        { field: 'source', header: 'Source', width: '160px', align: 'left' },
        { field: 'category', header: 'Category', width: '160px', align: 'left' },
        { field: 'drAmount', header: 'Dr Amount', width: '150px', align: 'right' },
        { field: 'crAmount', header: 'Cr Amount', width: '150px', align: 'right' },
        { field: 'reference', header: 'Reference', width: '150px', align: 'left' }
    ];

    constructor(
        private journalsHeaderDataService: JournalsHeaderDataService,
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
        private datePipe: DatePipe,
        private dateUtils: JhiDateUtils,
        private notificationService: NotificationsService,
        private templateDetailsService: TemplateDetailsService,
        private jobDetailsService: JobDetailsService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.columnOptions = [];
        for (let i = 0; i < this.journalsTableColumns.length; i++) {
            this.columnOptions.push({ label: this.journalsTableColumns[i].header, value: this.journalsTableColumns[i] });
        }
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.itemsPerPage1 = ITEMS_PER_PAGE;
    }
    getmindate() {
        this.minDate = this.journalsHeaderDataView.startDate;
    }
    changeMinDate() {
        this.config.minDate = this.journalsHeaderDataView.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/journals-header-data', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/journals-header-data', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    
    ngOnInit() {
        /* this.selectedDateRange == 'This Month'; */
        const selectedRange = [moment().startOf('month'), moment()];
        this.journalsHeaderDataView.startDate = new Date(selectedRange[0]);
        this.journalsHeaderDataView.endDate = new Date(selectedRange[1]);
        this.TemplatesHeight = (this.commonService.screensize().height - 390) + 'px';
        this.batchTableHeight = (this.commonService.screensize().height - 240) + 'px';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        /* this.journalsHeaderDataService.getJETempList().subscribe((res: any) => {
            this.journalsHeaderData = res;
            this.journalsHeaderData[0]['status'] = "No Batches for this template";
            //console.log('journalsHeaderData List :' + JSON.stringify(this.journalsHeaderData));
        }); */
        this.templateDetailsService.journalsList().subscribe((res: any) => {
            this.journalsHeaderData = res.json();
            //console.log('journalsHeaderData List :' + JSON.stringify(this.journalsHeaderData));
        });
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
        this.registerChangeInJournalsHeaderData();
    }

    /* FUNCTION: 1
       Author: AMIT

    */
    fetchLedgerAndPeriodList(val?: any, id?: any, col?: any) {
        if (this.testT == false) {
            let count = 0;
            //console.log('col ' + JSON.stringify(col));
            this.showJEDetails = false;
            //console.log('val ' + val);
            this.selectedBatchName = val;
            this.periodsList = [];
            this.ledgersList = [];
            this.journalsHeaderBatchDetails = [];
            this.journalsHeaderDataView.jeLedger = '';
            this.journalsHeaderDataView.jePeriod = '';
            //console.log('id ' + id);
            //this.noBatcheTemplate = false;
            this.journalsHeaderDataService.getLedgerAndPeriodList(id).subscribe((res: any) => {
                if (res.period == '' || res.ledgerNames == null) {
                    this.commonService.info('Info!', 'No Batches for this template');
                    this.noBatcheTemplate = true;
                    this.journalsHeaderDataView.jeLedger = "none";
                    this.journalsHeaderDataView.jePeriod = "none";
                } else {
                    this.periodsList = res.period;
                    this.ledgersList = res.ledgerNames;
                    if (this.ledgersList && this.ledgersList.length > 1) {
                        this.journalsHeaderDataView.jeLedger = "ALL";
                    } else if (this.ledgersList && this.ledgersList.length == 1) {
                        this.journalsHeaderDataView.jeLedger = this.ledgersList[0];
                    }
                    if (this.periodsList && this.periodsList.length > 1) {
                        this.journalsHeaderDataView.jePeriod = "ALL";
                    } else if (this.periodsList && this.periodsList.length == 1) {
                        this.journalsHeaderDataView.jePeriod = this.periodsList[0];
                    }
                    //this.journalsHeaderDataView.jePeriod = this.periodsList[0];  
                    if (this.callFromPostJE == true) {
                        count++;
                        this.callSetInt = false;
                        this.callFromPostJE = false;
                        clearInterval(this.setIntId);
                        if (count < 2) {
                            this.testT = true;
                            this.fetchBatchDetailedList();
                        }

                    }
                    this.fetchBatchDetailedList();
                }
                //    //console.log('this.ledgersList ' + JSON.stringify(this.ledgersList[0])); 
            });
        }
    }

    fetchBatchDetailedList() {
        //console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
        //console.log('selectedDateRange ::' + JSON.stringify(this.selectedDateRange));
        this.showJEDetails = false;
        /* this.showJEDetails = false; */
        this.page1 = 0;
        this.journalslevelDetails = [];

        /* if (this.selectedDateRange == '7 Days') {
            this.journalsHeaderDataView.startDate = new Date(Date.now());
            this.journalsHeaderDataView.startDate.setDate(this.journalsHeaderDataView.startDate.getDate() - 7);
            this.journalsHeaderDataView.endDate = new Date(Date.now());
        } else if (this.selectedDateRange == '15 Days') {
            this.journalsHeaderDataView.startDate = new Date(Date.now());
            this.journalsHeaderDataView.startDate.setDate(this.journalsHeaderDataView.startDate.getDate() - 15);
            this.journalsHeaderDataView.endDate = new Date(Date.now());
        } else if (this.selectedDateRange == '30 Days') {
            this.journalsHeaderDataView.startDate = new Date(Date.now());
            this.journalsHeaderDataView.startDate.setDate(this.journalsHeaderDataView.startDate.getDate() - 30);
            this.journalsHeaderDataView.endDate = new Date(Date.now());
        } else {

        } */
        //console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
        /* this.journalsHeaderDataView.startDate = this.journalsHeaderDataView.startDate+1; */
        this.journalsHeaderDataService.getJournalHeaderGroupedInfo(this.journalsHeaderDataView, this.page1, this.itemsPerPage1).subscribe((res: any) => {
            //console.log('res journ ' + JSON.stringify(res));
            this.tempBatchObj = res.json();
            //this.journalsHeaderBatchDetails = res.map;
            this.journalsHeaderBatchDetails = res.json().map;
            this.journalsHeaderBatchLength = +res.headers.get('x-count');
            if (this.journalsHeaderBatchDetails.length > 0) {
                this.toggleSB();
            } else {
                this.commonService.info('Info!', 'No Records found with in Date Range');
            }
            //console.log('journalsHeaderBatchDetails List :' + JSON.stringify(this.journalsHeaderBatchDetails));
            //console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
        },
            (res: Response) => {
                this.onError(res.json()
                )
                //this.commonService.error('Internal Server Error!', 'Please contact system admin');
            });

    }
    onPaginateChange1(event) {
        this.page1 = event.pageIndex;
        this.itemsPerPage1 = event.pageSize;
        /* this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage, this.tempLevel).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            this.journalslevelDetailsLength = +res.headers.get('x-count');
            this.journalLevel = this.tempLevel;
        }); */
        this.journalsHeaderDataService.getJournalHeaderGroupedInfo(this.journalsHeaderDataView, this.page1, this.itemsPerPage1).subscribe((res: any) => {
            this.tempBatchObj = res.json();
            this.journalsHeaderBatchDetails = res.json().map;
            this.journalsHeaderBatchLength = +res.headers.get('x-count');
        });
    }

    /* get Journal Header Detail Or Summary Info*/
    fetchJournalHeaderDetailOrSummaryInfo(id: any) {
        this.journalslevelDetails = [];
        this.journalsViewColumns = [];
        //console.log('id ' + id);
        this.tempBatchId = id;

        this.page = 0;
        this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            if (this.journalslevelDetails.jelineDetailsList.length > 0) {
                this.journalslevelDetailsLength = +res.headers.get('x-count');
                this.journalLevel = "summary";
                //console.log('journalslevelDetails List :' + JSON.stringify(this.journalslevelDetails));
                this.journalsViewColumns = this.journalslevelDetails.columnsWithAttributeNames;

                this.journalsDrillDownViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
                const tempLength = [];
                let tempWidth
                this.journalslevelDetails.jelineDetailsList.forEach((element, i) => {
                    const temp = ((element.codeCombination).length) * 10;
                    //tempLength.push(temp);
                    tempWidth = temp.toString().concat('px');
                    //console.log('temp length ' + tempWidth);
                });
                //console.log('journalsViewColumns ' + JSON.stringify(this.journalsViewColumns));
                this.journalsViewColumns[0]['width'] = '65px';
                this.journalsViewColumns[1]['width'] = tempWidth;
                /* this.journalsViewColumns = [
                    { field: 'codeCombination', header: 'Code Combination' },
                    { field: 'enteredCurrency', header: 'Currency' },
                    { field: 'accountedDebit', header: 'Accounted Debit' },
                    { field: 'accountedCredit', header: 'Accounted Credit' }
                ]; */
                this.columnOptions1 = [];
                this.columnOptions2 = [];
                for (let i = 0; i < this.journalsViewColumns.length; i++) {
                    /* this.journalsViewColumns[i]['align'] = 'right';
                    this.journalsViewColumns[0]['align'] = 'center'; */
                    this.columnOptions1.push({ label: this.journalsViewColumns[i].header, value: this.journalsViewColumns[i] });
                    this.columnOptions2.push({ label: this.journalsViewColumns[i].header, value: this.journalsViewColumns[i] });
                }
                this.showJEDetails = true;
                if (this.showJEDetails == true) {
                    $('html, body').animate({
                        scrollTop: $("#myDiv").offset().top - 55
                    }, 2000);
                    if(this.visibleFullScreenBatchDetails){
                        $('.jeHeadFullScr').animate({
                            scrollTop: $("#myDivFullScr").offset().top - 55
                        }, 2000);
                    }
                }

                //console.log('this.columnOptions1 ' + JSON.stringify(this.columnOptions1));
            } else {
                this.commonService.info('Info!', 'No Records found for selected batch');
                this.showReverseButton = false;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
               // this.commonService.error('Internal Server Error!', 'Please contact system admin');
            });
    }

    fetchJournalLevel(val: any) {
        //console.log('val ' + val);
        this.journalslevelDetails = [];
        this.journalsViewColumns = [];
        this.journalsDrillDownViewColumns = [];
        this.page = 0;
        this.tempLevel = val;
        this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage, this.tempLevel).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            this.journalslevelDetailsLength = +res.headers.get('x-count');
            this.journalLevel = val;
            //console.log('journalslevelDetails List :' + JSON.stringify(this.journalslevelDetails));
            this.journalsViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
            this.journalsDrillDownViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
            //this.journalsViewColumns[0]['width'] = '65px';
            this.columnOptions1 = [];
            for (let i = 0; i < this.journalsViewColumns.length; i++) {
                /* this.journalsViewColumns[i]['align'] = 'right';
                this.journalsViewColumns[0]['align'] = 'center'; */
                this.columnOptions1.push({ label: this.journalsViewColumns[i].header, value: this.journalsViewColumns[i] });
                this.columnOptions2.push({ label: this.journalsViewColumns[i].header, value: this.journalsViewColumns[i] });
            }
            this.showJEDetails = true;
        });
    }
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.journalLevel = this.tempLevel;
        this.itemsPerPage = event.pageSize;
        this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage, this.tempLevel).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            this.journalslevelDetailsLength = +res.headers.get('x-count');
            this.journalLevel = this.tempLevel;
        });
    }

    reverseFunctionality() {
        //console.log('reverseType ' + this.reverseType);
        //console.log('this.journalsHeaderDataView ' + JSON.stringify(this.journalsHeaderDataView));
        this.journalsHeaderDataService.getReverseFunctionality(this.selectedBatch.id, this.reverseType).subscribe((res: any) => {
            this.reverseFunctionDetails = res;
            this.journalsHeaderBatchDetails = [];
            this.showJEDetails = false;
            this.showReverseButton = false;
            //console.log('this.revfun ' + JSON.stringify(this.reverseFunctionDetails));
            this.journalsHeaderDataService.getJournalHeaderGroupedInfo(this.journalsHeaderDataView, this.page1, this.itemsPerPage1).subscribe((res1: any) => {
                this.journalsHeaderBatchDetails = res1.json().map;
                this.journalsHeaderBatchLength = +res1.headers.get('x-count');
                //console.log('journalsHeaderBatchDetails List :' + JSON.stringify(this.journalsHeaderBatchDetails));
                //console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
            });
        });

    }
    
    onRowSelect(event) {
        // console.log('event ' + JSON.stringify(event));
        this.selectedTempName = event.data.templateName;
        this.fetchJournalHeaderDetailOrSummaryInfo(event.data.id);
        if (event.data.revDetails == null) {
            this.showReverseButton = true;
        } else {
            this.showReverseButton = false;
        }
    }
    reverseBatch() {
        this.reverseType = 'switch';
        console.log('test ' + JSON.stringify(this.mySelectedRows));
        this.selectedBatch = this.mySelectedRows;
    }

    downloadJETemp(type) {
        // //console.log('batch id ' + this.tempBatchId + ' jourlev ' + this.journalLevel);
        this.journalsHeaderDataService.generateJQETempExcel(this.tempBatchId, type, this.journalLevel).subscribe(
            (res) => { this.commonService.exportData(res, type, 'JE Batch' + new Date()); },
            (err) => { this.commonService.error('Warning!', 'Error Occured'); }
        );
    }

    downloadJEBatchTemp(type) {
        // //console.log('this.selectedBatchName ' + this.selectedBatchName);
        // //console.log('this.journalsHeaderDataView ' + JSON.stringify(this.journalsHeaderDataView));
        this.journalsHeaderDataService.generateJournalBatchExcel(this.journalsHeaderDataView, this.selectedBatchName, type).subscribe(
            (res) => {
                /* var a = document.createElement("a");
                document.body.appendChild(a);
                let url = window.URL.createObjectURL(res.blob());
                a.href = url;
                a.download = "myFile.xls";
                a.click();
                window.URL.revokeObjectURL(url); */
                this.commonService.exportData(res, type, this.selectedBatchName+new Date()); 
            },
            (err) => { this.commonService.error('Warning!', 'Error Occured'); }
        );
    }

    /* toggleRange(e) {
        this.selectedMoreRange = false;
        let selectedRange: any;
        this.selectedDateRange = e.value;
        if (this.selectedDateRange == 'Today') {
            selectedRange = [moment(), moment()];
        } else if (this.selectedDateRange == 'Last 7 Days') {
            selectedRange = [moment().subtract(6, 'days'), moment()];
        } else if (this.selectedDateRange == 'Last 14 Days') {
            selectedRange = [moment().subtract(13, 'days'), moment()];
        } else if (this.selectedDateRange == 'Last 30 Days') {
            selectedRange = [moment().subtract(29, 'days'), moment()];
        } else if (this.selectedDateRange == 'Last 2Months') {
            selectedRange = [moment().subtract(1, 'month').startOf('month'), moment()];
        } else if (this.selectedDateRange == 'Last 3Months') {
            selectedRange = [moment().subtract(2, 'month').startOf('month'), moment()];
        } else if (this.selectedDateRange == 'Last 6Months') {
            selectedRange = [moment().subtract(5, 'month').startOf('month'), moment()];
        } else if (this.selectedDateRange == 'custom') {
            this.selectedMoreRange = true;
        } else if (this.selectedDateRange == 'Yesterday') {
            selectedRange = [moment().subtract(1, 'days'), moment().subtract(1, 'days')];
        } else if (this.selectedDateRange == 'This Month') {
            selectedRange = [moment().startOf('month'), moment()];
        }
        if (this.selectedDateRange != 'custom') {
            this.journalsHeaderDataView.startDate = new Date(selectedRange[0]);
            this.journalsHeaderDataView.endDate = new Date(selectedRange[1]);
            //this.loadall();
        }
        if (e.value == 'custom') {
            this.isCustomRange = true;
        } else {
            this.isCustomRange = false;
        }
    } */
    
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
            this.journalsHeaderDataView.startDate = new Date(selectedRange[0]);
            this.journalsHeaderDataView.endDate = new Date(selectedRange[1]);
            this.fetchBatchDetailedList();
        }
    }

    changeDateRange() {
        for (let i = 0; i < this.selectedDateRangeOption.length; i++) {
            this.selectedDateRangeOption[i]['checked'] = false;
            if (this.selectedDateRangeOption[i].type == "custom") {
                this.selectedDateRangeOption.splice(i, 1);
            }
        }
        const oneDay = 24 * 60 * 60 * 1000;
        let selectedRange: any;
        this.dateDifference = Math.round(Math.abs((this.journalsHeaderDataView.startDate.getTime() - this.journalsHeaderDataView.endDate.getTime()) / (oneDay)));
        if (this.dateDifference == 1) {
            selectedRange = [moment(), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Today')].checked = true;
        } else if (this.dateDifference == 7) {
            selectedRange = [moment().subtract(7, 'days'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Last 7 Days')].checked = true;
        } else if (this.dateDifference == 14) {
            selectedRange = [moment().subtract(14, 'days'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Last 14 Days')].checked = true;
        } else if (this.dateDifference == 30) {
            selectedRange = [moment().subtract(30, 'days'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Last 30 Days')].checked = true;
        } else if (this.dateDifference == 60) {
            selectedRange = [moment().subtract(1, 'month').startOf('month'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Last 2Months')].checked = true;
        } else if (this.dateDifference == 90) {
            selectedRange = [moment().subtract(2, 'month').startOf('month'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Last 3Months')].checked = true;
        } else if (this.dateDifference == 180) {
            selectedRange = [moment().subtract(5, 'month').startOf('month'), moment()];
            this.selectedDateRangeOption[this.selectedDateRangeOption.findIndex((x) => x.value == 'Last 6Months')].checked = true;
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
        this.journalsHeaderDataView.startDate = new Date(selectedRange[0]);
        this.journalsHeaderDataView.endDate = new Date(selectedRange[1]);
        this.fetchBatchDetailedList();
    }

    /* journalHeader */

    showJeLineDetails(jeLines) {
        const page = 1;
        const size = 20;
        //console.log('jeLines\n'+JSON.stringify(jeLines));
        this.journalsHeaderDataService.getJeLineDetailsViews(this.tempBatchId, page, size, jeLines).subscribe((res) => {
            this.jeDetailsViewList = res;
            //console.log('journalsDrillDownViewColumns ' + JSON.stringify(this.journalsDrillDownViewColumns));
            //console.log('columnOptions2 ' + JSON.stringify(this.columnOptions2));
            //console.log('jeDetailsViewList\n'+JSON.stringify(this.jeDetailsViewList));
            this.jeDetailsViewDialog = true;
        });
    }
    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
            $('#queryBlock').removeClass('jeQueryBlock1').addClass('jeQueryBlock');
            //$('.reconcile-main .upper-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('#queryBlock').removeClass('jeQueryBlock').addClass('jeQueryBlock1');
            //$('.reconcile-main .upper-component').addClass('visible');
        }
    }

    viewFullScreen() {
        this.mySelectedRows3 = [];
        //console.log('visibleFullScreenBatchDetails ' + this.visibleFullScreenBatchDetails);
        if (this.visibleFullScreenBatchDetails == false) {
            this.visibleFullScreenBatchDetails = true;
        } else if (this.visibleFullScreenBatchDetails == true) {
            this.visibleFullScreenBatchDetails = false;
        }
    }

    viewFullScreenJEDetails() {
        //console.log('visibleFullScreenJEDetails ' + this.visibleFullScreenJEDetails);
        if (this.visibleFullScreenJEDetails == false) {
            this.visibleFullScreenJEDetails = true;
        } else if (this.visibleFullScreenJEDetails == true) {
            this.visibleFullScreenJEDetails = false;
        }
    }

    clearAllData() {
        /*$("#example1").click(function(e) {
            window.open('data:application/vnd.ms-excel,' + $('#dvData').html());
            e.preventDefault();
        });*/
        /* new Angular2Csv(this.journalsHeaderBatchDetails, 'My Report'); */
        /* $('#example1').table2CSV({header:['prefix','Employee Name','Contact']}); */
        /*  var uri='data:application/vnd.ms-excel;base64,',
         template='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
         base64=function(s){return Window.arguments(unescape(encodeURIComponent(s)));},
         format=function(s,c){return s.replace(/{(\w+)}/g,function(m,p){return c[p];})};
         var table=$('#example1');
         var ctx={worksheet:'test',table:table.html()},
                 href=uri+base64(format(template,ctx)); */
        /* var outputFile = 'export'
       this.exportTableToCSV.apply(this, [$('#example1>table'), outputFile]);  */
        this.journalsHeaderBatchDetails = [];
        this.journalsHeaderDataView.jeTempId = null;
        this.journalsHeaderDataView.jeLedger = null;
        this.journalsHeaderDataView.jePeriod = null;
        this.showJEDetails = false;
        this.showJEDetails = false;
    }
    exportTableToCSV($table, filename) {
        //console.log('called ' + $table);
        const $headers = $table.find('tr:has(th)')
            , $rows = $table.find('tr:has(td)')

            // Temporary delimiter characters unlikely to be typed by keyboard
            // This is to avoid accidentally splitting the actual contents
            , tmpColDelim = String.fromCharCode(11) // vertical tab character
            , tmpRowDelim = String.fromCharCode(0) // null character

            // actual delimiter characters for CSV format
            , colDelim = '","'
            , rowDelim = '"\r\n"';

        // Grab text from table into CSV formatted string
        let csv = '"';
        csv += formatRows($headers.map(grabRow));
        csv += rowDelim;
        csv += formatRows($rows.map(grabRow)) + '"';

        // Data URI
        const csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

        $(this)
            .attr({
                'download': filename
                , 'href': csvData
                //,'target' : '_blank' //if you want it to open in a new window
            });

        //------------------------------------------------------------
        // Helper Functions 
        //------------------------------------------------------------
        // Format the output so it has the appropriate delimiters
        function formatRows(rows) {
            return rows.get().join(tmpRowDelim)
                .split(tmpRowDelim).join(rowDelim)
                .split(tmpColDelim).join(colDelim);
        }
        // Grab and format a row from the table
        function grabRow(i, row) {

            const $row = $(row);
            //for some reason $cols = $row.find('td') || $row.find('th') won't work...
            let $cols = $row.find('td');
            if (!$cols.length) $cols = $row.find('th');

            return $cols.map(grabCol)
                .get().join(tmpColDelim);
        }
        // Grab and format a column from the table 
        function grabCol(j, col) {
            const $col = $(col),
                $text = $col.text();

            return $text.replace('"', '""'); // escape double quotes

        }
    }
    postJETemp() {
        //console.log('selected JE ' + this.journalsHeaderDataView.jeTempId);
        const obj = {
            "param1": this.journalsHeaderDataView.jeTempId
        }
        this.journalsHeaderDataService.postJEJob(obj).subscribe((res1) => {
            //console.log('res ' + res.status + ' j '+ JSON.stringify(res));
            if (res1.status == 'Failed to intiate job') {
                this.commonService.error(
                    'Warning!',
                    'Data type of "CURRENCY" qualifier should be "VARCHAR"'
                )
            } else {
                this.callSetInt = true;
                this.setIntId = setInterval(() => {
                    //console.log('inside SetInterval ');
                    if (this.callSetInt == true) {
                        //console.log('calling Job status api ' + res.status);
                        this.jobDetailsService.oozieeJobStatus(res1.status).subscribe((res: any) => {
                            //this.acctJobStatus = res;
                            //console.log('acct job status:' + JSON.stringify(res._body));
                            //console.log('this.setIntId ' + this.setIntId);
                            if (res._body == 'SUCCEEDED') {
                                //console.log('this.setIntId ' + this.setIntId);
                                if (this.setIntId) {
                                    this.callFromPostJE = true;
                                    clearInterval(this.setIntId);
                                    this.fetchLedgerAndPeriodList(this.selectedBatchName, this.journalsHeaderDataView.jeTempId);

                                }
                            } else if (res._body == 'KILLED') {
                                //console.log('this.setIntId ' + this.setIntId);
                                if (this.setIntId) {
                                    clearInterval(this.setIntId);
                                    this.commonService.error(
                                        'Warning!',
                                        'Job Killed'
                                    )
                                }
                            } else if (res._body == 'FAILED') {
                                //console.log('this.setIntId ' + this.setIntId);
                                if (this.setIntId) {
                                    clearInterval(this.setIntId);
                                    this.commonService.error(
                                        'Warning!',
                                        'Job Failed'
                                    )
                                }
                            }/*  else {
                                //console.log('calling load all:');
                                //this.loadAll();
                                this.fetchLedgerAndPeriodList(this.selectedBatchName,this.journalsHeaderDataView.jeTempId);
                            } */
                        },
                            (res: Response) => {
                                this.onError(res.json())
                                this.commonService.error('Error!', 'Error while checking job status!');
                            }
                        )
                    } else {
                        clearInterval(this.setIntId);
                    }
                }, 5000);

                this.commonService.success(
                    'Success!',
                    'JE Posted Successfully'
                )
            }
        });
    }

    rePostBatch() {
        //console.log('this.tempBatchId ' + this.tempBatchId);
        this.postJETemp();
    }

    ngOnDestroy() {
        this.commonService.destroyNotification(); 
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: JournalsHeaderData) {
        return item.id;
    }
    registerChangeInJournalsHeaderData() {
        this.eventSubscriber = this.eventManager.subscribe('journalsHeaderDataListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.journalsHeaderData = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    loadAll() {

    }
}
