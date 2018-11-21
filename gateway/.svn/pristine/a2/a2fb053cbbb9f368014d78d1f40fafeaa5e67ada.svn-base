/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FunctionalityDetailComponent } from '../../../../../../main/webapp/app/entities/functionality/functionality-detail.component';
import { FunctionalityService } from '../../../../../../main/webapp/app/entities/functionality/functionality.service';
import { Functionality } from '../../../../../../main/webapp/app/entities/functionality/functionality.model';

describe('Component Tests', () => {

    describe('Functionality Management Detail Component', () => {
        let comp: FunctionalityDetailComponent;
        let fixture: ComponentFixture<FunctionalityDetailComponent>;
        let service: FunctionalityService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [FunctionalityDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FunctionalityService,
                    JhiEventManager
                ]
            }).overrideTemplate(FunctionalityDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FunctionalityDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FunctionalityService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Functionality(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.functionality).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
