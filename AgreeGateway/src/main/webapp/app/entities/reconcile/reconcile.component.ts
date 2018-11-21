/*
  Module Name: Reconciliation Work Queue
  Author: BHAGATH

  Search the function with Function key:

  1. FUNCTION1 : Side bar toggler functionality
  2. FUNCTION2 : Function to get Rule Groups based on type 
  3. FUNCTION3 : Function to fetch count and amount by rule group id
  4. FUNCTION4 : Function to fetch all records by type
  5. FUNCTION5 : Function to get Selected Source Lines
  6. FUNCTION6 : Function to get Selected target Lines
  7. FUNCTION7 : Function to post selected source and target lines for reconciliation
  8. FUNCTION8 : Function to fetch records by type and status on click of pie chart
  9. FUNCTION9 : Pagination event function
  10. FUNCTION10 : Function to Fetch Data View Columns by View Id
  11. FUNCTION11 : Function to apply operator based on column name selection
  12. FUNCTION12 : Function to fetch records based on custom filters
  13. FUNCTION13 : Function to toggle Target View
  14. FUNCTION14 : Function to add new filter line
  15. FUNCTION15 : Function to clear filter
  16. FUNCTION16 : On Load Function
  17. FUNCTION17 : Posting adhoc rule functionality
  18. FUNCTION18 : 
  19. FUNCTION19 : 
  13. FUNCTION20 : 
  
*/




import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import {
  Reconcile, ReportTypeRecords, ReconciliationRecords, SourceReconciliationLines,
  TargetReconciliationLines, ReconcileBreadCrumbTitles, customFilters, SfilterColumns, TfilterColumns,
  reconcileIdsforSource, reconcileIdsforTarget, selectedReconciledIds,
} from './reconcile.model';
import { ReconcileService } from './reconcile.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ENABLE_RULE_BLOCK } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NotificationsService } from 'angular2-notifications-lite';
import { FileTemplateLinesService } from '../file-template-lines/file-template-lines.service';
import { CommonService } from '../common.service';
import { RuleGroupService } from '../rule-group/rule-group.service';
import { PageEvent } from '@angular/material';
import { DataViewsService } from '../data-views/data-views.service';
import { JobDetailsService } from '../jobs/job-details.service';
import { RuleGroupWithRulesAndConditions } from '../rule-group/ruleGroupWithRulesAndConditions.model';
import { SessionStorageService } from 'ng2-webstorage';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
declare var $: any;

declare var AmCharts: any;
declare var anychart: any;
const COMMA = 188;

@Component({
  selector: 'jhi-reconcile',
  templateUrl: './reconcile.component.html'
})
export class ReconcileComponent implements OnInit,OnDestroy {

