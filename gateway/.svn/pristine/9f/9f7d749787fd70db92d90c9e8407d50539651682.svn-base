import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { Frequencies, Jobs,  } from '../jobs/jobs.model';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { SchedulingService } from './scheduling.service'; 
import { CommonService } from '../common.service';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $:any;
declare var jQuery: any;
 
@Component({
    selector: 'scheduling',
    templateUrl: './scheduling.component.html'
})
export class Scheduling implements OnInit {
    @Input('basicInfo') basicInfo = new SchedulingModel();
    @Output() changeDisplay: EventEmitter<boolean> = new EventEmitter<boolean>();
    currentFreq = new Frequencies(); 
    job=new Jobs();
    schedulerTypes: LookUpCode[] = [];
    runType='scheduleTo';
    neverEnd:boolean = false;
    selectedParameters: Parameters[]=[];
    today=new Date();
    test:boolean=false;
    startDateChange:boolean=false;
    endDateChange:boolean=false;
    timeStr='';
    displayTimesLov:boolean = false;
    hlpTimeFlag:boolean=false;
    timesLov:any[]=[];
    months = [{"code":"Jan","number":1,"name":"January","isSelected":false},
    {"code":"Feb","number":2,"name":"February","isSelected":false},
    {"code":"Mar","number":3,"name":"March","isSelected":false},
    {"code":"Apr","number":4,"name":"April","isSelected":false},
    {"code":"May","number":5,"name":"May","isSelected":false},
    {"code":"Jun","number":6,"name":"June","isSelected":false},
    {"code":"Jul","number":7,"name":"July","isSelected":false},
    {"code":"Aug","number":8,"name":"August","isSelected":false},
    {"code":"Sep","number":9,"name":"September","isSelected":false},
    {"code":"Oct","number":10,"name":"October","isSelected":false},
    {"code":"Nov","number":11,"name":"November","isSelected":false},
    {"code":"Dec","number":12,"name":"December","isSelected":false}];
    weeks = [{"code":"Sun","number":0,"name":"Sunday","isSelected":false},
    {"code":"Mon","number":1,"name":"Monday","isSelected":false},
    {"code":"Tue","number":2,"name":"Tuesday","isSelected":false},
    {"code":"Wed","number":3,"name":"Wednesday","isSelected":false},
    {"code":"Thu","number":4,"name":"Thursday","isSelected":false},
    {"code":"Fri","number":5,"name":"Friday","isSelected":false},
    {"code":"Sat","number":6,"name":"Saturday","isSelected":false}];
    constructor(
        private schedulingService: SchedulingService,
        private cs: CommonService
    ) {

    }

    ngOnInit() {
        this.neverEnd=false;
        this.runType='scheduleTo';
        this.selectedParameters=[];
        this.job=new Jobs();
        this.currentFreq = new Frequencies();
        this.currentFreq.type='ONETIME';
        this.currentFreq.time=new Date();
        this.job.scheStartDate= new Date();
        this.timesLov=this.buildTimeLOVs(30);
        this.schedulingService.getLookupValues('SCHEDULE_TYPES').subscribe((res: LookUpCode[]) => {
            this.schedulerTypes = res;
            this.typeChanged();
        });
    }

    repeatChanged(){
        if(this.runType=='schedule'){
            this.currentFreq.type='DAILY';
            this.typeChanged();
        }else if(this.runType=='scheduleTo'){
            this.currentFreq.type='ONETIME';
        }else{
            this.currentFreq.type='ONDEMAND';
        }
        this.typeChanged();
    }

    closeTimesLOV(){
        if(!this.hlpTimeFlag)
            this.displayTimesLov=false;
    }

