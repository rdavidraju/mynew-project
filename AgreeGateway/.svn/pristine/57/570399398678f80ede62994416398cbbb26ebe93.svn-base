import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { FxRates } from './fx-rates.model';
import { FxRatesPopupService } from './fx-rates-popup.service';
import { FxRatesService } from './fx-rates.service';

@Component({
    selector: 'jhi-fx-rates-dialog',
    templateUrl: './fx-rates-dialog.component.html'
})
export class FxRatesDialogComponent implements OnInit {

    fxRates: FxRates;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private fxRatesService: FxRatesService,
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
        if (this.fxRates.id !== undefined) {
            this.subscribeToSaveResponse(
                this.fxRatesService.update(this.fxRates));
        } else {
            this.subscribeToSaveResponse(
                this.fxRatesService.create(this.fxRates));
        }
    }

    private subscribeToSaveResponse(result: Observable<FxRates>) {
        result.subscribe((res: FxRates) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: FxRates) {
        this.eventManager.broadcast({ name: 'fxRatesListModification', content: 'OK'});
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
    selector: 'jhi-fx-rates-popup',
    template: ''
})
export class FxRatesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private fxRatesPopupService: FxRatesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.fxRatesPopupService
                    .open(FxRatesDialogComponent as Component, params['id']);
            } else {
                this.fxRatesPopupService
                    .open(FxRatesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
