import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { SessionStorageService } from 'ng2-webstorage';

@Injectable()
export class HierarchyService {

    //private UserData = this.$sessionStorage.retrieve('userData');
    private gethierarchyUrl = 'agreeapplication/api/hierarchyListFromParentHead';
    constructor(
        private http: Http,
        private $sessionStorage: SessionStorageService
    ) {}
    
    /**Get Hierarchy based on tenantId*/
    getHierarchy(){
        return this.http.get(this.gethierarchyUrl).map((res: Response)=>{
            const jsonResponse = res.json();
            return jsonResponse;
        })
    }
}