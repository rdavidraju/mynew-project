/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcessDetailComponent } from '../../../../../../main/webapp/app/entities/process/process-detail.component';
import { ProcessService } from '../../../../../../main/webapp/app/entities/process/process.service';
import { Process } from '../../../../../../main/webapp/app/entities/process/process.model';

describe('Component Tests', () => {

    describe('Process Management Detail Component', () => {
        let comp: ProcessDetailComponent;
        let fixture: ComponentFixture<ProcessDetailComponent>;
        let service: ProcessService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [ProcessDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcessService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcessDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcessDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcessService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Process(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.process).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
