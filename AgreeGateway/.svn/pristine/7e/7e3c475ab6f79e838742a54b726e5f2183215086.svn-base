import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { SelectItem } from 'primeng/primeng';
import { Functionality } from './functionality.model';
import { FunctionalityService } from './functionality.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, User, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CommonService } from '../../entities/common.service';
import { UserRoleAssignment } from '../../entities/roles/user-role-assignment.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-functionality',
    templateUrl: './functionality.component.html'
})
export class FunctionalityComponent implements OnInit, OnDestroy {

currentAccount: any;
    functionalities: Functionality[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any = 10;
    page: any = 0;
    pageSizeOptions = [10, 25, 50, 100];
    functionalityRecordsLength: number;
    predicate: any;
    previousPage: any;
    reverse: any;
    columnOptions: SelectItem[];
    TemplatesHeight: any;
    functionalityTableColumns = [
        // { field: 'id', header: 'Id', width: '150px', align: 'left' },
        { field: 'funcDesc', header: 'Description', width: '200px', align: 'left' },
        { field: 'funcType', header: 'Type', width: '200px', align: 'left' },
        // { field: 'tenantId', header: 'Tenant Id', width: '150px', align: 'left' },
        /* { field: 'startDate', header: 'Start Date', width: '200px', align: 'left' },
        { field: 'endDate', header: 'End Date', width: '200px', align: 'left' }, */
        // { field: 'activeInd', header: 'Status', width: '200px', align: 'left' },
    ];

    constructor(
        private functionalityService: FunctionalityService,
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
        for(let i = 0; i < this.functionalityTableColumns.length; i++) {
            this.columnOptions.push({label: this.functionalityTableColumns[i].header, value: this.functionalityTableColumns[i]});
        }

        /* this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        }); */
    }

    /* loadAll() {
        this.functionalityService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: Functionality) => this.onSuccess(res, res),
            (res: Functionality) => this.onSuccess(res, res),
            // (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            // (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/functionality'], {queryParams:
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
        this.router.navigate(['/functionality', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    } */
    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        // this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        // this.registerChangeInFunctionalities();

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
        // console.log('this.page: ' + this.page + '\n' + 'this.itemsPerPage: ' + this.itemsPerPage);
        this.functionalityService.getListFuncByTenantIdWithPagination(this.page, this.itemsPerPage).subscribe((res: any)=>{
            this.functionalities = res.json();
            this.functionalityRecordsLength = + res.headers.get('x-total-count');
        });

    }

    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        // console.log('this.page: ' + this.page + '\n' + 'this.itemsPerPage: ' + this.itemsPerPage);
        this.functionalityService.getListFuncByTenantIdWithPagination(this.page+1, this.itemsPerPage).subscribe((res: any)=>{
            this.functionalities = res.json();
            this.functionalityRecordsLength = + res.headers.get('x-total-count');
        });
    }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
    }

    /* trackId(index: number, item: Functionality) {
        return item.id;
    }
    registerChangeInFunctionalities() {
        this.eventSubscriber = this.eventManager.subscribe('functionalityListModification', (response) => this.loadAll());
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
        // this.totalItems = headers.get('X-Total-Count');
        // this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.functionalities = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    } */
}
