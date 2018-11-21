import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';  
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { SidebarModules } from '../../shared/primeng/primeng';
import { NgCircleProgressModule } from 'ng-circle-progress';
import {
    DashboardService,
    DashboardV6Component,
    dashboardverRoute

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

    ],
    entryComponents: [
        DashboardV6Component,

    ],
    providers: [
        DashboardService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1DashboardverModule {}
