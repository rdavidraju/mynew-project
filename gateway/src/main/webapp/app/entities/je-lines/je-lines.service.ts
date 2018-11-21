import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { JeLines } from './je-lines.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JeLinesService {

    private resourceUrl = 'agreeapplication/api/je-lines';
    private resourceSearchUrl = 'agreeapplication/api/_search/je-lines';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(jeLines: JeLines): Observable<JeLines> {
        const copy = this.convert(jeLines);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(jeLines: JeLines): Observable<JeLines> {
        const copy = this.convert(jeLines);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<JeLines> {
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

    private convert(jeLines: JeLines): JeLines {
        const copy: JeLines = Object.assign({}, jeLines);

        copy.createdDate = this.dateUtils.toDate(jeLines.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(jeLines.lastUpdatedDate);
        return copy;
    }
}
