import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    ProcessService,
    ProcessPopupService,
    ProcessComponent,
    ProcessDetailComponent,
    ProcessDialogComponent,
    ProcessPopupComponent,
    ProcessDeletePopupComponent,
    ProcessDeleteDialogComponent,
    processRoute,
    processPopupRoute,
} from './';

const ENTITY_STATES = [
    ...processRoute,
    ...processPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProcessComponent,
        ProcessDetailComponent,
        ProcessDialogComponent,
        ProcessDeleteDialogComponent,
        ProcessPopupComponent,
        ProcessDeletePopupComponent,
    ],
    entryComponents: [
        ProcessComponent,
        ProcessDialogComponent,
        ProcessPopupComponent,
        ProcessDeleteDialogComponent,
        ProcessDeletePopupComponent,
    ],
    providers: [
        ProcessService,
        ProcessPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ProcessModule {}
