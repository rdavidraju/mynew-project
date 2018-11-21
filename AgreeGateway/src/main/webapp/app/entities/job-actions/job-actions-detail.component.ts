import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { JobActions } from './job-actions.model';
import { JobActionsService } from './job-actions.service';

@Component({
    selector: 'jhi-job-actions-detail',
    templateUrl: './job-actions-detail.component.html'
})
export class JobActionsDetailComponent implements OnInit, OnDestroy {

    jobActions: JobActions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jobActionsService: JobActionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJobActions();
    }

    load(id) {
        this.jobActionsService.find(id).subscribe((jobActions) => {
            this.jobActions = jobActions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJobActions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jobActionsListModification',
            (response) => this.load(this.jobActions.id)
        );
    }
}
