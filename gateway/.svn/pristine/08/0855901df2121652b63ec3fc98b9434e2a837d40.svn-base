import { BaseEntity } from './../../shared';
import { JhiDateUtils } from 'ng-jhipster';
import { ExcelConditions } from '.';
import { Http, Headers, Response, RequestOptions, URLSearchParams, BaseRequestOptions } from '@angular/http';
import {FileTemplatesService} from './file-templates.service';
export class FileTemplates implements BaseEntity {
    constructor(
        public ftlService?:FileTemplatesService,
        public id?: number,
        public templateName?: string,
        public description?: string,
        public startDate?: any,
        public endDate?: any,
        public status?: string,
        public fileType?: string,
        public delimiter?: string,
        public source?: string,
        public skipRowsStart?: number,
        public skipRowsEnd?: number,
        public number_of_columns?: number,
        public headerRowNumber?: number,
        public tenantId?: number,
        public createdBy?: number,
        public createdDate?: any,
        public lastUpdatedBy?: number,
        public lastUpdatedDate?: any,
        public data?: any,
        public sampleData?:any,
        public colHeaders?: any,
        public sdFilename?: any,
        public record?: string,
        public recordIdentifier ?: string,
        public enabledFlag ?: boolean,
		public rowIdentifier?:any,
        public multipleRowIdentifiers ?:boolean ,
        public excelConditions ?: ExcelConditions,
        public startRowColumns ?: string[]
       // public idForDisplay ?: string
    ) {
        this.multipleRowIdentifiers =false;
      
        this.excelConditions = new ExcelConditions(this.ftlService);
        if(this.ftlService && this.ftlService.justJson.templateName)
        {
            this.templateName = this.ftlService.justJson.templateName;
        }
        if(this.ftlService && this.ftlService.justJson.description)
        {
            this.description = this.ftlService.justJson.description;
        }
        if( this.ftlService && this.ftlService.justJson.startDate) {
            this.startDate = this.ftlService.justJson.startDate;
        } else{
             this.startDate = new Date();
        }
       // else  
        //this.startDate = new Date();

        if(this.ftlService && this.ftlService.justJson.endDate)
        {
            this.endDate = this.ftlService.justJson.endDate;
        }
        if(this.ftlService && this.ftlService.justJson.Source)
        {
            this.source = this.ftlService.justJson.Source;
        }
        if(this.ftlService && this.ftlService.justJson.fileType)
        {
            this.fileType = this.ftlService.justJson.fileType;
        }
        if(this.ftlService && this.ftlService.justJson.delimiter)
        {
            this.fileType = this.ftlService.justJson.delimiter;
        }
        if(this.ftlService && this.ftlService.justJson.skipRowsStart)
        {
            this.fileType = this.ftlService.justJson.skipRowsStart;
        }
        if(this.ftlService && this.ftlService.justJson.skipRowsEnd)
        {
            this.fileType = this.ftlService.justJson.skipRowsEnd;
        }
        if(this.ftlService && this.ftlService.justJson.rowIdentifier)
        {
            this.fileType = this.ftlService.justJson.rowIdentifier;
        }
        if(this.ftlService && this.ftlService.justJson.multipleRowIdentifiers &&(
             this.ftlService.justJson.multipleRowIdentifiers.toLowerCase().search('true') >-1
             ||this.ftlService.justJson.multipleRowIdentifiers.toLowerCase().search('yes') >-1
           ||  this.ftlService.justJson.multipleRowIdentifiers.toLowerCase().search('true') >-1
         ||this.ftlService.justJson.multipleRowIdentifiers.toLowerCase().search('y') >-1 ))
        {
            this.multipleRowIdentifiers=true;
        }
        this.ftlService=null;
    }
}

export class FileTemplateBreadCrumbs {
    constructor(
        public fileTemplates: any = '',
        public fileTemplatesDetails: any = '',
        public fileTemplatesNew: any = '',
        public fileTemplatesEdit: any = ''
    ) {

    }
}
export const FileTemplateBreadCrumbTitles = {
    fileTemplates: 'Templates',
    fileTemplatesDetails: 'Template Details',
    fileTemplatesNew: 'Template Creation',
    fileTemplatesEdit: 'Edit Template',
    fileTemplatesCopy: 'Copy Template'
}


