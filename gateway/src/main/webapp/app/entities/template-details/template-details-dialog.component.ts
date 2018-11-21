import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TemplateDetails } from './template-details.model';
import { TemplateDetailsPopupService } from './template-details-popup.service';
import { TemplateDetailsService } from './template-details.service';

@Component({
    selector: 'jhi-template-details-dialog',
    templateUrl: './template-details-dialog.component.html'
})
export class TemplateDetailsDialogComponent implements OnInit {

    templateDetails: TemplateDetails;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private templateDetailsService: TemplateDetailsService,
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
        if (this.templateDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.templateDetailsService.update(this.templateDetails));
        } else {
            this.subscribeToSaveResponse(
                this.templateDetailsService.create(this.templateDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<TemplateDetails>) {
        result.subscribe((res: TemplateDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TemplateDetails) {
        this.eventManager.broadcast({ name: 'templateDetailsListModification', content: 'OK'});
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
    selector: 'jhi-template-details-popup',
    template: ''
})
export class TemplateDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private templateDetailsPopupService: TemplateDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.templateDetailsPopupService
                    .open(TemplateDetailsDialogComponent as Component, params['id']);
            } else {
                this.templateDetailsPopupService
                    .open(TemplateDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
