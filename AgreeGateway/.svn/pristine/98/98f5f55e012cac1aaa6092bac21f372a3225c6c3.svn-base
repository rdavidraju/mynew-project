import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JeLdrDetails } from './je-ldr-details.model';
import { JeLdrDetailsPopupService } from './je-ldr-details-popup.service';
import { JeLdrDetailsService } from './je-ldr-details.service';

@Component({
    selector: 'jhi-je-ldr-details-dialog',
    templateUrl: './je-ldr-details-dialog.component.html'
})
export class JeLdrDetailsDialogComponent implements OnInit {

    jeLdrDetails: JeLdrDetails;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private jeLdrDetailsService: JeLdrDetailsService,
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
        if (this.jeLdrDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jeLdrDetailsService.update(this.jeLdrDetails));
        } else {
            this.subscribeToSaveResponse(
                this.jeLdrDetailsService.create(this.jeLdrDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<JeLdrDetails>) {
        result.subscribe((res: JeLdrDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: JeLdrDetails) {
        this.eventManager.broadcast({ name: 'jeLdrDetailsListModification', content: 'OK'});
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
    selector: 'jhi-je-ldr-details-popup',
    template: ''
})
export class JeLdrDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jeLdrDetailsPopupService: JeLdrDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jeLdrDetailsPopupService
                    .open(JeLdrDetailsDialogComponent as Component, params['id']);
            } else {
                this.jeLdrDetailsPopupService
                    .open(JeLdrDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
