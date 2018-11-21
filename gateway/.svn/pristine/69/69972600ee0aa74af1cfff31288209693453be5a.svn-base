import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions,ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DataViews } from './data-views.model';
import { DataViewsColumns } from '../data-views-columns/data-views-columns.model';
import { JhiDateUtils } from 'ng-jhipster';


@Injectable()
export class DataViewsService {

    private resourceUrl = 'agreeapplication/api/data-views';
    private resourceSearchUrl = 'agreeapplication/api/_search/data-views';
    private getDataViewListUrl = 'agreeapplication/api/dataViewsByTenanat';
    private getDataViewListWithColumns = 'agreeapplication/api/getDataViewsNColsByTenant';
    private getfileTemplateListUrl = 'agreeapplication/api/fileTemplatesByTenantId';
    private getViewColumnsOrTemplateLinesUrl = 'agreeapplication/api/getViewColumnsOrTemplateLines';
    private dataViewColumn = 'agreeapplication/api/data-views-columns';
    private postDataViewColumnUrl = 'agreeapplication/api/postDataViewsColumns';
    private postDataView = 'agreeapplication/api/postViewColumnsOrTemplateLines';
    private operatorsList = 'agreeapplication/api/lookup-codes';
    private createDataViewList = 'agreeapplication/api/createDataView';
    private dataViewsAndcolumnsUrl = 'agreeapplication/api/getDataViewsNColsByTenant';
    private conditionUpdateUrl = 'agreeapplication/api/data-view-conditions';
    private postUnionDataViewColumnUrl = 'agreeapplication/api/updateDataViewColsNUnions';
    private deleteUnionViewColumn = 'agreeapplication/api/dataViewsColumnAndUnionDelete';
    private getDataViewsDataUrl = 'agreeapplication/api/getDataViewsData';
    private sideBarDataViewListsUrl = 'agreeapplication/api/dataViewsSideBarData';
    private dvListWithTypeUrl = 'agreeapplication/api/fetchDataViewsByType'; 
    private dvColumnsByType = 'agreeapplication/api/getDecimalColumnsByViewIdNType';
    private checkDataViewIsExistUrl = 'agreeapplication/api/checkDataViewIsExist';
    private getTemplateLinesUrl = 'agreeapplication/api/getTemplateLines';
    private dataViewsByRuleGroupUrl = 'agreeapplication/api/getDVNameByRuleGroupId';
    private validateCurrentTemplateUrl = 'agreeapplication/api/validateCurrentTemplate';
    private fetchDataViewColumnsbyDvIdUrl = 'agreeapplication/api/fetchDataViewColumnsbyDvId';
    private fetchDataViewsToColumnsMapUrl ='agreeapplication/api/fetchDataViewsToDataViewColumnsMap';
    private dataViewsListByTenantIdUrl = 'agreeapplication/api/dataViewsListByTenantId';
    private createDataViewPostUrl = 'agreeapplication/api/updatePhysicalView';
    private getDataViewsDataCSVUrl = 'agreeapplication/api/getDataViewsDataCSV';
    searchData:any;

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }


    create(dataViews: DataViews): Observable<DataViews> {
        const copy: DataViews = Object.assign({}, dataViews);
        copy.creationDate = this.dateUtils.toDate(dataViews.creationDate);
        copy.lastUpdatedDate = this.dateUtils.toDate(dataViews.lastUpdatedDate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    postDataViews(dataViews: DataViews): Observable<DataViews> {
        return this.http.post(this.postDataView, dataViews).map((res: Response) => {
            return res.json();
        });
    }

    createDataView(id: any): Observable<Response> {
        return this.http.get(`${this.createDataViewList}?viewId=${id}`).map((res: any) => res);
    }

    createDataViewPost(id: any, obj): Observable<Response> {
        return this.http.post(`${this.createDataViewPostUrl}?viewId=${id}`, obj).map((res: any) => res);
    }

    update(dataViews: DataViews): Observable<DataViews> {
        const copy: DataViews = Object.assign({}, dataViews);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<DataViews> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            jsonResponse.creationDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.creationDate);
            jsonResponse.lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            return jsonResponse;
        });
    }

    /* service to create and update dataview columns */
    updateDataViewCol(data: any): Observable<Response> {
        const copy: DataViewsColumns = Object.assign({}, data);
        copy.creationDate = this.dateUtils.toDate(data.creationDate);
        copy.lastUpdatedDate = this.dateUtils.toDate(data.lastUpdatedDate);
        if(data.id != null){
            return this.http.post(this.postDataViewColumnUrl, copy).map((res: Response) => {
                return res.json();
            });
        }else{
            return this.http.post(this.postDataViewColumnUrl, copy).map((res: Response) => {
                return res.json();
            });    
        }
    }

    updateUnionDataViewCol(data: any): Observable<Response> {
        const copy: DataViewsColumns = Object.assign({}, data);
            return this.http.post(this.postUnionDataViewColumnUrl, copy).map((res: Response) => {
                return res.json();
            });
    }

    /* service to delete dataview columns */
    deleteDataViewCol(id: number): Observable<Response> {
        return this.http.delete(`${this.dataViewColumn}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    /* serview to delete union columns */
    deleteUnionDataViewCol(id: number): Observable<Response> {
        return this.http.delete(`${this.deleteUnionViewColumn}?id=${id}`).map((res: Response) => {
            return res.json();
        });
    }

    getDataViewById(id: string): Observable<DataViews> {
        return this.http.get(`${this.fetchDataViewColumnsbyDvIdUrl}?dataViewId=${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            if(jsonResponse[0] && jsonResponse[0].startDate) {
                jsonResponse[0].startDate = this.dateUtils
                    .convertLocalDateFromServer(jsonResponse[0].startDate);
            }
            if( jsonResponse[0] && jsonResponse[0].endDate) {
                jsonResponse[0].endDate = this.dateUtils
                    .convertLocalDateFromServer(jsonResponse[0].endDate);
            }
            if( jsonResponse[0] && jsonResponse.creationDate) {
                jsonResponse.creationDate = this.dateUtils
                    .convertDateTimeFromServer(jsonResponse.creationDate);
            }
            if(jsonResponse[0] && jsonResponse.lastUpdatedDate) {
                jsonResponse.lastUpdatedDate = this.dateUtils
                    .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            }
            return jsonResponse;
        });
    }

     getDVById(id: string): Observable<DataViews> {
        return this.http.get(`${this.getDataViewListWithColumns}?dataViewId=${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            /*if(jsonResponse[0] && jsonResponse[0].startDate) {
                jsonResponse[0].startDate = this.dateUtils
                    .convertLocalDateFromServer(jsonResponse[0].startDate);
            }
            if( jsonResponse[0] && jsonResponse[0].endDate) {
                jsonResponse[0].endDate = this.dateUtils
                    .convertLocalDateFromServer(jsonResponse[0].endDate);
            }
            if( jsonResponse[0] && jsonResponse.creationDate) {
                jsonResponse.creationDate = this.dateUtils
                    .convertDateTimeFromServer(jsonResponse.creationDate);
            }
            if(jsonResponse[0] && jsonResponse.lastUpdatedDate) {
                jsonResponse.lastUpdatedDate = this.dateUtils
                    .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            }*/
            return jsonResponse;
        });
    }

    fetchDataViewsToColumnsMap(): Observable<DataViews> {
        return this.http.get(this.fetchDataViewsToColumnsMapUrl).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    sideBarDataViewLists(): Observable<Response> {
        return this.http.get(this.sideBarDataViewListsUrl).map((res: Response)=>{
            const jsonResponse = res.json();
            console.log('data view from service :' + JSON.stringify(jsonResponse.length));
            return jsonResponse;
        });
    }

    dataViewsWithType(page: number, per_page: number, relation: any, sortCol?: any, sortOrder?: any, searchVal?: any): Observable<Response> {
        relation = relation === 'Single Template' ? '' : relation;
        return this.http.get(`${this.dvListWithTypeUrl}?page=${page}&per_page=${per_page}&relation=${relation.toUpperCase()}&sortDirection=${sortOrder}&sortCol=${sortCol}${searchVal ? `&searchVal=${searchVal}` : ''} `).map((res: Response) => {
            return res;
        });
    }
    
    allDataViewLists(page:number):Observable<Response> {
        return this.http.get(`${this.getDataViewListUrl}?page=${page}`).map((res: Response)=>{
            const jsonResponse = res.json();
            console.log('data view from service :' + JSON.stringify(jsonResponse.length));
            return jsonResponse;    
        });
    }

    dataViewLists(page:number,per_page:number):Observable<Response> {
        return this.http.get(`${this.getDataViewListUrl}?page=${page}&per_page=${per_page}`).map((res: Response)=>{
            return res;
        });
    }

    dataViewList():Observable<Response> {
         return this.http.get(this.getDataViewListUrl).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    dataViewColTempList(data: any):Observable<Response> {
         return this.http.post(`${this.getViewColumnsOrTemplateLinesUrl}`, data).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    fileTemplateList(formType): Observable<Response> {
        return this.http.get(`${this.getfileTemplateListUrl}?page=1&per_page=0&formType=${formType}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    operators(data: any): Observable<DataViews> {
        return this.http.get(`${this.operatorsList}/${data}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    

    private convertResponse(res: any): any {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].creationDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].creationDate);
            jsonResponse[i].lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].lastUpdatedDate);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }

    /**
     * Author : Shobha
     */
    fetchDataViewsAndColumns(dataViewId?: any): Observable<any> {
        if(dataViewId){
            return this.http.get(`${this.dataViewsAndcolumnsUrl}?dataViewId=${dataViewId}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });    
        } else {
            return this.http.get(this.dataViewsAndcolumnsUrl).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }

    /* service to update conditions */
    conditionUpdate(val: any, id): Observable<DataViews> {
        const copy: DataViews = Object.assign({}, val);
        return this.http.put(`${this.conditionUpdateUrl}?dataViewId=${id}`, copy).map((res: Response) => {
            return res.json();
        });
    }

    /* service to get data views */
    getViewDetails(id:any,page:number,per_page:number):Observable<Response> {
         return this.http.get(`${this.getDataViewsDataUrl}?viewId=${id}&pageNumber=${page}&pageSize=${per_page}`).map((res: Response)=>{
            const jsonResponse = res;
            return jsonResponse;
        });
    }


    /* Fetch Dv Columns By Type */
    /* AUTHOR: BHAGTAH */
    fetchDvColumnsByType(viewId:any,dataType:any):Observable<Response> {
         return this.http.get(this.dvColumnsByType + '?viewId=' + viewId + "&dataType=" + dataType).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * Author: Sameer
     * Purpose: Check data view name duplicate
     */
    checkDataViewIsExist(name, id) {
        return this.http.get(`${this.checkDataViewIsExistUrl}?name=${name}${id ? '&id='+id : ''}`).map((res: Response) => {
            return res.json();
        });
    }

    /**
     * Author: Sameer
     * Purpose: Get Template Lines, array of array [[{}], [{}], [{}]]
     */
    getTemplateLines(data) {
        return this.http.post(this.getTemplateLinesUrl, data).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    fetchDataViewsByRuleGroup(ruleGrpId) {
        return this.http.get(this.dataViewsByRuleGroupUrl + '?ruleGroupId='+ruleGrpId ).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author: Sameer
     * @description: Check Current Template in formula
     * @param: formula
     * @param: curTempName
     */
    validateCurrentTemplate(formula, curTempName) {
        return this.http.post(`${this.validateCurrentTemplateUrl}?formula=${formula}`,curTempName).map((res) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
    * @author Sameer
    * @description Physical View Export
    * @param viewId
    * @param type
    */ 
    getDataViewsDataCSV(id, type) {
        return this.http.get(`${this.getDataViewsDataCSVUrl}?viewId=${id}&fileType=${type}`,{ responseType: ResponseContentType.Blob }).map((res) => {
            return res;
        });
    }

    /**
     * Author : Shobha
     */
    dataViewsListByTenantId() {
        return this.http.get(this.dataViewsListByTenantIdUrl).map((res) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
}
