import { BaseEntity } from './../../shared';

export class UserRoleAssignment implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public roleId?: number,
        public roleName ?: string,
        public assignedBy?: number,
        public deleteFlag?: boolean,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public activeFlag?: boolean,
        public startDate?: any,
        public endDate?: any
    ) {
    }
}
