import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { JhiLanguageHelper, StateStorageService, Principal, LoginService } from '../../shared';
import { ReportsBreadCrumbTitles } from './reports.model';
import { CommonService } from '../common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any

@Component( {
    selector: 'jhi-reports-home',
    templateUrl: './reports-home.component.html'
} )
export class ReportsHomeComponent implements OnInit {
     isVisibleA:boolean = true;
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private $storageService: StateStorageService,
        private loginService: LoginService,
        private principal: Principal,
        private router: Router,
        private cs: CommonService
    ) {
    }

    private getPageTitle( routeSnapshot: ActivatedRouteSnapshot ) {

        let title: string = ( routeSnapshot.data && routeSnapshot.data['pageTitle'] ) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
        if ( routeSnapshot.firstChild ) {
            title = this.getPageTitle( routeSnapshot.firstChild ) || title;
        }
        return title;
    }

    ngOnInit() {
        
        this.router.events.subscribe(( event ) => {
            if ( event instanceof NavigationEnd ) {
                this.jhiLanguageHelper.updateTitle( this.getPageTitle( this.router.routerState.snapshot.root ) );
            }
            if ( event instanceof RoutesRecognized ) {
                let params = {};
                let destinationData = {};
                let destinationName = '';
                const destinationEvent = event.state.root.firstChild.children[0];
                if ( destinationEvent !== undefined ) {
                    params = destinationEvent.params;
                    destinationData = destinationEvent.data;
                    destinationName = destinationEvent.url[0].path;
                }
                const from = { name: this.router.url.slice( 1 ) };
                const destination = { name: destinationName, data: destinationData };
                this.$storageService.storeDestinationState( destination, params, from );
            }
        } );

    }
}
