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
/* import { Angular2Csv } from 'angular2-csv/Angular2-csv'; */
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-journals-header-data-wq',
    templateUrl: './journals-header-data-WQ.component.html'
})
export class JournalsHeaderDataWQComponent implements OnInit, OnDestroy {

    currentAccount: any;
    journalsHeaderData: JournalsHeaderData[];
    journalsHeaderDataView = new JournalsHeaderDataView();
    error: any;
    isViewOnly: boolean = false;
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
    selectedDateRange: any = [];
    isVisibleA: boolean = true;
    isCustomRange: boolean = false;
    rangeTo: any;
    journalsHeaderBatchDetails: any = [];
    journalslevelDetails: any = [];
    journalsViewColumns: any = [];
    journalsDrillDownViewColumns: any = [];
    journalLevel: any;
    selectedBatch: any;
    reverseType: any;
    visibleFullScreenBatchDetails:boolean = false;
    visibleFullScreenJEDetails:boolean = false;
    fileServerPath: any = 'http://192.168.0.44';
    jeTempExcelDownload = '/RECON/WQ/journalTemplate.xlsx';
    jeBatchTempExcelDownload = '/RECON/WQ/journalHeader.xlsx';
    /**Je Details View Dialog */
    jeDetailsViewDialog: boolean = false;
    jeDetailsViewList: any;
    tempBatchObj:any;
    dateRanges: any = [
        {
            "name": "Last 7 Days",
            "value": 7
        },
        {
            "name": "Last 15 Days",
            "value": 15
        },
        {
            "name": "Last 30 Days",
            "value": 30
        },
        {
            "name": "Custom",
            "value": "none"
        }

    ];
    journalsTableColumns = [
        /* { field: 'batchName', header: 'Batch Name' },
        { field: 'batchDate', header: 'Batch Date' }, */
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
    batchTableHeight:any;
    ngOnInit() {
        this.selectedDateRange = '7 Days';
        this.TemplatesHeight = (this.commonService.screensize().height - 390) + 'px';
        this.batchTableHeight = (this.commonService.screensize().height - 240) + 'px';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        /* this.journalsHeaderDataService.getJETempList().subscribe((res: any) => {
            this.journalsHeaderData = res;
            this.journalsHeaderData[0]['status'] = "No Batches for this template";
            console.log('journalsHeaderData List :' + JSON.stringify(this.journalsHeaderData));
        }); */
        this.templateDetailsService.journalsList().subscribe((res: any) => {
            this.journalsHeaderData = res.json();
            console.log('journalsHeaderData List :' + JSON.stringify(this.journalsHeaderData));
        }); 
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
        this.registerChangeInJournalsHeaderData();
    }

    /* FUNCTION: 1
       Author: AMIT

    */
    testT:boolean = false;
    noBatcheTemplate:boolean = false;
    selectedBatchName:any;
    fetchLedgerAndPeriodList(val?:any,id?: any,col?:any) {
        if(this.testT == false){    
            let count = 0;
            console.log('col ' + JSON.stringify(col));
            this.showJEDetails = false;
            console.log('val ' + val);
            this.selectedBatchName = val;
            this.periodsList = [];
            this.ledgersList = [];
            this.journalsHeaderBatchDetails = [];
            this.journalsHeaderDataView.jeLedger = '';
            this.journalsHeaderDataView.jePeriod = '';
            console.log('id ' + id);
            //this.noBatcheTemplate = false;
            this.journalsHeaderDataService.getLedgerAndPeriodList(id).subscribe((res: any) => {
                if(res.period == '' || res.ledgerNames == null){
                    this.notificationService.info('Info!', 'No Batches for this template');
                    this.noBatcheTemplate = true;
                }else{
                    this.periodsList = res.period;
                    this.ledgersList = res.ledgerNames;
                    this.journalsHeaderDataView.jeLedger = this.ledgersList[0];
                    this.journalsHeaderDataView.jePeriod = this.periodsList[0];  
                    if(this.callFromPostJE == true){
                        count++;
                        this.callSetInt = false;
                        this.callFromPostJE = false;
                        clearInterval(this.setIntId);
                        if(count <2){
                            this.testT = true;
                            this.fetchBatchDetailedList();
                        }
                        
                    }  
                }
                //    console.log('this.ledgersList ' + JSON.stringify(this.ledgersList[0])); 
            });
        }
    }

    fetchBatchDetailedList() {
        console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
        console.log('selectedDateRange ::' + JSON.stringify(this.selectedDateRange));
        this.showJEDetails = false;
        /* this.showJEDetails = false; */
        this.page1 = 0;
        this.journalslevelDetails = [];
        if (this.selectedDateRange == '7 Days') {
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

        }
        console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
        /* this.journalsHeaderDataView.startDate = this.journalsHeaderDataView.startDate+1; */
        this.journalsHeaderDataService.getJournalHeaderGroupedInfo(this.journalsHeaderDataView, this.page1, this.itemsPerPage1).subscribe((res: any) => {
            console.log('res journ ' + JSON.stringify(res));
            this.tempBatchObj = res.json();
            //this.journalsHeaderBatchDetails = res.map;
            this.journalsHeaderBatchDetails = res.json().map;
            this.journalsHeaderBatchLength = +res.headers.get('x-count');
            if (this.journalsHeaderBatchDetails.length > 0) {
                this.toggleSB();
            } else {
                this.notificationService.info('Info!', 'No Records found with in Date Range');
            }
            console.log('journalsHeaderBatchDetails List :' + JSON.stringify(this.journalsHeaderBatchDetails));
            console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationService.error('Internal Server Error!', 'Please contact system admin');
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
        this.journalsHeaderDataService.getJournalHeaderGroupedInfo(this.journalsHeaderDataView,this.page1, this.itemsPerPage1).subscribe((res: any) => {
            this.tempBatchObj = res.json();
            this.journalsHeaderBatchDetails = res.json().map;
            this.journalsHeaderBatchLength = +res.headers.get('x-count');
        });
    }

    /* get Journal Header Detail Or Summary Info*/
    showJEDetails: boolean = false;
    tempBatchId: any;
    tempLevel: any;
    journalslevelDetailsLength: any;
    fetchJournalHeaderDetailOrSummaryInfo(id: any) {
        this.journalslevelDetails = [];
        this.journalsViewColumns = [];
        console.log('id ' + id);
        this.tempBatchId = id;
        
        this.page = 0;
        this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            if (this.journalslevelDetails.jelineDetailsList.length > 0) {
                this.journalslevelDetailsLength = +res.headers.get('x-count');
                this.journalLevel = "summary";
                console.log('journalslevelDetails List :' + JSON.stringify(this.journalslevelDetails));
                this.journalsViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
                this.journalsDrillDownViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
                console.log('journalsViewColumns ' + JSON.stringify(this.journalsViewColumns));
                this.journalsViewColumns[0]['width'] = '65px';
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
                if(this.showJEDetails == true){
                    $('html, body').animate({
                        scrollTop: $("#myDiv").offset().top - 55
                    }, 2000);
                }
               
                console.log('this.columnOptions1 ' + JSON.stringify(this.columnOptions1));
            } else {
                this.notificationService.info('Info!', 'No Records found for selected batch');
                this.showReverseButton = false;
            }
        },
            (res: Response) => {
                this.onError(res.json()
                )
                this.notificationService.error('Internal Server Error!', 'Please contact system admin');
            });
    }

    fetchJournalLevel(val: any) {
        console.log('val ' + val);
        this.journalslevelDetails = [];
        this.journalsViewColumns = [];
        this.journalsDrillDownViewColumns = [];
        this.page = 0;
        this.tempLevel = val;
        this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage, this.tempLevel).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            this.journalslevelDetailsLength = +res.headers.get('x-count');
            this.journalLevel = val;
            console.log('journalslevelDetails List :' + JSON.stringify(this.journalslevelDetails));
            this.journalsViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
            this.journalsDrillDownViewColumns = this.journalslevelDetails.columnsWithAttributeNames;
            this.journalsViewColumns[0]['width'] = '65px';
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
        this.itemsPerPage = event.pageSize;
        this.journalsHeaderDataService.getJournalHeaderDetailOrSummaryInfo(this.tempBatchId, this.page + 1, this.itemsPerPage, this.tempLevel).subscribe((res: any) => {
            this.journalslevelDetails = res.json();
            this.journalslevelDetailsLength = +res.headers.get('x-count');
            this.journalLevel = this.tempLevel;
        });
    }

    

