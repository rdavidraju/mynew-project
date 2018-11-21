import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Periods } from './periods.model';
import { PeriodsService } from './periods.service';

@Component({
    selector: 'jhi-periods-detail',
    templateUrl: './periods-detail.component.html'
})
export class PeriodsDetailComponent implements OnInit, OnDestroy {

    periods: Periods;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private periodsService: PeriodsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPeriods();
    }

    load(id) {
        this.periodsService.find(id).subscribe((periods) => {
            this.periods = periods;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPeriods() {
        this.eventSubscriber = this.eventManager.subscribe(
            'periodsListModification',
            (response) => this.load(this.periods.id)
        );
    }
}
