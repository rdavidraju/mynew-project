import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LookUpCodeComponent } from './look-up-code.component';
import { LookUpCodeDetailComponent } from './look-up-code-detail.component';
import { LookUpCodePopupComponent } from './look-up-code-dialog.component';
import { LookUpCodeDeletePopupComponent } from './look-up-code-delete-dialog.component';
import { LookupCodeHomeComponent } from './look-up-code-home.component';
import { LookUpCode,LookupCodesBreadCrumbTitles } from './look-up-code.model';

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
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.lookUpCode.home.title'
        },
        children: [
            {
                path: 'look-up-code-home',
                component: LookUpCodeComponent,
                resolve: {
                    'pagingParams': LookUpCodeResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeList,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: LookUpCodeDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeDetails,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeDetails]
                },
                outlet: 'content'
            },
            {
                path: 'look-up-code-new',
                component: LookUpCodeDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeNew,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: LookUpCodeDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.lookUpCode.home.title',
                    breadcrumb: LookupCodesBreadCrumbTitles.lookUpCodeEdit,
                    lablesArray: ['Admin',LookupCodesBreadCrumbTitles.lookUpCodeEdit]
                },
                outlet: 'content'
            }
        ]
    }
];


/* export const lookUpCodeRoute: Routes = [
    {
        path: 'look-up-code',
        component: LookUpCodeComponent,
        resolve: {
            'pagingParams': LookUpCodeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.lookUpCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'look-up-code/:id',
        component: LookUpCodeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.lookUpCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lookUpCodePopupRoute: Routes = [
    {
        path: 'look-up-code-new',
        component: LookUpCodePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.lookUpCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'look-up-code/:id/edit',
        component: LookUpCodePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.lookUpCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'look-up-code/:id/delete',
        component: LookUpCodeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.lookUpCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */