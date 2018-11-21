import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs/Rx';
import { MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { JobDetailsService } from './job-details.service';
import { Jobs, Programs, Frequencies, Parameters } from './jobs.model';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { CommonService } from '../common.service';
declare var jQuery: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jobs-new-dialog',
    templateUrl: 'jobs-new-dialog.component.html',
})
export class JobsNewDialog implements OnInit, OnDestroy {

    private unsubscribe: Subject<void> = new Subject();
    programsList: Programs[];
    jobs = new Jobs();
    jobsParameters: Parameters[];
    stDate: any;
    stTime: any;
    edDate: any;
    edTime: any;
    occurence: any[];
    weekly: any[];
    selWeek: any[] = [];
    monthly: any[];
    selMonth: any[] = [];
    selOcrnceType: any;
    occurenceTypes: any[];
    showOptional = false;
    duplicateJobName = false;
    TransformImmediately: boolean;
    initiateDate: any;
    initiatetime: any;
    timeArr: any[] = [];
    stCustTimePick = false;
    edCustTimePick = false;
    custTimePick = false;
    isSubmitting = false;
    isViewOnly = false;
    daysList: any[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31];
    monthDay: any = this.daysList[0];
    isSdChange = false;
    isEdChange = false;
    isIniDChange = false;
    stValid: any;
    edValid: any;
    intdValid: any;
    
