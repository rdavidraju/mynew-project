import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { MdDialog } from '@angular/material';
import { ChartOfAccount } from './chart-of-account.model';
import { ChartOfAccountService } from './chart-of-account.service';
import { MappingSet } from '../mapping-set/mapping-set.model';
import { MappingSetService } from '../mapping-set/mapping-set.service';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { BulkUploadDialog } from '../mapping-set/mapping-set-dialog.component';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-chart-of-account-detail',
    templateUrl: './chart-of-account-detail.component.html'
})
export class ChartOfAccountDetailComponent implements OnInit, OnDestroy {

    chartOfAccount = new ChartOfAccount();
    private unsubscribe: Subject<void> = new Subject();
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA = false;
    segmentLength: any;
    segmentSeperator: any;
    valueSetDialog = false;
    coaList: any;
    segmentCurIndex: any;
    segmentCurLenth: any;
    mappingSet = new MappingSet();
    valueSetsList: any = [];
    isEditNew = false;
    nameExist: string;
    qualifier:any[];
    isValSetEdit = false;
    bulkUploadReason: string;
    bulkUploadStatus: string;
    statuses = [{code: true,value: 'Active'},{code: false,value: 'Inactive'}];
    loadDocument = false;
    currentDate = new Date();
    isCoaUsed = false;

    constructor(
        private chartOfAccountService: ChartOfAccountService,
        private route: ActivatedRoute,
        private router: Router,
        private config: NgbDatepickerConfig,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        public dialog: MdDialog,
        private mappingSetService: MappingSetService,
        private lookUpCodeService: LookUpCodeService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        this.loadAll();
        this.getQualifier();
        this._getValueSet();
        this.segmentLength = [{segLen:1},{segLen:2},{segLen:3},{segLen:4},{segLen:5}];
        this.segmentSeperator = [{lookUpCode:'.',meaning:'Period(.)'},
                                 {lookUpCode:'-',meaning:'Dash(-)'}];
    }

