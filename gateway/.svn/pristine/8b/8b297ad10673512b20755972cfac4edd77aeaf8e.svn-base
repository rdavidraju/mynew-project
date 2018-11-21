import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Rules } from './rules.model';
import { RulesService } from './rules.service';
@Injectable()
export class RulesPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private rulesService: RulesService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.rulesService.find(id).subscribe(rules => {
                if (rules.startDate) {
                    rules.startDate = {
                        year: rules.startDate.getFullYear(),
                        month: rules.startDate.getMonth() + 1,
                        day: rules.startDate.getDate()
                    };
                }
                if (rules.endDate) {
                    rules.endDate = {
                        year: rules.endDate.getFullYear(),
                        month: rules.endDate.getMonth() + 1,
                        day: rules.endDate.getDate()
                    };
                }
                rules.creationDate = this.datePipe
                    .transform(rules.creationDate, 'yyyy-MM-ddThh:mm');
                rules.lastUpdatedDate = this.datePipe
                    .transform(rules.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                this.rulesModalRef(component, rules);
            });
        } else {
            return this.rulesModalRef(component, new Rules());
        }
    }

    rulesModalRef(component: Component, rules: Rules): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.rules = rules;
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
