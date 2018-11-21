import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Response } from '@angular/http';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { Reconcile } from './reconcile.model';
import { ReconcileService } from './reconcile.service';
import {UserData} from '../../home/home.component';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'reconcile-side-bar',
    templateUrl: './reconcile.sidebar.html'
})
export class ReconcileSideBarComponent implements OnInit {
    
    reconcile: Reconcile;
    authorities: any[];
    isSaving: boolean;
    sourcebody:any;
    sidebarCone: any;
    currentSource: string='';
    /*filterColumns : any =[
        {
            "colName":"googleDrive",
            "displayCol":"Google Drive"
        },
        {
            "colName":"dropbox",
            "displayCol":"Dropbox"
        },
        {
            "colName":"sftp",
            "displayCol":"SFTP"
        }
        ];*/
    filterColumns : any =[
        {
            "colName":"connectiontype",
            "displayCol":"Connection Type"
        }      
                          
    ];
    selectedCol :any = this.filterColumns[0].colName;
    sideBarData:any=[];
    sideBarFilter:any[];
    filterValues:any[];
    
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private router: Router,
        private reconcileService: ReconcileService
    ) {
       
    
    }

  ngOnInit() {
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
