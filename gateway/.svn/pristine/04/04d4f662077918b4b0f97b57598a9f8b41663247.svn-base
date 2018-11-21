import { Component, Input, OnInit, OnDestroy, ViewChild, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router, NavigationEnd } from '@angular/router';
import { RuleGroupService } from './rule-group.service';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { FormGroup, FormBuilder, FormArray, Validators, NgForm, FormControl } from '@angular/forms';
import { RuleGroupAndRuleWithLineItem, AccountingRuleGroup } from './ruleGroupAndRuleWithLineItem.model';
import { AccountingRule } from './accRule.model';
import { AccountingLine } from './LineItem.model';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { DataViewsService } from '../data-views/data-views.service';
import { LineDerivations } from './line-derivations.model';
import { Observable } from 'rxjs/Rx';
import { MappingSetService } from '../mapping-set/mapping-set.service';
import { LedgerDefinitionService } from '../ledger-definition/ledger-definition.service';
import { RulesService } from '../rules/rules.service';
import { JhiDateUtils } from 'ng-jhipster';
import { AccountingRuleConditions } from './accounting-rule-conditions.model';
import { ChartOfAccountService } from '../chart-of-account/chart-of-account.service';
import { SegmentsService } from '../segments/segments.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { AccountingConfirmActionModalDialog } from './rule-group-accounting-confirm-action-dialog';
//import { NotificationsService } from 'angular2-notifications-lite';
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';
import * as $ from "jquery";
import { ReconcileService } from '../reconcile/reconcile.service';
import { CommonService } from '../common.service';
declare var jQuery: any;
const URL = '';



@Component({
    selector: 'jhi-rule-group-accounting-new',
    templateUrl: './rule-group-accounting-new.component.html'
})
export class RuleGroupAccountingNewComponent {
    emptySpace=' ';
    dispViewField = false;
    ngxScrollToLastRule: string;
    openRuleTab: any = [];
    // debitSelectedIndex : any[] ;
    //  creditSelectedIndex : any[];
    //today=new Date();  
    startDateChange = false;
    endDateChange = false;
    reconDVdropdownSettings = {
        singleSelection: true,
        // text: "Recon data source",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        selectionLimit: 1,
        autoUnselect: true,
        closeOnSelect: true,
        showCheckbox: false
    };
    dropdownSettings = {
        singleSelection: true,
        // text: "Data source",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        selectionLimit: 1,
        autoUnselect: true,
        closeOnSelect: true,
        showCheckbox: false
    };

    @ViewChild(NgForm) accRuleForm;
    @Input() accountingMode = '';
    @Input() validateRuleForm: Function;
    routeSub: any;
    ruleCreationInWQ = false;

    isViewOnly = false;
    isCreateNew = false;
    isEdit = false;
    copyRule = false;

    mouseOverRule: number = -1;
    mouseOverDebitLine: number = -1;
    mouseOverCreditLine: number = -1;

    ruleListformArray: FormGroup[] = [];
    //ruleListArrays: any[] = [];

   // filteredRuleList: any[] = [];

    dataViewformArray: FormGroup[] = [];
    sourceDataViewsArrays: any = [];

    //dvColumns: any[] = [];

    mouseOverLineItem: any;

    chartOfAccounts = [];

    lineTypeDetails: any[] = [];
    columnsByViewId = [];
    segments = [];
    linetypes = [];
    accCriterias = [];
    headerLineTypes: Observable<Response>;
    // operators = [];
    //debitLineLevelOperatorsLOV = [];
    //creditLineLevelOperatorsLOV = [];
    mappingSets = [];
    ledgers = [];
    ledgersBasedOnCOA = [];
    debitLineModal = [];
    creditLineModal = [];
    // operatorLovForConditions = [];
    logicalOperatorLov = [];
    displayDebitConditions = [];
    displayCreditConditions = [];
    showDebitLineCondDetails = [];
    showCreditLineCondDetails = [];
    sourceLOV = [];
    categoryLOV = [];
    debitCreditLineSize: any = 'col-md-6';
    lineSegmentLOVs = [];
    mappingSetsWithValueSet = [];
    accountingPurpose: any = [];
    reconciliationList: any = [];
    accountingList: any = [];
    dataViewList: any = [];
    //reconDataSourceArrays: any = [];
    constructor(
        private route: ActivatedRoute,
        private router: Router,
        public ruleGroupService: RuleGroupService,
        private _sanitizer: DomSanitizer,
        private builder: FormBuilder,
        private lookUpCodeService: LookUpCodeService,
        private dataViewsService: DataViewsService,
        private mappingSetService: MappingSetService,
        private ledgerDefinitionService: LedgerDefinitionService,
        private rulesService: RulesService,
        private dateUtils: JhiDateUtils,
        private chartOfAccountService: ChartOfAccountService,
        private segmentsService: SegmentsService,
        public dialog: MdDialog,
       // private notificationsService: NotificationsService,
        private reconcileService: ReconcileService,
        private commonService: CommonService,
    ) {

    }

