import {ApprovalActionDetails} from './approval-action-details.model';
export class ApprovalActions {
    constructor(
       
        public assigneeType?: string,
        public actionDetails ?: ApprovalActionDetails[]
    ) {
       
        this.assigneeType=null;
        let actionDetail = new ApprovalActionDetails();
        this.actionDetails=[];
        this.actionDetails.push(actionDetail);
    }
}
