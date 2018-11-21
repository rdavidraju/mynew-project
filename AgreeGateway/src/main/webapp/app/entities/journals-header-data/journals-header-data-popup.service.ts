import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JournalsHeaderData } from './journals-header-data.model';
import { JournalsHeaderDataService } from './journals-header-data.service';

@Injectable()
export class JournalsHeaderDataPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private journalsHeaderDataService: JournalsHeaderDataService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.journalsHeaderDataService.find(id).subscribe((journalsHeaderData) => {
                    if (journalsHeaderData.jeBatchDate) {
                        journalsHeaderData.jeBatchDate = {
                            year: journalsHeaderData.jeBatchDate.getFullYear(),
                            month: journalsHeaderData.jeBatchDate.getMonth() + 1,
                            day: journalsHeaderData.jeBatchDate.getDate()
                        };
                    }
                    if (journalsHeaderData.glDate) {
                        journalsHeaderData.glDate = {
                            year: journalsHeaderData.glDate.getFullYear(),
                            month: journalsHeaderData.glDate.getMonth() + 1,
                            day: journalsHeaderData.glDate.getDate()
                        };
                    }
                    if (journalsHeaderData.runDate) {
                        journalsHeaderData.runDate = {
                            year: journalsHeaderData.runDate.getFullYear(),
                            month: journalsHeaderData.runDate.getMonth() + 1,
                            day: journalsHeaderData.runDate.getDate()
                        };
                    }
                    journalsHeaderData.createdDate = this.datePipe
                        .transform(journalsHeaderData.createdDate, 'yyyy-MM-ddThh:mm');
                    journalsHeaderData.lastUpdatedDate = this.datePipe
                        .transform(journalsHeaderData.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.journalsHeaderDataModalRef(component, journalsHeaderData);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.journalsHeaderDataModalRef(component, new JournalsHeaderData());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    journalsHeaderDataModalRef(component: Component, journalsHeaderData: JournalsHeaderData): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.journalsHeaderData = journalsHeaderData;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
