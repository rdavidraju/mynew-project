import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { TemplateDetails } from './template-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class TemplateDetailsService {

    private resourceUrl = 'agreeapplication/api/template-details';
    private resourceSearchUrl = 'agreeapplication/api/_search/template-details';
    private lookUpUrl = 'agreeapplication/api/lookup-codes';
    private mappingSetsUrl = 'agreeapplication/api/fetchMappingSetAndValuesByPurpose';
    private UserData = this.$sessionStorage.retrieve('userData');
    private getJournalsListUrl = 'agreeapplication/api/getJournalsTemplateDetails';
    private postingTemplateDetailsUrl = 'agreeapplication/api/postingTemplateDetails';
    private getJournalsDetailsUrl = 'agreeapplication/api/getTemplateDetails';
    private getDataViewsColumnsInfoUrl = 'agreeapplication/api/getDataViewsColumnsInfo';
    private getLedgersListUrl = 'agreeapplication/api/getLedgerDataByTenantId';
    private sideBarAPIForJournalHeaderUrl = 'agreeapplication/api/sideBarAPIForJournalHeader';
    private getAllcalenderListUrl = 'agreeapplication/api/getAllcalenderList';
    private postTemplateDefinitionUrl = 'agreeapplication/api/postingTemplateDetailsAndDerivation';
    private getTemplateDetailsAndDerivationUrl = 'agreeapplication/api/getTemplateDetailsAndDerivation';
    private gettemplateNameIsExistsUrl = 'agreeapplication/api/templateNameIsExists';
    private getConversionTypeUrl = 'agreeapplication/api/getActiveCalenderType';
    private getAllFxRatesUrl = 'agreeapplication/api/getAllFxRates';
    private ruleGroupsByTenantIdUrl = 'agreeapplication/api/ruleGroupsByTenantId';
    private getDVNameByRuleGroupIdUrl = 'agreeapplication/api/getDVNameByRuleGroupId';
    private getCoaDetailsByGrpIdUrl = 'agreeapplication/api/getCoaDetailsByGrpId';
    private getCOAandSegmentsOrderBySequenceUrl = 'agreeapplication/api/getCOAandSegmentsOrderBySequence';

    constructor(private http: Http, private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService) { }

    create(templateDetails: TemplateDetails): Observable<TemplateDetails> {
        const copy = this.convert(templateDetails);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(templateDetails: TemplateDetails): Observable<TemplateDetails> {
        const copy = this.convert(templateDetails);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<TemplateDetails> {
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
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(entity.endDate);
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(templateDetails: TemplateDetails): TemplateDetails {
        const copy: TemplateDetails = Object.assign({}, templateDetails);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(templateDetails.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(templateDetails.endDate);

        copy.createdDate = this.dateUtils.toDate(templateDetails.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(templateDetails.lastUpdatedDate);
        return copy;
    }

    getLookUps(code: any): Observable<TemplateDetails> {
        return this.http.get(`${this.lookUpUrl}/${code}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getConversionType(): Observable<TemplateDetails> {
        return this.http.get(`${this.getConversionTypeUrl}`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Below Service to fetch mapping set list */
    getmappingSets(): Observable<TemplateDetails> {
        return this.http.get(`${this.mappingSetsUrl}?purpose=JOURNAL_TEMPLATES`).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Service to get Journals List */
    journalsList(page?: number, per_page?: number): Observable<Response> {
        if (page && per_page) {
            return this.http.get(`${this.getJournalsListUrl}?page=${page}&per_page=${per_page}`).map((res: Response) => {
                let jsonResponse = res
                // console.log('journals List from service :' + JSON.stringify(jsonResponse));
                return jsonResponse;
            });
        } else {
            return this.http.get(`${this.getJournalsListUrl}`).map((res: Response) => {
                let jsonResponse = res
                // console.log('journals List from service :' + JSON.stringify(jsonResponse));
                return jsonResponse;
            });
        }
    }
    /* Service to post journals details*/
    postJournalsHeaderData(journalsHeaderData: TemplateDetails): Observable<TemplateDetails> {
        return this.http.post(`${this.postingTemplateDetailsUrl}`, journalsHeaderData).map((res: Response) => {
            return res.json();
        });
    }
    /* Service to get journal details by id */
    getJournalsById(id: number): Observable<TemplateDetails> {
        return this.http.get(`${this.getJournalsDetailsUrl}?templateId=${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.startDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.startDate);
            jsonResponse.endDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.endDate);
            //console.log('fetch : ' + JSON.stringify(jsonResponse));
            return jsonResponse;
        });
    }
    /* Service to get columns based on ids */
    getViewsColumnsData(obj): Observable<Response> {
        return this.http.post(`${this.getDataViewsColumnsInfoUrl}`, obj).map((res: Response) => {
            return res.json();
        });
    }

    getLedgersList(): Observable<Response> {
        return this.http.get(`${this.getLedgersListUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    getCalenderList(): Observable<Response> {
        return this.http.get(`${this.getAllcalenderListUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    /* Service to post journals definition*//* postingTemplateDetailsAndDerivation?tenantId=34&userId=342 */
    postTemplateDefinition(obj): Observable<Response> {
        console.log('journal Template while posting in service ::' + JSON.stringify(obj));
        return this.http.post(`${this.postTemplateDefinitionUrl}`, obj).map((res: Response) => {
            return res.json();
        });
    }

    //getTemplateDetailsAndDerivation?id=9
    /* Service to get journal definition details by id */
    getJournalDefinitionDetailsById(id: number): Observable<TemplateDetails> {
        return this.http.get(`${this.getTemplateDetailsAndDerivationUrl}?id=${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.startDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.startDate);
            jsonResponse.endDate = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.endDate);
            //console.log('fetch : ' + JSON.stringify(jsonResponse));
            return jsonResponse;
        });
    }

    //sideBarAPIForJournalHeader?tableName=TemplateDetails&columnName=templateName&tenantId=9
    //sideBarAPIForJournalHeaderUrl
    getSideBarJournalTemplateList(): Observable<Response> {
        return this.http.get(`${this.sideBarAPIForJournalHeaderUrl}?tableName=TemplateDetails&columnName=templateName`).map((res: Response) => {
            return res.json();
        });
    }


    //templateNameIsExists?tenantId=10&name=
    getCheckJournalName(name: any): Observable<Response> {
        return this.http.get(`${this.gettemplateNameIsExistsUrl}?name=${name}`).map((res: Response) => {
            return res.json();
        });
    }

    /* Service to get all fx rates list */
    //getAllFxRates?tenantId=11
    getFxRatesList(): Observable<Response> {
        return this.http.get(`${this.getAllFxRatesUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    /* Service to fetch rule group based on rule purpose */
    getAccountingRulesGrpList(rulePurpose: any): Observable<any> {
        return this.http.get(this.ruleGroupsByTenantIdUrl + '?rulePurpose=' + rulePurpose).map((res: Response) => {
            let jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Author: Amit
       Fetching dataviews list based on rule group id
    */
    fetchingDataViewList(ruGrpId): Observable<any> {
        return this.http.get(this.getDVNameByRuleGroupIdUrl + '?ruleGroupId=' + ruGrpId).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Author: Amit
       Fetching COA list based on rule group id
    */
    fetchingCOAList(ruGrpId): Observable<any> {
        return this.http.get(this.getCoaDetailsByGrpIdUrl + '?ruleGrpId=' + ruGrpId).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Author: Amit
       Fetching COA list based on rule group id
    */
   getCOAandSegments(id,qualifier): Observable<any> {
        return this.http.get(this.getCOAandSegmentsOrderBySequenceUrl + '?id=' + id + '&qualifier=' + qualifier).map((res: Response) => {
            let jsonResponse: any;
            jsonResponse = res.json();
            return jsonResponse;
        });
    }
    
}
