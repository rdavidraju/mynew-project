import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Response } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { FxRates, FxRatesBreadCrumbTitles } from './fx-rates.model';
import { FxRatesService } from './fx-rates.service';
import { SessionStorageService } from 'ng2-webstorage';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-fx-rates-detail',
    templateUrl: './fx-rates-detail.component.html'
})
export class FxRatesDetailComponent implements OnInit {

    private UserData = this.$sessionStorage.retrieve('userData');
    fxRates = new FxRates();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA: boolean = false;
    conversionType: any;
    currencies: any = this.commonService.currencies;
    startDate: any;
    endDate: any;

    constructor(
        private eventManager: JhiEventManager,
        private fxRatesService: FxRatesService,
        private route: ActivatedRoute,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        public dialog: MdDialog,
        private $sessionStorage: SessionStorageService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        /* this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFxRates(); */
        this.commonService.screensize();
        $(".split-example").css({'height': 'auto','min-height': (this.commonService.screensize().height - 161) + 'px'});

        /**Get FX Rates and details by id */
        this.loadAll();
        this.lookupcode();
    }

    /**Get FX Rates and details by id */
    loadAll(){
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "fx-rates") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                this.fxRatesService.getFxRatesAndFxRatesDetailsById(params['id']).subscribe((fxrates) => {
                        this.fxRates = fxrates;
                        this.fxRates.startDate = this.dateUtils.convertLocalDateFromServer(this.fxRates.startDate);
                        this.fxRates.endDate = this.dateUtils.convertLocalDateFromServer(this.fxRates.endDate);
                        for(let i=0; i<this.fxRates.fxRatesDetails.length; i++){
                            this.fxRates.fxRatesDetails[i].fromDate = this.dateUtils.convertLocalDateFromServer(this.fxRates.fxRatesDetails[i].fromDate);
                            this.fxRates.fxRatesDetails[i].toDate = this.dateUtils.convertLocalDateFromServer(this.fxRates.fxRatesDetails[i].toDate);
                        }
                        // console.log('this.fxRates\n', JSON.stringify(this.fxRates));
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            /**Focusing on first element */
                            $('#name').focus();
                        } else {
                            this.isViewOnly = true;
                        }
                    }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                /**Focusing on first element */
                $('#name').focus();
                /**First empty row by default while creating */
                let obj = {
                    'fromCurrency': null,
                    'toCurrency': null,
                    'conversionRate': null,
                    'inverseRate': null,
                    'fromDate': null,
                    'toDate': null,
                    'columnEdit': true
                };
                this.fxRates.fxRatesDetails.push(obj);
            }
        });
    }

    /**Save FX Rates */
    saveFxRates() {
        let link: any = '';
        let fxRatesEmpty: any = false;
        if (this.fxRates.id) {
            /**Update FX Rates */
            console.log('Update this.fxRates\n' + JSON.stringify(this.fxRates));
            this.fxRatesService.updateFXRates(this.fxRates).subscribe((res: any) => {
                // console.log('res\n' + JSON.stringify(res));
                this.notificationsService.success(
                    'Success!',
                    'FX Rates "' + res.name + '" Successfully Updated'
                );
                if (res.id) {
                    link = ['/fx-rates', { outlets: { 'content': res.id + '/details' } }];
                    if (this.isEdit) {
                        this.isEdit = false;
                    }
                    if (this.isCreateNew) {
                        this.isCreateNew = false;
                    }
                    this.isViewOnly = true;
                    this.router.navigate(link);
                }
            });
        } else {
            /**Create New FX Rates */
            for (let i = 0; i < this.fxRates.fxRatesDetails.length; i++) {
                if (this.fxRates.fxRatesDetails[i].fromCurrency == null || this.fxRates.fxRatesDetails[i].toCurrency == null ||
                    this.fxRates.fxRatesDetails[i].conversionRate == null || this.fxRates.fxRatesDetails[i].inverseRate == null ||
                    this.fxRates.fxRatesDetails[i].fromDate == null || this.fxRates.fxRatesDetails[i].toDate == null) {
                    fxRatesEmpty = true;
                }
            }
            if (fxRatesEmpty == true && this.isCreateNew == true) {
                this.notificationsService.error(
                    'Warning!',
                    'Fill mandatory fields'
                );
            } else {
                // console.log('Create new this.fxRates\n' + JSON.stringify(this.fxRates));
                this.fxRatesService.postFxRatesAndFxRatesDetails(this.fxRates).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success(
                        'Success!',
                        'FX Rates "' + res.name + '" Successfully Created'
                    );
                    if (res.id) {
                        link = ['/fx-rates', { outlets: { 'content': res.id + '/details' } }];
                        if (this.isEdit) {
                            this.isEdit = false;
                        }
                        if (this.isCreateNew) {
                            this.isCreateNew = false;
                        }
                        this.isViewOnly = true;
                        this.router.navigate(link);
                    }
                });
            }
        }
    }

    /**Empty and column edit Validation in FX rates details */
    /* fxRatesDetailsValidation(){
        for(let i=0;i<this.fxRates.fxRatesDetails.length;i++){
            if(this.fxRates.fxRatesDetails[i].fromCurrency == null || this.fxRates.fxRatesDetails[i].toCurrency == null ||
               this.fxRates.fxRatesDetails[i].conversionRate == null || this.fxRates.fxRatesDetails[i].inverseRate == null ||
               this.fxRates.fxRatesDetails[i].fromDate == null || this.fxRates.fxRatesDetails[i].toDate == null){
                this.fxRateDetailValid = true;
               }else if(this.fxRates.fxRatesDetails[i].columnEdit == true){
                this.fxRateDetailValid =  'columnEditTrue';
               }
        }
    } */

    /**Get conversion type */
    lookupcode(){
        this.fxRatesService.lookupCodes('CONVERSION_TYPE').subscribe((res: any)=>{
            this.conversionType = res;
        });
    }

    /* Function to display validition message */
    validationMsg(form) {
        if(form == 'fxRates'){
            if(this.fxRates.name == null || this.fxRates.name == ''){
                this.notificationsService.error(
                    'Warning!',
                    'Name is mandatory'
                );
            }else if(this.fxRates.conversionType == null){
                this.notificationsService.error(
                    'Warning!',
                    'Conversion Type is mandatory'
                );
            }else if(this.fxRates.startDate == null){
                this.notificationsService.error(
                    'Warning!',
                    'Start date is mandatory'
                );
            }else{
                this.notificationsService.error(
                    'Warning!',
                    'Fill mandatory fields in FX rates details'
                );
            }
        }else{
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        }
    }

    /**Toggle Sidebar */
    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }

    /**Add Row in FX Rates */
    addNewFxRates() {
        let throwValidation:any = false;
        for (let i = 0; i < this.fxRates.fxRatesDetails.length; i++) {
            if (this.fxRates.fxRatesDetails[i].fromCurrency == null || this.fxRates.fxRatesDetails[i].toCurrency == null ||
                this.fxRates.fxRatesDetails[i].conversionRate == null || this.fxRates.fxRatesDetails[i].inverseRate == null ||
                this.fxRates.fxRatesDetails[i].fromDate == null || this.fxRates.fxRatesDetails[i].toDate == null) {
                throwValidation = true;
            }else if(this.fxRates.fxRatesDetails[i].columnEdit == true){
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue' && this.isViewOnly == true) {
            this.notificationsService.error(
                'Warning!',
                'Please save row before adding new'
            );
        }else if(throwValidation == true){
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        }else {
            let obj = {
                'fromCurrency': null,
                'toCurrency': null,
                'conversionRate': null,
                'inverseRate': null,
                'fromDate': null,
                'toDate': null,
                'columnEdit': true,
                'newFxrateDetStatus': true
            };
            this.fxRates.fxRatesDetails.push(obj);
        }
    }

    /**Update FX Rates Detail */
    updateFxRatesDetail(fxRatesDetail) {
        let throwValidation: any = false;
        for (let i = 0; i < this.fxRates.fxRatesDetails.length; i++) {
            if (this.fxRates.fxRatesDetails[i].fromCurrency == null || this.fxRates.fxRatesDetails[i].toCurrency == null ||
                this.fxRates.fxRatesDetails[i].conversionRate == null || this.fxRates.fxRatesDetails[i].inverseRate == null ||
                this.fxRates.fxRatesDetails[i].fromDate == null || this.fxRates.fxRatesDetails[i].toDate == null) {
                throwValidation = true;
            }
        }
        if (throwValidation == true) {
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        } else {
            if (fxRatesDetail.id) {
                /**Update FXRate Detail */
                // console.log('Update fxRatesDetail:\n' + JSON.stringify(fxRatesDetail));
                this.fxRatesService.updateFXRatesDetail(fxRatesDetail).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success('Success!', fxRatesDetail.fromCurrency + ' to ' + fxRatesDetail.toCurrency + ' Updated Successfully');
                    this.loadAll();
                });
            } else {
                /**Add New FXRate Detail */
                fxRatesDetail.fxRateId = this.fxRates.id;
                fxRatesDetail.createdBy = this.UserData.id;
                fxRatesDetail.lastUpdatedBy = this.UserData.id;
                fxRatesDetail.lastUpdatedDate = new Date();
                fxRatesDetail.createdDate = new Date();
                fxRatesDetail.enabledFlag = true;
                // console.log('Add New fxRatesDetail:\n' + JSON.stringify(fxRatesDetail));
                this.fxRatesService.updateFXRatesDetail(fxRatesDetail).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success('Success!', fxRatesDetail.fromCurrency + ' to ' + fxRatesDetail.toCurrency + ' Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    /**Delete FX Rates Detail */
    deleteFxRatesDetail(fxRatesDetail){
        // console.log('Delete fxRatesDetail:\n' + JSON.stringify(fxRatesDetail));
        this.fxRatesService.deleteFXRatesDetail(fxRatesDetail.id).subscribe((res: any)=>{
            this.notificationsService.success('Success!',fxRatesDetail.fromCurrency + ' to ' + fxRatesDetail.toCurrency + ' Deleted Successfully');
            this.loadAll();
        });
    }

    /**Cancel Changes */
    cancelEdit(){
        this.loadAll();
    }

    /**Delete Row */
    deleteRow(index){
        if(this.fxRates.fxRatesDetails.length < 2){
            this.notificationsService.error('Warning!','Atleast one row is mandatory');
        }else{
            this.fxRates.fxRatesDetails.splice(index, 1);
        }
    }

    /**Edit FX Rates Detail */
    editFXRatesDetail(i){
        let throwValidation:any = false;
        for (let i = 0; i < this.fxRates.fxRatesDetails.length; i++) {
            if(this.fxRates.fxRatesDetails[i].columnEdit == true){
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.notificationsService.error('Warning!','Please save row before editing');
        }else{
            this.fxRates.fxRatesDetails[i].columnEdit = true;
        }
    }

    /**Prevent selecting same currencies from and to */
    checkFromToCurrency(currency, where, i) {
        if (currency.fromCurrency == currency.toCurrency) {
            this.notificationsService.error('Warning!', '"From" and "To" currency should not be same');
            setTimeout(() => {
                if (where == 'fromCurrency') {
                    this.fxRates.fxRatesDetails[i].fromCurrency = null;
                } else {
                    this.fxRates.fxRatesDetails[i].toCurrency = null;
                }
            }, 300);
        }
    }

    /* load(id) {
        this.fxRatesService.find(id).subscribe((fxRates) => {
            this.fxRates = fxRates;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFxRates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'fxRatesListModification',
            (response) => this.load(this.fxRates.id)
        );
    } */
}