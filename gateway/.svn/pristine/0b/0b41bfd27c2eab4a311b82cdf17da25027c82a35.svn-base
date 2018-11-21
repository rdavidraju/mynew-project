import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Rx';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { Processes, ProcessesDetails } from './processes.model';
import { ProcessesService } from './processes.service';
import { SourceProfilesService } from '../source-profiles/source-profiles.service';
import { RuleGroupService } from '../rule-group/rule-group.service';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-processes',
    templateUrl: './processes.component.html'
})
export class ProcessesComponent implements OnInit, OnDestroy {

    processes: Processes[];
    processesDetails = new ProcessesDetails();
    subscription: Subscription;
    itemsPerPage: any = 10;
    processSetupRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    editprocessSetupDialog = false;
    sourceProfile:any = [];
    calendarList:any = [];
    reconRuleGroup:any = [];
    accRuleGroup:any = [];
    editable = false;
    currentSrcProfile: any = [];
    createNewProcess = false;
    prsNameDup: boolean;
    checkNewPrcs = false;

    constructor(
        private processesService: ProcessesService,
        private sourceProfilesService: SourceProfilesService,
        private ruleGroupService: RuleGroupService,
        private dateUtils: JhiDateUtils,
        private notificationsService: NotificationsService
    ) {}

    ngOnInit() {
        this.getProcessSetup();

        // Esc to close dialog
        $(document).keyup((e) => {
            if (e.keyCode == 27) {
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

    ngOnDestroy() {

    }

    // Get All Process Setup
    getProcessSetup(){
        this.processesService.getProcess(this.page, this.itemsPerPage).subscribe((res)=>{
            this.processes = res.json();
            if(this.processes.length<1){
                this.addProcessSetup();
            }
            this.processSetupRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    // Pagination function
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.processesService.getProcess(this.page+1, this.itemsPerPage).subscribe((res: any) => {
            this.processes = res.json();
            this.processSetupRecordsLength =+ res.headers.get('x-total-count');
        });
    }

    // Open Dialog and show detail
    processSetupDetail(detail, isEdit?){
        this.processesService.getProcessDetail(detail.id).subscribe((res)=>{
            this.processesDetails = res[0];
            this.getSourceProfile();
            this.getRRuleGrp();
            this.getARuleGrp();
            this.fetchAllcalendarList();
            this.convertDate();
            this.editable = false;
            if(this.processesDetails.processDetailList){
                this.processesDetails.spList = this.processesDetails.processDetailList.spList;
                this.processesDetails.spListIds = [];
                this.processesDetails.processDetailList.spList.forEach((srcPrf) => {
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
            if (isEdit) {
                this.editable = true;
                this.createNewProcess = false;
                setTimeout(() => {
                    $('#name').focus();
                }, 200);
            }
        });
        this.editprocessSetupDialog = true;
        this.openReconDialog();
    }

    // Save or Update Process Setup
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
            this.processesDetails.spListIds.forEach((srcId) => {
                srcObj = {
                    "tagType": "sourceProfile",
                    "typeId": srcId
                };
                this.processesDetails.processDetailList.spList.push(srcObj);
            });
            if (this.processesDetails.reconcRuleGroupId) {
                const recObj: any = {
                    "tagType": "reconciliationRuleGroup",
                    "typeId": this.processesDetails.reconcRuleGroupId
                };
                this.processesDetails.processDetailList.recGrpList.push(recObj);
            }
            if (this.processesDetails.actRuleGroupId) {
                const accObj: any = {
                    "tagType": "accountingRuleGroup",
                    "typeId": this.processesDetails.actRuleGroupId
                };
                this.processesDetails.processDetailList.actGrpList.push(accObj);
            }
            if (this.processesDetails.calendar) {
                const calObj: any = {
                    "tagType": "Calendar",
                    "typeId": this.processesDetails.calendar
                };
                this.processesDetails.processDetailList.calendarList.push(calObj);
            }
            if (this.processesDetails.violation) {
                const calObj: any = {
                    "tagType": "violation",
                    "typeId": this.processesDetails.violation
                };
                this.processesDetails.processDetailList.violationList.push(calObj);
            }
            if (this.processesDetails.id) {
                this.checkNewPrcs = true;
            } else {
                this.checkNewPrcs = false;
            }
            this.processesService.postProcess(this.processesDetails).subscribe((res) => {
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

    // Get Profile
    getSourceProfile() {
        this.sourceProfilesService.getAllProfilesAndConnections(true).subscribe((sourceProfile) => {
            this.sourceProfile = sourceProfile.json();
        });
    }

    // Get Reconciliation Rule Groups
    getRRuleGrp(){
        this.ruleGroupService.fetchRuleGroupsBasedOnRulePurpose('RECONCILIATION',true).subscribe((rRuleGroup)=>{
            this.reconRuleGroup = rRuleGroup.json();
        });
    }

    // Get getAllcalendarList
    fetchAllcalendarList(){
        this.processesService.getAllcalendarList().subscribe((res)=>{
            this.calendarList = res;
        });
    }

    // Get Accounting Rule Groups
    getARuleGrp(){
        this.ruleGroupService.fetchRuleGroupsBasedOnRulePurpose('ACCOUNTING',true).subscribe((aRuleGroup)=>{
            this.accRuleGroup = aRuleGroup.json();
        });
    }

    // Give Access To Edit Process Details
    editProcessSetupDetails(detail){
        if(detail) {
            this.processSetupDetail(detail, 'edit');
        } else {
            this.editable = true;
            this.createNewProcess = false;
            setTimeout(() => {
                $('#name').focus();
            }, 200);
        }
    }

    // Add/Create New Process
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
        this.processesDetails.startDate = new Date();
    }

    // Convert date to prvent datepicker error
    convertDate(){
        this.processesDetails.startDate = this.dateUtils.convertDateTimeFromServer(this.processesDetails.startDate);
        this.processesDetails.endDate = this.dateUtils.convertDateTimeFromServer(this.processesDetails.endDate);
    }

    // Close Custom Dialog
    closeReconDialog(){
        $('.custom-dialog').removeClass('custDialogOpen');
    }

    // Open Custom Dialog
    openReconDialog() {
        $('.custom-dialog').addClass('custDialogOpen');
    }

    // Check Process Name Duplicate
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

    onRowSelect(evt) {
        // this.router.navigate(['/data-views', {outlets: {'content': evt.data.id +'/details'}}]);
        this.processSetupDetail(evt.data);
    }
}