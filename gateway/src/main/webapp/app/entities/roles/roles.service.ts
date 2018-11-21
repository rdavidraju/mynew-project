import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { Roles } from './roles.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RolesService {

    private getAllRoles = 'api/roles?size=2000';
    private resourceUrl = 'api/roles';
    private rolesAndFunctionalitiesByUser = 'api/rolesAndFunctionsByUserId';
    private rolesByTenant = 'api/rolesForTenant';
    private functaggedroles = 'api/role-function-assignments';
    private getRoleFuncNUserRoleByRoleIdUrl = 'api/getRoleFunctionsNUserRoleAssignmentsByRoleId?roleId=';
    private postRoleFuncNUserUrl = 'api/postRoleFunctionsUsersAssignment';
    private updateRoleUrl = 'api/updateRoleBasedonId';
    private updateRoleFuncUrl = 'api/updateRoleFunctionAssignmentById';
    private updateRoleUserUrl = 'api/updateUserRoleAssignmentById';
    private deleteRoleFuncUrl = 'api/deleteRoleFunctionByFunctionIdnRoleId?functionId=';
    private deleteRoleUserUrl = 'api/deleteUserRoleByUserIdnRoleId?userId=';
    private postTagFuncToRoleUrl = 'api/postRoleFunctionAssignment?roleId=';
    private postTagUsersToRoleUrl = '/api/postUserRoleAssignment?roleId=';
    private checkRoleIsExistUrl:any = 'api/checkRoleIsExist';

    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
    ) { }

    create(roles: Roles): Observable<Roles> {
        return this.http.post(this.resourceUrl, roles).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(roles: Roles): Observable<Roles> {
        return this.http.put(this.resourceUrl, roles).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Roles> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Roles> {
        const options = createRequestOption(req);
        return this.http.get(this.getAllRoles)
        .map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        })
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
        entity.startDate = this.dateUtils
            .convertLocalDateFromServer(entity.startDate);
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(entity.endDate);
    }

    private convert(roles: Roles): Roles {
        const copy: Roles = Object.assign({}, roles);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(roles.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(roles.endDate);
        return copy;
    }

    fetchRolesAndFunctionsByUserId(userId: number): Observable<Roles> {
        return this.http.get(this.rolesAndFunctionalitiesByUser + '?userId=' + userId).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    fetchAllRolesByTenantId(): Observable<any> {
        return this.http.get(this.rolesByTenant).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    fetchAllFuncByRoleId():Observable<any>{
        return this.http.get(this.functaggedroles).map((res: Response) => {
             const jsonResponse = res.json();
            return jsonResponse;
        });
    }


    /* function to fetch all roles */
    getAllRoless(req?: any): Observable<Roles> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl)
            .map((res: Response) => {
                const jsonResponse = res.json();
                this.convertItemFromServer(jsonResponse);
                return jsonResponse;
            })
    }

    /** Fetch Roles, tagged Functionalities and Users y Role Id*/
    getRoleFuncNUserRoleByRoleId(roleId){
        return this.http.get(this.getRoleFuncNUserRoleByRoleIdUrl + roleId).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        })
    }

    /**Post Roles With Optional Func and Users */
    postRoleFuncNUser(roles){
        return this.http.post(this.postRoleFuncNUserUrl, roles).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        })
    }

    /**update Roles */
    updateRole(roles){
        return this.http.post(this.updateRoleUrl, roles).map(() => {});
    }

    /**Update Role's Function */
    updateRoleFunc(roleFunc){
        return this.http.post(this.updateRoleFuncUrl, roleFunc).map(()=>{});
    }

    /**Update Role's User */
    updateRoleUser(roleUser){
        return this.http.post(this.updateRoleUserUrl, roleUser).map(()=>{});
    }

    /**Delete Role's Function */
    deleteRoleFunc(funcId, roleId){
        return this.http.delete(this.deleteRoleFuncUrl + funcId + '&roleId=' + roleId).map(()=>{});
    }

    /**Delete Role's User */
    deleteRoleUser(userId, roleId){
        return this.http.delete(this.deleteRoleUserUrl + userId + '&roleId=' + roleId).map(()=>{});
    }

    /**Post(Tag) Functionalities to Role*/
    postTagFuncToRole(functionalities, roleId){
        return this.http.post(this.postTagFuncToRoleUrl + roleId, functionalities).map(()=>{})
    }

    /**Post(Tag) Functionalities to Role*/
    postTagUsersToRole(users, roleId){
        return this.http.post(this.postTagUsersToRoleUrl + roleId, users).map(()=>{})
    }

    /**Get All Roles List with Pagination */
    getListRolesByTenantIdWithPagination(page, size): Observable<any>{
        return this.http.get(this.resourceUrl+'?page='+page+'&per_page='+size).map((res: Response)=>{
            return res;
        });
    }

    /**
     * @author: Sameer
     * @description: Check if name exists
     * @param name
     * @param id?
     */
    checkRoleIsExist(name, type, id?) {
        return this.http.get(`${this.checkRoleIsExistUrl}?name=${name}${id ? '&id='+id : ''}&type=${type}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

}