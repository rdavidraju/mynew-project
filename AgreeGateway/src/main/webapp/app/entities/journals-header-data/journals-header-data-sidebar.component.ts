import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { JournalsHeaderData } from './journals-header-data.model';
import { JournalsHeaderDataService } from './journals-header-data.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any


@Component({
    selector: 'journals-header-data-side-bar',
    templateUrl: './journals-header-data-sidebar.component.html'
})
export class JournalsHeaderDataSideBarComponent implements OnInit {
    jobsList: JournalsHeaderData[]=[];
    journalsBatchSideBarList:any = [];
    authorities: any[];
    isSaving: boolean;
     sourcebody:any;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private jobsDetailsService: JournalsHeaderDataService,
        private eventManager: JhiEventManager
    ) {
        
    }
    
    ngOnInit() {
        this.isSaving = false;
        this.jobsDetailsService.getJournalsSideBar().subscribe((res: any) => {
            this.journalsBatchSideBarList = res;
            
            //Sidebar List Default First Open
            if(this.journalsBatchSideBarList.length == 0){
                //Do nothing
            }else{
                this.journalsBatchSideBarList[0].firstOpened = true;
            }

        });
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

    getInSidHeight(){
        if(this.journalsBatchSideBarList.length == 1){
            return 'maxHeightNone'
        }else{
            $('.scrHetless').css('max-height',200);
            return 'scrHetless';
        }
    }
}