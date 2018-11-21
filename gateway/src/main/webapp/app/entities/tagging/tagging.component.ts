import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Frequencies, Jobs, } from '../jobs/jobs.model';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { TaggingService } from './tagging.service';
import { NotificationsService } from 'angular2-notifications-lite';
import { DatePipe } from '@angular/common';

import { ITEMS_PER_PAGE } from '../../shared';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/map';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'tagging',
    templateUrl: './tagging.component.html'
})
export class Tagging implements OnInit {
    @Input('basicInfor') basicInfor = new processInputObj();
    @Output() changeDisplay: EventEmitter<boolean> = new EventEmitter<boolean>();
    page: number = 0;
    itemsPerPage: number;
    selectedItems = [];
    selectedProcessIds: number[] = [];
    processId: any;
    sourceProfilesList: any[] = [];
    reconRuleGroups: any[] = [];
    processTaggingObj: any[] = [];
    taggingType: processDetailss = {};
    acctRuleGroups: any[] = [];
    processDetailsList: any[] = [];
    processList: any[] = [];
    constructor(
        private taggingService: TaggingService,
        private notificationsService: NotificationsService,
        private datePipe: DatePipe
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
    }

    getProcessDetails() {
        this.taggingService.getProcess(this.page, this.itemsPerPage).subscribe(
            (res: any) => {
                res.forEach(item => {
                    this.processList.push({
                        "itemName": item.processName,
                        "id": item.id
                    });
                });
                console.log('processes list : ' + JSON.stringify(this.processList));
                if (this.processList.length > 0) {

                } else {
                    this.notificationsService.info('Info!', 'No Processes Found!');
                }
            },
            (res: Response) => {
                this.notificationsService.error('Error!', 'Failed to get Processes List!');
            }
        );
    }

    onSelectTemplateAndDataView(ids) {
        ids.forEach(item => {
            this.selectedProcessIds.push(item.id);
        });
        console.log('ite m : ' + JSON.stringify(ids));
        this.taggingService.getProcessDetail(this.selectedProcessIds).subscribe(
            (res: any) => {
                this.processDetailsList = res;
                this.selectedProcessIds = [];
                if (this.processDetailsList.length > 0) {
                    this.processDetailsList[0].firstOpened = true;
                } else {
                    this.notificationsService.info('Info!', 'No Process Details  Found!');
                }
            },
            (res: Response) => {
                this.notificationsService.error('Error!', 'Failed to get Process Details!');
            }
        );

    }

    saveProcess() {
        let idlen: number = 0;
        this.processTaggingObj = [];
        this.processDetailsList.forEach(item => {
            if (this.basicInfor.tagType == 'sourceProfile') {
                item.processDetailList.spList.forEach(i => {
                    if (this.basicInfor.id == i.typeId) {
                        idlen = idlen + 1;
                    }
                });
            }

            if (this.basicInfor.tagType == 'reconciliationRuleGroup') {
                item.processDetailList.spList.forEach(i => {
                    if (this.basicInfor.id == i.typeId) {
                        idlen = idlen + 1;
                    }
                });
            }

            if (this.basicInfor.tagType == 'accountingRuleGroup') {
                item.processDetailList.spList.forEach(i => {
                    if (this.basicInfor.id != i.typeId) {
                        idlen = idlen + 1;
                    }
                });
            }
            if (idlen == 0) {
                this.processTaggingObj.push({
                    "processId": item.id,
                    "tagType": this.basicInfor.tagType,
                    "typeId": this.basicInfor.id
                });
            }
        })
        if (this.processTaggingObj.length > 0) {
            this.taggingService.postProcessForTagging(this.processTaggingObj).subscribe(
                (res: any) => {
                    this.processTaggingObj = [];
                    $('.tagging-custom-dialog').css('right', '-600px');
                    this.notificationsService.success('Success!', this.basicInfor.name + ' Source Profile Tagged Successfully')
                },
                (res: Response) => {
                    this.notificationsService.error('Error!', 'Failed to get Process Details!');
                }
            );
        } else {
            this.notificationsService.info('Info!', this.basicInfor.name + ' is already tagged to selected processes!');
        }
    }

    closeDialog() {
        $('.tagging-custom-dialog').css('right', '-600px');
    }

    ngOnInit() {
        $('.tagging-custom-dialog').css('right', '20px');
        this.getProcessDetails();
        //   console.log('basicInfo' + JSON.stringify(this.basicInfor));
    }
}

export class processInputObj {
    constructor(
        public name?: any,
        public id?: any,
        public tagType?: any
    ) {

    }
}

// export class saveProcessObj{
//     constructor(
//         public processName?: any,
//         public desc?: any,
//         public startDate?: any,
//         public endDate?: any,
//         public processDetailList?: processDetailss,
//     ){

//     }
// }

export class processDetailss {
    constructor(
        public spList?: any[],
        public actGrpList?: any[],
        public recGrpList?: any[]
    ) {
        this.spList = [];
        this.actGrpList = [];
        this.recGrpList = [];
    }
}