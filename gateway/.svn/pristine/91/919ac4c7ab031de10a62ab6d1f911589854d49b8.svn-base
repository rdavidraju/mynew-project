import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SourceProfilesComponent } from './source-profiles.component';
import { SourceProfilesDetailComponent } from './source-profiles-detail.component';
import { SourceProfilesPopupComponent } from './source-profiles-dialog.component';
//import { SourceProfilesDeletePopupComponent } from './source-profiles-delete-dialog.component';
import { SourceProfilesBreadCrumbTitles } from './source-profiles.model';
import { SourceProfilesEditComponent } from './source-profiles-edit.component';
import { SourceProfilesHomeComponent } from './source-profiles-home.component';

@Injectable()
export class SourceProfilesResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: JhiPaginationUtil) { }

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

export const sourceProfilesRoute: Routes = [
  {
    path: 'source-profiles',
    component: SourceProfilesHomeComponent,
    data: {
      authorities: ['SRC_PROFILES_LIST'],
      pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title'
    },
    children: [
      {
        path: 'source-profiles-list',
        component: SourceProfilesComponent,
        resolve: {
          'pagingParams': SourceProfilesResolvePagingParams
        },
        data: {
          authorities: ['SRC_PROFILES_LIST'],
          pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title',
          breadcrumb: SourceProfilesBreadCrumbTitles.sourceProfiles,
          lablesArray: ['Setups','ETL',SourceProfilesBreadCrumbTitles.sourceProfiles]
        },
        canActivate: [UserRouteAccessService],
        outlet: 'content'
      },
      {
        path: ':id/details',
        component: SourceProfilesEditComponent,
        data: {
          authorities: ['SRC_PROFILE_VIEW'],
          pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title',
          breadcrumb: SourceProfilesBreadCrumbTitles.sourceProfileDetails,
          lablesArray: ['Setups','ETL',SourceProfilesBreadCrumbTitles.sourceProfileDetails]
        },
        canActivate: [UserRouteAccessService],
        outlet: 'content'
      },

      {
        path: ':id/new',
        component: SourceProfilesEditComponent,
        data: {
          authorities: ['SRC_PROFILE_CREATE	'],
          pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title',
          breadcrumb: SourceProfilesBreadCrumbTitles.sourceProfileNew,
          lablesArray: ['Setups','ETL',SourceProfilesBreadCrumbTitles.sourceProfileNew]
        },
        canActivate: [UserRouteAccessService],
        outlet: 'content'
      },
      {
        path: 'source-profiles-new',
        component: SourceProfilesEditComponent,
        data: {
          authorities: ['SRC_PROFILE_CREATE'],
          pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title',
          breadcrumb: SourceProfilesBreadCrumbTitles.sourceProfileNew,
          lablesArray: ['Setups','ETL',SourceProfilesBreadCrumbTitles.sourceProfileNew]
        },
        canActivate: [UserRouteAccessService],
        outlet: 'content'
      },

      {
        path: ':id/edit',
        component: SourceProfilesEditComponent,
        data: {
          authorities: ['SRC_PROFILE_EDIT'],
          pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title',
          breadcrumb: SourceProfilesBreadCrumbTitles.sourceProfileEdit,
          lablesArray: ['Setups','ETL',SourceProfilesBreadCrumbTitles.sourceProfileEdit]
        },
        canActivate: [UserRouteAccessService],
        outlet: 'content'
      },
      {
        path: ':id/copyedit',
        component: SourceProfilesEditComponent,
        data: {
            authorities: ['SRC_PROFILE_COPY'],
            pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
            breadcrumb: SourceProfilesBreadCrumbTitles.sourceProfileCopy,
            lablesArray: ['Setups','ETL',SourceProfilesBreadCrumbTitles.sourceProfileCopy]
        },
        canActivate: [UserRouteAccessService],
        outlet: 'content'
    }
    ]
  }
];
