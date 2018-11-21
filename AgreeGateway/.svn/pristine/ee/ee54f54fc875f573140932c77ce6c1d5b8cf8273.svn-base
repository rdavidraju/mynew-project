import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Segments } from './segments.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
@Injectable()
export class SegmentsService {

    private resourceUrl = 'agreeapplication/api/segments';
    private resourceSearchUrl = 'agreeapplication/api/_search/segments';
    private segmentsByCOAUrl = 'agreeapplication/api/getCOAandSegments';
    private segmentsListByCOAURL = 'agreeapplication/api/getSegmentsByCoaId';
    private segmentsByCOAOrderBySequenceUrl = 'agreeapplication/api/getCOAandSegmentsOrderBySequence';
    //private UserData = this.$sessionStorage.retrieve('userData');

    constructor(private http: Http, private dateUtils: JhiDateUtils,
    private $sessionStorage: SessionStorageService) { }

    create(segments: Segments): Observable<Segments> {
        const copy = this.convert(segments);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(segments: Segments): Observable<Segments> {
        const copy = this.convert(segments);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Segments> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
    segmentsByCoa(coa:any): Observable<any> {
        return this.http.get(this.segmentsByCOAUrl + '?id=' +coa).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
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

    private convert(segments: Segments): Segments {
        const copy: Segments = Object.assign({}, segments);

        copy.createdDate = this.dateUtils.toDate(segments.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(segments.lastUpdatedDate);
        return copy;
    }
    
    fetchSegmentsByCOA(coa:any): Observable<Response> {

        return this.http.get(this.segmentsListByCOAURL + '?coaId=' + coa)
            .map((res: Response) => {
                return res.json();
            });

    };
    fetchSegmentsByCOAOrderBySequence(coa:any,qualifier?:any): Observable<Response> {
        console.log('qualifier'+qualifier)
        if(qualifier == undefined)
        qualifier ='';
            return this.http.get(this.segmentsByCOAOrderBySequenceUrl + '?id=' +coa+'&qualifier='+qualifier).map((res: Response) => {
                let jsonResponse: any;
                jsonResponse = res.json();
                console.log('jsonResponse of chart of accs'+JSON.stringify(jsonResponse));
        return jsonResponse;
            });
        }

}
