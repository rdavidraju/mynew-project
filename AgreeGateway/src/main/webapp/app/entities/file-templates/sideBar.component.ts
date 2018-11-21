import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Response } from '@angular/http';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { FileTemplates } from './file-templates.model';
import { FileTemplatesService } from './file-templates.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { CommonService } from '../common.service';
declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'side-bar',
    templateUrl: './sideBar.component.html'
})
export class SideBarComponent implements OnInit {

    fileTemplates: FileTemplates;
    sideBarHeight:any;
    authorities: any[];
    isSaving: boolean;
    sourcebody:any;
    groupbynav:true;
    currentSource: string='';
    filterColumns : any =[
        {
            "colName":"source",
            "dispCol":"Source"
        },
        {
            "colName":"region",
            "dispCol":"Region"
        }
        ];
    selectedCol :any = this.filterColumns[0].colName;
    sideBarData:any=[];
    
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private router: Router,
         private fileTemplatesService: FileTemplatesService,
         private commonService: CommonService,
    ) {     
    }

    ngOnInit() {
        this.sideBarHeight = (this.commonService.screensize().height - 300) + 'px';
        $('.sub-side-navbar perfect-scrollbar').css('max-height',this.sideBarHeight);
        console.log('oninit sdbar');
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
       // this.selectedCol = 'Source' ;
         this.fileTemplatesService.groupSideBarByCols('FileTemplates','source').subscribe(        
            (res:Response)=>{
                this.sideBarData = res;
               // console.log('this.sideBarData '+JSON.stringify(this.sideBarData ));
                //Sidebar List Default First Open
               if (this.sideBarData.length == 0) {
                   //Do nothing
               } else {
                   this.sideBarData[0].firstOpened = true;
               }

                //console.log('this.sbr in ngonint'+JSON.stringify(this.sideBarData));
                if(this.sideBarData[0] && this.sideBarData[0].source)

                    {
                    this.sourcebody = this.sideBarData[0].source;
                   this.currentSource = this.sideBarData[0].source;
                }
            });
         
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
        console.log('selectedCol is'+JSON.stringify(this.selectedCol));    
        this.fileTemplatesService.groupSideBarByCols('FileTemplates',this.selectedCol).subscribe(        
            (res:Response)=>{
            console.log('response in ts is'+JSON.stringify(res));    
                this.sideBarData= res;
                console.log('this.sbr'+JSON.stringify(this.sideBarData));
            });
    }

    selectedCol1(column: any): any{
        console.log('selectedCol is'+JSON.stringify(column.colName));    
        this.fileTemplatesService.groupSideBarByCols('FileTemplates',column.colName).subscribe(        
            (res:Response)=>{
            console.log('response in ts is'+JSON.stringify(res));    
                this.sideBarData= res;
                console.log('this.sbr'+JSON.stringify(this.sideBarData));
            });
    }

    private expandsource(sourcename) {
      console.log(this.currentSource);
      if (this.currentSource === sourcename) {  // if user click on same icon
        this.sourcebody = '';     // Collapse expanded list
        this.currentSource = '';  // Replace current selection
        return;
      }
      this.currentSource = sourcename;  // To hold current selection
      this.sideBarData.forEach(source => {
        if (source.source == sourcename) {
          this.sourcebody = sourcename;
        }
      });
    }
}
