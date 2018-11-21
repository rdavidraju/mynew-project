import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MappingSet } from './mapping-set.model';
import { MappingSetPopupService } from './mapping-set-popup.service';
import { MappingSetService } from './mapping-set.service';

@Component({
    selector: 'jhi-mapping-set-dialog',
    templateUrl: './mapping-set-dialog.component.html'
})
export class MappingSetDialogComponent implements OnInit {

    mappingSet: MappingSet;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private mappingSetService: MappingSetService,
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
        if (this.mappingSet.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mappingSetService.update(this.mappingSet));
        } else {
            this.subscribeToSaveResponse(
                this.mappingSetService.create(this.mappingSet));
        }
    }

    private subscribeToSaveResponse(result: Observable<MappingSet>) {
        result.subscribe((res: MappingSet) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: MappingSet) {
        this.eventManager.broadcast({ name: 'mappingSetListModification', content: 'OK'});
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
    selector: 'jhi-mapping-set-popup',
    template: ''
})
export class MappingSetPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mappingSetPopupService: MappingSetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mappingSetPopupService
                    .open(MappingSetDialogComponent as Component, params['id']);
            } else {
                this.mappingSetPopupService
                    .open(MappingSetDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
