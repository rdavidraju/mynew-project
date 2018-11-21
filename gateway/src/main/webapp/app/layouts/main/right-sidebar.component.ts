import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Principal } from '../../shared';
import { SessionStorageService } from 'ng2-webstorage';
import {CommonService} from '../../entities/common.service';
import { NavBarService } from '../../layouts/navbar/navbar.service';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { JhiTrackerService } from '../../shared/tracker/tracker.service';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-rightside-nav',
    templateUrl:'./right-sidebar.component.html',
    animations: [
        trigger(
            'enterAnimation', [
                transition(':enter', [
                    style({ opacity: 0 }),
                    animate('500ms ease-in', style({ opacity: 1 }))
                ]),
                transition(':leave', [
                    style({  opacity: 1 }),
                    animate('100ms ease-in', style({  opacity: 0 }))
                ])
            ]
        )]
})
export class LoggedInUsersComponent implements OnInit {
      opened=false;
      sideNavMode='slide';
      rightSidebarToggler = false;
      maxheightsidebar: any;
      currentAccount: any;
      loggedInUsers:any[]=[];
      currentColor =  'green';
      activities: any[] = [];
    constructor(
        private principal: Principal,
        private $sessionStorage: SessionStorageService,
        public commonService: CommonService,
        private router: Router,
        public navbarService: NavBarService,
        private trackerService: JhiTrackerService
    ) {
      
    }
    
    isAuthenticated() {
        return this.principal.isAuthenticated();
    } 
    isOpened(){
        if(this.opened == false){
            this.opened = true;
            $(".right-toggle-icon").addClass("toggled");
        } else {
            this.opened = false;
            $(".right-toggle-icon").removeClass("toggled");
        }
        
    }

    fetchLoggedInUsers(){
        this.navbarService.loggedInUsersService().subscribe((res: any) => {
            this.navbarService.loggedInUsersList = res;
            for (let i = 0; i < this.navbarService.loggedInUsersList.length; i++) {
                const user = this.navbarService.loggedInUsersList[i];
                user['firstLetter'] = user.firstName.charAt(0); 
            }
          })
    }

    ngOnInit() {
        this.isAuthenticated();
        // this.fetchLoggedInUsers();
        // this.trackerService.subscribeUsers();
        // this.trackerService.receiveLoggedInUsers().subscribe((activity) => {
        //     this.showActivity(activity);
        // });
    }

    // showActivity(activity: any) {
    //     let existingActivity = false;
    //     for (let index = 0; index < this.activities.length; index++) {
    //         if (this.activities[index].sessionId === activity.sessionId) {
    //             existingActivity = true;
    //             if ( activity.page === 'logout' ) {
    //                 this.activities.splice(index, 1);
    //             } else {
    //                 this.activities[index] = activity;
    //             }
    //         }
    //     }
    //     if (!existingActivity && (activity.page !== 'logout')) {
    //         this.activities.push(activity);
    //     }
    //     if(this.activities.length > 0){
    //         for (let i = 0; i < this.activities.length; i++) {
    //             const user = this.activities[i];
    //             user['firstLetter'] = user.firstName.charAt(0); 
    //         }
    //     }
    //     console.log("Logged In Users List: "+JSON.stringify(this.activities));
    // }

    // ngOnDestroy() {
    //     this.trackerService.unsubscribe();
    // }
}