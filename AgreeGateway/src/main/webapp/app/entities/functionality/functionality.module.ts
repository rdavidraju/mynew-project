import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    FunctionalityService,
    FunctionalityPopupService,
    FunctionalityComponent,
    FunctionalityDetailComponent,
    FunctionalityDialogComponent,
    FunctionalityPopupComponent,
    FunctionalityDeletePopupComponent,
    FunctionalityDeleteDialogComponent,
    functionalityRoute,
    FunctionalityHomeComponent,
    FunctionalitySideBarComponent,
    FunctionalityResolvePagingParams,
    FunctionalityModalDialog
} from './';

const ENTITY_STATES = [
    ...functionalityRoute,
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
        FunctionalityComponent,
        FunctionalityDetailComponent,
        FunctionalityDialogComponent,
        FunctionalityDeleteDialogComponent,
        FunctionalityPopupComponent,
        FunctionalityDeletePopupComponent,
        FunctionalityHomeComponent,
        FunctionalitySideBarComponent,
        FunctionalityModalDialog
    ],
    entryComponents: [
        FunctionalityComponent,
        FunctionalityDialogComponent,
        FunctionalityPopupComponent,
        FunctionalityDeleteDialogComponent,
        FunctionalityDeletePopupComponent,
        FunctionalityHomeComponent,
        FunctionalitySideBarComponent,
        FunctionalityModalDialog
    ],
    providers: [
        FunctionalityService,
        FunctionalityPopupService,
        FunctionalityResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1FunctionalityModule {}
