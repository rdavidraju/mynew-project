import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ApplicationPrograms } from './application-programs.model';
import { ApplicationProgramsService } from './application-programs.service';

@Component({
    selector: 'jhi-application-programs-detail',
    templateUrl: './application-programs-detail.component.html'
})
export class ApplicationProgramsDetailComponent implements OnInit, OnDestroy {

    applicationPrograms: ApplicationPrograms;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private applicationProgramsService: ApplicationProgramsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApplicationPrograms();
    }

    load(id) {
        this.applicationProgramsService.find(id).subscribe((applicationPrograms) => {
            this.applicationPrograms = applicationPrograms;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApplicationPrograms() {
        this.eventSubscriber = this.eventManager.subscribe(
            'applicationProgramsListModification',
            (response) => this.load(this.applicationPrograms.id)
        );
    }
}
