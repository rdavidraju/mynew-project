import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { LedgerDefinition } from './ledger-definition.model';
import { LedgerDefinitionService } from './ledger-definition.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, User, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CommonService } from '../../entities/common.service';
import { Response } from '@angular/http';
import { SelectItem } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-ledger-definition',
    templateUrl: './ledger-definition.component.html'
})
export class LedgerDefinitionComponent implements OnInit {

currentAccount: any;
ledgerDefinitions: LedgerDefinition[];
error: any;
success: any;
eventSubscriber: Subscription;
currentSearch: string;
routeData: any;
links: any;
totalItems: any;
queryCount: any;
itemsPerPage: any = 10;
ledgerDefinitionRecordsLength: number;
pageSizeOptions = [10, 25, 50, 100];
page: any = 0;
predicate: any;
previousPage: any;
reverse: any;
TemplatesHeight: any;
columnOptions: SelectItem[];
ledgerDefinitionTableColumns = [
    { field: 'description', header: 'Description', width: '200px', align: 'left' },
    { field: 'ledgerType', header: 'Ledger Type', width: '200px', align: 'left' },
    { field: 'currency', header: 'Currency', width: '200px', align: 'left' },
    // { field: 'calendarId', header: 'Calendar Id', width: '200px', align: 'left' },
    { field: 'startDate', header: 'Start Date', width: '200px', align: 'left' },
    { field: 'endDate', header: 'End Date', width: '200px', align: 'left' },
];

    constructor(
        private ledgerDefinitionService: LedgerDefinitionService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService
    ) {
        this.columnOptions = [];
        for(let i = 0; i < this.ledgerDefinitionTableColumns.length; i++) {
            this.columnOptions.push({label: this.ledgerDefinitionTableColumns[i].header, value: this.ledgerDefinitionTableColumns[i]});
        }

        /* this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : ''; */
    }


    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        // this.registerChangeInLedgerDefinitions();

        /** Function for Toggling Global Search **/
        $(".search-icon-body").click(function () {
            if ($(".ftlSearch md-input-container").hasClass("hidethis")) {
                $(".ftlSearch md-input-container").removeClass("hidethis");
                $(".ftlSearch md-input-container").addClass("show-this");
            } else if ($(".ftlSearch md-input-container").hasClass("show-this")) {
                var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if (value.length <= 0) { // zero-length string
                    $(".ftlSearch md-input-container").removeClass("show-this");
                    $(".ftlSearch md-input-container").addClass("hidethis");
                }
            } else {
                $(".ftlSearch md-input-container").addClass("show-this");
            }
        });
        $(".ftlSearch md-input-container .mySearchBox").blur(function () {
            var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".ftlSearch md-input-container").removeClass("show-this");
                $(".ftlSearch md-input-container").addClass("hidethis");
            }
        });
    }


    /**Load all ledgers */
    loadAll(){
        this.ledgerDefinitionService.getAllLedgerDefinition(this.page, this.itemsPerPage).subscribe((res: any) => {
            this.ledgerDefinitions = res.json();
            this.ledgerDefinitionRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    /**Pagination function */
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.ledgerDefinitionService.getAllLedgerDefinition(this.page+1, this.itemsPerPage).subscribe((res: any) => {
            this.ledgerDefinitions = res.json();
            this.ledgerDefinitionRecordsLength =+ res.headers.get('x-total-count');
        });
    }










/*     loadAll() {
        if (this.currentSearch) {
            this.ledgerDefinitionService.search({
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.ledgerDefinitionService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
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
    transition() {
        this.router.navigate(['/ledger-definition'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/ledger-definition', {
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
        this.router.navigate(['/ledger-definition', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: LedgerDefinition) {
        return item.id;
    }
    registerChangeInLedgerDefinitions() {
        this.eventSubscriber = this.eventManager.subscribe('ledgerDefinitionListModification', (response) => this.loadAll());
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
        this.ledgerDefinitions = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }*/
} 