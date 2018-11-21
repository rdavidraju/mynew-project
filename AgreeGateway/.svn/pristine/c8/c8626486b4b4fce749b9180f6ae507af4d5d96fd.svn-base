import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    ApprovalGroupMembersService,
    ApprovalGroupMembersPopupService,
    ApprovalGroupMembersComponent,
    ApprovalGroupMembersDetailComponent,
    ApprovalGroupMembersDialogComponent,
    ApprovalGroupMembersPopupComponent,
    ApprovalGroupMembersDeletePopupComponent,
    ApprovalGroupMembersDeleteDialogComponent,
    approvalGroupMembersRoute,
    approvalGroupMembersPopupRoute,
    ApprovalGroupMembersResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...approvalGroupMembersRoute,
    ...approvalGroupMembersPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ApprovalGroupMembersComponent,
        ApprovalGroupMembersDetailComponent,
        ApprovalGroupMembersDialogComponent,
        ApprovalGroupMembersDeleteDialogComponent,
        ApprovalGroupMembersPopupComponent,
        ApprovalGroupMembersDeletePopupComponent,
    ],
    entryComponents: [
        ApprovalGroupMembersComponent,
        ApprovalGroupMembersDialogComponent,
        ApprovalGroupMembersPopupComponent,
        ApprovalGroupMembersDeleteDialogComponent,
        ApprovalGroupMembersDeletePopupComponent,
    ],
    providers: [
        ApprovalGroupMembersService,
        ApprovalGroupMembersPopupService,
        ApprovalGroupMembersResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ApprovalGroupMembersModule {}
