import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { LedgerDefinition } from './ledger-definition.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LedgerDefinitionService {

    private resourceUrl = 'agreeapplication/api/ledger-definitions';
    private resourceSearchUrl = 'agreeapplication/api/_search/ledger-definitions';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes/';
    private getAllLedgers = 'agreeapplication/api/getAllLedgers';
    private getByTenantIdUrl ='agreeapplication/api/getLedgerDataByTenantId';
    private getByTenantIdAndCOAUrl ='agreeapplication/api/getLedgerDataByTenantIdAndCoa';
    private checkLedgerIsExistUrl:any = 'agreeapplication/api/checkLedgerIsExist';

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }

    create(ledgerDefinition) {
        return this.http.post(this.resourceUrl, ledgerDefinition).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    update(ledgerDefinition) {
        return this.http.put(this.resourceUrl, ledgerDefinition).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    find(id: number): Observable<LedgerDefinition> {
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
        entity.startDate = this.dateUtils
            .convertDateTimeFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertDateTimeFromServer(entity.endDate);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(ledgerDefinition: LedgerDefinition): LedgerDefinition {
        const copy: LedgerDefinition = Object.assign({}, ledgerDefinition);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(ledgerDefinition.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(ledgerDefinition.endDate);

        copy.createdDate = this.dateUtils.toDate(ledgerDefinition.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(ledgerDefinition.lastUpdatedDate);
        return copy;
    }

    /**New API's Start Here */

    /**Get all Ledger Definition by tenantid with pagination */
    getAllLedgerDefinition(page, size){
        return this.http.get(this.getAllLedgers + '?page=' + page + '&per_page=' + size ).map((res: Response)=>{
            return res;
        });
    }

    /**Get Ledger Type for Ledger definition*/
    lookupCodes(lookupType): Observable<any>{
        return this.http.get(this.lookupCodesUrl + lookupType).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Get All Ledger without pagination */
    getLedgersByTenant(): Observable<any>{
        return this.http.get(`${this.getByTenantIdUrl}`)
        .map((res: Response) => {
            return res.json();
        });
    }
    getLedgersByTenantAndCoa(coa): Observable<any> {
        return this.http.get(`${this.getByTenantIdAndCOAUrl}/?coa=${coa}`)
        .map((res: Response) => {
            return res.json();
        });
    }
    
    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkLedgerIsExist(name, id?) {
        return this.http.get(`${this.checkLedgerIsExistUrl}?name=${name}${id ? '&id='+id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
}
