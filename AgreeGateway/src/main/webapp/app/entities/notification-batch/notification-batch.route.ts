import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NotificationBatchComponent } from './notification-batch.component';
import { NotificationBatchDetailComponent } from './notification-batch-detail.component';
import { NotificationBatchPopupComponent } from './notification-batch-dialog.component';
import { NotificationBatchDeletePopupComponent } from './notification-batch-delete-dialog.component';
import { NotificationBatch, NotificationBreadCrumbTitles } from './notification-batch.model';
import { NotificationBatchHomeComponent } from './notification-batch-home.component';
import { Principal } from '../../shared';
/* 
@Injectable()
export class UserResolve implements CanActivate {

    constructor(private principal: Principal) { }

    canActivate() {
        return this.principal.identity().then((account) => this.principal.hasAnyAuthority(['ROLE_ADMIN']));
    }
} */

@Injectable()
export class NotificationBatchResolvePagingParams implements Resolve<any> {

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

export const notificationBatchRoute: Routes = [
    {
        path: 'notification-batch',
        component: NotificationBatchHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.notificationBatch.home.title'
        },
        children: [
            {
                path: 'notification-batch-home',
                component: NotificationBatchComponent,
                resolve: {
                    'pagingParams': NotificationBatchResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.notificationBatch.home.title',
                    breadcrumb: NotificationBreadCrumbTitles.notificationList,
                    lablesArray: ['Process','Approvals']
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: NotificationBatchDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.notificationBatch.home.title',
                    breadcrumb: NotificationBreadCrumbTitles.notificationDetails,
                    lablesArray: ['Process','Approvals']
                },
                outlet: 'content'
            },
            {
                path: 'notification-batch-new',
                component: NotificationBatchDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.notificationBatch.home.title',
                    breadcrumb: NotificationBreadCrumbTitles.notificationNew,
                    lablesArray: ['Process','Approvals']
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: NotificationBatchDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.notificationBatch.home.title',
                    breadcrumb: NotificationBreadCrumbTitles.notificationEdit,
                    lablesArray: ['Process','Approvals']
                },
                outlet: 'content'
            }
        ]
    }
];

// export const notificationBatchRoute: Routes = [
//     {
//         path: 'notification-batch',
//         component: NotificationBatchComponent,
//         resolve: {
//             'pagingParams': NotificationBatchResolvePagingParams
//         },
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.notificationBatch.home.title'
//         },
//         canActivate: [UserRouteAccessService]
//     }, {
//         path: 'notification-batch/:id',
//         component: NotificationBatchDetailComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.notificationBatch.home.title'
//         },
//         canActivate: [UserRouteAccessService]
//     }
// ];

// export const notificationBatchPopupRoute: Routes = [
//     {
//         path: 'notification-batch-new',
//         component: NotificationBatchPopupComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.notificationBatch.home.title'
//         },
//         canActivate: [UserRouteAccessService],
//         outlet: 'popup'
//     },
//     {
//         path: 'notification-batch/:id/edit',
//         component: NotificationBatchPopupComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.notificationBatch.home.title'
//         },
//         canActivate: [UserRouteAccessService],
//         outlet: 'popup'
//     },
//     {
//         path: 'notification-batch/:id/delete',
//         component: NotificationBatchDeletePopupComponent,
//         data: {
//             authorities: ['ROLE_USER'],
//             pageTitle: 'agreeGatewayV1App.notificationBatch.home.title'
//         },
//         canActivate: [UserRouteAccessService],
//         outlet: 'popup'
//     }
// ];
