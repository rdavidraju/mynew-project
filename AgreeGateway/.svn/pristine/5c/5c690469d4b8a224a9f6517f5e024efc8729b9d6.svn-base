import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { DataViews } from './data-views.model';
import { DataViewsService } from './data-views.service';

@Injectable()
export class DataViewsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private dataViewsService: DataViewsService

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
                this.dataViewsService.find(id).subscribe((dataViews) => {
                    dataViews.creationDate = this.datePipe
                        .transform(dataViews.creationDate, 'yyyy-MM-ddThh:mm');
                    dataViews.lastUpdatedDate = this.datePipe
                        .transform(dataViews.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.dataViewsModalRef(component, dataViews);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.dataViewsModalRef(component, new DataViews());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    dataViewsModalRef(component: Component, dataViews: DataViews): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.dataViews = dataViews;
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
