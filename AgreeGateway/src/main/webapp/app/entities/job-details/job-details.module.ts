import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    JobDetailsService,
    //JobDetailsPopupService,
    JobDetailsComponent,
    //JobDetailsDetailComponent,
    //JobDetailsDialogComponent,
    //JobDetailsPopupComponent,
    //JobDetailsDeletePopupComponent,
    //JobDetailsDeleteDialogComponent,
    jobDetailsRoute,
    jobDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobDetailsRoute,
    ...jobDetailsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobDetailsComponent,
        //JobDetailsDetailComponent,
        //JobDetailsDialogComponent,
       // JobDetailsDeleteDialogComponent,
        //JobDetailsPopupComponent,
        //JobDetailsDeletePopupComponent,
    ],
    entryComponents: [
        JobDetailsComponent,
        //JobDetailsDialogComponent,
        //JobDetailsPopupComponent,
        //JobDetailsDeleteDialogComponent,
       // JobDetailsDeletePopupComponent,
    ],
    providers: [
        JobDetailsService,
        //JobDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayJobDetailsModule {}
