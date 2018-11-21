/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PeriodsDetailComponent } from '../../../../../../main/webapp/app/entities/periods/periods-detail.component';
import { PeriodsService } from '../../../../../../main/webapp/app/entities/periods/periods.service';
import { Periods } from '../../../../../../main/webapp/app/entities/periods/periods.model';

describe('Component Tests', () => {

    describe('Periods Management Detail Component', () => {
        let comp: PeriodsDetailComponent;
        let fixture: ComponentFixture<PeriodsDetailComponent>;
        let service: PeriodsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [PeriodsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PeriodsService,
                    JhiEventManager
                ]
            }).overrideTemplate(PeriodsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PeriodsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeriodsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Periods(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.periods).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
