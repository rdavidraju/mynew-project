import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RulesComponent } from './rules.component';
import { RulesDetailComponent } from './rules-detail.component';
import { RulesPopupComponent } from './rules-dialog.component';
import { RulesDeletePopupComponent } from './rules-delete-dialog.component';

import { Principal } from '../../shared';


export const rulesRoute: Routes = [
  {
    path: 'rules',
    component: RulesComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayApp.rules.home.title'
    }
  }, {
    path: 'rules/:id',
    component: RulesDetailComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayApp.rules.home.title'
    }
  }
];

export const rulesPopupRoute: Routes = [
  {
    path: 'rules-new',
    component: RulesPopupComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayApp.rules.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'rules/:id/edit',
    component: RulesPopupComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayApp.rules.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'rules/:id/delete',
    component: RulesDeletePopupComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayApp.rules.home.title'
    },
    outlet: 'popup'
  }
];
