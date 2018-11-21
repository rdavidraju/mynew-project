import { Route, Routes } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { HomeComponent } from './';
import { JhiLoginComponent } from '../shared/login'

export const HOME_ROUTE: Routes = [{
    path: '',
    component: HomeComponent,
    data: {
        authorities: [],
        pageTitle: 'home.title'
    }
},
{
    path: '',
    component: HomeComponent,
    data: {
        authorities: [],
        pageTitle: 'home.title',
    },
    canActivate: [UserRouteAccessService]
},
{
    path: 'login',
    component: JhiLoginComponent,
    data: {
        authorities: [],
        pageTitle: 'login.title',
    },
    canActivate: [UserRouteAccessService]
}];


