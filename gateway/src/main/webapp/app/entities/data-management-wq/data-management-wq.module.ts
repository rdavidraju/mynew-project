import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {DataTableModule,SharedModule, MultiSelectModule,DialogModule,DragDropModule, DataGridModule} from 'primeng/primeng';
import {ConfirmDialogModule,ConfirmationService} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import {
    DataManagementWqService,
    DataManagementWqComponent,
    dataManagementWqRoute,
    DataManagementWqHomeComponent,
    DataManagementWQResolvePagingParams,
    ConfirmActionModalDialog
    
} from './';
import {AgreeGatewayV1FileTemplatesModule} from '../file-templates/file-templates.module';
import {AgreeGatewayV1JobsModule} from '../jobs/jobs.module';
import {InlineEditorModule} from '@qontu/ngx-inline-editor';

const ENTITY_STATES = [
    ...dataManagementWqRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        DataGridModule,
        DataTableModule,
        PaginatorModule,
        AccordionModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        AgreeGatewayV1JobsModule,
       AgreeGatewayV1FileTemplatesModule,
       InlineEditorModule
    ],
    declarations: [
        DataManagementWqComponent,
        DataManagementWqHomeComponent,
        ConfirmActionModalDialog
    ],
    entryComponents: [
        DataManagementWqComponent,
        DataManagementWqHomeComponent,
        ConfirmActionModalDialog
    ],
    providers: [
        DataManagementWqService,
        DataManagementWQResolvePagingParams,
        ConfirmationService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1DataManagementWqModule {}
