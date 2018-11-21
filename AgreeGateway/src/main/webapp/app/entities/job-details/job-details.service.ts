import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { JobDetails } from './job-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobDetailsService {

    private resourceUrl = 'agreeapplication/api/job-details';
    private resourceSearchUrl = 'agreeapplication/api/_search/job-details';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(jobDetails: JobDetails): Observable<JobDetails> {
        const copy = this.convert(jobDetails);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(jobDetails: JobDetails): Observable<JobDetails> {
        const copy = this.convert(jobDetails);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<JobDetails> {
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
        entity.creationDate = this.dateUtils
            .convertDateTimeFromServer(entity.creationDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(jobDetails: JobDetails): JobDetails {
        const copy: JobDetails = Object.assign({}, jobDetails);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(jobDetails.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(jobDetails.endDate);

        copy.creationDate = this.dateUtils.toDate(jobDetails.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(jobDetails.lastUpdatedDate);
        return copy;
    }
}
