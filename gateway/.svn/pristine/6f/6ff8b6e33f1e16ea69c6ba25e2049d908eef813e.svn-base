import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReportsBreadCrumbTitles } from './reports.model';
import { ReportsComponent } from './reports.component';
import { ReportsDetailComponent } from './reports-detail.component';
import { ReportsHomeComponent } from './reports-home.component';
import { RunReportsComponent } from './run-reports.component';
import { ReportsDashboardComponent } from './reports-dashboard.component';
import { BucketListComponent } from './agingBuckets.component';
import { BucketDetailComponent } from './agingBucket-detail.component';

//@Injectable()
//export class ReportsResolvePagingParams implements Resolve<any> {
//
//    constructor(private paginationUtil: JhiPaginationUtil) {}
//
//    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
//        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
//        return {
//            page: this.paginationUtil.parsePage(page),
//            predicate: this.paginationUtil.parsePredicate(sort),
//            ascending: this.paginationUtil.parseAscending(sort)
//      };
//    }
//}

export const reportsRoute: Routes = [
  {
    path: 'reports',
    component: ReportsHomeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Reports'
    },
    children: [
      {
        path: 'reports-dashboard',
        canActivate: [UserRouteAccessService],
        component: ReportsDashboardComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.dashboard,
          lablesArray: ['Reports', 'Reports Dashboard']
        },
        outlet: 'content'
      },
      {
        path: 'report-list',
        canActivate: [UserRouteAccessService],
        component: ReportsComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.reportsList,
          lablesArray: ['Setup', 'Reports', 'Reports List']
        },
        outlet: 'content'
      },
      {
        path: 'run-reports',
        canActivate: [UserRouteAccessService],
        component: RunReportsComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.reports,
          lablesArray: ['Process', 'Reports', 'Run Report']
        },
        outlet: 'content'
      },
      {
        path: ':id/run-reports',
        canActivate: [UserRouteAccessService],
        component: RunReportsComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.reports,
          lablesArray: ['Setup', 'Reports', 'Run Report']
        },
        outlet: 'content'
      },
      {
        path: 'run-reports/:reqId/:id',
        canActivate: [UserRouteAccessService],
        component: RunReportsComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.reports,
          lablesArray: ['Setup', 'Reports', 'Report By Request']
        },
        outlet: 'content'
      },
      {
        path: 'new-report',
        canActivate: [UserRouteAccessService],
        component: ReportsDetailComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.createReport,
          lablesArray: ['Setup', 'Reports', 'Create Report']
        },
        outlet: 'content'
      },
      {
        path: 'new-report/:type',
        canActivate: [UserRouteAccessService],
        component: ReportsDetailComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.createReport,
          lablesArray: ['Setup', 'Reports', 'Create Report']
        },
        outlet: 'content'
      },
      {
        path: ':id/details',
        canActivate: [UserRouteAccessService],
        component: ReportsDetailComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.showReport,
          lablesArray: ['Setup', 'Reports', 'Report Definition']
        },
        outlet: 'content'
      },
      {
        path: ':id/edit',
        canActivate: [UserRouteAccessService],
        component: ReportsDetailComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.editReport,
          lablesArray: ['Setup', 'Reports', 'Edit Report Definition']
        },
        outlet: 'content'
      },
      {
        path: 'aging-bucket-list',
        canActivate: [UserRouteAccessService],
        component: BucketListComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.bucketList,
          lablesArray: ['Setup', 'Reports', 'Bucket List']
        },
        outlet: 'content'
      },
      {
        path: 'aging-bucket-detail/:id/:mode',
        canActivate: [UserRouteAccessService],
        component: BucketDetailComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.bucketList,
          lablesArray: ['Setup', 'Reports', 'Bucket Definition']
        },
        outlet: 'content'
      },
      {
        path: 'aging-bucket-detail/:mode',
        canActivate: [UserRouteAccessService],
        component: BucketDetailComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reports.home.title',
          breadcrumb: ReportsBreadCrumbTitles.bucketList,
          lablesArray: ['Setup', 'Reports', 'Bucket Definition']
        },
        outlet: 'content'
      }
    ]
  }
];
