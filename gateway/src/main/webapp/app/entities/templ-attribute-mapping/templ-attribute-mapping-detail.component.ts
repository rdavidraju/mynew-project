import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TemplAttributeMapping } from './templ-attribute-mapping.model';
import { TemplAttributeMappingService } from './templ-attribute-mapping.service';

@Component({
    selector: 'jhi-templ-attribute-mapping-detail',
    templateUrl: './templ-attribute-mapping-detail.component.html'
})
export class TemplAttributeMappingDetailComponent implements OnInit, OnDestroy {

    templAttributeMapping: TemplAttributeMapping;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private templAttributeMappingService: TemplAttributeMappingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTemplAttributeMappings();
    }

    load(id) {
        this.templAttributeMappingService.find(id).subscribe((templAttributeMapping) => {
            this.templAttributeMapping = templAttributeMapping;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTemplAttributeMappings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'templAttributeMappingListModification',
            (response) => this.load(this.templAttributeMapping.id)
        );
    }
}
