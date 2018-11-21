import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    JeLinesService,
    JeLinesPopupService,
    JeLinesComponent,
    JeLinesDetailComponent,
    JeLinesDialogComponent,
    JeLinesPopupComponent,
    JeLinesDeletePopupComponent,
    JeLinesDeleteDialogComponent,
    jeLinesRoute,
    jeLinesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jeLinesRoute,
    ...jeLinesPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JeLinesComponent,
        JeLinesDetailComponent,
        JeLinesDialogComponent,
        JeLinesDeleteDialogComponent,
        JeLinesPopupComponent,
        JeLinesDeletePopupComponent,
    ],
    entryComponents: [
        JeLinesComponent,
        JeLinesDialogComponent,
        JeLinesPopupComponent,
        JeLinesDeleteDialogComponent,
        JeLinesDeletePopupComponent,
    ],
    providers: [
        JeLinesService,
        JeLinesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1JeLinesModule {}
