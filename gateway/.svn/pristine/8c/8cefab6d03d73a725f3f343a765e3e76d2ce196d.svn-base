import { Route, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../shared';
import { JhiLoginComponent } from './login.component';

export const loginRoute: Routes = [{
    path: 'login',
    component: JhiLoginComponent,
    data: {
        authorities: [],
        pageTitle: 'login.title',
    },
    canActivate: [UserRouteAccessService]
}];