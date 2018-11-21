import { BaseEntity } from './../../shared';

export class NotificationBatch implements BaseEntity {
    constructor(
        public id?: number,
        public notificationName?: string,
        public currentApprover?: number,
        public parentBatch?: number,
        public refLevel?: number,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public notificationDate?: string,
        public dueDate?: any,
        public ruleGroupName?: any,
        public ruleName?: any,
        public dataViewName?: any,
        public status?: any,
        public InProcess?: any,
        public total?: any,
        public Approved?: any,
        public Rejected?: any,
    ) {
    }
}

export const NotificationBreadCrumbTitles = {
    notificationList: 'Notification List',
    notificationDetails: 'Notification Details',
    notificationNew: 'Create New Notification',
    notificationEdit: 'Edit Notification',
}