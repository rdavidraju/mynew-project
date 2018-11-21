import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    ProcessesService,
    ProcessesPopupService,
    ProcessesComponent,
    ProcessesDetailComponent,
    ProcessesDialogComponent,
    ProcessesPopupComponent,
    ProcessesDeletePopupComponent,
    ProcessesDeleteDialogComponent,
    ProcessesRoute,
    ProcessHomeComponent,
    ProcessSideBarComponent
    /* processesPopupRoute,
    ProcessesResolvePagingParams, */
} from './';

const ENTITY_STATES = [
    ...ProcessesRoute,
    // ...processesPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        DataTableModule,
        SharedModule,
        MultiSelectModule,
        AgreePlugInsSharedModule,
        PaginatorModule,
        AccordionModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProcessesComponent,
        ProcessesDetailComponent,
        ProcessesDialogComponent,
        ProcessesDeleteDialogComponent,
        ProcessesPopupComponent,
        ProcessesDeletePopupComponent,
        ProcessHomeComponent,
        ProcessSideBarComponent
    ],
    entryComponents: [
        ProcessesComponent,
        ProcessesDialogComponent,
        ProcessesPopupComponent,
        ProcessesDeleteDialogComponent,
        ProcessesDeletePopupComponent,
        ProcessHomeComponent,
        ProcessSideBarComponent
    ],
    providers: [
        ProcessesService,
        ProcessesPopupService,
        // ProcessesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ProcessesModule {}
