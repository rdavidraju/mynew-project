/*
  Module Name: Data Views
  Author: AMIT
*/

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { SelectItem, OverlayPanelModule, DialogModule } from 'primeng/primeng';
import { DataViews } from './data-views.model';
import { DataViewsService } from './data-views.service';
import { ITEMS_PER_PAGE, Principal,ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { PageEvent } from '@angular/material';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { CommonService } from '../common.service';
import { NotificationsService } from 'angular2-notifications-lite';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-data-views',
    templateUrl: './data-views.component.html'
})
export class DataViewsComponent implements OnInit, OnDestroy {
    dataViews: DataViews[];
    dataViews1 = [];
    dataViewsList = [];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    mySelectedRows = [];
    columnOptions: SelectItem[];
    viewColumnOptions = [];
    dataViewRecordsLength: number;
    selectedViewRecordsLength: number;
    itemsPerPage: any;
    viewitemsPerPage: any;
    viewitemsPerPage1: any;
    pageSizeOptions = [10, 25, 50, 100];
    pageSize: number;
    pageSizeTemp = [];
    pageEvent: PageEvent = new PageEvent();
    page: number=0;
    page1: number=0;
    viewPage:any;
    viewLength: any;
    viewsList = [];
    viewTableColumns = [];
    viewId: any = '';
    calFun: boolean = false;
    routeSub: any;
    display: boolean = false;
    dataViewTableColumns = [
        { field: 'description', header: 'Description', width: '280px', align: 'left' },
        { field: 'type', header: 'Type', width: '280px', align: 'left' }
    ];
    TemplatesHeight: any;
    sideBarData: any = [];
    selectedTab: number=0;
    relation: any;
    sortColumn : string = 'id';
    sortOrder : string = 'desc';
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private dataViewsService: DataViewsService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private config: NgbDatepickerConfig,
        private commonService: CommonService,
        private router: Router,
        private notificationsService: NotificationsService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.columnOptions = [];
        for (let i = 0; i < this.dataViewTableColumns.length; i++) {
            this.columnOptions.push({ label: this.dataViewTableColumns[i].header, value: this.dataViewTableColumns[i] });
        }
        this.viewitemsPerPage1 = ITEMS_PER_PAGE;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.viewitemsPerPage = ITEMS_PER_PAGE;
    }
    changeMinDate() {
        this.config.minDate = this.dataViews[0].startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    loadAll() {
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        let url = this.activatedRoute.snapshot.url.map(segment => segment.path).join('/');
        this.routeSub = this.activatedRoute.params.subscribe(params => {
            if (params['relation']) {
                this.relation = params['relation'];
                this.dataViewsService.dataViewsWithType(this.page+1, this.itemsPerPage, params['relation'],this.sortColumn,this.sortOrder).subscribe((res: any) => {
                    this.dataViews = res;
                    this.dataViewsService.sideBarDataViewLists().subscribe((res: any) => {
                        this.sideBarData = res;
                        if (this.sideBarData && this.sideBarData[this.selectedTab] && this.sideBarData[this.selectedTab].count) {
                            this.dataViewRecordsLength = this.sideBarData[this.selectedTab].count;}
                    });
                });
            }
            else {
                this.dataViewsService.dataViewsWithType(this.page+1, this.itemsPerPage, '',this.sortColumn,this.sortOrder).subscribe((res: any) => {
                    this.dataViews = res;
                    this.dataViewsService.sideBarDataViewLists().subscribe((res: any) => {
                        this.sideBarData = res;
                        if (this.sideBarData && this.sideBarData[this.selectedTab] && this.sideBarData[this.selectedTab].count) {
                            this.dataViewRecordsLength = this.sideBarData[this.selectedTab].count;}
                    });
                });
            }
        });
        this.TemplatesHeight = 'calc(100vh - 322px)';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });

       /* this.dataViewsService.dataViewLists(this.page + 1, this.itemsPerPage).subscribe((res: Response) => {
            this.dataViews = res.json();

        });*/
        this.registerChangeInDataViews();

        $(".search-icon-body").click(function () {
            if ($(".ftlSearch md-input-container").hasClass("hidethis")) {
                $(".ftlSearch md-input-container").removeClass("hidethis");
                $(".ftlSearch md-input-container").addClass("show-this");
            } else if ($(".ftlSearch md-input-container").hasClass("show-this")) {
                var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if (value.length <= 0) {
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
            if (value.length <= 0) {
                $(".ftlSearch md-input-container").removeClass("show-this");
                $(".ftlSearch md-input-container").addClass("hidethis");
            }
        });
    }

    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, this.relation,this.sortColumn,this.sortOrder).subscribe((res: any) => {
            this.dataViews = res;
            if (this.sideBarData && this.sideBarData[this.selectedTab] && this.sideBarData[this.selectedTab].count) {
                this.dataViewRecordsLength = this.sideBarData[this.selectedTab].count;}
        });
    }
    ngOnDestroy() {

    }

    trackId(index: number, item: DataViews) {
        return item.id;
    }

    registerChangeInDataViews() {
        this.eventSubscriber = this.eventManager.subscribe('dataViewsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    onRowSelect(event) {
        console.log('id ' + JSON.stringify(event.data.id));
        this.viewId = event.data.id;
        this.calFun = true;
    }

    selectedView:any;
    selectedViewName:any;
    showView(event, col) {
        this.selectedViewName = '';
        this.viewsList = [];
        this.viewColumnOptions = [];
        this.viewTableColumns = [];
        console.log('event ' + JSON.stringify(event));
        console.log('col ' + JSON.stringify(col));
        this.selectedView = col.id;
        this.selectedViewName = col.dataViewDispName;
        this.page1 = 0;
            this.dataViewsService.getViewDetails(col.id,this.page1, this.viewitemsPerPage,).subscribe((res: any) => {
                this.viewTableColumns = res.json().columnsWithAttributeNames;
                for (let i = 0; i < res.json().columnsWithAttributeNames.length; i++) {
                    this.viewColumnOptions.push({ label: res.json().columnsWithAttributeNames[i].header, value: res.json().columnsWithAttributeNames[i] });
                }
                console.log('this.viewColumnOptions ' + JSON.stringify(this.viewColumnOptions));
                if(this.viewColumnOptions.length>0){
                    this.viewsList = res.json().dataView.mapList;
                    this.selectedViewRecordsLength = +res.headers.get('x-count');
                    this.display = true;
                    console.log('this.viewsList ' + JSON.stringify(this.viewsList));
                    // if (this.viewsList  && this.viewsList.length) {
                    // } else {
                    //     this.notificationsService.info('Warning!', 'No data to view');
                    // }
                    console.log('this.viewColumnOptions ' + JSON.stringify(this.viewColumnOptions));
                }
                /* 
                    if (this.viewsList.length > 0) {
                    let temp = Object.keys(this.viewsList[0]);
                    temp.forEach(element => {
                        this.viewTableColumns.push({ field: element, header: element });
                    });
                    if (this.viewTableColumns.length > 0) {
                        for (let i = 0; i < this.viewTableColumns.length; i++) {
                            let obj = {
                                "label": this.viewTableColumns[i].header,
                                "value": this.viewTableColumns[i]
                            }
                            this.viewColumnOptions.push(obj);
                        }
                        this.display = true;
                        console.log('this.viewColumnOptions ' + JSON.stringify(this.viewColumnOptions));
                    }
                }else{
                    this.display = true;
                }
                */
                /* if (res.columnsWithAttributeNames.length > 0) {
                    let temp = Object.keys(this.viewsList[0]);
                    res.columnsWithAttributeNames.forEach(element => {
                        this.viewTableColumns.push({ field: element, header: element });
                    });
                    if (this.viewTableColumns.length > 0) {
                        for (let i = 0; i < this.viewTableColumns.length; i++) {
                            let obj = {
                                "label": this.viewTableColumns[i].header,
                                "value": this.viewTableColumns[i]
                            }
                            this.viewColumnOptions.push(obj);
                        }
                        this.display = true;
                        console.log('this.viewColumnOptions ' + JSON.stringify(this.viewColumnOptions));
                    }
                }else{
                    this.display = true;
                } */
            });
    }

    onPaginateChangeView(event) {
        this.page1 = event.pageIndex;
        this.viewitemsPerPage = event.pageSize;
        this.dataViewsService.getViewDetails(this.selectedView,this.page1, this.viewitemsPerPage,).subscribe((res: any) => {
            this.viewsList = res.json().dataView.mapList;
            this.selectedViewRecordsLength = +res.headers.get('x-count');
        });
    }
    loadContent(e) {
        this.selectedTab = e.index;
        this.relation = this.sideBarData[e.index].relation;
        this.page = 0;
        this.router.navigate(['/data-views', { outlets: { 'content': [this.relation] + '/list' } }]);
    }
    changeSourceSort(event) {
     
        this.sortColumn =  event.field;
     
    
    if (event.order < 1) {
      this.sortOrder = 'desc';
    } else {
      this.sortOrder = 'asc';
    }
    this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, this.relation,this.sortColumn,this.sortOrder).subscribe((res: any) => {
        this.dataViews = res;
        if (this.sideBarData && this.sideBarData[this.selectedTab] && this.sideBarData[this.selectedTab].count) {
            this.dataViewRecordsLength = this.sideBarData[this.selectedTab].count;}
    });
    //this.getGroupByAcctRecords();
  }
  searchText(){
    console.log('called search' + this.dataViewsService.searchData);
    if(this.dataViewsService.searchData && this.dataViewsService.searchData != null && this.dataViewsService.searchData != '')
        {
        this.dataViewsService.search(
                {
                    page:this.page - 1,
                    size:this.itemsPerPage,
                }).subscribe((res: any) => {
                    this.dataViews = res;
                }
                );
        }
    else
        {
        //this.loadAll();
        }
   
}

    physicalViewExport() {
        this.dataViewsService.getDataViewsDataCSV(this.selectedView).subscribe(res => {
            if (res.status == 'success') {
                let link = document.createElement('a');
                link.setAttribute('download', null);
                link.setAttribute('href', res.path);
                link.click();
            }
        }, err => this.notificationsService.error('Warning!', 'Error Occured'));
    }

}
