import { BaseEntity } from './../../shared';
import {FileTemplatesService} from '../file-templates';
export class SourceProfileFileAssignments implements BaseEntity {
    constructor(
        public ftlService?:FileTemplatesService,
        public id?: number,
        public sourceProfileId?: number,
        public sourceProfileName?: number,
        public fileNameFormat?: string,
        public fileDescription?: string,
        public fileExtension?: string,
        public frequencyTypeCode?: number,
        public frequencyType?: number,
        public dueTime?: string,
        public day?: number,
        public sourceDirectoryPath?: string,
        public localDirectoryPath?: string,
        public templateId?: number,
        public enabledFlag?: boolean,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public templateName?: string,
        public edit?: boolean,
    ) {
        this.enabledFlag = false;
       // if(this.ftlService.justJson)
       this.ftlService=null;
    }
}
