import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    PeriodsService,
    PeriodsPopupService,
    PeriodsComponent,
    PeriodsDetailComponent,
    PeriodsDialogComponent,
    PeriodsPopupComponent,
    PeriodsDeletePopupComponent,
    PeriodsDeleteDialogComponent,
    periodsRoute,
    periodsPopupRoute,
    PeriodsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...periodsRoute,
    ...periodsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PeriodsComponent,
        PeriodsDetailComponent,
        PeriodsDialogComponent,
        PeriodsDeleteDialogComponent,
        PeriodsPopupComponent,
        PeriodsDeletePopupComponent,
    ],
    entryComponents: [
        PeriodsComponent,
        PeriodsDialogComponent,
        PeriodsPopupComponent,
        PeriodsDeleteDialogComponent,
        PeriodsDeletePopupComponent,
    ],
    providers: [
        PeriodsService,
        PeriodsPopupService,
        PeriodsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1PeriodsModule {}
