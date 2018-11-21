import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Process } from './process.model';
import { ProcessService } from './process.service';

@Component({
    selector: 'jhi-process-detail',
    templateUrl: './process-detail.component.html'
})
export class ProcessDetailComponent implements OnInit, OnDestroy {

    process: Process;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private processService: ProcessService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProcesses();
    }

    load(id) {
        this.processService.find(id).subscribe((process) => {
            this.process = process;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProcesses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'processListModification',
            (response) => this.load(this.process.id)
        );
    }
}
