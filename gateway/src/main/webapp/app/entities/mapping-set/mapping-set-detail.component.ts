import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { MappingSet } from './mapping-set.model';
import { MappingSetService } from './mapping-set.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-mapping-set-detail',
    templateUrl: './mapping-set-detail.component.html',
})
export class MappingSetDetailComponent implements OnInit, OnDestroy {

    mappingSet = new MappingSet();
    mappingValue = [];
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    dropdownSettings = {};
    purposeArray:any = [];
    selectedPurpose: any =[];
    paramsDetailId:any;
    hideSaveBtn = false;
    statuses = [{code: true,value: 'Active'},{code: false,value: 'Inactive'}];
    isValueSet = false;
    segLengthList = [{segLen:1},{segLen:2},{segLen:3},{segLen:4},{segLen:5}];
    segLenVald: any;
    private unsubscribe: Subject<void> = new Subject();
    currentDate = new Date();

    constructor(
        private router: Router,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private mappingSetService: MappingSetService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        // Fetch Mapping Set Detail Function
        this.fetchMappingSetDetail();

        // MultiSelect Setting for Purpose
        this.dropdownSettings = { 
            singleSelection: false, 
            text:"Purpose",
            selectAllText:'Select All',
            unSelectAllText:'UnSelect All',
            enableSearchFilter: true,
            classes:"",
            badgeShowLimit:1,
            disabled: false
          }; 

          // Calling LookupCodes Purpose Function
          this.lookupCode();
    }

