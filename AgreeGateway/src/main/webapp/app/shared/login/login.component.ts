import { Component, AfterViewInit, Renderer, ElementRef, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { CommonService} from '../../entities/common.service';
import { LoginService } from './login.service';
import { StateStorageService } from '../auth/state-storage.service';
import { NavBarService } from '../../layouts/navbar/navbar.service';
import { SessionStorageService } from 'ng2-webstorage'

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
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

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        public commonService: CommonService,
        private navBarService: NavBarService,
        private $sessionStorage: SessionStorageService
    ) {
        this.credentials = {};
    }

    ngAfterViewInit() {
        setTimeout(function () {
            document.getElementById('username').focus();
        }, 200);
        $('.login-page').css('height',this.commonService.screensize().height);
        $('.recBgImg').css('height',this.commonService.screensize().height);
    }

    ngOnInit(){
        $('body').addClass('login');
    }

    /* cancel() {
        this.credentials = {
            username: null,
            password: null,
            rememberMe: true
        };
        this.authenticationError = false;
    } */

    login() {
        this.loginService.login({
            username: this.username,
            password: this.password,
            rememberMe: this.rememberMe
        }).then(() => {
            this.authenticationError = false;
            this.router.navigate(['/']);
            this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
            });
            $('body').removeClass('login');
            this.navBarService.getNotificationList().subscribe((res: any)=>{
                this.navBarService.notificationlist = res;
            });
        }).catch(() => {
            this.authenticationError = true;
        });
    }

    /* login() {
        this.loginService.login({
            username: this.username,
            password: this.password,
            rememberMe: this.rememberMe
        }).then(() => {
            this.authenticationError = false;
            if (this.router.url === '/register' || (/activate/.test(this.router.url)) ||
                this.router.url === '/finishReset' || this.router.url === '/requestReset') {
                this.router.navigate(['']);
            }

            this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
            });
            const redirect = this.stateStorageService.getUrl();
            if (redirect) {
                this.router.navigate([redirect]);
            }
        }).catch(() => {
            this.authenticationError = true;
        });
    } */

    register() {
        this.router.navigate(['/register']);
    }

    requestResetPassword() {
        this.router.navigate(['/reset', 'request']);
    }
}