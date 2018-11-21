import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AppRuleConditions } from './app-rule-conditions.model';
import { AppRuleConditionsPopupService } from './app-rule-conditions-popup.service';
import { AppRuleConditionsService } from './app-rule-conditions.service';

@Component({
    selector: 'jhi-app-rule-conditions-dialog',
    templateUrl: './app-rule-conditions-dialog.component.html'
})
export class AppRuleConditionsDialogComponent implements OnInit {

    appRuleConditions: AppRuleConditions;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private appRuleConditionsService: AppRuleConditionsService,
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
        if (this.appRuleConditions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.appRuleConditionsService.update(this.appRuleConditions));
        } else {
            this.subscribeToSaveResponse(
                this.appRuleConditionsService.create(this.appRuleConditions));
        }
    }

    private subscribeToSaveResponse(result: Observable<AppRuleConditions>) {
        result.subscribe((res: AppRuleConditions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AppRuleConditions) {
        this.eventManager.broadcast({ name: 'appRuleConditionsListModification', content: 'OK'});
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
    selector: 'jhi-app-rule-conditions-popup',
    template: ''
})
export class AppRuleConditionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appRuleConditionsPopupService: AppRuleConditionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.appRuleConditionsPopupService
                    .open(AppRuleConditionsDialogComponent as Component, params['id']);
            } else {
                this.appRuleConditionsPopupService
                    .open(AppRuleConditionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
