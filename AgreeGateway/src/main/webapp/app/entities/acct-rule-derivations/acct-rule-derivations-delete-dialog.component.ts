import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AcctRuleDerivations } from './acct-rule-derivations.model';
import { AcctRuleDerivationsPopupService } from './acct-rule-derivations-popup.service';
import { AcctRuleDerivationsService } from './acct-rule-derivations.service';

@Component({
    selector: 'jhi-acct-rule-derivations-delete-dialog',
    templateUrl: './acct-rule-derivations-delete-dialog.component.html'
})
export class AcctRuleDerivationsDeleteDialogComponent {

    acctRuleDerivations: AcctRuleDerivations;

    constructor(
        private acctRuleDerivationsService: AcctRuleDerivationsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.acctRuleDerivationsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'acctRuleDerivationsListModification',
                content: 'Deleted an acctRuleDerivations'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-acct-rule-derivations-delete-popup',
    template: ''
})
export class AcctRuleDerivationsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private acctRuleDerivationsPopupService: AcctRuleDerivationsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.acctRuleDerivationsPopupService
                .open(AcctRuleDerivationsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
