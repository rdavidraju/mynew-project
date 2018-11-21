import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { LookUpCodeComponent } from './look-up-code.component';
import { LookUpCodeDetailComponent } from './look-up-code-detail.component';
import { LookupCodeHomeComponent } from './look-up-code-home.component';
import { LookupCodesBreadCrumbTitles } from './look-up-code.model';

@Injectable()
export class LookUpCodeResolvePagingParams implements Resolve<any> {

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

export const LookupCodeRoute: Routes = [
    {
        path: 'look-up-code',
        component: LookupCodeHomeComponent,
        data: {
            authorities: ['LOOKUP_CODE_LIST'],
            pageTitle: 'agreeGatewayV1App.lookUpCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        children: [
            {
                path: 'look-up-code-home',
                component: LookUpCodeComponent,
                resolve: {
                    'pagingParams': LookUpCodeResolvePagingParams
                },
                data: {
                    authorities: ['LOOKUP_CODE_LIST'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeList,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeList]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: LookUpCodeDetailComponent,
                data: {
                    authorities: ['LOOKUP_CODE_VIEW'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeDetails,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeDetails]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'look-up-code-new',
                component: LookUpCodeDetailComponent,
                data: {
                    authorities: ['LOOKUP_CODE_CREATE'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeNew,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeNew]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: LookUpCodeDetailComponent,
                data: {
                    authorities: ['LOOKUP_CODE_EDIT'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeEdit,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeEdit]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }
];