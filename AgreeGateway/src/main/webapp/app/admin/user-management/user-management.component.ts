import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit } from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
//import { UserRolesAssignmentService } from '../../entities/user-roles-assignment/user-roles-assignment.service';
//import { UserRolesAssignment } from '../../entities/user-roles-assignment/user-roles-assignment.model';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { SelectItem, OverlayPanelModule, DialogModule } from 'primeng/primeng';
import { Roles } from '../../entities/roles/roles.model';
import { RolesService } from '../../entities/roles/roles.service';
import { USERSMODEL } from './user-management.model';
import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { CommonService } from '../../entities/common.service';
import {UserRoleAssignment } from '../../entities/roles/user-role-assignment.model';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { SessionStorageService } from 'ng2-webstorage';
declare var $: any;
declare var jQuery: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
@Component({
    selector: 'jhi-user-mgmt',
    templateUrl: './user-management.component.html',
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
export class UserMgmtComponent implements OnInit, OnDestroy {

    users = [];
    availableRoles =[];
    assignedRoles =[];
    selectedRoles :UserRoleAssignment[];
    draggedRole : any ;
    eventSubscriber: Subscription;
    
    currentAccount: any;
    
    currentUser:User;
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
    mouseOverRowNo:number;
    selectedRowNo:number;
    isUserSelected:boolean=false;
    curUserRoles=[];
    curUserRolesList = [];
    allRoles:any={};
    display: boolean = false;
    unassignedRoles = [];
    userRoleAssignment =[];
    columnOptions: SelectItem[];
    startDate:any;
    TemplatesHeight: any;
    tableView:boolean = true;
    usersTableColumns = [
        /*  { field: 'login', header: 'Login', width: '150px', align: 'left' },  */
        { field: 'firstName', header: 'First Name', width: '150px', align: 'left' },
        { field: 'lastName', header: 'Last Name', width: '150px', align: 'left' },
        { field: 'email', header: 'Email', width: '150px', align: 'left' },
        { field: 'langKey', header: 'Language', width: '150px', align: 'left' }
    ];
    constructor(
         private config: NgbDatepickerConfig,
        private dateUtils: JhiDateUtils,
        private userService: UserService,
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        //private userRolesAssignmentService: UserRolesAssignmentService,
        private rolesService: RolesService,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
        private sessionStorage: SessionStorageService,
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    this.config.maxDate = { year: 2099, month: 12, day: 31 };
    this.columnOptions = [];
    for (let i = 0; i < this.usersTableColumns.length; i++) {
         this.columnOptions.push({ label: this.usersTableColumns[i].header, value: this.usersTableColumns[i] });
        }
//        this.itemsPerPage = ITEMS_PER_PAGE;
//        this.routeData = this.activatedRoute.data.subscribe(data => {
//            this.page = data['pagingParams'].page;
//            this.previousPage = data['pagingParams'].page;
//            this.reverse = data['pagingParams'].ascending;
//            this.predicate = data['pagingParams'].predicate;
//        });
        
    }
    changeMinDate() {
   // this.config.minDate = this.fileTemplate.startDate;
  }

  resetMinDate() {
    this.config.minDate = { year: 1950, month: 1, day: 1 };
  }

  showUserDetails(id){
      console.log('called ' + id);
      let userRouteName = 'fromUserPage';
      this.sessionStorage.store('userRoute',userRouteName);
      let link = ['/user-management', {outlets: {'content': id +'/details'}}]
      this.router.navigate(link);
  }
    tagRoles()
    {
        console.log('this.selectedRoles after drag'+JSON.stringify(this.selectedRoles));
        let roleIds = [];
       
        this.selectedRoles.forEach(role=>{
            let userRA = new UserRoleAssignment();
            if(role && role.id)
            {
                userRA.userId =  this.currentUser.id;
                userRA.createdBy = this.currentUser.id;
                userRA.assignedBy = this.currentUser.id;
                userRA.lastUpdatedBy = this.currentUser.id;
            }
        });
       // this.assignRolesToUser( this.currentUser.id,roleIds);
    }
    assignRolesToUser(userId : any, roleIds : any[])
    {
    //this.rolesService.assignRolesToUser().subscribe((res:any)=>{
        
   // });
    }
    drop(event) {
        console.log('dropped');
        if(this.draggedRole) {
            let draggedCarIndex = this.findIndex(this.draggedRole);
            this.selectedRoles = [...this.selectedRoles, this.draggedRole];
            this.availableRoles = this.availableRoles.filter((val,i) => i!=draggedCarIndex);
            this.draggedRole = null;
        }
        
    }
    
    dragEnd(event) {
        console.log('dragged');
        this.draggedRole = null;
    }
    
    findIndex(role: any) {
        let index = -1;
        for(let i = 0; i < this.availableRoles.length; i++) {
            if(role === this.availableRoles[i]) {
                index = i;
                break;
            }
        }
        return index;
    }
       assignRoles() {
        this.display = true;
           console.log('current user is'+JSON.stringify(this.currentUser));
            this.unassignedRoles = [];
         this.selectedRoles=[];
           this.fetchUnassignedRoles();
           
    }
    fetchUnassignedRoles()
    {
        this.rolesService.fetchAllRolesByTenantId().subscribe((res:any)=>{
           
            this.availableRoles = res;
            console.log('  this.availableRoles '+JSON.stringify(  this.availableRoles ));
        this.availableRoles.forEach(role=>{
            let occured : boolean =false;
            this.curUserRolesList.forEach(assignedRole=>{
                if(role.roleName === assignedRole.roleName)
                {
                    occured = true;
                }
            });
            if(occured)
            {
                
            }
            else
                {
                
                  this.unassignedRoles.push(role);
            }
        });
      console.log('this.unassignedRoles'+JSON.stringify(this.unassignedRoles));
            });
        
    }
    mouseOverOnRow(i:number){
        this.mouseOverRowNo=i;
    }
    fetchRolesAndFunctions()
    {
            
    }
    userSelected(curUser:any,i:number){
        console.log('cur user'+JSON.stringify(curUser));
        if(curUser && curUser.id)
        this.rolesService.fetchRolesAndFunctionsByUserId(curUser.id).subscribe((res:any)=>{
              this.curUserRolesList = [];
            this.curUserRoles=res;
            this.curUserRoles.forEach(role=>{
            this.curUserRolesList.push(role);   
             
            });
        });
        this.currentUser=curUser;
        this.isUserSelected=true;
        this.selectedRowNo=i;
        this.fetchRolesAndFunctions();
        //this.curUserRoles=
//        this.userRolesAssignmentService.findbyUserId(curUser.id).subscribe(
//            response => {
//                this.curUserRoles=response;
//                this.rolesService.query({
//                    sort: this.sort()}).subscribe(
//                    (res: Response) => {
//                        this.allRoles=res.json();
//                        .forEach(userRole=>{
//                            res.json().forEach(role=>{
//                                if(role.id==userRole.roleId)
//                                {
//                                    userRole.roleName=role.roleName;
//                                }
//                            })    
//                        })
//                    });
//            });
    }

    ngOnInit() {
        this.TemplatesHeight = (this.commonService.screensize().height - 340) + 'px';
        $(".setHeight").css({
            'max-height': this.TemplatesHeight
        });
        this.userService.fetchUsersByTenant().subscribe((res:any)=>{
            res.forEach(element => {
               if(element.langKey == 'en'){
                   element.langKey = "English";
               } 
            });
            this.users = res;
        });
        //this.userSelected(this.users[0],0);

        /** Function for Toggling Global Search **/
        $(".search-icon-body").click(function () {
            if ($(".ftlSearch md-input-container").hasClass("hidethis")) {
                $(".ftlSearch md-input-container").removeClass("hidethis");
                $(".ftlSearch md-input-container").addClass("show-this");
            } else if ($(".ftlSearch md-input-container").hasClass("show-this")) {
                var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                    return this.value != '';
                });
                if (value.length <= 0) { // zero-length string
                    $(".ftlSearch md-input-container").removeClass("show-this");
                    $(".ftlSearch md-input-container").addClass("hidethis");
                }
            } else {
                $(".ftlSearch md-input-container").addClass("show-this");
            }
        });
        $(".ftlSearch md-input-container .mySearchBox").blur(function () {
            var value = $('.ftlSearch md-input-container .mySearchBox').filter(function () {
                return this.value != '';
            });
            if (value.length <= 0) { // zero-length string
                $(".ftlSearch md-input-container").removeClass("show-this");
                $(".ftlSearch md-input-container").addClass("hidethis");
            }
        });
    }

    ngOnDestroy() {
    }


    setActive (user, isActivated) {
//        user.activated = isActivated;
//
//        this.userService.update(user).subscribe(
//            response => {
//                if (response.status === 200) {
//                    this.error = null;
//                    this.success = 'OK';
//                    this.loadAll();
//                } else {
//                    this.success = null;
//                    this.error = 'ERROR';
//                }
//            });
    }

    loadAll () {
        this.currentUser = this.users[0];
//        this.userService.query({
//            page: this.page - 1,
//            size: this.itemsPerPage,
//            sort: this.sort()}).subscribe(
//            (res: Response) =>{ this.onSuccess(res.json(), res.headers);},
//            (res: Response) => this.onError(res.json())
//        );
    }

    trackIdentity (index, item: User) {
        return item.id;
    }

    sort () {
        let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage (page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition () {
        this.router.navigate(['/user-management'], { queryParams:
                {
                    page: this.page,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }

    private onSuccess(data, headers) {
//        this.totalItems = headers.get('X-Total-Count');
//        this.queryCount = this.totalItems;
//        this.users = data;
    }

    private onError(error) {
    }
}
