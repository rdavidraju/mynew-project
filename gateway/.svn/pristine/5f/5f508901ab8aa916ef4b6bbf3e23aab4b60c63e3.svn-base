import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { ApprovalGroups } from './approval-groups.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ApprovalGroupsService {
    
    private resourceUrl = 'agreeapplication/api/approval-groups';
    private resourceSearchUrl = 'agreeapplication/api/_search/approval-groups';
    private approvalGroupsByTenant = 'agreeapplication/api/approvalGroupsForTenant';
    private getGroupMembersById = 'agreeapplication/api/getApprovalGroupMembers';
    private getApprovalGroupsAndGrpMembersUrl = 'agreeapplication/api/getApprovalGroupsAndGrpMembers?id=';
    private postApprovalGroupsAndGrpMembersUrl = 'agreeapplication/api/postApprovalGroupsAndGrpMembers'
    private checkApprovalGroupIsExistUrl = 'agreeapplication/api/checkApprovalGroupIsExist';

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils
    ) { }

    create(approvalGroups: ApprovalGroups): Observable<ApprovalGroups> {
        const copy = this.convert(approvalGroups);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    /* update(approvalGroups: ApprovalGroups): Observable<ApprovalGroups> {
        const copy = this.convert(approvalGroups);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    } */

    update(approvalGroups){
        return this.http.put(this.resourceUrl, approvalGroups).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    find(id: number): Observable<ApprovalGroups> {
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
    approvalgroupsForTenant(req?: any): Observable<any> {
        return this.http.get(`${this.approvalGroupsByTenant}`).map((res: Response) => {
            return res.json();
        }); 
    }
    getGroupMembers(groupId: any):Observable<any> {
        return this.http.get(this.getGroupMembersById+'?groupId='+groupId).map((res: Response) => {
            return res.json();
        }); 
    }
    private convertResponse(res: Response): ResponseWrapper {   
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.startDate =
        this.dateUtils.convertLocalDateFromServer(entity.startDate);

        entity.endDate =
        this.dateUtils.convertLocalDateFromServer(entity.endDate);

        entity.createdDate =
        this.dateUtils.convertDateTimeFromServer(entity.createdDate);

        entity.lastUpdatedDate =
        this.dateUtils.convertDateTimeFromServer(entity.lastUpdatedDate);

            /**Converting Approval Groups Members Date */
            for(let i=0; i<entity.approvalsGroupMemList.length; i++){
                entity.approvalsGroupMemList[i].startDate =
                this.dateUtils.convertLocalDateFromServer(entity.approvalsGroupMemList[i].startDate);

                entity.approvalsGroupMemList[i].endDate =
                this.dateUtils.convertLocalDateFromServer(entity.approvalsGroupMemList[i].endDate);
            }
    }

    private convert(approvalGroups: ApprovalGroups): ApprovalGroups {
        const copy: ApprovalGroups = Object.assign({}, approvalGroups);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(approvalGroups.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(approvalGroups.endDate);

        copy.createdDate = this.dateUtils.toDate(approvalGroups.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(approvalGroups.lastUpdatedDate);
        return copy;
    }

    /**New API's Start Here */

    /**Get all Approval Groups by tenantid with pagination */
    getAllApprovalGrps(page, size){
        return this.http.get(this.resourceUrl+'?page='+page+'&size=' + size ).map((res: Response)=>{
            return res;
        });
    }

    /**Get Approval groups and its members details by id */
    getApprovalGroupsAndGrpMembers(id){
        return this.http.get(this.getApprovalGroupsAndGrpMembersUrl+id).map((res: Response)=>{
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    /**Post Approval Group and Its Members */
    postApprovalGroupsAndGrpMembers(apprGrpNMembers){
        return this.http.post(this.postApprovalGroupsAndGrpMembersUrl, apprGrpNMembers).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkApprovalGroupIsExist(name, id?) {
        return this.http.get(`${this.checkApprovalGroupIsExistUrl}?name=${name}${id ? '&id='+id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
}
