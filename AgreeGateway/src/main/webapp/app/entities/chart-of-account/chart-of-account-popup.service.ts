import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ChartOfAccount } from './chart-of-account.model';
import { ChartOfAccountService } from './chart-of-account.service';

@Injectable()
export class ChartOfAccountPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private chartOfAccountService: ChartOfAccountService

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
                this.chartOfAccountService.find(id).subscribe((chartOfAccount) => {
                    if (chartOfAccount.startDate) {
                        chartOfAccount.startDate = {
                            year: chartOfAccount.startDate.getFullYear(),
                            month: chartOfAccount.startDate.getMonth() + 1,
                            day: chartOfAccount.startDate.getDate()
                        };
                    }
                    if (chartOfAccount.endDate) {
                        chartOfAccount.endDate = {
                            year: chartOfAccount.endDate.getFullYear(),
                            month: chartOfAccount.endDate.getMonth() + 1,
                            day: chartOfAccount.endDate.getDate()
                        };
                    }
                    chartOfAccount.createdDate = this.datePipe
                        .transform(chartOfAccount.createdDate, 'yyyy-MM-ddThh:mm');
                    chartOfAccount.lastUpdatedDate = this.datePipe
                        .transform(chartOfAccount.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.chartOfAccountModalRef(component, chartOfAccount);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.chartOfAccountModalRef(component, new ChartOfAccount());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    chartOfAccountModalRef(component: Component, chartOfAccount: ChartOfAccount): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.chartOfAccount = chartOfAccount;
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
