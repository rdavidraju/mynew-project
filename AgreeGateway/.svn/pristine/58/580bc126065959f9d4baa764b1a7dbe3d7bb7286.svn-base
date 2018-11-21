import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    SourceProfileFileAssignmentsService,
    //SourceProfileFileAssignmentsPopupService,
    //SourceProfileFileAssignmentsComponent,
    //SourceProfileFileAssignmentsDetailComponent,
    //SourceProfileFileAssignmentsDialogComponent,
    //SourceProfileFileAssignmentsPopupComponent,
    //SourceProfileFileAssignmentsDeletePopupComponent,
    //SourceProfileFileAssignmentsDeleteDialogComponent,
    sourceProfileFileAssignmentsRoute,
    sourceProfileFileAssignmentsPopupRoute,
    SourceProfileFileAssignmentsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sourceProfileFileAssignmentsRoute,
    ...sourceProfileFileAssignmentsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
       // SourceProfileFileAssignmentsComponent,
       // SourceProfileFileAssignmentsDetailComponent,
        //SourceProfileFileAssignmentsDialogComponent,
        //SourceProfileFileAssignmentsDeleteDialogComponent,
        //SourceProfileFileAssignmentsPopupComponent,
        //SourceProfileFileAssignmentsDeletePopupComponent,
    ],
    entryComponents: [
        //SourceProfileFileAssignmentsComponent,
       // SourceProfileFileAssignmentsDialogComponent,
       // SourceProfileFileAssignmentsPopupComponent,
        //SourceProfileFileAssignmentsDeleteDialogComponent,
        //SourceProfileFileAssignmentsDeletePopupComponent,
    ],
    providers: [
        SourceProfileFileAssignmentsService,
        //SourceProfileFileAssignmentsPopupService,
        SourceProfileFileAssignmentsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1SourceProfileFileAssignmentsModule {}
