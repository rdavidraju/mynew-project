import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    RolesService,
    RolesPopupService,
    RolesComponent,
    RolesDetailComponent,
    RolesDialogComponent,
    RolesPopupComponent,
    RolesDeletePopupComponent,
    RolesDeleteDialogComponent,
    rolesRoute,
    RolesHomeComponent,
    RolesSideBarComponent,
    RolesModalDialog

} from './';

const ENTITY_STATES = [
    ...rolesRoute,
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
        RolesComponent,
        RolesHomeComponent,
        RolesSideBarComponent,
        RolesDetailComponent,
        RolesDialogComponent,
        RolesDeleteDialogComponent,
        RolesPopupComponent,
        RolesDeletePopupComponent,
        RolesModalDialog
    ],
    entryComponents: [
        RolesComponent,
        RolesHomeComponent,
        RolesSideBarComponent,
        RolesDialogComponent,
        RolesPopupComponent,
        RolesDeleteDialogComponent,
        RolesDeletePopupComponent,
        RolesModalDialog
    ],
    providers: [
        RolesService,
        RolesPopupService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1RolesModule {}
