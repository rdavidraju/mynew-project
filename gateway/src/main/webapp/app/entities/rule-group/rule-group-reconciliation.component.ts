
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
//import { NotificationsService } from 'angular2-notifications-lite';
import { SessionStorageService } from 'ng2-webstorage';
import { RuleGroupAndRuleWithLineItem } from './ruleGroupAndRuleWithLineItem.model';
import { IterableDiffers, KeyValueDiffers } from '@angular/core';
import { ReconcileService } from '../reconcile/reconcile.service';
import { RuleGroup } from './rule-group.model';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import {ReconConfirmActionModalDialog} from './rule-group-recon-confirm-action-dialog';
import { AccountingConfirmActionModalDialog } from './rule-group-accounting-confirm-action-dialog';
import * as $ from "jquery";
declare var jQuery: any;
const URL = '';
@Component({
    selector: 'jhi-rule-group-reconciliation',
    templateUrl: './rule-group-reconciliation.component.html'
})
export class RuleGroupReconciliationComponent implements OnInit {
    toleranceTypeLOV=[];
    
    openBracket="(";
    closeBracket=")";
    dataViewsToColumnsMap:any;
    varcharDecDateLookupsMap:any
    //openRuleTab : any =[];
    ngxScrollToLastRule:string;
    today=new Date();    
    toOperands=[];
    fromOperands=[];
    tToOperands=[];
    tFromOperands=[];
    //logicalOpLOVArray = [];
   // ruletypeLOVArray = [];
    ruleTypePanelOpenState = false; 
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
    @Input() accountingMode = '';
    ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
    myData: any = '';
    filterAction: any = '';
    duplicateRuleGroupName = false;
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
    showOptions = false;
    mouseOverRule  = -1;
    mouseOverCondition  = -1;
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
    ruleCreationInWQ = false;
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    copyRule =false;

    create = 'create';
    edit = 'edit';
    view = 'view';

   // ruleListArrays: any[] = [];
    //filteredRuleList: any[] = [];

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
    open = true;
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

