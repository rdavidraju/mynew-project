import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import 'rxjs/add/operator/toPromise';
import { SplitRowParams,SelectedAcctGroup, JsonforAccounting,AwqAllParams,FilteredParams,RecordParams,AcctKeyValues,SelectedAcctRows } from './accounting-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
// import { SessionStorageService } from 'ng2-webstorage';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, DialogModule, InputMaskModule, TreeTableModule, TreeNode, SharedModule } from 'primeng/primeng';
@Injectable()
export class AccountingDataService {
    
    private acctRecordsPost = 'agreeapplication/api/postManualAccData';
    private acctExportToExcel = 'agreeapplication/api/exportAccDataToExcelFile';
    private distinctStatusSummary = 'agreeapplication/api/getAWQStatusesCountsNAmounts';
    private awqGroupingSummary = 'agreeapplication/api/getAWQGroupingSummaryInfo';
    private awqDetailedInfo = 'agreeapplication/api/getAccountingDetailInfo';
    public acctSchJob:boolean;
    private awqManualUnAcct = 'agreeapplication/api/manualUnAccDataAutoAcc';
    private accountedSummaryURL = 'agreeapplication/api/getAccountedSummaryInfo';
    private journalTemplates = 'agreeapplication/api/getTemplateIdAndNameByDataViewId';
    private splitRowURL = 'agreeapplication/api/postDataChild';
    private acctQueryParamsURL = 'agreeapplication/api/getAccHeaderParamsList';
    private accountedColsURL = 'agreeapplication/api/getColumnAllignForAccountedSummary';
    private accColHeadersURL = 'agreeapplication/api/getAccountingColumnAlignmentInfo';
    private getCoaByGroupIdUrl = 'agreeapplication/api/getCoaDetailsByGrpId';
    public selectedAcctGroup:SelectedAcctGroup;
    private allGroupsDataURL = 'agreeapplication/api/getAcctSummaryByRuleGroup';
    private getAcctQualifierColumnsURL = 'agreeapplication/api/getColumnQualifierInfo';
    private unAccountedSummaryURL = 'agreeapplication/api/getUnAccountedGroupInfo';
    public isAcctAllData:any[] = [];
    constructor(private http: Http, private dateUtils: JhiDateUtils
    ) { }


    fetchingColHeaders(groupId: any, viewId: any, status: any): Observable<Response> {

        return this.http.get(this.accColHeadersURL + '?groupId=' + groupId + '&viewId='
            + viewId + '&status=' + status)
            .map((res: Response) => {
                return res.json();
            });

    };

    /* Fetch Accounted Summary Columns Info */
    accountedColumnHeaders(status: any): Observable<Response> {
        return this.http.get(this.accountedColsURL  + '?status=' + status)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchAcctQueryParams(groupId: any): Observable<Response> {

        return this.http.get(this.acctQueryParamsURL + '?groupId=' + groupId)
            .map((res: Response) => {
                return res.json();
            });

    };

    manualAcct(manualAccDTO: JsonforAccounting): Observable<ResponseWrapper> {
        return this.http.post(this.acctRecordsPost , manualAccDTO).map((res: Response) => {
            return res.json();
        });
    }


    acctDataExcelExport(filterGroupBy: any, params: RecordParams): Observable<ResponseWrapper> {
        return this.http.post(this.acctExportToExcel + '?filterGroupBy=' + filterGroupBy, params).map((res: Response) => {
            return res.json();
        });
    }

    distinctStatuses( params: AwqAllParams): Observable<ResponseWrapper> {
            return this.http.post(this.distinctStatusSummary, params).map((res: Response) => {
                return res.json();
            });
    }

    groupingSummarySidebar( params: AwqAllParams): Observable<ResponseWrapper> {
            return this.http.post(this.awqGroupingSummary, params).map((res: Response) => {
                return res.json();
            });
    }

    detailedInfoRecords( params: AwqAllParams): Observable<ResponseWrapper> {
            return this.http.post(this.awqDetailedInfo, params).map((res: Response) => {
                return res.json();
            });
    }

    accountedSummaryInfo( params: AwqAllParams): Observable<ResponseWrapper> {
            return this.http.post(this.accountedSummaryURL, params).map((res: Response) => {
                return res.json();
            });
    }

    manualUnAcctNew( params: AwqAllParams): Observable<ResponseWrapper> {
        return this.http.post(this.awqManualUnAcct, params).map((res: Response) => {
            return res.json();
        });
    }


    journalTemplatesByViewId(viewId:any){
        return this.http.get(this.journalTemplates + '?dataViewId=' + viewId).map((res: Response) => {
            return res.json();
        });
    }


    splitRowPost( dataChilds:SplitRowParams[]): Observable<ResponseWrapper> {
         return this.http.post(this.splitRowURL, dataChilds).map((res: Response) => {
             return res.json();
         });
     }

    getCoaByGroupIdService(ruleGrpId: any){
        return this.http.get(this.getCoaByGroupIdUrl + '?ruleGrpId=' + ruleGrpId).map((res: Response) => {
            return res.json();
        });
    }

    acctAllData(): Observable<Response> {   
        return this.http.get(this.allGroupsDataURL).map((res: Response) => {
            return res.json();
        });
    }

    getAcctQaulifiers(viewId:any): Observable<Response> {   
        return this.http.get(this.getAcctQualifierColumnsURL + '?viewId='
        + viewId).map((res: Response) => {
            return res.json();
        });
    }

    getUnAcctSummary( params: AwqAllParams): Observable<Response> {
        return this.http.post(this.unAccountedSummaryURL,params).map((res: Response) => {
            return res.json();
        });
    }
    
}