import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { User, UserService } from '../../shared';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SessionStorageService } from 'ng2-webstorage';

import { Functionality } from './functionality.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FunctionalityService {

    //private UserData = this.$sessionStorage.retrieve('userData');
    private resourceUrl = 'api/functionalities';
    private roletaggedFunc = '/api/roles'
    private getFuncByTenantIdUrl = 'api/getFunctionalatiesByTenantId';
    private getFuncDetByIdWithRolesUrl = 'api/getFunctionalitiesAndRespectiveRoles?functId=';
    private postFuncDetByIdWithRolesUrl = 'api/postFunctionalitiesAndRespectiveRoles';
    private roleFunctionAssignments = 'api/role-function-assignments/';

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService
    ) { }

    create(functionality: Functionality): Observable<Functionality> {
        // const copy = this.convert(functionality);
        //functionality.tenantId = this.UserData.tenantId;
        return this.http.post(this.resourceUrl, functionality).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(functionality: Functionality): Observable<Functionality> {
        // const copy = this.convert(functionality);
        return this.http.put(this.resourceUrl, functionality).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Functionality> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Functionality> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl)
        .map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        })


            // .map((res: Response) => this.convertResponse(res));
    }

    // query(req?: any): Observable<ResponseWrapper> {
    //     const options = createRequestOption(req);
    //     return this.http.get(this.resourceUrl, options)
    //         .map((res: Response) => this.convertResponse(res));
    // }

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
        entity.funcStartDate = this.dateUtils
            .convertLocalDateFromServer(entity.funcStartDate);
        entity.funcEndDate = this.dateUtils
            .convertLocalDateFromServer(entity.funcEndDate);
    }

    private convert(functionality: Functionality): Functionality {
        const copy: Functionality = Object.assign({}, functionality);
        copy.funcStartDate = this.dateUtils
            .convertLocalDateToServer(functionality.funcStartDate);
        copy.funcEndDate = this.dateUtils
            .convertLocalDateToServer(functionality.funcEndDate);
        return copy;
    }

    fetchAllRoleByFuncId():Observable<any>{
        return this.http.get(this.roletaggedFunc).map((res: Response) => {
             const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    getFuncByTenantId(){
        return this.http.get(this.getFuncByTenantIdUrl).map((res: Response)=>{
            const jsonResponse =res.json();
            return jsonResponse;
        })
    }

    /**Get all list of Functionalities by TenantId with pagination */
    getListFuncByTenantIdWithPagination(page, size): Observable<any>{
        return this.http.get(this.resourceUrl+'?page='+page+'&per_page='+size).map((res: Response)=>{
            return res;
        });
    }

    /**Get Functionality detail by Id with respective roles */
    getFuncDetByIdWithRoles(funcId){
        return this.http.get(this.getFuncDetByIdWithRolesUrl+funcId).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Post Functionality detail with respective lsit of roles */
    postFuncDetWithRoles(funcWithRoles){
        return this.http.post(this.postFuncDetByIdWithRolesUrl, funcWithRoles).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /**Delete Role Function Assignment Tagged Role */
    deleteRoleAssignmentById(roleFuncId){
        return this.http.delete(this.roleFunctionAssignments + roleFuncId).map((res: Response)=>{    
        });
    }

    /**Update Role assignement by assignment id */
    updateRoleAssignment(funcRole){
        return this.http.put(this.roleFunctionAssignments, funcRole).map((res: Response)=>{
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
    checkFunctionalityIsExistUrl:any = 'api/checkFunctionalityIsExist';
    checkFunctionalityIsExist(name, id?) {
        return this.http.get(`${this.checkFunctionalityIsExistUrl}?name=${name}${id ? '&id='+id : ''}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }
}
