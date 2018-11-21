import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import {
    LedgerDefinitionService,
    LedgerDefinitionPopupService,
    LedgerDefinitionComponent,
    LedgerDefinitionDetailComponent,
    LedgerDefinitionDialogComponent,
    LedgerDefinitionPopupComponent,
    LedgerDefinitionDeletePopupComponent,
    LedgerDefinitionDeleteDialogComponent,
    LedgerDefinitionRoute,
    LedgerDefinitionHomeComponent,
    LedgerDefinitionSideBarComponent,
    LedgerDefinitionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...LedgerDefinitionRoute
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
        LedgerDefinitionComponent,
        LedgerDefinitionDetailComponent,
        LedgerDefinitionDialogComponent,
        LedgerDefinitionDeleteDialogComponent,
        LedgerDefinitionPopupComponent,
        LedgerDefinitionDeletePopupComponent,
        LedgerDefinitionHomeComponent,
        LedgerDefinitionSideBarComponent
    ],
    entryComponents: [
        LedgerDefinitionComponent,
        LedgerDefinitionDialogComponent,
        LedgerDefinitionPopupComponent,
        LedgerDefinitionDeleteDialogComponent,
        LedgerDefinitionDeletePopupComponent,
        LedgerDefinitionHomeComponent,
        LedgerDefinitionSideBarComponent
    ],
    providers: [
        LedgerDefinitionService,
        LedgerDefinitionPopupService,
        LedgerDefinitionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1LedgerDefinitionModule {}