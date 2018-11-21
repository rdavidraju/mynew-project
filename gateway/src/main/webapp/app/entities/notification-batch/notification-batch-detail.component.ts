import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService  } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { User, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { CommonService } from '../../entities/common.service';
import { NotificationBatch, NotificationBreadCrumbTitles } from './notification-batch.model';
import { NotificationBatchService } from './notification-batch.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-notification-batch-detail',
    templateUrl: './notification-batch-detail.component.html'
})
export class NotificationBatchDetailComponent implements OnInit, OnDestroy {

    notificationBatch: NotificationBatch;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    page: any;
    itemsPerPage: any;
    constructor(
        private eventManager: JhiEventManager,
        private notificationBatchService: NotificationBatchService,
        private route: ActivatedRoute,
        private userService: UserService,
        private parseLinks: JhiParseLinks,
        private paginationUtil: JhiPaginationUtil,
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
        this.itemsPerPage = ITEMS_PER_PAGE;
    }

    ngOnInit() {
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 161) + 'px'
        });
        // this.subscription = this.route.params.subscribe((params) => {
        //     this.load(params['id']);
        // });
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "roles") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                /* this.templateDetailsService.getJournalsById(params['id']).subscribe(
                    (journalsHeaderDataService) => { */
                        
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                        } else {
                            this.isViewOnly = true;
                            
                        }
                    /* }
                ); */
            } else {
                this.isCreateNew = true;
            }
        });
    }

    // load(id) {
    //     this.notificationBatchService.find(id).subscribe((notificationBatch) => {
    //         this.notificationBatch = notificationBatch;
    //     });
    // }
    // previousState() {
    //     window.history.back();
    // }

    ngOnDestroy() {
        // this.subscription.unsubscribe();
        // this.eventManager.destroy(this.eventSubscriber);
    }

    // registerChangeInNotificationBatches() {
    //     this.eventSubscriber = this.eventManager.subscribe(
    //         'notificationBatchListModification',
    //         (response) => this.load(this.notificationBatch.id)
    //     );
    // }
}
