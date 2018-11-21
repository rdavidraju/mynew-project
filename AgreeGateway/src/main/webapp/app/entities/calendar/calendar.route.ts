import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CalendarComponent } from './calendar.component';
import { CalendarDetailComponent } from './calendar-detail.component';
import { CalendarPopupComponent } from './calendar-dialog.component';
import { CalendarDeletePopupComponent } from './calendar-delete-dialog.component';
import { CalendarHomeComponent } from './calendar-home.component';
import { Calendar,CalendarBreadCrumbTitles } from './calendar.model';


@Injectable()
export class CalendarResolvePagingParams implements Resolve<any> {

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

export const CalendarRoute: Routes = [
    {
        path: 'calendar',
        component: CalendarHomeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.calendar.home.title'
        },
        children: [
            {
                path: 'calendar-home',
                component: CalendarComponent,
                resolve: {
                    'pagingParams': CalendarResolvePagingParams
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.calendar.home.title',
                    breadcrumb: CalendarBreadCrumbTitles.calendarList,
                    lablesArray: ['Setups','Master Setups',CalendarBreadCrumbTitles.calendarList]
                },
                outlet: 'content'
            },
            {
                path: ':id/details',
                component: CalendarDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.calendar.home.title',
                    breadcrumb: CalendarBreadCrumbTitles.calendarDetails,
                    lablesArray: ['Setups','Master Setups',CalendarBreadCrumbTitles.calendarDetails]
                },
                outlet: 'content'
            },
            {
                path: 'calendar-new',
                component: CalendarDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.calendar.home.title',
                    breadcrumb: CalendarBreadCrumbTitles.calendarNew,
                    lablesArray: ['Setups','Master Setups',CalendarBreadCrumbTitles.calendarNew]
                },
                outlet: 'content'
            },
            {
                path: ':id/edit',
                component: CalendarDetailComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'agreeGatewayV1App.calendar.home.title',
                    breadcrumb: CalendarBreadCrumbTitles.calendarEdit,
                    lablesArray: ['Setups','Master Setups',CalendarBreadCrumbTitles.calendarEdit]
                },
                outlet: 'content'
            }
        ]
    }
];

/* export const calendarRoute: Routes = [
    {
        path: 'calendar',
        component: CalendarComponent,
        resolve: {
            'pagingParams': CalendarResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.calendar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'calendar/:id',
        component: CalendarDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.calendar.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const calendarPopupRoute: Routes = [
    {
        path: 'calendar-new',
        component: CalendarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.calendar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'calendar/:id/edit',
        component: CalendarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.calendar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'calendar/:id/delete',
        component: CalendarDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agreeGatewayV1App.calendar.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
 */