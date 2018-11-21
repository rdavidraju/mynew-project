import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { AcctRuleConditions } from './acct-rule-conditions.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AcctRuleConditionsService {

    private resourceUrl = 'agreeapplication/api/acct-rule-conditions';
    private resourceSearchUrl = 'agreeapplication/api/_search/acct-rule-conditions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(acctRuleConditions: AcctRuleConditions): Observable<AcctRuleConditions> {
        const copy = this.convert(acctRuleConditions);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(acctRuleConditions: AcctRuleConditions): Observable<AcctRuleConditions> {
        const copy = this.convert(acctRuleConditions);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<AcctRuleConditions> {
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

    private convert(acctRuleConditions: AcctRuleConditions): AcctRuleConditions {
        const copy: AcctRuleConditions = Object.assign({}, acctRuleConditions);

        copy.createdDate = this.dateUtils.toDate(acctRuleConditions.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(acctRuleConditions.lastUpdatedDate);
        return copy;
    }
}
