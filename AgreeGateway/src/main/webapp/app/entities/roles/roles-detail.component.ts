import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService  } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { User, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { CommonService } from '../../entities/common.service';
import { RolesBreadCrumbTitles } from './roles.model';
import { Roles } from './roles.model';
import { Functionality } from '../functionality/functionality.model';
import { FunctionalityService } from '../functionality/functionality.service';
import { RolesService } from './roles.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { SessionStorageService } from 'ng2-webstorage';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'roles-modal',
    templateUrl: 'roles-modal.html',
})
export class RolesModalDialog {
    constructor(
        public dialogRef: MdDialogRef<RolesModalDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) { }

    onNoClick(): void {
        this.dialogRef.close();
    }
}


@Component({
    selector: 'jhi-roles-detail',
    templateUrl: './roles-detail.component.html'
})
export class RolesDetailComponent implements OnInit, OnDestroy {
    private UserData = this.$sessionStorage.retrieve('userData');
    roles = new Roles();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    allFunctionalities: any;
    allUsers: any;
    functionalityTagged = [];
    unassignedFunctionalities = [];
    unassignedUsers = [];
    display: boolean = false;
    displayusers: boolean = false;
    yesFunction: any = 'YesFunction';
    tagUser: any = 'NoSave';
    yesUser: any = 'YesUser';
    curRoleFuncList = [];
    curRoleUsersList = [];
    functionalitiesSelected = [];
    usersselected = [];
    selectedFunctionalities = [];
    selectedUsers = [];
    displayTaggedFunctionalities: boolean = false;
    displayTaggedUsers: boolean = false;
    paramsroleId: any
    isVisibleA: boolean = false;
    selectedTab:any;
    moreUsers:any = false;
    dndUsers:any = false;
    moreFunc:any = false;
    dndFunc:any = false;
    hideSaveBtn:boolean = false;

