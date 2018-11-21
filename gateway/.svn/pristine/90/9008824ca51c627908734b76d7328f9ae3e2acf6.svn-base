/*
  Module Name: Journal Template Builder
  Author: AMIT

    1. FUNCTION 1 : Side bar toggler functionality
    2. FUNCTION 2 - Fetch route path
    3. FUNCTION 3 - To save journal template
    4. FUNCTION 4 - While defining journal batch name onselection of checkbox
    5. FUNCTION 5 - To define batch name
    6. FUNCTION 6 - To fetch view columns based on view id
    7. FUNCTION 7 - To push data in header level selection on drag and drop
    8. FUNCTION 8 - To delete header seleted column
*/

import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { FormGroup, FormControl, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { SelectItem, MultiSelectModule, CheckboxModule } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { TemplateDefinition, CustomFunction } from './template-details.model';
import { TemplateDetailsService } from './template-details.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../common.service';
import { DataViewsService } from '../data-views/data-views.service';
import { RuleGroupService } from '../rule-group/rule-group.service';
import { routerAnimation, displayAnimation } from '../../shared/animations/routerAnimation';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-template-definition-detail',
    templateUrl: './template-definition-detail.component.html',
    animations: [routerAnimation, displayAnimation]
})

export class TemplateDefinitionDetailComponent implements OnInit, OnDestroy {

    journalTemplate = new TemplateDefinition();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    hideSaveButton: boolean;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA = false;
    dataViews: any = []; //List of data views
    journalGenerationLevelList: any = []; //List of je generation level
    commonHeaderAttribute: any = []; //List for common header attributes
    commonLineAttribute: any = []; //List for common header attributes
    headerDateLov: any = []; //List for headerdate lov
    selectedHeaderAttributes: any = []; // List of select header level 
    selectedViewColumnsAtLines: any = []; //List of columns
    selectedLineAttributes: any = []; // List of selected columns at line level
    defineJournalBatchNameList: any = []; // List to define journal batch name
    conversionTypeList: any = []; //List of conversion type from lookup
    sourceList: any = []; // List of source
    categoryList: any = []; // List of category
    calenderList: any = []; // List of calender
    lineDerivationRuleList: any = [];
    ledgersList: any = [];
    selectedValues: string[] = [];
    constantValue: any;
    constantInputField = false;
    dummyBatchName: any = []; //using for tempory batchname
    toleranceValue: any;
    toleranceSelectedValue: any;
    conversionTypeValue: any;
    journalTemplateDetails: any = [];
    selectedViewLabel: any;
    selectedRuleGrpLabel: any;
    selectedCOALabel: any;
    ruleGroupList: any = [];
    toleranceTempVal: any;
    functionDefinitionDialog = false;
    startDateChange = false;
    endDateChange = false;
    approvalProcessName: any;
    approvalInitiation = false;
    duplicateJournalName = false;
    jeTempName: any;
    dataViewsBasedOnRuleId: any = [];
    coaList: any = [];
    naturalAccountList: any = [];
    interCompanyList: any = [];
    selectedroundOffCard: any = [];
    selectedicSetOffDebit: any = [];
    selectedconversionTypeValue: any = [];
    displayJournalBatchName: any;
    displaySelectedLineAttributes: any = [];
    tempBatchName = new Array();
    today: any;
    jeHeaderDerivations: any = [];
    jeLineDerivations: any = [];
    accountValue: any = [];
    icSetOffDebitValue: any = [];
    icSetOffCreditValue: any = [];
    selectedHeaderAttributesDetails: any = [];
    dvDateColumns: any[] = [];
    myPopover: any;
    roundOffCard = false;
    icSetOffCard = false;
    multiCurrencyCard = false;
    customFunction = new CustomFunction();
    request: any;
    ruleGrpIdSearch: any;
    viewListSearch: any;
    coaIdSearch: any;
    submitted = false;
    loadDocument = false;
    toleranceLov: any = [
        {
            "name": "Amount",
            "value": "AMOUNT"
        },
        {
            "name": "Percentage",
            "value": "PERCENTAGE"
        }
    ];
    limitSelectionSettings = {
        text: "Select Account",
        singleSelection: true,
        enableSearchFilter: true,
        classes: "myclass custom-class",
        badgeShowLimit: 1,
        disabled: false,
        showCheckbox: false,
        closeOnSelect: true,
        selectionLimit: 1,
    };
    balanceTypeLov: any = [
        {
            "meaning": 'Company',
            "lookUpCode": 'COMPANY'
        }, {
            "meaning": 'Department',
            "lookUpCode": 'DEPARTMENT'
        }, {
            "meaning": 'Currency',
            "lookUpCode": 'CURRENCY'
        }, {
            "meaning": 'Location',
            "lookUpCode": 'LOCATION'
        }
    ];
    constructor(
        private eventManager: JhiEventManager,
        private templateDetailsService: TemplateDetailsService,
        private ruleGroupService: RuleGroupService,
        private route: ActivatedRoute,
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
        private dataViewsService: DataViewsService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    //If Start Date Entered Apply Class
    startEndDateClass() {
        if (this.journalTemplate.startDate != null) {
            return 'col-md-2 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }

    changeMinDate() {
        this.config.minDate = this.journalTemplate.startDate;
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

        this.request = this.ruleGroupService.fetchUnTaggedRuleGroups('JOURNAL_APPROVALS').subscribe((res: any) => {
            this.approvalProcessName = res.json();
            ////console.log('this.approvalProcessName ' + JSON.stringify(this.approvalProcessName));
        });
        this.request = this.templateDetailsService.getAccountingRulesGrpList('ACCOUNTING').subscribe((res: any) => {
            this.ruleGroupList = res;
        });
        /* Fetching List of dataview */
        this.request = this.dataViewsService.dataViewList().subscribe((res: any) => {
            res.forEach((element) => {
                if (element.enabledFlag == true) {
                    this.dataViews.push(element);
                }
            });
            //  ////console.log('dataViews ' + JSON.stringify(this.dataViews));
        });

        /* Fetching List of JOURNAL_GENERATION_LEVEL */
        this.request = this.templateDetailsService.getLookUps('JOURNAL_GENERATION_LEVEL').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.lookUpCode;
            });
            this.journalGenerationLevelList = res;
        });

