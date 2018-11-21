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

import { Component, OnInit, OnDestroy, Inject, ViewChild } from '@angular/core';
import { Response } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Subject } from 'rxjs/Rx';
import { DataViews } from './data-views.model';
import { DataViewsService } from './data-views.service';
import { CommonService } from '../common.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { ITEMS_PER_PAGE } from '../../shared';
import { JhiDateUtils } from 'ng-jhipster';
declare var $: any;
@Component({
    selector: 'jhi-data-views-detail',
    templateUrl: './data-views-detail.component.html'
})
export class DataViewsDetailComponent implements OnInit, OnDestroy {
    dataViews = new DataViews();
    fileTemplateList = [];
    operatorsList = [];
    dataTypeList = [];
    isCreateNew = false;
    isViewOnly = false;
    presentPath: any;
    isEdit = false;
    colField = false;
    showSelectedCol = false;
    savedCol = false;
    addRowIcon = true;
    dropdownList = [];
    selItems = [];
    selectedColumns = [];
    dropdownSettings = {};
    limitSelectionSettings:any = {};
    basicExampleSettings: any = {};
    dropDownColumnList = [];
    dataViewsColumnsList:any = [];
    colTemplList: any = [];
    dataViewsPost: any = [];
    newData = false;
    filter = false;
    duplicateViewName: any;
    excelFunctions = [];
    qualifierList = [];
    viewRelations = [];
    selectedTemplates = [];
    viewRelation: any;
    sourceRelation: any;
    basedTemplate: any;
    conditionList1: any;
    conditionList2: any;
    sourceName1: any;
    sourceName2: any;
    conditionValues: any = {};
    conditionsList: any = [];
    conditionEdit = false;
    sourceClick = false;
    targetClick = false;
    unionColumnsList = [];
    sourceCols: any = [];
    targetCols: any = [];
    unionTargetCol = [];
    conditionOperatorss: any;
    viewValidation = false;
    qualifierList1: any = [];
    sourceTemplate: any = [];
    sourceColumns: any = [];
    sourceColumns1: any = [];
    columnsTemp: any = [];
    hideSaveButton = false;
    buttonFunction = true;
    groupByChecked;
    disblRelation = false;
    operatorss = [
        { value: '=', meaning: 'Equals' }
    ];
    statuses = [{code: true,value: 'Active'},{code: false,value: 'Inactive'}];
    loadDocument = false;
    testAr = [];
    saveFlag: any;
    dispCopyIcon = false;
    isCustFxErrorSinJoin: any;
    isCustFxError: any;
    custFxDialog = false;
    custFxDialogData: any;
    isCopy = false;
    unsubscribe: Subject<void> = new Subject();
    disColDrpDwn = false;
    lastRelation: any;
    currentDate = new Date();
    selectedViewName: any;
    viewColumnOptions = [];
    viewTableColumns = [];
    selectedView: any;
    page1 = 0;
    viewitemsPerPage: any;
    selectedViewRecordsLength: number;
    display = false;
    viewsList:any = [];
    pageSizeOptions = [10, 25, 50, 100];
    cond1Search;
    cond2Search;
    @ViewChild('basedTemplRadio') basedTemplRadio;
    @ViewChild('viewRelationVar') viewRelationVar;

    constructor(
        private dataViewsService: DataViewsService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        public dialog: MdDialog,
        public overlayContainer: OverlayContainer,
        private dateUtils: JhiDateUtils,
    ) {
        this.viewitemsPerPage = ITEMS_PER_PAGE;
    }

