import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { LineCriteria } from './line-criteria.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LineCriteriaService {

    private resourceUrl = 'agreeapplication/api/line-criteria';
    private resourceSearchUrl = 'agreeapplication/api/_search/line-criteria';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(lineCriteria: LineCriteria): Observable<LineCriteria> {
        const copy = this.convert(lineCriteria);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(lineCriteria: LineCriteria): Observable<LineCriteria> {
        const copy = this.convert(lineCriteria);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<LineCriteria> {
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

    private convert(lineCriteria: LineCriteria): LineCriteria {
        const copy: LineCriteria = Object.assign({}, lineCriteria);

        copy.createdDate = this.dateUtils.toDate(lineCriteria.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(lineCriteria.lastUpdatedDate);
        return copy;
    }
}
