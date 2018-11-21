import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { Jobs, Frequencies } from '../jobs/jobs.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class TaggingService {
    private processUrl = 'agreeapplication/api/processessByTenantId';
    private processDetailUrl = 'agreeapplication/api/getprocessesesAndProcessDetails';
    private postProcessUrl = 'agreeapplication/api/processesesAndProcessDetails';
   // private UserData = this.$sessionStorage.retrieve('userData');
    private taggingPostURL = 'agreeapplication/api/processDetailsList';

    constructor(
        private http: Http, 
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService) 
        {

        }

        getProcess(page:number,size:number):Observable<Response>{
            return this.http.get(this.processUrl+'?page='+page + '&per_page=' + size ).map((res: Response)=>{
                return res.json();
            });
        }
    
        /**Get Process Detail */
        getProcessDetail(ids:any):Observable<Response>{
            return this.http.get(this.processDetailUrl+'?processIds=' + ids).map((res: Response)=>{
               return res.json();
            });
        }
    
        /**Post Process with Detail*/
        postProcess(process:any):Observable<Response>{
            return this.http.post(this.postProcessUrl,process).map((res: Response)=>{
                return res.json();
            });
        }

        postProcessForTagging(processDetailsList:any):Observable<Response>{
            return this.http.post(this.taggingPostURL ,processDetailsList).map((res: Response)=>{
                return res.json();
            });
        }
   
    
}
