import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';

import { FileTemplates } from './file-templates.model';
import { FileTemplatesPopupService } from './file-templates-popup.service';
import { FileTemplatesService } from './file-templates.service';

@Component({
    selector: 'file-templates-nav',
    templateUrl: './file-templates-nav.component.html'
})
export class FileTemplatesNavComponent {

    fileTemplates: FileTemplates;
    currentstate:string;
    id:any=1;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private fileTemplatesService: FileTemplatesService,
        private eventManager: JhiEventManager
    ) {
    }

    loadAll() {
                this.currentstate = "Template List";
            }
     newTemplate(){
        this.currentstate = "Create Template"
    }
    
}
