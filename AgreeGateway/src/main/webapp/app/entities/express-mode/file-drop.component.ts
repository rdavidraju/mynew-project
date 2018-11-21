//#region Import section
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { NotificationsService } from 'angular2-notifications-lite';
import { CommonService } from '../../entities/common.service';
import {FileSelectDirective, FileDropDirective, FileUploader,FileItem } from 'ng2-file-upload/ng2-file-upload';
import { RowIdentifier } from './express-mode.model';
import { ExpressModeService } from './express-mode.service';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import "rxjs/add/operator/takeWhile";
import { from } from 'rxjs/observable/from';
declare var $: any;
declare var jQuery: any;
const URL='';
//#endregion

@Component({
    selector: 'jhi-file-drop',
    templateUrl: './file-drop.component.html'
})
export class FileDropComponent implements OnDestroy, OnInit {
    //#region Parameters section
    private alive: boolean = true;      //Unsubscribe variable
    public uploader:FileUploader =new FileUploader({url:URL});
    hasBaseDropZoneOver:boolean = false;
    srcTemplateName?: string;
    trgtTemplateName?: string;
    srcCreateTemplate: boolean=false;
    trgtCreateTemplate: boolean=false;
    srcRowIdentifier =new RowIdentifier();
    trgtRowIdentifier =new RowIdentifier();
    srcDelemiter: string='';
    trgtDelemiter: string='';
    delimiterList:LookUpCode[]=[];
    srcSampleData: any[]=[];
    trgtSampleData: any[]=[];
    
    //#endregion

    constructor(
        private eventManager: JhiEventManager,
        private notificationsService: NotificationsService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        private expressModeService: ExpressModeService,
        // public dialog: MdDialog
    ) {
        
    }
    
    ngOnInit() {
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 400) +'px'
          });

          this.expressModeService.fetchLookUpsByLookUpType('DELIMITER').subscribe( (res: LookUpCode[]) => {
            this.delimiterList = res;
        });
    }

    ngOnDestroy() {
        this.alive = false;
    }

    fileOverBase(file:File):void{
        if(file)
            this.hasBaseDropZoneOver=true;
    }

    fileChange(file : any)
    {
        this.onFileDrop(file.files);
    }

    public onFileDrop(filelist: FileList): void {
        if (filelist.length > 0) {
            let file: File = filelist[0];
            console.log('File name: ' + file.name);
            let formData = new FormData();
            formData.append('file', file);
            formData.append('multipleIdentifiersList',JSON.stringify([this.srcRowIdentifier]));
            formData.append('multipleIdentifier','false');
            formData.append('delimiter', this.srcDelemiter);
            this.expressModeService.fetchSampleDataByFile(formData).subscribe(
                (res: any) => {
                    let resp: any = res;
                    console.log(res);
                    this.srcDelemiter=res.delimeterDescription;
                    if(res.rowIdentifier){
                        this.srcRowIdentifier=res.rowIdentifier;
                    }
                    if(res.data.length<1){
                        return;
                    }
                    this.srcSampleData=res.data;

                    /* msg = resp[0].status;
                    if (msg == 'Success' || msg == 200) {
                        this.extractedFileData = resp[0];
                        this.colHeaders = this.extractedFileData.headers;
                        this.sampleDataForMIInVIewMode = [];
                        let sampleDataList = [];
                        this.extractedFileData['extractedData'].forEach(sampleDataObj => {
                            let sampleDatawithRI = {};
                            Object.keys(sampleDataObj).forEach(key => {
                                let value = sampleDataObj[key];
                                sampleDatawithRI['rowIdentifier'] = key;
                                sampleDatawithRI['sampleData'] = value;
                            });
                            sampleDataList.push(sampleDatawithRI);
                        });
                        this.data = (sampleDataList);
                        let delim: string = '';
                        if (this.data && this.data[0] && this.data[0].delimiter) {
                            delim = this.data[0].delimiter;
                            this.fileTemplate.delimiter = delim;
                        }
                        this.fileTemplate.fileType = this.extractedFileData.fileType;
                        this.fileTemplateLines = this.extractedFileData.templateLines;
                        this.uploadedIcon = true;
                        this.selectedFileType();
                        this.lastLineNumber = +this.extractedFileData.lastLineNumber;
                        console.log('this.lastLineNumber:' + this.lastLineNumber);
                        this.fileTemplateLinesInfo = [];
                        console.log(' this.fileTemplateLinesInfo lengthhhh' + this.fileTemplateLinesInfo.length);
                        this.addLinee();
                        this.enableLineInfo = true;
                        this.viewSDdata();
                        this.refreshFile = false;
                    } */
                });
        }
        else {
            this.notificationsService.info(
                'Please drop a file',
                'Sorry!'
            )

        }
    }
}
