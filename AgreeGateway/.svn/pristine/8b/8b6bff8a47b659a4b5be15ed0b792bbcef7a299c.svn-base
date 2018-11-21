import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Functionality } from './functionality.model';
import { FunctionalityPopupService } from './functionality-popup.service';
import { FunctionalityService } from './functionality.service';

@Component({
    selector: 'jhi-functionality-delete-dialog',
    templateUrl: './functionality-delete-dialog.component.html'
})
export class FunctionalityDeleteDialogComponent {

    functionality: Functionality;

    constructor(
        private functionalityService: FunctionalityService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.functionalityService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'functionalityListModification',
                content: 'Deleted an functionality'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-functionality-delete-popup',
    template: ''
})
export class FunctionalityDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private functionalityPopupService: FunctionalityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.functionalityPopupService
                .open(FunctionalityDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
