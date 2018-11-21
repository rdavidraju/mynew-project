import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ApplicationPrograms } from './application-programs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ApplicationProgramsService {

    private resourceUrl = 'agreeapplication/api/application-programs';
    private resourceSearchUrl = 'agreeapplication/api/_search/application-programs';
    private jobsListWithProgramName='agreeapplication/api/JobsListWithProgramName';//tenantId parameter
    

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(applicationPrograms: ApplicationPrograms): Observable<ApplicationPrograms> {
        const copy = this.convert(applicationPrograms);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(applicationPrograms: ApplicationPrograms): Observable<ApplicationPrograms> {
        const copy = this.convert(applicationPrograms);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<ApplicationPrograms> {
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
        entity.lastUpdationDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdationDate);
    }

    private convert(applicationPrograms: ApplicationPrograms): ApplicationPrograms {
        const copy: ApplicationPrograms = Object.assign({}, applicationPrograms);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(applicationPrograms.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(applicationPrograms.endDate);

        copy.creationDate = this.dateUtils.toDate(applicationPrograms.creationDate);

        copy.lastUpdationDate = this.dateUtils.toDate(applicationPrograms.lastUpdationDate);
        return copy;
    }
}
