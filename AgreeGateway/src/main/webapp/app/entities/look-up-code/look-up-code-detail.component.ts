import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';
import { Response } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { SessionStorageService } from 'ng2-webstorage';

import { LookUpCode } from './look-up-code.model';
import { LookUpCodeService } from './look-up-code.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-look-up-code-detail',
    templateUrl: './look-up-code-detail.component.html'
})
export class LookUpCodeDetailComponent implements OnInit {

    /**Userdata from Session storage  */
    private UserData = this.$sessionStorage.retrieve('userData');
    lookUpCodeDetails = new LookUpCode();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    /**New Variables */
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA: boolean = false;
    /**All lookuptypes list */
    lookupTypesList: any;

    constructor(
        private eventManager: JhiEventManager,
        private lookUpCodeService: LookUpCodeService,
        private route: ActivatedRoute,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private $sessionStorage: SessionStorageService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        /**Data table Height based on browser screensize*/
        this.commonService.screensize();
        $(".split-example").css({'height': 'auto','min-height': (this.commonService.screensize().height - 161) + 'px'});

        /**Load lookup code detail by id */
        this.load();

        /* this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLookUpCodes(); */
    }

    load(){
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "look-up-code") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            this.getAllLookupTypes();

            if (params['id']) {
                this.lookUpCodeService.find(params['id']).subscribe((res) => {
                        this.lookUpCodeDetails = res;
                        // console.log('this.lookUpCodeDetails\n' + JSON.stringify(this.lookUpCodeDetails));
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            /**Focus on first element */
                            $('#lookUpCode').focus();
                        } else {
                            this.isViewOnly = true;
                        }
                    }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                /**Focus on first element */
                $('#lookUpCode').focus();
                /**Get Lookuptype from session*/
                if(this.$sessionStorage.retrieve('selcetedLookupType')){
                    this.lookUpCodeDetails.lookUpType = this.$sessionStorage.retrieve('selcetedLookupType')[0].id;
                }
            }
        });
    }

    /**Toggle Sidebar */
    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }

    /**Save/Update Lookup Code */
    saveLookupCode() {
        if (!this.nameExist)
            this.lookUpCodeService.checkLookUpCodeIsExist(this.lookUpCodeDetails.lookUpType, this.lookUpCodeDetails.lookUpCode, this.lookUpCodeDetails.id).subscribe(res => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.nameExist) {
                    if (this.lookUpCodeDetails.id) {
                        /* Update LookUpCode */
                        console.log('this.lookUpCodeDetails\n' + JSON.stringify(this.lookUpCodeDetails));
                        this.lookUpCodeService.update(this.lookUpCodeDetails).subscribe((res) => {
                            console.log('lookupcode updated\n' + JSON.stringify(res));
                            this.notificationsService.success('Success!', 'Lookup Code Successfully Updated');
                            this.navigateToDetails(res.id);
                        });
                    } else {
                        /**Create New LookUpCode */
                        this.lookUpCodeDetails.tenantId = this.UserData.tenantId;
                        this.lookUpCodeDetails.enableFlag = true;
                        this.lookUpCodeDetails.createdBy = this.UserData.id;
                        this.lookUpCodeDetails.creationDate = new Date();
                        this.lookUpCodeDetails.lastUpdatedBy = this.UserData.id;
                        this.lookUpCodeDetails.lastUpdatedDate = new Date();
                        console.log('this.lookUpCodeDetails\n' + JSON.stringify(this.lookUpCodeDetails));
                        this.lookUpCodeService.create(this.lookUpCodeDetails).subscribe((res) => {
                            console.log('New Lookup Code\n' + JSON.stringify(res));
                            this.notificationsService.success('Success!', 'New Lookup Code Successfully Created');
                            if (res.id) {
                                this.router.navigate(['/look-up-code', { outlets: { 'content': 'look-up-code-home' } }]);
                            }
                        });
                    }
                }
            });
    }

    /**Validation message function */
    validationMsg(){
        this.notificationsService.error('Warning!', 'Fill madatory fields');
    }

    /**Navigate to Details Page */
    navigateToDetails(id){
        let link: any = '';
        if (id) {
            link = ['/look-up-code', { outlets: { 'content': id + '/details' } }];
            if (this.isEdit) {
                this.isEdit = false;
            }
            if (this.isCreateNew) {
                this.isCreateNew = false;
            }
            this.isViewOnly = true;
            this.router.navigate(link);
        }else{
            this.notificationsService.error('Warning!', 'Could not redirect to details page');
        }
    }

    /** Get All Lookup Types List*/
    getAllLookupTypes(){
        this.lookUpCodeService.getAllLookupTypes().subscribe((res)=>{
            this.lookupTypesList = res;
        });
    }

    nameExist: string;
    isNameExist(type, code, id) {
        if (code)
        this.lookUpCodeService.checkLookUpCodeIsExist(type, code, id).subscribe(res => {
            this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
        });
    }


    

    /* load(id) {
        this.lookUpCodeService.find(id).subscribe((lookUpCode) => {
            this.lookUpCode = lookUpCode;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLookUpCodes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lookUpCodeListModification',
            (response) => this.load(this.lookUpCode.id)
        );
    } */
}
