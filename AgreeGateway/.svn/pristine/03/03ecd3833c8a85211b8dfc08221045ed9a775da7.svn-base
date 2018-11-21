import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JournalsHeaderData } from './journals-header-data.model';
import { JournalsHeaderDataService } from './journals-header-data.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { DataViewsService } from '../data-views/data-views.service';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { CommonService } from '../common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-journals-header-data-detail',
    templateUrl: './journals-header-data-detail.component.html'
})
export class JournalsHeaderDataDetailComponent implements OnInit, OnDestroy {

    journalsHeader:any = [];
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    dataViews: any;
    dataViewList = [];
    limitSelectionSettings1 = {};
    selectedView = [];
    dataViewCols = [];
    glDateDerivationList = [];
    periodList = [];
    conversionTypeList = [];
    journalBalanceList = [];
    journalHeaders = [];
    mappingTypes = [];
    mappingValues = [];
    mappingValue = [];
    jeLineDerivations = [];
    operatorList = [];
    journalGenerationLevelList = [];
    mappingTypesa: any = [];
    journalHeaderList: any = {};
    isVisibleA: boolean = false;
    constructor(
        private eventManager: JhiEventManager,
        private dataViewsService: DataViewsService,
        private journalsHeaderDataService: JournalsHeaderDataService,
        private notificationsService: NotificationsService,
        private principal: Principal,
        private config: NgbDatepickerConfig,
        private router: Router,
        private dateUtils: JhiDateUtils,
        private route: ActivatedRoute,
        private commonService: CommonService,
        private parseLinks: JhiParseLinks,
        private paginationUtil: JhiPaginationUtil,
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private paginationConfig: PaginationConfig,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    changeMinDate() {
        this.config.minDate = this.journalsHeader.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    ngOnInit() {
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 161) + 'px'
        });
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "journals-header-data") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                this.isCreateNew = false;
                        this.isViewOnly = true;
                 this.journalsHeaderDataService.getBatchDetailsId(params['id']).subscribe(
                    (res) => {
                        this.journalsHeader = res;
                        this.journalHeaderList = this.journalsHeader.journalHeader;
                      //  console.log('this.journalHeaderList ' + JSON.stringify(this.journalHeaderList));
                        this.isCreateNew = false;
                        this.isViewOnly = true;
                    }
                ); 
                /* this.journalsHeader = {
                                        "id": 6,
                                        "tenantId": 4,
                                        "targetAppSource": "Oracle_EBS",
                                        "templateName": "Test2",
                                        "description": "Test2 descq",
                                        "viewId": 124,
                                        "viewName": "Test Conditions",
                                        "jeBatchName": "MAY17 BILL JEbatch 0025",
                                        "jePeriod": "May-17",
                                        "history": "none",
                                        "startDate": "2017-09-04",
                                        "endDate": "2018-01-21",
                                        "enable": true,
                                        "createdBy": 3,
                                        "lastUpdatedBy": 3,
                                        "createdDate": "2017-09-20T10:01:17+05:30",
                                        "lastUpdatedDate": "2017-09-20T10:01:17+05:30",
                                        "jeBatchHeaderDetails": {
                                            "batchName": "Test Batch Name",
                                            "ledgerName": "Test Ledger Name",
                                            "source": "Test Source",
                                            "category": "Test category",
                                            "currency": "USD",
                                            "glDate": "Test GL DATE",
                                            "period": "Test Period",
                                            "rate": "Test rate",
                                            "conversionType": "Test conversion type",
                                            "journalGenerationLevel": "Test journalGenerationLevel",
                                            "journalBalance": "Test journalBalance"
                                        },
                                        "jeBatchLineDetails": [
                                            {
                                                "lineNo": 1,
                                                "codeCombination": "1001.0000. 221133.0000",
                                                "description": "test desc",
                                                "debit": 15282083.26,
                                                "credit": null
                                            },
                                            {
                                                "lineNo": 2,
                                                "codeCombination": "1001.0000. 303000.0000",
                                                "description": "test desc",
                                                "debit": null,
                                                "credit": 12123134.56
                                            },
                                            {
                                                "lineNo": 3,
                                                "codeCombination": "1001.0000. 303020.0000",
                                                "description": "test desc",
                                                "debit": null,
                                                "credit": 1453.93
                                            },
                                            {
                                                "lineNo": 4,
                                                "codeCombination": "1001.0000. 303040.0000",
                                                "description": "test desc",
                                                "debit": null,
                                                "credit": 3157494.77
                                            }
                                        ]
                                    } */
            }else{
                this.isVisibleA = true;
            }
        });
       
    }

    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }
    
    ngOnDestroy() {

    }

}