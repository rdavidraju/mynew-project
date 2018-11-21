/***** Module Name: Reconciliation Work Queue
       Author: BHAGATH
******/
import { Component, OnInit, OnDestroy, ChangeDetectorRef, Inject, Directive } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import {
  Reconcile, ReportTypeRecords, ReconciliationRecords, SourceReconciliationLines, TargetReconciliationLines, ReconcileBreadCrumbTitles, customFilters, SfilterColumns, TfilterColumns, reconcileIdsforSource,
  reconcileIdsforTarget, selectedReconciledIds, QueryParams, searchArray, anaylticParams,colSearchVal, reconDataQueries, reconKeyValues, reconAmounts, groupByColumnValues, manualReconOject, manualSReconArray, manualTReconArray, unReconKeyValues
} from './reconcile.model';
import { ReconcileService } from './reconcile.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ENABLE_RULE_BLOCK } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, OverlayPanelModule, TreeTableModule, TreeNode, SharedModule } from 'primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { CommonService } from '../common.service';
import { RuleGroupService } from '../rule-group/rule-group.service';
import { PageEvent } from '@angular/material';
import { DataViewsService } from '../data-views/data-views.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { RuleGroupWithRulesAndConditions } from '../rule-group/ruleGroupWithRulesAndConditions.model';
import { SessionStorageService } from 'ng2-webstorage';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { RulesService } from '../rules/rules.service';
import { Observable } from 'rxjs/Rx';
import { DatePipe } from '@angular/common';
import { OrderBy } from '../orderByPipe';
// import { AmountFormat } from '../amountFormatPipe';
import Chart from 'chart.js';
import { SchedulingModel, Parameters } from '../scheduling/scheduling.component';
import { AccountingDataService } from '../accounting-data/accounting-data.service';
// import { PushNotificationComponent } from 'ng2-notifications/ng2-notifications';
import { JhiDateUtils } from 'ng-jhipster';
import {ReconDashBoardObject} from '../../shared/constants/constants.values';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;
const COMMA = 188;


/* Confirmation Dialog Component */
@Component({
  selector: 'rwq-confirm-action-modal',
  templateUrl: 'rwq-confirm-action-modal.html'
})
export class WqConfirmActionModalDialog {
  constructor(
    public dialogRef: MdDialogRef<WqConfirmActionModalDialog>,
    public dialog: MdDialog,
    private recService: ReconcileService,
    private acService: AccountingDataService,
    @Inject(MD_DIALOG_DATA) public data: any) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

/* Reconcile Component */
@Component({
  selector: 'rwq-reconcile',
  templateUrl: './rwq.component.html',
})
export class RWQComponent implements OnInit, OnDestroy {
  currentAccount: any;
  reconciles: Reconcile[];
  error: any;
  success: any;
  public notification: any = {
    show: false,
    title: 'New Angular 2 Library!',
    body: 'ng2-notifications',
    icon: 'https://goo.gl/3eqeiE',
  };
  eventSubscriber: Subscription;
  links: any;
  totalItems: any;
  queryCount: any;

  //Modified Variables
  reconRuleGroups: any;
  reconRuleGroupName: string;
  reconRuleGroupId: number;
  sourceDataViews: any[] = [];
  targetDataViews: any[] = [];
  selectedSourceViews: any[] = [];
  selectedTargetViews: any[] = [];
  periodFactor: string = 'fileDate';
  selectedDateRange: string = '7days';
  rangeTo: any;
  rangeFrom: any;
  amountQaulifier: any;
  dateQualifier: any;
  currencyQualifier: any;
  isQuery: boolean = false;
  currentReconStatus: string = 'unreconciled';
  groupBy: string;
  reconciledAmount: any;
  unreconciledAmount: any;
  selectedKeys: any[] = [];
  selectedSource: any[];
  selectedTarget: any[];
  itemsPerPage: any;
  itemsPerPage1: any;
  page: any = 0;
  page1: any = 0;
  pageSizeOptions = [10, 25, 50, 100];;
  pageEvent: PageEvent;
  sSearchKeys: any[] = [];
  sKeyWord: any;
  sColumnName: any;
  tSearchKeys: any[] = [];
  tKeyWord: any;
  tColumnName: any;
  sourceKey: any;
  targetKey: any;
  sortBySColId: number;
  sortByTColId: number;
  sortOrderSource: any = "desc";
  sortOrderTarget: any = "desc";
  sourceLines: any[] = [];
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
  rwReconQueries: reconDataQueries = {};
  reconKeysObject: reconKeyValues = {};
  unReconKeysObject: unReconKeyValues = {};
  selectedSCountAmount: any;
  selectedTCountAmount: any;
  selectedSourceAmount: number = 0;
  selectedTargetAmount: number = 0;
  selectedSourceCount: any;
  selectedTargetCount: any;
  selectedDifference: number;
  selectedDifferencePercent: number;
  paramSet: any;
  queryParams: QueryParams;
  groupByAnalytics: any[] = [];
  showReconAnalytics: boolean = false;
  setIntId: any;
  sourceSelectionSettings: any = {};
  targetSelectionSettings: any = {};
  callSetInt: boolean = false;
  groupingColumnValues: groupByColumnValues = {};
  reconJobId: string;
  reconJobOutput: any
  reconJobStatus: any;
  manualReconArray: any[] = [];
  manualRqObject: manualReconOject = {};
  manualRSource: manualSReconArray[] = [];
  manualRTarget: manualTReconArray[] = [];
  sourceRecordRCount: number = 0;
  sourceRecordUCount: number = 0;
  isUnReconciled: boolean = false;
  isReconciled: boolean = false;
  create: string = 'createInWQ';
  runAcctShow: boolean = false;
  schReconShow: boolean = false;
  acctRuleGroups: any;
  acctRuleGroupId: any;
  acctJobOutput: any;
  acctJobId: any;
  acctJobStatus: any;
  acctParamSet: any;
  acctJobName: any = 'Accounting Job';
  reconSchedObj: SchedulingModel = {};
  reconSchedParamms: Parameters[] = [];
  reconFileExpList: any[] = [];
  sourceFileExpObject: any;
  showFilterView:boolean = false;
  targetFileExpObjext: any;
  formConfigObject: any = {};
  formFilterArray: any[] = [];
  buttonRestrict: boolean = false;
  isViewOnly: boolean = false;
  isVisibleA: boolean = true;
  selectedChildTab: number;
  selectedAmountTab: number;
  selectedParentTab: number;
  sourceDataViewId: any;
  targetDataViewId: any;
  sourceViewName: any;
  targetViewName: any;
  viewColumns: any;
  reconAnalytics: any;
  statisticsParams: anaylticParams;
  isSummary: boolean = false;
  recAmts: reconAmounts = {};
  columnFilter:colSearchVal[]=[];
  // isReconDetails:boolean = false;

