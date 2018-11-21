import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { RuleGroup, reconcileRuleJSON } from './rule-group.model';
import { JhiDateUtils } from 'ng-jhipster';
import { SessionStorageService } from 'ng2-webstorage';
import { RuleGroupWithRulesAndConditions } from './ruleGroupWithRulesAndConditions.model';
import { RulesAndConditions } from '../rules/rulesWithConditions.model';
import { RuleGroupAndRuleWithLineItem, AccountingRuleGroup } from './ruleGroupAndRuleWithLineItem.model';
import { AccRule } from './accRule.model';
import { ApprovalRuleGRoupWithRules } from './approval-rule-groupWithRules.model';
import { json } from 'd3';

@Injectable()
export class RuleGroupService {

    private resourceUrl = 'agreeapplication/api/rule-groups';
    private resourceSearchUrl = 'agreeapplication/api/_search/rule-groups';
    private postRuleGroupUrl = 'agreeapplication/api/postingRuleGrpAndRulesAndRuleConditions';
    private getGroupDetailsUrl = 'agreeapplication/api/getRuleGrpAndRuleConditionsAndRuleGrpDetails';
    private getRuleGroupsBytenantUrl = 'agreeapplication/api/ruleGroupsByTenantId';
    private fetchGroupByDataUrl = 'agreeapplication/api/fetchFileTemplateByColumnNameAndTableName';
    private fetchRuleGroupsByPurpose = 'agreeapplication/api/fetchRuleGroupIdsByTenantIdAndPurpose';
    private fetchRuleGroupsByTenantWithMeaningUrl = 'agreeapplication/api/RuleGroupsWithMeaning';
    private fetchUnTaggedRuleGroupsUrl = 'agreeapplication/api/fetchUnTaggedRuleGroups';
    private postAppRuleConditionsAndAppRuleActionsUrl = 'agreeapplication/api/postAppRuleConditionsAndAppRuleActions';
    private getDVNameByRuleGroupIdUrl = 'agreeapplication/api/getDVNameByRuleGroupId';
    //Accounting API's
    private fetchingGroupsByPurposeAndViewIdURL = 'agreeapplication/api/getRuleGroupNameByDvIdAndPurpose';

    private postAccountingRulesUrl = 'agreeapplication/api/postAcctRuleConditionsAndAcctRuleDerivations';
    private getAccountingRulesUrl = 'agreeapplication/api/fetchAcctRuleConditionsAndAcctRuleDerivations';

    private getApprovalRulesUrl = 'agreeapplication/api/fetchAppRuleConditionsAndAppActions';
    private testAccRule = 'agreeapplication/api/postAccountingRules';

   // private UserData = this.$sessionStorage.retrieve('userData');

    private postAccRuleGRpUrl = 'agreeapplication/api/postAccountingRuleDefination';
    private getAccRuleGrpUrl = 'agreeapplication/api/getAccountingRuleDerivation';

    private getTenantConfigModulesByTenantIdUrl = 'agreeapplication/api/getTenantConfigModulesByTenantId'; //tenantId

    private filterViewsForAccountingRuleUrl = 'agreeapplication/api/dataViewsBasedOnTenantId';

    private fetchRuleGroupObjectUrl = 'agreeapplication/api/getRuleGroupDetailsObject';

    private updatePriorityUrl = 'agreeapplication/api/updatePriority';

    private getReconDataSourcesByGrpIdUrl ='agreeapplication/api/';
    private bulkUploadReconcileRulesUrl ='agreeapplication/api/bulkUploadReconcileRules';

    private ruleDuplicationCheckUrl  = 'agreeapplication/api/ruleDuplicationCheck';

    //variables for session
    public approvalRuleGRoupWithRules = new ApprovalRuleGRoupWithRules();
    public ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
    public ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
    public sourceDataView: any;
    public targetDataView: any;
    public sourceDataViewId: any;
    public targetDataViewId: any;
    public accountingRuleGroup = new AccountingRuleGroup();
    //ngxScrollToLastRule:string;
    public routerRuleGroupType: any;


