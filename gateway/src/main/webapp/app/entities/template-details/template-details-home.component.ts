import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { JhiLanguageHelper, StateStorageService, Principal, LoginService } from '../../shared';
import { routerAnimation } from '../../shared/animations/routerAnimation';
import { TemplateDetails, TemplateDetailsBreadCrumbTitles } from './template-details.model';
import { TemplateDetailsService } from './template-details.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { CommonService } from '../common.service';
declare var $: any;
declare var jQuery: any

@Component({
    selector: 'template-details-home',
    templateUrl: './template-details-home.html',
    animations: [routerAnimation]
})
export class TemplateDetailsHomeComponent implements OnInit {
    public isClose = true;
    isVisibleA = false;
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private $storageService: StateStorageService,
        private loginService: LoginService,
        private principal: Principal,
        private router: Router,
        public commonService: CommonService,
    ) {
        this.isClose = true;
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {

        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
        if (routeSnapshot.data.breadcrumb === TemplateDetailsBreadCrumbTitles.templateDetailsNew || routeSnapshot.data.breadcrumb === TemplateDetailsBreadCrumbTitles.templateDetailsList) {
            this.isVisibleA = true;
        }else {
            this.isVisibleA = false;
        }
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
            $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }
    ngOnInit() {
        //  $(".split-example").css('height', (this.commonService.screensize().height - 161) + 'px');
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
            if (event instanceof RoutesRecognized) {
                let params = {};
                let destinationData = {};
                let destinationName = '';
                const destinationEvent = event.state.root.firstChild.children[0];
                if (destinationEvent !== undefined) {
                    params = destinationEvent.params;
                    destinationData = destinationEvent.data;
                    destinationName = destinationEvent.url[0].path;
                }
                const from = { name: this.router.url.slice(1) };
                const destination = { name: destinationName, data: destinationData };
                this.$storageService.storeDestinationState(destination, params, from);
            }
        });

    }
}