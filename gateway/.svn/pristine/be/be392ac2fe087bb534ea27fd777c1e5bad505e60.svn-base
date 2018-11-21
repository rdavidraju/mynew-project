import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Roles } from './roles.model';
import { RolesService } from './roles.service';
import { JhiDateUtils } from 'ng-jhipster';
@Component({
    selector: 'jhi-roles',
    templateUrl: './roles.component.html'
})
export class RolesComponent implements OnInit, OnDestroy {

currentAccount: any;
    roles: Roles[];
    itemsPerPage: any = 25;
    pageSizeOptions = [10, 25, 50, 100];
    rolesRecordsLength: number;
    page: any = 0;
    private unsubscribe: Subject<void> = new Subject();

    constructor(
        private rolesService: RolesService,
        private router: Router,
        private dateUtils: JhiDateUtils
    ) {}

    ngOnInit() {
        this.rolesService.getListRolesByTenantIdWithPagination(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            res.json().forEach((element) => {
                element.startDate = this.dateUtils.convertDateTimeFromServer(element.startDate);
                element.endDate = this.dateUtils.convertDateTimeFromServer(element.endDate);
            });
            this.roles = res.json();
            this.rolesRecordsLength= +res.headers.get('x-total-count');
        });
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    onPaginateChange(evt) {
        this.page = evt.pageIndex;
        this.itemsPerPage = evt.pageSize;
        this.rolesService.getListRolesByTenantIdWithPagination(this.page + 1, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            res.json().forEach((element) => {
                element.startDate = this.dateUtils.convertDateTimeFromServer(element.startDate);
                element.endDate = this.dateUtils.convertDateTimeFromServer(element.endDate);
            });
            this.roles = res.json();
            this.rolesRecordsLength= +res.headers.get('x-total-count');
        });
    }

    onRowSelect(evt) {
        this.router.navigate(['/roles', {outlets: {'content': evt.data.id +'/details'}}]);
    }
}
