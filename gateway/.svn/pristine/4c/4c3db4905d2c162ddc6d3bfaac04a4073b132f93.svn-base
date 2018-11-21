import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { CommonService } from '../../entities/common.service';
import { LedgerDefinition } from './ledger-definition.model';
import { LedgerDefinitionService } from './ledger-definition.service';
import { CalendarService } from '../calendar/calendar.service';
import { ChartOfAccountService } from '../chart-of-account/chart-of-account.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-ledger-definition-detail',
    templateUrl: './ledger-definition-detail.component.html',
})
export class LedgerDefinitionDetailComponent implements OnInit, OnDestroy {

    ledgerDefinition = new LedgerDefinition();
    isCreateNew= false;
    isEdit = false;
    isViewOnly = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA = false;
    ledgerType: any;
    currencies: any;
    calendars: any;
    chartOfAccounts: any;
    nameExist: string;
    statuses = [{code: true,value: 'Active'},{code: false,value: 'Inactive'}];
    currentDate = new Date();
    crnySearch;
    private unsubscribe: Subject<void> = new Subject();

    constructor(
        private ledgerDefinitionService: LedgerDefinitionService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        private calendarService: CalendarService,
        private chartOfAccountService: ChartOfAccountService
    ) {}

    ngOnInit() {
        // Get ledger details function
        this.loadAll();

        // Get ledger type lookup code function
        this.lookupcode()

        // Load All Currencies
        this.currencies = this.commonService.currencies;

        // Get all calendars
        this.getCalendars();

        // Get all chart of accounts
        this.getChartOfAccounts();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    // Load Ledger definition details
    loadAll(){
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;

            if (params['id']) {
                this.ledgerDefinitionService.find(params['id']).takeUntil(this.unsubscribe).subscribe(
                    (ledgerDefinition) => {
                        this.ledgerDefinition = ledgerDefinition;
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            // Focusing on first element
                            $('#name').focus();
                        } else {
                            this.ledgerDefinition.description = !this.ledgerDefinition.description ? '-' : this.ledgerDefinition.description;
                            this.ledgerDefinition.endDate = !this.ledgerDefinition.endDate ? '-' : this.ledgerDefinition.endDate;
                            this.isViewOnly = true;
                        }
                    }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                // Focusing on first element
                $('#name').focus();
                this.ledgerDefinition.startDate = new Date();
            }
        });
    }

    // Toggle Sidebar
    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }

    // Get Ledger type
    lookupcode(){
        this.ledgerDefinitionService.lookupCodes('LEDGER_TYPE').takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.ledgerType = res;
        });
    }

    // Get Calendars
    getCalendars(){
        this.calendarService.getAllcalenderList(0, 0, true).takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.calendars = res.json();
        });
    }

    // Get Chart of accounts
    getChartOfAccounts(){
        this.chartOfAccountService.getAllChartOfAccounts(0, 0, true).takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.chartOfAccounts = res.json();
        });
    }

    // Save Ledger
    saveLedger() {
        if (!this.nameExist) {
            this.ledgerDefinitionService.checkLedgerIsExist(this.ledgerDefinition.name, this.ledgerDefinition.id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.nameExist) {
                    let link: any = '';
                    if (this.ledgerDefinition.id) {
                        // Update Ledger Definition
                        this.ledgerDefinitionService.update(this.ledgerDefinition).takeUntil(this.unsubscribe).subscribe((updRes: any) => {
                            this.commonService.success('Success!','Ledger "' + updRes.name + '" Successfully Updated');
                            if (updRes.id) {
                                link = ['/ledger-definition', { outlets: { 'content': updRes.id + '/details' } }];
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
                        // Create New Ledger Definition
                        this.ledgerDefinition.status = 'TRUE';
                        this.ledgerDefinition.enabledFlag = true;
                        this.ledgerDefinition.tenantId = this.commonService.currentAccount.tenantId;
                        this.ledgerDefinition.createdBy = this.commonService.currentAccount.id;
                        this.ledgerDefinition.lastUpdatedBy = this.commonService.currentAccount.id;
                        this.ledgerDefinition.lastUpdatedDate = new Date();
                        this.ledgerDefinition.createdDate = new Date();
                        this.ledgerDefinitionService.create(this.ledgerDefinition).takeUntil(this.unsubscribe).subscribe((creRes: any) => {
                            this.commonService.success('Success!','Ledger "' + creRes.name + '" Successfully Created');
                            if (creRes.id) {
                                link = ['/ledger-definition', { outlets: { 'content': creRes.id + '/details' } }];
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
                }
            });
        }
    }


    // Function to display validition message
    validationMsg() {
        if (this.ledgerDefinition.name == null || this.ledgerDefinition.name == '') {
            this.commonService.error('Warning!','Name is mandatory');
        } else if (this.ledgerDefinition.ledgerType == null) {
            this.commonService.error('Warning!','Ledger Type is mandatory');
        } else if (this.ledgerDefinition.startDate == null) {
            this.commonService.error('Warning!','Start date is mandatory');
        } else {
            this.commonService.error('Warning!','Fill mandatory fields');
        }
    }

    // Check calendar name duplicates
    isNameExist(name, id) {
        if (name && !this.nameExist) {
            this.ledgerDefinitionService.checkLedgerIsExist(name, id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
            });
        }
    }
}
