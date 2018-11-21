import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    AcctRuleConditionsService,
    AcctRuleConditionsPopupService,
    AcctRuleConditionsComponent,
    AcctRuleConditionsDetailComponent,
    AcctRuleConditionsDialogComponent,
    AcctRuleConditionsPopupComponent,
    AcctRuleConditionsDeletePopupComponent,
    AcctRuleConditionsDeleteDialogComponent,
    acctRuleConditionsRoute,
    acctRuleConditionsPopupRoute,
    AcctRuleConditionsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...acctRuleConditionsRoute,
    ...acctRuleConditionsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AcctRuleConditionsComponent,
        AcctRuleConditionsDetailComponent,
        AcctRuleConditionsDialogComponent,
        AcctRuleConditionsDeleteDialogComponent,
        AcctRuleConditionsPopupComponent,
        AcctRuleConditionsDeletePopupComponent,
    ],
    entryComponents: [
        AcctRuleConditionsComponent,
        AcctRuleConditionsDialogComponent,
        AcctRuleConditionsPopupComponent,
        AcctRuleConditionsDeleteDialogComponent,
        AcctRuleConditionsDeletePopupComponent,
    ],
    providers: [
        AcctRuleConditionsService,
        AcctRuleConditionsPopupService,
        AcctRuleConditionsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AcctRuleConditionsModule {}
