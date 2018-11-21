import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { UserService } from '../../shared';
import { Router } from '@angular/router';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { Roles } from './roles.model';
import { Functionality } from '../functionality/functionality.model';
import { FunctionalityService } from '../functionality/functionality.service';
import { RolesService } from './roles.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'roles-modal',
    templateUrl: 'roles-modal.html',
})
export class RolesModalDialog {
    constructor(
        public dialogRef: MdDialogRef<RolesModalDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any
        ) { }

    onNoClick(): void {
        this.dialogRef.close();
    }
}


@Component({
    selector: 'jhi-roles-detail',
    templateUrl: './roles-detail.component.html'
})
export class RolesDetailComponent implements OnInit, OnDestroy {

    roles = new Roles();
    private unsubscribe: Subject<void> = new Subject();
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    allFunctionalities: any;
    allUsers: any;
    functionalityTagged = [];
    unassignedFunctionalities = [];
    unassignedUsers = [];
    display = false;
    displayusers = false;
    yesFunction: any = 'YesFunction';
    tagUser: any = 'NoSave';
    yesUser: any = 'YesUser';
    curRoleFuncList = [];
    curRoleUsersList = [];
    functionalitiesSelected = [];
    usersselected = [];
    selectedFunctionalities = [];
    selectedUsers = [];
    displayTaggedFunctionalities = false;
    displayTaggedUsers = false;
    paramsroleId: any
    isVisibleA = false;
    selectedTab:any;
    moreUsers:any = false;
    dndUsers:any = false;
    moreFunc:any = false;
    dndFunc:any = false;
    hideSaveBtn = false;
    isStChanged = false;
    nameExist: string;
    codeExist: string;
    statuses = [{code: true,value: 'Active'},{code: false,value: 'Inactive'}];
    currentDate = new Date();

    constructor(
        private userService: UserService,
        private router: Router,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private rolesService: RolesService,
        private route: ActivatedRoute,
        public dialog: MdDialog,
        private functionalityService: FunctionalityService,
    ) {}

    ngOnInit() {
        this.getAllRolesNTaggedFuncNUsers();
    }

