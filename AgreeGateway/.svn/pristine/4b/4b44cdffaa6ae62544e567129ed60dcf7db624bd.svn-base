import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Jobs } from './jobs.model';
import { JobsService } from './jobs.service';

@Injectable()
export class JobsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private jobsService: JobsService

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
                this.jobsService.find(id).subscribe((jobs) => {
                    // jobs.creationDate = this.datePipe
                    //     .transform(jobs.creationDate, 'yyyy-MM-ddThh:mm');
                    // jobs.lastUpdatedDate = this.datePipe
                    //     .transform(jobs.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.jobsModalRef(component, jobs);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.jobsModalRef(component, new Jobs());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    jobsModalRef(component: Component, jobs: Jobs): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.jobs = jobs;
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
