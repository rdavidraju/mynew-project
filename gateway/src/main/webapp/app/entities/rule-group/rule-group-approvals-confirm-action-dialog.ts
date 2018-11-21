import { Component, Input, OnInit, OnDestroy,ViewChild,Inject } from '@angular/core';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { RuleGroupService } from './rule-group.service';
import {RuleGroupAccountingNewComponent} from './rule-group-accounting-new.component';

@Component({
    selector: 'rule-group-approvals-confirm-action-modal',
    templateUrl: 'rule-group-approvals-confirm-action-modal.html'
})
export class ApprovalsConfirmActionModalDialog {

    constructor(
        public dialogRef: MdDialogRef<ApprovalsConfirmActionModalDialog>,
        public dialog: MdDialog,
        public ruleGroupService :RuleGroupService,
        @Inject(MD_DIALOG_DATA) public data: any) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
