export class RuleGroup {
    constructor(
        public id?: any,
        public tenantId?: number,
        public name?: string,
        public editRule ?: boolean,
        public status?: string,
        public ruleGroupType?: any,
        public rulePurpose ?: any,
        public accountingTypeCode ?: string,
        public accountingTypeMeaning ?: string,
        public apprRuleGrpId ?: any,
        public previousApprRuleGrpId?:any,
        public apprRuleGrpObj ?: any,
        public apprRuleGrpName ?:string,
        public configuredModuleIdPreviousSelection ?: string,
        public configuredModuleId ?: string,
        public configuredModuleName ?:string,
        public startDate?: any,
        public startDateChange ?:boolean,
        public endDate?: any,
        public endDateChange ?:boolean,
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
        this.editRule=true;
    }
}

export const RuleGroupBreadCrumbTitles={
    ruleGroups : 'Rule Groups',
    ruleGroupDetails: 'Rule Group Details',
    ruleGroupNew : 'Rule Group Creation ',
    ruleGroupEdit: 'Edit Rule Group'   ,
    ruleGroupCopy: 'Copy Rule Group' 
}



export class reconcileRuleJSON
{
    constructor(
        public name ?: string,
        public ruleCode ?: string,
        public ruleType ?: string,
        public suggestionFlag ?: string,
        public sDataViewDisplayName ?: string,
        public tDataViewDisplayName ?: string,
        public sColumnName ?: string,
        public sOperator ?: string,
        public sValue ?: string,
        public sFormula ?: string,
        public sToleranceValueFrom ?: string,
        public sToleranceValueTo ?: string,
        public operator ?: string,
        public tColumnName ?: string,
        public tOperator ?: string,
        public tValue ?: string,
        public tFormula ?: string,
        public tToleranceValueFrom ?: string,
        public tToleranceValueTo ?: string,
        public logicalOperator ?: string,
    )
    {


    }
}
