import { BaseEntity } from './../../shared';
import { User } from '../../shared/user/user.model';

export class TenantDetails implements BaseEntity {
    constructor(
        public id?: number,
        public tenantName?: string,
        public primaryContact?: string,
        public primaryContactExt?: string,
        public secondaryContact?: string,
        public secondaryContactExt?: string,
        public website?: string,
        public corporateAddress?: string,
        public corporateAddress2?: string,
        public city?: string,
        public state?: string,
        public country?: string,
        public pincode?: string,
        public domainName?: string,
        public domainNamedis?: string,
        public tenantConfigModule?: any,
        public firstName?:any,
        public lastName?:any,
        public emailId?:any,
        public conDetails?: any,
        public tenantLogo?: any,
        public tenantSubLogo?: any,
        public userDetails?: User
    ) {
    }
}

export class TenantConfigModule {
    constructor(
        public modules?: any,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public contractNum?: any,
        public mandatory?: boolean,
        public columnEdit?: boolean,
        public checkBox?: boolean,

    ) {
        this.startDate = null;
        this.endDate = null;
    }
}

export const TenantDetailsBreadCrumbTitles = {
    tenantDetailsList: 'Tenant Details',
    tenantDetailsDetails: 'Tenant Details Details',
    tenantDetailsNew: 'Create New Tenant Details',
    tenantDetailsEdit: 'Edit Tenant Details',
}
