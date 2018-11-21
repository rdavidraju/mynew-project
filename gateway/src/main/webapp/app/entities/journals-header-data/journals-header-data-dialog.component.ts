import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { JournalsHeaderData } from './journals-header-data.model';
import { JournalsHeaderDataPopupService } from './journals-header-data-popup.service';
import { JournalsHeaderDataService } from './journals-header-data.service';

@Component({
    selector: 'jhi-journals-header-data-dialog',
    templateUrl: './journals-header-data-dialog.component.html'
})
export class JournalsHeaderDataDialogComponent implements OnInit {

    journalsHeaderData: JournalsHeaderData;
    isSaving: boolean;
    jeBatchDateDp: any;
    glDateDp: any;
    runDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private journalsHeaderDataService: JournalsHeaderDataService,
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
        if (this.journalsHeaderData.id !== undefined) {
            this.subscribeToSaveResponse(
                this.journalsHeaderDataService.update(this.journalsHeaderData));
        } else {
            this.subscribeToSaveResponse(
                this.journalsHeaderDataService.create(this.journalsHeaderData));
        }
    }

    private subscribeToSaveResponse(result: Observable<JournalsHeaderData>) {
        result.subscribe((res: JournalsHeaderData) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: JournalsHeaderData) {
        this.eventManager.broadcast({ name: 'journalsHeaderDataListModification', content: 'OK'});
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
    selector: 'jhi-journals-header-data-popup',
    template: ''
})
export class JournalsHeaderDataPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private journalsHeaderDataPopupService: JournalsHeaderDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.journalsHeaderDataPopupService
                    .open(JournalsHeaderDataDialogComponent as Component, params['id']);
            } else {
                this.journalsHeaderDataPopupService
                    .open(JournalsHeaderDataDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
