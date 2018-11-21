import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Functionality } from './functionality.model';
import { FunctionalityPopupService } from './functionality-popup.service';
import { FunctionalityService } from './functionality.service';

@Component({
    selector: 'jhi-functionality-dialog',
    templateUrl: './functionality-dialog.component.html'
})
export class FunctionalityDialogComponent implements OnInit {

    functionality: Functionality;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private functionalityService: FunctionalityService,
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
        if (this.functionality.id !== undefined) {
            this.subscribeToSaveResponse(
                this.functionalityService.update(this.functionality));
        } else {
            this.subscribeToSaveResponse(
                this.functionalityService.create(this.functionality));
        }
    }

    private subscribeToSaveResponse(result: Observable<Functionality>) {
        result.subscribe((res: Functionality) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Functionality) {
        this.eventManager.broadcast({ name: 'functionalityListModification', content: 'OK'});
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
    selector: 'jhi-functionality-popup',
    template: ''
})
export class FunctionalityPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private functionalityPopupService: FunctionalityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.functionalityPopupService
                    .open(FunctionalityDialogComponent as Component, params['id']);
            } else {
                this.functionalityPopupService
                    .open(FunctionalityDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
