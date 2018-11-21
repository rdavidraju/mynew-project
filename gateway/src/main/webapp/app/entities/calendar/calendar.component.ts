import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Calendar } from './calendar.model';
import { CalendarService } from './calendar.service';

@Component({
    selector: 'jhi-calendar',
    templateUrl: './calendar.component.html'
})
export class CalendarComponent implements OnInit, OnDestroy {
    calendar: Calendar[];
    itemsPerPage: any = 25;
    calendarRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    private unsubscribe: Subject<void> = new Subject();

    constructor(
        private calendarService: CalendarService,
        private router: Router,
    ) { }

    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    loadAll() {
        this.calendarService.getAllcalenderList(this.page, this.itemsPerPage, '').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.calendar = res.json();
            this.calendarRecordsLength = + res.headers.get('x-total-count');
        });
    }

    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.calendarService.getAllcalenderList(this.page + 1, this.itemsPerPage, '').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.calendar = res.json();
            this.calendarRecordsLength = + res.headers.get('x-total-count');
        });
    }

    onRowSelect(evt) {
        this.router.navigate(['/calendar', { outlets: { 'content': evt.data.id + '/details' } }]);
    }
}