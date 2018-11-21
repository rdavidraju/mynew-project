import { BaseEntity } from './../../shared';

export class LedgerDefinition implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public description?: string,
        public ledgerType?: string,
        public startDate?: any,
        public endDate?: any,
        public currency?: string,
        public calendarId?: number,
        public status?: string,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public coaId?: any,
    ) {
        this.enabledFlag = false;
    }
}

export const LedgerDefinitionBreadCrumbTitles = {
    ledgerDefinitionList: 'Ledger Definition',
    ledgerDefinitionDetails: 'Ledger Definition Details',
    ledgerDefinitionNew: 'Create New Ledger Definition',
    ledgerDefinitionEdit: 'Edit Ledger Definition',
}