    reverseFunctionDetails: any;
    journalsHeaderBatchLength:any;
    reverseFunctionality() {
        console.log('reverseType ' + this.reverseType);
        console.log('this.journalsHeaderDataView ' + JSON.stringify(this.journalsHeaderDataView));
        this.journalsHeaderDataService.getReverseFunctionality(this.selectedBatch.id, this.reverseType).subscribe((res: any) => {
            this.reverseFunctionDetails = res;
            this.journalsHeaderBatchDetails = [];
            this.showJEDetails = false;
            this.showReverseButton = false;
            console.log('this.revfun ' + JSON.stringify(this.reverseFunctionDetails));
            this.journalsHeaderDataService.getJournalHeaderGroupedInfo(this.journalsHeaderDataView,this.page1, this.itemsPerPage1).subscribe((res: any) => {
                this.journalsHeaderBatchDetails = res.json().map;
                this.journalsHeaderBatchLength = +res.headers.get('x-count');
                console.log('journalsHeaderBatchDetails List :' + JSON.stringify(this.journalsHeaderBatchDetails));
                console.log('journalsHeaderDataView ::' + JSON.stringify(this.journalsHeaderDataView));
            });
        });

    }
    showReverseButton: boolean = false;
    onRowSelect(event) {
        console.log('event ' + JSON.stringify(event));
        this.fetchJournalHeaderDetailOrSummaryInfo(event.data.id);
        if(event.data.revDetails == null){
            this.showReverseButton = true;
        }else{
            this.showReverseButton = false;
        }
    }
    reverseBatch() {
        this.reverseType = 'switch';
        console.log('test ' + JSON.stringify(this.mySelectedRows));
        this.selectedBatch = this.mySelectedRows[0];
    }

