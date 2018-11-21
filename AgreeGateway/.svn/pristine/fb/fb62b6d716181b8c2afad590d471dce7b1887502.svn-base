import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AcctRuleDerivations } from './acct-rule-derivations.model';
import { AcctRuleDerivationsService } from './acct-rule-derivations.service';

@Injectable()
export class AcctRuleDerivationsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private acctRuleDerivationsService: AcctRuleDerivationsService

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
                this.acctRuleDerivationsService.find(id).subscribe((acctRuleDerivations) => {
                    acctRuleDerivations.createdDate = this.datePipe
                        .transform(acctRuleDerivations.createdDate, 'yyyy-MM-ddThh:mm');
                    acctRuleDerivations.lastUpdatedDate = this.datePipe
                        .transform(acctRuleDerivations.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.acctRuleDerivationsModalRef(component, acctRuleDerivations);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.acctRuleDerivationsModalRef(component, new AcctRuleDerivations());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    acctRuleDerivationsModalRef(component: Component, acctRuleDerivations: AcctRuleDerivations): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.acctRuleDerivations = acctRuleDerivations;
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
