import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { FxRates } from './fx-rates.model';
import { FxRatesService } from './fx-rates.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-fx-rates-detail',
    templateUrl: './fx-rates-detail.component.html'
})
export class FxRatesDetailComponent implements OnInit, OnDestroy {

    fxRates = new FxRates();
    private unsubscribe: Subject<void> = new Subject();
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    presentPath: any;
    presentUrl: any;
    conversionType: any;
    currencies: any = this.commonService.currencies;
    startDate: any;
    endDate: any;
    isStChanged = false;
    nameExist: string;
    statuses = [{code: true,value: 'Active'},{code: false,value: 'Inactive'}];
    currentDate = new Date();

    constructor(
        private fxRatesService: FxRatesService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        private dateUtils: JhiDateUtils
    ) {}

    ngOnInit() {
        // Get FX Rates and details by id
        this.loadAll();
        this.lookupcode();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    // Get FX Rates and details by id
    loadAll(){
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;

            if (params['id']) {
                this.fxRatesService.getFxRatesAndFxRatesDetailsById(params['id']).takeUntil(this.unsubscribe).subscribe((fxrates) => {
                        this.fxRates = fxrates;
                        this.fxRates.startDate = this.dateUtils.convertDateTimeFromServer(this.fxRates.startDate);
                        this.fxRates.endDate = this.dateUtils.convertDateTimeFromServer(this.fxRates.endDate);
                        for(let i=0; i<this.fxRates.fxRatesDetails.length; i++){
                            this.fxRates.fxRatesDetails[i].fromDate = this.dateUtils.convertDateTimeFromServer(this.fxRates.fxRatesDetails[i].fromDate);
                            this.fxRates.fxRatesDetails[i].toDate = this.dateUtils.convertDateTimeFromServer(this.fxRates.fxRatesDetails[i].toDate);
                        }
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            // Focusing on first element
                            $('#name').focus();
                        } else {
                            this.fxRates.description = !this.fxRates.description ? '-' : this.fxRates.description;
                            this.fxRates.endDate = !this.fxRates.endDate ? '-' : this.fxRates.endDate;
                            this.isViewOnly = true;
                        }
                    }
                );
            } else {
                this.isCreateNew = true;
                // Focusing on first element
                $('#name').focus();
                this.fxRates.startDate = new Date();
                // First empty row by default while creating
                const obj = {
                    'fromCurrency': null,
                    'toCurrency': null,
                    'conversionRate': null,
                    'inverseRate': null,
                    'fromDate': null,
                    'toDate': null,
                    'frmCrnSrch': null,
                    'toCrnSrch': null,
                    'columnEdit': true
                };
                this.fxRates.fxRatesDetails.push(obj);
            }
        });
    }

    // Save FX Rates
    saveFxRates() {
        if (!this.nameExist) {
            this.fxRatesService.checkFxRatesIsExist(this.fxRates.name, this.fxRates.id).takeUntil(this.unsubscribe).subscribe((nameRes) => {
                this.nameExist = nameRes.result != 'No Duplicates Found' ? nameRes.result : undefined;
                if (!this.nameExist) {
                    let link: any = '';
                    let fxRatesEmpty: any = false;
                    if (this.fxRates.id) {
                        // Update FX Rates
                        this.fxRatesService.updateFXRates(this.fxRates).takeUntil(this.unsubscribe).subscribe((res: any) => {
                            this.commonService.success('Success!','FX Rates "' + res.name + '" Successfully Updated');
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
                        // Create New FX Rates
                        for (let i = 0; i < this.fxRates.fxRatesDetails.length; i++) {
                            if (this.fxRates.fxRatesDetails[i].fromCurrency == null || this.fxRates.fxRatesDetails[i].toCurrency == null ||
                                this.fxRates.fxRatesDetails[i].conversionRate == null || this.fxRates.fxRatesDetails[i].inverseRate == null ||
                                this.fxRates.fxRatesDetails[i].fromDate == null || this.fxRates.fxRatesDetails[i].toDate == null) {
                                fxRatesEmpty = true;
                            }
                        }
                        if (fxRatesEmpty == true && this.isCreateNew == true) {
                            this.commonService.error('Warning!','Fill mandatory fields');
                        } else {
                            this.fxRatesService.postFxRatesAndFxRatesDetails(this.fxRates).takeUntil(this.unsubscribe).subscribe((res: any) => {
                                this.commonService.success('Success!','FX Rates "' + res.name + '" Successfully Created');
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
            });
        }
    }

    // Get conversion type
    lookupcode(){
        this.fxRatesService.lookupCodes('CONVERSION_TYPE').takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.conversionType = res;
        });
    }

    // Function to display validition message
    validationMsg(form) {
        if(form == 'fxRates'){
            if(this.fxRates.name == null || this.fxRates.name == ''){
                this.commonService.error('Warning!','Name is mandatory');
            }else if(this.fxRates.conversionType == null){
                this.commonService.error('Warning!','Conversion Type is mandatory');
            }else if(this.fxRates.startDate == null){
                this.commonService.error('Warning!','Start date is mandatory');
            }else{
                this.commonService.error('Warning!','Fill mandatory fields in FX rates details');
            }
        }else{
            this.commonService.error('Warning!','Fill mandatory fields');
        }
    }

    // Add Row in FX Rates
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
            this.commonService.error('Warning!','Please save row before adding new');
        }else if(throwValidation == true){
            this.commonService.error('Warning!','Fill mandatory fields');
        }else {
            const obj = {
                'fromCurrency': null,
                'toCurrency': null,
                'conversionRate': null,
                'inverseRate': null,
                'fromDate': null,
                'toDate': null,
                'frmCrnSrch': null,
                'toCrnSrch': null,
                'columnEdit': true,
                'newFxrateDetStatus': true
            };
            this.fxRates.fxRatesDetails.push(obj);
        }
    }

    // Update FX Rates Detail
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
            this.commonService.error('Warning!','Fill mandatory fields');
        } else {
            if (fxRatesDetail.id) {
                // Update FXRate Detail
                this.fxRatesService.updateFXRatesDetail(fxRatesDetail).takeUntil(this.unsubscribe).subscribe(() => {
                    this.commonService.success('Success!', fxRatesDetail.fromCurrency + ' to ' + fxRatesDetail.toCurrency + ' Updated Successfully');
                    this.loadAll();
                });
            } else {
                // Add New FXRate Detail
                fxRatesDetail.fxRateId = this.fxRates.id;
                fxRatesDetail.lastUpdatedDate = new Date();
                fxRatesDetail.createdDate = new Date();
                fxRatesDetail.enabledFlag = true;
                this.fxRatesService.updateFXRatesDetail(fxRatesDetail).takeUntil(this.unsubscribe).subscribe(() => {
                    this.commonService.success('Success!', fxRatesDetail.fromCurrency + ' to ' + fxRatesDetail.toCurrency + ' Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    // Delete FX Rates Detail
    deleteFxRatesDetail(fxRatesDetail){
        this.fxRatesService.deleteFXRatesDetail(fxRatesDetail.id).takeUntil(this.unsubscribe).subscribe(()=>{
            this.commonService.success('Success!',fxRatesDetail.fromCurrency + ' to ' + fxRatesDetail.toCurrency + ' Deleted Successfully');
            this.loadAll();
        });
    }

    // Cancel Changes
    cancelEdit(){
        this.loadAll();
    }

    // Delete Row
    deleteRow(index){
        if(this.fxRates.fxRatesDetails.length < 2){
            this.commonService.error('Warning!','Atleast one row is mandatory');
        }else{
            this.fxRates.fxRatesDetails.splice(index, 1);
        }
    }

    // Edit FX Rates Detail
    editFXRatesDetail(index){
        let throwValidation:any = false;
        for (let i = 0; i < this.fxRates.fxRatesDetails.length; i++) {
            if(this.fxRates.fxRatesDetails[i].columnEdit == true){
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.commonService.error('Warning!','Please save row before editing');
        }else{
            this.fxRates.fxRatesDetails[index].columnEdit = true;
        }
    }

    // Prevent selecting same currencies from and to
    checkFromToCurrency(currency, where, i) {
        if (currency.fromCurrency == currency.toCurrency) {
            this.commonService.error('Warning!', '"From" and "To" currency should not be same');
            setTimeout(() => {
                if (where == 'fromCurrency') {
                    this.fxRates.fxRatesDetails[i].fromCurrency = null;
                } else {
                    this.fxRates.fxRatesDetails[i].toCurrency = null;
                }
            }, 300);
        }
    }

    isNameExist(name, id) {
        if (name) {
            this.fxRatesService.checkFxRatesIsExist(name, id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
            });
        }
    }

    /**
     * @author Sameer
     * @description Export data to specific format
     * @param id 
     * @param type 
     */
    exportFXratesDetails(id, type) {
        this.fxRatesService.getFxRatesAndFxRatesDetailsById(id, true, type).takeUntil(this.unsubscribe).subscribe((res) => {
            this.commonService.exportData(res, type, this.fxRates.name);
        },
        () => {
            this.commonService.error('Warning!', 'Error Occured');
        });
    }
}