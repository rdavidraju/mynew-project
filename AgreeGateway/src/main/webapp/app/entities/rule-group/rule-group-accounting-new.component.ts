import { Component, Input, OnInit, OnDestroy, ViewChild, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router, NavigationEnd } from '@angular/router';
import { RuleGroupService } from './rule-group.service';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { FormGroup, FormBuilder, FormArray, Validators, NgForm } from '@angular/forms';
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
import { NotificationsService } from 'angular2-notifications-lite';
import * as $ from "jquery";

declare var jQuery: any;
const URL = '';



@Component({
    selector: 'jhi-rule-group-accounting-new',
    templateUrl: './rule-group-accounting-new.component.html'
})
export class RuleGroupAccountingNewComponent {
    today=new Date();  
    startDateChange: boolean = false;
    endDateChange: boolean = false;  
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
    @Input() accountingMode: string = '';
    @Input() validateRuleForm: Function;
    routeSub: any;
    ruleCreationInWQ: boolean = false;

    isViewOnly: boolean = false;
    isCreateNew: boolean = false;
    isEdit: boolean = false;

    mouseOverRule: any;

    ruleListformArray: FormGroup[] = [];
    ruleListArrays: any[] = [];

    filteredRuleList: any[] = [];

    dataViewformArray: FormGroup[] = [];
    sourceDataViewsArrays: any = [];

    dvColumns: any[] = [];

    mouseOverLineItem: any;

    chartOfAccounts = [];

