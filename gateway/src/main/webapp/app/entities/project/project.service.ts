import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Project } from './project.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';


@Injectable()
export class ProjectService {

    private resourceUrl = 'agreeapplication/api/projects';
    private resourceSearchUrl = 'agreeapplication/api/_search/projects';
    private acctDataPivotURL = 'agreeapplication/api/AWQPivotingData';
    private UserData = this.$sessionStorage.retrieve('userData');
    constructor(private http: Http, private dateUtils: JhiDateUtils, private $sessionStorage: SessionStorageService) { }

    create(project: Project): Observable<Project> {
        const copy = this.convert(project);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(project: Project): Observable<Project> {
        const copy = this.convert(project);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Project> {
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
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(project: Project): Project {
        const copy: Project = Object.assign({}, project);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(project.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(project.endDate);

        copy.creationDate = this.dateUtils.toDate(project.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(project.lastUpdatedDate);
        return copy;
    }

    getPivotAcctOutput(dataViewId,ruleGrpId, filtersMap: any) {
        return this.http.post( `${this.acctDataPivotURL}/?tenanatId=${this.UserData.tenantId}&dataViewId=${dataViewId}&ruleGrpId=${ruleGrpId}`,filtersMap).map(( res: Response ) =>{
            console.log(res.json());
            return res.json();
        });
    }
}
