import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import {FormControl} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { Router, NavigationEnd } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { RuleGroup } from './rule-group.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {CommonService} from '../common.service';
import {Rules} from '../rules/rules.model';
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
import { RuleGroupAndRuleWithLineItem } from './ruleGroupAndRuleWithLineItem.model';
import { IterableDiffers , KeyValueDiffers} from '@angular/core';

declare var $:any;
const URL='';
@Component({
    selector: 'jhi-rule-group-detail',
    templateUrl: './rule-group-detail.component.html'
})
export class RuleGroupDetailComponent implements OnInit {

    @Input() accountingMode : string ='';
    ruleGrpWithRuleAndLineItems = new  RuleGroupAndRuleWithLineItem() ;
    myData:any='';
    filterAction:any='';
    duplicateRuleGroupName : boolean =false;
    ruleShobha : Rules=new Rules();
    mySource=["abc","bcd","def"];
    ruleListformArray: FormGroup[]=[];
    targetDataViewformsArray : FormGroup[]=[];
    srcDataViewformsArray : FormGroup[]=[];
    
    srcColumnListformsArray : FormGroup [] = [];
    targetColumnListformsArray : FormGroup [] = [];
    
    selectedSrcDataViewAndColumns  = [];
    selectedTargetDataViewAndColumns  = [];
    
    
    logicalOperatorLOV = [];
    
    expandTab :boolean[] =[];
    showOptions : boolean =false;
    mouseOverRowNo: number = -1;
        parents =[
                  {
                      children :[
                                 "child1"
                                 ] 
                  },
                  {
                      children :[
                                 "child2"
                                 ] 
                  },
                  {
                      children :[
                                 "child3"
                                 ] 
                  },
                  {
                      children :[
                                 "child4"
                                 ] 
                  },
                  ];
        
        isCreateNew : boolean = false;
        isEdit      : boolean = false;
        isViewOnly  : boolean = false;
        
        create : string ='create';
        edit : string ='edit';
        view : string ='view';
        
        ruleListArrays : any[] =[];
        filteredRuleList : any[]=[];
        
        sourceDataViewsArrays : any [] = [];
        targetDataViewsArrays : any [] = [];
        
        sourceColumnArrays : any [] = [];
        targetColumnArrays : any [] = [];
        
        ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
        ruleGroupsList : RuleGroup[] =[] ;
        RuleWithConditions : RulesAndConditions[];
        sourceDataViewId : number ;
        targetDataViewId : number;
        ruleGroup   = new RuleGroup();
        rules       = new Rules() ;
        ruleConditions : RuleConditions[];
        ruleConditionList : RuleConditions[]=[];
        ruleNameValidation = '';
        rulesList : Rules[]=[];
        ruleGroupTypes = [];
        operatorLOV : any = [];
        ruletypeLOV : any =[];
        open : boolean = true;
        routeSub: any;
        formControlArray : FormControl[]=[];
        optionsArray : any[][]=[[],[]];
        myControl = new FormControl();
        sourceDataViewControl = new FormControl();
        targetDataViewControl = new FormControl();
        sourceColumnLOVControl = new FormControl();
        targetColumnLOVControl = new FormControl();
        operatorLOVControl = new FormControl();
        
        availablRuleTypes : any =[{
            "lookUpId" : '111',
            "ruleType" : 'ruleType1'
        },
        {
            "lookUpId" : '112',
            "ruleType" : 'ruleType2'
        },
        {
            "lookUpId" : '113',
            "ruleType" : 'ruleType3'
        },
        {
            "lookUpId" : '114',
            "ruleType" : 'ruleType4'
        },];
        
        options:any = [];
       
        sourceDataViewsAndColumns = [];
        targetDataViewsAndColumns =  [];
        sourceColumnLOV =[] ;
        targetColumnLOV =[] ;
        
        filteredOptions :  Observable<any[]>[];
        
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
        //
        FilterTitle :  string ='Filters';
        FormulaTitle : string = 'Formulae';
        ToleranceTitle : string ='Tolerance ';
        additionalFilters :  string ='  Additional filters '; 
        sFilterTitle =[];
        sFilter =[];
        sFormulaTitle=[];
        sFormula =[];
        sToleranceTitle =[];
        sTolerance =[];
        sAdditionalFiltersTitle =[];
        
        tFilterTitle =[];
        tFilter =[];
        tFormulaTitle=[];
        tFormula =[];
        tToleranceTitle =[];
        tTolerance =[];
        tAdditionalFiltersTitle =[];
        ruleGroupDiffer: any;
        ruleObjDiffer:any;
        ruleConditionsDiffer : any;
        ruleConditionObjDiffer:any;
        constructor(
                private builder: FormBuilder,
                private config: NgbDatepickerConfig,
                private router: Router,
                private route: ActivatedRoute,
                private dateUtils: JhiDateUtils,
                private commonService:CommonService,
                private lookUpCodeService:LookUpCodeService,
                private dataViewsService:DataViewsService,
                private rulesService : RulesService,
                private ruleGroupService : RuleGroupService,
                private _sanitizer: DomSanitizer,
                private notificationsService: NotificationsService,
                private differs: IterableDiffers,
                private objDiffers:  KeyValueDiffers
                
            ) {
            this.config.minDate = { year: 1950, month: 1, day: 1 };
            this.config.maxDate = { year: 2099, month: 12, day: 31 };
            this.ruleGroupDiffer = differs.find([]).create(null);
            this.ruleObjDiffer = objDiffers.find({}).create();
            this.ruleConditionsDiffer = differs.find([]).create(null);
            this.ruleConditionObjDiffer = objDiffers.find({}).create();
            }
        selectedRule(value:any, indexVal:any)
        {
           // this.setRuleObject(value,indexVal);
        }
        changeMinDate() {
            this.config.minDate = this.ruleGroup.startDate;
          }

          resetMinDate() {
            this.config.minDate = { year: 1950, month: 1, day: 1 };
          }
          
