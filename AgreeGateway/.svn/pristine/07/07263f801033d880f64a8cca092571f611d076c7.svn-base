import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { Calendar } from './calendar.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class CalendarService {

    //private UserData = this.$sessionStorage.retrieve('userData');
    private resourceUrl = 'agreeapplication/api/calendars';
    private resourceSearchUrl = 'agreeapplication/api/_search/calendars';
    private getAllcalenderListUrl = 'agreeapplication/api/getAllcalenderList?page=';
    private getCalenderAndItsPeriodsUrl = 'agreeapplication/api/getCalenderAndItsPeriods?id=';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes/';
    private postCalenderAndItsPeriodsUrl = 'agreeapplication/api/PostCalenderAndItsPeriods';
    private updatePeriodsUrl = 'agreeapplication/api/periods';
    private deletePeriodsUrl = 'agreeapplication/api/periods/';

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService
    ) { }

    create(calendar: Calendar): Observable<Calendar> {
        const copy = this.convert(calendar);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(calendar: Calendar): Observable<Calendar> {
        const copy = this.convert(calendar);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Calendar> {
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

    private convert(calendar: Calendar): Calendar {
        const copy: Calendar = Object.assign({}, calendar);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(calendar.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(calendar.endDate);

        copy.createdDate = this.dateUtils.toDate(calendar.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(calendar.lastUpdatedDate);
        return copy;
    }

    /**New API's */
    /**Get all calendar list with pagination by tenantid */
    getAllcalenderList(page, size, activeFlag){
        return this.http.get(this.getAllcalenderListUrl+page+'&per_page='+size+'&activeFlag='+activeFlag).map((res: Response)=>{
            return res;
        });
    }

    /**Get Calendar details with period details by id */
    getCalenderAndItsPeriods(id){
        return this.http.get(this.getCalenderAndItsPeriodsUrl+id).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Get Calendar Type */
    lookupCodes(lookupType): Observable<any>{
        return this.http.get(this.lookupCodesUrl + lookupType).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        })
    }

    /**Post calendar and its periods */
    postCalenderAndItsPeriods(calendarNPeriods){
        return this.http.post(this.postCalenderAndItsPeriodsUrl, calendarNPeriods).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Update calendar */
    updateCalendar(calendar){
        return this.http.put(this.resourceUrl, calendar).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Update calendar periods */
    updatePeriods(periods){
        return this.http.put(this.updatePeriodsUrl, periods).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Delete Period */
    deletePeriod(periodId){
        return this.http.delete(this.deletePeriodsUrl+periodId).map((res: Response)=>{
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
    checkCalendarNameIsExistUrl:any = 'agreeapplication/api/checkCalendarNameIsExist';
    checkCalendarNameIsExist(name, id?) {
        return this.http.get(`${this.checkCalendarNameIsExistUrl}?name=${name}${id ? '&id='+id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
}
