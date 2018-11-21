import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Segments } from './segments.model';
import { SegmentsPopupService } from './segments-popup.service';
import { SegmentsService } from './segments.service';

@Component({
    selector: 'jhi-segments-delete-dialog',
    templateUrl: './segments-delete-dialog.component.html'
})
export class SegmentsDeleteDialogComponent {

    segments: Segments;

    constructor(
        private segmentsService: SegmentsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.segmentsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'segmentsListModification',
                content: 'Deleted an segments'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-segments-delete-popup',
    template: ''
})
export class SegmentsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private segmentsPopupService: SegmentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.segmentsPopupService
                .open(SegmentsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
