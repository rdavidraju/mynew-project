import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { Jobs, Frequencies } from '../jobs/jobs.model';
import { OozieStatus } from '../reports/reports.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { NotificationsService } from 'angular2-notifications-lite';

@Injectable()
export class SchedulingService {
    
    private postJobUrl = 'agreeapplication/api/postingJobDetails';
    private isJobNameAvailableUrl = 'agreeapplication/api/isJobNameAvailable';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes';
    private testOozie='agreeapplication/api/testOozieStatus';
    constructor(private http: Http, private dateUtils: JhiDateUtils,
                private notificationsService: NotificationsService,) { }

     postJobDetails(currentJobDetails : any) : Observable<Response>
     {   
        return this.http.post(`${this.postJobUrl}`, currentJobDetails).map((res: Response) => {
            return res;
        });
     }

     checkJobNameAvailable(jobName: string){
        return this.http.get( `${this.isJobNameAvailableUrl}/?jobName=${jobName}` ).map(( res: Response ) =>{
            return res.json();
        });
     }
    
    // ******* Get lookup values based on lookup code @Rk *************
    getLookupValues(lookupType: string) {
        return this.http.get( `${this.lookupCodesUrl}/${lookupType}` ).map(( res: Response ) =>{return res.json();  });
    }

    testOozieServer(){
        return this.http.get(this.testOozie).map((res:Response)=>{
            return res.json();
        })
    }
}
