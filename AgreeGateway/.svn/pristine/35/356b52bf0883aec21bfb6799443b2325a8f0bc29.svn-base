import { BaseEntity } from './../../shared';

export class AccountingData implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public originalRowId?: number,
        public accountingRef1?: string,
        public accountingRef2?: string,
        public accountingRef3?: string,
        public accountingRef4?: string,
        public accountingRef5?: string,
        public accountingRef6?: string,
        public accountingRef7?: string,
        public accountingRef8?: string,
        public accountingRef9?: string,
        public accountingRef10?: string,
        public ledgerRef?: string,
        public currencyRef?: string,
        public lineType?: string,
        public coaRef?: string,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
    ) {
    }
}

export const AccountingDataBreadCrumbTitles = {
    accountingData: 'Accounting Data',
    accountingDataDetails: 'Accounting Details',
    accountingDataNew: 'Accounting Creation',
    accountingDataEdit: 'Edit Accounting'
    }


export class acctCustomFilters {
    constructor(
        public tenantId?:any,
        public filterColumns?:acctFilterColumns[],
        public limit?: number,
        public page?: number,
        public status?: string,
        public viewId?: any,
        public groupId?:any
    ){

    }
}

export class acctFilterColumns {
    constructor(
        public columnName?:string,
        public operator?:string,
        public columnValue?:string,           
    ){

    }
}

export class AcctDataQueries {
    constructor(
        public tenantId?:number,
        public groupId?: number,
        public dataViewId?: number,
        public sourceOrTarget?: any,
        public status?: string,
        public jobReference?:any,
        public groupBy?:any,
        public rangeFrom?:any,
        public rangeTo?: any,
        public keyValues?:acctKeyValues,
    ) {
    }
}

export class Car {
   constructor(
    public vin?:string,
    public year?:string,
    public brand?:string,
    public color?:string,
    public price?:string,
    public saleDate?:string
   )
   {

   }
}


export class recordParams{
    constructor(
        public dataViewId?:string,
        public groupBy?:any,
        public groupByParams?: filteredParams,
        public groupId?: string,
        public jobReference?: any,
        public keyValues?: acctKeyValues,
        public rangeFrom?:string,
        public rangeTo?:any,
        public sourceOrTarget?: string,
        public status?: string,
        public tenantId?: any,
        public originalRowIds?: any[],
        public searchWord? : any,
        public sortOrderBy? :any,
        public sortByColumnId? :any
    ) {
        this.dataViewId = undefined;
        this.groupBy = "";
        this.groupByParams = {};
        this.groupId = undefined;
        this.jobReference = "";
        this.keyValues = {};
        this.rangeFrom = "";
        this.rangeTo = "";
        this.sourceOrTarget = "";
        this.tenantId = undefined;
        this.originalRowIds = [];
        this.searchWord = "";
    }
}

export class acctKeyValues{
    constructor(
        public batchNames?:any[],
        public columnValues?:groupByColumnValues,
        public days?: any[],
        public originalRowIds?: any[],
        public reconReferences?: any[],
        public ruleIds?: any[],
        public viewId?:any
    ) {
        this.batchNames = [];
        this.columnValues = {};
        this.days = [];
        this.originalRowIds =[];
        this.reconReferences = [];
        this.ruleIds = [];
        this.viewId = 0
    }
}

export class filteredParams{
    constructor(
        public batchName?:string,
        public columnId?:any,
        public columnValue?: string,
        public day?: string,
        public ruleId?: any,
        public viewId?: any,
        public othersRuleName?: string
    ) {
    }
}

export class countAmountsObject{
   constructor(
        public sourceViewId?:string,
        public groupBy?:any,
        public headerParams?: filteredParams,
        public groupId?: string,
        public jobReference?: any,
        public rangeFrom?:string,
        public rangeTo?:any,
        public tenantId?: any,
        public filterGroupBy?:string,
        public columnId?: any
    ) {
        this.sourceViewId = undefined;
        this.groupBy = "";
        this.headerParams = {};
        this.groupId = undefined;
        this.jobReference = "";
        this.rangeFrom = "";
        this.rangeTo = "";
        this.tenantId = undefined;
        this.filterGroupBy = "";
        this.columnId = undefined;
    }
}


export class groupByColumnValues{
    constructor(
        public columnId?:any,
        public columnValues?: string[],
    ){
        this.columnId = 0;
        this.columnValues = [];
    }
}

export class selectedAcctRows {
    constructor(
        public categoryRef?:string,
        public coaRef?:string,
        public credits?:credits[],
        public currencyRef?:any,
        public debits?:debits[],
        public ledgerRef?:any,  
        public rowId?:any,
        public sourceRef?:string,
        public accountedCurrency?:string,
        public fxRateId?: any,
        public conversionDate?:string

    ){

    }
}

export class segValues {
    constructor(
        public id?: number,
		public mappingSetId?: number,
		public sourceValue?: number,
        public targetValue?: string,
        public createdBy?:number,
        public lastUpdatedBy?:number,
        public createdDate?:string,   
        public lastUpdatedDate?:string, 
        public status?:boolean,
        public startDate?:string,
        public endDate?:string
    ){
        this.id = 0;
        this.mappingSetId = 0;
        this.sourceValue = undefined;
        this.targetValue = "";
        this.createdBy = 0;
        this.lastUpdatedBy = 0;
        this.createdDate = "";
        this.lastUpdatedDate = "";
        this.status = false;
        this.startDate = null;
        this.endDate = null;
    }
}

export class credits {
    constructor(
        public amountColId?:any,
        public creditLine?:string,
        public lineTypeDetial?:string,   
        public amountColName?:string, 
        public lineTypeMeaning?:string,
        public creditSegs?:segValues[],
        public creditSegValues?:segValues[]
    ){
        this.amountColId = 0;
        this.creditLine = "";
        this.lineTypeDetial = "";
        this.lineTypeMeaning = "";
        this.amountColName = "";
        this.creditSegs = [];
        this.creditSegValues = [];
    }
}
export class debits {
    constructor(
        public amountColId?:any,
        public debitLine?:string,
        public lineTypeDetial?:string,
        public amountColName?:string, 
        public lineTypeMeaning?:string,
        public debitSegs?:segValues[],
        public debitSegValues?:segValues[]
    ){
        this.amountColId = 0;
        this.debitLine = "";
        this.lineTypeDetial = "";
        this.amountColName = "";
        this.lineTypeMeaning = "";
        this.debitSegs = [];
        this.debitSegValues = [];
    }
}

export class jsonforAccounting {
    constructor(
        public groupId?: any,
        public rows?: selectedAcctRows[],
        public viewId?:any
    )
    {

    }
}

export class groupingFilterParams {
    constructor(
        public  key?:string,
        public  values?:any[],
        public  dataType?:string
    ){

    }
}

export class colActSearchVal {
    constructor(
        public columnName?:any,
        public searchWord?:any
    ){

    }
}

export class awqAllParams {
    constructor(
        public groupId?:number,
        public tenantId?:number,
        public viewId?:number,
        public status?:string,
        public rangeFrom?:any,
        public rangeTo?:any,
        public filters?:groupingFilterParams[],
        public periodFactor?:any,
        public searchWord?:any,
        public sortByColumnName?:any,
        public sortOrderBy?:any,
        public userId?:number,
        public groupBy?:any,
        public keyValues?: acctKeyValues,
        public groupByParams?: filteredParams,
        public pageNumber?:number,
        public pageSize?:number,
        public columnSearch?:colActSearchVal[],
        public type?: string,
        public originalRowIds?:any[],
        public accountingType?:string

    ){

    }
}