          addNewCondition(indexVal : any,childIndex : any)
          {
              //console.log('childIndex'+childIndex);
              let ruleObj1 :RuleConditions = new  RuleConditions();
              this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex]={};
              this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions[childIndex]=ruleObj1;
              //addFilter titles
              this.sAdditionalFiltersTitle[childIndex] = this.additionalFilters;
              this.tAdditionalFiltersTitle[childIndex] = this.additionalFilters;
              
          }
          addNewRuleObject(indexVal:any,childIndex:any)
          {
            for(var i = 0;i<=indexVal;i++)
            {
                    this.expandTab[i]=false;
            
            }
            this.expandTab[indexVal]=true;
              let ruleConditions = new RuleConditions();
              let  rule = new Rules();
              
              let rulesConditionsListObj : RuleConditions[]=[];
              rulesConditionsListObj[0]=(ruleConditions);
              
              let rulesWithConditionsObj = new RulesAndConditions();
              
              rulesWithConditionsObj.rule =rule;
              rulesWithConditionsObj.ruleConditions = rulesConditionsListObj;
              if(indexVal == 0)
              {
                  this.ruleGroupRulesAndConditions['rules']=[];
                  this.ruleGroupRulesAndConditions['rules'][0]=( rulesWithConditionsObj);
              }
              else
                  {
                      this.ruleGroupRulesAndConditions.rules [indexVal]=(rulesWithConditionsObj);
                  }
             
              this.addnewLists(indexVal,0);
              //rule list array based on ruleGrouptype
              this.fetchRulesAndDataViewsListsBasedOnRuleGroupType(indexVal);
              this.setDataViewsArrays(indexVal);
          }
          setDataViewsArrays(indexVal:any)
          {
              this.sourceDataViewsArrays[indexVal] = this.sourceDataViewsAndColumns;
              this.targetDataViewsArrays[indexVal] = this.targetDataViewsAndColumns;
          }
          fetchRulesAndDataViewsListsBasedOnRuleGroupType(indexVal:any)
          {
              this.fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal);
              //fetch rule list number of data views src and target
              
              if(this.ruleListArrays[indexVal])
              this.ruleListArrays[indexVal].forEach(rule=>{
                  //console.log('for rule is'+JSON.stringify(rule));
                          this.ruleGroupRulesAndConditions.rules[indexVal].rule= rule;
                        
                  });
          }
          //set form group to all
          setFormGroupsForExisting()
          {
           for(var i=0;i<this.ruleGroupRulesAndConditions.rules.length;i++)
           {
              let ruleListForm: FormGroup;
              ruleListForm = this.builder.group({
              data : "",
              });
              this.ruleListformArray[i] = ruleListForm;
              
              let myForm: FormGroup;
              myForm = this.builder.group({
              data : "",
              });
              this.targetDataViewformsArray[i]=(myForm);
              
              let myForm1: FormGroup;
              myForm1 = this.builder.group({
              data : "",
              });
              this.srcDataViewformsArray[i] = (myForm1);
              
              let srcColListForm: FormGroup;
              srcColListForm = this.builder.group({
              data : "",
              });
              this.srcColumnListformsArray[i] = srcColListForm;
              
              let targetColListForm: FormGroup;
              targetColListForm = this.builder.group({
              data : "",
              });
              this.targetColumnListformsArray[i] = targetColListForm;
              
              this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
              this.sourceDataViewsArrays[i] = this.sourceDataViewsAndColumns;
         // //console.log(' does this have values'+JSON.stringify( this.sourceDataViewsArrays[i]));
               this.setDataViewsArrays(i);
              this.targetDataViewsArrays[i] = this.targetDataViewsAndColumns;3
               let srcviews = [];
               srcviews = this.sourceDataViewsArrays[i];
               srcviews.forEach(dataView => {
               //console.log('if(this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId'+this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId);
              //console.log('dataView.id'+dataView.id);
               if(+this.ruleGroupRulesAndConditions.rules[i].rule.sourceDataViewId == +dataView.id)
                   {
                   //console.log('yes matched');
                       this. sourceColumnLOV[i] = dataView.dataViewsColumnsList;
                   }
               });
               //console.log(' this. sourceColumnLOV[0] '+JSON.stringify( this. sourceColumnLOV[indexVal] ));
               let targetViews = [];
                   targetViews = this.targetDataViewsArrays[i];
                   targetViews.forEach(dataView =>{
                       if(+this.ruleGroupRulesAndConditions.rules[i].rule.targetDataViewId == +dataView.id)
                       {
                   //console.log('yes target matched');
                   this. targetColumnLOV[i] = dataView.dataViewsColumnsList;
                       }
                   });
           
            }
              
              
          }
           ngOnInit() {
               console.log('ngoninit of detail with reconcile'+this.accountingMode);
              /* this.objDiffer = {};
               this.ruleGroupRulesAndConditions.rules.forEach((elt) => {
                 this.objDiffer[elt] = this.differs.find(elt).create(null);
               });*/
               $(".split-example").css({
                   'height': 'auto',
                   'min-height': (this.commonService.screensize().height - 160) +'px'
                 });
               
               this.lookUpCodeService.fetchLookUpsByLookUpType('LOGICAL_OPERATOR').subscribe((res:any)=>{
                   this.logicalOperatorLOV=res; 
               });
           
               this.dataViewsService.operators('FUNCTIONS').subscribe((res:any)=>{
                   this.excelFunctions = res;
               });
               
           //operator list for filters
               this.lookUpCodeService.fetchLookUpsByLookUpType('VARCHAR').subscribe((res:any)=>{
                   this.operatorList = res; 
               });
               // 
           //create new
           let ruleListForm: FormGroup;
           ruleListForm = this.builder.group({
           data : "",
           });
           this.ruleListformArray[0] = ruleListForm;
           let myForm: FormGroup;
           myForm = this.builder.group({
           data : "",
           });
           this.targetDataViewformsArray[0]=(myForm);
           
           let myForm1: FormGroup;
           myForm1 = this.builder.group({
           data : "",
           });
           this.srcDataViewformsArray[0] = (myForm1);
           
           let srcColListForm: FormGroup;
           srcColListForm = this.builder.group({
           data : "",
           });
           this.srcColumnListformsArray[0] = srcColListForm;
           
           let targetColListForm: FormGroup;
           targetColListForm = this.builder.group({
           data : "",
           });
           this.targetColumnListformsArray[0] = targetColListForm;
           
           this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_TYPE').subscribe((res:any)=>{
               this.ruletypeLOV = res; 
           });
           this.lookUpCodeService.fetchLookUpsByLookUpType('OPERATOR').subscribe((res:any)=>{
               this.operatorLOV = res; 
           });
           
           this.lookUpCodeService.fetchLookUpsByLookUpType('RULE_GROUP_TYPE').subscribe((res:any)=>{
               this.ruleGroupTypes = res; 
           });
           this.commonService.callFunction();
           this.routeSub = this.route.params.subscribe(params => {

             /*  let url = this.route.snapshot.url.map(segment => segment.path).join('/');
               if (params['id']) {
                   this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                   //detail or edit
                   if (url.endsWith('edit') && this.accountingMode=='edit') {
                       
                       //if(!this.ruleGroupRulesAndConditions.rules ||  this.ruleGroupRulesAndConditions.rules.length<=0)
                          // {
                          
                           this.dataViewsService.fetchDataViewsAndColumns().subscribe((res:any)=>{
                               this.sourceDataViewsAndColumns = res; 
                               this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns ;
                               console.log('adding');
                              // this.addNewRuleObject(0,0);
                              // this.addNewCondition(0,0);
                               this.isEdit = true;
                          
                           });
                          
                        
                          // }
                      
                   } else if(this.accountingMode=='view'){
                       this.dataViewsService.fetchDataViewsAndColumns().subscribe((res:any)=>{
                           this.sourceDataViewsAndColumns = res; 
                           this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns ;
                           console.log('data view array is'+JSON.stringify(this.sourceDataViewsAndColumns));
                           if()
                           this.isViewOnly = true;
                       });
                      
                      
                   }
               } else if(this.accountingMode=='create'){
                   this.dataViewsService.fetchDataViewsAndColumns().subscribe((res:any)=>{
                       this.sourceDataViewsAndColumns = res; 
                       this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns ;
                       this.addNewRuleObject(0,0);
                       
                       this.addNewCondition(0,0);
                      
                       this.isCreateNew = true;
                   });
                  
                   
                   
               }
           */
                   
                   //detail or edit
                   if (this.accountingMode=='edit') {
                       this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                       //if(!this.ruleGroupRulesAndConditions.rules ||  this.ruleGroupRulesAndConditions.rules.length<=0)
                          // {
                          
                           this.dataViewsService.fetchDataViewsAndColumns().subscribe((res:any)=>{
                               this.sourceDataViewsAndColumns = res; 
                               this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns ;
                               console.log('adding');
                              // this.addNewRuleObject(0,0);
                              // this.addNewCondition(0,0);
                               this.isEdit = true;
                          
                           });
                          
                        
                          // }
                      
                   } else if(this.accountingMode=='view'){
                       this.fetchGroupAndRulesAndRuleConditionsByGroupId(params['id']);
                       this.dataViewsService.fetchDataViewsAndColumns().subscribe((res:any)=>{
                           this.sourceDataViewsAndColumns = res; 
                           this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns ;
                           console.log('data view array is'+JSON.stringify(this.sourceDataViewsAndColumns));
                           this.isViewOnly = true;
                       });
                      
                      
                   }
              else if(this.accountingMode=='create'){
                  console.log('create mode');
                   this.dataViewsService.fetchDataViewsAndColumns().subscribe((res:any)=>{
                       this.sourceDataViewsAndColumns = res; 
                       this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns ;
                       this.addNewRuleObject(0,0);
                       
                       this.addNewCondition(0,0);
                      
                       this.isCreateNew = true;
                       this.ruleGroupRulesAndConditions.rulePurpose ='RECONCILIATION';
                   });
                   
               }
           });
           if( this.ruleGroupRulesAndConditions.rulePurpose == 'ACCOUNTING')
               {
               
               }
      
          
          }
            SelectRuleGroupType()
            {
                if(this.ruleGroupRulesAndConditions.rulePurpose && this.ruleGroupRulesAndConditions.rulePurpose == 'RECONCILIATION' )
                {
                    this.ruleNameValidation = '';
                    if(this.ruleGroupRulesAndConditions.rules)
                        for(var i = 0;i<this.ruleGroupRulesAndConditions.rules.length;i++)
                        {
                        this.fetchRulesBasedOnRuleGroupTypeAndRuleType(i);
                        }
                }
                // this.fetchRulesBasedOnRuleGroupType();
               
            }
            SelectRuleType(indexVal : any)
            {
                this.fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal);
            }
            fetchRulesBasedOnRuleGroupTypeAndRuleType(indexVal : any)
            {
                //get rule type at that index and filter, and update the filter values
                if(this.ruleGroupRulesAndConditions.rules[indexVal] )
                {
                    let ruleType='';
                    if(this.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType)
                        {
                            ruleType = this.ruleGroupRulesAndConditions.rules[indexVal].rule.ruleType;
                        }
                    else
                        {
                        ruleType='';
                        }
                    this.rulesService.getRules(this.ruleGroupRulesAndConditions.rulePurpose,ruleType).subscribe((res:any)=>{
                    //this.ruleGroupRulesAndConditions.rules[indexVal].rule.rulePurpose =  this.ruleGroupRulesAndConditions.ruleGroupType;
                    let rulesFetched :Rules [] = [];
                    rulesFetched = res;
                    //console.log('rulesFetched'+JSON.stringify(rulesFetched));
                    this.filteredRuleList = [];
                    if(rulesFetched)
                    rulesFetched.forEach(rule=>{
                        this.filteredRuleList.push(rule);
                    });
                    this.ruleListArrays[indexVal]  =[];
                    this.ruleListArrays[indexVal] = this.filteredRuleList;
                    this.addnewLists(indexVal,0);
                    });
                }
                
               
            }
            fetchSrcColumns(srcDataViewName : any,indexVal : any,value:any)
            {
                //console.log('mode->'+this.isEdit+'value is'+JSON.stringify(value));
                if(this.isCreateNew)
                {
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId  = value.data.id;
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName = value.data.dataViewName;
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = '';
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName =  value.data.dataViewDispName;
                   
                    //this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.forEach(condition=>{
                        //condition.sDataViewName=value.data.dataViewName;
                       // condition.sDataViewId = value.data.id;
                      //  condition['sDataviewDisplayName']='';
                       // condition['sDataviewDisplayName']=value.data.dataViewDispName;
                  //  });
                    
                    let lovForSrc =[];
                    this.selectedSrcDataViewAndColumns = value.data.dataViewsColumnsList;
                    
                    lovForSrc [0] =this.selectedSrcDataViewAndColumns;
                    this.sourceColumnLOV[indexVal]= [];
                    this.sourceColumnLOV[indexVal] = ( this.selectedSrcDataViewAndColumns);
                    //console.log('shobha'+JSON.stringify(this.sourceColumnLOV));
                   // //console.log('  this.sourceColumnLOV[indexVal]'+JSON.stringify(  this.sourceColumnLOV[indexVal]));
                }
                else
                {
                    if(this.isEdit)
                    {
                        if(this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions && this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0)
                          {
                        this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.forEach(ruleCondition=>{
                            if(ruleCondition.id)
                                {
                                
                                }
                            else
                            {
                                this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId  = value.data.id;
                                this.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewName = value.data.dataViewName;
                                
                                this.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName = '';
                                this.ruleGroupRulesAndConditions.rules[indexVal].rule.sDataViewDisplayName =  value.data.dataViewDispName;
                            }
                       
                        });
                          }
                       
                    }
                    this.populateSourceColumns(indexVal);
                }
                
            }
            populateSourceColumns(indexVal : any)
            {
                this.sourceColumnLOV[indexVal] =[];
                if(this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions && this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length > 0)
                    for(var childIndex = 0;childIndex<this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length;childIndex++)
                    {
                        this.sourceDataViewsAndColumns.forEach(dataViewObj=>{
                            if(dataViewObj.id == this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId)
                            {
                               
                               //this.sourceColumnLOV[indexVal]=[];
                                this.sourceColumnLOV[indexVal]=dataViewObj.dataViewsColumnsList;
                            }
                        });
                    }
            }
    
            fetchTargetColumns(targetDataViewName:any,indexVal:any,value:any)
            {
                if(this.isCreateNew)
                    {
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = value.data.id;
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName = value.data.dataViewName;
                    
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName ='';
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = value.data.dataViewDispName;
                    
                   /* this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.forEach(condition=>{
                        condition.tDataViewName=value.data.dataViewName;
                        condition.tDataViewId = value.data.id;
                        condition.tDataviewDisplayName='';
                        condition['tDataViewDisplayName']=value.data.dataViewDispName;
                    });*/
                    let lovForTarget =[];
                    this.selectedTargetDataViewAndColumns = value.data.dataViewsColumnsList;
                  
                    lovForTarget [0] = this.selectedTargetDataViewAndColumns ;
                    this.targetColumnLOV[indexVal]= [];
                    this.targetColumnLOV[indexVal] =(this.selectedTargetDataViewAndColumns);
                    //console.log('shobha target'+JSON.stringify(this.targetColumnLOV));
                    ////console.log('  this.targetColumnLOV[indexVal]'+JSON.stringify(  this.targetColumnLOV[indexVal]));
                    }
                else
                    {
                     if(this.isEdit)
                         {
                         if(this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions && this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions .length >0)
                             {
                             this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.forEach(ruleCondition=>{
                                 if(ruleCondition.id)
                                     {
                                     
                                     }
                                 else
                                 {
                                     this.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId = value.data.id;
                                     this.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewName = value.data.dataViewName;
                                     this.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName ='';
                                     this.ruleGroupRulesAndConditions.rules[indexVal].rule.tDataViewDisplayName = value.data.dataViewDispName;
                                 }
                             });
                             }
                         }
                    this.populateTargetColumns(indexVal);
                    }
               
            }
            populateTargetColumns(indexVal : any)
            {
                this.targetColumnLOV[indexVal] =[];
                if(this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions && this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length>0)
                    for(var childIndex = 0;childIndex<this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions.length;childIndex++)
                    {
                    console.log('her target id is'+this.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId);
                        this.targetDataViewsAndColumns.forEach(dataViewObj=>{
                            if(dataViewObj.id == this.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId)
                            {
                               
                               // this.targetColumnLOV[indexVal][childIndex]=[];
                                this.targetColumnLOV[indexVal]=dataViewObj.dataViewsColumnsList;
                            }
                        });
                    }
            }
            autocompleteTargetDataViewListFormatter= (data: any) => {
                let html = `<span >${data.dataViewDispName}  </span>`;
                return this._sanitizer.bypassSecurityTrustHtml(html);
              }
            autocompleteSrcDataViewListFormatter = (data: any) => {
                let html = `<span >${data.dataViewDispName}  </span>`;
                return this._sanitizer.bypassSecurityTrustHtml(html);
              }
            autocompleListFormatter = (data: RulesAndConditions) => {
                let html = `<span >${data.rule.ruleCode}  </span>`;
                return this._sanitizer.bypassSecurityTrustHtml(html);
              }
            autocompleteSrcColumnListFormatter = (data: any) => {
                let html = `<span style='color:blue'>${data.columnName}  </span>`;
                return this._sanitizer.bypassSecurityTrustHtml(html);
              }
            fetchRulesBasedOnRuleGroupType()
            {
            this.rulesService.getRules(this.ruleGroupRulesAndConditions.rulePurpose,'').subscribe((res:any)=>{
                let rulesFetched :Rules [] = [];
                rulesFetched = res;
                rulesFetched.forEach(rule=>{
                    this.filteredRuleList.push(rule);
                  
                });
                this.addnewLists(0,0);
            });
            }
            addnewLists(indexVal:any,childIndex:any)
            {
                let lengthOfRules : number =0;
            if(this.ruleGroupRulesAndConditions && this.ruleGroupRulesAndConditions.rules)
            {
                lengthOfRules =  this.ruleGroupRulesAndConditions.rules.length;
                if(this.ruleListformArray.length<lengthOfRules)
                        {
                        
                        let myForm: FormGroup;
                        myForm = this.builder.group({
                        data : "",
                        });
                        this.ruleListformArray[indexVal]=(myForm);
                        if(this.srcDataViewformsArray.length < lengthOfRules)
                        {
                        let myForm: FormGroup;
                        myForm = this.builder.group({
                        data : "",
                        });
                        // this.srcDataViewformsArray[indexVal] = [];
                        this.srcDataViewformsArray[indexVal]=(myForm);
                        
                        //set columns LOV 
                        this.sourceColumnLOV[indexVal]=[];
                        this.sourceColumnLOV[indexVal][childIndex]=[];
                        
                        }
                        else
                        {
                        }
                        
                        if(this.targetDataViewformsArray.length < lengthOfRules)
                        {
                        let myForm: FormGroup;
                        myForm = this.builder.group({
                        data : "",
                        });
                        //this.targetDataViewformsArray[indexVal]=[];
                        this.targetDataViewformsArray[indexVal]=(myForm);
                        
                        //target columns LOV
                        this.targetColumnLOV[indexVal]=[];
                        this.targetColumnLOV[indexVal][childIndex]=[];
                        }
                        else
                        {
                        }
                        }
                    
            }
            }
            filter0(name: string,indexVal:any ): any[] {
                   
                        return this.optionsArray[indexVal].filter(option => 
                        new RegExp(`^${name}`, 'gi').test(option.ruleCode));
                        
            }
            filter1(name: string ): any[] {
                        return this.sourceDataViewsAndColumns.filter(option => new RegExp(`^${name}`, 'gi').test(option.dataViewDispName)); 
            }
            filter2(name: string ): any[] {
                        return this.targetDataViewsAndColumns.filter(option => new RegExp(`^${name}`, 'gi').test(option.dataViewDispName)); 
            }
            filter3(name: string ): any[] {
                    return this.sourceColumnLOV.filter(option => new RegExp(`^${name}`, 'gi').test(option.columnName)); 
            }
            filter4(name: string ): any[] {
                    return this.targetColumnLOV.filter(option => new RegExp(`^${name}`, 'gi').test(option.columnName)); 
            }
            
            displayFnRuleName(rule: any): string {
                     //this.rules = '';
                     return rule ? rule: rule;
                 }
           filterSrcToTargetViews(sCol : any)
           {
               this.targetDataViewsAndColumns.forEach(targetObj =>{
                   if(targetObj.dataViewDispName === sCol)
                   {
                       let index =  this.targetDataViewsAndColumns.indexOf(targetObj);
                       this.targetDataViewsAndColumns = this.targetDataViewsAndColumns.splice(index, 0);
                   }
               });
           }
            selectsrcColumns(sCol : any)
            {
                if(this.sourceDataViewsAndColumns)
                {
                    this.sourceDataViewsAndColumns.forEach(srcObject => {
                        if(srcObject.dataViewDispName === sCol )
                        {
                            this.sourceColumnLOV =[];
                            this.sourceDataViewId = srcObject.id;
                            this.sourceColumnLOV = srcObject.dataViewsColumnsList;
                        }
                             
                    });
                   // this.filterSrcToTargetViews(sCol);
                }
                else
                {
                }
            }
            selecttargetColumns(tCol : any)
            {
                if(  this.targetDataViewsAndColumns)
                {
                    this.targetDataViewsAndColumns.forEach(targetObj => {
                        if(targetObj.dataViewDispName === tCol)
                        {
                            this.targetColumnLOV=[];
                            this.targetDataViewId = targetObj.id;
                            this.targetColumnLOV = targetObj.dataViewsColumnsList;
                        }
                        
                    }); 
                }
                else
                {
                }
            }
            displayFnSrcDataView(srView : any): string {
                
                return srView ? srView: srView;
            }
            displayFnTargetDataView(targetView: any): string {
                
                return targetView ? targetView: targetView;
            }
            displayFnSrcColumns(srcColumn : any): string
            {
                return srcColumn ? srcColumn: srcColumn;
            }
            displayFnTargetColumns(targetColumn : any)
            {
                return targetColumn ? targetColumn: targetColumn;
            }
            selectedSourceColumn(i : any,childIndex : any,id: any){
                //console.log('id in selectedSourceColumn'+id);
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sColumnId=id;
               // sourceColumnLOV[0]
                //console.log('wwwwthis.sourceColumnLOV[0]'+JSON.stringify(this.sourceColumnLOV[0]));
                this.sourceColumnLOV[i].forEach(srcColObj =>{
                    if(srcColObj.id == id)
                        {
                            this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sColumnName =srcColObj.columnName;
                        }
                     
                });
                
            }
            selectedOperator()
            {
                
            }
            selectedTargetColumn(i : any,childIndex : any,id: any)
            {
                //console.log('id in selectedTargetColumn'+id);
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tColumnId=id;
            }
            
            /*validateRuleType()
            {
                if(!this.ruleGroup.ruleGroupType)
                    {
                     this.ruleNameValidation = 'Select Rule Group Type';  
                    }
            }*/
            setRuleObject(ruleName : any,indexVal : any,value:any)
            {
                //if existing rule is selected populate the existing details else no
              //console.log('value=>'+JSON.stringify(value));
                let rulesArray :Rules[]= [];
                rulesArray= this.ruleListArrays[indexVal];
                if(value.data && value.data.rule && value.data.rule.id)
                {
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule = value.data.rule;
                    //console.log('selected rule obj issss:'+JSON.stringify(this.ruleGroupRulesAndConditions.rules[indexVal].rule));
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupRulesAndConditions.rules[indexVal].rule.startDate);
                    this.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate = this.dateUtils.convertLocalDateFromServer(this.ruleGroupRulesAndConditions.rules[indexVal].rule.endDate);
                    this.ruleGroupRulesAndConditions.rules[indexVal].ruleConditions = value.data.ruleConditions;
                    //console.log('sourceDataViewsAndColumns'+JSON.stringify(this.sourceDataViewsAndColumns));
                    //fetch source and target column lists
                    let srcviews = [];
                    srcviews = this.sourceDataViewsArrays[indexVal];
                    srcviews.forEach(dataView => {
                        //console.log('if(this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId'+this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId);
                       //console.log('dataView.id'+dataView.id);
                        if(+this.ruleGroupRulesAndConditions.rules[indexVal].rule.sourceDataViewId == +dataView.id)
                            {
                            //console.log('yes matched');
                                this. sourceColumnLOV[indexVal] = dataView.dataViewsColumnsList;
                            }
                    });
                    //console.log(' this. sourceColumnLOV[0] '+JSON.stringify( this. sourceColumnLOV[indexVal] ));
                    let targetViews = [];
                    targetViews = this.targetDataViewsArrays[indexVal];
                    targetViews.forEach(dataView =>{
                        if(+this.ruleGroupRulesAndConditions.rules[indexVal].rule.targetDataViewId == +dataView.id)
                        {
                            //console.log('yes target matched');
                            this. targetColumnLOV[indexVal] = dataView.dataViewsColumnsList;
                        }
                    });
                    //console.log(' this. targetColumnLOV[0] '+JSON.stringify( this. targetColumnLOV[indexVal] ));
                  
                    
                }
                else
                {
                    
                }
               
                //fetch rule details and rule conditions for rule id
                //integrate api
            }
            changeState(create : boolean, view : boolean, edit : boolean)
            {
                    this.isCreateNew = create;
                     this.isViewOnly =view;
                    this.isEdit = edit;
            }
            saveRuleGroup()
            {
                if(this.ruleGroupRulesAndConditions.rulePurpose == 'ACCOUNTING')
                    {
                        console.log('mode is'+this.isCreateNew);                        
                        if(this.isCreateNew)
                        this.ruleGrpWithRuleAndLineItems.enabledFlag = true;
                        
                        this.ruleGrpWithRuleAndLineItems.rulePurpose = this.ruleGroupRulesAndConditions.rulePurpose;
                        this.ruleGrpWithRuleAndLineItems.name = this.ruleGroupRulesAndConditions.name;
                        this.ruleGrpWithRuleAndLineItems.startDate =this.ruleGroupRulesAndConditions.startDate;
                        this.ruleGrpWithRuleAndLineItems.endDate = this.ruleGroupRulesAndConditions.endDate;
                        console.log('ruleGrpWithRuleAndLineItems'+JSON.stringify(this.ruleGrpWithRuleAndLineItems));
                        //integrate post API
                    }
                else
                    {
                    for(var i=0;i<this.ruleGroupRulesAndConditions.rules.length;i++)
                    {
                    //let rule :any={};
                    // rule = this.ruleGroupRulesAndConditions.rules[i];
                    //console.log('null or not'+this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode);
                    if(!this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode)
                    {
                    this.ruleGroupRulesAndConditions.rules.splice(i,1);
                    
                    }else
                    {
                    //this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode = this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode;
                    //console.log('not null');
                    }
                    }
                    
                    if(this.ruleGroupRulesAndConditions.rules.length <= 0)
                    {
                    this.commonService.error(
                    'Warning!',
                    'Tag atleast one rule' 
                    )
                    }
                    else
                    {
                        let msg : string ='Tag atleast one Rule Condition for Rules - ';
                        let count :  number = 0;
                        this.ruleGroupRulesAndConditions.rules.forEach(rule =>{
                        if(rule.ruleConditions && rule.ruleConditions.length > 0)
                            {
                            count =  count + 1;
                            }
                        else
                            {
                                msg = msg +', '+ rule.rule.ruleCode;
                            }
                        
                    });
                   console.log('befor eservice'+JSON.stringify(this.ruleGroupRulesAndConditions));
                    if(count  ==  this.ruleGroupRulesAndConditions.rules.length){
                   /* this.ruleGroupService.postRuleGroup(this.ruleGroupRulesAndConditions).subscribe((res:any)=>{
                        let savedObj = [];
                        savedObj= res;
                        savedObj.forEach(task =>{
                            if(task.taskName === 'Rule Group Save')
                            {
                                this.commonService.success(
                                        'Saved!',
                                        this.ruleGroupRulesAndConditions.name + ' saved successfully!' 
                                )
                                let groupId:any;
                                        groupId = task.details;
                                        this.router.navigate(['/rule-group', {outlets: {'content': groupId+'/details'}}]);
                                        this.fetchGroupAndRulesAndRuleConditionsByGroupId(groupId);
                            }
                        });
                        
                        }); */
                        }
                    else
                        {
                        this.commonService.error(
                                'Warning!',
                                msg 
                                )
                        }
                    }
                  
                    }
             
                
            }
            fetchGroupAndRulesAndRuleConditionsByGroupId(groupId : any)
            {
                this.ruleGroupService.getGroupDetails(groupId).subscribe((res:any)=>{
                    
                    this.ruleGroupRulesAndConditions = res;
                    if( this.ruleGroupRulesAndConditions.rules && this.ruleGroupRulesAndConditions.rules.length>0)
                        {
                            this.expandTab[0] = true;
                        }
                   // this.ruleGroupRulesAndConditions.ruleGroupType =  this.ruleGroupRulesAndConditions.rules[0].rule.rulePurpose;
                    this.ruleGroupTypes.forEach(ruleGRpLookUp=>{
                        if(ruleGRpLookUp.lookUpCode == this.ruleGroupRulesAndConditions.rulePurpose)
                            {
                            this.ruleGroupRulesAndConditions.rulePurpose = ruleGRpLookUp.lookUpCode;
                            }                                                                                                                                                                                                                                                                                                                                     
                        
                    })
                    this.setFormGroupsForExisting();
                });
            }
            validationMsg()
            {
                
            }
            checkGroupName(name : string)
            {
                this.ruleGroupService.query().subscribe(
                        (res: Response) => {
                            this.ruleGroupsList = res.json();
                            let count : number =0;
                            //console.log(' this.ruleGroupsList'+JSON.stringify( this.ruleGroupsList));
                            this.ruleGroupsList.forEach(ruleGroup=>{
                                if(ruleGroup.name.toLowerCase() == name.toLowerCase())
                                    {
                                    count = count +1;
                                       
                                    }
                                else
                                    {
                                    
                                    }
                            });
                            if(count>0)
                                {
                                this.duplicateRuleGroupName = true;
                                }
                            else
                                {
                                this.duplicateRuleGroupName = false;
                                }
                        }
                    );
                
            }
            deleteCondition(i,childIndex)
            {
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions.splice(childIndex, 1);
            }
            deletRule(i)
            {
                this.ruleGroupRulesAndConditions.rules.splice(i, 1);
            }
            displayOptions()
            {
                this.showOptions = true;
            }
            filterDataView()
            {
                console.log('filtering dv');
            }
            addFilter(i: any,childIndex : any)
            {
                //this.sAdditionalFiltersTitle[childIndex] = this.sFilterTitle ;
                if(!this.sFilter[i])
                {
                    this.sFilter[i] = [];
                }
                else
                {
                    this.sFilter[i][childIndex] = '';
                    
                }
                this.sFilter[i][childIndex] = '';
                this.sFilter[i][childIndex] = true;
                if(this.sFilter[i][childIndex])
                    {
                   this.addDisplayPopOverContentClass();
                    }
               
                if(this.sFormula[i] && this.sFormula[i][childIndex])
                {
                    this.sFormula[i][childIndex]=false;
                }
                if(this.sTolerance[i] && this.sTolerance[i][childIndex])
                   {
                    this.sTolerance[i][childIndex] = false;
                   }
                // this.sFormula[childIndex] = false;
                //this.sTolerance[childIndex] = false;
               
            }
            addDisplayPopOverContentClass()
            {
               // $('.highlightActions li:eq(1)').addClass('highlight_stay');
                $('.rule-conditions-view').addClass('display-popoverbody');
                $('.rule-conditions-view .display-PopOverActions').removeClass('col-md-12').addClass('col-md-2');
                
            }
            addFormula(i: any,childIndex : any)
            {
                //this.sAdditionalFiltersTitle[childIndex] = this.sFormulaTitle;
                if(!this.sFormula[i])
                {
                    this.sFormula[i] = [];
                }
                else
                {
                   
                }
                this.sFormula[i][childIndex] = '';
                this.sFormula[i][childIndex] = true;
                
                if(this.sFormula[i][childIndex])
                this.addDisplayPopOverContentClass();
                
                if(this.sTolerance[i] && this.sTolerance[i][childIndex])
                {
                 this.sTolerance[i][childIndex] = false;
                }
                if(this.sFilter[i] && this.sFilter[i][childIndex])
                {
                 this.sFilter[i][childIndex] = false;
                }
               // this.sFilter[childIndex]= false;
               // this.sTolerance [childIndex]= false;
            }
            addTolerance(i: any,childIndex : any)
            {
                //this.sAdditionalFiltersTitle[childIndex] = this.tToleranceTitle;
                if(!this.sTolerance[i])
                {
                    this.sTolerance[i] = [];
                }
                else
                {
                    
                }
                this.sTolerance[i][childIndex] = '';
                this.sTolerance[i][childIndex] = true;
                
                if(this.sTolerance[i][childIndex])
                this.addDisplayPopOverContentClass();
                
                if(this.sFormula[i] && this.sFormula[i][childIndex])
                {
                    this.sFormula[i][childIndex]=false;
                }
                if(this.sFilter[i] && this.sFilter[i][childIndex])
                {
                 this.sFilter[i][childIndex] = false;
                }
                //this.sFormula[childIndex] = false;
               // this.sFilter [childIndex]= false;
            }
            selectOperator()
            {
                
            }
            addtFilter(i:any,childIndex : any)
            {
               // this.tAdditionalFiltersTitle[i][childIndex] = this.tFilterTitle ;
                if(this.tFilter[i])
                    {
                    
                    }
                else
                    {
                    this.tFilter[i] = [];
                    }
               
                this.tFilter[i][childIndex] = '';
                this.tFilter[i][childIndex] = true;
                
                if(this.tFilter[i][childIndex])
                this.addDisplayPopOverContentClass();
                if(this.tFormula[i] &&  this.tFormula[i][childIndex])
                {
                    this.tFormula[i][childIndex] =false;
                }
                if(this.tTolerance[i] &&  this.tTolerance[i][childIndex])
                {
                    this.tTolerance[i][childIndex]= false;
                }
                //this.tFormula[childIndex] = false;
                //this.tTolerance[childIndex] = false;
            }
            addtFormula(i:any,childIndex : any)
            {
                //this.tAdditionalFiltersTitle[childIndex] = this.tFormulaTitle;
                if(this.tFormula[i])
                {
                
                }
            else
                {
                this.tFormula[i] = [];
                }
                this.tFormula[i][childIndex] = '';
                this.tFormula[i][childIndex] = true;
                
                if(this.tFormula[i][childIndex])
                    this.addDisplayPopOverContentClass();
                
                if(this.tFilter[i] && this.tFilter[i][childIndex] )
                {
                    this.tFilter[i][childIndex] = false;
                }
                if(this.tTolerance[i] && this.tTolerance[i][childIndex])
                {
                    this.tTolerance[i][childIndex] = false;
                }
                //this.tFilter[childIndex] = false;
                //this.tTolerance[childIndex] = false;
            }
            addtTolerance(i:any,childIndex : any)
            {
                if(this.tTolerance[i])
                {
                
                }
            else
                {
                this.tTolerance[i] = [];
                }
                this.tTolerance[i][childIndex]='';
                this.tTolerance[i][childIndex] = true;
                
                if(this.tTolerance[i][childIndex] )
                    this.addDisplayPopOverContentClass();
                
                if(this.tFormula[i] &&  this.tFormula[i][childIndex])
                {
                    this.tFormula[i][childIndex] =false;
                }
                if(this.tFilter[i] && this.tFilter[i][childIndex] )
                {
                    this.tFilter[i][childIndex] = false;
                }
                //this.tAdditionalFiltersTitle [childIndex]= this.tToleranceTitle;
                //this.tFormula[childIndex] = false;
                //this.tFilter[childIndex] = false;
            }
            selecttOperator()
            {
                
            }
            selectedLogicalOperator()
            {
                
            }
            showExcelFunction(val,i, childIndex, examp){
                console.log('val  :' + val );
                console.log('ind :' + childIndex);
                console.log('examp :' + examp);
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormulaExpressionExample = examp;
            }
            checkTargetAndRefresh()
            {
            }
            savesrcExpression(val,i,childIndex){
                console.log(val);
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula = val;
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sformulaSave= true;
                this.sFormula[i][childIndex]=false;
                this.removeAddedClasses();
            }
            savetargetExpression(val,i,childIndex){
                console.log(val);
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula = val;
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tformulaSave= true;
                this.tFormula[i][childIndex]=false;
                this.removeAddedClasses();
                     } 
            
            savesrcFilter(i,childIndex){
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sfilterSave= true;
                this.sFilter[i][childIndex]= false;
                this.removeAddedClasses();
                //.addClass('col-md-2');
            }
            savetargetFilter(i,childIndex){
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tfilterSave= true;
                this.tFilter[i][childIndex]= false;
                this.removeAddedClasses();
                
            }
            savesrcTolerance(i,childIndex){
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].stoleranceSave= true;
                this.sTolerance[i][childIndex]= false;
                this.removeAddedClasses();
            }
            savetargetTolerance(i,childIndex){
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].ttoleranceSave= true;
                this.tTolerance[i][childIndex]= false;
                this.removeAddedClasses();
            }
            
            cancelsrcFilter(i,childIndex){
                
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sOperator ='';
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sValue ='';
                this.sFilter[i][childIndex]= false;
                console.log(' this.sFilter[i][childIndex]'+ this.sFilter[i][childIndex]);
                this.removeAddedClasses();
            }
            cancelTargetFilter(i,childIndex){
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tOperator ='';
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tValue ='';
                this.tFilter[i][childIndex]= false;
              this.removeAddedClasses();
            }
            cancelsrcFormula(i,childIndex)
            {
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sFormula ='';
                this.sFormula[i][childIndex]=false;
                this.removeAddedClasses();
            }
            canceltargetFormula(i,childIndex)
            {
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tFormula ='';
                this.tFormula[i][childIndex]=false;
                this.removeAddedClasses();
            }
            cancelsrcTolerance(i,childIndex)
            {
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].sToleranceValueFrom ='';
                this.sTolerance[i][childIndex]= false;
                this.removeAddedClasses();
            }
            canceltargetTolerance(i,childIndex)
            {
                this.ruleGroupRulesAndConditions.rules[i].ruleConditions[childIndex].tToleranceValueFrom ='';
                this.tTolerance[i][childIndex]= false;
                this.removeAddedClasses();
            }
            removeAddedClasses()
            {
                $('.rule-conditions-view').removeClass('display-popoverbody');
                $('.rule-conditions-view .display-PopOverActions').addClass('col-md-12');
            }
           closeSrc(i,childIndex)
           {
               this.sFilter[i][childIndex] = false;
               this.sFormula[i][childIndex] = false;
               this.sTolerance[i][childIndex] = false;
               this.removeAddedClasses();
           }
           closeTarget(i,childIndex)
           {
               this.tFilter[i][childIndex] = false;
               this.tFormula[i][childIndex] = false;
               this.tTolerance[i][childIndex] = false;
               this.removeAddedClasses();
           }
           deleteTargetFilter()
           {
               
           }
           clearRuleObject(i)
           {
               if(this.ruleGroupRulesAndConditions.rules[i] )
               {
                   let ruleObj = new RulesAndConditions();
                   let rule= new Rules();
                   ruleObj.rule = rule;
                   let ruleConditions = new RuleConditions();
                   ruleObj.ruleConditions = [];
                   this.ruleGroupRulesAndConditions.rules[i] = ruleObj;
                   this.addNewCondition(i,0);
               }
           }
           changeRuleName(val,i)
           {
               console.log('$event'+val);
               if(val == "" )
               {
                         console.log('val is empty clear the rule object');
                         let ruleObj = new RulesAndConditions();
                         let selectedRuletype= '';
                         selectedRuletype=this.ruleGroupRulesAndConditions.rules[i].rule.ruleType;
                         if(this.ruleGroupRulesAndConditions.rules[i] && this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode)
                             {
                                 let rule= new Rules();
                                 ruleObj.rule = rule;
                                 ruleObj.rule.ruleType= selectedRuletype;
                                 let ruleConditions = new RuleConditions();
                                 ruleObj.ruleConditions = [];
                                 this.ruleGroupRulesAndConditions.rules[i] = ruleObj;
                                 this.addNewCondition(i,0);
                             }
                         
               }
               else if(val != "" )
                   {
                   console.log('val is not empty ');
                   }
               let ruleType= '';
               if(this.ruleGroupRulesAndConditions.rules[i].rule.ruleType)
                   {
                       ruleType = this.ruleGroupRulesAndConditions.rules[i].rule.ruleType;
                   }
               this.rulesService.getRules(this.ruleGroupRulesAndConditions.rulePurpose,ruleType).subscribe((res:any)=>{
                   this.filteredRuleList =res;
                   this.ruleListArrays[i] =  this.filteredRuleList;   
               });
              
              // console.log('ruleListArrays'+JSON.stringify(this.ruleListArrays[i]));
               /* 
               let rule = new Rules();
               let ruleConditions = new RuleConditions();
               if(this.ruleGroupRulesAndConditions.rules[i].rule && this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode)
               {
                   
               }
               else if(this.ruleGroupRulesAndConditions.rules[i].rule && this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode)
               {
                   console.log('shobhaaaaaaaaaaaa'+this.ruleGroupRulesAndConditions.rules[i].rule.ruleCode);
               }
               else
               {
                   console.log('val isdefs');
                   this.ruleGroupRulesAndConditions.rules[i].rule = rule;
                   this.ruleGroupRulesAndConditions.rules[i].ruleConditions = [];
               }*/
               
           }
           deleteRuleObject(i)
           {
               
               this.ruleGroupRulesAndConditions.rules.splice(i, 1);
               if(this.ruleGroupRulesAndConditions.rules.length == 0)
                   {
                       this.addNewRuleObject(0, 0);
                   }
           }
           changeTemplate()
           {
               console.log('change in template triggered');
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
              
           if(  this.ruleGroupRulesAndConditions.rules)
           {
               var ruleListChanges = this.ruleGroupDiffer.diff(this.ruleGroupRulesAndConditions.rules);
               if(ruleListChanges)
               {
                   //console.log('==>1');
                   //this detection is only when object is added / removed to array
                   //console.log('changes detected for list of rules');
                   //console.log('previous were detected for rule'+JSON.stringify(this.ruleGroupRulesAndConditions.rules));
                  this.checkForRules();
               }
               else
                   {
                   // console.log('==>2');
                   //console.log('no addition / removal');
                   this.checkForRules();
                   }
           }
           
           }
           checkForRules()
           {
               let i:number =0;
         //  console.log('this.ruleGroupRulesAndConditions.rules length'+this.ruleGroupRulesAndConditions.rules.length);
           this.ruleGroupRulesAndConditions.rules.forEach(rule => {
               // console.log('check if rule is changed');
               var changesForRule = this.ruleObjDiffer.diff(rule.rule);
               var changesForRuleConditions = this.ruleConditionsDiffer.diff(rule.ruleConditions);
               
               if(changesForRule) {
                   // console.log('==>3');
                   // console.log('changes detected for rule'+JSON.stringify(rule));
                   //
                   changesForRule.forEachChangedItem(r => {
                       // console.log('changed rule is ', JSON.stringify(r.currentValue));
                       // console.log('previous val is ', JSON.stringify(r.previousValue))
                       // this.validateRuleObject(r.currentValue,r.previousValue);
                   }
                   
                   );
                   changesForRule.forEachAddedItem(r => {
                       //console.log('added rule' + JSON.stringify(r.currentValue)
                   }
                   );
                   changesForRule.forEachRemovedItem(r =>{
                       // console.log('removed ' + JSON.stringify(r.currentValue)
                   }
                   );
               } else {
                   // console.log('==>4');
                   // console.log('nothing changed for'+rule.rule.id);
               }
               
               if(changesForRuleConditions)
               {
                   /* rule.ruleConditions.forEach(ruleCondition=>{
                           var changesForCondition = this.ruleConditionObjDiffer.diff(ruleCondition);
                           if(changesForCondition)
                               {
                                   this.checkForConditions(i);
                               }
                       })*/
                   //  console.log('==>5');
                   this.checkForConditions(i);
               }
               else
               {
                   //console.log('no change in rule conditions array);
                   // console.log('==>6');
                   this.checkForConditions(i);
               }
               i=i+1;
           });
           }
           checkForConditions(i)
           {
               let j:number =0;
           this.ruleGroupRulesAndConditions.rules[i].ruleConditions.forEach(ruleCondition => {
               var changesForCondition = this.ruleConditionObjDiffer.diff(ruleCondition);
               if(changesForCondition)
               {
                   //console.log('changes in conditions'+JSON.stringify(ruleCondition));
                   //
                   //console.log('==>7');
                   changesForCondition.forEachChangedItem(r => {
                        //console.log('changed condition is ', JSON.stringify(r.currentValue))
                       // console.log('previous val is ', JSON.stringify(r.previousValue))
                       // this.validateRuleObject(r.currentValue,r.previousValue);
                    }
                    );
                   changesForCondition.forEachAddedItem(r => {
                       //console.log('Condition  added ' + JSON.stringify(r.currentValue)
                       
                   }
                   );
                   changesForCondition.forEachRemovedItem(r =>{
                       //console.log('Condition removed ' + JSON.stringify(r.currentValue);
                   });
               }
               else
                   {
                   // console.log('==>8');
                   }
           });
           
           }
           /*validateRuleObject(currentRule,previousRule)
           {
               console.log('currentRule'+JSON.stringify(currentRule));
               console.log('previousRule'+JSON.stringify(previousRule));
           }*/

           ngOnDestroy() {
            this.notificationsService.remove();
        }
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           

}
