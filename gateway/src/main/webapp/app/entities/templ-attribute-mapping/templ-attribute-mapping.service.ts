import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { TemplAttributeMapping } from './templ-attribute-mapping.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TemplAttributeMappingService {

    private resourceUrl = 'agreeapplication/api/templ-attribute-mappings';
    private resourceSearchUrl = 'agreeapplication/api/_search/templ-attribute-mappings';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(templAttributeMapping: TemplAttributeMapping): Observable<TemplAttributeMapping> {
        const copy = this.convert(templAttributeMapping);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(templAttributeMapping: TemplAttributeMapping): Observable<TemplAttributeMapping> {
        const copy = this.convert(templAttributeMapping);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<TemplAttributeMapping> {
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

    private convert(templAttributeMapping: TemplAttributeMapping): TemplAttributeMapping {
        const copy: TemplAttributeMapping = Object.assign({}, templAttributeMapping);

        copy.createdDate = this.dateUtils.toDate(templAttributeMapping.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(templAttributeMapping.lastUpdatedDate);
        return copy;
    }
}
