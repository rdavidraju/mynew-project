import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {AgreeGatewayV1RuleGroupModule} from '../rule-group/rule-group.module';
import { SidebarModules } from '../../shared/primeng/primeng';
import {
    AccountingDataService,
    AccountingDataComponent,
    AcctConfirmModalComponent,
    accountingDataRoute,
    AccountingDataResolvePagingParams,
    AccountingDataHomeComponent,
    AccountingDataWqComponent,
    AccountingDataWqHomeComponent
} from './';

const ENTITY_STATES = [
    ...accountingDataRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        SidebarModules,
        AgreePlugInsSharedModule,
        AgreeGatewayV1RuleGroupModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AccountingDataComponent,
        AccountingDataWqHomeComponent,
        AccountingDataWqComponent,
        AccountingDataHomeComponent,
        AcctConfirmModalComponent
    ],
    entryComponents: [
        AccountingDataComponent,
        AccountingDataWqHomeComponent,
        AccountingDataWqComponent,
        AccountingDataHomeComponent,
        AcctConfirmModalComponent
    ],
    providers: [
        AccountingDataService,
        AccountingDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AccountingDataModule {}
