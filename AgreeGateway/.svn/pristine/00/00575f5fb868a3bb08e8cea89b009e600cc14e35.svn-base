import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { SourceProfiles } from './source-profiles.model';
import { SourceProfilesService } from './source-profiles.service';

@Injectable()
export class SourceProfilesPopupService {
    private ngbModalRef: NgbModalRef;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private sourceProfilesService: SourceProfilesService

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
                this.sourceProfilesService.find(id).subscribe((sourceProfiles) => {
                    if (sourceProfiles.startDate) {
                        sourceProfiles.startDate = {
                            year: sourceProfiles.startDate.getFullYear(),
                            month: sourceProfiles.startDate.getMonth() + 1,
                            day: sourceProfiles.startDate.getDate()
                        };
                    }
                    if (sourceProfiles.endDate) {
                        sourceProfiles.endDate = {
                            year: sourceProfiles.endDate.getFullYear(),
                            month: sourceProfiles.endDate.getMonth() + 1,
                            day: sourceProfiles.endDate.getDate()
                        };
                    }
                    sourceProfiles.createdDate = this.datePipe
                        .transform(sourceProfiles.createdDate, 'yyyy-MM-ddThh:mm');
                    sourceProfiles.lastUpdatedDate = this.datePipe
                        .transform(sourceProfiles.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.sourceProfilesModalRef(component, sourceProfiles);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.sourceProfilesModalRef(component, new SourceProfiles());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    sourceProfilesModalRef(component: Component, sourceProfiles: SourceProfiles): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.sourceProfiles = sourceProfiles;
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
