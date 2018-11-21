import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { FileTemplateLines } from '../file-template-lines/file-template-lines.model';
import { FileTemplates } from './file-templates.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { FileTemplatesPostingData } from './fileTemplatesPosting.model';
import { SessionStorageService } from 'ng2-webstorage';
@Injectable()
export class FileTemplatesService {

    private resourceUrl = 'agreeapplication/api/fileTemplatesByTenantId';
    private linesResourceUrl = 'agreeapplication/api/file-template-lines';
    private resourceSearchUrl = 'agreeapplication/api/_search/file-templates';
    private fetchByIdUrl = 'agreeapplication/api/file-templates';
    private fileUploadUrl = 'agreeapplication/api/fileUpload';
    private multipleFileUploadurl = 'agreeapplication/api/multipleFileUpload';
    private fileTempAndLinesSaveUrl = 'agreeapplication/api/FileTemplatesPostingDTO';
    private fileTemplateListOfDistinctValues = 'agreeapplication/api/fetchFileTemplateByColumnNameAndTableName';//PathVariable-tableName,columnName
    private readAndExtractCSVUrl = 'agreeapplication/api/findDelimiterAndFileExtension';
    private lookupCodesAndMeaningUrl = 'agreeapplication/api/lookupCodesAndMeaning';
   // private UserData = this.$sessionStorage.retrieve('userData');
    private templateLinesByTempIdUrl = 'agreeapplication/api/fetchTempLinesByTempId/{templateId}';
    private templateLinesByTempIdAndInterIdUrl = 'agreeapplication/api/fetchTemplateLinesByTempIdAndIntermediateId';
    private fileTemplateswithStatusUrl = 'agreeapplication/api/fileTemplateswithStatus';
    private fetchSampleDataByFileUrl = 'agreeapplication/api/findDelimiterAndFileExtension';
    private fileTemplLinesBAi2Url = 'agreeapplication/api/fileTemplateLinesForBAi2Format';
    private fetchTemplatesUrl = 'agreeapplication/api/templatesForTenant';
    public isFullScreen:boolean = false;
    public functionName : any;
    public formula : any;
    public example:any;
    public searchData:any;
    public returnToWQ :  boolean = false;
    
