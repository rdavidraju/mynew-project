import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { User } from './user.model';
import { ResponseWrapper } from '../model/response-wrapper.model';
import { createRequestOption } from '../model/request-util';
import { SessionStorageService } from 'ng2-webstorage';
import { JhiDateUtils } from 'ng-jhipster';
import { DatePipe } from '@angular/common';

@Injectable()
export class UserService {
    private UserData = this.$sessionStorage.retrieve('userData');
    private resourceUrl = 'api/users';
    private usersByTenantIdUrl = 'api/usersListWithRolesAndFunctions';
    private getUsersByTenantIdUrl = 'api/getUsersByTenantId';
    private getUserByIdUrl = 'api/userById';
    private userCreationUrl = 'api/userAndUserRoleAssignmentCreation';
    private getListOfRolesUrl = 'api/rolesAndFunctionsByUserId';
    private getLoggedInUserRolesDetailsUrl = 'api/getLoggedInUserRolesDetails';
    private getUserRolesDetailsByUserIdUrl = 'api/getUserRolesDetailsByUserId';
    private assignRolesToUserUrl = 'api/assignRolesToUser';
    private uploadImageUrl = 'api/postUserImage';
    private updateUserRoleUrl = 'api/user-role-assignments';
    private emailIdValidationUrl = 'api/validatingUserEmail';
    private getUsersBasedOnRoleIdUrl = 'api/getUsersBasedOnRoleName';
    private getManagerialChainUrl = 'agreeapplication/api/getManagerialHierarchyList';
    
    
    constructor(
        private http: Http,
        private dateUtils: JhiDateUtils,
        private datePipe: DatePipe,
        private $sessionStorage: SessionStorageService
    ) { }

    create(user: User): Observable<ResponseWrapper> {
        user.tenantId = this.UserData.tenantId;
        console.log('in service ' + JSON.stringify(user));
        return this.http.post(this.resourceUrl, user)
            .map((res: Response) => {
                return res.json();
            });
    }

    update(user: User): Observable<ResponseWrapper> {
        return this.http.put(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    find(login: string): Observable<User> {
        return this.http.get(`${this.resourceUrl}/${login}`).map((res: Response) => res.json());
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(login: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${login}`);
    }

    authorities(): Observable<string[]> {
        return this.http.get('api/users/authorities').map((res: Response) => {
            const json = res.json();
            return <string[]>json;
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }
    fetchUsersByTenant(): Observable<any> {
        return this.http.get(this.usersByTenantIdUrl).map((res: Response) => {
            return res.json();
        }

        );
    }

    /* service to fetch user details by user id */
    userById(id: any): Observable<any> {
        console.log('in service ' + id);
        return this.http.get(`${this.getUserByIdUrl}?id=${id}`).map((res: Response) => {
            return res.json();
        });
    }

    /* Service function to fetch getLoggedInUserRolesDetails */
    getUserRolesDetails(): Observable<any> {
        return this.http.get(`${this.getLoggedInUserRolesDetailsUrl}`).map((res: Response) => {
            return res.json();
        });
    }

    /* Service function to fetch getLoggedInUserRolesDetails */
    getSelectedUserRolesDetails(id: any): Observable<any> {
        return this.http.get(`${this.getUserRolesDetailsByUserIdUrl}?userId=${id}`).map((res: Response) => {
            let jsonResponse = res.json();
         //   console.log('res ' + JSON.stringify(res.json()));

            if (jsonResponse && jsonResponse.createdDate) {
                jsonResponse.createdDate = this.datePipe.transform(jsonResponse.createdDate, 'yyyy-MM-dd');
                jsonResponse.createdDate = this.dateUtils
                    .convertLocalDateFromServer(jsonResponse.createdDate);
            }

            if (jsonResponse && jsonResponse.dateOfBirth)
                jsonResponse.dateOfBirth = this.dateUtils
                    .convertLocalDateFromServer(jsonResponse.dateOfBirth);
          //  console.log('jsonResponse ' + JSON.stringify(jsonResponse));
            return jsonResponse;
        });
    }

    /* service to create user */
    createUser(user: User): Observable<ResponseWrapper> {
        user.tenantId = this.UserData.tenantId;
        console.log('in service ' + JSON.stringify(user));
        return this.http.post(`${this.userCreationUrl}?userId=${this.UserData.id}`, user)
            .map((res: Response) => {
                return res.json();
            });
    }

    /* service to create user @append tenantId and UserId from Component*/
    userCreation(user: User): Observable<ResponseWrapper> {
        return this.http.post(`${this.userCreationUrl}?userId=${user.curUser}`, user)
            .map((res: Response) => {
                return res.json();
            });
    }

    getListOfRoles(id: any) {
        return this.http.get(`${this.getUserByIdUrl}?id=${id}`).map((res: Response) => {
            return res.json();
        });
    }

        getUsersByTenantId() {
      //console.log('tenant ID ' + this.UserData.tenantId);
        
            return this.http.get(this.getUsersByTenantIdUrl).map((res: Response) => {
                const jsonResponse = res.json();
                return jsonResponse;
            });
        
    }  

    /* service to assign Roles To User */
    assignRolesToUser(user: any): Observable<ResponseWrapper> {
        user.tenantId = this.UserData.tenantId;
        console.log('in service ' + JSON.stringify(user));
        return this.http.post(`${this.assignRolesToUserUrl}`, user)
            .map((res: Response) => {
                return res.json();
            });
    }

    /*  uploadImg(){
         //postUserImage?userId=fdsfsdfsdf
     } */
    public uploadImg(files: any, id: any): Observable<Response> {
        let formData = new FormData();
        formData.append('file', files);
        let headers = new Headers({});

        return this.http.post(`${this.uploadImageUrl}?userId=${id}`, formData).map((res: Response) => {
            return res.json();
        });
    }

    /* Service to update roles */
    updateRole(obj: any): Observable<User> {
        return this.http.put(this.updateUserRoleUrl, obj).map((res: Response) => res.json());
    }

    /* Service to delete roles */
    deleteRole(obj: any): Observable<Response> {
        return this.http.delete(`${this.updateUserRoleUrl}/${obj.id}`);
    }

    /* Service to validate email id */
    
    validateEmailId(email:any){
        return this.http.get(this.emailIdValidationUrl + '/' + email).map((res: Response) => {
            console.log('res ' + res);
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }

    /* Users based on role id *///getUsersBasedOnRoleName?roleName=TEST_ROLE&tenantId=9
    usersBasedOnRole(name:any){
        console.log('this.UserData ' + JSON.stringify(this.UserData));
            return this.http.get(this.getUsersBasedOnRoleIdUrl + '?roleName=' + name).map((res: Response) => {
                console.log('res ' + res);
                const jsonResponse = res.json();
                return jsonResponse;
            });
    }

    /**Managerial Chain */
    getManagerialChain(tenantId, userId) {
        return this.http.get(`${this.getManagerialChainUrl}?tenantId=${tenantId}&userId=${userId}`).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        })
    }
    
}
