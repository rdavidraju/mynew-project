import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LineCriteriaComponent } from './line-criteria.component';
import { LineCriteriaDetailComponent } from './line-criteria-detail.component';
import { LineCriteriaPopupComponent } from './line-criteria-dialog.component';
import { LineCriteriaDeletePopupComponent } from './line-criteria-delete-dialog.component';

@Injectable()
export class LineCriteriaResolvePagingParams implements Resolve<any> {

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

export const lineCriteriaRoute: Routes = [
    {
        path: 'line-criteria',
        component: LineCriteriaComponent,
        resolve: {
            'pagingParams': LineCriteriaResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.lineCriteria.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'line-criteria/:id',
        component: LineCriteriaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.lineCriteria.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lineCriteriaPopupRoute: Routes = [
    {
        path: 'line-criteria-new',
        component: LineCriteriaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.lineCriteria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'line-criteria/:id/edit',
        component: LineCriteriaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.lineCriteria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'line-criteria/:id/delete',
        component: LineCriteriaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.lineCriteria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
