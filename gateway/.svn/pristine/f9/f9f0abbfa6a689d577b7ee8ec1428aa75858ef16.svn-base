import { Routes } from '@angular/router';
import { HierarchyComponent } from './hierarchy.component';
import { UserRouteAccessService } from '../../shared';

export const hierarchyRoute: Routes = [{
    path: 'hierarchy',
    component: HierarchyComponent,
    data: {
        authorities: ['HIERARCHY_VIEW'],
        pageTitle: 'hierarchy.title'
    },
    canActivate: [UserRouteAccessService]
}];