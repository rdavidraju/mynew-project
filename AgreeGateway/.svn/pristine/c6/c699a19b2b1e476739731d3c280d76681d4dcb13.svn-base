import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TenantDetails } from './tenant-details.model';
import { TenantDetailsPopupService } from './tenant-details-popup.service';
import { TenantDetailsService } from './tenant-details.service';

@Component({
    selector: 'jhi-tenant-details-dialog',
    templateUrl: './tenant-details-dialog.component.html'
})
export class TenantDetailsDialogComponent implements OnInit {

    tenantDetails: TenantDetails;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private tenantDetailsService: TenantDetailsService,
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
        if (this.tenantDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tenantDetailsService.update(this.tenantDetails));
        } else {
            this.subscribeToSaveResponse(
                this.tenantDetailsService.create(this.tenantDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<TenantDetails>) {
        result.subscribe((res: TenantDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TenantDetails) {
        this.eventManager.broadcast({ name: 'tenantDetailsListModification', content: 'OK'});
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
    selector: 'jhi-tenant-details-popup',
    template: ''
})
export class TenantDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tenantDetailsPopupService: TenantDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tenantDetailsPopupService
                    .open(TenantDetailsDialogComponent as Component, params['id']);
            } else {
                this.tenantDetailsPopupService
                    .open(TenantDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
