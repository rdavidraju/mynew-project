// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { FileTemplatesDetailComponent } from '../../../../../../main/webapp/app/entities/file-templates/file-templates-detail.component';
// import { FileTemplatesService } from '../../../../../../main/webapp/app/entities/file-templates/file-templates.service';
// import { FileTemplates } from '../../../../../../main/webapp/app/entities/file-templates/file-templates.model';

// describe('Component Tests', () => {

//     describe('FileTemplates Management Detail Component', () => {
//         let comp: FileTemplatesDetailComponent;
//         let fixture: ComponentFixture<FileTemplatesDetailComponent>;
//         let service: FileTemplatesService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [FileTemplatesDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     FileTemplatesService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(FileTemplatesDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(FileTemplatesDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(FileTemplatesService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new FileTemplates(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.fileTemplates).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
