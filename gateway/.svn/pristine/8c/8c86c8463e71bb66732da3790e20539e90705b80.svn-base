import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {
    DataViewsService,
    DataViewsPopupService,
    DataViewsComponent,
    DataViewsDetailComponent,
    dataViewsRoute,
    DataViewHomeComponent,
    DataViewsSideBarComponent,
    DVConfirmationDialogComponent    
} from './';

const ENTITY_STATES = [
    ...dataViewsRoute
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
        DataViewsComponent,
        DataViewsDetailComponent,
        DataViewHomeComponent,
        DataViewsSideBarComponent,
        DVConfirmationDialogComponent
    ],
    entryComponents: [
        DataViewsComponent,
        DataViewHomeComponent,
        DataViewsSideBarComponent,
        DVConfirmationDialogComponent
    ],
    providers: [
        DataViewsService,
        DataViewsPopupService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1DataViewsModule {}
