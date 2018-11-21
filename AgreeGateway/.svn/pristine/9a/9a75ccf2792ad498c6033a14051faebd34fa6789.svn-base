import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AccountingLineTypes } from './accounting-line-types.model';
import { AccountingLineTypesService } from './accounting-line-types.service';

@Injectable()
export class AccountingLineTypesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private accountingLineTypesService: AccountingLineTypesService

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
                this.accountingLineTypesService.find(id).subscribe((accountingLineTypes) => {
                    accountingLineTypes.creationDate = this.datePipe
                        .transform(accountingLineTypes.creationDate, 'yyyy-MM-ddThh:mm');
                    accountingLineTypes.lastUpdatedDate = this.datePipe
                        .transform(accountingLineTypes.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.accountingLineTypesModalRef(component, accountingLineTypes);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.accountingLineTypesModalRef(component, new AccountingLineTypes());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    accountingLineTypesModalRef(component: Component, accountingLineTypes: AccountingLineTypes): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.accountingLineTypes = accountingLineTypes;
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
