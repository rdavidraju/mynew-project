/* tslint:disable max-line-length 
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AgreeGatewayV1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RuleGroupDetailComponent } from '../../../../../../main/webapp/app/entities/rule-group/rule-group-detail.component';
import { RuleGroupService } from '../../../../../../main/webapp/app/entities/rule-group/rule-group.service';
import { RuleGroup } from '../../../../../../main/webapp/app/entities/rule-group/rule-group.model';

describe('Component Tests', () => {

    describe('RuleGroup Management Detail Component', () => {
        let comp: RuleGroupDetailComponent;
        let fixture: ComponentFixture<RuleGroupDetailComponent>;
        let service: RuleGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AgreeGatewayV1TestModule],
                declarations: [RuleGroupDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RuleGroupService,
                    JhiEventManager
                ]
            }).overrideTemplate(RuleGroupDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RuleGroupDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RuleGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RuleGroup(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ruleGroup).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
*/