import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Reconcile } from './reconcile.model';
import { ReconcileService } from './reconcile.service';

@Component({
    selector: 'jhi-reconcile-detail',
    templateUrl: './reconcile-detail.component.html'
})
export class ReconcileDetailComponent implements OnInit, OnDestroy {

    reconcile: Reconcile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reconcileService: ReconcileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReconciles();
    }

    load(id) {
        this.reconcileService.find(id).subscribe((reconcile) => {
            this.reconcile = reconcile;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReconciles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reconcileListModification',
            (response) => this.load(this.reconcile.id)
        );
    }
}
