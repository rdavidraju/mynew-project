import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { UserMgmtBreadCrumbTitles } from './user-management.model';
import { UserModalService } from './user-modal.service';
import { User, UserService } from '../../shared';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any


@Component({
    selector: 'user-management-side-bar',
    templateUrl: './user-management-sidebar.component.html'
})
export class UserMgmtSideBarComponent implements OnInit {
    authorities: any[];
    isSaving: boolean;
     sourcebody:any;
     usersList:any=[];
     isVisibleA: boolean = false;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private userModalService: UserModalService,
        private eventManager: JhiEventManager,
        private userService: UserService,
    ) {
        
    }
    
    ngOnInit() {
         this.userService.getUsersByTenantId().subscribe((res: any) => {
            this.usersList = res;
            console.log('this.usersList ' + JSON.stringify(this.usersList));
        }); 
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
         $( document ).on( 'click', '.search-icon-sidebar', function() {
            if ( $( ".sbSearch md-input-container" ).hasClass( "hidethis" ) ) {
                $( ".sbSearch md-input-container" ).removeClass( "hidethis" );
                $( ".sbSearch md-input-container" ).addClass( "show-this" );
            } 
            else {
                var value = $('.sbSearch md-input-container .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if(value.length<=0) { // zero-length string
                    $( ".sbSearch md-input-container" ).removeClass( "show-this" );
                    $( ".sbSearch md-input-container" ).addClass( "hidethis" );
                }
            }
        } );
        $(".sbSearch md-input-container .mySearchBox").blur(function() {
            var value = $('.sbSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if(value.length<=0) { // zero-length string
                $( ".sbSearch md-input-container" ).removeClass( "show-this" );
                $( ".sbSearch md-input-container" ).addClass( "hidethis" );
            }
       });
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
}