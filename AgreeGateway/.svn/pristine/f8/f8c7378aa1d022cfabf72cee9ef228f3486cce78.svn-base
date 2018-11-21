import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgreePlugInsSharedModule } from '../../agree-plugins-shared.module';
import { AgreeGatewayV1SharedModule } from '../../shared';
import { DndModule } from 'ng2-dnd';
import {
    ExpressModeComponent,
    ExpressModeService,
    reportsRoute,
    FileDropComponent,
    // ConfirmActionModalDialog
} from './';
import {StepsModule} from 'primeng/primeng';

@NgModule({
    imports: [
        AgreeGatewayV1SharedModule,
        AgreePlugInsSharedModule,
        StepsModule,
        DndModule,
        RouterModule.forRoot([...reportsRoute], { useHash: true })
    ],
    declarations: [
        ExpressModeComponent,
        FileDropComponent,
        // ConfirmActionModalDialog
    ],
    entryComponents: [
        ExpressModeComponent,
        // ConfirmActionModalDialog
    ],
    providers: [
        ExpressModeService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreeGatewayV1ExpressModeModule {}
