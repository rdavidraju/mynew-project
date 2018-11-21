import { Component, OnInit, OnDestroy } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiPaginationUtil} from 'ng-jhipster';

import { Rules } from './rules.model';
import { RulesService } from './rules.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-rules',
    templateUrl: './rules.component.html'
})
export class RulesComponent implements OnInit, OnDestroy {
rules: Rules[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private rulesService: RulesService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.rulesService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: Response) => this.rules = res.json(),
                    (res: Response) => this.onError(res.json())
                );
            return;
       }
        this.rulesService.query().subscribe(
            (res: Response) => {
                this.rules = res.json();
                this.currentSearch = '';
            },
            (res: Response) => this.onError(res.json())
        );
    }

    search (query) {
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
        this.registerChangeInRules();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId (index: number, item: Rules) {
        return item.id;
    }



    registerChangeInRules() {
        this.eventSubscriber = this.eventManager.subscribe('rulesListModification', (response) => this.loadAll());
    }


    private onError (error) {
      //  this.alertService.error(error.message, null, null);
    }
}
