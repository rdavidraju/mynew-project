import {Component, Inject, OnInit} from '@angular/core';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormControl } from '@angular/forms';
import { ReportsService } from './reports.service';
import { NotificationsService } from 'angular2-notifications-lite';

import "rxjs/add/operator/takeWhile";
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
@Component({
    selector: 'share-via-email',
    templateUrl: 'shareViaEmail.component.html'
})
export class ShareViaEmailDialog implements OnInit {
    constructor(
        public dialogRef: MdDialogRef<ShareViaEmailDialog>,
        public dialog: MdDialog,
        private notificationsService: NotificationsService,
        private reportsService: ReportsService,
        @Inject(MD_DIALOG_DATA) public data: any) {
    }

    allUsers:any[]=[];
    selectedUsers:any[]=[];
    sendToOther:boolean=false;
    private alive: boolean = true;      //Unsubscribe variable
    otherEamils:string='';

    ngOnInit(){
        this.allUsers=[];
        this.selectedUsers=[];
        this.otherEamils='';
        this.reportsService.getAllUsers().takeWhile(()=>this.alive).subscribe((res:any[])=>{
            res.forEach(element => {
                this.allUsers.push({
                    "id": element.id,
                    "itemName": element.firstName+ ' ' +element.lastName,
                    "dataName": element.email
                });
            });
        })
    }

    userSelectionSettings = {
        text: "You can select multiple Users",
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        enableSearchFilter: false,
        badgeShowLimit: 2
    };

    close(): void {
        this.dialogRef.close();
    }

    checkCode(code){
        if(code==44)
            this.checkOtherEmails();
    }

    checkOtherEmails(){
        this.otherEamils=this.otherEamils.trim();
        this.otherEamils.split(',').forEach(element => {
            if (element.trim().length > 0) {
                if (!this.validateEmail(element.trim())) {
                    this.notificationsService.error('Invalid E-Mail', element, {
                        timeOut: 3000,
                        showProgressBar: false,
                        pauseOnHover: true,
                        clickToClose: true,
                        maxLength: 50
                    });
                }
            }
        });
    }

    validateEmail(email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    share(){
        let emails='';
        for(var i=0, len=this.selectedUsers.length; i<len;i++){
            if(i>0)
                emails=emails+';';
            emails=emails+this.selectedUsers[i].dataName;
        }
        if(this.otherEamils.length>3){
            if(emails.length>0)
                emails=emails+';';
            emails=emails+this.otherEamils.replace(' ','');
        }
        this.reportsService.shareReport(emails,this.data.reqId).takeWhile(() => this.alive).subscribe((res: any)=>{
            if(res.status){
                if(res.status=='success'&&res.failed==0){
                    this.notificationsService.success('Done!', 'Report shared to :'+res.send.toString() + ' E-mails', {
                        timeOut: 3000,
                        showProgressBar: false,
                        pauseOnHover: true,
                        clickToClose: true,
                        maxLength: 50
                    });
                }else{
                    this.notificationsService.success('Done!', 'Report shared to :'+res.send.toString() + ' E-mails \nFailed to :'+res.failed.toString(), {
                        timeOut: 5000,
                        showProgressBar: false,
                        pauseOnHover: true,
                        clickToClose: true,
                        maxLength: 70
                    });
                }
                
            }else{
                this.notificationsService.error('Sorry!', 'Request failed to share', {
                    timeOut: 3000,
                    showProgressBar: false,
                    pauseOnHover: true,
                    clickToClose: true,
                    maxLength: 50
                });
            }
        });
        
        this.close();
    }
} 