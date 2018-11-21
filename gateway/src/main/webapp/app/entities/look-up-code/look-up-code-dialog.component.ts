import { Component, OnInit, Inject } from '@angular/core';
import { LookUpCode } from './look-up-code.model';
import { MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { LookUpCodeService } from './look-up-code.service';

@Component({
    selector: 'look-up-code-dialog',
    templateUrl: './look-up-code-dialog.component.html',
    styles: [
        `
            .mat-dialog-title {
                background: whitesmoke;
                padding: 0.5rem !important;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .mat-slide-toggle {
                padding-top: 5px;
            }
            .mat-dialog-content {
                padding: 1rem;
                margin: 0;
            }
            .mat-dialog-actions {
                padding: 0 1rem 1rem 0;
                margin: 0;
                justify-content: flex-end;
            }
            .col-md-6, .col-md-12 {
                padding-bottom: 5px;
            }
        `
    ]
})
export class LookUpCodeDialogComponent implements OnInit {

    lookupCode = new LookUpCode();
    isDuplicate;
    allLookupTypes;
    seltdLookUpType;
    dropdownSettings = {
        singleSelection: true,
        text: "Lookup Type",
        enableSearchFilter: true,
    };

    constructor(
        public dialogRef: MdDialogRef<LookUpCodeDialogComponent>,
        @Inject(MD_DIALOG_DATA) public data,
        private lookUpCodeService: LookUpCodeService,
    ) { }

    ngOnInit() {
        if (!this.data.isCreation) {
            this.data.lookupCode.activeStartDate = new Date(this.data.lookupCode.activeStartDate);
            if (this.data.lookupCode.activeEndDate) {
                this.data.lookupCode.activeEndDate = new Date(this.data.lookupCode.activeEndDate);
            }
            this.lookupCode = this.data.lookupCode;
        } else {
            this.allLookupTypes = this.data.lookupCode.allLookupTypes;
            this.seltdLookUpType = this.data.lookupCode.seltdLookUpType;
            this.lookupCode.lookUpType = this.data.lookupCode.lookUpType;
            this.lookupCode.activeStartDate = new Date();
        }
    }

    closeDialog(close) {
        this.dialogRef.close(close ? null : this.lookupCode);
    }

    isDuplicateFn(type, code, id) {
        if (code) {
            this.lookUpCodeService.checkLookUpCodeIsExist(type, code, id).subscribe((res) => {
                this.isDuplicate = res.result != 'No Duplicates Found' ? res.result : undefined;
            });
        }
    }

    lookupChng() {
        this.lookupCode.lookUpType = this.seltdLookUpType[0].id;
        this.isDuplicate = '';
    }
}