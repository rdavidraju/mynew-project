import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CommonService } from '../../entities/common.service';
import { Response } from '@angular/http';
import { SelectItem } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
import { NotificationsService } from 'angular2-notifications-lite';
import { SessionStorageService } from 'ng2-webstorage';

import { LookUpCode } from './look-up-code.model';
import { LookUpCodeService } from './look-up-code.service';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-look-up-code',
    templateUrl: './look-up-code.component.html'
})
export class LookUpCodeComponent implements OnInit {

    currentAccount: any;
    lookUpCodes: LookUpCode[];
    editLookUpCodeDetails: any = [];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any = 10;
    lookupCodesRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    predicate: any;
    previousPage: any;
    reverse: any;
    TemplatesHeight: any;
    columnOptions: SelectItem[];
    lookupCodesTableColumns = [
        // { field: 'lookUpType', header: 'Lookup Type', width: '200px', align: 'left' },
        { field: 'meaning', header: 'Meaning', width: '200px', align: 'left' },
        { field: 'description', header: 'Description', width: '200px', align: 'left' },
        /* { field: 'activeStartDate', header: 'Start Date', width: '200px', align: 'left' },
        { field: 'activeEndDate', header: 'End Date', width: '200px', align: 'left' }, */
    ];
    /* All lookup types list */
    lookupTypesList: any;
    /* Get selected Lookup type */
    lookupType: any = [];
    /* Edit Lookup Code Dialog */
    editLookupCodeDialog: boolean = false;
    /**Dropdown Setting */
    dropdownSettings:any = {};
    /**Lookuptype dropdown */
    lookupTypesDropDown:any = [];

    constructor(
        private lookUpCodeService: LookUpCodeService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService
    ) {
        this.columnOptions = [];
        for(let i = 0; i < this.lookupCodesTableColumns.length; i++) {
            this.columnOptions.push({label: this.lookupCodesTableColumns[i].header, value: this.lookupCodesTableColumns[i]});
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
        /* Get all lookup type and get first lookuptype LookUpCode */
        this.getAllLookupTypes();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        // this.registerChangeInLookUpCodes();

        //MultiSelect Setting for Purpose
        this.dropdownSettings = { 
            singleSelection: true, 
            text:"Lookup Type",
            enableSearchFilter: true,
          }; 

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

    /**Load all lookup codes */
    loadAll(type){
        if(type){
            this.$sessionStorage.store('selcetedLookupType', this.lookupType);
            this.lookUpCodeService.getAllLookups(type).subscribe((res: any) => {
                this.lookUpCodes = res;
            });
        }
    }

    /**Pagination function */
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.lookUpCodeService.getAllLookupCodesPagination(this.page+1, this.itemsPerPage).subscribe((res: any) => {
            this.lookUpCodes = res.json();
            this.lookupCodesRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    /** Get All Lookup Types List*/
    getAllLookupTypes(){
        this.lookUpCodeService.getAllLookupTypes().subscribe((res)=>{
            // console.log('lookuptype\n'+JSON.stringify(res));
            this.lookupTypesList = res;
            this.lookupTypesList.forEach(lookup => {
                let obj = {
                    "id": lookup.lookUpType,
                    "itemName": lookup.meaning
                }
                this.lookupTypesDropDown.push(obj);
            });
            if(this.$sessionStorage.retrieve('selcetedLookupType')){
                this.lookupType = this.$sessionStorage.retrieve('selcetedLookupType');
                this.loadAll(this.lookupType[0].id);
            }else if(this.lookupTypesList.length > 0){
                let obj = {
                    "id": this.lookupTypesList[0].lookUpType,
                    "itemName": this.lookupTypesList[0].meaning
                }
                this.lookupType.push(obj);
                this.loadAll(this.lookupType[0].id);
            }
        });
    }

    /**Edit Lookup Code */
    editLookupCode(lookupcode){
        if(this.editLookupCodeDialog == false){
        lookupcode.activeStartDate = this.dateUtils.convertLocalDateFromServer(lookupcode.activeStartDate);
        lookupcode.activeEndDate = this.dateUtils.convertLocalDateFromServer(lookupcode.activeEndDate);
        this.editLookUpCodeDetails = Object.assign({}, lookupcode);
        // console.log('this.editLookUpCodeDetails\n'+JSON.stringify(this.editLookUpCodeDetails));
        this.editLookupCodeDialog = true;
        }
    }

    updateLookupCode(){
        /* Update LookUpCode */
        // console.log('this.editLookUpCodeDetails\n'+JSON.stringify(this.editLookUpCodeDetails));
        this.lookUpCodeService.update(this.editLookUpCodeDetails).subscribe((res) => {
            // console.log('lookupcode updated\n'+JSON.stringify(res));
            this.notificationsService.success('Success!', 'Lookup Code Successfully Updated');
            this.editLookupCodeDialog = false;
            this.loadAll(this.lookupType[0].id);
        });
    }

    /**Cancel Edit */
    dialogHide(){
        this.loadAll(this.lookupType[0].id);
    }

    changeLookup(){
        this.loadAll(this.lookupType[0].id);
    }




    /* loadAll() {
        if (this.currentSearch) {
            this.lookUpCodeService.search({
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.lookUpCodeService.query({
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
        this.router.navigate(['/look-up-code'], {queryParams:
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
        this.router.navigate(['/look-up-code', {
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
        this.router.navigate(['/look-up-code', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: LookUpCode) {
        return item.id;
    }
    registerChangeInLookUpCodes() {
        this.eventSubscriber = this.eventManager.subscribe('lookUpCodeListModification', (response) => this.loadAll());
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
        this.lookUpCodes = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    } */
}
