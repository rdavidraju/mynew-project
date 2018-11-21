export class ApprovalActionDetails {
    constructor(
        public id?: number,
                    public assigneeId?: number,
                    public approverId?: any,
                    public assigneeName?:string,
                    public email?: boolean,
                    public autoApproval?: boolean,
                    public notification?: boolean
    ) {
        
        this.assigneeId=null;
        this.assigneeName=null;
        this.email=null;
        this.autoApproval=null;
        this.notification=null;
    }
}
