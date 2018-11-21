/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcessesDetailComponent } from '../../../../../../main/webapp/app/entities/processes/processes-detail.component';
import { ProcessesService } from '../../../../../../main/webapp/app/entities/processes/processes.service';
import { Processes } from '../../../../../../main/webapp/app/entities/processes/processes.model';

describe('Component Tests', () => {

    describe('Processes Management Detail Component', () => {
        let comp: ProcessesDetailComponent;
        let fixture: ComponentFixture<ProcessesDetailComponent>;
        let service: ProcessesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [ProcessesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcessesService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcessesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcessesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcessesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Processes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.processes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
