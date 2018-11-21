import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Processes } from './processes.model';
import { ProcessesService } from './processes.service';

@Injectable()
export class ProcessesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private processesService: ProcessesService

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
                this.processesService.find(id).subscribe((processes) => {
                    if (processes.startDate) {
                        processes.startDate = {
                            year: processes.startDate.getFullYear(),
                            month: processes.startDate.getMonth() + 1,
                            day: processes.startDate.getDate()
                        };
                    }
                    if (processes.endDate) {
                        processes.endDate = {
                            year: processes.endDate.getFullYear(),
                            month: processes.endDate.getMonth() + 1,
                            day: processes.endDate.getDate()
                        };
                    }
                    processes.createdDate = this.datePipe
                        .transform(processes.createdDate, 'yyyy-MM-ddThh:mm');
                    processes.lastUpdatedDate = this.datePipe
                        .transform(processes.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.processesModalRef(component, processes);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.processesModalRef(component, new Processes());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    processesModalRef(component: Component, processes: Processes): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.processes = processes;
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
