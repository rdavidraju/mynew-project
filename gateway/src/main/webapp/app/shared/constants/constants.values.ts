import {BaseEntity} from '../../shared/model/base-entity';

export const notificationOptions = {
        position: top,
        timeOut: 1000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: false,
        clickIconToClose: true,
        preventLastDuplicates: false
//        maxLength: 10     // To specify length of the message string
    };

export  var isFullScreen:boolean;

export class ReconDashBoardObject {
    constructor(
        public ruleGroupId?: any,
        public sourceViewId?: any,
        public status?: string,
        public startDateRange?: string,
        public endDateRange?: string,
        public ruleGroupName?: string,
        public sourceViewName?:string,
        public targetViewId?:any,
        public targetViewName?:any,
        public periodFactor?:any,
        public groupBy?:any, //"columnName"
        public columnName?:any, //"currecncy_109"
        public columnValue?:any, //"INR"
        public columnDisplayName?:any
    ) {
        /* this.enable = false; */
    } 
}

export class AcctDashBoardObject {
    constructor(
        public ruleGroupId?: any,
        public dataViewId?: any,
        public status?: string,
        public startDateRange?: string,
        public endDateRange?: string,
        public filterName?: string,
        public ruleGroupName?: string,
        public dataViewName?:string,
        public periodFactor?:any,
        public groupBy?:any, //"columnName"
        public columnName?:any, //"currecncy_109"
        public columnValue?:any, //"INR"
        public columnDisplayName?:any
    ) {
        /* this.enable = false; */
    } 
}

export const selectedDateRange = [
    {
      "value" : "Today",
      "name"  : "1D",
      "checked" : false,
      "title" :  "Today",
      "type" : "default"
    },
    {
      "value" : "7Days",
      "name"  : "1W",
      "checked" : false,
      "title" :  "Last 7 Days",
      "type" : "default"
    },
    {
      "value" : "14Days",
      "name"  : "2W",
      "checked" : false,
      "title" :  "Last 14 Days",
      "type" : "default"
    },
    {
      "value" : "30Days",
      "name"  : "1M",
      "checked" : true,
      "title" :  "Last Month",
      "type" : "default"
    },
    {
      "value" : "2Months",
      "name"  : "2M",
      "checked" : false,
      "title" :  "Last 2 Months",
      "type" : "default"
    },
    {
      "value" : "3Months",
      "name"  : "3M",
      "checked" : false,
      "title" :  "Last 3 Months",
      "type" : "default"
    },
    {
      "value" : "6Months",
      "name"  : "6M",
      "checked" : false,
      "title" :  "Last 6 Months",
      "type" : "default"
    }
  ];