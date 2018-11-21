import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {DataTableModule,SharedModule, MultiSelectModule,PaginatorModule,AccordionModule} from 'primeng/primeng';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { CommonService } from '../common.service';
import {
    SourceConnectionDetailsService,
    SourceConnectionDetailsPopupService,
    SourceConnectionDetailsComponent,
    SourceConnectionDetailsDetailComponent,
    SourceConnectionDetailsDialogComponent,
    SourceConnectionDetailsPopupComponent,
    SourceConnectionDetailsDeletePopupComponent,
    SourceConnectionDetailsDeleteDialogComponent,
    sourceConnectionDetailsRoute,
    SourceConnectionDetailsResolvePagingParams,
    SourceConnectionDetailsHomeComponent,
    ConnectionSideBarComponent,
} from './';

const ENTITY_STATES = [
    ...sourceConnectionDetailsRoute,
    // ...sourceConnectionDetailsPopupRoute,
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
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SourceConnectionDetailsComponent,
        SourceConnectionDetailsDetailComponent,
        SourceConnectionDetailsDialogComponent,
        SourceConnectionDetailsDeleteDialogComponent,
        SourceConnectionDetailsPopupComponent,
        SourceConnectionDetailsDeletePopupComponent,
        SourceConnectionDetailsHomeComponent,
        ConnectionSideBarComponent,
    ],
    entryComponents: [
        SourceConnectionDetailsComponent,
        SourceConnectionDetailsDialogComponent,
        SourceConnectionDetailsPopupComponent,
        SourceConnectionDetailsDeleteDialogComponent,
        SourceConnectionDetailsDeletePopupComponent,
         SourceConnectionDetailsHomeComponent,
        ConnectionSideBarComponent,
    ],
    providers: [
        SourceConnectionDetailsService,
        SourceConnectionDetailsPopupService,
        SourceConnectionDetailsResolvePagingParams,
        CommonService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1SourceConnectionDetailsModule {}
