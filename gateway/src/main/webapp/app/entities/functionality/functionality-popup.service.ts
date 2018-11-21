import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Functionality } from './functionality.model';
import { FunctionalityService } from './functionality.service';

@Injectable()
export class FunctionalityPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private functionalityService: FunctionalityService

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
                this.functionalityService.find(id).subscribe((functionality) => {
                    if (functionality.funcStartDate) {
                        functionality.funcStartDate = {
                            year: functionality.funcStartDate.getFullYear(),
                            month: functionality.funcStartDate.getMonth() + 1,
                            day: functionality.funcStartDate.getDate()
                        };
                    }
                    if (functionality.funcEndDate) {
                        functionality.funcEndDate = {
                            year: functionality.funcEndDate.getFullYear(),
                            month: functionality.funcEndDate.getMonth() + 1,
                            day: functionality.funcEndDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.functionalityModalRef(component, functionality);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.functionalityModalRef(component, new Functionality());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    functionalityModalRef(component: Component, functionality: Functionality): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.functionality = functionality;
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