    ngOnInit() {
        this.ruleGroupService.submitted = false;
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa fa-angle-down');
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa fa-angle-up');
        //$('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(2)').addClass('cuppa-single-selection-angle');
        this.lookUpCodeService.fetchLookUpsByLookUpType('RECONCILIATION_STATUS_RS_LOV').subscribe((res: any) => {
            this.reconciliationList = res;
            // //console.log(' this.reconciliationList ' + JSON.stringify(this.reconciliationList));
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_STATUS_RS_LOV').subscribe((res: any) => {
            this.accountingList = res;
            // //console.log(' this.accountingList ' + JSON.stringify(this.accountingList));
        });


        const url = this.route.snapshot.url.map((segment)=> segment.path).join('/');
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                if (url.endsWith('edit')) {

                    this.fetchLookUps();
                   if(!this.ruleGroupService.ruleGroup.activityBased){
                    this.fetchDataViews();
                   }
                    //fetchAPI
                    this.fetchAccountingruleGroupByGrpId(params['id'], 'edit', url);
                    
                } else {
                    // //console.log('in view of acc')

                    //fetchAPI
                    this.fetchAccountingruleGroupByGrpId(params['id'], 'view');
                }
            }    else {
                console.log('Accounting Mode : '+this.accountingMode);
                if (this.accountingMode.toString().endsWith('WQ')) {
                    this.ruleCreationInWQ = true;
                    if(this.accountingMode.toString().split('-').length>0)  {
                        const groupId = this.accountingMode.toString().split('-')[0];
                        this.ruleGroupService.ruleGroup.id = groupId;
                        this.fetchAllDetailsForWorkQueue(groupId);
                    }
                   
                } else{
                    this.createForms(0);
                    this.fetchLookUps();
                    // //console.log('in new');
                    this.fetchDataViews();
                    this.isCreateNew = true;
                    this.addNewRuleObject(0, 0);
                }   
            }

        });
    }
    fetchAllDetailsForWorkQueue(groupId) {
         //get rule group info
        this.ruleGroupService.fetchRuleGroupObject(groupId).subscribe((res: any) => {
            this.ruleGroupService.ruleGroup = res;
            console.log(' rule group info while adhoc rule isss'+JSON.stringify( this.ruleGroupService.ruleGroup ));
            this.ruleGroupService.accountingRuleGroup.rules=[];
            this.fetchLookUps();
            //add new rule object
            this.addNewRuleObject(0,0);

              //recon data sources if group is activity based
           
            this.fetchColumnsByDVId(this.ruleGroupService.sourceDataViewId,0);

        });
       
       
      
        //segments based on coa.
        //columns based on accounting data source
        //reconciliation accounting status list
        //ledgers based on coa.
        //lookups for header derivations.
        

    }
    fetchAllLovsToPopulate() {
        if (this.ruleGroupService.accountingRuleGroup && this.ruleGroupService.accountingRuleGroup.rules && this.ruleGroupService.accountingRuleGroup.rules.length > 0) {
            for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {

                this.createForms(c);
               // this.fetchAccountingRuleList(c);
            }

        }

    }
    splitLineDerivations(i) {
     
            this.ruleGroupService.accountingRuleGroup.rules[i].today = this.ruleGroupService.accountingRuleGroup.rules[i].startDate;
            this.showDebitLineCondDetails[i] = [];
            this.showCreditLineCondDetails[i] = [];
            let j = 0;
            let k = 0;
           
            for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules.length; c++) {

                for(let m=0;m< this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].accountingRuleDerivations.length;m++)  {
                    if(this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].accountingRuleDerivations[m].criteria.search('MAPPING') != -1) {
                        this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].accountingRuleDerivations[m].segValue = 
                        +this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].accountingRuleDerivations[m].segValue;
                    }
                }

                if (!this.ruleGroupService.accountingRuleGroup.rules[i].debitLines) {
                    this.ruleGroupService.accountingRuleGroup.rules[i].debitLines = [];
                }
                if (!this.ruleGroupService.accountingRuleGroup.rules[i].creditLines) {
                    this.ruleGroupService.accountingRuleGroup.rules[i].creditLines = [];
                }
                if (this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].lineType == 'DEBIT') {
                    // this.debitSelectedIndex[i][j]=0;
                    this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].lineSelectedIndex = 0;
                    this.showDebitLineCondDetails[i][j] = [];
                                  
                    this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.push(this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c]);
                  
                   
                    j = j + 1;
                }  else {
                    //  this.creditSelectedIndex[i][k]=0;
                    this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c].lineSelectedIndex = 0;
                    this.showCreditLineCondDetails[i][k] = [];
                    this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.push(this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules[c]);                   
                    k = k + 1;
                }

            }
          //  if (this.ruleGroupService.accountingRuleGroup.rules[i])
            //    this.ruleGroupService.accountingRuleGroup.rules[i].formCntrl = new FormControl('');
                this.populateOperatorsInConditions(i);
        
    }
    fetchAccountingruleGroupByGrpId(grpId: any, mode, url?: any) {

        this.ruleGroupService.getAccRuleGroupDetails(grpId).subscribe((res: any) => {
            this.ruleGroupService.accountingRuleGroup = res;
            //let i: number = 0;
            if (this.ruleGroupService.accountingRuleGroup && this.ruleGroupService.accountingRuleGroup.rules && this.ruleGroupService.accountingRuleGroup.rules) {

                this.ruleGroupService.ruleGroup.coa = this.ruleGroupService.accountingRuleGroup.rules[0].coa;
              
                for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++)  {
                      if(!this.ruleGroupService.accountingRuleGroup.rules[c].editRule) {
                        this.ruleGroupService.ruleGroup.editRule=false;
                        break;
                      } else{
                        this.ruleGroupService.ruleGroup.editRule=true;
                      }
                }
                    // this.ruleGroupService.accountingRuleGroup.rules.forEach(rule => {
                if (this.ruleGroupService.accountingRuleGroup.rules) {
                    for (let i = 0; i < this.ruleGroupService.accountingRuleGroup.rules.length; i++)   {
                        this.splitLineDerivations(i);
                    }
                  }
                this.refreshWithCOA(this.ruleGroupService.ruleGroup.coa, 'populate');
                this.ruleGroupService.ruleGroup.coaMeaning = this.ruleGroupService.accountingRuleGroup.rules[0].coaMeaning;
               // if (this.ruleGroupService.accountingRuleGroup.rules[c])
                //this.ruleGroupService.accountingRuleGroup.rules[c].formCntrl = new FormControl('');

                if (mode == 'edit')   {
                    this.isEdit = true;
                    this.fetchAccountingRuleList(this.ruleGroupService.ruleGroup.coa);
                    for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++)  {
                        if (c == 0){
                            this.openRuleTab[c] = true;
                        }  else{
                            this.openRuleTab[c] = false;
                        }
                        const dv = {
                            "id": this.ruleGroupService.accountingRuleGroup.rules[c].sourceDataViewId,
                            "itemName": this.ruleGroupService.accountingRuleGroup.rules[c].sourceDataViewName
                        };
                        this.ruleGroupService.accountingRuleGroup.rules[c].sourceDVId = [];
                        this.ruleGroupService.accountingRuleGroup.rules[c].sourceDVId.push(dv);

                        /******************************************************************************/
                        //check if any process is initiated with this rule and set to true or false
                        //this.ruleGroupService.accountingRuleGroup.rules[c].editRule = true;
                        /******************************************************************************/
                        this.fetchColumnsByDVId(this.ruleGroupService.accountingRuleGroup.rules[c].sourceDataViewId, c);
                        const reconDV = {
                            "id": this.ruleGroupService.accountingRuleGroup.rules[c].reconDataSourceId,
                            "itemName": this.ruleGroupService.accountingRuleGroup.rules[c].reconDataSourceName
                        };
                        this.ruleGroupService.accountingRuleGroup.rules[c].reconSourceDVId = [];
                        this.ruleGroupService.accountingRuleGroup.rules[c].reconSourceDVId.push(reconDV);
                        if(this.ruleGroupService.ruleGroup.activityBased){
                        this.fetchReconDataSources(this.ruleGroupService.accountingRuleGroup.rules[c].sourceDataViewId, c);
                        }
                        this.refreshWithCOA(this.ruleGroupService.accountingRuleGroup.rules[c].coa);
                        for (let j = 0; j < this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules.length; j++) {
                           
                            if (this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules[j].criteria.search('MAPPING') != -1){
                                this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules[j].segValue = +this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules[j].segValue;
                            }
                            //fetch operator lov
                            if (this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules[j].dataViewColumn) {
                                this.SelectColumn(c, j);
                            }
                        }
                       this.populateOperatorsInConditions(c);
                    }
                   // this.fetchAllLovsToPopulate();
                    if (url.search('copy') != -1)  {
                        this.copyRule = true;
                        this.ruleGroupService.ruleGroup.editRule=true;
                        for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {
                            this.ruleGroupService.accountingRuleGroup.rules[c].editRule=true;
                            this.ruleGroupService.accountingRuleGroup.rules[c].ruleCode = null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].ruleName = null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].ruleGroupAssignId = null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].id = null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].editRule = true;
                            this.ruleGroupService.accountingRuleGroup.rules[c].enabledFlag = true;
                            this.ruleGroupService.accountingRuleGroup.rules[c].assignmentFlag = true;
                            this.ruleGroupService.accountingRuleGroup.rules[c].startDate = new Date();
                        }

                    }
                }    else {
                    console.log('mode is view here');
                    this.isViewOnly = true;
                    this.openRuleTab = [];
                    for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {
                       
                        if (c == 0){
                            this.openRuleTab[c] = true;
                        } else{
                            this.openRuleTab[c] = false;
                        }
                    }
                }

            }
        });
    }
    populateOperatorsInConditions(c) {
        if (this.ruleGroupService.accountingRuleGroup.rules[c].creditLines){
            for (let j = 0; j < this.ruleGroupService.accountingRuleGroup.rules[c].creditLines.length; j++) {
                for (let k = 0; k < this.ruleGroupService.accountingRuleGroup.rules[c].creditLines[j].accountingRuleConditions.length; k++) {
                    this.SelectCreditLineConditionColumn(c, j, k, this.ruleGroupService.accountingRuleGroup.rules[c].creditLines[j].accountingRuleConditions[k].dataType,
                        this.ruleGroupService.accountingRuleGroup.rules[c].creditLines[j].accountingRuleConditions[k].sViewColumnName);
    
                }
                for (let k = 0; k < this.ruleGroupService.accountingRuleGroup.rules[c].creditLines[j].accountingRuleDerivations.length; k++) {
                    this.SelectCreditLineLevelColumn(c, j, k);
                }
    
            }
        }
        
    if (this.ruleGroupService.accountingRuleGroup.rules[c].debitLines){
        for (let j = 0; j < this.ruleGroupService.accountingRuleGroup.rules[c].debitLines.length; j++) {
            for (let k = 0; k < this.ruleGroupService.accountingRuleGroup.rules[c].debitLines[j].accountingRuleConditions.length; k++) {

                this.SelectDebitLineConditionColumn(c, j, k, this.ruleGroupService.accountingRuleGroup.rules[c].debitLines[j].accountingRuleConditions[k].dataType,
                    this.ruleGroupService.accountingRuleGroup.rules[c].debitLines[j].accountingRuleConditions[k].sViewColumnName);
            }
            for (let k = 0; k < this.ruleGroupService.accountingRuleGroup.rules[c].creditLines[j].accountingRuleDerivations.length; k++) {
                this.SelectDebitLineLevelColumn(c, j, k);
            }
        }
    }
       
    }
    createForms(ind) {
        let ruleListForm: FormGroup;
        ruleListForm = this.builder.group({
            data: "",
        });
        this.ruleListformArray[ind] = ruleListForm;

        let dvForm: FormGroup;
        dvForm = this.builder.group({
            data: "",
        });
        this.dataViewformArray[ind] = dvForm;
        if (!this.sourceDataViewsArrays){
            this.sourceDataViewsArrays = [];
        }
           
    }
    fetchDataViews() {
        this.sourceDataViewsArrays = [];
        this.dataViewsService.dataViewList().subscribe((res: any) => {
            this.dataViewList = res;
            this.dataViewList.forEach((dv) => {
                dv['itemName'] = dv['dataViewDispName'];
            });
            this.sourceDataViewsArrays = this.dataViewList;
        });
    }
    fetchLookUps() {
        /*this.lookUpCodeService.fetchLookUpsByLookUpType( 'CHART_OF_ACCOUNTS' ).subscribe(( res: any ) => {
            this.chartOfAccounts = res;
        } );*/
        // this.chartOfAccountService.getChartOfAccountsByTenant().subscribe((res: any) => {
        //     this.chartOfAccounts = res;
        // });
        //  //console.log(' this.chartOfAccounts' + JSON.stringify(this.chartOfAccounts));
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_CRITERIA_TYPES').subscribe((res: any) => {
            this.accCriterias = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_LINE_TYPES').subscribe((res: any) => {
            this.linetypes = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACC_HEADER_LINE_TYPES').subscribe((res: any) => {
            this.headerLineTypes =null;
            this.headerLineTypes = res;
            let ind = 0;
            this.ruleGroupService.accountingRuleGroup.rules.forEach((rule) => {
                this.createHeaderForRule(ind);
                ind = ind + 1;
            });

        });
        this.mappingSetService.fetchActiveMappingSetsByTenant().subscribe((res: any) => {
            this.mappingSets = res;
        });
        //this.ledgerDefinitionService.getLedgersByTenant().subscribe((res: any) => {
        //    this.ledgers = res;
        //  //console.log(' this.ledgers ' + JSON.stringify(this.ledgers));
        // });
        this.lookUpCodeService.fetchLookUpsByLookUpType('LOGICAL_OPERATOR').subscribe((res: any) => {
            this.logicalOperatorLov = res;
        });

        this.lookUpCodeService.fetchLookUpsByLookUpType('SOURCE').subscribe((res: any) => {
            this.sourceLOV = res;
        });

        this.lookUpCodeService.fetchLookUpsByLookUpType('CATEGORY').subscribe((res: any) => {
            this.categoryLOV = res;
        });

    }
    autocompleListFormatter = (data: any) => {
        const html = `<span >${data.ruleCode}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    dataViewListFormatter = (data: any) => {
        const html = `<span >${data.dataViewDispName}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    copyAndCreateNewRule(indexToAdd: any, i: any) {
        this.addNewRuleObject(indexToAdd, 0);
        this.populateValues(indexToAdd, i);
    }
    copyRuleHeader(indexToAdd, i) {
       
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].sourceDVId = this.ruleGroupService.accountingRuleGroup.rules[i].sourceDVId;
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].sourceDataViewId = this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId;
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].sourceDataViewName = this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewName;

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].dvColumns = this.ruleGroupService.accountingRuleGroup.rules[i].dvColumns;

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconSourceDVId = this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId;
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconDataSourceName = this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName;
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconDataSourceId = this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId;
        // if(this.ruleGroupService.accountingRuleGroup.rules[i].startDate && this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.accountingRuleGroup.rules[i].startDate ) >= new Date())
        //   {
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].startDate = new Date();
        //   }
        //  else
        //  this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].startDate = new Date();

        //  if(this.ruleGroupService.accountingRuleGroup.rules[i].endDate >= new Date())
        //  {
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].endDate = this.ruleGroupService.accountingRuleGroup.rules[i].endDate;
        // }
        // else
        //this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].endDate = new Date();

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconciliationStatus = this.ruleGroupService.accountingRuleGroup.rules[i].reconciliationStatus;
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].accountingStatus = this.ruleGroupService.accountingRuleGroup.rules[i].accountingStatus;

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconDataSourceArrays = this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays;
        //this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconDataSourceId = this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId;
        //this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].reconDataSourceName = this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName;

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].enterCurrencyColId = this.ruleGroupService.accountingRuleGroup.rules[i].enterCurrencyColId;

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].coa = this.ruleGroupService.accountingRuleGroup.rules[i].coa;


    }
    copyRuleHeaderDerivation(indexToAdd, i) {
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules = JSON.parse(JSON.stringify(
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules));
            for(let j=0;j<this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules.length;j++)  {
                if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules[j].criteria.search('MAPPING') != -1)  {
                  
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules[j].segValue = 
                +this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules[j].segValue;
                }
                

             }
    }
    populateValues(indexToAdd, i) {
        this.copyRuleHeader(indexToAdd, i);
        this.copyRuleHeaderDerivation(indexToAdd, i);
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules = JSON.parse(JSON.stringify(
            this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules));

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines = JSON.parse(JSON.stringify(
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines));

        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines = JSON.parse(JSON.stringify(
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines));
        // this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].ruleCode=null;
        // this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].ruleName=null;
       this.emptyAllIdsAfterCopy(indexToAdd);
        if (this.ruleGroupService.accountingRuleGroup.rules[i].endDate){
            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].endDate =
                this.dateUtils.convertDateTimeFromServer(this.ruleGroupService.accountingRuleGroup.rules[i].endDate);
        }
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].startDate){
            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].startDate = new Date(this.ruleGroupService.accountingRuleGroup.rules[i].startDate);
        }
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].today){
            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].today = new Date(this.ruleGroupService.accountingRuleGroup.rules[i].today);
        }
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].editRule) {

        }  else {
            //fetch all lovs
        }
    }
    addNewRuleObject(indexVal: any, childIndex: any) {
        this.createForms(indexVal);
        const accRule = new AccountingRule();
        accRule.coa = this.ruleGroupService.ruleGroup.coa;
        accRule.enabledFlag=true;
        accRule.assignmentFlag=true;
        accRule.editRule = true;
        accRule.sourceDataViewName=this.ruleGroupService.sourceDataView;
        this.ruleGroupService.accountingRuleGroup.rules.push(accRule);
        this.createHeaderForRule(indexVal);
        
       // this.fetchAccountingRuleList(indexVal);
        this.displayDebitConditions[indexVal] = [];
        if (!this.showDebitLineCondDetails){
            this.showDebitLineCondDetails = [];
        }
           
        this.showDebitLineCondDetails[indexVal] = []; 
       
        if(this.ruleCreationInWQ) {
            this.fetchSegmentsBasedOnCOA();
        }
        if (this.isEdit  ) {
            this.refreshOneRuleWithCoa(indexVal);
        }
       
        if (!this.ruleCreationInWQ && this.ruleGroupService.accountingRuleGroup.rules[indexVal].editRule) {
            this.addNewDebitLine(indexVal, 0);
            this.addNewCreditLine(indexVal, 0);
        }
      //  console.log('===============1');
        this.ngxScrollToLastRule = 'ruleScroll_' + (this.ruleGroupService.accountingRuleGroup.rules.length - 1);
        for (let ind = 0; ind < this.ruleGroupService.accountingRuleGroup.rules.length; ind++) {
            if (ind == indexVal){

                this.openRuleTab[ind] = true;
           
            } else{
                this.openRuleTab[ind] = false;
            }
                
        }
       // console.log('===============2');
       if(this.ruleGroupService.ruleGroup.activityBased && this.ruleCreationInWQ){
           this.fetchReconDataSources(this.ruleGroupService.sourceDataViewId,0);
       }
    }
    scroll(i){
        this.ngxScrollToLastRule = 'ruleScroll_' + (i);
    }   
    fetchSegmentsBasedOnCOA()  {
        this.segmentsService.fetchSegmentsByCOAOrderBySequence(this.ruleGroupService.ruleGroup.coa).subscribe((res: any) => {
            const resp: any = res;
            this.ledgerDefinitionService.getLedgersByTenantAndCoa(this.ruleGroupService.ruleGroup.coa).subscribe((resp1: any) => {
                this.ledgersBasedOnCOA = resp1;
                this.segments = resp.segments;
                if(this.ruleCreationInWQ){
                this.refreshOneRuleWithCoa(0);
            }
            });
        });
    }
    createHeaderForRule(ind: any) {
        if (this.headerLineTypes) {
            // //console.log('header line types exists and are' + JSON.stringify(this.headerLineTypes));
            this.ruleGroupService.accountingRuleGroup.rules[ind].headerDerivationRules=[];
            this.headerLineTypes.forEach((lineType) => {
                // //console.log('loop for ');
                const lineDer = new LineDerivations();
                lineDer.accountingReferencesCode = lineType['lookUpCode'];
                lineDer.accountingReferencesMeaning = lineType['meaning'];
                lineDer.type = 'HEADER';
                this.ruleGroupService.accountingRuleGroup.rules[ind].headerDerivationRules.push(lineDer);
            });
        }else {
            // //console.log('no value present');
        }

    }
    saveAdhocRule() {
        this.ruleGroupService.accountingRuleGroup.rules[0].priority=null;
        this.ruleGroupService.accountingRuleGroup.rules[0].lineDerivationRules=[];
        if(this.ruleGroupService.accountingRuleGroup.rules[0].debitLines){
        this.ruleGroupService.accountingRuleGroup.rules[0].debitLines.forEach((line) => {
            this.ruleGroupService.accountingRuleGroup.rules[0].lineDerivationRules.push(line);
        });
    }
        if(this.ruleGroupService.accountingRuleGroup.rules[0].creditLines){
        this.ruleGroupService.accountingRuleGroup.rules[0].creditLines.forEach((line )=> {
            this.ruleGroupService.accountingRuleGroup.rules[0].lineDerivationRules.push(line);
        });
    }
        this.ruleGroupService.accountingRuleGroup.rules[0].sourceDataViewId= this.ruleGroupService.sourceDataViewId;
        this.ruleGroupService.postAccountingRuleGroup(true).subscribe((res: any) => {
            let savedObj = [];
            savedObj = res;
            this.reconcileService.ENABLE_RULE_BLOCK=false;
            if(savedObj &&  savedObj.length>0 && savedObj[savedObj.length-1] && savedObj[savedObj.length-1].taskStatus && savedObj[savedObj.length-1].taskName ) {
                if( savedObj[savedObj.length-1].taskStatus.search('Failed')!= -1) {
                    this.commonService.error(
                        savedObj[savedObj.length-1].taskName,
                         savedObj[savedObj.length-1].taskStatus 
                    )
                }  else {
                    this.commonService.success(
                        savedObj[savedObj.length-1].taskName,
                         savedObj[savedObj.length-1].taskStatus 
                    )
                   
                }
              //  this.reconcileService.ENABLE_RULE_BLOCK = false;
            }
        },
        (res: Response) => {
            this.reconcileService.ENABLE_RULE_BLOCK=false;
            this.commonService.error('Error Occured', 'Unable to Initiate Job!');
        }
        );
    }
    clearRuleObject(i) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i]) {
            this.ruleGroupService.accountingRuleGroup.rules.splice(i, 1);
            this.addNewRuleObject(i, 0);
        }
    }
    deleteRuleObject(i) {
        if (this.ruleGroupService.accountingRuleGroup.rules.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules.splice(i, 1);
            this.addNewRuleObject(0, 0);
        }else{
            this.ruleGroupService.accountingRuleGroup.rules.splice(i, 1);
        }
        this.ruleListformArray.splice(i, 1);
    }
    startEndDateClass(indexVal: any) {
        if (this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate != null) {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }
    }
   
    emptyAllIdsAfterCopy(indexToAdd) {
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].ruleCode=null;
    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].ruleGroupAssignId = null;
        this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].id = null;
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules){
            for (let c1 = 0; c1 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules.length; c1++) {
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules[c1].id = null;
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].headerDerivationRules[c1].ruleId = null;
            }
        }
           
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines){
            for (let c2 = 0; c2 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines.length; c2++) {
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].id = null;
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].ruleId = null;
                for (let d1 = 0; d1 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].accountingRuleDerivations.length; d1++) {
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].accountingRuleDerivations[d1].id = null;
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].accountingRuleDerivations[d1].ruleId = null;
                }
                for (let d1 = 0; d1 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].accountingRuleConditions.length; d1++) {
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].accountingRuleConditions[d1].id = null;
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[c2].accountingRuleConditions[d1].ruleId = null;
                }
            }
        }
           
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines){
            for (let c3 = 0; c3 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines.length; c3++) {
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].id = null;
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].ruleId = null;
                for (let d1 = 0; d1 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].accountingRuleDerivations.length; d1++) {
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].accountingRuleDerivations[d1].id = null;
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].accountingRuleDerivations[d1].ruleId = null;
                }
                for (let d1 = 0; d1 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].accountingRuleConditions.length; d1++) {
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].accountingRuleConditions[d1].id = null;
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[c3].accountingRuleConditions[d1].ruleId = null;
                }
            }
        }
           
        if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules){
            for (let c4 = 0; c4 < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules.length; c4++) {
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].id = null;
                for (let d = 0; d < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].accountingRuleDerivations.length; d++) {
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].accountingRuleDerivations[d].id = null;
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].accountingRuleDerivations[d].ruleId = null;
                }
                for (let d = 0; d < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].accountingRuleConditions.length; d++) {
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].accountingRuleConditions[d].id = null;
                    this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].lineDerivationRules[c4].accountingRuleConditions[d].ruleId = null;
                }
            }
        }
           
        this.openRuleTab = [];
        for (let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {
            //if(c==indexToAdd)
            // this.openRuleTab[c] =true;
            // else
            this.openRuleTab[c] = false;
        }
        this.openRuleTab[indexToAdd] = true;
    }
    refreshSpecifiedRuleWithCOA(indexToAdd)  {
        console.log('this.ruleGroupService.ruleGroup.coa'+this.ruleGroupService.ruleGroup.coa);
        this.segmentsService.fetchSegmentsByCOAOrderBySequence(this.ruleGroupService.ruleGroup.coa).subscribe((res: any) => {
            const resp: any = res;
            // 
            this.ledgerDefinitionService.getLedgersByTenantAndCoa(this.ruleGroupService.ruleGroup.coa).subscribe((resp1: any) => {
                this.ledgersBasedOnCOA = resp1;
                this.segments = resp.segments;
                this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].coa = this.ruleGroupService.ruleGroup.coa;
                //set value set for all lines
                if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines){

                    for (let cl = 0; cl < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines.length; cl++) {
                        for (let seg = 0; seg < this.segments.length; seg++) {
                            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[cl].accountingRuleDerivations[seg].accountingReferencesMeaning=
                            this.segments[seg].segmentName;
                            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].creditLines[cl].accountingRuleDerivations[seg].valueSet = this.segments[seg].valueSet;
                        }
                    }
                }

                if (this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines){ 
                    for (let cl = 0; cl < this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines.length; cl++) {
                        for (let seg = 0; seg < this.segments.length; seg++) {
                            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[cl].accountingRuleDerivations[seg].accountingReferencesMeaning=
                            this.segments[seg].segmentName;
                            this.ruleGroupService.accountingRuleGroup.rules[indexToAdd].debitLines[cl].accountingRuleDerivations[seg].valueSet = this.segments[seg].valueSet;
                        }
                    }
                }
                    
                   // console.log('this.ruleGroupService.accountingRuleGroup.rules[i] after refresh '+JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[indexToAdd]));
            });
        });
      
    }
    copyContent(value,indexVal,ruleObj){

          
            //this.ruleGroupService.accountingRuleGroup.rules[indexVal] = ruleObj;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].id = null;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].formCntrl = new FormControl('');
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].assignmentFlag = true;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].ruleGroupAssignId=null;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].editRule=true; 
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewId= ruleObj.sourceDataViewId;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewName= ruleObj.sourceDataViewName;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconDataSourceId= ruleObj.reconDataSourceId;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconDataSourceName= ruleObj.reconDataSourceName;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].coa=this.ruleGroupService.ruleGroup.coa;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].enterCurrencyColId= ruleObj.enterCurrencyColId;
            
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].enterCurrencyColName= ruleObj.enterCurrencyColName;
          
           // this.ruleGroupService.accountingRuleGroup.rules[indexVal].endDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.accountingRuleGroup.rules[indexVal].endDate);
           // this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate);
            const dv = {
                "id":this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewId,
                 "itemName": this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewName
                 };
                
                 this.fetchColumnsByDVId(this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewId,indexVal);
                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDVId = [];
                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDVId.push(dv);
                //this.setDataview(dv,indexVal);
                 if(this.ruleGroupService.ruleGroup.activityBased && this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconSourceDVId)  {
                    this.fetchReconDataSources(this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewId, indexVal);
                    const reconDV = {
                        "id":this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconDataSourceId,
                         "itemName": this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconDataSourceName
                         };
                         this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconSourceDVId = [];
                         this.ruleGroupService.accountingRuleGroup.rules[indexVal].reconSourceDVId.push(dv);
                 }
                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules = ruleObj.headerDerivationRules;
                 for(let j=0;j<this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules.length;j++)  {
                     
                    if (this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules[j].criteria.search('MAPPING') != -1)  {
                    //    console.log('the value of '+this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules[j].accountingReferencesMeaning
                    //     +'=>'+this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules[j].criteria
                    // +' is '+this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules[j].segValue);
                        this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules[j].segValue = 
                    +this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules[j].segValue;
                    }
                    

                 }
                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].lineDerivationRules=ruleObj.lineDerivationRules;
                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines=[];
                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines=[];
                 this.splitLineDerivations(indexVal);
                 this.refreshSpecifiedRuleWithCOA(indexVal);
               
                 this.emptyAllIdsAfterCopy(indexVal);
       
    }
    setRuleObject(indexVal: any, value: any) {
        // //console.log('value.data' + JSON.stringify(value.data));
        //console.log('setRuleObject - ruleName ' + ruleName);
        //console.log('setRuleObject - value ' + JSON.stringify(value));
       // if (value && value.data && value.data.id)
       if(value){

        this.rulesService.fetchExistingAccountingRuleDetails(value).subscribe((res: any) =>   {
            const ruleObj: any = res;
           // console.log('ruleObj' + JSON.stringify(ruleObj));
            if(this.ruleGroupService.ruleGroup.activityBased) {
                if (this.ruleGroupService.ruleGroup.reconciliationGroupId) {
                   
            //check if data views matched
            let matchFound=false;
            for(let c=0;c<this.sourceDataViewsArrays.length;c++)   {
                if(this.sourceDataViewsArrays[c].id === ruleObj.sourceDataViewId)  {
                    this.ruleGroupService.accountingRuleGroup.selectedExistingRule=null;
                    this.ruleGroupService.accountingRuleGroup.selectedExistingRule =  jQuery.extend( true, {},this.ruleGroupService.accountingRuleGroup.rules[indexVal].copiedRefId[0]);
                    matchFound= true;
                    break;
                }
                
            }
            if(matchFound) {
                this.copyContent(value,indexVal,ruleObj);
            } else{
                let data: any = {};
                data = {
                    copyRule : 'Copy Rule',
                    ok :'ok',
                    
                };
                const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
                    data, disableClose: true
                });
                dialogRef.afterClosed().subscribe((result) => {
                    if( this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewId){
                        this.ruleGroupService.accountingRuleGroup.rules[indexVal].copiedRefId =[];
                        this.ruleGroupService.accountingRuleGroup.rules[indexVal].copiedRefId.push(this.ruleGroupService.accountingRuleGroup.selectedExistingRule);    
                    }else{
                    this.ruleGroupService.accountingRuleGroup.rules[indexVal].copiedRefId =[];   
                    }
                    if (result && result == 'Copy Rule') {
                      
                    }
                });
            }
                  


                }   else{
                    this.copyContent(value,indexVal,ruleObj);
                }
            }  else{
                this.copyContent(value,indexVal,ruleObj);
            }
        });
           
       
       }
            
        //set 
        // if (value.data && value.data.ruleCode){
        //     this.dispViewField = false;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].ruleCode = value.data.ruleCode;
        // }
        // if (value.data && value.data.id) {
        //     this.dispViewField = true;

        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].id = value.data.id;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].enterCurrencyColId = value.data.enterCurrencyColId;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewId = value.data.sourceDataViewId;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].sourceDataViewName = value.data.sourceDataViewName;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].enterCurrencyColName = value.data.enterCurrencyColName;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].coa = value.data.coa;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].coaMeaning = value.data.coaMeaning;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate =
        //         this.dateUtils.convertLocalDateFromServer(value.data.startDate);
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].endDate =
        //         this.dateUtils.convertLocalDateFromServer(value.data.endDate);
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].coa = value.data.coa;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].headerDerivationRules = value.data.headerDerivationRules;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].lineDerivationRules = value.data.lineDerivationRules;
        //     this.ruleGroupService.accountingRuleGroup.rules[indexVal].lineDerivationRules.forEach(line => {
        //         if (line.lineType == 'DEBIT') {
        //             if (!this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines)
        //                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines = [];
        //             this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines.push(line);
        //         }
        //         else if (line.lineType == 'CREDIT') {
        //             if (!this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines)
        //                 this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines = [];
        //             this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines.push(line);
        //         }
        //     });

        // }
        // else {
        // }

    }
    // ChangeDataView(dvName: any, i: any, value: any) {
    //     let matchedDV = {};
    //     let loopVariable = 0;
    //     let matched: boolean = false;
    //     if (this.sourceDataViewsArrays[i]) {
    //         let dataView: any = {};
    //         this.sourceDataViewsArrays[i].forEach(dv => {
    //             if (dvName === dv.dataViewDispName) {
    //                 dataView = dv;
    //                 matched = true;
    //             }
    //             loopVariable = loopVariable + 1;

    //         });
    //         if (matched) {

    //             matchedDV = { "data": dataView };
    //             this.setDataview(i, matchedDV);
    //         }
    //     }
    //     else {
    //     }

    // }
    setDataview(view: any, i: any) {
        //console.log('set dv called');
        if (!this.ruleCreationInWQ) {
            //console.log('selected dv after dialog has'+JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId));
            $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden', "true");
            const selectedDv = {
                "id": this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId,
                "itemName": this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewName
            };
            if (this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId) {
                /*******************************************************************************/
                /*
                * Reset all the dependencies
                * Recon data source, entered currecny, reconciliation status, accounting status, header derivation rules, delete all line derivation rules. 
                */
                /*******************************************************************************/
                const data = {
                    refreshDataViews: 'refreshDataViews',
                    message: 'Header and lines derivations gets erased. Are you sure to continue ? ',
                    ok: 'ok',
                    no: 'No Thanks'
                }
                const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
                    data, disableClose: true
                });
                dialogRef.afterClosed().subscribe((result) => {
                    if (result && result == 'ok') {
                        //erase all dependencies
                        this.ruleGroupService.accountingRuleGroup.rules[i].enterCurrencyColId = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].enterCurrencyColName = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconciliationStatus = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].accountingStatus = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules = [];
                        this.ruleGroupService.accountingRuleGroup.rules[i].lineDerivationRules = [];
                        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines = [];
                        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines = [];
                        this.createHeaderForRule(i);
                        this.addNewDebitLine(i, 0);
                        this.addNewCreditLine(i, 0);
                        this.setDv(i, view);
                        // this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays = [];
                        if (this.ruleGroupService.ruleGroup.activityBased == true) {
                            this.fetchReconDataSources(view.id, i);

                        }
                    }  else {
                        //console.log('selected dv after dialog has'+JSON.stringify(selectedDv));
                        this.ruleGroupService.accountingRuleGroup.rules[i].sourceDVId = [];
                        this.ruleGroupService.accountingRuleGroup.rules[i].sourceDVId.push(selectedDv);
                        this.setDv(i, selectedDv);
                    }
                });
            }  else {
                this.setDv(i, view);
                this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = null;
                this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays = [];
                if (this.ruleGroupService.ruleGroup.activityBased == true){
                    this.fetchReconDataSources(view.id, i);
                }
                
            }

        }


        // if (value.data.id) {
        //     this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId = value.data.id;
        //     if (value.data.dataViewDispName)
        //         this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewName = value.data.dataViewDispName;
        // }
        // else {

        // }

        // if (value.data && value.data.id) {
        //     this.fetchColumnsByDVId(value.data.id, i);
        // }


    }
    setDv(i, view) {
        this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId = view.id;
        this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewName = view.itemName;

        this.fetchColumnsByDVId(view.id, i);
    }
    fetchReconDataSources(id, i) {
        this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource(this.ruleGroupService.ruleGroup.reconciliationGroupId, id).subscribe((res: any) => {
            this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays = res;
            //console.log('reconDataSourceArrays length is '+i+' '+ this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays.length );
            if (this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays.length > 0) {
                if (this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays.length == 1 && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays[0] && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays['failed']) {
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays = [];
                    this.commonService.info(
                        'Eligible data sets not found.',
                        ' '
                    )
                }  else    {
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays.forEach((dv) => {
                        dv['itemName'] = dv['DataViewDispName'];
                    });
                }
                   
            }  else {
                this.commonService.info(
                    'Eligible data sets not found',
                    ' '
                )
            }

        });
    }
    setReconDataSource(view: any, i: any) {
        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId = view.id;
        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName = view.itemName;
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden', "true");
    }
    fetchColumnsByDVId(id, i) {
        this.dataViewsService.getDataViewById(id).subscribe((res: any) => {
            const dvAndCols = res;
            //if (dvAndCols[0] && dvAndCols[0].dvColumnsList)
             {
                this.columnsByViewId = dvAndCols;
                if (!this.ruleGroupService.accountingRuleGroup.rules[i].dvColumns){
                    this.ruleGroupService.accountingRuleGroup.rules[i].dvColumns = [];
                }
                    

                this.ruleGroupService.accountingRuleGroup.rules[i].dvColumns = this.columnsByViewId;
            }

        });

    }
    refreshWithCOA(coaId, mode?: any) {
        this.segmentsService.fetchSegmentsByCOAOrderBySequence(coaId).subscribe((res: any) => {
            const resp: any = res;
            // 
            this.ledgerDefinitionService.getLedgersByTenantAndCoa(coaId).subscribe((resp1: any) => {
                this.ledgersBasedOnCOA = resp1;
                this.segments = resp.segments;
                let ind= 0;
                // //console.log('fetched segments are:' + JSON.stringify(this.segments));
                if (this.ruleGroupService.accountingRuleGroup.rules){
                    this.ruleGroupService.accountingRuleGroup.rules.forEach((rule) => {
                        if (mode == 'create') {
                            this.addNewDebitLine(ind, 0);
                            this.addNewCreditLine(ind, 0);
                        } else {
                            rule.coa = coaId;
                            //set value set for all lines
                            if (rule.creditLines){

                                for (let cl = 0; cl < rule.creditLines.length; cl++) {
                                    for (let seg = 0; seg < this.segments.length; seg++) {
                                        if(this.segments[seg] && this.segments[seg].valueSet){
                                            rule.creditLines[cl].accountingRuleDerivations[seg].valueSet = this.segments[seg].valueSet;
                                        }
                                        
                                    }
                                }
                            }
                            if (rule.debitLines){
                                for (let cl = 0; cl < rule.debitLines.length; cl++) {
                                    for (let seg = 0; seg < this.segments.length; seg++) {
                                        if(this.segments[seg] && this.segments[seg].valueSet){
                                            rule.debitLines[cl].accountingRuleDerivations[seg].valueSet = this.segments[seg].valueSet;
                                        }
                                        
                                    }
                                }
                            }
                               

                        }


                        //filter ledgers

                        ind = ind + 1;
                    });
                }
                    
            });


        });
        /*this.lookUpCodeService.fetchLookUpsByLookUpType( this.ruleGroupService.accountingRuleGroup.rules[indexVal].coa ).subscribe(( res: any ) => {
            this.segments = res;
        });*/
    }
    refreshOneRuleWithCoa(indexVal) {
        this.addNewDebitLine(indexVal, 0);
        this.addNewCreditLine(indexVal, 0);
    }
    SelectCOA(coaId) {


    }

    addNewCreditLine(indexVal: any, ind: any) {
        const newLine = new AccountingLine();
        newLine.lineType = 'CREDIT';
        // if(this.creditSelectedIndex &&  this.creditSelectedIndex[indexVal])
        // this.creditSelectedIndex[indexVal][ind]=0;
        // if(!newLine.accountingRuleDerivations)
        newLine.accountingRuleDerivations = [];
        this.segments.forEach((segment) => {
            const lineDerivation = new LineDerivations();
            lineDerivation.accountingReferencesCode = segment.id;
            lineDerivation.accountingReferencesMeaning = segment.segmentName;
            newLine.accountingRuleDerivations.push(lineDerivation);
            //fetch value set
            if (segment.valueSet){
                lineDerivation.valueSet = segment.valueSet;
            }
              
        })
        if (newLine.accountingRuleDerivations.length > 0){
            newLine.accountingRuleDerivations.forEach((derivation) => {
                derivation.type = 'LINE';
            });
        }
          
        this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines[ind] = newLine;
        this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines[ind].lineSelectedIndex = 0;
        if (!this.displayCreditConditions){
            this.displayCreditConditions = [];
        }
         
        if (!this.displayCreditConditions[indexVal]){
            this.displayCreditConditions[indexVal] = [];
        }
            
        if (this.displayCreditConditions[indexVal][ind]){
            this.displayCreditConditions[indexVal][ind] = [];
        }
            
    }

    addNewDebitLine(indexVal: any, ind: any) {
        const newLine = new AccountingLine();
        newLine.lineType = 'DEBIT';
        newLine.accountingRuleDerivations = [];
        if (this.segments && this.segments.length > 0) {
            let i= 0;
            this.segments.forEach((segment) => {
                const lineDerivation = new LineDerivations();
                lineDerivation.accountingReferencesCode = segment.id;
                lineDerivation.accountingReferencesMeaning = segment.segmentName;
                newLine.accountingRuleDerivations.push(lineDerivation);
                //fetch value set
                if (segment.valueSet){
                    lineDerivation.valueSet = segment.valueSet;
                }
 


                i = i + 1;
            })
            if (newLine.accountingRuleDerivations.length > 0){
                newLine.accountingRuleDerivations.forEach((derivation) => {
                    derivation.type = 'LINE';
                });
            }
              
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines[ind] = newLine;
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines[ind].lineSelectedIndex = 0;
            if (!this.displayDebitConditions){
                this.displayDebitConditions = [];
            }   
            if (!this.displayDebitConditions[indexVal]){
                this.displayDebitConditions[indexVal] = [];
            }
                
            if (!this.displayDebitConditions[indexVal][ind]){
                this.displayDebitConditions[indexVal][ind] = [];
            }
                
        }

    }
    SelectDebitLineLevelColumn(i, j, drd) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataType).subscribe((res: any) => {
                const operatorss = res;
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorsLOV = operatorss;
            });

        } else {
            // //console.log('no value set for line dt');
        }


    }
    SelectCreditLineLevelColumn(i, j, drd) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataType).subscribe((res: any) => {
                const operatorss = res;
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].operatorsLOV = operatorss;
            });

        }  else {
            // //console.log('no value set for line dt');
        }


    }
    SelectColumn(i, h) {
        // //console.log('this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h]' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h]));
        if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataType).subscribe((res: any) => {
                const operatorss = res;
                //if (!this.operators)
                //   this.operators = [];
                // if (!this.operators[i])
                //    this.operators[i] = [];
                // if (!this.operators[i][h])
                //     this.operators[i][h] = [];
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].operators = operatorss;
            });

        }   else {
            ////console.log('no value set for dt');
        }
    }
    selectHeaderCriteria(i, h) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].criteria == 'CONSTANT') {
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataViewColumn = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataViewColumnName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].mappingSetName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataType = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].operator = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].operatorMeaning = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].value = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].segValue = null;

        } else if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].criteria == 'VIEW_COLUMN') {
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].mappingSetName = null;
        }else if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].criteria == 'MAPPING_SET') {
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataViewColumn = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataViewColumnName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataType = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].operator = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].operatorMeaning = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].value = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].segValue = null;
        }
    }
    selectDebitCriteria(i, j, drd) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].criteria == 'CONSTANT') {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataViewColumn = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataViewColumnName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].mappingSetName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataType = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operator = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorMeaning = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].value = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].segValue = null;

        } else if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].criteria == 'VIEW_COLUMN') {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].mappingSetName = null;
        } else if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].criteria == 'MAPPING_SET') {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataViewColumn = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataViewColumnName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataType = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operator = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorMeaning = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].value = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].segValue = null;
        }
    }
    selectCreditCriteria(i, j, drd) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].criteria == 'CONSTANT') {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataViewColumn = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataViewColumnName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].mappingSetName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataType = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].operator = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].operatorMeaning = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].value = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].segValue = null;

        }   else if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].criteria == 'VIEW_COLUMN') {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].mappingSetName = null;
        }   else if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].criteria == 'MAPPING_SET') {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataViewColumn = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataViewColumnName = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataType = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].operator = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].operatorMeaning = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].value = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].segValue = null;
        }
    }
    fetchAccountingRuleList(coa) {
        this.rulesService.getAccountingRulesList('ACCOUNTING',coa,this.ruleGroupService.ruleGroup.activityBased).subscribe((res: any) => {
            let rulesFetched: any = [];
            rulesFetched = res;
            rulesFetched.forEach((rule) => {
                rule['itemName']=rule.ruleCode;
            });
            this.ruleGroupService.accountingRuleGroup.existingRuleListLOV = rulesFetched;
            //this.filteredRuleList = [];
           // if (rulesFetched)
             //  rulesFetched.forEach(rule => {
              //      this.filteredRuleList.push(rule);
              //  });
         //   this.ruleListArrays[indexVal] = [];
          //  this.ruleListArrays[indexVal] = this.filteredRuleList;
        });


    }
    addDebitLineCondition(i, j) {
        if (!this.debitLineModal){
            this.debitLineModal = [];
        }
            
        if (!this.debitLineModal[i]){
            this.debitLineModal[i] = [];
        }
          
        if (!this.debitLineModal[i][j]){
            this.debitLineModal[i][j] = [];
        }
            
        this.debitLineModal[i][j] = true;
    }
    addCreditLineCondition(i, j) {
        if (!this.creditLineModal){
            this.creditLineModal = [];
        }
            
        if (!this.creditLineModal[i]){
            this.creditLineModal[i] = [];
        }
            
        if (!this.creditLineModal[i][j]){
            this.creditLineModal[i][j] = [];
        }
            
        this.creditLineModal[i][j] = true;
    }
    SelectDebitLineConditionColumn(i, j, rc, datatype, sViewColName) {
        // //console.log('dvColumns[i] ' + JSON.stringify(this.dvColumns[i]));
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j]) {

            const linederivations = new AccountingLine();

            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j] = linederivations;

            const accRuleConditions = new AccountingRuleConditions();
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc] = accRuleConditions;

        }
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].sViewColumnName = sViewColName;
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].dataType = datatype;
        // //console.log(' this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j]' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j]));
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].dataType).subscribe((res: any) => {
                const operatorss = res;
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].operatorList = operatorss;
                // if (!this.operatorLovForConditions)
                //     this.operatorLovForConditions = [];
                // if (!this.operatorLovForConditions[i])
                //     this.operatorLovForConditions[i] = [];
                // if (!this.operatorLovForConditions[i][j])
                //     this.operatorLovForConditions[i][j] = [];
                // if (!this.operatorLovForConditions[i][j][rc])
                //     this.operatorLovForConditions[i][j][rc] = [];
                // this.operatorLovForConditions[i][j][rc] = operatorss;
            });

        }
    }
    SelectCreditLineConditionColumn(i, j, rc, datatype, sViewColName) {
        ////console.log('dvColumns[i] ' + JSON.stringify(this.dvColumns[i]));
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j]) {

            const linederivations = new AccountingLine();

            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j] = linederivations;

            const accRuleConditions = new AccountingRuleConditions();
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc] = accRuleConditions;

        }
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].sViewColumnName = sViewColName;
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].dataType = datatype;
        ////console.log(' this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j]' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j]));
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].dataType).subscribe((res: any) => {
                const operatorss = res;
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].operatorList = operatorss;
                // if (!this.operatorLovForConditions)
                //     this.operatorLovForConditions = [];
                // if (!this.operatorLovForConditions[i])
                //     this.operatorLovForConditions[i] = [];
                // if (!this.operatorLovForConditions[i][j])
                //     this.operatorLovForConditions[i][j] = [];
                // if (!this.operatorLovForConditions[i][j][rc])
                //     this.operatorLovForConditions[i][j][rc] = [];
                // this.operatorLovForConditions[i][j][rc] = operatorss;
            });

        }
    }
    addMappingCriteriaForDebitLines(i, j, rcIndex) {
        const accRuleCondition = new AccountingRuleConditions();
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions){
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions = [];
        }
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.push(accRuleCondition);
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rcIndex - 1] && this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rcIndex - 1].logicalOperator){
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rcIndex - 1].logicalOperator = 'AND';
        }
            
        ////console.log('  this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions));
        if (!this.showDebitLineCondDetails[i][j]){
            this.showDebitLineCondDetails[i][j] = false;
        }
            
    }
    addMappingCriteriaForCreditLines(i, j, rcIndex) {
        const accRuleCondition = new AccountingRuleConditions();
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions){
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions = [];
        }
            
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.push(accRuleCondition);
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rcIndex - 1] && this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rcIndex - 1].logicalOperator){
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rcIndex - 1].logicalOperator = 'AND';
        }
            
        // //console.log('  this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions));
    }
    deleteDebitRuleCondition(i, j, rc) {
        // if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.length == 1) {
        //     this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.splice(rc, 1);
        //     this.addMappingCriteriaForDebitLines(i, 0, 0);
        // }

        // else
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.splice(rc, 1);

    }
    deleteCreditRuleCondition(i, j, rc) {
        // if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.length == 1) {
        //     this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.splice(rc, 1);
        //     this.addMappingCriteriaForCreditLines(i, 0, 0);
        // }

        // else
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.splice(rc, 1);
    }
    displayDRC(i, j) {
        // //console.log('ij is' + i + '-' + j);
        if (!this.displayDebitConditions){
            this.displayDebitConditions = [];
        }
            
        if (!this.displayDebitConditions[i]){
            this.displayDebitConditions[i] = [];
        }
          
        if (!this.displayDebitConditions[i][j]){
            this.displayDebitConditions[i][j] = [];
        }
            
        this.displayDebitConditions[i][j] = true;
    }

    displayCRC(i, j) {
        if (!this.displayCreditConditions){
            this.displayCreditConditions = [];
        }
            
        if (!this.displayCreditConditions[i]){
            this.displayCreditConditions[i] = [];
        }
            
        if (!this.displayCreditConditions[i][j]){
            this.displayCreditConditions[i][j] = [];
        }
            
        this.displayCreditConditions[i][j] = true;
    }
    changeView(i) {
        if (this.debitCreditLineSize == 'col-md-12'){
            this.debitCreditLineSize = 'col-md-6';
        }  else if (this.debitCreditLineSize == 'col-md-6'){
            this.debitCreditLineSize = 'col-md-12';
        }
            
    }
    deleteDebitLineObject(i, j) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.splice(j, 1);
            this.addNewDebitLine(i, 0);
        }      else{
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.splice(j, 1);
        }
            
        /* if(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.length == 0 &&  this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.length == 0)
             this.addNewDebitLine(i, 0);*/
    }
    deleteCreditLineObject(i, k) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.splice(k, 1);
            this.addNewCreditLine(i, 0);
        }else{
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.splice(k, 1);
        }
            
        /*if(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.length == 0 &&  this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.length == 0)
            this.addNewCreditLine(i, 0);*/
    }

    getDataView(val: any, ind: any) {
        //this.ruleObj
        this.fetchDVBasedOnRuleGroup(ind, this.ruleGroupService.ruleGroup.reconciliationGroupId);
        //console.log('val ' + val);
        /* if(val == 'RECONCILIED' || val == 'NOT_RECONCILIED'){
            //console.log('ruleGrpId' + this.ruleGroupService.ruleGroup.reconciliationGroupId);
            this.fetchDVBasedOnRuleGroup(ind,this.ruleGroupService.ruleGroup.reconciliationGroupId);
        }else{
            this.dataViewsService.dataViewList().subscribe((res: any) => {
                this.dataViewList = res;
                this.sourceDataViewsArrays[ind] = this.dataViewList;
            });
        } */
    }

    /* Function to fetch dataview list based on rulegroupid */
    fetchDVBasedOnRuleGroup(ind, rGrpId) {
        //console.log('ind ' + ind + ' rgp ' + rGrpId);
        this.ruleGroupService.fetchingDataViewList(rGrpId).subscribe((res: any) => {
            //console.log('res ' + JSON.stringify(res));
            this.sourceDataViewsArrays = res;
        });
    }
    validateForm() {
        //console.log('accRuleForm validity' + this.accRuleForm.valid);

    }
    addHeaderFilter(i, headerInd) {
        let operatorMeaning: any = '';
        if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operatorMeaning){
            operatorMeaning = this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operatorMeaning;
        }
            
        const data = {
            addFilter: 'addFilter',
            operator: this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operator,
            operatorMeaning: operatorMeaning,
            ok: 'ok',
            value: this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].value,
            operatorsList: this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operators,
        }
        const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operator = data.operator;
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].value = data.value;
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operatorMeaning = data.operatorMeaning;
            }
        });
    }
    addDebitLineFilter(i, j, drd) {
        let operatorMeaning: any = '';
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorMeaning){
            operatorMeaning = this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorMeaning;
        }
            
        const data = {
            addFilter: 'addFilter',
            operator: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operator,
            operatorMeaning: operatorMeaning,
            ok: 'ok',
            value: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].value,
            operatorsList: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorsLOV,
        }
        const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operator = data.operator;
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].value = data.value;
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].operatorMeaning = data.operatorMeaning;
            }
        });
    }
    addCreditLineFilter(i, k, crd) {
        let operatorMeaning: any = '';
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].operatorMeaning){
            operatorMeaning = this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].operatorMeaning;
        }
            
        const data = {
            addFilter: 'addFilter',
            operator: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].operator,
            operatorMeaning: operatorMeaning,
            ok: 'ok',
            value: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].value,
            operatorsList: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].operatorsLOV,
        }
        const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].operator = data.operator;
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].value = data.value;
                this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[k].accountingRuleDerivations[crd].operatorMeaning = data.operatorMeaning;
            }
        });
    }
    openDebitLineDialog(i, j) {
        const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data: {
                addCondition: 'addCondition',
                ruleName: this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode,
                i: i,
                j: j,
                dvColumns: this.ruleGroupService.accountingRuleGroup.rules[i].dvColumns
            }, disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'ok') {
            }
        });
    }
    checkIfRuleCodeisUndefined(i) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode == undefined) {
            //console.log('rule code is undefined');
            this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode = '';
        }
    }

    filterReconDataSources(dataViewId) {

    }
    onOpen(event) {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden', "false");
    }
    copyConditions(i, k) {
        let data: any = {};
        data = {
            copyLineItem: 'copyLineItem',
            debitLines: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines,
            copy: 'copy',
            skipCopy: 'skipCopy',
            copyItemIndex: -1
        }
        const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'copy') {
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[k].accountingRuleConditions = JSON.parse(JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[data.copyItemIndex].accountingRuleConditions));

            }
        });
    }
    copyLineItem(i, k) {
        let data: any = {};
        data = {
            copyLineItem: 'copyLineItem',
            debitLines: this.ruleGroupService.accountingRuleGroup.rules[i].debitLines,
            copy: 'copy',
            skipCopy: 'skipCopy',
            copyItemIndex: -1
        }
        const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result && result == 'copy') {
                //  //console.log('data has now'+JSON.stringify(data));
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[k] = JSON.parse(JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[data.copyItemIndex]));
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[k].lineType = 'CREDIT';
            }
        });

        // //console.log('this.ruleGroupService.accountingRuleGroup.rules[i].creditLines'+JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines));
    }
    startDateChanged(dt: Date, i) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].endDate) {
            if (this.ruleGroupService.accountingRuleGroup.rules[i].endDate < this.ruleGroupService.accountingRuleGroup.rules[i].startDate) {
                this.ruleGroupService.accountingRuleGroup.rules[i].endDate = this.ruleGroupService.accountingRuleGroup.rules[i].startDate;
            }
        }
    }
    endDateChanged(i) {
        if (this.isCreateNew) {
            // if (this.endDateChange) {
            //     this.ruleGroupService.accountingRuleGroup.rules[i].endDate = new Date(this.ruleGroupService.accountingRuleGroup.rules[i].endDate.getTime() + 86400000);
            // }
        }
    }
    switchDebitTabs(event, i, j) {
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].lineSelectedIndex = event.index;
        // this.debitSelectedIndex[i][j]=event.index;
    }

    switchCreditTabs(event, i, j) {
        // this.creditSelectedIndex[i][j]=event.index;
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].lineSelectedIndex = event.index;
    }
    clearTaggedRuleObject(i)  {

    }
    getExistingRuleDetailsAndCopy(ruleId,i)  {
        console.log('index has '+i+' id is '+ruleId);
            this.setRuleObject(i,ruleId.id);
            
            $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
      
        
    }
    checkduplicate(i){
        for(let c =0;c<this.ruleGroupService.accountingRuleGroup.rules.length;c++)  {
            if(c != i)  {
                if(this.ruleGroupService.accountingRuleGroup.rules[c].ruleCode.toLowerCase() === this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode.toLowerCase() ){
                    this.ruleGroupService.accountingRuleGroup.rules[i].duplicateRuleName = true;
                }else{
                    this.ruleGroupService.accountingRuleGroup.rules[i].duplicateRuleName = false;
                }

            }
        }
    }
    ruleDuplicationCheck(i)  {
        this.ruleGroupService.ruleDuplicationCheck( this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode,this.ruleGroupService.ruleGroup.rulePurpose).subscribe((res: any) => {
            const resp = res;
            if(resp  && (resp._body == true || resp._body == 'true')) {
                this.ruleGroupService.accountingRuleGroup.rules[i].duplicateRuleName = true;
            }  else  {
                this.ruleGroupService.accountingRuleGroup.rules[i].duplicateRuleName = false;
            }
            this.checkduplicate(i);
        });
    }
    expandAll(){
        for(let c =0;c<this.openRuleTab.length;c++)  {
            this.openRuleTab[c]=true;
        }
    }
    collapseAll(){
        for(let c =0;c<this.openRuleTab.length;c++)  {
            this.openRuleTab[c]=false;
        }
    }

    // ngOnDestroy() {
    //     this.notificationsService.remove();
    // }
}
