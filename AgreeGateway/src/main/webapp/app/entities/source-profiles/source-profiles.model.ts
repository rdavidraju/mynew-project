import { BaseEntity } from './../../shared';
export class SourceProfileswithConnections {
    constructor(
            public accessToken?: number,
            public authEndPointUrl?: string,
            public callBackUrl?: string,
            public clientKey?: string,
            public clientSecret?: string,
            public connectionDescription?: string,
            public connectionId?: number,
            public name?: string,
            public connectionType?: string,
            public enabledFlag?: boolean,
            public endDate?: any,
            public host?: string,
            public id?: number,
            public jhiPassword?: string,
            public port?: string,
            public profileDescription?: string,
            public protocol?: string,
            public sourceProfileName?: string,
            public startDate?: any,
            public tokenEndPointUrl?: string,
            public userName?: string,
            public description?: string,
             public tenantId?: number
            
        ) {
            this.enabledFlag = false;
            this.startDate = new Date();
        }
    
}
export class SourceProfiles {
constructor(
        public id?: number,
        public sourceProfileName?: string,
        public description?: string,
        public startDate?: any,
        public endDate?: any,
        public enabledFlag?: boolean,
        public connectionId?: number,
        public tenantId?: number,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any
    ) {
        this.enabledFlag = false;
        this.startDate = new Date();
    }
    }

export const SourceProfilesBreadCrumbTitles = {
        sourceProfiles : 'Profiles',
        sourceProfileDetails : 'Profile Details',
        sourceProfileNew : 'New Profile',
        sourceProfileEdit : 'Edit Profile'
    };
