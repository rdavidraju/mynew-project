import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute, NavigationEnd, Params, PRIMARY_OUTLET } from "@angular/router";
import { SessionStorageService } from 'ng2-webstorage';
import "rxjs/add/operator/filter";
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
declare var $:any;
declare var jQuery: any;

interface IBreadcrumb {
  label: string;
  labelsArray: string[];
  params?: Params;
  url: string;
  routeType: string;
  parent: string;
}

@Component({
  selector: "breadcrumb",
  template: `
    <div class="page-bar">
        <ul *ngIf="currentBreadCrumb" class="page-breadcrumb" style="padding:6px 10px;">
         <li><a routerLink="/dashboardv1"><md-icon style="position: relative; top: 5px;">home</md-icon></a></li>
          <li *ngFor="let lable of currentBreadCrumb.labelsArray;">
            <a style="font-size: 12px;">{{lable}}</a>
          </li>
        </ul>
    </div>
  `
})
export class BreadcrumbComponent implements OnInit {

  /* <a class="material-icons" style="color: #0d46a0;" *ngIf="breadcrumbsHistory[0].routeType == 'B'" [routerLink]="[breadcrumbsHistory[0].parent, {outlets: {'content': [breadcrumbsHistory[0].url]}}]">arrow_back</a>
          <a class="material-icons" style="color: #0d46a0;" *ngIf="breadcrumbsHistory[0].routeType == 'A'" [routerLink]="[breadcrumbsHistory[0].parent, {outlets: {'content': [breadcrumbsHistory[0].url.split('/')[0]]+'/'+breadcrumbsHistory[0].url.split('/')[1]}}]">arrow_back</a>
          <a class="material-icons" style="color: #0d46a0;" *ngIf="breadcrumbsHistory[0].routeType == 'C'" [routerLink]="['/'+breadcrumbsHistory[0].url, breadcrumbsHistory[0].params]">arrow_back</a> */
        