    downloadJETemp() {
        console.log('batch id ' + this.tempBatchId + ' jourlev ' + this.journalLevel);
        this.journalsHeaderDataService.generateJQETempExcel(this.tempBatchId, this.journalLevel).subscribe((res: any) => {
            var link = document.createElement("a");
            link.download = 'journalTemplate';
            /* link.href = this.fileServerPath + this.jeTempExcelDownload; */
            console.log('fileServerPath ' + this.fileServerPath+res.destPath);
            link.href = this.fileServerPath + res.destPath;
            link.click();
        });
    }

    downloadJEBatchTemp() {
        console.log('this.selectedBatchName ' + this.selectedBatchName);
        console.log('this.journalsHeaderDataView ' + JSON.stringify(this.journalsHeaderDataView));
        this.journalsHeaderDataService.generateJournalBatchExcel(this.journalsHeaderDataView,this.selectedBatchName).subscribe((res: any) => {
            console.log('res ' +JSON.stringify(res));
            var link = document.createElement("a");
            link.download = 'journalHeader';
            console.log('this.fileServerPath ' + this.fileServerPath + ' this.jeBatchTempExcelDownload ' + this.jeBatchTempExcelDownload);
            console.log('fileServerPath ' + this.fileServerPath+res.destPath);
            link.href = this.fileServerPath + res.destPath;
            /* link.href = this.fileServerPath + this.jeBatchTempExcelDownload; */
            link.click();
        });
    }

    

    /* journalHeader */

