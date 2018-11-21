import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    ApplicationProgramsService,
    ApplicationProgramsPopupService,
    ApplicationProgramsComponent,
    ApplicationProgramsDetailComponent,
    ApplicationProgramsDialogComponent,
    ApplicationProgramsPopupComponent,
    ApplicationProgramsDeletePopupComponent,
    ApplicationProgramsDeleteDialogComponent,
    applicationProgramsRoute,
    applicationProgramsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...applicationProgramsRoute,
    ...applicationProgramsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ApplicationProgramsComponent,
        ApplicationProgramsDetailComponent,
        ApplicationProgramsDialogComponent,
        ApplicationProgramsDeleteDialogComponent,
        ApplicationProgramsPopupComponent,
        ApplicationProgramsDeletePopupComponent,
    ],
    entryComponents: [
        ApplicationProgramsComponent,
        ApplicationProgramsDialogComponent,
        ApplicationProgramsPopupComponent,
        ApplicationProgramsDeleteDialogComponent,
        ApplicationProgramsDeletePopupComponent,
    ],
    providers: [
        ApplicationProgramsService,
        ApplicationProgramsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayApplicationProgramsModule {}
