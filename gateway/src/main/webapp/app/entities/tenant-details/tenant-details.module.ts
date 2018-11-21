import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    TenantDetailsService,
    TenantDetailsPopupService,
    TenantDetailsComponent,
    TenantDetailsDetailComponent,
    TenantDetailsDialogComponent,
    TenantDetailsPopupComponent,
    TenantDetailsDeletePopupComponent,
    TenantDetailsDeleteDialogComponent,
    TenantDetailsRoute,
    // tenantDetailsPopupRoute,
    TenantDetailsResolvePagingParams,
    TenantDetailsHomeComponent,
    TenantDetailsSideBarComponent
} from './';

const ENTITY_STATES = [
    ...TenantDetailsRoute,
    // ...tenantDetailsPopupRoute,
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
        TenantDetailsComponent,
        TenantDetailsDetailComponent,
        TenantDetailsDialogComponent,
        TenantDetailsDeleteDialogComponent,
        TenantDetailsPopupComponent,
        TenantDetailsDeletePopupComponent,
        TenantDetailsHomeComponent,
        TenantDetailsSideBarComponent
    ],
    entryComponents: [
        TenantDetailsComponent,
        TenantDetailsDialogComponent,
        TenantDetailsPopupComponent,
        TenantDetailsDeleteDialogComponent,
        TenantDetailsDeletePopupComponent,
        TenantDetailsHomeComponent,
        TenantDetailsSideBarComponent
    ],
    providers: [
        TenantDetailsService,
        TenantDetailsPopupService,
        TenantDetailsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1TenantDetailsModule {}
