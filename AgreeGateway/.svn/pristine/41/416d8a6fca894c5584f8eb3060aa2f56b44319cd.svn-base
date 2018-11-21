import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { SelectItem } from 'primeng/primeng';
import { CommonService } from '../../entities/common.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';

import { Processes, ProcessesDetails } from './processes.model';
import { ProcessesService } from './processes.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SourceProfilesService } from '../source-profiles/source-profiles.service';
import { RuleGroupService } from '../rule-group/rule-group.service';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-processes',
    templateUrl: './processes.component.html'
})
export class ProcessesComponent implements OnInit {

    currentAccount: any;
    processes: Processes[];
    processesDetails = new ProcessesDetails();
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    processSetupTableColumns = [
        { field: 'desc', header: 'Description', width: '100px', align: 'left' }
    ];
    itemsPerPage: any = 10;
    processSetupRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    TemplatesHeight: any;
    columnOptions: SelectItem[];
    editprocessSetupDialog:boolean = false;
    sourceProfile:any = [];
    calendarList:any = [];
    reconRuleGroup:any = [];
    accRuleGroup:any = [];
    editable: boolean = false;
    currentSrcProfile: any = [];
    createNewProcess: boolean = false;
    prsNameDup: boolean;
    checkNewPrcs: boolean = false;

