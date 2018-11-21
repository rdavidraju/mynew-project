import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ChartOfAccountComponent } from './chart-of-account.component';
import { ChartOfAccountDetailComponent } from './chart-of-account-detail.component';
import { ChartOfAccountPopupComponent } from './chart-of-account-dialog.component';
import { ChartOfAccountDeletePopupComponent } from './chart-of-account-delete-dialog.component';
import { ChartOfAccountHomeComponent } from './chart-of-account-home.component';
import { ChartOfAccount,ChartOfAccountBreadCrumbTitles } from './chart-of-account.model';

@Injectable()
export class ChartOfAccountResolvePagingParams implements Resolve<any> {

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

export const ChartOfAccountRoute: Routes = [
    {
        path: 'chart-of-account',
        component: ChartOfAccountHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title'
        },
        children: [
            {
                path: 'chart-of-account-home',
                component: ChartOfAccountComponent,
                resolve: {
                    'pagingParams': ChartOfAccountResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title',
                    breadcrumb: ChartOfAccountBreadCrumbTitles.chartOfAccountList,
                    lablesArray: ['Setups','Master Setups',ChartOfAccountBreadCrumbTitles.chartOfAccountList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: ChartOfAccountDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title',
                    breadcrumb: ChartOfAccountBreadCrumbTitles.chartOfAccountDetails,
                    lablesArray: ['Setups','Master Setups',ChartOfAccountBreadCrumbTitles.chartOfAccountDetails]
                },
                outlet: 'content'
            },
            {
                path: 'chart-of-account-new',
                component: ChartOfAccountDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title',
                    breadcrumb: ChartOfAccountBreadCrumbTitles.chartOfAccountNew,
                    lablesArray: ['Setups','Master Setups',ChartOfAccountBreadCrumbTitles.chartOfAccountNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: ChartOfAccountDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title',
                    breadcrumb: ChartOfAccountBreadCrumbTitles.chartOfAccountEdit,
                    lablesArray: ['Setups','Master Setups',ChartOfAccountBreadCrumbTitles.chartOfAccountEdit]
                },
                outlet: 'content'
            }
        ]
    }
];

/* export const chartOfAccountRoute: Routes = [
    {
        path: 'chart-of-account',
        component: ChartOfAccountComponent,
        resolve: {
            'pagingParams': ChartOfAccountResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chart-of-account/:id',
        component: ChartOfAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chartOfAccountPopupRoute: Routes = [
    {
        path: 'chart-of-account-new',
        component: ChartOfAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chart-of-account/:id/edit',
        component: ChartOfAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chart-of-account/:id/delete',
        component: ChartOfAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.chartOfAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */