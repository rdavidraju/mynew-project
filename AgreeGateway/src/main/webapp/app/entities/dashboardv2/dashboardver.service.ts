import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DashboardService {

    private resourceUrl = 'agreeapplication/api/data-view-unions';
    private resourceSearchUrl = 'agreeapplication/api/_search/data-view-unions';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    
}
