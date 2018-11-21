import { FormGroup, FormBuilder, FormArray, Validators ,FormControl} from '@angular/forms';
import {ApprovalConditions} from './approval-conditions.model';
import { ApprovalActions } from './approval-actions.model';
export class ApprovalRules {
    constructor(
            public id?:number,
            public copiedRefId?:any,
            public editRule?:boolean,
            public duplicateRuleName?:boolean,
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
            public sourceDVId?:any,
            public ruleGroupAssignId?:number,
            public approvalConditions?:ApprovalConditions[],
            public approvalActions?: ApprovalActions,
            public startDateChange ?:boolean,
            public endDateChange ?: boolean, 
            public formCntrl?: FormControl,
            public columnLOV?:any,
            public selectedIndex=0
    ) {
        this.approvalNeededType=null;
        this.startDate = new Date();
        this.startDateChange=false;
        this.endDateChange=false;
        this.editRule=true;
        this.formCntrl = new FormControl('');
    }
}
