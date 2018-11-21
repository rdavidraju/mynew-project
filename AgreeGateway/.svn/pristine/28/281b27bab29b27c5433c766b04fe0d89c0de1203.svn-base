import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ApprovalGroups } from './approval-groups.model';
import { ApprovalGroupsPopupService } from './approval-groups-popup.service';
import { ApprovalGroupsService } from './approval-groups.service';

@Component({
    selector: 'jhi-approval-groups-delete-dialog',
    templateUrl: './approval-groups-delete-dialog.component.html'
})
export class ApprovalGroupsDeleteDialogComponent {

    approvalGroups: ApprovalGroups;

    constructor(
        private approvalGroupsService: ApprovalGroupsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.approvalGroupsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'approvalGroupsListModification',
                content: 'Deleted an approvalGroups'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-approval-groups-delete-popup',
    template: ''
})
export class ApprovalGroupsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private approvalGroupsPopupService: ApprovalGroupsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.approvalGroupsPopupService
                .open(ApprovalGroupsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
