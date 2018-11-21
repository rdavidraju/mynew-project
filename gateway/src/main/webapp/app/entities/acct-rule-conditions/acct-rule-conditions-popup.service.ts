import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AcctRuleConditions } from './acct-rule-conditions.model';
import { AcctRuleConditionsService } from './acct-rule-conditions.service';

@Injectable()
export class AcctRuleConditionsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private acctRuleConditionsService: AcctRuleConditionsService

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
                this.acctRuleConditionsService.find(id).subscribe((acctRuleConditions) => {
                    acctRuleConditions.createdDate = this.datePipe
                        .transform(acctRuleConditions.createdDate, 'yyyy-MM-ddThh:mm');
                    acctRuleConditions.lastUpdatedDate = this.datePipe
                        .transform(acctRuleConditions.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.acctRuleConditionsModalRef(component, acctRuleConditions);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.acctRuleConditionsModalRef(component, new AcctRuleConditions());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    acctRuleConditionsModalRef(component: Component, acctRuleConditions: AcctRuleConditions): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.acctRuleConditions = acctRuleConditions;
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
