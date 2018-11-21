import { BaseEntity } from './../../shared';

export class AcctRuleDerivations implements BaseEntity {
    constructor(
        public id?: number,
        public acctRuleActionId?: number,
        public dataViewColumn?: string,
        public accountingReferences?: string,
        public constantValue?: string,
        public mappingSetId?: number,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public createdDate?: any,
        public lastUpdatedDate?: any,
    ) {
    }
}
