///* tslint:disable max-line-length */
//import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
//import { OnInit } from '@angular/core';
//import { DatePipe } from '@angular/common';
//import { ActivatedRoute } from '@angular/router';
//import { Observable } from 'rxjs/Rx';
//import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
//import { AgreeGatewayV1TestModule } from '../../../test.module';
//import { MockActivatedRoute } from '../../../helpers/mock-route.service';
//import { JobDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/job-details/job-details-detail.component';
//import { JobDetailsService } from '../../../../../../main/webapp/app/entities/job-details/job-details.service';
//import { JobDetails } from '../../../../../../main/webapp/app/entities/job-details/job-details.model';
//
//describe('Component Tests', () => {
//
//    describe('JobDetails Management Detail Component', () => {
//        let comp: JobDetailsDetailComponent;
//        let fixture: ComponentFixture<JobDetailsDetailComponent>;
//        let service: JobDetailsService;
//
//        beforeEach(async(() => {
//            TestBed.configureTestingModule({
//                imports: [AgreeGatewayV1TestModule],
//                declarations: [JobDetailsDetailComponent],
//                providers: [
//                    JhiDateUtils,
//                    JhiDataUtils,
//                    DatePipe,
//                    {
//                        provide: ActivatedRoute,
//                        useValue: new MockActivatedRoute({id: 123})
//                    },
//                    JobDetailsService,
//                    JhiEventManager
//                ]
//            }).overrideTemplate(JobDetailsDetailComponent, '')
//            .compileComponents();
//        }));
//
//        beforeEach(() => {
//            fixture = TestBed.createComponent(JobDetailsDetailComponent);
//            comp = fixture.componentInstance;
//            service = fixture.debugElement.injector.get(JobDetailsService);
//        });
//
//        describe('OnInit', () => {
//            it('Should call load all on init', () => {
//            // GIVEN
//
//            spyOn(service, 'find').and.returnValue(Observable.of(new JobDetails(10)));
//
//            // WHEN
//            comp.ngOnInit();
//
//            // THEN
//            expect(service.find).toHaveBeenCalledWith(123);
//            expect(comp.jobDetails).toEqual(jasmine.objectContaining({id: 10}));
//            });
//        });
//    });
//
//});
