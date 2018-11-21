import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AppRuleConditions } from './app-rule-conditions.model';
import { AppRuleConditionsService } from './app-rule-conditions.service';

@Injectable()
export class AppRuleConditionsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private appRuleConditionsService: AppRuleConditionsService

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
                this.appRuleConditionsService.find(id).subscribe((appRuleConditions) => {
                    appRuleConditions.createdDate = this.datePipe
                        .transform(appRuleConditions.createdDate, 'yyyy-MM-ddThh:mm');
                    appRuleConditions.lastUpdatedDate = this.datePipe
                        .transform(appRuleConditions.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.appRuleConditionsModalRef(component, appRuleConditions);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.appRuleConditionsModalRef(component, new AppRuleConditions());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    appRuleConditionsModalRef(component: Component, appRuleConditions: AppRuleConditions): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.appRuleConditions = appRuleConditions;
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
