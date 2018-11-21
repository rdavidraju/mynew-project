import {ApprovalConditions} from './approval-conditions.model';
import { ApprovalActions } from './approval-actions.model';
export class ApprovalRules {
    constructor(
            public id?:number,
            public sequence?:number,
            public priority?:number,
            public ruleCode?: string,
            public enabledFlag?:boolean,
            public assignmentFlag?: boolean, 
            public startDate?:any,
            public endDate?: any,
            public approvalNeededType?: string,
            public approvalNeededTypeMeaning?: string,
            public sourceDataViewName?:string,
            public sourceDataViewId?:number,
            public sourceDVId?:number,
            public ruleGroupAssignId?:number,
            public approvalConditions?:ApprovalConditions[],
            public approvalActions?: ApprovalActions,
            public startDateChange ?:boolean,
            public endDateChange ?: boolean, 
    ) {
        this.approvalNeededType=null;
        this.startDate = new Date();
        this.startDateChange=false;
        this.endDateChange=false;
    }
}
