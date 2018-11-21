import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { LineCriteria } from './line-criteria.model';
import { LineCriteriaService } from './line-criteria.service';

@Component({
    selector: 'jhi-line-criteria-detail',
    templateUrl: './line-criteria-detail.component.html'
})
export class LineCriteriaDetailComponent implements OnInit, OnDestroy {

    lineCriteria: LineCriteria;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private lineCriteriaService: LineCriteriaService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLineCriteria();
    }

    load(id) {
        this.lineCriteriaService.find(id).subscribe((lineCriteria) => {
            this.lineCriteria = lineCriteria;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLineCriteria() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lineCriteriaListModification',
            (response) => this.load(this.lineCriteria.id)
        );
    }
}