    public ruleGroup = new RuleGroup();//common for all
    //public reconDataSourceArrays : any = [];
    public rulePriorityList : any =[];

    submitted  =false;
    showHeader =true;

    public reconcileRuleJSON : reconcileRuleJSON;
    /**
     * 
     * @param http
     * @param dateUtils
     * @param $sessionStorage
     */

    constructor(private http: Http, private dateUtils: JhiDateUtils, private $sessionStorage: SessionStorageService) {
        //console.log('instantiate accountingRuleGroup ' +JSON.stringify(this.accountingRuleGroup));
    }
    instantiateObjects() {
        this.approvalRuleGRoupWithRules = new ApprovalRuleGRoupWithRules();
        this.ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
        this.ruleGroupRulesAndConditions = new RuleGroupWithRulesAndConditions();
        this.accountingRuleGroup = new AccountingRuleGroup();
    }
    create(ruleGroup: RuleGroup): Observable<RuleGroup> {
        let copy: RuleGroup = Object.assign({}, ruleGroup);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(ruleGroup.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(ruleGroup.endDate);
        copy.creationDate = this.dateUtils.toDate(ruleGroup.creationDate);
        copy.lastUpdatedDate = this.dateUtils.toDate(ruleGroup.lastUpdatedDate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(ruleGroup: RuleGroup): Observable<RuleGroup> {
        let copy: RuleGroup = Object.assign({}, ruleGroup);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(ruleGroup.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(ruleGroup.endDate);

        copy.creationDate = this.dateUtils.toDate(ruleGroup.creationDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(ruleGroup.lastUpdatedDate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<RuleGroup> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.startDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.startDate);
            jsonResponse.endDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.endDate);
            jsonResponse.creationDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.creationDate);
            jsonResponse.lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        return this.http.get(this.resourceUrl )
            .map((res: any) => this.convertResponse(res))
            ;
    }
    getRuleGroupsbyTenantId(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.getRuleGroupsBytenantUrl , options)
            .map((res: Response) => this.convertResponse(res));
    }
    ruleDuplicationCheck(ruleName,rulePurpose): Observable<Response> {
        return this.http.get(this.ruleDuplicationCheckUrl+'?ruleName='+ruleName+'&rulePurpose='+rulePurpose)
            .map((res: Response) => this.convertResponse(res));
    }
    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    searchKeys(page: number, pageSize: number, filterValue: any, columnValue: any): Observable<Response> {
        let columnName: any;
        if (filterValue === undefined)
            filterValue = null;
        if (columnValue.toLowerCase() === 'all') {
            columnValue = null;
            columnName = null;
        }
        else {
            columnName = 'rule_purpose';
        }

        // console.log('page'+page+'pageSize'+pageSize+'columnValue'+columnValue+'filterValue'+filterValue);//page=${page}&per_page=${pageSize}&
        return this.http.get(`${this.resourceSearchUrl}/?columnName=${columnName}&columnValue=${columnValue}&filterValue=${filterValue}`)
            .map((res: any) => this.convertResponse(res))
            ;
    }

    search(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res))
            ;
    }

    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        //console.log('rulegroups lengrth is' +JSON.stringify( jsonResponse));
        for (let i = 0; i < jsonResponse.length; i++) {
            /* jsonResponse[i].startDate = this.dateUtils
                 .convertLocalDateFromServer(jsonResponse[i].startDate);
             jsonResponse[i].endDate = this.dateUtils
                 .convertLocalDateFromServer(jsonResponse[i].endDate);*/
            jsonResponse[i].creationDate = this.dateUtils
                .toDate(jsonResponse[i].creationDate);
            jsonResponse[i].lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].lastUpdatedDate);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
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

    /* getGroupDetails(groupId: any): Observable<any> {`${this.fileTemplateListOfDistinctValues}/${tableName}/${columnName}?tenantId=${UserData.tenantId}`
         return this.http.get(this.getGroupDetailsUrl,+'?tenantId='+UserData.tenantId+'&groupId='+groupId).map((res: Response) => {
             return res.json();
         });}*/
    getGroupDetails(groupId: any): Observable<any> {
        return this.http.get(this.getGroupDetailsUrl + '?groupId=' + groupId).map((res: Response) => {

            let jsonResponse: any;
            jsonResponse = res.json();
            //        console.log('details'+ JSON.stringify(jsonResponse));
           // jsonResponse.startDate = this.dateUtils
              //  .convertDateTimeFromServer(jsonResponse.startDate);
         //   jsonResponse.endDate = this.dateUtils
              //  .convertDateTimeFromServer(jsonResponse.endDate);

            //jsonResponse.creationDate = this.dateUtils
           //     .convertDateTimeFromServer(jsonResponse.creationDate);
          //  jsonResponse.lastUpdatedDate = this.dateUtils
           //     .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            let rules: any
            rules = jsonResponse.rules;
            rules.forEach((rule) => {
                rule.rule.startDate = this.dateUtils.convertDateTimeFromServer(rule.rule.startDate);
                rule.rule.endDate = this.dateUtils.convertDateTimeFromServer(rule.rule.endDate);
                rule.rule.creationDate = this.dateUtils.convertDateTimeFromServer(rule.rule.creationDate);
                rule.rule.lastUpdatedDate = this.dateUtils.convertDateTimeFromServer(rule.rule.lastUpdatedDate);
            });
            return jsonResponse;
        });
    }
    fetchRuleGroupObject(groupId: any): Observable<any> {
        return this.http.get(this.fetchRuleGroupObjectUrl + '?groupId=' + groupId).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            jsonResponse.startDate = this.dateUtils
            .convertDateTimeFromServer(jsonResponse.startDate);
        jsonResponse.endDate = this.dateUtils
            .convertDateTimeFromServer(jsonResponse.endDate);

        jsonResponse.creationDate = this.dateUtils
            .convertDateTimeFromServer(jsonResponse.creationDate);
        jsonResponse.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
          //  console.log('jsonResponse for rule object'+JSON.stringify(jsonResponse));
            return jsonResponse;
        });

        }
    
    fetchSideBarData(columnName: any): Observable<Response> {
        let tableName = 'RuleGroup';
        return this.http.get(`${this.fetchGroupByDataUrl}/${tableName}/${columnName}`).map((res: Response) => {
            //console.log('response in grp by sidebar service' + JSON.stringify(res.json()));
            return res.json();
        });
    }
    /*postRuleGroupForAccountingRules(ruleGroupDTO : RuleGroupAndRuleWithLineItem): Observable<any> {
        ruleGroupDTO['tenantId'] = this.UserData.tenantId;
        ruleGroupDTO['createdBy'] = this.UserData.id;
        ruleGroupDTO['lastUpdatedBy'] = this.UserData.id;
        //console.log('accounting data is:'+JSON.stringify(ruleGroupDTO));
        return this.http.post(this.postAccountingRulesUrl,ruleGroupDTO).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        }).share();
    }*/


    getAccountingRuleGroupDetails(groupId: any): Observable<any> {
        return this.http.get(this.getAccountingRulesUrl + '?groupId=' + groupId).map((res: Response) => {

            let jsonResponse: RuleGroupAndRuleWithLineItem = new RuleGroupAndRuleWithLineItem();
            jsonResponse = res.json();
            // jsonResponse.startDate = this.dateUtils
            //     .convertDateTimeFromServer(jsonResponse.startDate);
            // jsonResponse.endDate = this.dateUtils
            //     .convertDateTimeFromServer(jsonResponse.endDate);

            // jsonResponse.creationDate = this.dateUtils
            //     .convertDateTimeFromServer(jsonResponse.creationDate);
            // jsonResponse.lastUpdatedDate = this.dateUtils
            //     .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);
            let rules: AccRule[];
            rules = jsonResponse.rules;
            if (rules){
                rules.forEach((rule) => {
                    rule.startDate = this.dateUtils.convertDateTimeFromServer(rule.startDate);
                    rule.endDate = this.dateUtils.convertDateTimeFromServer(rule.endDate);
                    rule.creationDate = this.dateUtils.convertDateTimeFromServer(rule.creationDate);
                    rule.lastUpdatedDate = this.dateUtils.convertDateTimeFromServer(rule.lastUpdatedDate);
                });
            }
                
            return jsonResponse;
        });
    }

    fetchRuleGroupsBypurpose(purpose: any): Observable<Response> {
        return this.http.get(this.fetchRuleGroupsByPurpose +  '?purpose=' + purpose).map((res: Response) => {
            //console.log('response in grp by sidebar service' + JSON.stringify(res.json()));
            return res.json();
        });
    }
    fetchUnTaggedRuleGroups(rulePurpose: any): Observable<Response> {
        return this.http.get(`${this.fetchUnTaggedRuleGroupsUrl}/?rulePurpose=${rulePurpose}`).
            map((res: Response) => {
                return res;
            });
    }
    fetchRuleGroupsWithMeaningByTenant(page: number, pageSize: number, rulePurpose: any,sortColumn ?: any, sortOrder ?: any): Observable<Response> {
        //  console.log('paramters passed are:' +page+'=>'+pageSize+'=>'+rulePurpose);
        return this.http.get(`${this.fetchRuleGroupsByTenantWithMeaningUrl}/?page=${page}&per_page=${pageSize}&rulePurpose=${rulePurpose}&sortColName=${sortColumn}&sortOrder=${sortOrder}`).
            map((res: Response) => {
                return res;
            });
    }


    fetchApprovalRules(groupId): Observable<Response> {
        return this.http.get(this.getApprovalRulesUrl + '?groupId=' + groupId).map((res: Response) => {
            let jsonResponse = res.json();
            //console.log('approval=>' + JSON.stringify(jsonResponse));
            let rules: AccRule[];
            rules = jsonResponse.rules;
            if (rules){            
                rules.forEach((rule) => {
                    rule.startDate = this.dateUtils.convertDateTimeFromServer(rule.startDate);
                    rule.endDate = this.dateUtils.convertDateTimeFromServer(rule.endDate);
                    rule.creationDate = this.dateUtils.convertDateTimeFromServer(rule.creationDate);
                    rule.lastUpdatedDate = this.dateUtils.convertDateTimeFromServer(rule.lastUpdatedDate);
                });
            }
            return jsonResponse;
        });
    }
    //check all dependencies
    testPost(): Observable<Response> {
        this.ruleGrpWithRuleAndLineItems.id = this.ruleGroup.id;
        this.ruleGrpWithRuleAndLineItems.name = this.ruleGroup.name;
        this.ruleGrpWithRuleAndLineItems.rulePurpose = this.ruleGroup.rulePurpose;
        this.ruleGrpWithRuleAndLineItems.startDate = this.ruleGroup.startDate;
        this.ruleGrpWithRuleAndLineItems.endDate = this.ruleGroup.endDate;

       // this.ruleGrpWithRuleAndLineItems.tenantId = this.UserData.tenantId;
       // this.ruleGrpWithRuleAndLineItems.createdBy = this.UserData.id;
       // this.ruleGrpWithRuleAndLineItems.lastUpdatedBy = this.UserData.id;

        return this.http.post(this.testAccRule, this.ruleGrpWithRuleAndLineItems).map((res: Response) => {
            return res.json();
        });
    }
    postAdhocruleForAccounting(ruleGrpWithRuleAndLineItems): Observable<any> {

       // ruleGrpWithRuleAndLineItems.tenantId = this.UserData.tenantId;
       // ruleGrpWithRuleAndLineItems.createdBy = this.UserData.id;
       // ruleGrpWithRuleAndLineItems.lastUpdatedBy = this.UserData.id;
        //console.log('acc rules before rule build to save'+JSON.stringify(ruleGrpWithRuleAndLineItems));
        return this.http.post(this.testAccRule, ruleGrpWithRuleAndLineItems).map((res: Response) => {
            return res.json();
        });
    }
    postAdhocruleForReconcile(ruleGroupRulesAndConditions): Observable<any> {

        //ruleGroupRulesAndConditions.tenantId = this.UserData.tenantId;
       // ruleGroupRulesAndConditions.createdBy = this.UserData.id;
        ruleGroupRulesAndConditions.creationDate = new Date();
       // ruleGroupRulesAndConditions.lastUpdatedBy = this.UserData.id;
        //console.log('reconcile rules before rule build to save'+JSON.stringify(ruleGroupRulesAndConditions));
        return this.http.post(this.postRuleGroupUrl, ruleGroupRulesAndConditions).map((res: Response) => {
            return res.json();
        });
    }
 

    bulkUploadReconcileRules(reconcileRules,file: File,): Observable<Response> {

        let formData = new FormData();
        formData.append('file', file);
        formData.append('reconProcesses',JSON.stringify(reconcileRules));
        return this.http.post(this.bulkUploadReconcileRulesUrl,formData).map((res: Response) => {
            return res.json();
        });
    }


    postRuleGroup(): Observable<any> {

        this.ruleGroupRulesAndConditions.id = this.ruleGroup.id;
        this.ruleGroupRulesAndConditions.name = this.ruleGroup.name;
        this.ruleGroupRulesAndConditions.rulePurpose = this.ruleGroup.rulePurpose;
        this.ruleGroupRulesAndConditions.startDate = this.ruleGroup.startDate;
        this.ruleGroupRulesAndConditions.endDate = this.ruleGroup.endDate;
        this.ruleGroupRulesAndConditions.enabledFlag = this.ruleGroup.enableFlag;
        this.ruleGroupRulesAndConditions.enableFlag = this.ruleGroup.enableFlag;
      //  this.ruleGroupRulesAndConditions.tenantId = this.UserData.tenantId;
       // if (!this.ruleGroupRulesAndConditions.id || this.ruleGroupRulesAndConditions.id <= 0)
           // this.ruleGroupRulesAndConditions.createdBy = this.UserData.id;
       // this.ruleGroupRulesAndConditions.lastUpdatedBy = this.UserData.id;
         //console.log('reconcile rules to save'+JSON.stringify(this.ruleGroupRulesAndConditions));
        return this.http.post(this.postRuleGroupUrl, this.ruleGroupRulesAndConditions).map((res: Response) => {
            return res.json();
        });
    }
    postApprovalRules(): Observable<Response> {

        this.approvalRuleGRoupWithRules.id = this.ruleGroup.id;
        this.approvalRuleGRoupWithRules.name = this.ruleGroup.name;
        this.approvalRuleGRoupWithRules.startDate = this.ruleGroup.startDate;
        this.approvalRuleGRoupWithRules.endDate = this.ruleGroup.endDate;
        this.approvalRuleGRoupWithRules.rulePurpose = this.ruleGroup.rulePurpose;
      //  this.approvalRuleGRoupWithRules.tenantId = this.UserData.tenantId;
        this.approvalRuleGRoupWithRules.enableFlag =this.ruleGroup.enableFlag;
       // if (!this.approvalRuleGRoupWithRules.id || this.approvalRuleGRoupWithRules.id <= 0)
          //  this.approvalRuleGRoupWithRules.createdBy = this.UserData.id;
      //  this.approvalRuleGRoupWithRules.lastUpdatedBy = this.UserData.id;
        this.approvalRuleGRoupWithRules.apprRuleGrpId = this.ruleGroup.apprRuleGrpId;

        console.log('printing here in service the app' + JSON.stringify(this.approvalRuleGRoupWithRules));
        return this.http.post(this.postAppRuleConditionsAndAppRuleActionsUrl, this.approvalRuleGRoupWithRules).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }
    postAccountingRuleGroup(adhoc:boolean): Observable<Response> {
        this.accountingRuleGroup.id = this.ruleGroup.id;
        this.accountingRuleGroup.name = this.ruleGroup.name;
        this.accountingRuleGroup.rulePurpose = this.ruleGroup.rulePurpose;
        this.accountingRuleGroup.startDate = this.ruleGroup.startDate;
        this.accountingRuleGroup.endDate = this.ruleGroup.endDate;
        this.accountingRuleGroup.accountingTypeCode = this.ruleGroup.accountingTypeCode;
        this.accountingRuleGroup.reconciliationGroupId = this.ruleGroup.reconciliationGroupId;
        //this.accountingRuleGroup.tenantId = this.UserData.tenantId;
        this.accountingRuleGroup.apprRuleGrpId = this.ruleGroup.apprRuleGrpId;
        this.accountingRuleGroup.activityBased = this.ruleGroup.activityBased;
        this.accountingRuleGroup.multiCurrency = this.ruleGroup.multiCurrency;
        this.accountingRuleGroup.crossCurrency = this.ruleGroup.crossCurrency;
        this.accountingRuleGroup.fxRateId = this.ruleGroup.fxRateId;
        this.accountingRuleGroup.conversionDate = this.ruleGroup.conversionDate;
        this.accountingRuleGroup.controlAccount = this.ruleGroup.controlAccount;
        this.accountingRuleGroup.realizedGainLossAccount = this.ruleGroup.realizedGainLossAccount;
        this.accountingRuleGroup.fxGainAccount = this.ruleGroup.fxGainAccount;
        this.accountingRuleGroup.fxLossAccount = this.ruleGroup.fxLossAccount;
        this.accountingRuleGroup.enableFlag = this.ruleGroup.enableFlag;
        this.accountingRuleGroup.adhocRuleCreation=adhoc;
       // if (!this.accountingRuleGroup.id)
          //  this.accountingRuleGroup.createdBy = this.UserData.id;
      //  this.accountingRuleGroup.lastUpdatedBy = this.UserData.id;
        console.log('posting object in acc rule grp ' + JSON.stringify(this.accountingRuleGroup));
        return this.http.post(this.postAccRuleGRpUrl, this.accountingRuleGroup).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });

    }
    getAccRuleGroupDetails(groupId: any): Observable<any> {
        // console.log('in service to fetch acc detail');
       // let grpId: any = 0;
        //grpId = +groupId;
        return this.http.get(this.getAccRuleGrpUrl + '?groupId=' + groupId).map((res: Response) => {
            let jsonResponse: AccountingRuleGroup = new AccountingRuleGroup();
            jsonResponse = res.json();
            jsonResponse.startDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.startDate);
            jsonResponse.endDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.endDate);
            jsonResponse.creationDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.creationDate);
            jsonResponse.lastUpdatedDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.lastUpdatedDate);

            if (jsonResponse.rules) {
                jsonResponse.rules.forEach((rule) => {
                    rule.startDate = this.dateUtils.convertDateTimeFromServer(rule.startDate);
                    rule.endDate = this.dateUtils.convertDateTimeFromServer(rule.endDate);
                    rule.creationDate = this.dateUtils.convertDateTimeFromServer(rule.creationDate);
                    rule.lastUpdatedDate = this.dateUtils.convertDateTimeFromServer(rule.lastUpdatedDate);
                });
            }
            //console.log('jsonResponse of get api'+jsonResponse)
            return jsonResponse;
        });
    }
    getTenantConfigModules(): Observable<any> {
        return this.http.get(this.getTenantConfigModulesByTenantIdUrl ).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }

    //Author: BHAGATH
    //Service: Fetching Rule Grups by Purpose and DVID

    fetchingGroupsByViewId(dvId: any, purpose: any): Observable<any> {
        return this.http.get(this.fetchingGroupsByPurposeAndViewIdURL + '?dvId=' + dvId + '&purpose=' + purpose).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Author: Amit
       Fetching dataviews list based on rule group id
    */
    fetchingDataViewList(ruGrpId): Observable<any> {
        return this.http.get(this.getDVNameByRuleGroupIdUrl + '?ruleGroupId=' + ruGrpId ).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }
    // getDVNameByRuleGroupId?ruleGroupId=38&tenantId=9

    fetchRuleGroupsBasedOnRulePurpose(rulePurpose: any,status?): Observable<Response> {
        if(status){
            return this.http.get(`${this.fetchRuleGroupsByTenantWithMeaningUrl}?page=${1}&per_page=${0}&rulePurpose=${rulePurpose}&isActive=${status}`).
            map((res: Response) => {
                return res;
            });
        }else{
            return this.http.get(`${this.fetchRuleGroupsByTenantWithMeaningUrl}?page=${1}&per_page=${0}&rulePurpose=${rulePurpose}`).
            map((res: Response) => {
                return res;
            });
        }
    }
    filterViewsByReconRuleGroupAndAccountingDataSource(ruleGrpId: any, accDataSourceId:any,grpId?:any): Observable<Response> {
        console.log('inputs for recon ds fetch are=>'+ruleGrpId+'=>accDataSourceId'+accDataSourceId+"grpId=>"+grpId)
        
        let url ='';
        let jsonResponse: any;
        url = this.filterViewsForAccountingRuleUrl;
        if(ruleGrpId == undefined) {
            return jsonResponse;
        } else{
            url = url+ '?ruleGroupId=' + ruleGrpId ;
            if(grpId==undefined){
            grpId=null;
            }  else{
            url=url+'&accRuleGroupId='+grpId;
            }
            if(accDataSourceId == undefined || accDataSourceId == null || !accDataSourceId) {

            }else{
                            url = url+ '&viewId=' + accDataSourceId;
            }
            return this.http.get(url).
                map((res: Response) => {
                   
                jsonResponse = res.json();
                    //console.log('jsonResponse for viewsssss'+JSON.stringify(jsonResponse));
                    if(jsonResponse['All']) {
                        jsonResponse =jsonResponse['All'];
                    }  else if (jsonResponse[accDataSourceId]) {
                        jsonResponse= jsonResponse[accDataSourceId];
                    }
                    return jsonResponse;
                });
        }
       // return null;
       

    }
    updateRulesPriority(ruleListWithPriority): Observable<Response> {
        console.log('ruleListWithPriority'+JSON.stringify(ruleListWithPriority));
        return this.http.put(this.updatePriorityUrl, ruleListWithPriority).map((res: Response) => {
            return res.json();
        });
    }
    getReconDataSources(grpId:any): Observable<Response> {
        return this.http.get(`${this.getReconDataSourcesByGrpIdUrl}?`).
            map((res: Response) => {
                return res;
            });
    }
   
   toggleHeader(){
      
    this.showHeader = !this.showHeader;

}
    public ruleFinalJson ={
        "Reconciliation process name" : "name",
        "Rule name":"ruleCode",
        "Rule type":"ruleType",
        "Suggestion rule(T/F)":"suggestionFlag",
        "Reconciliation for":"sDataViewDisplayName",
        "Reconciliation with":"tDataViewDisplayName",
        "Source column":"sColumnName",
        "Source operator":"sOperator",
        "Source value":"sValue",
        "Source formula":"sFormula",
        "Source tolerance from":"sToleranceValueFrom",
        "Source tolerance to":"sToleranceValueTo",
       // "Source percentage":"",
        "Operator":"operator",
        "Target column":"tColumnName",
        "Target operator":"tOperator",
        "Target value":"tValue",
        "Target formula":"tFormula",
        "Target tolerance from":"tToleranceValueFrom",
        "Target tolerance to":"tToleranceValueTo",
        //"Target percentage":"",
        "Logical Operator":"logicalOperator",

    } 
}
