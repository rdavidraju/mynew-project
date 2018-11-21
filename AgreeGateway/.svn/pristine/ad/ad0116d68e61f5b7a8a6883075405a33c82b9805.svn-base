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
import { MappingSetBreadCrumbTitles } from './mapping-set.model';
import { MappingSet } from './mapping-set.model';
import { MappingSetService } from './mapping-set.service';
//import { MappingSetValuesService } from '../mapping-set-values/mapping-set-values.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-mapping-set-detail',
    templateUrl: './mapping-set-detail.component.html'
})
export class MappingSetDetailComponent implements OnInit, OnDestroy {

    mappingSet = new MappingSet();
    mappingValue = [];
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    // purposeList = [];
    dropdownSettings = {};
    purposeArray:any = [];
    selectedPurpose: any =[];
    paramsDetailId:any;
    hideSaveBtn:boolean = false;
    isVisibleA: boolean = false;
    constructor(
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
        private eventManager: JhiEventManager,
        private mappingSetService: MappingSetService,
        //private mappingSetValuesService: MappingSetValuesService,
        private route: ActivatedRoute
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
        /* this.subscription = this.route.params.subscribe((params) => {
            this.load(params['login']);
        }); */
        
        //Fetch Mapping Set Detail Function//
        this.fetchMappingSetDetail();

        //MultiSelect Setting for Purpose
        this.dropdownSettings = { 
            singleSelection: false, 
            text:"Purpose",
            selectAllText:'Select All',
            unSelectAllText:'UnSelect All',
            enableSearchFilter: true,
            classes:"",
            badgeShowLimit:2,
            disabled: false
          }; 

          //Calling LookupCodes Purpose Function
          this.lookupCode();
    }

