import { BaseEntity } from './../../shared';

export class ApprovalGroups implements BaseEntity {
    constructor(
        public id?: number,
        public groupName?: string,
        public type?: any,
        public tenantId?: number,
        public description?: string,
        public status?: string,
        public startDate?: any,
        public endDate?: any,
        public createdDate?: any,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public approvalsGroupMemList?: any
    ) {
        this.approvalsGroupMemList = [];
    }
}

export const ApprovalGroupsBreadCrumbTitles = {
    approvalGroupsList: 'Groups',
    approvalGroupsDetails: 'Group Details',
    approvalGroupsNew: 'Create New Group',
    approvalGroupsEdit: 'Edit Group',
}