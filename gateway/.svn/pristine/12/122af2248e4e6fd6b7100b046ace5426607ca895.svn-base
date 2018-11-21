import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { TemplateDetails, TemplateDetailsBreadCrumbTitles } from './template-details.model';
import { TemplateDetailsService } from './template-details.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any


@Component({
    selector: 'template-details-side-bar',
    templateUrl: './template-details-sidebar.component.html'
})
export class TemplateDetailsSideBarComponent implements OnInit {
    jobsList: TemplateDetails[] = [];
    authorities: any[];
    isSaving: boolean;
    sourcebody: any;
    journalsTemplateSideBarList: any = [];
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private templateDetailsService: TemplateDetailsService,
        private eventManager: JhiEventManager
    ) {

    }

    ngOnInit() {
        this.isSaving = false;
        this.templateDetailsService.getSideBarJournalTemplateList().subscribe((res: any) => {
            this.journalsTemplateSideBarList = res;

            //Sidebar List Default First Open
            if (this.journalsTemplateSideBarList.length == 0) {
                //Do nothing
            } else {
                this.journalsTemplateSideBarList[0].firstOpened = true;
            }

        });

        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        $(document).on('click', '.search-icon-sidebar', function() {
            if ($(".sbSearch md-input-container").hasClass("hidethis")) {
                $(".sbSearch md-input-container").removeClass("hidethis");
                $(".sbSearch md-input-container").addClass("show-this");
            }else {
                const value = $('.sbSearch md-input-container .mySearchBox').filter(function() {
                    return this.value != '';
                });
                if (value.length <= 0) { // zero-length string
                    $(".sbSearch md-input-container").removeClass("show-this");
                    $(".sbSearch md-input-container").addClass("hidethis");
                }
            }
        });
        $(".sbSearch md-input-container .mySearchBox").blur(function() {
            const value = $('.sbSearch md-input-container .mySearchBox').filter(function() {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".sbSearch md-input-container").removeClass("show-this");
                $(".sbSearch md-input-container").addClass("hidethis");
            }
        });
    }
    getInSidHeight() {
        if (this.journalsTemplateSideBarList.length == 1) {
            return 'maxHeightNone'
        } else {
            $('.scrHetless').css('max-height', 200);
            return 'scrHetless';
        }
    }
}