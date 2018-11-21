import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ChartOfAccount } from './chart-of-account.model';
import { ChartOfAccountPopupService } from './chart-of-account-popup.service';
import { ChartOfAccountService } from './chart-of-account.service';

@Component({
    selector: 'jhi-chart-of-account-delete-dialog',
    templateUrl: './chart-of-account-delete-dialog.component.html'
})
export class ChartOfAccountDeleteDialogComponent {

    chartOfAccount: ChartOfAccount;

    constructor(
        private chartOfAccountService: ChartOfAccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.chartOfAccountService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'chartOfAccountListModification',
                content: 'Deleted an chartOfAccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-chart-of-account-delete-popup',
    template: ''
})
export class ChartOfAccountDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chartOfAccountPopupService: ChartOfAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.chartOfAccountPopupService
                .open(ChartOfAccountDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
