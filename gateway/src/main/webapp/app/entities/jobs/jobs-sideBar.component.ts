import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd, Event } from '@angular/router';
import { Response } from '@angular/http';
import { CommonService } from '../../entities/common.service';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { JobsBasicDetails } from './jobs.model';
import { JobDetailsService } from './job-details.service';
import { JobsService } from './jobs.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any


@Component({
    selector: 'jobs-side-bar',
    templateUrl: './jobs-sideBar.component.html'
})
export class JobsSideBarComponent implements OnInit {
    jobsList: JobsBasicDetails[]=[];
    authorities: any[];
    isSaving: boolean;
    sourcebody:any;
    subscription: any;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private jobsDetailsService: JobDetailsService,
        public jobsService: JobsService,
        private eventManager: JhiEventManager,
        private router: Router,
        private route: ActivatedRoute,
        private commonService: CommonService,
        private jobDetailsService: JobDetailsService
    ) {
        /**
         * Look for jobs submitted successful and refresh accordingly
         */
        this.jobDetailsService.jobSubmitChangeDetectVar$.subscribe(data => {
            this.jobsService.frequencyType = data;
            this.getSidebarData();
        });
          /* this.subscription = this.route.params.subscribe(params => {
              if (params['maintype']) {
                  this.jobsService.frequencyType = params['maintype'];
              } else {
                this.jobsService.frequencyType = 'SCHEDULED';
              }
          }); */
    }
    
    ngOnInit() {
        $(".split-example .left-component").css('min-height','calc(100vh - 143px)');
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.getSidebarData();
//         this.jobsDetailsService.getJobsList(1,25).subscribe((res:any)=>{
//            this.jobsList=res;
//        })
        
        $( document ).on( 'click', '.search-icon-sidebar', function() {
            if ( $( ".sbSearch md-input-container" ).hasClass( "hidethis" ) ) {
                $( ".sbSearch md-input-container" ).removeClass( "hidethis" );
                $( ".sbSearch md-input-container" ).addClass( "show-this" );
            } 
            else {
                let value = $('.sbSearch md-input-container .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if(value.length<=0) { // zero-length string
                    $( ".sbSearch md-input-container" ).removeClass( "show-this" );
                    $( ".sbSearch md-input-container" ).addClass( "hidethis" );
                }
            }
        } );
        
        $(".sbSearch md-input-container .mySearchBox").blur(function() {
            let value = $('.sbSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if(value.length<=0) { // zero-length string
                $( ".sbSearch md-input-container" ).removeClass( "show-this" );
                $( ".sbSearch md-input-container" ).addClass( "hidethis" );
            }
       });
    }

    getSidebarData() {
        this.jobsService.sideBarJobLists(this.jobsService.frequencyType).subscribe((res: any) => {
            this.jobsList = res;
            //Sidebar List Default First Open
            if (this.jobsList.length == 0) {
                //Do nothing
            } else {
                this.jobsList[0].firstOpened = true;
            }
        });
    }
    
    //Innersidebar Content height based on length
    getInSidHeight(){
        if(this.jobsList.length == 1){
            return 'maxHeightNone'
        }else{
            $('.scrHetless').css('max-height',200);
            return 'scrHetless';
        }
    }

    changeFilter(param, type) {
        if (type == 'Frequency') {
            this.getSidebarData();
        } else {
            param = this.jobsList[param.index].programId
        }
        let link = ['/jobs', { outlets: { 'content': [param] + '/' + [type] + '/' + [this.jobsService.frequencyType] + '/schedulars-list' } }];
        this.router.navigate(link);
    }

    ngOnDestroy(): void {
        // this.subscription.unsubscribe();
    }

}