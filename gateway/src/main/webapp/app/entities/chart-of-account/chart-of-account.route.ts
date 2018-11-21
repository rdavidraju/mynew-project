import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ChartOfAccountComponent } from './chart-of-account.component';
import { ChartOfAccountDetailComponent } from './chart-of-account-detail.component';
import { ChartOfAccountHomeComponent } from './chart-of-account-home.component';
import { ChartOfAccountBreadCrumbTitles } from './chart-of-account.model';
import { UserRouteAccessService } from '../../shared';

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
                canActivate: [UserRouteAccessService],
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
                canActivate: [UserRouteAccessService],
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
                canActivate: [UserRouteAccessService],
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
                canActivate: [UserRouteAccessService],
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