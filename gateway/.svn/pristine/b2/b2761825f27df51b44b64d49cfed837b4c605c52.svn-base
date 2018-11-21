import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { MdDialog } from '@angular/material';
import { BulkUploadDialog } from '../mapping-set/mapping-set-dialog.component';
import { FxRates } from './fx-rates.model';
import { FxRatesService } from './fx-rates.service';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-fx-rates',
    templateUrl: './fx-rates.component.html'
})
export class FxRatesComponent implements OnInit, OnDestroy {
    
    fxRates: FxRates[];
    private unsubscribe: Subject<void> = new Subject();
    itemsPerPage: any = 25;
    fxRatesRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    bulkUploadReason: string;
    bulkUploadStatus: string;

    constructor(
        private fxRatesService: FxRatesService,
        public dialog: MdDialog,
        private router: Router
    ) {}


    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    loadAll(){
        this.fxRatesService.getAllFxRates(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.fxRates = res.json();
            this.fxRatesRecordsLength= +res.headers.get('x-total-count');
        });
    }

    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.fxRatesService.getAllFxRates(this.page+1, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.fxRates = res.json();
            this.fxRatesRecordsLength= +res.headers.get('x-total-count');
        });
    }

    // Bulk Upload Dialog
    openDialog(): void {
        const dialogRef = this.dialog.open(BulkUploadDialog, {
            width: '300px',
            data: { form: 'FX-RATES', status: this.bulkUploadStatus, reason: this.bulkUploadReason },
            disableClose: true
        });

        // For Applying Bulk Upload Specific Styles
        $('body').addClass('bulk-upload');

        dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
            $('body').removeClass('bulk-upload');
            if (result) {
                if (result.status == 'Success') {
                    this.loadAll();
                } else {
                    this.bulkUploadStatus = result.status;
                    this.bulkUploadReason = result.reason;
                    this.openDialog();
                }
            }
        });
    }

    onRowSelect(evt) {
        this.router.navigate(['/fx-rates', {outlets: {'content': evt.data.id +'/details'}}]);
    }
}
