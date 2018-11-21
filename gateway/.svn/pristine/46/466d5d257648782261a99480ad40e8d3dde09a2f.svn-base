import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {PaginatorModule,AccordionModule,Chips} from 'primeng/primeng';
import {sourceProfileFileAssignmentsPopupRoute} from '../source-profile-file-assignments/'; 
import { DndModule } from 'ng2-dnd';
import { AgreeGatewayV1SharedModule } from '../../shared';
//import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import {
    FileTemplatesService,
    FileTemplatesPopupService,
    FileTemplatesComponent,
   // FileTemplatesDetailComponent,
   // FileTemplatesDialogComponent,
   // FileTemplatesPopupComponent,
   // FileTemplatesDeletePopupComponent,
   // FileTemplatesDeleteDialogComponent,
    fileTemplatesRoute,
    FileTemplatesResolvePagingParams,
    FileTemplatesNavComponent,
    FileTemplatesDropZoneComponent,
    FileTemplatesEditComponent,
    FileTemplatesHomeComponent,
    ConfirmActionModalDialog,
    SideBarComponent
} from './';
import {CommonService} from '../common.service';
const ENTITY_STATES = [
    ...fileTemplatesRoute,
    ...sourceProfileFileAssignmentsPopupRoute
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,        
        DataTableModule,
        AccordionModule,
        SharedModule,
        DndModule,
        MultiSelectModule,
        PaginatorModule,
        AgreePlugInsSharedModule,
        AccordionModule,
     //   MultiselectDropdownModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    exports: [
FileTemplatesEditComponent
              ],
    declarations: [
        FileTemplatesComponent,
        //FileTemplatesDetailComponent,
        //FileTemplatesDialogComponent,
       // FileTemplatesDeleteDialogComponent,
       // FileTemplatesPopupComponent,
       // FileTemplatesDeletePopupComponent,
        SideBarComponent,
        FileTemplatesNavComponent,
        FileTemplatesDropZoneComponent,
        FileTemplatesEditComponent,
        FileTemplatesHomeComponent,
        ConfirmActionModalDialog
    ],
    entryComponents: [
        FileTemplatesComponent,
       // FileTemplatesDialogComponent,
       // FileTemplatesPopupComponent,
        //FileTemplatesDeleteDialogComponent,
       // FileTemplatesDeletePopupComponent,
        SideBarComponent,
        FileTemplatesNavComponent,
        FileTemplatesDropZoneComponent,
        FileTemplatesEditComponent,
        FileTemplatesHomeComponent,
        ConfirmActionModalDialog
    ],
    providers: [
        FileTemplatesService,
        FileTemplatesPopupService,
        FileTemplatesResolvePagingParams,
        CommonService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1FileTemplatesModule {}