    //sourceDataViewsAndColumns = [];
    dataViewsAndColumns = [];
    //sourceColumnLOV = [];
    //targetColumnLOV = [];

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
   // operatorBasedOnColumnLOV = [];
    stoleranceTypeLOV = [];
    ttoleranceTypeLOV=[];
    //
    FilterTitle = 'Filters';
    FormulaTitle = 'Formulae';
    ToleranceTitle = 'Tolerance ';
    additionalFilters = '  Additional filters ';
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
        //private notificationsService: NotificationsService,
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
        const ruleObj1: RuleConditions = new RuleConditions();
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex] = {};

        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex] = ruleObj1;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex].sequenceNo = childIndex + 1;
        this.sAdditionalFiltersTitle[childIndex] = this.additionalFilters;
        this.tAdditionalFiltersTitle[childIndex] = this.additionalFilters;
       // if (!this.operatorBasedOnColumnLOV[indexVal])
          //  this.operatorBasedOnColumnLOV[indexVal] = [];
      //  this.operatorBasedOnColumnLOV[indexVal][childIndex] = [];

        //if (!this.logicalOpLOVArray[indexVal])
        //    this.logicalOpLOVArray[indexVal] = [];
        //if (!this.logicalOpLOVArray[indexVal][childIndex])
       //     this.logicalOpLOVArray[indexVal][childIndex] = [];
       this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex].logicalOpLOVArray= jQuery.extend(true, [], this.logicalOperatorLOV);
       
    }
    copyAndCreateNewRule(indexToAdd:any,i:any){
        this.addNewRuleObject(indexToAdd,0);
        this.populateValues(indexToAdd,i);
    }
    populateValues(indexToAdd,i) {
            //if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate && this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate)>=new Date())
           // {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.endDate = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate;
           // }
           // else
           // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate = new Date();

          //  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate ;
          //  console.log(' this.dateUtils.convertLocalDateFromServer('+ this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate));
           // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate && this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate) >=new Date())
            //{
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate =  new Date();
           // }
           // else
           // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate = new Date();

            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.endDate = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.sourceDVId = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceDVId ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.sourceDataViewId = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceDataViewId ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.sDataViewName = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sDataViewName ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.sDataViewDisplayName = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sDataViewDisplayName ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.targetDVId = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetDVId ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.targetDataViewId = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetDataViewId ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.tDataViewName = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.tDataViewName ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.tDataViewDisplayName = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.tDataViewDisplayName ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.sourceColumnLOV = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV ;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.targetColumnLOV = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetColumnLOV ;

            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].ruleConditions=JSON.parse(JSON.stringify( 
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions));

            //this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.ruleCode='';
          //  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.ruleName='';
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.ruleGroupAssignId = null;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.id=null;

            for(let c=0;c<this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].ruleConditions.length;c++) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].ruleConditions[c].id=null;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].ruleConditions[c].ruleId=null;
            }
           // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].openRuleTab=;
            for(let c = 0;c<this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;c++) {
                //if(c==indexToAdd)
               // this.openRuleTab[c] =true;
               // else
               this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].openRuleTab=false;
            }
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].openRuleTab=true;
            // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.endDate)
            // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.endDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.endDate);
            // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate)
            //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.startDate = new Date (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate );
            
           // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.today)
            //this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.today=new Date (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.today );
           // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexToAdd].rule.editRule){
 
           // }
           // else{
                //fetch all lovs
           // }
            
    }
    foo(event: Event) {
        event.stopPropagation();
      }
    addNewRuleObject(indexVal: any, childIndex: any) {
        for (let i = 0; i <= indexVal; i++) {
            this.expandTab[i] = false;
        }
        this.expandTab[indexVal] = true;
        
        const ruleConditions = new RuleConditions();
        const rule = new Rules();
        rule.formCntrl=new FormControl('');
        rule.ruleCategory = 'new';
        rule.priority = indexVal + 1;
        const rulesConditionsListObj: RuleConditions[] = [];
        rulesConditionsListObj[0] = (ruleConditions);
       

        const rulesWithConditionsObj = new RulesAndConditions();

        rulesWithConditionsObj.rule = rule;
        rulesWithConditionsObj.rule.formCntrl=new FormControl('');
        rulesWithConditionsObj.ruleConditions = rulesConditionsListObj;
        if (indexVal == 0) {
            this.ruleGroupService.ruleGroupRulesAndConditions['rules'] = [];
        } else {

        }
        
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal] = (rulesWithConditionsObj);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.priority = indexVal + 1;
               this. addnewLists(indexVal,0);
        //rule list array based on ruleGrouptype
        this.fetchRulesAndDataViewsListsBasedOnRuleGroupType(indexVal);
        this.setDataViewsArrays(indexVal);

        //this.ruletypeLOVArray[indexVal] = jQuery.extend(true, [], this.ruletypeLOV);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray = jQuery.extend(true, [], this.ruletypeLOV);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray.forEach((op) => {
            op.value = false;

        });
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray &&
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray.length>0 && 
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray[0]){
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray[0].value=true;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType=this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray[0].setValue;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleTypeMeaning = this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruletypeLOVArray[0].code;
            }
        
        

       // this.operatorBasedOnColumnLOV[indexVal] = [];
        if (this.accountingMode.toString().endsWith('WQ')) {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = this.ruleGroupService.sourceDataViewId;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = this.ruleGroupService.targetDataViewId;
            this.fetchSrcColumns(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId, 0);
            this.fetchTargetColumns(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId, 0);
        }
        this.addNewCondition(indexVal,childIndex);
       
        for(let c=0;c<this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;c++) {
            
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].openRuleTab=false;
        }
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].openRuleTab=true;
        this.ngxScrollToLastRule = 'ruleScroll_'+(this.ruleGroupService.ruleGroupRulesAndConditions.rules.length-1);
    }
    setDataViewsArrays(indexVal: any) {
        //this.sourceDataViewsArrays[indexVal] = this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].sourceDataViewsAndColumns;
       // this.targetDataViewsArrays[indexVal] = this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].targetDataViewsAndColumns;
       this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].sourceDataViewsAndColumns = this.dataViewsAndColumns;
       this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].targetDataViewsAndColumns = this.dataViewsAndColumns;
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
        for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
            let ruleListForm: FormGroup;
            ruleListForm = this.builder.group({
                data: "",
            });
            this.ruleListformArray[i]= ruleListForm;

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
        this.lookUpCodeService.varcharDecimalDatelookUpsByLookUpType().subscribe((res:any)=>{
            const varcharDecDateLookupsMap =res;
            this.varcharDecDateLookupsMap=varcharDecDateLookupsMap;
            if(this.varcharDecDateLookupsMap && this.varcharDecDateLookupsMap['VARCHAR'])  {
                this.operatorList = this.varcharDecDateLookupsMap['VARCHAR'];
            }
            if(this.varcharDecDateLookupsMap && this.varcharDecDateLookupsMap['RECONCILIATION_FUNCTIONS']){
                this.excelFunctions =this.varcharDecDateLookupsMap['RECONCILIATION_FUNCTIONS'];
            }
            if(this.varcharDecDateLookupsMap && this.varcharDecDateLookupsMap['TOLERANCE_TYPE']) {
                this.stoleranceTypeLOV =this.varcharDecDateLookupsMap['TOLERANCE_TYPE'];
                this.ttoleranceTypeLOV = this.stoleranceTypeLOV;
            }
            if(this.varcharDecDateLookupsMap && this.varcharDecDateLookupsMap['OPERATOR'])  {
                this.operatorLOV =this.varcharDecDateLookupsMap['OPERATOR'];
            }
            if(this.varcharDecDateLookupsMap && this.varcharDecDateLookupsMap['RULE_GROUP_TYPE'])  {
                this.ruleGroupTypes =this.varcharDecDateLookupsMap['RULE_GROUP_TYPE'];
            }
        });
        
        // this.lookUpCodeService.fetchLookUpsByLookUpType('RECONCILIATION_FUNCTIONS').subscribe((res: any) => {
        //     this.excelFunctions = res;
        // });

        //operator list for filters
        // this.lookUpCodeService.fetchLookUpsByLookUpType('VARCHAR').subscribe((res: any) => {
        //     this.operatorList = res;
        // });
        // this.lookUpCodeService.fetchLookUpsByLookUpType('TOLERANCE_TYPE').subscribe((res: any) => {
        //     this.stoleranceTypeLOV = res;
        //     this.ttoleranceTypeLOV = jQuery.extend(true, [], this.stoleranceTypeLOV);
        // });

        // this.lookUpCodeService.fetchLookUpsByLookUpType('OPERATOR').subscribe((res: any) => {
        //     this.operatorLOV = res;
        // });

        // this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((res: any) => {
        //     this.ruleGroupTypes = res;
        // });


    }
    ngOnInit() {
        $('.cuppaSingleSelection .cuppa-dropdown .selected-list .c-btn span:nth-child(3)').addClass('mat-select-arrow');
        $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn span:nth-child(3)').removeClass('fa-angle-down');
        $('.cuppaSingleSelection .cuppa-dropdown .selected-list .c-btn span:nth-child(3)').removeClass('fa-angle-up');
        this.ruleGroupService.submitted=false;
        ////console.log('in rule recon');
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) + 'px'
        });


        // 
        //create new
        // let ruleListForm: FormGroup;
        // ruleListForm = this.builder.group({
        //     data: "",
        // });
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.ruleListformArray = ruleListForm;
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
            const ruleTypeLOV = res;
            ruleTypeLOV.forEach((ruleType) => {
                const rTypeObj = { "code": ruleType.meaning, "name": ruleType.description, "setValue": ruleType.lookUpCode, "value": false };
                this.ruletypeLOV.push(rTypeObj);
            });
            this.lookUpCodeService.fetchLookUpsByLookUpType('LOGICAL_OPERATOR').subscribe((resp: any) => {
                const logicalOperatorLOV = resp;
                logicalOperatorLOV.forEach((logOp) => {
                    const logOpObj = { "code": logOp.meaning, "name": logOp.description, "setValue": logOp.lookUpCode, "value": false };
                    this.logicalOperatorLOV.push(logOpObj);
                });
                const url = this.route.snapshot.url.map((segment) => segment.path).join('/');

                this.routeSub = this.route.params.subscribe((params) => {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose = 'RECONCILIATION';
                    ////console.log('params'+params['id']+'URL'+url);
                    if (params['id']) {
        
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            
                            this.fetchAllLookUps();
                            this.dataViewsService.dataViewsListByTenantId().subscribe((res1: any) => {
                                const sourceDataViewsAndColumns = res1;
                                if(sourceDataViewsAndColumns) {
                                    sourceDataViewsAndColumns.forEach((dv) => {
                                        dv['itemName'] = dv['dataViewDispName'];
                                        dv['id'] = dv['idForDisplay'];
                                        dv['display'] =false;
                                    });
                                    this.dataViewsAndColumns = sourceDataViewsAndColumns;
                                    if (url.search('copy') != -1) {
                                        this.copyRule = true;
                                        
                                    }
                                    this.fetchDataViewsToColumnsMap(params['id']);
                                }
                                
                            });
                        } else {
        
                            let viewData: any = [];
                            viewData = this.accountingMode.toString().split('-');
                            this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                            this.isViewOnly = true;
                           
                        }
                    } else {
                        this.fetchAllLookUps();
                        if (this.accountingMode.toString().endsWith('WQ')) {
                            this.ruleCreationInWQ = true;
                        }
                        this.fetchExistingRules();
                        this.dataViewsService.dataViewsListByTenantId().subscribe((res2: any) => {
                            const sourceDataViewsAndColumns = res2;
                            sourceDataViewsAndColumns.forEach((dv) => {
                                dv['itemName'] = dv['dataViewDispName'];
                                dv['id'] = dv['idForDisplay'];
                                dv['display'] =false;
                            });
                            this.dataViewsAndColumns = sourceDataViewsAndColumns;
                            this.addNewRuleObject(0, 0);
                            if(this.ruleCreationInWQ){
                            this.fetchRuleValuesForWQ();
                        }
                        //    this.addNewCondition(0, 0);
        
                            this.isCreateNew = true;
                            //if(!this.ruleCreationInWQ)
                            this.fetchDataViewsToColumnsMap();
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

            });
           

        });
       


        /*  this.dataViewsService.dataViewList().subscribe((res:any)=>{
              this.sourceDataViewsAndColumns = res;
              this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns;
          });*/

       

    }
    fetchDataViewsToColumnsMap(id?:any) {
        this.dataViewsService.fetchDataViewsToColumnsMap().subscribe((res:any)=>{
            const dataViewsToColumnsMap= res;
            this.dataViewsToColumnsMap= dataViewsToColumnsMap;
            if(this.isEdit){
                this.fetchGroupAndRulesAndRuleConditionsByGroupId(id);
            } else{
                this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap=this.dataViewsToColumnsMap;
            }
        });
        
    }
    fetchExistingRules()  {
    this.rulesService.getRules(this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose,null).subscribe((res: any) => {
        let rulesFetched: Rules[] = [];
        rulesFetched = res;
        for(let c=0;c<rulesFetched.length;c++)  {
            rulesFetched[c]['itemName'] = rulesFetched[c].ruleCode;
        }
        this.ruleGroupService.ruleGroupRulesAndConditions.existingRuleListLOV=rulesFetched;
    });
    }
    fetchRuleValuesForWQ() {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.editRule=true;
        this.dataViewsService.getDataViewById(this.ruleGroupService.sourceDataViewId).subscribe((res:any)=>{
            const dvAndCols= res;
           // if(dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
            this.selectedSrcDataViewAndColumns = dvAndCols;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.sourceColumnLOV = [];
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.sourceColumnLOV = ( this.selectedSrcDataViewAndColumns );
        });

        this.dataViewsService.getDataViewById(this.ruleGroupService.targetDataViewId).subscribe((res:any)=>{
            const dvAndCols= res;
           // if(dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList)
            this.selectedTargetDataViewAndColumns = dvAndCols;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.targetColumnLOV= [];
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.targetColumnLOV = (this.selectedTargetDataViewAndColumns);
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
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules){
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
                    this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
                }
            }
                
        }
    }

    fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal: any) {
        //get rule type at that index and filter, and update the filter values
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal]) {
            let ruleType = '';
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType) {
                ruleType = this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType;
            } else { 
                ruleType = '';
            }
            //this.rulesService.getRules(this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose, ruleType).subscribe((res: any) => {
               // let rulesFetched: Rules[] = [];
               // rulesFetched = res;
               // this.filteredRuleList = [];
                //if (rulesFetched)
                //    rulesFetched.forEach(rule => {
                 //       this.filteredRuleList.push(rule);
                 //   });
               // this.ruleListArrays[indexVal] = [];
               // this.ruleListArrays[indexVal] = this.filteredRuleList;
                //this.addnewLists(indexVal, 0);

           // });
            //enable smany tmany
            //////console.log( 'for enabling' + this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType );
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType == 'ONE_TO_MANY') {
                //enable tmany
                this.tManyEnable[indexVal] = [];
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.tManyEnable[indexVal]) {
                        this.tManyEnable[indexVal][i] = true;
                    }
                }
            } else if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType == 'MANY_TO_ONE') {
                this.sManyEnable[indexVal] = [];
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.sManyEnable[indexVal]) {
                        this.sManyEnable[indexVal][i] = true;
                    }
                }
            } else if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType == 'MANY_TO_MANY') {
                this.tManyEnable[indexVal] = [];
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.tManyEnable[indexVal]) {
                        this.tManyEnable[indexVal][i] = true;
                    }
                }
                this.sManyEnable[indexVal] = [];
                for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; i++) {
                    if (this.sManyEnable[indexVal]) {
                        this.sManyEnable[indexVal][i] = true;
                    }
                }
            }
        }


    }
    clearSourceDV(indexVal: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId =null;
    }
    fetchTableWidthAndDivide(){
        
        console.log('width has value of rule conditions as'+$("#rule-conditions-table-styles").width());
    }
    fetchSrcColumns(dvid: any, indexVal: any,set?:any) {
        
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
        // if( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId && this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId && (
        //     JSON.stringify( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId) == 
        // JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId))){
        //     this.commonService.error('Source and target sources cannot be same','Source and target sources cannot be same' );
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId=null;
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId=null;
        //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName='';

        //     if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId && this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName){
        //         let selectedDv = {
        //             "id": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId,
        //             "itemName":  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName
        //          };
        //          this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId=[];
        //          this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId.push(selectedDv);
        //     }else{
        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId=null;
        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId=null;
        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName='';
        //     }
           
        // }else{
            if (!this.ruleCreationInWQ) {
                if(set == 'set')   {
                   
                   /*******************************************************************************/
                 /*
                 * Reset all the dependencies
                 * Recon data source, entered currecny, reconciliation status, accounting status, header derivation rules, delete all line derivation rules. 
                 */
                 /*******************************************************************************/
                 if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId) {
                    const selectedDv = {
                        "id": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId,
                        "itemName":  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName
                     };
                     const data = {
                        refreshDataViews : 'refreshDataViews',
                        message  : 'Rule conditions will be cleared. Are you sure to continue ? ',
                        ok : 'ok',
                        no : 'No Thanks'
                    }
                    const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
                        data, disableClose: true
                    });
                    dialogRef.afterClosed().subscribe((result) => {
                        if (result && result == 'ok') {
                            this. setDv(indexVal,dvid);
                        }  else{
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId=[];
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId.push(selectedDv);
                        }
                    });
                 }  else{
                    const selectedDv = {
                        "id": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId,
                        "itemName":  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName
                     };
                     this. setDv(indexVal,dvid);
                 }
                 
                } else{
                    this. setDv(indexVal,dvid);
                }
             
            }else  {
                
                // this.dataViewsService.getDataViewById(dvid).subscribe((res:any)=>{
                //                     let dvAndCols= res;
                //                     this.selectedSrcDataViewAndColumns = dvAndCols;
                //                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = [];
                //                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = ( this.selectedSrcDataViewAndColumns );
                //                 });
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = [];
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = 
                this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap[dvid];
                
            }
       // }
        // $('.cuppaSingleSelection .cuppa-dropdown .selected-list .c-btn:last-child').removeClass('fa-angle-up');
        // $('.cuppaSingleSelection .cuppa-dropdown  .selected-list .c-btn:last-child').removeClass('fa-angle-down');
        // $('.cuppaSingleSelection .cuppa-dropdown .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
        ////////console.log('mode->'+this.isdata-viEdit+'value is'+JSON.stringify(value));
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
    setDv(indexVal,dvid) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = dvid.id;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = dvid.itemName;
        
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId = dvid.id;
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName = dv.dataViewName;
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = '';
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName =dv.dataViewDispName;

        // this.dataViewsService.getDataViewById(dvid.id).subscribe((res: any) => {
        //     let dvAndCols = res;
        //         this.selectedSrcDataViewAndColumns = dvAndCols;
        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV= [];
        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = (this.selectedSrcDataViewAndColumns);
        // });
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV= [];
        if(dvid && dvid.id)  {
            //console.log('  this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap length '+  this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap.length);
            if( this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap){
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV =        this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap[dvid.id];
            this.fetchTableWidthAndDivide();
            }
        
    }
    }
    populateSourceColumns(indexVal: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV[indexVal] = [];
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0){
            for (let childIndex = 0; childIndex < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; childIndex++) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].sourceDataViewsAndColumns.forEach((dataViewObj) => {
                    if (dataViewObj.id == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId) {
                        // this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
                        //     let dvAndCols = res;
                        //         this.selectedSrcDataViewAndColumns = dvAndCols;
                        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV= [];
                        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = (this.selectedSrcDataViewAndColumns);
                        // });
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV= [];
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV=
                        this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap[dataViewObj.id];
                    }
                });
            }
        }
           
    }
    clearTargetDV(indexVal: any)  {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId =null;
    }
    fetchTargetColumns(dvid: any, indexVal: any,set?:any) {
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
            // if( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId && this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId && (
            //     JSON.stringify( this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId) == 
            // JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId))){
            //     this.commonService.error('','Source and traget data sources cannot be same');
            //     if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId && this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName){
            //         let selectedDv = {
            //             "id": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId,
            //             "itemName":  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName
            //          };
            //          this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId=[];
            //          this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId.push(selectedDv);
            //     }else{
            //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId=null;
            //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId=null;
            //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName='';
            //     }
               
    
            // }else{
                if (!this.ruleCreationInWQ) {
                    if(set == 'set') {
                        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId)   {
    
                        
                        const selectedDv = {
                            "id": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId,
                            "itemName":  this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName
                         };
                       /*******************************************************************************/
                     /*
                     * Reset all the dependencies
                     * Recon data source, entered currecny, reconciliation status, accounting status, header derivation rules, delete all line derivation rules. 
                     */
                     /*******************************************************************************/
                     const data = {
                        refreshDataViews : 'refreshDataViews',
                        message  : 'Rule conditions will be cleared. Are you sure to continue ? ',
                        ok : 'ok',
                        no : 'No Thanks'
                    }
                    const dialogRef = this.dialog.open(AccountingConfirmActionModalDialog, {
                        data, disableClose: true
                    });
                    dialogRef.afterClosed().subscribe((result) => {
                        if (result && result == 'ok') {
                            this.setTargetDV(indexVal,dvid);
                        }  else{
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId=[];
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId.push(selectedDv);
                        }
                    });} else{
                        this.setTargetDV(indexVal,dvid);
                    }
                    }  else{
                        this.setTargetDV(indexVal,dvid);
                    }
                 
                }  else {
                       
                    //    this.dataViewsService.getDataViewById(dvid).subscribe((res:any)=>{
                    //                        let dvAndCols= res;
                    //                        this.selectedTargetDataViewAndColumns = dvAndCols;
                    //                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV= [];
                    //                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = (this.selectedTargetDataViewAndColumns);
                    //                    });
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV= [];
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV=
                    this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap[dvid];
                }
           // }
           
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
    setTargetDV(indexVal,dvid) {

        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = dvid.id;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = dvid.itemName;
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName = value.data.dataViewName;

        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = '';
        // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = value.data.dataViewDispName;

    //     this.dataViewsService.getDataViewById(dvid.id).subscribe((res: any) => {
    //         let dvAndCols = res;
    //             this.selectedTargetDataViewAndColumns = dvAndCols;
    //             this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = [];
    //             this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV= (this.selectedTargetDataViewAndColumns);
    //    });
    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = [];
    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = 
    this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap[dvid.id];
    this.fetchTableWidthAndDivide();
    }
    populateTargetColumns(indexVal: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = [];
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0){
            for (let childIndex = 0; childIndex < this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length; childIndex++) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].targetDataViewsAndColumns.forEach((dataViewObj) => {
                    if (dataViewObj.id == this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId) {
                        // this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
                        //     let dvAndCols = res;
                        //         this.selectedTargetDataViewAndColumns = dvAndCols;
                        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV= [];
                        //         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = (this.selectedTargetDataViewAndColumns);
                        // });
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV= [];
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV=
                        this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap[dataViewObj.id];
                    }
                });
            }
        }
    }
    autocompleListFormatter = (data: any) => {
        const html = `<span >${data.ruleCode}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml(html);
    }
    autocompleteSrcColumnListFormatter = (data: any) => {
        const html = `<span style='color:blue'>${data.columnName}  </span>`;
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
        let lengthOfRules = 0;
        if (this.ruleGroupService.ruleGroupRulesAndConditions && this.ruleGroupService.ruleGroupRulesAndConditions.rules) {
            lengthOfRules = this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;
            if (this.ruleListformArray.length < lengthOfRules) {

                let myForm: FormGroup;
                myForm = this.builder.group({
                    data: "",
                });
                this.ruleListformArray[indexVal] = (myForm);
                if (this.srcDataViewformsArray.length < lengthOfRules) {
                    let myForm1: FormGroup;
                    myForm1 = this.builder.group({
                        data: "",
                    });
                    // this.srcDataViewformsArray[indexVal] = [];
                    this.srcDataViewformsArray[indexVal] = (myForm1);

                    //set columns LOV 
                   // this.sourceColumnLOV[indexVal] = [];
                   // this.sourceColumnLOV[indexVal][childIndex] = [];

                } else {
                }

                if (this.targetDataViewformsArray.length < lengthOfRules) {
                    let form1: FormGroup;
                    form1 = this.builder.group({
                        data: "",
                    });
                    //this.targetDataViewformsArray[indexVal]=[];
                    this.targetDataViewformsArray[indexVal] = (form1);

                    //target columns LOV
                    //this.targetColumnLOV[indexVal] = [];
                   // this.targetColumnLOV[indexVal][childIndex] = [];
                }  else {
                }
            }

        }
    }
    filter0(name: string, indexVal: any): any[] {

        return this.optionsArray[indexVal].filter((option) =>
            new RegExp(`^${name}`, 'gi').test(option.ruleCode));

    }
    // filter1(name: string): any[] {
    //     return this.sourceDataViewsAndColumns.filter(option => new RegExp(`^${name}`, 'gi').test(option.dataViewDispName));
    // }
    // filter2(name: string): any[] {
    //     return this.targetDataViewsAndColumns.filter(option => new RegExp(`^${name}`, 'gi').test(option.dataViewDispName));
    // }
    // filter3(name: string): any[] {
    //     return this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV.filter(option => new RegExp(`^${name}`, 'gi').test(option.columnName));
    // }
    // filter4(name: string): any[] {
    //     return this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV.filter(option => new RegExp(`^${name}`, 'gi').test(option.columnName));
    // }

    displayFnRuleName(rule: any): string {
        //this.rules = '';
        return rule ? rule : rule;
    }
    // filterSrcToTargetViews(sCol: any) {
    //     this.targetDataViewsAndColumns.forEach(targetObj => {
    //         if (targetObj.dataViewDispName === sCol) {
    //             let index = this.targetDataViewsAndColumns.indexOf(targetObj);
    //             this.targetDataViewsAndColumns = this.targetDataViewsAndColumns.splice(index, 0);
    //         }
    //     });
    // }
    // selectsrcColumns(sCol: any) {
    //     if (this.sourceDataViewsAndColumns) {
    //         this.sourceDataViewsAndColumns.forEach(dataViewObj => {
    //             if (dataViewObj.dataViewDispName === sCol) {
    //                 this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
    //                     let dvAndCols = res;
    //                     if (dvAndCols[0] && dvAndCols[0].dvColumnsList)
    //                         this.selectedSrcDataViewAndColumns = dvAndCols[0].dvColumnsList;
    //                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceColumnLOV = (this.selectedSrcDataViewAndColumns);
    //                 });
    //             }
    //         });
    //         /* this.sourceDataViewsAndColumns.forEach( srcObject => {
    //              if ( srcObject.dataViewDispName === sCol ) {
    //                  this.sourceColumnLOV = [];
    //                  this.sourceDataViewId = srcObject.id;
    //                  this.sourceColumnLOV = srcObject.dvColumnsList;
    //              }
 
    //          } );*/
    //         // this.filterSrcToTargetViews(sCol);
    //     }
    //     else {
    //     }
    // }
    // selecttargetColumns(tCol: any) {
    //     if (this.targetDataViewsAndColumns) {
    //         this.targetDataViewsAndColumns.forEach(dataViewObj => {
    //             if (dataViewObj.dataViewDispName === tCol) {
    //                 this.dataViewsService.getDataViewById(dataViewObj.id).subscribe((res: any) => {
    //                     let dvAndCols = res;
    //                     this.selectedTargetDataViewAndColumns = dvAndCols[0].dvColumnsList;
    //                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetColumnLOV = (this.selectedTargetDataViewAndColumns);
    //                 });
    //             }
    //         });
    //         /*  this.targetDataViewsAndColumns.forEach( targetObj => {
    //               if ( targetObj.dataViewDispName === tCol ) {
    //                   this.targetColumnLOV = [];
    //                   this.targetDataViewId = targetObj.id;
    //                   this.targetColumnLOV = targetObj.dvColumnsList;
    //               }
  
    //           } );*/
    //     }
    //     else {
    //     }
    // }
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
    SelectColumn(i, j, columnId,dType?:any) {
        let dataType= '';
        if(!dType || dType==null )  {
        ////console.log('  this.sourceColumnLOV[i]'+  this.sourceColumnLOV[i].length);
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV.length > 0) {
         this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV.forEach((col) => {
        if (col.id == columnId) {
            dataType = col.colDataType;
             }
             });
            }
        } else{
            dataType = dType;
        }
        
        
       
        if (dataType) {
            // this.lookUpCodeService.fetchLookUpsByLookUpType(dataType).subscribe((res: any) => {
            //     let operatorss = res;
            //     if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[j])
            //     this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[j].operatorBasedOnColumnLOV = operatorss;
            // });
            if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[j]) {
                if( this.varcharDecDateLookupsMap && this.varcharDecDateLookupsMap[dataType]){
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[j].operatorBasedOnColumnLOV =  this.varcharDecDateLookupsMap[dataType];
                }
                
            }
           
        }  else{
            
        }

    }
    selectedSourceColumn(i: any, childIndex: any, id: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sColumnId = id;
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV){
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV.forEach((srcColObj) => {
                if (srcColObj.id == id) {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].dataType = srcColObj.colDataType;
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sColumnName = srcColObj.columnName;
                    this.SelectColumn(i, childIndex, id);
                    if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].dataType == 'DECIMAL'){
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceType = 'Amount';
                    } else if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].dataType == 'DATETIME' || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].dataType == 'DATE'){
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceType ='DAY';
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sPercentile=null;
                    }
    
                }
    
            });
        }
       

    }
    selectedOperator() {

    }
    selectedTargetColumn(i: any, childIndex: any, id: any) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tColumnId = id;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetColumnLOV.forEach((targetColObj) => {
            if (targetColObj.id == id) {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tDataType = targetColObj.colDataType;
                if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tDataType == 'DECIMAL') {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceType = 'Amount';
                }   else if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tDataType == 'DATETIME' || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tDataType == 'DATE'){
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceType ='DAY';
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tPercentile=null;
                }
            }
        });
    }
    MatchRule(i) {
        ////console.log('i====>'+i);    
    }
    getExistingRuleDetailsAndCopy(ruleId: any,indexVal: any){
        console.log('index has'+indexVal+' id is '+ruleId);
        this.setRuleObject(indexVal,ruleId.id);
        this.ruleGroupService.ruleGroupRulesAndConditions.selectedExistingRule = ruleId;
       // $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
    }
    setRuleObject(indexVal: any, value: any) {
        ////console.log('setRuleObject - ruleName ' + ruleName);
        ////console.log('setRuleObject - value ' + JSON.stringify(value));
        //if (value && value.data && value.data.id)
        if (value )  {
            this.rulesService.fetchExistingRuleDetails(value).subscribe((res: any) => {
                const ruleObj: any = res;
                //  //console.log('ruleObj'+JSON.stringify(ruleObj));
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule = ruleObj.rule;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.id=null;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.formCntrl = new FormControl('');
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleGroupAssignId=null;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.editRule = true;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate = this.dateUtils.convertDateTimeFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate);
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate = this.dateUtils.convertDateTimeFromServer(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate);
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions = ruleObj.ruleConditions;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleCategory='existing';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.copiedRefId=[];
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.copiedRefId.push( this.ruleGroupService.ruleGroupRulesAndConditions.selectedExistingRule);
                const dv = {
                    "id":this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId,
                     "itemName": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName };
                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId = [];
                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDVId.push(dv);
                     this.fetchSrcColumns(dv,indexVal);
                     const targetDV = {
                        "id":this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId,
                         "itemName": this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName };
                         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId = [];
                         this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDVId.push(targetDV);
                     this.fetchTargetColumns(targetDV,indexVal);
                    if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.editRule)  {
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.id = null;
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.editRule=true;
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleCode=null;
                        console.log('emptied at 1111');
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleName=null;
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleGroupAssignId=null;
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.assignmentFlag = true;
                    }

                    for(let con =0; con<this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length;con++) {
                        this.SelectColumn(indexVal,con,this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].sColumnId,this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].dataType);
                        if(this.copyRule)  {
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].ruleId=null;
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].id=null;
                        }
                       // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOperator || this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length==1 )
                       // {
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOpLOVArray = [];
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOpLOVArray =  jQuery.extend(true, [], this.logicalOperatorLOV);
                            for(let lOperator = 0;lOperator< this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOpLOVArray.length;lOperator++)  {
                                if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOpLOVArray[lOperator].setValue ==this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOperator ){
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[con].logicalOpLOVArray[lOperator].value= true; 
                                }
                            }
                       // }
                    }
                     

            });
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].hideRule=false;
        }  else{
          //  if(value.data)
           // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleCode=value.data;
           // console.log('emptied at 1145'+value.data);
           // this.ruleGroupService.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleName=value.data;
           
        }
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
           // let rulesListObject : RuleGroupWithRulesAndConditions =  res;
            this.ruleGroupService.ruleGroupRulesAndConditions = res;
            for(let con =0; con<this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;con++) {
                 if(!this.ruleGroupService.ruleGroupRulesAndConditions.rules[con].rule.editRule)   {
                    this.ruleGroupService.ruleGroup.editRule= false;
                    break;
                } else{
                    this.ruleGroupService.ruleGroup.editRule= true;
                }
            }
            this.ruleGroupService.ruleGroupRulesAndConditions.dataViewstoColumnsMap= this.dataViewsToColumnsMap;
            ////console.log('after get R-rules'+JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions));
            if(this.isViewOnly)  {
                if (this.ruleGroupService.ruleGroupRulesAndConditions.rules.length > 0) {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].openRuleTab= true;
                }
            }
            this.setFormGroupsForExisting();
            this.ruleGroupTypes.forEach((ruleGRpLookUp) => {
                if (ruleGRpLookUp.lookUpCode == this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose) {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rulePurpose = ruleGRpLookUp.lookUpCode;
                }

            });
            if (!this.isCreateNew) {
                let i = 0;
                this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach((rule) => {

                    //set rule forms if edit mode 
                    //if(rule.rule.editRule)
                    //{
                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.formCntrl = new FormControl('');
                    //}
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruletypeLOVArray = jQuery.extend(true, [], this.ruletypeLOV);
                    if(!this.isViewOnly){
                        if(i==0){
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].openRuleTab=true;
                        }else{
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].openRuleTab=false;
                        }
                    }                 
                    rule.rule.today = rule.rule.startDate;
                    this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruletypeLOVArray.forEach((ruleType)=>{
                        if(ruleType.setValue == rule.rule.ruleType){
                        ruleType.value=true;
                        }
                       
                    });          
                    if(!this.isViewOnly)  {
                        const dv = {
                            "id":this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceDataViewId,
                             "itemName": this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sDataViewDisplayName };
                             this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceDVId = [];
                             this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceDVId.push(dv);
                             this.fetchSrcColumns(dv,i);
    
                             const targetDV = {
                                "id":this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetDataViewId,
                                 "itemName": this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.tDataViewDisplayName };
                                 this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetDVId = [];
                                 this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetDVId.push(targetDV);
                             this.fetchTargetColumns(targetDV,i);
                             
                    }     
                  
                         if(this.copyRule)     {
                            this.ruleGroupService.ruleGroup.editRule= true;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id = null;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.editRule=true;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode=null;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate = new Date();
                                //console.log('emptied at 1244');
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleName=null;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleGroupAssignId=null;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.assignmentFlag = true;
                            
                            }
                            if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions){
                            for(let con =0; con<this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length;con++) {
                                this.SelectColumn(i,con,this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].sColumnId,this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].dataType);
                                if(this.copyRule) {
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].ruleId=null;
                                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].id=null;
                                }
                                if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].dataType == 'DECIMAL') {
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].sToleranceType = 'Amount';
                                } else if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].dataType == 'DATETIME' || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].dataType == 'DATE'){
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].sToleranceType ='DAY';
                                }
                                if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].tDataType == 'DECIMAL')  {
                                      this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].tToleranceType = 'Amount';
                                }  else if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].tDataType == 'DATETIME' || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].tDataType == 'DATE') {
                                     this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].tToleranceType ='DAY';
                                }

                                
                               // if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].logicalOperator || this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length==1 )
                               // {
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].logicalOpLOVArray = jQuery.extend(true, [], this.logicalOperatorLOV);
                                    for(let lOperator = 0;lOperator< this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].logicalOpLOVArray.length;lOperator++) {
                                        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].logicalOpLOVArray[lOperator].setValue ==this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].logicalOperator ){
                                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[con].logicalOpLOVArray[lOperator].value= true; 
                                        }
                                    }
                                //}
                            }
                        }

                            i = i + 1;
                            
                });
               
                // if(this.isEdit)
                //{
                    
               // }
               if(!this.isViewOnly){
               this.fetchExistingRules();
               }
                 
            } else {
               // //console.log('it wont enter to create')
            }
        });
      
    }
    validationMsg() {

    }
    checkGroupName(name: string) {
        this.ruleGroupService.query().subscribe(
            (res: Response) => {
                this.ruleGroupsList = res.json();
                let count= 0;
                ////////console.log(' this.ruleGroupsList'+JSON.stringify( this.ruleGroupsList));
                this.ruleGroupsList.forEach((ruleGroup) => {
                    if (ruleGroup.name && name && (ruleGroup.name.toLowerCase() == name.toLowerCase())) {
                        count = count + 1;

                    }  else {

                    }
                });
                if (count > 0) {
                    this.duplicateRuleGroupName = true;
                } else {
                    this.duplicateRuleGroupName = false;
                }
            }
        );

    }
    deleteCondition(i, childIndex) {
        let ruleCondLength =0;
        ruleCondLength =  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length;

        if (ruleCondLength > 0 ) {
               if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].amountQualifier){
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].hasAmountQualifier=false;
               }
              
                if(childIndex == (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length-1) ){
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex-1].logicalOpLOVArray  = [];
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex-1].logicalOpLOVArray = jQuery.extend(true, [], this.logicalOperatorLOV);
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex-1].logicalOpLOVArray.forEach((op)=>{
                             op.value=false;
                         });
                }
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(childIndex, 1);
                if( this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length==0) {
                    this.addNewCondition(i, 0);
                } else{
                   
                   
                }
              //  if (childIndex > 0 && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2])
               // {
                    //this.logicalOpLOVArray[i][ruleCondLength - 2]=[];    
                    //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2].logicalOpLOVArray  = [];         
                    //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2].logicalOpLOVArray = jQuery.extend(true, [], this.logicalOperatorLOV);
                   // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2].logicalOpLOVArray.forEach(op=>{
                   //     op.value=false;
                   // });
                   // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[ruleCondLength - 2].logicalOperator=null;
                   // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(childIndex, 1);
               // }
               // else 
               // {
                //    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(childIndex, 1);
                //        this.addNewCondition(i, childIndex);
              //  }
              // 
             
            }
            
         

    }
    deletRule(i) {
        this.ruleGroupService.ruleGroupRulesAndConditions.rules.splice(i, 1);
    }
    displayOptions() {
        this.showOptions = true;
    }
    filterDataView() {
        //////console.log('filtering dv');
        
        
    }
    openTargetFilters(i,childIndex) {
        let tTolFrom: any ;
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom == '-'){
            tTolFrom = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom+
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom;
        } else{
            tTolFrom = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom;

        }
      
        let tTolTo:any ;
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo == '-'){
            tTolTo = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo+
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo;
        }else{
            tTolTo = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo;

        }
       
        const data ={
                        addFilter:'target',
                        tOperatorList: this.operatorList,
                        tOperator : this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tOperator,
                        tValue :  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tValue,
                        tDataType : this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tDataType,

                        excelFunctions: this.excelFunctions,
                        tFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula,
                        tFormulaExpressionExample:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormulaExpressionExample,
                        isTFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isTFormula,
            
                        tToleranceType:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceType,
                        // ttoleranceTypeLOV:this.ttoleranceTypeLOV,
                        // tToleranceTypeMeaning:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceTypeMeaning,
                        tToleranceValueFrom:tTolFrom,
                        tToleranceOperatorFrom:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom,
                        tToleranceOperatorTo:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo,
                        tToleranceValueTo:tTolTo,
            
                        tPercentile:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tPercentile,
                        tToOperands  : this.tToOperands,
                        tFromOperands : this.tFromOperands,
                        ok:'ok',
                        cancel:'cancel',
                        disableClose: true
                    };

                    const dialogRef = this.dialog.open(ReconConfirmActionModalDialog, {
                        data: data,
                        disableClose: true
                    });
            
            
                    dialogRef.afterClosed().subscribe((result) => {
                       // //console.log('target filter result' + JSON.stringify(result));
                        if (result && result == 'ok') {
                           // //console.log('data'+JSON.stringify(data));
                          
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tOperator = data.tOperator;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tValue = data.tValue;
            
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula= data.tFormula;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormulaExpressionExample= data.tFormulaExpressionExample;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isTFormula= data.isTFormula;
                          this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tPercentile=data.tPercentile;
                          let tToleranceValueFrom: any ;
                          tToleranceValueFrom = data.tToleranceValueFrom.toString();
                         
                          if(tToleranceValueFrom.startsWith('-')) {
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '-';
                             const output = tToleranceValueFrom.split("-");
                              if(output && output[1]){
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = output[1];
                          }
                            } else {
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '+';
                              if( tToleranceValueFrom.startsWith('+'))  {
                                  const output = tToleranceValueFrom.split("+");
                                  if(output && output[1]){
                                  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = output[1];
                                  }
                              } else{
                                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = tToleranceValueFrom;
                              }
                          }
          
                          let tToleranceValueTo: any ;
                          tToleranceValueTo = data.tToleranceValueTo.toString();
                         
                          if(tToleranceValueTo.startsWith('-'))   {
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo= '-';
                             const output = tToleranceValueTo.split("-");
                              if(output && output[1]){
                           
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = output[1];
                              }
                          }     else  {
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '+';
                              if( tToleranceValueTo.startsWith('+')) {
                                  const output = tToleranceValueTo.split("+");
                                  if(output && output[1]){
                                  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = output[1];
                                  }
                              }  else{
                            
                              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = tToleranceValueTo;
                              }
                          
                          } 

                         // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = data.tToleranceOperatorFrom;
                          //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = data.tToleranceValueFrom;
                         // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = data.tToleranceOperatorTo;
                         // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo= data.tToleranceValueTo;
            
                        //  this.toOperands = data.toOperands ;
                         // this.fromOperands = data.fromOperands ;
                        }  else if (result && result == 'cancel') {
                          
                        }
                    });
    }
    openSrcFilters(i,childIndex) {
        let sTolFrom: any ;
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom == '-'){
            sTolFrom = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom+
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom;
        }  else{
        sTolFrom = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom;
        }
        let sTolTo: any ;
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo == '-'){
        sTolTo = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo+
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo;
        }else{
        sTolTo = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo;
        }
        const data ={  
            addFilter:'source',
            sOperatorList: this.operatorList,
            sOperator : this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sOperator,
            sValue :  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sValue,

            sDataType : this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].dataType,
            excelFunctions: this.excelFunctions,
            sFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula,
            sFormulaExpressionExample:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample,
            isSFormula: this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isSFormula,

             sToleranceType:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceType,
            // stoleranceTypeLOV:this.stoleranceTypeLOV,
            // sToleranceTypeMeaning:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceTypeMeaning,
            sToleranceValueFrom:sTolFrom,
            sToleranceOperatorFrom:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom,
            sToleranceOperatorTo:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo,
            sToleranceValueTo:sTolTo,
            sPercentile:this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sPercentile,
            toOperands : this.toOperands,
            fromOperands : this.fromOperands,


            ok:'ok',
            cancel:'cancel',
            disableClose: true
        };
        const dialogRef = this.dialog.open(ReconConfirmActionModalDialog, {
            data: data,
            disableClose: true
        });


        dialogRef.afterClosed().subscribe((result) => {
          //  //console.log('filter result' + JSON.stringify(result));
            if (result && result == 'ok') {
              //  //console.log('data'+JSON.stringify(data));
              
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sOperator = data.sOperator;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sValue = data.sValue;

              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula= data.sFormula;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample= data.sFormulaExpressionExample;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].isSFormula= data.isSFormula;
              this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sPercentile=data.sPercentile;
             
              

              let sToleranceValueFrom: any ;
                sToleranceValueFrom = data.sToleranceValueFrom.toString();
               
                if(sToleranceValueFrom.startsWith('-'))  {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '-';
                    const output = sToleranceValueFrom.split("-");
                   if(output && output[1]){
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom =output[1];
                   }
                   
                  
                }else {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '+';
                     if( sToleranceValueFrom.startsWith('+')) {
                         const output = sToleranceValueFrom.split("+");
                         if(output && output[1]){
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = output[1];
                         }
                         
                     } else{
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom =sToleranceValueFrom;
                     }
                   
                }

                let sToleranceValueTo: any ;
                sToleranceValueTo = data.sToleranceValueTo.toString();
               
                if(sToleranceValueTo.startsWith('-')) {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo= '-';
                     const output = sToleranceValueTo.split("-");
                     if(output && output[1]){
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo =  output[1];

                     }
                   
                } else {
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '+';
                    if( sToleranceValueTo.startsWith('+'))  {
                        const output = sToleranceValueTo.split("+");
                        if(output && output[1]){
                            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = output[1];
                        }
                        
                    }else{
                        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = sToleranceValueTo;
                    }
                  
                   
                } 
              
             // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = data.sToleranceOperatorTo;
            //  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo= data.sToleranceValueTo;

            //  this.toOperands = data.toOperands ;
             // this.fromOperands = data.fromOperands ;
            }  else if (result && result == 'cancel') {
              
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
        } else {

        }
        this.sFormula[i][childIndex] = '';
        this.sFormula[i][childIndex] = true;

        if (this.sFormula[i][childIndex]){
            this.addDisplayPopOverContentClass();
        }
           

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
        }else {

        }
        this.sTolerance[i][childIndex] = '';
        this.sTolerance[i][childIndex] = true;

        if (this.sTolerance[i][childIndex]){
            this.addDisplayPopOverContentClass();
        }
            

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

        } else {
            this.tFilter[i] = [];
        }

        this.tFilter[i][childIndex] = '';
        this.tFilter[i][childIndex] = true;

        if (this.tFilter[i][childIndex]){
            this.addDisplayPopOverContentClass();
        }

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

        } else {
            this.tFormula[i] = [];
        }
        this.tFormula[i][childIndex] = '';
        this.tFormula[i][childIndex] = true;

        if (this.tFormula[i][childIndex]){
            this.addDisplayPopOverContentClass();
        }
           

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

        } else {
            this.tTolerance[i] = [];
        }
        this.tTolerance[i][childIndex] = '';
        this.tTolerance[i][childIndex] = true;

        if (this.tTolerance[i][childIndex]){
            this.addDisplayPopOverContentClass();
        }
           

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

        let alreadySelected=0;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOpLOVArray .forEach((op) => {
            if(op.value == true){
                alreadySelected = 1;
            }
          
        });
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOpLOVArray.forEach((op) => {

            if (op.setValue == logOperatorObj.setValue){
                //ruleType.value = ruleType.value ? false : true;
                if(op.value == true) {

                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOperator = logOperatorObj.setValue;
                }   else{
                        op.value=true;
                    
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].logicalOperator = logOperatorObj.setValue;
                if(!this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex+1]){
                    this.addNewCondition(i, childIndex + 1);
                }
              
                }
            } else {
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
        //////console.log('val  :' + val );
        ////console.log('ind :' + childIndex);
        ////console.log('examp :' + examp);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample = examp;
    }
    checkTargetAndRefresh() {
    }
    savesrcExpression(val, i, childIndex) {
        ////console.log(val);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula = val;
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sformulaSave = true;
        this.sFormula[i][childIndex] = false;
        this.removeAddedClasses();
    }
    savetargetExpression(val, i, childIndex) {
        ////console.log(val);
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
        ////console.log(' this.sFilter[i][childIndex]'+ this.sFilter[i][childIndex]);
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
        if (this.sFilter[i] && this.sFilter[i][childIndex]){
            this.sFilter[i][childIndex] = false;
        }
            
        if (this.sFormula[i] && this.sFormula[i][childIndex]){
            this.sFormula[i][childIndex] = false;
        }
            
        if (this.sTolerance[i] && this.sTolerance[i][childIndex]){
            this.sTolerance[i][childIndex] = false;
        }
            
        //this.removeAddedClasses();
    }
    closeTarget(i, childIndex) {
        if (this.tFilter[i] && this.tFilter[i][childIndex]){
            this.tFilter[i][childIndex] = false;
        }
            
        if (this.tFormula[i] && this.tFormula[i][childIndex]){
            this.tFormula[i][childIndex] = false;
        }
            
        if (this.tTolerance[i] && this.tTolerance[i][childIndex]){
            this.tTolerance[i][childIndex] = false;
        }
            
        this.removeAddedClasses();
    }
    deleteTargetFilter() {

    }
    clearRuleObject(i) {
       // this.addNewRuleObject(i,0);
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i]) {
            const ruleObj = new RulesAndConditions();
            const rule = new Rules();
            ruleObj.rule = rule;
            const ruleConditions = new RuleConditions();
            ruleObj.ruleConditions = [];
            ruleObj.openRuleTab=true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] = ruleObj;
            this.setDataViewsArrays(i);
            this.addNewCondition(i, 0);
        }
    }
    changeTargetDataView(val, i) {
        ////console.log('$event target dv'+val);
        if (val == "") {
            ////console.log('val is empty clear the target dv object');
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions) {

                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach((ruleCondition) => {
                    ruleCondition.tColumnId = 0;
                });
            }

        }   else if (val != "") {
            ////console.log('target dv val is not empty ');
        }

    }
    changeSourceDataView(val, i) {
        ////console.log('$event src dv'+val);
        if (val == "") {
            ////console.log('val is empty clear the src dv object');
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions) {

                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach((ruleCondition) => {
                    ruleCondition.sColumnId = 0;
                });
            }

        }  else if (val != "") {
            ////console.log('src dv val is not empty ');
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
       // console.log('delete function called');
        if(this.ruleGroupService.ruleGroupRulesAndConditions.rules.length ==1)  {
            this.clearRuleObject(i);
           // this.openRuleTab=[];
           //this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].openRuleTab=true;
        }  else{
          //  this.ruletypeLOVArray.splice(i,1);
            this.ruleListformArray.splice(i,1);
            //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.sourceColumnLOV.splice(i,1);
           // this.operatorBasedOnColumnLOV.splice(i,1);
            //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.targetColumnLOV.splice(i,1);
            this.sFilter.splice(i,1);
            this.sFormula.splice(i,1);
            this.sTolerance.splice(i,1);
            this.tFilter.splice(i,1);
            this.tFormula.splice(i,1);
            this.tTolerance.splice(i,1);
           // this.logicalOpLOVArray.splice(i,1);
           this.ruleGroupService.ruleGroupRulesAndConditions.rules.splice(i, 1);
        }

       // if (this.ruleGroupService.ruleGroupRulesAndConditions.rules.length == 0)
            //this.addNewRuleObject(0, 0);
    }
    changeTemplate() {
        ////console.log('change in template triggered');
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
            const ruleListChanges = this.ruleGroupDiffer.diff(this.ruleGroupService.ruleGroupRulesAndConditions.rules);
            if (ruleListChanges) {
                //////console.log('==>1');
                //this detection is only when object is added / removed to array
                //////console.log('changes detected for list of rules');
                //////console.log('previous were detected for rule'+JSON.stringify(this.ruleGroupService.ruleGroupRulesAndConditions.rules));
                this.checkForRules();
            }  else {
                // ////console.log('==>2');
                //////console.log('no addition / removal');
                this.checkForRules();
            }
        }

    }
    checkForRules() {
        let i = 0;
        //  ////console.log('this.ruleGroupService.ruleGroupRulesAndConditions.rules length'+this.ruleGroupService.ruleGroupRulesAndConditions.rules.length);
        this.ruleGroupService.ruleGroupRulesAndConditions.rules.forEach((rule) => {
            // ////console.log('check if rule is changed');
            const changesForRule = this.ruleObjDiffer.diff(rule.rule);
            const changesForRuleConditions = this.ruleConditionsDiffer.diff(rule.ruleConditions);

            if (changesForRule) {
                // ////console.log('==>3');
                // ////console.log('changes detected for rule'+JSON.stringify(rule));
                //
                changesForRule.forEachChangedItem((r) => {
                    // ////console.log('changed rule is ', JSON.stringify(r.currentValue));
                    // ////console.log('previous val is ', JSON.stringify(r.previousValue))
                    // this.validateRuleObject(r.currentValue,r.previousValue);
                }

                );
                changesForRule.forEachAddedItem((r) => {
                    //////console.log('added rule' + JSON.stringify(r.currentValue)
                }
                );
                changesForRule.forEachRemovedItem((r) => {
                    // ////console.log('removed ' + JSON.stringify(r.currentValue)
                }
                );
            } else {
                // ////console.log('==>4');
                // ////console.log('nothing changed for'+rule.rule.id);
            }

            if (changesForRuleConditions) {
                /* rule.ruleConditions.forEach(ruleCondition=>{
                        var changesForCondition = this.ruleConditionObjDiffer.diff(ruleCondition);
                        if(changesForCondition)
                            {
                                this.checkForConditions(i);
                            }
                    })*/
                //  ////console.log('==>5');
                this.checkForConditions(i);
            } else {
                //////console.log('no change in rule conditions array);
                // ////console.log('==>6');
                this.checkForConditions(i);
            }
            i = i + 1;
        });
    }
    checkForConditions(i) {
        const j = 0;
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions){
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach((ruleCondition) => {
                const changesForCondition = this.ruleConditionObjDiffer.diff(ruleCondition);
                if (changesForCondition) {
                    //////console.log('changes in conditions'+JSON.stringify(ruleCondition));
                    //
                    //////console.log('==>7');
                    changesForCondition.forEachChangedItem((r) => {
                        //////console.log('changed condition is ', JSON.stringify(r.currentValue))
                        // ////console.log('previous val is ', JSON.stringify(r.previousValue))
                        // this.validateRuleObject(r.currentValue,r.previousValue);
                    }
                    );
                    changesForCondition.forEachAddedItem((r) => {
                        //////console.log('Condition  added ' + JSON.stringify(r.currentValue)

                    }
                    );
                    changesForCondition.forEachRemovedItem((r) => {
                        //////console.log('Condition removed ' + JSON.stringify(r.currentValue);
                    });
                } else {
                    // ////console.log('==>8');
                }
            });
        }

    }
    saveAdhocRule() {
        let groupId = [];
        groupId = this.accountingMode.toString().split("-");
        this.ruleGroupService.ruleGroupRulesAndConditions.adhocRuleCreation = true;
        this.ruleGroupService.ruleGroupRulesAndConditions.id = groupId[0];
        this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.priority=null;
       // this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.ruleType = 'ADHOC';
        this.ruleGroupService.getGroupDetails(groupId[0]).subscribe((res: any) => {

            let ruleGroupById = new RuleGroupWithRulesAndConditions();
            ruleGroupById = res;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.assignmentFlag = true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.enabledFlag = true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.sourceDataViewId = this.ruleGroupService.sourceDataViewId;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[0].rule.targetDataViewId = this.ruleGroupService.targetDataViewId;
            ruleGroupById.rules.push(this.ruleGroupService.ruleGroupRulesAndConditions.rules[0]);
            ruleGroupById.adhocRuleCreation = true;
            this.ruleGroupService.postAdhocruleForReconcile(ruleGroupById).subscribe((res1: any) => {
                let savedObj = [];
                savedObj = res1;
                if(savedObj &&  savedObj.length>0 && savedObj[savedObj.length-1] && savedObj[savedObj.length-1].taskStatus && savedObj[savedObj.length-1].taskName ) {
                    if( savedObj[savedObj.length-1].taskStatus.search('Failed')!= -1){
                        this.commonService.error(
                            savedObj[savedObj.length-1].taskName,
                             savedObj[savedObj.length-1].taskStatus 
                        )
                    } else {
                        this.commonService.success(
                            savedObj[savedObj.length-1].taskName,
                             savedObj[savedObj.length-1].taskStatus 
                        )
                       
                    }
                    this.reconcileService.ENABLE_RULE_BLOCK = false;
                }
                
            });

        });

    }
    /*validateRuleObject(currentRule,previousRule)
    {
        ////console.log('currentRule'+JSON.stringify(currentRule));
        ////console.log('previousRule'+JSON.stringify(previousRule));
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
                   
                   this.commonService.success(
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
            this.commonService.success(
                '',
                this.ruleGroupService.ruleGroupRulesAndConditions.rules.length + msg
            )
        }
       
    }*/
    splitOperatorAndValues() {
        for (let i = 0; i < this.ruleGroupService.ruleGroupRulesAndConditions.rules.length; i++) {
            for (let j = 0; j < this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions.length; j++) {
                if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.id) {
                }  else {
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
            const valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom;
            let values: string[];


            ////console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = values[1];
            }  else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom = values[1];
            } else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorFrom = '+';
                // this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom= values[1]; 
            }
        }

    }

    splitsToleranceTo(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo) {
            const valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo;
            let values: string[];

            ////console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = values[1];
            } else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueTo = values[1];
            } else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceOperatorTo = '+';
            }
        }

    }
    splittToleranceFrom(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom) {
            const valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom;
            let values: string[];


            // //console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = values[1];
            }  else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom = values[1];
            }  else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorFrom = '+';
            }
        }

    }
    splittToleranceTo(i, childIndex) {
        if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo) {
            const valuenOperator: string = this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo;
            let values: string[];

            ////console.log('valuenOperator'+valuenOperator)
            if (valuenOperator.startsWith('+')) {
                values = valuenOperator.split("+");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '+';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = values[1];
            }  else if (valuenOperator.startsWith('-')) {
                values = valuenOperator.split("-");
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '-';
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueTo = values[1];
            }  else {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceOperatorTo = '+';
            }


        }

    }


    selectValue() {
    }
    checkIfRuleCodeisUndefined(i) {
        if (this.isCreateNew && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode == undefined) {
            console.log('rule code is undefined');
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode = '';
            console.log('emptied at 2277');
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

        const alreadySelected=0;
        // this.ruletypeLOVArray[i].forEach(rtype => {
        //     if(rtype.value == true)
        //     alreadySelected = 1;
        // });
        for(let n =0;n<this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruletypeLOVArray.length;n++)  {
            if (this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruletypeLOVArray[n].setValue == ruleType.setValue)  {
                //ruleType.value = ruleType.value ? false : true;
                if(ruleType.value == true)  {


                }  else{
                        ruleType.value=true;
                    
                }
            } else {
                
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruletypeLOVArray[n].value = false;
            }
        }
        // this.ruletypeLOVArray[i].forEach(rtype => {

        //     if (rtype.setValue == ruleType.setValue)
        //     {
        //         //ruleType.value = ruleType.value ? false : true;
        //         if(ruleType.value == true)
        //         {


        //         }
        //         else{
        //                 ruleType.value=true;
                    
        //         }
        //     }
                
        //     else
        //     {
                
        //         rtype.value = false;
        //     }
                
        // });
    }
    checkIfBlurCalled(i,inputRuleName){
        //console.log('blur called with '+i+' inputRuleName '+inputRuleName);
        //check for duplicates
        //get all rules list (based on rule purpose). other than the current id's name
    }
    restrictTab(code) {
        if(code == 9){
            return false;
      
        }  else{

        }
        return true;
    }
    startDateChanged(dt:Date,i){
        if(  this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate){
            if(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate<this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate){
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.endDate=this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.startDate;
            }
        }
    } 

    changeRuleCategory(e,i)  {
        console.log('event '+' at '+i+' now has '+e);
        if( this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCategory == 'new')   {
            this.addNewRuleObject(i,0);
        }  else  {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].hideRule=true;
        }
    }
    checkduplicate(i){
        for(let c =0;c<this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;c++)  {
            if(c != i)  {
                if((this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].rule.ruleCode && this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode ) &&
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].rule.ruleCode.toLowerCase() === this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode.toLowerCase() ){
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.duplicateRuleName = true;
                }else{
                    this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.duplicateRuleName = false;
                }

            }
        }
    }
    ruleDuplicationCheck(i){
        this.ruleGroupService.ruleDuplicationCheck(this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode,this.ruleGroupService.ruleGroup.rulePurpose).subscribe((res: any) => {
            const resp = res;
            if(resp  && (resp._body == true || resp._body == 'true')) {
               this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.duplicateRuleName = true;
               //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleCode= null;
               //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.ruleName= null;
            }   else  {
                this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].rule.duplicateRuleName = false;
            }

            this.checkduplicate(i);
        });
    }
    checkQualifier(event, i , childIndex) {
        
        if(event.checked == true)  {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i]. hasAmountQualifier = true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sReconAmountMatch=true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tReconAmountMatch=true;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].amountQualifier=true;
        }  else  {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i]. hasAmountQualifier = false
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sReconAmountMatch=false;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tReconAmountMatch=false;
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].amountQualifier=false;
        }
    }
    selectCloseBracket(i,j) {
        //this.ruleGroupService.ruleGroupRulesAndConditions.rules[i].ruleConditions[j].openBracket=
    }

    selectOpenBracket(i,j) {
        
    }
 
    expandAll(){
        for(let c =0;c<this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;c++)  {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].openRuleTab=true;
        }
    }
    collapseAll(){
        for(let c =0;c<this.ruleGroupService.ruleGroupRulesAndConditions.rules.length;c++)  {
            this.ruleGroupService.ruleGroupRulesAndConditions.rules[c].openRuleTab=false;
        }
    }
    // ngOnDestroy() {
    //     this.notificationsService.remove();
    // }
}
