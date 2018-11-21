import { Component, OnInit, OnDestroy } from '@angular/core';
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
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    TemplatesHeight: any;
    tenantConfigModule:any = [];
    columnOptions: SelectItem[];
    tenantDetailsColumns = [
        { field: 'primaryContact', header: 'Primary Contact', width: '180px', align: 'left' },
        { field: 'corporateAddress', header: 'Corporate Address', width: '180px', align: 'left' },
        { field: 'website', header: 'Website', width: '250px', align: 'left' },
        { field: 'domainName', header: 'Domain Name', width: '280px', align: 'left' }
    ];

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
        private commonService: CommonService
    ) {
        this.columnOptions = [];
        for (let i = 0; i < this.tenantDetailsColumns.length; i++) {
            this.columnOptions.push({ label: this.tenantDetailsColumns[i].header, value: this.tenantDetailsColumns[i] });
        }
        this.itemsPerPage = ITEMS_PER_PAGE;
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
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        
        this.tenantDetailsService.fetchAllTenantList().subscribe(
                (res:any) => {
                    this.tenantDetailsList = res;
                    console.log('tenant Details :' + JSON.stringify(this.tenantDetailsList));
        });
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

    ngOnDestroy() {
        /* this.eventManager.destroy(this.eventSubscriber); */
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
onRowSelect(event)
    {
    
}
}
