import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MappingSetComponent } from './mapping-set.component';
import { MappingSetDetailComponent } from './mapping-set-detail.component';
import { MappingSetPopupComponent } from './mapping-set-dialog.component';
import { MappingSetDeletePopupComponent } from './mapping-set-delete-dialog.component';
import { MappingSetBreadCrumbTitles } from './mapping-set.model';
import { MappingSetHomeComponent } from './mapping-set-home.component';

import { Principal } from '../../shared';

export const mappingSetRoute: Routes = [
    {
        path: 'mapping-set',
        component: MappingSetHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
        },
        children: [
            {
                path: 'mapping-set-home',
                component: MappingSetComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetList,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: MappingSetDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetDetails,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetDetails]
                },
                outlet: 'content'
            },
            {
                path: 'mapping-set-new',
                component: MappingSetDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetNew,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: MappingSetDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetEdit,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetEdit]
                },
                outlet: 'content'
            }
        ]
    }
];

// export const mappingSetRoute: Routes = [
//     {
//         path: 'mapping-set',
//         component: MappingSetComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
//         },
//         canActivate: [UserRouteAccessService]
//     }, {
//         path: 'mapping-set/:id',
//         component: MappingSetDetailComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
//         },
//         canActivate: [UserRouteAccessService]
//     }
// ];

// export const mappingSetPopupRoute: Routes = [
//     {
//         path: 'mapping-set-new',
//         component: MappingSetPopupComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
//         },
//         canActivate: [UserRouteAccessService],
//         outlet: 'popup'
//     },
//     {
//         path: 'mapping-set/:id/edit',
//         component: MappingSetPopupComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
//         },
//         canActivate: [UserRouteAccessService],
//         outlet: 'popup'
//     },
//     {
//         path: 'mapping-set/:id/delete',
//         component: MappingSetDeletePopupComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
//         },
//         canActivate: [UserRouteAccessService],
//         outlet: 'popup'
//     }
// ];
