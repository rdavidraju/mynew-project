import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription, Observable } from 'rxjs/Rx';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { FormGroup, FormBuilder, FormArray, Validators, NgForm } from '@angular/forms';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { RuleGroupService } from '../rule-group/rule-group.service';
import {
  AccountingData, AccountingDataBreadCrumbTitles, acctCustomFilters,
  acctFilterColumns, jsonforAccounting, selectedAcctRows, AcctDataQueries,
  acctKeyValues, groupByColumnValues, filteredParams, recordParams, credits, segValues,debits, Car, awqAllParams, groupingFilterParams
} from './accounting-data.model';
import { AccountingDataService } from './accounting-data.service';
import { PageEvent } from '@angular/material';
import { CommonService } from '../common.service';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, DialogModule, InputMaskModule, TreeTableModule, TreeNode, SharedModule } from 'primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SessionStorageService } from 'ng2-webstorage';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { DataViewsService } from '../data-views/data-views.service';
import { LookUpCodeService } from '../look-up-code/look-up-code.service'
import { RuleGroupAndRuleWithLineItem } from '../rule-group/ruleGroupAndRuleWithLineItem.model';
import { ReconcileService } from '../reconcile/reconcile.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { DatePipe,DecimalPipe ,CurrencyPipe} from '@angular/common';
import { ChartOfAccountService } from '../chart-of-account/chart-of-account.service';
import { LedgerDefinitionService } from '../ledger-definition/ledger-definition.service';
import { SegmentsService } from '../segments/segments.service';
import { SchedulingModel, Parameters } from '../scheduling/scheduling.component';
import { ProjectService } from '../project/project.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'pivottable/dist/pivot.min.js';
import {AcctDashBoardObject} from '../../shared/constants/constants.values';
declare var $: any;
declare var jQuery: any;


