import { Component, OnInit, OnDestroy, Input, ElementRef, Renderer2, Inject, ViewChild } from '@angular/core';
import { NotificationsService } from 'angular2-notifications-lite';
import { OverlayContainer } from '@angular/cdk/overlay';
import {MdDialog, MdDialogRef, MD_DIALOG_DATA} from '@angular/material';
declare var $: any;
declare var jQuery: any;

import { AttachmentModel, blobTypes } from './attachment.model';
import { AttachmentService } from './attachment.service';


/**Material Dialog for displaying failed files with reasons */
@Component({
    selector: 'attachment-failed-dialog',
    template: `
        <h6 md-dialog-title>Failed Attchments List</h6>
        <md-dialog-content>
            <table class="table" *ngIf="failedAttachments.length">
                <thead>
                    <tr>
                        <th>File Name</th>
                        <th>Failed Reason</th>
                    </tr>
                </thead>
                <tbody>
                    <tr *ngFor="let fail of failedAttachments">
                        <td>{{fail.fileName}}</td>
                        <td>{{fail.fileContent}}</td>
                    </tr>
                </tbody>
            </table>
        </md-dialog-content>
        <md-dialog-actions>
            <button md-dialog-close md-button>Ok</button>
        </md-dialog-actions>
    `,
})
export class AttachmentFailedDialog {
    failedAttachments: any;
    successedAttachments: any;
    constructor(
        public dialogRef: MdDialogRef<AttachmentFailedDialog>,
        @Inject(MD_DIALOG_DATA) public data: any
    ) { 
        this.failedAttachments = this.data.failed;
        this.successedAttachments = this.data.success;
    }
}

@Component({
    selector: 'attachment',
    templateUrl: './attachment.component.html',
    styleUrls: ['./attachment.component.scss']
})
export class Attachment implements OnInit, OnDestroy {
    attachments: AttachmentModel;
    disAttchDialog: boolean = false;
    viewAttachment: boolean = false;
    @Input() attachmentsKey;
    @Input() attachmentCategory;

    constructor(
        private notificationsService: NotificationsService,
        private attachmentService: AttachmentService,
        private eRef: ElementRef,
        private renderer: Renderer2,
        private overlayContainer: OverlayContainer,
        public dialog: MdDialog,
    ) { }

    /** 
     * @description On component Initialization
    */
    ngOnInit() {
        if (this.attachmentsKey) {
            this.attachmentService.getAttachment(this.attachmentsKey).subscribe(res => {
                if (res.id) {
                    this.viewAttachment = true;
                    this.attachments = res;
                }
            }, err => {
                this.notificationsService.error('Warning', 'Error occured while fetching Attachment\'s');
            });
        }
    }

    /** 
     * @description On component destory
    */
    ngOnDestroy() {
        this.closeAttchment();
    }

    /** 
     * @author Sameer
     * @description Show Attachment Dropdown
    */
    showAttachments() {
        this.disAttchDialog = true;
    }

    /** 
     * @author Sameer
     * @description Close Attachment Dropdown
    */
   @ViewChild('file') fileInput;
    closeAttchment() {
        if (this.fileInput) {
            this.fileInput.nativeElement.value = null
        }
        this.disAttchDialog = false;
        this.attchUrls = [];
        this.attchTypeVal = null;
        this.formInvalid = null;
    }

    /**
     * @author Sameer
     * @description Converting file to blob, or downloading blob as file
     * @param fileBlob
     * @param isConvert
     * @param fileName
     */
    filetoBloborDwnld(fileBlob, isConvert?, fileName?) {
        return new Promise((resolve, reject) => {
            if (isConvert) {
                if (!fileBlob && !fileBlob.length) return reject('File is mandatory.');
                this.convertFilestoBlob(fileBlob).then(allFiles => {
                    resolve(allFiles);
                });
            } else {
                let a = document.createElement('a');
                a.href = fileBlob;
                a.download = fileName ? fileName : 'file-' + new Date();
                a.click();
            }
        });
    }

    /**
     * @author Sameer
     * @description Converting Files to blob with proper validations
     * @param files 
     */
    convertFilestoBlob(files) {
        let successStatus = [], failedStatus = [];
        return new Promise((resolve, reject) => {
            let filesArray: any = Array.from(files);
            filesArray.forEach((file, i) => {
                let ext = file.name.split('.');
                let extension = ext[ext.length - 1];
                let selType = blobTypes[extension.toString().toLowerCase()];
                if (file.size / 1024 / 1024 > 4) {
                    //Checking if file is greater than 4mb
                    failedStatus.push({ fileName: file.name, fileType: ext, fileContent: 'File is greater than 4MB.' });
                    if (i == filesArray.length - 1) {
                        let obj = {
                            success: successStatus,
                            failed: failedStatus
                        }
                        resolve(obj);
                    }
                } else if (!selType) {
                    //Checking if file extension is supported
                    failedStatus.push({ fileName: file.name, fileType: ext, fileContent: 'File extension not supported.' });
                    if (i == filesArray.length - 1) {
                        let obj = {
                            success: successStatus,
                            failed: failedStatus
                        }
                        resolve(obj);
                    }
                } else {
                    let blob = new Blob([file], { type: selType });
                    let reader = new FileReader();
                    reader.readAsDataURL(blob);
                    reader.onloadend = function () {
                        successStatus.push({ fileName: file.name, fileType: extension, fileContent: reader.result.split(',')[1] });
                        if (i == filesArray.length - 1) {
                            let obj = {
                                success: successStatus,
                                failed: failedStatus
                            }
                            resolve(obj);
                        }
                    }
                }
            });
        });
    }

