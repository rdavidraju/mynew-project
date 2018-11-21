import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {
    ChartOfAccountService,
    ChartOfAccountPopupService,
    ChartOfAccountComponent,
    ChartOfAccountDetailComponent,
    ChartOfAccountDialogComponent,
    ChartOfAccountPopupComponent,
    ChartOfAccountDeletePopupComponent,
    ChartOfAccountDeleteDialogComponent,
    ChartOfAccountRoute,
    ChartOfAccountHomeComponent,
    ChartOfAccountSideBarComponent,
    // chartOfAccountPopupRoute,
    ChartOfAccountResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...ChartOfAccountRoute,
    // ...chartOfAccountPopupRoute,
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
        ChartOfAccountComponent,
        ChartOfAccountDetailComponent,
        ChartOfAccountDialogComponent,
        ChartOfAccountDeleteDialogComponent,
        ChartOfAccountPopupComponent,
        ChartOfAccountDeletePopupComponent,
        ChartOfAccountHomeComponent,
        ChartOfAccountSideBarComponent
    ],
    entryComponents: [
        ChartOfAccountComponent,
        ChartOfAccountDialogComponent,
        ChartOfAccountPopupComponent,
        ChartOfAccountDeleteDialogComponent,
        ChartOfAccountDeletePopupComponent,
        ChartOfAccountHomeComponent,
        ChartOfAccountSideBarComponent
    ],
    providers: [
        ChartOfAccountService,
        ChartOfAccountPopupService,
        ChartOfAccountResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ChartOfAccountModule {}
