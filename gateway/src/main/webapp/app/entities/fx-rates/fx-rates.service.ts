import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions,ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { FxRates } from './fx-rates.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FxRatesService {

    private resourceUrl = 'agreeapplication/api/fx-rates';
    private resourceSearchUrl = 'agreeapplication/api/_search/fx-rates';
    private getAllFxRatesUrl = 'agreeapplication/api/getAllFxRates?page=';
    private getFxRatesAndFxRatesDetailsByIdUrl = 'agreeapplication/api/getFxRatesAndFxRatesDetailsById';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes/';
    private postFxRatesAndFxRatesDetailsUrl = 'agreeapplication/api/postFxRatesAndFxRatesDetails';
    private fXRatesDetailUrl  = 'agreeapplication/api/fx-rates-details';
    private fxRatesbyTenantUrl = 'agreeapplication/api/fetchfxRatesByTenant';
    private bulkUploadUrl = 'agreeapplication/api/bulkUploadForFxRates';
    private checkFxRatesIsExistUrl: any = 'agreeapplication/api/checkFxRatesIsExist';
    private activeFxRatesByTenant = 'agreeapplication/api/fxRatesActiveByTenant';
    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }

    create(fxRates: FxRates): Observable<FxRates> {
        const copy = this.convert(fxRates);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(fxRates: FxRates): Observable<FxRates> {
        const copy = this.convert(fxRates);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<FxRates> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
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
        entity.startDate = this.dateUtils
            .convertLocalDateFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(entity.endDate);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(fxRates: FxRates): FxRates {
        const copy: FxRates = Object.assign({}, fxRates);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(fxRates.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(fxRates.endDate);

        copy.createdDate = this.dateUtils.toDate(fxRates.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(fxRates.lastUpdatedDate);
        return copy;
    }

    /**Get all FX Rates by tenantid with pagination */
    getAllFxRates(page, size){
        return this.http.get(this.getAllFxRatesUrl + page + '&per_page=' + size ).map((res: Response)=>{
            return res;
        });
    }
    getAllFxRatesByTenant(){
        return this.http.get(`${this.fxRatesbyTenantUrl}`).map((res: Response)=>{
            return res.json();
        });
    }

    fetchActiveFxRatesByTenant(){
        return this.http.get(`${this.activeFxRatesByTenant}`).map((res: Response)=>{
            return res.json();
        });
    }

    /**
     * @author Sameer
     * @description Get FX rate and its details by id or export
     * @param id 
     * @param fileExport 
     * @param fileType 
     */
    getFxRatesAndFxRatesDetailsById(id, fileExport?, fileType?) {
        if(fileExport){
            return this.http.get(`${this.getFxRatesAndFxRatesDetailsByIdUrl}?id=${id}&fileExport=${fileExport ? fileExport : false}${fileExport ? `&fileType=${fileType}` : ''}`,{ responseType: ResponseContentType.Blob }).map((res: Response) => {
                return fileExport ? res : res.json();
            });
        }else{
            return this.http.get(`${this.getFxRatesAndFxRatesDetailsByIdUrl}?id=${id}&fileExport=${fileExport ? fileExport : false}${fileExport ? `&fileType=${fileType}` : ''}`).map((res: Response) => {
                return fileExport ? res : res.json();
            });
        }
    }

    /**Get Conversion Type for FX Rates*/
    lookupCodes(lookupType): Observable<any>{
        return this.http.get(this.lookupCodesUrl + lookupType ).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Post FX Rates and Its Details */
    postFxRatesAndFxRatesDetails(fxRates){
        return this.http.post(this.postFxRatesAndFxRatesDetailsUrl, fxRates).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Update FX Rates */
    updateFXRates(fxRates){
        return this.http.put(this.resourceUrl, fxRates).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Update FX Rates Details*/
    updateFXRatesDetail(fxRatesDetail){
        return this.http.put(this.fXRatesDetailUrl, fxRatesDetail).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Delete FX Rates Details*/
    deleteFXRatesDetail(id){
        return this.http.delete(this.fXRatesDetailUrl+'/'+id).map((res: Response)=>{
            /* const jsonResponse = res.json();
            return jsonResponse; */
        });
    }

    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkFxRatesIsExist(name, id?) {
        return this.http.get(`${this.checkFxRatesIsExistUrl}?name=${name}${id ? '&id=' + id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Bulk Upload Data */
    bulkUpload(file) {
        if (file) {
            const formData = new FormData();
            formData.append('multipart', file);
            const headers = new Headers({});
            const options = new RequestOptions({ headers });
            return this.http.post(this.bulkUploadUrl, formData, options).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }

}
