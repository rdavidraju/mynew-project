import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { SourceConnectionDetails } from './source-connection-details.model';
import { SourceConnectionDetailsService } from './source-connection-details.service';

@Injectable()
export class SourceConnectionDetailsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private sourceConnectionDetailsService: SourceConnectionDetailsService

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
                this.sourceConnectionDetailsService.find(id).subscribe((sourceConnectionDetails) => {
                    sourceConnectionDetails.createdDate = this.datePipe
                        .transform(sourceConnectionDetails.createdDate, 'yyyy-MM-ddThh:mm');
                    sourceConnectionDetails.lastUpdatedDate = this.datePipe
                        .transform(sourceConnectionDetails.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.sourceConnectionDetailsModalRef(component, sourceConnectionDetails);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.sourceConnectionDetailsModalRef(component, new SourceConnectionDetails());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    sourceConnectionDetailsModalRef(component: Component, sourceConnectionDetails: SourceConnectionDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.sourceConnectionDetails = sourceConnectionDetails;
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
