import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
import { Jobs, JobsBasicDetails } from './jobs.model';
//import { JobsService } from './jobs.service';
import { JobDetailsService } from './job-details.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
import { PageEvent } from '@angular/material';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage';
import { MdDialog } from '@angular/material';
import { JobsNewDialog } from './jobs-new-dialog.component';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-jobs',
    templateUrl: './jobs.component.html'
})
export class JobsComponent implements OnInit, OnDestroy {
    jobsList: JobsBasicDetails[]=[];
    selectedJobs: JobsBasicDetails[]=[];
    currentAccount: any;
    eventSubscriber: Subscription;
    columnOptions: SelectItem[];
    jobListsHeight:any;
    length: number;
    pageSize: number=0;
    pageSizeOptions = [5, 10, 25, 100];
    pageEvent: PageEvent = new PageEvent();
    //  UserData :any;
    jobsTableColumns = [                  //  Job list source columns
        { field: 'jobDescription', header: 'Job Description', width: '280px', align: 'left' },
        { field: 'programName', header: 'Program Name', width: '180px', align: 'left' }
    ];

    constructor(
        //private jobsService: JobsService,
        private jobsDetailsService: JobDetailsService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private commonService: CommonService,
        private $sessionStorage: SessionStorageService,
        public dialog: MdDialog,
    ) {
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.pageSize;
        this.columnOptions = [];
        for (let i = 0; i < this.jobsTableColumns.length; i++) {
            this.columnOptions.push({ label: this.jobsTableColumns[i].header, value: this.jobsTableColumns[i] });
        }
       //this.UserData = this.$sessionStorage.retrieve('userData');
        this.loadAll();
    }

    loadAll() {
      
        this.jobsDetailsService.getJobsList(this.pageEvent.pageIndex+1,this.pageEvent.pageSize).subscribe((res: Response)=>{
            this.jobsList=res.json();
            this.length= +res.headers.get('x-count');
        })
    }

    ngOnInit() {
        this.jobListsHeight = (this.commonService.screensize().height - 340) + 'px';
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        
        $(".search-icon-body").click(function() {   
            if ( $( ".ftlSearch md-input-container" ).hasClass( "hidethis" ) ) {
                $( ".ftlSearch md-input-container" ).removeClass( "hidethis" );
                $( ".ftlSearch md-input-container" ).addClass( "show-this" );
            } else if ( $( ".ftlSearch md-input-container" ).hasClass( "show-this" ) ) {
                var value = $( '.ftlSearch md-input-container .mySearchBox' ).filter( function() {
                    return this.value != '';
                } );
                if ( value.length <= 0 ) { // zero-length string
                    $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                    $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
                }
            } else {
                $( ".ftlSearch md-input-container" ).addClass( "show-this" );
            }
        } );
        $( ".ftlSearch md-input-container .mySearchBox" ).blur( function() {
            var value = $( '.ftlSearch md-input-container .mySearchBox' ).filter( function() {
                return this.value != '';
            } );
            if ( value.length <= 0 ) { // zero-length string
                $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
            }
        } );
    }

    ngOnDestroy() {
//        this.eventManager.destroy(this.eventSubscriber);
    }    

    /**
     * Author: Sameer
     * Open New Jobs Dialog
     */
    openDialog(): void {
        let dialogRef = this.dialog.open(JobsNewDialog, {
          width: '600px',
          disableClose: true
        });

        $('body').addClass('jobs-material');
    
        dialogRef.afterClosed().subscribe(result => {
          $('body').removeClass('jobs-material');
        });
      }
}