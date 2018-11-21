import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Reconcile } from './reconcile.model';
import { ReconcilePopupService } from './reconcile-popup.service';
import { ReconcileService } from './reconcile.service';

@Component({
    selector: 'jhi-reconcile-delete-dialog',
    templateUrl: './reconcile-delete-dialog.component.html'
})
export class ReconcileDeleteDialogComponent {

    reconcile: Reconcile;

    constructor(
        private reconcileService: ReconcileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reconcileService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reconcileListModification',
                content: 'Deleted an reconcile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reconcile-delete-popup',
    template: ''
})
export class ReconcileDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reconcilePopupService: ReconcilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.reconcilePopupService
                .open(ReconcileDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
