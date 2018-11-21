import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { TreeTableModule, TreeNode, SharedModule } from 'primeng/primeng';

import { Reconcile, customFilters, reconDataQueries, anaylticParams ,reconAmounts } from './reconcile.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class ReconcileService {
    private queryParametersURL = 'agreeapplication/api/getReconHeaderParamsList';
    private fetchAnalyticsURL = 'agreeapplication/api/getReconCountAndAmounts';
    private reconwqDataURL = 'agreeapplication/api/getReconDataByViewIds';
    private reconwqcustomFilter = 'agreeapplication/api/reconCustomFilter';
    private selectedCountAmountURL = 'agreeapplication/api/getCountAndAmountRecordWise';
    private fetchColumnsBasedonViewId = 'agreeapplication/api/getColumnNamesWithGroupByTrue';
    private manualRecURL = 'agreeapplication/api/postManualReconData';
    private manualUnRecURL = 'agreeapplication/api/processManualUnReconDataAutoAcct';
    private getAnalyticsMultiCurrURL = 'agreeapplication/api/getReconCountAmountsWithMultiCurrency';
    private getQualifierColumnsURL = 'agreeapplication/api/getColumnQualifierInfo';
    private exportReconDataURL = 'agreeapplication/api/exportReconDataToExcelFile';
    private postFormConfigURL = 'agreeapplication/api/postFormConfigParams';
    private fetcFormConfigURL = 'agreeapplication/api/getFormConfigParams';
    private fetchAmounts = 'agreeapplication/api/getReconUnReconAmounts';
    public reconSchJob:boolean;


    private resourceUrl = 'agreeapplication/api/reconciles';
    private resourceSearchUrl = 'agreeapplication/api/_search/reconciles';
    private reconcileURL = 'agreeapplication/api/reconcileData';
    private reconciledDataByYearURL = 'agreeapplication/api/reconciledDataByYear';
    private reconciliationResultURL = 'agreeapplication/api/getReconciliationCountAndAmount';
    public apiHost: string = '../jsonfolder/reconciledata.json';
    private getRecordsForReconcileURL = 'agreeapplication/api/getReconciliationDataByViewId';
    //private UserData = this.$sessionStorage.retrieve('userData');
    private customFilterRecordsURL = 'agreeapplication/api/getRecDataWithCustomFilter'; 
    private reconcileSideBarDataURL = 'agreeapplication/api/getReconViewColumnValueSet';
    private columnHeadersURL = 'agreeapplication/api/getReconColumnAlignmentInfo';
    private fetchRuleGroupIdsURL = 'agreeapplication/api/fetchRuleGroupIdsByTenantIdAndPurpose';


    //private reconcileDataUrl = "reconcile/reconcile-data.json";
    public ENABLE_RULE_BLOCK: boolean = false;

    constructor(private http: Http, private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService) { }
    getYearlyReconcileData(year: any): Observable<ResponseWrapper> {
        return this.http.get(`${'agreeapplication/api/reconciledYearlyData'}/${year}`)
            .map((res: any) => this.convertResponse(res));
    }

    create(reconcile: Reconcile): Observable<Reconcile> {
        const copy = this.convert(reconcile);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(reconcile: Reconcile): Observable<Reconcile> {
        const copy = this.convert(reconcile);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Reconcile> {
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.transactionDate = this.dateUtils
            .convertLocalDateFromServer(entity.transactionDate);
    }

    private convert(reconcile: Reconcile): Reconcile {
        const copy: Reconcile = Object.assign({}, reconcile);
        copy.transactionDate = this.dateUtils
            .convertLocalDateToServer(reconcile.transactionDate);
        return copy;
    }

    // private countURL = './assets/counts.json';

    // getCounts(): Observable<any> {
    //     return this.http.get(this.countURL).map((res: any) => {
    //         return res.json();
    //     });;
    // }

    fetchGroupedDataByYear(year: any): Observable<Response> {
        console.log('fetch by year' + year);
        return this.http.get(this.reconciledDataByYearURL + '?year=' + year);
    }

    fetchGroupedData(queryInput: string, columns: any): Observable<Response> {
        console.log('input query in service is:' + queryInput + 'columns:' + columns);
        return this.http.get("http://localhost:8080/agreeapplication/api/groupedData" + '?query=' + queryInput + '&columns=' + columns);
    }


    reconcileList(): Observable<Response> {
        return this.http.get(this.apiHost).map((res: Response) => {
            let jsonResponse = res.json();
            console.log('reconcile data from service :' + JSON.stringify(jsonResponse));
            return jsonResponse;

        });

    }

    reconciliationData(ruleGroupId: any): Observable<Response> {
        return this.http.get(this.reconciliationResultURL + '?groupId=' + ruleGroupId).map((res: Response) => {
            return res.json();
        });
    }

    fetchAllReconciledRecords(dataViewId: any, sourceOrTarget: any, groupId: number, page: number, per_page: number): Observable<Response> {

        return this.http.get(this.getRecordsForReconcileURL + '?dataViewId='
            + dataViewId + '&sourceOrTarget=' + sourceOrTarget + '&groupId=' + groupId + '&pageNumber=' + page + '&pageSize=' + per_page)
            .map((res: Response) => {
                return res.json();
            });
    };

    customFilterSearch(data: any): Observable<ResponseWrapper> {
       // data.tenantId = this.UserData.tenantId;
        let copy: customFilters = Object.assign({}, data);
        return this.http.post(this.customFilterRecordsURL, copy).map((res: Response) => {
            return res.json();
        });
    };

    getHeaderColumns(groupId: number, dataViewId: number, sourceOrTarget: any): Observable<Response> {

        return this.http.get(this.columnHeadersURL + '?groupId=' + groupId + '&viewId='
            + dataViewId + '&sourceOrTarget=' + sourceOrTarget)
            .map((res: Response) => {
                return res.json();
            });
    };

    // reconcileRecordsPost(data: any): Observable<ResponseWrapper> {
    //     console.log('data service ' + JSON.stringify(data));
    //     return this.http.post(this.reconciliationResultPostURL + '?tenantId=' + this.UserData.tenantId + '&userId=' + this.UserData.id, data).map((res: Response) => {
    //         return res.json();
    //     });
    // };

    fetchRecordsBasedOnStatus(dataViewId: any, sourceOrTarget: any, status: any, groupId: number, page: number, per_page: number): Observable<Response> {

        return this.http.get(this.getRecordsForReconcileURL + '?dataViewId='
            + dataViewId + '&sourceOrTarget=' + sourceOrTarget + '&status=' + status + '&groupId=' + groupId + '&pageNumber=' + page + '&pageSize=' + per_page)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchSidebarData(dataViewId: any, groupId: number, sourceOrTarget: any): Observable<Response> {

        return this.http.get(this.reconcileSideBarDataURL + '?viewId='
            + dataViewId + '&groupId=' + groupId + '&sourceOrTarget=' + sourceOrTarget)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchSidebarDataBasedOnStatus(dataViewId: any, groupId: number, sourceOrTarget: any, status: any): Observable<Response> {

        return this.http.get(this.reconcileSideBarDataURL + '?viewId='
            + dataViewId + '&groupId=' + groupId + '&sourceOrTarget=' + sourceOrTarget + '&status=' + status)
            .map((res: Response) => {
                return res.json();
            });
    };

    

    // manualUnreconciliation(data: number[]): Observable<ResponseWrapper> {
    //     console.log('data service ' + JSON.stringify(data));
    //     return this.http.post(this.unreconciliationResultPostURL + '?tenantId=' + this.UserData.tenantId, data).map((res: Response) => {
    //         return res.json();;
    //     });
    // };

    /* AUTHOR: AMIT */
    //fetchRuleGroupIdsByTenantIdAndPurpose?tenantId=9&purpose=APPROVALS
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


    reconAmounts(data: reconAmounts): Observable<Response> {
        // data.rangeFrom = '2018-01-01 00:00:00';
        // data.rangeTo = '2018-02-28 00:00:00';
        //data.tenantId = this.UserData.tenantId;
        return this.http.post(this.fetchAmounts,data)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchRqCountAmount(analyticParameters: anaylticParams): Observable<Response> {
        // analyticParameters.rangeFrom = '2018-01-01 00:00:00';
        // analyticParameters.rangeTo = '2018-02-28 00:00:00';
        //analyticParameters.tenantId = this.UserData.tenantId;
        return this.http.post(this.fetchAnalyticsURL,analyticParameters)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchAllReconWqRecords(pageNumber: any, pageSize: any, reconQueryParams: reconDataQueries): Observable<Response> {
        // reconQueryParams.rangeFrom = '2018-01-01 00:00:00';
        // reconQueryParams.rangeTo = '2018-02-28 00:00:00';
       // reconQueryParams.tenantId = this.UserData.tenantId;
       // reconQueryParams.userId = this.UserData.id;
        // console.log('recon query aprams:' + JSON.stringify(reconQueryParams));
        // console.log('page number:' + pageNumber + 'page size:' + pageSize);
        return this.http.post(this.reconwqDataURL + '?pageNumber=' + pageNumber + '&pageSize='
            + pageSize, reconQueryParams)
            .map((res: Response) => {
                return res.json();
            });
    };
    fetchTRecordsCustomFilter(groupId: any, sViewId: any, tViewId: any,sortByTColumnId:any, sortBySColumnId:any, sortOrderBy:any, pageNumber: any, pageSize: any,rowIds: any): Observable<Response> {
        // console.log('rowIds:' + JSON.stringify(rowIds));
        return this.http.post(this.reconwqcustomFilter + '?groupId='
            + groupId + '&sViewId=' + sViewId + '&tViewId='
            + tViewId + '&sortByTColumnId=' + sortByTColumnId + '&sortBySColumnId=' + sortBySColumnId + '&sortOrderBy='
            + sortOrderBy + '&pageNumber=' + pageNumber + '&pageSize='
            + pageSize, rowIds)
            .map((res: Response) => {
                return res.json();
            });
    };
    fetchCountAmountonSelectSource(viewId: any, rowIds: any, reconRefIds:any): Observable<Response> {
        return this.http.post(this.selectedCountAmountURL + '?viewId=' + viewId + '&rowIds=', rowIds)
            .map((res: Response) => {
                return res.json();
            });
    };

    getColumnIdNameByViewId(viewId: any): Observable<Response> {
        return this.http.get(this.fetchColumnsBasedonViewId + '?viewId='
            + viewId)
            .map((res: Response) => {
                return res.json();
            });
    };


    manualRecon(manualRecDTOs: any) {
        {
            return this.http.post(this.manualRecURL , manualRecDTOs)
                .map((res: Response) => {
                    return res.json();
                });
        };
    }

    manualUNRecon(groupId: any, type: any, rangeFrom: any, rangeTo: any, groupBy: any, viewId: any, keyValues: any) {
        // rangeFrom = '2018-01-01 00:00:00';
        // rangeTo = '2018-02-28 00:00:00';
        // console.log('un reconcile object: ' + JSON.stringify(keyValues));
        return this.http.post(this.manualUnRecURL + '?groupId='
            + groupId + '&type=' + type + '&rangeFrom=' + rangeFrom + '&rangeTo=' + rangeTo + '&groupBy=' + groupBy + '&viewId=' + viewId, keyValues)
            .map((res: Response) => {
                return res.json();
            });
    }

    getFilesystem(sampleTreeJson) {
        return this.http.get(sampleTreeJson)
            .toPromise()
            .then(res => <TreeNode[]>res.json().data);
    }


    fetchRqAmountsByMultiCurr(groupId: any, rangeFrom: any, rangeTo: any, groupBy: any, sourceViewId: any, jobReference?: any): Observable<Response> {
        // rangeFrom = '2018-01-01 00:00:00';
        // rangeTo = '2018-02-28 00:00:00';
        return this.http.get(this.getAnalyticsMultiCurrURL + '?groupId='
            + groupId + '&rangeFrom=' + rangeFrom + '&rangeTo=' + rangeTo + '&groupBy=' + groupBy + '&sourceViewId=' + sourceViewId + '&jobReference=' + jobReference)
            .map((res: Response) => {
                return res.json();
            });
    };

    getQualifierColumns(viewId: any): Observable<Response> {
        return this.http.get(this.getQualifierColumnsURL + '?viewId='
            + viewId)
            .map((res: Response) => {
                return res.json();
            });
    };

    recDataExcelExport(params: any): Observable<ResponseWrapper> {
       // params.tenantId = this.UserData.tenantId;
        // console.log('file export acct query:' + JSON.stringify(params));
        return this.http.post(this.exportReconDataURL, params).map((res: Response) => {
            return res.json();
        });
    }

    postCustomFitlerConfig(sViewId:any,tViewId:any,formConfig: any): Observable<ResponseWrapper> {
        // console.log('form config input:' + JSON.stringify(formConfig));
        return this.http.post(this.postFormConfigURL + '?formConf=' + 'reconcilewq' + '&sViewId=' + sViewId + '&tViewId=' + tViewId + '&formLevel='
            + 'customfilter', formConfig).map((res: Response) => {
                return res.json();
            });
    }

    fetchFormConfigData(sViewId:any,tViewId:any): Observable<Response> {
        return this.http.get(this.fetcFormConfigURL + '?sViewId=' + sViewId + '&tViewId=' + tViewId)
            .map((res: Response) => {
                return res.json();
            });
    };
}
