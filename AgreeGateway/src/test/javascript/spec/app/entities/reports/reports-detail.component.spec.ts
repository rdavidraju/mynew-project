///* tslint:disable max-line-length */
//import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
//import { OnInit } from '@angular/core';
//import { DatePipe } from '@angular/common';
//import { ActivatedRoute } from '@angular/router';
//import { Observable } from 'rxjs/Rx';
//import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
//import { AgreeGatewayV1TestModule } from '../../../test.module';
//import { MockActivatedRoute } from '../../../helpers/mock-route.service';
//import { ReportsDetailComponent } from '../../../../../../main/webapp/app/entities/reports/reports-detail.component';
//import { ReportsService } from '../../../../../../main/webapp/app/entities/reports/reports.service';
//import { Reports } from '../../../../../../main/webapp/app/entities/reports/reports.model';
//
//describe('Component Tests', () => {
//
//    describe('Reports Management Detail Component', () => {
//        let comp: ReportsDetailComponent;
//        let fixture: ComponentFixture<ReportsDetailComponent>;
//        let service: ReportsService;
//
//        beforeEach(async(() => {
//            TestBed.configureTestingModule({
//                imports: [AgreeGatewayV1TestModule],
//                declarations: [ReportsDetailComponent],
//                providers: [
//                    JhiDateUtils,
//                    JhiDataUtils,
//                    DatePipe,
//                    {
//                        provide: ActivatedRoute,
//                        useValue: new MockActivatedRoute({id: 123})
//                    },
//                    ReportsService,
//                    JhiEventManager
//                ]
//            }).overrideTemplate(ReportsDetailComponent, '')
//            .compileComponents();
//        }));
//
//        beforeEach(() => {
//            fixture = TestBed.createComponent(ReportsDetailComponent);
//            comp = fixture.componentInstance;
//            service = fixture.debugElement.injector.get(ReportsService);
//        });
//
//        describe('OnInit', () => {
//            it('Should call load all on init', () => {
//            // GIVEN
//
//            spyOn(service, 'find').and.returnValue(Observable.of(new Reports(10)));
//
//            // WHEN
//            comp.ngOnInit();
//
//            // THEN
//            expect(service.find).toHaveBeenCalledWith(123);
//            expect(comp.reports).toEqual(jasmine.objectContaining({id: 10}));
//            });
//        });
//    });
//
//});
