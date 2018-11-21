import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { FxRatesComponent } from './fx-rates.component';
import { FxRatesDetailComponent } from './fx-rates-detail.component';
import { FxRatesHomeComponent } from './fx-rates-home.component';
import { FxRatesBreadCrumbTitles } from './fx-rates.model';
import { UserRouteAccessService } from '../../shared';

@Injectable()
export class FxRatesResolvePagingParams implements Resolve<any> {

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

export const FxRatesRoute: Routes = [
    {
        path: 'fx-rates',
        component: FxRatesHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.fxRates.home.title'
        },
        children: [
            {
                path: 'fx-rates-home',
                component: FxRatesComponent,
                canActivate: [UserRouteAccessService],
                resolve: {
                    'pagingParams': FxRatesResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.fxRates.home.title',
                    breadcrumb: FxRatesBreadCrumbTitles.fxRatesList,
                    lablesArray: ['Setups','Master Setups',FxRatesBreadCrumbTitles.fxRatesList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: FxRatesDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.fxRates.home.title',
                    breadcrumb: FxRatesBreadCrumbTitles.fxRatesDetails,
                    lablesArray: ['Setups','Master Setups',FxRatesBreadCrumbTitles.fxRatesDetails]
                },
                outlet: 'content'
            },
            {
                path: 'fx-rates-new',
                component: FxRatesDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.fxRates.home.title',
                    breadcrumb: FxRatesBreadCrumbTitles.fxRatesNew,
                    lablesArray: ['Setups','Master Setups',FxRatesBreadCrumbTitles.fxRatesNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: FxRatesDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.fxRates.home.title',
                    breadcrumb: FxRatesBreadCrumbTitles.fxRatesEdit,
                    lablesArray: ['Setups','Master Setups',FxRatesBreadCrumbTitles.fxRatesEdit]
                },
                outlet: 'content'
            }
        ]
    }
];