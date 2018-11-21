import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    JobActionsService,
    JobActionsPopupService,
    JobActionsComponent,
    JobActionsDetailComponent,
    JobActionsDialogComponent,
    JobActionsPopupComponent,
    JobActionsDeletePopupComponent,
    JobActionsDeleteDialogComponent,
    jobActionsRoute,
    jobActionsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobActionsRoute,
    ...jobActionsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobActionsComponent,
        JobActionsDetailComponent,
        JobActionsDialogComponent,
        JobActionsDeleteDialogComponent,
        JobActionsPopupComponent,
        JobActionsDeletePopupComponent,
    ],
    entryComponents: [
        JobActionsComponent,
        JobActionsDialogComponent,
        JobActionsPopupComponent,
        JobActionsDeleteDialogComponent,
        JobActionsDeletePopupComponent,
    ],
    providers: [
        JobActionsService,
        JobActionsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1JobActionsModule {}
