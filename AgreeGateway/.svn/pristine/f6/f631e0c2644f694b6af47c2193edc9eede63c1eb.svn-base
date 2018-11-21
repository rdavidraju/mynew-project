import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Processes } from './processes.model';
import { ProcessesPopupService } from './processes-popup.service';
import { ProcessesService } from './processes.service';

@Component({
    selector: 'jhi-processes-delete-dialog',
    templateUrl: './processes-delete-dialog.component.html'
})
export class ProcessesDeleteDialogComponent {

    processes: Processes;

    constructor(
        private processesService: ProcessesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.processesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'processesListModification',
                content: 'Deleted an processes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-processes-delete-popup',
    template: ''
})
export class ProcessesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private processesPopupService: ProcessesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.processesPopupService
                .open(ProcessesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
