import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { FileTemplateBreadCrumbTitles } from './file-templates.model';
import { JhiLanguageHelper, StateStorageService, Principal, LoginService } from '../../shared';
import { CommonService } from '../common.service';
import { routerTransition} from '../../layouts/main/route.transition';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component( {
    selector: 'jhi-file-templates-home',
    templateUrl: './file-templates-home.component.html',
    animations: [routerTransition]

} )
export class FileTemplatesHomeComponent implements OnInit {
    displayFTSB = true;
    isVisibleA = false;
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private $storageService: StateStorageService,
        private loginService: LoginService,
        private principal: Principal,
        private router: Router,
        private commonService: CommonService,
    ) {
    }
    private getPageTitle( routeSnapshot: ActivatedRouteSnapshot ) {
        let title: string = ( routeSnapshot.data && routeSnapshot.data['pageTitle'] ) ? routeSnapshot.data['pageTitle'] : 'agreeGatewayApp';
        console.log( 'title is:' + JSON.stringify( routeSnapshot.data ) );
        if ( routeSnapshot.data.breadcrumb === FileTemplateBreadCrumbTitles.fileTemplatesNew || routeSnapshot.data.breadcrumb === FileTemplateBreadCrumbTitles.fileTemplates) {
            //this.closeGroupby();
            this.isVisibleA = true;
        } else {
            this.isVisibleA = false;
           // this.viewGroupby();
        }

        if ( routeSnapshot.firstChild ) {
            title = this.getPageTitle( routeSnapshot.firstChild ) || title;
        }
        return title;
    }
    // closeGroupby() {
    //     this.displayFTSB = false;
    //     $( '.sidebar' ).removeClass( 'col-md-2' );
    //     $( '.entitybody' ).removeClass( 'col-md-10' );
    //     $( '.entitybody' ).addClass( 'col-md-12' );
    //     $( '.component-title' ).removeClass( 'margin-left-22' );
    // }
    // viewGroupby() {
    //     this.displayFTSB = true;
    //     $( '.sidebar' ).addClass( 'col-md-2' );
    //     $( '.entitybody' ).addClass( 'col-md-10' );
    //     $( '.entitybody' ).removeClass( 'col-md-12' );
    //     $( '.component-title' ).addClass( 'margin-left-22' );

    // }
    // onNotify( event: any ) {
    //     this.isClose = event;
    // }
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
       // $(".split-example").css('min-height',(this.commonService.screensize().height - 130)+'px');
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
