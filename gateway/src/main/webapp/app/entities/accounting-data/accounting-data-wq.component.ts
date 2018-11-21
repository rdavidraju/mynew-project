import { Component, OnInit,OnDestroy,Renderer,ElementRef } from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription,Subject } from 'rxjs/Rx';
import { Response } from '@angular/http';
import { DatePipe } from '@angular/common';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'moment';

import {
  
} from './accounting-data.model';
import { AccountingDataService } from './accounting-data.service';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage';
import { NotificationsService } from 'angular2-notifications-lite';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ENABLE_RULE_BLOCK } from '../../shared';
declare var moment: any;
declare var $: any;
// var cars:any[]   = require('./cars-medium.json');
@Component({
  selector: 'jhi-accounting-data',
  templateUrl: './accounting-data-wq.component.html'
})
export class AccountingDataWqComponent implements OnInit , OnDestroy{
  private unsubscribe = new Subject();
  acctAllData: any;
  sViewIds: any[] = [];
  tViewId: any[] = [];
  searchKey: any;
  isViewOnly:boolean;
  isAcctList = false;
  private isScrolled = false;
  private rendererListen:any;
  constructor(
    public accountingDataService: AccountingDataService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private cs: CommonService,
    private ns: NotificationsService,
    private session: SessionStorageService,
    private datePipe: DatePipe,
    private renderer: Renderer,
    private elementRef: ElementRef
  ) {
    this.rendererListen = this.renderer.listen(window,'scroll',() => {
      const header = this.elementRef.nativeElement.querySelector('.ui-datatable-scrollable-header');
      this.isScrolled = window.pageYOffset > 35;
      this.renderer.setElementClass(header, 'header_scrolled', this.isScrolled);
  })
  }
  /* FUNCTION16 - On Load Function */
  /* Author : BHAGATH */
  getAcctAllData() {
    this.isAcctList = false;
    this.accountingDataService.isAcctAllData = [];
    this.accountingDataService.acctAllData().takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.isAcctList = true;
        this.acctAllData = res;
        if (this.acctAllData.summary.length > 0) {
          this.accountingDataService.isAcctAllData = this.acctAllData.summary;
        } else {
          this.ns.info('Info!', 'No Accounting Processes Found!')
        }
      },
      (res: Response) => {
        this.isAcctList = false;
        this.onError('Error Occured while fetching Reconciliation Data !')
      }
    );
  }
  ngOnInit() {
    this.cs.acctToRule = false;
    this.cs.reconToRule = false;
    if(this.cs.isAcctAllData == true){
      this.getAcctAllData();
    } else {
      if(this.accountingDataService.isAcctAllData.length == 0){
        this.getAcctAllData();
      }else {
        this.isAcctList = true;
      }
    }
    
    this.principal.identity().then((account) => {
      this.cs.currentAccount = account;
    });
  }

  private onError(errorMessage) {
    this.ns.error(
      'Error!',
      errorMessage,
      {
        timeOut: 3000,
        showProgressBar: false,
        pauseOnHover: true,
        clickToClose: true,
        maxLength: 100
      }
    )
  }

  getViewDetails(row) {
    this.accountingDataService.selectedAcctGroup = {};
    this.accountingDataService.selectedAcctGroup = {
      groupId : row.groupId,
      viewId : row.viewId,
      ruleGroupName : row.groupName,
      viewName : row.viewName
    }
    this.session.store('reconSelected', this.accountingDataService.selectedAcctGroup);
  }

  acctToRule() {
    this.cs.acctToRule = true;
  }

  ngOnDestroy(){
    this.unsubscribe.next();
    this.unsubscribe.complete();
    this.rendererListen();
  }

}
