export class AccountingRuleConditions {
    constructor(
       
        public formula?:string, 
        public sViewColumnId?: number,
        public sViewColumnName?: string,
        public dataType ?: string,
        public sequence?: number,
        public operator?: string,
        public operatorMeaning?:string,
        public value ?: string,
        public logicalOperator ?: string ,
        public func ?: any,
        public selectedFunctionName ?: any,
        public functionExample ?: any
    ) {
        this.sViewColumnId = null ;
        this.sViewColumnName =null;
        this.dataType =null;
        this.formula = null ;
        this.operator =null;
        this.value = null ;
        this.sequence =null;
        this.logicalOperator = null ;
        this.func =null;
        this.selectedFunctionName = null ;
        this.functionExample =null;
        this.operatorMeaning=null;
        
    }
}
