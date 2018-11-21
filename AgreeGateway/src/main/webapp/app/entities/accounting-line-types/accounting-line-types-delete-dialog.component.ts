import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AccountingLineTypes } from './accounting-line-types.model';
import { AccountingLineTypesPopupService } from './accounting-line-types-popup.service';
import { AccountingLineTypesService } from './accounting-line-types.service';

@Component({
    selector: 'jhi-accounting-line-types-delete-dialog',
    templateUrl: './accounting-line-types-delete-dialog.component.html'
})
export class AccountingLineTypesDeleteDialogComponent {

    accountingLineTypes: AccountingLineTypes;

    constructor(
        private accountingLineTypesService: AccountingLineTypesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.accountingLineTypesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'accountingLineTypesListModification',
                content: 'Deleted an accountingLineTypes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-accounting-line-types-delete-popup',
    template: ''
})
export class AccountingLineTypesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountingLineTypesPopupService: AccountingLineTypesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.accountingLineTypesPopupService
                .open(AccountingLineTypesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
