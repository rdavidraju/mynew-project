import { Component, OnInit, AfterViewInit, ViewChild, Input, ElementRef } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { BaseChartDirective } from 'ng2-charts/ng2-charts';
import { Account, LoginModalService, Principal } from '../shared';
import { SidebarModules, OverlayPanelModule } from '../shared/primeng/primeng';
import { DashBoard } from './home.model';
import { CommonService } from '../entities/common.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe, NgSwitch } from '@angular/common';
import { JhiDateUtils } from 'ng-jhipster';
import { trigger, state, style, animate, transition } from '@angular/animations';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import Chart from 'chart.js';
import { UserService } from '../shared/user/user.service'
import { NavBarService } from '../layouts/navbar/navbar.service';
import { SessionStorageService } from 'ng2-webstorage';
import { NotificationsService } from 'angular2-notifications-lite';
declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]
})
export class HomeComponent implements OnInit, AfterViewInit {
    @ViewChild(BaseChartDirective) chart: BaseChartDirective;
    public timeOut = 100;
    account: Account;
    modalRef: NgbModalRef;
    step = 0;
    @ViewChild('vdo') vdo: ElementRef;
    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private commonService: CommonService,
        private config: NgbDatepickerConfig,
        private datePipe: DatePipe,
        private dateUtils: JhiDateUtils,
        private userService: UserService,
        private navbarService: NavBarService,
        private $sessionStorage: SessionStorageService,
        private notificationService: NotificationsService,
    ) { }

    ngOnInit() {
        this.registerAuthenticationSuccess();
    }

    ngAfterViewInit() {
        if (this.vdo && this.vdo.nativeElement.paused) {
            this.vdo.nativeElement.play();
        }
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}