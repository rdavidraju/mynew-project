import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JeLdrDetails } from './je-ldr-details.model';
import { JeLdrDetailsPopupService } from './je-ldr-details-popup.service';
import { JeLdrDetailsService } from './je-ldr-details.service';

@Component({
    selector: 'jhi-je-ldr-details-delete-dialog',
    templateUrl: './je-ldr-details-delete-dialog.component.html'
})
export class JeLdrDetailsDeleteDialogComponent {

    jeLdrDetails: JeLdrDetails;

    constructor(
        private jeLdrDetailsService: JeLdrDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jeLdrDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jeLdrDetailsListModification',
                content: 'Deleted an jeLdrDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-je-ldr-details-delete-popup',
    template: ''
})
export class JeLdrDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jeLdrDetailsPopupService: JeLdrDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jeLdrDetailsPopupService
                .open(JeLdrDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
