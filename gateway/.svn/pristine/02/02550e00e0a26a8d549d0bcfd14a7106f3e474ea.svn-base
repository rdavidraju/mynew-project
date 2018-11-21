import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Segments } from './segments.model';
import { SegmentsService } from './segments.service';

@Component({
    selector: 'jhi-segments-detail',
    templateUrl: './segments-detail.component.html'
})
export class SegmentsDetailComponent implements OnInit, OnDestroy {

    segments: Segments;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private segmentsService: SegmentsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSegments();
    }

    load(id) {
        this.segmentsService.find(id).subscribe((segments) => {
            this.segments = segments;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSegments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'segmentsListModification',
            (response) => this.load(this.segments.id)
        );
    }
}
