import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    AcctRuleDerivationsService,
    AcctRuleDerivationsPopupService,
    AcctRuleDerivationsComponent,
    AcctRuleDerivationsDetailComponent,
    AcctRuleDerivationsDialogComponent,
    AcctRuleDerivationsPopupComponent,
    AcctRuleDerivationsDeletePopupComponent,
    AcctRuleDerivationsDeleteDialogComponent,
    acctRuleDerivationsRoute,
    acctRuleDerivationsPopupRoute,
    AcctRuleDerivationsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...acctRuleDerivationsRoute,
    ...acctRuleDerivationsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AcctRuleDerivationsComponent,
        AcctRuleDerivationsDetailComponent,
        AcctRuleDerivationsDialogComponent,
        AcctRuleDerivationsDeleteDialogComponent,
        AcctRuleDerivationsPopupComponent,
        AcctRuleDerivationsDeletePopupComponent,
    ],
    entryComponents: [
        AcctRuleDerivationsComponent,
        AcctRuleDerivationsDialogComponent,
        AcctRuleDerivationsPopupComponent,
        AcctRuleDerivationsDeleteDialogComponent,
        AcctRuleDerivationsDeletePopupComponent,
    ],
    providers: [
        AcctRuleDerivationsService,
        AcctRuleDerivationsPopupService,
        AcctRuleDerivationsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AcctRuleDerivationsModule {}
