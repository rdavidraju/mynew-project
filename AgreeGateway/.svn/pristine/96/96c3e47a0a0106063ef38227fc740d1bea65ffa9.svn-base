import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { TenantDetails } from './tenant-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import 'rxjs/add/operator/toPromise';
//import {} from '../../../content/images'


@Injectable()
export class TenantDetailsService {

    private resourceUrl = 'api/tenant-details';
    private tenantConfigModuleUrl = 'agreeapplication/api/tenant-config-modules-list';
    private updatetenantConfigModuleUrl = 'agreeapplication/api/tenant-config-modules';
   // private UserData = this.$sessionStorage.retrieve('userData');
    private tenantConfigModulesUrl = 'agreeapplication/api/getTenantConfigModulesByTenantId';
    private lookUpUrl = 'agreeapplication/api/lookup-codes';
    private getLookUpCodesForZeroTenant = 'agreeapplication/api/getLookUpCodesForZeroTenant';
    public jsonCountries = 'json/countries.json';
    public jsonState = 'json/states.json';
    public jsonCities = 'json/cities.json';
    private createTenantAndDefaultUserUrl = 'api/createTenantAndDefaultUser';
   // private jsonFile =  require('country_state_cities.json');
    //private countriesList = 'country_state_cities.json';

    constructor(private http: Http, private $sessionStorage: SessionStorageService,private dateUtils: JhiDateUtils) { }

    create(tenantDetails: TenantDetails): Observable<TenantDetails> {
        const copy = this.convert(tenantDetails);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    createNewTenant(tenantDetails: TenantDetails) {
        const copy = this.convert(tenantDetails);
        return this.http.post(this.createTenantAndDefaultUserUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(tenantDetails: TenantDetails): Observable<TenantDetails> {
        const copy = this.convert(tenantDetails);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<TenantDetails> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(tenantDetails: TenantDetails): TenantDetails {
        const copy: TenantDetails = Object.assign({}, tenantDetails);
        return copy;
    }

    fetchAllTenantList(): Observable<Response> {
        return this.http.get(`${this.resourceUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    fetchTenantDetails(): Observable<Response> {
        return this.http.get(`${this.resourceUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    fetchTenantConfigModule(tentId?:any): Observable<Response> {
        if(tentId){
            console.log('tentId ' + tentId);
            return this.http.get(`${this.tenantConfigModulesUrl}?tenantId=${tentId}`).map((res: Response) => {
                let jsonResponse =  res.json();
                jsonResponse.forEach(element => {
                    if(element && element.startDate)
                    element.startDate = this.dateUtils
                        .convertLocalDateFromServer(element.startDate);
                    if( element && element.endDate)
                    element.endDate = this.dateUtils
                        .convertLocalDateFromServer(element.endDate);    
                });
                return jsonResponse;
            });
        }else{
            
            return this.http.get(`${this.tenantConfigModulesUrl}`).map((res: Response) => {
                let jsonResponse =  res.json();
                jsonResponse.forEach(element => {
                    if(element && element.startDate)
                    element.startDate = this.dateUtils
                        .convertLocalDateFromServer(element.startDate);
                    if( element && element.endDate)
                    element.endDate = this.dateUtils
                        .convertLocalDateFromServer(element.endDate);    
                });
                return jsonResponse;
            });
        }
        
    }

    getLookUps(code: any): Observable<Response> {
        return this.http.get(`${this.lookUpUrl}/${code}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    getLookUpsModule(code: any): Observable<Response> {
        return this.http.get(`${this.getLookUpCodesForZeroTenant}?lookUptype=${code}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getCountriesList() {
        return this.http.get(this.jsonCountries).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    getStateList() {
        return this.http.get(this.jsonState).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    getCitiesList() {
        return this.http.get(this.jsonCities).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* service to post tenant config */
    postTenantConfig(obj) {
       // const copy = this.convert(obj);
        console.log('while posting ' + JSON.stringify(obj));
         return this.http.post(this.tenantConfigModuleUrl, obj).map((res: Response) => {
            return res;
        }); 
    }

    /* service to update tenant config */
    updateTenantConfig(obj) {
       // obj.tenantId = this.UserData.tenantId;
       // const copy = this.convert(obj);
        console.log('while update ' + JSON.stringify(obj));
         return this.http.put(this.updatetenantConfigModuleUrl, obj).map((res: Response) => {
            return res.json();
        }); 
    }

     /* service to post single tenant config */
    postSingleTenantConfig(obj) {
        console.log('while posting ' + JSON.stringify(obj));
         return this.http.post(this.updatetenantConfigModuleUrl, obj).map((res: Response) => {
            return res.json();
        }); 
    }

    /* service to delete tenant config */
    deleteTenantConfig(id) {
        console.log('while deleting ' +id);
         return this.http.delete(`${this.updatetenantConfigModuleUrl}/${id}`).map((res: Response) => {
            return res;
        }); 
    }

}
