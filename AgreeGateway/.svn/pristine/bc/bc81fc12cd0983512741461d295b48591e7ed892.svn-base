import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import {
    FileTemplateLinesService,
   // FileTemplateLinesPopupService,
   // FileTemplateLinesComponent,
   // FileTemplateLinesDetailComponent,
   // FileTemplateLinesDialogComponent,
   // FileTemplateLinesPopupComponent,
   // FileTemplateLinesDeletePopupComponent,
   // FileTemplateLinesDeleteDialogComponent,
    fileTemplateLinesRoute,
    fileTemplateLinesPopupRoute,
    FileTemplateLinesResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...fileTemplateLinesRoute,
    ...fileTemplateLinesPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
       // FileTemplateLinesComponent,
        //FileTemplateLinesDetailComponent,
        //FileTemplateLinesDialogComponent,
       // FileTemplateLinesDeleteDialogComponent,
      //  FileTemplateLinesPopupComponent,
        //FileTemplateLinesDeletePopupComponent,
    ],
    entryComponents: [
      //  FileTemplateLinesComponent,
       // FileTemplateLinesDialogComponent,
        //FileTemplateLinesPopupComponent,
        //FileTemplateLinesDeleteDialogComponent,
       // FileTemplateLinesDeletePopupComponent,
    ],
    providers: [
        FileTemplateLinesService,
       // FileTemplateLinesPopupService,
        FileTemplateLinesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1FileTemplateLinesModule {}
