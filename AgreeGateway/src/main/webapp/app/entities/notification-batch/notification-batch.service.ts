import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { NotificationBatch } from './notification-batch.model';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import 'rxjs/add/operator/toPromise';
@Injectable()
export class NotificationBatchService {

    //private UserData = this.$sessionStorage.retrieve('userData');
    private resourceUrl = 'agreeapplication/api/notification-batches';
    private resourceSearchUrl = 'agreeapplication/api/_search/notification-batches';
    private listOfNotificationBatchUrl = 'agreeapplication/api/getNotificationBatchList';
    private fetchSelectedBatchDetailsUrl = 'agreeapplication/api/getApprovalHistory';
    private getSelectedBatchDetailsUrl = 'agreeapplication/api/getDistinctRefNumForBatchId';
    private approveBatchTaskUrl = 'agreeapplication/api/approveTask';
    private rejectBatchTaskUrl = 'agreeapplication/api/rejectTask';
    private reassignBatchTaskUrl = 'agreeapplication/api/reassignTask';
    private approvalsHistoryUrl = 'agreeapplication/api/getApprovalsHistoryDetails';
    private getApprovalHistorySrcTargetTypedQueryUrl = 'agreeapplication/api/getApprovalHistorySrcTargetTypedQuery';
    private getUsersByTenantIdUrl = '/api/getUsersByTenantId';
    private getUsersBasedOnRoleNameUrl = '/api/getUsersBasedOnRoleName';
    private getHierarchySiderBarUrl = 'agreeapplication/api/hierarchyListForAParent';
    

    constructor(private http: Http, private dateUtils: JhiDateUtils, private $sessionStorage: SessionStorageService) { }

