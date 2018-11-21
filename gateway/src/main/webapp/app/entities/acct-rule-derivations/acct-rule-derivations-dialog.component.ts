import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AcctRuleDerivations } from './acct-rule-derivations.model';
import { AcctRuleDerivationsPopupService } from './acct-rule-derivations-popup.service';
import { AcctRuleDerivationsService } from './acct-rule-derivations.service';

@Component({
    selector: 'jhi-acct-rule-derivations-dialog',
    templateUrl: './acct-rule-derivations-dialog.component.html'
})
export class AcctRuleDerivationsDialogComponent implements OnInit {

    acctRuleDerivations: AcctRuleDerivations;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private acctRuleDerivationsService: AcctRuleDerivationsService,
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
        if (this.acctRuleDerivations.id !== undefined) {
            this.subscribeToSaveResponse(
                this.acctRuleDerivationsService.update(this.acctRuleDerivations));
        } else {
            this.subscribeToSaveResponse(
                this.acctRuleDerivationsService.create(this.acctRuleDerivations));
        }
    }

    private subscribeToSaveResponse(result: Observable<AcctRuleDerivations>) {
        result.subscribe((res: AcctRuleDerivations) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AcctRuleDerivations) {
        this.eventManager.broadcast({ name: 'acctRuleDerivationsListModification', content: 'OK'});
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
    selector: 'jhi-acct-rule-derivations-popup',
    template: ''
})
export class AcctRuleDerivationsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private acctRuleDerivationsPopupService: AcctRuleDerivationsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.acctRuleDerivationsPopupService
                    .open(AcctRuleDerivationsDialogComponent as Component, params['id']);
            } else {
                this.acctRuleDerivationsPopupService
                    .open(AcctRuleDerivationsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
