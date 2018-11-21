import {RuleConditions} from '../rule-conditions/rule-conditions.model';
import {Rules} from '../rules/rules.model';
import { RuleGroupService } from '../rule-group';
export class RulesAndConditions {
    constructor(
        public ruleGroupService ?: RuleGroupService,
        public selectedRule ?: any,
        public id?:any,
        public itemName?:any,
        public openRuleTab ?: boolean,
        public rule ?:Rules,
        public hasAmountQualifier ?: boolean,
        public hideRule ?: boolean,
        public sourceDataViewsAndColumns?: any,
        public targetDataViewsAndColumns?: any,
        public ruleConditions?:RuleConditions[]
    ) {
       

        if(this.ruleGroupService && this.ruleGroupService.reconcileRuleJSON) {
            this.rule =new Rules(this.ruleGroupService); 
            const ruleConditions = new RuleConditions(this.ruleGroupService);
            this.ruleConditions = [];
            this.sourceDataViewsAndColumns=[];
            this.targetDataViewsAndColumns=[];
            this.ruleConditions.push(ruleConditions);
            this.ruleGroupService= null;
        } else{
        this.hideRule=false;
        }
    }
}
