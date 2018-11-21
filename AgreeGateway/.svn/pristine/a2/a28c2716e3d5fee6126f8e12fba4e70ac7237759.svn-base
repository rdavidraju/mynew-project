import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { AcctRuleDerivations } from './acct-rule-derivations.model';
import { AcctRuleDerivationsService } from './acct-rule-derivations.service';

@Component({
    selector: 'jhi-acct-rule-derivations-detail',
    templateUrl: './acct-rule-derivations-detail.component.html'
})
export class AcctRuleDerivationsDetailComponent implements OnInit, OnDestroy {

    acctRuleDerivations: AcctRuleDerivations;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private acctRuleDerivationsService: AcctRuleDerivationsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAcctRuleDerivations();
    }

    load(id) {
        this.acctRuleDerivationsService.find(id).subscribe((acctRuleDerivations) => {
            this.acctRuleDerivations = acctRuleDerivations;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAcctRuleDerivations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'acctRuleDerivationsListModification',
            (response) => this.load(this.acctRuleDerivations.id)
        );
    }
}
