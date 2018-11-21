import { BaseEntity } from '../shared';

export class DashBoard implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public process?: string,
        public project?: string,
        public dateRange?: string,
        public viewId?: number,
        public viewName?: string,
        public startDate?: any,
        public endDate?: any,
        public enable?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public createdDate?: any,
        public lastUpdatedDate?: any,
        public reconciliation?:any,
    ) {
        /* this.enable = false; */
    } 
}

export class DashBoard4 implements BaseEntity {
    constructor(
        public id?: number,
        public processId?: any,
        public asOfValue?: boolean,
        public startDate?: any,
        public endDate?: any,
        public startDateSel?: any,
        public endDateSel?: any,
        public closedPeriods?: boolean,
        public selectedCalender?:any,
        public tempstartDate?: any,
        public startDateSelRecon?: any,
        public violationPeriod?:any,

    ){
        this.asOfValue = true;
    }
}

export class ModuleAnalysis implements BaseEntity {
    constructor(
        public id?: number,
        public etl?: any,
        public extracted?: any,
        public transformation?: any,
        public reconciliation?: any,
        public accounting?: any,
        public journals?: any,

    ){

    }
}

export class DashBoardValues implements BaseEntity {
    constructor(
        public id?: number,
        public etl?: DashBoardETL,
        public selectedProcess?: any,
        public reconciliation?: any,
        public accounting?: any,
        public journals?: any,
        public notifications?: DashBoardNotification,
        public dayWiseAnalysisBlock?: any,
    ){

    }
}

export class DashBoardETL implements BaseEntity {
    constructor(
        public id?: number,
        public headerNameTitle?: any,
        public dayWiseAnalysis?: any,
        public kpi?: any,
    ){

    }
}

export class DashBoardNotification implements BaseEntity {
    constructor(
        public id?: number,
        public headerTitle?: any,
        public display?: boolean,
        public defaultRange?: any,
    ){

    }
}


