import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Process } from './process.model';
import { ProcessService } from './process.service';

@Injectable()
export class ProcessPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private processService: ProcessService

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
                this.processService.find(id).subscribe((process) => {
                    if (process.startDate) {
                        process.startDate = {
                            year: process.startDate.getFullYear(),
                            month: process.startDate.getMonth() + 1,
                            day: process.startDate.getDate()
                        };
                    }
                    if (process.endDate) {
                        process.endDate = {
                            year: process.endDate.getFullYear(),
                            month: process.endDate.getMonth() + 1,
                            day: process.endDate.getDate()
                        };
                    }
                    process.creationDate = this.datePipe
                        .transform(process.creationDate, 'yyyy-MM-ddThh:mm');
                    process.lastUpdatedDate = this.datePipe
                        .transform(process.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.processModalRef(component, process);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.processModalRef(component, new Process());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    processModalRef(component: Component, process: Process): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.process = process;
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
