/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AcctRuleConditionsDetailComponent } from '../../../../../../main/webapp/app/entities/acct-rule-conditions/acct-rule-conditions-detail.component';
import { AcctRuleConditionsService } from '../../../../../../main/webapp/app/entities/acct-rule-conditions/acct-rule-conditions.service';
import { AcctRuleConditions } from '../../../../../../main/webapp/app/entities/acct-rule-conditions/acct-rule-conditions.model';

describe('Component Tests', () => {

    describe('AcctRuleConditions Management Detail Component', () => {
        let comp: AcctRuleConditionsDetailComponent;
        let fixture: ComponentFixture<AcctRuleConditionsDetailComponent>;
        let service: AcctRuleConditionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [AcctRuleConditionsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AcctRuleConditionsService,
                    JhiEventManager
                ]
            }).overrideTemplate(AcctRuleConditionsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AcctRuleConditionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AcctRuleConditionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AcctRuleConditions(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.acctRuleConditions).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
