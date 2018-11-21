import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Project } from './project.model';
import { ProjectService } from './project.service';

@Injectable()
export class ProjectPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private projectService: ProjectService

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
                this.projectService.find(id).subscribe((project) => {
                    if (project.startDate) {
                        project.startDate = {
                            year: project.startDate.getFullYear(),
                            month: project.startDate.getMonth() + 1,
                            day: project.startDate.getDate()
                        };
                    }
                    if (project.endDate) {
                        project.endDate = {
                            year: project.endDate.getFullYear(),
                            month: project.endDate.getMonth() + 1,
                            day: project.endDate.getDate()
                        };
                    }
                    project.creationDate = this.datePipe
                        .transform(project.creationDate, 'yyyy-MM-ddThh:mm');
                    project.lastUpdatedDate = this.datePipe
                        .transform(project.lastUpdatedDate, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.projectModalRef(component, project);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectModalRef(component, new Project());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    projectModalRef(component: Component, project: Project): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.project = project;
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
