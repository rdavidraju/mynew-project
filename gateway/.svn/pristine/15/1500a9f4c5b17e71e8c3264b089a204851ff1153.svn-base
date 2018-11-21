import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    JeLdrDetailsService,
    JeLdrDetailsPopupService,
    JeLdrDetailsComponent,
    JeLdrDetailsDetailComponent,
    JeLdrDetailsDialogComponent,
    JeLdrDetailsPopupComponent,
    JeLdrDetailsDeletePopupComponent,
    JeLdrDetailsDeleteDialogComponent,
    jeLdrDetailsRoute,
    jeLdrDetailsPopupRoute,
    JeLdrDetailsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...jeLdrDetailsRoute,
    ...jeLdrDetailsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JeLdrDetailsComponent,
        JeLdrDetailsDetailComponent,
        JeLdrDetailsDialogComponent,
        JeLdrDetailsDeleteDialogComponent,
        JeLdrDetailsPopupComponent,
        JeLdrDetailsDeletePopupComponent,
    ],
    entryComponents: [
        JeLdrDetailsComponent,
        JeLdrDetailsDialogComponent,
        JeLdrDetailsPopupComponent,
        JeLdrDetailsDeleteDialogComponent,
        JeLdrDetailsDeletePopupComponent,
    ],
    providers: [
        JeLdrDetailsService,
        JeLdrDetailsPopupService,
        JeLdrDetailsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1JeLdrDetailsModule {}