    constructor(
        private processesService: ProcessesService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private commonService: CommonService,
        private sourceProfilesService: SourceProfilesService,
        private ruleGroupService: RuleGroupService,
        private dateUtils: JhiDateUtils,
        private notificationsService: NotificationsService
    ) {
        this.columnOptions = [];
        for(let i = 0; i < this.processSetupTableColumns.length; i++) {
            this.columnOptions.push({label: this.processSetupTableColumns[i].header, value: this.processSetupTableColumns[i]});
        }
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        this.getProcessSetup();

        /** Function for Toggling Global Search **/
        $(".search-icon-body").click(function () {
            if ($(".ftlSearch md-input-container").hasClass("hidethis")) {
                $(".ftlSearch md-input-container").removeClass("hidethis");
                $(".ftlSearch md-input-container").addClass("show-this");
            } else if ($(".ftlSearch md-input-container").hasClass("show-this")) {
                var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if (value.length <= 0) { // zero-length string
                    $(".ftlSearch md-input-container").removeClass("show-this");
                    $(".ftlSearch md-input-container").addClass("hidethis");
                }
            } else {
                $(".ftlSearch md-input-container").addClass("show-this");
            }
        });
        $(".ftlSearch md-input-container .mySearchBox").blur(function () {
            var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".ftlSearch md-input-container").removeClass("show-this");
                $(".ftlSearch md-input-container").addClass("hidethis");
            }
        });

        /**Esc to close dialog */
        $(document).keyup((e) => {
            if (e.keyCode == 27) { // escape key
                if ($('.custom-dialog').hasClass('custDialogOpen')) {
                    this.closeReconDialog();
                }
           }
       });

       $(".custom-dialog-body").css('max-height',(window.innerHeight - 165)+'px');
        $(window).resize(function(){
            $(".custom-dialog-body").css('max-height',(window.innerHeight - 165)+'px');
        });
    }

    /**Get All Process Setup */
    getProcessSetup(){
        this.processesService.getProcess(this.page, this.itemsPerPage).subscribe((res)=>{
            this.processes = res.json();
            if(this.processes.length<1){
                this.addProcessSetup();
            }
            this.processSetupRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    /**Pagination function */
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.processesService.getProcess(this.page+1, this.itemsPerPage).subscribe((res: any) => {
            this.processes = res.json();
            this.processSetupRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    /**Open Dialog and show detail */
    processSetupDetail(detail){
        // console.log('detail', detail);
        this.processesService.getProcessDetail(detail.id).subscribe((res)=>{
            this.processesDetails = res[0];
            this.getSourceProfile();
            this.getRRuleGrp();
            this.getARuleGrp();
            this.fetchAllcalendarList();
            this.convertDate();
            this.editable = false;
            // console.log('this.processesDetails\n', JSON.stringify(this.processesDetails));
            if(this.processesDetails.processDetailList){
                this.processesDetails.spList = this.processesDetails.processDetailList.spList;
                this.processesDetails.spListIds = [];
                this.processesDetails.processDetailList.spList.forEach(srcPrf => {
                    this.processesDetails.spListIds.push(srcPrf.typeId);
                });
                if(this.processesDetails.processDetailList.recGrpList[0]){
                    this.processesDetails.reconcRuleGroupId = this.processesDetails.processDetailList.recGrpList[0].typeId;
                    this.processesDetails.reconcRuleGroupName = this.processesDetails.processDetailList.recGrpList[0].reconcRuleGroupName;
                }
                if(this.processesDetails.processDetailList.actGrpList[0]){
                    this.processesDetails.actRuleGroupId = this.processesDetails.processDetailList.actGrpList[0].typeId;
                    this.processesDetails.actRuleGroupName = this.processesDetails.processDetailList.actGrpList[0].actRuleGroupName;
                }
                if(this.processesDetails.processDetailList.calenderList[0]){
                    this.processesDetails.calendar = this.processesDetails.processDetailList.calenderList[0].typeId;
                    this.processesDetails.calendarName = this.processesDetails.processDetailList.calenderList[0].calendarName;
                }
                if(this.processesDetails.processDetailList.violationList[0]){
                    this.processesDetails.violation = this.processesDetails.processDetailList.violationList[0].typeId;
                    /* this.processesDetails.calendarName = this.processesDetails.processDetailList.violationList[0].calendarName; */
                }
            }            
        });
        this.editprocessSetupDialog = true;
        this.openReconDialog();
    }

    /**Save or Update Process Setup */
    saveOrUpdateProcess() {
        this.processDuplicateCheck(this.processesDetails.processName, this.processesDetails.id);
        if (this.prsNameDup == false) {
            this.processesDetails.processDetailList = {
                "spList": [],
                "actGrpList": [],
                "recGrpList": [],
                "calendarList": [],
                "violationList": []
            };
            let srcObj: any = [];
            this.processesDetails.spListIds.forEach(srcId => {
                srcObj = {
                    "tagType": "sourceProfile",
                    "typeId": srcId
                };
                this.processesDetails.processDetailList.spList.push(srcObj);
            });
            if (this.processesDetails.reconcRuleGroupId) {
                let recObj: any = {
                    "tagType": "reconciliationRuleGroup",
                    "typeId": this.processesDetails.reconcRuleGroupId
                };
                this.processesDetails.processDetailList.recGrpList.push(recObj);
            }
            if (this.processesDetails.actRuleGroupId) {
                let accObj: any = {
                    "tagType": "accountingRuleGroup",
                    "typeId": this.processesDetails.actRuleGroupId
                };
                this.processesDetails.processDetailList.actGrpList.push(accObj);
            }
            if (this.processesDetails.calendar) {
                let calObj: any = {
                    "tagType": "Calendar",
                    "typeId": this.processesDetails.calendar
                };
                this.processesDetails.processDetailList.calendarList.push(calObj);
            }
            if (this.processesDetails.violation) {
                let calObj: any = {
                    "tagType": "violation",
                    "typeId": this.processesDetails.violation
                };
                this.processesDetails.processDetailList.violationList.push(calObj);
            }
            // console.log(JSON.stringify(this.processesDetails));
            if (this.processesDetails.id) {
                this.checkNewPrcs = true;
            } else {
                this.checkNewPrcs = false;
            }
            this.processesService.postProcess(this.processesDetails).subscribe((res) => {
                // console.log('posted\n' + JSON.stringify(res));
                if (this.checkNewPrcs == true) {
                    this.notificationsService.success('Success!', 'Process Updated');
                } else {
                    this.notificationsService.success('Success!', 'Process Created');
                }
                this.getProcessSetup();
                this.closeReconDialog();
            });
        }
    }

    /**Get Profile */
    getSourceProfile() {
        this.sourceProfilesService.getAllProfilesAndConnections().subscribe((sourceProfile) => {
            this.sourceProfile = sourceProfile.json();
        });
    }

    /**Get Reconciliation Rule Groups */
    getRRuleGrp(){
        this.ruleGroupService.fetchRuleGroupsBasedOnRulePurpose('RECONCILIATION').subscribe((rRuleGroup)=>{
            this.reconRuleGroup = rRuleGroup.json();
        });
    }

    /**Get getAllcalendarList */
    fetchAllcalendarList(){
        this.processesService.getAllcalendarList().subscribe((res)=>{
            this.calendarList = res;
        });
    }

    /**Get Accounting Rule Groups */
    getARuleGrp(){
        this.ruleGroupService.fetchRuleGroupsBasedOnRulePurpose('ACCOUNTING').subscribe((aRuleGroup)=>{
            this.accRuleGroup = aRuleGroup.json();
        });
    }

    /**Give Access To Edit Process Details */
    editProcessSetupDetails(){
        this.editable = true;
        this.createNewProcess = false;
        setTimeout(() => {
            $('#name').focus();
        }, 200);
    }

    /**Add/Create New Process */
    addProcessSetup(form?) {
        this.processesDetails = new ProcessesDetails();
        if(form){
            form.reset();
        }
        this.openReconDialog();
        this.getSourceProfile();
        this.getRRuleGrp();
        this.fetchAllcalendarList();
        this.getARuleGrp();
        this.editable = true;
        this.createNewProcess = true;
        this.editprocessSetupDialog = true;
        setTimeout(() => {
            $('#name').focus();
        }, 200);
    }

    /**Convert date to prvent datepicker error */
    convertDate(){
        this.processesDetails.startDate = this.dateUtils.convertLocalDateFromServer(this.processesDetails.startDate);
        this.processesDetails.endDate = this.dateUtils.convertLocalDateFromServer(this.processesDetails.endDate);
    }

    /**Close Custom Dialog */
    closeReconDialog(){
        $('.custom-dialog').removeClass('custDialogOpen');
    }

    /**Open Custom Dialog */
    openReconDialog() {
        $('.custom-dialog').addClass('custDialogOpen');
    }

    /**Check Process Name Duplicate */
    processDuplicateCheck(name, id) {
        if (name) {
            this.processesService.processDuplicate(name, id).subscribe((res) => {
                if (res.result != 'No Duplicates Found') {
                    this.prsNameDup = true;
                } else {
                    this.prsNameDup = false;
                }
            });
        }
    }


    

/*     loadAll() {
        if (this.currentSearch) {
            this.processesService.search({
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.processesService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/processes'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/processes', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/processes', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    
    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Processes) {
        return item.id;
    }
    registerChangeInProcesses() {
        this.eventSubscriber = this.eventManager.subscribe('processesListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.processes = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }*/
}