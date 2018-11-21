import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, ViewChild, Inject } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { Router, NavigationEnd } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { RuleGroup } from './rule-group.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { CommonService } from '../common.service';
import { Rules } from '../rules/rules.model';
import { Observable } from 'rxjs/Rx';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { DataViewsService } from '../data-views/data-views.service';
import { RuleGroupConditionsPost } from './rule-group-conditions-post.model';
import { RulesService } from '../rules/rules.service';
import { RuleConditions } from '../rule-conditions/rule-conditions.model';
import { RuleGroupService } from '../rule-group/rule-group.service';
import { RuleGroupWithRulesAndConditions } from './ruleGroupWithRulesAndConditions.model';
import { RulesAndConditions } from '../rules/rulesWithConditions.model';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
//import { NotificationsService } from 'angular2-notifications-lite';
import { RuleGroupAndRuleWithLineItem, AccountingRuleGroup } from './ruleGroupAndRuleWithLineItem.model';
import { NguiMessagePopupComponent, NguiPopupComponent } from '@ngui/popup';
import { ApprovalRuleGRoupWithRules } from './approval-rule-groupWithRules.model';
import { RuleGroupAccountingNewComponent } from './rule-group-accounting-new.component';
import { RuleGroupReconciliationComponent } from './rule-group-reconciliation.component';
import { RuleGroupApprovalsComponent } from './rule-group-approvals.component';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { FxRatesService } from '../fx-rates/fx-rates.service';
import { ChartOfAccountService } from '../chart-of-account/chart-of-account.service';
import { SegmentsService } from '../segments/segments.service';
declare var $: any;
declare var jQuery: any;
const URL = '';

@Component({
    selector: 'rule-group-confirm-action-modal',
    templateUrl: 'rule-group-confirm-action-modal.html'
})
export class ConfirmActionModalDialog {
    constructor(
        public dialogRef: MdDialogRef<ConfirmActionModalDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}

@Component({
    selector: 'jhi-rule-group-edit',
    templateUrl: './rule-group-edit.component.html'
})
export class RuleGroupEditComponent {
    urlValue:any;
    ruleCreationInWQ:boolean;
    isCreateNew:boolean;
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

    today = new Date();
    // startDateChange  = false;
    //endDateChange  = false;
    cAccdropdownSettings = {
        singleSelection: true,
        // text: "Select Control A/C",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        //selectionLimit: 1,
        autoUnselect: true,
        // closeOnSelect: true,
        showCheckbox: false
    };
    realizedAccdropdownSettings = {
        singleSelection: true,
        // text: "Select  Realized Gain/Loss A/C",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        //selectionLimit: 1,
        autoUnselect: true,
        //closeOnSelect: true,
        showCheckbox: false
    };
    FxGainAccdropdownSettings = {
        singleSelection: true,
        //text: "Select FX Gain A/C",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        // selectionLimit: 1,
        autoUnselect: true,
        //closeOnSelect: true,
        showCheckbox: false
    };
    FXLossAccropdownSettings = {
        singleSelection: true,
        // text: "Select FX Loss A/C",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        // selectionLimit: 1,
        autoUnselect: true,
        //  closeOnSelect: true,
        showCheckbox: false
    };
    emptySpace=' ';
    @ViewChild(NguiPopupComponent) popup: NguiPopupComponent;
    @ViewChild(RuleGroupAccountingNewComponent) ruleGroupAccountingNewComponent: RuleGroupAccountingNewComponent;
    @ViewChild(RuleGroupReconciliationComponent) ruleGroupReconciliationComponent: RuleGroupReconciliationComponent;
    @ViewChild(RuleGroupApprovalsComponent) ruleGroupApprovalsComponent: RuleGroupApprovalsComponent;
    GroupTypePlaceHolder: any;
    ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
    @Input() saveButton  = false;
    myData: any = '';
    filterAction: any = '';
    duplicateRuleGroupName  = false;
    ruleListformArray: FormGroup[] = [];
    targetDataViewformsArray: FormGroup[] = [];
    srcDataViewformsArray: FormGroup[] = [];

    srcColumnListformsArray: FormGroup[] = [];
    targetColumnListformsArray: FormGroup[] = [];

    selectedSrcDataViewAndColumns = [];
    selectedTargetDataViewAndColumns = [];


    logicalOperatorLOV = [];

    //expandTab [] = [];
    showOptions = false;
    mouseOverRowNo: number = -1;
    parents = [
        {
            children: [
                "child1"
            ]
        },
        {
            children: [
                "child2"
            ]
        },
        {
            children: [
                "child3"
            ]
        },
        {
            children: [
                "child4"
            ]
        },
    ];

    isCreateNe = false;
    isEdit  = false;
    isViewOnly  = false;
    copyRuleGroup  = false;
 //   editRuleGroup  = false;

    create  = 'create';
    edit  = 'edit';
    view  = 'view';

    ruleListArrays: any[] = [];
    filteredRuleList: any[] = [];

    sourceDataViewsArrays: any[] = [];
    targetDataViewsArrays: any[] = [];

    sourceColumnArrays: any[] = [];
    targetColumnArrays: any[] = [];

    // ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
    ruleGroupsList: RuleGroup[] = [];
    RuleWithConditions: RulesAndConditions[];
    sourceDataViewId: number;
    targetDataViewId: number;
    ruleGroup = new RuleGroup();
    rules = new Rules();
    ruleConditions: RuleConditions[];
    ruleConditionList: RuleConditions[] = [];
    ruleNameValidation = '';
    rulesList: Rules[] = [];
    ruleGroupTypes = [];
    operatorLOV: any = [];
    ruletypeLOV: any = [];
    open  = true;
    routeSub: any;
    formControlArray: FormControl[] = [];
    optionsArray: any[][] = [[], []];
    myControl = new FormControl();
    sourceDataViewControl = new FormControl();
    targetDataViewControl = new FormControl();
    sourceColumnLOVControl = new FormControl();
    targetColumnLOVControl = new FormControl();
    operatorLOVControl = new FormControl();
    options: any = [];

    sourceDataViewsAndColumns = [];
    targetDataViewsAndColumns = [];
    sourceColumnLOV = [];
    targetColumnLOV = [];

    filteredOptions: Observable<any[]>[];

    ruleNamefilteredOptions: Observable<any[]>;
    sourceDataViews: Observable<any[]>;
    TargetDataViews: Observable<any[]>;

    srcColumnsList: Observable<any[]>;
    targetColumnList: Observable<any[]>;

    operatorColumnList: Observable<any[]>;
    excelFunctions = [];
    excelexpressioninput: any;
    excelexpressionExample: any;
    operatorList = [];
    //
    FilterTitle  = 'Filters';
    FormulaTitle  = 'Formulae';
    ToleranceTitle  = 'Tolerance ';
    additionalFilters  = '  Additional filters ';
    sFilterTitle = [];
    sFilter = [];
    sFormulaTitle = [];
    sFormula = [];
    sToleranceTitle = [];
    sTolerance = [];
    sAdditionalFiltersTitle = [];

