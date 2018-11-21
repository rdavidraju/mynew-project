import { Component, OnInit, OnDestroy, Renderer2, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { TenantDetails } from './tenant-details.model';
import { TenantDetailsService } from './tenant-details.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
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
    selector: 'jhi-tenant-details',
    templateUrl: './tenant-details.component.html'
})
export class TenantDetailsComponent implements OnInit, OnDestroy {

    currentAccount: any;
    tenantDetailsList:any = [];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    // itemsPerPage: any;
    page: any = 0;
    predicate: any;
    previousPage: any;
    reverse: any;
    TemplatesHeight: any;
    tenantConfigModule:any = [];
    tenantDetailsColumns = [
        { field: 'primaryContact', header: 'Primary Contact', width: '20%', align: 'left' },
        { field: 'corporateAddress', header: 'Corporate Address', width: '25%', align: 'left' },
        /* { field: 'website', header: 'Website', width: '20%', align: 'left' }, */
        // { field: 'domainName', header: 'Domain Name', align: 'left' }
    ];
    documentListener: any;
    tenantRecordsLength: number;
    itemsPerPage: any = 25;
    pageSizeOptions = [10, 25, 50, 100];
    pageSize: number;
    defaultImg: string = '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';

    constructor(
        private tenantDetailsService: TenantDetailsService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService,
        private renderer: Renderer2,
        private elem: ElementRef
    ) {
        this.documentListener = this.renderer.listen('document', 'click', (evt) => {
            if (this.lastElem && !evt.target.classList.contains('split-btn-open') && !evt.target.classList.contains('drop-icon')) {
                this.renderer.removeClass(this.lastElem, 'dis-dropdown');
            }
        });

        /* this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        }); */
    }

    loadAll() {
       /*  this.tenantDetailsService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        ); */
    }
    loadPage(page: number) {
        /* if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        } */
    }
    transition() {
        /* this.router.navigate(['/tenant-details'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll(); */
    }

    clear() {
       /*  this.page = 0;
        this.router.navigate(['/tenant-details', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll(); */
    }
    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        // this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

        this.fetchAllTenants();
       /*  this.tenantDetailsService.fetchTenantDetails().subscribe(
                (res:any) => {
                    this.tenantDetails = res;
                    console.log('tenant Details :' + JSON.stringify(this.tenantDetails));
        });

        this.tenantDetailsService.fetchTenantConfigModule().subscribe(
                (res:any) => {
                    this.tenantConfigModule = res;
                    console.log('tenantConfigModule Details :' + JSON.stringify(this.tenantConfigModule));
        }); */
        
        this.registerChangeInTenantDetails();

        /** Function for Toggling Global Search **/
        $(".search-icon-body").click(function () {
            if ($(".search-icon").hasClass("hidethis")) {
                $(".search-icon").removeClass("hidethis");
                $(".search-icon").addClass("show-this");
                $('#mySearchBox').focus();
                // $(".search-icon").focus();
            } else if ($(".search-icon").hasClass("show-this")) {
                var value = $('.search-icon .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if (value.length <= 0) { // zero-length string
                    $(".search-icon").removeClass("show-this");
                    $(".search-icon").addClass("hidethis");
                }
            } else {
                $(".search-icon").addClass("show-this");
            }
        });
        $(".search-icon .mySearchBox").blur(function () {
            var value = $('.search-icon .mySearchBox').filter(function () {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".search-icon").removeClass("show-this");
                $(".search-icon").addClass("hidethis");
            }
        });
    }

    fetchAllTenants() {
        this.tenantDetailsService.fetchAllTenantList(this.page, this.itemsPerPage).subscribe(res => {
            this.tenantDetailsList = res.json();
            this.tenantRecordsLength= +res.headers.get('x-total-count');
        });
    }

    onPaginateChange(evt) {
        this.page = evt.pageIndex;
        this.itemsPerPage = evt.pageSize;
        this.tenantDetailsService.fetchAllTenantList(this.page+1, this.itemsPerPage).subscribe(res => {
            this.tenantDetailsList = res.json();
            this.tenantRecordsLength = +res.headers.get('x-total-count');
        });
    }



    ngOnDestroy() {
        /* this.eventManager.destroy(this.eventSubscriber); */
        this.documentListener();
    }

    trackId(index: number, item: TenantDetails) {
        /* return item.id; */
    }
    registerChangeInTenantDetails() {
        this.eventSubscriber = this.eventManager.subscribe('tenantDetailsListModification', (response) => this.loadAll());
    }

    sort() {
       /*  const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result; */
    }

    private onSuccess(data, headers) {
        /* this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.tenantDetails = data; */
    }
    private onError(error) {
        /* this.alertService.error(error.message, null, null); */
    }

    currentRow: any;
    action(type?, data?) {
        if (type != 'edit') {
            this.currentRow = data;
        }
        if (type == 'view') {
            this.router.navigate(['/tenant-details', {outlets: {'content': [this.currentRow.id]+'/details'}}]);
        } else if (type == 'edit'){
            this.router.navigate(['/tenant-details', {outlets: {'content': [this.currentRow.id]+'/edit'}}]);
        }
    }

    lastElem: any;
    splitClose(list) {
        if(this.lastElem && this.lastElem != list)
        this.renderer.removeClass(this.lastElem, 'dis-dropdown');
        if (list.classList.contains('dis-dropdown')) {
            this.renderer.removeClass(list, 'dis-dropdown');
        } else {
            this.renderer.addClass(list, 'dis-dropdown');
        }
        this.lastElem = list;
    }
}
