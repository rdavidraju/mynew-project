import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AppRuleConditions } from './app-rule-conditions.model';
import { AppRuleConditionsPopupService } from './app-rule-conditions-popup.service';
import { AppRuleConditionsService } from './app-rule-conditions.service';

@Component({
    selector: 'jhi-app-rule-conditions-delete-dialog',
    templateUrl: './app-rule-conditions-delete-dialog.component.html'
})
export class AppRuleConditionsDeleteDialogComponent {

    appRuleConditions: AppRuleConditions;

    constructor(
        private appRuleConditionsService: AppRuleConditionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.appRuleConditionsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'appRuleConditionsListModification',
                content: 'Deleted an appRuleConditions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-app-rule-conditions-delete-popup',
    template: ''
})
export class AppRuleConditionsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appRuleConditionsPopupService: AppRuleConditionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.appRuleConditionsPopupService
                .open(AppRuleConditionsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
