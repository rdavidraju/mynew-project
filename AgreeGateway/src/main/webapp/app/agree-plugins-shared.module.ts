import 'jquery';
import 'hammerjs';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MaterialModule, MdTabsModule, MdNativeDateModule, MdDatepickerModule,MdAutocompleteModule, NativeDateAdapter, MD_DATE_FORMATS, DateAdapter,MdDialogModule,MdProgressBarModule,MdExpansionPanel, MdMenu, MdList } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileUploadModule } from "ng2-file-upload";
import { DndModule } from 'ng2-dnd';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SidebarModule } from 'ng-sidebar';
import { SimpleNotificationsModule } from 'angular2-notifications-lite';
import { InlineEditorModule } from '@qontu/ngx-inline-editor';
import { ChartsModule } from 'ng2-charts';
import { SplitPaneModule } from 'ng2-split-pane/lib/ng2-split-pane';
//import {SidebarModules} from 'primeng/primeng';/home/nspl/reconAgreeNew4/agreeGatewayV1/src/main/webapp/app/shared/primeng
import {SidebarModules} from './shared/primeng/primeng';
import 'chart.js';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import 'chartjs-plugin-datalabels';

import {
  PaginatorModule,
  AccordionModule,
  DataTableModule,
  SharedModule,
  MultiSelectModule,
  ButtonModule,
  CalendarModule,
 // ChartModule,
  DialogModule,
  OverlayPanelModule,
  DataGridModule,
  InputMaskModule,
  ConfirmDialogModule,
  ColorPickerModule,
  BlockUIModule,
} from 'primeng/primeng';
//import { AngularSplitModule,SplitAreaDirective,SplitComponent,SplitGutterDirective } from 'angular-split';
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface } from 'angular2-perfect-scrollbar';
import { PopoverModule } from "ngx-popover";

import { PeriodPicker } from './period-picker.component';
import { Scheduling } from './entities/scheduling/scheduling.component';
import { SchedulingService } from './entities/scheduling/scheduling.service';
import { ConfirmDialogComponent } from './entities/scheduling/confirm.dialog.component';
import { Tagging } from './entities/tagging/tagging.component';
import { TaggingService } from './entities/tagging/tagging.service';



const PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
};
@NgModule({
  imports: [
    MaterialModule,
    MdNativeDateModule,
    MdDatepickerModule,
    MdAutocompleteModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    FileUploadModule,
    DndModule.forRoot(),
    NgbModule.forRoot(),
    SidebarModule.forRoot(),
    SimpleNotificationsModule.forRoot(),
    InlineEditorModule,
    ChartsModule,
    SplitPaneModule,
    SidebarModules,
    AngularMultiSelectModule,
    PaginatorModule,
    AccordionModule,
    DataTableModule,
    SharedModule,
    MultiSelectModule,
    ButtonModule,
    CalendarModule,
   // ChartModule,
    DialogModule,
    OverlayPanelModule,
    ConfirmDialogModule,
    BlockUIModule,
    ColorPickerModule,
    InputMaskModule,
    PerfectScrollbarModule.forRoot(PERFECT_SCROLLBAR_CONFIG),
    PopoverModule,
    
  ],
  declarations: [
    // components and directives
      PeriodPicker,
      Scheduling,
      Tagging,
      ConfirmDialogComponent
  ],
  entryComponents: [ConfirmDialogComponent],
  providers: [
    //        Services
      SchedulingService,
      TaggingService,
       /* {provide:HIGHCHARTS_MODULES,useFactory: highchartsModules} */
  ],
  exports: [
    MaterialModule,
    MdNativeDateModule,
    MdDatepickerModule,
    MdAutocompleteModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    FileUploadModule,
    DndModule,
    NgbModule,
    SidebarModule,
    SimpleNotificationsModule,
    InlineEditorModule,
    ChartsModule,
    SplitPaneModule,

    AngularMultiSelectModule,
    PaginatorModule,
    AccordionModule,
    DataTableModule,
    SharedModule,
    MultiSelectModule,
    ButtonModule,
    CalendarModule,
    /* ChartModule, */
    InputMaskModule,
    PerfectScrollbarModule,
    PopoverModule,
    DialogModule,
    OverlayPanelModule,
    ConfirmDialogModule,
    BlockUIModule,
    ColorPickerModule,
    PeriodPicker,
    MdDialogModule,
    MdExpansionPanel,
    MdMenu,
    MdList,
	MdProgressBarModule,
    Scheduling,
    Tagging
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgreePlugInsSharedModule { }