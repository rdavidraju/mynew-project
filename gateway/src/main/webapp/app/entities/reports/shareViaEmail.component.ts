import {Component, Inject, OnInit, OnDestroy} from '@angular/core';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormControl } from '@angular/forms';
import { ReportsService } from './reports.service';
import { Subscription, Subject } from 'rxjs/Rx';
import { CommonService } from '../common.service';

import "rxjs/add/operator/takeWhile";
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
@Component({
    selector: 'share-via-email',
    templateUrl: 'shareViaEmail.component.html'
})
export class ShareViaEmailDialog implements OnInit, OnDestroy {
    allUsers:any[]=[];
    selectedUsers:any[]=[];
    sendToOther:boolean=false;
    private unsubscribe: Subject<void> = new Subject();      //Unsubscribe variable
    otherEamils:string='';
    comments:string='';
    userSelectionSettings = {
        text: "You can select multiple Users",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: false,
        badgeShowLimit: 2
    };

    constructor(
        public dialogRef: MdDialogRef<ShareViaEmailDialog>,
        public dialog: MdDialog,
        private cs: CommonService,
        private reportsService: ReportsService,
        @Inject(MD_DIALOG_DATA) public data: any) {
    }

    ngOnInit(){
        this.allUsers=[];
        this.selectedUsers=[];
        this.otherEamils='';
        this.reportsService.getAllUsers().takeUntil(this.unsubscribe).subscribe((res:any[])=>{
            res.forEach((element) => {
                this.allUsers.push({
                    "id": element.id,
                    "itemName": element.firstName+ ' ' +element.lastName,
                    "dataName": element.email
                });
            });
        })
    }

    ngOnDestroy(){
        this.unsubscribe.next();
        this.unsubscribe.complete();
        this.cs.destroyNotification(); 
    }

    close(): void {
        this.dialogRef.close();
    }

    checkCode(code){
        if(code==44){
            this.checkOtherEmails();
        }
    }

    checkOtherEmails(){
        this.otherEamils=this.otherEamils.trim();
        this.otherEamils.split(',').forEach((element) => {
            if (element.trim().length > 0) {
                if (!this.validateEmail(element.trim())) {
                    this.cs.error('Invalid E-Mail', element);
                }
            }
        });
        this.otherEamils=this.otherEamils.replace(' ','');
    }

    validateEmail(email) {
        const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    share(){
        let emails='';
        this.otherEamils=this.otherEamils.replace(' ','');
        for(let i=0, len=this.selectedUsers.length; i<len;i++){
            if(i>0){
                emails=emails+';';
            }
            emails=emails+this.selectedUsers[i].dataName;
        }
        if(this.otherEamils.length>3){
            if(emails.length>0){
                emails=emails+';';
            }
            const tempArr=this.otherEamils.split(',');
            emails=emails+tempArr.join(';');
        }
        this.reportsService.shareReport(emails,this.data.reqId,this.data.url,this.comments).takeUntil(this.unsubscribe).subscribe((res: any)=>{
            if(res.status){
                if(res.status=='success'&&res.failed==0){
                    this.cs.success('Done!', 'Report shared to :'+res.send.toString() + ' E-mails');
                }else{
                    this.cs.info('Done!', 'Report shared to :'+res.send.toString() + ' E-mails \nFailed to :'+res.failed.toString());
                }
                
            }else{
                this.cs.error('Sorry!', 'Request failed to share');
            }
        });
        
        this.close();
    }
} 