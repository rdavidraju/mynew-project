import { BaseEntity } from './../../shared';

export class Roles implements BaseEntity {
    constructor(
        public id?: number,
        public tenantId?: number,
        public roleName?: string,
        public roleDescription?: string,
        public startDate?: any,
        public endDate?: any,
        public activeInd?: boolean,
        public functionalities?: any,
        public users?: any,
        public roleFncs?: any,
        public usrRol?: any,
        public roleDesc?: any,
    ) {
    }
}

export const RolesBreadCrumbTitles = {
    rolesList: 'Roles',
    rolesDetails: 'Role Details',
    rolesNew: 'Create New Role',
    rolesEdit: 'Edit Role',
}