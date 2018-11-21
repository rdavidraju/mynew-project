/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ChartOfAccountDetailComponent } from '../../../../../../main/webapp/app/entities/chart-of-account/chart-of-account-detail.component';
import { ChartOfAccountService } from '../../../../../../main/webapp/app/entities/chart-of-account/chart-of-account.service';
import { ChartOfAccount } from '../../../../../../main/webapp/app/entities/chart-of-account/chart-of-account.model';

describe('Component Tests', () => {

    describe('ChartOfAccount Management Detail Component', () => {
        let comp: ChartOfAccountDetailComponent;
        let fixture: ComponentFixture<ChartOfAccountDetailComponent>;
        let service: ChartOfAccountService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [ChartOfAccountDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ChartOfAccountService,
                    JhiEventManager
                ]
            }).overrideTemplate(ChartOfAccountDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChartOfAccountDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChartOfAccountService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ChartOfAccount(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.chartOfAccount).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
