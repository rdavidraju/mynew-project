import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    MappingSetService,
    MappingSetPopupService,
    MappingSetComponent,
    MappingSetHomeComponent,
    MappingSetSideBarComponent,
    MappingSetDetailComponent,
    MappingSetDeletePopupComponent,
    MappingSetDeleteDialogComponent,
    mappingSetRoute,
    BulkUploadDialog,
    valueSetRoute
} from './';

const ENTITY_STATES = [
    ...mappingSetRoute,
    ...valueSetRoute,
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
        MappingSetComponent,
        MappingSetHomeComponent,
        MappingSetSideBarComponent,
        MappingSetDetailComponent,
        MappingSetDeleteDialogComponent,
        MappingSetDeletePopupComponent,
        BulkUploadDialog
    ],
    entryComponents: [
        MappingSetComponent,
        MappingSetHomeComponent,
        MappingSetSideBarComponent,
        MappingSetDeleteDialogComponent,
        MappingSetDeletePopupComponent,
        BulkUploadDialog
    ],
    providers: [
        MappingSetService,
        MappingSetPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1MappingSetModule {}
