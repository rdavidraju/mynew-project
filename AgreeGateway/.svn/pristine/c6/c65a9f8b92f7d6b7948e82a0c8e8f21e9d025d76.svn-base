import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ApprovalGroupMembersComponent } from './approval-group-members.component';
import { ApprovalGroupMembersDetailComponent } from './approval-group-members-detail.component';
import { ApprovalGroupMembersPopupComponent } from './approval-group-members-dialog.component';
import { ApprovalGroupMembersDeletePopupComponent } from './approval-group-members-delete-dialog.component';

@Injectable()
export class ApprovalGroupMembersResolvePagingParams implements Resolve<any> {

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

export const approvalGroupMembersRoute: Routes = [
    {
        path: 'approval-group-members',
        component: ApprovalGroupMembersComponent,
        resolve: {
            'pagingParams': ApprovalGroupMembersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroupMembers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'approval-group-members/:id',
        component: ApprovalGroupMembersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroupMembers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const approvalGroupMembersPopupRoute: Routes = [
    {
        path: 'approval-group-members-new',
        component: ApprovalGroupMembersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroupMembers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'approval-group-members/:id/edit',
        component: ApprovalGroupMembersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroupMembers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'approval-group-members/:id/delete',
        component: ApprovalGroupMembersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.approvalGroupMembers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
