import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rules } from './rules.model';
import { RulesPopupService } from './rules-popup.service';
import { RulesService } from './rules.service';

@Component({
    selector: 'jhi-rules-dialog',
    templateUrl: './rules-dialog.component.html'
})
export class RulesDialogComponent implements OnInit {

    rules: Rules;
    authorities: any[];
    isSaving: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private rulesService: RulesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.rules.id !== undefined) {
            this.rulesService.update(this.rules)
                .subscribe((res: Rules) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.rulesService.create(this.rules)
                .subscribe((res: Rules) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Rules) {
        this.eventManager.broadcast({ name: 'rulesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
       // this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-rules-popup',
    template: ''
})
export class RulesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private rulesPopupService: RulesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.rulesPopupService
                    .open(RulesDialogComponent as Component, params['id']);
            } else {
                this.modalRef = this.rulesPopupService
                    .open(RulesDialogComponent as Component);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
