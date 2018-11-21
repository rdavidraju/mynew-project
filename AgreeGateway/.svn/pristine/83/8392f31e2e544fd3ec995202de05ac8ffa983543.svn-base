import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { LineCriteria } from './line-criteria.model';
import { LineCriteriaService } from './line-criteria.service';

@Injectable()
export class LineCriteriaPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private lineCriteriaService: LineCriteriaService

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
                this.lineCriteriaService.find(id).subscribe((lineCriteria) => {
                    lineCriteria.createdDate = this.datePipe
                        .transform(lineCriteria.createdDate, 'yyyy-MM-ddThh:mm');
                    lineCriteria.lastUpdatedDate = this.datePipe
                        .transform(lineCriteria.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.lineCriteriaModalRef(component, lineCriteria);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.lineCriteriaModalRef(component, new LineCriteria());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    lineCriteriaModalRef(component: Component, lineCriteria: LineCriteria): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.lineCriteria = lineCriteria;
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
