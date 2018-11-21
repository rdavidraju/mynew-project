import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription ,Observable} from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { RuleGroupService } from '../rule-group/rule-group.service';
import {
  AccountingData, AccountingDataBreadCrumbTitles, acctCustomFilters,
  acctFilterColumns, jsonforAccounting, selectedAcctRows, AcctDataQueries,
  acctKeyValues, groupByColumnValues, filteredParams, recordParams, credits, debits ,Car
} from './accounting-data.model';
import { AccountingDataService } from './accounting-data.service';
import { PageEvent } from '@angular/material';
import { CommonService } from '../common.service';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, DialogModule, InputMaskModule, TreeTableModule, TreeNode, SharedModule } from 'primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SessionStorageService } from 'ng2-webstorage';
import { DataViewsService } from '../data-views/data-views.service';
import { LookUpCodeService } from '../look-up-code/look-up-code.service'
import { RuleGroupAndRuleWithLineItem } from '../rule-group/ruleGroupAndRuleWithLineItem.model';
import { ReconcileService } from '../reconcile/reconcile.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { DatePipe } from '@angular/common';
import { ChartOfAccountService } from '../chart-of-account/chart-of-account.service';
import { LedgerDefinitionService } from '../ledger-definition/ledger-definition.service';
import { SegmentsService } from '../segments/segments.service';
import { SchedulingModel, Parameters } from '../scheduling/scheduling.component';
import { ProjectService } from '../project/project.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'pivottable/dist/pivot.min.js';
declare var $: any;
declare var jQuery: any;
// var cars:any[]   = require('./cars-medium.json');
@Component({
  selector: 'accounting-data',
  templateUrl: './accounting-data-wq.component.html'
})
export class AccountingDataWqComponent implements OnInit, OnDestroy {
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
  selectedCars2:any[]=[];
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
  status: any = "accounted";
  isAcctProcess: boolean = false;
  isAcctProcess2: boolean = false;
  dataViews: any = [];
  ruleGroups: any;
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
  qualifierColumns: any;
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
  sortOrderColId:any;
  groupingHeads:any[]=[];


  //Modified Variables**//
  period:any;
  selectedParentTab:any;
  selectedAmountTab:any;
  selectedChildTab:any;
  carss:any[]=[];
  carHeaders:any[]=[];



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

  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.carHeaders = [
      {
        "field" : "vin",
        "header" : "Vin",
        "tex-align": "left"
      },
      {
        "field" : "debit",
        "header" : "Debit",
        "tex-align": "left"
      },
      {
        "field" : "credit",
        "header" : "Credit",
        "tex-align": "left"
      },
      {
        "field" : "brand",
        "header" : "Brand",
        "tex-align": "left"
      },
      {
        "field" : "year",
        "header" : "Year",
        "tex-align": "left"
      },
      {
        "field" : "color",
        "header" : "Color",
        "tex-align": "left"
      },
    ]
    this.carss = [
      {
          "vin": "a1653d4d",
          "debit": "544-5464-46545",
          "credit": "",
          "brand": "VW",
          "year": 1998,
          "color": "White",
          "price": 10000,
          "childs": [
              {
                  "vin": "a1653d4d",
                  "debit": "1222-23-46545",
                  "credit": "",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "544-5464-46545",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "723-5464-5434",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              }
          ]
      },
      {
          "vin": "ddeb9b10",
          "brand": "Mercedes",
          "year": 1985,
          "color": "Green",
          "price": 25000,
          "childs": [
              {
                  "vin": "a1653d4d",
                  "debit": "1222-23-46545",
                  "credit": "",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "544-5464-46545",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "723-5464-5434",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              }
          ]
      },
      {
          "vin": "d8ebe413",
          "brand": "Jaguar",
          "year": 1979,
          "color": "Silver",
          "price": 30000,
          "childs": [
              {
                  "vin": "a1653d4d",
                  "debit": "1222-23-46545",
                  "credit": "",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "544-5464-46545",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "723-5464-5434",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              }
          ]
      },
      {
          "vin": "aab227b7",
          "brand": "Audi",
          "year": 1970,
          "color": "Black",
          "price": 12000,
          "childs": [
              {
                  "vin": "a1653d4d",
                  "debit": "1222-23-46545",
                  "credit": "",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "544-5464-46545",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              },
              {
                  "vin": "a1653d4d",
                  "debit": "",
                  "credit": "723-5464-5434",
                  "brand": "VW",
                  "year": 1998,
                  "color": "White"
                   
              }
          ]
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


  changeGroupBy(e){
    this.selectedChildTab = 0;
    if (!e || !e.index) {
    this.selectedParentTab = 0;
    } else {
      this.selectedParentTab = e.index;
    }
    if(this.selectedParentTab == 0){
      this.status = "reconciled";
      this.selectedAmountTab = 0;
    } else if(this.selectedParentTab == 1){
      this.status = "unreconciled";
      this.selectedAmountTab = 1;
    } else if(this.selectedParentTab == 2){
      this.status = "suggested"
    }
  }
  changeViewBy(e){
    if (!e || !e.index) {
    this.selectedChildTab = 0;
    } else {
      this.selectedChildTab = e.index;
    }
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
  /***Toggle Pie Charts Window***/
  toggleCharts() {
    if (!this.showCharts) {
      this.showCharts = true;
    } else {
      this.showCharts = false;
    }
  }

  /**********F3*************/
  /***Fetching Accounting Rule Groups***/
  fetchRuleGroups() {
    this.ruleGroupService.fetchRuleGroupsBypurpose('ACCOUNTING').subscribe(
      (res: any) => {
        this.ruleGroups = res;
      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Error!', 'Error Occured while fetching Reconciliation Rule Groups!');
      }
    );
  }

  /**********F4*************/
  /***Fetching query parameters on rule group selection***/
  fetchQueryParams(groupId) {
    this.ruleGroupId = groupId;
    this.accountingDataService.fetchAcctQueryParams(groupId).subscribe(
      (res: any) => {
        this.queryParams = res;
        this.ruleGroupName = this.queryParams.ruleGroupName;
        this.dataViews = this.queryParams.dataViews;
        this.dataViewId = this.dataViews[0].id;
        this.fetchColumnIdNameByViewIdGroupByTrue(this.dataViewId);
        this.dataViewName = this.dataViews[0].viewName;
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  /**********F5*************/
  /***Fetching data view Columns having group By True***/
  fetchColumnIdNameByViewIdGroupByTrue(viewId) {
    // console.log('index : ' + index);
    this.reconcileService.getColumnIdNameByViewId(viewId).subscribe(
      (res: any) => {
        this.viewColumns = res;
        if (this.viewColumns.length > 0) {
          this.viewColumnId = this.viewColumns[0].columnId;
        } else {
          //this.notificationService.info('Info!', 'No Columns Found with Group By True!');
        }
        // this.fetchQualifierColumnsByView('TRANSDATE', viewId);
        // this.fetchQualifierColumnsByView('CURRENCYCODE', viewId);
        this.getColumnsByType(this.dataViewId, 'DECIMAL');
      },
      (res: Response) => {
        this.onError(res.json()
        )
        //this.notificationService.error('Error!', 'Error while fetching Group By Columns!');
      }
    )

  }

  selectedDV(index){
    this.selectedTab = index;
  }

  /**********F6*************/
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

  /**********F7*************/
  /***Fetching Lookups for Source and Category***/
  fetchSourceLookups(type) {
    this.lookUpCodeService.fetchLookUpsByLookUpType(type).subscribe(
      (res: any) => {
        if (type == 'SOURCE') {
          this.sourceLookups = res;
        } else if (type == 'CATEGORY') {
          this.categoryLookups = res;
        }
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }

  /**********F8*************/
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

  /**********F9*************/
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

  /**********F10*************/
  /***Fetch Ledger Definitions by COA***/
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


  /**********F11*************/
  /***Fetch Segments List by COA***/
  getSegmentsList(coa) {
    this.segmentService.fetchSegmentsByCOA(coa).subscribe(
      (res: any) => {
        this.segmentsList = res;
        if (this.segmentsList) {
          this.debitPlaceHolder = this.segmentsList.segmentPlaceholder;
          this.debitMask = this.segmentsList.segmentMask;
          if (!this.debits || this.debits.length < 1) {
            this.debits.push(new debits());
            this.debits[0].amountColId = 0;
            this.debits[0].debitLine = "";
          }
          if (!this.credits || this.credits.length < 1) {
            this.credits.push(new credits());
            this.credits[0].amountColId = 0;
            this.credits[0].creditLine = "";
          }
          console.log('credit liength' + this.credits.length + ' debit length' + this.debits.length);
        }
      },
      (res: Response) => {
        this.onError(res)
      }
    );
  }


  /**********F12*************/
  /***Fetch view Columns By Qualifier***/
  // fetchQualifierColumnsByView(qualifier, viewId) {
  //   this.reconcileService.getQualifierColumns(qualifier, viewId).subscribe(
  //     (res: any) => {
  //       if (qualifier == 'CURRENCYCODE') {
  //         this.currencyQualifiers = res;
  //         if (this.currencyQualifiers) {
  //           this.currencyQualifier = this.currencyQualifiers.columnDisplayName;
  //         } else {
  //           this.notificationService.info('Info!', 'No Columns Found with ' + qualifier + ' Qualifier');
  //         }
  //       } else if (qualifier == 'TRANSDATE') {
  //         this.qualifierColumns = res;
  //         if (this.qualifierColumns) {
  //           this.qualifierColumnId = this.qualifierColumns.columnId;
  //         } else {
  //           this.notificationService.info('Info!', 'No Columns Found with ' + qualifier + ' Qualifier');
  //         }
  //       }
  //     },
  //     (res: Response) => {
  //       this.onError(res)
  //       this.notificationService.error('Error!', 'Error While Fetching Qualifier View Columns');
  //     }
  //   );
  // }


  /********************* Manual Processes  Starts*****************/


  /**********F12*************/
  /***Manual Accounting Functionality***/
  /***Open Manual Account Dialog****/
  openAccountDialog() {
    let UAccLen: number = 0;
    if (this.selectedData.length > 0) {
      for (var i = 0; i < this.selectedData.length; i++) {
        if (this.selectedData[i].data.Status == 'U') {
          UAccLen = UAccLen + 1;
        }
      }
      if (this.selectedData.length == UAccLen) {
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


  /*** Add A new Debit Line ***/
  addDebitRow() {
    this.debits.push({
      debitLine: ""
    });
  }

  /*** Add A new Credit Line ***/
  addCreditRow() {
    this.credits.push({
      creditLine: ""
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


  /************* F13 *******************/
  /*** Edit Accounting Functionality ***/
  openEditAccounting() {
    this.debits = [];
    this.credits = [];
    if (this.selectedData.length == 1) {
      if (this.selectedData[0].data.Status == 'A' || this.selectedData[0].data.Status == 'I') {
        this.accountModalTog = true;
        let currencyQualifiers: any = {};
        console.log('selected record' + JSON.stringify(this.selectedData));
        this.acctCategory = this.selectedData[0].data.Category;
        this.acctSource = this.selectedData[0].data.Source;
        this.chartOfAccount = this.selectedData[0].data.coaId;
        this.segmentService.fetchSegmentsByCOA(this.chartOfAccount).subscribe(
          (res: any) => {
            this.segmentsList = res;
            this.debitPlaceHolder = this.segmentsList.segmentPlaceholder;
            this.debitMask = this.segmentsList.segmentMask;
            this.ledgerDefinitionService.getLedgersByTenantAndCoa(this.chartOfAccount).subscribe(
              (res: any) => {
                this.listOfLedgers = res;
                console.log('ledgers:' + JSON.stringify(res));
                this.ledgername = + this.selectedData[0].data.ledgerId;
                console.log('ledger Id :' + this.ledgername);
                this.selectedData[0].children.forEach(item => {
                  if (this.selectedData[0].children.length > 1) {
                    if (item.data.lineType == 'DEBIT') {
                      this.debits.push({
                        debitLine: item.data.Debit_Account,
                        amountColId: item.data.columnId
                      })
                    } else if (item.data.lineType == 'CREDIT') {
                      this.credits.push({
                        creditLine: item.data.Credit_Account,
                        amountColId: item.data.columnId
                      })
                    } else {
                      this.credits.push({
                        creditLine: "",
                        amountColId: 0
                      })
                    }
                  } else {
                    if (item.data.lineType == 'DEBIT') {
                      this.debits.push({
                        debitLine: item.data.Debit_Account,
                        amountColId: item.data.columnId
                      })
                    } else {
                      this.debits.push({
                        debitLine: "",
                        amountColId: 0
                      })
                    }

                    if (item.data.lineType == 'CREDIT') {
                      this.credits.push({
                        creditLine: item.data.Credit_Account,
                        amountColId: item.data.columnId
                      })
                    } else {
                      this.credits.push({
                        creditLine: "",
                        amountColId: 0
                      })
                    }
                  }
                });
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

  /************* F14 *******************/
  /***Manual Accounting Post Functionality ***/
  manualAccounting(groupId, viewId) {
    this.buttonRestrict = true;
    this.recordsForAccounting = {};
    this.rowForAccounting = [];
    let filteredGroupP2: string = "";
    let groupedBy: string = "";
    if (this.selectedData.length > 0) {
      this.recordsForAccounting.groupId = groupId;
      this.recordsForAccounting.viewId = viewId;
      this.selectedData.forEach(item => {
        this.rowForAccounting.push({
          coaRef: this.chartOfAccount,
          currencyRef: item.data[this.currencyQualifier],
          categoryRef: this.acctCategory,
          ledgerRef: this.ledgername,
          sourceRef: this.acctSource,
          rowId: item.data.Id,
          debits: this.debits,
          credits: this.credits
        })
      });
      this.recordsForAccounting.rows = this.rowForAccounting;
      console.log('Debits : ' + JSON.stringify(this.debits));
      console.log('Credits : ' + JSON.stringify(this.credits));
      console.log('json for post: ' + JSON.stringify(this.recordsForAccounting));
      console.log('rows for post: ' + JSON.stringify(this.rowForAccounting));
      if (this.filteredGroupBy == 'rules' || this.filteredGroupBy == 'days' || this.filteredGroupBy == 'batch') {
        filteredGroupP2 = this.filteredGroupBy;
      } else {
        filteredGroupP2 = 'columnName';
      }
      if (this.groupBy == 'rules' || this.groupBy == 'days' || this.groupBy == 'batch' || this.groupBy == 'process') {
        groupedBy = this.groupBy;
      } else {
        groupedBy = 'columnName';
      }
      this.accountingDataService.manualAcct(this.recordsForAccounting).subscribe(
        (res: any) => {
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.buttonRestrict = false;
          this.accountModalTog = false;
          this.codeCombinationClear();
          this.notificationService.info('Info!', 'Selected Records Accounted Successfully');
          this.fetchAcctCountAmount(this.ruleGroupId, this.selectedDateRange, groupedBy, this.dataViewId);
          this.fetchOnDemandAcctRec(filteredGroupP2);
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
        }
      );
    }
  }


  /************* F15 *******************/
  /***Service to Fetch Acct Data on Demand ***/
  fetchOnDemandAcctRec(filterGrp) {
    this.buttonRestrict = true;
    this.accountingDataService.fetchingAllAcctGroupData(this.page, this.itemsPerPage, this.objectForUnRecon, filterGrp).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        this.dataViewLines = res;
        if (this.dataViewLines.length > 1) {
          this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
          this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
        } else {
          this.notificationService.info('Info!', 'No Records Found!');
        }
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res.json()
        )
        this.notificationService.error('Internal Server Error!', '');
      }
    )
  }


  /**************** F16 *******************/
  /***Manual Un Accounting Functionality***/
  openUnAccountDialog(type) {
    this.buttonRestrict = true;
    let accLen: number = 0;
    let filteredGroupP: string = "";
    let groupedBy: string = "";
    if (this.selectedData.length > 0) {
      for (var i = 0; i < this.selectedData.length; i++) {
        if (this.selectedData[i].data.Status == 'A') {
          accLen = accLen + 1;
        }
      }
    }
    console.log('selected record' + JSON.stringify(this.selectedData));
    if (this.filteredGroupBy == 'rules' || this.filteredGroupBy == 'days' || this.filteredGroupBy == 'batch') {
      filteredGroupP = this.filteredGroupBy;
    } else {
      filteredGroupP = 'columnName';
    }
    if (this.groupBy == 'rules' || this.groupBy == 'days' || this.groupBy == 'batch' || this.groupBy == 'process') {
      groupedBy = this.groupBy;
    } else {
      groupedBy = 'columnName';
    }
    if (type == 'batchwise') {
      this.accountingDataService.manualUnAcct(type, this.objectForUnRecon, filteredGroupP).subscribe(
        (res: any) => {
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.recChangeColor = 'mat-raised-button';
          this.unRecChangeColor = 'mat-raised-button';
          this.buttonRestrict = false;
          this.notificationService.success('Success!', 'Selected Batch Un Accounted Successfully!');
          this.accountedCount = 0;
          this.unAccountedCount = 0;
          this.objectForUnRecon.status = 'unaccounted';
          this.fetchAcctCountAmount(this.ruleGroupId, this.selectedDateRange, groupedBy, this.dataViewId);
          this.fetchOnDemandAcctRec(filteredGroupP);
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
        }
      );
    } else {
      if (this.selectedData.length == accLen) {
        this.acctKeysObject.batchNames = [];
        this.groupingColumnValues.columnValues = [];
        this.groupingColumnValues.columnId = "";
        this.acctKeysObject.columnValues = this.groupingColumnValues;
        this.acctKeysObject.days = [];
        this.acctKeysObject.ruleIds = [];
        this.acctKeysObject.viewId = 0;
        let acIds: any[] = [];
        if (this.selectedData.length > 0) {
          this.selectedData.forEach(item => {
            acIds.push(item.data.Id);
          });
        }
        this.aqAcctQueries.originalRowIds = acIds;
        this.aqAcctQueries.dataViewId = this.dataViewId;
        this.aqAcctQueries.groupId = this.ruleGroupId;
        this.accountingDataService.manualUnAcct(type, this.aqAcctQueries).subscribe(
          (res: any) => {
            this.accountedCount = 0;
            this.unAccountedCount = 0;
            this.recChangeColor = 'mat-raised-button';
            this.unRecChangeColor = 'mat-raised-button';
            this.buttonRestrict = false;
            this.notificationService.success('Success!', 'Selected Records Un Accounted Successfully!');
            this.objectForUnRecon.status = 'unaccounted';
            this.fetchAcctCountAmount(this.ruleGroupId, this.selectedDateRange, groupedBy, this.dataViewId);
            this.fetchOnDemandAcctRec(filteredGroupP);
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

  /**************** F17 *******************/
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
    this.credits.push(new debits());
    this.credits[0].amountColId = 0;
    this.credits[0].creditLine = "";
  }


  /**************** F18 *******************/
  /****show counts in Header Actions ******/
  showBatchWiseCounts() {
    this.accountedCount = 0;
    this.unAccountedCount = 0;
    this.recChangeColor = 'mat-raised-button';
    this.unRecChangeColor = 'mat-raised-button';
    if (this.filteredGroupByAnalytics.length > 0) {
      if (this.selectedKeys.length > 0) {
        this.selectedKeys.forEach(item => {
          this.filteredGroupByAnalytics.forEach(k => {
            if (item.name == k.name) {
              this.accountedCount = this.accountedCount + k.acount;
              this.unAccountedCount = this.unAccountedCount + k.ucount;
              if (this.unAccountedCount > 0) {
                this.recChangeColor = 'mat-raised-button rec-bg-color';
              }
              if (this.accountedCount > 0) {
                this.unRecChangeColor = 'mat-raised-button rec-bg-color';
              }
            }
          })
        });
      } else if (this.selectedAKeys.length > 0) {
        this.selectedAKeys.forEach(item => {
          this.filteredGroupByAnalytics.forEach(k => {
            if (item.name == k.name) {
              this.accountedCount = this.accountedCount + k.acount;
              if (this.accountedCount > 0) {
                this.unRecChangeColor = 'mat-raised-button rec-bg-color';
              }
            }
          })
        });
      } else if (this.selectedUKeys.length > 0) {
        this.selectedUKeys.forEach(item => {
          this.filteredGroupByAnalytics.forEach(k => {
            if (item.name == k.name) {
              this.unAccountedCount = this.unAccountedCount + k.ucount;
              if (this.unAccountedCount > 0) {
                this.recChangeColor = 'mat-raised-button rec-bg-color';
              }
            }
          })
        });
      } else if (this.selectedPKeys.length > 0) {
        this.selectedPKeys.forEach(item => {
          this.filteredGroupByAnalytics.forEach(k => {
            if (item.name == k.name) {
              this.accountedCount = this.accountedCount + k.pcount;
              if (this.accountedCount > 0) {
                this.unRecChangeColor = 'mat-raised-button rec-bg-color';
              }
            }
          })
        });
      } else {
        this.accountedCount = 0;
        this.unAccountedCount = 0;
      }

    }
  }


  /******************* F19 *******************/
  /**** Fetch Acct Analytics Counts and Amounts ******/

  fetchAcctCountAmount(groupId, selectedDateRang, groupBy, sViewId) {
    this.accountedCount = 0;
    this.unAccountedCount = 0;
    this.recChangeColor = 'mat-raised-button';
    this.unRecChangeColor = 'mat-raised-button';
    this.buttonRestrict = true;
    this.pieChartMetrics = [];
    this.rangeTo = this.datePipe.transform(new Date(Date.now()), 'yyyy-MM-dd H:mm:ss');
    this.rangeFrom = new Date(Date.now());
    if (selectedDateRang == '7days') {
      this.rangeFrom.setDate(this.rangeFrom.getDate() - 7);
    } else if (selectedDateRang == '15days') {
      this.rangeFrom.setDate(this.rangeFrom.getDate() - 15);
    } else if (selectedDateRang == '30days') {
      this.rangeFrom.setDate(this.rangeFrom.getDate() - 30);
    } else {
      this.rangeTo = this.toRange;
      this.rangeTo = this.datePipe.transform(new Date(Date.now()), 'yyyy-MM-dd H:mm:ss');
      this.rangeFrom = this.fromRange;
    }
    this.rangeFrom = this.datePipe.transform(this.rangeFrom, 'yyyy-MM-dd H:mm:ss');
    this.selectedKeys = [];
    this.selectedAKeys = [];
    this.selectedUKeys = [];
    this.selectedPKeys = [];
    this.filterGroupingParams = {};
    if (groupBy == 'rules' || groupBy == 'process' || groupBy == 'batch') {
      this.showColSelection = false;
      this.accountingDataService.fetchGroupByCount(groupId, this.rangeFrom, this.rangeTo, groupBy, sViewId).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.acctAnalytics = res;
          this.groupByAnalytics = this.acctAnalytics.groupedList;
          if (this.groupByAnalytics.length > 0) {
            if (groupBy == 'rules') {
              this.filteredGroupBy = 'days';
              if (this.groupByAnalytics[0].name == 'Others') {
                this.filterGroupingParams.othersRuleName = this.groupByAnalytics[0].name;
              } else {
                this.filterGroupingParams.ruleId = this.groupByAnalytics[0].id;
              }
            } else if (groupBy == 'process') {
              this.filterGroupingParams.viewId = this.groupByAnalytics[0].id;
              this.filteredGroupBy = 'rules';
            } else if (groupBy == 'batch') {
              this.filteredGroupBy = 'days';
              this.filterGroupingParams.batchName = this.groupByAnalytics[0].name;
            }
            this.groupByAnalytics.forEach(item => {
              this.pieChartMetrics.push({
                labels: ['Accounted', 'Un-Accounted', 'Partially-Accounted'],
                datasets: [
                  {
                    data: [item.accounted.amount.replace(/,/g, ""), item.unAccounted.amount.replace(/,/g, ""), item.partiallyAccounted.amount.replace(/,/g, "")],
                    backgroundColor: [
                      "#287a1d",
                      "#d33f3f",
                      "#d2633e"
                    ],
                    hoverBackgroundColor: [
                      "#287a1d",
                      "#d33f3f",
                      "#d2633e"
                    ]
                  }
                ],
                pieChartOptions: {
                  responsive: true,
                  maintainAspectRatio: false,
                  legend: {
                    display: false
                  },
                  title: {
                    display: true,
                    text: item.name
                  }
                }
              });
            })
            this.showAcctAnalytics = true;
            // this.isVisibleA = false;
            this.filteredGroupByList(this.selectedTab, groupId, this.filteredGroupBy, sViewId);
            this.callSetInt = true;
          } else {
            this.notificationService.info('Info!', 'No Records found with in Date Range');
          }
          this.getColumnHeaders(groupId, sViewId);
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
          this.notificationService.error('Warning!', 'Please contact system admin!');
        }
      );
    } else {
      this.accountingDataService.fetchGroupByCountByColId(groupId, this.rangeFrom, this.rangeTo, 'columnName', sViewId, groupBy).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.acctAnalytics = res;
          this.groupByAnalytics = this.acctAnalytics.groupedList;
          if (this.groupByAnalytics.length > 0) {
            this.filterGroupingParams.columnId = groupBy;
            this.filterGroupingParams.columnValue = this.groupByAnalytics[0].name;
            this.groupByAnalytics.forEach(item => {
              this.pieChartMetrics.push({
                labels: ['Accounted', 'Un-Accounted', 'Partially-Accounted'],
                datasets: [
                  {
                    data: [item.accounted.amount.replace(/,/g, ""), item.unAccounted.amount.replace(/,/g, ""), item.partiallyAccounted.amount.replace(/,/g, "")],
                    backgroundColor: [
                      "#287a1d",
                      "#d33f3f",
                      "#d2633e"
                    ],
                    hoverBackgroundColor: [
                      "#287a1d",
                      "#d33f3f",
                      "#d2633e"
                    ]
                  }
                ],
                pieChartOptions: {
                  responsive: true,
                  maintainAspectRatio: false,
                  legend: {
                    display: false
                  },
                  title: {
                    display: true,
                    text: item.name
                  }
                }
              });
            });
            this.showAcctAnalytics = true;
            // this.isVisibleA = false;
            this.filteredGroupBy = 'days';
            // this.getColumnHeaders(groupId, sViewId);
            // this.getGroupByAcctRecords(groupId, groupBy,this.rangeFrom, this.rangeTo, this.status);
            this.filteredGroupByList(this.selectedTab, groupId, this.filteredGroupBy, sViewId);
            this.callSetInt = true;
          } else {
            this.notificationService.info('Info!', 'No Records found with in Date Range');
          }
          this.getColumnHeaders(groupId, sViewId);
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
          this.notificationService.error('Warning!', 'Please contact system admin!');
        }
      );
    }
  }



  showPivotView() {
    this.buttonRestrict = true;
    this.acctPivotView = true;
    this.pvtInputObject = {
      "outputType": "PIVOT",
      "valueCols": [
        {
          "id": 9326,
          "itemName": "Amount",
          "dataName": "Amount",
          "dataType": "DECIMAL",
          "refType": "DAT_VIEW"
        }
      ],
      "groupingCols": [
        {
          "id": 9329,
          "itemName": "Entity Type",
          "dataName": "Entity Type",
          "dataType": "VARCHAR",
          "refType": "DAT_VIEW"
        },
        {
          "id": 9328,
          "itemName": "MOP",
          "dataName": "MOP",
          "dataType": "VARCHAR",
          "refType": "DAT_VIEW"
        },
        {
          "id": 586,
          "itemName": "job_reference",
          "dataName": "job_reference",
          "dataType": "VARCHAR",
          "refType": "DAT_VIEW"
        },
        {
          "id": 586,
          "itemName": "rule_code",
          "dataName": "rule_code",
          "dataType": "VARCHAR",
          "refType": "DAT_VIEW"
        },
        {
          "id": 9325,
          "itemName": "Presentment Currency",
          "dataName": "Presentment Currency",
          "dataType": "VARCHAR",
          "refType": "DAT_VIEW"
        },
        {
          "id": 586,
          "itemName": "acc_status",
          "dataName": "acc_status",
          "dataType": "VARCHAR",
          "refType": "DAT_VIEW"
        }
      ]
    }
    this.projectService.getPivotAcctOutput(this.dataViewId, this.ruleGroupId, this.pvtInputObject).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        this.pvtAcctResult = res;
        console.log('pviot response: ' + JSON.stringify(this.pvtAcctResult));
        let pvtRows = ["Presentment Currency"];
        let pvtCols = ["acc_status", "type"];
        let pvtVals = ["value"];
        var derivers = $.pivotUtilities.derivers;
        var renderers = $.extend($.pivotUtilities.renderers, $.pivotUtilities.gchart_renderers);
        var utils = $.pivotUtilities;
        var sum = $.pivotUtilities.aggregatorTemplates.sum;
        var numberFormat = $.pivotUtilities.numberFormat;
        var intFormat = numberFormat({ digitsAfterDecimal: 0 });
        $("#pvtoutput").pivotUI(
          this.pvtAcctResult,
          {
            rows: pvtRows,
            cols: pvtCols,
            vals: pvtVals,
            aggregator: sum(intFormat)(pvtVals),
            aggregatorName: "Sum",
            rendererOptions: {
              table: {
                clickCallback:
                  function (e, value, filters, pivotData) {
                    var names = [];
                    pivotData.forEachMatchingRecord(filters,
                      function (record) {
                        names.push(filters);
                      });
                  }
              }
            },
            rendererName: "Area Chart"
          }
        );
        $('#pvtoutput').addClass('table-responsive');
        $('.pvtTable').addClass('table table-striped');
      },
      (res: Response) => {
        this.buttonRestrict = false;
        this.onError(res)
        this.notificationService.error('Warning!', 'Please contact system admin!');
      }
    );
  }

  /*FUNCTION3 - Function to get Counts and Amounts Functionality on filtered data*/
  /* Author: BHAGATH */

  filteredGroupByList(e, groupId, filterGroupBy, sViewId) {
    this.accountedCount = 0;
    this.unAccountedCount = 0;
    this.recChangeColor = 'mat-raised-button';
    this.unRecChangeColor = 'mat-raised-button';
    this.buttonRestrict = true;
    this.selectedKeys = [];
    this.selectedAKeys = [];
    this.selectedUKeys = [];
    this.selectedPKeys = [];
    let filterGroupParam: string = "";
    console.log(e);
    if (!e || !e.index) {
      this.selectedTab = 0;
    } else {
      this.selectedTab = e.index;
    }
    if (this.groupBy == 'rules' || this.groupBy == 'process' || this.groupBy == 'batch') {
      if (this.groupBy == 'rules') {
        if (this.groupByAnalytics[this.selectedTab].name == 'Others') {
          this.filterGroupingParams.othersRuleName = this.groupByAnalytics[this.selectedTab].name;
          this.filterGroupingParams.ruleId = undefined;
        } else {
          this.filterGroupingParams.ruleId = this.groupByAnalytics[this.selectedTab].id;
        }
        if (filterGroupBy == 'days') {
          filterGroupParam = 'days';
        } else {
          filterGroupParam = 'columnName';
          this.filterGroupingParams.columnId = filterGroupBy;
        }
      } else if (this.groupBy == 'process') {
        this.filterGroupingParams.viewId = this.groupByAnalytics[this.selectedTab].id;
        if (filterGroupBy == 'days' || filterGroupBy == 'rules' || filterGroupBy == 'batch') {
          filterGroupParam = filterGroupBy;
        } else {
          filterGroupParam = 'columnName';
          this.filterGroupingParams.columnId = filterGroupBy;
        }
      } else if (this.groupBy == 'batch') {
        this.filterGroupingParams.batchName = this.groupByAnalytics[this.selectedTab].name;
        if (filterGroupBy == 'days') {
          filterGroupParam = 'days';
        } else {
          filterGroupParam = 'columnName';
          this.filterGroupingParams.columnId = filterGroupBy;
        }
      }
      this.showColSelection = false;
      this.accountingDataService.fetchFilteredGroupByCount(groupId, this.rangeFrom, this.rangeTo, this.groupBy, sViewId, this.filterGroupingParams, filterGroupParam).subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.filteredAcctAnalytics = res;
          this.filteredGroupByAnalytics = this.filteredAcctAnalytics.groupedList;
          if (this.filteredGroupByAnalytics.length > 0) {
            this.showAcctAnalytics = true;
            // this.isVisibleA = false;
            this.callSetInt = true;
          } else {
            this.notificationService.info('Info!', 'No Records found with in Date Range');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
          this.notificationService.error('Warning!', 'Please contact system admin!');
        }
      );
    } else {
      this.filterGroupingParams.columnValue = this.groupByAnalytics[this.selectedTab].name;
      this.filterGroupingParams.columnId = this.groupBy;
      this.filteredGroupBy = 'days';
      this.accountingDataService.fetchFilteredGroupByCountByColId(groupId, this.rangeFrom, this.rangeTo, 'columnName', sViewId, this.groupBy, this.filterGroupingParams, 'days').subscribe(
        (res: any) => {
          this.buttonRestrict = false;
          this.filteredAcctAnalytics = res;

          this.filteredGroupByAnalytics = this.filteredAcctAnalytics.groupedList;
          if (this.filteredGroupByAnalytics.length > 0) {
            this.showAcctAnalytics = true;
            // this.isVisibleA = false;
            this.callSetInt = true;
          } else {
            this.notificationService.info('Info!', 'No Records found with in Date Range');
          }
        },
        (res: Response) => {
          this.buttonRestrict = false;
          this.onError(res)
          this.notificationService.error('Warning!', 'Please contact system admin!');
        }
      );
    }
  }


  selectedPieData(event, viewId) {
    console.log('id: ' + viewId);

  }

  /*FUNCTION19 - Function to get Column headers of a data view STARTS */
  /* Author: BHAGATH */

  getColumnHeaders(groupId, viewId) {
    this.accountingDataService.fetchingColHeaders(groupId, viewId, this.status).subscribe(
      (res: any) => {
        this.sourceHeaderColumns1 = res;
        this.dataHeaderColumns = this.sourceHeaderColumns1.columns;
        this.dataHeaderColumns.splice(0, 1);
        this.dataColumnOptions = [];
        for (let i = 0; i < this.dataHeaderColumns.length; i++) {
          this.dataColumnOptions.push({ label: this.dataHeaderColumns[i].header, value: this.dataHeaderColumns[i] });
        }
        // this.reconcileService.getQualifierColumns('AMOUNT', viewId).subscribe((res: any) => {
        //   this.sortOrderColId = res.columnId;
        // });
      },
      (res: Response) => {
        this.onError(res.json())
        this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      })
  }


  /* FUNCTION21 - Function to fecth Column Ids and Name of a Data view */
  /* Author : BHAGATH */



  /*FUNCTION15 - Function to Fetch Records of a data view based on grouping */
  /* Author: BHAGATH */

  getGroupByAcctRecords(groupId, groupBy, filterGroupBy, status) {
    this.buttonRestrict = true;
    this.showCharts = false;
    // this.dataViewLines = [];
    this.aqAcctQueries.dataViewId = this.dataViewId;
    this.aqAcctQueries.groupId = groupId;
    let filterGroupByParam1: string = "";
    if (groupBy == 'rules' || groupBy == 'process' || groupBy == 'batch' || groupBy == 'days') {
      this.aqAcctQueries.groupBy = groupBy;
    } else {
      this.aqAcctQueries.groupBy = 'columnName';
    }
    if (filterGroupBy == 'rules' || filterGroupBy == 'batch' || filterGroupBy == 'days') {
      filterGroupByParam1 = filterGroupBy;
    } else {
      filterGroupByParam1 = 'columnName';
    }
    this.aqAcctQueries.rangeFrom = this.rangeFrom;
    this.aqAcctQueries.rangeTo = this.rangeTo;
    if (status == "") {
      this.selectedAKeys = [];
      this.selectedUKeys = [];
      this.selectedPKeys = [];
      if (this.selectedKeys.length > 0) {
        this.aqAcctQueries.status = "";
        this.customFilterStatus = "";
        this.selectedKeys.forEach(item => {
          if (groupBy == 'rules' || groupBy == 'batch') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              let newDate = new Date(item.name);
              let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
              this.acctKeysObject.days.push(newDate2);
            } else {
              if (!this.groupingColumnValues.columnValues) {
                this.groupingColumnValues.columnValues = [];
              }
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          }

          else if (groupBy == 'days') {
            if (!this.groupingColumnValues.columnValues) {
              this.groupingColumnValues.columnValues = [];
            }
            this.groupingColumnValues.columnValues.push(item.name);
            this.groupingColumnValues.columnId = filterGroupBy;
            this.acctKeysObject.columnValues = this.groupingColumnValues;
          }

          else if (groupBy == 'process') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              let newDate = new Date(item.name);
              let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
              this.acctKeysObject.days.push(newDate2);
            } else if (filterGroupBy == 'rules') {
              if (!this.acctKeysObject.ruleIds) {
                this.acctKeysObject.ruleIds = [];
              }
              this.acctKeysObject.ruleIds.push(item.id);
            } else if (filterGroupBy == 'batch') {
              if (!this.acctKeysObject.batchNames) {
                this.acctKeysObject.batchNames = [];
              }
              this.acctKeysObject.batchNames.push(item.name);
            } else {
              if (!this.groupingColumnValues.columnValues) {
                this.groupingColumnValues.columnValues = [];
              }
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else {
            if (!this.acctKeysObject.days) {
              this.acctKeysObject.days = [];
            }
            let newDate = new Date(item.name);
            let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            this.acctKeysObject.days.push(newDate2);
          }
        });
      } else {
        this.aqAcctQueries.status = status;
        this.acctKeysObject.batchNames = [];
        this.groupingColumnValues.columnValues = [];
        this.groupingColumnValues.columnId = "";
        this.acctKeysObject.columnValues = this.groupingColumnValues;
        this.acctKeysObject.days = [];
        this.acctKeysObject.ruleIds = [];
      }
    } else if (status == 'accounted') {
      this.selectedKeys = [];
      this.selectedUKeys = [];
      this.selectedPKeys = [];

      if (this.selectedAKeys.length > 0) {

        this.aqAcctQueries.status = "accounted";
        this.customFilterStatus = "accounted";
        this.selectedAKeys.forEach(item => {
          if (groupBy == 'rules' || groupBy == 'batch') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              if (item.name != 'Others') {
                let newDate = new Date(item.name);
                let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
                this.acctKeysObject.days.push(newDate2);
              }
            } else {
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else if (groupBy == 'days') {
            this.groupingColumnValues.columnValues.push(item.name);
            this.groupingColumnValues.columnId = filterGroupBy;
            this.acctKeysObject.columnValues = this.groupingColumnValues;
          } else if (groupBy == 'process') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              let newDate = new Date(item.name);
              let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
              this.acctKeysObject.days.push(newDate2);
            } else if (filterGroupBy == 'rules') {
              if (!this.acctKeysObject.ruleIds) {
                this.acctKeysObject.ruleIds = [];
              }
              this.acctKeysObject.ruleIds.push(item.id);
            } else if (filterGroupBy == 'batch') {
              if (!this.acctKeysObject.batchNames) {
                this.acctKeysObject.batchNames = [];
              }
              this.acctKeysObject.batchNames.push(item.name);
            } else {
              if (!this.groupingColumnValues.columnValues) {
                this.groupingColumnValues.columnValues = [];
              }
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else {
            if (!this.acctKeysObject.days) {
              this.acctKeysObject.days = [];
            }
            let newDate = new Date(item.name);
            let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            this.acctKeysObject.days.push(newDate2);
          }
        });
      } else {
        this.aqAcctQueries.status = status;
        this.acctKeysObject.batchNames = [];
        this.groupingColumnValues.columnValues = [];
        this.groupingColumnValues.columnId = "";
        this.acctKeysObject.columnValues = this.groupingColumnValues;
        this.acctKeysObject.days = [];
        this.acctKeysObject.ruleIds = [];
      }

    } else if (status == 'unaccounted') {
      this.selectedAKeys = [];
      this.selectedKeys = [];
      this.selectedPKeys = [];
      if (this.selectedUKeys.length > 0) {

        this.aqAcctQueries.status = "unaccounted";
        this.customFilterStatus = "unaccounted";
        this.selectedUKeys.forEach(item => {
          if (groupBy == 'rules' || groupBy == 'batch') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              if (item.name != 'Others') {
                let newDate = new Date(item.name);
                let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
                this.acctKeysObject.days.push(newDate2);
              }
            } else {
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else if (groupBy == 'days') {
            this.groupingColumnValues.columnValues.push(item.name);
            this.groupingColumnValues.columnId = filterGroupBy;
            this.acctKeysObject.columnValues = this.groupingColumnValues;
          } else if (groupBy == 'process') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              let newDate = new Date(item.name);
              let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
              this.acctKeysObject.days.push(newDate2);
            } else if (filterGroupBy == 'rules') {
              this.acctKeysObject.ruleIds = [];
            } else if (filterGroupBy == 'batch') {
              this.acctKeysObject.batchNames = [];
            } else {
              if (!this.groupingColumnValues.columnValues) {
                this.groupingColumnValues.columnValues = [];
              }
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else {
            if (!this.acctKeysObject.days) {
              this.acctKeysObject.days = [];
            }
            let newDate = new Date(item.name);
            let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            this.acctKeysObject.days.push(newDate2);
          }
        });
      } else {
        this.aqAcctQueries.status = status;
        this.acctKeysObject.batchNames = [];
        this.groupingColumnValues.columnValues = [];
        this.groupingColumnValues.columnId = "";
        this.acctKeysObject.columnValues = this.groupingColumnValues;
        this.acctKeysObject.days = [];
        this.acctKeysObject.ruleIds = [];
      }
    } else {
      this.selectedAKeys = [];
      this.selectedUKeys = [];
      this.selectedKeys = [];
      if (this.selectedPKeys.length > 0) {
        this.aqAcctQueries.status = "partiallyaccounted";
        this.customFilterStatus = "partiallyaccounted";
        this.selectedPKeys.forEach(item => {
          if (groupBy == 'rules' || groupBy == 'batch') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              if (item.name != 'Manual') {
                let newDate = new Date(item.name);
                let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
                this.acctKeysObject.days.push(newDate2);
              }
            } else {
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else if (groupBy == 'days') {
            this.groupingColumnValues.columnValues.push(item.name);
            this.groupingColumnValues.columnId = filterGroupBy;
            this.acctKeysObject.columnValues = this.groupingColumnValues;
          } else if (groupBy == 'process') {
            if (filterGroupBy == 'days') {
              if (!this.acctKeysObject.days) {
                this.acctKeysObject.days = [];
              }
              let newDate = new Date(item.name);
              let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
              this.acctKeysObject.days.push(newDate2);
            } else if (filterGroupBy == 'rules') {
              this.acctKeysObject.ruleIds = [];
            } else if (filterGroupBy == 'batch') {
              this.acctKeysObject.batchNames = [];
            } else {
              if (!this.groupingColumnValues.columnValues) {
                this.groupingColumnValues.columnValues = [];
              }
              this.groupingColumnValues.columnValues.push(item.name);
              this.groupingColumnValues.columnId = filterGroupBy;
              this.acctKeysObject.columnValues = this.groupingColumnValues;
            }
          } else {
            if (!this.acctKeysObject.days) {
              this.acctKeysObject.days = [];
            }
            let newDate = new Date(item.name);
            let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
            this.acctKeysObject.days.push(newDate2);
          }
        });
      } else {
        this.aqAcctQueries.status = status;
        this.acctKeysObject.batchNames = [];
        this.groupingColumnValues.columnValues = [];
        this.groupingColumnValues.columnId = "";
        this.acctKeysObject.columnValues = this.groupingColumnValues;
        this.acctKeysObject.days = [];
        this.acctKeysObject.ruleIds = [];
      }
    }
    this.aqAcctQueries.keyValues = this.acctKeysObject;
    this.aqAcctQueries.groupByParams = this.filterGroupingParams;
    this.aqAcctQueries.sortOrderBy = 'desc';
    this.aqAcctQueries.sortByColumnId = this.sortOrderColId;
    this.accountingDataService.fetchingAllAcctGroupData(this.page, this.itemsPerPage, this.aqAcctQueries, filterGroupByParam1).subscribe(
      (res: any) => {
        this.buttonRestrict = false;
        this.dataViewLines = res;
        this.isAcctProcess2 = true;
        this.objectForUnRecon = jQuery.extend(true, {}, this.aqAcctQueries);
        console.log('acct object stroing:' + JSON.stringify(this.objectForUnRecon));
        this.acctKeysObject.batchNames = [];
        this.groupingColumnValues.columnValues = [];
        this.groupingColumnValues.columnId = "";
        this.acctKeysObject.columnValues = this.groupingColumnValues;
        this.acctKeysObject.days = [];
        this.acctKeysObject.ruleIds = [];
        this.aqAcctQueries.groupByParams = {};
        this.aqAcctQueries = {};
        console.log('acct object stroing:' + JSON.stringify(this.objectForUnRecon));
        if (this.dataViewLines.length > 1) {
          this.isAcctProcess = true;
          this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
          this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
          console.log('Data View Info : ' + JSON.stringify(this.dataViewInfo));
        } else {
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

  nodeSelect(event) {

    //  this.notificationService.info('Info!',event.node.data.Id);
    // this.msgs.push({severity: 'info', summary: 'Node Selected', detail: event.node.data.Id});
  }

  nodeUnselect(event) {
    // this.notificationService.info('Info!',event.node.data.Id);
  }


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


  toggleSideBar() {
    if (!this.isVisibleB) {
      this.isVisibleB = true;
    } else {
      this.isVisibleB = false;
    }
  }

  onPaginateChange(event) {
    this.page = event.pageIndex;
    this.itemsPerPage = event.pageSize;
    if (this.selectedKeys.length > 0) {
      this.getGroupByAcctRecords(this.ruleGroupId, this.groupBy, this.filteredGroupBy, '');
    } else if (this.selectedAKeys.length > 0) {
      this.getGroupByAcctRecords(this.ruleGroupId, this.groupBy, this.filteredGroupBy, 'accounted');
    } else if (this.selectedUKeys.length > 0) {
      this.getGroupByAcctRecords(this.ruleGroupId, this.groupBy, this.filteredGroupBy, 'unreconciled');
    } else if (this.selectedPKeys.length > 0) {
      this.getGroupByAcctRecords(this.ruleGroupId, this.groupBy, this.filteredGroupBy, 'partiallyaccounted');
    } else {
      this.getGroupByAcctRecords(this.ruleGroupId, this.groupBy, this.filteredGroupBy, this.customFilterStatus);
    }
  }

  searchWithKeyWord(searchkey) {
    this.aqAcctQueries.searchWord = searchkey;
    this.getGroupByAcctRecords(this.ruleGroupId, this.groupBy, this.filteredGroupBy, this.customFilterStatus);
  }

  /* FUNCTION24 : Function to call OnDemand Recon Job and Auto Refresh Functionality */
  /* AUTHOR : BHAGATH */



  initiateAcctJob(purpose) {
    let fromDate: any = "";
    let toDate: any = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    console.log('range form :' + this.rangeFrom + ' + ' + 'Range To: ' + this.rangeTo);
    this.dateRange = [fromDate, toDate];
    let colValues: any = [];
    if (purpose == 'ACCOUNTING') {
      if (this.filteredGroupBy == 'rules' || this.filteredGroupBy == 'batch') {
        this.paramSet = {
          'param1': this.ruleGroupId,
          'param3': this.dataViewId,
          'param6': this.dateRange
        }
        this.initiatingJob(purpose, this.paramSet);
      } else if (this.filteredGroupBy == 'days') {
        // this.reconcileService.getQualifierColumns('TRANSDATE', this.dataViewId).subscribe((res: any) => {
        //   this.qualifierColumns = res;
        //   this.qualifierColumnId = this.qualifierColumns.columnId;
        //   if (this.selectedKeys.length > 0) {
        //     this.selectedKeys.forEach(item => {
        //       let newDate = new Date(item.name);
        //       let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
        //       colValues.push(newDate2);
        //     });
        //     this.paramSet = {
        //       'param1': this.ruleGroupId,
        //       'param3': this.dataViewId,
        //       'param4': this.qualifierColumnId,
        //       'param5': colValues,
        //       'param6': this.dateRange
        //     }
        //   } else if (this.selectedUKeys.length > 0) {
        //     this.selectedUKeys.forEach(item => {
        //       let newDate = new Date(item.name);
        //       let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
        //       colValues.push(newDate2);
        //     });
        //     this.paramSet = {
        //       'param1': this.ruleGroupId,
        //       'param3': this.dataViewId,
        //       'param4': this.qualifierColumnId,
        //       'param5': colValues,
        //       'param6': this.dateRange
        //     }
        //   }
        //   this.initiatingJob(purpose, this.paramSet);
        //   colValues = [];
        // },
        //   (res: Response) => {
        //     this.onError(res.json())
        //     this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        //   }
        // )
      } else {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach(item => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.ruleGroupId,
            'param3': this.dataViewId,
            'param4': this.viewColumnId,
            'param5': colValues,
            'param6': this.dateRange
          }
        } else if (this.selectedUKeys.length > 0) {
          this.selectedUKeys.forEach(item => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.ruleGroupId,
            'param3': this.dataViewId,
            'param4': this.viewColumnId,
            'param5': colValues,
            'param6': this.dateRange
          }
        }
        this.initiatingJob(purpose, this.paramSet);
        colValues = [];

      }
    } else {
      if (this.filteredGroupBy == 'rules' || this.filteredGroupBy == 'batch') {
        this.paramSet = {
          'param1': this.reconRuleGroupId,
          'param3': this.dataViewId,
          'param6': this.dateRange
        }
        this.initiatingJob(purpose, this.paramSet);
      } else if (this.groupBy == 'days') {

        // this.reconcileService.getQualifierColumns('TRANSDATE', this.dataViewId).subscribe((res: any) => {
        //   this.qualifierColumns = res;
        //   this.qualifierColumnId = this.qualifierColumns.columnId;
        //   if (this.selectedKeys.length > 0) {
        //     this.selectedKeys.forEach(item => {
        //       let newDate = new Date(item.name);
        //       let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
        //       colValues.push(newDate2);
        //     });
        //     this.paramSet = {
        //       'param1': this.reconRuleGroupId,
        //       'param3': this.dataViewId,
        //       'param4': this.qualifierColumnId,
        //       'param5': colValues,
        //       'param6': this.dateRange
        //     }
        //   } else if (this.selectedUKeys.length > 0) {
        //     this.selectedUKeys.forEach(item => {
        //       let newDate = new Date(item.name);
        //       let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
        //       colValues.push(newDate2);
        //     });
        //     this.paramSet = {
        //       'param1': this.reconRuleGroupId,
        //       'param3': this.dataViewId,
        //       'param4': this.qualifierColumnId,
        //       'param5': colValues,
        //       'param6': this.dateRange
        //     }
        //   } else {
        //     this.paramSet = {
        //       'param1': this.reconRuleGroupId,
        //       'param3': this.dataViewId,
        //       'param6': this.dateRange
        //     }
        //   }
        //   this.initiatingJob(purpose, this.paramSet);
        //   colValues = [];
        // },
        //   (res: Response) => {
        //     this.onError(res.json())
        //     this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        //   }
        // )


      } else {
        if (this.selectedKeys.length > 0) {
          this.selectedKeys.forEach(item => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.dataViewId,
            'param4': this.viewColumnId,
            'param5': colValues,
            'param6': this.dateRange
          }
        } else if (this.selectedUKeys.length > 0) {
          this.selectedUKeys.forEach(item => {
            colValues.push(item.name);
          });
          this.paramSet = {
            'param1': this.reconRuleGroupId,
            'param3': this.dataViewId,
            'param4': this.viewColumnId,
            'param5': colValues,
            'param6': this.dateRange
          }
        }
        this.initiatingJob(purpose, this.paramSet);
        colValues = [];

      }
    }

  }




  beforeJobInitiate() {
    if (this.selectedKeys.length > 0 || this.selectedUKeys.length > 0) {
      this.displayConfirmDialog = true;
    } else if (this.selectedAKeys.length > 0) {
      this.notificationService.info('Info!', 'Please selected Unreconciled Records!');
    } else {
      this.notificationService.info('Info', 'No Records Selected');
    }
  }

  buildRule() {
    this.reconcileService.ENABLE_RULE_BLOCK = true;
    this.create = this.ruleGroupId + '-' + this.create;
  }

  loadAll() {
    let groupedBy: string = "";
    let filteredGroupP: string = "";
    if (this.groupBy == 'rules' || this.groupBy == 'days' || this.groupBy == 'batch' || this.groupBy == 'process') {
      groupedBy = this.groupBy;
    } else {
      groupedBy = 'columnName';
    }
    this.fetchAcctCountAmount(this.ruleGroupId, this.selectedDateRange, groupedBy, this.dataViewId);
    if (this.filteredGroupBy == 'rules' || this.filteredGroupBy == 'days' || this.filteredGroupBy == 'batch') {
      filteredGroupP = this.filteredGroupBy;
    } else {
      filteredGroupP = 'columnName';
    }
    this.accountingDataService.fetchingAllAcctGroupData(this.page, this.itemsPerPage, this.objectForUnRecon).subscribe(
      (res: any) => {
        this.dataViewLines = res;
        if (this.dataViewLines.length > 1) {
          this.viewLength = this.dataViewLines[this.dataViewLines.length - 1].info.totalCount;
          this.dataViewInfo = this.dataViewLines.splice(this.dataViewLines.length - 1, 1);
        } else {
          this.notificationService.info('Info!', 'No Records Found!');
        }
      },
      (res: Response) => {
        this.onError(res.json()
        )
        this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      }
    )

  }

  scheduleJobRecon() {
    this.acctSchedObj = {};
    this.acctSchedParamms = [];
    let fromDate: any = "";
    let toDate: any = "";
    let colValues: string = "";
    fromDate = this.rangeFrom;
    toDate = this.rangeTo;
    fromDate = this.datePipe.transform(fromDate, 'yyyy-MM-dd');
    toDate = this.datePipe.transform(toDate, 'yyyy-MM-dd');
    console.log('range form :' + fromDate + ' + ' + 'Range To: ' + toDate);
    if (this.groupBy == 'rules' || this.groupBy == 'batch' || this.groupBy == 'process') {
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
    }
    else if (this.groupBy == 'days') {
      // this.reconcileService.getQualifierColumns('TRANSDATE', this.dataViewId).subscribe((res: any) => {
      //   this.qualifierColumns = res;
      //   this.qualifierColumnId = this.qualifierColumns.columnId;
      //   if (this.selectedKeys.length > 0) {
      //     this.selectedKeys.forEach(item => {
      //       let newDate = new Date(item.name);
      //       let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
      //       if (colValues.length > 1) {
      //         colValues = colValues + "," + newDate2;
      //       } else {
      //         colValues = newDate2;
      //       }
      //     });
      //   } else if (this.selectedUKeys.length > 0) {
      //     this.selectedUKeys.forEach(item => {
      //       let newDate = new Date(item.name);
      //       let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
      //       if (colValues.length > 1) {
      //         colValues = colValues + "," + newDate2;
      //       } else {
      //         colValues = newDate2;
      //       }
      //     });
      //   }
      //   if (this.selectedKeys.length > 0 || this.selectedUKeys.length > 0) {
      //     this.acctSchedParamms = [
      //       {
      //         paramName: 'RuleGroupName',
      //         selectedValue: this.ruleGroupId
      //       },
      //       {
      //         paramName: 'Filter View',
      //         selectedValue: this.dataViewId
      //       },
      //       {
      //         paramName: 'Date Range',
      //         selectedValue: fromDate + "," + toDate
      //       },
      //       {
      //         paramName: 'Filter Column',
      //         selectedValue: this.qualifierColumnId
      //       },
      //       {
      //         paramName: 'Filter Values',
      //         selectedValue: colValues
      //       }
      //     ]
      //   } else {
      //     this.acctSchedParamms = [
      //       {
      //         paramName: 'RuleGroupName',
      //         selectedValue: this.ruleGroupId
      //       },
      //       {
      //         paramName: 'Filter View',
      //         selectedValue: this.dataViewId
      //       },
      //       {
      //         paramName: 'Date Range',
      //         selectedValue: fromDate + "," + toDate
      //       }
      //     ]
      //   }
      //   this.acctSchedObj.parameters = this.acctSchedParamms;
      //   this.acctSchedObj.jobName = this.ruleGroupName;
      //   this.acctSchedObj.programName = 'ACCOUNTING';
      //   console.log('scheduled obj' + JSON.stringify(this.acctSchedObj));


      // },
      //   (res: Response) => {
      //     this.onError(res.json())
      //     this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      //   }
      // )
    } else {
      if (this.selectedKeys.length > 0) {
        this.selectedKeys.forEach(item => {
          let newDate = new Date(item.name);
          let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          if (colValues.length > 1) {
            colValues = colValues + "," + newDate2;
          } else {
            colValues = newDate2;
          }
        });
      } else if (this.selectedUKeys.length > 0) {
        this.selectedUKeys.forEach(item => {
          let newDate = new Date(item.name);
          let newDate2 = this.datePipe.transform(newDate, 'yyyy-MM-dd');
          if (colValues.length > 1) {
            colValues = colValues + "," + newDate2;
          } else {
            colValues = newDate2;
          }
        });
      }
      if (this.selectedKeys.length > 0 || this.selectedUKeys.length > 0) {
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
          },
          {
            paramName: 'Filter Column',
            selectedValue: this.viewColumnId
          },
          {
            paramName: 'Filter Values',
            selectedValue: colValues
          }
        ]
      } else {
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
      }
      this.acctSchedObj.parameters = this.acctSchedParamms;
      this.acctSchedObj.jobName = this.ruleGroupName;
      this.acctSchedObj.programName = 'ACCOUNTING';
    }

    this.displayScheduling = true;

  }



  initiatingJob(purpose, set) {
    this.buttonRestrict = true;
    console.log('param set: ' + JSON.stringify(set));
    if (purpose == 'ACCOUNTING') {
      this.jobDetailsService.initiateAcctRecJob(purpose, set).subscribe((res: any) => {
        this.buttonRestrict = false;
        this.acctJobOutput = res;
        this.acctJobId = this.acctJobOutput.status;
        this.runAcctShow = false;
        this.displayConfirmDialog = false;
        console.log('acct job ref:' + JSON.stringify(this.acctJobId));
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
                this.recChangeColor = 'mat-raised-button';
                this.unRecChangeColor = 'mat-raised-button';
                console.log('acct job status:' + JSON.stringify(this.acctJobStatus._body));
                if (this.acctJobStatus._body == 'SUCCEEDED' || this.acctJobStatus._body == 'KILLED' || this.acctJobStatus._body == 'FAILED') {
                  if (this.setIntId) {
                    clearInterval(this.setIntId);
                  }
                } else {
                  console.log('calling load all:');
                  this.loadAll();
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
        console.log('recon job ref:' + JSON.stringify(this.reconJobId));
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
  cols:any[]=[];
  ngOnInit() {
    console.log('coars lenth ; '+ this.carss.length);
    this.cols = [
      {field: 'vin', header: 'Vin'},
      {field: 'year', header: 'Year'},
      {field: 'brand', header: 'Brand'},
      {field: 'color', header: 'Color'}
    ];
    if(this.selectedParentTab != 1){
      this.groupingHeads = ["Sub Process","Batch","Currency Code"];
    } else {
      this.groupingHeads = ["Period","Currency Code"];
    }
    this.fetchRuleGroups();
    this.getChartOfAccounts();
    this.fetchSourceLookups('SOURCE');
    this.fetchSourceLookups('CATEGORY');
    //this.jobDetailsService.getUserInfo();
    $(".split-example").css({
      'height': 'auto',
      'min-height': (this.commonService.screensize().height - 130) + 'px'
    });
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
