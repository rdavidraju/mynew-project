import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import { Subject }    from 'rxjs/Subject';

@Injectable()
export class NavBarService {
    
    public loggedInUsersList = [];
    //private UserData = this.$sessionStorage.retrieve('userData');
    private themeCol = new Subject<string>();
    private schedulersListUrl = 'agreeapplication/api/getAllSchedulersListByFilters';
    private notificationListUrl = 'agreeapplication/api/getNotificationsDataByUserId';
    private notificationisViewedUrl = 'agreeapplication/api/updateIsViewedAsTrue?notificationsId=';
    private getCountsForDashBoardUrl = 'agreeapplication/api/getCountsForDashBoard';
    /* private getAgingWiseAnalysisForDashBoardQueryUrl = 'agreeapplication/api/AgingWiseAnalysisForDashBoardQuery'; */
    private getAgingWiseAnalysisForDashBoardQueryUrl = 'agreeapplication/api/reconciliationAndAccoutingAgingWiseAnalysisForDashBoard'; /* recon and accounted */
    private getAgingAnalsisForUnReconciliationUrl = 'agreeapplication/api/getAgingAnalsisForUnReconciliation';
    private getSummaryInfoForReconciliationUrl = "agreeapplication/api/getSummaryInfoForReconciliation";
    private getSummaryInfoForAccountedUrl = "agreeapplication/api/getSummaryInfoForAccounted";
    private getNotificationDetailsUrl = 'agreeapplication/api/getNotificationsDataByUserIdAndTenantId';
    private getAgingAnalsisForAccountingSummaryUrl = "agreeapplication/api/getAgingAnalsisForAccountingSummary";
    private getEachDayAnalysisForAProcessUrl = "agreeapplication/api/getEachDayAnalysisForAProcess";
    private getprocessessByTenantIdUrl = "agreeapplication/api/processessByTenantId";
    private getExtractionDataUrl = "agreeapplication/api/getAgingAnalysisExtraction";
    private getTransformationDataUrl = "agreeapplication/api/getAgingAnalysisForTransformation";
    private getEachModuleAnalysisForAProcessUrl = "agreeapplication/api/getEachModuleAnalysisForAProcess";
    private reconciliationAnalysisforGivenPeriodUrl = "agreeapplication/api/reconciliationAnalysisforGivenPeriod";
    private getSummaryInfoForReconciliationV2Url = "agreeapplication/api/getSummaryInfoForReconciliationV2";
    private AccountingAnalysisforGivenPeriodUrl = "agreeapplication/api/AccountingAnalysisforGivenPeriod";
    private getSummaryInfoForAccountingV2Url = "agreeapplication/api/getSummaryInfoForAccountingV2";
    private getLovGroupByUrl = "agreeapplication/api/getLovGroupBy";
    private getUnProcessedOrProcessedDataUrl = "agreeapplication/api/getUnProcessedOrProcessedData";
    private getUnProcessedOrProcessedDataForAllGroupByUrl = "agreeapplication/api/getUnProcessedOrProcessedDataForAllGroupBy";
    private getUnReconciledRecordsForViolationUrl = "agreeapplication/api/getUnReconciledRecordsForViolation";
    private getUnApprovedRecordsUrl = "agreeapplication/api/getUnApprovedRecords";
    private extractionAnalysisforGivenPeriodUrl = "agreeapplication/api/extractionAnalysisforGivenPeriod";
    private transformationAnalysisforGivenPeriodUrl = "agreeapplication/api/transformationAnalysisforGivenPeriod";
    private extractionAndTransformationUnprocessedCountsUrl = "agreeapplication/api/extractionAndTransformationUnprocessedCounts";
    private journalsUnprocessedCountsUrl = "agreeapplication/api/journalsUnprocessedCounts";
    private processDetailsForCalenderUrl = "agreeapplication/api/processDetailsForCalender";
    private detailInformationForApprovalsUrl = "agreeapplication/api/detailInformationForApprovals";
    private detailInformationForJournalsUrl = "agreeapplication/api/detailInformationForJournals";
    private detailInformationForJournalsByViewUrl = "agreeapplication/api/detailInformationForJournalsByView";
    private weekAnalysisForModuleUrl = "agreeapplication/api/weekAnalysisForModule";
    private postFormConfigDetailsUrl = "agreeapplication/api/postFormConfigDetails";
    private getFormConfigDetailsUrl = "agreeapplication/api/getFormConfigDetails";
    private getCommentsURL = "api/getUserComments";
    private postCommentsURL = "api/postUserComments";
    private postUnReadCommentsURL = "api/updateIsReadAsTrue";
    private deleteCommentURL = "api/deleteUserComments";
    private logoutURL = "api/logout";
    private loggedInUsersURL = "api/loggedInUsers";
    //Notification List Global Variable
    public notificationlist: any;
    themeCol$ = this.themeCol.asObservable();


    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService
    ) { }
    
    publishData(data: string) {
        this.themeCol.next(data);
      }

    getSchedulersList(page: number, pageSize: number, filter: object) {
        return this.http.post(`${this.schedulersListUrl}/?page=${page}&per_page=${pageSize}`, filter).map((res: Response) => {
            return res;
        });
    }
    
    getprocessessList(){
        return this.http.get( `${this.getprocessessByTenantIdUrl}` ).map(( res: Response ) =>{
            return res.json();
        });
    }

     getNotificationList(){
         /* if(userData != undefined){ */
            return this.http.get(`${this.notificationListUrl}`).map((res: Response)=>{
                const jsonResponse = res.json();
                return jsonResponse; 
            });
         /* } */
     }

     notificationisViewed(isViewed){
        return this.http.post(this.notificationisViewedUrl, isViewed).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
     }

     /* service to display  getCountsForDashBoardUrl*/
    fetchCountsForDashBoard(processId,obj){
        //console.log('date obj for ETL data in service ::' + JSON.stringify(obj));
        //console.log('processId for ETL data in service ::' + processId);
        /* if(userDetails != undefined){ */
            return this.http.post(this.getCountsForDashBoardUrl + '?processId='+ processId,obj).map((res: Response) => {
                //console.log('res ' + res);
                const jsonResponse = res.json();
                return jsonResponse;
            });  
        /* } */
    }
    /* service to fetch Unreconciled Analysis */
    fetchUnReconAnalysis(obj){
        return this.http.post(this.getAgingAnalsisForUnReconciliationUrl,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    /* service to fetch Not Accounted Analysis Data */
    fetchNotAccountedDataAnalysis(obj){
        return this.http.post(this.getAgingAnalsisForAccountingSummaryUrl,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* service to fetch eachday analysis */
    fetchEachDayAnalysis(processId,obj){
        //console.log('proid ' + processId);
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(this.getEachDayAnalysisForAProcessUrl+'?processId='+processId,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to fetch each module analysis */
   // getEachModuleAnalysisForAProcess?processId=9&tenantId=5
    fetchModuleAnalysis(processId,obj){
        //console.log('proid ' + processId);
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(this.getEachModuleAnalysisForAProcessUrl+'?processId='+processId,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to fetch reconciliation analysis for given period */
    fetchReconciliationDetailedData(processId,violationPeriod,obj){
        
        return this.http.post(this.getSummaryInfoForReconciliationV2Url+'?processId='+processId+'&violation='+violationPeriod,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to fetch reconciliation analysis for given period */
    fetchAccountingDetailedData(processId,violationPeriod,obj){
        return this.http.post(this.getSummaryInfoForAccountingV2Url+'?processId='+processId+'&violation='+violationPeriod,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
    /* Service to fetch reconciliation analysis for given period */
    fetchReconciliationForGivenPeriod(processId,obj){
        return this.http.post(this.reconciliationAnalysisforGivenPeriodUrl+'?processId='+processId,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to fetch accounting analysis for given period */
    fetchAccountingForGivenPeriod(processId,obj){
        return this.http.post(this.AccountingAnalysisforGivenPeriodUrl+'?processId='+processId,obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
    //reconciliationAnalysisforGivenPeriod?processId=9&tenantId=5
     fetchCountsForDashBoardRecon(obj,processId){
        //console.log('date obj for reconciliation data in service ::' + JSON.stringify(obj));
        //console.log('processId for reconciliation data in service ::' + processId);
            return this.http.post(this.getSummaryInfoForReconciliationUrl + '?processId='+processId,obj).map((res: Response) => {
                //console.log('res ' + res);
                const jsonResponse = res.json();
                return jsonResponse;
            });
    } 
        
    fetchCountsForDashBoardAccounted(obj,processId){
        //console.log('date obj for accounting data in service ::' + JSON.stringify(obj));
        //console.log('process id for accounting data in service ::' + processId);
            return this.http.post(this.getSummaryInfoForAccountedUrl + '?processId='+processId,obj).map((res: Response) => {
                //console.log('res ' + res);
                const jsonResponse = res.json();
                return jsonResponse;
            });
    }

    /* service to get  */
    getAgingWiseAnalysis(obj:any) {
        //console.log('obj for aging analysis ::' + JSON.stringify(obj));
        return this.http.post(`${this.getAgingWiseAnalysisForDashBoardQueryUrl}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    } 

    /* Service to get notification details in dashboard */
   // getNotificationsDataByUserIdAndTenantId?tenantId=9&userId=9&module=All&day=last7days
    fetchNotificationDetailsForDashBoard(module,day){
        /* //console.log('userDetails ' + JSON.stringify(userDetails));
        //console.log('module ' + module + ' day ' + day); */
        /* if(userDetails != undefined){ */
            return this.http.get(this.getNotificationDetailsUrl +'?module='+module+'&day=' + day).map((res: Response) => {
                ////console.log('userDetails res ' + JSON.stringify(res.json()));
                const jsonResponse = res.json();
                return jsonResponse;
            });  
        /* } */
    }

    /* private getExtractionDataUrl = "agreeapplication/api/getAgingAnalysisExtraction";
    private getTransformationDataUrl = "agreeapplication/api/getAgingAnalysisForTransformation"; */
    /* Service to fetch Extraction Data */
    getExtractionAnalysis(obj:any) {
        //console.log('obj for getExtractionAnalysis ::' + JSON.stringify(obj));
        return this.http.post(`${this.getExtractionDataUrl}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
    /* Service to fetch Transformation Data */
    getTransformationAnalysis(obj:any) {
        //console.log('obj for getTransformationDataUrl ::' + JSON.stringify(obj));
        return this.http.post(`${this.getTransformationDataUrl}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getReconGroupByLov(viewId){
        return this.http.get(`${this.getLovGroupByUrl}?viewId=${viewId}`).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse; 
        });
    }

    getUnProcessedOrProcessedData(processId, viewId, moduleName, amtQuailifier, groupByColmns,obj,colName?) {
        //console.log('processId ' + processId + ' viewId ' + viewId + ' moduleName ' +moduleName+ ' amtQuailifier ' + amtQuailifier);
        //console.log('groupByColmns ' + JSON.stringify(groupByColmns));
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(`${this.getUnProcessedOrProcessedDataUrl}?processId=${processId}&viewId=${viewId}&module=${moduleName}&amtQuailifier=${amtQuailifier}&groupByColmns=${groupByColmns}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getUnProcessedOrProcessedDataList(processId, viewId, moduleName, amtQuailifier, groupByColmns,obj) {
        //console.log('processId ' + processId + ' viewId ' + viewId + ' moduleName ' +moduleName+ ' amtQuailifier ' + amtQuailifier);
        //console.log('groupByColmns ' + JSON.stringify(groupByColmns));
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(`${this.getUnProcessedOrProcessedDataForAllGroupByUrl}?processId=${processId}&viewId=${viewId}&module=${moduleName}&amtQuailifier=${amtQuailifier}&groupByColmns=${groupByColmns}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getUnProcessedOrProcessedDataViolation(processId, viewId, moduleName, amtQuailifier, groupByColmns, moduleType, violationPeriod, obj) {
        //console.log('processId ' + processId + ' viewId ' + viewId + ' moduleName ' +moduleName+ ' amtQuailifier ' + amtQuailifier);
        //console.log('groupByColmns ' + JSON.stringify(groupByColmns));
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(`${this.getUnReconciledRecordsForViolationUrl}?processId=${processId}&viewId=${viewId}&amtQuailifier=${amtQuailifier}&groupByColmns=${groupByColmns}&module=${moduleType}&violation=${violationPeriod}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getUnProcessedOrProcessedDataApproved(processId, viewId, moduleName, amtQuailifier, groupByColmns, moduleType,obj) {
        //console.log('processId ' + processId + ' viewId ' + viewId + ' moduleName ' +moduleName+ ' amtQuailifier ' + amtQuailifier);
        //console.log('groupByColmns ' + JSON.stringify(groupByColmns));
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(`${this.getUnApprovedRecordsUrl}?processId=${processId}&viewId=${viewId}&amtQuailifier=${amtQuailifier}&groupByColmns=${groupByColmns}&module=${moduleType}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

//getUnApprovedRecords?processId=1&viewId=35&amtQuailifier=INVOICETTLAMT_77&groupByColmns=CURRENCYCODE_77&module=reconciled
    getExtractionAnalysisData(processId,obj) {
        //console.log('processId ' + processId);
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(`${this.extractionAnalysisforGivenPeriodUrl}?processId=${processId}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
    getTransformationAnalysisData(processId,obj) {
        //console.log('processId ' + processId);
        //console.log('obj ' + JSON.stringify(obj));
        return this.http.post(`${this.transformationAnalysisforGivenPeriodUrl}?processId=${processId}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getETLDetailedAnalysisData(processId,obj) {
        return this.http.post(`${this.extractionAndTransformationUnprocessedCountsUrl}?processId=${processId}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getJournalsAnalysisData(processId,obj) {
        return this.http.post(`${this.journalsUnprocessedCountsUrl}?processId=${processId}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getCalenderDetails(processId) {
        return this.http.get(`${this.processDetailsForCalenderUrl}?processId=${processId}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getApproversAnalysisData(processId, viewId, moduleType, obj) {
        return this.http.post(`${this.detailInformationForApprovalsUrl}?processId=${processId}&viewId=${viewId}&module=${moduleType}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getdetailInfoForJE(processId,obj,status?) {
        if(status){
            return this.http.post(`${this.detailInformationForJournalsUrl}?processId=${processId}&status=${status}`, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }else{
            return this.http.post(`${this.detailInformationForJournalsUrl}?processId=${processId}`, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }

    getdetailInfoForJEByView(processId, viewId, grpBy, status, obj) {
        return this.http.post(`${this.detailInformationForJournalsByViewUrl}?processId=${processId}&viewId=${viewId}&groupByCol=${grpBy}&status=${status}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getWeekAnalysisForModule(processId, module, obj) {
        return this.http.post(`${this.weekAnalysisForModuleUrl}?processId=${processId}&module=${module}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author Amit, Sameer (Modified)
     * @param formConfig 
     * @param formLevel 
     * @param type 
     * @param obj 
     */
    postFormConfigDetails(formConfig, formLevel, type, obj) {
        return this.http.post(`${this.postFormConfigDetailsUrl}?formConfig=${formConfig}&formLevel=${formLevel}&type=${type}`, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author Amit, Sameer (Modified)
     * @param formConfig 
     * @param formLevel 
     * @param type 
     */
    getFormConfigDetails(formConfig, formLevel, type) {
        return this.http.get(`${this.getFormConfigDetailsUrl}?formConfig=${formConfig}&formLevel=${formLevel}&type=${type}`).map((res: Response) => {
            const contentType = res.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                const jsonResponse = res.json();
                return jsonResponse;
            }
        });
    }

    /**
     * @author sameer
     * @description change detector (when login successful to apply user theme)
     */
    private loginSuccessVar = new Subject<string>();
    loginSuccessVar$ = this.loginSuccessVar.asObservable();
    loginDetect() {
        this.loginSuccessVar.next();
    }

    /** 
     * @author Sameer
     * @description When theme, font or mode change
    */
   private preferenceChangeVar = new Subject<string>();
   preferenceChangeVar$ = this.preferenceChangeVar.asObservable();
    preferenceChangeDetect(val?) {
        this.preferenceChangeVar.next(val);
    }

    getAllComments(): Observable<Response>  {
        return this.http.get(this.getCommentsURL).map((res: Response) => {
            return res.json();
        });
    }

    postComments(data:any) {
        return this.http.post(this.postCommentsURL,data).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
        });
    }

    postUnReadComments(){
        return this.http.get(this.postUnReadCommentsURL).map((res : Response) => {
            return res.json();
        })
    }

    deleteCommentService(commentId,commentOrReply){
        return this.http.get(this.deleteCommentURL+"?commentId="+commentId+"&commentOrReply="+commentOrReply).map((res : Response) => {
            return res.json();
        })
    }
 
    logout(){
        return this.http.get(`${this.logoutURL}`).map((res: Response)=>{
            return res; 
        });
    }   

    loggedInUsersService(){
        return this.http.get(this.loggedInUsersURL).map((res : Response) => {
            return res.json();
        })
    }
}