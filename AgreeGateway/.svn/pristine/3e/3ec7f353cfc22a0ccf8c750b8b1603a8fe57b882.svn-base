import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    SegmentsService,
    SegmentsPopupService,
    SegmentsComponent,
    SegmentsDetailComponent,
    SegmentsDialogComponent,
    SegmentsPopupComponent,
    SegmentsDeletePopupComponent,
    SegmentsDeleteDialogComponent,
    segmentsRoute,
    segmentsPopupRoute,
    SegmentsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...segmentsRoute,
    ...segmentsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SegmentsComponent,
        SegmentsDetailComponent,
        SegmentsDialogComponent,
        SegmentsDeleteDialogComponent,
        SegmentsPopupComponent,
        SegmentsDeletePopupComponent,
    ],
    entryComponents: [
        SegmentsComponent,
        SegmentsDialogComponent,
        SegmentsPopupComponent,
        SegmentsDeleteDialogComponent,
        SegmentsDeletePopupComponent,
    ],
    providers: [
        SegmentsService,
        SegmentsPopupService,
        SegmentsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1SegmentsModule {}
