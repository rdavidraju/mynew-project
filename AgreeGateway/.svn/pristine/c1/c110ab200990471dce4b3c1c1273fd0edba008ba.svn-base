import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { NotificationBatch } from './notification-batch.model';
import { NotificationBatchPopupService } from './notification-batch-popup.service';
import { NotificationBatchService } from './notification-batch.service';

@Component({
    selector: 'jhi-notification-batch-dialog',
    templateUrl: './notification-batch-dialog.component.html'
})
export class NotificationBatchDialogComponent implements OnInit {

    notificationBatch: NotificationBatch;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private notificationBatchService: NotificationBatchService,
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
        if (this.notificationBatch.id !== undefined) {
            this.subscribeToSaveResponse(
                this.notificationBatchService.update(this.notificationBatch));
        } else {
            this.subscribeToSaveResponse(
                this.notificationBatchService.create(this.notificationBatch));
        }
    }

    private subscribeToSaveResponse(result: Observable<NotificationBatch>) {
        result.subscribe((res: NotificationBatch) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: NotificationBatch) {
        this.eventManager.broadcast({ name: 'notificationBatchListModification', content: 'OK'});
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
    selector: 'jhi-notification-batch-popup',
    template: ''
})
export class NotificationBatchPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notificationBatchPopupService: NotificationBatchPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.notificationBatchPopupService
                    .open(NotificationBatchDialogComponent as Component, params['id']);
            } else {
                this.notificationBatchPopupService
                    .open(NotificationBatchDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
