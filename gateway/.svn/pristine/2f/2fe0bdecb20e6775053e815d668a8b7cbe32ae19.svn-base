import { Component, OnInit, OnDestroy, ChangeDetectorRef, Inject, Directive, Renderer, ViewChild, ElementRef, group } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription, Subject } from 'rxjs/Rx';
import { Response } from '@angular/http';
import { DatePipe } from '@angular/common';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'moment';
import {
  ReconcileBreadCrumbTitles, SplitRowParams, QueryParams, AnaylticParams, ColSearchVal, SuggestedParams, SuggestedSource, SuggestedTarget,
  ReconDataQueries, ReconKeyValues, ReconAmounts, GroupByColumnValues, ManualReconOject, ManualSReconArray, ManualTReconArray, UnReconKeyValues, ReconciledLines, UnReconFilters
} from './reconcile.model';
import { ReconcileService } from './reconcile.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ENABLE_RULE_BLOCK } from '../../shared';
import { SelectItem } from 'primeng/primeng';
import { CommonService } from '../common.service';
import { RuleGroupService } from '../rule-group/rule-group.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA, PageEvent } from '@angular/material';
import { AmountFormat } from '../amountFormatPipe';
import { SchedulingModel, Parameters, Scheduling } from '../scheduling/scheduling.component';
import { ReconDashBoardObject } from '../../shared/constants/constants.values';
import { EventListener } from '@angular/core/src/debug/debug_node';
import { SessionStorageService } from 'ng2-webstorage';
import { BADFLAGS } from 'dns';
declare var $: any;
declare var jQuery: any;
declare var moment: any;

/* Confirmation Dialog Component */
@Component({
  selector: 'jhi-rwq-confirm-action-modal',
  templateUrl: 'rwq-confirm-action-modal.html'
})
export class WqConfirmActionModalDialogComponent {
  constructor(
    public dialogRef: MdDialogRef<WqConfirmActionModalDialogComponent>,
    public dialog: MdDialog,
    private recService: ReconcileService,
    @Inject(MD_DIALOG_DATA) public data: any) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

/* Reconcile Component */
@Component({
  selector: 'jhi-rwq-reconcile',
  templateUrl: './rwq.component.html'
})
export class RWQComponent implements OnInit, OnDestroy {
  @ViewChild(Scheduling) scheduling: Scheduling;
  private unsubscribe: Subject<void> = new Subject();
  reconRuleGroups: any;
  reconRuleGroupName: string;
  reconRuleGroupId: number;
  sourceDataViews: any[] = [];
  targetDataViews: any[] = [];
  periodFactor: string;
  rangeTo: any;
  rangeFrom: any;
  amountQaulifier: any;
  dateQualifier: any;
  dateQualifierName: string;
  sDateQualifierAlias: any;
  tDateQualifierAlias: any;
  tdateQualifierName: string;
  currencyQualifier: any;
  currentReconStatus = 'unreconciled';
  groupBy: string;
  selectedKeys: any[] = [];
  selectedSource: any[] = [];
  selectedTarget: any[] = [];
  itemsPerPage: any;
  recItemsPerPage: any;
  itemsPerPage1: any;
  sitemsPerPage: any;
  titemsPerPage: any;
  page: any = 0;
  recPage: any = 0;
  page1: any = 0;
  sPage: any = 0;
  tPage: any = 0;
  pageSizeOptions = [10, 25, 50, 100];;
  sortOrderSource: any = "desc";
  sortOrderTarget: any = "desc";
  sourceLines: any[] = [];
  reconciledLines = new ReconciledLines();
  targetLines: any[] = [];
  sourceLength: any;
  targetLength: any;
  sourceDataInfo: any;
  targetDataInfo: any;
  sourceHeaderColumns: any[] = [];
  sourceHeaderColumns1: any;
  targetHeaderColumns: any[] = [];
  targetHeaderColumns1: any;
  sourceColumnOptions: SelectItem[];
  targetColumnOptions: SelectItem[];
  rwReconQueries: ReconDataQueries = {};
  reconKeysObject: ReconKeyValues = {};
  unReconKeysObject: UnReconKeyValues = {};
  selectedSCountAmount: any;
  selectedTCountAmount: any;
  selectedSourceAmount = 0;
  selectedTargetAmount = 0;
  selectedSourceCount: any;
  selectedTargetCount: any;
  selectedDifference: number;
  selectedDifferencePercent: number;
  paramSet: any;
  queryParams: QueryParams;
  reconGroupByAnalytics: any[] = [];
  unReconGroupByAnalytics: any[] = [];
  showReconAnalytics = false;
  setIntId: any;
  callSetInt = false;
  groupingColumnValues: GroupByColumnValues = {};
  unReconFilterData: UnReconFilters;
  reconJobId: string;
  reconJobOutput: any
  reconJobStatus: any;
  manualReconArray: any[] = [];
  manualRqObject: ManualReconOject = {};
  manualRSource: ManualSReconArray[] = [];
  manualRTarget: ManualTReconArray[] = [];
  sourceRecordRCount = 0;
  sourceRecordUCount = 0;
  isUnReconciled = false;
  isReconciled = false;
  create: string;
  runAcctShow = false;
  schReconShow = false;
  acctRuleGroups: any;
  acctRuleGroupId: any;
  acctJobOutput: any;
  acctJobId: any;
  acctJobStatus: any;
  acctParamSet: any;
  acctJobName: any = 'Accounting Job';
  reconSchedObj = new SchedulingModel();
  reconSchedParamms: Parameters[] = [];
  reconFileExpList: any[] = [];
  sourceFileExpObject: any;
  showFilterView = false;
  targetFileExpObjext: any;
  formConfigObject: any = {};
  formFilterArray: any[] = [];
  buttonRestrict = false;
  isViewOnly = false;
  isVisibleA = true;
  selectedChildTab: number;
  // selectedAmountTab: number;
  selectedParentTab: number;
  sourceDataViewId: any;
  targetDataViewId: any;
  sourceViewName: any;
  targetViewName: any;
  viewColumns: any;
  reconAnalytics: any[] = [];
  unReconAnalytics: any[] = [];
  statisticsParams: AnaylticParams;
  isSummary = false;
  recAmts: ReconAmounts = {};
  columnFilter: ColSearchVal[] = [];
  showStart: any;
  showEnd: any;
  sourceSearchWord: any;
  targetSearchWord: any;
  selectedParams: any = {};
  approvalRuleGroups: any;
  approvalGroupId: any;
  approvalGroup: any;
  sViewColumns: any[] = [];
  tViewColumns: any[] = [];
  tAmountQualifier: number;
  tCurrencyQualifier: number;
  tDateQualifier: number;
  sCurrencyQualifierName: string;
  sCurrencyAliasName: string;
  tCurrencyAliasName: string;
  tCurrencyQualifierName: string;
  sAmountQualifierName: string;
  sAmountQualifierNameWithTemp: string;
  defaultSortField = "recon_reference";
  tAmountQualifierName: string;
  tAmountQualifierNameWithTemp: string;
  sourceRAmount: any;
  targetRCount: any;
  sourceRCount: any;
  sourceUCount: any;
  targetUCount: any;
  sourceSAmount: any;
  selectedMoreRange: any;
  a: any;
  b: any;
  c: any;
  d: any;
  isVisibleC: boolean;
  rulesData: any[] = [];
  batchData: any[] = [];
  daysData: any[] = [];
  recColumnNameData: any[] = [];
  unRecColumnNameData: any[] = [];
  approvalStatusData: any[] = [];
  approvalRuleData: any[] = [];
  approvalDateData: any[] = [];
  suggestedRulesData: any[] = [];
  suggestedBatchData: any[] = [];
  suggestedColumnData: any[] = [];
  rulesDataLength = 0;
  batchDataLength = 0;
  daysDataLength = 0;
  recColumnNameDataLength = 0;
  unRecColumnNameDataLength = 0;
  approvalStatusDataLength = 0;
  approvalRuleDataLength = 0;
  approvalDateDataLength = 0;
  suggestedRulesDataLength = 0;
  suggestedBatchDataLength = 0;
  suggestedColumnDataLength = 0;
  viewAttachment = false;
  isRejected = false;
  showTFilters = true;
  showSFilters = true;
  isApproval: any;
  isChecked = false;
  isAnalytics = true;
  sDataType: any = 'DECIMAL';
  tDataType: any = 'DECIMAL';
  sortEnabled = false;
  selectedSSource: any[] = [];
  selectedSTarget: any[] = [];
  suggestSource: SuggestedSource[] = [];
  suggestTarget: SuggestedTarget[] = [];
  suggestParams: SuggestedParams = {};
  sourceColumnsByruleId: any;
  sourceColumns1ByRuleId: any[] = [];
  varianceColumnsByRuleId: any[] = [];
  sourceOptionsByRuleId: any[] = [];
  targetColumnsByruleId: any;
  targetColumns1ByRuleId: any[] = [];
  targetOptionsByRuleId: any[] = [];
  totalLines: any = {};
  isCustomFilter = false;
  reconciledTarget: any[] = [];
  reconciledSource: any[] = [];
  reconciledTargetLength: any;
  reconciledSourceLength: any;
  showReconciliation = false;
  sVariances: any;
  tVariances: any;
  isVariance: any;
  oozieTestRecon: any;
  dateRange: any;
  displayConfirmDialog = false;
  exportRes: any;
  showSplit = false;
  rowAction: any;
  splitRow: any[] = [];
  isEvenly = "Evenly";
  rowCount: number;
  selectedAmount: any;
  splitRowArray: SplitRowParams[] = [];
  addRowArray: SplitRowParams[] = [];
  splitType: any;
  masterRow: any;
  splitCurreny: any;
  rowAmount = 0;
  selectedSigleRow: any = {};
  selectedExpandRow: any = {};
  totalData: any;
  reconciledLength: any;
  reconSearchWord: any;
  reconciledRecords: any[] = [];
  isReconData = false;
  isFullScreen = false;
  dateQualifierType: any;
  reconDaysData: any[] = [];
  sourceRecLines: any[] = [];
  targetRecLines: any[] = [];
  groupingColumns: any[] = [];
  reconDaysDataLength = 0;
  sourceRecLinesDataLength = 0;
  targetRecLinesDataLength = 0;
  groupingColumnsDataLength = 0;
  summaryPageEvent: PageEvent = new PageEvent();
  recSummaryLength = 0;
  unRecSummaryLength = 0;
  summaryPageSizeOptions = [10, 25, 50, 100];
  headerSortField = 'reconReference';
  showPageSize = true;
  showFirstLastButtons = true;
  periodsList: any[] = [];
  unReconAction = false;
  dateRangeList = [
    {
      "value": "Today",
      "name": "1D",
      "checked": false,
      "title": "Today",
      "type": "default"
    },
    {
      "value": "7Days",
      "name": "1W",
      "checked": false,
      "title": "Last 7 Days",
      "type": "default"
    },
    {
      "value": "14Days",
      "name": "2W",
      "checked": false,
      "title": "Last 14 Days",
      "type": "default"
    },
    {
      "value": "30Days",
      "name": "1M",
      "checked": true,
      "title": "Last Month",
      "type": "default"
    },
    {
      "value": "2Months",
      "name": "2M",
      "checked": false,
      "title": "Last 2 Months",
      "type": "default"
    },
    {
      "value": "3Months",
      "name": "3M",
      "checked": false,
      "title": "Last 3 Months",
      "type": "default"
    },
    {
      "value": "6Months",
      "name": "6M",
      "checked": false,
      "title": "Last 6 Months",
      "type": "default"
    }
  ];
  unReconGroupBy: any[] = [];
  columnInd: any;
  columnLabel = "";
  columnName = "";
  isNotCurrency = true;
  dateDifference: any;
  endDateDif: any;
  systemDate = new Date();
  constructor(
    private ruleGroupService: RuleGroupService,
    public reconcileService: ReconcileService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private commonService: CommonService,
    private jobDetailsService: JobDetailsService,
    public dialog: MdDialog,
    private datePipe: DatePipe,
    private cdr: ChangeDetectorRef,
    private renderer: Renderer,
    private session: SessionStorageService,
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.itemsPerPage1 = ITEMS_PER_PAGE;
    this.sitemsPerPage = ITEMS_PER_PAGE;
    this.titemsPerPage = ITEMS_PER_PAGE;
    this.recItemsPerPage = ITEMS_PER_PAGE;
    this.summaryPageEvent.pageIndex = 0;
    this.summaryPageEvent.pageSize = ITEMS_PER_PAGE;
    this.sourceHeaderColumns = [];
    this.targetHeaderColumns = [];
  }

