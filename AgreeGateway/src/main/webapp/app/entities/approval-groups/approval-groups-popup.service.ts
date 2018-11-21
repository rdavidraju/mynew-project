import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ApprovalGroups } from './approval-groups.model';
import { ApprovalGroupsService } from './approval-groups.service';

@Injectable()
export class ApprovalGroupsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private approvalGroupsService: ApprovalGroupsService

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
                this.approvalGroupsService.find(id).subscribe((approvalGroups) => {
                    if (approvalGroups.startDate) {
                        approvalGroups.startDate = {
                            year: approvalGroups.startDate.getFullYear(),
                            month: approvalGroups.startDate.getMonth() + 1,
                            day: approvalGroups.startDate.getDate()
                        };
                    }
                    if (approvalGroups.endDate) {
                        approvalGroups.endDate = {
                            year: approvalGroups.endDate.getFullYear(),
                            month: approvalGroups.endDate.getMonth() + 1,
                            day: approvalGroups.endDate.getDate()
                        };
                    }
                    approvalGroups.createdDate = this.datePipe
                        .transform(approvalGroups.createdDate, 'yyyy-MM-ddThh:mm');
                    approvalGroups.lastUpdatedDate = this.datePipe
                        .transform(approvalGroups.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.approvalGroupsModalRef(component, approvalGroups);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.approvalGroupsModalRef(component, new ApprovalGroups());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    approvalGroupsModalRef(component: Component, approvalGroups: ApprovalGroups): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.approvalGroups = approvalGroups;
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
