import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule} from 'primeng/primeng';
import {PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { SidebarModules } from '../../shared/primeng/primeng';
import {
    JournalsHeaderDataService,
    JournalsHeaderDataPopupService,
    JournalsHeaderDataComponent,
    JournalsHeaderDataWQComponent,
    JournalsHeaderDataDetailComponent,
    JournalsHeaderDataDialogComponent,
    JournalsHeaderDataPopupComponent,
    JournalsHeaderDataDeletePopupComponent,
    JournalsHeaderDataDeleteDialogComponent,
    journalsHeaderDataRoute,
    /* journalsHeaderDataPopupRoute, */
    JournalsHeaderDataResolvePagingParams,
    JournalsHeaderDataHomeComponent,
    JournalsHeaderDataSideBarComponent,
} from './';

const ENTITY_STATES = [
    ...journalsHeaderDataRoute,
    /* ...journalsHeaderDataPopupRoute, */
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        DataTableModule,
        SharedModule,
        MultiSelectModule,
        PaginatorModule,
        AccordionModule,
        SidebarModules,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JournalsHeaderDataComponent,
        JournalsHeaderDataWQComponent,
        JournalsHeaderDataDetailComponent,
        JournalsHeaderDataDialogComponent,
        JournalsHeaderDataDeleteDialogComponent,
        JournalsHeaderDataPopupComponent,
        JournalsHeaderDataDeletePopupComponent,
        JournalsHeaderDataHomeComponent,
        JournalsHeaderDataSideBarComponent,
    ],
    entryComponents: [
        JournalsHeaderDataComponent,
        JournalsHeaderDataWQComponent,
        JournalsHeaderDataDialogComponent,
        JournalsHeaderDataPopupComponent,
        JournalsHeaderDataDeleteDialogComponent,
        JournalsHeaderDataDeletePopupComponent,
        JournalsHeaderDataHomeComponent,
        JournalsHeaderDataSideBarComponent,
    ],
    providers: [
        JournalsHeaderDataService,
        JournalsHeaderDataPopupService,
        JournalsHeaderDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1JournalsHeaderDataModule {}
