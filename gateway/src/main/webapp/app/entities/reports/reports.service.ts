import { Injectable } from '@angular/core';
import { Http, Response, ResponseContentType } from '@angular/http';
import { Observable, Subject } from 'rxjs/Rx';
import 'rxjs/Rx' ;
import { JhiDateUtils } from 'ng-jhipster';

import { Reports, AgingBucket, AgingBucketDetails, ReportRequestList } from './reports.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { saveAs } from 'file-saver';
import * as FileSaver from 'file-saver';
import { CommonService } from '../common.service';

import {BrowserXhr} from '@angular/http';

@Injectable()
export class ReportsService {

    private resourceUrl = 'agreeapplication/api/reports';
    private resourceSearchUrl = 'agreeapplication/api/_search/reports';
    private reportsListUrl = 'agreeapplication/api/getReportsByTenant';
    private reportFieldsInfoUrl = 'agreeapplication/api/getReportFieldsInfo';
    private reportTypesUrl = 'agreeapplication/api/reportTypesByTenantId';
    private dataViewsListUrl = 'agreeapplication/api/dataViewsByTenanat';
    private dataViewColsListUrl = 'agreeapplication/api/getDataViewsNColsByTenant';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes';
    private postReportDefUrl = 'agreeapplication/api/reportDefination';
    private postBucketDefUrl = 'agreeapplication/api/postBucketInfo';
    private checkReportNameUrl = 'agreeapplication/api/validateReportName';
    private checkBucketNameUrl = 'agreeapplication/api/checkBucketNameDuplication';
    private reportDefByIdUrl = 'agreeapplication/api/getReportDefinations';
    private bucketDefByIdUrl = 'agreeapplication/api/getBucketInfo';
    private allBucketsByTenantIdUrl = 'agreeapplication/api/bucket-lists';
    private bucketsListByPaginationUrl = 'agreeapplication/api/getBucketsByTenantId';
    private runTabluarReportUrl = 'agreeapplication/api/TabularViewReportGeneration';
    private reportReqListById = 'agreeapplication/api/reportRequestByTenantIdOrReqId';
    private getReportOutputByIdUrl = 'agreeapplication/api/getReportOutputByRequestId';
    private getReportParameterFieldsListByIdUrl = 'agreeapplication/api/getFieldValuesList';
    private getSortedReportOutputByIdUrl = 'agreeapplication/api/sortingValuesInReportOutputJson';
    private shareCSVasMialUrl = 'agreeapplication/api/shareReportingCSV';
    private getAllUsersUrl = 'api/getUsersByTenantId';
    private getParamsObjByReqIdUrl='agreeapplication/api/getRequestparameterObject';
    private reportTabOutputAsyncUrl='agreeapplication/api/TabularViewReportGenerationAsync';
    private favouriteRptUrl='agreeapplication/api/favourite-reports';
    private removeFavouriteRptUrl='agreeapplication/api/remove-favourite-report';
    private testOozie='agreeapplication/api/testOozieStatus';
    private getSegmentQualifier='agreeapplication/api/getSegmentByCoaIdAndQualifier';
    private downloadReportUrl='agreeapplication/api/exportReport';
    private saveDashboardUrl='agreeapplication/api/saveDashboardDefinitions';
    private reportTemplateListUrl='agreeapplication/api/getAllReportTemplates';
    private reportTemplateColsUrl='agreeapplication/api/reportLayoutCols';
    private deleteDashboardUrl='agreeapplication/api/reporting-dashboards';
    private getDashboardDefUrl='agreeapplication/api/getDashboardDefinitions';
    private getDashboardsListUrl='agreeapplication/api/reporting-dashboards';
    private checkDashboardUrl='agreeapplication/api/checkDashboardName';
    private getDashboardOutputUrl='agreeapplication/api/getDashboardOutput';
    private getUsecaseOutputUrl='agreeapplication/api/getUsecaseOutput'
    private updatePreferencesUrl='agreeapplication/api/updatePreferences';
    private getPeriodsByLedgerNameUrl='agreeapplication/api/getPeriodsByLedgerName';
    private cancelJobUrl='agreeapplication/api/cancelJob';
    private refreshAsOfNowAsyncUrl='agreeapplication/api/refeshRecentRequest';
    private requestInfoByReqIdUrl = 'agreeapplication/api/requestInfoByReqId';
    

    constructor(private http: Http, private dateUtils: JhiDateUtils,private cs: CommonService) { }

    // ******* Cancel running job @Rk *************
    refreshAsOfNowAsync(rptId:any,reqId:any) {
        return this.http.get( `${this.refreshAsOfNowAsyncUrl}?reportId=${rptId}&requestId=${reqId}` ).map(( res: Response ) =>{ 
            if(res.status==200){
                this.cs.info(
                    'Done!',
                    'Request submitted'
                )
            }else{
                this.cs.error(
                    'Error!',
                    'Failed to submit request'
                )
            }
        });
    }

