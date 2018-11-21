import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { CalendarComponent } from './calendar.component';
import { CalendarDetailComponent } from './calendar-detail.component';
import { CalendarHomeComponent } from './calendar-home.component';
import { CalendarBreadCrumbTitles } from './calendar.model';
import { UserRouteAccessService } from '../../shared';

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
                canActivate: [UserRouteAccessService],
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
                canActivate: [UserRouteAccessService],
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
                canActivate: [UserRouteAccessService],
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
                canActivate: [UserRouteAccessService],
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