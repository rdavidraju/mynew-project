import { BaseEntity } from './../../shared';

export class AccountingLineTypes implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public ruleId?: number,
        public lineType?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
    ) {
    }
}
