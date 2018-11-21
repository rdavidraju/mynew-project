import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ApplicationPrograms } from './application-programs.model';
import { ApplicationProgramsPopupService } from './application-programs-popup.service';
import { ApplicationProgramsService } from './application-programs.service';

@Component({
    selector: 'jhi-application-programs-dialog',
    templateUrl: './application-programs-dialog.component.html'
})
export class ApplicationProgramsDialogComponent implements OnInit {

    applicationPrograms: ApplicationPrograms;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private applicationProgramsService: ApplicationProgramsService,
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
        if (this.applicationPrograms.id !== undefined) {
            this.subscribeToSaveResponse(
                this.applicationProgramsService.update(this.applicationPrograms));
        } else {
            this.subscribeToSaveResponse(
                this.applicationProgramsService.create(this.applicationPrograms));
        }
    }

    private subscribeToSaveResponse(result: Observable<ApplicationPrograms>) {
        result.subscribe((res: ApplicationPrograms) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ApplicationPrograms) {
        this.eventManager.broadcast({ name: 'applicationProgramsListModification', content: 'OK'});
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
    selector: 'jhi-application-programs-popup',
    template: ''
})
export class ApplicationProgramsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applicationProgramsPopupService: ApplicationProgramsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.applicationProgramsPopupService
                    .open(ApplicationProgramsDialogComponent as Component, params['id']);
            } else {
                this.applicationProgramsPopupService
                    .open(ApplicationProgramsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
