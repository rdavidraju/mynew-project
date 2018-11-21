import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JobActions } from './job-actions.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JobActionsService {

    private resourceUrl = 'agreeapplication/api/job-actions';
    private resourceSearchUrl = 'agreeapplication/api/_search/job-actions';

    constructor(private http: Http) { }

    create(jobActions: JobActions): Observable<JobActions> {
        const copy = this.convert(jobActions);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(jobActions: JobActions): Observable<JobActions> {
        const copy = this.convert(jobActions);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<JobActions> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(jobActions: JobActions): JobActions {
        const copy: JobActions = Object.assign({}, jobActions);
        return copy;
    }
}
