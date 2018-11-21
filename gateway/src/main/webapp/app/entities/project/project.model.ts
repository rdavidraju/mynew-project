import { BaseEntity } from './../../shared';

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public projectName?: string,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
    ) {
        this.enabledFlag = false;
    }
}
