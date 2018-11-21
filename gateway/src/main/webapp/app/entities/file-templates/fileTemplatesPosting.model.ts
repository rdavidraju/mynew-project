import {FileTemplateLines} from '../file-template-lines/file-template-lines.model';
import {FileTemplates,RowIdentifiersList} from './file-templates.model';
import {FileTemplatesService} from './file-templates.service';
import {SourceProfileFileAssignments} from '../source-profile-file-assignments/source-profile-file-assignments.model';
import { Condition } from '.';
import { FileTemplate } from '../source-profiles';
export class FileTemplatesPostingData {
    constructor(
        public ftlService?:FileTemplatesService,
        public fileTempDTO?:FileTemplates,
        public skipRowConditionsList ?: Condition[],
        public endRowConditionsList ?: Condition[],
        public multipleRIList?:RowIdentifiersList[],
        public fileTemplateLinesListDTO?:FileTemplateLines[][],
        public sourceProfileFileAssignmentDTO?:SourceProfileFileAssignments
    ) {
        this.fileTempDTO = new FileTemplates(this.ftlService);
        this.skipRowConditionsList=[];
        this.skipRowConditionsList.push(new Condition(this.ftlService));
        this.endRowConditionsList=[];
        this.endRowConditionsList.push(new Condition(this.ftlService));
        this.fileTemplateLinesListDTO=[];
        const ftlList:FileTemplateLines[]=[];
        const ftl = new FileTemplateLines(this.ftlService);
        ftlList.push(ftl);
        this.fileTemplateLinesListDTO.push(ftlList);
        this.multipleRIList=[];
        this.multipleRIList.push(new RowIdentifiersList(this.ftlService));
       // this.fileTemplateLinesListDTO.push(new FileTemplateLines(this.ftlService));
        this.ftlService=null;
    }
}