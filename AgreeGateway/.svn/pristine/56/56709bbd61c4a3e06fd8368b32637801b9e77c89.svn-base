import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TemplAttributeMappingComponent } from './templ-attribute-mapping.component';
import { TemplAttributeMappingDetailComponent } from './templ-attribute-mapping-detail.component';
//import { TemplAttributeMappingPopupComponent } from './templ-attribute-mapping-dialog.component';
//import { TemplAttributeMappingDeletePopupComponent } from './templ-attribute-mapping-delete-dialog.component';

@Injectable()
export class TemplAttributeMappingResolvePagingParams implements Resolve<any> {

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

export const templAttributeMappingRoute: Routes = [
    {
        path: 'templ-attribute-mapping',
        component: TemplAttributeMappingComponent,
        resolve: {
            'pagingParams': TemplAttributeMappingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templAttributeMapping.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'templ-attribute-mapping/:id',
        component: TemplAttributeMappingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.templAttributeMapping.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const templAttributeMappingPopupRoute: Routes = [
    // {
    //     path: 'templ-attribute-mapping-new',
    //     component: TemplAttributeMappingPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.templAttributeMapping.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'templ-attribute-mapping/:id/edit',
    //     component: TemplAttributeMappingPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.templAttributeMapping.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'templ-attribute-mapping/:id/delete',
    //     component: TemplAttributeMappingDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.templAttributeMapping.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
