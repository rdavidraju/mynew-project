
export class ApprovalConditions {
    constructor(
        public sequence?: number,
        public column?: string,
        public columnId?: string,
        public operator?: string,
        public value?: string,
        public logicalOperator?: string
        //
    ) {
        this.sequence=0;
        this.columnId=null;
        this.operator=null;
        this.value=null;
        this.logicalOperator=null;
    }
}