    //If Start Date Entered Apply Class
    startEndDateClass(){
        if(this.mappingSet.startDate != null){
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }else{
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }

    //Fetch Mapping Set Detail Function//
    fetchMappingSetDetail(){
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            
            this.paramsDetailId = params['id'];
            if (this.presentPath == "mapping-set") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                this.mappingSetService.getMappingSetsWithValues(params['id']).subscribe(
                    (res) => {
                        this.mappingSet = res;
                        this.mappingValue = this.mappingSet.mappingSetValues;
                        if(this.mappingSet.purpose != null || undefined){
                            this.selectedPurpose = [];
                            for (var i = 0; i < this.mappingSet.purpose.length; i++) {
                                let obj =
                                    { "id": this.mappingSet.purpose[i].lookupCode, "itemName":  this.mappingSet.purpose[i].lookupMeaning }
                                    this.selectedPurpose.push(obj);
                            }
                        }
                        this.convertMappingDate();
                        // console.log('this.mappingSet\n' + JSON.stringify(this.mappingSet));
                        this.dropdownSettings = {
                            disabled:true
                        }
                        this.isCreateNew = false;
                        if (this.mappingValue != null || undefined) {
                            for (var i = 0; i < this.mappingValue.length; i++) {
                                this.mappingValue[i]['sNo'] = i + 1;
                            }
                        }
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            //Focus on First Element
                            $('#mappingSetName').focus();
                            this.dropdownSettings = {
                                disabled:false
                            }
                        } else {
                            this.isViewOnly = true;
                        }
                     }); 
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                //Focus on First Element
                $('#mappingSetName').focus();
                let obj = {
                    'sNo' : 1,
                    'sourceValue': '',
                    'targetValue': '',
                    'startDate': null,
                    'endDate': null
                };
                this.mappingValue.push(obj);
                this.dropdownSettings = {
                    disabled:false
                }
            }
        });
    }

    //Converting Mapping Values Dates//
    convertMappingDate(){
        //Converting Mapping Sets Dates
        if(this.mappingSet.startDate != null || undefined || this.mappingSet.startDate != null || undefined){
            this.mappingSet.startDate = this.dateUtils.convertLocalDateFromServer(this.mappingSet.startDate);
            this.mappingSet.endDate = this.dateUtils.convertLocalDateFromServer(this.mappingSet.endDate);
        }

        //Converting Mapping Sets Values Dates
        if(this.mappingValue != null || undefined){
            this.mappingValue.forEach(mapVal => {
                mapVal.startDate = this.dateUtils.convertLocalDateFromServer(mapVal.startDate);
                mapVal.endDate = this.dateUtils.convertLocalDateFromServer(mapVal.endDate);
            })
        }
    }

    //Add New Row to Table - Mapping Value
    addRowMappingValue() {
        let count = 0;
        for (var i = 0; i < this.mappingValue.length; i++) {
            if (this.mappingValue[i].sourceValue == '' || this.mappingValue[i].targetValue == '' ||
                this.mappingValue[i].startDate == null) {
                count++;
            }
        }
        if (count > 0) {
            this.validationMsg();
        } else {
            let obj = {
                // 'sNo': i+1,
                'sourceValue': '',
                'targetValue': '',
                'startDate': null,
                'endDate': null,
                'columnEdit': true
            };
            this.mappingValue.push(obj);
        }
    }

    deleteMapValue(mapValue){
        this.mappingSetService.deleteMappingSetValue(mapValue.id).subscribe((res: any)=>{
            this.notificationsService.success(
                'Success!',
                mapValue.sourceValue + ' Deleted'
            );
            this.fetchMappingSetDetail();
        })
    }

    deleteRow(index){
        if(this.mappingValue.length < 2){
            this.notificationsService.error('Warning!','Atleast one row is mandatory');
        }else{
            this.mappingValue.splice(index, 1);
        }
    }

    /* Function to display validition message */
    validationMsg() {
        this.notificationsService.error(
            'Warning!',
            'Fill Mandatory Fields'
        )
    }



    onPurposeSelect(item:any){
        // console.log(item);
        // console.log('On Select - this.mappingSet.purpose: '+ JSON.stringify(this.mappingSet.purpose));
    }
    onPurposeDeSelect(item:any){
        // console.log(item);
        // console.log('On DeSelect - this.mappingSet.purpose: '+ JSON.stringify(this.mappingSet.purpose));
    }
    onPurposeSelectAll(items: any){
        // console.log(items);
        // console.log('On SelectAll - this.mappingSet.purpose: '+ JSON.stringify(this.mappingSet.purpose));
    }
    onPurposeDeSelectAll(items: any){
        // console.log(items);
        // console.log('On DeSelectAll - this.mappingSet.purpose: '+ JSON.stringify(this.mappingSet.purpose));
    }

    saveMappingSet() {
        // console.log(JSON.stringify(this.mappingSet.startDate));
        // console.log(JSON.stringify(this.mappingSet.endDate));
        this.hideSaveBtn = true;
        let link: any = '';
        if (this.mappingSet.id == undefined || null) {
            //IF New Mapping Set
            this.mappingSet.mappingSetValues = this.mappingValue;
            this.convertMappingSetPurpose();
            if (this.mappingSet.mappingSetValues.length > 0) {
                for (var i = 0; i < this.mappingSet.mappingSetValues.length; i++) {
                    this.mappingSet.mappingSetValues[i].status = true;
                }
            }
            let mappingSetValid = this.mappingSet.mappingSetValues;
            for (var i = 0; i < mappingSetValid.length; i++) {
                if (mappingSetValid[i].sourceValue == '' ||
                    mappingSetValid[i].targetValue == '' ||
                    mappingSetValid[i].startDate == null) {
                    this.validationMsg();
                    var savMap = false;
                    this.hideSaveBtn = false;
                } else {
                    savMap = true;
                }
            }
            if (savMap == true) {
                this.mappingSet.enabledFlag = true;
                this.mappingSetService.postMappingSetsWithValues(this.mappingSet).subscribe((mappingSet: any) => {
                    this.hideSaveBtn = false;
                    this.notificationsService.success(
                        'Success!',
                        'New Mapping Set Successfully Created'
                    );

                    link = ['/mapping-set', { outlets: { 'content': mappingSet.id + '/details' } }];
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
            //Else Updated Mapping Set
            this.mappingSet.mappingSetValues = null;
            this.convertMappingSetPurpose();
            this.mappingSet.purpose = this.mappingSet.lookUppurpose;
            // console.log(JSON.stringify(this.mappingSet));
            this.mappingSetService.updateMappingSet(this.mappingSet).subscribe((mappingSet: any) => {
                this.hideSaveBtn = false;
                this.notificationsService.success(
                    'Success!',
                    'Mapping Set Successfully Updated'
                );
                link = ['/mapping-set', { outlets: { 'content': mappingSet.id + '/details' } }];
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
        let tempArray = [];
        for (var i = 0; i < this.selectedPurpose.length; i++) {
            tempArray.push(this.selectedPurpose[i].id);
        }
        this.mappingSet.lookUppurpose = tempArray.toString();
    }

    //Update Mapping Set Values Function//
    updateMapSetsVal(mappingSetValues, index) {
        if (mappingSetValues.sourceValue == '' || undefined || null ||
            mappingSetValues.targetValue == '' || undefined || null ||
            mappingSetValues.startDate == null) {
                this.validationMsg();
                this.mappingValue[index].columnEdit = true;

        } else {
            if (mappingSetValues.id == null || mappingSetValues.id == undefined || mappingSetValues.id == '') {
                mappingSetValues.mappingSetId = this.mappingSet.id;
                mappingSetValues.status = true;
                this.mappingSetService.updateMappingSetValues(mappingSetValues).subscribe((res: any) => {
                    this.notificationsService.success(
                        'Success!',
                        'New Mapping Set Values Successfully Added'
                    );
                    this.fetchMappingSetDetail();
                });
            } else {
                console.log('mappingSetValues', JSON.stringify(mappingSetValues));
                this.mappingSetService.updateMappingSetValues(mappingSetValues).subscribe((res: any) => {
                    this.notificationsService.success(
                        'Success!',
                        'Mapping Set Values Successfully Updated'
                    );
                    this.fetchMappingSetDetail();
                });
            }
        }
    }

    //Get LookupCodes For Purpose//
    lookupCode() {
        this.mappingSetService.lookupCodes('MAPPING_TYPES').subscribe((res: any) => {
            // this.purposeArray = res;
            for (var i = 0; i < res.length; i++) {
                if (res[i].lookUpCode != 'CHART_OF_ACCOUNT') {
                    let obj = {
                        "id": res[i].lookUpCode,
                        "itemName": res[i].meaning
                    }
                    this.purposeArray.push(obj);
                }
            }
        });
    }

    //Cancel Changes Function//
    cancelEdit(){
        this.fetchMappingSetDetail();
    }
    
    ngOnDestroy() {
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
}

//     ngOnInit() {
//         this.subscription = this.route.params.subscribe((params) => {
//             this.load(params['id']);
//         });
//         this.registerChangeInMappingSets();
//     }

//     load(id) {
//         this.mappingSetService.find(id).subscribe((mappingSet) => {
//             this.mappingSet = mappingSet;
//         });
//     }
//     previousState() {
//         window.history.back();
//     }

//     ngOnDestroy() {
//         this.subscription.unsubscribe();
//         this.eventManager.destroy(this.eventSubscriber);
//     }

//     registerChangeInMappingSets() {
//         this.eventSubscriber = this.eventManager.subscribe(
//             'mappingSetListModification',
//             (response) => this.load(this.mappingSet.id)
//         );
//     }
// }
