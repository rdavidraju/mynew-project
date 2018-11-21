import { BaseEntity } from './../../shared';
import {FileTemplatesService} from '../file-templates/file-templates.service';
export class FileTemplateLines implements BaseEntity {
    constructor(
        public ftlService?:FileTemplatesService,
        public id?: number,
        public templateId?: number,
        public lineNumber?: number,
        public masterTableReferenceColumn?: string,
        public recordTYpe?: string,
        public recordIdentifier?: string,
        public columnNumber?: number,
        public enclosedChar?: string,
        public positionBegin?: number,
        public positionEnd?: number,
        public columnHeader?: string,
        public constantValue?: string,
        public zeroFill?: string,
        public align?: string,
        public formula?: string,
        public dateFormat?: string,
        public timeFormat?: string,
        public amountFormat?: string,
        public overFlow?: string,
        public skipColumn?: string,
        public columnDelimeter?: string,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public lastLine?:any,
        public lastLineNumber?:any,
        public edit?:any,
        public lastMasterTableRefCol?:boolean,
        public lastColNumber?:boolean,
        public example?:string,
        public functionName?:string,
        public showPopUp?:boolean,
        public selectedFunctionName?:string,
        public enableConstant ?: boolean,
        public interRI?:string,
        public duplicateFileTemplateColumn?:boolean
        //public customCol ?: boolean
    ) {
        this.id=0;
        this. templateId=0;
        this. lineNumber=0;
        this. masterTableReferenceColumn='';
        this. recordTYpe='';
        this. recordIdentifier='';
        this. columnNumber=0;
        this. enclosedChar='';
        if(this.ftlService && this.ftlService.justJson.columnHeader) {
            this.columnHeader= this.ftlService.justJson.columnHeader;
        }  else{
        this. columnHeader='';
        }
        if(this.ftlService && this.ftlService.justJson.dateFormat) {
            this.dateFormat= this.ftlService.justJson.dateFormat;
        } else{
        this. dateFormat='';
        }
        if(this.ftlService && this.ftlService.justJson.timeFormat) {
            this.timeFormat= this.ftlService.justJson.timeFormat;
        }  else{
        this. timeFormat='';
        }
        if(this.ftlService && this.ftlService.justJson.amountFormat)  {
            this.amountFormat= this.ftlService.justJson.amountFormat;
        }   else{
        this. amountFormat='';
        }

        this. skipColumn='';
        this. columnDelimeter='';
        this. edit=false;
        this. lastMasterTableRefCol=false;
        this.enableConstant = false;
        if(this.ftlService && this.ftlService.justJson && this.ftlService.justJson.recordStartRow){
        this.recordIdentifier= this.ftlService.justJson.recordStartRow;
        }
        this.ftlService=null;
    }
}
