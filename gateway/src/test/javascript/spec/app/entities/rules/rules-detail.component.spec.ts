// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { RulesDetailComponent } from '../../../../../../main/webapp/app/entities/rules/rules-detail.component';
// import { RulesService } from '../../../../../../main/webapp/app/entities/rules/rules.service';
// import { Rules } from '../../../../../../main/webapp/app/entities/rules/rules.model';

// describe('Component Tests', () => {

//     describe('Rules Management Detail Component', () => {
//         let comp: RulesDetailComponent;
//         let fixture: ComponentFixture<RulesDetailComponent>;
//         let service: RulesService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [RulesDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     RulesService,
//                     JhiEventManager
//                 ]    
//             }).overrideTemplate(RulesDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(RulesDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(RulesService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new Rules(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.rules).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
