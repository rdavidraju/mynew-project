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
        public createdBy?: number,
        public tenantId?: number,
        public dataViewName?: string,
        public id?: number,
        public conditions?: any[],
        public startDate?: any
    ){}
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
        public id?: number,
        public sourceName?: string,
        public columnName?: string
    ){}
}

export class Process{
    constructor(
        public name?: string,
        public isActive?: boolean
    ){
        this.isActive=true;
    }
}

export class RowIdentifier{
    constructor(
            public id ?: number,
            public rowIdentifier ?: string,
            public criteria ?: string,
            public positionStart ?: number,
            public positionEnd ?: number,
            public data ?: any,
            public selected ?: boolean
                    )
            {
        this.rowIdentifier = undefined;
        this.criteria='RECORD_START_ROW';
        this.positionStart=0;
        this.positionEnd=0;
    }
    
}
