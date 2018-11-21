import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Processes } from './processes.model';
import { ProcessesPopupService } from './processes-popup.service';
import { ProcessesService } from './processes.service';

@Component({
    selector: 'jhi-processes-dialog',
    templateUrl: './processes-dialog.component.html'
})
export class ProcessesDialogComponent implements OnInit {

    processes: Processes;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private processesService: ProcessesService,
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
        if (this.processes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.processesService.update(this.processes));
        } else {
            this.subscribeToSaveResponse(
                this.processesService.create(this.processes));
        }
    }

    private subscribeToSaveResponse(result: Observable<Processes>) {
        result.subscribe((res: Processes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Processes) {
        this.eventManager.broadcast({ name: 'processesListModification', content: 'OK'});
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
    selector: 'jhi-processes-popup',
    template: ''
})
export class ProcessesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private processesPopupService: ProcessesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.processesPopupService
                    .open(ProcessesDialogComponent as Component, params['id']);
            } else {
                this.processesPopupService
                    .open(ProcessesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
