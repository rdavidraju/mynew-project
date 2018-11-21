import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RuleGroup } from './rule-group.model';
import { RuleGroupPopupService } from './rule-group-popup.service';
import { RuleGroupService } from './rule-group.service';

@Component({
    selector: 'jhi-rule-group-dialog',
    templateUrl: './rule-group-dialog.component.html'
})
export class RuleGroupDialogComponent implements OnInit {

    ruleGroup: RuleGroup;
    authorities: any[];
    isSaving: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private ruleGroupService: RuleGroupService,
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
        if (this.ruleGroup.id !== undefined) {
            this.ruleGroupService.update(this.ruleGroup)
                .subscribe((res: RuleGroup) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.ruleGroupService.create(this.ruleGroup)
                .subscribe((res: RuleGroup) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: RuleGroup) {
        this.eventManager.broadcast({ name: 'ruleGroupListModification', content: 'OK'});
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
    selector: 'jhi-rule-group-popup',
    template: ''
})
export class RuleGroupPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private ruleGroupPopupService: RuleGroupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.ruleGroupPopupService
                    .open(RuleGroupDialogComponent as Component, params['id']);
            } else {
                this.modalRef = this.ruleGroupPopupService
                    .open(RuleGroupDialogComponent as Component);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
