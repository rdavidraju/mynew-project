import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule,PaginatorModule,AccordionModule, CheckboxModule,TreeModule,TreeNode} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { SidebarModules } from '../../shared/primeng/primeng';
import {
    NotificationBatchService,
    NotificationBatchPopupService,
    NotificationBatchComponent,
    NotificationBatchDetailComponent,
    NotificationBatchDialogComponent,
    NotificationBatchPopupComponent,
    NotificationBatchDeletePopupComponent,
    NotificationBatchDeleteDialogComponent,
    notificationBatchRoute,
    // notificationBatchPopupRoute,
    NotificationBatchResolvePagingParams,
    NotificationBatchHomeComponent,
    NotificationBatchSideBarComponent,

} from './';

const ENTITY_STATES = [
    ...notificationBatchRoute,
    // ...notificationBatchPopupRoute,
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
        CheckboxModule,
        SidebarModules,
        TreeModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        NotificationBatchComponent,
        NotificationBatchDetailComponent,
        NotificationBatchDialogComponent,
        NotificationBatchDeleteDialogComponent,
        NotificationBatchPopupComponent,
        NotificationBatchDeletePopupComponent,
        NotificationBatchHomeComponent,
        NotificationBatchSideBarComponent,
    ],
    entryComponents: [
        NotificationBatchComponent,
        NotificationBatchDialogComponent,
        NotificationBatchPopupComponent,
        NotificationBatchDeleteDialogComponent,
        NotificationBatchDeletePopupComponent,
        NotificationBatchHomeComponent,
        NotificationBatchSideBarComponent,
    ],
    providers: [
        NotificationBatchService,
        NotificationBatchPopupService,
        NotificationBatchResolvePagingParams,
        NotificationBatchSideBarComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1NotificationBatchModule {}