    typeChanged(){
        this.job.scheStartDate=new Date();
        this.job.scheEndDate=new Date();
        if(this.currentFreq.type=='MONTHLY'){
            this.checkEmptySelection(this.months,true);
            this.job.scheEndDate.setFullYear(this.job.scheStartDate.getFullYear()+1);
        }else if(this.currentFreq.type=='ONETIME'){
            this.job.scheEndDate.setDate(this.job.scheStartDate.getDate());
        }
        else if(this.currentFreq.type=='ONDEMAND'){
            this.job.scheEndDate=new Date();
        }
        else{
            if(this.currentFreq.type=='WEEKLY'){
                this.checkEmptySelection(this.weeks,false);
            }
            this.job.scheEndDate.setMonth(this.job.scheStartDate.getMonth()+1);
        }
        let dateTemp=new Date();
        this.timeStr=dateTemp.toLocaleString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true });
    }

    buildTimeLOVs(mins) {
        let arr = [];
        var today = new Date('01-01-2017 00:00 AM');
        for (; today.getDate() == 1;) {
            arr.push((today.toLocaleString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true })));
            today = new Date(today.getTime() + mins * 60000);
        }
        return arr;
    }

    occurranceSelected(item,srcArray,isMonth:boolean){
        item.isSelected=!item.isSelected;
        if(item.isSelected==true){
            return;
        }
        this.checkEmptySelection(srcArray,isMonth);
    }
    checkEmptySelection(srcArray,isMonth:boolean){
        let tmp=0;
        for(var i=0,len=srcArray.length;i<len;i++){
            if(srcArray[i].isSelected==true){
                tmp++;
            }
        }
        if(tmp==0){
            if(isMonth){
                srcArray[this.today.getMonth()].isSelected=true;
            }else{
                srcArray[this.today.getDay()].isSelected=true;
            }
        }
    }
    
    checkValidation(){
        this.test=true;
        if(this.basicInfo.displayParameters)
        for(var i=0, len=this.basicInfo.displayParameters.length;i<len;i++){
            this.selectedParameters.push(this.basicInfo.displayParameters[i]);
        }
        let newItem2=new Parameters();
        newItem2.paramName="Schedule Type";
        newItem2.selectedValue=this.currentFreq.type;
        this.selectedParameters.unshift(newItem2);
        let newItem1=new Parameters();
        newItem1.paramName="Program Name";
        newItem1.selectedValue=this.basicInfo.programName;
        this.selectedParameters.unshift(newItem1);
        let newItem=new Parameters();
        newItem.paramName="Job Name";
        newItem.selectedValue=this.basicInfo.jobName+'-'+this.currentFreq.type+'('+this.job.scheStartDate.toString()+')';
        this.selectedParameters.unshift(newItem);
    }

    scheduleThis(){
        let finalFrequencies=[];
        this.job.jobName=this.basicInfo.jobName+'-'+this.currentFreq.type+'('+this.job.scheStartDate.toString()+')';
        this.job.jobDesc=this.job.jobName;
        this.job.jobStatus = true;
        this.job.programName=this.basicInfo.programName;
        if(this.neverEnd){
            let newDate = new Date(this.job.scheStartDate);
            this.job.scheEndDate=newDate.setFullYear(newDate.getFullYear()+25);
        }
        this.job.scheStartDate.setHours(0,0,0,0);
        this.job.scheEndDate.setHours(23,59,59,999);
        this.job.scheEndDate.setDate(this.job.scheStartDate.getDate());
        let hlpDate=new Date(this.job.scheStartDate.getTime());
        hlpDate.setHours(Number(this.timeStr.split(':')[0]));
        hlpDate.setMinutes(Number(this.timeStr.split(':')[1].split(' ')[0]));
        hlpDate.setSeconds(1);
        hlpDate.setMilliseconds(1);
        switch(this.currentFreq.type){
            case 'ONETIME':
            let tempMon=[];
            tempMon.push((this.job.scheStartDate.getMonth()+1).toString());
            let tempD=[];
            tempD.push((this.job.scheStartDate.getDate()).toString());
            finalFrequencies.push({
                id: this.currentFreq.id,
                type: this.currentFreq.type,
                month: tempMon,
                day: tempD,
                hours: this.timeStr.split(':')[0],
                minutes: this.timeStr.split(':')[1].split(' ')[0],
                date: this.job.scheStartDate,
                time: this.timeStr.split(' ')[0]+':00',
                frequencyTime:hlpDate
            });
            break;
            case 'DAILY':
            finalFrequencies.push({
                id: this.currentFreq.id,
                type: this.currentFreq.type,
                hours: this.timeStr.split(':')[0],
                minutes: this.timeStr.split(':')[1].split(' ')[0],
                time: this.timeStr.split(' ')[0]+':00',
                frequencyTime:hlpDate
            });
            
            break;
            case 'WEEKLY':
            let tempWeeks=[];
            for(var i=0;i<7;i++){
                if(this.weeks[i].isSelected){
                    tempWeeks.push(this.weeks[i].number);
                }
            }
            finalFrequencies.push({
                id: this.currentFreq.id,
                type: this.currentFreq.type,
                weekDay: tempWeeks,
                hours: this.timeStr.split(':')[0],
                minutes: this.timeStr.split(':')[1].split(' ')[0],
                time: this.timeStr.split(' ')[0]+':00'
            });
            break; 
            case 'MONTHLY':
            
            let tempMonths=[];
            for(var i=0;i<12;i++){
                if(this.months[i].isSelected){
                    tempMonths.push(this.months[i].number);
                }
            }
            let tempDays=[];
            tempMonths.sort((a,b)=> a-b);
            tempDays.push((this.job.scheStartDate.getDate()).toString());
            hlpDate.setMonth(tempMonths[0]);
            finalFrequencies.push({
                id: this.currentFreq.id,
                type: this.currentFreq.type,
                month: tempMonths,
                day: tempDays,
                hours: this.timeStr.split(':')[0],
                minutes: this.timeStr.split(':')[1].split(' ')[0],
                time: this.timeStr.split(' ')[0]+':00',
                frequencyTime:hlpDate
            });
            break;
            default:
                finalFrequencies.push({
                    id: this.currentFreq.id,
                    type: this.currentFreq.type
                });
            break;
        }
        this.job.frequencies=finalFrequencies;
        this.job.parameters=this.basicInfo.parameters;
        if(this.startDateChange){
            this.job.scheStartDate=new Date(this.job.scheStartDate.getTime() + 86400000);
        }
        if(this.endDateChange){
            if(this.job.scheEndDate)
            this.job.scheEndDate=new Date(this.job.scheEndDate.getTime() + 86400000);
        }
        this.job.scheStartDate.setHours(0);
        this.job.scheStartDate.setMinutes(1);
        this.job.scheStartDate.setSeconds(1);
        this.job.scheEndDate.setHours(23);
        this.job.scheEndDate.setMinutes(59);
        this.job.scheEndDate.setSeconds(59);
        this.schedulingService.testOozieServer().subscribe((oozieStatus: any) => {
            if (oozieStatus && oozieStatus.dbStatus && oozieStatus.ooziestatus) {
                this.schedulingService.postJobDetails(this.job).subscribe((res: any) => {
                    let savedResponse: any;
                    savedResponse = res.json();
                    if (savedResponse && savedResponse != []) {
                        this.changeDisplay.emit(false);
                        //******** Reset Values **********//
                        this.job.scheStartDate=new Date();
                        this.job.scheEndDate=new Date();
                        this.neverEnd=false;
                        this.runType='scheduleTo';
                        this.currentFreq.type='ONETIME';
                        //********************************//
                        if (savedResponse[0] && savedResponse[0].taskName === 'Job save') {
                            if (savedResponse[1] && savedResponse[1].taskName === 'Schedulers Save') {
                                if (savedResponse[2] && savedResponse[2].taskName === 'Initiate Job') {
                                    this.cs.success(
                                        'Done!',
                                        'Job initiated successfully!'
                                    )
                                }
                            }
                            else {
                                this.cs.error( 
                                    'Oops...!',
                                    'Scheduler not saved! Try again.'
                                )
                            }
                        }
                        else {
                            this.cs.error(
                                'Oops...!',
                                'Job not saved! Try again.'
                            )
                        }
                    }
                    else {
                        this.cs.error(
                            'Oops...!',
                            'Job not saved! Try again.'
                        )
                    }
                    this.changeDisplay.emit(false);
                },(res: any)=>{
                    this.changeDisplay.emit(false);
                    this.cs.error('Sorry!', 'Request failed please try again!');
                }
            );
            } else {
                this.cs.error('Sorry!', 'Request failed please try again!');
            }
        },(res: any)=>{
            this.cs.error('Sorry!', 'Request failed please try again!');
        });
    }

    cancelSchedule(){
        this.changeDisplay.emit(false);
    }

    dateChange(dt,isStartdate:boolean){
        if(isStartdate){
            if(this.job.scheEndDate){
                if(this.job.scheEndDate<this.job.scheStartDate)
                    this.job.scheEndDate=this.job.scheStartDate;
            }
        }else{
            this.job.scheEndDate=undefined;
            this.job.scheEndDate=this.job.scheStartDate;
        }
    }
}


export class SchedulingModel{
    constructor(
        public jobName?: string,
        public programName?: string,
        public transformImmediately?:any,
        public parameters?: Parameters[],
        public displayParameters?: Parameters[]
    ){

    }
}

export class Parameters{
    constructor(
        public paramName?: string,
        public selectedValue?: any
    ){

    }
}