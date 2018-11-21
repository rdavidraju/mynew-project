import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Principal } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import {CommonService} from '../../entities/common.service';
import { NavBarService } from '../../layouts/navbar/navbar.service';
import { trigger, state, style, animate, transition } from '@angular/animations';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-side-nav',
    templateUrl:'./sidebar.component.html',
    animations: [
        trigger(
            'enterAnimation', [
                transition(':enter', [
                    style({ opacity: 0 }),
                    animate('1000ms ease-in', style({ opacity: 1 }))
                ]),
                transition(':leave', [
                    style({  opacity: 1 }),
                    animate('10ms ease-in', style({  opacity: 0 }))
                ])
            ]
        )]
})
export class JhiSideNavComponent implements OnInit, AfterViewInit {
      opened=false;
      sideNavMode='push';
      sidebarToggler = true;
    //   UserData: any;
      maxheightsidebar: any;
      currentAccount: any;
    constructor(
        private principal: Principal,
        private $sessionStorage: SessionStorageService,
        public commonService: CommonService,
        private router: Router,
        private navbarService: NavBarService
    ) {
      this.sideNavMode='push';
      this.navbarService.loginSuccessVar$.subscribe(() => {
        setTimeout(() => {
            if(this.commonService.currentAccount){
                if(this.commonService.currentAccount['image']){
                    $('.display-picture').css({
                        'background-image': 'url(data:image/png;base64,' + this.commonService.currentAccount['image'] + ')',
                        'background-position': '-15px',
                        'background-size' : '125px'
                    });
                }else if(this.commonService.currentAccount.firstName && this.commonService.currentAccount.firstName.length>0){
                    $('.display-picture').attr('data-content', this.commonService.currentAccount.firstName.charAt(0)).css({
                        'background-image':'unset',
                        'background-color' : 'white'});
                }
            }
        }, 100);
    });
    }
    
    isAuthenticated() {
        if(this.principal.isAuthenticated()){
            // this.UserData = this.$sessionStorage.retrieve('userData');
            this.opened=true;
        // console.log('image url:'+this.UserData.image);
            // if(this.UserData && this.UserData.image != null){
            //     $('.sidebar-profile .display-picture').css({
            //         'background-image' : 'url(data:image/png;base64,'+this.UserData.image+')',
            //         'background-size'  : '120px',
            //         'background-position' : '-25px'
            //     });
            // } 
            // else {
            //     $('.sidebar-profile .display-picture').css('background-image','url(../images/user-3.jpg)');
            // }
        }else{
            this.opened=false;
        }
    } 

    getCurrentAccount() {
        setTimeout(() => {
            if (!this.commonService.currentAccount) {
                this.principal.identity().then((account) => {
                    this.commonService.currentAccount = account;
                    if(!this.commonService.currentAccount) return;
                    if(this.commonService.currentAccount && this.commonService.currentAccount['image'] != null){
                        $('.display-picture').css({
                            'background-image' : 'url(data:image/png;base64,'+this.commonService.currentAccount['image']+')',
                            'background-size'  : '120px',
                            'background-position' : '-15px'
                        });
                    } else {
                        $('.display-picture').attr('data-content', this.commonService.currentAccount.firstName.charAt(0)).css({
                            'background-image':'unset',
                            'background-color' : 'white'});
                    }
                });
            } else {
                if(this.commonService.currentAccount['image'] != null){
                    $('.display-picture').css({
                        'background-image' : 'url(data:image/png;base64,'+this.commonService.currentAccount['image']+')',
                        'background-size'  : '120px',
                        'background-position' : '-15px'
                    });
                } else {
                    $('.display-picture').attr('data-content', this.commonService.currentAccount.firstName.charAt(0)).css({
                        'background-image':'unset',
                        'background-color' : 'white'});
                }
            }
        }, 100);
    }

    // toggleSidebarr(){
    //             if(this.sidebarToggler == true){
    //                 $('body').addClass('sidebar-static');
    //                 $('.ng-sidebar').removeClass('ng-sidebar-iconWidth');
    //                 this.sidebarToggler = false;
    //             } else if(this.sidebarToggler == false){
    //                 $('body').removeClass('sidebar-static');
    //                 this.sidebarToggler = true;
    //             }
    //     }

