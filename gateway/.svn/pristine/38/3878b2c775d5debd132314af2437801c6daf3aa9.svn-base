import { Routes } from '@angular/router';
import { MappingSetComponent } from './mapping-set.component';
import { MappingSetDetailComponent } from './mapping-set-detail.component';
import { MappingSetBreadCrumbTitles } from './mapping-set.model';
import { MappingSetHomeComponent } from './mapping-set-home.component';

export const mappingSetRoute: Routes = [
    {
        path: 'mapping-set',
        component: MappingSetHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.mappingSet.home.title'
        },
        children: [
            {
                path: 'mapping-set-home',
                component: MappingSetComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetList,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: MappingSetDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetDetails,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetDetails]
                },
                outlet: 'content'
            },
            {
                path: 'mapping-set-new',
                component: MappingSetDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetNew,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: MappingSetDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.mappingSet.home.title',
                    breadcrumb: MappingSetBreadCrumbTitles.mappingSetEdit,
                    lablesArray: ['Setups','Accounting',MappingSetBreadCrumbTitles.mappingSetEdit]
                },
                outlet: 'content'
            }
        ]
    }
];