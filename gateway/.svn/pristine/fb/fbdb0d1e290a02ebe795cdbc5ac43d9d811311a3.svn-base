import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { FxRates } from './fx-rates.model';
import { FxRatesPopupService } from './fx-rates-popup.service';
import { FxRatesService } from './fx-rates.service';

@Component({
    selector: 'jhi-fx-rates-delete-dialog',
    templateUrl: './fx-rates-delete-dialog.component.html'
})
export class FxRatesDeleteDialogComponent {

    fxRates: FxRates;

    constructor(
        private fxRatesService: FxRatesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.fxRatesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'fxRatesListModification',
                content: 'Deleted an fxRates'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-fx-rates-delete-popup',
    template: ''
})
export class FxRatesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private fxRatesPopupService: FxRatesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.fxRatesPopupService
                .open(FxRatesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
