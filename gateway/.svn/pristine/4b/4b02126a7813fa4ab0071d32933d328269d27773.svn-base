import { Component, OnInit, OnDestroy,Inject,HostListener,Renderer,ElementRef} from '@angular/core';
import { Response } from '@angular/http';
import { ActivatedRoute,ActivatedRouteSnapshot, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager,  JhiPaginationUtil } from 'ng-jhipster';
import { RuleGroup } from './rule-group.model';
import { RuleGroupService } from './rule-group.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { NotificationsService } from 'angular2-notifications-lite';
import { SelectItem } from 'primeng/primeng';
import {PageEvent} from '@angular/material';
import { CommonService } from '../common.service';
import { NavigationEnd } from '@angular/router';
import {routerTransition} from '../../layouts/main/route.transition';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { MultiSelectDropDownSettings } from '../../shared/components/multiselect-dropdown';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { BulkUploadConfirmActionModalDialog } from '../../shared/components/bulk-upload';
import { trigger, transition, style, animate } from '@angular/animations';
import {pageSizeOptionsList} from '../../shared/constants/pagination.constants';

declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'bulk-upload-modal',
    templateUrl: 'bulk-upload-modal.html'
})
export class BulkUploadModalDialog {
    constructor(
        public dialogRef: MdDialogRef<BulkUploadModalDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) {
        dialogRef.disableClose= true;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}

@Component({
    selector: 'jhi-rule-group',
    templateUrl: './rule-group.component.html',
    animations: [routerTransition,
        trigger('slide', [
            transition(':enter', [
              style({
                transform: 'translateX(110%)'
              }),
              animate('0.3s')
            ]),
            transition(':leave', [
              animate('0.3s', style({
                transform: 'translateX(110%)'
              }))
            ])
          ])]
})
export class RuleGroupComponent implements OnInit, OnDestroy {
    emptySpace=' ';
    showBulkUpload=false;
    settings:MultiSelectDropDownSettings = new MultiSelectDropDownSettings();
    singleSelectsettings:MultiSelectDropDownSettings = new MultiSelectDropDownSettings();
    data=
    [
                { name: 'Source view', abbreviation: 'SV', checked: false },
                { name: 'Target view', abbreviation: 'SV', checked: false },
                { name: 'abc view', abbreviation: 'SV', checked: false },
                { name: 'asdfsfs view', abbreviation: 'SV', checked: false },
                { name: 'gdgdg view', abbreviation: 'SV', checked: false },
                { name: 'Souyhtyryrtrce view', abbreviation: 'SV', checked: false },
            ];
            bulkUpload:boolean=false;
    gb:any;
    ruleGroups: RuleGroup[];
    selectedTab: any;
    routeSub: any;
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    itemsPerPage: any;
    routeData: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    columnOptions: SelectItem[]; 
    ruleGroupLength:any;
     TemplatesHeight:any;
    length: number;
    pageSize: number;
    pageSizeOptions = pageSizeOptionsList;
    pageEvent: PageEvent = new PageEvent();
    ruleGroupColumns = [
                    //   { field: 'name', header: 'Group Name', width: '30%', align: 'left' },
                      { field: 'rulePurpose', header: 'Rule Purpose', width: '10%', align: 'center' },
                      { field: 'ruleCount', header: 'Rule Count', width: '10%', align: 'center' }
                     /*  { field: 'startDate ', header: 'Start Date', width: '150px', align: 'left' },
                       { field: 'endDate', header: 'End Date', width: '100px', align: 'center' }*/
                     
                   /*    { field: 'status', header: 'Status', width: '100px', align: 'left' }*/
                     ];
    ruleGroupTabsData:any=[];
    searchWord: any;
    sortColumn = 'id';
    sortOrder = 'desc';
    activePurpose: any;
    windowScrollTop: any;
     private window;
     
