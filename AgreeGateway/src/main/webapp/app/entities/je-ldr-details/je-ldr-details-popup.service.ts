import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JeLdrDetails } from './je-ldr-details.model';
import { JeLdrDetailsService } from './je-ldr-details.service';

@Injectable()
export class JeLdrDetailsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private jeLdrDetailsService: JeLdrDetailsService

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
                this.jeLdrDetailsService.find(id).subscribe((jeLdrDetails) => {
                    jeLdrDetails.createdDate = this.datePipe
                        .transform(jeLdrDetails.createdDate, 'yyyy-MM-ddThh:mm');
                    jeLdrDetails.lastUpdatedDate = this.datePipe
                        .transform(jeLdrDetails.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.jeLdrDetailsModalRef(component, jeLdrDetails);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jeLdrDetailsModalRef(component, new JeLdrDetails());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jeLdrDetailsModalRef(component: Component, jeLdrDetails: JeLdrDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jeLdrDetails = jeLdrDetails;
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
