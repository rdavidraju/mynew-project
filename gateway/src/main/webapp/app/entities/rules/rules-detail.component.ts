import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Rules } from './rules.model';
import { RulesService } from './rules.service';

@Component({
    selector: 'jhi-rules-detail',
    templateUrl: './rules-detail.component.html'
})
export class RulesDetailComponent implements OnInit, OnDestroy {

    rules: Rules;
    private subscription: any;

    constructor(
        private rulesService: RulesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.rulesService.find(id).subscribe(rules => {
            this.rules = rules;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
