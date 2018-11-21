import { BaseEntity } from './../../shared';

export class AcctRuleConditions implements BaseEntity {
    constructor(
        public id?: number,
        public ruleId?: number,
        public ruleActionId?: number,
        public openBracket?: string,
        public sViewColumn?: string,
        public operator?: string,
        public value?: string,
        public closeBracket?: string,
        public logicalOperator?: string,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
    ) {
    }
}
