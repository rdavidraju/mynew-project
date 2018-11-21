import { BaseEntity } from './../../shared';
import { SourceProfilesService } from '.';
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
             public tenantId?: number,
             public idForDisplay?:string,
             public status?:string,
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
        public status?:string,
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
        sourceProfileEdit : 'Edit Profile',
        sourceProfileCopy : 'Copy Profile'
    };
    export class JustJson{
        constructor(
        public sourceProfileName?: string,
        public description?: string,
        public startDate?: any,
        public endDate?: any,
        public connectionType?: any,
        public connectionId?: any,
        public fileTemplate?: any,
        public fileExtension?: any,
        public fileNameFormat ?:any,
        public fileDescription?: any,
        public sourceDirectoryPath?: any,
        public refId?:any

        )
        {
    
        }
    }

export class JSONModel {
    constructor(
        public spService :SourceProfilesService,
       // public refId:any,
        public sourceProfileName?: string,
        public description?: string,
        public startDate?: any,
        public endDate?: any,
        public connectionType?: any,
        public connectionId?: any,
        public tableName ?: any,
       // public domainName ?:string,
       // public duplicateWith?:string,
        public fileTemplate ?: FileTemplate
      
    ) {
        this.sourceProfileName = spService.justJson.sourceProfileName;
        this.description = spService.justJson.description;
        this.startDate = spService.justJson.startDate;
        this.endDate = spService.justJson.endDate;
        this.connectionType = spService.justJson.connectionType;
        // this.connectionId = {
        //     "domainName":"com.nspl.app.domain.SourceConnectionDetails",
        //     "parentColumnName":"name",
        //     "fetchColumnName":"id",
        //     "childColumnName":"connectionId",
        //     "columnValue": spService.justJson.connectionId
        // };
        this.fileTemplate=new FileTemplate(spService);
        this.spService=null;
        //this.refId=spService.justJson.refId;
       // this.domainName='com.nspl.app.domain.SourceProfiles';
        this.tableName = 't_source_profiles';
      //  this.duplicateWith='sourceProfileName';
    }
}
export class SourceProfileAndTemplate{
    constructor(
        public templateId?: string,
        public templateName?: string,
        public sourceProfileName?:string,
        public connectionId?:string,
        public connectionName?:string,
        public fileNameFormat?:string,
        public fileExtension ?:string,
        public fileDescription ?:string,
        public sourceDirectoryPath ?: string,
        public extractAndTransform ?: boolean,
    ){

    }
}
export class FileTemplate{
    constructor(
        public spService:SourceProfilesService,
        public sourceProfileId?:any,
        public fileTemplate?: string,
        public fileNameFormat?: string,
        public fileExtension?: any,
        public fileDescription?: string,
        public sourceDirectoryPath?: any,
       // public domainName ?:string,
       // public parentTable ?: string,
       // public parentColumn ?: string,
       // public parentColumnValue ?: string,
       // public tableName ?: any,
       // public childColumn?:any
    )
    {
    this.fileTemplate=spService.justJson.fileTemplate;
    this.fileNameFormat=spService.justJson.fileNameFormat;
    this.fileExtension=spService.justJson.fileExtension;
    this.fileDescription=spService.justJson.fileDescription;
    this.sourceDirectoryPath=spService.justJson.sourceDirectoryPath;
    this.spService=null;
   // this.parentTable='com.nspl.app.domain.SourceProfiles';
    //this.parentColumn='sourceProfileName';
    //this.parentColumnValue=spService.justJson.sourceProfileName;
   // this.domainName='com.nspl.app.domain.SourceProfileFileAssignments';
    //this.tableName = 't_source_profile_file_assignments';
   // this.childColumn = 'sourceProfileId';
    }
}