  /*FUNCTION1 - Change Status Functionality*/
  /* Author: BHAGATH */
  changeStatus(e) {
    this.isNotCurrency = true;
    this.selectedKeys = [];
    this.isChecked = false;
    this.isAnalytics = true;
    this.summaryPageEvent.pageIndex = 0;
    this.summaryPageEvent.pageSize = ITEMS_PER_PAGE;
    if (!e || !e.index) {
      this.selectedParentTab = 0;
    } else {
      this.selectedParentTab = e.index;
    }
    if (this.selectedParentTab == 0) {
      this.currentReconStatus = "reconciled";
      this.groupBy = "rules";
      this.reconGroupByAnalytics = [];
      this.reconGroupByAnalytics = this.reconAnalytics[0].summary;
      this.recSummaryLength = this.reconAnalytics[0].info.totalCount;
      this.showSFilters = false;
      this.showTFilters = false;
    } else if (this.selectedParentTab == 1) {
      this.currentReconStatus = "unreconciled";
      this.groupBy = "days";
      this.unReconGroupByAnalytics = [];
      this.unReconGroupByAnalytics = this.unReconAnalytics[0].summary;
      this.unRecSummaryLength = this.unReconAnalytics[0].info.totalCount;
      this.showSFilters = true;
      this.showTFilters = true;
    } else if (this.selectedParentTab == 2) {
      this.currentReconStatus = "suggestion";
      this.groupBy = "rules";
      this.showSFilters = true;
      this.showTFilters = true;
    }
    if (!this.sourceHeaderColumns && this.sourceHeaderColumns.length == 0) {
      this.getColumnHeaders(this.reconRuleGroupId, this.sourceDataViewId, 'source', 'summary');
    }
    if (!this.targetHeaderColumns && this.targetHeaderColumns.length == 0) {
      this.getColumnHeaders(this.reconRuleGroupId, this.targetDataViewId, 'target', 'summary');
    }
  }

  amountRestrict(code) {
    if (code > 31 && code !== 46 && (code < 48 || code > 57)) {
      return false;
    }
    return true;
  }

  isRecAllData() {
    this.commonService.reconDashBoardObject = new ReconDashBoardObject();
    this.commonService.isRecAllData = false;
  }

