import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ApprovalGroupMembers } from './approval-group-members.model';
import { ApprovalGroupMembersPopupService } from './approval-group-members-popup.service';
import { ApprovalGroupMembersService } from './approval-group-members.service';

@Component({
    selector: 'jhi-approval-group-members-dialog',
    templateUrl: './approval-group-members-dialog.component.html'
})
export class ApprovalGroupMembersDialogComponent implements OnInit {

    approvalGroupMembers: ApprovalGroupMembers;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private approvalGroupMembersService: ApprovalGroupMembersService,
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
        if (this.approvalGroupMembers.id !== undefined) {
            this.subscribeToSaveResponse(
                this.approvalGroupMembersService.update(this.approvalGroupMembers));
        } else {
            this.subscribeToSaveResponse(
                this.approvalGroupMembersService.create(this.approvalGroupMembers));
        }
    }

    private subscribeToSaveResponse(result: Observable<ApprovalGroupMembers>) {
        result.subscribe((res: ApprovalGroupMembers) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ApprovalGroupMembers) {
        this.eventManager.broadcast({ name: 'approvalGroupMembersListModification', content: 'OK'});
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
    selector: 'jhi-approval-group-members-popup',
    template: ''
})
export class ApprovalGroupMembersPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private approvalGroupMembersPopupService: ApprovalGroupMembersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.approvalGroupMembersPopupService
                    .open(ApprovalGroupMembersDialogComponent as Component, params['id']);
            } else {
                this.approvalGroupMembersPopupService
                    .open(ApprovalGroupMembersDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
