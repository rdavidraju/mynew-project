import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Roles } from './roles.model';
import { RolesService } from './roles.service';

@Injectable()
export class RolesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private rolesService: RolesService

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
                this.rolesService.find(id).subscribe((roles) => {
                    if (roles.startDate) {
                        roles.startDate = {
                            year: roles.startDate.getFullYear(),
                            month: roles.startDate.getMonth() + 1,
                            day: roles.startDate.getDate()
                        };
                    }
                    if (roles.endDate) {
                        roles.endDate = {
                            year: roles.endDate.getFullYear(),
                            month: roles.endDate.getMonth() + 1,
                            day: roles.endDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.rolesModalRef(component, roles);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.rolesModalRef(component, new Roles());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    rolesModalRef(component: Component, roles: Roles): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.roles = roles;
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
