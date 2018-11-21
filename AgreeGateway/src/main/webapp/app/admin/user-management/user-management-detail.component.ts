import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { ElementRef } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { User, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule, DataGridModule,AccordionModule } from 'primeng/primeng';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { CommonService } from '../../entities/common.service';
import { UserMgmtBreadCrumbTitles } from './user-management.model';
import { UserModalService } from './user-modal.service';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { Roles } from '../../entities/roles/roles.model';
import { RolesService } from '../../entities/roles/roles.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { FileSelectDirective, FileDropDirective, FileUploader, FileItem } from 'ng2-file-upload/ng2-file-upload';
import { SessionStorageService,LocalStorageService } from 'ng2-webstorage';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { ApprovalGroupMembersService } from '../../entities/approval-group-members/approval-group-members.service';
import { ApprovalGroupsService } from '../../entities/approval-groups/approval-groups.service';
declare var $: any;
declare var jQuery: any;
const URL = '';

@Component({
    selector: 'jhi-user-mgmt-detail',
    templateUrl: './user-management-detail.component.html',
    animations: [
        trigger('flyInOut', [
            state('in', style({ transform: 'translateX(0)' })),
            transition('void => *', [
                style({ transform: 'translateX(100%)' }),
                animate(1000)
            ])
        ])
    ]
})
export class UserMgmtDetailComponent implements OnInit, OnDestroy {

    users = new User();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    tagRoles: any = 'Yes';
    tagRoles1: any = 'No';
    rolesTagged = [];
    allRoles: any = [];
    curUserRolesList = [];
    selectedRoles = [];
    unassignedRoles = [];
    display: boolean = false;
    userRoleAssnmt = [];
    displayTaggedRoles: boolean = false;
    columnEdit: boolean = false;
    currentUserId: any;
    filesAdded: FileList;
    fileName: any = '';
    public hasBaseDropZoneOver: boolean = false;
    public uploader: FileUploader = new FileUploader({ url: URL });
    languages = [{ value: 'en', name: 'English' }];
    userImg: any;
    dispImg: boolean = false;
    uploadedImage: boolean = false;
    isVisibleA: boolean = false;
    organization:any = 'Nekkanti Systems Pvt Ltd';
    businessTitle:any = 'UI Developer';
    workLocation:any = 'Hyderabad';
    effectiveDate:any = 'Jan 1, 2016';
    managerName:any;
    managerImage:any;
    tableViews:boolean = false;
    /**Groups Grid and Switch View */
    groupSwitchView:boolean = false;
    /* tableView:boolean = true; */
    roleValue:boolean = false;
    dob:any = '30/01/1991';
    address:any/*  = 'plot no 11, jubliee hills' */;
    contactNumber:any/*  = '+91 9866277630' */;
    timeZone:any = 'UTC+05:30';
    selectedTab:any;
    selectedTab1:any;
    managersList:any = [];
    /**Group Assignment Dialog */
    groupAssignmentDialog: boolean = false;
    /**All Groups List*/
    groupsList: any[];
    /**Selected Groups */
    selectedGroups: any[];
    /**DND Group Toggle*/
    dndgrp:boolean = false;
    /**Groups List based on user id */
    groupUser: any[];
    managerialChain:any = [];
    themes:any[]=[];
    fonts:any[]=[];
    activeTheme: string;
    private UserData = this.$sessionStorage.retrieve('userData');
    constructor(
        private userService: UserService,
        private route: ActivatedRoute,
        private eventManager: JhiEventManager,
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
        private userModalService: UserModalService,
        public dialog: MdDialog,
        private rolesService: RolesService,
        private elementRef: ElementRef,
        private $localStorage: LocalStorageService,
        private $sessionStorage: SessionStorageService,
        private approvalGroupMembersService: ApprovalGroupMembersService,
        private approvalGroupsService: ApprovalGroupsService
    ) {
         this.config.minDate = { year: 1950, month: 1, day: 1 };
         this.config.maxDate = { year: 2099, month: 12, day: 31 }; 
         this.selectedGroups = [];
         this.groupUser = [];
         this.themes = [
            { value: 'light-theme', label: 'Purple-Cyan' },
            { value: 'dark-light-theme', label: 'Deep Dark' },
            { value: 'deep-orange-theme', label: 'Green-Yellow' },
            { value: 'dark-night-theme', label: 'Deep Blue' },
            { value: 'full-light-theme', label: 'Light' }
        ];
        this.fonts = [
            { value: 'josefin-sans', label: 'Josefin Sans' },
            { value: 'artifika-serif', label: 'Artifika serif' },
            { value: 'gentium-basic', label: 'Gentium Basic' },
            { value: 'noto-serif', label: 'Noto Serif' },
            { value: 'pt-serif', label: 'PT Serif' },
            { value: 'baumans', label: 'Baumans' },
            { value: 'play', label: 'Play' },
            { value: 'roboto', label: 'Roboto' },
            { value: 'roboto-condensed', label: 'Roboto Condensed' },
            { value: 'pt-sansnarrow', label: 'PT Sans Narrow' },
            { value: 'exo-2', label: 'Exo 2' },
            { value: 'domine', label: 'Domine' },
            { value: 'istok-web', label: 'Istok Web' },
            { value: 'lato', label: 'Lato' }
    
        ];
    }

