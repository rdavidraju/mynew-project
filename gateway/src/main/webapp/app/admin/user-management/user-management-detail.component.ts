import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { Principal } from '../../shared';
import { User, UserService } from '../../shared';
import { Router } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { RolesService } from '../../entities/roles/roles.service';
import { FileUploader } from 'ng2-file-upload/ng2-file-upload';
import { SessionStorageService } from 'ng2-webstorage';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { ApprovalGroupMembersService } from '../../entities/approval-group-members/approval-group-members.service';
import { ApprovalGroupsService } from '../../entities/approval-groups/approval-groups.service';
import { NavBarService } from '../../layouts/navbar/navbar.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
const URL = '';

@Component({
    selector: 'jhi-user-mgmt-detail',
    templateUrl: './user-management-detail.component.html',
    animations: [
        trigger('flyInOut', [
            state('in', style({ transform: 'translateX(0)' })),
            transition('void => *', [
                style({ transform: 'translateX(100%)' }),
                animate(500)
            ])
        ]),
        trigger('enterAnimation', [
            transition(':enter', [
                style({ opacity: 0 }),
                animate('1000ms 100ms ease-in', style({ opacity: 1 }))
            ])
        ])
    ]
})
export class UserMgmtDetailComponent implements OnInit, OnDestroy {

    users = new User();
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    tagRoles: any = 'Yes';
    tagRoles1: any = 'No';
    allRoles: any = [];
    curUserRolesList = [];
    selectedRoles = [];
    unassignedRoles = [];
    display = false;
    userRoleAssnmt = [];
    displayTaggedRoles = false;
    hasBaseDropZoneOver = false;
    uploader: FileUploader = new FileUploader({ url: URL });
    languages: any[];
    userImg: any;
    dispImg = false;
    managerName: any;
    managerImage: any;
    tableViews = false;
    groupSwitchView = false;
    roleValue = false;
    selectedTab: any;
    selectedTab1: any;
    managersList: any = [];
    groupAssignmentDialog = false;
    groupsList: any[];
    selectedGroups: any[];
    dndgrp = false;
    groupUser: any[];
    managerialChain: any = [];
    fonts: any[] = [];
    selectedUserTab: any = 0;
    rolesProName = [];
    rolesGridDisplay = false;
    currentAccount: any;
    lastTheme: any;
    lastFont: any;
    tempUserRoles: any = [];
    tempVariable: any = [];
    duplicateEmail = false;
    emailValid = false;
    userAge: any;
    currentDate = new Date();
    maxDob = new Date(new Date().getFullYear() - 19, 11, 31);
    dndView = false;
    timeZones: any[];
    loadDocument = false;
    defRole: any;
    langSearch: any;
    tmZnSearch: any;
    private unsubscribe: Subject<void> = new Subject();

    constructor(
        private userService: UserService,
        private route: ActivatedRoute,
        private principal: Principal,
        private router: Router,
        private dateUtils: JhiDateUtils,
        public commonService: CommonService,
        private dialog: MdDialog,
        private rolesService: RolesService,
        private elementRef: ElementRef,
        private $sessionStorage: SessionStorageService,
        private approvalGroupMembersService: ApprovalGroupMembersService,
        private approvalGroupsService: ApprovalGroupsService,
        private navBarService: NavBarService
    ) {
        this.selectedGroups = [];
        this.groupUser = [];
        this.fonts = [
            { value: 'josefin-sans', label: 'Josefin Sans', fontFamily: 'josefinOption' },
            { value: 'artifika-serif', label: 'Artifika serif', fontFamily: 'artifikaOption' },
            { value: 'gentium-basic', label: 'Gentium Basic', fontFamily: 'gentiumOption' },
            { value: 'noto-serif', label: 'Noto Serif', fontFamily: 'notoOption' },
            { value: 'pt-serif', label: 'PT Serif', fontFamily: 'ptSerifOption' },
            { value: 'baumans', label: 'Baumans', fontFamily: 'baumansOption' },
            { value: 'play', label: 'Play', fontFamily: 'playOption' },
            { value: 'roboto', label: 'Roboto', fontFamily: 'robotoOption' },
            { value: 'roboto-condensed', label: 'Roboto Condensed', fontFamily: 'robotoCondensedOption' },
            { value: 'pt-sansnarrow', label: 'PT Sans Narrow', fontFamily: 'pTSansNarrowOption' },
            { value: 'exo-2', label: 'Exo 2', fontFamily: 'exo2Option' },
            { value: 'domine', label: 'Domine', fontFamily: 'domineOption' },
            { value: 'istok-web', label: 'Istok Web', fontFamily: 'istokWebOption' },
            { value: 'lato', label: 'Lato', fontFamily: 'latoOption' },
            { value: 'open-sans', label: 'Open Sans', fontFamily: 'openSansOption' }
        ];
        
        // Execute When font or theme change from user management
        this.navBarService.preferenceChangeVar$.takeUntil(this.unsubscribe).subscribe(() => {
            this.lastTheme = this.commonService.DYNAMIC_THEME;
        })
    }

