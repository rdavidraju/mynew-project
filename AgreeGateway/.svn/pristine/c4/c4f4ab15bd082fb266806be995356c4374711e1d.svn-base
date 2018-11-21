import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Periods } from './periods.model';
import { PeriodsPopupService } from './periods-popup.service';
import { PeriodsService } from './periods.service';

@Component({
    selector: 'jhi-periods-dialog',
    templateUrl: './periods-dialog.component.html'
})
export class PeriodsDialogComponent implements OnInit {

    periods: Periods;
    isSaving: boolean;
    fromDateDp: any;
    toDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private periodsService: PeriodsService,
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
        if (this.periods.id !== undefined) {
            this.subscribeToSaveResponse(
                this.periodsService.update(this.periods));
        } else {
            this.subscribeToSaveResponse(
                this.periodsService.create(this.periods));
        }
    }

    private subscribeToSaveResponse(result: Observable<Periods>) {
        result.subscribe((res: Periods) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Periods) {
        this.eventManager.broadcast({ name: 'periodsListModification', content: 'OK'});
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
    selector: 'jhi-periods-popup',
    template: ''
})
export class PeriodsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private periodsPopupService: PeriodsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.periodsPopupService
                    .open(PeriodsDialogComponent as Component, params['id']);
            } else {
                this.periodsPopupService
                    .open(PeriodsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
