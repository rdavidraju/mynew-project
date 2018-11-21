import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FileTemplatesComponent } from './file-templates.component';
//import { FileTemplatesDetailComponent } from './file-templates-detail.component';
//import { FileTemplatesPopupComponent } from './file-templates-dialog.component';
//import { FileTemplatesDeletePopupComponent } from './file-templates-delete-dialog.component';
import { FileTemplatesEditComponent } from './file-templates-edit.component';
import { FileTemplatesHomeComponent } from './file-templates-home.component';
import { Principal } from '../../shared';
import { FileTemplateBreadCrumbTitles } from './file-templates.model';
@Injectable()
export class FileTemplatesResolvePagingParams implements Resolve<any> {

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

export const fileTemplatesRoute: Routes = [
     {
        path: 'file-templates',
        component: FileTemplatesHomeComponent,
        data: {
            authorities: ['FILE_TEMPLATES_LIST'],
            pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
        },
        children: [
            {
                path: 'file-templates-home',
                component: FileTemplatesComponent,
                resolve: {
                    'pagingParams': FileTemplatesResolvePagingParams
                },
                data: {
                    authorities: ['FILE_TEMPLATES_LIST'],
                    pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
                    breadcrumb: FileTemplateBreadCrumbTitles.fileTemplates,
                    lablesArray: ['Setups','ETL','File Templates List']
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: FileTemplatesEditComponent,
                data: {
                    authorities: ['FILE_TEMPLATE_VIEW'],
                    pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
                    breadcrumb: FileTemplateBreadCrumbTitles.fileTemplatesDetails,
                    lablesArray: ['Setups','ETL','File Templates Details']
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: 'filetemplate-new',
                component: FileTemplatesEditComponent,
                data: {
                    authorities: ['FILE_TEMPLATE_CREATE'],
                    pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
                    breadcrumb: FileTemplateBreadCrumbTitles.fileTemplatesNew,
                    lablesArray: ['Setups','ETL','New File Template']
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: FileTemplatesEditComponent,
                data: {
                    authorities: ['FILE_TEMPLATE_EDIT'],
                    pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
                    breadcrumb: FileTemplateBreadCrumbTitles.fileTemplatesEdit,
                    lablesArray: ['Setups','ETL','Edit File Template']
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            },
            {
                path: ':id/copyedit',
                component: FileTemplatesEditComponent,
                data: {
                    authorities: ['FILE_TEMPLATE_COPY'],
                    pageTitle: 'agreeGatewayV1App.fileTemplates.home.title',
                    breadcrumb: FileTemplateBreadCrumbTitles.fileTemplatesCopy,
                    lablesArray: ['Setups','ETL','Copy File Template']
                },
                canActivate: [UserRouteAccessService],
                outlet: 'content'
            }
        ]
    }

];