import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { RuleConditions } from './rule-conditions.model';
import { RuleConditionsService } from './rule-conditions.service';
@Injectable()
export class RuleConditionsPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private ruleConditionsService: RuleConditionsService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.ruleConditionsService.find(id).subscribe(ruleConditions => {
                ruleConditions.creationDate = this.datePipe
                    .transform(ruleConditions.creationDate, 'yyyy-MM-ddThh:mm');
                ruleConditions.lastUpdatedDate = this.datePipe
                    .transform(ruleConditions.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                this.ruleConditionsModalRef(component, ruleConditions);
            });
        } else {
            return this.ruleConditionsModalRef(component, new RuleConditions());
        }
    }

    ruleConditionsModalRef(component: Component, ruleConditions: RuleConditions): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ruleConditions = ruleConditions;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
