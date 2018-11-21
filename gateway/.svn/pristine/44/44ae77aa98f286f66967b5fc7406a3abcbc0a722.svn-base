import { BaseEntity } from './../../shared';

export class ChartOfAccount implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public startDate?: any,
        public endDate?: any,
        public segmentSeperator?: string,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public segments?: any,
        public tenantId?: any,
    ) {
        // this.enabledFlag = false;
        this.segments = [];
    }
}

export const ChartOfAccountBreadCrumbTitles = {
    chartOfAccountList: 'Chart Of Account',
    chartOfAccountDetails: 'Chart Of Account Details',
    chartOfAccountNew: 'Create New Chart Of Account',
    chartOfAccountEdit: 'Edit Chart Of Account',
}