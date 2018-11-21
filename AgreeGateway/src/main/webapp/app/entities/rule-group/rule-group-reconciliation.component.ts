
import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, ViewChild } from '@angular/core';
import { FormControl, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router'; 
import { Response } from '@angular/http';
import { Router, NavigationEnd } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { CommonService } from '../common.service';
import { Rules } from '../rules/rules.model';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
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
import { RuleGroupAndRuleWithLineItem } from './ruleGroupAndRuleWithLineItem.model';
import { IterableDiffers, KeyValueDiffers } from '@angular/core';
import { ReconcileService } from '../reconcile/reconcile.service';
import { RuleGroup } from './rule-group.model';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import {ReconConfirmActionModalDialog} from './rule-group-recon-confirm-action-dialog';
import * as $ from "jquery";
declare var jQuery: any;
const URL = '';
@Component({
    selector: 'jhi-rule-group-reconciliation',
    templateUrl: './rule-group-reconciliation.component.html'
})
export class RuleGroupReconciliationComponent implements OnInit {
    today=new Date();    
    toOperands=[];
    fromOperands=[];
    tToOperands=[];
    tFromOperands=[];
    logicalOpLOVArray = [];
    ruletypeLOVArray = [];
    ruleTypePanelOpenState: boolean = false; 
    sourceDropdownSettings = {
        singleSelection: true,
       // text: "Reconciliation for",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        selectionLimit: 1,
        autoUnselect: true,
        closeOnSelect: true,
        showCheckbox: false
    };
    targetDropdownSettings = {
        singleSelection: true,
        //text: "Reconciliation with",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        selectionLimit: 1,
        autoUnselect: true,
        closeOnSelect: true,
        showCheckbox: false
    };

    @ViewChild(NgForm) reconForm;
    @Input() accountingMode: string = '';
    ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
    myData: any = '';
    filterAction: any = '';
    duplicateRuleGroupName: boolean = false;
    ruleShobha: Rules = new Rules();
    mySource = ["abc", "bcd", "def"];
    ruleListformArray: FormGroup[] = [];
    targetDataViewformsArray: FormGroup[] = [];
    srcDataViewformsArray: FormGroup[] = [];

    srcColumnListformsArray: FormGroup[] = [];
    targetColumnListformsArray: FormGroup[] = [];

    selectedSrcDataViewAndColumns = [];
    selectedTargetDataViewAndColumns = [];


    logicalOperatorLOV = [];
    sManyEnable = [];
    tManyEnable = [];

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
    ruleCreationInWQ: boolean = false;
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

    availablRuleTypes: any = [{
        "lookUpId": '111',
        "ruleType": 'ruleType1'
    },
    {
        "lookUpId": '112',
        "ruleType": 'ruleType2'
    },
    {
        "lookUpId": '113',
        "ruleType": 'ruleType3'
    },
    {
        "lookUpId": '114',
        "ruleType": 'ruleType4'
    },];

    options: any = [];

    sourceDataViewsAndColumns = [];
    targetDataViewsAndColumns = [];
    sourceColumnLOV = [];
    targetColumnLOV = [];

    ruleNamefilteredOptions: Observable<any[]>;
    sourceDataViews: Observable<any[]>;
    TargetDataViews: Observable<any[]>;

    srcColumnsList: Observable<any[]>;
    targetColumnList: Observable<any[]>;

    operatorColumnList: Observable<any[]>;
    /* sFilterTitle :  string ='      Filters           ';
     sFilter : boolean  = false;
     sFormulaTitle : string = 'Formulae              ';
     sFormula : boolean =false;
     sToleranceTitle : string ='Tolerance                ';
     sTolerance : boolean = false;
     sTitle :  string ='  Additional filters ';
     
     tFilterTitle :  string ='      Filters           ';
     tFilter : boolean  = false;
     tFormulaTitle : string = 'Formulae              ';
     tFormula : boolean =false;
     tToleranceTitle : string ='Tolerance                ';
     tTolerance : boolean = false;
     tTitle :  string ='  Additional filters ';*/
    excelFunctions = [];
    excelexpressioninput: any;
    excelexpressionExample: any;
    operatorList = [];
    operatorBasedOnColumnLOV = [];
    stoleranceTypeLOV = [];
    ttoleranceTypeLOV=[];
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
    enableruleBlock: boolean;
    operands=[];
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
        private differs: IterableDiffers,
        private objDiffers: KeyValueDiffers,
        private reconcileService: ReconcileService,
        public dialog: MdDialog
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.ruleGroupDiffer = differs.find([]).create(null);
        this.ruleObjDiffer = objDiffers.find({}).create();
        this.ruleConditionsDiffer = differs.find([]).create(null);
        this.ruleConditionObjDiffer = objDiffers.find({}).create();
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

    addNewCondition(indexVal: any, childIndex: any) {
        let ruleObj1: RuleConditions = new RuleConditions();
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex] = {};

        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex] = ruleObj1;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex].sequenceNo = childIndex + 1;
        this.sAdditionalFiltersTitle[childIndex] = this.additionalFilters;
        this.tAdditionalFiltersTitle[childIndex] = this.additionalFilters;
        if (!this.operatorBasedOnColumnLOV[indexVal])
            this.operatorBasedOnColumnLOV[indexVal] = [];
        this.operatorBasedOnColumnLOV[indexVal][childIndex] = [];

        if (!this.logicalOpLOVArray[indexVal])
            this.logicalOpLOVArray[indexVal] = [];
        if (!this.logicalOpLOVArray[indexVal][childIndex])
            this.logicalOpLOVArray[indexVal][childIndex] = [];
        this.logicalOpLOVArray[indexVal][childIndex] = jQuery.extend(true, [], this.logicalOperatorLOV);
       
    }
    addNewRuleObject(indexVal: any, childIndex: any) {
        for (var i = 0; i <= indexVal; i++) {
            this.expandTab[i] = false;

        }
        this.expandTab[indexVal] = true;
        let ruleConditions = new RuleConditions();
        let rule = new Rules();
        rule.priority = indexVal + 1;
        let rulesConditionsListObj: RuleConditions[] = [];
        rulesConditionsListObj[0] = (ruleConditions);
       

        let rulesWithConditionsObj = new RulesAndConditions();

        rulesWithConditionsObj.rule = rule;
        rulesWithConditionsObj.ruleConditions = rulesConditionsListObj;
        if (indexVal == 0) {
            this.ruleGroupService.ruleGroupRulesAndConditions['rules'] = [];
        }
        else {

        }
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal] = (rulesWithConditionsObj);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.priority = indexVal + 1;
        this.addnewLists(indexVal, 0);
        //rule list array based on ruleGrouptype
        this.fetchRulesAndDataViewsListsBasedOnRuleGroupType(indexVal);
        this.setDataViewsArrays(indexVal);

        this.ruletypeLOVArray[indexVal] = jQuery.extend(true, [], this.ruletypeLOV);
        this.ruletypeLOVArray[indexVal].forEach(op => {
            op.value = false;

        });
        this.ruletypeLOVArray[indexVal][0].value=true;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType=this.ruletypeLOVArray[indexVal][0].setValue;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleTypeMeaning = this.ruletypeLOVArray[indexVal][0].code;

        this.operatorBasedOnColumnLOV[indexVal] = [];
        if (this.accountingMode.toString().endsWith('WQ')) {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = this.ruleGroupService.sourceDataViewId;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = this.ruleGroupService.targetDataViewId;
            this.fetchSrcColumns(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId, 0);
            this.fetchTargetColumns(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId, 0);
        }
        this.addNewCondition(indexVal,childIndex);
    }
    setDataViewsArrays(indexVal: any) {
        this.sourceDataViewsArrays[indexVal] = this.sourceDataViewsAndColumns;
        this.targetDataViewsArrays[indexVal] = this.targetDataViewsAndColumns;
    }
    fetchRulesAndDataViewsListsBasedOnRuleGroupType(indexVal: any) {
        this.fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal);

        // if ( this.ruleListArrays[indexVal] ) {
        //     this.ruleListArrays[indexVal].forEach( rule => {
        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule = rule;

        //     } );
        // }


    }
    //set form group to all
    setFormGroupsForExisting() {
        for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
            let ruleListForm: FormGroup;
            ruleListForm = this.builder.group({
                data: "",
            });
            this.ruleListformArray[i] = ruleListForm;

            let myForm: FormGroup;
            myForm = this.builder.group({
                data: "",
            });
            this.targetDataViewformsArray[i] = (myForm);

            let myForm1: FormGroup;
            myForm1 = this.builder.group({
                data: "",
            });
            this.srcDataViewformsArray[i] = (myForm1);

            let srcColListForm: FormGroup;
            srcColListForm = this.builder.group({
                data: "",
            });
            this.srcColumnListformsArray[i] = srcColListForm;

            let targetColListForm: FormGroup;
            targetColListForm = this.builder.group({
                data: "",
            });
            this.targetColumnListformsArray[i] = targetColListForm;

            this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
            this.setDataViewsArrays(i);


        }


    } 
    fetchAllLookUps() {

        
        this.lookUpCodeService.fetchLookUpsByLookUpType('RECONCILIATION_FUNCTIONS').subscribe((res: any) => {
            this.excelFunctions = res;
        });

        //operator list for filters
        this.lookUpCodeService.fetchLookUpsByLookUpType('VARCHAR').subscribe((res: any) => {
            this.operatorList = res;
        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('TOLERANCE_TYPE').subscribe((res: any) => {
            this.stoleranceTypeLOV = res;
            this.ttoleranceTypeLOV = jQuery.extend(true, [], this.stoleranceTypeLOV);
        
        });

        this.lookUpCodeService.fetchLookUpsByLookUpType('OPERATOR').subscribe((res: any) => {
            this.operatorLOV = res;
        });

        this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((res: any) => {
            this.ruleGroupTypes = res;
        });


    }
    ngOnInit() {
        //console.log('in rule recon');
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) + 'px'
        });


        // 
        //create new
        let ruleListForm: FormGroup;
        ruleListForm = this.builder.group({
            data: "",
        });
        this.ruleListformArray[0] = ruleListForm;
        let myForm: FormGroup;
        myForm = this.builder.group({
            data: "",
        });
        this.targetDataViewformsArray[0] = (myForm);

        let myForm1: FormGroup;
        myForm1 = this.builder.group({
            data: "",
        });
        this.srcDataViewformsArray[0] = (myForm1);

        let srcColListForm: FormGroup;
        srcColListForm = this.builder.group({
            data: "",
        });
        this.srcColumnListformsArray[0] = srcColListForm;

        let targetColListForm: FormGroup;
        targetColListForm = this.builder.group({
            data: "",
        });
        this.targetColumnListformsArray[0] = targetColListForm;


        this.commonService.callFunction();
        let operand = { "code": '+' ,"value": true };
        this.operands.push(operand);
        operand = { "code": '-' ,"value": false };
        this.operands.push(operand);
        this.tToOperands =  jQuery.extend(true, [], this.operands);
        this.tFromOperands = jQuery.extend(true, [], this.operands);
        this. toOperands = jQuery.extend(true, [], this.operands);
        this.fromOperands = jQuery.extend(true, [], this.operands);

        this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_TYPE').subscribe((res: any) => {
            let ruleTypeLOV = res;
            ruleTypeLOV.forEach(ruleType => {
                let rTypeObj = { "code": ruleType.meaning, "name": ruleType.description, "setValue": ruleType.lookUpCode, "value": false };
                this.ruletypeLOV.push(rTypeObj);
            });

        });
        this.lookUpCodeService.fetchLookUpsByLookUpType('LOGICAL_OPERATOR').subscribe((res: any) => {
            let logicalOperatorLOV = res;
            logicalOperatorLOV.forEach(logOp => {
                let logOpObj = { "code": logOp.meaning, "name": logOp.description, "setValue": logOp.lookUpCode, "value": false };
                this.logicalOperatorLOV.push(logOpObj);
            });
        });


        /*  this.dataViewsService.dataViewList().subscribe((res:any)=>{
              this.sourceDataViewsAndColumns = res;
              this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns;
          });*/

        let url = this.route.snapshot.url.map(segment => segment.path).join('/');

        this.routeSub = this.route.params.subscribe(params => {
            this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose = 'RECONCILIATION';
            //console.log('params'+params['id']+'URL'+url);
            if (params['id']) {

                if (url.endsWith('edit')) {
                    this.fetchAllLookUps();
                    this.dataViewsService.dataViewList().subscribe((res: any) => {
                        this.sourceDataViewsAndColumns = res;
                        this.sourceDataViewsAndColumns.forEach(dv => {
                            dv['itemName'] = dv['dataViewDispName'];
                        });
                        this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns;
                        this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                        this.isEdit = true;
                    });
                }
                else {

                    let viewData: any = [];
                    viewData = this.accountingMode.toString().split('-');
                    this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                    this.isViewOnly = true;
                }
            }
            else {
                this.fetchAllLookUps();
                if (this.accountingMode.toString().endsWith('WQ')) {
                    this.ruleCreationInWQ = true;

                }
                this.dataViewsService.dataViewList().subscribe((res: any) => {
                    this.sourceDataViewsAndColumns = res;
                    this.sourceDataViewsAndColumns.forEach(dv => {
                        dv['itemName'] = dv['dataViewDispName'];
                    });
                    this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns;
                    this.addNewRuleObject(0, 0);

                //    this.addNewCondition(0, 0);

                    this.isCreateNew = true;
                });
            }




            //         var re = /create/gi; 
            //            if ( this.accountingMode == 'edit' ) {
            //               
            //           
            //            } else if ( this.accountingMode.toString().startsWith( 'view' ) ) {
            //
            //
            //            }
            //            else if ( this.accountingMode.toString().startsWith('create') )
            //            {
            //               
            //                
            //            }
        });

    }
    //If Start Date Entered Apply Class
    startEndDateClass(indexVal: any) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate != null) {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }
    SelectRuleGroupType() {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose && this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose == 'RECONCILIATION') {
            this.ruleNameValidation = '';
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules)
                for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
                    this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
                }
        }
    }

    fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal: any) {
        //get rule type at that index and filter, and update the filter values
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal]) {
            let ruleType = '';
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType) {
                ruleType = this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType;
            }
            else {
                ruleType = '';
            }
            this.rulesService.getRules(this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose, ruleType).subscribe((res: any) => {
                let rulesFetched: Rules[] = [];
                rulesFetched = res;
                this.filteredRuleList = [];
                if (rulesFetched)
                    rulesFetched.forEach(rule => {
                        this.filteredRuleList.push(rule);
                    });
                this.ruleListArrays[indexVal] = [];
                this.ruleListArrays[indexVal] = this.filteredRuleList;
                this.addnewLists(indexVal, 0);

            });
            //enable smany tmany
            ////console.log( 'for enabling' + this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType );
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType == 'ONE_TO_MANY') {
                //enable tmany
                this.tManyEnable[indexVal] = [];
                for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.tManyEnable[indexVal]) {
                        this.tManyEnable[indexVal][i] = true;
                    }
                }
            }
            else if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType == 'MANY_TO_ONE') {
                this.sManyEnable[indexVal] = [];
                for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.sManyEnable[indexVal]) {
                        this.sManyEnable[indexVal][i] = true;
                    }
                }
            }
            else if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType == 'MANY_TO_MANY') {
                this.tManyEnable[indexVal] = [];
                for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.tManyEnable[indexVal]) {
                        this.tManyEnable[indexVal][i] = true;
                    }
                }
                this.sManyEnable[indexVal] = [];
                for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.sManyEnable[indexVal]) {
                        this.sManyEnable[indexVal][i] = true;
                    }
                }
            }
        }


    }
    clearSourceDV(indexVal: any)
    {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId =null;
    }
    fetchSrcColumns(dvid: any, indexVal: any) {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
        //////console.log('mode->'+this.isdata-viEdit+'value is'+JSON.stringify(value));
        //if (this.ruleCreationInWQ) {
            //  let dv : number = +value;
            //  this.dataViewsService.getDataViewById(dv).subscribe((res:any)=>{
            //                 let dvAndCols= res;
            //                 if(dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
            //                 this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
            //                 this.sourceColumnLOV[indexVal] = [];
            //                 this.sourceColumnLOV[indexVal] = ( this.selectedSrcDataViewAndColumns );
            //             });
       // }
       // else {
          //  if (this.isCreateNew) {
            if (!this.ruleCreationInWQ) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = dvid.id;
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = dvid.id;
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName = dv.dataViewName;
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = '';
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName =dv.dataViewDispName;

                this.dataViewsService.getDataViewById(dvid.id).subscribe((res: any) => {
                    let dvAndCols = res;
                    if (dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
                        this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
                    this.sourceColumnLOV[indexVal] = [];
                    this.sourceColumnLOV[indexVal] = (this.selectedSrcDataViewAndColumns);
                });
            }
            else
            {
                
                this.dataViewsService.getDataViewById(dvid).subscribe((res:any)=>{
                                    let dvAndCols= res;
                                    if(dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
                                    this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
                                    this.sourceColumnLOV[indexVal] = [];
                                    this.sourceColumnLOV[indexVal] = ( this.selectedSrcDataViewAndColumns );
                                });
            }

                /* let lovForSrc = [];
                 this.selectedSrcDataViewAndColumns = value.data.dvColumnsList;
     
                 lovForSrc[0] = this.selectedSrcDataViewAndColumns;*/

          //  }
            //else {
              //  if (this.isEdit) {
                //    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = dvid.id;
                    // if (  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0 ) {
                    //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.forEach( ruleCondition => {
                    //         if ( ruleCondition.id ) {

                    //         }
                    //         else {

                    //            // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = dvid.id;
                    //             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName = dv.dataViewName;

                    //             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = '';
                    //             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = dv.dataViewDispName;
                    //         } 

                    //     } );
                    // }

                //}
               // this.populateSourceColumns(indexVal);
           // }
        //}


    }
    populateSourceColumns(indexVal: any) {
        this.sourceColumnLOV[indexVal] = [];
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0)
            for (var childIndex = 0; childIndex < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; childIndex++) {
                this.sourceDataViewsAndColumns.forEach(dataViewObj => {
                    if (dataViewObj.id == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId) {
                        this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
                            let dvAndCols = res;
                            if (dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
                                this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
                            this.sourceColumnLOV[indexVal] = [];
                            this.sourceColumnLOV[indexVal] = (this.selectedSrcDataViewAndColumns);
                        });
                        //   this.sourceColumnLOV[indexVal] = dataViewObj.dvColumnsList;
                    }
                });
            }
    }
    clearTargetDV(indexVal: any)
    {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId =null;
    }
    fetchTargetColumns(dvid: any, indexVal: any) {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
       // if (this.ruleCreationInWQ) {
            //                 let dv : number = +value;
            //  this.dataViewsService.getDataViewById(dv).subscribe((res:any)=>{
            //                 let dvAndCols= res;
            //                 if(dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
            //                 this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
            //                 this.targetColumnLOV[indexVal] = [];
            //                 this.targetColumnLOV[indexVal] = ( this.selectedSrcDataViewAndColumns );
            //             });
      //  }
       // else {
          //  if (this.isCreateNew) {
            if (!this.ruleCreationInWQ) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = dvid.id;
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName = value.data.dataViewName;

                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = '';
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = value.data.dataViewDispName;

                this.dataViewsService.getDataViewById(dvid.id).subscribe((res: any) => {
                    let dvAndCols = res;
                    if (dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
                        this.selectedTargetDataViewAndColumns = dvAndCols[0].dvColumnsList;
                    this.targetColumnLOV[indexVal] = [];
                    this.targetColumnLOV[indexVal] = (this.selectedTargetDataViewAndColumns);
               });
            }
               else
               {
                   
                   this.dataViewsService.getDataViewById(dvid).subscribe((res:any)=>{
                                       let dvAndCols= res;
                                       if(dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
                                       this.selectedTargetDataViewAndColumns = dvAndCols[0].dvColumnsList;
                                       this.targetColumnLOV[indexVal] = [];
                                       this.targetColumnLOV[indexVal] = (this.selectedTargetDataViewAndColumns);
                                   });
               }
                //  let lovForTarget = [];
                //  this.selectedTargetDataViewAndColumns = value.data.dvColumnsList;

                //   lovForTarget[0] = this.selectedTargetDataViewAndColumns;
                // this.targetColumnLOV[indexVal] = [];
                //  this.targetColumnLOV[indexVal] = ( this.selectedTargetDataViewAndColumns );
            //}
            // else {
            //     if (this.isEdit) {
            //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = dvid;
                    // if (  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0 ) {
                    //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.forEach( ruleCondition => {
                    //         if ( ruleCondition.id ) {

                    //         }
                    //         else {
                    //          //   
                    //             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName = value.data.dataViewName;
                    //             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = '';
                    //             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = value.data.dataViewDispName;
                    //         }
                    //     } );
                    // }
               // }
                //this.populateTargetColumns(indexVal);
           // }
       // }

    }
    populateTargetColumns(indexVal: any) {
        this.targetColumnLOV[indexVal] = [];
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0)
            for (var childIndex = 0; childIndex < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; childIndex++) {
                this.targetDataViewsAndColumns.forEach(dataViewObj => {
                    if (dataViewObj.id == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId) {
                        this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
                            let dvAndCols = res;
                            if (dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
                                this.selectedTargetDataViewAndColumns = dvAndCols[0].dvColumnsList;
                            this.targetColumnLOV[indexVal] = [];
                            this.targetColumnLOV[indexVal] = (this.selectedTargetDataViewAndColumns);
                        });
                        //  this.targetColumnLOV[indexVal] = dataViewObj.dvColumnsList;
                    }
                });
            }
    }
    autocompleListFormatter = (data: any) => {
        let html = `<span >${data.ruleCode}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    autocompleteSrcColumnListFormatter = (data: any) => {
        let html = `<span style='color:blue'>${data.columnName}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    // fetchRulesBasedOnRuleGroupType() {
    //     this.rulesService.getRules( this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose, '' ).subscribe(( res: any ) => {
    //         let rulesFetched: Rules[] = [];
    //         rulesFetched = res;
    //         rulesFetched.forEach( rule => {
    //             this.filteredRuleList.push( rule );

    //         } );
    //         this.addnewLists( 0, 0 );
    //     } );
    // }
    addnewLists(indexVal: any, childIndex: any) {
        let lengthOfRules: number = 0;
        if (this.ruleGroupService.ruleGroupRulesAndConditions && this.ruleGroupService.ruleGroupRulesAndConditions.rules) {
            lengthOfRules = this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;
            if (this.ruleListformArray.length < lengthOfRules) {

                let myForm: FormGroup;
                myForm = this.builder.group({
                    data: "",
                });
                this.ruleListformArray[indexVal] = (myForm);
                if (this.srcDataViewformsArray.length < lengthOfRules) {
                    let myForm: FormGroup;
                    myForm = this.builder.group({
                        data: "",
                    });
                    // this.srcDataViewformsArray[indexVal] = [];
                    this.srcDataViewformsArray[indexVal] = (myForm);

                    //set columns LOV 
                    this.sourceColumnLOV[indexVal] = [];
                    this.sourceColumnLOV[indexVal][childIndex] = [];

                }
                else {
                }

                if (this.targetDataViewformsArray.length < lengthOfRules) {
                    let myForm: FormGroup;
                    myForm = this.builder.group({
                        data: "",
                    });
                    //this.targetDataViewformsArray[indexVal]=[];
                    this.targetDataViewformsArray[indexVal] = (myForm);

                    //target columns LOV
                    this.targetColumnLOV[indexVal] = [];
                    this.targetColumnLOV[indexVal][childIndex] = [];
                }
                else {
                }
            }

        }
    }
    filter0(name: string, indexVal: any): any[] {

        return this.optionsArray[indexVal].filter(option =>
            new RegExp(`^${name}`, 'gi').test(option.ruleCode));

    }
    filter1(name: string): any[] {
        return this.sourceDataViewsAndColumns.filter(option => new RegExp(`^${name}`, 'gi').test(option.dataViewDispName));
    }
    filter2(name: string): any[] {
        return this.targetDataViewsAndColumns.filter(option => new RegExp(`^${name}`, 'gi').test(option.dataViewDispName));
    }
    filter3(name: string): any[] {
        return this.sourceColumnLOV.filter(option => new RegExp(`^${name}`, 'gi').test(option.columnName));
    }
    filter4(name: string): any[] {
        return this.targetColumnLOV.filter(option => new RegExp(`^${name}`, 'gi').test(option.columnName));
    }

    displayFnRuleName(rule: any): string {
        //this.rules = '';
        return rule ? rule : rule;
    }
    filterSrcToTargetViews(sCol: any) {
        this.targetDataViewsAndColumns.forEach(targetObj => {
            if (targetObj.dataViewDispName === sCol) {
                let index = this.targetDataViewsAndColumns.indexOf(targetObj);
                this.targetDataViewsAndColumns = this.targetDataViewsAndColumns.splice(index, 0);
            }
        });
    }
    selectsrcColumns(sCol: any) {
        if (this.sourceDataViewsAndColumns) {
            this.sourceDataViewsAndColumns.forEach(dataViewObj => {
                if (dataViewObj.dataViewDispName === sCol) {
                    this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
                        let dvAndCols = res;
                        if (dvAndCols[0] && dvAndCols[0].dvColumnsList)
                            this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
                        this.sourceColumnLOV = (this.selectedSrcDataViewAndColumns);
                    });
                }
            });
            /* this.sourceDataViewsAndColumns.forEach( srcObject => {
                 if ( srcObject.dataViewDispName === sCol ) {
                     this.sourceColumnLOV = [];
                     this.sourceDataViewId = srcObject.id;
                     this.sourceColumnLOV = srcObject.dvColumnsList;
                 }
 
             } );*/
            // this.filterSrcToTargetViews(sCol);
        }
        else {
        }
    }
    selecttargetColumns(tCol: any) {
        if (this.targetDataViewsAndColumns) {
            this.targetDataViewsAndColumns.forEach(dataViewObj => {
                if (dataViewObj.dataViewDispName === tCol) {
                    this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
                        let dvAndCols = res;
                        this.selectedTargetDataViewAndColumns = dvAndCols[0].dvColumnsList;
                        this.targetColumnLOV = (this.selectedTargetDataViewAndColumns);
                    });
                }
            });
            /*  this.targetDataViewsAndColumns.forEach( targetObj => {
                  if ( targetObj.dataViewDispName === tCol ) {
                      this.targetColumnLOV = [];
                      this.targetDataViewId = targetObj.id;
                      this.targetColumnLOV = targetObj.dvColumnsList;
                  }
  
              } );*/
        }
        else {
        }
    }
    displayFnSrcDataView(srView: any): string {

        return srView ? srView : srView;
    }
    displayFnTargetDataView(targetView: any): string {

        return targetView ? targetView : targetView;
    }
    displayFnSrcColumns(srcColumn: any): string {
        return srcColumn ? srcColumn : srcColumn;
    }
    displayFnTargetColumns(targetColumn: any) {
        return targetColumn ? targetColumn : targetColumn;
    }
    SelectColumn(i, j, columnId) {
        let dataType: string = '';
        //console.log('  this.sourceColumnLOV[i]'+  this.sourceColumnLOV[i].length);
        if (this.sourceColumnLOV && this.sourceColumnLOV[i].length > 0) {
            this.sourceColumnLOV[i].forEach(col => {
                if (col.id == columnId) {
                    dataType = col.colDataType;
                }
            });
        }
        if (dataType) {
            this.lookUpCodeService.fetchLookUpsByLookUpType(dataType).subscribe((res: any) => {
                let operatorss = res;
                if (this.operatorBasedOnColumnLOV[i]) {
                    if (this.operatorBasedOnColumnLOV[i][j]) {

                    }
                    else {
                        this.operatorBasedOnColumnLOV[i][j] = [];
                    }
                }
                else {
                    this.operatorBasedOnColumnLOV[i] = [];
                    this.operatorBasedOnColumnLOV[i][j] = [];
                }
                this.operatorBasedOnColumnLOV[i][j] = operatorss;

            });
        }

    }
    selectedSourceColumn(i: any, childIndex: any, id: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sColumnId = id;
        this.sourceColumnLOV[i].forEach(srcColObj => {
            if (srcColObj.id == id) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sColumnName = srcColObj.columnName;
                this.SelectColumn(i, childIndex, id);
            }

        });

    }
    selectedOperator() {

    }
    selectedTargetColumn(i: any, childIndex: any, id: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tColumnId = id;
    }
    MatchRule(i) {
        //console.log('i====>'+i);    
    }
    setRuleObject(ruleName: any, indexVal: any, value: any) {
        console.log('setRuleObject - ruleName ' + ruleName);
        console.log('setRuleObject - value ' + JSON.stringify(value));
        if (value && value.data && value.data.id)
            this.rulesService.fetchExistingRuleDetails(value.data.id).subscribe((res: any) => {
                let ruleObj: any = res;
                //  console.log('ruleObj'+JSON.stringify(ruleObj));
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule = ruleObj.rule;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate);
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate);
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions = ruleObj.ruleConditions;
            });
        // let rulesArray: Rules[] = [];
        // rulesArray = this.ruleListArrays[indexVal];
        // if ( value.data && value.data.rule && value.data.rule.id ) {
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule = value.data.rule;

        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate = this.dateUtils.convertLocalDateFromServer( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate );
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate = this.dateUtils.convertLocalDateFromServer( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate );
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions = value.data.ruleConditions;
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.newRule = false;

        //     this.sourceDataViewsAndColumns.forEach( dataViewObj => {
        //         if ( dataViewObj.id == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId ) {
        //             this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res:any)=>{
        //                 let dvAndCols= res;
        //                 if(dvAndCols[0] && dvAndCols[0].dvColumnsList)
        //                 this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
        //                 this.sourceColumnLOV[indexVal] = [];
        //                 this.sourceColumnLOV[indexVal] = ( this.selectedSrcDataViewAndColumns );
        //             });
        //         }
        //     } );
        //     this.targetDataViewsAndColumns.forEach( dataViewObj => {
        //         if ( dataViewObj.id == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId ) {
        //             this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res:any)=>{
        //                 let dvAndCols= res;
        //                 if(dvAndCols[0] && dvAndCols[0].dvColumnsList)
        //                 this.selectedTargetDataViewAndColumns = dvAndCols[0].dvColumnsList;
        //                 this.targetColumnLOV[indexVal] = [];
        //                 this.targetColumnLOV[indexVal] = ( this.selectedTargetDataViewAndColumns );
        //             });
        //         }
        //     } );



        // }
        // else {

        // }
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.setRuleObject = true;

    }
    changeState(create: boolean, view: boolean, edit: boolean) {
        this.isCreateNew = create;
        this.isViewOnly = view;
        this.isEdit = edit;
    }

    fetchGroupAndRulesAndRuleConditionsByGroupId(groupId: any) {
        this.ruleGroupService.getGroupDetails(groupId).subscribe((res: any) => {

            this.ruleGroupService.ruleGroupRulesAndConditions = res;
            //console.log('after get R-rules'+JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions));
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules.length > 0) {
                this.expandTab[0] = true;
            }
            this.ruleGroupTypes.forEach(ruleGRpLookUp => {
                if (ruleGRpLookUp.lookUpCode == this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose) {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose = ruleGRpLookUp.lookUpCode;
                }

            });
            if (!this.isCreateNew) {
                let i = 0;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach(rule => {
                   

                    this.ruletypeLOVArray[i] = jQuery.extend(true, [], this.ruletypeLOV);
                    this.ruletypeLOVArray[i].forEach(ruleType=>{
                        if(ruleType.setValue == rule.rule.ruleType)
                        ruleType.value=true;
                    });

                    // this.sourceColumnLOV[i] = [];
                   // this.targetColumnLOV[i] = [];
                    // if (rule.ruleConditions) {
                    //     if (rule.ruleConditions[0].sColumnList[0] && rule.ruleConditions[0].sColumnList[0].dvColumnsList) {
                    //         this.sourceColumnLOV[i] = rule.ruleConditions[0].sColumnList[0].dvColumnsList;
                    //         this.targetColumnLOV[i] = rule.ruleConditions[0].tColumnList[0].dvColumnsList;
                    //         //based on column id fetch operator lov
                    //     }
                    //     let j = 0;
                    //     rule.ruleConditions.forEach(condition => {
                    //         this.sourceColumnLOV[i].forEach(column => {
                    //             console.log(condition.sColumnId + '==' + column.id);
                    //             if (condition.sColumnId == column.id) {
                    //                 this.SelectColumn(i, j, column.id);
                    //             }
                    //         });
                    //         j = j + 1;
                    //     });

                    // }
                    i = i + 1;
                });
                //if(this.isEdit)
               // this.setFormGroupsForExisting();
            }
            else {
                console.log('it wont enter to create')
            }
        });
    }
    validationMsg() {

    }
    checkGroupName(name: string) {
        this.ruleGroupService.query().subscribe(
            (res: Response) => {
                this.ruleGroupsList = res.json();
                let count: number = 0;
                //////console.log(' this.ruleGroupsList'+JSON.stringify( this.ruleGroupsList));
                this.ruleGroupsList.forEach(ruleGroup => {
                    if (ruleGroup.name.toLowerCase() == name.toLowerCase()) {
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
    deleteCondition(i, childIndex) {
        let ruleCondLength :number=0;
        ruleCondLength =  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length;

        if (ruleCondLength > 0 )
            {
                if (childIndex > 0 && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2])
                {
                    this.logicalOpLOVArray[i][ruleCondLength - 2]=[];                
                    this.logicalOpLOVArray[i][ruleCondLength - 2] = jQuery.extend(true, [], this.logicalOperatorLOV);
                    this.logicalOpLOVArray[i][ruleCondLength - 2] .forEach(op=>{
                        op.value=false;
                    });
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2].logicalOperator=null;
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(childIndex, 1);
                }
                else 
                {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(childIndex, 1);
                        this.addNewCondition(i, childIndex);
                }
               
             
            }
            
         

    }
    deletRule(i) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules.splice(i, 1);
    }
    displayOptions() {
        this.showOptions = true;
    }
    filterDataView() {
        ////console.log('filtering dv');
        
        
    }
    openTargetFilters(i,childIndex)
    {
        let data ={
                        addFilter:'target',
                        tOperatorList: this.operatorList,
                        tOperator : this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tOperator,
                        tValue :  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tValue,
            
                        excelFunctions: this.excelFunctions,
                        tFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula,
                        tFormulaExpressionExample:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormulaExpressionExample,
                        isTFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isTFormula,
            
                        tToleranceType:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceType,
                        ttoleranceTypeLOV:this.ttoleranceTypeLOV,
                        tToleranceTypeMeaning:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceTypeMeaning,
                        tToleranceValueFrom: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom,
                        tToleranceOperatorFrom:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom,
                        tToleranceOperatorTo:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo,
                        tToleranceValueTo:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo,
            
                        tToOperands  : this.tToOperands,
                        tFromOperands : this.tFromOperands,
                        ok:'ok',
                        cancel:'cancel',
                        disableClose: true
                    };

                    var dialogRef = this.dialog.open(ReconConfirmActionModalDialog, {
                        data: data,
                        disableClose: true
                    });
            
            
                    dialogRef.afterClosed().subscribe(result => {
                        console.log('target filter result' + JSON.stringify(result));
                        if (result && result == 'ok') {
                            console.log('data'+JSON.stringify(data));
                          
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tOperator = data.tOperator;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tValue = data.tValue;
            
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula= data.tFormula;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormulaExpressionExample= data.tFormulaExpressionExample;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isTFormula= data.isTFormula;
                          
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceTypeMeaning=data.tToleranceTypeMeaning;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceType = data.tToleranceType;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = data.tToleranceOperatorFrom;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = data.tToleranceValueFrom;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = data.tToleranceOperatorTo;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo= data.tToleranceValueTo;
            
                        //  this.toOperands = data.toOperands ;
                         // this.fromOperands = data.fromOperands ;
                        }
                        else if (result && result == 'cancel') {
                          
                        }
                    });
    }
    openSrcFilters(i,childIndex)
    {
        
        let data ={  
            addFilter:'source',
            sOperatorList: this.operatorList,
            sOperator : this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sOperator,
            sValue :  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sValue,

            excelFunctions: this.excelFunctions,
            sFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula,
            sFormulaExpressionExample:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample,
            isSFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isSFormula,

            sToleranceType:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceType,
            stoleranceTypeLOV:this.stoleranceTypeLOV,
            sToleranceTypeMeaning:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceTypeMeaning,
            sToleranceValueFrom: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom,
            sToleranceOperatorFrom:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom,
            sToleranceOperatorTo:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo,
            sToleranceValueTo:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo,
           
            toOperands : this.toOperands,
            fromOperands : this.fromOperands,


            ok:'ok',
            cancel:'cancel',
            disableClose: true
        };
        var dialogRef = this.dialog.open(ReconConfirmActionModalDialog, {
            data: data,
            disableClose: true
        });


        dialogRef.afterClosed().subscribe(result => {
            console.log('filter result' + JSON.stringify(result));
            if (result && result == 'ok') {
                console.log('data'+JSON.stringify(data));
              
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sOperator = data.sOperator;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sValue = data.sValue;

              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula= data.sFormula;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample= data.sFormulaExpressionExample;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isSFormula= data.isSFormula;

              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceTypeMeaning=data.sToleranceTypeMeaning;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceType = data.sToleranceType;
                let sOperator : any ;
                sOperator = data.sToleranceValueFrom.toString();
               
                if(sOperator.startsWith('-'))
                {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '-';
                    //let output = sOperator.split("-");
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = sOperator;
                }
                else
                {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '+';
                    //let output = sOperator.split("+");
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = sOperator;
                }
                let tOperator : any ;
                tOperator = data.sToleranceValueTo.toString();
               
                if(tOperator.startsWith('-'))
                {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '-';
                   // let output = tOperator.split("-");
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = tOperator;
                }
                else
                {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '+';
                   // let output = tOperator.split("+");
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = tOperator;
                }
              
             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = data.sToleranceOperatorTo;
            //  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo= data.sToleranceValueTo;

            //  this.toOperands = data.toOperands ;
             // this.fromOperands = data.fromOperands ;
            }
            else if (result && result == 'cancel') {
              
            }
        });

    }
    addFilter(i: any, childIndex: any) {
        //this.sAdditionalFiltersTitle[childIndex] = this.sFilterTitle ;
        

        // if (!this.sFilter[i]) {
        //     this.sFilter[i] = [];
        // }
        // else {
        //     this.sFilter[i][childIndex] = '';

        // }
        // this.sFilter[i][childIndex] = '';
        // this.sFilter[i][childIndex] = true;
        // if (this.sFilter[i][childIndex]) {
        //     this.addDisplayPopOverContentClass();
        // }

        // if (this.sFormula[i] && this.sFormula[i][childIndex]) {
        //     this.sFormula[i][childIndex] = false;
        // }
        // if (this.sTolerance[i] && this.sTolerance[i][childIndex]) {
        //     this.sTolerance[i][childIndex] = false;
        // }
        // this.sFormula[childIndex] = false;
        //this.sTolerance[childIndex] = false;

    }
    addDisplayPopOverContentClass() {
        // $('.highlightActions li:eq(1)').addClass('highlight_stay');
        $('.rule-conditions-view').addClass('display-popoverbody');
        $('.rule-conditions-view .display-PopOverActions').removeClass('col-md-12').addClass('col-md-2');

    }
    addFormula(i: any, childIndex: any) {
        //this.sAdditionalFiltersTitle[childIndex] = this.sFormulaTitle;
        if (!this.sFormula[i]) {
            this.sFormula[i] = [];
        }
        else {

        }
        this.sFormula[i][childIndex] = '';
        this.sFormula[i][childIndex] = true;

        if (this.sFormula[i][childIndex])
            this.addDisplayPopOverContentClass();

        if (this.sTolerance[i] && this.sTolerance[i][childIndex]) {
            this.sTolerance[i][childIndex] = false;
        }
        if (this.sFilter[i] && this.sFilter[i][childIndex]) {
            this.sFilter[i][childIndex] = false;
        }
        // this.sFilter[childIndex]= false;
        // this.sTolerance [childIndex]= false;
    }
    addTolerance(i: any, childIndex: any) {
        //this.sAdditionalFiltersTitle[childIndex] = this.tToleranceTitle;
        if (!this.sTolerance[i]) {
            this.sTolerance[i] = [];
        }
        else {

        }
        this.sTolerance[i][childIndex] = '';
        this.sTolerance[i][childIndex] = true;

        if (this.sTolerance[i][childIndex])
            this.addDisplayPopOverContentClass();

        if (this.sFormula[i] && this.sFormula[i][childIndex]) {
            this.sFormula[i][childIndex] = false;
        }
        if (this.sFilter[i] && this.sFilter[i][childIndex]) {
            this.sFilter[i][childIndex] = false;
        }
        //this.sFormula[childIndex] = false;
        // this.sFilter [childIndex]= false;
    }
    selectOperator() {

    }
    addtFilter(i: any, childIndex: any) {
        // this.tAdditionalFiltersTitle[i][childIndex] = this.tFilterTitle ;
        if (this.tFilter[i]) {

        }
        else {
            this.tFilter[i] = [];
        }

        this.tFilter[i][childIndex] = '';
        this.tFilter[i][childIndex] = true;

        if (this.tFilter[i][childIndex])
            this.addDisplayPopOverContentClass();
        if (this.tFormula[i] && this.tFormula[i][childIndex]) {
            this.tFormula[i][childIndex] = false;
        }
        if (this.tTolerance[i] && this.tTolerance[i][childIndex]) {
            this.tTolerance[i][childIndex] = false;
        }
        //this.tFormula[childIndex] = false;
        //this.tTolerance[childIndex] = false;
    }
    addtFormula(i: any, childIndex: any) {
        //this.tAdditionalFiltersTitle[childIndex] = this.tFormulaTitle;
        if (this.tFormula[i]) {

        }
        else {
            this.tFormula[i] = [];
        }
        this.tFormula[i][childIndex] = '';
        this.tFormula[i][childIndex] = true;

        if (this.tFormula[i][childIndex])
            this.addDisplayPopOverContentClass();

        if (this.tFilter[i] && this.tFilter[i][childIndex]) {
            this.tFilter[i][childIndex] = false;
        }
        if (this.tTolerance[i] && this.tTolerance[i][childIndex]) {
            this.tTolerance[i][childIndex] = false;
        }
        //this.tFilter[childIndex] = false;
        //this.tTolerance[childIndex] = false;
    }
    addtTolerance(i: any, childIndex: any) {
        if (this.tTolerance[i]) {

        }
        else {
            this.tTolerance[i] = [];
        }
        this.tTolerance[i][childIndex] = '';
        this.tTolerance[i][childIndex] = true;

        if (this.tTolerance[i][childIndex])
            this.addDisplayPopOverContentClass();

        if (this.tFormula[i] && this.tFormula[i][childIndex]) {
            this.tFormula[i][childIndex] = false;
        }
        if (this.tFilter[i] && this.tFilter[i][childIndex]) {
            this.tFilter[i][childIndex] = false;
        }
        //this.tAdditionalFiltersTitle [childIndex]= this.tToleranceTitle;
        //this.tFormula[childIndex] = false;
        //this.tFilter[childIndex] = false;
    }
    selecttOperator() {

    }
    selectedLogicalOperator(logOperatorObj, i, childIndex) {

        let alreadySelected :number=0;
        this.logicalOpLOVArray[i][childIndex].forEach(op => {
            if(op.value == true)
            alreadySelected = 1;
        });
        this.logicalOpLOVArray[i][childIndex].forEach(op => {

            if (op.setValue == logOperatorObj.setValue)
            {
                //ruleType.value = ruleType.value ? false : true;
                if(op.value == true)
                {

                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOperator = logOperatorObj.setValue;
                }
                else{
                        op.value=true;
                    
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOperator = logOperatorObj.setValue;
                this.addNewCondition(i, childIndex + 1);
                }
            }
                
            else
            {
               // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOperator = null;
                op.value = false;
            }
                
        });

        // this.logicalOpLOVArray[i][childIndex].forEach(logOp => {
        //     if (logOp.setValue == logOperatorObj.setValue)
        //         logOp.value = logOp.value ? false : true;
        //     else
        //         logOp.value = false;

        //     if (logOp.value == true) {

        //     }
        //     else{
              
                
        //     }
                

        // });


    }
    showExcelFunction(val, i, childIndex, examp) {
        ////console.log('val  :' + val );
        //console.log('ind :' + childIndex);
        //console.log('examp :' + examp);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample = examp;
    }
    checkTargetAndRefresh() {
    }
    savesrcExpression(val, i, childIndex) {
        //console.log(val);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula = val;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sformulaSave = true;
        this.sFormula[i][childIndex] = false;
        this.removeAddedClasses();
    }
    savetargetExpression(val, i, childIndex) {
        //console.log(val);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula = val;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tformulaSave = true;
        this.tFormula[i][childIndex] = false;
        this.removeAddedClasses();
    }

    savesrcFilter(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sfilterSave = true;
        this.sFilter[i][childIndex] = false;
        this.removeAddedClasses();
        //.addClass('col-md-2');
    }
    savetargetFilter(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tfilterSave = true;
        this.tFilter[i][childIndex] = false;
        this.removeAddedClasses();

    }
    savesrcTolerance(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].stoleranceSave = true;
        this.sTolerance[i][childIndex] = false;
        this.removeAddedClasses();
    }
    savetargetTolerance(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].ttoleranceSave = true;
        this.tTolerance[i][childIndex] = false;
        this.removeAddedClasses();
    }

    cancelsrcFilter(i, childIndex) {

        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sOperator = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sValue = '';
        this.sFilter[i][childIndex] = false;
        //console.log(' this.sFilter[i][childIndex]'+ this.sFilter[i][childIndex]);
        this.removeAddedClasses();
    }
    cancelTargetFilter(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tOperator = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tValue = '';
        this.tFilter[i][childIndex] = false;
        this.removeAddedClasses();
    }
    cancelsrcFormula(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula = '';
        this.sFormula[i][childIndex] = false;
        this.removeAddedClasses();
    }
    canceltargetFormula(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula = '';
        this.tFormula[i][childIndex] = false;
        this.removeAddedClasses();
    }
    cancelsrcTolerance(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceType = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = '';
        this.sTolerance[i][childIndex] = false;
        this.removeAddedClasses();
    }
    canceltargetTolerance(i, childIndex) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceType = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '';
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = '';
        this.tTolerance[i][childIndex] = false;
        this.removeAddedClasses();
    }
    removeAddedClasses() {
        $('.rule-conditions-view').removeClass('display-popoverbody');
        $('.rule-conditions-view .display-PopOverActions').addClass('col-md-12');
    }
    closeSrc(i, childIndex) {
        if (this.sFilter[i] && this.sFilter[i][childIndex])
            this.sFilter[i][childIndex] = false;
        if (this.sFormula[i] && this.sFormula[i][childIndex])
            this.sFormula[i][childIndex] = false;
        if (this.sTolerance[i] && this.sTolerance[i][childIndex])
            this.sTolerance[i][childIndex] = false;
        //this.removeAddedClasses();
    }
    closeTarget(i, childIndex) {
        if (this.tFilter[i] && this.tFilter[i][childIndex])
            this.tFilter[i][childIndex] = false;
        if (this.tFormula[i] && this.tFormula[i][childIndex])
            this.tFormula[i][childIndex] = false;
        if (this.tTolerance[i] && this.tTolerance[i][childIndex])
            this.tTolerance[i][childIndex] = false;
        this.removeAddedClasses();
    }
    deleteTargetFilter() {

    }
    clearRuleObject(i) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i]) {
            let ruleObj = new RulesAndConditions();
            let rule = new Rules();
            ruleObj.rule = rule;
            let ruleConditions = new RuleConditions();
            ruleObj.ruleConditions = [];
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] = ruleObj;
            this.addNewCondition(i, 0);
        }
    }
    changeTargetDataView(val, i) {
        //console.log('$event target dv'+val);
        if (val == "") {
            //console.log('val is empty clear the target dv object');
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions) {

                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach(ruleCondition => {
                    ruleCondition.tColumnId = 0;
                });
            }

        }
        else if (val != "") {
            //console.log('target dv val is not empty ');
        }

    }
    changeSourceDataView(val, i) {
        //console.log('$event src dv'+val);
        if (val == "") {
            //console.log('val is empty clear the src dv object');
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions) {

                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach(ruleCondition => {
                    ruleCondition.sColumnId = 0;
                });
            }

        }
        else if (val != "") {
            //console.log('src dv val is not empty ');
        }

    }
    // changeRuleName( val, i ) {
    //     let matchedRule ={};
    //     let loopVariable = 0;
    //     let matched:boolean=false;
    //     if(this.filteredRuleList)
    //     this.filteredRuleList.forEach(rule=>{
    //         if(rule.rule.ruleCode === val )
    //             {
    //             matchedRule={"data":rule};
    //             matched = true;
    //             }
    //         else
    //             {
    //             }
    //         if(!matched)
    //         loopVariable=loopVariable+1;
    //         else
    //             {
    //             }

    //     });
    //     this.setRuleObject(val, i, matchedRule);    
    //     if ( val == "" || val == undefined ) {
    //         let ruleObj = new RulesAndConditions();
    //         let selectedRuletype = '';
    //         selectedRuletype = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleType;
    //         if ( this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode ) {
    //             let existingId: number = 0;
    //             if ( this.ruleListArrays[i] ) {
    //                 this.ruleListArrays[i].forEach( ruleObject => {
    //                     if ( ruleObject.rule.ruleCode == this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode ) {
    //                         existingId = ruleObject.rule.id;
    //                     }
    //                 } );
    //             }
    //             if ( existingId > 0 ) {
    //                 let rule = new Rules();
    //                 ruleObj.rule = rule;
    //                 ruleObj.rule.ruleType = selectedRuletype;
    //                 let ruleConditions = new RuleConditions();
    //                 ruleObj.ruleConditions = [];
    //                 this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] = ruleObj;
    //                 this.addNewCondition( i, 0 );

    //             }
    //             else {
    //                 this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode = '';
    //             }

    //         }

    //     }
    //     let ruleType = '';
    //     if ( this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleType ) {
    //         ruleType = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleType;
    //     }
    //     this.rulesService.getRules( this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose, ruleType ).subscribe(( res: any ) => {
    //         this.filteredRuleList = res;
    //         this.ruleListArrays[i] = this.filteredRuleList;
    //     } );
    // }
    deleteRuleObject(i) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules.splice(i, 1);
        this.ruletypeLOVArray.splice(i,1);
        this.sourceColumnLOV.splice(i,1);
        this.operatorBasedOnColumnLOV.splice(i,1);
        this.targetColumnLOV.splice(i,1);
        this.sFilter.splice(i,1);
        this.sFormula.splice(i,1);
        this.sTolerance.splice(i,1);
        this.tFilter.splice(i,1);
        this.tFormula.splice(i,1);
        this.tTolerance.splice(i,1);
        this.logicalOpLOVArray.splice(i,1);
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules.length == 0)
            this.addNewRuleObject(0, 0);
    }
    changeTemplate() {
        //console.log('change in template triggered');
    }
    /* ngDoCheck() {
         var changes = this.differ.diff(this.person);

         if (changes) {
           changes.forEachChangedItem((elt) => {
             if (elt.key === 'firstName' || elt.key === 'lastName' ) {
               this.person.fullName = this.person.firstName + ' ' + this.person.lastName;
             }
           });
         }
       }*/
    ngDoCheck() {

        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules) {
            var ruleListChanges = this.ruleGroupDiffer.diff(this.ruleGroupService.ruleGroupRulesAndConditions.rules);
            if (ruleListChanges) {
                ////console.log('==>1');
                //this detection is only when object is added / removed to array
                ////console.log('changes detected for list of rules');
                ////console.log('previous were detected for rule'+JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions.rules));
                this.checkForRules();
            }
            else {
                // //console.log('==>2');
                ////console.log('no addition / removal');
                this.checkForRules();
            }
        }

    }
    checkForRules() {
        let i: number = 0;
        //  //console.log('this.ruleGroupService.ruleGroupRulesAndConditions.rules length'+this.ruleGroupService.ruleGroupRulesAndConditions.rules.length);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach(rule => {
            // //console.log('check if rule is changed');
            var changesForRule = this.ruleObjDiffer.diff(rule.rule);
            var changesForRuleConditions = this.ruleConditionsDiffer.diff(rule.ruleConditions);

            if (changesForRule) {
                // //console.log('==>3');
                // //console.log('changes detected for rule'+JSON.stringify(rule));
                //
                changesForRule.forEachChangedItem(r => {
                    // //console.log('changed rule is ', JSON.stringify(r.currentValue));
                    // //console.log('previous val is ', JSON.stringify(r.previousValue))
                    // this.validateRuleObject(r.currentValue,r.previousValue);
                }

                );
                changesForRule.forEachAddedItem(r => {
                    ////console.log('added rule' + JSON.stringify(r.currentValue)
                }
                );
                changesForRule.forEachRemovedItem(r => {
                    // //console.log('removed ' + JSON.stringify(r.currentValue)
                }
                );
            } else {
                // //console.log('==>4');
                // //console.log('nothing changed for'+rule.rule.id);
            }

            if (changesForRuleConditions) {
                /* rule.ruleConditions.forEach(ruleCondition=>{
                        var changesForCondition = this.ruleConditionObjDiffer.diff(ruleCondition);
                        if(changesForCondition)
                            {
                                this.checkForConditions(i);
                            }
                    })*/
                //  //console.log('==>5');
                this.checkForConditions(i);
            }
            else {
                ////console.log('no change in rule conditions array);
                // //console.log('==>6');
                this.checkForConditions(i);
            }
            i = i + 1;
        });
    }
    checkForConditions(i) {
        let j: number = 0;
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions)
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach(ruleCondition => {
                var changesForCondition = this.ruleConditionObjDiffer.diff(ruleCondition);
                if (changesForCondition) {
                    ////console.log('changes in conditions'+JSON.stringify(ruleCondition));
                    //
                    ////console.log('==>7');
                    changesForCondition.forEachChangedItem(r => {
                        ////console.log('changed condition is ', JSON.stringify(r.currentValue))
                        // //console.log('previous val is ', JSON.stringify(r.previousValue))
                        // this.validateRuleObject(r.currentValue,r.previousValue);
                    }
                    );
                    changesForCondition.forEachAddedItem(r => {
                        ////console.log('Condition  added ' + JSON.stringify(r.currentValue)

                    }
                    );
                    changesForCondition.forEachRemovedItem(r => {
                        ////console.log('Condition removed ' + JSON.stringify(r.currentValue);
                    });
                }
                else {
                    // //console.log('==>8');
                }
            });

    }
    saveAdhocRule() {
        let groupId = [];
        groupId = this.accountingMode.toString().split("-");
        this.ruleGroupService.ruleGroupRulesAndConditions.adhocRuleCreation = true;
        this.ruleGroupService.ruleGroupRulesAndConditions.id = groupId[0];
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.ruleType = 'ADHOC';
        this.ruleGroupService.getGroupDetails(groupId[0]).subscribe((res: any) => {

            let ruleGroupById = new RuleGroupWithRulesAndConditions();
            ruleGroupById = res;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.assignmentFlag = true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.enabledFlag = true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.sourceDataViewId = this.ruleGroupService.sourceDataViewId;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.targetDataViewId = this.ruleGroupService.targetDataViewId;
            ruleGroupById.rules.push(this.ruleGroupService.ruleGroupRulesAndConditions.rules[0]);
            ruleGroupById.adhocRuleCreation = true;
            this.ruleGroupService.postAdhocruleForReconcile(ruleGroupById).subscribe((res: any) => {
                let savedObj = [];
                savedObj = res;

                this.notificationsService.success(
                    '',
                    'Job initiated with ' + this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.ruleCode
                )
                this.reconcileService.ENABLE_RULE_BLOCK = false;
            });

        });

    }
    /*validateRuleObject(currentRule,previousRule)
    {
        //console.log('currentRule'+JSON.stringify(currentRule));
        //console.log('previousRule'+JSON.stringify(previousRule));
    }*/
    /*saveToSession() {
        this.splitOperatorAndValues();
        this.$sessionStorage.store( 'reconciliationRuleList', this.ruleGroupService.ruleGroupRulesAndConditions );
        if ( this.ruleCreationInWQ ) {
            this.ruleGroupService.ruleGroupRulesAndConditions .rulePurpose = 'ORPHAN_RECON';
            this.ruleGroupService.ruleGroupRulesAndConditions .name = 'Orphan Recon';
            this.ruleGroupService.ruleGroupRulesAndConditions .startDate = new Date();
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.ruleType='ADHOC';
            let groupId = [];
            groupId = this.accountingMode.toString().split("-");
            this.ruleGroupService.ruleGroupRulesAndConditions.id = groupId[0];
            this.ruleGroupService.getGroupDetails( groupId[0] ).subscribe(( res: any ) => {

               let ruleGroupById = new RuleGroupWithRulesAndConditions();
               ruleGroupById = res;
               this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.assignmentFlag=true;
               this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.enabledFlag=true;
               ruleGroupById.rules.push(this.ruleGroupService.ruleGroupRulesAndConditions.rules[0]);
               this.ruleGroupService.postRuleGroup(  ruleGroupById).subscribe(( res: any ) => {
                   let savedObj = [];
                   savedObj = res;
                   
                   this.notificationsService.success(
                           '',
                           'Job initiated with '+this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.ruleCode
                       )
                   this.reconcileService.ENABLE_RULE_BLOCK = false;
                     //  $(".ui-dialog ui-widget ui-widget-content").css('display','none');
               } );
             
            });
       
           
        }
        else {
            let i: number = 0;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach( rule => {

                i = i + 1;
                rule.rule.priority = i;
            } );    
            let msg = '';
            if ( this.ruleGroupService.ruleGroupRulesAndConditions.rules.length > 1 )
                msg = ' rules tagged successfully!';
            if ( this.ruleGroupService.ruleGroupRulesAndConditions.rules.length == 1 )
                msg = ' rule tagged successfully!';
            this.notificationsService.success(
                '',
                this.ruleGroupService.ruleGroupRulesAndConditions.rules.length + msg
            )
        }
       
    }*/
    splitOperatorAndValues() {
        for (var i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
            for (var j = 0; j < this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length; j++) {
                if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id) {
                }
                else {
                    this.splitsToleranceFrom(i, j);
                    this.splitsToleranceTo(i, j);
                    this.splittToleranceFrom(i, j);
                    this.splittToleranceTo(i, j);

                }

            }
        }
    }
    enableSMany(i, j) {

    }
    enableTMany(i, j) {

    }

    splitsToleranceFrom(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom) {
            let valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom;
            let values: string[];


            //console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = values[1];
            }
            else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = values[1];
            }
            else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '+';
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom= values[1]; 
            }
        }

    }

    splitsToleranceTo(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo) {
            let valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo;
            let values: string[];

            //console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = values[1];
            }
            else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = values[1];
            }
            else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '+';
            }
        }

    }
    splittToleranceFrom(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom) {
            let valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom;
            let values: string[];


            // console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = values[1];
            }
            else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = values[1];
            }
            else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '+';
            }
        }

    }
    splittToleranceTo(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo) {
            let valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo;
            let values: string[];

            //console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = values[1];
            }
            else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = values[1];
            }
            else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '+';
            }


        }

    }


    selectValue() {
    }
    checkIfRuleCodeisUndefined(i) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode == undefined) {
            console.log('rule code is undefined');
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode = '';
        }
    }
    //selectedRuleType(indexVal:any)
    //{
    //  let selectedRuleType = '';
    // this.fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal);
    // if ( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.id > 0 ) {
    //    selectedRuleType = this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType;           
    //    this.addNewRuleObject( indexVal, 0 );
    //    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType = selectedRuleType;
    // }
    // this.ruletypeLOV.forEach(rtype=>{
    //     if(rtype.lookUpCode == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType)
    //     {
    //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleTypeMeaning = rtype.meaning;
    //     }
    // });
    //}
    selectedRuleType(ruleType, i) {
        this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleType = ruleType.setValue;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleTypeMeaning = ruleType.code;

        let alreadySelected :number=0;
        this.ruletypeLOVArray[i].forEach(rtype => {
            if(rtype.value == true)
            alreadySelected = 1;
        });
        this.ruletypeLOVArray[i].forEach(rtype => {

            if (rtype.setValue == ruleType.setValue)
            {
                //ruleType.value = ruleType.value ? false : true;
                if(ruleType.value == true)
                {


                }
                else{
                        ruleType.value=true;
                    
                }
            }
                
            else
            {
                
                rtype.value = false;
            }
                
        });
    }
    checkIfBlurCalled(i,inputRuleName)
    {
        console.log('blur called with '+i+' inputRuleName '+inputRuleName);
    }
    restrictTab(code)
    {
        if(code == 9)
        return false;
        else
        return true;
    }
    startDateChanged(dt:Date,i){
        if(  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate){
            if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate<this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate){
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate=this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate;
            }
        }
    }
 
}
