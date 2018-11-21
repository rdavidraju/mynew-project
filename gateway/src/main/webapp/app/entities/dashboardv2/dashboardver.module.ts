import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DataTableModule, SharedModule, MultiSelectModule } from 'primeng/primeng';
import { PaginatorModule, AccordionModule } from 'primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { SidebarModules } from '../../shared/primeng/primeng';
import { NgCircleProgressModule } from 'ng-circle-progress';
//import { DaterangepickerModule } from '../../daterangepicker';
//import { DaterangepickerModule } from 'angular-2-daterangepicker';
//import { Daterangepicker } from 'ng2-daterangepicker';
import { BrowserModule } from '@angular/platform-browser';
import {
    DashboardService,
    DashboardV6Component,
    DashboardV7Component,
    DashboardV8Component,
    dashboardverRoute,
    KPIModal

} from './';

const ENTITY_STATES = [
    ...dashboardverRoute,

];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        DataTableModule,
        SharedModule,
        MultiSelectModule,
        PaginatorModule,
        AccordionModule,
        AgreePlugInsSharedModule,
        /* MyDateRangePickerModule, */
        BrowserModule,
        SidebarModules,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        NgCircleProgressModule.forRoot({
            // set defaults here
            radius: 100,
            outerStrokeWidth: 16,
            innerStrokeWidth: 8,
            outerStrokeColor: "#78C000",
            innerStrokeColor: "#C7E596",
            animationDuration: 300
        }),
    ],
    declarations: [
        DashboardV6Component,
        DashboardV7Component,
        DashboardV8Component,
        KPIModal
    ],
    entryComponents: [
        DashboardV6Component,
        DashboardV7Component,
        DashboardV8Component,
        KPIModal
    ],
    providers: [
        DashboardService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1DashboardverModule { }
