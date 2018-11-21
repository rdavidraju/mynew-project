export class JobsBasicDetails {
    constructor(
        public jobId?: number,
        public jobName?: string,
        public jobDescription?: string,
        public programName?: string,
        public jobStatus?: boolean,
        public firstOpened?: boolean,
        public programId?: string,
    ) {
    }
}

export class Jobs {
    constructor(
        public id?: number,
        public jobName?: string,
        public jobDesc?: string,
        public programId?: number,
        public programName?: string,
        public jobStatus?: boolean,
        public parameters?: Parameters[],
        public sendFailAlerts?: boolean,
        public maxConsecutiveFails?: number,
        public scheStartDate?: any,
        public scheEndDate?: any,
        public frequencies?: Frequencies[],
        public occurence?: any,
        public runNow?: boolean,
        public neverEnd?: boolean
    ) {
        this.runNow = true;
        this.neverEnd = true;
    }
}

export class Frequencies{
   constructor(
    public id?: number,
    public type?: string,
    public typeMeaning?: string,
    public occurance?: number,
    public date?: Date,
    public minutes?: number,
    public hours?: number,
    public day?: any[],
    public month?: any[],
    public weekDay?: any[],
    public time?: any,
    public oozieJobId?: any,
    public status?: any,
    public frequencyTime?: any
    ){}
}

export class Parameters{
   constructor(
    public bindValue?: any,
    public paramId?: number,
    public paramName?: string,
    public valuesList?: any[],
    public showValue?: any,
    public selectedValue?: any,
    public dependency?: any,
    public mandatory?: boolean,
    public value?: any,
    public selectedValueName?: any,
    public search?: any
   ){
       
   }
}

export class Programs {
    constructor(
        public programId?: number,
        public programName?: string,
        public parametersSet?: Parameters[]
    ) {
    }
}

export class JobsSchedules{
    constructor(
     public oozieJobId?: string,
     public jobName?: string,
     public jobDescription?: string,
     public localStatDate?: any,
     public oozieStatus?: string,
     public childId?: string,
     public localEndDate?: any,
     public jobId?: number,
     public parametes?: string,
     public programName?: string,
     public frequencyType?: string,
     public startTime?: any,
     public endTime?: any,
     public schedulerName?: string,
     public status?: string
    ){}
 }

export const JobsBreadCrumbTitles = {
    jobsList: 'Jobs List',
    jobDetails: 'Job Details',
    jobNew: 'Job Creation',
    jobEdit: 'Edit Job',
    schedList: 'Schedulars List'
}

export class SchedulerActions{
    constructor(
     public actionName?: string,
     public createdBy?: number,
     public createdDate?: any,
     public endTime?: any,
     public id?: number,
     public jobId?: string,
     public lastUpdatedBy?: number,
     public lastUpdatedDate?: any,
     public schedulerId?: number,
     public startTime?: any,
     public status?: string,
     public step?: number,
     public tenantId?: number
    ){}
 }


 export class SchedularDetailFilter{
     constructor(
         public scheduledProgram?: number,
         public scheduledType?: string,
         public stDateFrom?: any,
         public stDateTo?: any,
         public scheduledStatusList?:any[]
     ){
         this.scheduledProgram=0;
         this.scheduledType='All';
         this.scheduledStatusList=jobStatusList;
     }
 }

export const jobStatusList = [{ statusName: 'Succeeded', status: 'SUCCEEDED', isSelected: true }, { statusName: 'Running', status: 'RUNNING', isSelected: true }, { statusName: 'Suspended', status: 'SUSPENDED', isSelected: true }, { statusName: 'Error', status: 'DONEWITHERROR', isSelected: true }, { statusName: 'Failed', status: 'FAILED', isSelected: true }, { statusName: 'Killed', status: 'KILLED', isSelected: true }, { statusName: 'Scheduled', status: 'SCHEDULED', isSelected: true }];
