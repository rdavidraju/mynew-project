import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    DataViewsColumnsService,
   // DataViewsColumnsPopupService,
   // DataViewsColumnsComponent,
   // DataViewsColumnsDetailComponent,
   // DataViewsColumnsDialogComponent,
   // DataViewsColumnsPopupComponent,
   // DataViewsColumnsDeletePopupComponent,
   // DataViewsColumnsDeleteDialogComponent,
    dataViewsColumnsRoute,
    dataViewsColumnsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...dataViewsColumnsRoute,
    ...dataViewsColumnsPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        //DataViewsColumnsComponent,
        //DataViewsColumnsDetailComponent,
       // DataViewsColumnsDialogComponent,
        //DataViewsColumnsDeleteDialogComponent,
       // DataViewsColumnsPopupComponent,
        //DataViewsColumnsDeletePopupComponent,
    ],
    entryComponents: [
       // DataViewsColumnsComponent,
       // DataViewsColumnsDialogComponent,
       // DataViewsColumnsPopupComponent,
       // DataViewsColumnsDeleteDialogComponent,
        //DataViewsColumnsDeletePopupComponent,
    ],
    providers: [
        DataViewsColumnsService,
       // DataViewsColumnsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1DataViewsColumnsModule {}
