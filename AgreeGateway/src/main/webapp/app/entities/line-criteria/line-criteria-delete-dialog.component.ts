import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LineCriteria } from './line-criteria.model';
import { LineCriteriaPopupService } from './line-criteria-popup.service';
import { LineCriteriaService } from './line-criteria.service';

@Component({
    selector: 'jhi-line-criteria-delete-dialog',
    templateUrl: './line-criteria-delete-dialog.component.html'
})
export class LineCriteriaDeleteDialogComponent {

    lineCriteria: LineCriteria;

    constructor(
        private lineCriteriaService: LineCriteriaService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lineCriteriaService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'lineCriteriaListModification',
                content: 'Deleted an lineCriteria'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-line-criteria-delete-popup',
    template: ''
})
export class LineCriteriaDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lineCriteriaPopupService: LineCriteriaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.lineCriteriaPopupService
                .open(LineCriteriaDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