    constructor(private http: Http, private dateUtils: JhiDateUtils,
    private $sessionStorage: SessionStorageService) {
        this.returnToWQ=null;
     }
    public $:any;
    create(fileTemplates: FileTemplates): Observable<FileTemplates> {
        const copy = this.convert(fileTemplates);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(fileTemplates: FileTemplates): Observable<FileTemplates> {
        const copy = this.convert(fileTemplates);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
    findLine(id: number): Observable<FileTemplateLines> {
        return this.http.get(`${this.linesResourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
    find(id: number): Observable<FileTemplates> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
    getFileTemplates(offset,limit,sortColumn?:any,sortOrder?:any): Observable<any> {

        return this.http.get(this.resourceUrl+'?page='+offset+'&per_page='+limit+'&formType='+'FILE_TEMPLATES'+'&sortColumn='+sortColumn+'&sortOrder='+sortOrder)
        .map((res: Response) => {
            
            return res.json();
        })
        ;
    }
    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}`, options)
            .map((res: Response) => 
                this.convertResponse(res)
            );
    }
    fetchFileTemplates(): Observable<ResponseWrapper> {
        
        return this.http.get(`${this.fetchTemplatesUrl}`)
            .map((res: Response) => 
                this.convertResponse(res)
            );
    }
    

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }
    search(req?: any): Observable<ResponseWrapper> {
        let options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl+'?filterValue='+this.searchData, options)
            .map((res: any) => this.convertResponse(res))
            ;
    }
    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
           this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.startDate = this.dateUtils
            .convertLocalDateFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(entity.endDate);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(fileTemplates: FileTemplates): FileTemplates {
        const copy: FileTemplates = Object.assign({}, fileTemplates);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(fileTemplates.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(fileTemplates.endDate);

        copy.createdDate = this.dateUtils.toDate(fileTemplates.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(fileTemplates.lastUpdatedDate);
        return copy;
    }
    /**
     * Author : Shobha
     * Updated : Rk
     * fetch template by id
     */
    public fetchById(id: any): Observable<FileTemplates> {
        return this.http.get(`${this.fetchByIdUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.startDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.startDate);
            jsonResponse.endDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.endDate);
            return jsonResponse;
        });
    }
     /**
     * Author : Shobha
     * Convert local date 
     */
    convertDate(date:any)
    {
        if(date)
        {
         let convertedDate = date;
          convertedDate = convertedDate.split("-");
            
          let convStDate={
            "year":+convertedDate[0],
                "month":+convertedDate[1],
                "day":+convertedDate[2]    
            };
        return convStDate;
            }
    }
    /**
     * Author: Shobha
     * File upload for button
     */
    uploadFile(file: any): Observable<Response> {
        let formData = new FormData();
        formData.append('file', file);

        let headers = new Headers({});
        let options = new RequestOptions({ headers });

        return this.http.post(this.fileUploadUrl, formData, options).map((res: Response) => {
            return res.json();
        });
    }
    /**
  * Author: Shobha
  * file upload for DropZone
  */
    public fileUpload(files: any, skipStartRow: any, skipEndRow: any,rowIdentifier:any,delimiter: any): Observable<Response> {
        let formData = new FormData();
        formData.append('file', files);
        formData.append('skipStartRow', skipStartRow);
        formData.append('skipEndRow', skipEndRow);
        formData.append('rowIdentifier',rowIdentifier);
        formData.append('multipleIdentifier',false+'');
        //formData.append('rowIdentifier', rowIdentifier);
       // formData.append('tenantId', this.UserData.tenantId);
        if(!delimiter)
        delimiter = '';
          formData.append('delimiter', delimiter);
        let headers = new Headers({});
        let options = new RequestOptions({ headers });

        return this.http.post(this.readAndExtractCSVUrl, formData, options).map((res: Response) => {
           
            return res.json();
        });
    }   
    /**
     * Author : Shobha
     * save template , lines, source profile assignment
     */
    postFileTemplates(fileTemplates: FileTemplatesPostingData): Observable<Response> {
        // fileTemplates.fileTempDTO.tenantId = this.UserData.tenantId;
        // if (fileTemplates.fileTempDTO.createdBy)
        // { }
        // else {
        //     fileTemplates.fileTempDTO.createdBy = this.UserData.id;
        // }
        // fileTemplates.fileTemplateLinesListDTO.forEach(tempLineList => {
        //     if(tempLineList)
        //         {
        //         tempLineList.forEach(tempLine=>{
        //             tempLine.lastUpdatedBy = this.UserData.id;
        //             if (tempLine.createdBy) {
        //             }

        //             else {
        //                 tempLine.createdBy = this.UserData.id;
        //             }
        //         });
        //         }
        // });

       // fileTemplates.fileTempDTO.lastUpdatedBy = this.UserData.id;
        console.log('fileTemplates in service before save'+JSON.stringify(fileTemplates));
        return this.http.post(this.fileTempAndLinesSaveUrl, fileTemplates).map((res: Response) => {
            return res.json();
        });
    }
    /**
     * Author : Shobha
     *Fetch file templates by table name and column names
     */
    groupSideBarByCols(tableName: any, columnName: any): Observable<Response> {
        return this.http.get(`${this.fileTemplateListOfDistinctValues}/${tableName}/${columnName}`).map((res: Response) => {
            console.log('response in grp service' + res.json());
            return res.json();
        });
    }
    fetchDelimiters(): Observable<Response>{
        return this.http.get(`${this.lookupCodesAndMeaningUrl}/${'DELIMITER'}`).map((res: Response) => {
            return res.json();
        });
    }
    fetchTemplateLinesByTemplateId(templateId:any):Observable<ResponseWrapper>
    {
        console.log('in service temp 18');
         return this.http.get(this.templateLinesByTempIdUrl+'?templateId='+templateId).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    fetchTemplateLinesByTemplateIdAndIdentifieId(templateId:any,identifierId:any):Observable<ResponseWrapper>
    {
        if(identifierId && identifierId != null )
        {
            return this.http.get(this.templateLinesByTempIdAndInterIdUrl+'?templateId='+templateId+'&intermediateId='+identifierId).map((res: Response) => {
                let jsonResponse = res.json();
                return jsonResponse;
            });
        }
        else{
            return this.http.get(this.templateLinesByTempIdUrl+'?templateId='+templateId).map((res: Response) => {
                let jsonResponse = res.json();
                return jsonResponse;
            });
        }
        
    }
    fetchFileTemplatesBySourceProfileId(profileId): Observable<Response> {
        return this.http.get(this.fileTemplateswithStatusUrl+'?sourceProfileId='+profileId).map((res: Response) => {
            let jsonResponse : any = res.json();
        let i=0;
        if(jsonResponse)
            {
        jsonResponse.forEach(template=>{
            if(template.hold)
                i=i+1;
        });          
        if(jsonResponse.length-1 == i)
            jsonResponse.forEach(template=>{ 
                template['allHold'] = true;
            });
            }
           // console.log('jsonResponse of file templates-'+JSON.stringify(jsonResponse));
            return jsonResponse;
        });
    }
    /**
     * Author: Shobha
     * fetch sample data by file 
     */
       public fetchSampleDataByFile(files: any,multipleIdentifiersList:any,delimiter: any,multipleIdentifier:any): Observable<Response> {
           let formData = new FormData();
           formData.append('file', files);
           formData.append('multipleIdentifiersList',JSON.stringify(multipleIdentifiersList));
          // formData.append('tenantId', this.UserData.tenantId);
           formData.append('multipleIdentifier',multipleIdentifier);
           if(!delimiter)
           delimiter = '';
             formData.append('delimiter', delimiter);
           let headers = new Headers({});
           let options = new RequestOptions({ headers });
           //const blobOverrides = JSON.stringify(multipleIdentifiersList);
           //formData.append('multipleIdentifiersList', blobOverrides);
           return this.http.post(this.fetchSampleDataByFileUrl, formData, options).map((res: Response) => {
               let jsonResponse : any =res.json();
               return jsonResponse;
           });
       }

       /**
        * Author: Sameer
        * Post File Template Lines BAI2 Format
        * */
       fileTemplLinesBAi2(delimiter,fileType) {
           return this.http.get(`${this.fileTemplLinesBAi2Url}?delimiter=${delimiter}&fileType=${fileType}`).map((res: Response)=> {
                const jsonResponse = res.json();
                return jsonResponse;
           });
       }
}
