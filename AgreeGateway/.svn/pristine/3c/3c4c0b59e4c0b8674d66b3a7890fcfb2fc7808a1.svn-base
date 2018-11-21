import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    AccountingLineTypesService,
    AccountingLineTypesPopupService,
    AccountingLineTypesComponent,
    AccountingLineTypesDetailComponent,
    AccountingLineTypesDialogComponent,
    AccountingLineTypesPopupComponent,
    AccountingLineTypesDeletePopupComponent,
    AccountingLineTypesDeleteDialogComponent,
    accountingLineTypesRoute,
    accountingLineTypesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...accountingLineTypesRoute,
    ...accountingLineTypesPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AccountingLineTypesComponent,
        AccountingLineTypesDetailComponent,
        AccountingLineTypesDialogComponent,
        AccountingLineTypesDeleteDialogComponent,
        AccountingLineTypesPopupComponent,
        AccountingLineTypesDeletePopupComponent,
    ],
    entryComponents: [
        AccountingLineTypesComponent,
        AccountingLineTypesDialogComponent,
        AccountingLineTypesPopupComponent,
        AccountingLineTypesDeleteDialogComponent,
        AccountingLineTypesDeletePopupComponent,
    ],
    providers: [
        AccountingLineTypesService,
        AccountingLineTypesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AccountingLineTypesModule {}
