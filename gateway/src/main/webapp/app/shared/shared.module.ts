import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';
import { CookieService } from 'ngx-cookie';
import { AgreePlugInsSharedModule } from '../agree-plugins-shared.module';
import { AmountFormat } from "../entities/amountFormatPipe";
import { NgxImgModule } from 'ngx-img';
import { SearchFilterPipe } from '../entities/search-filter.pipe';
import { Attachment, AttachmentFailedDialog } from '../shared/components/attachment/attachment.component';
import { AttachmentService } from '../shared/components/attachment/attachment.service';
import {MultiSelectDropdownComponent, MultiSelectDropdownService,MultiSelectDropDownSettings} from '../shared/components/multiselect-dropdown';
import { RouterLinkActiveContainsDirective } from './directives/router-link-active-contains';
import { NumberDirective } from './directives/number-only.directive';
import { MaterialFilterDirective } from './directives/material-filter.directive';
import { DeleteConfirmationDirective, DeleteConfirmDialog } from './directives/delete-confirmation.directive';
/* import { JhiTrackerService } */
import {BulkUploadComponent,BulkUploadService,BulkUploadConfirmActionModalDialog} from '../shared/components/bulk-upload';
import {
    AgreeGatewayV1SharedLibsModule,
    AgreeGatewayV1SharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    /* JhiTrackerService, */
    HasAnyAuthorityDirective,

} from './';

@NgModule({
    imports: [
        AgreeGatewayV1SharedLibsModule,
        AgreeGatewayV1SharedCommonModule,
        AgreePlugInsSharedModule,
        NgxImgModule.forRoot()
    ],
    declarations: [
        HasAnyAuthorityDirective,
        AmountFormat,
        SearchFilterPipe,
        Attachment,
        MultiSelectDropdownComponent,
        BulkUploadComponent,
        BulkUploadConfirmActionModalDialog,
        DeleteConfirmDialog,
        AttachmentFailedDialog,
        RouterLinkActiveContainsDirective,
        NumberDirective,
        MaterialFilterDirective,
        DeleteConfirmationDirective
    ],
    providers: [
        CookieService,
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        /* JhiTrackerService, */
        AuthServerProvider,
        UserService,
        DatePipe,
        AmountFormat,
        AttachmentService,
        MultiSelectDropdownService,
        BulkUploadService
    ],
    entryComponents: [
        AttachmentFailedDialog,
        BulkUploadConfirmActionModalDialog,
        DeleteConfirmDialog
    ],
    exports: [
        AgreeGatewayV1SharedCommonModule,
        HasAnyAuthorityDirective,
        DatePipe,
        AmountFormat,
        DatePipe,
        NgxImgModule,
        SearchFilterPipe,
        Attachment,
        MultiSelectDropdownComponent,
        BulkUploadComponent,
        BulkUploadConfirmActionModalDialog,
        DeleteConfirmDialog,
        RouterLinkActiveContainsDirective,
        NumberDirective,
        MaterialFilterDirective,
        DeleteConfirmationDirective
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class AgreeGatewayV1SharedModule { }