import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LineCriteria } from './line-criteria.model';
import { LineCriteriaPopupService } from './line-criteria-popup.service';
import { LineCriteriaService } from './line-criteria.service';

@Component({
    selector: 'jhi-line-criteria-dialog',
    templateUrl: './line-criteria-dialog.component.html'
})
export class LineCriteriaDialogComponent implements OnInit {

    lineCriteria: LineCriteria;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private lineCriteriaService: LineCriteriaService,
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
        if (this.lineCriteria.id !== undefined) {
            this.subscribeToSaveResponse(
                this.lineCriteriaService.update(this.lineCriteria));
        } else {
            this.subscribeToSaveResponse(
                this.lineCriteriaService.create(this.lineCriteria));
        }
    }

    private subscribeToSaveResponse(result: Observable<LineCriteria>) {
        result.subscribe((res: LineCriteria) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: LineCriteria) {
        this.eventManager.broadcast({ name: 'lineCriteriaListModification', content: 'OK'});
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
    selector: 'jhi-line-criteria-popup',
    template: ''
})
export class LineCriteriaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lineCriteriaPopupService: LineCriteriaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.lineCriteriaPopupService
                    .open(LineCriteriaDialogComponent as Component, params['id']);
            } else {
                this.lineCriteriaPopupService
                    .open(LineCriteriaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
