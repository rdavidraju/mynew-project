import { Component, Input, OnInit, OnDestroy,ViewChild } from '@angular/core'; 
import { ActivatedRoute } from '@angular/router';
import { ApprovalActions } from './approval-actions.model';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';
import { JhiDateUtils } from 'ng-jhipster';
import { RuleGroupPopupService } from './rule-group-popup.service';
import { RuleGroupService } from './rule-group.service';
import { RuleGroupWithRulesAndConditions } from './ruleGroupWithRulesAndConditions.model'
import { RuleGroupAndRuleWithLineItem } from './ruleGroupAndRuleWithLineItem.model';
import { AccRule } from './accRule.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { LineItem } from './LineItem.model';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { DataViewsService } from '../data-views/data-views.service';
import { AccountingRuleConditions } from './accounting-rule-conditions.model';
import {ApprovalRuleGRoupWithRules} from './approval-rule-groupWithRules.model';
import { FormGroup, FormBuilder, FormArray, Validators ,NgForm} from '@angular/forms';
import { RulesService } from '../rules/rules.service';
import { Rules } from '../rules/rules.model';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { MappingSetService } from '../mapping-set/mapping-set.service';
import { CommonService } from '../common.service';
import { Router, NavigationEnd } from '@angular/router';
//import { NotificationsService } from 'angular2-notifications-lite';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import {ApprovalRules} from './approval-rules.model';
import {ApprovalConditions} from './approval-conditions.model';
import {ApprovalActionDetails} from './approval-action-details.model';
import {UserService} from '../../shared/user/user.service';
import {ApprovalGroupsService} from '../approval-groups/approval-groups.service';
import { NotificationBatchService } from '../notification-batch/notification-batch.service';
import {ApprovalsConfirmActionModalDialog} from './rule-group-approvals-confirm-action-dialog';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
//import { IMultiSelectOption,IMultiSelectSettings,IMultiSelectTexts } from 'angular-2-dropdown-multiselect';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import * as $ from "jquery";
declare var jQuery: any;
//declare var $: any;
const URL = '';
@Component( {
    selector: 'jhi-rule-group-approvals',
    templateUrl: './rule-group-approvals.component.html'
} )
export class RuleGroupApprovalsComponent {
    today=new Date();  
    startDateChange = false;
    endDateChange = false; 
    openRuleTab: any =[];
    ngxScrollToLastRule:string;
    closeBracket=')';
    openBracket='(';
//    mySettings: IMultiSelectSettings = {
//        enableSearch: true,
//        checkedStyle: 'fontawesome',
//        buttonClasses: 'btn btn-default btn-block',
//        dynamicTitleMaxItems: 3,
//        displayAllSelectedText: true,
//        closeOnClickOutside:true,
//        closeOnSelect:true,
//        
//        autoUnselect:true
//    };
//    myTexts: IMultiSelectTexts = {
//        checkAll: 'Select all',
//        uncheckAll: 'Unselect all',
//        checked: 'item selected',
//        checkedPlural: 'items selected',
//        searchPlaceholder: 'Find',
//        searchEmptyResult: 'Nothing found...',
//        searchNoRenderText: 'Type in search box to see results...',
//        defaultTitle: 'Select',
//        allSelected: 'All selected',
//    };
    dropdownSettings = {
        singleSelection: true,
      //  text: "Data source",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        selectionLimit: 1,
        autoUnselect: true,
        closeOnSelect: true,
        showCheckbox: false
    };
    userDropdownSettings = {
        singleSelection: true,
       // text: "Select approver",
        enableSearchFilter: true,
        classes: "cuppaSingleSelection",
        selectionLimit: 1,
        autoUnselect: true,
        closeOnSelect: true,
        showCheckbox: false
    };
    @ViewChild(NgForm) approvalForm;
    sequences = [];
  //  approvalRuleGRoupWithRules = new ApprovalRuleGRoupWithRules();
    routeSub: any;
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    copyRule = false;

    //existingRules = [];
    
    srcDataViewformsArray: FormGroup[] = [];
    ruleListformArray: FormGroup[] = [];    
    
    ruleListArrays: any[] = [];
    sourceDataViewsArrays:any[]=[];
    
   sourceDataViewsAndColumns = [];
    targetDataViewsAndColumns = [];
    
   // columnLOV=[];
    //operators=[];
    
    logicalOperators=[];
    
