import { BaseEntity } from './../../shared';

export class Reconcile implements BaseEntity {
    constructor(
        public id?: number,
        public processorName?: string,
        public transactionDate?: any,
        public recordType?: string,
        public amount?: number,
        public currencyCode?: string,
    ) {

    }
}
export const ReconcileBreadCrumbTitles = {
    reconcile: 'Reconcile List',
    reconcileDetails: 'Reconcile Details',
    reconcileNew: 'Reconcile Creation',
    reconcileEdit: 'Edit Reconcile',
    reconcilewq: 'Reconcile List'
}
export class ReportTypeRecords {
    constructor(
        public noOfRecords?: number,
        public reportName?: string,
        public reportType?: string,
        public status?: boolean,
        public hidden?: boolean,
        public headerName?: string,
    ) {
    }
}

export class anaylticParams {
    constructor(
        public columnId?: number,
        public groupBy?: string,
        public periodFactor?: string,
        public rangeFrom?: string,
        public rangeTo?: string,
        public ruleGroupId?: number,
        public sViewId?:number,
        public status?: string,
        public tViewId?: number,
        public tenantId?: number,
        public sColumnId?:number,
        public tColumnId?:number
    ){

    }
}


export class searchArray {
    constructor (
        public keyWord?: string,
        public columnName?:string
    ){

    }
}

export class QueryParams {
    constructor(
        public ruleGroupId?:number,
        public ruleGroupName?: string,
        public batchNames?: any[],
        public source?: any[],
        public target?: any[],
    ){
    }
}
export class ReconciliationRecords {
    constructor(
        public id?: number,
        public groupName?: string,
        public label?: any[],
        public source?: SourceReconciliationLines[],
        public type?: string,
        public target?: TargetReconciliationLines[],
        public info?:any
    ) {
    }
}

export class SourceReconciliationLines {
    constructor(
        public total?: any,
        public viewId?: number,
        public reconciled?: any,
        public viewName?: string,
        public unReconciled?: any,
        public target?: any[],

    ) {
    }
}


export class TargetReconciliationLines {
    constructor(
        public total?: any,
        public viewId?: number,
        public reconciled?: any,
        public viewName?: string,
        public unReconciled?: any

    ) {
    }
}

export class reconDataQueries {
    constructor(
        public tenantId?:number,
        public groupId?: number,
        public dataViewId?: number,
        public sourceOrTarget?: any,
        public status?: string,
        public unReconciled?: any,
        public groupBy?:any,
        public rangeFrom?:any,
        public rangeTo?: any,
        public pageNumber?:number,
        public pageSize?:number,
        public keyValues?:reconKeyValues,
        public searchWord?:any,
        public sortOrderBy? :any,
        public sortByColumnId? :any,
        public userId?:any,
        public periodFactor?:any,
        public columnSearch?:colSearchVal[]

    ) {
    }
}

export class colSearchVal {
    constructor(
        public columnId?:any,
        public searchWord?:any
    ){

    }
}

export class reconAmounts {
    constructor(
        public periodFactor?: any,
        public rangeFrom?:any,
        public rangeTo?:any,
        public ruleGroupId?:any,
        public sViewIds?:any[],
        public tViewIds?:any[],
        public tenantId?:number
    ){
        this.periodFactor = "",
        this.rangeFrom = "",
        this.rangeTo = "",
        this.ruleGroupId = 0,
        this.sViewIds = [],
        this.tViewIds = [],
        this.tenantId = 0
    }
}

export class reconKeyValues{
    constructor(
        public batchNames?:any[],
        public columnValues?:groupByColumnValues,
        public days?: any[],
        public ruleIds?: any[],
        public viewId?: any
    ) {
        this.batchNames = [];
        this.columnValues = {};
        this.days = [];
        this.ruleIds =[];
        this.viewId = 0;
    }
}

export class unReconKeyValues{
    constructor(
        public batchNames?:any[],
        public columnValues?:groupByColumnValues,
        public days?: any[],
        public reconReferences? : any[],
        public ruleIds?: any[],
        public viewId?: any
    ) {
        this.batchNames = [];
        this.columnValues = {};
        this.days = [];
        this.reconReferences = [];
        this.ruleIds =[];
        this.viewId = 0;
    }
}

export class groupByColumnValues{
    constructor(
        public columnId?:any,
        public columnValues?: string[],
    ){
        
    }
}

export class manualReconOject{
    constructor(
        public source?:manualSReconArray[],
        public target?: manualTReconArray[],
    ){
        
    }
}




export class manualSReconArray{
    constructor(
        public groupId?:any,
        public rowId?: any,
        public viewId?: any
    ){
        
    }
}


export class manualTReconArray{
    constructor(
       public groupId?:any,
        public rowId?: any,
        public viewId?: any
    ){
        
    }
}

export class customFilters {
    constructor(
        public tenantId?:any,
        public filterColumns?:any[],
        public limit?: number,
        public page?: number,
        public sourceOrTarget?: string,
        public status?: string,
        public viewId?: any,
        public groupId?:any
    ){

    }
}

export class SfilterColumns {
    constructor(
        public columnName?:string,
        public operator?:string,
        public columnValue?:string,
        public columnName1?:string,
        public operator1?:string,             
    ){

    }
}
// export class filteringData {
//     constructor(
//         public columnName?:string,
//         public operator?:string,
//         public columnValue?:string          
//     ){

//     }
// }

export class TfilterColumns {
    constructor(
        public columnName?:string,
        public operator?:string,
        public columnValue?:string,
        public columnName1?:string,
        public operator1?:string,             
    ){

    }
}

export class selectedReconciledIds{
    constructor(
        public source?:reconcileIdsforSource[],
        public target?:reconcileIdsforTarget[]
    ){

    }
}

export class reconcileIdsforSource{
    constructor(
        public groupId?:any,
        public rowId?:any,
        public viewId?:any
    ){

    }
}

export class reconcileIdsforTarget{
    constructor(
        public groupId?:any,
        public rowId?:any,
        public viewId?:any
    ){

    }
}