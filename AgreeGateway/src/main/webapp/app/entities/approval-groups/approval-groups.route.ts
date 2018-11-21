import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ApprovalGroupsComponent } from './approval-groups.component';
import { ApprovalGroupsDetailComponent } from './approval-groups-detail.component';
import { ApprovalGroupsPopupComponent } from './approval-groups-dialog.component';
import { ApprovalGroupsDeletePopupComponent } from './approval-groups-delete-dialog.component';
import { ApprovalGroupsHomeComponent } from './approval-groups-home.component';
import { ApprovalGroups, ApprovalGroupsBreadCrumbTitles } from './approval-groups.model';


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
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        children: [
            {
                path: 'groups-home',
                component: ApprovalGroupsComponent,
                resolve: {
                    'pagingParams': ApprovalGroupsResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsList,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: ApprovalGroupsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsDetails,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsDetails]
                },
                outlet: 'content'
            },
            {
                path: 'groups-new',
                component: ApprovalGroupsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsNew,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: ApprovalGroupsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.approvalGroups.home.title',
                    breadcrumb: ApprovalGroupsBreadCrumbTitles.approvalGroupsEdit,
                    lablesArray: ['Admin','User Management',ApprovalGroupsBreadCrumbTitles.approvalGroupsEdit]
                },
                outlet: 'content'
            }
        ]
    }
];






/* export const approvalGroupsRoute: Routes = [
    {
        path: 'approval-groups',
        component: ApprovalGroupsComponent,
        resolve: {
            'pagingParams': ApprovalGroupsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'approval-groups/:id',
        component: ApprovalGroupsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const approvalGroupsPopupRoute: Routes = [
    {
        path: 'approval-groups-new',
        component: ApprovalGroupsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'approval-groups/:id/edit',
        component: ApprovalGroupsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'approval-groups/:id/delete',
        component: ApprovalGroupsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroups.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */