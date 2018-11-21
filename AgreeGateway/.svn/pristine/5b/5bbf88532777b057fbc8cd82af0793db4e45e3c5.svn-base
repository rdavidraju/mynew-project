import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TemplateDetails } from './template-details.model';
import { TemplateDetailsPopupService } from './template-details-popup.service';
import { TemplateDetailsService } from './template-details.service';

@Component({
    selector: 'jhi-template-details-delete-dialog',
    templateUrl: './template-details-delete-dialog.component.html'
})
export class TemplateDetailsDeleteDialogComponent {

    templateDetails: TemplateDetails;

    constructor(
        private templateDetailsService: TemplateDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.templateDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'templateDetailsListModification',
                content: 'Deleted an templateDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-template-details-delete-popup',
    template: ''
})
export class TemplateDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private templateDetailsPopupService: TemplateDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.templateDetailsPopupService
                .open(TemplateDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
