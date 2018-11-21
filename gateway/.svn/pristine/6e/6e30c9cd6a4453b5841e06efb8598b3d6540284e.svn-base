import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import {DataManagementWqHomeComponent} from './data-management-wq-home.component';
import {DataManagementWQBreadCrumbTitles} from './data-management-wq.model';
import { DataManagementWqComponent } from './data-management-wq.component';

@Injectable()
export class DataManagementWQResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const dataManagementWqRoute: Routes = [
    {
        path: 'ETL-WQ',
        component: DataManagementWqHomeComponent,
        resolve: {
            'pagingParams': DataManagementWQResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER']
        },
        children: [
                   {
                       path: 'ETL-WQ-home',
                       component: DataManagementWqComponent,
                       canActivate: [UserRouteAccessService],
                      
                       data: {
                           authorities: ['ROLE_USER'],
                           pageTitle: 'dataManagementWq.title',
                           breadcrumb: DataManagementWQBreadCrumbTitles.dataManagementHome,
                           lablesArray: ['Process','ETL Queue']
                       },
                       outlet: 'content'
                   }]
    }
];


