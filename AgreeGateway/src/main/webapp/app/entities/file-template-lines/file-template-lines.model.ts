import { BaseEntity } from './../../shared';

export class FileTemplateLines implements BaseEntity {
    constructor(
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
        public columnDelimiter?: string,
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
                public selectedFunctionName?:string          
    ) {
        this.id=0;
        this. templateId=0;
        this. lineNumber=0;
        this. masterTableReferenceColumn='';
        this. recordTYpe='';
        this. recordIdentifier='';
        this. columnNumber=0;
        this. enclosedChar='';
        this. columnHeader='';
        this. dateFormat='';
        this. timeFormat='';
        this. amountFormat='';
        this. skipColumn='';
        this. columnDelimiter='';
        this. edit=false;
        this. lastMasterTableRefCol=false;
    }
}
