import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MappingSet } from './mapping-set.model';
import { MappingSetPopupService } from './mapping-set-popup.service';
import { MappingSetService } from './mapping-set.service';

@Component({
    selector: 'jhi-mapping-set-delete-dialog',
    templateUrl: './mapping-set-delete-dialog.component.html'
})
export class MappingSetDeleteDialogComponent {

    mappingSet: MappingSet;

    constructor(
        private mappingSetService: MappingSetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.mappingSetService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'mappingSetListModification',
                content: 'Deleted an mappingSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-mapping-set-delete-popup',
    template: ''
})
export class MappingSetDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mappingSetPopupService: MappingSetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.mappingSetPopupService
                .open(MappingSetDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
