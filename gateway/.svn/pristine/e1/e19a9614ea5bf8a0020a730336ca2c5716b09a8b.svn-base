import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {OrganizationChartModule} from 'primeng/primeng';

import {
    HierarchyComponent,
    HierarchyService,
    hierarchyRoute
} from './';

const ENTITY_STATES = [
    ...hierarchyRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        OrganizationChartModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        HierarchyComponent
    ],
    entryComponents: [
        HierarchyComponent
    ],
    providers: [
        HierarchyService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1HierarchyModule {}
