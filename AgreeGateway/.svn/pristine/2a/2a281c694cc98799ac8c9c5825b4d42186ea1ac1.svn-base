import { Component } from '@angular/core';
import { Data } from '@angular/router/src/config';

@Component({
    selector: 'period-picker',

    template: `
    <div class="period-picker">  
        <select class="form-control" required name="periodSelection" [(ngModel)]="selectedPeriod">
            <option *ngFor="let period of periods" [value]="period.date">{{period.periodName}}</option>    
        </select>
    </div>`
})

export class PeriodPicker {
     
    periods: Period[]=[];
    selectedPeriod: Date;
    constructor(
    ) {
        this.getPeriods();
    }
    
    getPeriods(){
        var today = new Date();
        var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        this.periods=[];
        for(var i=0;i<50;i++){
            let newDate=Object.assign(new Date(),today);
            newDate.setDate(1);
            newDate.setMonth(newDate.getMonth()-i);
            this.periods.push({periodName: monthNames[newDate.getMonth()]+'-'+newDate.getFullYear().toString(), date: newDate});
        }
    }
}

export class Period {
    constructor(
        public periodName?: string,
        public date?: Date
    ) {
    }
}