import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { RuleGroup } from './rule-group.model';
import { RuleGroupService } from './rule-group.service';
@Injectable()
export class RuleGroupPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private ruleGroupService: RuleGroupService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.ruleGroupService.find(id).subscribe(ruleGroup => {
                if (ruleGroup.startDate) {
                    ruleGroup.startDate = {
                        year: ruleGroup.startDate.getFullYear(),
                        month: ruleGroup.startDate.getMonth() + 1,
                        day: ruleGroup.startDate.getDate()
                    };
                }
                if (ruleGroup.endDate) {
                    ruleGroup.endDate = {
                        year: ruleGroup.endDate.getFullYear(),
                        month: ruleGroup.endDate.getMonth() + 1,
                        day: ruleGroup.endDate.getDate()
                    };
                }
                ruleGroup.creationDate = this.datePipe
                    .transform(ruleGroup.creationDate, 'yyyy-MM-ddThh:mm');
                ruleGroup.lastUpdatedDate = this.datePipe
                    .transform(ruleGroup.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                this.ruleGroupModalRef(component, ruleGroup);
            });
        } else {
            return this.ruleGroupModalRef(component, new RuleGroup());
        }
    }

    ruleGroupModalRef(component: Component, ruleGroup: RuleGroup): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ruleGroup = ruleGroup;
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
