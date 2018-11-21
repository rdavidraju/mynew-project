import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Jobs } from './jobs.model';

@Injectable()
export class JobsService {

    private resourceUrl = 'agreeapplication/api/jobs';
    private resourceSearchUrl = 'agreeapplication/api/_search/jobs';
    private sideBarJobsListsUrl = 'agreeapplication/api/JobsListWithProgramName';
    public frequencyType = 'SCHEDULED';
    
    constructor(private http: Http) { }

    /** 
     * Author: Sameer(Modified)
    */
    sideBarJobLists(type): Observable<Response> {
        return this.http.get(`${this.sideBarJobsListsUrl}?frequencyType=${type}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

     find(id: number): Observable<Jobs> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
     }
}
