import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Calendar } from './calendar.model';
import { CalendarService } from './calendar.service';

@Injectable()
export class CalendarPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private calendarService: CalendarService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.calendarService.find(id).subscribe((calendar) => {
                    if (calendar.startDate) {
                        calendar.startDate = {
                            year: calendar.startDate.getFullYear(),
                            month: calendar.startDate.getMonth() + 1,
                            day: calendar.startDate.getDate()
                        };
                    }
                    if (calendar.endDate) {
                        calendar.endDate = {
                            year: calendar.endDate.getFullYear(),
                            month: calendar.endDate.getMonth() + 1,
                            day: calendar.endDate.getDate()
                        };
                    }
                    calendar.createdDate = this.datePipe
                        .transform(calendar.createdDate, 'yyyy-MM-ddThh:mm');
                    calendar.lastUpdatedDate = this.datePipe
                        .transform(calendar.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.calendarModalRef(component, calendar);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.calendarModalRef(component, new Calendar());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    calendarModalRef(component: Component, calendar: Calendar): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.calendar = calendar;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
