import { BaseEntity } from './../../shared';

export class JobDetails implements BaseEntity {
    constructor(
        public id?: number,
        public programmId?: number,
        public jobName?: string,
        public jobDescription?: string,
        public enable?: boolean,
        public startDate?: any,
        public endDate?: any,
        public parameterArguments1?: string,
        public parameterArguments2?: string,
        public parameterArguments3?: string,
        public parameterArguments4?: string,
        public parameterArguments5?: string,
        public parameterArguments6?: string,
        public parameterArguments7?: string,
        public parameterArguments8?: string,
        public parameterArguments9?: string,
        public parameterArguments10?: string,
        public createdBy?: number,
        public creationDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public tenantId?: number,
    ) {
        this.enable = false;
    }
}
