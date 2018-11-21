import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LedgerDefinition } from './ledger-definition.model';
import { LedgerDefinitionPopupService } from './ledger-definition-popup.service';
import { LedgerDefinitionService } from './ledger-definition.service';

@Component({
    selector: 'jhi-ledger-definition-delete-dialog',
    templateUrl: './ledger-definition-delete-dialog.component.html'
})
export class LedgerDefinitionDeleteDialogComponent {

    ledgerDefinition: LedgerDefinition;

    constructor(
        private ledgerDefinitionService: LedgerDefinitionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ledgerDefinitionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ledgerDefinitionListModification',
                content: 'Deleted an ledgerDefinition'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ledger-definition-delete-popup',
    template: ''
})
export class LedgerDefinitionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ledgerDefinitionPopupService: LedgerDefinitionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ledgerDefinitionPopupService
                .open(LedgerDefinitionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
