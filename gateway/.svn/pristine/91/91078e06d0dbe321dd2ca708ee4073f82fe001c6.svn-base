import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';

import { SourceProfiles } from './source-profiles.model';
import { SourceProfilesService } from './source-profiles.service';
import { CommonService } from '../common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any


@Component({
    selector: 'profiles-side-bar',
    templateUrl: './profiles-sideBar.component.html'
})
export class ProfilesSideBarComponent implements OnInit {

    profiles: SourceProfiles [];
    authorities: any[];
    isSaving: boolean;
     sourcebody:any;
     sideBarHeight:any;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private profilesService: SourceProfilesService,
        private eventManager: JhiEventManager,
         private commonService: CommonService,
    ) {
    }
    loadAllProfiles() { //Load all profiles with their connections information
        this.profilesService.getAllProfilesAndConnections().subscribe(
            ( res: Response ) => {
                this.profiles = res.json();
                
            },
            ( res: Response ) => this.onError( res.json() )
        );

       
    }
    ngOnInit() {
        this.sideBarHeight = (this.commonService.screensize().height - 300) + 'px';
        $('.sub-side-navbar perfect-scrollbar').css('max-height',this.sideBarHeight);
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
        this.loadAllProfiles();
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
    loadAll()
    {
         this.sourcebody = this.sideBarData[0].source;
     }
    // save () {
    //     this.isSaving = true;
    //     if (this.profiles.id !== undefined) {
    //         this.profilesService.update(this.profiles)
    //             .subscribe((res: SourceProfiles) =>
    //                 this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
    //     } else {
    //         this.profilesService.create(this.profiles)
    //             .subscribe((res: SourceProfiles) =>
    //                 this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
    //     }
    // }

    // private onSaveSuccess (result: SourceProfiles) {
    //     this.eventManager.broadcast({ name: 'fileTemplatesListModification', content: 'OK'});
    //     this.isSaving = false;
    // }

    // private onSaveError (error) {
    //     this.isSaving = false;
    //     this.onError(error);
    // }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }
      expandsource(sourcename){
        this.sideBarData.forEach (source =>{
            if (source.source == sourcename){
                this.sourcebody = sourcename;
            }    
        });
    }
     sideBarData= [
       {
            "id":1,
           "source": "Chase",
            "dispname": "Chase",
           "imgPath": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAAk1BMVEX///8AXLnw8PEAWLcYZrQAYMIAXr1ahbFOfaxKi818q9vt7u5bibhLfrEXaLoib73Y29/29vcsdsDR194accne4eS4w89ChMcNacXn6es5drjBzdoXbMJmndUAZcuktMVljLSctMw2fMOSrMd6nL9xn81UldYfbr0JZ8RJf7aYveOFnrxnlME/fbwKYLamwNuFs+KsN3Y0AAABXElEQVRIie3W23KCMBAG4ATctB4QAyIirQdapUht+/5P14gKG7JBOtObzvS/5f8gM4ENjLUjjyOco1HAGUvGSnC1TKS9H50SxibAtYBnFUnw4ZiAQ2ER4wwGFOAQO1TfyUDQgEOWmH0ZA7cBSshC9ayAQxAR/Q7AwZ9p4NLqABzy0Oh3Ai7mjXi/djqBEmmrfwPClrdF1d/Xt6zAdjqwJj8/o2yWUAHp2DNW1z9droN7Gf0+KAuvzmTWA3hraHLoAR7QxrjDf/BXwZeLvpA+IFpNm6x6ADUiUYi+Ce5FA88/A7BSa4geO7KVGEBQjdgN/kBaecIA/OuAXSwFpyN8BERej9fUJjDAo5Klc1ogoPUZC2nRALFMmZYwp0QNbmMVZeYTU7sGZl/tR2CKG1hvqB0lxBXAjn4HkqwtLmD9QvfVexsDAbxXW18d7if9x8Q9g9DeV6fJfohzSI3GNxONHJoVvN79AAAAAElFTkSuQmCC",
            "templatelist":[
            {
               "tempId":1,
              "TemplateName": "Chase ACH - ACT00334" ,
              "description":  "Chase ACH - ACT00334 File template"
            },
                {
                    "tempId":2,
               "TemplateName": "Chase ACH – ACT00105" ,
               "description":  "Chase ACH – ACT00105 File template"
            }
                ]
           
       },
        {
             "id":2,
           "source": "BOA",
            "dispname": "BOA",
           "imgPath": "data:image/png;base64,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",
            "templatelist":[
            {
                    "tempId":3,
              "TemplateName": "BOA ACH - ACT00334"  ,
              "description":  "BOA ACH - ACT00334 File template"
            },
                {
                    "tempId":4,
               "TemplateName": "BOA ACH – ACT00105" ,
               "description":  "BOA ACH – ACT00105 File template"
            }
                ]
           
       },
        {
             "id":3,
           "source": "Wellsforgo",
            "dispname": "Wells forgo",
           "imgPath": "http://www.montrosechamber.org/wp-content/sabai/File/files/664e01a80cd59928f6152513282a9f9c.png",
 "templatelist":[
            {
         "tempId":5,
              "TemplateName": "Wellsforgo ACH - ACT00334",
              "description":  "Wellsforgo ACH - ACT00334 File template"  
            },
                {
                    "tempId":6,
               "TemplateName": "Wellsforgo ACH – ACT00105" ,
               "description":  "Wellsforgo ACH – ACT00105 File template"
            }
                ]
           
       },
        {
             "id":4,
           "source": "Citi",
            "dispname": "Citi",
           "imgPath": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAAAP1BMVEX///8AAJkzM8ymyvBmZsz/AACZmcwAAMzMzP//MzP/mZn/Zmb/zMwAM8z/fID/UFDx8fH/mczM7P/j4+Pq6urRYjM3AAAAuklEQVQ4jeWRSQKDIAxFMxFQtHa6/1mbQNUWtd23WTD9l08IAD8Zw9SFELp8OR/IrnaFGXb03s77ujKm3+inELLPwnVzaoGcSxKhFpM8HdRJyO1REiJxb0nAEpFEfbPogh6RIZo52QqRICG+6KRKKMWcdURRNiDO/rEWpfPtdVwBxfG9vBYQlG8AfQZ4LXcfsMeVO+QQUHu5eHsWgJTgugJgPbCgBOMzt2kUwI2l/CDrvWZ4Y3TzI/8cDzOPBHXjaCmjAAAAAElFTkSuQmCC",
 "templatelist":[
            {
         "tempId":7,
              "TemplateName": "Citi ACH - ACT00334"  ,
              "description":  "Citi ACH - ACT00334 File template"
            },
                {
                    "tempId":8,
               "TemplateName": "Citi ACH – ACT00105" ,
               "description":  "Citi ACH – ACT00105 File template"
            }
                ]
           
       }];
}
