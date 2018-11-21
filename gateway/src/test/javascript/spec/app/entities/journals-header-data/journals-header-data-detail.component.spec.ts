/* tslint:disable max-line-length */
//import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
//import { OnInit } from '@angular/core';
//import { DatePipe } from '@angular/common';
//import { ActivatedRoute } from '@angular/router';
//import { Observable } from 'rxjs/Rx';
//import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
//import { AgreeGatewayV1TestModule } from '../../../test.module';
//import { MockActivatedRoute } from '../../../helpers/mock-route.service';
//import { JournalsHeaderDataDetailComponent } from '../../../../../../main/webapp/app/entities/journals-header-data/journals-header-data-detail.component';
//import { JournalsHeaderDataService } from '../../../../../../main/webapp/app/entities/journals-header-data/journals-header-data.service';
//import { JournalsHeaderData } from '../../../../../../main/webapp/app/entities/journals-header-data/journals-header-data.model';
//
//describe('Component Tests', () => {
//
//    describe('JournalsHeaderData Management Detail Component', () => {
//        let comp: JournalsHeaderDataDetailComponent;
//        let fixture: ComponentFixture<JournalsHeaderDataDetailComponent>;
//        let service: JournalsHeaderDataService;
//
//        beforeEach(async(() => {
//            TestBed.configureTestingModule({
//                imports: [AgreeGatewayV1TestModule],
//                declarations: [JournalsHeaderDataDetailComponent],
//                providers: [
//                    JhiDateUtils,
//                    JhiDataUtils,
//                    DatePipe,
//                    {
//                        provide: ActivatedRoute,
//                        useValue: new MockActivatedRoute({id: 123})
//                    },
//                    JournalsHeaderDataService,
//                    JhiEventManager
//                ]
//            }).overrideTemplate(JournalsHeaderDataDetailComponent, '')
//            .compileComponents();
//        }));
//
//        beforeEach(() => {
//            fixture = TestBed.createComponent(JournalsHeaderDataDetailComponent);
//            comp = fixture.componentInstance;
//            service = fixture.debugElement.injector.get(JournalsHeaderDataService);
//        });
//
//        describe('OnInit', () => {
//            it('Should call load all on init', () => {
//            // GIVEN
//
//            spyOn(service, 'find').and.returnValue(Observable.of(new JournalsHeaderData(10)));
//
//            // WHEN
//            comp.ngOnInit();
//
//            // THEN
//            expect(service.find).toHaveBeenCalledWith(123);
//            expect(comp.journalsHeaderData).toEqual(jasmine.objectContaining({id: 10}));
//            });
//        });
//    });
//
//});
