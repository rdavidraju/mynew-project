import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { MappingSet } from './mapping-set.model';
import { MappingSetService } from './mapping-set.service';

@Injectable()
export class MappingSetPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private mappingSetService: MappingSetService

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
                this.mappingSetService.find(id).subscribe((mappingSet) => {
                    mappingSet.createdDate = this.datePipe
                        .transform(mappingSet.createdDate, 'yyyy-MM-ddThh:mm');
                    mappingSet.lastUpdatedDate = this.datePipe
                        .transform(mappingSet.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.mappingSetModalRef(component, mappingSet);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.mappingSetModalRef(component, new MappingSet());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    mappingSetModalRef(component: Component, mappingSet: MappingSet): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.mappingSet = mappingSet;
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
