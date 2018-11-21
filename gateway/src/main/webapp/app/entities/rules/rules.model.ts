import { FormGroup, FormBuilder, FormArray, Validators ,FormControl} from '@angular/forms';
import { RuleGroupService } from '../rule-group';
export class Rules {
    constructor( 
        public ruleGroupService ?: RuleGroupService,
        public formCntrl?: FormControl,
        public id?: number,
        public duplicateRuleName ?: boolean,
        public today ?: Date,
        public tenantId?: number,
        public editRule ?: boolean,
        public selectedruleId?:number,
        public ruleCode?: string,
        public ruleName?: string,
        public priority?: number,        
        public sourceDataViewId?: number,
        public sourceDVId?: any,
        public sDataViewName?: string,
        public sDataViewDisplayName?: string,
        public targetDataViewId?: number,
        public targetDVId?: any,
        public tDataViewName?: string,
        public tDataViewDisplayName?: string,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public ruleType?: string,
        public ruleTypeMeaning?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public setRuleObject?: boolean,
        public selectedRuleCode?: string,
        public assignmentFlag ?: boolean,
        public suggestionFlag ?: boolean,
        public newRule ?: boolean,
        public startDateChange ?:boolean,
        public endDateChange ?: boolean, 
        public ruleGroupAssignId?:number,
        public sourceColumnLOV ?: any ,
        public targetColumnLOV ?: any,
        public ruleCategory ?: string,
        public copiedRefId ?: any,
        public  ruletypeLOVArray ?: any
       // public ruleListformArray?: FormGroup,
     
       
    ) {
       
       
           
            this.suggestionFlag = false;
                     this. ruleCode='',
                     this. ruleType='',
                     this. ruleTypeMeaning='',
                     this. createdBy=0,
                     this. lastUpdatedBy=0,
                     this. sourceDataViewId=0,
                     this. sDataViewName='',
                     this. sDataViewDisplayName='';
                     this. targetDataViewId=0;
                     this. tDataViewName='';
                     this. tDataViewDisplayName='';
                     this.priority=0;
                     this.ruletypeLOVArray=[];
                    
                    
                   
                   //  this.ruleListformArray = new FormGroup({});
        if(this.ruleGroupService && this.ruleGroupService.reconcileRuleJSON)
        {
            this.ruleCode =  this.ruleGroupService.reconcileRuleJSON.ruleCode;
            this.ruleType =  this.ruleGroupService.reconcileRuleJSON.ruleType;
            this.sDataViewDisplayName =  this.ruleGroupService.reconcileRuleJSON.sDataViewDisplayName;
            this.tDataViewDisplayName =  this.ruleGroupService.reconcileRuleJSON.tDataViewDisplayName;
            this.suggestionFlag =  Boolean(this.ruleGroupService.reconcileRuleJSON.suggestionFlag);
            this.ruleGroupService=null;
        }
        else{
            this.formCntrl = new FormControl('');
            this.today = new Date();;
            this. ruleName='';
            this.startDate = new Date();
            this.sourceColumnLOV=[];
            this.targetColumnLOV=[];
            this.editRule=true;
            this.selectedRuleCode =''; 
            this.startDateChange =false;
            this.endDateChange=false;
            this.enabledFlag = false;
            this.ruleCategory =  'new';
        }
      
    }
}
