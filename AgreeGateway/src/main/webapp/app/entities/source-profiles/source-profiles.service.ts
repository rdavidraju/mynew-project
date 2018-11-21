import { Injectable } from '@angular/core';
import { Http, Headers, Response, URLSearchParams, RequestOptions, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { SourceProfiles } from './source-profiles.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
@Injectable()
export class SourceProfilesService {

    private resourceUrl = 'agreeapplication/api/sourceProfilesByTenantId';
    private sourceProfileUrl='agreeapplication/api/source-profiles';
    private resourceSearchUrl = 'agreeapplication/api/_search/source-profiles';
    private sourceProfilesUrl = 'agreeapplication/api/fetchSourceProfiles';
    private sourceProfilesWithConnectionDetails='agreeapplication/api/connectionDetails';
    private postSrcProfileUrl = 'agreeapplication/api/sourceProfilesAndProfileAssignments';
    private fetchProfilesByTenantUrl = 'agreeapplication/api/sourceProfilesByTenantIdwithSort';
    private fetchActiveRecords='agreeapplication/api/fetchActiveRecords';//@pathvariable tenantId,startdate,tablename
   // private UserData = this.$sessionStorage.retrieve('userData');
    private fetchSPWithConDetailsUrl = 'agreeapplication/api/sourceProfileswithDetailInfo';
    public searchData:any;
    constructor(private http: Http, private dateUtils: JhiDateUtils, 
    private $sessionStorage: SessionStorageService) { }

    create(sourceProfiles: SourceProfiles): Observable<SourceProfiles> {
        const copy = this.convert(sourceProfiles);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(sourceProfiles: SourceProfiles): Observable<SourceProfiles> {
        const copy = this.convert(sourceProfiles);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<SourceProfiles> {
        return this.http.get(`${this.sourceProfileUrl}/${id}`).map((res: Response) => {
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
    search(req?: any): Observable<ResponseWrapper> {
        let options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl+'?filterValue='+this.searchData, options)
        .map((res: any) => this.convertResponse(res))
        ;
    }
    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
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

    private convert(sourceProfiles: SourceProfiles): SourceProfiles {
        const copy: SourceProfiles = Object.assign({}, sourceProfiles);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(sourceProfiles.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(sourceProfiles.endDate);

        copy.createdDate = this.dateUtils.toDate(sourceProfiles.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(sourceProfiles.lastUpdatedDate);
        return copy;
    }
     /**
     * Author : Rk
     * get Profiles and Connections info by profile ID
     */
    getProfilesAndConnectionsByProfileId( Id: any ): Observable<Response> {
       // console.log('tenant id' + this.UserData.tenantId);
       // console.log('id' + Id);
        return this.http.get( `${this.sourceProfilesWithConnectionDetails}/?Id=${Id}` ).map(( res: Response ) => {
            let jsonResponse = res.json();
           // console.log('jsonResponse;;;;'+JSON.stringify(jsonResponse));
            jsonResponse = jsonResponse[0];
            jsonResponse.startDate = this.dateUtils
               .convertLocalDateFromServer(jsonResponse.startDate);
           jsonResponse.endDate = this.dateUtils
               .convertLocalDateFromServer(jsonResponse.endDate);
           return jsonResponse;
        } );
    }
    fetchProfilesForTenant( offset: any ,limit,sortColName?:any,sortOrder?:any): Observable<Response> {
        if(!sortColName || sortColName == undefined || sortColName == '' || sortColName == null)
        {
            return this.http.get( this.fetchProfilesByTenantUrl+'?page='+offset+'&per_page='+limit ).map(( res: Response ) => {
                let jsonResponse = res.json();
                console.log('jsonResponsesh'+JSON.stringify(jsonResponse));
                return jsonResponse;
            });
        }
        else{
            return this.http.get( this.fetchProfilesByTenantUrl+'?page='+offset+'&per_page='+limit+'&sortColName='+sortColName+'&sortOrder='+sortOrder ).map(( res: Response ) => {
                let jsonResponse = res.json();
                console.log('jsonResponsesh'+JSON.stringify(jsonResponse));
                return jsonResponse;
            });
        }
      
    }

    /**
     * Author : Rk
     * get all Profiles and Connections info
     */
    getAllProfilesAndConnections(): Observable<Response> {
        //console.log('tenant id :' + this.UserData);
        return this.http.get( `${this.sourceProfilesWithConnectionDetails}` ).map(( res: Response ) => res );
     }

    /**
     * Author : Shobha
     * get source profiles by tenant id
     */
    getAllSourceProfiles(sourceProfile : SourceProfiles ):Observable<SourceProfiles>{
       // sourceProfile.tenantId=this.UserData.tenantId;
        return this.http.get(this.sourceProfilesUrl).map((res: Response) => {
            return res.json();
        });
        /*  return this.http.get(`${this.sourceProfilesUrl}/${this.UserData.tenantId}`) .map((res: any) => 
          {
        return  res.json();     
          })
        ;*/
    }
    /**
     * Author : Shobha
     */
    postSrcProfileAndAssignments(srcProfilesAndAssignments : any) : Observable<Response>
    {   
        //srcProfilesAndAssignments.sourceProfiles.tenantId = this.UserData.tenantId;
        let copy: any = Object.assign({}, srcProfilesAndAssignments);
        /*copy.sourceProfiles.startDate = this.dateUtils
            .convertLocalDateToServer(srcProfilesAndAssignments.sourceProfiles.startDate);
        copy.sourceProfiles.endDate = this.dateUtils
            .convertLocalDateToServer(srcProfilesAndAssignments.sourceProfiles.endDate);*/
        console.log('copy'+JSON.stringify(copy));
        return this.http.post(`${this.postSrcProfileUrl}`, copy).map((res: Response) => {
            //source-profiles/{userId}?userId=1
            //`${this.postSrcProfileUrl}/{userId}?userId=${UserData.id}`
            //sourceProfilesAndProfileAssignments?userId=1
            return res.json();
        });
    }
    getSourceProfilesBasedOnStDateAndEndDate(stDate :  any, endDate : any): Observable<Response>
    {
        console.log('start date:'+stDate+'end date'+endDate);
        return this.http.get(this.fetchActiveRecords+'?startDate='+stDate+'&endDate='+endDate+'&tableName='+'SourceProfiles').map((res: Response)=>
             {
                    return res.json();
                });
        
    }
    fetchSourceProfilesAndconDetails(status:boolean): Observable<Response> {
        return this.http.get(this.fetchSPWithConDetailsUrl+'?status='+status).map((res: Response) => {
            return res.json();
        });
    }

}
