import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ChartOfAccount } from './chart-of-account.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class ChartOfAccountService {

   // private UserData = this.$sessionStorage.retrieve('userData');
    private resourceUrl = 'agreeapplication/api/chart-of-accounts';
    private resourceSearchUrl = 'agreeapplication/api/_search/chart-of-accounts';
    private getAllChartOfAccountsUrl = 'agreeapplication/api/getAllChartOfAccounts';
    private getCOAandSegmentsUrl = 'agreeapplication/api/getCOAandSegments?id=';
    private deleteOrUpdateOrNewsegmentsUrl = 'agreeapplication/api/segments';
    private postCOAandSegmentsUrl = 'agreeapplication/api/postCOAandSegments';

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService
    ) { }

    create(chartOfAccount: ChartOfAccount): Observable<ChartOfAccount> {
        const copy = this.convert(chartOfAccount);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(chartOfAccount: ChartOfAccount): Observable<ChartOfAccount> {
        const copy = this.convert(chartOfAccount);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ChartOfAccount> {
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
            .convertLocalDateFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(entity.endDate);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(chartOfAccount: ChartOfAccount): ChartOfAccount {
        const copy: ChartOfAccount = Object.assign({}, chartOfAccount);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(chartOfAccount.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(chartOfAccount.endDate);

        copy.createdDate = this.dateUtils.toDate(chartOfAccount.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(chartOfAccount.lastUpdatedDate);
        return copy;
    }

    /**New API's start here */
    /**Get All chart of account by tenant and with pagination */
    getAllChartOfAccounts(page, size, activeFlag){
        return this.http.get(this.getAllChartOfAccountsUrl+'?page='+page+'&per_page='+size+'&activeFlag='+activeFlag).map((res: Response)=>{
            return res;
        });
    }

    /**Get chart of account and segment details by id */
    getCOAandSegments(id){
        return this.http.get(this.getCOAandSegmentsUrl+id).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Update or create new segments */
    updateOrNewsegments(segment){
        return this.http.put(this.deleteOrUpdateOrNewsegmentsUrl, segment).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Delete segment*/
    deleteSegment(id){
        return this.http.delete(this.deleteOrUpdateOrNewsegmentsUrl+'/'+id).map((res: Response)=>{
            /* const jsonResponse = res.json();
            return jsonResponse; */
        });
    }

    /**Update COA */
    updateCOA(CoaDetails){
        return this.http.put(this.resourceUrl, CoaDetails).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Create New COA with segment*/
    postCOAandSegments(CoaDetails){
        return this.http.post(this.postCOAandSegmentsUrl, CoaDetails).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Get All chart of accounts without pagination */
    getChartOfAccountsByTenant(): Observable<any> {
        return this.http.get(this.getAllChartOfAccountsUrl).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkCOAIsExistUrl:any = 'agreeapplication/api/checkCOAIsExist';
    checkCOAIsExist(name, id?) {
        return this.http.get(`${this.checkCOAIsExistUrl}?name=${name}${id ? '&id='+id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

}
