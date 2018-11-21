import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { AccountingLineTypes } from './accounting-line-types.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AccountingLineTypesService {

    private resourceUrl = 'agreeapplication/api/accounting-line-types';
    private resourceSearchUrl = 'agreeapplication/api/_search/accounting-line-types';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(accountingLineTypes: AccountingLineTypes): Observable<AccountingLineTypes> {
        const copy = this.convert(accountingLineTypes);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(accountingLineTypes: AccountingLineTypes): Observable<AccountingLineTypes> {
        const copy = this.convert(accountingLineTypes);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<AccountingLineTypes> {
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
        entity.creationDate = this.dateUtils
            .convertDateTimeFromServer(entity.creationDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(accountingLineTypes: AccountingLineTypes): AccountingLineTypes {
        const copy: AccountingLineTypes = Object.assign({}, accountingLineTypes);

        copy.creationDate = this.dateUtils.toDate(accountingLineTypes.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(accountingLineTypes.lastUpdatedDate);
        return copy;
    }
}
