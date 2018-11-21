import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LookUpCode } from './look-up-code.model';
import { LookUpCodePopupService } from './look-up-code-popup.service';
import { LookUpCodeService } from './look-up-code.service';

@Component({
    selector: 'jhi-look-up-code-dialog',
    templateUrl: './look-up-code-dialog.component.html'
})
export class LookUpCodeDialogComponent implements OnInit {

    lookUpCode: LookUpCode;
    isSaving: boolean;
    activeStartDateDp: any;
    activeEndDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private lookUpCodeService: LookUpCodeService,
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
        if (this.lookUpCode.id !== undefined) {
            this.subscribeToSaveResponse(
                this.lookUpCodeService.update(this.lookUpCode));
        } else {
            this.subscribeToSaveResponse(
                this.lookUpCodeService.create(this.lookUpCode));
        }
    }

    private subscribeToSaveResponse(result: Observable<LookUpCode>) {
        result.subscribe((res: LookUpCode) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: LookUpCode) {
        this.eventManager.broadcast({ name: 'lookUpCodeListModification', content: 'OK'});
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
    selector: 'jhi-look-up-code-popup',
    template: ''
})
export class LookUpCodePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lookUpCodePopupService: LookUpCodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.lookUpCodePopupService
                    .open(LookUpCodeDialogComponent as Component, params['id']);
            } else {
                this.lookUpCodePopupService
                    .open(LookUpCodeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
