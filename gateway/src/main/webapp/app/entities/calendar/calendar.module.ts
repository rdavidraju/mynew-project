import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    CalendarService,
    CalendarPopupService,
    CalendarComponent,
    CalendarDetailComponent,
    CalendarDialogComponent,
    CalendarPopupComponent,
    CalendarDeletePopupComponent,
    CalendarDeleteDialogComponent,
    CalendarRoute,
    CalendarResolvePagingParams,
    CalendarHomeComponent,
    CalendarSideBarComponent
} from './';

const ENTITY_STATES = [
    ...CalendarRoute
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
        CalendarComponent,
        CalendarDetailComponent,
        CalendarDialogComponent,
        CalendarDeleteDialogComponent,
        CalendarPopupComponent,
        CalendarDeletePopupComponent,
        CalendarHomeComponent,
        CalendarSideBarComponent
    ],
    entryComponents: [
        CalendarComponent,
        CalendarDialogComponent,
        CalendarPopupComponent,
        CalendarDeleteDialogComponent,
        CalendarDeletePopupComponent,
        CalendarHomeComponent,
        CalendarSideBarComponent
    ],
    providers: [
        CalendarService,
        CalendarPopupService,
        CalendarResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1CalendarModule {}
