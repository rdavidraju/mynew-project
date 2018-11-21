import {BaseEntity} from '../../shared/model/base-entity';

export const notificationOptions = {
        position: ["bottom", "right"],
        timeOut: 1000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: false,
        preventLastDuplicates: false
//        maxLength: 10     // To specify length of the message string
    };

export  var isFullScreen:boolean;

export class ReconDashBoardObject {
    constructor(
        public ruleGroupId?: number,
        public sourceViewId?: number,
        public status?: string,
        public startDateRange?: string,
        public endDateRange?: string,
        public ruleGroupName?: string,
        public sourceViewName?:string
    ) {
        /* this.enable = false; */
    } 
}

export class AcctDashBoardObject {
    constructor(
        public ruleGroupId?: number,
        public dataViewId?: number,
        public status?: string,
        public startDateRange?: string,
        public endDateRange?: string,
        public filterName?: string,
        public ruleGroupName?: string,
        public dataViewName?:string
    ) {
        /* this.enable = false; */
    } 
}