import { BaseEntity } from './../../shared';

export const AccountingDataBreadCrumbTitles = {
    accountingData: 'Accounting Data',
    accountingDataDetails: 'Accounting Details',
    accountingDataNew: 'Accounting Creation',
    accountingDataEdit: 'Edit Accounting'
    }

export class RecordParams{
    constructor(
        public dataViewId?:string,
        public groupBy?:any,
        public groupByParams?: FilteredParams,
        public groupId?: string,
        public jobReference?: any,
        public keyValues?: AcctKeyValues,
        public rangeFrom?:string,
        public rangeTo?:any,
        public sourceOrTarget?: string,
        public status?: string,
        public tenantId?: any,
        public originalRowIds?: any[],
        public searchWord?: any,
        public sortOrderBy?:any,
        public sortByColumnId?:any
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

export class AcctKeyValues{
    constructor(
        public batchNames?:any[],
        public columnValues?:GroupByColumnValues,
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

export class FilteredParams{
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

export class CountAmountsObject{
   constructor(
        public sourceViewId?:string,
        public groupBy?:any,
        public headerParams?: FilteredParams,
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


export class GroupByColumnValues{
    constructor(
        public columnId?:any,
        public columnValues?: string[],
    ){
        this.columnId = 0;
        this.columnValues = [];
    }
}

export class SelectedAcctRows {
    constructor(
        public categoryRef?:string,
        public coaRef?:string,
        public credits?:Credits[],
        public currencyRef?:any,
        public debits?:Debits[],
        public ledgerRef?:any,  
        public rows?:any[],
        public sourceRef?:string,
        public accountedCurrency?:string,
        public fxRateId?: any,
        public conversionDate?:string

    ){

    }
}

export class SegValues {
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

export class Credits {
    constructor(
        public amountColId?:any,
        public creditLine?:string,
        public lineTypeDetial?:string,   
        public amountColName?:string, 
        public lineTypeMeaning?:string,
        public creditSegs?:SegValues[],
        public creditSegValues?:SegValues[]
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
export class Debits {
    constructor(
        public amountColId?:any,
        public debitLine?:string,
        public lineTypeDetial?:string,
        public amountColName?:string, 
        public lineTypeMeaning?:string,
        public debitSegs?:SegValues[],
        public debitSegValues?:SegValues[]
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


export class JsonforAccounting {
    constructor(
        public groupId?: any,
        public viewId?:any,
        public categoryRef?:string,
        public coaRef?:string,
        public credits?:Credits[],
        public enteredCurrency?:any,
        public debits?:Debits[],
        public ledgerRef?:any,  
        public rows?:any[],
        public sourceRef?:string,
        public accountedCurrency?:string,
        public fxRateId?: any,
        public conversionDate?:string
    ){

    }
}

export class GroupingFilterParams {
    constructor(
        public  key?:string,
        public  values?:any[],
        public  dataType?:string
    ){

    }
}

export class ColActSearchVal {
    constructor(
        public columnName?:any,
        public searchWord?:any
    ){

    }
}

export class AwqAllParams {
    constructor(
        public groupId?:number,
        public tenantId?:number,
        public viewId?:number,
        public status?:string,
        public rangeFrom?:any,
        public rangeTo?:any,
        public filters?:GroupingFilterParams[],
        public periodFactor?:any,
        public searchWord?:any,
        public sortByColumnName?:any,
        public sortOrderBy?:any,
        public userId?:number,
        public groupBy?:any,
        public keyValues?: AcctKeyValues,
        public groupByParams?: FilteredParams,
        public pageNumber?:number,
        public pageSize?:number,
        public columnSearch?:ColActSearchVal[],
        public type?: string,
        public originalRowIds?:any[],
        public accountingType?:string,
        public groupByColumnName?: string

    ){

    }
}

export class SplitRowParams {
    constructor(
        public adjustmentMethod?: string,
        public dataViewId?:number,
        public adjustmentType?: string,
        public amount?:number,
        public rowDescription?: any,
        public masterRowId?: any,
        public percentValue?: any

    ){

    }
}

export class RowsForSplit {
    constructor(
        public rowType?: any,
        public rowAmount?: any,
        public rowNumber?: any
    ){}
}

export class SelectedAcctGroup {
    constructor(
        public groupId?:any,
        public viewId?:any,
        public periodFactor?: any,
        public rangeFrom?:any,
        public rangeTo?:any,
        public viewName?: string,
        public ruleGroupName?: string,
        public viewDisplayId?:string,
        public rgForDisplay?:string

    ){

    }
}