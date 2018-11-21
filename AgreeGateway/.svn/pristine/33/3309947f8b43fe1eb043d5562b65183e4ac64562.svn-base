/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FxRatesDetailComponent } from '../../../../../../main/webapp/app/entities/fx-rates/fx-rates-detail.component';
import { FxRatesService } from '../../../../../../main/webapp/app/entities/fx-rates/fx-rates.service';
import { FxRates } from '../../../../../../main/webapp/app/entities/fx-rates/fx-rates.model';

describe('Component Tests', () => {

    describe('FxRates Management Detail Component', () => {
        let comp: FxRatesDetailComponent;
        let fixture: ComponentFixture<FxRatesDetailComponent>;
        let service: FxRatesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [FxRatesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FxRatesService,
                    JhiEventManager
                ]
            }).overrideTemplate(FxRatesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FxRatesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FxRatesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new FxRates(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.fxRates).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
