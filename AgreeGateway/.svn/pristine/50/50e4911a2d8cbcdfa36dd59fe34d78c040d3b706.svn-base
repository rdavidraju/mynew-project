import { BaseEntity } from './../../shared';

export class SourceConnectionDetails implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public protocol?: string,
        public clientKey?: string,
        public clientSecret?: string,
        public authEndpointUrl?: string,
        public tokenEndpointUrl?: string,
        public callBackUrl?: string,
        public host?: string,
        public userName?: string,
        public password?: string,
        public url?: string,
        public tenantId?: number,
        public createdDate?: any,
        public createdBy?: number,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public connectionType?: string,
        public startDate?: any,
        public endDate?: any,
        public jhiPassword?: string,
        public accessToken?: number,
        public port?: string,
        public sourceProfileList?: any,
        public enabledFlag?: boolean,
    ) {
        this.startDate = new Date();
    }
}

export class SourceProfile implements BaseEntity {
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
        this.startDate = new Date();
    }
}
export const SourceConnectionsBreadCrumbTitles = {
    sourceConnections: 'Connections List',
    sourceConnectionDetails: 'Connection Details',
    sourceConnectionNew: 'Connection Creation',
    sourceConnectionEdit: 'Edit Connection'
}
