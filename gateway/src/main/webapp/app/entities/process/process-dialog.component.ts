import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Process } from './process.model';
import { ProcessPopupService } from './process-popup.service';
import { ProcessService } from './process.service';

@Component({
    selector: 'jhi-process-dialog',
    templateUrl: './process-dialog.component.html'
})
export class ProcessDialogComponent implements OnInit {

    process: Process;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private processService: ProcessService,
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
        if (this.process.id !== undefined) {
            this.subscribeToSaveResponse(
                this.processService.update(this.process));
        } else {
            this.subscribeToSaveResponse(
                this.processService.create(this.process));
        }
    }

    private subscribeToSaveResponse(result: Observable<Process>) {
        result.subscribe((res: Process) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Process) {
        this.eventManager.broadcast({ name: 'processListModification', content: 'OK'});
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
    selector: 'jhi-process-popup',
    template: ''
})
export class ProcessPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private processPopupService: ProcessPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.processPopupService
                    .open(ProcessDialogComponent as Component, params['id']);
            } else {
                this.processPopupService
                    .open(ProcessDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