        /* Fetching List for common attribute for header level */
        this.request = this.templateDetailsService.getLookUps('ACC_HEADER_ATTRIBUTE').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.lookUpCode;
                if (element.lookUpCode == 'DATE' || element.lookUpCode == 'SOURCE' || element.lookUpCode == 'CATEGORY') {
                    element['mappingType'] = 'LOOKUP_CODE';
                } else if (element.lookUpCode == 'LEDGER') {
                    element['mappingType'] = 'LEDGER_DEFINITION';
                } else if (element.lookUpCode == 'PERIOD') {
                    element['mappingType'] = 'CALENDAR';
                }
            });
            this.commonHeaderAttribute = res;
        });

        /* Fetching List for common attribute for line level */
        this.request = this.templateDetailsService.getLookUps('ACC_LINE_ATTRIBUTE').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
            });
            this.commonLineAttribute = res;
        });

        /* Fetching List for common attribute for line level */
        this.request = this.templateDetailsService.getLookUps('GL_DATES_DERIVATION').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.lookUpCode;
                element['ruleLevel'] = 'Header';
            });
            this.headerDateLov = res;
            /* ////console.log('this.headerDateLov ' + JSON.stringify(this.headerDateLov)); */
        });

        /* Fetching List for ledgers List Based on tenantid*/
        this.request = this.templateDetailsService.getLedgersList().subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.name;
                element['attributeName'] = element.name;
                element['ruleLevel'] = 'Header';
            });
            this.ledgersList = res;
            //   //console.log('this.ledgersList ' + JSON.stringify(this.ledgersList));
        });

        /* Fetching List of JOURNAL_BATCH_NAME */
        this.request = this.templateDetailsService.getLookUps('JOURNAL_BATCH_NAME').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.lookUpCode;
                element['ruleLevel'] = 'Header';
            });
            this.defineJournalBatchNameList = res;
        });

        /* Fetching List of SOURCE */
        this.request = this.templateDetailsService.getLookUps('SOURCE').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.lookUpCode;
                element['ruleLevel'] = 'Header';
            });
            this.sourceList = res;
        });

        /* Fetching List of CATEGORY */
        this.request = this.templateDetailsService.getLookUps('CATEGORY').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.lookUpCode;
                element['ruleLevel'] = 'Header';
            });
            this.categoryList = res;
        });

        /* Fetching List of CONVERSION_TYPE */
        this.request = this.templateDetailsService.getFxRatesList().subscribe((res: any) => {
            res.forEach((element) => {
                if (element.enabledFlag == true) {
                    element['aliasName'] = element.name;
                    element['attributeName'] = element.name;
                    this.conversionTypeList.push(element);
                }
            });
            /* //console.log('this.conversionTypeList ' + JSON.stringify(this.conversionTypeList)); */
        });/* getFxRatesList */

        /* Fetching List of calender */
        this.request = this.templateDetailsService.getCalenderList().subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.name;
                element['attributeName'] = element.name;
                element['ruleLevel'] = 'Header';
            });
            this.calenderList = res;
            /* //console.log('this.calenderList ' + JSON.stringify(this.calenderList)); */
        });
        /* Fetching List of LINE_DERIVATION_RULE */
        this.request = this.templateDetailsService.getLookUps('LINE_DERIVATION_RULE').subscribe((res: any) => {
            res.forEach((element) => {
                element['aliasName'] = element.meaning;
                element['attributeName'] = element.meaning;
            });
            this.lineDerivationRuleList = res;
            /* //console.log('lineDerivationRuleList' + JSON.stringify(this.lineDerivationRuleList)); */
        });

        this.fetchJournalsDetails();
    }


    customTabFun(e) {
        if (e.value == 'AMOUNT') {
            this.toleranceSelectedValue = 'AMOUNT';
        } else {
            this.toleranceSelectedValue = 'PERCENTAGE';
        }
    }
    /* Function to fetch dataview list based on rulegroupid */
    fetchDVBasedOnRuleGroup(rGrpId) {
        this.dataViewsBasedOnRuleId = [];
        this.selectedRuleGrpLabel = "Selected Accounting Process";
        this.selectedCOALabel = "Selected COA";
        this.request = this.templateDetailsService.fetchingDataViewList(rGrpId).subscribe((res: any) => {
            this.dataViewsBasedOnRuleId = res;
        });
        this.request = this.templateDetailsService.fetchingCOAList(rGrpId).subscribe((res: any) => {
            this.coaList = []
            this.coaList.push(res);
            this.journalTemplate.coaId = res.coaId;
            /* //console.log('journalTemplate.coaId' + JSON.stringify(res));
            //console.log('journalTemplate.coaId' + this.journalTemplate.coaId); */
            this.templateDetailsService.getCOAandSegments(this.journalTemplate.coaId, 'NATURAL_ACCOUNT').subscribe((res1: any) => {
                this.interCompanyList = res1;
                //console.log('this.interCompanyList ' + JSON.stringify(this.interCompanyList));
                //this.naturalAccountList = res;
                if (res1.segments[0]) {
                    res1.segments[0].valueSet.forEach((element, i) => {
                        const obj = {
                            "id": element[0],
                            "itemName": element[1],
                            "value": element[0]
                        }
                        this.naturalAccountList.push(obj);
                    });
                    //console.log('this.naturalAccountList ' + JSON.stringify(this.naturalAccountList));
                }
                /* let obj = {
                    "id": count++,
                    "itemName": this.fileTemplateList[i].templateName,
                    "dataName": this.fileTemplateList[i].templateName,
                    "type": "File Template",
                    "typeId": this.fileTemplateList[i].id,
                    "intermediateId": this.fileTemplateList[i].intermediateId
                }
                this.dropdownList.push(obj); */
            });
            /* this.templateDetailsService.getCOAandSegments(this.journalTemplate.coaId,'INTER_COMPANY').subscribe((res: any) => {
                this.interCompanyList = res;
            }); */
        });
    }

    /* FUNCTION 1 - Side bar toggler functionality 
       Author: AMIT 
    */
    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
        } else {
            this.isVisibleA = false;
        }
    }

    /* FUNCTION 2 - Fetch route path
       Author: AMIT 
    */
    fetchJournalsDetails() {
        this.subscription = this.route.params.subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "template-details") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                /* $('vertical-split-separator').css('display','none !important');
                $('.left-component').css('width','0% !important'); */
                this.request = this.templateDetailsService.getJournalDefinitionDetailsById(params['id']).subscribe(
                    (res) => {
                        this.selectedViewLabel = "Selected Source";
                        this.selectedRuleGrpLabel = "Selected Accounting Process";
                        ////console.log('this.dataViews ' + JSON.stringify(this.dataViews));
                        /* //console.log('journal Definition Detail ::' + JSON.stringify(res)); */
                        this.journalTemplate = res;
                        this.loadDocument = true;
                        //console.log('saveJournalTemplate ' + JSON.stringify(res));
                        this.journalTemplate.startDate = this.dateUtils.convertDateTimeFromServer(this.journalTemplate.startDate);
                        this.journalTemplate.endDate = this.dateUtils.convertDateTimeFromServer(this.journalTemplate.endDate);
                       // console.log('saveJournalTemplate ' + JSON.stringify(this.journalTemplate));
                        if (this.journalTemplate.apprRuleGrpId) {
                            this.approvalInitiation = true;
                        }
                        // this.journalTemplate.viewId = Number(this.journalTemplate.viewId);
                        
                        this.fetchDVBasedOnRuleGroup(this.journalTemplate.ruleGrpId);
                        this.journalTemplate.jeHeaderDerivations.forEach((element) => {
                            if (element.attributeName == 'JOURNAL_BATCH_NAME') {
                                const tempName = [];
                                tempName.push(element.value);
                                this.journalTemplate.jeBatchName = tempName;
                                //console.log('this.journalTemplate.jeBatchName ' + this.journalTemplate.jeBatchName);
                                let temp;
                                temp = element.value.replace(/[{}]/g, "");
                                this.tempBatchName = temp.split(", ");
                                //this.selectedValues = temp.split(",");
                                this.tempBatchName.forEach((element1) => {
                                    this.selectedValues.push(element1);
                                });
                                this.displayJournalBatchName = element.value;
                                ////console.log('defineJournalBatchNameList ' + JSON.stringify(this.defineJournalBatchNameList));
                                //console.log('selectedValues ' + this.selectedValues);
                            } else if (element.attributeName == 'DATE' || element.attributeName == 'SOURCE' || element.attributeName == 'CATEGORY' || element.attributeName == 'LEDGER' || element.attributeName == 'PERIOD') {
                                this.selectedHeaderAttributesDetails.push(element);
                                this.selectedHeaderAttributes.push(element);
                            } else if (element.attributeName == 'ROUNDING_SETOFF_ACCOUNT' && element.value != null) {
                                /* let obj = {
                                    "id": element[0],
                                    "itemName": element[1],
                                    "value": element[0]
                                }
                                this.naturalAccountList.push(obj); */
                                /* this.interCompanyList = res;
                //this.naturalAccountList = res;
                if(res.segments[0]){
                    res.segments[0].valueSet.forEach((element,i) => {
                        let obj = {
                            "id": element[0],
                            "itemName": element[1],
                            "value": element[0]
                        }
                        this.naturalAccountList.push(obj);
                    });
                } */
                                this.accountValue = [{
                                    "id": element.value,
                                    "itemName": element.valueName,
                                    "value": element.value
                                }];
                                this.selectedroundOffCard = ['ROUNDING_SETOFF_ACCOUNT'];
                                this.roundOffCard = true;
                                //console.log('naturalAccountList ' + JSON.stringify(this.naturalAccountList));
                                //console.log('accountValue ' + this.accountValue);
                                //console.log('this.interCompanyList ' + JSON.stringify(this.interCompanyList));

                            } else if (element.attributeName == 'ROUNDING_SETOFF_TOLERANCE' && element.value != null) {
                                this.toleranceTempVal = element.aliasName;
                                this.toleranceSelectedValue = element.aliasName;
                                this.toleranceValue = element.value;
                            } else if (element.attributeName == 'MULTI_COMPANY_ACCOUNTS' && element.aliasName == 'IC Set Off Debit' && element.value != null) {

                                this.icSetOffDebitValue = [{
                                    "id": element.value,
                                    "itemName": element.valueName,
                                    "value": element.value
                                }];
                                this.selectedicSetOffDebit = ['MULTI_COMPANY_ACCOUNTS'];
                                this.icSetOffCard = true;
                            } else if (element.attributeName == 'MULTI_COMPANY_ACCOUNTS' && element.aliasName == 'IC Set Off Credit' && element.value != null) {
                                this.icSetOffCreditValue = [{
                                    "id": element.value,
                                    "itemName": element.valueName,
                                    "value": element.value
                                }];
                            } else if (element.attributeName == 'CONVERSION_TYPE' && element.value != null) {
                                this.conversionTypeValue = parseInt('', element.value);/* Number(element.value); */
                                this.selectedconversionTypeValue = ['Multi Currency'];
                                this.multiCurrencyCard = true;
                            } else if (element.attributeName == 'BALANCE_TYPE') {
                                this.journalTemplate.balanceTypeDisplayName = element.aliasName;
                                const temp = [];
                                temp.push(element.value);
                                this.journalTemplate.balanceType = temp;
                                //console.log('this.journalTemplate.balanceType ' + JSON.stringify(this.journalTemplate.balanceType));
                            } else if (element.attributeName == 'JE_LEVEL') {
                                this.journalTemplate.jeLevel = element.valueName;
                            }
                        });
                        this.displaySelectedLineAttributes = this.journalTemplate.jeLineDerivations;
                        this.selectedLineAttributes = this.journalTemplate.jeLineDerivations;
                        //console.log('displaySelectedLineAttributes ' + JSON.stringify(this.displaySelectedLineAttributes));
                        const tempAry = [];
                        tempAry.push(this.journalTemplate.viewId);
                        this.templateDetailsService.getViewsColumnsData(tempAry)
                            .subscribe((res1: any) => {
                                res1[0].forEach((element) => {
                                    element['aliasName'] = element.lookUpCode;
                                });
                                this.selectedViewColumnsAtLines = res1[0];
                                /* //console.log('this.selectedViewColumnsAtLines ' + JSON.stringify(this.selectedViewColumnsAtLines)); */
                            });
                    });
                this.isCreateNew = false;
                if (url.endsWith('edit')) {
                    this.isEdit = true;
                    this.loadDocument = true;
                    //console.log('edit');
                } else {
                    //console.log('viewOnly');
                    this.isViewOnly = true;
                    setTimeout(function(){ 
                        $('.hideSidePanel').addClass('ifJEDetailsPage');
                    }, 0);
                }

            } else {
                this.today = new Date();
                this.toleranceTempVal = "AMOUNT";
                const stDate = new Date();
                this.journalTemplate.startDate = stDate;
                this.isCreateNew = true;
                this.loadDocument = true;
                this.toleranceSelectedValue = 'AMOUNT';
                this.journalTemplate.balanceTypeDisplayName = 'Dr = Cr';
                this.journalTemplate.jeLevel = 'DETAIL';
                this.selectedViewLabel = "Select Source";
                this.selectedRuleGrpLabel = "Select Accounting Process";
                this.selectedCOALabel = "Select COA";

            }
        });
    }

    /* FUNCTION 3 - To save journal template
       Author: AMIT 
    */
    
    saveJournalTemplate() {
        let periodVal = false;
        let dateVal = false;
        //console.log('this.selectedHeaderAttributes ' + JSON.stringify(this.selectedHeaderAttributes));
        this.selectedHeaderAttributes.forEach((element) => {
            if ((element.lookUpCode || element.attributeName) == 'PERIOD') {
                periodVal = true;
            } else if ((element.lookUpCode || element.attributeName) == 'DATE') {
                dateVal = true;
                if (element.value == "CUSTOM_FUNCTION") {
                    element.mappingType = 'CUSTOM_FUNCTION';
                    for (let i = 0; i < this.headerDateLov.length; i++) {
                        if (this.headerDateLov[i].lookUpCode == 'CUSTOM_FUNCTION') {
                            element.value = this.headerDateLov[i].meaning + '({' + this.customFunction.argument1 + '},{' + this.customFunction.argument2 + '},' + this.customFunction.offSetDays + ');'
                        }
                    }
                }
            }
        });
        if (periodVal && dateVal) {
            this.hideSaveButton = true;
            //console.log('selectedHeaderAttributes ' + JSON.stringify(this.selectedHeaderAttributes));
            //  //console.log('defaultLineAttributesList ' + JSON.stringify(this.defaultLineAttributesList));
            /* //console.log('lineDerivationRuleList ' + JSON.stringify(this.lineDerivationRuleList));
            //console.log('selectedLineAttributes ' + JSON.stringify(this.selectedLineAttributes)); */
            if (this.isEdit) {
                this.selectedHeaderAttributes.forEach((element) => {
                    const obj1 = {
                        "attributeName": element.attributeName,
                        "aliasName": element.aliasName,
                        "value": element.value,
                        "ruleLevel": 'Header',
                        "mappingType": element.mappingType
                    }
                    this.jeHeaderDerivations.push(obj1);
                });
            } else {
                this.selectedHeaderAttributes.forEach((element) => {
                    const obj3 = {
                        "attributeName": element.lookUpCode,
                        "aliasName": element.aliasName,
                        "value": element.value,
                        "ruleLevel": 'Header',
                        "mappingType": element.mappingType
                    }
                    this.jeHeaderDerivations.push(obj3);
                });
            }
            //console.log('this.lineDerivationRuleList ' + JSON.stringify(this.lineDerivationRuleList));
            //console.log('this.selectedLineAttributes ' + JSON.stringify(this.selectedLineAttributes));
            //selectedLineAttributes
            /*  this.lineDerivationRuleList.forEach(element => {
                 if(element.colType == 'SYSTEM'){
                     let obj = {
                         "colType": "SYSTEM",
                         "colValue": element.id
                     }
                     this.jeLineDerivations.push(obj);
                 }else{
                     let obj = {
                         "colType": "LINE",
                         "colValue": element.id
                     }
                     this.jeLineDerivations.push(obj);
                 }
                 //console.log('this.jeLineDerivations! ' + JSON.stringify(this.jeLineDerivations));
             }); */
            let count = 1;
            //console.log('this.isEdit ' + this.isEdit);
            if (this.isEdit) {
                this.selectedLineAttributes.forEach((element) => {

                    if (element.colValueName == 'Line' || element.colValueName == 'Code Combination' || element.colValueName == 'Accounted Debit' || element.colValueName == 'Accounted Credit') {
                        const obj4 = {
                            "colType": "SYSTEM",
                            "colValue": element.colValue
                        }
                        this.jeLineDerivations.push(obj4);
                    } else {
                        if (element.colType == 'LINE') {
                            const obj5 = {
                                "colType": "LINE",
                                "colValue": element.colValue,
                                "refColumn": count++
                            }
                            this.jeLineDerivations.push(obj5);
                        } else if (element.dataViewId) {
                            const obj7 = {
                                "colType": "LINE",
                                "colValue": element.id,
                                "refColumn": count++
                            }
                            this.jeLineDerivations.push(obj7);
                        } else if (element.lookUpCode) {
                            const obj8 = {
                                "colType": "SYSTEM",
                                "colValue": element.id,
                                "refColumn": count++
                            }
                            this.jeLineDerivations.push(obj8);
                        } else {
                            const obj9 = {
                                "colType": "SYSTEM",
                                "colValue": element.colValue,
                                "refColumn": count++
                            }
                            this.jeLineDerivations.push(obj9);
                        }

                    }
                    if(this.journalTemplate.enable){
                        this.journalTemplate['enabledFlag'] = true;
                    }else{
                        this.journalTemplate['enabledFlag'] = false;
                    }
                    /*  if (element.dataViewId || element.colType == 'LINE') {
                         let obj = {
                             "colType": "LINE",
                             "colValue": element.colValue,
                             "refColumn": count++
                         }
                         this.jeLineDerivations.push(obj);
                     }else{
                         let obj = {
                             "colType": "SYSTEM",
                             "colValue": element.colValue,
                             "refColumn": count++
                         }
                         this.jeLineDerivations.push(obj);
                     } */
                });
                //console.log('this.jeLineDerivations@ ' + JSON.stringify(this.jeLineDerivations));
            } else {
                this.journalTemplate['enabledFlag'] = true;
                this.lineDerivationRuleList.forEach((element) => {
                    const obj1 = {
                        "colType": "SYSTEM",
                        "colValue": element.id
                    }
                    this.jeLineDerivations.push(obj1);
                });

                this.selectedLineAttributes.forEach((element) => {

                    if (element.dataViewId || element.colType == 'LINE') {
                        const obj3 = {
                            "colType": "LINE",
                            "colValue": element.id,
                            "refColumn": count++
                        }
                        this.jeLineDerivations.push(obj3);
                    } else if (element.lookUpCode) {
                        const obj4 = {
                            "colType": "SYSTEM",
                            "colValue": element.id,
                            "refColumn": count++
                        }
                        this.jeLineDerivations.push(obj4);
                    }
                });
            }

            //accountValue 343-43-4234 toleranceSelectedValue AMOUNT toleranceValue 432
        //icSetOffDebitValue 654-64-5534 icSetOffCreditValue 234-54-3533
            //conversionTypeValue 1047
            //console.log('this.accountValue ' + JSON.stringify(this.accountValue));
            //console.log('this.icSetOffDebitValue ' + JSON.stringify(this.icSetOffDebitValue));
            //console.log('this.icSetOffCreditValue ' + JSON.stringify(this.icSetOffCreditValue));
            const obj = {
                "attributeName": 'JOURNAL_BATCH_NAME',
                "aliasName": null,
                /* "value": this.journalTemplate.jeBatchName, */
                "ruleLevel": 'Header'
            }
            this.jeHeaderDerivations.push(obj);
            if (this.accountValue != undefined && this.accountValue && this.accountValue.length) {
                const obj1 = {
                    "attributeName": 'ROUNDING_SETOFF_ACCOUNT',/* Rounding Setoff Account */
                    "aliasName": null,
                    "value": this.accountValue[0].value,
                    "ruleLevel": 'Header'
                }
                this.jeHeaderDerivations.push(obj1);
            }
            const obj2 = {
                "attributeName": 'ROUNDING_SETOFF_TOLERANCE',/* Rounding Setoff Tolerance */
                "aliasName": this.toleranceSelectedValue,
                "value": this.toleranceValue,
                "ruleLevel": 'Header'
            }
            this.jeHeaderDerivations.push(obj2);
            if (this.icSetOffDebitValue != undefined && this.icSetOffDebitValue && this.icSetOffDebitValue.length) {
                const obj3 = {
                    "attributeName": 'MULTI_COMPANY_ACCOUNTS',/* Multi Company Accounts */
                    "aliasName": 'IC Set Off Debit',
                    "value": this.icSetOffDebitValue[0].value,
                    "ruleLevel": 'Header'
                }
                this.jeHeaderDerivations.push(obj3);
            }
            if (this.icSetOffCreditValue != undefined && this.icSetOffCreditValue && this.icSetOffCreditValue.length) {
                const obj4 = {
                    "attributeName": 'MULTI_COMPANY_ACCOUNTS',/* Multi Company Accounts */
                    "aliasName": 'IC Set Off Credit',
                    "value": this.icSetOffCreditValue[0].value,
                    "ruleLevel": 'Header'
                }
                this.jeHeaderDerivations.push(obj4);
            }
            /* let obj5 = {
                "attributeName": 'CONVERSION_TYPE',// Conversion Type 
                "aliasName": null,
                "value": this.conversionTypeValue,
                "ruleLevel": 'Header'
            } */
            const obj6 = {
                "attributeName": 'BALANCE_TYPE',/* Balance Type */
                "aliasName": this.journalTemplate.balanceTypeDisplayName,
                /* "value": this.journalTemplate.balanceType,  */
                "ruleLevel": 'Line'
            }
            this.jeHeaderDerivations.push(obj6);
            /* let obj7 = {
                "attributeName": 'JE_LEVEL',
                "aliasName": null,
                "value": this.journalTemplate.jeLevel,
                "ruleLevel": 'Header',
                "mappingType": 'LOOKUP_CODE'
            } */
            //this.jeHeaderDerivations.push(obj7);
            /* //console.log('this.jeLineDerivations ' + JSON.stringify(this.jeLineDerivations));
            //console.log('this.jeHeaderDerivations ' + JSON.stringify(this.jeHeaderDerivations));
            //console.log('accountValue ' + this.accountValue + ' toleranceSelectedValue ' + this.toleranceSelectedValue + ' toleranceValue ' + this.toleranceValue);
            //console.log('icSetOffDebitValue ' + this.icSetOffDebitValue + ' icSetOffCreditValue ' + this.icSetOffCreditValue);
            //console.log('conversionTypeValue ' + this.conversionTypeValue); */
            this.journalTemplate['jeHeaderDerivations'] = this.jeHeaderDerivations;
            this.journalTemplate['jeLineDerivations'] = this.jeLineDerivations;
            /* delete this.journalTemplate.jeBatchName;
            delete this.journalTemplate.balanceType; */
            delete this.journalTemplate.jeLevel;
            delete this.journalTemplate.balanceTypeDisplayName;
            //this.journalTemplate.coaId = +this.journalTemplate.coaId;
            /* //console.log('journal Template while posting:: ' + JSON.stringify(this.journalTemplate)); */

            //#region Date picker issue handling @Rk
            /* if (this.startDateChange || this.isEdit) {
                this.journalTemplate.startDate = new Date(this.journalTemplate.startDate.getTime() + 86400000);
            }
            if (this.endDateChange || this.isEdit) {
                if (this.journalTemplate.endDate)
                    this.journalTemplate.endDate = new Date(this.journalTemplate.endDate.getTime() + 86400000);
            } */
            //#endregion
            const uniq = {};
            /* this.journalTemplate.jeHeaderDerivations = this.journalTemplate.jeHeaderDerivations.filter(obj => !uniq[obj.id] && (uniq[obj.id] = true)); */
            this.journalTemplate.jeLineDerivations = this.journalTemplate.jeLineDerivations.filter((obj9) => !uniq[obj9.colValue] && (uniq[obj9.colValue] = true));
            let link: any = '';
            this.templateDetailsService.postTemplateDefinition(this.journalTemplate)
                .subscribe((res) => {
                    this.journalTemplateDetails = res;
                    this.commonService.success(
                        'Success!',
                        'Journal template successfully created'
                    )
                    if (this.journalTemplateDetails.id) {
                        link = ['/template-details', { outlets: { 'content': this.journalTemplateDetails.idForDisplay + '/template-details' } }];
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
            this.commonService.error(
                'Warning!',
                '"GL Date" and "Period" are mandatory at "Header Derivation Rule"'
            )
            this.hideSaveButton = false;
        }
    }

    /* FUNCTION 4 - While defining journal batch name onselection of checkbox
       Author: AMIT 
    */
    selectBatchValue(event, val) {
        ////console.log('selectedValues ' + this.selectedValues);
        this.selectedValues.forEach((element) => {
            if (element == 'Constant' && event == true) {
                this.constantInputField = true;
            } else if (val == 'Constant' && event == false) {
                this.constantInputField = false;
            }
        });
        this.defineBatchName();
    }

    /* FUNCTION 5 - To define batch name
       Author: AMIT 
    */
    defineBatchName() {
        this.journalTemplate.jeBatchName = '';
        this.dummyBatchName = [];
        this.selectedValues.forEach((element) => {
            if (element == 'Constant') {
                this.dummyBatchName.push(this.constantValue);
            } else {
                const a = '{'
                const b = '}'
                const c = a.concat(element);
                this.dummyBatchName.push(c.concat(b));
            }
        });
        this.journalTemplate.jeBatchName = this.dummyBatchName;
    }

    /* FUNCTION 6 - To fetch view columns based on view id
       Author: AMIT 
    */
    // myPopover:any;
    fetchViewColumns(id) {
        this.selectedLineAttributes = [];
        /* this.selectedLineAttributes.forEach(element => {
            if(element.dataViewId == undefined){
                this.selectedLineAttributes.splice(element,1);
            }
        }); */
        this.selectedViewLabel = "Selected Source";
        const tempAry = [];
        tempAry.push(id);
        this.dvDateColumns = [];
        this.request = this.templateDetailsService.getViewsColumnsData(tempAry)
            .subscribe((res: any) => {
                res[0].forEach((element) => {
                    element['aliasName'] = element.meaning;
                });
                this.selectedViewColumnsAtLines = res[0];
                this.selectedViewColumnsAtLines.forEach((element) => {
                    if (element.colDataType == 'DATE' || element.colDataType == 'DATETIME') {
                        this.dvDateColumns.push(element);
                    }
                });
                // this.myPopover.show()
                /* //console.log('this.selectedViewColumnsAtLines ' + JSON.stringify(this.selectedViewColumnsAtLines)); */
            });
    }

    /* FUNCTION 7 - To push data in header level selection on drag and drop
       Author: AMIT 
    */
    
    transferHeaderData($event) {
        if (this.journalTemplate.jeBatchName == '' || this.journalTemplate.jeBatchName == null) {
            $('#jeDefineId').click();
            this.commonService.info(
                'Warning!',
                'Define Journal Batch Name First'
            )
            /* this.myPopover.show(); */
        } else {
            let count = 0;
            ////console.log('called ' + JSON.stringify($event.dragData));
            this.selectedHeaderAttributes.forEach((element) => {
                if (element.id == $event.dragData.id) {
                    count++;
                    this.commonService.error(
                        'Warning!',
                        'Attribute "' + element.aliasName + '" already exists'
                    )
                }
            });
            if (count == 0) {
                this.selectedHeaderAttributes.push($event.dragData);
            }
        }

    }

    /* FUNCTION 8 - To delete header seleted column
       Author: AMIT 
    */
    removeHeaderColumn(col, ind) {
        /* //console.log('col ' + col + ' ind ' + ind); */
        this.selectedHeaderAttributes[ind].value = '';
        this.selectedHeaderAttributes.splice(ind, 1)
    }

    /* FUNCTION 9 - To push data in Line level selection on drag and drop
       Author: AMIT 
    */
    transferLineData($event) {
        //console.log('$event ' + $event.dragData);
        //console.log('this.selectedLineAttributes ' + JSON.stringify(this.selectedLineAttributes));
        if ($event.dragData) {
            if (this.selectedLineAttributes.length < 6) {
                let count = 0;
                this.selectedLineAttributes.forEach((element) => {
                    if (element.id == $event.dragData.id) {
                        count++;
                        if(element.aliasName){
                            this.commonService.error(
                                'Warning!',
                                'Attribute "' + element.aliasName + '" already exists'
                            )
                        }else{
                            this.commonService.error(
                                'Warning!',
                                'Attribute "' + element.columnName + '" already exists'
                            )
                        }
                    }
                });
                if (count == 0) {
                    this.selectedLineAttributes.push($event.dragData);
                }
            } else {
                this.commonService.info('Info!', 'Maximum 6 columns only');
            }
        }
    }

    /* FUNCTION 10 - To delete line level seleted column
       Author: AMIT 
    */
    deleteLineAttribute(col, ind) {
        /* //console.log('col ' + col + ' ind ' + ind); */
        this.selectedLineAttributes.splice(ind, 1)
    }

    dropLineAttributes($event: any) {
        this.selectedLineAttributes.push($event.dragData);
    }

    roundingFun(e) {
        /* //console.log('checked ' + e.checked); */
        if (e.checked == true) {
            setTimeout(() => $('.jeOverFlowBlock').css('overflow', 'visible'), 1000);
        } else {
            setTimeout(() => $('.jeOverFlowBlock').css('overflow', 'hidden'), 0);
        }
    }

    icSetOffFun(e) {
        if (e.checked == true) {
            setTimeout(() => $('.jeOverFlowBlock1').css('overflow', 'visible'), 1000);
        } else {
            setTimeout(() => $('.jeOverFlowBlock1').css('overflow', 'hidden'), 0);
        }
    }

    multiCurrencyFun($event) {
        /* //console.log('event ' + JSON.stringify($event)); */
        if ($event == true) {
            this.multiCurrencyCard = true;
        } else {
            this.multiCurrencyCard = false;
        }
    }

    checkJournalName(name: any) {
        this.request = this.templateDetailsService.getCheckJournalName(name)
            .subscribe((res: any) => {
                this.jeTempName = res;
                if (this.jeTempName.result == "No Duplicates Found") {
                    this.duplicateJournalName = false;
                } else {
                    this.duplicateJournalName = true;
                    /* this.commonService.error(
                        'Warning!',
                        this.jeTempName.result
                    ) */
                }
            });
    }

    
    displayCustFunDialog(dateQualifier: String) {
        if (dateQualifier == 'CUSTOM_FUNCTION') {
            this.functionDefinitionDialog = !this.functionDefinitionDialog;
        }
    }


    // this method check pressed key is numeric or not
    keyHandlerCount(code) {
        if (code > 31 && (code < 48 || code > 57)){
            return false;
        }else{
            return true;
        }
    }

    selectedTolerance(e) {
        /* //console.log('e ' + e.value); */
    }
    
    ngOnDestroy() {
        this.notificationsService.remove();
        if (this.request){
            this.request.unsubscribe();
        }
    }

    trackByIndex(i: number, v: number) {
        return i;
    }
    
    validateForm() {
        this.submitted = true;
    }
}