@Component({
  selector: 'acct-confirm-modal',
  templateUrl: 'acct-confirm-modal.html'
})
export class AcctConfirmModal {
  constructor(
      public dialogRef: MdDialogRef<AcctConfirmModal>,
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
  currentAccount: any;
  accountingData: AccountingData[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any = 0;
  pageSizeOptions = [10, 25, 50, 100];;
  pageEvent: PageEvent;
  predicate: any;
  previousPage: any;
  selectedCars2: any[] = [];
  searchWord: any = "";
  reverse: any;
  selectedDataLinesToAccount: any[] = [];
  paramSet: any;
  sourceHeaderColumns1: any;
  /* Custom Variables */
  customFilterStatus: any;
  isVisibleA: boolean = true;
  isVisibleB: boolean = true;
  ruleGroupId: any;
  recordsForAccounting: jsonforAccounting;
  rowForAccounting: selectedAcctRows[] = [];
  ruleGroupName: any;
  dataViewId: any; ChartOfAccount
  dataViewName: any;
  groupBy: any = 'process';
  status: any;
  mainStatus:string="unaccounted";
  isAcctProcess: boolean = false;
  isAcctProcess2: boolean = false;
  dataViews: any = [];
  ruleGroups: any[]=[];;
  queryParams: any;
  acctKeysObject: acctKeyValues = {};
  // queryParams: QueryParams;
  aqAcctQueries: recordParams = {};
  groupingColumnValues: groupByColumnValues = {};
  showColSelection: boolean = false;
  isCustomRange: boolean = false;
  selectedDateRange: string = '7days';
  rangeTo: any;
  toRange: any;
  rangeFrom: any;
  fromRange: any;
  acctAnalytics: any;
  groupByAnalytics: any[] = [];
  filteredAcctAnalytics: any;
  filteredGroupByAnalytics: any[] = [];
  showAcctAnalytics: boolean = false;
  selectedKeys: any[] = [];
  selectedAKeys: any[] = [];
  selectedUKeys: any[] = [];
  selectedPKeys: any[] = [];
  dataHeaders: any[] = [];
  dataHeaderColumns: any[] = [];
  dataColumnOptions: SelectItem[];
  dataViewLines: any[] = [];
  dataViewLinesObject: any;
  treeDataViewLines: TreeNode[] = [];
  viewLength: any;
  dataViewInfo: any;
  dataViewQualifier: any;
  // selectedData: any[] = [];
  selectedData: TreeNode[] = [];
  viewColumns: any[];
  viewColumnNames: any;
  viewColumnId: any;
  setIntId: any;
  callSetInt: boolean = false;
  //   groupingColumnValues: groupByColumnValues = {};
  acctJobId: string;
  acctJobOutput: any
  acctJobStatus: any;
  accountedCount: number = 0;
  unAccountedCount: number = 0;
  partialAccountedCount: number = 0;
  recChangeColor: string = 'mat-raised-button';
  unRecChangeColor: string = 'mat-raised-button';
  dateRange: any[] = [];
  qualifierColumns: any[] = [];
  currencyQualifiers: any;
  qualifierColumnId: any;
  pieChartOptions: any;
  public doughnutChartType: string = 'pie';
  pieChartMetrics: any[] = [];
  displayConfirmDialog: boolean = false;
  sampleData: any[] = [];
  filteredGroupBy: any = 'rules';
  filterGroupingParams: filteredParams = {};
  selectedTab: any;
  selectedDefaultVal: any;
  showCharts: boolean = true;
  accountModalTog: boolean = false;
  ledgername: any;
  acctSource: any;
  acctCategory: any;
  chartOfAccountsList: any[] = [];
  listOfLedgers: any[] = [];
  chartOfAccountName: any;
  chartOfAccount: any;
  sourceLookups: any;
  categoryLookups: any;
  debitLineCode: any = [];
  creditLineCode: any = [];
  segmentsList: any;
  debitString: string = "";
  creditString: string = "";
  debits: debits[] = [];
  credits: credits[] = [];
  debitMask: string = "";
  debitPlaceHolder: string = "";
  dvColumnsByType: any[] = [];
  decimalColId: any;
  currencyQualifier: any;
  reconRuleGroupId: any;
  reconRuleGroups: any[] = [];
  reconJobId: string;
  reconJobOutput: any
  reconJobStatus: any;
  objectForUnRecon: any;
  create: string = 'createInWQ';
  showBatchUnAcct: boolean = false;
  acctSchedObj: SchedulingModel = {};
  acctSchedParamms: Parameters[] = [];
  displayScheduling: boolean = false;
  runAcctShow: boolean = false;
  acctPivotView: boolean = false;
  pvtAcctResult: any = [];
  pvtInputObject: any;
  buttonRestrict: boolean = false;
  sortOrderColId: any;
  groupingHeads: any[] = [];
  summaryJson:any[]=[];


  //Modified Variables**//
  period: string = 'fileDate';
  selectedParentTab: any;
  selectedAmountTab: any;
  selectedChildTab: any;
  statusSummaryParams = new awqAllParams();
  groupingSummaryParams = new awqAllParams();
  dataParams = new awqAllParams();
  statusSummary: any[] = [];
  groupingSummary: any[] = [];
  groupingSummaryFilters: any[] = [];
  statusSummaryObject: any = {};
  filterValues: groupingFilterParams[];
  leftPane:string = "0px";
  isVisibleD:boolean = false;
  showFilters:boolean = true;



  public pieChartType: string = 'pie';
  constructor(
    public accountingDataService: AccountingDataService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager,
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig,
    private ruleGroupService: RuleGroupService,
    private commonService: CommonService,
    private $sessionStorage: SessionStorageService,
    private notificationService: NotificationsService,
    private dataViewsService: DataViewsService,
    private lookUpCodeService: LookUpCodeService,
    public reconcileService: ReconcileService,
    private jobDetailsService: JobDetailsService,
    private datePipe: DatePipe,
    private chartOfAccountService: ChartOfAccountService,
    private ledgerDefinitionService: LedgerDefinitionService,
    private segmentService: SegmentsService,
    private projectService: ProjectService,
    private _sanitizer: DomSanitizer,
    private builder: FormBuilder,
    public dialog: MdDialog,

  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;

    this.summaryJson = [
      {
        "sourceName" : "Receivables",
        "category" : "Sales",
        "Account" : "Receivables",
        "Balance" : "$ 1,245,900.00",
        "codeCombination" : "01-000-20100-00-00"
      },
      {
        "sourceName" : "Payables",
        "category" : "Purchages",
        "Account" : "Payables",
        "Balance" : "$ 1,971,914.00",
        "codeCombination" : "01-000-20200-00-00"
      },
      {
        "sourceName" : "Adjustments",
        "category" : "Adjustments",
        "Account" : "Bad Debts",
        "Balance" : "₹ (7,900).00",
        "codeCombination" : "01-000-10100-00-00"
      },
      {
        "sourceName" : "Revenue",
        "category" : "Beta Online CC Cash",
        "Account" : "Cash in Transit",
        "Balance" : "$ 57,245,470.00",
        "codeCombination" : "01-000-20300-00-00"
      }
    ]
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    if (routeSnapshot.data.breadcrumb === AccountingDataBreadCrumbTitles.accountingDataNew) {
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

  /**********F1*************/
  /***Toggle Query Window***/
  toggleSB() {
    if (!this.isVisibleA) {
      this.isVisibleA = true;
    } else {
      this.isVisibleA = false;
    }
  }

  /**********F2*************/
  /***Fetching Accounting Rule Groups***/
  isMultiCurrency:boolean;
  fxRate:any;
  conversionDate:any;
  fetchRuleGroups() {
    this.ruleGroupService.fetchRuleGroupsBypurpose('ACCOUNTING').subscribe(
      (res: any) => {
        this.ruleGroups = res;
        this.isMultiCurrency = this.ruleGroups[0].multiCurrency;
        this.fxRate = this.ruleGroups[0].fxRateId;
        this.conversionDate = this.ruleGroups[0].conversionDate;
        if(!this.commonService.acctDashBoardObject.ruleGroupId){
          this.ruleGroupId = this.ruleGroups[0].id;
          this.fetchQueryParams(this.ruleGroupId);
        } else {
          this.ruleGroupId = this.commonService.acctDashBoardObject.ruleGroupId;
        }
      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Error!', 'Error Occured while fetching Reconciliation Rule Groups!');
      }
    );
  }

  /**********F3*************/
  /***Fetching query parameters on rule group selection***/
  fetchQueryParams(groupId) {
    for (let i = 0; i < this.ruleGroups.length; i++) {
      if(groupId === this.ruleGroups[i].id){
        this.isMultiCurrency = this.ruleGroups[i].multiCurrency;
        this.fxRate = this.ruleGroups[i].fxRateId;
        this.conversionDate = this.ruleGroups[i].conversionDate;
      }
    }
    this.ruleGroupId = groupId;
    this.accountingDataService.fetchAcctQueryParams(groupId).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.showAcctAnalytics = false;
        this.ruleGroupName = this.queryParams.ruleGroupName;
        this.dataViews = this.queryParams.dataViews;
        if(this.commonService.acctDashBoardObject.dataViewId){
          this.dataViewId = this.commonService.acctDashBoardObject.dataViewId;
          this.dataViewName = this.commonService.acctDashBoardObject.dataViewName;
          this.ruleGroupId = this.commonService.acctDashBoardObject.ruleGroupId;
          this.rangeFrom = this.commonService.acctDashBoardObject.startDateRange;
          this.rangeTo = this.commonService.acctDashBoardObject.endDateRange;
          this.getStatusLevelSummary();
        } else {
          this.dataViewId = this.dataViews[0].id;
          this.dataViewName = this.dataViews[0].viewName;
        }
        
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

/**********F3*************/
  /* Distinct Status level Summary tabs*/
  onChangeMainStatus(){
    if(this.mainStatus == 'accounted'){
      this.statusSummary = this.accountedSummary
    } else if(this.mainStatus == 'unaccounted'){
      this.statusSummary = this.unAccountedSummary;
    } else {
      this.statusSummary = this.statusSummaryObject.summary;
    }
    this.getSideBarGrouping('');
  }
  accountedSummary:any[]=[];
  unAccountedSummary:any[]=[];
  allStatusSummary:any[]=[];
  getStatusLevelSummary() {
    this.accountedSummary = [];
    this.unAccountedSummary = [];
    this.showAcctAnalytics = false;
    this.buttonRestrict = true;
    this.statusSummaryParams.groupId = this.ruleGroupId;
    this.statusSummaryParams.viewId = this.dataViewId;
    this.statusSummaryParams.periodFactor = this.period;
    if(!this.commonService.acctDashBoardObject.startDateRange){
      this.rangeTo = new Date(Date.now());
      this.rangeFrom = new Date(Date.now());
      if (this.selectedDateRange == '7days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 7);
      } else if (this.selectedDateRange == '15days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 15);
      } else if (this.selectedDateRange == '30days') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 30);
      } else if (this.selectedDateRange == 'today') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 0);
      } else if (this.selectedDateRange == 'yesterday') {
        this.rangeFrom.setDate(this.rangeFrom.getDate() - 1);
      }
      this.rangeTo = this.datePipe.transform(this.rangeTo, 'yyyy-MM-dd');
      this.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd');
    }
    this.statusSummaryParams.rangeFrom = this.rangeFrom;
    this.statusSummaryParams.rangeTo = this.rangeTo;
    this.accountingDataService.distinctStatuses(this.statusSummaryParams).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        this.statusSummaryObject = res;
        // this.statusSummary = this.statusSummaryObject.summary;
        if(this.statusSummaryObject.summary.length > 0){
          
            for (let y = 0; y < this.statusSummaryObject.summary.length; y++) {
              for (let l = 0; l < this.allLedgers.length; l++) {
                if(this.allLedgers[l].name === this.statusSummaryObject.summary[y].ledgerName){
                  this.statusSummaryObject.summary[y]['ledgerCurrency'] = this.allLedgers[l].currency;
                } else {
                  this.statusSummaryObject.summary[y]['ledgerCurrency'] = "";
                }
              }
            }
          for (let x = 0; x < this.statusSummaryObject.summary.length; x++) {
            if (this.statusSummaryObject.summary[x].count == '0') {
              this.statusSummaryObject.summary[x]['isSummary'] = false;
              this.statusSummaryObject.summary[x]['aColor'] = "";
            } else {
              this.statusSummaryObject.summary[x]['isSummary'] = true;
              this.statusSummaryObject.summary[x]['aColor'] = "";
            }

            if(this.statusSummaryObject.summary[x].status == 'accounted' || this.statusSummaryObject.summary[x].status == 'accounted, not reconciled' || this.statusSummaryObject.summary[x].status == 'accounted, reconciled'){
              this.statusSummaryObject.summary[x]['aColor'] = "green";
              this.accountedSummary.push(this.statusSummaryObject.summary[x]);
            } else if(this.statusSummaryObject.summary[x].status == 'un accounted' || this.statusSummaryObject.summary[x].status == 'un accounted, not reconciled' || this.statusSummaryObject.summary[x].status == 'un accounted, reconciled'){
              this.statusSummaryObject.summary[x]['aColor'] = "red";
              this.unAccountedSummary.push(this.statusSummaryObject.summary[x]);
            } else {
              this.statusSummaryObject.summary[x]['aColor'] = "blue";
              this.accountedSummary.push(this.statusSummaryObject.summary[x]);
            }

          }
          if(this.mainStatus == 'accounted'){
            this.statusSummary = this.accountedSummary;
          } else {
            this.statusSummary = this.unAccountedSummary;
          }
          this.getColumnHeaders(this.ruleGroupId, this.dataViewId);
          this.getAccountedColumnHeaders();
          this.showAcctAnalytics = true;
          
          this.getSideBarGrouping('');
        } else {
          this.notificationService.info('Info!','No Records Found within Date Range');
        }
      
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res)
        this.notificationService.error('Error!', 'Error Occured while fetching status wise summary!');
      }
    );
  }

  /**********F5*************/
  /* Showing Header Action Buttons in Status Summary Functionality */
  showMoreOptions(event,index) {
    if (index == this.selectedTab) {
      let ind: number = index + 1;
        $(".reconcilewq-main .status-list .mat-tab-header .mat-tab-label-container .mat-tab-labels > div:nth-child(" + ind + ")").addClass('show-more-options')
    }
  }

  hideMoreOptions(event,index) {
    if (index == this.selectedTab) {
      let ind: number = index + 1;
        $(".reconcilewq-main .status-list .mat-tab-header .mat-tab-label-container .mat-tab-labels > div:nth-child(" + ind + ")").removeClass('show-more-options')
    }
  }

  /**********F6*************/
  /* Fetching Grouping Summary Functionality */
  ledgerRef:any;
  getSideBarGrouping(e) {
    this.buttonRestrict = true;
    this.fetchQualifierColumnsByView(this.dataViewId);
    this.leftPane = "-500px";
    this.selectedKeys = [];
    $(".reconcilewq-main .status-list .mat-tab-header .mat-tab-label-container .mat-tab-labels > div").removeClass('show-more-options');
    if(this.commonService.acctDashBoardObject.status){
      for (let i = 0; i < this.statusSummary.length; i++) {
        if(this.commonService.acctDashBoardObject.status == this.statusSummary[i].status) {
          this.selectedTab = i; 
        }
      }
    } else {
      if (!e || !e.index) {
        this.selectedTab = 0;
      } else {
        this.selectedTab = e.index;
      }
    }
    this.status = this.statusSummary[this.selectedTab].status;
    this.ledgerRef = this.statusSummary[this.selectedTab].ledgerName;
    this.groupingSummaryParams.groupId = this.ruleGroupId;
    this.groupingSummaryParams.periodFactor = this.period;
    this.groupingSummaryParams.viewId = this.dataViewId;
    this.groupingSummaryParams.status = this.status;
    this.groupingSummaryParams.rangeFrom = this.rangeFrom;
    this.groupingSummaryParams.rangeTo = this.rangeTo;
    this.groupingSummaryParams.filters = [];
    if(this.status == 'accounted' || this.status == 'accounted, not reconciled' || this.status == 'accounted, reconciled'){
      this.groupingSummaryParams.filters.push({
        key : 'ledger_name',
        values : [this.ledgerRef],
        dataType : 'STRING'
      })
    }
    this.groupingSummaryParams.columnSearch = [];
    for (let i = 0; i < this.dataHeaderColumns.length; i++) {
      this.dataHeaderColumns[i].searchWord = "";
    }
    this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).subscribe(
      (res: any) => {
        this.commonService.acctDashBoardObject = new AcctDashBoardObject();
        this.buttonRestrict = false;
        this.leftPane = "0px"
        this.groupingSummary = res;
        if(this.groupingSummary.length > 0){
          for (let z = 0; z < this.groupingSummary.length; z++) {
            const m = this.groupingSummary[z];
            m['isChecked'] = false;
          }
          this.getColumnHeaders(this.ruleGroupId, this.dataViewId);
          if(this.status == 'un accounted' || this.status == 'un accounted, not reconciled' || this.status == 'un accounted, reconciled'){
            this.getGroupByAcctRecords();
          } else {
            this.getAccountedSummary();
          }
          
        } else {
          this.notificationService.info('Info!','No Filters Found for selected process')
        }
        
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res)
        this.notificationService.error('Error!', 'Error Occured while fetching filters for the selected process !');
      }
    );
  }

  /**********F71*************/
  /* Function to Fetch Accounted Summary Records */
  accountedSummaryData:any[]=[];
  isAccountedSummary:boolean = false;
  getAccountedSummary(){
    this.isAccountedSummary = false;
    this.buttonRestrict = true;
    this.groupingSummaryParams.groupId = this.ruleGroupId;
    this.groupingSummaryParams.periodFactor = this.period;
    this.groupingSummaryParams.viewId = this.dataViewId;
    this.groupingSummaryParams.status = this.status;
    this.groupingSummaryParams.rangeFrom = this.rangeFrom;
    this.groupingSummaryParams.rangeTo = this.rangeTo;
    this.accountingDataService.accountedSummaryInfo(this.groupingSummaryParams).subscribe(
      (res: any) => {
        this.dataViewLines = [];
        this.buttonRestrict = false;
        this.accountedSummaryData = res;
        if(this.groupingSummary.length > 0){
          this.isAcctProcess = true;
        } else {
          this.notificationService.info('Info!','No Records Found for selected Status')
        }
        
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res)
        this.notificationService.error('Error!', 'Error Occured while fetching Accounted Summary for the selected status !');
      }
    );
  }

  filterValues1:groupingFilterParams[]=[];
  getAccountedDetails(){
    let len:number = 0;
    this.buttonRestrict = true;
    if(this.changeSort == false){
      this.isAcctProcess = false;
    }
    this.filterValues1 = [];
    let filterKeys:any[]=[];
    this.selectedDataLinesToAccount = [];
    if(this.selectedSummary.length > 0) {
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
          dataType: 'STRING',
          values : [this.ledgerRef]
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
            }
          }
        }
      }
      // console.log('fltrers : '+JSON.stringify(this.filterValues1));
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
          this.onError(res.json()
          )
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    } else {
      this.filterValues1 = [
        {
          key: 'ledger_name',
          dataType: 'STRING',
          values : [this.ledgerRef]
        }
      ];
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
          this.onError(res.json()
          )
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    }
  }
  /**********F7*************/
  /* Function to Fetch Records of a data view based on grouping */
  pushedGroupingValues: any[] = [];
  getGroupByAcctRecords() {
    this.buttonRestrict = true;
    if(this.changeSort == false){
      this.isAcctProcess = false;
    }
    this.selectedDataLinesToAccount = [];
    this.pushedGroupingValues = [];
    let values: any[] = [];
    let keys: any[] = [];
    let filters: any[] = [];
    let arr: any[] = [];
    let len: number = 0;
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
      let pushLen: number = 0;
      this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).subscribe(
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
          this.onError(res)
          this.notificationService.error('Error!', 'Error Occured while fetching Reconciliation Rule Groups!');
        }
      );
    } else {
      this.groupingSummaryParams.filters = [];
      this.accountingDataService.groupingSummarySidebar(this.groupingSummaryParams).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.groupingSummary = res;
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
          this.notificationService.error('Error!', 'Error Occured while fetching Reconciliation Rule Groups!');
        }
      );
    }
    this.groupingSummaryParams.pageNumber = this.page;
    this.groupingSummaryParams.pageSize = this.itemsPerPage;
    this.groupingSummaryParams.sortOrderBy = this.sortOrder;
    this.groupingSummaryParams.sortByColumnName = this.amountQualifier;
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
    if(this.status == 'un accounted' || this.status == 'un accounted, not reconciled' || this.status == 'un accounted, reconciled'){
    this.accountingDataService.detailedInfoRecords(this.groupingSummaryParams).subscribe(
        (res: any) => {
          this.isAccountedSummary = false;
          this.buttonRestrict = false;
          this.dataViewLines = res;
          this.groupingSummaryParams.columnSearch = [];
          this.objectForUnRecon = jQuery.extend(true, {}, this.groupingSummaryParams);
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
          this.onError(res.json()
          )
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    } else {
      this.getAccountedSummary();
    }
  }

  duplicateKeys:any[]=[];
  checkAll(event,ind){  
    if(event == true){
      this.duplicateKeys = [];
      for (let i = 0; i < this.groupingSummary[ind].summary.length; i++) {
          this.duplicateKeys.push(this.groupingSummary[ind].summary[i]); 
      }
      for (let k = 0; k < this.duplicateKeys.length; k++) {
        this.selectedKeys.push(this.duplicateKeys[k]);
      }
      // this.selectedKeys = this.duplicateKeys;
      // console.log('selected keys: '+JSON.stringify(this.selectedKeys));
      this.getGroupByAcctRecords();
    } else {
      this.selectedKeys.splice(ind,1);
      this.getGroupByAcctRecords();
    }
  }

  unCheckAll(ind){
    this.groupingSummary[ind]['isChecked'] = false;
  }

  /**********F8*************/
  /* Server Side Change Sort Functionality */
  sortOrder: string = 'desc';
  changeSort:boolean = false;
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

  /**********F9*************/
  /***Fetching Recon Rule Groups by Data View***/
  fetchReconRuleGroups(viewId, purpose) {
    this.ruleGroupService.fetchingGroupsByViewId(viewId, purpose).subscribe(
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
        this.onError(res)
        this.notificationService.error('Warning!', 'Error Occured while fetching Reconciliation Rule Groups!');
      }
    );
  }

  /**********F10*************/
  /***Fetching Lookups for Source and Category***/
  lineTypeLookups:any[]=[];
  fetchSourceLookups(type) {
    this.lookUpCodeService.fetchLookUpsByLookUpType(type).subscribe(
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
        this.onError(res)
      }
    );
  }

  /**********F11*************/
  /***Fetch View Columns by Data Type***/
  getColumnsByType(viewId, type) {
    this.dataViewsService.fetchDvColumnsByType(viewId, type).subscribe(
      (res: any) => {
        this.dvColumnsByType = res;
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  /**********F12*************/
  /***Fetch Chart of Accounts List by Tenant Id***/
  getChartOfAccounts() {
    this.chartOfAccountService.getChartOfAccountsByTenant().subscribe(
      (res: any) => {
        this.chartOfAccountsList = res;
        if (this.chartOfAccountsList.length > 0) {
          this.chartOfAccountName = this.chartOfAccountsList[0].name;
        } else {
          this.notificationService.info('Info!', 'No Chart of Accounts Found for this Organization');
        }
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  /**********F13*************/
  /***Fetch Ledger Definitions by COA***/
  allLedgers:any[]=[];
  getAllLedgers(){
    this.ledgerDefinitionService.getLedgersByTenant().subscribe(
      (res: any) => {
        this.allLedgers = res;
      },
      (res: Response) => {
        this.notificationService.error('Warning!', 'Error while fetching ledger definitions');
      }
    );
  }
  getLedgerDefinitions(coa) {
    this.ledgerDefinitionService.getLedgersByTenantAndCoa(coa).subscribe(
      (res: any) => {
        this.listOfLedgers = res;
        if (this.listOfLedgers.length > 0) {
          this.ledgername = this.listOfLedgers[0].name;
        } else {
          this.notificationService.info('Info!', 'No Ledger Found for this COA');
        }
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  accountedCurrency:string="";
  getAccountedCurrency(id){
    for (let i = 0; i < this.listOfLedgers.length; i++) {
      if(id == this.listOfLedgers[i].id){
        this.accountedCurrency = this.listOfLedgers[i].currency;
      }    
    }
  }

  autocompleListFormatter = (data: any) => {
    let html = `<span >${data.sourceValue}  </span>`;
    return this._sanitizer.bypassSecurityTrustHtml(html);
  }

  autocompleListFormatter1 = (data: any) => {
    let html = `<span >${data.targetValue}  </span>`;
    return this._sanitizer.bypassSecurityTrustHtml(html);
  }

  /**********F14*************/
  /***Fetch Segments List by COA***/
  segmentsInner:any[]=[];
  getSegmentsList(coa) {
    this.segmentService.fetchSegmentsByCOA(coa).subscribe(
      (res: any) => {
        this.segmentsList = res;
        this.segmentsInner = this.segmentsList.segments;
        if (this.segmentsList) {
          this.debitPlaceHolder = this.segmentsList.segmentPlaceholder;
          this.debitMask = this.segmentsList.segmentMask;
          if (!this.debits || this.debits.length < 1) {
            this.debits.push(new debits());
            this.debits[0].amountColId = 0;
            this.debits[0].debitLine = "";
            this.debits[0].lineTypeDetial = "";
            this.debits[0].amountColName = "";
            this.debits[0].debitSegValues = [];
            this.debits[0].debitSegs = [];
            // this.debits[0].debitSegs[0].targetValue = "";
            // this.debits[0].debitSegs[0].sourceValue = "";
            // this.debits[0].debitSegValues[0].targetValue = "";
            // this.debits[0].debitSegValues[0].sourceValue = "";
          }
          if (!this.credits || this.credits.length < 1) {
            this.credits.push(new credits());
            this.credits[0].amountColId = 0;
            this.credits[0].creditLine = "";
            this.credits[0].lineTypeDetial = "";
            this.credits[0].amountColName = "";
            this.credits[0].creditSegValues = [];
            this.credits[0].creditSegs = [];
            // this.credits[0].creditSegs[0].targetValue = "";
            // this.credits[0].creditSegs[0].sourceValue = "";
            // this.credits[0].creditSegValues[0].targetValue = "";
            // this.credits[0].creditSegValues[0].sourceValue = "";
          }
          // console.log('credit liength' + this.credits.length + ' debit length' + this.debits.length);
        }
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  /**********F15*************/
  /***Fetch view Columns By Qualifier***/
  amountQualifier: any;
  dateQualifier: any;
  currencyQualifierName:any;
  fetchQualifierColumnsByView(viewId) {
    this.reconcileService.getQualifierColumns(viewId).subscribe(
      (res: any) => {
        this.qualifierColumns = res;
        for (let i = 0; i < this.qualifierColumns.length; i++) {
          if (this.qualifierColumns[i].qualifier == 'AMOUNT') {
            this.amountQualifier = this.qualifierColumns[i].columnName;
          } else if (this.qualifierColumns[i].qualifier == 'CURRENCYCODE') {
            this.currencyQualifier = this.qualifierColumns[i].columnId;
            this.currencyQualifierName = this.qualifierColumns[i].columnDisplayName;
          } else if (this.qualifierColumns[i].qualifier == 'TRANSDATE') {
            this.dateQualifier = this.qualifierColumns[i].columnId;
          }
        }
      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Error!', 'Error While Fetching Qualifier View Columns');
      }
    );
  }

  /********************* Manual Processes  Starts*****************/
  /**********F16*************/
  /***Manual Accounting Functionality***/
  /***Open Manual Account Dialog****/
  openAccountDialog() {
    let UAccLen: number = 0;
    if (this.selectedDataLinesToAccount.length > 0) {
      for (var i = 0; i < this.selectedDataLinesToAccount.length; i++) {
        if (this.selectedDataLinesToAccount[i].Status.includes('UN ACCOUNTED')) {
          UAccLen = UAccLen + 1;
        }
      }
      if (this.selectedDataLinesToAccount.length == UAccLen) {
        this.ledgername = null;
        this.acctCategory = "";
        this.acctSource = "";
        this.chartOfAccount = null;
        this.debits = [];
        this.credits = [];
        this.accountModalTog = true;
      } else {
        this.accountModalTog = false;
        this.notificationService.info('Info!', 'Please select only Un-Accounted records');
      }
    } else {
      this.accountModalTog = false;
      this.notificationService.info('Info!', 'No Records Selected');
    }
  }

  onChangeValueName(type, event, ind, dInd, val) {
    let debitCode: string = "";
    let creditCode: string = "";
    if (type == 'DEBIT') {
      for (let i = 0; i < val.length; i++) {
        const elem = val[i];
        if (elem.id == event.value.id) {
          if (this.debits[dInd].debitSegValues.length > 0) {
            let editing: boolean = false;
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
            let editing: boolean = false;
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

  onChangeValueId(type,event,ind,dInd,val){
    if(isNaN(val)){
      val = val.toString();
    }
    let debitCode:any[] =[];
    let newDebit:string = "";
    let creditCode:any[] =[];
    let newCredit:string = "";
    if(type == 'DEBIT'){
      debitCode.push(val);
      for (let i = 0; i < debitCode.length; i++) {
        if(i == debitCode.length -1){
          newDebit = newDebit + debitCode[i];
        } else {
          newDebit = newDebit + debitCode[i] + "-"
        }
      }
      this.debits[dInd].debitLine = newDebit;
    } else {
      creditCode.push(val);
      for (let i = 0; i < creditCode.length; i++) {
        if(i == creditCode.length -1){
          newCredit = newCredit + creditCode[i];
        } else {
          newCredit = newCredit + creditCode[i] + "-"
        }
      }
      this.credits[dInd].creditLine = newCredit;
    }
    // console.log('on change vent : '+ JSON.stringify(event));
      for (let i = 0; i < this.segmentsInner[ind]['valueSet'].length; i++) {
        const element = this.segmentsInner[ind]['valueSet'][i];
        if(event.sourceValue == element.sourceValue){
          if(type == 'DEBIT'){
            this.debits[dInd].debitSegs[ind] = element.targetValue;
          } else {
            this.credits[dInd].creditSegs[ind] = element.targetValue;
          }
          
          if(type == 'DEBIT'){
            if(this.debits[dInd].debitSegs && this.debits[dInd].debitSegs.length>0)
            {
              this.debits[dInd].debitSegs[ind].targetValue = element.targetValue;
            }
            else{
              this.debits[dInd].debitSegs[ind]['targetValue'] = element.targetValue;
            }
          } else {
            if(this.credits[dInd].creditSegs && this.credits[dInd].creditSegs.length>0)
            {
              this.credits[dInd].creditSegs[ind].targetValue = element.targetValue;
            }
            else{
              this.credits[dInd].creditSegs[ind]['targetValue'] = element.targetValue;
            }
            this.credits[dInd].creditSegs[ind].targetValue = element.targetValue;
          }
        } 
      }
  }

  // this method check pressed key is numeric or not
  valueCodeRestrict(code, len, val) {
    // console.log('abcd value ' + val);
    let valLen: number = 0;
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
    // console.log('on blur vent : '+ event);
    event.returnValue = false;
  }

  /*** Add A new Debit Line ***/
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

  /*** Add A new Credit Line ***/
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

  /*** Remove A new Debit Line ***/
  removeDebitLine(i: number) {
    if (this.debits.length > 1) {
      this.debits.splice(i, 1);
    }
  }

  /*** Remove A new Credit Line ***/
  removeCreditLine(i: number) {
    if (this.credits.length > 1) {
      this.credits.splice(i, 1);
    }
  }

  /*** Restrict Number Input ***/
  restrictNumber(code) {
    if (code > 31 && (code < 48 || code > 57)) {
      return false;
    }
    return true;
  }


  /************* F17 *******************/
  /*** Edit Accounting Functionality ***/
  openEditAccounting() {
    this.debits = [];
    this.credits = [];
    if (this.selectedDataLinesToAccount.length == 1) {
      if (this.selectedDataLinesToAccount[0].Status == 'ACCOUNTED' || this.selectedDataLinesToAccount[0].Status == 'INPROCESS') {
        this.accountModalTog = true;
        let currencyQualifiers: any = {};
        // console.log('selected record' + JSON.stringify(this.selectedDataLinesToAccount));
        this.acctCategory = this.selectedDataLinesToAccount[0].Category;
        this.acctSource = this.selectedDataLinesToAccount[0].Source;
        this.chartOfAccount = this.selectedDataLinesToAccount[0].coa_ref;
        this.segmentService.fetchSegmentsByCOA(this.chartOfAccount).subscribe(
          (res: any) => {
            this.segmentsList = res;
            this.debitPlaceHolder = this.segmentsList.segmentPlaceholder;
            this.debitMask = this.segmentsList.segmentMask;
            this.ledgerDefinitionService.getLedgersByTenantAndCoa(this.chartOfAccount).subscribe(
              (res: any) => {
                this.listOfLedgers = res;
                // console.log('ledgers:' + JSON.stringify(res));
                this.ledgername = this.selectedDataLinesToAccount[0].ledger_ref;
                // console.log('ledger Id :' + this.ledgername);
                for (let i = 0; i < this.selectedDataLinesToAccount[0].children.length; i++) {
                  const item = this.selectedDataLinesToAccount[0].children[i];
                  if (this.selectedDataLinesToAccount[0].children.length > 1) {
                    if (item.line_type == 'DEBIT') {
                      this.debits.push({
                        debitLine: item['Debit Account'],
                        amountColId: 0
                      })
                    } else if (item.line_type == 'CREDIT') {
                      this.credits.push({
                        creditLine: item['Credit Account'],
                        amountColId: 0
                      })
                    } else {
                      this.credits.push({
                        creditLine: "",
                        amountColId: 0
                      })
                    }
                  } else {
                    if (item.line_type == 'DEBIT') {
                      this.debits.push({
                        debitLine: item['Debit Account'],
                        amountColId: 0
                      })
                    } else {
                      this.debits.push({
                        debitLine: "",
                        amountColId: 0
                      })
                    }

                    if (item.line_type == 'CREDIT') {
                      this.credits.push({
                        creditLine: item['Credit Account'],
                        amountColId: 0
                      })
                    } else {
                      this.credits.push({
                        creditLine: "",
                        amountColId: 0
                      })
                    }
                  }
                }
              },
              (res: Response) => {
                this.onError(res)
              }
            );
          },
          (res: Response) => {
            this.onError(res)
          }
        );
      } else {
        this.accountModalTog = false;
        this.notificationService.info('Info!', 'Please select only one Accounted or Partial Accounted Record');
      }
    } else {
      this.accountModalTog = false;
      this.notificationService.info('Info!', 'Please select only One Accounted or Partial Accounted Record');
    }
  }

  /************* F18 *******************/
  /***Manual Accounting Post Functionality ***/
  manualAccounting(groupId, viewId) {
    this.buttonRestrict = true;
    this.recordsForAccounting = {};
    this.rowForAccounting = [];
    if (this.selectedDataLinesToAccount.length > 0) {
      this.recordsForAccounting.groupId = groupId;
      this.recordsForAccounting.viewId = viewId;
      for (let i = 0; i < this.selectedDataLinesToAccount.length; i++) {
        const item = this.selectedDataLinesToAccount[i];
        if (this.isMultiCurrency == true) {
          this.rowForAccounting.push({
            coaRef: this.chartOfAccount,
            currencyRef: item[this.currencyQualifierName],
            categoryRef: this.acctCategory,
            ledgerRef: this.ledgername,
            sourceRef: this.acctSource,
            rowId: item.id,
            debits: this.debits,
            credits: this.credits,
            accountedCurrency: this.accountedCurrency,
            fxRateId: this.fxRate,
            conversionDate: this.conversionDate
          });
        } else {
          this.rowForAccounting.push({
            coaRef: this.chartOfAccount,
            currencyRef: item[this.currencyQualifierName],
            categoryRef: this.acctCategory,
            ledgerRef: this.ledgername,
            sourceRef: this.acctSource,
            rowId: item.id,
            debits: this.debits,
            credits: this.credits
          });
        }
      }
      this.recordsForAccounting.rows = this.rowForAccounting;
      this.accountingDataService.manualAcct(this.recordsForAccounting).subscribe(
        (res: any) => {
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.buttonRestrict = false;
          this.accountModalTog = false;
          this.codeCombinationClear();
          this.notificationService.success('Info!', 'Selected Records Accounted Successfully');
          this.rowForAccounting = [];
          this.getStatusLevelSummary();
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
        }
      );
    }
  }
  /**************** F19 *******************/
  /***Manual Un Accounting Functionality***/
  openUnAccountDialog(type,purpose) {
    this.groupingSummaryParams.type = type;
    this.groupingSummaryParams.accountingType = purpose;
    var dialogRef = this.dialog.open(AcctConfirmModal, {
      width: '400px',
      data: { context: 'Un-Account', ok: 'ok', reason: '', cancel: 'cancel' }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result && result == 'ok') {
        this.buttonRestrict = true;
        let accLen: number = 0;
        if (this.selectedDataLinesToAccount.length > 0) {
          for (var i = 0; i < this.selectedDataLinesToAccount.length; i++) {
            if (this.selectedDataLinesToAccount[i].Status.includes('ACCOUNTED')) {
              accLen = accLen + 1;
            }
          }
        }
        // console.log('selected record' + JSON.stringify(this.selectedDataLinesToAccount));
        if (type == 'batchwise') {
          this.accountingDataService.manualUnAcctNew(this.groupingSummaryParams).subscribe(
            (res: any) => {
              this.accountedCount = 0;
              this.unAccountedCount = 0;
              this.recChangeColor = 'mat-raised-button';
              this.unRecChangeColor = 'mat-raised-button';
              this.buttonRestrict = false;
              if(purpose == 'reverse'){
                this.notificationService.success('Success!', 'Reverse Entries Accounted Successfully!');
              } else {
                this.notificationService.success('Success!', 'Selected Accounted Transactions Deleted Successfully!');
              }
              this.accountedCount = 0;
              this.unAccountedCount = 0;
              this.getStatusLevelSummary();
            },
            (res: Response) => {
              this.buttonRestrict = false;
              this.onError(res)
            }
          );
        } else {
          if (this.selectedDataLinesToAccount.length == accLen) {
            let acIds: any[] = [];
            if (this.selectedDataLinesToAccount.length > 0) {
              this.selectedDataLinesToAccount.forEach(item => {
                acIds.push(item.id);
              });
            }
            this.groupingSummaryParams.originalRowIds = acIds;
            this.accountingDataService.manualUnAcctNew(this.groupingSummaryParams).subscribe(
              (res: any) => {
                this.accountedCount = 0;
                this.unAccountedCount = 0;
                this.recChangeColor = 'mat-raised-button';
                this.unRecChangeColor = 'mat-raised-button';
                this.buttonRestrict = false;
                if(purpose == 'reverse'){
                  this.notificationService.success('Success!', 'Reverse Entries Accounted Successfully!');
                } else {
                  this.notificationService.success('Success!', 'Selected Accounted Transactions Deleted Successfully!');
                }
                this.getStatusLevelSummary();
              },
              (res: Response) => {
                this.buttonRestrict = false;
                this.onError(res)
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

  /**************** F20 *******************/
  /*** Code Combinations Clear Functionality***/
  codeCombinationClear() {
    this.ledgername = "";
    this.debits = [];
    this.credits = [];
    this.chartOfAccount = "";
    this.acctCategory = "";
    this.acctSource = "";
    this.debits.push(new debits());
    this.debits[0].amountColId = 0;
    this.debits[0].debitLine = "";
    this.debits[0].lineTypeDetial = "";
    this.debits[0].amountColName = "";
    this.credits.push(new debits());
    this.credits[0].amountColName = "";
    this.credits[0].amountColId = 0;
    this.credits[0].creditLine = "";
    this.credits[0].lineTypeDetial = "";
  }


  /**************** F18 *******************/
  /****show counts in Header Actions ******/
  // showBatchWiseCounts() {
  //   this.accountedCount = 0;
  //   this.unAccountedCount = 0;
  //   this.recChangeColor = 'mat-raised-button';
  //   this.unRecChangeColor = 'mat-raised-button';
  //   if (this.filteredGroupByAnalytics.length > 0) {
  //     if (this.selectedKeys.length > 0) {
  //       this.selectedKeys.forEach(item => {
  //         this.filteredGroupByAnalytics.forEach(k => {
  //           if (item.name == k.name) {
  //             this.accountedCount = this.accountedCount + k.acount;
  //             this.unAccountedCount = this.unAccountedCount + k.ucount;
  //             if (this.unAccountedCount > 0) {
  //               this.recChangeColor = 'mat-raised-button rec-bg-color';
  //             }
  //             if (this.accountedCount > 0) {
  //               this.unRecChangeColor = 'mat-raised-button rec-bg-color';
  //             }
  //           }
  //         })
  //       });
  //     } else if (this.selectedAKeys.length > 0) {
  //       this.selectedAKeys.forEach(item => {
  //         this.filteredGroupByAnalytics.forEach(k => {
  //           if (item.name == k.name) {
  //             this.accountedCount = this.accountedCount + k.acount;
  //             if (this.accountedCount > 0) {
  //               this.unRecChangeColor = 'mat-raised-button rec-bg-color';
  //             }
  //           }
  //         })
  //       });
  //     } else if (this.selectedUKeys.length > 0) {
  //       this.selectedUKeys.forEach(item => {
  //         this.filteredGroupByAnalytics.forEach(k => {
  //           if (item.name == k.name) {
  //             this.unAccountedCount = this.unAccountedCount + k.ucount;
  //             if (this.unAccountedCount > 0) {
  //               this.recChangeColor = 'mat-raised-button rec-bg-color';
  //             }
  //           }
  //         })
  //       });
  //     } else if (this.selectedPKeys.length > 0) {
  //       this.selectedPKeys.forEach(item => {
  //         this.filteredGroupByAnalytics.forEach(k => {
  //           if (item.name == k.name) {
  //             this.accountedCount = this.accountedCount + k.pcount;
  //             if (this.accountedCount > 0) {
  //               this.unRecChangeColor = 'mat-raised-button rec-bg-color';
  //             }
  //           }
  //         })
  //       });
  //     } else {
  //       this.accountedCount = 0;
  //       this.unAccountedCount = 0;
  //     }

  //   }
  // }

  /*FUNCTION21 - Function to get Column headers of a data view STARTS */
  /* Author: BHAGATH */

  getColumnHeaders(groupId, viewId) {
    this.accountingDataService.fetchingColHeaders(groupId, viewId, this.status).subscribe(
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
        this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      })
  }

   /*FUNCTION30 - Function to get Column headers of a data view STARTS */
  /* Author: BHAGATH */
  selectedSummary:any[]=[];
  accountedHeaders:any[]=[];
  accountedColumnOptions:any[]=[];
  getAccountedColumnHeaders() {
    this.accountingDataService.accountedColumnHeaders(this.status).subscribe(
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
        this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      })
  }


   /*FUNCTION22 - File Export */
  /* Author: BHAGATH */
  acctDataFileExp() {
    this.buttonRestrict = true;
    let reportURL: string = "http://192.168.0.44";
    let exportFilterGroup: string = "";
    if (this.filteredGroupBy == 'rules' || this.filteredGroupBy == 'days' || this.filteredGroupBy == 'batch') {
      exportFilterGroup = this.filteredGroupBy
    } else {
      exportFilterGroup = 'columnName';
    }
    this.accountingDataService.acctDataExcelExport(exportFilterGroup, this.objectForUnRecon).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        reportURL = reportURL + res['excelFilePath'];
        // console.log('reportURL' + reportURL);
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


  toggleSideBar() {
    if (!this.isVisibleB) {
      this.isVisibleB = true;
    } else {
      this.isVisibleB = false;
    }
  }

  /*FUNCTION23 - pagination functionality */
  /* Author: BHAGATH */
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

  /* FUNCTION24 : Function to call OnDemand Recon Job and Auto Refresh Functionality */
  /* AUTHOR : BHAGATH */
  oozieTest: any;
  // testOozieDB() {
  //   this.jobDetailsService.oozieDBTest().subscribe((res: any) => {
  //     this.oozieTest = res.ooziestatus;
  //   }, (res: Response) => {
  //     this.onError(res.json())
  //     this.notificationService.error('Error!', 'Error while checking Oozie status!');
  //   }
  //   )

  // }

  initiateAcctJob(purpose) {
    let fromDate: any = "";
    let toDate: any = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    // console.log('range form :' + this.rangeFrom + ' + ' + 'Range To: ' + this.rangeTo);
    this.dateRange = [fromDate, toDate];
    if (purpose == 'ACCOUNTING') {
      this.paramSet = {
        'param1': this.ruleGroupId,
        'param3': this.dataViewId,
        'param6': this.dateRange
      }
    } else {
      this.paramSet = {
        'param1': this.reconRuleGroupId,
        'param3': this.dataViewId,
        'param6': this.dateRange
      }
    }
    this.jobDetailsService.oozieDBTest().subscribe((res: any) => {
      this.oozieTest = res.ooziestatus;
      if (this.oozieTest == true) {
        this.initiatingJob(purpose, this.paramSet);
      } else {
        this.notificationService.error('Warning!','Job Engine Start Up Failure...Please try again later');
      }
    }, (res: Response) => {
      this.onError(res.json())
      this.notificationService.error('Error!', 'Error while checking Oozie status!');
    }
    )
  }

  beforeJobInitiate() {
    this.displayConfirmDialog = true;
  }

  buildRule() {
    this.reconcileService.ENABLE_RULE_BLOCK = true;
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
    let colValues: string = "";
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
    ]
    this.acctSchedObj.parameters = this.acctSchedParamms;
    this.acctSchedObj.jobName = this.ruleGroupName;
    this.acctSchedObj.programName = 'ACCOUNTING';
    this.displayScheduling = true;
  }



  initiatingJob(purpose, set) {
    this.buttonRestrict = true;
    // console.log('param set: ' + JSON.stringify(set));
    if (purpose == 'ACCOUNTING') {
      this.jobDetailsService.initiateAcctRecJob(purpose, set).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.acctJobOutput = res;
        this.acctJobId = this.acctJobOutput.status;
        this.runAcctShow = false;
        this.displayConfirmDialog = false;
        // console.log('acct job ref:' + JSON.stringify(this.acctJobId));
        if (this.acctJobId != 'Failed to intiate job') {
          this.notificationService.success('Info!', 'Job Initiated Successfully!');
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.setIntId = setInterval(() => {
            if (this.callSetInt == true) {
              this.jobDetailsService.oozieeJobStatus(this.acctJobId).subscribe((res: any) => {
                this.acctJobStatus = res;
                this.accountedCount = 0;
                this.unAccountedCount = 0;
                this.getStatusLevelSummary();
                this.recChangeColor = 'mat-raised-button';
                this.unRecChangeColor = 'mat-raised-button';
                // console.log('acct job status:' + JSON.stringify(this.acctJobStatus._body));
                if (this.acctJobStatus._body == 'SUCCEEDED' || this.acctJobStatus._body == 'KILLED' || this.acctJobStatus._body == 'FAILED') {
                  if (this.setIntId) {
                    this.getStatusLevelSummary();
                    clearInterval(this.setIntId);
                  }
                } else {
                  // console.log('calling load all:');
                  // this.loadAll();
                }
              },
                (res: Response) => {
                  this.onError(res.json())
                  this.notificationService.error('Error!', 'Error while checking job status!');
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
          this.onError(res.json())
          this.notificationService.error('Error!', 'Failed to Initiate Job!');
        }
      )

    } else {
      this.jobDetailsService.initiateAcctRecJob(purpose, set).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.reconJobOutput = res;
        this.runAcctShow = false;
        this.reconJobId = this.reconJobOutput.status;
        // console.log('recon job ref:' + JSON.stringify(this.reconJobId));
        if (this.reconJobId != 'Failed to intiate job') {
          this.notificationService.success('Info!', 'Job Initiated Successfully!');
        } else {
          this.notificationService.error('Error!', 'Failed to intiate job!');
        }
      },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res.json())
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    }
  }
  cols: any[] = [];
  sideBarHeight: any;
  acctTableHeight: any;
  ngOnInit() {
    this.getAllLedgers();
    this.fetchRuleGroups();
    this.getChartOfAccounts();
    this.fetchSourceLookups('SOURCE');
    this.fetchSourceLookups('CATEGORY');
    this.fetchSourceLookups('ACCOUNTING_LINE_TYPES');
    if(this.commonService.acctDashBoardObject){
      this.fetchQueryParams(this.commonService.acctDashBoardObject.ruleGroupId);

    }
    //this.jobDetailsService.getUserInfo();
    $(".split-example").css({
      'height': 'auto',
      'min-height': (this.commonService.screensize().height - 130) + 'px'
    });
    this.sideBarHeight = this.commonService.screensize().height - 185;
    this.acctTableHeight = this.commonService.screensize().height - 350;
    this.principal.identity().then((account) => {
      this.currentAccount = account;
    });
  }

  private onSuccess(data, headers) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    // this.page = pagingParams.page;
    this.accountingData = data;
  }
  private onError(error) {
    this.alertService.error(error.message, null, null);
  }

  ngOnDestroy() {
    if (this.setIntId) {
      clearInterval(this.setIntId);
    }
  }

}
