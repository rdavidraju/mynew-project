import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { Ng2Webstorage } from 'ng2-webstorage';
import { MaterialModule } from '@angular/material';
import { AnimationMetadata } from '@angular/animations';
import { AgreeGatewayV1SharedModule, UserRouteAccessService } from './shared';
import { AgreeGatewayV1HomeModule } from './home/home.module';
// import { AgreeGatewayV1NavigationModule } from './entities/navigation-home/navigation.module';
import { AgreeGatewayV1AdminModule } from './admin/admin.module';
import { AgreeGatewayV1AccountModule } from './account/account.module';
import { AgreeGatewayV1EntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';
import { AgreePlugInsSharedModule } from './agree-plugins-shared.module';
import { SharedContentModule } from './shared-content/shared-content.module';
import { BrowserXhr,HttpModule} from '@angular/http';
import { NgProgressBrowserXhr,NgProgressModule} from 'ngx-progressbar';
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';
import { MomentModule } from 'angular2-moment'; 
import {OrderBy} from "./entities/orderByPipe";
import {WindowRef} from "./shared/tracker/window.service";
import {JhiTrackerService} from "./shared/tracker/tracker.service";

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent,
    JhiSideNavComponent,
    LoggedInUsersComponent,
    DialogOverviewMainDialog,
    NavBarService,
    ApprovedComponent,
    RejectedComponent
} from './layouts';
import { SidebarModule } from 'ng-sidebar';
import { BreadcrumbComponent } from './breadcrumb.component';
@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpModule,
        FormsModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        AgreeGatewayV1SharedModule,
        AgreeGatewayV1HomeModule,
        // AgreeGatewayV1NavigationModule,
        AgreeGatewayV1AdminModule,
        AgreeGatewayV1AccountModule,
        AgreeGatewayV1EntityModule,
        MaterialModule,
        AgreePlugInsSharedModule,
        SharedContentModule,
        SidebarModule.forRoot(),
        NgProgressModule,
        MomentModule,
        NgIdleKeepaliveModule.forRoot(),
        LayoutRoutingModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        BreadcrumbComponent,
        JhiSideNavComponent,
        LoggedInUsersComponent,
        DialogOverviewMainDialog,
        OrderBy,
        ApprovedComponent,
    RejectedComponent
    ],
      entryComponents: [
        DialogOverviewMainDialog
      ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService,
        JhiSideNavComponent,
        LoggedInUsersComponent,
        NavBarService,
        WindowRef,
        JhiTrackerService,
        { provide: BrowserXhr, useClass: NgProgressBrowserXhr},
    ],
    bootstrap: [ JhiMainComponent ]
})
export class AgreeGatewayV1AppModule {}
