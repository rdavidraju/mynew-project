import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { AppRuleConditions } from './app-rule-conditions.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AppRuleConditionsService {

    private resourceUrl = 'agreeapplication/api/app-rule-conditions';
    private resourceSearchUrl = 'agreeapplication/api/_search/app-rule-conditions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(appRuleConditions: AppRuleConditions): Observable<AppRuleConditions> {
        const copy = this.convert(appRuleConditions);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(appRuleConditions: AppRuleConditions): Observable<AppRuleConditions> {
        const copy = this.convert(appRuleConditions);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<AppRuleConditions> {
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

    private convert(appRuleConditions: AppRuleConditions): AppRuleConditions {
        const copy: AppRuleConditions = Object.assign({}, appRuleConditions);

        copy.createdDate = this.dateUtils.toDate(appRuleConditions.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(appRuleConditions.lastUpdatedDate);
        return copy;
    }
}
