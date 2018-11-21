import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';
import { CookieService } from 'ngx-cookie';
import { AgreePlugInsSharedModule } from '../agree-plugins-shared.module';
/* import { JhiTrackerService } */
import {
    AgreeGatewayV1SharedLibsModule,
    AgreeGatewayV1SharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    /* JhiTrackerService, */
    HasAnyAuthorityDirective,
    
} from './';

@NgModule({
    imports: [
        AgreeGatewayV1SharedLibsModule,
        AgreeGatewayV1SharedCommonModule,
        AgreePlugInsSharedModule
    ],
    declarations: [
        HasAnyAuthorityDirective,
    ],
    providers: [
        CookieService,
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        /* JhiTrackerService, */
        AuthServerProvider,
        UserService,
        DatePipe,
        
    ],
    entryComponents: [],
    exports: [
        AgreeGatewayV1SharedCommonModule,
        HasAnyAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class AgreeGatewayV1SharedModule {}