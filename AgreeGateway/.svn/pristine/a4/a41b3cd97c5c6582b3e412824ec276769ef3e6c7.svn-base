import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JobsHomeComponent } from './jobs-home.component';
import { JobsComponent } from './jobs.component';
import { JobsDetailComponent } from './jobs-detail.component';
import { JobsPopupComponent } from './jobs-dialog.component';
//import { JobsDeletePopupComponent } from './jobs-delete-dialog.component';
import { JobsBreadCrumbTitles } from './jobs.model';
import { JobsSchedularDetailsComponent } from './jobs-schedulars-list.component';

export const jobsRoute: Routes = [
    {
        path: 'jobs',
        component: JobsHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.jobs.home.title'
        },
        children: [
            {
                path: 'jobs-list',
                component: JobsComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.jobsList,
                    lablesArray: ['Setups','Schedulers',JobsBreadCrumbTitles.jobsList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: JobsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.jobDetails,
                    lablesArray: ['Setups','Jobs',JobsBreadCrumbTitles.jobDetails]
                },
                outlet: 'content'
            },
            {
                path: 'jobs-new',
                component: JobsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.jobNew,
                    lablesArray: ['Setups','Jobs',JobsBreadCrumbTitles.jobNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: JobsDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.jobEdit,
                    lablesArray: ['Setups','Jobs',JobsBreadCrumbTitles.jobEdit]
                },
                outlet: 'content'
            },
            {
                path: 'schedulars-list',
                component: JobsSchedularDetailsComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.schedList,
                    lablesArray: ['Process',JobsBreadCrumbTitles.schedList]
                },
                outlet: 'content'
            },
            {
                path: ':id/schedulars-list',
                component: JobsSchedularDetailsComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.schedList,
                    lablesArray: ['Setups','Jobs',JobsBreadCrumbTitles.schedList]
                },
                outlet: 'content'
            },
            {
                path: ':id/:type/:maintype/schedulars-list',
                component: JobsSchedularDetailsComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.jobs.home.title',
                    breadcrumb: JobsBreadCrumbTitles.schedList,
                    lablesArray: ['Setups','Jobs',JobsBreadCrumbTitles.schedList]
                },
                outlet: 'content'
            }
        ]
    }
];