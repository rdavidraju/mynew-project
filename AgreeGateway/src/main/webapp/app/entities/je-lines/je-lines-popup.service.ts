import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { JeLines } from './je-lines.model';
import { JeLinesService } from './je-lines.service';

@Injectable()
export class JeLinesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private jeLinesService: JeLinesService

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
                this.jeLinesService.find(id).subscribe((jeLines) => {
                    jeLines.createdDate = this.datePipe
                        .transform(jeLines.createdDate, 'yyyy-MM-ddThh:mm');
                    jeLines.lastUpdatedDate = this.datePipe
                        .transform(jeLines.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.jeLinesModalRef(component, jeLines);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jeLinesModalRef(component, new JeLines());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jeLinesModalRef(component: Component, jeLines: JeLines): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jeLines = jeLines;
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