    usersList = [];
    groupsList = [];
    groupMembers =[];
    
    approvalType = [];
    
    userListformArray: any = [];  
    
    userListLOV = [];
    form: FormGroup;
    approvalTypeLov = [];
    constructor(
        private jhiLanguageService: JhiLanguageService,
        public ruleGroupService: RuleGroupService,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils,
        private config: NgbDatepickerConfig,
        private lookUpCodeService: LookUpCodeService,
        private dataViewsService: DataViewsService,
        private builder: FormBuilder,
        private  rulesService: RulesService,
        private _sanitizer: DomSanitizer,
        private mappingSetService: MappingSetService,
        private commonService: CommonService,
        private router: Router,
        private route: ActivatedRoute,
      //  private notificationsService: NotificationsService,
        private userService: UserService,
        private approvalGroupsService: ApprovalGroupsService,
        private notificationBatchService: NotificationBatchService,
        public dialog: MdDialog

    ) {
        this.form = builder.group({
            childInput: ''
          });
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    ngOnInit() {
        //fetch logical operators
       
        this.ruleGroupService.submitted=false;
        this.lookUpCodeService.fetchLookUpsByLookUpType( 'LOGICAL_OPERATOR' ).subscribe(( res: any ) => {
            this.logicalOperators = res;
        } );
        this.lookUpCodeService.fetchLookUpsByLookUpType( 'APPROVAL_TYPE' ).subscribe(( res: any ) => {
           this. approvalType = res;
        } );
       
        
        let url = this.route.snapshot.url.map( (segment) => segment.path ).join( '/' );
        this.routeSub = this.route.params.subscribe( (params) => {
            this.dataViewsService.dataViewList().subscribe((res:any)=>{
                this.sourceDataViewsAndColumns = res;
                this.sourceDataViewsAndColumns.forEach((dv) => {
                    dv['itemName'] = dv['dataViewDispName'];
                });
                //this.rulesService.getApprovalRules('APPROVALS').subscribe(( res: any ) => {
                 //   this.existingRules=res;
               
                console.log('params has '+params['id']);
                this.notificationBatchService.usersListBasedOnRoleTask('IS_APPROVAL').subscribe(
                    (resp) => {
                        this.usersList = resp;
                        this.usersList.forEach((user) => {
                            if( user['lastName']){
                                user['itemName'] = user['firstName']+' ' + user['lastName'];
                            }else{
                                user['itemName'] = user['firstName'];
                            }
                          
                        });
                        this.approvalGroupsService.approvalgroupsForTenant().subscribe((response:any)=>{
                            this.groupsList=response;
                            if ( params['id'] ) {
                                this.fetchApprovalRuleGroup(params['id'] );
                                    if ( url.endsWith( 'edit' )) {
                                        this.isEdit = true;  
                                        this.addNewRuleObject(0);
                                        if (url.search('copy') != -1) {
                                            this.copyRule = true;
                                        } 
                                       } else {
                                               this.isViewOnly=true;
                                        
                                           }
                                
                            } else {
                                this.fetchExistingRules();
                                this.isCreateNew= true;
                                //console.log('==============>1');
                               this.buildObjectsWhileNew();
                              // this.addNewRuleObject(0);
                            }
                        });
                    });
                   
               
             //   });
            });
           
        });
        
    }
    //If Start Date Entered Apply Class
    startEndDateClass(indexVal: any){
        if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].startDate != null){
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }else{
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }
    createFormsAndDisplay() {
        let i = 0;
        if( this.ruleGroupService.approvalRuleGRoupWithRules .rules) {
            this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach( (rule) => {
              
               // this.fetchSrcColumns( "", i, value );
                //operators based on cols
                if(rule.approvalActions && rule.approvalActions.assigneeType=='group') {
                    this.getGroupMembers(rule.approvalActions.actionDetails[0].assigneeId);
                }  else {
                    const actnIndx = 0;
                    rule.approvalActions.actionDetails.forEach((actnDet)=>{
                        this.setFormForUser(i,actnIndx);
                    });
                }
                //this.setFormsForAutocompletes(i);
                i=i+1;
                
            } );
        }
       
    }
    fetchApprovalRuleGroup(groupId: any){
        this.ruleGroupService.fetchApprovalRules(groupId).subscribe(( res: any ) => {
           let approvalRuleGRoupWithRules:any =[];
                approvalRuleGRoupWithRules = res;
            //let ruleNotEligibleToEdit = approvalRuleGRoupWithRules.find(rule => rule.editRule == false);
            for(let cat = 0;cat<approvalRuleGRoupWithRules.rules.length;cat++)     {
                approvalRuleGRoupWithRules.rules[cat].selectedIndex=0;
                if(approvalRuleGRoupWithRules.rules[cat] && !approvalRuleGRoupWithRules.rules[cat].editRule) {
                    this.ruleGroupService.ruleGroup.editRule = false;
                    break;
                }   else{
                    this.ruleGroupService.ruleGroup.editRule=true;
                }
            }
            //console.log('after fetchinggg'+JSON.stringify(approvalRuleGRoupWithRules ));
            this.ruleGroupService.approvalRuleGRoupWithRules = approvalRuleGRoupWithRules;
            if(!this.openRuleTab){
                this.openRuleTab=[];
            }
    



                 
            for (let t = 0; t < this.ruleGroupService.approvalRuleGRoupWithRules.rules.length; t++) {
              // if(this.isEdit)
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].editRule=true;
                if (t == 0){
                    this.openRuleTab[t] = true;
                }else{
                    this.openRuleTab[t] = false;
                }
                
                  

                if(this.isEdit)  {
                    const dv = {
                        "id":this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].sourceDataViewId,
                         "itemName": this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].sourceDataViewName
                        };
                         this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].sourceDVId = [];
                         this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].sourceDVId.push(dv);
                         this.fetchSrcColumns(dv,t);
                         for(let j=0;j<this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalConditions.length;j++)  {
                            if ( this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalConditions[j].colDataType ) {
                                this.lookUpCodeService.fetchLookUpsByLookUpType(  this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalConditions[j].colDataType  ).subscribe(( res: any ) => {
                                    const operatorss = res;
                                   this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalConditions[j].operators=operatorss;
                                } );
                            }
                         }
                        
