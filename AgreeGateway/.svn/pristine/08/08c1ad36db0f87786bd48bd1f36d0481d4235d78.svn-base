import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {DataTableModule,SharedModule, MultiSelectModule, OrganizationChartModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';

import {
    LookUpCodeService,
    LookUpCodePopupService,
    LookUpCodeComponent,
    LookUpCodeDetailComponent,
    LookUpCodeDialogComponent,
    LookUpCodePopupComponent,
    LookUpCodeDeletePopupComponent,
    LookUpCodeDeleteDialogComponent,
    LookupCodeRoute,
    // lookUpCodePopupRoute,
    LookUpCodeResolvePagingParams,
    LookupCodeHomeComponent,
    LookupCodeSideBarComponent
} from './';

const ENTITY_STATES = [
    ...LookupCodeRoute,
    // ...lookUpCodePopupRoute,
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
        LookUpCodeComponent,
        LookUpCodeDetailComponent,
        LookUpCodeDialogComponent,
        LookUpCodeDeleteDialogComponent,
        LookUpCodePopupComponent,
        LookUpCodeDeletePopupComponent,
        LookupCodeHomeComponent,
        LookupCodeSideBarComponent
    ],
    entryComponents: [
        LookUpCodeComponent,
        LookUpCodeDialogComponent,
        LookUpCodePopupComponent,
        LookUpCodeDeleteDialogComponent,
        LookUpCodeDeletePopupComponent,
        LookupCodeHomeComponent,
        LookupCodeSideBarComponent
    ],
    providers: [
        LookUpCodeService,
        LookUpCodePopupService,
        LookUpCodeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1LookUpCodeModule {}
