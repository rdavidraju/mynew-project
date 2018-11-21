import { BaseEntity } from './../../shared';

export class ReportTypes implements BaseEntity {
    constructor(
        public allowDrillDown?: boolean,
        public coa?: boolean,
        public coaList?: string[],
        public createdBy?: number,
        public creationDate?: any,
        public enableFlag?: boolean,
        public endDate?: any,
        public id?: any,
        public lastUpdatedBy?: number,
        public lasteUpdatedDate?: any,
        public mode?: boolean,         //Summary or Detail
        public show?: boolean,         //Accounted or Un-Accounted
        public reconcile?: boolean,    //Reconciled or Un-Reconciled
        public startDate?: any,
        public tenantId?: number,
        public type?: string,
        public typeDisplayName?: string,
        public typeDisplayName1?: string
    ) { }
}
export class Reports implements BaseEntity {
    constructor(
        public id?: any,
        public reportTypeId?: number,
        public reportType?: string,
        public reportTypeCode?: string,
        public reportTypeName?: string,
        public reportName?: string,
        public reportDesc?: string,
        public sourceViewId?: any,
        public trgtViewId?: any,
        public enableFlag?: boolean,
        public startDate?: Date,
        public endDate?: Date,
        public creationDate?: any,
        public createdBy?: number,
        public lastUpdatedDate?: any,
        public lastUpdatedBy?: number,
        public reportMode?: string,         //Summary or Detail
        public reportViewType?: string,     //Output type
        public coa?: string,
        public show?: string,               //Accounted or Un-Accounted
        public reconcile?: string,               //Reconciled or Un-Reconciled
        public chartType?: string,
        public allowDrilldown?: boolean,
        public rollBackType?: string,
        public rollBackCount?: number,
        public allowSequence?: boolean,
        public columnsDefinition?: DefinitionColsInfo[],
        public agingBucketId?: number,
        public agregator?: DefinitionColsInfo,
        public dateQualifier?: DefinitionColsInfo,
        public isFavourite?: boolean,
        public bucketType?:any
    ) {
        this.enableFlag = true;
    }
}

export class DefinitionColsInfo {
    constructor(
        public ColumnId?: any,
        public viewColName?: string,
        public viewColDisplayName?: string,
        public layoutDisplayName?: string,
        public parameterName?: string,
        public columnType?: string,
        public groupBy?: boolean,
        public parameterType?: string,
        public parameterTypeDisplay?: string,
        public showDetail?: boolean,
        public isMandatory?: boolean,
        public conditionalOperator?: string,
        public conditionalOperatorDisplay?: string,
        public conditionalValue?: string,
        public layoutValue?: string,
        public dataType?: string,
        public LOV?: any[],
        public isActive?: boolean,
        public isRecent?:boolean,
        public isRequired?: boolean
    ) { 
        this.isRequired=false;
    }
}

export class DataViews {
    constructor(
        public id?: any,
        public tenantId?: number,
        public dataViewDispName?: string,
        public dataViewName?: string,
        public dataViewObject?: string,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public startDate?: any,
        public endDate?: any,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public description?: any
    ) {
        this.enabledFlag = false;
    }
}

export class DataViewInfo {
    constructor(
        public lastUpdatedBy?: number,
        public endDate?: any,
        public description?: string,
        public enabledFlag?: boolean,
        public creationDate?: any,
        public dataViewDispName?: string,
        public lastUpdatedDate?: any,
        public dataViewsColumnsList?: DataViewCol[],
        public dataViewsUnionColumnsList?: any[],
        public createdBy?: number,
        public tenantId?: number,
        public dataViewName?: string,
        public id?: any,
        public conditions?: any[],
        public startDate?: any,
        public viewRelation?: any
    ) { }
}


export class DataViewCol {
    constructor(
        public colDataType?: string,
        public lastUpdatedBy?: number,
        public colName?: string,
        public columnHeader?: string,
        public refDvType?: string,
        public refDvName?: string,
        public creationDate?: any,
        public dataViewId?: number,
        public lastUpdatedDate?: any,
        public refDvColumn?: string,
        public createdBy?: number,
        public qualifier?: string,
        public groupBy?: boolean,
        public id?: any,
        public sourceName?: string,
        public columnName?: string
    ) { }
}

