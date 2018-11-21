import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Process } from './process.model';
import { ProcessPopupService } from './process-popup.service';
import { ProcessService } from './process.service';

@Component({
    selector: 'jhi-process-delete-dialog',
    templateUrl: './process-delete-dialog.component.html'
})
export class ProcessDeleteDialogComponent {

    process: Process;

    constructor(
        private processService: ProcessService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.processService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'processListModification',
                content: 'Deleted an process'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-process-delete-popup',
    template: ''
})
export class ProcessDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private processPopupService: ProcessPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.processPopupService
                .open(ProcessDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
