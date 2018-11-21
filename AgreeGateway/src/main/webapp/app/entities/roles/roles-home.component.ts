import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { JhiLanguageHelper, StateStorageService, Principal, LoginService } from '../../shared';
import { RolesBreadCrumbTitles } from './roles.model';
import { Roles } from './roles.model';
import { RolesService } from './roles.service';
import { CommonService } from '../../entities/common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any

@Component( {
    selector: 'roles-home',
    templateUrl: './roles-home.component.html'
} )
export class RolesHomeComponent implements OnInit {
        public isClose: boolean = true;
        isVisibleA:boolean =false;
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private $storageService: StateStorageService,
        private loginService: LoginService,
        private principal: Principal,
        private router: Router,
        private commonService: CommonService,
        private rolesModalService: RolesService
    ) {
        this.isClose = true;    
    }

    private getPageTitle( routeSnapshot: ActivatedRouteSnapshot ) {

        let title: string = ( routeSnapshot.data && routeSnapshot.data['pageTitle'] ) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
        if ( routeSnapshot.data.breadcrumb === RolesBreadCrumbTitles.rolesNew) {
            this.isVisibleA = true;
        }
        else {
            this.isVisibleA = false;
        }
        if ( routeSnapshot.firstChild ) {
            title = this.getPageTitle( routeSnapshot.firstChild ) || title;
        }
        return title;
    }
    
    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }
    ngOnInit() {
        $(".split-example").css('min-height', (this.commonService.screensize().height - 161) + 'px');
        this.router.events.subscribe(( event ) => {
            if ( event instanceof NavigationEnd ) {
                this.jhiLanguageHelper.updateTitle( this.getPageTitle( this.router.routerState.snapshot.root ) );
            }
            if ( event instanceof RoutesRecognized ) {
                let params = {};
                let destinationData = {};
                let destinationName = '';
                let destinationEvent = event.state.root.firstChild.children[0];
                if ( destinationEvent !== undefined ) {
                    params = destinationEvent.params;
                    destinationData = destinationEvent.data;
                    destinationName = destinationEvent.url[0].path;
                }
                let from = { name: this.router.url.slice( 1 ) };
                let destination = { name: destinationName, data: destinationData };
                this.$storageService.storeDestinationState( destination, params, from );
            }
        } );

    }
}