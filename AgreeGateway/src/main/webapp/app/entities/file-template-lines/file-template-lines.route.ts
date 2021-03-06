import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

//import { FileTemplateLinesComponent } from './file-template-lines.component';
//import { FileTemplateLinesDetailComponent } from './file-template-lines-detail.component';
//import { FileTemplateLinesPopupComponent } from './file-template-lines-dialog.component';
//import { FileTemplateLinesDeletePopupComponent } from './file-template-lines-delete-dialog.component';

@Injectable()
export class FileTemplateLinesResolvePagingParams implements Resolve<any> {

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

export const fileTemplateLinesRoute: Routes = [
    // {
    //     path: 'file-template-lines',
    //     component: FileTemplateLinesComponent,
    //     resolve: {
    //         'pagingParams': FileTemplateLinesResolvePagingParams
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.fileTemplateLines.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }, {
    //     path: 'file-template-lines/:id',
    //     component: FileTemplateLinesDetailComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.fileTemplateLines.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }
];

export const fileTemplateLinesPopupRoute: Routes = [
    // {
    //     path: 'file-template-lines-new',
    //     component: FileTemplateLinesPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.fileTemplateLines.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'file-template-lines/:id/edit',
    //     component: FileTemplateLinesPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.fileTemplateLines.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    // {
    //     path: 'file-template-lines/:id/delete',
    //     component: FileTemplateLinesDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'agreeGatewayV1App.fileTemplateLines.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
