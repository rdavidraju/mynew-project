/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AcctRuleDerivationsDetailComponent } from '../../../../../../main/webapp/app/entities/acct-rule-derivations/acct-rule-derivations-detail.component';
import { AcctRuleDerivationsService } from '../../../../../../main/webapp/app/entities/acct-rule-derivations/acct-rule-derivations.service';
import { AcctRuleDerivations } from '../../../../../../main/webapp/app/entities/acct-rule-derivations/acct-rule-derivations.model';

describe('Component Tests', () => {

    describe('AcctRuleDerivations Management Detail Component', () => {
        let comp: AcctRuleDerivationsDetailComponent;
        let fixture: ComponentFixture<AcctRuleDerivationsDetailComponent>;
        let service: AcctRuleDerivationsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [AcctRuleDerivationsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AcctRuleDerivationsService,
                    JhiEventManager
                ]
            }).overrideTemplate(AcctRuleDerivationsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AcctRuleDerivationsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AcctRuleDerivationsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AcctRuleDerivations(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.acctRuleDerivations).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
