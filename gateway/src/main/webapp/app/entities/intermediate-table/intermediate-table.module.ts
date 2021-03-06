import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    IntermediateTableService,
   // IntermediateTablePopupService,
   // IntermediateTableComponent,
   // IntermediateTableDetailComponent,
   // IntermediateTableDialogComponent,
   // IntermediateTablePopupComponent,
    //IntermediateTableDeletePopupComponent,
    //IntermediateTableDeleteDialogComponent,
    intermediateTableRoute,
    intermediateTablePopupRoute,
} from './';

const ENTITY_STATES = [
    ...intermediateTableRoute,
    ...intermediateTablePopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
       // IntermediateTableComponent,
        //IntermediateTableDetailComponent,
        //IntermediateTableDialogComponent,
       // IntermediateTableDeleteDialogComponent,
       // IntermediateTablePopupComponent,
        //IntermediateTableDeletePopupComponent,
    ],
    entryComponents: [
      //  IntermediateTableComponent,
       // IntermediateTableDialogComponent,
       // IntermediateTablePopupComponent,
       // IntermediateTableDeleteDialogComponent,
       // IntermediateTableDeletePopupComponent,
    ],
    providers: [
        IntermediateTableService
       // IntermediateTablePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1IntermediateTableModule {}
