import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import {MdAutocompleteModule} from '@angular/material';
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';
import { NguiAutoCompleteModule } from '@ngui/auto-complete';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
    RuleGroupService,
    RuleGroupPopupService,
    RuleGroupComponent,
    RuleGroupDetailComponent,
    RuleGroupDialogComponent,
    RuleGroupPopupComponent,
    RuleGroupDeletePopupComponent,
    RuleGroupAccountingComponent,
    RuleGroupAccountingNewComponent,
    ruleGroupRoute,
    RuleGroupHomeComponent,
    RuleGroupEditComponent,
    RuleGroupSideBarComponent,
    RuleGroupResolvePagingParams,
    RuleGroupReconciliationComponent,
    RuleGroupApprovalsComponent,
    ConfirmActionModalDialog,
    AccountingConfirmActionModalDialog,
    ReconConfirmActionModalDialog,
    ApprovalsConfirmActionModalDialog,
    BulkUploadModalDialog
} from './';
import { NguiPopupModule } from '@ngui/popup';
import {AgreeGatewayV1FileTemplatesModule} from '../file-templates/file-templates.module';
let ENTITY_STATES = [
    ...ruleGroupRoute
];

@NgModule({
    imports: [
        ScrollToModule.forRoot(),
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        PaginatorModule,
        AccordionModule,
        MdAutocompleteModule,
        NguiAutoCompleteModule,
        NguiPopupModule,
        AgreeGatewayV1FileTemplatesModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        BrowserAnimationsModule
    ],
    exports: [
              RuleGroupAccountingComponent,
              RuleGroupAccountingNewComponent,
              RuleGroupReconciliationComponent,
              RuleGroupApprovalsComponent
              ],
    declarations: [
        RuleGroupComponent,
        RuleGroupDetailComponent,
        RuleGroupDialogComponent,
        RuleGroupAccountingComponent,
        RuleGroupAccountingNewComponent,
        RuleGroupDeletePopupComponent,
        RuleGroupHomeComponent,
        RuleGroupEditComponent,
        RuleGroupSideBarComponent,
        RuleGroupPopupComponent,
        RuleGroupReconciliationComponent,
        RuleGroupApprovalsComponent,
        ConfirmActionModalDialog,
        AccountingConfirmActionModalDialog,
        ReconConfirmActionModalDialog,
        ApprovalsConfirmActionModalDialog,
        BulkUploadModalDialog
    ],
    entryComponents: [
        RuleGroupComponent,
        RuleGroupDialogComponent,
        RuleGroupAccountingComponent,
        RuleGroupDeletePopupComponent,
        RuleGroupHomeComponent,
        RuleGroupEditComponent,
        RuleGroupSideBarComponent,
        RuleGroupPopupComponent,
        RuleGroupReconciliationComponent,
        RuleGroupApprovalsComponent,
        ConfirmActionModalDialog,
        AccountingConfirmActionModalDialog,
        ReconConfirmActionModalDialog,
        ApprovalsConfirmActionModalDialog,
        BulkUploadModalDialog,
        
        ],
    providers: [
        RuleGroupService,
        RuleGroupPopupService,
        RuleGroupResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1RuleGroupModule {}
