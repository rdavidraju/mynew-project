import { Injectable } from '@angular/core';
import { Http, Response, ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DashboardService {

    private resourceUrl = 'agreeapplication/api/data-view-unions';
    private resourceSearchUrl = 'agreeapplication/api/_search/data-view-unions';
    private bucketlistsURL = "agreeapplication/api/bucket-lists";
    private getLovGroupByURL = "agreeapplication/api/getLovGroupBy";
    private getProcessedORUnProcesseGroupByColumnsInfoV4URL = "agreeapplication/api/getProcessedORUnProcesseGroupByColumnsInfoV4";
    private getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAgingURL = "agreeapplication/api/getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAging";
    private getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAgingURL = "agreeapplication/api/getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAging";
    private awaitingApprovalsInfoURL = "agreeapplication/api/awaitingApprovalsInfo";

    /* ETL */
    private extractionAnalysisforGivenPeriodV3Url = 'agreeapplication/api/extractionAnalysisforGivenPeriodV3';
    private transformationAnalysisforGivenPeriodV3Url = 'agreeapplication/api/transformationAnalysisforGivenPeriodV3';
    private getSummaryInfomartionForExtractionAndTransformationV3Url = 'agreeapplication/api/getSummaryInfomartionForExtractionAndTransformationV3';

    /* Reconciliation */
    private reconciliationAnalysisforGivenPeriodV3Url = 'agreeapplication/api/reconciliationAnalysisforGivenPeriodV3';
    private getSummaryInfoForReconciliationV3Url = 'agreeapplication/api/getSummaryInfoForReconciliationV3';
    private getUnProcessedOrProcessedDataForGroupByV3Url = 'agreeapplication/api/getUnProcessedOrProcessedDataForGroupByV3';
    private getViolationDetailsV3Url = 'agreeapplication/api/getViolationDetailsV3';
    private getSrcTargetCombinationViewsByRuleGrpUrl = 'agreeapplication/api/getSrcTargetCombinationViewsByRuleGrp';
    private agingAnalysisForUnReconciliationOrUnAccountedUrl = 'agreeapplication/api/agingAnalysisForUnReconciliationOrUnAccounted';
    private getSummaryInfoForReconciliationV8Url = 'agreeapplication/api/getSummaryInfoForReconciliationV8';

    /* Accounting */
    private AccountingAnalysisforGivenPeriodV3Url = 'agreeapplication/api/AccountingAnalysisforGivenPeriodV3';
    private getSummaryInfoForAccountingV3Url = 'agreeapplication/api/getSummaryInfoForAccountingV3';
    private detailInformationForJournalsUrl = "agreeapplication/api/detailInformationForJournals";
    private rulewiseSrcTargetCombinationReconDataUrl = "agreeapplication/api/rulewiseSrcTargetCombinationReconData"
    private getSummaryInfoForAccountingV8Url = 'agreeapplication/api/getSummaryInfoForAccountingV8';
    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    /* Dashboard V8 */
    private reconciliationRuleGroupSpecificInformationV4URL = 'agreeapplication/api/reconciliationRuleGroupSpecificInformationV4';
    private accountingRuleGroupSpecificInformationV4URL = 'agreeapplication/api/accountingRuleGroupSpecificInformationV4';

    //bucketlistsURL

    fetchBucketList(age?): Observable<Response> {
        if (age) {
            return this.http.get(this.bucketlistsURL + '?bucketType=' + age).map((res: Response) => {
                return res.json();
            });
        } else {

        }
    }

    /* Service to fetch Extraction analysis for given period */

    fetchExtractionForGivenPeriod(processId, type, obj): Observable<Response>  {
        return this.http.post(this.extractionAnalysisforGivenPeriodV3Url + '?processId=' + processId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* Service to fetch Transformation analysis for given period */

    fetchTransformationForGivenPeriod(processId, type, obj): Observable<Response>  {
        return this.http.post(this.transformationAnalysisforGivenPeriodV3Url + '?processId=' + processId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* Service to fetch Extraction & Transformation details analysis for given period */

    fetchETLDetailsAnalysisForGivenPeriod(processId, obj): Observable<Response>  {
        return this.http.post(this.getSummaryInfomartionForExtractionAndTransformationV3Url + '?processId=' + processId, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* Service to fetch reconciliation analysis for given period */

    fetchReconciliationForGivenPeriod(processId, type, obj): Observable<Response>  {
        return this.http.post(this.reconciliationAnalysisforGivenPeriodV3Url + '?processId=' + processId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* fetch dataview combination data set */

    datviewcombination(groupId, type, obj): Observable<Response>  {
        return this.http.post(this.getSrcTargetCombinationViewsByRuleGrpUrl + '?groupId=' + groupId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }


    /* Service to fetch reconciliation analysis view specific */

    fetchReconciliationViewSpecific(processId, violationPeriod, obj): Observable<Response>  {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        return this.http.post(this.getSummaryInfoForReconciliationV3Url + '?processId=' + processId + '&violation=' + violationPeriod, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    fetchReconSpecificbyRgId(ruleGroupId, violationPeriod, obj): Observable<Response>  {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        return this.http.post(this.getSummaryInfoForReconciliationV3Url + '?violation=' + violationPeriod + '&ruleGroupId=' + ruleGroupId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    /* Service to fetch reconciliation combination view analysis */
    ///getSrcTargetCombinationViewsByRuleGrp?groupId=168&type=Reconciled
    fetchReconciliationCombinationViewSpecific(groupId, type, obj): Observable<Response>  {
        return this.http.post(this.getSrcTargetCombinationViewsByRuleGrpUrl + '?groupId=' + groupId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    dVcombinationbyrule(groupId, type, srcViewId, tgViewId, obj): Observable<Response>  {
        return this.http.post(this.rulewiseSrcTargetCombinationReconDataUrl + '?groupId=' + groupId + '&type=' + type + '&srcViewId=' + srcViewId + '&tgtViewId=' + tgViewId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to fetch violation analysis */

    fetchViolationAnalysis(processId, violationPeriod, type, obj, ruleGroupId?): Observable<Response>  {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        if(ruleGroupId){
            return this.http.post(this.getViolationDetailsV3Url + '?violation=' + violationPeriod + '&type=' + type+'&ruleGroupId='+ruleGroupId, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }else{
            return this.http.post(this.getViolationDetailsV3Url + '?processId=' + processId + '&violation=' + violationPeriod + '&type=' + type, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }

    /* Service to fetch reconciliation analysis view specific groupBy*/

    fetchReconciliationViewSpecificgroupBy(processId, viewId, moduleType, amtQuailifier, groupByColmn, obj, ruleIdList, viewType): Observable<Response>  {
        return this.http.post(this.getUnProcessedOrProcessedDataForGroupByV3Url + '?processId=' + processId + '&viewId=' + viewId + '&module=' + moduleType + '&amtQuailifier=' + amtQuailifier + '&groupByColmn=' + groupByColmn +
            '&ruleIdList=' + ruleIdList + '&viewType=' + viewType, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
    }

    /* Service to fetch Accounting analysis for given period */

    fetchAccountingForGivenPeriod(processId, type, obj): Observable<Response>  {
        return this.http.post(this.AccountingAnalysisforGivenPeriodV3Url + '?processId=' + processId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* Service to fetch Accounting analysis view specific */

    fetchAccountingViewSpecific(processId, violationPeriod, obj): Observable<Response>  {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        return this.http.post(this.getSummaryInfoForAccountingV3Url + '?processId=' + processId + '&violation=' + violationPeriod, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
    fetchAccountingViewSpecificDetails(processId, type, obj): Observable<Response>  {
        return this.http.post(this.getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAgingURL + '?type=' + type + '&processId=' + processId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    fetchAccountingViewSpecificByView(violationPeriod, ruleGrpId, obj): Observable<Response>  {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        return this.http.post(this.getSummaryInfoForAccountingV3Url + '?violation=' + violationPeriod + '&ruleGroupId=' + ruleGrpId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to fetch je details */
    getdetailInfoForJE(processId, obj, status?,viewId?): Observable<Response>  {
        if (status) {
            return this.http.post(`${this.detailInformationForJournalsUrl}?processId=${processId}&status=${status}&viewId=${viewId}`, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        } else {
            return this.http.post(`${this.detailInformationForJournalsUrl}?processId=${processId}`, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }

    /*  Author: AMIT
        Service to fetch reconciliation RuleGroup Specific Information
    */
    reconAllDataList(obj): Observable<Response> {
        return this.http.post(this.reconciliationRuleGroupSpecificInformationV4URL, obj).map((res: Response) => {
            return res.json();
        });
    }

    /*  Author: AMIT
        Service to fetch accounting RuleGroup Specific Information
    */
    accountingAllDataList(obj): Observable<Response> {
        return this.http.post(this.accountingRuleGroupSpecificInformationV4URL, obj).map((res: Response) => {
            return res.json();
        });
    }

    /* Service to fetch reconciliation analysis for given period */

    fetchReconciliationForGivenPeriodBasedOnRuleGrpId(type, ruleGrpId, viewId, viewType, obj): Observable<Response>  {
        return this.http.post(this.reconciliationAnalysisforGivenPeriodV3Url + '?type=' + type + '&ruleGroupId=' + ruleGrpId + '&viewId=' + viewId+'&viewType='+viewType, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* Service to fetch accounting analysis for given period */

    fetchAccountingForGivenPeriodBasedOnRuleGrpId(type, ruleGrpId, viewId, obj): Observable<Response>  {
        return this.http.post(this.AccountingAnalysisforGivenPeriodV3Url + '?type=' + type + '&ruleGroupId=' + ruleGrpId + '&viewId=' + viewId, obj).map((res: Response) => {
            const jsonResponse = res;
            return jsonResponse;
        });
    }

    /* Service to get aging analysis */
    fetchAgingAnalysis(ruleGroupId, bucketId, type, obj, viewId, viewType?) {
        if(viewType){
            return this.http.post(this.agingAnalysisForUnReconciliationOrUnAccountedUrl + '?ruleGroupId=' + ruleGroupId + '&bucketId=' + bucketId + '&type=' + type + '&viewId=' + viewId+'&viewType='+viewType, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }else{
            return this.http.post(this.agingAnalysisForUnReconciliationOrUnAccountedUrl + '?ruleGroupId=' + ruleGroupId + '&bucketId=' + bucketId + '&type=' + type + '&viewId=' + viewId, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        }
    }


    /* Service to fetch columnList by view id */
    fetchColLovGrpBy(viewId): Observable<Response>  {
        return this.http.get(this.getLovGroupByURL + '?viewId=' + viewId).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to getUnReconciledOrUnAccountedGroupByInfoV4 */
    fetchUnReconciledOrUnAccountedGroupByInfo(ruleGroupId, viewId, viewType, rgType, groupByColumnNames, obj, violationPer?, age?, filterKey?, filterValues?,bucketId?): Observable<Response>  {
        if (violationPer && !filterKey) {
            return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
                viewId + '&viewType=' + viewType + '&rgType=' + rgType +  '&groupByColumnNames=' + groupByColumnNames + '&violation=' + violationPer, obj).map((res: Response) => {
                    const jsonResponse = res.json();
                    return jsonResponse;
                });
        } else if (age && !filterKey) {
            return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
                viewId + '&viewType=' + viewType + '&rgType=' + rgType +  '&groupByColumnNames=' + groupByColumnNames + '&age=' + age+'&bucketId='+bucketId, obj).map((res: Response) => {
                    const jsonResponse = res.json();
                    return jsonResponse;
                });
        } else if (filterKey && filterValues) {
            return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
                viewId + '&viewType=' + viewType + '&rgType=' + rgType +  '&groupByColumnNames=' + groupByColumnNames
                + '&filterKey=' + filterKey + '&filterValues=' + filterValues, obj).map((res: Response) => {
                    const jsonResponse = res.json();
                    return jsonResponse;
                });
        } else if (filterKey && !filterValues) {
            return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
                viewId + '&viewType=' + viewType + '&rgType=' + rgType +  '&groupByColumnNames=' + groupByColumnNames
                + '&filterKey=' + filterKey, obj).map((res: Response) => {
                    const jsonResponse = res.json();
                    return jsonResponse;
                });
        } else {
            return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
                viewId + '&viewType=' + viewType + '&rgType=' + rgType + '&groupByColumnNames=' + groupByColumnNames, obj).map((res: Response) => {
                    const jsonResponse = res.json();
                    return jsonResponse;
                });
        }
    }
    fetchUnReconciledOrUnAccountedGroupByInfoCurrency(ruleGroupId, viewId, viewType, rgType, groupByColumnNames, obj, violationPer?, age?): Observable<Response>  {
        return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
            viewId + '&viewType=' + viewType + '&rgType=' + rgType + '&groupByColumnNames=' + groupByColumnNames, obj).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
    }

    /* Service to src trg comb view for aging & violation */
    fetchSrcTrgCombViewAging(ruleGroupId, age, bucketId, obj): Observable<Response>  {
        return this.http.post(this.getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAgingURL + '?groupId=' + ruleGroupId + '&age=' + age + '&bucketId=' + bucketId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    fetchSrcTrgCombViewViolation(ruleGroupId, violation, obj): Observable<Response>  {
        return this.http.post(this.getSrcTargetCombinationViewsByRuleGrpOfUnReconForViolationAndAgingURL + '?groupId=' + ruleGroupId + '&violation=' + violation, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Fetch Accounting Views */
    fetchAccViewObj(groupId, type, obj): Observable<Response>  {
        return this.http.post(this.getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAgingURL + '?groupId=' + groupId + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Fetch Accounting Violation */
    fetchAccViewObjViolation(groupId, type, violation, obj): Observable<Response>  {
        return this.http.post(this.getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAgingURL + '?groupId=' + groupId + '&violation=' + violation + '&type=' + type, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Fetch Accounting Aging */
    fetchAccViewObjAging(groupId, type, age, bucketId, obj): Observable<Response>  {
        return this.http.post(this.getSrcViewsOfUnAccountedOrAccountedOrJeEnteredOrViolationAndAgingURL + '?groupId=' + groupId + '&age=' + age + '&type=' + type + '&bucketId=' + bucketId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Fetch Recon/Accounting Approvals */
    fetchApprovalsDetails(groupId, viewId, module1, obj): Observable<Response>  {
        return this.http.post(this.awaitingApprovalsInfoURL + '?ruleGroupId=' + groupId + '&viewId=' + viewId + '&module=' + module1, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    downloadReconGrpBy(ruleGroupId, viewId, viewType, rgType, groupByColumnNames, obj, fileType): Observable<Response>  {
        return this.http.post(this.getProcessedORUnProcesseGroupByColumnsInfoV4URL + '?ruleGroupId=' + ruleGroupId + '&viewId=' +
            viewId + '&viewType=' + viewType + '&rgType=' + rgType + '&groupByColumnNames=' + groupByColumnNames + '&fileExport=true&fileType=' + fileType, obj,{ responseType: ResponseContentType.Blob }).map((res: Response) => {
                const jsonResponse = res;
                return jsonResponse;
            });
    }

    fetchReconSpecificbyRgIdViewId(ruleGroupId, violationPeriod, viewId, viewType, obj) {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        return this.http.post(this.getSummaryInfoForReconciliationV8Url + '?violation=' + violationPeriod + '&ruleGroupId=' + ruleGroupId + '&viewId=' + viewId + '&viewType=' + viewType, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    fetchAccSpecificbyRgIdViewId(ruleGroupId, violationPeriod, viewId, obj): Observable<Response>  {
        if (violationPeriod == undefined) {
            violationPeriod = 2;
        }
        return this.http.post(this.getSummaryInfoForAccountingV8Url + '?violation=' + violationPeriod + '&ruleGroupId=' + ruleGroupId + '&dvId=' + viewId, obj).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
}
