/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CalendarDetailComponent } from '../../../../../../main/webapp/app/entities/calendar/calendar-detail.component';
import { CalendarService } from '../../../../../../main/webapp/app/entities/calendar/calendar.service';
import { Calendar } from '../../../../../../main/webapp/app/entities/calendar/calendar.model';

describe('Component Tests', () => {

    describe('Calendar Management Detail Component', () => {
        let comp: CalendarDetailComponent;
        let fixture: ComponentFixture<CalendarDetailComponent>;
        let service: CalendarService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [CalendarDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CalendarService,
                    JhiEventManager
                ]
            }).overrideTemplate(CalendarDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CalendarDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CalendarService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Calendar(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.calendar).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
