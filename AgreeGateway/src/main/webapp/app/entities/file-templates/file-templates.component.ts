import { Component,DoCheck, KeyValueDiffers, OnInit, OnDestroy ,Input, Output, EventEmitter  } from '@angular/core';
import { ActivatedRoute,ActivatedRouteSnapshot, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { JhiDateUtils } from 'ng-jhipster';
import { FileTemplates } from './file-templates.model';
import { FileTemplatesService } from './file-templates.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
import { NotificationsService } from 'angular2-notifications-lite';
import { CommonService } from '../common.service';
import { DatePipe } from '@angular/common';
import {PageEvent} from '@angular/material';
 
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
 
declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-file-templates',
    templateUrl: './file-templates.component.html'
})
export class FileTemplatesComponent implements OnInit, OnDestroy {
    @Output() notify: EventEmitter<boolean> = new EventEmitter<boolean>();
    isClose: boolean = false;
    currentAccount: any;
    fileTemplates: FileTemplates[];
    error: any;
    abc: any;
    page:any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    

    templateData: any = [];
  mySelectedRows = [];
  columnOptions: SelectItem[]; 
  gb:any;
  length: number;
  pageSize: number;
  pageSizeOptions = [5, 10, 25, 100];
  pageEvent: PageEvent = new PageEvent();

//  notificationOptions: any;
  
  myTableColumns = [
    { field: 'description', header: 'Description' },
    { field: 'fileType', header: 'File Type' },
    { field: 'delimiter', header: 'De-Limiter' },
    { field: 'source', header: 'Source' }
  ];
 
   fileTemplateColumns = [
   
    { field: 'description', header: 'Description', width: '280px', align: 'left' ,sortable:true},
    { field: 'fileType', header: 'File Type', width: '150px', align: 'left'  ,sortable:true},
    { field: 'delimiter', header: 'De-Limiter', width: '100px', align: 'center' ,sortable:true },
    { field: 'source', header: 'Source', width: '100px', align: 'left'  ,sortable:true},
    { field: 'taggedToProfCnt', header: 'Profile Count', width: '100px', align: 'left'  ,sortable:false}
    
    /*{ field: 'startDate', header: 'Start Date', width: '100px', align: 'left' },
    { field: 'endDate', header: 'End Date', width: '100px', align: 'left' }*/
  ];
   fileTemplatesHeight:any;
  routeSnapshot : ActivatedRouteSnapshot;
 

    constructor(
        private differs: KeyValueDiffers,
        private jhiLanguageService: JhiLanguageService,
        public fileTemplatesService: FileTemplatesService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,  
        private paginationConfig: PaginationConfig,
        private _service: NotificationsService,
        private commonService: CommonService,
        private dateUtils: JhiDateUtils
    ) {
            this.itemsPerPage = ITEMS_PER_PAGE;
            this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.pageSize = ITEMS_PER_PAGE;
            this.pageEvent.pageIndex = 0;
            this.pageEvent.pageSize = this.pageSize;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.columnOptions = [];
    for (let i = 0; i < this.fileTemplateColumns.length; i++) {
      this.columnOptions.push({ label: this.fileTemplateColumns[i].header, value: this.fileTemplateColumns[i] });
    }
    this.subSideNav();
    this.fileTemplatesService.searchData=null;
    }

    loadAll() {
        this.fileTemplatesService.getFileTemplates(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.sortColName,this.sortOrder).subscribe(
            ( res: any ) => {
                this.fileTemplates= res;
                if(  this.fileTemplates &&   this.fileTemplates.length>0)
                {
                    this.length =  this.fileTemplates[0]['totalCount'];
                }
            });

        
        // this.fileTemplatesService.query(
        //         {
        //             page:this.pageEvent.pageIndex+1,
        //             size:this.pageEvent.pageSize,
        //         }).subscribe(
        //         (res: ResponseWrapper) => this.onSuccess(res, res.headers),
        //         (res: ResponseWrapper) => this.onError(res.json())
        //         );
     /* this.fileTemplatesService.search({
        query: this.currentSearch,
//        size: this.itemsPerPage,
        sort: this.sort()
      }).subscribe(
        (res: ResponseWrapper) => this.onSuccess(res.json(), res.headers),
        (res: ResponseWrapper) => this.onError(res.json())
        );
      return;*/
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/file-templates'], {queryParams:
            {
                page: this.page,
            //        size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    subSideNav(){
      this.notify.emit(!this.isClose);
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/file-templates', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
    if (!query) {
      return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.router.navigate(['/file-templates', {
      search: this.currentSearch,
      page: this.page,
      sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    }]);
    this.loadAll();
  }
    ngOnInit() {
        this.fileTemplatesHeight = (this.commonService.screensize().height - 315) + 'px';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInFileTemplates();
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
    $(".ftlSearch md-input-container .mySearchBox").blur(function() {
        var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
            return this.value != '';
        });
        if(value.length<=0) { // zero-length string
            $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
            $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
        }
   });
    }

    searchText(){
        console.log('called search' + this.fileTemplatesService.searchData);
        if(this.fileTemplatesService.searchData && this.fileTemplatesService.searchData != null && this.fileTemplatesService.searchData != '')
            {
            this.fileTemplatesService.search(
                    {
                        page:this.pageEvent.pageIndex+1,
                        size:this.pageEvent.pageSize,
                    }).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json())
                    );
            }
        else
            {
            this.loadAll();
            }
       
    }

    ngOnDestroy() {
        //this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: FileTemplates) {
        return item.id;
    }
    registerChangeInFileTemplates() {
        this.eventSubscriber = this.eventManager.subscribe('fileTemplatesListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
       // this.links = this.parseLinks.parse(headers.get('link'));
      
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.fileTemplates = data.json;
        if(  this.fileTemplates &&   this.fileTemplates.length>0)
        {
            this.length =  this.fileTemplates[0]['totalCount'];
           // this.fileTemplateColumns = this.fileTemplates[0]['columnsList'];
        }
      //  console.log('  this.fileTemplates printed below'+JSON.stringify(this.fileTemplateColumns));
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    onRowSelect(event) {
    console.log(event);
  }
 
  onRowUnselect(event) {
    console.log(event);
  }
    private addNewTemplate(){
        
    }
    sortOrder: string = 'Descending';
    sortColName:string='id';
    changeSourceSort(event) {
     
          this.sortColName =  event.field;
       
      
      if (event.order < 1) {
        this.sortOrder = 'Descending';
      } else {
        this.sortOrder = 'Ascending';
      }
      this.fileTemplatesService.getFileTemplates(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.sortColName,this.sortOrder).subscribe(
       ( res: any ) => {
                this.fileTemplates= res;
                if(  this.fileTemplates &&   this.fileTemplates.length>0)
                {
                    this.length =  this.fileTemplates[0]['totalCount'];
                }
            });
      //this.getGroupByAcctRecords();
    }
}