  currentAccount: any;
  reconciles: Reconcile[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  param: string;
  search: any;
  values: string[];
  currentSearch: any;
  length: number;
  pageSizeOptions = [10, 25, 50, 100];;
  pageEvent: PageEvent;
  /* Custom Variables */
  labelData: any[];
  sourceHeaderColumns: any[] = [];
  sourceHeaderColumns1: any;
  targetHeaderColumns: any[] = [];
  targetHeaderColumns1: any;
  sourceHeaders: any[] = [];
  targetHeaders: any[] = [];
  sourceColumnOptions: SelectItem[];
  targetColumnOptions: SelectItem[];
  sourceDataViews: SourceReconciliationLines[] = [];
  targetDataViews: TargetReconciliationLines[] = [];
  sourceDataViewLines: any[] = [];
  targetDataViewLines: any[] = [];
  dataViewLines: any[];
  sourceDataReconciledLines: any[];
  sourceDataUnreconciledLines: any[];
  targetDataReconciledLines: any[];
  targetDataUnreconciledLines: any[];
  reconciliationResultView = new ReconciliationRecords();
  showAllData: any[];
  showSourceTable: boolean;
  showTargetTable: boolean;
  ruleGroups: any[];
  ruleGroupId: any;
  approvalRuleGroupId:any;
  selectedSourceLinesToReconcile: any[] = [];
  selectedTargetLinesToReconcile: any[] = [];
  hideTargetData: boolean = true;
  soucreTableHeight: any;
  targetTableHeight: any;
  targetTableWidth: any;
  targetTableTop: any;
  targetTableLeft: any;
  reconParams: any;
  sourceDataViewId: number;
  targetDataViewId: number;
  sourceOrTarget: any;
  selectedSourceTotal: number = 0;
  selectedTargetTotal: number = 0;
  isVisibleA: boolean = true;
  isReconProcess: boolean = false;
  customFilterObject: customFilters;
  sourceTargetPercent: any;
  operatorsList = [];
  sdataTypeList = [];
  tdataTypeList = [];
  sfilterColumns: SfilterColumns[] = [];
  tfilterColumns: TfilterColumns[] = [];
  toggleTargetFilter: boolean = false;
  toggleSourceFilter: boolean = false;
  reconIdstoReconcile: selectedReconciledIds;
  sourceReconIds: reconcileIdsforSource[] = [];
  targetReconIds: reconcileIdsforTarget[] = [];
  sdataViewColumns: any[] = [];
  tdataViewColumns: any[] = [];
  create: string = 'createInWQ';
  paramSet:any;
  enableDisableButton:boolean = true;
  targetDataViewName:any;
  setIntId:any;
  genericColumnHeaders: any[];
  dataViewDetails: any[] = [];
  sourceDataInfo: any;
  sourceData: any;
  targetDataInfo: any;
  sourceDataAmtQualifier: any;
  targetDataAmtQualifier: any;
  allSourceLines: any[] = [];
  sourceAmounts: any;
  targetAmounts: any;
  sourceReconciled: any;
  sourceUnReconciled: any;
  targetReconciled: any;
  targetUnReconciled: any;
  pieChartOptions: any;
  display: boolean = false;
  statusOnPieChart: any = "";
  targetRecordsLength: number;
  sourceRecordsLength: number;
  showPaginator: boolean = false;
  public doughnutChartType: string = 'pie';
  ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
  filterPagination: boolean = false;
  isVisibleB: boolean = true;
  sideBarFilter: any[];
  sideBarFilters: any[];
  customFilterStatus: any;
  sFilterColumnsSidebar: SfilterColumns[] = [];
  sideBarFiltersValueSet: any[] = [];
  sideBarFiltersColumnValue: string[] = [];
  showSourceTables: boolean = false;
  unReconcileModal: boolean = false;
  showTargetTables: boolean = false;
  reconFilterColumns: SfilterColumns[] = [];
  unReconciledSRecords: any;
  unReconciledTRecords: any;
  unReconciledSRecordsLength: any;
  unReconciledTRecordsLength: any;
  ruleGroupName: any;
  unReconciledResult: any;
  manualUnreconciliationDialog: boolean = false;
  filteringData: SfilterColumns[] = [];
  enableruleBlock: boolean;
  callSetInt:boolean = false;
  ruleGroupList:any = [];
  constructor(
    private ruleGroupService: RuleGroupService,
    private reconcileService: ReconcileService,
    private parseLinks: JhiParseLinks,
    private alertService: JhiAlertService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager,
    private paginationUtil: JhiPaginationUtil,
    private paginationConfig: PaginationConfig,
    private notificationService: NotificationsService,
    private fileTemplatesLinesService: FileTemplateLinesService,
    private commonService: CommonService,
    private dataViewsService: DataViewsService,
    private $sessionStorage: SessionStorageService,
    private jobDetailsService: JobDetailsService,
    public dialog: MdDialog
  ) {

    this.itemsPerPage = ITEMS_PER_PAGE;
    // this.routeData = this.activatedRoute.data.subscribe((data) => {
    //   this.page = data['pagingParams'].page;
    //   console.log('page : ' + this.page);
    //   this.previousPage = data['pagingParams'].page;
    //   this.reverse = data['pagingParams'].ascending;
    //   this.predicate = data['pagingParams'].predicate;
    // });
    this.pieChartOptions = {
      responsive: false,
      legend: {
        display: true,
        position: 'top',
        fullWidth: true,
      }
    }


    this.sideBarFilter = [
      {
        'id': 9,
        'columnName': 'Processor Name',
        "columnValuesSet": [
          {
            "name": "ADYEN",
            "count": 1834,
            "groupName": "Processor Name"
          },
          {
            "name": "ALIPAY",
            "count": 8,
            "groupName": "Processor Name"
          },
          {
            "name": "AMEX_INTL",
            "count": 26,
            "groupName": "Processor Name"
          },
          {
            "name": "AMEX",
            "count": 975,
            "groupName": "Processor Name"
          },
          {
            "name": "BRAINTREE",
            "count": 29,
            "groupName": "Processor Name"
          },
          {
            "name": "CHASE",
            "count": 1681,
            "groupName": "Processor Name"
          },
          {
            "name": "PAYPAL",
            "count": 125,
            "groupName": "Processor Name"
          },
          {
            "name": "PAYPAL_INTL",
            "count": 66,
            "groupName": "Processor Name"
          },
          {
            "name": "PAYTM",
            "count": 26,
            "groupName": "Processor Name"
          },
          {
            "name": "PAYU",
            "count": 26,
            "groupName": "Processor Name"
          },
          {
            "name": "WORLDPAY",
            "count": 4,
            "groupName": "Processor Name"
          },
          {
            "name": "ZAAKPAY",
            "count": 66,
            "groupName": "Processor Name"
          }
        ],
      },
      {
        'id': 2,
        'columnName': 'Currency Code',
        'columnValuesSet': [
          {
            'name': 'USD',
            'count': 68,
            "groupName": "Currency Code"
          },
          {
            'name': 'INR',
            'count': 56,
            "groupName": "Currency Code"
          },
          {
            'name': 'EUR',
            'count': 74,
            "groupName": "Currency Code"
          },
          {
            'name': 'LKR',
            'count': 12,
            "groupName": "Currency Code"
          },
          {
            'name': 'AUD',
            'count': 10,
            "groupName": "Currency Code"
          }
        ]
      },
      {
        'id': 3,
        'columnName': 'Ledger Name',
        'columnValuesSet': [
          {
            'name': 'US Ledger',
            'count': 68,
            "groupName": "Ledger Name"
          },
          {
            'name': 'IN Ledger',
            'count': 56,
            "groupName": "Ledger Name"
          },
          {
            'name': 'AU Ledger',
            'count': 74,
            "groupName": "Ledger Name"
          },
          {
            'name': 'HK Ledger',
            'count': 12,
            "groupName": "Ledger Name"
          },
          {
            'name': 'UK Ledger',
            'count': 10,
            "groupName": "Ledger Name"
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

  /* FUNCTION1 - Side bar toggler functionality 
     Author: Bhagath */

  toggleSB() {
    if (!this.isVisibleA) {
      this.isVisibleA = true;
    } else {
      this.isVisibleA = false;
    }
  }


  /*FUNCTION2 -  Function to get Rule Groups STARTS*/
  /* Author: BHAGATH */

  fetchRuleGroups() {
    this.ruleGroupService.fetchRuleGroupsBypurpose('RECONCILIATION').subscribe(
      (res: any) => {
        this.ruleGroups = res;
      },
      (res: Response) => {
        this.onError(res)
        this.notificationService.error('Warning!', 'Error Occured while fetching Reconciliation Rule Groups!');
      }
    );
  }

  
  /* FUNCTION3 - Function to fetch count and amount by rule group id STARTS*/
  /* Author: BHAGATH */
 loadAll(){
   console.log('Rule Group Id : '+this.ruleGroupId);
   console.log('Data View ID:'+this.sourceDataViewId);
   this.fetchCountAndAmountByRuleGroupId(this.ruleGroupId);
 }

  fetchCountAndAmountByRuleGroupId(id) {
    this.ruleGroupId = id;
    this.isReconProcess = true;
    this.reconcileService.reconciliationData(id).subscribe((res: any) => {
      this.reconciliationResultView = res;
      if (this.reconciliationResultView.info.status == 'Success') {
        this.ruleGroupName = this.reconciliationResultView.groupName;
        this.sourceDataViews = this.reconciliationResultView.source;
        this.sourceDataViewId = this.sourceDataViews[0].viewId;
        this.targetDataViews = this.reconciliationResultView.target;
        this.targetDataViewId = this.targetDataViews[0].viewId;
        // this.targetDataViewName = this.targetDataViews[0].viewName;
        this.sourceAmounts = {
          labels: ['Reconciled', 'Un-Reconciled'],
          datasets: [
            {
              data: [this.sourceDataViews[0].reconciled.amount.replace(/,/g, ""), this.sourceDataViews[0].unReconciled.amount.replace(/,/g, "")],
              backgroundColor: [
                "#36A2EB",
                "#FF6384"
              ],
              hoverBackgroundColor: [
                "#36A2EB",
                "#FF6384"
              ]
            }
          ]
        }
        this.targetAmounts = {
          labels: ['Reconciled', 'Un-Reconciled'],
          datasets: [
            {
              data: [this.targetDataViews[0].reconciled.amount.replace(/,/g, ""), this.targetDataViews[0].unReconciled.amount.replace(/,/g, "")],
              backgroundColor: [
                "#36A2EB",
                "#FF6384",
              ],
              hoverBackgroundColor: [
                "#36A2EB",
                "#FF6384"

              ]
            }
          ]
        }
        this.callSetInt = true;
        this.fetchRecordsByTypeAndStatus(this.sourceDataViewId, 'source', id);
      } else {
        this.notificationService.error('Error!', this.reconciliationResultView.info.reasons[0]);
      }
    },
      (res: Response) => {
        this.onError(res.json()
        )
        this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      }
    );
  }


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
        this.notificationService.error('Internal Server Error!', 'Please contact system admin');
      }
    )
  }

  /* FUNCTION5 - Function to get target count and amount on source select STARTS */
  /* Author: BHAGATH */
  fetchCountAmountOnSourceSelect(id) {
    this.statusOnPieChart = "";
    this.filterPagination = false;
    this.sourceDataViewId = id;
    this.sourceDataViews.forEach(item => {
      if (item.viewId == id) {
        this.targetDataViews = item.target;
      }
    })
    this.fetchRecordsByTypeAndStatus(id, 'source', this.ruleGroupId);
  }
  fetchRecordsOnTargetSelect(id) {
    this.statusOnPieChart = "";
    this.filterPagination = false;
    this.targetDataViewId = id;
    this.fetchRecordsByTypeAndStatus(id, 'target', this.ruleGroupId);
  }

  /* FUNCTION4 - Function to fetch all records by type STARTS*/
  /* Author: BHAGATH */

  fetchRecordsByTypeAndStatus(dataViewId, sourceOrTarget, ruleGroupId) {
    this.page = 0;
    if (sourceOrTarget == 'source') {
      this.reconcileService.fetchAllReconciledRecords(dataViewId, sourceOrTarget, ruleGroupId, this.page, this.itemsPerPage).subscribe(
        (res: any) => {
          this.sourceDataViewLines = res;
          if (this.sourceDataViewLines.length > 1) {
            this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
            this.sourceDataInfo = this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
            console.log('source data info:' + JSON.stringify(this.sourceDataInfo));
            if (this.sourceDataInfo[0].info.status == 'Success') {
              this.sourceDataInfo.forEach(k => {
                this.sourceDataAmtQualifier = k.info.amountQualifier;
              })
              this.getColumnHeaders(ruleGroupId, dataViewId, sourceOrTarget);
              this.sourceDataViews.forEach(item => {
                if (dataViewId == item.viewId) {
                  this.sourceAmounts = {
                    labels: ['Reconciled', 'Un-Reconciled'],
                    datasets: [
                      {
                        data: [item.reconciled.amount.replace(/,/g, ""), item.unReconciled.amount.replace(/,/g, "")],
                        backgroundColor: [
                          "#36A2EB",
                          "#FF6384",
                          "#3AD23D",
                          "#3CVF32",
                          "#23SS33"
                        ],
                        hoverBackgroundColor: [
                          "#36A2EB",
                          "#FF6384",
                          "#36A2EB",
                          "#36A2EB",
                          "#36A2EB"
                        ]
                      }
                    ]
                  }
                }
              })
              this.isVisibleA = false;
              this.sideBarFilteringData(dataViewId, ruleGroupId);
              this.showSourceTables = true;
            }
          } else {
            this.notificationService.error('Error Occured!', 'Unable to Fetch Records!');
          }
        },
        (res: Response) => {
          this.onError(res.json())
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    } else if (sourceOrTarget == 'target') {
      this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, 'unreconciled', ruleGroupId, this.page, this.itemsPerPage).subscribe(
        (res: any) => {
          this.targetDataViewLines = res;

          if (this.targetDataViewLines.length > 1) {
            this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
            this.targetDataInfo = this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
            if (this.targetDataInfo[0].info.status == 'Success') {
              this.targetDataInfo.forEach(l => {
                this.targetDataAmtQualifier = l.info.amountQualifier;
              })
              this.getColumnHeaders(ruleGroupId, dataViewId, sourceOrTarget);
              this.targetDataViews.forEach(item => {
                if (dataViewId == item.viewId) {
                  this.targetAmounts = {
                    labels: ['Reconciled', 'Un-Reconciled'],
                    datasets: [
                      {
                        data: [item.reconciled.amount.replace(/,/g, ""), item.unReconciled.amount.replace(/,/g, "")],
                        backgroundColor: [
                          "#36A2EB",
                          "#FF6384",
                        ],
                        hoverBackgroundColor: [
                          "#36A2EB",
                          "#FF6384"

                        ]
                      }
                    ]
                  }
                }
              });
              this.showTargetTables = true;
            }
          } else {
            this.notificationService.error('Error Occured!', 'Unable to Fetch Records!');
          }
        }
        ,
        (res: Response) => {
          this.onError(res.json())
          this.notificationService.error('Internal Server Error!', 'Please contact system admin');
        }
      )
    }
  }

  /* FUNCTION5 - Function to get Selected Source Lines STARTS */
  /* Author: BHAGATH */
  onClickFindMatch() {
    let onClickBoolean: boolean = false;
    if (this.selectedSourceLinesToReconcile.length == 1) {
      if (this.selectedSourceLinesToReconcile[0].Status == 'Reconciled') {
        this.getColumnHeaders(this.ruleGroupId, this.targetDataViewId, 'target');
        this.display = true;
        this.showTargetTables = true;
        this.customFilterObject = {};
        this.reconFilterColumns = [];
        this.reconFilterColumns.push({
          columnName: 'Recon_Ref_Id',
          columnValue: this.selectedSourceLinesToReconcile[0].Recon_Ref_Id,
          operator: 'EQUALS',
        });
        this.customFilterObject.groupId = this.ruleGroupId;
        this.customFilterObject.limit = this.itemsPerPage;
        this.customFilterObject.page = 0;
        // this.customFilterObject.sourceOrTarget = 'source';
        this.customFilterObject.status = 'reconciled';
        // this.customFilterObject.viewId = this.sourceDataViewId;
        this.customFilterObject.filterColumns = this.reconFilterColumns;
        console.log('custom filter object : ' + JSON.stringify(this.customFilterObject));
        // this.reconcileService.customFilterSearch(this.customFilterObject).subscribe(
        //   (res: any) => {
        //     this.sourceDataViewLines = res;
        //     if (this.sourceDataViewLines.length > 0) {
        //       this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
        //       this.sourceDataInfo = this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
        //     }
        this.customFilterObject.viewId = this.targetDataViewId;
        this.customFilterObject.sourceOrTarget = 'target';
        this.reconcileService.customFilterSearch(this.customFilterObject).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            if (this.targetDataViewLines.length > 0) {
              this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
              this.targetDataInfo = this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
            }
          },
          (res: Response) => {
            this.onError(res.json())
            this.notificationService.error('Internal Server Error!', 'Please contact system admin');
          });
        // });
      } else {
        this.display = true;
        this.showTargetTables = true;
        this.fetchRecordsByTypeAndStatus(this.targetDataViewId, 'target', this.ruleGroupId);
      }
    } else if (this.selectedSourceLinesToReconcile.length > 1) {
      let unreconLen: number = 0;
      let reconLen: number = 0;
      for (var i = 0; i < this.selectedSourceLinesToReconcile.length; i++) {
        if (this.selectedSourceLinesToReconcile[i].Status == 'Un-Reconciled') {
          unreconLen = unreconLen + 1;
        } else {
          reconLen = reconLen + 1;
        }
      }
      if (this.selectedSourceLinesToReconcile.length == unreconLen) {
        this.display = true;
        this.showTargetTables = true;
        this.fetchRecordsByTypeAndStatus(this.targetDataViewId, 'target', this.ruleGroupId);
      } else if (this.selectedSourceLinesToReconcile.length == reconLen) {
        this.notificationService.error('Check Status!', 'Please select Only one Reconciled Records to Compare with target');
      } else {
        this.notificationService.error('Check Status!', 'Please select Only Unreconciled Records for Reconciliation');
      }
    } else {
      this.notificationService.error('Warning!', 'No records selected!');
    }
  }


  selectedSourceLines() {
      //console.log('selected source lines:'+JSON.stringify(this.selectedSourceLinesToReconcile));
    this.selectedSourceTotal = 0;
    for (var item of this.selectedSourceLinesToReconcile) {
      this.selectedSourceTotal = this.selectedSourceTotal + parseInt(item[this.sourceDataAmtQualifier].replace(/,/g, ""));
    }
  }

  /* FUNCTION6 - Function to get Selected target Lines STARTS */
  /* Author: BHAGATH */

  selectedTargetLines() {
    this.selectedTargetTotal = 0;
    for (var item of this.selectedTargetLinesToReconcile) {
      this.selectedTargetTotal = this.selectedTargetTotal + parseInt(item[this.targetDataAmtQualifier].replace(/,/g, ""));
    }
    this.sourceTargetPercent = ((this.selectedTargetTotal / this.selectedSourceTotal) * 100).toFixed(2);
  }

  /* FUNCTION7 - Function to post selected source and target lines for reconciliation */
  /* Author: BHAGATH */

  postingRecordstoRecocile() {
    this.selectedSourceLinesToReconcile.forEach(item => {
      this.sourceReconIds.push({
        groupId: this.ruleGroupId,
        rowId: item.Id,
        viewId: this.sourceDataViewId
      });
    });
    this.selectedTargetLinesToReconcile.forEach(item => {
      this.targetReconIds.push({
        groupId: this.ruleGroupId,
        rowId: item.Id,
        viewId: this.targetDataViewId
      });
    });
    this.reconIdstoReconcile = {};
    this.reconIdstoReconcile.source = this.sourceReconIds;
    this.reconIdstoReconcile.target = this.targetReconIds;
  }

  onClickRadioCheck(dataViewId, ruleGroupId, sourceOrTarget, status) {
    this.reconcileService.fetchSidebarDataBasedOnStatus(dataViewId, ruleGroupId, sourceOrTarget, status).subscribe(
      (res: any) => {
        this.sideBarFilters = res;
        if (status == 'reconciled') {
          this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, status, ruleGroupId, this.page, this.itemsPerPage).subscribe(
            (res: any) => {
              this.statusOnPieChart == 'Reconciled';
              this.filterPagination == false;
              this.sourceDataViewLines = res;
              if (this.sourceDataViewLines.length > 1) {
                this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
                this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
              } else {
                this.notificationService.error('Error Occured!', 'Unable to Fetch Records!');
              }
            },

            (res: Response) => {
              this.onError(res.json())
              this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
            }

          )
        } else if (status == 'unreconciled') {
          this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, status, ruleGroupId, this.page, this.itemsPerPage).subscribe(
            (res: any) => {
              this.statusOnPieChart == 'Un-Reconciled';
              this.filterPagination == false;
              this.targetDataViewLines = res;
              if (this.targetDataViewLines.length > 1) {
                this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
                this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
              } else {
                this.notificationService.error('Error Occured!', 'Unable to Fetch Records!');
              }
            },

            (res: Response) => {
              this.onError(res.json())
              this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
            }
          )
        }
        // this.fetchRecordsByTypeAndStatus(this.sourceDataViewId, 'source', this.ruleGroupId);
      },
      (res: Response) => {
        this.onError(res.json())
        this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
      }
    );
  }
  /* FUNCTION8 - Function to fetch records by type and status STARTS*/
  /* Author: BHAGATH */

