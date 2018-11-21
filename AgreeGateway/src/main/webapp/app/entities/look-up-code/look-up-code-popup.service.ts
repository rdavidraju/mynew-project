import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { LookUpCode } from './look-up-code.model';
import { LookUpCodeService } from './look-up-code.service';

@Injectable()
export class LookUpCodePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private lookUpCodeService: LookUpCodeService

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
                this.lookUpCodeService.find(id).subscribe((lookUpCode) => {
                    if (lookUpCode.activeStartDate) {
                        lookUpCode.activeStartDate = {
                            year: lookUpCode.activeStartDate.getFullYear(),
                            month: lookUpCode.activeStartDate.getMonth() + 1,
                            day: lookUpCode.activeStartDate.getDate()
                        };
                    }
                    if (lookUpCode.activeEndDate) {
                        lookUpCode.activeEndDate = {
                            year: lookUpCode.activeEndDate.getFullYear(),
                            month: lookUpCode.activeEndDate.getMonth() + 1,
                            day: lookUpCode.activeEndDate.getDate()
                        };
                    }
                    lookUpCode.creationDate = this.datePipe
                        .transform(lookUpCode.creationDate, 'yyyy-MM-ddThh:mm');
                    lookUpCode.lastUpdatedDate = this.datePipe
                        .transform(lookUpCode.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.lookUpCodeModalRef(component, lookUpCode);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.lookUpCodeModalRef(component, new LookUpCode());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    lookUpCodeModalRef(component: Component, lookUpCode: LookUpCode): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.lookUpCode = lookUpCode;
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
