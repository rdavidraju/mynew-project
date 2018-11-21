import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { NotificationBatch } from './notification-batch.model';
import { NotificationBatchPopupService } from './notification-batch-popup.service';
import { NotificationBatchService } from './notification-batch.service';

@Component({
    selector: 'jhi-notification-batch-delete-dialog',
    templateUrl: './notification-batch-delete-dialog.component.html'
})
export class NotificationBatchDeleteDialogComponent {

    notificationBatch: NotificationBatch;

    constructor(
        private notificationBatchService: NotificationBatchService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.notificationBatchService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'notificationBatchListModification',
                content: 'Deleted an notificationBatch'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-notification-batch-delete-popup',
    template: ''
})
export class NotificationBatchDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notificationBatchPopupService: NotificationBatchPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.notificationBatchPopupService
                .open(NotificationBatchDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
