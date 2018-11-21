import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SessionStorageService } from 'ng2-webstorage';
import { Jobs } from './jobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
@Injectable()
export class JobsService {

    private resourceUrl = 'agreeapplication/api/jobs';
    private resourceSearchUrl = 'agreeapplication/api/_search/jobs';
    private sideBarJobsListsUrl = 'agreeapplication/api/JobsListWithProgramName';
    //private UserData = this.$sessionStorage.retrieve('userData');
    
    constructor(private http: Http, private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService) { }

    /** 
     * Author: Sameer(Modified)
    */
    sideBarJobLists(type): Observable<Response> {
        return this.http.get(`${this.sideBarJobsListsUrl}?frequencyType=${type}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    // create(jobs: Jobs): Observable<Jobs> {
    //     const copy = this.convert(jobs);
    //     return this.http.post(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

    // update(jobs: Jobs): Observable<Jobs> {
    //     const copy = this.convert(jobs);
    //     return this.http.put(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

     find(id: number): Observable<Jobs> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            // this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
     }

    // query(req?: any): Observable<ResponseWrapper> {
    //     const options = createRequestOption(req);
    //     return this.http.get(this.resourceUrl, options)
    //         .map((res: Response) => this.convertResponse(res));
    // }

    // delete(id: number): Observable<Response> {
    //     return this.http.delete(`${this.resourceUrl}/${id}`);
    // }

    // search(req?: any): Observable<ResponseWrapper> {
    //     const options = createRequestOption(req);
    //     return this.http.get(this.resourceSearchUrl, options)
    //         .map((res: any) => this.convertResponse(res));
    // }

    // private convertResponse(res: Response): ResponseWrapper {
    //     const jsonResponse = res.json();
    //     for (let i = 0; i < jsonResponse.length; i++) {
    //         this.convertItemFromServer(jsonResponse[i]);
    //     }
    //     return new ResponseWrapper(res.headers, jsonResponse, res.status);
    // }

    // private convertItemFromServer(entity: any) {
    //     entity.creationDate = this.dateUtils
    //         .convertDateTimeFromServer(entity.creationDate);
    //     entity.lastUpdatedDate = this.dateUtils
    //         .convertDateTimeFromServer(entity.lastUpdatedDate);
    // }

    // private convert(jobs: Jobs): Jobs {
    //     const copy: Jobs = Object.assign({}, jobs);

    //     copy.creationDate = this.dateUtils.toDate(jobs.creationDate);

    //     copy.lastUpdatedDate = this.dateUtils.toDate(jobs.lastUpdatedDate);
    //     return copy;
    // }
}
