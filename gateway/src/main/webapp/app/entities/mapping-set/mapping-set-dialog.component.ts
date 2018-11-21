import { Component, OnInit, Inject } from '@angular/core';
import { FileUploader } from 'ng2-file-upload/ng2-file-upload';
import { MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { NotificationsService } from 'angular2-notifications-lite';
import { MappingSetService } from './mapping-set.service';
import { FxRatesService } from '../fx-rates/fx-rates.service';
import { LookUpCodeService } from '../look-up-code/look-up-code.service';
import { CommonService } from '../common.service';
const URL = '';


@Component({
    selector: 'mapping-set-dialog',
    templateUrl: 'mapping-set-dialog.component.html',
})
export class BulkUploadDialog implements OnInit {

    uploader: FileUploader = new FileUploader({ url: URL });
    bulkUploadDialog = false;
    hasBaseDropZoneOver = false;
    enableLineInfo = false;

    constructor(
        public dialogRef: MdDialogRef<BulkUploadDialog>,
        @Inject(MD_DIALOG_DATA) public data: any,
        public mappingSetService: MappingSetService,
        public fxRatesService: FxRatesService,
        public lookUpCodeService: LookUpCodeService,
        public notificationsService: NotificationsService,
        public commonService: CommonService) {

    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit() {
    }

    /**On File Drop */
    onFileDrop(files: FileList) {
        const file = files[0];
        if (file) {
            if (this.data.form == 'MAPPING-SET') {
                this.bulkUploadMappingSet(file);
            } else if (this.data.form == 'FX-RATES') {
                this.bulkUploadFxRates(file);
            } else if (this.data.form == 'LOOKUP-CODES') {
                this.bulkUploadLookupCode(file);
            } else {
                this.onNoClick();
                this.notificationsService.error('Error', 'Something Went Wrong');
            }
        }
    }

    /**On File Dropping */
    fileOverBase(file: File): void {
        if (file) {
            this.hasBaseDropZoneOver = true;
        }
    }

    /**Single File Upload */
    fileChange(files) {
        this.onFileDrop(files.files);
    }

    /**Close Dialog */
    closeDialog() {
        this.dialogRef.close(this.data);
    }

    /**Bulk Upload Mapping Set */
    bulkUploadMappingSet(file) {
        this.mappingSetService.bulkUpload(file).subscribe((res) => {
            this.successFailureMsg(res);
        },
            (error) => {
                this.notificationsService.error('Error', 'Something Went Wrong');
            });
    }

    /**Bulk Upload FX Rates */
    bulkUploadFxRates(file) {
        this.fxRatesService.bulkUpload(file).subscribe((res) => {
            this.successFailureMsg(res);
        },
            (error) => {
                this.notificationsService.error('Error', 'Something Went Wrong');
            });
    }

    /**Bulk Upload Lookup Code */
    bulkUploadLookupCode(file) {
        this.lookUpCodeService.bulkUpload(file).subscribe((res) => {
            this.successFailureMsg(res);
        },
            (error) => {
                this.notificationsService.error('Error', 'Something Went Wrong');
            });
    }

    /**Send Success or Failure Status to Component */
    successFailureMsg(res) {
        if (res.status == 'Success') {
            this.data.status = 'Success';
            this.closeDialog();
            this.notificationsService.success('Success!', 'Uploaded Successfully');
        } else {
            this.data.status = 'Failed';
            this.data.reason = res.reasons;
        }
    }

    templateDownload() {
        if (this.data.form == 'MAPPING-SET') {
            const mappingSetJson = [{"name":"","description":"","mapping_set_start_date(dd/mm/yyyy)":"","mapping_set_end_date(dd/mm/yyyy)":"","purpose":"","source_value":"","mapping_value":"","mapping_set_value_start_date(dd/mm/yyyy)":"","mapping_set_value_end_date(dd/mm/yyyy)":""}];
            this.commonService.JSONToCSVConverter(mappingSetJson, 'Mapping set sample template', true);
        } else if (this.data.form == 'VALUE-SET') {
            const valueSetJson = [{"name":"","description":"","value_set_start_date(dd/mm/yyyy)":"","value_set_end_date(dd/mm/yyyy)":"","purpose":"","code":"","meaning":"","value_set_value_start_date(dd/mm/yyyy)":"","value_set_value_end_date(dd/mm/yyyy)":""}];
            this.commonService.JSONToCSVConverter(valueSetJson, 'Value set sample template', true);
        } else if (this.data.form == 'FX-RATES') {
            const fxRatesJson = [{"name":"","description":"","conversion_type":"","start_date(dd/mm/yyyy)":"","end_date(dd/mm/yyyy)":"","from_currency":"","to_currency":"","from_date(dd/mm/yyyy)":"","to_date(dd/mm/yyyy)":"","conversion_rate":"","inverse_rate":""}];
            this.commonService.JSONToCSVConverter(fxRatesJson, 'FX rates sample template', true);
        } else if (this.data.form == 'LOOKUP-CODES') {
            const lookupCodeJson = [{ "look_up_code": "", "look_up_type": "", "meaning": "", "description": "", "start_date": "", "end_date": "" }];
            this.commonService.JSONToCSVConverter(lookupCodeJson, 'Lookup code sample template', true);
        }
    }

    uploadAgain() {
        this.data.status = '';
    }
}