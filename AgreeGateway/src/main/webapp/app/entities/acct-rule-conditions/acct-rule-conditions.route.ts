import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AcctRuleConditionsComponent } from './acct-rule-conditions.component';
import { AcctRuleConditionsDetailComponent } from './acct-rule-conditions-detail.component';
import { AcctRuleConditionsPopupComponent } from './acct-rule-conditions-dialog.component';
import { AcctRuleConditionsDeletePopupComponent } from './acct-rule-conditions-delete-dialog.component';

@Injectable()
export class AcctRuleConditionsResolvePagingParams implements Resolve<any> {

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

export const acctRuleConditionsRoute: Routes = [
    {
        path: 'acct-rule-conditions',
        component: AcctRuleConditionsComponent,
        resolve: {
            'pagingParams': AcctRuleConditionsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'acct-rule-conditions/:id',
        component: AcctRuleConditionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const acctRuleConditionsPopupRoute: Routes = [
    {
        path: 'acct-rule-conditions-new',
        component: AcctRuleConditionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'acct-rule-conditions/:id/edit',
        component: AcctRuleConditionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'acct-rule-conditions/:id/delete',
        component: AcctRuleConditionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleConditions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
