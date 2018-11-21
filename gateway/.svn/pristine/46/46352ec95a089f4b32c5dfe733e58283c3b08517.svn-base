/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JeLdrDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/je-ldr-details/je-ldr-details-detail.component';
import { JeLdrDetailsService } from '../../../../../../main/webapp/app/entities/je-ldr-details/je-ldr-details.service';
import { JeLdrDetails } from '../../../../../../main/webapp/app/entities/je-ldr-details/je-ldr-details.model';

describe('Component Tests', () => {

    describe('JeLdrDetails Management Detail Component', () => {
        let comp: JeLdrDetailsDetailComponent;
        let fixture: ComponentFixture<JeLdrDetailsDetailComponent>;
        let service: JeLdrDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [JeLdrDetailsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JeLdrDetailsService,
                    JhiEventManager
                ]
            }).overrideTemplate(JeLdrDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JeLdrDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JeLdrDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JeLdrDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jeLdrDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
