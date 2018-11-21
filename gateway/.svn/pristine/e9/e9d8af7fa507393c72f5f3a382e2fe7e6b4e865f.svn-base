import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LookUpCode } from './look-up-code.model';
import { LookUpCodePopupService } from './look-up-code-popup.service';
import { LookUpCodeService } from './look-up-code.service';

@Component({
    selector: 'jhi-look-up-code-delete-dialog',
    templateUrl: './look-up-code-delete-dialog.component.html'
})
export class LookUpCodeDeleteDialogComponent {

    lookUpCode: LookUpCode;

    constructor(
        private lookUpCodeService: LookUpCodeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lookUpCodeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'lookUpCodeListModification',
                content: 'Deleted an lookUpCode'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-look-up-code-delete-popup',
    template: ''
})
export class LookUpCodeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lookUpCodePopupService: LookUpCodePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.lookUpCodePopupService
                .open(LookUpCodeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
