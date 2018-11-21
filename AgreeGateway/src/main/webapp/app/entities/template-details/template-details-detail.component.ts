import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { TemplateDetails, JournalsHeaderData } from './template-details.model';
import { TemplateDetailsService } from './template-details.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { CommonService } from '../common.service';
import { DataViewsService } from '../data-views/data-views.service';
import { trigger, state, style, animate, transition } from '@angular/animations';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-template-details-detail',
    templateUrl: './template-details-detail.component.html'
})


     /*  animations: [
        trigger('flyInOut', [
            state('in', style({ transform: 'translateX(0)' })),
            transition('void => *', [
                style({ transform: 'translateX(-100%)' }),
                animate(1000)
            ])
        ])
    ],  */
   
export class TemplateDetailsDetailComponent implements OnInit, OnDestroy {

    journalsHeader = new JournalsHeaderData();
    templateDetails: TemplateDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    dataViews: any;
    dataViewList = [];
    limitSelectionSettings1 = {};
    selectedView = [];
    dataViewCols = [];
    glDateDerivationList = [];
    periodList = [];
    conversionTypeList = [];
    journalBalanceList = [];
    journalHeaders = [];
    mappingTypes = [];
    mappingValues = [];
    mappingValue = [];
    ledgersList = [];
    jeLineDerivations = [];
    operatorList = [];
    journalGenerationLevelList = [];
    mappingTypesa: any = [];
    selectedViewArr: any = [];
    selectedViews: any = [];
    selectedViewColumns: any = [];
    selectedViewColumnsAtLines: any = [];
    displayLineje =false;
    hideSaveButton:boolean = false;
    isVisibleA: boolean = false;
    constructor(
        private eventManager: JhiEventManager,
        private templateDetailsService: TemplateDetailsService,
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
    startEndDateClass(){
        if(this.journalsHeader.startDate != null){
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }else{
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }

    changeMinDate() {
        this.config.minDate = this.journalsHeader.startDate;
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
        this.limitSelectionSettings1 = {
            text: "Select View",
            enableSearchFilter: true,
            searchPlaceholderText: "Search View..",
            limitSelection: 1
        };
        /* Calling below service to fetch dataviews list */
        this.dataViewsService.dataViewList().subscribe((res: any) => {
            this.dataViews = res;
            for (var i = 0; i < this.dataViews.length; i++) {
                let obj = {
                    "id": this.dataViews[i].id,
                    "itemName": this.dataViews[i].dataViewDispName
                }
                this.dataViewList.push(obj);
            }
        });
        
        this.templateDetailsService.getLookUps('GL_DATES_DERIVATION').subscribe((res: any) => {
            this.glDateDerivationList = res;
        });
        this.templateDetailsService.getLookUps('GL_PERIOD').subscribe((res: any) => {
            this.periodList = res;
        });
        this.templateDetailsService.getLookUps('CONVERSION_TYPE').subscribe((res: any) => {
            this.conversionTypeList = res;
        });
        this.templateDetailsService.getLookUps('DEBIT_AND_CREDIT_SHOULD_BALANCE').subscribe((res: any) => {
            this.journalBalanceList = res;
        });
        this.templateDetailsService.getLookUps('OPERATOR').subscribe((res: any) => {
            this.operatorList = res;
        });
        this.templateDetailsService.getLookUps('JOURNAL_GENERATION_LEVEL').subscribe((res: any) => {
            this.journalGenerationLevelList = res;
        });
        this.templateDetailsService.getLedgersList().subscribe((res: any) => {
            this.ledgersList = res;
            this.ledgersList.forEach(element => {
                element.id = element.id.toString();
            });
        });
        this.templateDetailsService.getmappingSets().subscribe((res: any) => {
            this.mappingValues = res;
            this.mappingValues.forEach(element => {
                element.id = element.id.toString();
            });
            console.log('this.mappingValues ::' + JSON.stringify(this.mappingValues));
        });
        this.fetchJournalsDetails();
    }

    fetchJournalsDetails() {
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "template-details") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                this.templateDetailsService.getJournalsById(params['id']).subscribe(
                    (journalsHeaderDataService) => {
                        this.journalsHeader = journalsHeaderDataService;
                        this.selectedViews = this.journalsHeader.selectedView9;
                        console.log('this.journalsHeader ::' + JSON.stringify(this.journalsHeader));
                        if(this.journalsHeader.jeLineDerivations){
                            let tempAry = [];
                            for (var i = 0; i < this.journalsHeader.jeLineDerivations.length; i++) {
                                tempAry.push(this.journalsHeader.jeLineDerivations[i].sViews);
                            }
                            if (tempAry.length == this.journalsHeader.jeLineDerivations.length) {
                                console.log('while post ' + JSON.stringify(tempAry));
                                this.templateDetailsService.getViewsColumnsData(tempAry)
                                    .subscribe((res: any) => {
                                        this.selectedViewColumnsAtLines = res;
                                        console.log('this.selectedViewColumnsAtLines ' + JSON.stringify(this.selectedViewColumnsAtLines));
                                    });
                            }
                            this.jeLineDerivations = this.journalsHeader.jeLineDerivations;
                            this.displayLineje = true;
                        }
                       
                        let temp = [];

                        /*  this.selectedViewColumns[0] = [];
                         if (this.journalsHeader.jeHeaderDerivations[1].viewId != null) {
                             let temp = [];
                             this.selectedViewColumns[1] = [];
                             this.dataViewsService.getDataViewById(this.journalsHeader.jeHeaderDerivations[1].viewId).subscribe((res: any) => {
                                 if (res[0].dataViewsColumnsList != undefined) {
                                     res[0].dataViewsColumnsList.forEach(element => {
                                         this.selectedViewColumns[1].push(element);
                                     });
 
                                 } else if (res[0].dataViewsUnionColumnsList != undefined) {
                                     res[0].dataViewsUnionColumnsList.forEach(element => {
                                         this.selectedViewColumns[1].push(element);
                                     });
                                 }
                             });
                         }
                         if (this.journalsHeader.jeHeaderDerivations[2].viewId != null) {
                             let temp = [];
                             this.selectedViewColumns[2] = [];
                             this.dataViewsService.getDataViewById(this.journalsHeader.jeHeaderDerivations[2].viewId).subscribe((res: any) => {
                                 if (res[0].dataViewsColumnsList != undefined) {
                                     res[0].dataViewsColumnsList.forEach(element => {
                                         this.selectedViewColumns[2].push(element);
                                     });
 
                                 } else if (res[0].dataViewsUnionColumnsList != undefined) {
                                     res[0].dataViewsUnionColumnsList.forEach(element => {
                                         this.selectedViewColumns[2].push(element);
                                     });
                                 }
                             });
                         }
                         if (this.journalsHeader.jeHeaderDerivations[1].viewId != null) {
                             let temp = [];
                             this.selectedViewColumns[3] = [];
                             this.dataViewsService.getDataViewById(this.journalsHeader.jeHeaderDerivations[1].viewId).subscribe((res: any) => {
                                 if (res[0].dataViewsColumnsList != undefined) {
                                     res[0].dataViewsColumnsList.forEach(element => {
                                         this.selectedViewColumns[3].push(element);
                                     });
 
                                 } else if (res[0].dataViewsUnionColumnsList != undefined) {
                                     res[0].dataViewsUnionColumnsList.forEach(element => {
                                         this.selectedViewColumns[3].push(element);
                                     });
                                 }
                             });
                         } */
                        console.log('selectedViewColumns ' + JSON.stringify(this.selectedViewColumns));
                        for (var i = 0; i < this.journalsHeader.jeHeaderDerivations.length; i++) {
                            /* this.journalsHeader.jeHeaderDerivations[i]['showRow'] = true; */
                            if (this.journalsHeader.jeHeaderDerivations[i].viewId != null) {
                                temp.push(this.journalsHeader.jeHeaderDerivations[i].viewId);
                            }
                            console.log('temp ' + JSON.stringify(temp));
                            this.mapval(this.journalsHeader.jeHeaderDerivations[i].meaning, i);
                            this.getType(this.journalsHeader.jeHeaderDerivations[i].mappingType, this.journalsHeader.jeHeaderDerivations[i].meaning, i);
                        }
                        console.log('while post header ' + JSON.stringify(temp));
                        this.templateDetailsService.getViewsColumnsData(temp)
                            .subscribe((res: any) => {
                                this.selectedViewColumns = res;
                                let emptyAry = [];
                                console.log('this.journalsHeader.jeHeaderDerivations ' + JSON.stringify(this.journalsHeader.jeHeaderDerivations));
                                for (var i = 0; i < this.journalsHeader.jeHeaderDerivations.length; i++) {
                                    console.log('this.journalsHeader.jeHeaderDerivations[i].viewId ' + this.journalsHeader.jeHeaderDerivations[i].viewId);
                                    if (this.journalsHeader.jeHeaderDerivations[i].viewId == null) {
                                        console.log(' inside i ' + i + ' emptyary ' + emptyAry);
                                        this.selectedViewColumns.splice(i, 0, emptyAry);
                                    }
                                    console.log('this.selectedViewColumns ' + JSON.stringify(this.selectedViewColumns));
                                }
                                //this.selectedViewColumns.push(emptyAry);

                            });
                        this.journalHeaders = this.journalsHeader.jeHeaderDerivations;
                        // console.log('this.journalHeaders9 :: ' + JSON.stringify(this.journalHeaders));
                        

                        console.log('journalsHeader to display by id :' + JSON.stringify(this.journalsHeader));
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                        } else {
                            this.isViewOnly = true;
                            this.limitSelectionSettings1 = {
                                text: "Select View",
                                enableSearchFilter: true,
                                searchPlaceholderText: "Search View..",
                                disabled: true,
                                limitSelection: 1
                            };
                        }
                    }
                );
            } else {
                this.isVisibleA = true;
                /* Calling below service to fetch journalDerivations list */
        this.templateDetailsService.getLookUps('JE_DERIVATIONS').subscribe((res: any) => {
            this.journalHeaders = res;
            console.log('this.journalHeaders ' + JSON.stringify(this.journalHeaders));
            /* Calling below service to fetch list of mapping type */
            this.templateDetailsService.getLookUps('MAPPING_TYPE').subscribe((res: any) => {
                this.mappingTypes = res;
                console.log('testMapping'+JSON.stringify(this.mappingTypes));
                for (var i = 0; i < this.journalHeaders.length; i++) {
                    this.journalHeaders[i]['showRow'] = false;
                    this.mapval(this.journalHeaders[i].meaning, i);
                }
            });
        });
                this.isCreateNew = true;
                let obj = {
                    "value": ''
                };
                this.jeLineDerivations.push(obj);

            }
            /* $('#disp_1').css('display', 'none');
        $('#disp_2').css('display', 'none');
        $('#disp_3').css('display', 'none');
        $('#disp_4').css('display', 'none');
        $('#disp_5').css('display', 'none');
        $('#disp_6').css('display', 'none');
        $('#disp_7').css('display', 'none');
        $('#disp_8').css('display', 'none');
        $('#disp_9').css('display', 'none');
        $('#disp_10').css('display', 'none'); */
        });
    }
    convertMappingSetPurpose() {
        console.log('journalsHeader.selectedView ::' + JSON.stringify(this.journalsHeader.selectedView));
        this.journalsHeader.viewId = '';
        this.journalsHeader.viewName = '';

        let tempArray = [];
        let tempArray2 = [];
        for (var i = 0; i < this.journalsHeader.selectedView.length; i++) {
            tempArray.push(this.journalsHeader.selectedView[i].id);
            tempArray2.push(this.journalsHeader.selectedView[i].itemName);

        }
        this.journalsHeader.viewId = tempArray.toString();
        this.journalsHeader.viewName = tempArray2.toString();
        console.log('this.journalsHeader :: ' + JSON.stringify(this.journalsHeader));
    }
    /* Function to save journal data */
    saveJournalData() {
        this.convertMappingSetPurpose();
        let link: any = '';
        delete this.journalsHeader.selectedView;
        if (this.journalsHeader.id == null || this.journalsHeader.id == undefined) {
            for (var i = 0; i < this.journalHeaders.length; i++) {
                this.journalHeaders[i]['attributeName'] = this.journalHeaders[i].lookUpCode;
                
            };
        }
        for (var i = 0; i < this.jeLineDerivations.length; i++) {
            this.jeLineDerivations[i].seq = i + 1;
        }
        this.journalsHeader['enable'] = true;
        this.journalsHeader['targetAppSource'] = 'Oracle_EBS';
        this.journalsHeader['jeHeaderDerivations'] = this.journalHeaders;
        this.journalsHeader.jeHeaderDerivations.splice(7,1);
        this.journalsHeader['jeLineDerivations'] = this.jeLineDerivations;
        console.log('Journal Data For Posting :' + JSON.stringify(this.journalsHeader));
        
         this.templateDetailsService.postJournalsHeaderData(this.journalsHeader)
            .subscribe((journalsHeader) => {
                this.journalsHeader = journalsHeader;
                this.notificationsService.success(
                    'Success!',
                    'Journal template successfully created'
                )
                this.hideSaveButton = false;
                if (this.journalsHeader.id) {
                    link = ['/template-details', { outlets: { 'content': this.journalsHeader.id + '/details' } }];
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
    testChange(obj: any, ind: any) {
        console.log('obj ::' + JSON.stringify(obj) + ' ind ::' + ind);
        let indVal = '#disp_'.concat(ind + 1);
        if (obj.lookUpCode == undefined) {
            obj.lookUpCode = obj.attributeName;
        }
        if ((obj.lookUpCode == 'JOURNAL_BATCH_NAME' || obj.lookUpCode == 'RATE') && (obj.value != '' && obj.value != undefined)) {
            $(indVal).css('display', '');
            if (obj.lookUpCode == 'JOURNAL_BATCH_NAME') {
                this.journalHeaders[1].showRow = true;
            } else if (obj.lookUpCode == 'RATE') {
                this.journalHeaders[8].showRow = true;
            }
        } else if (obj.lookUpCode == 'LEDGER' || obj.lookUpCode == 'SOURCE' || obj.lookUpCode == 'CATEGORY') {
            if (obj.mappingType == 'User Value' && (obj.value != '' && obj.value != undefined)) {
                $(indVal).css('display', '');
                if (obj.lookUpCode == 'LEDGER') {
                    this.journalHeaders[2].showRow = true;
                } else if (obj.lookUpCode == 'SOURCE') {
                    this.journalHeaders[3].showRow = true;
                } else if (obj.lookUpCode == 'CATEGORY') {
                    //this.journalHeaders[4].showRow = true;
                    if (this.journalHeaders[1].mappingType == 'LEDGER_SET') {
                        this.journalHeaders[4].showRow = true;
                        this.journalHeaders[5].showRow = true;
                    }else if (this.journalHeaders[1].mappingType == 'MAPPING_SET'){
                        this.journalHeaders[4].showRow = false;
                        this.journalHeaders[5].showRow = true;
                    }
                }
            } else if (obj.mappingType == 'MAPPING_SET' && (obj.sourceViewColumnIdv != null || obj.sourceViewColumnId != undefined)) {
                $(indVal).css('display', '');
                if (obj.lookUpCode == 'LEDGER') {
                    this.journalHeaders[2].showRow = true;
                } else if (obj.lookUpCode == 'SOURCE') {
                    this.journalHeaders[3].showRow = true;
                } else if (obj.lookUpCode == 'CATEGORY') {
                    //this.journalHeaders[4].showRow = true;
                    if (this.journalHeaders[1].mappingType == 'LEDGER_SET') {
                        this.journalHeaders[4].showRow = true;
                        this.journalHeaders[5].showRow = true;
                    }else if (this.journalHeaders[1].mappingType == 'MAPPING_SET'){
                        this.journalHeaders[4].showRow = false;
                        this.journalHeaders[5].showRow = true;
                    }
                }
            } else if (obj.mappingType == 'LEDGER_SET' && (obj.value != '' && obj.value != undefined)) {
                $(indVal).css('display', '');
                if (obj.lookUpCode == 'LEDGER') {
                    this.journalHeaders[2].showRow = true;
                    console.log('ledger id ' + this.journalHeaders[1].value);
                    console.log('ledgersList ' + JSON.stringify(this.ledgersList));
                    this.ledgersList.forEach(element => {
                        if (element.id == this.journalHeaders[1].value) {
                            this.journalHeaders[4].meaning = 'Currency';
                            this.journalHeaders[4].lookUpCode = 'CURRENCY';
                            this.journalHeaders[4].viewId = '';
                            this.journalHeaders[4].mappingType = 'User Value';
                            this.journalHeaders[4].value = element.currency;
                        }
                    });
                } else if (obj.lookUpCode == 'SOURCE') {
                    this.journalHeaders[3].showRow = true;
                } else if (obj.lookUpCode == 'CATEGORY') {
                    this.journalHeaders[4].showRow = true;
                }
            }
        } else if ((obj.lookUpCode == 'CURRENCY' || obj.lookUpCode == 'JOURNAL_GENERATION_LEVEL' || obj.lookUpCode == 'DEBIT_AND_CREDIT_SHOULD_BALANCE')
            && (obj.value != '' && obj.value != undefined)) {
            $(indVal).css('display', '');
            if (obj.lookUpCode == 'CURRENCY') {
                this.journalHeaders[5].showRow = true;
            } else if (obj.lookUpCode == 'JOURNAL_GENERATION_LEVEL') {
                this.journalHeaders[10].showRow = true;
            } else if (obj.lookUpCode == 'DEBIT_AND_CREDIT_SHOULD_BALANCE') {
                //this.journalHeaders[11].showRow = true;
            }
        } else if (obj.lookUpCode == 'GL_DATE' || obj.lookUpCode == 'PERIOD' || obj.lookUpCode == 'CONVERSION_TYPE' ||
            obj.lookUpCode == 'CONVERSION_TYPE') {
            if (obj.mappingType == 'User Value' && (obj.value != '' && obj.value != undefined)) {
                if (obj.lookUpCode == 'GL_DATE') {
                    this.journalHeaders[6].showRow = true;
                } else if (obj.lookUpCode == 'PERIOD') {
                    this.journalHeaders[8].showRow = true;
                } else if (obj.lookUpCode == 'CONVERSION_TYPE') {
                    this.journalHeaders[9].showRow = true;
                }
                $(indVal).css('display', '');
            } else if (obj.mappingType == 'LOOKUP_CODE' && (obj.value != '' && obj.value != undefined)) {
                $(indVal).css('display', '');
                if (obj.lookUpCode == 'GL_DATE') {
                    this.journalHeaders[6].showRow = true;
                } else if (obj.lookUpCode == 'PERIOD') {
                    this.journalHeaders[8].showRow = true;
                } else if (obj.lookUpCode == 'CONVERSION_TYPE') {
                    this.journalHeaders[9].showRow = true;
                }
            }
        }

        //Notification if Selected Detail
        if(obj.value == 'DETAIL'){
            this.notificationsService.alert(
                'Suggestion',
                'By opting this option you will get huge amount of mapping set details'
            );
        }

        //Scroll to Bottom
        if(obj.value.length > 0){
            $("html, body").animate({ scrollTop: $(document).height() }, 2000);
        }
    }
    //MAPPING_SET 
    /* Function onselect of view to get dataview columns */
    onViewSelect(obj: any) {
        console.log('journalHeaders ' + JSON.stringify(this.journalHeaders));
        for (var i = 0; i < this.journalHeaders.length; i++) {

            this.journalHeaders[i].viewId = '';
            this.journalHeaders[i].mappingType = '';
            this.journalHeaders[i].value = '';
        }
        this.journalHeaders[0].showRow = true;
        this.selectedViewArr = [];
        this.dataViewCols = [];
        this.selectedViews = obj;
        console.log('this.selectedViews ' + JSON.stringify(this.selectedViews));
        console.log('selected View :' + JSON.stringify(obj));
        this.journalsHeader.viewId = obj[0].id;
        this.journalsHeader.viewName = obj[0].itemName;
        for (var j = 0; j < this.selectedViews.length; j++) {
            console.log('obj[j].id ' + this.selectedViews[j].id);
            this.dataViewsService.getDataViewById(this.selectedViews[j].id).subscribe((res: any) => {
                this.selectedViewArr = res;
                console.log('this.selectedViewArr ' + JSON.stringify(this.selectedViewArr));
                if (this.selectedViewArr[0].dataViewsColumnsList != undefined) {
                    for (var i = 0; i < this.selectedViewArr[0].dataViewsColumnsList.length; i++) {
                        this.dataViewCols.push(this.selectedViewArr[0].dataViewsColumnsList[i]);
                        console.log('dataView Columns :' + JSON.stringify(this.dataViewCols));
                    }
                } else if (this.selectedViewArr[0].dataViewsUnionColumnsList != undefined) {
                    for (var x = 0; x < this.selectedViewArr[0].dataViewsUnionColumnsList.length; x++) {
                        this.dataViewCols.push(this.selectedViewArr[0].dataViewsUnionColumnsList[x]);
                        console.log('dataView Columns :' + JSON.stringify(this.dataViewCols));
                    }
                }
            });

        }
        //console.log('this.presentPath ' + this.presentPath);
        if (this.presentUrl.endsWith('edit')) {
            this.jeLineDerivations = [];
            for (var i = 0; i < this.journalsHeader.jeHeaderDerivations.length; i++) {
                if (this.journalsHeader.jeHeaderDerivations[i].sourceViewColumnId != null || this.journalsHeader.jeHeaderDerivations[i].sourceViewColumnId == undefined) {
                    this.journalsHeader.jeHeaderDerivations[i].sourceViewColumnId = '';
                }
            }
            let obj = {
                "value": ''
            };
            this.jeLineDerivations.push(obj);
        }
        $('#disp_1').css('display', 'none');
        $('#disp_2').css('display', 'none');
        $('#disp_3').css('display', 'none');
        $('#disp_4').css('display', 'none');
        $('#disp_5').css('display', 'none');
        $('#disp_6').css('display', 'none');
        $('#disp_7').css('display', 'none');
        $('#disp_8').css('display', 'none');
        $('#disp_9').css('display', 'none');
        $('#disp_10').css('display', 'none');

        // this.getColumns();
    }
    /* function to get columns on select of view */
    getColumns(val: any, ind) {
        console.log('val ::' + val + ' ind ' + ind);
        for (var i = 0; i < this.dataViewCols.length; i++) {
            this.selectedViewColumns[ind] = this.dataViewCols.filter((item) => item.dataViewId == val);
            console.log('selectedViewColumns[ind] ' + JSON.stringify(this.selectedViewColumns[ind]));
        }
    }
    getColumnsAtLineLevel(val: any, ind) {
        console.log('val ::' + val + ' ind ' + ind);
        for (var i = 0; i < this.dataViewCols.length; i++) {
            this.selectedViewColumnsAtLines[ind] = this.dataViewCols.filter((item) => item.dataViewId == val);
            console.log('selectedViewColumnsAtLines[ind] ' + JSON.stringify(this.selectedViewColumnsAtLines[ind]));
        }
    }
    /* Function to filter mapping types based on attribute names */
    mapval(val: any, ind) {
        console.log(JSON.stringify(this.mappingTypes));
        for (var i = 0; i < this.mappingTypes.length; i++) {
            if (val == 'Journal Batch Name') {
                this.mappingTypes[0]['filt'] = val;
            } else if (val == 'Ledger') {
                //this.mappingTypes[0]['filt'] = val;
                this.mappingTypes[1]['filt'] = val;
                this.mappingTypes[3]['filt'] = val;
            } else if (val == 'Source') {
                this.mappingTypes[0]['filt'] = val;
                this.mappingTypes[1]['filt'] = val;
            } else if (val == 'Category') {
                this.mappingTypes[0]['filt'] = val;
                this.mappingTypes[1]['filt'] = val;
            } else if (val == 'Currency') {
                this.mappingTypes[0]['filt'] = val;
            } else if (val == 'GL Date') {
                //this.mappingTypes[0]['filt'] = val;
                this.mappingTypes[2]['filt'] = val;
            } else if (val == 'Period') {
               // this.mappingTypes[0]['filt'] = val;
                this.mappingTypes[2]['filt'] = val;
            } else if (val == 'Rate') {
                this.mappingTypes[0]['filt'] = val;
            } else if (val == 'Conversion Type') {
                //this.mappingTypes[0]['filt'] = val;
                this.mappingTypes[2]['filt'] = val;
            } else if (val == 'Journal Generation Level') {
                this.mappingTypes[2]['filt'] = val;
            } else if (val == 'Debit and Credit Should Balance') {
                this.mappingTypes[2]['filt'] = val;
            }
        }
        this.mappingTypesa[ind] = this.mappingTypes.filter((item) => item.filt == val);
        console.log('this.mappingTypesa ' + JSON.stringify(this.mappingTypesa));
    }
    OnViewDeSelect(obj: any) {
        this.journalsHeader.viewId = null;
        this.journalsHeader.viewName = null;
        for (var i = 0; i < this.journalHeaders.length; i++) {
            this.journalHeaders[i].viewId = '';
            this.journalHeaders[i].mappingType = '';
            this.journalHeaders[i].value = '';
        }
        $('#disp_1').css('display', 'none');
        $('#disp_2').css('display', 'none');
        $('#disp_3').css('display', 'none');
        $('#disp_4').css('display', 'none');
        $('#disp_5').css('display', 'none');
        $('#disp_6').css('display', 'none');
        $('#disp_7').css('display', 'none');
        $('#disp_8').css('display', 'none');
        $('#disp_9').css('display', 'none');
        $('#disp_10').css('display', 'none');
    }
    /* Function to filter mapping values based on selected mapping type */
    getType(val: any, name: any, ind: any) {
        console.log('val ' + val + ' name ' + name + ' ind ' + ind);
        /* for(var i=0;i<this.mappingValues.length;i++){
            if(){

            }
        } 
        if(val == "MAPPING_SET"){

        } */
        if (val == "MAPPING_SET") {
            // this.mappingValue[ind] = this.mappingValues.filter((item) => item.id == null);
            this.mappingValue[ind] = this.mappingValues.filter((item) => item.description != null);

        }

        for (var i = 0; i < this.mappingValue.length; i++) {

            if (this.mappingValue[i] == null) {
                let temp = [];
                this.mappingValue[i] = temp;
            }
        }
        console.log('mappingValue[ind] ' + JSON.stringify(this.mappingValue));
    }
    /* Function to add column at line derivation rule */
    addColumn() {
        let count = 0;
        for (var i = 0; i < this.jeLineDerivations.length; i++) {
            if (!this.jeLineDerivations[i].sViewColumn || !this.jeLineDerivations[i].operator || !this.jeLineDerivations[i].value) {
                count++;
            } else {

            }
        }
        if (count > 0) {
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields to add another column'
            )
        } else {
            let obj = {
                "value": '',
            };
            this.jeLineDerivations.push(obj);
        }
    }
    /* Function to delete column at line derivation rule */
    deleteColumn(col: any) {
        console.log('delete col ' + JSON.stringify(col));
        if (this.jeLineDerivations.length > 1) {
            this.jeLineDerivations.splice(this.jeLineDerivations.indexOf(col), 1);
        } else {
            this.notificationsService.error(
                'Warning!',
                'Atleast one journal line derivation rule should exist'
            )
        }
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

    /* load(id) {
         this.templateDetailsService.find(id).subscribe((templateDetails) => {
            this.templateDetails = templateDetails;
        }); 
    } */
    /*  previousState() {
         window.history.back();
     } */

    ngOnDestroy() {
    }

    /* registerChangeInTemplateDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'templateDetailsListModification',
            (response) => this.load(this.templateDetails.id)
        );
    } */
}
