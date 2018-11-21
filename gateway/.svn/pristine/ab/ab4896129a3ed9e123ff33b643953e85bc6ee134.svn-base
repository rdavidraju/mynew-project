import { Component, OnDestroy, Output, EventEmitter } from '@angular/core';
import { Subject } from 'rxjs/Rx';
import { Jobs, Programs } from './jobs.model';
import { JobDetailsService } from './job-details.service';
import { CommonService } from '../common.service';
import { JhiDateUtils } from 'ng-jhipster';

@Component({
    selector: 'jhi-jobs-detail',
    templateUrl: './jobs-detail.component.html'
})
export class JobsDetailComponent implements OnDestroy {

    @Output() changeDisplay: EventEmitter<boolean> = new EventEmitter<boolean>();
    private unsubscribe: Subject<void> = new Subject();
    jobs = new Jobs();
    programsList: Programs[];
    freqTypeMeanings: any = {};
    loadDocument = false;
    disLisVal = 2;
    toggleDisLisVal = false;
    runs: any;
    lastRun: any;
    nextRun: any;
    accTab = true;
    showKillIcon = false;
    disHeader = false;
    disParameters = false;
    disSchedule = false;
    disRuns = false;
    curJobId: any;

    constructor(
        private jobDetailsService: JobDetailsService,
        private commonService: CommonService,
        private dateUtils: JhiDateUtils
    ) {}

    jobIdUpdated(id:any){
        this.curJobId = id;
        this.getJobDetails(this.curJobId);
    }

    getJobDetails(jobId) {
        if (jobId) {
            this.jobDetailsService.getJobDetailsById(jobId).takeUntil(this.unsubscribe).subscribe((jobRes: Jobs) => {
                this.jobs = jobRes;
                this.jobs.scheStartDate=this.dateUtils.convertDateTimeFromServer(this.jobs.scheStartDate);
                this.jobs.scheEndDate=this.dateUtils.convertDateTimeFromServer(this.jobs.scheEndDate);
                // Change Initiate Time to Proper Time
                if (this.jobs.frequencies[0].time) {
                    const fTime = this.jobs.frequencies[0].time.split(':');
                    const t = new Date();
                    t.setHours(fTime[0]);
                    t.setMinutes(fTime[1]);
                    t.setSeconds(fTime[2]);
                    this.jobs.frequencies[0].time = t;
                }

                this.disHeader = true;
                if(this.jobs.parameters.length > 0) {
                    this.disParameters = true;
                }

                if(this.jobs.scheStartDate || this.jobs.scheEndDate) {
                    this.disSchedule = true;
                }

                this.loadDocument = true;

                this.jobDetailsService.programsListByTenantId().takeUntil(this.unsubscribe).subscribe((res) => {
                    this.programsList = res;
                });

                const stat = this.jobs.frequencies[0].status;
                if(stat && stat != 'KILLED' && stat != 'SUCCEEDED') {
                    this.showKillIcon = true;
                } else {
                    this.showKillIcon = false;
                }

                if (this.jobs.parameters) {
                    this.jobs.parameters.forEach((item) => {
                        item.value = item.selectedValueName.split(',');
                    });
                }

                this.jobDetailsService.getSchedulersList(0, 0, jobId).takeUntil(this.unsubscribe).subscribe((res) => {
                    this.runs = res.json();
                    if(this.runs.length) {
                        this.disRuns = true;
                        this.runs.forEach((item)=>{
                            item.startTime=this.dateUtils.convertDateTimeFromServer(item.startTime);
                            item.endTime=this.dateUtils.convertDateTimeFromServer(item.endTime);
                        })
                    }

                    if (this.jobs.frequencies[0].oozieJobId) {
                        this.jobDetailsService.getLastRunOfAJob(this.jobs.frequencies[0].oozieJobId).takeUntil(this.unsubscribe).subscribe((lRes) => {
                            this.lastRun = lRes;
                            if(this.lastRun.lastModifiedTime){
                                this.lastRun.lastModifiedTime=this.dateUtils.convertDateTimeFromServer(this.lastRun.lastModifiedTime);
                            }
                            this.disSchedule = true;
                        });

                        this.jobDetailsService.getNxtRunOfAJob(this.jobs.frequencies[0].oozieJobId).takeUntil(this.unsubscribe).subscribe((nRes) => {
                            this.nextRun = nRes;
                            if(this.nextRun.nextMatdTime){
                                this.nextRun.nextMatdTime=this.dateUtils.convertDateTimeFromServer(this.nextRun.nextMatdTime);
                            }
                            this.disSchedule = true;
                        });
                    }
                });
            });
    }
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    /**
    * Author : Shobha
    * @param jobId
    */
    killJob(jobId) {
        this.jobDetailsService.killJob(jobId).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.commonService.info('Info', res ? 'Job killed' : 'Failed Killing Job');
            this.getJobDetails(this.curJobId);
        }, () => {
            this.commonService.info('Warning!', 'Error Occured');
        });
    }


    closeGoBack() {
        this.changeDisplay.emit(true);
        this.curJobId=undefined;
    }
    
    dislist(len) {
        this.toggleDisLisVal = !this.toggleDisLisVal;
        this.disLisVal = this.toggleDisLisVal ? len : 2;
    }

    accordionToggle() {
        this.accTab = !this.accTab;
    }
}