import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService } from 'ng-jhipster';

import { RuleConditions } from './rule-conditions.model';
import { RuleConditionsService } from './rule-conditions.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';


@Component({
    selector: 'jhi-rule-conditions',
    templateUrl: './rule-conditions.component.html'
})
export class RuleConditionsComponent implements OnInit, OnDestroy {
ruleConditions: RuleConditions[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    selectedReport: string;
    selectedReport1: string;
    reportType: string;
    selectedItems=[];
    checked: boolean = false;
    limitSelectionSettings = {
        text: "You can select multiple",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: true,
        badgeShowLimit: 1
    };
    dropdownList = [
        { "id": 1, "itemName": "India" },
        { "id": 2, "itemName": "Singapore" },
        { "id": 3, "itemName": "Australia" },
        { "id": 4, "itemName": "Canada" },
        { "id": 5, "itemName": "South Korea" },
        { "id": 6, "itemName": "Germany" },
        { "id": 7, "itemName": "France" },
        { "id": 8, "itemName": "Russia" },
        { "id": 9, "itemName": "Italy" },
        { "id": 10, "itemName": "Sweden" }
    ];
    
    finalFields = [];
    startDate = new Date(1990, 0, 1);
    
    seasons = [
        'Winter',
        'Spring',
        'Summer',
        'Autumn',
    ];

    constructor(
        private ruleConditionsService: RuleConditionsService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }
    
    reportSelected(){
        console.log(this.selectedReport);
        this.finalFields = [];
        this.selectedReport1='';
        this.json.forEach(item=>{
            if(item.report_type === this.selectedReport){
                this.finalFields.push(item);                
            }    
        })
    }
    
    onSelectTemplateAndDataView(selectedItems){
        
    }
    
    changed(){
        console.log('Hi'+this.checked);
    }
    
    
    reports = [
        { value: 'Account Analysis', viewValue: 'Account Analysis' },
        { value: 'Trail Balance', viewValue: 'Trail Balance' }
    ];
    
    reports1 = [
        { value: 'Account Analysis1', viewValue: 'Account Analysis1' },
        { value: 'Trail Balance1', viewValue: 'Trail Balance1' }
    ];

    json=[
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "Ledger Name",
          "Position": 1,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "Period From",
          "Position": 2,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "Period To",
          "Position": 3,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "currency",
          "Position": 4,
          "Type": "Multi",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "report type",
          "Position": 5,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "Detail",
          "Position": 6,
          "Type": "Radio",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-1",
          "parameters": "accounted",
          "Position": 7,
          "Type": "Radio",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-2",
          "parameters": "report group",
          "Position": "",
          "Type": "Yes",
          "levels": "Level-1",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Account Analysis",
          "report_name": "Account Analysis / summary",
          "Category": "sub query window-3",
          "parameters": "account code combinations",
          "Position": "",
          "Type": "Yes",
          "levels": "Segment-1",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-1",
          "parameters": "Ledger Name",
          "Position": 1,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-1",
          "parameters": "Period",
          "Position": 2,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-1",
          "parameters": "currency",
          "Position": 3,
          "Type": "Multi",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-1",
          "parameters": "report type",
          "Position": 4,
          "Type": "LOV",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-1",
          "parameters": "Detail",
          "Position": 5,
          "Type": "Radio",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-1",
          "parameters": "accounted",
          "Position": 6,
          "Type": "Radio",
          "levels": "",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-2",
          "parameters": "report group",
          "Position": "",
          "Type": "Yes",
          "levels": "Level-1",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        },
        {
          "report_type": "Trail Balance",
          "report_name": "Trail Balance / summary",
          "Category": "sub query window-3",
          "parameters": "account code combinations",
          "Position": "",
          "Type": "Yes",
          "levels": "Segment-1",
          "pivot rows": "FALSE",
          "pivot columns": "FALSE",
          "filters": "TRUE"
        }
      ];
}
