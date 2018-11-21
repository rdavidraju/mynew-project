import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { SharedContentModule } from "../../shared-content/shared-content.module";
import {
    JobsService,
    JobDetailsService,
    JobsPopupService,
    JobsComponent,
    JobsDetailComponent,
    JobsDialogComponent,
    JobsPopupComponent,
   // JobsDeletePopupComponent,
    //JobsDeleteDialogComponent,
    jobsRoute,
    JobsHomeComponent,
    JobsSideBarComponent,
    JobsSchedularDetailsComponent,
    JobsNewDialog
} from './';

const ENTITY_STATES = [
    ...jobsRoute
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        SharedContentModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobsComponent,
        JobsDetailComponent,
        JobsDialogComponent,
       // JobsDeleteDialogComponent,
        JobsPopupComponent,
        //JobsDeletePopupComponent,
        JobsHomeComponent,
        JobsSideBarComponent,
        JobsSchedularDetailsComponent,
        JobsNewDialog
    ],
    entryComponents: [
        JobsComponent,
        JobsDialogComponent,
        JobsPopupComponent,
       // JobsDeleteDialogComponent,
       // JobsDeletePopupComponent,
        JobsHomeComponent,
        JobsSideBarComponent,
        JobsSchedularDetailsComponent,
        JobsNewDialog
    ],
    providers: [
        JobsService,
        JobDetailsService,
        JobsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1JobsModule {}
