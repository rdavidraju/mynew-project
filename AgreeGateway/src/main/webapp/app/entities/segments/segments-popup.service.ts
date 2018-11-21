import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Segments } from './segments.model';
import { SegmentsService } from './segments.service';

@Injectable()
export class SegmentsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private segmentsService: SegmentsService

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
                this.segmentsService.find(id).subscribe((segments) => {
                    segments.createdDate = this.datePipe
                        .transform(segments.createdDate, 'yyyy-MM-ddThh:mm');
                    segments.lastUpdatedDate = this.datePipe
                        .transform(segments.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.segmentsModalRef(component, segments);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.segmentsModalRef(component, new Segments());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    segmentsModalRef(component: Component, segments: Segments): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.segments = segments;
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
