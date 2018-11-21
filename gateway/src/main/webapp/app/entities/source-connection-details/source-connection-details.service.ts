import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { SourceConnectionDetails } from './source-connection-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
@Injectable()
export class SourceConnectionDetailsService {

    private resourceUrl = 'agreeapplication/api/source-connection-details';
    private connectionListByTenantIdUrl = 'agreeapplication/api/SrcConnectionDetails';
    private sourceConnectionDetailsByTenantIdUrl = 'agreeapplication/api/sourceConnectionDetailsByTenantId';
    private resourceSearchUrl = 'agreeapplication/api/_search/source-connection-details';
    private sonnectionsByTypeURL = 'agreeapplication/api/connectionsByType';
    private srcConnectionsUrl = 'agreeapplication/api/fetchSrcConnectionDetails';
    private fileTemplateListOfDistinctValues='agreeapplication/api/fetchFileTemplateByColumnNameAndTableName';//PathVariable-tableName,columnName
    private fetchAllConnectionTypes ='agreeapplication/api/connectionsAndDisplayColumns';
    private checkSourceConnections='agreeapplication/api/checkSourceConnections';
    private unAssignedSourceProfileList='agreeapplication/api/UnAssignedSourceProfileList';
    private UnAssignedSourceProfilesUrl='agreeapplication/api/UnAssignedSourceProfiles';
    private testConnectionUrl = 'agreeapplication/api/testSourceConnections';
    private connectionsForTenantUrl = 'agreeapplication/api/connectionsForTenant';
    searchData : any ;

    public conToProf='';
    //private UserData = this.$sessionStorage.retrieve('userData')

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(sourceConnectionDetails: SourceConnectionDetails): Observable<SourceConnectionDetails> {
        sourceConnectionDetails.createdDate=null;
        sourceConnectionDetails.lastUpdatedDate=null;
        const copy = this.convert(sourceConnectionDetails);
        return this.http.post(`${this.resourceUrl}`, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(sourceConnectionDetails: SourceConnectionDetails): Observable<SourceConnectionDetails> {
        console.log('while update ' + JSON.stringify(sourceConnectionDetails));
        //const copy = this.convert(sourceConnectionDetails);
        return this.http.put(`${this.resourceUrl}`, sourceConnectionDetails).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<SourceConnectionDetails> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
 
    query(page,limit,sortColumn:any,sortOrder:any): Observable<ResponseWrapper> {
        return this.http.get(this.sourceConnectionDetailsByTenantIdUrl+'?page='+page+'&per_page='+limit+'&sortColName='+sortColumn+'&sortOrder='+sortOrder)
            .map((res: Response) => {
                return res.json();
            });
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
                .convertDateTimeFromServer(entity.startDate);
        entity.endDate = this.dateUtils
                .convertDateTimeFromServer(entity.endDate);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(sourceConnectionDetails: SourceConnectionDetails): SourceConnectionDetails {
        const copy: SourceConnectionDetails = Object.assign({}, sourceConnectionDetails);
        if(sourceConnectionDetails.createdDate){
            copy.createdDate = this.dateUtils.toDate(sourceConnectionDetails.createdDate);
        }
        
        if(sourceConnectionDetails.lastUpdatedDate){
            copy.lastUpdatedDate = this.dateUtils.toDate(sourceConnectionDetails.lastUpdatedDate);
        }
        
        return copy;
    }
    getConnections(type: string): Observable<Response> {
        return this.http.get(`${this.sonnectionsByTypeURL}/${type}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    fetchSourceconnectionsAndDetails(): Observable<Response>
    {
        return this.http.get(`${this.srcConnectionsUrl}`).map((res: Response)=>{
            let jsonResponse = res.json();
            return jsonResponse;
        }); 
    }
    
    groupSideBarByCols(tableName : any, columnName : any):Observable<Response>
    {
        console.log('group side bar by table name and columns'+tableName+'and'+columnName);
        return this.http.get(`${this.fileTemplateListOfDistinctValues}/${tableName}/${columnName}`).map((res:Response)=>
        {
           console.log('response in grp service'+res.json()); 
            return res.json();
        });
    }
    /**
     * Author : Shobha
     */
    fetchConnections() :Observable<Response>
    {
         return this.http.get(`${this.fetchAllConnectionTypes}/${'CONNECTION_TYPE'}`).map((res: Response)=>{
            let jsonResponse = res.json();
            return jsonResponse;
        });
    
    }
    getAllSourceProfilesList(stdt, endt): Observable<Response> {
       
        
       // console.log('tenant id :' + this.UserData.tenantId);
        return this.http.get( `${this.unAssignedSourceProfileList}/${stdt}?endDate=${endt}` ).map(( res: Response ) => res );
     }
   
    
    connectionListsFunc() :Observable<Response>
    {
        
         return this.http.get(`${this.connectionListByTenantIdUrl}`).map((res: Response)=>{
            let jsonResponse = res.json();
            //console.log('onload' + JSON.stringify(jsonResponse));
            return jsonResponse;
            
        });
    
    }

    getAllSourceProfiles(conId:any): Observable<Response> {
      // obj['tenantId'] = this.UserData.tenantId;
      console.log('unassigned profile get has '+conId);
        return this.http.get( this.UnAssignedSourceProfilesUrl+'?conId'+conId ).map(( res: Response ) => res );
     }
    //?tenantId=4&connectionId=1056&startDate=2017-08-01
     getAllSourceProfilesDetails(obj:any): Observable<Response> {
         
         console.log('obj ' + JSON.stringify(obj));
         return this.http.get( `${this.UnAssignedSourceProfilesUrl}/?connectionId=${obj.id}&startDate=${obj.startDate}&endDate=${obj.endDate}` ).map(( res: Response ) => res );
      }
      testConenction(sourceConnectionDetails)
      {
          return this.http.post(this.testConnectionUrl,sourceConnectionDetails).map((res: Response) => {
              return res.json();
          });
      }
      search(req?: any): Observable<ResponseWrapper> {
        let options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl+'?filterValue='+this.searchData, options)
            .map((res: any) => this.convertResponse(res))
            ;
    }
    connectionsForTenant(): Observable<Response> {
        return this.http.get( `${this.connectionsForTenantUrl}` ).map((res: Response)=>{
            let jsonResponse = res.json();
            return jsonResponse;
            
        });
     }
}
