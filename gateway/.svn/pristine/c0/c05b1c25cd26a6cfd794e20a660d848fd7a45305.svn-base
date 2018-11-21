import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Rx';
import { UserService } from '../../shared';
import { Router } from '@angular/router';
import { ApprovalGroups } from './approval-groups.model';
import { ApprovalGroupsService } from './approval-groups.service';
import { ApprovalGroupMembersService } from '../approval-group-members/approval-group-members.service';
import { CommonService } from '../common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $: any;

@Component({
    selector: 'jhi-approval-groups-detail',
    templateUrl: './approval-groups-detail.component.html'
})
export class ApprovalGroupsDetailComponent implements OnInit, OnDestroy {

    approvalGroups = new ApprovalGroups();
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    users: any;
    unassignedUsers: any = [];
    selectedUsers: any = [];
    apprGrpMemDialog = false;
    btnEditAll = false;
    startDateAllMember: any;
    endDateAllMember: any;
    dndToggle = false;
    nameExist = false;
    private unsubscribe: Subject<void> = new Subject();
    currentDate = new Date();
    loadDocument = false;

    constructor(
        private approvalGroupsService: ApprovalGroupsService,
        private route: ActivatedRoute,
        private router: Router,
        private userService: UserService,
        private approvalGroupMembersService: ApprovalGroupMembersService,
        private cs: CommonService
    ) {}

    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.unsubscribe.next();
        this.unsubscribe.complete();
    }

    /**Load Approval groups details */
    loadAll(){
        this.route.params.takeUntil(this.unsubscribe).subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');
            if (params['id']) {
                this.approvalGroupsService.getApprovalGroupsAndGrpMembers(params['id']).takeUntil(this.unsubscribe).subscribe((res) => {
                        this.approvalGroups = res;
                        // Get List of Users
                        this.getUsers();
                        this.isCreateNew = false;
                        this.loadDocument = true;
                        if (url.endsWith('edit')) {
                            this.isEdit = true;
                            // Focusing on first element
                            $('#groupName').focus();
                        } else {
                            this.isViewOnly = true;
                        }
                    }
                );
            } else {
                this.isCreateNew = true;
                this.loadDocument = true;
                // Get List of Users
                this.getUsers();
                this.approvalGroups.startDate = new Date();
                // Focusing on first element
                $('#groupName').focus();
                // First empty row by default while creating
                const obj = {
                    'seq': 1,
                    'userId': null,
                    'startDate': new Date(),
                    'endDate': null,
                    'columnEdit': true
                };
                this.approvalGroups.approvalsGroupMemList.push(obj);
            }
        });
    }
    
    /**Save Approval Groups and its members */
    saveApprGroup() {
        this.approvalGroupsService.checkApprovalGroupIsExist(this.approvalGroups.groupName, this.approvalGroups.id).takeUntil(this.unsubscribe).subscribe((res) => {
            this.nameExist = res.result != 'No Duplicates Found' ? true : false;
            if (!this.nameExist) {
                let link: any = '';
                let membersEmpty: any = false;
                if (this.approvalGroups.id) {
                    // Update Approval Group
                    this.approvalGroupsService.update(this.approvalGroups).takeUntil(this.unsubscribe).subscribe((upRes: any) => {
                        this.cs.success('Success!','"' + upRes.groupName + '"' + ' Successfully Updated');
                        if (upRes.id) {
                            link = ['/groups', { outlets: { 'content': upRes.id + '/details' } }];
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
                    // Create New Approval Group
                    for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                        if (this.approvalGroups.approvalsGroupMemList[i].seq == null || this.approvalGroups.approvalsGroupMemList[i].userId == null ||
                            this.approvalGroups.approvalsGroupMemList[i].startDate == null) {
                            membersEmpty = true;
                        }
                    }
                    if (membersEmpty == true) {
                        this.cs.error('Warning!','Fill mandatory fields');
                    } else {
                        this.approvalGroupsService.postApprovalGroupsAndGrpMembers(this.approvalGroups).takeUntil(this.unsubscribe).subscribe((postRes: any) => {
                            this.cs.success('Success!','Group Successfully Created');
                            if (postRes.id) {
                                link = ['/groups', { outlets: { 'content': postRes.id + '/details' } }];
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
                this.cs.error('Warning!','Name is mandatory');
            }else if(this.approvalGroups.startDate == null){
                this.cs.error('Warning!','Start date is mandatory');
            }
        }else{
            this.cs.error('Warning!','Fill mandatory fields');
        }
    }

    /**Get List of Users with activeFlag true */
    getUsers() {
        this.userService.getUsersByTenantId().takeUntil(this.unsubscribe).subscribe((res: any) => {
            this.users = res;
        });
    }

    /**Check if user already assigned */
    memberChange(value, index) {
        let throwValidation: any = false;
        if (this.approvalGroups.approvalsGroupMemList.length) {
            for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                if (value == this.approvalGroups.approvalsGroupMemList[i].userId && index != i) {
                    throwValidation = true
                }
            }
            if (throwValidation == true) {
                this.cs.error('Warning', 'User already assigned to this group');
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
            this.cs.error('Warning!','Fill mandatory fields');
        } else {
            if (memberDetail.id) {
                // Update Member Detail
                this.approvalGroupMembersService.update(memberDetail).takeUntil(this.unsubscribe).subscribe(() => {
                    this.cs.success('Success!', 'User Successfully Updated');
                    this.loadAll();
                });
            } else {
                // Add New Member Detail
                memberDetail.groupId = this.approvalGroups.id;
                memberDetail.lastUpdatedDate = new Date();
                memberDetail.createdDate = new Date();
                this.approvalGroupMembersService.update(memberDetail).takeUntil(this.unsubscribe).subscribe(() => {
                    this.cs.success('Success!', 'User Added Successfully');
                    this.loadAll();
                });
            }
        }
    }

    /**Edit Members detail assigned to approval group */
    editMemberDetail(ind){
        let throwValidation:any = false;
        for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
            if(this.approvalGroups.approvalsGroupMemList[i].columnEdit == true){
                throwValidation = 'columnEditTrue';
            }
        }
        if (throwValidation == 'columnEditTrue') {
            this.cs.error('Warning!','Please save row before editing');
        } else{
            this.approvalGroups.approvalsGroupMemList[ind].columnEdit = true;
        }
    }

    /**Delete Row */
    deleteRow(index){
        if(this.approvalGroups.approvalsGroupMemList.length < 2){
            this.cs.error('Warning!','Atleast one row is mandatory');
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
            this.cs.error('Warning!','Please save row before adding new');
        } else if (throwValidation == true && value != 'multiple') {
            this.cs.error('Warning!','Fill mandatory fields');
        } else {
            if (value == 'multiple') {
                // For Displaying Name in drag and drop
                if(this.approvalGroups.approvalsGroupMemList.length>0){
                    for(let i=0; i<this.approvalGroups.approvalsGroupMemList.length; i++){
                        for(let j=0; j<this.users.length; j++){
                            if(this.approvalGroups.approvalsGroupMemList[i].userId == this.users[j].id){
                                this.approvalGroups.approvalsGroupMemList[i].userName = this.users[j].firstName + ' ' + this.users[j].lastName;
                            }
                        }
                    }
                }
                // Preventing - Showing one empty list in creation form
                if (this.isCreateNew == true && !this.approvalGroups.approvalsGroupMemList[0].userId) {
                    this.approvalGroups.approvalsGroupMemList = [];
                }

                // Empty to Prevent duplicates
                this.unassignedUsers = [];
                this.selectedUsers = [];

                // Filtering Unassigned users
                this.users.forEach((unassigneduser) => {
                    let occured = false;
                    this.approvalGroups.approvalsGroupMemList.forEach((assignedUser) => {
                        if (unassigneduser.id === assignedUser.userId) {
                            occured = true;
                        }
                    });
                    if (occured) {
                        //Do Nothing
                    } else {
                        this.unassignedUsers.push(unassigneduser);
                    }
                });

                // Open Dialog/DND for Multiple Members
                this.apprGrpMemDialog = true;
                // Reset Start and End Date for Dialog
                this.startDateAllMember = undefined;
                this.endDateAllMember = undefined;

            } else {
                // One row at a time
                const obj = {
                    'seq': this.approvalGroups.approvalsGroupMemList.length+1,
                    'userId': null,
                    'startDate': new Date(),
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
            this.cs.error('Warning!', 'Drag atleast one member');
        }else if(!this.startDateAllMember){
            this.cs.error('Warning!', 'Start Date is mandatory');
        }else{
            for (let i = 0; i < this.selectedUsers.length; i++) {
                const obj = {
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
        }
    }

    /**Function when dialog closed */
    dialogHide(){
        if(this.isCreateNew == true && this.approvalGroups.approvalsGroupMemList.length < 1){
            // First empty row by default while creating
            const obj = {
                'seq': 1,
                'userId': null,
                'startDate': new Date(),
                'endDate': null,
                'columnEdit': true
            };
            this.approvalGroups.approvalsGroupMemList.push(obj);
        }
    }

    /**Edit all members at once */
    editAllMembers(action){
        if(action == 'EditAll'){
            // EditAll Button
                this.btnEditAll = true;
                for(let i=0; i<this.approvalGroups.approvalsGroupMemList.length; i++){
                    this.approvalGroups.approvalsGroupMemList[i].columnEdit = true;
                }
        }else if(action == 'SaveAll'){
            // SaveAll Button
            let throwValidation = false;
            for (let i = 0; i < this.approvalGroups.approvalsGroupMemList.length; i++) {
                if (this.approvalGroups.approvalsGroupMemList[i].seq == null || this.approvalGroups.approvalsGroupMemList[i].userId == null ||
                    this.approvalGroups.approvalsGroupMemList[i].startDate == null) {
                    throwValidation = true;
                }
            }
            if(throwValidation == true){
                this.cs.error('Warning!', 'Fill Mandatory fields');
            }else{
                const obj = {
                    'id': this.approvalGroups.id,
                    'approvalsGroupMemList': this.approvalGroups.approvalsGroupMemList
                }
                this.approvalGroupsService.postApprovalGroupsAndGrpMembers(obj).takeUntil(this.unsubscribe).subscribe(()=>{
                    this.cs.success('Success!', 'Group Users Successfully Updated');
                    this.btnEditAll = false;
                    this.loadAll();
                });
            }
        }else{
            // Cancel Button
            this.btnEditAll = false;
            this.loadAll();
        }
    }

    /*Delete Member Detail of approval groups */
    deleteMemberDetail(memberDetail) {
        if (this.approvalGroups.approvalsGroupMemList.length < 2)  {
            this.cs.error('Warning!','Atleast one user is mandatory');
        } else {
            this.approvalGroupMembersService.delete(memberDetail.id).takeUntil(this.unsubscribe).subscribe((res: any)=>{
                this.cs.success('Success!', memberDetail.userName + ' Successfully deleted');
                this.loadAll();
            });
        }
    }

    /**Validate Sequence */
    sequenceValidate(value, i) {
        if (value > 0) {
            this.approvalGroups.approvalsGroupMemList.forEach((user, index) => {
                if (value == user.seq && i != index) {
                    this.cs.error('Warning', 'Sequence already assigned to other user');
                    setTimeout(() => {
                        this.approvalGroups.approvalsGroupMemList[i].seq = null;
                    }, 200);
                }
            });
        }
    }

    isNameExist(name, id) {
        if (name) {
            this.approvalGroupsService.checkApprovalGroupIsExist(name, id).takeUntil(this.unsubscribe).subscribe((res) => {
                this.nameExist = res.result != 'No Duplicates Found' ? true : false;
            });
        }
    }
}