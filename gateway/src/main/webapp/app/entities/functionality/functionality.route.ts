import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FunctionalityComponent } from './functionality.component';
import { FunctionalityDetailComponent } from './functionality-detail.component';
import { FunctionalityPopupComponent } from './functionality-dialog.component';
import { FunctionalityDeletePopupComponent } from './functionality-delete-dialog.component';
import { FunctionalityBreadCrumbTitles } from './functionality.model';
import { FunctionalityHomeComponent } from './functionality-home.component';

import { Principal } from '../../shared';

@Injectable()
export class UserResolve implements CanActivate {

    constructor(private principal: Principal) { }

    canActivate() {
        return this.principal.identity().then((account) => this.principal.hasAnyAuthority(['ROLE_ADMIN']));
    }
}

@Injectable()
export class FunctionalityResolvePagingParams implements Resolve<any> {

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

export const functionalityRoute: Routes = [
    {
        path: 'functionality',
        component: FunctionalityHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.functionality.home.title'
        },
        children: [
            {
                path: 'functionality-home',
                component: FunctionalityComponent,
                canActivate: [UserRouteAccessService],
                resolve: {
                    'pagingParams': FunctionalityResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.functionality.home.title',
                    breadcrumb: FunctionalityBreadCrumbTitles.functionalityList,
                    lablesArray: ['Admin','User Management',FunctionalityBreadCrumbTitles.functionalityList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: FunctionalityDetailComponent,
                canActivate: [UserRouteAccessService],
                resolve: {
                    'pagingParams': FunctionalityResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.functionality.home.title',
                    breadcrumb: FunctionalityBreadCrumbTitles.functionalityDetails,
                    lablesArray: ['Admin','User Management',FunctionalityBreadCrumbTitles.functionalityDetails]
                },
                outlet: 'content'
            },
            {
                path: 'functionality-new',
                component: FunctionalityDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.functionality.home.title',
                    breadcrumb: FunctionalityBreadCrumbTitles.functionalityNew,
                    lablesArray: ['Admin','User Management',FunctionalityBreadCrumbTitles.functionalityNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: FunctionalityDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.functionality.home.title',
                    breadcrumb: FunctionalityBreadCrumbTitles.functionalityEdit,
                    lablesArray: ['Admin','User Management',FunctionalityBreadCrumbTitles.functionalityEdit]
                },
                outlet: 'content'
            }
        ]
    }
];

/* export const functionalityRoute: Routes = [
    {
        path: 'functionality',
        component: FunctionalityComponent,
        resolve: {
            'pagingParams': FunctionalityResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.functionality.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'functionality/:id',
        component: FunctionalityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.functionality.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const functionalityPopupRoute: Routes = [
    {
        path: 'functionality-new',
        component: FunctionalityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.functionality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'functionality/:id/edit',
        component: FunctionalityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.functionality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'functionality/:id/delete',
        component: FunctionalityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.functionality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */