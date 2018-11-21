import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { RolesService } from '../../entities/roles/roles.service';
import { ITEMS_PER_PAGE, User, UserService } from '../../shared';
import { UserRoleAssignment } from '../../entities/roles/user-role-assignment.model';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { SessionStorageService } from 'ng2-webstorage';
declare var $: any;
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
                animate(500)
            ])
        ]),
        trigger(
            'enterAnimation', [
                transition(':enter', [
                    style({ opacity: 0 }),
                    animate('1000ms 100ms ease-in', style({ opacity: 1 }))
                ])
            ]
        )
    ]
})
export class UserMgmtComponent implements OnInit, OnDestroy {

    users = [];
    availableRoles = [];
    assignedRoles = [];
    selectedRoles: UserRoleAssignment[];
    draggedRole: any;
    pageSizeOptions = [10, 25, 50, 100];
    currentUser: User;
    itemsPerPage: any;
    page = 0;
    mouseOverRowNo: number;
    selectedRowNo: number;
    isUserSelected = false;
    curUserRoles = [];
    curUserRolesList = [];
    allRoles: any = {};
    display = false;
    unassignedRoles = [];
    userRoleAssignment = [];
    startDate: any;
    tableView = true;
    usersTableColumns = [
        { field: 'lastName', header: 'Last Name', width: '20%', align: 'left' },
        { field: 'email', header: 'Email', width: '20%', align: 'left' },
        { field: 'langKey', header: 'Language', width: '10%', align: 'left' }
    ];
    usersRecordsLength: number;
    private unsubscribe: Subject<void> = new Subject();

    constructor(
        private userService: UserService,
        private rolesService: RolesService,
        private router: Router,
        private sessionStorage: SessionStorageService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
    }

    showUserDetails(id, type) {
        const userRouteName = 'fromUserPage';
        this.sessionStorage.store('userRoute', userRouteName);
        const link = ['/user-management', { outlets: { 'content': id + '/' + type } }]
        this.router.navigate(link);
    }

    tagRoles() {
        const roleIds = [];
        this.selectedRoles.forEach((role) => {
            const userRA = new UserRoleAssignment();
            if (role && role.id) {
                userRA.userId = this.currentUser.id;
                userRA.createdBy = this.currentUser.id;
                userRA.assignedBy = this.currentUser.id;
                userRA.lastUpdatedBy = this.currentUser.id;
            }
        });
    }

    drop(evt) {
        if (this.draggedRole) {
            const draggedCarIndex = this.findIndex(this.draggedRole);
            this.selectedRoles = [...this.selectedRoles, this.draggedRole];
            this.availableRoles = this.availableRoles.filter((val, i) => i != draggedCarIndex);
            this.draggedRole = null;
        }
    }

    dragEnd(evt) {
        this.draggedRole = null;
    }

    findIndex(role: any) {
        let index = -1;
        for (let i = 0; i < this.availableRoles.length; i++) {
            if (role === this.availableRoles[i]) {
                index = i;
                break;
            }
        }
        return index;
    }

    assignRoles() {
        this.display = true;
        this.unassignedRoles = [];
        this.selectedRoles = [];
        this.fetchUnassignedRoles();
    }

    fetchUnassignedRoles() {
        this.rolesService.fetchAllRolesByTenantId().takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.availableRoles = res;
            this.availableRoles.forEach((role) => {
                let occured = false;
                this.curUserRolesList.forEach((assignedRole) => {
                    if (role.roleName === assignedRole.roleName) {
                        occured = true;
                    }
                });
                if (occured) {

                } else {
                    this.unassignedRoles.push(role);
                }
            });
        });
    }

    userSelected(curUser: any, i: number) {
        if (curUser && curUser.id) {
            this.rolesService.fetchRolesAndFunctionsByUserId(curUser.id).takeUntil(this.unsubscribe).subscribe((res: any) => {
                this.curUserRolesList = [];
                this.curUserRoles = res;
                this.curUserRoles.forEach((role) => {
                    this.curUserRolesList.push(role);

                });
            });
        }
        this.currentUser = curUser;
        this.isUserSelected = true;
        this.selectedRowNo = i;
    }

    ngOnInit() {
        this.userService.fetchUsersByTenant1(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.users = res.json();
            this.usersRecordsLength = +res.headers.get('x-total-count');
            this.usersIconDisplay();
        });
    }
    usersIconDisplay(){
        this.users.forEach((element,i) => {
            setTimeout(() => {
                if (element['image']) {
                    if(this.tableView){
                        $('.userPicture_'+i).css({
                            'background-image': 'url(data:image/png;base64,' + element['image'] + ')',
                            'background-position': 'center',
                            'background-size': '52px'
                        });
                    }else{
                        $('.userPicture_'+i).css({
                            'background-image': 'url(data:image/png;base64,' + element['image'] + ')',
                            'background-position': 'center',
                            'background-size': '76px'
                        });
                    }
                } else {
                    $('.userPicture_'+i).attr('data-content', element.firstName.charAt(0).toUpperCase());
                }
            }, 0);
        });
    }

    onPaginateChange(evt) {
        this.page = evt.pageIndex;
        this.itemsPerPage = evt.pageSize;
        this.userService.fetchUsersByTenant1(this.page, this.itemsPerPage).takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.users = res.json();
            this.usersRecordsLength = +res.headers.get('x-total-count');
        });
    }
    
    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    loadAll() {
        this.currentUser = this.users[0];
    }

    onRowSelect(evt) {
        this.showUserDetails(evt.data.id, 'details');
    }
}
