import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rules } from './rules.model';
import { RulesPopupService } from './rules-popup.service';
import { RulesService } from './rules.service';

@Component({
    selector: 'jhi-rules-delete-dialog',
    templateUrl: './rules-delete-dialog.component.html'
})
export class RulesDeleteDialogComponent {

    rules: Rules;

    constructor(
        private rulesService: RulesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.rulesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rulesListModification',
                content: 'Deleted an rules'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rules-delete-popup',
    template: ''
})
export class RulesDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private rulesPopupService: RulesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.rulesPopupService
                .open(RulesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
