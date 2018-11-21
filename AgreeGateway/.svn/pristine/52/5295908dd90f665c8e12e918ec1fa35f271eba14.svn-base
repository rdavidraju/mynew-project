import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Reconcile } from './reconcile.model';
import { ReconcilePopupService } from './reconcile-popup.service';
import { ReconcileService } from './reconcile.service';

@Component({
    selector: 'jhi-reconcile-dialog',
    templateUrl: './reconcile-dialog.component.html'
})
export class ReconcileDialogComponent implements OnInit {

    reconcile: Reconcile;
    isSaving: boolean;
    transactionDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private reconcileService: ReconcileService,
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
        if (this.reconcile.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reconcileService.update(this.reconcile));
        } else {
            this.subscribeToSaveResponse(
                this.reconcileService.create(this.reconcile));
        }
    }

    private subscribeToSaveResponse(result: Observable<Reconcile>) {
        result.subscribe((res: Reconcile) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Reconcile) {
        this.eventManager.broadcast({ name: 'reconcileListModification', content: 'OK'});
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
    selector: 'jhi-reconcile-popup',
    template: ''
})
export class ReconcilePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reconcilePopupService: ReconcilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reconcilePopupService
                    .open(ReconcileDialogComponent as Component, params['id']);
            } else {
                this.reconcilePopupService
                    .open(ReconcileDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
