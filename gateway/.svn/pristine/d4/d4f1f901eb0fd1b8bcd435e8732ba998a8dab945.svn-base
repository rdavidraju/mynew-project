import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AcctRuleDerivationsComponent } from './acct-rule-derivations.component';
import { AcctRuleDerivationsDetailComponent } from './acct-rule-derivations-detail.component';
import { AcctRuleDerivationsPopupComponent } from './acct-rule-derivations-dialog.component';
import { AcctRuleDerivationsDeletePopupComponent } from './acct-rule-derivations-delete-dialog.component';

@Injectable()
export class AcctRuleDerivationsResolvePagingParams implements Resolve<any> {

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

export const acctRuleDerivationsRoute: Routes = [
    {
        path: 'acct-rule-derivations',
        component: AcctRuleDerivationsComponent,
        resolve: {
            'pagingParams': AcctRuleDerivationsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleDerivations.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'acct-rule-derivations/:id',
        component: AcctRuleDerivationsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleDerivations.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const acctRuleDerivationsPopupRoute: Routes = [
    {
        path: 'acct-rule-derivations-new',
        component: AcctRuleDerivationsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleDerivations.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'acct-rule-derivations/:id/edit',
        component: AcctRuleDerivationsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleDerivations.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'acct-rule-derivations/:id/delete',
        component: AcctRuleDerivationsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.acctRuleDerivations.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
