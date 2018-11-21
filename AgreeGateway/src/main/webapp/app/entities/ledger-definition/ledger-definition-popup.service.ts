import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { LedgerDefinition } from './ledger-definition.model';
import { LedgerDefinitionService } from './ledger-definition.service';

@Injectable()
export class LedgerDefinitionPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private ledgerDefinitionService: LedgerDefinitionService

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
                this.ledgerDefinitionService.find(id).subscribe((ledgerDefinition) => {
                    if (ledgerDefinition.startDate) {
                        ledgerDefinition.startDate = {
                            year: ledgerDefinition.startDate.getFullYear(),
                            month: ledgerDefinition.startDate.getMonth() + 1,
                            day: ledgerDefinition.startDate.getDate()
                        };
                    }
                    if (ledgerDefinition.endDate) {
                        ledgerDefinition.endDate = {
                            year: ledgerDefinition.endDate.getFullYear(),
                            month: ledgerDefinition.endDate.getMonth() + 1,
                            day: ledgerDefinition.endDate.getDate()
                        };
                    }
                    ledgerDefinition.createdDate = this.datePipe
                        .transform(ledgerDefinition.createdDate, 'yyyy-MM-ddThh:mm');
                    ledgerDefinition.lastUpdatedDate = this.datePipe
                        .transform(ledgerDefinition.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.ledgerDefinitionModalRef(component, ledgerDefinition);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.ledgerDefinitionModalRef(component, new LedgerDefinition());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    ledgerDefinitionModalRef(component: Component, ledgerDefinition: LedgerDefinition): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ledgerDefinition = ledgerDefinition;
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
