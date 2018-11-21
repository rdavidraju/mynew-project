import { BaseEntity } from './../../shared';

export const ReconcileBreadCrumbTitles = {
    reconcile: 'Reconcile List',
    reconcileDetails: 'Reconcile Details',
    reconcileNew: 'Reconcile Creation',
    reconcileEdit: 'Edit Reconcile',
    reconcilewq: 'Reconcile List'
}

export class AnaylticParams {
    constructor(
        public columnId?: any,
        public groupBy?: string,
        public periodFactor?: string,
        public rangeFrom?: string,
        public rangeTo?: string,
        public ruleGroupId?: any,
        public sViewId?:any,
        public status?: string,
        public tViewId?: any,
        public tenantId?: number,
        public sColumnName?:any,
        public tColumnName?:any,
        public dateQualifierType?:any,
        public pageSize?:number,
        public pageNumber?: number,
        public groupByColumnName?: string
    ){

    }
}

export class QueryParams {
    constructor(
        public ruleGroupId?:any,
        public ruleGroupName?: string,
        public batchNames?: any[],
        public source?: any[],
        public target?: any[],
    ){
    }
}

export class ReconDataQueries {
    constructor(
        public tenantId?:number,
        public sViewId?:any,
        public tViewId?:any,
        public dateQualifierType?: any,
        public groupId?: any,
        public dataViewId?: any,
        public sourceOrTarget?: any,
        public status?: string,
        public unReconciled?: any,
        public groupBy?:any,
        public rangeFrom?:any,
        public rangeTo?: any,
        public pageNumber?:number,
        public pageSize?:number,
        public keyValues?:ReconKeyValues,
        public searchWord?:any,
        public sortOrderBy?:any,
        public sortByColumnId?:any,
        public dataType?:any,
        public userId?:any,
        public periodFactor?:any,
        public columnSearch?:ColSearchVal[]

    ) {
    }
}

export class ColSearchVal {
    constructor(
        public columnId?:any,
        public searchWord?:any,
        public dataType?:any
    ){

    }
}

export class ReconAmounts {
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

export class ReconKeyValues{
    constructor(
        public batchNames?:any[],
        public columnValues?:GroupByColumnValues,
        public days?: any[],
        public ruleIds?: any[],
        public viewId?: any,
        public periodFactor?: string,
        public status?:any[],
        public filters?:any[]
    ) {
        this.batchNames = [];
        this.columnValues = {};
        this.days = [];
        this.ruleIds =[];
        this.viewId = 0;
        this.periodFactor = "";
        this.status = [];
    }
}

export class UnReconKeyValues{
    constructor(
        public filters?:UnReconFilters[],
        public groupId?:any,
        public periodFactor?: any,
        public rangeFrom?: any,
        public rangeTo?: any,
        public sViewId?: any,
        public tViewId?: any,
        public type?: any,
        public reconReferences?:any[],
        public unReconcileType?:any
    ) {

    }
}

export class UnReconFilters{
    constructor(
        public columnName?:any,
        public columnValues?:any[]
    ) {
        this.columnName = "";
        this.columnValues = [];
    }
}

export class GroupByColumnValues{
    constructor(
        public columnId?:any,
        public columnValues?: string[],
        public sColumnId?:any,
        public tColumnId?:any,
        public currencyValues?:string[]
    ){
        
    }
}

export class ManualReconOject{
    constructor(
        public source?:ManualSReconArray[],
        public target?:ManualTReconArray[],
    ){
        
    }
}

export class ManualSReconArray{
    constructor(
        public groupId?:any,
        public rowId?: any,
        public viewId?: any
    ){
        
    }
}


export class ManualTReconArray{
    constructor(
       public groupId?:any,
        public rowId?: any,
        public viewId?: any
    ){
        
    }
}

export class SplitRowParams {
    constructor(
        public adjustmentMethod?: string,
        public dataViewId?:any,
        public adjustmentType?: string,
        public amount?:number,
        public rowDescription?: any,
        public masterRowId?: any,
        public percentValue?: any

    ){

    }
}

export class SuggestedParams {
    constructor(
        public groupId?:any,
        public tViewId?:any,
        public sViewId?:any,
        public source?:SuggestedSource[],
        public target?:SuggestedTarget[]
    ){

    }

}

export class SuggestedSource {
    constructor(
        public jobReference?:string,
        public reconReference?:string,
        public rowId?: any,
        public ruleId?: 0
    ){
        
    }
}

export class SelectedGroup {
    constructor(
        public groupId?:any,
        public sViewId?:any,
        public tViewId?:any,
        public periodFactor?: any,
        public rangeFrom?:any,
        public rangeTo?:any,
        public sViewName?: string,
        public tViewName?: string,
        public ruleGroupName?: string
    ){

    }
}

export class SuggestedTarget {
    constructor(
        public jobReference?:string,
        public reconReference?:string,
        public rowId?: any,
        public ruleId?: 0
    ){
        
    }
}

export class ReconciledLines {
    constructor(
        public reconRef?:any[],
        public groupingColumn?:any,
        public source?:any[],
        public target?:any[],
        public info?:any
    ){
        this.reconRef = [];
        this.groupingColumn = {};
        this.source = [];
        this.target = [];
        this.info = {};
    }
}