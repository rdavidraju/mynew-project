import { Component, Input,OnInit, OnDestroy, Inject ,Output,EventEmitter} from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { JhiDateUtils } from 'ng-jhipster';
import { SelectItem } from 'primeng/primeng';
import { PageEvent } from '@angular/material';
import { NotificationsService } from 'angular2-notifications-lite';
import { MdDialog } from '@angular/material';
declare var $: any;
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { MultiSelectDropDownSettings } from '.';

@Component({
    selector: 'multiselect-dropdown',
    templateUrl: './multiselect-dropdown-component.html'
})
export class MultiSelectDropdownComponent implements OnInit, OnDestroy {
    @Input() settings:MultiSelectDropDownSettings=new MultiSelectDropDownSettings();
   @Input() data=[];
   @Output() change: EventEmitter<any> = new EventEmitter<number>();
    selectedObj = [];
    selectAllstates() {
        if (!this.selectAll) {
            this.data.forEach(state => {
                state.checked = false;
            })
            this.selectedObj = [];
        }
        else
        {
            this.selectedObj = [];
            this.data.forEach(state => {
                state.checked = true;
                this.selectedObj.push(state);
            });
        }
            
            this.change.emit(this.selectedObj);
    }
    selectObject(state)
    {
        this.selectedObj=[];
        this.selectedObj.push(state);
        this.change.emit(this.selectedObj);
    }
    pushObj(state, i) {

        if (!state.checked) {
            this.selectAll = false;
            let i:number=0;
        this.selectedObj.forEach(item=>{
            if(!item.checked)
            {
                this.selectedObj.splice(i,1);
                i=i-1;
            }
            i=i+1;
        });
        }
        else
            this.selectedObj.push(state);
        console.log('this.selectedObj' + JSON.stringify(this.selectedObj));
    }
    
    // usStates =
    //     [
    //         { name: 'AMERICAN SAMOA', abbreviation: 'AS', checked: false },
    //         { name: 'ARIZONA', abbreviation: 'AZ', checked: false },
    //         { name: 'ARKANSAS', abbreviation: 'AR', checked: false },
    //         { name: 'CALIFORNIA', abbreviation: 'CA', checked: false },
    //         { name: 'COLORADO', abbreviation: 'CO', checked: false },
    //         { name: 'CONNECTICUT', abbreviation: 'CT', checked: false },
    //         { name: 'DELAWARE', abbreviation: 'DE', checked: false },
    //         { name: 'DISTRICT OF COLUMBIA', abbreviation: 'DC', checked: false },
    //         { name: 'FEDERATED STATES OF MICRONESIA', abbreviation: 'FM', checked: false },
    //         { name: 'FLORIDA', abbreviation: 'FL', checked: false },
    //         { name: 'GEORGIA', abbreviation: 'GA', checked: false },
    //         { name: 'GUAM', abbreviation: 'GU', checked: false },
    //         { name: 'HAWAII', abbreviation: 'HI', checked: false },
    //         { name: 'IDAHO', abbreviation: 'ID', checked: false },
    //         { name: 'ILLINOIS', abbreviation: 'IL', checked: false },
    //         { name: 'INDIANA', abbreviation: 'IN', checked: false },
    //         { name: 'IOWA', abbreviation: 'IA', checked: false },
    //         { name: 'KANSAS', abbreviation: 'KS', checked: false },
    //         { name: 'KENTUCKY', abbreviation: 'KY', checked: false },
    //         { name: 'LOUISIANA', abbreviation: 'LA', checked: false },
    //         { name: 'MAINE', abbreviation: 'ME', checked: false },
    //         { name: 'MARSHALL ISLANDS', abbreviation: 'MH', checked: false },
    //         { name: 'MARYLAND', abbreviation: 'MD', checked: false },
    //         { name: 'MASSACHUSETTS', abbreviation: 'MA', checked: false },
    //         { name: 'MICHIGAN', abbreviation: 'MI', checked: false },
    //         { name: 'MINNESOTA', abbreviation: 'MN', checked: false },
    //         { name: 'MISSISSIPPI', abbreviation: 'MS', checked: false },
    //         { name: 'MISSOURI', abbreviation: 'MO', checked: false },
    //         { name: 'MONTANA', abbreviation: 'MT', checked: false },
    //         { name: 'NEBRASKA', abbreviation: 'NE', checked: false },
    //         { name: 'NEVADA', abbreviation: 'NV', checked: false },
    //         { name: 'NEW HAMPSHIRE', abbreviation: 'NH', checked: false },
    //         { name: 'NEW JERSEY', abbreviation: 'NJ', checked: false },
    //         { name: 'NEW MEXICO', abbreviation: 'NM', checked: false },
    //         { name: 'NEW YORK', abbreviation: 'NY', checked: false },
    //         { name: 'NORTH CAROLINA', abbreviation: 'NC', checked: false },
    //         { name: 'NORTH DAKOTA', abbreviation: 'ND', checked: false },
    //         { name: 'NORTHERN MARIANA ISLANDS', abbreviation: 'MP', checked: false },
    //         { name: 'OHIO', abbreviation: 'OH', checked: false },
    //         { name: 'OKLAHOMA', abbreviation: 'OK', checked: false },
    //         { name: 'OREGON', abbreviation: 'OR', checked: false },
    //         { name: 'PALAU', abbreviation: 'PW', checked: false },
    //         { name: 'PENNSYLVANIA', abbreviation: 'PA', checked: false },
    //         { name: 'PUERTO RICO', abbreviation: 'PR', checked: false },
    //         { name: 'RHODE ISLAND', abbreviation: 'RI', checked: false },
    //         { name: 'SOUTH CAROLINA', abbreviation: 'SC', checked: false },
    //         { name: 'SOUTH DAKOTA', abbreviation: 'SD', checked: false },
    //         { name: 'TENNESSEE', abbreviation: 'TN', checked: false },
    //         { name: 'TEXAS', abbreviation: 'TX', checked: false },
    //         { name: 'UTAH', abbreviation: 'UT', checked: false },
    //         { name: 'VERMONT', abbreviation: 'VT', checked: false },
    //         { name: 'VIRGIN ISLANDS', abbreviation: 'VI', checked: false },
    //         { name: 'VIRGINIA', abbreviation: 'VA', checked: false },
    //         { name: 'WASHINGTON', abbreviation: 'WA', checked: false },
    //         { name: 'WEST VIRGINIA', abbreviation: 'WV', checked: false },
    //         { name: 'WISCONSIN', abbreviation: 'WI', checked: false },
    //         { name: 'WYOMING', abbreviation: 'WY', checked: false }
    //     ];

    // <li> template
    selectAll: boolean = false;
    constructor(
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private paginationUtil: JhiPaginationUtil,
        public dialog: MdDialog,
        public notificationsService: NotificationsService
    ) {
    }
    ngOnDestroy() {
    }
    ngOnInit() {
        console.log('loaded multiselect dropdown component');
        $('.dropdown-container')
            .on('click', '.dropdown-button', function () {
                $('.dropdown-list').toggle();
            })
            .on('input', '.dropdown-search', function () {
                var target = $(this);
                var search = target.val().toLowerCase();

                if (!search) {
                    $('li').show();
                    return false;
                }

                $('li').each(function () {
                    var text = $(this).text().toLowerCase();
                    var match = text.indexOf(search) > -1;
                    $(this).toggle(match);
                });
            })
            .on('change', '[type="checkbox"]', function () {
                var numChecked = $('[type="checkbox"]:checked').length;
                $('.quantity').text(numChecked || 'Any');
            });

    }
    toggleSelect()
    {
        
    }
}