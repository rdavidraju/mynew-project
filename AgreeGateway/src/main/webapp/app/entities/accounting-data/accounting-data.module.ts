import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {DataTableModule,SharedModule, MultiSelectModule,ChartModule,DialogModule,CheckboxModule,TreeTableModule,InputMaskModule,BlockUIModule} from 'primeng/primeng';
import { DndModule } from 'ng2-dnd';
import {AgreeGatewayV1RuleGroupModule} from '../rule-group/rule-group.module';
import { NguiAutoCompleteModule } from '@ngui/auto-complete';
// import { StickyHeaderDirective } from './sticky-header.directive';
import {
    AccountingDataService,
    // AccountingDataPopupService,
    AccountingDataComponent,
    // AccountingDataDetailComponent,
    // AccountingDataDialogComponent,
    // AccountingDataPopupComponent,
    // AccountingDataDeletePopupComponent,
    // AccountingDataDeleteDialogComponent,
    AcctConfirmModal,
    accountingDataRoute,
    AccountingDataResolvePagingParams,
    AccountingDataHomeComponent,
    // AccountingDataSideBarComponent,
    AccountingDataWqComponent,
    AccountingDataWqHomeComponent
} from './';

const ENTITY_STATES = [
    ...accountingDataRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        DataTableModule,
        DataTableModule,
        SharedModule,
        DndModule,
        MultiSelectModule,
        ChartModule,
        AgreePlugInsSharedModule,
        DialogModule,
        CheckboxModule,
        AgreeGatewayV1RuleGroupModule,
        TreeTableModule,
        NguiAutoCompleteModule,
        InputMaskModule,
        BlockUIModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AccountingDataComponent,
        AccountingDataWqHomeComponent,
        AccountingDataWqComponent,
        // AccountingDataDetailComponent,
        // AccountingDataDialogComponent,
        // AccountingDataDeleteDialogComponent,
        // AccountingDataPopupComponent,
        // AccountingDataDeletePopupComponent,
        AccountingDataHomeComponent,
        AcctConfirmModal,
        // StickyHeaderDirective
        // AccountingDataSideBarComponent
    ],
    entryComponents: [
        AccountingDataComponent,
        AccountingDataWqHomeComponent,
        AccountingDataWqComponent,
        AccountingDataWqComponent,
        // AccountingDataDialogComponent,
        // AccountingDataPopupComponent,
        // AccountingDataDeleteDialogComponent,
        // AccountingDataDeletePopupComponent,
        AccountingDataHomeComponent,
        AcctConfirmModal
    ],
    providers: [
        AccountingDataService,
        // AccountingDataPopupService,
        AccountingDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AccountingDataModule {}
