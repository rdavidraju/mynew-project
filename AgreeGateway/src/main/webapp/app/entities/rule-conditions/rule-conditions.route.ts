import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ReportsBreadCrumbTitles } from './rule-conditions.model';
import { RuleConditionsComponent } from './rule-conditions.component';
//import { ReportsHomeComponent } from './reports-home.component';
   

export const ruleConditionsRoute: Routes = [
    // {
    //     path: 'reports123',
    //     component: ReportsHomeComponent,
    //     data: {
    //       authorities: ['ROLE_USER'],
    //       pageTitle: 'Reports'
    //     },
    //     children: [
    //       {
    //         path: 'run-reports',
    //         component: RuleConditionsComponent,
    //         data: {
    //           authorities: ['ROLE_USER'],
    //           pageTitle: 'agreeGatewayApp.sourceProfiles.home.title',
    //           breadcrumb: ReportsBreadCrumbTitles.reports
    //         },
    //         outlet: 'content' 
    //       }
    //     ]
    //   }
];
