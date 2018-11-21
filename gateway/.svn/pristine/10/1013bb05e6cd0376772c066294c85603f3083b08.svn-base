import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobDetailsComponent } from './job-details.component';
//import { JobDetailsDetailComponent } from './job-details-detail.component';
//import { JobDetailsPopupComponent } from './job-details-dialog.component';
//import { JobDetailsDeletePopupComponent } from './job-details-delete-dialog.component';

export const jobDetailsRoute: Routes = [
    {
        path: 'job-details',
        component: JobDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.jobDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    // , {
    //     path: 'job-details/:id',
    //     component: JobDetailsDetailComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.jobDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }
];

export const jobDetailsPopupRoute: Routes = [
    // {
    //     path: 'job-details-new',
    //     component: JobDetailsPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.jobDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'job-details/:id/edit',
    //     component: JobDetailsPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.jobDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'job-details/:id/delete',
    //     component: JobDetailsDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayApp.jobDetails.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
