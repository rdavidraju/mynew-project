import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    ReportsService,
    ReportsComponent,
    ReportsDetailComponent,
    reportsRoute,
    RunReportsComponent,
    ShareViaEmailDialog,
    ReportsHomeComponent,
} from './';

const ENTITY_STATES = [
    ...reportsRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReportsComponent,
        ReportsDetailComponent,
        ReportsHomeComponent,
        RunReportsComponent,
        ShareViaEmailDialog,
    ],
    entryComponents: [
        ReportsComponent,
        ReportsHomeComponent,
        RunReportsComponent,
        ShareViaEmailDialog,
    ],
    providers: [
        ReportsService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ReportsModule {}