    /**
     * @author Sameer
     * @description Attchment Type Selection
     */
    attchTypeVal: any;
    attchUrls: any[];
    attchType(val, input) {
        if (val == 'localFile') {
            val = null;
            input.click();
        } else {
            let obj = { fileName: null, url: null };
            this.attchUrls = [];
            this.attchUrls.push(obj);
        }
    }

    /**
     * @author Sameer
     * @description Add new or delete url obj
     */
    formInvalid: boolean = false;
    addUrlorDelete(i) {
        if (i >= 0) {
            this.attchUrls.splice(i, 1);
            if (!this.attchUrls.length) this.attchTypeVal = null;
        } else {
            this.urlFormValidation().then((res: boolean) => {
                if (res) {
                    this.formInvalid = res;
                } else {
                    let obj = { fileName: null, url: null };
                    this.attchUrls.unshift(obj);
                }
            });
        }
    }

    /** 
     * @author Sameer
     * @description Url Form Validation
    */
    urlFormValidation() {
        return new Promise((resolve, reject) => {
            for (let i = 0; i < this.attchUrls.length; i++) {
                if (!this.attchUrls[i].fileName || !this.attchUrls[i].url) {
                    resolve(true);
                } else {
                    resolve(false);
                }
            }
        });
    }

    addAttachment(input) {
        if (input) {
            this.attchTypeVal = null;
            this.filetoBloborDwnld(input.files, true).then((res: any) => {
                input.value = null;
                console.log(res);
                this.createPostObj(res.success).then(poObjRes => {
                    this.saveAttachment(poObjRes);
                    this.displayFailedFiles(res);
                }).catch(err => {
                    console.log(err);
                    this.displayFailedFiles(res);
                });
            }).catch(err => {
                console.error(err);
                input.value = null;
                this.notificationsService.error('Warning!', err);
            });
        } else {
            this.urlFormValidation().then((res: boolean) => {
                if (res) {
                    this.formInvalid = res;
                } else {
                    this.createPostObj(this.attchUrls).then(poObjRes => {
                        this.attchTypeVal = null;
                        this.attchUrls = [];
                        if(poObjRes) {
                            this.saveAttachment(poObjRes);
                        }
                    });
                }
            });
        }
    }

    createPostObj(data) {
        return new Promise((resolve, reject) => {
            let attch = new AttachmentModel();
            attch.attachmentKey = this.attachmentsKey;
            attch.attachmentCategory = this.attachmentCategory;
            attch.attachmentFilesList = [];
            if(data) {
                data.forEach(eachBlob => {
                    if(this.attachments){
                        eachBlob.attachmentId = this.attachments && this.attachments.attachmentFilesList.length ? this.attachments.attachmentFilesList[0].attachmentId : this.attachments.id;
                    } else {
                        eachBlob.attachmentId = this.attachments && this.attachments.attachmentFilesList.length ? this.attachments.attachmentFilesList[0].attachmentId : null;
                    }
                    
                    
                    attch.attachmentFilesList.push(eachBlob);
                });
            } else {
                reject('No files to post');
            }
            let finObj: any = this.viewAttachment ? attch.attachmentFilesList : attch;
            resolve(finObj);
        });
    }

    displayFailedFiles(res) {
        if (res.failed.length) {
            let dialogRef = this.dialog.open(AttachmentFailedDialog, {
                width: '400px',
                data: { success: res.success, failed: res.failed },
                disableClose: true
            });
        }
    }

    saveAttachment(obj) {
        this.attachmentService.postAttachment(obj, this.viewAttachment).subscribe(res => {
            this.notificationsService.success('Success!', 'Attachment uploaded successfully.');
            this.ngOnInit();
        }, err => {
            this.notificationsService.error('Warning!', 'Error occured while saving attachment');
        });
    }

    attchActions(i, isIndex) {
        if (isIndex == 'index') {
            let url = this.attachments.attachmentFilesList[i].fileContent;
            let fileType = blobTypes[this.attachments.attachmentFilesList[i].fileType];
            let fileName = this.attachments.attachmentFilesList[i].fileName;
            url = `data:${fileType};base64,${url}`;
            this.filetoBloborDwnld(url, false, fileName);
        } else if (isIndex == 'url'){
            let url = this.attachments.attachmentFilesList[i].url;
            if(url.toString().toLowerCase().search('http') != -1) {
                window.open(url);
            } else {
                window.open('http://'+url);
            }
        }
    }

    deleteAttachment(attachment) {
        this.attachmentService.deleteAttachment(attachment.id).subscribe(res => {
            this.notificationsService.success('Success!', 'Attachment succesfully deleted.');
            this.ngOnInit();
        }, err => {
            this.notificationsService.error('Warning!', 'Error occured while deleting attachment');
        });
    }

    duplicateNameCheck(url, ind) {
        if (!url) return;
        this.attchUrls.forEach((attchUrl, i) => {
            if (attchUrl.fileName == url.fileName && ind != i) {
                url.duplicateFileName = true;
            }
        });
    }
}
