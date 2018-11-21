import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { ExpressModeComponent } from './';
import { FileDropComponent } from './file-drop.component';

export const reportsRoute: Routes = [
    {
        path: 'express-mode',
        component: ExpressModeComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'Reports',
          breadcrumb: 'Express Mode'
        },
        children: [
          {
            path: 'file-drop',
            component: FileDropComponent,
            data: {
              authorities: ['ROLE_USER'],
              pageTitle: 'agreeGatewayV1App.reports.home.title',
            },
            outlet: 'content' 
          }]
      }
];