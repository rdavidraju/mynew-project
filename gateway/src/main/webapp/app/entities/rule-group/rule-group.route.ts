import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { RuleGroupBreadCrumbTitles } from './rule-group.model';
import { RuleGroupComponent } from './rule-group.component';
import { RuleGroupDetailComponent } from './rule-group-detail.component';
import { RuleGroupPopupComponent } from './rule-group-dialog.component';
import { RuleGroupDeletePopupComponent } from './rule-group-accounting.component';
import { RuleGroupHomeComponent } from './rule-group-home.component';
import {RuleGroupEditComponent } from './rule-group-edit.component';
import { Principal } from '../../shared';

@Injectable()
export class RuleGroupResolvePagingParams implements Resolve<any> {

    constructor( private paginationUtil: JhiPaginationUtil ) { }

    resolve( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
        let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage( page ),
            predicate: this.paginationUtil.parsePredicate( sort ),
            ascending: this.paginationUtil.parseAscending( sort )
        };
    }
}

export const ruleGroupRoute: Routes = [
  {
    path: 'rule-group',
    component: RuleGroupHomeComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'agreeGatewayV1App.ruleGroup.home.title'
    },
    children: [
            {
                path: 'rule-group-home',
                component: RuleGroupComponent,
                canActivate: [UserRouteAccessService],
                resolve: {
                    'pagingParams': RuleGroupResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroups,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroups]
                },
                outlet: 'content'
            },
            {
                path: ':rulePurpose/list',
                component: RuleGroupComponent,
                canActivate: [UserRouteAccessService],
                resolve: {
                    'pagingParams': RuleGroupResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroups,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroups]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: RuleGroupEditComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroupDetails,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroupDetails]
                },
                outlet: 'content'
            },
            {
                path: ':id/ruleDetails',
                component: RuleGroupEditComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroupDetails,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroupDetails]
                },
                outlet: 'content'
            },
            {
                path: 'rulegroup-new',
                component: RuleGroupEditComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroupNew,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroupNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: RuleGroupEditComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroupEdit,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroupEdit]
                },
                outlet: 'content'
            },
            {
                path: ':id/copyedit',
                component: RuleGroupEditComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ruleGroup.home.title',
                    breadcrumb: RuleGroupBreadCrumbTitles.ruleGroupCopy,
                    lablesArray: ['Setups','ETL','Process',RuleGroupBreadCrumbTitles.ruleGroupCopy]
                },
                outlet: 'content'
            }
        ]
  }
];
