import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { DataTableModule,SharedModule, MultiSelectModule } from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {
    LookUpCodeService,
    LookUpCodePopupService,
    LookUpCodeComponent,
    LookUpCodeDetailComponent,
    LookUpCodeDialogComponent,
    LookUpCodeDeletePopupComponent,
    LookUpCodeDeleteDialogComponent,
    LookupCodeRoute,
    LookUpCodeResolvePagingParams,
    LookupCodeHomeComponent,
    LookupCodeSideBarComponent
} from './';

const ENTITY_STATES = [
    ...LookupCodeRoute
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
        LookUpCodeDeletePopupComponent,
        LookupCodeHomeComponent,
        LookupCodeSideBarComponent
    ],
    entryComponents: [
        LookUpCodeComponent,
        LookUpCodeDialogComponent,
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
