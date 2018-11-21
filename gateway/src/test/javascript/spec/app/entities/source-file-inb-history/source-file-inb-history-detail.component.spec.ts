// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// //import { SourceFileInbHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/source-file-inb-history/source-file-inb-history-detail.component';
// import { SourceFileInbHistoryService } from '../../../../../../main/webapp/app/entities/source-file-inb-history/source-file-inb-history.service';
// import { SourceFileInbHistory } from '../../../../../../main/webapp/app/entities/source-file-inb-history/source-file-inb-history.model';

// describe('Component Tests', () => {

//     describe('SourceFileInbHistory Management Detail Component', () => {
//         let comp: SourceFileInbHistoryDetailComponent;
//         let fixture: ComponentFixture<SourceFileInbHistoryDetailComponent>;
//         let service: SourceFileInbHistoryService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [SourceFileInbHistoryDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     SourceFileInbHistoryService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(SourceFileInbHistoryDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(SourceFileInbHistoryDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(SourceFileInbHistoryService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new SourceFileInbHistory(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.sourceFileInbHistory).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