    ngOnInit() {
        this.userRoleAssnmt = [];
        this.lastTheme = this.commonService.DYNAMIC_THEME;
        this.lastFont = this.commonService.DYNAMIC_FONT;
        this.getCurrentAccount();
        this.fetchUserDetails();
        this.userService.getTimezones().takeUntil(this.unsubscribe).subscribe((res) => this.timeZones = res,
            () => this.commonService.error('Warning!', 'Error occured while fetching time zones'));
        this.userService.getLanguages().takeUntil(this.unsubscribe).subscribe((res) => this.languages = res.languages,
            () => this.commonService.error('Warning!', 'Error occured while fetching languages'));
    }
    
    getCurrentAccount() {
        if (!this.currentAccount) {
            this.principal.identity().then((account) => {
                this.currentAccount = account;
            });
        }
    }
    
    onThemeChange(theme) {
        this.commonService.DYNAMIC_THEME = theme;
        $('body').removeClass(this.lastTheme);
        $('body').addClass(theme);
        this.lastTheme = theme;
        this.navBarService.preferenceChangeDetect();
    }
    
    onFontChange(font) {
        this.commonService.DYNAMIC_FONT = font;
        $('body').removeClass(this.lastFont);
        $('body').addClass(font);
        this.lastFont = font;
        this.navBarService.preferenceChangeDetect();
    }

    public onFileDrop(filelist: FileList): void {
        const reader = new FileReader();
        const image = this.elementRef.nativeElement.querySelector('#myImage');

        reader.onload = function(e: any) {
            const src = e.target.result;
            image.src = src;
        };
        reader.readAsDataURL(filelist[0]);
        const file: File = filelist[0];
        this.userImg = file;
        this.dispImg = true;
        if (!this.isCreateNew && this.userImg) {
            this.userService.uploadImg(this.userImg, this.users.id).takeUntil(this.unsubscribe).subscribe(
            () => { this.commonService.success('Success!', 'Image Updated Successfully') },
            () => { this.commonService.error('Warning!', 'Error occured while uploading image') });
        }
    }

    public fileOverBase(file: File): void {
        if (file) {
            this.hasBaseDropZoneOver = true;
        }
    }

    changeView(tab) {
        if (tab == 'roles') {
            this.tableViews = this.tableViews ? false : true;
        } else {
            this.groupSwitchView = this.groupSwitchView ? false : true;
        }
    }
    