    constructor(
        public ruleGroupService: RuleGroupService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private cs: CommonService,
        private router: Router,
        public dialog: MdDialog,
        private renderer: Renderer,
        private el: ElementRef,
    ) {
        this.pageSize = ITEMS_PER_PAGE;
        this.pageEvent.pageIndex = 0;
        this.pageEvent.pageSize = this.pageSize;
//    this.notificationOptions = notificationOptions;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
      this.columnOptions = [];
      for (let i = 0; i < this.ruleGroupColumns.length; i++) {
          this.columnOptions.push({ label: this.ruleGroupColumns[i].header, value: this.ruleGroupColumns[i] });
        }
    });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }
    showPaginator=true;
    addPaginator(){
        this.showPaginator = !this.showPaginator;
      
        $(".paginator-class").css("background-color",'#efeeee');
        $(".paginator-class").css("white-space","nowrap");
      

    }
    removePaginator(){
       
        $(".rule-paginator").css("display",'none');
     
    }
   
    scrollToTop = 0;
    windowHeight =0;
    @HostListener("window:scroll", [])
        onWindowScroll() {
         const offsetExt = $("#list-table").offset();
         console.log('offsetExt'+JSON.stringify(offsetExt));
         const posExt = offsetExt.top - $(window).scrollTop();
             console.log('posExt'+posExt);
             console.log('$(window).scrollTop()'+($(window).scrollTop()));
             this.windowScrollTop=($(window).scrollTop()+250);
             console.log('$(window).height()'+$(window).height());
             this.windowHeight = $(window).height();
             this.scrollToTop = $(window).scrollTop();
             if(($(window).scrollTop()) == 0){
                  $(".paginator-class").removeClass("scrollPaginator");
                  
                  $(".paginator-class").addClass("openPaginator");
               
                 $(".paginator-class").css("transform","translateY(0px)");
                 $(".paginator-class").removeClass("translate-paginator");
                
              
             }else{
                $(".paginator-class").addClass("scrollPaginator");
                $(".paginator-class").removeClass("openPaginator");
         }

            
        
            }
    updateFromChild(e){
        console.log(' multiple select event has'+JSON.stringify(e));
      }

    singleSelect(e){
        console.log(' single select event has'+JSON.stringify(e));
      }
    loadAll(rulePurpose,event?:any) {
        
    //     if (this.currentSearch) {
    //         this.ruleGroupService.search({
    //             query: this.currentSearch,
    //             }).subscribe(
    //                 (res: Response) => this.ruleGroups = res.json(),
    //                 (res: Response) => this.onError(res.json())
    //             );
    //         return;
    //    }
        //console.log('this.pageEvent.pageIndex+1,this.pageEvent.pageSize'+this.pageEvent.pageIndex+'=>'+this.pageEvent.pageSize);
        if(event){
            this.pageSize = event.pageSize;
        }
        if(rulePurpose === undefined || !rulePurpose)   {
            rulePurpose = null;
            if ( this.ruleGroupTabsData && Object.keys(this.ruleGroupTabsData)[this.activePurpose]){
                rulePurpose = Object.keys(this.ruleGroupTabsData)[this.activePurpose];
            }
               
            }
        this.ruleGroupService.fetchRuleGroupsWithMeaningByTenant(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,rulePurpose,this.sortColumn,this.sortOrder).subscribe(
            (res: any) => {
                this.ruleGroups = res.json();
              //  console.log(' this.ruleGroups '+JSON.stringify( this.ruleGroups ));
                 this.length= +res.headers.get('x-count');
                 if(this.ruleGroups && this.ruleGroups.length>0) {
                    this.ruleGroupTabsData=this.ruleGroups[0]['ruleGrpTypeAndCount'];
                 }
                const unique = this.ruleGroups.filter(function(elem, index, self) {
                    return index == self.indexOf(elem);
                })
                
                
            //     this.ruleGroupService.fetchSideBarData('rulePurpose').subscribe(        
            // (res:Response)=>{
            //     this.sideBarData= res;
            //     let ind = 0;
            //     if(!rulePurpose)
            //         {
                    
            //         }
            //     else
            //         {
            //         this.sideBarData.forEach(sideBarObj=>{
            //             if(rulePurpose.toLowerCase() === sideBarObj.rulePurpose.toLowerCase())
            //                 {
            //                 this.activePurpose = ind;
            //                 }
            //             ind = ind +1;
            //         });
            //         }
               
                
            // });
                //this.currentSearch = '';
            },
            (res: Response) => this.onError(res.json())
        );
    }
    setPageSizeOptions(setPageSizeOptionsInput: string) {
     //   this.pageSizeOptions = setPageSizeOptionsInput.split(',').map((str )=> +str);
      }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll('All');
    }

    clear() {
        this.currentSearch = '';
        this.loadAll('All');
    }

   
    ngOnInit() {
        //set default top
       // $(".rule-paginator").css("top",($(window).scrollTop()+250));
       // $(".rule-paginator").css("display",'none');
       $(".paginator-class").addClass("openPaginator");
        this.settings.singleSelection=false;
        this.singleSelectsettings.singleSelection=true;
        this.singleSelectsettings.text='select sigle item';
        this.cs.reconToRule = false;
        this.cs.acctToRule = false;
        const url = this.activatedRoute.snapshot.url.map((segment) => segment.path).join('/');
        
        this.routeSub = this.activatedRoute.params.subscribe( (params) => {
            if (params['rulePurpose']) {
            this.activePurpose = params['rulePurpose'].toLowerCase() == 'all' ? 0 : params['rulePurpose'].toLowerCase()== 'reconciliation' ? 1 : params['rulePurpose'].toLowerCase() == 'accounting' ? 2 : params['rulePurpose'].toLowerCase() == 'approvals' ? 3 : 0;
            this.ruleGroupService.ruleGroup.rulePurpose=params['rulePurpose'];    
            if( this.ruleGroupService.ruleGroup.rulePurpose.toLowerCase()== 'reconciliation'){
                this.showBulkUpload=true;
            }    else{
                this.showBulkUpload=false;
            }          
            this.loadAll(params['rulePurpose']);
            } else  {
                this.loadAll('All');
                // console.log('doesnot contain rule purpose');
                }
            });
        this.TemplatesHeight = (this.cs.screensize().height - 370) + 'px';
        
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRuleGroups();
        $(".search-icon-body").click(function() {
            if ( $( ".ftlSearch md-input-container" ).hasClass( "hidethis" ) ) {
                $( ".ftlSearch md-input-container" ).removeClass( "hidethis" );
                $( ".ftlSearch md-input-container" ).addClass( "show-this" );
            } else if ( $( ".ftlSearch md-input-container" ).hasClass( "show-this" ) ) {
                const value = $( '.ftlSearch md-input-container .mySearchBox' ).filter( function() {
                    return this.value != '';
                } );
                if ( value.length <= 0 ) { // zero-length string
                    $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                    $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
                }
            } else {
                $( ".ftlSearch md-input-container" ).addClass( "show-this" );
            }
        } );
        $(".ftlSearch md-input-container .mySearchBox").blur(function() {
            const value = $('.ftlSearch md-input-container .mySearchBox').filter(function() {
                return this.value != '';
            });
            if(value.length<=0) { // zero-length string
                $( ".ftlSearch md-input-container" ).removeClass( "show-this" );
                $( ".ftlSearch md-input-container" ).addClass( "hidethis" );
            }
       });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: RuleGroup) {
        return item.id;
    }



    registerChangeInRuleGroups() {
        this.eventSubscriber = this.eventManager.subscribe('ruleGroupListModification', (response) => this.loadAll('All'));
    }


    private onError(error) {
       // this.alertService.error(error.message, null, null);
    }
   /* loadContent(rulePurpose)
    {
        //[routerLink]="['/rule-group', {outlets: {'content': 'All'}}]"
        this.router.navigate(['/rule-group', { outlets: {'content': rulePurpose} }]);
    }*/
     loadContent(e) {
        // console.log(e);
        this.activePurpose = e.value;
        //let rulePurpose = this.ruleGroupTabsData[e.value];
        const rulePurpose =Object.keys(this.ruleGroupTabsData)[e.value];
        if( rulePurpose== 'reconciliation'){
            this.showBulkUpload=true;
        }    else{
            this.showBulkUpload=false;
        }   
        this.router.navigate(['/rule-group', { outlets: {'content': [rulePurpose]+'/list'} }]);
    }
     searchData()  {
        //  console.log('searchWord'+this.searchWord);
         const rulePurpose = Object.keys(this.ruleGroupTabsData)[this.activePurpose];
         this.ruleGroupService.searchKeys(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.searchWord,rulePurpose).subscribe(        
                 (res:any)=>{
                     this.ruleGroups =res._body;
                    //  console.log(' this.ruleGroups after search'+JSON.stringify( this.ruleGroups));
                 });
     }
     setRuleGroup()   {
         //console.log('this.sideBarData[this.selectedTab].rulePurpose.toLowerCase'+this.sideBarData[this.selectedTab].rulePurpose.toLowerCase());
         if(Object.keys(this.ruleGroupTabsData)[this.activePurpose] &&  Object.keys(this.ruleGroupTabsData)[this.activePurpose].toLowerCase() != 'all' ){
         this.ruleGroupService.routerRuleGroupType = Object.keys(this.ruleGroupTabsData)[this.activePurpose].toUpperCase();
         } else{
             if(this.ruleGroupTabsData && this.ruleGroupTabsData[1] && this.ruleGroupTabsData[1]){
            this.ruleGroupService.routerRuleGroupType = this.ruleGroupTabsData[1].toUpperCase();
             }
         }
     }
     changeSourceSort(event) {
     if(event.field != 'ruleCount')  {
        this.sortColumn =  event.field;
     
    
        if (event.order < 1) {
          this.sortOrder = 'desc';
        } else {
          this.sortOrder = 'asc';
        }
       this.loadAll(this.ruleGroupTabsData[this.activePurpose]);
     }
    }
    
    openBulkUpload() {
        console.log('Open bulk upload dialog');
        this.bulkUpload=true;
        const dialogRef = this.dialog.open(BulkUploadConfirmActionModalDialog, {
            width: '400px'
        });
        dialogRef.afterClosed().subscribe((result) => {
            if(result && result == 'close') {
                //refresh rules
                this.router.navigate(['/rule-group', { outlets: {'content': [this.ruleGroupTabsData[this.activePurpose]]+'/list'} }]);
            }
        });
    }
}
