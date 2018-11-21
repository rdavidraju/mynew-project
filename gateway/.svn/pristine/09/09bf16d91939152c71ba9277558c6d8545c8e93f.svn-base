import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PeriodsComponent } from './periods.component';
import { PeriodsDetailComponent } from './periods-detail.component';
import { PeriodsPopupComponent } from './periods-dialog.component';
import { PeriodsDeletePopupComponent } from './periods-delete-dialog.component';

@Injectable()
export class PeriodsResolvePagingParams implements Resolve<any> {

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

export const periodsRoute: Routes = [
    {
        path: 'periods',
        component: PeriodsComponent,
        resolve: {
            'pagingParams': PeriodsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.periods.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'periods/:id',
        component: PeriodsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.periods.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const periodsPopupRoute: Routes = [
    {
        path: 'periods-new',
        component: PeriodsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.periods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'periods/:id/edit',
        component: PeriodsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.periods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'periods/:id/delete',
        component: PeriodsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.periods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