    // Get All Roles, Tagged Functionalities and Users Assigned
    getAllRolesNTaggedFuncNUsers(){
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map((segment) => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            this.paramsroleId = params['id'];

            if (params['id']) {
                 this.rolesService.getRoleFuncNUserRoleByRoleId(params['id']).takeUntil(this.unsubscribe).subscribe(
                    (res) => {
                        this.roles = res;
                        this.curRoleFuncList = [];
                        this.curRoleFuncList = this.roles.roleFncs;
                        for (let i = 0; i < this.curRoleFuncList.length; i++) {
                            this.curRoleFuncList[i].startDate = this.dateUtils.convertDateTimeFromServer(this.curRoleFuncList[i].startDate);
                            this.curRoleFuncList[i].endDate = this.dateUtils.convertDateTimeFromServer(this.curRoleFuncList[i].endDate);
                        }
                        this.curRoleUsersList = [];
                        this.curRoleUsersList = this.roles.usrRol;
                        for (let j = 0; j < this.curRoleUsersList.length; j++) {
                            this.curRoleUsersList[j].startDate = this.dateUtils.convertDateTimeFromServer(this.curRoleUsersList[j].startDate);
                            this.curRoleUsersList[j].endDate = this.dateUtils.convertDateTimeFromServer(this.curRoleUsersList[j].endDate);
                        }
                        this.roles.startDate = this.dateUtils.convertDateTimeFromServer(this.roles.startDate);
                        this.roles.endDate = this.dateUtils.convertDateTimeFromServer(this.roles.endDate);
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            $('#roleCode').focus();
                        } else {
                            this.roles.roleDescription = !this.roles.roleDescription ? '-' : this.roles.roleDescription;
                            this.roles.endDate = !this.roles.endDate ? '-' : this.roles.endDate;
                            this.isViewOnly = true;
                        }
                     }
                );
            } else {
                this.roles.startDate = new Date();

                this.isVisibleA = true;
                this.isCreateNew = true;
                // Focus on Element
                $('#roleCode').focus();
            }
        });
    }
    
    // Function to save role
    saveRole() {
        if (!this.nameExist) {
            this.rolesService.checkRoleIsExist(this.roles.roleName, 'name' ,this.roles.id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;
                if (!this.nameExist) {
                    this.hideSaveBtn = true;
                    let link: any = '';
                    if (this.roles.id == undefined || null) {
                        //If Condition - Create New Role
                        if (this.functionalitiesSelected.length > 0) {
                            //If Functionalities Tagged To Role//
                            if (this.usersselected.length > 0) {
                                //If Users Tagged To Role
                                this.saveRoleWithFuncandUser();
                            } else {
                                this.openDialog();
                            }
                        } else {
                            //Else No Functionalities Tagged To Role//
                            this.openDialog();
                        }

                    } else {
                        this.roles.roleFncs = [];
                        this.roles.usrRol = [];
                        this.rolesService.updateRole(this.roles).takeUntil(this.unsubscribe).subscribe(() => {
                            this.hideSaveBtn = false;
                            this.commonService.success('Success!','Role Successfully Updated');
                            link = ['/roles', { outlets: { 'content': this.paramsroleId + '/details' } }];
                            if (this.isEdit) {
                                this.isEdit = false;
                            }
                            if (this.isCreateNew) {
                                this.isCreateNew = false;
                            }
                            this.isViewOnly = true;
                            this.router.navigate(link);
                        });
                    }
                }
            });
        }
    }
    /* Function to display validition message */
    validationMsg() {
        this.commonService.error('Warning!','Fill Mandatory Fields');
    }

    /** Get All Functionalities*/
    getAllFunctionalities() {
        this.unassignedFunctionalities = [];
        this.selectedFunctionalities = [];
        this.functionalityService.getFuncByTenantId().takeUntil(this.unsubscribe).subscribe((res: Functionality) => {
                this.allFunctionalities = res
                //Filter Assigned and Unassigned Functionalities
                this.allFunctionalities.forEach((func) => {
                    func.startDate = this.dateUtils.convertDateTimeFromServer(func.startDate);
                    func.endDate = this.dateUtils.convertDateTimeFromServer(func.endDate);
                    let occured = false;
                    this.curRoleFuncList.forEach((assignedFunc) => {
                        if (func.id === assignedFunc.functionId) {
                            occured = true;
                        }
                    });
                    if (!occured) {
                        this.unassignedFunctionalities.push(func);
                    }
                });
                if (this.unassignedFunctionalities.length) this.display = true;
                else this.commonService.info('Info', 'No functionalities to assign');

            });
    }

    // Get All Users
    getAllUsers() {
        this.selectedUsers = [];
        this.unassignedUsers = [];
        this.userService.getUsersByTenantId().takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.allUsers = res;
            // Filter Assigned and Unassigned Users
            this.allUsers.forEach((users) => {
                users.startDate = this.dateUtils.convertDateTimeFromServer(users.startDate);
                users.endDate = this.dateUtils.convertDateTimeFromServer(users.endDate);
                let occured = false;
                this.curRoleUsersList.forEach((assignedUsers) => {
                    if (users.id === assignedUsers.userId) {
                        occured = true;
                    }
                });
                if (!occured) {
                    this.unassignedUsers.push(users);
                }
            });
            if(this.unassignedUsers.length) this.displayusers = true;
            else this.commonService.info('Info', 'No users to assign');
        });
    }

    openDialog(): void {
        if(this.functionalitiesSelected.length > 0){
            const dialogRef = this.dialog.open(RolesModalDialog, {
                width: '400px',
                data: { tagUserM: this.tagUser, tagUser1M: this.yesUser}
            });
            dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
                this.hideSaveBtn = false;
                if (result == 'YesUser') {
                    // Open Dialog - Show Drag and Drop
                    this.unassignedFunctionalities = [];
                    this.selectedFunctionalities=[];
                    this.tagUsers();
                    this.displayusers = true;
                } else if(result == 'NoSave'){
                    // Close Dialog - Save Role With Functionality Only
                    this.saveRoleWithFunc();
                }
            });
        }else{
            const dialogRef = this.dialog.open(RolesModalDialog, {
                width: '400px',
                data: { tagUserM: this.tagUser, tagFunc1M: this.yesFunction}
            });
            dialogRef.afterClosed().takeUntil(this.unsubscribe).subscribe((result) => {
                this.hideSaveBtn = false;
                if (result == 'YesFunction') {
                    // Open Dialog - Show Drag and Drop
                    this.unassignedUsers = [];
                    this.selectedUsers=[];
                    this.tagFunc();
                    this.display = true;
                } else if(result == 'NoSave'){
                    // Close Dialog - Save Role
                    if(this.selectedUsers.length > 0){
                        this.saveRoleWithFuncandUser()
                    }else{
                        this.saveRoleOnly();
                    }
                }
            });
        }
    }

    //Empty selected func and get all func
    tagFunc(){
        if(this.isViewOnly){
            this.dndFunc = false;
            this.moreFunc = false;
        }
        this.getAllFunctionalities();
    }

    //Empty selected users and get all users
    tagUsers(){
        if (this.isViewOnly) {
            this.dndUsers = false;
            this.moreUsers = false;
        }
        this.getAllUsers();
    }

    saveRoleOnly(){
        let link: any = '';
        this.roles.activeInd = true;
        this.roles.tenantId = this.commonService.currentAccount.tenantId;
        this.roles.roleDesc = this.roles.roleDescription;
        this.rolesService.create(this.roles).takeUntil(this.unsubscribe).subscribe((roles) => {
            this.commonService.success('Success!','Role Successfully Created');
            if (roles.id) {
                link = ['/roles', { outlets: { 'content': roles.id + '/details' } }];
                if (this.isEdit) {
                    this.isEdit = false;
                }
                if (this.isCreateNew) {
                    this.isCreateNew = false;
                }
                this.isViewOnly = true;
                this.router.navigate(link);
            }
        });
    }

    saveRoleWithFunc(){
        let link: any = '';
        this.roles.activeInd = true;
        this.roles.tenantId = this.commonService.currentAccount.tenantId;
        this.roles.roleFncs = this.functionalitiesSelected;
        for (let i = 0; i < this.functionalitiesSelected.length; i++) {
            this.roles.roleFncs[i].functionId = this.functionalitiesSelected[i].id;
            this.roles.roleFncs[i].activeFlag = true;
            this.roles.roleFncs[i].assignedBy = this.commonService.currentAccount.id;
        }
        this.rolesService.postRoleFuncNUser(this.roles).takeUntil(this.unsubscribe).subscribe((roles) => {
            this.commonService.success('Success!','Role Successfully Created');
            if (roles.id) {
                link = ['/roles', { outlets: { 'content': roles.id + '/details' } }];
                if (this.isEdit) {
                    this.isEdit = false;
                }
                if (this.isCreateNew) {
                    this.isCreateNew = false;
                }
                this.isViewOnly = true;
                this.router.navigate(link);
            }

        });
    }

    saveRoleWithFuncandUser(){
        let link: any = '';
        this.roles.activeInd = true;
        this.roles.tenantId = this.commonService.currentAccount.tenantId;
        this.roles.usrRol = this.usersselected;
        this.roles.roleFncs = this.functionalitiesSelected;
        for (let i = 0; i < this.functionalitiesSelected.length; i++) {
            this.roles.roleFncs[i].functionId = this.functionalitiesSelected[i].id;
            this.roles.roleFncs[i].activeFlag = true;
            this.roles.roleFncs[i].assignedBy = this.commonService.currentAccount.id;
        }
        for (let j = 0; j < this.usersselected.length; j++) {
            this.roles.usrRol[j].userId = this.usersselected[j].id;
            this.roles.usrRol[j].activeFlag = true;
            this.roles.usrRol[j].assignedBy = this.commonService.currentAccount.id;
        }
        this.rolesService.postRoleFuncNUser(this.roles).takeUntil(this.unsubscribe).subscribe((roles) => {
            this.hideSaveBtn = false;
            this.commonService.success('Success!','Role Successfully Created');
            if (roles.id) {
                link = ['/roles', { outlets: { 'content': roles.id + '/details' } }];
                if (this.isEdit) {
                    this.isEdit = false;
                }
                if (this.isCreateNew) {
                    this.isCreateNew = false;
                }
                this.isViewOnly = true;
                this.router.navigate(link);
            }

        });
    }

    // Confirm After Selecting Functionalities
    confirmFunctionalities(){
        this.functionalitiesSelected = [];
        this.selectedFunctionalities.forEach((element) => {
            element.startDate = new Date();
            element.endDate = undefined;
            this.functionalitiesSelected.push(element);
        });
        if(this.isCreateNew == true){
            this.displayTaggedFunctionalities = true;
            this.selectedTab = 0;
        }
    }

    // Confirm After Selecting Functionalities//
    confirmUsers(){
        this.usersselected = [];
        this.selectedUsers.forEach((element) => {
            element.startDate = new Date();
            element.endDate = undefined;
            this.usersselected.push(element);
        });
        if(this.isCreateNew == true){
            this.displayTaggedUsers = true;
            this.selectedTab = 1;
        }
    }


    // Cancel Row Edit
    cancelEdit(){
        this.getAllRolesNTaggedFuncNUsers();
    }

    // Save Role's Tagged Functionality
    updateRoleFunc(roleFunc){
        if(roleFunc.startDate != null){
            this.rolesService.updateRoleFunc(roleFunc).takeUntil(this.unsubscribe).subscribe((roles)=>{
                this.commonService.success('Success!','Tagged Functionality successfully updated');
                this.getAllRolesNTaggedFuncNUsers();
            });
        }else{
            this.commonService.error('Warning!', 'Fill mandatory fields');
        }
    }

    // Save Role's Tagged User
    updateRoleUser(roleUser){
        if(roleUser.startDate != null){
            this.rolesService.updateRoleUser(roleUser).takeUntil(this.unsubscribe).subscribe((roles)=>{
                this.commonService.success('Success!','Tagged User successfully updated');
                this.getAllRolesNTaggedFuncNUsers();
            });
        }else{
            this.commonService.error('Warning!', 'Fill mandatory fields');
        }
    }

    // Delete Role's Functionality
    deleteRoleFunc(roleFunc){
        this.rolesService.deleteRoleFunc(roleFunc.functionId, roleFunc.roleId).takeUntil(this.unsubscribe).subscribe(()=>{
            this.commonService.success('Success!','Tagged Functionality successfully deleted');
            this.getAllRolesNTaggedFuncNUsers();
        });
    }
    
    // Delete Role's User
    deleteRoleUser(roleUser){
        this.rolesService.deleteRoleUser(roleUser.userId, roleUser.roleId).takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.commonService.success('Success!','Tagged User successfully deleted');
            this.getAllRolesNTaggedFuncNUsers();
        });
    }

    addMoreFunc(){
        this.rolesService.postTagFuncToRole(this.functionalitiesSelected, this.roles.id).takeUntil(this.unsubscribe).subscribe((res: any)=>{
            this.commonService.success('Success!','New Functionalities Tagged to role successfully');
            this.getAllRolesNTaggedFuncNUsers();
        });
        this.display = false;
    }

    addMoreUsers() {
        this.rolesService.postTagUsersToRole(this.usersselected, this.roles.id).takeUntil(this.unsubscribe).subscribe(() => {
            this.commonService.success('Success!', 'New Users Tagged to role successfully');
            this.getAllRolesNTaggedFuncNUsers();
        });
        this.displayusers = false;
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    isNameExist(name, type, id) {
        if (name) {
            this.rolesService.checkRoleIsExist(name, type, id).takeUntil(this.unsubscribe).subscribe((res) => {
                if (type == 'name') {
                this.nameExist = res.result != 'No Duplicates Found' ? res.result : undefined;   
                } else {
                this.codeExist = res.result != 'No Duplicates Found' ? res.result : undefined;   
                }
            });
        }
    }

    openFuncAssingDialog() {
        if (this.unassignedFunctionalities.length) {
            this.display = true;
        } else {
            this.commonService.info('Info!', 'No functionalities to assign');
        }
    }

    openUserAssingDialog() {
        if (this.unassignedUsers.length) {
            this.displayusers = true;
        } else {
            this.commonService.info('Info!', 'No Users to assign');
        }
    }

}
