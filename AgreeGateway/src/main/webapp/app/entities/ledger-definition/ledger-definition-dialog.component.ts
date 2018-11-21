import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LedgerDefinition } from './ledger-definition.model';
import { LedgerDefinitionPopupService } from './ledger-definition-popup.service';
import { LedgerDefinitionService } from './ledger-definition.service';

@Component({
    selector: 'jhi-ledger-definition-dialog',
    templateUrl: './ledger-definition-dialog.component.html'
})
export class LedgerDefinitionDialogComponent implements OnInit {

    ledgerDefinition: LedgerDefinition;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private ledgerDefinitionService: LedgerDefinitionService,
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
        if (this.ledgerDefinition.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ledgerDefinitionService.update(this.ledgerDefinition));
        } else {
            this.subscribeToSaveResponse(
                this.ledgerDefinitionService.create(this.ledgerDefinition));
        }
    }

    private subscribeToSaveResponse(result: Observable<LedgerDefinition>) {
        result.subscribe((res: LedgerDefinition) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: LedgerDefinition) {
        this.eventManager.broadcast({ name: 'ledgerDefinitionListModification', content: 'OK'});
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
    selector: 'jhi-ledger-definition-popup',
    template: ''
})
export class LedgerDefinitionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ledgerDefinitionPopupService: LedgerDefinitionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ledgerDefinitionPopupService
                    .open(LedgerDefinitionDialogComponent as Component, params['id']);
            } else {
                this.ledgerDefinitionPopupService
                    .open(LedgerDefinitionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
