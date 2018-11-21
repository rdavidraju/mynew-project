import { BaseEntity } from './../../shared';

export class Calendar implements BaseEntity {
    constructor(
        public id?: any,
        public tenantId?: number,
        public name?: string,
        public description?: string,
        public calendarType?: string,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public periodList?: any,
        public period?: any
    ) {
        this.periodList = [];
    }
}

export const CalendarBreadCrumbTitles = {
    calendarList: 'Calendar',
    calendarDetails: 'Calendar Details',
    calendarNew: 'Create New Calendar',
    calendarEdit: 'Edit Calendar',
}