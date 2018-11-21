import {RulesAndConditions} from '../rules/rulesWithConditions.model';
export class RuleGroupWithRulesAndConditions {
    constructor(
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
        public rules ?: RulesAndConditions[]
    ) {
        this.enabledFlag = false;
        this.startDate = new Date();
    }
}