export class RowIdentifiersList{
    constructor(
            public ftlService?:FileTemplatesService,
            public id ?: number,
            public rowIdentifier ?: string,
            public criteriaMeaning?:string,
            public criteria ?: string,
            public positionStart ?: number,
            public positionEnd ?: number,
            public data ?: any,
            public createdBy ?: any,
            public lastUpdatedBy ?: any,
            public creationDate ?: any,
            public lastUpdatedDate ?: any,
            public selected ?: boolean
                    )
            {
        if(this.ftlService && this.ftlService.justJson.rowIdentifier)
        {
            this.rowIdentifier = this.ftlService.justJson.rowIdentifier;
        }
        else
        this.rowIdentifier = null;

        if(this.ftlService && this.ftlService.justJson.rowIdentifierCriteria)
        {
            this.criteria = this.ftlService.justJson.rowIdentifierCriteria;
        }
        else
        this.criteria=null;

        if(this.ftlService && this.ftlService.justJson.positionStart)
        {
            this.positionStart = +this.ftlService.justJson.positionStart;
        }
        else
        this.positionStart=0;

        if(this.ftlService && this.ftlService.justJson.positionEnd)
        {
            this.positionEnd = +this.ftlService.justJson.positionEnd;
        }
        else
        this.positionEnd=0; 
        this.selected =false;

        this.ftlService=null;
        
        
        
    }
   
}

export class JustJson{
    constructor(
        public templateName ?: string,
        public description ?: string,
        public startDate ?: string,
        public endDate ?: string,
        public fileType ?: string,
        public delimiter ?: string,
        public Source ?: string,
        public skipRowsStart ?: string,
        public skipRowsEnd ?: string,
        public refId ?: any,
        public multipleRowIdentifiers ?: string,
        public skipEmptyLines ?: string,
        public startRowColumns ?: string,
        public rowIdentifierCriteria ?: string,
        public rowIdentifier ?: string,
        public positionStart ?: string,
        //public positionEnd ?: string,
        public templateColumnName ?: string,
        public operator ?: string,
        public value ?: string,
        public logicalOperator ?: string,
        public type ?: string,
        public positionBegin ?: string,
        public positionEnd ?: string,
        public columnHeader ?: string,
        public constantValue ?: string,
        public dateFormat ?: string,
        public timeFormat ?: string,
        public amountFormat ?: string,
        public skipColumn ?: string,
        public recordStartRow?:string
    )
    {

    }
}
let justJ:JustJson;

