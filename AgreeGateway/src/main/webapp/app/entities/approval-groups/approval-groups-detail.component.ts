import { Component, OnInit, OnDestroy, OnChanges, Input, AfterViewInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';
import { Response } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, Principal, UserService } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Router, NavigationEnd } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SelectItem, MultiSelectModule } from 'primeng/primeng';
import { PerfectScrollbarComponent } from 'angular2-perfect-scrollbar';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NotificationsService } from 'angular2-notifications-lite';
import { JhiDateUtils } from 'ng-jhipster';
import { CommonService } from '../../entities/common.service';
import { ApprovalGroups, ApprovalGroupsBreadCrumbTitles } from './approval-groups.model';
import { ApprovalGroupsService } from './approval-groups.service';
import { ApprovalGroupMembersService } from '../approval-group-members/approval-group-members.service';
import { SessionStorageService } from 'ng2-webstorage';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'jhi-approval-groups-detail',
    templateUrl: './approval-groups-detail.component.html'
})
export class ApprovalGroupsDetailComponent implements OnInit {

    //private UserData = this.$sessionStorage.retrieve('userData');
    approvalGroups = new ApprovalGroups();
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    isCreateNew: boolean = false;
    isEdit: boolean = false;
    isViewOnly: boolean = false;
    routeSub: any;
    presentPath: any;
    presentUrl: any;
    isVisibleA: boolean = false;
    /**List of users - Mdselect */
    users: any;
    /**Unassigned users */
    unassignedUsers: any = [];
    /**Selected Users - Dragged Users for new group creation*/
    selectedUsers: any = [];
    /**Dialog and Drag and drop */
    apprGrpMemDialog: boolean = false;
    /**Multiple Records Edit */
    btnEditAll: boolean = false;
    /**Start and End Date for Dnd all members */
    startDateAllMember: any;
    endDateAllMember: any;
    dndToggle: boolean = false;
    nameExist: boolean = false;

