import { Component, OnInit, OnDestroy,Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { FileTemplates } from './file-templates.model';
import { FileTemplatesService } from './file-templates.service';
import { FileSelectDirective, FileDropDirective, FileUploader } from 'ng2-file-upload/ng2-file-upload';
import {MdProgressSpinnerModule} from '@angular/material';
import { Response } from '@angular/http';
const URL='';
@Component({
    selector: 'drop-zone',
    templateUrl: './file-templates-dropZone.component.html'
})
export class FileTemplatesDropZoneComponent implements OnInit, OnDestroy {

    fileTemplates: FileTemplates;
    private subscription: any;
    private uploadvar:boolean=false;
    uploadedIcon:boolean=false;
    uploadStatus:boolean =false;
    public uploader:FileUploader =new FileUploader({url:URL});
    public hasBaseDropZoneOver:boolean=false;
    public hasAnotherDropZoneOver:boolean=false;
    uploadingProcess :any=false;
    files : FileList; 
    private file: File;
    filesFromButton : Array<File>;
    viewAction:boolean=false;
    templateName:any="shobha";
    active:any;
    desc:any;
    detailId:any ;
    constructor(
        private jhiLanguageService: JhiLanguageService,
        private fileTemplatesService: FileTemplatesService,
        private route: ActivatedRoute
    ) {
        
       
    }
    
    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
              
        });
    }
    
    getFiles(event){ 
        this.files = event.target.files; 
        this.file = this.files[0];
        console.log(this.file);
        let msg:any;
        //this.filesFromButton.push(this.file);
        //console.log('file size in files from button arr:'+this.filesFromButton.length);
         this.fileTemplatesService.uploadFile(this.file).subscribe(
         (res:Response)=>{
            console.log('shobha'+res.status);
             msg= res.status;
             if(msg == 'success')
             {
                  this.uploadStatus = true;                 
                 }
             }
             );
    } 
  public onFileDrop(filelist: FileList): void {
      console.log('file dropped');
//      let msg:any;
//      if(filelist.length > 0) {
//            let file: File = filelist[0];
//        this.fileTemplatesService.fileUpload(file).subscribe(
//        (res:Response)=>{
//             msg= res.status;
//             if(msg == 'success')
//             {
//                  this.uploadedIcon = true;
//                 }
//             }
//             );
//          }
  }
    
    logForm(event) { 
    console.log('in log');
         console.log(this.files); 
    } 
    
    onChange(files) {
    console.log('on change');
    console.log(files);
  }
    load (id) {
        this.fileTemplatesService.find(id).subscribe(fileTemplates => {
            this.fileTemplates = fileTemplates;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        //this.subscription.unsubscribe();
    } 
    public fileOverBase(file:File):void{
        console.log('has file'+file);
        if(file)
        this.hasBaseDropZoneOver=true;
        console.log('hasBaseDropZoneOver'+this.hasBaseDropZoneOver);
        console.log('uploader?.queue?.length'+this.uploader.queue.length);
    }
    
    public fileOverAnother(e:any):void{
        this.hasAnotherDropZoneOver=e;
    }
    close()
    {
        console.log('close');
         this.uploadvar = false;
        }

    uploadFile(event)
    {
       
         console.log('uploading');
        this.uploadvar = true;
        console.log('uploader has'+this.uploader.queue);
        //this.uploader.queue[].upload();
       let files:any[];
        for(var i =0;i<this.uploader.queue.length;i++)
        {
           // console.log(i);
            console.log('this.uploader.queue[i]url'+ this.uploader.queue[i].url);
            //files.push(this.uploader.queue[i]);
            // console.log('fileslength'+files.length);
        this.fileTemplatesService.uploadFile(this.uploader.queue[i]).subscribe(
         
             );
            }
        }
    /**
     * Author : Shobha
     * fetch template by id
     */
//    fetchTemplateById(id:any)
//    {
//        console.log('fetch by id'+id);
//        this.fileTemplatesService.fetchById(id).subscribe(
//       (res:FileTemplates)=>{
//             console.log('shobhs'+JSON.stringify(res));
//           this.fileTemplates = res;
//           this.templateName = this.fileTemplates.templateName;
//           this.desc = this.fileTemplates.description;
//           this.active=this.fileTemplates.status;
//           console.log('this.fileTemplates'+this.templateName + this.desc+this.active);
//           }
//            );
//    }        
}
