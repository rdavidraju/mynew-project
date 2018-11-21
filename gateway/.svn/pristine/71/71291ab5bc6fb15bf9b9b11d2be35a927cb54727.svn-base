import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AccountingLineTypesComponent } from './accounting-line-types.component';
import { AccountingLineTypesDetailComponent } from './accounting-line-types-detail.component';
import { AccountingLineTypesPopupComponent } from './accounting-line-types-dialog.component';
import { AccountingLineTypesDeletePopupComponent } from './accounting-line-types-delete-dialog.component';

export const accountingLineTypesRoute: Routes = [
    {
        path: 'accounting-line-types',
        component: AccountingLineTypesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingLineTypes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'accounting-line-types/:id',
        component: AccountingLineTypesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingLineTypes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const accountingLineTypesPopupRoute: Routes = [
    {
        path: 'accounting-line-types-new',
        component: AccountingLineTypesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingLineTypes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'accounting-line-types/:id/edit',
        component: AccountingLineTypesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingLineTypes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'accounting-line-types/:id/delete',
        component: AccountingLineTypesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingLineTypes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
