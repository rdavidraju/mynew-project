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
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { ChartOfAccount, ChartOfAccountBreadCrumbTitles } from './chart-of-account.model';
import { ChartOfAccountService } from './chart-of-account.service';
import { SessionStorageService } from 'ng2-webstorage';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { MappingSet } from '../mapping-set/mapping-set.model';
import { MappingSetService } from '../mapping-set/mapping-set.service';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-chart-of-account-detail',
    templateUrl: './chart-of-account-detail.component.html'
})
export class ChartOfAccountDetailComponent implements OnInit {

    private UserData = this.$sessionStorage.retrieve('userData');
    chartOfAccount = new ChartOfAccount();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA: boolean = false;
    segmentLength: any;
    segmentSeperator: any;
    valueSetDialog: boolean = false;
    coaList: any;
    segmentCurIndex: any;
    segmentCurLenth: any;
    /**Mapping Set Form*/
    mappingSet = new MappingSet();
    /**List of Mapping Set */
    valueSetsList: any = [];
    isEditNew: boolean = false;

    constructor(
        private eventManager: JhiEventManager,
        private chartOfAccountService: ChartOfAccountService,
        private route: ActivatedRoute,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        public dialog: MdDialog,
        private $sessionStorage: SessionStorageService,
        private mappingSetService: MappingSetService,
        private lookUpCodeService: LookUpCodeService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 161) + 'px'
        });
        this.loadAll();
        this.getQualifier();
        this.segmentLength = [{segLen:1},{segLen:2},{segLen:3},{segLen:4},{segLen:5}];
        this.segmentSeperator = [{lookUpCode:'.',meaning:'Period(.)'},
                                 {lookUpCode:'-',meaning:'Dash(-)'}];
    }

    /**Get COA detail and segments detail by id */
    loadAll(){
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
                this.chartOfAccountService.getCOAandSegments(params['id']).subscribe((chartOfAccount) => {
                        this.chartOfAccount = chartOfAccount;
                        this.chartOfAccount.startDate = this.dateUtils.convertLocalDateFromServer(this.chartOfAccount.startDate);
                        this.chartOfAccount.endDate = this.dateUtils.convertLocalDateFromServer(this.chartOfAccount.endDate);
                        // console.log('this.chartOfAccount\n' + JSON.stringify(this.chartOfAccount));
                        this.isCreateNew = false;
                        /**Get Mapping Sets List by Purpose */
                        this.valueSetsList = [];
                        for(let i=0; i<this.chartOfAccount.segments.length; i++){
                            let obj = {
                                'id': this.chartOfAccount.segments[i].valueId,
                                'name': this.chartOfAccount.segments[i].valueName,
                            }
                            this.valueSetsList.push(obj);
                        }
                        // this.getAllMappingSet();
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
                this.isValSetEdit = true;
                /**Get Mapping Sets List by Purpose */
                // this.getAllMappingSet();
                /**Focusing on first element */
                $('#name').focus();
                /**First empty row by default while creating */
                for (let i = 0; i < 2; i++) {
                    let obj = {
                        'segmentName': null,
                        'valueId': null,
                        'segmentLength': null,
                        'columnEdit': true
                    };
                    this.chartOfAccount.segments.push(obj);
                }
            }
        });
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


    /**Edit Segment Details */
    editSegment(i){
        let throwValidation:any = false;
        for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
            if(this.chartOfAccount.segments[i].columnEdit == true){
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.notificationsService.error('Warning!','Please save row before editing');
        }else{
            this.chartOfAccount.segments[i].columnEdit = true;
        }
    }

    /**Cancel changes made in segments */
    cancelEdit(){
        this.loadAll();
    }

    /**Delete Segment */
    deleteSegment(segment){
        // console.log('Delete segment:\n' + JSON.stringify(segment));
        this.chartOfAccountService.deleteSegment(segment.id).subscribe((res: any)=>{
            this.notificationsService.success('Success!',segment.segmentName +' Deleted Successfully');
            this.loadAll();
        });
    }

    /**Delete Row */
    deleteRow(index){
        if(this.chartOfAccount.segments.length < 3){
            this.notificationsService.error('Warning!','Atleast two segments are mandatory');
        }else{
            this.chartOfAccount.segments.splice(index, 1);
        }
    }

    /**Delete Value Set row */
    deleteValueRow(index) {
        if (this.mappingSet.mappingSetValues < 2) {
            this.notificationsService.error('Warning!', 'Atleast one row is mandatory');
        } else {
            this.mappingSet.mappingSetValues.splice(index, 1);
        }
    }

    /**Update segment Detail */
    updateSegment(segment) {
        let throwValidation: any = false;
        for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
            if (this.chartOfAccount.segments[i].segmentName == null || this.chartOfAccount.segments[i].valueId == null ||
                this.chartOfAccount.segments[i].segmentLength == null) {
                throwValidation = true;
            }
        }
        if (throwValidation == true) {
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        } else {
            if (segment.id) {
                /**Update segment Detail */
                // console.log('Update segment:\n' + JSON.stringify(segment));
                this.chartOfAccountService.updateOrNewsegments(segment).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success('Success!', segment.segmentName + ' Updated Successfully');
                    this.loadAll();
                });
            } else {
                /**Add segment Detail */
                segment.coaId = this.chartOfAccount.id;
                segment.createdBy = this.UserData.id;
                segment.lastUpdatedBy = this.UserData.id;
                segment.lastUpdatedDate = new Date();
                segment.createdDate = new Date();
                segment.enabledFlag = true;
                // console.log('Add New segment:\n' + JSON.stringify(segment));
                this.chartOfAccountService.updateOrNewsegments(segment).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success('Success!', segment.segmentName + ' Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    /* Function to display validition message */
    validationMsg(form) {
        if(form == 'COA'){
            if(this.chartOfAccount.name == null || this.chartOfAccount.name == ''){
                this.notificationsService.error(
                    'Warning!',
                    'Name is mandatory'
                );
            }else if(this.chartOfAccount.startDate == null){
                this.notificationsService.error(
                    'Warning!',
                    'Start date is mandatory'
                );
            }else if(this.chartOfAccount.segmentSeperator == null){
                this.notificationsService.error(
                    'Warning!',
                    'Segment Separator is mandatory'
                );
            }
        }else{
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        }
    }

    /**Add Row in Segment */
    addNewSegment() {
        let throwValidation:any = false;
        for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
            if (this.chartOfAccount.segments[i].segmentName == null || this.chartOfAccount.segments[i].valueId == null ||
                this.chartOfAccount.segments[i].segmentLength == null) {
                throwValidation = true;
            }else if(this.chartOfAccount.segments[i].columnEdit == true){
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
                'segmentName': null,
                'valueId': null,
                'segmentLength': null,
                'columnEdit': true
            };
            this.chartOfAccount.segments.push(obj);
        }
    }

    /**Add row in value set table */
    addRowValueSetValues() {
        let throwValidation: any = false;
        for (let i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
            if (this.mappingSet.mappingSetValues[i].sourceValue == '' || this.mappingSet.mappingSetValues[i].targetValue == '' ||
                this.mappingSet.mappingSetValues[i].startDate == null) {
                throwValidation = true;
            }
        }
        if(throwValidation == true){
            this.notificationsService.error('Warning!','Fill mandatory fields');
        }else{
            let obj = {
                'sourceValue': '',
                'targetValue': '',
                'startDate': null,
                'endDate': null,
                'columnEdit': true
            };
            this.mappingSet.mappingSetValues.push(obj);
        }
    }

    /**Save COA */
    saveCoa() {
        if (!this.nameExist)
            this.chartOfAccountService.checkCOAIsExist(this.chartOfAccount.name, this.chartOfAccount.id).subscribe(res => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.nameExist) {
                    let link: any = '';
                    let segmentEmpty: any = false;
                    if (this.chartOfAccount.id) {
                        /**Update COA */
                        this.chartOfAccount.tenantId = this.UserData.tenantId;
                        // console.log('Update this.chartOfAccount\n' + JSON.stringify(this.chartOfAccount));
                        this.chartOfAccountService.updateCOA(this.chartOfAccount).subscribe((res: any) => {
                            // console.log('res\n' + JSON.stringify(res));
                            this.notificationsService.success(
                                'Success!',
                                '"' + res.name + '" Successfully Updated'
                            );
                            if (res.id) {
                                link = ['/chart-of-account', { outlets: { 'content': res.id + '/details' } }];
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
                        /**Create New COA */
                        if (this.chartOfAccount.segments.map(x => x.qualifier).indexOf('BALANCING') != -1 ||
                            this.chartOfAccount.segments.map(x => x.qualifier).indexOf('NATURAL_ACCOUNT') != -1) {
                            for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
                                if (this.chartOfAccount.segments[i].segmentName == null || this.chartOfAccount.segments[i].valueId == null ||
                                    this.chartOfAccount.segments[i].segmentLength == null) {
                                    segmentEmpty = true;
                                }
                            }
                            if (segmentEmpty == true) {
                                this.notificationsService.error('Warning!', 'Fill mandatory fields');
                            } else {
                                this.chartOfAccountService.postCOAandSegments(this.chartOfAccount).subscribe((res: any) => {
                                    this.notificationsService.success('Success!', '"' + res.coaName + '" Successfully Created');
                                    if (res.coaId) {
                                        link = ['/chart-of-account', { outlets: { 'content': res.coaId + '/details' } }];
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
                        } else {
                            this.notificationsService.info('Warning!', 'Segments should contain "Balancing" and "Natural Account" qualifier');
                        }
                    }
                }

            });
    }

    /**Create Mapping Set */
    createMappingSet(segment,index){
        if(segment.valueId == 'CreateNew'){
            if(segment.segmentLength == null || segment.segmentName == null || segment.segmentName == ''){
                this.notificationsService.error('Warning!','Fill mandatory fields');
                setTimeout(() => {
                    this.chartOfAccount.segments[index].valueId = undefined;
                }, 100);
            }else{
                this.mappingSet.name = null;
                this.mappingSet.description = null;
                this.mappingSet.startDate = null;
                this.mappingSet.endDate = null;
                // console.log('segment\n' + JSON.stringify(segment));
                /* this.mappingSet.name = segment.valueId;
                this.mappingSet.description = segment.valueId; */
                this.segmentCurIndex = index;
                this.segmentCurLenth = segment.segmentLength;
                this.valueSetDialog = true;
                this.isEditNew = true;
                this.isValSetEdit = true;
                let obj = {
                    'sourceValue': '',
                    'targetValue': '',
                    'startDate': null,
                    'endDate': null,
                    'columnEdit': true
                };
                this.mappingSet.mappingSetValues = [];
                this.mappingSet.mappingSetValues.push(obj);
            }
        }
    }

    /**Save COA and Mapping Set  */
    saveValueSet() {
        let throwValidation = false;
        for (var i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
            if (this.mappingSet.name == null || this.mappingSet.name == '' ||
                this.mappingSet.startDate == null ||
                this.mappingSet.mappingSetValues[i].sourceValue == '' ||
                this.mappingSet.mappingSetValues[i].targetValue == '' ||
                this.mappingSet.mappingSetValues[i].startDate == null) {
                throwValidation = true;
            }
        }
        if (throwValidation == true) {
            this.notificationsService.error('Warning!', 'Fill mandatory fields');
        } else {
            if (this.mappingSet.id) {
                this.mappingSet.purpose = 'CHART_OF_ACCOUNT';
                this.mappingSetService.updateMappingSet(this.mappingSet).subscribe((mappingSet: any) => {
                    this.notificationsService.success('Success!','Value set successfully updated');
                    this.showValueSet(this.mappingSet.id);
                });
            } else {
                this.mappingSet.lookUppurpose = 'CHART_OF_ACCOUNT';
                this.mappingSet.enabledFlag = true;
                if (this.mappingSet.mappingSetValues.length > 0) {
                    for (var i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
                        this.mappingSet.mappingSetValues[i].status = true;
                    }
                }
                // console.log('this.mappingSet\n', JSON.stringify(this.mappingSet));
                this.mappingSetService.postMappingSetsWithValues(this.mappingSet).subscribe((res: any) => {
                    // console.log(res);
                    /**Get Mapping Sets List by Purpose */
                    let obj = {
                        'id': res.id,
                        'name': res.name,
                    }
                    this.valueSetsList.push(obj);
                    // this.getAllMappingSet();
                    this.notificationsService.success('Success!', res.name + ' Successfully Created');
                    this.valueSetDialog = false;
                    setTimeout(() => {
                        this.chartOfAccount.segments[this.segmentCurIndex].valueId = res.id;
                    }, 100);
                });
            }
        }
    }


    /**Check calendar name duplicates */
    checkDuplicates(name, id) {
        if(this.coaList){
            for (let i = 0; i < this.coaList.length; i++) {
                if (this.coaList[i].name == name && this.coaList[i].id == id) {
                    // console.log('No Duplicates');
                } else if (this.coaList[i].name == name) {
                    this.notificationsService.error('Warning!', 'Chart of Account name "' + name + '" alreay exist');
                    setTimeout(() => {
                        this.chartOfAccount.name = null;
                    }, 500);
                }
            }
        }
    }

    /**Get all Mapping Set */
    /* getAllMappingSet(){
        this.mappingSetService.getMappingSetsNValuesListByPurpose('CHART_OF_ACCOUNT').subscribe((res: any)=>{
            this.valueSetsList = res;
            // console.log('this.valueSetsList\n' + JSON.stringify(this.valueSetsList));
        });
    } */

    /**Function on p-dialog hide */
    dialogHide(){
        if(this.segmentCurIndex != null){
            this.chartOfAccount.segments[this.segmentCurIndex].valueId = null;
        }
    }

    /**Allow only numbers in code */
    allowNumbersOnly(e){
        e = (e) ? e : window.event;
        var charCode = (e.which) ? e.which : e.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            return false;
        }
        return true;
    }

    /**Check Value Set Length */
    checkCodeLength(value, i){
        if(value){
            value = value.toString();
            if(value.length > this.segmentCurLenth){
                this.notificationsService.error('Warning!', 'Maximum Length for code is '+this.segmentCurLenth);
                this.mappingSet.mappingSetValues[i].sourceValue = undefined;
            }else if(value.length < this.segmentCurLenth){
                this.notificationsService.error('Warning!', 'Minimum Length for code is '+this.segmentCurLenth);
                this.mappingSet.mappingSetValues[i].sourceValue = undefined;
            }
            else {
                return true;
            }
        }
    }

    isValSetEdit: boolean = false;
    showValueSet(id) {
        this.mappingSetService.getMappingSetsWithValues(id).subscribe(res => {
            this.mappingSet = res;
            this.mappingSet.startDate = this.dateUtils.convertLocalDateFromServer(this.mappingSet.startDate);
            this.mappingSet.endDate = this.dateUtils.convertLocalDateFromServer(this.mappingSet.endDate);
            if(this.mappingSet && this.mappingSet.mappingSetValues){
                this.mappingSet.mappingSetValues.forEach(mapVal => {
                    mapVal.startDate = this.dateUtils.convertLocalDateFromServer(mapVal.startDate);
                    mapVal.endDate = this.dateUtils.convertLocalDateFromServer(mapVal.endDate);
                });
            }
            this.isValSetEdit = false;
            this.valueSetDialog = true;
        });
    }

    updateMapSetsVal(mappingSetValues, index) {
        if (this.checkCodeLength(mappingSetValues.sourceValue, index)) {
            if (mappingSetValues.sourceValue == '' || undefined || null ||
                mappingSetValues.targetValue == '' || undefined || null ||
                mappingSetValues.startDate == null) {
                this.notificationsService.error('Warning!', 'fill mandatory fields');
                this.mappingSet.mappingSetValues[index].columnEdit = true;
            } else {
                if (mappingSetValues.id == null || mappingSetValues.id == undefined || mappingSetValues.id == '') {
                    mappingSetValues.mappingSetId = this.mappingSet.id;
                    mappingSetValues.status = true;
                    this.mappingSetService.updateMappingSetValues(mappingSetValues).subscribe((res: any) => {
                        this.notificationsService.success('Success!', 'New value successfully added');
                        this.showValueSet(this.mappingSet.id);
                    });
                } else {
                    this.mappingSetService.updateMappingSetValues(mappingSetValues).subscribe((res: any) => {
                        this.notificationsService.success('Success!', 'Value successfully updated');
                        this.showValueSet(this.mappingSet.id);
                    });
                }
            }
        } else {
            this.mappingSet.mappingSetValues[index].columnEdit = true;
        }
    }

    qualifier:any[];
    getQualifier() {
        this.lookUpCodeService.getAllLookups('coa_qualifier').subscribe(res => {
            this.qualifier = res;
        });
    }

    curSegInd(i) {
        this.segmentCurLenth = this.chartOfAccount.segments[i].segmentLength;
    }

    qualifierChange(qualifier, i) {
        if (qualifier == 'BALANCING' || qualifier == 'NATURAL_ACCOUNT') {
            let count = this.chartOfAccount.segments.filter(segment => segment.qualifier == qualifier).length;
            if (count > 1) {
                this.notificationsService.error('Warning!', 'Only one "Balancing" and "Natural Account" qualifier is allowed');
                setTimeout(() => {
                    this.chartOfAccount.segments[i].qualifier = null;
                }, 100);
            }
        }
    }

    deleteMapValue(mapValue) {
        this.mappingSetService.deleteMappingSetValue(mapValue.id).subscribe((res: any) => {
            this.notificationsService.success('Success!', mapValue.sourceValue + ' Deleted');
            this.showValueSet(this.mappingSet.id);
        });
    }

    /**Check chart-of-account name duplicates */
    nameExist: string;
    isNameExist(name, id) {
        if (name && !this.nameExist)
        this.chartOfAccountService.checkCOAIsExist(name, id).subscribe(res => {
            this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
        });
    }



    /* this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInChartOfAccounts(); */

/*     load(id) {
        this.chartOfAccountService.find(id).subscribe((chartOfAccount) => {
            this.chartOfAccount = chartOfAccount;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChartOfAccounts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chartOfAccountListModification',
            (response) => this.load(this.chartOfAccount.id)
        );
    } */
}
