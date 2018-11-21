import { ApprovalRules } from './approval-rules.model';
export class ApprovalRuleGRoupWithRules {
    constructor(
        public id?: any,
        public tenantId?: number,
        public name?: string,
        public rulePurpose?: any,
        public startDate?: any,
        public endDate?: any,
        public enableFlag?: boolean,
        public apprRuleGrpId?: any,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public existingRuleListLOV ?: any,
        public selectedExistingRule ?: any,
        public rules ?: ApprovalRules[]
        
    ) {
        this.startDate = new Date();
    } 
}