  /*FUNCTION2 - Change group by functionality*/
  /* Author: BHAGATH */
  changeGroupBy(groupBy, column?: any) {
    this.isNotCurrency = true;
    this.selectedKeys = [];
    this.isChecked = false;
    this.groupBy = groupBy;
    this.summaryPageEvent.pageIndex = 0;
    this.summaryPageEvent.pageSize = ITEMS_PER_PAGE;
    this.getColumnHeaders(this.reconRuleGroupId, this.sourceDataViewId, 'source', 'summary');
    this.getColumnHeaders(this.reconRuleGroupId, this.targetDataViewId, 'target', 'summary');
    if (this.currentReconStatus == 'reconciled') {
      if (groupBy != 'columnName') {
        this.reconGroupByAnalytics = [];
        this.fetchReconCountAmount(this.currentReconStatus, groupBy);
      } else {
        this.reconGroupByAnalytics = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.displayName == column)].summary;
        if (this.reconGroupByAnalytics[this.reconGroupByAnalytics.length - 1]['groupBy']) {
          this.unReconGroupBy = this.reconGroupByAnalytics.splice(this.reconGroupByAnalytics.length - 1, 1);
        }
        this.currencyQualifier = this.groupingColumns[this.groupingColumns.findIndex((x) => x.displayName == column)].columnId;
        this.columnName = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.displayName == column)].groupBy;
        this.recSummaryLength = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.displayName == column)].info.totalCount;
        this.columnInd = this.recColumnNameData.findIndex((x) => x.displayName == column);
        this.columnLabel = column;
        if (this.reconGroupByAnalytics.length > 0) {
          if (this.reconGroupByAnalytics[0].name === this.reconGroupByAnalytics[0].currency) {
            this.isNotCurrency = false;
          } else {
            this.isNotCurrency = true;
          }
        }
      }
    } else if (this.currentReconStatus == 'unreconciled') {
      if (groupBy != 'columnName') {
        this.unReconGroupByAnalytics = [];
        this.unReconGroupByAnalytics = this.unReconAnalytics[0].summary;
        this.unRecSummaryLength = this.unReconAnalytics[0].info.totalCount;
      } else {
        this.unReconGroupByAnalytics = this.unRecColumnNameData[this.unRecColumnNameData.findIndex((x) => x.displayName == column)].summary;
        this.unReconGroupBy = this.reconGroupByAnalytics.splice(this.reconGroupByAnalytics.length - 1, 1);
        this.currencyQualifier = this.groupingColumns[this.groupingColumns.findIndex((x) => x.displayName == column)].columnId;
        this.columnName = this.unRecColumnNameData[this.unRecColumnNameData.findIndex((x) => x.displayName == column)].groupBy;
        this.unRecSummaryLength = this.unRecColumnNameData[this.unRecColumnNameData.findIndex((x) => x.displayName == column)].info.totalCount;
        this.columnInd = this.unRecColumnNameData.findIndex((x) => x.displayName == column);
        this.columnLabel = column;
        if (this.unReconGroupByAnalytics.length > 0) {
          if (this.unReconGroupByAnalytics[0].name === this.unReconGroupByAnalytics[0].currency) {
            this.isNotCurrency = false;
          } else {
            this.isNotCurrency = true;
          }
        }
      }
    }
  }

  /*FUNCTION4 - Show Batch Wise Counts against reconcile/unreconcile button*/
  /* Author: BHAGATH */
  showBatchWiseCounts() {
    this.sourceRecordRCount = 0;
    this.sourceRecordUCount = 0;
    if (this.currentReconStatus == 'reconciled') {
      if (this.reconGroupByAnalytics.length > 0) {
        if (this.selectedKeys.length > 0) {
          for (let i = 0; i < this.selectedKeys.length; i++) {
            const item = this.selectedKeys[i];
            for (let j = 0; j < this.reconGroupByAnalytics.length; j++) {
              const element = this.reconGroupByAnalytics[j];
              if (item.name == element.name && item.currency == element.currency) {
                this.sourceRecordRCount = this.sourceRecordRCount + element.dsCount;
              }
            }

          }
        }

      }
    } else {
      if (this.unReconGroupByAnalytics.length > 0) {
        if (this.selectedKeys.length > 0) {
          for (let i = 0; i < this.selectedKeys.length; i++) {
            const item = this.selectedKeys[i];
            for (let j = 0; j < this.unReconGroupByAnalytics.length; j++) {
              const element = this.unReconGroupByAnalytics[j];
              if (item.name == element.name && item.currency == element.currency) {
                this.sourceRecordRCount = this.sourceRecordRCount + element.dsCount;
              }
            }

          }
        }

      }
    }
  }

  /*FUNCTION4 -  Function to get Rule Groups STARTS*/
  /* Author: BHAGATH */

  fetchRuleGroups() {
    this.ruleGroupService.fetchRuleGroupsBypurpose('RECONCILIATION').takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.reconRuleGroups = res;
        if (this.reconRuleGroups.length > 0) {
          this.fetchQueryParams(this.reconRuleGroupId);
        } else {
          this.commonService.info("No Reconciliation Processes Found!", "Please create a new Process");
        }
      },
      (res: Response) => {
        this.onError('Error Occured while fetching reconciliation processess!')
      }
    );
  }

  /*FUNCTION6 -  Function to get query parameters STARTS*/
  /* Author: BHAGATH */
  fetchQueryParams(groupId) {
    this.selectedKeys = [];
    if (this.reconRuleGroups.length > 0) {
      this.isApproval = this.reconRuleGroups[this.reconRuleGroups.findIndex((x) => x.id == groupId)].apprRuleGrpId;
      this.approvalGroup = this.reconRuleGroups[this.reconRuleGroups.findIndex((x) => x.id == groupId)].apprRuleGrpId;
    }
    this.reconcileService.fetchQueryParams(groupId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.sourceDataViews = this.queryParams.source;
        this.periodsList = this.sourceDataViews[this.sourceDataViews.findIndex((x) => x.viewId == this.sourceDataViewId)].srcDateQualifiers;
        for (let i = 0; i < this.periodsList.length; i++) {
          this.periodsList[i]['checked'] = false;
        }
        this.periodsList[0]['checked'] = true;
        if (this.commonService.reconDashBoardObject.periodFactor) {
          this.periodFactor = this.commonService.reconDashBoardObject.periodFactor;
        } else {
          this.periodFactor = this.periodsList[0].columnName;
        }
        this.dateQualifierType = this.periodsList[0].dateQualifierType;
        this.targetDataViews = this.queryParams.target;
      },
      (res: Response) => {
        this.onError('Error Occured while fetching Query Parameters!');
      }
    );
    this.ruleGroupService.sourceDataViewId = this.sourceDataViewId;
    this.ruleGroupService.sourceDataView = this.sourceViewName;
    this.ruleGroupService.targetDataViewId = this.targetDataViewId;
    this.ruleGroupService.targetDataView = this.targetViewName;
    this.loadingAllData();
  }

  /*FUNCTION5 -  Function to get Accounting rule groups STARTS*/
  /* Author: BHAGATH */

  fetchAcctRuleGroups(viewId, purpose) {
    this.ruleGroupService.fetchingGroupsByViewId(viewId, purpose).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        if (purpose == 'ACCOUNTING') {
          this.acctRuleGroups = res;
          if (this.acctRuleGroups.length > 0) {
            this.acctRuleGroupId = this.acctRuleGroups[0].groupId;
            this.runAcctShow = true;
          } else {
            this.runAcctShow = false;
            this.commonService.info('Info!', 'No Eligble Accounting Processes Found!');
          }
        } else {
          this.approvalRuleGroups = res;
          if (this.approvalRuleGroups.length > 0) {
            this.approvalGroupId = this.isApproval;
            this.isRejected = true;
          } else {
            this.isRejected = false;
            this.commonService.info('Info!', 'No Eligble Approval Processes Found!');
          }
        }


      },
      (res: Response) => {
        this.onError('Error Occured while fetching Accounting Rule Groups!')
      }
    );
  }

  getGroupByViewColumns() {
    this.reconcileService.fetchGroupByColumns(this.sourceDataViewId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.groupingColumns = res;
      },
      (res: Response) => {
        this.onError('Error Occured while fetching data view groupby columns');
      }
    )
  }

  loadingAllData() {
    this.reconcileService.getQualifierColumns(this.sourceDataViewId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.sViewColumns = res;
        this.amountQaulifier = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnId;
        this.sAmountQualifierName = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnDisplayName;
        this.sAmountQualifierNameWithTemp = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnName;
        this.currencyQualifier = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnId;
        this.sCurrencyQualifierName = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnDisplayName;
        this.sCurrencyAliasName = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnName;
        this.dateQualifier = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnId;
        this.dateQualifierName = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnDisplayName;
        this.sDateQualifierAlias = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnName;
        this.reconcileService.getQualifierColumns(this.targetDataViewId).takeUntil(this.unsubscribe).subscribe(
          (targetQualRes: any) => {
            this.tViewColumns = targetQualRes;
            this.tAmountQualifier = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnId;
            this.tAmountQualifierName = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnDisplayName;
            this.tAmountQualifierNameWithTemp = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'AMOUNT')].columnName;
            this.tCurrencyQualifier = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnId;
            this.tCurrencyQualifierName = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnDisplayName;
            this.tCurrencyAliasName = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'CURRENCYCODE')].columnName;
            this.tDateQualifier = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnId;
            this.tdateQualifierName = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnDisplayName;
            this.tDateQualifierAlias = this.tViewColumns[this.tViewColumns.findIndex((x) => x.qualifier == 'TRANSDATE')].columnName;
            this.getGroupByViewColumns();
            this.getColumnHeaders(this.reconRuleGroupId, this.sourceDataViewId, 'source', 'summary');
            this.getColumnHeaders(this.reconRuleGroupId, this.targetDataViewId, 'target', 'summary');
            this.loadingDefaultCountAmount();
          },
          (targetQualRes: Response) => {
            this.onError('Error Occured while fetching data view qualifiers!'
            )
          }
        )
      },
      (res: Response) => {
        this.onError('Error Occured while fetching data view qualifiers!'
        )
      }
    );
  }
  loadingDefaultCountAmount() {
    this.fetchReconCountAmount('unreconciled', 'days');
    this.fetchReconCountAmount('reconciled', 'rules');
    this.fetchReconCountAmount('unreconciled', 'columnName');
    this.fetchReconCountAmount('reconciled', 'columnName');
  }

  onChangeRuleGroup(groupId) {
    if (this.reconRuleGroups.length > 0) {
      this.isApproval = this.reconRuleGroups[this.reconRuleGroups.findIndex((x) => x.id == groupId)].apprRuleGrpId;
      this.approvalGroup = this.reconRuleGroups[this.reconRuleGroups.findIndex((x) => x.id == groupId)].apprRuleGrpId;
    }
    this.reconcileService.fetchQueryParams(groupId).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.commonService.reconDashBoardObject = new ReconDashBoardObject();
        this.reconRuleGroupName = this.queryParams.ruleGroupName;
        this.sourceDataViews = this.queryParams.source;
        this.targetDataViews = this.queryParams.target;
        this.sourceDataViewId = this.sourceDataViews[0].viewId;
        this.periodsList = this.sourceDataViews[this.sourceDataViews.findIndex((x) => x.viewId == this.sourceDataViewId)].srcDateQualifiers;
        for (let i = 0; i < this.periodsList.length; i++) {
          this.periodsList[i]['checked'] = false;
        }
        this.periodsList[0]['checked'] = true;
        this.periodFactor = this.periodsList[0].columnName;
        this.dateQualifierType = this.periodsList[0].dateQualifierType;
        this.targetDataViewId = this.sourceDataViews[0].innerTarget[0].viewId;
        this.loadingAllData();
      },
      (res: Response) => {
        this.onError('Error Occured while fetching Query Parameters!');
      }
    );
  }

  onChangeSource(sViewId) {
    this.commonService.reconDashBoardObject = new ReconDashBoardObject();
    this.sourceViewName=this.sourceDataViews[this.sourceDataViews.findIndex((x) => x.viewId == sViewId)].viewName;
    this.periodsList = this.sourceDataViews[this.sourceDataViews.findIndex((x) => x.viewId == sViewId)].srcDateQualifiers;
    for (let i = 0; i < this.periodsList.length; i++) {
      this.periodsList[i]['checked'] = false;
    }
    this.periodsList[0]['checked'] = true;
    this.periodFactor = this.periodsList[0].columnName;
    this.dateQualifierType = this.periodsList[0].dateQualifierType;
    this.targetDataViews = this.sourceDataViews[this.sourceDataViews.findIndex((x) => x.viewId == sViewId)].innerTarget;
    this.targetDataViewId = this.targetDataViews[0].viewId;
    this.targetViewName=this.targetDataViews[this.targetDataViews.findIndex((x) => x.viewId == this.targetDataViewId)].viewName;
    this.loadingAllData();
  }

  onChangeTarget(tViewId) {
    this.commonService.reconDashBoardObject = new ReconDashBoardObject();
    this.targetViewName=this.targetDataViews[this.targetDataViews.findIndex((x) => x.viewId == tViewId)].viewName;
    this.loadingAllData();
  }



  checkAll(event) {
    this.sourceRecordRCount = 0;
    this.selectedKeys = [];
    if (event == true) {
      this.showBatchWiseCounts();
      if (this.currentReconStatus == 'reconciled') {
        for (let i = 0; i < this.reconGroupByAnalytics.length; i++) {
          this.selectedKeys.push(this.reconGroupByAnalytics[i]);
        }
        this.getReconciledRecords();
      } else if (this.currentReconStatus == 'unreconciled') {
        for (let i = 0; i < this.unReconGroupByAnalytics.length; i++) {
          this.selectedKeys.push(this.unReconGroupByAnalytics[i]);
        }
        this.loadData();
      }
      this.showBatchWiseCounts();

    } else {
      this.sourceRecordRCount = 0;
      this.selectedKeys = [];
    }
  }

  onSummaryPagination(status, groupby, column) {
    this.buttonRestrict = true;
    this.isChecked = false;
    this.selectedKeys = [];
    this.isSummary = false;
    this.statisticsParams = {};
    this.statisticsParams.groupBy = groupby;
    this.statisticsParams.periodFactor = this.periodFactor;
    this.statisticsParams.status = status;
    this.statisticsParams.ruleGroupId = this.reconRuleGroupId;
    this.statisticsParams.sViewId = this.sourceDataViewId;
    this.statisticsParams.tViewId = this.targetDataViewId;
    this.statisticsParams.pageNumber = this.summaryPageEvent.pageIndex;
    this.statisticsParams.pageSize = this.summaryPageEvent.pageSize;
    this.statisticsParams.groupByColumnName = column;
    if (groupby == 'days') {
      this.statisticsParams.dateQualifierType = this.dateQualifierType;
      this.statisticsParams.sColumnName = this.sDateQualifierAlias;
      this.statisticsParams.tColumnName = this.tDateQualifierAlias;
    }
    const selectedCurrency = {};
    this.statisticsParams.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
    this.statisticsParams.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
    this.reconcileService.fetchRqCountAmount(this.statisticsParams).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        $(".group-by md-tab").removeAttr('disabled');
        if (status == 'reconciled') {
          this.reconGroupByAnalytics = res[0].summary;
          this.unReconGroupBy = this.reconGroupByAnalytics.splice(this.reconGroupByAnalytics.length - 1, 1);
        } else {
          this.unReconGroupByAnalytics = res[0].summary;
        }
        this.isAnalytics = true;
        this.showReconAnalytics = true;
        this.isSummary = true;
        this.callSetInt = true;
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError('Error Occured while fetching recon analytics!');
      }
    );
  }

  /*FUNCTION10 - Function to get Counts and Amounts Functionality*/
  /* Author: BHAGATH */
  fetchReconCountAmount(status, groupby) {
    this.buttonRestrict = true;
    this.isChecked = false;
    this.isSummary = false;
    this.statisticsParams = {};
    this.statisticsParams.groupBy = groupby;
    this.statisticsParams.periodFactor = this.periodFactor;
    this.statisticsParams.status = status;
    this.statisticsParams.ruleGroupId = this.reconRuleGroupId;
    this.statisticsParams.sViewId = this.sourceDataViewId;
    this.statisticsParams.tViewId = this.targetDataViewId;
    this.statisticsParams.pageNumber = this.summaryPageEvent.pageIndex;
    this.statisticsParams.pageSize = this.summaryPageEvent.pageSize;
    if (groupby == 'days') {
      this.statisticsParams.dateQualifierType = this.dateQualifierType;
      this.sDateQualifierAlias = this.sViewColumns[this.sViewColumns.findIndex((x) => x.qualifier == this.dateQualifierType)].columnName;
      this.statisticsParams.sColumnName = this.sDateQualifierAlias;
      this.statisticsParams.tColumnName = this.tDateQualifierAlias;
    }
    this.sourceRecordRCount = 0;
    this.sourceRecordUCount = 0;
    let selectedCurrency = {};
    this.statisticsParams.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
    this.statisticsParams.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
    this.reconcileService.fetchRqCountAmount(this.statisticsParams).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        $(".group-by md-tab").removeAttr('disabled');
        if (status == 'reconciled') {
          if (groupby != 'columnName') {
            this.reconAnalytics = res;
            this.reconGroupByAnalytics = this.reconAnalytics[0].summary;
            this.unReconGroupBy = this.reconGroupByAnalytics.splice(this.reconGroupByAnalytics.length - 1, 1);
            this.recSummaryLength = this.reconAnalytics[0].info.totalCount;
          }
          this.sourceRCount = this.reconAnalytics[0].info.totalSrcCount;
          this.targetRCount = this.reconAnalytics[0].info.totalTrCount;
        } else {
          if (groupby != 'columnName') {
            this.unReconAnalytics = res;
            this.unReconGroupByAnalytics = this.unReconAnalytics[0].summary;
            this.unRecSummaryLength = this.unReconAnalytics[0].info.totalCount;
          }
          this.sourceUCount = this.unReconAnalytics[0].info.totalSrcCount;
          this.targetUCount = this.unReconAnalytics[0].info.totalTrCount;
        }
        if (groupby == 'columnName' && status == 'reconciled') {
          this.recColumnNameData = res;
          this.recColumnNameDataLength = this.recColumnNameData[0].info.totalCount;
          if (this.unReconAction && this.unReconGroupBy && this.unReconGroupBy[0]['groupBy']) {
            this.reconGroupByAnalytics = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.groupBy == this.unReconGroupBy[0]['groupBy'])].summary;
            this.unReconGroupBy = this.reconGroupByAnalytics.splice(this.reconGroupByAnalytics.length - 1, 1);
            this.currencyQualifier = this.groupingColumns[this.groupingColumns.findIndex((x) => x.columnName == this.unReconGroupBy[0]['groupBy'])].columnId;
            this.columnName = this.unReconGroupBy[0]['groupBy'];
            this.recSummaryLength = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.groupBy == this.unReconGroupBy[0]['groupBy'])].info.totalCount;
            this.columnInd = this.recColumnNameData.findIndex((x) => x.groupBy == this.unReconGroupBy[0]['groupBy']);
            this.columnLabel = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.groupBy == this.unReconGroupBy[0]['groupBy'])].displayName;;
            if (this.reconGroupByAnalytics.length > 0) {
              if (this.reconGroupByAnalytics[0].name === this.reconGroupByAnalytics[0].currency) {
                this.isNotCurrency = false;
              } else {
                this.isNotCurrency = true;
              }
            }
          }
          if (this.commonService.reconDashBoardObject.columnName) {
            if (this.commonService.reconDashBoardObject.status == 'reconciled') {
              this.reconGroupByAnalytics = this.recColumnNameData[this.recColumnNameData.findIndex((x) => x.displayName == this.commonService.reconDashBoardObject.columnDisplayName)].summary;
              this.unReconGroupBy = this.reconGroupByAnalytics.splice(this.reconGroupByAnalytics.length - 1, 1);
              this.currencyQualifier = this.groupingColumns[this.groupingColumns.findIndex((x) => x.displayName == this.commonService.reconDashBoardObject.columnDisplayName)].columnId;
              this.columnInd = this.recColumnNameData.findIndex((x) => x.displayName == this.commonService.reconDashBoardObject.columnDisplayName);
              this.columnLabel = this.commonService.reconDashBoardObject.columnDisplayName;
              this.columnName = this.commonService.reconDashBoardObject.columnName;
              if (this.reconGroupByAnalytics.length > 0) {
                if (this.reconGroupByAnalytics[0].name === this.reconGroupByAnalytics[0].currency) {
                  this.isNotCurrency = false;
                } else {
                  this.isNotCurrency = true;
                }
                if (this.commonService.reconDashBoardObject.columnValue) {
                  selectedCurrency = this.reconGroupByAnalytics[this.reconGroupByAnalytics.findIndex((x) => x.name == this.commonService.reconDashBoardObject.columnValue)];
                  this.selectedKeys.push(selectedCurrency);
                  this.getReconciledRecords();
                  this.showBatchWiseCounts();
                }
              }
            }
          }
        } else if (groupby == 'columnName' && status == 'unreconciled') {
          this.unRecColumnNameData = res;
          this.unRecColumnNameDataLength = this.unRecColumnNameData[0].info.totalCount;
          if (this.commonService.reconDashBoardObject.columnName) {
            if (this.commonService.reconDashBoardObject.status == 'unreconciled') {
              this.unReconGroupByAnalytics = this.unRecColumnNameData[this.unRecColumnNameData.findIndex((x) => x.displayName == this.commonService.reconDashBoardObject.columnDisplayName)].summary;
              this.currencyQualifier = this.groupingColumns[this.groupingColumns.findIndex((x) => x.displayName == this.commonService.reconDashBoardObject.columnDisplayName)].columnId;
              this.columnInd = this.unRecColumnNameData.findIndex((x) => x.displayName == this.commonService.reconDashBoardObject.columnDisplayName);
              this.columnLabel = this.commonService.reconDashBoardObject.columnDisplayName;
              if (this.unReconGroupByAnalytics.length > 0) {
                if (this.unReconGroupByAnalytics[0].name === this.unReconGroupByAnalytics[0].currency) {
                  this.isNotCurrency = false;
                } else {
                  this.isNotCurrency = true;
                }
                if (this.commonService.reconDashBoardObject.columnValue) {
                  selectedCurrency = this.unReconGroupByAnalytics[this.unReconGroupByAnalytics.findIndex((x) => x.name == this.commonService.reconDashBoardObject.columnValue)];
                  this.selectedKeys.push(selectedCurrency);
                  this.loadData();
                  this.showBatchWiseCounts();
                }
              }
            }
          }
        }
        this.rwReconQueries.status = "";
        this.isAnalytics = true;
        this.showReconAnalytics = true;
        this.isSummary = true;
        this.callSetInt = true;
        this.unReconAction = false;
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.unReconAction = false;
        this.onError('Error Occured while fetching recon analytics!');
      }
    );
  }

  loadData() {
    this.getGroupByReconRecords('source');
    this.getGroupByReconRecords('target');
  }

  /*FUNCTION11 - Toggle Query Window Functionality*/
  /* Author: BHAGATH */
  toggleSB() {
    if (!this.isVisibleA) {
      this.isVisibleA = true;
    } else {
      this.isVisibleA = false;
    }
  }

  getSourceTotal(e, ind: number) {
    this.buttonRestrict = true;
    const rowIds = [];
    const recRefs: any[] = [];
    const recReferences: any[] = [];
    if (this.selectedSource.length > 0) {
      for (let i = 0; i < this.selectedSource.length; i++) {
        const item = this.selectedSource[i];
        rowIds.push(item.dataRowId);
      }
      this.reconcileService.fetchCountAmountonSelectSource(this.sourceDataViewId, rowIds).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.selectedSCountAmount = res;
          this.selectedSourceAmount = this.selectedSCountAmount.amount;
          this.selectedSourceCount = this.selectedSCountAmount.count;
          this.selectedDifference = this.selectedSourceAmount - this.selectedTargetAmount;
          this.selectedDifferencePercent = ((this.selectedDifference / this.selectedSourceAmount) * 100);
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Unable to calculate source amount!');
        }
      )
    } else {
      this.getGroupByReconRecords('source');
      this.getGroupByReconRecords('target');
      this.buttonRestrict = false;
      this.selectedSourceCount = "";
      this.selectedSourceAmount = 0;
      this.selectedDifference = this.selectedTargetAmount - this.selectedSourceAmount;
      this.selectedDifferencePercent = ((this.selectedDifference / this.selectedTargetAmount) * 100);
      this.sourceHeaderColumns.forEach((item)=>{
        item.searchWord=undefined;
      });
      this.sourceSearchWord=undefined;
    }
  }

  getTargetTotal(e, ind: number) {
    this.buttonRestrict = true;
    const rowIds: any = [];
    const recRefs: any[] = [];
    if (this.selectedTarget.length > 0) {
      this.selectedTarget.forEach((item) => {
        rowIds.push(item.dataRowId);
      });
      this.reconcileService.fetchCountAmountonSelectSource(this.targetDataViewId, rowIds).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.selectedTCountAmount = res;
          this.selectedTargetAmount = this.selectedTCountAmount.amount;
          this.selectedTargetCount = this.selectedTCountAmount.count;
          this.selectedDifference = this.selectedSourceAmount - this.selectedTargetAmount;
          this.selectedDifferencePercent = ((this.selectedDifference / this.selectedSourceAmount) * 100);
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Unable to calculate source amount!');
        }
      )

    } else {
      $(".source-view-table .ui-chkbox .ui-state-active").closest("tr").removeClass('tr-active');
      $(".traget-view-table .ui-chkbox .ui-state-active").closest("tr").removeClass('tr-active');
      this.getGroupByReconRecords('target');
      this.buttonRestrict = false;
      this.selectedTargetCount = "";
      this.selectedTargetAmount = 0;
      this.selectedDifference = this.selectedSourceAmount - this.selectedTargetAmount;
      this.selectedDifferencePercent = ((this.selectedDifference / this.selectedSourceAmount) * 100);
      this.targetHeaderColumns.forEach((item)=>{
        item.searchWord=undefined;
      });
      this.targetSearchWord=undefined;
    }
  }

  /*FUNCTION15 - Function to Fetch Records of a data view based on grouping */
  /* Author: BHAGATH */
  getGroupByReconRecords(type) {
    this.isChecked = false;
    this.buttonRestrict = true;
    this.cdr.detectChanges();
    this.reconKeysObject.days = [];
    this.reconKeysObject.ruleIds = [];
    this.reconKeysObject.batchNames = [];
    this.reconKeysObject.status = [];
    this.reconKeysObject.columnValues = {};
    this.rwReconQueries.status = this.currentReconStatus;
    this.rwReconQueries.groupId = this.reconRuleGroupId;
    this.rwReconQueries.groupBy = this.groupBy;
    this.rwReconQueries.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');;
    this.rwReconQueries.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');;
    this.rwReconQueries.sourceOrTarget = type;
    this.rwReconQueries.periodFactor = this.periodFactor;
    this.groupingColumnValues.columnValues = [];
    this.groupingColumnValues.currencyValues = [];
    if (this.selectedKeys.length == this.unReconGroupByAnalytics.length) {
      this.isChecked = true;
    }
    if (this.selectedKeys.length > 0) {
      if(type == 'target'){
        for (let i = 0; i < this.selectedKeys.length; i++) {
          if (this.groupBy == 'rules') {
            this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
          } else if (this.groupBy == 'batch') {
            this.reconKeysObject.batchNames.push(this.selectedKeys[i].name);
          } else if (this.groupBy == 'days') {
            const newDate = new Date(this.selectedKeys[i].name);
            const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            this.reconKeysObject.days.push(newDate2);
          } else if (this.groupBy == 'approvalStatus') {
            this.reconKeysObject.status.push(this.selectedKeys[i].name);
          } else if (this.groupBy == 'approvalRule') {
            this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
            this.reconKeysObject.status.push(this.selectedKeys[i].status);
          } else if (this.groupBy == 'approvalDate') {
            const newDate = new Date(this.selectedKeys[i].name);
            const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            this.reconKeysObject.days.push(newDate2);
          } else {
            this.groupingColumnValues.columnId = this.tCurrencyQualifier;
            this.groupingColumnValues.columnValues.push(this.selectedKeys[i].name);
            this.reconKeysObject.columnValues = this.groupingColumnValues;
          }
          this.groupingColumnValues.currencyValues.push(this.selectedKeys[i].currency);
          this.reconKeysObject.columnValues = this.groupingColumnValues;
        }
      }else if(type == 'source'){
        let filters=[];
        for (let i = 0; i < this.selectedKeys.length; i++) {
          if (this.groupBy == 'rules') {
            this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
          } else if (this.groupBy == 'batch') {
            this.reconKeysObject.batchNames.push(this.selectedKeys[i].name);
          } 
          else if (this.groupBy == 'days') {
            const newDate = new Date(this.selectedKeys[i].name);
            const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            let obj={groupingValue:newDate2, currencyValue:this.selectedKeys[i].currency};
            filters.push(obj);
          } 
          else if (this.groupBy == 'approvalStatus') {
            this.reconKeysObject.status.push(this.selectedKeys[i].name);
          } 
          else if (this.groupBy == 'approvalRule') {
            this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
            this.reconKeysObject.status.push(this.selectedKeys[i].status);
          } 
          else if (this.groupBy == 'approvalDate') {
            const newDate = new Date(this.selectedKeys[i].name);
            const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            let obj={groupingValue:newDate2, currencyValue:this.selectedKeys[i].currency};
            filters.push(obj);
          } 
          else {
            let obj={groupingValue:this.selectedKeys[i].name, currencyValue:this.selectedKeys[i].currency};
            filters.push(obj);
            this.groupingColumnValues.columnId = this.currencyQualifier;
            this.reconKeysObject.columnValues = this.groupingColumnValues;
          }
          this.reconKeysObject.columnValues = this.groupingColumnValues;
        }
        this.reconKeysObject.filters=filters;
      }
      // *****************  Old Code  *********************
      // for (let i = 0; i < this.selectedKeys.length; i++) {
        // if (this.groupBy == 'rules') {
        //   this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
        // } else if (this.groupBy == 'batch') {
        //   this.reconKeysObject.batchNames.push(this.selectedKeys[i].name);
        // } else if (this.groupBy == 'days') {
        //   const newDate = new Date(this.selectedKeys[i].name);
        //   const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
        //   this.reconKeysObject.days.push(newDate2);
        // } else if (this.groupBy == 'approvalStatus') {
        //   this.reconKeysObject.status.push(this.selectedKeys[i].name);
        // } else if (this.groupBy == 'approvalRule') {
        //   this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
        //   this.reconKeysObject.status.push(this.selectedKeys[i].status);
        // } else if (this.groupBy == 'approvalDate') {
        //   const newDate = new Date(this.selectedKeys[i].name);
        //   const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
        //   this.reconKeysObject.days.push(newDate2);
        // } else {
        //   if (type == 'source') {
        //     this.groupingColumnValues.columnId = this.currencyQualifier;
        //   } else {
        //     this.groupingColumnValues.columnId = this.tCurrencyQualifier;
        //   }
        //   this.groupingColumnValues.columnValues.push(this.selectedKeys[i].name);
        //   this.reconKeysObject.columnValues = this.groupingColumnValues;
        // }
        // this.groupingColumnValues.currencyValues.push(this.selectedKeys[i].currency);
        // this.reconKeysObject.columnValues = this.groupingColumnValues;
      // }
      // *********************** END of old Code ***************************
      
      if (type == 'source') {
        this.rwReconQueries.keyValues = this.reconKeysObject;
        this.selectedSourceAmount = 0;
        this.selectedSourceCount = "";
        this.selectedSource = [];
        this.rwReconQueries.dataViewId = this.sourceDataViewId;
        this.rwReconQueries.sortOrderBy = this.sortOrderSource;
        this.rwReconQueries.sortByColumnId = this.sAmountQualifierNameWithTemp;
        this.rwReconQueries.dataType = this.sDataType;
        this.sourceFileExpObject = jQuery.extend(true, {}, this.rwReconQueries);
        this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage, this.rwReconQueries).takeUntil(this.unsubscribe).subscribe(
          (res: any) => {
            this.isCustomFilter = false;
            this.buttonRestrict = false;
            this.sourceLines = res;
            this.reconKeysObject.batchNames = [];
            this.groupingColumnValues.columnValues = [];
            this.groupingColumnValues.currencyValues = [];
            this.groupingColumnValues.columnId = "";
            this.reconKeysObject.columnValues = this.groupingColumnValues;
            this.reconKeysObject.days = [];
            this.reconKeysObject.ruleIds = [];
            this.reconKeysObject.status = [];
            if (this.sourceLines.length > 1) {
              this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
              this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
            } else {
              this.commonService.info('Info!', 'No Records Found!');
            }
            this.sortEnabled = false;
          },
          (res: Response) => {
            this.buttonRestrict = false;
            this.onError('Error while fetching source data!');
            this.sortEnabled = false;
          }
        )
      } else if (type == 'target') {
        this.selectedTargetCount = "";
        this.selectedTargetAmount = 0;
        this.selectedTarget = [];
        this.rwReconQueries.dataViewId = this.targetDataViewId;
        this.rwReconQueries.sortOrderBy = this.sortOrderTarget;
        this.rwReconQueries.sortByColumnId = this.tAmountQualifierNameWithTemp;
        this.rwReconQueries.dataType = this.tDataType;
        this.targetFileExpObjext = jQuery.extend(true, {}, this.rwReconQueries);
        this.reconcileService.fetchAllReconWqRecords(this.page1, this.itemsPerPage1, this.rwReconQueries).takeUntil(this.unsubscribe).subscribe(
          (res: any) => {
            this.buttonRestrict = false;
            this.targetLines = res;
            this.reconKeysObject.batchNames = [];
            this.groupingColumnValues.columnValues = [];
            this.groupingColumnValues.columnId = "";
            this.reconKeysObject.columnValues = this.groupingColumnValues;
            this.reconKeysObject.days = [];
            this.reconKeysObject.ruleIds = [];
            if (this.targetLines.length > 1) {
              this.targetLength = this.targetLines[this.targetLines.length - 1].info.totalCount;
              this.targetDataInfo = this.targetLines.splice(this.targetLines.length - 1, 1);
            } else {
              this.commonService.info('Info!', 'No Records Found!');
            }
            this.sortEnabled = false;
          },
          (res: Response) => {
            this.buttonRestrict = false;
            this.onError('Error while fetching source data!');
            this.sortEnabled = false;
          }
        )
      }
    } else {
      this.buttonRestrict = false;
    }

  }

  onRecPaginateChange(event) {
    this.recPage = event.pageIndex;
    this.recItemsPerPage = event.pageSize;
    if (this.selectedKeys.length > 0) {
      this.getReconciledRecords();
    }
  }

  getReconciledRecords() {
    this.isChecked = false;
    this.buttonRestrict = true;
    this.cdr.detectChanges();
    this.reconKeysObject.days = [];
    this.reconKeysObject.ruleIds = [];
    this.reconKeysObject.batchNames = [];
    this.reconKeysObject.status = [];
    this.reconKeysObject.columnValues = {};
    this.rwReconQueries.groupId = this.reconRuleGroupId;
    this.rwReconQueries.groupBy = this.groupBy;
    this.rwReconQueries.sViewId = this.sourceDataViewId;
    this.rwReconQueries.tViewId = this.targetDataViewId;
    this.rwReconQueries.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');;
    this.rwReconQueries.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');;
    this.rwReconQueries.periodFactor = this.periodFactor;
    this.groupingColumnValues.columnValues = [];
    this.groupingColumnValues.currencyValues = [];
    if (this.selectedKeys.length == this.reconGroupByAnalytics.length) {
      this.isChecked = true;
    }
    if (this.selectedKeys.length > 0) {
      let filters:any[]=[];
      for (let i = 0; i < this.selectedKeys.length; i++) {
        if (this.groupBy == 'rules') {
          let obj={groupingValue:this.selectedKeys[i].id, currencyValue:this.selectedKeys[i].currency};
          filters.push(obj);
        } else if (this.groupBy == 'batch') {
          let obj={groupingValue:this.selectedKeys[i].name, currencyValue:this.selectedKeys[i].currency};
          filters.push(obj);
        } else if (this.groupBy == 'days') {
          this.rwReconQueries.dateQualifierType = this.dateQualifierType
          const newDate = new Date(this.selectedKeys[i].name);
          const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          let obj={groupingValue:newDate2, currencyValue:this.selectedKeys[i].currency};
          filters.push(obj);
        } else if (this.groupBy == 'approvalStatus') {
          let obj={groupingValue:this.selectedKeys[i].name, currencyValue:this.selectedKeys[i].currency};
          filters.push(obj);
        } else if (this.groupBy == 'approvalRule') {
          let obj={groupingValue:this.selectedKeys[i].id, currencyValue:this.selectedKeys[i].currency};
          filters.push(obj);
        } else if (this.groupBy == 'approvalDate') {
          const newDate = new Date(this.selectedKeys[i].name);
          const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          let obj={groupingValue:newDate2, currencyValue:this.selectedKeys[i].currency};
          filters.push(obj);
        } else {
          let obj={groupingValue:this.selectedKeys[i].name, currencyValue:this.selectedKeys[i].currency};
          this.groupingColumnValues.sColumnId = this.currencyQualifier;
          filters.push(obj);
        }
        this.reconKeysObject.columnValues = this.groupingColumnValues;
        
      }
      this.reconKeysObject.filters=filters;
      // *************  Old Code  ***************
      // for (let i = 0; i < this.selectedKeys.length; i++) {
      //   if (this.groupBy == 'rules') {
      //     this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
      //   } else if (this.groupBy == 'batch') {
      //     this.reconKeysObject.batchNames.push(this.selectedKeys[i].name);
      //   } else if (this.groupBy == 'days') {
      //     this.rwReconQueries.dateQualifierType = this.dateQualifierType
      //     const newDate = new Date(this.selectedKeys[i].name);
      //     const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
      //     this.reconKeysObject.days.push(newDate2);
      //   } else if (this.groupBy == 'approvalStatus') {
      //     this.reconKeysObject.status.push(this.selectedKeys[i].name);
      //   } else if (this.groupBy == 'approvalRule') {
      //     this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
      //     this.reconKeysObject.status.push(this.selectedKeys[i].status);
      //   } else if (this.groupBy == 'approvalDate') {
      //     const newDate = new Date(this.selectedKeys[i].name);
      //     const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
      //     this.reconKeysObject.days.push(newDate2);
      //   } else {
      //     this.groupingColumnValues.sColumnId = this.currencyQualifier;
      //     this.groupingColumnValues.tColumnId = this.tCurrencyQualifier;
      //     this.groupingColumnValues.columnValues.push(this.selectedKeys[i].name);
      //     this.reconKeysObject.columnValues = this.groupingColumnValues;
      //   }
      //   this.groupingColumnValues.currencyValues.push(this.selectedKeys[i].currency);
      //   this.reconKeysObject.columnValues = this.groupingColumnValues;
      // }
      // ****************************************

      this.rwReconQueries.keyValues = this.reconKeysObject;
      this.selectedSourceAmount = 0;
      this.selectedSourceCount = "";
      this.selectedSource = [];
      this.rwReconQueries.sortOrderBy = this.sortOrderSource;
      this.rwReconQueries.sortByColumnId = this.defaultSortField;
      this.rwReconQueries.dataType = this.sDataType;
      this.sourceFileExpObject = jQuery.extend(true, {}, this.rwReconQueries);
      this.reconcileService.fetchReconciledRecords(this.recPage, this.recItemsPerPage, this.rwReconQueries).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.isReconData = true;
          this.isCustomFilter = false;
          this.buttonRestrict = false;
          this.reconciledLines = res;
          this.reconciledRecords = this.reconciledLines.reconRef;
          this.reconciledLines.target[0]['borderLeft'] = '1px solid #ececec';
          this.reconciledLines.target[1]['borderLeft'] = '0px solid #ececec';
          this.reconciledLines.target[2]['borderLeft'] = '0px solid #ececec';
          this.reconciledLength = this.reconciledLines.info.totalCount;
          this.reconKeysObject.batchNames = [];
          this.groupingColumnValues.columnValues = [];
          this.groupingColumnValues.currencyValues = [];
          this.groupingColumnValues.columnId = "";
          this.reconKeysObject.columnValues = this.groupingColumnValues;
          this.reconKeysObject.days = [];
          this.reconKeysObject.ruleIds = [];
          this.reconKeysObject.status = [];
          if (this.reconciledRecords.length > 0) {
            // this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
            // this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
          } else {
            this.commonService.info('Info!', 'No Records Found!');
          }
          this.sortEnabled = false;
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Error while fetching reconciled data!');
          this.sortEnabled = false;
        }
      )
    } else {
      this.buttonRestrict = false;
    }

  }


  changeSourceSort(eve) {
    for (let i = 0; i < this.sourceHeaderColumns.length; i++) {
      if (this.sourceHeaderColumns[i].field == eve.field) {
        this.sAmountQualifierNameWithTemp = this.sourceHeaderColumns[i].columnName;
        this.sDataType = this.sourceHeaderColumns[i].dataType;
      }
    }
    if (this.sAmountQualifierNameWithTemp == 'original_row_id') {
      this.sAmountQualifierNameWithTemp = 'scrIds';
    }
    if (eve.order < 1) {
      this.sortOrderSource = 'desc';
    } else {
      this.sortOrderSource = 'asc';
    }
    this.getGroupByReconRecords('source');
  }

  changeSort(event) {
    this.headerSortField = event.field;
    if (event.order < 1) {
      this.sortOrderSource = 'desc';
    } else {
      this.sortOrderSource = 'asc';
    }
    if (this.currentReconStatus == 'reconciled') {
      if (event.field == 'reconReference') {
        this.defaultSortField = 'recon_reference';
        this.sDataType = 'STRING';
      } else if (event.field == 'srcCount') {
        this.defaultSortField = 'src_count';
        this.sDataType = 'INTEGER';
      } else if (event.field == 'srcAmount') {
        this.defaultSortField = 'src_amount_sum';
        this.sDataType = 'DECIMAL';
      } else if (event.field == 'srcTolAmount') {
        this.defaultSortField = 'src_tolerance_amount';
        this.sDataType = 'DECIMAL';
      } else if (event.field == 'trCount') {
        this.defaultSortField = 'tr_count';
        this.sDataType = 'INTEGER';
      } else if (event.field == 'trAmount') {
        this.defaultSortField = 'tr_amount_sum';
        this.sDataType = 'DECIMAL';
      } else if (event.field == 'trTolAmount') {
        this.defaultSortField = 'tr_tolerance_amount';
        this.sDataType = 'DECIMAL';
      } else {
        this.defaultSortField = 'grouping_col'
        this.sDataType = 'STRING';
      }
      this.getReconciledRecords();
    }
  }


  changeTargetSort(eve) {
    for (let i = 0; i < this.targetHeaderColumns.length; i++) {
      if (this.targetHeaderColumns[i].field == eve.field) {
        this.tAmountQualifierNameWithTemp = this.targetHeaderColumns[i].columnName;
        this.tDataType = this.targetHeaderColumns[i].dataType;
      }
    }
    if (this.tAmountQualifierNameWithTemp == 'target_row_id') {
      this.tAmountQualifierNameWithTemp = 'scrIds';
    }
    if (eve.order < 1) {
      this.sortOrderTarget = 'desc';
    } else {
      this.sortOrderTarget = 'asc'
    }
    this.getGroupByReconRecords('target');
  }


  /*FUNCTION16 - Manual Reconciliation Adding to Sets Functionality */
  /* Author: BHAGATH */
  addSetsForMRecon(groupid, sviewId, tviewId, status) {
    if (this.selectedSource.length > 0) {
      for (let j = 0; j < this.selectedSource.length; j++) {
        this.manualRSource.push({
          groupId: groupid,
          rowId: this.selectedSource[j].dataRowId,
          viewId: sviewId,
        });
      }
    }
    if (this.selectedTarget.length > 0) {
      for (let k = 0; k < this.selectedTarget.length; k++) {
        this.manualRTarget.push({
          groupId: groupid,
          rowId: this.selectedTarget[k].dataRowId,
          viewId: tviewId,
        });
      }
    }

    if (this.selectedSource.length > 0 && this.selectedTarget.length > 0) {
      if (status == 'unreconciled') {
        this.manualRqObject = {
          source: this.manualRSource,
          target: this.manualRTarget
        }
        this.manualReconArray.push(this.manualRqObject);
        this.manualReconPost();
      } else {
        this.suggestParams = {
          groupId: groupid,
          sViewId: sviewId,
          tViewId: tviewId,
          source: this.suggestSource,
          target: this.suggestTarget
        }
      }
    }
    this.manualRSource = [];
    this.suggestSource = [];
    this.suggestTarget = [];
    this.manualRTarget = [];
    this.selectedSource = [];
    this.selectedTarget = [];
  }

  manualReconPost() {
    this.buttonRestrict = true;
    this.statisticsParams.groupBy = this.groupBy;
    this.statisticsParams.periodFactor = this.periodFactor;
    this.statisticsParams.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
    this.statisticsParams.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
    this.statisticsParams.ruleGroupId = this.reconRuleGroupId;
    this.statisticsParams.status = this.currentReconStatus;
    this.statisticsParams.sViewId = this.sourceDataViewId;
    this.statisticsParams.tViewId = this.targetDataViewId;
    if (this.manualReconArray.length > 0) {
      this.reconcileService.manualRecon(this.manualReconArray).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.sourceRecordRCount = 0;
          this.sourceRecordUCount = 0;
          this.manualReconArray = [];
          this.commonService.success('SUCCESS', 'Selected Records Reconciled Successfully!');
          this.reconcileService.fetchRqCountAmount(this.statisticsParams).takeUntil(this.unsubscribe).subscribe(
            (countRes: any) => {
              this.buttonRestrict = false;
              this.unReconAnalytics = countRes;
              this.unReconGroupByAnalytics = this.unReconAnalytics[0].summary;
            },
            (countRes: Response) => {
              this.buttonRestrict = false;
              this.onError('Unable to reconcile selected records!');
            })
          this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
          this.loadData();
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Unable to reconcile selected records!');
        }
      )
    } else {
      this.buttonRestrict = false;
      this.commonService.info('Info!', 'No Records Qualified');
    }
  }



  // /*FUNCTION18 - Manual Un Reconciliation Functionality */
  // /* Author: BHAGATH */

  manualUnRecon(type, unRecType) {
    const dialogRef = this.dialog.open(WqConfirmActionModalDialogComponent, {
      width: '400px',
      data: { ok: 'ok', reason: '', cancel: 'cancel' }
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result == 'ok') {
        this.unReconAction = true;
        this.buttonRestrict = true;
        this.unReconKeysObject.groupId = this.reconRuleGroupId;
        this.unReconKeysObject.periodFactor = this.periodFactor;
        this.unReconKeysObject.sViewId = this.sourceDataViewId;
        this.unReconKeysObject.tViewId = this.targetDataViewId;
        this.unReconKeysObject.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
        this.unReconKeysObject.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
        this.unReconKeysObject.unReconcileType = 'unReconcile';
        this.unReconKeysObject.type = type;
        if (type == 'recordwise') {
          let recRefs: any = [];
          if (unRecType == 'multiple') {
            if (this.selectedSource.length > 0) {
              recRefs = [];
              this.selectedSource.forEach((item) => {
                recRefs.push(item['reconReference']);
              });
            }
            if (this.selectedTarget.length > 0) {
              recRefs = [];
              this.selectedTarget.forEach((item) => {
                recRefs.push(item['reconReference']);
              });
            }
          } else if (unRecType == 'single') {
            if (this.selectedSigleRow) {
              this.selectedSource = [];
              this.selectedTarget = [];
              recRefs = [];
              recRefs.push(this.selectedSigleRow['reconReference']);
            }
          }
          this.unReconKeysObject.reconReferences = recRefs;
          this.reconcileService.manualUNRecon(this.unReconKeysObject).takeUntil(this.unsubscribe).subscribe(
            (res: any) => {
              if (res['approvalsCount'] == 0) {
                this.commonService.success('SUCCESS', 'Selected Records UnReconciled Successfully!');
              } else {
                this.commonService.success('Info', res['approvalsCount'] + ' Records are not unreconciled as they are in approval process!');
              }
              this.groupBy = "rules";
              this.showReconciliation = false;
              this.sourceRecordRCount = 0;
              this.sourceRecordUCount = 0;
              this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
              this.fetchReconCountAmount('unreconciled', 'days');
              this.selectedKeys = [];
            },
            (res: Response) => {
              this.unReconAction = false;
              this.onError('Error occured while un reconciling the selected records!');
            }
          )
        } else {
          this.unReconKeysObject.reconReferences = [];
          if (this.selectedKeys.length > 0) {
            // @Rk
            let filters=[];
            for (let i = 0; i < this.selectedKeys.length; i++) {
              const sKey = this.selectedKeys[i];
              let obj={groupingColumn: this.unReconGroupBy[0].groupBy, groupingValue: sKey.name, currencyColumn: this.sCurrencyAliasName, currencyValue: sKey.currency};
              filters.push(obj);
            }
            this.unReconKeysObject.filters=filters;
            // ********************
            // ************ Old Code ****************
            // this.unReconKeysObject.filters = [
            //   {
            //     columnName: this.unReconGroupBy[0].groupBy,
            //   },
            //   {
            //     columnName: this.sCurrencyAliasName
            //   }
            // ]
            // this.unReconKeysObject.filters[0].columnValues = [];
            // this.unReconKeysObject.filters[1].columnValues = [];
            // for (let i = 0; i < this.selectedKeys.length; i++) {
            //   const sKey = this.selectedKeys[i];
            //   this.unReconKeysObject.filters[0].columnValues.push(sKey.name);
            //   this.unReconKeysObject.filters[1].columnValues.push(sKey.currency);
            // }
            // ******************************************
            this.reconcileService.manualUNRecon(this.unReconKeysObject).takeUntil(this.unsubscribe).subscribe(
              (res: any) => {
                this.unReconKeysObject.reconReferences = [];
                this.groupingColumnValues.columnId = "";
                this.groupingColumnValues.columnValues = [];
                this.sourceRecordRCount = 0;
                this.sourceRecordUCount = 0;
                this.commonService.success('SUCCESS', 'Selected transactions UnReconciled Successfully!');
                this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
                this.fetchReconCountAmount('unreconciled', 'days');
                // this.loadData();
                this.selectedKeys = [];
                this.buttonRestrict = false;
              },
              (res: Response) => {
                this.unReconAction = false;
                this.buttonRestrict = false;
                this.onError('Unable to unreconcile the selected transactions!')
              }
            )
          } else {
            this.unReconAction = false;
            this.buttonRestrict = false;
            this.commonService.info('Info!', 'No transactions Selected!');
          }

        }
      }
    });
  }

  /*FUNCTION19 - Function to get Column headers of a data view STARTS */
  /* Author: BHAGATH */

  getColumnHeadersByRuleId(viewId, type, headeType, ruleId) {
    this.reconcileService.getHeaderColumnsByRuleId(this.reconRuleGroupId, viewId, type, this.currentReconStatus, headeType, ruleId, this.groupBy).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        if (type == 'source') {
          this.sourceColumnsByruleId = res;
          this.sourceColumns1ByRuleId = this.sourceColumnsByruleId.columns;
          if (this.sourceColumns1ByRuleId.length > 0) {
            this.sourceOptionsByRuleId = [];
            for (let i = 0; i < this.sourceColumns1ByRuleId.length; i++) {
              this.sourceColumns1ByRuleId[i]['searchWord'] = "";
              this.sourceOptionsByRuleId.push({ label: this.sourceColumns1ByRuleId[i].header, value: this.sourceColumns1ByRuleId[i] });
            }
          } else {
            this.commonService.info('Incorrect Inputs!', 'Unable to fetch Column Headers for' + viewId);
          }
        } else if (type == 'target') {
          this.targetColumnsByruleId = res;
          this.targetColumns1ByRuleId = this.targetColumnsByruleId.columns;
          if (this.targetColumns1ByRuleId.length > 0) {
            this.targetOptionsByRuleId = [];
            for (let i = 0; i < this.targetColumns1ByRuleId.length; i++) {
              this.targetColumns1ByRuleId[i]['searchWord'] = "";
              this.targetOptionsByRuleId.push({ label: this.targetColumns1ByRuleId[i].header, value: this.targetColumns1ByRuleId[i] });
            }
          } else {
            this.commonService.info('Incorrect Inputs!', 'Unable to fetch Column Headers for' + viewId);
          }
        }
      },
      (res: Response) => {
        this.onError('Unable to fetch data view columns!');
      }
    )
  }

  getColumnHeaders(groupId, viewId, type, headeType) {
    this.reconcileService.getHeaderColumns(groupId, viewId, type, this.currentReconStatus, headeType, this.groupBy).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        if (type == 'source') {
          this.sourceHeaderColumns1 = res;
          this.sourceHeaderColumns = this.sourceHeaderColumns1.columns;
          if (this.sourceHeaderColumns.length > 0) {
            this.sourceColumnOptions = [];
            for (let i = 0; i < this.sourceHeaderColumns.length; i++) {
              this.sourceHeaderColumns[i]['searchWord'] = "";
              this.sourceColumnOptions.push({ label: this.sourceHeaderColumns[i].header, value: this.sourceHeaderColumns[i] });
            }
          } else {
            this.commonService.info('Incorrect Inputs!', 'Unable to fetch Column Headers for' + viewId);
          }
        } else if (type == 'target') {
          this.targetHeaderColumns1 = res;
          this.targetHeaderColumns = this.targetHeaderColumns1.columns;
          if (this.targetHeaderColumns.length > 0) {
            this.targetColumnOptions = [];
            for (let i = 0; i < this.targetHeaderColumns.length; i++) {
              this.targetHeaderColumns[i]['searchWord'] = "";
              this.targetColumnOptions.push({ label: this.targetHeaderColumns[i].header, value: this.targetHeaderColumns[i] });
            }
          } else {
            this.commonService.info('Incorrect Inputs!', 'Unable to fetch Column Headers for' + viewId);
          }
        }
      },
      (res: Response) => {
        this.onError('Unable to fetch data view columns!');
      }
    )
  }

  onRowExpand(e, type, viewId) {
    this.selectedExpandRow = e.data;
    this.getColumnHeadersByRuleId(viewId, type, 'detail', e.data.ruleId);
    this.getReconChildData(type, e);
  }
  /*FUNCTION20 - Function to fetch target records based on Custom Filters STARTS */
  /* Author: BHAGATH */

  getCustomFilterSource(refs) {
    this.reconcileService.fetchSRecordsCustomFilter(this.reconRuleGroupId, this.sourceDataViewId, refs, this.sPage, this.sitemsPerPage).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.isCustomFilter = true;
        this.reconciledSource = res['source'];
        if (this.reconciledSource.length > 0) {
          this.showReconciliation = true;
        } else {
          this.buttonRestrict = false;
          this.showReconciliation = false;
          this.commonService.info('Info!', 'No Records Matched with current filter!');
        }
      },
      (res: Response) => {
        this.showReconciliation = false;
        this.isCustomFilter = false;
        this.buttonRestrict = false;
        this.onError('Unable to fetch filtered data!');
      }
    )
  }

  getCustomFilterTarget(refs) {
    this.reconcileService.fetchTRecordsCustomFilter(this.reconRuleGroupId, this.targetDataViewId, refs, this.tPage, this.titemsPerPage).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.isCustomFilter = true;
        this.reconciledTarget = res['target'];
        if (this.reconciledTarget.length > 0) {
          this.showReconciliation = true;
        } else {
          this.buttonRestrict = false;
          this.showReconciliation = false;
          this.commonService.info('Info!', 'No Records Matched with current filter!');
        }
      },
      (res: Response) => {
        this.showReconciliation = false;
        this.isCustomFilter = false;
        this.buttonRestrict = false;
        this.onError('Unable to fetch filtered data!');
      }
    )
  }

  totalCustomFilter(row) {
    let ref = "";
    ref = row.reconReference;
    this.reconcileService.fetchTotalCustomFilter(this.reconRuleGroupId, this.sourceDataViewId, this.targetDataViewId, ref).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.sVariances = res;
      }
    )
  }

  sReconPaginate(event) {
    this.sPage = event.pageIndex;
    this.sitemsPerPage = event.pageSize;
    this.getCustomFilterSource(this.selectedSigleRow.reconReference);
  }

  tReconPaginate(event) {
    this.tPage = event.pageIndex;
    this.sitemsPerPage = event.pageSize;
    this.getCustomFilterTarget(this.selectedSigleRow.reconReference);
  }

  fetchTargetOnSourceRecord(groupId, sViewId, tViewId, selectedRow) {
    $(".source-view-table .ui-datatable tbody tr").removeClass('tr-active');
    $(".traget-view-table .ui-datatable tbody tr").removeClass('tr-active');
    this.selectedSource = [];
    this.selectedTarget = [];
    this.selectedSigleRow = selectedRow;
    this.isReconciled = false;
    this.buttonRestrict = true;
    this.selectedSourceAmount = 0;
    this.selectedSourceCount = "";
    this.selectedTargetCount = "";
    this.selectedTargetAmount = 0;
    this.selectedDifference = 0;
    this.selectedDifferencePercent = 0;
    const sInfo: any[] = [];
    const tInfo: any[] = [];
    this.totalLines = {};
    let ruleId: number;
    ruleId = selectedRow.ruleId;
    this.getColumnHeadersByRuleId(sViewId, 'source', 'detail', ruleId);
    this.getColumnHeadersByRuleId(tViewId, 'target', 'detail', ruleId);
    if (this.currentReconStatus == 'reconciled') {
      this.getCustomFilterSource(selectedRow.reconReference);
      this.getCustomFilterTarget(selectedRow.reconReference);
      this.buttonRestrict = false;
    } else {
      this.buttonRestrict = false;
    }
    // }
  }

  changePFactor(e) {
    this.periodFactor = e.value;
    this.dateQualifierType = this.periodsList[this.periodsList.findIndex((x) => x.columnName == e.value)].dateQualifierType;
    this.loadingAllData();
  }

  /* FUNCTION22 - On Load Function */
  /* Author : BHAGATH */
  ngOnInit() {
    this.selectedParams = {};
    this.selectedParams = this.session.retrieve('reconSelected');
    if ((!this.selectedParams || this.selectedParams == null || this.selectedParams == undefined) && !this.commonService.reconDashBoardObject.ruleGroupId) {
      this.router.navigate(['/reconcile', { outlets: { 'content': ['reconcile-list'] } }]);
    }
    if (this.commonService.reconDashBoardObject.ruleGroupId) {
      this.reconRuleGroupId = this.commonService.reconDashBoardObject.ruleGroupId;
      this.periodFactor = this.commonService.reconDashBoardObject.periodFactor;
      this.sourceDataViewId = this.commonService.reconDashBoardObject.sourceViewId;
      this.targetDataViewId = this.commonService.reconDashBoardObject.targetViewId;
      this.sourceViewName = this.commonService.reconDashBoardObject.sourceViewName;
      this.targetViewName = this.commonService.reconDashBoardObject.targetViewName;
      this.reconRuleGroupName = this.commonService.reconDashBoardObject.ruleGroupName;
      this.rangeFrom = this.commonService.reconDashBoardObject.startDateRange;
      this.rangeTo = this.commonService.reconDashBoardObject.endDateRange;
      if (this.commonService.reconDashBoardObject.status == 'reconciled') {
        this.groupBy = "columnName";
        this.selectedParentTab = 0;
      } else {
        this.groupBy = "columnName";
        this.selectedParentTab = 1;
      }
      this.currentReconStatus = this.commonService.reconDashBoardObject.status;
    } else {
      const selectedRange = [moment().subtract(29, 'days'), moment()];
      this.rangeFrom = new Date(selectedRange[0]);
      this.rangeTo = new Date(selectedRange[1]);
      this.reconRuleGroupId = this.selectedParams.groupId;
      this.sourceDataViewId = this.selectedParams.sViewId;
      this.targetDataViewId = this.selectedParams.tViewId;
      this.reconRuleGroupName = this.selectedParams.ruleGroupName;
      this.sourceViewName = this.selectedParams.sViewName;
      this.targetViewName = this.selectedParams.tViewName;
      this.selectedParentTab = 1;
      this.groupBy = "days";
      this.currentReconStatus = "unreconciled";
    }
    this.fetchRuleGroups();

    $(".split-example").css({
      'height': 'auto',
      'min-height': (this.commonService.screensize().height - 130) + 'px'
    });

    this.principal.identity().then((account) => {
      this.commonService.currentAccount = account;
    });

  }
  /* FUNCTION23 : Function for Source pagination based on filtering */
  /* AUTHOR : BHAGATH */
  onSPaginateChange(event) {
    this.page = event.pageIndex;
    this.itemsPerPage = event.pageSize;
    if (this.selectedKeys.length > 0) {
      this.getGroupByReconRecords('source');
    }
  }
  /* FUNCTION4 : Function for Target pagination based on filtering */
  /* AUTHOR : BHAGATH */
  onTPaginateChange(event) {
    this.page1 = event.pageIndex;
    this.itemsPerPage1 = event.pageSize;
    this.getGroupByReconRecords('target');
  }

  scheduleJobRecon() {
    this.scheduling.ngOnInit();
    this.reconSchedObj = {};
    this.reconSchedParamms = [];
    let fromDate: any = "";
    let toDate: any = "";
    let colValues = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    if (this.selectedKeys.length == 0) {
      this.reconSchedParamms = [
        {
          paramName: 'RuleGroupName',
          selectedValue: this.reconRuleGroupId
        },
        {
          paramName: 'Filter View',
          selectedValue: this.sourceDataViewId
        },
        {
          paramName: 'Date Range',
          selectedValue: fromDate + "," + toDate
        }
      ];
    } else {
      if (this.groupBy == 'days') {
        this.selectedKeys.forEach((item) => {
          const newDate = new Date(item.name);
          const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          if (colValues.length > 1) {
            colValues = colValues + "," + newDate2;
          } else {
            colValues = newDate2;
          }
        });
        if (this.periodFactor == 'fileDate') {
          this.reconSchedParamms = [
            {
              paramName: 'RuleGroupName',
              selectedValue: this.reconRuleGroupId
            },
            {
              paramName: 'Filter View',
              selectedValue: this.sourceDataViewId
            },
            {
              paramName: 'Date Range',
              selectedValue: fromDate + "," + toDate
            },
            {
              paramName: 'Filter Column',
              selectedValue: this.periodFactor
            },
            {
              paramName: 'Filter Values',
              selectedValue: colValues
            }
          ];
          this.reconSchedObj.displayParameters = [
            {
              paramName: 'RuleGroupName',
              selectedValue: this.reconRuleGroupName
            },
            {
              paramName: 'Filter View',
              selectedValue: this.sourceViewName
            },
            {
              paramName: 'Date Range',
              selectedValue: fromDate + "," + toDate
            },
            {
              paramName: 'Filter Column',
              selectedValue: 'File Received Date'
            },
            {
              paramName: 'Filter Values',
              selectedValue: colValues
            }
          ]
        } else {
          this.reconSchedParamms = [
            {
              paramName: 'RuleGroupName',
              selectedValue: this.reconRuleGroupId
            },
            {
              paramName: 'Filter View',
              selectedValue: this.sourceDataViewId
            },
            {
              paramName: 'Date Range',
              selectedValue: fromDate + "," + toDate
            },
            {
              paramName: 'Filter Column',
              selectedValue: this.dateQualifier
            },
            {
              paramName: 'Filter Values',
              selectedValue: colValues
            }
          ];
          this.reconSchedObj.displayParameters = [
            {
              paramName: 'RuleGroupName',
              selectedValue: this.reconRuleGroupName
            },
            {
              paramName: 'Filter View',
              selectedValue: this.sourceViewName
            },
            {
              paramName: 'Date Range',
              selectedValue: fromDate + "," + toDate
            },
            {
              paramName: 'Filter Column',
              selectedValue: this.dateQualifierName
            },
            {
              paramName: 'Filter Values',
              selectedValue: colValues
            }
          ]
        }

      } else {
        this.selectedKeys.forEach((item) => {
          if (colValues.length > 1) {
            colValues = colValues + "," + item.name;
          } else {
            colValues = item.name;
          }
        });
        this.reconSchedParamms = [
          {
            paramName: 'RuleGroupName',
            selectedValue: this.reconRuleGroupId
          },
          {
            paramName: 'Filter View',
            selectedValue: this.sourceDataViewId
          },
          {
            paramName: 'Date Range',
            selectedValue: fromDate + "," + toDate
          },
          {
            paramName: 'Filter Column',
            selectedValue: this.currencyQualifier
          },
          {
            paramName: 'Filter Values',
            selectedValue: colValues
          }
        ];
        this.reconSchedObj.displayParameters = [
          {
            paramName: 'RuleGroupName',
            selectedValue: this.reconRuleGroupName
          },
          {
            paramName: 'Filter View',
            selectedValue: this.sourceViewName
          },
          {
            paramName: 'Date Range',
            selectedValue: fromDate + "," + toDate
          },
          {
            paramName: 'Filter Column',
            selectedValue: this.sCurrencyQualifierName
          },
          {
            paramName: 'Filter Values',
            selectedValue: colValues
          }
        ]
      }
    }
    if (this.reconSchedParamms) {
      this.reconSchedObj.parameters = this.reconSchedParamms;
      this.reconSchedObj.jobName = this.reconRuleGroupName;
      this.reconSchedObj.programName = 'RECONCILIATION';
    }
  }



  /* FUNCTION24 : Function to call OnDemand Recon Job and Auto Refresh Functionality */
  /* AUTHOR : BHAGATH */
  initiateReconJob(purpose) {
    let fromDate: any = "";
    let toDate: any = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    this.dateRange = [fromDate, toDate];
    let colValues: any = [];
    if (purpose == 'Reconciliation') {
      if (this.groupBy == 'days') {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach((item) => {
            const newDate = new Date(item.name);
            const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            colValues.push(newDate2);
          });
          if (this.periodFactor == 'fileDate') {
            this.paramSet = {
              'param1': this.reconRuleGroupId,
              'param3': this.sourceDataViewId,
              'param4': this.periodFactor,
              'param5': colValues,
              'param6': this.dateRange
            }
          } else {
            this.paramSet = {
              'param1': this.reconRuleGroupId,
              'param3': this.sourceDataViewId,
              'param4': this.dateQualifier,
              'param5': colValues,
              'param6': this.dateRange
            }
          }

        } else {
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.sourceDataViewId,
            'param6': this.dateRange
          }
        }
        colValues = [];
      } else {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach((item) => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.sourceDataViewId,
            'param4': this.currencyQualifier,
            'param5': colValues,
            'param6': this.dateRange
          }
        } else {
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.sourceDataViewId,
            'param6': this.dateRange
          }
        }
        colValues = [];
      }
    } else if (purpose == 'Accounting') {
      if (this.groupBy == 'days') {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach((item) => {
            const newDate = new Date(item.name);
            const newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            colValues.push(newDate2);
          });
          if (this.periodFactor == 'fileDate') {
            this.paramSet = {
              'param1': this.reconRuleGroupId,
              'param3': this.sourceDataViewId,
              'param4': this.periodFactor,
              'param5': colValues,
              'param6': this.dateRange
            }
          } else {
            this.paramSet = {
              'param1': this.reconRuleGroupId,
              'param3': this.sourceDataViewId,
              'param4': this.dateQualifier,
              'param5': colValues,
              'param6': this.dateRange
            }
          }
        } else {
          this.paramSet = {
            'param1': this.acctRuleGroupId,
            'param3': this.sourceDataViewId,
            'param6': this.dateRange
          }
        }
        colValues = [];
      } else {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach((item) => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.acctRuleGroupId,
            'param3': this.sourceDataViewId,
            'param4': this.groupBy,
            'param5': colValues,
            'param6': this.dateRange
          }
        } else {
          this.paramSet = {
            'param1': this.acctRuleGroupId,
            'param3': this.sourceDataViewId,
            'param6': this.dateRange
          }
        }
        colValues = [];

      }
    } else {
      this.paramSet = {
        'param1': this.approvalGroup
      }
    }
    this.jobDetailsService.oozieDBTest().takeUntil(this.unsubscribe).subscribe((res: any) => {
      this.oozieTestRecon = res.ooziestatus;
      if (this.oozieTestRecon == true) {
        this.initiatingJob(purpose, this.paramSet);
      } else {
        this.commonService.error('Warning!', 'Job Engine Start Up Failure...Please try again later');
      }
    }, (res: Response) => {
      this.onError('Error while checking Oozie status!')
    }
    )
  }

  beforeJobInitiate() {
    if (this.selectedKeys.length > 0) {
      this.displayConfirmDialog = true;
    } else {
      this.commonService.info('Info', 'No Records Selected');
    }
  }


  initiatingJob(purpose, set) {
    this.buttonRestrict = true;
    if (purpose == 'Reconciliation') {
      this.jobDetailsService.initiateAcctRecJob('Reconciliation', set).takeUntil(this.unsubscribe).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.reconJobOutput = res;
        this.reconJobId = this.reconJobOutput.status;

        if (this.reconJobId != 'Failed to intiate job') {
          this.commonService.success('Info!', 'Reconciliation Job Initiated Successfully!');
          this.setIntId = setInterval(() => {
            if (this.callSetInt == true) {
              this.jobDetailsService.oozieeJobStatus(this.reconJobId).takeUntil(this.unsubscribe).subscribe((oozieStatusRes: any) => {
                this.reconJobStatus = oozieStatusRes;
                this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
                this.loadData();
                this.sourceRecordUCount = 0;
                this.sourceRecordRCount = 0;
                if (this.reconJobStatus._body == 'SUCCEEDED' || this.reconJobStatus._body == 'KILLED' || this.reconJobStatus._body == 'FAILED') {
                  if (this.setIntId) {
                    this.sourceRecordUCount = 0;
                    this.sourceRecordRCount = 0;
                    this.loadData();
                    clearInterval(this.setIntId);
                  }
                }
              },
                (oozieStatusRes: Response) => {
                  clearInterval(this.setIntId);
                  this.sourceRecordUCount = 0;
                  this.sourceRecordRCount = 0;
                  this.onError('Failed to get job status!')
                }
              )
            }
          }, 30000);
        } else {
          this.commonService.error('Error!', 'Failed to intiate job!');
          this.sourceRecordUCount = 0;
          this.sourceRecordRCount = 0;
        }
      },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Failed to intiate job!');
        }
      )

    } else {
      this.jobDetailsService.initiateAcctRecJob(purpose, set).takeUntil(this.unsubscribe).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.acctJobOutput = res;
        this.acctJobId = this.acctJobOutput.status;
        if (this.acctJobId != 'Failed to intiate job') {
          this.commonService.success('Info!', purpose + ' Job Initiated Successfully!');
          this.runAcctShow=false;
        } else {
          this.commonService.error('Error!', 'Failed to intiate ' + purpose + ' job!');
        }
      },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Failed to intiate job!');
        }
      )
    }
  }


  private onError(errorMessage) {
    this.commonService.error(
      'Error!',
      errorMessage
    )
  }

  /* FUNCTION25 : Function to stop Auto Refresh Functionality on Destroy */
  /* AUTHOR : BHAGATH */

  public ngOnDestroy() {
    if (this.setIntId) {
      clearInterval(this.setIntId);
    }
    this.unsubscribe.next();
    this.unsubscribe.complete();
    this.commonService.destroyNotification(); 
  }


  hideSchDialog(event) {
    this.reconcileService.reconSchJob = false;
  }

  buildRule() {
    this.reconcileService.ENABLE_RULE_BLOCK = true;
    this.create = 'createInWQ';
    this.create = this.reconRuleGroupId + '-' + this.create;
  }

  searchWithKeyWord(searchkey, type) {
    this.buttonRestrict = true;
    if (type == 'source') {
      this.page = 0;
      this.selectedSourceAmount = 0;
      this.selectedSourceCount = "";
      this.selectedSource = [];
      this.sourceFileExpObject['searchWord'] = searchkey;
      this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage, this.sourceFileExpObject).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.sourceLines = res;
          if (this.sourceLines.length > 1) {
            this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
            this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
          } else {
            this.commonService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Search Request Failed!');
        }
      )
    } else {
      this.page1 = 0;
      this.selectedTargetCount = "";
      this.selectedTargetAmount = 0;
      this.selectedTarget = [];
      this.targetFileExpObjext['searchWord'] = searchkey;
      this.reconcileService.fetchAllReconWqRecords(this.page1, this.itemsPerPage1, this.targetFileExpObjext).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.targetLines = res;
          if (this.targetLines.length > 1) {
            this.targetLength = this.targetLines[this.targetLines.length - 1].info.totalCount;
            this.targetDataInfo = this.targetLines.splice(this.targetLines.length - 1, 1);
          } else {
            this.commonService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Search Request Failed!');
        }
      )
    }
  }

  reconGlobalSearch() {
    this.rwReconQueries.searchWord = this.reconSearchWord;
    this.getReconciledRecords();
  }

  colLevelSearchWithKeyWord(type) {
    this.buttonRestrict = true;
    this.rwReconQueries.columnSearch = [];
    if (type == 'source') {
      this.page = 0;
      this.selectedSourceAmount = 0;
      this.selectedSourceCount = "";
      this.selectedSource = [];
      if (this.currentReconStatus != 'reconciled') {
        for (let i = 0; i < this.sourceHeaderColumns.length; i++) {
          if (this.sourceHeaderColumns[i].searchWord != "") {
            this.rwReconQueries.columnSearch.push({
              columnId: this.sourceHeaderColumns[i].columnName,
              searchWord: this.sourceHeaderColumns[i].searchWord,
              dataType: this.sourceHeaderColumns[i].dataType,
            })
          }
        }
      } else {
        this.rwReconQueries.columnSearch.push({
          columnId: 'recon_reference',
          searchWord: this.sourceSearchWord,
          dataType: 'STRING',
        })
      }
      this.sourceFileExpObject['columnSearch'] = this.rwReconQueries.columnSearch;
      this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage, this.sourceFileExpObject).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.rwReconQueries.columnSearch = [];
          this.buttonRestrict = false;
          this.sourceLines = res;
          if (this.sourceLines.length > 1) {
            this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
            this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
          } else {
            this.commonService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Column search request failed!');
        }
      )
    } else {
      this.page1 = 0;
      this.selectedTargetCount = "";
      this.selectedTargetAmount = 0;
      this.selectedTarget = [];
      if (this.currentReconStatus != 'reconciled') {
        for (let i = 0; i < this.targetHeaderColumns.length; i++) {
          if (this.targetHeaderColumns[i].searchWord != "") {
            this.rwReconQueries.columnSearch.push({
              columnId: this.targetHeaderColumns[i].columnName,
              searchWord: this.targetHeaderColumns[i].searchWord,
              dataType: this.targetHeaderColumns[i].dataType
            })
          }
        };
      } else {
        this.rwReconQueries.columnSearch.push({
          columnId: 'recon_reference',
          searchWord: this.sourceSearchWord,
          dataType: 'STRING',
        })
      }
      this.targetFileExpObjext['columnSearch'] = this.rwReconQueries.columnSearch;
      this.reconcileService.fetchAllReconWqRecords(this.page1, this.itemsPerPage1, this.targetFileExpObjext).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.rwReconQueries.columnSearch = [];
          this.buttonRestrict = false;
          this.targetLines = res;
          if (this.targetLines.length > 1) {
            this.targetLength = this.targetLines[this.targetLines.length - 1].info.totalCount;
            this.targetDataInfo = this.targetLines.splice(this.targetLines.length - 1, 1);
          } else {
            this.commonService.info('Info!', 'No Records Found!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Column search request failed!');
        }
      )
    }
  }

  recDataFileExp(exportType) {
    this.reconcileService.exportReconciledAPI(this.reconRuleGroupName, this.sourceViewName, this.targetViewName, exportType, {}).takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        this.exportRes = res;
        this.commonService.exportData(this.exportRes, exportType, this.reconRuleGroupName + " - " + this.currentReconStatus);
        this.commonService.success("Download Success!", 'Data Exported Successfully');
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError('Download Failed!')
      }
    )
  }

  unRecDataFileExp(viewName, type, exportType) {
    if (type == 'source') {
      this.reconcileService.exportUnReconciledAPI(this.reconRuleGroupName, type, {}, viewName, '', exportType).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.commonService.exportData(res, exportType, viewName + " - " + this.currentReconStatus);
          this.commonService.success("Download Success!", 'Data Exported Successfully');
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Download Failed!')
        }
      )
    } else {
      this.reconcileService.exportUnReconciledAPI(this.reconRuleGroupName, type, {}, '', viewName, exportType).takeUntil(this.unsubscribe).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.commonService.exportData(res, exportType, viewName + " - " + this.currentReconStatus);
          this.commonService.success("Download Success!", 'Data Exported Successfully');
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError('Download Failed!')
        }
      )
    }

  }

  getDataForSplit(row, type, sourceOrTarget) {
    this.isEvenly = 'Evenly';
    this.splitType = sourceOrTarget;
    this.rowCount = undefined;
    this.splitRowArray = [];
    this.addRowArray = [];
    this.masterRow = row['dataRowId'];
    this.splitRow = [];
    this.splitRow.push(row);
    this.rowAction = type + ' Row';
    this.showSplit = true;
    if (sourceOrTarget == 'source') {
      this.selectedAmount = row[this.sAmountQualifierNameWithTemp];
      this.splitCurreny = row[this.sCurrencyQualifierName];
      if (type == 'Add') {
        this.addRowArray.push({
          adjustmentType: 'Add',
          dataViewId: this.sourceDataViewId,
          adjustmentMethod: 'Normal',
          masterRowId: this.masterRow
        })
      }
    } else {
      this.selectedAmount = row[this.tAmountQualifierNameWithTemp];
      this.splitCurreny = row[this.tCurrencyQualifierName];
      if (type == 'Add') {
        this.addRowArray.push({
          adjustmentType: 'Add',
          dataViewId: this.targetDataViewId,
          adjustmentMethod: 'Normal',
          masterRowId: this.masterRow
        })
      }
    }
  }
  updateSplitTable(count) {
    if(count < 2){
      this.splitRowArray = [];
      this.rowCount = undefined;
      this.commonService.info('Oops...!','Split row allow 2 or more rows');
    }else{
      let bFlag=false;
      if(count<this.splitRowArray.length){
        const tmpArr=[...this.splitRowArray];
        this.splitRowArray = [];
        let sum=0;
        for(let i=0;i<count;i++){
          this.splitRowArray.push(tmpArr[i]);
          sum=sum+tmpArr[i].percentValue;
        }
      }else{
        if(this.splitRowArray.length==0){
          bFlag=true;
        }else{
          const tmpArr=[...this.splitRowArray];
          this.splitRowArray = [];
          tmpArr.forEach(element => {
            this.splitRowArray.push(element);
          });
        }
        for(let i=count-this.splitRowArray.length;i>0;i--){
          this.splitRowArray.push({
            adjustmentType: 'Split',
            dataViewId: this.splitType == 'source' ? this.sourceDataViewId : this.targetDataViewId,
            adjustmentMethod: this.isEvenly,
            masterRowId: this.masterRow
          });
        }
      }
      if(this.isEvenly == 'Percent' && bFlag){
        for (let i = 0; i < this.splitRowArray.length; i++) {
          this.splitRowArray[i].percentValue = 100 / this.splitRowArray.length;
        }
      }
      this.isEvenlyTrue();
    }
  }
  updateSplitAmount(percent, ind) {
    if(percent){
      this.splitRowArray[ind].amount=Math.round((this.selectedAmount * this.splitRowArray[ind].percentValue) / 100);
    }else{
      this.splitRowArray[ind].amount=0;
    }

  }
  isEvenlyTrue() {
    if (this.isEvenly == 'Evenly') {
      for (let i = 0; i < this.splitRowArray.length; i++) {
        this.splitRowArray[i].amount = Math.round(this.selectedAmount / this.splitRowArray.length);
        this.splitRowArray[i].adjustmentMethod = this.isEvenly;
        this.splitRowArray[i].percentValue = undefined;
      }
    } else {
      for (let i = 0; i < this.splitRowArray.length; i++) {
        this.splitRowArray[i].adjustmentMethod = this.isEvenly;
        this.splitRowArray[i].percentValue = 100 / this.splitRowArray.length;
        this.splitRowArray[i].amount = Math.round((this.selectedAmount * this.splitRowArray[i].percentValue) / 100);
      }
    }
  }

  saveSplitRow() {
    if (this.rowAction == 'Split Row') {
      let totalPercent = 0;
      let totalAmount = 0;
      for (let i = 0; i < this.splitRowArray.length; i++) {
        const element = this.splitRowArray[i];
        if (element.adjustmentMethod == 'Percent') {
          totalPercent = totalPercent + element.percentValue;
        }
        totalAmount = totalAmount + element.amount;
      }
      if (this.isEvenly == 'Evenly') {
        if (totalAmount == this.selectedAmount) {
          this.reconcileService.splitRowPost(this.splitRowArray).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.showSplit = false;
            this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
            this.loadData();
          },
            (res: Response) => {
              this.onError('Error while splitting the record!');
            }
          )
        } else {
          this.commonService.info('Info!', 'Split Amounts should be equal to divisible amount!');
        }
      } else {
        if (totalAmount == this.selectedAmount && totalPercent == 100) {
          this.reconcileService.splitRowPost(this.splitRowArray).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.showSplit = false;
            this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
            this.loadData();
          },
            (res: Response) => {
              this.onError('Error while splitting the record!');
            }
          )
        } else {
          this.commonService.info('Info!', 'Split Amounts should be equal to divisible amount!');
        }
      }

    } else {
      this.reconcileService.splitRowPost(this.addRowArray).takeUntil(this.unsubscribe).subscribe((res: any) => {
        this.showSplit = false;
        this.fetchReconCountAmount(this.currentReconStatus, this.groupBy);
        this.loadData();
      },
        (res: Response) => {
          this.onError('Error while adding the record!');
        }
      )
    }

  }

  toggleRange(e) {
    this.selectedMoreRange = false;
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
      if (this.dateDifference == 0) {
        selectedRange = [moment(), moment()];
      } else if (this.endDateDif == 0) {
        selectedRange = [moment().subtract(this.dateDifference - 1, 'days'), moment()];
      } else {
        selectedRange = [moment().subtract(this.dateDifference - 1, 'days'), moment().subtract(this.endDateDif - 1, 'days')];
      }

    }
    this.rangeFrom = new Date(selectedRange[0]);
    this.rangeTo = new Date(selectedRange[1]);
    if (this.currentReconStatus == 'reconciled') {
      this.groupBy = 'rules';
    } else {
      this.groupBy = 'days';
    }
    this.loadingDefaultCountAmount();
  }

  changeStartRange() {
    this.reconGroupByAnalytics=[];
    this.unReconGroupByAnalytics=[];
    for (let i = 0; i < this.dateRangeList.length; i++) {
      this.dateRangeList[i]['checked'] = false;
      if (this.dateRangeList[i].type == "custom") {
        this.dateRangeList.splice(i, 1);
      }
    }
    const oneDay = 24 * 60 * 60 * 1000;
    this.dateDifference = Math.round(Math.abs((this.rangeTo.getTime() - this.rangeFrom.getTime()) / (oneDay)));
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
        checked: true,
        value: this.dateDifference + 'Days',
        title: 'Last ' + this.dateDifference + ' Days',
        name: this.dateDifference + ' Days',
        type: "custom"
      });
    }
    if (this.currentReconStatus == 'reconciled') {
      this.groupBy = 'rules';
    } else {
      this.groupBy = 'days';
    }
    this.loadingDefaultCountAmount();
  }

  changeEndRange() {
    this.reconGroupByAnalytics=[];
    this.unReconGroupByAnalytics=[];
    for (let i = 0; i < this.dateRangeList.length; i++) {
      this.dateRangeList[i]['checked'] = false;
      if (this.dateRangeList[i].type == "custom") {
        this.dateRangeList.splice(i, 1);
      }
    }
    const oneDay = 24 * 60 * 60 * 1000;
    this.dateDifference = Math.round(Math.abs((this.rangeTo.getTime() - this.rangeFrom.getTime()) / (oneDay)));
    this.endDateDif = Math.round(Math.abs((this.systemDate.getTime() - this.rangeTo.getTime()) / (oneDay)));
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
        checked: true,
        value: this.dateDifference + 'Days',
        title: 'Last ' + this.dateDifference + ' Days',
        name: this.dateDifference + ' Days',
        type: "custom"
      });
    }
    if (this.currentReconStatus == 'reconciled') {
      this.groupBy = 'rules';
    } else {
      this.groupBy = 'days';
    }
    this.loadingDefaultCountAmount();
  }

  getReconChildData(type, event) {
    const childParams: any = {};
    childParams['groupId'] = this.reconRuleGroupId;
    if (type == 'source') {
      childParams['viewId'] = this.sourceDataViewId
    } else {
      childParams['viewId'] = this.targetDataViewId;
    }
    childParams['sourceOrTarget'] = type;
    childParams['reconReference'] = event.data.reconReference;

    this.reconcileService.getReconChildData(childParams).takeUntil(this.unsubscribe).subscribe((res: any) => {
      if (type == 'source') {
        for (let i = 0; i < this.reconciledRecords.length; i++) {
          const sline = this.reconciledRecords[i];
          if (sline.reconReference == event.data.reconReference) {
            sline['sourceChild'] = res['reconRefData'];
          }
        }
      } else if (type == 'target') {
        for (let i = 0; i < this.reconciledRecords.length; i++) {
          const tline = this.reconciledRecords[i];
          if (tline.reconReference == event.data.reconReference) {
            tline['targetChild'] = res['reconRefData'];
          }
        }
      }
    },
      (res: Response) => {
        this.onError('Error while adding the record!');
      }
    )
  }
}