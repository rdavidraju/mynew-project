import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { SourceProfiles } from './source-profiles.model';
import { SourceProfilesService } from './source-profiles.service';

@Component({
    selector: 'jhi-source-profiles-detail',
    templateUrl: './source-profiles-detail.component.html'
})
export class SourceProfilesDetailComponent implements OnInit, OnDestroy {

    sourceProfiles: SourceProfiles;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sourceProfilesService: SourceProfilesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSourceProfiles();
    }

    load(id) {
        this.sourceProfilesService.find(id).subscribe((sourceProfiles) => {
            this.sourceProfiles = sourceProfiles;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
       // this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSourceProfiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sourceProfilesListModification',
            (response) => this.load(this.sourceProfiles.id)
        );
    }
}
