import { BaseEntity } from './../../shared';

export class Processes implements BaseEntity {
    constructor(
        public id?: number,
        public processName?: string,
        public desc?: string,
        public tenantId?: number,
        public startDate?: any,
        public endDate?: any,
        public enable?: boolean,
        public createdDate?: any,
        public createdBy?: number,
        public lastUpdatedDate?: any,
        public lastUpdatedBy?: number
    ) {
        this.enable = false;
    }
}

export class ProcessesDetails {
    constructor(
        public id?: number,
        public processName?: string,
        public desc?: string,
        public tenantId?: number,
        public startDate?: any,
        public endDate?: any,
        public enable?: boolean,
        public createdDate?: any,
        public createdBy?: number,
        public lastUpdatedDate?: any,
        public lastUpdatedBy?: number,
        public processDetailList?: any,
        public spListIds?: any[],
        public spList?: any,
        public recGrpList?: any,
        public actGrpList?: any,
        public actRuleGroupId?: number,
        public actRuleGroupName?: string,
        public reconcRuleGroupId?: number,
        public reconcRuleGroupName?: string,
        public calendar?:any,
        public calendarName?: string,
        public violation?:any,
    ) {
        this.spListIds = [];
    }
}

export const ProcessesBreadCrumbTitles = {
    processList: 'Process',
    processDetails: 'Process Details',
    processNew: 'Create New Process',
    processEdit: 'Edit Process',
}