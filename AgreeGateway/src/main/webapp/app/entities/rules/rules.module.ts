import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';

import {
    RulesService,
    RulesPopupService,
    RulesComponent,
    RulesDetailComponent,
    RulesDialogComponent,
    RulesPopupComponent,
    RulesDeletePopupComponent,
    RulesDeleteDialogComponent,
    rulesRoute,
    rulesPopupRoute,
} from './';

let ENTITY_STATES = [
    ...rulesRoute,
    ...rulesPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RulesComponent,
        RulesDetailComponent,
        RulesDialogComponent,
        RulesDeleteDialogComponent,
        RulesPopupComponent,
        RulesDeletePopupComponent,
    ],
    entryComponents: [
        RulesComponent,
        RulesDialogComponent,
        RulesPopupComponent,
        RulesDeleteDialogComponent,
        RulesDeletePopupComponent,
    ],
    providers: [
        RulesService,
        RulesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1RulesModule {}
