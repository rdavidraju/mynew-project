import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Roles } from './roles.model';
import { RolesPopupService } from './roles-popup.service';
import { RolesService } from './roles.service';

@Component({
    selector: 'jhi-roles-delete-dialog',
    templateUrl: './roles-delete-dialog.component.html'
})
export class RolesDeleteDialogComponent {

    roles: Roles;

    constructor(
        private rolesService: RolesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rolesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rolesListModification',
                content: 'Deleted an roles'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-roles-delete-popup',
    template: ''
})
export class RolesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolesPopupService: RolesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rolesPopupService
                .open(RolesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
