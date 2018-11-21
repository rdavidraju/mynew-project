import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Principal } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import {CommonService} from '../../entities/common.service';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-side-nav',
    templateUrl:'./sidebar.component.html' 
})
export class JhiSideNavComponent implements OnInit {
      opened=false;
      sideNavMode='push';
      sidebarToggler :boolean = true;
      UserData: any;
      maxheightsidebar: any;
    constructor(
        private principal: Principal,
        private $sessionStorage: SessionStorageService,
        private commonService: CommonService,
        private router: Router,
    ) {
      this.sideNavMode='push';
    }
    
    isAuthenticated() {
        if(this.principal.isAuthenticated()){
            this.UserData = this.$sessionStorage.retrieve('userData');
            this.opened=true;
        // console.log('image url:'+this.UserData.image);
            if(this.UserData && this.UserData.image != null){
                $('.sidebar-profile .display-picture').css({
                    'background-image' : 'url(data:image/png;base64,'+this.UserData.image+')',
                    'background-size'  : '120px',
                    'background-position' : '-25px'
                });
            } 
            // else {
            //     $('.sidebar-profile .display-picture').css('background-image','url(../images/user-3.jpg)');
            // }
        }else{
            this.opened=false;
        }
    } 


    toggleSidebarr(){
                if(this.sidebarToggler == true){
                    $('body').addClass('sidebar-static');
                    $('.ng-sidebar').removeClass('ng-sidebar-iconWidth');
                    this.sidebarToggler = false;
                } else if(this.sidebarToggler == false){
                    $('body').removeClass('sidebar-static');
                    this.sidebarToggler = true;
                }
        }

    ngOnInit() {
        //max height for sidebar scroll
        $("#maxHeightSidebar").css('max-height',(window.innerHeight - 220)+'px');
        $(window).resize(function(){
            $("#maxHeightSidebar").css('max-height',(window.innerHeight - 220)+'px');
        });

        $('.tempClass').addClass('ng-sidebar-iconWidth');

        $(".side-nav-list").hover(function () {
            $(".side-nav-list").removeClass("hover-item");
            $(this).addClass("hover-item");
        });
        $(document).on('click', '.hover-item .sidenav-list-items li', function () {
            $(".sidenav-list-items li").removeClass("highlight");
            $(this).addClass("highlight");
            $(".side-nav-list-icon").removeClass("highlightmain");
            $(".hover-item .side-nav-list-icon").addClass("highlightmain");
        });

        $(document).on('click', '.side-nav-list div', function () {
            $(".side-nav-list div").removeClass("highlight");
            $(this).addClass("highlight");
        });

        $('.sidebarToggle').hover(() => {
            $('.tempClass').removeClass('ng-sidebar-iconWidth');
            if (this.sidebarToggler == true) {
                $('.ng-sidebar').removeClass('ng-sidebar-iconWidth').addClass('ng-sidebar-fullWidth');
            }
        }, () => {
            if (this.sidebarToggler == true) {
                $('.ng-sidebar').removeClass('ng-sidebar-fullWidth').addClass('ng-sidebar-iconWidth');
            }
        });
    }

    activeTenant() {
        if (this.UserData && this.UserData.tenantId) {
            let link = ['/tenant-details', { outlets: { 'content': this.UserData.tenantId + '/details' } }];
            this.router.navigate(link);
        }
    }
}