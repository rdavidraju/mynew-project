import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SchedulerDetailsComponent } from './scheduler-details.component';
//import { SchedulerDetailsDetailComponent } from './scheduler-details-detail.component';
//import { SchedulerDetailsPopupComponent } from './scheduler-details-dialog.component';
//import { SchedulerDetailsDeletePopupComponent } from './scheduler-details-delete-dialog.component';

export const schedulerDetailsRoute: Routes = [
    {
        path: 'scheduler-details',
        component: SchedulerDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.schedulerDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    // , 
    // {
    //     path: 'scheduler-details/:id',
    //     component: SchedulerDetailsDetailComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.schedulerDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }
];

export const schedulerDetailsPopupRoute: Routes = [
    // {
    //     path: 'scheduler-details-new',
    //     component: SchedulerDetailsPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.schedulerDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'scheduler-details/:id/edit',
    //     component: SchedulerDetailsPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.schedulerDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'scheduler-details/:id/delete',
    //     component: SchedulerDetailsDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.schedulerDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