    // ******* get request info by request id @Rk ***********
    getRequestInfoByReqId(reqId){
        return this.http.get(`${this.requestInfoByReqIdUrl}?reqId=${reqId}`).map((res:Response)=>{
            return res.json();
        })
    }

    // ******* Cancel running job @Rk *************
    cancelJob(sysReqName:string) {
        return this.http.get( `${this.cancelJobUrl}?sysReqName=${sysReqName}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get period names by ledger name @Rk *************
    getPeriodsByLedgerName(ldgrName:any) {
        return this.http.get( `${this.getPeriodsByLedgerNameUrl}?ldgrName=${ldgrName}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Dashboard output @Rk *************
    getDashboardOutput(dashboardId:any) {
        return this.http.get( `${this.getDashboardOutputUrl}?dashboardId=${dashboardId}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Usecase output @Rk *************
    getUsecaseOutput(usecaseId:any) {
        return this.http.get( `${this.getUsecaseOutputUrl}?usecaseId=${usecaseId}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Update Dashboard preferences @Rk *************
    updatePreferences(preferences: any[]): Observable<Response> {
        return this.http.post( `${this.updatePreferencesUrl}`, preferences).map(( res: Response ) =>{ 
            return res.json();  
        });
    }

    // ******* Post reports definition @Rk *************
    postDashboard(dashBoard: any): Observable<Response> {
        return this.http.post( `${this.saveDashboardUrl}`, dashBoard).map(( res: Response ) =>{ 
            return res  
        });
     }

    // ******* Get Report Template list @Rk *************
    getReportTemplates() {
        return this.http.get( `${this.reportTemplateListUrl}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Report Template Cols @Rk *************
    getReportTemplateCols(id:any) {
        return this.http.get( `${this.reportTemplateColsUrl}?id=${id}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Report Template Cols @Rk *************
    getDashboardDef(id:any) {
        return this.http.get( `${this.getDashboardDefUrl}?dashBoardId=${id}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Delete Dashboard @Rk *************
    deleteDashboard(id:any) {
        return this.http.delete( `${this.deleteDashboardUrl}/${id}` ).map(( res: Response ) =>{ 
            return res.status;
        });
    }

    // ******* Get Dashboards list @Rk *************
    getDashboardsList() {
        return this.http.get( `${this.getDashboardsListUrl}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Report Template list @Rk *************
    checkDashboardName(name:string) {
        return this.http.get( `${this.checkDashboardUrl}?dashBoardName=${name}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Download report output @Rk *************
    downloadReportOutput(reqId:any,fileType:string,reportName:string) {
        return this.http.get(`${this.downloadReportUrl}?requestId=${reqId}&fileType=${fileType}`,{ responseType: ResponseContentType.Blob }).map((res: any)=> {
            const a = document.createElement("a");
            document.body.appendChild(a);
            const url = window.URL.createObjectURL(res.blob());
            a.href = url;
            if(fileType=='excel'){
                a.download = reportName+".xlsx";
            }else if(fileType=='csv'){
                a.download = reportName+".csv";
            }
            a.click();
            window.URL.revokeObjectURL(url);
        });
    }

    // ******* Download report output @Rk *************
    downloadCSV() {
        return this.http.get(`${'agreeapplication/api/downloadAsCSV'}`).map((success: any)=> {
            const blob = new Blob([success._body], { type: 'application/vnd.ms-excel' });
 
                if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveOrOpenBlob(blob, "report.xlsx");
                } else {
                    const a = document.createElement('a');
                    a.href = URL.createObjectURL(blob);
                    a.download = "report.xlsx";
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
                }
        });
    }
    
    // ******* Get Report Types list @Rk *************
    getReportTypes() {
        return this.http.get( `${this.reportTypesUrl}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Report Types list @Rk *************
    getSegmentByViewIdAndQaulifier(viewId:any,qualifier:String) {
        return this.http.get( `${this.getSegmentQualifier}/?viewId=${viewId}&qualifier=${qualifier}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }
    
    // ******* Get available Reports list @Rk *************
    getAllReportsList(paginnationOptions?: any, reportType?: string, sortDirection?: string, sortCol?: string,favRpts?:boolean,recentRpts?:boolean, searchKeyword?: string): Observable<Response> {
        let url = `${this.reportsListUrl}/`;
        if (paginnationOptions) {
            url = url + `?page=${paginnationOptions.pageInd}&per_page=${paginnationOptions.pageSize}`
        } else {
            url = url + `?page=${0}&per_page=${25}`
        }
        if (reportType && reportType.length > 0) {
            url = url + `&reportType=${reportType}`
        }
        if (sortCol && sortCol.length > 0) {
            if (sortDirection && sortDirection.length > 0) {
                url = url + `&sortDirection=${sortDirection}`
            } else {
                url = url + `&sortDirection=${'Ascending'}`
            }
            url = url + `&sortCol=${sortCol}`
        }
        if(favRpts){
            url=url+`&favRpts=${favRpts}`;
        }else{
            url=url+`&favRpts=${false}`;
        } 
        if(recentRpts){
            url=url+`&recentRpts=${recentRpts}`;
        }else{
            url=url+`&recentRpts=${false}`;
        }
        if(searchKeyword && searchKeyword.length>0){
            url=url+`&searchKeyword=${searchKeyword}`;
        }
        
        return this.http.get(url).map((res: Response) => {
            return res
        });
    }
    
    // ******* Get selected Report fields info, to present form controls while running the report @Rk *************
    getReportFieldsInfo(reportId: number) {
        return this.http.get( `${this.reportFieldsInfoUrl}/?reportId=${reportId}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }
    
    // ******* Get Data Views List info @Rk *************
    getDataViewsList() {
        return this.http.get( `${this.dataViewsListUrl}` ).map(( res: Response ) =>{ 
            return res.json();
        });
    }
    
    // ******* Get columns list by data view id List info @Rk *************
    getColsListByDVid(dataViewId: number) {
        return this.http.get( `${this.dataViewColsListUrl}/?dataViewId=${dataViewId}` ).map(( res: Response ) =>{ 
            return res.json()[0];
        });
    }
    
    // ******* Get Data View Columns List info @Rk *************
    getDataViewColsList(dataViewId: number) {
        return this.http.get( `${this.reportFieldsInfoUrl}/?dataViewId=${dataViewId}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }
    
    // ******* Get lookup values based on lookup code @Rk *************
    getLookupValues(lookupType: string,module?:string) {
        let url=`${this.lookupCodesUrl}/${lookupType}`;
        if(module){
            url=url+`?module=${module}`;
        }
        return this.http.get( url ).map(( res: Response ) =>{
            return res.json();  
        });
    }
    
    // ******* Post reports definition @Rk *************
    postReportDef(report: any): Observable<Response> {
       // report.createdBy=this.UserData.id;
        return this.http.post( `${this.postReportDefUrl}`, report).map(( res: Response ) =>{ 
            return res;  
        });
    }

    // ******* Check report name availability @Rk *************h
    checkReportNameAvailable(reportName: string){
        return this.http.get( `${this.checkReportNameUrl}/?reportName=${reportName}` ).map(( res: Response ) =>{
            const resSTR = JSON.stringify(res);
            const resJSON = JSON.parse(resSTR);
            return resJSON._body;
        });
     }
    
    // ******* Get Report Definition based on report ID @Rk *************
    getReportDefinition(reportId: any) {
        return this.http.get( `${this.reportDefByIdUrl}/?reportId=${reportId}`).map(( res: Response ) =>{ 
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    testOozieServer(){
        return this.http.get(this.testOozie).map((res:Response)=>{
            return res.json();
        })
    }

    // ******* Get Tabular Report output based on parameters @Rk *************
    getReportOutputFirstTime(sysReqName,reportId,reqName, paramsObj: any,sbmtAsJob?:string, PaginnationOptions?: any): Observable<Response> {
        let url=this.runTabluarReportUrl;
        if(sbmtAsJob && sbmtAsJob=='ok'){
            url=this.reportTabOutputAsyncUrl;
        }
        url=url+`/?reportId=${reportId}&reqName=${reqName}`;
        if(PaginnationOptions){
            url=url+`&pageNumber=${PaginnationOptions.page}&pageSize=${PaginnationOptions.size}`;
        }
        url=url+`&sysReqName=${sysReqName}`;
        return this.http.post(`${url}`, paramsObj).map((res: Response) => {
            return res;
        });
    }
    
    // ******* Post Bucket definition @Rk *************
    postBucketDef(bucket: any): Observable<Response> {
        return this.http.post( `${this.postBucketDefUrl}`, bucket).map(( res: Response ) =>{ 
            return res;
        });
    }

    // ******* Get Bucket Definition by bucket ID @Rk *************
    getBucketDef(bucketId: any) {
        return this.http.get( `${this.bucketDefByIdUrl}/?bucketId=${bucketId}`).map(( res: Response ) =>{ 
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    // ******* Get Bucket Definition by bucket ID @Rk *************
    getAllBuckets() {
        return this.http.get( `${this.allBucketsByTenantIdUrl}`).map(( res: Response ) =>{ 
            return res.json();
        });
    }

    // ******* Get Bucket Definition by bucket ID @Rk *************
    getAllBucketsByPagination(pageNumber,pageSize,searchKeyword) {
        return this.http.get( `${this.bucketsListByPaginationUrl}/?pageNumber=${pageNumber}&pageSize=${pageSize}&searchKeyword=${searchKeyword}`).map(( res: Response ) =>{ 
            return res;
        });
    }

    // ******* Check report name availability @Rk *************
    checkBucketNameAvailable(bucketName: string) {
        return this.http.get(`${this.checkBucketNameUrl}/?bucketName=${bucketName}`).map((res: Response) => {
            return res.json();
        });
    }

    // ******* Get Report Request list By Report Id @Rk *************
    getReportReqList(rptId: any, paginnationOptions?: any, requestType?:string, sortCol?:string, sortDirection?:string): any {
        let url=`${this.reportReqListById}/?repId=${rptId}`;
        if(paginnationOptions){
            url=url+`&page=${paginnationOptions.page}&per_page=${paginnationOptions.size}`;
        }
        if(requestType&&requestType.length>0){
            url=url+`&requestType=${requestType}`;
        }
        if(sortCol&&sortCol.length>0){
            if(sortDirection&&sortDirection.length>0){
                url=url+`&sortDirection=${sortDirection}&sortCol=${sortCol}`;
            }else{
                url=url+`&sortDirection=${'ascending'}&sortCol=${sortCol}`;
            }
        }
        return this.http.get( url ).map(( res: Response ) =>{ 
            return res;
        });
    }

    
    // ******* Get Report Output by Request Id @Rk *************
    getReportOutputByReqId(reqId,outputType:string, page, size, colSearch, sortOrder?, sortColumn?,searchString?) {
        let url=`${this.getReportOutputByIdUrl}?requestId=${reqId}&outputType=${outputType}&pageNumber=${page}&pageSize=${size}`;
        if(sortColumn&&sortOrder && sortColumn.length>0 && sortOrder.length>0){
            url=url+`&sortColumn=${sortColumn}&sortOrder=${sortOrder}`;
        }
        if(searchString && searchString.length>0){
            url=url+`&searchString=${searchString}`;
        }
        return this.http.post(url,colSearch).map((res: Response)=> {
            return res; 
        });
    }

    // ******* Get Report Output by Request Id With Sort Order @Rk *************
    getSortedReportOutputByReqId(reqId,sortCol,sortOrder, page?, size?) {
        return this.http.get(`${this.getSortedReportOutputByIdUrl}?requestId=${reqId}&sortColumn=${sortCol}&sortOrder=${sortOrder}&pageNumber=${page}&pageSize=${size}` ).map((res: Response)=> {
            return res;
        });
    }

    // ******* Get Report Parameter LOVs by Id @Rk *************
    getReportParamLovsByParamId(reportId, rParamId) {
        return this.http.get(`${this.getReportParameterFieldsListByIdUrl}?reportId=${reportId}&rParamId=${rParamId}`).map((res: Response)=> {
            return res.json();
        });
    }

    /* Share report with report req ID */
    shareReport(emails:string,reqId:number,url:string,comments:String) {
        return this.http.get(`${this.shareCSVasMialUrl}?emailList=${emails}&requetId=${reqId}&comments=${comments}&url=${url}`).map((res: Response)=> {
            return res.json();
        },(res: Response)=>{
            this.cs.error('Sorry!', 'Request failed to share');
        });
    }

    /* Get report params obj by req id */
    getParamsObj(reqId){
        return this.http.get(`${this.getParamsObjByReqIdUrl}?id=${reqId}`).map((res: Response)=> {
            return res.json();
        });
    }
    
    /* Get all users */
    getAllUsers(){
        return this.http.get(`${this.getAllUsersUrl}`).map((res: Response)=> {
            return res.json();
        });
    }

    // ******* Create Favourite Report @Rk *************
    setFavReport(rptId: any): Observable<Response> {
        const def={
            "reportId": rptId
           // "tenantId": this.UserData.tenantId,
           // "userId": this.UserData.id
          }
        return this.http.post( `${this.favouriteRptUrl}`, def).map(( res: Response ) =>{ 
            return res;
        });
    }

    // ******* Remove Favourite Report @Rk *************
    removeFavReport(rptId: any): Observable<Response> {
        return this.http.delete( `${this.removeFavouriteRptUrl}/?rptId=${rptId}`).map(( res: Response ) =>{ 
            return res;
        });
    }
    
    find(id: number): Observable<Reports> {
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

    convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    convertItemFromServer(entity: any) {
        entity.startDate = this.dateUtils
            .convertDateTimeFromServer(entity.startDate);
        if (entity.endDate) {
            entity.endDate = this.dateUtils
                .convertDateTimeFromServer(entity.endDate);
        }
    }
}
