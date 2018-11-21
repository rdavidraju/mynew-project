import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    AppRuleConditionsService,
    AppRuleConditionsPopupService,
    AppRuleConditionsComponent,
    AppRuleConditionsDetailComponent,
    AppRuleConditionsDialogComponent,
    AppRuleConditionsPopupComponent,
    AppRuleConditionsDeletePopupComponent,
    AppRuleConditionsDeleteDialogComponent,
    appRuleConditionsRoute,
    appRuleConditionsPopupRoute,
    AppRuleConditionsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...appRuleConditionsRoute,
    ...appRuleConditionsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AppRuleConditionsComponent,
        AppRuleConditionsDetailComponent,
        AppRuleConditionsDialogComponent,
        AppRuleConditionsDeleteDialogComponent,
        AppRuleConditionsPopupComponent,
        AppRuleConditionsDeletePopupComponent,
    ],
    entryComponents: [
        AppRuleConditionsComponent,
        AppRuleConditionsDialogComponent,
        AppRuleConditionsPopupComponent,
        AppRuleConditionsDeleteDialogComponent,
        AppRuleConditionsDeletePopupComponent,
    ],
    providers: [
        AppRuleConditionsService,
        AppRuleConditionsPopupService,
        AppRuleConditionsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AppRuleConditionsModule {}