                         for(let j=0;j<this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalActions.actionDetails.length;j++)  {
                            const user ={
                                "id":this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalActions.actionDetails[j].assigneeId,
                                "itemName":this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalActions.actionDetails[j].assigneeName
                            };
                            this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalActions.actionDetails[j].approverId =[];
                            this.ruleGroupService.approvalRuleGRoupWithRules.rules[t].approvalActions.actionDetails[j].approverId .push( user);
                         } 
                        
                }
                             
            }
            this.ruleGroupService.filterViewsByReconRuleGroupAndAccountingDataSource( this.ruleGroupService.approvalRuleGRoupWithRules.apprRuleGrpId,'')
            .subscribe((resp1: any) => {
                let viewsList = [];
                 viewsList = resp1;
                 if(viewsList && viewsList.length>0)  {
                   this.sourceDataViewsAndColumns = viewsList;
                 }
                });
            for(let c=0;c<this.ruleGroupService.approvalRuleGRoupWithRules.rules.length;c++)  {
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[c].formCntrl = new FormControl('');
            }
            // this.notificationBatchService.usersListBasedOnRoleTask('IS_APPROVAL').subscribe(
            //     (res) => {
            //         this.usersList = res;
            //         console.log('this.usersList ' + JSON.stringify(this.usersList));
            //         this.createFormsAndDisplay();
            // });
        });
    }
    // fetchDataViewsbyTenant()
    // {
    //     this.dataViewsService.dataViewList().subscribe((res:any)=>{
    //         this.sourceDataViewsAndColumns = res;
    //         this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns;
    //     });
    // }
    fetchDataViewsByRuleGroup() {
        this.sourceDataViewsAndColumns=[];
        this.dataViewsService.fetchDataViewsByRuleGroup(this.ruleGroupService.ruleGroup.apprRuleGrpId).subscribe((res:any)=>{
            this.sourceDataViewsAndColumns = res;
            this.sourceDataViewsAndColumns.forEach((dv) => {
                dv['itemName'] = dv['DataViewDispName'];
            });
            this.ruleGroupService.approvalRuleGRoupWithRules .rules.forEach((rule)=>{
                rule.sourceDVId=null;
                rule.sourceDataViewId=null;
            });
          //  this.targetDataViewsAndColumns = this.sourceDataViewsAndColumns;
        });
    }
    buildObjectsWhileNew() {
        //add one new rule object
       
        this.addNewRuleObject(0);
        
    }
    setFormsForAutocompletes(ind: any){
        let ruleListForm: FormGroup;
        ruleListForm = this.builder.group( {
            data: "",
        } );
        this.ruleListformArray[ind] = ruleListForm;
        if ( !this.ruleListArrays ){
            this.ruleListArrays = [];
        }
            

        this.ruleListArrays[ind] = [];
       // this.ruleListArrays[ind] = this.existingRules;
    //add filtered rule list to the above array

        let myForm1: FormGroup;
        myForm1 = this.builder.group( {
            data: "",
        } );
        this.srcDataViewformsArray[ind] = ( myForm1 );
        this.sourceDataViewsArrays[ind] = [];
        //add source dv array
        this.sourceDataViewsArrays[ind] = this.sourceDataViewsAndColumns;
        
        //this.operators[ind]=[];
       
    }
    setFormForUser(i,j) {
        let userListForm: FormGroup;
        userListForm = this.builder.group( {
            data: "",
        } );
        if(!this.userListformArray[i] )   {
            this.userListformArray[i] = [];
        }
        this.userListformArray[i][j] = userListForm;
        //this.userListLOV[i] = jQuery.extend(true, [], this.usersList);
        
    }
    switchTabs(event, i) {
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].selectedIndex = event.index;
        // this.debitSelectedIndex[i][j]=event.index;
    }
    addNewRuleObject(i) {
        let newRule = new ApprovalRules();
        if(!this.ruleGroupService.approvalRuleGRoupWithRules.rules) {
            let  rules : ApprovalRules[]=[];
        this.ruleGroupService.approvalRuleGRoupWithRules.rules=rules;
        }
        newRule.priority = i + 1;
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i]=(newRule);
       // this.setFormsForAutocompletes(i);
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions=[];
        this.approvalTypeLov[i]=[];
        this.approvalType.forEach((appType) => {
            let appTypeObj = { "code": appType.meaning, "name": appType.description, "setValue": appType.lookUpCode, "value": false };
            this.approvalTypeLov[i].push(appTypeObj);
        });
      
       // console.log('this.usersList'+this.usersList);
       // console.log('this.groupsList'+this.groupsList);
        if((!this.usersList || this.usersList.length ==0 ) && (this.groupsList.length==0)) {
            let data = {
                message:'oops!! No approvers found',
                ok:'ok',
                routerToNavigate:''
            }
            var dialogRef = this.dialog.open(ApprovalsConfirmActionModalDialog, {
                data, disableClose: true
            });
            dialogRef.afterClosed().subscribe((result) => {
                if (result && result == 'ok') {
                    let link: any = '';
                    link = ['/rule-group', {outlets: {'content': ['rule-group-home']}}];
                    this.router.navigate(link);
                }
            });
        }   else{
            this.addNewAction(i);
        let seq : any = {
            "id": i + 1,
            "selected": false
        };
        this.sequences.push(seq);
        }
        this.addNewCondition(i, 0);
       this. collapseAllOpenFirst(this.ruleGroupService.approvalRuleGRoupWithRules.rules.length-1);
        this.ngxScrollToLastRule = 'ruleScroll_'+(  this.ruleGroupService.approvalRuleGRoupWithRules.rules.length-1);
        
    }
    collapseAllOpenFirst(ind) {
        for(let i=0;i< this.ruleGroupService.approvalRuleGRoupWithRules.rules.length;i++) {
            if(!this.openRuleTab){
                this.openRuleTab=[];
            }
          
            this.openRuleTab[i] =false;
        } 
        this.openRuleTab[ind] = true;
        
    }
    addNewCondition(i,j){
        let condition = new ApprovalConditions();
        condition.operators =[];
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions[j] = condition;
    }
    addNewAction(i)  {
        let approvalActions = new ApprovalActions();
        let actionDetails:ApprovalActionDetails[];
        let actionDetail = new ApprovalActionDetails();
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions=approvalActions;
        //this.userListLOV[i] = jQuery.extend(true, [], this.usersList);
    }
  
    saveToSession() {
        let ind = 1; 
        this.ruleGroupService.approvalRuleGRoupWithRules.rules.forEach((rule)=>{
            rule.priority = ind;
            ind = ind +1 ;
        });
        //this.$sessionStorage.store('approvalsRuleList', this.ruleGroupService.approvalRuleGRoupWithRules);
        if(this.ruleGroupService.approvalRuleGRoupWithRules.rules.length<=0){
            this.commonService.error(
                    'Warning!',
                    'Tag atleast one rule'
                    )
            }  else  {
            this.commonService.success(
                    '',
                    this.ruleGroupService.approvalRuleGRoupWithRules.rules.length + ' rules tagged successfully!'
                )
            }
        //approvalsRuleList
    }
    changeSourceDataView(dvName, i) {
        
    }
    
    fetchSrcColumns( viewId: any, indexVal: any,mode?:string ) {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
      //  console.log('fetched source cols are'+JSON.stringify(viewId.id));
      this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewName= viewId.itemName;
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewId=viewId.id;
        this.dataViewsService.getDataViewById( viewId .id).subscribe(( res: any ) => {
            const dvAndCols = res;
            if ( dvAndCols  ){
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].columnLOV = [];
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].columnLOV = ( dvAndCols );
            }
           
           
         //   this.operators[indexVal] = [];
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].approvalActions.assigneeType='user';
            if(this.approvalTypeLov[indexVal] ){ 
                this.approvalTypeLov[indexVal][0].value = true;
            }
          
            if(this.isCreateNew && !mode){
                this.addNewActionDetails(indexVal);
            }
            
            if( mode && mode === 'populate'){
                for(let j=0;j<this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].approvalConditions.length;j++)   {
                    this.SelectColumn(indexVal,j,this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].approvalConditions[j].columnId);
                 }
            }
           

        });
    }
        // if(value && value.data  && value.data.id)
        // {
        //     console.log('insideee');
        //     this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewId=value.data.id;
        //     if(value.data.dataViewDispName)
        //     this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewName=value.data.dataViewDispName;
        //     this.dataViewsService.getDataViewById( value.data.id ).subscribe(( res: any ) => {
        //         let dvAndCols = res;
        //         if ( dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList )
        //             this.columnLOV[indexVal] = [];
        //         this.columnLOV[indexVal] = ( dvAndCols[0].dvColumnsList );
        //         this.operators[indexVal] = [];
        //     } );
        //     }
        // else if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewId)
        //     {
        //     console.log('11insideee'+this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewId);
        //     this.dataViewsService.getDataViewById( this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].sourceDataViewId).subscribe(( res: any ) => {
        //         let dvAndCols = res;
        //         console.log('dvAndColsdvAndColsdvAndCols'+JSON.stringify(dvAndCols));
        //         if ( dvAndCols && dvAndCols[0] && dvAndCols[0].dvColumnsList )
        //             this.columnLOV[indexVal] = [];
        //         this.columnLOV[indexVal] = ( dvAndCols[0].dvColumnsList );
        //         this.operators[indexVal] = [];
        //         let condIndex : number = 0;
        //         this.ruleGroupService.approvalRuleGRoupWithRules.rules[indexVal].approvalConditions.forEach(condition=>{
        //        this.SelectColumn(indexVal,condIndex,condition.columnId);
        //         condIndex = condIndex + 1;
                
        //     });
        //         console.log('data view colsssss'+JSON.stringify(this.columnLOV[indexVal]));
        //     } );
        //     }
    //}
    SelectColumn( i: any, childIndex: any, id: any ) {
        console.log('select column called');
        //this.operators[i][childIndex] =[];
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions[childIndex].operators=[];
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions[childIndex].columnId = id;
        for ( let val = 0; val < this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].columnLOV.length; val++ ) {
            if ( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].columnLOV[val].id == id ) {
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions[childIndex].column = this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].columnLOV[val].columnName;

                if ( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].columnLOV[val].colDataType ) {
                    this.lookUpCodeService.fetchLookUpsByLookUpType(  this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].columnLOV[val].colDataType  ).subscribe(( res: any ) => {
                        const operatorss = res;
                       // if(!this.operators[i])
                         //   this.operators[i]=[];
                        //if(!this.operators[i][childIndex])
                       // this.operators[i][childIndex] = operatorss;
                       this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions[childIndex].operators=operatorss;
                    } );
                }


            }
        }
    }
    SelectOperator(){
        
    }
    addNewActionDetails(i: any){
      
        //console.log('change event called with'+ this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.assigneeType );
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalNeededType='';
       // this.groupsList=[];
        //this.groupMembers=[];
       // this.usersList=[];
        if ( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.assigneeType == 'user' ) {
            //fetch users
            // this.notificationBatchService.usersListBasedOnRoleTask('IS_APPROVAL').subscribe(
            //     (res) => {
            //         this.usersList = res;
            //         this.usersList.forEach(user => {
            //             user['itemName'] = user['firstName']+' ' + user['lastName'];
            //         });
                   // console.log('this.usersList ' + JSON.stringify(this.usersList));
                    if(!this.usersList || this.usersList.length<= 0 || this.usersList == [])  {
                      //  console.log('no users list found');
                        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails=[];
                    } else {
                     this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails=[];
                     //this.setFormForUser(i,0);
                     const actiondDet=new ApprovalActionDetails();
                     this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails.push(actiondDet); 
                    // this.createFormsAndDisplay();   
                    }
                    
            //});
        }  else if ( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.assigneeType == 'group' ) {
            //fetch groups and group details
            // this.approvalGroupsService.approvalgroupsForTenant().subscribe((res:any)=>{
            //     this.groupsList=res;
              //  console.log('  this.groupsList '+JSON.stringify(this.groupsList));
                if( this.groupsList &&  this.groupsList.length>0) {
                    this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails=[];
                    for(let ind=0;ind<this.groupsList.length;ind++){
                    const actiondDet=new ApprovalActionDetails();
                    
                    this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails.push(actiondDet);
                    
                    }
                  //  console.log(' this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails'+JSON.stringify( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails));
                } 
                
           // });
        }
    }
    setUser(value,i,actionDetailsIndex)  {
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
      // console.log('set user for'+ this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[actionDetailsIndex].assigneeName );
        if ( value && value.id )  {
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[actionDetailsIndex].assigneeName  = value.login;
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[actionDetailsIndex].assigneeId =  value.id;
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[actionDetailsIndex].notification=true;
            }
    }
    setRuleObject(i, value) {
        //  console.log('set rule with values as'+JSON.stringify(value));
        if (value) {
            this.rulesService.getApprovalRules(value).subscribe((res: any) => {
                const fetchedRule = res;
                const rule = fetchedRule[0];
               // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].ruleCode = rule.ruleCode;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].startDate = rule.startDate;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate = rule.endDate;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].sourceDataViewId = rule.sourceDataViewId;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].startDate =
                this.dateUtils.convertDateTimeFromServer( rule.startDate );
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate =
            this.dateUtils.convertDateTimeFromServer( rule.endDate );
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i]. sourceDVId=[];
                const obj ={
                    "id":rule.sourceDataViewId,
                    "itemName":rule.sourceDataViewName
                };
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i]. sourceDVId.push(obj);
                this.fetchSrcColumns(obj,i,'populate');
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].sourceDataViewName = rule.sourceDataViewName;
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions= rule.approvalConditions;
                for(let j=0;j<this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions.length;j++)   {
                    this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions[j].id=null;
                }
                
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions = rule.approvalActions;
                if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.assigneeType && this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.assigneeType === 'user'){
                    for(let c=0;c<this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails.length;c++){
                        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[c].id=null;
                        const user = {
                            "id": this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[c].assigneeId,
                            "itemName":this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[c].assigneeName
                        }
                        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[c].approverId=[];
                        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[c].approverId.push(user);
                    }
                } else{

                }
               
                
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalNeededType = rule.approvalNeededType;
             
            });
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].ruleCode = value.data.ruleCode;
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].id = value.data.id;
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].sourceDataViewId = 
            //     value.data.sourceDataViewId;
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].sourceDataViewName= 
            //     value.data.sourceDataViewName;
            
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalNeededTypeMeaning=
            //     value.data.approvalNeededTypeMeaning;
             
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions = 
            //     value.data.approvalConditions;
            // this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions=
            //     value.data.approvalActions;
        }else {
            console.log('in else set rule');
            //this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].ruleCode = '';
        }
    }
    getGroupMembers(groupId : any) {
        this.approvalGroupsService.getGroupMembers(groupId).subscribe((res:any)=>{
        this.groupMembers=res;
       // console.log('this.groupMembersthis.groupMembers'+JSON.stringify(this.groupMembers));
        });
    }
    deleteRuleObject( i ) {

        this.ruleGroupService.approvalRuleGRoupWithRules.rules.splice( i, 1 );
        this.ruleListformArray.splice(i,1);
        if ( this.ruleGroupService.approvalRuleGRoupWithRules.rules.length == 0 ) {
            this.addNewRuleObject( 0);
          
        }
    }
    clearRuleObject( i ) {
    
    }
    selectPriority(i)  {
        this.sequences[i].selected=true;
        
    }
    addNewUser(i,actionDetailsIndex) {
        
        const approvalActionDetails = new ApprovalActionDetails();
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails[actionDetailsIndex] = approvalActionDetails;
        this.setFormForUser(i,actionDetailsIndex);
    }
    deleteRuleCondition(i,j)   {
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions.splice(j,1);
        if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalConditions.length == 0){
            this.addNewCondition(i,j);
        }
      
    }
    selectAppType(appType, i) {
        
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalNeededType   = appType.setValue;
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalNeededTypeMeaning  = appType.code;

        let alreadySelected =0;
        this.approvalTypeLov[i].forEach((appType1) => {
            if(appType1.value == true)  {
                alreadySelected = 1;
            }
         
        });
        this.approvalTypeLov[i].forEach((apprType1 )=> {

            if (appType.setValue == apprType1.setValue) {
                //ruleType.value = ruleType.value ? false : true;
                if(apprType1.value == true)  {


                } else{
                    apprType1.value=true;
                    
                }
            }  else  {
                
                apprType1.value = false;
            }
                
        });
    }
    deleteApprover(i,j) {
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails.splice(j,1);
        if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].approvalActions.actionDetails.length==0){
            this.addNewUser(i,0);
        }
       
    }
    startDateChanged(dt:Date,i){
        if( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate){
            if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate<this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].startDate){
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate=this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].startDate;
            }
        }
    }
    // endDateChanged(i)
    // {
    //     if(this.isCreateNew)
    //     {
    //         if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDateChange){
    //             this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate=new Date(this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].endDate.getTime() + 86400000);
    //         }
    //     }
    // }
    copyAndCreateNewRule(copyToIndex,copyFromIndex) {
        this.addNewRuleObject(copyToIndex);
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex]= jQuery.extend( true, {},this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyFromIndex]);
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].ruleCode=null;
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].id=null;
        this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].ruleGroupAssignId=null;
        for(let j =0;j<this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].approvalActions.actionDetails.length;j++)   {
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].approvalActions.actionDetails[j].id =null;
        }
        for(let j =0;j<this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].approvalConditions.length;j++) {
            this.ruleGroupService.approvalRuleGRoupWithRules.rules[copyToIndex].approvalConditions[j].id =null;
        }
    }
    ruleDuplicationCheck(i) {
        this.ruleGroupService.ruleDuplicationCheck( this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].ruleCode,this.ruleGroupService.ruleGroup.rulePurpose).subscribe((res: any) => {
            const resp = res;
            if(resp  && (resp._body == true || resp._body == 'true')) {
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].duplicateRuleName = true;
            }  else  {
                this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].duplicateRuleName = false;
            }
            this.checkduplicate(i);
        });

    }
    checkduplicate(i){
        for(let c =0;c<this.ruleGroupService.approvalRuleGRoupWithRules.rules.length;c++)  {
            if(c != i)  {
                if(this.ruleGroupService.approvalRuleGRoupWithRules.rules[c].ruleCode.toLowerCase() === this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].ruleCode.toLowerCase() ){
                    this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].duplicateRuleName = true;
                }else{
                    this.ruleGroupService.approvalRuleGRoupWithRules.rules[i].duplicateRuleName = false;
                }

            }
        }
    }
    fetchExistingRules()  {
    this.rulesService.getRules(this.ruleGroupService.ruleGroup.rulePurpose,null).subscribe((res: any) => {
        let rulesFetched: Rules[] = [];
        rulesFetched = res;
        for(let c=0;c<rulesFetched.length;c++){
            rulesFetched[c]['itemName'] = rulesFetched[c].ruleCode;
        }
        this.ruleGroupService.approvalRuleGRoupWithRules.existingRuleListLOV=rulesFetched;
    });
    }
    getExistingRuleDetailsAndCopy(ruleId: any,indexVal: any)   {
        console.log('index has'+indexVal+' id is '+JSON.stringify(ruleId));
        this.ruleGroupService.approvalRuleGRoupWithRules.selectedExistingRule = ruleId;
        this.setRuleObject(indexVal,ruleId.id);
       
        $('.cuppaSingleSelection .cuppa-dropdown  .dropdown-list').attr('hidden',"true");
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
