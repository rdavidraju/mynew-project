import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ApplicationPrograms } from './application-programs.model';
import { ApplicationProgramsService } from './application-programs.service';

@Injectable()
export class ApplicationProgramsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private applicationProgramsService: ApplicationProgramsService

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
                this.applicationProgramsService.find(id).subscribe((applicationPrograms) => {
                    if (applicationPrograms.startDate) {
                        applicationPrograms.startDate = {
                            year: applicationPrograms.startDate.getFullYear(),
                            month: applicationPrograms.startDate.getMonth() + 1,
                            day: applicationPrograms.startDate.getDate()
                        };
                    }
                    if (applicationPrograms.endDate) {
                        applicationPrograms.endDate = {
                            year: applicationPrograms.endDate.getFullYear(),
                            month: applicationPrograms.endDate.getMonth() + 1,
                            day: applicationPrograms.endDate.getDate()
                        };
                    }
                    applicationPrograms.creationDate = this.datePipe
                        .transform(applicationPrograms.creationDate, 'yyyy-MM-ddThh:mm');
                    applicationPrograms.lastUpdationDate = this.datePipe
                        .transform(applicationPrograms.lastUpdationDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.applicationProgramsModalRef(component, applicationPrograms);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.applicationProgramsModalRef(component, new ApplicationPrograms());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    applicationProgramsModalRef(component: Component, applicationPrograms: ApplicationPrograms): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.applicationPrograms = applicationPrograms;
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
