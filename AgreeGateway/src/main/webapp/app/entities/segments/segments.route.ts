import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SegmentsComponent } from './segments.component';
import { SegmentsDetailComponent } from './segments-detail.component';
import { SegmentsPopupComponent } from './segments-dialog.component';
import { SegmentsDeletePopupComponent } from './segments-delete-dialog.component';

@Injectable()
export class SegmentsResolvePagingParams implements Resolve<any> {

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

export const segmentsRoute: Routes = [
    {
        path: 'segments',
        component: SegmentsComponent,
        resolve: {
            'pagingParams': SegmentsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.segments.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'segments/:id',
        component: SegmentsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.segments.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const segmentsPopupRoute: Routes = [
    {
        path: 'segments-new',
        component: SegmentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.segments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'segments/:id/edit',
        component: SegmentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.segments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'segments/:id/delete',
        component: SegmentsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.segments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
