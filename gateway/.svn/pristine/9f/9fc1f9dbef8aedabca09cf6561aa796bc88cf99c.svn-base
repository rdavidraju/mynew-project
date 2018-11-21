import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SourceConnectionDetails } from './source-connection-details.model';
import { SourceConnectionDetailsPopupService } from './source-connection-details-popup.service';
import { SourceConnectionDetailsService } from './source-connection-details.service';

@Component({
    selector: 'jhi-source-connection-details-dialog',
    templateUrl: './source-connection-details-dialog.component.html'
})
export class SourceConnectionDetailsDialogComponent implements OnInit {

    sourceConnectionDetails: SourceConnectionDetails;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private sourceConnectionDetailsService: SourceConnectionDetailsService,
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
        if (this.sourceConnectionDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sourceConnectionDetailsService.update(this.sourceConnectionDetails));
        } else {
            this.subscribeToSaveResponse(
                this.sourceConnectionDetailsService.create(this.sourceConnectionDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<SourceConnectionDetails>) {
        result.subscribe((res: SourceConnectionDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: SourceConnectionDetails) {
        this.eventManager.broadcast({ name: 'sourceConnectionDetailsListModification', content: 'OK'});
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
    selector: 'jhi-source-connection-details-popup',
    template: ''
})
export class SourceConnectionDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sourceConnectionDetailsPopupService: SourceConnectionDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sourceConnectionDetailsPopupService
                    .open(SourceConnectionDetailsDialogComponent as Component, params['id']);
            } else {
                this.sourceConnectionDetailsPopupService
                    .open(SourceConnectionDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        //this.routeSub.unsubscribe();
    }
}