    fetchUserDetails() {
        this.userService.usersBasedOnRole("DEFAULT_USER").takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.managersList = res;
        }, () => { this.commonService.error('Warning!', 'Error occured while fetching manager list') });
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            if (params['id']) {
                this.rolesGridDisplay = false;
                this.tempUserRoles = [];
                this.userService.getSelectedUserRolesDetails(params['id']).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.users = res;
                    console.log('this.users.startDate ' + this.users.startDate);
                    console.log('this.users.endDate ' + this.users.endDate);
                    this.users.startDate = this.dateUtils.convertDateTimeFromServer(this.users.startDate);
                    this.users.endDate = this.dateUtils.convertDateTimeFromServer(this.users.endDate);
                    console.log('this.users.startDate ' + this.users.startDate);
                    console.log('this.users.endDate ' + this.users.endDate);
                    setTimeout(() => {
                        if (this.users['image']) {
                            $('.userPicture').css({
                                'background-image': 'url(data:image/png;base64,' + this.users['image'] + ')',
                                'background-position': 'center',
                                'background-size': '161px'
                            });
                        } else {
                            $('.userPicture').attr('data-content', this.users['firstName'].charAt(0).toUpperCase());
                        }
                    }, 0);
                    if (this.users.dateOfBirth) {
                        this.getAge(this.users.dateOfBirth);
                    }
                    this.getGroups(this.users.id);
                    this.getManagerialChain(this.commonService.currentAccount.tenantIdForDisplay, this.users.id);
                    //this.managersList
                    this.managersList.forEach((element) => {
                        if (element.id == this.users.managerId) {
                            this.managerName = element.firstName + ' ' + element.lastName;
                            this.managerImage = element.image;
                        }
                    });
                    this.userRoleAssnmt = this.users.userRoleAssnmt;
                    this.users.userRoleAssnmt.forEach((element, i) => {
                        this.rolesProName.push(element.roleName);
                        if (i < 4) {
                            this.tempUserRoles.push(element);
                        } else {
                            this.tempVariable.push(element);
                        }
                    });
                    this.userRoleAssnmt.forEach((element) => {
                        element.startDate = this.dateUtils
                            .convertDateTimeFromServer(element.startDate);
                        element.endDate = this.dateUtils
                            .convertDateTimeFromServer(element.endDate);
                    });
                    this.curUserRolesList = this.userRoleAssnmt;
                    if (this.userRoleAssnmt != null) {
                        this.displayTaggedRoles = true;
                    }
                    this.getAllRolesList();
                    this.isCreateNew = false;
                    if (url.endsWith('edit')) {
                        this.isEdit = true;
                        this.loadDocument = true;
                    } else {
                        this.users.lastName = !this.users.lastName ? '-' : this.users.lastName;
                        this.users.endDate = !this.users.endDate ? '-' : this.users.endDate;
                        this.users.address = !this.users.address ? '-' : this.users.address;
                        this.isViewOnly = true;
                        this.loadDocument = true;
                    }
                }, () => { this.commonService.error('Warning!', 'Error occured while fetching user details') });
                const recentRoutes = this.$sessionStorage.retrieve('userSetting');
                if (recentRoutes == true) {
                    this.selectedTab = 2;
                    this.$sessionStorage.clear('userSetting');
                } else {
                    this.selectedTab = 1;
                }

            } else {
                this.users.startDate = new Date();
                this.users.organizationName = this.commonService.currentAccount['tenantName'];
                this.users.gender = "Male";
                this.isCreateNew = true;
                this.loadDocument = true;
                this.getAllRolesList();
            }
        });
    }
    pushRoles() {
        this.tempVariable.forEach((element, i) => {
            setTimeout(() => {
                this.tempUserRoles.push(element);
            }, 100 * (i + 1));
        });
    }
    
    // Function to fetch All roles list
    getAllRolesList() {
        this.unassignedRoles = [];
        this.rolesService.fetchAllRolesByTenantId().takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.allRoles = res;
            this.allRoles.forEach((role) => {
                if (role.activeInd == "TRUE") {
                    role.activeFlag = true;
                } else {
                    role.activeFlag = false;
                }
                role.roleId = role.id;

                //Get "DEFAULT_USER" role to add by default to new user
                if (this.isCreateNew) {
                    if (role.roleCode == 'DEFAULT_USER') {
                        this.defRole = role;
                    }
                }

                role.startDate = new Date();
                role.endDate = undefined;
                let occured = false;
                this.curUserRolesList.forEach((assignedRole) => {
                    if (role.id === assignedRole.roleId) {
                        occured = true;
                    }
                });
                if (occured) {
                } else {
                    this.unassignedRoles.push(role);
                }
            });
            if (this.isCreateNew && this.unassignedRoles.length > 0) {
                for (let i = 0; i < this.selectedRoles.length; i++) {
                    for (let j = 0; j < this.unassignedRoles.length; j++) {
                        if (this.selectedRoles[i].id == this.unassignedRoles[j].id) {
                            this.unassignedRoles.splice(j, 1);
                        }
                    }
                }
            }
        }, () => { this.commonService.error('Warning!', 'Error occured while fetching roles') });

    }

    openDialog(): void {
        const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
            width: '300px',
            data: { tagRoles: this.tagRoles, tagRoles1: this.tagRoles1 }
        });

        dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
            if (result == 'Yes') {
                this.unassignedRoles = [];
                this.getAllRolesList();
                this.selectedRoles = [];
                this.display = true;
            } else if (result == 'No') {
                this.saveUserDetails();
            }
        });
    }

    saveUserDetails() {
        let link: any = '';
        delete this.users.id;
        this.users['password'] = 'welcome';
        this.users.curUser = this.commonService.currentAccount.id;
        this.users.tenantId = this.commonService.currentAccount.tenantId;
        this.userService.userCreation(this.users).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.users = res;
            // START Assign groups if user created
            if (this.groupUser.length) {
                let obj = {};
                const arr = [];
                this.groupUser.forEach((element) => {
                    obj = {
                        'groupId': element.id,
                        'startDate': element.startDate,
                        'endDate': element.endDate,
                        'userId': this.users.id
                    }
                    arr.push(obj);
                });

                if (arr.length) {
                    this.approvalGroupMembersService.postAppGrpMemUser(arr).takeUntil(this.unsubscribe).subscribe(() => {},
                    () => { this.commonService.error('Error!', 'Failed to save groups'); });
                }
            }

            // END Assign groups if user created
            this.commonService.success('Success!','User created successfully');
            if (this.userImg) {
                this.userService.uploadImg(this.userImg, this.users.id).takeUntil(this.unsubscribe).subscribe(() => {
                    if (this.users.id) {
                        link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                        if (this.isEdit) {
                            this.isEdit = false;
                        }
                        if (this.isCreateNew) {
                            this.isCreateNew = false;
                        }
                        this.isViewOnly = true;
                        this.router.navigate(link);
                    }
                }, () => { this.commonService.error('Warning!', 'Error occured while saving user image') });
            } else {
                if (this.users.id) {
                    link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                    if (this.isEdit) {
                        this.isEdit = false;
                    }
                    if (this.isCreateNew) {
                        this.isCreateNew = false;
                    }
                    this.isViewOnly = true;
                    this.router.navigate(link);
                }
            }

        }, () => { this.commonService.error('Warning!', 'Error occured while saving user details') });
    }
    
    // Function to save users
    saveUser() {
        let link: any = '';
        if (this.users.id) {
            if(this.commonService.editUserFromTenant){
                this.users.tenantId = this.commonService.editUserFromTenantId;
                this.users.curUser = this.users.id;
            }else{
                this.users.curUser = this.commonService.currentAccount.id;
                this.users.tenantId = this.commonService.currentAccount.tenantId;
            }
            this.userService.userCreation(this.users).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.users = res;
                if (this.userImg) {
                    this.userService.uploadImg(this.userImg, this.users.id).takeUntil(this.unsubscribe).subscribe(() => {
                        this.selectedTab = 0;
                        this.commonService.success('Success!','User details updated successfully');
                        if (this.users.id) {
                            if(this.commonService.editUserFromTenant){
                                this.validateUser();
                                this.commonService.editUserFromTenant = false;
                            }
                            link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                            this.router.navigate(link);
                        }
                    }, () => { this.commonService.error('Warning!', 'Error occured while saving user image') });
                } else {
                    this.commonService.success('Success!','User details updated successfully');
                    this.selectedTab = 0;
                    if (this.users.id) {
                        if(this.commonService.editUserFromTenant){
                            this.validateUser();
                            this.commonService.editUserFromTenant = false;
                        }
                        link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                        this.router.navigate(link);
                    }
                }

            }, () => { this.commonService.error('Warning!', 'Error occured while saving user details') });
        } else {
            this.userRoleAssnmt.forEach((element) => {
                element['activeFlag'] = true;
            });
            this.users['userRoleAssignments'] = this.userRoleAssnmt;
            if (this.isCreateNew) {
                this.defRole.activeFlag = true;
                this.users.userRoleAssignments.unshift(this.defRole);
            }
            this.users['authorities'] = [
                {
                    "name": "ROLE_USER"
                }
            ];
            this.users['activated'] = true;
            this.users['login'] = this.users.firstName;
            this.users['langKey'] = 'en';
            if (this.userRoleAssnmt.length > 0) {
                delete this.users.id;
                this.users['password'] = 'welcome';
                this.users.curUser = this.commonService.currentAccount.id;
                this.users.tenantId = this.commonService.currentAccount.tenantId;
                this.userService.userCreation(this.users).takeUntil(this.unsubscribe).subscribe((res: any) => {
                    this.users = res;
                    // START Assign groups if user created
                    if (this.groupUser) {
                        let obj = {};
                        const arr = [];
                        this.groupUser.forEach((element) => {
                            obj = {
                                'groupId': element.id,
                                'startDate': element.startDate,
                                'endDate': element.endDate,
                                'userId': this.users.id
                            }
                            arr.push(obj);
                        });

                        if (arr) {
                            this.approvalGroupMembersService.postAppGrpMemUser(arr).takeUntil(this.unsubscribe).subscribe(() => { },
                            () => { this.commonService.error('Error!', 'Failed to save groups'); });
                        }
                    }

                    // END Assign groups if user created
                    this.selectedTab = 0;
                    this.commonService.success('Success!','User created successfully');

                    if (this.userImg) {
                        this.userService.uploadImg(this.userImg, this.users.id).takeUntil(this.unsubscribe).subscribe(() => {
                            if (this.users.id) {
                                link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                                if (this.isEdit) {
                                    this.isEdit = false;
                                }
                                if (this.isCreateNew) {
                                    this.isCreateNew = false;
                                }
                                this.isViewOnly = true;
                                this.router.navigate(link);
                            }
                        }, () => { this.commonService.error('Warning!', 'Error occured while saving user image') });
                    } else {
                        if (this.users.id) {
                            link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                            if (this.isEdit) {
                                this.isEdit = false;
                            }
                            if (this.isCreateNew) {
                                this.isCreateNew = false;
                            }
                            this.isViewOnly = true;
                            this.router.navigate(link);
                        }
                    }

                }, () => { this.commonService.error('Warning!', 'Error occured while saving user details') });
            } else {
                this.openDialog();
            }

        }
    }

    tagSelectedRoles() {
        this.userRoleAssnmt = [];
        this.selectedRoles.forEach((element) => {
            this.userRoleAssnmt.push(element);
        });
        this.displayTaggedRoles = true;
    }

    tagNewSelectedRoles() {
        this.selectedRoles.forEach((element) => {
            element['userId'] = this.users.id;
            element['activeFlag'] = true;
        });

        for (let sRoleIndex = 0; sRoleIndex < this.selectedRoles.length; sRoleIndex++) {
            if (this.selectedRoles[sRoleIndex]) {
                this.selectedRoles[sRoleIndex].id = null;
            }
        }

        this.userService.assignRolesToUser(this.selectedRoles).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.commonService.success('Success!','Role(s) assigned to user');
            this.selectedRoles = [];
            this.unassignedRoles = [];
            this.roleValue = true;
            this.fetchUserDetails();

        }, () => { this.commonService.error('Warning!', 'Error occured while assigning role(s) to user') });

    }

    // function to get assigned roles and functions for selected user
    getAssignedRolesList() {
        if (!this.isCreateNew) {
            this.userService.getListOfRoles(this.users.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.curUserRolesList = res;
            }, () => { this.commonService.error('Warning!', 'Error occured while fetching user\'s roles') });
        }
    }

    // function to delete role
    deleteColumnNew(ind) {
        this.userRoleAssnmt.splice(ind, 1);
    }

    deleteColumn(val) {
        this.userService.deleteRole(val).takeUntil(this.unsubscribe).subscribe(() => {
            this.commonService.success('Success!', 'Role "' + val.roleName + '" successfully deleted');
            this.roleValue = true;
            this.fetchUserDetails();
        }, () => { this.commonService.error('Warning!', 'Error occured while deleting role') });
    }

    /* function to update roles */
    updateUserRole(obj: any) {
        obj['userId'] = this.users.id;
        this.userService.updateRole(obj).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.commonService.success('Success!','Role "' + obj.roleName + '" successfully updated');
            this.roleValue = true;
            this.fetchUserDetails();
        }, () => { this.commonService.error('Warning!', 'Error occured while updating role') });
    }
    
    validateEmail(email, id) {
        this.regemail(email);
        if (this.emailValid == true) {
            this.userService.validateEmailId(email, id).takeUntil(this.unsubscribe).subscribe(
                (res) => { this.duplicateEmail = res.result != 'No Duplicates Found' ? true : false; },
                () => { this.commonService.error('Warning!', 'Error occured while validating email') });
        }
    }
    
    regemail(email) {
        const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        this.emailValid = re.test(email);
    }

    addRoles() {
        if (!this.isCreateNew) {
            this.selectedRoles = [];
            this.dndView = false;
        } else {
            this.selectedRoles = [];
            this.userRoleAssnmt.forEach((element) => {
                this.selectedRoles.push(element);
            });
        }
        this.getAllRolesList();
    }
    
    ngOnDestroy() {
        this.commonService.editUserFromTenant = false;
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    // List of Groups based on user id
    getGroups(userId) {
        this.approvalGroupMembersService.getAppGrpMemUserId(userId).takeUntil(this.unsubscribe).subscribe((res) => {
            this.groupUser = res;
            this.dateConvert(this.groupUser);
        },
        () => { this.commonService.error('Error!', 'Failed to fetch groups'); });
    }

    // Navigate to Groups Creation
    createGroup() {
        this.router.navigate(['/groups', { outlets: { 'content': ['groups-new'] } }]);
        return false;
    }

    // Assign Group
    assignGroup() {
        this.groupsList = [];
        this.approvalGroupsService.approvalgroupsForTenant().takeUntil(this.unsubscribe).subscribe((res) => {
            res.forEach((unassgrp) => {
                let occured = false;
                this.groupUser.forEach((assGrp) => {
                    if (this.isCreateNew) {
                        if (unassgrp.id === assGrp.id) {
                            occured = true;
                        }
                    } else {
                        if (unassgrp.id === assGrp.groupId) {
                            occured = true;
                        }
                    }
                });
                if (occured) {
                } else {
                    unassgrp.startDate = new Date();
                    unassgrp.endDate = null;
                    this.groupsList.push(unassgrp);
                }
            });
            this.selectedGroups = [];
            this.dndgrp = false;
            this.groupAssignmentDialog = true;
        }, () => { this.commonService.error('Warning!', 'Error occured while fetching groups') });
    }

    // Save Assigned Group based on creation and updation
    saveAssignedGrp() {
        if (this.isCreateNew == true) {
            this.groupAssignmentDialog = false;
            this.groupSwitchView = true;
            this.selectedGroups.forEach((element) => {
                this.groupUser.push(element);
            });
        } else {
            this.saveGrp();
        }
    }

    // Convert Date to prevent error - DatePIcker
    dateConvert(arr) {
        if (arr) {
            arr.forEach((element) => {
                element.startDate = this.dateUtils.convertDateTimeFromServer(element.startDate);
                element.endDate = this.dateUtils.convertDateTimeFromServer(element.endDate);
            });
            return arr;
        }
    }

    // Edit Group User
    editGrpUser(index) {
        let throwValidation: any = false;
        for (let i = 0; i < this.groupUser.length; i++) {
            if (this.groupUser[i].columnEdit == true) {
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.commonService.error('Warning!', 'Please save row before editing');
        } else {
            this.groupUser[index].columnEdit = true;
        }
    }

    // Update Group User
    updateGrpUser(grpUser) {
        const arr = [];
        arr.push(grpUser);
        this.approvalGroupMembersService.postAppGrpMemUser(arr).takeUntil(this.unsubscribe).subscribe(() => {
            this.commonService.success('Success!', 'Group details updated successfully');
            this.fetchUserDetails();
        },
        () => { this.commonService.error('Error!', 'Failed to update user'); });
    }

    // Cancel Goup User
    cancelEdit() {
        this.getGroups(this.users.id);
    }

    // Save Group
    saveGrp() {
        let obj = {};
        const arr = [];
        this.selectedGroups.forEach((element) => {
            obj = {
                'groupId': element.id,
                'startDate': element.startDate,
                'endDate': element.endDate,
                'userId': this.users.id
            }
            arr.push(obj);
        });

        if (arr && arr.length > 0) {
            this.approvalGroupMembersService.postAppGrpMemUser(arr).takeUntil(this.unsubscribe).subscribe(() => {
                this.groupAssignmentDialog = false;
                this.commonService.success('Success!', `${arr.length > 1 ? 'Groups has been assigned' : 'Group has been assigned'}`);
                this.fetchUserDetails();
            },
            () => {
                this.commonService.error('Error!', 'Failed to save groups');
            });
        }
    }

    // Delete Row
    deleteRow(i) {
        this.groupUser.splice(i, 1);
    }

    // Get Current User Managerial Chain
    getManagerialChain(tenantId, userId) {
        this.userService.getManagerialChain(tenantId, userId).takeUntil(this.unsubscribe).subscribe((res) => {
            this.managerialChain = res;
        }, () => { this.commonService.error('Warning!', 'Error occured while fetching managerial chain') });
    }
    
    // Function to get age
    getAge(val) {
        const today = new Date();
        const birthDate = new Date(val);
        const age = today.getFullYear() - birthDate.getFullYear()
        this.userAge = age > 0 ? age : undefined;
        const m = today.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
            this.userAge--;
        }
        return this.userAge;
    }

    // Function to delete group assigned to user
    deleteGrpUser(col) {
        this.approvalGroupMembersService.delete(col.id).takeUntil(this.unsubscribe).subscribe(() => {
            this.commonService.success('Success!', 'Group has been removed');
            this.fetchUserDetails();
        }, () => { this.commonService.error('Warning!', 'Error occured while removing group') });
    }

    validateUser() {
        this.userService.validateUser(this.users.id).takeUntil(this.unsubscribe).subscribe((res) => {
            if (res && res.status == 'Email Sent') {
                this.commonService.success('Success!', `An email was sent to "${this.users.email}"`);
            } else {
                this.commonService.error('Warning!', 'Error occured while sending an email.');
            }
        }, () => { this.commonService.error('Warning!', 'Error occured while sending an email.'); });
    }

}
@Component({
    selector: 'dialog-overview-example-dialog',
    templateUrl: 'dialog-overview-example-dialog.component.html',
})
export class DialogOverviewExampleDialog {
    constructor(
        public dialogRef: MdDialogRef<DialogOverviewExampleDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) { }

    onNoClick(): void {
        this.dialogRef.close();
    }


}