        toggleSidebarr(){
            if(this.commonService.sidebarToggler == true){
                $('body').addClass('sidebar-static');
                $('.sidebarToggle .ng-sidebar').removeClass('ng-sidebar-iconWidth');
                $('.sidebarToggle .ng-sidebar').addClass('ng-sidebar-fullWidth');
                this.commonService.showText = true;
                // $('.ng-sidebar').removeClass('ng-sidebar-iconWidth').addClass('ng-sidebar-fullWidth');
                this.commonService.sidebarToggler = false;
            } else if(this.commonService.sidebarToggler == false){
                $('body').removeClass('sidebar-static');
                $('.sidebarToggle .ng-sidebar').removeClass('ng-sidebar-fullWidth');
                $('.sidebarToggle .ng-sidebar').addClass('ng-sidebar-iconWidth');
                this.commonService.sidebarToggler = true;
                this.commonService.showText = false;
            }
        }

    isRecData(){
        this.commonService.isRecAllData = true;
    }

    isAcctData(){
        this.commonService.isAcctAllData = true;
    }

    ngOnInit() {
        this.getCurrentAccount();
        $('.sidebarToggle .ng-sidebar').addClass('ng-sidebar-iconWidth');
        //max height for sidebar scroll
        // $("#maxHeightSidebar").css('max-height',(window.innerHeight - 220)+'px');
        // $(window).resize(function(){
        //     $("#maxHeightSidebar").css('max-height',(window.innerHeight - 220)+'px');
        // });

        // $('.tempClass').addClass('ng-sidebar-iconWidth');

        $(".side-nav-list").hover(function() {
            $(".side-nav-list").removeClass("hover-item");
            $(this).addClass("hover-item");
        });
        $(document).on('click', '.hover-item .sidenav-list-items li', function() {
            $(".sidenav-list-items li").removeClass("highlight");
            $(this).addClass("highlight");
            $(".side-nav-list-icon").removeClass("highlightmain");
            $(".hover-item .side-nav-list-icon").addClass("highlightmain");
        });

        $(document).on('click', '.side-nav-list div', function() {
            $(".side-nav-list div").removeClass("highlight");
            $(this).addClass("highlight");
        });

        $('.sidebarToggle').hover(() => {
             //console.log('sidebar toggle ',this.commonService.sidebarToggler);
            if (this.commonService.sidebarToggler == false) {
                $('.ng-sidebar').removeClass('ng-sidebar-iconWidth').addClass('ng-sidebar-fullWidth');
                this.commonService.showText = true;
            } else {
                $('.ng-sidebar').removeClass('ng-sidebar-iconWidth').addClass('ng-sidebar-fullWidth');
                this.commonService.showText = true;
            }
        }, () => {
             //console.log('sidebar toggle hover left',this.commonService.sidebarToggler);
            if (this.commonService.sidebarToggler == false) {
                $('.ng-sidebar').removeClass('ng-sidebar-iconWidth').addClass('ng-sidebar-fullWidth');
                this.commonService.showText = true;
            } else {
                $('.ng-sidebar').removeClass('ng-sidebar-fullWidth').addClass('ng-sidebar-iconWidth');
                this.commonService.showText = false;
            }
        });
    }

    activeTenant() {
        if (this.commonService.currentAccount.tenantIdForDisplay) {
            const link = ['/tenant-details', { outlets: { 'content': this.commonService.currentAccount.tenantIdForDisplay + '/details' } }];
            this.router.navigate(link);
        }
    }

    userSetting() {
        if (!this.currentAccount) {
            this.principal.identity().then((account) => {
                this.currentAccount = account;
                this.$sessionStorage.store('userSetting', true);
                const link = ['/user-management', { outlets: { 'content': [this.currentAccount.id] + '/details' } }];
                this.router.navigate(link);
            });
        } else {
            this.$sessionStorage.store('userSetting', true);
            const link = ['/user-management', { outlets: { 'content': [this.currentAccount.id] + '/details' } }];
            this.router.navigate(link);
        }
    }

    ngAfterViewInit() {
        document.querySelector('.ng-sidebar').classList.add('ng-sidebar-iconWidth');
    }
}