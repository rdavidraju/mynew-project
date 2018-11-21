import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'approved',
    templateUrl: './approved.component.html'
})
export class ApprovedComponent implements OnInit {
  
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

@Component({
    selector: 'rejected',
    templateUrl: './rejected.component.html'
})
export class RejectedComponent implements OnInit {
  
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
