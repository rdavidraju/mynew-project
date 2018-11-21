import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AccountingDataComponent } from './accounting-data.component';
import { AccountingDataWqComponent } from './accounting-data-wq.component';
// import { AccountingDataDetailComponent } from './accounting-data-detail.component';
// import { AccountingDataPopupComponent } from './accounting-data-dialog.component';
// import { AccountingDataDeletePopupComponent } from './accounting-data-delete-dialog.component';
import { AccountingDataHomeComponent } from './accounting-data-home.component';
import { AccountingDataWqHomeComponent } from './accounting-data-wq-home';
import { AccountingDataBreadCrumbTitles } from './accounting-data.model';

@Injectable()
export class AccountingDataResolvePagingParams implements Resolve<any> {

    constructor( private paginationUtil: JhiPaginationUtil ) { }

    resolve( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage( page ),
            predicate: this.paginationUtil.parsePredicate( sort ),
            ascending: this.paginationUtil.parseAscending( sort )
        };
    }
}

export const accountingDataRoute: Routes = [

    {
        path: 'accounting-data',
        component: AccountingDataHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingData.home.title'
        },
        children: [
            {
                path: 'accounting-data-home',
                component: AccountingDataComponent,
                resolve: {
                    'pagingParams': AccountingDataResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.accountingData.home.title',
                    breadcrumb: AccountingDataBreadCrumbTitles.accountingData,
                    lablesArray: ['Process','Accounting Work Queue']
                },
                outlet: 'content'
            }

        ]
    },

    {
        path: 'accounting-data-wq',
        component: AccountingDataWqHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.accountingData.home.title'
        },
        children: [
            {
                path: 'accounting-data-wq-home',
                component: AccountingDataWqComponent,
                resolve: {
                    'pagingParams': AccountingDataResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.accountingData.home.title',
                    breadcrumb: AccountingDataBreadCrumbTitles.accountingData,
                    lablesArray: ['Process','Accounting Work Queue']
                },
                outlet: 'content'
            }
        ]
    }

]








  /* */