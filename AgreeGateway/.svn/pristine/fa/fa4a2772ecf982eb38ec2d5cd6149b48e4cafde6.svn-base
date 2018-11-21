import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';
import { JhiDateUtils } from 'ng-jhipster';
import { RuleGroupPopupService } from './rule-group-popup.service';
import { RuleGroupService } from './rule-group.service';
import { RuleGroupWithRulesAndConditions } from './ruleGroupWithRulesAndConditions.model'
import { RuleGroupAndRuleWithLineItem } from './ruleGroupAndRuleWithLineItem.model';
import { AccRule } from './accRule.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { LineItem } from './LineItem.model';
import { LineDerivations } from './line-derivations.model';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { DataViewsService } from '../data-views/data-views.service';
import { AccountingRuleConditions } from './accounting-rule-conditions.model';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { RulesService } from '../rules/rules.service';
import { Rules } from '../rules/rules.model';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { SessionStorageService } from 'ng2-webstorage';
import { MappingSetService } from '../mapping-set/mapping-set.service';
import { CommonService } from '../common.service';
import { Router, NavigationEnd } from '@angular/router';
import { NotificationsService } from 'angular2-notifications-lite';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { ReconcileService } from '../reconcile/reconcile.service';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
declare var jQuery: any;
declare var $: any;
const URL = '';
@Component( {
    selector: 'jhi-rule-group-accounting',
    templateUrl: './rule-group-accounting.component.html'
} )
export class RuleGroupAccountingComponent {

    //ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
    @Input() accountingMode: string = '';
    mouseOverRowNo: number = -1;
    routeSub: any;

    mouseOverRule: any;
    mouseOverLineItem: any;
    mouseOverRC: any;
    mouseOverDer: any;

    ruleListformArray: FormGroup[] = [];
    ruleListArrays: any[] = [];
    filteredRuleList: any[] = [];

    dataViewformArray: FormGroup[][];
    dvArrays: any[] = [];
    sourceDataViewsArrays: any[] = [];

    myControl: FormControl = new FormControl();
    filteredOptions: Observable<any[]>;
    options = [
        'One',
        'Two',
        'Three'
    ];
    constantLength = {};
    containsConstant = [];

    ruleCreationInWQ: boolean = false;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;

    linetypes = [];
    chartOfAccounts = [];
    segments = [];
    sourceDataViews = [];
    operators = [];

    columnLOV = [];
    columnsByViewId = [];

    copiedArray = [];
    mappingSets = [];

    formula = [];
    function = [];
    excelFunctions = [];
    constructor(
        private jhiLanguageService: JhiLanguageService,
        public ruleGroupService: RuleGroupService,
        private eventManager: JhiEventManager,
        private dateUtils: JhiDateUtils,
        private config: NgbDatepickerConfig,
        private lookUpCodeService: LookUpCodeService,
        private dataViewsService: DataViewsService,
        private builder: FormBuilder,
        private rulesService: RulesService,
        private _sanitizer: DomSanitizer,
        private $sessionStorage: SessionStorageService,
        private mappingSetService: MappingSetService,
        private commonService: CommonService,
        private router: Router,
        private route: ActivatedRoute,
        private notificationsService: NotificationsService,
        private reconcileService: ReconcileService

    ) {
    }

    clear() {
        // this.activeModal.dismiss('cancel');
    }

