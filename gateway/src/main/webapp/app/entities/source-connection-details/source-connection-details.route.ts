import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SourceConnectionDetailsComponent } from './source-connection-details.component';
import { SourceConnectionDetailsDetailComponent } from './source-connection-details-detail.component';
import { SourceConnectionDetailsPopupComponent } from './source-connection-details-dialog.component';
import { SourceConnectionDetailsDeletePopupComponent } from './source-connection-details-delete-dialog.component';
import { SourceConnectionDetailsHomeComponent } from './source-connection-details-home.component';
import { SourceConnectionsBreadCrumbTitles } from './source-connection-details.model';
import { Principal } from '../../shared';
@Injectable()
export class SourceConnectionDetailsResolvePagingParams implements Resolve<any> {

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

export const sourceConnectionDetailsRoute: Routes = [
    {
        path: 'source-connection-details',
        component: SourceConnectionDetailsHomeComponent,
        data: {
            authorities: ['SRC_CONNECTIONS_LIST'],
            pageTitle: 'agreeGatewayV1App.sourceProfiles.home.title'
        },
        children: [
            {
                path: 'source-connection-detailst',
                component: SourceConnectionDetailsComponent,
                resolve: {
                    'pagingParams': SourceConnectionDetailsResolvePagingParams
                },
                data: {
                    authorities: ['SRC_CONNECTIONS_LIST'],
                    pageTitle: 'agreeGatewayV1App.sourceConnectionDetails.home.title',
                    breadcrumb: SourceConnectionsBreadCrumbTitles.sourceConnections,
                    lablesArray: ['Setups','ETL',SourceConnectionsBreadCrumbTitles.sourceConnections]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: SourceConnectionDetailsDetailComponent,
                data: {
                    authorities: ['SRC_CONNECTION_VIEW'],
                    pageTitle: 'agreeGatewayV1App.sourceConnectionDetails.home.title',
                    breadcrumb: SourceConnectionsBreadCrumbTitles.sourceConnectionDetails,
                    lablesArray: ['Setups','ETL',SourceConnectionsBreadCrumbTitles.sourceConnectionDetails]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'source-connection-details-new',
                component: SourceConnectionDetailsDetailComponent,
                data: {
                    authorities: ['SRC_CONNECTION_CREATE'],
                    pageTitle: 'agreeGatewayV1App.sourceConnectionDetails.home.title',
                    breadcrumb: SourceConnectionsBreadCrumbTitles.sourceConnectionNew,
                    lablesArray: ['Setups','ETL',SourceConnectionsBreadCrumbTitles.sourceConnectionNew]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: SourceConnectionDetailsDetailComponent,
                data: {
                    authorities: ['SRC_CONNECTION_EDIT'],
                    pageTitle: 'agreeGatewayV1App.sourceConnectionDetails.home.title',
                    breadcrumb: SourceConnectionsBreadCrumbTitles.sourceConnectionEdit,
                    lablesArray: ['Setups','ETL',SourceConnectionsBreadCrumbTitles.sourceConnectionEdit]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/copyedit',
                component: SourceConnectionDetailsDetailComponent,
                data: {
                    authorities: ['SRC_CONNECTION_COPY'],
                    pageTitle: 'agreeGatewayV1App.sourceConnectionDetails.home.title',
                    breadcrumb: SourceConnectionsBreadCrumbTitles.sourceConnectionCopy,
                    lablesArray: ['Setups','ETL',SourceConnectionsBreadCrumbTitles.sourceConnectionCopy]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }
];