    ngOnInit() {
        this.onThemeChange();
        this.onFontChange();
        this.userRoleAssnmt = [];
        this.commonService.screensize();
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 161) + 'px'
        });
        
        this.fetchUserDetails();
    }

    onThemeSelect(theme) {
        this.activeTheme = theme;
        this.$localStorage.store('key-theme',theme);
         this.onThemeChange();
     }
     onThemeChange(){
         if(this.$localStorage.retrieve('key-theme') != null || undefined || ''){
            this.activeTheme = this.commonService.DYNAMIC_THEME = this.$localStorage.retrieve('key-theme');
         } else {
            this.activeTheme = this.commonService.DYNAMIC_THEME = 'blue-skies-theme';
         }
     }
 
     onFontSelect(font) {
        this.$localStorage.store('key-font',font);
         this.onFontChange();
     }
     onFontChange(){
         if(this.$localStorage.retrieve('key-font') != null || undefined || ''){
             this.commonService.DYNAMIC_FONT = this.$localStorage.retrieve('key-font');
         } else {
             this.commonService.DYNAMIC_FONT = 'lato';
         }
     }
/* this.sessionStorage.store('recentRoutes',recentRoutes); */
    public onFileDrop(filelist: FileList): void {
        var reader = new FileReader();
        var image = this.elementRef.nativeElement.querySelector('#myImage');

        reader.onload = function (e: any) {
            var src = e.target.result;
            image.src = src;
        };
        reader.readAsDataURL(filelist[0]);
        this.filesAdded = filelist;
        let file: File = filelist[0];
        this.userImg = file;
        this.fileName = file.name;
        // console.log('this.fileName ' + this.fileName);
        this.dispImg = true;
    }

    public fileOverBase(file: File): void {
        if (file)
            this.hasBaseDropZoneOver = true;
      //  console.log('hasBaseDropZoneOver' + this.hasBaseDropZoneOver);
       // console.log('uploader?.queue?.length' + this.uploader.queue.length);
    }
    fileChange(file: any) {
        this.onFileDrop(file.files);
    }
    drop(event){
        // console.log('called');
    }

    changeView(tab){
        if(tab == 'roles'){
            this.tableViews = this.tableViews? false:true;
        }else{
            this.groupSwitchView = this.groupSwitchView?false:true;
        }
    }

    fetchUserDetails() {
       /*  this.userService.getUsersByTenantId().subscribe((res: any) => {
            this.managersList = res;
          //  console.log('this.managersList ' + JSON.stringify(this.managersList));
        }); */
        
        this.userService.usersBasedOnRole("IS_MANAGER").subscribe((res: any) => {
            this.managersList = res;
            // console.log('this.managersList ' + JSON.stringify(this.managersList));
        });
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            if (this.presentPath == "user-management") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }
            if (params['id']) {
                // console.log('in params if ' + params['id']);
                this.currentUserId = params['id'];
                this.userService.getSelectedUserRolesDetails(params['id']).subscribe((res: any) => {
                    this.users = res;
                    this.users.startDate = this.dateUtils.convertLocalDateFromServer(this.users.startDate);
                    this.users.endDate = this.dateUtils.convertLocalDateFromServer(this.users.endDate);
                    this.getGroups(this.currentUserId);
                    this.getManagerialChain(this.UserData.tenantId, this.currentUserId);
                    //this.managersList
                    this.managersList.forEach(element => {
                        if(element.id == this.users.managerId){
                         //   console.log('element ' + element.id);
                            this.managerName = element.firstName + ' ' +element.lastName;
                            this.managerImage = element.image;
                         //   console.log('this.managerName ' + this.managerName);
                         //   console.log('this.managerImage ' + this.managerImage);
                        }
                    });
                    this.userRoleAssnmt = this.users.userRoleAssnmt;
                    this.userRoleAssnmt.forEach(element => {
                        element.startDate = this.dateUtils
                            .convertLocalDateFromServer(element.startDate);
                        element.endDate = this.dateUtils
                            .convertLocalDateFromServer(element.endDate);
                    });
                    this.curUserRolesList = this.userRoleAssnmt;
                    if (this.userRoleAssnmt != null) {
                        this.displayTaggedRoles = true;
                    }
                    this.getAllRolesList();
                  //  console.log('user details ' + JSON.stringify(this.users));
                    this.isCreateNew = false;
                    if (url.endsWith('edit')) {
                        this.isEdit = true;
                    } else {
                        this.isViewOnly = true;

                    }
                });
             //   console.log('this.$sessionStorage ' + JSON.stringify(this.sessionStorage.retrieve('userRoute')));
                let tempRoute = this.$sessionStorage.retrieve('userRoute');
                let recentRoutes = this.$sessionStorage.retrieve('userSetting');
                // console.log('tempRoute ' + tempRoute);
                //this.presentPath
                /* if(tempRoute == 'fromUserPage'){
                    this.selectedTab = 0;
                    this.sessionStorage.clear('userRoute');
                }else if(url.endsWith('edit')){
                    this.selectedTab = 0;
                }else if(this.roleValue == true){
                    this.selectedTab = 3;
                }else{
                    this.selectedTab = 2;
                } */
                if(recentRoutes == true){
                    this.selectedTab = 2;
                    this.$sessionStorage.clear('userSetting');
                }else{
                    this.selectedTab = 1;
                }
                
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                this.users.organizationName = this.UserData.tenantName;
                this.getAllRolesList();
            }
        });
    }
    /* Function to fetch All roles list */
    getAllRolesList() {
            this.unassignedRoles = [];
     //   console.log('called ' + JSON.stringify(this.unassignedRoles));
        this.rolesService.fetchAllRolesByTenantId().subscribe((res: any) => {
            this.allRoles = res;
        //    console.log('all roles List :' + JSON.stringify(this.allRoles));
            this.allRoles.forEach(role => {
                if (role.activeInd == "TRUE") {
                    role.activeFlag = true;
                } else {
                    role.activeFlag = false;
                }
                role.roleId = role.id;

                role.startDate = this.dateUtils
                    .convertLocalDateFromServer(role.startDate);
                role.endDate = this.dateUtils
                    .convertLocalDateFromServer(role.endDate);
                let occured: boolean = false;
            //    console.log('this.curUserRolesList ' + JSON.stringify(this.curUserRolesList));
                this.curUserRolesList.forEach(assignedRole => {
                    if (role.id === assignedRole.roleId) {
                        occured = true;
                    }
                });
                if (occured) {

                }
                else {
                        this.unassignedRoles.push(role);
                }
            });
            if(this.isCreateNew && this.unassignedRoles.length>0){
                for(var i=0;i<this.selectedRoles.length;i++){
                    for(var j=0;j<this.unassignedRoles.length;j++){
                        if(this.selectedRoles[i].id == this.unassignedRoles[j].id){
                            this.unassignedRoles.splice(j,1);
                        }
                    }
                }
            }
        });
        
    }

    openDialog(): void {
        let dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
            width: '250px',
            data: { tagRoles: this.tagRoles, tagRoles1: this.tagRoles1 }
        });

        dialogRef.afterClosed().subscribe(result => {
            // console.log('The dialog was closed' + result);
            if (result == 'Yes') {
            //    console.log('yes ' + result);
                this.unassignedRoles = [];
                this.getAllRolesList();
                this.selectedRoles = [];
                this.display = true;
            } else if (result == 'No') {
             //   console.log('no ' + result);
                this.saveUserDetails();
            }
        });
    }
    saveUserDetails() {
        let link: any = '';
        delete this.users.id;
        this.users['password'] = 'welcome';
     //   console.log('before save ' + JSON.stringify(this.users));
        this.users.curUser = this.UserData.id;
        this.users.tenantId = this.UserData.tenantId;
        this.userService.userCreation(this.users).subscribe((res: any) => {
            this.users = res;
            /**START Assign groups if user created */
            if (this.groupUser) {
                let obj = {};
                let arr = [];
                this.groupUser.forEach(element => {
                    obj = {
                        'groupId': element.id,
                        'startDate': element.startDate,
                        'endDate': element.endDate,
                        'userId': this.users.id
                    }
                    arr.push(obj);
                });

                if (arr) {
                    this.approvalGroupMembersService.postAppGrpMemUser(arr).subscribe(() => {},
                    error => {
                        this.notificationsService.error('Error!', 'Failed to save groups');
                    });
                }
            }
            /**END Assign groups if user created */
            this.notificationsService.success(
                'Success!',
                'User created successfully'
            )
        //    console.log('after save ' + JSON.stringify(this.users));
            if (this.userImg) {
                this.userService.uploadImg(this.userImg, this.users.id).subscribe((res: any) => {
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
                });
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

        });
    }
    /* Function to save users */
    saveUser() {
     //   console.log('this.userRoleAssnmt ' + JSON.stringify(this.userRoleAssnmt));
        let link: any = '';
        if (this.users.id) {
            this.users.curUser = this.UserData.id;
            this.users.tenantId = this.UserData.tenantId;
      //      console.log('user to update ' + JSON.stringify(this.users));
            this.userService.userCreation(this.users).subscribe((res: any) => {
                this.users = res;
                if (this.userImg) {
                    this.userService.uploadImg(this.userImg, this.users.id).subscribe((res: any) => {
                    //    console.log('after save ' + JSON.stringify(this.users));
                        this.selectedTab = 0;
                        this.notificationsService.success(
                            'Success!',
                            'User details updated successfully'
                        )
                        if (this.users.id) {
                            link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                            this.router.navigate(link);
                        }
                    });
                } else {
                    this.notificationsService.success(
                        'Success!',
                        'User details updated successfully'
                    )
                    this.selectedTab = 0;
                    if (this.users.id) {
                        link = ['/user-management', { outlets: { 'content': this.users.id + '/details' } }];
                        this.router.navigate(link);
                    }
                }

            });
        } else {
            this.users['userRoleAssignments'] = this.userRoleAssnmt;
            this.userRoleAssnmt.forEach(element => {
                element['activeFlag'] = true;
            });
            this.users['authorities'] = [
                {
                    "name": "ROLE_USER"
                }
            ];
            this.users['activated'] = true;
            this.users['login'] = this.users.firstName;
            this.users['langKey'] = 'en';
        //    console.log('user to save ' + JSON.stringify(this.users));
            if (this.userRoleAssnmt.length > 0) {
                delete this.users.id;
                this.users['password'] = 'welcome';
                this.users.curUser = this.UserData.id;
                this.users.tenantId = this.UserData.tenantId;
                this.userService.userCreation(this.users).subscribe((res: any) => {
                    this.users = res;
                    /**START Assign groups if user created */
                    if (this.groupUser) {
                        let obj = {};
                        let arr = [];
                        this.groupUser.forEach(element => {
                            obj = {
                                'groupId': element.id,
                                'startDate': element.startDate,
                                'endDate': element.endDate,
                                'userId': this.users.id
                            }
                            arr.push(obj);
                        });

                        if (arr) {
                            this.approvalGroupMembersService.postAppGrpMemUser(arr).subscribe(() => {},
                            error => {
                                this.notificationsService.error('Error!', 'Failed to save groups');
                            });
                        }
                    }
                    /**END Assign groups if user created */
                    this.selectedTab = 0;
                    this.notificationsService.success(
                        'Success!',
                        'User created successfully'
                    )
                //    console.log('after save ' + JSON.stringify(this.users));
                //    console.log('this.userImg ' + this.userImg);
                    if (this.userImg) {
                        this.userService.uploadImg(this.userImg, this.users.id).subscribe((res: any) => {
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
                        });
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

                });
            } else {
                this.openDialog();
            }

        }
    }

    tagSelectedRoles() {
        
        this.userRoleAssnmt = [];
    //    console.log('selected roles ' + JSON.stringify(this.selectedRoles));
        this.selectedRoles.forEach(element => {
            this.userRoleAssnmt.push(element);
        });
        //this.userRoleAssnmt = this.userRoleAssnmt.filter(function(item, i, ar){ return ar.indexOf(item) === i; });
        //this.userRoleAssnmt = this.selectedRoles;
        this.displayTaggedRoles = true;
    }

    tagNewSelectedRoles() {
    //    console.log('this.currentUserId ' + this.currentUserId);
        let link: any = '';
        this.selectedRoles.forEach(element => {
            element['userId'] = this.currentUserId;
            element['activeFlag'] = true;
        });
     //   console.log('selectedRoles ' + JSON.stringify(this.selectedRoles));
        this.userService.assignRolesToUser(this.selectedRoles).subscribe((res: any) => {
            this.notificationsService.success(
                'Success!',
                'Role(s) tagged to selected user'
            )
        //    console.log('role tagged ' + JSON.stringify(res));
            this.selectedRoles = [];
            this.unassignedRoles = [];
            this.roleValue = true;
            this.fetchUserDetails();

        });

    }

    /* function to get assigned roles and functions for selected user */
    getAssignedRolesList() {
        if (!this.isCreateNew) {
            this.userService.getListOfRoles(this.users.id).subscribe((res: any) => {
                this.curUserRolesList = res;
             //   console.log('curUserRolesList ' + JSON.stringify(this.curUserRolesList));
            });
        }
    }

    /* function to delete role */
    deleteColumn(val, ind) {
     //   console.log('val ' + JSON.stringify(val));
     //   console.log('ind ' + ind);
        if(this.isCreateNew){
            this.userRoleAssnmt.splice(ind,1);
        }else{
            this.userService.deleteRole(val).subscribe((res: any) => {
                this.notificationsService.success(
                    'Success!',
                    'Role "' + val.roleName+'" successfully deleted'
                )
                this.roleValue = true;
                this.fetchUserDetails();
            });
        }
    }

    /* function to update roles */
    updateUserRole(obj:any){
      //  console.log('this.currentUserId ' + this.currentUserId);
        // console.log('obj ' + JSON.stringify(obj));
        obj['userId'] = this.currentUserId;
        this.userService.updateRole(obj).subscribe((res: any) => {
            this.notificationsService.success(
                'Success!',
                'Role "' + obj.roleName+'" successfully updated'
            )
            this.roleValue = true;
            this.fetchUserDetails();
        });
    }
    duplicateEmail:boolean = false;
    validateEmail(email: any) {
        this.regemail(email);
        if (this.emailValid == true) {
            this.userService.validateEmailId(email).subscribe((res: any) => {
                // console.log('res ' + JSON.stringify(res));
                if (res.result == 'Success') {
                    this.duplicateEmail = false;
                } else {
                    this.duplicateEmail = true;
                    /*this.notificationsService.error(
                        'Warning!',
                        res.status
                    )*/
                }
            });
        }
    }

    emailValid:boolean = false;
    regemail(email){
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        this.emailValid = re.test(email);
    }
    addRoles(){
        if(!this.isCreateNew){
            this.selectedRoles = [];
        } else{
            this.selectedRoles = [];
            this.userRoleAssnmt.forEach(element => {
                this.selectedRoles.push(element);
            });
            //this.selectedRoles = this.userRoleAssnmt;
            
            /* for(var i=0;i<this.selectedRoles.length;i++){
                for(var j=0;j<this.userRoleAssnmt.length;j++){
                    if(this.selectedRoles[i].id == this.userRoleAssnmt[j].id){
                        
                    }else{
                        this.selectedRoles.splice(i,1);
                    }
                }
            } */
        } 
        this.getAllRolesList();
    }
    /* function to cancel changes */
    cancelColumnChanges(ind) {
        // console.log('ind ' + ind);
    }

    ngOnDestroy() {
        /* this.subscription.unsubscribe(); */
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

    /**List of Groups based on user id */
    getGroups(userId){
        // console.log(`userId: ${userId}`);
        this.approvalGroupMembersService.getAppGrpMemUserId(userId).subscribe((res)=>{
            this.groupUser = res;
            this.dateConvert(this.groupUser);
            // console.log(this.groupUser);
        },
        error => {
            this.notificationsService.error('Error!', 'Failed to fetch groups');
        });
    }

    /**Navigate to Groups Creation */
    createGroup(){
        this.router.navigate(['/groups', {outlets: { 'content': ['groups-new']}}]);
        return false;
    }

    /**Assign Group */
    assignGroup(){
        this.groupsList = [];
        this.approvalGroupsService.approvalgroupsForTenant().subscribe((res)=>{
            res.forEach(unassgrp => {
                let occured: boolean = false;
                this.groupUser.forEach(assGrp => {
                    if(this.isCreateNew) {
                        if (unassgrp.id === assGrp.id) {
                            occured = true;
                        }
                    }else {
                        if (unassgrp.id === assGrp.groupId) {
                            occured = true;
                        }
                    }
                });
                if (occured) {/* Do Nothing */}
                else {
                    this.groupsList.push(unassgrp);
                }
            });
            this.dateConvert(this.groupsList);
            this.selectedGroups = [];
            this.dndgrp = false;
            this.groupAssignmentDialog = true;
        });
    }

    /**Save Assigned Group based on creation and updation */
    saveAssignedGrp(){
        if(this.isCreateNew == true) {
            this.groupAssignmentDialog = false;
            this.groupSwitchView = true;
            this.selectedGroups.forEach(element => {
                this.groupUser.push(element);
            });
        }else {
            this.saveGrp();
        }
    }

    /**Convert Date to prevent error - DatePIcker */
    dateConvert(arr) {
        if(arr){
            arr.forEach(element => {
                element.startDate = this.dateUtils.convertLocalDateFromServer(element.startDate);
                element.endDate = this.dateUtils.convertLocalDateFromServer(element.endDate);
            });
            return arr;
        }
    }

    /**Edit Group User */
    editGrpUser(i) {
        let throwValidation: any = false;
        for (let i = 0; i < this.groupUser.length; i++) {
            if (this.groupUser[i].columnEdit == true) {
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.notificationsService.error('Warning!', 'Please save row before editing');
        } else {
            this.groupUser[i].columnEdit = true;
        }
    }

    /**Update Group User */
    updateGrpUser(grpUser){
        // console.log(JSON.stringify(grpUser));
        let arr = [];
        arr.push(grpUser);
        this.approvalGroupMembersService.postAppGrpMemUser(arr).subscribe(res => {
            this.notificationsService.success('Success!', 'Group details updated successfully');
            this.fetchUserDetails();
        },
        err => {
            this.notificationsService.error('Error!', 'Failed to update user');
        });
    }

    /**Cancel Goup User */
    cancelEdit(){
        this.getGroups(this.currentUserId);
    }

    /**Save Group */
    saveGrp() {
        let obj = {};
        let arr = [];
        this.selectedGroups.forEach(element => {
            obj = {
                'groupId': element.id,
                'startDate': element.startDate,
                'endDate': element.endDate,
                'userId': this.currentUserId
            }
            arr.push(obj);
        });

        if(arr && arr.length > 0) {
            this.approvalGroupMembersService.postAppGrpMemUser(arr).subscribe(() => {
                this.groupAssignmentDialog = false;
                this.notificationsService.success('Success!', `${arr.length>1 ? 'Groups has been assigned' : 'Group has been assigned'}`);
                this.fetchUserDetails();
            },
            error => {
                this.notificationsService.error('Error!', 'Failed to save groups');
            });
        }
    }

    /**Delete Row */
    deleteRow(i) {
        this.groupUser.splice(i, 1);
    }

    /**Get Current User Managerial Chain */
    getManagerialChain(tenantId, userId) {
        this.userService.getManagerialChain(tenantId, userId).subscribe(res => {
            this.managerialChain = res;
        });
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