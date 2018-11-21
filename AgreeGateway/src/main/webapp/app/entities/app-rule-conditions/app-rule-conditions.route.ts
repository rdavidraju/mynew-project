import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AppRuleConditionsComponent } from './app-rule-conditions.component';
import { AppRuleConditionsDetailComponent } from './app-rule-conditions-detail.component';
import { AppRuleConditionsPopupComponent } from './app-rule-conditions-dialog.component';
import { AppRuleConditionsDeletePopupComponent } from './app-rule-conditions-delete-dialog.component';

@Injectable()
export class AppRuleConditionsResolvePagingParams implements Resolve<any> {

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

export const appRuleConditionsRoute: Routes = [
    {
        path: 'app-rule-conditions',
        component: AppRuleConditionsComponent,
        resolve: {
            'pagingParams': AppRuleConditionsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.appRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'app-rule-conditions/:id',
        component: AppRuleConditionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.appRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const appRuleConditionsPopupRoute: Routes = [
    {
        path: 'app-rule-conditions-new',
        component: AppRuleConditionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.appRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app-rule-conditions/:id/edit',
        component: AppRuleConditionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.appRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app-rule-conditions/:id/delete',
        component: AppRuleConditionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.appRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
