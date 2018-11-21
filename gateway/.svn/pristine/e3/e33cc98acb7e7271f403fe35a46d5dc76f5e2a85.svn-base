import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { navbarRoute } from '../app.route';
import { errorRoute} from './';
import {emailRoute} from './email-templates/email-templates.route';
const LAYOUT_ROUTES = [
    navbarRoute,
    ...emailRoute,
    ...errorRoute,
];

@NgModule({
    imports: [
        RouterModule.forRoot(LAYOUT_ROUTES, { useHash: true })
    ],
    exports: [
        RouterModule
    ]
})
export class LayoutRoutingModule {}
