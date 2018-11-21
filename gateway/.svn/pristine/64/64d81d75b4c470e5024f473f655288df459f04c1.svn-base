import { Component, OnInit, OnDestroy ,HostListener} from '@angular/core'; 
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import {routerTransition} from '../../layouts/main/route.transition';
import { SourceConnectionDetails } from './source-connection-details.model';
import { SourceConnectionDetailsService } from './source-connection-details.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import {pageSizeOptionsList} from '../../shared/constants/pagination.constants';
import { trigger, transition, style, animate } from '@angular/animations';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { CommonService } from '../common.service';
import {PageEvent} from '@angular/material';

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-source-connection-details',
    templateUrl: './source-connection-details.component.html',
    animations: [routerTransition,
        trigger('slide', [
            transition(':enter', [
              style({
                transform: 'translateX(110%)'
              }),
              animate('0.3s')
            ]),
            transition(':leave', [
              animate('0.3s', style({
                transform: 'translateX(110%)'
              }))
            ])
          ])]
})
export class SourceConnectionDetailsComponent implements OnInit, OnDestroy {
    gb:any;
    gb1:any;
    currentAccount: any;
    length:any;
    pageSize: number;
    pageSizeOptions =pageSizeOptionsList;
    pageEvent: PageEvent = new PageEvent();    
    sourceConnectionDetails: SourceConnectionDetails[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    TemplatesHeight: any;
    mySelectedRows = [];
    columnOptions: SelectItem[];
    sortColumn : string = 'id';
    sortOrder : string = 'desc';
    connectionTableColumns = [                  //  source connection list source columns
           { field: 'description', header: 'Description', width: '30%', align: 'left' },
           { field: 'userName', header: 'user Name', width: '10%', align: 'left' },
        //    { field: 'connectionType', header: 'Connection Type', width: '150px', align: 'left' },
        { field: 'enabledFlag', header: 'Status', width: '10%', align: 'center' },
            { field: 'profCnt', header: 'Profile Count', width: '10%', align: 'left' },
           
    ];
    constructor(
        public sourceConnectionDetailsService: SourceConnectionDetailsService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
         private commonService: CommonService,
    ) {
        this.page = 0;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.pageSize = ITEMS_PER_PAGE;
            this.pageEvent.pageIndex = 0;
            this.pageEvent.pageSize = this.pageSize;
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.columnOptions = [];
            for (let i = 0; i < this.connectionTableColumns.length; i++) {
                this.columnOptions.push({ label: this.connectionTableColumns[i].header, value: this.connectionTableColumns[i] });
            }
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        /* this.sourceConnectionDetailsService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        ); */
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/source-connection-details'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/source-connection-details', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    fetchCons()
    {
        
    this.sourceConnectionDetailsService.query(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.sortColumn,this.sortOrder).subscribe((res: any) => {
            this.sourceConnectionDetails = res;
             if(this.sourceConnectionDetails && this.sourceConnectionDetails.length>0)
                {
                    this.length =  this.sourceConnectionDetails[0]['count'];
                }
        });
    }
    ngOnInit() {
        $(".search-field").css('display','none');
        $(".search-field1").css('display','block');
        this.TemplatesHeight = (this.commonService.screensize().height - 315) + 'px';
        $(".paginator-class").addClass("openPaginator");
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.fetchCons();
        this.registerChangeInSourceConnectionDetails();
        $('.search-icon-body').click(function() {
            if ( $( '.ftlSearch md-input-container' ).hasClass( 'hidethis' ) ) {
                $( '.ftlSearch md-input-container' ).removeClass( 'hidethis' );
                $( '.ftlSearch md-input-container' ).addClass( 'show-this' );
            } else if ( $( '.ftlSearch md-input-container' ).hasClass( 'show-this' ) ) {
                const value = $( '.ftlSearch md-input-container .mySearchBox' ).filter( function() {
                    return this.value ! = '';
                } );
                if ( value.length <= 0 ) { // zero-length string
                    $( '.ftlSearch md-input-container' ).removeClass( 'show-this' );
                    $( '.ftlSearch md-input-container' ).addClass( 'hidethis' );
                }
            } else {
                $( '.ftlSearch md-input-container' ).addClass( 'show-this' );
            }
        } );
        $('.ftlSearch md-input-container .mySearchBox').blur(function() {
            const value = $('.ftlSearch md-input-container .mySearchBox').filter(function() {
                return this.value ! = '';
            });
            if (value.length <= 0) { // zero-length string
                $( '.ftlSearch md-input-container' ).removeClass( 'show-this' );
                $( '.ftlSearch md-input-container' ).addClass( 'hidethis' );
            }
       });
    }

    ngOnDestroy() {
       // this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SourceConnectionDetails) {
        return item.id;
    }
    registerChangeInSourceConnectionDetails() {
        this.eventSubscriber = this.eventManager.subscribe('sourceConnectionDetailsListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.sourceConnectionDetails = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
     changeSourceSort(event)
    {

        this.sortColumn =  event.field;
       
      
        if (event.order < 1) {
          this.sortOrder = 'desc'; 
        } else {
          this.sortOrder = 'asc';
        }
        this.sourceConnectionDetailsService.query(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.sortColumn,this.sortOrder).subscribe((res: any) => {
            this.sourceConnectionDetails = res;
             if(this.sourceConnectionDetails && this.sourceConnectionDetails.length>0)
                {
                    this.length =  this.sourceConnectionDetails[0]['count'];
                }
        });
        //this.profileList = true;
    }
    searchText(){
      //  console.log('called search' + this.fileTemplatesService.searchData);
        if(this.sourceConnectionDetailsService.searchData && this.sourceConnectionDetailsService.searchData != null && this.sourceConnectionDetailsService.searchData != '')
            {
            this.sourceConnectionDetailsService.search(
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
    windowScrollTop: any;
    scrollToTop = 0;
    windowHeight =0;
    @HostListener("window:scroll", [])
        onWindowScroll() {
         const offsetExt = $("#list-table").offset();
         console.log('offsetExt'+JSON.stringify(offsetExt));
         const posExt = offsetExt.top - $(window).scrollTop();
             console.log('posExt'+posExt);
             console.log('$(window).scrollTop()'+($(window).scrollTop()));
             this.windowScrollTop=($(window).scrollTop()+250);
             console.log('$(window).height()'+$(window).height());
             this.windowHeight = $(window).height();
             this.scrollToTop = $(window).scrollTop();
             if(($(window).scrollTop()) == 0){
                  $(".paginator-class").removeClass("scrollPaginator");
                  $(".search-field").css('display','none');
                  $(".search-field1").css('display','block');
                  
                  $(".paginator-class").addClass("openPaginator");
               
                 $(".paginator-class").css("transform","translateY(0px)");
                 $(".paginator-class").removeClass("translate-paginator");
                
              
             }else{
                $(".search-field").css('display','block');
                $(".search-field1").css('display','none');
                $(".paginator-class").addClass("scrollPaginator");
                $(".paginator-class").removeClass("openPaginator");
         }

            
        
            }
            showPaginator=true;
            addPaginator(){
                this.showPaginator = !this.showPaginator;
                $(".paginator-class").css("background-color",'#efeeee');
                $(".paginator-class").css("white-space","nowrap");
            
        
            }
        }
