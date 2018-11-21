import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    SourceFileInbHistoryService,
    //SourceFileInbHistoryPopupService,
    //SourceFileInbHistoryComponent,
    //SourceFileInbHistoryDetailComponent,
    //SourceFileInbHistoryDialogComponent,
    //SourceFileInbHistoryPopupComponent,
   // SourceFileInbHistoryDeletePopupComponent,
   // SourceFileInbHistoryDeleteDialogComponent,
    sourceFileInbHistoryRoute,
    sourceFileInbHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...sourceFileInbHistoryRoute,
    ...sourceFileInbHistoryPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
       // SourceFileInbHistoryComponent,
        //SourceFileInbHistoryDetailComponent,
       // SourceFileInbHistoryDialogComponent,
       // SourceFileInbHistoryDeleteDialogComponent,
       // SourceFileInbHistoryPopupComponent,
       // SourceFileInbHistoryDeletePopupComponent,
    ],
    entryComponents: [
        //SourceFileInbHistoryComponent,
       // SourceFileInbHistoryDialogComponent,
      //  SourceFileInbHistoryPopupComponent,
       // SourceFileInbHistoryDeleteDialogComponent,
       // SourceFileInbHistoryDeletePopupComponent,
    ],
    providers: [
        SourceFileInbHistoryService,
       // SourceFileInbHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1SourceFileInbHistoryModule {}
