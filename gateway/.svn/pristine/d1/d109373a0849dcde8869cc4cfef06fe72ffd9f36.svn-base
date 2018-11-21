import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { UserMgmtComponent } from './user-management.component';
import { UserMgmtDetailComponent } from './user-management-detail.component';
import { UserMgmtBreadCrumbTitles } from './user-management.model';
import { UserMgmtHomeComponent } from './user-management-home.component';

export const userMgmtRoute: Routes = [
    {
        path: 'user-management',
        component: UserMgmtHomeComponent,
        data: {
            authorities: ['USERS_LIST'],
            pageTitle: 'userManagement.home.title'
        },
        canActivate: [UserRouteAccessService],
        children: [
            {
                path: 'user-management-home',
                component: UserMgmtComponent,
                data: {
                    authorities: ['USERS_LIST'],
                    pageTitle: 'userManagement.home.title',
                    breadcrumb: UserMgmtBreadCrumbTitles.userMgmtList,
                    lablesArray: ['Admin', 'User Management', UserMgmtBreadCrumbTitles.userMgmtList]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: UserMgmtDetailComponent,
                data: {
                    authorities: ['USER_VIEW','DEFAULT_USER'],
                    pageTitle: 'userManagement.home.title',
                    breadcrumb: UserMgmtBreadCrumbTitles.userMgmtDetails,
                    lablesArray: ['Admin', 'User Management', UserMgmtBreadCrumbTitles.userMgmtDetails]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'user-management-new',
                component: UserMgmtDetailComponent,
                data: {
                    authorities: ['USER_CREATE'],
                    pageTitle: 'userManagement.home.title',
                    breadcrumb: UserMgmtBreadCrumbTitles.userMgmtNew,
                    lablesArray: ['Admin', 'User Management', UserMgmtBreadCrumbTitles.userMgmtNew]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: UserMgmtDetailComponent,
                data: {
                    authorities: ['USER_EDIT','DEFAULT_USER'],
                    pageTitle: 'userManagement.home.title',
                    breadcrumb: UserMgmtBreadCrumbTitles.userMgmtEdit,
                    lablesArray: ['Admin', 'User Management', UserMgmtBreadCrumbTitles.userMgmtEdit]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }
];