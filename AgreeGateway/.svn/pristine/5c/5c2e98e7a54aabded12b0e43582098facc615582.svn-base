import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ApprovalGroupMembers } from './approval-group-members.model';
import { ApprovalGroupMembersPopupService } from './approval-group-members-popup.service';
import { ApprovalGroupMembersService } from './approval-group-members.service';

@Component({
    selector: 'jhi-approval-group-members-delete-dialog',
    templateUrl: './approval-group-members-delete-dialog.component.html'
})
export class ApprovalGroupMembersDeleteDialogComponent {

    approvalGroupMembers: ApprovalGroupMembers;

    constructor(
        private approvalGroupMembersService: ApprovalGroupMembersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.approvalGroupMembersService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'approvalGroupMembersListModification',
                content: 'Deleted an approvalGroupMembers'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-approval-group-members-delete-popup',
    template: ''
})
export class ApprovalGroupMembersDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private approvalGroupMembersPopupService: ApprovalGroupMembersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.approvalGroupMembersPopupService
                .open(ApprovalGroupMembersDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
