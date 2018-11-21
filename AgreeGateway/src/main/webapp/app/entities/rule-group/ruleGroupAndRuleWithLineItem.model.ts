import { AccRule } from './accRule.model';
export class RuleGroupAndRuleWithLineItem {
    constructor(
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public rulePurpose?: any,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public rules ?: AccRule[]
        
    ) {
        this.startDate = new Date();
    } 
}

import { AccountingRule } from './accRule.model';
export class AccountingRuleGroup {
    constructor(
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public rulePurpose?: any,
        public accountingType ?: string,
        public startDate?: any,
        public endDate?: any,
        public enableFlag?: boolean,
                public accountingTypeCode ?: string,
                        public accountingTypeMeaning ?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public rules ?: AccountingRule[],
        public reconciliationGroupId?:any,
        public apprRuleGrpId ?: any,
        public reconciliationGroupName?:any,
        public activityBased?: boolean,
        public multiCurrency?: boolean,
        public crossCurrency?: boolean,
        public fxRateId?:any,
        public conversionDate?:any,
        public controlAccount ?: any,
        public realizedGainLossAccount?:any,
        public fxGainAccount?:any,
        public fxLossAccount?:any
        
    ) {
        this.id = null;
        this.tenantId = null;
        this.name = null;
        this.rulePurpose =null;
        this.accountingType = null;
        this.accountingType =null;
        this.startDate = new Date();
        this.endDate =null;
        this.enableFlag =null;
        this.createdBy =null;
        this.lastUpdatedBy =null;
        this.creationDate =null;
        this.lastUpdatedDate = null;
        this.rules = [];
         this.activityBased = false;
        this.multiCurrency = true;
        this.crossCurrency=false;
        this.reconciliationGroupId=null;
        this.reconciliationGroupName =null;
        //this.rules .push(new AccountingRule());
    } 
}