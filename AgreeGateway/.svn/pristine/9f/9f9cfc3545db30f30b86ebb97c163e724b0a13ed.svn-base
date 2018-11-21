import {Component, Inject, OnInit } from '@angular/core';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
@Component({
    selector: 'confirm-dialog',
    templateUrl: 'confirm.dialog.component.html'
})
export class ConfirmDialogComponent implements OnInit {
    constructor(
        public dialogRef: MdDialogRef<ConfirmDialogComponent>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any)
         {
    }

    ngOnInit(){
    }

    close(): void {
        this.dialogRef.close();
    }
} 