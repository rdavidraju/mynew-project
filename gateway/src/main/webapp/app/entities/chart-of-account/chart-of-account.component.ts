import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { ChartOfAccount } from './chart-of-account.model';
import { ChartOfAccountService } from './chart-of-account.service';

@Component({
    selector: 'jhi-chart-of-account',
    templateUrl: './chart-of-account.component.html'
})
export class ChartOfAccountComponent implements OnInit, OnDestroy {

    private unsubscribe: Subject<void> = new Subject();
    currentAccount: any;
    chartOfAccounts: ChartOfAccount[];
    error: any;
    success: any;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any = 10;
    chartOfAccountsRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private chartOfAccountService: ChartOfAccountService,
        private router: Router
    ) {}



    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    loadAll(){
        this.chartOfAccountService.getAllChartOfAccounts(this.page, this.itemsPerPage, '').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.chartOfAccounts = res.json();
            this.chartOfAccountsRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.chartOfAccountService.getAllChartOfAccounts(this.page+1, this.itemsPerPage, '').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.chartOfAccounts = res.json();
            this.chartOfAccountsRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    onRowSelect(evt) {
        this.router.navigate(['/chart-of-account', {outlets: {'content': evt.data.id +'/details'}}]);
    }
}
