import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Periods } from './periods.model';
import { PeriodsService } from './periods.service';

@Injectable()
export class PeriodsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private periodsService: PeriodsService

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
                this.periodsService.find(id).subscribe((periods) => {
                    if (periods.fromDate) {
                        periods.fromDate = {
                            year: periods.fromDate.getFullYear(),
                            month: periods.fromDate.getMonth() + 1,
                            day: periods.fromDate.getDate()
                        };
                    }
                    if (periods.toDate) {
                        periods.toDate = {
                            year: periods.toDate.getFullYear(),
                            month: periods.toDate.getMonth() + 1,
                            day: periods.toDate.getDate()
                        };
                    }
                    periods.createdDate = this.datePipe
                        .transform(periods.createdDate, 'yyyy-MM-ddThh:mm');
                    periods.lastUpdatedDate = this.datePipe
                        .transform(periods.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.periodsModalRef(component, periods);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.periodsModalRef(component, new Periods());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    periodsModalRef(component: Component, periods: Periods): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.periods = periods;
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
