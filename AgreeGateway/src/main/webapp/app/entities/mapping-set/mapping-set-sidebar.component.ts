import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { MappingSetBreadCrumbTitles } from './mapping-set.model';
import { MappingSetService } from './mapping-set.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any


@Component({
    selector: 'mapping-set-side-bar',
    templateUrl: './mapping-set-sidebar.component.html'
})
export class MappingSetSideBarComponent implements OnInit {
    authorities: any[];
    isSaving: boolean;
     sourcebody:any;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private mappingSetService: MappingSetService,
        private eventManager: JhiEventManager
    ) {
        
    }
    
    ngOnInit() {
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
}