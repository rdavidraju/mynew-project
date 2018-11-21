import { Component, OnInit ,OnDestroy,Renderer,ElementRef} from '@angular/core';
import { ActivatedRoute, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription,Subject } from 'rxjs/Rx';
import { Response } from '@angular/http';
import { DatePipe } from '@angular/common';

import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import 'moment';

import {
  ReconcileBreadCrumbTitles
} from './reconcile.model';
import { ReconcileService } from './reconcile.service';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, ENABLE_RULE_BLOCK,ReconDashBoardObject } from '../../shared';
declare var moment: any;
declare var $: any;

@Component({
  selector: 'jhi-reconcile',
  templateUrl: './reconcile.component.html'
})
export class ReconcileComponent implements OnInit, OnDestroy {
  private unsubscribe = new Subject();
  reconAllData: any;
  sViewIds: any[] = [];
  tViewId: any[] = [];
  searchKey: any;
  isViewOnly:boolean;
  isReconList = false;
  gb:any;
  private isScrolled = false;
  private rendererListen:any;
  constructor(
    public reconcileService: ReconcileService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    public cs: CommonService,
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
  getReconAllData() {
    this.isReconList = false;
    this.reconcileService.isAllData = [];
    this.reconcileService.reconAllData().takeUntil(this.unsubscribe).subscribe(
      (res: any) => {
        this.isReconList = true;
        this.reconAllData = res;
        if (this.reconAllData.summary.length > 0) {
          this.reconcileService.isAllData = this.reconAllData.summary;
        } else {
          this.cs.info('Info!', 'No Reconciliation Processes Found!')
        }
      },
      (res: Response) => {
        this.isReconList = false;
        this.onError('Error Occured while fetching Reconciliation Data !')
      }
    );
  }
  ngOnInit() {
    this.cs.reconDashBoardObject = new ReconDashBoardObject();
    this.cs.acctToRule = false;
    this.cs.reconToRule = false;
    if(this.cs.isRecAllData == true){
      this.getReconAllData();
    } else {
      if(this.reconcileService.isAllData.length == 0 ){
        this.getReconAllData();
      } else {
        this.isReconList = true;
      }
      
    }
    
    this.principal.identity().then((account) => {
      this.cs.currentAccount = account;
    });
  }

  private onError(errorMessage) {
    this.cs.error(
      'Error!',
      errorMessage
    )
  }

  getViewDetails(row) {
    this.reconcileService.selectedGroup = {};
    this.reconcileService.selectedGroup = {
      groupId : row.groupId,
      sViewId : row.sViewId,
      tViewId : row.tViewId,
      ruleGroupName : row.groupName,
      sViewName : row.sViewName,
      tViewName : row.tViewName
    }
    this.session.store('reconSelected', this.reconcileService.selectedGroup);
  }

  reconToRule(){
    this.cs.reconToRule = true;
  }

  public ngOnDestroy(){
    this.unsubscribe.next();
    this.unsubscribe.complete();
    this.rendererListen();
    this.cs.destroyNotification(); 
  }

}
