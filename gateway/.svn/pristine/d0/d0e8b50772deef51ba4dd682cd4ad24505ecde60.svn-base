import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { TenantDetails } from './tenant-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import 'rxjs/add/operator/toPromise';

@Injectable()
export class TenantDetailsService {

    private resourceUrl = 'api/tenant-details';
    private tenantConfigModuleUrl = 'agreeapplication/api/tenant-config-modules-list';
    private updatetenantConfigModuleUrl = 'agreeapplication/api/tenant-config-modules';
    private tenantConfigModulesUrl = 'agreeapplication/api/getTenantConfigModulesByTenantId';
    private lookUpUrl = 'agreeapplication/api/lookup-codes';
    private getLookUpCodesForZeroTenant = 'agreeapplication/api/getLookUpCodesForZeroTenant';
    public jsonCountries = 'json/countries.json';
    public jsonState = 'json/states.json';
    public jsonCities = 'json/cities.json';
    private createTenantAndDefaultUserUrl = 'api/createTenantAndDefaultUser';
    private updateSystemAdminUrl = 'api/user-role-assignments';
    checkDomainIsExistUrl: any = 'api/DomainIsExists';
    isEmailDuplicateUrl: any = 'api/validatingUserEmail';
    private validateUserUrl = 'api/validateUser';
    
    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
        ) { }

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

    fetchAllTenantList(page, size): Observable<Response> {
        return this.http.get(`${this.resourceUrl}?page=${page}&per_page=${size}`).map((res: Response) => {
            return res;
        });
    }

    fetchTenantDetails(): Observable<Response> {
        return this.http.get(`${this.resourceUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    fetchTenantConfigModule(tentId?:any): Observable<Response> {
        if(tentId){
            return this.http.get(`${this.tenantConfigModulesUrl}?tenantId=${tentId}`).map((res: Response) => {
                const jsonResponse =  res.json();
                /* jsonResponse.forEach((element) => {
                    if(element && element.startDate) {
                        element.startDate = this.dateUtils
                            .convertLocalDateFromServer(element.startDate);
                    }
                    if( element && element.endDate) {
                        element.endDate = this.dateUtils
                            .convertLocalDateFromServer(element.endDate);    
                    }
                }); */
                return jsonResponse;
            });
        }else{
            return this.http.get(`${this.tenantConfigModulesUrl}`).map((res: Response) => {
                const jsonResponse =  res.json();
                /* jsonResponse.forEach((element) => {
                    if(element && element.startDate) {
                        element.startDate = this.dateUtils
                            .convertLocalDateFromServer(element.startDate);
                    }
                    if( element && element.endDate) {
                        element.endDate = this.dateUtils
                            .convertLocalDateFromServer(element.endDate);    
                    }
                }); */
                return jsonResponse;
            });
        }
        
    }

    getLookUps(code: any): Observable<Response> {
        return this.http.get(`${this.lookUpUrl}/${code}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    getLookUpsModule(code: any): Observable<Response> {
        return this.http.get(`${this.getLookUpCodesForZeroTenant}?lookUptype=${code}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getCountriesList() {
        return this.http.get(this.jsonCountries).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    getStateList() {
        return this.http.get(this.jsonState).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    getCitiesList() {
        return this.http.get(this.jsonCities).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* service to post tenant config */
    postTenantConfig(obj) {
         return this.http.post(this.tenantConfigModuleUrl, obj).map((res: Response) => {
            return res;
        }); 
    }

    /* service to update tenant config */
    updateTenantConfig(obj) {
        console.log('while update ' + JSON.stringify(obj));
         return this.http.put(this.updatetenantConfigModuleUrl, obj).map((res: Response) => {
            return res.json();
        }); 
    }

     /* service to post single tenant config */
    postSingleTenantConfig(obj, id) {
         return this.http.post(`${this.updatetenantConfigModuleUrl}?tenantId=${id}`, obj).map((res: Response) => {
            return res.json();
        }); 
    }

    /* service to delete tenant config */
    deleteTenantConfig(id) {
         return this.http.delete(`${this.updatetenantConfigModuleUrl}/${id}`).map((res: Response) => {
            return res;
        }); 
    }

    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkDomainIsExist(name, id?) {
        return this.http.get(`${this.checkDomainIsExistUrl}?name=${name}${id ? '&id=' + id : ''}`).map((res: Response) => {
            return res.json();
        });
    }

    isEmailDuplicate(email, id) {
        return this.http.get(`${this.isEmailDuplicateUrl}?email=${email}${id ? `&id=${id}` : ''}`).map((res: Response) => {
            return res.json();
        });
    }

    /* Service to update system admin */
    updateSystemAdmin(obj,userID) {
        return this.http.put(this.updateSystemAdminUrl+'?userId='+userID, obj).map((res: Response) => {
            return res.json();
        });
    }

    /**
     * @author Amit
     * @description Validate user with user id
     * @param id
     */
    validateUser(id) {
        return this.http.get(`${this.validateUserUrl}?userId=${id}`).map((res) => res.json());
    }
}