    tFilterTitle = [];
    tFilter = [];
    tFormulaTitle = [];
    tFormula = [];
    tToleranceTitle = [];
    tTolerance = [];
    tAdditionalFiltersTitle = [];
    ruleGroupDiffer: any;
    ruleObjDiffer: any;
    ruleConditionsDiffer: any;
    ruleConditionObjDiffer: any;
    isVisibleA  = false;
    tenantSubscribedModules = [];
    tagRule: any;
    displayRule: any;
    groups = [];
    openDialog: any;
    displayAccountingType: any = 'col-md-12';
    accountingTypes = [];
    nameLength: any = 'col-md-4 col-sm-6 col-xs-12 form-group';
    rulePurposeLength: any = 'col-md-4 col-sm-6 col-xs-12 form-group';
    dateLength: any = 'col-md-4 col-sm-6 col-xs-12 form-group';
    ruleGroupList: any = [];
    conversionTypes = [];
    conversionDates = [];
    indexToRefresh = [];
    //reconDataSourceAfterChange = [];
    ruleNames  = '';
    previousGroupId: any;
    setRulePriority  = false;
    chartOfAccounts = [];
    ruless = [
        {
            "id": 0,
            "ruleName": "r1"
        },
        {
            "id": 1,
            "ruleName": "r2"
        },
        {
            "id": 21,
            "ruleName": "r3"
        },
        {
            "id": 3,
            "ruleName": "r4"
        },
        {
            "id": 4,
            "ruleName": "r5"
        },
    ];
    valueSetForControlAcc = [];
    valueSetForRealizedAcc = [];
    valueSetForFXGainAcc = [];
    valueSetForFXLossAcc = [];
    recActToolTip  = 'Rule Group list';
    // showHeader  =true;
    constructor(

        private builder: FormBuilder,
        private config: NgbDatepickerConfig,
        private router: Router,
        private route: ActivatedRoute,
        private dateUtils: JhiDateUtils,
        public commonService: CommonService,
        private lookUpCodeService: LookUpCodeService,
        private dataViewsService: DataViewsService,
        private rulesService: RulesService,
        public ruleGroupService: RuleGroupService,
        private _sanitizer: DomSanitizer,
        //private notificationsService: NotificationsService,
        public dialog: MdDialog,
        public fxRatesService: FxRatesService,
        private chartOfAccountService: ChartOfAccountService,
        private segmentsService: SegmentsService,


    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        this.ruleGroupService.instantiateObjects();
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa fa-angle-down');
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa fa-angle-up');
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('mat-select-arrow');
        this.ruleGroupTypes = [];
        //console.log( 'in ngonnt of edit comp' );
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) + 'px'
        });
        this.commonService.callFunction();
        this.routeSub = this.route.params.subscribe((params )=> {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.urlValue=url;
            console.log('param has' + params['id']);
            if (params['id']) {
                this.fetchReconGroups();
                this.fetchPrerequisiteDate();
                this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                // detail or edit

                if (url.endsWith('edit')) {
                    this.isEdit = true;
                   // this.fetchReconGroups();
                   
                    this.fetchTenantSubscribedModules();
                   
                    this.isVisibleA = true;
                  //  this.editRuleGroup = true;
                    if (url.search('copy') != -1) {
                        this.copyRuleGroup = true;
                        // this.editRuleGroup = true;
                        this.ruleGroupService.ruleGroup.editRule=true;
                        this.ruleGroupService.ruleGroup.id = null;
                        this.ruleGroupService.ruleGroup.name = null;
                        // this.SelectCOA('populate');

                    }
                } else {
                    this.view = this.view + '-' + params['id'];
                    this.isVisibleA = true;
                    this.isViewOnly = true;

                }
            } else {
                this.ruleGroupService.approvalRuleGRoupWithRules = new ApprovalRuleGRoupWithRules();
                this.ruleGroupService.ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
                this.ruleGroupService.accountingRuleGroup = new AccountingRuleGroup();
                this.ruleGroupService.ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
                this.ruleGroupService.ruleGroup = new RuleGroup();
                this.ruleGroupService.submitted = false;
                console.log('this.ruleGroupService.routerRuleGroupType' + this.ruleGroupService.routerRuleGroupType);
                this.ruleGroupService.ruleGroup.rulePurpose = this.ruleGroupService.routerRuleGroupType;
                let obj: any;
                this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((res: any) => {
                    let ruleGroupTypes = [];
                    this.ruleGroupTypes = [];
                    ruleGroupTypes = res;
                    let ind = 0;
                    if(!this.ruleGroupService.ruleGroup.rulePurpose || this.ruleGroupService.ruleGroup.rulePurpose == undefined){
                        this.ruleGroupService.ruleGroup.rulePurpose = 'RECONCILIATION';                        
                        this.ruleGroupService.routerRuleGroupType=this.ruleGroupService.ruleGroup.rulePurpose;
                        } 
                    ruleGroupTypes.forEach((rgType) => {
                        const rgTypeObj = { "code": rgType.meaning, "name": rgType.description, "setValue": rgType.lookUpCode };
                        if (this.ruleGroupService.routerRuleGroupType == rgType.lookUpCode) {
                            rgTypeObj['value'] = true;
                            obj = rgTypeObj;

                        } else{
                            rgTypeObj['value'] = false;
                        }
                           
                        let code  = '';
                        code = rgType.meaning;
                        code = code.substr(0, 3);
                        rgTypeObj.code = code;
                        this.ruleGroupTypes.push(rgTypeObj);

                        if (ruleGroupTypes.length - 1 == ind) {
                            console.log('ruleGroupTypes.length' + ruleGroupTypes.length + 'ind' + ind);
                            this.SelectRuleGroupType(obj);
                            this.startEndDateClass();
                            this.isCreateNew = true;
                           // this.editRuleGroup = true;
                            this.isVisibleA = true;
                            //   $('.split-example .right-component').css('width','100% !important');
                        }
                        ind = ind + 1;


                    });
                });

                //this.SelectRuleGroupType(obj);
                //       this.startEndDateClass();

            }
        });
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
            if (this.commonService.reconToRule == true) {
                this.isVisibleA = true;
                this.recActToolTip = 'Reconcile Group list';
            } else {
                this.recActToolTip = 'Rule Group list';
            }
        } else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            if (this.commonService.reconToRule == true) {
                this.recActToolTip = 'Accounting Group list';
                this.isVisibleA = true;
            } else {
                this.recActToolTip = 'Rule Group list';
            }
        } else {
            this.recActToolTip = 'Rule Group list';
        }

    }
    selectedRule(value: any, indexVal: any) {
        // this.setRuleObject(value,indexVal);
    }
    changeMinDate() {
        this.config.minDate = this.ruleGroup.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    fetchPrerequisiteDate() {
        this.chartOfAccountService.getChartOfAccountsByTenant().subscribe((res: any) => {
            this.chartOfAccounts = res;
            if(!this.chartOfAccounts || this.chartOfAccounts.length<=0){
                       const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                       data: {noCOA:'NoCOA' ,message: 'Please create Chart Of Accounts to define Accounting rule group' },
                       disableClose: true
                      });
                      this.router.navigate(['/rule-group', { outlets: {'content': ['ACCOUNTING']+'/list'} }]);
            }
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_TYPE').subscribe((res: any) => {
            this.accountingTypes = res;
            // console.log(' this.accountingTypes' + JSON.stringify(this.accountingTypes));
        });
        this.fxRatesService.fetchActiveFxRatesByTenant().subscribe((res: any) => {
            this.conversionTypes = res;
           
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('CONV_DATE_RS_LOV').subscribe((res: any) => {
            this.conversionDates = res;
        });
    }
    fetchReconGroups() {
        this.ruleGroupService.fetchRuleGroupsBypurpose('Reconciliation').subscribe(
            (res: any) => {
                this.ruleGroupList = res;
                //   console.log(' ruleGroupList ' + JSON.stringify(this.ruleGroupList));
            });
    }
   
    startEndDateClass() {
        if (this.ruleGroupService.ruleGroup.startDate != null) {
            // return 'col-md-3 col-sm-6 col-xs-12 form-group';
            if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
                this.nameLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
                this.rulePurposeLength = 'col-md-9 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            }else{
                this.nameLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
                this.rulePurposeLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            }


        } else {
            //return 'col-md-4 col-sm-6 col-xs-12 form-group';
            if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
                this.rulePurposeLength = 'col-md-9 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            }else {
                this.nameLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
                this.rulePurposeLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
            }

        }
    }

    SelectRuleGroupType(ruleGrpType?: any) {
        if (ruleGrpType == undefined || !ruleGrpType || ruleGrpType == '') {
            // console.log('went to undefined');
            this.ruleGroupTypes.forEach((rgType) => {
                if (rgType.setValue == this.ruleGroupService.routerRuleGroupType){ 
                       rgType.value = true;
                   } else{
                    rgType.value = false;
                   }
                    
            });
            //console.log('ruleGroupTypes after undefuned'+JSON.stringify(this.ruleGroupTypes));
            this.ruleGroupService.ruleGroup = new RuleGroup();
        }else {
            this.ruleGroupService.ruleGroup = new RuleGroup();
            this.ruleGroupService.ruleGroup.rulePurpose = ruleGrpType.setValue;
            let alreadySelected = 0;
            this.ruleGroupTypes.forEach((rgType)=> {
                if (rgType.value == true) {
                    alreadySelected = 1;
                }
            });
            this.ruleGroupTypes.forEach((rgType) => {

                if (ruleGrpType.setValue == rgType.setValue) {
                    //ruleType.value = ruleType.value ? false : true;
                    if (rgType.value == true) {
                    } else {
                        rgType.value = true;

                    }
                }else {
                    rgType.value = false;
                }

            });
        }

        if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            this.fetchReconGroups();
           this.fetchTenantSubscribedModules();

            //this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS';
            //this.ruleGroupService.instantiateObjects();
        }
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            this.fetchReconGroups();
            this.fetchPrerequisiteDate();
            this.displayAccountingType = 'col-md-3';
            this.nameLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            this.rulePurposeLength = 'col-md-6 col-sm-6 col-xs-12 form-group';
            this.startEndDateClass();
            //this.ruleGroupService.ruleGroup = new RuleGroup();
            // this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING';
            //  this.ruleGroupService.instantiateObjects();

        }  else {
            this.displayAccountingType = 'col-md-12';
            this.nameLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
            this.rulePurposeLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
            this.startEndDateClass();
            // this.ruleGroupService.ruleGroup = new RuleGroup();
            //  this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION';
            // 

        }

        this.ruleGroupService.instantiateObjects();

        this.setPlaceHolder();
        // this.ruleGrpWithRuleAndLineItems.rulePurpose = this.ruleGroupService.ruleGroup.rulePurpose;

    }
    fetchTenantSubscribedModules() {
        // this.ruleGroupService.getTenantConfigModules().subscribe((res: any) => {
        //     this.tenantSubscribedModules = res;
        //     let indexVal: any = 0;
        //     if (this.tenantSubscribedModules) {
        //         this.tenantSubscribedModules.forEach((module )=> {
        //             if (module.modules.search('_APPROVALS') != -1 || module.modules == 'APPROVALS') {
        //                 this.tenantSubscribedModules.splice(indexVal, 1);
        //             } else {
        //             }
        //             indexVal = indexVal + 1;
        //         });
        //     }
                
        //         if(!this.isCreateNew && this.ruleGroupService.ruleGroup.rulePurpose === 'APPROVALS'){
        //             this.populateProcessBasedOnModule();
        //         }
                
        // });
        this.lookUpCodeService.fetchLookUpsByLookUpType('APPR_CATEGORY_RS_LOV').subscribe((res: any) => {
            this.tenantSubscribedModules = res;
            let indexVal: any = 0;
            if (this.tenantSubscribedModules) {
                this.tenantSubscribedModules.forEach((lookup )=> {
                    if (lookup.lookUpCode.search('_APPROVALS') != -1 || lookup.lookUpCode == 'APPROVALS') {
                        this.tenantSubscribedModules.splice(indexVal, 1);
                    } else {
                    }
                    indexVal = indexVal + 1;
                });
            }
                
                if(!this.isCreateNew && this.ruleGroupService.ruleGroup.rulePurpose === 'APPROVALS'){
                    this.populateProcessBasedOnModule();
                }
                
        });
    }
    setPlaceHolder() {
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
            this.GroupTypePlaceHolder = 'Recon Rule Set Name';
        } else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            this.GroupTypePlaceHolder = 'Accounting  Rule Set Name';
        } else if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            this.GroupTypePlaceHolder = 'Approvals  Rule Set Name';
        }  else {
            this.GroupTypePlaceHolder = 'Process Name';
        }
    }
    fetchGroupAndRulesAndRuleConditionsByGroupId(groupId: any) {

        this.ruleGroupService.fetchRuleGroupObject(groupId).subscribe((res: any) => {
            this.ruleGroupService.ruleGroup = res;
            if(this.ruleGroupService.ruleGroup && this.ruleGroupService.ruleGroup.enableFlag){
                this.ruleGroupService.ruleGroup['status']='Active';
            }else if(this.ruleGroupService.ruleGroup && !this.ruleGroupService.ruleGroup.enableFlag){
                this.ruleGroupService.ruleGroup['status']='Inactive'; 
            }
            this.ruleGroupTypes = [];
            console.log('jsonResponse for rule object ' + JSON.stringify(this.ruleGroupService.ruleGroup));
            //    for(var i=0;i< this.ruleGroupTypes.length;i++)
            //     {
            //         if(this.ruleGroupService.ruleGroup .rulePurpose == this.ruleGroupTypes[i].setValue)
            //         {
            //             this.ruleGroupTypes[i].value=true;
            //         }
            //     }
            this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((resp: any) => {
                let ruleGroupTypes = [];
                ruleGroupTypes = resp;

                for (let ind = 0; ind < ruleGroupTypes.length; ind++) {
                    const rgTypeObj = { "code": ruleGroupTypes[ind].meaning, "name": ruleGroupTypes[ind].description, "setValue": ruleGroupTypes[ind].lookUpCode };
                    if (this.ruleGroupService.ruleGroup.rulePurpose == ruleGroupTypes[ind].lookUpCode){
                        rgTypeObj['value'] = true;
                    }else {
                        rgTypeObj['value'] = false;
                    }
                    let code  = '';
                    code = ruleGroupTypes[ind].meaning;
                    code = code.substr(0, 3);
                    rgTypeObj.code = code;
                    this.ruleGroupTypes.push(rgTypeObj);
                }
            });
            // const obj = {
            //     "id":  this.ruleGroupService.ruleGroup.apprRuleGrpId,
            //     "itemName": this.ruleGroupService.ruleGroup.apprRuleGrpName
            // };
            // this.ruleGroupService.ruleGroup.apprRuleGrpObj=[];
            // this.ruleGroupService.ruleGroup.apprRuleGrpObj.push(obj);
            //console.log('response from group details on april s'+JSON.stringify( this.ruleGroupService.ruleGroup));
            if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
                this.displayAccountingType = 'col-md-6';
            }
            this.today = this.ruleGroupService.ruleGroup.startDate;
            this.setPlaceHolder();
            if (this.copyRuleGroup) {

                this.ruleGroupService.ruleGroup.name = null;

            }
            if (!this.isViewOnly && this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING'){
                this.fetchDataViews();
            }
            this.SelectCOA('populate');

        });
    }
    checkGroupName(name ) {
        this.ruleGroupService.query().subscribe(
            (res: Response) => {
                this.ruleGroupsList = res.json();
                let count = 0;
                if (this.ruleGroupsList){
                    this.ruleGroupsList.forEach((ruleGroup )=> {
                        if (ruleGroup && ruleGroup.name && this.ruleGroupService.ruleGroup && this.ruleGroupService.ruleGroup.name &&
                            ( ruleGroup.name.toLowerCase() == this.ruleGroupService.ruleGroup.name.toLowerCase()) ){
                            count = count + 1;
                        }else {

                        }
                    });
                }
                   
                if (count > 0) {
                    this.duplicateRuleGroupName = true;
                }else {
                    this.duplicateRuleGroupName = false;
                }
            }
        );

    }

    displayOptions() {
        this.showOptions = true;
    }
    validateForm() {
        this.ruleGroupService.submitted = true;
    }
    saveRuleGroup() {
        this.ruleGroupService.submitted = true;
        // if (this.isCreateNew) {
        //     if (this.ruleGroupService.ruleGroup.startDateChange) {
        //         this.ruleGroupService.ruleGroup.startDate = new Date(this.ruleGroupService.ruleGroup.startDate.getTime() + 86400000);
        //     }
        //     if (this.ruleGroupService.ruleGroup.endDateChange && this.ruleGroupService.ruleGroup.endDate) {
        //         this.ruleGroupService.ruleGroup.endDate = new Date(this.ruleGroupService.ruleGroup.endDate.getTime() + 86400000);
        //     }
        // } else if (this.isEdit) {
        //     this.ruleGroupService.ruleGroup.startDate = new Date(this.ruleGroupService.ruleGroup.startDate.getTime() + 86400000);
        //     if (this.ruleGroupService.ruleGroup.endDate){
        //         this.ruleGroupService.ruleGroup.endDate = new Date(this.ruleGroupService.ruleGroup.endDate.getTime() + 86400000);
        //     }
               
        // }
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            for (let c = 0; c < this.ruleGroupService.approvalRuleGRoupWithRules.rules.length; c++) {
                if (this.ruleGroupService.approvalRuleGRoupWithRules.rules[c].duplicateRuleName) {
                    this.commonService.info(
                        '',
                        'Rule name at rule - '+   (c+1) +' already exists'
                    )
                    return;
                }
                if (!this.ruleGroupService.approvalRuleGRoupWithRules.rules[c].ruleCode) {
                    return;
                }
            }
            if (this.ruleGroupApprovalsComponent.approvalForm.valid) {
                let ind = 1;
                if (this.isCreateNew) {
                    this.ruleGroupService.ruleGroup.enableFlag = true;
                }
                // this.ruleGroupService.approvalRuleGRoupWithRules.enabledFlag = true;
                let readyToSave  = false;
                let noActionsList  = '';
                let actionsSize = 0;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach((rule )=> {
                    if (rule.approvalActions.actionDetails && rule.approvalActions.actionDetails.length > 0) {
                        actionsSize = actionsSize + 1;
                    } else {
                        if (ind - 1 == 0){
                            noActionsList = noActionsList + rule.ruleCode + ', ';
                        }  else{
                            noActionsList = noActionsList + rule.ruleCode;
                        }
                           
                    }
                    //rule.priority = ind;
                    ind = ind + 1;
                    if (!rule.ruleGroupAssignId) {
                        //new rule tagged
                        if (rule.id && rule.id > 0) {
                            //existing rule tagged
                            rule.assignmentFlag = true;
                        } else {
                            //new created rule
                            rule.enabledFlag = true;
                            rule.assignmentFlag = true;
                        }
                    }  else {
                        //editing tagged rule

                    }
                });

                /*   if(this.isCreateNew)
                       {
                           this.ruleGroupService.approvalRuleGRoupWithRules.enabledFlag = true;
                           if(this.ruleGroupService.approvalRuleGRoupWithRules.rules)
                               {
                                   this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach(rule=>{
                                       rule.enabledFlag = true;
                                       rule.assignmentFlag = true;
                                   });
                               }
                           
                       }
                   */
                if (this.ruleGroupService.approvalRuleGRoupWithRules.rules.length == actionsSize) {
                    readyToSave = true;
                }
                if (noActionsList) {
                    this.commonService.error(
                        'No approvers tagged to rules - ' + noActionsList,
                        ''
                    )
                } else {


                    if (readyToSave) {
                        this.setPriority();
                        this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach((rule )=> {
                            // if (!rule.id) {
                            //     if (rule.startDateChange) {
                            //         rule.startDate = new Date(rule.startDate.getTime() + 86400000);
                            //     }
                            //     if (rule.endDateChange) {
                            //         rule.endDate = new Date(rule.endDate.getTime() + 86400000);
                            //     }
                            // }

                            // if (!rule.id) {
                            //     if (rule.startDateChange) {
                            //         rule.startDate = new Date(rule.startDate.getTime() + 86400000);
                            //     }
                            //     if (rule.endDateChange && rule.endDate) {
                            //         rule.endDate = new Date(rule.endDate.getTime() + 86400000);
                            //     }
                            // } else if (rule.id) {
                            //     rule.startDate = new Date(rule.startDate.getTime() + 86400000);
                            //     if (rule.endDate){
                            //         rule.endDate = new Date(rule.endDate.getTime() + 86400000);
                            //     }
                                   
                            // }


                        });
                        this.ruleGroupService.postApprovalRules().subscribe((res: any) => {
                            let savedObj: any;
                            savedObj = res;
                            if (savedObj.taskStatus == 'success') {
                                this.commonService.success(
                                    'Saved!',
                                    this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                                )
                                let groupId: any;
                                groupId = savedObj.details;
                                this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
                            }  else {
                                this.commonService.error(
                                    savedObj.taskName,
                                    savedObj.details
                                )
                            }
                        });
                    }
                }

            }    else {
                // this.commonService.error('Please fill mandatory fields', '');
            }
        } else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            for ( let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {
                if (this.ruleGroupService.accountingRuleGroup.rules[c].duplicateRuleName) {
                    this.commonService.info(
                        '',
                        'Rule name at rule - '+   (c+1) +' already exists'
                    )
                    return;
                }
                if (!this.ruleGroupService.accountingRuleGroup.rules[c].ruleCode) {
                   
                    return;
                }
            }
            if (this.ruleGroupAccountingNewComponent.accRuleForm.valid) {
                if (this.isCreateNew || this.copyRuleGroup) {
                    this.ruleGroupService.ruleGroup.enableFlag = true;
                }
                this.ruleGroupService.accountingRuleGroup.rules.forEach((ruleObj) => {
                    if (this.isCreateNew || this.copyRuleGroup) {
                        ruleObj.enabledFlag = true;
                        ruleObj.assignmentFlag = true;
                    }
                    ruleObj.lineDerivationRules = [];
                    if (ruleObj.debitLines) {
                        ruleObj.debitLines.forEach((line )=> {
                            ruleObj.lineDerivationRules.push(line);
                        });
                    }
                      
                    if (ruleObj.creditLines)  {
                        ruleObj.creditLines.forEach((line )=> {
                            ruleObj.lineDerivationRules.push(line);
                        });
                    }
                        
                });


                // let ind: number = 1;
                // // this.ruleGroupService.accountingRuleGroup.enabledFlag = true;
                // this.ruleGroupService.accountingRuleGroup.rules.forEach(rule => {
                //     // rule.priority = ind;

                //     if (!rule.ruleGroupAssignId) {
                //         //new rule tagged
                //         if (rule.id && rule.id > 0) {
                //             //existing rule tagged
                //             rule.assignmentFlag = true;
                //         }
                //         else {
                //             //new created rule
                //             rule.enabledFlag = true;
                //             rule.assignmentFlag = true;
                //         }
                //     }
                //     else {
                //         //editing tagged rule

                //     }
                //     ind = ind + 1;
                // });
                this.setPriority();
                
                if (this.copyRuleGroup) {
                    this.ruleGroupService.ruleGroup.id = null;
                    for ( let c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {
                        this.ruleGroupService.accountingRuleGroup.rules[c].id = null;
                        this.ruleGroupService.accountingRuleGroup.rules[c].assignmentFlag = true;
                        for ( let h = 0; h < this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules.length; h++) {
                            this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules[h].ruleId = null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].headerDerivationRules[h].id = null;
                        }
                        for (let line = 0; line < this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules.length; line++) {
                            //this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[h].=null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].id = null;
                            this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].ruleId = null;
                            for (let sl = 0; sl < this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].accountingRuleDerivations.length; sl++) {
                                this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].accountingRuleDerivations[sl].id = null;
                                this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].accountingRuleDerivations[sl].ruleId = null;
                            }
                            for (let sl = 0; sl < this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].accountingRuleConditions.length; sl++) {
                                this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].accountingRuleConditions[sl].id = null;
                                this.ruleGroupService.accountingRuleGroup.rules[c].lineDerivationRules[line].accountingRuleConditions[sl].ruleId = null;
                            }
                        }


                    }
                }
                this.ruleGroupService.postAccountingRuleGroup(false).subscribe((res: any) => {
                    let savedObj = [];
                    savedObj = res;
                    //console.log( 'savedObj after acc rule save' + JSON.stringify( savedObj ) );
                    if (savedObj[0].taskName == 'Rule Group Save') {
                        this.commonService.success(
                            'Saved!',
                            this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                        )
                        let groupId: any;
                        groupId = savedObj[0].details;
                        this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
                    }
                });


            } else {
                // this.commonService.error('Please fill mandatory fields', '');
            }

        } else {
            if (this.ruleGroupReconciliationComponent.reconForm.valid) {
                let duplicateRuleFound   = false;
                for (let c = 0; c < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; c++) {
                    if (!this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].rule.ruleCode) {
                        return;
                    }
                    if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].rule.duplicateRuleName)  {
                    this.commonService.info(
                        '',
                        'Rule name at rule - '+   (c+1) +' already exists'
                    );
                    duplicateRuleFound= true;
                    break;
                }
                }
                if (this.isCreateNew){
                    this.ruleGroupService.ruleGroup.enableFlag = true;
                }
                    
                let emptyConditionsFound = 0;
                let readyToSave;
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
                    // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.priority = i + 1;

                    if (!this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleGroupAssignId) {
                        //new rule tagged
                        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id > 0) {
                            //existing rule tagged
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.assignmentFlag = true;
                        }   else {
                            //new created rule
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.enabledFlag = true;
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.assignmentFlag = true;
                        }
                    }   else {
                        //editing tagged rule

                    }
                    let ci = 0;
                    const indexes = [];
                    const sequences: any = '';

                    // console.log('conditions before edit' + JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i]));
                    if (!this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id <= 0) {
                        let emptyConditionsForRule = 0;
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach((condition) => {
                            if (!condition.sColumnId && !condition.operator && !condition.tColumnId) {
                                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(ci, 1);
                                emptyConditionsFound = emptyConditionsFound + 1;
                                emptyConditionsForRule = emptyConditionsForRule + 1;
                                if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ci - 1] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length == 1) {
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ci - 1].logicalOperator = null;
                                }
                            }
                            ci = ci + 1;
                        });
                        // console.log('emptyConditionsForRule'+emptyConditionsForRule);
                        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length == emptyConditionsForRule) {
                            this.commonService.info('','Empty conditions found for ' + this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode);
                            break;
                        } else  {
                            //balance parenthesis and validate expression
                        }




                    }

                    //    if(sequences == '')
                    //    {

                    //      if(i==this.ruleGroupService.ruleGroupRulesAndConditions.rules.length-1)
                    //      this.saveReconRuleGroup();
                    //    }
                    //    else{
                    //     var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    //         data: {  deleteEmptyConditions: 'delete', No: 'NoThanks',sequences:sequences, ruleName: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode}
                    //     });
                    //     dialogRef.afterClosed().subscribe(result => {
                    //         if (result && result == 'delete'){
                    //     indexes.forEach(inde=>{
                    //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(inde,1);
                    //     });
                    // if(i==this.ruleGroupService.ruleGroupRulesAndConditions.rules.length-1)
                    // this.saveReconRuleGroup();
                    //     }
                    //     else{
                    //         // this.saveReconRuleGroup();
                    //     }
                    //     });
                    //    }   
                    // console.log('this.ruleGroupService.ruleGroupRulesAndConditions.rules.length'+this.ruleGroupService.ruleGroupRulesAndConditions.rules.length);
                    // console.log('i=>'+i)
                    if (this.ruleGroupService.ruleGroupRulesAndConditions.rules.length - 1 == i) {
                        readyToSave = true;
                    }

                }

                // if (emptyConditionsFound > 0) { 
                //     var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                //         data: { message: ' Ignored ' + emptyConditionsFound + ' empty conditions' },
                //         disableClose: true
                //     });

                // }
                if (readyToSave && emptyConditionsFound < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length) {
                    this.setPriority();
                    this.saveReconRuleGroup();
                } else {
                    // this.commonService.error('Could not validate conditions','');
                }


            }
            //else
            // this.commonService.error('Please fill mandatory fields', '');
        }


    }
    saveReconRuleGroup() {
        if (this.copyRuleGroup){
            this.ruleGroupService.ruleGroup.id = null;
        }
            

            let duplicateRuleFound   = false;
        for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
            const rule = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i];


            // rule.rule.ruleListformArray =null;
            // if(rule.rule.startDateChange  && (rule.rule.editRule))
            // {
            //     if (rule.rule.startDate.getTime())
            //     rule.rule.startDate = new Date(rule.rule.startDate.getTime() + 86400000);
            // }
            // if(rule.rule.endDateChange && (rule.rule.editRule))
            // {
            //     if (rule.rule.endDate && rule.rule.endDate.getTime())
            //     rule.rule.endDate = new Date(rule.rule.endDate.getTime() + 86400000);
            // }
            if (rule.rule.duplicateRuleName) {
                this.commonService.info(
                    '',
                    'Rule name at rule - '+   (i+1) +' already exists'
                );
                duplicateRuleFound= true;
                break;
            }  else {
                if (rule.rule.editRule) {
                    if (rule.ruleConditions && rule.ruleConditions.length > 0) {
                        rule.ruleConditions.forEach((condition )=> {
                            if (!condition.sToleranceValueFrom && !condition.sToleranceValueTo) {
                                condition.sToleranceType = '';
                            }
                            if (!condition.tToleranceValueFrom && !condition.tToleranceValueTo) {
                                condition.tToleranceType = '';
                            }
                            if (condition.sPercentile) {
                                condition.sToleranceValueFrom = condition.sToleranceValueFrom + '%';
                                condition.sToleranceValueTo = condition.sToleranceValueTo + '%';
                            }
                            if (condition.tPercentile) {
                                condition.tToleranceValueFrom = condition.tToleranceValueFrom + '%';
                                condition.tToleranceValueTo = condition.tToleranceValueTo + '%';
                            }
                            if (!condition.sToleranceValueFrom && !condition.sToleranceValueTo) {
                                condition.sToleranceType = '';
                            }
                            if (condition.sToleranceValueFrom){
                                if (condition.sToleranceValueFrom.toString().startsWith("-")) {
                                    const val = condition.sToleranceValueFrom.toString().split("-");
                                    condition.sToleranceValueFrom = val[1];
                                    condition.sToleranceOperatorFrom = "-";
                                }  else if (condition.sToleranceValueFrom.toString().startsWith("+")) {
                                    const val = condition.sToleranceValueFrom.toString().split("+");
                                    condition.sToleranceValueFrom = val[1];
                                    condition.sToleranceOperatorFrom = "+";
                                }
                            }
                            if (condition.sToleranceValueTo){
                                if (condition.sToleranceValueTo.toString().startsWith("-")) {
                                    const val = condition.sToleranceValueTo.toString().split("-");
                                    condition.sToleranceValueTo = val[1];
                                    condition.sToleranceOperatorTo = "-";
                                }  else if (condition.sToleranceValueTo.toString().startsWith("+")) {
                                    const val = condition.sToleranceValueTo.toString().split("+");
                                    condition.sToleranceValueTo = val[1];
                                    condition.sToleranceOperatorTo = "+";
                                }
                            }
                              
                            if (condition.tToleranceValueFrom){
                                if (condition.tToleranceValueFrom.toString().startsWith("-")) {
                                    const val = condition.tToleranceValueFrom.toString().split("-");
                                    condition.tToleranceValueFrom = val[1];
                                    condition.tToleranceOperatorFrom = "-";
                                }  else if (condition.tToleranceValueFrom.toString().startsWith("+")) {
                                    const val = condition.tToleranceValueFrom.toString().split("+");
                                    condition.tToleranceValueFrom = val[1];
                                    condition.tToleranceOperatorFrom = "+";
                                }
                            }
                            if (condition.tToleranceValueTo){
                                if (condition.tToleranceValueTo.toString().startsWith("-")) {
                                    const val = condition.tToleranceValueTo.toString().split("-");
                                    condition.tToleranceValueTo = val[1];
                                    condition.tToleranceOperatorTo = "-";
                                }  else if (condition.tToleranceValueTo.toString().startsWith("+")) {
                                    const val = condition.tToleranceValueTo.toString().split("+");
                                    condition.tToleranceValueTo = val[1];
                                    condition.tToleranceOperatorTo = "+";
                                }
                            }
                            if (!condition.tToleranceValueFrom && !condition.tToleranceValueTo) {
                                condition.tToleranceType = '';
                            }

                        });
                    }
                }
            }
        }
            if(!duplicateRuleFound)    {
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruletypeLOVArray=[];
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].sourceDataViewsAndColumns=[];
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].targetDataViewsAndColumns=[];
                }
                this.ruleGroupService.postRuleGroup().subscribe((res: any) => {
                    let savedObj: any;
                    savedObj = [];
                    const resp = (res);
                    savedObj.push(resp);
                    //console.log('saved obj of reconciliation is:'+JSON.stringify(savedObj));
                    const taskk  = false;
                    if (savedObj) {
                        if (savedObj.length > 0) {
                            if (savedObj[0].taskStatus.toLowerCase().search('success') != -1) {
                                this.commonService.success(
                                    'Saved!',
                                    this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                                )
                                let groupId: any;
                                groupId = savedObj[0].details;
                                //console.log('groupId'+groupId);
                                this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
                            }   else if (savedObj[0].taskStatus.toLowerCase().search('fail') != -1) {
                                this.commonService.error(
                                    savedObj[0].taskName,
                                    savedObj[0].details
                                )
                            }  else {
                                this.commonService.info(
                                    '',
                                    'No response recieved'
                                )
                            }
        
                        }
                        // else if(savedObj.length ==1){
                        //     if(savedObj[0].taskName == 'Saving rule group' &&  savedObj.taskStatus.toLowerCase().search('success') != -1)
                        //     {
                        //         this.commonService.success(
                        //             'Saved!',
                        //             this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                        //         )
                        //         let groupId: any;
                        //         groupId = savedObj.details;
                        //         //console.log('groupId'+groupId);
                        //         this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
                        //     }
                        //     else if(savedObj[0].taskName == 'Saving rule group' &&  savedObj.taskStatus.toLowerCase().search('fail') != -1) {
                        //         this.commonService.error(
                        //             savedObj[0].taskName ,
                        //             savedObj[0].details
                        //         )
                        //     }
                        //     else{
                        //         this.commonService.info(
                        //             '',
                        //          'No response  recieved' 
                        //         )
                        //     }
        
                        // }
                    }  else {
                        this.commonService.info(
                            '',
                            'No response recieved'
                        )
                    }
        
        
        
                });
            }
       
    }
    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
            $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }
    selectConfiguredModules() {
        //fetch list based on configured module selection
        // console.log( 'selected configured module' + this.ruleGroupService.ruleGroup.configuredModuleId );
        if (this.ruleGroupService.ruleGroup.apprRuleGrpObj && this.ruleGroupService.ruleGroup.apprRuleGrpObj.length > 0) {
            const dialogRef = this.dialog.open(ConfirmActionModalDialog, {

                data: {
                    resetApprovalProcessModule: 'resetApprovalProcessModule',
                    ok: 'ok',
                    no: 'NoThanks'
                },
                disableClose: true
            });
            dialogRef.afterClosed().subscribe((result) => {

                if (result && result == 'ok') {
                    this.fetchProcessesBasedOnModule();
                } else {
                    //assign previous module value
                    console.log(' this.ruleGroupService.ruleGroup.configuredModuleIdPreviousSelection' + JSON.stringify(this.ruleGroupService.ruleGroup.configuredModuleIdPreviousSelection));
                    this.ruleGroupService.ruleGroup.configuredModuleId = this.ruleGroupService.ruleGroup.configuredModuleIdPreviousSelection;
                }

            });
        }  else {
            this.fetchProcessesBasedOnModule();
        }



    }
    populateProcessBasedOnModule() {
        if (this.ruleGroupService.ruleGroup.configuredModuleName && this.ruleGroupService.ruleGroup.configuredModuleName.toLowerCase().search('recon') != -1) {

            this.ruleGroupService.fetchUnTaggedRuleGroups('RECONCILIATION').subscribe((res: any) => {
                this.groups = res.json();
                // this.groups.forEach((grp) => {
                //     grp['itemName'] = grp['name'];
                //     if(this.ruleGroupService.ruleGroup.apprRuleGrpId && (this.ruleGroupService.ruleGroup.apprRuleGrpId  == grp['id']))  {
                //         this.ruleGroupService.ruleGroup.apprRuleGrpObj = [];
                //         this.ruleGroupService.ruleGroup.apprRuleGrpObj.push(grp);
                //     }
                // });
                if (this.groups) {
                    let ind = 0;
                    this.groups.forEach((group )=> {
                        if (group.apprRuleGrpId) {
                            this.groups.splice(ind, 1);
                        }
                        ind = ind + 1;
                    });
                }
            });

        }  else if (this.ruleGroupService.ruleGroup.configuredModuleName && this.ruleGroupService.ruleGroup.configuredModuleName.toLowerCase().search('accounting') != -1) {
            this.ruleGroupService.fetchUnTaggedRuleGroups('ACCOUNTING').subscribe((res: any) => {
                this.groups = res.json();
                // this.groups.forEach((grp )=> {
                //     grp['itemName'] = grp['name'];
                // });
                if (this.groups) {
                    let ind = 0;
                    this.groups.forEach((group) => {
                        if (group.apprRuleGrpId) {
                            this.groups.splice(ind, 1);
                        }
                        ind = ind + 1;
                    });
                }
            });
        }
    }
    fetchProcessesBasedOnModule() {
        this.groups = [];
        this.ruleGroupService.ruleGroup.apprRuleGrpObj = [];
        this.ruleGroupService.approvalRuleGRoupWithRules.rules = [];
        this.ruleGroupApprovalsComponent.addNewRuleObject(0);
        this.tenantSubscribedModules.forEach((module) => {
            if (module.lookUpCode == this.ruleGroupService.ruleGroup.configuredModuleId) {
                this.ruleGroupService.ruleGroup.configuredModuleName = module.meaning;
            }
        });
        if (this.ruleGroupService.ruleGroup.configuredModuleName.toLowerCase().search('recon') != -1) {

            this.ruleGroupService.fetchUnTaggedRuleGroups('RECONCILIATION').subscribe((res: any) => {
                this.groups = res.json();
                // this.groups.forEach((grp) => {
                //     grp['itemName'] = grp['name'];
                // });
                if (this.groups) {
                    let ind = 0;
                    this.groups.forEach((group )=> {
                        if (group.apprRuleGrpId) {
                            this.groups.splice(ind, 1);
                        }
                        ind = ind + 1;
                    });
                }
            });

        } else if (this.ruleGroupService.ruleGroup.configuredModuleName.toLowerCase().search('accounting') != -1) {
            this.ruleGroupService.fetchUnTaggedRuleGroups('ACCOUNTING').subscribe((res: any) => {
                this.groups = res.json();
                // this.groups.forEach((grp) => {
                //     grp['itemName'] = grp['name'];
                // });
                if (this.groups) {
                    let ind = 0;
                    this.groups.forEach((group) => {
                        if (group.apprRuleGrpId) {
                            this.groups.splice(ind, 1);
                        }
                        ind = ind + 1;
                    });
                }
            });
        }
        this.ruleGroupService.ruleGroup.configuredModuleIdPreviousSelection = this.ruleGroupService.ruleGroup.configuredModuleId;
    }
    ngOnDestroy() {
       // this.notificationsService.remove();
        this.routeSub.unsubscribe();
    }
    setActivityBased(event) {
        console.log('event' + event);
        // if (event.checked) {
        //     this.ruleGroupService.ruleGroup.crossCurrency == true;
        // }
        // else {
        //     this.ruleGroupService.ruleGroup.crossCurrency == false;
        // }
        //console.log(' this.ruleGroupService.ruleGroup.crossCurrency ' + this.ruleGroupService.ruleGroup.crossCurrency);
    }
    validateSegmentLength() {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden', "true");
        // if (this.ruleGroupService.ruleGroup.controlAccountValue && this.ruleGroupService.ruleGroup.controlAccountValue[0] && this.ruleGroupService.ruleGroup.controlAccountValue[0][0]){

       
        //     this.ruleGroupService.ruleGroup.controlAccount = this.ruleGroupService.ruleGroup.controlAccountValue[0][0];
        // }
        // if (this.ruleGroupService.ruleGroup.realizedGainLossAccountValue && this.ruleGroupService.ruleGroup.realizedGainLossAccountValue[0] && this.ruleGroupService.ruleGroup.realizedGainLossAccountValue[0][0]){


        //     this.ruleGroupService.ruleGroup.realizedGainLossAccount = this.ruleGroupService.ruleGroup.realizedGainLossAccountValue[0][0];
        // }
        // if (this.ruleGroupService.ruleGroup.fxGainAccountValue && this.ruleGroupService.ruleGroup.fxGainAccountValue[0] && this.ruleGroupService.ruleGroup.fxGainAccountValue[0][0]){
        //     this.ruleGroupService.ruleGroup.fxGainAccount = this.ruleGroupService.ruleGroup.fxGainAccountValue[0][0];
        // }
        // if (this.ruleGroupService.ruleGroup.fxLossAccountValue && this.ruleGroupService.ruleGroup.fxLossAccountValue[0] && this.ruleGroupService.ruleGroup.fxLossAccountValue[0][0]){
        //     this.ruleGroupService.ruleGroup.fxLossAccount = this.ruleGroupService.ruleGroup.fxLossAccountValue[0][0];
        // }
        // this.commonService.info(
        //     'Cross check the Account`s segment length',
        //     ' '
        // )
    }
    setFxLossIfEmpty() {
        if (!this.ruleGroupService.ruleGroup.fxLossAccount) {
            this.ruleGroupService.ruleGroup.fxLossAccount = this.ruleGroupService.ruleGroup.fxGainAccount;
        }
    }
    fetchDataViews() {
        this.ruleGroupAccountingNewComponent.sourceDataViewsArrays = [];
        if(this.ruleGroupService.ruleGroup.reconciliationGroupId && this.ruleGroupService.ruleGroup.reconciliationGroupId!= null && this.ruleGroupService.ruleGroup.reconciliationGroupId != undefined){
        this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource(this.ruleGroupService.ruleGroup.reconciliationGroupId, '')
            .subscribe((res: any) => {
                let viewsList = [];
                viewsList = res;
                if (viewsList && viewsList.length > 0) {
                   
                    this.ruleGroupAccountingNewComponent.sourceDataViewsArrays = viewsList;
                }  else{

                }
            });
        }
    }
    changeReconcileGrpForActivityBased() {
        // var dialogRef = this.dialog.open(ConfirmActionModalDialog, {

        //     data: {refreshActivityBased:'Rules will be refreshed', ok: 'ok' },
        //     disableClose: true
        // });
        // dialogRef.afterClosed().subscribe(result => {

        //     if (result && result == 'ok') {
        const displayPopUp  = false;
        this.ruleNames = '';
        this.indexToRefresh = [];
        if (this.ruleGroupService.ruleGroup.reconciliationGroupId) {
            this.fetchDataViews();
            for (let i = 0; i < this.ruleGroupService.accountingRuleGroup.rules.length;) {

                if (this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId) {
                    this.getRuleNames(i);
                    i = i + 1;
                }  else{
                    i = i + 1;
                }
            }
        }
        //     }
        // });




    }
    /**
     * If the recon rule group is changed, recon data sources gets refreshed, if already selected recon data source exists in refreshed array, just refresh the array but not the ngmodel value
     * @param i - index of rule
     */
    getRuleNames(i) {

        this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource(this.ruleGroupService.ruleGroup.reconciliationGroupId,
            this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId).subscribe((res: any) => {
                this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange = res;
                let exists  = false;
                // console.log('reconDataSourceAfterChange::'+JSON.stringify( this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange));
                if (this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange &&
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange.length > 0) {
                    if (this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange.length == 1 &&
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange[0] &&
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange[0]['failed']) {
                        //this.ruleGroupService.reconDataSourceArrays[i]=[];
                        // this.commonService.info(
                        //     'Eligible data sets not found.',
                        //     ' '
                        // )
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange = [];
                    }  else  {
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange.forEach((dv) => {
                            dv['itemName'] = dv['DataViewDispName'];
                            if (this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId != null) {
                                if (this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId == dv.id) {
                                    exists = true;
                                }
                            }
                        });
                    }
                       
                    if (exists) {

                    } else {
                        if (i == 0) {
                            if (this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode){
                                this.ruleNames = this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode;
                            }
                            this.indexToRefresh.push(i);
                            if (i == this.ruleGroupService.accountingRuleGroup.rules.length - 1){
                                this.displayMessage(this.ruleNames, this.indexToRefresh, this.ruleGroupService.ruleGroup.reconciliationGroupId);
                            }
                        }   else {
                            if (this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode){
                                this.ruleNames = this.ruleNames + ', ' + this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode;
                            }
                            this.indexToRefresh.push(i);
                            if (i == this.ruleGroupService.accountingRuleGroup.rules.length - 1){
                                this.displayMessage(this.ruleNames, this.indexToRefresh, this.ruleGroupService.ruleGroup.reconciliationGroupId);
                            }

                        }

                    }
                }   else {


                }

            });
    }
    displayMessage(ruleNames, indexToRefresh: any, selectedReconGroupId) {
        let msg = 'Recon group changed. ';
        msg = msg + 'previous selections gets refreshed';
        const dialogRef = this.dialog.open(ConfirmActionModalDialog, {

            data: { refreshViews: 'refreshViews', ruleNames: {ruleNames}, message: msg, ok: 'ok', no: 'No thanks' },
            disableClose: true
        });
        dialogRef.afterClosed().subscribe((result) => {

            if (result && result == 'ok') {
                for (let i = 0; i < this.ruleGroupService.accountingRuleGroup.rules.length; i++) {
                    //if (!this.ruleGroupService.reconDataSourceArrays[indexToRefresh[i]])
                    //   this.ruleGroupService.reconDataSourceArrays[indexToRefresh[i]] = [];
                    let selectedDV: any;
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = null;
                    if (this.indexToRefresh.indexOf(i) != -1) {
                        //console.log('exists in indextorefresh'+i);
                    } else {
                        selectedDV = {
                            "id": this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId,
                            "itemName": this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName
                        };
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = [];
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId.push(selectedDV);
                    }


                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId = null;
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceName = null;
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays = [];
                    this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays =
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceAfterChange;
                    // console.log('this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays  '+JSON.stringify(this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceArrays  ));



                }
            }    else {
                this.ruleGroupService.ruleGroup.reconciliationGroupId = this.previousGroupId;


            }
            this.previousGroupId = new Number(selectedReconGroupId);

        });
    }

    setPriority() {
        this.ruleGroupService.rulePriorityList = [];
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
            if (this.isCreateNew) {
                let indx = 0;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach((rule) => {
                    rule.rule.priority = indx + 1;
                    const obj = {
                        "ind": indx,
                        "priority": indx + 1,
                        "ruleName": rule.rule.ruleCode,
                        "assignmentId": rule.rule.ruleGroupAssignId
                    };
                    this.ruleGroupService.rulePriorityList.push(obj);
                    indx = indx + 1;
                });
            }  else {
                let indx = 0;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach((rule) => {
                    rule.rule.priority = indx + 1;
                    const obj = {
                        "ind": indx,
                        "priority": rule.rule.priority,
                        "ruleName": rule.rule.ruleCode,
                        "assignmentId": rule.rule.ruleGroupAssignId
                    };
                    this.ruleGroupService.rulePriorityList.push(obj);
                    indx = indx + 1;
                });
            }
        }  else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            if (this.isCreateNew) {
                let indx = 0;
                this.ruleGroupService.accountingRuleGroup.rules.forEach((rule) => {
                    rule.priority = indx + 1;
                    const obj = {
                        "ind": indx,
                        "priority": indx + 1,
                        "ruleName": rule.ruleCode,
                        "assignmentId": rule.ruleGroupAssignId
                    };
                    this.ruleGroupService.rulePriorityList.push(obj);
                    indx = indx + 1;
                });
            }   else {
                let indx = 0;
                this.ruleGroupService.accountingRuleGroup.rules.forEach((rule )=> {
                    rule.priority = indx + 1;
                    const obj = {
                        "ind": indx,
                        "priority": rule.priority,
                        "ruleName": rule.ruleCode,
                        "assignmentId": rule.ruleGroupAssignId
                    };
                    this.ruleGroupService.rulePriorityList.push(obj);
                    indx = indx + 1;
                });
            }
        }    else if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            if (this.isCreateNew) {
                let indx = 0;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach((rule) => {
                    rule.priority = indx + 1;
                    const obj = {
                        "ind": indx,
                        "priority": indx + 1,
                        "ruleName": rule.ruleCode,
                        "assignmentId": rule.ruleGroupAssignId
                    };
                    this.ruleGroupService.rulePriorityList.push(obj);
                    indx = indx + 1;
                });
            }  else {
                let indx = 0;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach((rule) => {
                    rule.priority = indx + 1;
                    const obj = {
                        "ind": indx,
                        "priority": rule.priority,
                        "ruleName": rule.ruleCode,
                        "assignmentId": rule.ruleGroupAssignId
                    };
                    this.ruleGroupService.rulePriorityList.push(obj);
                    indx = indx + 1;
                });
            }
        }

    }
    updatePriority() {
        console.log('event in update priority' + event);
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
            let indx = 1;
            this.ruleGroupService.rulePriorityList.forEach((priorityObj) => {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[priorityObj.ind].rule.priority = indx;
                priorityObj.priority = indx;
                if (this.ruleGroupService.rulePriorityList.length == indx) {
                    if (!this.isViewOnly) {
                        // this.saveRuleGroup();
                    } else{

                    
                        this.ruleGroupService.updateRulesPriority(this.ruleGroupService.rulePriorityList).subscribe((res: any) => {
                            const resp: any = res;
                            //console.log('saved or not ' + JSON.stringify(resp));
                            this.setRulePriority = false;
                            if (resp.updated == 'success') {
                                this.commonService.success(
                                    '','Priorities updated successfully'
                                    
                                )
                                if(this.urlValue.endsWith('ruleDetails')){
                                    this.router.navigate(['/rule-group', { outlets: { 'content': this.ruleGroupService.ruleGroup.id + '/details' } }]);
                                }else{
                                    this.router.navigate(['/rule-group', { outlets: { 'content': this.ruleGroupService.ruleGroup.id + '/ruleDetails' } }]);
                                }
                                
                            } else {
                                this.commonService.error(
                                '',    'Priorities updation failed'
                                
                                )
                            }
                        });
                    }
                }
                indx = indx + 1;

            });

        }  else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            let indx = 1;
            this.ruleGroupService.rulePriorityList.forEach((priorityObj )=> {
                this.ruleGroupService.accountingRuleGroup.rules[priorityObj.ind].priority = indx;
                priorityObj.priority = indx;
                if (this.ruleGroupService.rulePriorityList.length == indx) {
                    if (!this.isViewOnly) {
                        // this.saveRuleGroup();
                    } else{
                        this.ruleGroupService.updateRulesPriority(this.ruleGroupService.rulePriorityList).subscribe((res: any) => {
                            const resp: any = res;
                            //console.log('saved or not ' + JSON.stringify(resp));
                            this.setRulePriority = false;
                            if (resp.updated == 'success') {
                                this.commonService.success(
                                   '', 'Priorities updated successfully'
                                    
                                )
                                if(this.urlValue.endsWith('ruleDetails')){
                                    this.router.navigate(['/rule-group', { outlets: { 'content': this.ruleGroupService.ruleGroup.id + '/details' } }]);
                                }else{
                                    this.router.navigate(['/rule-group', { outlets: { 'content': this.ruleGroupService.ruleGroup.id + '/ruleDetails' } }]);
                                }
                            } else {
                                this.commonService.error(
                                    '','Priorities updation failed',
                                    
                                )
                            }
                        });
                    }
                      
                }
                indx = indx + 1;

            });
        } else if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            let indx = 1;
            this.ruleGroupService.rulePriorityList.forEach((priorityObj )=> {
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[priorityObj.ind].priority = indx;
                priorityObj.priority = indx;
                if (this.ruleGroupService.rulePriorityList.length == indx) {
                    if (!this.isViewOnly) {
                        // this.saveRuleGroup();
                    } else{
                        this.ruleGroupService.updateRulesPriority(this.ruleGroupService.rulePriorityList).subscribe((res: any) => {
                            const resp: any = res;
                            // console.log('saved or not ' + JSON.stringify(resp));
                            this.setRulePriority = false;
                            if (resp.updated == 'success') {
                                this.commonService.success(
                                    '','Priorities updated successfully',
                                    
                                )
                                if(this.urlValue.endsWith('ruleDetails')){
                                    this.router.navigate(['/rule-group', { outlets: { 'content': this.ruleGroupService.ruleGroup.id + '/details' } }]);
                                }else{
                                    this.router.navigate(['/rule-group', { outlets: { 'content': this.ruleGroupService.ruleGroup.id + '/ruleDetails' } }]);
                                }
                            } else {
                                this.commonService.error(
                                    '','Priorities updation failed',
                                    
                                )
                            }


                        });
                    }
                       
                }
                indx = indx + 1;

            });
        }

    }
    SelectCOA(mode?: any) {
        this.valueSetForControlAcc = [];
        this.valueSetForRealizedAcc = [];
        this.valueSetForFXGainAcc = [];
        this.valueSetForFXLossAcc = [];
        this.ruleGroupService.ruleGroup.controlAccountValue = null;
        this.ruleGroupService.ruleGroup.realizedGainLossAccountValue = null;
        this.ruleGroupService.ruleGroup.fxGainAccountValue = null;
        this.ruleGroupService.ruleGroup.fxLossAccountValue = null;
        this.segmentsService.fetchSegmentsByCOAOrderBySequence(this.ruleGroupService.ruleGroup.coa, 'NATURAL_ACCOUNT').subscribe((res: any) => {
            const resp: any = res;
            if (resp.segments && resp.segments[0] && resp.segments[0].valueSet)   {
                this.valueSetForControlAcc = resp.segments[0].valueSet;
            }
              
            if (this.valueSetForControlAcc && this.valueSetForControlAcc.length > 0) {
                // let ind : number = 0;
                for (let i = 0; i < this.valueSetForControlAcc.length; i++) {
                    this.valueSetForControlAcc[i]['itemName'] = this.valueSetForControlAcc[i][1];
                    this.valueSetForControlAcc[i]['id'] = this.valueSetForControlAcc[i][0];

                    // if (this.isEdit || this.copyRuleGroup) {
                    //     if (this.ruleGroupService.ruleGroup.crossCurrency) {
                    //         if (this.valueSetForControlAcc[i]['id'] == this.ruleGroupService.ruleGroup.controlAccount) {
                    //             const val = {
                    //                 "id": this.valueSetForControlAcc[i].id
                    //                 , "itemName": this.valueSetForControlAcc[i]['itemName']
                    //             };
                    //             this.ruleGroupService.ruleGroup.controlAccountValue = [];
                    //             this.ruleGroupService.ruleGroup.controlAccountValue.push(val);
                    //         }

                    //         if (this.valueSetForControlAcc[i]['id'] == this.ruleGroupService.ruleGroup.realizedGainLossAccount) {
                    //             const val = {
                    //                 "id": this.valueSetForControlAcc[i].id
                    //                 , "itemName": this.valueSetForControlAcc[i]['itemName']
                    //             };
                    //             this.ruleGroupService.ruleGroup.realizedGainLossAccountValue = [];
                    //             this.ruleGroupService.ruleGroup.realizedGainLossAccountValue.push(val);
                    //         }

                    //         if (this.valueSetForControlAcc[i]['id'] == this.ruleGroupService.ruleGroup.fxGainAccount) {
                    //             const val = {
                    //                 "id": this.valueSetForControlAcc[i].id
                    //                 , "itemName": this.valueSetForControlAcc[i]['itemName']
                    //             };
                    //             this.ruleGroupService.ruleGroup.fxGainAccountValue = [];
                    //             this.ruleGroupService.ruleGroup.fxGainAccountValue.push(val);
                    //         }

                    //         if (this.valueSetForControlAcc[i]['id'] == this.ruleGroupService.ruleGroup.fxLossAccount) {
                    //             const val = {
                    //                 "id": this.valueSetForControlAcc[i].id
                    //                 , "itemName": this.valueSetForControlAcc[i]['itemName']
                    //             };
                    //             this.ruleGroupService.ruleGroup.fxLossAccountValue = [];
                    //             this.ruleGroupService.ruleGroup.fxLossAccountValue.push(val);
                    //         }
                    //     }
                    // }
                    
                }
                this.valueSetForRealizedAcc = jQuery.extend(true, [], this.valueSetForControlAcc);
                this.valueSetForFXGainAcc = jQuery.extend(true, [], this.valueSetForControlAcc);
                this.valueSetForFXLossAcc = jQuery.extend(true, [], this.valueSetForControlAcc);

                //  console.log('this.valueSetForAcc' + JSON.stringify(this.valueSetForControlAcc));
            } else {
                //     if(this.ruleGroupService.ruleGroup.crossCurrency ==  true)
                //     this.ruleGroupService.ruleGroup.crossCurrency =false;
                this.commonService.error(
                    ' Value set is not tagged to the account segment',
                    ''
                )

            }

            this.ruleGroupAccountingNewComponent.fetchAccountingRuleList(this.ruleGroupService.ruleGroup.coa);
            if (this.ruleGroupService.accountingRuleGroup && this.ruleGroupService.accountingRuleGroup.rules && this.ruleGroupService.accountingRuleGroup.rules.length > 0) {
                for (let i = 0; i < this.ruleGroupService.accountingRuleGroup.rules.length; i++) {
                    this.ruleGroupService.accountingRuleGroup.rules[i].coa = this.ruleGroupService.ruleGroup.coa;
                    this.ruleGroupService.accountingRuleGroup.existingRuleListLOV = null;
                    this.ruleGroupService.accountingRuleGroup.rules[i].copiedRefId = null;
                }
                //                this.ruleGroupAccountingNewComponent.fetchSegmentsAndLedgers(this.ruleGroupService.ruleGroup.coa);
                this.ruleGroupAccountingNewComponent.refreshWithCOA(this.ruleGroupService.ruleGroup.coa, mode);
            }

        });

    }

    filterViews() {
        this.ruleGroupApprovalsComponent.fetchDataViewsByRuleGroup();
    }
    startDateChanged(dt: Date) {
        if (this.ruleGroupService.ruleGroup.endDate) {
            if (this.ruleGroupService.ruleGroup.endDate < this.ruleGroupService.ruleGroup.startDate) {
                this.ruleGroupService.ruleGroup.endDate = this.ruleGroupService.ruleGroup.startDate;
            }
        }
    }

    activityBasedCheckFunction(event) {
        //console.log('after activity based check value is '+event);
      //  if (this.editRuleGroup) {
            let processType = '';
            if (event.checked == true) {
                processType = 'Reset all rules and proceed the process as activity based';
                const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    data: { refreshActivityBased: processType, ok: 'ok', no: 'No thanks' },
                    disableClose: true
                });
                dialogRef.afterClosed().subscribe((result) => {
                    if (result && result.search('No') != -1) {
                        this.ruleGroupService.ruleGroup.activityBased = false;
                        event.checked = false;
                    } else {
                        this.ruleGroupService.ruleGroup.activityBased = true;
                        this.ruleGroupService.accountingRuleGroup.rules = [];
                        this.ruleGroupAccountingNewComponent.addNewRuleObject(0, 0);
                        this.ruleGroupAccountingNewComponent.fetchAccountingRuleList(this.ruleGroupService.ruleGroup.coa);
                    }
                });

            }   else {
                //reset rule or dependent fields
                processType = 'Reset all rules and proceed the process as non-activity based';
                const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    data: { refreshActivityBased: processType, ok: 'ok', no: 'No thanks' },
                    disableClose: true
                });
                dialogRef.afterClosed().subscribe((result )=> {
                    if (result && result.search('No') != -1) {
                        this.ruleGroupService.ruleGroup.activityBased = true;
                    } else {
                        this.ruleGroupService.ruleGroup.activityBased = false;
                        event.checked = false;
                        this.ruleGroupService.ruleGroup.reconciliationGroupId = null;
                        this.ruleGroupAccountingNewComponent.sourceDataViewsArrays = [];
                        this.ruleGroupAccountingNewComponent.fetchDataViews();
                        if (this.ruleGroupService.accountingRuleGroup && this.ruleGroupService.accountingRuleGroup.rules && this.ruleGroupService.accountingRuleGroup.rules.length > 0) {
                            this.ruleGroupService.accountingRuleGroup.rules = [];
                            this.ruleGroupAccountingNewComponent.addNewRuleObject(0, 0);
                            this.ruleGroupAccountingNewComponent.fetchAccountingRuleList(this.ruleGroupService.ruleGroup.coa);
                            //for (var c = 0; c < this.ruleGroupService.accountingRuleGroup.rules.length; c++) {
                            // this.ruleGroupService.accountingRuleGroup.rules[c].reconDataSourceId = null;
                            // this.ruleGroupService.accountingRuleGroup.rules[c].reconSourceDVId = null;
                            // this.ruleGroupService.accountingRuleGroup.rules[c].reconDataSourceName = null;
                            //}
                        }
                    }

                });
            }
      //  }
    }
    crossCurrencyCheckFunction(event) {
      //  if (this.editRuleGroup) {
            let processType = '';
            if (event.checked == true) {
                // this.ruleGroupService.ruleGroup.crossCurrency = null;
                processType = 'Are you sure to enable Cross currency';
                const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    data: { refreshActivityBased: processType, ok: 'ok', no: 'No thanks' },
                    disableClose: true
                });
                dialogRef.afterClosed().subscribe((result) => {
                    if (result && result.search('No') != -1) {
                        this.ruleGroupService.ruleGroup.crossCurrency = false;
                        event.checked = false;
                    } else {
                        this.ruleGroupService.ruleGroup.crossCurrency = true;
                        this.SelectCOA('populate');
                    }
                });

            } else {
                // this.ruleGroupService.ruleGroup.crossCurrency = true;
                processType = 'Are you sure to reset Cross currency fields and disable Cross currency';
                const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    data: { refreshActivityBased: processType, ok: 'ok', no: 'No thanks' },
                    disableClose: true
                });
                dialogRef.afterClosed().subscribe((result) => {
                    if (result && result.search('No') != -1) {
                        this.ruleGroupService.ruleGroup.crossCurrency = true;
                    } else {
                        this.ruleGroupService.ruleGroup.crossCurrency = false;
                        event.checked = false;
                        this.ruleGroupService.ruleGroup.controlAccount = null;
                        this.ruleGroupService.ruleGroup.controlAccountValue = null;
                        this.ruleGroupService.ruleGroup.realizedGainLossAccount = null;
                        this.ruleGroupService.ruleGroup.realizedGainLossAccountValue = null;
                        this.ruleGroupService.ruleGroup.fxGainAccount = null;
                        this.ruleGroupService.ruleGroup.fxGainAccountValue = null;
                        this.ruleGroupService.ruleGroup.fxLossAccount = null;
                        this.ruleGroupService.ruleGroup.fxLossAccountValue = null;
                    }

                });
            }
        // }
        // else {
        //     if (event.checked == false) {
        //         this.ruleGroupService.ruleGroup.crossCurrency = false;
        //         event.checked = false;
        //         this.ruleGroupService.ruleGroup.controlAccount = null;
        //         this.ruleGroupService.ruleGroup.controlAccountValue = null;
        //         this.ruleGroupService.ruleGroup.realizedGainLossAccount = null;
        //         this.ruleGroupService.ruleGroup.realizedGainLossAccountValue = null;
        //         this.ruleGroupService.ruleGroup.fxGainAccount = null;
        //         this.ruleGroupService.ruleGroup.fxGainAccountValue = null;
        //         this.ruleGroupService.ruleGroup.fxLossAccount = null;
        //         this.ruleGroupService.ruleGroup.fxGainAccountValue = null;
        //     }
        // }
    }
    multiCurrencycheckFunction(event) {
        if (event.checked == false) {
            this.ruleGroupService.ruleGroup.conversionDate = null;
            this.ruleGroupService.ruleGroup.fxRateId = null;
        }else{ 
            if(!  this.conversionTypes ||   this.conversionTypes.length<=0){
                var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                    data: {noFxRate:'noFxRate' ,message: 'Please create Exchange rates to define Accounting rule group' },
                    disableClose: true
                   });
                   this.router.navigate(['/rule-group', { outlets: {'content': ['ACCOUNTING']+'/list'} }]);
            }
        }
        if (!this.isCreateNew && !this.isViewOnly) {
            //reset rule or dependent fields
        }
    }   
    filterViewsBasedOnModule() {
        console.log('this.ruleGroupService.ruleGroup.apprRuleGrpObj[0]' + this.ruleGroupService.ruleGroup.apprRuleGrpId);
        this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource(this.ruleGroupService.ruleGroup.apprRuleGrpId, '')
            .subscribe((res: any) => {
                let viewsList = [];
                viewsList = res;
                if (viewsList && viewsList.length > 0) {
                    this.ruleGroupApprovalsComponent.sourceDataViewsAndColumns = viewsList;
                }else{
                    this.commonService.error(
                        'Sorry! No data sources found',
                        ''
                    )
                }
            });
    }
    getFilteredRules(){
        this.rulesService.getRules(this.ruleGroupService.ruleGroup.rulePurpose,null,this.ruleGroupService.ruleGroup.apprRuleGrpId).subscribe((res: any) => {
            let rulesFetched: Rules[] = [];
            rulesFetched = res;
            for(let c=0;c<rulesFetched.length;c++){
                rulesFetched[c]['itemName'] = rulesFetched[c].ruleCode;
            }
            this.ruleGroupService.approvalRuleGRoupWithRules.existingRuleListLOV=[];
            this.ruleGroupService.approvalRuleGRoupWithRules.existingRuleListLOV=rulesFetched;
        });
    }
    setProcess(group) {
        console.log('group has'+JSON.stringify(group));
        if(this.ruleGroupService.ruleGroup.previousApprRuleGrpId ){
            const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                data: {
                    resetAppGrp: 'resetAppGrp',
                    ok: 'ok',
                    no: 'NoThanks'
                },
                disableClose: true
            });
            dialogRef.afterClosed().subscribe((result) => {
                if (result && result == 'ok') {
                      //this.ruleGroupService.ruleGroup.apprRuleGrpId = group.id;
                    //  this.ruleGroupService.ruleGroup.apprRuleGrpName=group.itemName;
                     this.filterViewsBasedOnModule();
                     this.getFilteredRules();

                     this.ruleGroupService.approvalRuleGRoupWithRules.rules=[];
                     this.ruleGroupApprovalsComponent.addNewRuleObject(0);
                     this.ruleGroupService.ruleGroup.previousApprRuleGrpId = this.ruleGroupService.ruleGroup.apprRuleGrpId ;
                } else {
                 //   this.ruleGroupService.ruleGroup.apprRuleGrpObj=[];
                    //this.ruleGroupService.ruleGroup.apprRuleGrpObj.push(obj);
                     this.ruleGroupService.ruleGroup.apprRuleGrpId =  this.ruleGroupService.ruleGroup.previousApprRuleGrpId;
                    }
               
            });
        } else{
            //this.ruleGroupService.ruleGroup.apprRuleGrpId = group.id;
            this.filterViewsBasedOnModule();
          //  this.ruleGroupService.ruleGroup.apprRuleGrpName=group.itemName;
            this.getFilteredRules();
            this.ruleGroupService.ruleGroup.previousApprRuleGrpId = this.ruleGroupService.ruleGroup.apprRuleGrpId ;
        }
    }
    // setProcess(group) {
    //     $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden', "true");
    //     if(this.ruleGroupService.ruleGroup.apprRuleGrpId ){
    //         const obj={
    //             "id":this.ruleGroupService.ruleGroup.apprRuleGrpId ,
    //             "itemName":this.ruleGroupService.ruleGroup.apprRuleGrpName
    //         };
    //         const dialogRef = this.dialog.open(ConfirmActionModalDialog, {
    //             data: {
    //                 resetAppGrp: 'resetAppGrp',
    //                 ok: 'ok',
    //                 no: 'NoThanks'
    //             },
    //             disableClose: true
    //         });
    //         dialogRef.afterClosed().subscribe((result) => {

    //             if (result && result == 'ok') {
    //                   this.ruleGroupService.ruleGroup.apprRuleGrpId = group.id;
    //                   this.ruleGroupService.ruleGroup.apprRuleGrpName=group.itemName;
    //                  this.filterViewsBasedOnModule();
    //                  this.getFilteredRules();

    //                  this.ruleGroupService.approvalRuleGRoupWithRules.rules=[];
    //                  this.ruleGroupApprovalsComponent.addNewRuleObject(0);
    //             } else {
    //                 this.ruleGroupService.ruleGroup.apprRuleGrpObj=[];
    //                 this.ruleGroupService.ruleGroup.apprRuleGrpObj.push(obj);
    //                  this.ruleGroupService.ruleGroup.apprRuleGrpId = obj.id;
    //                 this.ruleGroupService.ruleGroup.apprRuleGrpName=obj.itemName;
    //             }

    //         });
    //     } else{
    //         this.ruleGroupService.ruleGroup.apprRuleGrpId = group.id;
    //         this.filterViewsBasedOnModule();
    //         this.ruleGroupService.ruleGroup.apprRuleGrpName=group.itemName;
    //         this.getFilteredRules();
    //     }

    // }

    /*Author: Bhagath */
    routingWithCondition() {
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
            if (this.commonService.reconToRule == true) {
                this.router.navigate(['/reconcile', { outlets: { 'content': ['reconcile-list'] } }]);
                this.commonService.reconToRule = false;
            } else {
                this.router.navigate(['/rule-group', { outlets: { 'content': [this.ruleGroupService.ruleGroup.rulePurpose] + '/list' } }]);
                this.commonService.reconToRule = false;
            }
        } else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            if (this.commonService.acctToRule == true) {
                this.router.navigate(['/accounting-data-wq', { outlets: { 'content': ['accounting-data-wq-home'] } }]);
                this.commonService.acctToRule = false;
            } else {
                this.router.navigate(['/rule-group', { outlets: { 'content': [this.ruleGroupService.ruleGroup.rulePurpose] + '/list' } }]);
                this.commonService.acctToRule = false;
            }
        } else {
            this.router.navigate(['/rule-group', { outlets: { 'content': [this.ruleGroupService.ruleGroup.rulePurpose] + '/list' } }]);
            this.commonService.acctToRule = false;
            this.commonService.reconToRule = false;
        }

    }
  
}
