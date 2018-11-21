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
    MappingSetDialogComponent,
    MappingSetPopupComponent,
    MappingSetDeletePopupComponent,
    MappingSetDeleteDialogComponent,
    mappingSetRoute,
    // mappingSetPopupRoute,
} from './';

const ENTITY_STATES = [
    ...mappingSetRoute,
    // ...mappingSetPopupRoute,
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
        MappingSetDialogComponent,
        MappingSetDeleteDialogComponent,
        MappingSetPopupComponent,
        MappingSetDeletePopupComponent,
    ],
    entryComponents: [
        MappingSetComponent,
        MappingSetHomeComponent,
        MappingSetSideBarComponent,
        MappingSetDialogComponent,
        MappingSetPopupComponent,
        MappingSetDeleteDialogComponent,
        MappingSetDeletePopupComponent,
    ],
    providers: [
        MappingSetService,
        MappingSetPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1MappingSetModule {}
