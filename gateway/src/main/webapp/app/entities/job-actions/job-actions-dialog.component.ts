import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JobActions } from './job-actions.model';
import { JobActionsPopupService } from './job-actions-popup.service';
import { JobActionsService } from './job-actions.service';

@Component({
    selector: 'jhi-job-actions-dialog',
    templateUrl: './job-actions-dialog.component.html'
})
export class JobActionsDialogComponent implements OnInit {

    jobActions: JobActions;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private jobActionsService: JobActionsService,
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
        if (this.jobActions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jobActionsService.update(this.jobActions));
        } else {
            this.subscribeToSaveResponse(
                this.jobActionsService.create(this.jobActions));
        }
    }

    private subscribeToSaveResponse(result: Observable<JobActions>) {
        result.subscribe((res: JobActions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: JobActions) {
        this.eventManager.broadcast({ name: 'jobActionsListModification', content: 'OK'});
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
    selector: 'jhi-job-actions-popup',
    template: ''
})
export class JobActionsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobActionsPopupService: JobActionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobActionsPopupService
                    .open(JobActionsDialogComponent as Component, params['id']);
            } else {
                this.jobActionsPopupService
                    .open(JobActionsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
