import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { Calendar } from './calendar.model';
import { CalendarService } from './calendar.service';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-calendar-detail',
    templateUrl: './calendar-detail.component.html'
})
export class CalendarDetailComponent implements OnInit, OnDestroy {

    calendar = new Calendar();
    private unsubscribe: Subject<void> = new Subject();
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA = false;
    calendarType: any;
    displayPeriod: any = false;
    calendarList: any;
    periodStatusList: LookUpCode[] = [];
    nameExist: string;
    statuses = [{ code: true, value: 'Active' }, { code: false, value: 'Inactive' }];
    loadDocument = false;
    currentDate = new Date();

    constructor(
        private calendarService: CalendarService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        private dateUtils: JhiDateUtils
    ) { }

    ngOnInit() {
        this.calendarService.lookupCodes('PERIOD_STATUS').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
            this.periodStatusList = res;
        }, () => {
            this.commonService.error('Error!', 'Failed to load period status list');
        });
        // Calling Function - Get calendar and period details by id
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    // Get calendar and period details by id
    loadAll() {
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;

            // Get Lookup codes for Calendar type
            this.calendarService.lookupCodes('CALENDAR_TYPE').takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.calendarType = res;
            })

            if (params['id']) {
                this.calendarService.getCalenderAndItsPeriods(params['id']).takeUntil(this.unsubscribe).subscribe((calendar) => {
                    this.calendar = calendar;
                    this.calendar.startDate = this.dateUtils.convertDateTimeFromServer(this.calendar.startDate);
                    this.calendar.endDate = this.dateUtils.convertDateTimeFromServer(this.calendar.endDate);
                    for (let i = 0; i < this.calendar.periodList.length; i++) {
                        this.calendar.periodList[i].fromDate = this.dateUtils.convertDateTimeFromServer(this.calendar.periodList[i].fromDate);
                        this.calendar.periodList[i].toDate = this.dateUtils.convertDateTimeFromServer(this.calendar.periodList[i].toDate);
                    }

                    this.isCreateNew = false;
                    if (url.endsWith('edit')) {
                        this.isEdit = true;
                        // Focusing on first element
                        $('#name').focus();
                    } else {
                        this.isViewOnly = true;
                        this.calendar.description = !this.calendar.description ? '-' : this.calendar.description;
                        this.calendar.endDate = !this.calendar.endDate ? '-' : this.calendar.endDate;
                    }
                    this.loadDocument = true;
                }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                this.loadDocument = true;
                // Focusing on first element
                $('#name').focus();
                this.calendar.startDate = new Date();
            }
        });
    }

    // Toggle sidebar
    toggleSB() {
        if (!this.isVisibleA) {
            this.isVisibleA = true;
            $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }

    // Save calendar with optional periods
    saveCalendar() {
        if (!this.nameExist) {
            this.calendarService.checkCalendarNameIsExist(this.calendar.name, this.calendar.id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.nameExist) {
                    let periodEmpty: any = false;
                    let link: any = '';
                    if (this.calendar.id) {
                        // Update Calendar creation
                        this.calendarService.updateCalendar(this.calendar).takeUntil(this.unsubscribe).subscribe((updRes: any) => {
                            this.commonService.success('Success!', 'Calendar ' + updRes.name + ' Successfully Updated');
                            this.loadAll();
                            if (updRes.id) {
                                link = ['/calendar', { outlets: { 'content': updRes.id + '/details' } }];
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
                        // New Calendar creation
                        for (const period of this.calendar.periodList) {
                            if (period.periodName == null || period.fromDate == null ||
                                period.toDate == null || period.periodNum == null ||
                                period.quarter == null || period.year == null) {
                                periodEmpty = true;
                            } else if (period.periodNum.toString().length > 2) {
                                periodEmpty = 'PeriodNum>2';
                            } else if (period.quarter.toString().length > 1) {
                                periodEmpty = 'Quarter>1'
                            } else if (period.year.toString().length > 4 || period.year.toString().length < 4) {
                                periodEmpty = 'Year!=4';
                            }
                        }
                        if (periodEmpty == true) {
                            this.commonService.error('Warning!','Fill mandatory fields');
                        } else if (periodEmpty == 'Year!=4') {
                            this.commonService.error('Warning!','Year should be 4 digits');
                        } else if (periodEmpty == 'PeriodNum>2') {
                            this.commonService.error('Warning!','Period Number should be less than 3 digits');
                        } else if (periodEmpty == 'Quarter>1') {
                            this.commonService.error('Warning!','Quarter should be only 1 digit');
                        } else {
                            this.calendarService.postCalenderAndItsPeriods(this.calendar).takeUntil(this.unsubscribe).subscribe((postRes: any) => {
                                if (postRes.id) {
                                    link = ['/calendar', { outlets: { 'content': postRes.id + '/details' } }];
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
                }
            });
        }
    }

    // Cancel Changes made in period
    cancelEdit() {
        this.loadAll();
    }

    // Delete Period
    deletePeriod(period) {
        this.calendarService.deletePeriod(period.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.commonService.success('Success!', 'Period ' + period.periodName + ' Successfully Deleted');
            this.loadAll();
        });
    }

    // Delete Row
    deleteRow(index) {
        if (this.calendar.periodList.length < 2) {
            this.commonService.error('Warning!', 'Atleast one row is mandatory');
        } else {
            this.calendar.periodList.splice(index, 1);
        }
    }

    // Edit Period
    editPeriod(i) {
        let throwValidation: any = false;
        for (const period of this.calendar.periodList) {
            if (period.columnEdit == true && this.isViewOnly == true) {
                throwValidation = true;
            }
        }
        if (throwValidation == true) {
            this.commonService.error('Warning!', 'Please save row before editing');
        } else {
            this.calendar.periodList[i].columnEdit = true;
        }
    }

    // Update Period
    updatePeriod(period, i) {
        if (period.periodName == null || period.fromDate == null ||
            period.toDate == null || period.periodNum == null ||
            period.quarter == null || period.year == null) {
            this.validationMsg('period');
        } else if (period.periodNum.toString().length > 2) {
            this.validationMsg('PeriodNum>2');
        } else if (period.quarter.toString().length > 1) {
            this.validationMsg('Quarter>1');
        } else if (period.year.toString().length > 4 || period.year.toString().length < 4) {
            this.validationMsg('Year!=4');
        } else {
            if (period.id) {
                this.calendarService.updatePeriods(period).takeUntil(this.unsubscribe).subscribe((updatedPeriod: any) => {
                    this.commonService.success('Success!', 'Period ' + updatedPeriod.periodName + ' Successfully Updated');
                    period.id = updatedPeriod.id;
                    period.calId = updatedPeriod.calId;
                    period.periodName = updatedPeriod.periodName;
                    period.fromDate = new Date(updatedPeriod.fromDate);
                    period.toDate = new Date(updatedPeriod.toDate);
                    period.periodNum = updatedPeriod.periodNum;
                    period.quarter = updatedPeriod.quarter;
                    period.year = updatedPeriod.year;
                    period.adjustment = updatedPeriod.adjustment;
                    period.status = updatedPeriod.status;
                    period.createdBy = updatedPeriod.createdBy;
                    period.lastUpdatedBy = updatedPeriod.lastUpdatedBy;
                    period.lastUpdatedDate = updatedPeriod.lastUpdatedDate;
                    period.enabledFlag = updatedPeriod.enabledFlag;
                    period.columnEdit = false;
                });
            } else {
                period.calId = this.calendar.id;
                period.createdDate = new Date();
                period.lastUpdatedDate = new Date();
                period.enabledFlag = true;
                this.calendarService.updatePeriods(period).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.commonService.success('Success!', 'Period ' + res.periodName + ' Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    addNewPeriods() {
        let throwValidation: any = false;
        for (let i = 0; i < this.calendar.periodList.length; i++) {
            if (this.calendar.periodList[i].columnEdit == true && this.isViewOnly == true) {
                throwValidation = 'Edit';
            } else if (this.calendar.periodList[i].periodName == null || this.calendar.periodList[i].fromDate == null ||
                this.calendar.periodList[i].toDate == null || this.calendar.periodList[i].periodNum == null ||
                this.calendar.periodList[i].quarter == null || this.calendar.periodList[i].year == null) {
                throwValidation = 'Empty';
            } else if (this.calendar.periodList[i].periodNum.toString().length > 2) {
                throwValidation = 'PeriodNum>2';
            } else if (this.calendar.periodList[i].quarter.toString().length > 1) {
                throwValidation = 'Quarter>1'
            } else if (this.calendar.periodList[i].year.toString().length > 4 ||
                this.calendar.periodList[i].year.toString().length < 4) {
                throwValidation = 'Year!=4';
            }
        }
        if (throwValidation == 'Edit') {
            this.commonService.error('Warning!','Please save row before adding new');
        } else if (throwValidation == 'Empty') {
            this.commonService.error('Warning!','Fill mandatory fields');
        } else if (throwValidation == 'Year!=4') {
            this.commonService.error('Warning!','Year Should be 4 digits');
        } else if (throwValidation == 'PeriodNum>2') {
            this.commonService.error('Warning!','Period Number should be less than 3 digits');
        } else if (throwValidation == 'Quarter>1') {
            this.commonService.error('Warning!','Quarter should be only 1 digit');
        } else {
            const obj = {
                'periodName': null,
                'fromDate': null,
                'toDate': null,
                'periodNum': null,
                'quarter': null,
                'year': null,
                'adjustment': null,
                'columnEdit': true,
                'newPeriodStatus': true,
                status: 'NEVER_OPENED'
            };
            this.calendar.periodList.push(obj);
        }
    }

    // Validation Msg in form
    validationMsg(reason) {
        if (reason == 'calendar') {
            if (this.calendar.name == null || this.calendar.name == '') {
                this.commonService.error('Warning!','Name is mandatory');
            } else if (this.calendar.calendarType == null) {
                this.commonService.error('Warning!','Calendar Type is mandatory');
            } else if (this.calendar.startDate == null) {
                this.commonService.error('Warning!','Start date is mandatory');
            }
        } else if (reason == 'Year!=4') {
            this.commonService.error('Warning!','Year should be 4 digits');
        } else if (reason == 'PeriodNum>2') {
            this.commonService.error('Warning!','Period Number should be less than 3 digits');
        } else if (reason == 'Quarter>1') {
            this.commonService.error('Warning!','Quarter should be only 1 digit');
        } else {
            this.commonService.error('Warning!','Fill mandatory fields');
        }
    }

    // Remove period if selected standard else add period
    calType(type) {
        this.calendar.periodList = [];
        this.calendar.period = null;
        if (type != 'MONTHLY') {
            const obj = {
                'periodName': null,
                'fromDate': null,
                'toDate': null,
                'periodNum': null,
                'quarter': null,
                'year': null,
                'adjustment': null,
                'columnEdit': true,
                'status': 'NEVER_OPENED'
            };
            this.calendar.periodList.push(obj);
            this.displayPeriod = true;
        } else {
            this.displayPeriod = false;
        }
    }

    // Create periods based on calendart type (if only Monthly)
    generatePeriods(isExisting?, popOver?) {
        if (this.calendar.name && this.calendar.startDate) {
            if (!isExisting) {
                this.calendar.periodList = [];
            }
            if (this.calendar.calendarType == 'MONTHLY' && this.calendar.period != null) {
                const newPeriods = [];
                const shortMonths = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                const quarter = [1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4];
                let startFrom;
                if (!isExisting) {
                    startFrom = new Date(+this.calendar.period);
                    startFrom.setMonth(startFrom.getMonth() - 1);
                }
                const lastDate = isExisting ? this.calendar.periodList[this.calendar.periodList.length - 1].toDate : startFrom;
                let numOfPer;
                if (isExisting) {
                    numOfPer = this.calendar.period;
                } else {
                    numOfPer = ((new Date().getFullYear() - this.calendar.period.getFullYear()) * 12);
                    numOfPer = (11 - this.calendar.period.getMonth()) == 0 ? numOfPer : (numOfPer + (11 - this.calendar.period.getMonth()));
                    numOfPer = numOfPer + 1;
                }
                for (let k = 0; k < numOfPer; k++) {
                    lastDate.setDate(1);
                    lastDate.setMonth(lastDate.getMonth() + 1);
                    const fDate = new Date(+lastDate), tDate = new Date(+lastDate), perNum = lastDate.getMonth() + 1;
                    fDate.setDate(1);
                    tDate.setDate(new Date(tDate.getFullYear(), tDate.getMonth() + 1, 0).getDate());
                    const obj = {
                        periodName: shortMonths[lastDate.getMonth()] + '-' + lastDate.getFullYear(),
                        fromDate: fDate,
                        toDate: tDate,
                        periodNum: perNum,
                        quarter: quarter[lastDate.getMonth()],
                        year: lastDate.getFullYear(),
                        newPeriodAdj: true,
                        columnEdit: true,
                        status: 'NEVER_OPENED'
                    }
                    if (isExisting) {
                        newPeriods.push(obj);
                    } else {
                        this.calendar.periodList.push(obj);
                    };
                }
                if (isExisting) {
                    const newCal = JSON.parse(JSON.stringify(this.calendar));
                    newCal.description = newCal.description == '-' ? null : newCal.description;
                    newCal.endDate = newCal.endDate == '-' ? null : newCal.endDate;
                    newCal.periodList = newPeriods;
                    this.calendarService.postCalenderAndItsPeriods(newCal).takeUntil(this.unsubscribe).subscribe((postRes: any) => {
                        popOver.hide();
                        this.loadAll();
                    }, () => { this.commonService.error('Warning!', 'Something went wrong while generating & saving new "Periods"') });
                } else {
                    this.displayPeriod = true;
                }
            }
        } else {
            this.commonService.error('Warning!','Fill mandatory fields');
        }
    }


    // Check calendar name duplicates
    isNameExist(name, id) {
        if (name && !this.nameExist) {
            this.calendarService.checkCalendarNameIsExist(name, id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
            });
        }
    }

    exportCalPeriods(id, type) {
        this.calendarService.getCalenderAndItsPeriods(id, true, type).takeUntil(this.unsubscribe).subscribe(
            (res) => {
                this.commonService.exportData(res, type, this.calendar.name);
            },
            (err) => {
                this.commonService.error('Warning!', 'Error Occured');
            });
    }
}