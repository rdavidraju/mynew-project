import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {AgreeGatewayV1RuleGroupModule} from '../rule-group/rule-group.module';
import {PushNotificationsService} from 'angular2-notifications-lite';

import 'chart.js';
import {
    ReconcileService,
    ReconcileComponent,
    reconcileRoute,
    ReconcileHomeComponent,
    ReconcileResolvePagingParams,
    RWQComponent,
    RWQHomeComponent,
    WqConfirmActionModalDialogComponent
} from './';

const ENTITY_STATES = [
    ...reconcileRoute,
];
@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        AgreeGatewayV1RuleGroupModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
    ],
    declarations: [
        ReconcileComponent,
        RWQComponent,
        RWQHomeComponent,
        ReconcileHomeComponent,
        WqConfirmActionModalDialogComponent,
    ],
    entryComponents: [
        ReconcileComponent,
        RWQComponent,
        RWQHomeComponent,
        ReconcileHomeComponent,
        WqConfirmActionModalDialogComponent
    ],
    providers: [
        ReconcileService,
        ReconcileResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ReconcileModule {}
