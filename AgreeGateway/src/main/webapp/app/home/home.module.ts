import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLoginComponent } from '../shared/login'
import { AgreePlugInsSharedModule } from '../agree-plugins-shared.module';
import { SidebarModules } from '../shared/primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../shared';
import { NgCircleProgressModule } from 'ng-circle-progress';
import {NgbDropdownModule} from "@ng-bootstrap/ng-bootstrap/dropdown/dropdown.module";
import {AmountFormat} from "../entities/amountFormatPipe";
import 'chartjs-plugin-datalabels';
/* import { ChartModule ,HIGHCHARTS_MODULES  } from 'angular-highcharts'; */
import { 
    HOME_ROUTE,
    HomeComponent,
    DashboardComponent,
    DashboardV2Component,
    DashboardMaterialDialog,
    DashboardV3Component,
    DashboardV4Component,
    DashboardV5Component,
    DialogOverviewExampleDialog
    // DateTimePickerComponent
 } from './';

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        SidebarModules,
        NgbDropdownModule,
        /* ChartModule, */
        RouterModule.forRoot([ ...HOME_ROUTE ], { useHash: true }),
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
        HomeComponent,
        JhiLoginComponent,
        DashboardComponent,
        DashboardV2Component,
        DashboardV3Component,
        DashboardV4Component,
        DashboardV5Component,
        DashboardMaterialDialog,
        DialogOverviewExampleDialog,
        AmountFormat,
        // DateTimePickerComponent
    ],
    entryComponents: [
        DashboardMaterialDialog,
        DialogOverviewExampleDialog
    ],
    providers: [
        AmountFormat
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1HomeModule {}