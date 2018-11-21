import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { ApplicationPrograms } from './application-programs.model';
import { ApplicationProgramsService } from './application-programs.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-application-programs',
    templateUrl: './application-programs.component.html'
})
export class ApplicationProgramsComponent implements OnInit, OnDestroy {
applicationPrograms: ApplicationPrograms[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private applicationProgramsService: ApplicationProgramsService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.applicationProgramsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.applicationPrograms = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.applicationProgramsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.applicationPrograms = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInApplicationPrograms();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ApplicationPrograms) {
        return item.id;
    }
    registerChangeInApplicationPrograms() {
        this.eventSubscriber = this.eventManager.subscribe('applicationProgramsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
