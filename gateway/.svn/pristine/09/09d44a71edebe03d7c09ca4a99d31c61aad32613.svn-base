import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { DataViewsComponent } from './data-views.component';
import { DataViewsDetailComponent } from './data-views-detail.component';
import { DataViewHomeComponent } from './data-views-home.component';
import { DataViewsBreadCrumbTitles } from './data-views.model';

export const dataViewsRoute: Routes = [
    {
        path: 'data-views',
        component: DataViewHomeComponent,
        data: {
            authorities: ['DATA_SRC_LIST'],
            pageTitle: 'agreeGatewayV1App.dataViews.home.title'
        },
        children: [
            {
                path: 'data-views-home',
                component: DataViewsComponent,
                data: {
                    authorities: ['DATA_SRC_LIST'],
                    pageTitle: 'agreeGatewayV1App.dataViews.home.title',
                    breadcrumb: DataViewsBreadCrumbTitles.dataViews,
                    lablesArray: ['Setups','ETL',DataViewsBreadCrumbTitles.dataViews]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':relation/list',
                component: DataViewsComponent,
                data: {
                    authorities: ['DATA_SRC_LIST'],
                    pageTitle: 'agreeGatewayV1App.dataViews.home.title',
                    breadcrumb: DataViewsBreadCrumbTitles.dataViews,
                    lablesArray: ['Setups','ETL',DataViewsBreadCrumbTitles.dataViews]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: DataViewsDetailComponent,
                data: {
                    authorities: ['DATA_SRC_VIEW'],
                    pageTitle: 'agreeGatewayV1App.dataViews.home.title',
                    breadcrumb: DataViewsBreadCrumbTitles.dataViewDetails,
                    lablesArray: ['Setups','ETL',DataViewsBreadCrumbTitles.dataViewDetails]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'data-views-new',
                component: DataViewsDetailComponent,
                data: {
                    authorities: ['DATA_SRC_CREATE'],
                    pageTitle: 'agreeGatewayV1App.dataViews.home.title',
                    breadcrumb: DataViewsBreadCrumbTitles.dataViewNew,
                    lablesArray: ['Setups','ETL',DataViewsBreadCrumbTitles.dataViewNew]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: DataViewsDetailComponent,
                data: {
                    authorities: ['DATA_SRC_EDIT'],
                    pageTitle: 'agreeGatewayV1App.dataViews.home.title',
                    breadcrumb: DataViewsBreadCrumbTitles.dataViewEdit,
                    lablesArray: ['Setups','ETL',DataViewsBreadCrumbTitles.dataViewEdit]
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }
];

