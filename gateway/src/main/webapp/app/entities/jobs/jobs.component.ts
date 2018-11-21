import { Component, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs/Rx';
import { JobsBasicDetails } from './jobs.model';
import { JobDetailsService } from './job-details.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { PageEvent } from '@angular/material';
import { MdDialog } from '@angular/material';
import { JobsNewDialog } from './jobs-new-dialog.component';
import { CommonService } from '../common.service';

@Component({
  selector: "jhi-jobs",
  templateUrl: "./jobs.component.html"
})
export class JobsComponent implements OnDestroy {
    private unsubscribe: Subject<void> = new Subject();
    jobsList: JobsBasicDetails[] = [];
    selectedJobs: JobsBasicDetails[] = [];
    listLength: number;
    pageSize = 0;
    pageSizeOptions = [5, 10, 25, 100];
    pageEvent: PageEvent = new PageEvent();

  constructor(
    private jobsDetailsService: JobDetailsService,
    public dialog: MdDialog,
    private commonService: CommonService
  ) {
    this.pageSize = ITEMS_PER_PAGE;
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = this.pageSize;
    this.loadAll();
  }

  loadAll() {
    this.jobsDetailsService
      .getJobsList(this.pageEvent.pageIndex + 1, this.pageEvent.pageSize)
      .takeUntil(this.unsubscribe)
      .subscribe((res) => {
        this.jobsList = res.json();
        this.listLength = +res.headers.get("x-count");
      },
      () => this.commonService.error('Warning!', 'Error occured while fetching jobs list'));
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  /**
   * Author: Sameer
   * Open New Jobs Dialog
   */
  openDialog(): void {
    this.dialog.open(JobsNewDialog, {
      width: "600px",
      disableClose: true,
      panelClass: "jobs-material"
    });
  }
}