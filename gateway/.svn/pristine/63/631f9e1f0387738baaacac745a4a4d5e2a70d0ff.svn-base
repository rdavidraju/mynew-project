import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions, ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { MappingSet } from './mapping-set.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MappingSetService {
    private resourceUrl = 'agreeapplication/api/mapping-sets';
    private deleteMappingSetValueUrl =  'agreeapplication/api/mapping-set-values';
    private resourceSearchUrl = 'agreeapplication/api/_search/mapping-sets';
    private mappingSetsByTenantIdUrl = 'agreeapplication/api/mappingSetsByTenantId'; 
    private activeMappingSetsByTenant = 'agreeapplication/api/getActiveMappingSetsByTenantId';
    private getAllMappingSetsByTenantId = 'agreeapplication/api/getMappingSetsByTenantId';
    private postingMappingSetsWithValues = 'agreeapplication/api/postingMappingSetAndValues';
    private getMappingSetsWithValuesUrl = 'agreeapplication/api/fetchMappingSetAndValuesById';
    private updateMappingSetUrl = 'agreeapplication/api/createOrUpdateMappingSet';
    private updateMappingSetValuesUrl = 'agreeapplication/api/createOrUpdateMappingSetValues';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes/';
    private getListMappingSetsByTenantIdWithPaginationUrl = 'agreeapplication/api/getMappingSetsByTenantId';
    private getMappingSetsNValuesListByPurposeUrl = 'agreeapplication/api/fetchMappingSetAndValuesByPurpose';
    private bulkUploadUrl = 'agreeapplication/api/bulkUploadForMappingSet';
    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }

    create(mappingSet: MappingSet): Observable<MappingSet> {
        const copy = this.convert(mappingSet);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(mappingSet: MappingSet): Observable<MappingSet> {
        const copy = this.convert(mappingSet);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<MappingSet> {
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
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
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

    private convert(mappingSet: MappingSet): MappingSet {
        const copy: MappingSet = Object.assign({}, mappingSet);
        copy.createdDate = this.dateUtils.toDate(mappingSet.createdDate);
        copy.lastUpdatedDate = this.dateUtils.toDate(mappingSet.lastUpdatedDate);
        return copy;
    }
    getMappingSetsByTenantId(): Observable<MappingSet> {
        return this.http.get(`${this.mappingSetsByTenantIdUrl}`).map((res: Response) => {
            return res.json();
        });
    }
    fetchActiveMappingSetsByTenant():Observable<MappingSet>{
        return this.http.get(`${this.activeMappingSetsByTenant}`).map((res: Response) => {
            return res.json();
        });
    }

    getListMappingSetsByTenantId(): Observable<any>{
        return this.http.get(this.getAllMappingSetsByTenantId ).map((res: Response) => {
            return res;
        });
    }

    postMappingSetsWithValues(mappingSetsWithValues): Observable<any>{
        return this.http.post(this.postingMappingSetsWithValues,mappingSetsWithValues).map((res: Response) => {
            return res.json();
        });
    }

    getMappingSetsWithValues(id, fileExport?, fileType?): Observable<any>{
        if(fileExport){
            return this.http.get(`${this.getMappingSetsWithValuesUrl}?id=${id}${fileExport ? `&fileExport=${fileExport}` : ''}${fileExport ? `&fileType=${fileType}` : ''}`,{ responseType: ResponseContentType.Blob }).map((res: Response) => {
                return fileExport ? res : res.json();
            });
        }else{
            return this.http.get(`${this.getMappingSetsWithValuesUrl}?id=${id}${fileExport ? `&fileExport=${fileExport}` : ''}${fileExport ? `&fileType=${fileType}` : ''}`).map((res: Response) => {
                return fileExport ? res : res.json();
            });
        }
        
    }

    updateMappingSet(mappingSet): Observable<any>{
        return this.http.put(this.updateMappingSetUrl, mappingSet).map((res: Response) => {
            return res.json();
        });
    }

    updateMappingSetValues(mappingsetValues): Observable<any>{
        return this.http.put(this.updateMappingSetValuesUrl, mappingsetValues).map((res: Response) => {
            return res.json();
        })
    }

    lookupCodes(lookupType): Observable<any>{
        return this.http.get(this.lookupCodesUrl + lookupType ).map((res: Response)=>{
            return res.json();
        })
    }

    getListMappingSetsByTenantIdWithPagination(page, size, purpose?): Observable<any>{
        return this.http.get(`${this.getListMappingSetsByTenantIdWithPaginationUrl}?pageNumber=${page}&pageSize=${size}${purpose ? `&purpose=${purpose}` : ''}`).map((res: Response)=>{
            return res;
        });
    }

    /**Get Mapping Set and Values by purpose */
    getMappingSetsNValuesListByPurpose(purpose){
        return this.http.get(this.getMappingSetsNValuesListByPurposeUrl+'?purpose='+purpose).map((res: Response)=>{
            return res.json();
        });
    }
    deleteMappingSetValue(id: number): Observable<Response> {
        return this.http.delete(`${this.deleteMappingSetValueUrl}/${id}`);
    }

    bulkUpload(file) {
        if (file) {
            const formData = new FormData();
            formData.append('multipart', file);
            const headers = new Headers({});
            const options = new RequestOptions({ headers });
            return this.http.post(this.bulkUploadUrl, formData, options).map((res: Response) => {
                return res.json();
            });
        }
    }

}
