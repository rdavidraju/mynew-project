import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ChartOfAccount } from './chart-of-account.model';
import { ChartOfAccountPopupService } from './chart-of-account-popup.service';
import { ChartOfAccountService } from './chart-of-account.service';

@Component({
    selector: 'jhi-chart-of-account-dialog',
    templateUrl: './chart-of-account-dialog.component.html'
})
export class ChartOfAccountDialogComponent implements OnInit {

    chartOfAccount: ChartOfAccount;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private chartOfAccountService: ChartOfAccountService,
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
        if (this.chartOfAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.chartOfAccountService.update(this.chartOfAccount));
        } else {
            this.subscribeToSaveResponse(
                this.chartOfAccountService.create(this.chartOfAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<ChartOfAccount>) {
        result.subscribe((res: ChartOfAccount) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ChartOfAccount) {
        this.eventManager.broadcast({ name: 'chartOfAccountListModification', content: 'OK'});
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
    selector: 'jhi-chart-of-account-popup',
    template: ''
})
export class ChartOfAccountPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chartOfAccountPopupService: ChartOfAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.chartOfAccountPopupService
                    .open(ChartOfAccountDialogComponent as Component, params['id']);
            } else {
                this.chartOfAccountPopupService
                    .open(ChartOfAccountDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
