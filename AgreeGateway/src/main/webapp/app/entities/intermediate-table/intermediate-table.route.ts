import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

//import { IntermediateTableComponent } from './intermediate-table.component';
//import { IntermediateTableDetailComponent } from './intermediate-table-detail.component';
//import { IntermediateTablePopupComponent } from './intermediate-table-dialog.component';
//import { IntermediateTableDeletePopupComponent } from './intermediate-table-delete-dialog.component';

export const intermediateTableRoute: Routes = [
    // {
    //     path: 'intermediate-table',
    //     component: IntermediateTableComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.intermediateTable.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }, {
    //     path: 'intermediate-table/:id',
    //     component: IntermediateTableDetailComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.intermediateTable.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }
];

export const intermediateTablePopupRoute: Routes = [
    // {
    //     path: 'intermediate-table-new',
    //     component: IntermediateTablePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.intermediateTable.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'intermediate-table/:id/edit',
    //     component: IntermediateTablePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.intermediateTable.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'intermediate-table/:id/delete',
    //     component: IntermediateTableDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.intermediateTable.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
