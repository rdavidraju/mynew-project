import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Segments } from './segments.model';
import { SegmentsPopupService } from './segments-popup.service';
import { SegmentsService } from './segments.service';

@Component({
    selector: 'jhi-segments-dialog',
    templateUrl: './segments-dialog.component.html'
})
export class SegmentsDialogComponent implements OnInit {

    segments: Segments;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private segmentsService: SegmentsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.segments.id !== undefined) {
            this.subscribeToSaveResponse(
                this.segmentsService.update(this.segments));
        } else {
            this.subscribeToSaveResponse(
                this.segmentsService.create(this.segments));
        }
    }

    private subscribeToSaveResponse(result: Observable<Segments>) {
        result.subscribe((res: Segments) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Segments) {
        this.eventManager.broadcast({ name: 'segmentsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-segments-popup',
    template: ''
})
export class SegmentsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private segmentsPopupService: SegmentsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.segmentsPopupService
                    .open(SegmentsDialogComponent as Component, params['id']);
            } else {
                this.segmentsPopupService
                    .open(SegmentsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
