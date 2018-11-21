import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Jobs } from './jobs.model';
import { JobsPopupService } from './jobs-popup.service';
//import { JobsService } from './jobs.service';

@Component({
    selector: 'jhi-jobs-dialog',
    templateUrl: './jobs-dialog.component.html'
})
export class JobsDialogComponent implements OnInit {

    jobs: Jobs;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
       // private jobsService: JobsService,
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
        // this.isSaving = true;
        // if (this.jobs.id !== undefined) {
        //     this.subscribeToSaveResponse(
        //         this.jobsService.update(this.jobs));
        // } else {
        //     this.subscribeToSaveResponse(
        //         this.jobsService.create(this.jobs));
        // }
    }

    private subscribeToSaveResponse(result: Observable<Jobs>) {
        result.subscribe((res: Jobs) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Jobs) {
        this.eventManager.broadcast({ name: 'jobsListModification', content: 'OK'});
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
    selector: 'jhi-jobs-popup',
    template: ''
})
export class JobsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jobsPopupService: JobsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jobsPopupService
                    .open(JobsDialogComponent as Component, params['id']);
            } else {
                this.jobsPopupService
                    .open(JobsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
