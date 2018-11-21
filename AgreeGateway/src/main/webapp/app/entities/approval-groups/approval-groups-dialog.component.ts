import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ApprovalGroups } from './approval-groups.model';
import { ApprovalGroupsPopupService } from './approval-groups-popup.service';
import { ApprovalGroupsService } from './approval-groups.service';

@Component({
    selector: 'jhi-approval-groups-dialog',
    templateUrl: './approval-groups-dialog.component.html'
})
export class ApprovalGroupsDialogComponent implements OnInit {

    approvalGroups: ApprovalGroups;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private approvalGroupsService: ApprovalGroupsService,
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
        if (this.approvalGroups.id !== undefined) {
            this.subscribeToSaveResponse(
                this.approvalGroupsService.update(this.approvalGroups));
        } else {
            this.subscribeToSaveResponse(
                this.approvalGroupsService.create(this.approvalGroups));
        }
    }

    private subscribeToSaveResponse(result: Observable<ApprovalGroups>) {
        result.subscribe((res: ApprovalGroups) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ApprovalGroups) {
        this.eventManager.broadcast({ name: 'approvalGroupsListModification', content: 'OK'});
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
    selector: 'jhi-approval-groups-popup',
    template: ''
})
export class ApprovalGroupsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private approvalGroupsPopupService: ApprovalGroupsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.approvalGroupsPopupService
                    .open(ApprovalGroupsDialogComponent as Component, params['id']);
            } else {
                this.approvalGroupsPopupService
                    .open(ApprovalGroupsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
