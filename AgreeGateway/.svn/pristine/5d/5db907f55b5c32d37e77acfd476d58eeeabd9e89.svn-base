import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { RuleConditions } from './rule-conditions.model';
import { JhiDateUtils } from 'ng-jhipster';
@Injectable()
export class RuleConditionsService {

    private resourceUrl = 'agreeapplication/api/rule-conditions';
    private resourceSearchUrl = 'agreeapplication/api/_search/rule-conditions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(ruleConditions: RuleConditions): Observable<RuleConditions> {
        let copy: RuleConditions = Object.assign({}, ruleConditions);
        copy.creationDate = this.dateUtils.toDate(ruleConditions.creationDate);
        copy.lastUpdatedDate = this.dateUtils.toDate(ruleConditions.lastUpdatedDate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(ruleConditions: RuleConditions): Observable<RuleConditions> {
        let copy: RuleConditions = Object.assign({}, ruleConditions);

        copy.creationDate = this.dateUtils.toDate(ruleConditions.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(ruleConditions.lastUpdatedDate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<RuleConditions> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.creationDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.creationDate);
            jsonResponse.lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].creationDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].creationDate);
            jsonResponse[i].lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].lastUpdatedDate);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