  constructor(
    private ruleGroupService: RuleGroupService,
    public reconcileService: ReconcileService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager,
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig,
    private notificationService: NotificationsService,
    private commonService: CommonService,
    private dataViewsService: DataViewsService,
    private $sessionStorage: SessionStorageService,
    private jobDetailsService: JobDetailsService,
    public dialog: MdDialog,
    private rulesService: RulesService,
    private datePipe: DatePipe,
    private cdr: ChangeDetectorRef,
    private dateUtils: JhiDateUtils
  ) {

    this.itemsPerPage = ITEMS_PER_PAGE;
    this.itemsPerPage1 = ITEMS_PER_PAGE;
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    if (routeSnapshot.data.breadcrumb === ReconcileBreadCrumbTitles.reconcileNew) {
      this.isVisibleA = true;
    }
    else {
      this.isVisibleA = false;
    }
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  /*FUNCTION1 - Change Status Functionality*/
  /* Author: BHAGATH */
  changeStatus(e) {
    console.log('chagne stat us e ' + e);
    this.selectedChildTab = 0;
    if (!e || !e.index) {
      this.selectedParentTab = 0;
    } else {
      this.selectedParentTab = e.index;
    }
    if (this.selectedParentTab == 0) {
      this.groupingHeads = ["Sub Process", "Batch", "Currency"];
      $(".group-by .mat-tab-header").removeClass('tab-width-320').addClass('tab-width-480');
      $("#dataColumns").removeClass('column-left-320').addClass('column-left-480');
      this.currentReconStatus = "reconciled";
      this.selectedAmountTab = 0;
      this.groupBy = "rules";
      this.fetchReconCountAmount(this.selectedDateRange);
    } else if (this.selectedParentTab == 1) {
      this.groupingHeads = ["Period", "Currency"];
      $(".group-by .mat-tab-header").removeClass('tab-width-480').addClass('tab-width-320');
      $("#dataColumns").removeClass('column-left-480').addClass('column-left-320');
      this.currentReconStatus = "unreconciled";
      this.selectedAmountTab = 1;
      this.groupBy = "days";
      this.fetchReconCountAmount(this.selectedDateRange);
    } else if (this.selectedParentTab == 2) {
      this.groupingHeads = ["Sub Process", "Batch", "Currency"];
      $(".group-by .mat-tab-header").removeClass('tab-width-320').addClass('tab-width-480');
      $("#dataColumns").removeClass('column-left-320').addClass('column-left-480');
      this.currentReconStatus = "suggested"
    }

  }

  /*FUNCTION2 - Change group by functionality*/
  /* Author: BHAGATH */
  changeGroupBy(e) {
    $(".group-by md-tab").attr('disabled',true);
    console.log('chagne group by e ' + e);
    if (!e || !e.index) {
      this.selectedChildTab = 0;
    } else {
      this.selectedChildTab = e.index;
    }
    if (this.currentReconStatus == 'reconciled') {
      if (this.selectedChildTab == 0) {
        this.groupBy = 'rules';
      } else if (this.selectedChildTab == 1) {
        this.groupBy = 'batch';
      } else if (this.selectedChildTab == 2) {
        this.groupBy = 'columnName';
      } else {
        this.groupBy = 'days';
      }
    } else if (this.currentReconStatus == 'unreconciled') {
      if (this.selectedChildTab == 0) {
        this.groupBy = 'days';
      } else if (this.selectedChildTab == 1) {
        this.groupBy = 'columnName';
      }
    }
    this.fetchReconCountAmount(this.selectedDateRange);
  }

  /*FUNCTION3 - Change group by Column Name functionality*/
  /* Author: BHAGATH */
  changeViewByColumn(cols) {
    this.currencyQualifier = cols.columnName;
    if (this.selectedParentTab == 0) {
      this.selectedChildTab = 2;
      this.groupingHeads = ["Sub Process", "Batch", "Currency"];
    } else if (this.selectedParentTab == 1) {
      this.selectedChildTab = 1;
      this.groupingHeads = ["Period", "Currency"];
    } else {
      this.selectedChildTab = 0;
    }
  }

  /*FUNCTION4 - Show Batch Wise Counts against reconcile/unreconcile button*/
  /* Author: BHAGATH */
  showBatchWiseCounts() {
    this.sourceRecordRCount = 0;
    this.sourceRecordUCount = 0;
    if (this.groupByAnalytics.length > 0) {
      if (this.selectedKeys.length > 0) {
        this.selectedKeys.forEach(item => {
          this.groupByAnalytics.forEach(k => {
            if (item.name == k.name) {
              this.sourceRecordRCount = this.sourceRecordRCount + k.dsCount;
            }
          })
        });
      }
    }
  }

  /*FUNCTION4 -  Function to get Rule Groups STARTS*/
  /* Author: BHAGATH */

  fetchRuleGroups() {
    this.ruleGroupService.fetchRuleGroupsBypurpose('RECONCILIATION').subscribe(
      (res: any) => {
        this.reconRuleGroups = res;
        this.reconRuleGroupId = this.reconRuleGroups[0].id;
        if(!this.commonService.acctDashBoardObject.ruleGroupId){
          this.fetchQueryParams(this.reconRuleGroupId);
        }        
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  /*FUNCTION5 -  Function to get Accounting rule groups STARTS*/
  /* Author: BHAGATH */
  fetchAcctRuleGroups(viewId, purpose) {
    this.ruleGroupService.fetchingGroupsByViewId(viewId, purpose).subscribe(
      (res: any) => {
        this.acctRuleGroups = res;
        if (this.acctRuleGroups.length > 0) {
          this.acctRuleGroupId = this.acctRuleGroups[0].groupId;
          this.runAcctShow = true;
        } else {
          this.runAcctShow = false;
          this.notificationService.info('Info!', 'No Eligble Accounting Processes Found!');
        }
      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Warning!', 'Error Occured while fetching Accounting Rule Groups!');
      }
    );
  }

  /*FUNCTION6 -  Function to get query parameters STARTS*/
  /* Author: BHAGATH */
  fetchQueryParams(groupId) {
    this.showReconAnalytics = false;
    this.selectedKeys = [];
    this.reconRuleGroupId = groupId;
    this.targetDataViews = [];
    this.sourceDataViews = [];
    this.selectedTargetViews = [];
    this.selectedSourceViews = [];
    this.reconcileService.fetchQueryParams(groupId).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.reconRuleGroupName = this.queryParams.ruleGroupName;
        this.sourceDataViews = this.queryParams.source;
        if(this.commonService.reconDashBoardObject.sourceViewId){
          this.sourceDataViewId = this.commonService.reconDashBoardObject.sourceViewId;
          for (var j = 0; j < this.sourceDataViews.length; j++) {
            if(this.sourceDataViews[j].id == this.sourceDataViewId){
              this.targetDataViews= this.sourceDataViews[j].innerTargetViews;
              this.sourceViewName = this.sourceDataViews[j].itemName;
              this.selectedSourceViews = this.sourceDataViews[j];
            }
          }
          this.ruleGroupService.sourceDataViewId = this.sourceDataViewId;
          this.ruleGroupService.sourceDataView = this.sourceViewName;
          this.targetDataViewId = this.targetDataViews[0].id;
          this.ruleGroupService.targetDataViewId = this.targetDataViewId;
          this.targetViewName = this.targetDataViews[0].itemName;
          this.ruleGroupService.targetDataView = this.targetViewName;
          this.selectedTargetViews.push(this.targetDataViews[0]);

          this.onClickColumns(this.sourceDataViewId, 'source');
          this.onClickColumns(this.targetDataViewId, 'target');
          this.fetchReconCountAmount(this.selectedDateRange);
          this.reconTotalAmounts(this.selectedDateRange);
        } else {
          this.sourceDataViewId = this.sourceDataViews[0].id;
          this.sourceViewName = this.sourceDataViews[0].itemName;
          for (var j = 0; j < this.sourceDataViews.length; j++) {
            if(this.sourceDataViews[j].id == this.sourceDataViewId){
              this.targetDataViews = this.sourceDataViews[j].innerTargetViews;
              this.sourceViewName = this.sourceDataViews[j].itemName;
              this.selectedSourceViews = this.sourceDataViews[j];
            }
          }
          this.ruleGroupService.sourceDataViewId = this.sourceDataViewId;
          this.ruleGroupService.sourceDataView = this.sourceViewName;
          this.targetDataViewId = this.targetDataViews[0].id;
          this.ruleGroupService.targetDataViewId = this.targetDataViewId;
          this.targetViewName = this.targetDataViews[0].itemName;
          this.ruleGroupService.targetDataView = this.targetViewName;
          this.selectedSourceViews[0] = this.sourceDataViewId;
          this.selectedTargetViews[0] = this.targetDataViewId;
          this.selectedTargetViews.push(this.targetDataViews[0]);
          this.onClickColumns(this.sourceDataViewId, 'source');
          this.onClickColumns(this.targetDataViewId, 'target');
        }

      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Warning!', 'Error Occured while fetching Query Parameters!');
      }
    );
  }

  /*FUNCTION7 -  on Change View COlumns Functionality STARTS*/
  /* Author: BHAGATH */
  sViewColumns: any[] = [];
  tViewColumns: any[] = [];
  tAmountQualifier: number;
  tCurrencyQualifier: number;
  tDateQualifier: number;
  sCurrencyQualifierName:string;
  tCurrencyQualifierName:string;
  onClickColumns(viewId, type) {
    this.reconcileService.getQualifierColumns(viewId).subscribe(
      (res: any) => {
        if (type == 'source') {
          this.sViewColumns = res;
          for (let i = 0; i < this.sViewColumns.length; i++) {
            if (this.sViewColumns[i].qualifier == 'AMOUNT') {
              this.amountQaulifier = this.sViewColumns[i].columnId;
            } else if (this.sViewColumns[i].qualifier == 'CURRENCYCODE') {
              this.currencyQualifier = this.sViewColumns[i].columnId;
              this.sCurrencyQualifierName = this.sViewColumns[i].columnDisplayName;
            } else if (this.sViewColumns[i].qualifier == 'TRANSDATE') {
              this.dateQualifier = this.sViewColumns[i].columnId;
            }
          }
        } else {
          this.tViewColumns = res;
          for (let i = 0; i < this.tViewColumns.length; i++) {
            if (this.tViewColumns[i].qualifier == 'AMOUNT') {
              this.tAmountQualifier = this.tViewColumns[i].columnId;
            } else if (this.tViewColumns[i].qualifier == 'CURRENCYCODE') {
              this.tCurrencyQualifier = this.tViewColumns[i].columnId;
              this.tCurrencyQualifierName = this.tViewColumns[i].columnDisplayName;
            } else if (this.tViewColumns[i].qualifier == 'TRANSDATE') {
              this.tDateQualifier = this.tViewColumns[i].columnId;
            }
          }
        }
      },
      (res: Response) => {
        this.onError(res.json()
        )
        this.notificationService.error('Warning!', 'Error Occured!');
      }
    )
  }

  /*FUNCTION8 -  Function to get Target Views on Source Select STARTS*/
  /* Author: BHAGATH */

  fetchTargetViews(sViewId) {
    this.sourceDataViewId = sViewId;
    this.targetDataViews = [];
    this.selectedTargetViews = [];
    this.selectedSourceViews =[];
    let targets: any[] = [];
    let tlen: number = 0;
    for (var j = 0; j < this.sourceDataViews.length; j++) {
      const elem = this.sourceDataViews[j];
      if(elem.id == sViewId){
        this.sourceViewName = elem.itemName;
        this.targetDataViews = elem.innerTargetViews;
        this.selectedSourceViews.push(elem);
        this.targetDataViewId = this.targetDataViews[0].id;
        this.selectedTargetViews.push(this.targetDataViews[0]);
      }
    }
    this.ruleGroupService.sourceDataViewId = this.sourceDataViewId;
    this.ruleGroupService.sourceDataView = this.sourceViewName;
    this.ruleGroupService.targetDataViewId = this.targetDataViewId;
    this.targetViewName = this.selectedTargetViews[0].itemName;
    this.ruleGroupService.targetDataView = this.targetViewName;
    this.onClickColumns(this.sourceDataViewId, 'source');
  }

  /*FUNCTION9 - Function to get target View Name on Selection of Target*/
  /* Author: BHAGATH */

  onSelectTargetView(tViewId) {
    for (var j = 0; j < this.targetDataViews.length; j++) {
      const elem = this.targetDataViews[j];
      if(elem.id == tViewId){
        this.targetDataViewId = tViewId;
        this.targetViewName = elem.itemName;
        this.targetDataViews = elem.innerTargetViews;
        this.selectedTargetViews.push(this.targetDataViews[j]);
      }
    }
    this.onClickColumns(this.targetDataViewId, 'target');
  }

  isChecked:boolean = false;
  checkAll(event){
    this.selectedKeys = [];
    if(event == true){
      for (let i = 0; i < this.groupByAnalytics.length; i++) {
        this.selectedKeys.push(this.groupByAnalytics[i]);
      }
      this.loadData();
    } else {
      this.selectedKeys = [];
    }
  }

  /*FUNCTION10 - Function to get Counts and Amounts Functionality*/
  /* Author: BHAGATH */

  fetchReconCountAmount(selectedDateRang) {
    this.buttonRestrict = true;
    this.isChecked = false;
    this.selectedKeys = [];
    this.isSummary = false;
    this.statisticsParams = {};
    this.statisticsParams.groupBy = this.groupBy;
    this.statisticsParams.periodFactor = this.periodFactor;
    this.statisticsParams.status = this.currentReconStatus;
    this.statisticsParams.ruleGroupId = this.reconRuleGroupId;
    this.statisticsParams.sViewId = this.sourceDataViewId;
    this.statisticsParams.tViewId = this.targetDataViewId;
    if(this.groupBy == 'columnName'){
      this.statisticsParams.sColumnId = this.currencyQualifier;
      this.statisticsParams.tColumnId = this.tCurrencyQualifier;
    }
    this.sourceRecordRCount = 0;
    this.sourceRecordUCount = 0;
    this.selectedKeys = [];
    if(!this.commonService.reconDashBoardObject.startDateRange){
      this.rangeTo = new Date(Date.now());
      this.rangeFrom = new Date(Date.now());
      if (selectedDateRang == '7days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 7);
      } else if (selectedDateRang == '15days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 15);
      } else if (selectedDateRang == '30days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 30);
      } else if (selectedDateRang == 'today') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 0);
      } else if (selectedDateRang == 'yesterday') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 1);
      }
      this.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
      this.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
    }
    this.statisticsParams.rangeFrom = this.rangeFrom;
    this.statisticsParams.rangeTo = this.rangeTo;
    // if (groupBy == 'rules' || groupBy == 'days' || groupBy == 'batch') {
    // console.log('statuc params: ' + JSON.stringify(this.statisticsParams));
    this.buttonRestrict = false;
    this.reconcileService.fetchRqCountAmount(this.statisticsParams).subscribe(
      (res: any) => {
        // if($(".ng-progress").hasClass('active')){
        //   $(".ng-progress").removeClass('active');
        // }
        this.commonService.reconDashBoardObject = {};
        $(".group-by md-tab").removeAttr('disabled');
        this.reconAnalytics = res;
        this.groupByAnalytics = this.reconAnalytics.summary;
        this.rwReconQueries.status = "";
        if (this.groupByAnalytics.length > 0) {
          this.showReconAnalytics = true;
          this.isSummary = true;
          this.getColumnHeaders(this.reconRuleGroupId, this.sourceDataViewId, 'source');
          this.getColumnHeaders(this.reconRuleGroupId, this.targetDataViewId, 'target');
          this.callSetInt = true;
        } else {
          this.notificationService.info('Info!', 'No Records found with in Date Range');
        }
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res)
        this.notificationService.error('Warning!', 'Error Occured while fetching recon analytics!');
      }
    );
    // }
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

  sourceRAmount: any;
  sourceUAmount: any;

  reconTotalAmounts(selectedDateRang) {
    this.recAmts.sViewIds = [];
    this.recAmts.tViewIds = [];
    if(!this.commonService.reconDashBoardObject.startDateRange) {
      this.rangeTo = new Date(Date.now());
      this.rangeFrom = new Date(Date.now());
      if (selectedDateRang == '7days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 7);
      } else if (selectedDateRang == '15days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 15);
      } else if (selectedDateRang == '30days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 30);
      } else if (selectedDateRang == 'today') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 0);
      } else if (selectedDateRang == 'yesterday') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 1);
      }
      this.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
      this.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
    }

    this.recAmts.periodFactor = this.periodFactor;
    this.recAmts.rangeFrom = this.rangeFrom;
    this.recAmts.rangeTo = this.rangeTo;
    this.recAmts.ruleGroupId = this.reconRuleGroupId;
    // for (let i = 0; i < sViews.length; i++) {
    //   this.recAmts.sViewIds.push(sViews[i].id);
    // }
    // for (let i = 0; i < tViews.length; i++) {
    //   this.recAmts.tViewIds.push(tViews[i].id);
    // }
    this.recAmts.sViewIds.push(this.sourceDataViewId);
    this.recAmts.tViewIds.push(this.targetDataViewId);
    this.reconcileService.reconAmounts(this.recAmts).subscribe(
      (res: any) => {
        this.sourceRAmount = res.source.reconciled;
        this.sourceUAmount = res.source.unReconciled;
      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Warning!', 'Error Occured while fetching Query Parameters!');
      }
    );
    // }
  }

  /*FUNCTION12 - Function to get source and target amounts comparison*/
  /* Author: BHAGATH */

  getSourceAndTargetTotalonSourceSelect(viewId, selectedRecords, type) {
    this.buttonRestrict = true;
    this.selectedDifference = 0;
    this.selectedTargetAmount = 0;
    this.selectedTargetCount = "";
    this.selectedDifferencePercent = 0;
    let rowIds: any = [];
    let reconRefIds: any = [];
    let recStatus: any;
    if (selectedRecords.length > 0) {
      selectedRecords.forEach(item => {
        rowIds.push(item.Id);
        if (item['Recon Ref Id'] != "") {
          reconRefIds.push(item['Recon Ref Id']);
        }
      });
    }
    if (type == 'source') {
      if (rowIds.length > 0) {
        this.reconcileService.fetchCountAmountonSelectSource(viewId, rowIds, reconRefIds).subscribe(
          (res: any) => {
            this.buttonRestrict = false;
            this.selectedSCountAmount = res;
            this.selectedSourceAmount = this.selectedSCountAmount.amount;
            this.selectedSourceCount = this.selectedSCountAmount.count;
            if (this.selectedSourceAmount) {
              this.selectedDifference = this.selectedSourceAmount - this.selectedTargetAmount;
              this.selectedDifferencePercent = ((this.selectedDifference / this.selectedSourceAmount) * 100);
            }
          },
          (res: Response) => {
            this.buttonRestrict = false;
            this.onError(res.json()
            )
            this.notificationService.error('Warning!', 'Error Occured!');
          }
        )
      }
    } else {
      if (rowIds.length > 0) {
        this.reconcileService.fetchCountAmountonSelectSource(viewId, rowIds, reconRefIds).subscribe(
          (res: any) => {
            this.buttonRestrict = false;
            this.selectedTCountAmount = res;
            this.selectedTargetAmount = this.selectedTCountAmount.amount;
            this.selectedTargetCount = this.selectedTCountAmount.count;
            if (this.selectedSourceAmount) {
              this.selectedDifference = this.selectedSourceAmount - this.selectedTargetAmount;
              this.selectedDifferencePercent = ((this.selectedDifference / this.selectedSourceAmount) * 100);
            }
          },
          (res: Response) => {
            this.buttonRestrict = false;
            this.onError(res.json()
            )
            this.notificationService.error('Warning!', 'Error Occured!');
          }
        )
      }
    }
    this.buttonRestrict = false;
  }

  /*FUNCTION15 - Function to Fetch Records of a data view based on grouping */
  /* Author: BHAGATH */

  getGroupByReconRecords(type) {
    this.isChecked = false;
    this.buttonRestrict = true;
    this.cdr.detectChanges();
    // this.selectedDifference = 0;
    // this.selectedDifferencePercent = 0;
    this.reconKeysObject.days = [];
    this.reconKeysObject.ruleIds = [];
    this.reconKeysObject.batchNames = [];
    this.reconKeysObject.columnValues = {};
    this.rwReconQueries.status = this.currentReconStatus;
    this.rwReconQueries.groupId = this.reconRuleGroupId;
    this.rwReconQueries.groupBy = this.groupBy;
    this.rwReconQueries.rangeFrom = this.rangeFrom;
    this.rwReconQueries.rangeTo = this.rangeTo;
    this.rwReconQueries.sourceOrTarget = type;
    this.rwReconQueries.periodFactor = this.periodFactor;
    this.groupingColumnValues.columnValues = [];
    if(this.selectedKeys.length == this.groupByAnalytics.length){
      this.isChecked = true;
    }
    if (this.selectedKeys.length > 0) {
      for (let i = 0; i < this.selectedKeys.length; i++) {
        if (this.groupBy == 'rules') {
          this.reconKeysObject.ruleIds.push(this.selectedKeys[i].id);
        } else if (this.groupBy == 'batch') {
          this.reconKeysObject.batchNames.push(this.selectedKeys[i].name);
        } else if (this.groupBy == 'days') {
          let newDate = new Date(this.selectedKeys[i].name);
          let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          this.reconKeysObject.days.push(newDate2);
        } else {
          if(type == 'source'){
            this.groupingColumnValues.columnId = this.currencyQualifier;
          } else {
            this.groupingColumnValues.columnId = this.tCurrencyQualifier;
          }
          this.groupingColumnValues.columnValues.push(this.selectedKeys[i].name);
          this.reconKeysObject.columnValues = this.groupingColumnValues;
        }
      }
    this.rwReconQueries.keyValues = this.reconKeysObject;
    if (type == 'source') {
      this.selectedSourceAmount = 0;
      this.selectedSourceCount = "";
      this.selectedSource = [];
      this.rwReconQueries.dataViewId = this.sourceDataViewId;
      this.rwReconQueries.sortOrderBy = this.sortOrderSource;
      this.rwReconQueries.sortByColumnId = this.amountQaulifier;
      this.sourceFileExpObject = jQuery.extend(true, {}, this.rwReconQueries);
      // console.log('recon object : ' + JSON.stringify(this.rwReconQueries));
      this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage, this.rwReconQueries).subscribe(
        (res: any) => {
          this.isCustomFilter = false;
          this.buttonRestrict = false;
          this.sourceLines = res;
          this.reconKeysObject.batchNames = [];
          this.groupingColumnValues.columnValues = [];
          this.groupingColumnValues.columnId = "";
          this.reconKeysObject.columnValues = this.groupingColumnValues;
          this.reconKeysObject.days = [];
          this.reconKeysObject.ruleIds = [];
          if (this.sourceLines.length > 1) {
            this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
            this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
          //   $('html, body').animate({
          //     scrollTop: $("#activityWindow").offset().top - 55
          // }, 2000);
          } else {
            this.notificationService.info('Info!', 'No Records Records!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Warning!', 'Error Occured!');
        }
      )
    } else if (type == 'target') {
      this.selectedTargetCount = "";
      this.selectedTargetAmount = 0;
      this.selectedTarget = [];
      this.rwReconQueries.dataViewId = this.targetDataViewId;
      this.rwReconQueries.sortOrderBy = this.sortOrderTarget;
      this.rwReconQueries.sortByColumnId = this.tAmountQualifier;
      this.targetFileExpObjext = jQuery.extend(true, {}, this.rwReconQueries);
      this.reconcileService.fetchAllReconWqRecords(this.page1, this.itemsPerPage1, this.rwReconQueries).subscribe(
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
            // $('html, body').animate({
            //   scrollTop: $("#activityWindow").offset().top - 55
            // }, 2000);
          } else {
            this.notificationService.info('Info!', 'No Records Records!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Warning!', 'Error Occured!');
        }
      )
    }
  }
  this.buttonRestrict = false;
}

  changeSourceSort(event) {
    for (let i = 0; i < this.sourceHeaderColumns.length; i++) {
      if (this.sourceHeaderColumns[i].header == event.field) {
        this.amountQaulifier = this.sourceHeaderColumns[i].colId;
      }
    }
    if (event.order < 1) {
      this.sortOrderSource = 'desc';
    } else {
      this.sortOrderSource = 'asc';
    }
    this.getGroupByReconRecords('source');
  }

  changeTargetSort(event) {
    for (let i = 0; i < this.targetHeaderColumns.length; i++) {
      if (this.targetHeaderColumns[i].header == event.field) {
        this.tAmountQualifier = this.targetHeaderColumns[i].colId;
      }
    }
    if (event.order < 1) {
      this.sortOrderTarget = 'desc';
    } else {
      this.sortOrderTarget = 'asc'
    }
    this.getGroupByReconRecords('source');
  }


  /*FUNCTION16 - Manual Reconciliation Adding to Sets Functionality */
  /* Author: BHAGATH */

  addSetsForMRecon(groupId, sViewId, tViewId) {
    if (this.selectedSource.length > 0) {
      for (var j = 0; j < this.selectedSource.length; j++) {
        this.manualRSource.push({
          groupId: groupId,
          rowId: this.selectedSource[j].Id,
          viewId: sViewId,
        })
      }
    }
    if (this.selectedTarget.length > 0) {
      for (var k = 0; k < this.selectedTarget.length; k++) {
        this.manualRTarget.push({
          groupId: groupId,
          rowId: this.selectedTarget[k].Id,
          viewId: tViewId,
        })
      }
    }
    if (this.selectedSource.length > 0 && this.selectedTarget.length > 0) {
      this.manualRqObject = {
        source: this.manualRSource,
        target: this.manualRTarget
      }
      this.manualReconArray.push(this.manualRqObject);
      this.manualReconPost();
    }
    this.manualRSource = [];
    this.manualRTarget = [];
    this.selectedSource = [];
    this.selectedTarget = [];
  }


  // /*FUNCTION17 - Manual Reconciliation Functionality Final Post Functionality */
  // /* Author: BHAGATH */

  manualReconPost() {
    this.buttonRestrict = true;
    this.statisticsParams.groupBy = this.groupBy;
    this.statisticsParams.periodFactor = this.periodFactor;
    this.statisticsParams.rangeFrom = this.rangeFrom;
    this.statisticsParams.rangeTo = this.rangeTo;
    this.statisticsParams.ruleGroupId = this.reconRuleGroupId;
    this.statisticsParams.status = this.currentReconStatus;
    this.statisticsParams.sViewId = this.sourceDataViewId;
    this.statisticsParams.tViewId = this.targetDataViewId;
    if (this.manualReconArray.length > 0) {
      this.reconcileService.manualRecon(this.manualReconArray).subscribe(
        (res: any) => {
          this.sourceRecordRCount = 0;
          this.sourceRecordUCount = 0;
          this.manualReconArray = [];
          this.notificationService.success('SUCCESS', 'Selected Records Reconciled Successfully!');
          this.reconcileService.fetchRqCountAmount(this.statisticsParams).subscribe(
            (res: any) => {
              this.buttonRestrict = false;
              this.reconAnalytics = res;
              this.groupByAnalytics = this.reconAnalytics.summary;
            })
          this.getGroupByReconRecords('source');
          this.getGroupByReconRecords('target');
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Warning!', 'Error Occured!');
        }
      )
    } else {
      this.buttonRestrict = false;
      this.notificationService.info('Info!', 'No Records Qualified');
    }
  }



  // /*FUNCTION18 - Manual Un Reconciliation Functionality */
  // /* Author: BHAGATH */

  manualUnRecon(groupId, type, sViewId, groupBy) {
    var dialogRef = this.dialog.open(WqConfirmActionModalDialog, {
      width: '400px',
      data: { ok: 'ok', reason: '', cancel: 'cancel' }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result && result == 'ok') {
        this.buttonRestrict = true;
        if (type == 'recordwise') {
          this.unReconKeysObject.batchNames = [];
          this.groupingColumnValues.columnValues = [];
          this.groupingColumnValues.columnId = "";
          this.unReconKeysObject.columnValues = this.groupingColumnValues;
          this.unReconKeysObject.days = [];
          this.unReconKeysObject.ruleIds = [];
          this.unReconKeysObject.viewId = 0;
          this.reconKeysObject.viewId = 0;
          let recRefs: any = [];
          if (this.selectedSource.length > 0) {
            this.selectedSource.forEach(item => {
              recRefs.push(item['Recon Ref Id']);
            });
          }
          this.unReconKeysObject.reconReferences = recRefs;
          this.reconcileService.manualUNRecon(groupId, type, this.rangeFrom, this.rangeTo, groupBy, sViewId, this.unReconKeysObject).subscribe(
            (res: any) => {
              this.sourceRecordRCount = 0;
              this.sourceRecordUCount = 0;
              this.notificationService.success('SUCCESS', 'Selected Records UnReconciled Successfully!');
              this.fetchReconCountAmount(this.selectedDateRange);
              this.loadData();
            },
            (res: Response) => {
              this.onError(res.json()
              )
              this.notificationService.error('Warning!', 'Error Occured!');
            }
          )
        } else {
          this.unReconKeysObject.reconReferences = [];
          this.unReconKeysObject.viewId = 0;
          if (this.selectedKeys.length > 0) {
            this.selectedKeys.forEach(item => {
              if (groupBy == 'rules') {
                this.reconKeysObject.ruleIds.push(item.id);
              } else if (groupBy == 'batch') {
                this.reconKeysObject.batchNames.push(item.name);
              } else if (groupBy == 'days') {
                let newDate = new Date(item.name);
                let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
                this.reconKeysObject.days.push(newDate2);
              } else {
                this.groupingColumnValues.columnValues.push(item.name);
                this.groupingColumnValues.columnId = this.currencyQualifier;;
                this.reconKeysObject.columnValues = this.groupingColumnValues;
              }
            });
            this.reconcileService.manualUNRecon(groupId, type, this.rangeFrom, this.rangeTo, groupBy, sViewId, this.reconKeysObject).subscribe(
              (res: any) => {
                this.reconKeysObject.ruleIds = [];
                this.reconKeysObject.batchNames = [];
                this.unReconKeysObject.reconReferences = [];
                this.unReconKeysObject.viewId = 0;
                this.reconKeysObject.days = [];
                this.reconKeysObject.ruleIds = [];
                this.groupingColumnValues.columnId = "";
                this.groupingColumnValues.columnValues = [];
                this.reconKeysObject.columnValues = this.groupingColumnValues;
                this.sourceRecordRCount = 0;
                this.sourceRecordUCount = 0;
                this.notificationService.success('SUCCESS', 'Selected transactions UnReconciled Successfully!');
                this.fetchReconCountAmount(this.selectedDateRange);
                this.loadData();
                this.buttonRestrict = false;
              },
              (res: Response) => {
                this.buttonRestrict = false;
                this.onError(res.json()
                )
                this.notificationService.error('Warning!', 'Unable to unreconcile the selected transactions!');
              }
            )
          } else {
            this.buttonRestrict = false;
            this.notificationService.info('Info!', 'No transactions Selected!');
          }

        }
      }
    });
  }

  /*FUNCTION19 - Function to get Column headers of a data view STARTS */
  /* Author: BHAGATH */

  getColumnHeaders(groupId, viewId, type) {
    this.reconcileService.getHeaderColumns(groupId, viewId, type).subscribe(
      (res: any) => {
        if (type == 'source') {
          this.sourceHeaderColumns1 = res;
          this.sourceHeaderColumns = this.sourceHeaderColumns1.columns;
          if (this.sourceHeaderColumns.length > 0) {
            this.sourceHeaderColumns.splice(0, 1);
            this.sourceColumnOptions = [];
            for (let i = 0; i < this.sourceHeaderColumns.length; i++) {
              this.sourceHeaderColumns[i]['searchWord'] = "";
              this.sourceColumnOptions.push({ label: this.sourceHeaderColumns[i].header, value: this.sourceHeaderColumns[i] });
            }
          } else {
            this.notificationService.info('Incorrect Inputs!', 'Unable to fetch Column Headers for' + viewId);
          }
        } else if (type == 'target') {
          this.targetHeaderColumns1 = res;
          this.targetHeaderColumns = this.targetHeaderColumns1.columns;
          if (this.targetHeaderColumns.length > 0) {
            this.targetHeaderColumns.splice(0, 1);
            this.targetColumnOptions = [];
            for (let i = 0; i < this.targetHeaderColumns.length; i++) {
              this.targetHeaderColumns[i]['searchWord'] = "";
              this.targetColumnOptions.push({ label: this.targetHeaderColumns[i].header, value: this.targetHeaderColumns[i] });
            }
          } else {
            this.notificationService.info('Incorrect Inputs!', 'Unable to fetch Column Headers for' + viewId);
          }
        }
      },
      (res: Response) => {
        this.onError(res.json()
        )
        this.notificationService.error('Warning!', 'Error Occured!');
      }
    )
  }


  /*FUNCTION20 - Function to fetch target records based on Custom Filters STARTS */
  /* Author: BHAGATH */
  totalLines:any = {};
  isCustomFilter:boolean = false;
  fetchTargetOnSourceRecord(groupId, sViewId, tViewId, selectedrecords) {
    this.isReconciled = false;
    this.buttonRestrict = true;
    this.selectedSourceAmount = 0;
    this.selectedSourceCount = "";
    this.selectedTargetCount = "";
    this.selectedTargetAmount = 0;
    this.selectedTarget = [];
    this.selectedDifference = 0;
    this.selectedDifferencePercent = 0;
    let sInfo:any[] = [];
    let sRecords:any[]=[];
    this.totalLines = {};
    if (selectedrecords.length > 0) {
      let rowIds: any = [];
      for (let i = 0; i < selectedrecords.length; i++) {
          rowIds.push(selectedrecords[i].Id);
      }
      if (this.currentReconStatus == 'reconciled') {
        this.reconcileService.fetchTRecordsCustomFilter(groupId, sViewId, tViewId,this.tAmountQualifier,this.amountQaulifier,this.sortOrderTarget, this.page1, this.itemsPerPage1, rowIds).subscribe(
          (res: any) => {
            this.isCustomFilter = true;
            this.totalLines = res;
            this.buttonRestrict = false;
            // console.log('total lines;'+JSON.stringify(this.totalLines));
            this.targetLines = this.totalLines['target'];
            sRecords = this.totalLines['source'];
            if (this.targetLines.length > 1) {
              sInfo = sRecords.splice(sRecords.length - 1, 1);
              this.selectedSourceAmount = sInfo[0].info.amount;
              this.selectedSourceCount = sInfo[0].info.count;
              this.targetLength = this.targetLines[this.targetLines.length - 1].info.count;
              this.targetDataInfo = this.targetLines.splice(this.targetLines.length - 1, 1);
              this.selectedTargetAmount = this.targetDataInfo[0].info.amount;
              this.selectedTargetCount = this.targetDataInfo[0].info.count;
              this.selectedDifference = this.selectedSourceAmount - this.selectedTargetAmount;
              this.selectedDifferencePercent = ((this.selectedDifference / this.selectedSourceAmount) * 100);
              this.isReconciled = true;
              this.isUnReconciled = false;
            } else {
              this.notificationService.info('Info!', 'No Records Matched with current filter!');
            }
          },
          (res: Response) => {
            this.isCustomFilter = false;
            this.buttonRestrict = false;
            this.onError(res.json()
            )
            this.notificationService.error('Warning!', 'Error Occured!');
          }
        )
      } else {
        this.isReconciled = false;
        this.isUnReconciled = true;
        this.getSourceAndTargetTotalonSourceSelect(sViewId, selectedrecords, 'source');
      }
    } else {
      this.loadData();
      this.buttonRestrict = false;
    }
  }

  /* FUNCTION22 - On Load Function */
  /* Author : BHAGATH */
  groupingHeads: any[] = [];
  reconTableHeights: any;
  summaryHeight: any;
  ngOnInit() {
    this.reconTableHeights = (this.commonService.screensize().height - 300) + 'px';
    this.summaryHeight = (this.commonService.screensize().height - 500) + 'px';
    this.sourceSelectionSettings = {
      text: "Select View(s)",
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      classes: "myclass custom-class",
      disabled: false,
      enableCheckAll: true
    };
    //this.jobDetailsService.getUserInfo();
    this.fetchRuleGroups();
      if(this.commonService.reconDashBoardObject.ruleGroupId){
        this.fetchQueryParams(this.commonService.reconDashBoardObject.ruleGroupId);
        this.rangeFrom = this.commonService.reconDashBoardObject.startDateRange;
        this.rangeTo = this.commonService.reconDashBoardObject.endDateRange;
        if(this.commonService.reconDashBoardObject.status == 'reconciled'){
          this.groupingHeads = ["Sub Process","Batch","Currency"];
          this.groupBy = "rules";
          this.selectedParentTab = 0;
          this.selectedAmountTab = 0;
          this.selectedChildTab = 0;
        } else {
          this.groupingHeads = ["Period", "Currency"];
          this.groupBy = "days";
          this.selectedParentTab = 1;
          this.selectedAmountTab = 1;
          this.selectedChildTab = 0;
        }
      this.currentReconStatus = this.commonService.reconDashBoardObject.status;
      } 
      else {
      this.selectedParentTab = 1;
      this.groupingHeads = ["Period", "Currency"];
      this.selectedChildTab = 0;
      this.groupBy = "days";
      this.selectedAmountTab = 1;
      this.currentReconStatus = "unreconciled";
    }

    $(".split-example").css({
      'height': 'auto',
      'min-height': (this.commonService.screensize().height - 130) + 'px'
    });

    this.principal.identity().then((account) => {
      this.currentAccount = account;
    });

  }


  /* FUNCTION23 : Function for Source pagination based on filtering */
  /* AUTHOR : BHAGATH */

  onSPaginateChange(event) {
    this.page = event.pageIndex;
    this.itemsPerPage = event.pageSize;
    if (this.selectedKeys.length > 0) {
      // this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage,this.sourceFileExpObject).subscribe(
      //   (res: any) => {
      //     this.buttonRestrict = false;
      //     this.sourceLines = res;
      //     if (this.sourceLines.length > 1) {
      //       this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
      //       this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
      //     } else {
      //       this.notificationService.info('Info!', 'No Records Records!');
      //     }
      //   },
      //   (res: Response) => {
      //     this.buttonRestrict = false;
      //     this.onError(res.json()
      //     )
      //     this.notificationService.error('Warning!', 'Error Occured!');
      //   }
      // )
      this.getGroupByReconRecords('source');
    }
  }

  /* FUNCTION4 : Function for Target pagination based on filtering */
  /* AUTHOR : BHAGATH */

  onTPaginateChange(event) {
    this.page1 = event.pageIndex;
    this.itemsPerPage1 = event.pageSize;
    if(this.isCustomFilter == false){
      this.getGroupByReconRecords('target');
    } else {
      let sInfo:any[] = [];
      let sRecords:any[]=[];
      this.totalLines = {};
      let rowIds: any = [];
      for (let i = 0; i < this.selectedSource.length; i++) {
          rowIds.push(this.selectedSource[i].Id);
      }
      if (this.currentReconStatus == 'reconciled') {
        this.reconcileService.fetchTRecordsCustomFilter(this.reconRuleGroupId,this.sourceDataViewId,this.targetDataViewId,this.tAmountQualifier,this.amountQaulifier,this.sortOrderTarget, this.page1, this.itemsPerPage1, rowIds).subscribe(
          (res: any) => {
            this.isCustomFilter = true;
            this.totalLines = res;
            this.buttonRestrict = false;
            this.targetLines = this.totalLines['target'];
            if (this.targetLines.length > 1) {
             
            } else {
              this.notificationService.info('Info!', 'No Records Matched with current filter!');
            }
          },
          (res: Response) => {
            this.isCustomFilter = false;
            this.buttonRestrict = false;
            this.onError(res.json()
            )
            this.notificationService.error('Warning!', 'Error Occured!');
          }
        )
      }
    }
    
  }

  scheduleJobRecon() {
    this.reconSchedObj = {};
    this.reconSchedParamms = [];
    let fromDate: any = "";
    let toDate: any = "";
    let colValues: string = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    console.log('range form :' + fromDate + ' + ' + 'Range To: ' + toDate);
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
        this.selectedKeys.forEach(item => {
          let newDate = new Date(item.name);
          let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          if (colValues.length > 1) {
            colValues = colValues + "," + newDate2;
          } else {
            colValues = newDate2;
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
            selectedValue: this.dateQualifier
          },
          {
            paramName: 'Filter Values',
            selectedValue: colValues
          }
        ];
      } else {
        this.selectedKeys.forEach(item => {
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
  oozieTestRecon: any;
  dateRange: any;
  initiateReconJob(purpose) {
    let fromDate: any = "";
    let toDate: any = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    // console.log('range form :' + this.rangeFrom + ' + ' + 'Range To: ' + this.rangeTo);
    this.dateRange = [fromDate, toDate];
    let colValues: any = [];
    if (purpose == 'RECONCILIATION') {
      if (this.groupBy == 'days') {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach(item => {
            let newDate = new Date(item.name);
            let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            colValues.push(newDate2);
          });
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.sourceDataViewId,
            'param4': this.dateQualifier,
            'param5': colValues,
            'param6': this.dateRange
          }
        }
        // this.initiatingJob(purpose, this.paramSet);
        colValues = [];
      } else {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach(item => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.sourceDataViewId,
            'param4': this.currencyQualifier,
            'param5': colValues,
            'param6': this.dateRange
          }
        }
        // this.initiatingJob(purpose, this.paramSet);
        colValues = [];
      }
    } else {
      if (this.groupBy == 'days') {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach(item => {
            let newDate = new Date(item.name);
            let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            colValues.push(newDate2);
          });
          this.paramSet = {
            'param1': this.acctRuleGroupId,
            'param3': this.sourceDataViewId,
            'param4': this.dateQualifier,
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
        // this.initiatingJob(purpose, this.paramSet);
        colValues = [];
      } else {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach(item => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.acctRuleGroupId,
            'param3': this.sourceDataViewId,
            'param4': this.groupBy,
            'param5': colValues,
            'param6': this.dateRange
          }
        }
        // this.initiatingJob(purpose, this.paramSet);
        colValues = [];

      }
    }
    this.jobDetailsService.oozieDBTest().subscribe((res: any) => {
      this.oozieTestRecon = res.ooziestatus;
      if (this.oozieTestRecon == true) {
        this.initiatingJob(purpose, this.paramSet);
      } else {
        this.notificationService.error('Warning!', 'Job Engine Start Up Failure...Please try again later');
      }
    }, (res: Response) => {
      this.onError(res.json())
      this.notificationService.error('Error!', 'Error while checking Oozie status!');
    }
    )
  }
  displayConfirmDialog: boolean = false;
  beforeJobInitiate() {
    if (this.selectedKeys.length > 0) {
      this.displayConfirmDialog = true;
    } else {
      this.notificationService.info('Info', 'No Records Selected');
    }
  }


  initiatingJob(purpose, set) {
    this.buttonRestrict = true;
    if (purpose == 'RECONCILIATION') {
      this.jobDetailsService.initiateAcctRecJob('RECONCILIATION', set).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.reconJobOutput = res;
        this.reconJobId = this.reconJobOutput.status;
        console.log('recon job ref:' + JSON.stringify(this.reconJobId));

        if (this.reconJobId != 'Failed to intiate job') {
          this.notificationService.success('Info!', 'Job Initiated Successfully!');
          this.setIntId = setInterval(() => {
            if (this.callSetInt == true) {
              this.jobDetailsService.oozieeJobStatus(this.reconJobId).subscribe((res: any) => {
                this.reconJobStatus = res;
                console.log('recon job status:' + JSON.stringify(this.reconJobStatus._body));
                this.fetchReconCountAmount(this.selectedDateRange);
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
                (res: Response) => {
                  clearInterval(this.setIntId);
                  this.sourceRecordUCount = 0;
                  this.sourceRecordRCount = 0;
                  this.onError(res.json())
                  this.notificationService.error('Error!', 'Failed to get job status!');
                }
              )
            }
          }, 30000);
        } else {
          this.notificationService.error('Error!', 'Failed to intiate job!');
          this.sourceRecordUCount = 0;
          this.sourceRecordRCount = 0;
        }
      },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json())
          this.notificationService.error('Error!', 'Failed to intiate job!');
        }
      )

    } else {
      this.jobDetailsService.initiateAcctRecJob('ACCOUNTING', set).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.acctJobOutput = res;
        this.acctJobId = this.acctJobOutput.status;
        console.log('recon job ref:' + JSON.stringify(this.acctJobId));
        if (this.acctJobId != 'Failed to intiate job') {
          this.notificationService.success('Info!', 'Job Initiated Successfully!');
        } else {
          this.notificationService.error('Error!', 'Failed to intiate accounting job!');
        }
      },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json())
          this.notificationService.error('Error!', 'Failed to intiate job!');
        }
      )
    }
  }

  /* Default Functions Starts*/
  trackId(index: number, item: Reconcile) {
    return item.id;
  }

  private onSuccess(data, headers) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.reconciles = data;
  }

  private onError(error) {
    this.alertService.error(error.message, null, null);
  }

  /* FUNCTION25 : Function to stop Auto Refresh Functionality on Destroy */
  /* AUTHOR : BHAGATH */

  ngOnDestroy() {
    if (this.setIntId) {
      clearInterval(this.setIntId);
    }
    // this.eventManager.destroy(this.eventManager.subscribe('lineCriteriaListModification', (response) => this.fetchReconCountAmount(this.selectedDateRange)));
  }

  /* FUNCTION26 : Function to get data view columns by qualifier */
  /* AUTHOR : BHAGATH */

  // fetchQualifierColumns(qualifier, viewId) {
  //   this.reconcileService.getQualifierColumns(qualifier, viewId).subscribe((res: any) => {
  //     this.qualifierColumns = res;
  //     this.qualifierColumnId = this.qualifierColumns.columnId;
  //     console.log('qualifier Id:' + this.qualifierColumnId);
  //   },
  //     (res: Response) => {
  //       this.onError(res.json())
  //       this.notificationService.error('Warning!', 'Error Occured!');
  //     }
  //   )
  // }

  hideSchDialog(event) {
    this.reconcileService.reconSchJob = false;
  }

  buildRule() {
    this.reconcileService.ENABLE_RULE_BLOCK = true;
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
      this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage, this.sourceFileExpObject).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.sourceLines = res;
          if (this.sourceLines.length > 1) {
            this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
            this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
          } else {
            this.notificationService.info('Info!', 'No Records Records!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Warning!', 'Error Occured!');
        }
      )
    } else {
      this.page1 = 0;
      this.selectedTargetCount = "";
      this.selectedTargetAmount = 0;
      this.selectedTarget = [];
      this.targetFileExpObjext['searchWord'] = searchkey;
      this.reconcileService.fetchAllReconWqRecords(this.page1, this.itemsPerPage1, this.targetFileExpObjext).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.targetLines = res;
          if (this.targetLines.length > 1) {
            this.targetLength = this.targetLines[this.targetLines.length - 1].info.totalCount;
            this.targetDataInfo = this.targetLines.splice(this.targetLines.length - 1, 1);
          } else {
            this.notificationService.info('Info!', 'No Records Records!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    }
  }

  colLevelSearchWithKeyWord(type) {
    this.buttonRestrict = true;
    this.rwReconQueries.columnSearch = [];
    if(type == 'source'){
    this.page = 0;
    this.selectedSourceAmount = 0;
    this.selectedSourceCount = "";
    this.selectedSource = [];
      for (let i = 0; i < this.sourceHeaderColumns.length; i++) {
        if(this.sourceHeaderColumns[i].searchWord != ""){
          this.rwReconQueries.columnSearch.push({
            columnId: this.sourceHeaderColumns[i].colId,
            searchWord: this.sourceHeaderColumns[i].searchWord
          })
        }
      }
      this.sourceFileExpObject['columnSearch'] = this.rwReconQueries.columnSearch;
      this.reconcileService.fetchAllReconWqRecords(this.page, this.itemsPerPage, this.sourceFileExpObject).subscribe(
        (res: any) => {
          this.rwReconQueries.columnSearch = [];
          this.buttonRestrict = false;
          this.sourceLines = res;
          if (this.sourceLines.length > 1) {
            this.sourceLength = this.sourceLines[this.sourceLines.length - 1].info.totalCount;
            this.sourceDataInfo = this.sourceLines.splice(this.sourceLines.length - 1, 1);
          } else {
            this.notificationService.info('Info!', 'No Records Records!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Warning!', 'Error Occured!');
        }
      )
    } else {
    this.page1 = 0;
    this.selectedTargetCount = "";
    this.selectedTargetAmount = 0;
    this.selectedTarget = [];
      for (let i = 0; i < this.targetHeaderColumns.length; i++) {
        if(this.targetHeaderColumns[i].searchWord != ""){
          this.rwReconQueries.columnSearch.push({
            columnId: this.targetHeaderColumns[i].colId,
            searchWord: this.targetHeaderColumns[i].searchWord
          })
        }
      }
      this.targetFileExpObjext['columnSearch'] = this.rwReconQueries.columnSearch;
      this.reconcileService.fetchAllReconWqRecords(this.page1, this.itemsPerPage1, this.targetFileExpObjext).subscribe(
        (res: any) => {
          this.rwReconQueries.columnSearch = [];
          this.buttonRestrict = false;
          this.targetLines = res;
          if (this.targetLines.length > 1) {
            this.targetLength = this.targetLines[this.targetLines.length - 1].info.totalCount;
            this.targetDataInfo = this.targetLines.splice(this.targetLines.length - 1, 1);
          } else {
            this.notificationService.info('Info!', 'No Records Records!');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json()
          )
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    }
  }

  recDataFileExp() {
    this.buttonRestrict = true;
    let reportURL: string = "http://192.168.0.44";
    this.reconFileExpList = [];
    this.reconFileExpList = [this.sourceFileExpObject, this.targetFileExpObjext];
    this.reconcileService.recDataExcelExport(this.reconFileExpList).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        reportURL = reportURL + res['excelFilePath'];
        console.log('reportURL' + reportURL);
        var link = document.createElement('a');
        link.setAttribute('download', null);
        link.style.display = 'none';
        document.body.appendChild(link);
        link.setAttribute('href', reportURL);
        link.click();
        document.body.removeChild(link);
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res.json()
        )
        this.notificationService.error('Error!', 'Download Failed!');
      }
    )
  }

  fetchFormConfig() {
    let filterString: any;
    let filterString1: any;
    let filterString2: any;
    this.reconcileService.fetchFormConfigData(this.sourceDataViewId, this.targetDataViewId).subscribe((res: any) => {
      this.formConfigObject = res;
      this.formFilterArray = this.formConfigObject['filterValues'];
      // console.log(JSON.stringify(this.formConfigObject));
    },
      (res: Response) => {
        this.onError(res.json())
        this.notificationService.error('Error!', 'Error While Fetching Filter Configurations');
      }
    )
  }

  // postFormConfig(sViewId, tViewId, formConfigData) {
  //   this.buttonRestrict = true;
  //   this.reconcileService.postCustomFitlerConfig(sViewId, tViewId, formConfigData).subscribe((res: any) => {
  //     this.showFilterView = false;
  //     this.buttonRestrict = false;
  //   },
  //     (res: Response) => {
  //       this.buttonRestrict = false;
  //       this.onError(res.json())
  //       this.notificationService.error('Error!', 'Error While posting Filter Configurations');
  //     }
  //   )
  // }

}