import { Component, AfterViewInit, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { CommonService} from '../../entities/common.service';
import { LoginService } from './login.service';
import { StateStorageService } from '../auth/state-storage.service';
import { NavBarService } from '../../layouts/navbar/navbar.service';
import { Principal} from '../../shared';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-login-modal',
    templateUrl: './login.component.html'
})
export class JhiLoginComponent implements AfterViewInit, OnInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    isActivated = false;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private router: Router,
        public commonService: CommonService,
        private navBarService: NavBarService,
        private principal: Principal,
    ) {
        this.credentials = {};
    }

    ngAfterViewInit() {
        $('.recBgImg').css('height',this.commonService.screensize().height);
    }

    ngOnInit(){
        if(this.commonService.currentAccount) {
            this.router.navigate(['/kpiboard']);
            return;
        };
        $('body').addClass('login');
    }

    login() {
        this.loginService.login({
            username: this.username,
            password: this.password,
            rememberMe: this.rememberMe
        }).then(() => {
            this.authenticationError = false;
            this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
            });
            $('body').removeClass('login');
            this.principal.identity().then((account) => {
                this.commonService.currentAccount = account;
            });
            this.navBarService.getNotificationList().subscribe((res: any)=>{
                this.navBarService.notificationlist = res;
            });
            this.navBarService.loginDetect();
            this.navBarService.loggedInUsersService().subscribe((res: any) => {
                this.navBarService.loggedInUsersList = res;
                for (let i = 0; i < this.navBarService.loggedInUsersList.length; i++) {
                    const user = this.navBarService.loggedInUsersList[i];
                    user['firstLetter'] = user.firstName.charAt(0); 
                }
            });
            const redirect = this.stateStorageService.getUrl();
            if (redirect) {
                const parentRoute = (redirect.split('(')[0]).substring(1, (redirect.split('(')[0]).length - 1);
                const childRoute = (redirect.split('(')[1]).substring(0, (redirect.split('(')[1]).length - 1);
                this.router.navigate([`/${parentRoute}`, { outlets: { 'content': (childRoute.split(':')[1]).split('/') } }]);
                this.stateStorageService.clearUrl();
            } else {
                this.router.navigate(['/kpiboard']);
            }
        }).catch((err) => {
            if (err) {
                this.isActivated = err.json().AuthenticationException.includes('was not activated');
                this.authenticationError = !this.isActivated;
            } else {
                this.authenticationError = true;
            }
        });
    }

    register() {
        this.router.navigate(['/register']);
    }

    requestResetPassword() {
        this.router.navigate(['/reset', 'request']);
    }
}
