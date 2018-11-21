import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { SourceFileInbHistory } from './source-file-inb-history.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SourceFileInbHistoryService {

    private resourceUrl = 'agreeapplication/api/source-file-inb-histories';
    private resourceSearchUrl = 'agreeapplication/api/_search/source-file-inb-histories';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(sourceFileInbHistory: SourceFileInbHistory): Observable<SourceFileInbHistory> {
        const copy = this.convert(sourceFileInbHistory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(sourceFileInbHistory: SourceFileInbHistory): Observable<SourceFileInbHistory> {
        const copy = this.convert(sourceFileInbHistory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<SourceFileInbHistory> {
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
        entity.fileReceivedDate = this.dateUtils
            .convertDateTimeFromServer(entity.fileReceivedDate);
        entity.creationDate = this.dateUtils
            .convertDateTimeFromServer(entity.creationDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(sourceFileInbHistory: SourceFileInbHistory): SourceFileInbHistory {
        const copy: SourceFileInbHistory = Object.assign({}, sourceFileInbHistory);

        copy.fileReceivedDate = this.dateUtils.toDate(sourceFileInbHistory.fileReceivedDate);

        copy.creationDate = this.dateUtils.toDate(sourceFileInbHistory.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(sourceFileInbHistory.lastUpdatedDate);
        return copy;
    }
}
