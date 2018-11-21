import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { MappingSet } from './mapping-set.model';
import { MappingSetService } from './mapping-set.service';
import { PageEvent } from '@angular/material';
import { NotificationsService } from 'angular2-notifications-lite';
import { MdDialog } from '@angular/material';
import { BulkUploadDialog } from './mapping-set-dialog.component';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-mapping-set',
    templateUrl: './mapping-set.component.html'
})
export class MappingSetComponent implements OnInit, OnDestroy {
    mappingSets: MappingSet[];
    mapingsetRecordsLength: number;
    itemsPerPage: any = 25;
    pageSizeOptions = [10, 25, 50, 100];
    pageEvent: PageEvent = new PageEvent();
    page: any = 0;
    bulkUploadDialog = false;
    hasBaseDropZoneOver = false;
    bulkUploadReason: string;
    bulkUploadStatus: string;
    presentUrl: any;
    purpose: any;
    private unsubscribe: Subject<void> = new Subject();

    constructor(
        private mappingSetService: MappingSetService,
        private router: Router,
        public dialog: MdDialog,
        public notificationsService: NotificationsService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.route.params.takeUntil(this.unsubscribe).subscribe(() => {
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.purpose = this.presentUrl == 'value-set-home' ? 'CHART_OF_ACCOUNT' : undefined;
            this.getAllMappingSets();
        });
    }

    getAllMappingSets(){
        this.mappingSetService.getListMappingSetsByTenantIdWithPagination(this.page, this.itemsPerPage, this.purpose).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.mappingSets = res.json();
            this.mapingsetRecordsLength = + res.headers.get('x-count');
        })
    }
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.mappingSetService.getListMappingSetsByTenantIdWithPagination(this.page, this.itemsPerPage, this.purpose).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.mappingSets = res.json();
            this.mapingsetRecordsLength= +res.headers.get('x-count');
        });
    }

    // Bulk Upload
    bulkUpload(){
        this.bulkUploadDialog = true;
    }
    
    openDialog(): void {
        const dialogRef = this.dialog.open(BulkUploadDialog, {
            width: '300px',
            data: { form: this.presentUrl == 'value-set-home' ? 'VALUE-SET' : 'MAPPING-SET', status: this.bulkUploadStatus, reason: this.bulkUploadReason },
            disableClose: true
        });

        // For Applying Bulk Upload Specific Styles
        $('body').addClass('bulk-upload');

        dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
            $('body').removeClass('bulk-upload');
            if (result) {
                if (result.status == 'Success') {
                    this.getAllMappingSets();
                } else {
                    this.bulkUploadStatus = result.status;
                    this.bulkUploadReason = result.reason;
                    this.openDialog();
                }
            }
        });
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    onRowSelect(evt) {
        if (this.purpose) {
            this.router.navigate(['/value-set', {outlets: {'content': evt.data.id +'/details'}}]);
        } else {
            this.router.navigate(['/mapping-set', {outlets: {'content': evt.data.id +'/details'}}]);
        }
    }

}