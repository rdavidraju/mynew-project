export class Rules {
    constructor(
        public id?: number,
        public tenantId?: number,
        public selectedruleId?:number,
        public ruleCode?: string,
        public ruleName?: string,
        public priority?: number,        
        public sourceDataViewId?: number,
        public sourceDVId?: number,
        public sDataViewName?: string,
        public sDataViewDisplayName?: string,
        public targetDataViewId?: number,
        public targetDVId?: number,
        public tDataViewName?: string,
        public tDataViewDisplayName?: string,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public ruleType?: string,
        public ruleTypeMeaning?: string,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public creationDate?: any,
        public lastUpdatedDate?: any,
        public setRuleObject?: boolean,
        public selectedRuleCode?: string,
        public assignmentFlag ?: boolean,
        public newRule ?: boolean,
        public startDateChange ?:boolean,
        public endDateChange ?: boolean, 
        public ruleGroupAssignId?:number,
    ) {
        this.startDateChange =false;
        this.endDateChange=false;
        this.enabledFlag = false;
                 this. ruleCode='',
                 this. ruleName='',
                 this. ruleType='',
                 this. ruleTypeMeaning='',
                 this. createdBy=0,
                 this. lastUpdatedBy=0,
                 this. sourceDataViewId=0,
                 this. sDataViewName='',
                 this. sDataViewDisplayName='',
                 this. targetDataViewId=0,
                 this. tDataViewName='',
                 this. tDataViewDisplayName='',
                 this.priority=0,
                 this.selectedRuleCode =''; 
                 this.startDate = new Date();
    }
}