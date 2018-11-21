import { BaseEntity } from './../../shared';

export class LookUpCode implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public lookUpCode?: string,
        public lookUpType?: string,
        public meaning?: string,
        public description?: string,
        public enableFlag?: boolean,
        public activeStartDate?: any,
        public activeEndDate?: any,
        public secureAttribute1?: string,
        public secureAttribute2?: string,
        public secureAttribute3?: string,
        public createdBy?: number,
        public creationDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
    ) {
        this.enableFlag = false;
    }
}

export const LookupCodesBreadCrumbTitles = {
    lookUpCodeList: 'Lookup Code',
    lookUpCodeDetails: 'Lookup Code Details',
    lookUpCodeNew: 'Create New Lookup Code',
    lookUpCodeEdit: 'Edit Lookup Code',
}