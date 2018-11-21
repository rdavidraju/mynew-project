/* 
Author : Bhagath
Functions List : 
  1. On Load function calling - LINE 237
  2. Notification Message Popup Function - LINE 237
  3. Fetching Accounting Rule Groups - LINE 237
  4. Fetch Ledger Definitions by COA - LINE 237
*/


import { Component, OnInit, OnDestroy, Inject, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription, Observable, Subject } from 'rxjs/Rx';
import { RuleGroupService } from '../rule-group/rule-group.service';
import { DatePipe} from '@angular/common';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'moment';

import {
  AccountingDataBreadCrumbTitles,JsonforAccounting, SelectedAcctRows,SplitRowParams,
  RowsForSplit, AcctKeyValues, GroupByColumnValues, FilteredParams, RecordParams, 
  Credits, SegValues,Debits, AwqAllParams, GroupingFilterParams
} from './accounting-data.model';
import { SchedulingModel, Parameters,Scheduling } from '../scheduling/scheduling.component';
import {AcctDashBoardObject} from '../../shared/constants/constants.values';

import { SelectItem } from 'primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA, PageEvent } from '@angular/material';

import { CommonService } from '../common.service';
import { AccountingDataService } from './accounting-data.service';
import { DataViewsService } from '../data-views/data-views.service';
import { LookUpCodeService } from '../look-up-code/look-up-code.service'
import { ReconcileService } from '../reconcile/reconcile.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { ChartOfAccountService } from '../chart-of-account/chart-of-account.service';
import { LedgerDefinitionService } from '../ledger-definition/ledger-definition.service';
import { SegmentsService } from '../segments/segments.service';
import {SessionStorageService} from 'ng2-webstorage';
import { NgForm } from '@angular/forms';
import { sum } from 'd3';
declare var $: any;
declare var jQuery: any;
declare var moment: any;

@Component({
  selector: 'jhi-acct-confirm-modal',
  templateUrl: 'acct-confirm-modal.html'
})
export class AcctConfirmModalComponent {
  constructor(
      public dialogRef: MdDialogRef<AcctConfirmModalComponent>,
      public dialog: MdDialog,
      public accountingDataService: AccountingDataService,
      @Inject(MD_DIALOG_DATA) public data: any) {
  }

  onNoClick(): void {
      this.dialogRef.close();
  }
}

@Component({
  selector: 'jhi-accounting-data',
  templateUrl: './accounting-data.component.html'
})
export class AccountingDataComponent implements OnInit, OnDestroy {
  @ViewChild(Scheduling) scheduling:Scheduling;
  @ViewChild('accountForm') accountForm: NgForm;
  private unsubscribe: Subject<void>  = new Subject();
  itemsPerPage: any;
  page = 0;
  pageSizeOptions = [10, 25, 50, 100];;
  searchWord: any = "";
  selectedDataLinesToAccount: any[] = [];
  paramSet: any;
  sourceHeaderColumns1: any;
  isVisibleB = true;
  ruleGroupId: any;
  recordsForAccounting: JsonforAccounting;
  rowForAccounting: SelectedAcctRows[] = [];
  ruleGroupName: any;
  dataViewId: any;
  dataViewName: any;
  status: any= "";
  isAcctProcess = false;
  isAcctProcess2 = false;
  dataViews: any = [];
  ruleGroups: any[]=[];;
  queryParams: any;
  isCustomRange = false;
  selectedDateRange = '7days';
  rangeTo: any;
  toRange: any;
  rangeFrom: any;
  fromRange: any;
  showAcctAnalytics = false;
  selectedKeys: any[] = [];
  dataHeaderColumns: any[] = [];
  dataColumnOptions: SelectItem[];
  detailHeaderColumns: any[] = [];
  detailColumnOptions: SelectItem[];
  dataViewLines: any[] = [];
  viewLength: any;
  dataViewInfo: any;
  setIntId: any;
  callSetInt = false;
  acctJobId: string;
  acctJobOutput: any
  acctJobStatus: any;
  accountedCount = 0;
  unAccountedCount = 0;
  partialAccountedCount = 0;
  recChangeColor = 'mat-raised-button';
  unRecChangeColor = 'mat-raised-button';
  dateRange: any[] = [];
  qualifierColumns: any[] = [];
  displayConfirmDialog = false;
  filteredGroupBy: any = 'rules';
  selectedTab: any;
  accountModalTog = false;
  ledgername: any;
  acctSource: any;
  acctCategory: any;
  chartOfAccountsList: any[] = [];
  listOfLedgers: any[] = [];
  chartOfAccountName: any;
  chartOfAccount: any;
  sourceLookups: any;
  categoryLookups: any;
  segmentsList: any;
  debits: Debits[] = [];
  credits: Credits[] = [];
  dvColumnsByType: any[] = [];
  currencyQualifier: any;
  reconRuleGroupId: any;
  reconRuleGroups: any[] = [];
  reconJobId: string;
  reconJobOutput: any
  reconJobStatus: any;
  objectForUnRecon: any;
  objectForAcct: any;
  create: string;
  acctSchedObj = new SchedulingModel();
  acctSchedParamms: Parameters[] = [];
  displayScheduling = false;
  runAcctShow = false;
  buttonRestrict = false;
  period = 'fileDate';
  statusSummaryParams = new AwqAllParams();
  groupingSummaryParams = new AwqAllParams();
  statusSummary: any[] = [];
  groupingSummary: any[] = [];
  groupingSummaryFilters: any[] = [];
  statusSummaryObject: any = {};
  filterValues: GroupingFilterParams[];
  leftPane = "0px";
  isVisibleD = false;
  showFilters = true;
  isFullScreen = false;
  showStart:any;
  showEnd:any;
  isMultiCurrency: boolean;
  fxRate:any;
  conversionDate:any;
  groupIdForDisplay:any;
  accountedSummary:any[]=[];
  unAccountedSummary:any[]=[];
  unAccountedAlignInfo:any[]=[];
  unAcctSummaryInfo:any[]=[];
  allStatusSummary:any[]=[];
  ledgerRef:any;
  accountedSummaryData:any[]=[];
  isAccountedSummary = false;
  filterValues1:GroupingFilterParams[]=[];
  selectedMoreRange:any;
  isViewOnly:boolean;
  selectedParams:any;
  viewIdDisplay:any;
  isApproval:any;
  selectedSummary:any[]=[];
  selectedUnAcctSummary:any[]=[];
  accountedHeaders:any[]=[];
  accountedColumnOptions:any[]=[];
  showSplit = false;
  rowAction:any;
  splitRow:any[]=[];
  isEvenly:any;
  rowCount:number;
  selectedAmount:any;
  splitRowArray:SplitRowParams[]=[];
  masterRow:any;
  splitCurreny:any;
  addRowArray:SplitRowParams[]=[];
  isEditAcct = false;
  segmentsInner:any[]=[];
  segmentSep:any;
  viewColumns:any;
  currencyQualifiers:any[]=[];
  enteredCurrency:any;
  filterSidebar = false;
  accountedCurrency:string;
  pushedGroupingValues: any[] = [];
  duplicateKeys:any[]=[];
  sortOrder = 'desc';
  changeSort = false;
  lineTypeLookups:any[]=[];
  amountQualifier: any;
  dateQualifier: any;
  currencyQualifierName:any;
  amountQualifierName:any;
  oozieTest: any;
  tempLines:any[]=[];
  journalsJob:any;
  isChecked = false;
  isUnAcctChecked = false;
  periodsList:any[]=[];
  dateQualifierType:any;
  summaryPageEvent: PageEvent = new PageEvent();
  summaryLength = 0;
  summaryPageSizeOptions = [10, 25, 50, 100];
  dateRangeList = [
    {
      "value" : "Today",
      "name"  : "1D",
      "checked" : false,
      "title" :  "Today",
      "type" : "default"
    },
    {
      "value" : "7Days",
      "name"  : "1W",
      "checked" : false,
      "title" :  "Last 7 Days",
      "type" : "default"
    },
    {
      "value" : "14Days",
      "name"  : "2W",
      "checked" : false,
      "title" :  "Last 14 Days",
      "type" : "default"
    },
    {
      "value" : "30Days",
      "name"  : "1M",
      "checked" : true,
      "title" :  "Last Month",
      "type" : "default"
    },
    {
      "value" : "2Months",
      "name"  : "2M",
      "checked" : false,
      "title" :  "Last 2 Months",
      "type" : "default"
    },
    {
      "value" : "3Months",
      "name"  : "3M",
      "checked" : false,
      "title" :  "Last 3 Months",
      "type" : "default"
    },
    {
      "value" : "6Months",
      "name"  : "6M",
      "checked" : false,
      "title" :  "Last 6 Months",
      "type" : "default"
    }
  ];
  showActionButton:boolean;
  unAcctSummary:any[]=[];
  dateDifference:any;
  endDateDif:any;
  systemDate = new Date();
  columnLabel:any;
  groupingColName:any;
  columnInd:any;
  isNotCurrency = true;
  groupingColumns:any[]=[];
  getCurrencyColumn:any;
  constructor(
    public accountingDataService: AccountingDataService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private ruleGroupService: RuleGroupService,
    private commonService: CommonService,
    private notificationService: NotificationsService,
    private dataViewsService: DataViewsService,
    private lookUpCodeService: LookUpCodeService,
    public reconcileService: ReconcileService,
    private jobDetailsService: JobDetailsService,
    private datePipe: DatePipe,
    private chartOfAccountService: ChartOfAccountService,
    private ledgerDefinitionService: LedgerDefinitionService,
    private segmentService: SegmentsService,
    public dialog: MdDialog,
    private session:SessionStorageService
    ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.isEditAcct = false;
    this.summaryPageEvent.pageIndex=0;
    this.summaryPageEvent.pageSize=ITEMS_PER_PAGE;
  }

