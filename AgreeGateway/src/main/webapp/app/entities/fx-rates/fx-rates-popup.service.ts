import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { FxRates } from './fx-rates.model';
import { FxRatesService } from './fx-rates.service';

@Injectable()
export class FxRatesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private fxRatesService: FxRatesService

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
                this.fxRatesService.find(id).subscribe((fxRates) => {
                    if (fxRates.startDate) {
                        fxRates.startDate = {
                            year: fxRates.startDate.getFullYear(),
                            month: fxRates.startDate.getMonth() + 1,
                            day: fxRates.startDate.getDate()
                        };
                    }
                    if (fxRates.endDate) {
                        fxRates.endDate = {
                            year: fxRates.endDate.getFullYear(),
                            month: fxRates.endDate.getMonth() + 1,
                            day: fxRates.endDate.getDate()
                        };
                    }
                    fxRates.createdDate = this.datePipe
                        .transform(fxRates.createdDate, 'yyyy-MM-ddThh:mm');
                    fxRates.lastUpdatedDate = this.datePipe
                        .transform(fxRates.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.fxRatesModalRef(component, fxRates);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.fxRatesModalRef(component, new FxRates());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    fxRatesModalRef(component: Component, fxRates: FxRates): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.fxRates = fxRates;
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
