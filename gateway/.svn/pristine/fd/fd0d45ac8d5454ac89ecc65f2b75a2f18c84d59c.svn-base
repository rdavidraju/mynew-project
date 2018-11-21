import { Component, OnInit } from '@angular/core';
import { HierarchyService } from './hierarchy.service';
import { NotificationsService } from 'angular2-notifications-lite';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'hierarchy',
    templateUrl: './hierarchy.component.html',
    styleUrls: ['hierarchy.scss']
})

export class HierarchyComponent implements OnInit{
    hierarchy:any = [];
    selectedNode:any;

    constructor(
        private hierarchyService: HierarchyService,
        private notificationsService: NotificationsService
    ){}

    ngOnInit(){
        this.hierarchyService.getHierarchy().subscribe((res)=>{
            if(res.userId)
            this.hierarchy.push(res);
            // console.log(this.hierarchy);
        },
        err => {
            this.notificationsService.error('Error', 'Something Went Wrong ');
        });
    }

    onNodeSelect(data){
        /* console.log(data); */
    }

}