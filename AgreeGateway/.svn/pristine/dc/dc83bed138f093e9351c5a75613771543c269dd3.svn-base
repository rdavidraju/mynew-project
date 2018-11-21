import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { TenantDetails, TenantConfigModule } from './tenant-details.model';
import { TenantDetailsService } from './tenant-details.service';
import { Response } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { DatePipe } from '@angular/common';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;
var countriesList = require('./countriesList.json');
var statesAllList = require('./statesAllList.json');
var citiesAllList = require('./citiesAllList.json');

@Component({
    selector: 'jhi-tenant-details-detail',
    templateUrl: './tenant-details-detail.component.html'
})
export class TenantDetailsDetailComponent implements OnInit {

    tenantDetails = new TenantDetails();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA: boolean = false;
    currentAccount: any;
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    TemplatesHeight: any;
    columnEdit: boolean = false;
    //tenantConfigModule: any = [];
    tenantConfigModule = [];
    tenantConfigModuleList = [];
    tenantConfigModule1 = [];
    tempTenantId: any;
    hideButtonIcons: boolean = false;
    checkBox: boolean;
    tempContractNum: any;

    constructor(
        private eventManager: JhiEventManager,
        private tenantDetailsService: TenantDetailsService,
        private route: ActivatedRoute,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private paginationUtil: JhiPaginationUtil,
        private notificationsService: NotificationsService,
        private datePipe: DatePipe,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    changeMinDate() {
        //this.config.minDate = this.tenantConfigModule.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    countriesList: any;
    statesAllList: any;
    statesList: any;
    citiesAllList: any;
    citiesList: any;
    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 161) + 'px'
        });

        this.tenantDetailsService.getLookUpsModule('APP_MODULES').subscribe((res: any) => {
            res.forEach(element => {
                element['modules'] = element.meaning;
            });
            this.tenantConfigModule1 = res;
            //console.log('this.tenantConfigModule1 ' + JSON.stringify(this.tenantConfigModule1));
        });
        //console.log('countries ' + JSON.stringify(countriesList.countries));
        //console.log('statesAllList.states ' + JSON.stringify(statesAllList.states));
        //console.log('citiesAllList.cities ' + JSON.stringify(citiesAllList.cities));
        this.countriesList = countriesList.countries;
        this.statesAllList = statesAllList.states;
        this.citiesAllList = citiesAllList.cities;

        // this.tenantDetailsService.getCountriesList().subscribe((res: any) => {
        //     this.countriesList = res.countries;
        // });
        // this.tenantDetailsService.getStateList().subscribe((res: any) => {
        //     this.statesAllList = res.states;
        // });
        // this.tenantDetailsService.getCitiesList().subscribe((res: any) => {
        //     this.citiesAllList = res.cities;
        // });

        this.fetchTenantDetails();

    }

    getStates(id) {
        this.statesList = [];
        this.citiesList = [];
        this.statesAllList.forEach(element => {
            if (id == element.country_id) {
                this.statesList.push(element);
            }
        });
    }

    getCities(id) {
        this.citiesList = [];
        this.citiesAllList.forEach(element => {
            if (id == element.state_id) {
                this.citiesList.push(element);
            }
        });
    }

    testCiti: boolean = false;
    stateIdTemp: any;
    fetchTenantDetails() {
        this.tenantConfigModule = [];
        // this.tenantConfigModule = [];
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "ledger-definition") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }
            if (params['id']) {
                this.tenantDetailsService.find(params['id']).subscribe((res) => {
                    //console.log('params ' + params['id']);
                    this.tenantDetails = res;
                    //console.log('tenant Details :' + JSON.stringify(this.tenantDetails));
                    this.statesList = [];
                    this.citiesList = [];
                    if(this.countriesList.length>0){
                        this.countriesList.forEach(element => {
                            if (element.name == this.tenantDetails.country) {
                                if(this.statesAllList.length>0){
                                /* this.tenantDetailsService.getStateList().subscribe((res: any) => { */
                                    /* this.statesAllList = res.states; */
                                    this.statesAllList.forEach(element1 => {
                                        if (element1.country_id == element.id) {
                                            this.statesList.push(element1);
                                            this.testCiti = true;
                                        }
                                    });
                                    this.statesList.forEach(element3 => {
                                        if (element3.name == this.tenantDetails.state) {
                                            if(this.citiesAllList.length>0){
                                                /* this.tenantDetailsService.getCitiesList().subscribe((res: any) => { */
                                                    this.citiesAllList.forEach(element1 => {
                                                        if (element1.state_id == element3.id) {
                                                            this.citiesList.push(element1);
                                                        }
                                                    });
                                                /* }); */
                                            }
                                        }
                                    });
                                /* }); */
                                }
                            }
                        });
                    }
                    
                    /* this.tenantDetailsService.getCountriesList().subscribe((res: any) => {
                        this.statesList = [];
                        this.citiesList = [];
                        res.countries.forEach(element => {
                            if (element.name == this.tenantDetails.country) {
                                this.tenantDetailsService.getStateList().subscribe((res: any) => {
                                    this.statesAllList = res.states;
                                    res.states.forEach(element1 => {
                                        if (element1.country_id == element.id) {
                                            this.statesList.push(element1);
                                            this.testCiti = true;
                                        }
                                    });
                                    this.statesList.forEach(element3 => {
                                        if (element3.name == this.tenantDetails.state) {
                                            this.tenantDetailsService.getCitiesList().subscribe((res: any) => {
                                                res.cities.forEach(element1 => {
                                                    if (element1.state_id == element3.id) {
                                                        this.citiesList.push(element1);
                                                    }
                                                });
                                            });
                                        }
                                    });
                                });
                            }
                        });
                    }); */
                    this.tenantDetailsService.fetchTenantConfigModule(this.tenantDetails.id).subscribe(
                        (res: any) => {
                            this.tenantConfigModule = res;
                            this.tenantConfigModule.forEach(element => {
                                element['tempMinDate'] = new Date();
                            });
                            //console.log('tenantConfigModule Details :' + JSON.stringify(this.tenantConfigModule));
                        });
                    this.isCreateNew = false;
                    if (url.endsWith('edit')) {
                        this.isEdit = true;
                        this.hideButtonIcons = true;
                    } else {
                        this.isViewOnly = true;
                        this.hideButtonIcons = false;
                    }
                }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                this.tenantDetailsService.getLookUpsModule('APP_MODULES').subscribe((res: any) => {
                    res.forEach(element => {
                        element['modules'] = element.meaning;
                        element['tempMinDate'] = new Date();
                    });
                    this.tenantConfigModule = res;
                    //console.log('this.tenantConfigModule new ' + JSON.stringify(this.tenantConfigModule));
                });
                /* this.tenantDetailsService.fetchTenantConfigModule().subscribe(
                    (res: any) => {
                        res.forEach(element => {
                            element.startDate = null;
                            element.endDate = null;
                        });
                        this.tenantConfigModule = res;
                        console.log('tenantConfigModule Details :' + JSON.stringify(this.tenantConfigModule));
                    });   */
            }

        });

    }

    load(id) {
        this.tenantDetailsService.find(id).subscribe((tenantDetails) => {
            this.tenantDetails = tenantDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    /* ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    } */

    registerChangeInTenantDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tenantDetailsListModification',
            (response) => this.load(this.tenantDetails.id)
        );
    }

    tempTenantId1: any;
    saveTenantDetails() {
        let link: any = '';
        if (this.tenantDetails.id) {
            this.tenantDetailsService.update(this.tenantDetails).subscribe((res: any) => {
                //console.log('updated  ' + JSON.stringify(res));
                //console.log('this.tempTenantId1 ' + this.tempTenantId1);
                this.notificationsService.success(
                    'Success!',
                    '"' + res.tenantName + '" Details Successfully Updated'
                )
                link = ['/tenant-details', { outlets: { 'content': res.id + '/details' } }];
                this.router.navigate(link);
            });
        } else {
            let obj = {
                "firstName": this.tenantDetails.firstName,
                "lastName": this.tenantDetails.lastName,
                "email": this.tenantDetails.emailId,
                "langKey": "en",
                "login": this.tenantDetails.firstName,
                "password": "welcome",
                "authorities": [ 
                    {
                      "name": "ROLE_ADMIN"
                    },
                    {
                      "name": "ROLE_USER"
                    }
                ]
            }
            this.tenantDetails['conDetails'] = obj;
            //console.log('tenantDetails ' + JSON.stringify(this.tenantDetails));
            this.tenantDetailsService.createNewTenant(this.tenantDetails).subscribe((res: any) => {
                //this.tenantConfigModule = res;
                //console.log('this.tenantConfigModule ' + JSON.stringify(res));
                this.tempTenantId1 = res.tenantId;
                this.tenantConfigModule.forEach(element => {
                    if (element.checkBox == true) {
                        element.tenantId = res.tenantId;
                        element['enabledFlag'] = true;
                        element['modules'] = element.lookUpCode;
                        /* element['enabledFlag'] = element.enableFlag; */
                        element['contractNum'] = this.tempContractNum;
                        delete element.id;
                        /* delete element.tenantId;
                        delete element.tenantId; */
                        delete element.lookUpCode;
                        delete element.lookUpType;
                        delete element.description;
                        delete element.activeStartDate;
                        delete element.activeEndDate;
                        delete element.secureAttribute1;
                        delete element.secureAttribute2;
                        delete element.secureAttribute3;
                        delete element.createdBy;
                        delete element.creationDate;
                        delete element.lastUpdatedBy;
                        delete element.lastUpdatedDate;
                        delete element.checkBox;
                        this.tenantConfigModuleList.push(element);
                    }
                    //console.log('tenantConfigModuleList ' + JSON.stringify(this.tenantConfigModuleList));
                });
                this.tenantDetailsService.postTenantConfig(this.tenantConfigModuleList).subscribe((res: any) => {
                    //console.log('tenantConfigModuleList res ' + JSON.stringify(res));
                    this.notificationsService.success(
                        'Success!',
                        'Tenant Successfully Created'
                    )
                    link = ['/tenant-details', { outlets: { 'content': this.tempTenantId1 + '/details' } }];
                    this.router.navigate(link);
                });

            });
        }
    }

    /* Function to add modules */
    addModule() {
        let newLine = {
            "columnEdit": true
        };
        this.tenantConfigModule.push(newLine);
    }

    dispMin: any;
    dispMinDate: boolean = false;
    displayColumns(col, i) {
        this.dispMinDate = false;
        this.tenantConfigModule[i]['tempMinDate'] = null;
        this.tenantConfigModule[i].startDate = null;
        //console.log('col ' + JSON.stringify(col));
        //console.log('this.tenantConfigModule ' + JSON.stringify(this.tenantConfigModule));
        //console.log('i ' + i);
        let count = 0;
        this.tenantConfigModule.forEach(element => {
            if (element.purpose == col.meaning && count == 0) {
                count++;
                let date = new Date(element.endDate);
                this.tenantConfigModule[i].startDate = new Date(date.setDate(date.getDate() + 1));
                this.tenantConfigModule[i]['tempMinDate'] = this.tenantConfigModule[i].startDate;
                /* this.tenantConfigModule[i].startDate = new Date(element.endDate);
                this.tenantConfigModule[i].startDate.setDate(this.tenantConfigModule[i].startDate.getDate() + 1);*/
                //console.log('stda ' + this.tenantConfigModule[i].startDate);
                //console.log('this.tenantConfigModule ' + JSON.stringify(this.tenantConfigModule));
                this.dispMinDate = true;
            }
            if (this.dispMinDate == false) {
                this.tenantConfigModule[i]['tempMinDate'] = new Date();
            }
        });
    }

    addDateEvent(type, event, ind) {
        if (!this.isCreateNew) {
            //console.log('event ' + event.value);
            for (var i = 0; i < this.tenantConfigModule.length; i++) {
                if (i != ind && this.tenantConfigModule[i].modules == this.tenantConfigModule[ind].modules) {
                    //console.log('called ' + this.tenantConfigModule[i].endDate);
                    //this.datePipe.transform(new Date(this.tenantConfigModule[ind].startDate), 'yyyy-MM-dd');
                    //console.log('called1 ' + this.datePipe.transform(new Date(this.tenantConfigModule[ind].startDate), 'yyyy-MM-dd'));
                    if (this.datePipe.transform(new Date(this.tenantConfigModule[ind].startDate), 'yyyy-MM-dd') < this.tenantConfigModule[i].endDate) {
                        this.notificationService.error('Warning!', 'Start Date should be greater than "' + this.tenantConfigModule[i].endDate + '"');
                        this.tenantConfigModule[ind].startDate = null;
                    } else {
                        //console.log('success');
                    }
                }
            }
        }
    }

    /* Function to cancel changes */
    cancelChanges(ind) {
        //console.log('ind ' + ind);
        this.fetchTenantDetails();
        //delete this.tenantConfigModule[ind];
    }

    /* function to update tenantconfigmodule */
    updateTenantConfig(obj: any) {
        //console.log('this.tenantDetails ' + JSON.stringify(this.tenantDetails));
        //console.log('obj ' + JSON.stringify(obj));
        if (obj.id) {
            this.tenantDetailsService.updateTenantConfig(obj).subscribe((res: any) => {
                this.notificationsService.success(
                    'Success!',
                    'Successfully "' + obj.modules + '" details updated'
                )
                this.fetchTenantDetails();
            });
        } else {
            obj['tenantId'] = this.tenantDetails.id;
            obj['enabledFlag'] = true;
            this.tenantDetailsService.postSingleTenantConfig(obj).subscribe((res: any) => {
                this.notificationsService.success(
                    'Success!',
                    'Successfully "' + obj.modules + '" Created'
                )
                this.fetchTenantDetails();
            });
        }
    }

    /* Function to delete tenant config */
    deleteTenantConfig(obj: any) {
        this.tenantDetailsService.deleteTenantConfig(obj.id).subscribe((res: any) => {
            this.notificationsService.success(
                'Success!',
                'Successfully "' + obj.modules + '" Deleted'
            )
            this.fetchTenantDetails();
        });
    }

    testEdit(obj, i) {
        //console.log('testEdit ' + JSON.stringify(obj));
        this.tenantConfigModule[i]['tempMinDate'] = this.tenantConfigModule[i].startDate;
    }
}
