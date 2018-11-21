import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { SessionStorageService } from 'ng2-webstorage';
import { MdDialog } from '@angular/material';
import { BulkUploadDialog } from '../mapping-set/mapping-set-dialog.component';
import { LookUpCodeDialogComponent } from './look-up-code-dialog.component';
import { LookUpCode } from './look-up-code.model';
import { LookUpCodeService } from './look-up-code.service';
import { CommonService } from '../common.service';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

@Component({
    selector: 'jhi-look-up-code',
    templateUrl: './look-up-code.component.html'
})
export class LookUpCodeComponent implements OnInit, OnDestroy {

    private unsubscribe: Subject<void> = new Subject();
    lookUpCodes: LookUpCode[];
    editLookUpCodeDetails: any = [];
    itemsPerPage: any = 10;
    lookupCodesRecordsLength: number;
    pageSizeOptions = [10, 25, 50, 100];
    page: any = 0;
    lookupCodesTableColumns = [
        { field: 'meaning', header: 'Meaning', width: '200px', align: 'left' },
        { field: 'description', header: 'Description', width: '200px', align: 'left' }
    ];
    lookupTypesList: any;
    lookupType: any = [];
    dropdownSettings: any = {};
    lookupTypesDropDown: any = [];
    bulkUploadReason: string;
    bulkUploadStatus: string;
    lookupTypeDetails;

    constructor(
        private lookUpCodeService: LookUpCodeService,
        private dateUtils: JhiDateUtils,
        private $sessionStorage: SessionStorageService,
        public dialog: MdDialog,
        private commonService: CommonService
    ) {}

    ngOnInit() {
        // Get all lookup type and get first lookuptype LookUpCode
        this.getAllLookupTypes();

        // MultiSelect Setting for Purpose
        this.dropdownSettings = {
            singleSelection: true,
            text: "Lookup Type",
            enableSearchFilter: true,
        };
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    /**Load all lookup codes */
    loadAll(type) {
        if (type) {
            this.$sessionStorage.store('selcetedLookupType', this.lookupType);
            this.getLookupTypeDetails(type);
            this.lookUpCodeService.getAllLookups(type).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.lookUpCodes = res;
                this.lookUpCodes.forEach((element) => {
                    element.activeStartDate = this.dateUtils.convertDateTimeFromServer(element.activeStartDate);
                    element.activeEndDate = this.dateUtils.convertDateTimeFromServer(element.activeEndDate);
                });
            });
        }
    }

    /**Pagination function */
    onPaginateChange(event) {
        this.page = event.pageIndex;
        this.itemsPerPage = event.pageSize;
        this.lookUpCodeService.getAllLookupCodesPagination(this.page + 1, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.lookUpCodes = res.json();
            this.lookupCodesRecordsLength = + res.headers.get('x-total-count');
        });
    }

    /** Get All Lookup Types List*/
    getAllLookupTypes() {
        this.lookUpCodeService.getAllLookupTypes().takeUntil(this.unsubscribe).subscribe((res) => {
            this.lookupTypesList = res;
            this.lookupTypesList.forEach((lookup) => {
                const obj = {
                    "id": lookup.lookUpType,
                    "itemName": lookup.meaning
                }
                this.lookupTypesDropDown.push(obj);
                lookup.activeStartDate = this.dateUtils.convertDateTimeFromServer(lookup.activeStartDate);
                lookup.activeEndDate = this.dateUtils.convertDateTimeFromServer(lookup.activeEndDate);
            });
            if (this.$sessionStorage.retrieve('selcetedLookupType')) {
                this.lookupType = this.$sessionStorage.retrieve('selcetedLookupType');
                this.loadAll(this.lookupType[0].id);
            } else if (this.lookupTypesList.length) {
                const obj = {
                    "id": this.lookupTypesList[0].lookUpType,
                    "itemName": this.lookupTypesList[0].meaning
                }
                this.lookupType.push(obj);
                this.loadAll(this.lookupType[0].id);
            }
        });
    }

    /* Update LookUpCode */
    updateLookupCode(lookupcode) {
        if (lookupcode.id) {
            this.lookUpCodeService.update(lookupcode).takeUntil(this.unsubscribe).subscribe(() => {
                this.commonService.success('Success!', 'Lookup Code Updated Successfully');
                this.loadAll(this.lookupType[0].id);
            }, () => this.commonService.error('Warning!', 'Error occured while Updating Lookup Code'));
        } else {
            lookupcode.enableFlag = true;
            lookupcode.creationDate = new Date();
            lookupcode.lastUpdatedDate = new Date();
            this.lookUpCodeService.create(lookupcode).subscribe(() => {
                this.commonService.success('Success!', 'Lookup Code Created Successfully');
                this.loadAll(lookupcode.lookUpType);
            }, () => this.commonService.error('Warning!', 'Error occured while Saving Lookup Code'));
        }
    }

    /* Cancel Edit */
    dialogHide() {
        this.loadAll(this.lookupType[0].id);
    }

    changeLookup() {
        this.loadAll(this.lookupType[0].id);
    }

    /* Bulk Upload Dialog */
    openDialog(): void {
        const dialogRef = this.dialog.open(BulkUploadDialog, {
            width: '300px',
            data: { form: 'LOOKUP-CODES', status: this.bulkUploadStatus, reason: this.bulkUploadReason },
            disableClose: true
        });

        // For Applying Bulk Upload Specific Styles
        $('body').addClass('bulk-upload');

        dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
            $('body').removeClass('bulk-upload');
            if (result) {
                if (result.status == 'Success') {
                    this.getAllLookupTypes();
                } else {
                    this.bulkUploadStatus = result.status;
                    this.bulkUploadReason = result.reason;
                    this.openDialog();
                }
            }
        });
    }

    getLookupTypeDetails(type) {
        this.lookUpCodeService.getLookupTypeDetailsByType(type).takeUntil(this.unsubscribe).subscribe((res) => {
            this.lookupTypeDetails = res;
            this.lookupTypeDetails.activeStartDate = this.dateUtils.convertDateTimeFromServer(this.lookupTypeDetails.activeStartDate);
            this.lookupTypeDetails.activeEndDate = this.dateUtils.convertDateTimeFromServer(this.lookupTypeDetails.activeEndDate);
        },
        () => this.commonService.error('Warning!', 'Error Occured while fetching Lookup Type Details'));
    }

    crtOrUpdtLkpCd(isCreation, lookupCode?) {
        let lookupCodeCopy;
        if (isCreation) {
            lookupCodeCopy = {
                lookUpType: this.lookupTypeDetails.lookUpType,
                allLookupTypes: this.lookupTypesDropDown,
                seltdLookUpType: this.lookupType
            }
        } else {
            lookupCodeCopy = JSON.parse(JSON.stringify(lookupCode))
        }
        const dialogRef = this.dialog.open(LookUpCodeDialogComponent, {
            width: '700px',
            disableClose: true,
            panelClass: 'lookup-code',
            data: { 'isCreation': isCreation, 'lookupCode': lookupCodeCopy }
        });

        dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
            if (result) {
                const obj = {
                    "id": result.lookUpType,
                    "itemName": result.lookUpType
                }
                this.lookupType = [];
                this.lookupType.push(obj);
                this.updateLookupCode(result);
            }
        });
    }
}
