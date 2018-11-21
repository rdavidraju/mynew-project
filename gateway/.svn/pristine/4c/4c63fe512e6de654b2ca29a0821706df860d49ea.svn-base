import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JournalsHeaderDataComponent } from './journals-header-data.component';
import { JournalsHeaderDataWQComponent }  from './journals-header-data-WQ.component';
import { JournalsHeaderDataDetailComponent } from './journals-header-data-detail.component';

import { JournalsHeaderDataPopupComponent } from './journals-header-data-dialog.component';
import { JournalsHeaderDataDeletePopupComponent } from './journals-header-data-delete-dialog.component';
import { JournalsHeaderDataBreadCrumbTitles } from './journals-header-data.model';
import { JournalsHeaderDataHomeComponent } from './journals-header-data-home.component';

import { Principal } from '../../shared';

@Injectable()
export class JournalsHeaderDataResolvePagingParams implements Resolve<any> {

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

export const journalsHeaderDataRoute: Routes = [
    {
        path: 'journals-header-data',
        component: JournalsHeaderDataHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title'
        },
        children: [
            {
                path: 'journals-header-data-home',
                component: JournalsHeaderDataWQComponent,
                canActivate: [UserRouteAccessService],
                resolve: {
                    'pagingParams': JournalsHeaderDataResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title',
                    breadcrumb: JournalsHeaderDataBreadCrumbTitles.journalsHeaderDataList,
                    lablesArray: ['Process','Journals']
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: JournalsHeaderDataDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title',
                    breadcrumb: JournalsHeaderDataBreadCrumbTitles.journalsHeaderDataDetails,
                    lablesArray: ['Process','Journals']
                },
                outlet: 'content'
            },
            {
                path: 'journals-header-data-new',
                component: JournalsHeaderDataDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title',
                    breadcrumb: JournalsHeaderDataBreadCrumbTitles.journalsHeaderDataNew,
                    lablesArray: ['Process','Journals']
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: JournalsHeaderDataDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title',
                    breadcrumb: JournalsHeaderDataBreadCrumbTitles.journalsHeaderDataEdit,
                    lablesArray: ['Process','Journals']
                },
                outlet: 'content'
            }
        ]
    }
];


/* export const journalsHeaderDataRoute: Routes = [
    {
        path: 'journals-header-data',
        component: JournalsHeaderDataComponent,
        resolve: {
            'pagingParams': JournalsHeaderDataResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'journals-header-data/:id',
        component: JournalsHeaderDataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const journalsHeaderDataPopupRoute: Routes = [
    {
        path: 'journals-header-data-new',
        component: JournalsHeaderDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'journals-header-data/:id/edit',
        component: JournalsHeaderDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'journals-header-data/:id/delete',
        component: JournalsHeaderDataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.journalsHeaderData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */