import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { JeLdrDetails } from './je-ldr-details.model';
import { JeLdrDetailsService } from './je-ldr-details.service';

@Component({
    selector: 'jhi-je-ldr-details-detail',
    templateUrl: './je-ldr-details-detail.component.html'
})
export class JeLdrDetailsDetailComponent implements OnInit, OnDestroy {

    jeLdrDetails: JeLdrDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jeLdrDetailsService: JeLdrDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJeLdrDetails();
    }

    load(id) {
        this.jeLdrDetailsService.find(id).subscribe((jeLdrDetails) => {
            this.jeLdrDetails = jeLdrDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJeLdrDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jeLdrDetailsListModification',
            (response) => this.load(this.jeLdrDetails.id)
        );
    }
}
