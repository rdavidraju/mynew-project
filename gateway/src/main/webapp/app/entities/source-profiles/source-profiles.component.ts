import { Component, OnInit, OnDestroy,HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { SourceProfiles, SourceProfileswithConnections } from './source-profiles.model';
import { SourceProfilesService } from './source-profiles.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
import {PageEvent} from '@angular/material';
import { CommonService } from '../common.service';
import {routerTransition} from '../../layouts/main/route.transition';
import { trigger, transition, style, animate } from '@angular/animations';
import {pageSizeOptionsList} from '../../shared/constants/pagination.constants';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-source-profiles',
    templateUrl: './source-profiles.component.html',
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
export class SourceProfilesComponent implements OnInit, OnDestroy {
    gb:any;
    length: number;
     pageSize: number;
     pageSizeOptions = pageSizeOptionsList;
     pageEvent: PageEvent = new PageEvent();
    currentAccount: any;
    sourceProfiles:SourceProfileswithConnections[];
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
    TemplatesHeight:any;
    reverse: any;
    mySelectedRows = [];                  // for profiles list selection
    columnOptions: SelectItem[];
    profileList: boolean;               // flag variable for to show or hide Profile templates list 
    profileTableColumns = [                  //  profiles list source columns
        { field: 'sourceProfileName',   header: 'Profile Name', width: '30%', align: 'left' },
        { field: 'profileDescription',header: 'Description', width: '30%', align: 'left' },
        { field: 'connectionName',  header: 'Connection Name', width: '30%', align: 'left' },
        { field: 'enabledFlag', header: 'Status', width: '10%', align: 'center' },
        { field: 'taggedProfCnt',  header: 'Template count', width: '20%', align: 'left' }
        
        
       /* { field: 'startDate', header: 'Start Date' },
        { field: 'endDate', header: 'End Date' }*/
        
      
    ];
    searchData:any;
    constructor(
        public sourceProfilesService: SourceProfilesService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private cs: CommonService
    ) {
        
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.pageSize = ITEMS_PER_PAGE;
            this.pageEvent.pageIndex = 0;
            this.pageEvent.pageSize = this.pageSize;
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.columnOptions = [];
        for (let i = 0; i < this.profileTableColumns.length; i++) {
            this.columnOptions.push({ label: this.profileTableColumns[i].header, value: this.profileTableColumns[i] });
        }
        this.sourceProfilesService.searchData=null;

    }
    loadAllProfiles() { //Load all profiles with their connections information
        //console.log('this.pageEvent.pageIndex+1,this.pageEvent.pageSize'+this.pageEvent.pageIndex+this.pageEvent.pageSize);
        this.sourceProfilesService.fetchProfilesForTenant(this.pageEvent.pageIndex,this.pageEvent.pageSize,this.sortColName,this.sortOrder).subscribe(
            ( res: any ) => {
                this.sourceProfiles = res;
               // console.log('in comp'+JSON.stringify(this.sourceProfiles ));
                if(this.sourceProfiles && this.sourceProfiles.length>0)
                {
                    this.length =  this.sourceProfiles[0]['count'];
                }
                //console.log("sourceProfiles = " + JSON.stringify(this.sourceProfiles));
            },
            ( res: ResponseWrapper ) => this.onError( res )
        );

        this.profileList = true;
    }
    loadAll() {
        this.sourceProfilesService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
            );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    searchText(){
       // console.log('called search' + this.sourceProfilesService.searchData);
        if(this.sourceProfilesService.searchData && this.sourceProfilesService.searchData != null && this.sourceProfilesService.searchData != ''){
            this.sourceProfilesService.search(
                    {
                        page:this.page - 1,
                        size:this.itemsPerPage,
                    },this.sortColName,this.sortOrder).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json())
                    );
            } else  {
            //this.loadAll();
            }
       
    }
    transition() {
        this.router.navigate(['/source-profiles'], {
            queryParams:
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
        this.router.navigate(['/source-profiles', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        $(".paginator-class").addClass("openPaginator");
        this.TemplatesHeight = (this.cs.screensize().height - 315) + 'px';
        this.loadAllProfiles();
        this.principal.identity().then(( account ) => {
            this.currentAccount = account;
        } );
        //        this.registerChangeInSourceProfiles();

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
       //this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SourceProfiles) {
        return item.id;
    }
    registerChangeInSourceProfiles() {
        this.eventSubscriber = this.eventManager.subscribe('sourceProfilesListModification', (response) => this.loadAll());
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
        this.sourceProfiles = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    
    public addNewProfile(){
        this.cs.recentBreadCrumbs=[];
        this.router.navigate(['/source-profiles', {outlets: { 'content': ['source-profiles-new']}}]);
    }
    sortOrder: string = 'desc';
    sortColName:string='id';
    changeSourceSort(event)
    {
        if(  event.field != 'taggedProfCnt')
        {
            this.sortColName =  event.field;
       
      
            if (event.order < 1) {
              this.sortOrder = 'desc'; 
            } else {
              this.sortOrder = 'asc';
            }
            this.sourceProfilesService.fetchProfilesForTenant(this.pageEvent.pageIndex,this.pageEvent.pageSize,this.sortColName,this.sortOrder).subscribe(
                ( res: any ) => {
                    this.sourceProfiles = res;
                   // console.log('in comp'+JSON.stringify(this.sourceProfiles ));
                    if(this.sourceProfiles && this.sourceProfiles.length>0)
                    {
                        this.length =  this.sourceProfiles[0]['count'];
                    }
                    //console.log("sourceProfiles = " + JSON.stringify(this.sourceProfiles));
                },
                ( res: ResponseWrapper ) => this.onError( res )
            );
    
            this.profileList = true;
        }
      
    }
    showPaginator=true;
    addPaginator(){
        this.showPaginator = !this.showPaginator;
        $(".paginator-class").css("background-color",'#efeeee');
        $(".paginator-class").css("white-space","nowrap");
    

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
                  
                  $(".paginator-class").addClass("openPaginator");
              
                 $(".paginator-class").css("transform","translateY(0px)");
                 $(".paginator-class").removeClass("translate-paginator");
               
             }else{
                $(".paginator-class").addClass("scrollPaginator");
                $(".paginator-class").removeClass("openPaginator");
           
               
             }

            
        
            }
}
