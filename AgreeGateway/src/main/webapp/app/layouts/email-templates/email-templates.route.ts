import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ApprovedComponent,RejectedComponent } from './email-templates';

export const emailRoute: Routes = [
    {
        path: 'approved',
        component: ApprovedComponent,
        data: {
            authorities: [],
            pageTitle: 'approved.title'
        }
    },
    {
        path: 'rejected',
        component: RejectedComponent,
        data: {
            authorities: [],
            pageTitle: 'rejected.title'            
        }
    }
];
