import { BaseEntity } from './../../shared';

export class TemplateDetails implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public targetAppSource?: string,
        public templateName?: string,
        public description?: string,
        public viewId?: number,
        public viewName?: string,
        public startDate?: any,
        public endDate?: any,
        public enable?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public createdDate?: any,
        public lastUpdatedDate?: any,
    ) {
        this.enable = false;
    } 
}
export class JournalsHeaderData implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public jeTempId?: number,
        public jeBatchName?: string, /* Journal Batch Name */
        public description?: any,
        public jeBatchDate?: any,
        public glDate?: any,    /* GL Date */
        public currency?: string,   /* Currency */
        public conversionType?: string, /* Conversion Type */
        public category?: string, /* Category */
        public source?: string, /* Source */
        public ledgerName?: string, /* Ledger Name */
        public batchTotal?: number,
        public runDate?: any,
        public submittedBy?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public createdDate?: any,
        public lastUpdatedDate?: any,
        public templateName?: string,
        public viewName?: any,
        public startDate?: any,
        public endDate?: any,
        public viewId?: any,
        public period?: any, /* Period */
        public rate?: any, /* Rate */
        public summary?: boolean, /* Summary/Details */
        public journalBalance?: any, /* journalBalance */
        public jeLineDerivations?: any,
        public journalHeaders?: any,
        public selectedView?: any,
        public selectedView9?:any,
        public seq?: any,
        public jeHeaderDerivations?: any,
        public attributeName?:any ,
        public value?: any,
        public aliasName?:any,
        public valueName?: any,
    ) {
    }
}

export class TemplateDefinition implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public jeBatchName?: any,
        public targetAppSource?: string,
        public templateName?: string,
        public description?: string,
        public viewId?: number,
        public viewName?: string,
        public startDate?: any,
        public endDate?: any,
        public enable?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public createdDate?: any,
        public lastUpdatedDate?: any,
        public balanceType?: any,
        public jeLevel?: any,
        public balanceTypeDisplayName?: string,
        public jeHeaderDerivations?: JournalsHeaderData[],
        public value?:any,
        public jeLineDerivations?:any,
        public ruleGrpId?:any,
        public coaId?:number
    ) {

    } 
}

export const TemplateDetailsBreadCrumbTitles = {
        templateDetailsList: 'Journal Templates',
        templateDetailsDetails: 'Journal Template Details',
        templateDetailsNew: 'Create Journal Template',
        templateDetailsEdit: 'Journal Edit Template',
        templateDefinitionDetails: 'Journal Template Details',
        templateDefinitionNew: 'Create Journal Template',
    }
