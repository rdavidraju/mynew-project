import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AcctRuleConditions } from './acct-rule-conditions.model';
import { AcctRuleConditionsPopupService } from './acct-rule-conditions-popup.service';
import { AcctRuleConditionsService } from './acct-rule-conditions.service';

@Component({
    selector: 'jhi-acct-rule-conditions-delete-dialog',
    templateUrl: './acct-rule-conditions-delete-dialog.component.html'
})
export class AcctRuleConditionsDeleteDialogComponent {

    acctRuleConditions: AcctRuleConditions;

    constructor(
        private acctRuleConditionsService: AcctRuleConditionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.acctRuleConditionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'acctRuleConditionsListModification',
                content: 'Deleted an acctRuleConditions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-acct-rule-conditions-delete-popup',
    template: ''
})
export class AcctRuleConditionsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private acctRuleConditionsPopupService: AcctRuleConditionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.acctRuleConditionsPopupService
                .open(AcctRuleConditionsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
