// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { SourceProfileUsagesDetailComponent } from '../../../../../../main/webapp/app/entities/source-profile-usages/source-profile-usages-detail.component';
// import { SourceProfileUsagesService } from '../../../../../../main/webapp/app/entities/source-profile-usages/source-profile-usages.service';
// import { SourceProfileUsages } from '../../../../../../main/webapp/app/entities/source-profile-usages/source-profile-usages.model';

// describe('Component Tests', () => {

//     describe('SourceProfileUsages Management Detail Component', () => {
//         let comp: SourceProfileUsagesDetailComponent;
//         let fixture: ComponentFixture<SourceProfileUsagesDetailComponent>;
//         let service: SourceProfileUsagesService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [SourceProfileUsagesDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     SourceProfileUsagesService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(SourceProfileUsagesDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(SourceProfileUsagesDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(SourceProfileUsagesService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new SourceProfileUsages(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.sourceProfileUsages).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
