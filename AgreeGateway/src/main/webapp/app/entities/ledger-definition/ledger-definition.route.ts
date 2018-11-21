import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';
import { LedgerDefinitionComponent } from './ledger-definition.component';
import { LedgerDefinitionDetailComponent } from './ledger-definition-detail.component';
import { LedgerDefinitionPopupComponent } from './ledger-definition-dialog.component';
import { LedgerDefinitionDeletePopupComponent } from './ledger-definition-delete-dialog.component';
import { LedgerDefinitionHomeComponent } from './ledger-definition-home.component';
import { LedgerDefinition,LedgerDefinitionBreadCrumbTitles } from './ledger-definition.model';

@Injectable()
export class LedgerDefinitionResolvePagingParams implements Resolve<any> {

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

export const LedgerDefinitionRoute: Routes = [
    {
        path: 'ledger-definition',
        component: LedgerDefinitionHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title'
        },
        children: [
            {
                path: 'ledger-definition-home',
                component: LedgerDefinitionComponent,
                resolve: {
                    'pagingParams': LedgerDefinitionResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title',
                    breadcrumb: LedgerDefinitionBreadCrumbTitles.ledgerDefinitionList,
                    lablesArray: ['Setups','Master Setups',LedgerDefinitionBreadCrumbTitles.ledgerDefinitionList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: LedgerDefinitionDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title',
                    breadcrumb: LedgerDefinitionBreadCrumbTitles.ledgerDefinitionDetails,
                    lablesArray: ['Setups','Master Setups',LedgerDefinitionBreadCrumbTitles.ledgerDefinitionDetails]
                },
                outlet: 'content'
            },
            {
                path: 'ledger-definition-new',
                component: LedgerDefinitionDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title',
                    breadcrumb: LedgerDefinitionBreadCrumbTitles.ledgerDefinitionNew,
                    lablesArray: ['Setups','Master Setups',LedgerDefinitionBreadCrumbTitles.ledgerDefinitionNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: LedgerDefinitionDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title',
                    breadcrumb: LedgerDefinitionBreadCrumbTitles.ledgerDefinitionEdit,
                    lablesArray: ['Setups','Master Setups',LedgerDefinitionBreadCrumbTitles.ledgerDefinitionEdit]
                },
                outlet: 'content'
            }
        ]
    }
];



/* export const ledgerDefinitionRoute: Routes = [
    {
        path: 'ledger-definition',
        component: LedgerDefinitionComponent,
        resolve: {
            'pagingParams': LedgerDefinitionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ledger-definition/:id',
        component: LedgerDefinitionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ledgerDefinitionPopupRoute: Routes = [
    {
        path: 'ledger-definition-new',
        component: LedgerDefinitionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ledger-definition/:id/edit',
        component: LedgerDefinitionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ledger-definition/:id/delete',
        component: LedgerDefinitionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.ledgerDefinition.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
]; */
