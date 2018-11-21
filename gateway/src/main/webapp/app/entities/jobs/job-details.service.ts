import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable, Subject } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { Jobs, Parameters, SchedularDetailFilter } from './jobs.model';
import { ResponseWrapper } from '../../shared';
import { saveAs } from 'file-saver';

@Injectable()
export class JobDetailsService {
    
    private resourceUrl = 'agreeapplication/api/job-details';
    private resourceSearchUrl = 'agreeapplication/api/_search/job-details';
    private programsGetUrl = 'agreeapplication/api/programe';
    private postJobUrl = 'agreeapplication/api/postingJobDetails';
    private getJobByIdUrl = 'agreeapplication/api/jobDetailsAndSchedularByJobId';
    private getJobsListUrl = 'agreeapplication/api/getAllJobDetailsByTenantId';
    private isJobNameAvailableUrl = 'agreeapplication/api/isJobNameAvailable';
    private scheduleRerunUrl = 'agreeapplication/api/rerunOozieCoordJob';
    private scheduleKillUrl = 'agreeapplication/api/killOozieCoordJob';
    private scheduleLogUrl = 'agreeapplication/api/OozieJobLog';
    private schedulersListUrl = 'agreeapplication/api/getAllSchedulersList';
    private scheduleActionsListUrl = 'agreeapplication/api/jobActionsByJobId';
    private lookupCodesUrl = 'agreeapplication/api/lookup-codes';
    private getValuesListUrl = 'agreeapplication/api/getRulesByProgParamSetIdAndDependencyId';
     private killOozieJobUrl ='agreeapplication/api/oozieKillJob'; //request param jobId
    private suspendOozieJobUrl ='agreeapplication/api/suspendOozieCoordJob'; //request param jobId
    private resumeOozieJobUrl ='agreeapplication/api/resumeOozieCoordJob'; //request param jobId
    private jobInitiateforAccRecon ='agreeapplication/api/jobIntiateForAcctAndRec'; //request param jobId
    private oozieRerunWFJobUrl = 'agreeapplication/api/oozieRerunWFJob';
    private oozieJobStatus = 'agreeapplication/api/getStatusOfJob';   
    private programsListByTenantIdUrl = 'agreeapplication/api/programsListByTenantId';
    private getLastRunOfAJobUrl = 'agreeapplication/api/getLastRunOfAJob';
    private getNxtRunOfAJobUrl = 'agreeapplication/api/getNxtRunOfAJob';
    private testOozie = 'agreeapplication/api/testOozieStatus';
    private getSchedListByFilters = 'agreeapplication/api/getAllSchedulersListByFilters';
    public parameters: Parameters[];
    private jobSubmitChangeDetectVar = new Subject<string>();
    jobSubmitChangeDetectVar$ = this.jobSubmitChangeDetectVar.asObservable();

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }

    /**
     * Author : Rk, Sameer(Modified)
     * get Programs List by tenant Id
     * @param: programId
     */
    getProgramsList(id?): Observable<Response> {
        return this.http.get(`${this.programsGetUrl}/?programId=${id}`).map((res: Response) => {
            return res.json()
        });
     }

     postJobDetails(currentJobDetails: any): Observable<Response> {
         return this.http.post(`${this.postJobUrl}`, currentJobDetails).map((res: Response) => {
             return res;
         });
     }

     getJobDetailsById(id:any){
        return this.http.get( `${this.getJobByIdUrl}/?Id=${id}` ).map(( res: Response ) =>{
            let jsonResponse = new Jobs(); 
            jsonResponse = res.json();
            if(jsonResponse.scheStartDate !== null) {
                jsonResponse.scheStartDate = new Date(jsonResponse.scheStartDate.toString());
            }
            if(jsonResponse.scheEndDate !== null) {
                jsonResponse.scheEndDate = new Date(jsonResponse.scheEndDate.toString());
            }
            jsonResponse.frequencies.forEach((item) => {
                switch(item.type){
                    case 'ONETIME':
                        item.date = this.dateUtils.convertLocalDateFromServer(item.date);
                    break;
                }
            });
           return jsonResponse;
        });
     }

     getJobsList(page: number, pageSize: number): Observable<Response> {
        return this.http.get( `${this.getJobsListUrl}?page=${page}&per_page=${pageSize}` ).map(( res: Response ) =>{
            return res 
        });
     }

     checkJobNameAvailable(jobName: string){
        return this.http.get( `${this.isJobNameAvailableUrl}/?jobName=${jobName}` ).map(( res: Response ) =>{
            return res.json();
        });
     }
    
    getSchedulersList(page: number, pageSize: number, jobId: any) {
        if (!jobId || jobId == 'undefined' || jobId == null) {
            jobId = 0;
        }
        return this.http.get(`${this.schedulersListUrl}/?page=${page}&per_page=${pageSize}&jobId=${jobId}`).map((res: Response) => {
            return res;
        });
    }

     rerunSchedule(jobId: any){
        /*return this.http.get( `${this.scheduleRerunUrl}/?jobId=${jobId}&rerunType=${rerunType}&scope=${scope}` ).map(( res: Response ) =>{
            return res;
        });*/
         return this.http.get( `${this.oozieRerunWFJobUrl}/?jobId=${jobId}` ).map(( res: Response ) =>{
             return res;
         });   
     }

     killSchedule(jobId: any, rangeType:any, scope:any){
        return this.http.get( `${this.scheduleKillUrl}/?jobId=${jobId}&rangeType=${rangeType}&scope=${scope}` ).map(( res: Response ) =>{
            return res;
        });
     }

     showScheduleLog(jobId: any){
        return this.http.get( `${this.scheduleLogUrl}/?jobId=${jobId}` ).map(( res: any ) =>{
            saveAs(new Blob([res._body], { type: 'text;charset=utf-8' }), jobId.toString()+'.txt');
        });
     }
    
    getScheduleActions(jobId: any){
        return this.http.get( `${this.scheduleActionsListUrl}/?JobId=${jobId}` ).map(( res: Response ) =>{
            return res.json();
        });
     }
    
    // ******* Get lookup values based on lookup code @Rk *************
    getLookupValues(lookupType: string) {
        return this.http.get( `${this.lookupCodesUrl}/${lookupType}` ).map(( res: Response ) => res.json());
    }
    
    getValueListBasedOnDependency(progParamSetId: any, value: any):Observable<Response> {
        return this.http.get(this.getValuesListUrl+'?progParamSetId='+progParamSetId+'&value='+value).map((res: Response) => {
            return res.json()
        });
    };
     /**
     * Author : Shobha
     * @param jobId
     */
    killJob(jobId) {
        return this.http.get(this.killOozieJobUrl+'?jobId='+jobId).map((res: any) => {
            return res;
        });
    }
    /**
     * Author : Shobha
     * @param jobId
     */
    suspendJob(jobId) {
        return this.http.get(this.suspendOozieJobUrl+'?jobId='+jobId).map((res: any) => {
            return res;
        });
    }
    /**
     * Author : Shobha
     * @param jobId
     */
    resumeJob(jobId) {
        return this.http.get(this.resumeOozieJobUrl+'?jobId='+jobId).map((res: any) => {
            return res;
        });
    }

    initiateOnDemandTypeJob(progType: any, userData, paramSet: any): Observable<ResponseWrapper> {
        return this.http.post(`${this.jobInitiateforAccRecon}?progType=${progType}`, paramSet).map((res: Response) => {
            return res.json();
        });
    }

    initiateAcctRecJob(progType: any,paramSet:any): Observable<ResponseWrapper> {
         return this.http.post(this.jobInitiateforAccRecon+'?progType='+progType,paramSet).map((res: Response) => {
            return res.json();
         });
     }

    oozieeJobStatus(jobId:any) {
        return this.http.get(this.oozieJobStatus+'?jobId='+jobId).map((res: any) => {
            return res;
        });
    }

    /**Author: Sameer 
     * Get Schedulers List based on Filter
    */
    filterSchedulersList(page, size, param) {
        let api;
        if(param.type == 'Frequency' ) {
            api = `${this.schedulersListUrl}?page=${page}&per_page=${size}&freType=${param.id}`;
        } else if (param.type == 'child') {
            api = `${this.schedulersListUrl}?page=${page}&per_page=${size}&jobId=${param.id}`;
        } else if (param.type == 'Program') {
            api = `${this.schedulersListUrl}?page=${page}&per_page=${size}&programId=${param.id}&freType=${param.maintype}`;
        }
        return this.http.get(api).map((res: Response) => {
            return res;
        });
    }

    /**
     * Author: Sameer
     * Get all programs list based on tenant id
     */
    programsListByTenantId() {
        return this.http.get(`${this.programsListByTenantIdUrl}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * Author: Sameer
     * Get Last Run of Job
     * @param oozieJobId
     */
    getLastRunOfAJob(oozieJobId) {
        return this.http.get(`${this.getLastRunOfAJobUrl}?oozieJobId=${oozieJobId}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * Author: Sameer
     * Get Next Run of Job
     * @param oozieJobId
     */
    getNxtRunOfAJob(oozieJobId) {
        return this.http.get(`${this.getNxtRunOfAJobUrl}?oozieJobId=${oozieJobId}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author Bhagath, Sameer (Modified)
     * @description Check Oozie Status and db Status
     * @param: db? or null
     */
    oozieDBTest(domain?: any) {
        return this.http.get(`${this.testOozie}${domain ? '?domain=' + domain : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author sameer
     * @description change detector (when jobs submit successful)
     * @param type
     */
    jobSubmitChangeDetect(type) {
        this.jobSubmitChangeDetectVar.next(type);
    }

    /**
     * @author Rk
     * @description Get all schedulars List based on filter object
     */
    getschedularsListByFilter(page:number,per_page:number,filterObj:SchedularDetailFilter): Observable<Response> {
        const arrTemp:string[]=[];
        filterObj.scheduledStatusList.forEach((element) => {
            if(element.isSelected){
                arrTemp.push(element.status);
            }
        });
        filterObj.scheduledStatusList=arrTemp;
        if(filterObj.stDateFrom){
            filterObj.stDateFrom=new Date(filterObj.stDateFrom.getTime() + 86400000);
        }
        if(filterObj.stDateTo){
            filterObj.stDateTo=new Date(filterObj.stDateTo.getTime() + 86400000);
        }
        
        return this.http.post(`${this.getSchedListByFilters}?page=${page}&per_page=${per_page}`,filterObj).map((res: Response) => {
            return res;
        });
    }
}
