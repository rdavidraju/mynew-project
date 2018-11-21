import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Roles } from './roles.model';
import { RolesPopupService } from './roles-popup.service';
import { RolesService } from './roles.service';

@Component({
    selector: 'jhi-roles-dialog',
    templateUrl: './roles-dialog.component.html'
})
export class RolesDialogComponent implements OnInit {

    roles: Roles;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private rolesService: RolesService,
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
        if (this.roles.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rolesService.update(this.roles));
        } else {
            this.subscribeToSaveResponse(
                this.rolesService.create(this.roles));
        }
    }

    private subscribeToSaveResponse(result: Observable<Roles>) {
        result.subscribe((res: Roles) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Roles) {
        this.eventManager.broadcast({ name: 'rolesListModification', content: 'OK'});
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
    selector: 'jhi-roles-popup',
    template: ''
})
export class RolesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolesPopupService: RolesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rolesPopupService
                    .open(RolesDialogComponent as Component, params['id']);
            } else {
                this.rolesPopupService
                    .open(RolesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