    ngOnInit() {
        this.showSelectedCol = false;
        this.getFileTemplateAndDataViews();

        this.dropdownSettings = {
            singleSelection: false,
            text: "Select Countries",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true
        };
        this.limitSelectionSettings = {
            text: "",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true,
            badgeShowLimit: 1,
            disabled: false,
            classes: 'template-dropdown'
        };
        this.basicExampleSettings = {
            text: "",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true,
            disabled: false,
            badgeShowLimit: 1,
            classes: 'columns-dropdown'
        }
        // Fetching Excel Function List
        this.dataViewsService.operators('FUNCTIONS').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.excelFunctions = res;
        });
        // Fetching Operators List
        this.dataViewsService.operators('All').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.operatorsList = res;
        });
        // Fetching Data Type List
        this.dataViewsService.operators('DATA_TYPE').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.dataTypeList = res;
        });
        // Fetching Qualifiers List
        this.dataViewsService.operators('RECON_QUALIFIERS').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.qualifierList = res;
        });
        // Fetching Relations List
        this.dataViewsService.operators('DV_RELATION').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.viewRelations = res;
        });
        // Calling Function "fetchDataViewDetails" to fetch route path and get details accordingly
        this.fetchDataViewDetails();
    }

    /* FUNCTION 3 - Calling Function "fetchDataViewDetails" to fetch route path and get details accordingly
       Author: AMIT 
    */
    fetchDataViewDetails() {
        this.dataViewsColumnsList = [];
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (params['id']) {
                this.sourceTemplate = [];
                this.dataViewsColumnsList = [];
                this.conditionOperatorss = "=";
                this.dataViewsService.getDVById(params['id']).takeUntil(this.unsubscribe).subscribe(
                    (dataViews) => {
                        this.dataViews = dataViews[0];
                        this.dataViews.startDate = this.dateUtils.convertDateTimeFromServer(this.dataViews.startDate);
                        if (this.dataViews.endDate) {
                            this.dataViews.endDate = this.dateUtils.convertDateTimeFromServer(this.dataViews.endDate);
                        }
                        this.isCreateNew = false;
                        this.loadDocument = true;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                        } else {
                            this.isViewOnly = true;
                            this.dataViews.description = !this.dataViews.description ? '-' : this.dataViews.description;
                            this.dataViews.endDate = !this.dataViews.endDate ? '-' : this.dataViews.endDate;
                        }
                        // Three types of views "Single Template", "UNION" and "JOIN"
                        this.sourceRelation = this.viewRelation = this.lastRelation = this.dataViews.viewRelation;
                        if (this.dataViews.viewRelation != "UNION") {
                            this.showSelectedCol = true;
                            if(this.dataViews.dataViewsColumnsList) {
                                for (let i = 0; i < this.dataViews.dataViewsColumnsList.length; i++) {
                                    this.dataViewsColumnsList.push(this.dataViews.dataViewsColumnsList[i]);
                                    this.showQualifier(this.dataViews.dataViewsColumnsList[i].colDataType, i);
                                }
                            }
                            if(this.dataViews.templateInfo) {
                                this.selectedTemplates = this.dataViews.templateInfo; 
                                for (let x = 0; x < this.dataViews.templateInfo.length; x++) {
                                    const obj = [{
                                        "type": this.dataViews.templateInfo[x].type,
                                        "typeId": this.dataViews.templateInfo[x].typeId
                                    }];
                                    this.dataViewsService.dataViewColTempList(obj).takeUntil(this.unsubscribe).subscribe(
                                        (res: Response) => {
                                            this.columnsTemp = res;
                                            for (let j = 0; j < this.columnsTemp.length; j++) {
                                                this.columnsTemp[j]['refDvColumn'] = this.columnsTemp[j].id;
                                                this.columnsTemp[j]['refDvName'] = this.columnsTemp[j].dataViewId;
                                                this.columnsTemp[j]['sourceName'] = this.columnsTemp[j].dataViewDisplayName;
                                                this.sourceColumns.push(this.columnsTemp[j]);
                                            }
                                            const temp = {
                                                "sourceName": this.columnsTemp[0].dataViewDisplayName,
                                                "refDvName": this.columnsTemp[0].dataViewId
                                            }
                                            this.sourceTemplate.push(temp);
                                        });
                                }
                            }
                            if (this.dataViews.conditions && this.dataViews.conditions.length > 0) {
                                let obj1;
                                let obj2;
                                for (let j = 0; j < this.dataViews.conditions.length; j++) {
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
                                this.dataViewsService.dataViewColTempList(obj1).takeUntil(this.unsubscribe).subscribe(
                                    (res: Response) => {
                                        this.conditionList1 = res;
                                        for (let i = 0; i < obj1.length; i++) {
                                            for (let j = 0; j < this.conditionList1.length; j++) {
                                                if (obj1[i].id == this.conditionList1[j].id) {
                                                    this.dataViews.source = this.conditionList1[j].id;
                                                    this.sourceName1 = this.conditionList1[j].templateName;
                                                }
                                                this.conditionList1[j]['refDvColumn'] = this.conditionList1[j].id;
                                                this.conditionList1[j]['refDvName'] = this.conditionList1[j].dataViewId;
                                                this.conditionList1[j]['sourceName'] = this.conditionList1[j].dataViewDisplayName;
                                            }
                                        }
                                        const temp = {
                                            "sourceName": this.conditionList1[0].dataViewDisplayName,
                                            "refDvName": this.conditionList1[0].dataViewId
                                        }
                                    });
                                this.dataViewsService.dataViewColTempList(obj2).takeUntil(this.unsubscribe).subscribe(
                                    (res: Response) => {
                                        this.conditionList2 = res;
                                        for (let i = 0; i < obj2.length; i++) {
                                            for (let j = 0; j < this.conditionList2.length; j++) {
                                                if (obj2[i].id == this.conditionList2[j].id) {
                                                    this.dataViews.target = this.conditionList2[j].id;
                                                    this.sourceName2 = this.conditionList2[j].templateName;
                                                }
                                                this.conditionList2[j]['refDvColumn'] = this.conditionList2[j].id;
                                                this.conditionList2[j]['refDvName'] = this.conditionList2[j].dataViewId;
                                                this.conditionList2[j]['sourceName'] = this.conditionList2[j].dataViewDisplayName;
                                            }
                                        }
                                        const temp = {
                                            "sourceName": this.conditionList2[0].dataViewDisplayName,
                                            "refDvName": this.conditionList2[0].dataViewId
                                        }
                                    });
                            }
                        } else if (this.dataViews.viewRelation == "UNION") {
                            this.showSelectedCol = false;
                            this.unionColumnsList = this.dataViews.dataViewsUnionColumnsList;
                            this.unionColumnsList.forEach((uniColEach) => {
                                uniColEach.templSearch = [];
                            });
                            if (this.unionColumnsList.length) {
                                this.selectedTemplates = [];
                                this.unionColumnsList[0].src.forEach((uninSrcEach) => {
                                    this.selectedTemplates.push({
                                        dataName: uninSrcEach.dataName,
                                        type: uninSrcEach.refDvType,
                                        typeId: uninSrcEach.refDvName,
                                        intermediateId: uninSrcEach.intermediateId
                                    });
                                });
                            }
                            if(this.selectedTemplates.length) {
                                this.dataViewsService.getTemplateLines(this.selectedTemplates).takeUntil(this.unsubscribe).subscribe((res) => {
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
                            for (let i = 0; i < this.dataViews.dataViewsUnionColumnsList.length; i++) {
                                this.showQualifier(this.dataViews.dataViewsUnionColumnsList[i].colDataType, i);
                            }
                        }
                    }
                );
            } else {
                $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").css('min-height', (window.innerHeight - 330) + 'px');
                this.isCreateNew = true;
                this.loadDocument = true;
                this.dataViews.startDate = new Date();
                $(window).on('click', function(e) {
                    const elem1 = document.querySelector('.template-dropdown');
                    const elem2 = document.querySelector('.columns-dropdown');
                    if ((elem1 && !elem1.contains(e.target)) && (elem2 && !elem2.contains(e.target))) {
                        $(".split-example").css('min-height', (window.innerHeight - 130) + 'px');
                        $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").css('min-height', (window.innerHeight - 330) + 'px');
                    } else if (!elem2 && (elem1 && !elem1.contains(e.target))) {
                        $(".split-example").css('min-height', (window.innerHeight - 130) + 'px');
                        $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").css('min-height', (window.innerHeight - 330) + 'px');
                    }
                });
            }
        });
    }

    

    /* FUNCTION 4 - Checking duplicate view name
       Author: AMIT 
    */
    checkViewName(val, id) {
        this.dataViews.dataViewDispName = val = val ? val.trim() : val;
        if (!val) return;
        this.dataViewsService.checkDataViewIsExist(val, id).takeUntil(this.unsubscribe).subscribe((res) => {
            this.duplicateViewName = res.result != 'No Duplicates Found' ? res.result : undefined;
        }, () => this.commonService.error('Warning!', 'Error occured while checking source name'));
    }

    /* FUNCTION 5 - Function calling on view select and deselect
       Author: AMIT 
    */
    onSelectTemplateAndDataView(item, evt, type) {
        let furtherFlag = false;
        // If Deselected check for id, intermediate id and remove selected from main array
        if (evt) {
            if(type && type == 'deselect' && this.viewRelation == 'UNION') {
                this.unionColumnsList.forEach((col) => {
                    col.src.forEach((srcCol, i) => {
                        if (evt.intermediateId) {
                            if (srcCol && srcCol.intermediateId == evt.intermediateId) {
                                col.src.splice(i, 1);
                            }
                        } else if (srcCol && srcCol.refDvName == evt.typeId) {
                            col.src.splice(i, 1);
                        }
                    });
                });
                if(this.isCopy) {
                    this.getTepmlateLinesUnion(item);
                    this.emptyOtherthanUnion();
                }
            } else if(type && type == 'select' && item.length > 5) {
                // Preventing user from selecting more than 5 templates
                item.forEach((each, i) => {
                   if(each.id == evt.id) {
                        item.splice(i, 1);
                        furtherFlag = true;
                   } 
                });
            }
        }
        if(furtherFlag) return;

        this.dropDownColumnList = [];
        this.dataViewsColumnsList = [];
        this.selectedColumns = [];
        this.dataViews.srcMapping = this.selectedTemplates = item;
        this.viewRelation = undefined;
        this.sourceRelation = undefined;
        this.basedTemplate = '';
        this.dataViews.source = '';
        this.dataViews.target = '';
        this.conditionList1 = [];
        this.conditionList2 = [];
        // If colField is true showing field to select view relation else select column field
        if (item.length == 1) {
            this.colField = true;
            this.dataViews.viewRelation = undefined;
            if(this.isCopy) {
                this.unionColumnsList = [];
                this.sourceCols = [];
                this.viewRelation = undefined;
                this.sourceRelation = undefined;
            }
        } else if (item.length == 2) {
            this.disblRelation = false;
            this.colField = false;
            this.selectedColumns = [];
            this.unionColumnsList = [];
        } else {
            this.sourceRelation = this.viewRelation = this.viewRelations[1].lookUpCode;
            this.disblRelation = true;
        }

        // Calling service function "dataViewColTempList" to fetch selected view columns
        if (item.length > 1) {
            if (this.viewRelation == 'UNION'){
                this.getTepmlateLinesUnion(item);
                this.emptyOtherthanUnion();
            }
        } else {
            this.getTepmlateLinesSingleJoin(item);
        }
    }


    getTepmlateLinesUnion(item) {
        this.dataViewsService.getTemplateLines(item).takeUntil(this.unsubscribe).subscribe((res) => {
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
        this.dropDownColumnList = [];
        this.dataViewsService.dataViewColTempList(item).takeUntil(this.unsubscribe).subscribe((res: Response) => {
            this.colTemplList = res;
            let count = 1;
            for (let i = 0; i < this.colTemplList.length; i++) {
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace("_", " ");
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace("_", " ");
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace("_", " ");
                this.colTemplList[i].columnName = this.colTemplList[i].columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
                this.colTemplList[i]['refDvColumn'] = this.colTemplList[i].id;
                this.colTemplList[i]['refDvName'] = this.colTemplList[i].dataViewId;
                this.colTemplList[i].columnEdit = true;
                let occured = false;
                for (let j = 0; j < item.length; j++) {
                    if (this.colTemplList[i].dataViewId == item[j].typeId) {
                        occured = true;
                    }
                }
                if (occured) {
                    const obj = {
                        "id": count++,
                        "itemName": this.colTemplList[i].columnHeader + " - " + this.colTemplList[i].dataViewDisplayName,
                        "selColName": this.colTemplList[i].columnHeader,
                        "colId": this.colTemplList[i].dataViewId,
                        "columnrefDvColumn": this.colTemplList[i].refDvColumn,
                        "intermediateId": this.colTemplList[i].intermediateId
                    }
                    this.dropDownColumnList.push(obj);
                }
            }
            if (this.isCopy) {
                this.selectedColumns = [];
                this.dropDownColumnList.forEach((ddColEach) => {
                    this.dataViews.dataViewsColumnsList.forEach((dvColEach) => {
                        if (dvColEach.refDvColumn == ddColEach.columnrefDvColumn) {
                            this.selectedColumns.push(ddColEach);
                        }
                    });
                });
                if(this.basedTemplRadio) {
                    this.basedTemplRadio.nativeElement.children[0].querySelector('input').click();
                }
            }
        });
    }



    /* FUNCTION 6 - Function calling on selection of relation
       Author: AMIT 
    */
    relationFun(rel: any) {
        this.dataViews.viewRelation = rel;
        if(this.isCopy && this.viewRelation == this.viewRelations[1].lookUpCode) return;
        this.qualifierList1 = [];
        if (rel == 'UNION') {
            this.getTepmlateLinesUnion(this.selectedTemplates);
            this.emptyOtherthanUnion();
            this.unionColumnsList = [];
            const obj = {
                "columnEdit": true,
                "src": [],
                "templSearch": []
            }
            this.unionColumnsList.push(obj);
        } else {
            this.basedTemplate = this.selectedTemplates[0];
            const conOne = [];
            const conTwo = [];
            conOne.push(this.selectedTemplates[0]);
            conTwo.push(this.selectedTemplates[1]);
            this.dataViewsService.dataViewColTempList(conOne).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                this.conditionList1 = res;
                this.sourceName1 = this.conditionList1[0].templateName;
            });
            this.dataViewsService.dataViewColTempList(conTwo).takeUntil(this.unsubscribe).subscribe((res: Response) => {
                this.conditionList2 = res;
                this.sourceName2 = this.conditionList2[0].templateName;
            });
            this.getTepmlateLinesSingleJoin(this.selectedTemplates);
            delete this.dataViews.unionColumnsList;
            delete this.unionColumnsList;
            this.sourceCols = [];
        }
    }

    emptyOtherthanUnion() {
        delete this.dataViews.dataViewsColumnsList;
        delete this.dataViews.basedTemplate;
        delete this.dataViews.conditions;
        this.selectedColumns = [];
        this.dataViewsColumnsList = [];
        this.basedTemplate = '';
        if (!this.unionColumnsList || !this.unionColumnsList.length) {
            this.unionColumnsList = [];
            const obj = {
                "columnEdit": true,
                "src": [],
                "templSearch": []
            }
            this.unionColumnsList.push(obj);
        }
    }

    /* FUNCTION 7 - Function calling on selection of view column
       Author: AMIT 
    */
    onColumnSelect(item, evt) {
        this.filter = false;
        for (let j = 0; j < this.colTemplList.length; j++) {
            if (!this.isCopy) {
                for (let i = 0; i < item.length; i++) {
                    if (item[i].colId == this.colTemplList[j].dataViewId &&
                        item[i].selColName == this.colTemplList[j].columnHeader &&
                        item[i].columnrefDvColumn == this.colTemplList[j].refDvColumn) {
                        this.colTemplList[j].colName = this.colTemplList[j].columnHeader;
                        this.colTemplList[j]['sourceName'] = this.colTemplList[j].dataViewDisplayName;
                        this.dataViewsColumnsList.push(this.colTemplList[j]);
                    }
                }
            } else if(this.isCopy) {
                if(evt.length > 1) {
                    evt.forEach((evtEach) => {
                        if (evtEach.columnrefDvColumn == this.colTemplList[j].refDvColumn) {
                            this.colTemplList[j].colName = this.colTemplList[j].columnHeader;
                            this.colTemplList[j]['sourceName'] = this.colTemplList[j].dataViewDisplayName;
                            this.dataViewsColumnsList.push(this.colTemplList[j]);
                        } 
                    });
                    this.dataViewsColumnsList = Array.from(this.dataViewsColumnsList.reduce((m, t) => m.set(parseInt(t.refDvColumn), t), new Map()).values());
                } else {
                    if (evt.columnrefDvColumn == this.colTemplList[j].refDvColumn) {
                        this.colTemplList[j].colName = this.colTemplList[j].columnHeader;
                        this.colTemplList[j]['sourceName'] = this.colTemplList[j].dataViewDisplayName;
                        this.dataViewsColumnsList.push(this.colTemplList[j]);
                    }
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
    OnColumnDeSelect(item, evt) {
        this.filter = false;
        if (!this.isCopy) {
            this.dataViewsColumnsList.forEach((dvColEach, i) => {
                if (evt.columnrefDvColumn == dvColEach.refDvColumn) {
                    this.qualifierList1.splice(i, 1);
                }
            });
            this.dataViewsColumnsList = this.dataViewsColumnsList.filter(function(o1) {
                return item.some(function(o2) {
                    return (o1.dataViewId === o2.colId && o1.columnHeader === o2.selColName) || (o1.dataViewId === undefined && o1.columnHeader === undefined); // return the ones with equal id
                });
            });
        } else if (this.isCopy) {
            if (Array.isArray(evt)) {
                // DeSelect All
                this.dataViewsColumnsList = [];
            } else if(evt) {
                // DeSelect Single Item
                this.dataViewsColumnsList.forEach((dvColEach, i) => {
                    if (evt.columnrefDvColumn == dvColEach.refDvColumn) {
                        this.dataViewsColumnsList.splice(i, 1);
                        this.qualifierList1.splice(i, 1);
                    }
                });
            }
        }
    }

    /* Function to display operator based on selected data type */
    showOperator(val, ind) {
        this.operatorsList[ind] = this.operatorsList.filter((item) => item.lookUpType == val);
    }

    showQualifier(val, ind) {
        for (let i = 0; i < this.qualifierList.length; i++) {
            if (val == "DECIMAL" && this.qualifierList[i].lookUpCode == 'AMOUNT') {
                this.qualifierList[i]['quali'] = val;
            } else if (val == "VARCHAR" && (this.qualifierList[i].lookUpCode == 'CURRENCYCODE' || this.qualifierList[i].lookUpCode == 'GROUPBY_COLUMN')) {
                this.qualifierList[i]['quali'] = val;
            } else if ((val == "DATE" || val == "DATETIME") && (this.qualifierList[i].lookUpCode == 'TRANSDATE'  || this.qualifierList[i].lookUpCode == 'FILEDATE')) {
                this.qualifierList[i]['quali'] = val;
            } else if (val == "BOOLEAN" || val == "INTEGER") {

            }
        }
        this.qualifierList1[ind] = this.qualifierList.filter((item) => item.quali == val);
    }

    displayColumns(id: any, ind: any) {
        this.sourceColumns1 = [];
        this.sourceColumns1 = this.sourceColumns.filter((item) => item.refDvName == id);
        for (let i = 0; i < this.dataViewsColumnsList.length; i++) {
            for(let j=0;j<this.sourceColumns1.length;j++){
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
        for (let i = 0; i < this.dataViewsColumnsList.length; i++) {
            if (ind == i) {
                this.dataViewsColumnsList[ind]['columnName'] = col.columnName;
            }
        }
    }

    listOperatorFun() {
        if (this.dataViewsColumnsList != null || this.dataViewsColumnsList != undefined) {
            for (let i = 0; i < this.dataViewsColumnsList.length; i++) {
                this.showOperator(this.dataViewsColumnsList[i].colDataType, i);
            }
        }
    }

    /* Function to get file templates and data views list */
    getFileTemplateAndDataViews() {
        let count = 1;
        this.dataViewsService.fileTemplateList('DATA_VIEW').takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.fileTemplateList = res;
            for (let i = 0; i < this.fileTemplateList.length; i++) {
                if (this.fileTemplateList[i].status == 'Active') {
                    const obj = {
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

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
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
            const qualifierValidate: any = [];
            this.dataViewsColumnsList.forEach((colEach) => {
                qualifierValidate.push(colEach.qualifier);
            });

            if (!qualifierValidate.includes('AMOUNT')) {
                this.commonService.error('Warning!', '"Amount" qualifier is mandatory');
                val.columnEdit = true;
            } else if (!qualifierValidate.includes('TRANSDATE')) {
                this.commonService.error('Warning!', '"Date" qualifier is mandatory');
                val.columnEdit = true;
            } else if (!qualifierValidate.includes('CURRENCYCODE')) {
                this.commonService.error('Warning!', '"Currency" qualifier is mandatory');
                val.columnEdit = true;
            } else {

                if (val.dataViewId == undefined) {
                    val.dataViewId = this.dataViews.id;
                    if (val.refDvName != null) {
                        val.refDvType = "File Template";
                    }
                }
            }
            val.columnName = val.columnName.replace("_", " ");
            val.columnName = val.columnName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
            this.dataViewsService.updateDataViewCol(val)
            .takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.buttonFunction = true;
                    this.dataViewsService.createDataViewPost(this.dataViews.id, res).subscribe((dataViews) => {});
                    this.dataViewsColumnsList = [];
                    this.commonService.success('Success!','Column Data Successfully Updated');
                    this.fetchDataViewDetails();
                }
                );
        }
    }
    updateUnionColumnData(val: any, ind: any) {
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
        val.columnName = val.columnName.replace("_", " ");
        val.columnName = val.columnName.replace(/\w\S*/g, function(txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); });
        const qualifierValidate: any = [];
        this.unionColumnsList.forEach((unionEach) => {
            qualifierValidate.push(unionEach.qualifier);
        });

        if (!qualifierValidate.includes('AMOUNT')) {
            this.commonService.error('Warning!', '"Amount" qualifier is mandatory');
            val.columnEdit = true;
        } else if (!qualifierValidate.includes('TRANSDATE')) {
            this.commonService.error('Warning!', '"Date" qualifier is mandatory');
            val.columnEdit = true;
        } else if(!qualifierValidate.includes('CURRENCYCODE')) {
            this.commonService.error('Warning!', '"Currency" qualifier is mandatory');
            val.columnEdit = true;
        } else {
            if (val.id != null) {
                this.dataViewsService.updateUnionDataViewCol(val).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.buttonFunction = true;
                    const obj = res;
                    this.dataViewsService.createDataViewPost(this.dataViews.id, obj).subscribe((dataViews) => {});
                    this.dataViewsColumnsList = [];
                    this.commonService.success('Success!','Column Data Successfully Updated');
                    this.fetchDataViewDetails();
                });
            } else {
                val.dataViewId = this.dataViews.id;
                this.dataViewsService.updateUnionDataViewCol(val)
                .takeUntil(this.unsubscribe).subscribe((res: any) => {
                        this.buttonFunction = true;
                        const obj = res;
                        this.dataViewsService.createDataViewPost(this.dataViews.id, obj).subscribe((dataViews) => {});
                        this.dataViewsColumnsList = [];
                        this.commonService.success('Success!','Column Data Successfully Updated');
                        this.fetchDataViewDetails();
                    });
            }
        }
    }

    deleteColumnNew(val, ind) {
        if (this.dataViewsColumnsList.length > 1) {
            const temp = [];
            if (val.id) {
                for (let a = 0; a < this.selectedColumns.length; a++) {
                    if (this.selectedColumns[a].colId == val.refDvName && this.selectedColumns[a].selColName == val.columnHeader) {
                        this.selectedColumns.splice(a, 1);
                    }
                }
            }

            for (let i = 0; i < this.dataViewsColumnsList.length; i++) {
                if (ind == i) {
                    this.dataViewsColumnsList[i].colDataType = '';
                    delete this.qualifierList1[ind];
                    this.dataViewsColumnsList.splice(this.dataViewsColumnsList.indexOf(val), 1);
                }
            }
            for (let j = 0; j < this.qualifierList1.length; j++) {
                if (this.qualifierList1[j]) {
                    temp.push(this.qualifierList1[j]);
                }
            }
            this.qualifierList1 = temp;
        } else {
            this.commonService.error('Warning!', 'Atleast one column is mandatory');
        }
    }

    deleteColumn(val) {
        if (this.dataViewsColumnsList.length > 1) {
            if (val.qualifier == 'CURRENCYCODE' || val.qualifier == 'AMOUNT' || val.qualifier == 'TRANSDATE') {
                this.commonService.error('Warning', 'This qualifier column is mandatory');
            } else {
                this.dataViewsService.deleteDataViewCol(val.id).takeUntil(this.unsubscribe).subscribe((response) => {
                    this.dataViewsColumnsList.splice(this.dataViewsColumnsList.indexOf(val), 1);
                    this.dataViewsService.createDataViewPost(this.dataViews.id, response).subscribe((dataViews) => { });
                    this.commonService.success('Success!', 'Column Deleted Successfully');
                });
            }
        } else {
            this.commonService.error('Warning!', 'Atleast one column is mandatory');
        }

    }

    deleteUnionColumnNew(val, ind) {
        if (this.unionColumnsList.length > 1) {
            const temp = [];
            for (let i = 0; i < this.unionColumnsList.length; i++) {
                if (ind == i) {
                    this.unionColumnsList[i].colDataType = '';
                    delete this.qualifierList1[ind];
                    this.unionColumnsList.splice(this.unionColumnsList.indexOf(val), 1);
                }
            }
            for (let j = 0; j < this.qualifierList1.length; j++) {
                if (this.qualifierList1[j]) {
                    temp.push(this.qualifierList1[j]);
                }
            }
            this.qualifierList1 = temp;
        } else {
            this.commonService.error('Warning!', 'Atleast one column is mandatory');
        }
    }

    deleteUnionColumn(val) {
        if (this.unionColumnsList.length > 1) {
            if (val.qualifier == 'CURRENCYCODE' || val.qualifier == 'AMOUNT' || val.qualifier == 'TRANSDATE') {
                this.commonService.error('Warning', 'This qualifier column is mandatory');
            } else {
                this.dataViewsService.deleteUnionDataViewCol(val.id).takeUntil(this.unsubscribe).subscribe((response) => {
                    const result: any = response;
                    this.dataViewsService.createDataViewPost(this.dataViews.id, result).subscribe((dataViews) => { });
                    this.fetchDataViewDetails();
                    this.commonService.success('Success!', 'Column Deleted Successfully');
                });
            }
        } else {
            this.commonService.error('Warning!', 'Atleast one column is mandatory');
        }

    }

    addUnionColumn() {
        let count = 0;
        let editValidation = false;
        for (let i = 0; i < this.unionColumnsList.length; i++) {
            if (this.unionColumnsList[i].columnName == '' || this.unionColumnsList[i].colDataType == '' ||
                this.unionColumnsList[i].columnName == undefined || this.unionColumnsList[i].colDataType == undefined ||
                this.unionColumnsList[i].src.length != this.selectedTemplates.length) {
                count++;
            }
            if (this.unionColumnsList[i].columnEdit) {
                editValidation = true;
            }
        }
        if (count > 0 &&  !this.isViewOnly) {
            this.commonService.error('Warning!','Fill mandatory fields to add another column');
        } else if(editValidation && this.isViewOnly) {
            this.commonService.error('Warning!','Please save column');
        } else {
            const obj = {
                "columnEdit": true,
                "src": [],
                "templSearch": []
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
        this.unionColumnsList.forEach((element) => {
            if (element.id == undefined) {
                this.unionColumnsList.splice(ind, 1);
            }
        });
        this.fetchDataViewDetails();
    }
    cancelUnionColumnChanges(ind: any) {
        this.dataViewsColumnsList.forEach((element) => {
            if (element.id == undefined) {
                this.dataViewsColumnsList.splice(ind, 1);
            }
        });

        this.fetchDataViewDetails();
    }
    addColumn() {
        let count = 0;
        let viewVald = false;
        for (let i = 0; i < this.dataViewsColumnsList.length; i++) {
            if (this.dataViewsColumnsList[i].columnName == '' || this.dataViewsColumnsList[i].colDataType == '' ||
                this.dataViewsColumnsList[i].columnName == undefined || this.dataViewsColumnsList[i].colDataType == undefined) {
                count++;
            }
            if (this.dataViewsColumnsList[i].columnEdit) {
                viewVald = true;
            }
        }
        if (count > 0 && !this.isViewOnly) {
            this.commonService.error('Warning!','Fill mandatory fields to add another column');
        } else if(viewVald && this.isViewOnly) {
            this.commonService.error('Warning!', 'Please save column');
        } else {
            const newLine = {
                "columnEdit": true
            };
            this.dataViewsColumnsList.push(newLine);
            this.filter = false;
        }
    }

    /* Function to show/hide filter columns */
    applyFilter() {
        let count = 0;
        this.dataViewsColumnsList.forEach((element) => {
            if (element.colDataType === null || element.colDataType === undefined || element.colDataType === '') {
                count++;
            }
        });
        if (count > 0) {
            this.commonService.error('Warning!','Data Type is mandatory to apply filter');
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
    /* Function to save and update data view */
    saveDataViews() {
        this.saveFlag = undefined;
        this.hideSaveButton = true;
        const count = 0;
        const count1 = 0;
        if (!this.isCopy) {
            this.dataViews['viewRelation'] = this.viewRelation;
        }
        this.dataViews['basedTemplate'] = this.basedTemplate;
        let link: any = '';

        if (this.dataViews) {
            if (!this.dataViews.viewRelation || (this.dataViews.viewRelation && this.dataViews.viewRelation != 'UNION')) {
                const betweenOperRegex = new RegExp('^.+,.+$');
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
            if (this.isCopy) {
                this.dataViews.copyId = this.dataViews.id;
                this.dataViews.id = undefined;
            }
            if (this.dataViews.id !== undefined) {
                this.dataViewsPost = [];
                this.dataViewsPost.push(this.dataViews);
                delete this.dataViewsPost[0].dataViewsColumnsList;
                delete this.dataViewsPost[0].dataViewsUnionColumnsList;
                this.dataViewsService.postDataViews(this.dataViewsPost)
                .takeUntil(this.unsubscribe).subscribe((dataViews) => {
                        this.dataViews = dataViews;
                        this.commonService.success('Success!','Successfully Updated Data Source');
                        this.hideSaveButton = false;
                        if (this.dataViews.id) {
                            link = ['/data-views', { outlets: { 'content': this.dataViews.id + '/details' } }];
                            this.router.navigate(link);
                        }
                        this.dataViewsService.createDataView(this.dataViews.id).subscribe(() => {});
                    });
            } else {
                if ((this.dataViews.viewRelation === null || this.dataViews.viewRelation === undefined || this.dataViews.viewRelation === '') &&
                    (this.dataViews.basedTemplate === null || this.dataViews.basedTemplate === undefined || this.dataViews.basedTemplate === '')) {
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                } else {
                    this.conditionsList = [];
                    this.conditionsList.push(this.conditionValues);
                    this.dataViews['conditions'] = this.conditionsList;
                }
    
                if (this.dataViews.viewRelation != 'UNION') {
                    this.dataViews['dataViewsColumnsList'] = this.dataViewsColumnsList;
                } else {
                    let index = -1;
                    for (let i = 0; i < this.unionColumnsList.length; i++) {
                        this.testAr = [];
                        index++;
                        this.unionColumnsList[i]['index'] = index;
                        for (let j = 0; j < this.sourceCols.length; j++) {
                            for (let x = 0; x < this.targetCols.length; x++) {
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
                this.dataViewsPost = [];
                this.dataViewsPost.push(this.dataViews);
                if (this.dataViews.viewRelation != 'UNION') {
                    this.saveDataViewsV();
                } else {
                    this.viewValidation = true;
                }
    
                if (this.viewValidation == true) {
                    this.dataViewsService.postDataViews(this.dataViewsPost)
                    .takeUntil(this.unsubscribe).subscribe((dataViews) => {
                            this.dataViews = dataViews;
                            this.dataViewsService.createDataView(this.dataViews.id).subscribe(() => {});
                            this.commonService.success('Success!','Successfully Data Source Created');
                            this.hideSaveButton = false;
                            if (this.dataViews.id) {
                                if(this.isCopy) {
                                    this.router.navigate(['/data-views', { outlets: {'content': ['All']+'/list'} }]);
                                    return;
                                }
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
                        this.commonService.error('Error!', 'Something Went Wrong');
                        this.hideSaveButton = false;
                    });
                }
    
    
            }
        }

    }

    saveDataViewsU() {
        this.unionColumnsList.forEach((element) => {
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
            let currencyValid = 0;
            let dateValid = 0;
            for (let i = 0; i < this.unionColumnsList.length; i++) {
                if (this.unionColumnsList[i].colDataType == '' || this.unionColumnsList[i].colDataType == null) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','Data Type is Mandatory');
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

                if (this.unionColumnsList[i].qualifier == 'CURRENCYCODE') {
                    currencyValid++;
                }

                if (this.unionColumnsList[i].qualifier == 'TRANSDATE') {
                    dateValid++;
                }
            }

            if (count > 1) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"Amount" qualifier should be tagged only to one column with data type "DECIMAL"');
            } else if (count == 1) {
                if (intcount == 1) {
                } else {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','Data type of "Amount" qualifier should be of type "DECIMAL"');
                }
            } else if (count < 1) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"Amount" qualifier is mandatory');
            }

            if (datecountint > 0 && datecount < 1) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"TRANSDATE" qualifier should be tagged to data type "DATE"');
            }
            if (datecountint1 > 0 && datecount1 < 1) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"TRANSDATE" qualifier should be tagged to data type "DATETIME"');
            }

            if (currcount > 1) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"CURRENCY" qualifier should be tagged only to one column with data type "VARCHAR');
            } else if (currcount == 1) {
                if (currcountint == 1) {
                } else {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','Data type of "TRANSDATE" qualifier should be "VARCHAR"');
                }
            }

            if (currencyValid < 1) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"Currency" qualifier is mandatory');
            }

            if (dateValid < 1 && !((datecountint > 0 && datecount < 1) || (datecountint1 > 0 && datecount1 < 1))) {
                this.hideSaveButton = false;
                this.commonService.error('Warning!','"Transaction Date" qualifier is mandatory');
            }

            if (groupByCount == 0) {
                    this.commonService.error('Warning!','Atleast one "Group By" column should be selected');
                    this.hideSaveButton = false;
                }
            if ((count == 1 && intcount == 1) && (datecount == datecountint) && (datecount1 == datecountint1) && groupByCount>0 && currencyValid == 1 && dateValid == 1) {
                this.saveDataViews();
            }

        }


    }

    /* Function to display validition message */
    validationMsg() {
        if (this.duplicateViewName == true) {
            this.hideSaveButton = false;
            this.commonService.error('Warning!','Display name already exists');
        } else if (this.duplicateViewName == false) {
            this.hideSaveButton = false;
            this.commonService.error('Warning!','Fill mandatory fields');
        } else if (this.dataViewsColumnsList.length < 1) {
            this.hideSaveButton = false;
            this.commonService.error('Warning!','Select atleast one column');
        } else {
            this.hideSaveButton = false;
            this.commonService.error('Warning!', 'Fill mandatory fields');
        }
    }


    showExcelFunction(val, ind, examp) {
        this.dataViewsColumnsList[ind]['excelexpressioninput'] = val;
        this.dataViewsColumnsList[ind]['excelexpressionExample'] = examp;
        this.dataViewsColumnsList[ind]['excelexpressioninput'] =
            examp.replace(/templateName/g, this.selectedTemplates[0].dataName)
                .replace(/SourceName1/g, this.selectedTemplates[0].dataName)
                .replace(/SourceName2/g, this.selectedTemplates[0].dataName);
    }

    showExcelUnionFunction(val) {
        this.unionColumnsList[val.i].src[val.j].refDvColumn = 'customFunction'; 
        if (!val.isSaved){
            val.selFx.excelexpressioninputUnion = null;
            this.custFxDialogData.selFx.excelexpressioninputUnion =
                this.custFxDialogData.selFx.description.replace(/templateName/g, this.selectedTemplates[this.custFxDialogData.j].dataName)
                    .replace(/SourceName1/g, this.selectedTemplates[this.custFxDialogData.j].dataName)
                    .replace(/SourceName2/g, this.selectedTemplates[this.custFxDialogData.j].dataName);
        }
    }

    saveExpression(val, ind, frml, popOver) {
        const templ = [];
        this.selectedTemplates.forEach((eachTempl) => {
            templ.push(eachTempl.dataName);
        });
        const finFrml = val ? val : frml;
        this.dataViewsService.validateCurrentTemplate(finFrml, templ).takeUntil(this.unsubscribe).subscribe((res) => {
            if (res.taskStatus) {
                this.isCustFxErrorSinJoin = res.details ? res.details : undefined;
            } else {
                this.dataViewsColumnsList[ind]['formula'] = finFrml;
                this.dispCopyIcon = false;
                popOver.hide();
            }
        },
        () => {
            this.commonService.error('Warning!', 'Error in Expression or Template not match');
        });
    }

    insertUnionExpression() {
        const fx = this.custFxDialogData;
        const templ = [];
        templ.push(this.selectedTemplates[fx.j].dataName);
        if (fx.selFx.excelexpressioninputUnion) {
            this.dataViewsService.validateCurrentTemplate(fx.selFx.excelexpressioninputUnion, templ).takeUntil(this.unsubscribe).subscribe((res) => {
                if (res.taskStatus) {
                    this.isCustFxError = res.details ? res.details : undefined;
                } else {
                    this.unionColumnsList[fx.i].src[fx.j].lookUpCode = fx.selFx.lookUpCode;
                    this.unionColumnsList[fx.i].src[fx.j].description = fx.selFx.description;
                    this.unionColumnsList[fx.i].src[fx.j].excelexpressioninputUnion = fx.selFx.excelexpressioninputUnion;
                    this.unionColumnsList[fx.i].src[fx.j].refDvType = 'File Template';
                    this.unionColumnsList[fx.i].src[fx.j].refDvName = this.selectedTemplates[fx.j].typeId;
                    this.unionColumnsList[fx.i].src[fx.j].intermediateId = this.selectedTemplates[fx.j].intermediateId;
                    this.unionColumnsList[fx.i].src[fx.j].columnName = 'customcolumn';
                    this.unionColumnsList[fx.i].src[fx.j].isSaved = true;
                    this.custFxDialog = false;
                    this.limitSelectionSettings = {
                        text: "Select Template / Data Source",
                        selectAllText: 'Select All',
                        unSelectAllText: 'UnSelect All',
                        enableSearchFilter: true,
                        badgeShowLimit: 1,
                        disabled: false,
                        classes: 'template-dropdown',
                    };
                }
            },
            () => {
                this.commonService.error('Warning!', 'Error validating custom function');
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
        const dataviewId = this.conditionValues.dataViewId;
        this.conditionValues.dataViewId = null;
        this.dataViewsService.conditionUpdate(this.conditionValues, dataviewId).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.dataViewsService.createDataViewPost(this.dataViews.id, res).subscribe((dataViews) => {});
            this.fetchDataViewDetails();
        });
    }

    cancelConditionChanges() {
        this.fetchDataViewDetails();
    }

    /* function for validation */
    saveDataViewsV() {
        const tempIntMd = [];
        const tempDvId = [];
        let uniqueIntMd;
        let uniqueDvId;
        this.dataViews.dataViewsColumnsList.forEach((element) => {
            if (element.intermediateId) {
                tempIntMd.push(element.intermediateId);
            } else {
                tempDvId.push(element.refDvName);
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
        });
        uniqueIntMd = tempIntMd.filter(function(item, i, ar) { return ar.indexOf(item) === i; });
        uniqueDvId = tempDvId.filter(function(item, i, ar) { return ar.indexOf(item) === i; });
        const finArr = [...uniqueIntMd, ...uniqueDvId];
        if (uniqueDvId.length > 1 || uniqueIntMd.length > 1 || finArr.length > 1 || !this.dataViews.viewRelation) {
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
                let currencyValid = 0;
                let dateValid = 0;
                for (let i = 0; i < this.dataViews.dataViewsColumnsList.length; i++) {
                    if (this.dataViews.dataViewsColumnsList[i].colDataType == '' || this.dataViews.dataViewsColumnsList[i].colDataType == null) {
                        this.hideSaveButton = false;
                        this.commonService.error('Warning!','Data Type is Mandatory');
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                        break;
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
                        this.commonService.error('Warning!','Display Name is Mandatory');
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                        break;
                    } else if ((this.dataViews.dataViewsColumnsList[i].operator != null) && (this.dataViews.dataViewsColumnsList[i].colValue == '' || this.dataViews.dataViewsColumnsList[i].colValue == null || this.dataViews.dataViewsColumnsList[i].colValue == undefined)) {
                        this.hideSaveButton = false;
                        this.commonService.error('Warning!','Value is Mandatory for selected operator');
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                        break;
                    } else if (this.dataViews.dataViewsColumnsList[i].dataViewDisplayName == '' || this.dataViews.dataViewsColumnsList[i].dataViewDisplayName == null || this.dataViews.dataViewsColumnsList[i].dataViewDisplayName == undefined) {
                        if (!this.dataViews.dataViewsColumnsList[i].formula) {
                            this.hideSaveButton = false;
                            this.commonService.error('Warning!','Build Expression for newly added row');
                            this.viewValidation = false;
                            this.dataViewsPost = [];
                            delete this.dataViews.dataViewsColumnsList;
                            delete this.dataViews.conditions;
                            delete this.dataViews.basedTemplate;
                            delete this.dataViews.viewRelation;
                            break;
                        }
                    } else {
                        this.viewValidation = true;
                    }
                    if (this.dataViews.dataViewsColumnsList[i].qualifier == 'CURRENCYCODE') {
                        currencyValid++;
                    } else if (this.dataViews.dataViewsColumnsList[i].qualifier == 'TRANSDATE') {
                        dateValid++;
                    } 
                }

                if (count > 1) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','"Amount" qualifier should be tagged only to one column with data type "DECIMAL"');
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
                        this.commonService.error('Warning!','Data type of "Amount" qualifier should be of type "DECIMAL"');
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    }
                } else if (count < 1) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','"Amount" qualifier is mandatory');
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
                    this.commonService.error('Warning!','"TRANSDATE" qualifier should be tagged to data type "DATE"');
                }
                if (datecountint1 > 0 && datecount1 < 1) {
                    this.hideSaveButton = false;
                    this.viewValidation = false;
                    this.commonService.error('Warning!','"TRANSDATE" qualifier should be tagged to data type"DATETIME"');
                }

                if (currcount > 1) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','"CURRENCY" qualifier should be tagged only to one column with data type "VARCHAR');
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
                        this.commonService.error('Warning!','Data type of "CURRENCY" qualifier should be "VARCHAR"');
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    }
                }

                if (currencyValid < 1) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','"Currency" qualifier is mandatory');
                    this.viewValidation = false;
                    this.dataViewsPost = [];
                    delete this.dataViews.dataViewsColumnsList;
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                }

                if (dateValid < 1 && !((datecountint > 0 && datecount < 1) || (datecountint1 > 0 && datecount1 < 1))) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','"Date" qualifier is mandatory');
                    this.viewValidation = false;
                    this.dataViewsPost = [];
                    delete this.dataViews.dataViewsColumnsList;
                    delete this.dataViews.conditions;
                    delete this.dataViews.basedTemplate;
                    delete this.dataViews.viewRelation;
                }


                if (groupcount > 1) {
                    this.hideSaveButton = false;
                    this.commonService.error('Warning!','"GROUPBY_COLUMN" qualifier should be tagged only to one column with data type "VARCHAR');
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
                        this.commonService.error('Warning!','Data type of "GROUPBY_COLUMN" qualifier should be "VARCHAR"');
                        this.viewValidation = false;
                        this.dataViewsPost = [];
                        delete this.dataViews.dataViewsColumnsList;
                        delete this.dataViews.conditions;
                        delete this.dataViews.basedTemplate;
                        delete this.dataViews.viewRelation;
                    }
                }
                if (groupByCount == 0) {
                    this.commonService.error('Warning!','Atleast one "Group By" column should be selected');
                    this.hideSaveButton = false;
                    this.viewValidation = false;
                }

            }
        } else {
            this.hideSaveButton = false;
            this.commonService.error('Warning!','Atleast one column is required from selected templates');
            this.viewValidation = false;
            this.dataViewsPost = [];
            delete this.dataViews.dataViewsColumnsList;
            delete this.dataViews.conditions;
            delete this.dataViews.basedTemplate;
            delete this.dataViews.viewRelation;
        }
    }

    /** 
     * Author: Sameer
     * Purpose: getting object as selected value, need to display selected object
    */
    tempLinesComWith(obj1, obj2) {
        if (obj1 && obj2) {
            return obj1.refDvColumn == obj2.refDvColumn;
        }
    }

    /**
     * @author Sameer
     * @description Compare with selected lookupcode and select option
     * @param obj1 
     * @param obj2 
     */
    lookUpComWith(obj1, obj2) {
        if (obj1 && obj2.lookUpCode) {
            return obj1.lookUpCode == obj2.lookUpCode;
        }
    }

    /**Adding Dynamic Height to split example based on dropdown*/
    multiselectClick(val) {
        const list = $(`${val} .dropdown-list`).attr('hidden');
        if (list) {
            $(".split-example").css('min-height', (window.innerHeight - 130) + 'px');
            $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").css('min-height', (window.innerHeight - 330) + 'px');
        } else {
            if (window.innerHeight < 860) {
                $('.split-example').css('min-height', (window.innerHeight + (val == '.template-dropdown' ? 45 : 115)) + 'px');
                $('.dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content').css('min-height', (window.innerHeight + (val == '.template-dropdown' ? 45 : 115)) + 'px');
            }
        }
    }
    
    clearUnionFunction() {
        this.custFxDialog = false;
        this.limitSelectionSettings = {
            text: "Select Template / Data Source",
            selectAllText: 'Select All',
            unSelectAllText: 'UnSelect All',
            enableSearchFilter: true,
            badgeShowLimit: 1,
            disabled: false,
            classes: 'template-dropdown',
        };
        const fx = this.custFxDialogData;
        if(!this.unionColumnsList[fx.i].src[fx.j].isSaved && !this.unionColumnsList[fx.i].src[fx.j].excelexpressioninputUnion) {
            this.unionColumnsList[fx.i].src[fx.j] = null;
            this.sourceCols[fx.j][0] = {
                'refDvColumn': 'customFunction',
                'columnName': 'Custom Function'
            }
        }
    }

    showFxDialog(data, i, j) {
        if (data && data.refDvColumn == 'customFunction') {
            this.isCustFxError = undefined;
            this.custFxDialog = true;
            this.limitSelectionSettings = {
                text: "Select Template / Data Source",
                selectAllText: 'Select All',
                unSelectAllText: 'UnSelect All',
                enableSearchFilter: true,
                badgeShowLimit: 1,
                disabled: true,
                classes: 'template-dropdown',
            };
            this.custFxDialogData = data;
            this.custFxDialogData.i = i;
            this.custFxDialogData.j = j;
            if (data.isSaved || data.excelexpressioninputUnion) {
                data['selFx'] = {
                    'lookUpCode': data.lookUpCode,
                    'excelexpressioninputUnion': data.excelexpressioninputUnion,
                    'description': data.description
                }
            }
        } else if(data && data.refDvColumn == 'none') {
            data.refDvType = this.selectedTemplates[j].type;
            data.refDvName = this.selectedTemplates[j].typeId;
            data.intermediateId = this.selectedTemplates[j].intermediateId;
        }
    }


    copyColumn(val, key) {
        const selBox = document.createElement('textarea');
        selBox.style.position = 'fixed';
        selBox.style.left = '0';
        selBox.style.top = '0';
        selBox.style.opacity = '0';
        selBox.value = key == 'dataName' ? val.dataName : key == 'source' ? val.sourceName : val.columnHeader;
        document.body.appendChild(selBox);
        selBox.focus();
        selBox.select();
        document.execCommand('copy');
        document.body.removeChild(selBox);
    }

    checkTempColumnName(name, i) {
        if(!name) return;
        const arr = this.viewRelation && this.viewRelation == 'UNION' ? this.unionColumnsList : this.dataViewsColumnsList;
        name = name.trim();
        arr[i].columnName = name;
        const count = arr.filter((each) => each.columnName && each.columnName.toString().toLowerCase() == name.toString().toLowerCase()).length;
        if (count > 1) {
            this.commonService.error('Warning!', 'Display name should be unique');
            setTimeout(() => {
                arr[i].columnName = null;
            }, 100);
        }
    }

    qualifierChange(qualifier, i, arr) {
        if (qualifier) {
            const count = [];
            arr.map((each) => {
                if (each.qualifier == qualifier) {
                    count.push(each);
                }
            });
            if (count.length > 1) {
                this.commonService.error('Warning!', 'Qualifier already used');
                setTimeout(() => {
                    arr[i].qualifier = null;
                }, 100);
            } else {
                if (arr[i].qualifier == 'CURRENCYCODE') {
                    arr[i].groupBy = true;
                } else {
                    arr[i].groupBy = false;
                }
            }
        }
    }

    groupByChange(col) {
        if (col.qualifier == 'CURRENCYCODE' && !col.groupBy) {
            this.commonService.info('Warning!', 'Group by is mandatory for "Transaction Currency"');
            setTimeout(() => {
                col.groupBy = true;
            }, 100);
        }
    }

    enableCopy() {
        $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").css('min-height', (window.innerHeight - 330) + 'px');
        this.isCopy = true;
        this.isViewOnly = false;
        this.dataViews.copyId = this.dataViews.id;
        this.sourceRelation = this.viewRelation = this.lastRelation = this.dataViews.viewRelation;
        this.dataViews.id = undefined;
        this.dataViews.dataViewDispName = null;
        this.dataViews.description = null;
        this.dataViews.startDate = new Date();
        this.dataViews.endDate = null;
        this.selItems = [];
        this.selectedTemplates = [];
        let dataArray = [];
        this.listOperatorFun();
        if(!this.viewRelation || this.viewRelation == 'JOIN') {
            this.getTepmlateLinesSingleJoin(this.dataViews.templateInfo);
        }
        if (this.viewRelation == 'JOIN') {
            this.basedTemplate = this.selectedTemplates[0];
            this.conditionValues.srcType1 = this.dataViews.conditions[0].srcType1;
            this.conditionValues.scr1 = this.dataViews.conditions[0].scr1;
            this.conditionValues.srcCol1 = this.dataViews.conditions[0].srcCol1;
            this.conditionValues.srcType2 = this.dataViews.conditions[0].srcType2;
            this.conditionValues.scr2 = this.dataViews.conditions[0].scr2;
            this.conditionValues.srcCol2 = this.dataViews.conditions[0].srcCol2;
            this.conditionValues.conditionOperator = "=";
        }
        if (!this.viewRelation) {
            this.disColDrpDwn = true;
            delete this.dataViews.conditions;
        }
        if(this.viewRelation == 'UNION') {
            dataArray = this.unionColumnsList[0].src;
        } else {
            dataArray = this.dataViews.templateInfo;
        }
        dataArray.forEach((uninSrcEach) => {
            const key = uninSrcEach.typeId ? 'typeId' : 'refDvName';
            this.dropdownList.forEach((drpDwnEach) => {
                if (uninSrcEach.intermediateId) {
                    if (uninSrcEach[key] == drpDwnEach.typeId && uninSrcEach.intermediateId == drpDwnEach.intermediateId) {
                        this.selectedTemplates.push(drpDwnEach);
                    }
                } else {
                    if (uninSrcEach[key] == drpDwnEach.typeId) {
                        this.selectedTemplates.push(drpDwnEach);
                    }
                }
            });
        });
        this.dataViews.srcMapping = this.selItems = this.selectedTemplates;
    }

    gotoDetails() {
        $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").css('min-height', 'auto');
        this.isCopy = false;
        this.isViewOnly = true;
        this.duplicateViewName = undefined;
    }

    clearViewConfirmation(data): void {
        if (this.unionColumnsList && this.unionColumnsList.length || this.dataViewsColumnsList && this.dataViewsColumnsList.length) {
            this.overlayContainer.getContainerElement().classList.add('data-views-component');
            const dialogRef = this.dialog.open(DVConfirmationDialogComponent, {
                width: '400px',
                data: { yes: true, no: false },
                disableClose: true
            });

            dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
                this.overlayContainer.getContainerElement().classList.remove('data-views-component');
                if (result) {
                    this.viewRelation = this.sourceRelation;
                    this.relationFun(data);
                } else {
                    this.sourceRelation = this.viewRelation = this.lastRelation;
                }
            });
        } else {
            this.lastRelation = this.viewRelation = this.sourceRelation;
            this.relationFun(data);
        }
    }

    clearViewConfOnTemplSel(item, evt, type) {
        let showDialog;
        if (item.length == 1) {
            // Single template
            if (this.unionColumnsList && this.unionColumnsList.length || this.dataViewsColumnsList && this.dataViewsColumnsList.length) {
                showDialog = true;
            } else {
                this.onSelectTemplateAndDataView(item, evt, type);
            }
        } else if (item.length == 2) {
            // Either Join or Union based on user selection
            if (this.unionColumnsList && this.unionColumnsList.length || this.dataViewsColumnsList && this.dataViewsColumnsList.length) {
                showDialog = true;
            } else {
                this.onSelectTemplateAndDataView(item, evt, type);
            }

        } else if (item.length > 2) {
            // Union Template
            if (this.dataViewsColumnsList && this.dataViewsColumnsList.length) {
                showDialog = true;
            } else {
                this.onSelectTemplateAndDataView(item, evt, type);
            }
        }

        if (showDialog) {
            this.overlayContainer.getContainerElement().classList.add('data-views-component');
            const dialogRef = this.dialog.open(DVConfirmationDialogComponent, {
                width: '400px',
                data: { yes: true, no: false },
                disableClose: true
            });

            dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
                this.overlayContainer.getContainerElement().classList.remove('data-views-component');
                if (result) {
                    if(item.length == 1) {
                        this.unionColumnsList = [];
                    } else if (item.length == 2) {
                        this.viewRelationVar.nativeElement.children[0].querySelector('input').click();
                    }
                    this.onSelectTemplateAndDataView(item, evt, type);
                } else {
                    if (type == 'select') {
                        // If selected new and don't want to clear view remove newly added
                        item.splice(item.length-1, 1);
                    } else if (type == 'deselect') {
                        // If unselected existing and don't want to clear view add existing clear
                        item.push(evt);
                    }
                }
            });
        }
    }

    trackByIndex(i: number, v: number) {
        return i;
    }

    showView(evt, col) {
        this.selectedViewName = col.dataViewDispName;
        this.viewsList = [];
        this.viewColumnOptions = [];
        this.viewTableColumns = [];
        this.selectedView = col.id;
        this.page1 = 0;
        this.dataViewsService.getViewDetails(col.id, this.page1, this.viewitemsPerPage, ).subscribe((res: any) => {
            this.viewTableColumns = res.json().columnsWithAttributeNames;
            for (let i = 0; i < res.json().columnsWithAttributeNames.length; i++) {
                this.viewColumnOptions.push({ label: res.json().columnsWithAttributeNames[i].header, value: res.json().columnsWithAttributeNames[i] });
            }
            if (this.viewColumnOptions.length > 0) {
                this.viewsList = res.json().dataView.mapList;
                this.selectedViewRecordsLength = +res.headers.get('x-count');
                this.display = true;
            }
        });
    }

    /**
     * @description Physical View Export
     * @param type 
     */
    physicalViewExport(type) {
        this.dataViewsService.getDataViewsDataCSV(this.selectedView, type).takeUntil(this.unsubscribe).subscribe(
            (res) => { this.commonService.exportData(res, type, this.selectedViewName); },
            () => this.commonService.error('Warning!', 'Error occured while exporting View.'));
    }

    /**
     * @description Physical View Pagnation
     * @param evt 
     */
    onPaginateChangeView(evt) {
        this.page1 = evt.pageIndex;
        this.viewitemsPerPage = evt.pageSize;
        this.dataViewsService.getViewDetails(this.selectedView, this.page1, this.viewitemsPerPage, ).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.viewsList = res.json().dataView.mapList;
            this.selectedViewRecordsLength = +res.headers.get('x-count');
        });
    }

}

/********************Confirmation Dialog********************/
@Component({
    selector: 'dv-confirmation-dialog',
    template: `
                    <p>Current View will be clear, Do you want to proceed</p>
                    <div class="actions">
                        <button md-button [md-dialog-close]="data.yes" tabindex="2">Yes</button>
                        <button md-button [md-dialog-close]="data.no" tabindex="-1">No</button>
                    </div>
    `
})

export class DVConfirmationDialogComponent {
    constructor(
        public dialogRef: MdDialogRef<DVConfirmationDialogComponent>,
        @Inject(MD_DIALOG_DATA) public data: any
    ) { }
    
      onNoClick(): void {
        this.dialogRef.close();
      }
}
