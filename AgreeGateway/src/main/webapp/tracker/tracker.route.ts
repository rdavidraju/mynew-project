import { Route } from '@angular/router';

import { UserRouteAccessService, Principal } from '../app/shared';
import { JhiTrackerComponent } from './tracker.component';
import { JhiTrackerService } from '../app/shared/tracker/tracker.service';

export const trackerRoute: Route = {
    path: 'jhi-tracker',
    component: JhiTrackerComponent,
    data: {
        pageTitle: 'tracker.title'
    }
};