    constructor(
        private userService: UserService,
        private parseLinks: JhiParseLinks,
        private paginationUtil: JhiPaginationUtil,
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private eventManager: JhiEventManager,
        private rolesService: RolesService,
        private route: ActivatedRoute,
        public dialog: MdDialog,
        private functionalityService: FunctionalityService,
        private $sessionStorage: SessionStorageService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 161) + 'px'
        });

        this.getAllRolesNTaggedFuncNUsers();
    }

    /** Get All Roles, Tagged Functionalities and Users Assigned*/
    getAllRolesNTaggedFuncNUsers(){
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            this.paramsroleId = params['id'];
            if (this.presentPath == "roles") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {
                 this.rolesService.getRoleFuncNUserRoleByRoleId(params['id']).subscribe(
                    (res) => {
                        this.roles = res;
                        console.log('this.roles: ' + JSON.stringify(this.roles));
                        this.curRoleFuncList = [];
                        this.curRoleFuncList = this.roles.roleFncs;
                        for (var i = 0; i < this.curRoleFuncList.length; i++) {
                            this.curRoleFuncList[i].startDate = this.dateUtils.convertLocalDateFromServer(this.curRoleFuncList[i].startDate);
                            this.curRoleFuncList[i].endDate = this.dateUtils.convertLocalDateFromServer(this.curRoleFuncList[i].endDate);
                        }
                        this.curRoleUsersList = [];
                        this.curRoleUsersList = this.roles.usrRol;
                        for (var j = 0; j < this.curRoleUsersList.length; j++) {
                            this.curRoleUsersList[j].startDate = this.dateUtils.convertLocalDateFromServer(this.curRoleUsersList[j].startDate);
                            this.curRoleUsersList[j].endDate = this.dateUtils.convertLocalDateFromServer(this.curRoleUsersList[j].endDate);
                        }
                        this.roles.startDate = this.dateUtils.convertLocalDateFromServer(this.roles.startDate);
                        this.roles.endDate = this.dateUtils.convertLocalDateFromServer(this.roles.endDate);
                        // console.log('this.roles\n' + JSON.stringify(this.roles));
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            $('#roleName').focus();
                        } else {
                            this.isViewOnly = true;
                            
                        }
                     }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                //Focus on Element
                $('#roleName').focus();
            }
        });
    }

    //If Start Date Entered Apply Class
    startEndDateClass(){
        if(this.roles.startDate != null){
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }else{
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }

    /* Function to save role */
    saveRole(){
        this.hideSaveBtn = true;
        let link: any = '';
        if(this.roles.id == undefined || null){
            //If Condition - Create New Role
            if(this.functionalitiesSelected.length > 0){
                //If Functionalities Tagged To Role//
                if(this.usersselected.length > 0){
                    //If Users Tagged To Role
                    this.saveRoleWithFuncandUser();
                }else{
                    this.openDialog();
                }
            }else{
                //Else No Functionalities Tagged To Role//
                this.openDialog();
            }

        }else{
            this.roles.roleFncs = [];
            this.roles.usrRol = [];
            console.log('this.roles' + JSON.stringify(this.roles));
            this.rolesService.updateRole(this.roles).subscribe((res: any) => {
                this.hideSaveBtn = false;
                this.notificationsService.success(
                    'Success!',
                    'Role Successfully Updated'
                );
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
    /* Function to display validition message */
    validationMsg() {
        this.notificationsService.error(
            'Warning!',
            'Fill Mandatory Fields'
        );
    }

    /** Get All Functionalities*/
    getAllFunctionalities() {
        this.selectedFunctionalities = [];
        this.functionalityService.getFuncByTenantId().subscribe((res: Functionality) => {
                this.allFunctionalities = res
                //Filter Assigned and Unassigned Functionalities
                this.allFunctionalities.forEach(func => {
                    func.startDate = this.dateUtils.convertLocalDateFromServer(func.startDate);
                    func.endDate = this.dateUtils.convertLocalDateFromServer(func.endDate);
                    let occured: boolean = false;
                    this.curRoleFuncList.forEach(assignedFunc => {
                        if (func.id === assignedFunc.functionId) {
                            occured = true;
                        }
                    });
                    if (occured) {
                        //Do Nothing
                    }
                    else {
                        this.unassignedFunctionalities.push(func);
                    }
                });

            });
    }

    /** Get All Users */
    getAllUsers() {
        this.selectedUsers = [];
        this.userService.getUsersByTenantId().subscribe((res: any)=>{
            this.allUsers = res;
            //Filter Assigned and Unassigned Users
            this.allUsers.forEach(users => {
                users.startDate = this.dateUtils.convertLocalDateFromServer(users.startDate);
                users.endDate = this.dateUtils.convertLocalDateFromServer(users.endDate);
                let occured: boolean = false;
                this.curRoleUsersList.forEach(assignedUsers => {
                    if (users.id === assignedUsers.userId) {
                        occured = true;
                    }
                });
                if (occured) {
                    //Do Nothing
                }
                else {
                    this.unassignedUsers.push(users);
//                    console.log(JSON.stringify(this.unassignedUsers));
                }
            });
        });
    }

    /* getAllFuncAssToRoles(){
        this.rolesService.fetchAllFuncByRoleId().subscribe((res: any) => {
            this.functionalityTagged = res;
            this.curRoleFuncList = this.functionalityTagged;
        })
    } */

    /** Get All Users Tagged to Roles */
    /* getAllUsersAssToRoles(){
        this.userService.fetchUsersByTenant()
        .subscribe((res: any) => {
            this.curRoleUsersList = res;
        })
    } */

    openDialog(): void {
        if(this.functionalitiesSelected.length > 0){
            var dialogRef = this.dialog.open(RolesModalDialog, {
                width: '400px',
                data: { tagUserM: this.tagUser, tagUser1M: this.yesUser}
            });
            dialogRef.afterClosed().subscribe(result => {
                this.hideSaveBtn = false;
                if (result == 'YesUser') {
                    //Open Dialog - Show Drag and Drop
                    this.unassignedFunctionalities = [];
                    this.selectedFunctionalities=[];
                    this.tagUsers();
                    this.displayusers = true;
                } else if(result == 'NoSave'){
                    //Close Dialog - Save Role With Functionality Only
                    this.saveRoleWithFunc();
                }
            });
        }else{
            var dialogRef = this.dialog.open(RolesModalDialog, {
                width: '400px',
                data: { tagUserM: this.tagUser, tagFunc1M: this.yesFunction}
            });
            dialogRef.afterClosed().subscribe(result => {
                this.hideSaveBtn = false;
                if (result == 'YesFunction') {
                    //Open Dialog - Show Drag and Drop
                    this.unassignedUsers = [];
                    this.selectedUsers=[];
                    this.tagFunc();
                    this.display = true;
                } else if(result == 'NoSave'){
                    //Close Dialog - Save Role
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
        this.display = true;
    }

    //Empty selected users and get all users
    tagUsers(){
        if (this.isViewOnly) {
            this.dndUsers = false;
            this.moreUsers = false;
        }
        this.getAllUsers();
        this.displayusers = true;
    }

    saveRoleOnly(){
        let link: any = '';
        this.roles.activeInd = true;
        this.roles.tenantId = this.UserData.tenantId;
        this.roles.roleDesc = this.roles.roleDescription;
        this.rolesService.create(this.roles).subscribe((roles) => {
            this.notificationsService.success(
                'Success!',
                'Role Successfully Created'
            );
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
        console.log('Roles only saved: ' + JSON.stringify(this.roles));
    }

    saveRoleWithFunc(){
        let link: any = '';
        this.roles.activeInd = true;
        this.roles.tenantId = this.UserData.tenantId;
        this.roles.roleFncs = this.functionalitiesSelected;
        for (var i = 0; i < this.functionalitiesSelected.length; i++) {
            this.roles.roleFncs[i].functionId = this.functionalitiesSelected[i].id;
            this.roles.roleFncs[i].activeFlag = true;
            this.roles.roleFncs[i].assignedBy = this.UserData.id;
        }
        console.log('this.roles\n'+JSON.stringify(this.roles));
        this.rolesService.postRoleFuncNUser(this.roles).subscribe((roles) => {
            this.notificationsService.success(
                'Success!',
                'Role Successfully Created'
            );
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
        this.roles.tenantId = this.UserData.tenantId;
        this.roles.usrRol = this.usersselected;
        this.roles.roleFncs = this.functionalitiesSelected;
        for (var i = 0; i < this.functionalitiesSelected.length; i++) {
            this.roles.roleFncs[i].functionId = this.functionalitiesSelected[i].id;
            this.roles.roleFncs[i].activeFlag = true;
            this.roles.roleFncs[i].assignedBy = this.UserData.id;
        }
        for (var j = 0; j < this.usersselected.length; j++) {
            this.roles.usrRol[j].userId = this.usersselected[j].id;
            this.roles.usrRol[j].activeFlag = true;
            this.roles.usrRol[j].assignedBy = this.UserData.id;
        }
        console.log('this.roles\n'+JSON.stringify(this.roles));
        this.rolesService.postRoleFuncNUser(this.roles).subscribe((roles) => {
            this.hideSaveBtn = false;
            this.notificationsService.success(
                'Success!',
                'Role Successfully Created'
            );
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

    //Confirm After Selecting Functionalities//
    confirmFunctionalities(){
        this.functionalitiesSelected = [];
        this.selectedFunctionalities.forEach(element => {
            this.functionalitiesSelected.push(element);
        });
        if(this.isCreateNew == true){
            this.displayTaggedFunctionalities = true;
            this.selectedTab = 0;
        }
    }

    //Confirm After Selecting Functionalities//
    confirmUsers(){
        this.usersselected = [];
        this.selectedUsers.forEach(element => {
            this.usersselected.push(element);
        });
        if(this.isCreateNew == true){
            this.displayTaggedUsers = true;
            this.selectedTab = 1;
        }
    }


    //Cancel Row Edit
    cancelEdit(){
        this.getAllRolesNTaggedFuncNUsers();
    }

    //Save Role's Tagged Functionality
    updateRoleFunc(roleFunc){
        if(roleFunc.startDate != null){
            console.log('roleFunc: ' + JSON.stringify(roleFunc));
            this.rolesService.updateRoleFunc(roleFunc).subscribe((roles)=>{
                this.notificationsService.success(
                    'Success!',
                    'Tagged Functionality successfully updated'
                );
                this.getAllRolesNTaggedFuncNUsers();
            });
        }else{
            this.notificationsService.error('Warning!', 'Fill mandatory fields');
        }
    }

    //Save Role's Tagged User
    updateRoleUser(roleUser){
        if(roleUser.startDate != null){
            this.rolesService.updateRoleUser(roleUser).subscribe((roles)=>{
                this.notificationsService.success(
                    'Success!',
                    'Tagged User successfully updated'
                );
                this.getAllRolesNTaggedFuncNUsers();
            });
        }else{
            this.notificationsService.error('Warning!', 'Fill mandatory fields');
        }
    }

    //Delete Role's Functionality
    deleteRoleFunc(roleFunc){
        // console.log('roleFunc in delete func' + JSON.stringify(roleFunc));
        this.rolesService.deleteRoleFunc(roleFunc.functionId, roleFunc.roleId).subscribe((res: any)=>{
            this.notificationsService.success(
                'Success!',
                'Tagged Functionality successfully deleted'
            );
            this.getAllRolesNTaggedFuncNUsers();
        });
    }
    
    //Delete Role's User
    deleteRoleUser(roleUser){
        // console.log('roleUser in delete user' + JSON.stringify(roleUser));
        this.rolesService.deleteRoleUser(roleUser.userId, roleUser.roleId).subscribe((res: any)=>{
            this.notificationsService.success(
                'Success!',
                'Tagged User successfully deleted'
            );
            this.getAllRolesNTaggedFuncNUsers();
        });
    }

    toggleSB() {
        if(!this.isVisibleA){
           this.isVisibleA = true;
           $('.split-example .left-component').addClass('visible');
        } else {
            this.isVisibleA = false;
            $('.split-example .left-component').addClass('visible');
        }
    }

    addMoreFunc(){
        //console.log('this.functionalitiesSelected: \n' + JSON.stringify(this.functionalitiesSelected));
        this.rolesService.postTagFuncToRole(this.functionalitiesSelected, this.roles.id).subscribe((res: any)=>{
            this.notificationsService.success(
                'Success!',
                'New Functionalities Tagged to role successfully'
            );
            this.getAllRolesNTaggedFuncNUsers();
        });
        this.display = false;
    }

    addMoreUsers(){
        //console.log('this.usersselected: \n' + JSON.stringify(this.usersselected));
        this.rolesService.postTagUsersToRole(this.usersselected, this.roles.id).subscribe((res: any)=>{
            this.notificationsService.success(
                'Success!',
                'New Users Tagged to role successfully'
            );
            this.getAllRolesNTaggedFuncNUsers();
        });
        this.displayusers = false;
    }












  /*   load(login) {
        this.userService.find(login).subscribe((user) => {
            this.user = user;
        });
    } */

    ngOnDestroy() {
        /* this.subscription.unsubscribe(); */
    }

}


//Old Code

//     ngOnInit() {
//         this.subscription = this.route.params.subscribe((params) => {
//             this.load(params['id']);
//         });
//         this.registerChangeInRoles();
//     }

//     load(id) {
//         this.rolesService.find(id).subscribe((roles) => {
//             this.roles = roles;
//         });
//     }
//     previousState() {
//         window.history.back();
//     }

//     ngOnDestroy() {
//         this.subscription.unsubscribe();
//         this.eventManager.destroy(this.eventSubscriber);
//     }

//     registerChangeInRoles() {
//         this.eventSubscriber = this.eventManager.subscribe(
//             'rolesListModification',
//             (response) => this.load(this.roles.id)
//         );
//     }
// }
