import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule,ChartModule,DialogModule,CheckboxModule,TreeTableModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {AgreeGatewayV1RuleGroupModule} from '../rule-group/rule-group.module';
import { CountoModule }  from 'angular2-counto';
import 'chart.js';
import {
    ReconcileService,
    ReconcilePopupService,
    ReconcileComponent,
    ReconcileDetailComponent,
    ReconcileDialogComponent,
    ReconcilePopupComponent,
    ReconcileDeletePopupComponent,
    ReconcileDeleteDialogComponent,
    reconcileRoute,
    reconcileHomeComponent,
    /* reconcilePopupRoute, */
    ReconcileResolvePagingParams,
    ReconcileSideBarComponent,
    RWQComponent,
    RWQHomeComponent,
    WqConfirmActionModalDialog
} from './';

const ENTITY_STATES = [
    ...reconcileRoute,
    /* ...reconcilePopupRoute, */
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        DataTableModule,
        SharedModule,
        MultiSelectModule,
        PaginatorModule,
        AccordionModule,
        ChartModule,
        DialogModule,
        CheckboxModule,
        TreeTableModule,
        AgreePlugInsSharedModule,
        AgreeGatewayV1RuleGroupModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        CountoModule
    ],
    declarations: [
        ReconcileComponent,
        RWQComponent,
        RWQHomeComponent,
        ReconcileDetailComponent,
        ReconcileDialogComponent,
        ReconcileDeleteDialogComponent,
        ReconcilePopupComponent,
        ReconcileDeletePopupComponent,
        reconcileHomeComponent,
        ReconcileSideBarComponent,
        WqConfirmActionModalDialog,
    ],
    entryComponents: [
        ReconcileComponent,
        RWQComponent,
        RWQHomeComponent,
        ReconcileDialogComponent,
        ReconcilePopupComponent,
        ReconcileDeleteDialogComponent,
        ReconcileDeletePopupComponent,
        reconcileHomeComponent,
        ReconcileSideBarComponent,
        WqConfirmActionModalDialog
    ],
    providers: [
        ReconcileService,
        ReconcilePopupService,
        ReconcileResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ReconcileModule {}
