import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import 'rxjs/add/operator/toPromise';
import { AccountingData, acctCustomFilters, jsonforAccounting,AcctDataQueries,awqAllParams, Car,filteredParams, recordParams, acctKeyValues, selectedAcctRows } from './accounting-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import { SelectItem, MultiSelectModule, DataGridModule, ChipsModule, DialogModule, InputMaskModule, TreeTableModule, TreeNode, SharedModule } from 'primeng/primeng';
@Injectable()
export class AccountingDataService {
    private acctQueryParamsURL = 'agreeapplication/api/getAccHeaderParamsList';
    private acctURPRecordsURL = 'agreeapplication/api/getFilterGroupByAccountingData';
    private acctRecordsPost = 'agreeapplication/api/postManualAccData';
    private acctRecordsDel = 'agreeapplication/api/manualUnAccData';
    private acctExportToExcel = 'agreeapplication/api/exportAccDataToExcelFile';
    private groupByURL = 'agreeapplication/api/getAccountingGroupByAnalytics';
    private filteredGroupByURL = 'agreeapplication/api/getAccountingFilterGroupBySummary';
    private distinctStatusSummary = 'agreeapplication/api/getAWQStatusesCountsNAmounts';
    private awqGroupingSummary = 'agreeapplication/api/getAWQGroupingSummaryInfo';
    private awqDetailedInfo = 'agreeapplication/api/getAccountingDetailInfo';
    public acctSchJob:boolean;
    private awqManualUnAcct = 'agreeapplication/api/manualUnAccDataAutoAcc';
    private accountedColsURL = 'agreeapplication/api/getColumnAllignForAccountedSummary';
    private accountedSummaryURL = 'agreeapplication/api/getAccountedSummaryInfo'








   // private resourceUrl = 'agreeapplication/api/accounting-data';
   // private resourceSearchUrl = 'agreeapplication/api/_search/accounting-data';
    //private fetchAmountAndCountUrl = 'agreeapplication/api/getAccountingCountAndAmount';
    private fetchRecordsByViewIds = 'agreeapplication/api/getAccountingDataByViewId';
    private acctFilterRecordsURL = 'agreeapplication/api/getAccDataWithCustomFilter';
    private postingAccoutningRecordsURL = 'agreeapplication/api/postManualAccountingData';
    private postingUnAccoutningRecordsURL = 'agreeapplication/api/processManualUnAccountingData';
    private accountingSideBarURL = 'agreeapplication/api/getAccountingViewColumnValueSet';
    private accColHeadersURL = 'agreeapplication/api/getAccountingColumnAlignmentInfo';
    private treeNodeData: any[] = [];
    //private UserData = this.$sessionStorage.retrieve('userData');

    constructor(private http: Http, private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService) { }

