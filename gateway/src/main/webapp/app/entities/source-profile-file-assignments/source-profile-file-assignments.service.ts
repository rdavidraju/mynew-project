import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions  } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { SourceProfileFileAssignments } from './source-profile-file-assignments.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { FileTemplates } from '../file-templates/file-templates.model';
/*import { FileTemplates } from ' ../file-templates.model ';*/

@Injectable()
export class SourceProfileFileAssignmentsService {

   private resourceUrl = 'agreeapplication/api/source-profile-file-assignments';
    private resourceSearchUrl = 'agreeapplication/api/_search/source-profile-file-assignments';
    private spaByTempUrl = 'agreeapplication/api/SPAByTempId';
    private profileFileAssignmentsUrl = 'agreeapplication/api/sorceProfilesFileAssignments';
    private unassignedFileTemplatesUrl = 'agreeapplication/api/UnAssignedFileTemplatesList';
    private fetchFilesFromCloudToLocalUrl ='agreeapplication/api/fetchFilesFromCloudToLocalUsingSpfaId';
    private ProcessingFilesFromLocalPathUrl = 'agreeapplication/api/ProcessingFilesFromLocalPath';
    private holdFileTemplateUrl = 'agreeapplication/api/holdTemplate';
    private unHoldFileTemplateUrl = 'agreeapplication/api/unHoldTemplate';
    public ftToProf = '';
    public profToFt = '';
   // private UserData = this.$sessionStorage.retrieve('userData');
    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(sourceProfileFileAssignments: SourceProfileFileAssignments): Observable<SourceProfileFileAssignments> {
        const copy = this.convert(sourceProfileFileAssignments);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(sourceProfileFileAssignments: SourceProfileFileAssignments): Observable<SourceProfileFileAssignments> {
        const copy = this.convert(sourceProfileFileAssignments);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<SourceProfileFileAssignments> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }
    search(req?: any): Observable<ResponseWrapper> {
        let options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
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
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(sourceProfileFileAssignments: SourceProfileFileAssignments): SourceProfileFileAssignments {
        const copy: SourceProfileFileAssignments = Object.assign({}, sourceProfileFileAssignments);

        copy.createdDate = this.dateUtils.toDate(sourceProfileFileAssignments.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(sourceProfileFileAssignments.lastUpdatedDate);
        return copy;
    }
    /**
     * Author : shobha
     * SPA by template id
     */
     fetchSPAByTempId(tempId:any)
    {
        console.log('tempId while spaid is'+tempId)
         return this.http.get(this.spaByTempUrl+'?templateIdForDisplay='+tempId).map((res: Response) => {
             return res.json(); 
         });
    }
     
     /**
      * Author : Rk
      * Get all file assignments for one profile
      */
     getFileAssignments(profId: any): Observable<SourceProfileFileAssignments> {
         return this.http.get(`${this.profileFileAssignmentsUrl}/${profId}`).map((res: Response) => {
             let jsonResponse = res.json();
             return jsonResponse;
         });
     }
     
     fetchUnassignedFileTemplates(obj : any): Observable<FileTemplates[]> {
        // this.convertItemFromServer(obj);
        // console.log('UserData'+JSON.stringify(this.UserData));
         const copy = this.convert(obj);
         //copy['tenantId']=this.UserData.tenantId;
         console.log('service dates'+JSON.stringify(copy));
         return this.http.post(this.unassignedFileTemplatesUrl, copy).map((res: Response) => {
             const jsonResponse = res.json();
             //console.log('fetched templates are'+ JSON.stringify(res));
             return jsonResponse;
         });
         
         /*return this.http.get(this.unassignedFileTemplatesUrl+'?tenantId='+UserData.tenantId+'&startDate='+obj.startDate+'&endDate='+obj.endDate).map((res: Response) => {
             let jsonResponse = res.json();
             console.log('fetchUnassignedFileTemplates:'+jsonResponse);
             return jsonResponse;
         });*/
     }
     
     MoveFilesFromCloudToLocal(spaId:any): Observable<Response> 
     {
         //`${this.resourceUrl}/{tenantId}?tenantId=${UserData.tenantId}`, options
         return this.http.get(this.fetchFilesFromCloudToLocalUrl+'?spfaId='+spaId).map((res: Response) => {
             console.log('after execution'+JSON.stringify(res));
             const jsonResponse = res.json();
             console.log('after execution'+JSON.stringify(jsonResponse));
             return jsonResponse;   
         });
     }
     processFilesFromLocalPath(spaId:any): Observable<Response> 
     {
         console.log('spaId -----'+ spaId);
         return this.http.get(this.ProcessingFilesFromLocalPathUrl+'?spfaId='+spaId).map((res: any) => {
             return res;
         });
     }
     holdTemplate(spaId): Observable<Response> {
         const copy = null;
         return this.http.put(this.holdFileTemplateUrl+'?spaId='+spaId,copy).map((res: Response) => {
             return res;
         });
     }
     unHoldTemplate(spaId): Observable<Response> {
         const copy = null;
         return this.http.put(this.unHoldFileTemplateUrl+'?spaId='+spaId,copy).map((res: Response) => {
             return res;
         });
     }
     
}
