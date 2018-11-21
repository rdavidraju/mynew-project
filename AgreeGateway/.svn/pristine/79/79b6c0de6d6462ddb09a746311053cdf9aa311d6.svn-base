import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobActionsComponent } from './job-actions.component';
import { JobActionsDetailComponent } from './job-actions-detail.component';
import { JobActionsPopupComponent } from './job-actions-dialog.component';
import { JobActionsDeletePopupComponent } from './job-actions-delete-dialog.component';

export const jobActionsRoute: Routes = [
    {
        path: 'job-actions',
        component: JobActionsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jobActions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'job-actions/:id',
        component: JobActionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jobActions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jobActionsPopupRoute: Routes = [
    {
        path: 'job-actions-new',
        component: JobActionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jobActions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-actions/:id/edit',
        component: JobActionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jobActions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'job-actions/:id/delete',
        component: JobActionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jobActions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
