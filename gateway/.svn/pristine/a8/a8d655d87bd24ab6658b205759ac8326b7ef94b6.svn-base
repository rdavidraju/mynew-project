import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs/Rx';
import { ApprovalGroups } from './approval-groups.model';
import { ApprovalGroupsService } from './approval-groups.service';

@Component({
    selector: 'jhi-approval-groups',
    templateUrl: './approval-groups.component.html'
})
export class ApprovalGroupsComponent implements OnInit, OnDestroy {

    approvalGroups: ApprovalGroups[];
    private unsubscribe: Subject<void> = new Subject();
    itemsPerPage: any = 25;
    approvalGroupsRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;

    constructor(
        private approvalGroupsService: ApprovalGroupsService
    ) {}

    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    /**Load all Approval Groups */
    loadAll(){
        this.approvalGroupsService.getAllApprovalGrps(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.approvalGroups = res.json();
            this.approvalGroupsRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    /**Pagination function */
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.approvalGroupsService.getAllApprovalGrps(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.approvalGroups = res.json();
            this.approvalGroupsRecordsLength =+ res.headers.get('x-total-count');
        });
    }
}