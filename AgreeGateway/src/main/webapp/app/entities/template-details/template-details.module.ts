import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule, CheckboxModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {
    TemplateDetailsService,
    TemplateDetailsPopupService,
    TemplateDetailsComponent,
    TemplateDetailsDetailComponent,
    TemplateDefinitionDetailComponent,
    TemplateDetailsDialogComponent,
    TemplateDetailsPopupComponent,
    TemplateDetailsDeletePopupComponent,
    TemplateDetailsDeleteDialogComponent,
    templateDetailsRoute,
  //  templateDetailsPopupRoute,
    TemplateDetailsResolvePagingParams,
    TemplateDetailsSideBarComponent,
    TemplateDetailsHomeComponent,
    
} from './';

const ENTITY_STATES = [
    ...templateDetailsRoute,
   // ...templateDetailsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        DataTableModule,
        SharedModule,
        MultiSelectModule,
        PaginatorModule,
        CheckboxModule,
        AccordionModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TemplateDetailsComponent,
        TemplateDetailsDetailComponent,
        TemplateDefinitionDetailComponent,
        TemplateDetailsDialogComponent,
        TemplateDetailsDeleteDialogComponent,
        TemplateDetailsPopupComponent,
        TemplateDetailsDeletePopupComponent,
        TemplateDetailsSideBarComponent,
        TemplateDetailsHomeComponent,
    ],
    entryComponents: [
        TemplateDetailsComponent,
        TemplateDetailsDialogComponent,
        TemplateDetailsPopupComponent,
        TemplateDetailsDeleteDialogComponent,
        TemplateDetailsDeletePopupComponent,
        TemplateDetailsSideBarComponent,
        TemplateDetailsHomeComponent,
    ],
    providers: [
        TemplateDetailsService,
        TemplateDetailsPopupService,
        TemplateDetailsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1TemplateDetailsModule {}
