import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Processes } from './processes.model';
import { ProcessesService } from './processes.service';

@Component({
    selector: 'jhi-processes-detail',
    templateUrl: './processes-detail.component.html'
})
export class ProcessesDetailComponent implements OnInit, OnDestroy {

    processes: Processes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private processesService: ProcessesService,
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
        this.processesService.find(id).subscribe((processes) => {
            this.processes = processes;
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
            'processesListModification',
            (response) => this.load(this.processes.id)
        );
    }
}
