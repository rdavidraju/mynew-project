import { Injectable } from '@angular/core';
import { Http, Response,Headers,RequestOptions, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { FileTemplateLines } from './file-template-lines.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FileTemplateLinesService {

    private resourceUrl = 'agreeapplication/api/file-template-lines';
    private resourceSearchUrl = 'agreeapplication/api/_search/file-template-lines';
    private templateLinesByTempIdUrl = 'agreeapplication/api/fetchTempLinesByTempId';
    private postTempLinesUrl = 'agreeapplication/api/postTemplateLines';
    private fetchMIFileTemplateLinesUrl = 'agreeapplication/api/fetchTempLinesByTempIdOrIntermediateIds';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(fileTemplateLines: FileTemplateLines): Observable<FileTemplateLines> {
        const copy = this.convert(fileTemplateLines);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(fileTemplateLines: FileTemplateLines): Observable<FileTemplateLines> {
        const copy = this.convert(fileTemplateLines);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<FileTemplateLines> {
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
            .map((res: any) => this.convertResponse(res))
        ;
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

    private convert(fileTemplateLines: FileTemplateLines): FileTemplateLines {
        const copy: FileTemplateLines = Object.assign({}, fileTemplateLines);

        copy.createdDate = this.dateUtils.toDate(fileTemplateLines.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(fileTemplateLines.lastUpdatedDate);
        return copy;
    }
    fetchTemplateLinesByTemplateId(templateId:any):Observable<ResponseWrapper>
    {
         return this.http.get(`${this.templateLinesByTempIdUrl}/${templateId}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    postTemplateLines(lines:any): Observable<Response> {
        console.log('save template lines'+JSON.stringify(lines));
         let formData = new FormData();
       formData.append('fileTemplateLines', lines);
         let headers = new Headers({});
       let options = new RequestOptions({ headers });
        return this.http.post(this.postTempLinesUrl, formData, options).map((res: Response) => {
            return res.json();
        });
    }
    fetchFTLByTempIdAndInterIDs(templateId,intermediateIds): Observable<any> {
        return this.http.get(this.fetchMIFileTemplateLinesUrl + '?templateId=' + templateId+'&intermediateIds='+intermediateIds).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }
}
