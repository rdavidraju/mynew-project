import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

import { ActivateService } from './activate.service';
import { LoginModalService } from '../../shared';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-activate',
    templateUrl: './activate-default-user.component.html'
})
export class ActivateDefaultUserComponent implements OnInit {
    error: string;
    success: string;
    modalRef: NgbModalRef;

    constructor(
        private activateService: ActivateService,
        private loginModalService: LoginModalService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        /**Remove Navbar */
        $('body').addClass('login');
        /* this.route.queryParams.subscribe((params) => {
            this.activateService.get(params['key']).subscribe(() => {
                this.error = null;
                this.success = 'OK';
            }, () => {
                this.success = null;
                this.error = 'ERROR';
            });
        }); */

        /* Dynamic Height for Content */
        $(".content").css('min-height',window.innerHeight+'px');
        $(window).resize(function () {
            $(".content").css('min-height',window.innerHeight+'px');
        });
    }

    /* login() {
        this.modalRef = this.loginModalService.open();
    } */
}
