import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TenantDetailsComponent } from './tenant-details.component';
import { TenantDetailsDetailComponent } from './tenant-details-detail.component';
import { TenantDetailsPopupComponent } from './tenant-details-dialog.component';
import { TenantDetailsDeletePopupComponent } from './tenant-details-delete-dialog.component';
import { TenantDetailsHomeComponent } from './tenant-details-home.component';
import { TenantDetails,TenantDetailsBreadCrumbTitles } from './tenant-details.model';

@Injectable()
export class TenantDetailsResolvePagingParams implements Resolve<any> {

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

export const TenantDetailsRoute: Routes = [
    {
        path: 'tenant-details',
        component: TenantDetailsHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.tenantDetails.home.title'
        },
        children: [
            {
                path: 'tenant-details-home',
                component: TenantDetailsComponent,
                resolve: {
                    'pagingParams': TenantDetailsResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.tenantDetails.home.title',
                    breadcrumb: TenantDetailsBreadCrumbTitles.tenantDetailsList,
                    lablesArray: ['Admin',TenantDetailsBreadCrumbTitles.tenantDetailsList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: TenantDetailsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.tenantDetails.home.title',
                    breadcrumb: TenantDetailsBreadCrumbTitles.tenantDetailsDetails,
                    lablesArray: ['Admin',TenantDetailsBreadCrumbTitles.tenantDetailsDetails]
                },
                outlet: 'content'
            },
            {
                path: 'tenant-details-new',
                component: TenantDetailsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.tenantDetails.home.title',
                    breadcrumb: TenantDetailsBreadCrumbTitles.tenantDetailsNew,
                    lablesArray: ['Admin',TenantDetailsBreadCrumbTitles.tenantDetailsNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: TenantDetailsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.tenantDetails.home.title',
                    breadcrumb: TenantDetailsBreadCrumbTitles.tenantDetailsEdit,
                    lablesArray: ['Admin',TenantDetailsBreadCrumbTitles.tenantDetailsEdit]
                },
                outlet: 'content'
            }
        ]
    }
];

/* export const tenantDetailsRoute: Routes = [
    {
        path: 'tenant-details',
        component: TenantDetailsComponent,
        resolve: {
            'pagingParams': TenantDetailsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.tenantDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tenant-details/:id',
        component: TenantDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.tenantDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tenantDetailsPopupRoute: Routes = [
    {
        path: 'tenant-details-new',
        component: TenantDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.tenantDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tenant-details/:id/edit',
        component: TenantDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.tenantDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tenant-details/:id/delete',
        component: TenantDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.tenantDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */