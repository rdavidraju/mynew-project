import { Component, Input } from '@angular/core';
import { SharedContentValues } from '../shared-content-values.service';
@Component({
    selector: 'my-datepicker',
    templateUrl: './date-picker.component.html'
})

export class DatePickerComponent{
    @Input() placeholder: string;
    @Input() selectionMode: string;
    dateformat = this.sharedContentValues.dateFormat;
    @Input() showIcon: boolean;
    @Input() icon: string;
    @Input() readonlyInput: boolean;
    @Input() minDate: Date;
    @Input() maxDate: Date;
    @Input() showTime: boolean;
    @Input() required: boolean;
    @Input() value: any;
    hourFormat = this.sharedContentValues.hourFormat;
    
    constructor(
        private sharedContentValues: SharedContentValues
    ) {
        
    }
    
    dateSelected(item: any){
        console.log('hello :'+item);
        console.log('hello :'+this.value);
    }
}
