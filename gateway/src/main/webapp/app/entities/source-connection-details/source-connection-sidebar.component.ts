import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Response } from '@angular/http';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { SourceConnectionDetails } from './source-connection-details.model';
import { SourceConnectionDetailsService } from './source-connection-details.service';
import { CommonService } from '../common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'connection-side-bar',
    templateUrl: './source-connection-sidebar.component.html'
})
export class ConnectionSideBarComponent implements OnInit {
    
    sourceConnectionDetails: SourceConnectionDetails;
    authorities: any[];
    isSaving: boolean;
    sourcebody:any;
    sidebarCone: any;
    currentSource: string='';
    sideBarHeight:any;
    filterColumns : any =[
        {
            "colName":"connectiontype",
            "displayCol":"Connection Type"
        }      
                          
    ];
    selectedCol :any = this.filterColumns[0].colName;
    sideBarData:any=[];
    
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private router: Router,
        private sourceConnectionDetailsService: SourceConnectionDetailsService,
         private commonService: CommonService,
    ) {
     
    }

    ngOnInit() {
         this.sideBarHeight = (this.commonService.screensize().height - 300) + 'px';
        $('.sub-side-navbar perfect-scrollbar').css('max-height',this.sideBarHeight);
        this.selectedColumn();
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
    selectedColumn()
    {
        this.sourceConnectionDetailsService.groupSideBarByCols('SourceConnectionDetails','connectionType').subscribe(        
                (res:Response)=>{
                    this.sideBarData= res;

                    //Sidebar List Default First Open
                    if (this.sideBarData.length == 0) {
                        //Do nothing
                    } else {
                        this.sideBarData[0].firstOpened = true;
                    }

                    if(this.sideBarData[0] && this.sideBarData[0].connectionType)

                    {
                    this.sourcebody = this.sideBarData[0].connectionType;
                   this.currentSource = this.sideBarData[0].connectionType;
                }
                    
                });
        
    }
    
    private expandsource(name) {
        if (this.currentSource === name) {  // if user click on same icon
          this.sourcebody = '';     // Collapse expanded list
          this.currentSource = '';  // Replace current selection
          return;
        }
        this.currentSource = name;  // To hold current selection
        this.sideBarData.forEach(connectionType => {
          if (connectionType.connectionType == name) {
            this.sourcebody = name;
          }
        });
      }
    
}
