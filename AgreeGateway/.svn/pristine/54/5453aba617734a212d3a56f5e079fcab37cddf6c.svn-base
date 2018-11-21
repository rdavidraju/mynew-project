import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Reports, AgingBucket, AgingBucketDetails, ReportRequestList } from './reports.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import { saveAs } from 'file-saver';
import { NotificationsService } from 'angular2-notifications-lite';
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
    private runTabluarReportUrl = 'agreeapplication/api/TabularViewReportGeneration';
    private runTabluarReportUrlAccountBalance = 'agreeapplication/api/accountBalanceReportTesting';
    private runPivotReportUrl = 'agreeapplication/api/PivotViewReport';
    private runChartReportUrl = 'agreeapplication/api/generateChart';
    private reportOutputUrl = 'agreeapplication/api/getReportOutputByPage';
    private reportReqListById = 'agreeapplication/api/reportRequestByTenantIdOrReqId';
    private getReportOutputByIdUrl = 'agreeapplication/api/getReportOutputByRequestId';
    private getReportParameterFieldsListByIdUrl = 'agreeapplication/api/getFieldValuesList';
    private getSortedReportOutputByIdUrl = 'agreeapplication/api/sortingValuesInReportOutputJson';
    private shareCSVasMialUrl = 'agreeapplication/api/shareReportingCSV';
    private exportToCSVUrl = 'agreeapplication/api/reportJsonExportToCSV';
    private getAllUsersUrl = 'api/getUsersByTenantId';
    private getParamsObjByReqIdUrl='agreeapplication/api/getRequestparameterObject';
    private reportTabOutputAsyncUrl='agreeapplication/api/TabularViewReportGenerationAsync';
    private reportPvtOutputAsyncUrl='agreeapplication/api/PivotViewReportAsync';
    private favouriteRptUrl='agreeapplication/api/favourite-reports';
    private removeFavouriteRptUrl='agreeapplication/api/remove-favourite-report';
    private testOozie='agreeapplication/api/testOozieStatus';
    private getSegmentQualifier='agreeapplication/api/getSegmentByCoaIdAndQualifier';

    constructor(private http: Http, private dateUtils: JhiDateUtils, private sessionStorage: SessionStorageService,private notificationsService: NotificationsService,) { }
    
    // ******* Get Report Types list @Rk *************
    getReportTypes() {
        return this.http.get( `${this.reportTypesUrl}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }

    // ******* Get Report Types list @Rk *************
    getSegmentByViewIdAndQaulifier(viewId:number,qualifier:String) {
        return this.http.get( `${this.getSegmentQualifier}/?viewId=${viewId}&qualifier=${qualifier}` ).map(( res: Response ) =>{ 
            return res.json(); 
        });
    }
    
    // ******* Get available Reports list @Rk *************
    getAllReportsList(paginnationOptions?: any, reportType?: string, sortDirection?: string, sortCol?: string,favRpts?:boolean,recentRpts?:boolean): Observable<Response> {
        let url = `${this.reportsListUrl}/`;
        if (paginnationOptions) {
            url = url + `?page=${paginnationOptions.pageInd}&per_page=${paginnationOptions.pageSize}`
        }else
            url = url + `?page=${0}&per_page=${25}`
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
        
        return this.http.get(url).map((res: Response) => {
            return res
        });
    }
    
    // ******* Get selected Report fields info, to present form controls while running the report @Rk *************
    getReportFieldsInfo(reportId: number) {
        return this.http.get( `${this.reportFieldsInfoUrl}/?reportId=${reportId}` ).map(( res: Response ) =>{ 
            return res.json(); });
    }
    
    // ******* Get Data Views List info @Rk *************
    getDataViewsList() {
        return this.http.get( `${this.dataViewsListUrl}` ).map(( res: Response ) =>{ return res.json() });
    }
    
    // ******* Get columns list by data view id List info @Rk *************
    getColsListByDVid(dataViewId: number) {
        return this.http.get( `${this.dataViewColsListUrl}/?dataViewId=${dataViewId}` ).map(( res: Response ) =>{ return res.json()[0] });
    }
    
    // ******* Get Data View Columns List info @Rk *************
    getDataViewColsList(dataViewId: number) {
        return this.http.get( `${this.reportFieldsInfoUrl}/?dataViewId=${dataViewId}` ).map(( res: Response ) =>{ return res.json() });
    }
    
    // ******* Get lookup values based on lookup code @Rk *************
    getLookupValues(lookupType: string,module?:string) {
        let url=`${this.lookupCodesUrl}/${lookupType}`;
        if(module){
            url=url+`?module=${module}`;
        }
        return this.http.get( url ).map(( res: Response ) =>{return res.json();  });
    }
    
    // ******* Post reports definition @Rk *************
    postReportDef(report: any) : Observable<Response> {
       // report.createdBy=this.UserData.id;
        return this.http.post( `${this.postReportDefUrl}`, report).map(( res: Response ) =>{ return res  });
    }

    // ******* Check report name availability @Rk *************
    checkReportNameAvailable(reportName: string){
        return this.http.get( `${this.checkReportNameUrl}/?reportName=${reportName}` ).map(( res: Response ) =>{
            return res.json();
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
    getReportOutputFirstTime(reportId, paramsObj: any,sbmtAsJob?:string, PaginnationOptions?: any) : Observable<Response> {
        let url=this.runTabluarReportUrl;
        if(sbmtAsJob && sbmtAsJob=='ok'){
            url=this.reportTabOutputAsyncUrl;
        }
        // if(paramsObj.fields[0].repType === 'TRAIL_BALANCE_REPORT'){
        //     url='agreeapplication/api/getTrailBalanceReportOutput';
        // }
        url=url+`/?reportId=${reportId}`;
        if(PaginnationOptions){
            url=url+`&pageNumber=${PaginnationOptions.page}&pageSize=${PaginnationOptions.size}`;
        }
        return this.http.post(`${url}`, paramsObj).map((res: Response) => {
            return res;
        });
        // let respObj:OozieStatus;
        // this.testOozieServer().subscribe((res: OozieStatus) => {      //To get Oozie status
        //     respObj = res;
        // });
        // if(respObj){
        //     if (respObj.ooziestatus && respObj.dbStatus) {
        //         return this.http.post(`${url}`, paramsObj).map((res: Response) => {
        //             return res;
        //         });
        //     } else{
        //         this.notificationsService.error('Sorry!', 'Server was unstable!', {
        //             timeOut: 4000,
        //             showProgressBar: false,
        //             pauseOnHover: true,
        //             clickToClose: true,
        //             maxLength: 100
        //         });
        //         return undefined;
        //     }
        // }
    }
    
    // ******* Get Pivot Report output based on parameters @Rk *************
    getPivotReportOutput(reportId, paramsObj: any, sbmtAsJob?:string) {
        let url=this.runPivotReportUrl;
        if(sbmtAsJob && sbmtAsJob=='ok'){
            url=this.reportPvtOutputAsyncUrl;
        }
        return this.http.post( `${url}/?reportId=${reportId}`,paramsObj).map(( res: Response ) =>{ 
            return res;
        });
    }
    
    // ******* Get Chart Report output based on parameters @Rk *************
    getChartReportOutput(reportId, paramsObj: any) {
        return this.http.post( `${this.runChartReportUrl}/?reportId=${reportId}`,paramsObj).map(( res: Response ) =>{ 
            return res.json();
        });
//        return this.tempChart;
    }

    // ******* Post Bucket definition @Rk *************
    postBucketDef(bucket: any) : Observable<Response> {
        return this.http.post( `${this.postBucketDefUrl}`, bucket).map(( res: Response ) =>{ return res  });
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
        /* return this.http.get( `${this.allBucketsByTenantIdUrl}`).map(( res: Response ) =>{ 
            return res.json();
        }); */
        return this.http.get( `${this.allBucketsByTenantIdUrl}`).map(( res: Response ) =>{ 
            return res.json();
        });
    }

    // ******* Check report name availability @Rk *************
    checkBucketNameAvailable(bucketName: string) {
        return this.http.get(`${this.checkBucketNameUrl}/?bucketName=${bucketName}`).map((res: Response) => {
            return res.json();
        });
    }

    // ******* Get Report Request list By Report Id @Rk *************
    getReportReqList(rptId: Number, paginnationOptions?: any, requestType?:string, sortCol?:string, sortDirection?:string): any {
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
        return this.http.get( url ).map(( res: Response ) =>{ return res });
    }

    
    // ******* Get Report Output by Request Id @Rk *************
    getReportOutputByReqId(reqId,outputType:string, page, size, sortOrder?, sortColumn?,searchString?) {
        let url=`${this.getReportOutputByIdUrl}?requestId=${reqId}&outputType=${outputType}&pageNumber=${page}&pageSize=${size}`;
        if(sortColumn&&sortOrder && sortColumn.length>0 && sortOrder.length>0){
            url=url+`&sortColumn=${sortColumn}&sortOrder=${sortOrder}`;
        }
        if(searchString && searchString.length>0){
            url=url+`&searchString=${searchString}`;
        }
        return this.http.get(url).map((res: Response)=> {
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
    shareReport(emails:string,reqId:number) {
        return this.http.get(`${this.shareCSVasMialUrl}?emailList=${emails}&requetId=${reqId}`).map((res: Response)=> {
            return res.json();
        },(res: Response)=>{
            this.notificationsService.error('Sorry!', 'Request failed to share', {
                timeOut: 3000,
                showProgressBar: false,
                pauseOnHover: true,
                clickToClose: true,
                maxLength: 50
            });
        });
    }

    /* Get report params obj by req id */
    getParamsObj(reqId){
        return this.http.get(`${this.getParamsObjByReqIdUrl}?id=${reqId}`).map((res: Response)=> {
            return res.json();
        });
    }

    /* Download report output */
    downloadReportCSVFile(reqId:number) {
        return this.http.get(`${this.exportToCSVUrl}?requestId=${reqId}`).map((res: Response)=> {
            if(res.json().status=='success'){
                let path=res.json().destPath;
                var link = document.createElement("a");  
                link.download = path.split('/')[path.split('/').length-1];
                link.href = path;
             console.log('url to download :'+link.href);
                link.click();
            }else{
                this.notificationsService.error('Sorry!', 'Report is not available to download!', {
                    timeOut: 3000,
                    showProgressBar: false,
                    pauseOnHover: true,
                    clickToClose: true,
                    maxLength: 50
                });
            }
            
        });
    }

    downloadFile(data: Response){
        var blob = new Blob([data], { type: 'text/csv' });
        var url= window.URL.createObjectURL(blob);
        window.open(url);
      }

    /* Get all users */
    getAllUsers(){
        return this.http.get(`${this.getAllUsersUrl}`).map((res: Response)=> {
            return res.json();
        });
    }

    // ******* Create Favourite Report @Rk *************
    setFavReport(rptId: number) : Observable<Response> {
        let def={
            "reportId": rptId
           // "tenantId": this.UserData.tenantId,
           // "userId": this.UserData.id
          }
        return this.http.post( `${this.favouriteRptUrl}`, def).map(( res: Response ) =>{ return res  });
    }

    // ******* Remove Favourite Report @Rk *************
    removeFavReport(rptId: number) : Observable<Response> {
        return this.http.delete( `${this.removeFavouriteRptUrl}/?rptId=${rptId}`).map(( res: Response ) =>{ return res  });
    }
    
    
    operators(type: any) {
        return this.http.get(`${this.lookupCodesUrl}/${type}`).map((res: Response) => {
            return res.json();
        });
    }
    
    getChartData(){
        return this.tempChart;
    }

    // create(reports: Reports): Observable<Reports> {
    //     const copy = this.convert(reports);
    //     return this.http.post(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

    // update(reports: Reports): Observable<Reports> {
    //     const copy = this.convert(reports);
    //     return this.http.put(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

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
        if (entity.endDate) {
            entity.endDate = this.dateUtils
                .convertLocalDateFromServer(entity.endDate);
        }
    }

    // private convert(reports: Reports): Reports {
    //     const copy: Reports = Object.assign({}, reports);
    //     copy.startDate = this.dateUtils
    //         .convertLocalDateToServer(reports.startDate);
    //     if (reports.endDate) {
    //         copy.endDate = this.dateUtils
    //             .convertLocalDateToServer(reports.endDate);
    //     }
    //     return copy;
    // }

    tempChart = {
                labels: ['VISA', 'MASTER'],
                datasets: [
                    { data: [100, 200], backgroundColor: '#80A6F9', fill: false, label: 'USD' }
                ]
            };
    
    /**Report Output By Pagination**/
    reportOutput(reporttype,path, page, size){
        if(reporttype != "ACCOUNT_BALANCE_REPORT"){
             return this.http.get(`${this.reportOutputUrl}?reportPath=${path}&pageNumber=${page}&pageSize=${size}`).map((res: Response)=>{
                return res;
            });   
        }else{
        }
    }

}
