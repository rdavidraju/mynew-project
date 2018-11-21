import { BaseEntity } from './../../shared';

export class FxRates implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public name?: string,
        public description?: string,
        public conversionType?: string,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public fxRatesDetails?: any
    ) {
        this.fxRatesDetails = [];
    }
}

export const FxRatesBreadCrumbTitles = {
    fxRatesList: 'FX Rates',
    fxRatesDetails: 'FX Rates Details',
    fxRatesNew: 'Create New FX Rates',
    fxRatesEdit: 'Edit FX Rates',
}