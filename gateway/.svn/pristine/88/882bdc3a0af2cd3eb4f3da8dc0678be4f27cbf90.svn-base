
import { Component, OnInit, OnDestroy, OnChanges, ViewChild,Input,Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbDatepickerConfig, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Subscription';
import { Router, NavigationEnd } from '@angular/router';
import { SourceProfileFileAssignments } from '../source-profile-file-assignments/source-profile-file-assignments.model';
import { SourceProfilesService } from '../source-profiles/source-profiles.service';
import { SourceProfileswithConnections } from './source-profiles.model';
import { FormGroup, FormBuilder, FormArray, Validators, NgForm } from '@angular/forms';
import { JhiDateUtils } from 'ng-jhipster';
import { SourceProfileFileAssignmentsService } from '../source-profile-file-assignments/source-profile-file-assignments.service';
import { FileTemplates } from '../file-templates/file-templates.model';
import { FileTemplatesService } from '../file-templates/file-templates.service';
import { SourceConnectionDetails } from '../source-connection-details/source-connection-details.model';
import { SourceConnectionDetailsService } from '../source-connection-details/source-connection-details.service';
//import { NotificationsService } from 'angular2-notifications-lite';
import { ResponseWrapper, createRequestOption } from '../../shared';
import { currentRoute } from '../../breadcrumb.component';
import { CommonService } from '../common.service';
import { SessionStorageService } from 'ng2-webstorage';
import { processInputObj } from '../tagging/tagging.component';
import { MdDialog, MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
import { behavior } from 'd3';
declare var $: any;
declare var jQuery: any;

@Component({
    selector: 'profile-confirm-action-modal',
    templateUrl: 'profile-confirm-action-modal.html'
})
export class ConfirmActionModalDialog {
    constructor(
        public dialogRef: MdDialogRef<ConfirmActionModalDialog>,
        public dialog: MdDialog,
        @Inject(MD_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
    }
    onNoClick(): void {
        this.dialogRef.close();
    }
}

@Component({
    selector: 'jhi-source-profiles-edit',
    templateUrl: './source-profiles-edit.component.html'
})
export class SourceProfilesEditComponent implements OnInit, OnDestroy {
	connectionTypeSearch:any;
    submitted =false;
    @ViewChild(NgForm) ftlAssignment;
    copyProfile = false;
    today:any=new Date();     
    routeSub: any;
    mouseOverRowNo: number = -1;
    isCreateNew = false;
    isEdit = false;
    isViewOnly = false;
    copyTemplate = false;
    sourceProfile = new SourceProfileswithConnections();
    profileFileAssignments: SourceProfileFileAssignments[];
    fileTemplates: FileTemplates[];
    expandConnection = false;
    isTagging = false;
    connectionDetails: SourceConnectionDetails[];
    selectedConnection: any = {};
    connectionTypesList = [];
    finalCols = [];
    displayColumns = [];
    presentPath: any;
    sourceProfilesList: any = [];
    duplicateProfileName = false;
    dsplyFiletmplts = true;
    dsplyConnections = true;
    extraction =[];
    isVisibleA = true;
    taggingObject: processInputObj = {};
    startDateChange = false;
    endDateChange = false;
    routerPath ="'/source-profiles', {outlets: {'content': ['source-profiles-list']}}";
    constructor(
        private config: NgbDatepickerConfig,
        private dialog: MdDialog,
        private router: Router,
        private sourceProfilesService: SourceProfilesService,
        private route: ActivatedRoute,
        private dateUtils: JhiDateUtils,
        public sourceProfileAssignmentService: SourceProfileFileAssignmentsService,
        private fileTemplateService: FileTemplatesService,
        public sourceConnectionDetailsService: SourceConnectionDetailsService,
        //private notificationsService: NotificationsService,
        private cs: CommonService,
        private sessionStorage: SessionStorageService,
        private commonService: CommonService
    ) {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
        this.config.maxDate = { year: 2099, month: 12, day: 31 };
    }

    changeMinDate() {
        this.config.minDate = this.sourceProfile.startDate;
    }

    resetMinDate() {
        this.config.minDate = { year: 1950, month: 1, day: 1 };
    }

    
    ngOnInit() {
        this.sourceProfileAssignmentService.profToFt='';
       // this.routerPath ="/source-profiles', {outlets: {'content': ['source-profiles-list']}}";
        if(this.sourceConnectionDetailsService && this.sourceConnectionDetailsService.conToProf){
            this.routerPath= "'/source-connection-details', {outlets: {'content': ["+this.sourceConnectionDetailsService.conToProf+"]+'/details'}}";
        }
        if(this.sourceProfileAssignmentService.ftToProf){
            this.routerPath = "'/file-templates', {outlets : {'content': ["+this.sourceProfileAssignmentService.ftToProf+"]+'/details'}}";
        }
        $(".split-example").css({'height':'auto','min-height':(this.commonService.screensize().height - 130)+'px'});
        //      Load unassigned file templates
       /* this.sourceProfileAssignmentService.fetchUnassignedFileTemplates().subscribe((res: FileTemplates[]) => {
            this.fileTemplates = res;
            //console.log('this.fileTemplates unassigned are'+JSON.stringify(this.fileTemplates ));
        });*/
        this.routeSub = this.route.params.subscribe((params) => {
            const url = this.route.snapshot.url.map((segment) => segment.path).join('/');

            this.presentPath = this.route.snapshot.url;

            if (this.presentPath == "source-profiles-new") {
                //console.log('current path : '+this.presentPath);
                $('.component-title').removeClass('margin-left-22');
            } else {
                $('.component-title').addClass('margin-left-22');
            }

            if (params['id']) {

                if (url.endsWith('new')) {
                    this.isCreateNew = true;
                    this.expandConnection = true;
                    this.profileFileAssignments = [new SourceProfileFileAssignments()];
                    this.profileFileAssignments[0].edit = true;
                    //fetch source con0nection by id
                    this.sourceConnectionDetailsService.find(params['id']).subscribe((res1: any) => {
                        const resp = res1;
                        this.sourceProfile.connectionId = resp.id;
                        this.sourceProfile.connectionType = resp.connectionType;
                        //Load all available connections
                        this.sourceConnectionDetailsService.fetchSourceconnectionsAndDetails().subscribe((response: any) => {
                            this.connectionTypesList = response;
                            if (this.sourceProfile.connectionType) {
                                this.connectionTypesList.forEach((con) => {
                                    if (con.connectionTypeCode === this.sourceProfile.connectionType) {
                                        this.connectionDetails = con.sourceConnectionDetails;
                                        this.displayColumns = con.displayColumns;
                                    }
                                });

                                this.getConnections();
                                this.getConnectionDetails();
                            }
                        });

                    });
                }   else {
                    if (this.sessionStorage.retrieve('recentRoutes')['fileTemplatesRoute']) {
                        this.dsplyFiletmplts = false
                    }
                    if (this.sessionStorage.retrieve('recentRoutes')['connectionProfileRoute']) {
                        this.dsplyConnections = false;
                    }
                    if (this.sessionStorage.retrieve('connectionType')) {
                        this.sourceProfile = this.sessionStorage.retrieve('profilesInfo');
                        if(this.sourceProfile && this.sourceProfile.enabledFlag){
                            this.sourceProfile.status='Active';
                        }else if(this.sourceProfile && !this.sourceProfile.enabledFlag){
                            this.sourceProfile.status='Inactive';
                        }
                        this.expandConnection = true; 
                        this.sourceProfile.connectionType = this.sessionStorage.retrieve('connectionType');
                        this.sourceProfile.connectionId = this.sessionStorage.retrieve('connectionId');
                        this.sessionStorage.clear('connectionType');
                        this.sessionStorage.clear('connectionId');
                        this.sessionStorage.clear('profilesInfo');
                        this.sourceConnectionDetailsService.fetchSourceconnectionsAndDetails().subscribe((res: any) => {
                            this.connectionTypesList = res;
                            if (this.sourceProfile.connectionType) {
                                this.connectionTypesList.forEach((con) => {
                                    if (con.connectionTypeCode === this.sourceProfile.connectionType) {
                                        this.connectionDetails = con.sourceConnectionDetails;
                                        this.displayColumns = con.displayColumns;
                                    }
                                });
                                this.getConnections();
                                this.getConnectionDetails();
                                this.getUnassignedList(this.sourceProfile);
                            }
                        });
                        this.today =this.sourceProfile.startDate;
                        this.isEdit = true;
                    }else{
                        this.sourceProfilesService.getProfilesAndConnectionsByProfileId(params['id']).subscribe(
                        (res: any) => {

                            this.sourceProfile = res;
                            if(this.sourceProfile && this.sourceProfile.enabledFlag){
                                this.sourceProfile.status='Active';
                            }else if(this.sourceProfile && !this.sourceProfile.enabledFlag){
                                this.sourceProfile.status='Inactive';
                            }
                            this.today =this.sourceProfile.startDate;
                            this.taggingObject.tagType = 'sourceProfile';
                            this.taggingObject.name = this.sourceProfile.sourceProfileName;
                            this.taggingObject.id = this.sourceProfile.id;
                          //  console.log('taggin object ' + JSON.stringify(this.taggingObject));
                            //get connection list
                            this.getConnectionsList();
                        //    Load file assignments
                            this.sourceProfileAssignmentService.getFileAssignments(params['id']).subscribe((response1: any) => {
                                this.profileFileAssignments = response1;
                                if (url.endsWith('edit')) {
                                    this.profileFileAssignments.forEach((item) => {
                                        item.edit = true;
                                    })
                                    this.isEdit = true;
                                    this.getUnassignedList(this.sourceProfile);
                                    if (url.search('copy') != -1) {
                                        this.copyProfile = true;
                                        this.sourceProfile.sourceProfileName = '';
                                    }
                                }else {
                                    this.isViewOnly = true;
                                }
                            });
                        });
                    }
                    
                }
            }  else {
                this.getConnectionsList();
                this.isCreateNew = true;
                this.isVisibleA = true;
                this.getUnassignedList( this.sourceProfile );
                this.profileFileAssignments = [new SourceProfileFileAssignments()];
                this.profileFileAssignments[0].edit = true;
                if (this.sessionStorage.retrieve('recentRoutes')['fileTemplatesRoute']) {
                    this.dsplyFiletmplts = false;
                }
                if (this.sessionStorage.retrieve('recentRoutes')['connectionProfileRoute']) {
                  
                    this.dsplyConnections = false;
                }
                if (this.sessionStorage.retrieve('connectionType')) {
                    this.sourceProfile = this.sessionStorage.retrieve('profilesInfo');
                    if(this.sourceProfile && this.sourceProfile.enabledFlag){
                        this.sourceProfile.status='Active';
                    }else if(this.sourceProfile && !this.sourceProfile.enabledFlag){
                        this.sourceProfile.status='Inactive';
                    }
                    this.expandConnection = true;
                    this.sourceProfile.connectionType = this.sessionStorage.retrieve('connectionType');
                    this.sourceProfile.connectionId = this.sessionStorage.retrieve('connectionId');
                    this.sessionStorage.clear('connectionType');
                    this.sessionStorage.clear('connectionId');
                    this.sessionStorage.clear('profilesInfo');
                    this.sourceConnectionDetailsService.fetchSourceconnectionsAndDetails().subscribe((res: any) => {
                            this.connectionTypesList = res;
                            if (this.sourceProfile.connectionType) {
                                this.connectionTypesList.forEach((con) => {
                                    if (con.connectionTypeCode === this.sourceProfile.connectionType) {
                                        this.connectionDetails = con.sourceConnectionDetails;
                                        this.displayColumns = con.displayColumns;
                                    }
                                });
                                this.getConnections();
                                this.getConnectionDetails();
                            }
                        });
                }
            }
        });
       console.log('printing'+JSON.stringify( $(".dv-mt-expansion .mat-expansion-panel:first-child .mat-expansion-panel-content").
        css('min-height', (window.innerHeight - 330) + 'px')));
    };
    //If Start Date Entered Apply Class
    startEndDateClass(){ 
        if(this.sourceProfile.startDate != null){
            return 'col-md-3 col-sm-6 col-xs-12 form-group';
        }else{
            return 'col-md-4 col-sm-6 col-xs-12 form-group';
        }
    }
    openTaggingDialog(){
        $('.tagging-custom-dialog').css('right','20px');
    }
    ngOnDestroy() {
        //this.notificationsService.remove();
        this.routeSub.unsubscribe();
    }
    getConnectionsList() {
        this.sourceConnectionDetailsService.fetchSourceconnectionsAndDetails().subscribe((res: any) => {
            this.connectionTypesList = res;
            if (this.sourceProfile.connectionType) {
                this.connectionTypesList.forEach((con) => {
                    if (con.connectionTypeCode === this.sourceProfile.connectionType) {
                        this.connectionDetails = con.sourceConnectionDetails;
                    }
                });
                this.getConnections();
            }
        });
    }
    getConnections() {
        this.connectionDetails = [];
        this.displayColumns = [];
        this.finalCols = [];
        if (this.sourceProfile.connectionType) {
            this.connectionTypesList.forEach((con) => {
                if (con.connectionTypeCode === this.sourceProfile.connectionType) {
                    this.connectionDetails = con.sourceConnectionDetails;
                    this.displayColumns = con.displayColumns;
                }  else {
                    //console.log('not matched');
                }
            });
            //this.getUnassignedList(this.sourceProfile);

        }
    }

    addNewAssignment() {
        const newAssignment = new SourceProfileFileAssignments();
        newAssignment.edit = true;
        this.profileFileAssignments.push(newAssignment);
    }

    removeAssignment(id: any, index: number) {
        if (this.isCreateNew || this.isEdit) {
            //isnew 
            this.profileFileAssignments.splice(index, 1);
            //integrate serv    
            if (this.profileFileAssignments.length < 1) {
                this.addNewAssignment();
            }
        } else if (this.isViewOnly) {
            //call api to delete
            this.sourceProfileAssignmentService.delete(id).subscribe((res: Response) => {
                this.sourceProfileAssignmentService.getFileAssignments(this.sourceProfile.id).subscribe((response: any) => {
                    this.profileFileAssignments = response;
                });
            });
        }

    }

    saveOneAssignment(fileAssignment: any) {
        //console.log(fileAssignment);
        fileAssignment.sourceProfileId = this.sourceProfile.id;
        if (fileAssignment.id) {
            this.sourceProfileAssignmentService.update(fileAssignment).subscribe((res: any) => {
            });
        }  else {
            this.sourceProfileAssignmentService.create(fileAssignment).subscribe((res: any) => {
            });
        }


    }

    getConnectionDetails() {
        this.finalCols = [];
        if (this.sourceProfile.connectionId == -1) {
            let recentRoutes = this.sessionStorage.retrieve('recentRoutes');    // Get existing routes object from session storage
            if(recentRoutes == undefined){  // If object is undefined create new object
                recentRoutes = {};
            }
            Object.assign(recentRoutes,{profilesRoute: {    // Assign new item to existing routes json
                parentRoute: currentRoute.parent.snapshot.url.map((segment) => segment.path).join("/"),
                childRoute: currentRoute.snapshot.url.map((segment) => segment.path).join("/")}});
            
            
            this.sessionStorage.store('recentRoutes',recentRoutes);  // Store object into session storage
            const keysByIndex = Object.keys(recentRoutes);    // TO get all keys into one array
            this.sessionStorage.store('profilesInfo',this.sourceProfile);
            this.router.navigate(['/source-connection-details', { outlets: { 'content': ['source-connection-details-new'] } }]);
        } else {
            this.connectionTypesList.forEach((con) => {
                let srcConfig: any = [];
                srcConfig = con.sourceConnectionDetails;
                srcConfig.forEach((connectionObj) => {
                    if (connectionObj.id === this.sourceProfile.connectionId) {
                        this.selectedConnection = connectionObj;
                        this.displayColumns.forEach((column) => {
                            for (const key in this.selectedConnection) {
                                if (this.selectedConnection.hasOwnProperty(key)) {
                                    if (column.code === key) {
                                        const obj = {
                                            "display": column.meaning,
                                            "value": this.selectedConnection[key]
                                        };
                                        this.finalCols.push(obj);
                                    }
                                }
                            }
                        });
                       this.getUnassignedList(this.sourceProfile);
                    }
                });
            });
        }
    }

    saveProfile() {
        this.submitted = true;
        if(this.isCreateNew) {
            if(this.startDateChange){
             //   this.sourceProfile.startDate=new Date(this.sourceProfile.startDate.getTime() + 86400000);
            }
            if(this.endDateChange){
               // this.sourceProfile.endDate=new Date(this.sourceProfile.endDate.getTime() + 86400000);
            }
        } else if(this.isEdit){
            if(this.copyProfile)  {
                this.sourceProfile.id = null;
                this.sourceProfile.idForDisplay =null;
                for(let c=0;c<this.profileFileAssignments.length;c++) {
                    this.profileFileAssignments[c].id=null;
                    this.profileFileAssignments[c].sourceProfileId=null;

                }
            }
            //if(this.sourceProfile.startDate.getTime())
            //this.sourceProfile.startDate=new Date(this.sourceProfile.startDate.getTime() + 86400000);
           // if(this.sourceProfile.endDate && this.sourceProfile.endDate.getTime())
           // this.sourceProfile.endDate=new Date(this.sourceProfile.endDate.getTime() + 86400000);
        }
        this.sourceProfile.description = this.sourceProfile.profileDescription;
        if(this.isCreateNew){
            this.sourceProfile.enabledFlag = true;
        }
        
        const sourceProfileToSave = {
            "sourceProfiles": this.sourceProfile,
            "sourceProfileFileAssignments": this.profileFileAssignments
        };
        //if(this.ftlAssignment.valid)
       // {
            this.sourceProfilesService.postSrcProfileAndAssignments(sourceProfileToSave).
            subscribe((res: Response) => {
                let tasks: any = [];
                tasks = res;
                const id = tasks[0].details;
                this.sourceProfilesService.getProfilesAndConnectionsByProfileId(id).subscribe(
                    (response: any) => {
                        const recentRoutes = this.sessionStorage.retrieve('recentRoutes');
                        const keysByIndex = Object.keys(recentRoutes);    // TO get all keys into one array
                        if (recentRoutes == undefined || keysByIndex.length < 1) {
                            this.sourceProfile = response;
                            if(this.sourceProfile && this.sourceProfile.enabledFlag){
                                this.sourceProfile.status='Active';
                            }else if(this.sourceProfile && !this.sourceProfile.enabledFlag){
                                this.sourceProfile.status='Inactive';
                            }
                            this.getConnections();
                            //                      Load file assignments
                            this.sourceProfileAssignmentService.getFileAssignments(id).subscribe((response1: any) => {
                                this.profileFileAssignments = response1;
                            })
                             this.isViewOnly = true;
                            this.isCreateNew = false;
                            this.isEdit = false;
                            let link:any= '';
                            //link =['/source-profiles', {outlets: {'content': ['source-profiles-list']}}];
                            link = ['/source-profiles', {outlets: {'content': this.sourceProfile.idForDisplay +'/details'}}];
                            this.router.navigate(link);
                           
                        } else {
                            this.sessionStorage.store('profileId',id);
                            this.sessionStorage.store('sourceProfileName',this.sourceProfile.sourceProfileName);
                            const routerParent = recentRoutes[keysByIndex[keysByIndex.length-1]].parentRoute;
                            const routerChild = recentRoutes[keysByIndex[keysByIndex.length-1]].childRoute;
                            delete recentRoutes[keysByIndex[keysByIndex.length-1]];
                            this.sessionStorage.store('recentRoutes',recentRoutes);
                            if(routerChild.search('/')==-1){
                                this.router.navigate([`/${routerParent}`, { outlets: { 'content': [routerChild] } }]);
                            }else{
                                const routeLink = ['/'+routerParent, { outlets: { 'content': routerChild.split('/')[0] + '/'+routerChild.split('/')[1] } }];
                                this.router.navigate(routeLink);
                            }
                        }
                        this.commonService.success(
                            'Saved!',
                            this.sourceProfile.sourceProfileName + ' saved successfully!'
                        )
                    });
            });
      //  }
       /// else{
       //     this.commonService.error('File templates assignments are not valid','');
       // }
       
    }
    /*Functionality to check source profile name*/
    checkSourceProfileName(val) {
        //console.log('val :' + val);
        let count = 0;
        this.sourceProfilesService.getAllProfilesAndConnections().subscribe((res: ResponseWrapper) => {
            this.sourceProfilesList = res.json();
          //  console.log('profiles for tenant id areeeee ' + this.sourceProfilesList.length);
            this.sourceProfilesList.forEach((element) => {
                //console.log('inside for');
               let  enteredName:string;
                enteredName = val.toLowerCase( );
                let existingName:string ;
                if(element.sourceProfileName){
                    existingName = element.sourceProfileName.toLowerCase( );
                }
                
               
                if ( existingName == enteredName) {
                    count++;
                    //console.log('inside if');
                }
            });
            //console.log('count ' + count);
            if (count > 0) {
                this.duplicateProfileName = true;
            } else {
                this.duplicateProfileName = false;
            }
        }
        );
    }
    /*Function to show validation message*/
    profileValidationMsg() {
        this.submitted=true;
        if (this.duplicateProfileName == true) {
            this.commonService.error(
                'Warning!',
                'Profile Name already exists'
            )
        } else {
            // this.commonService.error(
            //     'Warning!',
            //     'Fill Mandatory Fields'
            // )
        }
    }

    
    execute(fileAssignmentId: any,i:any)  {
        console.log('FA Id:'+fileAssignmentId);
        this.extraction[i]=true;
        this.sourceProfileAssignmentService.MoveFilesFromCloudToLocal(fileAssignmentId).subscribe((res: ResponseWrapper) => {
            const resp:any =res;
           // console.log('resp:'+JSON.stringify(resp));
            if(resp.Status == 'Success' )  {
                if(resp.Number_of_files == 0)  {
                    this.commonService.success(
                            'No Files To Move',
                            ''
                    )
                    this.extraction[i]=false;
                    }  else{
                    //process moved local files
                    this.sourceProfileAssignmentService.processFilesFromLocalPath(fileAssignmentId).subscribe((response: any) => {
                        const resp1:any =response;
                  //  console.log('resp1'+JSON.stringify(resp1));
                    if(resp1)  {
                        this.commonService.success(
                                'Extraction Successful',
                                ''
                        )
                        this.extraction[i]=false;
                    } else {
                        this.commonService.error(
                                'Warning!',
                                'Extraction Failed'
                        )
                        this.extraction[i]=false;
                    }
                    });
                }
                
            } else  {
                this.commonService.error(
                        'Warning!',
                        'Failed while accessing files'
                )
                this.extraction[i]=false;
            }
        });
    }
    getUnassignedList(sourceProfilee:any) {
        //console.log('sho'+JSON.stringify(sourceProfilee)+'sourceProfile tenantid '+this.sourceProfile.tenantId);
       if(!this.isViewOnly){
        this.fileTemplates =[];
           this.sourceProfileAssignmentService.fetchUnassignedFileTemplates( sourceProfilee).subscribe((res: FileTemplates[]) => {
            this.fileTemplates = res;
               //console.log('this.fileTemplates unassigned are'+JSON.stringify(this.fileTemplates ));
           });
       }
        
    }
clearFields(i) {
        this.profileFileAssignments[i].templateId=null;
        this.profileFileAssignments[i].fileNameFormat='';
        this.profileFileAssignments[i].fileExtension='';
        this.profileFileAssignments[i].fileDescription='';
        this.profileFileAssignments[i].sourceDirectoryPath='';
        this.profileFileAssignments[i].localDirectoryPath='';
    }    
toggleSB() {
    if(!this.isVisibleA){
       this.isVisibleA = true;
       $('.split-example .left-component').addClass('visible');
    } else {
        this.isVisibleA = false;
        $('.split-example .left-component').addClass('visible');
    }
}
close(){
    if(this.sourceProfileAssignmentService.ftToProf){

    }else{
        
    }

    // let recentRoutes = this.sessionStorage.retrieve('recentRoutes');   
    // let keysByIndex = Object.keys(recentRoutes); 
    // if (recentRoutes == undefined || keysByIndex.length < 1){
    //     //if(this.isCreateNew)
    //    // {
    //         this.router.navigate(['/source-profiles', {outlets: {'content': ['source-profiles-list']}}]);
    //    // }
    //    // else if(this.isEdit)
    //    // {
    //    //     this.router.navigate(['/source-profiles', {outlets: {'content': [this.sourceProfile.idForDisplay]+'/details'}}]);
    //    // }

       
    // }else{
    //     let routerParent = recentRoutes[keysByIndex[keysByIndex.length-1]].parentRoute;
    //     let routerChild = recentRoutes[keysByIndex[keysByIndex.length-1]].childRoute;
    //     console.log('recentRoutes before'+JSON.stringify(recentRoutes));
    //     delete recentRoutes[keysByIndex[keysByIndex.length-1]];
    //     console.log('recentRoutes after'+JSON.stringify(recentRoutes));
    //     this.sessionStorage.store('recentRoutes',recentRoutes);
    //     if(routerChild.search('/')==-1){
    //         this.router.navigate([`/${routerParent}`, { outlets: { 'content': [routerChild] } }]);
    //     }else{
    //         let routeLink = ['/'+routerParent, { outlets: { 'content': routerChild.split('/')[0] + '/'+routerChild.split('/')[1] } }];
    //         this.router.navigate(routeLink);
    //     }
    // }
}
    selectedProfile()  {
    
    }
    startDateChanged(dt:Date){
        if(this.sourceProfile.endDate){
            if(this.sourceProfile.endDate<this.sourceProfile.startDate){
                this.sourceProfile.endDate=this.sourceProfile.startDate;
            }
        }
    }
}
