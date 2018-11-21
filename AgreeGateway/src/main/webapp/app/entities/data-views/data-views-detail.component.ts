/*
  Module Name: Data Views Creation & Details Page
  Author: AMIT

    1. FUNCTION 1 : Side bar toggler functionality
    2. FUNCTION 2 - Function for responsive of start and end date
    3. FUNCTION 3 - Calling Function "fetchDataViewDetails" to fetch route path and get details accordingly
    4. FUNCTION 4 - Checking duplicate view name
    5. FUNCTION 5 - Function calling on view select and deselect
    6. FUNCTION 6 - Function calling on selection of relation
    7. FUNCTION 7 - Function calling on selection of view column
    8. FUNCTION 8 - Function calling on de-selection of view column
    9. 

*/

import { Component, OnInit, OnDestroy, OnChanges, Input, Inject } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { DataViews } from './data-views.model';
import { DataViewsService } from './data-views.service';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Router, NavigationEnd } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { NotificationsService } from 'angular2-notifications-lite';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { DataViewsColumnsService } from '../data-views-columns/data-views-columns.service';
import { CommonService } from '../common.service';
/* import { Angular2Csv } from 'angular2-csv/Angular2-csv'; */

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-data-views-detail',
    templateUrl: './data-views-detail.component.html'
})
export class DataViewsDetailComponent implements OnInit, OnDestroy {
    dataViews = new DataViews();
    fileTemplateList = [];
    operatorsList = [];
    dataTypeList = [];
    private subscription: any;
    eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    isEdit: boolean = false;
    parentPath = [];
    colField: boolean = false;
    columnEdit: boolean = false;
    showSelectedCol: boolean = false;
    savedCol: boolean = false;
    showColLov: boolean = false;
    addRowIcon: boolean = true;
    dropdownList = [];
    selectedItems = [];
    selectedColumns = [];
    dropdownSettings = {};
    limitSelectionSettings = {};
    basicExampleSettings: any = {};
    dropDownColumnList = [];
    dataViewsColumnsList = [];
    selectedviews = [];
    colTemplList: any = [];
    dataViewsPost: any = [];
    newData: boolean = false;
    filter: boolean = false;
    dataViewsVal: any = [];
    duplicateViewName: boolean = false;
    operator: any = [];
    colValue: any = [];
    excelFunctions = [];
    excelexpressioninput: any;
    excelexpressionExample: any;
    qualifierList = [];
    selectedRow: number = -1;
    rowSelected: boolean = true;
    viewRelations = [];
    selectedTemplates = [];
    viewRelation: any;
    basedTemplate: any;
    conditionLists = [];
    conditionList1: any;
    conditionList2: any;
    sourceName1: any;
    sourceName2: any;
    conditionValues: any = {};
    conditionsList: any = [];
    conditionEdit: boolean = false;
    sourceClick: boolean = false;
    targetClick: boolean = false;
    unionColumnsList = [];
    sourceCols: any = [];
    targetCols: any = [];
    unionSourceCol: any = [];
    unionTargetCol = [];
    conditionOperatorss: any;
    viewValidation: boolean = false;
    unionVal: boolean = false;
    qualifierList1: any = [];
    sourceTemplate: any = [];
    sourceColumns: any = [];
    sourceColumns1: any = [];
    columnsTemp: any = [];
    hideSaveButton: boolean = false;
    buttonFunction: boolean = true;
    groupByChecked: boolean;
    disblRelation: boolean = false;
    operatorss = [
        { value: '=', meaning: 'Equals' }
    ];
    isVisibleA: boolean = false;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private dataViewsService: DataViewsService,
        private route: ActivatedRoute,
        private router: Router,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private config: NgbDatepickerConfig,
        private dateUtils: JhiDateUtils,
        private notificationsService: NotificationsService,
        private dataViewsColumnsService: DataViewsColumnsService,
        private commonService: CommonService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }
   /*  ExportTable(){
				$("selectedColumnTable").tableExport({
				headings: true,                    // (Boolean), display table headings (th/td elements) in the <thead>
				footers: true,                     // (Boolean), display table footers (th/td elements) in the <tfoot>
				formats: ["xls"],    // (String[]), filetypes for the export
				fileName: "id",                    // (id, String), filename for the downloaded file
				bootstrap: true,                   // (Boolean), style buttons using bootstrap
				position: "well" ,                // (top, bottom), position of the caption element relative to table
				ignoreRows: null,                  // (Number, Number[]), row indices to exclude from the exported file
				ignoreCols: null,                 // (Number, Number[]), column indices to exclude from the exported file
				ignoreCSS: ".tableexport-ignore"   // (selector, selector[]), selector(s) to exclude from the exported file
			});
			}  */
    changeMinDate() {
        this.config.minDate = this.dataViews.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    ngOnInit() {
        this.showSelectedCol = false;
        this.commonService.screensize();
        this.getFileTemplateAndDataViews();

        this.dropdownSettings = {
            singleSelection: false,
            text: "Select Countries",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true
        };
        this.limitSelectionSettings = {
            text: "Select Template / Data Source",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true,
            badgeShowLimit: 1,
            disabled: false,
            classes: 'template-dropdown',
            limitSelection: 5
        };
        this.basicExampleSettings = {
            text: "Select Columns",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true,
            disabled: false,
            badgeShowLimit: 1,
            classes: 'columns-dropdown'
        }
        /* Fetching Excel Function List */
        this.dataViewsService.operators('FUNCTIONS').subscribe((res: any) => {
            this.excelFunctions = res;
        });
        /* Fetching Operators List */
        this.dataViewsService.operators('All').subscribe((res: any) => {
            this.operatorsList = res;
        });
        /* Fetching Data Type List */
        this.dataViewsService.operators('DATA_TYPE').subscribe((res: any) => {
            this.dataTypeList = res;
        });
        /* Fetching Qualifiers List */
        this.dataViewsService.operators('RECON_QUALIFIERS').subscribe((res: any) => {
            this.qualifierList = res;
        });
        /* Fetching Relations List */
        this.dataViewsService.operators('DV_RELATION').subscribe((res: any) => {
            this.viewRelations = res;
        });
        /* Calling Function "fetchDataViewDetails" to fetch route path and get details accordingly*/
        this.fetchDataViewDetails();
    }

    /* FUNCTION 1 - Side bar toggler functionality 
       Author: AMIT 
    */
    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
            $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }

    /* FUNCTION 2 - Function for responsive of start and end date
       Author: AMIT
       If Start Date Entered Apply Class 
    */
    startEndDateClass() {
        if (this.dataViews.startDate != null) {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }

    /* FUNCTION 3 - Calling Function "fetchDataViewDetails" to fetch route path and get details accordingly
       Author: AMIT 
    */
    fetchDataViewDetails() {
        this.dataViewsColumnsList = [];
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "data-views-new") {
                $('.component-title').removeClass('margin-left-22');
                this.newData = true;
            } else {
                $('.component-title').addClass('margin-left-22');
                this.newData = false;
            }
            if (params['id']) {
                this.sourceTemplate = [];
                this.dataViewsColumnsList = [];
                this.conditionOperatorss = "=";
                this.dataViewsService.getDataViewById(params['id']).subscribe(
                    (dataViews) => {
                        this.dataViews = dataViews[0];
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                        } else {
                            this.isViewOnly = true;
                        }
                        /* Three types of views "Single Template", "UNION" and "JOIN" */
                        if (this.dataViews.viewRelation != "UNION") {
                            this.showSelectedCol = true;
                            if(this.dataViews.dataViewsColumnsList)
                            for (var i = 0; i < this.dataViews.dataViewsColumnsList.length; i++) {
                                this.dataViews.dataViewsColumnsList[i].colName = this.dataViews.dataViewsColumnsList[i].colName;
                                this.dataViewsColumnsList.push(this.dataViews.dataViewsColumnsList[i]);
                                this.showQualifier(this.dataViews.dataViewsColumnsList[i].colDataType, i);
                            }
                            if(this.dataViews.templateInfo)
                            for (var x = 0; x < this.dataViews.templateInfo.length; x++) {
                                let obj = [{
                                    "type": this.dataViews.templateInfo[x].type,
                                    "typeId": this.dataViews.templateInfo[x].typeId
                                }];
                                this.dataViewsService.dataViewColTempList(obj).subscribe(
                                    (res: Response) => {
                                        this.columnsTemp = res;
                                        for (j = 0; j < this.columnsTemp.length; j++) {
                                            this.columnsTemp[j]['refDvColumn'] = this.columnsTemp[j].id;
                                            this.columnsTemp[j]['refDvName'] = this.columnsTemp[j].dataViewId;
                                            this.columnsTemp[j]['sourceName'] = this.columnsTemp[j].dataViewDisplayName;
                                            this.sourceColumns.push(this.columnsTemp[j]);
                                        }
                                        let temp = {
                                            "sourceName": this.columnsTemp[0].dataViewDisplayName,
                                            "refDvName": this.columnsTemp[0].dataViewId
                                        }
                                        this.sourceTemplate.push(temp);
                                    });
                            }
                            if (this.dataViews.conditions && this.dataViews.conditions.length > 0) {
                                let obj1;
                                let obj2;
                                for (var j = 0; j < this.dataViews.conditions.length; j++) {
                                    obj1 = [{
                                        "id": this.dataViews.conditions[j].srcCol1,
                                        "type": this.dataViews.conditions[j].srcType1,
                                        "typeId": this.dataViews.conditions[j].scr1
                                    }];
                                    obj2 = [{
                                        "id": this.dataViews.conditions[j].srcCol2,
                                        "type": this.dataViews.conditions[j].srcType2,
                                        "typeId": this.dataViews.conditions[j].scr2
                                    }];
                                }
                                this.dataViewsService.dataViewColTempList(obj1).subscribe(
                                    (res: Response) => {
                                        this.conditionList1 = res;
                                        for (var i = 0; i < obj1.length; i++) {
                                            for (var j = 0; j < this.conditionList1.length; j++) {
                                                if (obj1[i].id == this.conditionList1[j].id) {
                                                    this.dataViews.source = this.conditionList1[j].id;
                                                    this.sourceName1 = this.conditionList1[j].templateName;
                                                }
                                                this.conditionList1[j]['refDvColumn'] = this.conditionList1[j].id;
                                                this.conditionList1[j]['refDvName'] = this.conditionList1[j].dataViewId;
                                                this.conditionList1[j]['sourceName'] = this.conditionList1[j].dataViewDisplayName;
                                            }
                                        }
                                        let temp = {
                                            "sourceName": this.conditionList1[0].dataViewDisplayName,
                                            "refDvName": this.conditionList1[0].dataViewId
                                        }
                                    });
                                this.dataViewsService.dataViewColTempList(obj2).subscribe(
                                    (res: Response) => {
                                        this.conditionList2 = res;
                                        for (var i = 0; i < obj2.length; i++) {
                                            for (var j = 0; j < this.conditionList2.length; j++) {
                                                if (obj2[i].id == this.conditionList2[j].id) {
                                                    this.dataViews.target = this.conditionList2[j].id;
                                                    this.sourceName2 = this.conditionList2[j].templateName;
                                                }
                                                this.conditionList2[j]['refDvColumn'] = this.conditionList2[j].id;
                                                this.conditionList2[j]['refDvName'] = this.conditionList2[j].dataViewId;
                                                this.conditionList2[j]['sourceName'] = this.conditionList2[j].dataViewDisplayName;
                                            }
                                        }
                                        let temp = {
                                            "sourceName": this.conditionList2[0].dataViewDisplayName,
                                            "refDvName": this.conditionList2[0].dataViewId
                                        }
                                    });
                            }
                                /* $('selectedColumnTable').tableExport({type:'excel'}); */
                        } else if (this.dataViews.viewRelation == "UNION") {
                            this.showSelectedCol = false;
                            this.unionColumnsList = this.dataViews.dataViewsUnionColumnsList;
                            this.selectedTemplates = this.dataViews.templateInfo;
                            if(this.selectedTemplates.length) {
                                this.dataViewsService.getTemplateLines(this.selectedTemplates).subscribe(res => {
                                    this.sourceCols = res;
                                    for (let j = 0; j < this.sourceCols.length; j++) {
                                        this.sourceCols[j].unshift({
                                            'refDvColumn': 'none',
                                            'columnName': 'None',
                                            'excelexpressioninputUnion': null
                                        });
                                        this.sourceCols[j].unshift({
                                            'refDvColumn': 'customFunction',
                                            'columnName': 'Custom Function'
                                        });
                                    }
                                    //Disabling already selected option
                                    /* this.unionColumnsList.forEach(uninColEach => {
                                        uninColEach.src.forEach((srcEach, i) => {
                                            this.sourceCols[i].forEach(sourColEach => {
                                                if (srcEach.refDvColumn == sourColEach.refDvColumn) {
                                                    sourColEach.isDisabled = true;
                                                }
                                            });
                                        });
                                    }); */

                                });
                            }
                            for (var i = 0; i < this.dataViews.dataViewsUnionColumnsList.length; i++) {
                                this.showQualifier(this.dataViews.dataViewsUnionColumnsList[i].colDataType, i);
                                /* this.dataViews.dataViewsUnionColumnsList[i]['sourceCol'] = this.dataViews.dataViewsUnionColumnsList[i].src[0].refDvColumn;
                                this.dataViews.dataViewsUnionColumnsList[i]['targetCol'] = this.dataViews.dataViewsUnionColumnsList[i].src[1].refDvColumn; */
                            }
                        }
                    }
                );
            } else {
                this.isCreateNew = true;
                this.isVisibleA = true;
                this.dataViews.startDate = new Date();
                $(window).on('click', function (e) {
                    let elem1 = document.querySelector('.template-dropdown');
                    let elem2 = document.querySelector('.columns-dropdown');
                    if ((elem1 && !elem1.contains(e.target)) && (elem2 && !elem2.contains(e.target))) {
                        $(".split-example").css('min-height', (window.innerHeight - 130) + 'px');
                    } else if (!elem2 && (elem1 && !elem1.contains(e.target))) {
                        $(".split-example").css('min-height', (window.innerHeight - 130) + 'px');
                    }
                });
            }
        });
    }

    

    /* FUNCTION 4 - Checking duplicate view name
       Author: AMIT 
    */
    checkViewName(val, id) {
        this.dataViewsService.checkDataViewIsExist(val, id).subscribe(res => {
            if (res.result != 'No Duplicates Found') {
                this.duplicateViewName = true;
            } else {
                this.duplicateViewName = false;
            }
        }, err => this.notificationsService.error('Warning!', 'Error Occured'));
    }

    /* FUNCTION 5 - Function calling on view select and deselect
       Author: AMIT 
    */
    onSelectTemplateAndDataView(item) {
        item = item.sort((a, b) => {
            if (a.typeId != b.typeId) {
                return a.typeId - b.typeId;
            } else {
                return a.intermediateId - b.intermediateId;
            }
        });
        this.dropDownColumnList = [];
        this.dataViewsColumnsList = [];
        this.selectedColumns = [];
        this.dataViews.srcMapping = this.selectedTemplates = item;
        this.viewRelation = '';
        this.basedTemplate = '';
        this.dataViews.source = '';
        this.dataViews.target = '';
        this.conditionList1 = [];
        this.conditionList2 = [];
        /* If colField is true showing field to select view relation else select column field */
        if (item.length == 1) {
            this.colField = true;
        } else if (item.length == 2) {
            this.disblRelation = false;
            this.colField = false;
            this.selectedColumns = [];
        } else {
            this.viewRelation = this.viewRelations[1].lookUpCode;
            this.disblRelation = true;
        }

        /* Calling service function "dataViewColTempList" to fetch selected view columns */
        if (item.length > 1) {
            if (this.viewRelation == 'UNION'){
                this.getTepmlateLinesUnion(item);
                this.emptyOtherthanUnion();
            }
        } else {
            this.getTepmlateLinesSingleJoin(item);
        }
    } //<---(End:) onSelectTemplateAndDataView


    getTepmlateLinesUnion(item) {
        this.dataViewsService.getTemplateLines(item).subscribe(res => {
            this.sourceCols = res;
            for (let j = 0; j < this.sourceCols.length; j++) {
                this.sourceCols[j].unshift({
                    'refDvColumn': 'none',
                    'columnName': 'None',
                    'excelexpressioninputUnion': null
                });
                this.sourceCols[j].unshift({
                    'refDvColumn': 'customFunction',
                    'columnName': 'Custom Function'
                });
            }
        });
    }

    getTepmlateLinesSingleJoin(item) {
        this.dataViewsService.dataViewColTempList(item).subscribe((res: Response) => {
            this.colTemplList = res;
            let count = 1;
            for (var i = 0; i < this.colTemplList.length; i++) {
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace("_", " ");
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace(/\w\S*/g, function (txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace("_", " ");
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace(/\w\S*/g, function (txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace("_", " ");
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace(/\w\S*/g, function (txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
                this.colTemplList[i]['refDvColumn'] = this.colTemplList[i].id;
                this.colTemplList[i]['refDvName'] = this.colTemplList[i].dataViewId;
                this.colTemplList[i].columnEdit = true;
                let occured: boolean = false;
                for (var j = 0; j < item.length; j++) {
                    if (this.colTemplList[i].dataViewId == item[j].typeId) {
                        occured = true;
                        this.colTemplList[i].intermediateId = item[j].intermediateId;
                    }
                }
                if (occured) {
                    let obj = {
                        "id": count++,
                        "itemName": this.colTemplList[i].columnHeader + " - " + this.colTemplList[i].dataViewDisplayName,
                        "selColName": this.colTemplList[i].columnHeader,
                        "colId": this.colTemplList[i].dataViewId,
                        "columnrefDvColumn": this.colTemplList[i].refDvColumn
                    }
                    this.dropDownColumnList.push(obj);
                }
            }
        });
    }



    /* FUNCTION 6 - Function calling on selection of relation
       Author: AMIT 
    */
    relationFun(rel: any) {
        this.qualifierList1 = [];
        if (rel == 'UNION') {
            this.getTepmlateLinesUnion(this.selectedTemplates);
            this.emptyOtherthanUnion();
            this.unionColumnsList = [];
            let obj = {
                "columnEdit": true,
                "src": []
            }
            this.unionColumnsList.push(obj);
        } else {
            this.basedTemplate = this.selectedTemplates[0];
            const conOne = [];
            const conTwo = [];
            conOne.push(this.selectedTemplates[0]);
            conTwo.push(this.selectedTemplates[1]);
            this.dataViewsService.dataViewColTempList(conOne).subscribe((res: Response) => {
                this.conditionList1 = res;
                this.sourceName1 = this.conditionList1[0].templateName;
            });
            this.dataViewsService.dataViewColTempList(conTwo).subscribe((res: Response) => {
                this.conditionList2 = res;
                this.sourceName2 = this.conditionList2[0].templateName;
            });
            this.getTepmlateLinesSingleJoin(this.selectedTemplates);
            delete this.dataViews.unionColumnsList;
        }
    }

    emptyOtherthanUnion() {
        delete this.dataViews.dataViewsColumnsList;
        delete this.dataViews.basedTemplate;
        delete this.dataViews.conditions;
        this.selectedColumns = [];
        this.dataViewsColumnsList = [];
        this.basedTemplate = '';
        if (!this.unionColumnsList.length) {
            this.unionColumnsList = [];
            let obj = {
                "columnEdit": true,
                "src": []
            }
            this.unionColumnsList.push(obj);
        }
    }

    /* FUNCTION 7 - Function calling on selection of view column
       Author: AMIT 
    */
    onColumnSelect(item) {
        this.filter = false;
        for (var j = 0; j < this.colTemplList.length; j++) {
            for (var i = 0; i < item.length; i++) {
                if (item[i].colId == this.colTemplList[j].dataViewId && item[i].selColName == this.colTemplList[j].columnHeader && item[i].columnrefDvColumn == this.colTemplList[j].refDvColumn) {
                    this.colTemplList[j].colName = this.colTemplList[j].columnHeader;
                    this.colTemplList[j]['sourceName'] = this.colTemplList[j].dataViewDisplayName;
                    this.dataViewsColumnsList.push(this.colTemplList[j]);
                }
            }
        }
        this.dataViewsColumnsList = Array.from(this.dataViewsColumnsList.reduce((m, t) => m.set(t.refDvColumn, t), new Map()).values());
        if (this.dataViewsColumnsList.length > 0) {
            this.showSelectedCol = true;
        }
    }

    /* FUNCTION 8 - Function calling on de-selection of view column
       Author: AMIT 
    */
    OnColumnDeSelect(item) {
        this.filter = false;
        this.dataViewsColumnsList = this.dataViewsColumnsList.filter(function (o1) {
            return item.some(function (o2) {
                return (o1.dataViewId === o2.colId && o1.columnHeader === o2.selColName) || (o1.dataViewId === undefined && o1.columnHeader === undefined); // return the ones with equal id
            });
        });
    }

    /* Function to display operator based on selected data type */
    showOperator(val, ind) {
        this.operatorsList[ind] = this.operatorsList.filter((item) => item.lookUpType == val);
    }

    showQualifier(val, ind) {
        for (var i = 0; i < this.qualifierList.length; i++) {
            if (val == "DECIMAL" && this.qualifierList[i].lookUpCode == 'AMOUNT') {
                this.qualifierList[i]['quali'] = val;
            } else if (val == "VARCHAR" && (this.qualifierList[i].lookUpCode == 'CURRENCYCODE' || this.qualifierList[i].lookUpCode == 'GROUPBY_COLUMN')) {
                this.qualifierList[i]['quali'] = val;
            } else if ((val == "DATE" || val == "DATETIME") && this.qualifierList[i].lookUpCode == 'TRANSDATE') {
                this.qualifierList[i]['quali'] = val;
            } else if (val == "BOOLEAN" || val == "INTEGER") {

            }
        }
        this.qualifierList1[ind] = this.qualifierList.filter((item) => item.quali == val);
    }

    displayColumns(id: any, ind: any) {
        this.sourceColumns1 = [];
        this.sourceColumns1 = this.sourceColumns.filter((item) => item.refDvName == id);
        for (var i = 0; i < this.dataViewsColumnsList.length; i++) {
            for(var j=0;j<this.sourceColumns1.length;j++){
                this.sourceColumns1[j].columnName = this.sourceColumns1[j].columnName.replace("_", " ");
                this.sourceColumns1[j].columnName = this.sourceColumns1[j].columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
                if(this.dataViewsColumnsList[i].refDvColumn == this.sourceColumns1[j].refDvColumn){
                    this.sourceColumns1.splice(j,1);
                }
                if (ind == i) {
                    this.dataViewsColumnsList[ind]['columnName'] = '';
                }
            }
        }
    }

    setColumnName(col: any, ind: any) {
        for (var i = 0; i < this.dataViewsColumnsList.length; i++) {
            if (ind == i) {
                this.dataViewsColumnsList[ind]['columnName'] = col.columnName;
            }
        }
    }

    listOperatorFun() {
        if (this.dataViewsColumnsList != null || this.dataViewsColumnsList != undefined) {
            for (var i = 0; i < this.dataViewsColumnsList.length; i++) {
                this.showOperator(this.dataViewsColumnsList[i].colDataType, i);
            }
        }
    }

    /* Function to get file templates and data views list */
    getFileTemplateAndDataViews() {
        let count = 1;
        this.dataViewsService.fileTemplateList('DATA_VIEW').subscribe((res: any) => {
            this.fileTemplateList = res;
            for (var i = 0; i < this.fileTemplateList.length; i++) {
                if (this.fileTemplateList[i].status == 'Active') {
                    let obj = {
                        "id": count++,
                        "itemName": this.fileTemplateList[i].templateName,
                        "dataName": this.fileTemplateList[i].templateName,
                        "type": "File Template",
                        "typeId": this.fileTemplateList[i].id,
                        "intermediateId": this.fileTemplateList[i].intermediateId
                    }
                    this.dropdownList.push(obj);
                }
            }
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        //If not stop listening, the above (windw) listener will not stop even component destroys
        $(window).off('click');
    }






    saveColumns(val) {
        this.showSelectedCol = true;
        this.savedCol = true;
        this.basicExampleSettings = {
            text: "Select Columns",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: false,
            classes: "myclass custom-class",
            disabled: true
        }
    }
    /* Function to update and save dataview columns */
    updateColumnData(val: any) {
        if (val.colValue == null) {
            delete val.colValue;
        } else if (val.operator == null) {
            delete val.operator;
        }
        if (this.presentPath == "data-views-new") {

        } else {
            if (val.dataViewId == undefined) {
                val.dataViewId = this.dataViews.id;
                if (val.refDvName != null) {
                    val.refDvType = "File Template";
                }
            }
            val.columnName = val.columnName.replace("_", " ");
            val.columnName = val.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
            this.dataViewsService.updateDataViewCol(val)
                .subscribe((res: any) => {
                    this.buttonFunction = true;
                    this.dataViewsService.createDataView(this.dataViews.id).subscribe((dataViews) => {

                    });
                    this.dataViewsColumnsList = [];
                    this.notificationsService.success(
                        'Success!',
                        'Column Data Successfully Updated'
                    )
                    this.fetchDataViewDetails();
                }
                );
        }
    }
    updateUnionColumnData(val: any, ind: any) {
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
            if (val.id != null) {
                this.dataViewsService.updateUnionDataViewCol(val).subscribe((res: any) => {
                        this.buttonFunction = true;
                        this.dataViewsService.createDataView(this.dataViews.id).subscribe((dataViews) => {

                        });
                        this.dataViewsColumnsList = [];
                        this.notificationsService.success(
                            'Success!',
                            'Column Data Successfully Updated'
                        )
                        this.fetchDataViewDetails();
                    });
            } else {
                val.dataViewId = this.dataViews.id;
                this.dataViewsService.updateUnionDataViewCol(val)
                    .subscribe((res: any) => {
                        this.buttonFunction = true;
                        this.dataViewsService.createDataView(this.dataViews.id).subscribe((dataViews) => {

                        });
                        this.dataViewsColumnsList = [];
                        this.notificationsService.success(
                            'Success!',
                            'Column Data Successfully Updated'
                        )
                        this.fetchDataViewDetails();
                    });
            }
    }
    deleteColumn(val: any, ind: any) {
        if (this.dataViewsColumnsList.length > 1) {
            if (this.presentPath == "data-views-new") {
                let temp = [];
                if (val.id) {
                    for (var a = 0; a < this.selectedColumns.length; a++) {
                        if (this.selectedColumns[a].colId == val.refDvName && this.selectedColumns[a].selColName == val.columnHeader) {
                            this.selectedColumns.splice(a, 1);
                        }
                    }
                }

                for (var i = 0; i < this.dataViewsColumnsList.length; i++) {
                    if (ind == i) {
                        this.dataViewsColumnsList[i].colDataType = '';
                        delete this.qualifierList1[ind];
                        this.dataViewsColumnsList.splice(this.dataViewsColumnsList.indexOf(val), 1);
                    }
                }
                for (var j = 0; j < this.qualifierList1.length; j++) {
                    if (this.qualifierList1[j]) {
                        temp.push(this.qualifierList1[j]);
                    }
                }
                this.qualifierList1 = temp;
            } else {
                this.dataViewsService.deleteDataViewCol(val.id).subscribe(response => {
                    this.dataViewsColumnsList.splice(this.dataViewsColumnsList.indexOf(val), 1);
                    this.dataViewsService.createDataView(this.dataViews.id).subscribe((dataViews) => {});
                    this.notificationsService.success('Success!','Column Deleted Successfully');
                });
            }
        } else {
            this.notificationsService.error('Warning!','Atleast one column is mandatory');
        }

    }

    deleteUnionColumn(val: any, ind: any) {
        if (this.unionColumnsList.length > 1) {
            if (this.presentPath == "data-views-new") {
                let temp = [];
                for (var i = 0; i < this.unionColumnsList.length; i++) {
                    if (ind == i) {
                        this.unionColumnsList[i].colDataType = '';
                        delete this.qualifierList1[ind];
                        this.unionColumnsList.splice(this.unionColumnsList.indexOf(val), 1);
                    }
                }
                for (var j = 0; j < this.qualifierList1.length; j++) {
                    if (this.qualifierList1[j]) {
                        temp.push(this.qualifierList1[j]);
                    }
                }
                this.qualifierList1 = temp;

            } else {
                this.dataViewsService.deleteUnionDataViewCol(val.id).subscribe(response => {
                    let result: any = response;
                    this.dataViewsService.createDataView(this.dataViews.id)
                        .subscribe((dataViews) => {

                        });
                    this.fetchDataViewDetails();
                    this.notificationsService.success(
                        'Success!',
                        'Column "' + result.columnName + '" Deleted Successfully'
                    )
                });
            }
        } else {
            this.notificationsService.error(
                'Warning!',
                'Atleast one column is mandatory'
            )
        }

    }
    addUnionColumn() {
        let count = 0;
        for (var i = 0; i < this.unionColumnsList.length; i++) {
            if (this.unionColumnsList[i].columnName == '' || this.unionColumnsList[i].colDataType == '' ||
                this.unionColumnsList[i].columnName == undefined || this.unionColumnsList[i].colDataType == undefined ||
                this.unionColumnsList[i].src.length != this.selectedTemplates.length) {
                count++;
            }
        }
        if (count > 0) {
            this.notificationsService.error(
                'Warning!',
                'Fill is mandatory fields to add another column'
            )
        } else {
            let obj = {
                "columnEdit": true,
                "src": []
            }
            this.unionColumnsList.push(obj);
            for (let i = 0; i < this.unionColumnsList.length; i++) {
                if (!this.unionColumnsList[i].src.length) {
                    for (let j = 0; j < this.sourceCols.length; j++) {
                        this.sourceCols[j].splice(0, 1, {
                            'refDvColumn': 'customFunction',
                            'columnName': 'Custom Function'
                        });
                    }
                }
            }
        }
    }

    cancelColumnChanges(ind: any) {
        this.sourceColumns1 = [];
        this.sourceColumns = [];
        this.unionColumnsList.forEach(element => {
            if (element.id == undefined) {
                this.unionColumnsList.splice(ind, 1);
            }
        });
        this.fetchDataViewDetails();
    }
    cancelUnionColumnChanges(ind: any) {
        this.dataViewsColumnsList.forEach(element => {
            if (element.id == undefined) {
                this.dataViewsColumnsList.splice(ind, 1);
            }
        });

        this.fetchDataViewDetails();
    }
    addColumn() {
        let count = 0;
        for (var i = 0; i < this.dataViewsColumnsList.length; i++) {
            if (this.dataViewsColumnsList[i].columnName == '' || this.dataViewsColumnsList[i].colDataType == '' ||
                this.dataViewsColumnsList[i].columnName == undefined || this.dataViewsColumnsList[i].colDataType == undefined) {
                count++;
            }
        }
        if (count > 0) {
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields to add another column'
            )
        } else {
            let newLine = {
                "columnEdit": true
            };
            this.dataViewsColumnsList.push(newLine);
            this.filter = false;
        }
    }

    /* Function to show/hide filter columns */
    applyFilter() {
        let count = 0;
        this.dataViewsColumnsList.forEach(element => {
            if (element.colDataType === null || element.colDataType === undefined || element.colDataType === '') {
                count++;
            }
        });
        if (count > 0) {
            this.notificationsService.error(
                'Warning!',
                'Data Type is mandatory to apply filter'
            )
        } else {
            if (this.filter == true) {
                this.filter = false;
            } else {
                this.filter = true;
            }
        }
    }
    sourceCall(obj: any) {
        this.conditionValues.srcType1 = obj.refDvType;
        this.conditionValues.scr1 = obj.dataViewId;
        this.conditionValues.srcCol1 = obj.id;
        this.sourceClick = true;

    }
    targetCall(obj: any) {
        this.conditionValues.srcType2 = obj.refDvType;
        this.conditionValues.scr2 = obj.dataViewId;
        this.conditionValues.srcCol2 = obj.id;
        this.conditionValues.conditionOperator = "=";
        this.targetClick = true;
    }
    testAr = [];
    /* Function to save and update data view */
    isStartDateChanged: boolean = false;
    saveFlag: any;
    saveDataViews() {
        this.saveFlag = undefined;
        this.hideSaveButton = true;
        let count = 0;
        let count1 = 0;
        this.dataViews['viewRelation'] = this.viewRelation;
        this.dataViews['basedTemplate'] = this.basedTemplate;
        let link: any = '';
        
        if(!this.isStartDateChanged && this.isCreateNew) {
            this.dataViews.startDate = new Date(this.dataViews.startDate.setDate(this.dataViews.startDate.getDate()-1));
        }

        if (this.dataViews) {
            if (this.dataViews.viewRelation && this.dataViews.viewRelation != 'UNION') {
                let betweenOperRegex = new RegExp('^[a-zA-z]+,[a-zA-z]+$');
                this.dataViewsColumnsList.forEach((each, i) => {
                    if (each.operator && !each.colValue) {
                        this.saveFlag = count1+1;
                    } else if(each.operator == 'BETWEEN' && !betweenOperRegex.test(each.colValue)) {
                        this.saveFlag = count1+1;
                    }
                });
            }
        }

        if (this.saveFlag) {
            this.hideSaveButton = false;
        } else {
            if (this.dataViews.id !== undefined) {
                this.dataViewsPost = [];
                this.dataViewsPost.push(this.dataViews);
                delete this.dataViewsPost[0].dataViewsColumnsList;
                delete this.dataViewsPost[0].dataViewsUnionColumnsList;
                this.dataViewsService.postDataViews(this.dataViewsPost)
                    .subscribe((dataViews) => {
                        this.dataViews = dataViews;
                        this.notificationsService.success(
                            'Success!',
                            'Successfully Updated Data Source'
                        )
                        this.hideSaveButton = false;
                        if (this.dataViews.id) {
                            link = ['/data-views', { outlets: { 'content': this.dataViews.id + '/details' } }];
                            this.router.navigate(link);
                        }
                        this.dataViewsService.createDataView(this.dataViews.id)
                            .subscribe((dataViews) => {
    
                            });
                    });
            } else {
                if ((this.dataViews.viewRelation === null || this.dataViews.viewRelation === undefined || this.dataViews.viewRelation === '') &&
                    (this.dataViews.basedTemplate === null || this.dataViews.basedTemplate === undefined || this.dataViews.basedTemplate === '')) {
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                } else {
                    this.conditionsList.push(this.conditionValues);
                    this.dataViews['conditions'] = this.conditionsList;
                }
    
                if (this.dataViews.viewRelation != 'UNION') {
                    this.dataViews['dataViewsColumnsList'] = this.dataViewsColumnsList;
                } else {
                    let count = -1;
                    for (var i = 0; i < this.unionColumnsList.length; i++) {
                        this.testAr = [];
                        count++;
                        this.unionColumnsList[i]['index'] = count;
                        for (var j = 0; j < this.sourceCols.length; j++) {
                            for (var x = 0; x < this.targetCols.length; x++) {
                                if (this.unionColumnsList[i].sourceCol == this.sourceCols[j].refDvColumn && this.unionColumnsList[i].targetCol == this.targetCols[x].refDvColumn
                                    && this.unionColumnsList[i].index == i) {
                                    this.testAr.push(this.sourceCols[j]);
                                    this.testAr.push(this.targetCols[x]);
                                    this.unionColumnsList[i].src = this.testAr;
                                }
                            }
                        }
                    }
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    this.dataViews['dataViewsUnionColumnsList'] = this.unionColumnsList;
                }
                this.dataViews['enabledFlag'] = true;
                this.dataViewsPost.push(this.dataViews);
                if (this.dataViews.viewRelation != 'UNION') {
                    this.saveDataViewsV();
                } else {
    
                    this.viewValidation = true;
                }
    
                if (this.viewValidation == true) {
                    this.dataViewsService.postDataViews(this.dataViewsPost)
                        .subscribe((dataViews) => {
                            this.dataViews = dataViews;
                            this.dataViewsService.createDataView(this.dataViews.id)
                                .subscribe((dataViews) => {
    
                                });
                            this.notificationsService.success(
                                'Success!',
                                'Successfully Data Source Created'
                            )
                            this.hideSaveButton = false;
                            if (this.dataViews.id) {
                                link = ['/data-views', { outlets: { 'content': this.dataViews.id + '/details' } }];
                                if (this.isEdit) {
                                    this.isEdit = false;
                                }
                                if (this.isCreateNew) {
                                    this.isCreateNew = false;
                                }
                                this.isViewOnly = true;
                                this.router.navigate(link);
                            }
    
                        },
                    (res: Response) => {
                        this.onError(res.json()
                        )
                        this.notificationsService.error('Error!', 'Something Went Wrong');
                        this.hideSaveButton = false;
                    });
                }
    
    
            }
        }

    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    saveDataViewsU() {
        this.unionColumnsList.forEach(element => {
            element.columnName = element.columnName.replace("_", " ");
            element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
            element.columnName = element.columnName.replace("_", " ");
            element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
            element.columnName = element.columnName.replace("_", " ");
            element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
            element.columnName = element.columnName.replace("_", " ");
            element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        });
        
        if (this.unionColumnsList) {
            let count = 0;
            let intcount = 0;
            let datecount = 0;
            let datecountint = 0;
            let currcount = 0;
            let currcountint = 0;
            let datecountint1 = 0;
            let datecount1 = 0;
            let groupByCount = 0;
            for (var i = 0; i < this.unionColumnsList.length; i++) {
                if (this.unionColumnsList[i].colDataType == '' || this.unionColumnsList[i].colDataType == null) {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        'Data Type is Mandatory'
                    )
                } else if (this.unionColumnsList[i].colDataType == 'DATE') {
                    datecountint++;
                    if (this.unionColumnsList[i].qualifier == 'TRANSDATE') {
                        datecount++;
                    }
                } else if (this.unionColumnsList[i].colDataType == 'DATETIME') {
                    datecountint1++;
                    if (this.unionColumnsList[i].qualifier == 'TRANSDATE') {
                        datecount1++;
                    }
                }else if (this.unionColumnsList[i].groupBy) {
                    if(this.unionColumnsList[i].groupBy == true){
                        groupByCount++;
                    }
                } else if (this.unionColumnsList[i].qualifier != '' || this.unionColumnsList[i].qualifier == undefined || this.unionColumnsList[i].qualifier == '') {
                    if (this.unionColumnsList[i].qualifier == 'AMOUNT') {
                        count++;
                        if (this.unionColumnsList[i].colDataType == 'DECIMAL') {
                            intcount++;
                        }
                    } else if (this.unionColumnsList[i].qualifier == 'CURRENCYCODE' || this.unionColumnsList[i].qualifier == 'GROUPBY_COLUMN') {
                        currcount++;
                        if (this.unionColumnsList[i].colDataType == 'VARCHAR') {
                            currcountint++;
                        }
                    }
                } else {
                    this.saveDataViews();
                }
            }

            if (count > 1) {
                this.hideSaveButton = false;
                this.notificationsService.error(
                    'Warning!',
                    '"Amount" qualifier should be tagged only to one column with data type "DECIMAL"'
                )
            } else if (count == 1) {
                if (intcount == 1) {
                } else {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        'Data type of "Amount" qualifier should be of type "DECIMAL"'
                    )
                }
            } else if (count < 1) {
                this.hideSaveButton = false;
                this.notificationsService.error(
                    'Warning!',
                    '"Amount" qualifier is mandatory'
                )
            }

            if (datecountint > 0 && datecount < 1) {
                this.hideSaveButton = false;
                this.notificationsService.error(
                    'Warning!',
                    '"TRANSDATE" qualifier should be tagged to data type "DATE"'
                )
            }
            if (datecountint1 > 0 && datecount1 < 1) {
                this.hideSaveButton = false;
                this.notificationsService.error(
                    'Warning!',
                    '"TRANSDATE" qualifier should be tagged to data type "DATETIME"'
                )
            }

            if (currcount > 1) {
                this.hideSaveButton = false;
                this.notificationsService.error(
                    'Warning!',
                    '"CURRENCY" qualifier should be tagged only to one column with data type "VARCHAR'
                )
            } else if (currcount == 1) {
                if (currcountint == 1) {
                } else {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        'Data type of "TRANSDATE" qualifier should be "VARCHAR"'
                    )
                }
            }
            if (groupByCount == 0) {
                    this.notificationsService.error(
                        'Warning!',
                        'Atleast one "Group By" column should be selected'
                    )
                    this.hideSaveButton = false;
                }
            if ((count == 1 && intcount == 1) && (datecount == datecountint) && (datecount1 == datecountint1) && groupByCount>0) {
                this.saveDataViews();
            }

        }


    }
    /* Function to display validition message */
    validationMsg() {
        if (this.duplicateViewName == true) {
            this.hideSaveButton = false;
            this.notificationsService.error(
                'Warning!',
                'Display Name already exists'
            )
        } else if (this.duplicateViewName == false) {
            this.hideSaveButton = false;
            this.notificationsService.error(
                'Warning!',
                'Fill Mandatory Fields'
            )
        } else if (this.dataViewsColumnsList.length < 1) {
            this.hideSaveButton = false;
            this.notificationsService.error(
                'Warning!',
                'Select atleast one column'
            )
        }
    }


    showExcelFunction(val, ind, examp) {
        for (var i = 0; i < this.dataViewsColumnsList.length; i++) {
            if (i == ind) {
                this.dataViewsColumnsList[i]['excelexpressioninput'] = val;
                this.dataViewsColumnsList[i]['excelexpressionExample'] = examp;
            }
        }
    }

    showExcelUnionFunction(val) {
        this.unionColumnsList[val.i].src[val.j].refDvColumn = 'customFunction'; 
        if (!val.isSaved){
            val.selFx.excelexpressioninputUnion = null;
        }
        /* ,i,j,examp,forml */
        /* this.unionColumnsList[i].src[j] = {
            'excelExpressionUnion': val,
            'excelexpressionExampleUnion': examp,
            'refDvColumn': 'customFunction',
            'formula': forml
        }; */
        /* this.unionColumnsList[val.i].src[val.j].excelExpressionUnion = val.selFx.lookUpCode;
        this.unionColumnsList[val.i].src[val.j].excelexpressionExampleUnion = val.selFx.description;
        this.unionColumnsList[i].src[j].formula = forml; */
    }

    dispCopyIcon = false;
    saveExpression(val, ind, frml, popOver) {
        let templ = [];
        this.selectedTemplates.forEach(eachTempl => {
            templ.push(eachTempl.dataName);
        });
        let finFrml = val ? val : frml;
        this.dataViewsService.validateCurrentTemplate(finFrml, templ).subscribe(res => {
            if (res.status == 'Failure') {
                this.notificationsService.error('Warning!', 'Error in Expression or Template not match');
            } else {
                this.dataViewsColumnsList[ind]['formula'] = finFrml;
                this.dispCopyIcon = false;
                popOver.hide();
            }
        });
    }

    insertUnionExpression() {
        let fx = this.custFxDialogData;
        let templ = [];
        templ.push(this.selectedTemplates[fx.j].dataName);
        if (fx.selFx.excelexpressioninputUnion) {
            this.dataViewsService.validateCurrentTemplate(fx.selFx.excelexpressioninputUnion, templ).subscribe(res => {
                if (res.status == 'Failure') {
                    this.notificationsService.error('Warning!', 'Error in Expression or Template not match');
                } else {
                    this.unionColumnsList[fx.i].src[fx.j].excelexpressioninputUnion = fx.selFx.excelexpressioninputUnion;
                    this.unionColumnsList[fx.i].src[fx.j].refDvType = 'File Template';
                    this.unionColumnsList[fx.i].src[fx.j].refDvName = this.selectedTemplates[fx.j].typeId;
                    this.unionColumnsList[fx.i].src[fx.j].intermediateId = this.selectedTemplates[fx.j].intermediateId;
                    this.unionColumnsList[fx.i].src[fx.j].columnName = 'customcolumn';
                    this.unionColumnsList[fx.i].src[fx.j].isSaved = true;
                    this.custFxDialog = false;
                }
            });
        }
    }

    /* Function to update conditions */
    updateConditions() {
        this.conditionValues.filterOperator = '=';
        this.conditionValues.dataViewId = this.dataViews.id;
        this.conditionValues.id = this.dataViews.conditions[0].id;
        if (this.sourceClick == false) {
            this.conditionValues.refSrcType = this.dataViews.conditions[0].srcType1;
            this.conditionValues.refSrcId = this.dataViews.conditions[0].scr1;
            this.conditionValues.refSrcColId = this.dataViews.conditions[0].srcCol1;
        } else {
            this.conditionValues.refSrcType = this.conditionValues.srcType1;
            this.conditionValues.refSrcId = this.conditionValues.scr1;
            this.conditionValues.refSrcColId = this.conditionValues.srcCol1;
        }
        if (this.targetClick == false) {
            this.conditionValues.refSrcType2 = this.dataViews.conditions[0].srcType2;
            this.conditionValues.refSrcId2 = this.dataViews.conditions[0].scr2;
            this.conditionValues.refSrcColId2 = this.dataViews.conditions[0].srcCol2;
        } else {
            this.conditionValues.refSrcType2 = this.conditionValues.srcType2;
            this.conditionValues.refSrcId2 = this.conditionValues.scr2;
            this.conditionValues.refSrcColId2 = this.conditionValues.srcCol2;
        }
        this.dataViewsService.conditionUpdate(this.conditionValues).subscribe((res: any) => {
            this.dataViewsService.createDataView(this.dataViews.id)
                .subscribe((dataViews) => {

                });
            this.fetchDataViewDetails();
        });
    }

    cancelConditionChanges() {
        this.fetchDataViewDetails();
    }

    /* function for validation */

    saveDataViewsV() {
        let temp = [];
        var unique;
        this.dataViews.dataViewsColumnsList.forEach(element => {
            if (element.refDvName) {
                temp.push(element.refDvName);
            }
            if(element.columnName){
                element.columnName = element.columnName.replace("_", " ");
                element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
                element.columnName = element.columnName.replace("_", " ");
                element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
                element.columnName = element.columnName.replace("_", " ");
                element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
                element.columnName = element.columnName.replace("_", " ");
                element.columnName = element.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
            }
            unique = temp.filter(function (item, i, ar) { return ar.indexOf(item) === i; });
        });
        /* (unique.length > 1) || (this.dataViews.viewRelation == '' || this.dataViews.viewRelation == undefined || this.dataViews.viewRelation == null) */
        if (this.dataViews.viewRelation || !this.dataViews.viewRelation) {
            if (this.dataViews.dataViewsColumnsList) {
                let count = 0;
                let intcount = 0;
                let datecount = 0;
                let datecountint = 0;
                let currcount = 0;
                let currcountint = 0;
                let groupcount = 0;
                let groupcountint = 0;
                let datecountint1 = 0;
                let datecount1 = 0;
                let groupByCount = 0;
                for (var i = 0; i < this.dataViews.dataViewsColumnsList.length; i++) {
                    if (this.dataViews.dataViewsColumnsList[i].colDataType == '' || this.dataViews.dataViewsColumnsList[i].colDataType == null) {
                        this.hideSaveButton = false;
                        this.notificationsService.error(
                            'Warning!',
                            'Data Type is Mandatory'
                        )
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    } else if (this.dataViews.dataViewsColumnsList[i].colDataType == 'DATE') {
                        datecountint++;
                        if (this.dataViews.dataViewsColumnsList[i].qualifier == 'TRANSDATE') {
                            datecount++;
                        }
                    } else if (this.dataViews.dataViewsColumnsList[i].colDataType == 'DATETIME') {
                        datecountint1++;
                        if (this.dataViews.dataViewsColumnsList[i].qualifier == 'TRANSDATE') {
                            datecount1++;
                        }
                    } else if (this.dataViews.dataViewsColumnsList[i].groupBy) {
                        groupByCount++;
                    } else if (this.dataViews.dataViewsColumnsList[i].qualifier != '' || this.dataViews.dataViewsColumnsList[i].qualifier == undefined || this.dataViews.dataViewsColumnsList[i].qualifier == '') {
                        if (this.dataViews.dataViewsColumnsList[i].qualifier == 'AMOUNT') {
                            count++;
                            if (this.dataViews.dataViewsColumnsList[i].colDataType == 'DECIMAL') {
                                intcount++;
                            }
                        } else if (this.dataViews.dataViewsColumnsList[i].qualifier == 'CURRENCYCODE') {
                            currcount++;
                            if (this.dataViews.dataViewsColumnsList[i].colDataType == 'VARCHAR') {
                                currcountint++;
                            }
                        } else if (this.dataViews.dataViewsColumnsList[i].qualifier == 'GROUPBY_COLUMN') {
                            groupcount++;
                            if (this.dataViews.dataViewsColumnsList[i].colDataType == 'VARCHAR') {
                                groupcountint++;
                            }
                        }
                    } else if (this.dataViews.dataViewsColumnsList[i].columnName == '' || this.dataViews.dataViewsColumnsList[i].columnName == null) {
                        this.hideSaveButton = false;
                        this.notificationsService.error(
                            'Warning!',
                            'Display Name is Mandatory'
                        )
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    } else if ((this.dataViews.dataViewsColumnsList[i].operator != null) && (this.dataViews.dataViewsColumnsList[i].colValue == '' || this.dataViews.dataViewsColumnsList[i].colValue == null || this.dataViews.dataViewsColumnsList[i].colValue == undefined)) {
                        this.hideSaveButton = false;
                        this.notificationsService.error(
                            'Warning!',
                            'Value is Mandatory for selected operator'
                        )
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;


                    } else if (this.dataViews.dataViewsColumnsList[i].dataViewDisplayName == '' || this.dataViews.dataViewsColumnsList[i].dataViewDisplayName == null || this.dataViews.dataViewsColumnsList[i].dataViewDisplayName == undefined) {
                        if (!this.dataViews.dataViewsColumnsList[i].formula) {
                            this.hideSaveButton = false;
                            this.notificationsService.error(
                                'Warning!',
                                'Build Expression for newly added row'
                            )
                            this.viewValidation = false;
                            this.dataViewsPost = [];
                            delete this.dataViews.dataViewsColumnsList;
                            delete this.dataViews.conditions;
                            delete this.dataViews.basedTemplate;
                            delete this.dataViews.viewRelation;
                        }
                    } else {
                        this.viewValidation = true;
                    }
                }

                if (count > 1) {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        '"Amount" qualifier should be tagged only to one column with data type "DECIMAL"'
                    )
                    this.viewValidation = false;
                    this.dataViewsPost = [];
                    delete this.dataViews.dataViewsColumnsList;
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                } else if (count == 1) {
                    if (intcount == 1) {
                        this.viewValidation = true;
                    } else {
                        this.hideSaveButton = false;
                        this.notificationsService.error(
                            'Warning!',
                            'Data type of "Amount" qualifier should be of type "DECIMAL"'
                        )
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    }
                } else if (count < 1) {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        '"Amount" qualifier is mandatory'
                    )
                    this.viewValidation = false;
                    this.dataViewsPost = [];
                    delete this.dataViews.dataViewsColumnsList;
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                }

                if (datecountint > 0 && datecount < 1) {
                    this.hideSaveButton = false;
                    this.viewValidation = false;
                    this.notificationsService.error(
                        'Warning!',
                        '"TRANSDATE" qualifier should be tagged to data type "DATE"'
                    )
                }
                if (datecountint1 > 0 && datecount1 < 1) {
                    this.hideSaveButton = false;
                    this.viewValidation = false;
                    this.notificationsService.error(
                        'Warning!',
                        '"TRANSDATE" qualifier should be tagged to data type"DATETIME"'
                    )
                }

                if (currcount > 1) {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        '"CURRENCY" qualifier should be tagged only to one column with data type "VARCHAR'
                    )
                    this.viewValidation = false;
                    this.dataViewsPost = [];
                    delete this.dataViews.dataViewsColumnsList;
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                } else if (currcount == 1) {
                    if (currcountint == 1) {
                    } else {
                        this.hideSaveButton = false;
                        this.notificationsService.error(
                            'Warning!',
                            'Data type of "CURRENCY" qualifier should be "VARCHAR"'
                        )
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    }
                }
                if (groupcount > 1) {
                    this.hideSaveButton = false;
                    this.notificationsService.error(
                        'Warning!',
                        '"GROUPBY_COLUMN" qualifier should be tagged only to one column with data type "VARCHAR'
                    )
                    this.viewValidation = false;
                    this.dataViewsPost = [];
                    delete this.dataViews.dataViewsColumnsList;
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                } else if (groupcount == 1) {
                    if (groupcountint == 1) {
                    } else {
                        this.hideSaveButton = false;
                        this.notificationsService.error(
                            'Warning!',
                            'Data type of "GROUPBY_COLUMN" qualifier should be "VARCHAR"'
                        )
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    }
                }
                if (groupByCount == 0) {
                    this.notificationsService.error(
                        'Warning!',
                        'Atleast one "Group By" column should be selected'
                    )
                    this.hideSaveButton = false;
                    this.viewValidation = false;
                }

            }
        } else {
            this.hideSaveButton = false;
            this.notificationsService.error(
                'Warning!',
                'Atleast one column is required from selected templates'
            )
            this.viewValidation = false;
            this.dataViewsPost = [];
            delete this.dataViews.dataViewsColumnsList;
            delete this.dataViews.conditions;
            delete this.dataViews.basedTemplate;
            delete this.dataViews.viewRelation;
        }



    }



    /**Block Special Characters and space*/
    blockSpecialChar(e, prevent) {
        var k = (e.which) ? e.which : e.keyCode;
        if (prevent == 'specialChar') {
            return ((k > 64 && k < 91) || (k > 96 && k < 123) || k == 8 || (k >= 48 && k <= 57) || (k == 32) || (k == 95));
        } else if (prevent == 'space') {
            return (k > 32);
        }
    }

    /** 
     * Author: Sameer
     * Purpose: getting object as selected value, need to display selected object
    */
    tempLinesComWith(obj1, obj2) {
        return obj1.refDvColumn == obj2.refDvColumn;
    }

    /**Adding Dynamic Height to split example based on dropdown*/
    multiselectClick(val) {
        let list = $(`${val} .dropdown-list`).attr('hidden');
        if (list) {
            $(".split-example").css('min-height', (window.innerHeight - 130) + 'px');
        } else {
            if (window.innerHeight < 860) {
                $('.split-example').css('min-height', (window.innerHeight + (val == '.template-dropdown' ? 45 : 115)) + 'px');
            }
        }
    }
    
    clearUnionFunction() {
        // val, i, j, popOver
        this.custFxDialog = false;
        let fx = this.custFxDialogData;
        if(!this.unionColumnsList[fx.i].src[fx.j].isSaved && this.isCreateNew) {
            this.unionColumnsList[fx.i].src[fx.j] = null;
            this.sourceCols[fx.j][0] = {
                'refDvColumn': 'customFunction',
                'columnName': 'Custom Function'
            }
        }
        /* if (this.isCreateNew) {
            if (val && !val.isSaved) {
                this.unionColumnsList[i].src[j] = null;
                this.sourceCols[j][0] = {
                    'refDvColumn': 'customFunction',
                    'columnName': 'Custom Function'
                }
            } else {
                if (!val.excelexpressioninputUnion) {
                    this.unionColumnsList[i].src[j] = null;
                    this.sourceCols[j][0] = {
                        'refDvColumn': 'customFunction',
                        'columnName': 'Custom Function'
                    }
                }
            }
        } else {
            if (val && !val.isSaved) {
                this.unionColumnsList[i].src[j] = null;
                this.sourceCols[j][0] = {
                    'refDvColumn': 'customFunction',
                    'columnName': 'Custom Function'
                }
            }
            if (val.formula) {
                val.excelexpressioninputUnion = val.formula;
                val.excelExpressionUnion = null;
            }
        }
        popOver.hide(); */
    }

    custFxDialog: boolean = false;
    custFxDialogData: any;
    showFxDialog(data, i, j) {
        if (data && data.refDvColumn == 'customFunction') {
            this.custFxDialog = true;
            this.custFxDialogData = data;
            this.custFxDialogData.i = i;
            this.custFxDialogData.j = j;
            if (data.isSaved || data.excelexpressioninputUnion) {
                data['selFx'] = {
                    'excelexpressioninputUnion' : data.excelexpressioninputUnion
                }
            }
        } else if(data && data.refDvColumn == 'none') {
            data.refDvType = this.selectedTemplates[j].type;
            data.refDvName = this.selectedTemplates[j].typeId;
            data.intermediateId = this.selectedTemplates[j].intermediateId;
        }
    }


    copyColumn(val) {
        let selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = val.columnName ? val.columnName : val.columnHeader;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy');
        document.body.removeChild(selBox);
    }

    checkTempColumnName(name, i) {
        let count = this.unionColumnsList.filter(uninEach => uninEach.columnName == name).length;
        if (count > 1) {
            this.notificationsService.error('Warning!', 'Display name should be unique');
            setTimeout(() => {
                this.unionColumnsList[i].columnName = null;
            }, 100);
        }
    }

    qualifierChange(qualifier, i, arr) {
        if (qualifier) {
            let count = [];
            arr.map(each => {
                if (each.qualifier == qualifier) {
                    count.push(each);
                }
            });
            if (count.length > 1) {
                this.notificationsService.error('Warning!', 'Qualifier already used');
                setTimeout(() => {
                    arr[i].qualifier = null;
                }, 100);
            }
        }
    }

}