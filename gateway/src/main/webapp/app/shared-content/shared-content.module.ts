import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
//import { AgreePlugInsSharedModule } from '../agree-plugins-shared.module';
import { DatePickerComponent } from './date-picker/date-picker.component'
import { CalendarModule, MultiSelectModule } from 'primeng/primeng';
import { SharedContentValues } from './shared-content-values.service';
@NgModule({
    imports: [
        BrowserModule,
        CalendarModule,
        MultiSelectModule,
        FormsModule
//        AgreePlugInsSharedModule,
    ],
    declarations: [DatePickerComponent],
    entryComponents: [DatePickerComponent],
    exports: [DatePickerComponent],
    providers: [ SharedContentValues ],
})
export class SharedContentModule{};