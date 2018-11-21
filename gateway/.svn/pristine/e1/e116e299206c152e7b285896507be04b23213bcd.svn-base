import { RuleGroupService } from "../rule-group";

export class RuleConditions {
    constructor(
        public ruleGroupService ?: RuleGroupService,
        public id?: number,
        public sequenceNo?:number,
        public ruleId?: number,
        public openBracket?: string,
        public dataType ?: string,
        public sColumnType?: string,
        public sColumnId?: number,
        public sColumnName?: string,
        public sFormula?: string,
        public isSFormula?: string,
        public sFormulaExpressionExample ?: string,
        public sOperator?: string,
        public sValue?: string,
        public valueType?: string,
        public operator?: string,
        public value?: string,
        public sToleranceType?: string,
        public sToleranceTypeMeaning?: string,
        public tToleranceTypeMeaning?: string,
        public sToleranceOperator?: string,
        public sToleranceValue?: string,
        public tOperator?: string,
        public tValue?: string,
        public tDataType ?: string,
        public sMany?: boolean,
        public sToleranceValueFrom ?: string,
        public sColumnList?: any,
        public sfilterSave ?: boolean ,
        public sformulaSave ?:boolean ,
        public stoleranceSave ?: boolean ,
        public sToleranceValueTo ?: string,
        public sToleranceOperatorTo ?: string,
        public sToleranceOperatorFrom ?: string,
        public aggregationMethod?: string,
        public amountQualifier?: boolean,
        public tColumnType?: string,
        public tColumnId?: number,
        public tColumnName?:string,
        public tFormula?: string,
        public isTFormula?: string,
        public tFormulaExpressionExample ?: string,
        public tMany?: boolean,
        public tToleranceType?: string,
        public tToleranceOperator?: string,
        public tToleranceValue?: string,
        public closeBracket?: string,
        public logicalOperator?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public tfilterSave ?: boolean ,
        public tformulaSave ?: boolean ,
        public ttoleranceSave ?: boolean ,
        public tToleranceValueTo ?: string,
        public tToleranceValueFrom ?: string,
        public tToleranceOperatorTo ?: string,
        public tToleranceOperatorFrom ?: string,
        public tColumnList?: any,
        public operatorBasedOnColumnLOV ?: any,
        public logicalOpLOVArray ?: any,
        public tPercentile ?: boolean,
        public sPercentile ?: boolean,
        public tReconAmountMatch?: boolean,
        public sReconAmountMatch?: boolean
        
    ) {
        
            if(this.ruleGroupService && this.ruleGroupService.reconcileRuleJSON)
            {
                this.sColumnName =  this.ruleGroupService.reconcileRuleJSON.sColumnName;
                this.sOperator =  this.ruleGroupService.reconcileRuleJSON.sOperator;
                this.sValue =  this.ruleGroupService.reconcileRuleJSON.sValue;
                this.sFormula =  this.ruleGroupService.reconcileRuleJSON.sFormula;
                this.sToleranceValueFrom =  this.ruleGroupService.reconcileRuleJSON.sToleranceValueFrom;
                this.sToleranceValueTo =  this.ruleGroupService.reconcileRuleJSON.sToleranceValueTo;
                this.operator =  this.ruleGroupService.reconcileRuleJSON.operator;
                this.tColumnName = this.ruleGroupService.reconcileRuleJSON.tColumnName;
                this.tOperator =  this.ruleGroupService.reconcileRuleJSON.tOperator;
                this.tValue =  this.ruleGroupService.reconcileRuleJSON.tValue;
                this.tFormula=  this.ruleGroupService.reconcileRuleJSON.tFormula;
                this.tToleranceValueFrom =  this.ruleGroupService.reconcileRuleJSON.tToleranceValueFrom;
                this.tToleranceValueTo =   this.ruleGroupService.reconcileRuleJSON.tToleranceValueTo;

                this.logicalOperator = this.ruleGroupService.reconcileRuleJSON.logicalOperator;
                this.ruleGroupService=null;
            }
            else{

        this.id = 0,
        this.sequenceNo=0,
        this. ruleId=0,
        this. openBracket='',
        this.sOperator ='',
        this.sValue='',
        this.tOperator='',
        this.tValue='',
        this. sColumnType='',
        this. sColumnId=null,
        this. sColumnName='',
        this. sFormula='',
        this. valueType='',
        this. operator='',
        this. value='',
        this. sToleranceType='',
        this. sToleranceOperator='',
        this. sToleranceValue='',
        this. sMany=false,
        this. aggregationMethod='',
      
        this. tColumnType='',
        this. tColumnId=null,
        this. tColumnName='',
        this. tFormula='',
        this. tMany=false,
        this. tToleranceType='',
        this.sToleranceTypeMeaning='';
        this.tToleranceTypeMeaning='';
        this. tToleranceOperator='',
        this. tToleranceValue='',
        this. closeBracket='',
        this. logicalOperator='',
        this. createdBy=0,
        this. lastUpdatedBy=0,
        this.isSFormula = '',
        this.isTFormula = '',
        this.sFormulaExpressionExample = '',
        this.tFormulaExpressionExample = '',
        this.sfilterSave=false,
            this.sformulaSave=false,
            this.stoleranceSave =false,
            this.tfilterSave=false,
            this.tformulaSave=false,
            this.ttoleranceSave =false,
            this.sToleranceValueFrom = '',
            this.tToleranceValueFrom = '',
            this.sToleranceValueTo= '',
            this.tToleranceValueTo = '',
            this.tColumnList = [],
            this.sColumnList = [],
            this.sToleranceOperatorTo='+',
            this.sToleranceOperatorFrom ='+',
            this.tToleranceOperatorTo='+',
            this.tToleranceOperatorFrom='+',
            this.tPercentile=false;
            this.sPercentile=false;
                this.amountQualifier=false;
        }
          
        
    }
}

export const ReportsBreadCrumbTitles = {
    reports : 'Run Reports'
};
