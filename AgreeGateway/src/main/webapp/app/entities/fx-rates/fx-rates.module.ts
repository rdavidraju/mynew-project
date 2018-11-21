import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import {
    FxRatesService,
    FxRatesPopupService,
    FxRatesComponent,
    FxRatesDetailComponent,
    FxRatesDialogComponent,
    FxRatesPopupComponent,
    FxRatesDeletePopupComponent,
    FxRatesDeleteDialogComponent,
    FxRatesRoute,
    // fxRatesPopupRoute,
    FxRatesResolvePagingParams,
    FxRatesHomeComponent,
    FxRatesSideBarComponent
    
} from './';

const ENTITY_STATES = [
    ...FxRatesRoute,
    // ...fxRatesPopupRoute,
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
        FxRatesComponent,
        FxRatesDetailComponent,
        FxRatesDialogComponent,
        FxRatesDeleteDialogComponent,
        FxRatesPopupComponent,
        FxRatesDeletePopupComponent,
        FxRatesHomeComponent,
        FxRatesSideBarComponent
    ],
    entryComponents: [
        FxRatesComponent,
        FxRatesDialogComponent,
        FxRatesPopupComponent,
        FxRatesDeleteDialogComponent,
        FxRatesDeletePopupComponent,
        FxRatesHomeComponent,
        FxRatesSideBarComponent
    ],
    providers: [
        FxRatesService,
        FxRatesPopupService,
        FxRatesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1FxRatesModule {}