    ngOnInit() {
        console.log( 'in acc  form' );
        this.lookUpCodeService.fetchLookUpsByLookUpType( 'ACCOUNTING_FUNCTIONS' ).subscribe(( res: any ) => {
            this.excelFunctions = res;
        } );
        this.dataViewformArray = [];
        $( ".split-example" ).css( {
            'height': 'auto',
            'min-height': ( this.commonService.screensize().height - 130 ) + 'px'
        } );
        this.commonService.callFunction();
        //form arrays
        let ruleListForm: FormGroup;
        ruleListForm = this.builder.group( {
            data: "",
        } );
        this.ruleListformArray[0] = ruleListForm;

        let url = this.route.snapshot.url.map( segment => segment.path ).join( '/' );
        console.log( 'url' + JSON.stringify( url ) );
        this.dataViewsService.dataViewList().subscribe(( res: any ) => {
            this.sourceDataViews = res;
            this.routeSub = this.route.params.subscribe( params => {
                if ( params['id'] ) {
                    //fetch by id

                    if ( url.endsWith( 'edit' ) ) {
                        this.fetchLists();
                        this.isEdit = true;
                        this.fetchAccountingruleGroupByGrpId( params['id'] );
                    }
                    else {
                        //this.fetchLists();
                        this.fetchAccountingruleGroupByGrpId( params['id'] );
                        this.isViewOnly = true;
                    }
                } else {
                    if ( this.accountingMode.toString().endsWith( 'WQ' ) ) {
                        this.ruleCreationInWQ = true;
                    }
                    this.fetchLists();
                    this.addNewRuleObject( 0, 0 );
                    this.isCreateNew = true;
                }

            } );
        } );
    }
    //If Start Date Entered Apply Class
    startEndDateClass( indexVal: any ) {
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].startDate != null ) {
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        } else {
            return 'col-md-6 col-sm-6 col-xs-12 form-group';
        }
    }
    fetchLists() {
        this.lookUpCodeService.fetchLookUpsByLookUpType( 'LINE_TYPE' ).subscribe(( res: any ) => {
            this.linetypes = res;
        } );
        this.lookUpCodeService.fetchLookUpsByLookUpType( 'CHART_OF_ACCOUNTS' ).subscribe(( res: any ) => {
            this.chartOfAccounts = res;
        } );


        this.mappingSetService.getMappingSetsByTenantId().subscribe(( res: any ) => {
            this.mappingSets = res;
        } );
    }
    fetchAccountingruleGroupByGrpId( grpId ) {
        this.ruleGroupService.getAccountingRuleGroupDetails( grpId ).subscribe(( res: any ) => {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems = res;
            console.log( 'in detail acc rules fetched are:' + JSON.stringify( this.ruleGroupService.ruleGrpWithRuleAndLineItems ) );
            if ( this.isEdit ) {
                let i = 0;
                if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems && this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length > 0 ) {
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.forEach( rules => {
                        if ( !this.columnLOV )
                            this.columnLOV = [];
                        if ( !this.columnLOV[i] )
                            this.columnLOV[i] = [];
                        let j = 0;
                        rules.lineItems.forEach( lineItem => {
                            if ( lineItem.accountingRuleConditions ) {
                                if ( !this.columnLOV[i][j] )
                                    this.columnLOV[i][j] = [];
                                this.columnLOV[i][j] = rules.lineItems[0].columnsForDV[0].dvColumnsList;
                                //fetchOperators
                                let rcIndex: number = 0;
                                lineItem.accountingRuleConditions.forEach( condition => {
                                    this.SelectColumn( i, j, rcIndex, condition.sViewColumnId );
                                    rcIndex = rcIndex + 1;
                                } );

                            }
                            j = j + 1;

                        } );
                        i = i + 1;
                    } );
                }

                //if ( this.isEdit )
                this.setFormGroupsForExisting();
            }
            else {
                console.log( 'its in else bcz restricted' );
            }

        } );
    }
    setFormGroupsForExisting() {

        for ( var i = 0; i < this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length; i++ ) {
            let ruleListForm: FormGroup;
            ruleListForm = this.builder.group( {
                data: "",
            } );
            this.ruleListformArray[i] = ruleListForm;
        }
        //set dv forms
        let indexVal: number = 0;
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.forEach( rule => {
            let childIndex: number = 0;
            if ( !this.dataViewformArray[indexVal] )
                this.dataViewformArray[indexVal] = [];
            this.sourceDataViewsArrays[indexVal] = [];
            rule.lineItems.forEach( lineItem => {
                let dvForm: FormGroup;
                dvForm = this.builder.group( {
                    data: "",
                } );
                this.dataViewformArray[indexVal][childIndex] = dvForm;
                this.sourceDataViewsArrays[indexVal][childIndex] = this.sourceDataViews;
                childIndex = childIndex + 1;
            } );
            indexVal = indexVal + 1;
        } );
        this.fetchRulesBasedOnRuleGroupType( i );
    }
    /* confirmDelete (id: number) {
         this.ruleGroupService.delete(id).subscribe(response => {
             this.eventManager.broadcast({
                 name: 'ruleGroupListModification',
                 content: 'Deleted an ruleGroup'
             });
             this.activeModal.dismiss(true);
         });
     }*/
    addNewRuleObject( indexVal: any, childIndex: any ) {
        let coaLine = new LineDerivations();

        let lineItem = new LineItem();
        //lineItem.coaLines = [];
        //lineItem.coaLines.push(coaLine);
        let lineItems: LineItem[] = [];
        lineItems.push( lineItem );
        let rulesObj = new AccRule();
        rulesObj.lineItems = lineItems;
        if ( indexVal == 0 ) {
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems )
                this.ruleGroupService.ruleGrpWithRuleAndLineItems['rules'] = [];
            //put one empty object into rules

            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0] = rulesObj;
        }
        else {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal] = rulesObj;
        }
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].priority = indexVal + 1;
        this.copiedArray[indexVal] = [];
        this.copiedArray[indexVal][0] = false;
        if ( !this.columnLOV[indexVal] )
            this.columnLOV[indexVal] = [];


        let myForm: FormGroup;
        myForm = this.builder.group( {
            data: "",
        } );
        this.ruleListformArray[indexVal] = ( myForm );
        this.fetchRuleList( indexVal );
        this.dataViewformArray[indexVal] = [];
        this.sourceDataViewsArrays[indexVal] = [];
        if ( childIndex == 0 ) {
            let dvForm: FormGroup;
            dvForm = this.builder.group( {
                data: "",
            } );
            this.dataViewformArray[indexVal][childIndex] = dvForm;
            this.sourceDataViewsArrays[indexVal][childIndex] = [];
            this.sourceDataViewsArrays[indexVal][childIndex] = this.sourceDataViews;
        }
        //this.fetchRulesBasedOnRuleGroupType(indexVal);
        this.operators[indexVal] = [];
        this.containsConstant = [];
    }
    SelectCOA( indexVal: any, childIndex: any ) {
        this.lookUpCodeService.fetchLookUpsByLookUpType( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].coa ).subscribe(( res: any ) => {
            this.segments = res;

            if ( this.segments ) {
                if ( childIndex ) {
                    if ( !this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[childIndex].accountingRuleDerivations )
                        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[childIndex].accountingRuleDerivations = [];
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[childIndex].accountingRuleDerivations = [];
                    this.segments.forEach( segment => {
                        let coaLine = new LineDerivations();
                        coaLine.accountingReferencesCode = segment.lookUpCode;
                        coaLine.accountingReferencesMeaning = segment.meaning;
                        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[childIndex].accountingRuleDerivations.push( coaLine );
                    } );
                }
                else {
                    let j = 0;
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems.forEach( lineItem => {
                        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[j].accountingRuleDerivations = [];
                        this.segments.forEach( segment => {
                            let coaLine = new LineDerivations();
                            coaLine.accountingReferencesCode = segment.lookUpCode;
                            coaLine.accountingReferencesMeaning = segment.meaning;
                            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[j].accountingRuleDerivations.push( coaLine );
                        } );
                        j = j + 1;
                    } );

                }
            }
        } );
    }

    fetchColumnsByDVId( id, i, j ) {
        this.dataViewsService.getDataViewById( id ).subscribe(( res: any ) => {
            let dvAndCols = res;
            if ( dvAndCols[0] && dvAndCols[0].dvColumnsList ) {
                this.columnsByViewId = dvAndCols[0].dvColumnsList;
                if ( !this.columnLOV[i] )
                    this.columnLOV[i] = [];
                if ( !this.columnLOV[i][j] )
                    this.columnLOV[i][j] = [];

                this.columnLOV[i][j] = this.columnsByViewId;
            }

        } );

    }
    addMappingCriteria( i, j, rcIndex ) {
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j] ) {
            let accountingRuleConditions = new AccountingRuleConditions();
            accountingRuleConditions.sequence = rcIndex + 1;
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions ) {

            }
            else {
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions = [];
            }

            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions[rcIndex] = ( accountingRuleConditions );
        }
        else {
            /*this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j] = [];*/
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions = [];
            let accountingRuleConditions = new AccountingRuleConditions();
            accountingRuleConditions.sequence = rcIndex + 1;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions[rcIndex] = ( accountingRuleConditions );
        }
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j] ) {
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions ) {
                if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.length > 1 ) {
                    let len = this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.length - 2;
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions[len].logicalOperator = 'AND';
                }
            }
        }
        if ( rcIndex == 0 ) {
            this.operators[i][j] = [];
        }

        this.operators[i][j][rcIndex] = [];
    }
    SelectOperator() {

    }
    SelectColumn( i, j, mcIndex, columnId ) {
        let dataType: string = '';
        if ( !this.columnLOV[i] )
            this.columnLOV[i] = [];
        if ( !this.columnLOV[i][j] ) {
            this.columnLOV[i][j] = [];
            if ( columnId )
                this.fetchColumnsByDVId( columnId, i, j );
        }
        this.columnLOV[i][j].forEach( col => {
            if ( col.id === columnId ) {
                dataType = col.colDataType;
            }
        } );
        if ( !this.operators )
            this.operators = [];

        if ( !this.operators[i] )
            this.operators[i] = [];

        if ( !this.operators[i][j] )
            this.operators[i][j] = [];

        if ( !this.operators[i][j][mcIndex] )
            this.operators[i][j][mcIndex] = [];
        if ( dataType ) {
            this.lookUpCodeService.fetchLookUpsByLookUpType( dataType ).subscribe(( res: any ) => {
                let operatorss = res;
                this.operators[i][j][mcIndex] = operatorss;
            } );

        }


    }
    copyLineItem( i, j ) {
        //issue: doesnot copy the first row of rule conditions
        /**
         * should copy dataview, rule conditions, coa value,
         * coa related lineshas to be fetched
         * lineItemType, constant/mapping set in Account derivation lines has to be empty
         */
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sameAsAbove ) {
            this.clearCopiedSameAsAbove( i, j );
        }
        else {
            let copiedLineItem = new LineItem();
            // copiedLineItem =   JSON.parse(JSON.stringify(this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j-1]))
            copiedLineItem = jQuery.extend( true, {}, this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j - 1] );
            if ( copiedLineItem.sourceDataviewId ) {
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewId = copiedLineItem.sourceDataviewId;
                if ( copiedLineItem.sourceDataviewDisplayName ) {
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewDisplayName = copiedLineItem.sourceDataviewDisplayName;
                    this.fetchColumnsByDVId( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewId, i, j );
                }

                //this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].lineItemType = copiedLineItem.lineItemType;
                if ( copiedLineItem.accountingRuleConditions ) {
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions = this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j - 1].accountingRuleConditions;
                }
                this.columnLOV[i][j] = this.columnLOV[i][j - 1];
                let mcIndex = 0;
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.forEach( condition => {
                    this.operators[i][j][mcIndex] = this.operators[i][j - 1][mcIndex];
                    mcIndex = mcIndex + 1;
                } );
            }
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations )
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations.forEach( der => {
                    der.dataViewColumn = null;
                    der.mappingSetId = null;
                    der.constantValue = null;
                } );

            //fetch previous line's account derivation rules
            //this.SelectCOA( i, j );
        }

    }
    clearCopiedSameAsAbove( i, j ) {
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewId = null;
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewDisplayName = '';
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions = [];
        // this.addMappingCriteria( i, j, 0 );
    }
    SelectLineType() {

    }
    addNewLineItem( i, lineItemLength, mappingCriteriaIndex ) {
        let lineItem = new LineItem();

        let ruleCondition = new AccountingRuleConditions();
        let accountingRuleConditions: AccountingRuleConditions[] = [];
        accountingRuleConditions.push( ruleCondition );
        let accountingRuleDerivations: LineDerivations[];
        lineItem.accountingRuleDerivations = accountingRuleDerivations;
        lineItem.accountingRuleConditions = accountingRuleConditions;
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i] && this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems.length >= 0 )
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[lineItemLength] = lineItem;
        if ( lineItemLength > 0 && this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].coa ) {
            this.SelectCOA( i, lineItemLength );
            /* this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[lineItemLength]. accountingRuleDerivations = this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[lineItemLength-1]. accountingRuleDerivations ;
             for(var j=0;j< this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[lineItemLength]. accountingRuleDerivations.length;j++)
                 {
                 this.clearDerivedValues(i, lineItemLength, j);
                 }
             */
        }
        if ( this.copiedArray[i] ) {

            if ( this.copiedArray[i][lineItemLength] ) {

            }
            else {
                if ( lineItemLength > 0 ) {
                    this.copiedArray[i][lineItemLength] = false;
                }
            }
        }
        else {
            this.copiedArray[i] = [];
            this.copiedArray[i][lineItemLength] = false;
        }
        let dvForm: FormGroup;
        dvForm = this.builder.group( {
            data: "",
        } );
        if ( !this.dataViewformArray[i] )
            this.dataViewformArray[i] = [];
        this.dataViewformArray[i][lineItemLength] = dvForm;
        this.sourceDataViewsArrays[i][lineItemLength] = [];
        this.sourceDataViewsArrays[i][lineItemLength] = this.sourceDataViews;
        this.operators[i][lineItemLength] = [];
    }
    SelectDVCol() {

    }
    addnewLists( indexVal: any ) {
        let myForm: FormGroup;
        myForm = this.builder.group( {
            data: "",
        } );
        this.ruleListformArray[indexVal] = ( myForm );
        /*  let lengthOfRules : number =0;
          lengthOfRules =  this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length;
          this.columnLOV[indexVal] = [];
          let childIndex : number = 0;
          this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems.forEach(lineItem=>{
              this.columnLOV[indexVal][childIndex] = [];
              childIndex = childIndex +1;
          });*/
    }
    fetchRulesBasedOnRuleGroupType( indexVal: any ) {
        //get rule type at that index and filter, and update the filter values
        this.fetchRuleList( indexVal );


    }
    fetchRuleList( indexVal ) {
        this.rulesService.getAccountingRules( 'ACCOUNTING' ).subscribe(( res: any ) => {
            let rulesFetched: any = [];
            rulesFetched = res;
            this.filteredRuleList = [];
            if ( rulesFetched )
                rulesFetched.forEach( rule => {
                    this.filteredRuleList.push( rule );
                } );
            this.ruleListArrays[indexVal] = [];
            this.ruleListArrays[indexVal] = this.filteredRuleList;
        } );


    }
    dataViewListFormatter = ( data: any ) => {
        let html = `<span >${data.dataViewDispName}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml( html );
    }
    autocompleListFormatter = ( data: any ) => {
        let html = `<span >${data.ruleCode}  </span>`;
        return this._sanitizer.bypassSecurityTrustHtml( html );
    }

    setRuleObject( ruleName: any, indexVal: any, value: any ) {
        //set 
        if ( value.data && value.data.ruleCode )
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].ruleCode = value.data.ruleCode;
        if ( value.data && value.data.id ) {
            //this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal]=value.data.rule;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].id = value.data.id;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].startDate =
                this.dateUtils.convertLocalDateFromServer( value.data.startDate );
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].endDate =
                this.dateUtils.convertLocalDateFromServer( value.data.endDate );
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].coa = value.data.coa;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems = value.data.lineItems;//after integrating api , fetch line items to display
            //fetch columns
            let loopVariable: number = 0;
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems )
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems.forEach( lineItem => {
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[loopVariable].sourceDataviewId = value.data.lineItems[loopVariable].sourceDataviewId;
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[loopVariable].sourceDataviewDisplayName = value.data.lineItems[loopVariable].sourceDataviewName;
                    this.fetchColumnsByDVId( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[loopVariable].sourceDataviewId, indexVal, loopVariable );
                    //fetch operator for columns
                    let mcIndex = 0;
                    //this.fetchColumnsByDVId( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[indexVal].lineItems[loopVariable].sourceDataviewId, indexVal, loopVariable );
                    if ( lineItem.accountingRuleConditions )
                        lineItem.accountingRuleConditions.forEach( condition => {
                            condition.sequence = mcIndex + 1;
                            this.SelectColumn( indexVal, loopVariable, mcIndex, condition.sViewColumnId );
                            mcIndex = mcIndex + 1;
                        } );
                    loopVariable = loopVariable + 1;
                } );

        }
        else {
        }
    }
    /*saveToSession() {

        this.$sessionStorage.store( 'accountingRuleList', this.ruleGroupService.ruleGrpWithRuleAndLineItems );
        if ( this.ruleCreationInWQ ) {
            let ruleGrpWithRuleAndLineItems = new RuleGroupAndRuleWithLineItem();
            ruleGrpWithRuleAndLineItems = this.$sessionStorage.retrieve( 'accountingRuleList' );
            //set adhoc type here
            ruleGrpWithRuleAndLineItems.rulePurpose = 'ORPHAN_ACCOUNTING';
            ruleGrpWithRuleAndLineItems.name = 'Orphan Accounting';
            ruleGrpWithRuleAndLineItems.startDate = new Date();
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].ruleType='ADHOC';
            let groupId = [];
            groupId = this.accountingMode.toString().split("-");
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.id = groupId[0];
            this.ruleGroupService.getAccountingRuleGroupDetails( groupId[0] ).subscribe(( res: any ) => {
                let ruleGrpById = new RuleGroupAndRuleWithLineItem();
                ruleGrpById = res;
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].assignmentFlag=true;
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].enabledFlag=true;
                ruleGrpById.rules.push(this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0]);
                this.ruleGroupService.testPost(ruleGrpById ).subscribe(( res: any ) => {
                    let savedObj = [];
                    savedObj = res;
                    this.$sessionStorage.store('WQAccountingRule', savedObj );
                    console.log( 'saved object after saving' + JSON.stringify( savedObj ) );
                    this.notificationsService.success(
                            '',
                            'Job initiated with '+ ruleGrpWithRuleAndLineItems.rules[0].ruleCode
                        )
                        this.reconcileService.ENABLE_RULE_BLOCK = false;
                } );
            });
           
           
        }
        else {
            let msg = '';
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length > 1 )
                msg = ' rules tagged successfully!';
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length == 1 )
                msg = ' rule tagged successfully!';
            this.notificationsService.success(
                '',
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length + msg
            )
        }


    }*/

    SelectMappingSet() {
    }
    setDataview( i, j, value ) {
        if ( value.data.id ) {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewId = value.data.id;
            if ( value.data.dataViewName )
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewName = value.data.dataViewName;
            if ( value.data.dataViewDispName )
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].sourceDataviewDisplayName = value.data.dataViewDispName;

            if ( !this.isViewOnly && ( !this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions || this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.length <= 0 ) )
                this.addMappingCriteria( i, j, 0 );
        }
        else {
            //  if 
        }

        if ( value.data && value.data.id ) {
            this.fetchColumnsByDVId( value.data.id, i, j );
        }

    }
    ChangeDataView( dvName: any, i: any, j: any, value: any ) {
        let matchedDV = {};
        let loopVariable = 0;
        let matched: boolean = false;
        if ( this.sourceDataViewsArrays[i][j] ) {
            let dataView: any = {};
            this.sourceDataViewsArrays[i][j].forEach( dv => {
                if ( dvName === dv.dataViewDispName ) {
                    dataView = dv;
                    matched = true;
                }
                loopVariable = loopVariable + 1;

            } );
            if ( matched ) {

                matchedDV = { "data": dataView };
                this.setDataview( i, j, matchedDV );
            }
        }
        else {
        }

    }
    changeRuleName( val, i ) {
        let matchedRule = {};
        let loopVariable = 0;
        let matched: boolean = false;
        if ( this.ruleListArrays[i] )
            if ( this.ruleListArrays[i] )
                this.ruleListArrays[i].forEach( rule => {
                    if ( rule.ruleCode === val ) {
                        matchedRule = { "data": rule };
                        matched = true;
                    }
                    else {
                    }
                    if ( !matched )
                        loopVariable = loopVariable + 1;
                    else {
                    }

                } );
        if ( matched )
            this.setRuleObject( val, i, matchedRule );

        if ( val == "" || val == undefined ) {
            let ruleObj = new AccRule();
            if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i] && this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].ruleCode ) {
                let existingId: number = 0;
                if ( this.ruleListArrays[i] ) {
                    this.ruleListArrays[i].forEach( ruleObject => {
                        if ( ruleObject.ruleCode == this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].ruleCode ) {
                            existingId = ruleObject.id;
                        }
                    } );
                }
                if ( existingId > 0 ) {
                    this.deleteRuleObject( i );
                    this.addNewRuleObject( i, 0 );
                }
                else {
                    this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].ruleCode = '';
                }


            }

        }
        else if ( val != "" ) {
            if ( !matched ) {
                /*this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].id=null;
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].coa=null;
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].startDate=null;
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].endDate=null;
                let lineItems: LineItem[]   = [];
                let lineItem = new LineItem();
                lineItems.push(lineItem);
                this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems = lineItems;*/
            }
        }
    }
    clearRuleObject( i ) {
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i] ) {

            let rule = new AccRule();
            let lineItems: LineItem[] = [];
            let lineItem = new LineItem();
            lineItems.push( lineItem );
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i] = rule;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].priority = i + 1;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems = lineItems;
            this.addNewLineItem( i, 0, 0 );
        }
    }
    clearLineItem( i, j ) {
    }
    deleteRuleObject( i ) {

        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.splice( i, 1 );
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules.length == 0 ) {
            this.addNewRuleObject( 0, 0 );
            /*     let rule = new AccRule();
                 let lineItems: LineItem[]   = [];
                 let lineItem = new LineItem();
                 lineItems.push(lineItem);
                 this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i] = rule;
                 this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems = lineItems;
                     this.addNewLineItem(i,0, 0);*/
        }
    }
    deleteRuleCondition( i, j, mcIndex ) {
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.length == 1 ) {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.splice( mcIndex, 1 );
            this.addMappingCriteria( i, j, 0 );
        }
        else if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.length > 1 ) {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions.splice( mcIndex, 1 );
        }

    }
    clearDerivedValues( i, j, coaIndex ) {
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].dataViewColumn ) {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].dataViewColumn = null;
        }
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].constantValue ) {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].constantValue = null;
        }
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].mappingSetId > 0 ) {
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].mappingSetId = null;
        }
    }
    deleteLineItem( i, j: number ) {

        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems.splice( j, 1 );
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems.length <= 0 ) {
            this.addNewLineItem( i, 0, 0 );
        }

    }
    checkConstantLength( constantValue, coa, accountingReference, i, j, coaIndex ) {
        if ( this.constantLength[coa + accountingReference] ) {
            if ( this.containsConstant[i] && this.containsConstant[i][j] ) {
                if ( this.containsConstant[i][j][coaIndex] ) {
                }
                else {
                    this.containsConstant[i][j][coaIndex] = this.constantLength[coa + accountingReference];
                }
            }
            else {
                this.containsConstant[i] = [];
                this.containsConstant[i][j] = [];
                this.containsConstant[i][j][coaIndex] = this.constantLength[coa + accountingReference];
            }
        }
        else {
            if ( constantValue )
                this.constantLength[coa + accountingReference] = constantValue.length;
        }
    }

    addfunction( i, j, mcIndex ) {
        if ( !this.function )
            this.function = [];
        if ( !this.function[i] )
            this.function[i] = [];
        if ( !this.function[i][j] )
            this.function[i][j] = [];
        if ( !this.function[i][j][mcIndex] )
            this.function[i][j][mcIndex] = true;
        console.log( 'this.function[i][j][mcIndex]' + this.function[i][j][mcIndex] );
    }
    showExcelFunction( i, j, mcIndex, examp ) {
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions[mcIndex].functionExample = examp;
    }
    saveFunction( i, j, mcIndex ) {
    }
    cancelsrcFormula( i, j, mcIndex ) {
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleConditions[mcIndex].func = '';
    }
    saveAdhocRule() {
        let groupId = [];
        groupId = this.accountingMode.toString().split( "-" );
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.id = groupId[0];
        this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].ruleType = 'ADHOC';
        this.ruleGroupService.getAccountingRuleGroupDetails( groupId[0] ).subscribe(( res: any ) => {

            let ruleGroupById = new RuleGroupAndRuleWithLineItem();
            ruleGroupById = res;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].assignmentFlag = true;
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].enabledFlag = true;
            ruleGroupById.rules.push( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0] );
            this.ruleGroupService.postAdhocruleForAccounting( ruleGroupById ).subscribe(( res: any ) => {
                let savedObj = [];
                savedObj = res;

                this.notificationsService.success(
                    '',
                    'Job initiated with ' + this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[0].ruleCode
                )
                this.reconcileService.ENABLE_RULE_BLOCK = false;
            } );

        } );

    }
    /**
     * clear if constant is removed
     */
    checkForConstantValue( i, j, coaIndex ) {
        if ( this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].constantValue ) {
            console.log( 'its not empty has' + this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].constantValue );
        }
        else {
            console.log( 'no value' );
            this.ruleGroupService.ruleGrpWithRuleAndLineItems.rules[i].lineItems[j].accountingRuleDerivations[coaIndex].constantValue = null;

        }

    }

}

@Component( {
    selector: 'jhi-rule-group-delete-popup',
    template: ''
} )
export class RuleGroupDeletePopupComponent {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ruleGroupPopupService: RuleGroupPopupService
    ) { }

    /*ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.ruleGroupPopupService
                .open(RuleGroupDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }*/
}