    showJeLineDetails(jeLines) {
        let page = 1;
        let size = 20;
        console.log('jeLines\n'+JSON.stringify(jeLines));
        this.journalsHeaderDataService.getJeLineDetailsViews(this.tempBatchId,page,size,jeLines).subscribe((res)=>{
            this.jeDetailsViewList = res;
            console.log('journalsDrillDownViewColumns ' + JSON.stringify(this.journalsDrillDownViewColumns));
            console.log('columnOptions2 ' + JSON.stringify(this.columnOptions2));
            console.log('jeDetailsViewList\n'+JSON.stringify(this.jeDetailsViewList));
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
    
    viewFullScreen(){
        this.mySelectedRows3 = [];
        console.log('visibleFullScreenBatchDetails ' + this.visibleFullScreenBatchDetails);
        if(this.visibleFullScreenBatchDetails == false){
            this.visibleFullScreenBatchDetails = true;
        }else if(this.visibleFullScreenBatchDetails == true){
            this.visibleFullScreenBatchDetails = false;
        }
    }

    viewFullScreenJEDetails(){
        console.log('visibleFullScreenJEDetails ' + this.visibleFullScreenJEDetails);
        if(this.visibleFullScreenJEDetails == false){
            this.visibleFullScreenJEDetails = true;
        }else if(this.visibleFullScreenJEDetails == true){
            this.visibleFullScreenJEDetails = false;
        }
    }

    clearAllData(){
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
        console.log('called ' + $table);
                var $headers = $table.find('tr:has(th)')
                    ,$rows = $table.find('tr:has(td)')

                    // Temporary delimiter characters unlikely to be typed by keyboard
                    // This is to avoid accidentally splitting the actual contents
                    ,tmpColDelim = String.fromCharCode(11) // vertical tab character
                    ,tmpRowDelim = String.fromCharCode(0) // null character

                    // actual delimiter characters for CSV format
                    ,colDelim = '","'
                    ,rowDelim = '"\r\n"';

                    // Grab text from table into CSV formatted string
                    var csv = '"';
                    csv += formatRows($headers.map(grabRow));
                    csv += rowDelim;
                    csv += formatRows($rows.map(grabRow)) + '"';

                    // Data URI
                    var csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

                $(this)
                    .attr({
                    'download': filename
                        ,'href': csvData
                        //,'target' : '_blank' //if you want it to open in a new window
                });

                //------------------------------------------------------------
                // Helper Functions 
                //------------------------------------------------------------
                // Format the output so it has the appropriate delimiters
                function formatRows(rows){
                    return rows.get().join(tmpRowDelim)
                        .split(tmpRowDelim).join(rowDelim)
                        .split(tmpColDelim).join(colDelim);
                }
                // Grab and format a row from the table
                function grabRow(i,row){
                     
                    var $row = $(row);
                    //for some reason $cols = $row.find('td') || $row.find('th') won't work...
                    var $cols = $row.find('td'); 
                    if(!$cols.length) $cols = $row.find('th');  

                    return $cols.map(grabCol)
                                .get().join(tmpColDelim);
                }
                // Grab and format a column from the table 
                function grabCol(j,col){
                    var $col = $(col),
                        $text = $col.text();

                    return $text.replace('"', '""'); // escape double quotes

                }
            }
    //*ngIf="journalsHeaderBatchDetails.length"
    setIntId: any;
    callSetInt: boolean = false;
    callFromPostJE:boolean = false;
    postJETemp(){
        console.log('selected JE ' + this.journalsHeaderDataView.jeTempId);
        let obj = {
            "param1": this.journalsHeaderDataView.jeTempId
        }
        this.journalsHeaderDataService.postJEJob(obj).subscribe((res)=>{
            console.log('res ' + res.status + ' j '+ JSON.stringify(res));
            if(res.status == 'Failed to intiate job'){
                this.notificationService.error(
                    'Warning!',
                    'Data type of "CURRENCY" qualifier should be "VARCHAR"'
                )
            }else{
                this.callSetInt = true;
                this.setIntId = setInterval(() => {
                    console.log('inside SetInterval ');
                    if (this.callSetInt == true) {
                        console.log('calling Job status api ' + res.status);
                        this.jobDetailsService.oozieeJobStatus(res.status).subscribe((res: any) => {
                            //this.acctJobStatus = res;
                            console.log('acct job status:' + JSON.stringify(res._body));
                            console.log('this.setIntId ' + this.setIntId);
                            if (res._body == 'SUCCEEDED') {
                                console.log('this.setIntId ' + this.setIntId);
                                if (this.setIntId) {
                                    this.callFromPostJE = true;
                                    clearInterval(this.setIntId);
                                    this.fetchLedgerAndPeriodList(this.selectedBatchName,this.journalsHeaderDataView.jeTempId);
                                    
                                }
                            }else if (res._body == 'KILLED') {
                                console.log('this.setIntId ' + this.setIntId);
                                if (this.setIntId) {
                                    clearInterval(this.setIntId);
                                    this.notificationService.error(
                                        'Warning!',
                                        'Job Killed'
                                    )
                                }
                            }else if (res._body == 'FAILED') {
                                console.log('this.setIntId ' + this.setIntId);
                                if (this.setIntId) {
                                    clearInterval(this.setIntId);
                                    this.notificationService.error(
                                        'Warning!',
                                        'Job Failed'
                                    )
                                }
                            }/*  else {
                                console.log('calling load all:');
                                //this.loadAll();
                                this.fetchLedgerAndPeriodList(this.selectedBatchName,this.journalsHeaderDataView.jeTempId);
                            } */
                        },
                            (res: Response) => {
                                this.onError(res.json())
                                this.notificationService.error('Error!', 'Error while checking job status!');
                                }
                            )
                    } else {
                        clearInterval(this.setIntId);
                    }
                }, 5000);

                this.notificationService.success(
                    'Success!',
                    'JE Posted Successfully'
                )
            }
        });
    }

    rePostBatch(){
        console.log('this.tempBatchId ' + this.tempBatchId);
        this.postJETemp();
    }

    ngOnDestroy() {
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
