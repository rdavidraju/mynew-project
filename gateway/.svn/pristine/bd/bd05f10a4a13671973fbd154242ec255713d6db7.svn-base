import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

//import { SourceProfileFileAssignmentsComponent } from './source-profile-file-assignments.component';
//import { SourceProfileFileAssignmentsDetailComponent } from './source-profile-file-assignments-detail.component';
//import { SourceProfileFileAssignmentsPopupComponent } from './source-profile-file-assignments-dialog.component';
// import {
//     SourceProfileFileAssignmentsDeletePopupComponent
// } from './source-profile-file-assignments-delete-dialog.component';

@Injectable()
export class SourceProfileFileAssignmentsResolvePagingParams implements Resolve<any> {

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

export const sourceProfileFileAssignmentsRoute: Routes = [
    // {
    //     path: 'source-profile-file-assignments',
    //     component: SourceProfileFileAssignmentsComponent,
    //     resolve: {
    //         'pagingParams': SourceProfileFileAssignmentsResolvePagingParams
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.sourceProfileFileAssignments.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }, {
    //     path: 'source-profile-file-assignments/:id',
    //     component: SourceProfileFileAssignmentsDetailComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.sourceProfileFileAssignments.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }
];

export const sourceProfileFileAssignmentsPopupRoute: Routes = [
    // {
    //     path: 'source-profile-file-assignments-new',
    //     component: SourceProfileFileAssignmentsPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.sourceProfileFileAssignments.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'source-profile-file-assignments/:id/edit',
    //     component: SourceProfileFileAssignmentsPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.sourceProfileFileAssignments.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'source-profile-file-assignments/:id/delete',
    //     component: SourceProfileFileAssignmentsDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.sourceProfileFileAssignments.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
