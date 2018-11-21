import { Component, Inject, Directive, HostListener, Input, Output, EventEmitter } from '@angular/core';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';

@Directive({
    selector: '[deleteConfirmation]'
})
export class DeleteConfirmationDirective {
    @Input('deleteMsg') deleteMsg;
    @Output() deleteIt: EventEmitter<any> = new EventEmitter<any>();

    constructor(
        public dialog: MdDialog
    ) { }

    @HostListener('click', ['$event'])
    confirmDelete() {
        const dialogRef = this.dialog.open(DeleteConfirmDialog, {
            width: '300px',
            panelClass: 'delete-confirm-dialog',
            data: {
                title: 'Delete',
                msg: this.deleteMsg ? this.deleteMsg : 'Are you sure ?'
            }
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.deleteIt.emit();
            }
        });
    }

}

@Component({
    selector: "delete-confirm-dialog",
    template: `
    <h1 md-dialog-title>{{data.title}}</h1>
    <div md-dialog-content>
      <h6>{{data.msg}}</h6>
    </div>
    <div md-dialog-actions>
      <button md-raised-button (click)="onNoClick()">Cancel</button>
      <button md-raised-button [md-dialog-close]="true">Yes</button>
    </div>
    `
})
export class DeleteConfirmDialog {
    constructor(
        public dialogRef: MdDialogRef<DeleteConfirmDialog>,
        @Inject(MD_DIALOG_DATA) public data
    ) { }

    onNoClick(): void {
        this.dialogRef.close();
    }
}