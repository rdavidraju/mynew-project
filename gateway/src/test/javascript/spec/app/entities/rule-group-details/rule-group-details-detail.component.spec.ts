// /* tslint:disable max-line-length */
// import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
// import { OnInit } from '@angular/core';
// import { DatePipe } from '@angular/common';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs/Rx';
// import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
// import { AgreeGatewayV1TestModule } from '../../../test.module';
// import { MockActivatedRoute } from '../../../helpers/mock-route.service';
// import { RuleGroupDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/rule-group-details/rule-group-details-detail.component';
// import { RuleGroupDetailsService } from '../../../../../../main/webapp/app/entities/rule-group-details/rule-group-details.service';
// import { RuleGroupDetails } from '../../../../../../main/webapp/app/entities/rule-group-details/rule-group-details.model';

// describe('Component Tests', () => {

//     describe('RuleGroupDetails Management Detail Component', () => {
//         let comp: RuleGroupDetailsDetailComponent;
//         let fixture: ComponentFixture<RuleGroupDetailsDetailComponent>;
//         let service: RuleGroupDetailsService;

//         beforeEach(async(() => {
//             TestBed.configureTestingModule({
//                 imports: [AgreeGatewayV1TestModule],
//                 declarations: [RuleGroupDetailsDetailComponent],
//                 providers: [
//                     JhiDateUtils,
//                     JhiDataUtils,
//                     DatePipe,
//                     {
//                         provide: ActivatedRoute,
//                         useValue: new MockActivatedRoute({id: 123})
//                     },
//                     RuleGroupDetailsService,
//                     JhiEventManager
//                 ]
//             }).overrideTemplate(RuleGroupDetailsDetailComponent, '')
//             .compileComponents();
//         }));

//         beforeEach(() => {
//             fixture = TestBed.createComponent(RuleGroupDetailsDetailComponent);
//             comp = fixture.componentInstance;
//             service = fixture.debugElement.injector.get(RuleGroupDetailsService);
//         });

//         describe('OnInit', () => {
//             it('Should call load all on init', () => {
//             // GIVEN

//             spyOn(service, 'find').and.returnValue(Observable.of(new RuleGroupDetails(10)));

//             // WHEN
//             comp.ngOnInit();

//             // THEN
//             expect(service.find).toHaveBeenCalledWith(123);
//             expect(comp.ruleGroupDetails).toEqual(jasmine.objectContaining({id: 10}));
//             });
//         });
//     });

// });
