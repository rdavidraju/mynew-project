import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../shared';
import { JhiTrackerService } from './../shared/tracker/tracker.service';

import { JhiTrackerComponent } from '../../tracker/tracker.component';
//import { WebSocketSharedModule } from '../shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */
import {DataTableModule,SharedModule, MultiSelectModule,DialogModule,DragDropModule, DataGridModule,AccordionModule} from 'primeng/primeng';
import {
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    DialogOverviewExampleDialog,
    UserDialogComponent,
    UserDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtDialogComponent,
    UserMgmtDeleteDialogComponent,
    LogsComponent,
    JhiMetricsMonitoringModalComponent,
    JhiMetricsMonitoringComponent,
    JhiHealthModalComponent,
    JhiHealthCheckComponent,
    JhiConfigurationComponent,
    JhiDocsComponent,
    AuditsService,
    JhiConfigurationService,
    JhiHealthService,
    JhiMetricsService,
    GatewayRoutesService,
    JhiGatewayComponent,
    LogsService,
    UserResolvePagingParams,
    UserResolve,
    UserModalService,
    UserMgmtSideBarComponent,
    UserMgmtHomeComponent,
} from './';

@NgModule({
    imports: [
  //  WebSocketSharedModule,
    DialogModule,
    DragDropModule,
    DataGridModule,
    AccordionModule,
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(adminState, { useHash: true }),
        AgreePlugInsSharedModule
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        DialogOverviewExampleDialog,
        UserDialogComponent,
        UserDeleteDialogComponent,
        UserMgmtDetailComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        LogsComponent,
        JhiConfigurationComponent,
        JhiHealthCheckComponent,
        JhiHealthModalComponent,
        JhiDocsComponent,
        JhiGatewayComponent,
        JhiMetricsMonitoringComponent,
        JhiTrackerComponent,
        JhiMetricsMonitoringModalComponent,
        UserMgmtSideBarComponent,
        UserMgmtHomeComponent,
    ],
    entryComponents: [
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        JhiHealthModalComponent,
        JhiMetricsMonitoringModalComponent,
        UserMgmtSideBarComponent,
        UserMgmtHomeComponent,
        DialogOverviewExampleDialog,
    ],
    providers: [
        JhiTrackerService,
        AuditsService,
        JhiConfigurationService,
        JhiHealthService,
        JhiMetricsService,
        GatewayRoutesService,
        LogsService,
        UserResolvePagingParams,
        UserResolve,
        UserModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1AdminModule {}
