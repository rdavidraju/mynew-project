import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DataTableModule, SharedModule, MultiSelectModule, DialogModule, DragDropModule, DataGridModule } from 'primeng/primeng';
import { PaginatorModule, AccordionModule } from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    UserMgmtComponent,
    UserMgmtDetailComponent,
    UserModalService,
    UserMgmtSideBarComponent,
    UserMgmtHomeComponent,
    userMgmtRoute,
    DialogOverviewExampleDialog

} from './';

const ENTITY_STATES = [
    ...userMgmtRoute,
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
        DialogModule,
        DragDropModule,
        DataGridModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserMgmtComponent,
        UserMgmtDetailComponent,
        UserMgmtSideBarComponent,
        UserMgmtHomeComponent,
        DialogOverviewExampleDialog
    ],
    entryComponents: [
        UserMgmtSideBarComponent,
        UserMgmtHomeComponent,
        DialogOverviewExampleDialog
    ],
    providers: [
        UserModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1UserMgmtModule { }
