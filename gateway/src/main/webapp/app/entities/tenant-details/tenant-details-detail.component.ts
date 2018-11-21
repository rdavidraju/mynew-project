import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { TenantDetails } from './tenant-details.model';
import { TenantDetailsService } from './tenant-details.service';
import { Router } from '@angular/router';
import { NotificationsService } from 'angular2-notifications-lite';
import { DatePipe } from '@angular/common';
import { DomSanitizer } from '@angular/platform-browser';
import { CommonService } from '../common.service';
import { AnimationBuilder, style, animate, AnimationPlayer } from '@angular/animations';
import { UserService } from '../../shared/user/user.service';
import { JhiDateUtils } from 'ng-jhipster';
const countriesList = require('./countriesList.json');
const statesAllList = require('./statesAllList.json');
const citiesAllList = require('./citiesAllList.json');
import { routerAnimation, displayAnimation } from '../../shared/animations/routerAnimation';

@Component({
    selector: 'jhi-tenant-details-detail',
    templateUrl: './tenant-details-detail.component.html',
    animations: [routerAnimation, displayAnimation]
})
export class TenantDetailsDetailComponent implements OnInit, OnDestroy {

    tenantDetails = new TenantDetails();
    private subscription: Subscription;
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA = false;
    currentAccount: any;
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    columnEdit = false;
    tenantConfigModule = [];
    tenantConfigModuleList = [];
    tenantConfigModule1 = [];
    tempTenantId: any;
    hideButtonIcons = false;
    checkBox: boolean;
    tempContractNum: any;
    tenantImgUploadDial = false;
    countriesList: any;
    statesAllList: any;
    statesList: any;
    citiesAllList: any;
    citiesList: any;
    testCiti = false;
    stateIdTemp: any;
    usersListByTenant: any;
    changeSysAdminButton = false;
    tempTenantId1: any;
    @ViewChild('uploadLogoBtn') uploadLogoBtn;
    scalePlayer: AnimationPlayer;
    dispMin: any;
    dispMinDate = false;
    @ViewChild('ngxImgContainer') ngxImgContainer;
    logoType: any;
    domainExist: string;
    emailDuplicate: any;
    errorMsg: any = 'Invalid Email Id';
    showErrorBlock = false;
    logoReq = false;
    statuses = [{ code: true, value: 'Active' }, { code: false, value: 'Inactive' }];
    notificationMessage: any;
    countrySearch: any;
    loadDocument = false;

    constructor(
        private tenantDetailsService: TenantDetailsService,
        private route: ActivatedRoute,
        private router: Router,
        private notificationService: NotificationsService,
        private notificationsService: NotificationsService,
        private datePipe: DatePipe,
        private domSanitizer: DomSanitizer,
        private animationBuilder: AnimationBuilder,
        private userService: UserService,
        public commonService: CommonService,
        private dateUtils: JhiDateUtils,
    ) { }

    ngOnInit() {
        this.tenantDetailsService.getLookUpsModule('APP_MODULES').subscribe((res: any) => {
            res.forEach((element) => {
                element['modules'] = element.meaning;
            });
            this.tenantConfigModule1 = res;
        });
        this.countriesList = countriesList.countries;
        this.statesAllList = statesAllList.states;
        this.citiesAllList = citiesAllList.cities;
        this.fetchTenantDetails();
    }

    getStates(id) {
        //console.log('id ' + id);
        this.statesList = [];
        this.citiesList = [];
        this.statesAllList.forEach((element) => {
            if (id == element.country_id) {
                this.statesList.push(element);
            }
        });
    }

    getCities(id) {
        this.citiesList = [];
        this.citiesAllList.forEach((element) => {
            if (id == element.state_id) {
                this.citiesList.push(element);
            }
        });
    }

