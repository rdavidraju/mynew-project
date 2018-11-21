import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JeLines } from './je-lines.model';
import { JeLinesPopupService } from './je-lines-popup.service';
import { JeLinesService } from './je-lines.service';

@Component({
    selector: 'jhi-je-lines-delete-dialog',
    templateUrl: './je-lines-delete-dialog.component.html'
})
export class JeLinesDeleteDialogComponent {

    jeLines: JeLines;

    constructor(
        private jeLinesService: JeLinesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jeLinesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jeLinesListModification',
                content: 'Deleted an jeLines'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-je-lines-delete-popup',
    template: ''
})
export class JeLinesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jeLinesPopupService: JeLinesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jeLinesPopupService
                .open(JeLinesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
