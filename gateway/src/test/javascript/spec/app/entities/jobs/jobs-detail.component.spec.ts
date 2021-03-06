///* tslint:disable max-line-length */
//import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
//import { OnInit } from '@angular/core';
//import { DatePipe } from '@angular/common';
//import { ActivatedRoute } from '@angular/router';
//import { Observable } from 'rxjs/Rx';
//import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
//import { AgreeGatewayV1TestModule } from '../../../test.module';
//import { MockActivatedRoute } from '../../../helpers/mock-route.service';
//import { JobsDetailComponent } from '../../../../../../main/webapp/app/entities/jobs/jobs-detail.component';
//import { JobsService } from '../../../../../../main/webapp/app/entities/jobs/jobs.service';
//import { Jobs } from '../../../../../../main/webapp/app/entities/jobs/jobs.model';
//
//describe('Component Tests', () => {
//
//    describe('Jobs Management Detail Component', () => {
//        let comp: JobsDetailComponent;
//        let fixture: ComponentFixture<JobsDetailComponent>;
//        let service: JobsService;
//
//        beforeEach(async(() => {
//            TestBed.configureTestingModule({
//                imports: [AgreeGatewayV1TestModule],
//                declarations: [JobsDetailComponent],
//                providers: [
//                    JhiDateUtils,
//                    JhiDataUtils,
//                    DatePipe,
//                    {
//                        provide: ActivatedRoute,
//                        useValue: new MockActivatedRoute({id: 123})
//                    },
//                    JobsService,
//                    JhiEventManager
//                ]
//            }).overrideTemplate(JobsDetailComponent, '')
//            .compileComponents();
//        }));
//
//        beforeEach(() => {
//            fixture = TestBed.createComponent(JobsDetailComponent);
//            comp = fixture.componentInstance;
//            service = fixture.debugElement.injector.get(JobsService);
//        });
//
//        describe('OnInit', () => {
//            it('Should call load all on init', () => {
//            // GIVEN
//
//            spyOn(service, 'find').and.returnValue(Observable.of(new Jobs(10)));
//
//            // WHEN
//            comp.ngOnInit();
//
//            // THEN
//            expect(service.find).toHaveBeenCalledWith(123);
//            expect(comp.jobs).toEqual(jasmine.objectContaining({id: 10}));
//            });
//        });
//    });
//
//});
