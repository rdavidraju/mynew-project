import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ApprovalGroupMembers } from './approval-group-members.model';
import { ApprovalGroupMembersService } from './approval-group-members.service';

@Component({
    selector: 'jhi-approval-group-members-detail',
    templateUrl: './approval-group-members-detail.component.html'
})
export class ApprovalGroupMembersDetailComponent implements OnInit, OnDestroy {

    approvalGroupMembers: ApprovalGroupMembers;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private approvalGroupMembersService: ApprovalGroupMembersService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApprovalGroupMembers();
    }

    load(id) {
        this.approvalGroupMembersService.find(id).subscribe((approvalGroupMembers) => {
            this.approvalGroupMembers = approvalGroupMembers;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApprovalGroupMembers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'approvalGroupMembersListModification',
            (response) => this.load(this.approvalGroupMembers.id)
        );
    }
}
