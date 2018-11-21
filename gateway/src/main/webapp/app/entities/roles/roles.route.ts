import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { RolesComponent } from './roles.component';
import { RolesDetailComponent } from './roles-detail.component';
import { RolesBreadCrumbTitles } from './roles.model';
import { RolesHomeComponent } from './roles-home.component';

export const rolesRoute: Routes = [
    {
        path: 'roles',
        component: RolesHomeComponent,
        data: {
            authorities: ['ROLES_LIST'],
            pageTitle: 'agreeGatewayV1App.roles.home.title'
        },
        canActivate: [UserRouteAccessService],
        children: [
            {
                path: 'roles-home',
                component: RolesComponent,
                data: {
                    authorities: ['ROLES_LIST'],
                    pageTitle: 'agreeGatewayV1App.roles.home.title',
                    breadcrumb: RolesBreadCrumbTitles.rolesList,
                    lablesArray: ['Admin','User Management',RolesBreadCrumbTitles.rolesList]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: RolesDetailComponent,
                data: {
                    authorities: ['ROLE_VIEW'],
                    pageTitle: 'agreeGatewayV1App.roles.home.title',
                    breadcrumb: RolesBreadCrumbTitles.rolesDetails,
                    lablesArray: ['Admin','User Management',RolesBreadCrumbTitles.rolesDetails]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'roles-new',
                component: RolesDetailComponent,
                data: {
                    authorities: ['ROLE_CREATE'],
                    pageTitle: 'agreeGatewayV1App.roles.home.title',
                    breadcrumb: RolesBreadCrumbTitles.rolesNew,
                    lablesArray: ['Admin','User Management',RolesBreadCrumbTitles.rolesNew]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: RolesDetailComponent,
                data: {
                    authorities: ['ROLE_EDIT'],
                    pageTitle: 'agreeGatewayV1App.roles.home.title',
                    breadcrumb: RolesBreadCrumbTitles.rolesEdit,
                    lablesArray: ['Admin','User Management',RolesBreadCrumbTitles.rolesEdit]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }
];