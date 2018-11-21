import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProcessesComponent } from './processes.component';
import { ProcessesDetailComponent } from './processes-detail.component';
import { ProcessesPopupComponent } from './processes-dialog.component';
import { ProcessesDeletePopupComponent } from './processes-delete-dialog.component';
import { ProcessHomeComponent } from './processes-home.component';
import { ProcessesBreadCrumbTitles } from './processes.model';

/**New Routes */
export const ProcessesRoute: Routes = [
    {
        path: 'processes',
        component: ProcessHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.processes.home.title'
        },
        children: [
            {
                path: 'processes-home',
                component: ProcessesComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.processes.home.title',
                    breadcrumb: ProcessesBreadCrumbTitles.processList,
                    lablesArray: ['Setups','Master Setups',ProcessesBreadCrumbTitles.processList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: ProcessesDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.processes.home.title',
                    breadcrumb: ProcessesBreadCrumbTitles.processDetails,
                    lablesArray: ['Setups','Master Setups',ProcessesBreadCrumbTitles.processDetails]
                },
                outlet: 'content'
            },
            {
                path: 'processes-new',
                component: ProcessesDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.processes.home.title',
                    breadcrumb: ProcessesBreadCrumbTitles.processNew,
                    lablesArray: ['Setups','Master Setups',ProcessesBreadCrumbTitles.processNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: ProcessesDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.processes.home.title',
                    breadcrumb: ProcessesBreadCrumbTitles.processEdit,
                    lablesArray: ['Setups','Master Setups',ProcessesBreadCrumbTitles.processEdit]
                },
                outlet: 'content'
            }
        ]
    }
];

/* @Injectable()
export class ProcessesResolvePagingParams implements Resolve<any> {

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

export const processesRoute: Routes = [
    {
        path: 'processes',
        component: ProcessesComponent,
        resolve: {
            'pagingParams': ProcessesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.processes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'processes/:id',
        component: ProcessesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.processes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const processesPopupRoute: Routes = [
    {
        path: 'processes-new',
        component: ProcessesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.processes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'processes/:id/edit',
        component: ProcessesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.processes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'processes/:id/delete',
        component: ProcessesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.processes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */