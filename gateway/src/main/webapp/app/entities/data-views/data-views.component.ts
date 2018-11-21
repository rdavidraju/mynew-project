/*
  Module Name: Data Views
  Author: AMIT
*/

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { ITEMS_PER_PAGE } from '../../shared';
import { PageEvent } from '@angular/material';
import { DataViews } from './data-views.model';
import { DataViewsService } from './data-views.service';
import { CommonService } from '../common.service';

@Component({
    selector: 'jhi-data-views',
    templateUrl: './data-views.component.html'
})
export class DataViewsComponent implements OnInit, OnDestroy {
    dataViews: DataViews[];
    dataViews1 = [];
    dataViewsList = [];
    mySelectedRows = [];
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
    page = 0;
    page1 = 0;
    viewPage: any;
    viewLength: any;
    viewsList = [];
    viewTableColumns = [];
    viewId: any = '';
    calFun = false;
    display = false;
    relation: any;
    sortColumn = 'id';
    sortOrder = 'desc';
    relations: any[];
    selectedView: any;
    selectedViewName: any;
    unsubscribe: Subject<void> = new Subject();

    constructor(
        public dataViewsService: DataViewsService,
        private activatedRoute: ActivatedRoute,
        private commonService: CommonService,
        private router: Router
    ) {
        this.viewitemsPerPage1 = ITEMS_PER_PAGE;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.viewitemsPerPage = ITEMS_PER_PAGE;
    }

    /** 
     * @description Init
    */
    ngOnInit() {
        this.activatedRoute.params.takeUntil(this.unsubscribe).subscribe((params) => {
            this.relation = params['relation'] ? params['relation'] : 'All';
            this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, params['relation'] ? params['relation'] : '', this.sortColumn, this.sortOrder).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.dataViews = res.json();
                if (!res.headers) return;
                this.relations = JSON.parse(res.headers.get('relationCounts'));
                this.dataViewRecordsLength = res.headers.get('x-total-count');
            });
        });
    }

    /**
     * @description Data Source List Pagnation
     * @param evt
     */
    onPaginateChange(evt) {
        this.page = evt.pageIndex;
        this.itemsPerPage = evt.pageSize;
        this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, this.relation, this.sortColumn, this.sortOrder).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.dataViews = res.json();
            if (!res.headers) return;
            this.relations = JSON.parse(res.headers.get('relationCounts'));
            this.dataViewRecordsLength = res.headers.get('x-total-count');
        });
    }

    /** 
     * @description Component Destroy
    */
    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }


    /**
     * @description Physical View
     * @param evt 
     * @param col 
     */
    showView(evt, col) {
        this.selectedViewName = '';
        this.viewsList = [];
        this.viewColumnOptions = [];
        this.viewTableColumns = [];
        this.selectedView = col.id;
        this.selectedViewName = col.dataViewDispName;
        this.page1 = 0;
        this.dataViewsService.getViewDetails(col.id, this.page1, this.viewitemsPerPage, ).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.viewTableColumns = res.json().columnsWithAttributeNames;
            for (let i = 0; i < res.json().columnsWithAttributeNames.length; i++) {
                this.viewColumnOptions.push({ label: res.json().columnsWithAttributeNames[i].header, value: res.json().columnsWithAttributeNames[i] });
            }
            if (this.viewColumnOptions.length > 0) {
                this.viewsList = res.json().dataView.mapList;
                this.selectedViewRecordsLength = +res.headers.get('x-count');
                this.display = true;
            }
        });
    }

    /**
     * @description Physical View Pagnation
     * @param evt 
     */
    onPaginateChangeView(evt) {
        this.page1 = evt.pageIndex;
        this.viewitemsPerPage = evt.pageSize;
        this.dataViewsService.getViewDetails(this.selectedView, this.page1, this.viewitemsPerPage, ).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.viewsList = res.json().dataView.mapList;
            this.selectedViewRecordsLength = +res.headers.get('x-count');
        });
    }

    /**
     * @description relation change
     * @param evt
     */
    relationChange(evt) {
        this.relation = this.relations[evt.value].relation;
        this.page = 0;
        this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, this.relation, this.sortColumn, this.sortOrder, this.dataViewsService.searchData).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.dataViews = res.json();
            if (!res.headers) return;
            this.relations = JSON.parse(res.headers.get('relationCounts'));
            this.dataViewRecordsLength = res.headers.get('x-total-count');
        });
    }

    /**
     * @description Data Source List column Sort
     * @param evt 
     */
    changeSourceSort(evt) {
        this.sortColumn = evt.field;
        this.sortOrder = evt.order < 1 ? 'desc' : 'asc';
        this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, this.relation, this.sortColumn, this.sortOrder).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.dataViews = res.json();
            if (!res.headers) return;
            this.relations = JSON.parse(res.headers.get('relationCounts'));
            this.dataViewRecordsLength = res.headers.get('x-total-count');
        });
    }

    /** 
     * @description Data Source List Search
    */
    searchText() {
        this.dataViewsService.dataViewsWithType(this.page + 1, this.itemsPerPage, this.relation, this.sortColumn, this.sortOrder, this.dataViewsService.searchData).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.dataViews = res.json();
            if (!res.headers) return;
            this.relations = JSON.parse(res.headers.get('relationCounts'));
            this.dataViewRecordsLength = res.headers.get('x-total-count');
        });
    }

    /**
     * @description Physical View Export
     * @param type 
     */
    physicalViewExport(type) {
        this.dataViewsService.getDataViewsDataCSV(this.selectedView, type).takeUntil(this.unsubscribe).subscribe(
            (res) => { this.commonService.exportData(res, type, this.selectedViewName); },
            () => this.commonService.error('Warning!', 'Error occured while exporting View.'));
    }

    onRowSelect(evt) {
        this.router.navigate(['/data-views', {outlets: {'content': evt.data.id +'/details'}}]);
    }

}
