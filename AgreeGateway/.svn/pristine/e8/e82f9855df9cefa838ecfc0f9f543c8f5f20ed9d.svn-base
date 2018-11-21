export class RuleGroup {
    constructor(
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public ruleGroupType?: any,
        public rulePurpose ?: any,
        public accountingTypeCode ?: string,
        public accountingTypeMeaning ?: string,
        public apprRuleGrpId ?: any,
        public apprRuleGrpName ?:string,
        public configuredModuleId ?: string,
        public configuredModuleName ?:string,
        public startDate?: any,
        public endDate?: any,
        public enableFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public purpose?: any,
        public reconciliationGroupId?:any,
        public reconciliationGroupName?:any,
        public activityBased?: boolean,
        public multiCurrency?: boolean,
        public crossCurrency?: boolean,
        public fxRateId?:any,
        public conversionDate?:any,
        public controlAccount ?: any,
        public realizedGainLossAccount?:any,
        public fxGainAccount?:any,
        public fxLossAccount?:any,
        public controlAccountValue ?: any,
        public realizedGainLossAccountValue ?:any,
        public fxGainAccountValue ?:any,
        public fxLossAccountValue ?:any,
        public coa ?: any,
        public coaMeaning ?: any
        
    ) {
        this.rulePurpose='';
        this.enableFlag = false;
        this.apprRuleGrpId=null;
        this.configuredModuleId=null;
        this.configuredModuleName=null;
        this.activityBased = false;
        this.multiCurrency = true;
        this.crossCurrency=false;
        this.reconciliationGroupId=null;
        this.reconciliationGroupName =null;
        this.startDate = new Date();
    }
}

export const RuleGroupBreadCrumbTitles={
    ruleGroups : 'Rule Groups',
    ruleGroupDetails: 'Rule Group Details',
    ruleGroupNew : 'Rule Group Creation ',
    ruleGroupEdit: 'Edit Rule Group'    
}