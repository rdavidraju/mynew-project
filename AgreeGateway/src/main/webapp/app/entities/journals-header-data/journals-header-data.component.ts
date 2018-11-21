import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { JournalsHeaderData } from './journals-header-data.model';
import { JournalsHeaderDataService } from './journals-header-data.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
import { CommonService } from '../common.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-journals-header-data',
    templateUrl: './journals-header-data.component.html'
})
export class JournalsHeaderDataComponent implements OnInit, OnDestroy {

currentAccount: any;
    journalsHeaderData: JournalsHeaderData[];
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
    columnOptions: SelectItem[];
    TemplatesHeight:any;
    mySelectedRows = [];
    journalsTableColumns = [
        /* { field: 'description', header: 'Description' }, */
        { field: 'jeBatchName', header: 'JE Batch Name' },
        { field: 'period', header: 'JE Period' }
    ];

    constructor(
        private journalsHeaderDataService: JournalsHeaderDataService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService,
        private config: NgbDatepickerConfig,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.columnOptions = [];
        for(let i = 0; i < this.journalsTableColumns.length; i++) {
               this.columnOptions.push({ label: this.journalsTableColumns[i].header, value: this.journalsTableColumns[i] });
        }   
    }
    changeMinDate() {
        this.config.minDate = this.journalsHeaderData[0].startDate;
    }

    resetMinDate() {
       this.config.minDate = { year: 1950, month: 1, day: 1 };
    }
    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/journals-header-data', {
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
        this.router.navigate(['/journals-header-data', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 300) + 'px';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.journalsHeaderDataService.journalsBatchList().subscribe((res:any)=>{
            this.journalsHeaderData = res;
            console.log('journalsHeaderData List :' + JSON.stringify(this.journalsHeaderData));
        
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
        $(".ftlSearch md-input-container .mySearchBox").blur(function() {
            var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if(value.length<=0) { // zero-length string
                $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
            }
       });
        this.registerChangeInJournalsHeaderData();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: JournalsHeaderData) {
        return item.id;
    }
    registerChangeInJournalsHeaderData() {
        this.eventSubscriber = this.eventManager.subscribe('journalsHeaderDataListModification', (response) => this.loadAll());
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
        this.journalsHeaderData = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    loadAll() {
        /* if (this.currentSearch) {
            this.journalsHeaderDataService.search({
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.journalsHeaderDataService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        ); */
    }
}

    /* loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    } */
    /* transition() {
        this.router.navigate(['/journals-header-data'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    } */
