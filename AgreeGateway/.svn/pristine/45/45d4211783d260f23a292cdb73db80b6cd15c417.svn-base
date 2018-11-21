import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JeLinesComponent } from './je-lines.component';
import { JeLinesDetailComponent } from './je-lines-detail.component';
import { JeLinesPopupComponent } from './je-lines-dialog.component';
import { JeLinesDeletePopupComponent } from './je-lines-delete-dialog.component';

export const jeLinesRoute: Routes = [
    {
        path: 'je-lines',
        component: JeLinesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLines.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'je-lines/:id',
        component: JeLinesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLines.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jeLinesPopupRoute: Routes = [
    {
        path: 'je-lines-new',
        component: JeLinesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLines.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'je-lines/:id/edit',
        component: JeLinesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLines.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'je-lines/:id/delete',
        component: JeLinesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jeLines.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
