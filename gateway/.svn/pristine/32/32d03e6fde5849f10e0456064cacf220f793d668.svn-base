import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { AccountingLineTypes } from './accounting-line-types.model';
import { AccountingLineTypesService } from './accounting-line-types.service';

@Component({
    selector: 'jhi-accounting-line-types-detail',
    templateUrl: './accounting-line-types-detail.component.html'
})
export class AccountingLineTypesDetailComponent implements OnInit, OnDestroy {

    accountingLineTypes: AccountingLineTypes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private accountingLineTypesService: AccountingLineTypesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAccountingLineTypes();
    }

    load(id) {
        this.accountingLineTypesService.find(id).subscribe((accountingLineTypes) => {
            this.accountingLineTypes = accountingLineTypes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAccountingLineTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'accountingLineTypesListModification',
            (response) => this.load(this.accountingLineTypes.id)
        );
    }
}
