export class LineDerivations {
    constructor(
        public criteria ?: string,
        public criteriaMeaning ?: string,
        public operator ?: string,
        public operatorMeaning ?: string,
        public value ?: string,
        public id?: number,
        public dataViewColumn ?: number,
        public dataType ?: string,
        public operatorsLOV ?: any,
      
        public dataViewColumnName ?: string,
        public accountingReferencesCode?: string, //use for both line type in header and acc ref in line level
        public accountingReferencesMeaning?: string,
        public constantValue?: string,
        public mappingSetId?: number,
        public mappingSetName?: string,
        public segValue ?: any,
        public segValueMeaning ?: string,
        public ruleId?:any,
        public type?:any,
        public valueSet ?: any,
        public operators ?: any
    ) {
        this.criteria= null;
        this.criteriaMeaning =null;
        this.operator =null;
        this.operatorMeaning =null;
        this.value= null;
        this.dataViewColumnName =null;
        this.constantValue= null;
        this.mappingSetId =null;
        this.mappingSetId =null; 
        this.segValue =null;        
        this.dataViewColumn=null;
        this.dataType=null;
        this.accountingReferencesCode=null;
        this.accountingReferencesMeaning =null;
        this.ruleId=null;
        this.type=null;
        this.segValueMeaning =null;
        this.valueSet = [];
        this.operators=[];
    
        this.operatorsLOV=[];
    }
}
