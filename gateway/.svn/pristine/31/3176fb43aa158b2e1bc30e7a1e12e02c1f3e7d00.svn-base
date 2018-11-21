import { Component,DoCheck, KeyValueDiffers, OnInit,HostListener, OnDestroy ,Input, Output, EventEmitter  } from '@angular/core';
import { ActivatedRoute,ActivatedRouteSnapshot, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';
import { JhiDateUtils } from 'ng-jhipster';
import { FileTemplates, FinalJSONModel } from './file-templates.model';
import { FileTemplatesService } from './file-templates.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { SelectItem } from 'primeng/primeng';
//import { NotificationsService } from 'angular2-notifications-lite';
import { CommonService } from '../common.service';
import { DatePipe } from '@angular/common';
import {PageEvent} from '@angular/material';
import {JsonConvert} from 'json2typescript';
import { FileSelectDirective, FileDropDirective, FileUploader, FileItem ,FileUploaderOptions} from 'ng2-file-upload/ng2-file-upload';
declare function escape(s:string): string;
import {pageSizeOptionsList} from '../../shared/constants/pagination.constants';
import { trigger, transition, style, animate } from '@angular/animations';
import {routerTransition} from '../../layouts/main/route.transition';
import 'jquery';
import 'jquery-ui-bundle/jquery-ui.min.js';
const URL = '';
declare var $: any;
declare var jQuery: any;
@Component({
    selector: 'jhi-file-templates',
    templateUrl: './file-templates.component.html',
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
export class FileTemplatesComponent implements OnInit, OnDestroy {
    @Output() notify: EventEmitter<boolean> = new EventEmitter<boolean>();
    isClose = false;
    currentAccount: any;
    fileTemplates: FileTemplates[];
    error: any;
    abc: any;
    page:any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    filesAdded:FileList;
    windowScrollTop: any;
    scrollToTop = 0;
    windowHeight =0;
    showPaginator=true;

    templateData: any = [];
  mySelectedRows = [];
  columnOptions: SelectItem[]; 
  gb:any;
  length: number;
  pageSize: number;
  pageSizeOptions = pageSizeOptionsList;
  pageEvent: PageEvent = new PageEvent();

//  notificationOptions: any;
  
  myTableColumns = [
    { field: 'description', header: 'Description' },
    { field: 'fileType', header: 'File Type' },
    { field: 'delimiter', header: 'De-Limiter' },
    { field: 'source', header: 'Source' }
  ];
 
   fileTemplateColumns = [
   
    { field: 'description', header: 'Description', width: '30%', align: 'left' ,sortable:true},
    { field: 'fileType', header: 'File Type', width: '10%', align: 'left'  ,sortable:true},
    { field: 'delimiter', header: 'De-Limiter', width: '10%', align: 'center' ,sortable:true },
    { field: 'sourceMeaning', header: 'Source', width: '10%', align: 'left'  ,sortable:true},
    { field: 'status', header: 'Status', width: '10%', align: 'center'  ,sortable:true},
     { field: 'taggedToProfCnt', header: 'Profile Count', width: '20%', align: 'center'  ,sortable:false}
    
    /*{ field: 'startDate', header: 'Start Date', width: '100px', align: 'left' },
    { field: 'endDate', header: 'End Date', width: '100px', align: 'left' }*/
  ];
   fileTemplatesHeight:any;
  routeSnapshot: ActivatedRouteSnapshot;
  domainAndColumnsList : any = [];
  templateColumnsJSON :any=[];
  sortOrder = 'Descending';
  sortColName='id';
  finalJSON = {
    "Template Name":"templateName",
    "Description":"description",
    "Source":"source",
    "Start Date":"startDate",
    "End Date":"endDate",
    "File Type":"fileType",
    "Delimiter":"delimiter",
    "Skip Start":"skipRowsStart",
    "Skip End":"skipRowsEnd",
    "Row Identifier":"rowIdentifier",
    "Multiple Identifier":"multipleIdentifier",
    "Skip Empty Lines":"skipEmptyLines",
    "Start Row Columns":"startRowColumns",
    "Position Begin":"positionBegin",
    "Position End":"positionEnd",
    "Column Name":"columnHeader",
    "Constant":"constantValue",
    "Zero Fill":"zeroFill",
    "Date Format":"dateFormat",
    "Time Format":"timeFormat",
    "Amount Format":"amountFormat",
    "Skip Column":"skipColumn",
    "Criteria":"rowIdentifierCriteria",
    "Identifier":"rowIdentifier",
    "Identifier Position Start":"positionStart",
    "Identifier Position End":"positionEnd", 
    "Template Column Name":"",
    "Operator":"operator",
    "Value":"value",
    "Logical Operator":"logicalOperator",
    "Type":"type"
}
fileTemplateJSON =
{
"com.nspl.app.domain.FileTemplates":  [
   {
    "Template Name":"templateName",
    "Description":"description",
    "Source":"source",
    "Start Date":"startDate",
    "End Date":"endDate",
    "File Type":"fileType",
    "Delimiter":"delimiter",
    "Skip Start":"skipRowsStart",
    "Skip End":"skipRowsEnd",
    "Row Identifier":"rowIdentifier",
    "Multiple Identifier":"multipleIdentifier",
    "Skip Empty Lines":"skipEmptyLines",
    "Start Row Columns":"startRowColumns",
   },
   {
       "parentTable":"",
       "parentColumn":""
   }
]
};
fileTemplateLinesJSON =
{
"com.nspl.app.domain.FileTemplateLines":  
  [
       {
       "Position Begin":"positionBegin",
       "Position End":"positionEnd",
       "Column Name":"columnHeader",
       "Constant":"constantValue",
       "Zero Fill":"zeroFill",
       "Date Format":"dateFormat",
       "Time Format":"timeFormat",
       "Amount Format":"amountFormat",
       "Skip Column":"skipColumn"
    },
    {
        "parentTable":"com.nspl.app.domain.FileTemplates",
        "parentColumn":"templateName"
   }
]
};

intermediateJSON =
{
"com.nspl.app.domain.IntermediateTable": 
   [ 
   {
       "Criteria":"rowIdentifierCriteria",
       "Identifier":"rowIdentifier",
       "Identifier Position Start":"positionStart",
       "Identifier Position End":"positionEnd",  
     },
     {
       "parentTable":"com.nspl.app.domain.FileTemplates",
       "parentColumn":"templateName"
      }
]
};
rowconditionsJSON =
{
"com.nspl.app.domain.RowConditions":  
  [ {
       "Template Column Name":"columnHeader",
       "Operator":"operator",
       "Value":"value",
       "Logical Operator":"logicalOperator",
       "Type":"type",

    },
     {
         "parentTable":"com.nspl.app.domain.FileTemplateLines",
          "parentColumn":"columnHeader"
  }]
};
  public uploaderOptions: FileUploaderOptions;
    public hasBaseDropZoneOver = false;
    public uploader: FileUploader = new FileUploader({ url: URL });
    constructor(
        private differs: KeyValueDiffers,
        private jhiLanguageService: JhiLanguageService,
        public fileTemplatesService: FileTemplatesService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,  
        private paginationConfig: PaginationConfig,
     //   private _service: NotificationsService,
        private commonService: CommonService,
        private dateUtils: JhiDateUtils
    ) {
            this.itemsPerPage = ITEMS_PER_PAGE;
            this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.pageSize = ITEMS_PER_PAGE;
            this.pageEvent.pageIndex = 0;
            this.pageEvent.pageSize = this.pageSize;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    this.columnOptions = [];
    for (let i = 0; i < this.fileTemplateColumns.length; i++) {
      this.columnOptions.push({ label: this.fileTemplateColumns[i].header, value: this.fileTemplateColumns[i] });
    }
    this.subSideNav();
    this.fileTemplatesService.searchData=null;
    }

    loadAll() {
        this.fileTemplatesService.getFileTemplates(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.sortColName,this.sortOrder).subscribe(
            ( res: any ) => {
                this.fileTemplates= res;
                if(  this.fileTemplates &&   this.fileTemplates.length>0) {
                    this.length =  this.fileTemplates[0]['totalCount'];
                }
            });

        
        // this.fileTemplatesService.query(
        //         {
        //             page:this.pageEvent.pageIndex+1,
        //             size:this.pageEvent.pageSize,
        //         }).subscribe(
        //         (res: ResponseWrapper) => this.onSuccess(res, res.headers),
        //         (res: ResponseWrapper) => this.onError(res.json())
        //         );
     /* this.fileTemplatesService.search({
        query: this.currentSearch,
//        size: this.itemsPerPage,
        sort: this.sort()
      }).subscribe(
        (res: ResponseWrapper) => this.onSuccess(res.json(), res.headers),
        (res: ResponseWrapper) => this.onError(res.json())
        );
      return;*/
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/file-templates'], {queryParams:
            {
                page: this.page,
            //        size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    subSideNav(){
      this.notify.emit(!this.isClose);
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/file-templates', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
    if (!query) {
      return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.router.navigate(['/file-templates', {
      search: this.currentSearch,
      page: this.page,
      sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    }]);
    this.loadAll();
  }
    ngOnInit() {
        $(".paginator-class").addClass("openPaginator");
        this.fileTemplatesHeight = (this.commonService.screensize().height - 315) + 'px';
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInFileTemplates();
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

    searchText(){
        console.log('called search' + this.fileTemplatesService.searchData);
        if(this.fileTemplatesService.searchData && this.fileTemplatesService.searchData != null && this.fileTemplatesService.searchData != '')   {
            this.fileTemplatesService.search(
                    {
                        page:this.pageEvent.pageIndex+1,
                        size:this.pageEvent.pageSize,
                    }).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json())
                    );
            } else {
            this.loadAll();
            }
       
    }

    ngOnDestroy() {
        //this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: FileTemplates) {
        return item.id;
    }
    registerChangeInFileTemplates() {
        this.eventSubscriber = this.eventManager.subscribe('fileTemplatesListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
       // this.links = this.parseLinks.parse(headers.get('link'));
      
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.fileTemplates = data.json;
        if(  this.fileTemplates &&   this.fileTemplates.length>0) {
            this.length =  this.fileTemplates[0]['totalCount'];
           // this.fileTemplateColumns = this.fileTemplates[0]['columnsList'];
        }
      //  console.log('  this.fileTemplates printed below'+JSON.stringify(this.fileTemplateColumns));
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    onRowSelect(event) {
    console.log(event);
  }
 
  onRowUnselect(event) {
    console.log(event);
  }
    private addNewTemplate(){
        
    }
    changeSourceSort(event) {
     
          this.sortColName =  event.field;
       
      
      if (event.order < 1) {
        this.sortOrder = 'Descending';
      } else {
        this.sortOrder = 'Ascending';
      }
      this.fileTemplatesService.getFileTemplates(this.pageEvent.pageIndex+1,this.pageEvent.pageSize,this.sortColName,this.sortOrder).subscribe(
       ( res: any ) => {
                this.fileTemplates= res;
                if(  this.fileTemplates &&   this.fileTemplates.length>0){
                    this.length =  this.fileTemplates[0]['totalCount'];
                }
            });
      //this.getGroupByAcctRecords();
    }
    /**
     * Bulk Upload
     */
  
    loadTemplatesForTenant() {
        
        // this.fileTemplatesService.loadTemplates(this.templateColumnsJSON[0]).subscribe((res: Response) => {
         
        // });
    }
    //Validation to save as inactive if mandatory fields aren't filled, however when it is to be made active, fields should be filled
   
    // templateColumnsJSON =[ {
    //     "Template Name":"templateName",
    //     "Description":"description",
    //     "Source":"source",
    //     "Start Date":"startDate",
    //     "End Date":"endDate",
    //     "File Type":"fileType",
    //     "Delimiter":"delimiter",
    //     "Skip Start":"skipRowsStart",
    //     "Skip End":"skipRowsEnd",
    //     "Row Identifier":"rowIdentifier",
    //     "Multiple Identifier":"multipleIdentifier",
    //     "Skip Empty Lines":"skipEmptyLines",
    //     "Start Row Columns":"startRowColumns",
        
    //     "Criteria":"rowIdentifierCriteria",
    //     "Identifier":"rowIdentifier",
    //     "Identifier Position Start":"positionStart",
    //     "Identifier Position End":"positionEnd",

    //     "Template Column Name":"",
    //     "Operator":"operator",
    //     "Value":"value",
    //     "Logical Operator":"logicalOperator",
    //     "Type":"type",

    //     "Position Begin":"positionBegin",
    //     "Position End":"positionEnd",
    //     "Column Name":"columnHeader",
    //     "Constant":"constantValue",
    //     "Zero Fill":"zeroFill",
    //     "Date Format":"dateFormat",
    //     "Time Format":"timeFormat",
    //     "Amount Format":"amountFormat",
    //     "Skip Column":"skipColumn"
       
    // }];
 

    JSONToCSVConverter( ReportTitle, ShowLabel){
        console.log('json to csv converter');
        const templateColumnsJSON = [];
       

        templateColumnsJSON.push( Object.assign({}, this.fileTemplateJSON['com.nspl.app.domain.FileTemplates'][0], this.fileTemplateLinesJSON['com.nspl.app.domain.FileTemplateLines'][0]
        ,this.intermediateJSON['com.nspl.app.domain.IntermediateTable'][0],this.rowconditionsJSON['com.nspl.app.domain.RowConditions'][0])); //concat all json objects
        this.templateColumnsJSON = templateColumnsJSON;
        //console.log('templateColumnsJSON'+JSON.stringify(templateColumnsJSON));
        const JSONData: any =templateColumnsJSON;
        //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
        const arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
         //   console.log('arrData'+JSON.stringify(arrData));
        let CSV = '';    
        //Set Report title in first row or line
        
        /* CSV += ReportTitle + '\r\n\n'; */
    
        //This condition will generate the Label/Header
        if (ShowLabel) {
            var row = "";
            
            //This loop will extract the label from 1st index of on array
            if(arrData[0]){
                for (var index in arrData[0]) {
                    if (arrData[0].hasOwnProperty(index)) {
                        //Now convert each value to string and comma-seprated
                        row += index + ',';
                    }
                   
                }
            }
            
    
            row = row.slice(0, -1);
            
            //append Label row with line break
            CSV += row + '\r\n';
        }
        
        //1st loop is to extract each row
        for (var i = 0; i < arrData.length; i++) {
            var row = "";
            
            //2nd loop will extract each column and convert it in string comma-seprated
           // console.log('arrData[i]'+JSON.stringify(arrData[i]));
            // for (var c=0;c<arrData[i].key;c++) {
            //     console.log('arrData[i][c]'+arrData[i][c]);
            //     row += '"' + arrData[i][c] + '",';
            // }
            Object.keys(arrData[i]).forEach((key) => {
                //console.log('key'+key);
                row += '"' + key + '",';
            });
    
            row.slice(0, row.length - 1);
            
            //add a line break after each row
            CSV += row + '\r\n';
        }
    
        if (CSV == '') {
            alert("Invalid data");
            return;
        }
        
        //Generate a file name
        var fileName = "";
        //this will remove the blank-spaces from the title and replace it with an underscore
        fileName += ReportTitle.replace(/ /g,"_");
        
        //Initialize file format you want csv or xls
        var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
        
        // Now the little tricky part.
        // you can use either>> window.open(uri);
        // but this will not work in some browsers
        // or you will not get the correct file extension    
        
        //this trick will generate a temp <a /> tag
        var link = document.createElement("a");
        link.href = uri;
        
        //set the visibility hidden so it will not effect on your web-layout
        // link.style = "visibility:hidden";
        link.download = fileName + ".csv";
        
        //this part will append the anchor tag and remove it after automatic click
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
     public fileOverBase(file: File): void {
        if (file){
            this.hasBaseDropZoneOver = true;
        }
            
        //console.log('hasBaseDropZoneOver'+this.hasBaseDropZoneOver);
        //console.log('uploader?.queue?.length'+this.uploader.queue.length);
    }
   
    public onFileDrop(filelist: FileList): void {
        //header validation
        this.domainAndColumnsList = [];
        this.domainAndColumnsList.push( this.fileTemplateJSON);
        this.domainAndColumnsList.push( this.intermediateJSON);
        this.domainAndColumnsList.push( this.rowconditionsJSON);
        this.domainAndColumnsList.push( this.fileTemplateLinesJSON);
        
        //console.log('domainAndColumnsList'+JSON.stringify(this.domainAndColumnsList));
         let fileItem: FileItem = this.uploader.queue[this.uploader.queue.length - 1];
            this.uploader.queue = [];
            this.uploader.queue[0] = fileItem;
            if (filelist && filelist.length > 0) {
              
                this.filesAdded = filelist;
                let file: File = filelist[0];
              
                let domainNames : any =['com.nspl.app.domain.FileTemplates',
                                        'com.nspl.app.domain.FileTemplateLines',
                                        'com.nspl.app.domain.intermediateTable',
                                        'com.nspl'];

                // this.fileTemplatesService.loadTemplates(file, this.domainAndColumnsList,domainNames).subscribe((res: any) => {
                //     let resp=res;
                //     console.log('response after bulk upload is'+JSON.stringify(res));
                // });
        }
        this.convertFile();
    }

    public csvJSON(csv) {
        var lines = csv.split("\n");
    
        var result = [];
    
        var headers = lines[0].split(",");
        let linesList =[];
        let previousHeader ='';
        var occured =false;
        for (var i = 1; i < lines.length;) {
            var currentline = lines[i].split(",");
          
            // if(i==1)
            // {
            //     linesList.push(currentline);
            //     obj = this.buildJSONFromLine(headers,currentline);
            //     result.push(obj);
            //     i++;
            // }
            // else{
            //     var previousline = lines[i-1].split(",");
               
            //     var currentName :string= currentline[0];
              
            //     var previousName :string= previousline[0];
               
            //     if(i>1 && currentName === previousName)
            //     {
            //         linesList.push(currentline);
            //         occured=true;
            //         i++;
            //     }
            //     else{
            //         var obj = {};
            //         obj = this.buildJSONFromLine(headers,currentline);
            //         let previousLine : any;
            //         previousLine = result[result.length-1];
            //         previousLine['child']= [];
            //         for(var c=0;c<linesList.length;c++)
            //         {
            //             let childObj = {};
            //             childObj = this.buildJSONFromLine(headers,linesList[c]);
            //             previousLine['child'].push(childObj);
                        
            //         }
            //         linesList=[];
            //         result.push(obj);
            //         if(occured)
            //         {
            //            // i--;
            //             occured=false;
            //             i++;
            //         }
            //         else
            //         i++;
            //     } 
            // }
            var obj:any= {};
            var line= {};
            line = lines[i].split(",");
            // for (var j = 0; j < headers.length; j++) {
            //     obj[headers[j]] = line[j];
            // }
            obj = this.buildJSONFromLine(headers,currentline);
          //  let jsonObj: object = JSON.parse(obj+'');
         // obj = Object.setPrototypeOf(obj, FinalJSONModel);
         // console.log('heyyyy'+JSON.stringify(obj));
           // let jsonConvert: JsonConvert = new JsonConvert();
            console.log('obj before has'+JSON.stringify(obj));
            //let finalObj: FinalJSONModel = jsonConvert.deserializeObject(obj, FinalJSONModel);
            
           // let foo:FinalJSONModel = Object.assign(new FinalJSONModel(), obj);
           // console.log('convertedline'+JSON.stringify(foo));
            // let convertedline :FinalJSONModel ;
            // console.log('convertedline before'+JSON.stringify(new FinalJSONModel()));
            // convertedline = <FinalJSONModel>obj;
            // console.log('convertedline'+JSON.stringify(convertedline));
            result.push(obj);
            //rule-group-approvals.component.t
            i++;
        }
        //return result; //JavaScript object
        return JSON.stringify(result); //JSON
    }
    buildJSONFromLine(headers,currentline): any {
        var obj = {};
        for (var j = 0; j < headers.length; j++) {
            if(j < Object.keys( this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0]).length ) {    
                let domainObj = this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0];
                for (var key in domainObj) {
                    if (domainObj.hasOwnProperty(key) && headers[j] == key ) {
                      obj[domainObj[key]] = currentline[j];
                    }
                  }   
            }  else if( j>= Object.keys(this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0]).length
                 && j < (Object.keys(this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0]).length
             + Object.keys(this.domainAndColumnsList[1]['com.nspl.app.domain.IntermediateTable'][0]).length )){
                let domainObj = this.domainAndColumnsList[1]['com.nspl.app.domain.IntermediateTable'][0];
                for (var key in domainObj) {
                    if (domainObj.hasOwnProperty(key) && headers[j] == key ) {
                      obj[domainObj[key]] = currentline[j];
                    }
                  }
            }  else if(j>=(Object.keys(this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0]).length
            + Object.keys(this.domainAndColumnsList[1]['com.nspl.app.domain.IntermediateTable'][0]).length)
            
            && 
             
             j < (this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0].length 
             +this.domainAndColumnsList[1]['com.nspl.app.domain.IntermediateTable'][0].length 
            +this.domainAndColumnsList[2]['com.nspl.app.domain.RowConditions'][0].length))  {
                let domainObj = this.domainAndColumnsList[2]['com.nspl.app.domain.RowConditions'][0];
                for (var key in domainObj) {
                    if (domainObj.hasOwnProperty(key) && headers[j] == key ) {
                      obj[domainObj[key]] = currentline[j];
                    }
                  }
            }  else if( j >= (Object.keys(this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0]).length
                     +Object.keys(this.domainAndColumnsList[1]['com.nspl.app.domain.IntermediateTable'][0]).length 
                     +Object.keys(this.domainAndColumnsList[2]['com.nspl.app.domain.RowConditions'][0]).length) 
                     && j < (Object.keys(this.domainAndColumnsList[0]['com.nspl.app.domain.FileTemplates'][0]).length 
                    +Object.keys(this.domainAndColumnsList[1]['com.nspl.app.domain.IntermediateTable'][0]).length 
                    +Object.keys(this.domainAndColumnsList[2]['com.nspl.app.domain.RowConditions'][0]).length+
                    Object.keys(this.domainAndColumnsList[3]['com.nspl.app.domain.FileTemplateLines'][0]).length) ) {
                let domainObj = this.domainAndColumnsList[3]['com.nspl.app.domain.FileTemplateLines'][0];
                for (var key in domainObj) {
                    if (domainObj.hasOwnProperty(key) && headers[j] == key ) {
                      obj[domainObj[key]] = currentline[j];
                    }
                  }
            }
           
        }
       // obj['child']= linesList;
        return obj;
    }
     convertFile = () => {
     //   const input = document.getElementById('fileInput');
      
        const reader = new FileReader();
        reader.onload = () => {
          let text = reader.result;
          //console.log('CSV: ', text.substring(0, 100) + '...');
          
          //convert text to json here
          const json = this.csvJSON(text);
         // console.log('json before stringify'+(json));
         this.buildLines(json);
         // console.log('converted json has'+JSON.stringify(json));
        };
        reader.readAsText(this.filesAdded[0]);
      };
      buildLines(json)  {
          /**
           * loop through that input jsons set and for every line prepare a map with parent values
           */
          let linesFetched: any =[];
          linesFetched = json;
          
      }
      loadDefaultTemplates(){
        this.fileTemplatesService.loadDefaultTemplates().subscribe(
            ( res: any ) => {
                const resp = res;
                console.log('response on loading default templates '+JSON.stringify(resp));
                this.ngOnInit();
            });
      }
     
      addPaginator(){
          this.showPaginator = !this.showPaginator;
          $(".paginator-class").css("background-color",'#efeeee');
          $(".paginator-class").css("white-space","nowrap");
      
  
      }
     
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
    }
