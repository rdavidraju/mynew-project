import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReconcileComponent } from './reconcile.component';
import { RWQComponent} from './rwq.component';
import { ReconcileBreadCrumbTitles } from './reconcile.model';
import { ReconcileHomeComponent } from './reconcile.home';
import { RWQHomeComponent } from './rwq.home';
import { Principal } from '../../shared';

@Injectable()
export class ReconcileResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const reconcileRoute: Routes = [
{
    path: 'reconcile',
    component: ReconcileHomeComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayV1App.reconcile.home.title'
    },
    children: [
      {
       path: 'reconcile-list',
       component: ReconcileComponent,
       canActivate: [UserRouteAccessService],
       resolve: {
        'pagingParams': ReconcileResolvePagingParams
      },
       data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reconcile.home.title',
          breadcrumb: ReconcileBreadCrumbTitles.reconcile,
          lablesArray: ['Process','Reconciliation Work Queue']
       },
      outlet: 'content'
      }
    ]
  },
{
    path: 'reconcilewq',
    component: RWQHomeComponent,
    canActivate: [UserRouteAccessService],
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayV1App.reconcile.home.title'
    },
    children: [
      {
       path: 'reconcile-details',
       canActivate: [UserRouteAccessService],
       component: RWQComponent,
       resolve: {
        'pagingParams': ReconcileResolvePagingParams
      },
       data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'agreeGatewayV1App.reconcile.home.title',
          breadcrumb: ReconcileBreadCrumbTitles.reconcilewq,
          lablesArray: ['Process','Reconciliation Work Queue']
       },
      outlet: 'content'
      }
    ]
  }                                     
];