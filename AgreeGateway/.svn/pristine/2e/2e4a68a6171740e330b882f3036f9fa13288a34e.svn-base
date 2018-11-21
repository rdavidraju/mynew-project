import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TemplateDetailsComponent } from './template-details.component';
import { TemplateDetailsDetailComponent } from './template-details-detail.component';
import { TemplateDefinitionDetailComponent } from './template-definition-detail.component';
import { TemplateDetailsPopupComponent } from './template-details-dialog.component';
import { TemplateDetailsDeletePopupComponent } from './template-details-delete-dialog.component';
import { TemplateDetailsBreadCrumbTitles } from './template-details.model';
import { TemplateDetailsHomeComponent } from './template-details-home.component';

import { Principal } from '../../shared';

@Injectable()
export class TemplateDetailsResolvePagingParams implements Resolve<any> {

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

export const templateDetailsRoute: Routes = [
    {
        path: 'template-details',
        component: TemplateDetailsHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templateDetails.home.title'
        },
        children: [
            {
                path: 'template-details-home',
                component: TemplateDetailsComponent,
                resolve: {
                    'pagingParams': TemplateDetailsResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.templateDetails.home.title',
                    breadcrumb: TemplateDetailsBreadCrumbTitles.templateDetailsList,
                    lablesArray: ['Setups',TemplateDetailsBreadCrumbTitles.templateDetailsList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: TemplateDetailsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.templateDetails.home.title',
                    breadcrumb: TemplateDetailsBreadCrumbTitles.templateDetailsDetails,
                    lablesArray: ['Setups',TemplateDetailsBreadCrumbTitles.templateDetailsDetails]
                },
                outlet: 'content'
            },
            {
                path: 'template-details-new',
                component: TemplateDetailsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.templateDetails.home.title',
                    breadcrumb: TemplateDetailsBreadCrumbTitles.templateDetailsNew,
                    lablesArray: ['Setups',TemplateDetailsBreadCrumbTitles.templateDetailsNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: TemplateDetailsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.templateDetails.home.title',
                    breadcrumb: TemplateDetailsBreadCrumbTitles.templateDetailsEdit,
                    lablesArray: ['Setups',TemplateDetailsBreadCrumbTitles.templateDetailsEdit]
                },
                outlet: 'content'
            },
            {
                path: ':id/template-details',
                component: TemplateDefinitionDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.templateDetails.home.title',
                    breadcrumb: TemplateDetailsBreadCrumbTitles.templateDefinitionDetails,
                    lablesArray: ['Setups',TemplateDetailsBreadCrumbTitles.templateDefinitionDetails]
                },
                outlet: 'content'
            },
            {
                path: 'template-definition-new',
                component: TemplateDefinitionDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.templateDetails.home.title',
                    breadcrumb: TemplateDetailsBreadCrumbTitles.templateDefinitionNew,
                    lablesArray: ['Setups',TemplateDetailsBreadCrumbTitles.templateDefinitionNew]
                },
                outlet: 'content'
            }
        ]
    }
];
/* 
export const templateDetailsRoute: Routes = [
    {
        path: 'template-details',
        component: TemplateDetailsComponent,
        resolve: {
            'pagingParams': TemplateDetailsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templateDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'template-details/:id',
        component: TemplateDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templateDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const templateDetailsPopupRoute: Routes = [
    {
        path: 'template-details-new',
        component: TemplateDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templateDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'template-details/:id/edit',
        component: TemplateDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templateDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'template-details/:id/delete',
        component: TemplateDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templateDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
]; */