  selectedData(event, dataViewId, sourceOrTarget, ruleGroupId) {
    this.filterPagination = false;
    this.page = 0;
    if (sourceOrTarget == 'source') {
      this.statusOnPieChart = this.sourceAmounts.labels[event.element._index];
      if (this.statusOnPieChart == 'Un-Reconciled') {
        this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, 'unreconciled', ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.customFilterStatus = 'unreconciled';
            this.sourceDataViewLines = res;
            if (this.sourceDataViewLines.length > 1) {
              this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
              this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
              this.onClickRadioCheck(dataViewId, ruleGroupId, sourceOrTarget, 'unreconciled');
            } else {
              this.notificationService.error('Warning!', 'No Records Found');
            }
          },

          (res: Response) => {
            this.onError(res.json())
            this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
          }
        )
      } else if (this.statusOnPieChart == 'Reconciled') {
        this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, 'reconciled', ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.customFilterStatus = 'reconciled';
            this.sourceDataViewLines = res;
            if (this.sourceDataViewLines.length > 1) {
              this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
              this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
              this.onClickRadioCheck(dataViewId, ruleGroupId, sourceOrTarget, 'reconciled');
            } else {
              this.notificationService.info('Warning!', 'No Records Found');
            }
          },

          (res: Response) => {
            this.onError(res.json())
            this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
          }
        )
      }
    } else if (sourceOrTarget == 'target') {
      this.statusOnPieChart = this.targetAmounts.labels[event.element._index];
      if (this.statusOnPieChart == 'Un-Reconciled') {
        this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, 'reconciled', ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            if (this.targetDataViewLines.length > 1) {
              this.display = true;
              this.showTargetTables = true;
              this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
              this.targetDataInfo = this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
            } else {
              this.notificationService.info('Warning!', 'No Records Found');
            }
          },

          (res: Response) => {
            this.onError(res.json())
            this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
          }
        )
      } else if (this.statusOnPieChart == 'Reconciled') {
        this.reconcileService.fetchRecordsBasedOnStatus(dataViewId, sourceOrTarget, 'reconciled', ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            if (this.targetDataViewLines.length > 1) {
              this.display = true;
              this.showTargetTables = true;
              this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
              this.targetDataInfo = this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
            } else {
              this.notificationService.info('Warning!', 'No Records Found');
            }
          },

          (res: Response) => {
            this.onError(res.json())
            this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
          }
        )
      }
    }
  }


  // this.sideBarFilters.forEach(item => {
  //       //this.sideBarFiltersValueSet.push(item.columnValuesSet);
  //       console.log('selected values' + JSON.stringify(this.sideBarFiltersColumnValue));

  //         if (this.sideBarFiltersColumnValue) {
  //           for (var i = 0; i < this.sideBarFiltersColumnValue.length; i++) {
  //             if (i == this.sideBarFiltersColumnValue.length - 1)
  //               colValue = colValue + this.sideBarFiltersColumnValue[i];
  //             else {
  //               colValue = colValue + this.sideBarFiltersColumnValue[i] + ',';
  //             }
  //           }

  //         }
  //       this.sFilterColumnsSidebar.push({
  //         columnName: item.columnName,
  //         operator: 'EQUALS',
  //         columnValue: colValue
  //       })
  //     })

  unReconcileRecords() {
    this.enableDisableButton = false;
    if (this.selectedSourceLinesToReconcile.length > 0) {
      let reconRefId: number[] = [];
      this.selectedSourceLinesToReconcile.forEach(item => {
        if (item.Status == 'Reconciled') {
          reconRefId.push(item.Recon_Ref_Id);
          console.log('recon ref:' + reconRefId);
        }
      })

    }
  }

  openUnReconDialog() {
    if (this.selectedSourceLinesToReconcile.length > 0) {
      let reconforUnReconCount: number = 0;
      let reconRefId: string = this.selectedSourceLinesToReconcile[0].Recon_Ref_Id;
      for (var i = 0; i < this.selectedSourceLinesToReconcile.length; i++) {
        if (this.selectedSourceLinesToReconcile[i].Status == 'Reconciled') {
          reconforUnReconCount = reconforUnReconCount + 1;
        }
      }
      if (this.selectedSourceLinesToReconcile.length == reconforUnReconCount) {
//        this.selectedSourceLinesToReconcile.forEach(item => {
//          if (this.selectedSourceLinesToReconcile.length > 1) {
//            reconRefId = reconRefId + ',' + item.Recon_Ref_Id;
//          } else {
//            reconRefId = item.Recon_Ref_Id;
//          }
//        })
          for (var i = 1; i < this.selectedSourceLinesToReconcile.length; i++) {
              if(this.selectedSourceLinesToReconcile.length > 1){
                  reconRefId = reconRefId + ',' + this.selectedSourceLinesToReconcile[i].Recon_Ref_Id;
              } else {
                  reconRefId = reconRefId;
              }
          }
        this.customFilterObject = {};
        this.filteringData = [];
        this.filteringData.push({
          columnName: 'Recon_Ref_Id',
          columnValue: reconRefId,
          operator: 'EQUALS'
        });
        this.customFilterObject.filterColumns = this.filteringData;
        this.customFilterObject.groupId = this.ruleGroupId;
        this.customFilterObject.limit = this.itemsPerPage;
        this.customFilterObject.page = 0;
        this.customFilterObject.sourceOrTarget = 'source';
        this.customFilterObject.status = 'reconciled';
        this.customFilterObject.viewId = this.sourceDataViewId;
        console.log('custom filter bject :' + JSON.stringify(this.customFilterObject));
        this.reconcileService.customFilterSearch(this.customFilterObject).subscribe(
          (res: any) => {
            this.unReconciledSRecords = res;
            this.unReconciledSRecords.splice(this.targetDataViewLines.length - 1, 1);
            this.unReconciledSRecordsLength = this.unReconciledSRecords.length;
            this.customFilterObject.viewId = this.targetDataViewId;
            this.customFilterObject.sourceOrTarget = 'target';
            this.customFilterObject.tenantId = '';
            console.log('custom filter bject :' + JSON.stringify(this.customFilterObject));
            this.reconcileService.customFilterSearch(this.customFilterObject).subscribe(
              (res: any) => {
                this.filteringData = [];
                this.manualUnreconciliationDialog = true;
                this.unReconciledTRecords = res;
                this.unReconciledTRecords.splice(this.targetDataViewLines.length - 1, 1);
                this.unReconciledTRecordsLength = this.unReconciledSRecords.length;
              },
              (res: Response) => {
                this.onError(res.json())
                this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
              }
            );
          },
          (res: Response) => {
            this.onError(res.json())
            this.notificationService.error('Internal Server Error!', 'Please contact system admin!');
          }
        );
      } else {
        this.notificationService.info('Warning!', 'Please select only reconciled records to unreconcile');
      }
    } else {
      this.notificationService.error('Warning!', 'No records selected!');
    }
  }



  /*FUNCTION9 - Pagination event function STARTS */
  /* Author: BHAGATH */
  onPaginateChange(event, type) {
    this.page = event.pageIndex;
    this.itemsPerPage = event.pageSize;
    if (type == 'source') {
      if (this.statusOnPieChart == 'Reconciled' && this.filterPagination == false) {
        this.reconcileService.fetchRecordsBasedOnStatus(this.sourceDataViewId, type, 'reconciled', this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.sourceDataViewLines = res;
            this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
            this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
          })
      } else if (this.statusOnPieChart == 'Un-Reconciled' && this.filterPagination == false) {
        this.reconcileService.fetchRecordsBasedOnStatus(this.sourceDataViewId, type, 'unreconciled', this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.sourceDataViewLines = res;
            this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
            this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
          })
      } else if (this.filterPagination == true) {
        this.customFilterObject.page = event.pageIndex;
        this.reconcileService.customFilterSearch(this.customFilterObject).subscribe((res: any) => {
          this.sourceDataViewLines = res;
          this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
          this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
        });
      } else if (this.statusOnPieChart != 'Reconciled' && this.filterPagination == false && this.statusOnPieChart != 'Un-Reconciled') {
        this.reconcileService.fetchAllReconciledRecords(this.sourceDataViewId, type, this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.sourceDataViewLines = res;
            this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
            this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
          })
      } else {
        this.reconcileService.fetchAllReconciledRecords(this.sourceDataViewId, type, this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.sourceDataViewLines = res;
            this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
            this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
          })
      }
    } else if (type == 'target') {
      if (this.statusOnPieChart == 'Reconciled' && this.filterPagination == false) {
        this.reconcileService.fetchRecordsBasedOnStatus(this.targetDataViewId, type, 'reconciled', this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
            this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
          })
      } else if (this.statusOnPieChart == 'Un-Reconciled' && this.filterPagination == false) {
        this.reconcileService.fetchRecordsBasedOnStatus(this.targetDataViewId, type, 'unreconciled', this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
            this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
          })
      } else if (this.filterPagination == true) {
        this.reconcileService.fetchRecordsBasedOnStatus(this.targetDataViewId, type, 'unreconciled', this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
            this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
          })
      } else if (this.statusOnPieChart != 'Reconciled' && this.filterPagination == false && this.statusOnPieChart != 'Un-Reconciled') {
        this.reconcileService.fetchAllReconciledRecords(this.targetDataViewId, type, this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
            this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
          })
      } else {
        this.reconcileService.fetchAllReconciledRecords(this.targetDataViewId, type, this.ruleGroupId, this.page, this.itemsPerPage).subscribe(
          (res: any) => {
            this.targetDataViewLines = res;
            this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
            this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);
          })
      }
    }
  }

  /*FUNCTION10 - Function to Fetch Data View Columns by View Id */
  /* Author: BHAGATH */

  fetchDataViewColumns(id, sourceOrTarget) {
    this.dataViewsService.fetchDataViewsAndColumns(id).subscribe((res: any) => {
      this.dataViewDetails = res;
      if (sourceOrTarget == 'source') {
        this.sdataViewColumns = [];
        this.dataViewDetails[0].dataViewsColumnsList.forEach(j => {
          this.sdataViewColumns.push(j.columnHeader);
          this.sdataTypeList.push(j.colDataType);
        })
      } else if (sourceOrTarget == 'target') {
        this.tdataViewColumns = [];
        this.dataViewDetails[0].dataViewsColumnsList.forEach(j => {
          this.tdataViewColumns.push(j.columnHeader);
          this.tdataTypeList.push(j.colDataType);
        })
        console.log('t veiw details:' + JSON.stringify(this.tdataViewColumns));
      }
    },
      (res: Response) => this.onError(res.json())
    )
  }

  /*FUNCTION11 - Function to apply operator based on column name selection */
  /* Author: BHAGATH */

  onSelectColumnName(ind, colName, type) {
    for (var i = 0; i < this.dataViewDetails[0].dataViewsColumnsList.length; i++) {
      var colObj = this.dataViewDetails[0].dataViewsColumnsList[i];
      if (colObj.columnHeader == colName) {
        this.dataViewsService.operators(colObj.colDataType).subscribe((res: any) => {
          this.operatorsList[ind] = [];
          this.operatorsList[ind] = res;
        });
      }
    }

  }

  /* FUNCTION12 - Function to fetch records based on custom filters */
  /* Author: BHAGATH */

  fetchRecordsByCustomFilter(dataViewId, sourceOrTarget, status) {
    this.enableDisableButton = false;
    this.customFilterObject = {};
    this.customFilterObject.sourceOrTarget = sourceOrTarget;
    this.customFilterObject.status = status;
    this.customFilterObject.viewId = dataViewId;
    this.customFilterObject.limit = this.itemsPerPage;
    this.customFilterObject.page = 0;
    this.customFilterObject.groupId = this.ruleGroupId;

    if (sourceOrTarget == 'target') {
      this.customFilterObject.filterColumns = this.tfilterColumns;
      this.tfilterColumns.forEach(item => {
        item.columnName = item.columnName1;
        item.operator = item.operator1;
      })
      this.reconcileService.customFilterSearch(this.customFilterObject).subscribe((res: any) => {
        this.enableDisableButton = false;
        this.filterPagination = true;
        this.targetDataViewLines = res;
        this.targetRecordsLength = this.targetDataViewLines[this.targetDataViewLines.length - 1].info.totalCount;
        this.targetDataViewLines.splice(this.targetDataViewLines.length - 1, 1);

      });
    } else if (sourceOrTarget == 'source') {
      this.customFilterObject.filterColumns = this.sfilterColumns;
      this.reconcileService.customFilterSearch(this.customFilterObject).subscribe((res: any) => {
        this.enableDisableButton = false;
        this.filterPagination = true;
        this.sourceDataViewLines = res;
        this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
        this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
      });
    }
  }

  /*  FUNCTION18 - Sidebar Filter JSON Data */
  sideBarFilteringData(viewId, groupId) {
    this.reconcileService.fetchSidebarData(viewId, groupId, 'source').subscribe((res: any) => {
      this.sideBarFilters = res;
    });
  }
  /* FUNCTION19 - Function to fetch records using sidebar */
  /* Author: BHAGATH */
  onTabOpen(event) {
    this.sideBarFiltersColumnValue = [];
    this.sFilterColumnsSidebar = [];
  }


  fetchRecordsSidebarFilter(dataViewId) {
    this.sFilterColumnsSidebar = [];
    let colValue: string = "";
    this.customFilterObject = {};
    this.customFilterObject.sourceOrTarget = 'source';
    this.customFilterObject.viewId = dataViewId;
    this.customFilterObject.limit = this.itemsPerPage;
    this.customFilterObject.page = 0;
    this.customFilterObject.groupId = this.ruleGroupId;

    if (this.sideBarFiltersColumnValue.length > 0) {
      for (var i = 0; i < this.sideBarFiltersColumnValue.length; i++) {
        for (var j = 0; j < this.sideBarFilters.length; j++) {
          this.sideBarFiltersValueSet = this.sideBarFilters[j].columnValuesSet;
          for (var k = 0; k < this.sideBarFiltersValueSet.length; k++) {
            if (this.sideBarFiltersColumnValue[i] == this.sideBarFiltersValueSet[k].name) {
              if (this.sFilterColumnsSidebar.length == 0) {
                this.sFilterColumnsSidebar.push({
                  columnName: this.sideBarFilters[j].columnName,
                  columnValue: this.sideBarFiltersColumnValue[i],
                  operator: 'EQUALS'
                });
                this.sideBarFiltersColumnValue.splice(0, 1);
              } else {
                for (var x = 0; x < this.sFilterColumnsSidebar.length; x++) {
                  if (this.sFilterColumnsSidebar[x].columnName == this.sideBarFilters[j].columnName) {
                    this.sFilterColumnsSidebar[x].columnValue = this.sFilterColumnsSidebar[x].columnValue + ',' + this.sideBarFiltersColumnValue[i];
                    this.sideBarFiltersColumnValue.splice(0, 1);
                    break;
                  } else {
                    this.sFilterColumnsSidebar.push({
                      columnName: this.sideBarFilters[j].columnName,
                      columnValue: this.sideBarFiltersColumnValue[i],
                      operator: 'EQUALS'
                    });
                    this.sideBarFiltersColumnValue.splice(0, 1);
                  }
                }
              }
            }
          }
        }
      }
    }
    this.customFilterObject.filterColumns = this.sFilterColumnsSidebar;
    this.customFilterObject.status = this.customFilterStatus;
    this.reconcileService.customFilterSearch(this.customFilterObject).subscribe((res: any) => {
      this.filterPagination = true;
      this.sourceDataViewLines = res;
      if(this.sourceDataViewLines.length > 0){
        this.sourceRecordsLength = this.sourceDataViewLines[this.sourceDataViewLines.length - 1].info.totalCount;
        this.sourceDataViewLines.splice(this.sourceDataViewLines.length - 1, 1);
      } else {
        this.notificationService.info('Info!','No Records Found');
      }
    },
      (res: Response) => {this.onError(res.json())
        this.notificationService.error('Internal Server Error!','Please contact System Admin');
      }
    );
  }

  /* FUNCTION13  - Function to toggle Target View STARTS*/
  /* Author: BHAGATH */

  toggleTargetView() {
    $("#main_target_div").css({
      'visibility': 'hidden'
    });
    $("#target_data_view").css({
      'visibility': 'hidden'
    });
  }


  /* FUNCTION14 - Function to add new filter line */
  /* Author: BHAGATH */
  addColumn(type) {
    if (type == 'source') {
      let newsLine = {
        "columnValue": ""
      };
      this.sfilterColumns.push(newsLine);
    } else if (type == 'target') {
      let newtLine = {
        "columnValue": ""
      };
      this.tfilterColumns.push(newtLine);
    }
  }

  removeLine(i: number, type) {
    if (type == 'source') {
      this.sfilterColumns.splice(i, 1);
    } else if (type == 'target') {
      this.tfilterColumns.splice(i, 1);
    }

  }

  /* FUNCTION15 - Function to clear filter */
  /* Author: BHAGATH */
  clearFilter(type) {
    if (type == 'source') {
      this.sfilterColumns = [];
      let newLine = {
        "columnValue": ''
      };
      this.sfilterColumns.push(newLine);
    } else if (type == 'target') {
      this.tfilterColumns = [];
      let newLine = {
        "columnValue": ''
      };
      this.tfilterColumns.push(newLine);
    }
  }

  /* FUNCTION16 - On Load Function */
  /* Author : BHAGATH */
  ngOnInit() {
    //  this.jobDetailsService.getUserInfo();
    this.fetchRuleGroups();
    this.soucreTableHeight = (this.commonService.screensize().height - 350) + 'px';
    this.targetTableHeight = (this.commonService.screensize().height - 600) + 'px';
    this.targetTableWidth = (parseInt($('.source-view-table').outerWidth()) - 100) + 'px';
    this.targetTableTop = this.commonService.screensize().width;
    this.targetTableLeft = (parseInt($('.source-view-table').outerWidth()) / 1.8) + 'px';
    $(".split-example").css({
      'height': 'auto',
      'min-height': (this.commonService.screensize().height - 130) + 'px'
    });

    if (this.selectedTargetLinesToReconcile.length > 0) {
      this.selectedTargetTotal = 0;
      this.selectedTargetLinesToReconcile.forEach(item => {
        this.selectedTargetTotal = this.selectedTargetTotal + parseInt(item.ROC_AMT);
      });
    }

    this.addColumn('source');
    this.addColumn('target');
    this.principal.identity().then((account) => {
      this.currentAccount = account;
    });
    
    this.reconcileService.fetchRuleGroupIds().subscribe((res: any) => {
        this.ruleGroupList = res;
        console.log('this.ruleGroupList ' + JSON.stringify(this.ruleGroupList));
    });
  }

  toggleSideBar() {
    if (!this.isVisibleB) {
      this.isVisibleB = true;
      $('.split-example .left-component').addClass('visible');
    } else {
      this.isVisibleB = false;
      $('.split-example .left-component').addClass('visible');
    }
  }

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

  /* Showing Build Adhoc Rule Modal */
  /* Author: SHOBHA */
  buildRule() {
    this.display = false;
    this.reconcileService.ENABLE_RULE_BLOCK = true;
    this.create = this.ruleGroupId + '-' + this.create;
  }

  /* FUNCTION17 - Posting adhoc rule functionality */
  /* Author : SHOBHA */
  /*postRule(groupId: any) {
    let ruleGroupRulesAndConditionsToSave = new RuleGroupWithRulesAndConditions();
    ruleGroupRulesAndConditionsToSave = this.$sessionStorage.retrieve('reconciliationRuleList');
    //set adhoc type here
     ruleGroupRulesAndConditionsToSave.rulePurpose = 'ORPHAN_RECON';
     ruleGroupRulesAndConditionsToSave.name = 'ORPHAN';
     ruleGroupRulesAndConditionsToSave.startDate = new Date();
    ruleGroupRulesAndConditionsToSave.id = groupId;
    this.ruleGroupService.postRuleGroup(ruleGroupRulesAndConditionsToSave).subscribe((res: any) => {
      let savedObj = [];
      savedObj = res;
      //let ruleId: any;
      //  ruleId = savedObj[0].subTasksList[0].details;
      this.notificationService.success(
        '',
        'Job initiated with created rule'
      )
    });
  }*/

  /* AUTHOR: AMIT */
  setApprovalGrpId(ruleGrpId:any){
    console.log('ruleGrpId ' + ruleGrpId);
    console.log('approvalRuleGroupId ' + this.approvalRuleGroupId);
    this.initiateRecJob();
  }

  /* Initiaing Job for Reconcilation Functionality*/
  /* AUTHOR: BHAGATH */

  initiateRecJob() {
    this.enableDisableButton = false;
        this.paramSet = {
            'param1': this.ruleGroupId,
            'param3': this.approvalRuleGroupId/* ,
            'param4': null */
        }
        this.jobDetailsService.initiateAcctRecJob('Reconciliation and Approvals', this.paramSet).subscribe((res: any) => {
            //  let dialogRef = this.dialog.open(confimationDialog, {
            //     width: '300px'
            // });
            this.notificationService.success('','Job Initiated Successfully!');
            this.enableDisableButton = true;
            this.setIntId = setInterval(() => {
              if(this.ruleGroupId != undefined && this.sourceDataViewId != undefined && this.callSetInt == true) {
              console.log('calling load all:');
              this.loadAll();
            }
            },30000);
        },
            (res: Response) => {
                this.onError(res.json())
                this.notificationService.error('Internal Server Error!', 'Please contact system admin');
                this.enableDisableButton = true;
            }
        )
    }
  ngOnDestroy(){
    if(this.setIntId){
      clearInterval(this.setIntId);
    }
  }


}
