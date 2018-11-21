import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SourceConnectionDetails } from './source-connection-details.model';
import { SourceConnectionDetailsPopupService } from './source-connection-details-popup.service';
import { SourceConnectionDetailsService } from './source-connection-details.service';

@Component({
    selector: 'jhi-source-connection-details-delete-dialog',
    templateUrl: './source-connection-details-delete-dialog.component.html'
})
export class SourceConnectionDetailsDeleteDialogComponent {

    sourceConnectionDetails: SourceConnectionDetails;

    constructor(
        private sourceConnectionDetailsService: SourceConnectionDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sourceConnectionDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sourceConnectionDetailsListModification',
                content: 'Deleted an sourceConnectionDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-source-connection-details-delete-popup',
    template: ''
})
export class SourceConnectionDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sourceConnectionDetailsPopupService: SourceConnectionDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sourceConnectionDetailsPopupService
                .open(SourceConnectionDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        //this.routeSub.unsubscribe();
    }
}
