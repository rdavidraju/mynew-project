import { Routes } from '@angular/router';
import { MappingSetComponent } from './mapping-set.component';
import { MappingSetDetailComponent } from './mapping-set-detail.component';
import { MappingSetBreadCrumbTitles } from './mapping-set.model';
import { MappingSetHomeComponent } from './mapping-set-home.component';
import { UserRouteAccessService } from '../../shared';

export const valueSetRoute: Routes = [
    {
        path: 'value-set',
        component: MappingSetHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
        },
        children: [
            {
                path: 'value-set-home',
                component: MappingSetComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetList,
                    lablesArray: ['Setups', 'Accounting', MappingSetBreadCrumbTitles.mappingSetList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: MappingSetDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetDetails,
                    lablesArray: ['Setups', 'Accounting', MappingSetBreadCrumbTitles.mappingSetDetails]
                },
                outlet: 'content'
            },
            {
                path: 'value-set-new',
                component: MappingSetDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetNew,
                    lablesArray: ['Setups', 'Accounting', MappingSetBreadCrumbTitles.mappingSetNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: MappingSetDetailComponent,
                canActivate: [UserRouteAccessService],
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetEdit,
                    lablesArray: ['Setups', 'Accounting', MappingSetBreadCrumbTitles.mappingSetEdit]
                },
                outlet: 'content'
            }
        ]
    }
];