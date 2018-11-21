import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { Jobs, Programs, Frequencies, Parameters } from './jobs.model';
import { JobDetailsService } from './job-details.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-jobs-detail',
    templateUrl: './jobs-detail.component.html'
})
export class JobsDetailComponent implements OnInit, OnDestroy {
    private breadCrumb = this.$sessionStorage.retrieve('sessionbreadcrumbhis');
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    jobs = new Jobs();
    programsList: Programs[];
    routeSub: any;
    presentPath: any;
    freqTypeMeanings: any = {};
    // UserData: any;
    runs: any;
    lastRun: any;
    nextRun: any;
    accTab: boolean = true;
    showKillIcon: boolean = false;
    disHeader: boolean = false;
    disParameters: boolean = false;
    disSchedule: boolean = false;
    disRuns: boolean = false;
    curJobId: any;

    constructor(
        private eventManager: JhiEventManager,
        private jobDetailsService: JobDetailsService,
        private notificationsService: NotificationsService,
        private router: Router,
        private route: ActivatedRoute,
        private commonService: CommonService,
        private $sessionStorage: SessionStorageService
    ) {

    }

    ngOnInit() {
      //  this.jobDetailsService.getUserInfo();
        /* if (this.$sessionStorage.retrieve('userData')) {
            this.UserData = this.$sessionStorage.retrieve('userData');
        } */
        this.routeSub = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "jobs-new") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            this.curJobId = params['id'];

            this.getJobDetails(params['id']);
            
        });
    }

    getJobDetails(jobId) {
        if (jobId) {
            this.jobDetailsService.getJobDetailsById(jobId).subscribe((jobRes: Jobs) => {
                this.jobs = jobRes;
                this.disHeader = true;
                if(this.jobs.parameters.length > 0) {
                    this.disParameters = true;
                }

                if(this.jobs.scheStartDate || this.jobs.scheEndDate) {
                    this.disSchedule = true;
                }

                this.jobDetailsService.programsListByTenantId().subscribe((res) => {
                    this.programsList = res;
                });
                let stat = this.jobs.frequencies[0].status;
                if(stat && stat != 'KILLED' && stat != 'SUCCEEDED') {
                    this.showKillIcon = true;
                } else {
                    this.showKillIcon = false;
                }

                if (this.jobs.parameters) {
                    this.jobs.parameters.forEach(item => {
                        item.value = item.selectedValueName.split(',');
                    });
                }

                this.jobDetailsService.getSchedulersList(0, 0, jobId).subscribe(res => {
                    this.runs = res.json();
                    if(this.runs.length) {
                        this.disRuns = true;
                    }

                    if (this.jobs.frequencies[0].oozieJobId) {
                        this.jobDetailsService.getLastRunOfAJob(this.jobs.frequencies[0].oozieJobId).subscribe(res => {
                            this.lastRun = res;
                            this.disSchedule = true;
                        });

                        this.jobDetailsService.getNxtRunOfAJob(this.jobs.frequencies[0].oozieJobId).subscribe(res => {
                            this.nextRun = res;
                            this.disSchedule = true;
                        });
                    }
                    // this.lastRun = { "lastModifiedTime": "2018-03-20 05:14:27.0", "createdTime": "2018-03-20 05:09:44.0", "externalId": "0001290-180313083847588-oozie-hdsi-W", "id": "0001288-180313083847588-oozie-hdsi-C@1", "status": "SUCCEEDED" };
                    // this.nextRun = { "nextMatdTime": "2019-03-20 05:14:00.0", "createdTime": "2018-03-20 05:09:43.0", "id": "0001288-180313083847588-oozie-hdsi-C", "endTime": "2018-04-21 05:12:00.0", "frequency": "14 5 20 3 *" };
                });
            });
    }
    }

    ngOnDestroy() {

    }

    /**
    * Author : Shobha
    * @param jobId
    */
    killJob(jobId) {
        this.jobDetailsService.killJob(jobId).subscribe((res: any) => {
            this.notificationsService.info('', res ? 'Job killed' : 'Failed Killing Job');
            this.getJobDetails(this.curJobId);
        }, err => {
            this.notificationsService.info('Warning!', 'Error Occured');
        });
    }


    closeGoBack() {
        if (this.breadCrumb[0].parent == '/jobs') {
            let params = this.breadCrumb[0].url.split('/');
            if (params && params.length == 1) {
                this.router.navigate(['/jobs', { outlets: { 'content': 'jobs-list' } }]);
            } else {
                if (this.jobs.frequencies[0].type != 'ONDEMAND') {
                    this.router.navigate([this.breadCrumb[0].parent, { outlets: { 'content': ['SCHEDULED'] + '/' + [params[1]] + '/' + ['SCHEDULED'] + '/' + params[3] } }]);
                } else {
                    this.router.navigate([this.breadCrumb[0].parent, { outlets: { 'content': ['ONDEMAND'] + '/' + [params[1]] + '/' + ['ONDEMAND'] + '/' + params[3] } }]);
                }
            }
        }
    }

    disLisVal: number = 2;
    toggleDisLisVal: boolean = false;
    dislist(len) {
        this.toggleDisLisVal = !this.toggleDisLisVal;
        this.disLisVal = this.toggleDisLisVal ? len : 2;
    }

    accordionToggle() {
        this.accTab = !this.accTab;
    }
}