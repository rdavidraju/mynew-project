import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { TemplateDetails } from './template-details.model';
import { TemplateDetailsService } from './template-details.service';

@Injectable()
export class TemplateDetailsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private templateDetailsService: TemplateDetailsService

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
                this.templateDetailsService.find(id).subscribe((templateDetails) => {
                    if (templateDetails.startDate) {
                        templateDetails.startDate = {
                            year: templateDetails.startDate.getFullYear(),
                            month: templateDetails.startDate.getMonth() + 1,
                            day: templateDetails.startDate.getDate()
                        };
                    }
                    if (templateDetails.endDate) {
                        templateDetails.endDate = {
                            year: templateDetails.endDate.getFullYear(),
                            month: templateDetails.endDate.getMonth() + 1,
                            day: templateDetails.endDate.getDate()
                        };
                    }
                    templateDetails.createdDate = this.datePipe
                        .transform(templateDetails.createdDate, 'yyyy-MM-ddThh:mm');
                    templateDetails.lastUpdatedDate = this.datePipe
                        .transform(templateDetails.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.templateDetailsModalRef(component, templateDetails);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.templateDetailsModalRef(component, new TemplateDetails());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    templateDetailsModalRef(component: Component, templateDetails: TemplateDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.templateDetails = templateDetails;
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