    create(notificationBatch: NotificationBatch): Observable<NotificationBatch> {
        const copy = this.convert(notificationBatch);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(notificationBatch: NotificationBatch): Observable<NotificationBatch> {
        const copy = this.convert(notificationBatch);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<NotificationBatch> {
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
        entity.createdDate = this.dateUtils
            .convertDateTimeFromServer(entity.createdDate);
        entity.lastUpdatedDate = this.dateUtils
            .convertDateTimeFromServer(entity.lastUpdatedDate);
    }

    private convert(notificationBatch: NotificationBatch): NotificationBatch {
        const copy: NotificationBatch = Object.assign({}, notificationBatch);

        copy.createdDate = this.dateUtils.toDate(notificationBatch.createdDate);

        copy.lastUpdatedDate = this.dateUtils.toDate(notificationBatch.lastUpdatedDate);
        return copy;
    }

    //getNotificationBatchListBasedOnHierarchyType?tenantId=9&userId=240&page=1&per_page=10&status=IN_PROCESS&hierarchyType=hierarchy&module=RECON_APPROVALS
    /* service to fetch list of notification batch */
    fetchListOfNotificationBatch(page: number, per_page: number, moduleType?: any, status?: any,userId?: any): Observable<Response> {
        console.log('page ' + page + ' per_page ' + per_page + ' moduleType ' + moduleType + ' status ' + status + ' userId ' + userId);
        if (status && userId) {
            console.log('inside su');
            return this.http.get(`${this.listOfNotificationBatchUrl}?page=${page}&per_page=${per_page}&status=${status}&hierarchyType=hierarchy&module=${moduleType}`).map((res: Response) => {
                return res;
            });
        }else if (status && !userId) {
            console.log('inside s!u');
            return this.http.get(`${this.listOfNotificationBatchUrl}?page=${page}&per_page=${per_page}&status=${status}&hierarchyType=hierarchy&module=${moduleType}`).map((res: Response) => {
                return res;
            });
        }else if (!status && userId) {
            console.log('inside su');
            return this.http.get(`${this.listOfNotificationBatchUrl}?page=${page}&per_page=${per_page}&hierarchyType=hierarchy&module=${moduleType}`).map((res: Response) => {
                return res;
            });
        } else {
            console.log('inside nothing');
            return this.http.get(`${this.listOfNotificationBatchUrl}?page=${page}&per_page=${per_page}&hierarchyType=hierarchy&module=${moduleType}`).map((res: Response) => {
                return res;
            });
        }

    }

    /* service to fetch selected batch details */
    fetchSelectedBatchDetails(id: any): Observable<Response> {
        return this.http.get(`${this.fetchSelectedBatchDetailsUrl}?notificationId=${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }
    //getDistinctRefNumForBatchId?notificationId=254&pageNumber=1&pageSize=5&status=INPROCESS
    getSelectedBatchDetails(id: any, page: number, per_page: number, status?: any): Observable<Response> {
        console.log('id ' + id + ' page ' + page + ' perpage ' + per_page);
        if (status) {
            return this.http.get(`${this.getSelectedBatchDetailsUrl}?notificationId=${id}&status=${status}&pageNumber=${page}&pageSize=${per_page}`).map((res: Response) => {
                return res;
            });
        } else {
            return this.http.get(`${this.getSelectedBatchDetailsUrl}?notificationId=${id}&pageNumber=${page}&pageSize=${per_page}`).map((res: Response) => {
                return res;
            });
        }
    }
    //getApprovalHistorySrcTargetTypedQuery?notificationId=134&reconRef=10000
    getSelectedBatchSrcTrgDetails(id: any, batchId: any): Observable<Response> {
        return this.http.get(`${this.getApprovalHistorySrcTargetTypedQueryUrl}?notificationId=${batchId}&reconRef=${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    /* Service to approve selected batch */
    approveBatchTask(ids: any, type: any, batchId?: any): Observable<Response> {
        let obj = [];
        console.log('ids ::' + JSON.stringify(ids));
        console.log('type ::' + type);
        //console.log('userId ::' + this.UserData.id);
        console.log('batchId ::' + batchId);
       // console.log('tenantId ::' + this.UserData.tenantId);//?userId=354&tenantId=954&type=batch
        if (batchId) {
            console.log('insidebid');
            return this.http.post(`${this.approveBatchTaskUrl}?batchId=${batchId}&type=${type}`, ids).map((res: Response) => {
                const jsonResponse = res.json();
                this.convertItemFromServer(jsonResponse);
                return jsonResponse;
            });
        } else {
            console.log('out');
            return this.http.post(`${this.approveBatchTaskUrl}?type=${type}`, ids).map((res: Response) => {
                const jsonResponse = res.json();
                this.convertItemFromServer(jsonResponse);
                return jsonResponse;
            });
        }
    }

    /* Service to reject selected batch */
    /*  rejectBatchTask(taskId, batchId): Observable<Response> {
         let obj = [];
         console.log('taskid ::' + taskId);
         console.log('batchId ::' + batchId);
         console.log('userId ::' + this.UserData.id);
         console.log('tenantId ::' + this.UserData.tenantId);
         return this.http.post(`${this.rejectBatchTaskUrl}?taskId=${taskId}&batchId=${batchId}&userId=${this.UserData.id}&tenantId=${this.UserData.tenantId}`, obj).map((res: Response) => {
             const jsonResponse = res.json();
             this.convertItemFromServer(jsonResponse);
             return jsonResponse;
         });
     } */
    rejectBatchTask(ids: any, type: any, batchId?: any): Observable<Response> {
        let obj = [];
        console.log('ids ::' + JSON.stringify(ids));
        console.log('type ::' + type);
        //console.log('userId ::' + this.UserData.id);
        console.log('batchId ::' + batchId);
       // console.log('tenantId ::' + this.UserData.tenantId);//?userId=354&tenantId=954&type=batch
        if (batchId) {
            console.log('insidebid');
            return this.http.post(`${this.rejectBatchTaskUrl}?batchId=${batchId}&type=${type}`, ids).map((res: Response) => {
                const jsonResponse = res.json();
                this.convertItemFromServer(jsonResponse);
                return jsonResponse;
            });
        } else {
            console.log('out');
            return this.http.post(`${this.rejectBatchTaskUrl}?type=${type}`, ids).map((res: Response) => {
                const jsonResponse = res.json();
                this.convertItemFromServer(jsonResponse);
                return jsonResponse;
            });
        }
    }

    /* Service to reassing batch */
    reassignBatchTask(userid:any, ids: any, type: any) {
        console.log('ids ' + JSON.stringify(ids));
         return this.http.post(`${this.reassignBatchTaskUrl}?type=${type}&assignTo=${userid}`, ids).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        }); 
    }

    /* Service to fetch approvals history */
    getApprovalsHistory(id: any): Observable<Response> {
        return this.http.get(`${this.approvalsHistoryUrl}?notificationId=${id}`).map((res: Response) => {
            // const jsonResponse = res.json();
            // this.convertItemFromServer(jsonResponse);
            return res.json();
        });
    }

    usersListTask() {
        
        return this.http.get(`${this.getUsersByTenantIdUrl}`).map((res: Response) => {
                return res.json();
            });
    }

    usersListBasedOnRoleTask(role:any) {
        
        return this.http.get(`${this.getUsersBasedOnRoleNameUrl}?roleName=${role}`).map((res: Response) => {
                return res.json();
            });
    }

    /* service to fetch hierchy sidebar */

    fetchHierarchySideBar() {
        //hierarchyListForAParent?userId=240&tenantId=9&module=ALL
        return this.http.get(`${this.getHierarchySiderBarUrl}?module=ALL`).map((res: Response) => {
            return res.json().data;
        });
    }
    
//getUsersByTenantId?tenantId=9
    /*   getCarsMedium(): Observable<Response> {
         return this.http.get(`${this.getcarsList}`).map((res: Response) => {
             const jsonResponse = res.json();
             this.convertItemFromServer(jsonResponse);
             return jsonResponse;
         });
     }  */

    /*  getCarsMedium() {
     return this.http.get<any>('./cars-medium.json')
       .toPromise()
       .then(res => <Car[]>res.data)
       .then(data => { return data; });
     } */
}