export class ReportColsInfo {
    constructor(
        public accVal?: string,
        public allowDrilldown?: boolean,
        public coa?: string,
        public coaId?: number,
        public reconcile?: string,
        public fields?: any[],
        public reportMode?: string,
        public outputType?: string,
        public chartType?: string,
        public dateTimeVal?: any,
        public groupingCols?: any[],
        public columnCols?: any[],
        public valueCols?: any[],
        public segmentsCount?: number,
        public segmentInfo?: Segments[],
        public outputCols?: any[],
        public reportTypeCode?: string,
        public categorizeBy?: string,
        public selPeriod?: any,
        public additionalParams?:any[],
        public reportConditionsList?: any[],
        public trgtViewId?:any
    ) { 
        this.categorizeBy='RECONCILIATION';
    }
}

export class Segments {
    constructor(
        public sequence?: number,
        public segId?: number,
        public flexValues?: string[],
        public flexValues1?: any[],
        public segName?: string,
        public selFlexValues?: string[],
        public selFlexValues1?: any[]
        
    ) { }
}

export class ParametersSubmited {
    constructor(
        public ParamId?:number,
        public Parameter?: string,
        public Value?: any
    ) { }
}

export class AgingBucket {
    constructor(
        public id?: any,
        public bucketName?: string,
        public count?: number,
        public type?: string,
        public startDate?: any,
        public endDate?: any,
        public flag?: boolean,
        public bucketDetDataList?: AgingBucketDetails[],
        public enabledFlag?: boolean,
    ) { }
}

export class AgingBucketDetails {
    constructor(
        public id?: any,
        public bucketId?: string,
        public fromValue?: number,
        public toValue?: number,
    ) { }
}

export class ReportRequestList {
    constructor(
        public reqId?: any,
        public reqName?: string,
        public reportId?: number,
        public outputPath?: string,
        public fileName?: string,
        public tenantId?: number,
        public status?: string,
        public createdDate?: any,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public submittedTime?: any,
        public generatedTime?: any,
        public jobId?: string,
        public filterMap?: ReportRequestParamsList[],
    ) { }
}

export class ReportRequestParamsList {
    constructor(
        public reconcile?: string,
        public coa?: any,
        public accVal?: number,
        public aggregator?: any,
        public allowDrilldown?: any,
        public outputType?: string,
        public reportMode?: string,
        public fields?: any[],
        public dateTimeVal?: string,
        public outputCols?: any[]
    ) { }
}

export class FavouriteReport {
    constructor(
        public id?: any,
        public reportId?: number,
        public tenantId?: number,
        public userId?: number
    ) { }
}

export const ReportsBreadCrumbTitles = {
    reports: 'Run Reports',
    reportsList: 'Reports List',
    createReport: 'Create Report',
    editReport: 'Edit Report',
    showReport: 'Show Report',
    dashboard: 'Dashboard',
    bucketList: 'Aging Bucket List',
    bucketDetail: 'Aging Bucket Detail'
};

export class ReportHeaders {
    constructor(
        public field?: string,
        public dataType?: string,
        public width?: string,
        public header?: string,
        public align?: string,
        public searchString?: string
    ) { }
}

export class OozieStatus {
    constructor(
        public ooziestatus?: boolean,
        public dbStatus?: boolean
    ) { }
}

export class Dashboard {
    constructor(
        public id?: any,
        public tenantId?: any,
        public userId?: any,
        public name?: string,
        public defaultFlag?: boolean
    ) { 
        this.defaultFlag=false;
    }
}

export class UseCase {
    constructor(
        public id?: any,
        public dashboardId?: any,
        public seqNum?: number,
        public usecaseName?: string,
        public reportTempId?: string,
        public outputType?: string,
        public groupbyCol?: string,
        public valbyCol?: string,
        public colbyCol?: string,
        public srcGroupbyCol?: ReportLayoutCols[],
        public srcValbyCol?: ReportLayoutCols[],
        public srcColbyCol?: ReportLayoutCols[],
        public selGroupbyCol?: ReportLayoutCols[],
        public selValbyCol?: ReportLayoutCols[],
        public selColbyCol?: ReportLayoutCols[],
        public xAxis?:number,
        public yAxis?:number,
        public width?:number,
        public height?:number
    ) { 
        this.xAxis=0;
        this.yAxis=-1;
        this.width=4;
        this.height=1;
    }
}

export class ReportLayoutCols {
    constructor(
        public id?: number,
        public itemName?: string,
        public dataName?: string,
    ) { }
}

export class UsecaseData {
    constructor(
        public name?: string,
        public type?: number,
        public data?: any[],
        public columns?: any[],
        public showFilters?: boolean
    ) {
        this.showFilters=false;
     }
}

export class ReportQueCard {
    constructor(
        public name?: string,
        public value?: number,
        public colName?: string,
        public lastUpdate?: number
    ) { }
}

