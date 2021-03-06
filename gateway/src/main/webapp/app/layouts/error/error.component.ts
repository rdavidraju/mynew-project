import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-error',
    templateUrl: './error.component.html',
    styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {
    errorMessage: string;
    error403: boolean;
    pageNotExist = false;

    constructor(
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.route.data.subscribe((routeData) => {
            if (routeData.error403) {
                this.error403 = routeData.error403;
            }
            if (routeData.errorMessage) {
                this.errorMessage = routeData.errorMessage;
            }
            if (routeData.ispathNotFound) {
                this.pageNotExist = true;
            }
        });
    }
}
