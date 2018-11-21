import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { JeLines } from './je-lines.model';
import { JeLinesService } from './je-lines.service';

@Component({
    selector: 'jhi-je-lines-detail',
    templateUrl: './je-lines-detail.component.html'
})
export class JeLinesDetailComponent implements OnInit, OnDestroy {

    jeLines: JeLines;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private jeLinesService: JeLinesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJeLines();
    }

    load(id) {
        this.jeLinesService.find(id).subscribe((jeLines) => {
            this.jeLines = jeLines;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJeLines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jeLinesListModification',
            (response) => this.load(this.jeLines.id)
        );
    }
}