        // <div *ngIf="breadcrumbsHistory.length>0" class="page-breadcrumb-history custom-breadcrumb hand prevRoute" style="right: 30px;" [ngStyle]="{'padding':nextRoutes.length>0 ? '15px 80px' : '15px 28px'}">
        //   <a class="material-icons" style="color: #0d46a0;" (click)="previousRouteF();">arrow_back</a>
        //   <ul class="myClass" style="display:none;" [ngStyle]="{'right':nextRoutes.length>0 ? '100px' : '50px'}">
        //      <perfect-scrollbar style="max-height: 120px; height:auto;" >
        //       <li *ngFor="let breadcrumb of breadcrumbsHistory">
        //         <a *ngIf="breadcrumb.routeType == 'B'" [routerLink]="[breadcrumb.parent, {outlets: {'content': [breadcrumb.url]}}]">{{breadcrumb.label}}</a>
        //         <a *ngIf="breadcrumb.routeType == 'A'" [routerLink]="[breadcrumb.parent, {outlets: {'content': [breadcrumb.url.split('/')[0]]+'/'+breadcrumb.url.split('/')[1]}}]">{{breadcrumb.label}}</a>
        //         <a *ngIf="breadcrumb.routeType == 'C'" [routerLink]="['/'+breadcrumb.url, breadcrumb.params]">{{breadcrumb.label}}</a>
        //       </li>
        //     </perfect-scrollbar>
        //   </ul>
        // </div>
        // <div *ngIf="nextRoutes.length>0" class="page-breadcrumb-history custom-breadcrumb hand nextRoute" style="padding: 15px 28px; right: 30px;">
        //   <a class="material-icons" style="color: #0d46a0;" (click)="nextRouteF();">arrow_forward</a>
        //   <ul class="myClass" style="display:none;">
        //      <perfect-scrollbar style="max-height: 120px; height:auto;" >
        //       <li *ngFor="let nextRt of nextRoutes">
        //         <a *ngIf="nextRt.routeType == 'B'" [routerLink]="[nextRt.parent, {outlets: {'content': [nextRt.url]}}]">{{nextRt.label}}</a>
        //         <a *ngIf="nextRt.routeType == 'A'" [routerLink]="[nextRt.parent, {outlets: {'content': [nextRt.url.split('/')[0]]+'/'+nextRt.url.split('/')[1]}}]">{{nextRt.label}}</a>
        //         <a *ngIf="nextRt.routeType == 'C'" [routerLink]="['/'+nextRt.url, nextRt.params]">{{nextRt.label}}</a>
        //       </li>
        //     </perfect-scrollbar>
        //   </ul>
        // </div>
  public breadcrumbs: IBreadcrumb[]= [];
  public breadcrumbsHistory: IBreadcrumb[]= [];
  public currentBreadCrumb: IBreadcrumb = {
    label: '',
    labelsArray: [],
    params: {},
    url: '',
    routeType: '',
    parent: ''
  };
  nextRoutes: any = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private $sessionStorage: SessionStorageService
  ) {
      this.breadcrumbs=[];
  }

  ngOnInit() {
    const ROUTE_DATA_BREADCRUMB: string = "breadcrumb";

    /**Retrieve in session storage */
    if(this.$sessionStorage.retrieve('sessionbreadCrumb')){
      this.breadcrumbs = this.$sessionStorage.retrieve('sessionbreadCrumb');
      this.currentBreadCrumb=this.breadcrumbs[0];
    }
    if(this.$sessionStorage.retrieve('sessionbreadCrumbHis')){
      this.breadcrumbsHistory = this.$sessionStorage.retrieve('sessionbreadCrumbHis');
    }

    //subscribe to the NavigationEnd event
    this.router.events.filter(event => event instanceof NavigationEnd).subscribe(event => {
      //set breadcrumbs
      
      let root: ActivatedRoute = this.activatedRoute.root;
      let newBreadCrumbList = this.getBreadcrumbs(root);
      if (this.breadcrumbs.length > 1) {
          for (var i = 0; i < this.breadcrumbs.length - 1; i++) {
            this.breadcrumbsHistory.unshift(this.breadcrumbs[i]);
            this.breadcrumbs.splice(i, 1);
            i = i - 1;
          }
      }
      /**Store in session storage */
      this.$sessionStorage.store('sessionbreadCrumbHis', this.breadcrumbsHistory);
      this.$sessionStorage.store('sessionbreadCrumb', this.breadcrumbs);
    });
  }
  
  private getBreadcrumbs(route: ActivatedRoute, url: string="", breadcrumbs: IBreadcrumb[]=[]): IBreadcrumb[] {
      previousRoute.push(route);
      currentRoute = route;
    const ROUTE_DATA_BREADCRUMB: string = "breadcrumb";
    const BREADCRUMB_LABLES: string = "lablesArray";

    //get the child routes
    let children: ActivatedRoute[] = route.children;
    
    
    //return if there are no more children
    if (children.length === 0) {
      return breadcrumbs;
    }
        
    //iterate over each children
    for (let child of children) {
            
      //verify the custom data property "breadcrumb" is specified on the route
      if (!child.snapshot.data.hasOwnProperty(ROUTE_DATA_BREADCRUMB)) {
        return this.getBreadcrumbs(child, url, breadcrumbs);
      }

      //get the route's URL segment
      let routeURL: string = child.snapshot.url.map(segment => segment.path).join("/");

      //append route URL to URL
      url += `${routeURL}`;
      
      //add breadcrumb
      let breadcrumb: IBreadcrumb;
      let tempArray=[];
      if (child.snapshot.data.hasOwnProperty(BREADCRUMB_LABLES)) {
        tempArray=child.snapshot.data[BREADCRUMB_LABLES];
      }
        
      if (child.outlet === 'content') {
        if (url.split('/').length > 1) {
          breadcrumb = {
            label: child.snapshot.data[ROUTE_DATA_BREADCRUMB],
            labelsArray: tempArray,
            params: child.snapshot.params,
            url: url,
            routeType: 'A',
            parent: `/${child.parent.snapshot.url.map(segment => segment.path).join("/")}`
          }
        }else if(url.split('/').length == 1){
          breadcrumb = {
            label: child.snapshot.data[ROUTE_DATA_BREADCRUMB],
            labelsArray: tempArray,
            params: child.snapshot.params,
            url: url,
            routeType: 'B',
            parent: `/${child.parent.snapshot.url.map(segment => segment.path).join("/")}`
          }
        }
      } else {
        breadcrumb = {
          label: child.snapshot.data[ROUTE_DATA_BREADCRUMB],
          labelsArray: tempArray,
          params: child.snapshot.params,
          url: url,
          routeType: 'C',
          parent: `/${child.parent.snapshot.url.map(segment => segment.path).join("/")}`
        }
      };
      if (breadcrumb && this.currentBreadCrumb &&( this.currentBreadCrumb.label !== breadcrumb.label))
      { 
        this.currentBreadCrumb = breadcrumb;
        this.breadcrumbs.push(breadcrumb);
      }
      this.currentBreadCrumb = breadcrumb;
      //recursive
      return this.getBreadcrumbs(child, url, breadcrumbs);
    }

    console.log(JSON.stringify(this.breadcrumbs));
    
    //we should never get here, but just in case
    return breadcrumbs;
  }

  previousRouteF() {
    let link:any = '';
    if(this.breadcrumbsHistory[0].routeType == 'A') {
      //For Jobs
      if(this.breadcrumbsHistory[0].url.split('/').length == 4) {
        link = [this.breadcrumbsHistory[0].parent, {outlets: {'content': [this.breadcrumbsHistory[0].url.split('/')[0]]+'/'+[this.breadcrumbsHistory[0].url.split('/')[1]]+'/'+[this.breadcrumbsHistory[0].url.split('/')[2]]+'/'+this.breadcrumbsHistory[0].url.split('/')[3]}}];
      } else {
        link = [this.breadcrumbsHistory[0].parent, {outlets: {'content': [this.breadcrumbsHistory[0].url.split('/')[0]]+'/'+this.breadcrumbsHistory[0].url.split('/')[1]}}];
      }
    }else if(this.breadcrumbsHistory[0].routeType == 'B') {
      link = [this.breadcrumbsHistory[0].parent, {outlets: {'content': [this.breadcrumbsHistory[0].url]}}];
    }else if(this.breadcrumbsHistory[0].routeType == 'C') {
      link = ['/'+this.breadcrumbsHistory[0].url, this.breadcrumbsHistory[0].params];
    }
    this.router.navigate(link);
    this.breadcrumbsHistory.splice(0,1);
    let tempRoute = this.breadcrumbs.splice(0,1)[0];
    this.nextRoutes.unshift(tempRoute);
  }

  nextRouteF() {
    let link:any = '';
    if(this.nextRoutes[0].routeType == 'A') {
      //For Jobs
      if(this.nextRoutes[0].url.split('/').length == 4) {
        link = [this.nextRoutes[0].parent, {outlets: {'content': [this.nextRoutes[0].url.split('/')[0]]+'/'+[this.nextRoutes[0].url.split('/')[1]]+'/'+[this.nextRoutes[0].url.split('/')[2]]+'/'+this.nextRoutes[0].url.split('/')[3]}}];
      } else {
        link = [this.nextRoutes[0].parent, {outlets: {'content': [this.nextRoutes[0].url.split('/')[0]]+'/'+this.nextRoutes[0].url.split('/')[1]}}];
      }
    }else if(this.nextRoutes[0].routeType == 'B') {
      link = [this.nextRoutes[0].parent, {outlets: {'content': [this.nextRoutes[0].url]}}];
    }else if(this.nextRoutes[0].routeType == 'C') {
      link = ['/'+this.nextRoutes[0].url, this.nextRoutes[0].params];
    }
    this.router.navigate(link);
    this.nextRoutes.splice(0,1);
  }

}
export var previousRoute : ActivatedRoute[]=[];
export var currentRoute : ActivatedRoute;