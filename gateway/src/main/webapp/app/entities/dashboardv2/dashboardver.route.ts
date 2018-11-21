import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DashboardV6Component } from './dashboard-v6.component';
import { DashboardV7Component } from './dashboard-v7.component';
import { DashboardV8Component } from './dashboard-v8.component';
import { DashboardV2BreadCrumbTitles } from './dashboardv2.model';

export const dashboardverRoute: Routes = [
    {
        path: 'dashboardv6',
        component: DashboardV6Component,
        
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.dataViewUnion.home.title',
            breadcrumb: DashboardV2BreadCrumbTitles.dashboard,
            lablesArray: [DashboardV2BreadCrumbTitles.dashboard]
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'processboard',
        component: DashboardV7Component,
        
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.dashboard.home.title',
            breadcrumb: DashboardV2BreadCrumbTitles.dashboard,
            lablesArray: [DashboardV2BreadCrumbTitles.dashboard]
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'kpiboard',
        component: DashboardV8Component,
        
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.dashboard.home.title',
            breadcrumb: DashboardV2BreadCrumbTitles.dashboard,
            lablesArray: [DashboardV2BreadCrumbTitles.dashboard]
        },
        canActivate: [UserRouteAccessService]
    }
];
