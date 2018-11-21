import { Routes } from '@angular/router';
import { HierarchyComponent } from './hierarchy.component';
import { UserRouteAccessService } from '../../shared';

export const hierarchyRoute: Routes = [{
    path: 'hierarchy',
    component: HierarchyComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'hierarchy.title'
    },
    canActivate: [UserRouteAccessService]
}];