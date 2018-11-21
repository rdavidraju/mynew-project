import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Calendar } from './calendar.model';
import { CalendarPopupService } from './calendar-popup.service';
import { CalendarService } from './calendar.service';

@Component({
    selector: 'jhi-calendar-delete-dialog',
    templateUrl: './calendar-delete-dialog.component.html'
})
export class CalendarDeleteDialogComponent {

    calendar: Calendar;

    constructor(
        private calendarService: CalendarService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.calendarService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'calendarListModification',
                content: 'Deleted an calendar'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-calendar-delete-popup',
    template: ''
})
export class CalendarDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private calendarPopupService: CalendarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.calendarPopupService
                .open(CalendarDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
