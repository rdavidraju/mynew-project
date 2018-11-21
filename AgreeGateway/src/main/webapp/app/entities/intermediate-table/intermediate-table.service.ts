import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { IntermediateTable } from './intermediate-table.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class IntermediateTableService {

    private resourceUrl = 'agreeapplication/api/intermediate-tables';
    private resourceSearchUrl = 'agreeapplication/api/_search/intermediate-tables';
    private fetchIntermediateRecordsUrl = 'agreeapplication/api/fetchIntermediateTablesByTemplateId';
   
    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(intermediateTable: IntermediateTable): Observable<IntermediateTable> {
        const copy = this.convert(intermediateTable);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(intermediateTable: IntermediateTable): Observable<IntermediateTable> {
        const copy = this.convert(intermediateTable);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<IntermediateTable> {
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

    private convert(intermediateTable: IntermediateTable): IntermediateTable {
        const copy: IntermediateTable = Object.assign({}, intermediateTable);

        copy.creationDate = this.dateUtils.toDate(intermediateTable.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(intermediateTable.lastUpdatedDate);
        return copy;
    }
    getIntermediateRecords(templateId): Observable<any> {
        return this.http.get(this.fetchIntermediateRecordsUrl + '?templateId=' + templateId).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
               let data :String = '';
         if(jsonResponse)
                {
                
                jsonResponse.forEach(interRecord=>{
                    if(interRecord && interRecord.data)
                        {
                        /*var fixedResponse = interRecord.data.replace(/\\'/g, "'");  
                        var jsonObj = JSON.parse(fixedResponse); 
                        interRecord.data = jsonObj;*/
                        }
                })
                }
            
    return jsonResponse;
        });
    }
}