export class FinalJSONModel
{
 constructor(
    public ftlService:FileTemplatesService,
    public refId:any,
    public templateName ?: string,
    public description ?: string,
    public Source ?: string,
    public startDate ?: string,
    public endDate ?: string,
    public fileType ?: string,
    public delimiter ?: string,
    public skipRowsStart ?: string,
    public skipRowsEnd ?: string,
    //public rowIdentifier ?: string,
    public multipleIdentifier ?: string,
    public skipEmptyLines ?: string,
    public startRowColumns ?: string,
    public rowIdentifier ?: string,
    public domainName ?:string,
    public tableName ?: string,
    public duplicateWith?:string,
    public templateLine ?: TemplateLine, 
    public rowIdentiList ?: RowIdenti,   
    public rowCondi ?: RowCondi
)
 {
  this.templateName= ftlService.justJson.templateName;
  this.description= ftlService.justJson.description;
  this.Source=  ftlService.justJson.Source;;
  this.startDate=  ftlService.justJson.startDate;
  this.endDate=  ftlService.justJson.endDate;
  this.fileType=  ftlService.justJson.fileType;
  this.delimiter=  ftlService.justJson.delimiter;
  this.skipRowsStart=  ftlService.justJson.skipRowsStart;
  this.skipRowsEnd=  ftlService.justJson.skipRowsEnd;
  this.rowIdentifier=  ftlService.justJson.rowIdentifier;
  this.multipleIdentifier=  ftlService.justJson.multipleRowIdentifiers;
  this.skipEmptyLines=  ftlService.justJson.skipEmptyLines;
  this.startRowColumns=  ftlService.justJson.startRowColumns;
  this.templateLine = new TemplateLine(ftlService);
  this.rowIdentiList = new RowIdenti(ftlService);
  this.rowCondi = new RowCondi(ftlService);
  this.ftlService=null;
  this.refId= ftlService.justJson.refId;
  this.domainName='com.nspl.app.domain.FileTemplates';
  this.tableName='t_file_templates';
  this.duplicateWith='templateName';
 }   
}
export class RowIdenti{
    constructor(
        public ftlService:FileTemplatesService,
        public refId?:any,
        public rowIdentifierCriteria ?: string,
        public rowIdentifier ?: string,
        public positionStart ?: string,
        public positionEnd ?: string,
        public templateName ?: string,
        public domainName ?:string,
        public parentTable ?: string,
        public parentColumn ?: string,
        public parentColumnValue ?: string,
        public tableName ?: string,
        public childColumn?:any
      //  public duplicateWith?:string
    )
    {
        this.templateName= ftlService.justJson.templateName;
        this.rowIdentifierCriteria = ftlService.justJson.rowIdentifierCriteria;
        this.rowIdentifier = ftlService.justJson.rowIdentifier;
        this.positionStart = ftlService.justJson.positionStart;
        this.positionEnd = ftlService.justJson.positionEnd;
        this.ftlService=null;
        this.domainName='com.nspl.app.domain.IntermediateTable';
        this.parentTable='com.nspl.app.domain.FileTemplates';
        this.parentColumn='templateName';
        this.parentColumnValue=ftlService.justJson.templateName;
        this.tableName='t_intermediate_table';
        this.childColumn='templateId';
       // this.duplicateWith='templateName';
    }
}
export class RowCondi{
    constructor(
        public ftlService:FileTemplatesService,
        public refId?:any,
        public templateColumnName ?: string,
        public operator ?: string,
        public value ?: string,
        public logicalOperator ?: string,
        public type ?: string,
        public domainName ?:string,
        public parentTable ?: string,
        public parentColumn ?: string,
        public parentColumnValue ?: string,
        public tableName ?: string,
        public childColumn?:any
    )
    {
        this.templateColumnName = ftlService.justJson.columnHeader;
        this.operator = ftlService.justJson.operator;
        this.value = ftlService.justJson.value;
        this.logicalOperator = ftlService.justJson.logicalOperator;
        this.type = ftlService.justJson.type;
        this.ftlService=null;
        this.domainName='com.nspl.app.domain.RowConditions';
        this.parentTable='com.nspl.app.domain.FileTemplateLines';
        this.parentColumn='columnHeader';
        this.parentColumnValue=ftlService.justJson.columnHeader;
        this.tableName='t_row_conditions';
        this.childColumn='templateLineId';
    }
}
export class TemplateLine{
    constructor(
        public ftlService:FileTemplatesService,
        public refId?:any,
        public positionBegin ?: string,
        public positionEnd ?: string,
        public columnHeader ?: string,
        public constantValue ?: string,
        public dateFormat ?: string,
        public timeFormat ?: string,
        public amountFormat ?: string,
        public skipColumn ?: string,
        public domainName ?:string,
        public parentTable ?: string,
        public parentColumn ?: string,
        public parentColumnValue ?: string,
        public tableName ?: string,
        public childColumn?:any
    )
    {
        this.positionBegin = ftlService.justJson.positionBegin;
        this.positionEnd = ftlService.justJson.positionEnd;
        this.columnHeader = ftlService.justJson.columnHeader;
        this.constantValue = ftlService.justJson.constantValue;
        this.dateFormat = ftlService.justJson.dateFormat;
        this.timeFormat = ftlService.justJson.timeFormat;
        this.amountFormat = ftlService.justJson.amountFormat;
        this.skipColumn = ftlService.justJson.skipColumn;
        this.ftlService=null;
        this.domainName='com.nspl.app.domain.FileTemplateLines';
        this.parentTable='com.nspl.app.domain.FileTemplates';
        this.parentColumn='templateName';
        this.parentColumnValue=ftlService.justJson.templateName;  
        this.tableName='t_file_template_lines'; 
        this.childColumn='templateId';
     }
}