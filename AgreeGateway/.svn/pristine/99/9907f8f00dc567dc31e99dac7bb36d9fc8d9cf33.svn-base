import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { FileTemplates } from './file-templates.model';
import { FileTemplatesService } from './file-templates.service';

@Injectable()
export class FileTemplatesPopupService {
    private ngbModalRef: NgbModalRef;
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private fileTemplatesService: FileTemplatesService

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
                this.fileTemplatesService.find(id).subscribe((fileTemplates) => {
                    if (fileTemplates.startDate) {
                        fileTemplates.startDate = {
                            year: fileTemplates.startDate.getFullYear(),
                            month: fileTemplates.startDate.getMonth() + 1,
                            day: fileTemplates.startDate.getDate()
                        };
                    }
                    if (fileTemplates.endDate) {
                        fileTemplates.endDate = {
                            year: fileTemplates.endDate.getFullYear(),
                            month: fileTemplates.endDate.getMonth() + 1,
                            day: fileTemplates.endDate.getDate()
                        };
                    }
                    fileTemplates.createdDate = this.datePipe
                        .transform(fileTemplates.createdDate, 'yyyy-MM-ddThh:mm');
                    fileTemplates.lastUpdatedDate = this.datePipe
                        .transform(fileTemplates.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.fileTemplatesModalRef(component, fileTemplates);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.fileTemplatesModalRef(component, new FileTemplates());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    fileTemplatesModalRef(component: Component, fileTemplates: FileTemplates): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.fileTemplates = fileTemplates;
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
