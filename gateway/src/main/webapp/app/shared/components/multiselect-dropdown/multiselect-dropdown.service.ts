import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SessionStorageService } from 'ng2-webstorage';
@Injectable()
export class MultiSelectDropdownService {
    constructor(private http: Http, private dateUtils: JhiDateUtils,private $sessionStorage: SessionStorageService) { }


}
