import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ApplicationPrograms } from './application-programs.model';
import { ApplicationProgramsPopupService } from './application-programs-popup.service';
import { ApplicationProgramsService } from './application-programs.service';

@Component({
    selector: 'jhi-application-programs-delete-dialog',
    templateUrl: './application-programs-delete-dialog.component.html'
})
export class ApplicationProgramsDeleteDialogComponent {

    applicationPrograms: ApplicationPrograms;

    constructor(
        private applicationProgramsService: ApplicationProgramsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.applicationProgramsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'applicationProgramsListModification',
                content: 'Deleted an applicationPrograms'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-application-programs-delete-popup',
    template: ''
})
export class ApplicationProgramsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applicationProgramsPopupService: ApplicationProgramsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.applicationProgramsPopupService
                .open(ApplicationProgramsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
