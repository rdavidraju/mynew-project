import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AcctRuleConditions } from './acct-rule-conditions.model';
import { AcctRuleConditionsPopupService } from './acct-rule-conditions-popup.service';
import { AcctRuleConditionsService } from './acct-rule-conditions.service';

@Component({
    selector: 'jhi-acct-rule-conditions-dialog',
    templateUrl: './acct-rule-conditions-dialog.component.html'
})
export class AcctRuleConditionsDialogComponent implements OnInit {

    acctRuleConditions: AcctRuleConditions;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private acctRuleConditionsService: AcctRuleConditionsService,
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
        if (this.acctRuleConditions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.acctRuleConditionsService.update(this.acctRuleConditions));
        } else {
            this.subscribeToSaveResponse(
                this.acctRuleConditionsService.create(this.acctRuleConditions));
        }
    }

    private subscribeToSaveResponse(result: Observable<AcctRuleConditions>) {
        result.subscribe((res: AcctRuleConditions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AcctRuleConditions) {
        this.eventManager.broadcast({ name: 'acctRuleConditionsListModification', content: 'OK'});
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
    selector: 'jhi-acct-rule-conditions-popup',
    template: ''
})
export class AcctRuleConditionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private acctRuleConditionsPopupService: AcctRuleConditionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.acctRuleConditionsPopupService
                    .open(AcctRuleConditionsDialogComponent as Component, params['id']);
            } else {
                this.acctRuleConditionsPopupService
                    .open(AcctRuleConditionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
