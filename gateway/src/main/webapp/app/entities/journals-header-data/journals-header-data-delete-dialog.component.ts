import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JournalsHeaderData } from './journals-header-data.model';
import { JournalsHeaderDataPopupService } from './journals-header-data-popup.service';
import { JournalsHeaderDataService } from './journals-header-data.service';

@Component({
    selector: 'jhi-journals-header-data-delete-dialog',
    templateUrl: './journals-header-data-delete-dialog.component.html'
})
export class JournalsHeaderDataDeleteDialogComponent {

    journalsHeaderData: JournalsHeaderData;

    constructor(
        private journalsHeaderDataService: JournalsHeaderDataService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.journalsHeaderDataService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'journalsHeaderDataListModification',
                content: 'Deleted an journalsHeaderData'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-journals-header-data-delete-popup',
    template: ''
})
export class JournalsHeaderDataDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private journalsHeaderDataPopupService: JournalsHeaderDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.journalsHeaderDataPopupService
                .open(JournalsHeaderDataDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
