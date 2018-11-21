import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JobActions } from './job-actions.model';
import { JobActionsPopupService } from './job-actions-popup.service';
import { JobActionsService } from './job-actions.service';

@Component({
    selector: 'jhi-job-actions-delete-dialog',
    templateUrl: './job-actions-delete-dialog.component.html'
})
export class JobActionsDeleteDialogComponent {

    jobActions: JobActions;

    constructor(
        private jobActionsService: JobActionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jobActionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jobActionsListModification',
                content: 'Deleted an jobActions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-job-actions-delete-popup',
    template: ''
})
export class JobActionsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobActionsPopupService: JobActionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jobActionsPopupService
                .open(JobActionsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
