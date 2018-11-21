import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';
import { Response } from '@angular/http';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import {RuleGroupService} from './rule-group.service';
import { CommonService } from '../common.service';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';

declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'rule-group-side-bar',
    templateUrl: './rule-Group-side-bar.component.html'
})
export class RuleGroupSideBarComponent implements OnInit {

    authorities: any[];
    isSaving: boolean;
    groupbynav:true;

    rulePurposeBody:any;
    currentrulePurpose: string='';

    expandRuleGroup = [] ;
    ruleGroupBody = [];
    
    expandRuleName = [] ;
    ruleNameBody = [];
    
    filterColumns : any =[
        {
            "colName":"source",
            "dispCol":"Source"
        },
        {
            "colName":"region",
            "dispCol":"Region"
        }
        ];
    selectedCol :any = this.filterColumns[0].colName;
    sideBarData:any=[];
    
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private router: Router,
        private ruleGroupService : RuleGroupService,
        private commonService: CommonService
    ) {     
    }

    ngOnInit() {
        console.log('oninit sdbar');
        this.commonService.callFunction();
        this.commonService.screensize();
        $(".addHeight").css({
          'height': 'auto',
          'min-height': (this.commonService.screensize().height - 130) +'px'
        });
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
       // this.selectedCol = 'Source' ;
         this.ruleGroupService.fetchSideBarData('rulePurpose').subscribe(        
            (res:Response)=>{
                this.sideBarData= res;

                //Sidebar List Default First Open
                if (this.sideBarData.length == 0) {
                    //Do nothing
                } else {
                    this.sideBarData[0].firstOpened = true;
                }

                //console.log('this.sbr in ngonint'+JSON.stringify(this.sideBarData));
                if(this.sideBarData[0] && this.sideBarData[0].rulePurpose)
                {
                    this.rulePurposeBody = this.sideBarData[0].rulePurpose;
                    this.currentrulePurpose = this.sideBarData[0].rulePurpose;
                }
            });
         
         $( document ).on( 'click', '.search-icon-sidebar', function() {
             if ( $( ".sbSearch md-input-container" ).hasClass( "hidethis" ) ) {
                 $( ".sbSearch md-input-container" ).removeClass( "hidethis" );
                 $( ".sbSearch md-input-container" ).addClass( "show-this" );
             } 
             else {
                 var value = $('.sbSearch md-input-container .mySearchBox').filter(function () {
                     return this.value != '';
                 });
                 if(value.length<=0) { // zero-length string
                     $( ".sbSearch md-input-container" ).removeClass( "show-this" );
                     $( ".sbSearch md-input-container" ).addClass( "hidethis" );
                 }
             }
         } );
         
         $(".sbSearch md-input-container .mySearchBox").blur(function() {
             var value = $('.sbSearch md-input-container .mySearchBox').filter(function () {
                 return this.value != '';
             });
             if(value.length<=0) { // zero-length string
                 $( ".sbSearch md-input-container" ).removeClass( "show-this" );
                 $( ".sbSearch md-input-container" ).addClass( "hidethis" );
             }
        });
         this.expandRuleGroup.forEach(item=>{
             item = false;
         });
         this.expandRuleName.forEach(item=>{
             item = false;
         });
    }

    //Innersidebar Content height based on length
    getInSidHeight(){
        if(this.sideBarData.length == 1){
            return 'maxHeightNone'
        }else{
            $('.scrHetless').css('max-height',200);
            return 'scrHetless';
        }
    }
    
    selectedColumn()
    {
        console.log('selectedCol is'+JSON.stringify(this.selectedCol));    
        /*this.fileTemplatesService.groupSideBarByCols('FileTemplates',this.selectedCol).subscribe(        
            (res:Response)=>{
            console.log('response in ts is'+JSON.stringify(res));    
                this.sideBarData= res;
                console.log('this.sbr'+JSON.stringify(this.sideBarData));
            });*/
    }

    selectedCol1(column: any): any{
        console.log('selectedCol is'+JSON.stringify(column.colName));    
        /*this.fileTemplatesService.groupSideBarByCols('FileTemplates',column.colName).subscribe(        
            (res:Response)=>{
            console.log('response in ts is'+JSON.stringify(res));    
                this.sideBarData= res;
                console.log('this.sbr'+JSON.stringify(this.sideBarData));
            });*/
    }
    private expandRuleNameBody(j:any , ruleName : any)
    {
        if(this.expandRuleName[j])
        {
            this.expandRuleName[j] = false;
            this.ruleNameBody[j] = '';
        }
        else
        {
            this.expandRuleName [j]= true;
            this.ruleNameBody [j]= ruleName;
        }
    }
    private expandRuleGroupBody(i:any , ruleGroup : any)
    {
        if(this.expandRuleGroup[i])
            {
                this.expandRuleGroup[i] = false;
                this.ruleGroupBody[i] = '';
            }
        else
            {
                this.expandRuleGroup [i]= true;
                this.ruleGroupBody [i]= ruleGroup;
            }
    }
    private expandrulePurpose(rulePurpose) {
      console.log(this.currentrulePurpose);
      if (this.currentrulePurpose === rulePurpose) {  // if user click on same icon
        this.rulePurposeBody = '';     // Collapse expanded list
        this.currentrulePurpose = '';  // Replace current selection
        return;
      }
      this.currentrulePurpose = rulePurpose;  // To hold current selection
      this.sideBarData.forEach(rulePurposeData => {
        if (rulePurposeData.rulePurpose == rulePurpose) {
          this.rulePurposeBody = rulePurpose;
        }
      });
    }
}
