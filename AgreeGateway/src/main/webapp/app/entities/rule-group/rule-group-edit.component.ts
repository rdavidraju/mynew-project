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
import { NotificationsService } from 'angular2-notifications-lite';
import { SessionStorageService } from 'ng2-webstorage';
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
    today=new Date();  
    startDateChange: boolean = false;
    endDateChange: boolean = false;   
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
    @ViewChild(NguiPopupComponent) popup: NguiPopupComponent;
    @ViewChild(RuleGroupAccountingNewComponent) ruleGroupAccountingNewComponent: RuleGroupAccountingNewComponent;
    @ViewChild(RuleGroupReconciliationComponent) ruleGroupReconciliationComponent: RuleGroupReconciliationComponent;
    @ViewChild(RuleGroupApprovalsComponent) ruleGroupApprovalsComponent: RuleGroupApprovalsComponent;
    GroupTypePlaceHolder: any;
    ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
    @Input() saveButton: boolean = false;
    myData: any = '';
    filterAction: any = '';
    duplicateRuleGroupName: boolean = false;
    ruleListformArray: FormGroup[] = [];
    targetDataViewformsArray: FormGroup[] = [];
    srcDataViewformsArray: FormGroup[] = [];

    srcColumnListformsArray: FormGroup[] = [];
    targetColumnListformsArray: FormGroup[] = [];

    selectedSrcDataViewAndColumns = [];
    selectedTargetDataViewAndColumns = [];


    logicalOperatorLOV = [];

    expandTab: boolean[] = [];
    showOptions: boolean = false;
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

    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;

    create: string = 'create';
    edit: string = 'edit';
    view: string = 'view';

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
    open: boolean = true;
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
    FilterTitle: string = 'Filters';
    FormulaTitle: string = 'Formulae';
    ToleranceTitle: string = 'Tolerance ';
    additionalFilters: string = '  Additional filters ';
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
    isVisibleA: boolean = false;
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
    indexToRefresh =[];
    reconDataSourceAfterChange = [];
    ruleNames:string = '' ; 
    previousGroupId : any;
    setRulePriority:boolean=false;
    chartOfAccounts =[];
    ruless=[
        {
            "id" : 0,
            "ruleName":"r1"
        },
        {
            "id" : 1,
            "ruleName":"r2"
        },
        {
            "id" : 21,
            "ruleName":"r3"
        },
        {
            "id" : 3,
            "ruleName":"r4"
        },
        {
            "id" : 4,
            "ruleName":"r5"
        },
    ];
    valueSetForControlAcc = [];
    valueSetForRealizedAcc = [];
    valueSetForFXGainAcc = [];
    valueSetForFXLossAcc = [];

    constructor(

        private builder: FormBuilder,
        private config: NgbDatepickerConfig,
        private router: Router,
        private route: ActivatedRoute,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private lookUpCodeService: LookUpCodeService,
        private dataViewsService: DataViewsService,
        private rulesService: RulesService,
        public ruleGroupService: RuleGroupService,
        private _sanitizer: DomSanitizer,
        private notificationsService: NotificationsService,
        private $sessionStorage: SessionStorageService,
        public dialog: MdDialog,
        public fxRatesService: FxRatesService,
        private chartOfAccountService: ChartOfAccountService,
        private segmentsService: SegmentsService,


    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
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



    ngOnInit() {
        this.ruleGroupTypes=[];
        //console.log( 'in ngonnt of edit comp' );
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) + 'px'
        });
        this.ruleGroupService.fetchRuleGroupsBypurpose('Reconciliation').subscribe(
            (res: any) => {
                this.ruleGroupList = res;
                //   console.log(' ruleGroupList ' + JSON.stringify(this.ruleGroupList));
            });
        this.chartOfAccountService.getChartOfAccountsByTenant().subscribe((res: any) => {
            this.chartOfAccounts = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('ACCOUNTING_TYPE').subscribe((res: any) => {
            this.accountingTypes = res;
            // console.log(' this.accountingTypes' + JSON.stringify(this.accountingTypes));
        });
        this.fxRatesService.getAllFxRatesByTenant().subscribe((res: any) => {
            this.conversionTypes = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('GL_DATES_DERIVATION').subscribe((res: any) => {
            this.conversionDates = res;
        });

        this.commonService.callFunction();
        this.routeSub = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            if (params['id']) {
                this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                // detail or edit

                if (url.endsWith('edit')) {
                    this.isEdit = true;
                } else {
                    this.view = this.view + '-' + params['id'];
                   
                    this.isViewOnly = true;

                }
            } else {
                this.ruleGroupService.approvalRuleGRoupWithRules = new ApprovalRuleGRoupWithRules();
                this.ruleGroupService.ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
                this.ruleGroupService.accountingRuleGroup = new AccountingRuleGroup();
                this.ruleGroupService.ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
                this.ruleGroupService.ruleGroup = new RuleGroup();
                console.log('this.ruleGroupService.routerRuleGroupType'+this.ruleGroupService.routerRuleGroupType);
                this.ruleGroupService.ruleGroup.rulePurpose = this.ruleGroupService.routerRuleGroupType;
                let obj :any;
                this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((res: any) => {
                    let ruleGroupTypes = [];
                    this.ruleGroupTypes=[];
                    ruleGroupTypes = res;
                    let ind : number =0;
                    
                    ruleGroupTypes.forEach(rgType => {
                        let rgTypeObj = { "code": rgType.meaning, "name": rgType.description, "setValue": rgType.lookUpCode};
                        if(this.ruleGroupService.routerRuleGroupType == rgType.lookUpCode)
                        {
                              rgTypeObj['value']=true;
                            obj=rgTypeObj;
                            
                        }
                      
                        else
                        rgTypeObj['value']=false;
                        let code :string = '';
                        code  = rgType.meaning;
                        code = code.substr(0,3);
                        rgTypeObj.code = code;
                        this.ruleGroupTypes.push(rgTypeObj);
                        
                          if(ruleGroupTypes.length-1== ind){
                              console.log('ruleGroupTypes.length'+ruleGroupTypes.length+'ind'+ind);
                        this.SelectRuleGroupType(obj);
                          this.startEndDateClass();
                                this.isCreateNew = true;
                              this.isVisibleA = true;
                            //   $('.split-example .right-component').css('width','100% !important');
                            }
                        ind=ind+1;
                        
                      
                    });
                });
              
                //this.SelectRuleGroupType(obj);
                   //       this.startEndDateClass();
              
            }
        });

    }
    startEndDateClass() {
        if (this.ruleGroupService.ruleGroup.startDate != null) {
            // return 'col-md-3 col-sm-6 col-xs-12 form-group';
            if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
                this.nameLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
                this.rulePurposeLength = 'col-md-9 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            }
            else {
                this.nameLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
                this.rulePurposeLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            }


        } else {
            //return 'col-md-4 col-sm-6 col-xs-12 form-group';
            if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
                this.rulePurposeLength = 'col-md-9 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            }
            else {
                this.nameLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
                this.rulePurposeLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
                this.dateLength = 'col-md-4 col-sm-6 col-xs-12 form-group';
            }

        }
    }

    SelectRuleGroupType(ruleGrpType?:any) {
        if( ruleGrpType == undefined || !ruleGrpType || ruleGrpType=='')
        {
            console.log('went to undefined');
            this.ruleGroupTypes.forEach(rgType => {
                if(rgType.setValue == this.ruleGroupService.routerRuleGroupType)
                rgType.value=true;
                else
                rgType.value=false;
            });
         console.log('ruleGroupTypes after undefuned'+JSON.stringify(this.ruleGroupTypes));
            this.ruleGroupService.ruleGroup = new RuleGroup();
        }
        else
        {
            this.ruleGroupService.ruleGroup = new RuleGroup();
            this.ruleGroupService.ruleGroup.rulePurpose = ruleGrpType.setValue;
            let alreadySelected :number=0;
            this.ruleGroupTypes.forEach(rgType => {
                if(rgType.value == true)
                alreadySelected = 1;
            });
            this.ruleGroupTypes.forEach(rgType => {
    
                if (ruleGrpType.setValue == rgType.setValue)
                {
                    //ruleType.value = ruleType.value ? false : true;
                    if(rgType.value == true)
                    {
    
    
                    }
                    else{
                        rgType.value=true;
                        
                    }
                }
                    
                else
                {
                    
                    rgType.value = false;
                }
                    
            });
        }

        if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            this.ruleGroupService.getTenantConfigModules().subscribe((res: any) => {
                this.tenantSubscribedModules = res;
                let indexVal: any = 0;
                if (this.tenantSubscribedModules)
                    this.tenantSubscribedModules.forEach(module => {
                        if (module.modules.search('_APPROVALS') != -1) {
                            this.tenantSubscribedModules.splice(indexVal, 1);
                        }
                        else {
                        }
                        indexVal = indexVal + 1;
                    });
            });
            
            //this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS';
            //this.ruleGroupService.instantiateObjects();
        }
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            this.displayAccountingType = 'col-md-3';
            this.nameLength = 'col-md-3 col-sm-6 col-xs-12 form-group';
            this.rulePurposeLength = 'col-md-6 col-sm-6 col-xs-12 form-group';
            this.startEndDateClass();
            //this.ruleGroupService.ruleGroup = new RuleGroup();
           // this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING';
          //  this.ruleGroupService.instantiateObjects();
           
        }
        else {
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
    setPlaceHolder() {
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
            this.GroupTypePlaceHolder = 'Reconciliation Process Name';
        }
        else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            this.GroupTypePlaceHolder = 'Accounting Process Name';
        }
        else if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            this.GroupTypePlaceHolder = 'Approvals Process Name';
        }
        else {
            this.GroupTypePlaceHolder = 'Process Name';
        }
    }
    fetchGroupAndRulesAndRuleConditionsByGroupId(groupId: any) {

        this.ruleGroupService.fetchRuleGroupObject(groupId).subscribe((res: any) => {
            this.ruleGroupService.ruleGroup = res;
            this.ruleGroupTypes=[];
            console.log('jsonResponse for rule object'+JSON.stringify( this.ruleGroupService.ruleGroup ));
        //    for(var i=0;i< this.ruleGroupTypes.length;i++)
        //     {
        //         if(this.ruleGroupService.ruleGroup .rulePurpose == this.ruleGroupTypes[i].setValue)
        //         {
        //             this.ruleGroupTypes[i].value=true;
        //         }
        //     }
            this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((res: any) => {
                let ruleGroupTypes = [];
                ruleGroupTypes = res;
               
                for(var ind = 0;ind<ruleGroupTypes.length;ind++)
                {
                    let rgTypeObj = { "code": ruleGroupTypes[ind].meaning, "name": ruleGroupTypes[ind].description, "setValue": ruleGroupTypes[ind].lookUpCode};
                    if(this.ruleGroupService .ruleGroup.rulePurpose  == ruleGroupTypes[ind].lookUpCode)
                    rgTypeObj['value']=true;
                    else
                    rgTypeObj['value']=false;
                    let code :string = '';
                    code  = ruleGroupTypes[ind].meaning;
                    code = code.substr(0,3);
                    rgTypeObj.code = code;
                    this.ruleGroupTypes.push(rgTypeObj);
                }
            });
            console.log('response from group details on april s'+JSON.stringify( this.ruleGroupService.ruleGroup));
            if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
                this.displayAccountingType = 'col-md-6';
            }
            this.setPlaceHolder();
        });
    }
    checkGroupName(name: string) {
        this.ruleGroupService.query().subscribe(
            (res: Response) => {
                this.ruleGroupsList = res.json();
                let count: number = 0;
                if (this.ruleGroupsList)
                    this.ruleGroupsList.forEach(ruleGroup => {
                        if (ruleGroup.name.toLowerCase() == this.ruleGroupService.ruleGroup.name.toLowerCase()) {
                            count = count + 1;

                        }
                        else {

                        }
                    });
                if (count > 0) {
                    this.duplicateRuleGroupName = true;
                }
                else {
                    this.duplicateRuleGroupName = false;
                }
            }
        );

    }

    displayOptions() {
        this.showOptions = true;
    }

    saveRuleGroup() {
        if(this.isCreateNew)
        {
            if(this.startDateChange){
                this.ruleGroupService.ruleGroup.startDate=new Date(this.ruleGroupService.ruleGroup.startDate.getTime() + 86400000);
            }
            if(this.endDateChange){
                this.ruleGroupService.ruleGroup.endDate=new Date(this.ruleGroupService.ruleGroup.endDate.getTime() + 86400000);
            }
        }
        else if(this.isEdit)
        {
            this.ruleGroupService.ruleGroup.startDate=new Date(this.ruleGroupService.ruleGroup.startDate.getTime() + 86400000);
            if(this.ruleGroupService.ruleGroup.endDate)
            this.ruleGroupService.ruleGroup.endDate=new Date(this.ruleGroupService.ruleGroup.endDate.getTime() + 86400000);
        }
        if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
            if (this.ruleGroupApprovalsComponent.approvalForm.valid) {
                let ind: number = 1;
                if(this.isCreateNew)
                {
                    this.ruleGroupService.ruleGroup.enableFlag =true;
                }
               // this.ruleGroupService.approvalRuleGRoupWithRules.enabledFlag = true;
                let readyToSave : boolean = false;
                let noActionsList : string='' ;
                let actionsSize : number=0 ;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach(rule => {
                   if( rule.approvalActions.actionDetails && rule.approvalActions.actionDetails.length>0)
                   {
                    actionsSize = actionsSize+1;
                   }
                   else{
                       if(ind-1 == 0)
                    noActionsList = noActionsList+rule.ruleCode+', ';
                    else
                    noActionsList = noActionsList+rule.ruleCode;
                   }
                    //rule.priority = ind;
                    ind = ind + 1;
                    if (!rule.ruleGroupAssignId) {
                        //new rule tagged
                        if (rule.id && rule.id > 0) {
                            //existing rule tagged
                            rule.assignmentFlag = true;
                        }
                        else {
                            //new created rule
                            rule.enabledFlag = true;
                            rule.assignmentFlag = true;
                        }
                    }
                    else {
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
                  if(this.ruleGroupService.approvalRuleGRoupWithRules.rules.length == actionsSize )
                  {
                    readyToSave = true;
                  }
                  if(noActionsList)
                  {
                    this.notificationsService.error(
                        'No approvers tagged to rules - '+noActionsList,
                        ''
                    )
                  }
                  else{

                
                       if(readyToSave)
                       {
                           this.setPriority();
                           this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach(rule => {
                           if(!rule.id)
                           {
                               if(rule.startDateChange){
                                rule.startDate=new Date(rule.startDate.getTime() + 86400000);
                               }
                               if(rule.endDateChange){
                                rule.endDate=new Date(rule.endDate.getTime() + 86400000);
                               }
                           }
                        });
                        this.ruleGroupService.postApprovalRules().subscribe((res: any) => {
                            let savedObj = [];
                            savedObj = res;
                            if (savedObj[0].taskName == 'Rule Group Save') {
                                this.notificationsService.success(
                                    'Saved!',
                                    this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                                )
                                let groupId: any;
                                groupId = savedObj[0].details;
                                this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
                            }
                        });
                       }
                    }
              
            }
            else {
                this.notificationsService.error('Please fill mandatory fields', '');
            }
        }
        else if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
            if (this.ruleGroupAccountingNewComponent.accRuleForm.valid) {
                if (this.isCreateNew) {
                    this.ruleGroupService.ruleGroup.enableFlag = true;
                }
                this.ruleGroupService.accountingRuleGroup.rules.forEach(ruleObj => {
                    if (ruleObj.id) {
                    }
                    else {
                        ruleObj.enabledFlag = true;
                        ruleObj.assignmentFlag = true;
                        ruleObj.debitLines.forEach(line => {
                            ruleObj.lineDerivationRules.push(line);
                        });
                        ruleObj.creditLines.forEach(line => {
                            ruleObj.lineDerivationRules.push(line);
                        });
                    }

                });
                let ind: number = 1;
               // this.ruleGroupService.accountingRuleGroup.enabledFlag = true;
                this.ruleGroupService.accountingRuleGroup.rules.forEach(rule => {
                   // rule.priority = ind;

                    if (!rule.ruleGroupAssignId) {
                        //new rule tagged
                        if (rule.id && rule.id > 0) {
                            //existing rule tagged
                            rule.assignmentFlag = true;
                        }
                        else {
                            //new created rule
                            rule.enabledFlag = true;
                            rule.assignmentFlag = true;
                        }
                    }
                    else {
                        //editing tagged rule

                    }
                    ind = ind + 1;
                });
                this.setPriority();
                this.ruleGroupService.accountingRuleGroup.rules.forEach(rule => {
                    if(!rule.id)
                    {
                        if(rule.startDateChange){
                         rule.startDate=new Date(rule.startDate.getTime() + 86400000);
                        }
                        if(rule.endDateChange){
                         rule.endDate=new Date(rule.endDate.getTime() + 86400000);
                        }
                    }
                 });
                this.ruleGroupService.postAccountingRuleGroup().subscribe((res: any) => {
                    let savedObj = [];
                    savedObj = res;
                    //console.log( 'savedObj after acc rule save' + JSON.stringify( savedObj ) );
                    if (savedObj[0].taskName == 'Rule Group Save') {
                        this.notificationsService.success(
                            'Saved!',
                            this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                        )
                        let groupId: any;
                        groupId = savedObj[0].details;
                        this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
                    }
                });


            }
            else {
                this.notificationsService.error('Please fill mandatory fields', '');
            }

        }
        else {
            if (this.ruleGroupReconciliationComponent.reconForm.valid) {
                if(this.isCreateNew)
                this.ruleGroupService.ruleGroup.enableFlag = true;
                let emptyConditionsFound: number = 0;

                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
                   // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.priority = i + 1;

                    if (!this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleGroupAssignId) {
                        //new rule tagged
                        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id > 0) {
                            //existing rule tagged
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.assignmentFlag = true;
                        }
                        else {
                            //new created rule
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.enabledFlag = true;
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.assignmentFlag = true;
                        }
                    }
                    else {
                        //editing tagged rule

                    }
                    let ci: number = 0;
                    let indexes = [];
                    let sequences: any = '';
                    console.log('conditions before edit' + JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i]));
                    if (!this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id <= 0) {
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach(condition => {
                            if (!condition.sColumnId && !condition.operator && !condition.tColumnId) {
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(ci, 1);
                                emptyConditionsFound = emptyConditionsFound + 1;
                                if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ci - 1] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length == 1) {
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ci - 1].logicalOperator = null;
                                }
                            }
                            ci = ci + 1;
                        });

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

                }

                if (emptyConditionsFound > 0) {
                    var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
                        data: { message: ' Ignored ' + emptyConditionsFound + ' empty conditions' },
                        disableClose: true
                    });
                }
                this.setPriority();
                this.saveReconRuleGroup();
            }
            else
                this.notificationsService.error('Please fill mandatory fields', '');
        }


    }
    saveReconRuleGroup() {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach(rule => {
            if(!rule.id)
            {
                if(rule.rule.startDateChange){
                 rule.rule.startDate=new Date(rule.rule.startDate.getTime() + 86400000);
                }
                if(rule.rule.endDateChange){
                    if(rule.rule.endDate.getTime() )
                 rule.rule.endDate=new Date(rule.rule.endDate.getTime() + 86400000);
                }
            }
         });
        this.ruleGroupService.postRuleGroup().subscribe((res: any) => {
            let savedObj = [];
            savedObj = res;
            //console.log('saved obj of reconciliation is:'+JSON.stringify(savedObj));
            let taskk: boolean = false;
            if (savedObj[0].taskName == 'Rule Group Save') {
                this.notificationsService.success(
                    'Saved!',
                    this.ruleGroupService.ruleGroup.name + ' saved successfully!'
                )
                this.$sessionStorage.clear('reconciliationRuleList');
                let groupId: any;
                groupId = savedObj[0].details;
                //console.log('groupId'+groupId);
                this.router.navigate(['/rule-group', { outlets: { 'content': groupId + '/details' } }]);
            }
        });
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
        this.groups = [];
        this.tenantSubscribedModules.forEach(module => {
            if (module.id == this.ruleGroupService.ruleGroup.configuredModuleId) {
                this.ruleGroupService.ruleGroup.configuredModuleName = module.modules;
            }
        });
        if (this.ruleGroupService.ruleGroup.configuredModuleName.search('RECON') != -1) {

            this.ruleGroupService.fetchUnTaggedRuleGroups('RECONCILIATION').subscribe((res: any) => {
                this.groups = res.json();
                // console.log( ' this.groups' + JSON.stringify( this.groups ) );
            });

        }
        else if (this.ruleGroupService.ruleGroup.configuredModuleName.search('ACCOUNTING') != -1) {
            this.ruleGroupService.fetchUnTaggedRuleGroups('ACCOUNTING').subscribe((res: any) => {
                this.groups = res.json();
                // console.log( ' this.groups' + JSON.stringify( this.groups ) );
            });
        }
        if (this.groups) {
            let ind: number = 0;
            this.groups.forEach(group => {
                if (group.apprRuleGrpId) {
                    this.groups.splice(ind, 1);
                }
                ind = ind + 1;
            });
        }

    }
    ngOnDestroy() {
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
        console.log(' this.ruleGroupService.ruleGroup.crossCurrency ' + this.ruleGroupService.ruleGroup.crossCurrency);
    }
    validateSegmentLength() {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
        if(this.ruleGroupService.ruleGroup.controlAccountValue && this.ruleGroupService.ruleGroup.controlAccountValue[0] &&this.ruleGroupService.ruleGroup.controlAccountValue[0][0])
        this.ruleGroupService.ruleGroup.controlAccount = this.ruleGroupService.ruleGroup.controlAccountValue[0][0];
       
        if(this.ruleGroupService.ruleGroup.realizedGainLossAccountValue && this.ruleGroupService.ruleGroup.realizedGainLossAccountValue[0] &&this.ruleGroupService.ruleGroup.realizedGainLossAccountValue[0][0])
        this.ruleGroupService.ruleGroup.realizedGainLossAccount= this.ruleGroupService.ruleGroup.realizedGainLossAccountValue[0][0];
       
        if(this.ruleGroupService.ruleGroup.fxGainAccountValue && this.ruleGroupService.ruleGroup.fxGainAccountValue[0] &&this.ruleGroupService.ruleGroup.fxGainAccountValue[0][0])
        this.ruleGroupService.ruleGroup.fxGainAccount = this.ruleGroupService.ruleGroup.fxGainAccountValue[0][0];
       
        if(this.ruleGroupService.ruleGroup.fxLossAccountValue && this.ruleGroupService.ruleGroup.fxLossAccountValue[0] &&this.ruleGroupService.ruleGroup.fxLossAccountValue[0][0])
        this.ruleGroupService.ruleGroup.fxLossAccount = this.ruleGroupService.ruleGroup.fxLossAccountValue[0][0];
        this.notificationsService.info(
            'Cross check the Account`s segment length',
            ' '
        )
    }
    setFxLossIfEmpty() {
        if (!this.ruleGroupService.ruleGroup.fxLossAccount) {
            this.ruleGroupService.ruleGroup.fxLossAccount = this.ruleGroupService.ruleGroup.fxGainAccount;
        }
    }
    changeReconcileGrpForActivityBased()
    {
        
        
       
        let displayPopUp : boolean = false;
        this.ruleNames='';
        this.indexToRefresh=[];
            for(var i :number = 0;i< this.ruleGroupService.accountingRuleGroup.rules.length;)   
            {

            if(this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId)
            {
                
               this. getRuleNames(i);
              
                i= i+1;
            }
            else
            i=i+1;
        }
      
           
    }
    getRuleNames (i)
    {
        
    this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource(this.ruleGroupService.ruleGroup.reconciliationGroupId, 
        this.ruleGroupService.accountingRuleGroup.rules[i].sourceDataViewId).subscribe((res: any) => {
        this.reconDataSourceAfterChange[i] = res;
        let exists : boolean = false;
        if (this.reconDataSourceAfterChange[i] && this.reconDataSourceAfterChange[i].length > 0) {
            if(this. reconDataSourceAfterChange[i].length  == 1 &&   this.reconDataSourceAfterChange[i][0] && this. reconDataSourceAfterChange[i][0]['failed'])
            {
                //this.ruleGroupService.reconDataSourceArrays[i]=[];
                // this.notificationsService.info(
                //     'Eligible data sets not found.',
                //     ' '
                // )
                this. reconDataSourceAfterChange[i] =[];
            }
            else
           this. reconDataSourceAfterChange[i].forEach(dv => {
                dv['itemName'] = dv['DataViewDispName'];
                if(this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId != null)
                {
                    if(this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId == dv.id)
                    {
                        exists = true;
                    }
                }
            });
            if(exists)
            {
                
            }
            else{
                if(i == 0)
                {
                    if(this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode)
                    this.ruleNames = this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode;
                    this.indexToRefresh.push(i);
                    if(i == this.ruleGroupService.accountingRuleGroup.rules.length-1)
                    this. displayMessage(this.ruleNames,this.indexToRefresh,this.ruleGroupService.ruleGroup.reconciliationGroupId);
                    
                }
             
                else
                {
                    if(this.ruleGroupService.accountingRuleGroup.rules[i] && this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode)
                    this.ruleNames = this.ruleNames + ', ' +this.ruleGroupService.accountingRuleGroup.rules[i].ruleCode;
                    this.indexToRefresh.push(i);
                    if(i == this.ruleGroupService.accountingRuleGroup.rules.length-1)
                    this. displayMessage(this.ruleNames,this.indexToRefresh,this.ruleGroupService.ruleGroup.reconciliationGroupId);
                    
                }
             
            }
        }
        else{
            
            
        }

    });
}
    displayMessage(ruleNames,indexToRefresh,selectedReconGroupId)
    {
        var dialogRef = this.dialog.open(ConfirmActionModalDialog, {
            data: {  refreshViews : 'refreshViews' , ruleNames : ruleNames ,ok:'ok',no:'No thanks'},
            disableClose: true
        });
        dialogRef.afterClosed().subscribe(result => {
          
            if (result && result == 'ok'){ 
               
               
                    for(var i :number = 0;i< indexToRefresh.length;i++)   
                    {
                        if(! this.ruleGroupService.reconDataSourceArrays[indexToRefresh[i]])
                        this.ruleGroupService.reconDataSourceArrays[indexToRefresh[i]]=[];
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconDataSourceId = null;
                        this.ruleGroupService.accountingRuleGroup.rules[i].reconSourceDVId = null;
                      this.ruleGroupService.reconDataSourceArrays[indexToRefresh[i]] = this.reconDataSourceAfterChange[indexToRefresh[i]];
                    }
            }
            else{
                this.ruleGroupService.ruleGroup.reconciliationGroupId =  this.previousGroupId ;

              
            }
            this.previousGroupId  = new Number(selectedReconGroupId);

            });  
    }