    lineTypeDetails: any[] = [];
    columnsByViewId = [];
    segments = [];
    linetypes = [];
    accCriterias = [];
    headerLineTypes: Observable<Response>;
    operators = [];
    debitLineLevelOperatorsLOV = [];
    creditLineLevelOperatorsLOV = [];
    mappingSets = [];
    ledgers = [];
    ledgersBasedOnCOA = [];
    debitLineModal = [];
    creditLineModal = [];
    operatorLovForConditions = [];
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
        private notificationsService: NotificationsService
    ) {

    }
    ngOnInit() {
       
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa fa-angle-down');
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa fa-angle-up');
        //$('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(2)').addClass('cuppa-single-selection-angle');
        this.lookUpCodeService.fetchLookUpsByLookUpType('RECONCILIATION_STATUS').subscribe((res: any) => {
            this.reconciliationList = res;
            console.log(' this.reconciliationList ' + JSON.stringify(this.reconciliationList));
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_STATUS').subscribe((res: any) => {
            this.accountingList = res;
            console.log(' this.accountingList ' + JSON.stringify(this.accountingList));
        });


        let url = this.route.snapshot.url.map(segment => segment.path).join('/');
        this.routeSub = this.route.params.subscribe(params => {
            if (params['id']) {
                if (url.endsWith('edit')) {
                    this.isEdit = true;
                    this.fetchLookUps();
                    this.fetchDataViews();
                    //fetchAPI
                    this.fetchAccountingruleGroupByGrpId(params['id']);
                }
                else {
                    // console.log('in view of acc')
                    this.isViewOnly = true;
                    //fetchAPI
                    this.fetchAccountingruleGroupByGrpId(params['id']);
                }
            }
            else {
                if (this.accountingMode.toString().endsWith('WQ')) {
                    this.ruleCreationInWQ = true;
                }
                this.createForms(0);
                this.fetchLookUps();
                // console.log('in new');
                this.fetchDataViews();
                this.isCreateNew = true;
                this.addNewRuleObject(0, 0);
            }

        });
    }
    fetchAccountingruleGroupByGrpId(grpId) {
      
        this.ruleGroupService.getAccRuleGroupDetails(grpId).subscribe((res: any) => {
            this.ruleGroupService.accountingRuleGroup = res;
            let i: number = 0;
            if (this.ruleGroupService.accountingRuleGroup && this.ruleGroupService.accountingRuleGroup.rules) {
                this.ruleGroupService.ruleGroup.coa = this.ruleGroupService.accountingRuleGroup.rules[0].coa;
                this.refreshWithCOA(this.ruleGroupService.ruleGroup.coa);
                this.ruleGroupService.ruleGroup.coaMeaning =  this.ruleGroupService.accountingRuleGroup.rules[0].coaMeaning;
                this.ruleGroupService.accountingRuleGroup.rules.forEach(rule => {
                    this.showDebitLineCondDetails[i] = [];
                    this.showCreditLineCondDetails[i] = [];
                    let j = 0;
                    let k = 0;
                    rule.lineDerivationRules.forEach(line => {

                        if (!rule.debitLines) {
                            rule.debitLines = [];
                        }
                        if (!rule.creditLines) {
                            rule.creditLines = [];
                        }
                        if (line.lineType == 'DEBIT') {
                            this.showDebitLineCondDetails[i][j] = [];
                            rule.debitLines.push(line);
                        }
                        else {
                            this.showCreditLineCondDetails[i][k] = [];
                            rule.creditLines.push(line);
                        }
                    });
                    i = i + 1;
                });
            }
            console.log(' fetch returned' + JSON.stringify(this.ruleGroupService.accountingRuleGroup));
            if (this.isEdit) {

            }
            else {

            }
        });
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
        if (!this.sourceDataViewsArrays)
            this.sourceDataViewsArrays = [];
    }
    fetchDataViews() {
        this.sourceDataViewsArrays = [];
        this.dataViewsService.dataViewList().subscribe((res: any) => {
            this.dataViewList = res;
            this.dataViewList.forEach(dv => {
                dv['itemName'] = dv['dataViewDispName'];
            });
            this.sourceDataViewsArrays = this.dataViewList;
        });
    }
    fetchLookUps() {
        /*this.lookUpCodeService.fetchLookUpsByLookUpType( 'CHART_OF_ACCOUNTS' ).subscribe(( res: any ) => {
            this.chartOfAccounts = res;
        } );*/
        this.chartOfAccountService.getChartOfAccountsByTenant().subscribe((res: any) => {
            this.chartOfAccounts = res;
        });
        //  console.log(' this.chartOfAccounts' + JSON.stringify(this.chartOfAccounts));
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_CRITERIA_TYPES').subscribe((res: any) => {
            this.accCriterias = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_LINE_TYPES').subscribe((res: any) => {
            this.linetypes = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACC_HEADER_LINE_TYPES').subscribe((res: any) => {
            this.headerLineTypes = res;
            let ind: number = 0;
            this.ruleGroupService.accountingRuleGroup.rules.forEach(rule => {
                this.createHeaderForRule(ind);
                ind = ind + 1;
            });

        });
        this.mappingSetService.getMappingSetsByTenantId().subscribe((res: any) => {
            this.mappingSets = res;
        });
        //this.ledgerDefinitionService.getLedgersByTenant().subscribe((res: any) => {
        //    this.ledgers = res;
        //  console.log(' this.ledgers ' + JSON.stringify(this.ledgers));
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
        let html = `<span >${data.ruleCode}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    dataViewListFormatter = (data: any) => {
        let html = `<span >${data.dataViewDispName}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    addNewRuleObject(indexVal: any, childIndex: any) {
        this.createForms(indexVal);
        let accRule = new AccountingRule();
        accRule.coa =  this.ruleGroupService.ruleGroup.coa;
        // accRule.lineDerivationRules = accLines;

        this.ruleGroupService.accountingRuleGroup.rules.push(accRule);

        // this.sourceDataViewsArrays[indexVal] = this.sourceDataViewsArrays[0];
        //this.sourceDataViewsArrays[indexVal] = this.dataViewList;
        this.createHeaderForRule(indexVal);
        this.fetchAccountingRuleList(indexVal);
        this.displayDebitConditions[indexVal] = [];
        if (!this.showDebitLineCondDetails)
            this.showDebitLineCondDetails = [];
        this.showDebitLineCondDetails[indexVal] = [];
        if(this.isEdit)
        {
            this.refreshOneRuleWithCoa(indexVal);
        }
        if(this.isCreateNew)
        {
            this.addNewDebitLine(indexVal, 0);
            this.addNewCreditLine(indexVal, 0);
        }
       
        // if (!this.ledgersBasedOnCOA)
        //     this.ledgersBasedOnCOA = [];
        // this.ledgersBasedOnCOA[indexVal] = [];
        //this.addDebitLineCondition(indexVal,childIndex);
        // this. addCreditLineCondition(indexVal,childIndex);
        // console.log('generated rule obj after addition at'+indexVal+'=>'+childIndex+'=>'+JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules));
    }
    createHeaderForRule(ind: any) {
        if (this.headerLineTypes) {
            // console.log('header line types exists and are' + JSON.stringify(this.headerLineTypes));

            this.headerLineTypes.forEach(lineType => {
                // console.log('loop for ');
                let lineDer = new LineDerivations();
                lineDer.accountingReferencesCode = lineType['lookUpCode'];
                lineDer.accountingReferencesMeaning = lineType['meaning'];
                lineDer.type = 'HEADER';
                this.ruleGroupService.accountingRuleGroup.rules[ind].headerDerivationRules.push(lineDer);
            });
        }
        else {
            // console.log('no value present');
        }

    }
    saveAdhocRule() {

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
        }
        else
            this.ruleGroupService.accountingRuleGroup.rules.splice(i, 1);
    }
    startEndDateClass(indexVal: any) {
        if (this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate != null) {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }
    }
    dispViewField: boolean = false;
    setRuleObject(ruleName: any, indexVal: any, value: any) {
        // console.log('value.data' + JSON.stringify(value.data));
        console.log('setRuleObject - ruleName ' + ruleName);
        console.log('setRuleObject - value ' + JSON.stringify(value));
        if (value && value.data && value.data.id)
            this.rulesService.fetchExistingAccountingRuleDetails(value.data.id).subscribe((res: any) => {
                let ruleObj: any = res;
                console.log('ruleObj' + JSON.stringify(ruleObj));
                this.ruleGroupService.accountingRuleGroup.rules[indexVal] = ruleObj;
                this.ruleGroupService.accountingRuleGroup.rules[indexVal].endDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.accountingRuleGroup.rules[indexVal].endDate);
                this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.accountingRuleGroup.rules[indexVal].startDate);
            });
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
        console.log('set dv called');
        if (!this.ruleCreationInWQ) {
            $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
            this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId = view.id;
            this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewName = view.itemName;
            this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = null;
            this.ruleGroupService.reconDataSourceArrays[i]=[];
            this.fetchColumnsByDVId(view.id, i);
        }
        if (this.ruleGroupService.ruleGroup.activityBased == true) {
           
            this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource(this.ruleGroupService.ruleGroup.reconciliationGroupId, view.id).subscribe((res: any) => {
                this.ruleGroupService.reconDataSourceArrays[i] = res;
                if (this.ruleGroupService.reconDataSourceArrays[i] && this.ruleGroupService.reconDataSourceArrays[i].length > 0) {
                    if(this.ruleGroupService.reconDataSourceArrays[i].length  == 1 &&  this.ruleGroupService.reconDataSourceArrays[i][0] && this.ruleGroupService.reconDataSourceArrays[i][0]['failed'])
                    {
                        this.ruleGroupService.reconDataSourceArrays[i]=[];
                        this.notificationsService.info(
                            'Eligible data sets not found.',
                            ' '
                        )
                    }
                    else
                    this.ruleGroupService.reconDataSourceArrays[i].forEach(dv => {
                        dv['itemName'] = dv['DataViewDispName'];
                    });
                }
                else{
                    this.notificationsService.info(
                        'Eligible data sets not found',
                        ' '
                    )
                }

            });
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
    setReconDataSource(view: any, i: any) {
              this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId = view.id;
              this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName = view.itemName;
              $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
        }
    fetchColumnsByDVId(id, i) {
        this.dataViewsService.getDataViewById(id).subscribe((res: any) => {
            let dvAndCols = res;
            if (dvAndCols[0] && dvAndCols[0].dvColumnsList) {
                this.columnsByViewId = dvAndCols[0].dvColumnsList;
                if (!this.dvColumns[i])
                    this.dvColumns[i] = [];

                this.dvColumns[i] = this.columnsByViewId;
            }

        });

    }
    refreshWithCOA(coaId)
    {
        this.segmentsService.fetchSegmentsByCOAOrderBySequence(coaId).subscribe((res: any) => {
            let resp: any = res;
            // 
            this.ledgerDefinitionService.getLedgersByTenantAndCoa(coaId).subscribe((res: any) => {
                this.ledgersBasedOnCOA = res;
                this.segments = resp.segments;
                let ind : number =0;
                //console.log('fetched segments are:' + JSON.stringify(this.segments));
                this.ruleGroupService.accountingRuleGroup.rules.forEach(rule=>{
                    if(!rule.id)
                    {
                        this.addNewDebitLine(ind, 0);
                        this.addNewCreditLine(ind, 0);
                    }
                    else{
                        rule.coa = coaId;
                    }

                   
                    //filter ledgers
                    
                    ind= ind +1;
                });
            });
          
           
        });
        /*this.lookUpCodeService.fetchLookUpsByLookUpType( this.ruleGroupService.accountingRuleGroup.rules[indexVal].coa ).subscribe(( res: any ) => {
            this.segments = res;
        });*/
    }
    refreshOneRuleWithCoa(indexVal)
    {
        this.addNewDebitLine(indexVal, 0);
        this.addNewCreditLine(indexVal, 0);
    }
    SelectCOA(coaId) {
        
       
    }

    addNewCreditLine(indexVal: any, ind: any) {
        let newLine = new AccountingLine();
        newLine.lineType = 'CREDIT';
        // if(!newLine.accountingRuleDerivations)
        newLine.accountingRuleDerivations = [];
        this.segments.forEach(segment => {
            let lineDerivation = new LineDerivations();
            lineDerivation.accountingReferencesCode = segment.id;
            lineDerivation.accountingReferencesMeaning = segment.segmentName;
            newLine.accountingRuleDerivations.push(lineDerivation);
            //fetch value set
            if (segment.valueSet)
                lineDerivation.valueSet = segment.valueSet;
        })
        if (newLine.accountingRuleDerivations.length > 0)
            newLine.accountingRuleDerivations.forEach(derivation => {
                derivation.type = 'LINE';
            });
        this.ruleGroupService.accountingRuleGroup.rules[indexVal].creditLines[ind] = newLine;
        if (!this.displayCreditConditions)
            this.displayCreditConditions = [];
        if (!this.displayCreditConditions[indexVal])
            this.displayCreditConditions[indexVal] = [];
        if (this.displayCreditConditions[indexVal][ind])
            this.displayCreditConditions[indexVal][ind] = [];
    }

    addNewDebitLine(indexVal: any, ind: any) {
        let newLine = new AccountingLine();
        newLine.lineType = 'DEBIT';
        //if(!newLine.accountingRuleDerivations)
        newLine.accountingRuleDerivations = [];
        //console.log('newLine.accountingRuleDerivations' + indexVal + '-' + ind + '-' + newLine.accountingRuleDerivations.length);
        // console.log('  this.segments' + this.segments.length);
        if (!this.lineSegmentLOVs)
            this.lineSegmentLOVs = [];

        if (!this.lineSegmentLOVs[indexVal])
            this.lineSegmentLOVs[indexVal] = [];

        let indexBasedSegmentLovList = [];

        if (this.segments && this.segments.length > 0) {
            let i: number = 0;
            this.segments.forEach(segment => {
                let lineDerivation = new LineDerivations();
                lineDerivation.accountingReferencesCode = segment.id;
                lineDerivation.accountingReferencesMeaning = segment.segmentName;
                newLine.accountingRuleDerivations.push(lineDerivation);
                //fetch value set
                if (segment.valueSet)
                    lineDerivation.valueSet = segment.valueSet;


                i = i + 1;
            })
            if (newLine.accountingRuleDerivations.length > 0)
                newLine.accountingRuleDerivations.forEach(derivation => {
                    derivation.type = 'LINE';
                });
            //console.log('newLine.accountingRuleDerivations' + indexVal + '-' + ind + '-' + newLine.accountingRuleDerivations.length);
            this.ruleGroupService.accountingRuleGroup.rules[indexVal].debitLines[ind] = newLine;
            if (!this.displayDebitConditions)
                this.displayDebitConditions = [];
            if (!this.displayDebitConditions[indexVal])
                this.displayDebitConditions[indexVal] = [];
            if (!this.displayDebitConditions[indexVal][ind])
                this.displayDebitConditions[indexVal][ind] = [];
        }

    }
    SelectDebitLineLevelColumn(i, j, drd) {
        if (!this.debitLineLevelOperatorsLOV)
            this.debitLineLevelOperatorsLOV = [];
        if (!this.debitLineLevelOperatorsLOV[i])
            this.debitLineLevelOperatorsLOV[i] = [];
        if (!this.debitLineLevelOperatorsLOV[i][j])
            this.debitLineLevelOperatorsLOV[i][j] = [];
        if (!this.debitLineLevelOperatorsLOV[i][j][drd])
            this.debitLineLevelOperatorsLOV[i][j][drd] = [];

        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].dataType).subscribe((res: any) => {
                let operatorss = res;
                this.debitLineLevelOperatorsLOV[i][j][drd] = operatorss;
            });

        }
        else {
            // console.log('no value set for line dt');
        }


    }
    SelectCreditLineLevelColumn(i, j, drd) {
        if (!this.creditLineLevelOperatorsLOV)
            this.creditLineLevelOperatorsLOV = [];
        if (!this.creditLineLevelOperatorsLOV[i])
            this.creditLineLevelOperatorsLOV[i] = [];
        if (!this.creditLineLevelOperatorsLOV[i][j])
            this.creditLineLevelOperatorsLOV[i][j] = [];
        if (!this.creditLineLevelOperatorsLOV[i][j][drd])
            this.creditLineLevelOperatorsLOV[i][j][drd] = [];

        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].dataType).subscribe((res: any) => {
                let operatorss = res;
                this.creditLineLevelOperatorsLOV[i][j][drd] = operatorss;
            });

        }
        else {
            // console.log('no value set for line dt');
        }


    }
    SelectColumn(i, h) {
        // console.log('this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h]' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h]));
        if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].dataType).subscribe((res: any) => {
                let operatorss = res;
                if (!this.operators)
                    this.operators = [];
                if (!this.operators[i])
                    this.operators[i] = [];
                if (!this.operators[i][h])
                    this.operators[i][h] = [];
                this.operators[i][h] = operatorss;
            });

        }
        else {
            //console.log('no value set for dt');
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

        }
        else if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].criteria == 'VIEW_COLUMN') {
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].mappingSetName = null;
        }
        else if (this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[h].criteria == 'MAPPING_SET') {
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

        }
        else if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].criteria == 'VIEW_COLUMN') {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].mappingSetName = null;
        }
        else if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleDerivations[drd].criteria == 'MAPPING_SET') {
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

        }
        else if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].criteria == 'VIEW_COLUMN') {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].constantValue = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].mappingSetId = null;
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].mappingSetName = null;
        }
        else if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleDerivations[drd].criteria == 'MAPPING_SET') {
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
    fetchAccountingRuleList(indexVal) {
        this.rulesService.getAccountingRulesList('ACCOUNTING').subscribe((res: any) => {
            let rulesFetched: any = [];
            rulesFetched = res;
            this.filteredRuleList = [];
            if (rulesFetched)
                rulesFetched.forEach(rule => {
                    this.filteredRuleList.push(rule);
                });
            this.ruleListArrays[indexVal] = [];
            this.ruleListArrays[indexVal] = this.filteredRuleList;
        });


    }
    addDebitLineCondition(i, j) {
        if (!this.debitLineModal)
            this.debitLineModal = [];
        if (!this.debitLineModal[i])
            this.debitLineModal[i] = [];
        if (!this.debitLineModal[i][j])
            this.debitLineModal[i][j] = [];
        this.debitLineModal[i][j] = true;
    }
    addCreditLineCondition(i, j) {
        if (!this.creditLineModal)
            this.creditLineModal = [];
        if (!this.creditLineModal[i])
            this.creditLineModal[i] = [];
        if (!this.creditLineModal[i][j])
            this.creditLineModal[i][j] = [];
        this.creditLineModal[i][j] = true;
    }
    SelectDebitLineConditionColumn(i, j, rc, datatype, sViewColName) {
        // console.log('dvColumns[i] ' + JSON.stringify(this.dvColumns[i]));
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j]) {

            let linederivations = new AccountingLine();

            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j] = linederivations;

            let accRuleConditions = new AccountingRuleConditions();
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc] = accRuleConditions;

        }
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].sViewColumnName = sViewColName;
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].dataType = datatype;
        // console.log(' this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j]' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j]));
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rc].dataType).subscribe((res: any) => {
                let operatorss = res;
                if (!this.operatorLovForConditions)
                    this.operatorLovForConditions = [];
                if (!this.operatorLovForConditions[i])
                    this.operatorLovForConditions[i] = [];
                if (!this.operatorLovForConditions[i][j])
                    this.operatorLovForConditions[i][j] = [];
                if (!this.operatorLovForConditions[i][j][rc])
                    this.operatorLovForConditions[i][j][rc] = [];
                this.operatorLovForConditions[i][j][rc] = operatorss;
            });

        }
    }
    SelectCreditLineConditionColumn(i, j, rc, datatype, sViewColName) {
        //console.log('dvColumns[i] ' + JSON.stringify(this.dvColumns[i]));
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j]) {

            let linederivations = new AccountingLine();

            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j] = linederivations;

            let accRuleConditions = new AccountingRuleConditions();
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc] = accRuleConditions;

        }
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].sViewColumnName = sViewColName;
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].dataType = datatype;
        //console.log(' this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j]' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j]));
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rc].dataType).subscribe((res: any) => {
                let operatorss = res;
                if (!this.operatorLovForConditions)
                    this.operatorLovForConditions = [];
                if (!this.operatorLovForConditions[i])
                    this.operatorLovForConditions[i] = [];
                if (!this.operatorLovForConditions[i][j])
                    this.operatorLovForConditions[i][j] = [];
                if (!this.operatorLovForConditions[i][j][rc])
                    this.operatorLovForConditions[i][j][rc] = [];
                this.operatorLovForConditions[i][j][rc] = operatorss;
            });

        }
    }
    addMappingCriteriaForDebitLines(i, j, rcIndex) {
        let accRuleCondition = new AccountingRuleConditions();
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions)
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions = [];
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.push(accRuleCondition);
        this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions[rcIndex - 1].logicalOperator = 'AND';
        //console.log('  this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions));
        if (!this.showDebitLineCondDetails[i][j])
            this.showDebitLineCondDetails[i][j] = false;
    }
    addMappingCriteriaForCreditLines(i, j, rcIndex) {
        let accRuleCondition = new AccountingRuleConditions();
        if (!this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions)
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions = [];
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.push(accRuleCondition);
        this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions[rcIndex - 1].logicalOperator = 'AND';
        // console.log('  this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions' + JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions));
    }
    deleteDebitRuleCondition(i, j, rc) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.splice(rc, 1);
            this.addMappingCriteriaForDebitLines(i, 0, 0);
        }

        else
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[j].accountingRuleConditions.splice(rc, 1);

    }
    deleteCreditRuleCondition(i, j, rc) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.splice(rc, 1);
            this.addMappingCriteriaForCreditLines(i, 0, 0);
        }

        else
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[j].accountingRuleConditions.splice(rc, 1);
    }
    displayDRC(i, j) {
        // console.log('ij is' + i + '-' + j);
        if (!this.displayDebitConditions)
            this.displayDebitConditions = [];
        if (!this.displayDebitConditions[i])
            this.displayDebitConditions[i] = [];
        if (!this.displayDebitConditions[i][j])
            this.displayDebitConditions[i][j] = [];
        this.displayDebitConditions[i][j] = true;
    }

    displayCRC(i, j) {
        if (!this.displayCreditConditions)
            this.displayCreditConditions = [];
        if (!this.displayCreditConditions[i])
            this.displayCreditConditions[i] = [];
        if (!this.displayCreditConditions[i][j])
            this.displayCreditConditions[i][j] = [];
        this.displayCreditConditions[i][j] = true;
    }
    changeView(i) {
        if (this.debitCreditLineSize == 'col-md-12')
            this.debitCreditLineSize = 'col-md-6';
        else if (this.debitCreditLineSize == 'col-md-6')
            this.debitCreditLineSize = 'col-md-12';
    }
    deleteDebitLineObject(i, j) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.splice(j, 1);
            this.addNewDebitLine(i, 0);
        }
        else
            this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.splice(j, 1);
        /* if(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.length == 0 &&  this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.length == 0)
             this.addNewDebitLine(i, 0);*/
    }
    deleteCreditLineObject(i, k) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.length == 1) {
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.splice(k, 1);
            this.addNewCreditLine(i, 0);
        }
        else
            this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.splice(k, 1);
        /*if(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines.length == 0 &&  this.ruleGroupService.accountingRuleGroup.rules[i].debitLines.length == 0)
            this.addNewCreditLine(i, 0);*/
    }

    getDataView(val: any, ind: any) {
        //this.ruleObj
        this.fetchDVBasedOnRuleGroup(ind, this.ruleGroupService.ruleGroup.reconciliationGroupId);
        console.log('val ' + val);
        /* if(val == 'RECONCILIED' || val == 'NOT_RECONCILIED'){
            console.log('ruleGrpId' + this.ruleGroupService.ruleGroup.reconciliationGroupId);
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
        console.log('ind ' + ind + ' rgp ' + rGrpId);
        this.ruleGroupService.fetchingDataViewList(rGrpId).subscribe((res: any) => {
            console.log('res ' + JSON.stringify(res));
            this.sourceDataViewsArrays = res;
        });
    }
    validateForm() {
        console.log('accRuleForm validity' + this.accRuleForm.valid);

    }
    addHeaderFilter(i,headerInd)
    {
        let operatorMeaning : any='';
        if(  this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operatorMeaning )
        operatorMeaning =   this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operatorMeaning ;
        let data = {
            addHeaderFilter: 'addHeaderFilter',
            operator : this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operator,
            operatorMeaning:operatorMeaning,
            ok:'ok',
            value : this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].value,
            operatorsList:this.operators[i][headerInd],
        }
        var dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok') {
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operator= data.operator;
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].value= data.value;
                this.ruleGroupService.accountingRuleGroup.rules[i].headerDerivationRules[headerInd].operatorMeaning =data.operatorMeaning;
            }
        });
    }
    openDebitLineDialog(i, j) {
        var dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data: {
                addCondition: 'addCondition',
                ruleName: this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode,
                i: i,
                j: j,
                dvColumns: this.dvColumns[i]
            }, disableClose: true
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'ok') {
            }
        });
    }
    checkIfRuleCodeisUndefined(i) {
        if (this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode == undefined) {
            console.log('rule code is undefined');
            this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode = '';
        }
    }

    filterReconDataSources(dataViewId) {

    }
    onOpen(event)
    {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"false");
    }
    copyConditions(i,k)
    {
        let data:any={};
        data={
            copyLineItem : 'copyLineItem',
            debitLines : this.ruleGroupService.accountingRuleGroup.rules[i].debitLines,
            copy:'copy',
            skipCopy:'skipCopy',
            copyItemIndex:-1
          }
        var dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'copy') {
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[k].accountingRuleConditions = JSON.parse(JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[data.copyItemIndex].accountingRuleConditions));
                  
            }
        });
    }
    copyLineItem(i,k)
    {
        let data:any={};
        data={
            copyLineItem : 'copyLineItem',
            debitLines : this.ruleGroupService.accountingRuleGroup.rules[i].debitLines,
            copy:'copy',
            skipCopy:'skipCopy',
            copyItemIndex:-1
          }
        var dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
            data, disableClose: true
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result && result == 'copy') {
              //  console.log('data has now'+JSON.stringify(data));
                this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[k] = JSON.parse(JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].debitLines[data.copyItemIndex]));
                  this.ruleGroupService.accountingRuleGroup.rules[i].creditLines[k].lineType ='CREDIT';
            }
        });
        
      // console.log('this.ruleGroupService.accountingRuleGroup.rules[i].creditLines'+JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].creditLines));
    }
    startDateChanged(dt:Date,i){
        if(  this.ruleGroupService.accountingRuleGroup.rules[i].endDate){
            if(this.ruleGroupService.accountingRuleGroup.rules[i].endDate<this.ruleGroupService.accountingRuleGroup.rules[i].startDate){
                this.ruleGroupService.accountingRuleGroup.rules[i].endDate=this.ruleGroupService.accountingRuleGroup.rules[i].startDate;
            }
        }
    }
    endDateChanged(i)
    {
        if(this.isCreateNew)
        {
            if(this.endDateChange){
                this.ruleGroupService.accountingRuleGroup.rules[i].endDate=new Date(this.ruleGroupService.accountingRuleGroup.rules[i].endDate.getTime() + 86400000);
            }
        }
    }
}