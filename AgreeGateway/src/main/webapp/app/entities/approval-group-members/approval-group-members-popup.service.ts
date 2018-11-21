import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ApprovalGroupMembers } from './approval-group-members.model';
import { ApprovalGroupMembersService } from './approval-group-members.service';

@Injectable()
export class ApprovalGroupMembersPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private approvalGroupMembersService: ApprovalGroupMembersService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.approvalGroupMembersService.find(id).subscribe((approvalGroupMembers) => {
                    if (approvalGroupMembers.startDate) {
                        approvalGroupMembers.startDate = {
                            year: approvalGroupMembers.startDate.getFullYear(),
                            month: approvalGroupMembers.startDate.getMonth() + 1,
                            day: approvalGroupMembers.startDate.getDate()
                        };
                    }
                    if (approvalGroupMembers.endDate) {
                        approvalGroupMembers.endDate = {
                            year: approvalGroupMembers.endDate.getFullYear(),
                            month: approvalGroupMembers.endDate.getMonth() + 1,
                            day: approvalGroupMembers.endDate.getDate()
                        };
                    }
                    approvalGroupMembers.createdDate = this.datePipe
                        .transform(approvalGroupMembers.createdDate, 'yyyy-MM-ddThh:mm');
                    approvalGroupMembers.lastUpdatedDate = this.datePipe
                        .transform(approvalGroupMembers.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.approvalGroupMembersModalRef(component, approvalGroupMembers);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.approvalGroupMembersModalRef(component, new ApprovalGroupMembers());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    approvalGroupMembersModalRef(component: Component, approvalGroupMembers: ApprovalGroupMembers): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.approvalGroupMembers = approvalGroupMembers;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
