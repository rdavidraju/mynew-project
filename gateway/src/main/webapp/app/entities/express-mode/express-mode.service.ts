import { Injectable } from '@angular/core';
import { Http, Response, RequestOptions, BaseRequestOptions, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class ExpressModeService {

   // private UserData = this.sessionStorage.retrieve('userData');
    private fetchSampleDataByFileUrl = 'agreeapplication/api/findDelimiterAndFileExtension';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes';

    constructor(private http: Http, private dateUtils: JhiDateUtils, private sessionStorage: SessionStorageService) { }

    // fetch sample data from uploaded file
    public fetchSampleDataByFile(formData: FormData): Observable<Response> {
       // formData.append('tenantId', this.UserData.tenantId);
        let headers = new Headers({});
        let options = new RequestOptions({ headers });
        return this.http.post(this.fetchSampleDataByFileUrl, formData, options).map((res: Response) => {
            return res.json();
        });
    }

    // ******* Get lookup values based on lookup code @Rk *************
    public fetchLookUpsByLookUpType(lookupType: string) {
        return this.http.get( `${this.lookupCodesUrl}/${lookupType}` ).map(( res: Response ) =>{return res.json();  });
    }
    
}
