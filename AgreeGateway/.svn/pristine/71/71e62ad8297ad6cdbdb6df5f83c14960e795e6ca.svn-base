import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AccountingLineTypes } from './accounting-line-types.model';
import { AccountingLineTypesPopupService } from './accounting-line-types-popup.service';
import { AccountingLineTypesService } from './accounting-line-types.service';

@Component({
    selector: 'jhi-accounting-line-types-dialog',
    templateUrl: './accounting-line-types-dialog.component.html'
})
export class AccountingLineTypesDialogComponent implements OnInit {

    accountingLineTypes: AccountingLineTypes;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private accountingLineTypesService: AccountingLineTypesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.accountingLineTypes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.accountingLineTypesService.update(this.accountingLineTypes));
        } else {
            this.subscribeToSaveResponse(
                this.accountingLineTypesService.create(this.accountingLineTypes));
        }
    }

    private subscribeToSaveResponse(result: Observable<AccountingLineTypes>) {
        result.subscribe((res: AccountingLineTypes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AccountingLineTypes) {
        this.eventManager.broadcast({ name: 'accountingLineTypesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-accounting-line-types-popup',
    template: ''
})
export class AccountingLineTypesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountingLineTypesPopupService: AccountingLineTypesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.accountingLineTypesPopupService
                    .open(AccountingLineTypesDialogComponent as Component, params['id']);
            } else {
                this.accountingLineTypesPopupService
                    .open(AccountingLineTypesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
