import { Component, OnDestroy, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { PageEvent } from '@angular/material';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../common.service';
import { JobsSchedules, SchedulerActions, SchedularDetailFilter } from './jobs.model';
import { JobDetailsService } from './job-details.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { MdDialog } from '@angular/material';
import { JobsNewDialog } from './jobs-new-dialog.component';
import { JobsDetailComponent } from './jobs-detail.component';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var jQuery: any;

@Component({
    selector: 'jhi-jobs-sched-list',
    templateUrl: './jobs-schedulars-list.component.html'
})
export class JobsSchedularDetailsComponent implements OnDestroy {

    @ViewChild(JobsDetailComponent) jobsDetailComponent: JobsDetailComponent;
    schedulsTableColumns = [
        { field: 'programName', header: 'Program' },
        { field: 'oozieStatus', header: 'Status' },
        { field: 'frequencyType', header: 'Frequency Type' }
    ];
    private unsubscribe: Subject<void> = new Subject();
    parentDiv: any;
    showSchedulersList = true;
    jobId = 0;
    blockUI = false;
    routeSub: any;
    presentPath: any;
    schedularsList: JobsSchedules[] = [];
    selSingleSchedule = new JobsSchedules();
    selectedScheduls: JobsSchedules[] = [];
    listLength: number;
    pageSize: number;
    pageSizeOptions = [5, 10, 25, 100];
    pageEvent: PageEvent = new PageEvent();
    schedularDetailFilter = new SchedularDetailFilter();
    programsList: any[];
    allStatuses = true;
    schedulerActions: SchedulerActions[] = [];
    showActionsLog = false;
    slctdJobId:any;

    constructor(
        private jobDetailsService: JobDetailsService,
        private router: Router,
        public dialog: MdDialog,
        private commonService: CommonService,
        private dateUtils: JhiDateUtils
    ) {
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.pageSize;
        this.blockUI = true;
        this.jobDetailsService.programsListByTenantId().takeUntil(this.unsubscribe).subscribe((res) => {
            this.programsList = res;
            this.programsList.unshift({ "id": 0, "prgmName": "All" });
        });
        this.loadShcedulersListByFilters();
    }

    allStatusesChanged() {
        if (this.allStatuses) {
            for (let i = 0; i < this.schedularDetailFilter.scheduledStatusList.length; i++) {
                this.schedularDetailFilter.scheduledStatusList[i].isSelected = true;
            }
            this.loadShcedulersListByFilters();
        } else {
            for (let i = 0; i < this.schedularDetailFilter.scheduledStatusList.length; i++) {
                this.schedularDetailFilter.scheduledStatusList[i].isSelected = false;
            }
            this.schedularsList = [];
            this.listLength = 0;
        }
    }

    /**
     * Author: Rk
     * Load all schedulers list based on filters
     */
    loadShcedulersListByFilters() {
        for (let i = 0; i < this.schedularDetailFilter.scheduledStatusList.length; i++) {
            if (!this.schedularDetailFilter.scheduledStatusList[i].isSelected) {
                this.allStatuses = false;
            }
        }
        this.blockUI = true;
        if(!this.showSchedulersList && this.slctdJobId){
            this.showJobDetails(this.slctdJobId);
        }
        if (this.schedularDetailFilter.stDateFrom && this.schedularDetailFilter.stDateTo) {
            if (this.schedularDetailFilter.stDateFrom <= this.schedularDetailFilter.stDateTo) {
                this.jobDetailsService.getschedularsListByFilter(this.pageEvent.pageIndex, this.pageEvent.pageSize, jQuery.extend(true, new SchedularDetailFilter, this.schedularDetailFilter))
                .takeUntil(this.unsubscribe)
                .subscribe((res) => {
                    this.schedularsList = res.json();
                    this.listLength = +res.headers.get('x-count');
                    this.schedularsList.forEach((item)=>{
                        item.endTime=this.dateUtils.convertDateTimeFromServer(item.endTime);
                        item.startTime=this.dateUtils.convertDateTimeFromServer(item.startTime);
                    });
                    this.blockUI = false;
                }, () => {
                    this.blockUI = false;
                    this.listLength = 0;
                    this.schedularsList = [];
                });
            } else {
                this.commonService.info('Oops...!', 'Request Start Date should be prior date of Request End Date');
                this.blockUI = false;
                this.listLength = 0;
                this.schedularsList = [];
            }
        } else {
            this.jobDetailsService.getschedularsListByFilter(this.pageEvent.pageIndex, this.pageEvent.pageSize, jQuery.extend(true, new SchedularDetailFilter, this.schedularDetailFilter))
            .takeUntil(this.unsubscribe)
            .subscribe((res) => {
                this.schedularsList = res.json();
                this.listLength = +res.headers.get('x-count');
                this.schedularsList.forEach((item)=>{
                    item.endTime=this.dateUtils.convertDateTimeFromServer(item.endTime);
                    item.startTime=this.dateUtils.convertDateTimeFromServer(item.startTime);
                });
                this.blockUI = false;
            }, () => {
                this.blockUI = false;
                this.listLength = 0;
                this.schedularsList = [];
            });
        }
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    rerunSchedule(oozieJobId: any) {
        this.jobDetailsService.rerunSchedule(oozieJobId).takeUntil(this.unsubscribe).subscribe((resp: any) => {
            this.loadShcedulersListByFilters();
        });
    }

    stopSchedule(id: any, rangeType: any, scope: any) {
        this.jobDetailsService.killSchedule(id, 'action', 1).takeUntil(this.unsubscribe).subscribe((resp: any) => {
            this.loadShcedulersListByFilters();
        });
    }

    showScheduleLog(id: any) {
        this.jobDetailsService.showScheduleLog(id).takeUntil(this.unsubscribe).subscribe((resp: any) => {
        });
    }

    getScheduleActions(schedule: JobsSchedules, id: any) {
        this.selSingleSchedule = schedule;
        this.jobDetailsService.getScheduleActions(id).takeUntil(this.unsubscribe).subscribe((resp: SchedulerActions[]) => {
            this.schedulerActions = resp;
            this.showActionsLog = true;
        });
    }

    resetmodal() {
        this.showActionsLog = false;
    }
    /**
   * Author : Shobha
   * @param jobId
   */
    suspendJob(jobId) {
        this.jobDetailsService.suspendJob(jobId).takeUntil(this.unsubscribe).subscribe((res: any) => {
            const resp: any = res;
            if (resp) {
                this.commonService.info('Info!','Job Suspended');
                this.loadShcedulersListByFilters();
            } else {
                this.commonService.error('Warning!','Error occured while Suspending Job');
            }
        });
    }

    /**
    * Author : Shobha
    * @param jobId
    */
    resumeJob(jobId) {
        this.jobDetailsService.resumeJob(jobId).takeUntil(this.unsubscribe).subscribe((res: any) => {
            const resp: any = res;
            if (resp) {
                this.commonService.info('Info!','Job resumed');
                this.loadShcedulersListByFilters();
            } else {
                this.commonService.error('Warning!','Error occured while resuming Job');
            }
        });
    }

    /**
     * Author : Shobha
     * @param jobId
     */
    killJob(jobId) {
        this.jobDetailsService.killJob(jobId).takeUntil(this.unsubscribe).subscribe(() => {
            this.loadShcedulersListByFilters();
        });
    }
    
    showJobDetails(jobId: any) {
        this.slctdJobId=jobId;
        this.showSchedulersList = false;
        this.jobsDetailComponent.jobIdUpdated(jobId);
    }

    hideDialog(event) {
        this.showSchedulersList = event;
    }

    openDialog(): void {
        this.jobDetailsService.oozieDBTest().takeUntil(this.unsubscribe).subscribe((oozieStatus: any) => {
            if (oozieStatus && oozieStatus.dbStatus && oozieStatus.ooziestatus) {
                const dialogRef = this.dialog.open(JobsNewDialog, {
                    width: '600px',
                    disableClose: true,
                    panelClass: 'jobs-material'
                });
                dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe(() => {
                    this.loadShcedulersListByFilters();
                });
            } else {
                this.commonService.error('Warning!', 'Server is unstable!');
            }
        });
    }
}