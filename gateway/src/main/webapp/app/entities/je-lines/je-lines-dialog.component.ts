import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JeLines } from './je-lines.model';
import { JeLinesPopupService } from './je-lines-popup.service';
import { JeLinesService } from './je-lines.service';

@Component({
    selector: 'jhi-je-lines-dialog',
    templateUrl: './je-lines-dialog.component.html'
})
export class JeLinesDialogComponent implements OnInit {

    jeLines: JeLines;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private jeLinesService: JeLinesService,
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
        if (this.jeLines.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jeLinesService.update(this.jeLines));
        } else {
            this.subscribeToSaveResponse(
                this.jeLinesService.create(this.jeLines));
        }
    }

    private subscribeToSaveResponse(result: Observable<JeLines>) {
        result.subscribe((res: JeLines) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: JeLines) {
        this.eventManager.broadcast({ name: 'jeLinesListModification', content: 'OK'});
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
    selector: 'jhi-je-lines-popup',
    template: ''
})
export class JeLinesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jeLinesPopupService: JeLinesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jeLinesPopupService
                    .open(JeLinesDialogComponent as Component, params['id']);
            } else {
                this.jeLinesPopupService
                    .open(JeLinesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
