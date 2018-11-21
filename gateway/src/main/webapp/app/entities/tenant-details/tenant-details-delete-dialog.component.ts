import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TenantDetails } from './tenant-details.model';
import { TenantDetailsPopupService } from './tenant-details-popup.service';
import { TenantDetailsService } from './tenant-details.service';

@Component({
    selector: 'jhi-tenant-details-delete-dialog',
    templateUrl: './tenant-details-delete-dialog.component.html'
})
export class TenantDetailsDeleteDialogComponent {

    tenantDetails: TenantDetails;

    constructor(
        private tenantDetailsService: TenantDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tenantDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tenantDetailsListModification',
                content: 'Deleted an tenantDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tenant-details-delete-popup',
    template: ''
})
export class TenantDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tenantDetailsPopupService: TenantDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tenantDetailsPopupService
                .open(TenantDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