    /**Get COA detail and segments detail by id */
    loadAll(){
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;

            if (params['id']) {
                this.chartOfAccountService.getCOAandSegments(params['id']).takeUntil(this.unsubscribe).subscribe((chartOfAccount) => {
                        this.chartOfAccount = chartOfAccount;
                        this.chartOfAccount.startDate = this.dateUtils.convertDateTimeFromServer(this.chartOfAccount.startDate);
                        this.chartOfAccount.endDate = this.dateUtils.convertDateTimeFromServer(this.chartOfAccount.endDate);
                        this.isCreateNew = false;
                        /**Get Mapping Sets List by Purpose */
                        // for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
                        //     const arr = [{
                        //         'data': [
                        //             {
                        //                 'id': this.chartOfAccount.segments[i].valueId,
                        //                 'name': this.chartOfAccount.segments[i].valueName,
                        //             }
                        //         ]
                        //     }];
                        //     this.valueSetsList.push(arr);
                        // }
                        // this.getAllMappingSet();
                        this.chartOfAccountService.isChartofAccountUsed(this.chartOfAccount.id).takeUntil(this.unsubscribe).subscribe((res) => this.isCoaUsed = res,
                        () => this.commonService.error('Warning!', 'Error occured while checking whether COA used in accounting') );
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            /**Focusing on first element */
                            $('#name').focus();
                        } else {
                            this.isViewOnly = true;
                            this.chartOfAccount.description = !this.chartOfAccount.description ? '-' : this.chartOfAccount.description;
                            this.chartOfAccount.endDate = !this.chartOfAccount.endDate ? '-' : this.chartOfAccount.endDate;
                        }
                        this.loadDocument = true;
                    }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                this.isValSetEdit = true;
                this.loadDocument = true;
                /**Get Mapping Sets List by Purpose */
                // this.getAllMappingSet();
                /**Focusing on first element */
                $('#name').focus();
                this.chartOfAccount.startDate = new Date();
                /**First empty row by default while creating */
                for (let i = 0; i < 2; i++) {
                    const obj = {
                        'segmentName': null,
                        'valueId': null,
                        'segmentLength': null,
                        'vsSearch': null,
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
    editSegment(ind) {
        let throwValidation: any = false;
        for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
            if (this.chartOfAccount.segments[i].columnEdit == true) {
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.commonService.error('Warning!', 'Please save row before editing');
        } else {
            this.chartOfAccount.segments[ind].columnEdit = true;
        }
    }

    /**Cancel changes made in segments */
    cancelEdit(){
        this.loadAll();
    }

    deleteRowNew(index) {
        if (this.chartOfAccount.segments.length < 3) {
            this.commonService.error('Warning!', 'Atleast two segments are mandatory');
        } else {
            this.chartOfAccount.segments.splice(index, 1);
        }
    }

    /**Delete Row */
    deleteRow(segment) {
        if (segment.qualifier == 'BALANCING' || segment.qualifier == 'NATURAL_ACCOUNT') {
            this.commonService.error('Warning!', 'Balancing and Natural Account are mandatory');
        } else {
            this.chartOfAccountService.deleteSegment(segment.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.commonService.success('Success!', segment.segmentName + ' Deleted Successfully');
                this.loadAll();
            });
        }
    }

    /**Delete Value Set row */
    deleteValueRow(index) {
        if (this.mappingSet.mappingSetValues.length < 2) {
            this.commonService.error('Warning!', 'Atleast one row is mandatory');
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
            this.commonService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        } else {
            if (segment.id) {
                // Update segment Detail
                segment.coaId = null;
                this.chartOfAccountService.updateOrNewsegments(segment, this.chartOfAccount.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.commonService.success('Success!', segment.segmentName + ' Updated Successfully');
                    this.loadAll();
                });
            } else {
                // Add segment Detail
                segment.lastUpdatedDate = new Date();
                segment.createdDate = new Date();
                segment.enabledFlag = true;
                this.chartOfAccountService.updateOrNewsegments(segment, this.chartOfAccount.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.commonService.success('Success!', segment.segmentName + ' Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    /* Function to display validition message */
    validationMsg(form) {
        if(form == 'COA'){
            if(this.chartOfAccount.name == null || this.chartOfAccount.name == ''){
                this.commonService.error(
                    'Warning!',
                    'Name is mandatory'
                );
            }else if(this.chartOfAccount.startDate == null){
                this.commonService.error(
                    'Warning!',
                    'Start date is mandatory'
                );
            }else if(this.chartOfAccount.segmentSeperator == null){
                this.commonService.error(
                    'Warning!',
                    'Segment Separator is mandatory'
                );
            }
        }else{
            this.commonService.error(
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
            this.commonService.error(
                'Warning!',
                'Please save row before adding new'
            );
        }else if(throwValidation == true){
            this.commonService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        }else {
            const obj = {
                'segmentName': null,
                'valueId': null,
                'segmentLength': null,
                'vsSearch': null,
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
            this.commonService.error('Warning!','Fill mandatory fields');
        }else{
            const obj = {
                'sourceValue': '',
                'targetValue': '',
                'startDate': new Date(),
                'endDate': null,
                'columnEdit': true
            };
            this.mappingSet.mappingSetValues.push(obj);
        }
    }

    /**Save COA */
    saveCoa() {
        if (!this.nameExist) {
            this.chartOfAccountService.checkCOAIsExist(this.chartOfAccount.name, this.chartOfAccount.id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.nameExist) {
                    let link: any = '';
                    let segmentEmpty: any = false;
                    if (this.chartOfAccount.id) {
                        /**Update COA */
                        this.chartOfAccountService.updateCOA(this.chartOfAccount).takeUntil(this.unsubscribe).subscribe((cRes: any) => {
                            this.commonService.success(
                                'Success!',
                                '"' + cRes.name + '" Successfully Updated'
                            );
                            if (cRes.id) {
                                link = ['/chart-of-account', { outlets: { 'content': cRes.id + '/details' } }];
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
                        if (this.chartOfAccount.segments.map((x) => x.qualifier).indexOf('BALANCING') != -1 &&
                            this.chartOfAccount.segments.map((x) => x.qualifier).indexOf('NATURAL_ACCOUNT') != -1) {
                            for (let i = 0; i < this.chartOfAccount.segments.length; i++) {
                                if (this.chartOfAccount.segments[i].segmentName == null || this.chartOfAccount.segments[i].valueId == null ||
                                    this.chartOfAccount.segments[i].segmentLength == null) {
                                    segmentEmpty = true;
                                }
                            }
                            if (segmentEmpty == true) {
                                this.commonService.error('Warning!', 'Fill mandatory fields');
                            } else {
                                this.chartOfAccountService.postCOAandSegments(this.chartOfAccount).takeUntil(this.unsubscribe).subscribe((coaRes)=> {
                                    this.commonService.success('Success!', '"' + coaRes.coaName + '" Successfully Created');
                                    if (coaRes.coaId) {
                                        link = ['/chart-of-account', { outlets: { 'content': coaRes.coaId + '/details' } }];
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
                            this.commonService.info('Warning!', 'Segments should contain "Balancing" and "Natural Account" qualifier');
                        }
                    }
                }

            });
        }
    }

    /**Create Mapping Set */
    createMappingSet(segment,index){
        if(segment.valueId == 'CreateNew'){
            if(segment.segmentLength == null || segment.segmentName == null || segment.segmentName == ''){
                this.commonService.error('Warning!','Fill mandatory fields');
                setTimeout(() => {
                    this.chartOfAccount.segments[index].valueId = undefined;
                }, 100);
            }else{
                this.mappingSet.name = null;
                this.mappingSet.description = null;
                this.mappingSet.startDate = new Date();
                this.mappingSet.endDate = null;
                /* this.mappingSet.name = segment.valueId;
                this.mappingSet.description = segment.valueId; */
                this.segmentCurIndex = index;
                this.segmentCurLenth = segment.segmentLength;
                this.valueSetDialog = true;
                this.isEditNew = true;
                this.isValSetEdit = true;
                const obj = {
                    'sourceValue': '',
                    'targetValue': '',
                    'startDate': new Date(),
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
        for (let i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
            if (this.mappingSet.name == null || this.mappingSet.name == '' ||
                this.mappingSet.startDate == null ||
                this.mappingSet.mappingSetValues[i].sourceValue == '' ||
                this.mappingSet.mappingSetValues[i].targetValue == '' ||
                this.mappingSet.mappingSetValues[i].startDate == null) {
                throwValidation = true;
            }
        }
        if (throwValidation == true) {
            this.commonService.error('Warning!', 'Fill mandatory fields');
        } else {
            if (this.mappingSet.id) {
                this.mappingSet.purpose = 'CHART_OF_ACCOUNT';
                this.mappingSetService.updateMappingSet(this.mappingSet).takeUntil(this.unsubscribe).subscribe((mappingSet: any) => {
                    this.commonService.success('Success!','Value set successfully updated');
                    this.showValueSet(this.mappingSet.id);
                });
            } else {
                this.mappingSet.lookUppurpose = 'CHART_OF_ACCOUNT';
                this.mappingSet.enabledFlag = true;
                if (this.mappingSet.mappingSetValues.length > 0) {
                    for (let i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
                        this.mappingSet.mappingSetValues[i].status = true;
                    }
                }
                this.mappingSetService.postMappingSetsWithValues(this.mappingSet).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    /**Get Mapping Sets List by Purpose */
                    const obj = {
                        name: 'Recently Created',
                        data: [
                            {
                                id: res.id,
                                name: res.name,
                            }
                        ]
                    };
                    this.valueSetsList[this.segmentCurIndex].push(obj);
                    // this.getAllMappingSet();
                    this.commonService.success('Success!', res.name + ' Successfully Created');
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
                } else if (this.coaList[i].name == name) {
                    this.commonService.error('Warning!', 'Chart of Account name "' + name + '" alreay exist');
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
        const charCode = (e.which) ? e.which : e.keyCode;
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
                this.commonService.error('Warning!', 'Maximum Length for code is '+this.segmentCurLenth);
                this.mappingSet.mappingSetValues[i].sourceValue = undefined;
            }else if(value.length < this.segmentCurLenth){
                this.commonService.error('Warning!', 'Minimum Length for code is '+this.segmentCurLenth);
                this.mappingSet.mappingSetValues[i].sourceValue = undefined;
            } else {
                return true;
            }
        }
    }

    showValueSet(id) {
        this.router.navigate(['/value-set', { outlets: { 'content': id + '/details' } }]);
        /* this.mappingSetService.getMappingSetsWithValues(id).subscribe((res) => {
            this.mappingSet = res;
            this.mappingSet.startDate = this.dateUtils.convertDateTimeFromServer(this.mappingSet.startDate);
            this.mappingSet.endDate = this.dateUtils.convertDateTimeFromServer(this.mappingSet.endDate);
            if(this.mappingSet && this.mappingSet.mappingSetValues){
                this.mappingSet.mappingSetValues.forEach((mapVal) => {
                    mapVal.startDate = this.dateUtils.convertDateTimeFromServer(mapVal.startDate);
                    mapVal.endDate = this.dateUtils.convertDateTimeFromServer(mapVal.endDate);
                });
            }
            this.isValSetEdit = false;
            this.valueSetDialog = true;
        }); */
    }

    updateMapSetsVal(mappingSetValues, index) {
        if (this.checkCodeLength(mappingSetValues.sourceValue, index)) {
            if (mappingSetValues.sourceValue == '' || undefined || null ||
                mappingSetValues.targetValue == '' || undefined || null ||
                mappingSetValues.startDate == null) {
                this.commonService.error('Warning!', 'fill mandatory fields');
                this.mappingSet.mappingSetValues[index].columnEdit = true;
            } else {
                if (mappingSetValues.id == null || mappingSetValues.id == undefined || mappingSetValues.id == '') {
                    mappingSetValues.mappingSetId = this.mappingSet.id;
                    mappingSetValues.status = true;
                    this.mappingSetService.updateMappingSetValues(mappingSetValues).takeUntil(this.unsubscribe).subscribe((res: any) => {
                        this.commonService.success('Success!', 'New value successfully added');
                        this.showValueSet(this.mappingSet.id);
                    });
                } else {
                    this.mappingSetService.updateMappingSetValues(mappingSetValues).takeUntil(this.unsubscribe).subscribe((res: any) => {
                        this.commonService.success('Success!', 'Value successfully updated');
                        this.showValueSet(this.mappingSet.id);
                    });
                }
            }
        } else {
            this.mappingSet.mappingSetValues[index].columnEdit = true;
        }
    }

    getQualifier() {
        this.lookUpCodeService.getAllLookups('coa_qualifier').takeUntil(this.unsubscribe).subscribe((res) => {
            this.qualifier = res;
        });
    }

    curSegInd(i) {
        this.segmentCurLenth = this.chartOfAccount.segments[i].segmentLength;
    }

    qualifierChange(qualifier, i) {
        const count = this.chartOfAccount.segments.filter((segment) => segment.qualifier == qualifier).length;
        if (count > 1) {
            this.commonService.error('Warning!', `Duplicate qualifier is not allowed`);
            setTimeout(() => {
                this.chartOfAccount.segments[i].qualifier = null;
            }, 100);
        }
    }

    /* getValueSet(ind) {
        const segLen = this.chartOfAccount.segments[ind].segmentLength;
        const qualifier = this.chartOfAccount.segments[ind].qualifier;
        if (segLen) {
            this.chartOfAccountService.getMappingSetValuesBySegLengAndQualifier(segLen, qualifier).takeUntil(this.unsubscribe).subscribe((res) => {
                if (!this.valueSetsList[ind]) {
                    this.valueSetsList[ind] = [];
                }
                this.valueSetsList[ind] = res;
            });
        }
    } */

    deleteMapValue(mapValue) {
        this.mappingSetService.deleteMappingSetValue(mapValue.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.commonService.success('Success!', mapValue.sourceValue + ' Deleted');
            this.showValueSet(this.mappingSet.id);
        });
    }

    /**Check chart-of-account name duplicates */
    isNameExist(name, id) {
        if (name && !this.nameExist) {
            this.chartOfAccountService.checkCOAIsExist(name, id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
            });
        }
    }

    exportVS(id, type) {
        this.mappingSetService.getMappingSetsWithValues(id, true, type).takeUntil(this.unsubscribe).subscribe((res) => {
            this.commonService.exportData(res, type, 'ValueSets');
        }, () => {
            this.commonService.error('Warning!', 'Error Occured');
        });
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    openDialog(id): void {
        const dialogRef = this.dialog.open(BulkUploadDialog, {
            width: '300px',
            data: { form: 'MAPPING-SET', status: this.bulkUploadStatus, reason: this.bulkUploadReason },
            disableClose: true
        });

        //For Applying Bulk Upload Specific Styles
        $('body').addClass('bulk-upload');

        dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
            $('body').removeClass('bulk-upload');
            if (result) {
                if (result.status == 'Success') {
                    this.showValueSet(id);
                } else {
                    this.bulkUploadStatus = result.status;
                    this.bulkUploadReason = result.reason;
                    this.openDialog(id);
                }
            }
        });
    }

    private _getValueSet() {
        this.chartOfAccountService.getActiveVSByTenantIdAndPurpose('CHART_OF_ACCOUNT').takeUntil(this.unsubscribe).subscribe((res) => {
            this.valueSetsList = res;
        }, () => this.commonService.error('Warning!', 'Error occured while fetching Value Sets List'));
    }

    checkDuplicate(evt, val, ind) {
        const arr = this.chartOfAccount.segments;
        const count = arr.filter(
            (each) => each.segmentName && each.segmentName.toString().toLowerCase() == val && val.toString().toLowerCase()).length;
        if (count > 1) {
            this.commonService.error('Warning!', 'Duplicate Value');
                arr[ind].segmentName = null;
                evt.target.focus();
        }
    }
}
