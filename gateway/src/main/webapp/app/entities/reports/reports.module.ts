import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {NvD3Module} from 'ng2-nvd3';
import { DecimalPipe } from '@angular/common';
//import { GridsterModule } from 'angular2gridster';
import { WebDataRocksPivot } from "../../shared/webdatarocks/webdatarocks.angular4";
import {
    ReportsService,
    ReportsComponent,
    ReportsDetailComponent,
    reportsRoute,
    RunReportsComponent,
    ShareViaEmailDialog,
    ReportsHomeComponent,
    ReportsDashboardComponent,
    BucketListComponent,
    BucketDetailComponent,
    RunReportWizardComponent
} from './';

const ENTITY_STATES = [
    ...reportsRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        NvD3Module,
//        GridsterModule.forRoot(),
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReportsComponent,
        ReportsDetailComponent,
        ReportsHomeComponent,
        RunReportsComponent,
        ShareViaEmailDialog,
        ReportsDashboardComponent,
        WebDataRocksPivot,
        BucketListComponent,
        BucketDetailComponent,
        RunReportWizardComponent
    ],
    entryComponents: [
        ReportsComponent,
        ReportsHomeComponent,
        RunReportsComponent,
        ShareViaEmailDialog,
        ReportsDashboardComponent
    ],
    providers: [
        ReportsService,
        DecimalPipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ReportsModule {}