    constructor(
        public dialogRef: MdDialogRef<JobsNewDialog>,
        @Inject(MD_DIALOG_DATA) public data: any,
        private jobDetailsService: JobDetailsService,
        private commonService: CommonService
    ) { }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit() {
        this.jobDetailsService.programsListByTenantId().takeUntil(this.unsubscribe).subscribe((res) => {
            this.programsList = res;
            this.getTimes();
            const today = new Date();
            this.stDate = this.initiateDate = today;
            const hour = today.getHours() >=12 ? today.getHours()-12 : today.getHours() == 0 ? 12 : today.getHours();
            const min = today.getMinutes() < 10 ? '0' + today.getMinutes() : today.getMinutes();
            this.stTime = this.initiatetime = (hour==0 ? 12 : hour<10 ? '0'+hour : hour) + ":" + min + (today.getHours() >=12 ? 'pm' : 'am');
        });

        this.jobDetailsService.getLookupValues('SCHEDULE_TYPES').takeUntil(this.unsubscribe).subscribe((res: LookUpCode[]) => {
            this.occurence = res;
        });
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    programSelected(programId: number, e) {
        this.jobs.runNow = true;
        if (programId) {
            const t = new Date();
            this.jobs.jobName = e.source.triggerValue+t.getDate()+(t.getMonth()+1)+t.getFullYear()+t.getHours()+t.getMinutes()+t.getSeconds();
            this.jobDetailsService.getProgramsList(programId).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.jobsParameters = res[0].parametersSet;
                this.jobsParameters.forEach((item, i) => {
                    item.selectedValue = undefined;
                    item.search = null;
                    if (item.dependency && item.dependency != null) {
                        item.valuesList = undefined;
                    }
                });
            });
        }
    }

    runNowF() {
        if (!this.jobs.runNow) {
            this.weekly = [{"code":"sun","disValue":"S","name":"Sunday","value":false},{"code":"mon","disValue":"M","name":"Monday","value":false},{"code":"tue","disValue":"T","name":"Tuesday","value":false},{"code":"wed","disValue":"W","name":"Wednesday","value":false},{"code":"thu","disValue":"T","name":"Thursday","value":false},{"code":"fri","disValue":"F","name":"Friday","value":false},{"code":"sat","disValue":"S","name":"Saturday","value":false}];
            this.monthly = [{"code":"jan","disValue":1,"name":"January","value":false},{"code":"feb","disValue":2,"name":"February","value":false},{"code":"mar","disValue":3,"name":"March","value":false},{"code":"apr","disValue":4,"name":"April","value":false},{"code":"may","disValue":5,"name":"May","value":false},{"code":"jun","disValue":6,"name":"June","value":false},{"code":"jul","disValue":7,"name":"July","value":false},{"code":"aug","disValue":8,"name":"August","value":false},{"code":"sep","disValue":9,"name":"September","value":false},{"code":"oct","disValue":10,"name":"October","value":false},{"code":"nov","disValue":11,"name":"November","value":false},{"code":"dec","disValue":12,"name":"December","value":false}];
            this.occurenceTypes = ['Initiate At', 'Schedule'];
            this.selOcrnceType = this.occurenceTypes[0];
            this.jobs.occurence = null;
        }
    }

    selWeekF(week) {
        week.value = week.value?false:true;
        const weekSel = [];
        const curWeek = new Date().getDay();
        this.weekly.forEach((item, i) => {
            if(item.value) {
                weekSel.push(item);
            }
        });
        if(weekSel.length < 1) {
            this.weekly[curWeek].value = true;
        }
    }

    selMonthF(month) {
        month.value = month.value?false:true;
        const monthSel = [];
        const curMonth = new Date().getMonth();
        this.monthly.forEach((item) => {
            if(item.value) {
                monthSel.push(item);
            }
        });
        if(monthSel.length < 1) {
            this.monthly[curMonth].value = true;
        }
    }

    occurenceChange(val) {
        if (val == 'radio') {
            if(this.selOcrnceType == 'Initiate At') {
                this.jobs.occurence = null;
                this.jobs.neverEnd = false;
            }else {
                this.jobs.occurence = this.occurence[0].lookUpCode;
                this.jobs.neverEnd = true;
            }
        } else {
            const curWeek = new Date().getDay();
            const curMonth = new Date().getMonth();
            if(this.jobs.occurence == 'WEEKLY') {
                this.weekly.forEach((item,i) => {
                    if (i == curWeek) {
                        item.value = true;
                    } else {
                        item.value = false;
                    }
                });
            } else if(this.jobs.occurence == 'MONTHLY') {
                this.monthly.forEach((item,i) => {
                    if (i == curMonth) {
                        item.value = true;
                    } else {
                        item.value = false;
                    }
                });
            }
        }
    }

    checkJobName(jobName: string) {
        if (!jobName || jobName.length < 1) return;
        this.jobDetailsService.checkJobNameAvailable(jobName).takeUntil(this.unsubscribe).subscribe((res: number) => {
            if (res > 0) {
                if (this.jobs.id == res) {
                    return;
                } else {
                    this.duplicateJobName = true;
                }
            }
        });
    }

    blockSpecialChar(e,jobName1) {
        const k = (e.which) ? e.which : e.keyCode;
        if ((k > 64 && k < 91)) {
            return true;
        } else if (k > 96 && k < 123) {
            return true;
        } else if (k == 8) {
            return true;
        } else if (k > 47 && k < 57) {
            if (jobName1.selectionStart != 0) {
                return true;
            } else{
                this.commonService.info('Info!', 'Numbers at first place not allowed');
                return false;
            }
        } else {
            this.commonService.info('Info!', 'Special chararcters not allowed');
            return false;
        }
    }
    
    submit() {
        this.jobs.frequencies = [];
        const tempFrequency = new Frequencies();
        this.jobs.scheStartDate = this.combineDateTime(this.stDate, this.stTime);
        /*If startdate not changed, update it to current date and time*/
        if (!this.isSdChange) {
            this.jobs.scheStartDate = new Date();
        }
        if (this.jobs.neverEnd) {
            const curDate = new Date();
            this.jobs.scheEndDate = new Date(curDate.setFullYear(curDate.getFullYear()+5));
            this.jobs.scheEndDate.setHours(23,59,59,999);
        } else {
            this.jobs.scheEndDate = this.combineDateTime(this.edDate, this.edTime);
        }
        
        if(this.jobs.scheStartDate < new Date() && !this.jobs.runNow && this.isSdChange) {
            this.stValid = 'Please select valid start date and time.';
            this.showOptional = true;
            return;
        } else if(this.jobs.scheEndDate < this.jobs.scheStartDate) {
            this.edValid = 'Plase select valid end date and time.';
            return;
        } else if (((this.combineDateTime(this.initiateDate,this.initiatetime) < new Date() && this.selOcrnceType == 'Initiate At')) || (this.combineDateTime(this.initiateDate,this.initiatetime) < this.jobs.scheStartDate && this.selOcrnceType == 'Initiate At')) {
            this.intdValid = 'Please select valid date and time.';
            return;
        }
        
        this.isSubmitting = true;
        this.dialogRef.close();
        this.jobs.jobStatus = true;
        this.jobs.parameters = [];
        this.jobsParameters.forEach((item) => {
            this.jobs.parameters.push(Object.assign({}, item));
        });
        this.jobs.parameters.forEach((item) => {
            if (item.dependency) {
                if (item.selectedValue && item.selectedValue.length > 0) {
                    let concateVal: any;
                    let i = 0;
                    item.selectedValue.forEach((value) => {
                        concateVal = i == 0 ? value : concateVal + ',' + value;
                        i = i + 1;
                    });
                    item.selectedValue = null;
                    item.selectedValue = concateVal;
                }
            }
        });
        if(this.jobs.runNow) {
            tempFrequency.type = 'ONDEMAND';
            this.jobs.scheEndDate = this.jobs.scheStartDate = new Date();
            this.jobs.scheEndDate.setDate(this.jobs.scheEndDate.getDate()+1);
        } else {
            const hlpDate=new Date(this.initiateDate.getTime());
            const curSec = new Date().getSeconds();
            if(this.selOcrnceType == 'Initiate At') {
                tempFrequency.type = 'ONETIME';
                tempFrequency.month = [];
                tempFrequency.month.push(this.initiateDate.getMonth()+1);
                tempFrequency.day = [];
                tempFrequency.day.push(this.initiateDate.getDate());
                tempFrequency.date = this.initiateDate;
                this.jobs.scheEndDate = new Date(this.initiateDate);
                this.jobs.scheEndDate.setDate(this.jobs.scheEndDate.getDate()+1);
                this.jobs.scheEndDate.setSeconds(curSec);
                this.jobs.scheEndDate.setMilliseconds(111);
            } else {
                tempFrequency.type = this.jobs.occurence;
                if(tempFrequency.type == 'WEEKLY') {
                    tempFrequency.weekDay = this.getSelected(tempFrequency.type);
                } else if(tempFrequency.type == 'MONTHLY') {
                    tempFrequency.day = [];
                    tempFrequency.day.push(this.monthDay);
                    tempFrequency.month = this.getSelected(tempFrequency.type);
                    this.monthly.forEach((item)=>{
                        if(tempFrequency.month[0]==item.code){
                            hlpDate.setMonth(item.disValue - 1);
                        }
                    })
                    hlpDate.setDate(this.monthDay);
                }
            }
            const meridiem = this.initiatetime.substr(this.initiatetime.length -2);
            tempFrequency.hours = meridiem == 'pm' ? parseInt(this.initiatetime.split(":")[0])+12 : parseInt(this.initiatetime.split(":")[0]);
            tempFrequency.hours = tempFrequency.hours == 24 ? 12 : tempFrequency.hours;
            tempFrequency.minutes = parseInt(this.initiatetime.split(":")[1].substring(0,2));
            const timeHr = meridiem == 'pm' && tempFrequency.hours == 12 ? 12 : meridiem == 'am' && tempFrequency.hours == 12 ? '00' : tempFrequency.hours;
            tempFrequency.time = (timeHr)+':'+(tempFrequency.minutes)+':'+(curSec);
            hlpDate.setHours(Number(timeHr));
            hlpDate.setMinutes(Number(tempFrequency.minutes));
            hlpDate.setSeconds(Number(curSec));
            hlpDate.setMilliseconds(1);
            tempFrequency.frequencyTime = hlpDate;
        }
        this.jobs.frequencies.push(tempFrequency);

        this.jobs.parameters.forEach((each, i) => {
            if(each.selectedValue && each.selectedValue.toString().toLowerCase().search('all') != -1) {
                each.selectedValue = null;
            }
        });
        this.jobDetailsService.postJobDetails(this.jobs).subscribe((res: any) => {
            let savedResponse: any;
            savedResponse = res.json();
            this.isSubmitting = false;
            if (savedResponse) {
                if (savedResponse[0] && savedResponse[0].taskName === 'Job save') {
                    if (savedResponse[1].taskName === 'Schedulers Save') {
                        if (savedResponse[2].taskName === 'Initiate Job' && savedResponse[2].taskStatus === 'Success') {
                            this.commonService.success('Success!', 'Process initiated successfully');
                            this.jobDetailsService.jobSubmitChangeDetect(tempFrequency.type == 'ONDEMAND' ? tempFrequency.type : 'SCHEDULED');
                        } else if (savedResponse[2].taskName === 'Initiate Job' && savedResponse[2].taskStatus === 'Failure') {
                            this.commonService.info('Info!', 'Process saved but initiation failed! Try again.');
                        }
                    } else {
                        this.commonService.error('Oops...!', 'Scheduler not saved! Try again.');
                    }
                } else {
                    this.commonService.error('Oops...!', 'Process not saved! Try again.');
                }
            } else {
                this.commonService.error('Oops...!', 'Process not saved! Try again.');
            }
        },
        () => {
            this.commonService.error('Warning!','Error Occured');
            this.isSubmitting = false;
        });
    }

    getSelected(param) {
        const array = param == 'WEEKLY' ? this.weekly : this.monthly;
        const tempArray = [];
        array.forEach((each) => {
            if(each.value) {
                tempArray.push(each.code);
            }
        });
        return tempArray;
    }

    combineDateTime(date,time) {
        let hour;
        const curSec = new Date().getSeconds();
        if(date && time) {
            if(time.substr(-2) == 'pm') {
                hour = parseInt(time.split(":")[0]) + 12;
            } else {
                hour = parseInt(time.split(":")[0]);
            }
            hour = hour == 12 ? '00' : hour == 24 ? 12 : hour;
            const finDate = new Date(date).setHours(hour,parseInt(time.split(":")[1].substring(0,2)),curSec);
            return new Date(new Date(finDate).setMilliseconds(111));
        }
    }

    neverEnd() {
        if(this.jobs.neverEnd) {
            this.edDate = undefined;
            this.edTime = undefined;
        } else {
            const today = new Date();
            this.edDate = new Date(today.setDate(today.getDate()+1));
            const hour = today.getHours() >=12 ? today.getHours()-12 : today.getHours() == 0 ? 12 : today.getHours();
            const min = today.getMinutes() < 10 ? '0' + today.getMinutes() : today.getMinutes();
            this.edTime = (hour==0 ? 12 : hour<10 ? '0'+hour : hour) + ":" + min + (today.getHours() >=12 ? 'pm' : 'am');
        }
    }

    getvaluesList(i) {
        /* if next param exists, based on the dependency of that param, call api with dependency id, parameter id,  */
        /* progParamSetId, dependencyId */
        if (this.jobsParameters[i + 1] && this.jobsParameters[i + 1].dependency != null) {
            if (this.jobsParameters[i + 1].paramId != null) {
                this.jobsParameters[i + 1].valuesList = [];
                this.jobsParameters[i + 1].selectedValue = null;
                //add value parameter
                // console.log(' jQuery.type(obj);' + jQuery.type(this.jobsParameters[i].selectedValue));
                if (jQuery.type(this.jobsParameters[i].selectedValue) == 'string') {
                    this.jobsParameters[i + 1].valuesList = [];

                    this.jobDetailsService.getValueListBasedOnDependency(this.jobsParameters[i + 1].paramId, this.jobsParameters[i].selectedValue).takeUntil(this.unsubscribe).subscribe((res: any) => {
                        this.jobsParameters[i + 1].valuesList = res;
                        if(this.jobsParameters[i + 1].dependency != null) {
                            this.jobsParameters[i + 1].selectedValue = [];
                            /* this.jobsParameters[i + 1].selectedValue.push(this.jobsParameters[i + 1].valuesList[0][this.jobsParameters[i + 1].bindValue]); */
                            this.jobsParameters[i + 1].valuesList.forEach((item) => {
                                this.jobsParameters[i + 1].selectedValue.push(item[this.jobsParameters[i + 1].bindValue]);
                            });
                        }
                    });
                } else if (jQuery.type(this.jobsParameters[i].selectedValue) == 'array') {
                    if (this.jobsParameters[i].selectedValue && this.jobsParameters[i].selectedValue.length > 0) {
                        let vals = [];
                        /* this.jobsParameters[i].selectedValue.forEach(selVal => {
                            vals.push(selVal[this.jobsParameters[i].bindValue]);
                        }); */
                        if (this.jobsParameters[i].selectedValue[0].toString().toLowerCase().search('all') != -1) {
                            vals = [];
                        } else {
                            vals = this.jobsParameters[i].selectedValue;
                        }
                        this.jobsParameters[i + 1].valuesList = [];
                        this.jobDetailsService.getValueListBasedOnDependency(this.jobsParameters[i + 1].paramId, vals).takeUntil(this.unsubscribe).subscribe((res: any) => {
                            this.jobsParameters[i + 1].valuesList = res;
                            if(this.jobsParameters[i + 1].dependency != null) {
                                this.jobsParameters[i + 1].selectedValue = [];
                                /* this.jobsParameters[i + 1].selectedValue.push(this.jobsParameters[i + 1].valuesList[0][this.jobsParameters[i + 1].bindValue]); */
                                this.jobsParameters[i + 1].valuesList.forEach((item) => {
                                    this.jobsParameters[i + 1].selectedValue.push(item[this.jobsParameters[i + 1].bindValue]);
                                });
                            }
                        });
                    }
                }
                if (this.jobsParameters[i + 1].valuesList) {
                    if (this.jobsParameters[i + 1].valuesList.length == 0) {
                        this.jobsParameters[i + 1].valuesList = [];
                    }
                }
            }
        } else if(this.jobsParameters[i + 1]) {
            this.jobsParameters[i + 1].selectedValue = null;
        }
    }

    getTimes() {
        let date = new Date('01-01-2017 00:00:00 am');
        for (let i = 0; i <= 47; i++) {
            if (date.getHours() <= 12) {
                if (date.getMinutes() <= 12) {
                    this.timeArr.push((date.getHours() == 0 ? 12 : date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':0' + date.getMinutes() + 'am');
                } else {
                    this.timeArr.push((date.getHours() == 0 ? 12 : date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':' + date.getMinutes() + 'am');
                }
            } else {
                if (date.getMinutes() <= 12) {
                    this.timeArr.push(((date.getHours() - 12) < 10 ? ('0' + (date.getHours() - 12)) : (date.getHours() - 12)) + ':0' + date.getMinutes() + 'pm');
                } else {
                    this.timeArr.push(((date.getHours() - 12) < 10 ? ('0' + (date.getHours() - 12)) : (date.getHours() - 12)) + ':' + date.getMinutes() + 'pm');
                }
            }
            date = new Date(date.getTime() + 30 * 60000);
        }
    }

    selectTime(time, which) {
        if (which == 'stTime') {
            this.stTime = time;
        } else if (which == 'edTime') {
            this.edTime = time;
        } else if(which == 'initiatetime') {
            this.initiatetime = time;
        }
    }

    hideTimePicker() {
        setTimeout(() => {
            if (this.stCustTimePick) {
                this.stCustTimePick = false;
            } 
            if (this.edCustTimePick) {
                this.edCustTimePick = false;
            } 
            if (this.custTimePick) {
                this.custTimePick = false;
            }
        }, 100);
    }

    showTimePicker(picker) {
        if(picker == 'stCustTimePick') {
            this.stCustTimePick = true;
            this.edCustTimePick = false;
            this.custTimePick = false;
        } else if (picker == 'edCustTimePick') {
            this.edCustTimePick = true;
            this.stCustTimePick = false;
            this.custTimePick = false;
        } else if (picker == 'custTimePick') {
            this.custTimePick = true;
            this.stCustTimePick = false;
            this.edCustTimePick = false;
        }
    }

    toggleCheck(i,j,select, search, obj) {
        if(search && !obj[this.jobsParameters[i].bindValue].toLowerCase().includes('all')) {
            return;
        }
        if(j <= 0) {
            // this.jobsParameters[i].valuesList[0][this.jobsParameters[i].bindValue] == this.jobsParameters[i].selectedValue[0]
            // && select.options.length != select.selected.length
            if (select.options._results[0].selected) {
                //Check All
                this.jobsParameters[i].selectedValue = [];
                this.jobsParameters[i].valuesList.forEach((e) => {
                    this.jobsParameters[i].selectedValue.push(e[this.jobsParameters[i].bindValue]);
                });
                this.getvaluesList(i);
            } else {
                //UnCheck All
                this.jobsParameters[i].selectedValue = [];
            }
        } else {
            let deselect = false;
            select.options.forEach((each, ind) => {
                if (!each.selected && ind > 0) {
                    deselect = true;
                }
            });
            if (deselect) {
                select.options._results[0].deselect();
                if (this.jobsParameters[i].selectedValue.length) {
                    if (this.jobsParameters[i].selectedValue[0].toString().toLowerCase().includes('all')) {
                        this.jobsParameters[i].selectedValue.splice(0, 1);
                    }
                }
            } else {
                select.options._results[0].select();
                if (this.jobsParameters[i].selectedValue.length) {
                    if (!this.jobsParameters[i].selectedValue[0].toString().toLowerCase().includes('all')) {
                        this.jobsParameters[i].selectedValue.unshift(
                            this.jobsParameters[i].valuesList[0][this.jobsParameters[i].bindValue]
                        );
                    }
                }
            }
            this.getvaluesList(i);
        }
    }

}