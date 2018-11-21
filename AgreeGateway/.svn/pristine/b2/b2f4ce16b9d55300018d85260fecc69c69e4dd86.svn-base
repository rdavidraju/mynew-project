import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DashboardV6Component } from './dashboard-v6.component';



export const dashboardverRoute: Routes = [
    {
        path: 'dashboardv2',
        component: DashboardV6Component,
        
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.dataViewUnion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