  ngOnInit() {
    this.fetchSourceLookups('SOURCE');
    this.fetchSourceLookups('CATEGORY');
    this.fetchSourceLookups('ACCOUNTING_LINE_TYPES');
    this.selectedParams = {};
    this.selectedParams = this.session.retrieve('reconSelected');
    if((!this.selectedParams || this.selectedParams == null || this.selectedParams == undefined) && !this.commonService.reconDashBoardObject.ruleGroupId){
      this.router.navigate(['/accounting-data-wq', {outlets: {'content': ['accounting-data-wq-home']}}]);
    }
    if (this.commonService.acctDashBoardObject.ruleGroupId) {
      this.ruleGroupId = this.commonService.acctDashBoardObject.ruleGroupId;
      this.dataViewId = this.commonService.acctDashBoardObject.dataViewId;
      this.dataViewName = this.commonService.acctDashBoardObject.dataViewName;
      this.period = this.commonService.acctDashBoardObject.periodFactor;
      this.rangeFrom = this.commonService.acctDashBoardObject.startDateRange;
      this.rangeTo = this.commonService.acctDashBoardObject.endDateRange;
    } else {
      this.ruleGroupId = this.selectedParams.groupId;
      this.dataViewId = this.selectedParams.viewId;
      this.ruleGroupName = this.selectedParams.ruleGroupName;
      this.dataViewName = this.selectedParams.viewName;
      let selectedRange:any;
      selectedRange = [moment().subtract(29, 'days'), moment()];
      this.rangeFrom = new Date(selectedRange[0]);
      this.rangeTo = new Date(selectedRange[1]);
    }
    this.fetchRuleGroups();
    this.principal.identity().then((account) => {
      this.commonService.currentAccount = account;
    });
  }

