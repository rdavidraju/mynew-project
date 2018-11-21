import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Processes } from './processes.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class ProcessesService {
    
    //private UserData = this.$sessionStorage.retrieve('userData');
    private resourceUrl = 'agreeapplication/api/processes';
    private resourceSearchUrl = 'agreeapplication/api/_search/processes';
    /**New Api's */
    /**Get Process */
    private processUrl = 'agreeapplication/api/processessByTenantId';
    private processDetailUrl = 'agreeapplication/api/getprocessesesAndProcessDetails';
    private postProcessUrl = 'agreeapplication/api/processesesAndProcessDetails';
    private processDuplicateUrl = 'agreeapplication/api/processIsExists';
    private getAllcalendarListUrl = 'agreeapplication/api/getAllcalenderList';
    
    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService
    ) { }

    create(processes: Processes): Observable<Processes> {
        const copy = this.convert(processes);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(processes: Processes): Observable<Processes> {
        const copy = this.convert(processes);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Processes> {
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

    private convert(processes: Processes): Processes {
        const copy: Processes = Object.assign({}, processes);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(processes.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(processes.endDate);

        copy.createdDate = this.dateUtils.toDate(processes.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(processes.lastUpdatedDate);
        return copy;
    }

    /**Get Process */
    getProcess(page,size){
        return this.http.get(this.processUrl+`?page=${page}&per_page=${size}`).map((res: Response)=>{
            return res;
        });
    }

    /**Get Process Detail */
    getProcessDetail(id){
        return this.http.get(this.processDetailUrl+`?processIds=${id}`).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Post Process with Detail*/
    postProcess(process){
        return this.http.post(this.postProcessUrl, process).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Check Duplicate Process Name */
    processDuplicate(name, id) {
        let prcsDupUrl = '';
        if (id) {
            prcsDupUrl = `${this.processDuplicateUrl}?name=${name}&id=${id}`;
        } else {
            prcsDupUrl = `${this.processDuplicateUrl}?name=${name}`;
        }
        return this.http.get(prcsDupUrl).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Get Calendar List */
    getAllcalendarList(){
        return this.http.get(this.getAllcalendarListUrl).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
}
