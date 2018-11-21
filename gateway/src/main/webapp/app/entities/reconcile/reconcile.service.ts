import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions, ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { ReconDataQueries, SplitRowParams, AnaylticParams, ReconAmounts, SuggestedParams, SelectedGroup ,UnReconKeyValues} from './reconcile.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ReconcileService {
    private queryParametersURL = 'agreeapplication/api/getReconHeaderParamsList';
    private fetchAnalyticsURL = 'agreeapplication/api/getReconCountAndAmounts';
    private reconwqDataURL = 'agreeapplication/api/getReconDataByViewIds';
    private reconciledDataURL = 'agreeapplication/api/getReconciledTransactionsRWQ';
    private reconSourcecustomFilter = 'agreeapplication/api/reconCustomFilterForSource';
    private reconTargetcustomFilter = 'agreeapplication/api/reconCustomFilterForTarget';
    private selectedCountAmountURL = 'agreeapplication/api/getCountAndAmountRecordWise';
    private fetchColumnsBasedonViewId = 'agreeapplication/api/getColumnNamesWithGroupByTrue';
    private manualRecURL = 'agreeapplication/api/postManualReconData';
    private manualUnRecURL = 'agreeapplication/api/processManualUnReconDataAutoAcct';
    private getQualifierColumnsURL = 'agreeapplication/api/getColumnQualifierInfo';
    private fetchAmounts = 'agreeapplication/api/getReconUnReconAmounts';
    private getRecentActivitiesURL = 'agreeapplication/api/getRecentActivitiesByModule';
    public reconSchJob: boolean;
    public selectedGroup: SelectedGroup;
    public isAllData:any[] = [];
    private splitRowURL = 'agreeapplication/api/postDataChild';
    private columnHeadersURL = 'agreeapplication/api/getReconColumnAlignmentInfo';
    private fetchRuleGroupIdsURL = 'agreeapplication/api/fetchRuleGroupIdsByTenantIdAndPurpose';
    private suggestedReconileURL = 'agreeapplication/api/postSuggestedData';
    private allGroupsDataURL = 'agreeapplication/api/getReconSummaryByRuleGroup';
    private reconChildURL = 'agreeapplication/api/getReconRefTransactions';
    private reconcileExportURL = 'agreeapplication/api/getReconciledTransactions';
    private unReconcileExportURL = 'agreeapplication/api/getUnReconciledTransactions';
    private reconTotalCustomFilterURL = 'agreeapplication/api/getReconCustomFilterInfo';
    public ENABLE_RULE_BLOCK = false;

    constructor(private http: Http, private dateUtils: JhiDateUtils
    ) { }

    getHeaderColumns(groupId: number, dataViewId: number, sourceOrTarget: any, status: any, type: any, groupBy: any): Observable<Response> {
        return this.http.get(this.columnHeadersURL + '?groupId=' + groupId + '&viewId='
            + dataViewId + '&sourceOrTarget=' + sourceOrTarget + '&status=' + status + '&type=' + type + '&groupBy=' + groupBy)
            .map((res: Response) => {
                return res.json();
            });
    };
    getHeaderColumnsByRuleId(groupId: number, dataViewId: number, sourceOrTarget: any, status: any, type: any, ruleId: any, groupBy: any): Observable<Response> {
        return this.http.get(this.columnHeadersURL + '?groupId=' + groupId + '&viewId='
            + dataViewId + '&sourceOrTarget=' + sourceOrTarget + '&status=' + status + '&type=' + type + '&ruleId=' + ruleId + '&groupBy=' + groupBy)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchRuleGroupIds() {
        return this.http.get(this.fetchRuleGroupIdsURL + '?purpose=APPROVALS')
            .map((res: Response) => {
                return res.json();
            });
    }

    fetchQueryParams(groupId: any): Observable<Response> {
        return this.http.get(this.queryParametersURL + '?groupId='
            + groupId)
            .map((res: Response) => {
                return res.json();
            });
    };


    reconAmounts(data: ReconAmounts): Observable<Response> {
        return this.http.post(this.fetchAmounts, data)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchRqCountAmount(analyticParameters: AnaylticParams): Observable<Response> {
        return this.http.post(this.fetchAnalyticsURL, analyticParameters)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchAllReconWqRecords(pageNumber: any, pageSize: any, reconQueryParams: ReconDataQueries): Observable<Response> {
        return this.http.post(this.reconwqDataURL + '?pageNumber=' + pageNumber + '&pageSize='
            + pageSize, reconQueryParams)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchReconciledRecords(pageNumber: any, pageSize: any, reconQueryParams: ReconDataQueries): Observable<Response> {
        return this.http.post(this.reconciledDataURL + '?pageNumber=' + pageNumber + '&pageSize='
            + pageSize, reconQueryParams)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchTRecordsCustomFilter(groupId: any,tViewId: any,  recReferences: any, pageNumber: any, pageSize: any): Observable<Response> {
        return this.http.get(this.reconTargetcustomFilter + '?groupId='
            + groupId + '&tViewId=' + tViewId + '&recReferences=' + recReferences + '&pageNumber=' + pageNumber + '&pageSize='
            + pageSize)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchSRecordsCustomFilter(groupId: any, sViewId: any, recReferences: any,pageNumber: any, pageSize: any): Observable<Response> {
        return this.http.get(this.reconSourcecustomFilter + '?groupId='
            + groupId + '&sViewId=' + sViewId + '&recReferences=' + recReferences + '&pageNumber=' + pageNumber + '&pageSize='
            + pageSize)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchTotalCustomFilter(groupId:any, sViewId:any, tViewId:any,recReferences:any): Observable<Response> {
        return this.http.get(this.reconTotalCustomFilterURL + '?groupId='
        + groupId + '&sViewId=' + sViewId + '&tViewId=' + tViewId  + '&recReferences=' + recReferences).map((res:Response) => {
            return res.json();
        });
    }

    fetchCountAmountonSelectSource(viewId: any, rowIds: any): Observable<Response> {
        return this.http.post(this.selectedCountAmountURL + '?viewId=' + viewId, rowIds)
            .map((res: Response) => {
                return res.json();
            });
    };

    manualRecon(manualRecDTOs: any) {
        return this.http.post(this.manualRecURL, manualRecDTOs)
            .map((res: Response) => {
                return res.json();
            });
    }

    manualUNRecon(params:UnReconKeyValues) {
        return this.http.post(this.manualUnRecURL,params)
            .map((res: Response) => {
                return res.json();
            });
    }

    getQualifierColumns(viewId: any): Observable<Response> {
        return this.http.get(this.getQualifierColumnsURL + '?viewId='
            + viewId)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchRecentActivities(module: any, fromDate: any, toDate: any): Observable<Response> {
        return this.http.get(this.getRecentActivitiesURL + '?module=' + module + '&fromDate=' + fromDate + '&toDate=' + toDate)
            .map((res: Response) => {
                return res.json();
            });
    };

    splitRowPost(dataChilds: SplitRowParams[]): Observable<ResponseWrapper> {
        return this.http.post(this.splitRowURL, dataChilds).map((res: Response) => {
            return res.json();
        });
    }

    suggestedReconciliation(params: SuggestedParams) {
        return this.http.post(this.suggestedReconileURL, params).map((res: Response) => {
            return res.json();
        });
    }

    reconAllData(): Observable<Response> {
        return this.http.get(this.allGroupsDataURL).map((res: Response) => {
            return res.json();
        });
    }

    getReconChildData(params: any) {
        return this.http.post(this.reconChildURL + '?pageNumber=' + 0 + '&pageSize='
        + 3, params).map((res: Response) => {
            return res.json();
        });
    }

    exportReconciledAPI(ruleGroupName:any,sourceViewName:any,targetViewName:any,type:any,filterColumns:any,pageNumber?:any,pageSize?:any){
        filterColumns = {};
        return this.http.post(this.reconcileExportURL  + '?ruleGroupName=' + ruleGroupName + '&sourceViewName=' + sourceViewName + '&targetViewName=' + targetViewName 
        + '&fileExport=' + 'YES' + '&fileType=' + type,filterColumns,{ responseType: ResponseContentType.Blob }).map((res: Response) => {
            return res;
        });
    }

    exportUnReconciledAPI(ruleGroupName:any,type:any,filterColumns:any,sourceViewName?:any,targetViewName?:any,fileType?:any,pageNumber?:any,pageSize?:any,fileExport?:any){
        filterColumns = {};
        if(type == 'source'){
            return this.http.post(this.unReconcileExportURL  + '?ruleGroupName=' + ruleGroupName + '&sourceViewName=' + sourceViewName
            + '&fileExport=' + 'YES' + '&fileType=' + fileType,filterColumns,{ responseType: ResponseContentType.Blob }).map((res: Response) => {
                return res;
            });
        } else {
            return this.http.post(this.unReconcileExportURL  + '?ruleGroupName=' + ruleGroupName + '&targetViewName=' + targetViewName 
            + '&fileExport=' + 'YES' + '&fileType=' + fileType,filterColumns,{ responseType: ResponseContentType.Blob }).map((res: Response) => {
                return res;
            });
        }
        
    }
    
    fetchGroupByColumns(viewId:any){
        return this.http.get(this.fetchColumnsBasedonViewId + '?viewId=' + viewId).map((res: Response) => {
            return res.json();
        });
    }
}
