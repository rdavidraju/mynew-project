import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { LedgerDefinition } from './ledger-definition.model';
import { LedgerDefinitionService } from './ledger-definition.service';

@Component({
    selector: 'jhi-ledger-definition',
    templateUrl: './ledger-definition.component.html'
})
export class LedgerDefinitionComponent implements OnInit, OnDestroy {

ledgerDefinitions: LedgerDefinition[];
itemsPerPage: any = 10;
ledgerDefinitionRecordsLength: number;
pageSizeOptions = [10, 25, 50, 100];
page: any = 0;
private unsubscribe: Subject<void> = new Subject();

    constructor(
        private ledgerDefinitionService: LedgerDefinitionService,
        private router: Router
    ) {}


    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }


    // Load all ledgers
    loadAll(){
        this.ledgerDefinitionService.getAllLedgerDefinition(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.ledgerDefinitions = res.json();
            this.ledgerDefinitionRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    // Pagination function
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.ledgerDefinitionService.getAllLedgerDefinition(this.page+1, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.ledgerDefinitions = res.json();
            this.ledgerDefinitionRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    // Fire on row select (datatable)
    onRowSelect(evt) {
        this.router.navigate(['/ledger-definition', {outlets: {'content': evt.data.id +'/details'}}]);
    }
} 