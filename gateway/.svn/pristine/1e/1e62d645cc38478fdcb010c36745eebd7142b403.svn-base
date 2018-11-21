import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { LookUpCode } from './look-up-code.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LookUpCodeService {

    private resourceUrl = 'agreeapplication/api/look-up-codes';
    private resourceSearchUrl = 'agreeapplication/api/_search/look-up-codes';
    private LookUpCodesByLookUpMeanings='agreeapplication/api/lookup-codes';
    private lookupTypeUrl = 'agreeapplication/api/look-up-types';
    private getAllLookupsUrl = 'agreeapplication/api/getALlLookUpCodes';
    private bulkUploadUrl = 'agreeapplication/api/lookUpsBulkUpload';
    private varcharDecimalDatelookUpsByLookUpTypeUrl='agreeapplication/api/lookUpTypeToLookUpCodeMap';
    private accountingRuleLookUpsUrl ='agreeapplication/api/accLookUpTypeToLookUpCodeMap';
    private checkLookUpCodeIsExistUrl = 'agreeapplication/api/checkLookUpCodeIsExist';
    private getDomainNameToTableNameMapUrl='agreeapplication/api/domainNameToTableNameMap';
    private getLookupTypeDetailsByTypeUrl = 'agreeapplication/api/getlookUpTypeByType';
    
    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }

    create(lookUpCode: LookUpCode): Observable<LookUpCode> {
        return this.http.post(this.resourceUrl, lookUpCode).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(lookUpCode: LookUpCode): Observable<LookUpCode> {
        return this.http.put(this.resourceUrl, lookUpCode).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<LookUpCode> {
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
        entity.activeStartDate = this.dateUtils
            .convertLocalDateFromServer(entity.activeStartDate);
        entity.activeEndDate = this.dateUtils
            .convertLocalDateFromServer(entity.activeEndDate);
        entity.creationDate = this.dateUtils
            .convertDateTimeFromServer(entity.creationDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(lookUpCode: LookUpCode): LookUpCode {
        const copy: LookUpCode = Object.assign({}, lookUpCode);
        copy.activeStartDate = this.dateUtils
            .convertLocalDateToServer(lookUpCode.activeStartDate);
        copy.activeEndDate = this.dateUtils
            .convertLocalDateToServer(lookUpCode.activeEndDate);

        copy.creationDate = this.dateUtils.toDate(lookUpCode.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(lookUpCode.lastUpdatedDate);
        return copy;
    }

    /**
     * Author : Shobha
     * @param lookUpType
     */
     fetchLookUpsByLookUpType(lookUpType: any): Observable<Response>{
         if(lookUpType) {
             return this.http.get( `${this.LookUpCodesByLookUpMeanings}/${lookUpType}`).map(( res: Response ) => {
                 const jsonResponse = res.json();
                return jsonResponse;
             } );
         }
     }

    /**Get all Lookup Codes by tenantid with pagination */
    getAllLookupCodesPagination(page, size){
        return this.http.get(this.resourceUrl + '?page=' + page + '&size=' + size).map((res: Response)=>{
            return res;
        });
    }

    /**Get all Lookup Types by tenantId */
    getAllLookupTypes(){
        return this.http.get(this.lookupTypeUrl).map((res)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }


    getAllLookups(type) {
        return this.http.get(`${this.getAllLookupsUrl}/${type}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkLookUpCodeIsExist(type, code, id?) {
        return this.http.get(`${this.checkLookUpCodeIsExistUrl}?type=${type}&code=${code}${id ? '&id='+id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Bulk Upload Data */
    bulkUpload(file) {
        if (file) {
            const formData = new FormData();
            formData.append('multipart', file);
            const headers = new Headers({});
            const options = new RequestOptions({ headers });
            return this.http.post(this.bulkUploadUrl, formData, options).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }

    /**
     * Author : Shobha
     */
    varcharDecimalDatelookUpsByLookUpType(): Observable<Response>{
        return this.http.get( this.varcharDecimalDatelookUpsByLookUpTypeUrl).map(( res: Response ) => {
            const jsonResponse = res.json();
           return jsonResponse;
        } );
    }

     /**
     * Author : Shobha
     */
    accountingRuleLookUps(): Observable<Response>{
        return this.http.get( this.accountingRuleLookUpsUrl).map(( res: Response ) => {
            const jsonResponse = res.json();
           return jsonResponse;
        } );
    }

    getLookupTypeDetailsByType(type) {
        return this.http.get(`${this.getLookupTypeDetailsByTypeUrl}?lookUpType=${type}`).map(( res: Response ) => res.json() );
    }
   
}
