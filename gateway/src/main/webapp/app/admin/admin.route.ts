import { Routes } from '@angular/router';

import {
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    gatewayRoute,
    // userMgmtRoute,
    // userDialogRoute,
    // trackerRoute
} from './';

import { UserRouteAccessService } from '../shared';

const ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    gatewayRoute,
    // ...userMgmtRoute,
    metricsRoute,
    // trackerRoute
];

export const adminState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService],
    children: ADMIN_ROUTES
},
    // ...userDialogRoute
];
