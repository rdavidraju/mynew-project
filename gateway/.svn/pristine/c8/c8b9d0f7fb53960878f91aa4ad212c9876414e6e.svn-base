import { Injectable, Component } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Response, Headers, RequestOptions } from '@angular/http';
@Injectable()
export class BulkUploadService {

    private getDomainNameToTableNameMapUrl='agreeapplication/api/domainNameToTableNameMap';
    private postRecordsUrl = 'agreeapplication/api/postRecords';
    constructor(
        private http: Http
    ) {}
    getDomainNameToTableNameMap(domainName: any) {
		return this.http.get(`${this.getDomainNameToTableNameMapUrl}?className=${domainName}`).map((res: Response) => {
			const jsonResponse = res.json();
			return jsonResponse;
		});
    }
    	postRecords(queriesList): Observable<Response> {
		//console.log('post records') ;     
		return this.http.post(this.postRecordsUrl, queriesList).map((res: Response) => {
			let jsonResponse = res.json();
			//   console.log('jsonResponse of queries'+JSON.stringify(jsonResponse));
			return jsonResponse;
		});
	}
}