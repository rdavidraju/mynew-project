import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JeLdrDetailsComponent } from './je-ldr-details.component';
import { JeLdrDetailsDetailComponent } from './je-ldr-details-detail.component';
import { JeLdrDetailsPopupComponent } from './je-ldr-details-dialog.component';
import { JeLdrDetailsDeletePopupComponent } from './je-ldr-details-delete-dialog.component';

@Injectable()
export class JeLdrDetailsResolvePagingParams implements Resolve<any> {

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

export const jeLdrDetailsRoute: Routes = [
    {
        path: 'je-ldr-details',
        component: JeLdrDetailsComponent,
        resolve: {
            'pagingParams': JeLdrDetailsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLdrDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'je-ldr-details/:id',
        component: JeLdrDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLdrDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jeLdrDetailsPopupRoute: Routes = [
    {
        path: 'je-ldr-details-new',
        component: JeLdrDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLdrDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'je-ldr-details/:id/edit',
        component: JeLdrDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLdrDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'je-ldr-details/:id/delete',
        component: JeLdrDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLdrDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
