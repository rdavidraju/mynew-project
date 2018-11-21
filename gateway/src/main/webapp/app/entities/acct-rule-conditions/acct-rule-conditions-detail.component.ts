import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { AcctRuleConditions } from './acct-rule-conditions.model';
import { AcctRuleConditionsService } from './acct-rule-conditions.service';

@Component({
    selector: 'jhi-acct-rule-conditions-detail',
    templateUrl: './acct-rule-conditions-detail.component.html'
})
export class AcctRuleConditionsDetailComponent implements OnInit, OnDestroy {

    acctRuleConditions: AcctRuleConditions;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private acctRuleConditionsService: AcctRuleConditionsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAcctRuleConditions();
    }

    load(id) {
        this.acctRuleConditionsService.find(id).subscribe((acctRuleConditions) => {
            this.acctRuleConditions = acctRuleConditions;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAcctRuleConditions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'acctRuleConditionsListModification',
            (response) => this.load(this.acctRuleConditions.id)
        );
    }
}