    constructor(
        private eventManager: JhiEventManager,
        private approvalGroupsService: ApprovalGroupsService,
        private route: ActivatedRoute,
        private principal: Principal,
        private paginationConfig: PaginationConfig,
        private router: Router,
        private config: NgbDatepickerConfig,
        private notificationsService: NotificationsService,
        private dateUtils: JhiDateUtils,
        private commonService: CommonService,
        private userService: UserService,
        private approvalGroupMembersService: ApprovalGroupMembersService,
        private $sessionStorage: SessionStorageService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    ngOnInit() {
        this.loadAll();
        this.commonService.screensize();
        $(".split-example").css({'height': 'auto','min-height': (this.commonService.screensize().height - 161) + 'px'});

        /* this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApprovalGroups(); */
    }

    /**Load Approval groups details */
    loadAll(){
        this.subscription = this.route.params.subscribe(params => {
            let url = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentUrl = this.route.snapshot.url.map(segment => segment.path).join('/');
            this.presentPath = this.route.snapshot.url;
            /* console.log(this.presentPath);
            if (this.presentPath == "groups") {
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            } */

            if (params['id']) {
                this.approvalGroupsService.getApprovalGroupsAndGrpMembers(params['id']).subscribe((res) => {
                        this.approvalGroups = res;
                        /**Get List of Users */
                        this.getUsers();
                        // console.log('this.approvalGroups\n' + JSON.stringify(this.approvalGroups));
                        this.isCreateNew = false;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            /**Focusing on first element */
                            $('#groupName').focus();
                        } else {
                            this.isViewOnly = true;
                        }
                    }
                );
            } else {
                this.isVisibleA = true;
                this.isCreateNew = true;
                /**Get List of Users */
                this.getUsers();
                /**Focusing on first element */
                $('#groupName').focus();
                /**First empty row by default while creating */
                let obj = {
                    'seq': null,
                    'userId': null,
                    'startDate': null,
                    'endDate': null,
                    'columnEdit': true
                };
                this.approvalGroups.approvalsGroupMemList.push(obj);
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

    /**Save Approval Groups and its members */
    saveApprGroup() {
        this.approvalGroupsService.checkApprovalGroupIsExist(this.approvalGroups.groupName, this.approvalGroups.id).subscribe(res => {
            this.nameExist = res.result != 'No Duplicates Found' ? true : false;
            if (!this.nameExist) {
                let link: any = '';
                let membersEmpty: any = false;
                if (this.approvalGroups.id) {
                    /**Update Approval Group */
                    /**Plus one day */
                    /* this.approvalGroups.startDate = this.approvalGroups.startDate.setDate(this.approvalGroups.startDate.getDate()+1);
                    this.approvalGroups.startDate = new Date(this.approvalGroups.startDate);
                    this.approvalGroups.endDate = this.approvalGroups.endDate.setDate(this.approvalGroups.endDate.getDate()+1);
                    this.approvalGroups.endDate = new Date(this.approvalGroups.endDate); */

                    // console.log('Update this.approvalGroups\n' + JSON.stringify(this.approvalGroups));
                    this.approvalGroupsService.update(this.approvalGroups).subscribe((res: any) => {
                        // console.log('res\n' + JSON.stringify(res));
                        this.notificationsService.success(
                            'Success!',
                            '"' + res.groupName + '"' + ' Successfully Updated'
                        );
                        if (res.id) {
                            link = ['/groups', { outlets: { 'content': res.id + '/details' } }];
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
                    /**Create New Approval Group */
                    for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                        if (this.approvalGroups.approvalsGroupMemList[i].seq == null || this.approvalGroups.approvalsGroupMemList[i].userId == null ||
                            this.approvalGroups.approvalsGroupMemList[i].startDate == null) {
                            membersEmpty = true;
                        }
                    }
                    if (membersEmpty == true) {
                        this.notificationsService.error(
                            'Warning!',
                            'Fill mandatory fields'
                        );
                    } else {
                        // console.log('Create new this.approvalGroups\n' + JSON.stringify(this.approvalGroups));
                        this.approvalGroupsService.postApprovalGroupsAndGrpMembers(this.approvalGroups).subscribe((res: any) => {
                            // console.log('res\n' + JSON.stringify(res));
                            this.notificationsService.success(
                                'Success!',
                                'Group Successfully Created'
                            );
                            if (res.id) {
                                link = ['/groups', { outlets: { 'content': res.id + '/details' } }];
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
                }
            }
        });
    }

    /* Function to display validition message */
    validationMsg(form) {
        if(form == 'apprGrp'){
            if(this.approvalGroups.groupName == null || this.approvalGroups.groupName == ''){
                this.notificationsService.error(
                    'Warning!',
                    'Name is mandatory'
                );
            }else if(this.approvalGroups.startDate == null){
                this.notificationsService.error(
                    'Warning!',
                    'Start date is mandatory'
                );
            }
        }else{
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        }
    }

    /**Get List of Users with activeFlag true */
    getUsers() {
        this.userService.getUsersByTenantId().subscribe((res: any) => {
            this.users = res;
        });
    }

    /**Check if user already assigned */
    memberChange(value, index) {
        let throwValidation: any = false;
        if (this.approvalGroups.approvalsGroupMemList.length < 1) { }
        else {
            for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                if (value == this.approvalGroups.approvalsGroupMemList[i].userId && index != i) {
                    throwValidation = true
                }
            }
            if (throwValidation == true) {
                this.notificationsService.error('Warning', 'User already assigned to this group');
                setTimeout(() => {
                    this.approvalGroups.approvalsGroupMemList[index].userId = null;
                }, 200);
            }
        }
    }

    /**Cancel Changes */
    cancelEdit(){
        this.loadAll();
    }

    /**Update Members detail assigned to approval group */
    updateMemberDetail(memberDetail) {
        if (memberDetail.seq == null || memberDetail.userId == null ||
            memberDetail.startDate == null) {
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        } else {
            /* memberDetail.startDate = memberDetail.startDate.setDate(memberDetail.startDate.getDate()+1);
            memberDetail.startDate = new Date(memberDetail.startDate);
            memberDetail.endDate = memberDetail.endDate.setDate(memberDetail.endDate.getDate()+1);
            memberDetail.endDate = new Date(memberDetail.endDate); */
            if (memberDetail.id) {
                /**Update Member Detail */
                // console.log('Update memberDetail\n' + JSON.stringify(memberDetail));
                this.approvalGroupMembersService.update(memberDetail).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success('Success!', 'User Successfully Updated');
                    this.loadAll();
                });
            } else {
                /**Add New Member Detail */
                memberDetail.groupId = this.approvalGroups.id;
              //  memberDetail.createdBy = this.UserData.id;
               // memberDetail.lastUpdatedBy = this.UserData.id;
                memberDetail.lastUpdatedDate = new Date();
                memberDetail.createdDate = new Date();
                // console.log('Add new memberDetail\n' + JSON.stringify(memberDetail));
                this.approvalGroupMembersService.update(memberDetail).subscribe((res: any) => {
                    // console.log('res\n' + JSON.stringify(res));
                    this.notificationsService.success('Success!', 'User Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    /**Edit Members detail assigned to approval group */
    editMemberDetail(i){
        let throwValidation:any = false;
        for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
            if(this.approvalGroups.approvalsGroupMemList[i].columnEdit == true){
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.notificationsService.error('Warning!','Please save row before editing');
        }else{
            this.approvalGroups.approvalsGroupMemList[i].columnEdit = true;
        }
    }

    /**Delete Row */
    deleteRow(index){
        if(this.approvalGroups.approvalsGroupMemList.length < 2){
            this.notificationsService.error('Warning!','Atleast one row is mandatory');
        }else{
            this.approvalGroups.approvalsGroupMemList.splice(index, 1);
        }
    }

    /**Add Row in Members Detail */
    addNewMembers(value) {
        let throwValidation: any = false;
        if (this.approvalGroups.approvalsGroupMemList) {
            for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                if (this.approvalGroups.approvalsGroupMemList[i].seq == null || this.approvalGroups.approvalsGroupMemList[i].userId == null ||
                    this.approvalGroups.approvalsGroupMemList[i].startDate == null) {
                    throwValidation = true;
                } else if (this.approvalGroups.approvalsGroupMemList[i].columnEdit == true) {
                    throwValidation = 'columnEditTrue';
                }
            }
        } else {
            this.approvalGroups.approvalsGroupMemList = [];
        }
        if (throwValidation == 'columnEditTrue' && this.isViewOnly == true && this.btnEditAll == false && value != 'multiple') {
            this.notificationsService.error(
                'Warning!',
                'Please save row before adding new'
            );
        } else if (throwValidation == true && value != 'multiple') {
            this.notificationsService.error(
                'Warning!',
                'Fill mandatory fields'
            );
        } else {
            if (value == 'multiple') {
                /**For Displaying Name in drag and drop */
                if(this.approvalGroups.approvalsGroupMemList.length>0){
                    for(let i=0; i<this.approvalGroups.approvalsGroupMemList.length; i++){
                        for(let j=0; j<this.users.length; j++){
                            if(this.approvalGroups.approvalsGroupMemList[i].userId == this.users[j].id){
                                this.approvalGroups.approvalsGroupMemList[i].userName = this.users[j].firstName + ' ' + this.users[j].lastName;
                            }
                        }
                    }
                }
                // console.log('In drag function creation\n'+JSON.stringify(this.approvalGroups.approvalsGroupMemList));
                /* Preventing - Showing one empty list in creation form */
                if (this.isCreateNew == true && !this.approvalGroups.approvalsGroupMemList[0].userId) {
                    this.approvalGroups.approvalsGroupMemList = [];
                }

                /**Empty to Prevent duplicates */
                this.unassignedUsers = [];
                this.selectedUsers = [];

                /**Filtering Unassigned users */
                this.users.forEach(unassigneduser => {
                    let occured: boolean = false;
                    this.approvalGroups.approvalsGroupMemList.forEach(assignedUser => {
                        if (unassigneduser.id === assignedUser.userId) {
                            occured = true;
                        }
                    });
                    if (occured) {
                        //Do Nothing
                    }
                    else {
                        this.unassignedUsers.push(unassigneduser);
                    }
                });

                /**Open Dialog/DND for Multiple Members */
                this.apprGrpMemDialog = true;
                /**Reset Start and End Date for Dialog*/
                this.startDateAllMember = undefined;
                this.endDateAllMember = undefined;

            } else {
                /**One row at a time */
                let obj = {
                    'seq': null,
                    'userId': null,
                    'startDate': null,
                    'endDate': null,
                    'columnEdit': true
                };
                this.approvalGroups.approvalsGroupMemList.push(obj);
            }
        }
    }

    /**Save Action Drag and Drop members to group */
    confirmApprGrpMem() {
        if(this.selectedUsers.length < 1){
            this.notificationsService.error('Warning!', 'Drag atleast one member');
        }else if(!this.startDateAllMember){
            this.notificationsService.error('Warning!', 'Start Date is mandatory');
        }else{
            // console.log('this.selectedUsers\n'+JSON.stringify(this.selectedUsers));
            // console.log('start and end date' + this.startDateAllMember, this.endDateAllMember);
            for (let i = 0; i < this.selectedUsers.length; i++) {
                let obj = {
                    'groupId': this.approvalGroups.id,
                    'seq': this.approvalGroups.approvalsGroupMemList.length+1,
                    'userId': this.selectedUsers[i].id,
                    'userName': this.selectedUsers[i].firstName +' '+this.selectedUsers[i].lastName,
                    'startDate': this.startDateAllMember,
                    'endDate': this.endDateAllMember,
                    'columnEdit': true,
                    'deleteNewAdded': true
                };
                this.approvalGroups.approvalsGroupMemList.push(obj);
            }
            this.apprGrpMemDialog = false;
            if(this.isCreateNew != true){
                for(let i=0; i<this.approvalGroups.approvalsGroupMemList.length; i++){
                    if(this.approvalGroups.approvalsGroupMemList[i].columnEdit != true){
                        this.approvalGroups.approvalsGroupMemList[i].columnEdit = true;
                    }
                }
                this.btnEditAll = true;
            }
            // console.log('this.approvalGroups.approvalsGroupMemList\n'+JSON.stringify(this.approvalGroups.approvalsGroupMemList));
        }
    }

    /**Function when dialog closed */
    dialogHide(){
        if(this.isCreateNew == true && this.approvalGroups.approvalsGroupMemList.length < 1){
            /**First empty row by default while creating */
            let obj = {
                'seq': null,
                'userId': null,
                'startDate': null,
                'endDate': null,
                'columnEdit': true
            };
            this.approvalGroups.approvalsGroupMemList.push(obj);
        }
    }

    /**Edit all members at once */
    editAllMembers(action){
        if(action == 'EditAll'){
            /**EditAll Button */
                this.btnEditAll = true;
                for(let i=0; i<this.approvalGroups.approvalsGroupMemList.length; i++){
                    this.approvalGroups.approvalsGroupMemList[i].columnEdit = true;
                }
        }else if(action == 'SaveAll'){
            /**SaveAll Button */
            let throwValidation = false;
            for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                if (this.approvalGroups.approvalsGroupMemList[i].seq == null || this.approvalGroups.approvalsGroupMemList[i].userId == null ||
                    this.approvalGroups.approvalsGroupMemList[i].startDate == null) {
                    throwValidation = true;
                }
            }
            if(throwValidation == true){
                this.notificationsService.error('Warning!', 'Fill Mandatory fields');
            }else{
                let obj = {
                    'id': this.approvalGroups.id,
                    'approvalsGroupMemList': this.approvalGroups.approvalsGroupMemList
                }
                // console.log('update all members - obj\n'+JSON.stringify(obj));
                this.approvalGroupsService.postApprovalGroupsAndGrpMembers(obj).subscribe((res: any)=>{
                    // console.log('res\n'+JSON.stringify(res));
                    this.notificationsService.success('Success!', 'Group Users Successfully Updated');
                    this.btnEditAll = false;
                    this.loadAll();
                });
            }
        }else{
            /**Cancel Button */
            this.btnEditAll = false;
            this.loadAll();
        }
    }

    /*Delete Member Detail of approval groups */
    deleteMemberDetail(memberDetail){
        // console.log('memberDetail\n'+JSON.stringify(memberDetail));
        this.approvalGroupMembersService.delete(memberDetail.id).subscribe((res: any)=>{
            this.notificationsService.success('Success!', memberDetail.userName + ' Successfully deleted');
            this.loadAll();
        });
    }

    /**Validate Sequence */
    sequenceValidate(value, i) {
        this.approvalGroups.approvalsGroupMemList.forEach((user, index) => {
            if (value == user.seq && i != index) {
                this.notificationsService.error('Warning', 'Sequence already assigned to other user');
                setTimeout(() => {
                    this.approvalGroups.approvalsGroupMemList[i].seq = null;
                }, 200);
            }
        });
    }

    isNameExist(name, id) {
        if (name)
        this.approvalGroupsService.checkApprovalGroupIsExist(name, id).subscribe(res => {
            this.nameExist = res.result != 'No Duplicates Found' ? true : false;
        });
    }
}

    /* load(id) {
        this.approvalGroupsService.find(id).subscribe((approvalGroups) => {
            this.approvalGroups = approvalGroups;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApprovalGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'approvalGroupsListModification',
            (response) => this.load(this.approvalGroups.id)
        );
    } */
