/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SegmentsDetailComponent } from '../../../../../../main/webapp/app/entities/segments/segments-detail.component';
import { SegmentsService } from '../../../../../../main/webapp/app/entities/segments/segments.service';
import { Segments } from '../../../../../../main/webapp/app/entities/segments/segments.model';

describe('Component Tests', () => {

    describe('Segments Management Detail Component', () => {
        let comp: SegmentsDetailComponent;
        let fixture: ComponentFixture<SegmentsDetailComponent>;
        let service: SegmentsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [SegmentsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SegmentsService,
                    JhiEventManager
                ]
            }).overrideTemplate(SegmentsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SegmentsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SegmentsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Segments(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.segments).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
