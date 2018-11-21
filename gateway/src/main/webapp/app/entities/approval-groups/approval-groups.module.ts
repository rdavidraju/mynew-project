import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {
    ApprovalGroupsService,
    ApprovalGroupsPopupService,
    ApprovalGroupsComponent,
    ApprovalGroupsDetailComponent,
    ApprovalGroupsDialogComponent,
    ApprovalGroupsPopupComponent,
    ApprovalGroupsDeletePopupComponent,
    ApprovalGroupsDeleteDialogComponent,
    ApprovalGroupsRoute,
    ApprovalGroupsResolvePagingParams,
    ApprovalGroupsHomeComponent,
    ApprovalGroupsSideBarComponent
} from './';

const ENTITY_STATES = [
    ...ApprovalGroupsRoute
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
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ApprovalGroupsComponent,
        ApprovalGroupsDetailComponent,
        ApprovalGroupsDialogComponent,
        ApprovalGroupsDeleteDialogComponent,
        ApprovalGroupsPopupComponent,
        ApprovalGroupsDeletePopupComponent,
        ApprovalGroupsHomeComponent,
        ApprovalGroupsSideBarComponent
    ],
    entryComponents: [
        ApprovalGroupsComponent,
        ApprovalGroupsDialogComponent,
        ApprovalGroupsPopupComponent,
        ApprovalGroupsDeleteDialogComponent,
        ApprovalGroupsDeletePopupComponent,
        ApprovalGroupsHomeComponent,
        ApprovalGroupsSideBarComponent
    ],
    providers: [
        ApprovalGroupsService,
        ApprovalGroupsPopupService,
        ApprovalGroupsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ApprovalGroupsModule {}
