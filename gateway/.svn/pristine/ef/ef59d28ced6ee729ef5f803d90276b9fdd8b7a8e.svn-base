//#region Import section
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';
import { LookUpCode } from '../look-up-code/look-up-code.model';
import { NotificationsService } from 'angular2-notifications-lite';
import { CommonService } from '../../entities/common.service';
import { Process } from './express-mode.model';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import "rxjs/add/operator/takeWhile";
declare var $: any;
declare var jQuery: any;
//#endregion

@Component({
    selector: 'jhi-express-mode',
    templateUrl: './express-mode.component.html'
})
export class ExpressModeComponent implements OnDestroy, OnInit {
    //#region Parameters section
    private alive: boolean = true;      //Unsubscribe variable
    items: any[]=[{label: 'File Drop'},{label: 'Data Views'},{label: 'Rules'},{label: 'Result'}];
    msgs: any[] = [];
    activeIndex: number = 0;   
    currentProcess = new Process();
    
    //#endregion

    constructor(
        private eventManager: JhiEventManager,
        private notificationsService: NotificationsService,
        private route: ActivatedRoute,
        private router: Router,
        private commonService: CommonService,
    ) {
        router.navigate(['/express-mode', {outlets: {'content': ['file-drop']}}]);
    }
    
    ngOnInit() {
        $(".split-example").css({
            'height': 'auto',
            'min-height': (this.commonService.screensize().height - 130) +'px'
          });
    }

    ngOnDestroy() {
        this.alive = false;
    }

    checkProcessName(){

    }
}
