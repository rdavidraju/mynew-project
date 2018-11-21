import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-activate-error',
    templateUrl: './activate-error.component.html'
})
export class ActivateErrorComponent implements OnInit {
  
    constructor(
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        /**Remove Navbar */
        $('body').addClass('login');
        /* Dynamic Height for Content */
        $(".content").css('min-height',window.innerHeight+'px');
        $(window).resize(function () {
            $(".content").css('min-height',window.innerHeight+'px');
        });
    }
}