    //Fetch Mapping Set Detail Function
    fetchMappingSetDetail(){
        this.isValueSet = this.router.url.includes('value-set');
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.paramsDetailId = params['id'];
            if (params['id']) {
                this.mappingSetService.getMappingSetsWithValues(params['id']).takeUntil(this.unsubscribe).subscribe(
                    (res) => {
                        this.mappingSet = res;
                        this.mappingValue = this.mappingSet.mappingSetValues;
                        this.mappingSet.segmentLength = this.mappingValue.length ? this.mappingValue[0].sourceValue.length : '';
                        if (this.isValueSet) {
                            this.segLenChange(this.mappingSet.segmentLength);
                        }
                        if(this.mappingSet.purpose != null || this.mappingSet.purpose != undefined){
                            this.selectedPurpose = [];
                            for (let i = 0; i < this.mappingSet.purpose.length; i++) {
                                const obj = { "id": this.mappingSet.purpose[i].lookupCode, "itemName":  this.mappingSet.purpose[i].lookupMeaning };
                                this.selectedPurpose.push(obj);
                            }
                        }
                        this.convertMappingDate();
                        this.dropdownSettings = {
                            disabled:true
                        }
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            // Focus on First Element
                            $('#mappingSetName').focus();
                            this.dropdownSettings = {
                                disabled:false
                            }
                        } else {
                            this.mappingSet.description = !this.mappingSet.description ? '-' : this.mappingSet.description;
                            this.mappingSet.endDate = !this.mappingSet.endDate ? '-' : this.mappingSet.endDate;
                            this.isViewOnly = true;
                        }
                     }); 
            } else {
                this.isCreateNew = true;
                // Focus on First Element
                $('#mappingSetName').focus();
                const obj = {
                    'sNo' : 1,
                    'sourceValue': '',
                    'targetValue': '',
                    'startDate': new Date(),
                    'endDate': null,
                    'columnEdit': true
                };
                this.mappingValue.push(obj);
                this.dropdownSettings = {
                    disabled:false
                }
                this.mappingSet.startDate = new Date();
            }
        });
    }

    //Converting Mapping Values Dates
    convertMappingDate(){
        // Converting Mapping Sets Dates
        if (this.mappingSet.startDate) {
            this.mappingSet.startDate = this.dateUtils.convertDateTimeFromServer(this.mappingSet.startDate);
            this.mappingSet.endDate = this.dateUtils.convertDateTimeFromServer(this.mappingSet.endDate);
        }

        // Converting Mapping Sets Values Dates
        if (this.mappingValue.length) {
            this.mappingValue.forEach((mapVal) => {
                mapVal.startDate = this.dateUtils.convertDateTimeFromServer(mapVal.startDate);
                mapVal.endDate = this.dateUtils.convertDateTimeFromServer(mapVal.endDate);
            });
        }
    }

    //Add New Row to Table - Mapping Value
    addRowMappingValue() {
        let count = 0;
        for (let i = 0; i < this.mappingValue.length; i++) {
            if (this.mappingValue[i].sourceValue == '' || this.mappingValue[i].targetValue == '' ||
                this.mappingValue[i].startDate == null) {
                count++;
            }
        }
        if (count > 0) {
            this.validationMsg();
        } else {
            const obj = {
                'sourceValue': '',
                'targetValue': '',
                'startDate': null,
                'endDate': null,
                'columnEdit': true,
                'isNew': true
            };
            this.mappingValue.push(obj);
        }
    }

    deleteMapValue(mapValue){
        this.mappingSetService.deleteMappingSetValue(mapValue.id).takeUntil(this.unsubscribe).subscribe(()=>{
            this.commonService.success('Success!',mapValue.sourceValue + ' Deleted');
            this.fetchMappingSetDetail();
        })
    }

    deleteRow(index){
        if(this.mappingValue.length < 2){
            this.commonService.error('Warning!','Atleast one row is mandatory');
        }else{
            this.mappingValue.splice(index, 1);
        }
    }

    // Function to display validition message
    validationMsg(disError?) {
        if (!disError) {
            this.commonService.error('Warning!','Fill Mandatory Fields');
        }
    }

    saveMappingSet() {
        this.hideSaveBtn = true;
        const parentPath = this.isValueSet ? '/value-set' : '/mapping-set';
        let link: any = '';
        if (!this.mappingSet.id) {
            // If New Mapping Set
            this.mappingSet.mappingSetValues = this.mappingValue;
            if (this.isValueSet) {
                this.mappingSet.purpose = null;
                this.mappingSet.lookUppurpose = 'CHART_OF_ACCOUNT';
            } else {
                this.convertMappingSetPurpose();
            }
            if (this.mappingSet.mappingSetValues.length > 0) {
                for (let i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
                    this.mappingSet.mappingSetValues[i].status = true;
                }
            }
            const mappingSetValid = this.mappingSet.mappingSetValues;
            let savMap;
            const regEx = new RegExp(`^\s*(?:[0-9]{${this.mappingSet.segmentLength}})\s*$`);
            for (let i = 0; i < mappingSetValid.length; i++) {
                if (!mappingSetValid[i].sourceValue ||
                    !mappingSetValid[i].targetValue ||
                    !mappingSetValid[i].startDate) {
                    this.validationMsg();
                    savMap = false;
                    this.hideSaveBtn = false;
                } else if (this.isValueSet && !regEx.test(mappingSetValid[i].sourceValue)) {
                    this.validationMsg(true);
                    savMap = false;
                    this.hideSaveBtn = false;
                } else {
                    savMap = true;
                }
            }
            if (savMap == true) {
                this.mappingSet.enabledFlag = true;
                this.mappingSetService.postMappingSetsWithValues(this.mappingSet).takeUntil(this.unsubscribe).subscribe((mappingSet: any) => {
                    this.hideSaveBtn = false;
                    this.commonService.success('Success!',`New ${this.isValueSet ? 'Value' : 'Mapping'} Set Successfully Created`);
                    link = [parentPath, { outlets: { 'content': mappingSet.id + '/details' } }];
                    if (this.isEdit) {
                        this.isEdit = false;
                    }
                    if (this.isCreateNew) {
                        this.isCreateNew = false;
                    }
                    this.isViewOnly = true;
                    this.router.navigate(link);
                });
            }
        } else {
            // Else Updated Mapping Set
            this.mappingSet.mappingSetValues = null;
            if (this.isValueSet) {
                this.mappingSet.purpose = 'CHART_OF_ACCOUNT';
            } else {
                this.convertMappingSetPurpose();
                this.mappingSet.purpose = this.mappingSet.lookUppurpose;
            }
            this.mappingSetService.updateMappingSet(this.mappingSet).takeUntil(this.unsubscribe).subscribe((mappingSet: any) => {
                this.hideSaveBtn = false;
                this.commonService.success('Success!',`${this.isValueSet ? 'Value' : 'Mapping'} Set Successfully Updated`);
                link = [parentPath, { outlets: { 'content': mappingSet.id + '/details' } }];
                if (this.isEdit) {
                    this.isEdit = false;
                }
                if (this.isCreateNew) {
                    this.isCreateNew = false;
                }
                this.isViewOnly = true;
                this.router.navigate(link);
            });
        }
    }

    //Convert Mapping Set Purpose
    convertMappingSetPurpose(){
        this.mappingSet.purpose = null;
        const tempArray = [];
        for (let i = 0; i < this.selectedPurpose.length; i++) {
            tempArray.push(this.selectedPurpose[i].id);
        }
        this.mappingSet.lookUppurpose = tempArray.toString();
    }

    //Update Mapping Set Values Function//
    updateMapSetsVal(mappingSetValues, index) {
        const regEx = new RegExp(`^\s*(?:[0-9]{${this.mappingSet.segmentLength}})\s*$`);
        if (!mappingSetValues.sourceValue || !mappingSetValues.targetValue || !mappingSetValues.startDate) {
                this.validationMsg();
                this.mappingValue[index].columnEdit = true;
        } else if (this.isValueSet && !regEx.test(mappingSetValues.sourceValue)) {
            this.validationMsg(true);
            this.mappingValue[index].columnEdit = true;
        } else {
            if (!mappingSetValues.id) {
                mappingSetValues.mappingSetId = this.mappingSet.id;
                mappingSetValues.status = true;
                this.mappingSetService.updateMappingSetValues(mappingSetValues).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.commonService.success('Success!',`New ${this.isValueSet ? 'Value' : 'Mapping Set Value'} Successfully Added`);
                    this.fetchMappingSetDetail();
                });
            } else {
                this.mappingSetService.updateMappingSetValues(mappingSetValues).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.commonService.success('Success!',`${this.isValueSet ? 'Value' : 'Mapping Set Value'} Successfully Updated`);
                    this.fetchMappingSetDetail();
                });
            }
        }
    }

    // Get LookupCodes For Purpose
    lookupCode() {
        this.mappingSetService.lookupCodes('MAPPING_TYPES').takeUntil(this.unsubscribe).subscribe((res: any) => {
            for (let i = 0; i < res.length; i++) {
                if (res[i].lookUpCode != 'CHART_OF_ACCOUNT') {
                    const obj = {
                        "id": res[i].lookUpCode,
                        "itemName": res[i].meaning
                    }
                    this.purposeArray.push(obj);
                }
            }
        });
    }

    // Cancel Changes Function
    cancelEdit(){
        this.fetchMappingSetDetail();
    }
    
    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    exportMappingSet(id, type) {
        this.mappingSetService.getMappingSetsWithValues(id, true, type).takeUntil(this.unsubscribe).subscribe((res) => {
            this.commonService.exportData(res, type, this.mappingSet.name);
        }, () => {
            this.commonService.error('Warning!', 'Error Occured');
        });
    }

    segLenChange(len) {
        this.segLenVald = `^\s*(?:[0-9]{${len}})\s*$`;
    }

    checkDuplicate(evt, val, ind, key, pattern?) {
        const count = this.mappingValue.filter(
            (each) => each[key].toString().toLowerCase() == val.toString().toLowerCase()).length;
        if (count > 1) {
            this.commonService.error('Warning!', 'Duplicate Value');
                this.mappingValue[ind][key] = null;
                evt.target.focus();
        } else if (pattern) {
            evt.target.focus();
        }
    }
}