    fetchTenantDetails() {
        this.tenantConfigModule = [];
        this.subscription = this.route.params.subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (params['id']) {
                this.tenantDetailsService.find(params['id']).subscribe((res) => {
                    this.tenantDetails = res;
                    this.loadDocument = true;
                    this.tenantDetails.domainNamedis = this.tenantDetails.domainName;
                    if (this.tenantDetails.userDetails) {
                        this.tenantDetails.firstName = this.tenantDetails.userDetails.firstName;
                        this.tenantDetails.lastName = this.tenantDetails.userDetails.lastName;
                        this.tenantDetails.emailId = this.tenantDetails.userDetails.email;
                    }
                    this.userService.getUsersByTenantId().subscribe((ubtres) => {
                        this.usersListByTenant = ubtres;
                    });
                    this.statesList = [];
                    this.citiesList = [];
                    if (this.countriesList.length > 0) {
                        this.countriesList.forEach((element) => {
                            if (element.name == this.tenantDetails.country) {
                                if (this.statesAllList.length > 0) {
                                    this.statesAllList.forEach((element1) => {
                                        if (element1.country_id == element.id) {
                                            this.statesList.push(element1);
                                            this.testCiti = true;
                                        }
                                    });
                                    this.statesList.forEach((element3) => {
                                        if (element3.name == this.tenantDetails.state) {
                                            if (this.citiesAllList.length > 0) {
                                                this.citiesAllList.forEach((element1) => {
                                                    if (element1.state_id == element3.id) {
                                                        this.citiesList.push(element1);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }

                    this.tenantDetailsService.fetchTenantConfigModule(this.tenantDetails.id).subscribe(
                        (ftcmRes: any) => {
                            this.tenantConfigModule = ftcmRes;
                            //console.log('this.tenantConfigModule ' + JSON.stringify(this.tenantConfigModule));
                            this.tenantConfigModule.forEach((element) => {
                                element['tempMinDate'] = new Date();
                                element.startDate = this.dateUtils.convertDateTimeFromServer(element.startDate);
                                element.endDate = this.dateUtils.convertDateTimeFromServer(element.endDate);
                            });
                            //console.log('this.tenantConfigModule ' + JSON.stringify(this.tenantConfigModule));
                        });
                    this.isCreateNew = false;
                    if (url.endsWith('edit')) {
                        this.isEdit = true;
                        this.hideButtonIcons = true;
                        this.loadDocument = true;
                    } else {
                        this.isViewOnly = true;
                        this.tenantDetails.lastName = !this.tenantDetails.lastName ? '-' : this.tenantDetails.lastName;
                        this.tenantDetails.secondaryContact = !this.tenantDetails.secondaryContact ? '-' : this.tenantDetails.secondaryContact;
                        this.hideButtonIcons = false;
                    }
                });
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                this.tenantDetailsService.getLookUpsModule('APP_MODULES').subscribe((res: any) => {
                    res.forEach((element) => {
                        element['modules'] = element.meaning;
                        element['tempMinDate'] = new Date();
                    });
                    this.tenantConfigModule = res;
                    this.tenantConfigModule.forEach((element) => {
                        if (element.lookUpCode == 'DATA_EXTRACTION' || element.lookUpCode == 'DATA_TRANSFORMATION') {
                            element.startDate = new Date();
                            element['mandatory'] = true;
                            element['checkBox'] = true;
                        }
                    });
                });
                this.loadDocument = true;
            }

        });

    }

    load(id) {
        this.tenantDetailsService.find(id).subscribe((tenantDetails) => {
            this.tenantDetails = tenantDetails;
        });
    }

    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    saveTenantDetails() {
        let name: any;
        if (!this.domainExist) {
            name = this.isCreateNew ? `${this.tenantDetails.domainNamedis}.glbyte.com` : this.tenantDetails.domainNamedis;
        }
        if (name) {
            this.tenantDetailsService.checkDomainIsExist(name, this.tenantDetails.id).subscribe((res) => {
                this.domainExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.domainExist) {
                    if (!this.emailDuplicate && this.isCreateNew) {
                        this.tenantDetailsService.isEmailDuplicate(this.tenantDetails.emailId, this.tenantDetails.id).subscribe(
                            (isEDRes) => {
                                this.emailDuplicate = isEDRes.result != 'No Duplicates Found' ? isEDRes.result : undefined;
                                if (!this.emailDuplicate) {
                                    this.saveTenant();
                                }
                            }, () => { this.notificationService.error('Warning!', 'Error Occured'); }
                        );
                    } else if (!this.isCreateNew) {
                        this.saveTenant();
                    }
                }
            }, () => { this.notificationService.error('Warning!', 'Error Occured') });
        }
    }

    saveTenant() {
        let link: any = '';
        if (!this.tenantDetails.tenantSubLogo || !this.tenantDetails.tenantSubLogo) {
            this.notificationService.error('Warning!', 'Logo is mandatory');
            const scaleAnimation = this.buildScaleAnimation();
            this.scalePlayer = scaleAnimation.create(this.uploadLogoBtn._elementRef.nativeElement);
            this.scalePlayer.play();
            return;
        }
        if (this.tenantDetails.id) {
            this.tenantDetails.domainName = this.tenantDetails.domainNamedis;
            this.tenantDetailsService.update(this.tenantDetails).subscribe((res: any) => {
                this.commonService.success('Success!', 'Tenant Details Successfully Updated');
                link = ['/tenant-details', { outlets: { 'content': res.id + '/details' } }];
                this.router.navigate(link);
            });
        } else {
            this.tenantDetails.domainName = `${this.tenantDetails.domainNamedis}.glbyte.com`;
            const obj = {
                "firstName": this.tenantDetails.firstName,
                "lastName": this.tenantDetails.lastName,
                "email": this.tenantDetails.emailId,
                "langKey": "en",
                "password": "welcome",
                "authorities": [{ "name": "ROLE_ADMIN" }, { "name": "ROLE_USER" }]
            }
            this.tenantDetails['conDetails'] = obj;
            this.tenantDetailsService.createNewTenant(this.tenantDetails).subscribe((res: any) => {
                this.tempTenantId1 = res.tenantId;
                this.tenantConfigModule.forEach((element) => {
                    if (element.checkBox == true) {
                        element.tenantId = res.id;
                        element['enabledFlag'] = true;
                        element['modules'] = element.lookUpCode;
                        element['contractNum'] = this.tempContractNum;
                        delete element.id;
                        delete element.lookUpCode;
                        delete element.lookUpType;
                        delete element.description;
                        delete element.activeStartDate;
                        delete element.activeEndDate;
                        delete element.secureAttribute1;
                        delete element.secureAttribute2;
                        delete element.secureAttribute3;
                        delete element.createdBy;
                        delete element.creationDate;
                        delete element.lastUpdatedBy;
                        delete element.lastUpdatedDate;
                        delete element.checkBox;
                        this.tenantConfigModuleList.push(element);
                    }
                });
                this.tenantDetailsService.postTenantConfig(this.tenantConfigModuleList).subscribe(() => {
                    this.commonService.success('Success!', 'Tenant Successfully Created');
                    link = ['/tenant-details', { outlets: { 'content': this.tempTenantId1 + '/details' } }];
                    this.router.navigate(link);
                });

            });
        }
    }

    /* Function to add modules */
    addModule() {
        const newLine = {
            "columnEdit": true
        };
        this.tenantConfigModule.push(newLine);
    }

    displayColumns(col, i) {
        this.dispMinDate = false;
        this.tenantConfigModule[i]['tempMinDate'] = null;
        this.tenantConfigModule[i].startDate = null;
        let count = 0;
        this.tenantConfigModule.forEach((element) => {
            if (element.purpose == col.meaning) {
                count++;
                const date = new Date(element.endDate);
                this.tenantConfigModule[i].startDate = new Date(date.setDate(date.getDate() + 1));
                this.tenantConfigModule[i]['tempMinDate'] = this.tenantConfigModule[i].startDate;
                this.dispMinDate = true;
            }
            if (this.dispMinDate == false) {
                this.tenantConfigModule[i]['tempMinDate'] = new Date();
            }
        });
    }

    addDateEvent(ind) {
        if (!this.isCreateNew) {
            for (let i = 0; i < this.tenantConfigModule.length; i++) {
                if (i != ind && this.tenantConfigModule[i].modules == this.tenantConfigModule[ind].modules) {
                    if (this.datePipe.transform(new Date(this.tenantConfigModule[ind].startDate), 'yyyy-MM-dd') < this.tenantConfigModule[i].endDate) {
                        this.notificationService.error('Warning!', 'Start Date should be greater than "' + this.tenantConfigModule[i].endDate + '"');
                        this.tenantConfigModule[ind].startDate = null;
                    } else { }
                }
            }
        }
    }

    /* Function to cancel changes */
    cancelChanges(ind) {
        this.fetchTenantDetails();
    }

    /* function to update tenantconfigmodule */
    updateTenantConfig(obj: any) {
        if (obj.id) {
            this.tenantDetailsService.updateTenantConfig(obj).subscribe(() => {
                this.commonService.success(
                    'Success!',
                    'Successfully "' + obj.modules + '" details updated'
                )
                this.fetchTenantDetails();
            },
                (res: Response) => {
                    this.onError('Some Thing Went Wrong');
                });
        } else {
            obj['enabledFlag'] = true;
            this.tenantDetailsService.postSingleTenantConfig(obj, this.tenantDetails.id).subscribe(() => {
                this.commonService.success(
                    'Success!',
                    'Successfully "' + obj.modules + '" Created'
                )
                this.fetchTenantDetails();
            },
                (res: Response) => {
                    this.onError('Some Thing Went Wrong');
                });
        }
    }

    /* Function to delete tenant config */
    deleteTenantConfig(obj: any) {
        this.tenantDetailsService.deleteTenantConfig(obj.id).subscribe((res: any) => {
            this.commonService.success(
                'Success!',
                'Successfully "' + obj.modules + '" Deleted'
            )
            this.fetchTenantDetails();
        });
    }

    testEdit(i) {
        this.tenantConfigModule[i]['tempMinDate'] = this.tenantConfigModule[i].startDate;
    }


    onTenantLogoUpload(evt) {
        if (this.logoType == 'logo') {
            const subLogo: any = this.sanitizeUrl(evt);
            if (subLogo) {
                this.tenantDetails.tenantSubLogo = subLogo.changingThisBreaksApplicationSecurity.split('data:image/jpeg;base64,')[1];
            }
        } else {
            const fullLogo: any = this.sanitizeUrl(evt);
            if (fullLogo) {
                this.tenantDetails.tenantLogo = fullLogo.changingThisBreaksApplicationSecurity.split('data:image/jpeg;base64,')[1];
            }
        }
    }

    onReset() {
        if (this.logoType == 'logo') {
            this.tenantDetails.tenantSubLogo = undefined;
        } else {
            this.tenantDetails.tenantLogo = undefined;
        }
    }

    openUploadDialog(logoType) {
        const clearBtn = this.ngxImgContainer.nativeElement.querySelector('button.ngx-img-clear');
        this.logoType = logoType;
        if (clearBtn) {
            clearBtn.click();
        }
        this.tenantImgUploadDial = true;
        setTimeout(() => {
            const uploadBtn = this.ngxImgContainer.nativeElement.querySelector('input[type="file"]');
            if (uploadBtn) {
                uploadBtn.click();
            }
        }, 50);
    }

    sanitizeUrl(url) {
        return this.domSanitizer.bypassSecurityTrustUrl(url);
    }

    buildScaleAnimation() {
        if (this.scalePlayer) {
            this.scalePlayer.destroy();
        }
        const scaleAnimation = this.animationBuilder.build([
            style({ border: '1px solid red', transform: 'scale(1.1)' }),
            animate('0.5s ease-in-out')
        ]);
        return scaleAnimation;
    }

    isDomainNameExist(name, id) {
        if (name) {
            name = this.isCreateNew ? `${name}.glbyte.com` : name;
            this.tenantDetailsService.checkDomainIsExist(name, id).subscribe((res) => {
                this.domainExist = res.result != 'No Duplicates Found' ? res.result : undefined;
            });
        }
    }

    addStrtDate(col) {
        col.startDate = new Date();
        col.mandatory = true;
    }

    checkEmailDuplicate(email, id) {
        if (email) {
            this.tenantDetailsService.isEmailDuplicate(email, id).subscribe(
                (res) => { this.emailDuplicate = res.result != 'No Duplicates Found' ? res.result : undefined; },
                () => { this.notificationService.error('Warning!', 'Error Occured'); }
            )
        }
    }

    validateUser() {
        this.userService.validateUser(this.tenantDetails['userDetails'][0]['id']).subscribe((res) => {
            if (res && res.status == 'Email Sent') {
                this.notificationService.success('Success!', `An email was sent to "${this.tenantDetails['userDetails'][0].email}"`);
            } else {
                this.notificationService.error('Warning!', 'Error occured while sending an email.');
            }
        }, () => {
            this.notificationService.error('Warning!', 'Error occured while sending an email.');
        });
    }

    /* Function to change system admin */
    changeSysAdmin() {
        this.tenantDetails['adminDetails'][0]['userId'] = this.tenantDetails.userDetails.id;
        this.tenantDetailsService.updateSystemAdmin(this.tenantDetails['adminDetails'][0], this.tenantDetails.userDetails.id).subscribe((res: any) => {
            this.changeSysAdminButton = false;
            this.commonService.success(
                'Success!',
                'System Admin Updated Successfully'
            )
            this.fetchTenantDetails();
        });
    }

    testForm(f) {
        this.showErrorBlock = true;
        this.notificationMessage = "Some Thing Went Wrong";
        //console.log(f);/* f._submitted + f.ngSubmit + f.form */
    }

    private onError(error) {
        this.commonService.showErrorBlock = true;
        this.notificationMessage = error;
       // console.log('this.notificationMessage ' + this.notificationMessage);
    }
}
