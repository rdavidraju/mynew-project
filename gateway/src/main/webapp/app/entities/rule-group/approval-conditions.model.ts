
export class ApprovalConditions {
    constructor(
        public id?: number,
        public sequence?: number,
        public column?: string,
        public columnId?: string,
        public operator?: string,
        public value?: string,
        public logicalOperator?: string,
        public colDataType?:string,
        public operators?:any
        //
    ) {
        this.sequence=0;
        this.columnId=null;
        this.operator=null;
        this.value=null;
        this.logicalOperator=null;
        //this.operators=[];
    }
}
