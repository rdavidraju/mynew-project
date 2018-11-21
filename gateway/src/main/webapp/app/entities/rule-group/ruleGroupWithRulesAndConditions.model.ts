import {RulesAndConditions} from '../rules/rulesWithConditions.model';
import { Rules } from '../rules/rules.model';
import { RuleGroupService } from '.';
export class RuleGroupWithRulesAndConditions {
    constructor(
        public ruleGroupService?:RuleGroupService,
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public ruleGroupType?: any,
        public rulePurpose?: string,
        public adhocRuleCreation ?:boolean,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public enableFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public dataViewstoColumnsMap ?:any,
        public rules ?: RulesAndConditions[],
        public existingRuleListLOV ?: Rules[],
        public selectedExistingRule?:any
    ) {
        
    
        this.adhocRuleCreation=false;
       
        if(this.ruleGroupService && this.ruleGroupService.reconcileRuleJSON)
        {
            this.name =  this.ruleGroupService.reconcileRuleJSON.name;
            let rule = new RulesAndConditions(this.ruleGroupService);
            this.rules = [];
            this.rules.push(rule);
            this.ruleGroupService = null;
        
        }
        else{
            this.selectedExistingRule = null;
            this.startDate = new Date();
            this.enabledFlag = false;
        }
    }
}
