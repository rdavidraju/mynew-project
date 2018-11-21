import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import {OverlayContainer} from '@angular/material';

import { JhiLanguageHelper, StateStorageService, Principal, LoginService, notificationOptions } from '../../shared';

import { JhiSideNavComponent } from './sidebar.component';
import { CommonService } from '../../entities/common.service';
import {Idle, DEFAULT_INTERRUPTSOURCES} from '@ng-idle/core';
import {Keepalive} from '@ng-idle/keepalive';
import { PopoverModule } from "ngx-popover";
import { NguiMessagePopupComponent, NguiPopupComponent} from '@ngui/popup';
import { NguiPopupModule } from '@ngui/popup';
import 'jquery';
import { LocalStorageService } from 'ng2-webstorage';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import {routerTransition} from './route.transition';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html',
    animations: [routerTransition]
})
export class JhiMainComponent implements OnInit {
    @ViewChild(NguiPopupComponent) popup: NguiPopupComponent;
    isNavbarCollapsed: boolean;
    idleState = 'Not started.';
    timedOut = false;
    lastPing?: Date = null;
    opened = false;
    paddingVal = "0";
    notificationOptions: any;
    animal: string;
    name: string;
    count:any;
    value:any;
    timeOut:any;
    idletime:any;
    timeOutValue:any;
    
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private $storageService: StateStorageService,
        private loginService: LoginService,
        private principal: Principal,
        private router: Router,
        private sideNavBar: JhiSideNavComponent,
        public commonService: CommonService,
        private idle: Idle, private keepalive: Keepalive,
        public dialog: MdDialog,
        private overlayContainer: OverlayContainer,
        private $localStorage: LocalStorageService,
    ) {
        
        this.notificationOptions = notificationOptions;
        
        // Ng Idle value declaration. Need to change from service Value.
        this.timeOut = 30;
        this.idletime = 1000;
        
        // sets an idle timeout of 5 seconds, for testing purposes.
        idle.setIdle(this.idletime);
        // // sets a timeout period of 5 seconds. after 10 seconds of inactivity, the user will be considered timed out.
        idle.setTimeout(this.timeOut);
        // // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
        idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

        idle.onIdleEnd.subscribe(() => this.dialog.closeAll());
        idle.onTimeout.subscribe(() => this.logout());    
        idle.onTimeoutWarning.subscribe((countdown) => this.idlelogout(countdown));
        this.reset();
        this.isAuthenticated();
    }
    isAuthenticated() {
        if (this.principal.isAuthenticated()) {
            this.opened = true;
            $(".page-content-wrapper").removeClass('home-page-wrapper');
            $('body').removeClass('home-page-before-login');
        } else {
            this.opened = false;
            $(".page-content-wrapper").addClass('home-page-wrapper');
            $('body').addClass('home-page-before-login');
        }
    }
//     public getRouterOutletState(outlet) {
//   return outlet.isActivated ? outlet.activatedRoute : '';
// }
    idlelogout(countdown){
        
           if (this.principal.isAuthenticated()) {         
            let dialogRef = this.dialog.open(DialogOverviewMainDialog, {
                width: '300px',
                data: { name: this.name, count: countdown, timeOutValue: (countdown/this.timeOut)*100}
            });
          
            if(countdown==this.timeOut){
            
          }
          

        //    dialogRef.afterClosed().subscribe(result => {
        //     console.log('The dialog was closed');
        //     this.count = result;
        //   }); 
    
            // this.popup.open(NguiMessagePopupComponent, {
            //     title: 'Idle State',
            //     message: 'You will time out in ' + countdown + ' seconds!',
            // })
           // this.idle.watch();
        }
    }
    logout() {
        if (this.principal.isAuthenticated()) {
            this.loginService.logout();
            this.router.navigate(['']);
            //this.idle.watch();
            location.reload();
        } 
    }

    reset() {
        this.idle.watch();
        this.idleState = 'Started.';
        this.timedOut = false;
    }
    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngAfterViewInit() {
        jQuery('button').click();
    }

    ngOnInit() {
        $(".spinner-icon").css('width','50px');
        //For Footer
        $(".page-content").css('min-height',(window.innerHeight - 95)+'px');

        if(this.$localStorage.retrieve('key-theme') != null || undefined || ''){
            this.overlayContainer.themeClass = this.$localStorage.retrieve('key-theme');
        } else {
            this.overlayContainer.themeClass = 'blue-skies-theme';
        }
        // console.log('theme class:'+this.overlayContainer.themeClass);
        $(".main-compo").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 118) + 'px'
        });

        //for footer
        $(window).resize(function(){
            $(".page-content").css('min-height',(window.innerHeight - 95)+'px');
        });
        
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
            if (event instanceof RoutesRecognized) {
                let params = {};
                let destinationData = {};
                let destinationName = '';
                let destinationEvent = event.state.root.firstChild.children[0];
                if (destinationEvent !== undefined) {
                    params = destinationEvent.params;
                    destinationData = destinationEvent.data;
                    destinationName = destinationEvent.url[0].path;
                }
                let from = { name: this.router.url.slice(1) };
                let destination = { name: destinationName, data: destinationData };
                this.$storageService.storeDestinationState(destination, params, from);
            }
        });
    }
    
}

@Component({
    selector: 'main.dialog',
    templateUrl: 'main.dialog.html',
})
export class DialogOverviewMainDialog {
    constructor(
        public dialogRef: MdDialogRef<DialogOverviewMainDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) { }

    onNoClick(): void {
        this.dialogRef.close();
    }


}