  fetchSourceLookups(type) {
    this.lookUpCodeService.fetchLookUpsByLookUpType(type).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        if (type == 'SOURCE') {
          this.sourceLookups = res;
        } else if (type == 'CATEGORY') {
          this.categoryLookups = res;
        } else if(type == 'ACCOUNTING_LINE_TYPES'){
          this.lineTypeLookups = res;
        }
      },
      (res: Response) => {
        this.notif('error','Error while fetching source meanings!');
      }
    );
  }

  private notif(type, message) {
    if (type == 'success') {
      this.notificationService.success('',message,{ timeOut: 3000,showProgressBar: false,pauseOnHover: true,clickToClose: true,maxLength: 100})
    } else if (type == 'error') {
      this.notificationService.error('',message,{ timeOut: 3000,showProgressBar: false,pauseOnHover: true,clickToClose: true,maxLength: 100})
    } else if (type == 'info') {
      this.notificationService.info('',message,{ timeOut: 3000,showProgressBar: false,pauseOnHover: true,clickToClose: true,maxLength: 100})
    }
  }

  fetchRuleGroups() {
    this.ruleGroupService.fetchRuleGroupsBypurpose('ACCOUNTING').takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.ruleGroups = res;
        if(this.ruleGroups.length > 0){
          this.fetchQueryParams(this.ruleGroupId);
        } else {
          this.notif('info','No Accounting processes found!');
        }
      },
      (res: Response) => {
        this.notif('error','Error Occured while fetching Accounting Processes!');
      }
    );
  }

  loadingAllData(){
    this.ruleGroupService.sourceDataViewId = this.dataViewId;
    this.ruleGroupService.sourceDataView = this.dataViewName;
    this.accountingDataService.getCoaByGroupIdService(this.ruleGroupId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.chartOfAccount = res.coaId;
        this.chartOfAccountName = res.coaName;
        this.getLedgerDefinitions(this.chartOfAccount);
        this.getSegmentsList(this.chartOfAccount);
      },
      (res: Response) => {
        this.notif('error','Error Occurred while fetching COA for ' + this.ruleGroups[0].name);
      }
    );
    this.fetchQualifierColumnsByView(this.dataViewId);
    this.getColumnsByType(this.dataViewId,'DECIMAL');
    this.getAccountedColumnHeaders();
    this.getDetailColumnHeaders();
    this.getUnaccountedColumnHeaders();
    this.getStatusLevelSummary();
  }

  onChangeRuleGroup(groupId){
    if (this.ruleGroups.length > 0) {
      this.isMultiCurrency = this.ruleGroups[this.ruleGroups.findIndex((x) => x.id == groupId)].multiCurrency;
      this.fxRate = this.ruleGroups[this.ruleGroups.findIndex((x) => x.id == groupId)].fxRateId;
      this.conversionDate = this.ruleGroups[this.ruleGroups.findIndex((x) => x.id == groupId)].conversionDate;
    }
    this.accountingDataService.fetchAcctQueryParams(groupId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.ruleGroupName = this.queryParams.ruleGroupName;
        this.dataViews = this.queryParams.dataViews;
        this.dataViewId = this.dataViews[0].id;
        this.periodsList = this.dataViews[this.dataViews.findIndex((x) => x.id == this.dataViewId)].dateQualifiers;
        for (let i = 0; i < this.periodsList.length; i++) {
          this.periodsList[i]['checked'] = false;
        }
        this.periodsList[0]['checked'] = true;
        this.period = this.periodsList[0].columnName;
        this.dateQualifierType = this.periodsList[0].dateQualifierType;
        this.loadingAllData();
      },
      (res: Response) => {
        this.notif('error','Error Occured while fetching Query Parameters!');
      }
    );
  }

  onChangeView(){
    this.periodsList = this.dataViews[this.dataViews.findIndex((x) => x.id == this.dataViewId)].dateQualifiers;
    for (let i = 0; i < this.periodsList.length; i++) {
      this.periodsList[i]['checked'] = false;
    }
    this.periodsList[0]['checked'] = true;
    this.period = this.periodsList[0].columnName;
    this.dateQualifierType = this.periodsList[0].dateQualifierType;
    this.loadingAllData();
  }

  fetchQueryParams(groupId) {
    if (this.commonService.acctDashBoardObject.dataViewId) {
      this.ruleGroupName = this.commonService.acctDashBoardObject.ruleGroupName;
      this.dataViewName = this.commonService.acctDashBoardObject.dataViewName;
      this.dataViewId = this.commonService.acctDashBoardObject.dataViewId;
    }
    this.isMultiCurrency = this.ruleGroups[this.ruleGroups.findIndex((x) => x.id == groupId)].multiCurrency;
    this.fxRate = this.ruleGroups[this.ruleGroups.findIndex((x) => x.id == groupId)].fxRateId;
    this.conversionDate = this.ruleGroups[this.ruleGroups.findIndex((x) => x.id == groupId)].conversionDate;
    this.accountingDataService.fetchAcctQueryParams(groupId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.showAcctAnalytics = false;
        this.ruleGroupName = this.queryParams.ruleGroupName;
        this.dataViews = this.queryParams.dataViews;
        this.periodsList = this.dataViews[this.dataViews.findIndex((x) => x.id == this.dataViewId)].dateQualifiers;
        for (let i = 0; i < this.periodsList.length; i++) {
          this.periodsList[i]['checked'] = false;
        }
        this.periodsList[0]['checked'] = true;
        this.period = this.periodsList[0].columnName;
        this.dateQualifierType = this.periodsList[0].dateQualifierType;
        this.loadingAllData();
      },
      (res: Response) => {
        this.notif('error','Error Occured while fetching Query Parameters!');
      }
    );
    
  }

  getLedgerDefinitions(coa) {
    this.ledgerDefinitionService.getLedgersByTenantAndCoa(coa).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.listOfLedgers = res;
        if (this.listOfLedgers.length > 0) {
          this.ledgername = this.listOfLedgers[0].id;
        } else {
          this.notificationService.info('Info!', 'No Ledger Found for this COA');
        }
      },
      (res: Response) => {
        this.notif('error','Error while fetching ledger definitions!');
      }
    );
  }

  getSegmentsList(coa) {
    this.segmentService.fetchSegmentsByCOA(coa).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.segmentsList = res;
        this.segmentsInner = this.segmentsList.segments;
        this.segmentSep = this.segmentsList.segmentSeparator;
        if (this.segmentsList) {
          this.debits = [];
          this.credits = [];
          if (!this.debits || this.debits.length < 1) {
            this.debits.push(new Debits());
            this.debits[0].amountColId = 0;
            this.debits[0].debitLine = "";
            this.debits[0].lineTypeDetial = "";
            this.debits[0].amountColName = "";
            this.debits[0].debitSegValues = [];
            this.debits[0].debitSegs = [];
          }
          if (!this.credits || this.credits.length < 1) {
            this.credits.push(new Credits());
            this.credits[0].amountColId = 0;
            this.credits[0].creditLine = "";
            this.credits[0].lineTypeDetial = "";
            this.credits[0].amountColName = "";
            this.credits[0].creditSegValues = [];
            this.credits[0].creditSegs = [];
          }
        }
      },
      (res: Response) => {
        this.notif('error','Error while fetching segments list of selected COA!')
      }
    );
  }

  fetchQualifierColumnsByView(viewId) {
    this.accountingDataService.getAcctQaulifiers(viewId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.currencyQualifiers = [];
        this.qualifierColumns = res;
        this.amountQualifier = this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnName;
        this.amountQualifierName = this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnDisplayName;
        this.currencyQualifier = this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnId;
        this.currencyQualifierName = this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnDisplayName;
        this.dateQualifier = this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnId;
        this.currencyQualifiers.push({
          "columnId" :  this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnId,
          "columnName" : this.qualifierColumns[this.qualifierColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnDisplayName
        });
      },
      (res: Response) => {
        this.notif('error','Error While Fetching Qualifier View Columns');
        
      }
    );
  }

  getColumnsByType(viewId, type) {
    this.dataViewsService.fetchDvColumnsByType(viewId, type).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.dvColumnsByType = res;
      },
      (res: Response) => {
        this.notif('error','Error while fetching data view columns!');
      }
    );
  }

  getAccountedColumnHeaders() {
    this.accountingDataService.accountedColumnHeaders('accounted').takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.accountedHeaders = res['columns'];
        this.accountedColumnOptions = [];
        for (let i = 0; i < this.accountedHeaders.length; i++) {
          this.accountedHeaders[i]['searchWord'] = "";
          this.accountedColumnOptions.push({ label: this.accountedHeaders[i].header, value: this.accountedHeaders[i] });
        }
      },
      (res: Response) => {
        this.notif('error','Unable to fetch data view column headers!');
      })
  }

  getDetailColumnHeaders(){
    this.accountingDataService.fetchingColHeaders(this.ruleGroupId,this.dataViewId,'un accounted').takeUntil(this.unsubscribe).subscribe(
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
        this.notif('error','Unable to fetch data view column headers!');
      })
  }

  getUnaccountedColumnHeaders() {
    this.accountingDataService.fetchingColHeaders(this.ruleGroupId, this.dataViewId, this.status).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.detailHeaderColumns = res.columns;
        this.detailColumnOptions =[];
        for (let i = 0; i < this.detailHeaderColumns.length; i++) {
          const item = this.detailHeaderColumns[i];
          this.detailHeaderColumns[i]['searchWord'] = "";
          this.detailColumnOptions.push({ label: item.header, value: item });
        }
      },
      (res: Response) => {
        this.notif('error','Unable to fetch data view column headers!');
      })
  }

  getStatusLevelSummary() {
    this.statusSummary = [];
    this.showAcctAnalytics = false;
    this.buttonRestrict = true;
    this.statusSummaryParams.groupId = this.ruleGroupId;
    this.statusSummaryParams.viewId = this.dataViewId;
    this.statusSummaryParams.periodFactor = this.period;
    this.statusSummaryParams.rangeFrom = this.datePipe.transform(this.rangeFrom,'yyyy-MM-dd');
    this.statusSummaryParams.rangeTo = this.datePipe.transform(this.rangeTo,'yyyy-MM-dd');
    this.accountingDataService.distinctStatuses(this.statusSummaryParams).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        this.statusSummaryObject = res;
        if (this.statusSummaryObject.summary.length > 0) {

          for (let y = 0; y < this.statusSummaryObject.summary.length; y++) {
            // if(this.listOfLedgers.length > 0){
            //   for (let l = 0; l < this.listOfLedgers.length; l++) {
            //     if (this.listOfLedgers[l].name === this.statusSummaryObject.summary[y].ledgerName) {
            //       this.statusSummaryObject.summary[y]['ledgerCurrency'] = this.listOfLedgers[l].currency;
            //     } else {
            //       this.statusSummaryObject.summary[y]['ledgerCurrency'] = "";
            //     }
            //   }
            // }
            if (this.statusSummaryObject.summary[y].count == '0') {
              this.statusSummaryObject.summary[y]['isSummary'] = false;
              this.statusSummaryObject.summary[y]['aColor'] = "";
            } else {
              this.statusSummaryObject.summary[y]['isSummary'] = true;
              this.statusSummaryObject.summary[y]['aColor'] = "";
            }

            if (this.statusSummaryObject.summary[y].status == 'accounted' || this.statusSummaryObject.summary[y].status == 'accounted, not reconciled' || this.statusSummaryObject.summary[y].status == 'accounted, reconciled') {
              this.statusSummaryObject.summary[y]['aColor'] = "green";
            } else if (this.statusSummaryObject.summary[y].status == 'un accounted' || this.statusSummaryObject.summary[y].status == 'un accounted, not reconciled' || this.statusSummaryObject.summary[y].status == 'un accounted, reconciled') {
              this.statusSummaryObject.summary[y]['aColor'] = "red";
            } else {
              this.statusSummaryObject.summary[y]['aColor'] = "blue";
            }

          }
          this.statusSummary = this.statusSummaryObject.summary;
          this.showAcctAnalytics = true;
          this.getSideBarGrouping('');
        } else {
          this.notificationService.info('Info!', 'No Records Found within Date Range');
        }

      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.notif('error','Error Occured while fetching status wise summary!')
      }
    );
  }

  getSideBarGrouping(e) {
    this.summaryPageEvent.pageIndex=0;
    this.summaryPageEvent.pageSize=ITEMS_PER_PAGE;
    this.isChecked = false;
    this.groupingSummary = [];
    this.selectedUnAcctSummary = [];
    this.selectedSummary = [];
    this.buttonRestrict = true;
    this.leftPane = "-500px";
    this.selectedKeys = [];
    if(this.commonService.acctDashBoardObject.status){
      this.selectedTab = this.statusSummary.findIndex((x) => x.status == this.commonService.acctDashBoardObject.status);
    } else {
      if(!e){
        if(this.status == ''){
          this.selectedTab = 1;
        } else {
          this.selectedTab = this.statusSummary.findIndex((x) => x.status == this.status);
        }
      } else {
        this.selectedTab = e.index;
      }
    }
    this.status = this.statusSummary[this.selectedTab].status;
    // this.ledgerRef = this.statusSummary[this.selectedTab].ledgerName;
    this.groupingSummaryParams.groupId = this.ruleGroupId;
    this.groupingSummaryParams.periodFactor = this.period;
    this.groupingSummaryParams.viewId = this.dataViewId;
    this.groupingSummaryParams.status = this.status;
    this.groupingSummaryParams.rangeFrom = this.datePipe.transform(this.rangeFrom,'yyyy-MM-dd');
    this.groupingSummaryParams.rangeTo = this.datePipe.transform(this.rangeTo,'yyyy-MM-dd');
    this.groupingSummaryParams.filters = [];
    // if(this.status == 'accounted' || this.status == 'accounted, not reconciled' || this.status == 'accounted, reconciled' || this.status == 'inprocess'){
    //   this.groupingSummaryParams.filters.push({
    //     key : 'ledger_name',
    //     values : [this.ledgerRef],
    //     dataType : 'STRING'
    //   })
    // } else {
    this.groupingSummaryParams.filters = [];
    // }
    this.groupingSummaryParams.columnSearch = [];
    this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.commonService.acctDashBoardObject = new AcctDashBoardObject();
        this.buttonRestrict = false;
        this.groupingSummary = res;
        if(this.groupingSummary.length > 0){
          for (let z = 0; z < this.groupingSummary.length; z++) {
            const m = this.groupingSummary[z];
            m['isChecked'] = false;
          }
          if(this.status == 'un accounted' || this.status == 'un accounted, not reconciled' || this.status == 'un accounted, reconciled'){
            // this.getGroupByAcctRecords();
            this.fetchUnAccountedSummary();
          } else {
            this.selectedSummary = [];
            this.getAccountedSummary();
          }
          
        } else {
          this.notificationService.info('Info!','No Filters Found for selected process')
        }
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.notif('error','Error Occured while fetching filters for the selected process !')
      }
    );
  }

  getAccountedSummary(){
    this.accountedSummaryData = [];
    this.buttonRestrict = true;
    this.groupingSummaryParams.groupId = this.ruleGroupId;
    this.groupingSummaryParams.periodFactor = this.period;
    this.groupingSummaryParams.viewId = this.dataViewId;
    this.groupingSummaryParams.status = this.status;
    this.groupingSummaryParams.rangeFrom = this.datePipe.transform(this.rangeFrom,'yyyy-MM-dd');
    this.groupingSummaryParams.rangeTo = this.datePipe.transform(this.rangeTo,'yyyy-MM-dd');
    this.groupingSummaryParams.pageNumber=this.summaryPageEvent.pageIndex;
    this.groupingSummaryParams.pageSize=this.summaryPageEvent.pageSize;
    this.accountingDataService.accountedSummaryInfo(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.isAccountedSummary = true;
        this.buttonRestrict = false;
        this.accountedSummaryData = res.summary;
        this.summaryLength=res.totalCount;
        if(this.groupingSummary.length > 0){
          this.isAcctProcess = true;
        } else {
          this.notificationService.info('Info!','No Records Found for selected Status')
        }
        
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.notif('error', 'Error Occured while fetching Accounted Summary for the selected status !');
      }
    );
  }

  getGroupByAcctRecords() {
    this.dataViewLines = [];
    this.buttonRestrict = true;
    if(this.changeSort == false){
      this.isAcctProcess = false;
    }
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
            dataType : this.selectedKeys[i].dataType
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
              dataType : this.selectedKeys[i].dataType
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
            } else if(this.filterValues[l].key === this.selectedKeys[k].filterColumn && this.filterValues[l].dataType == 'DATE'){
              arr.push(this.datePipe.transform(this.selectedKeys[k].name,'yyyy-MM-dd'));
              this.filterValues[l].values = arr;
            }
          }
        }
      }
      this.groupingSummaryParams.filters = this.filterValues;
      const pushLen = 0;
      this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
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
          this.buttonRestrict = false;
          this.notif('error','Error Occured while fetching grouping parameters!');
        }
      );
    } 
    
    if(this.selectedUnAcctSummary.length > 0){
      if(this.groupingColName == this.getCurrencyColumn){
        this.filterValues.push({
          key : this.groupingColName,
          dataType : "STRING"  
        })
      } else {
        this.filterValues.push({
          key : this.groupingColName,
          dataType : "STRING"  
        });
        this.filterValues.push({
          key : this.getCurrencyColumn,
          dataType : "STRING"  
        });
      }
      if (this.filterValues.length > 0) {
        for (let l = 0; l < this.filterValues.length; l++) {
          arr = [];
          for (let k = 0; k < this.selectedUnAcctSummary.length; k++) {
            if (this.filterValues[l].key === this.groupingColName) {
              arr.push(this.selectedUnAcctSummary[k].groupingColumn);
              this.filterValues[l].values = arr;
            }
            if(this.getCurrencyColumn != this.groupingColName){
              if(this.filterValues[l].key === this.getCurrencyColumn){
                arr.push(this.selectedUnAcctSummary[k][this.getCurrencyColumn]);
                this.filterValues[l].values = arr;
              }
            }
          }
        }
        for (let c = 0; c < this.filterValues.length; c++) {
          if(!this.filterValues[c].values){
            this.filterValues.splice(c,1);
          }
        }
      }
      this.groupingSummaryParams.filters = this.filterValues;
    }
   
    if(this.selectedUnAcctSummary.length == 0 && this.selectedKeys.length == 0){
      this.groupingSummaryParams.filters = [];
    }
    this.groupingSummaryParams.pageNumber = this.page;
    this.groupingSummaryParams.pageSize = this.itemsPerPage;
    this.groupingSummaryParams.sortOrderBy = this.sortOrder;
    this.groupingSummaryParams.sortByColumnName = this.amountQualifier;
    this.groupingSummaryParams.columnSearch = [];
    if(this.status == 'un accounted' || this.status == 'un accounted, not reconciled' || this.status == 'un accounted, reconciled'){
    this.accountingDataService.detailedInfoRecords(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.dataViewLines = res;
          this.objectForUnRecon = jQuery.extend(true, {}, this.groupingSummaryParams);
          if (this.dataViewLines.length > 1) {
            this.changeSort = false;
            this.isAcctProcess = true;
            this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
            this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
          } else {
            this.isAcctProcess = true;
            this.notificationService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.notif('error','Error while fetching un accounted records');
        }
      )
    } else {
      this.selectedSummary = [];
      this.getAccountedSummary();
      // this.getAccountedDetails();
    }
  }

  emptyGroupingSummary(){
    if(this.selectedKeys.length == 0){
      this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.groupingSummary = res;
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.notif('error','Error Occured while fetching grouping parameters!');
        }
      );
    }
  }

  changePFactor(e){
    this.period = e.value;
    this.dateQualifierType = this.periodsList[this.periodsList.findIndex((x) => x.columnName == e.value)].dateQualifierType;
    this.loadingAllData();
  }

  colLevelSearchWithKeyWord() {
    this.buttonRestrict = true;
    this.selectedDataLinesToAccount = [];
    if(this.objectForUnRecon){
      this.groupingSummaryParams.filters = this.objectForUnRecon['filters'];
    }else{
      this.groupingSummaryParams.filters = [];
    }
    this.groupingSummaryParams.pageNumber = 0;
    this.groupingSummaryParams.pageSize = this.itemsPerPage;
    this.groupingSummaryParams.sortOrderBy = this.sortOrder;
    this.groupingSummaryParams.sortByColumnName = this.amountQualifier;
    this.groupingSummaryParams.columnSearch = [];
    this.groupingSummaryParams.searchWord = "";
    for (let i = 0; i < this.dataHeaderColumns.length; i++) {
      if (this.dataHeaderColumns[i].searchWord != "") {
        this.groupingSummaryParams.columnSearch.push({
          columnName: this.dataHeaderColumns[i].columnName,
          searchWord: this.dataHeaderColumns[i].searchWord
        })
      }
    }
    if(this.status == 'un accounted' || this.status == 'un accounted, not reconciled' || this.status == 'un accounted, reconciled'){
    this.accountingDataService.detailedInfoRecords(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.dataViewLines = res;
          this.groupingSummaryParams.columnSearch = [];
          if (this.dataViewLines.length > 1) {
            this.changeSort = false;
            this.isAcctProcess = true;
            this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
            this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
            if(this.isAcctProcess){
              // $('html, body').animate({
              //   scrollTop: $("#group_details").offset().top - 200
              // }, 2000);
            }
          } else {
            this.isAcctProcess = true;
            this.notificationService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.notif('error','Error while fetching un accounted records');
        }
      )
    } else {
      this.getAccountedDetails();
    }
  }

  checkAll(event) {
    this.selectedSummary = [];
    if (event == true) {
      for (let i = 0; i < this.accountedSummaryData.length; i++) {
        this.selectedSummary.push(this.accountedSummaryData[i]);
      }
      this.getAccountedDetails();
      
    } else {
      this.selectedSummary = [];
    }
  }

  unAcctCheckAll(event) {
    this.selectedKeys = [];
    if (event == true) {
      for (let i = 0; i < this.unAccountedSummary.length; i++) {
        this.selectedUnAcctSummary.push(this.unAccountedSummary[i]);
      }
      this.getGroupByAcctRecords();
      
    } else {
      this.selectedUnAcctSummary = [];
    }
  }


  getAccountedDetails(){
    this.isChecked = false;
    this.buttonRestrict = true;
    if(this.changeSort == false){
      this.isAcctProcess = false;
    }
    this.filterValues1 = [];
    this.selectedDataLinesToAccount = [];
    if(this.selectedSummary.length > 0) {
      let len = 0;
      for (let i = 0; i < this.selectedSummary.length; i++) {
        const element = this.selectedSummary[i];
        len = len + element.je_count;
      }
      if(len == 0){
        this.showActionButton = true;
      } else {
        this.showActionButton = false;
      }
      if (this.selectedSummary.length == this.accountedSummaryData.length) {
        this.isChecked = true;
      }
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
        }
      ];
      let arr:any[]=[];
      if (this.filterValues1.length > 0) {
        for (let l = 0; l < this.filterValues1.length; l++) {
          arr = [];
          for (let k = 0; k < this.selectedSummary.length; k++) {
            if (this.filterValues1[l].key === 'source_meaning') {
              arr.push(this.selectedSummary[k]['source_meaning']);
              this.filterValues1[l].values = arr;
            } else if(this.filterValues1[l].key === 'category_meaning'){
              arr.push(this.selectedSummary[k]['category_meaning']);
              this.filterValues1[l].values = arr;
            } else if(this.filterValues1[l].key === 'entered_currency'){
              arr.push(this.selectedSummary[k]['entered_currency']);
              this.filterValues1[l].values = arr;
            } else if(this.filterValues1[l].key === 'ledger_name'){
              arr.push(this.selectedSummary[k]['ledger_name']);
              this.filterValues1[l].values = arr;
            }
          }
        }
      }
      if(this.selectedKeys.length > 0){
        for (let i = 0; i < this.filterValues.length; i++) {
          this.filterValues1.push(this.filterValues[i]);
        }
      }
      this.groupingSummaryParams.filters = this.filterValues1;
      this.groupingSummaryParams.pageNumber = this.page;
      this.groupingSummaryParams.pageSize = this.itemsPerPage;
      this.groupingSummaryParams.sortOrderBy = this.sortOrder;
      this.groupingSummaryParams.sortByColumnName = this.amountQualifier;
      this.groupingSummaryParams.searchWord = "";
      this.groupingSummaryParams.columnSearch = [];
      for (let i = 0; i < this.detailHeaderColumns.length; i++) {
        if (this.detailHeaderColumns[i].searchWord != "") {
          this.groupingSummaryParams.columnSearch.push({
            columnName: this.detailHeaderColumns[i].columnName,
            searchWord: this.detailHeaderColumns[i].searchWord
          })
        }
      }
      this.accountingDataService.detailedInfoRecords(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.dataViewLines = res;
          this.groupingSummaryParams.columnSearch = [];
          this.objectForAcct = jQuery.extend(true, {}, this.groupingSummaryParams);
          if (this.dataViewLines.length > 1) {
            this.changeSort = false;
            this.isAcctProcess = true;
            this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
            this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
          } else {
            this.isAcctProcess = true;
            this.notificationService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.notif('error','Error occured while fetching accounted records!');
        }
      )
    } else {
      this.buttonRestrict = false;
    }
  }

  unCheckAll(ind){
    this.groupingSummary[ind]['isChecked'] = false;
  }

  changeSourceSort(event) {
    this.changeSort = true;
    for (let i = 0; i < this.dataHeaderColumns.length; i++) {
      if (this.dataHeaderColumns[i].header == event.field) {
        this.amountQualifier = this.dataHeaderColumns[i].columnName;
      }
    }
    if (event.order < 1) {
      this.sortOrder = 'desc';
    } else {
      this.sortOrder = 'asc';
    }
    if(this.isAccountedSummary == false){
      this.getGroupByAcctRecords();
    } else {
      this.getAccountedDetails();
    }
    
  }

  fetchReconRuleGroups(viewId, purpose) {
    this.ruleGroupService.fetchingGroupsByViewId(viewId, purpose).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.reconRuleGroups = res;
        if (this.reconRuleGroups.length > 0) {
          this.runAcctShow = true;
        } else {
          this.runAcctShow = false;
          this.notificationService.info('Info!', 'No Eligble Reconciliation Processes Found!');
        }
      },
      (res: Response) => {
        this.notif('error','Error Occured while fetching Reconciliation Rule Groups!');
      }
    );
  }
 
  getAccountedCurrency(id){
    this.accountedCurrency = this.listOfLedgers[this.listOfLedgers.findIndex((x) => x.id == id)].currency;
  }

  closeReconDialog(){
    $('.custom-dialog').removeClass('custDialogOpen');
    this.isEditAcct = false;
    this.accountForm.resetForm();
  }
  
  openAccountDialog() {
    $('.custom-dialog').addClass('custDialogOpen');
    const UAccLen = 0;
    if (this.selectedDataLinesToAccount.length > 0) {
      this.isEditAcct = false;
        this.ledgername = null;
        this.acctCategory = "";
        this.acctSource = "";
        this.enteredCurrency = "";
    } else {
      $('.custom-dialog').removeClass('custDialogOpen');
      this.notificationService.info('Info!', 'No Records Selected');
    }
  }

  onChangeValueName(type, event, ind, dInd, val) {
    let debitCode = "";
    let creditCode = "";
    if (type == 'DEBIT') {
      for (let i = 0; i < val.length; i++) {
        const elem = val[i];
        if (elem.id == event.value.id) {
          if (this.debits[dInd].debitSegValues.length > 0) {
            let editing = false;
            for (let l = 0; l < this.debits[dInd].debitSegValues.length; l++) {
              const elem2 = this.debits[dInd].debitSegValues[l];
              if (elem2.mappingSetId == elem.mappingSetId) {
                this.debits[dInd].debitSegValues.splice(l, 1, elem);
                editing = true;
              }
            }
            if (editing == false) {
              this.debits[dInd].debitSegValues.push(elem);
            }
          } else {
            this.debits[dInd].debitSegValues.push(elem);
          }
        }
      }
      if (!isNaN(event.value.targetValue)) {
        event.value.targetValue = event.value.targetValue.toString();
      }
      for (let j = 0; j < this.debits[dInd].debitSegValues.length; j++) {
        const elem1 = this.debits[dInd].debitSegValues[j];
        if (j == this.debits[dInd].debitSegValues.length - 1) {
          debitCode = debitCode + this.debits[dInd].debitSegValues[j]['sourceValue'];
        } else {
          debitCode = debitCode + this.debits[dInd].debitSegValues[j]['sourceValue'] + "-"
        }
      }
      this.debits[dInd].debitLine = debitCode;
    } else {
      for (let i = 0; i < val.length; i++) {
        const elem = val[i];
        if (elem.id == event.value.id) {
          if (this.credits[dInd].creditSegValues.length > 0) {
            let editing= false;
            for (let l = 0; l < this.credits[dInd].creditSegValues.length; l++) {
              const elem2 = this.credits[dInd].creditSegValues[l];
              if (elem2.mappingSetId == elem.mappingSetId) {
                this.credits[dInd].creditSegValues.splice(l, 1, elem);
                editing = true;
              }
            }
            if (editing == false) {
              this.credits[dInd].creditSegValues.push(elem);
            }
          } else {
            this.credits[dInd].creditSegValues.push(elem);
          }
        }
      }
      if (!isNaN(event.value.targetValue)) {
        event.value.targetValue = event.value.targetValue.toString();
      }
      for (let j = 0; j < this.credits[dInd].creditSegValues.length; j++) {
        const elem1 = this.credits[dInd].creditSegValues[j];
        if (j == this.credits[dInd].creditSegValues.length - 1) {
          creditCode = creditCode + this.credits[dInd].creditSegValues[j]['sourceValue'];
        } else {
          creditCode = creditCode + this.credits[dInd].creditSegValues[j]['sourceValue'] + "-"
        }
      }
      this.credits[dInd].creditLine = creditCode;
    }
  }

  valueCodeRestrict(code, len, val) {
    let valLen = 0;
    if (code > 31 && (code < 48 || code > 57) && (code < 96 || code > 105)) {
      return false;
    } else {
      if (isNaN(val)) {
        valLen = val.toString().length;
      } else {
        valLen = val.length;
      }

      if (code == 9 && valLen != len) {
        return false;
      } else {
        return true;
      }
    }

  }

  onBlurValueSet(event){
    event.returnValue = false;
  }

  addDebitRow() {
    this.debits.push({
      amountColId : 0,
      debitLine : "",
      lineTypeDetial : "",
      amountColName : "",
      debitSegValues : [],
      debitSegs : [],
    });
  }

  addCreditRow() {
    this.credits.push({
      amountColId : 0,
      creditLine : "",
      lineTypeDetial : "",
      amountColName : "",
      creditSegValues : [],
      creditSegs : [],
    });
  }

  trackByIndex(i:number){
    return i;
  }

  removeDebitLine(i: number) {
    if (this.debits.length > 1) {
      this.debits.splice(i, 1);
    } else {
      this.notificationService.info('Info!','Atleast one debit line should be entered!');
    }
  }

  removeCreditLine(i: number) {
    if (this.credits.length > 1) {
      this.credits.splice(i, 1);
    } else {
      this.notificationService.info('Info!','Atleast one credit line should be entered!');
    }
  }

  openEditAccounting(row) {
    this.isEditAcct = true;
    this.selectedDataLinesToAccount = [];
    this.selectedDataLinesToAccount.push(row);
    $('.custom-dialog').addClass('custDialogOpen');
    this.debits = [];
    this.credits = [];
    this.acctCategory = this.categoryLookups[this.categoryLookups.findIndex((x) => x.meaning == row.Category)].lookUpCode;
    this.acctSource = this.sourceLookups[this.sourceLookups.findIndex((x) => x.meaning == row.Source)].lookUpCode;
    this.ledgername = row.ledger_ref;
    this.accountedCurrency = this.listOfLedgers[this.listOfLedgers.findIndex((x) => x.id == this.ledgername)].currency;
    this.enteredCurrency = this.currencyQualifier;
    for (let i = 0; i < row.children.length; i++) {
      const item = row.children[i];
      if(item['Debit Account'] != ""){
        this.debits.push({
          lineTypeDetial : item['Line Type Detail'],
          amountColId : parseInt(item['amount_col_id'],10),
          debitLine : item['Debit Account'],
          debitSegs : []
        })
      } else {
        this.credits.push({
          lineTypeDetial : item['Line Type Detail'],
          amountColId : parseInt(item['amount_col_id'],10),
          creditLine : item['Credit Account'],
          creditSegs : []
        })
      }
    }
    let dSegs:any[]=[];
    let cSegs:any[]=[];
    for (let d = 0; d < this.debits.length; d++) {
      const debit = this.debits[d];
      dSegs = [];
      dSegs = debit.debitLine.split(this.segmentSep);
      for (let ds = 0; ds < dSegs.length; ds++) {
        for (let v = 0; v < this.segmentsInner[ds].valueSet.length; v++) {
          const segv = this.segmentsInner[ds].valueSet[v];
          if(dSegs[ds] ===  segv.sourceValue){
            debit.debitSegs.push(segv);
            break;
          }
          
        }
      }
    }
    for (let c = 0; c < this.credits.length; c++) {
      const credit = this.credits[c];
      cSegs = [];
      cSegs = credit.creditLine.split(this.segmentSep);
      for (let cs = 0; cs < cSegs.length; cs++) {
        for (let v = 0; v < this.segmentsInner[cs].valueSet.length; v++) {
          const segv = this.segmentsInner[cs].valueSet[v];
          if(cSegs[cs] ==  segv.sourceValue){
            credit.creditSegs.push(segv);
          }
          
        }
      }
    }
  }

  manualAccounting(groupId, viewId) {
    this.buttonRestrict = true;
    this.recordsForAccounting = {};
    this.rowForAccounting = [];
    if (this.selectedDataLinesToAccount.length > 0) {
      this.recordsForAccounting.groupId = groupId;
      this.recordsForAccounting.viewId = viewId;
      this.recordsForAccounting.categoryRef = this.acctCategory;
      this.recordsForAccounting.coaRef  = this.chartOfAccount;
      this.recordsForAccounting.enteredCurrency = this.enteredCurrency;
      this.recordsForAccounting.ledgerRef  = this.ledgername,
      this.recordsForAccounting.sourceRef = this.acctSource;
      this.recordsForAccounting.credits = this.credits;
      this.recordsForAccounting.debits = this.debits;
      this.recordsForAccounting.rows = [];
      if (this.isMultiCurrency == true) {
        this.recordsForAccounting.conversionDate = this.conversionDate;
        this.recordsForAccounting.fxRateId = this.fxRate;
      }
      this.recordsForAccounting.accountedCurrency = this.accountedCurrency;
      for (let i = 0; i < this.selectedDataLinesToAccount.length; i++) {
        const item = this.selectedDataLinesToAccount[i];
        this.recordsForAccounting.rows.push(item.dataRowId);
      }
      this.accountingDataService.manualAcct(this.recordsForAccounting).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.closeReconDialog();
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.buttonRestrict = false;
          this.accountModalTog = false;
          this.codeCombinationClear();
          this.notificationService.success('Info!', 'Selected Records Accounted Successfully');
          this.rowForAccounting = [];
          if(!this.isEditAcct) {
            this.getStatusLevelSummary();
          } else {
            this.getSideBarGrouping(undefined);
          }
          this.isEditAcct = false;
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.isEditAcct = false;
          this.notif('error','Error occured while accounting the transactions')
        }
      );
    }
  }

  openUnAccountDialog(type,purpose) {
    this.groupingSummaryParams.type = type;
    this.groupingSummaryParams.accountingType = purpose;
    const dialogRef = this.dialog.open(AcctConfirmModalComponent, {
      width: '400px',
      data: { context: purpose, ok: 'ok', reason: '', cancel: 'cancel' }
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result == 'ok') {
        this.buttonRestrict = true;
        let accLen = 0;
        if (this.selectedDataLinesToAccount.length > 0) {
          for (let i = 0; i < this.selectedDataLinesToAccount.length; i++) {
            if (this.selectedDataLinesToAccount[i].Status.includes('ACCOUNTED')) {
              accLen = accLen + 1;
            }
          }
        }
        if (type == 'batchwise') {
          this.accountingDataService.manualUnAcctNew(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
            (res: any) => {
              this.accountedCount = 0;
              this.unAccountedCount = 0;
              this.recChangeColor = 'mat-raised-button';
              this.unRecChangeColor = 'mat-raised-button';
              this.buttonRestrict = false;
              if(purpose == 'Reverse'){
                this.notificationService.success('Success!', 'Reverse Entries Accounted Successfully!');
              } else {
                this.notificationService.success('Success!', 'Selected Accounted Transactions UnAccounted Successfully!');
              }
              this.accountedCount = 0;
              this.unAccountedCount = 0;
              this.getStatusLevelSummary();
            },
            (res: Response) => {
              this.buttonRestrict = false;
              this.notif('error','Error occured while un accounting the transactions')
            }
          );
        } else {
          if (this.selectedDataLinesToAccount.length == accLen) {
            const acIds: any[] = [];
            if (this.selectedDataLinesToAccount.length > 0) {
              this.selectedDataLinesToAccount.forEach((item) => {
                acIds.push(item.dataRowId);
              });
            }
            this.groupingSummaryParams.originalRowIds = acIds;
            this.accountingDataService.manualUnAcctNew(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe(
              (res: any) => {
                this.accountedCount = 0;
                this.unAccountedCount = 0;
                this.recChangeColor = 'mat-raised-button';
                this.unRecChangeColor = 'mat-raised-button';
                this.buttonRestrict = false;
                if(purpose == 'Reverse'){
                  this.notificationService.success('Success!', 'Reverse Entries Accounted Successfully!');
                } else {
                  this.notificationService.success('Success!', 'Selected Accounted Transactions UnAccounted Successfully!');
                }
                this.getStatusLevelSummary();
              },
              (res: Response) => {
                this.buttonRestrict = false;
                this.notif('error','Error occured while un accounting the transactions');
              }
            );
          } else {
            this.buttonRestrict = false;
            this.accountModalTog = false;
            this.notificationService.info('Info!', 'Please select only Accounted records');
          }

        }
      }
    })

  }

  codeCombinationClear() {
    this.ledgername = "";
    this.debits = [];
    this.credits = [];
    this.acctCategory = "";
    this.acctSource = "";
    this.debits.push(new Debits());
    this.debits[0].amountColId = 0;
    this.debits[0].debitLine = "";
    this.debits[0].lineTypeDetial = "";
    this.debits[0].amountColName = "";
    this.credits.push(new Debits());
    this.credits[0].amountColName = "";
    this.credits[0].amountColId = 0;
    this.credits[0].creditLine = "";
    this.credits[0].lineTypeDetial = "";
    this.debits[0].debitSegs = [];
    this.debits[0].debitSegValues = [];
    this.credits[0].creditSegs = [];
    this.credits[0].creditSegValues = [];
  }

  onPaginateChange(event) {
    this.page = event.pageIndex;
    this.itemsPerPage = event.pageSize;
    for (let i = 0; i < this.dataHeaderColumns.length; i++) {
      if (this.dataHeaderColumns[i].searchWord != "") {
        this.groupingSummaryParams.columnSearch.push({
          columnName: this.dataHeaderColumns[i].columnName,
          searchWord: this.dataHeaderColumns[i].searchWord
        })
      }
    }
    if(this.isAccountedSummary == false){
      this.getGroupByAcctRecords();
    } else {
      this.getAccountedDetails();
    }
    
  }

  initiateJournals(){
    const dialogRef = this.dialog.open(AcctConfirmModalComponent, {
      width: '400px',
      data: { context: 'Prepare Journals for' , ok: 'ok', reason: '', cancel: 'cancel' }
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result == 'ok') {
        this.accountingDataService.journalTemplatesByViewId(this.dataViewId).takeUntil(this.unsubscribe).subscribe((res: any) => {
          this.tempLines = res;
          if(this.tempLines.length > 0){
            this.jobDetailsService.oozieDBTest().takeUntil(this.unsubscribe).subscribe((oozieres: any) => {
              this.oozieTest = oozieres.ooziestatus;
              if (this.oozieTest == true) {
                for (let i = 0; i < this.tempLines.length; i++) {
                  this.paramSet = {
                    'param1' : this.tempLines[i].idForDisplay
                  }
                  this.jobDetailsService.initiateAcctRecJob('JOURNALS', this.paramSet).takeUntil(this.unsubscribe).subscribe((jobres: any) => {
                    this.journalsJob = jobres.status;
                    if (this.journalsJob != 'Failed to intiate job') {
                      this.notificationService.success('Info!', 'Journals Preparation Initiated Successfully!');
                    } else {
                      this.notificationService.error('Error!', 'Failed to intiate job!');
                    }
                  },
                    (jobres: Response) => {
                      this.buttonRestrict = false;
                      this.notif('error', 'Failed to intiate job!');
                    }
                  )
                }
              } else {
                this.notificationService.error('Warning!','Job Engine Start Up Failure...Please try again later');
              }
            }, (oozieres: Response) => {
              this.notif('error','Error while checking Oozie status!');
            }
            )
          } else {
            this.notificationService.info('Error!', 'No Templates Found for the selected data view!');
          }
        }, (res: Response) => {
          this.notif('error','Error while getting template lines for selected data view!');
        }
        )
      }
    })
  }

  initiateAcctJob(purpose) {
    this.scheduling.ngOnInit();
    let fromDate: any = "";
    let toDate: any = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    this.dateRange = [fromDate, toDate];
    if (purpose == 'ACCOUNTING') {
      this.paramSet = {
        'param1': this.ruleGroupId,
        'param3': this.dataViewId,
        'param6': this.dateRange
      }
    } else if('RECONCILIATION'){
      this.paramSet = {
        'param1': this.reconRuleGroupId,
        'param3': this.dataViewId,
        'param6': this.dateRange
      }
    }
    this.jobDetailsService.oozieDBTest().takeUntil(this.unsubscribe).subscribe((res: any) => {
      this.oozieTest = res.ooziestatus;
      if (this.oozieTest == true) {
        this.initiatingJob(purpose, this.paramSet);
      } else {
        this.notificationService.error('Warning!','Job Engine Start Up Failure...Please try again later');
      }
    }, (res: Response) => {
      this.notif('error','Error while checking Oozie status!');
    }
    )
  }

  beforeJobInitiate() {
    this.displayConfirmDialog = true;
  }

  buildRule() {
    this.reconcileService.ENABLE_RULE_BLOCK = true;
    console.log('Rule Group ID for Build Rule '+this.ruleGroupId);
    this.create = 'createInWQ';
    this.create = this.ruleGroupId + '-' + this.create;
  }

  hideSchDialog(event) {
    this.accountingDataService.acctSchJob = false;
  }

  scheduleJobAcct() {
    this.acctSchedObj = {};
    this.acctSchedParamms = [];
    let fromDate: any = "";
    let toDate: any = "";
    const colValues = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    this.acctSchedParamms = [
      {
        paramName: 'RuleGroupName',
        selectedValue: this.ruleGroupId
      },
      {
        paramName: 'Filter View',
        selectedValue: this.dataViewId
      },
      {
        paramName: 'Date Range',
        selectedValue: fromDate + "," + toDate
      }
    ];
    this.acctSchedObj.displayParameters = [
      {
        paramName: 'RuleGroupName',
        selectedValue: this.ruleGroupName
      },
      {
        paramName: 'Filter View',
        selectedValue: this.dataViewName
      },
      {
        paramName: 'Date Range',
        selectedValue: fromDate + "," + toDate
      }
    ]
    this.acctSchedObj.parameters = this.acctSchedParamms;
    this.acctSchedObj.jobName = this.ruleGroupName;
    this.acctSchedObj.programName = 'ACCOUNTING';
    this.displayScheduling = true;
  }



  initiatingJob(purpose, set) {
    this.buttonRestrict = true;
    if (purpose == 'ACCOUNTING') {
      this.jobDetailsService.initiateAcctRecJob(purpose, set).takeUntil(this.unsubscribe).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.acctJobOutput = res;
        this.acctJobId = this.acctJobOutput.status;
        this.runAcctShow = false;
        this.displayConfirmDialog = false;
        if (this.acctJobId != 'Failed to intiate job') {
          this.notificationService.success('Info!', 'Job Initiated Successfully!');
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.setIntId = setInterval(() => {
            if (this.callSetInt == true) {
              this.jobDetailsService.oozieeJobStatus(this.acctJobId).takeUntil(this.unsubscribe).subscribe((oozieres: any) => {
                this.acctJobStatus = oozieres;
                this.accountedCount = 0;
                this.unAccountedCount = 0;
                this.getStatusLevelSummary();
                this.recChangeColor = 'mat-raised-button';
                this.unRecChangeColor = 'mat-raised-button';
                if (this.acctJobStatus._body == 'SUCCEEDED' || this.acctJobStatus._body == 'KILLED' || this.acctJobStatus._body == 'FAILED') {
                  if (this.setIntId) {
                    this.getStatusLevelSummary();
                    clearInterval(this.setIntId);
                  }
                }
              },
                (oozieres: Response) => {
                  this.notif('error','Error while checking job status!');
                }
              )
            } else {
              clearInterval(this.setIntId);
            }
          }, 40000);
        } else {
          this.notificationService.error('Error!', 'Failed to Initiate Job!');
        }
      },
        (res: Response) => {
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.buttonRestrict = false;
          this.notif('error', 'Failed to Initiate Job!');
        }
      )

    } else {
      this.jobDetailsService.initiateAcctRecJob(purpose, set).takeUntil(this.unsubscribe).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.reconJobOutput = res;
        this.runAcctShow = false;
        this.reconJobId = this.reconJobOutput.status;
        if (this.reconJobId != 'Failed to intiate job') {
          this.notificationService.success('Info!', 'Job Initiated Successfully!');
        } else {
          this.notificationService.error('Error!', 'Failed to intiate job!');
        }
      },
        (res: Response) => {
          this.buttonRestrict = false;
          this.notif('error', 'Failed to Initiate Job!');
        }
      )
    }
  }

  public ngOnDestroy() {
    if (this.setIntId) {
      clearInterval(this.setIntId);
    }
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  getDataForSplit(row,type){
    this.isEvenly = 'Evenly';
    this.rowCount = undefined;
    this.splitRowArray = [];
    this.addRowArray  = [];
    this.masterRow = row['dataRowId'];
    this.splitRow = [];
    this.splitRow.push(row);
    this.rowAction = type + ' Row';
    this.showSplit = true;
    this.selectedAmount = row[this.amountQualifierName];
    this.splitCurreny = row[this.currencyQualifierName];
    if(type == 'Add'){
      this.addRowArray.push({
        adjustmentType : 'Add',
        dataViewId : this.dataViewId,
        adjustmentMethod : 'Normal',
        masterRowId : this.masterRow
      })
    }
  }
  updateSplitTable(count){
    this.isEvenly = 'Evenly';
    this.splitRowArray = [];
    for (let i = 0; i < count; i++) {
      this.splitRowArray.push({
        adjustmentType : 'Split',
        dataViewId : this.dataViewId,
        adjustmentMethod: this.isEvenly,
        masterRowId : this.masterRow
      });
    }
    if(this.isEvenly == 'Evenly'){
      for (let i = 0; i < this.splitRowArray.length; i++) {
        this.splitRowArray[i].amount = this.selectedAmount / this.splitRowArray.length
      }
    }
  }

  isEvenlyTrue(){
    if(this.isEvenly == 'Evenly'){
      for (let i = 0; i < this.splitRowArray.length; i++) {
        this.splitRowArray[i].amount = this.selectedAmount / this.splitRowArray.length;
        this.splitRowArray[i].adjustmentMethod = this.isEvenly;
        this.splitRowArray[i].percentValue = undefined;
      }
    }else {
      for (let i = 0; i < this.splitRowArray.length; i++) {
        this.splitRowArray[i].amount = undefined;
        this.splitRowArray[i].adjustmentMethod = this.isEvenly;
      }
    }
  }

  updateSplitAmount(percent,ind){
    this.splitRowArray[ind].amount = (this.selectedAmount * percent) / 100;
  }

  amountRestrict(code) {
    if (code > 31 && code !== 46 && (code < 48 || code > 57) ) {
      return false;
    }
    return true;
  }

  saveSplitRow(){
    if(this.rowAction == 'Split Row'){
      let totalPercent = 0;
      let totalAmount = 0;
      for (let i = 0; i < this.splitRowArray.length; i++) {
        const element = this.splitRowArray[i];
        if(element.adjustmentMethod == 'Percent'){
          totalPercent = totalPercent + element.percentValue;
        }
        totalAmount = totalAmount + element.amount;
      }
      if(this.isEvenly == 'Evenly'){
        if(totalAmount == this.selectedAmount){
          this.accountingDataService.splitRowPost(this.splitRowArray).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.showSplit = false;
            this.getStatusLevelSummary();
          },
            (res: Response) => {
              this.notif('error','Error while splitting the record!');
            }
          )
        } else {
          this.notificationService.info('Info!','Split Amounts should be equal to divisible amount!');
        }
      } else {
        if(totalAmount == this.selectedAmount && totalPercent == 100){
          this.reconcileService.splitRowPost(this.splitRowArray).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.showSplit = false;
            this.getStatusLevelSummary();
          },
            (res: Response) => {
              this.notif('error','Error while splitting the record!');
            }
          )
        } else {
          this.notificationService.info('Info!','Split Amounts should be equal to divisible amount!');
        }
      }
      
    } else {
      this.reconcileService.splitRowPost(this.addRowArray).takeUntil(this.unsubscribe).subscribe((res: any) => {
        this.showSplit = false;
        this.getStatusLevelSummary();
      },
        (res: Response) => {
          this.notif('error','Error while adding the record!');
        }
      )
    }
    
  }

  
  toggleRange(e) {
    this.selectedMoreRange = false;
    console.log('range ' + e.value);
    let selectedRange: any;
    if (e.value == 'Today') {
      selectedRange = [moment(), moment()];
    } else if (e.value == '7Days') {
      selectedRange = [moment().subtract(6, 'days'), moment()];
    } else if (e.value == '14Days') {
      selectedRange = [moment().subtract(13, 'days'), moment()];
    } else if (e.value == '30Days') {
      selectedRange = [moment().subtract(29, 'days'), moment()];
    } else if (e.value == '2Months') {
      selectedRange = [moment().subtract(1, 'month').startOf('month'), moment()];
    } else if (e.value == '3Months') {
      selectedRange = [moment().subtract(2, 'month').startOf('month'), moment()];
    } else if (e.value == '6Months') {
      selectedRange = [moment().subtract(5, 'month').startOf('month'), moment()];
    } else {
      if(this.dateDifference == 0){
        selectedRange = [moment(), moment()];
      } else if(this.endDateDif == 0){
        selectedRange = [moment().subtract(this.dateDifference-1, 'days'), moment()];
      } else {
        selectedRange = [moment().subtract(this.dateDifference-1, 'days'), moment().subtract(this.endDateDif-1, 'days')];
      }
      
    }
    console.log('selectedRange ' + selectedRange);
    this.rangeFrom = new Date(selectedRange[0]);
    this.rangeTo = new Date(selectedRange[1]);
    this.loadingAllData();
  }

  changeStartRange() {
    for (let i = 0; i < this.dateRangeList.length; i++) {
      this.dateRangeList[i]['checked'] = false;
      if(this.dateRangeList[i].type == "custom"){
        this.dateRangeList.splice(i,1);
      }
    }
    const oneDay = 24*60*60*1000;
    this.dateDifference = Math.round(Math.abs((this.rangeTo.getTime() - this.rangeFrom.getTime())/(oneDay)));
    console.log('Date Diff: '+ this.dateDifference);
    if (this.dateDifference == 1) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == 'Today')].checked = true;
    } else if (this.dateDifference == 7) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '7Days')].checked = true;
    } else if (this.dateDifference == 14) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '14Days')].checked = true;
    } else if (this.dateDifference == 30) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '30Days')].checked = true;
    } else if (this.dateDifference == 60) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '2Months')].checked = true;
    } else if (this.dateDifference == 90) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '3Months')].checked = true;
    } else if (this.dateDifference == 180) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '6Months')].checked = true;
    } else {
      this.dateRangeList.push({
        checked : true,
        value : this.dateDifference + 'Days',
        title : 'Last ' + this.dateDifference + ' Days',
        name : this.dateDifference + ' Days',
        type : "custom"
      });
    }
    this.loadingAllData();
  }

  changeEndRange() {
    for (let i = 0; i < this.dateRangeList.length; i++) {
      this.dateRangeList[i]['checked'] = false;
      if(this.dateRangeList[i].type == "custom"){
        this.dateRangeList.splice(i,1);
      }
    }
    const oneDay = 24*60*60*1000;
    this.dateDifference = Math.round(Math.abs((this.rangeTo.getTime() - this.rangeFrom.getTime())/(oneDay)));
    this.endDateDif = Math.round(Math.abs((this.systemDate.getTime() - this.rangeTo.getTime())/(oneDay)));
    console.log('End Date Diff: '+ this.endDateDif);
    if (this.dateDifference == 1) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == 'Today')].checked = true;
    } else if (this.dateDifference == 7) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '7Days')].checked = true;
    } else if (this.dateDifference == 14) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '14Days')].checked = true;
    } else if (this.dateDifference == 30) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '30Days')].checked = true;
    } else if (this.dateDifference == 60) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '2Months')].checked = true;
    } else if (this.dateDifference == 90) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '3Months')].checked = true;
    } else if (this.dateDifference == 180) {
      this.dateRangeList[this.dateRangeList.findIndex((x) => x.value == '6Months')].checked = true;
    } else {
      this.dateRangeList.push({
        checked : true,
        value : this.dateDifference + 'Days',
        title : 'Last ' + this.dateDifference + ' Days',
        name : this.dateDifference + ' Days',
        type : "custom"
      });
    }
    this.loadingAllData();
  }

  isAccAllData(){
    this.commonService.isAcctAllData = false;
  }

  unAcctSummPagination(){
    this.groupingSummaryParams.groupId = this.ruleGroupId;
    this.groupingSummaryParams.periodFactor = this.period;
    this.groupingSummaryParams.viewId = this.dataViewId;
    this.groupingSummaryParams.status = this.status;
    this.groupingSummaryParams.rangeFrom = this.datePipe.transform(this.rangeFrom,'yyyy-MM-dd');
    this.groupingSummaryParams.rangeTo = this.datePipe.transform(this.rangeTo,'yyyy-MM-dd');
    this.groupingSummaryParams.pageSize=this.summaryPageEvent.pageSize;
    this.groupingSummaryParams.pageNumber=this.summaryPageEvent.pageIndex;
    this.groupingSummaryParams.groupByColumnName = this.groupingColName;
    this.accountingDataService.getUnAcctSummary(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe((res: any) => {
      this.groupingSummaryParams.groupByColumnName = "";
      this.unAccountedSummary = res.summaryInfo[0].summary;
      this.isAccountedSummary = false;
    },
      (res: Response) => {
        this.notif('error','Error while adding the record!');
      }
    )
  }

  fetchUnAccountedSummary(column?:any,groupBy?:any){
    this.groupingSummaryParams.groupId = this.ruleGroupId;
    this.groupingSummaryParams.periodFactor = this.period;
    this.groupingSummaryParams.viewId = this.dataViewId;
    this.groupingSummaryParams.status = this.status;
    this.groupingSummaryParams.rangeFrom = this.datePipe.transform(this.rangeFrom,'yyyy-MM-dd');
    this.groupingSummaryParams.rangeTo = this.datePipe.transform(this.rangeTo,'yyyy-MM-dd');
    this.groupingSummaryParams.pageSize=this.summaryPageEvent.pageSize;
    this.groupingSummaryParams.pageNumber=this.summaryPageEvent.pageIndex;
    this.accountingDataService.getUnAcctSummary(this.groupingSummaryParams).takeUntil(this.unsubscribe).subscribe((res: any) => {
      this.unAcctSummaryInfo = res.summaryInfo;
      this.unAccountedAlignInfo = res.columnAlignInfo;
      this.unAccountedSummary = this.unAcctSummaryInfo[0].summary;
      this.summaryLength=this.unAcctSummaryInfo[0].totalCount;
      this.isAccountedSummary = false;
      this.columnLabel = this.unAcctSummaryInfo[0].displayName;
      this.columnInd = 0;
      this.getCurrencyColumn = res.currencyColumn;
      this.groupingColName = this.unAcctSummaryInfo[0].groupBy;
      if(this.unAccountedSummary.length > 0){
        if(this.unAccountedSummary[0].groupingColumn === this.unAccountedSummary[0][this.getCurrencyColumn]){
          this.isNotCurrency = false;
        } else {
          this.isNotCurrency = true;
        }
      }
    },
      (res: Response) => {
        this.notif('error','Error while adding the record!');
      }
    )
  }
  changeGroupBy(column, groupBy) {
    this.summaryPageEvent.pageIndex=0;
    this.summaryPageEvent.pageSize=ITEMS_PER_PAGE;
    this.selectedUnAcctSummary = [];
    this.unAccountedSummary = this.unAcctSummaryInfo[this.unAcctSummaryInfo.findIndex((x) => x.displayName == column)].summary;
    this.summaryLength=this.unAcctSummaryInfo[this.unAcctSummaryInfo.findIndex((x) => x.displayName == column)].totalCount;
    this.columnInd = this.unAcctSummaryInfo.findIndex((x) => x.displayName == column);
    this.columnLabel = column;
    this.groupingColName = groupBy;
    if(this.unAccountedSummary.length > 0){
      if(this.unAccountedSummary[0].groupingColumn === this.unAccountedSummary[0][this.getCurrencyColumn]){
        this.isNotCurrency = false;
      } else {
        this.isNotCurrency = true;
      }
    }
  }

  fetchGroupByTrueColumns(){
    this.reconcileService.fetchGroupByColumns(this.dataViewId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.groupingColumns = res;
      },
      (res: Response) => {
        this.onError('Error Occured while fetching data view groupby columns');
      }
    )
  }

  private onError(errorMessage) {
    this.notificationService.error(
      'Error!',
      errorMessage,
      {
        timeOut: 3000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: true,
        maxLength: 100
      }
    )
  }

}





