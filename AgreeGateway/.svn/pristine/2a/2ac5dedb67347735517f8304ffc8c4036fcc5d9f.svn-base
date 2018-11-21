import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgreeGatewayV1SharedModule } from '../../shared';
import {
    LineCriteriaService,
    LineCriteriaPopupService,
    LineCriteriaComponent,
    LineCriteriaDetailComponent,
    LineCriteriaDialogComponent,
    LineCriteriaPopupComponent,
    LineCriteriaDeletePopupComponent,
    LineCriteriaDeleteDialogComponent,
    lineCriteriaRoute,
    lineCriteriaPopupRoute,
    LineCriteriaResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...lineCriteriaRoute,
    ...lineCriteriaPopupRoute,
];

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LineCriteriaComponent,
        LineCriteriaDetailComponent,
        LineCriteriaDialogComponent,
        LineCriteriaDeleteDialogComponent,
        LineCriteriaPopupComponent,
        LineCriteriaDeletePopupComponent,
    ],
    entryComponents: [
        LineCriteriaComponent,
        LineCriteriaDialogComponent,
        LineCriteriaPopupComponent,
        LineCriteriaDeleteDialogComponent,
        LineCriteriaDeletePopupComponent,
    ],
    providers: [
        LineCriteriaService,
        LineCriteriaPopupService,
        LineCriteriaResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1LineCriteriaModule {}
