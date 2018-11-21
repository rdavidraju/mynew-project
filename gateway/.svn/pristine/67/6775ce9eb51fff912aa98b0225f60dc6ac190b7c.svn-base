import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ApplicationProgramsComponent } from './application-programs.component';
import { ApplicationProgramsDetailComponent } from './application-programs-detail.component';
import { ApplicationProgramsPopupComponent } from './application-programs-dialog.component';
import { ApplicationProgramsDeletePopupComponent } from './application-programs-delete-dialog.component';

export const applicationProgramsRoute: Routes = [
    {
        path: 'application-programs',
        component: ApplicationProgramsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.applicationPrograms.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'application-programs/:id',
        component: ApplicationProgramsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.applicationPrograms.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const applicationProgramsPopupRoute: Routes = [
    {
        path: 'application-programs-new',
        component: ApplicationProgramsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.applicationPrograms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'application-programs/:id/edit',
        component: ApplicationProgramsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.applicationPrograms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'application-programs/:id/delete',
        component: ApplicationProgramsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayApp.applicationPrograms.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
