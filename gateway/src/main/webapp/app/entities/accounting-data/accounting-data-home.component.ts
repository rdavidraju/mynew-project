import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { AccountingDataBreadCrumbTitles } from './accounting-data.model';
import { JhiLanguageHelper, StateStorageService, Principal, LoginService } from '../../shared';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component( {
    selector: 'jhi-accounting-data-home',
    templateUrl: './accounting-data-home.component.html'
} )
export class AccountingDataHomeComponent implements OnInit {
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private $storageService: StateStorageService,
        private loginService: LoginService,
        private principal: Principal,
        private router: Router
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
