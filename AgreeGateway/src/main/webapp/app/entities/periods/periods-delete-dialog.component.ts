import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Periods } from './periods.model';
import { PeriodsPopupService } from './periods-popup.service';
import { PeriodsService } from './periods.service';

@Component({
    selector: 'jhi-periods-delete-dialog',
    templateUrl: './periods-delete-dialog.component.html'
})
export class PeriodsDeleteDialogComponent {

    periods: Periods;

    constructor(
        private periodsService: PeriodsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.periodsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'periodsListModification',
                content: 'Deleted an periods'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-periods-delete-popup',
    template: ''
})
export class PeriodsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private periodsPopupService: PeriodsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.periodsPopupService
                .open(PeriodsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
