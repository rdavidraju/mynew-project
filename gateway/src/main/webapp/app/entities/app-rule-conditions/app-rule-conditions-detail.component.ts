import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { AppRuleConditions } from './app-rule-conditions.model';
import { AppRuleConditionsService } from './app-rule-conditions.service';

@Component({
    selector: 'jhi-app-rule-conditions-detail',
    templateUrl: './app-rule-conditions-detail.component.html'
})
export class AppRuleConditionsDetailComponent implements OnInit, OnDestroy {

    appRuleConditions: AppRuleConditions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private appRuleConditionsService: AppRuleConditionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAppRuleConditions();
    }

    load(id) {
        this.appRuleConditionsService.find(id).subscribe((appRuleConditions) => {
            this.appRuleConditions = appRuleConditions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAppRuleConditions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'appRuleConditionsListModification',
            (response) => this.load(this.appRuleConditions.id)
        );
    }
}
