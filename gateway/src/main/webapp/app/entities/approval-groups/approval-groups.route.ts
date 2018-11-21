import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ApprovalGroupsComponent } from './approval-groups.component';
import { ApprovalGroupsDetailComponent } from './approval-groups-detail.component';
import { ApprovalGroupsHomeComponent } from './approval-groups-home.component';
import { ApprovalGroupsBreadCrumbTitles } from './approval-groups.model';


@Injectable()
export class ApprovalGroupsResolvePagingParams implements Resolve<any> {

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


export const ApprovalGroupsRoute: Routes = [
    {
        path: 'groups',
        component: ApprovalGroupsHomeComponent,
        data: {
            authorities: ['GROUPS_LIST'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        canActivate: [UserRouteAccessService],
        children: [
            {
                path: 'groups-home',
                component: ApprovalGroupsComponent,
                resolve: {
                    'pagingParams': ApprovalGroupsResolvePagingParams
                },
                data: {
                    authorities: ['GROUPS_LIST'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsList,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsList]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: ApprovalGroupsDetailComponent,
                data: {
                    authorities: ['GROUP_VIEW'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsDetails,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsDetails]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'groups-new',
                component: ApprovalGroupsDetailComponent,
                data: {
                    authorities: ['GROUP_CREATE'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsNew,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsNew]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: ApprovalGroupsDetailComponent,
                data: {
                    authorities: ['GROUP_EDIT'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsEdit,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsEdit]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }
];