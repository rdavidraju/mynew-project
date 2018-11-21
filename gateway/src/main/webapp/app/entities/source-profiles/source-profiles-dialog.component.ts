import { Component, DoCheck , KeyValueDiffers, OnInit, OnDestroy, OnChanges, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SourceProfiles } from './source-profiles.model';
import { SourceProfilesPopupService } from './source-profiles-popup.service';
import { SourceProfilesService } from './source-profiles.service';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Subscription';
import { Router, NavigationEnd } from '@angular/router';

@Component({
    selector: 'jhi-source-profiles-dialog',
    templateUrl: './source-profiles-dialog.component.html'
})
export class SourceProfilesDialogComponent implements OnInit {

    sourceProfiles: SourceProfiles;
     authorities: any[];
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private sourceProfilesService: SourceProfilesService,
        private eventManager: JhiEventManager,
            public config: NgbDatepickerConfig,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        this.isSaving = false;
         this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.sourceProfiles.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sourceProfilesService.update(this.sourceProfiles));
        } else {
            this.subscribeToSaveResponse(
                this.sourceProfilesService.create(this.sourceProfiles));
        }
    }

    private subscribeToSaveResponse(result: Observable<SourceProfiles>) {
        result.subscribe((res: SourceProfiles) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: SourceProfiles) {
        this.eventManager.broadcast({ name: 'sourceProfilesListModification', content: 'OK'});
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
    selector: 'jhi-source-profiles-popup',
    template: ''
})
export class SourceProfilesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sourceProfilesPopupService: SourceProfilesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.sourceProfilesPopupService
                    .open(SourceProfilesDialogComponent as Component, params['id']);
            } else {
                this.sourceProfilesPopupService
                    .open(SourceProfilesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        //this.routeSub.unsubscribe();
    }
}
