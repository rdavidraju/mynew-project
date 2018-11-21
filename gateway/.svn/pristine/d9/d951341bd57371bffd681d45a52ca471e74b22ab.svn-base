import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ActivateComponent } from './activate.component';
import { ActivateDefaultUserComponent } from './activate-default-user.component';
import { ActivateErrorComponent } from './activate-error.component';

/* export const activateRoute: Route =     {
    path: 'activate',
    component: ActivateComponent,
    data: {
        authorities: [],
        pageTitle: 'activate.title'
    },
    canActivate: [UserRouteAccessService],
    children: [
        {
            path: 'error',
            component: ActivateErrorComponent,
            data: {
                authorities: [],
                pageTitle: 'activate.title'
            },
            canActivate: [UserRouteAccessService]
        }
    ]
} */

export const activateRoute: Routes = [
    {
        path: 'activate-default-user',
        component: ActivateComponent,
        data: {
            authorities: [],
            pageTitle: 'activate.title'
        }
    },    {
        path: 'activate',
        component: ActivateComponent,
        data: {
            authorities: [],
            pageTitle: 'activate.title'
        }
    },
    {
        path: 'activate-error',
        component: ActivateErrorComponent,
        data: {
            authorities: [],
            pageTitle: 'activate.title'            
        }
    }
];