setPriority()
{
    this.ruleGroupService.rulePriorityList = [];
    if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
        if(this.isCreateNew)
        {
            let indx : number =0;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach(rule=>{
                rule.rule.priority = indx+1;
                let obj ={
                    "ind" : indx,
                    "priority" : indx+1,
                    "ruleName" : rule.rule.ruleCode,
                    "assignmentId":rule.rule.ruleGroupAssignId
                };
                this.ruleGroupService.rulePriorityList.push(obj);
                indx =indx+1;
            });
        }
        else{
            let indx : number =0;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach(rule=>{
                rule.rule.priority = indx+1;
                let obj ={
                    "ind" : indx,
                    "priority" : rule.rule.priority,
                    "ruleName" : rule.rule.ruleCode,
                    "assignmentId":rule.rule.ruleGroupAssignId
                };
                this.ruleGroupService.rulePriorityList.push(obj);
                indx =indx+1;
            });
        }
    }
    else  if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
        if(this.isCreateNew)
        {
            let indx : number =0;
            this.ruleGroupService.accountingRuleGroup.rules.forEach(rule=>{
                rule.priority = indx+1;
                let obj ={
                    "ind" : indx,
                    "priority" : indx+1,
                    "ruleName" : rule.ruleCode,
                    "assignmentId":rule.ruleGroupAssignId
                };
                this.ruleGroupService.rulePriorityList.push(obj);
                indx =indx+1;
            });
        }
        else{
            let indx : number =0;
            this.ruleGroupService.accountingRuleGroup.rules.forEach(rule=>{
                rule.priority = indx+1;
                let obj ={
                    "ind" : indx,
                    "priority" : rule.priority,
                    "ruleName" : rule.ruleCode,
                    "assignmentId":rule.ruleGroupAssignId
                };
                this.ruleGroupService.rulePriorityList.push(obj);
                indx =indx+1;
            });
        }
    }
    else  if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
        if(this.isCreateNew)
        {
            let indx : number =0;
            this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach(rule=>{
                rule.priority = indx+1;
                let obj ={
                    "ind" : indx,
                    "priority" : indx+1,
                    "ruleName" : rule.ruleCode,
                    "assignmentId":rule.ruleGroupAssignId
                };
                this.ruleGroupService.rulePriorityList.push(obj);
                indx =indx+1;
            });
        }
        else{
            let indx : number =0;
            this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach(rule=>{
                rule.priority = indx+1;
                let obj ={
                    "ind" : indx,
                    "priority" : rule.priority,
                    "ruleName" : rule.ruleCode,
                    "assignmentId":rule.ruleGroupAssignId
                };
                this.ruleGroupService.rulePriorityList.push(obj);
                indx =indx+1;
            });
        }
    }
    
}
updatePriority()
{
    console.log('event in update priority'+event);
    if (this.ruleGroupService.ruleGroup.rulePurpose == 'RECONCILIATION') {
        let indx : number =1;
        this.ruleGroupService.rulePriorityList.forEach(priorityObj=>{
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[priorityObj.ind].rule.priority = indx;
            priorityObj.priority = indx;
            if(this.ruleGroupService.rulePriorityList.length == indx)
            {
                if(!this.isViewOnly){
                    // this.saveRuleGroup();
                }
               
                else
                this.ruleGroupService.updateRulesPriority(this.ruleGroupService.rulePriorityList).subscribe((res: any) => {
                    let resp : any =res;
                    console.log('saved or not '+JSON.stringify(resp));
                    this.setRulePriority=false;
                    if(resp.updated == 'success')
                    {
                        this.notificationsService.success(
                            'Priorities updated successfully',
                            ''
                        )
                    }
                    else
                    {
                        this.notificationsService.error(
                            'Priorities updation failed',
                            ''
                        )
                    }
                }); 
            }
            indx =indx+1;

        });
        
    }
    else  if (this.ruleGroupService.ruleGroup.rulePurpose == 'ACCOUNTING') {
        let indx : number =1;
        this.ruleGroupService.rulePriorityList.forEach(priorityObj=>{
            this.ruleGroupService.accountingRuleGroup.rules[priorityObj.ind].priority = indx;
            priorityObj.priority = indx;
            if(this.ruleGroupService.rulePriorityList.length == indx)
            {
                if(!this.isViewOnly){
                    // this.saveRuleGroup();
                }
               
                else
                this.ruleGroupService.updateRulesPriority(this.ruleGroupService.rulePriorityList).subscribe((res: any) => {
                    let resp : any =res;
                    console.log('saved or not '+JSON.stringify(resp));
                    this.setRulePriority=false;
                    if(resp.updated == 'success')
                    {
                        this.notificationsService.success(
                            'Priorities updated successfully',
                            ''
                        )
                    }
                    else
                    {
                        this.notificationsService.error(
                            'Priorities updation failed',
                            ''
                        )
                    }
                }); ;
            }
            indx =indx+1;

        });
    }
    else  if (this.ruleGroupService.ruleGroup.rulePurpose == 'APPROVALS') {
        let indx : number =1;
        this.ruleGroupService.rulePriorityList.forEach(priorityObj=>{
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[priorityObj.ind].priority = indx;
            priorityObj.priority = indx;
            if(this.ruleGroupService.rulePriorityList.length == indx)
            {
                if(!this.isViewOnly){
                    // this.saveRuleGroup();
                }
               
                else
                this.ruleGroupService.updateRulesPriority(this.ruleGroupService.rulePriorityList).subscribe((res: any) => {
                    let resp : any =res;
                    console.log('saved or not '+JSON.stringify(resp));
                    this.setRulePriority=false;
                    if(resp.updated == 'success')
                    {
                        this.notificationsService.success(
                            'Priorities updated successfully',
                            ''
                        )
                    }
                    else
                    {
                        this.notificationsService.error(
                            'Priorities updation failed',
                            ''
                        )
                    }
                    
                    
                }); ;
            }
            indx =indx+1;

        });
    }
    
}
SelectCOA() {
    this.valueSetForControlAcc=[];
    this.valueSetForRealizedAcc=[];
    this.valueSetForFXGainAcc=[];
    this.valueSetForFXLossAcc=[];
    this.ruleGroupService.ruleGroup.controlAccountValue =null;
    this.ruleGroupService.ruleGroup.realizedGainLossAccountValue=null;
    this.ruleGroupService.ruleGroup.fxGainAccountValue =null;
    this.ruleGroupService.ruleGroup.fxLossAccountValue =null;
    this.segmentsService.fetchSegmentsByCOAOrderBySequence(this.ruleGroupService.ruleGroup.coa,'NATURAL_ACCOUNT').subscribe((res: any) => {
        let resp: any = res;
        if(resp.segments && resp.segments[0] && resp.segments[0].valueSet)
        this.valueSetForControlAcc = resp.segments[0].valueSet;
        if(this.valueSetForControlAcc &&  this.valueSetForControlAcc.length>0)
        {
           // let ind : number = 0;
            for(var i =0;i<this.valueSetForControlAcc.length;i++){
                this.valueSetForControlAcc[i]['itemName']=this.valueSetForControlAcc[i][1];
                this.valueSetForControlAcc[i]['id']=this.valueSetForControlAcc[i][0];
              //  ind = ind+1;
            }
            this.valueSetForRealizedAcc = jQuery.extend(true, [], this.valueSetForControlAcc);
            this.valueSetForFXGainAcc =jQuery.extend(true, [], this.valueSetForControlAcc);
            this.valueSetForFXLossAcc =jQuery.extend(true, [], this.valueSetForControlAcc);
            
            console.log('this.valueSetForAcc'+JSON.stringify(this.valueSetForControlAcc));
        }
         else
         {
        //     if(this.ruleGroupService.ruleGroup.crossCurrency ==  true)
        //     this.ruleGroupService.ruleGroup.crossCurrency =false;
        this.notificationsService.error(
            ' Value set is not tagged to the account segment',
            ''
        )
        
         }
        if(this.ruleGroupService.accountingRuleGroup && this.ruleGroupService.accountingRuleGroup.rules && this.ruleGroupService.accountingRuleGroup.rules.length>0)
        {
            for(var i=0;i<this.ruleGroupService.accountingRuleGroup.rules.length;i++)
            {
                this.ruleGroupService.accountingRuleGroup.rules[i].coa=this.ruleGroupService.ruleGroup.coa;
            }
            this.ruleGroupAccountingNewComponent.refreshWithCOA(this.ruleGroupService.ruleGroup.coa);
        }
      
    });

}

filterViews()
{
this.ruleGroupApprovalsComponent.fetchDataViewsByRuleGroup();
}
startDateChanged(dt:Date){
    if(this.ruleGroupService.ruleGroup.endDate){
        if(this.ruleGroupService.ruleGroup.endDate<this.ruleGroupService.ruleGroup.startDate){
            this.ruleGroupService.ruleGroup.endDate=this.ruleGroupService.ruleGroup.startDate;
        }
    }
}
}
