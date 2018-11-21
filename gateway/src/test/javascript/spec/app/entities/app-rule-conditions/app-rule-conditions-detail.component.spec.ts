/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AppRuleConditionsDetailComponent } from '../../../../../../main/webapp/app/entities/app-rule-conditions/app-rule-conditions-detail.component';
import { AppRuleConditionsService } from '../../../../../../main/webapp/app/entities/app-rule-conditions/app-rule-conditions.service';
import { AppRuleConditions } from '../../../../../../main/webapp/app/entities/app-rule-conditions/app-rule-conditions.model';

describe('Component Tests', () => {

    describe('AppRuleConditions Management Detail Component', () => {
        let comp: AppRuleConditionsDetailComponent;
        let fixture: ComponentFixture<AppRuleConditionsDetailComponent>;
        let service: AppRuleConditionsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [AppRuleConditionsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AppRuleConditionsService,
                    JhiEventManager
                ]
            }).overrideTemplate(AppRuleConditionsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AppRuleConditionsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AppRuleConditionsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new AppRuleConditions(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.appRuleConditions).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