    // create(accountingData: AccountingData): Observable<AccountingData> {
    //     const copy = this.convert(accountingData);
    //     return this.http.post(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
            
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }
    
    // update(accountingData: AccountingData): Observable<AccountingData> {
    //     const copy = this.convert(accountingData);
    //     return this.http.put(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

    // find(id: number): Observable<AccountingData> {
    //     return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

    // query(req?: any): Observable<ResponseWrapper> {
    //     const options = createRequestOption(req);
    //     return this.http.get(this.resourceUrl, options)
    //         .map((res: Response) => this.convertResponse(res));
    // }

    // delete(id: number): Observable<Response> {
    //     return this.http.delete(`${this.resourceUrl}/${id}`);
    // }

    // search(req?: any): Observable<ResponseWrapper> {
    //     const options = createRequestOption(req);
    //     return this.http.get(this.resourceSearchUrl, options)
    //         .map((res: any) => this.convertResponse(res));
    // }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(accountingData: AccountingData): AccountingData {
        const copy: AccountingData = Object.assign({}, accountingData);

        copy.createdDate = this.dateUtils.toDate(accountingData.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(accountingData.lastUpdatedDate);
        return copy;
    }

    fetchRecordsByViewId(dataViewId: any, groupId: any, page: number, perPage: number): Observable<Response> {

        return this.http.get(this.fetchRecordsByViewIds + '?dataViewId='
            + dataViewId + '&groupId=' + groupId + '&pageNumber=' + page + '&pageSize=' + perPage)
            .map((res: Response) => {
                return res.json();
            });

    };

    fetchRecordsByViewIdAndStatus(dataViewId: any, groupId: any, status: any, page: number, perPage: number): Observable<Response> {

        return this.http.get(this.fetchRecordsByViewIds + '?dataViewId='
            + dataViewId + '&status=' + status + '&groupId=' + groupId + '&pageNumber=' + page + '&pageSize=' + perPage)
            .map((res: Response) => {
                return res.json();
            });

    };

    fetchingAccSidebarColumns(dataViewId: any, groupId: any): Observable<Response> {

        return this.http.get(this.accountingSideBarURL + '?viewId='
            + dataViewId + '&groupId=' + groupId)
            .map((res: Response) => {
                return res.json();
            });

    };

    fetchingAccSidebarColumnsByStatus(dataViewId: any, groupId: any, status: any): Observable<Response> {

        return this.http.get(this.accountingSideBarURL + '?viewId='
            + dataViewId + '&groupId=' + groupId + '&status=' + status)
            .map((res: Response) => {
                return res.json();
            });

    };

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

    fetchAcctRecordsUsingCustomFilter(data: any): Observable<ResponseWrapper> {
       // data.tenantId = this.UserData.tenantId;
        let copy: acctCustomFilters = Object.assign({}, data);
        return this.http.post(this.acctFilterRecordsURL, copy).map((res: Response) => {
            return res.json();
        });
    };

    postingAccountRecords(data: any): Observable<ResponseWrapper> {
        return this.http.post(this.postingAccoutningRecordsURL , data).map((res: Response) => {
            return res.json();
        });
    }

    postingUnAccountRecords(data: any, dataViewId, groupId): Observable<ResponseWrapper> {
        return this.http.post(this.postingUnAccoutningRecordsURL + '?dataViewId=' + dataViewId + '&groupId=' + groupId, data).map((res: Response) => {
            return res.json();
        });
    }


    fetchAcctQueryParams(groupId: any): Observable<Response> {

        return this.http.get(this.acctQueryParamsURL + '?groupId=' + groupId)
            .map((res: Response) => {
                return res.json();
            });

    };
        
    fetchFilteredGroupByCount(groupId: any, rangeFrom: any, rangeTo: any, groupBy: any, sourceViewId: any, params: filteredParams,filterGroupBy?:any): Observable<Response> {
        return this.http.post(this.filteredGroupByURL + '?groupId='
                + groupId + '&rangeFrom=' + rangeFrom + '&rangeTo=' + rangeTo + '&groupBy=' + groupBy + '&sourceViewId=' + sourceViewId + '&filterGroupBy=' + filterGroupBy, params)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchFilteredGroupByCountByColId(groupId: any, rangeFrom: any, rangeTo: any, groupBy: any, sourceViewId: any, columnId: any, params: filteredParams, filterGroupBy?: any): Observable<Response> {
        return this.http.post(this.filteredGroupByURL + '?groupId='
                + groupId + '&rangeFrom=' + rangeFrom + '&rangeTo=' + rangeTo + '&groupBy=' + groupBy + '&sourceViewId=' + sourceViewId + '&columnId=' + columnId + '&filterGroupBy=' + filterGroupBy, params)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchGroupByCount(groupId: any, rangeFrom: any, rangeTo: any, groupBy: any, sourceViewId: any): Observable<Response> {
        return this.http.get(this.groupByURL + '?groupId='
            + groupId + '&rangeFrom=' + rangeFrom + '&rangeTo=' + rangeTo + '&groupBy=' + groupBy + '&sourceViewId=' + sourceViewId)
            .map((res: Response) => {
                return res.json();
            });
    };

    fetchGroupByCountByColId(groupId: any, rangeFrom: any, rangeTo: any, groupBy: any, sourceViewId: any, columnId: any): Observable<Response> {
        return this.http.get(this.groupByURL + '?groupId='
            + groupId + '&rangeFrom=' + rangeFrom + '&rangeTo=' + rangeTo + '&groupBy=' + groupBy + '&sourceViewId=' + sourceViewId + '&columnId=' + columnId)
            .map((res: Response) => {
                return res.json();
            });
    };



    fetchingAllAcctGroupData(pageNumber: any, pageSize: any, params: recordParams, filterGroupBy?: any): Observable<Response> {
        //params.tenantId = this.UserData.tenantId;
        // console.log('recon query aprams:' + JSON.stringify(params));
        // console.log('page number:' + pageNumber + 'page size:' + pageSize);
        if (filterGroupBy) {
            // console.log('filtered Group By:' + filterGroupBy);
            return this.http.post(this.acctURPRecordsURL + '?pageNumber=' + pageNumber + '&pageSize='
                + pageSize + '&filterGroupBy=' + filterGroupBy, params)
                .map((res: Response) => {
                    return res.json();
                });
        } else {
            // console.log('without filtered Group By:' + filterGroupBy);
            return this.http.post(this.acctURPRecordsURL + '?pageNumber=' + pageNumber + '&pageSize='
                + pageSize, params)
                .map((res: Response) => {
                    return res.json();
                });
        }
    };

    manualAcct(manualAccDTO: jsonforAccounting): Observable<ResponseWrapper> {
        // console.log('manual acct query:' + JSON.stringify(manualAccDTO));
        return this.http.post(this.acctRecordsPost , manualAccDTO).map((res: Response) => {
            return res.json();
        });
    }


    acctDataExcelExport(filterGroupBy: any, params: recordParams): Observable<ResponseWrapper> {
       // params.tenantId = this.UserData.tenantId;
        // console.log('file export acct query:' + JSON.stringify(params));
        return this.http.post(this.acctExportToExcel + '?filterGroupBy=' + filterGroupBy, params).map((res: Response) => {
            return res.json();
        });
    }


    manualUnAcct(type: any, params: recordParams, filterGroupBy?: any): Observable<ResponseWrapper> {
       // params.tenantId = this.UserData.tenantId;
        // console.log('manual acct query:' + JSON.stringify(params));
        if (filterGroupBy != undefined) {
            return this.http.post(this.acctRecordsDel + '?type=' + type + '&filterGroupBy=' + filterGroupBy, params).map((res: Response) => {
                return res.json();
            });
        } else {
            return this.http.post(this.acctRecordsDel + '?type=' + type, params).map((res: Response) => {
                return res.json();
            });
        }
    }

    //* AWQ New Services *//

    distinctStatuses( params: awqAllParams): Observable<ResponseWrapper> {
       // params.tenantId = this.UserData.tenantId;
        // console.log('distinct status query:' + JSON.stringify(params));
            return this.http.post(this.distinctStatusSummary, params).map((res: Response) => {
                return res.json();
            });
    }

    groupingSummarySidebar( params: awqAllParams): Observable<ResponseWrapper> {
       // params.tenantId = this.UserData.tenantId;
        // console.log('distinct grouping query:' + JSON.stringify(params));
            return this.http.post(this.awqGroupingSummary, params).map((res: Response) => {
                return res.json();
            });
    }

    detailedInfoRecords( params: awqAllParams): Observable<ResponseWrapper> {
     //   params.tenantId = this.UserData.tenantId;
        // console.log('distinct detail query:' + JSON.stringify(params));
            return this.http.post(this.awqDetailedInfo, params).map((res: Response) => {
                return res.json();
            });
    }

    accountedSummaryInfo( params: awqAllParams): Observable<ResponseWrapper> {
        // console.log('distinct detail query:' + JSON.stringify(params));
            return this.http.post(this.accountedSummaryURL, params).map((res: Response) => {
                return res.json();
            });
    }

    manualUnAcctNew( params: awqAllParams): Observable<ResponseWrapper> {
       // params.tenantId = this.UserData.tenantId;
       // params.userId = this.UserData.id;
        // console.log('Distinct Query for Manual Unac' + JSON.stringify(params));
        return this.http.post(this.awqManualUnAcct, params).map((res: Response) => {
            return res.json();
        });
    }
    
}