import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Reconcile } from './reconcile.model';
import { ReconcileService } from './reconcile.service';

@Injectable()
export class ReconcilePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private reconcileService: ReconcileService

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
                this.reconcileService.find(id).subscribe((reconcile) => {
                    if (reconcile.transactionDate) {
                        reconcile.transactionDate = {
                            year: reconcile.transactionDate.getFullYear(),
                            month: reconcile.transactionDate.getMonth() + 1,
                            day: reconcile.transactionDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.reconcileModalRef(component, reconcile);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.reconcileModalRef(component, new Reconcile());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    reconcileModalRef(component: Component, reconcile: Reconcile): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.reconcile = reconcile;
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
