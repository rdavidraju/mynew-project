import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Calendar } from './calendar.model';
import { CalendarPopupService } from './calendar-popup.service';
import { CalendarService } from './calendar.service';

@Component({
    selector: 'jhi-calendar-dialog',
    templateUrl: './calendar-dialog.component.html'
})
export class CalendarDialogComponent implements OnInit {

    calendar: Calendar;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private calendarService: CalendarService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.calendar.id !== undefined) {
            this.subscribeToSaveResponse(
                this.calendarService.update(this.calendar));
        } else {
            this.subscribeToSaveResponse(
                this.calendarService.create(this.calendar));
        }
    }

    private subscribeToSaveResponse(result: Observable<Calendar>) {
        result.subscribe((res: Calendar) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Calendar) {
        this.eventManager.broadcast({ name: 'calendarListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-calendar-popup',
    template: ''
})
export class CalendarPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private calendarPopupService: CalendarPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.calendarPopupService
                    .open(CalendarDialogComponent as Component, params['id']);
            } else {
                this.calendarPopupService
                    .open(CalendarDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
